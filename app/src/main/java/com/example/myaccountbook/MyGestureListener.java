package com.example.myaccountbook;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {


    String TAG = "MyGestureListener";
    boolean result = true;

    MyInterface myInterface;

    public void setMyInterface(MyInterface myInterface) {
        this.myInterface = myInterface;
    }

    public interface MyInterface {
        void 이전으로이동();
        void 이후로이동();
    }

    public boolean onDown(MotionEvent motionEvent) {
        println("onDown() 호출됨");
        println("motionEvent X : "+Integer.toString((int) motionEvent.getX()));
        println("motionEvent Y : "+Integer.toString((int) motionEvent.getY()));
        result = true;

        return true;
    }
    public void onShowPress(MotionEvent motionEvent) {
        println("onSHowPress() 호출됨");
    }
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        println("onSingleTapUp() 호출됨");
        return true;
    }
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1){
        println("onScroll() 호출됨 : ");

        int action = motionEvent1.getActionMasked();
        println("motionEvent1.getActionMasked()"+Integer.toString(action));
//                if(action == MotionEvent.ACTION_UP){
//                    println("---------------------------------------------");
//                    println("MotionEvent.ACTION_UP");
//                    println("---------------------------------------------");
//
//                }
//                int action1 = motionEvent.getActionMasked();
//
//                if(action1 == MotionEvent.ACTION_DOWN){
//                    println("---------------------------------------------");
//                    println("MotionEvent.ACTION_DOWN");
//                    println("---------------------------------------------");
//
//                }
//                if(action1 == MotionEvent.ACTION_UP){
//                    println("---------------------------------------------");
//                    println("MotionEvent.ACTION_UP");
//                    println("---------------------------------------------");
//
//                }
        if(motionEvent == null){
            println("motionEvent null");
            println("motionEvent1 X : "+Integer.toString((int) motionEvent1.getX()));
            println("motionEvent1 Y : "+Integer.toString((int) motionEvent1.getY()));

        }
        else{
            println("motionEvent X : "+Integer.toString((int) motionEvent.getX()));
            println("motionEvent Y : "+Integer.toString((int) motionEvent.getY()));
            println("motionEvent1 X : "+Integer.toString((int) motionEvent1.getX()));
            println("motionEvent1 Y : "+Integer.toString((int) motionEvent1.getY()));

            float X = (motionEvent1.getX() - motionEvent.getX());
            float Y = (motionEvent1.getY() - motionEvent.getY());

            println("Y*Y/X*X : "+Float.toString((Y*Y)/(X*X)));
            if( X*X > 600*600 && (Y*Y)/(X*X) < 1.0 ){
                if(X > 0){
                    println("X > 0-------------------------------------------------");
                    if(result){
                        result = false;
                        println("이전으로 이동함.");
                        myInterface.이전으로이동();
//                    제스쳐.setText("이전으로 이동");
                    }
                }
                else{
                    println("X < 0-------------------------------------------------");
                    if(result){
                        result = false;
                        println("이후로 이동함");
                        myInterface.이후로이동();
//                    제스쳐.setText("이후로 이동");
                    }
                }
            }
        }
        println("onScroll() 호출됨 : "+v+", "+v1);

        return true;
    }
    public void onLongPress(MotionEvent motionEvent) {
        println("onLongPress() 호출됨");
    }
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1){

        println("onFling() 호출됨 : " +v+", "+v1);

        if(motionEvent == null){
            println("motionEvent null");
            println("motionEvent1 X : "+Integer.toString((int) motionEvent1.getX()));
            println("motionEvent1 Y : "+Integer.toString((int) motionEvent1.getY()));
            return true;
        }

        println("motionEvent X : "+Integer.toString((int) motionEvent.getX()));
        println("motionEvent Y : "+Integer.toString((int) motionEvent.getY()));

        println("motionEvent1 X : "+Integer.toString((int) motionEvent1.getX()));
        println("motionEvent1 Y : "+Integer.toString((int) motionEvent1.getY()));

        float X = (motionEvent1.getX() - motionEvent.getX());
        float Y = (motionEvent1.getY() - motionEvent.getY());

        println("Y*Y/X*X : "+Float.toString((Y*Y)/(X*X)));

        if( (Y*Y)/(X*X) < 1.0){
            if(X > 0){
                println("X > 0-------------------------------------------------");
                if(result){
                    result = false;
                    println("이전으로 이동함.");
                    myInterface.이전으로이동();
//                    제스쳐.setText("이전으로 이동");
                }
            }
            else{
                println("X < 0-------------------------------------------------");
                if(result){
                    result = false;
                    println("이후로 이동함");
                    myInterface.이후로이동();
//                    제스쳐.setText("이후로 이동");
                }
            }
        }

        return true;
    }
    void println(String message){
        Log.i("println",message);
    }

}
