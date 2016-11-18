package com.example.peixuan.todolist;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by peixuan on 16/5/26.
 */
public class NewItemFragment extends Fragment {

    public interface OnNewItemAddedListener {
        void onNewItemAdded(String item);
    }
    private OnNewItemAddedListener onNewItemAddedListener;

    /**
     * 当Attach到Activity时被调用
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            onNewItemAddedListener = (OnNewItemAddedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnNewItemAddedListener");
        }
    }

    /**
     * 如果自定义的Fragment实现客制化的View, 在onCreateView()内展开布局，返回View
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inherited root xml
        View view = inflater.inflate(R.layout.new_item_fragment, container, false);

        final EditText myEditText = (EditText) view.findViewById(R.id.myEditText);
        //set key listener for EditText
        myEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                            keyCode == KeyEvent.KEYCODE_ENTER) {
                        String newItem = myEditText.getText().toString();
                        onNewItemAddedListener.onNewItemAdded(newItem);
                        myEditText.setText("");
                        return true;
                    }
                return false;
            }
        });
        return view;
    }
}
