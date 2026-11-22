package com.example.mazegame;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mazegame.model.MazeGenerator;
import com.example.mazegame.view.MazeView;

public class GameActivity extends AppCompatActivity {

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
        mazeView.setMaze(new MazeGenerator().generate(9, 15, 1L));
    }
}