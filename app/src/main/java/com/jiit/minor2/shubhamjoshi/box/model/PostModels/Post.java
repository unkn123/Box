package com.jiit.minor2.shubhamjoshi.box.model.PostModels;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Shubham Joshi on 19-03-2016.
 */
public class Post {

    private String body;
    private HashMap<String, Object> timestampLastChanged;
    private String title;
    private String email;
    private String postImageUrl;
    private HashMap<String, Object> timestampLastChangedReverse;


    public Post() {
    }

    public Post(String body, String title, String email, String postImageUrl) {
        this.body = body;
        HashMap<String, Object> timestampLastChangedObj = new HashMap<String, Object>();

        timestampLastChangedObj.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampLastChangedObj;
        this.title = title;
        this.timestampLastChangedReverse =timestampLastChanged;
        this.email = email;
        this.postImageUrl = postImageUrl;

    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public void setTimestampLastChanged(HashMap<String, Object> timestampLastChanged) {
        this.timestampLastChanged = timestampLastChanged;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }


    @JsonIgnore
    public long getTimestampLastChangedLong() {

        return (long) timestampLastChanged.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }

    public HashMap<String, Object> getTimestampLastChangedReverse() {
        return timestampLastChangedReverse;
    }

    @JsonIgnore
    public long getTimestampLastChangedReverseLong() {

        return (long) timestampLastChangedReverse.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }
}