package com.merann.smamonov.meraimageviewer.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.merann.smamonov.meraimageviewer.R;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final SplashScreenActivity intend_context = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent open_home_activity_intent = new Intent(intend_context, HomeScreenActivity.class);
                intend_context.finish();
                startActivity(open_home_activity_intent);
            }
        }, 1000);
    }
}
