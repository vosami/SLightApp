package com.syncworks.blelib;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vosami on 2015-02-25.
 * 블루투스 서비스 클래스
 *
 */
public class BluetoothLeService extends Service {
    // 태그용 문자열
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    // 블루투스 관련 설정
    public final static String BLE_ADDRESS = "BluetoothAddress";
    // 선 정의된 연결 상태
    public final static int STATE_DISCONNECTED = 0;
    public final static int STATE_CONNECTING = 1;
    public final static int STATE_CONNECTED = 2;
    // 연결 상태에 따른 메시지
    public final static String ACTION_GATT_CONNECTED =
            "GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "EXTRA_DATA";
    public final static String ACTION_DATA_WRITE_COMPLETE =
            "GATT_WRITE_COMPLETE";

    // UUID 정의
    public final static UUID UUID_LEC_SERVICE = UUID.fromString(LecGattAttributes.LEC_PROFILE_UUID);
    public final static UUID UUID_LEC_TX = UUID.fromString(LecGattAttributes.LEC_TX_UUID);
    public final static UUID UUID_LEC_RX = UUID.fromString(LecGattAttributes.LEC_RX_UUID);
    public final static UUID UUID_LEC_DEV_NAME = UUID.fromString(LecGattAttributes.LEC_DEV_NAME_UUID);
    public final static UUID UUID_LEC_VERSION = UUID.fromString(LecGattAttributes.LEC_VERSION_UUID);
    public final static UUID UUID_LEC_TX_POWER = UUID.fromString(LecGattAttributes.LEC_TX_POWER_UUID);

    // 송신 배열 (데이터 송신 명령시 배열에 우선 넣고 순서에 따라 송신)
    private List<byte[]> txDataList;


    // 송수신 캐릭터 확인
    private BluetoothGattCharacteristic charLecTx = null;
    private BluetoothGattCharacteristic charLecRx = null;
    private BluetoothGattCharacteristic charLecDevName = null;
    private BluetoothGattCharacteristic charLecDevVer = null;
    private BluetoothGattCharacteristic charLecTxPower = null;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    // 연결 대상 블루투스 장치 주소
    private String mBluetoothDeviceAddress;
    // 초기 연결 상태는 Disconnected 상태로 설정
    private int mConnectionState = STATE_DISCONNECTED;

    private BleNotifier bleNotifier = null;

    // 블루투스 장치 상태 변화에 따른 콜백 함수 설정
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        // 연결 상태가 변화될 경우
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            //super.onConnectionStateChange(gatt, status, newState);
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {                 // 장치와 연결되면
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;     // 연결 상태를 연결로 설정
                Log.d(TAG,"Connected to GATT server");  // 로그 메시지 표시
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());
//                broadcastUpdate(intentAction);          // 메시지 전달
                if (bleNotifier !=null) {
                    bleNotifier.bleConnected();
                }
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {       // 장치와 연결이 끊어지면
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;  // 연결 상태를 연결안됨으로 설정
//                broadcastUpdate(intentAction);          // 메시지 전달
                if (bleNotifier !=null) {
                    bleNotifier.bleDisconnected();
                }
                Log.d(TAG, "Disconnected from GATT server.");   // 로그 메시지 표시
            }
        }
        // 블루투스 장치 검색 콜백 함수
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            //super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                getGattService();
                if (bleNotifier !=null) {
                    bleNotifier.bleServiceDiscovered();
                }
//				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.d(TAG, "onServicesDiscovered received: " + status);
            }
        }
        // 메시지 수신 콜백 함수
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG, "Data read");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (bleNotifier !=null) {
                    bleNotifier.bleDataAvailable();
                }
