package com.gank.demo.bluetooth.printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gank.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by GankLi on 2016/5/31.
 */
public class BluetoothListActivity extends Activity implements View.OnClickListener {

    Button mOpenBT;
    Button mGetDevices;
    ListView mBondedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_layout);
        mOpenBT = (Button) findViewById(R.id.open_bluetooth);
        mOpenBT.setOnClickListener(this);
        mGetDevices = (Button) findViewById(R.id.get_bonded_devices);
        mGetDevices.setOnClickListener(this);
        mBondedListView = (ListView) findViewById(R.id.bondDevices);

        if (BluetoothUtils.getBluetoothState() == BluetoothAdapter.STATE_ON) {
            mOpenBT.setEnabled(false);
            mGetDevices.setEnabled(true);
            mBondedListView.setEnabled(true);
        } else if (BluetoothUtils.getBluetoothState() == BluetoothAdapter.STATE_OFF) {
            mOpenBT.setEnabled(true);
            mGetDevices.setEnabled(false);
            mBondedListView.setEnabled(false);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_bluetooth) {
            Intent intent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(intent);
        } else if (v.getId() == R.id.get_bonded_devices) {
            addbondDevicesToListView();
        }
    }


    private void addbondDevicesToListView() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        Set<BluetoothDevice> devices = BluetoothUtils.getBondedDevices();
        int count = devices.size();
        for (BluetoothDevice device : devices) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", device.getName());
            map.put("address", device.getAddress());
            data.add(map);
        }
        String[] from = {"name", "address"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, from, to);

        // 把适配器装载到listView中
        mBondedListView.setAdapter(simpleAdapter);

        // 为每个item绑定监听，用于设备间的配对
        mBondedListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String address = ((TextView)view.findViewById(android.R.id.text2)).getText().toString();
                        Intent intent = new Intent();
                        intent.setClass(getBaseContext(), BluetoothPrinterActivity.class);
                        intent.putExtra("address", address);
                        startActivity(intent);
                    }
                });
    }


    /**
     * 蓝牙广播接收器
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    //Bond
                } else {
                    //Unbond
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Toast.makeText(getBaseContext(), "搜索蓝牙设备中...", Toast.LENGTH_SHORT).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Toast.makeText(getBaseContext(), "设备搜索完毕...", Toast.LENGTH_SHORT).show();
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (BluetoothUtils.getBluetoothState() == BluetoothAdapter.STATE_ON) {
                    Toast.makeText(getBaseContext(), "--------打开蓝牙-----------", Toast.LENGTH_SHORT).show();
                    mOpenBT.setEnabled(false);
                    mGetDevices.setEnabled(true);
                    mBondedListView.setEnabled(true);
                } else if (BluetoothUtils.getBluetoothState() == BluetoothAdapter.STATE_OFF) {
                    Toast.makeText(getBaseContext(), "--------关闭蓝牙-----------", Toast.LENGTH_SHORT).show();
                    mOpenBT.setEnabled(true);
                    mGetDevices.setEnabled(false);
                    mBondedListView.setEnabled(false);
                }
            }
        }
    };
}
