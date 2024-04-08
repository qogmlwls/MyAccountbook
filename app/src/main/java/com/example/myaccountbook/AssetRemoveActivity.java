package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myaccountbook.adapter.AssetPageAdapter;
import com.example.myaccountbook.adapter.AssetRemoveAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssetRemoveActivity extends CommonActivity {


    AssetDataManager assetDataManager;
    AssetRemoveAdapter adapter;
    MyApplication application;
    List<자산삭제아이템> list;


    public class 자산삭제아이템{
        public int position;
        public AssetDataManager.자산 자산;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_remove);


        print("--------------------");
        print("뒤로가기 버튼 추가와");
        print("XML 레이아웃 파일에 있는 View 요소들의 객체에 대한 참조 가져오기 시작");

//        toolbar16
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar16);
//        toolbar.setBack
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        RecyclerView recyclerView = findViewById(R.id.recyclerView12);

        print("---------------------");

        print("application 객체, assetDataManager 객체의 참조 가져오기");
        application = (MyApplication)getApplication();
        print("application",application.toString());

        assetDataManager = application.assetDataManager;
        print("assetDataManager",assetDataManager.toString());

        list = new ArrayList<>();

        print("--------------------");

        assetDataManager.DB에서자산데이터가져오기(application.readDb);
        List<AssetDataManager.자산별합계> items = assetDataManager.자산별합계반환(application.readDb);

        for(int i=0;i<items.size();i++){


            자산삭제아이템 아이템 = new 자산삭제아이템();
            아이템.position = i;
            아이템.자산 = items.get(i).자산;

            list.add(아이템);

        }
        items.clear();
        items = null;

        adapter = new AssetRemoveAdapter(list,AssetRemoveActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        print("---------------------");



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
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
