package com.example.peixuan.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by peixuan on 16/11/20.
 */

public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    private int resource;

    public ToDoItemAdapter(Context context, int resource) {
        super(context, resource);
        this.resource = resource;
    }

    public ToDoItemAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.resource = resource;
    }

    public ToDoItemAdapter(Context context, int resource, ToDoItem[] objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public ToDoItemAdapter(Context context, int resource, int textViewResourceId, ToDoItem[] objects) {
        super(context, resource, textViewResourceId, objects);
        this.resource = resource;
    }

    public ToDoItemAdapter(Context context, int resource, List<ToDoItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public ToDoItemAdapter(Context context, int resource, int textViewResourceId, List<ToDoItem> objects) {
        super(context, resource, textViewResourceId, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout todoView;

        ToDoItem item = getItem(position);

        String task = item.getTask();
        Date created = item.getCreated();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        String date = simpleDateFormat.format(created);

        if (convertView == null) {
            todoView = new RelativeLayout(getContext());
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(resource, todoView, true);
        } else {
            todoView = (RelativeLayout) convertView;
        }

        TextView dateView = (TextView) todoView.findViewById(R.id.rowDate);
        TextView taskView = (TextView) todoView.findViewById(R.id.row);

        dateView.setText(date);
        taskView.setText(task);

        return todoView;
    }
}
