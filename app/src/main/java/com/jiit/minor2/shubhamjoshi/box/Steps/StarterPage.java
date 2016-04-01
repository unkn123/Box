package com.jiit.minor2.shubhamjoshi.box.Steps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.jiit.minor2.shubhamjoshi.box.AddingPost.PostAdding;
import com.jiit.minor2.shubhamjoshi.box.Holder.PostHolder;
import com.jiit.minor2.shubhamjoshi.box.MainActivity;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.GalleryModel;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.Post;
import com.jiit.minor2.shubhamjoshi.box.profile.Profile;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import twitter4j.MediaEntity;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class StarterPage extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private static final int TYPE_IMAGE = 1;
    private static final int TYPE_GROUP = 2;
    private static String LOG_TAG = "RecyclerViewActivity";
    List<Map<String, String>> twitterList = new ArrayList<Map<String, String>>();
    private FirebaseRecyclerAdapter mAdapter;
    private RecyclerView recycler;
    private Toolbar mToolbar;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout nav;
    private LinearLayout explore;
    private LinearLayout profileNav;
    private ProgressBar mProgressBar;
    private String likeQuery = "";
    private Set<String> likes = new HashSet<>();
    private FloatingActionButton fab;
    private String pathPart;
    private boolean firstStateOfAnimation = true;
    private List<GalleryModel> persons;

    public static String caluculateTimeAgo(long timeStamp) {

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
        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");


        //Getting users interest

        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        Firebase mref = ref.child(Constants.LIKES).child(pathPart);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                HashMap<String, String> a = (HashMap<String, String>) snapshot.getValue();

                likes = a.keySet();


                int count = 0;

                for (String t : likes) {
                    count++;

                    likeQuery += "#";

                    likeQuery += t;

                    if (count != likes.size())
                        likeQuery += " OR ";
                }


                new Fun().execute();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        Log.e("SSJSJ", "CHECKING>>>>>");

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));


        Query mRef = new Firebase(Constants.FIREBASE_URL).child("allPosts").orderByChild(getString(R.string.sorting_order_time_reverse));
        final Firebase photoRef = new Firebase(Constants.FIREBASE_URL).child("user");

        mAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(Post.class, R.layout.home_post, PostHolder.class, mRef) {


            @Override
            public int getItemViewType(int position) {

                return position % 3 == 2 ? TYPE_IMAGE : TYPE_GROUP;
            }


            @Override
            public void populateViewHolder(final PostHolder postHolder, final Post post, final int position) {

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
                    mProgressBar.setVisibility(View.INVISIBLE);

                }


                if (postHolder.getItemViewType() == TYPE_GROUP) {

                    postHolder.postBody.setText(post.getTitle().toString());
                    postHolder.postHead.setText(post.getBody().toString());


                    if (post.getPostImageUrl().toString().length() >= 1) {

                        postHolder.postImage.setVisibility(View.VISIBLE);
                        postHolder.mainHolder.setVisibility(View.VISIBLE);


                        Picasso.with(getBaseContext())
                                .load(post.getPostImageUrl().toString()).fit()
                                .into(postHolder.postImage);

                        Picasso.with(getBaseContext()).load(post.getPostImageUrl().toString())
                                .transform(new Blur(getBaseContext(), 50)).fit().centerCrop().into(postHolder.mainHolder);
                        postHolder.mainHolder.setAlpha(.6f);
                    }


                    Firebase photoEmailRef = photoRef.child(post.getEmail().toString()).child(Constants.PROFILE_URL);

                    photoEmailRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            Picasso.with(getBaseContext()).load(snapshot.getValue().toString()).
                                    resize(100, 100).into(postHolder.postOwnerPhoto);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });

                    Firebase usernameRef = photoRef.child(post.getEmail().toString()).child(Constants.USERNAME);

                    usernameRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            postHolder.postOwnerName.setText(snapshot.getValue().toString());


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            System.out.println("The read failed: " + firebaseError.getMessage());
                        }
                    });
                    postHolder.postOwnerPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callProfileActivity(post.getEmail());
                        }
                    });

                    postHolder.postOwnerName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callProfileActivity(post.getEmail());
                        }
                    });


                    if (post.getTimestampLastChanged() != null) {
                        postHolder.timeStamp.setText(caluculateTimeAgo(post.getTimestampLastChangedLong()));

                    }


                }
                if (postHolder.getItemViewType() == TYPE_IMAGE && twitterList.size() > position) {

                    postHolder.postImage.setVisibility(View.VISIBLE);
                    postHolder.mainHolder.setVisibility(View.VISIBLE);

                    postHolder.postHead.setText(twitterList.get(position).get("Text"));
                    Picasso.with(getBaseContext()).load(twitterList.get(position).get("OwnerImage")).resize(100, 100).into(postHolder.postOwnerPhoto);
                    postHolder.postOwnerName.setText(twitterList.get(position).get("Username"));
                    Picasso.with(getBaseContext()).load(twitterList.get(position).get("ImageUrl")).fit().into(postHolder.postImage);
                    Picasso.with(getBaseContext()).load(twitterList.get(position).get("ImageUrl"))
                            .transform(new Blur(getBaseContext(), 50)).resize(450, 660).into(postHolder.mainHolder);
                    postHolder.postBody.setText("You've shown interest in " + twitterList.get(position).get("Matched"));
                    postHolder.mainHolder.setAlpha(.6f);
                }
                // postHolder.
                //Log.e("SJSJ",twitterPosts.toString());


            }


        };
        recycler.setAdapter(mAdapter);
        profileNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getBaseContext(), Profile.class);
                intent.putExtra("path", pathPart);
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

        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Explore.class));
            }
        });
    }

    private void callProfileActivity(String pathPart) {

        Intent intent = new Intent(getBaseContext(), Profile.class);
        intent.putExtra("path", pathPart);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    private void init() {
        recycler = (RecyclerView) findViewById(R.id.rView);
        nav = (LinearLayout) findViewById(R.id.navigation);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        profileNav = (LinearLayout) findViewById(R.id.profileNav);
        explore = (LinearLayout) findViewById(R.id.
                explore);
        fab = (FloatingActionButton) findViewById(R.id.fabButton);
        mProgressBar=(ProgressBar)findViewById(R.id.chooserProgress);
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


    public class Fun extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... params) {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            //  likeQuery="#fun OR #cool OR #srk OR #shubham OR #india";
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(getString(R.string.token1))
                    .setOAuthConsumerSecret(getString(R.string.token2))
                    .setOAuthAccessToken(getString(R.string.token3))
                    .setOAuthAccessTokenSecret(getString(R.string.token4));

            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
            int count = 0;
            try {
                twitter4j.Query query = new twitter4j.Query();
                if (likeQuery != "")
                    query.setQuery(likeQuery);
                else
                    query.setQuery("#fun");
                query.setCount(100);
                QueryResult result;
                result = twitter.search(query);
                List<twitter4j.Status> tweets = result.getTweets();

                for (twitter4j.Status tweet : tweets) {

                    MediaEntity[] media = tweet.getMediaEntities();
                    String ans = "";
                    for (MediaEntity m : media) {

                        ans = m.getMediaURLHttps();
                    }
                    if (ans != "") {
                        Map<String, String> twitterPosts = new HashMap<>();
                        twitterPosts.put("ImageUrl", ans);
                        twitterPosts.put("Username", tweet.getUser().getName());
                        twitterPosts.put("Text", tweet.getText());
                        twitterPosts.put("OwnerImage", tweet.getUser().getBiggerProfileImageURL());

                        String contains = "";
                        for (String str : likes) {
                            if (tweet.getText().toLowerCase().contains(str)) {
                                contains = str;
                                break;
                            }
                        }
                        twitterPosts.put("Matched", contains);
                        twitterList.add(twitterPosts);
                    }


                }


            } catch (TwitterException te) {
                te.printStackTrace();
                System.out.println("Failed to search tweets: " + te.getMessage());
                System.exit(-1);
            }

            return null;
        }
    }
}
