package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;

import com.example.myaccountbook.adapter.BudgetListAdapter;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.defaultBudget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SetBudgetActivity extends AppCompatActivity {

    final int REQUEST_CREATE = 100;
    final int REQUEST_EDIT = 101;


    Toolbar toolbar;

    ImageButton 예산추가버튼;


    RecyclerView recyclerView;

    MyApplication application;

    Date date;

    List<Budget> list;

    BudgetListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        toolbar = findViewById(R.id.toolbar7);

        예산추가버튼 = findViewById(R.id.imageButton10);
        recyclerView = findViewById(R.id.recyclerView5);

        application = (MyApplication)getApplication();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        예산추가버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SetBudgetActivity.this, CreateBudgetActivity.class);

                intent.putExtra("year",date.getYear());
                intent.putExtra("month",date.getMonth());
                startActivityForResult(intent,REQUEST_CREATE);

            }
        });

        int year = getIntent().getIntExtra("year",-1);
        int month = getIntent().getIntExtra("month",-1);

        date = new Date();
        date.setYear(year);
        date.setMonth(month);

        list = application.년월예산목록(date.getYear(),date.getMonth());

        list.sort(new Comparator<Budget>() {
            @Override
            public int compare(Budget o1, Budget o2) {
                return o1.카테고리식별자 - o2.카테고리식별자;
            }
        });


        listAdapter = new BudgetListAdapter(SetBudgetActivity.this,list);
//        list.

        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CREATE) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {

                    String name = data.getStringExtra("name"); // 결과에서 데이터 추출'
                    int pk = data.getIntExtra("pk",-1); // 결과에서 데이터 추출
//                    카테고리분류.setText(name);
//                    카테고리pk = pk;

                    list = application.년월예산목록(date.getYear(),date.getMonth());
                    list.sort(new Comparator<Budget>() {
                        @Override
                        public int compare(Budget o1, Budget o2) {
                            return o1.카테고리식별자 - o2.카테고리식별자;
                        }
                    });
                    listAdapter.setList(list);
                    listAdapter.notifyDataSetChanged();


                    Intent resultIntent = new Intent();
//                    Activity activity = (Activity)context;
                    // 결과 설정 및 현재 액티비티 종료
                    setResult(RESULT_OK, resultIntent);
//                    activity.finish();
                }
            }
        }
        else if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {

                    list = application.년월예산목록(date.getYear(),date.getMonth());
                    list.sort(new Comparator<Budget>() {
                        @Override
                        public int compare(Budget o1, Budget o2) {
                            return o1.카테고리식별자 - o2.카테고리식별자;
                        }
                    });
                    listAdapter.setList(list);
                    listAdapter.notifyDataSetChanged();

                    Intent resultIntent = new Intent();
//                    Activity activity = (Activity)context;
                    // 결과 설정 및 현재 액티비티 종료
                    setResult(RESULT_OK, resultIntent);
//                    activity.finish();

                }
            }
        }

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