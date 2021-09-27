package com.base_util.util;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.base_util.R;
import com.qq.okhttp.OkHttpUtils2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    /**
     * Log的tag
     */
    public final String TAG = getClass().getName();

    protected String  toolBarName = "", toolBarLeftState = "V";

    protected String state = "", id = "", mType = "";

    protected int page = 1, pageCount;

    private Toolbar toolbar;

    //ViewDataBinding
    protected T dataBinding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setViewDataBinding();
        //初始化沉浸式
        Log.e("Activity", "run:--------->当前类名: " + TAG);
        AppManager.getAppManager().addActivity(this);
        initView();
//        initTitleView();
        setClick();
    }

    /**
     * 加载布局contentView
     * @return 布局
     */
    protected abstract int getLayoutRes();
    protected abstract void initView();
    protected abstract void setClick();

    private void setViewDataBinding() {
        dataBinding = DataBindingUtil.setContentView(this,getLayoutRes());
    }

    protected void initTitleView() {
        try {
            /**
             * comtitle  的使用
             */
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (null != toolbar) {
//                toolbar.setNavigationIcon(R.mipmap.point_left);
                if (toolBarName != "") {
                    toolbar.setTitle(toolBarName);
                } else {
                    toolbar.setTitle("");
                }
                setSupportActionBar(toolbar);
                toolbar.setTitleTextColor(Color.WHITE);

                switch (toolBarLeftState) {
                    case "V":
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
                        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
                        break;
                    case "I":
                        break;
                    case "G":
                        break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //白色title调用
    protected static void initTitle(Activity activity, View view) {
        StatusBarUtil.setTranslucent(activity,0);
        StatusBarUtil.setLightMode(activity);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
        //根据 Tag 取消请求
        OkHttpUtils2.getInstance().cancelTag(this);
    }

    /**
     * 设置 app 字体不随系统字体设置改变
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res != null) {
            Configuration config = res.getConfiguration();
            if (config != null && config.fontScale != 1.0f) {
                config.fontScale = 1.0f;
                res.updateConfiguration(config, res.getDisplayMetrics());
            }
        }
        return res;
    }

}