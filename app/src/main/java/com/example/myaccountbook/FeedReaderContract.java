package com.example.myaccountbook;


import android.provider.BaseColumns;

// 테이블 이름, 열 이름들 가지고 있는 클래스
// 공식문서 설명 : 테이블 이름과 RSS 피드를 나타내는 단일 테이블의 열 이름을 정의합니다.
// 잘 모르겠는데, 그냥 위의 내용으로 채움
public final class FeedReaderContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry1";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    
    // 자산 테이블, 카테고리, 통계청 카테고리 static 느낌의 데이터들?
    /* Inner class that defines the table contents */
    public static class 데이터테이블 implements BaseColumns {
        public static final String TABLE_NAME = "data";
        public static final String COLUMN_PK = "pk";

        // 타입별 자산, 카테고리, 통계청카테고리
        public static final String COLUMN_TYPE = "type";

        // 자산명
        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_CYCLE = "cycle";


        // 통계청의 지출 종류(통계청 사이트에서 가구당 지출 평균을 보여주는 데이터가 있고, 보여줄때의 지출 카테고리)
        // 식비, 통신, 교통 등.
        public static final String COLUMN_CATEGORY = "category";

        // 자산을 삭제한다면 비활성화해두고, 더이상 해당 자산명으로 새로운 데이터 추가 못하도록.
        // 이전 데이터들은 유지할 수 있도록 하기 위함.
        public static final String COLUMN_ENABLE = "enable";

        // 삭제한 날 (통계때 삭제 안한날까지는 통계에 나오도록 혹시나할까봐)
        public static final String COLUMN_DELETEDATE = "delete_date";



    }

    public static class 가계부내역 implements BaseColumns {

        public static final String TABLE_NAME = "accountbook";

        public static final String COLUMN_PK = "pk";
        
        // 수입(1)인지 고정지출(2)인지 변동지출(3)인지 이체(4)인지 구분하는 값
        public static final String COLUMN_TYPE = "type";

        // 어떤 자산과 관련된 수입or지출인지 자산 식별자
        public static final String COLUMN_ASSET_PK = "asset_pk";

        // 카테고리 식별자
        public static final String COLUMN_CATEGORY_PK = "category_pk";

        // 금액 (이체는 수수료)
        public static final String COLUMN_AMOUNT = "amount";

        public static final String COLUMN_DATE = "date";

        // 입금 자산 식별자
        public static final String COLUMN_DEPOSIT = "deposit";

        // 출금 자산 식별자
        public static final String COLUMN_WITHDRAW = "withdraw";

        // 반복인지 아닌지
        public static final String COLUMN_ROUTINETYPEs = "routine";

        public static final String COLUMN_CONTENT = "content";


        // 메모
        public static final String COLUMN_MEMO = "memo";

        // 이미지 3장
        public static final String COLUMN_IMAGE1 = "image1";
        public static final String COLUMN_IMAGE2 = "image2";
        public static final String COLUMN_IMAGE3 = "image3";

    }

    public static class 반복내역테이블 implements BaseColumns {
        public static final String TABLE_NAME = "routine";
        public static final String COLUMN_PK = "pk";

        // 반복할 내역을 가진 가계부 데이터 식별자
        public static final String COLUMN_ABPK = "AB_pk";

        // 반복이름
        public static final String COLUMN_NAME = "name";

        // 내역이 생성될 날짜
        public static final String COLUMN_DATE = "date";


    }


    public static class 일반예산테이블 implements BaseColumns {
        public static final String TABLE_NAME = "budget2";
        public static final String COLUMN_PK = "pk";

        // 반복이름
        public static final String COLUMN_YEAR = "year";

        // 내역이 생성될 날짜
        public static final String COLUMN_MONTH = "month";

        public static final String COLUMN_AMOUNT = "amount";

        public static final String COLUMN_CATEGORY_PK = "category_pk";

    }

    public static class 기본예산테이블 implements BaseColumns {
        public static final String TABLE_NAME = "budget1";
        public static final String COLUMN_PK = "pk";

        public static final String COLUMN_AMOUNT = "amount";

        public static final String COLUMN_CATEGORY_PK = "category_pk";

    }
}

