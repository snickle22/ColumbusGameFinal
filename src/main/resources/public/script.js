
console.log("Ocean Explorer JavaScript is running!");

// Store the settings used for the current game
let lastGameConfig = null;

// Tracks whether the previous game is being replayed
let isWatchingReplay = false;

document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("start-game-button")
        .addEventListener("click", () => startGame());

    document.getElementById("replay-button")
        .addEventListener("click", replayGame);

    document.getElementById("change-settings-button")
        .addEventListener("click", changeSettings);

    document.getElementById("watch-game-button")
        .addEventListener("click", watchGame);

    // Undo button
    document.getElementById("undo-button")
        .addEventListener("click", undoMove);

    // Register keyboard controls only once
    document.addEventListener("keydown", handleKeyPress);
});

// Get the selected game settings
function getGameConfig() {

    return {
        gridSize: Number(document.getElementById("grid-size").value),
        islandCount: Number(document.getElementById("island-count").value),
        pirateCount: Number(document.getElementById("pirate-count").value),
        seaMonsterCount: Number(document.getElementById("monster-count").value),
        pirateStrategy: document.getElementById("pirate-strategy").value
    };
}

// Start a new game
async function startGame(config = getGameConfig()) {

    try {

        lastGameConfig = { ...config };

        document.getElementById("end-game-modal").style.display = "none";

        const response = await fetch("/api/start", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(config)
        });

        if (!response.ok) {
            throw new Error("Failed to start the game.");
        }

        const gameState = await response.json();

        document.getElementById("start-menu").style.display = "none";
        document.getElementById("game-screen").style.display = "block";

        displayGame(gameState);

    } catch (error) {

        console.error("Error starting game:", error);
        alert("Could not start the game. Please try again.");
    }
}

// Replay using the same settings
function replayGame() {

    if (lastGameConfig !== null) {
        startGame(lastGameConfig);
    }
}

// Return to the start menu
function changeSettings() {

    document.getElementById("end-game-modal").style.display = "none";
    document.getElementById("game-screen").style.display = "none";

    document.getElementById("start-menu").style.display = "block";

    document.getElementById("game-grid").innerHTML = "";

    document.getElementById("server-message").textContent =
        "Set up your voyage before setting sail!";
}

// Load existing game state
async function loadGameState() {

    try {

        const response = await fetch("/api/state");

        if (!response.ok) {
            throw new Error("Failed to load game state.");
        }

        const gameState = await response.json();

        console.log("Game state received from Java:", gameState);

        displayGame(gameState);

    } catch (error) {

        console.error("Error loading game:", error);

        document.getElementById("server-message").textContent =
            "Could not connect to the game server.";
    }
}

// Display the game board and current game status
function displayGame(gameState, showPopup = true) {

    const grid = document.getElementById("game-grid");
    grid.innerHTML = "";

    grid.style.gridTemplateColumns =
        `repeat(${gameState.dimensions}, 50px)`;

    grid.style.gridTemplateRows =
        `repeat(${gameState.dimensions}, 50px)`;

    // Update Undo button
    const undoButton = document.getElementById("undo-button");

    undoButton.disabled = isWatchingReplay || !gameState.canUndo;

    // Display game status
    const serverMessage = document.getElementById("server-message");

    if (gameState.phase === "Won") {

        serverMessage.textContent =
            "Congratulations! You found the treasure and won the game!";

        if (showPopup) {
            showEndGamePopup("Won");
        } else {
            document.getElementById("end-game-modal").style.display = "none";
        }

    } else if (gameState.phase === "Lost") {

        serverMessage.textContent =
            "Game Over! Your ship was caught by an enemy!";

        if (showPopup) {
            showEndGamePopup("Lost");
        } else {
            document.getElementById("end-game-modal").style.display = "none";
        }

    } else {

        serverMessage.textContent =
            "Find the treasure while avoiding pirates and sea monsters!";

        document.getElementById("end-game-modal").style.display = "none";
    }

    // Draw the board
    for (let y = 0; y < gameState.dimensions; y++) {

        for (let x = 0; x < gameState.dimensions; x++) {

            const cell = document.createElement("div");
            cell.classList.add("cell");

            // Display islands
            if (!gameState.map[x][y]) {

                cell.classList.add("island");

                const islandImage = document.createElement("img");
                islandImage.src = "/images/island.jpg";
                islandImage.alt = "Island";
                islandImage.classList.add("game-image");

                cell.appendChild(islandImage);
            }

            // Display player ship
            if (x === gameState.shipX && y === gameState.shipY) {

                cell.innerHTML = "";

                const shipImage = document.createElement("img");
                shipImage.src = "/images/ship.jpg";
                shipImage.alt = "Player ship";
                shipImage.classList.add("game-image");

                cell.appendChild(shipImage);

            } else {

                // Display pirate ships
                const pirateIndex = gameState.piratePositions.findIndex(
                    position => position[0] === x && position[1] === y
                );

                if (pirateIndex !== -1) {

                    cell.innerHTML = "";
                    cell.classList.add("pirate");

                    const pirateImage = document.createElement("img");
                    pirateImage.src = "/images/pirateShip.jpg";
                    pirateImage.alt = "Pirate ship";
                    pirateImage.classList.add("game-image");

                    cell.appendChild(pirateImage);

                } else {

                    // Display sea monsters
                    const seaMonsterIndex =
                        gameState.seaMonsterPositions.findIndex(
                            position => position[0] === x &&
                                        position[1] === y
                        );

                    if (seaMonsterIndex !== -1) {

                        cell.innerHTML = "";
                        cell.classList.add("sea-monster");

                        const seaMonsterImage = document.createElement("img");
                        seaMonsterImage.src = "/images/seaMonster.jpg";
                        seaMonsterImage.alt = "Sea monster";
                        seaMonsterImage.classList.add("game-image");

                        cell.appendChild(seaMonsterImage);

                    } else {

                        // Display treasure
                        if (x === gameState.treasureX &&
                            y === gameState.treasureY) {

                            cell.innerHTML = "";

                            const treasureImage = document.createElement("img");
                            treasureImage.src = "/images/treasure.jpg";
                            treasureImage.alt = "Treasure";
                            treasureImage.classList.add("game-image");

                            cell.appendChild(treasureImage);
                        }
                    }
                }
            }

            grid.appendChild(cell);
        }
    }
}

