package com.example.peixuan.earthquakefeed;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

public class FragmentPreferences extends PreferenceActivity {

    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
    public static final String PREF_MIN_MAG = "PREF_MIN_MAG";
    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }
}
