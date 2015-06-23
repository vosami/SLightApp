package com.syncworks.blelib;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;



/**
 * Created by vosami on 2015-03-19.
 * 블루투스 자동 연결 서비스
 */
public class BleAutoService extends Service implements Runnable, BleConsumer{
    private final static String TAG = BleAutoService.class.getSimpleName();
    // 반복 횟수
    private int count = 0;
    // 블루투스 매니저
    private BleManager bleManager = null;
    // 블루투스 연결 시간 간격
    private int timeGapOfScanPeriod = 5000;

    private String bleAddress = null;



    // 다른 컴포넌트가 startService()를 호출해서 서비스가 시작되면 이 메소드 호출
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bleAddress = intent.getStringExtra(BluetoothLeService.BLE_ADDRESS);
        Log.d(TAG,"onStartCommand BLE_ADDRESS:"+bleAddress);

        bleManager = BleManager.getBleManager(this);
        bleManager.bind(this);
        return super.onStartCommand(intent, flags, startId);
    }
    // 다른 컴포넌트가 bindService()를 호출해서 서비스와 연결을 시도하면 이 메소드 호출
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*@Override
    protected void onHandleIntent(Intent intent) {
        bleAddress = intent.getStringExtra(Define.BLE_ADDRESS);
        Log.d(TAG,"BLE_ADDRESS:"+bleAddress);
    }*/

    // 서비스가 처음 생성시
    @Override
    public void onCreate() {
        super.onCreate();
        timeGapOfScanPeriod = 10000;

        // 스레드를 이용해 반복하여 로그 출력
        Thread bleAutoThread = new Thread(this);
        bleAutoThread.start();

        Log.d(TAG, "onCreate");
    }

    // 서비스가 종료시

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        bleManager.unbind(this);
    }



    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(timeGapOfScanPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 다른 액티비티에서 블루투스 매니저를 사용하고 있지 않다면 실행
            // 또는 블루투스가 사용 가능한 상태라면 실행
            BluetoothAdapter bluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
            if (bleManager.isBoundedExceptMe(this) && bluetoothAdapter.isEnabled()) {
                // 연결 안되어 있는 상태라면
                if (!bleManager.isBleServiceConnected()) {
                    // 블루투스 연결 시도
                    bleManager.bleConnect(bleAddress);
                    Log.d(TAG, "BLE_Not_Connected");
                }
                else {
                    Log.d(TAG, "BLE_Connected");
                }
                Log.d(TAG,"BleAutoService called #"+count++);
            }
        }
    }

    @Override
    public void onBleServiceConnect() {
        Log.d(TAG,"BleServiceConnect");
    }
}
