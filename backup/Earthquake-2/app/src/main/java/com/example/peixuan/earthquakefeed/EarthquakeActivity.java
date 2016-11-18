package com.example.peixuan.earthquakefeed;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class EarthquakeActivity extends Activity {
    private static final int MENU_PREFERENCES = Menu.FIRST + 1;
    private static final int MENU_UPDATE = Menu.FIRST + 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        updateFromPreferences();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_PREFERENCES, Menu.NONE, R.string.menu_preferences);
        return true;
    }

    //request code
    private static final int SHOW_PREFERENCES = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case MENU_PREFERENCES:
                Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                        PreferencesActivity.class : FragmentPreferences.class;
                Intent intent = new Intent(this, c);
                startActivityForResult(intent, SHOW_PREFERENCES);
                return true;
        }
        return false;
    }

    //startActivityForResult()返回后的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHOW_PREFERENCES)
            updateFromPreferences();

        FragmentManager fm = getFragmentManager();
        final EarthquakeListFragment earthquakeListFragment =
                (EarthquakeListFragment) fm.findFragmentById(R.id.EearthquakeListFragment);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                earthquakeListFragment.refreshEarthquakes();
            }
        });
        t.start();
    }

    //获取SharedPreferences保存的值
    public int minimumMagnitude = 0;
    public boolean autoUpdateChecked = false;
    public int updateFreq = 0;

    private void updateFromPreferences() {
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        minimumMagnitude = Integer.parseInt(prefs.getString(PreferencesActivity.PREF_MIN_MAG, "3"));
        updateFreq = Integer.parseInt(prefs.getString(PreferencesActivity.PREF_UPDATE_FREQ, "60"));
        autoUpdateChecked = prefs.getBoolean(PreferencesActivity.PREF_AUTO_UPDATE, false);

    }

}
