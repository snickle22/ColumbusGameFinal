
package com.oceanexplorer.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.gson.Gson;
import com.oceanexplorer.game.Game;
import com.oceanexplorer.game.GameCommand;
import com.oceanexplorer.game.GameConfig;
import com.oceanexplorer.game.GameState;
import com.oceanexplorer.game.MoveCommand;

import fi.iki.elonen.NanoHTTPD;

public class GameServer extends NanoHTTPD {

    private Game game;
    private final Gson gson = new Gson();

    public GameServer() {
        super(8080);
    }

    @Override
    public Response serve(IHTTPSession session) {

        String uri = session.getUri();

        // Start a new game using the menu configuration
        if (uri.equals("/api/start") && session.getMethod() == Method.POST) {

            try {
                Map<String, String> body = new java.util.HashMap<>();

                session.parseBody(body);

                String requestBody = body.get("postData");

                GameConfig config = gson.fromJson(requestBody, GameConfig.class);   
                game = new Game(config);

                GameState gameState = new GameState(game);

                return newFixedLengthResponse(
                        Response.Status.OK,
                        "application/json",
                        gson.toJson(gameState)
                );
            } catch (IOException | ResponseException e) {

                return newFixedLengthResponse(
                        Response.Status.INTERNAL_ERROR,
                        "text/plain",
                        "Error processing start game request."
                );

            } catch (IllegalArgumentException e) {

                return newFixedLengthResponse(
                        Response.Status.BAD_REQUEST,
                        "text/plain",
                        "Invalid game configuration."
                );
            }
        }


        // Return the current game state
        if (uri.equals("/api/state") &&
                session.getMethod() == Method.GET) {

            GameState gameState = new GameState(game);

            return newFixedLengthResponse(
                    Response.Status.OK,
                    "application/json",
                    gson.toJson(gameState)
            );
        }

        // Handle ship movement
        if (uri.equals("/api/move") &&
                session.getMethod() == Method.POST) {

            try {
                Map<String, String> body = new java.util.HashMap<>();

                session.parseBody(body);

                String requestBody = body.get("postData");

                Map<String, String> request =
                        gson.fromJson(requestBody, Map.class);

                String direction = request.get("direction");

                GameCommand command = new MoveCommand(direction);
                GameState gameState = game.executeCommand(command);

                return newFixedLengthResponse(
                        Response.Status.OK,
                        "application/json",
                        gson.toJson(gameState)
                );

            } catch (IOException | ResponseException e) {

                return newFixedLengthResponse(
                        Response.Status.INTERNAL_ERROR,
                        "text/plain",
                        "Error processing movement request."
                );

            } catch (IllegalArgumentException e) {

                return newFixedLengthResponse(
                        Response.Status.BAD_REQUEST,
                        "text/plain",
                        "Invalid movement direction."
                );
            }
        }

        // Serve the HTML, CSS, and JavaScript files
        String resourcePath;
        String contentType;

        if (uri.equals("/") || uri.equals("/index.html")) {
            resourcePath = "/public/index.html";
            contentType = "text/html; charset=UTF-8";
        }
        else if (uri.equals("/style.css")) {
            resourcePath = "/public/style.css";
            contentType = "text/css; charset=UTF-8";
        }
        else if (uri.equals("/script.js")) {
            resourcePath = "/public/script.js";
            contentType = "application/javascript; charset=UTF-8";
        }
        else if (uri.startsWith("/images/")) {
            resourcePath = "/public" + uri;

            if (uri.endsWith(".jpg") || uri.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (uri.endsWith(".png")) {
                contentType = "image/png";
            } else {
                return newFixedLengthResponse(
                        Response.Status.NOT_FOUND,
                        "text/plain",
                        "Unsupported image type"
                );
            }
        }
        else {
            return newFixedLengthResponse(
                    Response.Status.NOT_FOUND,
                    "text/plain",
                    "File not found"
            );
        }

        try (InputStream file = getClass().getResourceAsStream(resourcePath)) {

            if (file == null) {
                return newFixedLengthResponse(
                        Response.Status.NOT_FOUND,
                        "text/plain",
                        "Resource not found: " + resourcePath
                );
            }

            byte[] content = file.readAllBytes();

            return newFixedLengthResponse(
                    Response.Status.OK,
                    contentType,
                    new java.io.ByteArrayInputStream(content),
                    content.length
            );

        } catch (IOException e) {

            return newFixedLengthResponse(
                    Response.Status.INTERNAL_ERROR,
                    "text/plain",
                    "Error loading resource"
            );
        }
    }
}
