package com.example.myaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DecimalFormat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class RoutineManager {

    static String 없음 = "없음";
    static String 매일 = "매일";

    static String 주중 = "주중";

    static String 주말 = "주말";

    static String 매주 = "매주";

    static String two주마다 = "2주마다";

    static String four주마다 = "4주마다";

    static String one개월마다 = "매월";

    static String 월말 = "월말";

    static String two개월마다 = "2개월마다";

    static String three개월마다 = "3개월마다";

    static String four개월마다 = "4개월마다";

    static String six개월마다 = "6개월마다";
    static String one년마다 = "1년마다";

    String TAG = "RoutineManager";

    List<Integer> 반복날짜;
    List<routineRecord> 반복기록;

    long 합계;

    // 1~12 사이로

    RoutineManager(){
        반복날짜 = new ArrayList<>();
        반복기록 = new ArrayList<>();
    }
    public void getData(SQLiteDatabase database,MyApplication application){

        반복기록.clear();

        String sql = "select * from routine join accountbook on routine.AB_pk=accountbook.pk;";

        Log.i(TAG,sql);
        Cursor cursor = database.rawQuery(sql,null);

        while(cursor.moveToNext()){

            routineRecord record = new routineRecord();

            accountBook 내역 = new accountBook();
            int pk = cursor.getInt(0);
            int AB_pk = cursor.getInt(1);
            String name = cursor.getString(2);
            String date = cursor.getString(3);

            record.pk = pk;
            record.AB_pk = AB_pk;
            record.name = name;
            record.date = date;


            int acc_pk = cursor.getInt(4);
            int acc_type = cursor.getInt(5);
            int 자산pk = cursor.getInt(6);
            int 카테고리pk = cursor.getInt(7);
            int 금액 = cursor.getInt(8);
            String acc_date = cursor.getString(9);
            int 입금자산식별자 = cursor.getInt(10);
            int 출금자산식별자 = cursor.getInt(11);
            String 내용 = cursor.getString(13);
            String 메모 = cursor.getString(14);
            String 이미지1 = cursor.getString(15);
            String 이미지2 = cursor.getString(16);
            String 이미지3 = cursor.getString(17);
//            String content = cursor.getString(9);
//            String memo = cursor.getString(10);
//            String image1 = cursor.getString(11);
//            String image2 = cursor.getString(12);
//            String image3 = cursor.getString(13);

            내역.pk = acc_pk;
            내역.type = acc_type;
            내역.금액 = 금액;
            내역.금액원 = 금액표시(내역.금액);

            내역.내용 = 내용;

            내역.날짜 = acc_date;
            내역.date = 내역.parseISODate(내역.날짜);


//            내역.날짜 = 날짜;
//            내역.date = 내역.parseISODate(내역.날짜);

//
            내역.입금자산식별자 = 입금자산식별자;
            내역.출금자산식별자 = 출금자산식별자;
            내역.자산식별자 = 자산pk;
            내역.카테고리식별자 = 카테고리pk;
//            내역.내용 = content;
//
            내역.메모 = 메모;
            내역.이미지1 = 이미지1;
            내역.이미지2 = 이미지2;
            내역.이미지3 = 이미지3;

//            int 입금자산식별자 = cursor.getInt(10);
//            int 출금자산식별자 = cursor.getInt(11);
////            String 메모 = cursor.getString(14);
//            String 이미지1 = cursor.getString(15);
//            String 이미지2 = cursor.getString(16);
//            String 이미지3 = cursor.getString(17);

            내역 = application.치환(내역);

            record.내역 = 내역;

            반복기록.add(record);

        }

        cursor.close();

    }
    DecimalFormat myFormatter = new DecimalFormat("###,###");

    String 금액표시(long 금액){
        return myFormatter.format(금액);
    }
    
    
    // 해당년,월에 남은 반복일, 합계
    // 1~12 사이로
    // routine DB에는 과거 반복 기록은 남지 않는다.
    public long 총금액( int year, int month){

        반복날짜.clear();

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year,month-1,1,0,0,0);
        //  (A년 B월의 말일 : 말일
        int 말일 = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);  //말일


