package com.example.peixuan.todolist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by peixuan on 16/11/14.
 */

public class ToDoListItemView extends TextView {
    private Paint marginPaint;
    private Paint linePaint;
    private int paperColor;
    private float margin;

    public ToDoListItemView(Context context) {
        super(context);
        init();
    }

    public ToDoListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToDoListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(paperColor);

        canvas.drawLine(0, 0, 0, getMeasuredHeight(), linePaint);
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), linePaint);

        //draw margin
        canvas.drawLine(margin, 0, margin, getMeasuredHeight(), marginPaint);

        canvas.save();
        //preconcat matrix
        canvas.translate(margin, 0);
        //use TextView to render the text
        super.onDraw(canvas);
        canvas.restore();
        //super.onDraw(canvas);
    }

    private void init() {
        Resources resources =  getResources();

        marginPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        marginPaint.setColor(resources.getColor(R.color.notepad_margin));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(resources.getColor(R.color.notepad_line));

        paperColor = resources.getColor(R.color.notepad_paper);
        margin = resources.getDimension(R.dimen.notepad_margin);
    }

}
