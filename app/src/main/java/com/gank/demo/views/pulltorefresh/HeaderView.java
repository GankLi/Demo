package com.gank.demo.views.pulltorefresh;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.gank.demo.R;

/**
 * Created by GankLi on 2016/6/20.
 */

public class HeaderView extends LinearLayout {

    private static final String TAG = "Gank";

    public HeaderView(Context context) {
        super(context, null);
        initViews();
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    void initViews(){
        LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.views_pull_header, this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.w(TAG, "HeaderView - 1: HSpec: " + MeasureSpec.toString(heightMeasureSpec) + " WSpec: " + MeasureSpec.toString(widthMeasureSpec) + " H: " + getMeasuredHeight() + " W: " + getMeasuredWidth());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.w(TAG, "HeaderView - 2: HSpec: " + MeasureSpec.toString(heightMeasureSpec) + " WSpec: " + MeasureSpec.toString(widthMeasureSpec) + " H: " + getMeasuredHeight() + " W: " + getMeasuredWidth());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.w(TAG, "HeaderView - onLayout - changed: " + changed + " (" + l + "," + t + ") - (" + r + "," + b + ")");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.w(TAG, "HeaderView - ----draw----");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.w(TAG, "HeaderView - ----onDraw----");
    }
}
