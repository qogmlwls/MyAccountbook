package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditRecordActivity extends AppCompatActivity {
    EditText 자산종류, 금액, 내용, 카테고리, 입금입력창, 출금입력창,메모;

    Button 저장하기, 수입으로변경하기, 지출로변경하기, 이체로변경하기,반복버튼;
    ImageButton 사진선택;

    Toolbar toolbar;

    TextView 날짜의시분,날짜의년월,수수료글자;
    Spinner spinner;

    LinearLayout 자산창, 카테고리창, 출금창, 입금창, 분류창,이미지창;

    MyApplication application;

    int year, month, day,hour, min;

    int type;
    String TAG = "EditRecordActivity";

    int 자산pk, 출금pk,입금pk, 카테고리pk, 반복pk;


    boolean repeat;
    String 반복명;
    //    accountBook 가계부;
    SimpleDateFormat 년월일 = new SimpleDateFormat("yy/MM/dd (E)", Locale.KOREA);
    SimpleDateFormat 시분 = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    List<String> 이미지경로;

    Button 삭제;

    accountBook 가계부;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        toolbar = (Toolbar)findViewById(R.id.toolbar2);
//        toolbar.setBack
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        application = (MyApplication)getApplication();



        가계부 = (accountBook) getIntent().getSerializableExtra("가계부");
        position = getIntent().getIntExtra("position",-1);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            가계부 = (accountBook) getIntent().getSerializableExtra("가계부",accountBook.class);
//        }
//        else{
//
//            Log.i(TAG,"가계부 데이터 가져오기 실패");
//            Toast.makeText(application, "가계부 데이터 가져오기 실패", Toast.LENGTH_SHORT).show();
//            finish();
//        }

        Log.i(TAG,Integer.toString(가계부.pk));


        int pk = 가계부.pk;
        삭제 = findViewById(R.id.button23);
        삭제.setVisibility(View.VISIBLE);
        삭제.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                application.deleteRecord(FeedReaderContract.가계부내역.TABLE_NAME,pk);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("key", "value");
//                resultIntent.putExtra("state", "delete");
//                resultIntent.putExtra("pk",pk);
;                resultIntent.putExtra("가계부",가계부);
                resultIntent.putExtra("position",position);
                resultIntent.putExtra("상태","삭제");
                // 결과 설정 및 현재 액티비티 종료
                setResult(RESULT_OK, resultIntent);

                finish();

            }
        });
//        가계부.type


        수입으로변경하기 = findViewById(R.id.button);
        지출로변경하기 = findViewById(R.id.button2);
        이체로변경하기 = findViewById(R.id.button6);
        수수료글자 = findViewById(R.id.textView34);

        입금창 = findViewById(R.id.linearLayout9);
        출금창 = findViewById(R.id.linearLayout8);
        자산창 = findViewById(R.id.linearLayout6);
        카테고리창 = findViewById(R.id.linearLayout7);
        분류창 = findViewById(R.id.linearLayout12);
        이미지창 = findViewById(R.id.linearLayout16);
        저장하기 = findViewById(R.id.button9);

        저장하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "저장하기 시작");

                String originalString = 금액.getText().toString();
                String 금액쉼표제거 = originalString.replace(",", "");

                Log.i("originalString" , originalString);
                Log.i("금액쉼표제거" , 금액쉼표제거);

