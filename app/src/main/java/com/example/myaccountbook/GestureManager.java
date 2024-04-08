package com.example.myaccountbook;

import android.content.Context;
import android.view.GestureDetector;

public class GestureManager {
    GestureDetector detector;
    MyGestureListener myGestureListener;

    public GestureDetector getDetector(){
        return detector;
    }
    GestureManager(Context context){

        myGestureListener = new MyGestureListener();


        detector = new GestureDetector(context, myGestureListener );

    }


    public void setMyInterface(MyGestureListener.MyInterface myInterface) {

        myGestureListener.setMyInterface(myInterface);

    }



}
