package com.example.peixuan.codesnippet8;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by peixuan on 16/7/2.
 */
public class MyContentProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.peixuan.codesnippet8/elements");

    //定义常量区分不同的URI请求
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;

    //构造UriMatcher对象，'elements'匹配所有的item， ‘elements/rowID’匹配一行
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.peixuan.codesnippet8", "elements", ALLROWS);
        uriMatcher.addURI("com.example.peixuan.codesnippet8", "elements/#", SINGLE_ROW);
    }

    //index column name for use in where clause
    public static final String KEY_ID = "_id";

    public static final String KEY_COLUMN_1_NAME = "KEY_COLUMN_1_NAME";


    private MySQLiteOpenHelper mySQLiteOpenHelper;

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    @Override
    public boolean onCreate() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(getContext(),
                MySQLiteOpenHelper.);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
