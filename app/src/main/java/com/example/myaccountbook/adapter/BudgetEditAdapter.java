package com.example.myaccountbook.adapter;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.BudgetManager;
import com.example.myaccountbook.Calendar;
import com.example.myaccountbook.EditBudget2Activity;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.R;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Settlement;
import com.example.myaccountbook.data.defaultBudget;

import java.util.Comparator;
import java.util.List;

public class BudgetEditAdapter extends RecyclerView.Adapter<BudgetEditAdapter.ViewHolder> {


    List<Date> list;

    Context context;

    MyApplication application;


    BudgetManager budgetManager;
    int category_pk;

    Date 날짜기준;

    public BudgetEditAdapter(Context context, List<Date> list,int category_pk,Date date){

        this.list = list;
        this.context = context;
        application = (MyApplication) context.getApplicationContext();
//        달력 = new Calendar(application);

        this.category_pk = category_pk;

        budgetManager = new BudgetManager();

        날짜기준 = date;

    }

    @NonNull
    @Override
    public BudgetEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_budget_item, parent, false);
        return new BudgetEditAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetEditAdapter.ViewHolder holder, int position) {

        Date date = list.get(position);

        List<Settlement> settlements = budgetManager.getSettlementList(date,application,category_pk);
        settlements.sort(new Comparator<Settlement>() {
            @Override
            public int compare(Settlement o1, Settlement o2) {
                return o2.date.getMonth() - o1.date.getMonth();
            }
        });

//        application.

        defaultBudget d = budgetManager.get기본예산(application,category_pk);
        holder.기본예산.setText(d.금액표시(d.예산));

        holder.예산12.setText(settlements.get(0).예산.금액표시(settlements.get(0).예산.예산));
        holder.예산11.setText(settlements.get(1).예산.금액표시(settlements.get(1).예산.예산));
        holder.예산10.setText(settlements.get(2).예산.금액표시(settlements.get(2).예산.예산));
        holder.예산9.setText(settlements.get(3).예산.금액표시(settlements.get(3).예산.예산));
        holder.예산8.setText(settlements.get(4).예산.금액표시(settlements.get(4).예산.예산));
        holder.예산7.setText(settlements.get(5).예산.금액표시(settlements.get(5).예산.예산));
        holder.예산6.setText(settlements.get(6).예산.금액표시(settlements.get(6).예산.예산));
        holder.예산5.setText(settlements.get(7).예산.금액표시(settlements.get(7).예산.예산));
        holder.예산4.setText(settlements.get(8).예산.금액표시(settlements.get(8).예산.예산));
        holder.예산3.setText(settlements.get(9).예산.금액표시(settlements.get(9).예산.예산));
        holder.예산2.setText(settlements.get(10).예산.금액표시(settlements.get(10).예산.예산));
        holder.예산1.setText(settlements.get(11).예산.금액표시(settlements.get(11).예산.예산));

        holder.년.setText(Integer.toString(date.getYear())+"년");
        holder.toolbar.setTitle(application.카테고리데이터명가져오기(category_pk));
        if(category_pk == Budget.전체식별자){
            holder.toolbar.setTitle(Budget.전체);
        }

        AppCompatActivity activity = (AppCompatActivity) context;
        activity.setSupportActionBar(holder.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디


        if(date.getYear() == 날짜기준.getYear()){

            Drawable drawable = context.getDrawable(R.drawable.red_line);
            int color = context.getColor(R.color.orange);

            switch (날짜기준.getMonth()){
                case 1:
                    holder.월1.setBackground(drawable);
                    holder.월1.setTextColor(color);
                    break;
                case 2:
                    holder.월2.setBackground(drawable);
                    holder.월2.setTextColor(color);
                    break;
                case 3:
                    holder.월3.setBackground(drawable);
                    holder.월3.setTextColor(color);
                    break;
                case 4:
                    holder.월4.setBackground(drawable);
                    holder.월4.setTextColor(color);
                    break;
                case 5:
                    holder.월5.setBackground(drawable);
                    holder.월5.setTextColor(color);
                    break;
                case 6:
                    holder.월6.setBackground(drawable);
                    holder.월6.setTextColor(color);
                    break;
                case 7:
                    holder.월7.setBackground(drawable);
                    holder.월7.setTextColor(color);
                    break;
                case 8:
                    holder.월8.setBackground(drawable);
                    holder.월8.setTextColor(color);
                    break;
                case 9:
                    holder.월9.setBackground(drawable);
                    holder.월9.setTextColor(color);
                    break;
                case 10:
                    holder.월10.setBackground(drawable);
                    holder.월10.setTextColor(color);
                    break;
                case 11:
                    holder.월11.setBackground(drawable);
                    holder.월11.setTextColor(color);
                    break;
                case 12:
                    holder.월12.setBackground(drawable);
                    holder.월12.setTextColor(color);
                    break;
            }

        }


//        settlement.
//        settlements.
//        TextView textView;
//        textView.setBackground();



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void noti(){
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView 기본예산,예산12,예산11,예산10,예산9,예산8,예산7,예산6,예산5,예산4,예산3,예산2,예산1;

        TextView 월12,월11,월10,월9,월8,월7,월6,월5,월4,월3,월2,월1;

        TableRow t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11,t12, 기본;

        TextView 년;

        Toolbar toolbar;

        ImageButton 삭제;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            삭제 = itemView.findViewById(R.id.imageButton11);
            삭제.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    application.예산삭제(category_pk);

                    Intent resultIntent = new Intent();
                    Activity activity = (Activity)context;
                    // 결과 설정 및 현재 액티비티 종료
                    activity.setResult(RESULT_OK, resultIntent);
                    activity.finish();

                }
            });


            toolbar = itemView.findViewById(R.id.toolbar10);
            년 = itemView.findViewById(R.id.textView99);

            기본예산 = itemView.findViewById(R.id.textView74);

            월12 = itemView.findViewById(R.id.textView75);
            월11 = itemView.findViewById(R.id.textView76);
            월10 = itemView.findViewById(R.id.textView77);
            월9 = itemView.findViewById(R.id.textView78);
            월8 = itemView.findViewById(R.id.textView79);
            월7 = itemView.findViewById(R.id.textView80);
            월6 = itemView.findViewById(R.id.textView81);
            월5 = itemView.findViewById(R.id.textView82);
            월4 = itemView.findViewById(R.id.textView83);
            월3 = itemView.findViewById(R.id.textView84);
            월2 = itemView.findViewById(R.id.textView85);
            월1 = itemView.findViewById(R.id.textView86);


            예산12 = itemView.findViewById(R.id.textView87);
            예산11 = itemView.findViewById(R.id.textView88);
            예산10 = itemView.findViewById(R.id.textView89);

            예산9 = itemView.findViewById(R.id.textView90);
            예산8 = itemView.findViewById(R.id.textView91);
            예산7 = itemView.findViewById(R.id.textView92);
            예산6 = itemView.findViewById(R.id.textView93);
            예산5 = itemView.findViewById(R.id.textView94);
            예산4 = itemView.findViewById(R.id.textView95);
            예산3 = itemView.findViewById(R.id.textView96);
            예산2 = itemView.findViewById(R.id.textView97);
            예산1 = itemView.findViewById(R.id.textView98);


            기본 = itemView.findViewById(R.id.tablerow13);
            t12 = itemView.findViewById(R.id.tablerow12);
            t11 = itemView.findViewById(R.id.tablerow11);
            t10 = itemView.findViewById(R.id.tablerow10);
            t9 = itemView.findViewById(R.id.tablerow9);
            t8 = itemView.findViewById(R.id.tablerow8);

            t7 = itemView.findViewById(R.id.tablerow7);
            t6 = itemView.findViewById(R.id.tablerow6);
            t5 = itemView.findViewById(R.id.tablerow5);
            t4 = itemView.findViewById(R.id.tablerow4);
            t3 = itemView.findViewById(R.id.tablerow3);
            t2 = itemView.findViewById(R.id.tablerow2);
            t1 = itemView.findViewById(R.id.tablerow1);

            기본.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",0);

                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",기본예산.getText().toString().substring(0,기본예산.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });
            t12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",12);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산12.getText().toString().substring(0,예산12.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });
            t11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",11);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산11.getText().toString().substring(0,예산11.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });
            t10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",10);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산10.getText().toString().substring(0,예산10.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",9);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산9.getText().toString().substring(0,예산9.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",8);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산8.getText().toString().substring(0,예산8.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",7);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산7.getText().toString().lastIndexOf(-1));
                    intent.putExtra("amount",예산7.getText().toString().substring(0,예산7.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",6);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산6.getText().toString().substring(0,예산6.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",5);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산5.getText().toString().substring(0,예산5.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",4);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산4.getText().toString().substring(0,예산4.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",3);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산3.getText().toString().substring(0,예산3.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",2);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산2.getText().toString().substring(0,예산2.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

            t1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    Date date = list.get(position);

                    Intent intent = new Intent(context, EditBudget2Activity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",1);
                    intent.putExtra("category_pk",category_pk);
                    intent.putExtra("amount",예산1.getText().toString().substring(0,예산1.getText().length()-1));

                    Activity activity = (Activity) context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);

                }
            });

        }
    }




    final int REQUEST_EDIT = 103;

}
