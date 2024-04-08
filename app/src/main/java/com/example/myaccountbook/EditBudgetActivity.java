package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myaccountbook.adapter.BudgetEditAdapter;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Settlement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EditBudgetActivity extends AppCompatActivity {

    private boolean isLoading = false;

    BudgetEditAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget);

        BudgetManager budgetManager;
        budgetManager = new BudgetManager();

        MyApplication application;
        application = (MyApplication)getApplication();

        Date date = new Date();
        int category_pk = getIntent().getIntExtra("category_pk",-1);
        int year = getIntent().getIntExtra("year",-1);
        int month = getIntent().getIntExtra("month",-1);
        date.setYear(year);
        date.setMonth(month);

        date.setDay(1);

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerView8);

        List<Date> list;
        list = new ArrayList<>();
        list.add(date);


        adapter = new BudgetEditAdapter(EditBudgetActivity.this,list,category_pk,date);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(EditBudgetActivity.this,LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        // 스크롤 리스너 설정
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

//                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                        && firstVisibleItemPosition >= 0) {
//                    // 다음 페이지 로드
//                    getPosts();
//                }
                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        // 다음 페이지 로드
//                        Toast.makeText(BudgetActivity.this, "getPosts", Toast.LENGTH_SHORT).show();


                        for(int i=0;i<3;i++){
                            Date date = Date.다음해날짜(list.get(list.size()-1));
                            list.add(date);
                            adapter.notifyItemInserted(list.size()-1);
                        }



//                        getPosts();
                    }
                    if(firstVisibleItemPosition == 0){
//                        Toast.makeText(BudgetActivity.this, "getChats", Toast.LENGTH_SHORT).show();
                        for(int i=0;i<3;i++){
                            Date date = Date.이전해날짜(list.get(0));
                            list.add(0,date);
                            adapter.notifyItemInserted(0);
                        }


                    }
                }

            }
        });

    }


    final int REQUEST_EDIT = 103;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {


//                    ada
                    adapter.noti();

                    Intent resultIntent = new Intent();
                    // 결과 설정 및 현재 액티비티 종료
                    setResult(RESULT_OK, resultIntent);
//                    activity.finish();


                }
            }
        }
    }

                    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        Toast.makeText(this, Integer.toString(item.getItemId()), Toast.LENGTH_SHORT).show();


        // 뒤로가기 버튼을 눌렀을 때의 동작
        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}