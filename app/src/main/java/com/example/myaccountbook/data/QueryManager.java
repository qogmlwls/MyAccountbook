package com.example.myaccountbook.data;

import android.util.Log;

import com.example.myaccountbook.FeedReaderContract;
import com.example.myaccountbook.accountBook;

import java.text.SimpleDateFormat;

public class QueryManager {

    static String 수입 = "type=1";
    static String 지출 = "(type=2 OR type=3)";


    public static class PieChart{

        public static String GetMonthsAmountData(Date 날짜,int type,int category_pk){

//            SELECT strftime('%m', date) AS month, SUM(amount) AS total_amount
//            FROM accountbook
//            WHERE category_pk=9 AND strftime('%Y%m', date) = '202401' AND routine = 0
//            GROUP BY strftime('%m', date);

            String type_msg = "";
            if(type == accountBook.수입){
                type_msg = 수입;
            }
            else if(type == accountBook.지출){
                type_msg = 지출;
            }

            String year = Integer.toString(날짜.getYear());
            String month = String.format("%02d", 날짜.getMonth());

            return "SELECT strftime('%m', date) AS month, SUM(amount) AS total_amount " +
                    "FROM accountbook " +
                    "WHERE category_pk=" +Integer.toString(category_pk)+" AND "+type_msg +
                    " AND strftime('%Y%m', date) = '"+year+month+"' AND routine = 0 " +
                    "GROUP BY strftime('%m', date)";

        }

        public static String GetWeeksAmountData(Date 날짜,int type,int category_pk){

//SELECT strftime('%Y%W', date) AS week, SUM(amount) AS total_amount
//FROM accountbook
//WHERE category_pk=9 AND strftime('%Y%W', date) = '202401' AND routine = 0
//GROUP BY strftime('%Y%W', date);

//            SELECT strftime('%Y%W', date) AS week, SUM(amount) AS total_amount
//            FROM accountbook
//            WHERE category_pk=14 AND date >= '2024-01-01 00:00' AND date < '2024-02-01 00:00'  AND routine = 0
//            GROUP By category_pk

            String type_msg = "";
            if(type == accountBook.수입){
                type_msg = 수입;
            }
            else if(type == accountBook.지출){
                type_msg = 지출;
            }

//            String year = Integer.toString(날짜.getYear());
//            String month = String.format("%02d", 날짜.getMonth());
//            Log.i("year",year);
//            Log.i("month",month);
//            SimpleDateFormat format = new SimpleDateFormat("w");
//
//            String week = String.format("%02d", Integer.parseInt(format.format(날짜.getDate())));
//
//            Log.i("week",week);
//            format = new SimpleDateFormat("yyyy mm dd hh");
//            String week2 = format.format(날짜.getDate());
//            Log.i("week2",week2);

            Date 다음주 = Date.다음주(날짜);
            String 날짜1 = "'"+날짜.getYear()+"-"+String.format("%02d", 날짜.getMonth())+"-"+String.format("%02d", 날짜.getDay())+" 00:00'";
            String 날짜2 = "'"+다음주.getYear()+"-"+String.format("%02d", 다음주.getMonth())+"-"+String.format("%02d", 다음주.getDay())+" 00:00'";

            return "SELECT strftime('%Y%W', date) AS week, SUM(amount) AS total_amount " +
                    "FROM accountbook " +
                    "WHERE category_pk=" +Integer.toString(category_pk)+" AND "+type_msg +
                    " AND date>= "+날짜1+" AND date < "+날짜2+
                    " AND routine = 0 " +
//                    " AND strftime('%Y%W', date) = '"+year+week+"' AND routine = 0 " +
                    "GROUP BY category_pk";

        }

