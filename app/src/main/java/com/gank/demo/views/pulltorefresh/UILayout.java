package com.gank.demo.views.pulltorefresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by GankLi on 2016/6/20.
 */

public class UILayout extends ViewGroup {

    private static final String TAG = "Gank";

    private HeaderView mHeaderView;
    private View mContentView;

    private boolean mIsPulling = false;
    private int mHeaderOffset = 0;
    private static final float OFFSET_Y = 20.0f;
    private float mBeginX;
    private float mBeginY;
    private int mTotal = 0;

    public UILayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Header
        initHeaderVIew(context);
    }

    public UILayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //Header
        initHeaderVIew(context);
    }

    void initHeaderVIew(Context context){
        mHeaderView = new HeaderView(context);
        mHeaderView.setBackgroundColor(Color.parseColor("#4b4b4b"));
//        mHeaderView = new TextView(context);
//        mHeaderView.setText("下拉刷新");
//        mHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//        mHeaderView.setGravity(Gravity.CENTER);
//        mHeaderView.setBackgroundColor(Color.parseColor("#4b4b4b"));
        addView(mHeaderView);
    }

    @Override
    protected void onFinishInflate() {
        int size = getChildCount();
        Log.w(TAG, "Size: " + size);
        for (int i = 0; i < size; i++) {
            View child = getChildAt(i);
            Log.w(TAG, "View: " + child);
            if (!(child instanceof HeaderView)) {
                mContentView = child;
            }
        }
        super.onFinishInflate();
    }

    /**
     * 设置 Head 和 Content 的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.w(TAG, "---------------------Begin - onMeasure----------------------");
        Log.w(TAG, "1: HSpec: " + MeasureSpec.toString(heightMeasureSpec) + " WSpec: " + MeasureSpec.toString(widthMeasureSpec) + " H: " + getMeasuredHeight() + " W: " + getMeasuredWidth());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.w(TAG, "2: HSpec: " + MeasureSpec.toString(heightMeasureSpec) + " WSpec: " + MeasureSpec.toString(widthMeasureSpec) + " H: " + getMeasuredHeight() + " W: " + getMeasuredWidth());
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        Log.w(TAG, "HSpec: " + MeasureSpec.toString(heightMeasureSpec) + " WSpec: " + MeasureSpec.toString(widthMeasureSpec) + " Head - HSpec: " + MeasureSpec.toString(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)));
        mContentView.measure(widthMeasureSpec, heightMeasureSpec);
        mHeaderView.measure(widthMeasureSpec, MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        Log.w(TAG, "---------------------End - onMeasure----------------------");
    }

    /**
     * 设置 Head 和 Content 的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.w(TAG, "=============Begin - onLayout=============");
        Log.w(TAG, "changed: " + changed + "（" + l + "," + t + "） - (" + r + "," + b + ")");
        if (mContentView == null) {
            return;
        }
        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        Log.w(TAG, "H: " + height + " W: " + width);
        Log.w(TAG, "mContentView:  （0," + mHeaderOffset + "） - (" + width + "," + (height + mHeaderOffset) + ")");
        Log.w(TAG, "mHeaderView:  （0," + (mHeaderView.getMeasuredHeight() + mHeaderOffset) + "） - (" + width + "," + mHeaderOffset + ")");

        mContentView.layout(0, mHeaderOffset, width, height + mHeaderOffset);
        mHeaderView.layout(0, mHeaderOffset - mHeaderView.getMeasuredHeight(), width, mHeaderOffset);
        Log.w(TAG, "=============End - onLayout=============");
    }

    /**
     *  根据触摸， 显示下拉操作
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: //按下的时候，记录位置
                mBeginX = ev.getX();
                mBeginY = ev.getY();
                mTotal = 0;
                mIsPulling = false;
                Log.w(TAG, "==ACTION_DOWN== (" + mBeginX + "," + mBeginY + ")");
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE: //
                float offesty = ev.getY() - mBeginY;
                if (!mIsPulling) {
                    if (offesty > OFFSET_Y) {
                        boolean canScrollUp = mContentView.canScrollVertically(-1);
                        if (canScrollUp) {//可以继续上滑
                            return super.dispatchTouchEvent(ev);
                        }else{//出发下拉逻辑
                            mIsPulling = true;
                            mHeaderOffset = (int) (offesty - OFFSET_Y);
                            mHeaderView.setVisibility(VISIBLE);
                            invalidate();
                            return true;
                        }
                    }
                } else {//处理下拉操作
                    mHeaderOffset = (int) (offesty - OFFSET_Y);
                    mTotal += 3;
                    mHeaderView.offsetTopAndBottom(3);
                    mContentView.offsetTopAndBottom(3);
                    invalidate();
                    return true;
                }
                //Log.w(TAG, "====ACTION_MOVE==== (" + mBeginX + "," + mBeginY + ")" + " Offset: " + mHeaderOffset + " Pulling: " + mIsPulling);
                return super.dispatchTouchEvent(ev);
            case MotionEvent.ACTION_UP: //点击释放的时候， 下拉返回
                mHeaderOffset = 0;
                if (mIsPulling) {
                    mHeaderView.offsetTopAndBottom(-mTotal);
                    mContentView.offsetTopAndBottom(-mTotal);
                    invalidate();
                    mIsPulling = false;
                    return true;
                }
                Log.w(TAG, "==ACTION_UP== (" + ev.getX() + "," + ev.getY() + ")");
                return super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.w(TAG, "UILayout - =====draw=====");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.w(TAG, "UILayout - =====onDraw=====");
    }
}
