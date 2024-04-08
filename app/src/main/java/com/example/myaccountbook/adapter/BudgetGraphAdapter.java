package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.BudgetManager;
import com.example.myaccountbook.Calendar;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.R;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Settlement;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class BudgetGraphAdapter extends RecyclerView.Adapter<BudgetGraphAdapter.ViewHolder> {


    List<Date> list;

    Context context;

    Calendar 달력;

    MyApplication application;

    BudgetManager budgetManager;
    int category_pk;
    public BudgetGraphAdapter(Context context, List<Date> list,int 카테고리식별자){

        this.list = list;
        this.context = context;
        application = (MyApplication) context.getApplicationContext();
        달력 = new Calendar(application);

        budgetManager = new BudgetManager();
        category_pk = 카테고리식별자;

    }


    @NonNull
    @Override
    public BudgetGraphAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_graph_item, parent, false);
        return new BudgetGraphAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetGraphAdapter.ViewHolder holder, int position) {

        Date date = list.get(position);

        holder.날짜.setText(날짜가져오기(date));
        holder.toolbar.setTitle(application.카테고리데이터명가져오기(category_pk));
        if(category_pk == Budget.전체식별자){
            holder.toolbar.setTitle(Budget.전체);
        }
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.setSupportActionBar(holder.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        BudgetManager.카테고리가계부목록 data = budgetManager.getList(date,application,category_pk);

        holder.예산.setText(data.settlement.예산금액());
//        if(data.)

        holder.사용금액.setText(data.settlement.지출());
        holder.남은금액.setText(data.settlement.남은돈());
        if(data.settlement.퍼센트()>100){
            holder.남은금액.setTextColor(Color.RED);
        }
        else{
            holder.남은금액.setTextColor(Color.BLACK);
        }


        BudgetGraphInnerAdapter adapter = new BudgetGraphInnerAdapter(context,data.카테고리내역기록,date.getYear(),date.getMonth());
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        for(int i=0;i<list.size();i++){
//            Date date1 = list.get(i);
//            Log.i("BudgetGraer",날짜가져오기(date1));
//        }
//


        if(data.카테고리내역기록.size() == 0){
            holder.데이터가없습니다.setVisibility(View.VISIBLE);
        }
        else{
            holder.데이터가없습니다.setVisibility(View.GONE);
        }



        holder.chart.getDescription().setEnabled(false);
        holder.chart.setBackgroundColor(Color.WHITE);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);
        holder.chart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        holder.chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
//        chart.getDescription().setEnabled(false); // chart 밑에 description 표시 유무
        holder.chart.getLegend().setEnabled(false); // Legend는 차트의 범례

        Legend l = holder.chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setDrawInside(false);

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawLabels(false); // label 삭제

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = holder.chart.getXAxis();
//            xAxis.setAxisMaximum(data.getXMax() + 0.25f);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new MyAxisFormatter());

//            xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        xAxis.setLabelCount(12);
        List<Settlement> settlements = budgetManager.getSettlementList(date,application,category_pk);

        Log.i("settlements",Integer.toString(settlements.size()));
        Log.i("settlements",Integer.toString(date.getMonth()-1));


        CombinedData combinedData = new CombinedData();
        combinedData.setData(generateLineData(settlements,date.getMonth()-1));

        combinedData.setData(generateBarData(settlements));

//            XAxis xAxis = chart.getXAxis();
//        xAxis.setAxisMaximum(combinedData.getXMax() + 0.25f);
        holder.chart.setData(combinedData);
        holder.chart.invalidate();

        if(category_pk != Budget.전체식별자){



        }



    }





    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        Button 날짜;

        Toolbar toolbar;

        TextView 예산,사용금액,남은금액;

        RecyclerView recyclerView;
        TextView 데이터가없습니다;

        private CombinedChart chart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            날짜 = itemView.findViewById(R.id.button8);
            toolbar = itemView.findViewById(R.id.toolbar9);
            recyclerView = itemView.findViewById(R.id.recyclerView7);


            예산 = itemView.findViewById(R.id.textView63);
            사용금액 = itemView.findViewById(R.id.textView64);
            남은금액 = itemView.findViewById(R.id.textView65);

            데이터가없습니다 = itemView.findViewById(R.id.textView72);

            chart = itemView.findViewById(R.id.chart1);

        }
    }


    private BarData generateBarData(List<Settlement> settlements ) {

        ArrayList<BarEntry> entries1 = new ArrayList<>();
//        ArrayList<BarEntry> entries2 = new ArrayList<>();

        for (int index = 0; index < count; index++) {

            Settlement settlement = settlements.get(index);

//            settlement.
//            entries1.add(new BarEntry(0, getRandom(25, 25)));
            entries1.add(new BarEntry(index+1, settlement.지출));
//            entries2.add(new BarEntry(0, 1));

            // stacked
//            entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));

        }

        BarDataSet set1 = new BarDataSet(entries1, "지출");
        set1.setValueFormatter(new Formatter());

        set1.setColor(Color.rgb(0, 0, 255));
//        set1.setValueTextColor(Color.rgb(255, 255, 255));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
//
//        BarDataSet set2 = new BarDataSet(entries2, "");
//        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
//        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255));
//        set2.setValueTextColor(Color.rgb(61, 165, 255));
//        set2.setValueTextSize(10f);
//        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

//        BarData d = new BarData(set1);
//
//        d.groupBars(0,groupSpace,barSpace);
        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);



//         make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0


        return d;
    }
    private LineData generateLineData(List<Settlement> settlements , int data_index) {

        LineData d = new LineData();

//        Settlement settlement;
//        settlement.예산.예산

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++){

            Settlement settlement = settlements.get(index);

            Log.i("예산",Integer.toString(settlement.예산.예산));



            int 예산 = settlement.예산.예산;
            if(index == data_index){
//                Drawable drawable = getDrawable(R.drawable.my_selector);
                Entry entry = new Entry(index+1,예산);
//            entry

                entries.add(entry);
            }
            else{
                Drawable drawable = context.getDrawable(R.drawable.my_selector2);
                Entry entry = new Entry(index+1,예산,drawable);
//            entry

                entries.add(entry);
            }


        }

        LineDataSet set = new LineDataSet(entries, "예산");
        set.setValueTextColor(Color.rgb(255, 255, 255));
        set.setValueFormatter(new Formatter());

//        set.set
        set.setColor(Color.rgb(255, 0, 0));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(255, 0, 0));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(255, 0, 0));

//        set.setDrawCircleHole(true);
//        set.setCircleHoleColor(Color.rgb(255,0,0));
//


//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setMode(LineDataSet.Mode.LINEAR);
//        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        set.setDrawValues(true);
//        set.setValueTextSize(10f);
//        set.setValueTextColor(Color.rgb(240, 238, 70));

//        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);
//        d.addXValue(monthNameString);
//        d.setCircleColorHole(Color.parseColor("#891e9a"));

//        set.setCircleColors();
        return d;
    }

    private final int count = 12;

    private class MyAxisFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {

            int value2 = (int)value;
            if(value2 == 0){
                return "";
            }
            return Integer.toString(value2)+"월";
        }
    }
    private class Formatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return "";
//            int value2 = (int)value;
//            if(value2 == 0){
//
//            }
//            return Integer.toString(value2)+"월";
        }
    }
    public String 날짜가져오기(Date date){

        return date.년월날짜();

    }

}
