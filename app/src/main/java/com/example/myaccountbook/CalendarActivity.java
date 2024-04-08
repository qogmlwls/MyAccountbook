package com.example.myaccountbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.graphics.Canvas;


import com.example.myaccountbook.adapter.CalendarAdapter;
import com.example.myaccountbook.adapter.SelectRoutineAdapter;
import com.example.myaccountbook.data.Date;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import android.text.style.LineBackgroundSpan;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {


    final int REQUEST_ROUTINE = 100;
    private MaterialCalendarView calendarView;

    TextView 총수입, 총지출, 합계, 일총수입, 일총지출, 일_년월, 일_일,일_요일, 데이터가없습니다, 반복합계;
    TextView 반복날짜들;

    Calendar 달력;

    RoutineManager routineManager;

    DecimalFormat myFormatter = new DecimalFormat("###,###");

    List<accountBook> 가계부기록;

    TableRow row;
    MyApplication application;
    String 금액표시(long 금액){
        return myFormatter.format(금액);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarView = findViewById(R.id.calendarview);

        반복날짜들 = findViewById(R.id.textView13);
        row = findViewById(R.id.tablerow);

        총수입 = findViewById(R.id.textView6);
        총지출 = findViewById(R.id.textView7);
        합계 = findViewById(R.id.textView8);

        일총수입 = findViewById(R.id.textView18);
        일총지출 = findViewById(R.id.textView19);
        일_년월 = findViewById(R.id.textView17);
        일_일 = findViewById(R.id.textView15);
        일_요일 = findViewById(R.id.textView16);

        데이터가없습니다 = findViewById(R.id.textView35);
        반복합계 = findViewById(R.id.textView14);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
//        toolbar.setBack
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        application = (MyApplication)getApplication();
        routineManager = new RoutineManager();
        routineManager.getData(application.readDb,application);

        Date date = (Date) getIntent().getSerializableExtra("날짜");

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(date.getYear(),date.getMonth()-1,date.getDay(),0,0);

        반복합계.setText(금액표시(routineManager.총금액(calendar.get(java.util.Calendar.YEAR),calendar.get(java.util.Calendar.MONTH)+1))+"원");
        반복날짜들.setText(routineManager.날짜(calendar.get(java.util.Calendar.MONTH)+1));
        if(반복날짜들.getText().toString().length() == 0) {
            row.setVisibility(View.GONE);
        }
        else{
            row.setVisibility(View.VISIBLE);
        }


        달력 = new Calendar(application);
        달력.setMonth(application.readDb,calendar.get(java.util.Calendar.YEAR),calendar.get(java.util.Calendar.MONTH)+1);

        가계부기록 = 달력.내역기록;

        long 월총수입 = 달력.월총수입가져오기();
        long 월총지출 = 달력.월총지출가져오기();

        총수입.setText(금액표시(월총수입));
        총지출.setText(금액표시(월총지출));
        합계.setText(금액표시(월총수입-월총지출));
        
        Drawable drawable;

        drawable = getResources().getDrawable(R.drawable.my_selector);

//        calendarView.addDecorators(
//        )
//        ;
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//
//
//        calendarView.setTileSize(width / 7);  // will make cal
//


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                하단변경(date);
                widget.invalidateDecorators();
//                adapter.notifyDataSetChanged();
//                그 외 다른 것들
//                일_년월 = findViewById(R.id.textView17);
//                일_일 = findViewById(R.id.textView15);
//                일_요일 = findViewById(R.id.textView16);

            }
        });

        calendarView.setOnTitleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarDay date = calendarView.getSelectedDate();


                DatePickerDialog dialog = new DatePickerDialog(CalendarActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
                        calendar1.set(year, month, dayOfMonth);
                        calendarView.setCurrentDate(calendar1.getTime());
                        calendarView.setSelectedDate(calendar1.getTime());


                        하단변경(calendarView.getSelectedDate());


                        calendarView.invalidateDecorators();


//                        calendarView.set
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.set(year, month, dayOfMonth);
//                        long timeInMillis = calendar.getTimeInMillis();
//                        calendarView.setDate(timeInMillis);


                    }
                },date.getYear(),date.getMonth(),date.getDay());

                dialog.setTitle(String.format("%04d", date.getYear())+"년 "+String.format("%02d", date.getMonth())+"월");
                dialog.getDatePicker().setCalendarViewShown(false);

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();

            }
        });


        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                // 계산.
