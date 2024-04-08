package com.example.myaccountbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DecimalFormat;
import android.util.Log;

import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Settlement;
import com.example.myaccountbook.data.defaultBudget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BudgetManager {

    private String 변환2(int number){
        return String.format("%02d", number);
    }
    private String 변환4(int number){
        return String.format("%04d", number);
    }
    DecimalFormat myFormatter = new DecimalFormat("###,###");

    String 금액표시(long 금액){
        return myFormatter.format(금액);
    }
    List<accountBook> 내역기록;

    public class 카테고리가계부목록{
        public List<accountBook> 카테고리내역기록;

        public Settlement settlement;

    }




   public BudgetManager(){
        내역기록 = new ArrayList<>();

//        calendar = new Calendar(application);
    }

    public 카테고리가계부목록 getList(Date date,MyApplication application,int categoryPk){

        setMonth(application.readDb, date.getYear(), date.getMonth(),application);

        카테고리가계부목록 목록 = new 카테고리가계부목록();
        List<accountBook> 기록 = new ArrayList<>();
        List<Settlement> list = new ArrayList<>();
//        Settlement settlement1 = new Settlement();
//        settlement.
        // defaultBudget 객체 목록 먼저 가져오기
        // 전체 예산 설정 안되어있으면 목록의 예산 다 더해서 전체 예산 추가하기
        // 이때의 전체 예산의 지출은 목록의 지출 다 더한 값.
        // 그게 아니라면 전체 지출

        // 객체마다 지출 가져오기

        List<defaultBudget> budgetList;
        budgetList = application.예산목록(date.getYear(),date.getMonth());

        budgetList.sort(new Comparator<defaultBudget>() {
            @Override
            public int compare(defaultBudget o1, defaultBudget o2) {
                return o1.카테고리식별자 - o2.카테고리식별자;
            }
        });



        boolean result = true;
        for(int i=0;i<budgetList.size();i++){
            Settlement settlement = new Settlement();

            settlement.예산 = budgetList.get(i);
            settlement.date = date;
            settlement.지출 = 카테고리_지출가져오기(budgetList.get(i).카테고리식별자);

            if(budgetList.get(i).카테고리식별자 == Budget.전체식별자){

                int 지출 = 0;

                for(int j=0;j<budgetList.size();j++){
                    if(budgetList.get(j).카테고리식별자 != Budget.전체식별자) {
                        지출 += 카테고리_지출가져오기(budgetList.get(j).카테고리식별자);



                    }
                }
                settlement.지출 = 지출;
                list.add(0,settlement);

            }
            else{
                list.add(settlement);

            }
            result = false;
//            list.add(settlement);
        }

        if(result){

            defaultBudget budget;
            budget = new defaultBudget();

            budget.카테고리식별자 = Budget.전체식별자;
            budget.카테고리이름 = Budget.전체;
            budget.date = date;
            budget.예산 = 0;

            for(int j=0;j<budgetList.size();j++){
                if(budgetList.get(j).카테고리식별자 != Budget.전체식별자) {
                    budget.예산 += budgetList.get(j).예산;
                }
            }

            Settlement settlement = new Settlement();
            settlement.지출 = Integer.parseInt(Long.toString(월총지출가져오기()));
            settlement.예산 = budget;
            settlement.date = date;

            list.add(0,settlement);
//            budget.예산 = 카테고리_지출가져오기(budget.카테고리식별자);
//            budgetList.add(0,budget);
        }


        for (int i =0 ;i<list.size();i++){
            if(list.get(i).예산.카테고리식별자 == categoryPk){
//                settlement1 = list.get(i);
                목록.settlement = list.get(i);
            }
        }

        if(result){
            for(int i=0;i< 내역기록.size();i++) {

                accountBook 가계부 = 내역기록.get(i);

//            java.util.Date 날짜 = 가계부.parseISODate(가계부.날짜);
//            java.util.Calendar calendar = java.util.Calendar.getInstance();
//
//            calendar.setTime(날짜);
//            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);
//
//            if(day == 일)
//                if(내역.type == accountBook.고정지출 || 내역.type == accountBook.변동지출 ){
//                    총지출 += 내역.금액;
//                }

                if (가계부.type == accountBook.고정지출 || 가계부.type == accountBook.변동지출) {


                    if(Budget.전체식별자 == categoryPk || 가계부.카테고리식별자 == categoryPk) {

                        기록.add(가계부);
                    }

                }
            }
        }
        else{

            if(categoryPk == Budget.전체식별자){
                for(int i=0;i<budgetList.size();i++){
                    defaultBudget budget = budgetList.get(i);
                    int pk = budget.카테고리식별자;

                    for(int j=0;j< 내역기록.size();j++) {

                        accountBook 가계부 = 내역기록.get(j);

//            java.util.Date 날짜 = 가계부.parseISODate(가계부.날짜);
//            java.util.Calendar calendar = java.util.Calendar.getInstance();
//
//            calendar.setTime(날짜);
//            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);
//
//            if(day == 일)
//                if(내역.type == accountBook.고정지출 || 내역.type == accountBook.변동지출 ){
//                    총지출 += 내역.금액;
//                }

                        if (가계부.type == accountBook.고정지출 || 가계부.type == accountBook.변동지출) {
                            if(가계부.카테고리식별자 == pk) {

                                기록.add(가계부);
                            }
                        }
                    }
                }
            }
            else{
                for(int i=0;i< 내역기록.size();i++) {

                    accountBook 가계부 = 내역기록.get(i);

//            java.util.Date 날짜 = 가계부.parseISODate(가계부.날짜);
//            java.util.Calendar calendar = java.util.Calendar.getInstance();
//
//            calendar.setTime(날짜);
//            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);
//
//            if(day == 일)
//                if(내역.type == accountBook.고정지출 || 내역.type == accountBook.변동지출 ){
//                    총지출 += 내역.금액;
//                }

                    if (가계부.type == accountBook.고정지출 || 가계부.type == accountBook.변동지출) {


                        if(가계부.카테고리식별자 == categoryPk) {

                            기록.add(가계부);
                        }

                    }
                }
            }
        }


//        Date date = new Date();


        목록.카테고리내역기록 = 기록;


        return 목록;

    }
    public List<Settlement> getSettlementList(Date date,MyApplication application){

        setMonth(application.readDb, date.getYear(), date.getMonth(),application);

        List<Settlement> list = new ArrayList<>();

        // defaultBudget 객체 목록 먼저 가져오기
        // 전체 예산 설정 안되어있으면 목록의 예산 다 더해서 전체 예산 추가하기
        // 이때의 전체 예산의 지출은 목록의 지출 다 더한 값.
        // 그게 아니라면 전체 지출

        // 객체마다 지출 가져오기

        List<defaultBudget> budgetList;
        budgetList = application.예산목록(date.getYear(),date.getMonth());

        budgetList.sort(new Comparator<defaultBudget>() {
            @Override
            public int compare(defaultBudget o1, defaultBudget o2) {
                return o1.카테고리식별자 - o2.카테고리식별자;
            }
        });

        boolean result = true;
        for(int i=0;i<budgetList.size();i++){
            Settlement settlement = new Settlement();

            settlement.예산 = budgetList.get(i);
            settlement.date = date;
            settlement.지출 = 카테고리_지출가져오기(budgetList.get(i).카테고리식별자);

            if(budgetList.get(i).카테고리식별자 == Budget.전체식별자){

                int 지출 = 0;

                for(int j=0;j<budgetList.size();j++){
                    if(budgetList.get(j).카테고리식별자 != Budget.전체식별자) {
                        지출 += 카테고리_지출가져오기(budgetList.get(j).카테고리식별자);
                    }
                }
                settlement.지출 = 지출;
                list.add(0,settlement);

            }
            else{
                list.add(settlement);

            }
            result = false;
//            list.add(settlement);
        }


        if(result){

            defaultBudget budget;
            budget = new defaultBudget();

            budget.카테고리식별자 = Budget.전체식별자;
            budget.카테고리이름 = Budget.전체;
            budget.date = date;
            budget.예산 = 0;

            for(int j=0;j<budgetList.size();j++){
                if(budgetList.get(j).카테고리식별자 != Budget.전체식별자) {
                    budget.예산 += budgetList.get(j).예산;
                }
            }

            Settlement settlement = new Settlement();
            settlement.지출 = Integer.parseInt(Long.toString(월총지출가져오기()));
            settlement.예산 = budget;
            settlement.date = date;

            list.add(0,settlement);
//            budget.예산 = 카테고리_지출가져오기(budget.카테고리식별자);
//            budgetList.add(0,budget);
        }


//        Date date = new Date();




        return list;

    }

    public long 일총지출가져오기(int day){

        long 총지출 = 0;

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);


            java.util.Date 날짜 = 내역.parseISODate(내역.날짜);
            java.util.Calendar calendar = java.util.Calendar.getInstance();

            calendar.setTime(날짜);
            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            if(day == 일)
                if(내역.type == accountBook.고정지출 || 내역.type == accountBook.변동지출 ){
                    총지출 += 내역.금액;
                }


        }
        return 총지출;

    }


    public defaultBudget get기본예산(MyApplication application, int category_pk){
        List<defaultBudget> 기본예산목록 = application.dbHelper.기본예산데이터요청(application.readDb);
        for(int i=0;i<기본예산목록.size();i++){
            defaultBudget defaultBudget = 기본예산목록.get(i);
            if(category_pk == defaultBudget.카테고리식별자){
                return defaultBudget;
            }

        }

//
//        if(category_pk != Budget.전체식별자){
//
//        }
//        else{
//
//            boolean result = false;
//            for(int i=0;i<기본예산목록.size();i++){
//                defaultBudget defaultBudget = 기본예산목록.get(i);
//                if(defaultBudget.카테고리식별자 == Budget.전체식별자){
//
//                    result = true;
//
//
//                }
//
//            }
//
//            if(result){
//
//                // 이버
//
//
//            }
//
//
//        }


        return null;

    }

    public List<Settlement> getSettlementList(Date date,MyApplication application,int category_pk){

        List<Settlement> list = new ArrayList<>();


        for(int i=1;i<=12;i++){
            setMonth(application.readDb, date.getYear(), i,application);


            // defaultBudget 객체 목록 먼저 가져오기
            // 전체 예산 설정 안되어있으면 목록의 예산 다 더해서 전체 예산 추가하기
            // 이때의 전체 예산의 지출은 목록의 지출 다 더한 값.
            // 그게 아니라면 전체 지출

            // 객체마다 지출 가져오기

            List<defaultBudget> budgetList;
            budgetList = application.예산목록(date.getYear(),i);

            boolean result = false;

            for(int j=0;j<budgetList.size();j++){
                if(category_pk == budgetList.get(j).카테고리식별자) {

                    defaultBudget budget = budgetList.get(j);
                    Settlement settlement = new Settlement();

                    if(category_pk != Budget.전체식별자){

                        settlement.예산 = budget;
                        Date date1 = new Date();
                        date1.setYear(date.getYear());
                        date1.setMonth(i);
                        settlement.date = date1;

                        settlement.지출 = 카테고리_지출가져오기(budget.카테고리식별자);
                        list.add(settlement);

                    }
                    else{

                        int 지출 = 0;

                        Date date1 = new Date();
                        date1.setYear(date.getYear());
                        date1.setMonth(i);
//                        date1.year = date.year;
//                        date1.month = i;
                        settlement.date = date1;

//                        for(int p=0;p<budgetList.size();p++){
//                            if(budgetList.get().카테고리식별자 != Budget.전체식별자) {
//                                지출 += 카테고리_지출가져오기(budgetList.get(j).카테고리식별자);
//                            }
//                        }
                        for(int p=0;p<budgetList.size();p++){
                            if(budgetList.get(p).카테고리식별자 != Budget.전체식별자) {
                                지출 += 카테고리_지출가져오기(budgetList.get(p).카테고리식별자);
                            }
                        }
                        settlement.예산 = budget;
                        settlement.지출 = 지출;
                        list.add(settlement);


                    }
                    result = true;

                }
            }

            if(!result){

                defaultBudget budget;
                budget = new defaultBudget();

                budget.카테고리식별자 = Budget.전체식별자;
                budget.카테고리이름 = Budget.전체;
                budget.date = date;
                budget.예산 = 0;

                for(int j=0;j<budgetList.size();j++){
                    if(budgetList.get(j).카테고리식별자 != Budget.전체식별자) {
                        budget.예산 += budgetList.get(j).예산;
                    }
                }

                Settlement settlement = new Settlement();
                settlement.지출 = Integer.parseInt(Long.toString(월총지출가져오기()));
                settlement.예산 = budget;
                settlement.date = date;

                list.add(settlement);
//            budget.예산 = 카테고리_지출가져오기(budget.카테고리식별자);
//            budgetList.add(0,budget);
            }

//        Date date = new Date();

        }

        return list;

    }


    public int 카테고리_지출가져오기(int 카테고리식별자){

        int 지출 = 0;
        for(int i=0;i< 내역기록.size();i++){

            accountBook 가계부 = 내역기록.get(i);
            if(가계부.type == accountBook.고정지출 || 가계부.type == accountBook.변동지출){

                if(카테고리식별자 == 가계부.카테고리식별자){

                    지출 += 가계부.금액;
                }

            }


        }
        return 지출;

    }

    public long 월총지출가져오기(){

        long 총지출 = 0;

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);
            if(내역.type == accountBook.고정지출 || 내역.type == accountBook.변동지출){
                총지출 += 내역.금액;
            }
        }
        return 총지출;

    }
    // 1~12 사이로
    public void setMonth(SQLiteDatabase database, int year, int month,MyApplication application){



//        this.month = month;
        내역기록.clear();

        // 내역 가져오기.
//        select * from accountbook where date>'2024-01-05 00:00' AND date < '2024-01-12 05:22' AND routine =0

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year,month-1,1);
        int day = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);  //말일


        String sql = "select * from accountbook where date>= '"+변환4(year)+"-"+변환2(month)+"-01 00:00' AND date <= '"+변환4(year)+"-"+변환2(month)+"-"+변환2(  day)+" 23:59' AND routine =0 ORDER by date";

//        Log.i(TAG,sql);
        Cursor cursor = database.rawQuery(sql,null);

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
            내역.금액원 = 금액표시(내역.금액);

            내역.날짜 = 날짜;
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


            내역기록.add(application.치환(내역));

        }

        cursor.close();

    }




    public List<Budget> 예산목록반환(){

        List<Budget> list = new ArrayList<>();

        Budget budget = new Budget();



        list.add(budget);

        return list;

    }

}
