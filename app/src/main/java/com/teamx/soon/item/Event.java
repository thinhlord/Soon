package com.teamx.soon.item;

import java.io.Serializable;

/**
 * Created by ruler_000 on 24/10/2015.
 * Project: Soon
 */
public class Event implements Serializable {
    public int id;
    public String name;
    public String image;
    public String des;
    public String date;
    public String type;
    public String status;
    public String description;

    public Event(String name, String image, String address, String date, String type, String status, String description) {
        this.name = name;
        this.image = image;
        this.des = address;
        this.date = date;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public Event() {

    }
}
