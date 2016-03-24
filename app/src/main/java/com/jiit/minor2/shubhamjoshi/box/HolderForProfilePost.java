package com.jiit.minor2.shubhamjoshi.box;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public  class HolderForProfilePost extends RecyclerView.ViewHolder {
    public ImageView post;


    public HolderForProfilePost(View itemView) {
        super(itemView);
         post = (ImageView)itemView.findViewById(R.id.individual_image);

    }
}