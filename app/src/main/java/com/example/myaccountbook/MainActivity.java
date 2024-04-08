package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.MainData;
import com.example.myaccountbook.data.RequestCode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends CommonActivity {

    String TAG = "MainActivity";


    TextView 날짜;

    Button 총수입, 총지출, 정기지출, 변동지출;
    MainActivityDataManager dataManager;
    Date 기준이되는날짜, 현재날짜;
    MainData data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        print("--------------------------------------");

        총수입 = findViewById(R.id.button18);
        총지출 = findViewById(R.id.button19);
        정기지출 = findViewById(R.id.button15);
        변동지출 = findViewById(R.id.button16);

        날짜 = findViewById(R.id.textView);

        FloatingActionButton 지출화면으로이동 = findViewById(R.id.floatingActionButton);
        Button 달력 = findViewById(R.id.button12);
        Button 결산 = findViewById(R.id.button14);
        Button 가계부 = findViewById(R.id.button3);
        Button 통계 = findViewById(R.id.button4);
        Button 자산 = findViewById(R.id.button5);

        print("--------------------------------------");

        print("기준이되는날짜, 현재날짜 에 값 할당");

        기준이되는날짜 = new Date();
        기준이되는날짜.today();
        현재날짜 = new Date();
        현재날짜.today();
//        기준이되는날짜 = Date.today();
//        현재날짜 = Date.today();
        print("기준이되는날짜 : "+Integer.toString(기준이되는날짜.getYear())+Integer.toString(기준이되는날짜.getMonth())+Integer.toString(기준이되는날짜.getDay()));
        print("현재날짜 : "+Integer.toString(현재날짜.getYear())+Integer.toString(현재날짜.getMonth())+Integer.toString(현재날짜.getDay()));

        print("--------------------------------------");

        날짜.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.number_picker, null, false);


                NumberPicker year = view.findViewById(R.id.yearpicker_datepicker);
                NumberPicker month = view.findViewById(R.id.monthpicker_datepicker);

                //  순환 안되게 막기
                year.setWrapSelectorWheel(false);
                month.setWrapSelectorWheel(false);

                //  editText 설정 해제
                year.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                month.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

                //  최소값 설정
                year.setMinValue(2000);
                month.setMinValue(1);

                //  최대값 설정
                year.setMaxValue(2100);
                month.setMaxValue(12);

                // 현재 값 설정
                year.setValue(기준이되는날짜.getYear());
                month.setValue(기준이되는날짜.getMonth());

                String[] strings = new String[101];
                for (int i = 0; i <= 100; i++) {
                    String 날짜 = Integer.toString(i + 2000) + "년";
                    strings[i] = 날짜;
                }

                year.setDisplayedValues(strings);


                String[] strings2 = new String[12];
                for (int i = 0; i < 12; i++) {
                    String 날짜 = Integer.toString(i + 1) + "월";
                    strings2[i] = 날짜;
                }

                month.setDisplayedValues(strings2);

                builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        int yearValue = year.getValue();
                        int monthValue = month.getValue();

                        기준이되는날짜.setYear(yearValue);
                        기준이되는날짜.setMonth(monthValue);
                        기준이되는날짜.setDay(1);

                        data = dataManager.데이터반환(기준이되는날짜);
                        화면그리기(data);

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                builder.setView(view);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        총수입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainDataDetailActivity.class);
                intent.putExtra("날짜",기준이되는날짜);
                intent.putExtra("type",accountBook.수입);

                startActivityForResult(intent,RequestCode.REQUEST_MAIN);
            }
        });

        총지출.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainDataDetailActivity.class);
                intent.putExtra("날짜",기준이되는날짜);
                intent.putExtra("type",accountBook.지출);
                
                startActivityForResult(intent,RequestCode.REQUEST_MAIN);
            }
        });

        정기지출.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainDataDetailActivity.class);
                intent.putExtra("날짜",기준이되는날짜);
                intent.putExtra("type",accountBook.고정지출);

                startActivityForResult(intent,RequestCode.REQUEST_MAIN);
            }
        });

        변동지출.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MainDataDetailActivity.class);
                intent.putExtra("날짜",기준이되는날짜);
                intent.putExtra("type",accountBook.변동지출);

                startActivityForResult(intent,RequestCode.REQUEST_MAIN);
            }
        });

        지출화면으로이동.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivityForResult(intent,REQUEST_RECORD);
//                overridePendingTransition(R.anim.from_left_enter,R.anim.none);
//                overridePendingTransition(0,0);

            }
        });


        달력.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                intent.putExtra("날짜",기준이되는날짜);
                startActivityForResult(intent,RequestCode.REQUEST_MAIN);
//                startActivityForResult(CalendarActivity.class, RequestCode.REQUEST_MAIN);
            }
        });

        결산.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BudgetActivity.class);
                intent.putExtra("날짜",기준이되는날짜);
                startActivityForResult(intent,RequestCode.REQUEST_MAIN);
            }
        });
        
        가계부.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                기준이되는날짜 = new Date();
                기준이되는날짜.today();
//                기준이되는날짜 = Date.today();
                data = dataManager.데이터반환(기준이되는날짜);
                화면그리기(data);


            }
        });

        통계.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(PieChartActivity.class, RequestCode.REQUEST_MAIN);
            }
        });

        
        자산.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AssetPageActivity.class, RequestCode.REQUEST_MAIN);
            }
        });


        print("--------------------------------------");


        MyApplication application = (MyApplication)getApplication();
        dataManager = new MainActivityDataManager(application.readDb);
        print("MainActivityDataManager 객체 생성 : "+dataManager.toString());


        data = dataManager.데이터반환(기준이되는날짜);
        화면그리기(data);

        print("화면 그리기");

        print("--------------------------------------");

    }

    void 날짜변경(){

    }


    void 화면그리기(MainData data){

        날짜.setText(기준이되는날짜.년월날짜());
        
        총수입.setText("총 수입\n\n"+data.총수입가져오기());
        총지출.setText("총 지출 \n\n"+data.총지출가져오기());
        정기지출.setText("고정 지출\n 지출 "+data.총정기지출가져오기());
        변동지출.setText("변동 지출\n"+"\n지출 "+data.총변동지출가져오기()+"\n변동 지출 예산 "+data.전체예산가져오기());

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }

    final int REQUEST_RECORD = 101;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RECORD) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {
                    accountBook 가계부 = (accountBook) data.getSerializableExtra("accountBook");
                    // 추출된 결과를 사용
//                    Toast.makeText(this, "결과: " + 가계부.날짜, Toast.LENGTH_SHORT).show();
                }

                this.data = dataManager.데이터반환(기준이되는날짜);
                화면그리기( this.data );

            } else if (resultCode == RESULT_CANCELED) { // 결과가 CANCELED인 경우
                // 사용자가 액티비티를 취소한 경우
//                Toast.makeText(this, "사용자가 취소함", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == RequestCode.REQUEST_MAIN) {

            if (resultCode == RESULT_OK) { // 결과가 OK인 경우

                if (data != null) {

                }
                else{

                }

                this.data = dataManager.데이터반환(기준이되는날짜);
                화면그리기( this.data );

            } else if (resultCode == RESULT_CANCELED) { // 결과가 CANCELED인 경우
                // 사용자가 액티비티를 취소한 경우
//                Toast.makeText(this, "사용자가 취소함", Toast.LENGTH_SHORT).show();
            }
        }




    }


}