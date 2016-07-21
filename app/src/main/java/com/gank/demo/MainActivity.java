package com.gank.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gank.demo.bluetooth.advertiser.BLEAdvertiserActivity;
import com.gank.demo.bluetooth.talk.ServerActivity;
import com.gank.demo.dialog.CustomDialog;
import com.gank.demo.dialog.DialogUtils;
import com.gank.demo.gaussblurtest.GaussActivity;
import com.gank.demo.gaussblurtest.RenderActivity;
import com.gank.demo.info.SystemInfoActivity;
import com.gank.demo.mvp.ui.activitys.WeatherActivity;
import com.gank.demo.opengl.egl.OpenGlDemo;
import com.gank.demo.opengl.glsurfaceview.OpenGLES20Activity;
import com.gank.demo.bluetooth.printer.BluetoothListActivity;
import com.gank.demo.views.event.EventActivity;
import com.gank.demo.views.progress.ActivityProgress;
import com.gank.demo.views.progress.TestActivity;
import com.gank.demo.views.pulltorefresh.PullActivity;

import annotation.MyAnnotation;

public class MainActivity extends Activity {

    TextView mTxt;

    @MyAnnotation(value="abc")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int res1 = Log.v("Gank", "-----onCreate------");
        int res2 = Log.e("Gank", "-----onCreate------");
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(BluetoothListActivity.class);
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(ServerActivity.class);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(BLEAdvertiserActivity.class);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(OpenGlDemo.class);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(OpenGLES20Activity.class);
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.show(getBaseContext());
            }
        });
        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
                CustomDialog dialog = builder.create();
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                lp.alpha = 0.5f;
                dialogWindow.setAttributes(lp);
                dialog.show();
            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(ActivityProgress.class);
            }
        });
        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(TestActivity.class);
            }
        });
        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(com.gank.demo.systemui.MainActivity.class);
            }
        });
        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(GaussActivity.class);
            }
        });
        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(RenderActivity.class);
            }
        });
        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(SystemInfoActivity.class);
            }
        });
        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(PullActivity.class);
            }
        });
        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(WeatherActivity.class);
            }
        });
        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDemo(EventActivity.class);
            }
        });
        Toast.makeText(getBaseContext(), res1 + " " + res2, Toast.LENGTH_SHORT).show();
    }

    private void startDemo(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
