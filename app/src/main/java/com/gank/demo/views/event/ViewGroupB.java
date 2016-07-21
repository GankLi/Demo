package com.gank.demo.views.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by GankLi on 2016/7/6.
 */

public class ViewGroupB extends LinearLayout {
    public ViewGroupB(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.w("Gank", "ViewGroupB - dispatchTouchEvent: " + MotionEvent.actionToString(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.w("Gank", "ViewGroupB - onInterceptTouchEvent: " + MotionEvent.actionToString(ev.getAction()));
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w("Gank", "ViewGroupB - onTouchEvent: " + MotionEvent.actionToString(event.getAction()));
        return super.onTouchEvent(event);
    }
}
