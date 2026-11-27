package com.oceanexplorer.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private int gridSize;
    private int islandCount;
    private int pirateCount;
    private int seaMonsterCount;

    private OceanMap oceanMap;
    private Ship ship;

    private final List<PirateShip> pirates = new ArrayList<>();
    private final List<SeaMonster> seaMonsters = new ArrayList<>();

    private PirateStrategy pirateStrategy;

    // State pattern
    private GamePhase phase;

    // Default constructor
    public Game() {
        gridSize = 10;
        islandCount = 10;
        pirateCount = 2;
        seaMonsterCount = 0;
        pirateStrategy = new ChaseStrategy();

        initializeGame();
    }

    // Configurable constructor
    public Game(GameConfig config) {
        gridSize = config.getGridSize();
        islandCount = config.getIslandCount();
        pirateCount = config.getPirateCount();
        seaMonsterCount = config.getSeaMonsterCount();

        pirateStrategy = createPirateStrategy(
                config.getPirateStrategy()
        );

        initializeGame();
    }

    // Create the selected pirate movement strategy
    private PirateStrategy createPirateStrategy(String strategy) {

        switch (strategy.toLowerCase()) {
            case "chase":
                return new ChaseStrategy();

            case "random":
                return new RandomStrategy();

            case "smart":
                return new SmartStrategy();

            default:
                return new ChaseStrategy();
        }
    }

    // Initialize the game
    private void initializeGame() {

        oceanMap = OceanMap.getInstance(gridSize);
        oceanMap.initializeMap();
        oceanMap.generateIslands(islandCount);

        ship = new Ship(oceanMap.getMap(), gridSize);

        pirates.clear();
        seaMonsters.clear();

        List<Point> occupiedLocations = new ArrayList<>();
        occupiedLocations.add(ship.getPosition());

        // Create pirates
        for (int i = 0; i < pirateCount; i++) {

            PirateShip pirate = new PirateShip(
                    oceanMap.getMap(),
                    occupiedLocations,
                    gridSize
            );

            pirates.add(pirate);
            occupiedLocations.add(pirate.getShipLocation());
        }

        // Create sea monsters
        for (int i = 0; i < seaMonsterCount; i++) {

            SeaMonster seaMonster = new SeaMonster(
                    oceanMap.getMap(),
                    occupiedLocations,
                    gridSize
            );

            seaMonsters.add(seaMonster);
            occupiedLocations.add(seaMonster.getPosition());
        }

        // State pattern: start in Ready state
        phase = new ReadyPhase();
    }

    // Reset the game
    public void reset() {
        initializeGame();
    }

    // Execute a command through the current game phase
    public GameState executeCommand(GameCommand command) {

        phase.handleMove(this, command);

        return new GameState(this);
    }

    // Move all enemies
    public void moveEnemies() {

        Point playerLocation = ship.getPosition();

        // Move pirates using the selected strategy
        for (PirateShip pirate : pirates) {
            pirateStrategy.move(pirate, playerLocation);
        }

        // Move sea monsters randomly
        for (SeaMonster seaMonster : seaMonsters) {
            seaMonster.moveRandomly();
        }
    }

    // Move the player
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
                throw new IllegalArgumentException(
                        "Invalid movement direction."
                );
        }
    }

    // State pattern: change the current phase
    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    // Get the current phase
    public GamePhase getPhase() {
        return phase;
    }

    // Get the current phase name
    public String getPhaseName() {
        return phase.getName();
    }

    // Getters
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

    // Compatibility wrapper for movement
    public GameState move(String direction) {
        return executeCommand(new MoveCommand(direction));
    }
}