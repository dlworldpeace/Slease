package com.example.gaominyu.slease.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class ItemPreview {

    public String title;
    public int categoryID;
    public String rate;
    public String deposit;
    public String interval;
    public String imageBase64;
    public String userId;
    public String itemId;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Item.class)
    public ItemPreview() {
    }

    public ItemPreview(String title, int categoryID, String rate, String deposit, String interval,
                       String imageBase64, String userId, String itemId){
        this.title = title;
        this.categoryID = categoryID;
        this.rate = rate;
        this.deposit = deposit;
        this.interval = interval;
        this.imageBase64 = imageBase64;
        this.userId = userId;
        this.itemId = itemId;
    }

}
