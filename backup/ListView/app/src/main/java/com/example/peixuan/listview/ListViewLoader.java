package com.example.peixuan.listview;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

/**
 * ListActivity包含一个ListView
 * CusorLoader是查询Cusor对象异步任务，不会阻塞主线程。activity需要实现LoaderCallbacks
 */
public class ListViewLoader extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter mAdapter;

    static final String[] PROJECTION = new String[]{
            ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME,
    };

    static final String SELECTION = "((" + ContactsContract.Data.DISPLAY_NAME +
            " NOTNULL) AND (" + ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);
        //adapter为空时显示此view
        getListView().setEmptyView(progressBar);

        String[] formColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {android.R.id.text1};//TextView in simple_list_items1
        //构造空的adapter，更新再onLoadFinished()
        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
                null, formColumns, toViews, 0);
        setListAdapter(mAdapter);

        /**
         * prepare the loader
         * this -> 回调interface
         */
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
    }
}
