package com.example.mazegame;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mazegame.data.ProgressStorage;
import com.example.mazegame.model.LevelConfig;
import com.example.mazegame.model.Maze;
import com.example.mazegame.model.MazeGenerator;
import com.example.mazegame.view.MazeView;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_LEVEL = "level";

    private static final int FREE_PLAY = 0;
    private static final int FREE_PLAY_WIDTH = 9;
    private static final int FREE_PLAY_HEIGHT = 15;

    private TextView movesText;
    private MazeView mazeView;
    private ProgressStorage progress;
    private int level;

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
        } else {
            level = getIntent().getIntExtra(EXTRA_LEVEL, FREE_PLAY);
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
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_LEVEL, level);
    }

    private Maze buildMaze() {
        MazeGenerator generator = new MazeGenerator();
        if (level > FREE_PLAY) {
            return generator.generate(
                    LevelConfig.width(level),
                    LevelConfig.height(level),
                    LevelConfig.seed(level));
        }
        return generator.generate(FREE_PLAY_WIDTH, FREE_PLAY_HEIGHT);
    }

    private void showWinDialog(int moves) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.level_complete)
                .setMessage(getString(R.string.moves_format, moves))
                .setCancelable(false);

        if (level > FREE_PLAY) {
            progress.markCompleted(level);
            if (level < LevelConfig.LEVEL_COUNT) {
                builder.setPositiveButton(R.string.next_level, (dialog, which) -> {
                    level++;
                    mazeView.setMaze(buildMaze());
                });
            }
            builder.setNegativeButton(R.string.menu_levels, (dialog, which) -> finish());
        } else {
            builder.setPositiveButton(R.string.new_maze, (dialog, which) ->
                    mazeView.setMaze(buildMaze()));
            builder.setNegativeButton(R.string.to_menu, (dialog, which) -> finish());
        }
        builder.show();
    }
}