package com.jiit.minor2.shubhamjoshi.box.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiit.minor2.shubhamjoshi.box.R;

public class FriendsHolder extends RecyclerView.ViewHolder {
    public ImageView photo;
    public TextView name;

    public FriendsHolder(View itemView) {
        super(itemView);

        photo = (ImageView) itemView.findViewById(R.id.image);
        name = (TextView) itemView.findViewById(R.id.desc);

    }
}