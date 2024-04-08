package com.example.myaccountbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Formatter;
import com.example.myaccountbook.data.자산통계데이터;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

// 전체 통계
public class FullStatisticsActivity extends CommonActivity {

    AssetDataManager assetDataManager;
    List<String> stringList;
    boolean result;
    LineChart lineChart;
    BarChart barChart;
    List<자산통계데이터> list;
    TextView 누적잔액;
    Button 날짜버튼;
    TextView 지출1, 지출2, 지출3, 지출4, 지출5, 지출6, 수입1, 수입2, 수입3, 수입4, 수입5, 수입6, 누적잔액1,누적잔액2,누적잔액3,누적잔액4,누적잔액5,누적잔액6;
    MyApplication application;
    GestureManager manager;
    ImageButton 이전날짜,다음날짜;
    Date 날짜;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_statistics);

        print("onCreate() 시작 ------------------");

        print("-------------------");
        print("XML 레이아웃 파일에 있는 View 요소들의 객체에 대한 참조 가져오기 시작");

        이전날짜 = findViewById(R.id.imageButton14);
        다음날짜 = findViewById(R.id.imageButton15);
        날짜버튼 = findViewById(R.id.button37);

        누적잔액1 = findViewById(R.id.textView170);
        누적잔액2 = findViewById(R.id.textView171);
        누적잔액3 = findViewById(R.id.textView172);
        누적잔액4 = findViewById(R.id.textView173);
        누적잔액5 = findViewById(R.id.textView174);
        누적잔액6 = findViewById(R.id.textView175);

        수입1 = findViewById(R.id.textView163);
        수입2 = findViewById(R.id.textView164);
        수입3 = findViewById(R.id.textView165);
        수입4 = findViewById(R.id.textView166);
        수입5 = findViewById(R.id.textView167);
        수입6 = findViewById(R.id.textView168);

        지출1 = findViewById(R.id.textView157);
        지출2 = findViewById(R.id.textView158);
        지출3 = findViewById(R.id.textView159);
        지출4 = findViewById(R.id.textView160);
        지출5 = findViewById(R.id.textView161);
        지출6 = findViewById(R.id.textView162);

        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.chart1);
        누적잔액 = findViewById(R.id.textView121);

        print("-------------------");

        result = true;

        날짜 = new Date();
        날짜.today();


        // 뒤로가기
        Toolbar toolbar = findViewById(R.id.toolbar14);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        application = (MyApplication)getApplication();
        assetDataManager = application.assetDataManager;
        stringList = new ArrayList<>();

        manager = new GestureManager(this);

        이전날짜.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(result){
                    result = false;

//                    com.example.myaccountbook.data.Date 날짜 = new Date();
                    날짜.이전달로이동();

//                    날짜 = Date.이전날짜(날짜);
                    com.example.myaccountbook.data.Date date = 날짜.clone(날짜);
                    date.이전달로이동(5);

//                    com.example.myaccountbook.data.Date date = com.example.myaccountbook.data.Date.이전날짜(날짜,5);
                    list.remove(list.size()-1);

                    list.add(0, assetDataManager.전체통계데이터반환(date));

                    화면변경(list);

                    result = true;

                }


            }
        });


        다음날짜.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(result){
                    result = false;

                    날짜.다음달로이동();
//                    날짜 = Date.이후날짜(날짜);

                    list.remove(0);

                    list.add( assetDataManager.전체통계데이터반환(날짜));

                    화면변경(list);

                    result = true;

                }
            }
        });

        날짜버튼.setText(날짜.년월날짜());
        날짜버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(result) {
                    result = false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(FullStatisticsActivity.this);
                    View view = LayoutInflater.from(v.getContext()).inflate(R.layout.number_picker, null, false);


                    NumberPicker year = view.findViewById(R.id.yearpicker_datepicker);
                    NumberPicker month = view.findViewById(R.id.monthpicker_datepicker);

                    //  순환 안되게 막기
                    year.setWrapSelectorWheel(false);
                    month.setWrapSelectorWheel(false);

                    //  editText 설정 해제
                    year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                    month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                    //  최소값 설정
                    year.setMinValue(2000);
                    month.setMinValue(1);

                    //  최대값 설정
                    year.setMaxValue(2100);
                    month.setMaxValue(12);

                    // 현재 값 설정
                    year.setValue(날짜.getYear());
                    month.setValue(날짜.getMonth());

                    String[] strings = new String[101];
                    for (int i = 0; i <= 100; i++) {
                        String 날짜 = Integer.toString(i + 2000) + "년";
                        strings[i] = 날짜;
                    }

                    year.setDisplayedValues(strings);


                    String[] strings2 = new String[12];
                    for (int i = 0; i < 12; i++) {
                        String 날짜 = Integer.toString(i + 1) + "월";
                        strings2[i] = 날짜;
                    }

                    month.setDisplayedValues(strings2);

                    builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            print("변경 onClick() 시작-------------------------------------");
                            if(result) {
                                result = false;
                                int yearValue = year.getValue();
                                int monthValue = month.getValue();
                                print("선택한 년",Integer.toString(yearValue));
                                print("선택한 월",Integer.toString(monthValue));
                                날짜.setYear(yearValue);
                                날짜.setMonth(monthValue);
                                날짜.setDay(1);
                                print("날짜 년",Integer.toString(날짜.getYear()));
                                print("날짜 월",Integer.toString(날짜.getMonth()));
                                print("날짜 일",Integer.toString(날짜.getDay()));
                                list.clear();
                                print("list clear");
                                list = assetDataManager.전체통계데이터목록반환(날짜);
                                print("전체통계 데이터 모두 가져오기");
                                화면변경(list);
                                result = true;
                            }
                            print("변경 onClick() 끝-------------------------------------");
                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

                    builder.setView(view);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    result = true;

                }
            }
        });


        list = assetDataManager.전체통계데이터목록반환(날짜);

