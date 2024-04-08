package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;

import com.example.myaccountbook.view.amountEditText;

public class AssetCreateActivity extends CommonActivity {


    Button 저장하기버튼;
    EditText 금액입력창;
    EditText 자산명입력창;

    amountEditText 금액창helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_create);

        print("--------------------");
        print("뒤로가기 버튼 추가와");
        print("XML 레이아웃 파일에 있는 View 요소들의 객체에 대한 참조 가져오기 시작");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar15);
//        toolbar.setBack
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


        자산명입력창 = findViewById(R.id.editTextText13);
        금액입력창 = findViewById(R.id.editTextNumber4);

        저장하기버튼 = findViewById(R.id.button39);

        print("--------------------");

        print("금액창helper 객체 생성");
        금액창helper = new amountEditText();

        print("--------------------");

        저장하기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 자산 추가
                // 금액 값 작성하면
                // 해당하는 내역 기록도 같이 추가.

                String 자산명 = 자산명입력창.getText().toString();

                if(자산명입력창.getText().toString().length() == 0){
                    MyDialog myDialog = new MyDialog();
                    Dialog dialog = myDialog.이름이없습니다다이얼로그생성(getContext());
                    // 이름 미입력시,
                    // 이름이 없습니다. 확인. 다이얼로그 출력.
                    dialog.show();
                    return;
                }


                // DB에 자산 추가
                Data data = new Data();
                long 자산식별자 = data.자산추가((MyApplication) getApplication(),자산명입력창.getText().toString());
                // 자산식별자가 -1이면 추가에 실패한 것임.


                int 금액 = 0;
                // 금액 입력 + 입력한 금액이 0 보다 클때
                // 새로운 수입 내역 추가.
                if(금액입력창.getText().toString().length() != 0){

                    금액  = 금액창helper.금액쉼표제거(금액입력창);

                    if(금액 > 0){

                        // 금액 입력 + 입력한 금액이 0 보다 클때
                        // 새로운 수입 내역 추가.
                        MyApplication application = (MyApplication)getApplication();
                        int 카테고리식별자 = application.카테고리식별자("자산 수정");
                        accountBook accountBook = new accountBook(com.example.myaccountbook.accountBook.수입,(int) 자산식별자,금액, 카테고리식별자);

//                        MyApplication application = (MyApplication) getApplication();
                        application.가계부작성하기(accountBook);


                    }

                }



                // 자산으로 돌아갈 때, 이전 엑티비티에
                // 추가한 자산명과, 식별자값, 금액 보내기.

                // 최하단에 추가됨. (이전 엑티비티에 )
                // 금액은 상단 자산, 부채, 합계에도 반영이 되어야 함.


                Intent resultIntent = new Intent();

                resultIntent.putExtra("금액", 금액);
                resultIntent.putExtra("name", 자산명);
                resultIntent.putExtra("pk",(int)자산식별자);

                // 결과 설정 및 현재 액티비티 종료
                setResult(RESULT_OK, resultIntent);

                finish();


            }
        });

        print("저장하기 버튼에 클릭 이벤트 리스너 설정");
        print("--------------------");


        금액창helper.setTextWatcher(금액입력창);
        print("금액입력창에 setTextWatcher 리스너 설정");
        print("--------------------");






    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 뒤로가기 버튼을 눌렀을 때의 동작
        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            Toast.makeText(application, "취소", Toast.LENGTH_SHORT).show();
//            this.setResult(RESULT_CANCELED,new Intent());
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}