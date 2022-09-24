package com.example.finalproject.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;

/**
 * Customized Horizontal Progress Bar with Animation
 * @author Mengru.Ji
 */

public class MyProgressBar extends View {

    /**
     * Paint for Background
     */
    private Paint bgPaint;
    /**
     * Paint for Progress
     */
    private Paint progressPaint;

    /**
     * Paint for Tip
     */
    private Paint tipPaint;
    /**
     * Paint for Text
     */
    private Paint textPaint;

    /**
     * width
     */
    private int mWidth;
    /**
     * height
     */
    private int mHeight;
    /**
     * Height of view
     */
    private int mViewHeight;
    /**
     * progress
     */
    private float mProgress;

    /**
     * current Progress
     */
    private float currentProgress;

    /**
     * Animation of ProgressBar
     */
    private ValueAnimator progressAnimator;

    /**
     * Duration of Animation
     */
    private int duration = 1500;
    /**
     * Delay Time for Animation Start
     */
    private int startDelay = 500;

    /**
     * Width of Paint for ProgressBar
     */
    private int progressPaintWidth;

    /**
     * Width of TipWidth
     */
    private int tipPaintWidth;

    /**
     * Height of Tip
     */
    private int tipHeight;

    /**
     * Width of Tip
     */
    private int tipWidth;

    /**
     * Path of drawing Triangle
     */
    private Path path = new Path();
    /**
     * Height of Triangle
     */
    private int triangleHeight;
    /**
     * ProgressBar MarginTop
     */
    private int progressMarginTop;

    /**
     * Distance of Move
     */
    private float moveDis;

    private Rect textRect = new Rect();
    private String textString = "0";
    /**
     * Text Size of Percent Number
     */
    private int textPaintSize;

    /**
     * Background Color of ProgressBar
     */
    private int bgColor = 0xFFe1e5e8;
    /**
     * Color of ProgressBar
     */
    private int progressColor = 0xFFf66b12;

    /**
     * RectF for Tip
     */
    private RectF rectF = new RectF();

    /**
     * Radius of RoundRect
     */
    private int roundRectRadius;

