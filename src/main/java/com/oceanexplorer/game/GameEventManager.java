package com.oceanexplorer.game;

import java.util.ArrayList;
import java.util.List;

public class GameEventManager {

    private final List<GameObserver> observers = new ArrayList<>();

    // Register an observer
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    // Remove an observer
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers
    public void notifyObservers(String event) {
        for (GameObserver observer : observers) {
            observer.onGameEvent(event);
        }
    }
}