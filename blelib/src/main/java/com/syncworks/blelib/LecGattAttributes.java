package com.syncworks.blelib;

import java.util.HashMap;

/**
 * Created by vosami on 2015-02-25.
 */
public class LecGattAttributes {
	public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    // LEC 의 Profile
    public final static String LEC_PROFILE_UUID = "713d0000-503e-4c75-ba94-3148f18d941e";
    // Android -> LEC Module
    public final static String LEC_TX_UUID = "713d0003-503e-4c75-ba94-3148f18d941e";
    // LEC Module -> Android
    public final static String LEC_RX_UUID = "713d0002-503e-4c75-ba94-3148f18d941e";
    // LEC 장치 이름 설정
    public final static String LEC_DEV_NAME_UUID = "713d0005-503e-4c75-ba94-3148f18d941e";
    // LEC 버전 정보 (Read Only)
    public final static String LEC_VERSION_UUID = "713d0006-503e-4c75-ba94-3148f18d941e";
    // LEC 송신 파워 출력 정보
    public final static String LEC_TX_POWER_UUID = "713d0007-503e-4c75-ba94-3148f18d941e";

    private static HashMap<String, String> attributes = new HashMap<String, String>();

    static {
        attributes.put(LEC_PROFILE_UUID,"LEC_Service");
        //송수신 정보
        attributes.put(LEC_TX_UUID, "LEC_TX_UUID");
        attributes.put(LEC_RX_UUID, "LEC_RX_UUID");

        attributes.put(LEC_DEV_NAME_UUID, "LEC_DEV_NAME_UUID");
        attributes.put(LEC_VERSION_UUID, "LEC_VERSION_UUID");
        attributes.put(LEC_TX_POWER_UUID, "LEC_TX_POWER_UUID");
    }
    // UUID 확인
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