//                System.out.println(modifiedString); // 출력: "문자열 쉼표 제거하기"

                // 조건 충족하는지 확인
                if(type == accountBook.수입 ||type == accountBook.고정지출 || type == accountBook.변동지출 ){

                    if(자산pk == -1){
//                        Toast.makeText(getBaseContext(), "자산pk 자산을 선택해주세요." +Integer.toString(자산pk), Toast.LENGTH_SHORT).show();

//                        Toast.makeText(getBaseContext(), "자산pk: " + 자산pk, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(), "자산을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(카테고리pk == -1){
                        Toast.makeText(getBaseContext(), "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(금액쉼표제거.length() == 0){
                        Toast.makeText(getBaseContext(), "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(내용.getText().toString().length() == 0){
                        Toast.makeText(getBaseContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                else if(type == accountBook.이체){

                    if(금액쉼표제거.length() == 0){
                        Toast.makeText(getBaseContext(), "금액을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(내용.getText().toString().length() == 0){
                        Toast.makeText(getBaseContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(출금pk == -1){
                        Toast.makeText(getBaseContext(), "출금 자산을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(입금pk == -1){
                        Toast.makeText(getBaseContext(), "입금 자산을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    Log.i(TAG,"type 값 이상함 : "+Integer.toString(type));
                    return;
                }

//                accountBook 가계부 = new accountBook();

                Log.i("금액쉼표제거",금액쉼표제거);

//                int s = Integer.reverse('')
                int 가계부금액 = Integer.parseInt(금액쉼표제거);

//                Integer.getInteger(금액쉼표제거).

                if(type == accountBook.수입 || type == accountBook.고정지출 || type == accountBook.변동지출){
                    가계부.자산식별자 = 자산pk;
                    가계부.카테고리식별자 = 카테고리pk;
                }
                else if(type == accountBook.이체){
                    가계부.입금자산식별자 = 입금pk;
                    가계부.출금자산식별자 = 출금pk;
                }
                else {
                    Log.i(TAG, "type 값 이상함 : " + Integer.toString(type));
                    return;
                }

                가계부.type = type;
                가계부.금액 = 가계부금액;
                가계부.내용 = 내용.getText().toString();
                if(repeat){
                    가계부.반복타입 = Routine.반복있음;
                }
                else{
                    가계부.반복타입 = Routine.반복없음;
                }


                if(메모.getText().toString().length() > 0){
                    가계부.메모 = 메모.getText().toString();
                }

                if(이미지경로.size() == 3){
                    가계부.이미지1 = 이미지경로.get(0);
                    가계부.이미지2 = 이미지경로.get(1);
                    가계부.이미지3 = 이미지경로.get(2);
                }
                else if(이미지경로.size() == 2){
                    가계부.이미지1 = 이미지경로.get(0);
                    가계부.이미지2 = 이미지경로.get(1);
//                    가계부.이미지3 = 이미지경로.get(2);
                }
                else if(이미지경로.size() == 1){
                    가계부.이미지1 = 이미지경로.get(0);
//                    가계부.이미지2 = 이미지경로.get(1);
//                    가계부.이미지3 = 이미지경로.get(2);
                }

                Log.i("이미지",Integer.toString(이미지경로.size()));

                long result;
                if(repeat){

                    가계부.날짜 = 가계부.날짜(year,month,day,0,0);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(year,month-1,day,0,0);
                    가계부.date = calendar1.getTime();

                    result = application.가계부작성하기(가계부,반복명);
                }
                else{

                    가계부.날짜 = 가계부.날짜(year,month,day,hour,min);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(year,month-1,day,hour,min);
                    가계부.date = calendar1.getTime();

//                    result = application.가계부작성하기(가계부);
                }
                application.가계부수정(가계부);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("key", "value");
                resultIntent.putExtra("가계부",가계부);
                resultIntent.putExtra("position",position);
                resultIntent.putExtra("상태","수정");


//                가계부 = (accountBook) getIntent().getSerializableExtra("가계부");

//                resultIntent.putExtra("pk",pk);
//                resultIntent.putExtra("가계부",가계부);

                // 결과 설정 및 현재 액티비티 종료
                setResult(RESULT_OK, resultIntent);

                finish();
//                if(result != -1){
//
//
//
//
//                }
//                else{
//                    Log.i(TAG, "작성하기 실패했습니다.");
////                    Toast.makeText(RecordActivity.this, "작성하기 실패했습니다.", Toast.LENGTH_SHORT).show();
//                }


            }
        });
//        ///
//        저장하기.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                가계부.내용 = 내용.getText().toString();
//                application.가계부수정(가계부);
//
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("key", "value");
////                resultIntent.putExtra("pk",pk);
////                resultIntent.putExtra("가계부",가계부);
//
//                // 결과 설정 및 현재 액티비티 종료
//                setResult(RESULT_OK, resultIntent);
//
//                finish();
//            }
//        });

        수입으로변경하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(RecordActivity.this, "수입으로변경하기" , Toast.LENGTH_SHORT).show();

                지출로변경하기.setBackgroundResource(R.drawable.record_notclick);
                이체로변경하기.setBackgroundResource(R.drawable.record_notclick);
                수입으로변경하기.setBackgroundResource(R.drawable.income_click);
                지출로변경하기.setTextColor(getResources().getColor(R.color.gray));
                이체로변경하기.setTextColor(getResources().getColor(R.color.gray));
                수입으로변경하기.setTextColor(getResources().getColor(R.color.blue));

                카테고리창.setVisibility(View.VISIBLE);
                자산창.setVisibility(View.VISIBLE);

                입금창.setVisibility(View.GONE);
                출금창.setVisibility(View.GONE);
                분류창.setVisibility(View.GONE);
                수수료글자.setVisibility(View.GONE);

                입금pk = -1;
                출금pk = -1;

                입금입력창.setText("");
                출금입력창.setText("");
                spinner.setSelection(0);


                저장하기.setBackgroundColor(getResources().getColor(R.color.blue));

                type = accountBook.수입;
                toolbar.setTitle("수입");
            }
        });
        지출로변경하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                수입으로변경하기.setBackgroundResource(R.drawable.record_notclick);
                이체로변경하기.setBackgroundResource(R.drawable.record_notclick);
                지출로변경하기.setBackgroundResource(R.drawable.expenses_click);
                수입으로변경하기.setTextColor(getResources().getColor(R.color.gray));
                이체로변경하기.setTextColor(getResources().getColor(R.color.gray));
                지출로변경하기.setTextColor(getResources().getColor(R.color.orange));


                저장하기.setBackgroundColor(getResources().getColor(R.color.orange));

                자산창.setVisibility(View.VISIBLE);
                카테고리창.setVisibility(View.VISIBLE);
                분류창.setVisibility(View.VISIBLE);

                입금창.setVisibility(View.GONE);
                출금창.setVisibility(View.GONE);
                수수료글자.setVisibility(View.GONE);

                입금pk = -1;
                출금pk = -1;

                입금입력창.setText("");
                출금입력창.setText("");

                type = accountBook.변동지출;
                toolbar.setTitle("지출");
            }
        });
        이체로변경하기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                수입으로변경하기.setBackgroundResource(R.drawable.record_notclick);
                지출로변경하기.setBackgroundResource(R.drawable.record_notclick);
                이체로변경하기.setBackgroundResource(R.drawable.transfer_click);
                수입으로변경하기.setTextColor(getResources().getColor(R.color.gray));
                지출로변경하기.setTextColor(getResources().getColor(R.color.gray));
                이체로변경하기.setTextColor(getResources().getColor(R.color.black));


                입금창.setVisibility(View.VISIBLE);
                출금창.setVisibility(View.VISIBLE);
                수수료글자.setVisibility(View.VISIBLE);

                분류창.setVisibility(View.GONE);
                카테고리창.setVisibility(View.GONE);
                자산창.setVisibility(View.GONE);

                자산pk = -1;
                카테고리pk = -1;

                카테고리.setText("");
                자산종류.setText("");
                spinner.setSelection(0);

                저장하기.setBackgroundColor(getResources().getColor(R.color.black));
                type = accountBook.이체;
                toolbar.setTitle("이체");

            }
        });

        이미지경로 = new ArrayList<>();

        type = 가계부.type;


        자산pk = 가계부.자산식별자;
        출금pk = 가계부.출금자산식별자;
        입금pk = 가계부.입금자산식별자;
        카테고리pk = 가계부.카테고리식별자;
        반복pk = 0;
