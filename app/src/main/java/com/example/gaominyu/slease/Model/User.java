package com.example.gaominyu.slease.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String displayName;
    public String description;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}