//        누적잔액.setText(Formatter.천단위구분(list.get(list.size()-1).누적잔액.amount));
//

        // 데이터 1
        // 날짜 , 6개의 날짜 - 누적잔액(미삭제자산 합계)
        // x월 / 누적잔액

        // 데이터 2
        // 날짜, 자산(삭제포함) 총 수입, 자산 총 지출

//        for (int i = 0; i < list.size(); i++) {
//            전체통계 data = list.get(i);
//            stringList.add(Integer.toString(data.월)+"월");
//        }

//        for(int i=0;i<list.size();i++){
//            전체통계  전체통계 = list.get(i);
//
//            Log.i("전체통계",String.valueOf(전체통계.월));
//            Log.i("전체통계",전체통계.누적잔액.getAmount());
//            Log.i("전체통계",전체통계.총수입.getAmount());
//            Log.i("전체통계",전체통계.총지출.getAmount());
//
//        }

//        하단화면표시();

//        LineData data = getData(list,Color.RED,lineChart);
        // add some transparency to the color with "& 0x90FFFFFF"
        setupChart(lineChart);
        lineChart.getXAxis().setAxisMinimum(0);
        lineChart.getXAxis().setAxisMaximum(list.size()+0.1f);
//        BarData barData = generateBarData(list, barChart);

        setupChart(barChart);

        lineChart.setViewPortOffsets(100, 100, 100, 50);
        barChart.setViewPortOffsets(100, 50, 100, 50);

        // create marker to display box when values are selected
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        // Set the marker to the chart
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);

        // create marker to display box when values are selected
        MyMarkerView mv2 = new MyMarkerView(this, R.layout.custom_marker_view);

        // Set the marker to the chart
        mv2.setChartView(barChart);
        barChart.setMarker(mv2);

//        lineChart.invalidate();
//        barChart.invalidate();

