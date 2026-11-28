
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class SeaMonster {

    private Point position;
    private final boolean[][] oceanGrid;
    private final int dimensions;
    private final Random random = new Random();

    private Point blockedLocation;

    public SeaMonster(boolean[][] oceanGrid, List<Point> occupiedLocations,
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

    public void setBlockedLocation(Point location) {
        blockedLocation = new Point(location);
    }

    public Point getPosition() {
        return new Point(position);
    }

    // Restore the sea monster's previous position (used by Undo)
    public void setPosition(Point position) {
        this.position = new Point(position);
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

    public void moveRandomly() {

        int direction = random.nextInt(4);

        switch (direction) {
            case 0 -> goNorth();
            case 1 -> goSouth();
            case 2 -> goEast();
            case 3 -> goWest();
        }
    }

    private void goNorth() {
        if (canMoveTo(position.x, position.y - 1)) {
            position.y--;
        }
    }

    private void goSouth() {
        if (canMoveTo(position.x, position.y + 1)) {
            position.y++;
        }
    }

    private void goEast() {
        if (canMoveTo(position.x + 1, position.y)) {
            position.x++;
        }
    }

    private void goWest() {
        if (canMoveTo(position.x - 1, position.y)) {
            position.y--;
        }
    }
}
