package com.jiit.minor2.shubhamjoshi.box.model.PostModels;


import java.util.HashMap;

/**
 * Created by Shubham Joshi on 19-03-2016.
 */
public class CopyPost {

    private String body;
    private HashMap<String, Object> timestampLastChanged;
    private String title;
    private String email;
    private String postImageUrl;
    private HashMap<String, Object> timestampLastChangedReverse;


    public CopyPost() {
    }

    public CopyPost(String body, String title, String email, String postImageUrl,HashMap<String,Object> timestampLastChanged,HashMap<String,Object> timestampLastChangedReverse) {
        this.body = body;

        this.timestampLastChanged = timestampLastChanged;
        this.title = title;
        this.timestampLastChangedReverse =timestampLastChangedReverse;
        this.email = email;
        this.postImageUrl = postImageUrl;

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    public void setTimestampLastChanged(HashMap<String, Object> timestampLastChanged) {
        this.timestampLastChanged = timestampLastChanged;
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

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public HashMap<String, Object> getTimestampLastChangedReverse() {
        return timestampLastChangedReverse;
    }

    public void setTimestampLastChangedReverse(HashMap<String, Object> timestampLastChangedReverse) {
        this.timestampLastChangedReverse = timestampLastChangedReverse;
    }
}