package com.codershil.algorithmvisualizer.models;

public class Category {
    int imageUrl;
    String categoryName;
    public Category(int imageUrl,String categoryName){
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
