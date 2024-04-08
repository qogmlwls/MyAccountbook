package com.example.myaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myaccountbook.adapter.AssetChangeOrderAdapter;
import com.example.myaccountbook.adapter.AssetPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssetChangeOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_change_order);

        RecyclerView recyclerView = findViewById(R.id.recyclerView12);
        List<String> list;
        list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        AssetChangeOrderAdapter adapter = new AssetChangeOrderAdapter(list,AssetChangeOrderActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
}