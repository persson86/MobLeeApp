package com.mobile.persson.mobleeapp;

import android.app.Application;
import android.os.SystemClock;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EApplication;

import java.util.concurrent.TimeUnit;

/**
 * Created by persson on 08/12/16.
 */

@EApplication
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        sleepScreen();
    }

    @Background
    public void sleepScreen(){
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(2));
    }
}