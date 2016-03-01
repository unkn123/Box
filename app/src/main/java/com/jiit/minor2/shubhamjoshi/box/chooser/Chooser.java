package com.jiit.minor2.shubhamjoshi.box.chooser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.jiit.minor2.shubhamjoshi.box.Adapters.AdapterForChooser;
import com.jiit.minor2.shubhamjoshi.box.MainActivity;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.list_models.ChooserObject;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class Chooser extends AppCompatActivity {

    private List<ChooserObject> chooserItems;
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


        chooserItems = getAllCategories();
        mGridLayoutManager = new GridLayoutManager(Chooser.this, 3);

        RecyclerView rView = (RecyclerView) findViewById(R.id.interest_choices_recycler_view);

        rView.setLayoutManager(mGridLayoutManager);


        AdapterForChooser mAdapterForChooser = new AdapterForChooser(Chooser.this, chooserItems);
        rView.setAdapter(mAdapterForChooser);


        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFERENCE_FOR_GETTING_USER, MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            String email = prefs.getString("email", "No name defined");
            Log.e("Tag", email);
        }


    }


    public List<ChooserObject> getAllCategories() {
        List<ChooserObject> chooserItems = new ArrayList<ChooserObject>();
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter1304.JPG"));

        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter1173.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter5358.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter2134.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter62.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter4592.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter292.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter3321.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter4592.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter292.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter2134.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter2187.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter4498.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter2646.JPG"));
        chooserItems.add(new ChooserObject("http://jiitminor128.netai.net/pictures/counter4592.JPG"));
        return chooserItems;
    }
}
