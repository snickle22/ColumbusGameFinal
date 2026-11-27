package com.oceanexplorer.game;

public class PlayingPhase implements GamePhase {

    @Override
    public void handleMove(Game game, GameCommand command) {
        // Execute the player's movement command
        command.execute(game);

        // Complete the rest of the turn
        game.moveEnemies();
    }

    @Override
    public String getName() {
        return "Playing";
    }
}