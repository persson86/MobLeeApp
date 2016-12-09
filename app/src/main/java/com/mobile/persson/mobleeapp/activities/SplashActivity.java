package com.mobile.persson.mobleeapp.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.mobile.persson.mobleeapp.R;

import org.androidannotations.annotations.EActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent it = new Intent(this, MainActivity_.class);
        startActivity(it);
        finish();
    }
}
