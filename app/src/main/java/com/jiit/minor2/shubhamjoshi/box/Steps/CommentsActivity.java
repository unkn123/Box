package com.jiit.minor2.shubhamjoshi.box.Steps;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.jiit.minor2.shubhamjoshi.box.Holder.CommentsHolder;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.PostModels.Comment;
import com.jiit.minor2.shubhamjoshi.box.model.User;
import com.jiit.minor2.shubhamjoshi.box.utils.Constants;
import com.jiit.minor2.shubhamjoshi.box.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    private EditText commentBox;
    private String pathPart;
    private FirebaseRecyclerAdapter mAdapter;
    private TextView post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        init();

        final Firebase vaibhavFirebaseQuery = new Firebase(Constants.FIREBASE_URL);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comment = commentBox.getText().toString();
                final HashMap<String, String> h = new HashMap<String, String>();
                commentBox.setText("");

                vaibhavFirebaseQuery.child("user").child(pathPart).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        h.put("username", pathPart);
                        h.put("comment", comment);
                        User user = dataSnapshot.getValue(User.class);
                        Comment c = new Comment(comment,pathPart,user.getProfileUrl());
                        h.put("image", user.getProfileUrl());
                        vaibhavFirebaseQuery.child("postsComments").child(getIntent().getStringExtra("KEY").toString())
                                .push().setValue(c);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        Firebase mRef = vaibhavFirebaseQuery.child("postsComments").child(getIntent().getStringExtra("KEY").toString());

        mAdapter = new FirebaseRecyclerAdapter<Comment, CommentsHolder>(Comment.class, R.layout.comment_frame,CommentsHolder.class, mRef) {
            @Override
            public void populateViewHolder(CommentsHolder chatMessageViewHolder, Comment chatMessage, int position) {
                chatMessageViewHolder.comment.setText(chatMessage.getComment());
                int index = chatMessage.getUsername().lastIndexOf("@");


                if (chatMessage.getTimestampLastChanged() != null) {
                                            chatMessageViewHolder.time.setText(
                                                            caluculateTimeAgo(chatMessage.getTimestampLastChangedLong()));
                                        } else {
                                            chatMessageViewHolder.time.setText("");
                }

                chatMessageViewHolder.username.setText(chatMessage.getUsername().substring(0, index));
                Picasso.with(getBaseContext()).load(chatMessage.getImage()).into(chatMessageViewHolder.image);

            }
        };
        recycler.setAdapter(mAdapter);


    }

    public  void init()
    {
        SharedPreferences sp = getSharedPreferences(Constants.SHAREDPREF_EMAIL, Context.MODE_PRIVATE);
        pathPart = sp.getString(Constants.SPEMAIL, "Error");

        commentBox = (EditText)findViewById(R.id.comment);
        post = (TextView)findViewById(R.id.post);
    }

    public static String caluculateTimeAgo(long timeStamp) {

        double seconds = Math.floor((System.currentTimeMillis() - timeStamp) / 1000);  //get current time in seconds.
        double interval = Math.floor(seconds / 31536000);
        if (interval >= 1) {
            return interval + " y";
        } else {
            interval = Math.floor(seconds / 2592000);
            if (interval >= 1) {
                return interval + " m";
            }
            interval = Math.floor(seconds / 86400);
            if (interval >= 1)
                return interval + " d";
            interval = Math.floor(seconds / 3600);
            if (interval >= 1)
                return interval + " h";
            interval = Math.floor(seconds / 60);
            if (interval >= 1)
                return interval + " m";
            double t = Math.floor(seconds);
            return t + " s";
        }
    }
}
