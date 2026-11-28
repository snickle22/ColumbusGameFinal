package com.oceanexplorer.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ChaseStrategyTest {

    // Creates a 10x10 grid with all cells set to open water
    private boolean[][] createOpenOcean() {

        boolean[][] oceanGrid = new boolean[10][10];

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                oceanGrid[x][y] = true;
            }
        }

        return oceanGrid;
    }

    // Creates a pirate at a specific position for predictable testing
    private PirateShip createPirate(Point startPosition) {

        boolean[][] oceanGrid = createOpenOcean();

        PirateShip pirate = new PirateShip(
                oceanGrid,
                new ArrayList<>(),
                10
        );

        pirate.setPosition(startPosition);

        return pirate;
    }

    // 1. Pirate moves east
    @Test
    public void testPirateMovesEast() {

        PirateShip pirate = createPirate(new Point(4, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(6, 4));

        assertEquals(new Point(5, 4), pirate.getShipLocation());
    }

    // 2. Pirate moves west
    @Test
    public void testPirateMovesWest() {

        PirateShip pirate = createPirate(new Point(4, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(2, 4));

        assertEquals(new Point(3, 4), pirate.getShipLocation());
    }

    // 3. Pirate moves south
    @Test
    public void testPirateMovesSouth() {

        PirateShip pirate = createPirate(new Point(4, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(4, 6));

        assertEquals(new Point(4, 5), pirate.getShipLocation());
    }

    // 4. Pirate moves north
    @Test
    public void testPirateMovesNorth() {

        PirateShip pirate = createPirate(new Point(4, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(4, 2));

        assertEquals(new Point(4, 3), pirate.getShipLocation());
    }

    // 5. Pirate does not move when already at player position
    @Test
    public void testPirateDoesNotMoveWhenAtPlayerPosition() {

        PirateShip pirate = createPirate(new Point(4, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(4, 4));

        assertEquals(new Point(4, 4), pirate.getShipLocation());
    }

    // 6. Pirate moves diagonally toward player
    @Test
    public void testPirateMovesDiagonally() {

        PirateShip pirate = createPirate(new Point(4, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(7, 7));

        assertEquals(new Point(5, 5), pirate.getShipLocation());
    }

    // 7. Pirate moves only one space toward a distant player
    @Test
    public void testPirateMovesOnlyOneSpaceTowardPlayer() {

        PirateShip pirate = createPirate(new Point(2, 2));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(8, 2));

        assertEquals(new Point(3, 2), pirate.getShipLocation());
    }

    // 8. Pirate cannot move onto an island
    @Test
    public void testPirateCannotMoveOntoIsland() {

        boolean[][] oceanGrid = createOpenOcean();

        // Place an island directly east of the pirate
        oceanGrid[5][4] = false;

        PirateShip pirate = new PirateShip(
                oceanGrid,
                new ArrayList<>(),
                10
        );

        pirate.setPosition(new Point(4, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(6, 4));

        // Pirate should remain in place because east is blocked
        assertEquals(new Point(4, 4), pirate.getShipLocation());
    }

    // 9. Pirate cannot move beyond the edge of the map
    @Test
    public void testPirateCannotMoveBeyondMapBoundary() {

        PirateShip pirate = createPirate(new Point(9, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        // Player is east of the pirate, beyond the map
        strategy.move(pirate, new Point(10, 4));

        // Pirate should remain at the right edge
        assertEquals(new Point(9, 4), pirate.getShipLocation());
    }

    // 10. Pirate cannot move onto the blocked treasure location
    @Test
    public void testPirateCannotMoveOntoTreasure() {

        PirateShip pirate = createPirate(new Point(4, 4));

        // Block the cell directly east, representing treasure
        pirate.setBlockedLocation(new Point(5, 4));

        ChaseStrategy strategy = new ChaseStrategy();

        strategy.move(pirate, new Point(6, 4));

        // Pirate should not move onto the treasure
        assertEquals(new Point(4, 4), pirate.getShipLocation());
    }
}