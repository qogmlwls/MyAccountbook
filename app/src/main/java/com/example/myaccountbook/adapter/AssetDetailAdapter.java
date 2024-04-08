package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.AssetDataManager;
import com.example.myaccountbook.EditRecordActivity;
import com.example.myaccountbook.GestureManager;
import com.example.myaccountbook.R;
import com.example.myaccountbook.accountBook;
import com.example.myaccountbook.data.RequestCode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AssetDetailAdapter extends RecyclerView.Adapter<AssetDetailAdapter.ViewHolder> {

    Context context;
    List<accountBook> list;
    SimpleDateFormat 시분 = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    String 자산명;
    int 자산식별자;

    GestureManager manager;
    boolean getmanager;

    public AssetDetailAdapter(Context context){
//        this.list = list;
        this.context = context;
//        자산명 = "";
//        자산식별자 = -1;
        getmanager = false;
    }

    public AssetDetailAdapter(List<accountBook> list, Context context,String 자산명,int 자산식별자,GestureManager manager){
        this.list = list;
        this.context = context;
        this.자산명 = 자산명;
        this.자산식별자 = 자산식별자;
        this.manager = manager;
        getmanager = true;
    }

    public void setData(List<accountBook> list,String 자산명){

        if(this.list != null)
            this.list.clear();
        this.list = list;
        this.자산명 = 자산명;
        notifyDataSetChanged();

    }

    public void set자산명(String 자산명){

        this.자산명 = 자산명;
        notifyDataSetChanged();
    }
    public void clear(){

        this.list.clear();

    }

    @NonNull
    @Override
    public AssetDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_detail_item, parent, false);
        return new AssetDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetDetailAdapter.ViewHolder holder, int position) {

        accountBook 가계부 = list.get(position);

//        일 = itemView.findViewById(R.id.textView147);
//        요일 = itemView.findViewById(R.id.textView148);
//        년월 = itemView.findViewById(R.id.textView149);
//        총수입 = itemView.findViewById(R.id.textView150);
//        총지출 = itemView.findViewById(R.id.textView151)

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(가계부.date);
        SimpleDateFormat 년월 = new SimpleDateFormat("yyyy.MM", Locale.KOREA);
        SimpleDateFormat 일 = new SimpleDateFormat("dd", Locale.KOREA);

        holder.년월.setText(년월.format(가계부.date.getTime()));
        holder.일.setText(일.format(가계부.date.getTime()));

        SimpleDateFormat 년월일 = new SimpleDateFormat("E", Locale.KOREA);
        holder.요일.setText(년월일.format(가계부.date.getTime())+"요일");

        if(position != 0 ){
            int m_position = position-1;
            accountBook 가계부1 = list.get(m_position);
            if(일.format(가계부.date.getTime()).equals(일.format(가계부1.date.getTime()))){
                holder.위화면.setVisibility(View.GONE);
            }
            else{
                holder.위화면.setVisibility(View.VISIBLE);
            }
        }
        else{
            holder.위화면.setVisibility(View.VISIBLE);
        }

        switch (가계부.type){

            case accountBook.수입:
                holder.카테고리명.setText(가계부.카테고리);
                holder.금액.setText(가계부.금액원 + "원");
                holder.금액.setTextColor(Color.BLUE);
                holder.내용.setText(가계부.내용);
                holder.시간.setText(시분.format(가계부.date.getTime()));
                if(getmanager){
                    holder.자산명.setText(자산명);
                }
                else{
                    holder.자산명.setText(가계부.자산);
                }
                break;

            case accountBook.고정지출:
            case accountBook.변동지출:
                holder.카테고리명.setText(가계부.카테고리);
                holder.금액.setText(가계부.금액원 + "원");
                holder.금액.setTextColor(Color.RED);
                holder.내용.setText(가계부.내용);
                holder.시간.setText(시분.format(가계부.date.getTime()));
                if(getmanager){
                    holder.자산명.setText(자산명);
                }
                else{
                    holder.자산명.setText(가계부.자산);
                }
                break;

            case accountBook.이체:
                holder.카테고리명.setText("이체");
                holder.금액.setText(가계부.금액원 + "원");
//                if(가계부.입금자산식별자)
//                holder.금액.setTextColor(context.getResources().getColor(R.color.black));
                holder.시간.setText(시분.format(가계부.date.getTime()));
                if(getmanager) {
                    if (가계부.입금자산식별자 == 자산식별자) {

                        holder.내용.setText(가계부.출금자산 + "->" + 자산명);
                        holder.금액.setTextColor(Color.BLUE);

                    } else if (가계부.출금자산식별자 == 자산식별자) {

                        holder.내용.setText(자산명 + "->" + 가계부.입금자산);
                        holder.금액.setTextColor(Color.RED);

                    }
                }
                else{
                    if (가계부.입금자산식별자 == 자산식별자) {

                        holder.내용.setText(가계부.출금자산 + "->" + 가계부.입금자산);

                    } else if (가계부.출금자산식별자 == 자산식별자) {

                        holder.내용.setText(가계부.출금자산 + "->" + 가계부.입금자산);

                    }
                    holder.금액.setTextColor(Color.BLACK);

                }

//                holder.내용.setText(가계부.출금자산 + "->" + 가계부.입금자산);
                holder.자산명.setText("");
                break;

        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout 위화면;
        TextView 일,요일,년월,총수입, 총지출,카테고리명,내용,시간,자산명,금액;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            위화면 = itemView.findViewById(R.id.constraintLayout2);


            일 = itemView.findViewById(R.id.textView147);
            요일 = itemView.findViewById(R.id.textView148);
            년월 = itemView.findViewById(R.id.textView149);
            총수입 = itemView.findViewById(R.id.textView150);
            총지출 = itemView.findViewById(R.id.textView151);
            카테고리명 = itemView.findViewById(R.id.textView152);
            내용 = itemView.findViewById(R.id.textView153);
            시간 = itemView.findViewById(R.id.textView154);
            자산명 = itemView.findViewById(R.id.textView155);
            금액 = itemView.findViewById(R.id.textView156);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    accountBook data = list.get(position);

                    Intent intent = new Intent(context, EditRecordActivity.class);
                    intent.putExtra("가계부",data);

                    Activity activity = (Activity)context;
                    activity.startActivityForResult(intent, RequestCode.REQUEST_ASSET_RECORD);
                }
            });
            if(getmanager){
                itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        manager.getDetector().onTouchEvent(event);
                        return false;
                    }
                });
            }


        }


    }
}
