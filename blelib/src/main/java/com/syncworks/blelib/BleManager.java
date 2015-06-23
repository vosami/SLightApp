package com.syncworks.blelib;

import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by vosami on 2015-03-19.
 * Bluetooth Low Energy API Manager
 * 블루투스 사용을 간편하게 해주는 API 매니저
 */
public class BleManager {
    // 디버그용 태그
    private final static String TAG = BleManager.class.getSimpleName();
    // 기본 블루투스 검색 시간(10초)
    public final static long DEFAULT_SCAN_PERIOD = 1000;

    private Context _context;
    protected static BleManager client = null;
    private final ConcurrentMap<BleConsumer, ConsumerInfo> consumers = new ConcurrentHashMap<>();

    private BluetoothLeService bluetoothLeService = null;

    protected BleNotifier bleNotifier = null;

    public static BleManager getBleManager(Context context) {
        if (client == null) {
            client = new BleManager(context);
        }
        return client;
    }

    // 블루투스 매니저 생성자 - getBleManager 메소드로 생성할 것
    protected BleManager(Context context) {
        _context = context;
    }
    // 해당 안드로이드 장치가 Ble 를 지원하는지 확인
    public boolean checkAvailability() {
        if (Build.VERSION.SDK_INT <18) {
            Log.d(TAG, "Bluetooth LE not supported by this device");
        }
        if (!_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.d(TAG, "Bluetooth LE not supported by this device");
        } else if (((BluetoothManager)_context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()) {
            return true;
        }
        return false;
    }

    public void bind(BleConsumer consumer) {
        if (!checkAvailability()) {
            return;
        }
        synchronized (consumers) {
            ConsumerInfo consumerInfo = consumers.putIfAbsent(consumer, new ConsumerInfo());
            if (consumerInfo != null) {
                Log.d(TAG, "This consumer is already bound");
            }
            else {
                Log.d(TAG, "This consumer is not bound. binding:"+ consumer);
                Intent intent = new Intent(consumer.getApplicationContext(), BluetoothLeService.class);
                consumer.bindService(intent, bluetoothLeServiceConnection ,Context.BIND_AUTO_CREATE);
                Log.d(TAG,"consumer count is now: "+consumers.size());

            }
        }
    }

    public void unbind(BleConsumer consumer) {
        if (!checkAvailability()) {
            return;
        }
        synchronized (consumers) {
            if (consumers.containsKey(consumer)) {
                Log.d(TAG, "Unbind");
                consumer.unbindService(bluetoothLeServiceConnection);
                consumers.remove(consumer);
                if (consumers.size() == 0) {
                    bluetoothLeService = null;
                }
            }
            else {
                Log.d(TAG,"This consumer is not bound to: "+consumer);
            }
        }
    }

    private ServiceConnection bluetoothLeServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                return;
            }
            Log.d(TAG, "I get the SmartLight Service");
            synchronized (consumers) {
                Iterator<Map.Entry<BleConsumer,ConsumerInfo>> iterator = consumers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<BleConsumer, ConsumerInfo> entry = iterator.next();
                    if (!entry.getValue().isConnected) {
                        entry.getKey().onBleServiceConnect();
                        entry.getValue().isConnected = true;
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            //bluetoothLeService = null;
        }
    };

    private final BroadcastReceiver bleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            // 연결 완료 메시지를 받으면
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Log.d(TAG, "Connected");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.d(TAG, "Disconnected");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.d(TAG, "Service Discovered");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Log.d(TAG, "Data Available");
            } else if (BluetoothLeService.ACTION_DATA_WRITE_COMPLETE.equals(action)) {
                Log.d(TAG, "Data Write Complete");
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE_COMPLETE);

        return intentFilter;
    }

    private class ConsumerInfo {
        public boolean isConnected = false;
    }

    /**
     * 현재 액티비티가 블루투스 서비스와 연결되어 있는지 확인
     * @param consumer
     * @return
     */
    public boolean isBound(BleConsumer consumer) {
        synchronized (consumers) {
            return consumer != null && consumers.get(consumer) != null && (bluetoothLeService != null);
        }
    }

    /**
     * 현재 액티비티 또는 서비스 외에 다른 동작과 연결된 것이 있는지 확인
     * @param consumer
     * @return
     */
    public boolean isBoundedExceptMe(BleConsumer consumer) {
        synchronized (consumers) {
            return consumer != null && consumers.size() == 1 && consumers.get(consumer) != null && (bluetoothLeService != null);
        }
    }

    /**
     * 블루투스 서비스와 연결되어 있는 액티비티가 있는지 확인
     * @return
     */
    public boolean isAnyConsumerBound() {
        synchronized (consumers) {
            return consumers.size() > 0 && (bluetoothLeService != null);
        }
    }

    /**
     * 블루투스 서비스가 연결되어 있는지 확인
     * @return
     */
    public boolean isBleServiceConnected() {
        boolean retBool = false;
        synchronized (consumers) {
            if (bluetoothLeService != null) {
                if (bluetoothLeService.getStateConnect() == BluetoothLeService.STATE_CONNECTED) {
                    retBool = true;
                }
            }
        }
        return retBool;
    }

    // 블루투스 연결
    public void bleConnect(String addr) {
        if (bluetoothLeService != null && bluetoothLeService.getStateConnect() == BluetoothLeService.STATE_DISCONNECTED) {
			if (addr.length() >5) {
				bluetoothLeService.connect(addr);
			}
        }
    }
    // 블루투스 해제
    public void bleDisconnect() {
        if (bluetoothLeService != null && bluetoothLeService.getStateConnect() == BluetoothLeService.STATE_CONNECTED) {
            bluetoothLeService.disconnect();
        }
    }
    // 블루투스 연결 상태 확인
    public int getBleConnectState() {
        if (bluetoothLeService != null) {
            return bluetoothLeService.getStateConnect();
        }
        return BluetoothLeService.STATE_DISCONNECTED;
    }

    public void setBleNotifier(BleNotifier notifier) {
        bluetoothLeService.setBleNotifier(notifier);
//        bleNotifier = notifier;
    }

    public BleNotifier getBleNotifier() {
        return bluetoothLeService.getBleNotifier();
//        return this.bleNotifier;
    }
    // 데이터 송신 메소드
    public void writeTxData(byte[] mData) {
        if (bluetoothLeService != null) {
            int length = mData.length;
            byte[] newData = new byte[length];
            for (int i=0;i<length;i++) {
                newData[i] = mData[i];
            }
            bluetoothLeService.writeTxList(newData);
        }
    }

    public void writeName(String name) {
        if (bluetoothLeService != null) {
            bluetoothLeService.writeDeviceName(name);
        }
    }

}
