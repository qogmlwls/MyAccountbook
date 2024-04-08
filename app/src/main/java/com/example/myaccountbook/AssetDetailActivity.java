package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myaccountbook.adapter.AssetChangeOrderAdapter;
import com.example.myaccountbook.adapter.AssetDetailAdapter;
import com.example.myaccountbook.data.Amount;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Formatter;
import com.example.myaccountbook.data.RequestCode;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class AssetDetailActivity extends CommonActivity {

    TextView 사용기간, 입금, 출금, 합계, 누적잔액;
    Button 날짜버튼;
    AssetDataManager assetDataManager;

    Date 날짜;

    int 자산식별자;
    String 자산명;

    boolean result;
    Toolbar toolbar;

    MyApplication application;

    boolean 화면상세내역임, 통계초기화안되어있음;


    List<String> stringList;

    TextView 지출1, 지출2, 지출3, 지출4, 지출5, 지출6;
    TextView 수입1, 수입2, 수입3, 수입4, 수입5, 수입6;
    TextView 누적잔액1,누적잔액2,누적잔액3,누적잔액4,누적잔액5,누적잔액6;

    List<AssetDetailActivity.통계> 통계목록;

    LineChart lineChart;
    BarChart barChart;
    TableLayout tableLayout,tableLayout1;

    GestureManager manager;

    RecyclerView recyclerView;
    AssetDetailAdapter adapter;
    List<accountBook> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);

        result = true;
        화면상세내역임 = true;
        통계초기화안되어있음 = true;

        print("----------------------------------------------");

        ImageButton 이전날짜 = findViewById(R.id.imageButton19);
        ImageButton 다음날짜 = findViewById(R.id.imageButton20);
        ImageButton 자산수정버튼 = findViewById(R.id.imageButton21);
        ImageButton 자산상세그래프내역 = findViewById(R.id.imageButton22);
        날짜버튼 = findViewById(R.id.button40);

        입금 = findViewById(R.id.textView132);
        출금 = findViewById(R.id.textView133);
        합계 = findViewById(R.id.textView134);
        누적잔액 = findViewById(R.id.textView135);
        사용기간 = findViewById(R.id.textView127);


        누적잔액1 = findViewById(R.id.textView177);
        누적잔액2 = findViewById(R.id.textView178);
        누적잔액3 = findViewById(R.id.textView179);
        누적잔액4 = findViewById(R.id.textView180);
        누적잔액5 = findViewById(R.id.textView181);
        누적잔액6 = findViewById(R.id.textView182);

        수입1 = findViewById(R.id.textView183);
        수입2 = findViewById(R.id.textView184);
        수입3 = findViewById(R.id.textView185);
        수입4 = findViewById(R.id.textView186);
        수입5 = findViewById(R.id.textView187);
        수입6 = findViewById(R.id.textView188);

        지출1 = findViewById(R.id.textView189);
        지출2 = findViewById(R.id.textView190);
        지출3 = findViewById(R.id.textView191);
        지출4 = findViewById(R.id.textView192);
        지출5 = findViewById(R.id.textView193);
        지출6 = findViewById(R.id.textView194);

        recyclerView = findViewById(R.id.recyclerView13);
        tableLayout = findViewById(R.id.tableLayout8);
        tableLayout1 = findViewById(R.id.tableLayout9);
        // 뒤로가기
        lineChart = findViewById(R.id.lineChart2);
        barChart = findViewById(R.id.chart2);
        toolbar = findViewById(R.id.toolbar18);

        print("----------------------------------------------");
        날짜 = (Date) getIntent().getSerializableExtra("날짜");
        자산식별자 = getIntent().getIntExtra("자산식별자",-1);
        자산명 = getIntent().getStringExtra("자산명");

        application = (MyApplication)getApplication();
        assetDataManager = application.assetDataManager;

        통계목록 = new ArrayList<>();
        stringList = new ArrayList<>();
        values = new ArrayList<>();
        manager = new GestureManager(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        if(날짜 == null){
            날짜 = new Date();
            날짜.today();
            날짜.setDay(1);
        }

        toolbar.setTitle(자산명);
        날짜버튼.setText(날짜.년월날짜());
        사용기간.setText(사용기간날짜글자(날짜));

        이전날짜.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(result){

                    result = false;

                    // 누적잔액은 현재 누적잔액에서 - 현재 합계를 빼기
                    날짜.이전달로이동();
//                    날짜 = Date.이전날짜(날짜);
                    AssetDataManager.자산상세공통데이터 자산상세공통데이터 = assetDataManager.자산상세공통데이터(날짜, 자산식별자);
                    입금출금합계누적잔액표시(자산상세공통데이터);
                    사용기간.setText(사용기간날짜글자(날짜));
                    날짜버튼.setText(날짜.년월날짜());

                    if(화면상세내역임){
                        list.clear();
                        list = assetDataManager.자산상세내역데이터(날짜, 자산식별자,application);
                        if(list.size() == 0){
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.VISIBLE);
                        }
                        else{
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.INVISIBLE);
                        }

                        adapter.setData(list,자산명);
                    }
                    else{
                        그래프변경();
                    }


                    result = true;

                }

            }
        });
        다음날짜.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(result){
                    result = false;
                    // 누적잔액은 현재 누적잔액에서 - 현재 합계를 빼기

                    날짜.다음달로이동();
//                    날짜 = Date.이후날짜(날짜);

                    AssetDataManager.자산상세공통데이터 자산상세공통데이터 = assetDataManager.자산상세공통데이터(날짜, 자산식별자);
                    입금출금합계누적잔액표시(자산상세공통데이터);
                    사용기간.setText(사용기간날짜글자(날짜));
                    날짜버튼.setText(날짜.년월날짜());

                    if(화면상세내역임) {
                        list.clear();
                        list = assetDataManager.자산상세내역데이터(날짜, 자산식별자, application);

                        if (list.size() == 0) {
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.INVISIBLE);
                        }
                        adapter.setData(list, 자산명);
                    }
                    else{
                        그래프변경();
                    }


                    result = true;

                }

            }
        });
        자산상세그래프내역.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result){
                    result = false;

                    if(!화면상세내역임){

                        // 리사이클러뷰
                        // 상세내역
                        자산상세그래프내역.setImageResource(R.drawable.baseline_insert_chart_outlined_24); // 새 이미지로 변경

                        lineChart.setVisibility(View.INVISIBLE);
                        barChart.setVisibility(View.INVISIBLE);
                        tableLayout.setVisibility(View.INVISIBLE);
                        tableLayout1.setVisibility(View.INVISIBLE);


                        통계목록.clear();
                        stringList.clear();

                        values.clear();

                        lineChart.getData().clearValues();
                        barChart.getData().clearValues();

                        lineChart.clear();
                        barChart.clear();


                        recyclerView.setVisibility(View.VISIBLE);

                        list = assetDataManager.자산상세내역데이터(날짜, 자산식별자,  application);
                        if (list.size() == 0) {
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.INVISIBLE);
                        }

                        adapter.setData(list, 자산명);


                    }
                    else{
                        // 그래프 보여짐.
                        자산상세그래프내역.setImageResource(R.drawable.baseline_format_list_bulleted_24); // 새 이미지로 변경

                        lineChart.setVisibility(View.VISIBLE);
                        barChart.setVisibility(View.VISIBLE);
                        tableLayout.setVisibility(View.VISIBLE);
                        tableLayout1.setVisibility(View.VISIBLE);

                        recyclerView.setVisibility(View.INVISIBLE);
                        TextView textView = findViewById(R.id.textView176);
                        textView.setVisibility(View.INVISIBLE);

                        list.clear();
                        adapter.clear();


                        if(통계초기화안되어있음){
                            통계초기화안되어있음 = false;

                            통계목록 = assetDataManager.자산통계데이터목록(날짜,자산식별자);

                            stringList = new ArrayList<>();
                            for (int i = 0; i < 통계목록.size(); i++) {
                                AssetDetailActivity.통계 data = 통계목록.get(i);
                                stringList.add(Integer.toString(data.월)+"월");
                            }
                            for(int i=0;i<통계목록.size();i++){
                                AssetDetailActivity.통계 통계 = 통계목록.get(i);

                                Log.i("통계",String.valueOf(통계.월));
                                Log.i("통계",통계.누적잔액.getAmount());
                                Log.i("통계",통계.총수입.getAmount());
                                Log.i("통계",통계.총지출.getAmount());

                            }

                            // 날짜까지의
                            // 누적 잔액

//-------------------------
                            LineData data = getData(통계목록,Color.RED,lineChart);
                            // add some transparency to the color with "& 0x90FFFFFF"
                            setupChart(lineChart, data, Color.RED);
                            lineChart.getXAxis().setAxisMinimum(0);
                            lineChart.getXAxis().setAxisMaximum(통계목록.size()+0.1f);
                            BarData barData = generateBarData(통계목록, barChart);

                            setupChart(barChart,Color.RED);

                            하단화면표시();


                            lineChart.setViewPortOffsets(100, 100, 100, 50);
                            barChart.setViewPortOffsets(100, 50, 100, 50);

                            // create marker to display box when values are selected
                            MyMarkerView mv = new MyMarkerView(AssetDetailActivity.this, R.layout.custom_marker_view);

                            // Set the marker to the chart
                            mv.setChartView(lineChart);
                            lineChart.setMarker(mv);

                            // create marker to display box when values are selected
                            MyMarkerView mv2 = new MyMarkerView(AssetDetailActivity.this, R.layout.custom_marker_view);

                            // Set the marker to the chart
                            mv2.setChartView(barChart);
                            barChart.setMarker(mv2);

                            lineChart.invalidate();
                            barChart.invalidate();

                        }
                        else{
                            화면();
                        }


                    }

                    화면상세내역임 = !화면상세내역임;

                    result = true;

                }
            }
        });
        자산수정버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result){
                    result = false;
                    Intent intent = new Intent(AssetDetailActivity.this, AssetEditActivity.class);
    //                intent.putExtra("날짜",날짜);
                    intent.putExtra("자산식별자",자산식별자);
                    intent.putExtra("자산명",자산명);
                    startActivityForResult(intent, RequestCode.REQUEST_CHANGEASSET);
//                    startActivity(intent);
    //                overridePendingTransition(0,0);
    //                finish();
                    result = true;

                }
            }
        });

        날짜버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result){
                    result = false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(AssetDetailActivity.this);
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
                            if(result) {
                                result = false;

                                int yearValue = year.getValue();
                                int monthValue = month.getValue();

                                날짜.setYear(yearValue);
                                날짜.setMonth(monthValue);
                                날짜.setDay(1);

                                AssetDataManager.자산상세공통데이터 자산상세공통데이터 = assetDataManager.자산상세공통데이터(날짜, 자산식별자);
                                입금출금합계누적잔액표시(자산상세공통데이터);
                                사용기간.setText(사용기간날짜글자(날짜));
                                날짜버튼.setText(날짜.년월날짜());

                                if(화면상세내역임) {
                                    list.clear();
                                    list = assetDataManager.자산상세내역데이터(날짜, 자산식별자,application);
                                    if (list.size() == 0) {
                                        TextView textView = findViewById(R.id.textView176);
                                        textView.setVisibility(View.VISIBLE);
                                    } else {
                                        TextView textView = findViewById(R.id.textView176);
                                        textView.setVisibility(View.INVISIBLE);
                                    }

                                    adapter.setData(list, 자산명);
                                }
                                else{
                                    그래프변경();
                                }

                                result = true;

                            }

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



        AssetDataManager.자산상세공통데이터 자산상세공통데이터 = assetDataManager.자산상세공통데이터(날짜, 자산식별자);
        입금출금합계누적잔액표시(자산상세공통데이터);



        list = assetDataManager.자산상세내역데이터(날짜, 자산식별자,application);
        if(list.size() == 0){
            TextView textView = findViewById(R.id.textView176);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            TextView textView = findViewById(R.id.textView176);
            textView.setVisibility(View.INVISIBLE);
        }

        adapter = new AssetDetailAdapter(list,AssetDetailActivity.this,자산명,자산식별자,manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                manager.getDetector().onTouchEvent(event);
                return false;
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

        manager.setMyInterface(new MyGestureListener.MyInterface() {
            @Override
            public void 이전으로이동() {
                if(result){

                    result = false;

                    // 누적잔액은 현재 누적잔액에서 - 현재 합계를 빼기
                    날짜.이전달로이동();
//                    날짜 = Date.이전날짜(날짜);
                    AssetDataManager.자산상세공통데이터 자산상세공통데이터 = assetDataManager.자산상세공통데이터(날짜, 자산식별자);
                    입금출금합계누적잔액표시(자산상세공통데이터);
                    사용기간.setText(사용기간날짜글자(날짜));
                    날짜버튼.setText(날짜.년월날짜());

                    if(화면상세내역임){
                        list.clear();
                        list = assetDataManager.자산상세내역데이터(날짜, 자산식별자,application);
                        if(list.size() == 0){
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.VISIBLE);
                        }
                        else{
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.INVISIBLE);
                        }

                        adapter.setData(list,자산명);
                    }
                    else{
                        그래프변경();
                    }


                    result = true;

                }            }

            @Override
            public void 이후로이동() {
                if(result){
                    result = false;
                    // 누적잔액은 현재 누적잔액에서 - 현재 합계를 빼기

                    날짜.다음달로이동();
//                    날짜 = Date.이후날짜(날짜);

                    AssetDataManager.자산상세공통데이터 자산상세공통데이터 = assetDataManager.자산상세공통데이터(날짜, 자산식별자);
                    입금출금합계누적잔액표시(자산상세공통데이터);
                    사용기간.setText(사용기간날짜글자(날짜));
                    날짜버튼.setText(날짜.년월날짜());

                    if(화면상세내역임) {
                        list.clear();
                        list = assetDataManager.자산상세내역데이터(날짜, 자산식별자, application);

                        if (list.size() == 0) {
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.INVISIBLE);
                        }
                        adapter.setData(list, 자산명);
                    }
                    else{
                        그래프변경();
                    }


                    result = true;

                }
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return manager.getDetector().onTouchEvent(event) || super.onTouchEvent(event);
    }


    void 입금출금합계누적잔액표시(AssetDataManager.자산상세공통데이터 자산상세공통데이터){

        입금.setText(Formatter.천단위구분(자산상세공통데이터.입금));

        출금.setText(Formatter.천단위구분(자산상세공통데이터.출금));

        합계.setText(Formatter.천단위구분(자산상세공통데이터.합계));

        if(자산상세공통데이터.누적잔액 >= 0){
            누적잔액.setText(Formatter.천단위구분(자산상세공통데이터.누적잔액));
            누적잔액.setTextColor(Color.BLUE);
        }
        else if(자산상세공통데이터.누적잔액 < 0){
            누적잔액.setText(Formatter.천단위구분(자산상세공통데이터.누적잔액*-1));
            누적잔액.setTextColor(Color.RED);
        }
        else{

        }

    }

    String 사용기간날짜글자(Date date){
        int year = date.getYear()%100;
        java.util.Calendar calendar = Date.getCalendar(date);
        int 말일 = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return Integer.toString(year)+"."+Integer.toString(date.getMonth())
                +".1 ~ "+Integer.toString(year)+"."+Integer.toString(date.getMonth())+"."+Integer.toString(말일);
    }


    private BarData generateBarData(List<AssetDetailActivity.통계> dataList, BarChart barChart) {

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            AssetDetailActivity.통계 data = dataList.get(i);
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

    ArrayList<Entry> values;
    private LineData getData(List<AssetDetailActivity.통계> dataList, int color, LineChart lineChart) {


        for (int i = 0; i < dataList.size(); i++) {
            통계 data = dataList.get(i);
//            stringList.add(Integer.toString(data.월)+"월");
            Drawable drawable = getDrawable(R.drawable.not_select_circle);
            Entry entry = new Entry(i+0.5f,data.누적잔액.amount,drawable);
            values.add(entry);
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

        // create a data object with the data sets
        return getData(set1);
    }
    private LineData getData(LineDataSet set1) {

        // 선 굵기
        set1.setLineWidth(1.75f);
        // 원 크기
        set1.setCircleRadius(5f);
        // 가운데 하얀 원 크기
        set1.setCircleHoleRadius(2.5f);

        // 선 색상
        set1.setColor(Color.RED);
        // 원 색상
        set1.setCircleColor(Color.RED);
        // 클릭시 보이는 highlight 선 색상 투명하게(안보이게)
        set1.setHighLightColor(Color.TRANSPARENT);
        // 숫자 미표시
        set1.setDrawValues(false);


        // create a data object with the data sets
        return new LineData(set1);
    }
    private void setupChart(LineChart chart, LineData data, int color) {
        // add data
        lineChart.setData(data);
        // 데이터 변경 알림
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

        // 하단 우측에 표시되는 설명 안보이게 하기
        lineChart.getDescription().setEnabled(false);
        // 기존 배경색으로 배경 안그리기?
        lineChart.setDrawGridBackground(false);
        // 드래그 비활성화
        lineChart.setDragEnabled(false);
        // pinchzoom 비활성화 (손가락으로 꼬집기 등의 제스쳐하면 화면 확대/축소)
        lineChart.setPinchZoom(false);
        // 그래프 확대/축소 비활성화
        lineChart.setScaleEnabled(false);

        // 하단에 표시되는 LineDataSet lable 안보이게 설정
        Legend l = lineChart.getLegend();
        l.setEnabled(false);


        // y축 value 3개만 보여주기. (갯수 최대 3개로 설정)
        lineChart.getAxisLeft().setLabelCount(3);
        lineChart.getAxisRight().setLabelCount(3);
        lineChart.getAxisLeft().setCenterAxisLabels(true);
        lineChart.getAxisLeft().setGranularity(1f);

        int 최대,최소;
        최대 = 통계목록.get(0).누적잔액.amount;
        최소 = 통계목록.get(0).누적잔액.amount;
        for(int i=1;i<통계목록.size();i++){

            통계 통계 = 통계목록.get(i);
            if(통계.누적잔액.amount > 최대){
                최대 = 통계.누적잔액.amount;
            }
            if (통계.누적잔액.amount < 최소) {
                최소 = 통계.누적잔액.amount;
            }


        }
//        lineChart.getAxisLeft().setAxisMinimum(최소-0.1f);
//        lineChart.getAxisLeft().setAxisMaximum(최대+0.1f);

        // y축 오른쪽 비활성화
        lineChart.getAxisRight().setEnabled(false);

        // x 축 가져오기
        XAxis xAxis = lineChart.getXAxis();
        // label 중앙 정렬
        xAxis.setCenterAxisLabels(true);
        // 표에 x축 선 그리지 않기
        xAxis.setDrawGridLines(false);

        // 하단에 표시하기
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // x 축 값 시작(최소), 끝(최대) 값을 설정하여 보여지는 범위를 설정한다.
        lineChart.getXAxis().setAxisMinimum(0);
        lineChart.getXAxis().setAxisMaximum(통계목록.size()+0.1f);


        // X축 value formatter
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                // value 가 0.0, 1.0, 2.0, 3.0, 4.0 ... 으로 바뀜.
                // Date로 가져와서, 날짜 가져오기.
                if((int)value >= 0 && (int)value < stringList.size())
                    return stringList.get((int)value) ;
                else return "";

//                return Float.toString(value);

            }
        });


        // Y축 value formatter
        lineChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

//                return Float.toString(value);

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

        // 문제가 있음. 간헐적으로 동작안함..
        // 그래프 padding 처리
        lineChart.setViewPortOffsets(100, 200, 100, 50);
        lineChart.invalidate();


    }
    MyMarkerView mv;
    private void setupChart(BarChart chart, int color) {
//        chart.setHighlightPerTapEnabled(false);

        // no description text
        chart.getDescription().setEnabled(false);

        // chart.setDrawHorizontalGrid(false);
        //
//        // enable / disable grid background
//        chart.setDrawGridBackground(false);
////        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);
////
////        // enable touch gestures
////        chart.setTouchEnabled(true);
////
////        // enable scaling and dragging
////        chart.setDragEnabled(true);
//////        chart.setScaleEnabled(true);
//
//        // if disabled, scaling can be done on x- and y-axis separately
//        chart.setPinchZoom(false);

//        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
//        chart.setViewPortOffsets(100, 100, 100, 100);

//        chart.setViewPortOffsets(100, 50, 100, 50);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);
//        chart.getAxisLeft().set
//        chart.get
//        chart.getAxisLeft().setSpaceTop(20);
        chart.getAxisLeft().setLabelCount(3);
        chart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                if((int)value == 0){
                    return "0";

//                    return "   0";
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
//        chart.getXAxis().setCenterAxisLabels(true);
//        chart.getXAxis().setGranularity(1f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setCenterAxisLabels(true);


//        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);
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
//                Log.i("",Integer.toString((int)value));
//                Log.i("",Integer.toString(stringList.size()));
//                return Float.toString(value);
                if((int)value >= 0 && (int)value < stringList.size())
                    return stringList.get((int)value) ;
                else return "";
            }
        });
        chart.setDragEnabled(false);

