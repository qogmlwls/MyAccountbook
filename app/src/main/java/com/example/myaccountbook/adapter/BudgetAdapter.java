package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.BudgetActivity;
import com.example.myaccountbook.BudgetManager;
import com.example.myaccountbook.Calendar;
import com.example.myaccountbook.Data;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.R;
import com.example.myaccountbook.SetBudgetActivity;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Settlement;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {

    DecimalFormat myFormatter = new DecimalFormat("###,###");

    String 금액표시(long 금액){
        return myFormatter.format(금액);
    }


    List<Date> list;

    Context context;

    Calendar 달력;

    MyApplication application;

    BudgetManager budgetManager;
    public BudgetAdapter(Context context, List<Date> list){

        this.list = list;
        this.context = context;
        application = (MyApplication) context.getApplicationContext();
        달력 = new Calendar(application);

        budgetManager = new BudgetManager();
    }

    @NonNull
    @Override
    public BudgetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_item, parent, false);
        return new BudgetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetAdapter.ViewHolder holder, int position) {


        Date date = list.get(position);
        달력.setMonth(application.readDb,date);

        holder.날짜버튼.setText(date.년월날짜());

        long 월총수입 = 달력.월총수입가져오기();
        long 월총지출 = 달력.월총지출가져오기();

        holder.총수입.setText(금액표시(월총수입));
        holder.총지출.setText(금액표시(월총지출));
        holder.합계.setText(금액표시(월총수입-월총지출));


        List<Settlement> data = budgetManager.getSettlementList(date,application);

        if(data.size() == 0){
            holder.안내문.setVisibility(View.VISIBLE);
        }
        else{
            holder.안내문.setVisibility(View.GONE);
        }


        BudgetInnerAdapter adapter = new BudgetInnerAdapter(context,data);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(adapter);



    }

    public void noti(){
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void 이전데이터추가(){




    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button 날짜버튼, 예산설정버튼;

        TextView 총수입, 총지출, 합계, 안내문;

        RecyclerView recyclerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            날짜버튼 = itemView.findViewById(R.id.button);
            예산설정버튼 = itemView.findViewById(R.id.button2);


            총수입 = itemView.findViewById(R.id.textView4);
            총지출 = itemView.findViewById(R.id.textView5);
            합계 = itemView.findViewById(R.id.textView6);

            안내문 = itemView.findViewById(R.id.textView50);


            recyclerView = itemView.findViewById(R.id.recyclerView);

            예산설정버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);


                    Intent intent = new Intent(context, SetBudgetActivity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",date.getMonth());
                    Activity activity = (Activity)context;
                    activity.startActivityForResult(intent,100);
//                    context.startActivity(intent);

                }
            });


        }

    }

}
