package com.teamx.soon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.teamx.soon.item.Event;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment {
    private static final String PARAM_EVENT = "param1";

    private Event event;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EventDetailFragment.
     */
    public static EventDetailFragment newInstance(Event param1) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(PARAM_EVENT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable(PARAM_EVENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);

        if (event != null) {
            TextView name = (TextView) view.findViewById(R.id.event_name);
            TextView place = (TextView) view.findViewById(R.id.event_place);
            TextView date = (TextView) view.findViewById(R.id.event_date);
            TextView des = (TextView) view.findViewById(R.id.event_des);
            ImageView image = (ImageView) view.findViewById(R.id.event_image);
//            View dateContainer = view.findViewById(R.id.event_date_container);
//            View placeContainer = view.findViewById(R.id.event_place_container);

            name.setText(event.name);
            place.setText(event.address);
            date.setText(event.date);
            des.setText(event.des);
            Picasso.with(getActivity()).load(event.image).into(image);
//            dateContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//            placeContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

        }

        return view;
    }


}
