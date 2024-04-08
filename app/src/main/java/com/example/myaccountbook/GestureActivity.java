package com.example.myaccountbook;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureActivity extends CommonActivity {

    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGestureListener myGestureListener;
        myGestureListener = new MyGestureListener();
        myGestureListener.setMyInterface(new MyGestureListener.MyInterface() {
            @Override
            public void 이전으로이동() {
                Log.i("MainActivity9","MainActivity9");
                이전으로이동();
//                제스쳐.setText("textView12 이전으로 이동");
            }

            @Override
            public void 이후로이동() {
                Log.i("MainActivity9","MainActivity9");
                이후로이동();
//                제스쳐.setText("textView12 이후로 이동");
            }
        });

        detector = new GestureDetector(this,myGestureListener );


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    protected void 이전으로이동(){

    }

    protected void 이후로이동(){

    }

}