//        Toast.makeText(getBaseContext(), "?자산pk " +Integer.toString(자산pk), Toast.LENGTH_SHORT).show();

        repeat = false;
        반복명 = "없음";


        EditText 날짜입력창 = findViewById(R.id.editTextText);
        날짜의년월 = findViewById(R.id.textView9);
//        Log.i("",가계부.날짜);
//        String 날짜 = 가계부.날짜;
        날짜입력창.setClickable(false);
        날짜입력창.setFocusable(false);

//        Date date = 가계부.parseISODate(날짜);
        Date date = 가계부.date;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);

        날짜의년월.setText(년월일.format(date));

        날짜의년월.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int m_year, int m_month, int dayOfMonth) {


                        // 선택된 날짜를 Calendar 객체로 변환
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(m_year, m_month, dayOfMonth);

                        year = m_year;
                        month = m_month+1;
                        day = dayOfMonth;


                        // ISO 8601 형식으로 날짜 저장
                        String isoDate = 년월일.format(selectedDate.getTime());
                        날짜의년월.setText(isoDate);

                    }
                },year,month-1,day);

                dialog.show();

            }
        });

        날짜의시분 = findViewById(R.id.textView10);
        날짜의시분.setText(시분.format(date));
        날짜의시분.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        hour = hourOfDay;
                        min = minute;

                        // 선택된 날짜를 Calendar 객체로 변환
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, day,hourOfDay, minute);

                        // ISO 8601 형식으로 날짜 저장
                        String isoDate = 시분.format(selectedDate.getTime());
                        날짜의시분.setText(isoDate);

                    }
                }, hour, min, false);

                dialog.show();
            }
        });


        반복버튼 = findViewById(R.id.button11);
        반복버튼.setVisibility(View.INVISIBLE);


        자산종류 = findViewById(R.id.editTextText3);
        자산종류.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditRecordActivity.this, AssetActivity.class);
                startActivityForResult(intent,REQUEST_ASSET);

//                startActivityIfNeeded(intent,REQUEST_ASSET);
            }
        });
        자산종류.setClickable(false);
        자산종류.setFocusable(false);


        입금입력창 = findViewById(R.id.editTextText6);
        입금입력창.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditRecordActivity.this, AssetActivity.class);
                startActivityForResult(intent,REQUEST_DEPOSIT);

            }
        });
        입금입력창.setClickable(false);
        입금입력창.setFocusable(false);


        출금입력창 = findViewById(R.id.editTextText7);
        출금입력창.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditRecordActivity.this, AssetActivity.class);
                startActivityForResult(intent,REQUEST_WITHDRAW);
            }
        });
        출금입력창.setClickable(false);
        출금입력창.setFocusable(false);


        사진선택 = findViewById(R.id.imageButton);
        사진선택.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopupMenu 객체 생성
                PopupMenu popup= new PopupMenu(EditRecordActivity.this, v); //두 번째 파라미터가 팝업메뉴가 붙을 뷰
                //PopupMenu popup= new PopupMenu(MainActivity.this, btn2); //첫번째 버튼을 눌렀지만 팝업메뉴는 btn2에 붙어서 나타남
                getMenuInflater().inflate(R.menu.select_image, popup.getMenu());

                //팝업메뉴의 메뉴아이템을 선택하는 것을 듣는 리스너 객체 생성 및 설정
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        int id = menuItem.getItemId();

                        if (id == R.id.camera) {
//                            Toast.makeText(RecordActivity.this, "Option 1 selected", Toast.LENGTH_SHORT).show();
                            //카메라앱 호출을 위한 Intent생성
//                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
//                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

//                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                                // 이미지파일생성
//                                File imageFile = createImageFile();
//                                if (imageFile != null) {
//                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri);
//                                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                                }
//                            }

// 카메라 앱을 실행하여 사진을 찍는 메서드 호출
                            dispatchTakePictureIntent();

                            return true;
                        } else if (id == R.id.gallery) {

                            pickImageFromGallery();
//                            linearLayout16
//                            Toast.makeText(RecordActivity.this, "Option 2 selected", Toast.LENGTH_SHORT).show();
                            return true;
                        }

                        return false;
                    }
                });

                popup.show();


            }
        });


        금액 = findViewById(R.id.editTextText2);
        EditRecordActivity.NumberTextWatcher textWatcher = new EditRecordActivity.NumberTextWatcher(금액);
        금액.addTextChangedListener(textWatcher);

