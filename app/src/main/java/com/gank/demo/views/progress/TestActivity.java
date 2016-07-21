package com.gank.demo.views.progress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.gank.demo.R;
import com.gank.demo.views.progress.AppUpdateProgress;

/**
 * Created by GankLi on 2016/5/16.
 */
public class TestActivity extends Activity {
    AppUpdateProgress mRoundPro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRoundPro = (AppUpdateProgress) findViewById(R.id.progress);
        mRoundPro.setMax(100);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = mRoundPro.getProgress();
                mRoundPro.setProgress(count + 10);
            }
        });
    }
}
