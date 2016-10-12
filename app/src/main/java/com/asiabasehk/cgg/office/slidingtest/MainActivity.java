package com.asiabasehk.cgg.office.slidingtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.asiabasehk.cgg.office.slidingtest.activity.AbsActivity;
import com.asiabasehk.cgg.office.slidingtest.activity.NormalActivity;
import com.asiabasehk.cgg.office.slidingtest.activity.ScrollActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mButtonNormal = (Button) findViewById(R.id.normal_activity);
        mButtonNormal.setOnClickListener(this);

        Button mButtonAbs = (Button) findViewById(R.id.absListview_activity);
        mButtonAbs.setOnClickListener(this);

        Button mButtonScroll = (Button) findViewById(R.id.scrollview_activity);
        mButtonScroll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent = null;
        switch (v.getId()) {
            case R.id.normal_activity:
                mIntent = new Intent(MainActivity.this, NormalActivity.class);
                break;
            case R.id.absListview_activity:
                mIntent = new Intent(MainActivity.this, AbsActivity.class);
                break;
            case R.id.scrollview_activity:
                mIntent = new Intent(MainActivity.this, ScrollActivity.class);
                break;
        }

        startActivity(mIntent);
    }
}
