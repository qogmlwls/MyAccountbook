package com.example.myaccountbook;

public class PeriodManager {


    public static final int 주간 = 1;
    public static final int 월간 = 2;
    public static final int 연간 = 3;
    public static final int 기간 = 4;



    static String getName(int type){
        if(type == 주간){
            return "주간";
        }
        else if(type == 월간){
            return "월간";
        }
        else if(type == 연간){
            return "연간";
        }
        else if(type == 기간){
            return "기간";
        }
        else{
            return "없음";
        }
    }
    
}
