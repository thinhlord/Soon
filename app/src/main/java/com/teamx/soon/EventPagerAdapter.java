package com.teamx.soon;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamx.soon.item.Event;

public class EventPagerAdapter extends FragmentPagerAdapter {

    EventDetailFragment eventDetailFragment;

    public EventPagerAdapter(FragmentManager fm, Event event) {
        super(fm);
        eventDetailFragment = EventDetailFragment.newInstance(event);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Chi tiết";
            case 1:
                return "Ảnh";
            case 2:
                return "Bình luận";
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return eventDetailFragment;
            case 1:
                return PlaceholderFragment.newInstance(position + 1);
            case 2:
                return PlaceholderFragment.newInstance(position + 1);
        }
        return null;
    }

//    @Override
//    public Fragment instantiateItem(ViewGroup container, int position) {
//        Fragment f = (Fragment) super.instantiateItem(container, position);
//        switch (position) {
//            case 0:
//                mISTableFragment = (ISTableFragment) f;
//                break;
//            case 1:
//                mISListFragment = (ISListFragment) f;
//                break;
//            case 2:
//                mISStandardFragment = (ISStandardFragment) f;
//                break;
//        }
//        return f;
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText("Sẽ sớm xuất hiện");
            return rootView;
        }
    }
}