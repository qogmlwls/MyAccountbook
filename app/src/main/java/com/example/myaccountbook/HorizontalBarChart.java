package com.example.myaccountbook;

import android.content.Context;
import android.util.AttributeSet;

public class HorizontalBarChart extends com.github.mikephil.charting.charts.BarChart {
    private boolean mCustomViewPortEnabled = false;

    public HorizontalBarChart(Context context) {
        super(context);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setViewPortOffsets(float left, float top, float right, float bottom) {
        mCustomViewPortEnabled = true;
        super.setViewPortOffsets(left, top, right, bottom);
    }

    @Override
    public void calculateOffsets() {
        if (!mCustomViewPortEnabled) {
            super.calculateOffsets();
        } else {
            prepareOffsetMatrix();
            prepareValuePxMatrix();
        }
    }
}