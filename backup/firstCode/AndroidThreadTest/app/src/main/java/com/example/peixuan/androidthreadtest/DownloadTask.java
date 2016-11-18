package com.example.peixuan.androidthreadtest;

import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by peixuan on 16/8/14.
 */
public class DownloadTask extends AsyncTask<Void, Integer, Boolean>{
    @Override
    protected Boolean doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
