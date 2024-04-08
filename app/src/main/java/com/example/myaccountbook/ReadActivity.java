package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ReadActivity extends AppCompatActivity {

    String TAG = "ReadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        Button button = findViewById(R.id.button10);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ReadActivity.this,MainActivity2.class);
                startActivity(intent);

//                dl(MainActivity.class);


            }
        });
    }

    public <T> void dl(Class<T> d){

        try {
            T dd =  d.newInstance(); // 클래스의 기본 생성자를 호출하여 인스턴스 생성
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }
}