package com.jiit.minor2.shubhamjoshi.box.AddingPost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.jiit.minor2.shubhamjoshi.box.R;

public class PostAdding extends AppCompatActivity {

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_adding);
        init();
      //  setSupportActionBar(mToolbar);
    }

    public void init()
    {
       // mToolbar = (Toolbar) findViewById(R.id.tBar);
    }
}
