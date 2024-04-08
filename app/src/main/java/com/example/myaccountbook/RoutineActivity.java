package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RoutineActivity extends AppCompatActivity {


    final int REQUEST_EDITROUTINE = 101;


    String TAG = "RoutineActivity";

    public SimpleDateFormat 날짜형식 = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);

    RoutineManager  routineManager;
    LinearLayout 수입창,총수입합계,총지출합계,총이체합계,지출창,이체창;

    Toolbar toolbar;

    MyApplication application;

    List<routineRecord> 총수입목록,총지출목록, 총이체목록;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);

        application = (MyApplication)getApplication();

        routineManager = new RoutineManager();
        routineManager.getData(application.readDb,application);

        toolbar = (Toolbar)findViewById(R.id.toolbar6);
//        toolbar.setBack
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        총수입합계 = findViewById(R.id.linearLayout14);
        총지출합계 = findViewById(R.id.linearLayout17);
        총이체합계 = findViewById(R.id.linearLayout19);

        수입창 = findViewById(R.id.linearLayout13);
        지출창 = findViewById(R.id.linearLayout18);
        이체창 = findViewById(R.id.linearLayout20);

//
//        int year = getIntent().getIntExtra("year",-1);
//        int month = getIntent().getIntExtra("month",-1);
//        Log.i("year : ","year : "+Integer.toString(year));
//        Log.i("month : ","month : "+Integer.toString(month));

        총수입목록 = routineManager.총수입목록반환();

        if(총수입목록.size() >0){
            총수입합계.setVisibility(View.VISIBLE);
            수입창.setVisibility(View.VISIBLE);
        }
        else{
            총수입합계.setVisibility(View.GONE);
            수입창.setVisibility(View.GONE);
        }


        for(int i=0;i<총수입목록.size();i++){

            routineRecord 반복 = 총수입목록.get(i);

            View view = LayoutInflater.from(수입창.getContext()).inflate(R.layout.routine_item, 수입창, false);
            수입창.addView(view);

            TextView 시작시간 = view.findViewById(R.id.textView42);

            Date date = 반복.내역.parseISODate(반복.date);

            시작시간.setText(날짜형식.format(date));

            TextView 반복명 = view.findViewById(R.id.textView43);
            반복명.setText(반복.name);

            TextView 내용 = view.findViewById(R.id.textView44);
            내용.setText(반복.내역.내용);

            TextView 가격 = view.findViewById(R.id.textView45);
            가격.setText(반복.내역.금액원+"원");
            가격.setTextColor(Color.BLUE);

            ImageButton 수정 = view.findViewById(R.id.imageButton7);
            ImageButton 삭제 = view.findViewById(R.id.imageButton6);

            수정.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RoutineActivity.this, EditRoutineActivity.class);


//                    accountBook 가계부 = list.get(position);
////                    Toast.makeText(context, "상세내역", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(context, EditRecordActivity.class);
                    intent.putExtra("가계부",반복.내역);
                    intent.putExtra("반복명",반복.name);

//                    Log.i("",가계부.날짜);
//                    Activity activity = (Activity)context;
//                    activity.startActivityForResult(intent,REQUEST_EDIT);
//

                    startActivityForResult(intent, REQUEST_EDITROUTINE );

                }
            });

            삭제.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialog dialog = new MyDialog();
                    Dialog  삭제다이얼로그 = dialog.삭제다이얼로그생성(RoutineActivity.this);
                    삭제다이얼로그.show();

                    Button 아니오 = 삭제다이얼로그.findViewById(R.id.button13);
                    Button 예 = 삭제다이얼로그.findViewById(R.id.button20);

                    아니오.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            삭제다이얼로그.dismiss();
                        }
                    });
                    예.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int pk = 반복.pk;
                            int ABpk = 반복.AB_pk;

                            application.deleteRecord(FeedReaderContract.반복내역테이블.TABLE_NAME,pk);
                            application.deleteRecord(FeedReaderContract.가계부내역.TABLE_NAME,ABpk);


                            수입창.removeView(view);
                            삭제다이얼로그.dismiss();

                            if(수입창.getChildCount() >0){
                                총수입합계.setVisibility(View.VISIBLE);
                                수입창.setVisibility(View.VISIBLE);
                            }
                            else{
                                총수입합계.setVisibility(View.GONE);
                                수입창.setVisibility(View.GONE);
                            }


                            Intent resultIntent = new Intent();
