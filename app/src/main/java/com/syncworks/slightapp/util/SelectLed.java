package com.syncworks.slightapp.util;


import static com.syncworks.slightapp.util.Define.*;
/**
 * Created by Kim on 2015-06-28.
 * Led Select
 */
public class SelectLed {
    private boolean[] rgb;
    private boolean[] led;

    public SelectLed() {
        init();
    }
    public SelectLed(int type, int led) {
        init();

    }

    private void init() {
        rgb = new boolean[NUMBER_OF_COLOR_LED];
        led = new boolean[NUMBER_OF_SINGLE_LED];

        for (int i=0;i<NUMBER_OF_COLOR_LED;i++) {
            rgb[i] = false;
        }
        for (int i=0;i<NUMBER_OF_SINGLE_LED;i++) {
            led[i] = false;
        }
    }
    // 단색 LED 체크 설정
    public void ledSet(int ledNum) {
        if (ledNum >= 0 && ledNum <= NUMBER_OF_SINGLE_LED) {
            led[ledNum] = true;
            switch (ledNum) {
                case 0:
                    rgb[0] = false;
                    break;
                case 1:
                    rgb[0] = false;
                    break;
                case 2:
                    rgb[0] = false;
                    break;
                case 3:
                    rgb[1] = false;
                    break;
                case 4:
                    rgb[1] = false;
                    break;
                case 5:
                    rgb[1] = false;
                    break;
                case 6:
                    rgb[2] = false;
                    break;
                case 7:
                    rgb[2] = false;
                    break;
                case 8:
                    rgb[2] = false;
                    break;
            }
        }
    }
    // RGB LED 체크 설정
    public void rgbSet(int ledNum) {
        if (ledNum >=0 && ledNum <=NUMBER_OF_COLOR_LED) {
            rgb[ledNum] = true;
            switch (ledNum) {
                case 0:
                    led[0] = false;
                    led[1] = false;
                    led[2] = false;
                    break;
                case 1:
                    led[3] = false;
                    led[4] = false;
                    led[5] = false;
                    break;
                case 2:
                    led[6] = false;
                    led[7] = false;
                    led[8] = false;
                    break;
            }
        }
    }

    public void ledClear(int ledNum) {
        if (ledNum >= 0 && ledNum <= NUMBER_OF_SINGLE_LED) {
            led[ledNum] = false;
        }
    }

    public void rgbClear(int ledNum) {
        if (ledNum >=0 && ledNum <= NUMBER_OF_COLOR_LED) {
            led[ledNum] = false;
        }
    }

    public void ledSet(String binary) {
        if (binary.length() == NUMBER_OF_SINGLE_LED) {
            for (int i=0;i<NUMBER_OF_SINGLE_LED;i++) {
                char m = binary.charAt(i);
                if (m == '1') {
                    ledSet(i);
                } else {
                    ledClear(i);
                }
            }
        }
    }

    public void rgbSet(String binary) {
        if (binary.length() == NUMBER_OF_COLOR_LED) {
            for (int i=0;i<NUMBER_OF_COLOR_LED;i++) {
                char m = binary.charAt(i);
                if (m == '1') {
                    rgbSet(i);
                } else {
                    rgbClear(i);
                }
            }
        }
    }

    public boolean getLed(int ledNum) {
        if (ledNum >= NUMBER_OF_SINGLE_LED) {
            return false;
        }
        return led[ledNum];
    }

    public boolean getRgbState(int ledNum) {
        if (ledNum >= NUMBER_OF_COLOR_LED) {
            return false;
        }
        return rgb[ledNum];
    }
}
