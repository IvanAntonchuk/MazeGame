package com.example.mazegame.model;

public final class LevelConfig {

    public static final int LEVEL_COUNT = 10;

    private LevelConfig() {
    }

    public static int width(int level) {
        return 5 + (level - 1) * 10 / 9;
    }

    public static int height(int level) {
        return width(level) * 5 / 3;
    }

    public static long seed(int level) {
        return 1000L + level;
    }
}