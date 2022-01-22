package com.base_util.picseleter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.base_util.util.Tools;

import java.io.File;
import java.io.IOException;

import androidx.core.content.FileProvider;
import id.zelory.compressor.Compressor;

public class PicSelect {
    public static String CAMEAR_PATH = "";
    public static String COMPRESS_PATH = "";//压缩路径
    public static final int REQUEST_CAMERA = 1;
    public static final int CHOOSE_REQUEST = 2;


    /**
     * 单独拍照
     *
     * @param activity
     */
    public static void getCamereAc(Activity activity) {
        String fileName = "img_" + System.currentTimeMillis() + ".jpg";
        //步骤一:创建存储照片的文件
        String imagePath = Environment.getExternalStorageDirectory()
                + File.separator + "Android" + File.separator + "data"
                + File.separator + activity.getPackageName() + File.separator + "files" + File.separator + "imageFile" + File.separator + fileName;
        File file = new File(imagePath);
        //创建文件夹
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri fileUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //步骤二: Android 7.0及以上获取文件Uri
            fileUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", file);
        } else {
            //步骤三:获取文件Uri
            fileUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);// 设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);// 将拍取的照片保存到指定URI
        CAMEAR_PATH = imagePath;
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * 选择图片
     *
     * @param activity
     */
    public static void seleteImageViewAc(Activity activity) {
        if (Build.VERSION.SDK_INT < 19) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            activity.startActivityForResult(intent, CHOOSE_REQUEST);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            activity.startActivityForResult(intent, CHOOSE_REQUEST);
        }
    }

    public static String getRealPath(Activity activity, Uri fileUrl) {
        String fileName = null;
        if (fileUrl != null) {
            if (fileUrl.getScheme().toString().compareTo("content") == 0) // content://开头的uri
            {
                Cursor cursor = activity.getContentResolver().query(fileUrl, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    try {
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        fileName = cursor.getString(column_index); // 取出文件路径
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } finally {
                        cursor.close();
                    }
                }
            } else if (fileUrl.getScheme().compareTo("file") == 0) // file:///开头的uri
            {
                fileName = fileUrl.getPath();
            }
        }
        return fileName;
    }


    /**
     * 沙河机制
     *
     * @param context
     * @return
     */
    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT >= 29) {
                //Android10之后
                sdDir = context.getExternalFilesDir(null);//获取应用所在根目录/Android/data/your.app.name/file/ 也可以根据沙盒机制传入自己想传的参数，存放在指定目录
            } else {
                sdDir = Environment.getExternalStorageDirectory();// 获取SD卡根目录
            }
        } else {
            sdDir = Environment.getRootDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 使用Compressor IO模式自定义压缩
     *
     * @param path .setMaxWidth(640).setMaxHeight(480)这两个数值越高，压缩力度越小，图片也不清晰
     *             .setQuality(75)这个方法只是设置图片质量，并不影响压缩图片的大小KB
     *             .setCompressFormat(Bitmap.CompressFormat.WEBP) WEBP图片格式是Google推出的 压缩强，质量 高，但是IOS不识别，需要把图片转为字节流然后转PNG格式
     *             .setCompressFormat(Bitmap.CompressFormat.PNG)PNG格式的压缩，会导致图片变大，并耗过大的内 存，手机反应缓慢
     *             .setCompressFormat(Bitmap.CompressFormat.JPEG)JPEG压缩；压缩速度比PNG快，质量一般，基本上属于1/10的压缩比例
     *
     *  COMPRESS_PATH   压缩路径 指定同目录文件会出现压缩失败，防止压缩失败源文件也失效问题
     */
    public static String initCompressorIO(Context context, String path) {
        String comprePath = "";
        try {
            File file = new Compressor(context)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(COMPRESS_PATH)
                    .compressToFile(new File(path));
            comprePath = file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comprePath;
    }

}
