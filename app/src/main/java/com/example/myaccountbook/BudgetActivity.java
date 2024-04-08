package com.example.myaccountbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myaccountbook.adapter.BudgetAdapter;
import com.example.myaccountbook.adapter.ImagePageAdapter;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BudgetActivity extends AppCompatActivity {

    private boolean isLoading = false;


    List<Date> list;
    BudgetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerView4);

        list = new ArrayList<>();
        Date today = (Date) getIntent().getSerializableExtra("날짜");

//        Date today = Date.today();
        list.add(today);
//        list.add(Date.이후날짜(today));
//        list.add(0,Date.이전날짜(today));


        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);


        adapter = new BudgetAdapter(BudgetActivity.this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(BudgetActivity.this,LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
//        recyclerView.setAdapter(new BudgetAdapter(list,this));


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
//                            Date date = list.get(list.size()-1);
//                            date.다음달로이동(date);
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
                            list.add(0,date);
                            adapter.notifyItemInserted(0);
                        }


                    }
                }

            }
        });

    }


    final int REQUEST_CHANGE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CHANGE) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {

//                    Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//                    listAdapter.setList(list);
                    adapter.noti();



                }
            }
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
//        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }
}

