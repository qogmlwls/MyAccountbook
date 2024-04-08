package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myaccountbook.adapter.SelectRoutineAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectRoutineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_routine);

        RecyclerView 반복종류목록 = findViewById(R.id.recyclerView);

        MyApplication application = (MyApplication) getApplication();
        List<Data> routines  = application.반복데이터;

        SelectRoutineAdapter adapter = new SelectRoutineAdapter(routines,this);
        반복종류목록.setAdapter(adapter);
        반복종류목록.setLayoutManager(new LinearLayoutManager(this));


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar3);
//        toolbar.setBack
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

    }

    void setData(List<String> routines){

        routines.add("없음");
        routines.add("매일");
        routines.add("주중");
        routines.add("주말");
        routines.add("매주");
        routines.add("2주마다");
        routines.add("4주마다");
        routines.add("매월");
        routines.add("월말");
        routines.add("2개월마다");
        routines.add("3개월마다");
        routines.add("4개월마다");
        routines.add("6개월마다");
        routines.add("1년마다");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(this, Integer.toString(item.getItemId()), Toast.LENGTH_SHORT).show();


        // 뒤로가기 버튼을 눌렀을 때의 동작
        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}