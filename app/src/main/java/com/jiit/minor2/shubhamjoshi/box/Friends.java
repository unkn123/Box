package com.jiit.minor2.shubhamjoshi.box;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Friends extends AppCompatActivity {

    private ImageView firstSuggestedFriend1;
    private ImageView firstSuggestedFriend2;
    private ImageView firstSuggestedFriend3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        init();

        final String id  = AccessToken.getCurrentAccessToken().getUserId().toString();

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



                            for(int i=0;i<rawName.length();i++)
                            {

                                URL profile_pic = new URL(
                                        "https://graph.facebook.com/" + rawName.getJSONObject(i).getString("id").toString()
                                                + "/picture?type=large");
                                if(i==0)
                                Picasso.with(getBaseContext()).load(profile_pic.toString()).into(firstSuggestedFriend1);
                                if(i==1) Picasso.with(getBaseContext()).load(profile_pic.toString()).into(firstSuggestedFriend2);
                                if(i==2) Picasso.with(getBaseContext()).load(profile_pic.toString()).into(firstSuggestedFriend3);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }

                    }
                }
        ).executeAsync();
    }

    public void init()
    {
        firstSuggestedFriend1 = (ImageView)findViewById(R.id.oneSug1);
        firstSuggestedFriend2 = (ImageView)findViewById(R.id.oneSug2);
        firstSuggestedFriend3 = (ImageView)findViewById(R.id.oneSug3);
    }
}
