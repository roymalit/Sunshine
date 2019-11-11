package com.example.royma.sunshine.app;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

class WindDirectionView extends View {
    private Paint textPaint;
    private Paint compassPaint;
    private Paint compassBorder;
    private float textHeight;
    private Paint shadowPaint;
    private Path mCirle;

    public WindDirectionView(Context context) {
        this (context, null);
    }

    public WindDirectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mCirle = new Path();

        // Set up paints for canvas drawing
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (textHeight == 0) {
            textHeight = textPaint.getTextSize();
        } else {
            textPaint.setTextSize(textHeight);
        }
        //textPaint.setTextSize(40f);

        int[] colors = {
                Color.RED,
                Color.BLUE
        };
        compassPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        compassPaint.setShader(new LinearGradient(0, 0, 500, 0, colors, null, Shader.TileMode.CLAMP));
        compassPaint.setTextSize(textHeight);

        compassBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        compassBorder.setColor(Color.DKGRAY);
        compassBorder.setTextSize(textHeight);

        shadowPaint = new Paint(0);
        shadowPaint.setColor(0xff101010);
        shadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
    }

    public WindDirectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v("MyView onMeasure w", MeasureSpec.toString(widthMeasureSpec));
        Log.v("MyView onMeasure h", MeasureSpec.toString(heightMeasureSpec));

        // Add padding to maximum width/height calculation
        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

        // Reconcile size that this view wants to be with the size the parent will let it be
        final int measuredWidth = measureDimension(desiredWidth, widthMeasureSpec);
        final int measuredHeight = measureDimension(desiredHeight, heightMeasureSpec);

        // Store final measured dimensions
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //TODO: Draw rest of compass - 2 half-circles, labels in-between, gradient ring
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        int radius = y;
        mCirle.addCircle(x,y, radius, Path.Direction.CW);

        // Rotate canvas so drawing starts from top
        canvas.rotate(-90, x, y);

        //canvas.drawCircle(x, y, radius, compassBorder);

        canvas.drawCircle(x, y, radius-35, compassPaint);

        // Draw the label text
        canvas.drawTextOnPath("Draw text along path " +
                "starting from the origin and ending wherever", mCirle, 0, textPaint.getTextSize(), textPaint);
        //canvas.drawPath(mCirle, textPaint);
    }

    /**
     * Reconcile a desired size for the view contents with a {@link android.view.View.MeasureSpec}
     * constraint passed by the parent.
     *
     * @param contentSize Size of the view's contents
     * @param measureSpec A {@link android.view.View.MeasureSpec} passed by the parent
     * @return A size that best fits {@code contentSize}
     */
    private int measureDimension(int contentSize, int measureSpec){
        final int mode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);

        switch (mode){
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                if (contentSize < specSize){
                    return contentSize;
                }else {
                    return specSize;
                }
            case MeasureSpec.UNSPECIFIED:
            default:
                return contentSize;
        }
    }
}
