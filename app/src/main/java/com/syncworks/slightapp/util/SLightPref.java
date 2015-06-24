package com.syncworks.slightapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.syncworks.slightapp.R;

/**
 * Created by vosami on 2015-06-24.
 * 스마트 라이트 설정 값 저장
 */
public class SLightPref {
    private final static String PREF_NAME = "com.syncworks.slightapp";

    private final static String FIRST_EXEC = "first_exe";

    public final static String EASY_STEP1_EXEC = "easy_step1_exec";
    public final static String EASY_STEP2_EXEC = "easy_step2_exec";
    public final static String EASY_STEP3_EXEC = "easy_step3_exec";
    public final static String EASY_STEP4_EXEC = "easy_step4_exec";
    public final static String EASY_STEP5_EXEC = "easy_step5_exec";

    // 장치 이름 저장
    public final static String DEVICE_NAME = "dev_name";
    // 장치 주소 저장
    public final static String DEVICE_ADDR = "dev_addr";

    public final static String[] DEVICE_LED_NAME = {"key_led1","key_led2","key_led3","key_led4",
            "key_led5","key_led6","key_led7","key_led8","key_led9"};
    public final static String[] DEVICE_COLOR_LED_NAME = {"key_color_led1","key_color_led2","key_color_led3"};

    private static Context context;

    private SharedPreferences pref = null;

    /**
     * 생성자
     * @param c 컨텍스트 설정
     */
    public SLightPref(Context c) {
        context = c;
        pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        // 처음 실행하는 것이라면 환경 변수 새로 설정
        if (!getBoolean(FIRST_EXEC)) {
            initPref();
        }
    }

    private void initPref() {
        // 최초 실행 확인 변수 설정
        putBoolean(FIRST_EXEC, true);
        // LED 장치 이름 설정
        Resources r = context.getResources();

        // 쉽게 설정하기 최초 실행 확인
        putBoolean(EASY_STEP1_EXEC,false);
        putBoolean(EASY_STEP2_EXEC,false);
        putBoolean(EASY_STEP3_EXEC,false);
        putBoolean(EASY_STEP4_EXEC,false);
        putBoolean(EASY_STEP5_EXEC,false);

        // 연결 장치 이름 설정
        putString(DEVICE_NAME,r.getString(R.string.ble_default_device_name));
        // 연결 장치 주소 설정
        putString(DEVICE_ADDR,r.getString(R.string.ble_default_device_address));

        putString(DEVICE_LED_NAME[0],r.getString(R.string.led1_txt));
        putString(DEVICE_LED_NAME[1],r.getString(R.string.led2_txt));
        putString(DEVICE_LED_NAME[2],r.getString(R.string.led3_txt));
        putString(DEVICE_LED_NAME[3],r.getString(R.string.led4_txt));
        putString(DEVICE_LED_NAME[4],r.getString(R.string.led5_txt));
        putString(DEVICE_LED_NAME[5],r.getString(R.string.led6_txt));
        putString(DEVICE_LED_NAME[6],r.getString(R.string.led7_txt));
        putString(DEVICE_LED_NAME[7],r.getString(R.string.led8_txt));
        putString(DEVICE_LED_NAME[8],r.getString(R.string.led9_txt));
        putString(DEVICE_COLOR_LED_NAME[0],r.getString(R.string.cled1_txt));
        putString(DEVICE_COLOR_LED_NAME[1],r.getString(R.string.cled2_txt));
        putString(DEVICE_COLOR_LED_NAME[2],r.getString(R.string.cled3_txt));
    }

    public void putString(String key, String val) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, val);
        editor.apply();
    }

    public String getString(String key) {
        // 데이터가 없다면 "NONE"을 반환, 에러 발생하면 "ERROR" 반환
        try {
            return pref.getString(key, "NONE");
        } catch (Exception e) {
            Log.e(PREF_NAME, "Error getString");
            return "ERROR";
        }
    }

    public void putBoolean(String key, boolean val) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key,false);
    }
}
