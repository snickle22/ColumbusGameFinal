
package com.oceanexplorer.game;

import java.util.Random;

public class OceanMap {

    // The single shared instance of OceanMap
    private static OceanMap instance;

    private int dimensions;
    private boolean[][] map;

    private final Random random = new Random();

    // Private constructor prevents other classes from creating OceanMap
    private OceanMap(int dimensions) {
        this.dimensions = dimensions;
        initializeMap();
    }

    // Returns the shared OceanMap instance
    public static OceanMap getInstance(int dimensions) {

        if (instance == null) {
            instance = new OceanMap(dimensions);
        }
        else if (instance.dimensions != dimensions) {
            instance.dimensions = dimensions;
            instance.initializeMap();
        }

        return instance;
    }

    // Initializes the entire map as water
    public void initializeMap() {

        map = new boolean[dimensions][dimensions];

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
