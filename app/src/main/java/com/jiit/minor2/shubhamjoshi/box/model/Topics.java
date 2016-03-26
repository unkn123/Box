package com.jiit.minor2.shubhamjoshi.box.model;

/**
 * Created by Shubham Joshi on 26-03-2016.
 */
public class Topics {
    public String description;
    public String url;
    public String subDescription;
    public Topics()
    {}

    public Topics(String description,String url,String subDescription)
    {
        this.description=description;
        this.url=url;
        this.subDescription=subDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getSubDescription() {
        return subDescription;
    }

    public void setSubDescription(String subDescription) {
        this.subDescription = subDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
