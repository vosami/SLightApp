package com.syncworks.slightapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.syncworks.slightapp.R;
import com.syncworks.slightapp.util.Logger;


/**
 * TODO: document your custom view class.
 */
public class LedBtn extends Button {

    private OnLedBtnListener onLedBtnListener;

    private final static int[] BTN_STATE_CHECKED = {R.attr.btn_state_checked};
    private final static int[] BTN_STATE_ENABLED = {R.attr.btn_state_enabled};
    private final static int[] BTN_STATE_BRIGHT = {R.attr.btn_state_bright};

    private int state = 0;


    private final static int MAX_BRIGHT = 191;
    private final static int DEFAULT_BRIGHT = 95;



    public final static int TYPE_SINGLE_LED = 0;
    public final static int TYPE_RGB_LED = 1;

    private boolean isBtnChecked = false;
    private boolean isBtnEnabled = true;
    private boolean isBtnBright = false;
    private String textOn = "ON";
    private String textOff = "OFF";
    private int type = TYPE_SINGLE_LED;
    private int bright1 = DEFAULT_BRIGHT;
    private int bright2 = DEFAULT_BRIGHT;
    private int bright3 = DEFAULT_BRIGHT;


    private Paint strokePaint = new Paint();
    private Paint fillPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint percentPaint = new Paint();
    private Paint textPaint = new Paint();

    private float centerX = 0;
    private float centerY = 0;
    private float radius = 0;


    public LedBtn(Context context) {
        this(context, null);
    }

    public LedBtn(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LedBtn(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
        syncTextState();
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.LedBtn,defStyle,0);

            textOn = a.getString(R.styleable.LedBtn_text_on);
            textOff = a.getString(R.styleable.LedBtn_text_off);
            isBtnChecked = a.getBoolean(R.styleable.LedBtn_btn_state_checked, false);
            isBtnEnabled = a.getBoolean(R.styleable.LedBtn_btn_state_enabled,true);
            isBtnBright = a.getBoolean(R.styleable.LedBtn_btn_state_bright, false);
            type = a.getInt(R.styleable.LedBtn_type, TYPE_SINGLE_LED);
            bright1 = a.getInt(R.styleable.LedBtn_bright1, DEFAULT_BRIGHT);
            bright2 = a.getInt(R.styleable.LedBtn_bright2,DEFAULT_BRIGHT);
            bright3 = a.getInt(R.styleable.LedBtn_bright3,DEFAULT_BRIGHT);
        }

        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(Color.rgb(220, 220, 220));

        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(Color.rgb(100, 100, 100));

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.rgb(180, 180, 180));

        percentPaint.setStyle(Paint.Style.FILL);
        percentPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        percentPaint.setColor(Color.rgb(255, 211, 0));

        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.rgb(0,0,0));
    }

    private void syncTextState() {
        if (!isBtnEnabled) {
            if (textOff != null) {
                setTextColor(Color.rgb(200,200,200));
                setText(textOff);
            }
        } else if (isBtnBright) {
            setText("");
        } else if (isBtnChecked) {
            if (textOn != null) {
                setTextColor(Color.rgb(255, 255, 255));
                setText(textOn);
            }
        } else {
            if (textOff != null) {
                setTextColor(Color.rgb(0, 0, 0));
                setText(textOff);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchy = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if (!isBtnEnabled) {
                    doNotify();
                } else if (isBtnBright) {
                    doNotify();
                } else if (isBtnChecked){
                    setBtnChecked(false);
                    doClick();
                } else {
                    setBtnChecked(true);
                    doClick();
                }
                syncTextState();
                break;
        }
        return true;
    }

    public void setBtnChecked(boolean isChecked) {
        if (this.isBtnChecked != isChecked) {
            this.isBtnChecked = isChecked;
            refreshDrawableState();
        }
    }

    public void setBtnEnabled(boolean isEnabled) {
        if (this.isBtnEnabled != isEnabled) {
            this.isBtnEnabled = isEnabled;
            if (!isEnabled) {
                this.isBtnChecked = false;
                //this.isBtnBright = false;
            }
            refreshDrawableState();
        }
        syncTextState();
    }

    public void setBtnBright(boolean isBright) {
        if (this.isBtnBright != isBright) {
            this.isBtnBright = isBright;
            refreshDrawableState();
        }
    }

    public boolean getBtnChecked() {
        return isBtnChecked;
    }

    public boolean getBtnEnabled() {
        return isBtnEnabled;
    }

    public boolean getBtnBright() {
        return isBtnBright;
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 3);
        if (isBtnChecked) {
            mergeDrawableStates(drawableState,BTN_STATE_CHECKED);
        }
        if (isBtnEnabled) {
            mergeDrawableStates(drawableState,BTN_STATE_ENABLED);
        }
        if (isBtnBright) {
            mergeDrawableStates(drawableState, BTN_STATE_BRIGHT);
        }
        return drawableState;
    }

    // LedBtn 리스너 설정
    public void setOnLedBtnListener(OnLedBtnListener listener) {
        this.onLedBtnListener = listener;
    }
    // LedBtn 리스너
    public static interface OnLedBtnListener {
        void onClick(View view);
        void onNotify(View view);
    }

    private void doClick() {
        if (onLedBtnListener != null) {
            onLedBtnListener.onClick(this);
        }
    }

    private void doNotify() {
        if (onLedBtnListener != null) {
            onLedBtnListener.onNotify(this);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isBtnBright) {
            if (type == TYPE_SINGLE_LED) {
                float mWidth = getWidth();
                float mHeight = getHeight();

                centerX = (float)(mWidth*0.5);
                centerY = (float)(mHeight*0.5);

                if (mWidth > mHeight) {
                    radius = (float) (mHeight * 0.5);
                } else {
                    radius = (float) (mWidth * 0.5);
                }



                canvas.drawCircle(centerX, centerY, radius, fillPaint);
                canvas.drawCircle(centerX, centerY, radius, strokePaint);
                canvas.drawCircle(centerX, centerY, (float) (radius * 0.9), circlePaint);

               drawPercent(canvas);
                Logger.d(this, "onDraw", mWidth, mHeight);
            }
        }
    }

    private void drawPercent(Canvas canvas) {
        float percentRadius = (float) (radius * 0.9);
        RectF mPercent = new RectF(centerX - percentRadius, centerY-percentRadius,
                centerX + percentRadius, centerY + percentRadius);
        canvas.drawArc(mPercent, 270, 360 * bright1 / MAX_BRIGHT, true, percentPaint);
        canvas.drawArc(mPercent, 270, 360 * bright1 / MAX_BRIGHT, true, strokePaint);
        canvas.drawCircle(centerX, centerY, (float) (radius * 0.6), fillPaint);
        float textSize = (float) (radius*0.5);
        int percent = Math.round((float) bright1 * 100 / MAX_BRIGHT);
        textPaint.setTextSize(textSize);
        canvas.drawText(Integer.toString(percent) + "%", centerX, centerY + (float) (textSize * 0.3), textPaint);
    }

    private void drawRgbPercent(Canvas canvas) {

    }


}