//        화면에데이터표시(list);
        화면변경(list);


        manager.setMyInterface(new MyGestureListener.MyInterface() {

            @Override
            public void 이전으로이동() {
                if(result){
                    result = false;

                    날짜.이전달로이동();
                    com.example.myaccountbook.data.Date date = 날짜.clone(날짜);
                    date.이전달로이동(5);
                    list.remove(list.size()-1);
                    list.add(0, assetDataManager.전체통계데이터반환(date));
                    화면변경(list);

                    result = true;

                }

            }

            @Override
            public void 이후로이동() {
                if(result){
                    result = false;

                    날짜.다음달로이동();
//                    날짜 = Date.이후날짜(날짜);

                    list.remove(0);

                    list.add( assetDataManager.전체통계데이터반환(날짜));

                    화면변경(list);

                    result = true;

                }
            }
        });


        toolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                manager.getDetector().onTouchEvent(event);
                return false;
            }
        });
        lineChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                manager.getDetector().onTouchEvent(event);
                return false;
            }
        });

        barChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                manager.getDetector().onTouchEvent(event);
                return false;
            }
        });




    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return manager.getDetector().onTouchEvent(event) || super.onTouchEvent(event);
    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent me) {
//        // Call onTouchEvent of SimpleGestureFilter class
//        this.detector.onTouchEvent(me);
//        return super.dispatchTouchEvent(me);
//    }

    private void 화면변경(List<자산통계데이터> list){

        lineChart.clear();
        stringList.clear();
        barChart.clear();

        화면에데이터표시(list);

    }

    private void 화면에데이터표시(List<자산통계데이터> list){


        for (int i = 0; i < list.size(); i++) {
            자산통계데이터 data = list.get(i);
            stringList.add(Integer.toString(data.월)+"월");
        }

        LineData data = getData(list);
        lineChart.setData(data);
//        lineChart.invalidate();

        BarData barData = generateBarData(list, barChart);
        barChart.invalidate();

        누적잔액.setText(Formatter.천단위구분(list.get(list.size()-1).누적잔액.amount));
        날짜버튼.setText(날짜.년월날짜());

        하단화면표시();

    }


    private void 하단화면표시(){

        누적잔액1.setText(Formatter.천단위구분(list.get(0).누적잔액.amount));
        누적잔액2.setText(Formatter.천단위구분(list.get(1).누적잔액.amount));
        누적잔액3.setText(Formatter.천단위구분(list.get(2).누적잔액.amount));
        누적잔액4.setText(Formatter.천단위구분(list.get(3).누적잔액.amount));
        누적잔액5.setText(Formatter.천단위구분(list.get(4).누적잔액.amount));
        누적잔액6.setText(Formatter.천단위구분(list.get(5).누적잔액.amount));


        지출1.setText(Formatter.천단위구분(list.get(0).총지출.amount));
        지출2.setText(Formatter.천단위구분(list.get(1).총지출.amount));
        지출3.setText(Formatter.천단위구분(list.get(2).총지출.amount));
        지출4.setText(Formatter.천단위구분(list.get(3).총지출.amount));
        지출5.setText(Formatter.천단위구분(list.get(4).총지출.amount));
        지출6.setText(Formatter.천단위구분(list.get(5).총지출.amount));


        수입1.setText(Formatter.천단위구분(list.get(0).총수입.amount));
        수입2.setText(Formatter.천단위구분(list.get(1).총수입.amount));
        수입3.setText(Formatter.천단위구분(list.get(2).총수입.amount));
        수입4.setText(Formatter.천단위구분(list.get(3).총수입.amount));
        수입5.setText(Formatter.천단위구분(list.get(4).총수입.amount));
        수입6.setText(Formatter.천단위구분(list.get(5).총수입.amount));

    }

//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return gestureDetector.onTouchEvent(event);
//    }

//    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
////        private final TextView textView;
//        private static final int SWIPE_THRESHOLD = 100;
//
//        MyGestureListener() {
////            this.textView = textView;
//        }
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
////            textView.setText("Single Tap Detected");
//            return true;
//        }
//
//        @Override
//        public void onLongPress(MotionEvent e) {
////            textView.setText("Long Press Detected");
//        }
//
//        @Override
//        public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
//            return super.onFling(e1, e2, velocityX, velocityY);
//
//
//        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            if (Math.abs(distanceX) > Math.abs(distanceY)) {
//                if (distanceX > SWIPE_THRESHOLD) {
//                    Log.i("onScroll","distanceX > SWIPE_THRESHOLD");
////                    textView.setText("Right Swipe Detected");
//                } else if (distanceX < -SWIPE_THRESHOLD) {
////                    textView.setText("Left Swipe Detected");
//                    Log.i("onScroll","distanceX < -SWIPE_THRESHOLD");
//
//                }
//            }
//
//            return true;
//        }
//
//    }

    private BarData generateBarData(List<자산통계데이터> dataList, BarChart barChart) {

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            자산통계데이터 data = dataList.get(i);
            entries1.add(new BarEntry(Float.valueOf(Integer.toString(i)), data.총지출.amount));
            entries2.add(new BarEntry(i, data.총수입.amount));
        }
//        barChart.setViewPortOffsets(100, 50, 100, 50);
//
////        barChart.setViewPortOffsets(100, 50, 100, 50);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                barChart.invalidate();
//
//                barChart.setVisibility(View.VISIBLE);
//            }
//        },90);
//        barChart.getAxisLeft().setXOffset(20f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(dataList.size()+0.1f);

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.RED);
        set1.setValueTextColor(Color.RED);
        set1.setValueTextSize(10f);
        set1.setDrawValues(false);
