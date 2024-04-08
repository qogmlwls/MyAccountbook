package com.example.myaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.text.style.LineBackgroundSpan;
import android.util.Log;

import androidx.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// 특정 달의 총수입, 총지출, 합계, 일마다의 총 수입, 지출, 합계 , 일마다의 기록목록들

public class Calendar {


    String TAG = "Calendar";

    List<accountBook> 내역기록;
    MyApplication application;
    int month;
    private String 변환2(int number){
        return String.format("%02d", number);
    }
    private String 변환4(int number){
        return String.format("%04d", number);
    }


    public Calendar(MyApplication application){

        내역기록 = new ArrayList<>();
        this.application = application;

        for(int i=1;i<32;i++){
            DayViewDecorator decorator = createDecorator(i);
            decorators.add(decorator);
        }

    }

    // 1~12 사이로
    public void setMonth(SQLiteDatabase database, com.example.myaccountbook.data.Date date) {

        setMonth(database,date.getYear(),date.getMonth());

    }


        // 1~12 사이로
    public void setMonth(SQLiteDatabase database, int year, int month){



        this.month = month;
        내역기록.clear();

        // 내역 가져오기.
//        select * from accountbook where date>'2024-01-05 00:00' AND date < '2024-01-12 05:22' AND routine =0

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year,month-1,1);
        int day = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);  //말일


        String sql = "select * from accountbook where date>= '"+변환4(year)+"-"+변환2(month)+"-01 00:00' AND date <= '"+변환4(year)+"-"+변환2(month)+"-"+변환2(  day)+" 23:59' AND routine =0 ORDER by date";

        Log.i(TAG,sql);
        Cursor cursor = database.rawQuery(sql,null);

        while(cursor.moveToNext()){

            accountBook 내역 = new accountBook();
            int pk = cursor.getInt(0);
            int type = cursor.getInt(1);
            int 자산pk = cursor.getInt(2);
            int 카테고리pk = cursor.getInt(3);
            int 금액 = cursor.getInt(4);
            
            
            String 날짜 = cursor.getString(5);
            int 입금자산pk = cursor.getInt(6);
            int 출금자산pk = cursor.getInt(7);
            int routine = cursor.getInt(8);
            
            String content = cursor.getString(9);
            String memo = cursor.getString(10);
            String image1 = cursor.getString(11);
            String image2 = cursor.getString(12);
            String image3 = cursor.getString(13);

            내역.pk = pk;
            내역.type = type;
            내역.금액 = 금액;
            내역.금액원 = 금액표시(내역.금액);

            내역.날짜 = 날짜;
            내역.date = 내역.parseISODate(내역.날짜);


            내역.입금자산식별자 = 입금자산pk;
            내역.출금자산식별자 = 출금자산pk;
            내역.자산식별자 = 자산pk;
            내역.카테고리식별자 = 카테고리pk;
            내역.내용 = content;

            내역.메모 = memo;
            내역.이미지1 = image1;
            내역.이미지2 = image2;
            내역.이미지3 = image3;


            내역기록.add(application.치환(내역));

        }

        cursor.close();

    }



    public long 월총수입가져오기(){

        long 총수입 = 0;

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);
            if(내역.type == accountBook.수입){
                총수입 += 내역.금액;
            }
        }
        return 총수입;

    }

    public long 일총수입가져오기(int day){

        long 총수입 = 0;

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);


            Date 날짜 = 내역.parseISODate(내역.날짜);
            java.util.Calendar calendar = java.util.Calendar.getInstance();

            calendar.setTime(날짜);
            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            if(day == 일)
                if(내역.type == accountBook.수입){
                    총수입 += 내역.금액;
                }


        }
        return 총수입;

    }

    public long 월총지출가져오기(){

        long 총지출 = 0;

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);
            if(내역.type == accountBook.고정지출 || 내역.type == accountBook.변동지출){
                총지출 += 내역.금액;
            }
        }
        return 총지출;

    }

    public long 일총지출가져오기(int day){

        long 총지출 = 0;

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);


            Date 날짜 = 내역.parseISODate(내역.날짜);
            java.util.Calendar calendar = java.util.Calendar.getInstance();

            calendar.setTime(날짜);
            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            if(day == 일)
                if(내역.type == accountBook.고정지출 || 내역.type == accountBook.변동지출 ){
                    총지출 += 내역.금액;
                }


        }
        return 총지출;

    }


    public List<accountBook> 기록들(){



        List<accountBook> 내역기록 = new ArrayList<>();



        return 내역기록;


    }
    public List<accountBook> 가계부내역가져오기(int day){


        List<accountBook> 기록 = new ArrayList<>();
        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);

            Date 날짜 = 내역.parseISODate(내역.날짜);
            java.util.Calendar calendar = java.util.Calendar.getInstance();

            calendar.setTime(날짜);
            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            if(day == 일)
                기록.add(내역);


        }
        return 기록;

    }
    public boolean 해당일에가계부기록이있나요(int day){

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);

            Date 날짜 = 내역.parseISODate(내역.날짜);
            java.util.Calendar calendar = java.util.Calendar.getInstance();

            calendar.setTime(날짜);
            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            if(day == 일)
                return true;


        }
        return false;

    }
    private List<DayViewDecorator> decorators = new ArrayList<>();

    public List<DayViewDecorator> getDecorators(){

//        decorators.clear();
//
////        java.util.Calendar calendar = java.util.Calendar.getInstance();
////        calendar.set(year,month-1,1);
////        int day = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);  //말일
//
//
//        for(int i=1;i<32;i++){
//
//            DayViewDecorator decorator = createDecorator(i);
//            decorators.add(decorator);
//        }

        return decorators;
    }
    DecimalFormat myFormatter = new DecimalFormat("###,###");

    String 금액표시(long 금액){
        return myFormatter.format(금액);
    }
    DayViewDecorator createDecorator(final int dayNumber) {
        return new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
//                day.getMonth()
                return day.getDay() == dayNumber && 해당일에가계부기록이있나요(dayNumber);
            }

            @Override
            public void decorate(DayViewFacade view) {
                // 날짜에 따른 장식 작업


                view.addSpan(

                    new LineBackgroundSpan() {
                        @Override
                        public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {

                            Paint paint1 = new Paint();
                            paint1.setColor(Color.BLUE);
                            paint1.setTextSize(20); // 텍스트 크기 설정
//
//                            paint.setColor(Color.BLUE);
//                            paint.setTextSize(20); // 텍스트 크기 설정

                            long 일총수입 = 일총수입가져오기(dayNumber);

                            if(일총수입 == 0){
                                canvas.drawText("", (float)((left + right) / 4), (float) (bottom + 11), paint1);
                            }
                            else{
                                canvas.drawText(금액표시(일총수입), (float)((left + right) / 4), (float) (bottom + 11), paint1);
                            }

                            Paint paint2 = new Paint();
                            paint2.setColor(Color.RED);
                            paint2.setTextSize(20); // 텍스트 크기 설정

                            long 일총지출 = 일총지출가져오기(dayNumber);

                            if(일총지출 == 0){
                                canvas.drawText("", (float)((left + right) / 4), (float) (bottom + 26), paint2);
                            }
                            else{
                                canvas.drawText(금액표시(일총지출), (float)((left + right) / 4), (float) (bottom + 26), paint2);
                            }

                        }
                    }
                );


            }
        };
    }

    public DayViewDecorator getDecorator(int year,int month, int dayNumber){

        return new DayViewDecorator() {

            @Override
            public boolean shouldDecorate(CalendarDay day) {

                return day.getDay() == dayNumber && day.getMonth() == (month-1) && day.getYear() == year;

            }

            @Override
            public void decorate(DayViewFacade view) {


                Drawable drawable = application.getResources().getDrawable(R.drawable.insetclicl);
                view.setBackgroundDrawable(drawable);

                drawable = application.getResources().getDrawable(R.drawable.non);
                view.setSelectionDrawable(drawable);

                // 날짜에 따른 장식 작업
                view.addSpan(

                        new LineBackgroundSpan() {
                            @Override
                            public void drawBackground(@NonNull Canvas canvas, @NonNull Paint paint, int left, int right, int top, int baseline, int bottom, @NonNull CharSequence text, int start, int end, int lineNumber) {
                                paint.setColor(Color.WHITE);

//
//
//                                Paint paint1 = new Paint();
//                                paint1.setTextSize(20); // 텍스트 크기 설정
////
////                            paint.setColor(Color.BLUE);
////                            paint.setTextSize(20); // 텍스트 크기 설정
//
//                                long 일총수입 = 일총수입가져오기(dayNumber);
//
//                                if(일총수입 == 0){
//                                    canvas.drawText("", (float)((left + right) / 4), (float) (bottom + 11), paint1);
//                                }
//                                else{
//                                    canvas.drawText(금액표시(일총수입), (float)((left + right) / 4), (float) (bottom + 11), paint1);
//                                }
//
//                                Paint paint2 = new Paint();
//                                paint2.setColor(Color.RED);
//                                paint2.setTextSize(20); // 텍스트 크기 설정
//
//                                long 일총지출 = 일총지출가져오기(dayNumber);
//
//                                if(일총지출 == 0){
//                                    canvas.drawText("", (float)((left + right) / 4), (float) (bottom + 26), paint2);
//                                }
//                                else{
//                                    canvas.drawText(금액표시(일총지출), (float)((left + right) / 4), (float) (bottom + 26), paint2);
//                                }

                            }
                        }
                );


            }
        };
    }


}
