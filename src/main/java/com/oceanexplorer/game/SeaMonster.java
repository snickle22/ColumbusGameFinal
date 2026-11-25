package com.oceanexplorer.game;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class SeaMonster {

    private Point position;
    private final boolean[][] oceanGrid;
    private final int dimensions;
    private final Random random = new Random();

    public SeaMonster(boolean[][] oceanGrid, List<Point> occupiedLocations,
                     int dimensions) {
        this.oceanGrid = oceanGrid;
        this.dimensions = dimensions;
        setStartPosition(occupiedLocations);
    }

    private void setStartPosition(List<Point> occupiedLocations) {
        int x, y;

        do {
            x = random.nextInt(dimensions);
            y = random.nextInt(dimensions);
        } while (!oceanGrid[x][y] ||
                 occupiedLocations.contains(new Point(x, y)));

        position = new Point(x, y);
    }

    public Point getPosition() {
        return new Point(position);
    }

    public void moveRandomly() {
        int direction = random.nextInt(4);

        switch (direction) {
            case 0:
                goNorth();
                break;
            case 1:
                goSouth();
                break;
            case 2:
                goEast();
                break;
            case 3:
                goWest();
                break;
        }
    }

    private void goNorth() {
        if (position.y > 0 && oceanGrid[position.x][position.y - 1]) {
            position.y--;
        }
    }

    private void goSouth() {
        if (position.y < dimensions - 1 &&
                oceanGrid[position.x][position.y + 1]) {
            position.y++;
        }
    }

    private void goEast() {
        if (position.x < dimensions - 1 &&
                oceanGrid[position.x + 1][position.y]) {
            position.x++;
        }
    }

    private void goWest() {
        if (position.x > 0 &&
                oceanGrid[position.x - 1][position.y]) {
            position.x--;
        }
    }
}