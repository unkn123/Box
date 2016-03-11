package com.jiit.minor2.shubhamjoshi.box;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

/**
 * Created by Shubham Joshi on 14-02-2016.
 */
public class engine extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Picasso p = new Picasso.Builder(this)
                .memoryCache(new LruCache(24000))
                .build();
    }
}
