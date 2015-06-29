package com.syncworks.slightapp;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.syncworks.blelib.BleConsumer;
import com.syncworks.slightapp.easy_fragments.BleSetFragment;
import com.syncworks.slightapp.easy_fragments.BrightFragment;
import com.syncworks.slightapp.easy_fragments.LedSelectFragment;
import com.syncworks.slightapp.easy_fragments.OnEasyFragmentListener;
import com.syncworks.slightapp.util.Logger;
import com.syncworks.slightapp.util.SLightPref;
import com.syncworks.slightapp.widget.StepView;


public class EasyActivity extends ActionBarActivity implements BleConsumer, OnEasyFragmentListener{
    // 상단 메뉴
    private Menu menu = null;
    // 연결 상태 확인
    private boolean connectState = false;

    /* 장치 설정 확인*/
    SLightPref appPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);
        appPref = new SLightPref(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findView();
        createFragment();

        //showOverLay();
    }

    private StepView stepView = null;

    private void findView() {
        stepView = (StepView) findViewById(R.id.easy_step_view);
        StepView.OnStepViewTouchListener stepViewTouchListener = new StepView.OnStepViewTouchListener() {
            @Override
            public void onStepViewEvent(int clickStep) {
                Logger.v(this,"",clickStep);
            }
        };
        stepView.setOnStepViewTouchListener(stepViewTouchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_easy, menu);
        this.menu = menu;
        setConnectIcon(connectState);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_connect:
                if (connectState) {

                } else {

                }
                break;
            case R.id.action_help:
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (connectState) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_connected_red));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_disconnected));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * ActionBar 의 연결 상태 아이콘 설정
     * @param connectState 연결상태(true:연결, false:끊김)
     */
    private void setConnectIcon(boolean connectState) {
        this.connectState = connectState;
        invalidateOptionsMenu();
    }
    /**********************************************************************************************
     * 블루투스 관련 설정
     *********************************************************************************************/
    @Override
    public void onBleServiceConnect() {

    }

    /**********************************************************************************************
     * Fragment 관련 설정
     *********************************************************************************************/

    private BleSetFragment fragment1st;
    private LedSelectFragment fragment2nd;
    private BrightFragment fragment3rd;
//    private EffectFragment fragment4th;

    private void createFragment() {
        String deviceName, deviceAddr;
        deviceName = appPref.getString(SLightPref.DEVICE_NAME);
        deviceAddr = appPref.getString(SLightPref.DEVICE_ADDR);
        fragment1st = BleSetFragment.newInstance(deviceName, deviceAddr);
        fragment2nd = LedSelectFragment.newInstance();
        fragment3rd = BrightFragment.newInstance("","");
//        fragment4th = EffectFragment.newInstance();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.easy_ll_fragment, fragment3rd);
        fragmentTransaction.commit();
    }

    @Override
    public void onModifyName() {

    }

    @Override
    public void onScanStart() {

    }

    @Override
    public void onScanStop() {

    }

    @Override
    public void onSetDeviceItem(String devName, String devAddr) {

    }

    @Override
    public void onSelectLed(int ledNum, boolean state) {

    }

    @Override
    public void onSelectRGB(int ledNum, boolean state) {

    }

    @Override
    public void onBrightRGB(int ledNum, int bright) {

    }

    @Override
    public void onBrightLed(int ledNum, int bright) {

    }

    @Override
    public void onEffect(int effect, int param) {

    }

    @Override
    public void onColorDialog() {

    }

    @Override
    public void onNotDialog() {

    }

    private void showOverLay() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_help_led_select);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.overlay_help);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
