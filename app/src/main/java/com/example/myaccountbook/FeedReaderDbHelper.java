package com.example.myaccountbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.defaultBudget;

import java.util.ArrayList;
import java.util.List;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "test10.db";

    private static final String createDataTable =
            "CREATE TABLE if not exists " + FeedReaderContract.데이터테이블.TABLE_NAME + " (" +
                    FeedReaderContract.데이터테이블.COLUMN_PK + " INTEGER ," +
                    FeedReaderContract.데이터테이블.COLUMN_TYPE + " INTEGER," +
                    FeedReaderContract.데이터테이블.COLUMN_NAME + " TEXT," +
                    FeedReaderContract.데이터테이블.COLUMN_CYCLE + " TEXT, " +
                    FeedReaderContract.데이터테이블.COLUMN_CATEGORY + " INTEGER, " +
                    FeedReaderContract.데이터테이블.COLUMN_ENABLE + " INTEGER DEFAULT 0, " +
                    FeedReaderContract.데이터테이블.COLUMN_DELETEDATE + " TEXT, " +
                    " PRIMARY KEY( pk AUTOINCREMENT) ); ";

    private static final String createRoutineTable =
            "CREATE TABLE if not exists " + FeedReaderContract.반복내역테이블.TABLE_NAME + " (" +
                    FeedReaderContract.반복내역테이블.COLUMN_PK + " INTEGER ," +
                    FeedReaderContract.반복내역테이블.COLUMN_ABPK + " INTEGER," +
                    FeedReaderContract.반복내역테이블.COLUMN_NAME + " TEXT," +
                    FeedReaderContract.반복내역테이블.COLUMN_DATE + " TEXT, " +
                    " PRIMARY KEY( pk AUTOINCREMENT) ); ";


    private static final String createDefaultBudgetTable =
            "CREATE TABLE if not exists " + FeedReaderContract.기본예산테이블.TABLE_NAME + " (" +
                    FeedReaderContract.기본예산테이블.COLUMN_PK + " INTEGER ," +
                    FeedReaderContract.기본예산테이블.COLUMN_AMOUNT + " INTEGER DEFAULT -1," +
                    FeedReaderContract.기본예산테이블.COLUMN_CATEGORY_PK + " INTEGER, " +
                    " PRIMARY KEY( pk AUTOINCREMENT) ); ";

    private static final String createBudgetTable =
            "CREATE TABLE if not exists " + FeedReaderContract.일반예산테이블.TABLE_NAME + " (" +
                    FeedReaderContract.일반예산테이블.COLUMN_PK + " INTEGER ," +
                    FeedReaderContract.일반예산테이블.COLUMN_AMOUNT + " INTEGER," +
//                    FeedReaderContract.예산테이블.COLUMN_BUDGET_AMOUNT + " INTEGER DEFAULT -1," +
                    FeedReaderContract.일반예산테이블.COLUMN_YEAR + " INTEGER, " +
                    FeedReaderContract.일반예산테이블.COLUMN_MONTH + " INTEGER, " +
                    FeedReaderContract.일반예산테이블.COLUMN_CATEGORY_PK + " INTEGER, " +
                    " PRIMARY KEY( pk AUTOINCREMENT) ); ";


    private static final String createAccountBookTable =
            "CREATE TABLE if not exists " + FeedReaderContract.가계부내역.TABLE_NAME + " (" +
                    FeedReaderContract.가계부내역.COLUMN_PK + " INTEGER ," +
                    FeedReaderContract.가계부내역.COLUMN_TYPE + " INTEGER," +
                    FeedReaderContract.가계부내역.COLUMN_ASSET_PK + " INTEGER, " +
                    FeedReaderContract.가계부내역.COLUMN_CATEGORY_PK + " INTEGER, " +
                    FeedReaderContract.가계부내역.COLUMN_AMOUNT + " INTEGER ," +
                    FeedReaderContract.가계부내역.COLUMN_DATE + " TEXT," +
                    FeedReaderContract.가계부내역.COLUMN_DEPOSIT + " INTEGER, " +
                    FeedReaderContract.가계부내역.COLUMN_WITHDRAW + " INTEGER, " +
                    FeedReaderContract.가계부내역.COLUMN_ROUTINETYPEs + " INTEGER, " +
                    FeedReaderContract.가계부내역.COLUMN_CONTENT + " TEXT ," +
                    FeedReaderContract.가계부내역.COLUMN_MEMO + " TEXT ," +
                    FeedReaderContract.가계부내역.COLUMN_IMAGE1 + " TEXT," +
                    FeedReaderContract.가계부내역.COLUMN_IMAGE2 + " TEXT, " +
                    FeedReaderContract.가계부내역.COLUMN_IMAGE3 + " TEXT, " +
                    " PRIMARY KEY( pk AUTOINCREMENT) ); ";