//        if(dayOfWeekNumber>1 && dayOfWeekNumber <7){
//            return time;
//        }

        Log.i(TAG,"반복기록 수 : "+Integer.toString(반복기록.size()));

        long 합계 = 0;
        for(int i=0;i<반복기록.size();i++){

            routineRecord record = 반복기록.get(i);

            int 금액 = record.내역.금액;
            String type = record.name;
            Log.i(TAG,"type : "+type);

            Date date = record.내역.parseISODate(record.date);
            calendar.setTime(date);
            int startYear = calendar.get(Calendar.YEAR);
            int startMonth = calendar.get(Calendar.MONTH)+1;
            int 요일 = calendar.get(Calendar.DAY_OF_WEEK);

            int startDay;
            if((startYear == year&&startMonth>month)||(startYear > year)){

                Log.i(TAG,"startYear : "+Integer.toString(startYear));
                Log.i(TAG,"startMonth : "+Integer.toString(startMonth));

                continue;
            }
            else if(startYear == year && startMonth == month){

                startDay = calendar.get(Calendar.DAY_OF_MONTH);
            }else{
                startDay = 1;
            }

            Log.i(TAG,"startDay : "+Integer.toString(startDay));

            if(type.equals(매일)){
                // 오늘부터 +1+1, 마자믹일까지.

                Log.i(TAG,"매일");
                if(record.내역.type == accountBook.수입){
                    합계 += (record.내역.금액*(말일-startDay+1));
                }
                else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                    합계 -= (record.내역.금액*(말일-startDay+1));
                }
                for(int j=startDay;j<=말일;j++){
                    if(!반복날짜.contains(j))
                        반복날짜.add(j);
                }
                
            }
            else if(type.equals(주중)){

                calendar.set(year,month-1,startDay);
                int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
//                int dayOfWeekNumber = 요일;
                for(int j=startDay;j<=말일;j++){


                    if(dayOfWeekNumber == 8){
                        dayOfWeekNumber = 1;
                    }
                    
                    // 주중
                    if(dayOfWeekNumber>1 && dayOfWeekNumber <7){

                        if(record.내역.type == accountBook.수입){
                            합계 += (record.내역.금액);
                        }
                        else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                            합계 -= (record.내역.금액);
                        }
                        if(!반복날짜.contains(j))
                            반복날짜.add(j);
                    }

                    dayOfWeekNumber++;

                }


            }
            else if(type.equals(주말)){


                while(! (calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    Log.i("1",Integer.toString(calendar.get(Calendar.MONTH)));
                    Log.i("1",Integer.toString(calendar.get(Calendar.YEAR)));

                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                }

                if(calendar.get(Calendar.DAY_OF_MONTH) == 7 && calendar.get(Calendar.DAY_OF_WEEK) == 7){

                    if(record.내역.type == accountBook.수입){
                        합계 += (record.내역.금액);
                    }
                    else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                        합계 -= (record.내역.금액);
                    }
                    if(!반복날짜.contains(1))
                        반복날짜.add(1);

                }
                else if(calendar.get(Calendar.DAY_OF_MONTH)>1 && calendar.get(Calendar.DAY_OF_WEEK) == 1 ){
                    Log.i("2",Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));

                    if(calendar.get(Calendar.DAY_OF_MONTH)-1 >= startDay){
                        if(record.내역.type == accountBook.수입){
                            합계 += (record.내역.금액);
                        }
                        else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                            합계 -= (record.내역.금액);
                        }
                        if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)-1))
                            반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH)-1);
                    }

                    if(record.내역.type == accountBook.수입){
                        합계 += (record.내역.금액);
                    }
                    else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                        합계 -= (record.내역.금액);
                    }

                    if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                        반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                    calendar.add(Calendar.DAY_OF_MONTH, 6);

                }

                while((calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){

                    if(calendar.get(Calendar.DAY_OF_MONTH) == 말일){

                        if(record.내역.type == accountBook.수입){
                            합계 += (record.내역.금액);
                        }
                        else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                            합계 -= (record.내역.금액);
                        }
                        if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                            반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                    }
                    else{

                        if(record.내역.type == accountBook.수입){
                            합계 += (record.내역.금액*2);
                        }
                        else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                            합계 -= (record.내역.금액*2);
                        }

                        if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                            반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                        if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)+1))
                            반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH)+1);

                        Log.i("",Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));


                        //                        Toast.makeText(, "", Toast.LENGTH_SHORT).show();

                    }

                    calendar.add(Calendar.WEEK_OF_YEAR, 1);

                }



            }
            else if(type.equals(매주)){

                calendar.set(year,month-1,startDay);
                int week = calendar.get(Calendar.DAY_OF_WEEK);

                for(int j=startDay;j<=말일;j++){

                    if(week == 8){
                        week = 1;
                    }

                    if(week == 요일 ){
                        if(record.내역.type == accountBook.수입){
                            합계 += (record.내역.금액);
                        }
                        else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                            합계 -= (record.내역.금액);
                        }
                        if(!반복날짜.contains(j))
                            반복날짜.add(j);
                    }

                    week++;

                }

            }
            else if(type.equals(two주마다)){


                while(! (calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    calendar.add(Calendar.WEEK_OF_YEAR, 2);
                }

                int count =0;
                while ((calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                        반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                    calendar.add(Calendar.WEEK_OF_YEAR, 2);
                    count++;
                }
                if(record.내역.type == accountBook.수입){
                    합계 += (record.내역.금액*count);
                }
                else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                    합계 -= (record.내역.금액*count);
                }

            }
            else if(type.equals(four주마다)){


                while(! (calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    calendar.add(Calendar.WEEK_OF_YEAR, 4);
                }

                int count =0;
                while ((calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                        반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));
                    calendar.add(Calendar.WEEK_OF_YEAR, 4);
                    count++;
                }
                if(record.내역.type == accountBook.수입){
                    합계 += (record.내역.금액*count);
                }
                else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                    합계 -= (record.내역.금액*count);
                }

            }
            else if(type.equals(one개월마다)){

                if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                    반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                if(record.내역.type == accountBook.수입){
                    합계 += (record.내역.금액);
                }
                else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                    합계 -= (record.내역.금액);
                }

            }
            else if(type.equals(월말)){


                if(!반복날짜.contains(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
                    반복날짜.add(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));


                if(record.내역.type == accountBook.수입){
                    합계 += (record.내역.금액);
                }
                else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                    합계 -= (record.내역.금액);
                }

            }
            else if(type.equals(two개월마다)){



                while(! (calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    calendar.add(Calendar.MONTH, 2);
                    if(calendar.get(Calendar.YEAR)>year || (calendar.get(Calendar.YEAR)==year && calendar.get(Calendar.MONTH)>month))
                        break;
                }

                int count =0;
                while ((calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){

                    if(record.내역.type == accountBook.수입){
                        합계 += (record.내역.금액);
                    }
                    else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                        합계 -= (record.내역.금액);
                    }

                    if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                        반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                }

            }
            else if(type.equals(three개월마다)){



                while(! (calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    calendar.add(Calendar.MONTH, 3);
                    if(calendar.get(Calendar.YEAR)>year || (calendar.get(Calendar.YEAR)==year && calendar.get(Calendar.MONTH)>month))
                        break;
                }

                int count =0;
                while ((calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    if(record.내역.type == accountBook.수입){
                        합계 += (record.내역.금액);
                    }
                    else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                        합계 -= (record.내역.금액);
                    }

                    if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                        반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                }

            }
            else if(type.equals(four개월마다)){



                while(! (calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    calendar.add(Calendar.MONTH, 4);
                    if(calendar.get(Calendar.YEAR)>year || (calendar.get(Calendar.YEAR)==year && calendar.get(Calendar.MONTH)>month))
                        break;
                }

                int count =0;
                while ((calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    if(record.내역.type == accountBook.수입){
                        합계 += (record.내역.금액);
                    }
                    else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                        합계 -= (record.내역.금액);
                    }

                    if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                        반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));


                }

            }
            else if(type.equals(six개월마다)){



                while(! (calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    calendar.add(Calendar.MONTH, 6);
                    if(calendar.get(Calendar.YEAR)>year || (calendar.get(Calendar.YEAR)==year && calendar.get(Calendar.MONTH)>month))
                        break;
                }

                int count =0;
                while ((calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    if(record.내역.type == accountBook.수입){
                        합계 += (record.내역.금액);
                    }
                    else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                        합계 -= (record.내역.금액);
                    }

                    if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                        반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                }

            }
            else if(type.equals(one년마다)){



                while(! (calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    calendar.add(Calendar.YEAR, 1);
                    if(calendar.get(Calendar.YEAR)>year || (calendar.get(Calendar.YEAR)==year && calendar.get(Calendar.MONTH)>month))
                        break;
                }

                int count =0;
                while ((calendar.get(Calendar.MONTH)+1 == month && calendar.get(Calendar.YEAR) == year)){
                    if(record.내역.type == accountBook.수입){
                        합계 += (record.내역.금액);
                    }
                    else if(record.내역.type == accountBook.고정지출 || record.내역.type == accountBook.변동지출){
                        합계 -= (record.내역.금액);
                    }

                    if(!반복날짜.contains(calendar.get(Calendar.DAY_OF_MONTH)))
                        반복날짜.add(calendar.get(Calendar.DAY_OF_MONTH));

                }

            }
        }

        return 합계;

    }


    public String 날짜(int month){

        String 날짜="";

        반복날짜.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });


        for(int i=0;i<반복날짜.size();i++){

            String m_date = Integer.toString(month)+"/"+Integer.toString(반복날짜.get(i));

            if (i != 0) {
                날짜 = 날짜 +", "+m_date;
            }
            else{
                날짜 = m_date;

            }
        }

        return 날짜;

    }


    public static Date 다음날짜(Date date, String name /*반복명*/){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if(name.equals(매일)){
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }
        else if(name.equals(주중)){
            int 요일 = calendar.get(Calendar.DAY_OF_WEEK);
            if(요일 > 6){
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            else{
                calendar.add(Calendar.DAY_OF_MONTH, 3);
            }

        }
        else if(name.equals(주말)){
            int 요일 = calendar.get(Calendar.DAY_OF_WEEK);
            if(요일 == 7){
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            else{
                calendar.add(Calendar.DAY_OF_MONTH, 6);
            }

        }
        else if(name.equals(매주)){
            calendar.add(Calendar.WEEK_OF_YEAR, 1);

        }
        else if(name.equals(two주마다)){
            calendar.add(Calendar.WEEK_OF_YEAR, 2);

        }
        else if(name.equals(four주마다)){
            calendar.add(Calendar.WEEK_OF_YEAR, 4);

        }
        else if(name.equals(one개월마다)){
            calendar.add(Calendar.MONTH, 1);
        }
        else if(name.equals(월말)){
            // 한주 이동, 그 년,월의 끝으로 이동.
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
//            calendar.add(Calendar.MONTH, 1);
        }
        else if(name.equals(two개월마다)){
            calendar.add(Calendar.MONTH, 2);
        }
        else if(name.equals(three개월마다)){
            calendar.add(Calendar.MONTH, 3);
        }
        else if(name.equals(four개월마다)){
            calendar.add(Calendar.MONTH, 4);
        }else if(name.equals(six개월마다)){
            calendar.add(Calendar.MONTH, 6);
        }else if(name.equals(one년마다)){
            calendar.add(Calendar.YEAR, 1);
        }
        else{
            return null;
        }

        return calendar.getTime();

    }

    public List<routineRecord> 총수입목록반환(){

        List<routineRecord> list = new ArrayList<>();

        for(int i=0;i<반복기록.size();i++) {

            routineRecord 반복 = 반복기록.get(i);
            if(반복.내역.type == accountBook.수입){

                list.add(반복);
                
            }

        }

        return list;

    }

    public List<routineRecord> 총지출목록반환(){

        List<routineRecord> list = new ArrayList<>();

        for(int i=0;i<반복기록.size();i++) {

            routineRecord 반복 = 반복기록.get(i);
            if(반복.내역.type == accountBook.고정지출 || 반복.내역.type == accountBook.변동지출){

                list.add(반복);

            }

        }

        return list;

    }

    public List<routineRecord> 총이체목록반환(){

        List<routineRecord> list = new ArrayList<>();

        for(int i=0;i<반복기록.size();i++) {

            routineRecord 반복 = 반복기록.get(i);
            if( 반복.내역.type == accountBook.이체 ){

                list.add(반복);

            }

        }

        return list;

    }


}
