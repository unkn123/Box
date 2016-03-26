package com.jiit.minor2.shubhamjoshi.box.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.jiit.minor2.shubhamjoshi.box.Holder.HolderForProfilePost;
import com.jiit.minor2.shubhamjoshi.box.MainActivity;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.Post;
import com.jiit.minor2.shubhamjoshi.box.model.User;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import de.hdodenhof.circleimageview.CircleImageView;
import ooo.oxo.library.widget.PullBackLayout;

public class Profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, PullBackLayout.Callback {

    private String pathPart;
    private String ImageUrl;
    private CircleImageView profile;
    private Toolbar mToolbar;
    private TextView title;
    private String username;
    private TextView mTitle;
    private FirebaseRecyclerAdapter mAdapter;
    private TextView mUsername;
    private AppBarLayout mAppBarLayout;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private boolean firstStateOfAnimation=true;
    final Firebase baseRef = new Firebase(Constants.FIREBASE_URL);

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        setTitle("");
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        PullBackLayout puller = (PullBackLayout) findViewById(R.id.puller);
        puller.setCallback(this);

        setSupportActionBar(mToolbar);
            Intent intent = getIntent();
            pathPart=intent.getExtras().getString("path");
//        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
//        pathPart = sp.getString(Constants.SPEMAIL, "Error");

        Firebase ref = baseRef.child(Constants.USER).child(pathPart);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                ImageUrl = user.getProfileUrl();

//                Log.e("SJSJ", ImageUrl + "  " + user.getEmail());
                Picasso.with(getBaseContext()).load(ImageUrl).resize(100, 100).into(profile);
                ImageView relative = (ImageView) findViewById(R.id.profileBg);
                Picasso.with(getBaseContext()).load(ImageUrl).transform(new Blur(getBaseContext(), 50)).into(relative);
                relative.setAlpha(.5f);
                username = user.getUsername();
                Log.e("SJS", username);
                mUsername.setText(username);
                title.setText(username);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        final RecyclerView recycler = (RecyclerView) findViewById(R.id.postsByUser);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(this,3));

        Firebase mRef = baseRef.child(Constants.POST).child(pathPart);
        mAdapter = new FirebaseRecyclerAdapter<Post,HolderForProfilePost>(Post.class,
                R.layout.images_posted, HolderForProfilePost.class, mRef) {
            @Override
            public void populateViewHolder(HolderForProfilePost holderForProfilePost, Post post, int position) {

                if (firstStateOfAnimation) {
                    firstStateOfAnimation = false;
                    recycler.setTranslationY(610);
                    recycler.setAlpha(0f);
                    recycler.animate()
                            .translationY(0)
                            .setDuration(400)
                            .alpha(1f)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .start();
                }
                if(post.getPostImageUrl().length()>2)
                Picasso.with(Profile.this).load(post.getPostImageUrl()).resize(250,250).into(holderForProfilePost.post);

            }
        };
        recycler.setAdapter(mAdapter);
    }

    private void init() {
        profile = (CircleImageView) findViewById(R.id.fb123);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        title=(TextView)findViewById(R.id.title_toolbar);
        mTitle = (TextView) findViewById(R.id.title_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mUsername = (TextView) findViewById(R.id.username);
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
            baseRef.unauth();
            //For loggin out from facebook
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getBaseContext(), MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }


    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public void onPullStart() {


    }

    @Override
    public void onPull(float v) {

        if (v * 10 > 1.4) {
            finish();
            firstStateOfAnimation=true;
        }
    }

    @Override
    public void onPullCancel() {

    }

    @Override
    public void onPullComplete() {


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
