package com.base_util.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.billy.android.loading.Gloading;
import com.kongzue.dialog.util.DialogSettings;
import com.base_util.gloading.GlobalAdapter;
import com.base_util.util.ComData;
import com.base_util.util.MMkvUtil;
import com.tencent.mmkv.MMKV;

import java.io.File;

import androidx.multidex.MultiDex;

/**
 * Created by Administrator on 2016/5/13.
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;
    public static Context context;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        //Gloading
        Gloading.initDefault(new GlobalAdapter());
        initDialog();//初始化V3dialog
        initMmkv();
        dirFile();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initMmkv() {
        MMKV.initialize(this);
        MMkvUtil.getInstance();
    }


    private void initDialog() {
        DialogSettings.init();
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;

        //文件下载地址
        ComData.filePath = Environment.getExternalStorageDirectory() + "/Android/data/" + this.getPackageName() + "/imageFile/";
    }

    /**
     * 创建下载文件
     */
    private void dirFile() {
        File file = new File(ComData.filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}

