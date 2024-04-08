package com.example.myaccountbook;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import android.content.ContentValues;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.defaultBudget;

public class MyApplication extends Application {

    public List<Data> 자산데이터;
    public List<Data> 카테고리데이터;
    public List<Data> 통계청카테고리데이터;
    public List<Data> 반복데이터;

    AssetDataManager assetDataManager;


    private static final String 자산데이터가져오기 = "SELECT * FROM "+FeedReaderContract.데이터테이블.TABLE_NAME+" where type="+Integer.toString(Data.자산)+";";
    private static final String 카테고리데이터가져오기 = "SELECT * FROM "+FeedReaderContract.데이터테이블.TABLE_NAME+" where type="+Integer.toString(Data.카테고리)+";";
    private static final String 통계청카테고리데이터가져오기 = "SELECT * FROM "+FeedReaderContract.데이터테이블.TABLE_NAME+" where type="+Integer.toString(Data.통계청카테고리)+";";
    private static final String 반복데이터가져오기 = "SELECT * FROM "+FeedReaderContract.데이터테이블.TABLE_NAME+" where type="+Integer.toString(Data.반복)+";";


    String 치환2(int pk){

//        public List<Data> 자산데이터;
//        public List<Data> 카테고리데이터;
//        public List<Data> 통계청카테고리데이터;
//        public List<Data> 반복데이터;

        for(int i=0;i<자산데이터.size();i++){

            Data data = 자산데이터.get(i);

            if(data.pk == pk){
                return data.name;
            }

        }

        for(int i=0;i<카테고리데이터.size();i++){
            Data data = 카테고리데이터.get(i);

            if(data.pk == pk){
                return data.name;
            }

        }

        for(int i=0;i<통계청카테고리데이터.size();i++){
            Data data = 통계청카테고리데이터.get(i);

            if(data.pk == pk){
                return data.name;
            }

        }

        for(int i=0;i<반복데이터.size();i++){
            Data data = 반복데이터.get(i);

            if(data.pk == pk){
                return data.name;
            }

        }
        return null;

    }

    public void 일반예산추가수정(int category_pk,int 금액,int year,int month){
//        sdfsdsfd


        String tableName = FeedReaderContract.일반예산테이블.TABLE_NAME;
        String 일반예산있나요 = "SELECT * FROM "+tableName
                +" where "+FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK+"="+Integer.toString(category_pk)
                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_YEAR+"="+Integer.toString(year)
                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_MONTH+"="+Integer.toString(month)+";";
//                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_TYPE+"="+Budget.일반예산+";";
//
//        FeedReaderContract.일반예산테이블.

        List<defaultBudget> 일반예산목록 = dbHelper.일반예산데이터요청(readDb,일반예산있나요);

        if(일반예산목록.size() == 0){
            //추가

            defaultBudget budget = new defaultBudget();
            budget.예산 = 금액;
            budget.카테고리식별자 = category_pk;
            com.example.myaccountbook.data.Date date = new com.example.myaccountbook.data.Date();
            date.setYear(year);
            date.setMonth(month);
            budget.date = date;
//            sdsddsd
            dbHelper.데이터입력하기(readDb,FeedReaderContract.일반예산테이블.TABLE_NAME,budget.일반예산추가하기());

        }
        else{
            // 수정
            defaultBudget budget = 일반예산목록.get(0);
            budget.예산 = 금액;
            dbHelper.데이터수정하기(readDb,FeedReaderContract.일반예산테이블.TABLE_NAME,Integer.toString(budget.pk),budget.일반예산수정하기());

        }

//        List<defaultBudget> 기본예산목록 = dbHelper.기본예산데이터요청(readDb);
//        for(int i=0;i<기본예산목록.size();i++){
//
//            defaultBudget budget = 기본예산목록.get(i);
//            if(budget.카테고리식별자 == category_pk){
//                budget.예산 = 금액;
//                dbHelper.데이터수정하기(readDb,FeedReaderContract.기본예산테이블.TABLE_NAME,Integer.toString(budget.pk),budget.기본예산수정하기());
//
//            }
//        }

    }

    public void 기본예산수정(int category_pk,int 금액){
//        sdfsdsfd

        List<defaultBudget> 기본예산목록 = dbHelper.기본예산데이터요청(readDb);
        for(int i=0;i<기본예산목록.size();i++){

            defaultBudget budget = 기본예산목록.get(i);
            if(budget.카테고리식별자 == category_pk){
                budget.예산 = 금액;
                dbHelper.데이터수정하기(readDb,FeedReaderContract.기본예산테이블.TABLE_NAME,Integer.toString(budget.pk),budget.기본예산수정하기());

            }
        }

    }

