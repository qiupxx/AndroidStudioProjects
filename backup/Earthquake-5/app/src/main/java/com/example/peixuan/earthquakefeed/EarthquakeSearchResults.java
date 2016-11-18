package com.example.peixuan.earthquakefeed;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

/**
 * Created by peixuan on 16/7/24.
 */
public class EarthquakeSearchResults extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                new String[] { EarthquakeProvider.KEY_SUMMARY },
                new int[] { android.R.id.text1}, 0);
        setListAdapter(adapter);

        //初始化cursor loader
        getLoaderManager().initLoader(0, null, this);

        //get the launch intent
        pareseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        pareseIntent(getIntent());
    }

    private static String QUERY_EXTAR_KEY = "QUERY_EXTAR_KEY";
    private void pareseIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            Bundle args = new Bundle();
            args.putString(QUERY_EXTAR_KEY, searchQuery);
            getLoaderManager().restartLoader(0, args, this);
        }
    }

    //LoaderCallbacks
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String query = "0";

        if (args != null) {
            query = args.getString(QUERY_EXTAR_KEY);
        }
        String[] projection = { EarthquakeProvider.KEY_ID, EarthquakeProvider.KEY_SUMMARY};
        String where = EarthquakeProvider.KEY_SUMMARY + " LIKE \"%" + query + "%\"";
        String[] whereArgs = null;
        String sortorder = EarthquakeProvider.KEY_SUMMARY + " COLLATE LOCALIZED ASC";
        return new CursorLoader(this, EarthquakeProvider.CONTENT_URI,
                projection , where, whereArgs, sortorder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
