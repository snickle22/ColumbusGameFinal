package com.oceanexplorer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.oceanexplorer.server.GameServer;

public class Main {

    public static void main(String[] args) throws IOException {

        GameServer server = new GameServer();

        server.start();

        System.out.println("Ocean Explorer server started!");
        System.out.println("Open http://localhost:8080 in your browser.");

        // Keep the main program alive while the server is running
        CountDownLatch shutdown = new CountDownLatch(1);

        try {
            shutdown.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            server.stop();
        }
    }
}