//        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "Bar 2");
//        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set2.setColors(Color.BLUE);
        set2.setValueTextColor(Color.BLUE);
        set2.setValueTextSize(10f);
        set2.setDrawValues(false);
//        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);

//        float groupSpace = 0.06f;
//        float barSpace = 0.02f; // x2 dataset
//        float barWidth = 0.45f; // x2 dataset
//        float groupSpace = 0.05f;
//        float barSpace = 0.05f; // x2 dataset
//        float barWidth = 0.4f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData data = new BarData(set1, set2);
//        d.setBarWidth(barWidth);
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset

//        float groupSpace = 0.06f;
//        float barSpace = 0.02f; // x2 dataset
//        float barWidth = 0.45f; // x2 dataset
        data.setBarWidth(barWidth);

        barChart.setData(data);
        barChart.notifyDataSetChanged();

        barChart.groupBars(0, groupSpace, barSpace);
        barChart.setFitBars(true);

        return data;
    }

    private LineData getData(List<자산통계데이터> dataList) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            자산통계데이터 data = dataList.get(i);
//            stringList.add(Integer.toString(data.월)+"월");
            values.add(new Entry(i+0.5f, data.누적잔액.amount));
        }

//        // set custom chart offsets (automatic offset calculation is hereby disabled)
//        lineChart.setViewPortOffsets(100, 50, 100, 50);
        
        
        // 간헐적으로 viewPortOffset이 안먹혀서 hander로 invalidate() 실행하도록 해서 임시해결

//        barChart.setViewPortOffsets(100, 50, 100, 50);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                lineChart.invalidate();
//
//                lineChart.setVisibility(View.VISIBLE);
//            }
//        },90);
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");

        set1.setLineWidth(1.75f);
        set1.setCircleRadius(5f);
        set1.setCircleHoleRadius(2.5f);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setHighLightColor(Color.TRANSPARENT);
        set1.setDrawValues(false);

        // create a data object with the data sets
        return new LineData(set1);
    }

    private void setupChart(LineChart chart) {

        chart.notifyDataSetChanged();
        // no description text
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        // enable scaling and dragging
        chart.setDragEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);
//        chart.getAxisLeft().set
////        chart.get
//        chart.getAxisLeft().setSpaceTop(20);
        chart.getAxisLeft().setLabelCount(3);
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                if((int)value == 0){
                    return "0";
                }
                int 값 = (int)value/1000;
                if(값 == 0){
                    return Integer.toString((int)value);
                }
                else{
                    return Integer.toString(값)+"k";
                }
//                if()
//                return Integer.toString((int)value);
            }
        });

//        chart.getAxisLeft().setSpaceBottom(0);
        chart.getAxisRight().setEnabled(false);

//        chart.getXAxis().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        chart.getXAxis().setLabelCount(stringList.size());
        chart.getXAxis().setCenterAxisLabels(true);
//        chart.getXAxis().setGranularity(1f);
        XAxis xAxis = chart.getXAxis();
//        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setCenterAxisLabels(true);

        xAxis.setDrawGridLines(false);
