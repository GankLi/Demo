package com.gank.demo.views.event;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gank.demo.R;

public class EventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("Gank", "EventActivityd - onCreate");
        setContentView(R.layout.activity_event);
        findViewById(R.id.viewc).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.w("Gank", "EventActivityd - onTouchEvent: " + MotionEvent.actionToString(event.getAction()));
                return false;
            }
        });
    }
}
