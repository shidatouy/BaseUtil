package com.base_util.util;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;


public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    /**
     * Log 的 tag
     */
    protected final String TAG = getClass().getName() + "--";

    protected T dataBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + getClass().getSimpleName());
        initViewBinding(inflater, container);
        initView();
        return dataBinding.getRoot();
    }

    protected abstract int getLayoutRes();

    protected abstract void initView();

    private void initViewBinding(LayoutInflater inflater, ViewGroup container) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Fragment 的 View 已销毁，及时释放 binding，避免持有已销毁的视图树
        dataBinding = null;
    }
}
