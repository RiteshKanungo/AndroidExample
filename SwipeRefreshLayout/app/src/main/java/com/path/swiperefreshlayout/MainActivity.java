package com.path.swiperefreshlayout;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ArrayList arrayList = new ArrayList();
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        listView = findViewById(R.id.listView);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        arrayList.add("First Element");
        arrayList.add("Second Element");
        arrayList.add("Third Element");
        arrayList.add("Fourth Element");
        arrayList.add("Fifth Element");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shuffle();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void shuffle() {
        Collections.shuffle(arrayList, new Random(System.currentTimeMillis()));
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
    }

}