//                            resultIntent.putExtra("name", data.name);
//                            resultIntent.putExtra("pk", data.pk);
//
//                            Activity activity = (Activity)context;
                            // 결과 설정 및 현재 액티비티 종료
                            setResult(RESULT_OK, resultIntent);


                        }
                    });

               }
            });


        }


        총지출목록 = routineManager.총지출목록반환();

        if(총지출목록.size() >0){
            총지출합계.setVisibility(View.VISIBLE);
            지출창.setVisibility(View.VISIBLE);
        }
        else{
            총지출합계.setVisibility(View.GONE);
            지출창.setVisibility(View.GONE);
        }

        for(int i=0;i<총지출목록.size();i++){

            routineRecord 반복 = 총지출목록.get(i);

            View view = LayoutInflater.from(지출창.getContext()).inflate(R.layout.routine_item, 지출창, false);
            지출창.addView(view);

            TextView 시작시간 = view.findViewById(R.id.textView42);

            Date date = 반복.내역.parseISODate(반복.date);

            시작시간.setText(날짜형식.format(date));

            TextView 반복명 = view.findViewById(R.id.textView43);
            반복명.setText(반복.name);

            TextView 내용 = view.findViewById(R.id.textView44);
            내용.setText(반복.내역.내용);

            TextView 가격 = view.findViewById(R.id.textView45);
            가격.setText(반복.내역.금액원+"원");
            가격.setTextColor(Color.RED);

            ImageButton 수정 = view.findViewById(R.id.imageButton7);
            ImageButton 삭제 = view.findViewById(R.id.imageButton6);

            수정.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RoutineActivity.this, EditRoutineActivity.class);

                    intent.putExtra("가계부",반복.내역);
                    intent.putExtra("반복명",반복.name);

                    startActivityForResult(intent, REQUEST_EDITROUTINE );
                }
            });

            삭제.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialog dialog = new MyDialog();
                    Dialog  삭제다이얼로그 = dialog.삭제다이얼로그생성(RoutineActivity.this);
                    삭제다이얼로그.show();

                    Button 아니오 = 삭제다이얼로그.findViewById(R.id.button13);
                    Button 예 = 삭제다이얼로그.findViewById(R.id.button20);

                    아니오.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            삭제다이얼로그.dismiss();
                        }
                    });
                    예.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int pk = 반복.pk;
                            int ABpk = 반복.AB_pk;

                            application.deleteRecord(FeedReaderContract.반복내역테이블.TABLE_NAME,pk);
                            application.deleteRecord(FeedReaderContract.가계부내역.TABLE_NAME,ABpk);


                            지출창.removeView(view);
                            삭제다이얼로그.dismiss();

                            if(지출창.getChildCount() >0){
                                총지출합계.setVisibility(View.VISIBLE);
                                지출창.setVisibility(View.VISIBLE);
                            }
                            else{
                                총지출합계.setVisibility(View.GONE);
                                지출창.setVisibility(View.GONE);
                            }


                            Intent resultIntent = new Intent();
