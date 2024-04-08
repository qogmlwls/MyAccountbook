package com.example.myaccountbook.adapter;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaccountbook.CategoryActivity;
import com.example.myaccountbook.Data;
import com.example.myaccountbook.MyApplication;
import com.example.myaccountbook.MyDialog;
import com.example.myaccountbook.R;
import com.example.myaccountbook.data.Budget;

import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {


    List<Data> list;
    Context context;
    boolean 통계청목록클릭;
    public AssetAdapter(List<Data> list, Context context){
        this.list = list;
        this.context = context;
//        통계청목록클릭 = false;

    }

    @NonNull
    @Override
    public AssetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        return new AssetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssetAdapter.ViewHolder holder, int position) {

//
        Data routinesName = list.get(position);
        holder.반복종류.setText(routinesName.name);

        if(routinesName.type == Data.카테고리){
            if(routinesName.name.equals(Budget.전체)){
                holder.수정.setVisibility(View.GONE);
                holder.삭제.setVisibility(View.GONE);
            }
            else{
                holder.수정.setVisibility(View.VISIBLE);
                holder.삭제.setVisibility(View.VISIBLE);
            }
        }
//
//        if(routinesName.enable == Data.비활성화){
//            holder.
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView 반복종류;

        ImageButton 수정, 삭제;

        public ViewHolder(View view) {
            super(view);

            반복종류 = view.findViewById(R.id.textView26);
//            반복종류.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//
//                }
//            });

            수정 = view.findViewById(R.id.imageButton4);
            수정.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // 결과 전달을 위한 Intent 생성 및 데이터 추가
                        Data data = list.get(position);
                        MyDialog dialog = new MyDialog();

                        if(data.type == Data.자산){
                            Dialog 자산다이얼로그 = dialog.자산추가수정다이얼로그생성(context);
                            자산다이얼로그.show();
//                            dialog.자산수정(context, AssetAdapter.this,data);
//                        자산다이얼로그.
                            Button button = 자산다이얼로그.findViewById(R.id.button17);
                            button.setText("자산 수정");
                            EditText 입력값 = 자산다이얼로그.findViewById(R.id.editTextText5);
                            입력값.setText(data.name);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(입력값.getText().toString().length() == 0){
                                        Toast.makeText(context, "자산 명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
//                    Data data = new Data();
//                    .type = Data.자산;
                                        data.name = 입력값.getText().toString();

                                        MyApplication application = (MyApplication) context.getApplicationContext();
                                        application.데이터수정(data);

                                        for(int i=0;i<application.자산데이터.size();i++){
                                            Data applicationData = application.자산데이터.get(i);


                                            if(data.pk == applicationData.pk){
                                                applicationData.name = data.name;
                                            }
                                        }

//                    long row =  application.데이터저장하기(data);
//                    data.pk = Integer.parseInt(Long.toString(row));
//                    application.자산데이터.add(data);
                                        notifyDataSetChanged();
//                            dialog.cancel();
                                        자산다이얼로그.dismiss();
                                    }

                                }
                            });
                        }
                        else if(data.type == Data.카테고리){

                            통계청목록클릭 = false;


                            MyApplication application = (MyApplication)context.getApplicationContext();

                            MyDialog myDialog = new MyDialog();
//                dialog.자산추가(AssetActivity.this,application,adapter);

                            Dialog 카테고리추가수정다이얼로그 = myDialog.카테고리추가수정다이얼로그생성(context);
                            카테고리추가수정다이얼로그.show();
                            Button 카테고리추가버튼 = 카테고리추가수정다이얼로그.findViewById(R.id.button21);
                            카테고리추가버튼.setText("카테고리 수정");
                            EditText 입력값 = 카테고리추가수정다이얼로그.findViewById(R.id.editTextText10);
                            입력값.setText(data.name);
                            Spinner 통계청카테고리 = 카테고리추가수정다이얼로그.findViewById(R.id.spinner);

                            EditText 입력값2 = 카테고리추가수정다이얼로그.findViewById(R.id.editTextText12);
                            입력값2.setText(application.카테고리데이터명(data.office_category));

                            Log.i("카테고리 수정",data.name);
                            Log.i("카테고리 수정",Integer.toString(data.office_category));


//                            Toast.makeText(context, application.카테고리데이터명(data.office_category), Toast.LENGTH_SHORT).show();
                            입력값2.setClickable(false);
                            입력값2.setFocusable(false);
                            입력값2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    통계청목록클릭 = true;
//                        Toast.makeText(CategoryActivity.this, "click", Toast.LENGTH_SHORT).show();
                                    통계청카테고리.performClick();

                                }
                            });

                            카테고리추가버튼.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(입력값.getText().toString().length() == 0){
                                        Toast.makeText( context, "카테고리 명을 입력해주세요", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(입력값2.getText().toString().length()==0){
                                        Toast.makeText(context, "카테고리 설명을 선택해주세요", Toast.LENGTH_SHORT).show();
                                    }
                                    else{

                                        // 카테고리 추가.
//                                        Data data = new Data();
//                                        data.type = Data.카테고리;
                                        data.name = 입력값.getText().toString();
                                        data.office_category = application.카테고리데이터식별자(입력값2.getText().toString());

                                        if(data.office_category == -1){
                                            Toast.makeText(context, "문제 발생", Toast.LENGTH_SHORT).show();
                                            Log.i("TAG","data office_category 이상.");
                                            Log.i("TAG","data office_category : "+Integer.toString(data.office_category));
                                            return;
                                        }

                                        application.데이터수정(data);
                                        for(int i=0;i<application.카테고리데이터.size();i++){
                                            Data applicationData = application.카테고리데이터.get(i);


                                            if(data.pk == applicationData.pk){
                                                applicationData.name = data.name;
                                                applicationData.office_category = data.office_category;
                                            }
                                        }
//                                        long row =  application.데이터저장하기(data);
//                                        data.pk = Integer.parseInt(Long.toString(row));
//                                        application.카테고리데이터.add(data);
//                                        list.add(data);
                                        notifyDataSetChanged();

                                        카테고리추가수정다이얼로그.dismiss();

                                    }
                                }
                            });

                            // Spinner에 들어갈 목록 생성
                            List<String> items = application.통계청가져오기();

                            // ArrayAdapter를 사용하여 Spinner에 목록 추가
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            통계청카테고리.setAdapter(adapter);
                            통계청카테고리.setSelection(adapter.getPosition(application.카테고리데이터명(data.office_category)));

                            // Spinner에서 항목을 선택했을 때 이벤트 처리
                            통계청카테고리.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedItem = parent.getItemAtPosition(position).toString();

                                    if(통계청목록클릭)
                                        입력값2.setText(selectedItem);
                                    통계청목록클릭 = false;

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // 아무 항목도 선택하지 않았을 때의 동작
                                }
                            });


