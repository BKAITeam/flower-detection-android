package com.bkai.flowerdetect_android.models;

/**
 * Created by marsch on 4/14/17.
 */

public class Flower {
    String name;
    String description;

    public Flower(String name, String description){
        this.name  = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
