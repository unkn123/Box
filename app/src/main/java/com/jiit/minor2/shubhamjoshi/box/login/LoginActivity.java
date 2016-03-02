package com.jiit.minor2.shubhamjoshi.box.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.chooser.Chooser;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private EditText password;
    private EditText email;
    private View facebookLoginButton;
    private ProgressDialog mProgress;
    private CallbackManager mCallbackManager;
    private LoginButton mLoginButton;
    private Firebase baseRef;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        //progress bar setup
        mProgress = new ProgressDialog(LoginActivity.this, ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setTitle(getString(R.string.processing));
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        baseRef = new Firebase(Constants.FIREBASE_URL);
        mCallbackManager = CallbackManager.Factory.create();

        //For fb LoginButton
        fbLoginFunctionality();

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginButton.performClick();
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
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        mProgress.dismiss();
                        if (firebaseError.getCode() == FirebaseError.INVALID_EMAIL)
                            email.setError(firebaseError.getMessage());
                        else if (firebaseError.getCode() == FirebaseError.INVALID_PASSWORD)
                            password.setError(firebaseError.getMessage());
                        else if (firebaseError.getCode() == FirebaseError.USER_DOES_NOT_EXIST)
                            email.setError(firebaseError.getMessage());
                        Toast.makeText(getBaseContext(), firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void fbLoginFunctionality() {
        mLoginButton.setReadPermissions(Arrays.asList(Constants.USER_PHOTO, Constants.EMAIL,
                Constants.BIRTHDAY, Constants.PUBLIC_PROFILE));

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                final String accessToken = loginResult.getAccessToken()
                        .getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String id = object.getString("id");
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=large");
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    String name = object.getString("name");
                                    onFacebookAccessTokenChange(loginResult.getAccessToken());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            baseRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    Intent intent = new Intent(getBaseContext(), Chooser.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {

                }
            });
        } else {
            baseRef.unauth();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        mCallbackManager.onActivityResult(requestCode, responseCode, data);
    }

    public void init() {
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        facebookLoginButton = findViewById(R.id.fbLogin);
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
    }
}
