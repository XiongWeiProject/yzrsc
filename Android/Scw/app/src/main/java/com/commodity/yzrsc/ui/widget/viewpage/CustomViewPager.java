package com.commodity.yzrsc.ui.widget.viewpage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by liyushen on 2017/5/30 22:40
 */
public class CustomViewPager extends ViewPager {
    boolean isCanScroll = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (Exception e) {
                // ignore it
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            // ignore it
        }
        return false;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        try {
            return super.dispatchTouchEvent(event);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }

    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

}
