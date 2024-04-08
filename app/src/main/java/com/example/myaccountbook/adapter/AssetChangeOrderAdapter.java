package com.example.myaccountbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.R;

import java.util.List;

public class AssetChangeOrderAdapter extends RecyclerView.Adapter<AssetChangeOrderAdapter.ViewHolder> {

    Context context;
    List<String> list;

    public AssetChangeOrderAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public AssetChangeOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_change_order_item, parent, false);
        return new AssetChangeOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetChangeOrderAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }


    }
}
