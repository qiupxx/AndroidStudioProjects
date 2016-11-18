package com.example.peixuan.databasetest;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by peixuan on 16/8/10.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "CREATE TABLE book ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "author TEXT,"
            + "price REAL,"
            + "pages INTEGER,"
            + "name TEXT,"
            + "category_id INTEGER);";

    public static final String CREATE_CATEGORY = "CREATE TABLE category ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "category_name TEXT,"
            + "category_code INTEGER);";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //注意：switch 语句没有break，确保每层更新版本都能覆盖到
        switch (oldVersion) {
            case 1:
                db.execSQL(CREATE_CATEGORY);
            case 2:
                db.execSQL("ALTER TABLE book ADD COLUMN category_id INTEGER");
            default:
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
