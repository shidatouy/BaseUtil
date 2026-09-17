package com.base_util.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.base_util.util.DateFormatUtil
import com.qq.okhttp.OkHttpUtils2.put
import com.socks.library.KLog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files.exists
import java.util.*

object MediaStoreUtil {


    /**
     * 将 Bitmap 保存为 PNG 格式，并自动刷新媒体库
     *
     * @param context 上下文
     * @param bitmap 要保存的位图
     * @param dirName 目录名，支持以下格式：
     *                - "wuliu"          → 自动补全为 "DCIM/wuliu"
     *                - "DCIM/wuliu"     → 原样使用
     *                - "Pictures/myapp" → 原样使用
     *                - "/DCIM/wuliu"    → 自动去掉开头的 "/"
     * @param fileName 文件名（如 IMG_20250405_1234.png）
     * @return 返回 Uri，可用于上传或预览
     */
    fun saveImageToGallery(context: Context, bitmap: Bitmap, dirName: String , fileName: String = "IMG_${DateFormatUtil.dateToStr(Date(), "yyyyMMdd_HHmmss")}.png"): Uri? {
        val relativePath = normalizeRelativePath(dirName)
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        try {
            uri?.let {
                resolver.openOutputStream(it)?.use { os ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    os.flush()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        // 刷新媒体库
        uri?.let { refreshMediaStore(context, it) }

        return uri
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
     * 刷新媒体库，确保图片/视频出现在相册中
     *
     * @param context 上下文
     * @param uri 文件的 Uri（可以是图片、视频等）
     */
    fun refreshMediaStore(context: Context, uri: Uri) {
        try {
            // 推荐方式：使用 MediaScannerConnection 主动刷新
            val filePath = getFilePathFromUri(context, uri)
            if (!filePath.isNullOrEmpty()) {
                MediaScannerConnection.scanFile(context, arrayOf(filePath), null) { _, _ -> }
            } else {
                // 如果拿不到真实路径，尝试用广播刷新 Uri
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = uri
                context.sendBroadcast(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 尝试从 Uri 获取文件路径（仅用于刷新）
     */
     fun getFilePathFromUri(context: Context, uri: Uri): String? {
        return try {
            val resolver = context.contentResolver
            val cursor = resolver.query(uri, null, null, null, null)
            cursor?.use {
                val dataIndex = it.getColumnIndex(MediaStore.MediaColumns.DATA)
                if (dataIndex >= 0 && it.moveToFirst()) {
                    it.getString(dataIndex)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取应用私有目录，用于保存临时文件
     */
    fun getAppPrivateDir(context: Context): File? {
        return context.getExternalFilesDir(null)
    }

    /**
     * 获取自定义子目录，用于保存临时文件
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
    fun saveBitmapToFileWithName(context: Context, bitmap: Bitmap, subDirName: String = "imageFile", prefix: String = "IMG_", suffix: String = ".png"): File? {
        val dir = getCustomSubDir(context, subDirName) ?: return null
        val fileName = "${prefix}${DateFormatUtil.dateToStr(Date(), "yyyyMMdd_HHmmss")}${suffix}"
        val file = File(dir, fileName)
        return if (saveBitmapToFile(file, bitmap)) file else null
    }


    // 因为你要把数据写入到文件中，所以使用的是 输出流（OutputStream）；
    //  如果是读取文件内容到内存中，才需要使用 输入流（InputStream）。
    fun saveBitmapToFile(file: File, bitmap: Bitmap): Boolean {
        return try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.use { it.flush() }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



    /**
     * Uri 转换为 Base64
     */
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val bytes = inputStream.readBytes()
                android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    /**
     * File 转换为 Base64
     */
    fun fileToBase64(file: File): String? {
        return try {
            val bytes = file.readBytes()
            android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * View 转换为 Bitmap
     */
    fun loadBitmapFromView(view: View): Bitmap {
        val width = view.width
        val height = view.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.layout(0, 0, width, height)
        view.draw(canvas)
        return bitmap
    }


    /**
     * 读取图片旋转角度
     */
    fun readPictureDegree(context: Context,bitmap: Bitmap): Int {
        var degree = 0
        try {
            // 创建一个临时文件来保存 Bitmap
            val tempFile = File.createTempFile("temp", ".jpg", context.cacheDir)
            val fos = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

            // 使用 ExifInterface 读取旋转角度
            val exifInterface = ExifInterface(tempFile.absolutePath)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }

            // 删除临时文件
            tempFile.delete()
        } catch (e: IOException) {
            KLog.e("ReadPictureDegree", "IO Exception: " + e.message)
        }
        return degree
    }

    /**
     * 对bitmap进行旋转
     *
     * @param bitmap
     * @param i
     * @return
     */
    fun matrixBitmap(bitmap: Bitmap, i: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(i.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 对bitmap进行旋转
     */
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees, width / 2f, height / 2f) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }


}