        public static String GetMonthDetailData(Date 날짜,int type,int category_pk){


            String type_msg = "";
            if(type == accountBook.수입){
                type_msg = 수입;
            }
            else if(type == accountBook.지출){
                type_msg = 지출;
            }

            String year = Integer.toString(날짜.getYear());
            String month = String.format("%02d", 날짜.getMonth());

            Date 이후날짜 = 날짜.clone(날짜);
            이후날짜.다음달로이동();
//        Date 이후날짜 = Date.이후날짜(날짜);
            String 이후year = Integer.toString(이후날짜.getYear());
            String 이후month = String.format("%02d", 이후날짜.getMonth());;

            return "select * from accountbook " +
                    "where category_pk="+Integer.toString(category_pk)+" AND "+type_msg +
                    " AND date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
//                    " AND date>= "+날짜1+" AND date <= "+날짜2+
                    " AND routine = 0" +
                    " ORDER by date";

        }

        public static String GetYearDetailData(Date 날짜,int type,int category_pk){

            String type_msg = "";
            if(type == accountBook.수입){
                type_msg = 수입;
            }
            else if(type == accountBook.지출){
                type_msg = 지출;
            }

            return "select * from accountbook " +
                    "where category_pk="+Integer.toString(category_pk)+" AND "+type_msg +
                    " AND date>= '"+날짜.getYear()+"-01-01 00:00' AND date <= '"+날짜.getYear()+"-12-31 23:59'"+
//                    " AND date>= "+날짜1+" AND date <= "+날짜2+
                    " AND routine = 0 ORDER by date";

        }

        public static String GetWeekDetailData(Date 날짜,int type,int category_pk){

            String type_msg = "";
            if(type == accountBook.수입){
                type_msg = 수입;
            }
            else if(type == accountBook.지출){
                type_msg = 지출;
            }

            Date 다음주 = Date.다음주(날짜);
            String 날짜1 = "'"+날짜.getYear()+"-"+String.format("%02d", 날짜.getMonth())+"-"+String.format("%02d", 날짜.getDay())+" 00:00'";
            String 날짜2 = "'"+다음주.getYear()+"-"+String.format("%02d", 다음주.getMonth())+"-"+String.format("%02d", 다음주.getDay())+" 00:00'";

            return "select * from accountbook " +
                    "where category_pk="+Integer.toString(category_pk)+" AND "+type_msg +
                    " AND date>= "+날짜1+" AND date < "+날짜2+
                    " AND routine = 0 ORDER by date";

        }
        public static String GetDaysDetailData(Date 시작날짜, Date 끝날짜,int type, int category_pk){

            String type_msg = "";
            if(type == accountBook.수입){
                type_msg = 수입;
            }
            else if(type == accountBook.지출){
                type_msg = 지출;
            }


            String 날짜1 = "'"+시작날짜.getYear()+"-"+String.format("%02d", 시작날짜.getMonth())+"-"+String.format("%02d", 시작날짜.getDay())+" 00:00'";
            String 날짜2 = "'"+끝날짜.getYear()+"-"+String.format("%02d", 끝날짜.getMonth())+"-"+String.format("%02d", 끝날짜.getDay())+" 23:59'";

            return "select * from accountbook " +
                    "where category_pk="+Integer.toString(category_pk)+ " AND "+type_msg+
                    " AND date>= "+날짜1+" AND date <= "+날짜2+
                    " AND routine = 0 ORDER by date";
        }

//        static String DetailData(Date 시작날짜,Date 끝날짜){
//            " AND date>= '"+변환4(날짜.getYear())+"-"+변환2(날짜.getMonth())+"-"+변환2(날짜.getDay())+" 00:00'" +
//                    " AND date <= '"+변환4(다음주.getYear())+"-"+변환2(다음주.getMonth())+"-"+변환2(다음주.getDay())+" 23:59'" +
//        }

