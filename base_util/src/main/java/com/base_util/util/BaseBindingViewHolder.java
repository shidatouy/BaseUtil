package com.base_util.util;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import androidx.databinding.ViewDataBinding;

/**
 * Date: 2017/5/11 14:42.
 * Email: tyshengsx@gmail.com
 * @author tysheng
 */

public class BaseBindingViewHolder<B extends ViewDataBinding> extends BaseViewHolder {
    private B mB;

    public BaseBindingViewHolder(View view) {
        super(view);
    }

    public B getBinding() {
        return mB;
    }

    public void setBinding(B b) {
        mB = b;
    }
}
