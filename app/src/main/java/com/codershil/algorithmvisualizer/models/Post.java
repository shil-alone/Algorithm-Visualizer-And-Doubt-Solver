package com.codershil.algorithmvisualizer.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {
    String userName,postContent,userUid;

    @ServerTimestamp
    private Date createdAt;

    public Post(String userName, String postContent,String userUid) {
        this.userName = userName;
        this.postContent = postContent;
        this.userUid = userUid;
    }
    public Post(){

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
