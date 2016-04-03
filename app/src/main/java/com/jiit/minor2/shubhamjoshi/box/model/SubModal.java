package com.jiit.minor2.shubhamjoshi.box.model;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Shubham Joshi on 02-04-2016.
 */
public class SubModal implements Serializable {
    public String mImageView;
    public String mHeader;
    public int count;
    public String mRating;

    public SubModal(String imageView, String head, String rating,int count) {
        this.mImageView = imageView;
        this.mHeader = head;
        this.mRating = rating;
        this.count = count;
    }
}
