package com.example.myaccountbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.R;
import com.example.myaccountbook.accountBook;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ImagePageAdapter  extends RecyclerView.Adapter<ImagePageAdapter.ViewHolder> {


    List<String> list;
    Context context;



    public ImagePageAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public ImagePageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagepage_item, parent, false);
        return new ImagePageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePageAdapter.ViewHolder holder, int position) {

        holder.레이아웃.removeAllViews();

        String data = list.get(position);
        Uri uri = Uri.parse(data);

        holder.이미지.setImageURI(uri);

        for(int i=0;i< list.size();i++){
            ImageView 이미지 = new ImageView(context);

            if(i==position){
                // 리소스에서 이미지 가져오기
                int resId = R.drawable.circle_red; // my_image는 이미지 리소스의 이름입니다.
                이미지.setImageResource(resId);

            }
            else{
                // 리소스에서 이미지 가져오기
                int resId = R.drawable.circle_gray; // my_image는 이미지 리소스의 이름입니다.
                이미지.setImageResource(resId);

            }
            holder.레이아웃.addView(이미지);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout 레이아웃;

        ImageView 이미지;

        public ViewHolder(View view) {
            super(view);


            레이아웃 = view.findViewById(R.id.linearLayout15);
            이미지 = view.findViewById(R.id.imageView);


        }


    }

}