//                select * from accountbook where date>'2024-01-05 00:00' AND date < '2024-01-12 05:22'
//                select * from accountbook where date>'2024-01-05 00:00'
//                select * from accountbook where date>'2024-01-05 00:00' AND date < '2024-01-12 05:22' AND routine =0

//                calendarView.removeDecorators();


                달력.setMonth(application.readDb, date.getCalendar().get(java.util.Calendar.YEAR), date.getCalendar().get(java.util.Calendar.MONTH)+1);
                가계부기록 = 달력.내역기록;


                long 월총수입 = 달력.월총수입가져오기();
                long 월총지출 = 달력.월총지출가져오기();

                총수입.setText(금액표시(월총수입));
                총지출.setText(금액표시(월총지출));
                합계.setText(금액표시(월총수입-월총지출));

//                calendarView.addDecorators(달력.getDecorators());

                반복합계.setText(금액표시(routineManager.총금액(date.getCalendar().get(java.util.Calendar.YEAR),date.getCalendar().get(java.util.Calendar.MONTH)+1))+"원");
                반복날짜들.setText(routineManager.날짜(date.getCalendar().get(java.util.Calendar.MONTH)+1));

//                row.setVisibility(View.VISIBLE);

                if(반복날짜들.getText().toString().length() == 0) {
                    row.setVisibility(View.GONE);
                }
                else{
                    row.setVisibility(View.VISIBLE);
                }

                widget.invalidateDecorators();

//                calendarView.addDecorator(new DayViewDecorator() {
//                    @Override
//                    public boolean shouldDecorate(CalendarDay day) {
//                        return calendarView.getSelectedDate().getDay() == day.getDay();
//
//                    }
//
//                    @Override
//                    public void decorate(DayViewFacade view) {
//                        Drawable drawable;
//                        drawable = application.getResources().getDrawable(R.drawable.transfer_click);
//                        view.setSelectionDrawable(drawable);
//
//                        view.setSelectionDrawable(null);
//                    }
//                });

            }
        });


//        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
//                widget.invalidateDecorators();
//            }
//        });
//        calendarView.setClick
//

        calendarView.addDecorators(달력.getDecorators());


        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {

                return day.getDay() == calendarView.getSelectedDate().getDay() && day.getMonth() == calendarView.getSelectedDate().getMonth() && day.getYear() == calendarView.getSelectedDate().getYear();

            }

            @Override
            public void decorate(DayViewFacade view) {

                // 날짜에 따른 장식 작업
                view.addSpan(

                        new LineBackgroundSpan() {
                            @Override
                            public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
                                paint.setColor(Color.BLACK);
                            }
                        }
                );


                Drawable drawable = application.getResources().getDrawable(R.drawable.transfer_click);
                view.setSelectionDrawable(drawable);


//
//                drawable = application.getResources().getDrawable(R.drawable.non);
//                view.setSelectionDrawable(drawable);
            }
        });
//        calendarView.addDecorator(new DayViewDecorator() {
//            @Override
//            public boolean shouldDecorate(CalendarDay day) {
//                return calendarView.getSelectedDate().getDay() == day.getDay();
//
//            }
//
//            @Override
//            public void decorate(DayViewFacade view) {
//                Drawable drawable;
////                drawable = application.getResources().getDrawable(R.drawable.transfer_click);
////                view.setBackgroundDrawable(drawable);
//////                view.setSelectionDrawable(null);
//                drawable = application.getResources().getDrawable(R.drawable.insetclicl);
//
////                view.setBackgroundDrawable(drawable);
//
//                view.setBackgroundDrawable(drawable);
//                view.setSelectionDrawable(getDrawable(R.drawable.click));
//            }
//        });


        calendarView.addDecorator(달력.getDecorator(calendar.get(java.util.Calendar.YEAR),calendar.get(java.util.Calendar.MONTH)+1,calendar.get(java.util.Calendar.DAY_OF_MONTH)));



        TableRow 반복관리 = findViewById(R.id.tablerow);
        반복관리.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, RoutineActivity.class);