        public static String GetMonthMainData(Date 날짜){

//            select SUM(amount) AS total_sum,data.name AS NAME, accountbook.type,category_pk from accountbook LEFT JOIN data
//            ON accountbook.category_pk=data.pk
//            where date >= '2024-01-01 00:00' AND date <= '2024-01-31 23:59' AND routine = 0
//            GROUP BY category_pk , accountbook.type ORDER by total_sum DESC ,accountbook.type DESC;

            String year = Integer.toString(날짜.getYear());
            String month = String.format("%02d", 날짜.getMonth());

            Date 이후날짜 = 날짜.clone(날짜);
            이후날짜.다음달로이동();
//        Date 이후날짜 = Date.이후날짜(날짜);
            String 이후year = Integer.toString(이후날짜.getYear());
            String 이후month = String.format("%02d", 이후날짜.getMonth());;

            return "select SUM(amount) AS total_sum, data.name AS NAME, accountbook.type,category_pk " +
                    "from accountbook LEFT JOIN data " +
                    "ON accountbook.category_pk = data.pk " +
                    "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
//                    "where date >= '2024-01-01 00:00' AND date <= '2024-01-31 23:59'" +
                    " AND routine = 0 " +
                    "GROUP BY category_pk , accountbook.type " +
                    "ORDER by total_sum DESC, accountbook.type DESC";

        }
        public static String GetYearMainData(Date 날짜){

//            select SUM(amount) AS total_sum,data.name AS NAME, accountbook.type,category_pk from accountbook LEFT JOIN data
//            ON accountbook.category_pk=data.pk
//            where date >= '2024-01-01 00:00' AND date <= '2024-12-31 23:59' AND routine = 0
//            GROUP BY category_pk , accountbook.type ORDER by total_sum DESC ,accountbook.type DESC;

//            " AND date>= '"+변환4(날짜.getYear())+"-01-01 00:00' AND date <= '"+변환4(날짜.getYear())+"-12-31 23:59' AND routine = 0 ORDER by date";

            return "select SUM(amount) AS total_sum, data.name AS NAME, accountbook.type,category_pk " +
                    "from accountbook LEFT JOIN data " +
                    "ON accountbook.category_pk = data.pk " +
//                    "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
                    " where date>= '"+날짜.getYear()+"-01-01 00:00' AND date <= '"+날짜.getYear()+"-12-31 23:59'"+
//                    "where date >= '2024-01-01 00:00' AND date <= '2024-01-31 23:59'" +
                    " AND routine = 0 " +
                    "GROUP BY category_pk , accountbook.type " +
                    "ORDER by total_sum DESC, accountbook.type DESC";

        }
        public static String GetWeekMainData(Date 날짜){

//            select SUM(amount) AS total_sum,data.name AS NAME, accountbook.type,category_pk from accountbook LEFT JOIN data
//            ON accountbook.category_pk=data.pk
//            where date >= '2024-01-01 00:00' AND date <= '2024-12-31 23:59' AND routine = 0
//            GROUP BY category_pk , accountbook.type ORDER by total_sum DESC ,accountbook.type DESC;

//            " AND date>= '"+변환4(날짜.getYear())+"-01-01 00:00' AND date <= '"+변환4(날짜.getYear())+"-12-31 23:59' AND routine = 0 ORDER by date";
//            String month = String.format("%02d", 날짜.getMonth());
            Date 다음주 = Date.다음주(날짜);

            String 날짜1 = "'"+날짜.getYear()+"-"+String.format("%02d", 날짜.getMonth())+"-"+String.format("%02d", 날짜.getDay())+" 00:00'";
            String 날짜2 = "'"+다음주.getYear()+"-"+String.format("%02d", 다음주.getMonth())+"-"+String.format("%02d", 다음주.getDay())+" 00:00'";

            return "select SUM(amount) AS total_sum, data.name AS NAME, accountbook.type,category_pk " +
                    "from accountbook LEFT JOIN data " +
                    "ON accountbook.category_pk = data.pk " +
//                    "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
                    " where date>= "+날짜1+" AND date < "+날짜2+
//                    "where date >= '2024-01-01 00:00' AND date <= '2024-01-31 23:59'" +
                    " AND routine = 0 " +
                    "GROUP BY category_pk , accountbook.type " +
                    "ORDER by total_sum DESC, accountbook.type DESC";

        }
        public static String GetDaysMainData(Date 시작날짜, Date 끝날짜){

//            select SUM(amount) AS total_sum,data.name AS NAME, accountbook.type,category_pk from accountbook LEFT JOIN data
//            ON accountbook.category_pk=data.pk
//            where date >= '2024-01-01 00:00' AND date <= '2024-12-31 23:59' AND routine = 0
//            GROUP BY category_pk , accountbook.type ORDER by total_sum DESC ,accountbook.type DESC;

//            " AND date>= '"+변환4(날짜.getYear())+"-01-01 00:00' AND date <= '"+변환4(날짜.getYear())+"-12-31 23:59' AND routine = 0 ORDER by date";
//            String month = String.format("%02d", 날짜.getMonth());

            String 날짜1 = "'"+시작날짜.getYear()+"-"+String.format("%02d", 시작날짜.getMonth())+"-"+String.format("%02d", 시작날짜.getDay())+" 00:00'";
            String 날짜2 = "'"+끝날짜.getYear()+"-"+String.format("%02d", 끝날짜.getMonth())+"-"+String.format("%02d", 끝날짜.getDay())+" 23:59'";

//            query = "select * from accountbook where " +
//                    " date>= '" + 변환4(시작날짜.getYear()) + "-" + 변환2(시작날짜.getMonth()) + "-" + 변환2(시작날짜.getDay()) + " 00:00' " +
//                    "AND date <= '" + 변환4(끝날짜.getYear()) + "-" + 변환2(끝날짜.getMonth()) + "-" + 변환2(끝날짜.getDay()) + " 23:59' " +
//                    "AND routine=0 ORDER by date";

            return "select SUM(amount) AS total_sum, data.name AS NAME, accountbook.type,category_pk " +
                    "from accountbook LEFT JOIN data " +
                    "ON accountbook.category_pk = data.pk " +
//                    "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
                    " where date>= "+날짜1+" AND date <= "+날짜2+
//                    "where date >= '2024-01-01 00:00' AND date <= '2024-01-31 23:59'" +
                    " AND routine = 0 " +
                    "GROUP BY category_pk , accountbook.type " +
                    "ORDER by total_sum DESC, accountbook.type DESC";

        }





    }

