package com.oceanexplorer.game;

public class WonPhase implements GamePhase {

    @Override
    public void handleMove(Game game, GameCommand command) {
        // Ignore movement commands after winning
    }

    @Override
    public String getName() {
        return "Won";
    }
}