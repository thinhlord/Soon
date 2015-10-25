package com.teamx.soon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.teamx.soon.item.Feedback;

import java.util.ArrayList;

/**
 * Created by Nguyen Duc Thinh on 20/02/2015.
 * Project type: Android
 */
public class FeedbackListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Feedback> events;

    public FeedbackListAdapter(Context c, ArrayList<Feedback> events) {
        mContext = c;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_feedback, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.number = (TextView) convertView.findViewById(R.id.feedback_number);
            viewHolder.content = (TextView) convertView.findViewById(R.id.feedback_content);
            viewHolder.answerChoice = (RadioGroup) convertView.findViewById(R.id.radio_group);
            viewHolder.answerText = (EditText) convertView.findViewById(R.id.answer_text);
            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Feedback event = events.get(position);
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
        return convertView;
    }

    static class ViewHolder {
        TextView number;
        TextView content;
        RadioGroup answerChoice;
        EditText answerText;
    }

}
