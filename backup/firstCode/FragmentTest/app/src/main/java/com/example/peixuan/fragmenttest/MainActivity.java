package com.example.peixuan.fragmenttest;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                /*
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                AnotherRightFragment fragment = new AnotherRightFragment();
                fragmentTransaction.replace(R.id.right_layout, fragment, null);
                //当按back键不会推出程序，二是会懂上一个fragment
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                */
                break;
            default:
                break;
        }
    }
}
