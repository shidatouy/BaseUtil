package com.base_util.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 可拖动布局
 */
public class DragLineLayout extends LinearLayout {

    private int mWidth;
    private int mHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private Context mContext;
    private onLocationListener mLocationListener;/*listen to the Rect */
    //是否拖动
    private boolean isDrag = false;

    public boolean isDrag() {
        return isDrag;
    }

    public DragLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(measureWidth, measureHeight);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mScreenWidth = getScreenWidth(mContext);
        mScreenHeight = getScreenHeight(mContext) - getStatusBarHeight();
    }

    public int getStatusBarHeight() {
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return mContext.getResources().getDimensionPixelSize(resourceId);
    }

    public int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    public int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    private float mDownX;
    private float mDownY;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrag = false;
                    mDownX = event.getX();
                    mDownY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float mXdistance = event.getX() - mDownX;
                    final float mYdistance = event.getY() - mDownY;
                    int l, r, t, b;
                    //当水平或者垂直滑动距离大于10,才算是拖动事件
                    if (Math.abs(mXdistance) > 10 || Math.abs(mYdistance) > 10) {
                        isDrag = true;
                        l = (int) (getLeft() + mXdistance);
                        r = l + mWidth;
                        t = (int) (getTop() + mYdistance);
                        b = t + mHeight;
                        //边界判断,不让布局滑出界面
                        if (l < 0) {
                            l = 0;
                            r = l + mWidth;
                        } else if (r > mScreenWidth) {
                            r = mScreenWidth;
                            l = r - mWidth;
                        }
                        if (t < 0) {
                            t = 0;
                            b = t + mHeight;
                        } else if (b > mScreenHeight) {
                            b = mScreenHeight;
                            t = b - mHeight;
                        }
                        //回调移动后的坐标点
                        if (mLocationListener != null) {
                            mLocationListener.locationRect((l + r) / 2, (t + b) / 2);
                        }
                        this.layout(l, t, r, b);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    setPressed(false);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    setPressed(false);
                    break;
            }
            return true;
        }
        return false;
    }

    public void setLocationListener(onLocationListener LocationListener) {
        this.mLocationListener = LocationListener;
    }

    public interface onLocationListener {
        void locationRect(float locationX, float locationY);
    }
}