//        RecordActivity.NumberTextWatcher textWatcher = new EditRecordActivity().NumberTextWatcher(금액);
//        금액.addTextChangedListener(textWatcher);

        카테고리 = findViewById(R.id.editTextText9);
        카테고리.setClickable(false);
        카테고리.setFocusable(false);
        카테고리.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditRecordActivity.this, CategoryActivity.class);
                startActivityForResult(intent,REQUEST_CATEGORY);

            }
        });

        내용 = findViewById(R.id.editTextText4);
        메모 = findViewById(R.id.editTextText8);



        // Spinner 가져오기
        spinner = findViewById(R.id.spinner2);

        // Spinner에 들어갈 목록 생성
        List<String> items = new ArrayList<>();
        items.add("변동 지출");
        items.add("고정 지출");

        // ArrayAdapter를 사용하여 Spinner에 목록 추가
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Spinner에서 항목을 선택했을 때 이벤트 처리
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
//                Toast.makeText(RecordActivity.this, "선택된 항목: " + selectedItem, Toast.LENGTH_SHORT).show();

                if(position == 0){
                    type = accountBook.변동지출;
                }
                else if(position == 1){
                    type = accountBook.고정지출;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 아무 항목도 선택하지 않았을 때의 동작
            }
        });


        if(가계부.type != accountBook.이체){
            자산종류.setText(application.자산데이터명(가계부.자산식별자));
            카테고리.setText(application.카테고리데이터명가져오기(가계부.카테고리식별자));
            

        }
        else{
            입금입력창.setText(application.자산데이터명(가계부.입금자산식별자));
            출금입력창.setText(application.자산데이터명(가계부.출금자산식별자));



        }
        내용.setText(가계부.내용);
        DecimalFormat format = new DecimalFormat("#,###");
        금액.setText(format.format(가계부.금액));

        if(가계부.type == accountBook.고정지출){
            spinner.setSelection(1);
        }
        else if(가계부.type == accountBook.변동지출){
            spinner.setSelection(0);
        }



        if(가계부.메모 != null){
            메모.setText(가계부.메모);
        }


        if(가계부.이미지1 != null){
            이미지창.setVisibility(View.VISIBLE);

            Log.i(TAG,"이미지1");

        }

        if(가계부.이미지1 != null){

            Uri photoUri = Uri.parse(가계부.이미지1);

            View view = LayoutInflater.from(이미지창.getContext()).inflate(R.layout.image_item, 이미지창, false);
            ImageButton button = view.findViewById(R.id.imageButton8);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    이미지창.removeView(view);
                    for(int i=0;i<이미지경로.size();i++){
                        if(이미지경로.get(i).equals(photoUri.toString())){
                            이미지경로.remove(i);
                            break;
                        }
                    }

                    if(이미지경로.size() == 0){
                        이미지창.setVisibility(View.GONE);
                    }

                    사진선택.setVisibility(View.VISIBLE);
                }
            });
            ImageView 이미지 = view.findViewById(R.id.imageView4);
            이미지.setImageURI(photoUri);

            이미지창.addView(view);

            int 이미지수 = 이미지경로.size();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(EditRecordActivity.this,ImagePageActivity.class);

                    intent.putExtra("count", 이미지경로.size() );
                    for(int i=0;i<이미지경로.size();i++){
                        intent.putExtra("이미지경로"+Integer.toString(i),이미지경로.get(i));
                        if(이미지경로.get(i).equals(photoUri.toString())){
                            intent.putExtra("position",i );

                        }
                    }
//                intent.putExtra("position",이미지수 );

                    startActivity(intent);
                }
            });
