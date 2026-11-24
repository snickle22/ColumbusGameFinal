package com.oceanexplorer.game;

import java.util.Random;

public class OceanMap {

    private final int dimensions;
    private final boolean[][] map;

    private final Random random = new Random();

    public OceanMap(int dimensions) {
        this.dimensions = dimensions;
        this.map = new boolean[dimensions][dimensions];

        // Initialize every location as water
        for (int x = 0; x < dimensions; x++) {
            for (int y = 0; y < dimensions; y++) {
                map[x][y] = true;
            }
        }
    }

    public void generateIslands(int islandCount) {

        if (islandCount < 0 || islandCount > dimensions * dimensions) {
            throw new IllegalArgumentException("Invalid island count.");
        }

        int islandsPlaced = 0;

        while (islandsPlaced < islandCount) {

            int x = random.nextInt(dimensions);
            int y = random.nextInt(dimensions);

            // Only place an island where there is currently water
            if (map[x][y]) {
                map[x][y] = false;
                islandsPlaced++;
            }
        }
    }

    public boolean[][] getMap() {
        return map;
    }

    public int getDimensions() {
        return dimensions;
    }
}