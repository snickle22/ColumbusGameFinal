
package com.oceanexplorer.game;

public class PlayingPhase implements GamePhase {

    @Override
    public void handleMove(Game game, GameCommand command) {

        // Execute the player's movement
        command.execute(game);

        // Check whether the player reached the treasure
        if (game.checkTreasure()) {
            return;
        }

        // Check whether the player collided with an enemy
        if (game.checkCollisions()) {
            return;
        }

        // Move pirates and sea monsters
        game.moveEnemies();

        // Check for collisions after enemies move
        game.checkCollisions();
    }

    @Override
    public String getName() {
        return "Playing";
    }
}