//        이미지1.setImageURI(photoUri);
//        이미지2.setImageURI(photoUri);
//        이미지3.setImageURI(photoUri);
//        file:///storage/emulated/0/Android/data/com.example.myaccountbook/files/Pictures/JPEG_20240103_175935_832471153044161471.jpg
            Log.i("",photoUri.toString());
            이미지경로.add(photoUri.toString());

            if(이미지경로.size() == 3){
                사진선택.setVisibility(View.GONE);

            }

        }
        if(가계부.이미지2 != null){

                Uri photoUri = Uri.parse(가계부.이미지2);

                View view = LayoutInflater.from(이미지창.getContext()).inflate(R.layout.image_item, 이미지창, false);
                ImageButton button = view.findViewById(R.id.imageButton8);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        이미지창.removeView(view);
                        for(int i=0;i<이미지경로.size();i++){
                            if(이미지경로.get(i).equals(photoUri.toString())){
                                이미지경로.remove(i);
                                break;
                            }
                        }

                        if(이미지경로.size() == 0){
                            이미지창.setVisibility(View.GONE);
                        }

                        사진선택.setVisibility(View.VISIBLE);
                    }
                });
                ImageView 이미지 = view.findViewById(R.id.imageView4);
                이미지.setImageURI(photoUri);

                이미지창.addView(view);

                int 이미지수 = 이미지경로.size();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(EditRecordActivity.this,ImagePageActivity.class);

                        intent.putExtra("count", 이미지경로.size() );
                        for(int i=0;i<이미지경로.size();i++){
                            intent.putExtra("이미지경로"+Integer.toString(i),이미지경로.get(i));
                            if(이미지경로.get(i).equals(photoUri.toString())){
                                intent.putExtra("position",i );

                            }
                        }
    //                intent.putExtra("position",이미지수 );

                        startActivity(intent);
                    }
                });
    //        이미지1.setImageURI(photoUri);
    //        이미지2.setImageURI(photoUri);
    //        이미지3.setImageURI(photoUri);
    //        file:///storage/emulated/0/Android/data/com.example.myaccountbook/files/Pictures/JPEG_20240103_175935_832471153044161471.jpg
                Log.i("",photoUri.toString());
                이미지경로.add(photoUri.toString());

                if(이미지경로.size() == 3){
                    사진선택.setVisibility(View.GONE);

                }

            }
        if(가계부.이미지3 != null){

            Uri photoUri = Uri.parse(가계부.이미지3);

            View view = LayoutInflater.from(이미지창.getContext()).inflate(R.layout.image_item, 이미지창, false);
            ImageButton button = view.findViewById(R.id.imageButton8);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    이미지창.removeView(view);
                    for(int i=0;i<이미지경로.size();i++){
                        if(이미지경로.get(i).equals(photoUri.toString())){
                            이미지경로.remove(i);
                            break;
                        }
                    }

                    if(이미지경로.size() == 0){
                        이미지창.setVisibility(View.GONE);
                    }

                    사진선택.setVisibility(View.VISIBLE);
                }
            });
            ImageView 이미지 = view.findViewById(R.id.imageView4);
            이미지.setImageURI(photoUri);

            이미지창.addView(view);

            int 이미지수 = 이미지경로.size();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(EditRecordActivity.this,ImagePageActivity.class);

                    intent.putExtra("count", 이미지경로.size() );
                    for(int i=0;i<이미지경로.size();i++){
                        intent.putExtra("이미지경로"+Integer.toString(i),이미지경로.get(i));
                        if(이미지경로.get(i).equals(photoUri.toString())){
                            intent.putExtra("position",i );

                        }
                    }
//                intent.putExtra("position",이미지수 );

                    startActivity(intent);
                }
            });
//        이미지1.setImageURI(photoUri);
//        이미지2.setImageURI(photoUri);
//        이미지3.setImageURI(photoUri);
//        file:///storage/emulated/0/Android/data/com.example.myaccountbook/files/Pictures/JPEG_20240103_175935_832471153044161471.jpg
            Log.i("",photoUri.toString());
            이미지경로.add(photoUri.toString());

            if(이미지경로.size() == 3){
                사진선택.setVisibility(View.GONE);

            }

        }

