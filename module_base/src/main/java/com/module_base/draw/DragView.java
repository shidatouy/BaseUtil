package com.module_base.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.module_base.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Robert on 2017/6/21.
 */

public class DragView extends RelativeLayout implements MoveLayout.DeleteMoveLayout, MoveLayout.ClickMoveLayout {

    private static final String TAG = "DragView";

    private int mSelfViewWidth = 0;
    private int mSelfViewHeight = 0;

    private Context mContext;

    /**
     * the identity of the moveLayout
     */
    private int mLocalIdentity = 0;

    private List<MoveLayout> mMoveLayoutList;

    /*
     * 拖拽框最小尺寸
     */
    private int mMinHeight = 120;
    private int mMinWidth = 180;

    private boolean mIsAddDeleteView = false;
    private boolean mIsAddAreaView = false;
    private TextView deleteArea, aearNumber;

    private int DELETE_AREA_WIDTH = 120;
    private int DELETE_AREA_HEIGHT = 90;

    String area = "";
    List<HashMap<Object, Object>> mapList = new ArrayList<>();


    public DragView(Context context) {
        super(context);
        init(context, this);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, this);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, this);
    }

    private void init(Context c, DragView thisp) {
        mContext = c;
        mMoveLayoutList = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //  Log.e(TAG, "onDraw: height=" + getHeight());
        mSelfViewWidth = getWidth();
        mSelfViewHeight = getHeight();

        if (mMoveLayoutList != null) {
            int count = mMoveLayoutList.size();
            for (int a = 0; a < count; a++) {
                mMoveLayoutList.get(a).setViewWidthHeight(mSelfViewWidth, mSelfViewHeight);
                mMoveLayoutList.get(a).setDeleteWidthHeight(DELETE_AREA_WIDTH, DELETE_AREA_HEIGHT);
            }
        }

    }

    /**
     * call set Min height before addDragView
     *
     * @param height
     */
    private void setMinHeight(int height) {
        mMinHeight = height;
    }

    /**
     * call set Min width before addDragView
     *
     * @param width
     */
    private void setMinWidth(int width) {
        mMinWidth = width;
    }

    public void addDragView(View selfView, int left, int top, int right, int bottom, boolean isFixedSize, boolean whitebg, String tag) {
        addDragView(selfView, left, top, right, bottom, isFixedSize, whitebg, mMinWidth, mMinHeight, tag);
    }

    /**
     * 每个moveLayout都可以拥有自己的最小尺寸
     */
    public void addDragView(int resId, int left, int top, int right, int bottom, boolean isFixedSize, boolean whitebg, int minwidth, int minheight, String tag) {
        LayoutInflater inflater2 = LayoutInflater.from(mContext);
        View selfView = inflater2.inflate(resId, null);
        addDragView(selfView, left, top, right, bottom, isFixedSize, whitebg, minwidth, minheight, tag);
    }

    /**
     * 每个moveLayout都可以拥有自己的最小尺寸
     */
    public void addDragView(View selfView, int left, int top, int right, int bottom, boolean isFixedSize, boolean whitebg, int minwidth, int minheight, String tag) {
        //    invalidate();
        //  Log.e(TAG, "addDragView: height="+getHeight() +"   width+"+ getWidth() );

        MoveLayout moveLayout = new MoveLayout(mContext);

        moveLayout.setClickable(true);
        moveLayout.setMinHeight(minheight);
        moveLayout.setMinWidth(minwidth);
        int tempWidth = right - left;
        int tempHeight = bottom - top;
        if (tempWidth < minwidth) tempWidth = minwidth;
        if (tempHeight < minheight) tempHeight = minheight;

        //set postion
        LayoutParams lp = new LayoutParams(tempWidth, tempHeight);
        lp.setMargins(left, top, 0, 0);
        moveLayout.setLayoutParams(lp);

        //add sub view (has click indicator)
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dragSubView = inflater.inflate(R.layout.drag_sub_view, null);
        LinearLayout addYourViewHere = (LinearLayout) dragSubView.findViewById(R.id.add_your_view_here);
        LinearLayout.LayoutParams lv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addYourViewHere.addView(selfView, lv);

        if (whitebg == true) {
            LinearLayout changeBg = (LinearLayout) dragSubView.findViewById(R.id.change_bg);
            changeBg.setBackgroundResource(R.drawable.corners_bg2);
        }

        moveLayout.addView(dragSubView);

        //set fixed size
        moveLayout.setFixedSize(isFixedSize);

        moveLayout.setOnDeleteMoveLayout(this);
        moveLayout.setOnClickMoveLayout(this);
        moveLayout.setIdentity(mLocalIdentity++);
        moveLayout.setAC(tag);

        if (mIsAddDeleteView == false) {
            //add delete area
            deleteArea = new TextView(mContext);
            deleteArea.setText("删除");
            deleteArea.setBackgroundColor(Color.argb(99, 0xbb, 0, 0));
            LayoutParams dellayout = new LayoutParams(DELETE_AREA_WIDTH, DELETE_AREA_HEIGHT);
            dellayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            dellayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            deleteArea.setLayoutParams(dellayout);
            deleteArea.setGravity(Gravity.CENTER);
            // moveLayout.setDeleteWidthHeight(180, 90);
            deleteArea.setVisibility(View.INVISIBLE);
            addView(deleteArea);
        }

        if (mIsAddAreaView == false) {
            //add delete area
            aearNumber = new TextView(mContext);
            aearNumber.setBackgroundColor(Color.argb(99, 0xbb, 0, 0));
            LayoutParams dellayout = new LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT);
            dellayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            dellayout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            dellayout.setMargins(0, 30, 0, 0);
            aearNumber.setLayoutParams(dellayout);
            aearNumber.setGravity(Gravity.CENTER);
            // moveLayout.setDeleteWidthHeight(180, 90);
            aearNumber.setVisibility(View.INVISIBLE);
            addView(aearNumber);
        }

        //set view to get control
        moveLayout.setDeleteView(deleteArea);
        moveLayout.setClickView(aearNumber);

        addView(moveLayout);

        mMoveLayoutList.add(moveLayout);
    }

    public void addDragView(int resId, int left, int top, int right, int bottom, boolean isFixedSize, boolean whitebg, String tag) {
        LayoutInflater inflater2 = LayoutInflater.from(mContext);
        View selfView = inflater2.inflate(resId, null);
        addDragView(selfView, left, top, right, bottom, isFixedSize, whitebg, tag);
    }

    @Override
    public void onDeleteMoveLayout(int identity) {
        int count = mMoveLayoutList.size();
        for (int a = 0; a < count; a++) {
            if (mMoveLayoutList.get(a).getIdentity() == identity) {
                //delete
                removeView(mMoveLayoutList.get(a));
                removeNumber(mMoveLayoutList.get(a).getIdentity());
            }
        }
    }

    private void removeNumber(int a) {
        for (int i = 0; i < mapList.size(); i++) {
            if ((Object) a == mapList.get(i).get("key")) {
                mapList.remove(i);
            }
        }
        setTextArea(mapList);
    }

    @Override
    public void onClickMoveLayout(final int identity) {
        int count = mMoveLayoutList.size();
        for (int a = 0; a < count; a++) {
            if (mMoveLayoutList.get(a).getIdentity() == identity) {
                //click
                System.out.println("点击了===" + identity);
                final int finalA = a;
                InputDialog.build((AppCompatActivity) mContext)
                        .setTitle("提示")
                        .setMessage("请输入面积 宽 * 长")
                        .setOkButton("确定", new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                if (!mIsAddAreaView) {
                                    aearNumber.setVisibility(VISIBLE);
                                    mIsAddAreaView = true;
                                }
//                                aearNumber.setText(mMoveLayoutList.get(finalA).getAC() + ":" + inputStr);
                                HashMap<Object, Object> stringHashMap = new HashMap<>();
                                stringHashMap.put("name", inputStr);
                                stringHashMap.put("key", identity);
                                stringHashMap.put("tag", mMoveLayoutList.get(finalA).getAC());
                                mapList.add(stringHashMap);
                                setTextArea(mapList);
                                return false;
                            }
                        })
                        .setCancelButton("取消")
                        .setCancelable(false)
                        .show();
            }
        }
    }

    private void setTextArea(List<HashMap<Object, Object>> mapList) {
        String name = "";
        for (int i = 0; i < mapList.size(); i++) {
            if (i < mapList.size()) {
                name += mapList.get(i).get("tag") + ":" + mapList.get(i).get("name") + "\n";
            }
        }
        aearNumber.setText(name);
    }

//
//    /**
//     * delete interface
//     */
//    private DeleteDragView mListener = null;
//    public interface DeleteDragView {
//        void onDeleteDragView();
//    }
//    public void setOnDeleteDragView(DeleteDragView l) {
//        mListener = l;
//    }


}
