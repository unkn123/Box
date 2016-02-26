package com.jiit.minor2.shubhamjoshi.box.chooser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.jiit.minor2.shubhamjoshi.box.MainActivity;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

public class Chooser extends AppCompatActivity {

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
    }
}
