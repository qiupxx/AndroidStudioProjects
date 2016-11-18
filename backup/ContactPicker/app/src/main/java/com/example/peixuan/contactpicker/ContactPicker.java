package com.example.peixuan.contactpicker;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

public class ContactPicker extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{
    private SimpleCursorAdapter mAdapter;

    static final String[] PROJECTION = new String[] {ContactsContract.Data._ID,
        ContactsContract.Data.DISPLAY_NAME};

    static final String SELECTION = "((" + ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    private Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        //must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        String[] from = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
        int[] to = new int[] {R.id.itemTextView};

        mAdapter = new SimpleCursorAdapter(this, R.layout.listitemlayout,
                null, from, to, 0);
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    //called when a new loader needs to be created
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //now create and return a CursorLoader that will take care of
        //creating a Cursor for the data being displayed
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    //called when a previously created loader has finished loading
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //swap the new cursor in
        //the framework will take care of closing the old cursor once we return
        mAdapter.swapCursor(data);
    }

    //called when a previously created loader is reset, making data unavailable
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        c.moveToPosition(position);
        int rowId = c.getInt(c.getColumnIndexOrThrow("_id"));
        Uri outUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                rowId);
        Intent outData = new Intent();
        outData.setData(outUri);
        setResult(Activity.RESULT_OK, outData);
        finish();
    }
}
