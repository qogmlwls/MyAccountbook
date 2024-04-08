package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.myaccountbook.adapter.BudgetGraphInnerAdapter;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.PieChartDataProvider;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetailPieChartActivity extends AppCompatActivity {

    // 시작날짜의 월총액 반환.

    static final String TAG = "DetailPieChartActivity";

    RecyclerView recyclerView;
    BudgetGraphInnerAdapter adapter;

    List<accountBook> list;

    private GestureDetector gestureDetector;
    int 주기,type,선색상,카테고리식별자;
    
    String 카테고리명;
    

    Date 기준이되는날짜;

//    List<accountBook> detailData;


    LineDataProvider lineDataProvider;
    List<LineDataProvider.DATA> dataList;
    MyApplication application;

    LineChart chart;
    PieChartDataProvider pieChartDataProvider;


    TextView 합계;

    Date 시작날짜, 끝날짜;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pie_chart);

//        intent.putExtra("주기",주기);
//        intent.putExtra("type",type);
//        intent.putExtra("카테고리명",data.카테고리명);
//        intent.putExtra("카테고리식별자",data.카테고리식별자);
//
//        int index = position% PieChartDataProvider.PieColor.length;
//        intent.putExtra("선색상",PieChartDataProvider.PieColor[index]);
//        intent.putExtra("최초시작날짜",최초시작날짜);


        기준이되는날짜 = (Date)getIntent().getSerializableExtra("최초시작날짜");
        선색상 = getIntent().getIntExtra("선색상",-1);
        카테고리식별자 = getIntent().getIntExtra("카테고리식별자",-1);
        주기 = getIntent().getIntExtra("주기",-1);
        type = getIntent().getIntExtra("type",-1);
        카테고리명 = getIntent().getStringExtra("카테고리명");

        시작날짜 = (Date)getIntent().getSerializableExtra("시작날짜");
        끝날짜 = (Date)getIntent().getSerializableExtra("끝날짜");

        Toolbar toolbar;
        toolbar = findViewById(R.id.toolbar12);

        toolbar.setTitle(카테고리명);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        application = (MyApplication)getApplication();

        pieChartDataProvider = new PieChartDataProvider(application.readDb);


        lineDataProvider = new LineDataProvider();
  
        
        TextView 메세지;
        메세지 = findViewById(R.id.textView106);
        
        if(주기 == PeriodManager.주간){
            메세지.setText("주간 총액");
        }
        else if(주기 == PeriodManager.월간){
            메세지.setText("월총액");
        }
        else if(주기 == PeriodManager.연간){
            메세지.setText("연지출");
        }
        else if(주기 == PeriodManager.기간){
            메세지.setText("합계");
        }

        Button 날짜버튼;
        날짜버튼 = findViewById(R.id.button28);

        Button 시작날짜버튼, 끝날짜버튼;
        시작날짜버튼 = findViewById(R.id.button29);
        끝날짜버튼 = findViewById(R.id.button30);
        TextView 물결;
        물결 = findViewById(R.id.textView111);



        if (주기 != PeriodManager.기간) {

            날짜버튼.setText(Date.날짜글자(기준이되는날짜, 주기));
            날짜버튼.setVisibility(View.VISIBLE);

            if (주기 == PeriodManager.주간) {
                기준이되는날짜 = Date.기간시작날짜(기준이되는날짜, PeriodManager.주간);
            }

//            totalData = chartDataProvider.getChartData(기준이되는날짜, 기간, type, application);

        }
        else {

            시작날짜버튼.setVisibility(View.VISIBLE);
            끝날짜버튼.setVisibility(View.VISIBLE);
            물결.setVisibility(View.VISIBLE);
            날짜버튼.setVisibility(View.GONE);


            시작날짜버튼.setText(Integer.toString(시작날짜.getYear() % 100) + "." + Integer.toString(시작날짜.getMonth()) + "." + Integer.toString(시작날짜.getDay()));
            끝날짜버튼.setText(Integer.toString(끝날짜.getYear() % 100) + "." + Integer.toString(끝날짜.getMonth()) + "." + Integer.toString(끝날짜.getDay()));


        }

        if(주기 == PeriodManager.기간){
            list = pieChartDataProvider.getDetailData(시작날짜,끝날짜,type,카테고리식별자, application);

        }
        else{
            list = pieChartDataProvider.getDetailData(기준이되는날짜,주기,type,카테고리식별자, application);

        }

        합계 = findViewById(R.id.textView107);


        gestureDetector = new GestureDetector(DetailPieChartActivity.this, new DetailPieChartActivity.MyGestureListener());


        //        detailData.총합계.getAmount()
