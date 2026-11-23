package com.example.mazegame.view;

import timber.log.Timber;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mazegame.model.Direction;
import com.example.mazegame.model.Maze;
import com.example.mazegame.model.Player;

public class MazeView extends View {

    public interface GameListener {
        void onMovesChanged(int moves);

        void onExitReached(int moves);
    }

    private static final float MIN_SWIPE_DP = 24f;
    private static final long MS_PER_CELL = 50L;

    private final Paint wallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint startPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint exitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint playerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Maze maze;
    private Player player;
    private GameListener listener;
    private ValueAnimator animator;

    private float playerX;
    private float playerY;
    private float touchStartX;
    private float touchStartY;

    public MazeView(Context context) {
        this(context, null);
    }

    public MazeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        wallPaint.setColor(Color.parseColor("#37474F"));
        wallPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setStrokeCap(Paint.Cap.ROUND);
        startPaint.setColor(Color.parseColor("#C8E6C9"));
        exitPaint.setColor(Color.parseColor("#FFE082"));
        playerPaint.setColor(Color.parseColor("#1976D2"));
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
    }

    public void setMaze(Maze maze) {
        stopAnimation();
        this.maze = maze;
        this.player = new Player(maze);
        playerX = player.getX();
        playerY = player.getY();
        if (listener != null) {
            listener.onMovesChanged(0);
        }
        invalidate();
    }

    public void restart() {
        if (player == null) {
            return;
        }
        stopAnimation();
        player.reset();
        playerX = player.getX();
        playerY = player.getY();
        if (listener != null) {
            listener.onMovesChanged(0);
        }
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (maze == null) {
            return;
        }
        int columns = maze.getWidth();
        int rows = maze.getHeight();
        float cellSize = Math.min((float) getWidth() / columns, (float) getHeight() / rows);
        float offsetX = (getWidth() - cellSize * columns) / 2f;
        float offsetY = (getHeight() - cellSize * rows) / 2f;
        wallPaint.setStrokeWidth(Math.max(2f, cellSize * 0.08f));

        drawCellBackground(canvas, maze.getStartX(), maze.getStartY(), cellSize, offsetX, offsetY, startPaint);
        drawCellBackground(canvas, maze.getExitX(), maze.getExitY(), cellSize, offsetX, offsetY, exitPaint);

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                float left = offsetX + x * cellSize;
                float top = offsetY + y * cellSize;
                float right = left + cellSize;
                float bottom = top + cellSize;
                if (maze.getCell(x, y).hasWall(Direction.UP)) {
                    canvas.drawLine(left, top, right, top, wallPaint);
                }
                if (maze.getCell(x, y).hasWall(Direction.DOWN)) {
                    canvas.drawLine(left, bottom, right, bottom, wallPaint);
                }
                if (maze.getCell(x, y).hasWall(Direction.LEFT)) {
                    canvas.drawLine(left, top, left, bottom, wallPaint);
                }
                if (maze.getCell(x, y).hasWall(Direction.RIGHT)) {
                    canvas.drawLine(right, top, right, bottom, wallPaint);
                }
            }
        }

        float centerX = offsetX + (playerX + 0.5f) * cellSize;
        float centerY = offsetY + (playerY + 0.5f) * cellSize;
        canvas.drawCircle(centerX, centerY, cellSize * 0.3f, playerPaint);
    }

    private void drawCellBackground(Canvas canvas, int x, int y, float cellSize,
                                    float offsetX, float offsetY, Paint paint) {
        float left = offsetX + x * cellSize;
        float top = offsetY + y * cellSize;
        canvas.drawRect(left, top, left + cellSize, top + cellSize, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = event.getX();
                touchStartY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                handleSwipe(event.getX() - touchStartX, event.getY() - touchStartY);
                return true;
            default:
                return true;
        }
    }

    private void handleSwipe(float dx, float dy) {
        if (maze == null || player == null || animator != null) {
            return;
        }
        float threshold = getResources().getDisplayMetrics().density * MIN_SWIPE_DP;
        if (Math.max(Math.abs(dx), Math.abs(dy)) < threshold) {
            return;
        }
        Direction direction;
        if (Math.abs(dx) > Math.abs(dy)) {
            direction = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            direction = dy > 0 ? Direction.DOWN : Direction.UP;
        }
        move(direction);
    }

    private void move(Direction direction) {
        final int fromX = player.getX();
        final int fromY = player.getY();
        int steps = player.slide(direction);
        if (steps == 0) {
            Timber.v("Swipe %s blocked at (%d, %d)", direction, fromX, fromY);
            return;
        }
        final int toX = player.getX();
        final int toY = player.getY();
        Timber.d("Swipe %s: %d cells to (%d, %d)", direction, steps, toX, toY);

        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(steps * MS_PER_CELL);
        animator.addUpdateListener(animation -> {
            float t = (float) animation.getAnimatedValue();
            playerX = fromX + (toX - fromX) * t;
            playerY = fromY + (toY - fromY) * t;
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animator = null;
                playerX = toX;
                playerY = toY;
                invalidate();
                if (listener != null) {
                    listener.onMovesChanged(player.getMoves());
                    if (player.isAtExit()) {
                        listener.onExitReached(player.getMoves());
                    }
                }
            }
        });
        animator.start();
    }

    private void stopAnimation() {
        if (animator != null) {
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }
}