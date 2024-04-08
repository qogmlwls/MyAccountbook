package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myaccountbook.adapter.BudgetAdapter;
import com.example.myaccountbook.adapter.BudgetGraphAdapter;
import com.example.myaccountbook.data.Date;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BudgetGraphActivity extends AppCompatActivity {

    String TAG = "BudgetGraphActivity";
    private boolean isLoading = false;

    List<Date> list;

    RecyclerView recyclerView;
    BudgetGraphAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_graph);

        recyclerView = findViewById(R.id.recyclerView6);


        list = new ArrayList<>();
        int year = getIntent().getIntExtra("year",-1);
        int month = getIntent().getIntExtra("month",-1);
        int 카테고리식별자 = getIntent().getIntExtra("category",-1);
        int 예산 = getIntent().getIntExtra("예산",-1);
        int 지출 = getIntent().getIntExtra("지출",-1);
//        intent.putExtra("예산",settlement.예산.예산);
//        intent.putExtra("지출",settlement.지출);
        Date date = new Date();
        date.setYear(year);
        date.setMonth(month);
        date.setDay(1);
        Log.i(TAG,Integer.toString(month));

        list.add(date);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        adapter = new BudgetGraphAdapter(BudgetGraphActivity.this,list,카테고리식별자);
        recyclerView.setLayoutManager(new LinearLayoutManager(BudgetGraphActivity.this,LinearLayoutManager.HORIZONTAL, false));
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
                            Date date1 = list.get(list.size()-1);
                            Date date = date1.clone(date1);
                            date.다음달로이동();
//                            Date date = Date.이후날짜(list.get(list.size()-1));
                            list.add(date);
                            adapter.notifyItemInserted(list.size()-1);
                        }



//                        getPosts();
                    }
                    if(firstVisibleItemPosition == 0){
//                        Toast.makeText(BudgetActivity.this, "getChats", Toast.LENGTH_SHORT).show();
                        for(int i=0;i<3;i++){


                            Date date = list.get(0).clone(list.get(0));
                            date.이전달로이동();

//                            Date date = Date.이전날짜(list.get(0));
                            list.add(0,date);
                            adapter.notifyItemInserted(0);
                        }


                    }
                }

            }
        });
    }

//
    final int REQUEST_EDIT = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult");


        if (requestCode == REQUEST_EDIT && resultCode == RESULT_OK && data != null) {


            adapter.notifyDataSetChanged();
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