package com.oceanexplorer.game;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.util.List;

import org.junit.jupiter.api.Test;

class GameTest {

    private final Game game = createEmptyGame();

    // Create a predictable game with no islands or enemies
    private Game createEmptyGame() {
        GameConfig config = new GameConfig(
                10,      // grid size
                0,       // islands
                0,       // pirates
                0,       // sea monsters
                "chase"  // pirate strategy
        );

        return new Game(config);
    }

    // --------------------------------------------------
    // INITIALIZATION TESTS
    // --------------------------------------------------

    @Test
    void gameStartsInReadyPhase() {
        assertEquals("Ready", game.getPhaseName());
    }

    @Test
    void gameInitializesShip() {
        assertNotNull(game.getShip());
        assertNotNull(game.getShip().getPosition());
    }

    @Test
    void gameInitializesTreasure() {
        assertNotNull(game.getTreasure());
        assertNotNull(game.getTreasure().getLocation());
    }

    @Test
    void gameInitializesEmptyEnemyLists() {
        assertTrue(game.getPirates().isEmpty());
        assertTrue(game.getSeaMonsters().isEmpty());
    }

    @Test
    void gameStartsWithNoUndoHistory() {
        assertFalse(game.canUndo());
    }

    // --------------------------------------------------
    // MOVEMENT TESTS
    // --------------------------------------------------

    @Test
    void playerMovesEast() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        assertEquals(
                new Point(6, 5),
                game.getShip().getPosition()
        );
    }

    @Test
    void playerMovesWest() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("west");

        assertEquals(
                new Point(4, 5),
                game.getShip().getPosition()
        );
    }

    @Test
    void playerMovesNorth() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("north");

        assertEquals(
                new Point(5, 4),
                game.getShip().getPosition()
        );
    }

    @Test
    void playerMovesSouth() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("south");

        assertEquals(
                new Point(5, 6),
                game.getShip().getPosition()
        );
    }

    @Test
    void firstMoveChangesPhaseFromReadyToPlaying() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        assertEquals("Playing", game.getPhaseName());
    }

    @Test
    void invalidMovementDirectionThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> game.movePlayer("invalid")
        );

        assertEquals("Invalid movement direction.", exception.getMessage());
    }

    // --------------------------------------------------
    // UNDO TESTS
    // --------------------------------------------------

    @Test
    void movementCreatesUndoHistory() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        assertTrue(game.canUndo());
    }

    @Test
    void undoRestoresPreviousPlayerPosition() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        game.undo();

        assertEquals(
                new Point(5, 5),
                game.getShip().getPosition()
        );
    }

    @Test
    void undoRestoresPreviousPhase() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        game.undo();

        assertEquals("Ready", game.getPhaseName());
    }

    @Test
    void undoRemovesHistoryEntry() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        game.undo();

        assertFalse(game.canUndo());
    }

    @Test
    void undoMultipleMovesRestoresPositionsInOrder() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");
        game.move("south");

        assertEquals(
                new Point(6, 6),
                game.getShip().getPosition()
        );

        game.undo();

        assertEquals(
                new Point(6, 5),
                game.getShip().getPosition()
        );

        game.undo();

        assertEquals(
                new Point(5, 5),
                game.getShip().getPosition()
        );

        assertEquals("Ready", game.getPhaseName());
    }

    @Test
    void undoWithoutHistoryDoesNotChangeGame() {
        game.getShip().setPosition(new Point(5, 5));

        game.undo();

        assertEquals(
                new Point(5, 5),
                game.getShip().getPosition()
        );

        assertEquals("Ready", game.getPhaseName());
    }

    // --------------------------------------------------
    // REPLAY HISTORY TESTS
    // --------------------------------------------------

    @Test
    void replayHistoryContainsInitialSnapshot() {
        List<GameSnapshot> replayHistory =
                game.getReplayHistory();

        assertEquals(1, replayHistory.size());
        assertEquals(
                "Ready",
                replayHistory.get(0).getPhase()
        );
    }

    @Test
    void replayHistoryContainsSnapshotsAfterMoves() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        List<GameSnapshot> replayHistory =
                game.getReplayHistory();

        assertEquals(2, replayHistory.size());
    }

    @Test
    void replayHistoryIsInChronologicalOrder() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        List<GameSnapshot> replayHistory =
                game.getReplayHistory();

        assertEquals(
                new Point(5, 5),
                replayHistory.get(0).getShipPosition()
        );

        assertEquals(
                new Point(6, 5),
                replayHistory.get(1).getShipPosition()
        );
    }

    // --------------------------------------------------
    // TREASURE TESTS
    // --------------------------------------------------

    @Test
    void checkTreasureReturnsFalseWhenPlayerIsNotAtTreasure() {
        game.getShip().setPosition(new Point(5, 5));

        assertFalse(game.checkTreasure());
    }

    @Test
    void checkTreasureReturnsTrueWhenPlayerReachesTreasure() {
        Point treasureLocation =
                game.getTreasure().getLocation();

        game.getShip().setPosition(treasureLocation);

        assertTrue(game.checkTreasure());
    }

    // --------------------------------------------------
    // COLLISION TESTS
    // --------------------------------------------------

    @Test
    void checkCollisionsReturnsFalseWhenThereAreNoEnemies() {
        assertFalse(game.checkCollisions());
    }

    @Test
    void checkCollisionsDetectsPirateCollision() {
        GameConfig config = new GameConfig(
                10,
                0,
                1,
                0,
                "chase"
        );

        Game gameWithPirate = new Game(config);

        Point playerPosition = new Point(5, 5);

        gameWithPirate.getShip()
                .setPosition(playerPosition);

        gameWithPirate.getPirates()
                .get(0)
                .setPosition(playerPosition);

        assertTrue(gameWithPirate.checkCollisions());
    }

    @Test
    void checkCollisionsDetectsSeaMonsterCollision() {
        GameConfig config = new GameConfig(
                10,
                0,
                0,
                1,
                "chase"
        );

        Game gameWithMonster = new Game(config);

        Point playerPosition = new Point(5, 5);

        gameWithMonster.getShip()
                .setPosition(playerPosition);

        gameWithMonster.getSeaMonsters()
                .get(0)
                .setPosition(playerPosition);

        assertTrue(gameWithMonster.checkCollisions());
    }

    // --------------------------------------------------
    // RESET TESTS
    // --------------------------------------------------

    @Test
    void resetReturnsGameToReadyPhase() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        game.reset();

        assertEquals("Ready", game.getPhaseName());
    }

    @Test
    void resetClearsUndoHistory() {
        game.getShip().setPosition(new Point(5, 5));

        game.move("east");

        assertTrue(game.canUndo());

        game.reset();

        assertFalse(game.canUndo());
    }

    @Test
    void resetReinitializesEnemyLists() {
        GameConfig config = new GameConfig(
                10,
                0,
                1,
                1,
                "chase"
        );

        Game gameWithEnemies = new Game(config);

        gameWithEnemies.reset();

        assertEquals(1, gameWithEnemies.getPirates().size());
        assertEquals(1, gameWithEnemies.getSeaMonsters().size());
    }
}