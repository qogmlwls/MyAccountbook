package com.example.myaccountbook.data;

public class 자산통계데이터 {

    public int 월;
    public Amount 누적잔액;
    public Amount 총수입;
    public Amount 총지출;
    public 자산통계데이터(){
        누적잔액 = new Amount();
        누적잔액.amount = 0;

        총수입 = new Amount();
        총수입.amount = 0;

        총지출 = new Amount();
        총지출.amount = 0;
    }
}
