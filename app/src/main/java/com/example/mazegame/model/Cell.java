package com.example.mazegame.model;

public class Cell {
    private final int x;
    private final int y;
    private final boolean[] walls = {true, true, true, true};

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasWall(Direction direction) {
        return walls[direction.ordinal()];
    }

    public void setWall(Direction direction, boolean value) {
        walls[direction.ordinal()] = value;
    }
}