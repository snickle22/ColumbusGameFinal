package com.oceanexplorer.game;

public interface GamePhase {

    void handleMove(Game game, GameCommand command);

    String getName();
}