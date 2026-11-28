
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.List;

public class GameState {

    private final boolean[][] map;

    private final int shipX;
    private final int shipY;

    // Treasure coordinates
    private final int treasureX;
    private final int treasureY;

    // Each entry contains a pirate's [x, y] coordinates
    private final int[][] piratePositions;

    // Each entry contains a sea monster's [x, y] coordinates
    private final int[][] seaMonsterPositions;

    private final int dimensions;

    // Game status
    private final String phase;

    public GameState(Game game) {

        map = game.getOceanMap().getMap();

        Point shipLocation = game.getShip().getPosition();
        shipX = shipLocation.x;
        shipY = shipLocation.y;

        // Get treasure coordinates
        Point treasureLocation = game.getTreasure().getLocation();
        treasureX = treasureLocation.x;
        treasureY = treasureLocation.y;

        List<PirateShip> pirates = game.getPirates();

        piratePositions = new int[pirates.size()][2];

        for (int i = 0; i < pirates.size(); i++) {

            Point location = pirates.get(i).getShipLocation();

            piratePositions[i][0] = location.x;
            piratePositions[i][1] = location.y;
        }

        List<SeaMonster> seaMonsters = game.getSeaMonsters();

        seaMonsterPositions = new int[seaMonsters.size()][2];

        for (int i = 0; i < seaMonsters.size(); i++) {

            Point location = seaMonsters.get(i).getPosition();

            seaMonsterPositions[i][0] = location.x;
            seaMonsterPositions[i][1] = location.y;
        }

        dimensions = game.getOceanMap().getDimensions();

        phase = game.getPhaseName();
    }

    public boolean[][] getMap() {
        return map;
    }

    public String getPhase() {
        return phase;
    }

    public int getShipX() {
        return shipX;
    }

    public int getShipY() {
        return shipY;
    }

    public int getTreasureX() {
        return treasureX;
    }

    public int getTreasureY() {
        return treasureY;
    }

    public int[][] getPiratePositions() {
        return piratePositions;
    }

    public int[][] getSeaMonsterPositions() {
        return seaMonsterPositions;
    }

    public int getDimensions() {
        return dimensions;
    }
}
