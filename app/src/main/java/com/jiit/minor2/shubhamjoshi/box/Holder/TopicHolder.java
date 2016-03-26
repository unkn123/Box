package com.jiit.minor2.shubhamjoshi.box.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiit.minor2.shubhamjoshi.box.R;

/**
 * Created by Shubham Joshi on 26-03-2016.
 */
public class TopicHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public TextView mTextView;
    public TopicHolder(View itemView) {
        super(itemView);

        mImageView = (ImageView)itemView.findViewById(R.id.image);
        mTextView =(TextView)itemView.findViewById(R.id.desc);

    }


}
