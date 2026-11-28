package com.oceanexplorer.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class GameSnapshotTest {

    // 1. Snapshot saves the player's position
    @Test
    public void testSnapshotSavesPlayerPosition() {

        Game game = new Game();
        GameSnapshot snapshot = new GameSnapshot(game);

        assertEquals(
                game.getShip().getPosition(),
                snapshot.getShipPosition()
        );
    }

    // 2. Snapshot saves pirate positions
    @Test
    public void testSnapshotSavesPiratePositions() {

        Game game = new Game();
        GameSnapshot snapshot = new GameSnapshot(game);

        List<Point> expectedPositions = new ArrayList<>();

        for (PirateShip pirate : game.getPirates()) {
            expectedPositions.add(pirate.getShipLocation());
        }

        assertEquals(
                expectedPositions,
                snapshot.getPiratePositions()
        );
    }

    // 3. Snapshot saves sea monster positions
    @Test
    public void testSnapshotSavesSeaMonsterPositions() {

        GameConfig config = new GameConfig(
                10, 10, 2, 1, "chase"
        );

        Game game = new Game(config);
        GameSnapshot snapshot = new GameSnapshot(game);

        List<Point> expectedPositions = new ArrayList<>();

        for (SeaMonster monster : game.getSeaMonsters()) {
            expectedPositions.add(monster.getPosition());
        }

        assertEquals(
                expectedPositions,
                snapshot.getSeaMonsterPositions()
        );
    }

    // 4. Snapshot saves the game phase
    @Test
    public void testSnapshotSavesGamePhase() {

        Game game = new Game();
        GameSnapshot snapshot = new GameSnapshot(game);

        assertEquals("Ready", snapshot.getPhase());
    }

    // 5. Position getter returns an independent Point
    @Test
    public void testPositionGetterReturnsIndependentCopy() {

        Game game = new Game();
        GameSnapshot snapshot = new GameSnapshot(game);

        Point firstPosition = snapshot.getShipPosition();
        Point secondPosition = snapshot.getShipPosition();

        assertNotSame(firstPosition, secondPosition);
        assertEquals(firstPosition, secondPosition);
    }

    // 6. Snapshot preserves the original ship position
    // even if the game changes afterward
    @Test
    public void testSnapshotPreservesOriginalShipPosition() {

        Game game = new Game();
        GameSnapshot snapshot = new GameSnapshot(game);

        Point originalPosition = snapshot.getShipPosition();

        game.getShip().setPosition(new Point(0, 0));

        assertEquals(
                originalPosition,
                snapshot.getShipPosition()
        );
    }

    // 7. Removing items from returned pirate list
    // does not change the snapshot's stored list
    @Test
    public void testPirateListIsIndependent() {

        Game game = new Game();
        GameSnapshot snapshot = new GameSnapshot(game);

        List<Point> returnedPositions =
                snapshot.getPiratePositions();

        int originalSize = returnedPositions.size();

        returnedPositions.clear();

        assertEquals(
                originalSize,
                snapshot.getPiratePositions().size()
        );
    }

    // 8. Removing items from returned sea monster list
    // does not change the snapshot's stored list
    @Test
    public void testSeaMonsterListIsIndependent() {

        GameConfig config = new GameConfig(
                10, 10, 2, 2, "chase"
        );

        Game game = new Game(config);
        GameSnapshot snapshot = new GameSnapshot(game);

        List<Point> returnedPositions =
                snapshot.getSeaMonsterPositions();

        int originalSize = returnedPositions.size();

        returnedPositions.clear();

        assertEquals(
                originalSize,
                snapshot.getSeaMonsterPositions().size()
        );
    }

    // 9. Snapshot preserves the Playing phase
    @Test
    public void testSnapshotSavesPlayingPhase() {

        Game game = new Game();

        game.setPhase(new PlayingPhase());

        GameSnapshot snapshot = new GameSnapshot(game);

        assertEquals("Playing", snapshot.getPhase());
    }

    // 10. Changing the returned pirate Point
    // should not change the snapshot's stored Point
    @Test
    public void testPiratePositionGetterReturnsIndependentPoints() {

        Game game = new Game();
        GameSnapshot snapshot = new GameSnapshot(game);

        List<Point> returnedPositions =
                snapshot.getPiratePositions();

        assertTrue(!returnedPositions.isEmpty());

        Point originalSnapshotPosition =
                snapshot.getPiratePositions().get(0);

        // Change the Point returned to the test
        returnedPositions.get(0).setLocation(99, 99);

        // The snapshot should still hold its original position
        assertEquals(
                originalSnapshotPosition,
                snapshot.getPiratePositions().get(0)
        );
    }
}