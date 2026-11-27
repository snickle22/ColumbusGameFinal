package com.oceanexplorer.game;

public class LostPhase implements GamePhase {

    @Override
    public void handleMove(Game game, GameCommand command) {
        // Ignore movement commands after losing
    }

    @Override
    public String getName() {
        return "Lost";
    }
}