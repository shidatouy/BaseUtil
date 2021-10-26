package com.base_util.picseleter;

import android.app.Activity;

import com.base_util.R;
import com.base_util.util.Tools;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import androidx.fragment.app.Fragment;

public class PicSelect {
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
                .compressSavePath(Tools.filePath)//自定义压缩图片保存地址，注意Q版本下的适配
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
                .compressSavePath(Tools.filePath)//自定义压缩图片保存地址，注意Q版本下的适配
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
                .compressSavePath(Tools.filePath)//自定义压缩图片保存地址，注意Q版本下的适配
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
                .compressSavePath(Tools.filePath)//自定义压缩图片保存地址，注意Q版本下的适配
                .forResult(PictureConfig.REQUEST_CAMERA);
    }


}