    public void 예산삭제(int category_pk){


//        sfdssd

        List<defaultBudget> list = new ArrayList<>();
        String tableName = FeedReaderContract.일반예산테이블.TABLE_NAME;
        String 일반예산있나요 = "SELECT * FROM "+tableName
                +" where "+FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK+"="+Integer.toString(category_pk)+";";
//                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK+"="+Integer.toString(budget.카테고리식별자)+";";
//                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_TYPE+"="+Budget.일반예산+";";
//

        List<defaultBudget> 일반예산목록 = dbHelper.일반예산데이터요청(readDb,일반예산있나요);
        List<defaultBudget> 기본예산목록 = dbHelper.기본예산데이터요청(readDb);


        for(int i=0;i<일반예산목록.size();i++){
            defaultBudget defaultBudget = 일반예산목록.get(i);
//            defaultBudget.pk
            deleteRecord(FeedReaderContract.일반예산테이블.TABLE_NAME,defaultBudget.pk);
        }


        for(int i=0;i<기본예산목록.size();i++){
            defaultBudget defaultBudget = 기본예산목록.get(i);

            if(defaultBudget.카테고리식별자 == category_pk){
                deleteRecord(FeedReaderContract.기본예산테이블.TABLE_NAME,defaultBudget.pk);

            }
//            defaultBudget.pk
        }




    }

    public void deleteRecord(String TABLE_NAME, int id) {
        dbHelper.deleteRecord(TABLE_NAME,id);
    }

    public accountBook 치환(accountBook 가계부 ){

        가계부.카테고리 = 치환2(가계부.카테고리식별자);

        가계부.자산 = 치환2(가계부.자산식별자);
        가계부.입금자산 = 치환2(가계부.입금자산식별자);
        가계부.출금자산 = 치환2(가계부.출금자산식별자);

        return 가계부;
    }


    SQLiteDatabase db;
    public SQLiteDatabase readDb;
    FeedReaderDbHelper dbHelper;
    @Override
    public void onCreate() {
        super.onCreate();


        Log.i(TAG,"onCreate");
        //        DBHelper helper;
//        SQLiteDatabase db;
//        helper = new DBHelper(getApplicationContext(), "newdb.db", null, 1);
//        db = helper.getWritableDatabase();
//        helper.onCreate(db);

        dbHelper = new FeedReaderDbHelper(getApplicationContext());
//푸시알림을 보내기 위해, 시스템에서 알림 서비스를 생성해주는 코드
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        // getWritableDatabase() 실행시에는 백앤드에서 쓰레드로 돌리길 권장,(공식문서)
        Thread init = new Thread(new Runnable() {
            @Override
            public void run() {
                db = dbHelper.getWritableDatabase();
                readDb = dbHelper.getReadableDatabase();
                assetDataManager = new AssetDataManager(readDb);

                dbHelper.init(db);
//                dbHelper.onCreate(db);


//                // Create a new map of values, where column names are the keys
//                ContentValues values = new ContentValues();
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "title");
//                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "subtitle");
//
//                // Insert the new row, returning the primary key value of the new row
//                long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                자산데이터 = dbHelper.데이터요청(db,자산데이터가져오기);
                카테고리데이터 = dbHelper.데이터요청(db,카테고리데이터가져오기);
                통계청카테고리데이터 = dbHelper.데이터요청(db,통계청카테고리데이터가져오기);
                반복데이터 = dbHelper.데이터요청(db,반복데이터가져오기);


                // 이전 반복 내역들 중에서 자동저장 해야 하는 것들 모두 자동저장
                // 자정에 실행되는 알람 설정.
                이전반복내역자동저장();

                //테스트데이터만들기();

                

            }
        });

