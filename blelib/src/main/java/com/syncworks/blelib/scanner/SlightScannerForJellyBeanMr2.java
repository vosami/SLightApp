package com.syncworks.blelib.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

/**
 * Created by vosami on 2015-04-28.
 * 안드로이드 버전 5.0이하 스캐너
 */
public class SlightScannerForJellyBeanMr2 extends SlightScanner {
    private final static String TAG = SlightScannerForJellyBeanMr2.class.getSimpleName();
    private BluetoothAdapter.LeScanCallback leScanCallback;

    protected SlightScannerForJellyBeanMr2(Context _context, long _scanPeriod, SlightScanCallback _slightScanCallback) {
        super(_context, _scanPeriod, _slightScanCallback);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void stopScan() {
        try {
            BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
            if (bluetoothAdapter != null) {
                bluetoothAdapter.stopLeScan(getLeScanCallback());
            }

        } catch(Exception e) {
            Log.e(TAG, "Internal Android exception scanning");
        }
    }

    @Override
    protected boolean deferScanIfNeeded() {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void startScan() {
        getBluetoothAdapter().startLeScan(getLeScanCallback());
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void finishScan() {
        getBluetoothAdapter().stopLeScan(getLeScanCallback());
    }


    private BluetoothAdapter.LeScanCallback getLeScanCallback() {
        if (leScanCallback == null) {
            leScanCallback =
                    new BluetoothAdapter.LeScanCallback() {

                        @Override
                        public void onLeScan(final BluetoothDevice device, final int rssi,
                                             final byte[] scanRecord) {
                            Log.d(TAG, "got record");
                            slightScanCallback.onLeScan(device, rssi, scanRecord);
                        }
                    };
        }
        return leScanCallback;
    }
}