    public static String 내역가져오기(Date 날짜, int type){

//        SELECT *
//                FROM accountbook
//        WHERE date >= '2024-01-01 00:00' AND date < '2024-02-01 00:00' AND type=2
        String year = Integer.toString(날짜.getYear());
        String month = String.format("%02d", 날짜.getMonth());

        Date 이후날짜 = 날짜.clone(날짜);
        이후날짜.다음달로이동();
//        Date 이후날짜 = Date.이후날짜(날짜);
        String 이후year = Integer.toString(이후날짜.getYear());
        String 이후month = String.format("%02d", 이후날짜.getMonth());;

        if(type != accountBook.지출){
            return "SELECT * " +
                    "FROM accountbook " +
                    "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
                    " AND type="+Integer.toString(type)+" ORDER BY date DESC";
        }
        else{
            return "SELECT * " +
                    "FROM accountbook " +
                    "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
                    " AND (type=2 OR type=3) ORDER BY date DESC";
        }


    }

    public static String 월간수입지출총액가져오기(Date 날짜){

//        SELECT SUM(amount) AS total_sum, COUNT(*) AS total_rows, type
//        FROM accountbook
//        WHERE date >= '2024-01-01 00:00' AND date < '2024-02-01 00:00'
//        GROUP BY type;
        String year = Integer.toString(날짜.getYear());
        String month = String.format("%02d", 날짜.getMonth());

        Date 이후날짜 = 날짜.clone(날짜);
        이후날짜.다음달로이동();
//        Date 이후날짜 = Date.이후날짜(날짜);
        String 이후year = Integer.toString(이후날짜.getYear());
        String 이후month = String.format("%02d", 이후날짜.getMonth());;

        String query = "SELECT SUM(amount) AS total_sum, COUNT(*) AS total_rows, type " +
                "FROM accountbook " +
                "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
//                "WHERE date >= '2024-01-01 00:00' AND date < '2024-02-01 00:00' " +
                " GROUP BY type;";
        return query;
    }
    public static String 월간일반예산_전체예산가져오기(Date 날짜){
//        SELECT * from budget2
//        where budget2.category_pk=0 AND year=2024 AND month=3
        return "SELECT amount, * from budget2 " +
                "where budget2.category_pk=0 AND "+년월(날짜);
    }

