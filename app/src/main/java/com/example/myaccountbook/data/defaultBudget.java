package com.example.myaccountbook.data;

import android.content.ContentValues;
import android.icu.text.DecimalFormat;
import android.util.Log;

import com.example.myaccountbook.FeedReaderContract;

public class defaultBudget {


    public Date date;
    public int pk;
    public int 예산;

    public int 카테고리식별자;
    public String 카테고리이름;

    DecimalFormat myFormatter = new DecimalFormat("###,###");

    public String 금액표시(int 금액){
        return myFormatter.format(금액)+"원";
    }

    public ContentValues 일반예산추가하기(){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        Log.i("Budget","일반예산 : "+Integer.toString(예산));
        values.put(FeedReaderContract.일반예산테이블.COLUMN_YEAR, date.getYear());
        values.put(FeedReaderContract.일반예산테이블.COLUMN_MONTH, date.getMonth());
        values.put(FeedReaderContract.일반예산테이블.COLUMN_AMOUNT, 예산 );
//        values.put(FeedReaderContract.일반예산테이블.COLUMN_BUDGET_AMOUNT, 일반예산);
        values.put(FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK, 카테고리식별자);
//        values.put(FeedReaderContract.예산테이블.COLUMN_PK,  );

        return values;
    }
    public ContentValues 기본예산추가하기(){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.기본예산테이블.COLUMN_AMOUNT, 예산 );
//        values.put(FeedReaderContract.일반예산테이블.COLUMN_BUDGET_AMOUNT, 일반예산);
        values.put(FeedReaderContract.기본예산테이블.COLUMN_CATEGORY_PK, 카테고리식별자);
//        values.put(FeedReaderContract.예산테이블.COLUMN_PK,  );

        return values;
    }
    public ContentValues 기본예산수정하기(){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.기본예산테이블.COLUMN_AMOUNT, 예산 );
//        values.put(FeedReaderContract.예산테이블.COLUMN_BUDGET_AMOUNT, 일반예산);
        values.put(FeedReaderContract.기본예산테이블.COLUMN_PK, pk );

        return values;
    }

    public ContentValues 일반예산수정하기(){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.일반예산테이블.COLUMN_AMOUNT, 예산 );
//        values.put(FeedReaderContract.예산테이블.COLUMN_BUDGET_AMOUNT, 일반예산);
        values.put(FeedReaderContract.일반예산테이블.COLUMN_PK, pk );

        return values;
    }


}
