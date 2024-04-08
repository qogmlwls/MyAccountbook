package com.example.myaccountbook;

public class Data {

    public static final int 자산  = 0;
    public static final int 카테고리  = 1;
    public static final int 통계청카테고리  = 2;
    public static final int 반복  = 3;

    public static final int 활성화 = 0;
    public static final int 비활성화 = 1;


    public int pk;
    public String name;
    public int enable;

    // 자산 : 0, 카테고리:1, 통계청카테고리 : 2
    public int type;

    // 통계청 카테고리
    public int office_category;

    String delete_date;


    public long 자산추가(MyApplication application, String 자산명){

        Data data = new Data();
        data.type = Data.자산;
        data.name = 자산명;

        long row =  application.데이터저장하기(data);
        data.pk = Integer.parseInt(Long.toString(row));
        application.자산데이터.add(data);

        return row;

    }
//    public long 자산삭제(MyApplication application, int pk){
//
//        Data data = new Data();
//        data.type = Data.자산;
//        data.pk = pk;
////        data.name = 자산명;
//
//        long row =  application.데이터수정(data);
//        data.pk = Integer.parseInt(Long.toString(row));
//        application.자산데이터.add(data);
//
//        return row;
//
//    }

}
