package com.example.myaccountbook.data;

import android.icu.text.DecimalFormat;

public class Amount {

    public int amount;

    public Amount(){
        amount=0;
    }
    DecimalFormat myFormatter
            = new DecimalFormat("###,###");

    public String getAmount(){
        return myFormatter.format(amount) +"원";
    }
    public String getAmount(boolean setting){
        return myFormatter.format(amount*-1) +"원";
    }

}
