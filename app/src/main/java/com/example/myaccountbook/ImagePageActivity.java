package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myaccountbook.adapter.ImagePageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_page);

        RecyclerView 리사이클러뷰;
        리사이클러뷰 = findViewById(R.id.recyclerView3);

        Intent intent = getIntent();
//        String str = "file:///storage/emulated/0/Android/data/com.example.myaccountbook/files/Pictures/JPEG_20240103_175935_832471153044161471.jpg";
        List<String> list = new ArrayList<>();
        int size = intent.getIntExtra("count",-1);
        for(int i=0;i<size;i++){

            String str = intent.getStringExtra("이미지경로"+Integer.toString(i));
            list.add(str);


        }

        int 이미지수 = intent.getIntExtra("position",-1);

//        list.add(str);
//        list.add(str);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(리사이클러뷰);
        리사이클러뷰.setLayoutManager(new LinearLayoutManager(ImagePageActivity.this,LinearLayoutManager.HORIZONTAL, false));
        리사이클러뷰.setAdapter(new ImagePageAdapter(list,this));

        if(이미지수 != -1){
//            Toast.makeText(this, "not -1", Toast.LENGTH_SHORT).show();
//            리사이클러뷰.scrollToPosition(이미지수-1);
            LinearLayoutManager layoutManager = (LinearLayoutManager) 리사이클러뷰.getLayoutManager();

//            int secondItemPosition = 1; // 두 번째 아이템의 위치(index), 여기서는 0부터 시작하는 인덱스라면 1로 지정

            layoutManager.scrollToPosition(이미지수);
        }

//        ImageView 이미지 = findViewById(R.id.imageView);
//
//// Tint 색 지정
//        ColorStateList colorTintList = ContextCompat.getColorStateList(this, R.color.your_tint_color); // your_tint_color는 원하는 색상으로 대체해야 합니다.
//        imageView.setImageTintList(colorTintList);


        ImageButton 닫기;
        닫기 = findViewById(R.id.imageButton9);


        닫기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}