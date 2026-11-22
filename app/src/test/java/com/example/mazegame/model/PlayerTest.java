package com.example.mazegame.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PlayerTest {

    private Maze corridorMaze() {
        Maze maze = new Maze(4, 2);
        maze.removeWall(0, 0, Direction.RIGHT);
        maze.removeWall(1, 0, Direction.RIGHT);
        maze.removeWall(2, 0, Direction.RIGHT);
        return maze;
    }

    @Test
    public void playerStartsAtStartCell() {
        Maze maze = corridorMaze();
        Player player = new Player(maze);
        assertEquals(maze.getStartX(), player.getX());
        assertEquals(maze.getStartY(), player.getY());
        assertEquals(0, player.getMoves());
    }

    @Test
    public void slideStopsAtWall() {
        Player player = new Player(corridorMaze());
        int steps = player.slide(Direction.RIGHT);
        assertEquals(3, steps);
        assertEquals(3, player.getX());
        assertEquals(0, player.getY());
        assertEquals(1, player.getMoves());
    }

    @Test
    public void slideStopsAtJunction() {
        Maze maze = corridorMaze();
        maze.removeWall(2, 0, Direction.DOWN);
        Player player = new Player(maze);
        int steps = player.slide(Direction.RIGHT);
        assertEquals(2, steps);
        assertEquals(2, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    public void slideIntoWallDoesNotMovePlayerOrCountMove() {
        Player player = new Player(corridorMaze());
        assertEquals(0, player.slide(Direction.UP));
        assertEquals(0, player.slide(Direction.DOWN));
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
        assertEquals(0, player.getMoves());
    }

    @Test
    public void playerReachesExit() {
        Maze maze = new Maze(2, 2);
        maze.removeWall(0, 0, Direction.RIGHT);
        maze.removeWall(1, 0, Direction.DOWN);
        Player player = new Player(maze);
        assertFalse(player.isAtExit());
        player.slide(Direction.RIGHT);
        player.slide(Direction.DOWN);
        assertTrue(player.isAtExit());
        assertEquals(2, player.getMoves());
    }

    @Test
    public void resetReturnsPlayerToStart() {
        Player player = new Player(corridorMaze());
        player.slide(Direction.RIGHT);
        player.reset();
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
        assertEquals(0, player.getMoves());
    }
}