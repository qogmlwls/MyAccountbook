package com.example.myaccountbook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myaccountbook.data.PieChartDataProvider;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.List;

public class CustomMarkerView extends MarkerView {
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */


    TextView 카테고리명, 가격;
    LinearLayout layout;

    List<PieChartDataProvider.Data> list;

    public CustomMarkerView(Context context, int layoutResource,List<PieChartDataProvider.Data> list) {
        super(context, layoutResource);


        layout = findViewById(R.id.linearLayout9);
        카테고리명 = findViewById(R.id.textView100);
        가격 = findViewById(R.id.textView101);


        this.list = list;

//        카테고리명.draw
    }
    // 내부 클래스
    class CustomView extends View { // View 클래스 상속

        public CustomView(Context context) {
            super(context);
        }

        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);


        }
    }
    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        super.draw(canvas, posX, posY);
        getOffsetForDrawingAtPoint(posX, posY);
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        Log.i("getX",Float.toString(highlight.getX()));
        Log.i("getY",Float.toString(highlight.getY()));
        Log.i("getXPx",Float.toString(highlight.getXPx()));
        Log.i("getYPx",Float.toString(highlight.getYPx()));
        Log.i("getStackIndex",Integer.toString(highlight.getStackIndex()));
        Log.i("getDataIndex",Integer.toString(highlight.getDataIndex()));
        Log.i("getDataSetIndex",Integer.toString(highlight.getDataSetIndex()));

//        int index = (int)e.getX();

        PieChartDataProvider.Data data = list.get((int)highlight.getX());

        카테고리명.setText(data.카테고리명);
        가격.setText(data.금액.getAmount());
        Log.i("data.카테고리명",data.카테고리명);
        Log.i("data.카테고리명",카테고리명.getText().toString());
        Log.i("data.금액.getAmount()",data.금액.getAmount());
        Log.i("data.금액.getAmount()",가격.getText().toString());

//
////        e.
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(5, PieChartDataProvider.PieColor[(int)highlight.getX()]);  // 테두리 두께 및 색상 설정
//        drawable.setColor(Color.parseColor("75FFFFFF"));  // 배경 색상 설정
        drawable.setColor(Color.WHITE);

        layout.setBackground(drawable);

        super.refreshContent(e, highlight);

//        setChartView(chart);
//        e.
//        highli

    }

}
