package com.jiit.minor2.shubhamjoshi.box.AddingPost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.Post;
import com.jiit.minor2.shubhamjoshi.box.model.User;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PostAdding extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;
    private String pathPart;
    private String ImageUrl;
    private Firebase baseRef;
    private ImageView profile;
    private TextView submitPost;
    private ImageView picturePick;
    private ImageView imageOfPost;
    private EditText postTitle;
    private EditText postBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_adding);
        init();
        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");
        // Log.e("SJSj", pathPart);
        Firebase ref = baseRef.child(Constants.USER).child(pathPart);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                ImageUrl = user.getProfileUrl();

                Picasso.with(getBaseContext()).load(ImageUrl).resize(100, 100).into(profile);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        submitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase posts = baseRef.child("posts").child(pathPart);
                Firebase allPosts = baseRef.child("allPosts");
                Post post = new Post(postTitle.getText().toString(), postBody.getText().toString(),pathPart);

                String id = posts.push().getKey();



                posts.child(id).setValue(post);
                allPosts.child(id).setValue(post);
                finish();
            }
        });

        picturePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    public void init() {
        baseRef = new Firebase(Constants.FIREBASE_URL);
        profile = (ImageView) findViewById(R.id.pImage);
        submitPost = (TextView) findViewById(R.id.postTextView);
        picturePick = (ImageView) findViewById(R.id.picture_tab);
        imageOfPost = (ImageView) findViewById(R.id.image_of_post);
        postTitle = (EditText) findViewById(R.id.titlePost);
        postBody = (EditText) findViewById(R.id.post_body);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                    imageOfPost.setImageBitmap(yourSelectedImage);
                }
        }
    }
}