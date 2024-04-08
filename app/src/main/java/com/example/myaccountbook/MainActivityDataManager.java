package com.example.myaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Formatter;
import com.example.myaccountbook.data.MainData;
import com.example.myaccountbook.data.QueryManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivityDataManager {

    private SQLiteDatabase readDb;

    MainActivityDataManager(SQLiteDatabase readDb){

        this.readDb = readDb;

    }

    MainData 데이터반환(Date 날짜){

        MainData mainData = new MainData();

        수입지출데이터가져오기(mainData, 날짜);
        Log.i("수입",mainData.총수입가져오기());
        Log.i("고정 지출",mainData.총정기지출가져오기());
        Log.i("변동 지출",mainData.총변동지출가져오기());
        Log.i("지출",mainData.총지출가져오기());

        예산데이터가져오기(mainData, 날짜);
        Log.i("전체 예산",mainData.전체예산가져오기());

        return mainData;

    }

    void 수입지출데이터가져오기(MainData data,Date 날짜){

        String 수입지출데이터가져오기쿼리문 = QueryManager.월간수입지출총액가져오기(날짜);
        Log.i("query",수입지출데이터가져오기쿼리문);

        Cursor cursor = readDb.rawQuery(수입지출데이터가져오기쿼리문,null);

        int 수입 =0, 고정지출=0, 변동지출=0;
        Log.i("cursor.getCount",Integer.toString(cursor.getCount()));

        while(cursor.moveToNext()){

            int sum = cursor.getInt(0);
            int type = cursor.getInt(2);

            if(type == accountBook.수입){
                수입 = sum;
            } else if (type == accountBook.고정지출) {
                고정지출 = sum;
            }
            else if (type == accountBook.변동지출) {
                변동지출 = sum;
            }
        }
        Log.i("수입",Integer.toString(수입));
        Log.i("고정 지출",Integer.toString(고정지출));
        Log.i("변동 지출",Integer.toString(변동지출));

        data.setData(수입,고정지출,변동지출);
        cursor.close();

    }

    void 예산데이터가져오기(MainData data,Date 날짜){

        String query = QueryManager.월간일반예산_전체예산가져오기(날짜);
        Log.i("query",query);

        Cursor cursor = readDb.rawQuery(query,null);

        if(!전체예산설정(cursor,data)){

            query = QueryManager.월간기본예산_전체예산가져오기();
            Log.i("query",query);

            cursor = readDb.rawQuery(query,null);
            if(!전체예산설정(cursor,data)) {

                query = QueryManager.기본예산에서일반예산미설정카테고리들의예산총합가져오기(날짜);
                Log.i("query",query);
                cursor = readDb.rawQuery(query,null);
                전체예산설정(cursor,data);

                query = QueryManager.월간일반예산총합가져오기(날짜);
                Log.i("query",query);
                cursor = readDb.rawQuery(query,null);
                전체예산설정(cursor,data);

            }

        }

    }

    private boolean 전체예산설정(Cursor cursor, MainData data){

        if(cursor.getCount() == 0){
            Log.i("cursor.getCount == 0","cursor.getCount == 0");

            return false;
        }
        Log.i("cursor.getCount",Integer.toString(cursor.getCount()));

        while(cursor.moveToNext()){

            int sum = cursor.getInt(0);

            data.addData(sum);
            Log.i("addData(sum)",Integer.toString(sum));

        }

        cursor.close();
        return true;
    }

    List<accountBook> 내역반환(Date 날짜, int type,MyApplication application){


        String query = QueryManager.내역가져오기(날짜,type);
        Log.i("query",query);

        Cursor cursor = readDb.rawQuery(query,null);

        Log.i("cursor.getCount",Integer.toString(cursor.getCount()));
        List<accountBook> 내역 = 내역데이터파싱(cursor,application);

        return 내역;

    }



    List<accountBook> 내역데이터파싱(Cursor cursor,MyApplication application){

        List<accountBook> list = new ArrayList<>();

        while(cursor.moveToNext()){

            accountBook 내역 = new accountBook();
            int pk = cursor.getInt(0);
            int type = cursor.getInt(1);
            int 자산pk = cursor.getInt(2);
            int 카테고리pk = cursor.getInt(3);
            int 금액 = cursor.getInt(4);


            String 날짜 = cursor.getString(5);
            int 입금자산pk = cursor.getInt(6);
            int 출금자산pk = cursor.getInt(7);
            int routine = cursor.getInt(8);

            String content = cursor.getString(9);
            String memo = cursor.getString(10);
            String image1 = cursor.getString(11);
            String image2 = cursor.getString(12);
            String image3 = cursor.getString(13);

            내역.pk = pk;
            내역.type = type;
            내역.금액 = 금액;
            내역.날짜 = 날짜;
            내역.입금자산식별자 = 입금자산pk;
            내역.출금자산식별자 = 출금자산pk;
            내역.자산식별자 = 자산pk;
            내역.카테고리식별자 = 카테고리pk;
            내역.내용 = content;
            내역.메모 = memo;
            내역.이미지1 = image1;
            내역.이미지2 = image2;
            내역.이미지3 = image3;

            내역.date = 내역.parseISODate(내역.날짜);

            내역.금액원 = Formatter.천단위구분(내역.금액);

//            list.add(내역);
            list.add(application.치환(내역));

        }

        return list;

    }

}

