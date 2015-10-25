package com.teamx.soon.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;
import com.teamx.soon.EventListAdapter;
import com.teamx.soon.GlobalConst;
import com.teamx.soon.HttpClient;
import com.teamx.soon.R;
import com.teamx.soon.item.Comment;
import com.teamx.soon.item.Event;
import com.teamx.soon.item.User;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public Drawer drawer;
    AccountHeader header;
    Toolbar toolbar;
    ArrayList<Event> events = new ArrayList<>();
    EventListAdapter eventListAdapter;
    ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Fun data

        eventListView = (ListView) findViewById(R.id.event_list);
        eventListAdapter = new EventListAdapter(this, events);
        eventListView.setAdapter(eventListAdapter);

        HttpClient.getEvent(10, 0, new HttpClient.ListResponse<Event>() {
            @Override
            public void onSuccess(ArrayList<Event> objects) {
                events = objects;
                eventListAdapter = new EventListAdapter(HomeActivity.this, events);
                eventListView.setAdapter(eventListAdapter);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        setupDrawer();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupDrawer() {
        // Init image loader
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext())
                        .load(uri)
                        .placeholder(placeholder)
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context context) {
                return null;
            }
        });

        IDrawerItem logoutTab = null;

        // Init profile
        if (User.getCurrentUser() != null) {
            ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem()
                    .withName(User.getCurrentUser().name)
                    .withEmail(User.getCurrentUser().email)
                    .withIcon(User.getCurrentUser().photoUrl);

            // Init profile header
            if (header == null) {
                header = new AccountHeaderBuilder()
                        .withActivity(this)
                        .withHeaderBackground(R.drawable.side_nav_bar)
                        .addProfiles(profileDrawerItem)
                        .withSelectionListEnabledForSingleProfile(false)
                        .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                            @Override
                            public boolean onClick(View view, IProfile iProfile) {
                                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                                return true;
                            }
                        })
                        .withProfileImagesClickable(true)
                        .build();

                logoutTab = new PrimaryDrawerItem()
                        .withName("Đăng xuất")
                        .withIdentifier(GlobalConst.LOGOUT_ID);
            }
        } else {
            ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem()
                    .withName("Đăng nhập");

            if (header == null) {
                header = new AccountHeaderBuilder()
                        .withActivity(this)
                        .withHeaderBackground(R.drawable.side_nav_bar)
                        .addProfiles(profileDrawerItem)
                        .withSelectionListEnabledForSingleProfile(false)
                        .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                            @Override
                            public boolean onClick(View view, IProfile iProfile) {
                                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                                return true;
                            }
                        })
                        .withProfileImagesClickable(true)
                        .build();
            }
        }

        // Init drawer items
        int drawerItemsCount = GlobalConst.DRAWER_TAB_TYPE.length;
        IDrawerItem[] drawerItems = new IDrawerItem[drawerItemsCount];
        for (int i = 0; i < drawerItemsCount; i++) {
            switch (GlobalConst.DRAWER_TAB_TYPE[i]) {
                case GlobalConst.HEADER:
                    drawerItems[i] = new SectionDrawerItem()
                            .withDivider(false)
                            .withName(GlobalConst.DRAWER_TABS_TEXT[i]);
                    break;
                case GlobalConst.TAB:
                    drawerItems[i] = new PrimaryDrawerItem()
                            .withName(GlobalConst.DRAWER_TABS_TEXT[i])
                            .withIcon(GlobalConst.DRAWER_TABS_ICON[i]);
                    break;
                case GlobalConst.SEPARATOR:
                    drawerItems[i] = new DividerDrawerItem();
                    break;
            }
        }

        // Init drawer
        if (drawer == null) {
            drawer = new DrawerBuilder().withActivity(this)
                    .withTranslucentStatusBar(true)
                    .withToolbar(toolbar)
                    .addDrawerItems(drawerItems)
                    .withActionBarDrawerToggle(true)
                    .withActionBarDrawerToggleAnimated(true)
                    .withDelayOnDrawerClose(-1)
                    .withAccountHeader(header)
                    .withCloseOnClick(true)
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                            if (drawerItem != null && drawerItem.getIdentifier() == GlobalConst.LOGOUT_ID) {
//                                showLogoutDialog();
//                                drawer.setSelection(currentDrawerSelectedPosition, false);
//                                drawer.closeDrawer();
//                            } else {
//                                changeDisplayFragment(position);
//                            }
                            return true;
                        }

                    })
                    .build();
            if (logoutTab != null) drawer.addStickyFooterItem(logoutTab);
        }
    }
}
