package com.teamx.soon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamx.soon.activity.EventActivity;
import com.teamx.soon.item.Event;

import java.util.ArrayList;

/**
 * Created by Nguyen Duc Thinh on 20/02/2015.
 * Project type: Android
 */
public class EventListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Event> events;

    public EventListAdapter(Context c, ArrayList<Event> events) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_adapter_event, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.event_image);
            viewHolder.name = (TextView) convertView.findViewById(R.id.event_name);
            viewHolder.date = (TextView) convertView.findViewById(R.id.event_date);
            viewHolder.place = (TextView) convertView.findViewById(R.id.event_place);
            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Event event = events.get(position);
        if (event != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EventActivity.class);
                    intent.putExtra("event", event);
                    mContext.startActivity(intent);
                }
            });


            viewHolder.name.setText(event.name);
            viewHolder.date.setText(event.date);
            viewHolder.place.setText(event.address);
            Picasso.with(mContext).load(event.image).placeholder(R.drawable.ic_logo).into(viewHolder.image);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView name;
        TextView date;
        TextView place;
    }

}
