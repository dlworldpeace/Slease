package com.example.gaominyu.slease.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Item {
    public String title;
    public String description;
    public int categoryID;
    public String deposit;
    public String rate;
    public String interval;
    public boolean allowCash;
    public boolean allowTransfer;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Item.class)
    public Item() {
    }

    public Item(String title, String description, int categoryID, String deposit,
                String rate, String interval, boolean allowCash, boolean allowTransfer){
        this.title = title;
        this.description = description;
        this.categoryID = categoryID;
        this.deposit = deposit;
        this.rate = rate;
        this.interval = interval;
        this.allowCash = allowCash;
        this.allowTransfer = allowTransfer;
    }

}
