package com.base_util.base;

import android.content.Context;

public interface BaseView {
    Context getContext();

    void onSuccess(int type, Object... params);
}
