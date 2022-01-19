package com.example.baseutil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;

import com.base_util.picseleter.PicSelect;
import com.base_util.util.BaseActivity;
import com.base_util.util.ComData;
import com.base_util.util.T;
import com.base_util.util.Tools;
import com.example.baseutil.databinding.ActivityTestBinding;
import com.jaeger.library.StatusBarUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

public class TestActivity extends BaseActivity<ActivityTestBinding> {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        toolBarName = "测试";
        toolBarLeftState = "G";
        initWhiteTitle(this);
        dataBinding.tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission();
            }
        });
    }

    private void getPermission() {
        PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            getSDPath(TestActivity.this);
                            String imagePath = Environment.getExternalStorageDirectory()
                                    + File.separator + "Android" + File.separator + "data"
                                    + File.separator + getPackageName() + File.separator + "files" + File.separator + "imageFile" + File.separator;
                            File file = new File(imagePath);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            Tools.initDialog(TestActivity.this);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PicSelect.CHOOSE_REQUEST:
                // 图片选择结果回调
                if (null != data) {
                    Uri uri = data.getData();
                    String path = PicSelect.getRealPath(this, uri);
                }
                break;

            case PicSelect.REQUEST_CAMERA: //  media.getFileName() 获取不到照片名字
                // 结果回调
                String pth = PicSelect.CAMEAR_PATH;
                break;
        }
    }

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


}
