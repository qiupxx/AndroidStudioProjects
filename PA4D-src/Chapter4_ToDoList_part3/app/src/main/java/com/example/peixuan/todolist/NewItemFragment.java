package com.example.peixuan.todolist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by peixuan on 16/11/13.
 */

public class NewItemFragment extends Fragment {
    private OnNewItemAddedListener onNewItemAddedListener;

    //deprecated in api23
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            onNewItemAddedListener = (OnNewItemAddedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(((Activity)context).toString() + "must implement OnNewItemAddedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_item_fragment, container, false);

        final EditText myEditText = (EditText) view.findViewById(R.id.myEditText);
        //register key listener
        myEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        Log.d(ToDoListActivity.TAG, "onKey invoked");
                        String newItem = myEditText.getText().toString();
                        onNewItemAddedListener.onNewItemAdded(newItem);
                        myEditText.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }
}
