
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class PirateShip {

    private Point position;
    private final boolean[][] oceanGrid;
    private final int dimensions;
    private final Random random = new Random();

    // Location pirates are not allowed to enter
    private Point blockedLocation;

    public PirateShip(boolean[][] oceanGrid, List<Point> occupiedLocations,
                      int dimensions) {

        this.oceanGrid = oceanGrid;
        this.dimensions = dimensions;

        setStartPosition(occupiedLocations);
    }

    private void setStartPosition(List<Point> occupiedLocations) {

        int x;
        int y;

        do {
            x = random.nextInt(dimensions);
            y = random.nextInt(dimensions);

        } while (!oceanGrid[x][y]
                || occupiedLocations.contains(new Point(x, y)));

        position = new Point(x, y);
    }

    // Set the treasure location that pirates cannot enter
    public void setBlockedLocation(Point location) {
        blockedLocation = new Point(location);
    }

    public Point getShipLocation() {
        return new Point(position);
    }

    private boolean canMoveTo(int x, int y) {

        Point destination = new Point(x, y);

        return x >= 0
                && x < dimensions
                && y >= 0
                && y < dimensions
                && oceanGrid[x][y]
                && (blockedLocation == null
                    || !destination.equals(blockedLocation));
    }

    public void goEast() {
        if (canMoveTo(position.x + 1, position.y)) {
            position.x++;
        }
    }

    public void goWest() {
        if (canMoveTo(position.x - 1, position.y)) {
            position.x--;
        }
    }

    public void goNorth() {
        if (canMoveTo(position.x, position.y - 1)) {
            position.y--;
        }
    }

    public void goSouth() {
        if (canMoveTo(position.x, position.y + 1)) {
            position.y++;
        }
    }
}
