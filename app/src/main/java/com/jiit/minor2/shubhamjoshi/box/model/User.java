package com.jiit.minor2.shubhamjoshi.box.model;

/**
 * Created by Shubham Joshi on 13-02-2016.
 */
public class User {
    private String email;
    private String username;
    private String dob;
    private String gender;

    public User(){}

    public User(String email, String username, String dob, String gender){
        this.email = email;
        this.username = username;
        this.dob = dob;
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }
}
