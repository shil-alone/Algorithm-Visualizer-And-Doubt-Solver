package com.codershil.algorithmvisualizer.visualizer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

import com.codershil.algorithmvisualizer.R;


public class SortingVisualizer extends View {

    Paint mPaint, outerPaint;
    Context context;
    DisplayMetrics displayMetrics;
    Activity activity;
    private int[] randomArray;
    float screenWidth, screenHeight, startX, startY, lineGap;
    int col1 = -1, col2 = -1;
    int comp1 = -1, comp2 = -1;
    int index = -1;
    int lineColor;

    public SortingVisualizer(Context context) {
        super(context);
        this.context = context;
        activity = (Activity) context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        lineColor = context.getResources().getColor(R.color.status_bar_login);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(20);

        outerPaint = new Paint();
        outerPaint.setStyle(Paint.Style.FILL);
        outerPaint.setColor(Color.TRANSPARENT);

        displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        // getting the screenWidth and screenHeight using the displayMetrics class
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(outerPaint);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        startX = 1;
        startY = 1;
        lineGap = 1;
        screenWidth = screenWidth - (randomArray.length * lineGap);
        mPaint.setStrokeWidth(screenWidth / randomArray.length);

        for (int i = 0; i < randomArray.length; i++) {
            if (comp1 == i || comp2 == i) {
                mPaint.setColor(Color.RED);
            } else if (col1 == i || col2 == i) {
                mPaint.setColor(Color.GREEN);
            } else if (index == i) {
                mPaint.setColor(Color.YELLOW);
            } else {
                mPaint.setColor(lineColor);
            }
            canvas.drawLine(startX, startY, startX, (randomArray[i]) * (screenHeight / randomArray.length), mPaint);
            startX += (screenWidth / randomArray.length) + lineGap;
        }
        col1 = -1;
        col2 = -1;
        index = -1;
        comp1 = -1;
        comp2 = -1;
    }


    public void colSwap(int col1, int col2) {
        this.col1 = col1;
        this.col2 = col2;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void colComp(int comp1, int comp2) {
        this.comp1 = comp1;
        this.comp2 = comp2;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void colIndex(int index) {
        this.index = index;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    public void setRandomArray(int[] randomArray) {
        this.randomArray = randomArray;
    }

    public int getArrayCount() {
        return randomArray.length;
    }

}
