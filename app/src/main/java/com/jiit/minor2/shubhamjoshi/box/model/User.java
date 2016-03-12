package com.jiit.minor2.shubhamjoshi.box.model;

/**
 * Created by Shubham Joshi on 13-02-2016.
 */
public class User {
    public String email;
    public String username;
    public String dob;
    public String gender;
    public String profileUrl;
    public User(){}

    public User(String email, String username, String dob, String gender,String profileUrl){
        this.email = email;
        this.username = username;
        this.dob = dob;
        this.gender = gender;
        this.profileUrl = profileUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