//                            resultIntent.putExtra("name", data.name);
//                            resultIntent.putExtra("pk", data.pk);
//
//                            Activity activity = (Activity)context;
                            // 결과 설정 및 현재 액티비티 종료
                            setResult(RESULT_OK, resultIntent);

                        }
                    });

                }
            });

        }



        총이체목록 = routineManager.총이체목록반환();

        if(총이체목록.size() >0){
            총이체합계.setVisibility(View.VISIBLE);
            이체창.setVisibility(View.VISIBLE);
        }
        else{
            총이체합계.setVisibility(View.GONE);
            이체창.setVisibility(View.GONE);
        }

        for(int i=0;i<총이체목록.size();i++){

            routineRecord 반복 = 총이체목록.get(i);

            View view = LayoutInflater.from(이체창.getContext()).inflate(R.layout.routine_item, 이체창, false);
            이체창.addView(view);

            TextView 시작시간 = view.findViewById(R.id.textView42);

            Date date = 반복.내역.parseISODate(반복.date);

            시작시간.setText(날짜형식.format(date));

            TextView 반복명 = view.findViewById(R.id.textView43);
            반복명.setText(반복.name);

            TextView 내용 = view.findViewById(R.id.textView44);
            내용.setText(반복.내역.내용);

            TextView 가격 = view.findViewById(R.id.textView45);
            가격.setText(반복.내역.금액원+"원");
            가격.setTextColor(Color.BLACK);

            ImageButton 수정 = view.findViewById(R.id.imageButton7);
            ImageButton 삭제 = view.findViewById(R.id.imageButton6);

            수정.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RoutineActivity.this, EditRoutineActivity.class);

                    intent.putExtra("가계부",반복.내역);
                    intent.putExtra("반복명",반복.name);
                    startActivityForResult(intent, REQUEST_EDITROUTINE );
                }
            });

            삭제.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDialog dialog = new MyDialog();
                    Dialog  삭제다이얼로그 = dialog.삭제다이얼로그생성(RoutineActivity.this);
                    삭제다이얼로그.show();

                    Button 아니오 = 삭제다이얼로그.findViewById(R.id.button13);
                    Button 예 = 삭제다이얼로그.findViewById(R.id.button20);

                    아니오.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            삭제다이얼로그.dismiss();
                        }
                    });
                    예.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int pk = 반복.pk;
                            int ABpk = 반복.AB_pk;

                            application.deleteRecord(FeedReaderContract.반복내역테이블.TABLE_NAME,pk);
                            application.deleteRecord(FeedReaderContract.가계부내역.TABLE_NAME,ABpk);


                            이체창.removeView(view);
                            삭제다이얼로그.dismiss();

                            if(이체창.getChildCount() >0){
                                총이체합계.setVisibility(View.VISIBLE);
                                이체창.setVisibility(View.VISIBLE);
                            }
                            else{
                                총이체합계.setVisibility(View.GONE);
                                이체창.setVisibility(View.GONE);
                            }


                            Intent resultIntent = new Intent();
//                            resultIntent.putExtra("name", data.name);
//                            resultIntent.putExtra("pk", data.pk);
//
//                            Activity activity = (Activity)context;
                            // 결과 설정 및 현재 액티비티 종료
                            setResult(RESULT_OK, resultIntent);

                        }
                    });

                }
            });

        }

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


    private static final int REQUEST_PICK_IMAGE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult");

        if (requestCode == REQUEST_EDITROUTINE && resultCode == RESULT_OK && data != null) {

            routineManager.getData(application.readDb,application);

            수입창.removeAllViews();
            지출창.removeAllViews();
            이체창.removeAllViews();


            총수입목록 = routineManager.총수입목록반환();

            if(총수입목록.size() >0){
                총수입합계.setVisibility(View.VISIBLE);
                수입창.setVisibility(View.VISIBLE);
            }
            else{
                총수입합계.setVisibility(View.GONE);
                수입창.setVisibility(View.GONE);
            }


            for(int i=0;i<총수입목록.size();i++){

                routineRecord 반복 = 총수입목록.get(i);

                View view = LayoutInflater.from(수입창.getContext()).inflate(R.layout.routine_item, 수입창, false);
                수입창.addView(view);

                TextView 시작시간 = view.findViewById(R.id.textView42);

                Date date = 반복.내역.parseISODate(반복.date);

                시작시간.setText(날짜형식.format(date));

                TextView 반복명 = view.findViewById(R.id.textView43);
                반복명.setText(반복.name);

                TextView 내용 = view.findViewById(R.id.textView44);
                내용.setText(반복.내역.내용);

                TextView 가격 = view.findViewById(R.id.textView45);
                가격.setText(반복.내역.금액원+"원");
                가격.setTextColor(Color.BLUE);

                ImageButton 수정 = view.findViewById(R.id.imageButton7);
                ImageButton 삭제 = view.findViewById(R.id.imageButton6);

                수정.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RoutineActivity.this, EditRoutineActivity.class);


//                    accountBook 가계부 = list.get(position);
////                    Toast.makeText(context, "상세내역", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(context, EditRecordActivity.class);
                        intent.putExtra("가계부",반복.내역);
                        intent.putExtra("반복명",반복.name);

//                    Log.i("",가계부.날짜);
//                    Activity activity = (Activity)context;
//                    activity.startActivityForResult(intent,REQUEST_EDIT);
//

                        startActivityForResult(intent, REQUEST_EDITROUTINE );

                    }
                });

                삭제.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog dialog = new MyDialog();
                        Dialog  삭제다이얼로그 = dialog.삭제다이얼로그생성(RoutineActivity.this);
                        삭제다이얼로그.show();

                        Button 아니오 = 삭제다이얼로그.findViewById(R.id.button13);
                        Button 예 = 삭제다이얼로그.findViewById(R.id.button20);

                        아니오.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                삭제다이얼로그.dismiss();
                            }
                        });
                        예.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int pk = 반복.pk;
                                int ABpk = 반복.AB_pk;

                                application.deleteRecord(FeedReaderContract.반복내역테이블.TABLE_NAME,pk);
                                application.deleteRecord(FeedReaderContract.가계부내역.TABLE_NAME,ABpk);


                                수입창.removeView(view);
                                삭제다이얼로그.dismiss();

                                if(수입창.getChildCount() >0){
                                    총수입합계.setVisibility(View.VISIBLE);
                                    수입창.setVisibility(View.VISIBLE);
                                }
                                else{
                                    총수입합계.setVisibility(View.GONE);
                                    수입창.setVisibility(View.GONE);
                                }


                                Intent resultIntent = new Intent();
