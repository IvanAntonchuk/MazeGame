package com.example.mazegame.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LevelConfigTest {

    @Test
    public void mazeSizeGrowsWithLevel() {
        for (int level = 2; level <= LevelConfig.LEVEL_COUNT; level++) {
            assertTrue(LevelConfig.width(level) >= LevelConfig.width(level - 1));
            assertTrue(LevelConfig.height(level) >= LevelConfig.height(level - 1));
        }
        assertTrue(LevelConfig.width(LevelConfig.LEVEL_COUNT) > LevelConfig.width(1));
    }

    @Test
    public void firstAndLastLevelSizes() {
        assertEquals(5, LevelConfig.width(1));
        assertEquals(8, LevelConfig.height(1));
        assertEquals(15, LevelConfig.width(LevelConfig.LEVEL_COUNT));
        assertEquals(25, LevelConfig.height(LevelConfig.LEVEL_COUNT));
    }

    @Test
    public void everyLevelIsIdenticalOnEachGeneration() {
        MazeGenerator generator = new MazeGenerator();
        for (int level = 1; level <= LevelConfig.LEVEL_COUNT; level++) {
            Maze first = generator.generate(
                    LevelConfig.width(level), LevelConfig.height(level), LevelConfig.seed(level));
            Maze second = generator.generate(
                    LevelConfig.width(level), LevelConfig.height(level), LevelConfig.seed(level));
            assertTrue(sameWalls(first, second));
        }
    }

    private boolean sameWalls(Maze first, Maze second) {
        for (int x = 0; x < first.getWidth(); x++) {
            for (int y = 0; y < first.getHeight(); y++) {
                for (Direction direction : Direction.values()) {
                    if (first.getCell(x, y).hasWall(direction)
                            != second.getCell(x, y).hasWall(direction)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}