////
////        if(가계부.이미지1 != null){
////
////            View view = LayoutInflater.from(이미지창.getContext()).inflate(R.layout.image_item, 이미지창, false);
////            ImageButton button = view.findViewById(R.id.imageButton8);
////            ImageView 이미지 = view.findViewById(R.id.imageView4);
////            이미지.setImageURI(Uri.parse(가계부.이미지1));
////
////            이미지창.addView(view);
////
////            이미지경로.add(가계부.이미지1);
////        }
//
//        if(가계부.이미지2 != null){
//            View view = LayoutInflater.from(이미지창.getContext()).inflate(R.layout.image_item, 이미지창, false);
//            ImageButton button = view.findViewById(R.id.imageButton8);
//            ImageView 이미지 = view.findViewById(R.id.imageView4);
//            이미지.setImageURI(Uri.parse(가계부.이미지2));
//
//            이미지창.addView(view);
//            이미지경로.add(가계부.이미지2);
//
//        }
//
//        if(가계부.이미지3 != null){
//            View view = LayoutInflater.from(이미지창.getContext()).inflate(R.layout.image_item, 이미지창, false);
//            ImageButton button = view.findViewById(R.id.imageButton8);
//            ImageView 이미지 = view.findViewById(R.id.imageView4);
//            이미지.setImageURI(Uri.parse(가계부.이미지3));
//
//            이미지창.addView(view);
//            이미지경로.add(가계부.이미지3);
//        }

        if(가계부.type == accountBook.수입){
            수입으로변경하기.performClick();
        }
        else if(가계부.type == accountBook.이체){
            이체로변경하기.performClick();
        }
        else {
            지출로변경하기.performClick();
        }


        가계부.이미지1 = null;
        가계부.이미지2 = null;
        가계부.이미지3 = null;

    }

    final int SelectRoutlineCode =  100 , REQUEST_ASSET =  102,REQUEST_CATEGORY = 103,REQUEST_DEPOSIT = 104,REQUEST_WITHDRAW = 105;
    final int REQUEST_IMAGE_CAPTURE = 101;


    private void pickImageFromGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickImageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE);
        } else {
            Toast.makeText(this, "갤러리 앱을 열 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private File createFIle() {
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

//        String fileName = "profileSample";
        File imageDir = getApplicationContext().getDir("profileImages", MODE_PRIVATE);

        return new File(imageDir, fileName+".png");
    }
//    private static final int REQUEST_IMAGE_CAPTURE = 111;

    String 임시경로;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = createFIle();
            Uri providerFileUri2 = Uri.fromFile(photoFile);
            if (photoFile.exists()) {
                Log.i("EditRecordActivity","photoFile.exists()");
//                imageView.setImageURI(Uri.fromFile(imgFile));
            }
            else{

                Log.i("EditRecordActivity","photoFile not exists()");

            }

            임시경로 = photoFile.getAbsolutePath();
            Log.i("uri : ",providerFileUri2.toString());

            File imgFile = new File(임시경로);
            if (imgFile.exists()) {
                Log.i("EditRecordActivity","임시경로.exists()");
//                imageView.setImageURI(Uri.fromFile(imgFile));
            }
            else{

                Log.i("EditRecordActivity","임시경로 not exists()");

            }
//            Uri providerFileUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);

            Uri providerFileUri = FileProvider.getUriForFile(this,
                    "com.example.myaccountbook.fileprovider",
                    photoFile);

            Log.i("사진 저장 uri : ",providerFileUri.toString());

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFileUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private Uri imageUri;
    private String currentPhotoPath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void saveImage(Uri selectedImageUri) throws IOException {
        File imageFile = createImageFile();
        Uri photoUri = Uri.fromFile(imageFile);

        // 이미지 복사 및 저장
        ContentResolver resolver = getContentResolver();
        InputStream inputStream = resolver.openInputStream(selectedImageUri);
        OutputStream outputStream = new FileOutputStream(imageFile);

        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }

        inputStream.close();
        outputStream.close();

        이미지창.setVisibility(View.VISIBLE);

        View view = LayoutInflater.from(이미지창.getContext()).inflate(R.layout.image_item, 이미지창, false);
        ImageButton button = view.findViewById(R.id.imageButton8);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                이미지창.removeView(view);
                for(int i=0;i<이미지경로.size();i++){
                    if(이미지경로.get(i).equals(photoUri.toString())){
                        이미지경로.remove(i);
                        break;
                    }
                }

                if(이미지경로.size() == 0){
                    이미지창.setVisibility(View.GONE);
                }

                사진선택.setVisibility(View.VISIBLE);
            }
        });
        ImageView 이미지 = view.findViewById(R.id.imageView4);
        이미지.setImageURI(photoUri);

        이미지창.addView(view);

        int 이미지수 = 이미지경로.size();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditRecordActivity.this,ImagePageActivity.class);

                intent.putExtra("count", 이미지경로.size() );
                for(int i=0;i<이미지경로.size();i++){
                    intent.putExtra("이미지경로"+Integer.toString(i),이미지경로.get(i));
                    if(이미지경로.get(i).equals(photoUri.toString())){
                        intent.putExtra("position",i );

                    }
                }
//                intent.putExtra("position",이미지수 );

                startActivity(intent);
            }
        });
//        이미지1.setImageURI(photoUri);
//        이미지2.setImageURI(photoUri);
//        이미지3.setImageURI(photoUri);
//        file:///storage/emulated/0/Android/data/com.example.myaccountbook/files/Pictures/JPEG_20240103_175935_832471153044161471.jpg
        Log.i("",photoUri.toString());
        이미지경로.add(photoUri.toString());

        if(이미지경로.size() == 3){
            사진선택.setVisibility(View.GONE);

        }

