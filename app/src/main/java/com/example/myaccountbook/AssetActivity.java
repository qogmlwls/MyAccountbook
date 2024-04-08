package com.example.myaccountbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myaccountbook.adapter.AssetAdapter;

import java.util.ArrayList;
import java.util.List;

// 자산
public class AssetActivity extends AppCompatActivity {
    AssetAdapter adapter;
    List<Data> list;
    String TAG = "AssetActivity";
    MyApplication application;

    ImageButton 추가;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar4);
//        toolbar.setBack
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        application = (MyApplication)getApplication();

        RecyclerView 리사이클러뷰;
        리사이클러뷰 = findViewById(R.id.recyclerView1);


        추가 = findViewById(R.id.imageButton2);
        추가.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                MyDialog myDialog = new MyDialog();
//                dialog.자산추가(AssetActivity.this,application,adapter);

                Dialog dialog = myDialog.자산추가수정다이얼로그생성(AssetActivity.this);
                dialog.show();
                Button 자산종류추가버튼 = dialog.findViewById(R.id.button17);
                EditText 입력값 = dialog.findViewById(R.id.editTextText5);

                자산종류추가버튼.setText("자산 종류 추가");
                자산종류추가버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(입력값.getText().toString().length() == 0){
                            Toast.makeText(AssetActivity.this, "자산 명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Data data = new Data();
                            data.type = Data.자산;
                            data.name = 입력값.getText().toString();

                            long row =  application.데이터저장하기(data);
                            data.pk = Integer.parseInt(Long.toString(row));
                            application.자산데이터.add(data);
                            list.add(data);
                            adapter.notifyDataSetChanged();
//                            dialog.cancel();
                            dialog.dismiss();
                        }

                    }
                });
            }
        });


        list = application.삭제안된자산데이터();
        adapter = new AssetAdapter(list, this);
        리사이클러뷰.setAdapter(adapter);
        리사이클러뷰.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 뒤로가기 버튼을 눌렀을 때의 동작
        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            Toast.makeText(application, "취소", Toast.LENGTH_SHORT).show();
            this.setResult(RESULT_CANCELED,new Intent());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}