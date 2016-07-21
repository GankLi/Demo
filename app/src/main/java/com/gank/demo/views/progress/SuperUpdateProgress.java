package com.gank.demo.views.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 *
 * @author xiaanming
 */
public class SuperUpdateProgress extends View {
    /**
     * 画笔对象的引用
     */
    private Paint mRingPaint;
    private Paint mBarPaint;
    private Paint mTxtPaint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public SuperUpdateProgress(Context context) {
        this(context, null);
    }

    public SuperUpdateProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperUpdateProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //获取自定义属性和默认值
        roundColor = Color.parseColor("#F4F4F4");
        roundProgressColor = Color.parseColor("#FF6900");
        textColor = Color.parseColor("#FF6900");
        textSize = 18;
        roundWidth = 4;
        max = 360;
        textIsDisplayable = true;
        style = 0;
        setup();
    }

    public boolean isSpinning = false;
    private int spinSpeed = 2;
    private int delayMillis = 50;

    private Handler spinHandler = new Handler() {
        /**
         * This is the code that will increment the progress variable
         * and so spin the wheel
         */
        @Override
        public void handleMessage(Message msg) {
            invalidate();
            if (isSpinning) {
                progress += spinSpeed;
                if (progress > max) {
                    progress = 0;
                }
                spinHandler.sendEmptyMessageDelayed(0, delayMillis);
            }
        }
    };

    /**
     * Turn off spin mode
     */
    public void stopSpinning() {
        isSpinning = false;
        progress = 0;
        spinHandler.removeMessages(0);
    }


    /**
     * Puts the view on spin mode
     */
    public void spin() {
        isSpinning = true;
        spinHandler.sendEmptyMessage(0);
    }

    int centre;
    int radius;

    void setup() {
        /**
         * 大圆环
         */
        centre = getWidth() / 2; //获取圆心的x坐标
        radius = (int) (centre - roundWidth / 2); //圆环的半径
        mRingPaint = new Paint();
        mRingPaint.setColor(roundColor); //设置圆环的颜色
        mRingPaint.setStyle(Paint.Style.STROKE); //设置空心
        mRingPaint.setStrokeWidth(roundWidth); //设置圆环的宽度
        mRingPaint.setAntiAlias(true);  //消除锯齿
        //文字
        mTxtPaint = new Paint();
        mTxtPaint.setStrokeWidth(0);
        mTxtPaint.setColor(textColor);
        mTxtPaint.setTextSize(textSize);
        mTxtPaint.setTypeface(Typeface.DEFAULT); //设置字体
        //进度
        mBarPaint = new Paint();
        mBarPaint.setColor(roundProgressColor); //设置圆环的颜色
        mBarPaint.setStyle(Paint.Style.STROKE); //设置空心
        mBarPaint.setStrokeWidth(roundWidth); //设置圆环的宽度
        mBarPaint.setAntiAlias(true);  //消除锯齿
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 画最外层的大圆环
         */
        canvas.drawCircle(centre, centre, radius, mRingPaint); //画出圆环
        /**
         * 文字
         */
        if (textIsDisplayable && style == STROKE) {
            String str1 = "正在优化第" + progress + "个应用";
            String str2 = "共" + max + "个";
            float width1 = mRingPaint.measureText(str1);
            float width2 = mRingPaint.measureText(str2);
            canvas.drawText(str1, centre - width1 / 2, centre, mTxtPaint);
            canvas.drawText(str2, centre - width2 / 2, centre + textSize + 3, mTxtPaint);
        }

        /**
         * 画圆弧 ，画圆环的进度
         */
        //设置进度是实心还是空心
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
        switch (style) {
            case STROKE: {
                mBarPaint.setStyle(Paint.Style.STROKE);
                //canvas.drawArc(oval, 270, 360 * progress / max, false, ffpaint);  //根据进度画圆弧
                canvas.drawArc(oval, progress - 90, 40, false, mBarPaint);
                break;
            }
            case FILL: {
                mBarPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 270, 360 * progress / max, true, mBarPaint);  //根据进度画圆弧
                break;
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setup();
        invalidate();
    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }


    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }


}
