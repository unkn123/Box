package com.jiit.minor2.shubhamjoshi.box.Steps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jiit.minor2.shubhamjoshi.box.Adapters.ParseAdapter;
import com.jiit.minor2.shubhamjoshi.box.R;
import com.jiit.minor2.shubhamjoshi.box.model.SubModal;

import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<SubModal> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler);
        Intent i = getIntent();
        list = (List<SubModal>) i.getSerializableExtra("LIST");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ParseAdapter(this,list));
    }

}
