package com.jiit.minor2.shubhamjoshi.box;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PostHolder extends RecyclerView.ViewHolder {

    TextView postHead;
    TextView postBody;
    TextView timeStamp;
    ImageView postOwnerPhoto;

    public PostHolder(View itemView) {
        super(itemView);

        postHead = (TextView) itemView.findViewById(R.id.head);
        postBody = (TextView) itemView.findViewById(R.id.body);
        timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
        postOwnerPhoto = (ImageView) itemView.findViewById(R.id.post_owner);
    }
}