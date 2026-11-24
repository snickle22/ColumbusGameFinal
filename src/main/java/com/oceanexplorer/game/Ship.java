package com.oceanexplorer.game;

import java.awt.Point;
import java.util.Random;

public class Ship {

    private final int dimensions;
    private final boolean[][] map;
    private final Random random = new Random();

    private Point position;

    public Ship(boolean[][] map, int dimensions) {
        this.map = map;
        this.dimensions = dimensions;

        placeShip();
    }

    private void placeShip() {

        int x;
        int y;

        // Find a random location that contains water
        do {
            x = random.nextInt(dimensions);
            y = random.nextInt(dimensions);
        } while (!map[x][y]);

        position = new Point(x, y);
    }

    public Point getPosition() {
        return new Point(position);
    }

    public void goNorth() {
        move(0, -1);
    }

    public void goSouth() {
        move(0, 1);
    }

    public void goEast() {
        move(1, 0);
    }

    public void goWest() {
        move(-1, 0);
    }

    private void move(int xChange, int yChange) {

        int newX = position.x + xChange;
        int newY = position.y + yChange;

        // Check that the new position is inside the map
        if (newX < 0 || newX >= dimensions ||
            newY < 0 || newY >= dimensions) {
            return;
        }

        // Only allow movement onto water
        if (map[newX][newY]) {
            position.setLocation(newX, newY);
        }
    }
}