// Undo the most recent turn
async function undoMove() {

    const undoButton = document.getElementById("undo-button");

    if (undoButton.disabled) {
        return;
    }

    try {

        undoButton.disabled = true;

        const response = await fetch("/api/undo", {
            method: "POST"
        });

        if (!response.ok) {
            throw new Error("Undo request failed.");
        }

        const gameState = await response.json();

        displayGame(gameState);

    } catch (error) {

        console.error("Error undoing move:", error);

        alert("Could not undo the previous move.");

    }
}

// Display the centered end-game popup
function showEndGamePopup(result) {

    const modal = document.getElementById("end-game-modal");
    const title = document.getElementById("end-game-title");
    const message = document.getElementById("end-game-message");

    if (result === "Won") {

        title.textContent = "You Won!";
        message.textContent =
            "Congratulations! You found the treasure and completed your voyage!";

    } else {

        title.textContent = "Game Over!";
        message.textContent =
            "Your ship was caught by a pirate or sea monster. Better luck next time!";
    }

    modal.style.display = "flex";
}

// Handle arrow key presses
function handleKeyPress(event) {

    const directions = {
        ArrowUp: "north",
        ArrowDown: "south",
        ArrowRight: "east",
        ArrowLeft: "west"
    };

    const direction = directions[event.key];

    if (direction) {

        event.preventDefault();

        const modal = document.getElementById("end-game-modal");

        if (modal.style.display !== "none") {
            return;
        }

        moveShip(direction);
    }
}

// Send movement request to Java
async function moveShip(direction) {

    try {

        const response = await fetch("/api/move", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                direction: direction
            })
        });

        if (!response.ok) {
            throw new Error("Movement request failed.");
        }

        const gameState = await response.json();

        displayGame(gameState);

    } catch (error) {

        console.error("Error moving ship:", error);
    }
}

// Replay the completed game
async function watchGame() {

    if (isWatchingReplay) {
        return;
    }

    try {

        isWatchingReplay = true;

        // Hide the end-game popup during playback
        document.getElementById("end-game-modal").style.display = "none";

        // Retrieve the saved history and the current board information
        const [replayResponse, stateResponse] = await Promise.all([
            fetch("/api/replay"),
            fetch("/api/state")
        ]);

        if (!replayResponse.ok || !stateResponse.ok) {
            throw new Error("Could not retrieve the game history.");
        }

        const snapshots = await replayResponse.json();
        const currentState = await stateResponse.json();

        if (snapshots.length === 0) {
            throw new Error("No gameplay history is available.");
        }

        // Combine each snapshot with the map and treasure information
        const replayStates = snapshots.map(snapshot => ({

            ...currentState,

            shipX: snapshot.shipPosition.x,
            shipY: snapshot.shipPosition.y,

            piratePositions: snapshot.piratePositions.map(
                position => [position.x, position.y]
            ),

            seaMonsterPositions: snapshot.seaMonsterPositions.map(
                position => [position.x, position.y]
            ),

            phase: snapshot.phase,
            canUndo: false
        }));

        // Play each saved board state
        for (const gameState of replayStates) {

            displayGame(gameState, false);

            document.getElementById("server-message").textContent =
                "Watching your previous voyage...";

            // Wait 600 milliseconds before showing the next turn
            await new Promise(resolve => setTimeout(resolve, 600));
        }

        // Playback is finished
        isWatchingReplay = false;

        // Display the final board and original end-game popup
        const finalState = replayStates[replayStates.length - 1];

        displayGame(finalState);

    } catch (error) {

        console.error("Error replaying game:", error);

        isWatchingReplay = false;

        alert("Could not replay the previous game.");

        // Restore the current board if playback fails
        try {
            const response = await fetch("/api/state");

            if (response.ok) {
                const gameState = await response.json();
                displayGame(gameState);
            }
        } catch (restoreError) {
            console.error("Could not restore the game board:", restoreError);
        }
    }
}
