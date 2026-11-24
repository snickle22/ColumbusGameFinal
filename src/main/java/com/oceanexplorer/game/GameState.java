
package com.oceanexplorer.game;

public class GameState {

    private final boolean[][] map;

    private final int shipX;
    private final int shipY;

    private final int pirate1X;
    private final int pirate1Y;

    private final int pirate2X;
    private final int pirate2Y;

    private final int dimensions;

    public GameState(Game game) {

        this.map = game.getOceanMap().getMap();

        this.shipX = game.getShip().getPosition().x;
        this.shipY = game.getShip().getPosition().y;

        this.pirate1X = game.getPirate1().getShipLocation().x;
        this.pirate1Y = game.getPirate1().getShipLocation().y;

        this.pirate2X = game.getPirate2().getShipLocation().x;
        this.pirate2Y = game.getPirate2().getShipLocation().y;

        this.dimensions = game.getOceanMap().getDimensions();
    }

    public boolean[][] getMap() {
        return map;
    }

    public int getShipX() {
        return shipX;
    }

    public int getShipY() {
        return shipY;
    }

    public int getPirate1X() {
        return pirate1X;
    }

    public int getPirate1Y() {
        return pirate1Y;
    }

    public int getPirate2X() {
        return pirate2X;
    }

    public int getPirate2Y() {
        return pirate2Y;
    }

    public int getDimensions() {
        return dimensions;
    }
}
