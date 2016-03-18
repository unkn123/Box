package com.jiit.minor2.shubhamjoshi.box;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
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
import com.jiit.minor2.shubhamjoshi.box.Adapters.PostAdapter;
import com.jiit.minor2.shubhamjoshi.box.model.GalleryModel;
import com.jiit.minor2.shubhamjoshi.box.profile.Profile;

import java.util.ArrayList;
import java.util.List;


public class StarterPage extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    private LinearLayout nav;
    private LinearLayout profileNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter_page);
        init();
        setSupportActionBar(mToolbar);
        setTitle("Home");
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<GalleryModel> l = new ArrayList<>();
        initializeData();
        PostAdapter postAdapter = new PostAdapter(persons);
        mRecyclerView.setAdapter(postAdapter);

        profileNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up );

            }
        });

    }


    private void init() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rView);
        nav = (LinearLayout) findViewById(R.id.navigation);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        profileNav = (LinearLayout)findViewById(R.id.profileNav);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.e("SJ", verticalOffset + "");
    }


    private List<GalleryModel> persons;

    private void initializeData() {
        persons = new ArrayList<>();
        persons.add(new GalleryModel("Emma Wilson", "23 years old"));
        persons.add(new GalleryModel("Lavery Maiss", "25 years old"));
        persons.add(new GalleryModel("Lillie Watts", "35 years old"));
        persons.add(new GalleryModel("Emma Wilson", "23 years old"));
        persons.add(new GalleryModel("Lavery Maiss", "25 years old"));
        persons.add(new GalleryModel("Lillie Watts", "35 years old"));
        persons.add(new GalleryModel("Emma Wilson", "23 years old"));
        persons.add(new GalleryModel("Lavery Maiss", "25 years old"));
        persons.add(new GalleryModel("Lillie Watts", "35 years old"));
    }

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

}
