
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.List;

public class GameState {

    private final boolean[][] map;

    private final int shipX;
    private final int shipY;

    // Each entry contains a pirate's [x, y] coordinates
    private final int[][] piratePositions;

    // Sea moonster [x, y] coordinates
    private final int[][] seaMonsterPositions;

    private final int dimensions;

    public GameState(Game game) {

        map = game.getOceanMap().getMap();

        Point shipLocation = game.getShip().getPosition();
        shipX = shipLocation.x;
        shipY = shipLocation.y;

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
    }

    public boolean[][] getMap() {
        return map;
    }

    public int getShipX() {
        return shipX;
    }

    public int getShipY() {
        return shipY;
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
