package com.base_util.application;

import android.app.Application;

import com.base_util.gloading.GlobalAdapter;
import com.base_util.util.MMkvUtil;
import com.billy.android.loading.Gloading;
import com.kongzue.dialog.util.DialogSettings;
import com.tencent.mmkv.MMKV;

/**
 * Created by Administrator on 2016/5/13.
 *
 * minSdk 21 起系统原生支持 multidex，无需再手动 install。
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
