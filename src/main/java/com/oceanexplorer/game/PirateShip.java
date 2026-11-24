
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.Random;

public class PirateShip {

    private Point position;
    private final boolean[][] oceanGrid;
    private final int dimensions;
    private final Random random = new Random();

    public PirateShip(boolean[][] oceanGrid, Point playerLocation,
                      Point otherPirateLocation, int dimensions) {

        this.oceanGrid = oceanGrid;
        this.dimensions = dimensions;

        setStartPosition(playerLocation, otherPirateLocation);
    }

    private void setStartPosition(Point playerLocation,
                                  Point otherPirateLocation) {

        int x;
        int y;

        do {
            x = random.nextInt(dimensions);
            y = random.nextInt(dimensions);

        } while (!oceanGrid[x][y]
                || new Point(x, y).equals(playerLocation)
                || (otherPirateLocation != null
                && new Point(x, y).equals(otherPirateLocation)));

        position = new Point(x, y);
    }

    public Point getShipLocation() {
        return new Point(position);
    }

    public void chase(Point playerLocation) {

        // Chase horizontally
        if (position.x < playerLocation.x) {
            goEast();
        }
        else if (position.x > playerLocation.x) {
            goWest();
        }

        // Chase vertically
        if (position.y < playerLocation.y) {
            goSouth();
        }
        else if (position.y > playerLocation.y) {
            goNorth();
        }
    }

    public void goEast() {

        if (position.x < dimensions - 1 &&
                oceanGrid[position.x + 1][position.y]) {

            position.x++;
        }
    }

    public void goWest() {

        if (position.x > 0 &&
                oceanGrid[position.x - 1][position.y]) {

            position.x--;
        }
    }

    public void goNorth() {

        if (position.y > 0 &&
                oceanGrid[position.x][position.y - 1]) {

            position.y--;
        }
    }

    public void goSouth() {

        if (position.y < dimensions - 1 &&
                oceanGrid[position.x][position.y + 1]) {

            position.y++;
        }
    }
}

