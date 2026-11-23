package com.example.mazegame;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.levelsButton).setOnClickListener(v -> openLevels());
        findViewById(R.id.freePlayButton).setOnClickListener(v -> openFreePlay());
        findViewById(R.id.settingsButton).setOnClickListener(v -> openSettings());
    }

    private void openLevels() {
        Timber.d("Levels selected");
        startActivity(new Intent(this, LevelsActivity.class));
    }

    private void openFreePlay() {
        Timber.d("Free play selected");
        startActivity(new Intent(this, GameActivity.class));
    }

    private void openSettings() {
        Timber.d("Settings selected");
        startActivity(new Intent(this, SettingsActivity.class));
    }
}