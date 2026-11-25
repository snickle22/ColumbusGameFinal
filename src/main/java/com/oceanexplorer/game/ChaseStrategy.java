
package com.oceanexplorer.game;

import java.awt.Point;

public class ChaseStrategy implements PirateStrategy {

    @Override
    public void move(PirateShip pirate, Point playerLocation) {
        Point pirateLocation = pirate.getShipLocation();

        if (pirateLocation.x < playerLocation.x) {
            pirate.goEast();
        } else if (pirateLocation.x > playerLocation.x) {
            pirate.goWest();
        }

        if (pirateLocation.y < playerLocation.y) {
            pirate.goSouth();
        } else if (pirateLocation.y > playerLocation.y) {
            pirate.goNorth();
        }
    }
}

