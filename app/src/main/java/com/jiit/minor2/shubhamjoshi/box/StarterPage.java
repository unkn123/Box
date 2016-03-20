package com.jiit.minor2.shubhamjoshi.box;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.jiit.minor2.shubhamjoshi.box.AddingPost.PostAdding;
import com.jiit.minor2.shubhamjoshi.box.model.GalleryModel;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.Post;
import com.jiit.minor2.shubhamjoshi.box.profile.Profile;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.jiit.minor2.shubhamjoshi.box.utils.Utils;

import java.util.Date;
import java.util.List;


public class StarterPage extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    private LinearLayout nav;
    private LinearLayout profileNav;
    private FloatingActionButton fab;
    FirebaseRecyclerAdapter mAdapter;
    private String pathPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter_page);
        init();
        setSupportActionBar(mToolbar);
        setTitle("Home");
        RecyclerView recycler = (RecyclerView) findViewById(R.id.rView);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");
        // Log.e("SJSj", pathPart);
        Firebase mRef = new Firebase(Constants.FIREBASE_URL).child("posts").child(pathPart);
        mAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(Post.class, R.layout.home_post, PostHolder.class, mRef) {
            @Override
            public void populateViewHolder(PostHolder postHolder, Post post, int position) {
                postHolder.postBody.setText(post.getTitle().toString());
                postHolder.postHead.setText(post.getBody().toString());
                if (post.getTimestampLastChanged() != null) {
                    postHolder.timeStamp.setText(
                            Utils.SIMPLE_DATE_FORMAT.format(
                                    new Date(post.getTimestampLastChangedLong())));
                } else {
                    postHolder.timeStamp.setText("");
                }


            }
        };
        recycler.setAdapter(mAdapter);
        profileNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PostAdding.class);
                startActivity(intent);

            }
        });

    }


    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rView);
        nav = (LinearLayout) findViewById(R.id.navigation);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        profileNav = (LinearLayout) findViewById(R.id.profileNav);
        fab = (FloatingActionButton) findViewById(R.id.fabButton);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.e("SJ", verticalOffset + "");
    }


    private List<GalleryModel> persons;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            //For loggin out from facebook
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
