package com.base_util.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.base_util.R;
import com.base_util.picseleter.PicSelect;

import java.lang.ref.WeakReference;

/**
 * 通用弹窗入口（拍照 / 相册 / 取消）。
 *
 * 旧实现用 {@code private static DialogA dialogA} 强引用，弹窗持有 Activity，
 * 页面销毁后既泄漏 Window 又容易在下次弹出时叠窗；这里改成弱引用 + 统一关闭入口。
 */
public class Tools {

    /**
     * 文件资料库下载的文件地址
     */
    public static String filePath;

    private static WeakReference<DialogA> dialogRef;

    public static void loadImagePop(Activity activity) {
        initDialog(activity);
    }

    /**
     * 加载 弹窗
     */
    public static void initDialog(final Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        // 先关掉上一次的弹窗，避免连续点击叠出多个
        dismissDialog();

        DialogA dialog = new DialogA(activity, R.layout.pop_menulist, Gravity.BOTTOM) {
            @Override
            protected void initView(View view) {
                Button paiImage = (Button) view.findViewById(R.id.pai_image);
                Button getImage = (Button) view.findViewById(R.id.getImage);
                Button btnQuxiao = (Button) view.findViewById(R.id.btn_quxiao);

                paiImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 返回 null 说明目录不可写或 FileProvider 没声明，别让用户以为点了没反应
                        if (PicSelect.startCamera(activity) == null) {
                            Toast.makeText(activity, "无法启动相机，请检查 FileProvider 配置",
                                    Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    }
                });

                getImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicSelect.selectImage(activity);
                        dismissDialog();
                    }
                });

                btnQuxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                    }
                });
            }
        };
        dialogRef = new WeakReference<>(dialog);
    }

    /**
     * 关闭当前弹窗；页面销毁时建议在 onDestroy 调用一次
     */
    public static void dismissDialog() {
        DialogA dialog = dialogRef == null ? null : dialogRef.get();
        dialogRef = null;
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