//        Toast.makeText(this, "이미지가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private static final int REQUEST_PICK_IMAGE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG,"onActivityResult");


        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    saveImage(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == REQUEST_ASSET) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {
                    String name = data.getStringExtra("name"); // 결과에서 데이터 추출'
                    int pk = data.getIntExtra("pk",-1); // 결과에서 데이터 추출

                    자산종류.setText(name);
                    자산pk = pk;
                    // 추출된 결과를 사용
//                    Toast.makeText(this, "결과: " + pk, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getBaseContext(), "자산pk " +Integer.toString(자산pk), Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode == RESULT_CANCELED) { // 결과가 CANCELED인 경우
                // 사용자가 액티비티를 취소한 경우
//                Toast.makeText(getBaseContext(), "자산pk 사용자가 취소함" +Integer.toString(자산pk), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getBaseContext(), " ", Toast.LENGTH_SHORT).show();

            }


            if(자산pk != -1){
                List<Data> 자산데이터 = application.자산데이터;
                for (int i=0;i<자산데이터.size();i++){
                    Data 데이터 = 자산데이터.get(i);
                    if(데이터.pk == 자산pk){
                        if(데이터.enable == 1){
                            자산종류.setText("");
                            자산pk = -1;
                        }
                        else{

                            자산종류.setText(데이터.name);

                        }
                    }
                }
            }
        }
        else if (requestCode == REQUEST_CATEGORY) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {
                    String name = data.getStringExtra("name"); // 결과에서 데이터 추출'
                    int pk = data.getIntExtra("pk",-1); // 결과에서 데이터 추출
                    카테고리.setText(name);
                    카테고리pk = pk;
                    // 추출된 결과를 사용
//                    Toast.makeText(this, "결과: " + pk, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getBaseContext(), "자산pk " +Integer.toString(자산pk), Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode == RESULT_CANCELED) { // 결과가 CANCELED인 경우
                // 사용자가 액티비티를 취소한 경우
//                Toast.makeText(this, "사용자가 취소함", Toast.LENGTH_SHORT).show();
            }

            if(카테고리pk != -1){
                List<Data> 카테고리데이터 = application.카테고리데이터;
                for (int i=0;i<카테고리데이터.size();i++){
                    Data 데이터 = 카테고리데이터.get(i);
                    if(데이터.pk == 카테고리pk){
                        if(데이터.enable == 1){
                            카테고리.setText("");
                            카테고리pk = -1;
                        }
                        else{

                            카테고리.setText(데이터.name);

                        }
                    }
                }
            }

        }
        else if (requestCode == REQUEST_DEPOSIT) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {
                    String name = data.getStringExtra("name"); // 결과에서 데이터 추출'
                    int pk = data.getIntExtra("pk",-1); // 결과에서 데이터 추출
                    입금입력창.setText(name);
                    입금pk = pk;
                    // 추출된 결과를 사용
//                    Toast.makeText(this, "결과: " + pk, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) { // 결과가 CANCELED인 경우
                // 사용자가 액티비티를 취소한 경우
//                Toast.makeText(this, "사용자가 취소함", Toast.LENGTH_SHORT).show();
            }

            if(입금pk != -1){
                List<Data> 자산데이터 = application.자산데이터;
                for (int i=0;i<자산데이터.size();i++){
                    Data 데이터 = 자산데이터.get(i);
                    if(데이터.pk == 입금pk){
                        if(데이터.enable == 1){
                            입금입력창.setText("");
                            입금pk = -1;
                        }
                        else{

                            입금입력창.setText(데이터.name);

                        }
                    }
                }
            }


            if(출금pk != -1){
                List<Data> 자산데이터 = application.자산데이터;
                for (int i=0;i<자산데이터.size();i++){
                    Data 데이터 = 자산데이터.get(i);
                    if(데이터.pk == 출금pk){
                        if(데이터.enable == 1){
                            출금입력창.setText("");
                            출금pk = -1;
                        }
                        else{

                            출금입력창.setText(데이터.name);

                        }
                    }
                }
            }

        }
        else if (requestCode == REQUEST_WITHDRAW) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {
                    String name = data.getStringExtra("name"); // 결과에서 데이터 추출'
                    int pk = data.getIntExtra("pk",-1); // 결과에서 데이터 추출
                    출금입력창.setText(name);
                    출금pk = pk;
                    // 추출된 결과를 사용
//                    Toast.makeText(this, "결과: " + pk, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) { // 결과가 CANCELED인 경우
                // 사용자가 액티비티를 취소한 경우
//                Toast.makeText(this, "사용자가 취소함", Toast.LENGTH_SHORT).show();
            }

            if(입금pk != -1){
                List<Data> 자산데이터 = application.자산데이터;
                for (int i=0;i<자산데이터.size();i++){
                    Data 데이터 = 자산데이터.get(i);
                    if(데이터.pk == 입금pk){
                        if(데이터.enable == 1){
                            입금입력창.setText("");
                            입금pk = -1;
                        }
                        else{

                            입금입력창.setText(데이터.name);

                        }
                    }
                }
            }

            if(출금pk != -1){
                List<Data> 자산데이터 = application.자산데이터;
                for (int i=0;i<자산데이터.size();i++){
                    Data 데이터 = 자산데이터.get(i);
                    if(데이터.pk == 출금pk){
                        if(데이터.enable == 1){
                            출금입력창.setText("");
                            출금pk = -1;
                        }
                        else{

                            출금입력창.setText(데이터.name);

                        }
                    }
                }
            }

        }
        else if (requestCode == SelectRoutlineCode) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {
                    String name = data.getStringExtra("name"); // 결과에서 데이터 추출'
                    int pk = data.getIntExtra("pk",-1); // 결과에서 데이터 추출

//                    Toast.makeText(getBaseContext(), "name : "+name, Toast.LENGTH_SHORT).show();

                    if(name.equals("없음")){
                        반복버튼.setTextColor(getResources().getColor(R.color.gray));
                        // 버튼의 CompoundDrawables 가져오기 (좌측, 상단, 우측, 하단)
                        Drawable[] drawables = 반복버튼.getCompoundDrawables();

                        // 원하는 Drawable 위치(예: 우측)의 Tint 설정
                        Drawable drawableRight = drawables[1]; // 우측 Drawable의 인덱스는 2
                        if (drawableRight != null) {
                            // Tint 색상 설정
                            int tintColor = ContextCompat.getColor(this, R.color.gray); // 원하는 색상 지정
                            drawableRight.setTint(tintColor);
//
//                            drawableRight = DrawableCompat.wrap(drawableRight);
//                            DrawableCompat.setTint(drawableRight, tintColor);
//                            DrawableCompat.setTintMode(drawableRight, PorterDuff.Mode.SRC_IN);
//                            반복버튼.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawableRight, drawables[3]);
                        }

                        repeat = false;
                        반복명 = name;

                        날짜의시분.setVisibility(View.VISIBLE);

                    }
                    else{
                        반복버튼.setTextColor(getResources().getColor(R.color.black));
                        // 버튼의 CompoundDrawables 가져오기 (좌측, 상단, 우측, 하단)
                        Drawable[] drawables = 반복버튼.getCompoundDrawables();

                        // 원하는 Drawable 위치(예: 우측)의 Tint 설정
                        Drawable drawableRight = drawables[1]; // 우측 Drawable의 인덱스는 2
                        if (drawableRight != null) {
                            // Tint 색상 설정
                            int tintColor = ContextCompat.getColor(this, R.color.black); // 원하는 색상 지정
//                            drawableRight = DrawableCompat.wrap(drawableRight);
                            drawableRight.setTint(tintColor);
//                            DrawableCompat.setTint(drawableRight, tintColor);
//                            DrawableCompat.setTintMode(drawableRight, PorterDuff.Mode.SRC_IN);
//                            반복버튼.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawableRight,drawables[2] , drawables[3]);
                        }


                        Calendar m_calendar = Calendar.getInstance();
                        m_calendar.set(year,month-1,day);
                        Date date = Routine.getTime(m_calendar.getTime(),name);

                        m_calendar.setTime(date);
                        year = m_calendar.get(Calendar.YEAR);
                        month = m_calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
                        day = m_calendar.get(Calendar.DAY_OF_MONTH);

                        날짜의년월.setText(년월일.format(date));
                        날짜의시분.setVisibility(View.GONE);

                        repeat = true;
                        반복명 = name;
                    }


                    반복버튼.setText(name);
                    반복pk = pk;
                    // 추출된 결과를 사용
//                    Toast.makeText(this, "결과: " + pk, Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode == RESULT_CANCELED) { // 결과가 CANCELED인 경우
                // 사용자가 액티비티를 취소한 경우
//                Toast.makeText(this, "사용자가 취소함", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("EditRecordActivity", "REQUEST_IMAGE_CAPTURE");

            if (data == null) {
                Log.i("EditRecordActivity", "(data == null)");

            } else if (data.getData() == null) {
                Log.i("EditRecordActivity", "(data.getData == null)");

            }


            Log.i("EditRecordActivity", 임시경로);
            File imgFile = new File(임시경로);
            if (imgFile.exists()) {
                Log.i("EditRecordActivity", "임시경로.exists()");
//                imageView.setImageURI(Uri.fromFile(imgFile));
            } else {

                Log.i("EditRecordActivity", "임시경로 not exists()");

            }

            Log.i("이미지창 view수 : ", Integer.toString(이미지창.getChildCount()));
            이미지창.setVisibility(View.VISIBLE);

            String 경로 = imgFile.getAbsolutePath();
            View view = LayoutInflater.from(이미지창.getContext()).inflate(R.layout.image_item, 이미지창, false);
            ImageButton button = view.findViewById(R.id.imageButton8);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    이미지창.removeView(view);
                    for(int i=0;i<이미지경로.size();i++){
                        if(이미지경로.get(i).equals(경로)){
                            이미지경로.remove(i);
                            break;
                        }
                    }

                    if(이미지경로.size() == 0){
                        이미지창.setVisibility(View.GONE);
                    }

                    사진선택.setVisibility(View.VISIBLE);
                }
            });
            ImageView 이미지 = view.findViewById(R.id.imageView4);
            이미지.setImageURI(Uri.parse(경로));

            이미지창.addView(view);

            int 이미지수 = 이미지경로.size();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(EditRecordActivity.this,ImagePageActivity.class);

                    intent.putExtra("count", 이미지경로.size() );
                    for(int i=0;i<이미지경로.size();i++){
                        intent.putExtra("이미지경로"+Integer.toString(i),이미지경로.get(i));
                        if(이미지경로.get(i).equals(경로)){
                            intent.putExtra("position",i);

                        }
                    }



                    startActivity(intent);
                }
            });
