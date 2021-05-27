package com.codershil.algorithmvisualizer.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class User {
    String name, email, mobile,uid;

    @ServerTimestamp
    private Date createdAt;

    public User(String name, String email, String mobile,String uid) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.uid = uid;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
