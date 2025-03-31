package com.base_util.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.base_util.R;
import com.base_util.picseleter.PicSelect;

public class Tools {
    /**
     * 文件资料库下载的文件地址
     */
    public static String filePath;

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
                        PicSelect.getCamereAc(activity);

                        dialogA.disMiss();
                    }
                });

                getImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicSelect.seleteImageViewAc(activity);
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