//                broadcastUpdate(ACTION_DATA_AVAILABLE,characteristic);
            }
        }
        // 메시지가 변경될 경우 콜백 함수
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //super.onCharacteristicChanged(gatt, characteristic);
//            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            if (UUID_LEC_RX.equals(characteristic.getUuid())) {
                final byte[] rx = characteristic.getValue();
                Log.d(TAG, "Data Changed"+bytesToHex(rx));
            }


            if (bleNotifier !=null) {
                bleNotifier.bleDataAvailable();
            }
        }

        /**
         * 바이트 to 헥스 변환
         */
        final protected char[] hexArray = "0123456789ABCDEF".toCharArray();
        public String bytesToHex(byte[] bytes) {
            char[] hexChars = new char[bytes.length * 2];
            for ( int j = 0; j < bytes.length; j++ ) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }

        // 쓰기 작업이 완료될 경우 콜백 함수
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
//                broadcastUpdate(ACTION_DATA_WRITE_COMPLETE, characteristic);
                Log.d(TAG,"Write Complete"+status);
                if (txDataList.size() > 0) {
                    txDataList.remove(0);
                }
                if (txDataList.size() > 0) {
                    writeTxData(txDataList.get(0));
                }

                if (bleNotifier !=null) {
                    bleNotifier.bleDataWriteComplete();
                }
            }

        }
    };
    // Activity 클래스에 메시지 전달
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    // Activity 클래스에 메시지 전달
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // For all other profiles, writes the data formatted in HEX.
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for(byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));
            intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
        }
        sendBroadcast(intent);
    }
    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }
    // 서비스 바인드
    @Override
    public IBinder onBind(Intent intent) {
        txDataList = new ArrayList<>();
        return mBinder;
    }
    // 서비스 언바인드
    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * 블루투스 어댑터 초기화
     * @return 정상 초기화시 true, 블루투스 장치를 초기화 할수 없다면 false
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        Log.d(TAG,"BLE 지원");
        return true;
    }

    /**
     * 블루투스 장치 GATT 서버에 연결 시도.
     *
     * @param address 블루투스 장치의 주소.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * 연결을 끊는다.
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * 송신 명령
     * @param characteristic
     */
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
	// 수신 데이터 Notification 설정 및 수신 데이터 확인 설정
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
		if (UUID_LEC_RX.equals(characteristic.getUuid())) {
			BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
					UUID.fromString(LecGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		}
    }

    // 송수신 Profile 을 확인합니다.
    private void getGattService() {
        // LEC 서비스를 지원하지 않는다면 중지
        BluetoothGattService gattService = mBluetoothGatt.getService(UUID_LEC_SERVICE);
        if (gattService == null) {
            Log.d(TAG,"getGattService" + "NULL");
            return;
        }

        charLecTx = gattService.getCharacteristic(UUID_LEC_TX);
        charLecRx = gattService.getCharacteristic(UUID_LEC_RX);
        charLecDevName = gattService.getCharacteristic(UUID_LEC_DEV_NAME);
        charLecDevVer = gattService.getCharacteristic(UUID_LEC_VERSION);
        charLecTxPower = gattService.getCharacteristic(UUID_LEC_TX_POWER);

		setCharacteristicNotification(charLecRx, true);
		readCharacteristic(charLecRx);
        Log.d(TAG,"getGattService : " + "Success");
    }
    public boolean isAcquireNameService() {
        if (charLecDevName != null) {
            return true;
        }
        return false;
    }
    // 장치 이름을 새로 설정합니다.
    public void writeDeviceName(String mDevName) {
        // 연결상태이며, Profile 캐릭터를 확인했으며, 데이터 길이가 20보다 작으면 데이터 송신
        if (mConnectionState == STATE_CONNECTED &&
                charLecDevName != null) {
            String txStr;
            if (mDevName.length() < 20) {
                txStr = mDevName + " \0";
            } else {
                txStr = mDevName.substring(0,19);
            }
            byte[] txBytes = txStr.getBytes();
            Log.d(TAG, "writeDeviceName"+txStr);
            charLecDevName.setValue(txBytes);
            mBluetoothGatt.writeCharacteristic(charLecDevName);
        }
    }
    // 장치의 송신 출력을 설정합니다.
    // 0~1 비트는 송신 출력, 7비트는 Advertise 모드 (1:GAP_ADTYPE_FLAGS_GENERAL, 0:GAP_ADTYPE_FLAGS_LIMITED)
    public void writeDeviceTxPower(int mDevTxPower) {
        if (mConnectionState == STATE_CONNECTED && charLecTxPower !=null) {
            byte[] txBytes = new byte[1];
            txBytes[0] = (byte)mDevTxPower;
            charLecTxPower.setValue(txBytes);
            mBluetoothGatt.writeCharacteristic(charLecTxPower);
        }
    }

    public void writeTxData(byte[] mData) {
        if (mConnectionState == STATE_CONNECTED &&
                charLecTx != null) {
//            Log.d(TAG, "writeTxData");
            charLecTx.setValue(mData);
            boolean retBool = mBluetoothGatt.writeCharacteristic(charLecTx);
            Log.d(TAG, "writeTxData" + retBool);
        }
    }

    public void writeTxList(byte[] mData) {
        if (mConnectionState == STATE_CONNECTED &&
                charLecTx != null) {
            /*byte[] mData1 = {0,1,2,3,4,5};
            byte[] mData2 = {1,1,2,3,4,5};
            byte[] mData3 = {2,1,2,3,4,5};
            byte[] mData4 = {3,1,2,3,4,5};

            txDataList.add(mData1);
            txDataList.add(mData2);
            txDataList.add(mData3);
            txDataList.add(mData4);
            txDataList.add(mData);*/

            txDataList.add(mData);
            if (txDataList.size() == 1) {
                Log.i(TAG,"write");
                writeTxData(mData);
            }
        }
    }

    private void pushTxList(byte[] mData) {
        txDataList.add(mData);
    }
    private byte[] pullTxList() {
        byte[] retData = null;
        if (txDataList.size() > 0) {
            retData = txDataList.get(0);
            txDataList.remove(0);
        }
        return retData;
    }

    // 연결 상태 반환
    public int getStateConnect() {
        return mConnectionState;
    }

	// LEC 블루투스 서비스 반환
	public BluetoothGattService getSupportedGattService() {
		if (mBluetoothGatt == null) {
			return null;
		}
		return mBluetoothGatt.getService(UUID_LEC_SERVICE);
	}

    public void setBleNotifier(BleNotifier notifier) {
        bleNotifier = notifier;
    }

    public BleNotifier getBleNotifier() {
        return bleNotifier;
    }
}
