package com.example.peixuan.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });

        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //插入第一条数据
                ContentValues values = new ContentValues();
                values.put("name", "the da vinci code");
                values.put("author", "dan brown");
                values.put("price", 16.96);
                values.put("pages", 454);
                db.insert("book", null, values);
                //插入第二条数据
                ContentValues values2 = new ContentValues();
                values.put("name", "great hall");
                values.put("author", "brows");
                values.put("price", 24.2);
                values.put("pages", 560);
                db.insert("book", null, values);
            }
        });

        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("price", 15.12);
                db.update("book", contentValues, "name=?", new String[]{"the da vinci code"});
            }
        });

        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("book", "pages>?", new String[]{"500"});
            }
        });

        Button query_data = (Button) findViewById(R.id.query_data);
        query_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("book", new String[]{"name", "author"},
                        "name=?", new String[]{"the da vinci code"}, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        Toast.makeText(MainActivity.this, "name=" + name + " author=" + author, Toast.LENGTH_SHORT).show();
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }
        });

        Button replaceData = (Button) findViewById(R.id.replace_data);
        replaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.beginTransaction();
                try {
                    db.delete("book", null, null);
                    if (true) {
                        //手动抛出异常，让事物失败
                        throw new NullPointerException();
                    }
                    ContentValues values = new ContentValues();
                    values.put("name", "game of thrones");
                    values.put("author", "george martin");
                    values.put("pages", 720);
                    values.put("price", 20.85);
                    db.insert("book", null, values);
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });
    }
}
