package com.example.myaccountbook.adapter;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.Data;
import com.example.myaccountbook.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SelectRoutineAdapter extends RecyclerView.Adapter<SelectRoutineAdapter.ViewHolder> {


    List<Data> list;
    Context context;

    public SelectRoutineAdapter(List<Data> list, Context context){
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public SelectRoutineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_routine_item, parent, false);
        return new SelectRoutineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectRoutineAdapter.ViewHolder holder, int position) {

        Data data = list.get(position);
        String routinesName = data.name;
        holder.반복종류.setText(routinesName);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView 반복종류;

        public ViewHolder(View view) {
            super(view);

            반복종류 = view.findViewById(R.id.textView11);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Data data = list.get(position);

                    String name = data.name;
                    int pk = data.pk;

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("pk",pk);

                    Activity activity = (Activity)context;
                    // 결과 설정 및 현재 액티비티 종료
                    activity.setResult(RESULT_OK, resultIntent);
                    activity.finish();
                }
            });

        }


    }

}