        init.start();



    }


    public void 테스트데이터만들기(){

        Random rand = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023,Calendar.JANUARY,1,0,0,0);
        long start_date = calendar.getTime().getTime();

        calendar.set(2026,Calendar.JANUARY,1,0,0,0);
        long end_date = calendar.getTime().getTime();
        long start_end = end_date - start_date;
        Log.i("start_date",Long.toString(start_date));
        Log.i("end_date",Long.toString(end_date));
        Log.i("start_end",Long.toString(start_end));

        for(int i=0;i<4000;i++){

            accountBook 가계부 = new accountBook();

            가계부.type = rand.nextInt(4)+1;
            가계부.금액 = rand.nextInt(10000)+1;
            가계부.내용 = "내용";

            Log.i("자산데이터.size",Integer.toString(자산데이터.size()));
            Log.i("카테고리데이터.size",Integer.toString(카테고리데이터.size()));

            if(가계부.type != accountBook.이체){
                가계부.자산식별자 = 자산데이터.get(rand.nextInt(자산데이터.size())).pk;
                가계부.카테고리식별자 = 카테고리데이터.get(rand.nextInt(카테고리데이터.size())).pk;
            }
            else{
                가계부.출금자산식별자 = 자산데이터.get(rand.nextInt(자산데이터.size())).pk;
                가계부.입금자산식별자 = 자산데이터.get(rand.nextInt(자산데이터.size())).pk;
            }



            Date date = new Date();
            long randomLongValue = ThreadLocalRandom.current().nextLong(start_date, end_date);
            date.setTime(randomLongValue);

            Log.i("date",Long.toString(date.getTime()));
            가계부.날짜 = 가계부.날짜(date);
            Log.i(" 가계부.날짜", 가계부.날짜);

//            date.setTime(start_date);
//            가계부.날짜 = 가계부.날짜(date);
//            Log.i(" 가계부.날짜 start_date", 가계부.날짜);
//            date.setTime(end_date);
//            가계부.날짜 = 가계부.날짜(date);
//            Log.i(" 가계부.날짜 end_date", 가계부.날짜);

            //  int randIntWithin100 = rand.nextInt(100); // 0(포함)부터 100(미포함) 사이의 임의의 정수


            // Create a new map of values, where column names are the keys
            ContentValues values = 가계부.가계부내역데이터(false);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(FeedReaderContract.가계부내역.TABLE_NAME, null, values);
            if(newRowId == -1){
                Log.i("init","insert error");
                Toast.makeText(this, "insert error", Toast.LENGTH_SHORT).show();
                return;
            }
        }


    }


    // 현재 날짜 보다 이전의 날짜 들은 모두 자동저장
    // record, 가계부 데이터의 날짜 수정.
    public void 이전반복내역자동저장(){

        Calendar calendar = Calendar.getInstance();
        // 현재 시간 년, 월, 일
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // calendar 보다
        calendar.set(year,month,day,0,0);

        RoutineManager routineManager = new RoutineManager();
        routineManager.getData(readDb,this);
//        List<routineRecord> 반복기록 = routineManager.반복기록;


        for(int i=0;i<routineManager.반복기록.size();i++){

            // 현재 날짜 보다 이전의 날짜 들은 모두 자동저장
            // record, 가계부 데이터의 날짜 수정.
            routineRecord record = routineManager.반복기록.get(i);
            Date date = record.내역.parseISODate(record.date);


            // 반복날짜가 이후 가 아니라면 ( 현재날짜보다)
            while (!date.after(calendar.getTime())){

                //
                가계부작성하기(record.내역);

                record.내역.date = RoutineManager.다음날짜(record.내역.date,record.name);
                record.내역.날짜 = record.내역.날짜(record.내역.date);
                record.date = record.내역.날짜;

                date = record.내역.parseISODate(record.date);

            }


            ContentValues values = record.내역.가계부내역데이터(true);
            db.update(FeedReaderContract.가계부내역.TABLE_NAME, values, "pk=?", new String[]{Integer.toString(record.내역.pk)});

            ContentValues values1 = record.가계부내역데이터();
            db.update(FeedReaderContract.반복내역테이블.TABLE_NAME,values1,"pk=?", new String[]{Integer.toString(record.pk)});


        }

        // 예약 걸어두기.
        calendar.add(Calendar.DAY_OF_MONTH,1);
        // 예약걸어두기.


        //알람을 수신할 수 있도록 하는 리시버로 인텐트 요청
        Intent receiverIntent = new Intent(this, NotificationReceiver.class);
        receiverIntent.putExtra("content", "알람등록 테스트");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 123, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);


    }
    AlarmManager alarmManager;


    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG,"onTerminate");

        Intent receiverIntent = new Intent(this, NotificationReceiver.class);
        receiverIntent.putExtra("content", "알람등록 테스트");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 123, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

    }

    public List<Data> 삭제안된자산데이터(){
        List<Data> assetData = new ArrayList<>();

        for(int i=0;i<자산데이터.size();i++){
            Data data = 자산데이터.get(i);

            if(data.enable == Data.활성화){
                assetData.add(data);
            }
        }

        return assetData;
    }

    public List<Data> 삭제안된카테고리데이터(){
        List<Data> assetData = new ArrayList<>();

        for(int i=0;i<카테고리데이터.size();i++){
            Data data = 카테고리데이터.get(i);

            if(data.enable == Data.활성화){
                assetData.add(data);
            }
        }

        return assetData;
    }

    public int 카테고리데이터식별자(String name){

        Log.i(TAG,name);


        for(int i=0;i<통계청카테고리데이터.size();i++){
            Data data = 통계청카테고리데이터.get(i);

            Log.i(TAG,data.name);
            Log.i(TAG,Integer.toString(data.pk));
            Log.i(TAG,Integer.toString(data.type));


            if(data.name.equals(name)){
                return data.pk;
//                assetData.add(data);
            }
        }

        return -1;
    }
    public String 자산데이터명(int pk){

        Log.i(TAG,Integer.toString(pk));


        for(int i=0;i<자산데이터.size();i++){
            Data data = 자산데이터.get(i);

//            Log.i(TAG,data.name);
//            Log.i(TAG,Integer.toString(data.pk));
//            Log.i(TAG,Integer.toString(data.type));


            if(data.pk == pk){
                return data.name;
//                return data.pk;
//                assetData.add(data);
            }
        }

        return null;
    }

    public int 카테고리식별자(String name){

        Log.i(TAG,name);


        for(int i=0;i<카테고리데이터.size();i++){
            Data data = 카테고리데이터.get(i);

//            Log.i(TAG,data.name);
//            Log.i(TAG,Integer.toString(data.pk));
//            Log.i(TAG,Integer.toString(data.type));


            if(data.type == Data.카테고리 && data.name.equals(name)){
                return data.pk;
//                return data.pk;
//                assetData.add(data);
            }
        }

        return -1;
    }

    public String 카테고리데이터명가져오기(int pk){

        Log.i(TAG,Integer.toString(pk));


        for(int i=0;i<카테고리데이터.size();i++){
            Data data = 카테고리데이터.get(i);

            Log.i(TAG,data.name);
            Log.i(TAG,Integer.toString(data.pk));
            Log.i(TAG,Integer.toString(data.type));


            if(data.pk == pk){
                return data.name;
//                return data.pk;
//                assetData.add(data);
            }
        }

        return null;
    }
    public String 카테고리데이터명(int pk){

        Log.i(TAG,Integer.toString(pk));


        for(int i=0;i<통계청카테고리데이터.size();i++){
            Data data = 통계청카테고리데이터.get(i);

            Log.i(TAG,data.name);
            Log.i(TAG,Integer.toString(data.pk));
            Log.i(TAG,Integer.toString(data.type));


            if(data.pk == pk){
                return data.name;
//                return data.pk;
//                assetData.add(data);
            }
        }

        return null;
    }
    
    public  List<String> 통계청가져오기(){
        List<String> 통계청 = new ArrayList<>();

//        Toast.makeText(this, Integer.toString(통계청카테고리데이터.size()), Toast.LENGTH_SHORT).show();
        for(int i=0;i<통계청카테고리데이터.size();i++){
            Data 통계청데이터 = 통계청카테고리데이터.get(i);
            통계청.add(통계청데이터.name);
        }
        return 통계청;

    }
    public long 가계부작성하기(accountBook 가계부){

        // Create a new map of values, where column names are the keys
        ContentValues values = 가계부.가계부내역데이터(false);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.가계부내역.TABLE_NAME, null, values);

        return newRowId;

    }
    public void 가계부수정(accountBook 가계부){
        // calling a method to get writable database.
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = 가계부.가계부내역데이터(false);

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(FeedReaderContract.가계부내역.TABLE_NAME, values, "pk=?", new String[]{Integer.toString(가계부.pk)});
//        db.close();
    }

    public long 데이터저장하기(Data data){

        // Create a new map of values, where column names are the keys
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.데이터테이블.COLUMN_TYPE, data.type);
        values.put(FeedReaderContract.데이터테이블.COLUMN_NAME, data.name);

        if(data.type == Data.카테고리)
            values.put(FeedReaderContract.데이터테이블.COLUMN_CATEGORY, data.office_category);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FeedReaderContract.데이터테이블.TABLE_NAME, null, values);

        return newRowId;

    }


    public void 데이터수정(Data data){
        // calling a method to get writable database.
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(FeedReaderContract.데이터테이블.COLUMN_PK, data.pk);
        values.put(FeedReaderContract.데이터테이블.COLUMN_NAME, data.name);
        if(data.enable == Data.비활성화)
            values.put(FeedReaderContract.데이터테이블.COLUMN_ENABLE, data.enable);
        if(data.type == Data.카테고리)
            values.put(FeedReaderContract.데이터테이블.COLUMN_CATEGORY, data.office_category);

//        values.put(FeedReaderContract.데이터테이블.COLUMN_DELETEDATE, data.delete_date);

        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.update(FeedReaderContract.데이터테이블.TABLE_NAME, values, "pk=?", new String[]{Integer.toString(data.pk)});

        if(data.type == Data.자산){
            for(int i=0;i<자산데이터.size();i++){
                Data m_data = 자산데이터.get(i);

                if(data.pk == m_data.pk){
                    자산데이터.set(i,data);
                }
            }
        }
        else if(data.type == Data.카테고리){
            for(int i=0;i<카테고리데이터.size();i++){
                Data m_data = 카테고리데이터.get(i);

                if(data.pk == m_data.pk){
                    카테고리데이터.set(i,data);
                }
            }
        }
        else if(data.type == Data.통계청카테고리){
            for(int i=0;i<통계청카테고리데이터.size();i++){
                Data m_data = 통계청카테고리데이터.get(i);

                if(data.pk == m_data.pk){
                    통계청카테고리데이터.set(i,data);
                }
            }
        }
        else if(data.type == Data.반복){
            for(int i=0;i<반복데이터.size();i++){
                Data m_data = 반복데이터.get(i);

                if(data.pk == m_data.pk){
                    반복데이터.set(i,data);
                }
            }
        }

//        db.close();
    }

    public void 데이터삭제(int pk){
        // calling a method to get writable database.
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.데이터테이블.COLUMN_PK, pk);
        values.put(FeedReaderContract.데이터테이블.COLUMN_ENABLE, Data.비활성화);

        db.update(FeedReaderContract.데이터테이블.TABLE_NAME, values, "pk=?", new String[]{Integer.toString(pk)});

        for(int i=0;i<자산데이터.size();i++){
            Data data = 자산데이터.get(i);

//            Log.i(TAG,data.name);
//            Log.i(TAG,Integer.toString(data.pk));
//            Log.i(TAG,Integer.toString(data.type));

            if(data.pk == pk){
                data.enable = Data.비활성화;
//                return data.pk;
//                assetData.add(data);
            }
        }
