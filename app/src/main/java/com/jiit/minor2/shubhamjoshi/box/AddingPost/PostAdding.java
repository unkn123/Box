package com.jiit.minor2.shubhamjoshi.box.AddingPost;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.User;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;

public class PostAdding extends AppCompatActivity {

    private Toolbar mToolbar;
    private String pathPart;
    private String ImageUrl;
    private Firebase baseRef;
    private ImageView profile;
    private TextView submitPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_adding);
        init();
        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");
       // Log.e("SJSj", pathPart);
        Firebase ref = baseRef.child(Constants.USER).child(pathPart);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                ImageUrl = user.getProfileUrl();

                Picasso.with(getBaseContext()).load(ImageUrl).resize(100, 100).into(profile);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        submitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void init() {
        baseRef = new Firebase(Constants.FIREBASE_URL);
        profile = (ImageView) findViewById(R.id.pImage);
        submitPost = (TextView)findViewById(R.id.postTextView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
