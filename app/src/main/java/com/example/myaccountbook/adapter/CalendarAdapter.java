package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.EditRecordActivity;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.R;
import com.example.myaccountbook.accountBook;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {


    List<accountBook> list;
    Context context;

    SimpleDateFormat 시분 = new SimpleDateFormat("a hh:mm", Locale.KOREA);


    public CalendarAdapter(List<accountBook> list, Context context){
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new CalendarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {


        accountBook 가계부 = list.get(position);

        switch (가계부.type){

            case accountBook.수입:
                holder.카테고리.setText(가계부.카테고리);
                holder.가격.setText(가계부.금액원 + "원");
                holder.가격.setTextColor(Color.BLUE);
                holder.내용.setText(가계부.내용);
                holder.시간.setText(시분.format(가계부.date.getTime()));
                holder.자산명.setText(가계부.자산);
                break;

            case accountBook.고정지출:
            case accountBook.변동지출:
                holder.카테고리.setText(가계부.카테고리);
                holder.가격.setText(가계부.금액원 + "원");
                holder.가격.setTextColor(Color.RED);
                holder.내용.setText(가계부.내용);
                holder.시간.setText(시분.format(가계부.date.getTime()));
                holder.자산명.setText(가계부.자산);
                break;

            case accountBook.이체:
                holder.카테고리.setText("이체");
                holder.가격.setText(가계부.금액원 + "원");
                holder.가격.setTextColor(context.getResources().getColor(R.color.black));
                holder.시간.setText(시분.format(가계부.date.getTime()));
                holder.내용.setText(가계부.출금자산 + "->" + 가계부.입금자산);
                holder.자산명.setText("");
                break;

        }
        



        //        String routinesName = list.get(position);
//        holder.반복종류.setText(routinesName);

    }

    public void setList(List<accountBook> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView 카테고리, 내용, 이체내용, 시간, 자산명, 가격;

        public ViewHolder(View view) {
            super(view);

            카테고리 = view.findViewById(R.id.textView20);
            내용 = view.findViewById(R.id.textView21);
            이체내용 = view.findViewById(R.id.textView22);

            시간 = view.findViewById(R.id.textView23);
            자산명 = view.findViewById(R.id.textView24);
            가격 = view.findViewById(R.id.textView25);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    accountBook 가계부 = list.get(position);
//                    Toast.makeText(context, "상세내역", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, EditRecordActivity.class);
                    intent.putExtra("가계부",가계부);

                    Log.i("",가계부.날짜);
                    Activity activity = (Activity)context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);
//                    context.startActivity(intent);
//                    context.start
                }
            });
        }


    }
    final int REQUEST_EDIT = 101;

}