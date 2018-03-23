package com.loftschool.moneytracker;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable{

    public static final String TYPE_INCOMES = "0";
    public static final String TYPE_EXPENSES = "1";
    public static final String TYPE_BALANCE = "2";
    public static final String TYPE_UNKNOWN = "-1";

    public int id;
    @SerializedName("name")
    public String title;
    public String price;
    public String type;

    public Item(String title, String price, String type) {
        this.title = title;
        this.price = price;
        this.type = type;
    }

    protected Item(Parcel in) {
        id = in.readInt();
        title = in.readString();
        price = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(price);
        parcel.writeString(type);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