    /**
     * Listener
     */
    private ProgressListener progressListener;

    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initPaint();
    }

    /**
     * init the width of paint and the size of view
     */
    private void init() {
        progressPaintWidth = dp2px(6);
        tipHeight = dp2px(25);
        tipWidth = dp2px(50);
        tipPaintWidth = dp2px(1);
        triangleHeight = dp2px(3);
        roundRectRadius = dp2px(2);
        textPaintSize = sp2px(15);
        progressMarginTop = dp2px(8);

        //real height of view
        mViewHeight = tipHeight + tipPaintWidth + triangleHeight + progressPaintWidth + progressMarginTop;
    }

    /**
     * init Paint
     */
    private void initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.STROKE);
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.STROKE);
        tipPaint = getPaint(tipPaintWidth, progressColor, Paint.Style.FILL);

        initTextPaint();
    }

    /**
     * init Text Paint
     */
    private void initTextPaint() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textPaintSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    /**
     * getPaint
     *
     * @param strokeWidth
     * @param color
     * @param style
     * @return
     */
    private Paint getPaint(int strokeWidth, int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(style);
        return paint;
    }

    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMode, width), measureHeight(heightMode, height));
    }

    /**
     * Measure Width
     *
     * @param mode
     * @param width
     * @return
     */
    private int measureWidth(int mode, int width) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
        }
        return mWidth;
    }

    /**
     * Measure Height
     *
     * @param mode
     * @param height
     * @return
     */
    private int measureHeight(int mode, int height) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                mHeight = mViewHeight;
                break;
            case MeasureSpec.EXACTLY:
                mHeight = height;
                break;
        }
        return mHeight;
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw Background
        canvas.drawLine(getPaddingLeft(),
                tipHeight + progressMarginTop,
                getWidth(),
                tipHeight + progressMarginTop,
                bgPaint);

        //draw current Progress
        canvas.drawLine(getPaddingLeft(),
                tipHeight + progressMarginTop,
                currentProgress,
                tipHeight + progressMarginTop,
                progressPaint);

        drawTipView(canvas);
        drawText(canvas, textString);

    }

    /**
     * Draw Tip View that above the Progressbar to show points
     *
     * @param canvas
     */
    private void drawTipView(Canvas canvas) {
        drawRoundRect(canvas);
        drawTriangle(canvas);
    }


    /**
     * Draw RoundRect
     *
     * @param canvas
     */
    private void drawRoundRect(Canvas canvas) {
        rectF.set(moveDis, 0, tipWidth + moveDis, tipHeight);
        canvas.drawRoundRect(rectF, roundRectRadius, roundRectRadius, tipPaint);
    }

    /**
     * Draw Triangle
     *
     * @param canvas
     */
    private void drawTriangle(Canvas canvas) {
        path.moveTo(tipWidth / 2 - triangleHeight + moveDis, tipHeight);
        path.lineTo(tipWidth / 2 + moveDis, tipHeight + triangleHeight);
        path.lineTo(tipWidth / 2 + triangleHeight + moveDis, tipHeight);
        canvas.drawPath(path, tipPaint);
        path.reset();

    }

    /**
     * Draw Text
     *
     * @param canvas
     */
    private void drawText(Canvas canvas, String textString) {
        textRect.left = (int) moveDis;
        textRect.top = 0;
        textRect.right = (int) (tipWidth + moveDis);
        textRect.bottom = tipHeight;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //文字绘制到整个布局的中心位置
        canvas.drawText(textString , textRect.centerX(), baseline, textPaint);
    }

    /**
     * Animation of progress movement
     * Change the distance moved by interpolation
     */
    private void initAnimation() {

        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        Log.i("QQQ","mProgress"+mProgress);
        progressAnimator.setDuration(duration);
        progressAnimator.setStartDelay(startDelay);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float value = (float) valueAnimator.getAnimatedValue();
                //Only show Integer
                textString = formatNum(format2Int(value));
                //Convert the current percentage progress to the proportion corresponding to the width of the view
                currentProgress = value * mWidth / 300;
                //
                if (progressListener != null) {
                    progressListener.currentProgressListener(value);
                }
                // Move the percentage tip box, only when the current progress reaches the position in the middle of the tip box, start moving,
                // stop moving when the progress box moves to the far right, but the progress bar can continue to move
                // moveDis is the distance the tip box moves
                if (currentProgress >= (tipWidth / 2) &&
                        currentProgress <= (mWidth - tipWidth / 2)) {
                    moveDis = currentProgress - tipWidth / 2;
                }
                invalidate();
                setCurrentProgress(value);
            }
        });
        progressAnimator.start();
    }


    /**
     * set Animation for ProgressBar
     *
     * @param progress
     * @return
     */
    public MyProgressBar setProgressWithAnimation(float progress) {

        mProgress = progress;
        Log.i("QQQ","progress:"+progress);
        initAnimation();
        return this;
    }

    /**
     * set current Progress
     *
     * @param progress
     * @return
     */
    public MyProgressBar setCurrentProgress(float progress) {

        mProgress = progress;
        currentProgress = progress * mWidth / 300;

        textString = Float.toString(progress);

        // Move the percentage prompt box, only when the current progress reaches the position in the middle of the prompt box, start moving,
        // stop moving when the progress box moves to the far right, but the progress bar can continue to move
        // moveDis is the distance the tip box moves
        if (currentProgress >= (tipWidth / 2) &&
                currentProgress <= (mWidth - tipWidth / 2)) {
            moveDis = currentProgress - tipWidth / 2;
        }

        invalidate();
        return this;
    }

    /**
     * Start Animation
     */
    public void startProgressAnimation() {
        if (progressAnimator != null &&
                !progressAnimator.isRunning() &&
                !progressAnimator.isStarted())
            progressAnimator.start();
    }

    /**
     * Pause Animation
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseProgressAnimation() {
        if (progressAnimator != null) {
            progressAnimator.pause();
        }
    }

    /**
     * Resume Animation
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resumeProgressAnimation() {
        if (progressAnimator != null)
            progressAnimator.resume();
    }

    /**
     * Stop Animation
     */
    public void stopProgressAnimation() {
        if (progressAnimator != null) {
            progressAnimator.end();
        }
    }

    /**
     * Listener
     */
    public interface ProgressListener {
        void currentProgressListener(float currentProgress);
    }

    /**
     * set Listener
     *
     * @param listener
     * @return
     */
    public MyProgressBar setProgressListener(ProgressListener listener) {
        progressListener = listener;
        return this;
    }

    /**
     * Format numbers (retain two decimal places)
     *
     * @param money
     * @return
     */
    public static String formatNumTwo(double money) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(money);
    }

    /**
     * Format numbers (retain one decimal places)
     *
     * @param money
     * @return
     */
    public static String formatNum(int money) {
        DecimalFormat format = new DecimalFormat("0");
        return format.format(money);
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

    public static int format2Int(double i) {
        return (int) i;
    }
}
