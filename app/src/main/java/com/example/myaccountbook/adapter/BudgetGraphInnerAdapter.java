package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.EditRecordActivity;
import com.example.myaccountbook.R;
import com.example.myaccountbook.accountBook;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BudgetGraphInnerAdapter extends RecyclerView.Adapter<BudgetGraphInnerAdapter.ViewHolder> {

    List<DATA> list;

    Context context;

    GestureDetector gestureDetector;

    public BudgetGraphInnerAdapter(Context context, List<accountBook> list, int year, int month){

        this.context = context;
//        this.list = list;

        // 리스트 가공하기

        this.list = new ArrayList<>();

        List<Integer> integerList = 일목록반환(list);
        for(int i=0;i<integerList.size();i++){

            int value = integerList.get(i);
            List<accountBook> 일가계부 = 가계부내역가져오기(list,value);


//            Date date = 가계부.parseISODate(가계부.날짜);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
            com.example.myaccountbook.data.Date date = new com.example.myaccountbook.data.Date();

            date.setYear(year);
            date.setMonth(month);
            date.setDay(value);

            DATA data = new DATA();
            data.가계부내역 = 일가계부;
            data.date = date;

            this.list.add(data);

        }

    }

    public BudgetGraphInnerAdapter(Context context, List<accountBook> list, GestureDetector gestureDetector){

        this.context = context;
//        this.list = list;

        // 리스트 가공하기

        this.list = new ArrayList<>();

        this.gestureDetector = gestureDetector;


        List<Date> 날짜목록 = 날짜목록반환(list);
        Log.i("BudgetGraphInnerAdapter","날짜목록 크기 : "+Integer.toString(날짜목록.size()));

        for(int i=0;i<날짜목록.size();i++){

            Date date1 = 날짜목록.get(i);

            accountBook accountBook = new accountBook();
            Log.i("BudgetGraphInnerAdapter","날짜 : "+accountBook.날짜(date1));

            List<accountBook> 일가계부 = 가계부내역가져오기(list,date1);
            Log.i("BudgetGraphInnerAdapter","일가계부 크기 : "+Integer.toString(일가계부.size()));



//            Date date = 가계부.parseISODate(가계부.날짜);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            com.example.myaccountbook.data.Date date = com.example.myaccountbook.data.Date.getDate(calendar);


            DATA data = new DATA();
            data.가계부내역 = 일가계부;
            data.date = date;

            this.list.add(data);

        }

    }

    List<Integer> 일목록반환(List<accountBook> 가계부목록){

        List<Integer> list1 = new ArrayList<>();

        for(int i=0;i<가계부목록.size();i++){
            accountBook 가계부 = 가계부목록.get(i);

            Date date = 가계부.parseISODate(가계부.날짜);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            if(!list1.contains(calendar.get(Calendar.DAY_OF_MONTH))){
                list1.add(calendar.get(Calendar.DAY_OF_MONTH));
            }

        }

        return list1;

    }

    List<Date> 날짜목록반환(List<accountBook> 가계부목록){

        List<Date> list1 = new ArrayList<>();
        // long으로 중복검사 하기.
        List<Long> list2 = new ArrayList<>();

        for(int i=0;i<가계부목록.size();i++){

            accountBook 가계부 = 가계부목록.get(i);

            Log.i("날짜목록반환",가계부.날짜);

            Date date = 가계부.parseISODate(가계부.날짜);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.set(year,month,day,0,0,0);
            Date date1 = calendar.getTime();
            Log.i("날짜목록반환",가계부.날짜(date1));


            long 날짜 = date1.getTime()/1000;

            if(!list2.contains(날짜)){


                list2.add(날짜);
            }


        }

        for(int i=0;i<list2.size();i++){

            long 날짜 = list2.get(i);
            Date date = new Date(날짜*1000);

            accountBook accountBook = new accountBook();
            Log.i("날짜목록반환","날짜 list2 : "+accountBook.날짜(date));


            list1.add(date);

        }

        return list1;

    }
    class DATA{
        public com.example.myaccountbook.data.Date date;

        public List<accountBook> 가계부내역;
    }
    public List<accountBook> 가계부내역가져오기(List<accountBook> 내역기록, Date date){


        List<accountBook> 기록 = new ArrayList<>();

        accountBook accountBook = new accountBook();
        Log.i("가계부내역가져오기","Date date : "+accountBook.날짜(date));

        for(int i=0;i<내역기록.size();i++){

            accountBook 내역 = 내역기록.get(i);

            String 날짜 = 내역.날짜(date);
            Log.i("가계부내역가져오기","날짜 : "+날짜);

            String 비교날짜 = 날짜.substring(0,10);
            String 비교날짜2 = 내역.날짜.substring(0,10);

            if(비교날짜.equals(비교날짜2)){
                Log.i("가계부내역가져오기","기록.add(내역); : "+내역.날짜);

                기록.add(내역);
            }
        }


        기록.sort(new Comparator<accountBook>() {
            @Override
            public int compare(accountBook o1, accountBook o2) {

                long time1 = o1.date.getTime();
                long time2 = o2.date.getTime();
                long time = time1-time2;

                return (int)time;

            }
        });

        return 기록;

    }
    public List<accountBook> 가계부내역가져오기(List<accountBook> 내역기록, int day){


        List<accountBook> 기록 = new ArrayList<>();
        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);

            Date 날짜 = 내역.parseISODate(내역.날짜);
            java.util.Calendar calendar = java.util.Calendar.getInstance();

            calendar.setTime(날짜);
            int 일 = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            if(day == 일)
                기록.add(내역);


        }

        기록.sort(new Comparator<accountBook>() {
            @Override
            public int compare(accountBook o1, accountBook o2) {

                long time1 = o1.date.getTime();
                long time2 = o2.date.getTime();
                long time = time1-time2;

                return (int)time;

            }
        });

        return 기록;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_graph_inner_item, parent, false);
        return new BudgetGraphInnerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        DATA data = list.get(position);

        com.example.myaccountbook.data.Date date = data.date;

        int 클릭일 = date.getDay();

        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(),date.getMonth()-1,date.getDay(),0,0);

        holder.년월.setText(String.format("%04d", date.getYear())+ "."+String.format("%02d", date.getMonth()+1));
        holder.일.setText(String.format("%02d", 클릭일));

        SimpleDateFormat 년월일 = new SimpleDateFormat("E", Locale.KOREA);
        holder.요일.setText(년월일.format(calendar.getTime())+"요일");
