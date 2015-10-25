package com.teamx.soon.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.teamx.soon.FeedbackListAdapter;
import com.teamx.soon.HttpClient;
import com.teamx.soon.R;
import com.teamx.soon.item.Event;
import com.teamx.soon.item.Feedback;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<Feedback> feedback = new ArrayList<>();
    ListView feedbackList;
    FeedbackListAdapter feedbackListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Title");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        feedbackList = (ListView) findViewById(R.id.feedback_list);
        feedbackListAdapter = new FeedbackListAdapter(this, feedback);
        feedbackList.setAdapter(feedbackListAdapter);

        Event e = (Event) getIntent().getSerializableExtra("event");
        if (e != null) {
            HttpClient.getFeedback(e, new HttpClient.ListResponse<Feedback>() {
                @Override
                public void onSuccess(ArrayList<Feedback> objects) {
                    feedback = objects;
                    feedbackList = (ListView) findViewById(R.id.feedback_list);
                    feedbackListAdapter = new FeedbackListAdapter(FeedbackActivity.this, feedback);
                    feedbackList.setAdapter(feedbackListAdapter);
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }

    }
}
