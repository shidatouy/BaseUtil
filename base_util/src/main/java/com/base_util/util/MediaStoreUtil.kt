package com.base_util.util

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

/**
 * 图片文件工具：相册保存、Uri <-> 路径、Base64、EXIF 旋转等。
 *
 * 说明：
 * - Android 10（API 29）及以上走 MediaStore，无需存储权限；
 * - Android 9 及以下直接写公共目录，调用方需要已获得 WRITE_EXTERNAL_STORAGE。
 */
object MediaStoreUtil {

    private const val TAG = "MediaStoreUtil"

    // ==================== 保存到相册 ====================

    /**
     * 将 Bitmap 保存为 PNG 格式，并自动刷新媒体库
     *
     * Android 10 及以上使用 MediaStore 插入（自动出现在相册，不需要存储权限）；
     * Android 9 及以下写入公共目录后通过 MediaScannerConnection 扫描，
     * 此时调用方需要已经拿到 WRITE_EXTERNAL_STORAGE 权限。
     *
     * @param context 上下文
     * @param bitmap 要保存的位图
     * @param dirName 目录名，支持以下格式：
     *                - "wuliu"          → 自动补全为 "DCIM/wuliu"
     *                - "DCIM/wuliu"     → 原样使用
     *                - "Pictures/myapp" → 原样使用
     *                - "/DCIM/wuliu"    → 自动去掉开头的 "/"
     * @param fileName 文件名（如 IMG_20250405_1234.png）
     * @return 返回 Uri，可用于上传或预览；失败返回 null
     */
    @JvmOverloads
    fun saveImageToGallery(
        context: Context,
        bitmap: Bitmap,
        dirName: String,
        fileName: String = "IMG_${DateFormatUtil.dateToStr(Date(), "yyyyMMdd_HHmmss")}.png"
    ): Uri? {
        val relativePath = normalizeRelativePath(dirName)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveViaMediaStore(context, bitmap, relativePath, fileName)
        } else {
            saveViaFile(context, bitmap, relativePath, fileName)
        }
    }

    /** Android 10+：通过 MediaStore 插入，插入后即被媒体库收录 */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveViaMediaStore(
        context: Context,
        bitmap: Bitmap,
        relativePath: String,
        fileName: String
    ): Uri? {
        val resolver = context.contentResolver
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            // 写入期间对其他应用不可见，避免相册里出现半张图
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

        val uri = try {
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } catch (e: Exception) {
            Log.e(TAG, "insert media store failed", e)
            null
        } ?: return null

        return try {
            val output = resolver.openOutputStream(uri)
            if (output == null) throw IOException("openOutputStream returned null")
            output.use { os ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                    throw IOException("bitmap compress failed")
                }
                os.flush()
            }
            // 写入完成，解除 pending 状态
            val done = ContentValues().apply { put(MediaStore.MediaColumns.IS_PENDING, 0) }
            resolver.update(uri, done, null, null)
            uri
        } catch (e: Exception) {
            Log.e(TAG, "write image failed", e)
            // 清理失败留下的空记录
            runCatching { resolver.delete(uri, null, null) }
            null
        }
    }

    /** Android 9 及以下：直接写公共目录，再扫描媒体库 */
    private fun saveViaFile(
        context: Context,
        bitmap: Bitmap,
        relativePath: String,
        fileName: String
    ): Uri? {
        return try {
            val dir = File(Environment.getExternalStorageDirectory(), relativePath)
            if (!dir.exists() && !dir.mkdirs()) {
                Log.e(TAG, "create dir failed: ${dir.absolutePath}")
                return null
            }
            val file = File(dir, fileName)
            FileOutputStream(file).use { os ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)) {
                    throw IOException("bitmap compress failed")
                }
                os.flush()
            }
            MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null, null)
            Uri.fromFile(file)
        } catch (e: Exception) {
            Log.e(TAG, "save image to file failed", e)
            null
        }
    }

    /**
     * 规范化相对路径，确保符合 MediaStore 要求
     */
    private fun normalizeRelativePath(dirName: String): String {
        var path = dirName.trim()

        // 1. 去掉开头的 "/"
        if (path.startsWith("/")) {
            path = path.substring(1)
        }

        // 2. 去掉结尾的 "/"
        if (path.endsWith("/")) {
            path = path.substring(0, path.length - 1)
        }

        // 3. 如果为空，用默认目录
        if (path.isEmpty()) {
            return "${Environment.DIRECTORY_DCIM}/MyApp"
        }

        // 4. 如果不含 "/"，说明只传了子目录名，自动补 DCIM/
        if (!path.contains("/")) {
            return "${Environment.DIRECTORY_DCIM}/$path"
        }

        // 5. 如果已含 "/"，说明传了完整路径，原样使用
        return path
    }

    /**
     * 刷新媒体库，确保文件出现在相册中。
     *
     * Android 10 及以上通过 MediaStore 插入的文件本身已被收录，无需再扫描；
     * 这里只处理低版本，或者你是用 File 方式写到公共目录的情况。
     *
     * @param context 上下文
     * @param filePath 文件的绝对路径
     */
    fun refreshMediaStore(context: Context, filePath: String?) {
        if (filePath.isNullOrEmpty()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) return
        try {
            MediaScannerConnection.scanFile(context, arrayOf(filePath), null, null)
        } catch (e: Exception) {
            Log.e(TAG, "scan file failed", e)
        }
    }

    /**
     * 尝试从 Uri 获取文件路径。
     *
     * 注意：MediaStore 的 DATA 列在 Android 10 起已废弃，且查询别人写入的文件会拿不到值，
     * 返回 null 属于正常情况，不要用它做业务判断。
     */
    @Suppress("DEPRECATION")
    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val dataIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
                if (dataIndex >= 0 && cursor.moveToFirst()) cursor.getString(dataIndex) else null
            }
        } catch (e: Exception) {
            Log.e(TAG, "query uri path failed", e)
            null
        }
    }

    /**
     * 查询相册里的图片，按添加时间倒序，返回 `content://` Uri 字符串。
     *
     * 需要 READ_EXTERNAL_STORAGE（Android 13 及以上为 READ_MEDIA_IMAGES）。
     *
     * 这里查的是 `_ID` 列再拼成 Uri，不用已废弃的 `DATA` 列 ——
     * `DATA` 在 Android 10 起可能返回 null，用它会出现"有图片却查不到"的情况。
     * 返回的 Uri 可以直接交给 Glide 加载；需要绝对路径时用 [getFilePathFromUri]。
     *
     * @param limit 最多返回多少张
     * @return 图片 Uri 列表，无权限或查询失败时返回空列表
     */
    @JvmStatic
    @JvmOverloads
    fun queryImages(context: Context, limit: Int = 50): List<String> {
        if (limit <= 0) return emptyList()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val result = ArrayList<String>()
        try {
            context.contentResolver.query(
                collection,
                arrayOf(MediaStore.Images.Media._ID),
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
            )?.use { cursor ->
                val idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                if (idIndex < 0) return emptyList()
                while (cursor.moveToNext() && result.size < limit) {
                    val id = cursor.getLong(idIndex)
                    if (id > 0) {
                        result.add(ContentUris.withAppendedId(collection, id).toString())
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "query images failed", e)
        }
        return result
    }

    // ==================== 应用私有目录 ====================

    /**
     * 获取应用私有目录，用于保存临时文件
     */
    fun getAppPrivateDir(context: Context): File? = context.getExternalFilesDir(null)

    /**
     * 获取自定义子目录（应用私有目录下），用于保存临时文件
     */
    fun getCustomSubDir(context: Context, subDirName: String): File? {
        val baseDir = context.getExternalFilesDir(null) ?: return null
        return File(baseDir, subDirName).apply {
            if (!exists()) mkdirs()
        }
    }

    // File.createTempFile(...) 是系统 API，会自动生成唯一的文件名；
    // createNewFile() 是确保文件真的被创建出来（有些情况可能已存在）；
    fun createTempImageFile(context: Context, prefix: String = "IMG_", suffix: String = ".png"): File? {
        val imageDir = getCustomSubDir(context, "imageFile") ?: return null
        return File.createTempFile(prefix, suffix, imageDir).apply {
            createNewFile()
        }
    }

    /**
     * 保存 Bitmap 到指定目录，并返回保存的文件
     */
    fun saveBitmapToFileWithName(
        context: Context,
        bitmap: Bitmap,
        subDirName: String = "imageFile",
        prefix: String = "IMG_",
        suffix: String = ".png"
    ): File? {
        val dir = getCustomSubDir(context, subDirName) ?: return null
        val fileName = "${prefix}${DateFormatUtil.dateToStr(Date(), "yyyyMMdd_HHmmss")}${suffix}"
        val file = File(dir, fileName)
        return if (saveBitmapToFile(file, bitmap)) file else null
    }

    // 因为你要把数据写入到文件中，所以使用的是 输出流（OutputStream）；
    // 如果是读取文件内容到内存中，才需要使用 输入流（InputStream）。
    fun saveBitmapToFile(file: File, bitmap: Bitmap): Boolean {
        return try {
            FileOutputStream(file).use { fos ->
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
                    throw IOException("bitmap compress failed")
                }
                fos.flush()
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "save bitmap to file failed: ${file.absolutePath}", e)
            false
        }
    }

    // ==================== Base64 ====================

    /**
     * Uri 转换为 Base64
     */
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                android.util.Base64.encodeToString(inputStream.readBytes(), android.util.Base64.DEFAULT)
            }
        } catch (e: Exception) {
            Log.e(TAG, "uri to base64 failed", e)
            null
        }
    }

    /**
     * File 转换为 Base64
     */
    fun fileToBase64(file: File): String? {
        return try {
            android.util.Base64.encodeToString(file.readBytes(), android.util.Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e(TAG, "file to base64 failed", e)
            null
        }
    }

    // ==================== View / 图片处理 ====================

    /**
     * View 转换为 Bitmap。
     * View 尚未完成测量时（宽高为 0）会先按父容器宽度测量一次，避免 createBitmap 抛异常。
     */
    fun loadBitmapFromView(view: View): Bitmap {
        var width = view.width
        var height = view.height
        if (width <= 0 || height <= 0) {
            val widthSpec = View.MeasureSpec.makeMeasureSpec(
                (view.layoutParams?.width ?: 0).coerceAtLeast(0),
                View.MeasureSpec.UNSPECIFIED
            )
            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            view.measure(widthSpec, heightSpec)
            width = view.measuredWidth.coerceAtLeast(1)
            height = view.measuredHeight.coerceAtLeast(1)
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.layout(0, 0, width, height)
        view.draw(canvas)
        return bitmap
    }

    /**
     * 读取图片的 EXIF 旋转角度。
     *
     * @param filePath 图片文件路径
     * @return 0 / 90 / 180 / 270
     */
    fun readPictureDegree(filePath: String?): Int {
        if (filePath.isNullOrEmpty()) return 0
        return try {
            degreeOf(ExifInterface(filePath))
        } catch (e: IOException) {
            Log.e(TAG, "read exif failed: $filePath", e)
            0
        }
    }

    /**
     * 读取图片的 EXIF 旋转角度。
     *
     * @param context 上下文
     * @param uri 图片 Uri（相册返回的 Uri 也可以）
     * @return 0 / 90 / 180 / 270
     */
    fun readPictureDegree(context: Context, uri: Uri): Int {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // API 24 起 ExifInterface 支持直接读 InputStream
                    degreeOf(ExifInterface(input))
                } else {
                    val temp = File.createTempFile("exif_", ".jpg", context.cacheDir)
                    try {
                        temp.outputStream().use { out -> input.copyTo(out) }
                        degreeOf(ExifInterface(temp.absolutePath))
                    } finally {
                        temp.delete()
                    }
                }
            } ?: 0
        } catch (e: Exception) {
            Log.e(TAG, "read exif from uri failed: $uri", e)
            0
        }
    }

    /**
     * 旧接口：Bitmap 本身不携带 EXIF 信息（压缩写入时会丢失），这里永远返回 0。
     * 请改用 [readPictureDegree] 的路径或 Uri 重载。
     */
    @Deprecated(
        "Bitmap 不携带 EXIF 信息，永远返回 0",
        ReplaceWith("readPictureDegree(filePath) 或 readPictureDegree(context, uri)")
    )
    @Suppress("UNUSED_PARAMETER")
    fun readPictureDegree(context: Context, bitmap: Bitmap): Int = 0

    private fun degreeOf(exif: ExifInterface): Int {
        return when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    /**
     * 对 bitmap 进行旋转
     */
    fun matrixBitmap(bitmap: Bitmap, i: Int): Bitmap? {
        return try {
            val matrix = Matrix()
            matrix.postRotate(i.toFloat())
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            Log.e(TAG, "rotate bitmap failed", e)
            null
        }
    }

    /**
     * 对 bitmap 进行旋转
     */
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees, width / 2f, height / 2f) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}
