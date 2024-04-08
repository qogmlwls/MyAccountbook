package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class AssetEditActivity extends CommonActivity {


    String 자산명;
    int 자산식별자;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_edit);

        자산명 = getIntent().getStringExtra("자산명");
        자산식별자 = getIntent().getIntExtra("자산식별자",-1);

        print("자산식별자",Integer.toString(자산식별자));



        // 뒤로가기
        Toolbar toolbar = findViewById(R.id.toolbar20);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        ImageButton 자산삭제버튼 = findViewById(R.id.imageButton27);
        자산삭제버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("변경","삭제");
                MyApplication application = (MyApplication) getApplication();
                application.데이터삭제(자산식별자);
                // 결과 설정 및 현재 액티비티 종료
                setResult(RESULT_OK, resultIntent);

                finish();

            }
        });

        EditText 자산입력창 = findViewById(R.id.editTextText14);
        자산입력창.setText(자산명);


        Button 자산수정 = findViewById(R.id.button42);
        자산수정.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String 변경할자산명 = 자산입력창.getText().toString();

                if(자산입력창.getText().toString().length() == 0){
                    Toast.makeText(AssetEditActivity.this, "자산명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("변경","수정");
                resultIntent.putExtra("자산명",변경할자산명);
                MyApplication application = (MyApplication) getApplication();
                application.데이터명수정(자산식별자,변경할자산명);
                // 결과 설정 및 현재 액티비티 종료
                setResult(RESULT_OK, resultIntent);

                finish();


            }
        });


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