package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.adapter.PercentAdapter;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.PieChartDataProvider;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerImage;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.animation.Easing;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
//import com.github.mikephil.charting.animation.Easing.EasingOption;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class PieChartActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    static final int REQUEST_DETAIL_PIECHART = 111;

    static final String 주기 = "period", 날짜 = "날짜", 이전주기 = "이전주기";

    Context context;

    String TAG = "PieChartActivity";

    Button 주기선택버튼, 날짜버튼, 기간시작날짜, 기간끝날짜;

    TabItem 수입으로변경, 지출로변경;

    int 기간, type;

    Date 기준이되는날짜;


    TabLayout tabLayout;

    MyApplication application;

    PieChartDataProvider chartDataProvider;
    PieChartDataProvider.TotalData totalData;

    RecyclerView recyclerView;
    PercentAdapter adapter;

    TextView 물결;
    int 이전기간;


    Date 시작날짜;
    Date 끝날짜;


    Button 가계부이동, 통계이동;
    List<PieChartDataProvider.Data> dataList;

    PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        가계부이동 = findViewById(R.id.button25);
        통계이동 = findViewById(R.id.button26);

        주기선택버튼 = findViewById(R.id.button32);
        날짜버튼 = findViewById(R.id.button31);

        기간시작날짜 = findViewById(R.id.button33);
        기간끝날짜 = findViewById(R.id.button34);
        물결 = findViewById(R.id.textView105);


        수입으로변경 = findViewById(R.id.tabitem1);
        지출로변경 = findViewById(R.id.tabitem2);

        tabLayout = findViewById(R.id.store_fragment_tablayout);
        gestureDetector = new GestureDetector(PieChartActivity.this, new PieChartActivity.MyGestureListener());

        context = getBaseContext();

        기간 = getIntent().getIntExtra(주기, -1);
        기준이되는날짜 = (Date) getIntent().getSerializableExtra(날짜);
        type = getIntent().getIntExtra("type", -1);

        application = (MyApplication) getApplication();
        chartDataProvider = new PieChartDataProvider(application.readDb);


        print(Integer.toString(기간));
        print(Integer.toString(type));

        if (기준이되는날짜 == null && 기간 == -1 && type == -1) {

            // 첫 시작.
            기준이되는날짜 = new Date();
            기준이되는날짜.today();
//            기준이되는날짜 = Date.today();
            type = accountBook.지출;
            기간 = PeriodManager.월간;

        }


        if (기간 != PeriodManager.기간) {

            날짜버튼.setText(Date.날짜글자(기준이되는날짜, 기간));
            날짜버튼.setVisibility(View.VISIBLE);

            if (기간 == PeriodManager.주간) {
                기준이되는날짜 = Date.기간시작날짜(기준이되는날짜, PeriodManager.주간);
            }

            totalData = chartDataProvider.getChartData(기준이되는날짜, 기간, type);

        }
        else {

            이전기간 = getIntent().getIntExtra(이전주기, -1);
            시작날짜 = (Date) getIntent().getSerializableExtra("시작날짜");
            끝날짜 = (Date) getIntent().getSerializableExtra("끝날짜");


            기간시작날짜.setVisibility(View.VISIBLE);
            기간끝날짜.setVisibility(View.VISIBLE);
            물결.setVisibility(View.VISIBLE);
            날짜버튼.setVisibility(View.GONE);

            if (시작날짜 == null && 끝날짜 == null) {

                기간시작날짜.setText(Date.기간시작날짜글자(기준이되는날짜, 이전기간));
                기간끝날짜.setText(Date.기간끝날짜글자(기준이되는날짜, 이전기간));

                시작날짜 = Date.기간시작날짜(기준이되는날짜, 이전기간);
                끝날짜 = Date.기간끝날짜(기준이되는날짜, 이전기간);

            } else {

                기간시작날짜.setText(Integer.toString(시작날짜.getYear() % 100) + "." + Integer.toString(시작날짜.getMonth()) + "." + Integer.toString(시작날짜.getDay()));
                기간끝날짜.setText(Integer.toString(끝날짜.getYear() % 100) + "." + Integer.toString(끝날짜.getMonth()) + "." + Integer.toString(끝날짜.getDay()));

            }

            totalData = chartDataProvider.getChartData(시작날짜, 끝날짜, type );

        }


        int 총합계 = 0;

        // 화면 초기화.

        // data 가져오기
        // 화면 구성.
        if (type == accountBook.수입) {
            tabLayout.selectTab(tabLayout.getTabAt(0));
            총합계 = totalData.총수입.amount;
        } else if (type == accountBook.지출) {
            tabLayout.selectTab(tabLayout.getTabAt(1));
            총합계 = totalData.총지출.amount;
        } else {

        }


        tabLayout.getTabAt(0).setText("수입 " + totalData.총수입.getAmount());
        tabLayout.getTabAt(1).setText("지출 " + totalData.총지출.getAmount());


        dataList = totalData.list;

        recyclerView = findViewById(R.id.recyclerView9);
        adapter = new PercentAdapter(PieChartActivity.this, dataList, 총합계, REQUEST_DETAIL_PIECHART, gestureDetector);

        if(기간 != PeriodManager.기간){
            adapter.setData(기간, type, 기준이되는날짜);
        }
        else{
//            시작날짜 = (Date) getIntent().getSerializableExtra("시작날짜");
//            끝날짜 = (Date) getIntent().getSerializableExtra("끝날짜");

//            public void setData(int 주기, int type, Date 시작날짜, Date 끝날짜){
                adapter.setData(기간, type, 시작날짜,끝날짜);
        }


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));


        // event 달기.
        가계부이동.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        통계이동.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PieChartActivity.this,PieChartActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);

                finish();
            }
        });

        // 스크롤시 다음화면으로 이동 이벤트 필수.

        주기선택버튼.setText(PeriodManager.getName(기간));
        주기선택버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopupMenu 객체 생성
                PopupMenu popup = new PopupMenu(context, v); //두 번째 파라미터가 팝업메뉴가 붙을 뷰
                //PopupMenu popup= new PopupMenu(MainActivity.this, btn2); //첫번째 버튼을 눌렀지만 팝업메뉴는 btn2에 붙어서 나타남
                getMenuInflater().inflate(R.menu.select_period, popup.getMenu());

                //팝업메뉴의 메뉴아이템을 선택하는 것을 듣는 리스너 객체 생성 및 설정
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        int id = menuItem.getItemId();

                        // 주간
                        if (id == R.id.weekly) {
                            주기변경(PeriodManager.주간);
                            return true;
                        }
                        // 월간
                        else if (id == R.id.monthly) {
                            주기변경(PeriodManager.월간);
                            return true;
                        }
                        // 연간
                        else if (id == R.id.yearly) {
                            주기변경(PeriodManager.연간);
                            return true;
                        }
                        // 기간
                        else if (id == R.id.period) {
                            주기변경(PeriodManager.기간);
                            return true;
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // tab이 선택되었을 때
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                print("onTabSelected");
                switch (tab.getPosition()) {
                    case 0:
                        타입변경(accountBook.수입);
                        break;
                    case 1:
                        타입변경(accountBook.지출);
                        break;
                }

            }

            // tab이 선택되지 않았을 때
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                print("onTabUnselected");
                // tab클릭시 실행되며, EditText입력 이벤트와 유사한듯


            }

            // tab이 다시 선택되었을 때
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                print("onTabReselected");

            }
        });


        if (기간 == PeriodManager.월간) {

            날짜버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(PieChartActivity.this);
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
                    year.setValue(기준이되는날짜.getYear());
                    month.setValue(기준이되는날짜.getMonth());

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


//                    보여질 값 설정 (string)
//                    String[] strings = arrayOf();
//                    String.format(d)
//                    year.setDisplayedValues(arrayOf("2019년", "2020년"));
//                    month.setDisplayedValues();

                    builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            int yearValue = year.getValue();
                            int monthValue = month.getValue();

                            기준이되는날짜.setYear(yearValue);
                            기준이되는날짜.setMonth(monthValue);
                            기준이되는날짜.setDay(1);

                            날짜변경(기준이되는날짜);

//                            Toast.makeText(getApplicationContext(), "OK Click", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
//                            alertDialog.dismiss();
//                            alertDialog.cancel();
//                            Toast.makeText(getApplicationContext(), "Cancel Click", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setView(view);
//                    AlertDialog alertDialog = null;
//                    year.selec
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });


        } else if (기간 == PeriodManager.주간) {

            날짜버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int year = 기준이되는날짜.getYear();
                    int month = 기준이되는날짜.getMonth() - 1;
                    int day = 기준이되는날짜.getDay();

                    DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int m_year, int m_month, int dayOfMonth) {

                            기준이되는날짜.setYear(m_year);
                            기준이되는날짜.setMonth( m_month + 1);
                            기준이되는날짜.setDay(dayOfMonth);

                            Log.i(TAG, "year : " + Integer.toString(m_year));
                            Log.i(TAG, "month : " + Integer.toString(m_month));
                            Log.i(TAG, "day : " + Integer.toString(dayOfMonth));

                            날짜변경(기준이되는날짜);

                        }
                    }, year, month, day);

                    dialog.show();

                }
            });

        } else if (기간 == PeriodManager.기간) {

            기간시작날짜.setOnClickListener(new View.OnClickListener() {
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

            기간끝날짜.setOnClickListener(new View.OnClickListener() {
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


        if (totalData.list.size() == 0) {

            // 데이터가 없습니다.

            TextView textView = findViewById(R.id.textView109);
            textView.setVisibility(View.VISIBLE);

//            TextView textView1 = findViewById(R.id.textView110);

            TextView textView2 = findViewById(R.id.textView110);
            textView2.setVisibility(View.VISIBLE);

//            totalData.

        }


        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });


        // ---------------------------------


        pieChart = findViewById(R.id.piechart);
        CustomMarkerView marker = new CustomMarkerView(PieChartActivity.this, R.layout.marker_view, dataList);
        pieChart.setMarker(marker);

        // 백분율 값 설정.
        pieChart.setUsePercentValues(true);

        // 설명 미사용으로 설정.
        pieChart.getDescription().setEnabled(false);

        // 여백 설정.
        pieChart.setExtraOffsets(20, 20, 20, 20);

        // 드래그 감속 설정.
        // 드래그 효과를 더 부드럽게 만들어주는
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        // 가운데 구멍을 그릴지 여부.
        pieChart.setDrawHoleEnabled(false);

        // 파이차트에 보여질 데이터
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        for (int i = 0; i < totalData.list.size(); i++) {
            PieChartDataProvider.Data data = totalData.list.get(i);
//           yValues.add(new PieEntry(data.금액.amount, data.카테고리명));
            yValues.add(new PieEntry(data.금액.amount, ""));
        }

        // Easing.EaseInOutQuad : 처음,나중에는 느리게, 중간에는 빠르게 애니메이션
        pieChart.animateY(1000, Easing.EaseInOutQuad); //애니메이션
        pieChart.animateX(1000, Easing.EaseInOutQuad);

        //
        PieDataSet dataSet = new PieDataSet(yValues, "음식 종류");
        // 파이 간격 설정.
        dataSet.setSliceSpace(4f);

        // 클릭시 간격 움직이는 정도
        dataSet.setSelectionShift(15f);

        // true로 설정하면 slice 위에 Entry로 설정한 값이 보여진다.
        // 만들어야하는 디자인에는 값이 필요없어 false로 설정해줬다.
        dataSet.setDrawValues(false);

        dataSet.setColors(PieChartDataProvider.PieColor);
        // 슬라이스 테두리 설정
        dataSet.setHighlightEnabled(true);

        //
        PieData data = new PieData((dataSet));
        // 퍼센트 글자 크기
        data.setValueTextSize(12f);
        pieChart.setRotationEnabled(false);

        pieChart.setEntryLabelTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setData(data);

        Legend l = pieChart.getLegend();
        l.setEnabled(false);

        pieChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("ActivityTouchEvent", "onTouchEvent: " + event.getAction());

        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }


    void print(String message) {
        Log.i(TAG, message);

    }

    void print(String key, String value) {
        Log.i(TAG, key + " : " + value);

    }


    void 전체화면변경(int 주기, int type, Date date) {

        Intent intent = new Intent(context, PieChartActivity.class);


        if (주기 == PeriodManager.기간) {

            if (시작날짜 != null && 끝날짜 != null) {
                intent.putExtra("시작날짜", 시작날짜);
                intent.putExtra("끝날짜", 끝날짜);
            }

        }
        if (this.기간 == PeriodManager.기간) {
            intent.putExtra(this.이전주기, 이전기간);
        } else {
            intent.putExtra(this.이전주기, 기간);
        }

        intent.putExtra(this.주기, 주기);
        intent.putExtra(this.날짜, date);
        intent.putExtra("type", type);
        startActivity(intent);
        finish();

    }

    void 전체화면변경(int 주기, int type, Date 시작날짜, Date 끝날짜) {

        Intent intent = new Intent(context, PieChartActivity.class);

        if (this.기간 == PeriodManager.기간) {
            intent.putExtra(this.이전주기, 이전기간);
            if (시작날짜 != null && 끝날짜 != null) {

                intent.putExtra("시작날짜", 시작날짜);
                intent.putExtra("끝날짜", 끝날짜);

            }
        } else {
            intent.putExtra(this.이전주기, 기간);
        }

        intent.putExtra(this.주기, 주기);
        intent.putExtra(this.날짜, 기준이되는날짜);
        intent.putExtra("type", type);
        startActivity(intent);
        finish();

    }

    void 날짜변경(Date date) {

        전체화면변경(기간, type, date);
        overridePendingTransition(0, 0);

    }


    void 날짜변경(Date date1, Date date2) {

        전체화면변경(기간, type, date1, date2);
        overridePendingTransition(0, 0);

    }


    void 타입변경(int type) {

        전체화면변경(기간, type, 기준이되는날짜);
        overridePendingTransition(0, 0);

    }

    void 주기변경(int 주기) {

        전체화면변경(주기, type, 기준이되는날짜);
        overridePendingTransition(0, 0);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (기간 != PeriodManager.기간) {

            totalData = chartDataProvider.getChartData(기준이되는날짜, 기간, type);

        }
        else {

            totalData = chartDataProvider.getChartData(시작날짜, 끝날짜, type );

        }

        // data 가져오기
        // 화면 구성.
        if (type == accountBook.수입) {

            adapter.set총합계(totalData.총수입.amount);
        } else if (type == accountBook.지출) {

            adapter.set총합계(totalData.총지출.amount);

        } else {

        }

        tabLayout.getTabAt(0).setText("수입 " + totalData.총수입.getAmount());
        tabLayout.getTabAt(1).setText("지출 " + totalData.총지출.getAmount());

        dataList.clear();
        dataList = totalData.list;

        adapter.setList(dataList);

        if (totalData.list.size() == 0) {

            // 데이터가 없습니다.

            TextView textView = findViewById(R.id.textView109);
            textView.setVisibility(View.VISIBLE);
//            TextView textView1 = findViewById(R.id.textView110);
            TextView textView2 = findViewById(R.id.textView110);
            textView2.setVisibility(View.VISIBLE);

        }

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        for (int i = 0; i < totalData.list.size(); i++) {
            PieChartDataProvider.Data providerData = totalData.list.get(i);
//           yValues.add(new PieEntry(data.금액.amount, data.카테고리명));
            yValues.add(new PieEntry(providerData.금액.amount, ""));
        }


        //
        PieDataSet dataSet = new PieDataSet(yValues, "음식 종류");
        // 파이 간격 설정.
        dataSet.setSliceSpace(4f);

        // 클릭시 간격 움직이는 정도
        dataSet.setSelectionShift(15f);

        // true로 설정하면 slice 위에 Entry로 설정한 값이 보여진다.
        // 만들어야하는 디자인에는 값이 필요없어 false로 설정해줬다.
        dataSet.setDrawValues(false);

        dataSet.setColors(PieChartDataProvider.PieColor);
        // 슬라이스 테두리 설정
        dataSet.setHighlightEnabled(true);

        //
        PieData pieData = new PieData((dataSet));
        // 퍼센트 글자 크기
        pieData.setValueTextSize(12f);
        pieChart.setData(pieData);

        pieChart.invalidate();


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


            Log.i("MyGestureListener", "onScroll distanceX : " + Float.toString((e2.getX()-e1.getX())));
            Log.i("MyGestureListener", "onScroll distanceX : " + distanceX);

            Log.i("MyGestureListener", "onScroll distanceY : " + distanceY);

            float x = e2.getX()-e1.getX();
            float y = e2.getY()-e1.getY();


            // 절댓값을 비교하여 좌우 스크롤 여부 확인
            if (Math.abs(x) > Math.abs(y)) {

                if (result) {

                    result = false;
                    if (기간 != PeriodManager.기간) {
                        // distanceX 값이 양수면 오른쪽으로, 음수면 왼쪽으로 스크롤
                        if (distanceX > 0) {
                            Date 변경날짜 = null;
                            if (기간 == PeriodManager.주간) {

                                Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                                변경날짜 = Date.getDate(calendar);
                            } else if (기간 == PeriodManager.월간) {
                                Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(Calendar.MONTH, 1);
                                변경날짜 = Date.getDate(calendar);
                            } else if (기간 == PeriodManager.연간) {
                                Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(Calendar.YEAR, 1);
                                변경날짜 = Date.getDate(calendar);
                            }

                            날짜변경(변경날짜);

                        } else if (distanceX < 0) {
                            Log.i(TAG, "왼쪽으로");
                            Date 변경날짜 = null;
                            if (기간 == PeriodManager.주간) {

                                Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                                변경날짜 = Date.getDate(calendar);
                            } else if (기간 == PeriodManager.월간) {
                                Calendar calendar = Date.getCalendar(기준이되는날짜);
                                calendar.add(Calendar.MONTH, -1);
                                변경날짜 = Date.getDate(calendar);
                            } else if (기간 == PeriodManager.연간) {
                                Calendar calendar = Date.getCalendar(기준이되는날짜);
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
}

//    //
//    PieChart pieChart = findViewById(R.id.piechart);
//
////        pieChart.setMinAngleForSlices();
////        IMarker marker = new MarkerImage(PieChartActivity.this,R.drawable.my_selector2);

//    CustomMarkerView marker = new CustomMarkerView(PieChartActivity.this,R.layout.marker_view);
//        pieChart.setMarker(marker);
//
//                // 백분율 값 설정.
//                pieChart.setUsePercentValues(true);
//
//                // 설명 미사용으로 설정.
//                pieChart.getDescription().setEnabled(false);
//
//                // 여백 설정.
//                pieChart.setExtraOffsets(20,20,20,20);
//
//                // 드래그 감속 설정.
//                // 드래그 효과를 더 부드럽게 만들어주는
//                pieChart.setDragDecelerationFrictionCoef(0.95f);
//
//                // 가운데 구멍을 그릴지 여부.
//                pieChart.setDrawHoleEnabled(false);
//
//
//                // 파이차트에 보여질 데이터
//                ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
//
////       for(int i=0;i<3;i++){
//        yValues.add(new PieEntry(15,"한식"));
//        yValues.add(new PieEntry(25,"중식"));
//        yValues.add(new PieEntry(35,"일식"));
//        yValues.add(new PieEntry(25,"양식"));
//        yValues.add(new PieEntry(15,"동남아"));
//        yValues.add(new PieEntry(15,"기타"));
//
////       }
//        // Easing.EaseInOutQuad : 처음,나중에는 느리게, 중간에는 빠르게 애니메이션
//        pieChart.animateY(1000, Easing.EaseInOutQuad); //애니메이션
//        pieChart.animateX(1000, Easing.EaseInOutQuad);
//
//
//        PieDataSet dataSet = new PieDataSet(yValues,"음식 종류");
//        // 파이 간격 설정.
//        dataSet.setSliceSpace(4f);
//
//        // 클릭시 간격 움직이는 정도
//        dataSet.setSelectionShift(15f);
//
//
//        // label 표시되는 값의 위치를 바깥에 표시하도록
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//
//        // 백분율 표시되는 값의 위치를 바깥에 표시하도록
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//
////        dataSet.setXValuePosition(PieDataSet.ValuePosition.);
//
////        dataSet.setValueLineWidth(100);
////        dataSet.setAutomaticallyDisableSliceSpacing(true);
//
////
////        dataSet.setValueFormatter(new ValueFormatter() {
////            String[] strings = {};
////            @Override
////            public String getFormattedValue(float value) {
////                return super.getFormattedValue(value)+"%";
////            }
////        });
//
//        dataSet.setValueLinePart1Length(0.6f);
////        dataSet.setValueLinePart2Length(1f);
//
//
////        // 커스텀 렌더러 사용 예시
////        CustomPieChartRenderer renderer = new CustomPieChartRenderer(pieChart, pieChart.getAnimator(), pieChart.getViewPortHandler());
////        pieChart.setRenderer(renderer);
//
//
////        dataSet.setValueLinePart1OffsetPercentage(200);
//
//        dataSet.setValueLineVariableLength(true);
//
////        pieChart.highlightValue(10f,0);
//
//        // slice 색상이 Line의 색상으로 자동지정됨.
//        dataSet.setUsingSliceColorAsValueLineColor(true);
//
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//        // 슬라이스 테두리 설정
//        dataSet.setHighlightEnabled(true);
//
//
//
////        dataSet.setEntryLineWidth(2f); // 테두리 선 두께
////        dataSet.setEntryLineColor(Color.BLACK); // 테두리 선 색상
//
////        dataSet.setLine
////        dataSet.setDrawIcons(true);
//
//        PieData data = new PieData((dataSet));
//        // 퍼센트 글자 크기
//        data.setValueTextSize(12f);
//
////        data.isHighlightEnabled()
//
////        pieChart.setHighlightPerTapEnabled(true);
//
////        pieChart.setCenterTextOffset(0, -200);
//
////        data.setValueTextColor(Color.BLACK);
//
////        data.setValueTextColor(Color.YELLOW);
//
//        pieChart.setRotationEnabled(false);
//        //pieChart.invalidate(); // 회전 및 터치 효과 사라짐
//        //pieChart.setTouchEnabled(false);
//
////        pieChart.setTransparentCircleRadius(45);
//
//        pieChart.setEntryLabelTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//        pieChart.setEntryLabelColor(Color.BLACK);
//
//        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
//@Override
//public void onValueSelected(Entry e, Highlight h) {
//        Toast.makeText(PieChartActivity.this, "click", Toast.LENGTH_SHORT).show();
//
//
//        pieChart.setEntryLabelTypeface(Typeface.SANS_SERIF);
//        e.setX(100);
////                dataSet.setSliceSpace(0);
//
//        pieChart.setEntryLabelColor(Color.RED);
//
//        }
//
//@Override
//public void onNothingSelected() {
//
//        }
//        });
//
////        pieChart.setOutlineAmbientShadowColor(Color.RED);
//        pieChart.setData(data);
//
//        Legend l = pieChart.getLegend();
////        ((Legend) l).setDrawInside(false);
//        l.setEnabled(false);
////        dataSet.setValueLinePart1OffsetPercentage(10); //starting of the line from center of the chart offset

