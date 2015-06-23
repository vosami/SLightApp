package com.syncworks.slightapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.syncworks.slightapp.R;


public class StepView extends View {

    // 리스너 설정
    OnStepViewTouchListener onStepViewTouchListener = null;

    private final static int MAX_STEP = 5;
    private int curStep = 1;
    private int oldStep = -1;

    private Paint paint = null;

    public StepView(Context context) {
        super(context);
        init();
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public void setStep(int num) {
        this.curStep = num;
        this.invalidate();
    }

    public int getStep() {
        return this.curStep;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        float mRadius = (float) (mHeight*0.4);
        float middleHeight = (float) (mHeight * 0.5);
        float intervalWidth = (mWidth - mHeight) / (MAX_STEP-1);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, mWidth, mHeight, paint);
        paint.setColor(Color.rgb(210, 210, 210));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(0, 0, mWidth, 0, paint);
        canvas.drawLine(0, mHeight - 1, mWidth, mHeight - 1, paint);

        for (int i=0;i<MAX_STEP-1;i++) {
            paint.setColor(Color.rgb(230, 230, 230));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(middleHeight + intervalWidth * i, (float) (middleHeight * 0.7),
                    middleHeight + intervalWidth * (i + 1), (float) (middleHeight * 1.3), paint);
            paint.setColor(Color.rgb(80, 80, 80));
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(middleHeight + intervalWidth * i,(float)(middleHeight*0.7),
                    middleHeight + intervalWidth * (i+1),(float)(middleHeight*1.3),paint);
            if (curStep > i+1) {
                paint.setColor(Color.rgb(131, 197, 1));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(middleHeight + intervalWidth * i, (float) (middleHeight * 0.8),
                        middleHeight + intervalWidth * (i + 1), (float) (middleHeight * 1.2), paint);
                paint.setColor(Color.rgb(91,152,0));
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(middleHeight + intervalWidth * i,(float)(middleHeight*0.8),
                        middleHeight + intervalWidth * (i+1),(float)(middleHeight*1.2),paint);
            }

        }

        for (int i=0;i<MAX_STEP;i++) {
            if (curStep > i+1) {
                paint.setColor(Color.rgb(230, 230, 230));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), mRadius, paint);
                paint.setColor(Color.rgb(80, 80, 80));
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), mRadius, paint);
                paint.setColor(Color.rgb(131, 197, 1));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), (float) (mRadius * 0.8), paint);
                paint.setColor(Color.rgb(91, 152, 0));
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), (float) (mRadius * 0.8), paint);
                paint.setColor(Color.WHITE);
                paint.setTextSize((float) (mHeight*0.6));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("?",middleHeight + intervalWidth * i,(float)(mHeight*0.7),paint);

            } else if (curStep == i+1) {
                paint.setColor(Color.rgb(1, 139, 185));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), mRadius, paint);
                paint.setColor(Color.rgb(1, 120, 150));
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), mRadius, paint);
                paint.setColor(Color.WHITE);
                paint.setTextSize((float) (mHeight * 0.6));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(Integer.toString(i+1), middleHeight + intervalWidth * i, (float) (mHeight * 0.7), paint);
            } else {
                paint.setColor(Color.rgb(230, 230, 230));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), mRadius, paint);
                paint.setColor(Color.rgb(80,80,80));
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), mRadius, paint);
                paint.setColor(Color.rgb(238, 238, 238));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), (float) (mRadius * 0.8), paint);
                paint.setColor(Color.rgb(190, 190, 190));
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle((float) (middleHeight + intervalWidth * i),
                        (float) (middleHeight), (float) (mRadius * 0.8), paint);
                paint.setColor(Color.rgb(210,211,216));
                paint.setTextSize((float) (mHeight * 0.6));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(Integer.toString(i + 1), middleHeight + intervalWidth * i, (float) (mHeight * 0.7), paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        //float mWidth = getMeasuredWidth();
        //float mHeight = getMeasuredHeight();
        //float intervalWidth = (mWidth - mHeight) / (MAX_STEP-1);
        // 터치 입력 좌표값을 얻어옵니다.
        int touchX = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldStep = getTouchStep(touchX);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int curTouch = getTouchStep(touchX);
                if (curTouch >= 0 && curTouch <= MAX_STEP) {
                    if (oldStep == curTouch) {
                        doStepViewEvent(curTouch);
                        Log.d("test","click"+curTouch);
                    }
                }
                oldStep = -1;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    private int getTouchStep(int touchX) {
        int retVal = -1;
        float mWidth = getMeasuredWidth();
        float mHeight = getMeasuredHeight();
        float intervalWidth = (mWidth - mHeight) / (MAX_STEP-1);
        for(int i=0;i<MAX_STEP;i++) {
            if (touchX > (mHeight*0.1 + intervalWidth*i) && touchX < (mHeight*0.9 + intervalWidth*i)) {
                retVal = i;
            }
        }
        return retVal;
    }

    // Activity 와 통신할 수 있는 인터페이스 설정
    public interface OnStepViewTouchListener {
        void onStepViewEvent(int clickStep);
    }
    // Activity 에서 인터페이스 설정하는 함수
    public void setOnStepViewTouchListener(OnStepViewTouchListener listener) {
        this.onStepViewTouchListener = listener;
    }
    private void doStepViewEvent(int clickStep) {
        if (onStepViewTouchListener != null) {
            onStepViewTouchListener.onStepViewEvent(clickStep);
        }
    }
}
