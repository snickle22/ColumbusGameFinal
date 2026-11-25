
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private int gridSize;
    private int islandCount;
    private int pirateCount;

    private OceanMap oceanMap;
    private Ship ship;

    private final List<PirateShip> pirates = new ArrayList<>();

    public Game() {
        gridSize = 10;
        islandCount = 10;
        pirateCount = 2;

        initializeGame();
    }

    public Game(GameConfig config) {
        gridSize = config.getGridSize();
        islandCount = config.getIslandCount();
        pirateCount = config.getPirateCount();

        initializeGame();
    }

    private void initializeGame() {

        // Get the shared Singleton map
        oceanMap = OceanMap.getInstance(gridSize);
        oceanMap.initializeMap();
        oceanMap.generateIslands(islandCount);

        ship = new Ship(oceanMap.getMap(), gridSize);

        pirates.clear();

        // Track occupied locations so ships don't spawn on one another
        List<Point> occupiedLocations = new ArrayList<>();
        occupiedLocations.add(ship.getPosition());

        // Create the requested number of pirate ships
        for (int i = 0; i < pirateCount; i++) {

            PirateShip pirate = new PirateShip(
                    oceanMap.getMap(),
                    occupiedLocations,
                    gridSize
            );

            pirates.add(pirate);
            occupiedLocations.add(pirate.getShipLocation());
        }
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

    public List<PirateShip> getPirates() {
        return pirates;
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

        // Move every pirate toward the player's position
        Point playerLocation = ship.getPosition();

        for (PirateShip pirate : pirates) {
            pirate.chase(playerLocation);
        }

        return new GameState(this);
    }
}
