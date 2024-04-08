package com.example.myaccountbook;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myaccountbook.adapter.AssetAdapter;

public class MyDialog {


    public Dialog 이름이없습니다다이얼로그생성(Context context){
        Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.no_name_dialog);

        return dialog1;
//        dialog1.show();

    }


    public Dialog 삭제다이얼로그생성(Context context){
        Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.delete);

        return dialog1;
//        dialog1.show();

    }
//    public void 자산수정(Context context, AssetAdapter adapter,Data 자산){
//        Dialog dialog1 = new Dialog(context);
//        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog1.setContentView(R.layout.assetcreate);
//
//        dialog1.show();
//
//
////        Button button = dialog1.findViewById(R.id.button17);
////        button.setText("자산 수정");
////        EditText 입력값 = dialog1.findViewById(R.id.editTextText5);
////        입력값.setText(자산.name);
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                if(입력값.getText().toString().length() == 0){
////                    Toast.makeText(context, "자산 명을 입력해주세요.", Toast.LENGTH_SHORT).show();
////                }
////                else{
//////                    Data data = new Data();
//////                    .type = Data.자산;
////                    자산.name = 입력값.getText().toString();
////
////                    application.자산수정(자산);
////
//////                    long row =  application.데이터저장하기(data);
//////                    data.pk = Integer.parseInt(Long.toString(row));
//////                    application.자산데이터.add(data);
////                    adapter.notifyDataSetChanged();
//////                            dialog.cancel();
////                    dialog1.dismiss();
////                }
////
////            }
////        });
//    }

    public Dialog 자산추가수정다이얼로그생성(Context context){
        Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.assetcreate);
        return dialog1;
    }
    public Dialog 카테고리추가수정다이얼로그생성(Context context){
        Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.categorycreate);
        return dialog1;
    }
}
