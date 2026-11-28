package com.oceanexplorer.game;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Game {

    private int gridSize;
    private int islandCount;
    private int pirateCount;
    private int seaMonsterCount;

    private OceanMap oceanMap;
    private Ship ship;
    private Treasure treasure;

    private final List<PirateShip> pirates = new ArrayList<>();
    private final List<SeaMonster> seaMonsters = new ArrayList<>();

    private PirateStrategy pirateStrategy;

    // State pattern
    private GamePhase phase;

    // Observer pattern
    private GameEventManager eventManager = new GameEventManager();

    // Undo history
    private final Deque<GameSnapshot> history = new ArrayDeque<>();

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

        pirateStrategy = createPirateStrategy(
                config.getPirateStrategy()
        );

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
                return new ChaseStrategy();
        }
    }

    private void initializeGame() {

        oceanMap = OceanMap.getInstance(gridSize);
        oceanMap.initializeMap();
        oceanMap.generateIslands(islandCount);

        ship = new Ship(oceanMap.getMap(), gridSize);

        pirates.clear();
        seaMonsters.clear();
        history.clear();

        List<Point> occupiedLocations = new ArrayList<>();
        occupiedLocations.add(ship.getPosition());

        treasure = new Treasure(
                oceanMap.getMap(),
                occupiedLocations,
                gridSize
        );

        occupiedLocations.add(treasure.getLocation());

        for (int i = 0; i < pirateCount; i++) {

            PirateShip pirate = new PirateShip(
                    oceanMap.getMap(),
                    occupiedLocations,
                    gridSize
            );

            pirate.setBlockedLocation(treasure.getLocation());
            pirates.add(pirate);
            occupiedLocations.add(pirate.getShipLocation());
        }

        for (int i = 0; i < seaMonsterCount; i++) {

            SeaMonster seaMonster = new SeaMonster(
                    oceanMap.getMap(),
                    occupiedLocations,
                    gridSize
            );

            seaMonster.setBlockedLocation(treasure.getLocation());
            seaMonsters.add(seaMonster);
            occupiedLocations.add(seaMonster.getPosition());
        }

        phase = new ReadyPhase();

        eventManager = new GameEventManager();
        eventManager.addObserver(new CollisionObserver(this));
        eventManager.addObserver(new TreasureObserver(this));
    }

    public void reset() {
        initializeGame();
    }

    // Execute a command and save the previous turn for Undo
    public GameState executeCommand(GameCommand command) {

        // Do not accept moves after the game has ended
        if (phase.getName().equals("Won")
                || phase.getName().equals("Lost")) {
            return new GameState(this);
        }

        // Save the board before this turn begins
        history.push(new GameSnapshot(this));

        phase.handleMove(this, command);

        return new GameState(this);
    }

    // Undo the most recent turn
    public GameState undo() {

        if (!history.isEmpty()) {
            GameSnapshot snapshot = history.pop();

            // Restore player position
            ship.setPosition(snapshot.getShipPosition());

            // Restore pirate positions
            List<Point> piratePositions =
                    snapshot.getPiratePositions();

            for (int i = 0; i < pirates.size(); i++) {
                pirates.get(i).setPosition(piratePositions.get(i));
            }

            // Restore sea monster positions
            List<Point> monsterPositions =
                    snapshot.getSeaMonsterPositions();

            for (int i = 0; i < seaMonsters.size(); i++) {
                seaMonsters.get(i).setPosition(monsterPositions.get(i));
            }

            // Restore previous game phase
            switch (snapshot.getPhase()) {
                case "Ready":
                    phase = new ReadyPhase();
                    break;
                case "Playing":
                    phase = new PlayingPhase();
                    break;
                case "Won":
                    phase = new WonPhase();
                    break;
                case "Lost":
                    phase = new LostPhase();
                    break;
            }
        }

        return new GameState(this);
    }

    // Check whether Undo is available
    public boolean canUndo() {
        return !history.isEmpty();
    }

    // Get complete game history for playback
    public List<GameSnapshot> getReplayHistory() {

        // Copy existing history
        List<GameSnapshot> replayHistory = new ArrayList<>(history);

        // Deque is LIFO, so reverse the order for playback
        java.util.Collections.reverse(replayHistory);

        // Add the current state to the end of the list
        replayHistory.add(new GameSnapshot(this));

        return replayHistory;
    }

    public void moveEnemies() {

        Point playerLocation = ship.getPosition();

        for (PirateShip pirate : pirates) {
            pirateStrategy.move(pirate, playerLocation);
        }

        for (SeaMonster seaMonster : seaMonsters) {
            seaMonster.moveRandomly();
        }

        checkCollisions();
    }

    public boolean checkCollisions() {

        Point playerLocation = ship.getPosition();

        for (PirateShip pirate : pirates) {

            if (playerLocation.equals(pirate.getShipLocation())) {
                eventManager.notifyObservers("COLLISION");
                return true;
            }
        }

        for (SeaMonster seaMonster : seaMonsters) {

            if (playerLocation.equals(seaMonster.getPosition())) {
                eventManager.notifyObservers("COLLISION");
                return true;
            }
        }

        return false;
    }

    public boolean checkTreasure() {

        Point playerLocation = ship.getPosition();

        if (treasure.isAtLocation(playerLocation)) {
            eventManager.notifyObservers("TREASURE_FOUND");
            return true;
        }

        return false;
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
                throw new IllegalArgumentException(
                        "Invalid movement direction."
                );
        }
    }

    public void setPhase(GamePhase phase) {
        this.phase = phase;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public String getPhaseName() {
        return phase.getName();
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

    public Treasure getTreasure() {
        return treasure;
    }

    public GameState move(String direction) {
        return executeCommand(new MoveCommand(direction));
    }
}
