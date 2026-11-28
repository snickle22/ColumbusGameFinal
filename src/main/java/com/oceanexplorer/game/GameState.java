
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.List;

public class GameState {

    private final boolean[][] map;
    private final int shipX;
    private final int shipY;
    private final int treasureX;
    private final int treasureY;
    private final int[][] piratePositions;
    private final int[][] seaMonsterPositions;
    private final int dimensions;
    private final String phase;
    private final boolean canUndo;

    public GameState(Game game) {

        map = game.getOceanMap().getMap();

        Point shipLocation = game.getShip().getPosition();
        shipX = shipLocation.x;
        shipY = shipLocation.y;

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

        // Tell the browser whether an Undo is available
        canUndo = game.canUndo();
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

    public String getPhase() {
        return phase;
    }

    public boolean isCanUndo() {
        return canUndo;
    }
}
