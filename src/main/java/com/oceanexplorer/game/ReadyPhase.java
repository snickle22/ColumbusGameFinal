
package com.oceanexplorer.game;

public class ReadyPhase implements GamePhase {

    @Override
    public void handleMove(Game game, GameCommand command) {

        // Transition to Playing when the first move is made
        game.setPhase(new PlayingPhase());

        // Handle the first move using the Playing phase
        game.getPhase().handleMove(game, command);
    }

    @Override
    public String getName() {
        return "Ready";
    }
}