    public static String 년월(Date 날짜){
        return " year="+Integer.toString(날짜.getYear())+" AND month="+Integer.toString(날짜.getMonth());
    }

    public static String 월간기본예산_전체예산가져오기(){
//        SELECT * from budget1
//        where budget1.category_pk=0
        return "SELECT amount, * from budget1 where budget1.category_pk=0";
    }
    public static String 기본예산에서일반예산미설정카테고리들의예산총합가져오기(Date 날짜){

//        SELECT budget1.*, SUM(amount)
//        FROM budget1
//        WHERE NOT EXISTS (
//                SELECT 1
//                FROM budget2
//                WHERE budget1.category_pk =budget2.category_pk AND budget2.year = 2023 AND budget2.month = 1
//        );

        return "SELECT SUM(amount), budget1.* " +
                "FROM budget1 " +
                "WHERE NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM budget2 " +
                "    WHERE budget1.category_pk =budget2.category_pk AND " +
                " budget2.year="+Integer.toString(날짜.getYear())+" AND budget2.month="+Integer.toString(날짜.getMonth()) +
                ");";
    }

    public static String 월간일반예산총합가져오기(Date 날짜){
        return "SELECT SUM(amount), budget2.* " +
                "FROM budget2 " +
                "where "+" year="+Integer.toString(날짜.getYear())
                +" AND month="+Integer.toString(날짜.getMonth());
    }



    public static String 월특정자산의내역데이터가져오기(int 자산식별자, com.example.myaccountbook.data.Date date){

//        SELECT * from accountbook
//        where date >= '2024-01-01 00:00' AND date < '2024-02-01 00:00' AND (asset_pk=3 OR withdraw=3 OR deposit = 3 )
//        ORDER BY date DESC

        String year = Integer.toString(date.getYear());
        String month = String.format("%02d", date.getMonth());

        Date 이후날짜 = date.clone(date);
        이후날짜.다음달로이동();
//        Date 이후날짜 = Date.이후날짜(date);
        String 이후year = Integer.toString(이후날짜.getYear());
        String 이후month = String.format("%02d", 이후날짜.getMonth());;

        return "SELECT * from accountbook " +
                "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+" AND " +
                "(asset_pk="+Integer.toString(자산식별자)+" OR withdraw="+Integer.toString(자산식별자)+" OR deposit = "+Integer.toString(자산식별자)+" ) " +
                "ORDER BY date DESC";
    }

