package com.example.peixuan.codesnippet7;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by peixuan on 16/7/1.
 */
public class MyFragment extends Fragment {
    private static String USER_SELECTION = "USER_SELECTION";
    private int userSelection = 0;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Control whether a fragment instance is retained across Activity
         * re-creation (such as from a configuration change).  This can only
         * be used with fragments not in the back stack.  If set, the fragment
         * lifecycle will be slightly different when an activity is recreated:
         */
        setRetainInstance(true);
        if (savedInstanceState != null)
            userSelection = savedInstanceState.getInt(USER_SELECTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragment, container, false);

        textView = (TextView) view.findViewById(R.id.textView);
        setSelection(userSelection);

        Button b1 = (Button) view.findViewById(R.id.button1);
        Button b2 = (Button) view.findViewById(R.id.button2);
        Button b3 = (Button) view.findViewById(R.id.button3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(1);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(2);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(3);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(USER_SELECTION, userSelection);
        super.onSaveInstanceState(outState);
    }

    private void setSelection(int selection) {
        userSelection = selection;
        textView.setText("selected: " + selection);
    }
}