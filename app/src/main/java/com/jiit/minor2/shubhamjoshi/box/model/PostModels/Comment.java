package com.jiit.minor2.shubhamjoshi.box.model.PostModels;

/**
 * Created by Shubham Joshi on 01-05-2016.
 */
public class Comment {
    public  String comment;
    public String username;
    public String image;

    public Comment(){

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

    public Comment(String comment,String username,String image)
    {
        this.comment = comment;
        this.username=username;
        this.image = image;
    }
}
