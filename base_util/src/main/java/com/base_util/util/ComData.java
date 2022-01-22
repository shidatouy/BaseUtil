package com.base_util.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.base_util.imagepager.ImagePagerActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ComData {

    /**
     * 截取字符串的第一位到倒数第二位
     */
    public static String subString(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        String name = number.substring(0, number.length() - 1);
        return name;
    }

    /****
     * 计算文件大小
     *
     * @param length
     * @return
     */
    public static String ShowLongFileSzie(Long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else if (length < 1024) {
            return length + "B";
        } else {
            return "0KB";
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
     * @param images   图片数组 list<String>
     * @param position 下标
     */
    public static void seePicture(List<String> images, int position, Context context) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) images);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        context.startActivity(intent);
    }

    /**
     * 图片数组 list<String>
     */
    public static void seePicturePath(String path, Context context) {
        //update hqx
        if (TextUtils.isEmpty(path)) {
            return;
        }
        List<String> paths = new ArrayList<>();
        paths.add(path);
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) paths);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        context.startActivity(intent);
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

    /*
     * 是否为浮点数？double或float类型。
     * @param str 传入的字符串。
     * @return 是浮点数返回true,否则返回false。
     */
    public static boolean isDoubleOrFloat(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 根据总数和每页显示量，计算页数
     *
     * @param total
     * @param pageSize
     * @return
     */
    public static int getPageCount(int total, int pageSize) {
        if (total % pageSize == 0) {
            return total / pageSize;
        } else {
            return total / pageSize + 1;
        }
    }


    //获取当前获取焦点的元素id
    public static int getFocusId(Activity activity) {
        View rootView = activity.getWindow().getDecorView();
        return rootView.findFocus().getId();
    }


    /**
     * bitMap 存到指定路径
     *
     * @param fileName
     * @param bitmap
     */
    public static void saveBitmap(String fileWj, String fileName, Bitmap bitmap) {
        File file = new File(fileWj, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
