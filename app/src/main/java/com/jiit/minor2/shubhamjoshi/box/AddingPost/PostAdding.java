package com.jiit.minor2.shubhamjoshi.box.AddingPost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.CopyPost;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.Post;
import com.jiit.minor2.shubhamjoshi.box.model.SingleEmail;
import com.jiit.minor2.shubhamjoshi.box.model.User;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.jiit.minor2.shubhamjoshi.box.utils.PicUploadBackend;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private ProgressDialog mProgress;
    private String postImageName;
    HashMap<String, Object> fun;
    HashMap<String, Object> timePre;
    HashMap<String, Object> root;


    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_adding);
        init();
        mProgress = new ProgressDialog(PostAdding.this, ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setTitle(getString(R.string.processing));
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

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
                final Firebase posts = baseRef.child("posts").child(pathPart);
                final Firebase allPosts = baseRef.child("posts").child(pathPart);


                final Firebase follower = baseRef.child("follower").child(pathPart);
                final String uniqueKey = posts.push().getKey();

                final String imageUrl;
                if (filePath == null)
                    imageUrl = "";
                else
                    imageUrl = Constants.MAIN_URL + uniqueKey + ".JPG";

                final Post post = new Post(postTitle.getText().toString(), postBody.getText().toString(),
                        pathPart, imageUrl);


                String iii = post.getPostImageUrl();
                int start = iii.lastIndexOf("/");
                final String keyUnique =post.getPostImageUrl().substring(start+1,iii.length()-4);
                final Firebase newQuery  = new Firebase(Constants.FIREBASE_URL).child("postsStatus");




                posts.child(uniqueKey).setValue(post);
                allPosts.child(uniqueKey).setValue(post);


                final Long time = -1 * System.currentTimeMillis() / 1000L;

                final Firebase readRef = allPosts.child(uniqueKey);

                readRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Post post = dataSnapshot.getValue(Post.class);
                        Long timeStamp = post.getTimestampLastChangedLong();
                        timePre = new HashMap<String, Object>();
                        timePre.put("timestamp", timeStamp);
                        fun = new HashMap<String, Object>();
                        fun.put("timestamp", time);
                        root = new HashMap<String, Object>();
                        root.put("timestampLastChangedReverse", fun);

                        readRef.updateChildren(root);

                        //Getting all followers
                        final ArrayList<String> followersList = new ArrayList<String>();

                        Firebase refBaseForFollowers = new Firebase(Constants.FIREBASE_URL).child("follower")
                                .child(pathPart);
                        refBaseForFollowers.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot sp : dataSnapshot.getChildren()) {
                                    followersList.add(sp.getKey());

                                }

                                final CopyPost newCopyPost = new CopyPost(postTitle.getText().toString(), postBody.getText().toString(),
                                        pathPart, imageUrl, timePre, fun);

                                posts.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.e("SJ", "size " + followersList.size());
                                        for (int i = 0; i < followersList.size(); i++) {

                                            Firebase workRef = new Firebase(Constants.FIREBASE_URL);
                                            workRef.child("DisplayPosts").child(followersList.get(i))
                                                    .child(uniqueKey).setValue(newCopyPost);
                                        }
                                        Firebase personal = new Firebase(Constants.FIREBASE_URL);
                                        personal.child("DisplayPosts").child(pathPart)
                                                .child(uniqueKey).setValue(newCopyPost);
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


                //if()
                if (filePath != null)
                    uploadImage(uniqueKey);

                else
                    finish();

            }
        });

        picturePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageOfPost.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(final String postImageName) {

        this.postImageName = postImageName;
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            PicUploadBackend rh = new PicUploadBackend();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgress.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mProgress.dismiss();
                finish();
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put("image", uploadImage);


                data.put("name", postImageName);

                String result = rh.sendPostRequest(Constants.UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }


}
