package com.example.peixuan.myfirstapp;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MyActivity extends AppCompatActivity {
    public final static String EXTAR_MESSAGE = "com.example.peixuan.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView()is not used. we user the root android.R.id.content as the container for each fragment
        setContentView(R.layout.activity_main);
        //hide actionbar
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();
        /*
        //setup action bar for tabs
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        ActionBar.Tab tab = actionBar.newTab().setText(R.string.artist)
                .setTabListener(new TabListener<ArtistFragment>(this, "artist", ArtistFragment.class));
        actionBar.addTab(tab);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Log.d("seach icon pressed", "pressed");
                //openSearch();
                return true;
            case R.id.action_settings:
                //openSearch();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTAR_MESSAGE, message);
        startActivity(intent);
    }
/* action bar tabs
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
           if (mFragment == null) {
               mFragment = Fragment.instantiate(mActivity, mClass.getName());
               ft.add(android.R.id.content, mFragment, mTag);
           } else {
               ft.attach(mFragment);
           }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            //user selected the already selected tab. usually do nothing
        }
    }
    */
}
