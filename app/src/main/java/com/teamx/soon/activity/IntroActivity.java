package com.teamx.soon.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.teamx.soon.AppDelegate;
import com.teamx.soon.GlobalConst;
import com.teamx.soon.R;

public class IntroActivity extends AppCompatActivity {

    SliderLayout sliderShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        sliderShow = (SliderLayout) findViewById(R.id.slider);

        DefaultSliderView sliderView = new DefaultSliderView(this);
        sliderView.image(R.drawable.event_1);
        sliderShow.addSlider(sliderView);
        sliderView = new DefaultSliderView(this);
        sliderView.image(R.drawable.event_2);
        sliderShow.addSlider(sliderView);
        sliderShow.startAutoCycle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
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

    public void onLoginButtonClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void onLaterButtonClick(View view) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AppDelegate.getInstance()).edit();
        editor.putInt(GlobalConst.SPK_FIRST_TIME, 1);
        editor.apply();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
