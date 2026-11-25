
package com.oceanexplorer.game;

import java.awt.Point;

public class Game {

    private int gridSize;
    private int islandCount;

    private OceanMap oceanMap;
    private Ship ship;

    private PirateShip pirate1;
    private PirateShip pirate2;

    public Game() {
        this.gridSize = 10;
        this.islandCount = 10;

        initializeGame();
    }

    public Game(GameConfig config) {

        this.gridSize = config.getGridSize();
        this.islandCount = config.getIslandCount();

        initializeGame();
    }

    private void initializeGame() {

        // Retrieve the singleton instance of OceanMap
        oceanMap = OceanMap.getInstance(gridSize);

        // Reset map for a new game
        oceanMap.initializeMap();

        // Generate islands
        oceanMap.generateIslands(islandCount);

        ship = new Ship(oceanMap.getMap(), gridSize);

        Point playerLocation = ship.getPosition();

        pirate1 = new PirateShip(
                oceanMap.getMap(),
                playerLocation,
                null,
                gridSize
        );

        pirate2 = new PirateShip(
                oceanMap.getMap(),
                playerLocation,
                pirate1.getShipLocation(),
                gridSize
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

        Point playerLocation = ship.getPosition();

        pirate1.chase(playerLocation);
        pirate2.chase(playerLocation);

        return new GameState(this);
    }
}