//        adapter.setList(list);


        if(data.가계부내역.get(0).type == accountBook.수입){
            long 클릭일총수입 = 수입가져오기(data.가계부내역);

            holder.수입.setText(금액표시(클릭일총수입)+"원");
            holder.지출.setText("0원");

        }
        else{
            long 클릭일총지출 = 지출가져오기(data.가계부내역);

            holder.수입.setText("0원");
            holder.지출.setText(금액표시(클릭일총지출)+"원");

        }


        if(holder.layout.getChildCount() != 0){
            holder.layout.removeAllViews();
            Log.i("!!!!!!","holder.layout.removeAllViews()");
        }

        for(int i=0;i<data.가계부내역.size();i++){


            accountBook 가계부 = data.가계부내역.get(i);

            View view = LayoutInflater.from(holder.layout.getContext()).inflate(R.layout.record_item, holder.layout, false);
            TextView 카테고리, 내용, 이체내용, 시간, 자산명, 가격;

            카테고리 = view.findViewById(R.id.textView20);
            내용 = view.findViewById(R.id.textView21);
            이체내용 = view.findViewById(R.id.textView22);

            시간 = view.findViewById(R.id.textView23);
            자산명 = view.findViewById(R.id.textView24);
            가격 = view.findViewById(R.id.textView25);

            카테고리.setText(가계부.카테고리);
            가격.setText(가계부.금액원 + "원");
            if(가계부.type == accountBook.수입){
                가격.setTextColor(Color.BLUE);

            }
            else{
                가격.setTextColor(Color.RED);

            }
            내용.setText(가계부.내용);
            시간.setText(시분.format(가계부.date.getTime()));
            자산명.setText(가계부.자산);


            int 포지션1  =position;
            int 포지션2 = i;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    accountBook 가계부 = list.get(position);
//                    Toast.makeText(context, "상세내역", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, EditRecordActivity.class);
                    intent.putExtra("가계부",가계부);
                    intent.putExtra("position",포지션1);
//                    intent.putExtra("position2",포지션2);

                    Log.i("",가계부.날짜);
                    Activity activity = (Activity)context;
                    activity.startActivityForResult(intent,REQUEST_EDIT);
//                    context.startActivity(intent);
//                    context.start
                }
            });

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            holder.layout.addView(view);

        }


    }

    final int REQUEST_EDIT = 101;

    String 금액표시(long 금액){
        return myFormatter.format(금액);
    }
    DecimalFormat myFormatter = new DecimalFormat("###,###");
    SimpleDateFormat 시분 = new SimpleDateFormat("a hh:mm", Locale.KOREA);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        LinearLayout layout;

        TextView 년월, 일, 요일, 지출,수입;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.linearLayout9);

            년월 = itemView.findViewById(R.id.textView68);
            일 = itemView.findViewById(R.id.textView66);
            요일 = itemView.findViewById(R.id.textView67);

            지출 = itemView.findViewById(R.id.textView70);
            수입 = itemView.findViewById(R.id.textView69);

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });


        }
    }

    public void setData(List<accountBook> 가계부목록 ){

        list.clear();

        List<Date> 날짜목록 = 날짜목록반환(가계부목록);
        Log.i("BudgetGraphInnerAdapter","날짜목록 크기 : "+Integer.toString(날짜목록.size()));

        for(int i=0;i<날짜목록.size();i++){

            Date date1 = 날짜목록.get(i);

            accountBook accountBook = new accountBook();
            Log.i("BudgetGraphInnerAdapter","날짜 : "+accountBook.날짜(date1));

            List<accountBook> 일가계부 = 가계부내역가져오기(가계부목록,date1);
            Log.i("BudgetGraphInnerAdapter","일가계부 크기 : "+Integer.toString(일가계부.size()));


//            Date date = 가계부.parseISODate(가계부.날짜);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date1);
            com.example.myaccountbook.data.Date date = com.example.myaccountbook.data.Date.getDate(calendar);


            DATA data = new DATA();
            data.가계부내역 = 일가계부;
            data.date = date;

            this.list.add(data);

        }

        notifyDataSetChanged();

    }

    public long 지출가져오기(List<accountBook> 내역기록){

        long 총지출 = 0;

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);

            if(내역.type == accountBook.고정지출 || 내역.type == accountBook.변동지출 ){
                총지출 += 내역.금액;
            }
        }
        return 총지출;

    }

    public long 수입가져오기(List<accountBook> 내역기록){

        long 총수입 = 0;

        for(int i=0;i<내역기록.size();i++){
            accountBook 내역 = 내역기록.get(i);

            if(내역.type == accountBook.수입){
                총수입+= 내역.금액;
            }
        }
        return 총수입;

    }
}
