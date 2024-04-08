package com.example.myaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myaccountbook.data.Amount;
import com.example.myaccountbook.data.Formatter;
import com.example.myaccountbook.data.QueryManager;
import com.example.myaccountbook.data.자산통계데이터;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssetDataManager {


    // 년_월_자산식별자 ( key ) : 객체 ( value )

    private List<String> 년월목록;
    private HashMap<String, List<accountBook>> 내역목록;
    private HashMap<String, 합계> 합계목록;

    private HashMap<String, Integer> 이체금액목록;

    private List<자산> 자산목록;

    private List<자산> 미삭제자산목록;


    SQLiteDatabase readDb;

    // 객체 생성
    AssetDataManager(SQLiteDatabase readDb){

        print("AssetDataManager 객체 생성");

        내역목록 = new HashMap<>();
        합계목록 = new HashMap<>();
        자산목록 = new ArrayList<>();
        년월목록 = new ArrayList<>();
        미삭제자산목록 = new ArrayList<>();
        이체금액목록 = new HashMap<>();

        this.readDb = readDb;
        print("내역목록 합계목록 자산목록 년월목록 미삭제자산목록 이체금액목록 객체 생성");

    }

    Amount 누적잔액가져오기(Cursor cursor){

        Amount amount = new Amount();

        while(cursor.moveToNext()){

           int type = cursor.getInt(0);
           int sum = cursor.getInt(1);

           if(type == accountBook.수입){
               amount.amount += sum;
           } else if (type == accountBook.고정지출
                   || type == accountBook.변동지출) {
               amount.amount -= sum;
           }

        }
        return amount;

    }

    public List<자산통계데이터> 전체통계데이터목록반환
            (com.example.myaccountbook.data.Date date){

        List<자산통계데이터> list = new ArrayList<>();
        com.example.myaccountbook.data.Date 날짜X = date.clone(date);

        for(int i=0;i<6;i++){
            자산통계데이터 전체통계 = 전체통계데이터반환(날짜X);
            list.add(0,전체통계);
            날짜X.이전달로이동();
        }

        return list;
    }

    public 자산통계데이터 전체통계데이터반환
            (com.example.myaccountbook.data.Date date){


        자산통계데이터 전체통계 = new 자산통계데이터();
        // 시작날짜까지의 누적날짜

        // 시작날짜의 다음날짜의 누적날짜
        // 총 6개 구하기

        전체통계.월 = date.getMonth();

        // 자산목록, 미삭제자산목록에 추가.

        print("자산 데이터 가져오기");
        print("매개변수 SQLiteDatabase isOpen : ", String.valueOf(readDb.isOpen()));

        // query : select * from (데이터 table) where type = 0;
        String query = QueryManager.월간전체통계누적수입지출가져오기(date);
        print("query ", query);

        // 가계부 내역 데이터 가져오기 cursor
        Cursor cursor = readDb.rawQuery(query,null);
        print("자산 데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));


//            자산데이터파싱및자산데이터값할당(cursor);
        Amount 누적잔액 = 누적잔액가져오기(cursor);
        전체통계.누적잔액 = 누적잔액;
        cursor.close();
        print("월간전체통계누적수입지출가져오기 가져오기 완료");


        query = QueryManager.월간전체통계수입지출가져오기(date);
        print("query ", query);

        // 가계부 내역 데이터 가져오기 cursor
        cursor = readDb.rawQuery(query,null);
        print("자산 데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        while(cursor.moveToNext()){

            int type = cursor.getInt(0);
            int sum = cursor.getInt(1);

            if(type == accountBook.수입){
                전체통계.총수입.amount += sum;
            } else if (type == accountBook.고정지출
                    || type == accountBook.변동지출) {
                전체통계.총지출.amount += sum;
            }

        }

        cursor.close();


        return 전체통계;

    }


//    public List<FullStatisticsActivity.전체통계> 전체통계데이터반환(com.example.myaccountbook.data.Date date){
//
//
//        List<FullStatisticsActivity.전체통계> list = new ArrayList<>();
//        com.example.myaccountbook.data.Date 날짜 = com.example.myaccountbook.data.Date.이전날짜(date,5);
//
//
//        for(int i=0;i<6;i++){
////            print("")
//
//            FullStatisticsActivity.전체통계 전체통계 = new FullStatisticsActivity.전체통계();
//            // 시작날짜까지의 누적날짜
//
//            // 시작날짜의 다음날짜의 누적날짜
//            // 총 6개 구하기
//
//            전체통계.월 = 날짜.month;
//
//            String 년월 = Integer.toString(날짜.year*100+날짜.month);
//
//            if(년월목록.size() == 0){
//                list.add(전체통계);
//                continue;
//            }
//
//            int j=0;
//            while (j<년월목록.size() && Integer.parseInt(년월) >= Integer.parseInt(년월목록.get(j))){
//
//                String 지역변수년월 = 년월목록.get(j);
//                print("지역변수년월",지역변수년월);
//
////                전체통계.누적잔액.amount +=
//                // 시작날짜의 모든 내역의 총 수입, 총 지출
//                for(int p=0;p<미삭제자산목록.size();p++){
//
//                    자산 자산 = 미삭제자산목록.get(p);
//                    int 자산식별자 = 자산.식별자;
//
//                    String key = 지역변수년월+Integer.toString(자산식별자);
//                    print("key",key);
//
//                    if(합계목록.containsKey(key)){
//                        합계 합계 = 합계목록.get(key);
////                합계.
//
//                        print("전체통계.누적잔액.amount",Integer.toString(전체통계.누적잔액.amount));
//
//
//                        print("합계.총수입.amount",Integer.toString(합계.총수입.amount));
//                        print("합계.총지출.amount",Integer.toString(합계.총지출.amount));
//                        전체통계.누적잔액.amount += 합계.총수입.amount;
//                        전체통계.누적잔액.amount -= 합계.총지출.amount;
//
//                        print("전체통계.누적잔액.amount",Integer.toString(전체통계.누적잔액.amount));
//                    }
//
//                }
//
//                j++;
//
//            }
//
////            print("o1",o1);
////            print("o2",o2);
////
////            int value = Integer.parseInt(o1) - Integer.parseInt(o2);
////            print("01-02",Integer.toString(value));
//
//
//
//
//            // 시작날짜의 모든 내역의 총 수입, 총 지출
//            for(int p=0;p<자산목록.size();p++){
//
//                자산 자산 = 자산목록.get(p);
//                int 자산식별자 = 자산.식별자;
//
//                String key = Integer.toString(날짜.year*100+날짜.month)+Integer.toString(자산식별자);
//                print("key",key);
//
//
//                if(합계목록.containsKey(key)){
//                    합계 합계 = 합계목록.get(key);
////                합계.
//
//                    print("전체통계.총수입.amount",Integer.toString(전체통계.총수입.amount));
//                    print("전체통계.총지출.amount",Integer.toString(전체통계.총지출.amount));
//
//                    print("합계.총수입.amount",Integer.toString(합계.총수입.amount));
//                    print("합계.총지출.amount",Integer.toString(합계.총지출.amount));
//
//                    전체통계.총수입.amount += 합계.총수입.amount;
//                    전체통계.총지출.amount += 합계.총지출.amount;
//
//                    print("전체통계.총수입.amount",Integer.toString(전체통계.총수입.amount));
//                    print("전체통계.총지출.amount",Integer.toString(전체통계.총지출.amount));
//
//                }
//
//            }
//
//            if(이체금액목록.containsKey(Integer.toString(날짜.year*100+날짜.month))){
//                print("이체금액 있음.");
//                int 이체금액 = 이체금액목록.get(Integer.toString(날짜.year*100+날짜.month));
//                print("이체금액",Integer.toString(이체금액));
//
//                print("전체통계.총수입.amount",Integer.toString(전체통계.총수입.amount));
//                print("전체통계.총지출.amount",Integer.toString(전체통계.총지출.amount));
//
//
//                전체통계.총수입.amount -= 이체금액;
//                전체통계.총지출.amount -= 이체금액;
//
//
//            }
//
//            print("전체통계.총수입.amount",Integer.toString(전체통계.총수입.amount));
//            print("전체통계.총지출.amount",Integer.toString(전체통계.총지출.amount));
//
//            list.add(전체통계);
//
//            날짜 = com.example.myaccountbook.data.Date.이후날짜(날짜);
//
//        }
//
//        return list;
//
//    }
//

    public List<AssetDetailActivity.통계> 자산통계데이터목록(com.example.myaccountbook.data.Date date,int 자산식별자){

        List<AssetDetailActivity.통계> list = new ArrayList<>();
//        com.example.myaccountbook.data.Date 날짜 = com.example.myaccountbook.data.Date.이전날짜(date,5);
        com.example.myaccountbook.data.Date 날짜 =  date.clone(date);
        날짜.이전달로이동(5);


        for(int i=0;i<6;i++){
//            print("")

            AssetDetailActivity.통계 통계 = new AssetDetailActivity.통계();


            자산상세공통데이터 자산상세공통데이터 = 자산상세공통데이터(날짜,자산식별자);
            통계.월 = 날짜.getMonth();

            통계.총수입.amount = 자산상세공통데이터.입금;
            통계.총지출.amount = 자산상세공통데이터.출금;

            통계.누적잔액.amount = 자산상세공통데이터.누적잔액;


//            입금출금합계데이터가져오기

            list.add(통계);

            날짜.다음달로이동();
//            날짜 = com.example.myaccountbook.data.Date.이후날짜(날짜);

        }

        return list;


    }

    public AssetDetailActivity.통계 자산통계데이터반환(com.example.myaccountbook.data.Date date,int 자산식별자){


        AssetDetailActivity.통계 통계 = new AssetDetailActivity.통계();
        // 시작날짜까지의 누적날짜

        // 시작날짜의 다음날짜의 누적날짜
        // 총 6개 구하기

        통계.월 = date.getMonth();

        // 자산목록, 미삭제자산목록에 추가.

        print("자산 데이터 가져오기");
        print("매개변수 SQLiteDatabase isOpen : ", String.valueOf(readDb.isOpen()));

        // query : select * from (데이터 table) where type = 0;
        String query = QueryManager.월간통계누적수입지출가져오기(date,자산식별자);
        print("query ", query);

        // 가계부 내역 데이터 가져오기 cursor
        Cursor cursor = readDb.rawQuery(query,null);
        print("자산 데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));


//            자산데이터파싱및자산데이터값할당(cursor);
//        Amount 누적잔액 = 누적잔액가져오기(cursor);

        Amount 누적잔액 = new Amount();

        while(cursor.moveToNext()){

            int type = cursor.getInt(0);
            int sum = cursor.getInt(1);
            int 입금식별자 = cursor.getInt(2);
            int 출금식별자 = cursor.getInt(3);


            if(type == accountBook.수입){
                누적잔액.amount += sum;
            } else if (type == accountBook.고정지출 || type == accountBook.변동지출) {
                누적잔액.amount -= sum;
            }
            else if(type == accountBook.이체){

                if(자산식별자 == 입금식별자){
                    누적잔액.amount += sum;

                }
                if(자산식별자 == 출금식별자){
                    누적잔액.amount -= sum;

                }
            }
        }

        통계.누적잔액 = 누적잔액;
        cursor.close();
        print("월간 통계누적수입지출가져오기 가져오기 완료");


        query = QueryManager.월간통계수입지출가져오기(date,자산식별자);
        print("query ", query);

        // 가계부 내역 데이터 가져오기 cursor
        cursor = readDb.rawQuery(query,null);
        print("자산 데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        while(cursor.moveToNext()){

            int type = cursor.getInt(0);
            int sum = cursor.getInt(1);
            int 입금식별자 = cursor.getInt(2);
            int 출금식별자 = cursor.getInt(3);


            print("type",Integer.toString(type));
            print("sum",Integer.toString(sum));


            if(type == accountBook.수입){
                통계.총수입.amount += sum;
            } else if (type == accountBook.고정지출 || type == accountBook.변동지출) {
                통계.총지출.amount += sum;
            }
            else if(type == accountBook.이체){

                if(자산식별자 == 입금식별자){
                    통계.총수입.amount += sum;

                }
                if(자산식별자 == 출금식별자){
                    통계.총지출.amount += sum;

                }
            }

        }

        cursor.close();


        return 통계;

    }
    
    public List<자산통계데이터> 자산통계데이터반환(com.example.myaccountbook.data.Date date, int 자산식별자,boolean result){


        List<자산통계데이터> list = new ArrayList<>();

        com.example.myaccountbook.data.Date 날짜 =  date.clone(date);
        날짜.이전달로이동(5);

//        com.example.myaccountbook.data.Date 날짜 = com.example.myaccountbook.data.Date.이전날짜(date,5);


        for(int i=0;i<6;i++){
//            print("")

            자산통계데이터 전체통계 = new 자산통계데이터();
            // 시작날짜까지의 누적날짜

            // 시작날짜의 다음날짜의 누적날짜
            // 총 6개 구하기

            전체통계.월 = 날짜.getMonth();

            String 년월 = Integer.toString(날짜.getYear()*100+날짜.getMonth());

            if(년월목록.size() == 0){
                list.add(전체통계);
                continue;
            }

            int j=0;
            while (j<년월목록.size() && Integer.parseInt(년월) >= Integer.parseInt(년월목록.get(j))){

                String 지역변수년월 = 년월목록.get(j);
                print("지역변수년월",지역변수년월);


                String key = 지역변수년월+Integer.toString(자산식별자);
                print("key",key);

                if(합계목록.containsKey(key)){
                    합계 합계 = 합계목록.get(key);
//                합계.

                    print("전체통계.누적잔액.amount",Integer.toString(전체통계.누적잔액.amount));


                    print("합계.총수입.amount",Integer.toString(합계.총수입.amount));
                    print("합계.총지출.amount",Integer.toString(합계.총지출.amount));
                    전체통계.누적잔액.amount += 합계.총수입.amount;
                    전체통계.누적잔액.amount -= 합계.총지출.amount;

                    print("전체통계.누적잔액.amount",Integer.toString(전체통계.누적잔액.amount));
                }
                j++;

            }

//            print("o1",o1);
//            print("o2",o2);
//
            String key = Integer.toString(날짜.getYear()*100+날짜.getMonth())+Integer.toString(자산식별자);
            print("key",key);


            if(합계목록.containsKey(key)){
                합계 합계 = 합계목록.get(key);
//                합계.

                print("전체통계.총수입.amount",Integer.toString(전체통계.총수입.amount));
                print("전체통계.총지출.amount",Integer.toString(전체통계.총지출.amount));

                print("합계.총수입.amount",Integer.toString(합계.총수입.amount));
                print("합계.총지출.amount",Integer.toString(합계.총지출.amount));

                전체통계.총수입.amount += 합계.총수입.amount;
                전체통계.총지출.amount += 합계.총지출.amount;

                print("전체통계.총수입.amount",Integer.toString(전체통계.총수입.amount));
                print("전체통계.총지출.amount",Integer.toString(전체통계.총지출.amount));

            }



            print("전체통계.총수입.amount",Integer.toString(전체통계.총수입.amount));
            print("전체통계.총지출.amount",Integer.toString(전체통계.총지출.amount));

            list.add(전체통계);

            날짜.다음달로이동();

//            날짜 = com.example.myaccountbook.data.Date.이후날짜(날짜);

        }

        return list;

    }

    public 자산상세공통데이터 입금출금합계데이터가져오기(com.example.myaccountbook.data.Date date, int 자산식별자){

        자산상세공통데이터 data = new 자산상세공통데이터();

        print("자산 상세 공통 데이터 가져오기");
        print("매개변수 SQLiteDatabase isOpen : ", String.valueOf(readDb.isOpen()));

        String query = QueryManager.월특정자산의입금출금데이터가져오기(자산식별자, date);
        print("query ", query);

        // 입금, 출금, 합계, 누적잔액

        // 입금은 특정기간의 수입, 이체중 입금
        // 출금은 특정기간의 지출, 이체중 출금
        // 합계는 입금-출금
        // 누적잔액은 특정기간까지의 입금-출금

        Cursor cursor = readDb.rawQuery(query,null);
        print("데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        while(cursor.moveToNext()){

            int type = cursor.getInt(0);
            int 자산pk = cursor.getInt(1);
            int 입금식별자 = cursor.getInt(2);
            int 출금식별자 = cursor.getInt(3);
            int sum = cursor.getInt(4);

            if(type == accountBook.수입){
                data.입금 += sum;
            } else if (type == accountBook.고정지출 || type == accountBook.변동지출) {
                data.출금 += sum;
            }
            else if(type == accountBook.이체){

                if(자산식별자 == 입금식별자){
                    data.입금 += sum;

                }

                if(자산식별자 == 출금식별자){
                    data.출금 += sum;

                }

            }

        }

        cursor.close();

        print("월간전체통계누적수입지출가져오기 가져오기 완료");


        data.합계 = data.입금 - data.출금;
        // 이체의 입금식별자와 같으면 입금으로
        // 이체의 출금식별자와 같으면 출금으로 처리
        // 합계는 특정기간의 입금-출금

        return data;

    }

    int 자산상세_누적잔액가져오기(com.example.myaccountbook.data.Date date, int 자산식별자) {


        int 누적잔액 = 0;

        String query = QueryManager.월특정자산의누적잔액데이터가져오기(자산식별자, date);
        print("query ", query);

        // 입금, 출금, 합계, 누적잔액

        // 입금은 특정기간의 수입, 이체중 입금
        // 출금은 특정기간의 지출, 이체중 출금
        // 합계는 입금-출금
        // 누적잔액은 특정기간까지의 입금-출금

        Cursor cursor = readDb.rawQuery(query,null);
        print("데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        while(cursor.moveToNext()){

            int type = cursor.getInt(0);
            int 자산pk = cursor.getInt(1);
            int 입금식별자 = cursor.getInt(2);
            int 출금식별자 = cursor.getInt(3);
            int sum = cursor.getInt(4);

            if(type == accountBook.수입){
                누적잔액 += sum;
            } else if (type == accountBook.고정지출 || type == accountBook.변동지출) {
                누적잔액 -= sum;
            }
            else if(type == accountBook.이체){

                if(자산식별자 == 입금식별자){
                    누적잔액 += sum;
                }
                if(자산식별자 == 출금식별자){
                    누적잔액 -= sum;
                }
            }
        }
        cursor.close();

        return 누적잔액;

    }
    public 자산상세공통데이터 자산상세공통데이터(com.example.myaccountbook.data.Date date, int 자산식별자){

        자산상세공통데이터 data = 입금출금합계데이터가져오기(date,자산식별자);

        data.누적잔액 = 자산상세_누적잔액가져오기(date,자산식별자);

        return data;

    }

    public List<accountBook> 자산상세내역데이터(com.example.myaccountbook.data.Date date, int 자산식별자, MyApplication application){


        String query = QueryManager.월특정자산의내역데이터가져오기(자산식별자, date);
        print("query ", query);

        // 입금, 출금, 합계, 누적잔액

        // 입금은 특정기간의 수입, 이체중 입금
        // 출금은 특정기간의 지출, 이체중 출금
        // 합계는 입금-출금
        // 누적잔액은 특정기간까지의 입금-출금

        Cursor cursor = readDb.rawQuery(query,null);
        print("데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        List<accountBook> list = 내역데이터파싱(cursor,application);
        print("내역 데이터 갯수", Integer.toString(list.size()));

        return list;

    }


    public 자산상세공통데이터 자산상세공통데이터(com.example.myaccountbook.data.Date date, int 자산식별자,boolean result){

        자산상세공통데이터 data = new 자산상세공통데이터();


        String key = Integer.toString(date.getYear()*100+date.getMonth())+Integer.toString(자산식별자);
        if(합계목록.containsKey(key)){
            합계 합계 = 합계목록.get(key);
            data.입금 = 합계.총수입.amount;
            data.출금 = 합계.총지출.amount;
            data.합계 = data.입금 - data.출금;

        }

        if(년월목록.size() != 0){

            int j=0;
            while (j<년월목록.size() && (date.getYear()*100+date.getMonth()) >= Integer.parseInt(년월목록.get(j))){

                String 지역변수년월 = 년월목록.get(j);
                print("지역변수년월",지역변수년월);

//                전체통계.누적잔액.amount +=
                // 시작날짜의 모든 내역의 총 수입, 총 지출
                key = 지역변수년월+Integer.toString(자산식별자);
                print("key",key);

                if(합계목록.containsKey(key)){
                    합계 지역변수합계 = 합계목록.get(key);
//                합계.
                    data.누적잔액 += 지역변수합계.총수입.amount;
                    data.누적잔액 -= 지역변수합계.총지출.amount;

//                        print("전체통계.누적잔액.amount",Integer.toString(전체통계.누적잔액.amount));
//
//
//                        print("합계.총수입.amount",Integer.toString(합계.총수입.amount));
//                        print("합계.총지출.amount",Integer.toString(합계.총지출.amount));
//                        전체통계.누적잔액.amount += 합계.총수입.amount;
//                        전체통계.누적잔액.amount -= 합계.총지출.amount;
//
//                        print("전체통계.누적잔액.amount",Integer.toString(전체통계.누적잔액.amount));
                }

                j++;

            }

        }

        return data;

    }
//
//    public List<자산별합계> 자산별합계반환(){
//
//        print("자산별 합계 반환 함수 실행 시작");
//
//        List<자산별합계> list = new ArrayList<>();
//
//        print("list 생성");
//        print("미 삭제 자산 목록 크기", Integer.toString(미삭제자산목록.size()));
//
//
//        int 자산값 = 0,부채값 = 0,합계값 = 0;
//
//        for(int i=0;i<미삭제자산목록.size();i++){
//
//            print("반복",Integer.toString(i)+"번째");
//
//            자산별합계 자산별합계 = new 자산별합계();
//            print("자산별합계 객체 생성");
//
//            자산 자산 = 미삭제자산목록.get(i);
//            print("미삭제자산목록.get(i)",미삭제자산목록.get(i).자산명);
//            print("미삭제자산목록.get(i)",Integer.toString(미삭제자산목록.get(i).식별자));
//
//            자산별합계.자산 = 자산;
//            print("자산별합계.자산.자산명",자산별합계.자산.자산명);
//            print("자산별합계.자산.식별자",Integer.toString(자산별합계.자산.식별자));
//
//
//            for(int j=0;j<년월목록.size();j++){
//                print("년월목록 반복",Integer.toString(j));
//
//                String 년월 = 년월목록.get(j);
//                print("년월",년월);
//
//                String key = 년월+Integer.toString(자산.식별자);
//                print("key",key);
//                합계 합계 = 합계목록.get(key);
//
//                print("합계목록.containsKey()",Boolean.toString(합계목록.containsKey(key)));
//                if(합계목록.containsKey(key)){
//                    print("합계.총수입",Integer.toString(합계.총수입.amount));
//                    print("합계.총지출",Integer.toString(합계.총지출.amount));
//
//                    자산별합계.합계.amount += 합계.총수입.amount;
//                    자산별합계.합계.amount -= 합계.총지출.amount;
//
//                }
//
////                int 자산값 = 0,부채값 = 0,합계값 = 0;
//
//            }
//
//            print("자산별합계.합계.amount",Integer.toString(자산별합계.합계.amount));
//
//
//
//            if(자산별합계.합계.amount < 0){
//                부채값 += 자산별합계.합계.amount;
//            }
//            else{
//                자산값 += 자산별합계.합계.amount;
//            }
//
//            if(자산별합계.합계.amount < 0){
//                print("지출로 설정, amount * -1");
//                자산별합계.type = accountBook.지출;
//                자산별합계.합계.amount *= -1;
//            }
//            else if(자산별합계.합계.amount == 0){
//                print("이체로 설정");
//                자산별합계.type = accountBook.이체;
//            }
//            else{
//                print("수입으로 설정");
//                자산별합계.type = accountBook.수입;
//            }
//
//            print("자산별합계.합계.amount",Integer.toString(자산별합계.합계.amount));
//            list.add(자산별합계);
//
//        }
//자산별합계가져오기
//
//        print("--------------------------------------");
//        print("결과");
//        print("자산값",Integer.toString(자산값));
//        print("부채값",Integer.toString(부채값));
//        print("합계값",Integer.toString(자산값 + 부채값));
//
//        print("자산별 합계 반환 함수 실행 종료");
//
//        return list;
//
//    }

    class 자산별합계데이터{
        int type;
        int asset_pk;
        int withdraw;
        int deposit;
        int total_amount;

        자산별합계데이터(int type,int asset_pk,int withdraw,int deposit,int total_amount){
            this.type = type;
            this.asset_pk = asset_pk;
            this.withdraw = withdraw;
            this.deposit = deposit;
            this.total_amount = total_amount;

        }
    }

    List<자산별합계데이터> 자산별합계데이터가져오기(Cursor cursor) {


        List<자산별합계데이터> list = new ArrayList<>();

        while(cursor.moveToNext()){

            int type = cursor.getInt(0);
            int asset_pk = cursor.getInt(1);
            int withdraw = cursor.getInt(2);
            int deposit = cursor.getInt(3);
//            String name = cursor.getString(4);
            int total_amount = cursor.getInt(4);

            list.add(new 자산별합계데이터(type,asset_pk,withdraw,deposit,total_amount));

        }
//        print("년월 데이터 파싱 종료");
//
//
////        for(int i=0;i<list.size();i++){
////            String str = list.get(i);
////            print("년월목록 "+Integer.toString(i),str);
////        }
//
//        print("결과","년월목록 크기 : "+Integer.toString(년월목록.size()));


        return list;

    }
    HashMap<Integer,Integer> 자산별합계데이터목록가져오기(List<자산별합계데이터> list){
        HashMap<Integer,Integer> hashMap = new HashMap<>();

        for(int i=0;i<자산목록.size();i++){
            자산 자산 = 자산목록.get(i);
            hashMap.put(자산.식별자,0);
        }

        for(int i=0;i<list.size();i++){

            자산별합계데이터 자산별합계데이터 = list.get(i);

            if(자산별합계데이터.type == accountBook.이체){
                hashMap.replace(자산별합계데이터.deposit,hashMap.get(자산별합계데이터.deposit)+자산별합계데이터.total_amount);
                hashMap.replace(자산별합계데이터.withdraw,hashMap.get(자산별합계데이터.withdraw)-자산별합계데이터.total_amount);
            }
            else if(자산별합계데이터.type == accountBook.수입){
                hashMap.replace(자산별합계데이터.asset_pk,hashMap.get(자산별합계데이터.asset_pk)+자산별합계데이터.total_amount);
            }
            else{
                hashMap.replace(자산별합계데이터.asset_pk,hashMap.get(자산별합계데이터.asset_pk)-자산별합계데이터.total_amount);

            }

        }


        return hashMap;
    }
    List<자산별합계> 자산별합계가져오기(Cursor cursor){


        List<자산별합계> list = new ArrayList<>();

        List<자산별합계데이터> 자산별합계데이터목록 = 자산별합계데이터가져오기(cursor);
        HashMap<Integer,Integer> hashMap = 자산별합계데이터목록가져오기(자산별합계데이터목록);

        for(int i=0;i<미삭제자산목록.size();i++){

            자산별합계 자산별합계 = new 자산별합계();
            자산 자산 = 미삭제자산목록.get(i);

            자산별합계.자산 = 자산;
            자산별합계.합계.amount = hashMap.get(자산.식별자);
            자산별합계.type = 자산별합계.합계.amount > 0? accountBook.수입:accountBook.지출;

            list.add(자산별합계);

        }


        print("--------------------------------------");


        print("자산별합계가져오기 함수 실행 종료");
        return list;


    }

    public List<자산별합계> 자산별합계반환(SQLiteDatabase readDb){

        print("자산별 합계 반환 함수 실행 시작");

        print("DB 데이터 가져오기 시작");
        print("매개변수 SQLiteDatabase isOpen : ", String.valueOf(readDb.isOpen()));

        print("---------------------------------");
        print("DB의 가계부 테이블의 type, 자산, 입금자산, 출금자산별 합계 가져오기");

        // 년월목록 가져오기
//        SELECT DISTINCT date FROM accountbook ORDER BY date;
        String query = QueryManager.자산별합계가져오기();
        print("query ", query);

        // 가계부 내역 데이터 가져오기 cursor
        Cursor cursor = readDb.rawQuery(query,null);
        print("가계부 데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        List<자산별합계> list = 자산별합계가져오기(cursor);

        cursor.close();
        cursor = null;

        print("cursor 변수 null 할당(초기화)");
        print("---------------------------------");

        return list;

    }

    public void DB에서자산데이터가져오기(SQLiteDatabase readDb){
        //년월 정렬 가장 오래된 순으로 정렬
        // 내역목록 값들 최신순으로 정렬

        // 자산 데이터 가져오기
        // 활성화 데이터, 일반 데이터
        // 자산목록, 미삭제자산목록에 추가.

        print("자산 데이터 가져오기");
        print("매개변수 SQLiteDatabase isOpen : ", String.valueOf(readDb.isOpen()));

        // query : select * from (데이터 table) where type = 0;
        String query = "select * from "+FeedReaderContract.데이터테이블.TABLE_NAME +" where "+FeedReaderContract.데이터테이블.COLUMN_TYPE+"="+Data.자산;
        print("query ", query);

        // 가계부 내역 데이터 가져오기 cursor
        Cursor cursor = readDb.rawQuery(query,null);
        print("자산 데이터 요청 결과 받음");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        자산데이터파싱및자산데이터값할당(cursor);

        cursor.close();

        print("자산 데이터 가져오기 종료");
        print("가계부, 자산 데이터 가져오기 종료");
    }

//    public void DB에서날짜데이터가져오기및년월목록값할당(SQLiteDatabase readDb){
//
//        print("DB 데이터 가져오기 시작");
//        print("매개변수 SQLiteDatabase isOpen : ", String.valueOf(readDb.isOpen()));
//
//        print("---------------------------------");
//        print("DB의 가계부 테이블의 날짜 데이터 모두 가져오기");
//
//        // 년월목록 가져오기
////        SELECT DISTINCT date FROM accountbook ORDER BY date;
//        String query = "select DISTINCT "+FeedReaderContract.가계부내역.COLUMN_DATE+" from "+FeedReaderContract.가계부내역.TABLE_NAME+" ORDER BY "+FeedReaderContract.가계부내역.COLUMN_DATE;
//        print("query ", query);
//
//        // 가계부 내역 데이터 가져오기 cursor
//        Cursor cursor = readDb.rawQuery(query,null);
//        print("가계부 데이터 요청 결과 받음");
//        print("데이터 갯수", Integer.toString(cursor.getCount()));
//
//        년월데이터파싱및년월데이터값할당(cursor);
//
//        cursor.close();
//        cursor = null;
//
//        print("cursor 변수 null 할당(초기화)");
//        print("---------------------------------");
//
//
//
//    }
//
//    // 데이터 가져오기
//    // db에서 데이터 가져오기
//
//    public void DB에서데이터가져오기(SQLiteDatabase readDb){
//
//        print("DB 데이터 가져오기 시작");
//        print("매개변수 SQLiteDatabase isOpen : ", String.valueOf(readDb.isOpen()));
//
//        print("---------------------------------");
//        DB에서날짜데이터가져오기및년월목록값할당(readDb);
//        print("---------------------------------");
//
//
//
//
//        // query : select * from (가계부 table);
//        String query = "select * from "+FeedReaderContract.가계부내역.TABLE_NAME;
//        print("query ", query);
//
//        // 가계부 내역 데이터 가져오기 cursor
//        Cursor cursor = readDb.rawQuery(query,null);
//        print("가계부 데이터 요청 결과 받음");
//        print("데이터 갯수", Integer.toString(cursor.getCount()));
//
////        readDb.execSQL();
//        // 내역 데이터 account 클래스 만들기
//        List<accountBook> 내역데이터 = 내역데이터파싱(cursor);
//        print("내역 데이터 갯수", Integer.toString(내역데이터.size()));
//
//        cursor.close();
//        cursor = null;
//        print("cursor 닫기");
//
//
//
//        print("가계부 데이터를 AssetData로 Parsing 시작.");
//
//        for(int i=0;i<내역데이터.size();i++){
//
//            print("반복",Integer.toString(i)+"번쨰");
//            accountBook 가계부 = 내역데이터.get(i);
//            print("가계부 pk",Integer.toString(가계부.pk));
//
//            // 년월자산
//            // 년월목록 추가 (나중에 중복제거)
//
//            String 년월 = 년월(가계부.날짜);
//            print("년월 ", 년월);
//
//
//            if(가계부.type == accountBook.수입){
//
//                // 수입
//                // 내역목록에 추가 (객체 생성 유무 따로)
//                // 합계자산에 총수입 + (객체 생성 유무 따로)
//
//                print("가계부 수입 내역임");
//                String key = 년월자산식별자(년월, 가계부.자산식별자);
//
//                내역목록과합계목록KEY초기화(key);
//                수입입금내역저장(key, 가계부);
//
//            }
//            else if(가계부.type == accountBook.변동지출 || 가계부.type == accountBook.고정지출){
//
//                // 지출(변동, 고정)
//                // 내역목록에 추가 (객체 생성 유무 따로)
//                // 합계자산에 총지출 + (객체 생성 유무 따로)
//                print("가계부 지출 내역임");
//                String key = 년월자산식별자(년월, 가계부.자산식별자);
//
//                내역목록과합계목록KEY초기화(key);
//                지출출금내역저장(key, 가계부);
//
//            }
//            else if(가계부.type == accountBook.이체){
//                // 이체
//                // 내역목록에 입금자산으로 추가 (객체 생성 유무 따로)
//                // 내역목록에 출금자산으로 추가 (객체 생성 유무 따로)
//                // 합계자산에 입금자산으로 총수입 + (객체 생성 유무 따로)
//                // 합계자산에 출금자산으로 총지출 + (객체 생성 유무 따로)
//                print("가계부 이체 내역임");
//
//                String 입금자산key = 년월자산식별자(년월, 가계부.입금자산식별자);
//                String 출금자산key = 년월자산식별자(년월, 가계부.출금자산식별자);
//
//                내역목록과합계목록KEY초기화(입금자산key);
//                내역목록과합계목록KEY초기화(출금자산key);
//
//                수입입금내역저장(입금자산key, 가계부);
//                지출출금내역저장(출금자산key, 가계부);
//
//                if(!이체금액목록.containsKey(년월)){
//                    print("!이체금액목록.containsKey(년월)");
//                    이체금액목록.put(년월,0);
//                }
//                int 이체금액 = 이체금액목록.get(년월);
//
//                print("가계부 금액 ",Integer.toString(가계부.금액));
//                print("이체금액목록",년월 + " : "+Integer.toString(이체금액목록.get(년월)));
//
//                이체금액 += 가계부.금액;
//
////                이체금액목록.put(년월,이체금액);
//                이체금액목록.replace(년월,이체금액);
//                print("이체금액목록",년월 + " : "+Integer.toString(이체금액목록.get(년월)));
//
//            }
//            else{
//                print("문제 발생","가계부 type 알 수 없음.");
//                print("가계부 type : ",Integer.toString(가계부.type));
//            }
//
//            print("내역목록, 합계목록, 년월목록에 값 추가 완료");
//            // 내역목록, 합계목록, 년월목록에 값 추가.
//
//        }
//
//        print("AssetData Parsing 진행중","정렬, 중복 제거 작업 진행 시작");
//
//
//        // 내역목록 정렬
//        print("----------------------------------------");
//        for (String key: 내역목록.keySet()) {
//            print("key",key);
////            System.out.println("Key:" + key + ", Value:" + value);
//            List<accountBook> value = 내역목록.get(key);
//            print("value 크기",Integer.toString(value.size()));
//
//            print("정렬 전 출력");
//            for(int i=0;i<value.size();i++){
//                String str = value.get(i).날짜;
//                print("내역목록 "+Integer.toString(i),str);
//            }
//
//            print("내역목록 정렬 시작");
//            value.sort(new Comparator<accountBook>() {
//                @Override
//                public int compare(accountBook o1, accountBook o2) {
//
//                    Date o1date = o1.parseISODate(o1.날짜);
//                    Date o2date = o2.parseISODate(o2.날짜);
//
//                    print("o1",Long.toString(o1date.getTime()));
//                    print("o2",Long.toString(o2date.getTime()));
//
//                    long date = o2date.getTime()-o1date.getTime();
//                    print("02-01",Long.toString(date));
//
//                    return Integer.parseInt(Long.toString(date));
//
//                }
//            });
//
//            print("년월목록 정렬 끝");
//
//            print("정렬 후 출력");
//            for(int i=0;i<value.size();i++){
//                String str = value.get(i).날짜;
//                print("내역목록 "+Integer.toString(i),str);
//            }
//
//        }
//
//
//        print("----------------------------------------");
//
//        //년월 정렬 가장 오래된 순으로 정렬
//        // 내역목록 값들 최신순으로 정렬
//
//        // 자산 데이터 가져오기
//        // 활성화 데이터, 일반 데이터
//        // 자산목록, 미삭제자산목록에 추가.
//
//        print("자산 데이터 가져오기");
//        print("매개변수 SQLiteDatabase isOpen : ", String.valueOf(readDb.isOpen()));
//
//        // query : select * from (데이터 table) where type = 0;
//        query = "select * from "+FeedReaderContract.데이터테이블.TABLE_NAME +" where "+FeedReaderContract.데이터테이블.COLUMN_TYPE+"="+Data.자산;
//        print("query ", query);
//
//        // 가계부 내역 데이터 가져오기 cursor
//        cursor = readDb.rawQuery(query,null);
//        print("자산 데이터 요청 결과 받음");
//        print("데이터 갯수", Integer.toString(cursor.getCount()));
//
//        자산데이터파싱및자산데이터값할당(cursor);
//
//        cursor.close();
//
//        print("자산 데이터 가져오기 종료");
//        print("가계부, 자산 데이터 가져오기 종료");
//
//    }

    String 년월자산식별자(String 년월, int 자산식별자){

        print("년월자산식별자 글자 만들기 함수 시작");
        print("매개변수의 자산식별자 : ", Integer.toString(자산식별자));

        String key = 년월+Integer.toString(자산식별자);
        print("key",key);
        print("년월자산식별자 글자 만들기 함수 끝");

        return key;

    }

    String 년월(String 날짜){

        print("년월 글자 만들기 함수 시작");
        print("매개변수의 날짜 : ",날짜);
        String 년월 = 날짜.substring(0,4)+날짜.substring(5,7);
        print("년월 ", 년월);
        print("년월 글자 만들기 함수 끝");

        return 년월;

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

    void 년월데이터파싱및년월데이터값할당(Cursor cursor){
        print("년월 데이터 파싱 시작 ");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        년월목록.clear();

        print("년월목록 데이터 clear");

        while(cursor.moveToNext()){

            String 날짜 = cursor.getString(0);
            print("날짜",날짜);
            
            String 년월 = 년월(날짜);
            print("년월 값 추출", 년월);

            if(!년월목록.contains(년월)){
                print("년월목록에 없는 데이터(년월)");
                년월목록.add(년월);
                print("년월목록에 데이터(년월) 추가");
            }

        }

        print("년월 데이터 파싱 종료");


        for(int i=0;i<년월목록.size();i++){
            String str = 년월목록.get(i);
            print("년월목록 "+Integer.toString(i),str);
        }

        print("결과","년월목록 크기 : "+Integer.toString(년월목록.size()));

    }

    void 자산데이터파싱및자산데이터값할당(Cursor cursor){

        print("자산 데이터 파싱 시작 ");
        print("데이터 갯수", Integer.toString(cursor.getCount()));

        자산목록.clear();
        미삭제자산목록.clear();

        print("자산 목록, 미삭제 자산 목록 데이터 clear");

        while(cursor.moveToNext()){

            int pk = cursor.getInt(0);
            String 자산명 = cursor.getString(2);
            int enable = cursor.getInt(5);

            print("pk",Integer.toString(pk));
            print("자산명",자산명);
            print("enable",Integer.toString(enable));

            자산 자산 = new 자산();
            자산.식별자 = pk;
            자산.자산명 = 자산명;

            print("자산 객체 생성 및 값 할당");

            자산목록.add(자산);
            if(enable == Data.활성화){
                미삭제자산목록.add(자산);
            }

        }

        print("자산 데이터 파싱 종료");


    }


    void 수입입금내역저장(String key, accountBook 가계부){
        List<accountBook> list = 내역목록.get(key);
        print("내역목록 value 가져오기");
        print("내역목록 value size : ",Integer.toString(list.size()));

        list.add(가계부);
        print("내역목록 에 가계부 객체 추가");
        print("HashMap의 value size : ",Integer.toString(내역목록.get(key).size()));

        합계 합계 = 합계목록.get(key);
        print("합계목록 value 가져오기");
        print("가계부 금액",Integer.toString(가계부.금액));
        print("(더하기 전) 합계목록 총수입",Integer.toString(합계목록.get(key).총수입.amount));

        합계.총수입.amount += 가계부.금액;
        print("합계목록 총수입 += 가계부.금액 ");
        print("HashMap의 value 총수입 : ",Integer.toString(합계목록.get(key).총수입.amount));
    }
    void 지출출금내역저장(String key, accountBook 가계부){
        List<accountBook> list = 내역목록.get(key);
        print("내역목록 value 가져오기");
        print("내역목록 value size : ",Integer.toString(list.size()));

        list.add(가계부);
        print("내역목록 에 가계부 객체 추가");
        print("HashMap의 value size : ",Integer.toString(내역목록.get(key).size()));

        합계 합계 = 합계목록.get(key);
        print("합계목록 value 가져오기");
        print("가계부 금액",Integer.toString(가계부.금액));
        print("(더하기 전) 합계목록 총지출",Integer.toString(합계목록.get(key).총지출.amount));
        합계.총지출.amount += 가계부.금액;
        print("합계목록 총지출 += 가계부.금액 ");
        print("HashMap의 value 총수입 : ",Integer.toString(합계목록.get(key).총지출.amount));
    }
    void 내역목록과합계목록KEY초기화(String key){
        print("내역목록과합계목록KEY초기화 함수 시작");
        if(!내역목록.containsKey(key)){
            print("내역목록의 년월자산 key 값이 없습니다.");
            내역목록.put(key,new ArrayList<>());
            print("key, value 생성","put(key, new ArrayList<>())");
        }

        if(!합계목록.containsKey(key)){
            print("합계목록의 년월자산 key 값이 없습니다.");
            합계목록.put(key,new 합계());
            print("key, value 생성","put(key, new 합계())");
        }
        print("내역목록과합계목록KEY초기화 함수 끝");
    }

    // 데이터 정리하기
    void 데이터정리하기(){
        내역목록.clear();
        합계목록.clear();
        자산목록.clear();
        년월목록.clear();
        미삭제자산목록.clear();
    }

    class 합계{
        Amount 총수입;
        Amount 총지출;

        합계(){
            총수입 = new Amount();
            총지출 = new Amount();
            총수입.amount = 0;
            총지출.amount = 0;
        }
    }
    public static class 자산 {
        public int 식별자;
        public String 자산명;
    }

    public static class 자산별합계{
        public 자산 자산;
        public Amount 합계;
        public int type;

        자산별합계(){
            합계 = new Amount();
            합계.amount = 0;
        }
        자산별합계(int 금액, int 자산식별자, String 자산명){
            합계 = new Amount();
            합계.amount = 금액;
            자산 = new 자산();
            자산.자산명 = 자산명;
            자산.식별자 = 자산식별자;
        }
    }

    public class 자산상세공통데이터{
        public int 입금,출금,합계,누적잔액;
        자산상세공통데이터(){
            입금 = 0;
            출금 = 0;
            합계 = 0;
            누적잔액 = 0;
        }
    }

    boolean result = true;

    void print(String message){
        if(result)
        Log.i("AssetDataManager : ",message);
    }
    void print(String key, String value){
        if(result)
        Log.i("AssetDataManager : ",key + " : "+value);
    }
}
