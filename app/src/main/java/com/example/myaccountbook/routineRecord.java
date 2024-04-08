package com.example.myaccountbook;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class routineRecord {

    String TAG = "routineRecord";

    int pk;
    int AB_pk;
    String name;
    String date;

    accountBook 내역;

    routineRecord(){

    }


    public ContentValues 가계부내역데이터() {

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.반복내역테이블.COLUMN_PK, pk);

        values.put(FeedReaderContract.반복내역테이블.COLUMN_ABPK, AB_pk);
        values.put(FeedReaderContract.반복내역테이블.COLUMN_NAME, name);
        values.put(FeedReaderContract.반복내역테이블.COLUMN_DATE, date);
//        values.put(FeedReaderContract.반복내역테이블.TABLE_NAME, 내용);


        return values;

    }


}
