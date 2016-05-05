package com.jiit.minor2.shubhamjoshi.box.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiit.minor2.shubhamjoshi.box.R;

/**
 * Created by Shubham Joshi on 06-05-2016.
 */

public class CommentsHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView username;
    public TextView comment;

    public CommentsHolder(View itemView) {
        super(itemView);

        image = (ImageView) itemView.findViewById(R.id.ownerI);
        username = (TextView) itemView.findViewById(R.id.usernameI);
        comment =(TextView)itemView.findViewById(R.id.commentI);
    }

}
