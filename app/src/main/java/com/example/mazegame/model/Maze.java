package com.example.mazegame.model;

import timber.log.Timber;

public class Maze {
    private final int width;
    private final int height;
    private final Cell[][] cells;

    public Maze(int width, int height) {
        if (width < 2 || height < 2) {
            Timber.e("Invalid maze size %dx%d", width, height);
            throw new IllegalArgumentException("Maze must be at least 2x2");
        }
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public boolean canMove(int x, int y, Direction direction) {
        if (!isInside(x, y) || cells[x][y].hasWall(direction)) {
            return false;
        }
        return isInside(x + direction.getDx(), y + direction.getDy());
    }

    public void removeWall(int x, int y, Direction direction) {
        int nx = x + direction.getDx();
        int ny = y + direction.getDy();
        if (!isInside(x, y) || !isInside(nx, ny)) {
            throw new IllegalArgumentException("Cannot remove wall outside the maze");
        }
        cells[x][y].setWall(direction, false);
        cells[nx][ny].setWall(direction.opposite(), false);
    }

    public int getStartX() {
        return 0;
    }

    public int getStartY() {
        return 0;
    }

    public int getExitX() {
        return width - 1;
    }

    public int getExitY() {
        return height - 1;
    }
}