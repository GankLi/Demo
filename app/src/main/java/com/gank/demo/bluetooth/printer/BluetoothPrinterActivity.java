package com.gank.demo.bluetooth.printer;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gank.demo.R;

import java.io.IOException;
import java.io.OutputStream;

public class BluetoothPrinterActivity extends Activity implements View.OnClickListener {
    TextView mDeviceName;
    TextView mConnectState;
    EditText mData;
    Button mSend;
    Button mCommand;
    BluetoothSocket mSocket;
    OutputStream mOut = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("蓝牙打印");
        setContentView(R.layout.activity_bluetooth_printer);
        initView();
        initPrinter();
    }

    private void initPrinter() {
        String address = getIntent().getStringExtra("address");
        final BluetoothDevice device = BluetoothUtils.getDeviceByAddress(address);
        mDeviceName.setText(device.getName());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mSocket = BluetoothUtils.connect(device);
                if (mSocket != null) {
                    try {
                        mOut = mSocket.getOutputStream();
                    } catch (IOException e) {
                        Log.e("Gank", e.getMessage());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mOut != null) {
                                mConnectState.setText("连接成功");
                                mSend.setEnabled(true);
                                mCommand.setEnabled(true);
                            } else {
                                mConnectState.setText("连接失败");
                            }
                        }
                    });
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void initView() {
        mDeviceName = (TextView) findViewById(R.id.device_name);
        mConnectState = (TextView) findViewById(R.id.connect_state);
        mConnectState.setText("正在建立连接");
        mData = (EditText) this.findViewById(R.id.print_data);
        mSend = (Button) this.findViewById(R.id.send);
        mCommand = (Button) this.findViewById(R.id.command);
        mSend.setOnClickListener(this);
        mCommand.setOnClickListener(this);
        mSend.setEnabled(false);
        mCommand.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send) {
            sendData(mData.getText().toString() + "\n");
        } else if (v.getId() == R.id.command) {
            sendCommand();
        }
    }

    private void sendData(String data) {
        Log.e("Gank", "Try Send: " + data);
        if (mOut != null) {
            try {
                byte[] bytes = data.getBytes("gbk");

                mOut.write(bytes, 0, bytes.length);
                mOut.flush();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "发送失败！", Toast.LENGTH_SHORT).show();
                Log.e("Gank", e.getMessage());
            }
        }
    }

    final String[] items = {"复位打印机", "标准ASCII字体", "压缩ASCII字体", "字体不放大",
            "宽高加倍", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印", "取消黑白反显", "选择黑白反显",
            "取消顺时针旋转90°", "选择顺时针旋转90°"};
    final byte[][] byteCommands = {{0x1b, 0x40},// 复位打印机
            {0x1b, 0x4d, 0x00},// 标准ASCII字体
            {0x1b, 0x4d, 0x01},// 压缩ASCII字体
            {0x1d, 0x21, 0x00},// 字体不放大
            {0x1d, 0x21, 0x11},// 宽高加倍
            {0x1b, 0x45, 0x00},// 取消加粗模式
            {0x1b, 0x45, 0x01},// 选择加粗模式
            {0x1b, 0x7b, 0x00},// 取消倒置打印
            {0x1b, 0x7b, 0x01},// 选择倒置打印
            {0x1d, 0x42, 0x00},// 取消黑白反显
            {0x1d, 0x42, 0x01},// 选择黑白反显
            {0x1b, 0x56, 0x00},// 取消顺时针旋转90°
            {0x1b, 0x56, 0x01},// 选择顺时针旋转90°
    };

    private void sendCommand() {
        new AlertDialog.Builder(this).setTitle("请选择指令")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            mOut.write(byteCommands[which]);
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(), "设置指令失败！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create().show();
    }
}
