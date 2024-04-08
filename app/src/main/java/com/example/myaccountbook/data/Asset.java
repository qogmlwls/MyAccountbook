package com.example.myaccountbook.data;

import com.example.myaccountbook.Data;
import com.example.myaccountbook.MyApplication;

public class Asset {
    
    
    public void 자산추가(MyApplication application, String 자산명){
        
        Data data = new Data();
        data.type = Data.자산;
        data.name = 자산명;

        long row =  application.데이터저장하기(data);
        data.pk = Integer.parseInt(Long.toString(row));
        application.자산데이터.add(data);
        
        
    }
}
