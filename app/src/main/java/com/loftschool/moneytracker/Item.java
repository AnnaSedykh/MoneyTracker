package com.loftschool.moneytracker;

import com.google.gson.annotations.SerializedName;

public class Item {

    public static final String TYPE_INCOMES = "0";
    public static final String TYPE_EXPENSES = "1";
    public static final String TYPE_BALANCE = "2";
    public static final String TYPE_UNKNOWN = "-1";

    public int id;
    @SerializedName("name")
    public String title;
    public int price;
    public String type;

    public Item(String title, int price, String type) {
        this.title = title;
        this.price = price;
        this.type = type;
    }
}