    public static String 월특정자산의입금출금데이터가져오기(int 자산식별자, com.example.myaccountbook.data.Date date){

//        SELECT type,asset_pk,deposit,withdraw,SUM(accountbook.amount) AS total_amount from accountbook
//        where date >= '2024-01-01 00:00' AND date < '2024-02-01 00:00' AND (asset_pk=3 OR withdraw=3 OR deposit = 3 )
//        GROUP BY type,asset_pk,withdraw,deposit;

        String year = Integer.toString(date.getYear());
        String month = String.format("%02d", date.getMonth());
        Date 이후날짜 = date.clone(date);
        이후날짜.다음달로이동();
        String 이후year = Integer.toString(이후날짜.getYear());
        String 이후month = String.format("%02d", 이후날짜.getMonth());;

        String query = "SELECT type, asset_pk, deposit, withdraw, SUM(accountbook.amount) AS total_amount from accountbook " +
                "where date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+" AND " +
                "(asset_pk="+Integer.toString(자산식별자)
                +" OR withdraw="+Integer.toString(자산식별자)
                +" OR deposit = "+Integer.toString(자산식별자)+" ) " +
                "GROUP BY type,asset_pk,withdraw,deposit";
        Log.v("월특정자산의입금출금데이터가져오기 query : ",query);

        return query;
    }
    public static String 월특정자산의누적잔액데이터가져오기(int 자산식별자, com.example.myaccountbook.data.Date date){

//        SELECT type,asset_pk,deposit,withdraw,SUM(accountbook.amount) AS total_amount from accountbook
//        where date < '2024-02-01 00:00' AND (asset_pk=3 OR withdraw=3 OR deposit = 3 )
//        GROUP BY type,asset_pk,withdraw,deposit;

        Date 이후날짜 = date.clone(date);
        이후날짜.다음달로이동();
//        Date 이후날짜 = Date.이후날짜(date);

        String 이후year = Integer.toString(이후날짜.getYear());
        String 이후month = String.format("%02d", 이후날짜.getMonth());;
        String query = "SELECT type, asset_pk, deposit, withdraw, SUM(accountbook.amount) AS total_amount from accountbook " +
                "where date < "+날짜(이후year,이후month)+" AND " +
                "(asset_pk="+Integer.toString(자산식별자)
                +" OR withdraw="+Integer.toString(자산식별자)
                +" OR deposit = "+Integer.toString(자산식별자)+" ) " +
                "GROUP BY type,asset_pk,withdraw,deposit";
        Log.v("월특정자산의누적잔액데이터가져오기 query : ",query);

        return query;
    }

    private String 변환2(int number){
        return String.format("%02d", number);
    }


    public static String 날짜(String year,String month){

        return "'"+year+"-"+month+"-01 00:00'";
    }


    public static String 월간전체통계누적수입지출가져오기
            (com.example.myaccountbook.data.Date date){

        Log.v("월간전체통계누적수입지출가져오기 ",date.년월날짜());
        Date 이후날짜 = date.clone(date);
        이후날짜.다음달로이동();

        String year = Integer.toString(이후날짜.getYear());
        String month = String.format("%02d", 이후날짜.getMonth());
        String query = "SELECT type,SUM(amount) FROM accountbook " +
                "WHERE date < "+날짜(year,month)+" GROUP BY type";
        Log.v("월간전체통계누적수입지출가져오기 query : ",query);

        return query;

    }

