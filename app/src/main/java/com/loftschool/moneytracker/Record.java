package com.loftschool.moneytracker;

/**
 * Created by PC on 14.03.2018.
 */

public class Record {

    private final String title;
    private final int price;
    private String comment;

    public Record(String title, int price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
