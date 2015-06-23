package com.syncworks.blelib.scanner;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.util.Date;

/**
 * Created by vosami on 2015-04-28.
 * 블루투스 스캐너
 */
public abstract class SlightScanner {
    private final static String TAG = SlightScanner.class.getSimpleName();
    // 블루투스 어댑터 설정
    private BluetoothAdapter mBluetoothAdapter = null;

    private long scanPeriod;

    private long mScanCycleStopTime = 0l;

    private boolean isScanning = false;
    protected final Handler scanHandler = new Handler();

    protected final Context context;

    protected final SlightScanCallback slightScanCallback;

    protected SlightScanner(Context _context, long _scanPeriod, SlightScanCallback _slightScanCallback) {
        this.context = _context;
        this.scanPeriod = _scanPeriod;
        slightScanCallback = _slightScanCallback;
    }

    public static SlightScanner createScanner(Context _context, long _scanPeriod, SlightScanCallback _slightScanCallback) {
        boolean useAndroidLScanner;
        if (Build.VERSION.SDK_INT < 18) {
            Log.w(TAG, "Not supported prior to API 18");
            return null;
        }
        if (Build.VERSION.SDK_INT < 21) {
            Log.i(TAG, "This is not Android 5.0.  We are using old scanning APIs");
            useAndroidLScanner = false;
        } else {
            Log.i(TAG,"This Android 5.0.  We are using new scanning APIs");
            useAndroidLScanner = true;
        }

        if (useAndroidLScanner) {
            return new SlightScannerForLollipop(_context,_scanPeriod,_slightScanCallback);
        } else {
            return new SlightScannerForJellyBeanMr2(_context,_scanPeriod,_slightScanCallback);
        }
    }

    public void start() {
        Log.d(TAG, "start() called");
        if (!isScanning) {
            scanLeDevice(true);
        } else {
            Log.d(TAG,"scanning already started");
        }
    }

    @SuppressLint("NewApi")
    public void stop() {
        Log.d(TAG, "stop() called");
        mScanCycleStopTime = 0;
    }

    public boolean getStateScanning() {
        return isScanning;
    }

    protected abstract void stopScan();

    protected abstract boolean deferScanIfNeeded();

    protected abstract void startScan();

    @SuppressLint("NewApi")
    protected void scanLeDevice(final Boolean enable) {
        if (getBluetoothAdapter() == null) {
            Log.e(TAG, "No bluetooth adapter");
            return;
        }

        if (enable) {
            if (deferScanIfNeeded()) {
                return;
            }
            Log.d(TAG,"starting a new scan cycle");
            if (!isScanning) {
                isScanning = true;
                try {
                    if (getBluetoothAdapter().isEnabled()) {
                        try {
                            startScan();
                        } catch(Exception e) {
                            Log.d(TAG,"Internal Android exception scanning");
                        }
                    } else {
                        Log.d(TAG,"Bluetooth is disabled.  Cannot scan.");
                    }
                } catch (Exception e) {
                    Log.d(TAG,"Exception starting bluetooth scan.  Perhaps bluetooth is disabled or unavailable?");
                }
            } else {
                Log.d(TAG,"We are already scanning");
            }
            mScanCycleStopTime = (new Date().getTime() + scanPeriod);
            scheduleScanCycleStop();
        } else {
            Log.d(TAG,"disabling scan");
            isScanning = false;
            stopScan();
        }
    }

    protected abstract void finishScan();


    private void finishScanCycle() {
        Log.d(TAG, "Done with scan cycle");
        slightScanCallback.onEnd();
        if (isScanning) {
            if (getBluetoothAdapter() != null) {
                if (getBluetoothAdapter().isEnabled()) {
                    try {
                        Log.d(TAG, "stopping bluetooth le scan");

                        finishScan();

                    } catch (Exception e) {
                        Log.w(TAG, "Internal Android exception scanning");
                    }
                } else {
                    Log.w(TAG, "Bluetooth is disabled.  Cannot scan for beacons.");
                }
            }
            isScanning = false;
        }
    }

    protected BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            // Initializes Bluetooth adapter.
            final BluetoothManager bluetoothManager =
                    (BluetoothManager) context.getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter == null) {
                Log.w(TAG, "Failed to construct a BluetoothAdapter");
            }
        }
        return mBluetoothAdapter;
    }

    protected void scheduleScanCycleStop() {
        long millisecondsUntilStop = mScanCycleStopTime - (new Date().getTime());
        if (millisecondsUntilStop > 0) {
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scheduleScanCycleStop();
                }
            }, millisecondsUntilStop > 1000 ? 1000 : millisecondsUntilStop);
        } else {
            finishScanCycle();
        }
    }
}
