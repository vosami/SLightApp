package com.syncworks.slightapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syncworks.slightapp.R;

/**
 * Created by vosami on 2015-06-25.
 */
public class LedButtonLayout extends LinearLayout{

    TextView tvBoolEffect, tvNumEffect;
    LedButton ledButton;

    private int ledNum = 0;



    public LedButtonLayout(Context context) {
        this(context,null);
    }

    public LedButtonLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LedButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LedButtonLayout, defStyleAttr, 0);

        LayoutInflater.from(context).inflate(R.layout.layout_led_button,this);
        ledButton = (LedButton) findViewById(R.id.layout_led_button);
        tvBoolEffect = (TextView) findViewById(R.id.layout_tv_effect);
        tvNumEffect = (TextView) findViewById(R.id.layout_tv_effect_num);

        String onText = a.getString(R.styleable.LedButtonLayout_l_on_text);
        if (onText == null) onText = "✓";
        String offText = a.getString(R.styleable.LedButtonLayout_l_off_text);
        if (offText == null) offText = "1";
        ledNum = a.getInt(R.styleable.LedButtonLayout_l_led_num,1);

        boolean isEnable = a.getBoolean(R.styleable.LedButtonLayout_l_enable, true);
        boolean isChecked = a.getBoolean(R.styleable.LedButtonLayout_l_checked, false);
        boolean isEffect = a.getBoolean(R.styleable.LedButtonLayout_l_effect, false);

        int effectNum = a.getInteger(R.styleable.LedButtonLayout_l_effect_num,0);


        ledButton.init(onText, offText, isEnable, isChecked, isEffect, 95);
        tvNumEffect.setText(Integer.toString(effectNum+1));

        if (isEffect) {
            tvBoolEffect.setVisibility(VISIBLE);
            tvNumEffect.setVisibility(VISIBLE);
        } else {
            tvBoolEffect.setVisibility(INVISIBLE);
            tvNumEffect.setVisibility(INVISIBLE);
        }

        ledButton.setOnLedButtonListener(ledButtonListener);
    }

    private LedButton.OnLedButtonListener ledButtonListener = new LedButton.OnLedButtonListener() {
        @Override
        public void onCheckEvent(boolean check, boolean effect) {
            if (effect) {
                tvBoolEffect.setVisibility(VISIBLE);
                tvNumEffect.setVisibility(VISIBLE);
            } else {
                tvBoolEffect.setVisibility(INVISIBLE);
                tvNumEffect.setVisibility(INVISIBLE);
            }
        }
    };

}
