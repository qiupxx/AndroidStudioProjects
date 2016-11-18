package com.example.peixuan.compass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by peixuan on 16/5/28.
 */
public class CompassView extends View {
    private float bearing;
    private Paint markerPaint;
    private Paint textPain;
    private Paint circlePaint;
    private String northString;
    private String estString;
    private String southString;
    private String westString;
    private int textHeight;

    public CompassView(Context context) {
        super(context);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCompassView();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCompassView();
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.dispatchPopulateAccessibilityEvent(event);
        if (isShown()) {
            String bearingStr = String.valueOf(bearing);
            event.getText().add(bearingStr);
            return true;
        }
        else
            return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //return the width setted by onMeasure()
        int mMeasuredWidth = getMeasuredWidth();
        int mMeasuredHeight = getMeasuredWidth();

        int px =  mMeasuredWidth / 2;
        int py = mMeasuredHeight / 2;

        int radius = Math.min(px, py);

        //draw the outer boundary
        canvas.drawCircle(px, py, radius, circlePaint);

        canvas.save();
        canvas.rotate(-bearing, px, py);

        int textWidth = (int) textPain.measureText("W");
        int cardinalX = px - textWidth / 2;
        int cardianlY = py - radius + textHeight;

        //draw the marker every 15 degrees
        for (int i = 0; i < 24; i++) {
            canvas.drawLine(px, py-radius, px, py-radius + 10, markerPaint);

            //save canvas state before modifying it
            canvas.save();

            canvas.translate(0, textHeight);

            if ( i % 6 == 0) {
                String dirString = " ";
                switch (i) {
                    case (0): {
                        dirString = northString;
                        int arrowY = 2 * textHeight;
                        canvas.drawLine(px, arrowY, px-5, 3*textHeight, markerPaint);
                        canvas.drawLine(px, arrowY, px+5, 3*textHeight, markerPaint);
                        break;
                    }
                    case (6) : {
                        dirString = estString; break;
                    }
                    case (12) : {
                        dirString = southString; break;
                    }
                    case (18) : {
                        dirString = westString; break;
                    }
                }
                canvas.drawText(dirString, cardinalX, cardianlY, textPain);
            } else if ( i % 3 == 0) {
                String angle = String.valueOf(i*15);
                float angleTextWidth = textPain.measureText(angle);

                int angleTextX = (int) (px - angleTextWidth/2);
                int angleTextY = py - radius + textHeight;
                canvas.drawText(angle, angleTextX, angleTextY, textPain);
            }
            //restore canvas state paired with canvas.save()
            canvas.restore();

            canvas.rotate(15, px, py);
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measure(widthMeasureSpec);
        int measureHeight = measure(heightMeasureSpec);

        int d = Math.min(measureWidth, measureHeight);

        //must be called
        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }

        return result;
    }

    private void initCompassView() {
        setFocusable(true);

        Resources r = this.getResources();

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(r.getColor(R.color.background_color));
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        northString = r.getString(R.string.cardinal_north);
        estString = r.getString(R.string.cardinal_east);
        southString = r.getString(R.string.cardinal_south);
        westString = r.getString(R.string.cardinal_west);

        textPain = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPain.setColor(r.getColor(R.color.text_color));

        textHeight = (int) textPain.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(r.getColor(R.color.marker_color));
    }

    public void setBearing(float _bearing) {
        bearing = _bearing;
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
    }

    public float getBearing() {
        return bearing;
    }
}
