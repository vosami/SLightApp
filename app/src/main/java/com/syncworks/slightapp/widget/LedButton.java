package com.syncworks.slightapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.syncworks.slightapp.R;

/**
 * Created by vosami on 2015-06-24.
 * 3가지 상태를 가질 수 있는 버튼
 */
public class LedButton extends View {

    // LED 버튼 상태 (ON-Checked, OFF-Unchecked, EFFECT-퍼센트)
    public enum LedButtonState {
        ON, OFF, EFFECT
    }
    // 현재 버튼 상태
    private LedButtonState ledState;
    // 밝기 정보, 효과 정보
    private int _Bright, _Effect;

    private String onText, offText;
    private int selEffect, selBright;
    // 페인트
    private Paint paint = null;

    public LedButton(Context context) {
        this(context,null);
    }

    public LedButton(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public LedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        ledState = LedButtonState.OFF;
        _Bright = 95;
        _Effect = 0;
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.attr_led_button, defStyle, 0);
        onText = a.getString(R.styleable.attr_led_button_text_on);
        offText = a.getString(R.styleable.attr_led_button_text_off);
        selBright = a.getInt(R.styleable.attr_led_button_bright, 95);
        selEffect = a.getInt(R.styleable.attr_led_button_effect,0);
    }

    private void setLedState(LedButtonState ledState) {
        this.ledState = ledState;
        invalidate();
    }

    private void setBright(int bright) {
        this._Bright = bright;
        invalidate();
    }

    private void setEffect(int effect) {
        this._Effect = effect;
        invalidate();
    }

    RectF rectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        float padding = (float) (mWidth*0.05);
        float round = (float) (mWidth*0.1);
        paint.setStrokeWidth(2);
        rectF.set(padding, padding, mWidth - padding, mHeight - padding);
//        paint.setColor(Color.rgb(252, 154, 36));
        paint.setColor(Color.rgb(255,255,255));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, round, round, paint);
        paint.setColor(Color.rgb(170,170,170));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(rectF, round, round, paint);
    }

    private OnLedButtonListener mListener = null;

    public interface OnLedButtonListener {
        void onChecked();
        void onUnChecked();
        void onEffect(int brightness, int pattern);
    }

    public void setOnLedButtonListener(OnLedButtonListener listener) {
        mListener = listener;
    }

}
