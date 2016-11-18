package com.example.peixuan.actionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView text;
    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        //getActionBar().hide();
        //getActionBar().show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_action, menu);

        MenuItem searchItem = menu.findItem(R.id.ic_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //set on expand listener
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //do something whe expand
                return true;//return true to expand action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //do something whe expand
                return true;//return true to collapse action view
            }
        });
        //ShareActionProvider
        MenuItem shareItem = menu.findItem(R.id.ic_share);
        mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        mShareActionProvider.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_search:
                openSearch();
                return true;
            case R.id.ic_compose:
                sendCompose();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearch() {
       text.setText("search");
        return;
    }

    private void sendCompose() {
        text.setText("compose");
    }

    public void startSecondActivity(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        return;
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        return intent;
    }
}