//
//            = "CREATE TABLE if not exists asset3 (" +
//            " pk INTEGER NOT NULL, " +
//            " name TEXT NOT NULL, " +
////            " enable INTEGER DEFAULT 0, " +
////            " deletedate TEXT); " +
//            " PRIMARY KEY( pk AUTOINCREMENT) " +
//            ");";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;



    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // table 생성,(없다면), 기본 값 추가.
    public void init(SQLiteDatabase db){

        db.execSQL(createDataTable);


        ContentValues values10 = new ContentValues();
        values10.put(FeedReaderContract.데이터테이블.COLUMN_NAME,  "자산 수정");
        values10.put(FeedReaderContract.데이터테이블.COLUMN_TYPE, Data.카테고리);
        values10.put(FeedReaderContract.데이터테이블.COLUMN_ENABLE, Data.비활성화);

        // Insert the new row, returning the primary key value of the new row
        long newRowId10 = db.insert(FeedReaderContract.데이터테이블.TABLE_NAME, null, values10);

        Log.i("","newRowId : "+ Long.toString(newRowId10));
        if(newRowId10 == -1){
            Log.i("","자산 기본값 insert 실패");
            Log.i("","자산 수정");

            return;
        }



        if(행수가져오기(db,FeedReaderContract.데이터테이블.TABLE_NAME) == 1){

            Log.i("asset 행 수 0","");

            String[] array = new String[4];
            array[0] = "현금";
            array[1] = "은행";
            array[2] = "카드";
            array[3] = "투자";


            for(int i=0;i<array.length;i++){
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.데이터테이블.COLUMN_NAME, array[i]);
                values.put(FeedReaderContract.데이터테이블.COLUMN_TYPE, Data.자산);

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(FeedReaderContract.데이터테이블.TABLE_NAME, null, values);

                Log.i("","newRowId : "+ Long.toString(newRowId));
                if(newRowId == -1){
                    Log.i("","자산 기본값 insert 실패");
                    Log.i("",array[i]);

                    return;
                }

            }

            String[] array2 = new String[13];
            array2[0] = "식료품,비주류음료";
            array2[1] = "주류,담배";
            array2[2] = "의류,신발";
            array2[3] = "주거,수도,광열";
            array2[4] = "가정용품,가사서비스";
            array2[5] = "보건";
            array2[6] = "교통";
            array2[7] = "통신";
            array2[8] = "오락,문화";
            array2[9] = "교육";
            array2[10] = "음식,숙박";
            array2[11] = "기타상품,서비스";
            array2[12] = "비소비지출";


            for(int i=0;i<array2.length;i++){


                ContentValues values2 = new ContentValues();
                values2.put(FeedReaderContract.데이터테이블.COLUMN_NAME, array2[i]);
                values2.put(FeedReaderContract.데이터테이블.COLUMN_TYPE, Data.통계청카테고리);

                // Insert the new row, returning the primary key value of the new row
                long newRowId2 = db.insert(FeedReaderContract.데이터테이블.TABLE_NAME, null, values2);

                Log.i("","newRowId : "+ Long.toString(newRowId2));
                if(newRowId2 == -1){
                    Log.i("","통계청지출카테고리테이블 기본값 insert 실패");
                    Log.i("",array[i]);

                    return;
                }


                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.데이터테이블.COLUMN_NAME, array2[i]);
                values.put(FeedReaderContract.데이터테이블.COLUMN_CATEGORY, Integer.parseInt(Long.toString(newRowId2)));
                values.put(FeedReaderContract.데이터테이블.COLUMN_TYPE, Data.카테고리);

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(FeedReaderContract.데이터테이블.TABLE_NAME, null, values);

                Log.i("","newRowId : "+ Long.toString(newRowId));
                if(newRowId == -1){
                    Log.i("","카테고리 기본값 insert 실패");
                    Log.i("",array[i]);
                    return;
                }

            }

            List<String> routines = new ArrayList<>();

            routines.add("없음");
            routines.add("매일");
            routines.add("주중");
            routines.add("주말");
            routines.add("매주");
            routines.add("2주마다");
            routines.add("4주마다");
            routines.add("매월");
            routines.add("월말");
            routines.add("2개월마다");
            routines.add("3개월마다");
            routines.add("4개월마다");
            routines.add("6개월마다");
            routines.add("1년마다");


            for(int i=0;i<routines.size();i++){
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.데이터테이블.COLUMN_NAME, routines.get(i));
                values.put(FeedReaderContract.데이터테이블.COLUMN_TYPE, Data.반복);

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(FeedReaderContract.데이터테이블.TABLE_NAME, null, values);

                Log.i("","newRowId : "+ Long.toString(newRowId));
                if(newRowId == -1){
                    Log.i("","반복 기본값 insert 실패");
                    Log.i("",array[i]);

                    return;
                }

            }

        }

        db.execSQL(createAccountBookTable);
        db.execSQL(createRoutineTable);

        db.execSQL(createDefaultBudgetTable);
        db.execSQL(createBudgetTable);


    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // 삭제 메서드
    public void deleteRecord(String TABLE_NAME, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,  "pk = ?", new String[]{String.valueOf(id)});
