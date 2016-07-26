package com.gank.demo.gaussblurtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.demo.R;
import com.gank.demo.utils.BitmapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class GaussActivity extends Activity {
    @BindView(R.id.image_gauss)
    ImageView mGaussImg = null;
    @BindView(R.id.image_avg)
    ImageView mAvgImg = null;
    @BindView(R.id.image_renderscript)
    ImageView mRenderScriptImg = null;
    @BindView(R.id.image_java)
    ImageView mJavaImg = null;
    @BindView(R.id.text_gauss)
    TextView mGaussText = null;
    @BindView(R.id.text_avg)
    TextView mAvgText = null;
    @BindView(R.id.text_renderscript)
    TextView mRenderScriptText = null;
    @BindView(R.id.text_java)
    TextView mJavaText = null;
    @BindView(R.id.btn)
    Button mBtn = null;
    @BindView(R.id.title)
    TextView mTitle = null;

    Bitmap mBitmap = null;
    static final int Radius = 25;
    static final int Radius_RenderScript = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);
        ButterKnife.bind(this);
        mAvgImg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Log.w("Gank", "---onPreDraw---");
                mHandler.sendEmptyMessage(1);
                return true;
            }
        });
    }

    Handler mHandler = new Handler(new Handler.Callback(){
        boolean mInit = false;
        @Override
        public boolean handleMessage(Message msg) {
            if(!mInit){
                init();
                mInit = true;
            }
            return true;
        }
    });

    private void init() {
        //1: Load Bitmap
        mBitmap = BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.test, mGaussImg.getWidth(), mGaussImg.getHeight());
//      mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        Log.w("Gank", "mBitmap - H： " + mBitmap.getHeight() + " W: " + mBitmap.getWidth());

        mTitle.setText("Bitmap - H: " + mBitmap.getHeight() + " W: " + mBitmap.getWidth());
        //2: Init View
        mGaussImg.setImageBitmap(mBitmap);
        mAvgImg.setImageBitmap(mBitmap);
        mRenderScriptImg.setImageBitmap(mBitmap);
        mJavaImg.setImageBitmap(mBitmap);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.create(new Observable.OnSubscribe<BlurInfo>() {
                    @Override
                    public void call(Subscriber<? super BlurInfo> subscriber) {
                        Log.w("Gank", "Call");
                        long begin = 0;

                        BlurInfo info2 = new BlurInfo(BlurInfo.TYPE_AVG);
                        begin = System.currentTimeMillis();
                        info2.bitmap = BlurUtil.gaussBlurUseAvg(mBitmap, Radius);
                        info2.use_time = System.currentTimeMillis() - begin;
                        subscriber.onNext(info2);

                        BlurInfo info = new BlurInfo(BlurInfo.TYPE_BLUR);
                        begin = System.currentTimeMillis();
                        info.bitmap = BlurUtil.gaussBlurUseGauss(mBitmap, Radius);
                        info.use_time = System.currentTimeMillis() - begin;
                        subscriber.onNext(info);

                        BlurInfo info3 = new BlurInfo(BlurInfo.TYPE_RENDORSCRIPT);
                        begin = System.currentTimeMillis();
                        info3.bitmap = BlurUtil.gaussBlurUseRenderScript(mBitmap, Radius_RenderScript, getBaseContext());
                        info3.use_time = System.currentTimeMillis() - begin;
                        subscriber.onNext(info3);

                        BlurInfo info4 = new BlurInfo(BlurInfo.TYPE_JAVA);
                        begin = System.currentTimeMillis();
                        info4.bitmap = BlurUtil.gaussBlurUseJava(mBitmap, Radius_RenderScript, false);
                        info4.use_time = System.currentTimeMillis() - begin;
                        subscriber.onNext(info4);

                        subscriber.onCompleted();
                    }
                }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<BlurInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.w("Gank", "onCompleted");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.w("Gank", "onError", e);
                    }
                    @Override
                    public void onNext(BlurInfo info) {
                        Log.w("Gank", "onNext");
                        switch (info.type){
                            case BlurInfo.TYPE_BLUR:
                                mGaussText.setText("高斯模糊，半径" + Radius + "，用时：" + info.use_time + "ms");
                                mGaussImg.setImageBitmap(info.bitmap);
                                break;
                            case BlurInfo.TYPE_AVG:
                                mAvgText.setText("3次均值模糊，半径" + Radius + "，用时：" + info.use_time + "ms");
                                mAvgImg.setImageBitmap(info.bitmap);
                                break;
                            case BlurInfo.TYPE_RENDORSCRIPT:
                                mRenderScriptText.setText("RenderScript模糊，半径" + Radius + "，用时：" + info.use_time + "ms");
                                mRenderScriptImg.setImageBitmap(info.bitmap);
                                break;
                            case BlurInfo.TYPE_JAVA:
                                mJavaText.setText("Java模糊，半径" + Radius + "，用时：" + info.use_time + "ms");
                                mJavaImg.setImageBitmap(info.bitmap);
                                break;
                        }
                    }
                });
            }
        });
    }

    class BlurInfo {
        static final int TYPE_BLUR = 1;
        static final int TYPE_AVG = 2;
        static final int TYPE_RENDORSCRIPT = 3;
        static final int TYPE_JAVA = 4;
        BlurInfo(int type){
            this.type = type;
        }
        int type;
        Bitmap bitmap;
        long use_time;
    }
}
