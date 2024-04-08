package com.example.myaccountbook;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.myaccountbook.adapter.AssetPageAdapter;
import com.example.myaccountbook.data.Amount;
import com.example.myaccountbook.data.Formatter;
import com.example.myaccountbook.data.RequestCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class AssetPageActivity extends CommonActivity {

    AssetDataManager assetDataManager;
    AssetPageAdapter adapter;
    MyApplication application;
    List<AssetDataManager.자산별합계> list;

    TextView 자산텍스트뷰,부채텍스트뷰,합계텍스트뷰;
    PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        print("onCreate 실행.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_page);

        print("-------------------");
        print("XML 레이아웃 파일에 있는 View 요소들의 객체에 대한 참조 가져오기 시작");
        
        ImageButton 전체통계화면으로이동버튼 = findViewById(R.id.imageButton13);
        print("전체 통계화면으로 이동버튼",전체통계화면으로이동버튼.toString());
        Button 가계부화면으로이동버튼 = findViewById(R.id.button35);
        print("가계부화면으로이동버튼",가계부화면으로이동버튼.toString());
        Button 통계화면으로이동버튼 = findViewById(R.id.button36);
        print("통계화면으로이동버튼",통계화면으로이동버튼.toString());
        ImageButton 더보기버튼 = findViewById(R.id.imageButton12);
        print("더보기버튼",더보기버튼.toString());

        RecyclerView recyclerView = findViewById(R.id.recyclerView11);
        print("recyclerView",recyclerView.toString());

        자산텍스트뷰 = findViewById(R.id.textView115);
        print("자산",자산텍스트뷰.toString());

        부채텍스트뷰 = findViewById(R.id.textView116);
        print("부채",부채텍스트뷰.toString());

        합계텍스트뷰 = findViewById(R.id.textView117);
        print("합계",합계텍스트뷰.toString());

        print("---------------------");
        print("application 객체, assetDataManager 객체의 참조 가져오기");
        application = (MyApplication)getApplication();
        print("application",application.toString());

        assetDataManager = application.assetDataManager;
        print("assetDataManager",assetDataManager.toString());

        print("PopupMenu 객체 생성 및 초기화");
        //PopupMenu 객체 생성
        popup= new PopupMenu(getContext(), 더보기버튼); //두 번째 파라미터가 팝업메뉴가 붙을 뷰
        print("popup",popup.toString());
        print("popupMenu 객체 생성 (더보기 버튼 주변에 보여질)");

        //PopupMenu popup= new PopupMenu(MainActivity.this, btn2); //첫번째 버튼을 눌렀지만 팝업메뉴는 btn2에 붙어서 나타남
        print("getMenuInflater()",getMenuInflater().toString());
        print("popup.getMenu()",popup.getMenu().toString());

        // getMenuInflater : Munu리소스를 메모리 위에 생성. (필요한 객체 생성?)
        // R.menu.asset_item(메뉴 리소스 파일)에 정의된 항목을 메모리에 올려놓고,
        // 메모리에 있는 메뉴리소스와 관련된 객체들을 popup 객체에 추가.
        // popup 객체와 R.menu.asset_item 을 연결한다?
        getMenuInflater().inflate(R.menu.asset_item, popup.getMenu());
        print("R.menu.asset_item 에 정의된 메뉴항목들이 PopupMenu에 추가됨.");
        
        print("---------------------");


        전체통계화면으로이동버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("전체 통계 화면으로 이동 버튼 클릭.");
                startActivity(FullStatisticsActivity.class);
                print("startActivity(FullStatisticsActivity.class) 실행 끝.");
            }
        });

        print("전체통계화면으로이동버튼 클릭 이벤트 설정.");

        가계부화면으로이동버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("가계부화면으로이동버튼 클릭.");
                finish();
                print("AssetPageActivity 종료");

            }
        });
        print("가계부화면으로이동버튼 클릭 이벤트 설정.");

        통계화면으로이동버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print("통계화면으로이동버튼 클릭.");
                startActivity(PieChartActivity.class);
                finish();
                print("AssetPageActivity 종료");
            }
        });
        print("통계화면으로이동버튼 클릭 이벤트 설정.");


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                print("팝업메뉴 아이템 클릭");

                int id = menuItem.getItemId();
                print("메뉴 아이템의 식별자",Integer.toString(id));
                print("메뉴 아이템의 제목",menuItem.getTitle().toString());

                if (R.id.asset_add == id) {
                    print("자산추가 화면으로 이동");
                    startActivityForResult(AssetCreateActivity.class,RequestCode.REQUEST_CREATEASSET);
                    print("true 반환(이벤트 처리)");
                    //이 경우, 이후에 등록된 다른 리스너들이나 기본 동작은 실행되지 않습니다.
                    return true;

                } else if (R.id.asset_remove == id) {
                    print("자산 삭제 화면으로 이동");
//                    Intent intent = new Intent();
//                    intent.putExtra("", Arrays.);
//                    List<AssetDataManager.자산> 자산목록 = new ArrayList<>();
//                    for(int i=0;i<list.size();i++){
//
//                        AssetDataManager.자산별합계 자산별합계 = list.get(i);
//                        자산목록.add(자산별합계.자산);
////                        자산목록.add()
//
//                    }
//// 보내는 쪽에서의 코드
//                    Intent intent = new Intent(getContext(), AssetRemoveActivity.class);
//                    Bundle bundle = new Bundle();
////                    bundle.putSerializable("자산목록", (Serializable) 자산목록);
////                    intent.putExtras(bundle);
//                    bundle.putParcelableArrayList("자산목록", (ArrayList<? extends Parcelable>) 자산목록);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent,RequestCode.REQUEST_REMOVEASSET);
//

                    startActivityForResult(AssetRemoveActivity.class,RequestCode.REQUEST_REMOVEASSET);
                    print("true 반환(이벤트 처리)");
                    return true;
                }

                print("false 반환(이벤트 미처리)");
                // 이 경우, 이후에 등록된 다른 리스너들이나 기본 동작이 실행될 수 있습니다.
                return false;

            }
        });
        print("팝업메뉴 아이템 클릭 리스너 객체 생성 및 리스너 등록");

        더보기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                print("더보기버튼 클릭");
                popup.show();
                print("popup메뉴 화면에 보여주기");

            }
        });

        print("더보기 버튼 클릭 이벤트 설정.");
        print("---------------------");



        print("application.readDb",application.readDb.toString());
        print("application.readDb.isOpen() ",Boolean.toString(application.readDb.isOpen()));