//        db.close();
    }
    public int 행수가져오기(SQLiteDatabase sqlDB,String tableName){

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM "+tableName+";",null);
        int 행갯수 = cursor.getCount();
        Log.i("행갯수 : ", Integer.toString(행갯수) );
        cursor.close();
        return 행갯수;

    }


    public boolean 데이터가있나요(SQLiteDatabase sqlDB,String query){
        Cursor cursor;
        cursor = sqlDB.rawQuery(query,null);

//        cursor = sqlDB.rawQuery("SELECT * FROM "+tableName+";",null);
        int 행갯수 = cursor.getCount();
        Log.i("행갯수 : ", Integer.toString(행갯수) );
        cursor.close();
        return 행갯수==0?false:true;
//        return 행갯수;
    }

    public List<Data> 데이터요청(SQLiteDatabase sqlDB,String sql){

        Log.i("데이터","가져오기");
        List<Data> 데이터 = new ArrayList<>();

        Cursor cursor;
        cursor = sqlDB.rawQuery(sql,null);

//        cursor.
        while (cursor.moveToNext()){

            Log.i("데이터","가져오기");
            Data 자산 = new Data();
            자산.pk =  cursor.getInt(0);
            자산.type = cursor.getInt(1);
            자산.name = cursor.getString(2);
            자산.office_category = cursor.getInt(4);
            자산.enable = cursor.getInt(5);
            데이터.add(자산);
//            strNames += cursor.getString(0) + "\r\n";
//                    strNumbers += cursor.getString(1) + "\r\n";
        }
//        button.setText(strNames);
//                edtNumberResult.setText(strNumbers);
        cursor.close();
//        sqlDB.close();

        return 데이터;
    }

    public long 데이터입력하기(SQLiteDatabase sqlDB, String tableName, ContentValues values){


        // Insert the new row, returning the primary key value of the new row
        long newRowId = sqlDB.insert(tableName, null, values);
        return newRowId;

    }

    public void 데이터수정하기(SQLiteDatabase sqlDB, String tableName,String pk,ContentValues values){

        // and we are comparing it with name of our course which is stored in original name variable.
        sqlDB.update(tableName, values, "pk=?", new String[]{pk});
//        db.close();
    }

    public List<defaultBudget> 기본예산데이터요청(SQLiteDatabase db){

//        String 자산데이터가져오기 = "SELECT * FROM "+FeedReaderContract.데이터테이블.TABLE_NAME+" where type="+Integer.toString(Data.자산)+";";

        String query = "SELECT * FROM "+FeedReaderContract.기본예산테이블.TABLE_NAME +";";
        List<defaultBudget> 데이터 = new ArrayList<>();

        Cursor cursor;
        cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){

            Log.i("데이터","가져오기");
            defaultBudget budget = new defaultBudget();
            budget.pk =  cursor.getInt(0);
            budget.예산 = cursor.getInt(1);
            budget.카테고리식별자 = cursor.getInt(2);
         
            데이터.add(budget);
//            strNames += cursor.getString(0) + "\r\n";
//                    strNumbers += cursor.getString(1) + "\r\n";
        }
