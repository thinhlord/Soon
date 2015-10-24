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
    public String address;

    public static Event troll() {
        Event e = new Event();
        e.id = 0;
        e.name = "Bia Saigon Hypersonic Music Festival";
        e.image = "https://media-ticketbox.cdn.vccloud.vn/eventcover/2015/10/13/D74C46.jpg?w=555&maxheight=210&mode=crop&anchor=topcenter";
        e.address = "TP Hồ Chí Minh";
        e.des = "Để đánh dấu bề dày lịch sử 150 năm Bia Sài Gòn hiện diện trên thị trường, và nay là một thương hiệu nổi tiếng của Việt Nam, Hypersonic Music Festival - một đại tiệc âm nhạc quy mô lớn sẽ được tổ chức, nằm trong chuỗi các hoạt động kỷ niệm dấu mốc quan trọng này.";
        e.date = "Thứ 7 ngày 8/9/2010";
        return e;
    }

    public Event(String name, String image, String address, String date, String type, String status, String description) {
        this.name = name;
        this.image = image;
        this.address = address;
        this.date = date;
        this.type = type;
        this.status = status;
        this.des = description;
    }

    public Event() {

    }
}
