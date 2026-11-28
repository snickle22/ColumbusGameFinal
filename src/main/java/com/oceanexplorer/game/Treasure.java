package com.oceanexplorer.game;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class Treasure {

    private Point location;
    private final Random random = new Random();

    public Treasure(
            boolean[][] oceanGrid,
            List<Point> occupiedLocations,
            int dimensions) {

        placeTreasure(oceanGrid, occupiedLocations, dimensions);
    }

    // Place treasure on an unoccupied water location
    private void placeTreasure(
            boolean[][] oceanGrid,
            List<Point> occupiedLocations,
            int dimensions) {

        while (true) {

            int x = random.nextInt(dimensions);
            int y = random.nextInt(dimensions);

            Point candidate = new Point(x, y);

            if (oceanGrid[x][y]
                    && !occupiedLocations.contains(candidate)) {

                location = candidate;
                return;
            }
        }
    }

    // Check whether the player reached the treasure
    public boolean isAtLocation(Point playerLocation) {
        return location.equals(playerLocation);
    }

    // Return a copy of the treasure location
    public Point getLocation() {
        return new Point(location);
    }
}