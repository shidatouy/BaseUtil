package com.base_util.util;

import static com.base_util.util.StatusBarKt.immersive;
import static com.base_util.util.StatusBarKt.statusBarColor;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.base_util.R;
import com.qq.okhttp.OkHttpUtils2;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    public final String TAG = getClass().getName();

    protected String state = "", id = "";

    private Toolbar toolbar;
    protected T dataBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setViewDataBinding();

        // ✅ 状态栏样式
        if (isImmersiveStatusBar()) {
            immersive(this, 0, true);
        } else {
            statusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
        }

        // ✅ 标题栏：标题名不为空才显示
        String title = getToolBarName();
        if (title != null && !title.isEmpty()) {
            initTitleView(title);
        }

        Log.e("Activity", "run:--------->当前类名: " + TAG);
        AppManager.getAppManager().addActivity(this);
        initView();
    }

    // ==================== 子类可重写的方法 ====================

    /**
     * 是否沉浸式状态栏
     * true：沉浸式（透明 + 暗色文字）
     * false：普通（主题色）
     */
    protected boolean isImmersiveStatusBar() {
        return false;  // 默认普通状态栏
    }

    /**
     * 标题栏名称
     * 返回 null 或 ""：不显示标题栏（默认）
     * 返回具体文字：显示标题栏
     */
    protected String getToolBarName() {
        return null;  // 默认不显示
    }

    /**
     * 标题栏左侧按钮状态："V" 返回箭头，"I" 无，"G" 其他
     */
    protected String getToolBarLeftState() {
        return "V";
    }

    // ==================== 抽象方法 ====================

    protected abstract int getLayoutRes();
    protected abstract void initView();

    // ==================== 内部逻辑 ====================

    private void setViewDataBinding() {
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
    }

    private void initTitleView(String title) {
        try {
            toolbar = findViewById(R.id.toolbar);
            if (toolbar != null) {
                toolbar.setTitle(title);
                setSupportActionBar(toolbar);
                toolbar.setTitleTextColor(Color.WHITE);

                switch (getToolBarLeftState()) {
                    case "V":
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setHomeButtonEnabled(true);
                        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        OkHttpUtils2.getInstance().cancelTag(this);
    }

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