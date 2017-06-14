package com.example.u4815128.androidspreadsheet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Class created to complete the challenge question of Week 5 lab.
 * It does not work because the question was stupid and of course there is no concept of
 * "hovering" in Android, which can only tell when your finger has touched the screen
 */

public class HoverTool extends View implements View.OnTouchListener {

    float xt = 400.0f;
    float yt = 600.0f;
    int counter = 0;
    String text = "Not yet hovered";

    public HoverTool(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint p = new Paint();
        p.setTextSize(20.0f);
        canvas.drawText(text, xt + 50, yt + 50, p);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        xt = event.getX();
        yt = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            counter += 1;
        text = "click " + counter + " !";
        this.invalidate();
        return true;
    }


}
