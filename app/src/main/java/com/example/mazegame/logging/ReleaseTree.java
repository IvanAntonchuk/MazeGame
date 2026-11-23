package com.example.mazegame.logging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import timber.log.Timber;

public class ReleaseTree extends Timber.Tree {

    private static final String DEFAULT_TAG = "MazeGame";

    @Override
    protected boolean isLoggable(@Nullable String tag, int priority) {
        return priority >= Log.WARN;
    }

    @Override
    protected void log(int priority, @Nullable String tag, @NonNull String message, @Nullable Throwable t) {
        String safeTag = tag != null ? tag : DEFAULT_TAG;
        Log.println(priority, safeTag, message);
        if (t != null) {
            Log.println(priority, safeTag, Log.getStackTraceString(t));
        }
    }
}