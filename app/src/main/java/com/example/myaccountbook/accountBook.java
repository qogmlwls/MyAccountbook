package com.example.myaccountbook;

import android.content.ContentValues;
import android.util.Log;

import com.example.myaccountbook.data.Amount;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class accountBook implements Serializable {

    public static final int 수입 = 1;
    public static final int 고정지출 = 2;
    public static final int 변동지출 = 3;
    public static final int 이체 = 4;

    public static final int 지출 = 5;
    
    public int pk;
    public int type;

    public int 자산식별자;
    public int 카테고리식별자;
    public String 카테고리,자산,입금자산,출금자산;
    int 반복타입;
    public int 입금자산식별자;
    public int 출금자산식별자;

    public String 날짜;

    public int 금액;
    public Amount amount;

    public String 금액원;
    public String 내용;
    public String 이미지1, 이미지2, 이미지3;

    public String 메모;

    public Date date;

    public SimpleDateFormat 날짜형식 = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.KOREA);


    public accountBook(){
        
    }
    
    accountBook(int type,int 자산식별자, int 금액, int 카테고리식별자){

        this.type = type;
        this.자산식별자 = 자산식별자;
        this.금액 = 금액;
        Calendar calendar = Calendar.getInstance();
//        calendar.getTime();

        this.카테고리식별자 =  카테고리식별자;

        날짜 = 날짜(calendar.getTime());

        내용 = "차액";

    }
    
    

    // 저장된 ISO 8601 형식의 날짜를 다시 파싱하여 Date 객체로 변환하는 메소드 예시
    public Date parseISODate(String isoDate) {
        try {
            return 날짜형식.parse(isoDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String 변환2(int number){
        return String.format("%02d", number);
    }
    private String 변환4(int number){
        return String.format("%04d", number);
    }
    public String 날짜(int year,int month, int day,int hour,int min){
        return 변환4(year)+"-"+변환2(month)+"-"+변환2(day)+" "+변환2(hour)+":"+변환2(min);
    }




    public String 날짜(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR); // 월은 0부터 시작하므로 +1 해줍니다.
        int min = calendar.get(Calendar.MINUTE);
//        int year,int month, int day,int hour,int min
        return 변환4(year)+"-"+변환2(month)+"-"+변환2(day)+" "+변환2(hour)+":"+변환2(min);
    }


    // repeatData true시 반복데이터로 적용
    // false시 일반데이터로 저장.
    public ContentValues 가계부내역데이터(boolean repeatData){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.가계부내역.COLUMN_TYPE, type);

        values.put(FeedReaderContract.가계부내역.COLUMN_AMOUNT, 금액);
        values.put(FeedReaderContract.가계부내역.COLUMN_DATE, 날짜);
        values.put(FeedReaderContract.가계부내역.COLUMN_CONTENT, 내용);

        if(repeatData){
            values.put(FeedReaderContract.가계부내역.COLUMN_ROUTINETYPEs, Routine.반복있음);
        }
        else{
            values.put(FeedReaderContract.가계부내역.COLUMN_ROUTINETYPEs, Routine.반복없음);
        }


        switch (type){
            case accountBook.수입 :
            case accountBook.변동지출 :
            case accountBook.고정지출 :
                values.put(FeedReaderContract.가계부내역.COLUMN_ASSET_PK, 자산식별자);
                values.put(FeedReaderContract.가계부내역.COLUMN_CATEGORY_PK, 카테고리식별자);
                break;
            case accountBook.이체 :
                values.put(FeedReaderContract.가계부내역.COLUMN_DEPOSIT, 입금자산식별자);
                values.put(FeedReaderContract.가계부내역.COLUMN_WITHDRAW, 출금자산식별자);
                break;
            default:
                Log.i("?",Integer.toString(type));
        }


        if(메모 != null)
            values.put(FeedReaderContract.가계부내역.COLUMN_MEMO, 메모);


        if(이미지1 != null)
            values.put(FeedReaderContract.가계부내역.COLUMN_IMAGE1, 이미지1);
        else{
            values.putNull(FeedReaderContract.가계부내역.COLUMN_IMAGE1);
//            values.remove(FeedReaderContract.가계부내역.COLUMN_IMAGE1);
//           values.put(FeedReaderContract.가계부내역.COLUMN_IMAGE1, (String) null);
        }
        if(이미지2 != null)
           values.put(FeedReaderContract.가계부내역.COLUMN_IMAGE2, 이미지2);
        else{
            values.putNull(FeedReaderContract.가계부내역.COLUMN_IMAGE2);
//            values.remove(FeedReaderContract.가계부내역.COLUMN_IMAGE1);
//           values.put(FeedReaderContract.가계부내역.COLUMN_IMAGE1, (String) null);
        }

        if(이미지3 != null)
            values.put(FeedReaderContract.가계부내역.COLUMN_IMAGE3, 이미지3);
        else{
            values.putNull(FeedReaderContract.가계부내역.COLUMN_IMAGE3);
//            values.remove(FeedReaderContract.가계부내역.COLUMN_IMAGE1);
//           values.put(FeedReaderContract.가계부내역.COLUMN_IMAGE1, (String) null);
        }
        return values;

    }

}
