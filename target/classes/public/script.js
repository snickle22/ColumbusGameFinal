
console.log("Ocean Explorer JavaScript is running!");

// Store the settings used for the current game
let lastGameConfig = null;

document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("start-game-button")
        .addEventListener("click", () => startGame());

    document.getElementById("replay-button")
        .addEventListener("click", replayGame);

    document.getElementById("change-settings-button")
        .addEventListener("click", changeSettings);

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

        // Remember the settings for replay
        lastGameConfig = { ...config };

        // Hide the end game popup
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

        // Hide start menu
        document.getElementById("start-menu").style.display = "none";

        // Show game screen
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

    // Hide popup and game screen
    document.getElementById("end-game-modal").style.display = "none";
    document.getElementById("game-screen").style.display = "none";

    // Show start menu
    document.getElementById("start-menu").style.display = "block";

    // Clear the previous game board
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
function displayGame(gameState) {

    const grid = document.getElementById("game-grid");
    grid.innerHTML = "";

    grid.style.gridTemplateColumns =
        `repeat(${gameState.dimensions}, 50px)`;

    grid.style.gridTemplateRows =
        `repeat(${gameState.dimensions}, 50px)`;

    // Display game status
    const serverMessage = document.getElementById("server-message");

    if (gameState.phase === "Won") {

        serverMessage.textContent =
            "Congratulations! You found the treasure and won the game!";

        showEndGamePopup("Won");

    } else if (gameState.phase === "Lost") {

        serverMessage.textContent =
            "Game Over! Your ship was caught by an enemy!";

        showEndGamePopup("Lost");

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

        // Don't send keyboard movement while the popup is open
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
