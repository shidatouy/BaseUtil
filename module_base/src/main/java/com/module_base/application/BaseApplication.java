package com.module_base.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.alibaba.android.arouter.launcher.ARouter;
import com.billy.android.loading.Gloading;
import com.kongzue.dialog.util.DialogSettings;
import com.module_base.BuildConfig;
import com.module_base.config.ModuleConfig;
import com.module_base.gloading.GlobalAdapter;
import com.module_base.util.ComData;
import com.module_base.util.MMkvUtil;
import com.socks.library.KLog;
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
        initRouter(this);
//        modulesApplicationInit();//模块初始化applcation
        initMmkv();
        dirFile();
        initKlog();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initKlog() {
        if (BuildConfig.DEBUG == true) {
            KLog.init(true);
        } else {
            KLog.init(false);
        }
    }

    private void initMmkv() {
        MMKV.initialize(this);
        MMkvUtil.getInstance();
    }


    private void modulesApplicationInit() {
        for (String moduleImpl : ModuleConfig.MODULESLIST) {
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof ApplicationImpl) {
                    ((ApplicationImpl) obj).onCreate(this);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public static void initRouter(Application application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application);
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

