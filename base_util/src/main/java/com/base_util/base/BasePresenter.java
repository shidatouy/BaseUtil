package com.base_util.base;


import com.qq.okhttp.OkHttpUtils2;

public class BasePresenter<V extends BaseView,M extends BaseModel>{
    protected V mView;
    protected M model;
    public void attech(BaseView view) {
        mView = (V) view;
    }

    public void destroy() {
        if (null == mView) {
            return;
        }
        OkHttpUtils2.getInstance().cancelTag(mView.getContext());
        model = null;
    }
}
