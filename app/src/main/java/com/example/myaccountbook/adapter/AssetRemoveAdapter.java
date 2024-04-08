package com.example.myaccountbook.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.AssetDataManager;
import com.example.myaccountbook.AssetDetailActivity;
import com.example.myaccountbook.AssetRemoveActivity;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.R;
import com.example.myaccountbook.data.자산삭제아이템;

import java.util.ArrayList;
import java.util.List;

public class AssetRemoveAdapter extends RecyclerView.Adapter<AssetRemoveAdapter.ViewHolder> {

    Context context;
    List<AssetRemoveActivity.자산삭제아이템> list;
    List<Integer> removeItem;
    public AssetRemoveAdapter(List<AssetRemoveActivity.자산삭제아이템> list, AssetRemoveActivity context) {
        this.list = list;
        this.context = context;
        removeItem = new ArrayList<>();
    }

     void removeData(int index){

        list.remove(index);
        notifyItemRemoved(index);

    }


    @NonNull
    @Override
    public AssetRemoveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_remove_item, parent, false);
        return new AssetRemoveAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetRemoveAdapter.ViewHolder holder, int position) {
        AssetRemoveActivity.자산삭제아이템 아이템 = list.get(position);

        holder.자산명.setText(아이템.자산.자산명);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView 자산명;
        ImageButton 삭제버튼;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            자산명 = itemView.findViewById(R.id.textView124);
            삭제버튼 = itemView.findViewById(R.id.imageButton16);
            삭제버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    Log.i("","position : "+Integer.toString(position));
                    AssetRemoveActivity.자산삭제아이템 아이템 = list.get(position);

                    removeItem.add(아이템.position);

                    Intent resultIntent = new Intent();
                    resultIntent.putIntegerArrayListExtra("삭제아이템", (ArrayList<Integer>) removeItem);

                    Activity activity = (Activity) context;
                    MyApplication application = (MyApplication) activity.getApplication();

                    application.데이터삭제(아이템.자산.식별자);
                    // 결과 설정 및 현재 액티비티 종료
                    activity.setResult(activity.RESULT_OK, resultIntent);

                    removeData(position);

                }
            });

        }


    }
}
