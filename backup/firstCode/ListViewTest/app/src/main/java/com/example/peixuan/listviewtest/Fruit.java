package com.example.peixuan.listviewtest;

import android.app.FragmentManager;

/**
 * Created by peixuan on 16/8/6.
 */
public class Fruit {
    private String name;
    private int imageId;
    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
