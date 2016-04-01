package com.jiit.minor2.shubhamjoshi.box.Steps.TopicSteps;

import android.content.Intent;
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
import android.widget.TextView;

import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.Steps.Results;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubCategory extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private AppBarLayout mAppBarLayout;
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    private boolean mIsTheTitleVisible = false;
    private TextView mTitle;
    private String postHead;
    private List<SubModal> mList = new ArrayList<>();
    private String photoHead;
    private RecyclerView mRecyclerView;
    private String urlToParse;
    private String ratingsHead;
    private EditText searchLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
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
                String searchHLLanguage = searchLanguage.getText().toString();
                HashMap<String, String> regs = new HashMap<String, String>();
                regs.put("TOP", "(top) (\\d+)");
                regs.put("cheapest", "(cheapest) (\\d+)");

//                mAppBarLayout.animate().translationY(-600).setDuration(500);
//                searchLanguage.animate().translationY(-300).setDuration(500);
                for (HashMap.Entry<String, String> entry : regs.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Matcher m = Pattern.compile(value).matcher(searchHLLanguage);
                    boolean f = m.find();
                    while (f) {
                        System.out.println(key);
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
        if (phrase.toLowerCase().equals("top"))
            finalUrl = Constants.generalFoodUrl + "" + CITY + "/best" + "-" + "restaurants";
        if (phrase.toLowerCase().equals("cheapest"))
            finalUrl = Constants.generalFoodUrl + "" + CITY + "/restaurants" + "?sort=ca";
        return finalUrl;
    }

    private void init() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        mTitle = (TextView) findViewById(R.id.title);
        searchLanguage = (EditText) findViewById(R.id.searchLang);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);

    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        Log.e("SJSJ", percentage + "");
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
        protected String doInBackground(Void... params) {
            String title = "";
            Document doc;
            try {
                doc = Jsoup.connect(urlToParse).get();
                Elements article = doc.select("article.search-result");

                for (org.jsoup.nodes.Element element : article) {
                    Elements resturants = element.select("a.result-title");
                    Elements newsHeadlines = element.select(".res-rating-nf");

                    Elements photo = element.select("a.feat-img");


                    String style = photo.attr("data-original");
                    postHead = resturants.text();
                    photoHead = style.toString();
                    ratingsHead = newsHeadlines.toString();
                    SubModal sm = new SubModal(photoHead, postHead, ratingsHead);
                    mList.add(sm);




                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;
        }


        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(getApplicationContext(),Results.class);
            intent.putExtra("LIST", (Serializable) mList);
            startActivity(intent);


        }
    }
}
