package com.vcom.publiclibrary.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Iterator;

/**
 * Created by Lifa on 2016-05-24 14:39.
 */
public class BluetoothUtils {
    private static BluetoothAdapter mBluetoothAdapter = null;

    public static BluetoothAdapter getBluetoothAdapter() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled())
            mBluetoothAdapter.enable();
        return mBluetoothAdapter;
    }

    public static BluetoothDevice getBluetoothDevive(String name) {
        BluetoothDevice device = null;
        if (mBluetoothAdapter == null)
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.enable();
        Iterator iterator = mBluetoothAdapter.getBondedDevices().iterator();
        while (iterator.hasNext()) {
            device = (BluetoothDevice) iterator.next();
            if (device.getName().equals(name) || device.getName().indexOf(name) > 5) {
                break;
            }
        }
        return device;
    }

    public static String getBluetoodAddr(String name) {
        String result = "";
        BluetoothDevice device = null;
        if (mBluetoothAdapter == null)
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.enable();
        Iterator iterator = mBluetoothAdapter.getBondedDevices().iterator();
        while (iterator.hasNext()) {
            device = (BluetoothDevice) iterator.next();
            if (device.getName().equals(name) || device.getName().indexOf(name) > 5) {
                result = device.getAddress();
            }
        }
        return result;
    }

    public static void closeBluetooth() {
        mBluetoothAdapter.disable();
    }


}
