package com.example.peixuan.todolist;

import java.util.Date;

/**
 * Created by peixuan on 16/11/19.
 */

public class ToDoItem {
    private String task;
    private Date created;

    public String getTask() {
        return task;
    }

    public Date getCreated() {
        return created;
    }

    public ToDoItem(String _task) {
        this(_task, new Date(java.lang.System.currentTimeMillis()));
    }

    public ToDoItem(String _task, Date _created) {
        task = _task;
        created = _created;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "task='" + task + '\'' +
                ", created=" + created +
                '}';
    }
}
