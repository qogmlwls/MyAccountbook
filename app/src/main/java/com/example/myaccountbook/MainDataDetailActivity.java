package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.myaccountbook.adapter.AssetDetailAdapter;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.MainData;
import com.example.myaccountbook.data.RequestCode;

import java.util.List;

public class MainDataDetailActivity extends CommonActivity {

    Toolbar toolbar;
    TextView 날짜, 글자, 금액, 내역갯수;

    RecyclerView recyclerView;

    int type;
    Date 기준이되는날짜;

    MainActivityDataManager dataManager;
    MainData data;

    AssetDetailAdapter adapter;

    List<accountBook> list;

    MyApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_data_detail);

        print("--------------------------------------");

        toolbar = findViewById(R.id.toolbar19);
        날짜 = findViewById(R.id.textView136);
        글자 = findViewById(R.id.textView137);
        금액 = findViewById(R.id.textView138);
        내역갯수 = findViewById(R.id.textView139);
        recyclerView = findViewById(R.id.recyclerView14);

        print("--------------------------------------");

        type = getIntent().getIntExtra("type",-1);
        기준이되는날짜 = (Date)getIntent().getSerializableExtra("날짜");

        print("type : ",Integer.toString(type));
        print("기준이 되는 날짜", Integer.toString(기준이되는날짜.getYear())+ Integer.toString(기준이되는날짜.getMonth())+ Integer.toString(기준이되는날짜.getDay()));

        print("--------------------------------------");

        if(type == accountBook.수입){
            글자.setText("총 수입");
        }
        else if(type == accountBook.고정지출){
            글자.setText("총 고정지출");
        }
        else if(type == accountBook.변동지출){
            글자.setText("총 변동지출");
        }
        else if(type == accountBook.지출){
            글자.setText("총 지출");
        }

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        adapter = new AssetDetailAdapter(MainDataDetailActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        application = (MyApplication)getApplication();
        dataManager = new MainActivityDataManager(application.readDb);
        print("MainActivityDataManager 객체 생성 : "+dataManager.toString());


        print("--------------------------------------");


        날짜.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainDataDetailActivity.this);
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.number_picker, null, false);


                NumberPicker year = view.findViewById(R.id.yearpicker_datepicker);
                NumberPicker month = view.findViewById(R.id.monthpicker_datepicker);

                //  순환 안되게 막기
                year.setWrapSelectorWheel(false);
                month.setWrapSelectorWheel(false);

                //  editText 설정 해제
                year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                //  최소값 설정
                year.setMinValue(2000);
                month.setMinValue(1);

                //  최대값 설정
                year.setMaxValue(2100);
                month.setMaxValue(12);

                // 현재 값 설정
                year.setValue(기준이되는날짜.getYear());
                month.setValue(기준이되는날짜.getMonth());

                String[] strings = new String[101];
                for (int i = 0; i <= 100; i++) {
                    String 날짜 = Integer.toString(i + 2000) + "년";
                    strings[i] = 날짜;
                }

                year.setDisplayedValues(strings);


                String[] strings2 = new String[12];
                for (int i = 0; i < 12; i++) {
                    String 날짜 = Integer.toString(i + 1) + "월";
                    strings2[i] = 날짜;
                }

                month.setDisplayedValues(strings2);

                builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        int yearValue = year.getValue();
                        int monthValue = month.getValue();

                        기준이되는날짜.setYear(yearValue);
                        기준이되는날짜.setMonth(monthValue);
                        기준이되는날짜.setDay(1);
//
                        data = dataManager.데이터반환(기준이되는날짜);
                        화면그리기(data);
                        내역데이터가져와서adapter에설정();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        print("--------------------------------------");


        data = dataManager.데이터반환(기준이되는날짜);
        화면그리기(data);
        내역데이터가져와서adapter에설정();
        print("--------------------------------------");


    }

    void 내역데이터가져와서adapter에설정(){

        if(list != null){
            list.clear();
            adapter.clear();
        }

        list = dataManager.내역반환(기준이되는날짜, type,application);
        if(list.size() == 0){
            TextView textView = findViewById(R.id.textView140);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            TextView textView = findViewById(R.id.textView140);
            textView.setVisibility(View.INVISIBLE);
        }
        내역갯수.setText("총 "+Integer.toString(list.size())+"건");

        adapter.setData(list,"");

    }

    void 화면그리기(MainData data){

        날짜.setText(기준이되는날짜.년월날짜());

        if(type == accountBook.수입){
            금액.setText(data.총수입가져오기());
        }
        else if(type == accountBook.고정지출){
            금액.setText(data.총정기지출가져오기());
        }
        else if(type == accountBook.변동지출){
            금액.setText(data.총변동지출가져오기());
        }
        else if(type == accountBook.지출){
            금액.setText(data.총지출가져오기());
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.REQUEST_ASSET_RECORD) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {
//                    accountBook 가계부 = (accountBook) data.getSerializableExtra("accountBook");
//                    // 추출된 결과를 사용
////                    Toast.makeText(this, "결과: " + 가계부.날짜, Toast.LENGTH_SHORT).show();
                }

                this.data = dataManager.데이터반환(기준이되는날짜);
                화면그리기(this.data);
                내역데이터가져와서adapter에설정();

                setResult(RESULT_OK);
            } else if (resultCode == RESULT_CANCELED) { // 결과가 CANCELED인 경우
                // 사용자가 액티비티를 취소한 경우
//                Toast.makeText(this, "사용자가 취소함", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 뒤로가기 버튼을 눌렀을 때의 동작
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}