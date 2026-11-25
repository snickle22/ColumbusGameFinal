package com.oceanexplorer.game;

import java.awt.Point;

public class SmartStrategy implements PirateStrategy {

    @Override
    public void move(PirateShip pirate, Point playerLocation) {
        Point pirateLocation = pirate.getShipLocation();

        int horizontalDistance =
                Math.abs(playerLocation.x - pirateLocation.x);

        int verticalDistance =
                Math.abs(playerLocation.y - pirateLocation.y);

        // Try the direction with the larger distance first
        if (horizontalDistance >= verticalDistance) {
            if (playerLocation.x > pirateLocation.x) {
                pirate.goEast();
            } else if (playerLocation.x < pirateLocation.x) {
                pirate.goWest();
            }

            if (pirate.getShipLocation().equals(pirateLocation)) {
                moveVertically(pirate, playerLocation, pirateLocation);
            }
        } else {
            if (playerLocation.y > pirateLocation.y) {
                pirate.goSouth();
            } else if (playerLocation.y < pirateLocation.y) {
                pirate.goNorth();
            }

            if (pirate.getShipLocation().equals(pirateLocation)) {
                moveHorizontally(pirate, playerLocation, pirateLocation);
            }
        }
    }

    private void moveVertically(PirateShip pirate, Point player,
                                Point originalPosition) {
        if (player.y > originalPosition.y) {
            pirate.goSouth();
        } else if (player.y < originalPosition.y) {
            pirate.goNorth();
        }
    }

    private void moveHorizontally(PirateShip pirate, Point player,
                                  Point originalPosition) {
        if (player.x > originalPosition.x) {
            pirate.goEast();
        } else if (player.x < originalPosition.x) {
            pirate.goWest();
        }
    }
}
