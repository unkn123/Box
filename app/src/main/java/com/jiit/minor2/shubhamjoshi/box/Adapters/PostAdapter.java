package com.jiit.minor2.shubhamjoshi.box.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.GalleryModel;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PersonViewHolder>{

    public List<GalleryModel> posts;

    public PostAdapter(List<GalleryModel> persons){
        this.posts = persons;
    }
    @Override
    public PostAdapter.PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item,parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(PostAdapter.PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(posts.get(i).getName());
        personViewHolder.personAge.setText(posts.get(i).getAge());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);

            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

}