package com.jiit.minor2.shubhamjoshi.box.model.PostModels;

/**
 * Created by Shubham Joshi on 19-03-2016.
 */
public class Post {
    private String timeStamp;
    private String body;
    private String title;

    public Post() {
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Post(String timeStamp, String body, String title) {
        this.timeStamp = timeStamp;
        this.body = body;
        this.title = title;
    }

}
