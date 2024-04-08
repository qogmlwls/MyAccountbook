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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.AssetCreateActivity;
import com.example.myaccountbook.AssetDataManager;
import com.example.myaccountbook.AssetDetailActivity;
import com.example.myaccountbook.AssetDetailGraphActivity;
import com.example.myaccountbook.AssetPageActivity;
import com.example.myaccountbook.AssetRemoveActivity;
import com.example.myaccountbook.R;
import com.example.myaccountbook.accountBook;
import com.example.myaccountbook.data.Budget;
import com.example.myaccountbook.data.RequestCode;

import org.w3c.dom.Text;

import java.util.List;

public class AssetPageAdapter extends RecyclerView.Adapter<AssetPageAdapter.ViewHolder> {

    Context context;
    List<AssetDataManager.자산별합계> list;
    final int REQUEST_DETAIL_GRAPH = 104;

    public AssetPageAdapter(List<AssetDataManager.자산별합계> list, Context context){
        this.list = list;
        this.context = context;
    }


    public void addData(AssetDataManager.자산별합계 data){

         list.add(data);
         notifyItemInserted(list.size()-1);

    }

    public AssetDataManager.자산별합계 getItem(int index){
        return list.get(index);
    }

    public void clear(){

        list.clear();
        notifyDataSetChanged();

    }

    public void setData(List<AssetDataManager.자산별합계> list){
        this.list = list;
    }

    public void removeData(int index){

        list.remove(index);
        notifyItemRemoved(index);

    }

    @NonNull
    @Override
    public AssetPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_page_item, parent, false);
        return new AssetPageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetPageAdapter.ViewHolder holder, int position) {

        AssetDataManager.자산별합계 자산별합계 = list.get(position);

        holder.자산명.setText(자산별합계.자산.자산명);

        if(자산별합계.합계.amount > 0){
            holder.금액.setTextColor(Color.BLUE);
            holder.금액.setText(자산별합계.합계.getAmount());

        }
        else if(자산별합계.합계.amount < 0){
            holder.금액.setTextColor(Color.RED);
            holder.금액.setText(자산별합계.합계.getAmount(true));

        }
        else if(자산별합계.합계.amount == 0){
            holder.금액.setTextColor(Color.BLACK);
            holder.금액.setText(자산별합계.합계.getAmount());

        }
        else {

        }


    }

    public int getItemCount() {

        return list.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView 자산명, 금액;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            자산명 = itemView.findViewById(R.id.textView118);
            금액 = itemView.findViewById(R.id.textView119);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    AssetDataManager.자산별합계 data = list.get(position);

                    Intent intent = new Intent(context, AssetDetailActivity.class);
                    intent.putExtra("자산식별자",data.자산.식별자);
                    intent.putExtra("자산명",data.자산.자산명);

                    Activity activity1 = (Activity)context;
                    activity1.startActivityForResult(intent, RequestCode.REQUEST_DETAIL_GRAPH);
//                    startActivityForResult(AssetRemoveActivity.class, RequestCode.REQUEST_REMOVEASSET);

                }
            });


        }


    }

}