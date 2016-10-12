package com.asiabasehk.cgg.office.slidingtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.asiabasehk.cgg.office.slidingtest.R;
import com.asiabasehk.cgg.office.slidingtest.view.SlidingLayout;

import java.util.ArrayList;
import java.util.List;

public class AbsActivity extends AppCompatActivity {
    private List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abs);

        for (int i = 0; i <= 30; i++) {
            list.add("测试数据" + i);
        }

        ListView mListView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                AbsActivity.this, android.R.layout.simple_list_item_1, list);
        mListView.setAdapter(adapter);

        SlidingLayout slidingLayout = (SlidingLayout) findViewById(R.id.sildingFinishLayout);
        slidingLayout.setOnSlidingListener(new SlidingLayout.OnSlidingListener() {
            @Override
            public void onSliding() {
                AbsActivity.this.finish();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startActivity(new Intent(AbsActivity.this, NormalActivity.class));
            }
        });
    }
}
