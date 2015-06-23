package com.syncworks.blelib;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

/**
 * Created by vosami on 2015-03-19.
 * 블루투스 사용 인터페이스
 */
public interface BleConsumer {

    public void onBleServiceConnect();

    public Context getApplicationContext();

    public void unbindService(ServiceConnection connection);

    public boolean bindService(Intent intent, ServiceConnection connection, int mode);
}
