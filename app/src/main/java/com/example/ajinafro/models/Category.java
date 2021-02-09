package com.example.ajinafro.models;

public class Category {
    private String mCategoryName;
    private int mCategoryImage;

    public Category(String mCategoryName, int mCategoryImage) {
        this.mCategoryName = mCategoryName;
        this.mCategoryImage = mCategoryImage;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public int getCategoryDrawable() {
        return mCategoryImage;
    }
}