//        button.setText(strNames);
//                edtNumberResult.setText(strNumbers);
        cursor.close();

        return 데이터;

    }
    public List<defaultBudget> 일반예산데이터요청(SQLiteDatabase db,String query){

//        String 자산데이터가져오기 = "SELECT * FROM "+FeedReaderContract.데이터테이블.TABLE_NAME+" where type="+Integer.toString(Data.자산)+";";

//        String query = "SELECT * FROM "+FeedReaderContract.일반예산테이블.TABLE_NAME
//                +" where "+FeedReaderContract.일반예산테이블.COLUMN_YEAR+"="+Integer.toString(year)
//                +"AND "+FeedReaderContract.일반예산테이블.COLUMN_MONTH+"="+Integer.toString(month)+";";


        List<defaultBudget> 데이터 = new ArrayList<>();

        Cursor cursor;
        cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){

            Log.i("데이터","가져오기");
            defaultBudget budget = new defaultBudget();
            budget.pk =  cursor.getInt(0);
            budget.예산 = cursor.getInt(1);
            budget.카테고리식별자 = cursor.getInt(4);

            데이터.add(budget);
//            strNames += cursor.getString(0) + "\r\n";
//                    strNumbers += cursor.getString(1) + "\r\n";
        }
//        button.setText(strNames);
//                edtNumberResult.setText(strNumbers);
        cursor.close();

        return 데이터;

    }

    public List<defaultBudget> 일반예산데이터요청(SQLiteDatabase db,int year, int month){

//        String 자산데이터가져오기 = "SELECT * FROM "+FeedReaderContract.데이터테이블.TABLE_NAME+" where type="+Integer.toString(Data.자산)+";";

        String query = "SELECT * FROM "+FeedReaderContract.일반예산테이블.TABLE_NAME
                +" where "+FeedReaderContract.일반예산테이블.COLUMN_YEAR+"="+Integer.toString(year)
                +"AND "+FeedReaderContract.일반예산테이블.COLUMN_MONTH+"="+Integer.toString(month)+";";


        List<defaultBudget> 데이터 = new ArrayList<>();

        Cursor cursor;
        cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){

            Log.i("데이터","가져오기");
            defaultBudget budget = new defaultBudget();
            budget.pk =  cursor.getInt(0);
            budget.예산 = cursor.getInt(1);
            budget.카테고리식별자 = cursor.getInt(4);

            데이터.add(budget);
//            strNames += cursor.getString(0) + "\r\n";
//                    strNumbers += cursor.getString(1) + "\r\n";
        }
//        button.setText(strNames);
//                edtNumberResult.setText(strNumbers);
        cursor.close();

        return 데이터;

    }
    
//    public List<Budget> 예산데이터요청(SQLiteDatabase sqlDB, String sql){
//
//
//        Log.i("데이터","가져오기");
//        List<Budget> 데이터 = new ArrayList<>();
//
//        Cursor cursor;
//        cursor = sqlDB.rawQuery(sql,null);
//
////        cursor.
//        while (cursor.moveToNext()){
//
//            Log.i("데이터","가져오기");
//            Budget 자산 = new Budget();
//            자산.pk =  cursor.getInt(0);
//            int type = cursor.getInt(1);
//            int 예산 = cursor.getInt(2);
//            if(type == -1){
//                자산.기본예산 = 예산;
//            }
//            else{
//                자산.일반예산 = 예산;
////                자산.date.year = cursor.getInt(3);
////                자산.date.month = cursor.getInt(4);
//            }
//
//            자산.카테고리식별자 = cursor.getInt(5);
//            데이터.add(자산);
////            strNames += cursor.getString(0) + "\r\n";
////                    strNumbers += cursor.getString(1) + "\r\n";
//        }
////        button.setText(strNames);
////                edtNumberResult.setText(strNumbers);
//        cursor.close();
////        sqlDB.close();
//
//        return 데이터;
//    }


}

