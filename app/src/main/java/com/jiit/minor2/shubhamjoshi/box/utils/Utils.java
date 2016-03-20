package com.jiit.minor2.shubhamjoshi.box.utils;

import android.content.Context;

import java.text.SimpleDateFormat;

public class Utils {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Context mContext = null;

    public Utils(Context con) {
        mContext = con;
    }

}