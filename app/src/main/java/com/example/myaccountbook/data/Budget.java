package com.example.myaccountbook.data;

import android.content.ContentValues;
import android.icu.text.DecimalFormat;
import android.util.Log;

import com.example.myaccountbook.Data;
import com.example.myaccountbook.FeedReaderContract;

public class Budget {

    public static int ActivityType = 1;

    public static int 전체식별자 = 0;
    public static String 전체 = "전체예산";


//    public static int 기본예산 = 0;
//
//    public static int 일반예산 = 1;
//
//


    public Date date;
    public int 기본예산;

    public int 일반예산;

    public int 카테고리식별자;
    public String 카테고리이름;


    // 기본예산의 값으로 설정됨.
    // 본인 값 기본예산-기본예산값, 일반예산-일반예산값으로 설정됨.
    public int state;

    // 기본 예산 or 일반 예산
//    public int type;

    public int 기본예산pk;
    public int 일반예산pk;

    public Budget(){
        일반예산 = -1;
        기본예산 = -1;
    }


    DecimalFormat myFormatter = new DecimalFormat("###,###");

    public String 금액표시(int 금액){
        return myFormatter.format(금액)+"원";
    }

    public int 예산(){

        if(일반예산 == -1 && 기본예산 != -1){
            return 기본예산;
        }
        else if(일반예산 != -1){
            return 일반예산;
        }
        else{
            return -2;
        }

    }

    public void 예산(int 금액){

        if(기본예산 == -1){
            기본예산 = 금액;
        }
//        else if(일반예산 == -1){
//            일반예산 = 금액;
//        }
        else{
            일반예산 = 금액;
//            return -2;
        }

    }
//
//
//    public ContentValues 일반예산추가하기(){
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//
//        Log.i("Budget","일반예산 : "+Integer.toString(일반예산));
//        values.put(FeedReaderContract.일반예산테이블.COLUMN_YEAR, date.year);
//        values.put(FeedReaderContract.일반예산테이블.COLUMN_MONTH, date.month);
//        values.put(FeedReaderContract.일반예산테이블.COLUMN_AMOUNT, 일반예산 );
////        values.put(FeedReaderContract.일반예산테이블.COLUMN_BUDGET_AMOUNT, 일반예산);
//        values.put(FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK, 카테고리식별자);
////        values.put(FeedReaderContract.예산테이블.COLUMN_PK,  );
//
//        return values;
//    }
//    public ContentValues 기본예산추가하기(){
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//
//        values.put(FeedReaderContract.기본예산테이블.COLUMN_AMOUNT, 기본예산 );
////        values.put(FeedReaderContract.일반예산테이블.COLUMN_BUDGET_AMOUNT, 일반예산);
//        values.put(FeedReaderContract.기본예산테이블.COLUMN_CATEGORY_PK, 카테고리식별자);
////        values.put(FeedReaderContract.예산테이블.COLUMN_PK,  );
//
//        return values;
//    }
//    public ContentValues 기본예산수정하기(){
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//
//        values.put(FeedReaderContract.기본예산테이블.COLUMN_AMOUNT, 기본예산 );
////        values.put(FeedReaderContract.예산테이블.COLUMN_BUDGET_AMOUNT, 일반예산);
//        values.put(FeedReaderContract.기본예산테이블.COLUMN_PK, 기본예산pk );
//
//        return values;
//    }
//
//    public ContentValues 일반예산수정하기(){
//        // Create a new map of values, where column names are the keys
//        ContentValues values = new ContentValues();
//
//        values.put(FeedReaderContract.일반예산테이블.COLUMN_AMOUNT, 일반예산 );
////        values.put(FeedReaderContract.예산테이블.COLUMN_BUDGET_AMOUNT, 일반예산);
//        values.put(FeedReaderContract.일반예산테이블.COLUMN_PK, 일반예산pk );
//
//        return values;
//    }
//

}
