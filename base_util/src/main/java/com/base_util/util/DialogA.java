package com.base_util.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.base_util.R;

public abstract class DialogA {
    private AlertDialog dialog;
    private View dialogView;

    public DialogA(Context c, int layoutRes, int gravity) {
        //加载布局并初始化组件
        dialogView = LayoutInflater.from(c).inflate(layoutRes, null);
        initView(dialogView);
        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(c, R.style.AlertDialog);
        layoutDialog.setView(dialogView);
        dialog = layoutDialog.create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(gravity);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    protected abstract void initView(View dialogView);

    public void disMiss() {
        dialog.dismiss();
    }
}