//        list = detailData.list;

        if(list.size() == 0){
            TextView textView = findViewById(R.id.textView108);
            textView.setVisibility(View.VISIBLE);
        }
        recyclerView = findViewById(R.id.recyclerView10);
        adapter = new BudgetGraphInnerAdapter(DetailPieChartActivity.this, list,gestureDetector);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        chart = findViewById(R.id.piechart1);
        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        dataList = lineDataProvider.getData(기준이되는날짜, 주기,카테고리식별자,type, application.readDb);
        LineData data = getData(dataList,선색상);
        // add some transparency to the color with "& 0x90FFFFFF"
        setupChart(chart, data, 선색상);
        합계.setText(dataList.get(dataList.size()-1).금액.getAmount());



        시작날짜버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = 시작날짜.getYear();
                int month = 시작날짜.getMonth() - 1;
                int day = 시작날짜.getDay();

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int m_year, int m_month, int dayOfMonth) {


                        시작날짜.setYear(m_year);
                        시작날짜.setMonth(m_month + 1);
                        시작날짜.setDay(dayOfMonth);

                        Log.i(TAG, "year : " + Integer.toString(m_year));
                        Log.i(TAG, "month : " + Integer.toString(m_month));
                        Log.i(TAG, "day : " + Integer.toString(dayOfMonth));


                        if (Date.after(시작날짜, 끝날짜)) {
                            날짜변경(시작날짜, 시작날짜);

                        } else {
                            날짜변경(시작날짜, 끝날짜);

                        }


                    }
                }, year, month, day);

                dialog.show();
   

            }
        });


        끝날짜버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = 끝날짜.getYear();
                int month = 끝날짜.getMonth() - 1;
                int day = 끝날짜.getDay();

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int m_year, int m_month, int dayOfMonth) {


                        끝날짜.setYear(m_year);
                        끝날짜.setMonth(m_month + 1);
                        끝날짜.setDay(dayOfMonth);

                        Log.i(TAG, "year : " + Integer.toString(m_year));
                        Log.i(TAG, "month : " + Integer.toString(m_month));
                        Log.i(TAG, "day : " + Integer.toString(dayOfMonth));


                        if (Date.after(시작날짜, 끝날짜)) {
                            날짜변경(끝날짜, 끝날짜);

                        } else {
                            날짜변경(시작날짜, 끝날짜);

                        }


                    }
                }, year, month, day);

                dialog.show();
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    List<String> stringList;

    private LineData getData(List<LineDataProvider.DATA> dataList,int color) {

        ArrayList<Entry> values = new ArrayList<>();

        stringList = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            LineDataProvider.DATA data = dataList.get(i);
            stringList.add(data.날짜);
            values.add(new Entry(i, data.금액.amount));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(color);
        set1.setCircleColor(color);
        set1.setHighLightColor(Color.TRANSPARENT);
        set1.setDrawValues(false);

        // create a data object with the data sets
        return new LineData(set1);
    }


    final int REQUEST_EDIT = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK && data != null) {

//            accountBook 가계부 = (accountBook) getIntent().getSerializableExtra("가계부");
////                            resultIntent.putExtra("가계부",가계부);
////            resultIntent.putExtra("position",position);
////            resultIntent.putExtra("상태","삭제");
//
//            int position  = getIntent().getIntExtra("position",-1);
//            String 상태 = getIntent().getStringExtra("상태");
//
//            if(상태 != null){
//
//                adapter.setData(position,상태, 가계부);
//
//            }
//
//            list = detailData.list;

//            .list.clear();
            list.clear();

            if(주기 == PeriodManager.기간){
                list= pieChartDataProvider.getDetailData(시작날짜,끝날짜,type,카테고리식별자, application);

            }
            else{
                list= pieChartDataProvider.getDetailData(기준이되는날짜,주기,type,카테고리식별자, application);

            }




//            list = detailData.list;
            adapter.setData(list);


            dataList.clear();
            dataList = lineDataProvider.getData(기준이되는날짜, 주기,카테고리식별자,type, application.readDb);
            합계.setText(dataList.get(dataList.size()-1).금액.getAmount());



            LineData lineData = getData(dataList,선색상);
            // add some transparency to the color with "& 0x90FFFFFF"
            setupChart(chart, lineData, 선색상);

            chart.invalidate();


//            resultIntent.putExtra("position",position);
//            resultIntent.putExtra("상태","삭제");
//            adapter.notifyDataSetChanged();

        }

    }

    private void setupChart(LineChart chart, LineData data, int color) {
//        chart.setHighlightPerTapEnabled(false);


        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(color);
//        chart.setGridBackgroundColor(color);
//        chart.setNoDataTextColor(color);
//        chart.setOutlineSpotShadowColor(color);
//        chart.setGridBackgroundColor(color);
//

//        ((LineDataSet) data.getDataSetByIndex(0)).color
        // no description text
        chart.getDescription().setEnabled(false);

        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

//        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
//        chart.setViewPortOffsets(100, 100, 100, 100);



        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);


