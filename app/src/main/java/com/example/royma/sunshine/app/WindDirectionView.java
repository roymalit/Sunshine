package com.example.royma.sunshine.app;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import java.util.Arrays;
import java.util.List;

class WindDirectionView extends View {
    private final String LOG_TAG = WindDirectionView.class.getSimpleName();

    private Paint textPaint;
    private Paint compassPaintTop;
    private Paint compassPaintBottom;
    private Paint compassBorder;
    private float textHeight;
    private Paint shadowPaint;
    private Path mCircle;
    private RectF mRectF;
    private List<String> directionText;

    public WindDirectionView(Context context) {
        this (context, null);
    }

    public WindDirectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mCircle = new Path();
        mRectF = new RectF();

        // Compass directions
        String[] directions = {"N", "NNE", "NE", "ENE","E","ESE","SE","SSE",
                "S","SSW","SW","WSW","W","WNW","NW","NNW"};
        directionText = Arrays.asList(directions);

        // Set up paints for canvas drawing
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (textHeight == 0) {
            textPaint.setTextSize(35f);
        } else {
            textHeight = textPaint.getTextSize();
        }

        // Top half of compass
        compassPaintTop = new Paint(Paint.ANTI_ALIAS_FLAG);
        compassPaintTop.setColor(Color.rgb(32, 89, 232));
        compassPaintTop.setTextSize(textHeight);

        // Bottom half of compass
        compassPaintBottom = new Paint(Paint.ANTI_ALIAS_FLAG);
        compassPaintBottom.setColor(Color.rgb(232, 112, 32));
        compassPaintBottom.setTextSize(textHeight);

        // Compass border
        compassBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        compassBorder.setStyle(Paint.Style.STROKE);
        compassBorder.setStrokeWidth(textPaint.getTextSize() + 10);
        compassBorder.setColor(Color.DKGRAY);
        compassBorder.setTextSize(textHeight);

        shadowPaint = new Paint(0);
        shadowPaint.setColor(0xff101010);
        shadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        // Detect value change for content description
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }
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
        //TODO: Draw rest of compass - direction labels, centre lines (implement after app release)
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float radius = y;
        mCircle.addCircle(x,y, radius, Path.Direction.CW);
        float textPadding = textPaint.getTextSize();
        double circlePerimeter = Math.PI * radius / 8;  // 2*PI*r / 16 pieces

        // Rotate canvas so drawing starts from top
        canvas.rotate(-90, x, y);

        canvas.drawCircle(x, y, radius-compassBorder.getStrokeWidth(), compassPaintTop);

        // Rectangle to draw semi-circle within
        mRectF.set(x-radius, y-radius, x+radius,y+radius);
        // Draw bottom semi-circle
        canvas.drawArc(mRectF, 90, 180, true, compassPaintBottom);

        // Draw compass border. Halved compass border = half of text height taken into account
        canvas.drawCircle(x, y, radius-compassBorder.getStrokeWidth()/2, compassBorder);

        double currentAngle = 0.0;
        // Draw the label text
        for (int i = 0; i < directionText.size(); i++){

            canvas.drawTextOnPath(directionText.get(i), mCircle, (float) currentAngle, textPadding, textPaint);

            Log.d(LOG_TAG, "currentAngle(" + i + "): " + currentAngle);
            currentAngle+=circlePerimeter;
        }

        // Draw compass lines
        // canvas.drawPath();
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

    // TODO: Implement once windSpeedDir has been created
    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        // event.getText().add(windSpeedDir);
        return true;
    }
}
