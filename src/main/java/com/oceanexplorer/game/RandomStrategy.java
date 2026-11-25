package com.oceanexplorer.game;

import java.awt.Point;
import java.util.Random;

public class RandomStrategy implements PirateStrategy {
    private final Random random = new Random();

    @Override
    public void move(PirateShip pirate, Point playerLocation) {
        int direction = random.nextInt(4);

        switch (direction) {
            case 0:
                pirate.goNorth();
                break;
            case 1:
                pirate.goSouth();
                break;
            case 2:
                pirate.goEast();
                break;
            case 3:
                pirate.goWest();
                break;
        }
    }

}