//                            resultIntent.putExtra("name", data.name);
//                            resultIntent.putExtra("pk", data.pk);
//
//                            Activity activity = (Activity)context;
                                // 결과 설정 및 현재 액티비티 종료
                                setResult(RESULT_OK, resultIntent);


                            }
                        });

                    }
                });


            }


            총지출목록 = routineManager.총지출목록반환();

            if(총지출목록.size() >0){
                총지출합계.setVisibility(View.VISIBLE);
                지출창.setVisibility(View.VISIBLE);
            }
            else{
                총지출합계.setVisibility(View.GONE);
                지출창.setVisibility(View.GONE);
            }

            for(int i=0;i<총지출목록.size();i++){

                routineRecord 반복 = 총지출목록.get(i);

                View view = LayoutInflater.from(지출창.getContext()).inflate(R.layout.routine_item, 지출창, false);
                지출창.addView(view);

                TextView 시작시간 = view.findViewById(R.id.textView42);

                Date date = 반복.내역.parseISODate(반복.date);

                시작시간.setText(날짜형식.format(date));

                TextView 반복명 = view.findViewById(R.id.textView43);
                반복명.setText(반복.name);

                TextView 내용 = view.findViewById(R.id.textView44);
                내용.setText(반복.내역.내용);

                TextView 가격 = view.findViewById(R.id.textView45);
                가격.setText(반복.내역.금액원+"원");
                가격.setTextColor(Color.RED);

                ImageButton 수정 = view.findViewById(R.id.imageButton7);
                ImageButton 삭제 = view.findViewById(R.id.imageButton6);

                수정.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RoutineActivity.this, EditRoutineActivity.class);

                        intent.putExtra("가계부",반복.내역);
                        intent.putExtra("반복명",반복.name);

                        startActivityForResult(intent, REQUEST_EDITROUTINE );
                    }
                });

                삭제.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog dialog = new MyDialog();
                        Dialog  삭제다이얼로그 = dialog.삭제다이얼로그생성(RoutineActivity.this);
                        삭제다이얼로그.show();

                        Button 아니오 = 삭제다이얼로그.findViewById(R.id.button13);
                        Button 예 = 삭제다이얼로그.findViewById(R.id.button20);

                        아니오.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                삭제다이얼로그.dismiss();
                            }
                        });
                        예.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int pk = 반복.pk;
                                int ABpk = 반복.AB_pk;

                                application.deleteRecord(FeedReaderContract.반복내역테이블.TABLE_NAME,pk);
                                application.deleteRecord(FeedReaderContract.가계부내역.TABLE_NAME,ABpk);


                                지출창.removeView(view);
                                삭제다이얼로그.dismiss();

                                if(지출창.getChildCount() >0){
                                    총지출합계.setVisibility(View.VISIBLE);
                                    지출창.setVisibility(View.VISIBLE);
                                }
                                else{
                                    총지출합계.setVisibility(View.GONE);
                                    지출창.setVisibility(View.GONE);
                                }


                                Intent resultIntent = new Intent();
