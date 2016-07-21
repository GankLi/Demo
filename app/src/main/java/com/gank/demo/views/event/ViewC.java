package com.gank.demo.views.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by GankLi on 2016/7/6.
 */

public class ViewC extends ImageView {
    public ViewC(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewC(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //Log.w("Gank", "ViewC - dispatchTouchEvent: " + MotionEvent.actionToString(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w("Gank", "ViewC - onTouchEvent: " + MotionEvent.actionToString(event.getAction()));
        super.onTouchEvent(event);
        return true;
    }
}
