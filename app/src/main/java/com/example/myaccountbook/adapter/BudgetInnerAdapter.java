package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.BudgetGraphActivity;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.R;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.Date;
import com.example.myaccountbook.data.Settlement;
import com.fenchtose.tooltip.Tooltip;

import java.util.Calendar;
import java.util.List;

public class BudgetInnerAdapter extends RecyclerView.Adapter<BudgetInnerAdapter.ViewHolder> {

    List<Settlement> list;
    Context context;
    MyApplication application;

    BudgetInnerAdapter(Context context, List<Settlement> list){

        this.list = list;
        this.context = context;
        application = (MyApplication) context.getApplicationContext();
    }

    @NonNull
    @Override
    public BudgetInnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgetinner_item, parent, false);
        return new BudgetInnerAdapter.ViewHolder(view);
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetInnerAdapter.ViewHolder holder, int position) {

        Settlement data = list.get(position);

        holder.지출.setText(data.지출());
        holder.카테고리명.setText(application.카테고리데이터명가져오기(data.예산.카테고리식별자));
        if(data.예산.카테고리식별자 == Budget.전체식별자){
            holder.카테고리명.setText(Budget.전체);

        }
        holder.예산.setText(data.예산금액());
        holder.남은금액.setText(data.남은돈());
        holder.퍼센트지.setText(data.퍼센트지());
        holder.bar.setProgress(data.퍼센트());

        if(data.퍼센트() > 100){
//            holder.bar.
            Drawable drawable = holder.bar.getProgressDrawable();
            int tintColor = ContextCompat.getColor(context, R.color.orange); // 원하는 색상 지정
            drawable.setTint(tintColor);

            holder.지출.setTextColor(Color.RED);
            holder.퍼센트지.setTextColor(Color.WHITE);

        }
        else{
            holder.지출.setTextColor(Color.BLUE);
            holder.퍼센트지.setTextColor(Color.BLACK);

        }


        if(오늘인가요(data.date)){
            holder.view.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.view.getLayoutParams();
//            params.horizontalBias = 0.2f; // here is one modification for example. modify anything else you want :)
            params.horizontalBias = 오늘의퍼센트지(data.date);
            holder.view.setLayoutParams(params); // request the view to use the new modified params

            if(data.예산.카테고리식별자 == Budget.전체식별자){

                int tipSizeSmall = context.getResources().getDimensionPixelSize(R.dimen.tip_dimen_small);

//            tipSizeSmall=12dp;
                Activity activity = (Activity)context;
                View content = activity.getLayoutInflater().inflate(R.layout.tooltip_textview, null);

//            context.getL
                // ConstraintLayout 가져오기

                // 새로운 ConstraintSet 생성
//            ConstraintSet constraintSet = new ConstraintSet();
//            constraintSet.clone(holder.layout);

                // 동적으로 설정할 뷰의 ID 가져오기 (예: R.id.myView)
//            int viewId = R.id.myView;

                // 뷰의 높이를 설정하려는 값으로 바꾸기 (예: 300 픽셀)
                int newHeight = context.getResources().getDimensionPixelSize(R.dimen.parent_height);
                // ConstraintSet을 사용하여 높이 설정
//            constraintSet.constrainHeight(R.id.constraintLayout1, newHeight);
//
//            // 새로운 ConstraintSet을 적용하여 레이아웃 업데이트
//            constraintSet.applyTo(holder.layout);
                // ConstraintLayout의 LayoutParams 가져오기
                ViewGroup.LayoutParams layoutParams = holder.layout.getLayoutParams();

                // 높이 설정
                layoutParams.height = newHeight;

                // 변경된 LayoutParams를 다시 설정
                holder.layout.setLayoutParams(layoutParams);

                Tooltip tooltip = new Tooltip.Builder(context)
                        .anchor(holder.view, Tooltip.TOP)
//                .animate(new TooltipAnimation(TooltipAnimation.REVEAL, 400))
//                .autoAdjust(true)
//                .autoCancel(2000)
                        .cancelable(false)
                        .content(content)
                        .withPadding(context.getResources().getDimensionPixelOffset(R.dimen.menu_tooltip_padding))
                        .withTip(new Tooltip.Tip(tipSizeSmall, tipSizeSmall, Color.GRAY))
                        .into(holder.layout)
                        .debug(true)
                        .show();

            }


        }
        else{
            holder.view.setVisibility(View.GONE);
        }

    }

    private float 오늘의퍼센트지(Date date){
        Calendar calendar = Calendar.getInstance();
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        float day2 = Float.parseFloat(Integer.toString(day));
        Log.i("RecyclerView2",Float.toString(day2));
//        Log.i("RecyclerView2",Long.toString(max));
//        Log.i("RecyclerView2",Long.toString(day2/max));
        return day2/max;

    }

    private boolean 오늘인가요(Date date){
        Calendar calendar = Calendar.getInstance();

        if(calendar.get(Calendar.YEAR)==date.getYear() && calendar.get(Calendar.MONTH) +1 == date.getMonth()){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView 카테고리명, 예산, 지출, 남은금액, 퍼센트지;

        View view;

        ConstraintLayout layout;

        ProgressBar bar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.constraintLayout1);

            카테고리명 = itemView.findViewById(R.id.textView53);
            예산 = itemView.findViewById(R.id.textView56);
            지출 = itemView.findViewById(R.id.textView57);
            남은금액 = itemView.findViewById(R.id.textView58);
            퍼센트지 = itemView.findViewById(R.id.textView59);

            view = itemView.findViewById(R.id.view13);

            bar = itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Settlement settlement = list.get(position);
                    Date date = settlement.date;

                    Intent intent = new Intent(context, BudgetGraphActivity.class);
                    intent.putExtra("year",date.getYear());
                    intent.putExtra("month",date.getMonth());
                    intent.putExtra("category",settlement.예산.카테고리식별자);
                    intent.putExtra("예산",settlement.예산.예산);
                    intent.putExtra("지출",settlement.지출);
//                    intent.putExtra("month",settlement.지출);

                    Activity activity = (Activity)context;
                    activity.startActivityForResult(intent,100);
//                    context.startActivity(intent);

                }
            });

        }
    }



}
