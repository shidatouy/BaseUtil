package com.base_util.picseleter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import androidx.core.content.FileProvider;

/**
 * 拍照 / 相册选图。
 *
 * 相比旧版的关键改动：
 * 1. 不再要求调用方先设置路径，目录都有默认值（应用私有目录下的 imageFile / compress），
 *    需要自定义时用 setImageDir / setCompressDir；
 * 2. 拍照结果通过 takeCameraResult(activity) 按 Activity 取回，不再是一个全局静态字符串，
 *    同时开两个页面拍照不会互相覆盖；
 * 3. 拍照前会把目标文件记进 onSaveInstanceState（BaseActivity 已自动调用），
 *    相机把宿主进程挤掉后回来仍能找到照片。
 */
public  class PicSelect {

    public static final int REQUEST_CAMERA = 1;
    public static final int CHOOSE_REQUEST = 2;

    private static final String KEY_PENDING = "com.base_util.picseleter.PENDING_CAMERA";

    /** 每个 Activity 正在进行的拍照任务，WeakHashMap 保证页面销毁后不泄漏 */
    private static final Map<Activity, File> PENDING = new WeakHashMap<>();

    private static String imageDirOverride;
    private static String compressDirOverride;

    // ==================== 目录 ====================

    /** 应用私有根目录，外部存储不可用时退回内部存储 */
    private static File rootDir(Context context) {
        File dir = context.getExternalFilesDir(null);
        return dir != null ? dir : context.getFilesDir();
    }

    /**
     * 应用私有根目录的绝对路径，默认 `Android/data/包名/files`。
     *
     * 外部存储不可用（未挂载）时退回内部存储的 `files` 目录，
     * 因此返回的路径在不同设备上可能不同，不要写死。
     */
    public static String getBaseDir(Context context) {
        return rootDir(context).getAbsolutePath();
    }

    /**
     * 拍照输出目录，默认 `Android/data/包名/files/imageFile`
     */
    public static File getImageDir(Context context) {
        return imageDirOverride == null
                ? new File(rootDir(context), "imageFile")
                : new File(imageDirOverride);
    }

    /** 自定义拍照输出目录，传 null 恢复默认 */
    public static void setImageDir(String path) {
        imageDirOverride = path;
    }

    /**
     * 压缩结果目录，默认 `Android/data/包名/files/compress`
     */
    public static File getCompressDir(Context context) {
        return compressDirOverride == null
                ? new File(rootDir(context), "compress")
                : new File(compressDirOverride);
    }

    /** 自定义压缩目录，传 null 恢复默认 */
    public static void setCompressDir(String path) {
        compressDirOverride = path;
    }

    // ==================== 拍照 ====================

    /**
     * 启动相机拍照，输出文件落在 [getImageDir]。
     *
     * <pre>
     * // 点击拍照
     * PicSelect.startCamera(this);
     *
     * // onActivityResult
     * if (requestCode == PicSelect.REQUEST_CAMERA) {
     *     File file = PicSelect.takeCameraResult(this);
     * }
     * </pre>
     *
     * @return 照片将要写入的 Uri；目录不可写或 provider 未声明时返回 null
     */
    public static Uri startCamera(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        File dir = getImageDir(activity);
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }
        File file = new File(dir, "img_" + System.currentTimeMillis() + ".jpg");
        Uri uri;
        try {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
        } catch (Exception e) {
            // 接入方没有在 manifest 里声明 ${applicationId}.provider
            e.printStackTrace();
            return null;
        }
        synchronized (PENDING) {
            PENDING.put(activity, file);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
        return uri;
    }

    /**
     * 取回拍照结果，取一次即清除（重复调用返回 null）。
     *
     * 用户取消拍照时也会返回文件，调用方自己判断 `file.length() > 0`。
     *
     * @return 拍照写入的文件，没有进行中的拍照时返回 null
     */
    public static File takeCameraResult(Activity activity) {
        if (activity == null) {
            return null;
        }
        synchronized (PENDING) {
            return PENDING.remove(activity);
        }
    }

    /**
     * 查看当前进行中的拍照文件，不删除
     */
    public static File peekCameraFile(Activity activity) {
        if (activity == null) {
            return null;
        }
        synchronized (PENDING) {
            return PENDING.get(activity);
        }
    }

    // ==================== 状态保存 ====================

    /**
     * 在 onSaveInstanceState 里调用，供进程被回收后恢复。
     * 继承 BaseActivity 的页面已自动调用，无需手动处理。
     */
    public static void saveState(Activity activity, Bundle outState) {
        if (activity == null || outState == null) {
            return;
        }
        File file = peekCameraFile(activity);
        if (file != null) {
            outState.putString(KEY_PENDING, file.getAbsolutePath());
        }
    }

    /**
     * 在 onCreate 里调用，恢复拍照中丢失的文件记录。
     * 继承 BaseActivity 的页面已自动调用。
     */
    public static void restoreState(Activity activity, Bundle savedInstanceState) {
        if (activity == null || savedInstanceState == null) {
            return;
        }
        String path = savedInstanceState.getString(KEY_PENDING);
        if (path == null) {
            return;
        }
        synchronized (PENDING) {
            PENDING.put(activity, new File(path));
        }
    }

    // ==================== 选图 ====================

    /**
     * 从系统相册选择一张图片。
     *
     * <pre>
     * // 点击选图
     * PicSelect.selectImage(this);
     *
     * // onActivityResult
     * if (requestCode == PicSelect.CHOOSE_REQUEST &amp;&amp; data != null) {
     *     String path = GetFilePathFromUri.getFileAbsolutePath(this, data.getData());
     * }
     * </pre>
     *
     * 注意：Android 13 起需要 READ_MEDIA_IMAGES，低版本需要 READ_EXTERNAL_STORAGE，
     * 权限由调用方申请。
     */
    public static void selectImage(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, CHOOSE_REQUEST);
    }
}
