package com.jiit.minor2.shubhamjoshi.box.model.list_models;

/**
 * Created by Shubham Joshi on 02-03-2016.
 */
public class Categories {
    private String url;
    private String description;


    public Categories() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

}
