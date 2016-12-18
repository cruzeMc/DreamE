package com.events.model;

import com.google.gson.annotations.SerializedName;

/**
 * Create   `AQ `d by Cruze on 12/7/2016.
 */

public class Category {
    @SerializedName("category_name")
    private String categoryName;
    private String image;
    private Integer id;

    public Category(Integer id, String categoryName, String image) {
        this.id = id;
        this.categoryName = categoryName;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
