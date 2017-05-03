package com.bkai.flowerdetect.models;

import java.io.Serializable;

/**
 * Created by marsch on 4/14/17.
 */

public class Flower implements Serializable {
    String name;
    String scienceName;
    String engName;
    String description;
    String cotich;
    int id;

    public Flower(int id, String name, String scienceName, String description){
        this.id = id;
        this.name  = name;
        this.scienceName = scienceName;
        this.description = description;
    }

    public Flower(int id, String name, String scienceName, String description, String engName){
        this.id = id;
        this.name  = name;
        this.scienceName = scienceName;
        this.engName = engName;
        this.description = description;
    }

    public Flower(int id, String name, String scienceName, String description, String engName, String cotich){
        this.id = id;
        this.name  = name;
        this.scienceName = scienceName;
        this.engName = engName;
        this.description = description;
        this.cotich = cotich;
    }

    public Flower(String name, String scienceName, String description){
        this.name  = name;
        this.scienceName = scienceName;
        this.description = description;
    }

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

    public String getScienceName() {
        return scienceName;
    }

    public void setScienceName(String scienceName) {
        this.scienceName = scienceName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getCotich() {
        return cotich;
    }

    public void setCotich(String cotich) {
        this.cotich = cotich;
    }
}