//                            resultIntent.putExtra("name", data.name);
//                            resultIntent.putExtra("pk", data.pk);
//
//                            Activity activity = (Activity)context;
                                // 결과 설정 및 현재 액티비티 종료
                                setResult(RESULT_OK, resultIntent);

                            }
                        });

                    }
                });

            }



            총이체목록 = routineManager.총이체목록반환();

            if(총이체목록.size() >0){
                총이체합계.setVisibility(View.VISIBLE);
                이체창.setVisibility(View.VISIBLE);
            }
            else{
                총이체합계.setVisibility(View.GONE);
                이체창.setVisibility(View.GONE);
            }

            for(int i=0;i<총이체목록.size();i++){

                routineRecord 반복 = 총이체목록.get(i);

                View view = LayoutInflater.from(이체창.getContext()).inflate(R.layout.routine_item, 이체창, false);
                이체창.addView(view);

                TextView 시작시간 = view.findViewById(R.id.textView42);

                Date date = 반복.내역.parseISODate(반복.date);

                시작시간.setText(날짜형식.format(date));

                TextView 반복명 = view.findViewById(R.id.textView43);
                반복명.setText(반복.name);

                TextView 내용 = view.findViewById(R.id.textView44);
                내용.setText(반복.내역.내용);

                TextView 가격 = view.findViewById(R.id.textView45);
                가격.setText(반복.내역.금액원+"원");
                가격.setTextColor(Color.BLACK);

                ImageButton 수정 = view.findViewById(R.id.imageButton7);
                ImageButton 삭제 = view.findViewById(R.id.imageButton6);

                수정.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RoutineActivity.this, EditRoutineActivity.class);

                        intent.putExtra("가계부",반복.내역);
                        intent.putExtra("반복명",반복.name);
                        startActivityForResult(intent, REQUEST_EDITROUTINE );
                    }
                });

                삭제.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyDialog dialog = new MyDialog();
                        Dialog  삭제다이얼로그 = dialog.삭제다이얼로그생성(RoutineActivity.this);
                        삭제다이얼로그.show();

                        Button 아니오 = 삭제다이얼로그.findViewById(R.id.button13);
                        Button 예 = 삭제다이얼로그.findViewById(R.id.button20);

                        아니오.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                삭제다이얼로그.dismiss();
                            }
                        });
                        예.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int pk = 반복.pk;
                                int ABpk = 반복.AB_pk;

                                application.deleteRecord(FeedReaderContract.반복내역테이블.TABLE_NAME,pk);
                                application.deleteRecord(FeedReaderContract.가계부내역.TABLE_NAME,ABpk);


                                이체창.removeView(view);
                                삭제다이얼로그.dismiss();

                                if(이체창.getChildCount() >0){
                                    총이체합계.setVisibility(View.VISIBLE);
                                    이체창.setVisibility(View.VISIBLE);
                                }
                                else{
                                    총이체합계.setVisibility(View.GONE);
                                    이체창.setVisibility(View.GONE);
                                }


                                Intent resultIntent = new Intent();
//                            resultIntent.putExtra("name", data.name);
//                            resultIntent.putExtra("pk", data.pk);
//
//                            Activity activity = (Activity)context;
                                // 결과 설정 및 현재 액티비티 종료
                                setResult(RESULT_OK, resultIntent);

                            }
                        });

                    }
                });

            }



            Intent resultIntent = new Intent();
//                            resultIntent.putExtra("name", data.name);
//                            resultIntent.putExtra("pk", data.pk);
//
//                            Activity activity = (Activity)context;
            // 결과 설정 및 현재 액티비티 종료
            setResult(RESULT_OK, resultIntent);


        }


    }

}