package com.example.mazegame.model;

public class Player {
    private final Maze maze;
    private int x;
    private int y;
    private int moves;

    public Player(Maze maze) {
        this.maze = maze;
        reset();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMoves() {
        return moves;
    }

    public boolean isAtExit() {
        return x == maze.getExitX() && y == maze.getExitY();
    }

    public int slide(Direction direction) {
        int steps = 0;
        while (maze.canMove(x, y, direction)) {
            x += direction.getDx();
            y += direction.getDy();
            steps++;
            if (isAtExit() || hasSideOpening(direction)) {
                break;
            }
        }
        if (steps > 0) {
            moves++;
        }
        return steps;
    }

    public void reset() {
        x = maze.getStartX();
        y = maze.getStartY();
        moves = 0;
    }

    private boolean hasSideOpening(Direction direction) {
        for (Direction other : Direction.values()) {
            if (other != direction && other != direction.opposite() && maze.canMove(x, y, other)) {
                return true;
            }
        }
        return false;
    }
}