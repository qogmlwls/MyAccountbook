package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.EditBudgetActivity;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.R;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;

import java.util.List;

public class BudgetListAdapter  extends RecyclerView.Adapter<BudgetListAdapter.ViewHolder> {

    Context context;
    List<Budget> list;

    MyApplication application;


    final int REQUEST_EDIT = 101;

    public BudgetListAdapter(Context context, List<Budget> list){

        this.context = context;

        this.list = list;

        application = (MyApplication)context.getApplicationContext();

    }

    public void setList(List<Budget> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public BudgetListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetlist_item, parent, false);
        return new BudgetListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetListAdapter.ViewHolder holder, int position) {

        Budget budget = list.get(position);

        String 카테고리명 = application.카테고리데이터명가져오기(budget.카테고리식별자);
        if(budget.카테고리식별자 == 0)
            카테고리명 = "전체 예산";

        holder.카테고리명.setText(카테고리명);
//        holder.예산.setText(budget.금액표시(budget.일반예산));
        holder.예산.setText(budget.금액표시(budget.예산()));



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView 카테고리명, 예산;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            카테고리명 = itemView.findViewById(R.id.textView54);
            예산 = itemView.findViewById(R.id.textView55);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Budget date = list.get(position);

                    Intent intent = new Intent(context, EditBudgetActivity.class);
                    intent.putExtra("category_pk",date.카테고리식별자);
                    intent.putExtra("year",date.date.getYear());
                    intent.putExtra("month",date.date.getMonth());

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);
                }
            });

        }

    }

}
