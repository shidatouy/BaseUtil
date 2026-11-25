package com.example.baseutil;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.base_util.photopreview.PhotoPreviewActivity;
import com.base_util.picseleter.PicSelect;
import com.base_util.util.BaseActivity;
import com.base_util.util.ComData;
import com.base_util.util.GetFilePathFromUri;
import com.base_util.util.MediaStoreUtil;
import com.base_util.util.Tools;
import com.bumptech.glide.Glide;
import com.example.baseutil.databinding.ActivityTestBinding;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Demo：
 * 1. 选图/拍照 → 回显 → 点图片看单张大图；
 * 2. 直接预览相册最近 9 张，测左右滑动与双指缩放。
 */
public class TestActivity extends BaseActivity<ActivityTestBinding> {

    /** 相册预览最多取几张 */
    private static final int PREVIEW_LIMIT = 9;

    /** compileSdk 是 30，Manifest.permission 里还没有这个常量，只能写字面量（33 才引入） */
    private static final String READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES";

    private String path;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    protected String getToolBarName() {
        return "图片工具";
    }

    @Override
    protected void initView() {
        // 选图 / 压缩
        dataBinding.tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionThen(new Runnable() {
                    @Override
                    public void run() {
                        initImageDirs();
                        Tools.initDialog(TestActivity.this);
                    }
                });
            }
        });

        // 预览相册里的多张图片
        dataBinding.tvPreviewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionThen(new Runnable() {
                    @Override
                    public void run() {
                        previewGallery();
                    }
                });
            }
        });

        // 预览当前选中的单张图片
        dataBinding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (path == null) {
                    Toast.makeText(TestActivity.this, "先选一张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                ComData.seePicturePath(path, TestActivity.this);
            }
        });
    }

    private void requestPermissionThen(final Runnable action) {
        PermissionX.init(this)
                .permissions(needPermissions())
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            action.run();
                        } else {
                            Toast.makeText(TestActivity.this, "缺少读取相册/相机的权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 按系统版本取需要的权限：
     * 13(33)+ 读相册要 READ_MEDIA_IMAGES，READ_EXTERNAL_STORAGE 会被系统直接忽略；
     * 10(29)~12 读相册用 READ_EXTERNAL_STORAGE，写应用私有目录不再需要 WRITE；
     * 9(28) 及以下才需要 WRITE_EXTERNAL_STORAGE。
     */
    private String[] needPermissions() {
        if (Build.VERSION.SDK_INT >= 33) {
            return new String[]{READ_MEDIA_IMAGES, Manifest.permission.CAMERA};
        }
        if (Build.VERSION.SDK_INT >= 29) {
            return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA};
        }
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    }

    /**
     * 取相册最近的若干张图片直接进预览，用来验证滑动与缩放
     */
    private void previewGallery() {
        List<String> images = MediaStoreUtil.queryImages(this, PREVIEW_LIMIT);
        if (images.isEmpty()) {
            Toast.makeText(this, "相册里没有取到图片，先选一张试试", Toast.LENGTH_SHORT).show();
            return;
        }
        PhotoPreviewActivity.start(this, images, 0);
    }

    /**
     * 目录由 PicSelect 自己维护（默认在应用私有目录下），这里只负责建出来
     */
    private void initImageDirs() {
        File imageDir = PicSelect.getImageDir(this);
        File compressDir = PicSelect.getCompressDir(this);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        if (!compressDir.exists()) {
            compressDir.mkdirs();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PicSelect.REQUEST_CAMERA) {
            // 拍照结果按 Activity 取回，用户取消时文件长度为 0
            File file = PicSelect.takeCameraResult(this);
            if (resultCode != RESULT_OK || file == null || file.length() == 0) {
                return;
            }
            path = file.getAbsolutePath();
            showImage();
            return;
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PicSelect.CHOOSE_REQUEST) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                path = GetFilePathFromUri.getFileAbsolutePath(this, uri);
                showImage();
            }
        }
    }

    private void showImage() {
        Glide.with(this).load(path).into(dataBinding.img);
    }
}
