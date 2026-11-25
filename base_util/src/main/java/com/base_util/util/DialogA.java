package com.base_util.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.base_util.R;

/**
 * 底部弹出样式的基础 Dialog。
 * 子类在 {@link #initView(View)} 里绑定控件即可。
 */
public abstract class DialogA {
    private final AlertDialog dialog;
    private final View dialogView;

    public DialogA(Context c, int layoutRes, int gravity) {
        //加载布局并初始化组件
        dialogView = LayoutInflater.from(c).inflate(layoutRes, null);
        initView(dialogView);
        AlertDialog.Builder layoutDialog = new AlertDialog.Builder(c, R.style.AlertDialog);
        layoutDialog.setView(dialogView);
        dialog = layoutDialog.create();
        dialog.show();
        // show() 之后 window 才可用，部分机型仍可能为空，做一次保护
        Window window = dialog.getWindow();
        if (window != null) {
            window.setGravity(gravity);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    protected abstract void initView(View dialogView);

    public AlertDialog getDialog() {
        return dialog;
    }

    public View getDialogView() {
        return dialogView;
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void disMiss() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 修正旧拼写，保留 disMiss() 兼容老代码
     */
    public void dismiss() {
        disMiss();
    }
}
