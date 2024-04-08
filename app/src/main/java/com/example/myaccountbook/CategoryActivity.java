package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myaccountbook.adapter.AssetAdapter;
import com.example.myaccountbook.data.Budget;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    MyApplication application;
    AssetAdapter adapter;

    List<Data> list;

    ImageButton 추가버튼;

    boolean 통계청목록클릭;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar5);
//        toolbar.setBack
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        application = (MyApplication)getApplication();

        RecyclerView 리사이클러뷰;
        리사이클러뷰 = findViewById(R.id.recyclerView2);


        list = application.삭제안된카테고리데이터();

        int type = getIntent().getIntExtra("type",-1);
        if(type == Budget.ActivityType){
            Data data = new Data();
            data.name = Budget.전체;
            data.type = Data.카테고리;
            data.pk = Budget.전체식별자;
            data.enable = 0;

            list.add(0,data);
        }

        adapter = new AssetAdapter(list, this);
        리사이클러뷰.setAdapter(adapter);
        리사이클러뷰.setLayoutManager(new LinearLayoutManager(this));

        추가버튼 = findViewById(R.id.imageButton5);
        추가버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                통계청목록클릭 = false;


                MyDialog myDialog = new MyDialog();
//                dialog.자산추가(AssetActivity.this,application,adapter);

                Dialog dialog = myDialog.카테고리추가수정다이얼로그생성(CategoryActivity.this);
                dialog.show();
                Button 카테고리추가버튼 = dialog.findViewById(R.id.button21);
                카테고리추가버튼.setText("카테고리 추가");
                EditText 입력값 = dialog.findViewById(R.id.editTextText10);
                Spinner 통계청카테고리 = dialog.findViewById(R.id.spinner);

                EditText 입력값2 = dialog.findViewById(R.id.editTextText12);
                입력값2.setClickable(false);
                입력값2.setFocusable(false);
                입력값2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        통계청목록클릭 = true;
//                        Toast.makeText(CategoryActivity.this, "click", Toast.LENGTH_SHORT).show();
                        통계청카테고리.performClick();
//                        입력값2.

                    }
                });

                카테고리추가버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(입력값.getText().toString().length() == 0){
                            Toast.makeText(CategoryActivity.this, "카테고리 명을 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else if(입력값2.getText().toString().length()==0){
                            Toast.makeText(CategoryActivity.this, "카테고리 설명을 선택해주세요", Toast.LENGTH_SHORT).show();
                        }
                        else{

                            // 카테고리 추가.
                            Data data = new Data();
                            data.type = Data.카테고리;
                            data.name = 입력값.getText().toString();
                            data.office_category = application.카테고리데이터식별자(입력값2.getText().toString());


                            if(data.office_category == -1){
                                Toast.makeText(CategoryActivity.this, "문제 발생", Toast.LENGTH_SHORT).show();
                                Log.i("TAG","data office_category 이상.");
                                Log.i("TAG","data office_category : "+Integer.toString(data.office_category));
                                return;
                            }
                            long row =  application.데이터저장하기(data);
                            data.pk = Integer.parseInt(Long.toString(row));
                            application.카테고리데이터.add(data);
                            list.add(data);
                            adapter.notifyDataSetChanged();
//                            dialog.cancel();
                            dialog.dismiss();

                        }
                    }
                });

//                통계청카테고리.setVisibility(View.INVISIBLE);
//                통계청카테고리.onClick();
                // Spinner에 들어갈 목록 생성
                List<String> items = application.통계청가져오기();

//                Toast.makeText(application, Integer.toString(items.size()), Toast.LENGTH_SHORT).show();

                // ArrayAdapter를 사용하여 Spinner에 목록 추가
                ArrayAdapter<String> adapter = new ArrayAdapter<>(CategoryActivity.this, android.R.layout.simple_spinner_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                통계청카테고리.setAdapter(adapter);

                // Spinner에서 항목을 선택했을 때 이벤트 처리
                통계청카테고리.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = parent.getItemAtPosition(position).toString();
//                Toast.makeText(RecordActivity.this, "선택된 항목: " + selectedItem, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(application,selectedItem, Toast.LENGTH_SHORT).show();

                        if(통계청목록클릭)
                            입력값2.setText(selectedItem);
                        통계청목록클릭 = false;

//                        int position1 = adapter.getPosition(selectedItem);
//                        통계청카테고리.setSelection(position1);
//                        if(position == 0){
//                            type = accountBook.변동지출;
//                        }
//                        else if(position == 1){
//                            type = accountBook.고정지출;
//                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // 아무 항목도 선택하지 않았을 때의 동작
                    }
                });

//                입력값.setText("");
//                // 현재 선택된 항목이 있을 경우 선택된 항목을 표시
//                String currentlySelectedValue = "현재 선택된 값"; // 현재 선택된 값을 여기에 넣으세요
//                int position = adapter.getPosition(currentlySelectedValue);
//                통계청카테고리.setSelection(position);
//

//                통계청카테고리.setSelection(-1);



            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 뒤로가기 버튼을 눌렀을 때의 동작
        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}