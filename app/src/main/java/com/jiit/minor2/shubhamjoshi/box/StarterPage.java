package com.jiit.minor2.shubhamjoshi.box;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.jiit.minor2.shubhamjoshi.box.AddingPost.PostAdding;
import com.jiit.minor2.shubhamjoshi.box.model.GalleryModel;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.Post;
import com.jiit.minor2.shubhamjoshi.box.profile.Profile;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;


public class StarterPage extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private static String LOG_TAG = "RecyclerViewActivity";
    FirebaseRecyclerAdapter mAdapter;
    private RecyclerView recycler;
    private Toolbar mToolbar;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout nav;
    private LinearLayout profileNav;
    private FloatingActionButton fab;
    private String pathPart;
    private boolean firstStateOfAnimation = true;
    private List<GalleryModel> persons;

    public static String caluculateTimeAgo(long timeStamp) {
        String intervalType = null;
        double seconds = Math.floor((System.currentTimeMillis() - timeStamp) / 1000);  //get current time in seconds.
        double interval = Math.floor(seconds / 31536000);
        if (interval >= 1) {
            return interval + " y";
        } else {
            interval = Math.floor(seconds / 2592000);
            if (interval >= 1) {
                return interval + " m";
            }
            interval = Math.floor(seconds / 86400);
            if (interval >= 1)
                return interval + " d";
            interval = Math.floor(seconds / 3600);
            if (interval >= 1)
                return interval + " h";
            interval = Math.floor(seconds / 60);
            if (interval >= 1)
                return interval + " m";
            double t = Math.floor(seconds);
            return t + " s";
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        recycler.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter_page);
        init();
        setSupportActionBar(mToolbar);
        setTitle("Home");
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");
        // Log.e("SJSj", pathPart);
        Query mRef = new Firebase(Constants.FIREBASE_URL).child("allPosts").orderByChild("timestampLastChangedReverse/timestamp");
        final Firebase photoRef = new Firebase(Constants.FIREBASE_URL).child("user");


        mAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(Post.class, R.layout.home_post, PostHolder.class, mRef) {

            @Override
            public void onBindViewHolder(PostHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);


            }

            @Override
            public void populateViewHolder(final PostHolder postHolder, Post post, int position) {

                if (firstStateOfAnimation) {
                    firstStateOfAnimation = false;
                    recycler.setTranslationY(510);
                    recycler.setAlpha(0f);
                    recycler.animate()
                            .translationY(0)
                            .setDuration(400)
                            .alpha(1f)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .start();
                }
                postHolder.postBody.setText(post.getTitle().toString());
                postHolder.postHead.setText(post.getBody().toString());

                if (post.getPostImageUrl().toString().length() >= 1) {
                    postHolder.postImage.setVisibility(View.VISIBLE);
                    postHolder.mainHolder.setVisibility(View.VISIBLE);

                    Picasso.with(getBaseContext())
                            .load(post.getPostImageUrl().toString()).fit()
                            .into(postHolder.postImage);

                    Picasso.with(getBaseContext()).load(post.getPostImageUrl().toString())
                            .transform(new Blur(getBaseContext(), 50)).fit().into(postHolder.mainHolder);
                    postHolder.mainHolder.setAlpha(.6f);
                } else {
                    postHolder.postImage.setVisibility(View.GONE);
                    postHolder.mainHolder.setVisibility(View.GONE);

                }


                Firebase photoEmailRef = photoRef.child(post.getEmail().toString()).child(Constants.PROFILE_URL);

                photoEmailRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //Log.e("SJSJ",snapshot.getValue().toString());
                        Picasso.with(getBaseContext()).load(snapshot.getValue().toString()).resize(100, 100).into(postHolder.postOwnerPhoto);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });
                //  Log.e("SJS",photoRef.child(post.getEmail().toString()).g);

                if (post.getTimestampLastChanged() != null) {
                    postHolder.timeStamp.setText(caluculateTimeAgo(post.getTimestampLastChangedLong()));

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
        recycler = (RecyclerView) findViewById(R.id.rView);
        nav = (LinearLayout) findViewById(R.id.navigation);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        profileNav = (LinearLayout) findViewById(R.id.profileNav);
        fab = (FloatingActionButton) findViewById(R.id.fabButton);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.e("SJ", verticalOffset + "");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    private void runEnterAnimation(View view, int position) {


        view.setTranslationY(10);
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    public class Blur implements Transformation {
        protected static final int UP_LIMIT = 25;
        protected static final int LOW_LIMIT = 1;
        protected final Context context;
        protected final int blurRadius;


        public Blur(Context context, int radius) {
            this.context = context;

            if (radius < LOW_LIMIT) {
                this.blurRadius = LOW_LIMIT;
            } else if (radius > UP_LIMIT) {
                this.blurRadius = UP_LIMIT;
            } else
                this.blurRadius = radius;
        }


        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap sourceBitmap = source;

            Bitmap blurredBitmap;
            blurredBitmap = source.copy(source.getConfig(), true);

            RenderScript renderScript = RenderScript.create(context);

            Allocation input = Allocation.createFromBitmap(renderScript,
                    sourceBitmap,
                    Allocation.MipmapControl.MIPMAP_FULL,
                    Allocation.USAGE_SCRIPT);


            Allocation output = Allocation.createTyped(renderScript, input.getType());

            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript,
                    Element.U8_4(renderScript));

            script.setInput(input);
            script.setRadius(blurRadius);

            script.forEach(output);
            output.copyTo(blurredBitmap);

            source.recycle();
            return blurredBitmap;
        }

        @Override
        public String key() {
            return "blurred";
        }
    }
}
