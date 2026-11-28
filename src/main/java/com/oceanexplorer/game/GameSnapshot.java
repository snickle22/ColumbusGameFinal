
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameSnapshot {

    private final Point shipPosition;
    private final List<Point> piratePositions;
    private final List<Point> seaMonsterPositions;
    private final String phase;

    public GameSnapshot(Game game) {

        // Save player position
        shipPosition = game.getShip().getPosition();

        // Save every pirate position
        piratePositions = new ArrayList<>();

        for (PirateShip pirate : game.getPirates()) {
            piratePositions.add(pirate.getShipLocation());
        }

        // Save every sea monster position
        seaMonsterPositions = new ArrayList<>();

        for (SeaMonster monster : game.getSeaMonsters()) {
            seaMonsterPositions.add(monster.getPosition());
        }

        // Save current game phase
        phase = game.getPhaseName();
    }

    public Point getShipPosition() {
        return new Point(shipPosition);
    }

    public List<Point> getPiratePositions() {
        return new ArrayList<>(piratePositions);
    }

    public List<Point> getSeaMonsterPositions() {
        return new ArrayList<>(seaMonsterPositions);
    }

    public String getPhase() {
        return phase;
    }
}
