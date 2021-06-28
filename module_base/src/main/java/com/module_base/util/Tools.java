package com.module_base.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.module_base.R;

public class Tools {
    private static DialogA dialogA;

    public static void loadImagePop(Activity activity){
        initDialog(activity);
    }

    /**
     * 加载 弹窗
     */
    public static void initDialog(final Activity activity) {
        dialogA = new DialogA(activity, R.layout.pop_menulist, Gravity.BOTTOM) {
            @Override
            protected void initView(View view) {
                Button paiImage = (Button) view.findViewById(R.id.pai_image);
                Button getImage = (Button) view.findViewById(R.id.getImage);
                Button btn_quxiao = (Button) view.findViewById(R.id.btn_quxiao);

                paiImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ComData.getCamereAc(activity);
                        dialogA.disMiss();
                    }
                });

                getImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ComData.seleteImageViewAc(activity);
                        dialogA.disMiss();
                    }
                });

                btn_quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogA.disMiss();
                    }
                });
            }
        };

    }

}
