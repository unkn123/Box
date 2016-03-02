package com.jiit.minor2.shubhamjoshi.box.signup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
import com.jiit.minor2.shubhamjoshi.box.dialogs.DateDialogPicker;
import com.jiit.minor2.shubhamjoshi.box.model.User;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private TextView dateTextView;
    private EditText email;
    private EditText username;
    private TextView dob;
    private View facebookLoginButton;
    private EditText password;
    private TextView genderTextView;
    private ProgressDialog mProgress;
    private Firebase baseUrl;
    private LoginButton mLoginButton;
    private CallbackManager mCallbackManager;
    private String Email;
    private String Username;
    private String Dob;
    private String Gender;
    private final static String TAG = SignUp.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();

        dateTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                DateDialogPicker picker = new DateDialogPicker(v);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                picker.show(ft, getString(R.string.Date));
                return false;
            }
        });

        genderTextView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final CharSequence[] gender = {getString(R.string.Male), getString(R.string.Female)};
                AlertDialog.Builder alert = new AlertDialog.Builder(SignUp.this, AlertDialog.THEME_HOLO_DARK);
                alert.setTitle(Html.fromHtml(getString(R.string.green_color_hack) + getString(R.string.Gender) + getString(R.string.font_tag)));
                alert.setItems(gender, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (gender[which] == getString(R.string.Male)) {
                            genderTextView.setText(getString(R.string.Male));
                        } else
                            genderTextView.setText(getString(R.string.Female));
                    }
                });

                /****HACK **/
                Dialog d = alert.show();
                int dividerId = d.getContext().getResources().getIdentifier(getString(R.string.hack), null, null);
                View divider = d.findViewById(dividerId);
                divider.setBackgroundColor(Color.parseColor("#1db954"));
                return false;
            }
        });

    }


    //For storing in db
    Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
        @Override
        public void onAuthenticated(AuthData authData) {
            // Authenticated successfully with payload authData
            User user = new User(Email, Username, Dob, Gender);
            Firebase child = baseUrl.child(Constants.USER).child(Constants.encodeEmail(Email));
            child.setValue(user);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            // Authenticated failed with error firebaseError
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mProgress = new ProgressDialog(SignUp.this, ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setTitle(getString(R.string.processing));
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        init();

        baseUrl = new Firebase(Constants.FIREBASE_URL);
        mCallbackManager = CallbackManager.Factory.create();

        //Crux of facebook Login
        fbLoginFunctionality();


        //Facebook Login Screen
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginButton.performClick();
            }
        });


        View S = findViewById(R.id.signUp);
        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                Email = email.getText().toString();
                final String Password = password.getText().toString();
                Username = username.getText().toString();
                Dob = dob.getText().toString();
                Gender = genderTextView.getText().toString();
                //FireBase Normal Login

                baseUrl.createUser(Email, Password, new Firebase.ValueResultHandler<Map<String, Object>>() {

                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        baseUrl.authWithPassword(Email, Password, authResultHandler);
                        mProgress.dismiss();
                        finish();
                        //dismiss the progress dialog
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        mProgress.dismiss();
                        if (firebaseError.getCode() == FirebaseError.INVALID_EMAIL)
                            email.setError(firebaseError.getMessage());
                        if (firebaseError.getCode() == FirebaseError.INVALID_PASSWORD)
                            password.setError(firebaseError.getMessage());
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
                System.out.println("onSuccess");
                final String accessToken = loginResult.getAccessToken()
                        .getToken();
                Log.e(TAG, loginResult.toString());
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
                                    String email = object.getString("email");
                                    String gender = object.getString("gender");
                                    String birthday = object.getString("birthday");

                                    //String birthday="";
                                    onFacebookAccessTokenChange(loginResult.getAccessToken(), name, email, birthday, gender);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields",
                        "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("onError");
            }
        });
    }

    private void onFacebookAccessTokenChange(AccessToken token, final String name, final String email, final String dob, final String gender) {
        if (token != null) {
            baseUrl.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    //add to db
                    User user = new User(email, name, dob, gender);
                    Firebase child = baseUrl.child(Constants.USER).child(Constants.encodeEmail(email));
                    child.setValue(user);
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {

                }
            });
        } else {
            baseUrl.unauth();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        mCallbackManager.onActivityResult(requestCode, responseCode, data);
    }

    public void init() {
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        dateTextView = (TextView) findViewById(R.id.date);
        genderTextView = (TextView) findViewById(R.id.gender);
        dob = (TextView) findViewById(R.id.date);
        facebookLoginButton = findViewById(R.id.fbLogin);
        mLoginButton = (LoginButton) findViewById(R.id.login_button);

    }

}
