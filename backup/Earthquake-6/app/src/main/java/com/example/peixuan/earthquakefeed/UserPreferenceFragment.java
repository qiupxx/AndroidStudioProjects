package com.example.peixuan.earthquakefeed;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by peixuan on 16/7/17.
 */
public class UserPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.userpreferences);
    }
}