//        MyMarkerView mv2 = new MyMarkerView(AssetDetailActivity.this, R.layout.custom_marker_view);
//
//        // Set the marker to the chart
//        mv2.setChartView(barChart);
//        barChart.setMarker(mv2);
//        barChart.invalidate();
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

    private void 하단화면표시(){

        누적잔액1.setText(Formatter.천단위구분(통계목록.get(0).누적잔액.amount));
        누적잔액2.setText(Formatter.천단위구분(통계목록.get(1).누적잔액.amount));
        누적잔액3.setText(Formatter.천단위구분(통계목록.get(2).누적잔액.amount));
        누적잔액4.setText(Formatter.천단위구분(통계목록.get(3).누적잔액.amount));
        누적잔액5.setText(Formatter.천단위구분(통계목록.get(4).누적잔액.amount));
        누적잔액6.setText(Formatter.천단위구분(통계목록.get(5).누적잔액.amount));


        지출1.setText(Formatter.천단위구분(통계목록.get(0).총지출.amount));
        지출2.setText(Formatter.천단위구분(통계목록.get(1).총지출.amount));
        지출3.setText(Formatter.천단위구분(통계목록.get(2).총지출.amount));
        지출4.setText(Formatter.천단위구분(통계목록.get(3).총지출.amount));
        지출5.setText(Formatter.천단위구분(통계목록.get(4).총지출.amount));
        지출6.setText(Formatter.천단위구분(통계목록.get(5).총지출.amount));


        수입1.setText(Formatter.천단위구분(통계목록.get(0).총수입.amount));
        수입2.setText(Formatter.천단위구분(통계목록.get(1).총수입.amount));
        수입3.setText(Formatter.천단위구분(통계목록.get(2).총수입.amount));
        수입4.setText(Formatter.천단위구분(통계목록.get(3).총수입.amount));
        수입5.setText(Formatter.천단위구분(통계목록.get(4).총수입.amount));
        수입6.setText(Formatter.천단위구분(통계목록.get(5).총수입.amount));



    }

    void 그래프변경(){

        통계목록.clear();
        stringList.clear();

        values.clear();

        lineChart.getData().clearValues();
        barChart.getData().clearValues();

        lineChart.clear();
        barChart.clear();

        화면();

    }

    void 화면(){
        통계목록 = assetDataManager.자산통계데이터목록(날짜,자산식별자);
        for (int i = 0; i < 통계목록.size(); i++) {
            AssetDetailActivity.통계 data = 통계목록.get(i);
            stringList.add(Integer.toString(data.월)+"월");
        }


        for(int i=0;i<통계목록.size();i++){
            AssetDetailActivity.통계 통계 = 통계목록.get(i);

            Log.i("통계",String.valueOf(통계.월));
            Log.i("통계",통계.누적잔액.getAmount());
            Log.i("통계",통계.총수입.getAmount());
            Log.i("통계",통계.총지출.getAmount());

        }

        //-------------------------

        values.clear();

        LineData data = getData(통계목록,Color.RED,lineChart);
        // add some transparency to the color with "& 0x90FFFFFF"
//        setupChart(lineChart, data, Color.RED);
        lineChart.setData(data);
        lineChart.notifyDataSetChanged();

        BarData barData = generateBarData(통계목록, barChart);

//        setupChart(barChart,Color.RED);

        lineChart.invalidate();
        barChart.invalidate();

        하단화면표시();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RequestCode.REQUEST_CHANGEASSET) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {

                    String 변경 = data.getStringExtra("변경");
                    if(변경 == null){

                    }
                    else if(변경.equals("삭제")){
                        setResult(RESULT_OK);
                        finish();
                    }
                    else if(변경.equals("수정")){
                        String 자산명 = data.getStringExtra("자산명");

                        // toolbar title, 내역의 자산명

                        this.자산명 = 자산명;
                        toolbar.setTitle(자산명);

                        adapter.set자산명(자산명);
                        setResult(RESULT_OK);

                    }



                }
            }
        }
        if (requestCode == RequestCode.REQUEST_ASSET_RECORD) {
            print("REQUEST_ASSET_RECORD 결과가 OK인 경우");

            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {

                    print("data != null");
                    if (화면상세내역임) {
                        print("화면상세내역임");

                        //  입금, 출금, 합계, 누적잔액 다시 가져오기.
                        AssetDataManager.자산상세공통데이터 자산상세공통데이터 = assetDataManager.자산상세공통데이터(날짜, 자산식별자);
                        입금출금합계누적잔액표시(자산상세공통데이터);

                        // 리사이클러뷰
                        // 상세내역
                        list.clear();
                        adapter.clear();

                        list = assetDataManager.자산상세내역데이터(날짜, 자산식별자,  application);
                        if (list.size() == 0) {
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            TextView textView = findViewById(R.id.textView176);
                            textView.setVisibility(View.INVISIBLE);
                        }

                        adapter.setData(list, 자산명);
                        print("상세내역 재설정.");

                        setResult(RESULT_OK);

                    }
                }
            }
        }
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


    public static class 통계{
        int 월;
        Amount 누적잔액;
        Amount 총수입;
        Amount 총지출;
        통계(){
            누적잔액 = new Amount();
            누적잔액.amount = 0;

            총수입 = new Amount();
            총수입.amount = 0;

            총지출 = new Amount();
            총지출.amount = 0;
        }

    }
}

