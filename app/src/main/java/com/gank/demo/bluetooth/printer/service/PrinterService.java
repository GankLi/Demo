package com.gank.demo.bluetooth.printer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.gank.demo.service.IPrinterService;

import java.io.UnsupportedEncodingException;

public class PrinterService extends Service {

    private final static String TAG = "PrinterService";
    private Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "-------------onBind-----------");
        return new PrinterBinder();
    }

    class PrinterBinder extends IPrinterService.Stub {
        @Override
        public void postPrintData(byte[] data, int onset, int offset) throws RemoteException {
            final String str;
            try {
                str = new String(data, onset, offset, "gbk");
                Log.v(TAG, "-postPrintData-" + str);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
