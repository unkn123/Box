package com.jiit.minor2.shubhamjoshi.box.model.list_models;

/**
 * Created by Shubham Joshi on 02-03-2016.
 */
public class GiantChooserModel {
    private String url;
    private String description;
    private boolean selected;


    public GiantChooserModel() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