//        이미지1.setImageURI(photoUri);
//        이미지2.setImageURI(photoUri);
//        이미지3.setImageURI(photoUri);
//        file:///storage/emulated/0/Android/data/com.example.myaccountbook/files/Pictures/JPEG_20240103_175935_832471153044161471.jpg
            Log.i("",경로);
            이미지경로.add(경로);

            if(이미지경로.size() == 3){
                사진선택.setVisibility(View.GONE);

            }
            Log.i("이미지창 view수 : ", Integer.toString(이미지창.getChildCount()));
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
    class NumberTextWatcher implements TextWatcher {
        private EditText editText;
        private DecimalFormat decimalFormat;
        private String current = "";

        public NumberTextWatcher(EditText editText) {
            this.editText = editText;
            this.decimalFormat = new DecimalFormat("#,###");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No implementation needed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // No implementation needed
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable != null && !editable.toString().equals(current)) {
                editText.removeTextChangedListener(this);

                String userInput = editable.toString().replace(",", "");
                if (!userInput.isEmpty()) {
                    try {
                        long parsed = Long.parseLong(userInput);
                        String formatted = decimalFormat.format(parsed);
                        current = formatted;
                        editText.setText(formatted);
                        editText.setSelection(formatted.length());
                    } catch (NumberFormatException ex) {
                        // Handle the exception as needed
                    }
                }

                editText.addTextChangedListener(this);
            }
        }
    }
}


