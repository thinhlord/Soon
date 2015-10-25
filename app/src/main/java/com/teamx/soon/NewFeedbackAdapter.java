package com.teamx.soon;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.teamx.soon.item.Feedback;

import java.util.ArrayList;


/**
 * Created by ruler_000 on 10/04/2015.
 * Project: SoYBa
 */
public class NewFeedbackAdapter extends RecyclerView.Adapter<NewFeedbackAdapter.ViewHolder> {
    Activity activity;
    private ArrayList<Feedback> sicks;

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewFeedbackAdapter(Activity activity, ArrayList<Feedback> sicks) {
        this.activity = activity;
        this.sicks = sicks;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewFeedbackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_adapter_feedback, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Feedback event = sicks.get(position);
        if (event != null) {

            viewHolder.number.setText((position + 1) + "");
            viewHolder.content.setText(event.content);
            if (event.type == 1) {
                viewHolder.answerText.setVisibility(View.GONE);
                viewHolder.answerChoice.setVisibility(View.VISIBLE);
            } else {
                viewHolder.answerText.setVisibility(View.VISIBLE);
                viewHolder.answerChoice.setVisibility(View.GONE);
            }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sicks.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView number;
        public TextView content;
        public RadioGroup answerChoice;
        public EditText answerText;

        public ViewHolder(View v) {
            super(v);
            number = (TextView) v.findViewById(R.id.feedback_number);
            content = (TextView) v.findViewById(R.id.feedback_content);
            answerChoice = (RadioGroup) v.findViewById(R.id.radio_group);
            answerText = (EditText) v.findViewById(R.id.answer_text);
        }
    }
}
