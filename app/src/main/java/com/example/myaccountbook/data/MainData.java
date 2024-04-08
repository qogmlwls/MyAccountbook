package com.example.myaccountbook.data;

public class MainData {


    Amount 수입, 지출,정기지출, 변동지출, 전체예산;

    public MainData(){
        수입 = new Amount();
        지출 = new Amount();
        정기지출 = new Amount();
        변동지출 = new Amount();
        전체예산 = new Amount();
    }

    public String 총수입가져오기(){
        return 수입.getAmount();
    }

    public String 총지출가져오기(){
        return 지출.getAmount();
    }

    public String 총정기지출가져오기(){
        return 정기지출.getAmount();
    }

    public String 총변동지출가져오기(){
        return 변동지출.getAmount();
    }

    public String 전체예산가져오기(){
        return 전체예산.getAmount();
    }

    public void setData(int 총수입, int 총정기지출, int 총변동지출){
        수입.amount = 총수입;
        지출.amount = 총정기지출+총변동지출;
        정기지출.amount = 총정기지출;
        변동지출.amount = 총변동지출;
    }

    public void addData(int 전체예산) {
        this.전체예산.amount += 전체예산;
    }


}
