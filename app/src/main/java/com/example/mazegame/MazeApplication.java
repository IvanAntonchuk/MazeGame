package com.example.mazegame;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.example.mazegame.logging.ReleaseTree;

import timber.log.Timber;

public class MazeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebuggable()) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseTree());
        }
        Timber.i("Application started, debuggable=%b", isDebuggable());
    }

    private boolean isDebuggable() {
        return (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}