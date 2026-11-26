package com.oceanexplorer.game;

public class MoveCommand implements GameCommand {

    private final String direction;

    public MoveCommand(String direction) {
        this.direction = direction;
    }

    @Override
    public void execute(Game game) {
        game.movePlayer(direction);
    }
}
