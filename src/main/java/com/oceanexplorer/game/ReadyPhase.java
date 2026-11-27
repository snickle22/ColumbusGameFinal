package com.oceanexplorer.game;

public class ReadyPhase implements GamePhase {

    @Override
    public void handleMove(Game game, GameCommand command) {

        // Transition to Playing when the first move is made
        game.setPhase(new PlayingPhase());

        // Execute the player's first move
        command.execute(game);

        // Move enemies after the player's first move
        game.moveEnemies();
    }

    @Override
    public String getName() {
        return "Ready";
    }
}