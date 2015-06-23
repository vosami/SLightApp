package com.syncworks.blelib.scanner;

import android.annotation.TargetApi;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vosami on 2015-04-28.
 * 안드로이드 버전 5.0 이상 스캐너
 */
@TargetApi(21)
public class SlightScannerForLollipop extends SlightScanner{
    private final static String TAG = SlightScannerForLollipop.class.getSimpleName();
    private BluetoothLeScanner mScanner;
    private ScanCallback leScanCallback;


    protected SlightScannerForLollipop(Context _context, long _scanPeriod, SlightScanCallback _slightScanCallback) {
        super(_context, _scanPeriod, _slightScanCallback);
    }

    @Override
    protected void stopScan() {
        try {
            if (getScanner()!= null) {
                getScanner().stopScan(getNewLeScanCallback());
            }
        } catch (Exception e) {
            Log.w(TAG,"Internal Android exception scanning");
        }
    }

    @Override
    protected boolean deferScanIfNeeded() {
        return false;
    }

    @Override
    protected void startScan() {
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        ScanSettings settings;

        Log.d(TAG, "starting scan in SCAN_MODE_LOW_LATENCY");
        settings = (new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)).build();

        try {
            if (getScanner() != null) {
                getScanner().startScan(filters, settings, getNewLeScanCallback());
            }
        }
        catch (IllegalStateException e) {
            Log.w(TAG, "Cannot start scan.  Bluetooth may be turned off.");
        }
    }

    @Override
    protected void finishScan() {
        try {
            if (getScanner() != null) {
                getScanner().stopScan(getNewLeScanCallback());
            }
        }
        catch (IllegalStateException e) {
            Log.w(TAG, "Cannot stop scan.  Bluetooth may be turned off.");
        }
    }


    private BluetoothLeScanner getScanner() {
        if (mScanner == null) {
            Log.d(TAG, "Making new Android L scanner");
            mScanner = getBluetoothAdapter().getBluetoothLeScanner();
            if (mScanner == null) {
                Log.w(TAG, "Failed to make new Android L scanner");
            }
        }
        return mScanner;
    }

    private ScanCallback getNewLeScanCallback() {
        if (leScanCallback == null) {
            leScanCallback = new ScanCallback() {

                @Override
                public void onScanResult(int callbackType, ScanResult scanResult) {
                    Log.d(TAG, "got record");
                    slightScanCallback.onLeScan(scanResult.getDevice(),
                            scanResult.getRssi(), scanResult.getScanRecord().getBytes());
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    Log.d(TAG, "got batch records");
                    for (ScanResult scanResult : results) {
                        slightScanCallback.onLeScan(scanResult.getDevice(),
                                scanResult.getRssi(), scanResult.getScanRecord().getBytes());
                    }
                }

                @Override
                public void onScanFailed(int i) {
                    Log.e(TAG, "Scan Failed");
                }
            };
        }
        return leScanCallback;
    }
}
