package com.mobile.persson.mobleeapp.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.mobile.persson.mobleeapp.R;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sleepScreen();

        Intent it = new Intent(this, MainActivity_.class);
        startActivity(it);
        finish();
    }

    public void sleepScreen() {
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2));
    }
}
