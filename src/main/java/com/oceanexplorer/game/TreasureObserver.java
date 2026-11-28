package com.oceanexplorer.game;

public class TreasureObserver implements GameObserver {

    private final Game game;

    public TreasureObserver(Game game) {
        this.game = game;
    }

    @Override
    public void onGameEvent(String event) {

        if (event.equals("TREASURE_FOUND")) {
            game.setPhase(new WonPhase());
        }
    }
}
