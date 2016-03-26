package com.jiit.minor2.shubhamjoshi.box;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.jiit.minor2.shubhamjoshi.box.Steps.StarterPage;
import com.jiit.minor2.shubhamjoshi.box.login.LoginActivity;
import com.jiit.minor2.shubhamjoshi.box.signup.SignUp;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;


public class MainActivity extends AppCompatActivity {

    private VideoView view;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Firebase baseRef = new Firebase(Constants.FIREBASE_URL);
        baseRef.addAuthStateListener(new Firebase.AuthStateListener() {

            @Override
            public void onAuthStateChanged(final AuthData authData) {

                if (authData != null) {
                    // user is logged in

                    Intent intent = new Intent(getBaseContext(), StarterPage.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    //startActivity(new Intent(MainActivity.this, StarterPage.class));
                } else {

                    Button login = (Button) findViewById(R.id.loginB);
                    login.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getBaseContext(), LoginActivity.class));

                        }
                    });

                    Button signup = (Button) findViewById(R.id.signupB);
                    signup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getBaseContext(), SignUp.class));

                        }
                    });
                }
            }
        });


    }
}