//        YAxis yAxis;
//        {   // // Y-Axis Style // //
//            yAxis = chart.getAxisLeft();
//
//            // disable dual axis (only use LEFT axis)
//            chart.getAxisRight().setEnabled(false);
//
//            // horizontal grid lines
//            yAxis.enableGridDashedLine(10f, 10f, 0f);
//
//            // axis range
//            yAxis.setAxisMaximum(200f);
//            yAxis.setAxisMinimum(-50f);
//        }


//        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(20);
        chart.getAxisLeft().setLabelCount(3);
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                return Integer.toString((int)value);
            }
        });

        chart.getAxisLeft().setSpaceBottom(0);
        chart.getAxisRight().setEnabled(false);

//        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

//        chart.getXAxis().setAxisLineWidth(30f);

//        chart.getXAxis().






//        chart.getXAxis().setAxisMinimum(0f);
//        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setLabelCount(stringList.size());
//        chart.getXAxis().setCenterAxisLabels(true);
        chart.getXAxis().setGranularity(1f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
//        xAxis.setAxisMinimum(-1);
//        xAxis.setAxisMaximum(5);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
//
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Date로 가져와서, 날짜 가져오기.

                return stringList.get((int)value);
            }
        });
//
//



        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // Set the marker to the chart
        mv.setChartView(chart);
        chart.setMarker(mv);


//        chart.
//        chart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Toast.makeText(MainActivity7.this, "click", Toast.LENGTH_SHORT).show();
////                ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(Color.WHITE);
//
//            }
//        });


        // add data
        chart.setData(data);
        chart.invalidate();

        // animate calls invalidate()...
