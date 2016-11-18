package com.example.peixuan.popupmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

public class PopupMenuActivity extends AppCompatActivity implements
        PopupMenu.OnMenuItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_menu);
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        //MenuInflater menuInflater = popupMenu.getMenuInflater();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pupup_menu, popupMenu.getMenu());
        popupMenu.show();
        //注册PopupMenu 项点击listener
        popupMenu.setOnMenuItemClickListener(this);

       //register PopupMenu dismiss listener
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplication(), "setOnDismissListener", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(getApplication(), "OnMenuItemClick", Toast.LENGTH_LONG).show();
                //do something
                return true;
            case R.id.item2:
                //do something
                return true;
        }
        return false;
    }
}
