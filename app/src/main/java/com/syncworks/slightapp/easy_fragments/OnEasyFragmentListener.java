package com.syncworks.slightapp.easy_fragments;

/**
 * Created by vosami on 2015-06-11.
 */
public interface OnEasyFragmentListener {
    public void onModifyName();

    public void onScanStart();

    public void onScanStop();

    public void onSetDeviceItem(String devName, String devAddr);

    public void onSelectLed(int ledNum, boolean state);

    public void onSelectRGB(int ledNum, boolean state);

    public void onBrightRGB(int ledNum, int bright);

    public void onBrightLed(int ledNum, int bright);

    public void onEffect(int effect, int param);

    public void onColorDialog();

    public void onNotDialog();
}
