package com.base_util.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.TextView;

import com.base_util.photopreview.PhotoPreviewActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ComData {

    /**
     * 截取字符串的第一位到倒数第二位
     */
    public static String subString(String number) {
        return removeLast(number);
    }

    /****
     * 计算文件大小
     *
     * @param length
     * @return
     */
    public static String ShowLongFileSzie(Long length) {
        if (length == null || length <= 0) {
            return "0B";
        }
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else {
            return length + "B";
        }
    }


    /**
     * 判断获取跳转传的参数  不为 NULL
     *
     * @param key      传的参数
     * @param activity 当前activity
     * @return
     */
    public static String getExtra(String key, Activity activity) {
        String value = "";
        if (!TextUtils.isEmpty(key)) {
            if (null != activity.getIntent().getStringExtra(key)) {
                value = activity.getIntent().getStringExtra(key);
            }
        }
        return value;
    }


    /**
     * 文本 获取 去除空白字符
     */

    public static String getRemoveTrim(TextView view) {
        return view.getText().toString().trim();
    }


    /**
     * 判断字符串非空
     *
     * @param value
     * @return
     */
    public static boolean strEmpty(String value) {
        if (null != value && !"".equals(value) && !"null".equals(value)) {
            return false;
        }
        return true;
    }

    /**
     * 查看多张图片
     *
     * @param images   图片地址列表
     * @param position 默认显示第几张
     */
    public static void seePicture(List<String> images, int position, Context context) {
        if (images == null || images.isEmpty() || context == null) {
            return;
        }
        PhotoPreviewActivity.start(context, images, position);
    }

    /**
     * 查看单个图片
     */
    public static void seePicturePath(String path, Context context) {
        if (TextUtils.isEmpty(path) || context == null) {
            return;
        }
        List<String> paths = new ArrayList<>();
        paths.add(path);
        seePicture(paths, 0, context);
    }

    /**
     * 去除最后一个符号
     *
     * @return
     */
    public static String removeLast(String str) {
        if (TextUtils.isEmpty(str)) return "";
        return str.substring(0, str.length() - 1);
    }

    /**
     * 根据总数和每页显示量，计算页数
     *
     * @param total
     * @param pageSize
     * @return
     */
    public static int getPageCount(int total, int pageSize) {
        // pageSize 为 0 时旧写法会除零崩溃
        if (total <= 0 || pageSize <= 0) {
            return 0;
        }
        return (total + pageSize - 1) / pageSize;
    }


    /**
     * bitMap 存到指定路径
     *
     * @param fileName
     * @param bitmap
     */
    public static void saveBitmap(String fileWj, String fileName, Bitmap bitmap) {
        if (bitmap == null || TextUtils.isEmpty(fileWj) || TextUtils.isEmpty(fileName)) {
            return;
        }
        File dir = new File(fileWj);
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }
        // 旧实现在 compress 抛异常时不会关闭流，会导致文件句柄泄漏
        try (FileOutputStream fos = new FileOutputStream(new File(dir, fileName))) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
