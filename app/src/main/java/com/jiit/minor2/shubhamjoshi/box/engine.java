package com.jiit.minor2.shubhamjoshi.box;

import android.app.Application;

import com.firebase.client.Firebase;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

/**
 * Created by Shubham Joshi on 14-02-2016.
 */
public class engine extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
