package com.jiit.minor2.shubhamjoshi.box;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.jiit.minor2.shubhamjoshi.box.chooser.Chooser;
import com.jiit.minor2.shubhamjoshi.box.login.LoginActivity;
import com.jiit.minor2.shubhamjoshi.box.signup.SignUp;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Firebase baseRef = new Firebase(Constants.FIREBASE_URL);
        baseRef.addAuthStateListener(new Firebase.AuthStateListener() {

            @Override
            public void onAuthStateChanged(AuthData authData) {

                if (authData != null) {
                    // user is logged in
                    Intent intent = new Intent(getBaseContext(), Chooser.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(new Intent(getBaseContext(), Chooser.class));
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
