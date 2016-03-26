package com.jiit.minor2.shubhamjoshi.box.Steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.jiit.minor2.shubhamjoshi.box.Holder.TopicHolder;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.Steps.TopicSteps.SubCategory;
import com.jiit.minor2.shubhamjoshi.box.model.Topics;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;

public class Explore extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView recycler;
    private FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        init();
        setSupportActionBar(mToolbar);
        setTitle("Explore");
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getBaseContext(),2));

        Firebase firebase = new Firebase(Constants.FIREBASE_URL);
        Firebase mRef = firebase.child("categories");
        mAdapter = new FirebaseRecyclerAdapter<Topics,TopicHolder>(Topics.class, R.layout.topic_layout, TopicHolder.class, mRef) {
            @Override
            public void populateViewHolder(TopicHolder topicHolder, Topics topic, int position) {

                Picasso.with(getBaseContext()).load(topic.getUrl()).centerCrop().resize(465,465).into(topicHolder.mImageView);
                topicHolder.mTextView.setText(topic.getDescription());
                topicHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(), SubCategory.class));
                    }
                });
            }
        };
        recycler.setAdapter(mAdapter);
    }

    public void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
