package com.base_util.application;

import android.app.Application;
import android.content.Context;

import com.base_util.gloading.GlobalAdapter;
import com.base_util.util.MMkvUtil;
import com.billy.android.loading.Gloading;
import com.kongzue.dialog.util.DialogSettings;
import com.tencent.mmkv.MMKV;

import androidx.multidex.MultiDex;

/**
 * Created by Administrator on 2016/5/13.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Gloading
        Gloading.initDefault(new GlobalAdapter());
        initDialog();//初始化V3dialog
        initMmkv();
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
    }

}

