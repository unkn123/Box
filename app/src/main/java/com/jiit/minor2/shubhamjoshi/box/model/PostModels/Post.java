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

    public Post(String timeStamp, String body, String title) {
        this.timeStamp = timeStamp;
        this.body = body;
        this.title = title;
    }

}
