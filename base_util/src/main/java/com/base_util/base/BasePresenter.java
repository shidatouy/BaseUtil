package com.base_util.base;

import com.zhy.http.okhttp.OkHttpUtils;

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
        OkHttpUtils.getInstance().cancelTag(mView.getContext());
        model = null;
    }
}
