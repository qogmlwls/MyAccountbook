package com.example.myaccountbook.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.myaccountbook.AssetDataManager;

public class 자산삭제아이템 implements Parcelable {

    int position;
    AssetDataManager.자산 자산;


    protected 자산삭제아이템(Parcel in) {
        position = in.readInt();
    }

    public static final Creator<자산삭제아이템> CREATOR = new Creator<자산삭제아이템>() {
        @Override
        public 자산삭제아이템 createFromParcel(Parcel in) {
            return new 자산삭제아이템(in);
        }

        @Override
        public 자산삭제아이템[] newArray(int size) {
            return new 자산삭제아이템[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(position);
    }
}
