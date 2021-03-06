package com.example.peixuan.todolist;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ToDoListActivity extends Activity implements OnNewItemAddedListener {
    protected static final String TAG = "ToDoListActivity";

    private ArrayList<String> todoItems;
    private ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate layout
        setContentView(R.layout.main);

        FragmentManager fm = getFragmentManager();
        ToDoListFragment toDoListFragment = (ToDoListFragment) fm.findFragmentById(R.id.ToDoListFragment);

        todoItems = new ArrayList<>();

        //bind adapter to data source
        int resID = R.layout.todolist_item;
        aa = new ArrayAdapter<String>(this, resID, todoItems);

        toDoListFragment.setListAdapter(aa);
    }

    @Override
    public void onNewItemAdded(String newItem) {
        todoItems.add(0, newItem);
        aa.notifyDataSetChanged();
    }
}
