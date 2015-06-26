package com.syncworks.slightapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.syncworks.slightapp.R;
import com.syncworks.slightapp.util.Logger;

/**
 * TODO: document your custom view class.
 */
public class RgbButton extends View {

    float btnHeight;

    public final static int DEFAULT_BRIGHT = 95;

    private OnLedButtonListener ledButtonListener;

    private String onText;
    private String offText;
    private boolean isEnable;
    private boolean isChecked;
    private boolean isEffect;
    private int redBright;
    private int greenBright;
    private int blueBright;
    private int pattern;


    private Paint clearPaint;
    private Paint shadowPaint;
    private Paint fillPaint;
    private Paint strokePaint;
    private Paint redPercentPaint;
    private Paint greenPercentPaint;
    private Paint bluePercentPaint;
    private Paint percentStrokePaint;
    private TextPaint mTextPaint;

    private RectF rect;
    private RectF shadowRect;


    public RgbButton(Context context) {
        super(context);
        init(null, 0);
    }

    public RgbButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RgbButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.LedButton, defStyle, 0);

            onText = a.getString(R.styleable.LedButton_on_text);
            if (onText == null) onText = "";
            offText = a.getString(R.styleable.LedButton_off_text);
            if (offText == null) offText = "";
            isEnable = a.getBoolean(R.styleable.LedButton_enable, true);
            isChecked = a.getBoolean(R.styleable.LedButton_check, false);
            isEffect = a.getBoolean(R.styleable.LedButton_effect, false);
            redBright = a.getInt(R.styleable.LedButton_red_bright, DEFAULT_BRIGHT);
            greenBright = a.getInt(R.styleable.LedButton_green_bright,DEFAULT_BRIGHT);
            blueBright = a.getInt(R.styleable.LedButton_blue_bright,DEFAULT_BRIGHT);
            pattern = a.getInt(R.styleable.LedButton_pattern, 0);

            a.recycle();
        } else {
            onText = "✓";
            offText = "0";
            isEnable = true;
            isChecked = false;
            isEffect = false;
            redBright = DEFAULT_BRIGHT;
            greenBright = DEFAULT_BRIGHT;
            blueBright = DEFAULT_BRIGHT;
            pattern = 0;
        }
        clearPaint = new Paint();
        clearPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        clearPaint.setStyle(Paint.Style.FILL);
        clearPaint.setColor(Color.rgb(255,255,255));

        shadowPaint = new Paint();
        shadowPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setStyle(Paint.Style.FILL);

        fillPaint = new Paint();
        fillPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint();
        strokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(1);

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        redPercentPaint = new Paint();
        redPercentPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        redPercentPaint.setStyle(Paint.Style.FILL);
        redPercentPaint.setColor(Color.rgb(240,49,49));

        greenPercentPaint = new Paint();
        greenPercentPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        greenPercentPaint.setStyle(Paint.Style.FILL);
        greenPercentPaint.setColor(Color.rgb(138,189,0));

        bluePercentPaint = new Paint();
        bluePercentPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        bluePercentPaint.setStyle(Paint.Style.FILL);
        bluePercentPaint.setColor(Color.rgb(36,173,222));

        percentStrokePaint = new Paint();
        percentStrokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        percentStrokePaint.setStyle(Paint.Style.FILL);
        percentStrokePaint.setColor(Color.rgb(200,200,200));

        rect = new RectF(0,0,0,0);
        shadowRect = new RectF(0,0,0,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // width 진짜 크기 구하기
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = 0;
        switch(widthMode) {
            case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
                widthSize = widthMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                widthSize = 100;
                break;
            case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                widthSize = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }

        // height 진짜 크기 구하기
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = 0;
        switch(heightMode) {
            case MeasureSpec.UNSPECIFIED:    // mode 가 셋팅되지 않은 크기가 넘어올때
                heightSize = heightMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:        // wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
                heightSize = (int)(widthSize*0.778);    // 2.7로 나눔
                Logger.d(this,"onMeasure",widthSize,heightSize);
                break;
            case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
        float padding = (float) (widthSize * 0.05 * 0.37);
        float paddingHorizontal = (float) (widthSize * 0.1);
        float shadowPadding = (float) (widthSize * 0.03 * 0.37);
        rect.set(paddingHorizontal, padding, widthSize - paddingHorizontal, (float) (heightSize / 2.1 - padding));
        shadowRect.set(paddingHorizontal + shadowPadding, padding + shadowPadding, widthSize - paddingHorizontal + shadowPadding, (float) (heightSize / 2.1 - padding + shadowPadding));
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String mText;

        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();

        btnHeight = (float) (mHeight/2.1);


        float radius = (float) (mWidth * 0.2 * 0.37);

        float textX;

        mTextPaint.setTextSize((float) (mWidth * 0.5 * 0.37));

        canvas.drawRect(0,0,mWidth,mHeight,clearPaint);

        if (isEffect) {
            if (isEnable) {
                if (isChecked) {
                    textX = (float) (mWidth * 0.5);
                    fillPaint.setShader(new LinearGradient(0, 0, 0, btnHeight, Color.rgb(83, 147, 63), Color.rgb(112, 198, 86), Shader.TileMode.MIRROR));
                    strokePaint.setColor(Color.rgb(0, 0, 0));
                    strokePaint.setStrokeWidth(5);
                    mTextPaint.setColor(Color.rgb(164, 2, 85));
                    mText = onText;
                } else {
                    textX = (float) (mWidth * 0.5);
                    //fillPaint.setColor(Color.rgb(255,255,255));
                    fillPaint.setShader(new LinearGradient(0, 0, 0, btnHeight, Color.rgb(255,255,255), Color.rgb(255,255,255), Shader.TileMode.MIRROR));
                    strokePaint.setColor(Color.rgb(150, 150, 150));
                    strokePaint.setStrokeWidth(1);
                    mTextPaint.setColor(Color.rgb(30, 30, 30));
                    shadowPaint.setColor(Color.rgb(100, 100, 100));
                    canvas.drawRoundRect(shadowRect, radius, radius, shadowPaint);
                    mText = offText;
                }
            } else {
                textX = (float) (mWidth * 0.5);
                fillPaint.setColor(Color.rgb(195, 195, 195));
                strokePaint.setColor(Color.rgb(150, 150, 150));
                strokePaint.setStrokeWidth(1);
                mTextPaint.setColor(Color.rgb(127, 127, 127));
                mText = offText;
            }
        } else {
            textX = (float) (mWidth * 0.5);
            if (isEnable) {
                if (isChecked) {
                    fillPaint.setShader(new LinearGradient(0, 0, 0, btnHeight, Color.rgb(83, 147, 63), Color.rgb(112, 198, 86), Shader.TileMode.MIRROR));
                    strokePaint.setColor(Color.rgb(0, 0, 0));
                    strokePaint.setStrokeWidth(5);
                    mTextPaint.setColor(Color.rgb(164, 2, 85));
                    mText = onText;
                } else {
                    fillPaint.setShader(new LinearGradient(0, 0, 0, btnHeight, Color.rgb(224, 209, 178), Color.rgb(210, 178, 110), Shader.TileMode.MIRROR));
                    strokePaint.setColor(Color.rgb(150, 150, 150));
                    strokePaint.setStrokeWidth(1);
                    mTextPaint.setColor(Color.rgb(30, 30, 30));
                    shadowPaint.setColor(Color.rgb(100, 100, 100));
                    canvas.drawRoundRect(shadowRect, radius, radius, shadowPaint);
                    mText = offText;
                }

            } else {
                fillPaint.setColor(Color.rgb(195, 195, 195));
                strokePaint.setColor(Color.rgb(150, 150, 150));
                strokePaint.setStrokeWidth(1);
                mTextPaint.setColor(Color.rgb(127, 127, 127));
                mText = offText;
            }
        }

        canvas.drawRoundRect(rect, radius, radius, fillPaint);
        if (isEffect & isEnable & !isChecked) {
            float redPercent = (float)((btnHeight - (mWidth * 0.05 * 0.37)) + ((float)redBright/191)*((mWidth * 0.1 * 0.37)-btnHeight));
            float greenPercent = (float)((btnHeight - (mWidth * 0.05 * 0.37)) + ((float)greenBright/191)*((mWidth * 0.1 * 0.37)-btnHeight));
            float bluePercent = (float)((btnHeight - (mWidth * 0.05 * 0.37)) + ((float)blueBright/191)*((mWidth * 0.1 * 0.37)-btnHeight));
            canvas.drawRect((float)(mWidth*0.2),(float)(mWidth * 0.05 * 0.37),(float)(mWidth*0.8),(float)(btnHeight - (mWidth * 0.05 * 0.37)),percentStrokePaint);
            canvas.drawRect((float) (mWidth * 0.22), redPercent, (float) (mWidth * 0.38), (float) (btnHeight - (mWidth * 0.05 * 0.37)), redPercentPaint);
            canvas.drawRect((float) (mWidth * 0.42), greenPercent, (float) (mWidth * 0.58), (float) (btnHeight - (mWidth * 0.05 * 0.37)), greenPercentPaint);
            canvas.drawRect((float) (mWidth * 0.62), bluePercent, (float) (mWidth * 0.78), (float) (btnHeight - (mWidth * 0.05 * 0.37)), bluePercentPaint);
            int id = 0;
            switch (pattern) {
                case 0:
                    id = R.drawable.ic_pattern1;
                    break;
                case 1:
                    id = R.drawable.ic_pattern2;
                    break;
                case 2:
                    id = R.drawable.ic_pattern3;
                    break;
                case 3:
                    id = R.drawable.ic_pattern4;
                    break;
                case 4:
                    id = R.drawable.ic_pattern5;
                    break;
                default:
                    id = R.drawable.ic_pattern1;
                    break;
            }
            Bitmap mBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getContext().getResources(), id), (int)(mWidth*0.9 * 0.37), (int) (mWidth*0.9 * 0.37), false);
            canvas.drawBitmap(mBitmap, (float)(mWidth*0.9 * 0.37), (float)(mHeight*0.524), fillPaint);
        }
        canvas.drawRoundRect(rect, radius, radius, strokePaint);
        canvas.drawText(mText, textX, (float) (btnHeight * 0.7), mTextPaint);
    }

    private int oldY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchy = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Logger.d(this, "ACTION_DOWN", touchy);
                oldY = touchy;
                break;
            case MotionEvent.ACTION_UP:
                Logger.d(this,"ACTION_UP",touchy);
                if (oldY < btnHeight && touchy < btnHeight) {
                    if (isChecked) {
                        setChecked(false);
                    } else {
                        if (isEffect) {
                            isEffect = false;
                            redBright = DEFAULT_BRIGHT;
                            greenBright = DEFAULT_BRIGHT;
                            blueBright = DEFAULT_BRIGHT;
                        }
                        setChecked(true);
                    }
                    doCheckEvent();
                    this.invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Logger.d(this,"ACTION_MOVE",touchy);
                break;

        }


        return true;
    }


    // 체크 상태 설정
    public void setChecked(boolean check) {
        isChecked = check;
        if (check) {
            pattern = 0;
            redBright = DEFAULT_BRIGHT;
            greenBright = DEFAULT_BRIGHT;
            blueBright = DEFAULT_BRIGHT;
            isEffect = false;
        }
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
        if (!isEnable) {
            pattern = 0;
            redBright = DEFAULT_BRIGHT;
            greenBright = DEFAULT_BRIGHT;
            blueBright = DEFAULT_BRIGHT;
            isEffect = false;
        }
    }

    // 밝기 설정
    public void setBright(int red, int green, int blue) {
        if (red < 0) {
            this.redBright = 0;
        } else if (red > 191) {
            this.redBright = 191;
        } else {
            this.redBright = red;
        }
        if (green < 0) {
            this.greenBright = 0;
        } else if (green > 191) {
            this.greenBright = 191;
        } else {
            this.greenBright = green;
        }
        if (blue < 0) {
            this.blueBright = 0;
        } else if (blue > 191) {
            this.blueBright = 191;
        } else {
            this.blueBright = blue;
        }
    }

    // 효과 설정
    public void setEffect(boolean effect) {
        isEffect = effect;
    }
    // 패턴 설정
    public void setPattern(int pattern) {
        this.pattern = pattern;
    }
    // 효과가 설정되었는지 확인
    public boolean getEffect() {
        return isEffect;
    }
    // 
    public int getPattern() {
        return this.pattern;
    }


    public void init(String onText, String offText, boolean isEnable, boolean isChecked, boolean isEffect, int red, int green, int blue) {
        this.onText = onText;
        this.offText = offText;
        this.isEnable = isEnable;
        this.isChecked = isChecked;
        this.isEffect = isEffect;
        this.redBright = red;
        this.greenBright = green;
        this.blueBright = blue;
    }

    public void setOnLedButtonListener(OnLedButtonListener listener) {
        ledButtonListener = listener;
    }

    public interface OnLedButtonListener {
        void onCheckEvent(boolean check, boolean effect);
    }

    private void doCheckEvent() {
        if (ledButtonListener != null) {
            ledButtonListener.onCheckEvent(this.isChecked, this.isEffect);
        }
    }
}
