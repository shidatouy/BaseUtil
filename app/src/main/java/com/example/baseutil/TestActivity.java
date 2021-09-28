package com.example.baseutil;

import android.view.View;

import com.base_util.util.BaseActivity;
import com.base_util.util.T;
import com.example.baseutil.databinding.ActivityTestBinding;
import com.jaeger.library.StatusBarUtil;

public class TestActivity extends BaseActivity<ActivityTestBinding> {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        toolBarName ="测试";
        toolBarLeftState ="G";

        StatusBarUtil.setTransparent(this);
        dataBinding.tvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(TestActivity.this,"a");
            }
        });
    }

}
