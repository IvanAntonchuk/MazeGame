package com.example.mazegame;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mazegame.data.LanguageManager;
import com.example.mazegame.data.ProgressStorage;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioGroup languageGroup = findViewById(R.id.languageGroup);
        languageGroup.check(idForLanguage(LanguageManager.current()));
        languageGroup.setOnCheckedChangeListener((group, checkedId) ->
                LanguageManager.apply(languageForId(checkedId)));

        findViewById(R.id.resetButton).setOnClickListener(v -> confirmReset());
    }

    private int idForLanguage(String language) {
        if (LanguageManager.ENGLISH.equals(language)) {
            return R.id.languageEnglish;
        }
        if (LanguageManager.UKRAINIAN.equals(language)) {
            return R.id.languageUkrainian;
        }
        return R.id.languageSystem;
    }

    private String languageForId(int id) {
        if (id == R.id.languageEnglish) {
            return LanguageManager.ENGLISH;
        }
        if (id == R.id.languageUkrainian) {
            return LanguageManager.UKRAINIAN;
        }
        return LanguageManager.SYSTEM;
    }

    private void confirmReset() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.reset_confirm_title)
                .setMessage(R.string.reset_confirm_message)
                .setPositiveButton(R.string.reset_confirm_yes, (dialog, which) -> {
                    new ProgressStorage(this).reset();
                    Toast.makeText(this, R.string.reset_done, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}