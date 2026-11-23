package com.example.mazegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mazegame.data.LanguageManager;
import com.example.mazegame.data.ProgressStorage;
import com.example.mazegame.model.LevelConfig;
import com.example.mazegame.model.Maze;
import com.example.mazegame.model.MazeGenerator;
import com.example.mazegame.view.MazeView;

import java.util.Random;

import timber.log.Timber;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_LEVEL = "level";

    private static final String STATE_SEED = "seed";
    private static final int FREE_PLAY = 0;
    private static final int FREE_PLAY_WIDTH = 9;
    private static final int FREE_PLAY_HEIGHT = 15;

    private TextView movesText;
    private MazeView mazeView;
    private ProgressStorage progress;
    private int level;
    private long freePlaySeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progress = new ProgressStorage(this);
        if (savedInstanceState != null) {
            level = savedInstanceState.getInt(EXTRA_LEVEL, FREE_PLAY);
            freePlaySeed = savedInstanceState.getLong(STATE_SEED);
            Timber.d("Game screen recreated");
        } else {
            level = getIntent().getIntExtra(EXTRA_LEVEL, FREE_PLAY);
            freePlaySeed = new Random().nextLong();
        }

        movesText = findViewById(R.id.movesText);
        mazeView = findViewById(R.id.mazeView);
        mazeView.setListener(new MazeView.GameListener() {
            @Override
            public void onMovesChanged(int moves) {
                movesText.setText(getString(R.string.moves_format, moves));
            }

            @Override
            public void onExitReached(int moves) {
                showWinDialog(moves);
            }
        });
        mazeView.setMaze(buildMaze());
        Timber.i("Game started: level=%d", level);

        findViewById(R.id.menuButton).setOnClickListener(v -> showGameMenu());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_LEVEL, level);
        outState.putLong(STATE_SEED, freePlaySeed);
    }

    private Maze buildMaze() {
        MazeGenerator generator = new MazeGenerator();
        if (level > FREE_PLAY) {
            return generator.generate(
                    LevelConfig.width(level),
                    LevelConfig.height(level),
                    LevelConfig.seed(level));
        }
        return generator.generate(FREE_PLAY_WIDTH, FREE_PLAY_HEIGHT, freePlaySeed);
    }

    private void showGameMenu() {
        Timber.d("Game menu opened");
        String[] items = {
                getString(R.string.game_menu_resume),
                getString(R.string.game_menu_restart),
                getString(R.string.settings_language),
                getString(R.string.game_menu_main)
        };
        new AlertDialog.Builder(this)
                .setTitle(R.string.game_menu_title)
                .setItems(items, (dialog, which) -> handleMenuChoice(which))
                .show();
    }

    private void handleMenuChoice(int which) {
        if (which == 1) {
            Timber.i("Level restarted from game menu");
            mazeView.restart();
        } else if (which == 2) {
            showLanguageDialog();
        } else if (which == 3) {
            goToMainMenu();
        }
    }

    private void showLanguageDialog() {
        final String[] codes = {
                LanguageManager.SYSTEM,
                LanguageManager.ENGLISH,
                LanguageManager.UKRAINIAN
        };
        String[] names = {
                getString(R.string.language_system),
                getString(R.string.language_english),
                getString(R.string.language_ukrainian)
        };
        String current = LanguageManager.current();
        int checked = 0;
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(current)) {
                checked = i;
            }
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.settings_language)
                .setSingleChoiceItems(names, checked, (dialog, which) -> {
                    dialog.dismiss();
                    LanguageManager.apply(codes[which]);
                })
                .show();
    }

    private void goToMainMenu() {
        Timber.d("Returning to main menu");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void startNextLevel() {
        level++;
        Timber.i("Starting level %d", level);
        mazeView.setMaze(buildMaze());
    }

    private void startNewFreePlayMaze() {
        freePlaySeed = new Random().nextLong();
        Timber.i("New free play maze requested");
        mazeView.setMaze(buildMaze());
    }

    private void showWinDialog(int moves) {
        Timber.i("Exit reached: level=%d, moves=%d", level, moves);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.level_complete)
                .setMessage(getString(R.string.moves_format, moves))
                .setCancelable(false);

        if (level > FREE_PLAY) {
            progress.markCompleted(level);
            if (level < LevelConfig.LEVEL_COUNT) {
                builder.setPositiveButton(R.string.next_level, (dialog, which) -> startNextLevel());
            }
            builder.setNegativeButton(R.string.menu_levels, (dialog, which) -> finish());
        } else {
            builder.setPositiveButton(R.string.new_maze, (dialog, which) -> startNewFreePlayMaze());
            builder.setNegativeButton(R.string.to_menu, (dialog, which) -> finish());
        }
        builder.show();
    }
}