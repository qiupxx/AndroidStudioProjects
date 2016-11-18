package com.example.peixuan.providetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private String newId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加数据
                Uri uri = Uri.parse("content://com.example.peixuan.databasetest/book");
                ContentValues values = new ContentValues();
                values.put("name", "a clash of kings");
                values.put("author", "george martin");
                values.put("pages", 1050);
                values.put("price", 22.85);
                Uri newUri = getContentResolver().insert(uri, values);
                newId = newUri.getPathSegments().get(1);
            }
        });

        Button queryData = (Button) findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询数据
                Uri uri = Uri.parse("content://com.example.peixuan.databasetest/book");
                Cursor cursor = getContentResolver().query(uri, null, null, null,null,null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Toast.makeText(MainActivity.this, name + author + pages + price, Toast.LENGTH_SHORT).show();
                    }
                }
                //一定要关闭Cursor
                cursor.close();
            }
        });

        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新数据
                Uri uri = Uri.parse("content://com.example.peixuan.databasetest/book");
                ContentValues values = new ContentValues();
                values.put("name", "a storm of swords");
                values.put("pages", 1050);
                getContentResolver().update(uri, values, null, null);
            }
        });

        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //删除数据
                Uri uri = Uri.parse("content://com.example.peixuan.databasetest/book");
                getContentResolver().delete(uri, null, null);
            }
        });
    }
}
