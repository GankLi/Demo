package com.gank.demo.info;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.gank.demo.R;
import com.gank.demo.utils.ShellUtil;

public class SystemInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        ShellUtil.CommandResult res = ShellUtil.execCommand("df", false);
        Toast.makeText(getBaseContext(), res.responseMsg, Toast.LENGTH_SHORT).show();
    }

}
