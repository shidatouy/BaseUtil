package com.base_util.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base_util.R;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;

/**
 * 全局加载框。
 *
 * 旧实现用静态 Dialog 直接持有 Activity，页面销毁后既泄漏又容易在别的页面弹出时抛 BadTokenException。
 * 这里改为：
 * 1. 用 WeakReference 持有 Dialog；
 * 2. 每次 show 前先关闭旧的，并校验 Activity 是否还在可用状态；
 * 3. 只接受 Activity 上下文，传 Application 直接忽略（避免用错 token 崩溃）。
 */
public class LoadPD {

    private static WeakReference<MyDialog> dialogRef;

    public static void show(Context mContext, String message) {
        if (mContext == null) return;
        Activity activity = resolveActivity(mContext);
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        // 先关掉上一个页面的加载框，避免串页面
        close();

        try {
            MyDialog dialog = createLoadingDialog(activity, message);
            dialogRef = new WeakReference<>(dialog);
            dialog.show();
        } catch (Exception e) {
            close();
            e.printStackTrace();
        }
    }

    public static void close() {
        MyDialog dialog = dialogRef == null ? null : dialogRef.get();
        dialogRef = null;
        if (dialog != null) {
            try {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception ignored) {
                // 页面已经销毁时 dismiss 可能抛异常，忽略即可
            }
        }
    }

    /**
     * 保留旧方法名，内部同 {@link #close()}
     */
    public static void closeDialog() {
        close();
    }

    /**
     * 从 Context 里找出可用的 Activity（支持 ContextWrapper 包装）
     */
    private static Activity resolveActivity(Context context) {
        Context ctx = context;
        while (ctx instanceof ContextWrapper) {
            if (ctx instanceof Activity) {
                return (Activity) ctx;
            }
            ctx = ((ContextWrapper) ctx).getBaseContext();
        }
        return null;
    }

    /**
     * 得到自定义的 progressDialog
     */
    private static MyDialog createLoadingDialog(@NonNull Context context, String msg) {
        int width = context.getResources().getDisplayMetrics().widthPixels;
        View v = LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        LinearLayout layout = v.findViewById(R.id.dialog_load_view);
        layout.setLayoutParams(new RelativeLayout.LayoutParams(
                (int) (width / 1.5), (int) (width / 3)));
        View spaceshipImage = v.findViewById(R.id.img_view);
        TextView tipTextView = v.findViewById(R.id.tipTextView);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);

        MyDialog loadingDialog = new MyDialog(context, R.style.loading_dialog);
        loadingDialog.setContentView(v);
        // 加载框不允许返回键/点击外部关闭，只能由业务调 close()，否则请求还没回来框就没了
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }

    static class MyDialog extends Dialog {

        public MyDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }
    }
}
