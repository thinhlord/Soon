package com.teamx.soon.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import com.teamx.soon.HttpClient;
import com.teamx.soon.NewFeedbackAdapter;
import com.teamx.soon.R;
import com.teamx.soon.item.Event;
import com.teamx.soon.item.Feedback;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<Feedback> feedback = new ArrayList<>();
    RecyclerView feedbackList;
    NewFeedbackAdapter feedbackListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Phản hồi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feedbackList = (RecyclerView) findViewById(R.id.feedback_list);
        feedbackList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        feedbackList.setLayoutManager(mLayoutManager);
        feedbackListAdapter = new NewFeedbackAdapter(this, feedback);
        feedbackList.setAdapter(feedbackListAdapter);

        Event e = (Event) getIntent().getSerializableExtra("event");
        if (e != null) {
            Picasso.with(this).load(e.image).into((ImageView) findViewById(R.id.imageView));
            HttpClient.getFeedback(e, new HttpClient.ListResponse<Feedback>() {
                @Override
                public void onSuccess(ArrayList<Feedback> objects) {
                    feedback = objects;
                    feedbackListAdapter = new NewFeedbackAdapter(FeedbackActivity.this, feedback);
                    feedbackList.setAdapter(feedbackListAdapter);
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }

    }

    public void onClick(View v) {
        new MaterialDialog.Builder(this)
                .customView(R.layout.dialog, false)
                .show();
    }
}
