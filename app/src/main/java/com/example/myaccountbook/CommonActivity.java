package com.example.myaccountbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CommonActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        print("CommonActivity onCreate 실행.");
        super.onCreate(savedInstanceState);
    }

    protected void startActivity(Class activity){
        print("startActivity(Class activity) 시작");
        print("매개변수 activity.getName",activity.getName());

        Intent intent = new Intent(CommonActivity.this,activity);
        print("intent 생성");
        startActivity(intent);
        print("startActivity(intent) 실행.");

        print("startActivity(Class activity) 끝");


    }

    protected void startActivityForResult(Class activity,int requestCode){

        print("startActivityForResult(Class activity,int requestCode) 시작");
        print("매개변수 activity.getName",activity.getName());
        print("매개변수 requestCode",Integer.toString(requestCode));


        Intent intent = new Intent(CommonActivity.this,activity);
        print("intent 생성");
        startActivityForResult(intent, requestCode);
        print("startActivityForResult(intent, requestCode) 실행.");

        print("startActivityForResult(Class activity,int requestCode) 끝");

    }
    protected void print(String message){
        Log.i(CommonActivity.this.getLocalClassName(), message);
    }
    protected void print(String key,String value){
        Log.i(CommonActivity.this.getLocalClassName(), key +" : "+value);
    }

    protected Context getContext(){
        return CommonActivity.this;
    }

}
