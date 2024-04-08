package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.DetailPieChartActivity;
import com.example.myaccountbook.PeriodManager;
import com.example.myaccountbook.R;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.PieChartDataProvider;

import java.util.List;

public class PercentAdapter extends RecyclerView.Adapter<PercentAdapter.ViewHolder> {

    Context context;
    List<PieChartDataProvider.Data> list;
    int 총합계;

    GestureDetector gestureDetector;
    int REQUEST_DETAIL_PIECHART;

    public PercentAdapter(Context context, List<PieChartDataProvider.Data> list, int 총합계, final int requestCode , GestureDetector gestureDetector) {

        this.context = context;
        this.list = list;
        this.총합계 = 총합계;
        this.gestureDetector = gestureDetector;

        REQUEST_DETAIL_PIECHART = requestCode;

    }

    @NonNull
    @Override
    public PercentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.percent_item, parent, false);
        return new PercentAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PercentAdapter.ViewHolder holder, int position) {

        PieChartDataProvider.Data data = list.get(position);

        holder.카테고리명.setText(data.카테고리명);
        holder.금액.setText(data.금액.getAmount());
        holder.퍼센트.setText(퍼센트계산(data.금액.amount,총합계));

        int index = position%PieChartDataProvider.PieColor.length;
        int color = PieChartDataProvider.PieColor[index];
        holder.퍼센트.setBackgroundColor(color);

    }

    public String 퍼센트계산(int 일부값, int 전체값) {


        if(일부값 == 0){
            return "0%";
        }
        return Integer.toString((int) ((float)일부값 /(float)전체값 *100))+"%";

    }
    
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView 퍼센트, 카테고리명, 금액;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            퍼센트 = itemView.findViewById(R.id.textView102);
            카테고리명 = itemView.findViewById(R.id.textView103);
            금액 = itemView.findViewById(R.id.textView104);


            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    PieChartDataProvider.Data data = list.get(position);


                    Intent intent = new Intent(context, DetailPieChartActivity.class);

                    intent.putExtra("주기",주기);
                    intent.putExtra("type",type);
                    intent.putExtra("카테고리명",data.카테고리명);
                    intent.putExtra("카테고리식별자",data.카테고리식별자);

                    int index = position%PieChartDataProvider.PieColor.length;
                    intent.putExtra("선색상",PieChartDataProvider.PieColor[index]);


                    if(주기 == PeriodManager.기간){
                        intent.putExtra("끝날짜",끝날짜);
                        intent.putExtra("시작날짜",최초시작날짜);
                        intent.putExtra("최초시작날짜",끝날짜);
                    }
                    else{
                        intent.putExtra("최초시작날짜",최초시작날짜);
                    }

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_DETAIL_PIECHART);


                }
            });

        }


    }
    int 주기, type;
    Date 최초시작날짜,끝날짜;

    public void setData(int 주기, int type, Date 최초시작날짜){

        this.주기 = 주기;
        this.type = type;
        this.최초시작날짜 = 최초시작날짜;

    }

    public void setData(int 주기, int type, Date 시작날짜, Date 끝날짜){

        this.주기 = 주기;
        this.type = type;
        this.최초시작날짜 = 시작날짜;
        this.끝날짜 = 끝날짜;

    }

    public void set총합계(int 총합계) {
        this.총합계 = 총합계;
    }


    public void setList(List<PieChartDataProvider.Data> list) {

        this.list = list;
        notifyDataSetChanged();

    }


}
