package com.jiit.minor2.shubhamjoshi.box;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.jiit.minor2.shubhamjoshi.box.Holder.FriendsHolder;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.CopyPost;
import com.jiit.minor2.shubhamjoshi.box.model.User;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Friends extends AppCompatActivity {

    FirebaseRecyclerAdapter mAdapter;

    private RecyclerView usersList;

    private String pathPart;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        init();
        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");


        final String id = AccessToken.getCurrentAccessToken().getUserId().toString();

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                        try {
                            JSONArray rawName = response.getJSONObject().getJSONArray("data");


                            for (int i = 0; i < rawName.length(); i++) {
                                URL profile_pic = new URL(
                                        "https://graph.facebook.com/" + rawName.getJSONObject(i).getString("id").toString()
                                                + "/picture?type=large");


                                ImageView image = new ImageView(getBaseContext());
                                rl.addView(image);
                                image.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.tH) - 30,
                                        100 + getResources().getDimensionPixelSize(R.dimen.tH)));
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(image.getLayoutParams());
                                lp.setMargins(2 + getResources().getDimensionPixelSize(R.dimen.tH) * i, 10, 0, 0);
                                TextView textView = new TextView(getBaseContext());
                                rl.addView(textView);

                                textView.setText(rawName.getJSONObject(i).getString("name").toString());
                                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                textView.setBackgroundColor(getResources().getColor(R.color.fadeBlack));
                                RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(image.getLayoutParams());


                                lp2.setMargins(2 + getResources().getDimensionPixelSize(R.dimen.tH) * i,
                                        getResources().getDimensionPixelSize(R.dimen.tH),0,0);

                                textView.setLayoutParams(lp2);
                                image.setLayoutParams(lp);


                                Picasso.with(getBaseContext()).load(profile_pic.toString()).centerCrop().resize(250, 300).into(image);


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }


                    }
                }
        ).executeAsync();


        final Firebase ref = new Firebase(Constants.FIREBASE_URL).child("user");


        usersList.setHasFixedSize(true);
        usersList.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));

        mAdapter = new FirebaseRecyclerAdapter<User, FriendsHolder>(User.class, R.layout.friends_list, FriendsHolder.class, ref) {
            @Override
            public void populateViewHolder(final FriendsHolder friendsHolder, final User user, final int position) {

//                if (Constants.encodeEmail(user.getEmail()).equals(pathPart)) { //ensures user dont see himself in friends to add list
                //Giant tree traversing algo

                if (user.getProfileUrl() != "")
                    Picasso.with(getBaseContext()).load(user.getProfileUrl()).centerCrop().resize(465, 465).into(friendsHolder.photo);
                friendsHolder.name.setText(user.getUsername());
                friendsHolder.name.setTextSize(14);

                friendsHolder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Firebase alanRef = new Firebase(Constants.FIREBASE_URL).
                                child("friends").child(pathPart);
                        Firebase follower = new Firebase(Constants.FIREBASE_URL).child("follower");
                        alanRef.child(Constants.encodeEmail(user.getEmail())).setValue(user);
                        HashMap<String, String> h = new HashMap<String, String>();
                        h.put("name", pathPart);
                        follower.child(Constants.encodeEmail(user.getEmail())).push().setValue(h);

                        Firebase ref = new Firebase(Constants.FIREBASE_URL).child("posts")
                                .child(Constants.encodeEmail(user.getEmail()));
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    CopyPost post = postSnapshot.getValue(CopyPost.class);
                                    String key = postSnapshot.getKey();
                                    Firebase addingCopyPost = new Firebase(Constants.FIREBASE_URL).child("DisplayPosts")
                                            .child(pathPart).child(key);
                                    addingCopyPost.setValue(post);


                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                });
            }
            //}
        };


        usersList.setAdapter(mAdapter);


    }

    public void init() {

        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.suggestions);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);

        usersList = (RecyclerView) findViewById(R.id.usersList);
        rl = (RelativeLayout) findViewById(R.id.relativeLayoutHSV);


    }
}
