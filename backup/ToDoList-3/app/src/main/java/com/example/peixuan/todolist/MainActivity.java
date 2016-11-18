package com.example.peixuan.todolist;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity implements NewItemFragment.OnNewItemAddedListener {
    //create the array list of to do items
    private ArrayList<String> todoItems;
    //create the array adapte to bind the array to the list view
    private ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate your view
        setContentView(R.layout.activity_main);

        todoItems = new ArrayList<String>();
        aa = new ArrayAdapter<String>(this, R.layout.todolist_item, todoItems);

        FragmentManager fm = getFragmentManager();
        ToDoListFragment todoListFragment = (ToDoListFragment) fm.findFragmentById(R.id.ToDoListFragment);
        //bind the array adapte to the list view
        todoListFragment.setListAdapter(aa);

    }

    @Override
    public void onNewItemAdded(String item) {
       todoItems.add(item);
        aa.notifyDataSetChanged();
    }
}
