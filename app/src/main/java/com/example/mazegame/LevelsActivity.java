package com.example.mazegame;

import timber.log.Timber;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mazegame.data.ProgressStorage;
import com.example.mazegame.model.LevelConfig;

public class LevelsActivity extends AppCompatActivity {

    private GridLayout levelsGrid;
    private ProgressStorage progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_levels);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        levelsGrid = findViewById(R.id.levelsGrid);
        progress = new ProgressStorage(this);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillGrid();
    }

    private void fillGrid() {
        levelsGrid.removeAllViews();
        int margin = (int) (8 * getResources().getDisplayMetrics().density);
        for (int level = 1; level <= LevelConfig.LEVEL_COUNT; level++) {
            final int selected = level;
            Button button = new Button(this);
            int textRes = progress.isCompleted(level)
                    ? R.string.level_button_done
                    : R.string.level_button;
            button.setText(getString(textRes, level));
            button.setOnClickListener(v -> openLevel(selected));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f));
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.setMargins(margin, margin, margin, margin);
            levelsGrid.addView(button, params);
        }
    }

    private void openLevel(int level) {
        Timber.d("Opening level %d", level);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_LEVEL, level);
        startActivity(intent);
    }
}