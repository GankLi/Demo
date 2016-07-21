package com.gank.demo.bluetooth.advertiser;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gank.demo.R;
import com.gank.demo.utils.Utils;

import java.util.UUID;

public class BLEAdvertiserActivity extends Activity {

    private final static UUID WEIXIN_UUID = UUID.fromString("FDA50693-A4E2-4FB1-AFCF-C6EB07647825");
    private static final String TAG = "AdvertiseActivity";
    public static final UUID THERM_SERVICE = UUID.fromString("00001809-0000-1000-8000-00805f9b34fb");
    public static final UUID THERM_SERVICE_Test = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private boolean isAdv = false;
    private AdvertiseCallback mAdvertiseCallback;
    private BluetoothLeAdvertiser mLeAdvertiser;

    private EditText mMajor;
    private EditText mMinor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleadvertiser);
        mAdvertiseCallback = createAdvertiseCallback();
        Log.e("Gank", "-onCreate-");
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Gank", "-onClick-");
                int major = Integer.valueOf(mMajor.getEditableText().toString());
                int minor = Integer.valueOf(mMinor.getEditableText().toString());
                startAdvertising(THERM_SERVICE_Test, major, minor);
            }
        });
        mMajor = (EditText) findViewById(R.id.major);
        mMinor = (EditText) findViewById(R.id.minor);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isAdv){
            if(mLeAdvertiser != null){
                mLeAdvertiser.stopAdvertising(mAdvertiseCallback);
            }
        }
    }

    private AdvertiseCallback createAdvertiseCallback() {
        return new AdvertiseCallback() {
            public void onStartFailure(int errorcode) {
                String str;
                switch (errorcode){
                    case 1:
                        str = "ADVERTISE_FAILED_DATA_TOO_LARGE";
                        break;
                    case 2:
                        str = "ADVERTISE_FAILED_TOO_MANY_ADVERTISERS";
                        break;
                    case 3:
                        str = "ADVERTISE_FAILED_ALREADY_STARTED";
                        break;
                    case 4:
                        str = "ADVERTISE_FAILED_INTERNAL_ERROR";
                        break;
                    case 5:
                        str = "ADVERTISE_FAILED_FEATURE_UNSUPPORTED";
                        break;
                    default:
                        str = "unknown";

                }
                Log.d("Gank", "onStartFailure:" + errorcode + " - " + str);
            }

            public void onStartSuccess(AdvertiseSettings paramAdvertiseSettings) {
                Log.d("Gank", "onStartSuccess:" + paramAdvertiseSettings);
            }
        };
    }

    private void startAdvertising(UUID uuid, int major, int minor) {
        mLeAdvertiser = ((BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().getBluetoothLeAdvertiser();
        if (this.mLeAdvertiser == null)
        {
            Log.e("Gank", "didn't get a bluetooth le advertiser");
            Toast.makeText(getBaseContext(), "提示:\n该设备不支持此功能,该Android系统版本不支持广播", Toast.LENGTH_SHORT).show();
            return;
        }
        AdvertiseSettings.Builder settingsBuilder = new AdvertiseSettings.Builder().setTxPowerLevel(3);
        settingsBuilder.setAdvertiseMode(0);
        settingsBuilder.setConnectable(true);
        AdvertiseData.Builder dataBuilder = new AdvertiseData.Builder();
        byte[] arrayOfByte1 = Utils.uuidToByte(uuid);
        byte[] arrayOfByte2 = Utils.inttobyte(major);
        byte[] arrayOfByte3 = Utils.inttobyte(minor);
        byte[] arrayOfByte4 = { -65 };
        byte[] arrayOfByte5 = new byte[23];
        System.arraycopy(new byte[] { 2, 21 }, 0, arrayOfByte5, 0, 2);
        System.arraycopy(arrayOfByte1, 0, arrayOfByte5, 2, 16);
        System.arraycopy(arrayOfByte2, 0, arrayOfByte5, 18, 2);
        System.arraycopy(arrayOfByte3, 0, arrayOfByte5, 20, 2);
        System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 22, 1);
        dataBuilder.addManufacturerData(76, arrayOfByte5);
        this.mAdvertiseCallback = createAdvertiseCallback();
        this.mLeAdvertiser.startAdvertising(settingsBuilder.build(), dataBuilder.build(), this.mAdvertiseCallback);
    }

}
