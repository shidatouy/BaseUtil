package com.base_util.picseleter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.base_util.util.Tools;

import java.io.File;

import androidx.core.content.FileProvider;

public class PicSelect {
    public static String CAMEAR_PATH = "";
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
                + File.separator + activity.getPackageName() + File.separator + "files" + File.separator + "imageFile" + File.separator;
        File file = new File(imagePath, fileName);
//        //创建文件夹
//        if (!file.getParentFile().exists()) {
//            file.getParentFile().mkdirs();
//        }
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

}
