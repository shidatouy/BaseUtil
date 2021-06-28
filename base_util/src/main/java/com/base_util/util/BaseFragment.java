package com.base_util.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;


public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    /**
     * Log的tag
     */
    protected final String TAG = getClass().getName() + "--";
    protected static Context mContext;

    protected int page = 0, pageCount;

    protected String state = "";

    protected T dataBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("run:--------->当前类名: " + TAG);
        initViewBinding(inflater,container);
        initView();
        return dataBinding.getRoot();
    }
    protected abstract int getLayoutRes();

    protected abstract void initView();

    private void initViewBinding(LayoutInflater inflater, ViewGroup container) {
        dataBinding= DataBindingUtil.inflate(inflater,getLayoutRes(),container,false);
    }

    /**
     * 通过类名启动Activity
     *
     * @param cls
     */
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void startActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        mContext.startActivity(intent);
    }


}