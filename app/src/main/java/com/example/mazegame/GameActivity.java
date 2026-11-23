package com.example.mazegame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mazegame.model.Maze;
import com.example.mazegame.model.MazeGenerator;
import com.example.mazegame.view.MazeView;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_WIDTH = "width";
    public static final String EXTRA_HEIGHT = "height";
    public static final String EXTRA_SEED = "seed";

    private static final int DEFAULT_WIDTH = 9;
    private static final int DEFAULT_HEIGHT = 15;

    private TextView movesText;
    private MazeView mazeView;

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

        movesText = findViewById(R.id.movesText);
        mazeView = findViewById(R.id.mazeView);
        mazeView.setListener(new MazeView.GameListener() {
            @Override
            public void onMovesChanged(int moves) {
                movesText.setText(getString(R.string.moves_format, moves));
            }

            @Override
            public void onExitReached(int moves) {
                Toast.makeText(GameActivity.this, R.string.level_complete, Toast.LENGTH_SHORT).show();
            }
        });
        mazeView.setMaze(createMaze(getIntent()));
    }

    private Maze createMaze(Intent intent) {
        int width = intent.getIntExtra(EXTRA_WIDTH, DEFAULT_WIDTH);
        int height = intent.getIntExtra(EXTRA_HEIGHT, DEFAULT_HEIGHT);
        MazeGenerator generator = new MazeGenerator();
        if (intent.hasExtra(EXTRA_SEED)) {
            return generator.generate(width, height, intent.getLongExtra(EXTRA_SEED, 0L));
        }
        return generator.generate(width, height);
    }
}