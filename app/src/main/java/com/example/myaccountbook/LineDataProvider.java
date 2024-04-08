package com.example.myaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myaccountbook.data.Amount;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.QueryManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LineDataProvider {
    String TAG = "LineDataProvider";


    //형식


    class DATA{
        Amount 금액;
        String 날짜;

        public DATA(){
            금액 = new Amount();

        }
    }


    int xsize(int 주기){
        if(주기 != PeriodManager.연간){
            return 8;
        }
        else{
            return 12;
        }
    }

    Date 이전날짜로이동(Date 날짜, int 주기){

        Date date = 날짜.clone(날짜);
        if(주기 != PeriodManager.주간){
            // 이전 월로 이동
            date.이전달로이동(1);
        }
        else{
            // 이전 주로 이동
            date.이전주로이동(1);
        }
        return date;

    }

    public List<DATA> getData(Date 기준이되는날짜, int 주기, int category_pk, int type, SQLiteDatabase readDb){

        List<DATA> list = new ArrayList<>();
        int 반복횟수 = xsize(주기);

        if(주기 == PeriodManager.연간){
            기준이되는날짜.setMonth(12);
        }

        for(int i=0;i<반복횟수;i++){

            String query;
            if(주기 == PeriodManager.주간){
                query = QueryManager.PieChart.GetWeeksAmountData(기준이되는날짜,type,category_pk);
            }
            else{
                query = QueryManager.PieChart.GetMonthsAmountData(기준이되는날짜,type,category_pk);
            }
            Log.i("query",query);

            Cursor cursor = readDb.rawQuery(query,null);
            DATA totalData = getDATA(cursor,날짜(기준이되는날짜,주기));

            list.add(0,totalData);
            기준이되는날짜 = 이전날짜로이동(기준이되는날짜,주기);

        }

        return list;

    }

    String 날짜(Date 기준이되는날짜, int 주기){

        if(주기 == PeriodManager.주간){

            // 이전 월로 이동
            Calendar calendar = Date.getCalendar(기준이되는날짜);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return Integer.toString(month)+"/"+Integer.toString(day);

        }
        else{

            // 이전 주로 이동
            Calendar calendar = Date.getCalendar(기준이되는날짜);
            int month = calendar.get(Calendar.MONTH)+1;

            return Integer.toString(month)+"월";

        }

    }
    
    DATA getDATA(Cursor cursor, String 날짜) {

        DATA totalData = new DATA();

        while(cursor.moveToNext()) {

//            int m_type = cursor.getInt(1);
//            int 금액 = cursor.getInt(4);
            int 금액 = cursor.getInt(1);
            Log.i("금액 : ",Integer.toString(금액));
            totalData.금액.amount += 금액;
//            if( (type == accountBook.지출 && (m_type == accountBook.고정지출 || m_type == accountBook.변동지출))
//                    || (type == accountBook.수입 && m_type == accountBook.수입) ) {
//
//                총합계.amount += 금액;
//            }

        }

        totalData.날짜 = 날짜;
        return totalData;

    }




}
