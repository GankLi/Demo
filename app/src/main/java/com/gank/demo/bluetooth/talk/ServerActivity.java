package com.gank.demo.bluetooth.talk;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gank.demo.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.woyou.aidlservice.IWoyouReceiverService;


/**
 * Created by GankLi on 2016/5/31.
 */
public class ServerActivity extends Activity {

    private final UUID UUID_PRINTER = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();

    BluetoothServerSocket mFirServerSocket;
    ExecutorService mExecutorService = null;

    List<AppClient> mClient;
    TextView mStatus;
    TextView mInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_server);
        mExecutorService = Executors.newCachedThreadPool();
        bindWoyouService();
        mClient = new ArrayList<AppClient>();
        mStatus = (TextView) findViewById(R.id.status);
        mInfo = (TextView) findViewById(R.id.info);
        startServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFirServerSocket != null) {
            try {
                mFirServerSocket.close();
                mFirServerSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (AppClient client : mClient) {
            try {
                client.release(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mClient.clear();
    }

    void updateStatus(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatus.setText("已经连接客户端： " + mClient.size() + " 个");
            }
        });
    }

    void updateInfo(final String info){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInfo.setText(info);
            }
        });
    }

    void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    void startServer() {
        try {
            mFirServerSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Serveer", UUID_PRINTER);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            BluetoothSocket socket = mFirServerSocket.accept();
                            AppClient temp = new AppClient(socket, "Fir");
                            mExecutorService.execute(temp);
                            mClient.add(temp);
                            updateStatus();
                            toast("Fir - 发现客户端");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Simulate", e.getMessage());
                        toast("Fir - 服务端错误");
                    }
                }
            });
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Simulate", e.getMessage());
        }
    }


    private class AppClient implements Runnable {
        private BluetoothSocket mSocket;
        private InputStream mIn = null;
        private OutputStream mOs = null;
        private byte[] mBuffer = new byte[8096];
        private String mAddress = null;
        private int mUid;
        private String mPackageName;
        private String mName;

        public AppClient(BluetoothSocket socket, String name) {
            this.mSocket = socket;
            this.mName = name;
            try {
                mIn = socket.getInputStream();
                mOs = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                int offset = 0;
                String msg = null;
                Log.v("Simulate", "--------run--------" + mName);
                while (true) {
                    if ((offset = mIn.read(mBuffer)) > 0) {
                        if (mWoyouReceiverService != null) {
                            Log.v("Simulate", mName + " --postPrintData-");
                            mWoyouReceiverService.postPrintData(mBuffer, 0, offset);
                        }
                        msg = new String(mBuffer, 0, offset, "gbk");
                        Log.v("Simulate", msg);
                        updateInfo(msg);
                    } else {
                        Log.e("Simulate", "-----------Client Socket Broken----------");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Simulate", "", e);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                try {
                    release(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void release(boolean release) throws IOException {
            mIn.close();
            mOs.close();
            mSocket.close();
            if (release) {
                mClient.remove(this);
                updateStatus();
            }
        }
    }

    private void bindWoyouService() {
        try {
            Intent intent = new Intent("com.woyou.receiver");
            intent.setPackage("com.woyou.bluetoothreceiver");
            bindService(intent, mReceiverConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            Log.e("Simulate", "---Unable bind BluetoothReceiver---");
        }
    }

    private IWoyouReceiverService mWoyouReceiverService;

    private ServiceConnection mReceiverConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("Simulate", "IWoyouReceiverService - onServiceDisconnected");
            mWoyouReceiverService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Simulate", "IWoyouReceiverService - onServiceConnected");
            mWoyouReceiverService = IWoyouReceiverService.Stub.asInterface(service);
        }
    };
}
