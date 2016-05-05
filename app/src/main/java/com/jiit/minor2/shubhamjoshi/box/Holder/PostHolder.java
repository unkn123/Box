package com.jiit.minor2.shubhamjoshi.box.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiit.minor2.shubhamjoshi.box.R;

public class PostHolder extends RecyclerView.ViewHolder {

   public TextView postHead;
   public TextView postBody;
   public TextView timeStamp;
   public TextView postOwnerName;
   public ImageView postOwnerPhoto;
    public TextView commentCount;
    public ImageView like;
   public ImageView postImage;
    public ImageView comments;
    public TextView likeCount;
   public ImageView mainHolder;

    public PostHolder(View itemView) {
        super(itemView);

        postHead = (TextView) itemView.findViewById(R.id.head);
        postBody = (TextView) itemView.findViewById(R.id.body);
        timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
        commentCount = (TextView)itemView.findViewById(R.id.commentCount);
        like = (ImageView)itemView.findViewById(R.id.like);
        likeCount = (TextView)itemView.findViewById(R.id.likecount);
        postOwnerPhoto = (ImageView) itemView.findViewById(R.id.post_owner);
        postImage = (ImageView)itemView.findViewById(R.id.postImage);
        comments = (ImageView)itemView.findViewById(R.id.imageView2);
        postOwnerName=(TextView)itemView.findViewById(R.id.profileName);
        mainHolder=(ImageView)itemView.findViewById(R.id.bgRecView);
    }


}