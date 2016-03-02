package com.jiit.minor2.shubhamjoshi.box.chooser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jiit.minor2.shubhamjoshi.box.Adapters.AdapterForChooser;
import com.jiit.minor2.shubhamjoshi.box.MainActivity;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.Categories;
import com.jiit.minor2.shubhamjoshi.box.model.list_models.ChooserObject;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Chooser extends AppCompatActivity {

    private ArrayList chooserItems = new ArrayList();
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
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

        Firebase newRef = baseRef.child("categories");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("There are " + snapshot.getChildrenCount() + " blog posts");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Categories post = postSnapshot.getValue(Categories.class);
                    System.out.println(post.getUrl() + " - " + post.getDescription());
                    chooserItems.add(post);

                }
                Log.e("Shubham", "" + chooserItems.size());
                mGridLayoutManager = new GridLayoutManager(Chooser.this, 3);

                RecyclerView rView = (RecyclerView) findViewById(R.id.interest_choices_recycler_view);

                rView.setLayoutManager(mGridLayoutManager);


                AdapterForChooser mAdapterForChooser = new AdapterForChooser(Chooser.this, chooserItems);
                rView.setAdapter(mAdapterForChooser);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
        // chooserItems = getAllCategories();

    }
}