//                intent.putExtra("year",calendarView.getCurrentDate().getYear());
//                intent.putExtra("month",calendarView.getCurrentDate().getMonth()+1);

                startActivityForResult(intent,REQUEST_ROUTINE);
            }
        });

        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        calendarView.setSelectedDate(calendar1.getTime());


        RecyclerView 내역보기 = findViewById(R.id.recyclerView);
        내역보기.setLayoutManager(new LinearLayoutManager(this));
        list = 달력.기록들();
//        accountBook 내역 = new accountBook();
//        list.add(내역);
//        list.add(내역);
//        list.add(내역);
//        list.add(내역);
        if(list.size() == 0)
            데이터가없습니다.setVisibility(View.GONE);
        else{
            데이터가없습니다.setVisibility(View.VISIBLE);

        }
        adapter = new CalendarAdapter(list,this);
        내역보기.setAdapter(adapter);

        하단변경(calendarView.getSelectedDate());


    }

    CalendarAdapter adapter;
    List<accountBook> list;
    final int REQUEST_EDIT = 101;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK && data != null) {

            String state = data.getStringExtra("key");
//            int pk = data.getIntExtra("pk",-1);
            if(state.equals("value")){

//                calendarView.removeDecorators();

                CalendarDay date = calendarView.getSelectedDate();
                달력.setMonth(application.readDb, date.getCalendar().get(java.util.Calendar.YEAR), date.getCalendar().get(java.util.Calendar.MONTH)+1);
                하단변경(date);

                 date = calendarView.getCurrentDate();
//                CalendarDay date = calendarView.getSelectedDate();

                달력.setMonth(application.readDb, calendarView.getCurrentDate().getCalendar().get(java.util.Calendar.YEAR), date.getCalendar().get(java.util.Calendar.MONTH)+1);
                가계부기록 = 달력.내역기록;


                long 월총수입 = 달력.월총수입가져오기();
                long 월총지출 = 달력.월총지출가져오기();

                총수입.setText(금액표시(월총수입));
                총지출.setText(금액표시(월총지출));
                합계.setText(금액표시(월총수입-월총지출));

//                calendarView.addDecorators(달력.getDecorators());

                반복합계.setText(금액표시(routineManager.총금액(calendarView.getCurrentDate().getCalendar().get(java.util.Calendar.YEAR),date.getCalendar().get(java.util.Calendar.MONTH)+1))+"원");
                반복날짜들.setText(routineManager.날짜(date.getCalendar().get(java.util.Calendar.MONTH)+1));

//                row.setVisibility(View.VISIBLE);

                if(반복날짜들.getText().toString().length() == 0) {
                    row.setVisibility(View.GONE);
                }
                else{
                    row.setVisibility(View.VISIBLE);
                }



            }
            calendarView.invalidateDecorators();

        }
        else if(requestCode == REQUEST_ROUTINE && resultCode == RESULT_OK && data != null){

            Log.i("onActivityResult ","REQUEST_ROUTINE");
            java.util.Calendar calendar = calendarView.getCurrentDate().getCalendar();

            routineManager.getData(application.readDb,application);
            반복합계.setText(금액표시(routineManager.총금액(calendar.get(java.util.Calendar.YEAR),calendar.get(java.util.Calendar.MONTH)+1))+"원");
            반복날짜들.setText(routineManager.날짜(calendar.get(java.util.Calendar.MONTH)+1));
            if(반복날짜들.getText().toString().length() == 0) {
                row.setVisibility(View.GONE);
            }
            else{
                row.setVisibility(View.VISIBLE);
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


    void 하단변경(CalendarDay date){

        int 클릭일 = date.getDay();
        list = 달력.가계부내역가져오기(클릭일);

        if(list.size() == 0){
            데이터가없습니다.setVisibility(View.VISIBLE);
        }
        else{
            데이터가없습니다.setVisibility(View.GONE);
        }

        일_년월.setText(String.format("%04d", date.getYear())+ "."+String.format("%02d", date.getMonth()+1));
        일_일.setText(String.format("%02d", 클릭일));

        SimpleDateFormat 년월일 = new SimpleDateFormat("E", Locale.KOREA);
        일_요일.setText(년월일.format(date.getDate())+"요일");
        adapter.setList(list);


        long 클릭일총수입 = 달력.일총수입가져오기(클릭일);
        long 클릭일총지출 = 달력.일총지출가져오기(클릭일);

        일총수입.setText(금액표시(클릭일총수입)+"원");
        일총지출.setText(금액표시(클릭일총지출)+"원");

    }


}