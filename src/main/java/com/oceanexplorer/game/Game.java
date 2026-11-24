
package com.oceanexplorer.game;

import java.awt.Point;

public class Game {

    private static final int GRID_SIZE = 10;
    private static final int ISLAND_COUNT = 10;

    private OceanMap oceanMap;
    private Ship ship;

    private PirateShip pirate1;
    private PirateShip pirate2;

    public Game() {
        initializeGame();
    }

    private void initializeGame() {

        oceanMap = new OceanMap(GRID_SIZE);
        oceanMap.generateIslands(ISLAND_COUNT);

        ship = new Ship(oceanMap.getMap(), GRID_SIZE);

        Point playerLocation = ship.getPosition();

        pirate1 = new PirateShip(
                oceanMap.getMap(),
                playerLocation,
                null,
                GRID_SIZE
        );

        pirate2 = new PirateShip(
                oceanMap.getMap(),
                playerLocation,
                pirate1.getShipLocation(),
                GRID_SIZE
        );
    }

    public void reset() {
        initializeGame();
    }

    public OceanMap getOceanMap() {
        return oceanMap;
    }

    public Ship getShip() {
        return ship;
    }

    public PirateShip getPirate1() {
        return pirate1;
    }

    public PirateShip getPirate2() {
        return pirate2;
    }

    public GameState move(String direction) {

        switch (direction.toLowerCase()) {
            case "north":
                ship.goNorth();
                break;
            case "south":
                ship.goSouth();
                break;
            case "east":
                ship.goEast();
                break;
            case "west":
                ship.goWest();
                break;
            default:
                throw new IllegalArgumentException("Invalid direction.");
        }

        // Get the player's updated position
        Point playerLocation = ship.getPosition();

        // Move both pirates toward the player
        pirate1.chase(playerLocation);
        pirate2.chase(playerLocation);

        return new GameState(this);
    }
}
