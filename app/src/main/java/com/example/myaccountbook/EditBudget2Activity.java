package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.view.amountEditText;

public class EditBudget2Activity extends AppCompatActivity {


    Toolbar toolbar;

    TextView 년월;
    EditText 금액입력창;
    Button 저장하기;

    MyApplication application;

    int month;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_budget2);

        toolbar = findViewById(R.id.toolbar11);

        년월 = findViewById(R.id.textView71);
        금액입력창 = findViewById(R.id.editTextNumber2);

        저장하기 = findViewById(R.id.button24);


        application = (MyApplication) getApplication();

        int year = getIntent().getIntExtra("year",-1);
        month = getIntent().getIntExtra("month",-1);
        int category_pk = getIntent().getIntExtra("category_pk",-1);

        String 금액 = getIntent().getStringExtra("amount");
        금액입력창.setText(금액);
        
        if(category_pk == Budget.전체식별자){
            toolbar.setTitle(Budget.전체);
        }
        else{
            toolbar.setTitle(application.카테고리데이터명가져오기(category_pk));


        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        if(month == 0){
            년월.setText(Integer.toString(year)+"년");
        }
        else{
            년월.setText(Integer.toString(year)+"년 "+Integer.toString(month)+"월");
        }

        amountEditText 금액보조 = new amountEditText();
        금액보조.setTextWatcher(금액입력창);


        저장하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(금액입력창.getText().toString().length() == 0){

                    Toast.makeText(EditBudget2Activity.this, "예산을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                // 기본 값 변경

                
                // 또는
                // 예산 변경

                int 금액 = 금액보조.금액쉼표제거(금액입력창);
                
                if(month == 0){
                    //기본값변경
                    application.기본예산수정(category_pk,금액);

                }
                else{
                   // 예산 추가 또는 변경a
                    application.일반예산추가수정(category_pk,금액,year,month);

                }

                Intent resultIntent = new Intent();
                // 결과 설정 및 현재 액티비티 종료
                setResult(RESULT_OK, resultIntent);
                finish();

            }
        });

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