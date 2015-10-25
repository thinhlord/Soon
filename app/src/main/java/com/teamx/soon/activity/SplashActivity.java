package com.teamx.soon.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.teamx.soon.AppDelegate;
import com.teamx.soon.GlobalConst;
import com.teamx.soon.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 1500ms
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AppDelegate.getInstance());
                if (sp.getInt(GlobalConst.SPK_FIRST_TIME, -1) != -1) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                }
                SplashActivity.this.finish();
            }
        }, 1500);
    }
}
