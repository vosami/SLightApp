package com.syncworks.slightapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.syncworks.slightapp.R;
/**
 * TODO: document your custom view class.
 */
public class LedButton extends View {
    private String onText;
    private String offText;
    private boolean isChecked;
    private boolean isEffect;
    private int bright;

    private Paint shadowPaint;
    private Paint fillPaint;
    private Paint strokePaint;
    private Paint percentPaint;
    private Paint percentStrokePaint;
    private TextPaint mTextPaint;

    private RectF rect;
    private RectF shadowRect;
    private RectF innerRect;


    public LedButton(Context context) {
        super(context);
        init(null, 0);
    }

    public LedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        if (attrs != null) {
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.LedButton, defStyle, 0);

            onText = a.getString(R.styleable.LedButton_on_text);
            offText = a.getString(R.styleable.LedButton_off_text);
            isChecked = a.getBoolean(R.styleable.LedButton_check, false);
            isEffect = a.getBoolean(R.styleable.LedButton_effect, false);
            bright = a.getInt(R.styleable.LedButton_bright, 95);

            a.recycle();
        } else {
            onText = "✓";
            offText = "0";
            isChecked = false;
            isEffect = false;
            bright = 95;
        }
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

        percentPaint = new Paint();
        percentPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        percentPaint.setStyle(Paint.Style.FILL);
        percentPaint.setColor(Color.rgb(247, 153, 75));

        percentStrokePaint = new Paint();
        percentStrokePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        percentStrokePaint.setStyle(Paint.Style.FILL);
        percentStrokePaint.setColor(Color.rgb(200,200,200));

        rect = new RectF(0,0,0,0);
        shadowRect = new RectF(0,0,0,0);
        innerRect = new RectF(0,0,0,0);
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
                heightSize = (int)(widthSize);
                break;
            case MeasureSpec.EXACTLY:        // fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
        float padding = (float) (widthSize * 0.05);
        float shadowPadding = (float) (widthSize * 0.03);
        rect.set(padding, padding, widthSize-padding, heightSize - padding);
        shadowRect.set(padding+shadowPadding, padding+shadowPadding, widthSize-padding+shadowPadding, heightSize - padding+shadowPadding);
        innerRect.set(padding-shadowPadding, padding-shadowPadding, widthSize - padding-shadowPadding, heightSize - padding-shadowPadding);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String mText;

        int contentWidth = getMeasuredWidth();
        int contentHeight = getMeasuredHeight();
        float radius = (float) (contentWidth * 0.2);

        float textX;

        mTextPaint.setTextSize((float) (contentHeight * 0.5));

        if (isEffect) {
            if (isEnabled()) {
                if (isChecked) {
                    textX = (float) (contentWidth * 0.5);
                    fillPaint.setShader(new LinearGradient(0, 0, 0, contentHeight, Color.rgb(83, 147, 63), Color.rgb(112, 198, 86), Shader.TileMode.MIRROR));
                    strokePaint.setColor(Color.rgb(0, 0, 0));
                    strokePaint.setStrokeWidth(5);
                    mTextPaint.setColor(Color.rgb(164, 2, 85));
                    mText = onText;
                } else {
                    textX = (float) (contentWidth * 0.5);
                    fillPaint.setColor(Color.rgb(255,255,255));
                    strokePaint.setColor(Color.rgb(150, 150, 150));
                    mTextPaint.setColor(Color.rgb(30, 30, 30));
                    shadowPaint.setColor(Color.rgb(100, 100, 100));
                    canvas.drawRoundRect(shadowRect, radius, radius, shadowPaint);
                    mText = offText;
                }
            } else {
                textX = (float) (contentWidth * 0.5);
                fillPaint.setColor(Color.rgb(195, 195, 195));
                strokePaint.setColor(Color.rgb(150, 150, 150));
                mTextPaint.setColor(Color.rgb(127, 127, 127));
                mText = offText;
            }
        } else {
            textX = (float) (contentWidth * 0.5);
            if (isEnabled()) {
                if (isChecked) {
                    fillPaint.setShader(new LinearGradient(0, 0, 0, contentHeight, Color.rgb(83, 147, 63), Color.rgb(112, 198, 86), Shader.TileMode.MIRROR));
                    strokePaint.setColor(Color.rgb(0, 0, 0));
                    strokePaint.setStrokeWidth(5);
                    mTextPaint.setColor(Color.rgb(164, 2, 85));
                    mText = onText;
                } else {
                    fillPaint.setShader(new LinearGradient(0, 0, 0, contentHeight, Color.rgb(224, 209, 178), Color.rgb(210, 178, 110), Shader.TileMode.MIRROR));
                    strokePaint.setColor(Color.rgb(150, 150, 150));
                    mTextPaint.setColor(Color.rgb(30, 30, 30));
                    shadowPaint.setColor(Color.rgb(100, 100, 100));
                    canvas.drawRoundRect(shadowRect, radius, radius, shadowPaint);
                    mText = offText;
                }

            } else {
                fillPaint.setColor(Color.rgb(195, 195, 195));
                strokePaint.setColor(Color.rgb(150, 150, 150));
                mTextPaint.setColor(Color.rgb(127, 127, 127));
                mText = offText;
            }
        }

        canvas.drawRoundRect(rect, radius, radius, fillPaint);
        if (isEffect & isEnabled() & !isChecked) {
            float percent = (float)((contentHeight - (contentWidth * 0.05)) + ((float)bright/191)*((contentWidth * 0.1)-contentHeight));//((bright/191)*((contentHeight - (contentWidth * 0.05))-(contentWidth * 0.05)));
            canvas.drawRect((float)(contentWidth*0.2),(float)(contentWidth * 0.05),(float)(contentWidth*0.8),(float)(contentHeight - (contentWidth * 0.05)),percentStrokePaint);
            canvas.drawRect((float)(contentWidth*0.23),percent,(float)(contentWidth*0.77),(float)(contentHeight - (contentWidth * 0.05)),percentPaint);
        }
        canvas.drawRoundRect(rect,radius,radius,strokePaint);
        canvas.drawText(mText, textX, (float) (contentHeight * 0.7), mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled()) {

        }
        return super.onTouchEvent(event);
    }
}
