package com.base_util.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.base_util.R;
import com.base_util.imagepager.ImagePagerActivity;
import com.base_util.picseleter.GlideEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.fragment.app.Fragment;

public class ComData {
    public static Gson myGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    /**
     * 文件资料库下载的文件地址
     */
    public static String filePath;

    /**
     * 正式服务器地址  https://xczm.yc12313.com:1443  http://192.168.11.82:7443
     */
    public final static String TEST_HOST = "https://nyzm.yc12313.com:6443";
    //   public final static String TEST_HOST = "http://192.168.11.84:7443";


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
     * GSON 将 array 转成 list
     *
     * @param json
     * @param clazz 实体类
     * @param <T>
     * @return 数组
     */
    public static <T> List<T> stringToList(String json, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setDateFormat("yyyy-MM-dd HH:mm").create().fromJson(json, type);
        return list;
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    /**
     * GSON 将 objet 转成 实体
     *
     * @param json
     * @param clazz
     * @return
     */
    public static Class<T> stringToEntity(String json, Class clazz) {
        Gson gson = new Gson();
        Class<T> tClass = gson.fromJson(json, (Type) clazz);
        return tClass;
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
     * 带失败图
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void getGlidLoad(Context context, Object url, ImageView imageView) {
        Glide.with(context).load(url)
//                .placeholder(R.mipmap.jzz)//图片加载出来前，显示的图片
                .placeholder(R.mipmap.place)//图片加载出来前，显示的图片
                .error(R.mipmap.img_error2)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }


    /**
     * 拼接服务器IP
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void getGlidLoadInter(Context context, Object url, ImageView imageView) {
        Glide.with(context).load(ComData.TEST_HOST + "/" + url)
//                .placeholder(R.mipmap.img_zzjz)//图片加载出来前，显示的图片
                .error(R.mipmap.img_error2)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
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
     * @param position 下标 从 0 开始
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
        List<String> paths = new ArrayList<>();
        paths.add(ComData.TEST_HOST + "/" + path);
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) paths);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        context.startActivity(intent);
    }


    /**
     * 判断两个时间差
     *
     * @param date1
     * @param date2
     * @return
     */
    public static String getTimeDifference(Date date1, Date date2) {
//一天的毫秒数
        long d = 1000 * 24 * 60 * 60;
//一小时的毫秒数
        long h = 1000 * 60 * 60;
//一分钟的毫秒数
        long m = 1000 * 60;
// long ns = 1000;
// 获得两个时间的毫秒时间差异
        long timeDiff = date2.getTime() - date1.getTime();
// 计算差多少天
        long day = timeDiff / d;
// 计算差多少小时
        long hour = timeDiff % d / h;
// 计算差多少分钟
        long min = timeDiff % d % h / m;
        //输出结果
        return day + "天" + hour + "小时" + min + "分钟";
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

    /**
     * GSON 将 array 转成 list
     *
     * @param json
     * @param clazz 实体类
     * @param <T>
     * @return 数组
     */
    public static <T> List<T> jsonToList(String json, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list = myGson.fromJson(json, type);
        return list;
    }


    //获取当前获取焦点的元素id
    public static int getFocusId(Activity activity) {
        View rootView = activity.getWindow().getDecorView();
        return rootView.findFocus().getId();
    }

    /**
     * 选择图片
     *
     * @param activity
     */
    public static void seleteImageViewAc(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .theme(R.style.picture_default_style)
                .isWeChatStyle(false)
                .selectionMode(PictureConfig.SINGLE)
                .isSingleDirectReturn(true)//选择照片直接返回
                .compress(true)
                .compressSavePath(ComData.filePath)//自定义压缩图片保存地址，注意Q版本下的适配
                .imageSpanCount(4)// 每行显示个数 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.ofPNG())// 拍照保存图片格式后缀,默认jpeg
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 选择图片
     *
     * @param fragment
     */
    public static void seleteImageViewFa(Fragment fragment) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .theme(R.style.picture_default_style)
                .isWeChatStyle(false)
                .selectionMode(PictureConfig.SINGLE)
                .isSingleDirectReturn(true)//选择照片直接返回
                .compress(true)
                .compressSavePath(ComData.filePath)//自定义压缩图片保存地址，注意Q版本下的适配
                .imageSpanCount(4)// 每行显示个数 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.ofPNG())// 拍照保存图片格式后缀,默认jpeg
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 单独拍照
     *
     * @param activity
     */
    public static void getCamereAc(Activity activity) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .compress(true)
                .compressSavePath(ComData.filePath)//自定义压缩图片保存地址，注意Q版本下的适配
                .forResult(PictureConfig.REQUEST_CAMERA);
    }

    /**
     * 单独拍照
     *
     * @param fragment
     */
    public static void getCamereFr(Fragment fragment) {
        PictureSelector.create(fragment)
                .openCamera(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .compress(true)
                .compressSavePath(ComData.filePath)//自定义压缩图片保存地址，注意Q版本下的适配
                .forResult(PictureConfig.REQUEST_CAMERA);
    }


    /**
     * bitMap 存到指定路径
     *
     * @param fileName
     * @param bitmap
     */
    public static void saveBitmap(String fileWj,String fileName, Bitmap bitmap) {
        File file = new File(fileWj ,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sout(String url, Map<String, String> map) {
        String params = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return url + "?" + params;
    }

}
