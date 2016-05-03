package com.jiit.minor2.shubhamjoshi.box.Steps.TopicSteps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.Steps.Results;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.Post;
import com.jiit.minor2.shubhamjoshi.box.model.SubModal;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubCategory extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private AppBarLayout mAppBarLayout;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private TextView mTitle;
    private String postHead;
    private ProgressBar mProgressBar;
    private ArrayList<SubModal> mList = new ArrayList<>();
    private String photoHead;
    private RecyclerView mRecyclerView;
    private int count=0;
    private String urlToParse;
    private String ratingsHead;
    private String pathPart;
    private EditText searchLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.cover);
        init();
        setTitle("");
        Bundle bundle = getIntent().getExtras();
        String imageOfCover = bundle.getString("IMAGE");


        Picasso.with(getBaseContext()).load(imageOfCover).into(imageView);
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);


        //Regular Expression work
        final Button search = (Button) findViewById(R.id.createQueryButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Post p = new Post("User just asked ","tell me ",pathPart,"http://jiitminor128.netai.net/pictures/2.JPG");
                final Firebase firebase = new Firebase(Constants.FIREBASE_URL);

                firebase.child("follower").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                            firebase.child("DisplayPosts").child(snapshot.getKey()).push().setValue(p);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });



                String searchHLLanguage = searchLanguage.getText().toString();
                HashMap<String, String> regs = new HashMap<String, String>();
                regs.put("TOP", "(top) (\\d+)");
                regs.put("cheapest", "(cheapest) (\\d+)");


                for (HashMap.Entry<String, String> entry : regs.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Matcher m = Pattern.compile(value).matcher(searchHLLanguage);
                    boolean f = m.find();
                    while (f) {
                        System.out.println(m.group(1).toLowerCase().trim());
                        urlToParse = generateURL(m.group(1).toLowerCase().trim(), m.group(2).toLowerCase().trim());
                        new MyTask().execute();

                        f = m.find();
                    }

                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public String generateURL(String phrase, String count) {
        String finalUrl = "";
        String CITY = "ncr";
        this.count = Integer.parseInt(count);
        if (phrase.toLowerCase().equals("top"))
            finalUrl = Constants.generalFoodUrl + "" + CITY + "/best" + "-" + "restaurants";
        else
            finalUrl = Constants.generalFoodUrl + "" + CITY + "/restaurants" + "?sort=ca";
        return finalUrl;
    }

    private void init() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mTitle = (TextView) findViewById(R.id.title);
        mProgressBar = (ProgressBar)findViewById(R.id.chooserProgress);
        searchLanguage = (EditText) findViewById(R.id.searchLang);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
       // Log.e("SJSJ", percentage + "");
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

    class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            String title = "";
            Document doc;
            try {
                doc = Jsoup.connect(urlToParse).get();
                Elements article = doc.select("article.search-result");

                for (org.jsoup.nodes.Element element : article) {
                    Elements resturants = element.select("a.result-title");

                    Elements rating = element.select(".res-rating-nf");
                    Elements photo = element.select("a.feat-img");


                    String style = photo.attr("data-original");
                    postHead = resturants.text();
                    photoHead = style.toString();
                    ratingsHead = rating.text();

                    System.out.print(ratingsHead);


                    SubModal sm = new SubModal(photoHead, postHead, ratingsHead,count);

                    mList.add(sm);




                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }


        @Override
        protected void onPostExecute(String result) {
          //  Log.e("SJSJ",ratingsHead);

            Intent intent = new Intent(getApplicationContext(),Results.class);
            Log.e("SJJS",count+"");
            ArrayList<SubModal> list = new ArrayList<>();
            list.addAll(mList.subList(0,count));
            intent.putExtra("LIST", (Serializable)list);
            startActivity(intent);


        }
    }
}
