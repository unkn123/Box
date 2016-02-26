package com.jiit.minor2.shubhamjoshi.box.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.chooser.Chooser;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private EditText password;
    private EditText email;
    private View facebookLoginButton;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        mProgress= new ProgressDialog(LoginActivity.this, ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        final Firebase baseRef = new Firebase(Constants.FIREBASE_URL);

        //For fb LoginButton

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("SJ","YO");
                com.facebook.login.widget.LoginButton btn = new LoginButton(LoginActivity.this);
                btn.performClick();
            }
        });

        View login = findViewById(R.id.LoginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                mProgress.show();
                baseRef.authWithPassword(Email, Password, new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {
                        mProgress.dismiss();
                        Intent intent = new Intent(getBaseContext(), Chooser.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Toast.makeText(getBaseContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void init() {
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        facebookLoginButton = findViewById(R.id.fbLogin);

    }
}
