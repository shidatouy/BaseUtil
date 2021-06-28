package com.module_base.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.module_base.R;

import androidx.annotation.NonNull;


public class LoadPD {
    static MyDialog dialog = null;

    public static void show(Context mContext, String message) {
        try {
            if (null == dialog) {
                dialog = createLoadingDialog(((Activity) mContext), message);
                // dialog.setTitle("提示�?);
                // dialog.setMessage(message);
                // dialog.setIndeterminate(b);
                dialog.setCancelable(true);
                dialog.show();
            } else {
                dialog.setCancelable(true);
                dialog.show();
            }
        } catch (Exception e) {
            try {
                dialog.cancel();
                dialog.dismiss();
                dialog = null;
                dialog = createLoadingDialog(((Activity) mContext), message);
                dialog.setCancelable(true);
                dialog.show();
            } catch (Exception e1) {
                e.printStackTrace();
            }
        }
    }

    public static void close() {
        if (dialog != null && dialog.isShowing()) {
            try {
                dialog.cancel();
                dialog.dismiss();
                dialog = null;
            } catch (Exception e) {
                dialog = null;
            }
        }
    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    private static MyDialog createLoadingDialog(Context context, String msg) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        View v = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_load_view);// 加载布局
        layout.setLayoutParams(new RelativeLayout.LayoutParams(
                (int) (width / 1.5), (int) (width / 3)));
        // main.xml中的ImageView
        View spaceshipImage = (View) v.findViewById(R.id.img_view);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        MyDialog loadingDialog = new MyDialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setContentView(v);// 设置布局
        return loadingDialog;
    }

    public static void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    static class MyDialog extends Dialog {

        public MyDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        @Override
        public void cancel() {
            super.cancel();
            if (dialog != null) {
                dialog = null;
            }
        }

        @Override
        public void dismiss() {
            super.dismiss();
            if (dialog != null) {
                dialog = null;
            }
        }
    }

}
