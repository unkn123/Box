package com.jiit.minor2.shubhamjoshi.box.utils;

import com.jiit.minor2.shubhamjoshi.box.BuildConfig;

/**
 * Created by Shubham Joshi on 13-02-2016.
 */
public final class Constants {
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String USER = "user";
    public static final String BIRTHDAY = "user_birthday";
    public static final String PUBLIC_PROFILE = "public_profile";
    public static final String USER_PHOTO = "user_photos";
    public static final String FIREBASE_CATEGORIES="categories";
    public static final String EMAIL = "email";
    public static final String SPEMAIL="EmailSP";
    public static final String SHAREDPREF_EMAIL="emailSharedPreff";

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}
