package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.defaultBudget;
import com.example.myaccountbook.view.amountEditText;

public class CreateBudgetActivity extends AppCompatActivity {


    final int REQUEST_CATEGORY = 100;


    Toolbar toolbar;

    EditText 카테고리분류, 금액;
    Button 저장하기;
//    Budget budget;
    defaultBudget budget;

    MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);

        toolbar = findViewById(R.id.toolbar8);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        budget = new defaultBudget();
        int year = getIntent().getIntExtra("year",-1);
        int month = getIntent().getIntExtra("month",-1);
        Date date = new Date();
        date.setYear(year);
        date.setMonth(month);
//        budget.
        budget.date = date;

        application = (MyApplication)getApplication();


        카테고리분류 = findViewById(R.id.editTextText11);
        카테고리분류.setClickable(false);
        카테고리분류.setFocusable(false);

        카테고리분류.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateBudgetActivity.this, CategoryActivity.class);
                intent.putExtra("type",Budget.ActivityType);
                startActivityForResult(intent,REQUEST_CATEGORY);

            }
        });

        금액 = findViewById(R.id.editTextNumber);
        amountEditText 금액창helper = new amountEditText();
        금액창helper.setTextWatcher(금액);
//        금액창helper.금액쉼표제거();


        저장하기 = findViewById(R.id.button7);
        저장하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(카테고리분류.getText().toString().length() == 0){
                    Toast.makeText(CreateBudgetActivity.this, "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(금액.getText().toString().length()==0){
                    Toast.makeText(CreateBudgetActivity.this, "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                budget.예산 = (금액창helper.금액쉼표제거(금액));

                Log.i("Create budget : ",Integer.toString(금액창helper.금액쉼표제거(금액)));


                // 기본예산 or 일반예산 추가
                // 이미 있다면 일반예산 수정.
//                application
                application.예산추가(budget);

//                budget.

                Intent resultIntent = new Intent();


//                resultIntent.putExtra("금액",I);

                setResult(RESULT_OK, resultIntent);
                finish();


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CATEGORY) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {

                    String name = data.getStringExtra("name"); // 결과에서 데이터 추출'
                    int pk = data.getIntExtra("pk",-1); // 결과에서 데이터 추출
                    카테고리분류.setText(name);
                    if(pk == -1){
                        Toast.makeText(application, "카테고리 가져오기 실패", Toast.LENGTH_SHORT).show();
                    }

                    Log.i("REQUEST_CATEGORY","카테고리 가져오기 실패");

//                    카테고리pk = pk;
                    budget.카테고리이름 = name;
                    budget.카테고리식별자 = pk;


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