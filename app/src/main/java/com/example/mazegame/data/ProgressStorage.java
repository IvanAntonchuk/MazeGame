package com.example.mazegame.data;

import android.content.Context;
import android.content.SharedPreferences;

import timber.log.Timber;

public class ProgressStorage {

    private static final String PREFS_NAME = "maze_progress";
    private static final String KEY_COMPLETED_PREFIX = "completed_";

    private final SharedPreferences prefs;

    public ProgressStorage(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isCompleted(int level) {
        return prefs.getBoolean(KEY_COMPLETED_PREFIX + level, false);
    }

    public void markCompleted(int level) {
        prefs.edit().putBoolean(KEY_COMPLETED_PREFIX + level, true).apply();
        Timber.i("Level %d marked as completed", level);
    }

    public void reset() {
        prefs.edit().clear().apply();
        Timber.w("Progress reset");
    }
}