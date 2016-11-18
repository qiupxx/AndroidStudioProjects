package com.example.peixuan.contactpicker;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactPicker extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        String[] from = new String[] {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
        int[] to = new int[] {R.id.itemTextView};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.listitemlayout,
                c, from, to);

        ListView lv = (ListView) findViewById(R.id.contactListView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //move the cursor to the selected item
                c.moveToPosition(position);
                //extract the row id
                int rowId = c.getInt(c.getColumnIndexOrThrow("_id"));
                //construct the result URI
                Uri outURI = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, rowId);
                Intent outData = new Intent();
                outData.setData(outURI);
                setResult(Activity.RESULT_OK, outData);
                finish();
            }
        });
    }
}
