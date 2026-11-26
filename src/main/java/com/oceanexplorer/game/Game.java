
package com.oceanexplorer.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private int gridSize;
    private int islandCount;
    private int pirateCount;
    private int seaMonsterCount;

    private PirateStrategy pirateStrategy;

    private OceanMap oceanMap;
    private Ship ship;

    private final List<PirateShip> pirates = new ArrayList<>();
    private final List<SeaMonster> seaMonsters = new ArrayList<>();

    public Game() {
        gridSize = 10;
        islandCount = 10;
        pirateCount = 2;
        seaMonsterCount = 0;
        pirateStrategy = new ChaseStrategy();

        initializeGame();
    }

    public Game(GameConfig config) {
        gridSize = config.getGridSize();
        islandCount = config.getIslandCount();
        pirateCount = config.getPirateCount();
        seaMonsterCount = config.getSeaMonsterCount();
        pirateStrategy = createPirateStrategy(config.getPirateStrategy());

        initializeGame();
    }

    private PirateStrategy createPirateStrategy(String strategy) {
        switch (strategy.toLowerCase()) {
            case "chase":
                return new ChaseStrategy();
            case "random":
                return new RandomStrategy();
            case "smart":
                return new SmartStrategy();
            default:
                throw new IllegalArgumentException("Invalid pirate strategy: " + strategy);
        }
    }

    private void initializeGame() {

        // Get the shared Singleton map
        oceanMap = OceanMap.getInstance(gridSize);
        oceanMap.initializeMap();
        oceanMap.generateIslands(islandCount);

        ship = new Ship(oceanMap.getMap(), gridSize);

        pirates.clear();
        seaMonsters.clear();

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

        // Create the requested number of sea monsters
        for (int i = 0; i < seaMonsterCount; i++) {

            SeaMonster seaMonster = new SeaMonster(
                    oceanMap.getMap(),
                    occupiedLocations,
                    gridSize
            );

            seaMonsters.add(seaMonster);
            occupiedLocations.add(seaMonster.getPosition());
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

    public List<SeaMonster> getSeaMonsters() {
        return seaMonsters;
    }

    
    public GameState executeCommand(GameCommand command) {
        // Execute the player's command
        command.execute(this);

        // Get the player's updated location
        Point playerLocation = ship.getPosition();

        // Move pirates using the selected strategy
        for (PirateShip pirate : pirates) {
            pirateStrategy.move(pirate, playerLocation);
        }

        // Move sea monsters randomly
        for (SeaMonster seaMonster : seaMonsters) {
            seaMonster.moveRandomly();
        }

        return new GameState(this);
    }

    public void movePlayer(String direction) {
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
    }

    // Keep this temporarily for compatibility with the existing server
    public GameState move(String direction) {
        return executeCommand(new MoveCommand(direction));
    }

}
