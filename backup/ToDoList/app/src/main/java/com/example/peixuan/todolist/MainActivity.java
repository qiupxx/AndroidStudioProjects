package com.example.peixuan.todolist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends Activity implements NewItemFragment.OnNewItemAddedListener,
    LoaderManager.LoaderCallbacks<Cursor> {/*LoaderManager管理Loaders,适合异步执行长时间运行任务*/
    private ArrayList<ToDoItem> todoItems;
    private ToDoItemAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate your layout
        setContentView(R.layout.activity_main);

        todoItems = new ArrayList<ToDoItem>();
        aa = new ToDoItemAdapter(this, R.layout.todolist_item, todoItems);

        FragmentManager fm = getFragmentManager();
        ToDoListFragment todoListFragment = (ToDoListFragment) fm.findFragmentById(R.id.ToDoListFragment);
        //bind the array adapte to the list view
        todoListFragment.setListAdapter(aa);

        //在onCreate()方法内初始化Loader
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    /**
     * 实现NewItemFragment.OnNewItemAddedListener
     * @param newItem
     */
    @Override
    public void onNewItemAdded(String newItem) {
        ContentResolver cr = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(ToDoContentProvider.KEY_TASK, newItem);

        cr.insert(ToDoContentProvider.CONTENT_URI, values);
        getLoaderManager().restartLoader(0, null, this);
    }

    //实现LoaderManager.LoaderCallbacks interface
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this,
                ToDoContentProvider.CONTENT_URI, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int keyTaskIndex = data.getColumnIndexOrThrow(ToDoContentProvider.KEY_TASK);

        todoItems.clear();
        while (data.moveToNext()) {
            ToDoItem newItem = new ToDoItem(data.getString(keyTaskIndex));
            todoItems.add(newItem);
        }
        aa.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
