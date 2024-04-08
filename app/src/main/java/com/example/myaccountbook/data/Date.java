package com.example.myaccountbook.data;

import android.util.Log;

import com.example.myaccountbook.CommonActivity;
import com.example.myaccountbook.PeriodManager;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Date implements Serializable {

    private int year;
    private int month;

    private int day;
    private int hour;
    private int min;

    public void today(){

        print("today() 시작------------------------------------------");
        Calendar calendar = Calendar.getInstance();
        print("Calendar 인스턴스 가져오기",calendar.toString());
        setData(calendar);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long now = System.currentTimeMillis();
        java.util.Date date = new java.util.Date(now);
        String getTime = dateFormat1.format(date);
        print("현재 시간 :"+getTime);
        print("today 결과값 :");
        데이터로그찍기();

    }


    private void setData(Calendar calendar){

        print("setData() 시작------------------------------------------");
        print("매개변수 calendar",calendar.toString());

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        min = calendar.get(Calendar.MINUTE);

        print("변경 후 데이터 :");
        데이터로그찍기();

    }
    public void 이전달로이동(int 이동하는달횟수){

        print("이전달로이동(int 이동하는달횟수) 시작------------------------------------------");
        Calendar calendar = getCalendar();
        print("Calendar 인스턴스 가져오기",calendar.toString());
        calendar.add(Calendar.MONTH,-1*이동하는달횟수);
        setData(calendar);
        print("이전달로이동(int 이동하는달횟수) 결과 : ");
        데이터로그찍기();
    }
    public void 이전주로이동(int 이동하는달횟수){

        print("이전주로이동(int 이동하는달횟수) 시작------------------------------------------");
        Calendar calendar = getCalendar();
        print("Calendar 인스턴스 가져오기",calendar.toString());
        calendar.add(Calendar.WEEK_OF_YEAR,-1*이동하는달횟수);
        setData(calendar);
        print("이전주로이동(int 이동하는달횟수) 결과 : ");
        데이터로그찍기();
    }
    public void 이전주로이동(){

        print("이전주로이동() 시작------------------------------------------");
        이전주로이동(1);
        print("이전주로이동() 결과 : ");
        데이터로그찍기();
    }

    public java.util.Date getDate(){
        데이터로그찍기();
        Calendar calendar = getCalendar(this);
        return calendar.getTime();

    }

    private void 데이터로그찍기(){

        print("year",Integer.toString(year));
        print("month",Integer.toString(month));
        print("day",Integer.toString(day));
        print("hour",Integer.toString(hour));
        print("min",Integer.toString(min));

    }

    private Calendar getCalendar(){
        print("getCalendar() 시작------------------------------------------");

        Calendar calendar = Calendar.getInstance();
        print("Calendar 인스턴스 가져오기",calendar.toString());
        print("calendar에 설정할 데이터");
        데이터로그찍기();
        calendar.set(year,month-1,day,hour, min);
        return calendar;

    }

    public void 이전달로이동(){

        print("이전달로이동() 시작------------------------------------------");
        이전달로이동(1);
        print("이전달로이동() 결과 : ");
        데이터로그찍기();
    }
    
    public void 다음달로이동(){

        print("다음달로이동() 시작------------------------------------------");
        이전달로이동(-1);
        print("다음달로이동() 결과 : ");
        데이터로그찍기();
    }

    public Date clone(Date date1){

        Date date = new Date();

        date.year = date1.year;
        date.month = date1.month;
        date.day = date1.day;
        date.hour = date1.hour;
        date.min = date1.min;

        return date;
    }


    public static Date 다음해날짜(Date 날짜){

        Calendar calendar = Calendar.getInstance();
        calendar.set(날짜.year,날짜.month-1,날짜.day,날짜.hour, 날짜.min);
        calendar.add(Calendar.YEAR,1);
//        calendar.roll(Calendar.MONTH,-1);

        Date date = new Date();
//        date.setData(calendar);

        date.year = calendar.get(Calendar.YEAR);
        date.month = calendar.get(Calendar.MONTH)+1;
        date.day = calendar.get(Calendar.DAY_OF_MONTH);
        date.hour = calendar.get(Calendar.HOUR);
        date.min = calendar.get(Calendar.MINUTE);

        return date;

    }


    public static Date 이전해날짜(Date 날짜){

        Calendar calendar = Calendar.getInstance();
        calendar.set(날짜.year,날짜.month-1,날짜.day,날짜.hour, 날짜.min);

//        calendar.add();
        calendar.add(Calendar.YEAR,-1);
//        calendar.roll(Calendar.MONTH,-1);

        Date date = new Date();
//        date.setData(calendar);

        date.year = calendar.get(Calendar.YEAR);
        date.month = calendar.get(Calendar.MONTH)+1;
        date.day = calendar.get(Calendar.DAY_OF_MONTH);
        date.hour = calendar.get(Calendar.HOUR);
        date.min = calendar.get(Calendar.MINUTE);

        return date;

    }


    public static int 말일(Date 날짜){

        Calendar calendar = Calendar.getInstance();
        calendar.set(날짜.year,날짜.month-1,날짜.day,날짜.hour, 날짜.min);

        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return day;

    }

    public static Date 다음주(Date 날짜){

        Calendar calendar = getCalendar(날짜);

        calendar.add(Calendar.WEEK_OF_YEAR,1);

        Date date = new Date();
//        date.setData(calendar);

        date.year = calendar.get(Calendar.YEAR);
        date.month = calendar.get(Calendar.MONTH)+1;
        date.day = calendar.get(Calendar.DAY_OF_MONTH);
        date.hour = calendar.get(Calendar.HOUR);
        date.min = calendar.get(Calendar.MINUTE);

        return date;
    }


    public static Date getDate(Calendar calendar){

        Date date = new Date();
//        date.setData(calendar);

        date.year = calendar.get(Calendar.YEAR);
        date.month = calendar.get(Calendar.MONTH)+1;
        date.day = calendar.get(Calendar.DAY_OF_MONTH);
        date.hour = calendar.get(Calendar.HOUR);
        date.min = calendar.get(Calendar.MINUTE);

        return date;

    }


    public static Calendar getCalendar(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.set(date.year,date.month-1,date.day,date.hour, date.min);

        return calendar;

    }



    public static String 주간날짜글자(Date date){


        Calendar calendar = getCalendar(date);

        // 토요일과 가장 가까운 이전 일요일을 찾으시오 - 시작날짜
        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_YEAR, -(dayOfWeekNumber-1) );
        Date date2 = new Date();
        date2.year = calendar.get(Calendar.YEAR);
        date2.month = calendar.get(Calendar.MONTH)+1;
        date2.day = calendar.get(Calendar.DAY_OF_MONTH);


        // 가장 가까운 다음 토요일을 찾으시오 - 끝날짜
        calendar.add(Calendar.DAY_OF_YEAR,6);
        Date date3 = new Date();
        date3.year = calendar.get(Calendar.YEAR);
        date3.month = calendar.get(Calendar.MONTH)+1;
        date3.day = calendar.get(Calendar.DAY_OF_MONTH);


        return 주간날짜(date2) + " ~ " + 주간날짜(date3);

    }

    static String 주간날짜(Date date){
        return Integer.toString(date.year%100)+"."+Integer.toString(date.month)+"."+Integer.toString(date.day);
    }

    public static String 날짜글자(Date 기준이되는날짜, int 기간){
        if(기간 == PeriodManager.주간){
           return 주간날짜글자(기준이되는날짜);
        }

        Calendar calendar = getCalendar(기준이되는날짜);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;

        if(기간 == PeriodManager.월간){
            return Integer.toString(year) + "년 "+Integer.toString(month)+"월";
        }
        else if(기간 == PeriodManager.연간){
            return Integer.toString(year) + "년";
        }
        else{
            return "";
        }
    }





    public static String 기간시작날짜글자(Date 기준이되는날짜, int 이전기간){

        if(이전기간 == PeriodManager.주간){

            Calendar calendar = getCalendar(기준이되는날짜);

            // 토요일과 가장 가까운 이전 일요일을 찾으시오 - 시작날짜
            int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_YEAR, -(dayOfWeekNumber-1) );
            Date date2 = new Date();
            date2.year = calendar.get(Calendar.YEAR);
            date2.month = calendar.get(Calendar.MONTH)+1;
            date2.day = calendar.get(Calendar.DAY_OF_MONTH);


            return 주간날짜(date2);

        }
        else if(이전기간 == PeriodManager.월간){

            return Integer.toString(기준이되는날짜.year%100) + "." +Integer.toString(기준이되는날짜.month)+".1";

        }
        else if(이전기간 == PeriodManager.연간){

            return Integer.toString(기준이되는날짜.year%100) + ".1.1";

        }
        else{
            return "";
        }

    }
    public static String 기간끝날짜글자(Date 기준이되는날짜, int 이전기간){

        Calendar calendar = getCalendar(기준이되는날짜);

        if(이전기간 == PeriodManager.주간){


            // 토요일과 가장 가까운 이전 일요일을 찾으시오 - 시작날짜
            int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_YEAR, -(dayOfWeekNumber-1) );
            // 가장 가까운 다음 토요일을 찾으시오 - 끝날짜
            calendar.add(Calendar.DAY_OF_YEAR,6);
            Date date3 = new Date();
            date3.year = calendar.get(Calendar.YEAR);
            date3.month = calendar.get(Calendar.MONTH)+1;
            date3.day = calendar.get(Calendar.DAY_OF_MONTH);



            return 주간날짜(date3);

        }
        else if(이전기간 == PeriodManager.월간){

            return Integer.toString(기준이되는날짜.year%100) + "." +Integer.toString(기준이되는날짜.month)+"."+Integer.toString(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        }
        else if(이전기간 == PeriodManager.연간){

            return Integer.toString(기준이되는날짜.year%100) + ".12."+Integer.toString(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        }
        else{
            return "";
        }

    }


    public static Date 기간시작날짜(Date 기준이되는날짜, int 이전기간){

        Date date2 = new Date();

        if(이전기간 == PeriodManager.주간){

            Calendar calendar = getCalendar(기준이되는날짜);

            // 토요일과 가장 가까운 이전 일요일을 찾으시오 - 시작날짜
            int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_YEAR, -(dayOfWeekNumber-1) );
            date2.year = calendar.get(Calendar.YEAR);
            date2.month = calendar.get(Calendar.MONTH)+1;
            date2.day = calendar.get(Calendar.DAY_OF_MONTH);




        }
        else if(이전기간 == PeriodManager.월간){

            date2.year = 기준이되는날짜.year;
            date2.month = 기준이되는날짜.month;
            date2.day = 1;

        }
        else if(이전기간 == PeriodManager.연간){

            date2.year = 기준이되는날짜.year;
            date2.month = 1;
            date2.day = 1;
        }
        else{
            return null;
        }
        return date2;
    }
    public static Date 기간끝날짜(Date 기준이되는날짜, int 이전기간){

        Calendar calendar = getCalendar(기준이되는날짜);
        Date date3 = new Date();

        if(이전기간 == PeriodManager.주간){

            // 토요일과 가장 가까운 이전 일요일을 찾으시오 - 시작날짜
            int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DAY_OF_YEAR, -(dayOfWeekNumber-1) );
            // 가장 가까운 다음 토요일을 찾으시오 - 끝날짜
            calendar.add(Calendar.DAY_OF_YEAR,6);
            date3.year = calendar.get(Calendar.YEAR);
            date3.month = calendar.get(Calendar.MONTH)+1;
            date3.day = calendar.get(Calendar.DAY_OF_MONTH);

        }
        else if(이전기간 == PeriodManager.월간){

            date3.year = 기준이되는날짜.year;
            date3.month = 기준이되는날짜.month;
            date3.day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        }
        else if(이전기간 == PeriodManager.연간){

            date3.year = 기준이되는날짜.year;
            date3.month = 12;
            date3.day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        }
        else{
            return null;
        }
        return date3;

    }

//                기간시작날짜.setText(Date.기간시작날짜글자(기준이되는날짜,기간, 이전기간));
//            기간끝날짜.setText(Date.기간끝날짜글자(기준이되는날짜,기간, 이전기간));


    public static boolean after(Date 기준이되는날짜, Date 비교날짜){


        Calendar calendar = getCalendar(기준이되는날짜);
        long 기준이되는날짜값 = calendar.getTimeInMillis();

        Calendar calendar1 = getCalendar(비교날짜);
        long 비교날짜값 = calendar1.getTimeInMillis();

        if(기준이되는날짜값 > 비교날짜값)
            return true;
        else
            return false;

    }

    public static java.util.Date getDate(int year, int month){

        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,1,0,0);

        return calendar.getTime();

    }

    public String 년월날짜(){
        return Integer.toString(year)+"년 "
                +Integer.toString(month)+"월";
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    boolean result = true;
    void print(String message){
        if(result)
        Log.i("Date class", message);
    }
    void print(String key,String value){
        if(result)
            Log.i("Date class", key +" : "+value);
    }
}
