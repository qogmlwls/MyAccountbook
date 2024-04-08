package com.example.myaccountbook.data;

import android.icu.text.DecimalFormat;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Formatter {
    public static int 금액쉼표제거(String amount){

        String 금액쉼표제거 = amount.replace(",", "");

        return Integer.parseInt(금액쉼표제거);

    }

    public static String 천단위구분(int amount){
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        return myFormatter.format(amount);
    }


    public static String DB저장날짜형식으로파싱(Date 날짜데이터){
        SimpleDateFormat 날짜형식 = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.KOREA);


        return 날짜형식.format(날짜데이터);

    }
    public static Date DB저장날짜형식을파싱(String 날짜데이터){
        SimpleDateFormat 날짜형식 = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.KOREA);

        try {
            return 날짜형식.parse(날짜데이터);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

}
