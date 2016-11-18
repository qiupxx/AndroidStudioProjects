package com.example.peixuan.earthquakefeed;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import static android.content.SharedPreferences.*;
//自定义PrefenceActivity实现
public class PreferencesActivity extends Activity {
    CheckBox autoUpdate;
    Spinner updateFreq;
    Spinner updateMag;

    //定义SharedPreferences 键值
    public static final String USER_PREFERENCE = "USER_PREFERENCE";
    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_MIN_MAG_INDEX = "PREF_MIN_MAG_INDEX";
    public static final String PREF_UPDATE_FREQ_INDEX = "PREF_UPDATE_FREQ_INDEX";

    //使用默认的SharedPreferences
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        autoUpdate = (CheckBox) findViewById(R.id.checkBox_auto_update);
        updateFreq = (Spinner) findViewById(R.id.spinner_update_freq);
        updateMag = (Spinner) findViewById(R.id.spinner_quake_mag);

        //设置更新频率与地震等级spinner, 关联ArrayAdapter
        populateSpinners();

        //获取默认的SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //更新PreferenceActivity UI
        updateUIFromPreferences();

        //两个Button设置事件listener
        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
                PreferencesActivity.this.setResult(RESULT_OK);
                finish();
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferencesActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    //设置更新频率和地震等级Spinner数据来源
    private void populateSpinners() {
        //更新频率
        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(this,
                R.array.update_freq_options, android.R.layout.simple_spinner_item);
        freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateFreq.setAdapter(freqAdapter);

        //更新地震等级
        ArrayAdapter<CharSequence> magAdapter = ArrayAdapter.createFromResource(this,
                R.array.magnitude_options, android.R.layout.simple_spinner_item);
        magAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateMag.setAdapter(magAdapter);

    }

    //更新PrefenceActivity UI
    private void updateUIFromPreferences() {
        boolean autoUpChecked = prefs.getBoolean(PREF_AUTO_UPDATE, false);
        int updateFreqIndex = prefs.getInt(PREF_UPDATE_FREQ_INDEX, 2);
        int minMagIndex = prefs.getInt(PREF_MIN_MAG_INDEX, 0);

        updateFreq.setSelection(updateFreqIndex);
        updateMag.setSelection(minMagIndex);
        autoUpdate.setChecked(autoUpChecked);
    }

    private void savePreferences(){
        int updateIndex = updateFreq.getSelectedItemPosition();
        int minMagIndex = updateMag.getSelectedItemPosition();
        boolean autoUpateChecked = autoUpdate.isChecked();

        Editor editor = prefs.edit();
        editor.putBoolean(PREF_AUTO_UPDATE, autoUpateChecked);
        editor.putInt(PREF_UPDATE_FREQ_INDEX, updateIndex);
        editor.putInt(PREF_MIN_MAG_INDEX, minMagIndex);
        editor.commit();
        return;
    }
}
