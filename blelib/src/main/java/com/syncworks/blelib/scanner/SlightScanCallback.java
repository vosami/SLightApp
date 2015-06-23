package com.syncworks.blelib.scanner;

import android.bluetooth.BluetoothDevice;

/**
 * Created by vosami on 2015-04-28.
 */
public interface SlightScanCallback {
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord);
    public void onEnd();
}