//                            입력값2.setText(application.카테고리데이터명(data.office_category));

                        }
                    }
                }
            });

            삭제 = view.findViewById(R.id.imageButton3);
            삭제.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MyDialog dialog = new MyDialog();
                    Dialog 삭제다이얼로그 = dialog.삭제다이얼로그생성(context);
                    삭제다이얼로그.show();

                    Button 아니오 = 삭제다이얼로그.findViewById(R.id.button13);
                    Button 예 = 삭제다이얼로그.findViewById(R.id.button20);

                    아니오.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            삭제다이얼로그.dismiss();
                        }
                    });
                    예.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // enable 값을 1로 변경.
                            // adapter 리스트에서 값 변경.
                            // 현재 삭제할 값을 선택했다면 선택값 리셋 (신호 보내기)

                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                // 결과 전달을 위한 Intent 생성 및 데이터 추가

                                Data data = list.get(position);
                                data.enable = Data.비활성화;
                                if(data.type == Data.자산){
                                    MyApplication application = (MyApplication) context.getApplicationContext();
                                    application.데이터수정(data);

                                    for(int i=0;i<application.자산데이터.size();i++){
                                        Data applicationData = application.자산데이터.get(i);

                                        if(data.pk == applicationData.pk){
                                            applicationData.enable = Data.비활성화;
                                        }
                                    }

                                }
                                else if(data.type == Data.카테고리){

                                    MyApplication application = (MyApplication) context.getApplicationContext();
                                    application.데이터수정(data);

                                    for(int i=0;i<application.카테고리데이터.size();i++){
                                        Data applicationData = application.카테고리데이터.get(i);

                                        if(data.pk == applicationData.pk){
                                            applicationData.enable = Data.비활성화;
                                        }
                                    }


                                }
                                list.remove(position);
                                notifyItemRemoved(position);

                                삭제다이얼로그.dismiss();
                            }

                        }
                    });


                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // 결과 전달을 위한 Intent 생성 및 데이터 추가

                        Data data = list.get(position);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("name", data.name);
                        resultIntent.putExtra("pk", data.pk);

                        Activity activity = (Activity)context;
                        // 결과 설정 및 현재 액티비티 종료
                        activity.setResult(RESULT_OK, resultIntent);
                        activity.finish();
                    }

                }
            });

        }


    }

}