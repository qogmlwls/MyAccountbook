package com.example.myaccountbook;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class SpinnerManager {



    static void setAdapter(Spinner spinner, Context context, List<String> items){
        // ArrayAdapter를 사용하여 Spinner에 목록 추가
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

//    static <T> void setAdapter(Spinner spinner, Context context, List<T> items){
//        // ArrayAdapter를 사용하여 Spinner에 목록 추가
//        ArrayAdapter<T> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//    }

//    // ArrayAdapter를 사용하여 Spinner에 목록 추가
//    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//    // Spinner에서 항목을 선택했을 때 이벤트 처리
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            String selectedItem = parent.getItemAtPosition(position).toString();
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            // 아무 항목도 선택하지 않았을 때의 동작
//        }
//    });



}
