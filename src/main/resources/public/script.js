
console.log("Ocean Explorer JavaScript is running!");

document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("start-game-button")
        .addEventListener("click", startGame); 

});

async function startGame() {

    const config = {
        gridSize: Number(document.getElementById("grid-size").value),
        islandCount: Number(document.getElementById("island-count").value),
        pirateCount: Number(document.getElementById("pirate-count").value),
        seaMonsterCount: Number(document.getElementById("monster-count").value),
        pirateStrategy: document.getElementById("pirate-strategy").value
    };

    try {

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

        // Show the game
        document.getElementById("game-screen").style.display = "block";

        // Display the game 
        document.getElementById("server-message").textContent =
            `Game started! Grid size: ${gameState.dimensions}x${gameState.dimensions}`;
        displayGame(gameState);

        // Enable keyboard controls
        document.addEventListener("keydown", handleKeyPress);
    } catch (error) {

        console.error("Error starting game:", error);
        alert("Could not start the game. Please try again.");
    }
}

async function loadGameState() {

    try {
        const response = await fetch("/api/state");

        if (!response.ok) {
            throw new Error("Failed to load game state.");
        }

        const gameState = await response.json();

        console.log("Game state received from Java:", gameState);

        document.getElementById("server-message").textContent =
            `Game loaded! Grid size: ${gameState.dimensions}x${gameState.dimensions}`;

        displayGame(gameState);

    } catch (error) {
        console.error("Error loading game:", error);

        document.getElementById("server-message").textContent =
            "Could not connect to the game server.";
    }
}


function displayGame(gameState) {

    const grid = document.getElementById("game-grid");

    grid.innerHTML = "";
    
    grid.style.gridTemplateColumns = 
        `repeat(${gameState.dimensions}, 50px)`;

    grid.style.gridTemplateRows = 
        `repeat(${gameState.dimensions}, 50px)`;

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
            }

            // Display first pirate
            else if (x === gameState.pirate1X && y === gameState.pirate1Y) {
                cell.innerHTML = "";
                const pirateImage = document.createElement("img");
                pirateImage.src = "/images/pirateShip.jpg";
                pirateImage.alt = "Pirate ship";
                pirateImage.classList.add("game-image");
                cell.appendChild(pirateImage);
            }

            // Display second pirate
            else if (x === gameState.pirate2X && y === gameState.pirate2Y) {
                cell.innerHTML = "";
                const pirateImage = document.createElement("img");
                pirateImage.src = "/images/pirateShip.jpg";
                pirateImage.alt = "Pirate ship";
                pirateImage.classList.add("game-image");
                cell.appendChild(pirateImage);
            }

            grid.appendChild(cell);
        }
    }
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
