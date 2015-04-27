package com.youmu.wangyilotteryshake;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/**
 * Created by youzehong on 15/4/27.
 */
public class ScaleBgLayout extends RelativeLayout {

    private int mLongPressTimeout;
    private int mScaledTouchSlop;

    public ScaleBgLayout(Context context) {
        super(context, null);
    }

    public ScaleBgLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    private void init(Context ctx) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(ctx);
        mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        mLongPressTimeout = ViewConfiguration.getLongPressTimeout();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int actionMasked = MotionEventCompat.getActionMasked(event);

        switch (actionMasked){
            case MotionEvent.ACTION_DOWN:
                scaleLayout();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                restoreLayout();
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 缩放布局
     */
    private void scaleLayout () {
        PropertyValuesHolder pivotX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.8f);
        PropertyValuesHolder pivotY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.8f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pivotX, pivotY);
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(shakeImg1,"scaleX", 1.0f, 0.8f);
//        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(shakeImg1,"scaleY", 1.0f, 0.8f);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(objectAnimator, objectAnimator1);
//        animatorSet.setDuration(500);
//        animatorSet.start();

        objectAnimator.setDuration(500);
        objectAnimator.start();
    }

    /**
     * 还原布局
     */
    private void restoreLayout () {
        PropertyValuesHolder pivotX = PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1.0f);
        PropertyValuesHolder pivotY = PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pivotX, pivotY);
        objectAnimator.setDuration(500);
        objectAnimator.start();
    }
}
