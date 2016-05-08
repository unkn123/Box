package com.jiit.minor2.shubhamjoshi.box.model.PostModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

import java.util.HashMap;

/**
 * Created by Shubham Joshi on 01-05-2016.
 */
public class Comment {
    public String comment;
    public String username;
    public String image;
    private HashMap<String, Object> timestampLastChanged;

    public Comment() {

    }

    public Comment(String comment, String username, String image) {
        this.comment = comment;
        this.username = username;
        this.image = image;
        HashMap<String, Object> timestampLastChangedObj = new HashMap<String, Object>();
        timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampLastChangedObj;


    }


    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }


    @JsonIgnore
    public long getTimestampLastChangedLong() {

        return (long) timestampLastChanged.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }
}
