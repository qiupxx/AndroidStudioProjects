package com.example.peixuan.todolist;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends Activity
        implements NewItemFragment.OnNewItemAddedListener {
    //create the array list of to do items
    private ArrayList<ToDoItem> todoItems;
    //create the array adapte to bind the array to the list view
    private ToDoItemAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate your view
        setContentView(R.layout.activity_main);

        todoItems = new ArrayList<ToDoItem>();
        int resID = R.layout.todolist_item;
        aa = new ToDoItemAdapter(this, resID, todoItems);

        FragmentManager fm = getFragmentManager();
        ToDoListFragment todoListFragment = (ToDoListFragment) fm.findFragmentById(R.id.ToDoListFragment);
        //bind the array adapte to the list view
        todoListFragment.setListAdapter(aa);

    }

    @Override
    public void onNewItemAdded(String newItem) {
        ToDoItem newTodoItem = new ToDoItem(newItem);
        todoItems.add(0, newTodoItem);
        aa.notifyDataSetChanged();
    }
}
