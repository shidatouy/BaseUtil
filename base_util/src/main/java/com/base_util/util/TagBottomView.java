package com.base_util.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base_util.R;

import androidx.annotation.Nullable;


/**
 * function: 底部按钮
 * author: zhengzq
 * date: 2019/4/9 14:11
 */
public class TagBottomView extends LinearLayout {

    private int bottom_icon;
    private String bottom_text;

    private LinearLayout mRelativeLayout;
    private TextView mTextView;
    private ImageView mImageView;
    private boolean isStartAnimation = false;
    private boolean isEndAnimation = false;
    AnimatorSet endAnimSet;
    AnimatorSet startAnimSet;

    public TagBottomView(Context context) {
        this(context, null);
    }

    public TagBottomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setClipToPadding(false);
        setClipChildren(false);

        startAnimSet = new AnimatorSet();
        endAnimSet = new AnimatorSet();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagBottomView, defStyleAttr, 0);
        bottom_icon = a.getResourceId(R.styleable.TagBottomView_bottom_icon, -1);
        bottom_text = a.getString(R.styleable.TagBottomView_bottom_text);
        a.recycle();

        mRelativeLayout = new LinearLayout(context);
        mRelativeLayout.setOrientation(VERTICAL);
        mRelativeLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        mImageView = new ImageView(context);
        mImageView.setImageResource(bottom_icon);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ScreenUtil.dip2px(context, 22), ScreenUtil.dip2px(context, 22));
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mImageView.setLayoutParams(lp);

        mTextView = new TextView(context);
        mTextView.setTextSize(10);
        mTextView.setText(bottom_text);
        mTextView.setPadding(0, 8, 0, 8);
        mTextView.setTextColor(getResources().getColor(R.color.text666));

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mTextView.setLayoutParams(lp1);

        mRelativeLayout.addView(mImageView);
        mRelativeLayout.addView(mTextView);
        addView(mRelativeLayout);
    }

    public void setImageResource(int id) {
        bottom_icon = id;
        mImageView.setImageResource(bottom_icon);
    }

    /**
     * 不动画
     *
     * @param first
     */
    public void setCancelAnimation(boolean first) {
        isStartAnimation = first;
        isEndAnimation = first;
    }

    public void setMySelected(boolean selected) {
        if (selected) {
            mTextView.setTextColor(getResources().getColor(R.color.mainColor));
            mImageView.setSelected(true);
            if (isStartAnimation) {
                isStartAnimation = false;
            } else {
                startAnimator();
            }
        } else {
            endAnimator();
        }
    }

    private void startAnimator() {
        if (!startAnimSet.isRunning()) {
            if (!endAnimSet.isRunning()) {
                //图片动画
                ObjectAnimator animation1 = ObjectAnimator.ofFloat(mImageView, "scaleY", 1, 1.2f);
                animation1.setDuration(200);
                ObjectAnimator animation2 = ObjectAnimator.ofFloat(mImageView, "scaleX", 1, 1.2f);
                animation2.setDuration(200);
                ObjectAnimator animation3 = ObjectAnimator.ofFloat(mImageView, "alpha", 0.5f, 0.8f);
                animation3.setDuration(200);
                mTextView.setTextColor(getResources().getColor(R.color.text666));
                ObjectAnimator animation4 = ObjectAnimator.ofFloat(mImageView, "scaleY", 1.2f, 1);
                animation4.setDuration(80);
                ObjectAnimator animation5 = ObjectAnimator.ofFloat(mImageView, "scaleX", 1.2f, 1);
                animation5.setDuration(80);
                ObjectAnimator animation6 = ObjectAnimator.ofFloat(mImageView, "alpha", 0.8f, 1f);
                animation6.setDuration(80);

                AnimatorSet animSet1 = new AnimatorSet();
                animSet1.playTogether(animation4, animation5, animation6);
                animSet1.setStartDelay(200);
                startAnimSet.playTogether(animation1, animation2, animation3, animSet1);
                startAnimSet.start();
            } else {
                endAnimSet.removeAllListeners();
                endAnimSet.cancel();
                mImageView.setSelected(true);
                mTextView.setTextColor(getResources().getColor(R.color.text666));
            }
        } else {
            startAnimSet.removeAllListeners();
            startAnimSet.cancel();
            mImageView.setSelected(true);
            mTextView.setTextColor(getResources().getColor(R.color.text666));
        }
    }

    private void endAnimator() {
        if (mImageView.isSelected()) {
            if (isEndAnimation) {
                mImageView.setSelected(false);
                mTextView.setTextColor(getResources().getColor(R.color.text666));
                isEndAnimation = false;
            } else {
                if (!endAnimSet.isRunning()) {
                    if (!startAnimSet.isRunning()) {
                        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0.6f).setDuration(120);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (float) animation.getAnimatedValue();
//                                mImageView.setAlpha(value);
                                if (value == 0.6f) {
                                    mImageView.setSelected(false);
//                                    mImageView.setAlpha(1f);
                                }
                            }
                        });

                        mTextView.setTextColor(getResources().getColor(R.color.text666));

                        endAnimSet.playTogether(animator);

                        endAnimSet.start();
                    } else {
                        startAnimSet.removeAllListeners();
                        startAnimSet.cancel();
                        mImageView.setSelected(false);
                        mTextView.setTextColor(getResources().getColor(R.color.text666));
                    }

                } else {
                    endAnimSet.removeAllListeners();
                    endAnimSet.cancel();
                    mImageView.setSelected(false);
                    mTextView.setTextColor(getResources().getColor(R.color.text666));
                }
            }
        } else {
            mImageView.setSelected(false);
            mTextView.setTextColor(getResources().getColor(R.color.text666));
        }
    }
}
