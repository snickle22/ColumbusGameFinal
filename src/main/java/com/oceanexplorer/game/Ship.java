
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

        do {
            x = random.nextInt(dimensions);
            y = random.nextInt(dimensions);
        } while (!map[x][y]);

        position = new Point(x, y);
    }

    public Point getPosition() {
        return new Point(position);
    }

    // Restore the ship's previous position (used by Undo)
    public void setPosition(Point position) {
        this.position = new Point(position);
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

        if (newX < 0 || newX >= dimensions ||
            newY < 0 || newY >= dimensions) {
            return;
        }

        if (map[newX][newY]) {
            position.setLocation(newX, newY);
        }
    }
}
