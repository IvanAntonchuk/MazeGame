package com.example.mazegame.model;

import timber.log.Timber;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class MazeGenerator {

    public Maze generate(int width, int height, long seed) {
        Timber.d("Generating maze %dx%d with seed %d", width, height, seed);
        Maze maze = new Maze(width, height);
        Random random = new Random(seed);
        boolean[][] visited = new boolean[width][height];
        Deque<Cell> stack = new ArrayDeque<>();

        Cell start = maze.getCell(maze.getStartX(), maze.getStartY());
        visited[start.getX()][start.getY()] = true;
        stack.push(start);

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            List<Direction> options = unvisitedNeighbours(maze, visited, current);
            if (options.isEmpty()) {
                stack.pop();
                continue;
            }
            Direction direction = options.get(random.nextInt(options.size()));
            maze.removeWall(current.getX(), current.getY(), direction);
            Cell next = maze.getCell(
                    current.getX() + direction.getDx(),
                    current.getY() + direction.getDy());
            visited[next.getX()][next.getY()] = true;
            stack.push(next);
        }
        return maze;
    }

    public Maze generate(int width, int height) {
        return generate(width, height, new Random().nextLong());
    }

    private List<Direction> unvisitedNeighbours(Maze maze, boolean[][] visited, Cell cell) {
        List<Direction> result = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            int nx = cell.getX() + direction.getDx();
            int ny = cell.getY() + direction.getDy();
            if (maze.isInside(nx, ny) && !visited[nx][ny]) {
                result.add(direction);
            }
        }
        return result;
    }
}