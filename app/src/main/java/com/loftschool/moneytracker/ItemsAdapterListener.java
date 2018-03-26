package com.loftschool.moneytracker;

public interface ItemsAdapterListener {
    void onItemClick(Item item, int position);
    void onItemLongClick(Item item, int position);
}
