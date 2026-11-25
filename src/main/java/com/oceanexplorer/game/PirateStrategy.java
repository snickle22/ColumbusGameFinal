package com.oceanexplorer.game;

import java.awt.Point;

public interface PirateStrategy {
    void move(PirateShip pirate, Point playerLocation);
}
