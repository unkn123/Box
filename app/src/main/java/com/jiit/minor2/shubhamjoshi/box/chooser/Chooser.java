package com.jiit.minor2.shubhamjoshi.box.chooser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.jiit.minor2.shubhamjoshi.box.Adapters.AdapterForChooser;
import com.jiit.minor2.shubhamjoshi.box.MainActivity;
import com.jiit.minor2.shubhamjoshi.box.StarterPage;
import com.jiit.minor2.shubhamjoshi.box.profile.Profile;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.list_models.Categories;
import com.jiit.minor2.shubhamjoshi.box.model.list_models.GiantChooserModel;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;

public class Chooser extends AppCompatActivity {

    private ArrayList chooserItems = new ArrayList();
    private GridLayoutManager mGridLayoutManager;
    private String pathPart;
    private ArrayList<String> likes = new ArrayList<>();
    private ArrayList<GiantChooserModel> mGiantChooserModels = new ArrayList<>();
    private FirebaseRecyclerAdapter<Categories, ChooserInterestHolder> mAdapter;
    private int countForStatus = 0;
    private String ImageUrl;


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");
//        ImageUrl = sp.getString("ProfilePhoto","ERROR");

       // Log.e("SJ", ImageUrl+"SD");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        final LinearLayout header = (LinearLayout) findViewById(R.id.headerChooser);
        final RelativeLayout footer = (RelativeLayout) findViewById(R.id.footerChooser);


        header.setAlpha(0f);
        footer.setAlpha(0f);

        final Firebase baseRef = new Firebase(Constants.FIREBASE_URL);
        View v = findViewById(R.id.logout);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for logging out from firebase
                baseRef.unauth();
                //For loggin out from facebook
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });


        Firebase newRef = baseRef.child(Constants.FIREBASE_CATEGORIES);

        newRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Categories post = postSnapshot.getValue(Categories.class);
                    chooserItems.add(post);
                    GiantChooserModel giantChooserModel = new GiantChooserModel();
                    giantChooserModel.setDescription(post.getDescription());
                    giantChooserModel.setSelected(false);
                    giantChooserModel.setUrl(post.getUrl());
                    mGiantChooserModels.add(giantChooserModel);


                }

                ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.chooserProgress);
                mProgressBar.setVisibility(View.INVISIBLE);
                footer.setAlpha(1f);
                header.setAlpha(1f);

                mGridLayoutManager = new GridLayoutManager(Chooser.this, 3);
                RecyclerView rView = (RecyclerView) findViewById(R.id.interest_choices_recycler_view);
                rView.setLayoutManager(mGridLayoutManager);
                AdapterForChooser mAdapterForChooser = new AdapterForChooser(Chooser.this, chooserItems, pathPart,mGiantChooserModels);
                rView.setAdapter(mAdapterForChooser);
                rView.setHasFixedSize(true);

                mAdapterForChooser.setClickListener(new AdapterForChooser.ClickListener() {

                    @Override
                    public void onClick(View v, int pos) {


                        Firebase ref = baseRef.child("likes").child(pathPart);
                        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.selectedBg);

                        if (!mGiantChooserModels.get(pos).isSelected()) {
                            Vibrator vibe = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibe.vibrate(50);
                            rl.setVisibility(View.VISIBLE);
                            countForStatus++;

                            likes.add(mGiantChooserModels.get(pos).getDescription());
                            mGiantChooserModels.get(pos).setSelected(true);


                            ref.child(mGiantChooserModels.get(pos).getDescription()).setValue("");


                        } else {
                            rl.setVisibility(View.INVISIBLE);
                            countForStatus--;
                            //o(n) complexity
                            int count = Collections.frequency(likes, mGiantChooserModels.get(pos).getDescription());
                            if (count == 1)
                                ref.child(mGiantChooserModels.get(pos).getDescription()).removeValue();
                            mGiantChooserModels.get(pos).setSelected(false);
                            likes.remove(mGiantChooserModels.get(pos).getDescription());
                        }

//                        Log.e("SJS",likes.size() + "");

                    }
                });

                mAdapterForChooser.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chooser.this,StarterPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}
