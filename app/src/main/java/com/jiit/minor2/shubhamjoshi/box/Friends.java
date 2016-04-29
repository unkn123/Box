package com.jiit.minor2.shubhamjoshi.box;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.firebase.client.ChildEventListener;
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

public class Friends extends AppCompatActivity {

    private ImageView firstSuggestedFriend1;
    private ImageView firstSuggestedFriend2;
    private ImageView firstSuggestedFriend3;
    private TextView name1;
    FirebaseRecyclerAdapter mAdapter;
    private TextView name2;
    private RecyclerView usersList;
    private TextView name3;
    private String pathPart;

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


                                if (i == 0) {
                                    Picasso.with(getBaseContext()).load(profile_pic.toString()).centerCrop().resize(450, 600).into(firstSuggestedFriend1);
                                    name1.setText(rawName.getJSONObject(0).getString("name").toString());
                                }
                                if (i == 1) {
                                    Picasso.with(getBaseContext()).load(profile_pic.toString()).centerCrop().resize(450, 600).into(firstSuggestedFriend2);
                                    name2.setText(rawName.getJSONObject(1).getString("name").toString());
                                }
                                if (i == 2) {
                                    Picasso.with(getBaseContext()).load(profile_pic.toString()).centerCrop().resize(450, 600).into(firstSuggestedFriend3);
                                    name3.setText(rawName.getJSONObject(2).getString("name").toString());
                                }
                                ;
                                Log.e("SJ", rawName.getJSONObject(i).getString("name").toString());

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

                if (!Constants.encodeEmail(user.getEmail()).equals(pathPart)) { //ensures user dont see himself in friends to add list
                    if (user.getProfileUrl() != "")
                        Picasso.with(getBaseContext()).load(user.getProfileUrl()).centerCrop().resize(465, 465).into(friendsHolder.photo);
                    friendsHolder.name.setText(user.getUsername());
                    friendsHolder.name.setTextSize(14);
                    friendsHolder.photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Firebase alanRef = new Firebase(Constants.FIREBASE_URL).
                                    child("friends").child(pathPart);
                            alanRef.push().setValue(user);

                            Firebase ref = new Firebase(Constants.FIREBASE_URL).child("posts")
                                    .child(Constants.encodeEmail(user.getEmail()));
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    System.out.println("There are " + dataSnapshot.getKey()+ " blog posts");
                                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                        CopyPost post = postSnapshot.getValue(CopyPost.class);
                                        String key = postSnapshot.getKey();
                                        Firebase addingCopyPost = new Firebase(Constants.FIREBASE_URL).child("posts")
                                                .child(pathPart).child(key);
                                        addingCopyPost.setValue(post);

                                        Handler handler = new Handler();

                                        final Runnable r = new Runnable() {
                                            public void run() {
                                                notifyItemRemoved(position);
                                            }
                                        };

                                        handler.post(r);
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }
                    });
                } else {
                    Handler handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            notifyItemRemoved(position);
                        }
                    };

                    handler.post(r);
                }
            }
        };


        usersList.setAdapter(mAdapter);


    }

    public void init() {
        firstSuggestedFriend1 = (ImageView) findViewById(R.id.oneSug1);
        firstSuggestedFriend2 = (ImageView) findViewById(R.id.oneSug2);
        firstSuggestedFriend3 = (ImageView) findViewById(R.id.oneSug3);
        HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.suggestions);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);
        name1 = (TextView) findViewById(R.id.tt1);
        name2 = (TextView) findViewById(R.id.tt2);
        name3 = (TextView) findViewById(R.id.tt3);
        usersList = (RecyclerView) findViewById(R.id.usersList);


    }
}