//        assetDataManager.DB에서날짜데이터가져오기및년월목록값할당(application.readDb);
//        assetDataManager.DB에서데이터가져오기(application.readDb);
        assetDataManager.DB에서자산데이터가져오기(application.readDb);
        print("---------------------");

        list = assetDataManager.자산별합계반환(application.readDb);

        adapter = new AssetPageAdapter(list,AssetPageActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        자산데이터 자산데이터 = 자산데이터가져오기(list);

        자산텍스트뷰.setText(Formatter.천단위구분(자산데이터.자산));
        부채텍스트뷰.setText(Formatter.천단위구분(자산데이터.부채));
        합계텍스트뷰.setText(Formatter.천단위구분(자산데이터.합계));


    }

    private 자산데이터 자산데이터가져오기(List<AssetDataManager.자산별합계> list){


        자산데이터 자산데이터 = new 자산데이터();

        for(int i=0;i<list.size();i++){

            AssetDataManager.자산별합계 자산별합계 = list.get(i);

            if(자산별합계.type == accountBook.수입){
                자산데이터.자산 += 자산별합계.합계.amount;

            }
            else if(자산별합계.type == accountBook.지출){
                자산데이터.부채 -= 자산별합계.합계.amount;

            }
            else{

            }

        }

        자산데이터.합계 = 자산데이터.자산 - 자산데이터.부채;

        return 자산데이터;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        assetDataManager.데이터정리하기();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RequestCode.REQUEST_CREATEASSET) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {

                    int 금액 = data.getIntExtra("금액",-1);
                    int 자산식별자 = data.getIntExtra("pk",-1);
                    String 자산명 = data.getStringExtra("name");

                    AssetDataManager.자산별합계 자산별합계 = new AssetDataManager.자산별합계(금액, 자산식별자,자산명);
                    adapter.addData(자산별합계);

                    int 자산금액 = Formatter.금액쉼표제거(자산텍스트뷰.getText().toString());
                    int 부채금액 =  Formatter.금액쉼표제거(부채텍스트뷰.getText().toString());

                    자산금액 += 금액;
                    int 합계금액 = 자산금액 - 부채금액;

                    자산텍스트뷰.setText(Formatter.천단위구분(자산금액));
                    부채텍스트뷰.setText(Formatter.천단위구분(부채금액));
                    합계텍스트뷰.setText(Formatter.천단위구분(합계금액));

                }
            }
        }
        else if (requestCode == RequestCode.REQUEST_REMOVEASSET) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {

                    List<Integer> removeItemPosition = data.getIntegerArrayListExtra("삭제아이템");

                    removeItemPosition.sort(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o2 -o1;
                        }
                    });


                    for(int i=0;i<removeItemPosition.size();i++){

                        print(Integer.toString(i),Integer.toString(removeItemPosition.get(i)));

                    }

                    int 자산 =0, 부채 = 0;

                    for(int i=0;i<removeItemPosition.size();i++){
                        int position = removeItemPosition.get(i);
                        print(Integer.toString(position),Integer.toString(removeItemPosition.get(i)));
                        // adapter remove 진행.
                        // 하면서 삭제한 자산의
                        // 자산, 부채값 저장하고
                        int 금액 = adapter.getItem(position).합계.amount;
                        if(금액 > 0){
                            자산 += 금액;
                        }
                        else if(금액 < 0){
                            부채 += 금액;
                        }

                        print("금액",Integer.toString(금액));
                        adapter.removeData(position);

                    }
                    // 삭제 후 자산, 부채, 합계 값 다시 계산해서 표시\
                    int 자산금액 = Formatter.금액쉼표제거(자산텍스트뷰.getText().toString());
                    int 부채금액 =  Formatter.금액쉼표제거(부채텍스트뷰.getText().toString());

                    자산금액 -= 자산;
                    부채금액 -= 부채;

                    int 합계금액 = 자산금액 - 부채금액;

                    자산텍스트뷰.setText(Formatter.천단위구분(자산금액));
                    부채텍스트뷰.setText(Formatter.천단위구분(부채금액));
                    합계텍스트뷰.setText(Formatter.천단위구분(합계금액));


                }
            }
        }
        else if (requestCode == RequestCode.REQUEST_CHANGEORDER) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
                if (data != null) {




                }
            }
        }
        else if (requestCode == RequestCode.REQUEST_DETAIL_GRAPH) {
            if (resultCode == RESULT_OK) { // 결과가 OK인 경우
//                if (data != null) {

                    list.clear();
                    adapter.clear();

                    print("application.readDb",application.readDb.toString());
                    print("application.readDb.isOpen() ",Boolean.toString(application.readDb.isOpen()));
//        assetDataManager.DB에서날짜데이터가져오기및년월목록값할당(application.readDb);
//        assetDataManager.DB에서데이터가져오기(application.readDb);
                    assetDataManager.DB에서자산데이터가져오기(application.readDb);
                    print("---------------------");

                    list = assetDataManager.자산별합계반환(application.readDb);

                    adapter.setData(list);
                    자산데이터 자산데이터 = 자산데이터가져오기(list);

                    자산텍스트뷰.setText(Formatter.천단위구분(자산데이터.자산));
                    부채텍스트뷰.setText(Formatter.천단위구분(자산데이터.부채));
                    합계텍스트뷰.setText(Formatter.천단위구분(자산데이터.합계));

//                }
            }
        }


    }

    class 자산데이터{
        int 자산;
        int 부채;
        int 합계;
        자산데이터(){
            자산 = 0;
            부채 = 0;
            합계 = 0;
        }
    }

}

//
//    private void 화면에데이터표시(){
//
//
//
//        adapter.setData();
//
//    }