//        chart.animateX(2500);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("ActivityTouchEvent", "onTouchEvent: " + event.getAction());

        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Toast.makeText(this, Integer.toString(item.getItemId()), Toast.LENGTH_SHORT).show();

        // 뒤로가기 버튼을 눌렀을 때의 동작
        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }



    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {


        boolean result = true;
        String TAG = "MyGestureListener";

//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            // 사용자가 화면에서 손을 뗄 때 호출됨
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            // 빠른 속도로 스크롤을 한 후 손을 뗄 때 호출됨
////            Log.i("MyGestureListener", "onFling distanceX : " + Float.toString((e2.getX()-e1.getX())));
//            Log.i("MyGestureListener", "onFling velocityX : " + velocityX);
//
//            Log.i("MyGestureListener", "onFling velocityY : " + velocityY);
////            양수는 오른쪽 방향으로의 이동 속도를, 음수는 왼쪽 방향으로의 이동 속도를 나타냅니다
//
//            if(velocityX>0){
//                // 오른쪽
//
//            }
//
//            return true;
//        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


//            Log.i("MyGestureListener", "onScroll distanceX : " + Float.toString((e2.getX()-e1.getX())));
            Log.i("MyGestureListener", "onScroll distanceX : " + distanceX);

            Log.i("MyGestureListener", "onScroll distanceY : " + distanceY);

            float x = e2.getX()-e1.getX();
            float y = e2.getY()-e1.getY();


            // 절댓값을 비교하여 좌우 스크롤 여부 확인
            if (Math.abs(x) > Math.abs(y)) {

                if (result) {

                    result = false;
                    if (주기 != PeriodManager.기간) {
                        // distanceX 값이 양수면 오른쪽으로, 음수면 왼쪽으로 스크롤
                        if (distanceX > 0) {
                            Date 변경날짜 = null;
                            if (주기 == PeriodManager.주간) {

                                java.util.Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(java.util.Calendar.WEEK_OF_YEAR, 1);
                                변경날짜 = Date.getDate(calendar);
                            } else if (주기 == PeriodManager.월간) {
                                java.util.Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(java.util.Calendar.MONTH, 1);
                                변경날짜 = Date.getDate(calendar);
                            } else if (주기 == PeriodManager.연간) {
                                java.util.Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(java.util.Calendar.YEAR, 1);
                                변경날짜 = Date.getDate(calendar);
                            }

                            날짜변경(변경날짜);

                        } else if (distanceX < 0) {
                            Log.i(TAG, "왼쪽으로");
                            Date 변경날짜 = null;
                            if (주기 == PeriodManager.주간) {

                                java.util.Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(java.util.Calendar.WEEK_OF_YEAR, -1);
                                변경날짜 = Date.getDate(calendar);
                            } else if (주기 == PeriodManager.월간) {
                                java.util.Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(java.util.Calendar.MONTH, -1);
                                변경날짜 = Date.getDate(calendar);
                            } else if (주기 == PeriodManager.연간) {
                                java.util.Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(Calendar.YEAR, -1);
                                변경날짜 = Date.getDate(calendar);
                            }

                            날짜변경(변경날짜);

                        }

                    }
                }
            }


            return false;

        }

    }
    void 날짜변경(Date date) {

        전체화면변경(date);
        overridePendingTransition(0, 0);

    }

    void 날짜변경(Date date1, Date date2) {

        Intent intent = new Intent(DetailPieChartActivity.this, DetailPieChartActivity.class);

        intent.putExtra("주기",주기);
        intent.putExtra("type",type);
        intent.putExtra("카테고리명",카테고리명);
        intent.putExtra("카테고리식별자",카테고리식별자);

        intent.putExtra("선색상",선색상);
        intent.putExtra("최초시작날짜",기준이되는날짜);

        intent.putExtra("시작날짜",date1);
        intent.putExtra("끝날짜",date2);

        startActivity(intent);
        finish();

        overridePendingTransition(0, 0);

    }

    void 전체화면변경( Date date) {

        Intent intent = new Intent(DetailPieChartActivity.this, DetailPieChartActivity.class);

        intent.putExtra("주기",주기);
        intent.putExtra("type",type);
        intent.putExtra("카테고리명",카테고리명);
        intent.putExtra("카테고리식별자",카테고리식별자);

        intent.putExtra("선색상",선색상);
        intent.putExtra("최초시작날짜",date);
        startActivity(intent);
        finish();

    }

}