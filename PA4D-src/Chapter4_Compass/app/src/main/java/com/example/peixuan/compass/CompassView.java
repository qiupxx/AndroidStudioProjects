package com.example.peixuan.compass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by peixuan on 16/11/17.
 */

public class CompassView extends View {
    private float bearing;
    private Paint markerPaint;
    private Paint textPaint;
    private Paint circlePaint;
    private String northString;
    private String eastString;
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
            String bearing = String.valueOf(getBearing());
            if (bearing.length() > AccessibilityEvent.MAX_TEXT_LENGTH) {
                bearing = bearing.substring(0, AccessibilityEvent.MAX_TEXT_LENGTH);
            }
            event.getText().add(bearing);
            return true;
        }
        else
            return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int px = measuredWidth / 2;
        int py = measuredHeight / 2;
        int radius = Math.min(px, py);

        canvas.drawCircle(px, py, radius, circlePaint);

        //rorate perspective so that the top is facing current bearing
        canvas.save();
        canvas.rotate(-bearing, px, py);

        //computate text coordinate
        int textWidth = (int) textPaint.measureText("W");
        int cardinalX = px - textWidth/2;
        int cardianlY = py - radius + textHeight;

        for (int i = 0; i < 24; i++) {
            canvas.drawLine(px, py-radius, px, py-radius+10, markerPaint);

            canvas.save();
            canvas.translate(0, textHeight);

            if (i % 6 == 0) {
                String dirString = "";
                switch (i) {
                    case 0:
                        dirString = northString;
                        int arrowY = 2 * textHeight;
                        canvas.drawLine(px, arrowY, px - 5, 3*textHeight, markerPaint);
                        canvas.drawLine(px, arrowY, px + 5, 3*textHeight, markerPaint);
                        break;
                    case 6:
                        dirString = eastString;
                        break;
                    case 12:
                        dirString = southString;
                        break;
                    case 18:
                        dirString = westString;
                        break;
                    default:
                        break;
                }
                canvas.drawText(dirString, cardinalX, cardianlY, textPaint);
            } else if (i % 3 == 0) {
                String angle = String.valueOf(i*15);
                float angleTextWidth = textPaint.measureText(angle);

                int angleX = (int) (px - angleTextWidth/2);
                int angleY = py - radius + textHeight;
                canvas.drawText(angle, angleX, angleY, textPaint);
            }
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

        //must call this method
        setMeasuredDimension(d, d);
    }

    private void initCompassView() {
        //set whether this view can receive focus
        setFocusable(true);

        Resources resources = getResources();

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(resources.getColor(R.color.backgroud_color));
        //设置空心画笔
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        northString = resources.getString(R.string.cardinal_north);
        eastString = resources.getString(R.string.cardinal_east);
        southString = resources.getString(R.string.cardinal_south);
        westString = resources.getString(R.string.cardinal_west);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(resources.getColor(R.color.text_color));

        textHeight = (int) textPaint.measureText("yY");

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(resources.getColor(R.color.marker_color));
    }

    private int measure(int measureSpec) {
        int result = 0;

        int specCode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specCode == MeasureSpec.UNSPECIFIED) {
            result = 200;
        } else {
            result = specSize;
        }
        return result;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
    }

    public float getBearing() {
        return this.bearing;
    }
}
