package com.module_base.draw;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * Created by Robert on 2017/6/20.
 */

public class MoveLayout extends RelativeLayout {

    private int dragDirection = 0;
    private static final int TOP = 0x15;
    private static final int LEFT = 0x16;
    private static final int BOTTOM = 0x17;
    private static final int RIGHT = 0x18;
    private static final int LEFT_TOP = 0x11;
    private static final int RIGHT_TOP = 0x12;
    private static final int LEFT_BOTTOM = 0x13;
    private static final int RIGHT_BOTTOM = 0x14;
    private static final int CENTER = 0x19;


    private int lastX;
    private int lastY;

    private int screenWidth;
    private int screenHeight;

    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;

    public String getAC() {
        return AC;
    }

    public void setAC(String AC) {
        this.AC = AC;
    }

    private String AC = "";

    /**
     * 标示此类的每个实例的id
     */
    private int identity = 0;


    /**
     * 触控区域设定
     */
    private int touchAreaLength = 60;

    private int minHeight = 120;
    private int minWidth = 180;
    private static final String TAG = "MoveLinearLayout";

    private int downHeight, upHeight;

    public MoveLayout(Context context) {
        super(context);
        init();
    }

    public MoveLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoveLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        screenHeight = 500;//getResources().getDisplayMetrics().heightPixels - 40;
        screenWidth = 500;// getResources().getDisplayMetrics().widthPixels;
    }

    public void setViewWidthHeight(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void setMinHeight(int height) {
        minHeight = height;
        if (minHeight < touchAreaLength * 2) {
            minHeight = touchAreaLength * 2;
        }
    }

    public void setMinWidth(int width) {
        minWidth = width;
        if (minWidth < touchAreaLength * 3) {
            minWidth = touchAreaLength * 3;
        }
    }

    private boolean mFixedSize = false;

    public void setFixedSize(boolean b) {
        mFixedSize = b;
    }

    private int mDeleteHeight = 0;
    private int mDeleteWidth = 0;
    private boolean isInDeleteArea = false;

    public void setDeleteWidthHeight(int width, int height) {
        mDeleteWidth = screenWidth - width;
        mDeleteHeight = height;
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    public void doublePoint(MotionEvent event) {
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
            int xlen = Math.abs((int) event.getX(0) - (int) event.getX(1));
            int ylen = Math.abs((int) event.getY(0) - (int) event.getY(1));
            nLenStart = Math.sqrt((double) xlen * xlen + (double) ylen * ylen);
        } else if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_UP) {
            oriLeft = getLeft();
            oriRight = getRight();
            oriTop = getTop();
            oriBottom = getBottom();
            int wid = oriRight - oriLeft;
            int hei = oriBottom - oriTop;
            for (int i = 0; i < 2; i++) {
                int xlen = Math.abs((int) event.getX(0) - (int) event.getX(1));
                int ylen = Math.abs((int) event.getY(0) - (int) event.getY(1));

                double nLenEnd = Math.sqrt((double) xlen * xlen + (double) ylen * ylen);
                double diff = nLenEnd - nLenStart;
                int df = (int) diff / 10;
                wid = wid + df * 4;
                hei = hei + df * 4;
//                if(nLenEnd > nLenStart){//通过两个手指开始距离和结束距离，来判断放大缩小
//                }  else {
//                    System.out.println("缩小:"+df);
//                }
            }
            LayoutParams lp = new LayoutParams(wid, hei);
            lp.setMargins(oriLeft, oriTop, 0, 0);
            setLayoutParams(lp);
        }
    }

    double nLenStart = 0;
    boolean ifDouble = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int nCnt = event.getPointerCount(); // 获得多少点
        if (nCnt == 2) {// 多点触控，
            ifDouble = true;
            doublePoint(event);
            return true;
        } else {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    ifDouble = false;
                    Log.d(TAG, "onTouchEvent: down height=" + getHeight());
                    oriLeft = getLeft();
                    oriRight = getRight();
                    oriTop = getTop();
                    oriBottom = getBottom();

                    lastY = (int) event.getRawY();
                    lastX = (int) event.getRawX();
                    dragDirection = getDirection((int) event.getX(), (int) event.getY());
                    downHeight = getHeight();
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouchEvent: up height=" + getHeight());
                    spotL = false;
                    spotT = false;
                    spotR = false;
                    spotB = false;
                    requestLayout();
                    mDeleteView.setVisibility(View.INVISIBLE);
                    upHeight = getHeight();
                    // invalidate();
//                    ifClick();
                    break;
//            case MotionEvent.ACTION_CANCEL:zidin
//                Log.d(TAG, "onTouchEvent: cancel");
//                spotL = false;
//                spotT = false;
//                spotR = false;
//                spotB = false;
//                invalidate();
//                break;
                case MotionEvent.ACTION_MOVE:
                    if (!ifDouble) {
                        int tempRawX = (int) event.getRawX();
                        int tempRawY = (int) event.getRawY();

                        int dx = tempRawX - lastX;
                        int dy = tempRawY - lastY;
                        lastX = tempRawX;
                        lastY = tempRawY;

                        switch (dragDirection) {
                            case LEFT:
                                left(dx);
                                break;
                            case RIGHT:
                                right(dx);
                                break;
                            case BOTTOM:
                                bottom(dy);
                                break;
                            case TOP:
                                top(dy);
                                break;
                            case CENTER:
                                center(dx, dy);
                                break;
//                    case LEFT_BOTTOM:
//                        left( dx);
//                        bottom( dy);
//                        break;
//                    case LEFT_TOP:
//                        left( dx);
//                        top(dy);
//                        break;
//                    case RIGHT_BOTTOM:
//                        right( dx);
//                        bottom( dy);
//                        break;
//                    case RIGHT_TOP:
//                        right( dx);
//                        top( dy);
//                        break;
                        }

                        //new pos l t r b is set into oriLeft, oriTop, oriRight, oriBottom
                        LayoutParams lp = new LayoutParams(oriRight - oriLeft, oriBottom - oriTop);
                        lp.setMargins(oriLeft, oriTop, 0, 0);
                        setLayoutParams(lp);
                    }
                    // Log.d(TAG, "onTouchEvent: move");

                    //   Log.d(TAG, "onTouchEvent: set layout width="+(oriRight - oriLeft)+" height="+(oriBottom - oriTop));
                    //   Log.d(TAG, "onTouchEvent: marginLeft="+oriLeft+"  marginTop"+oriTop);
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    private void ifClick() {
        cListener.onClickMoveLayout(identity);
    }

    /**
     * 触摸点为中心->>移动
     */
    private void center(int dx, int dy) {
        int left = getLeft() + dx;
        int top = getTop() + dy;
        int right = getRight() + dx;
        int bottom = getBottom() + dy;

        if (left < 0) {
            left = 0;
            right = left + getWidth();
        }
        if (right > screenWidth) {
            right = screenWidth;
            left = right - getWidth();
        }
        if (top < 0) {
            top = 0;
            bottom = top + getHeight();
        }
        if (bottom > screenHeight) {
            bottom = screenHeight;
            top = bottom - getHeight();
        }

        oriLeft = left;
        oriTop = top;
        oriRight = right;
        oriBottom = bottom;

        //todo: show delete icon
        mDeleteView.setVisibility(View.VISIBLE);
////        //do delete
        if (isInDeleteArea == false && oriRight > mDeleteWidth && oriTop < mDeleteHeight) {//delete
            Log.e(TAG, "center: oriRight" + oriRight + "  mDeleteWidth" + mDeleteWidth + "  oriTop" + oriTop + "  mDeleteHeightv" + mDeleteHeight);
            if (mListener != null) {
                mListener.onDeleteMoveLayout(identity);
                mDeleteView.setVisibility(View.INVISIBLE);
                isInDeleteArea = true;//this object is deleted ,only one-time-using
            }
        }

    }

    /**
     * 触摸点为上边缘
     */
    private void top(int dy) {
        oriTop += dy;
        if (oriTop < 0) {
            oriTop = 0;
        }
        if (oriBottom - oriTop < minHeight) {
            oriTop = oriBottom - minHeight;
        }
    }

    /**
     * 触摸点为下边缘
     */
    private void bottom(int dy) {

        oriBottom += dy;
        if (oriBottom > screenHeight) {
            oriBottom = screenHeight;
        }
        if (oriBottom - oriTop < minHeight) {
            oriBottom = minHeight + oriTop;
        }
    }

    /**
     * 触摸点为右边缘
     */
    private void right(int dx) {
        oriRight += dx;
        if (oriRight > screenWidth) {
            oriRight = screenWidth;
        }
        if (oriRight - oriLeft < minWidth) {
            oriRight = oriLeft + minWidth;
        }
    }

    /**
     * 触摸点为左边缘
     */
    private void left(int dx) {
        oriLeft += dx;
        if (oriLeft < 0) {
            oriLeft = 0;
        }
        if (oriRight - oriLeft < minWidth) {
            oriLeft = oriRight - minWidth;
        }
    }

    private int getDirection(int x, int y) {
        int left = getLeft();
        int right = getRight();
        int bottom = getBottom();
        int top = getTop();

//        if (x < touchAreaLength && y < touchAreaLength) {
//            return LEFT_TOP;
//        }
//        if (y < touchAreaLength && right - left - x < touchAreaLength) {
//            return RIGHT_TOP;
//        }
//        if (x < touchAreaLength && bottom - top - y < touchAreaLength) {
//            return LEFT_BOTTOM;
//        }
//        if (right - left - x < touchAreaLength && bottom - top - y < touchAreaLength) {
//            return RIGHT_BOTTOM;
//        }
        if (mFixedSize == true) {
            return CENTER;
        }

        if (x < touchAreaLength) {
            spotL = true;
            requestLayout();
            return LEFT;
        }
        if (y < touchAreaLength) {
            spotT = true;
            requestLayout();
            return TOP;
        }
        if (right - left - x < touchAreaLength) {
            spotR = true;
            requestLayout();
            return RIGHT;
        }
        if (bottom - top - y < touchAreaLength) {
            spotB = true;
            requestLayout();
            return BOTTOM;
        }
        return CENTER;
    }

    private boolean spotL = false;
    private boolean spotT = false;
    private boolean spotR = false;
    private boolean spotB = false;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);


        RelativeLayout rlt = (RelativeLayout) getChildAt(0);
        int count = rlt.getChildCount();

        for (int a = 0; a < count; a++) {
            if (a == 1) {        //l
                if (spotL)
                    rlt.getChildAt(a).setVisibility(View.VISIBLE);
                else
                    rlt.getChildAt(a).setVisibility(View.INVISIBLE);
            } else if (a == 2) { //t
                if (spotT)
                    rlt.getChildAt(a).setVisibility(View.VISIBLE);
                else
                    rlt.getChildAt(a).setVisibility(View.INVISIBLE);
            } else if (a == 3) { //r
                if (spotR)
                    rlt.getChildAt(a).setVisibility(View.VISIBLE);
                else
                    rlt.getChildAt(a).setVisibility(View.INVISIBLE);
            } else if (a == 4) { //b
                if (spotB)
                    rlt.getChildAt(a).setVisibility(View.VISIBLE);
                else
                    rlt.getChildAt(a).setVisibility(View.INVISIBLE);
            }
            // Log.d(TAG, "onLayout: "+rlt.getChildAt(a).getClass().toString());
        }

    }


    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    //set the main delete area object (to change visibility)
    private View mDeleteView = null;

    public void setDeleteView(View v) {
        mDeleteView = v;
    }

    //set the main click area object (to change visibility)
    private View mClickView = null;

    public void setClickView(View v) {
        mClickView = v;
    }

    //delete listener
    private DeleteMoveLayout mListener = null;

    public interface DeleteMoveLayout {
        void onDeleteMoveLayout(int identity);
    }

    public void setOnDeleteMoveLayout(DeleteMoveLayout l) {
        mListener = l;
    }


    //click listener
    private ClickMoveLayout cListener = null;

    public interface ClickMoveLayout {
        void onClickMoveLayout(int identity);
    }

    public void setOnClickMoveLayout(ClickMoveLayout l) {
        cListener = l;
    }

}
