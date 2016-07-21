package com.gank.demo.bluetooth.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.util.Set;
import java.util.UUID;

/**
 * Created by GankLi on 2016/5/31.
 */
public class BluetoothUtils {

    private static final UUID UUID_PRINTER = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static BluetoothAdapter sBluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();

    public static int getBluetoothState() {
        return sBluetoothAdapter.getState();
    }

    public static Set<BluetoothDevice> getBondedDevices() {
        return sBluetoothAdapter.getBondedDevices();
    }

    public static BluetoothDevice getDeviceByAddress(String address){
        Set<BluetoothDevice> devices = getBondedDevices();
        for(BluetoothDevice device : devices){
            if(device.getAddress().equals(address)){
                return device;
            }
        }
        return null;
    }

    public static BluetoothSocket connect(BluetoothDevice device) {
        BluetoothSocket bluetoothSocket = null;
        try {
            bluetoothSocket = device
                    .createRfcommSocketToServiceRecord(UUID_PRINTER);
            bluetoothSocket.connect();
        } catch (Exception e) {
            Log.e("Gank", e.getMessage());
        }
        return bluetoothSocket;
    }
}
