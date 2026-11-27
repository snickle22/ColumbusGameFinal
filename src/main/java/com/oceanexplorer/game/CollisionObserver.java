package com.oceanexplorer.game;

public class CollisionObserver implements GameObserver {

    private final Game game;

    public CollisionObserver(Game game) {
        this.game = game;
    }

    @Override
    public void onGameEvent(String event) {

        if (event.equals("COLLISION")) {
            game.setPhase(new LostPhase());
        }
    }
}