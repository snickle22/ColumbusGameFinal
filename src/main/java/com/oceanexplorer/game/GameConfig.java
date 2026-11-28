
package com.oceanexplorer.game;

public class GameConfig {

    private final int gridSize;
    private final int islandCount;
    private final int pirateCount;
    private final int seaMonsterCount;
    private final String pirateStrategy;

    public GameConfig(int gridSize, int islandCount,
                     int pirateCount, int seaMonsterCount,
                     String pirateStrategy) {

        this.gridSize = gridSize;
        this.islandCount = islandCount;
        this.pirateCount = pirateCount;
        this.seaMonsterCount = seaMonsterCount;
        this.pirateStrategy = pirateStrategy;
    }

    public int getGridSize() {
        return gridSize;
    }

    public int getIslandCount() {
        return islandCount;
    }

    public int getPirateCount() {
        return pirateCount;
    }

    public int getSeaMonsterCount() {
        return seaMonsterCount;
    }

    public String getPirateStrategy() {
        return pirateStrategy;
    }
}