    public static String 월간통계누적수입지출가져오기(com.example.myaccountbook.data.Date date,int 자산식별자){

//        Date 이후날짜 = Date.이후날짜(date);
        Date 이후날짜 = date.clone(date);
        이후날짜.다음달로이동();

        String year = Integer.toString(이후날짜.getYear());
        String month = String.format("%02d", 이후날짜.getMonth());
//        SELECT type,SUM(amount) FROM accountbook WHERE date < '2024-01-01 00:00' GROUP BY type;
        return "SELECT type,SUM(amount),deposit,withdraw FROM accountbook WHERE date < "+날짜(year,month)+
//                "AND (asset_pk="+Integer.toString(자산식별자)+" OR withdraw="+Integer.toString(자산식별자)+" OR deposit = "+Integer.toString(자산식별자)+" ) " +
//                "AND COALESCE(withdraw = deposit, TRUE)"+
                "AND (asset_pk="+Integer.toString(자산식별자)
                +" OR ((withdraw="+Integer.toString(자산식별자)+" OR deposit = "+Integer.toString(자산식별자)+") AND (withdraw != deposit) ))"+
                " GROUP BY type";
    }
    public static String 월간통계수입지출가져오기(com.example.myaccountbook.data.Date date,int 자산식별자){
        String year = Integer.toString(date.getYear());
        String month = String.format("%02d", date.getMonth());
        Date 이후날짜 = date.clone(date);
        이후날짜.다음달로이동();
//        Date 이후날짜 = Date.이후날짜(date);
        String 이후year = Integer.toString(이후날짜.getYear());
        String 이후month = String.format("%02d", 이후날짜.getMonth());;
        return "SELECT type,SUM(amount),deposit,withdraw FROM accountbook WHERE date >= "+날짜(year,month)+" AND date < "+날짜(이후year,이후month)+
                "AND (asset_pk="+Integer.toString(자산식별자)
                +" OR ((withdraw="+Integer.toString(자산식별자)+" OR deposit = "+Integer.toString(자산식별자)+") AND (withdraw != deposit) ))"+
//                "AND COALESCE(withdraw = deposit, TRUE)"+
                " GROUP BY type";
    }
    public static String 월간전체통계수입지출가져오기
            (com.example.myaccountbook.data.Date date){

        Log.v("월간전체통계수입지출가져오기 ",date.년월날짜());
        String year = Integer.toString(date.getYear());
        String month = String.format("%02d", date.getMonth());
        Date 이후날짜 = date.clone(date);
        이후날짜.다음달로이동();
//        Date 이후날짜 = Date.이후날짜(date);
        String 이후year = Integer.toString(이후날짜.getYear());
        String 이후month = String.format("%02d", 이후날짜.getMonth());

        String query ="SELECT type,SUM(amount) FROM accountbook " +
                "WHERE date >= "+날짜(year,month)+" AND" +
                " date < "+날짜(이후year,이후month)+
                " GROUP BY type";
        Log.v("월간전체통계수입지출가져오기 query : ",query);
        return query;

    }


    public static String 자산별합계가져오기(){

//        SELECT accountbook.type, accountbook.asset_pk, accountbook.withdraw, accountbook.deposit, SUM(accountbook.amount) AS total_amount
//        FROM accountbook
//        INNER JOIN data ON accountbook.asset_pk = data.pk OR accountbook.withdraw = data.pk OR accountbook.deposit = data.pk
//        GROUP BY accountbook.type, accountbook.asset_pk, accountbook.withdraw, accountbook.deposit;
        return "SELECT accountbook.type, accountbook.asset_pk, accountbook.withdraw, accountbook.deposit, SUM(accountbook.amount) AS total_amount " +
                "FROM accountbook " +
//                "INNER JOIN data ON accountbook.asset_pk = data.pk OR accountbook.withdraw = data.pk OR accountbook.deposit = data.pk " +
//                "WHERE data.enable = 0 " +
                "GROUP BY accountbook.type, accountbook.asset_pk, accountbook.withdraw, accountbook.deposit";
    }
//
//    public static String 날짜조건문(Date startDate, Date endDate){
//
//        return "("+ FeedReaderContract.가계부내역.COLUMN_DATE+" >= '"+Formatter.DB저장날짜형식으로파싱(startDate)+"' AND "
//                + FeedReaderContract.가계부내역.COLUMN_DATE+" < '"+Formatter.DB저장날짜형식으로파싱(endDate)+"')";
//    }
//
//    public static String 가계부조건합계(){
//
//        return "select SUM("+FeedReaderContract.가계부내역.COLUMN_AMOUNT+") from "+FeedReaderContract.가계부내역.TABLE_NAME
//                +"where "+FeedReaderContract.가계부내역.COLUMN_TYPE+"="+ accountBook.이체 + " AND "+ QueryManager.날짜조건문(com.example.myaccountbook.data.Date.getDate());
//
//    }

}
