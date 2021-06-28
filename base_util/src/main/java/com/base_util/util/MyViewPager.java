package com.base_util.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;


/**
 * @author WangJintao  禁止滑动
 * @date 2015-12-26 下午4:14:37 类说明
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        try {
            super.onTouchEvent(arg0);
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        boolean b = false;
        try {
            b = super.onInterceptTouchEvent(arg0);
        } catch (Exception e) {

        }
        return b; //网上看的方法是直接返回false，但是会导致ViewPager翻页有BUG
    }

}