//        xAxis.setAxisMinimum(-1);
//        xAxis.setAxisMaximum(5);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Date로 가져와서, 날짜 가져오기.
//                Log.i("",Integer.toString((int)value));
//                Log.i("",Integer.toString(stringList.size()));
                Log.i("getFormattedValue",Float.toString(value));
//                return Float.toString(value);

                if((int)value >= 0 && (int)value < stringList.size())
                    return stringList.get((int)value) ;
                else return "";
            }
        });

//
//        chart.setScaleEnabled(false);
//        chart.setAutoScaleMinMaxEnabled(false);

//        chart.invalidate();
    }

    private void setupChart(BarChart chart) {

        chart.getDescription().setEnabled(false);
        Legend l = chart.getLegend();
        l.setEnabled(false);

        chart.getAxisLeft().setLabelCount(3);
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                if((int)value == 0){
                    return "0";
                }
                int 값 = (int)value/1000;
                if(값 == 0){
                    return Integer.toString((int)value);
                }
                else{
                    return Integer.toString(값)+"k";
                }
            }
        });

        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = chart.getXAxis();
        xAxis.setCenterAxisLabels(true);

        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        chart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Date로 가져와서, 날짜 가져오기.
//                Log.i("",Integer.toString((int)value));
//                Log.i("",Integer.toString(stringList.size()));
//                return Float.toString(value);
                if((int)value >= 0 && (int)value < stringList.size())
                    return stringList.get((int)value) ;
                else return "";
            }
        });
        chart.setDragEnabled(false);
//        // create marker to display box when values are selected
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//
//        // Set the marker to the chart
//        mv.setChartView(chart);
//        chart.setMarker(mv);

//        chart.setScaleEnabled(false);
//
//        chart.setAutoScaleMinMaxEnabled(false);
//        chart.setScaleEnabled(false);
//        chart.setAutoScaleMinMaxEnabled(false);
//        chart.setScaleXEnabled(false);
//        chart.setScaleYEnabled(false);

//        XAxis xAxis = chart.getXAxis();
        chart.setScaleEnabled(false);
//        chart.setAutoScaleMinMaxEnabled(false);

//        chart.invalidate();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    
        // 뒤로가기 버튼을 눌렀을 때의 동작
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
//
//    public  class 전체통계{
//        int 월;
//        Amount 누적잔액;
//        Amount 총수입;
//        Amount 총지출;
//        전체통계(){
//            누적잔액 = new Amount();
//            누적잔액.amount = 0;
//
//            총수입 = new Amount();
//            총수입.amount = 0;
//
//            총지출 = new Amount();
//            총지출.amount = 0;
//        }
//
//    }

}

//        gestureDetector = new GestureDetector(this, new MyGestureListener());


//        Log.i("getExtraLeftOffset",Float.toString(lineChart.get()));

//        Log.i("getExtraLeftOffset",Float.toString(lineChart.getExtraLeftOffset()));
//        detector = new SimpleGestureFilter(FullStatisticsActivity.this, new SimpleGestureFilter.SimpleGestureListener() {
//            @Override
//            public void onSwipe(int direction) {
//            //Detect the swipe gestures and display toast
//                String showToastMessage = "";
//
//                switch (direction) {
//
//                    case SimpleGestureFilter.SWIPE_RIGHT:
//                        showToastMessage = "You have Swiped Right.";
//                        break;
//                    case SimpleGestureFilter.SWIPE_LEFT:
//                        showToastMessage = "You have Swiped Left.";
//                        break;
//                    case SimpleGestureFilter.SWIPE_DOWN:
//                        showToastMessage = "You have Swiped Down.";
//                        break;
//                    case SimpleGestureFilter.SWIPE_UP:
//                        showToastMessage = "You have Swiped Up.";
//                        break;
//
//                }
//
//                Log.i("onSwipe",showToastMessage);
//                Toast.makeText(FullStatisticsActivity.this, showToastMessage, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDoubleTap() {
//                Toast.makeText(FullStatisticsActivity.this, "You have Double Tapped.", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });
//
//        barChart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                detector.onTouchEvent(event);
//                return false;
//            }
//        });
//        lineChart.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                detector.onTouchEvent(event);
//                return false;
//            }
//        });