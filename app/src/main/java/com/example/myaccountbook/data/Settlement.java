package com.example.myaccountbook.data;

import android.icu.text.DecimalFormat;

public class Settlement {


    public Date date;

    public defaultBudget 예산;
    public int 지출;


    DecimalFormat myFormatter = new DecimalFormat("###,###");

    String 금액표시(int 금액){
        return myFormatter.format(금액);
    }

    
    public String 남은돈(){

        int 남은돈 = 예산.예산 - 지출;

        if(남은돈 >=0 ){
            return  myFormatter.format(남은돈)+"원";
        }
        else{
            return "초과 "+myFormatter.format(남은돈)+"원";
        }
//
//        // -원, 초과-원
//        return  "";
    }

    public int 퍼센트(){

        if(지출 == 0){
            return 0;
        }
        int 퍼센트지 = (지출*100/예산.예산);
        // -%
        return 퍼센트지;
    }
    public String 퍼센트지(){

        if(지출 == 0){
            return "0%";
        }
        int 퍼센트지 = (지출*100/예산.예산);
        // -%
        return Integer.toString(퍼센트지) + "%";
    }
    
    public String 지출(){

        
        String result = myFormatter.format(지출);
        // -원
        return result+"원";
    }


    public String 예산금액(){

        int 예산금액 = 예산.예산;
        String result = myFormatter.format(예산금액);
        return result+"원";

    }
    
    public String 카테고리명(){
        return 예산.카테고리이름;
    }
    
}
