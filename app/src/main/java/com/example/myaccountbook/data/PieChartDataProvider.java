package com.example.myaccountbook.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.util.Log;

import com.example.myaccountbook.FeedReaderContract;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.PeriodManager;
import com.example.myaccountbook.accountBook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class PieChartDataProvider {


    SQLiteDatabase readDb;
    public static int[] PieColor = new int[8];

    String TAG = "PieChartDataProvider";
    public PieChartDataProvider(SQLiteDatabase readDb){
        PieColor[0] = Color.RED;
        PieColor[1] = Color.argb(255, 255, 125, 0);
        PieColor[2] = Color.MAGENTA;
        PieColor[3] = Color.argb(255, 0, 150, 136);
        PieColor[4] = Color.CYAN;
        PieColor[5] = Color.DKGRAY;
        PieColor[6] = Color.BLUE;
        PieColor[7] = Color.GRAY;

        this.readDb = readDb;
    }

    public class TotalData{

        public Amount 총수입, 총지출;
        public List<Data> list;
        public TotalData(){
            총수입 = new Amount();
            총지출 = new Amount();
        }
    }

    public class Data{

//        public int 금액;
        public Amount 금액;
        public int 카테고리식별자;
        public String 카테고리명;
        Data(){
            금액 = new Amount();
        }
//        public int 색상;

    }
//    private String 변환2(int number){
//        return String.format("%02d", number);
//    }
//    private String 변환4(int number){
//        return String.format("%04d", number);
//    }

//    String createQuery(Date 시작날짜, Date 끝날짜,int 주기,int category_pk){
//
//        String query = null;
//
//        if(주기 == PeriodManager.기간) {
//            query = "select * from accountbook where " + FeedReaderContract.가계부내역.COLUMN_CATEGORY_PK+"="+Integer.toString(category_pk)+
//                    " AND date>= '" + 변환4(시작날짜.getYear()) + "-" + 변환2(시작날짜.getMonth()) + "-" + 변환2(시작날짜.getDay()) + " 00:00' AND date <= '" + 변환4(끝날짜.getYear()) + "-" + 변환2(끝날짜.getMonth()) + "-" + 변환2(끝날짜.getDay()) + " 23:59' AND routine =0 ORDER by date";
//        }
//
//        Log.i(TAG,"query : " + query);
//
//        return query;
//    }

//    String createQuery(Date 날짜, int 주기,int category_pk){
//
//        String query = null;
//
//
//        if(주기 == PeriodManager.주간){
//
//            Date 다음주 = Date.다음주(날짜);
//            query = "select * from accountbook where " + FeedReaderContract.가계부내역.COLUMN_CATEGORY_PK+"="+Integer.toString(category_pk)+
//                    " AND date>= '"+변환4(날짜.getYear())+"-"+변환2(날짜.getMonth())+"-"+변환2(날짜.getDay())+" 00:00' AND date <= '"+변환4(다음주.getYear())+"-"+변환2(다음주.getMonth())+"-"+변환2(다음주.getDay())+" 23:59' AND routine = 0 ORDER by date";
//
//
//        }
//        else if(주기 == PeriodManager.월간){
//            query = "select * from accountbook where " + FeedReaderContract.가계부내역.COLUMN_CATEGORY_PK+"="+Integer.toString(category_pk)+
//                    " AND date>= '"+변환4(날짜.getYear())+"-"+변환2(날짜.getMonth())+"-01 00:00' AND date <= '"+변환4(날짜.getYear())+"-"+변환2(날짜.getMonth())+"-"+변환2(Date.말일(날짜))+" 23:59' AND routine = 0 ORDER by date";
//
//        }
//        else if(주기 == PeriodManager.연간){
//            query = "select * from accountbook where " + FeedReaderContract.가계부내역.COLUMN_CATEGORY_PK+"="+Integer.toString(category_pk)+
//                    " AND date>= '"+변환4(날짜.getYear())+"-01-01 00:00' AND date <= '"+변환4(날짜.getYear())+"-12-31 23:59' AND routine = 0 ORDER by date";
//
//        }
//
//        Log.i(TAG,"query : " + query);
//        return query;
//
//    }
//

    public List<accountBook> getDetailData(Date 날짜, int 주기, int type, int category_pk,MyApplication application){

        String query;
        if(주기 == PeriodManager.주간){
            query = QueryManager.PieChart.GetWeekDetailData(날짜,type,category_pk);
        }
        else if(주기 == PeriodManager.월간){
            query = QueryManager.PieChart.GetMonthDetailData(날짜,type,category_pk);

        }
        else if(주기 == PeriodManager.연간){
            query = QueryManager.PieChart.GetYearDetailData(날짜,type,category_pk);

        }
        else{
            return null;
        }

        Log.i("detail query",query);
        Cursor cursor = readDb.rawQuery(query,null);

//        List<accountBook> list = get내역(cursor,application);
        List<accountBook> list = get내역(cursor,application);
        내역sort(list);

        return list;

    }
    public List<accountBook> getDetailData(Date 시작날짜,Date 끝날짜, int type, int category_pk,MyApplication application){

        String query =  QueryManager.PieChart.GetDaysDetailData(시작날짜,끝날짜,type,category_pk);
        Cursor cursor = readDb.rawQuery(query,null);

        List<accountBook> list = get내역(cursor,application);
        내역sort(list);

        return list;
    }

    List<accountBook> get내역(Cursor cursor,MyApplication application){

        List<accountBook> list = new ArrayList<>();

        while(cursor.moveToNext()) {

            accountBook 내역 = new accountBook();
            int pk = cursor.getInt(0);
            int m_type = cursor.getInt(1);
            int 자산pk = cursor.getInt(2);
            int 카테고리pk = cursor.getInt(3);
            int 금액 = cursor.getInt(4);


            String 내역날짜 = cursor.getString(5);
            int 입금자산pk = cursor.getInt(6);
            int 출금자산pk = cursor.getInt(7);
            int routine = cursor.getInt(8);

            String content = cursor.getString(9);
            String memo = cursor.getString(10);
            String image1 = cursor.getString(11);
            String image2 = cursor.getString(12);
            String image3 = cursor.getString(13);

            내역.pk = pk;
            내역.type = m_type;
            내역.금액 = 금액;
            내역.금액원 = Formatter.천단위구분(내역.금액);

            내역.날짜 = 내역날짜;
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

            list.add(application.치환(내역));

        }

        cursor.close();

        return list;
    }


    public TotalData getChartData(Date 시작날짜, Date 끝날짜, int type){

        String query = QueryManager.PieChart.GetDaysMainData(시작날짜,끝날짜);

        Cursor cursor = readDb.rawQuery(query,null);
        TotalData totalData = getData(cursor, type);
        sort(totalData.list);

        return totalData;
    }

    public TotalData getChartData(Date 날짜, int 주기, int type){

        String query;

        if(PeriodManager.월간 == 주기){

            query = QueryManager.PieChart.GetMonthMainData(날짜);
            Log.i("getChartData 월간 : ",query);

        }
        else if(PeriodManager.연간 == 주기){

            query = QueryManager.PieChart.GetYearMainData(날짜);
            Log.i("getChartData 연간 : ",query);

        }
        else if(PeriodManager.주간 == 주기){

            query = QueryManager.PieChart.GetWeekMainData(날짜);
            Log.i("getChartData 주간 : ",query);

        }
        else{
            return null;
        }

        Cursor cursor = readDb.rawQuery(query,null);
        TotalData totalData = getData(cursor, type);
        sort(totalData.list);

        return totalData;

    }

    TotalData getData(Cursor cursor, int type){

        TotalData totalData = new TotalData();
        HashMap<Integer,Data> hashMap = new HashMap<>();

        while(cursor.moveToNext()) {
            int total = cursor.getInt(0);

//            int total = cursor.getInt(1);
            String 카테고리명 = cursor.getString(1);
            int m_type = cursor.getInt(2);


            int 카테고리pk = cursor.getInt(3);
//            int 금액 = cursor.getInt(4);
            Log.i(TAG,"total : "+Integer.toString(total));
            Log.i(TAG,"카테고리명 : "+카테고리명);
            Log.i(TAG,"m_type : "+Integer.toString(m_type));
            Log.i(TAG,"카테고리pk : "+Integer.toString(카테고리pk));

            if( (type == accountBook.지출 && (m_type == accountBook.고정지출 || m_type == accountBook.변동지출))
                    || (type == accountBook.수입 && m_type == accountBook.수입) ) {

                if(hashMap.containsKey(카테고리pk)){

                    Data data = hashMap.get(카테고리pk);
                    data.금액.amount += total;

//                    hashMap.replace(카테고리pk, hashMap.get(카테고리pk)+total);
                }
                else{
                    Data data = new Data();
                    data.카테고리명 = 카테고리명;
                    data.카테고리식별자 = 카테고리pk;
                    data.금액.amount = total;
                    hashMap.put(카테고리pk, data);
                }

            }

            if(m_type == accountBook.고정지출 || m_type == accountBook.변동지출){

                Log.i(TAG,"지출 : "+Integer.toString(total));
                totalData.총지출.amount += total;

            }
            else if(m_type == accountBook.수입){

                Log.i(TAG,"수입 : "+Integer.toString(total));
                totalData.총수입.amount += total;

            }

        }


        Log.i(TAG,"총수입 : "+Integer.toString(totalData.총수입.amount));
        Log.i(TAG,"총지출 : "+Integer.toString(totalData.총지출.amount));


        List<Data> list = new ArrayList<>(hashMap.values());

        cursor.close();

        totalData.list = list;

        return totalData;
    }


    void sort(List<Data> list){

        list.sort(new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return Integer.compare(o2.금액.amount, o1.금액.amount);
            }
        });

    }

    void 내역sort(List<accountBook> list){

        list.sort(new Comparator<accountBook>() {
            @Override
            public int compare(accountBook o1, accountBook o2) {

                return (int)(o1.date.getTime() - o2.date.getTime());

            }
        });

    }

}
//    String createQuery(Date 날짜, int 주기){
//
//        String query = null;
//
//
//        if(주기 == PeriodManager.주간){
//
//            Date 다음주 = Date.다음주(날짜);
//            query = "select * from accountbook where date>= '"+변환4(날짜.getYear())+"-"+변환2(날짜.getMonth())+"-"+변환2(날짜.getDay())+" 00:00' AND date <= '"+변환4(다음주.getYear())+"-"+변환2(다음주.getMonth())+"-"+변환2(다음주.getDay())+" 23:59' AND routine = 0 ORDER by date";
//
//
//        }
//        else if(주기 == PeriodManager.월간){
//            query = "select * from accountbook where date>= '"+변환4(날짜.getYear())+"-"+변환2(날짜.getMonth())+"-01 00:00' AND date <= '"+변환4(날짜.getYear())+"-"+변환2(날짜.getMonth())+"-"+변환2(Date.말일(날짜))+" 23:59' AND routine = 0 ORDER by date";
//
//        }
//        else if(주기 == PeriodManager.연간){
//            query = "select * from accountbook where date>= '"+변환4(날짜.getYear())+"-01-01 00:00' AND date <= '"+변환4(날짜.getYear())+"-12-31 23:59' AND routine = 0 ORDER by date";
//
//        }
//
//        Log.i(TAG,"query : " + query);
//        return query;
//
//    }
//
//
//    TotalData getData(Cursor cursor, MyApplication application, int type){
//
//
//        TotalData totalData = new TotalData();
//
//        Amount 총수입, 총지출;
//
//        총수입 = new Amount();
//        총지출 = new Amount();
//
//        총수입.amount = 0;
//        총지출.amount = 0;
//
//        HashMap<Integer,Integer> hashMap = new HashMap<>();
//
//        while(cursor.moveToNext()) {
//
//            int m_type = cursor.getInt(1);
//            int 카테고리pk = cursor.getInt(3);
//            int 금액 = cursor.getInt(4);
//
//            if( (type == accountBook.지출 && (m_type == accountBook.고정지출 || m_type == accountBook.변동지출))
//                    || (type == accountBook.수입 && m_type == accountBook.수입) ) {
//
//                if(hashMap.containsKey(카테고리pk)){
//                    hashMap.replace(카테고리pk, hashMap.get(카테고리pk)+금액);
//                }
//                else{
//                    hashMap.put(카테고리pk, 금액);
//                }
//
//            }
//
//            if(m_type == accountBook.고정지출 || m_type == accountBook.변동지출){
//
//                Log.i(TAG,"지출 : "+Integer.toString(금액));
//                총지출.amount += 금액;
//
//            }
//            else if(m_type == accountBook.수입){
//
//                Log.i(TAG,"수입 : "+Integer.toString(금액));
//                총수입.amount += 금액;
//
//            }
//
//        }
//
//
//        Log.i(TAG,"총수입 : "+Integer.toString(총수입.amount));
//        Log.i(TAG,"총지출 : "+Integer.toString(총지출.amount));
//
//
//        //        hashMap.keySet();
//        //        Set<Integer> keys = hashMap.keySet();
//        // Set을 List로 변환합니다.
//        List<Integer> keys = new ArrayList<>(hashMap.keySet());
//        List<Data> list = new ArrayList<>();
//
//        for(int i=0;i<keys.size();i++){
//
//            int key = keys.get(i);
//            int value = hashMap.get(key);
//
//            Data data = new Data();
//            Amount amount = new Amount();
//
//            data.카테고리명 = application.카테고리데이터명가져오기(key);
//            data.카테고리식별자 = key;
//            amount.amount = value;
//            data.금액 = amount;
//
//            Log.i(TAG,"카테고리명 : "+data.카테고리명);
//            Log.i(TAG,"금액 : "+Integer.toString(amount.amount));
//
//            list.add(data);
//
//        }
//
//        cursor.close();
//
//        totalData.총수입 = 총수입;
//        totalData.총지출 = 총지출;
//        totalData.list = list;
//
//        return totalData;
//    }
//

//    DecimalFormat myFormatter = new DecimalFormat("###,###");
//
//    String 금액표시(long 금액){
//        return myFormatter.format(금액);
//    }

//    String createQuery(Date 시작날짜, Date 끝날짜,int 주기){
//
//        String query = null;
//
//        if(주기 == PeriodManager.기간) {
//            query = "select * from accountbook where " +
//                    " date>= '" + 변환4(시작날짜.getYear()) + "-" + 변환2(시작날짜.getMonth()) + "-" + 변환2(시작날짜.getDay()) + " 00:00' " +
//                    "AND date <= '" + 변환4(끝날짜.getYear()) + "-" + 변환2(끝날짜.getMonth()) + "-" + 변환2(끝날짜.getDay()) + " 23:59' " +
//                    "AND routine=0 ORDER by date";
//        }
//
//        Log.i(TAG,"createQuery : " + query);
//
//        return query;
//    }