//        db.close();
    }


    public void 데이터명수정(int pk,String name){
        // calling a method to get writable database.
//        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.데이터테이블.COLUMN_PK, pk);
        values.put(FeedReaderContract.데이터테이블.COLUMN_NAME, name);

        db.update(FeedReaderContract.데이터테이블.TABLE_NAME, values, "pk=?", new String[]{Integer.toString(pk)});

        for(int i=0;i<자산데이터.size();i++){
            Data data = 자산데이터.get(i);

//            Log.i(TAG,data.name);
//            Log.i(TAG,Integer.toString(data.pk));
//            Log.i(TAG,Integer.toString(data.type));


            if(data.pk == pk){
                data.name = name;
//                return data.pk;
//                assetData.add(data);
            }
        }
//        db.close();
    }


    public long 가계부작성하기(accountBook 가계부,String 반복명){

        // 반복날짜가 오늘이면, 내역 작성하고, 다음날짜로 기입.
        // 반복 날짜에도 작성.
        // 반복 로직에 추가.



        // 반복날짜가 이후 가 아니라면 ( 현재날짜보다)
        while (Routine.오늘또는오늘이전인지(가계부.date)){

            // Create a new map of values, where column names are the keys
            ContentValues values = 가계부.가계부내역데이터(false);
            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(FeedReaderContract.가계부내역.TABLE_NAME, null, values);
            if(newRowId == -1){
                return newRowId;
            }

            가계부.date = RoutineManager.다음날짜(가계부.date,반복명);
            가계부.날짜 = 가계부.날짜(가계부.date);

//
//            가계부작성하기(record.내역);
//
//            record.내역.date = RoutineManager.다음날짜(record.내역.date,record.name);
//            record.내역.날짜 = record.내역.날짜(record.내역.date);
//            record.date = record.내역.날짜;
//
//            date = record.내역.parseISODate(record.date);

        }


//        if(Routine.오늘또는오늘이전인지(가계부.date)){
//            // Create a new map of values, where column names are the keys
//            ContentValues values = 가계부.가계부내역데이터(false);
//            // Insert the new row, returning the primary key value of the new row
//            long newRowId = db.insert(FeedReaderContract.가계부내역.TABLE_NAME, null, values);
//            if(newRowId == -1){
//                return newRowId;
//            }
//
////            long 날짜 = 가계부.date.getTime()+Routine.하루;
////
////
////
////            가계부.date = Routine.getTime(RoutineManager.다음날짜(가계부.date,반복명),반복명);
//
//            가계부.date = RoutineManager.다음날짜(가계부.date,반복명);
//
//            가계부.날짜 = 가계부.날짜(가계부.date);
//
//
//        }

        ContentValues values = 가계부.가계부내역데이터(true);
        long newRowId = db.insert(FeedReaderContract.가계부내역.TABLE_NAME, null, values);

        if(newRowId == -1){
            return newRowId;
        }

        // 반복 데이터 추가.
        // Create a new map of values, where column names are the keys
        ContentValues values2 = new ContentValues();
        values2.put(FeedReaderContract.반복내역테이블.COLUMN_ABPK, newRowId);
        values2.put(FeedReaderContract.반복내역테이블.COLUMN_NAME, 반복명);
        values2.put(FeedReaderContract.반복내역테이블.COLUMN_DATE, 가계부.날짜);
        long newRowId2 = db.insert(FeedReaderContract.반복내역테이블.TABLE_NAME, null, values2);
        if(newRowId2 == -1){
            return newRowId2;
        }
        else{
            // 알림 설정.
            return newRowId2;
        }

    }


    public void 예산추가(defaultBudget budget){


        String tableName = FeedReaderContract.기본예산테이블.TABLE_NAME;
        String 기본예산있나요 = "SELECT * FROM "+tableName
                +" where "+FeedReaderContract.기본예산테이블.COLUMN_CATEGORY_PK+"="+Integer.toString(budget.카테고리식별자)+";";
//                +" AND "+FeedReaderContract.예산테이블.COLUMN_MONTH+"="+Integer.toString(budget.date.month)
//                +" AND "+FeedReaderContract.예산테이블.COLUMN_CATEGORY_PK+"="+budget.카테고리식별자
//                +" AND "+FeedReaderContract.예산테이블.COLUMN_TYPE+"="+Budget.기본예산+";";
        tableName = FeedReaderContract.일반예산테이블.TABLE_NAME;
        String 일반예산있나요 = "SELECT * FROM "+tableName
                +" where "+FeedReaderContract.일반예산테이블.COLUMN_YEAR+"="+Integer.toString(budget.date.getYear())
                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_MONTH+"="+Integer.toString(budget.date.getMonth())
                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK+"="+Integer.toString(budget.카테고리식별자)+";";
//                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_TYPE+"="+Budget.일반예산+";";



        // 기본예산 or 일반예산 추가
        // 이미 있다면 일반예산 수정
        if(dbHelper.데이터가있나요(readDb,기본예산있나요)){
            if(dbHelper.데이터가있나요(readDb,일반예산있나요)){
                //이미 있다면 일반예산 수정
////                budget.type = Budget.일반예산;
//                // pk 가져오기
                List<defaultBudget> list = dbHelper.일반예산데이터요청(readDb,일반예산있나요);
                Log.i(TAG,Integer.toString(list.size()));

                budget.pk = list.get(0).pk;
                dbHelper.데이터수정하기(readDb,FeedReaderContract.일반예산테이블.TABLE_NAME,Integer.toString(budget.pk),budget.일반예산수정하기());

                Log.i(TAG,"일반 예산 수정하기");
            }
            else{
                // 일반예산 추가
//                budget.type = Budget.일반예산;
                dbHelper.데이터입력하기(readDb,FeedReaderContract.일반예산테이블.TABLE_NAME,budget.일반예산추가하기());
            }
        }
        else{
            // 기본예산 추가.
//            budget.type = Budget.기본예산;
            dbHelper.데이터입력하기(readDb,FeedReaderContract.기본예산테이블.TABLE_NAME,budget.기본예산추가하기());

        }



    }

    String TAG = "Application";
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG,"onLowMemory");

    }

    public List<defaultBudget> 예산목록(int year,int month){

        List<defaultBudget> list = new ArrayList<>();
        String tableName = FeedReaderContract.일반예산테이블.TABLE_NAME;
        String 일반예산있나요 = "SELECT * FROM "+tableName
                +" where "+FeedReaderContract.일반예산테이블.COLUMN_YEAR+"="+Integer.toString(year)
                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_MONTH+"="+Integer.toString(month)+";";
//                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK+"="+Integer.toString(budget.카테고리식별자)+";";
//                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_TYPE+"="+Budget.일반예산+";";
//

        List<defaultBudget> 일반예산목록 = dbHelper.일반예산데이터요청(readDb,일반예산있나요);
        List<defaultBudget> 기본예산목록 = dbHelper.기본예산데이터요청(readDb);


        for(int i=0;i<기본예산목록.size();i++){

            defaultBudget budget = 기본예산목록.get(i);

            boolean check = false;

            for(int j=0;j<일반예산목록.size();j++){
                defaultBudget 일반예산 = 일반예산목록.get(j);
                if(일반예산.카테고리식별자 == budget.카테고리식별자){
                    check = true;
                    list.add(일반예산);

                    break;
//                    budget.일반예산 = 일반예산.예산;
//                    budget.일반예산pk = 일반예산.pk;
//                    Log.i(TAG,"일반예산 : "+Integer.toString(budget.일반예산));
                }

            }

            if(!check){
                list.add(budget);
            }


        }

        return list;

    }


    public List<Budget> 년월예산목록(int year, int month){

        String tableName = FeedReaderContract.기본예산테이블.TABLE_NAME;
        String 기본예산있나요 = "SELECT * FROM "+tableName+";";
//                +" where "+FeedReaderContract.기본예산테이블.COLUMN_CATEGORY_PK+"="+Integer.toString(budget.카테고리식별자)+";";
//                +" AND "+FeedReaderContract.예산테이블.COLUMN_MONTH+"="+Integer.toString(budget.date.month)
//                +" AND "+FeedReaderContract.예산테이블.COLUMN_CATEGORY_PK+"="+budget.카테고리식별자
//                +" AND "+FeedReaderContract.예산테이블.COLUMN_TYPE+"="+Budget.기본예산+";";
        tableName = FeedReaderContract.일반예산테이블.TABLE_NAME;
        String 일반예산있나요 = "SELECT * FROM "+tableName
                +" where "+FeedReaderContract.일반예산테이블.COLUMN_YEAR+"="+Integer.toString(year)
                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_MONTH+"="+Integer.toString(month)+";";
//                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK+"="+Integer.toString(budget.카테고리식별자)+";";
//                +" AND "+FeedReaderContract.일반예산테이블.COLUMN_TYPE+"="+Budget.일반예산+";";
//

        List<defaultBudget> 일반예산목록 = dbHelper.일반예산데이터요청(readDb,일반예산있나요);
        List<defaultBudget> 기본예산목록 = dbHelper.기본예산데이터요청(readDb);


        List<Budget> list = new ArrayList<>();
        com.example.myaccountbook.data.Date date = new com.example.myaccountbook.data.Date();
        date.setYear(year);
        date.setMonth(month);

        for(int i=0;i<기본예산목록.size();i++){

            Budget budget = new Budget();

            budget.기본예산pk = 기본예산목록.get(i).pk;
            budget.date = date;
            budget.카테고리식별자 = 기본예산목록.get(i).카테고리식별자;
            budget.기본예산 = 기본예산목록.get(i).예산;
            Log.i(TAG,"기본예산 : " + Integer.toString(budget.기본예산));


            for(int j=0;j<일반예산목록.size();j++){
                defaultBudget 일반예산 = 일반예산목록.get(j);
                if(일반예산.카테고리식별자 == 기본예산목록.get(i).카테고리식별자){
                    budget.일반예산 = 일반예산.예산;
                    budget.일반예산pk = 일반예산.pk;
                    Log.i(TAG,"일반예산 : "+Integer.toString(budget.일반예산));
                }

            }

            list.add(budget);

        }


        return list;

    }

}
