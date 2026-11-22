package com.example.mazegame.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;

public class MazeGeneratorTest {

    private final MazeGenerator generator = new MazeGenerator();

    @Test
    public void sameSeedProducesSameMaze() {
        Maze first = generator.generate(8, 12, 42L);
        Maze second = generator.generate(8, 12, 42L);
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 12; y++) {
                for (Direction direction : Direction.values()) {
                    assertEquals(
                            first.getCell(x, y).hasWall(direction),
                            second.getCell(x, y).hasWall(direction));
                }
            }
        }
    }

    @Test
    public void everyCellIsReachableFromStart() {
        Maze maze = generator.generate(10, 15, 7L);
        boolean[][] visited = new boolean[maze.getWidth()][maze.getHeight()];
        Deque<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{maze.getStartX(), maze.getStartY()});
        visited[maze.getStartX()][maze.getStartY()] = true;
        int reached = 1;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            for (Direction direction : Direction.values()) {
                if (maze.canMove(current[0], current[1], direction)) {
                    int nx = current[0] + direction.getDx();
                    int ny = current[1] + direction.getDy();
                    if (!visited[nx][ny]) {
                        visited[nx][ny] = true;
                        reached++;
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }
        assertEquals(maze.getWidth() * maze.getHeight(), reached);
    }

    @Test
    public void mazeHasExactlyOnePathBetweenCells() {
        Maze maze = generator.generate(9, 14, 123L);
        int passages = 0;
        for (int x = 0; x < maze.getWidth(); x++) {
            for (int y = 0; y < maze.getHeight(); y++) {
                if (!maze.getCell(x, y).hasWall(Direction.RIGHT)) {
                    passages++;
                }
                if (!maze.getCell(x, y).hasWall(Direction.DOWN)) {
                    passages++;
                }
            }
        }
        assertEquals(maze.getWidth() * maze.getHeight() - 1, passages);
    }

    @Test
    public void outerBordersStayClosed() {
        Maze maze = generator.generate(6, 9, 5L);
        for (int x = 0; x < maze.getWidth(); x++) {
            assertTrue(maze.getCell(x, 0).hasWall(Direction.UP));
            assertTrue(maze.getCell(x, maze.getHeight() - 1).hasWall(Direction.DOWN));
        }
        for (int y = 0; y < maze.getHeight(); y++) {
            assertTrue(maze.getCell(0, y).hasWall(Direction.LEFT));
            assertTrue(maze.getCell(maze.getWidth() - 1, y).hasWall(Direction.RIGHT));
        }
    }
}