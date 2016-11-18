package com.example.peixuan.actionbar;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ActionBar actionBar = getActionBar();
        //显示navigation up
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
