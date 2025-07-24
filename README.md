# 🤖 Java Chess Bot with Web UI

A complete, playable chess application featuring a Java-based backend and an interactive web-based frontend. The backend contains a custom-built chess engine and an intelligent AI opponent that uses the Minimax algorithm with Alpha-Beta pruning to make decisions. The game is served locally and can be played in any modern web browser.

---

## ✨ Features

-   **Playable Chess Game**: A full implementation of chess rules, allowing a human player (White) to compete against a computer AI (Black).
-   **Intelligent AI Opponent**: The AI uses a Minimax algorithm with Alpha-Beta pruning, giving it a respectable estimated Elo of 1200-1500. It avoids simple blunders and plays a strong tactical game.
-   **Complete Chess Logic**: The engine correctly handles all standard piece movements, including two-square pawn advances, castling, and pawn promotion.
-   **Interactive Web Interface**: A clean and responsive UI built with HTML, CSS, and JavaScript. It features click-to-move functionality, highlights for the last move, and distinct piece colors for clarity.
-   **Decoupled Architecture**: The project is cleanly separated into a Java backend that handles all game logic and a lightweight frontend that focuses solely on presentation and user interaction.

---

## 📁 Project Structure

```
.
├── index.html                 # The web-based frontend UI
│
├── lib/
│   └── gson-x.x.x.jar         # Google's Gson library for JSON handling
│
└── src/
    └── chess/
        ├── WebServer.java     # Main entry point: Starts the local web server
        ├── Game.java          # Manages a single game session
        ├── Board.java         # Represents the board and enforces all chess rules
        ├── ChessAI.java       # Contains the Minimax AI logic
        ├── Move.java          # Data object for representing a single move
        └── Piece.java         # Defines all the chess pieces and their properties
```

---

## ⚙️ Requirements

-   Java Development Kit (JDK) 11+
-   Google Gson Library: The .jar file is required in the `lib` folder.
-   A modern web browser (Chrome, Firefox, Edge, etc.)

---

## 🚀 Setup and Usage

### 1. Clone the Repository

```bash
git clone <https://github.com/srivastava491/ChessBOT.git>
cd <Chess>
```

### 2. Add the Gson Library

1.  Download the Gson JAR file from a source like the [Maven Repository](https://search.maven.org/search?q=g:com.google.code.gson%20AND%20a:gson).
2.  Create a `lib` folder in the project's root directory.
3.  Place the downloaded `gson-x.x.x.jar` file inside the `lib` folder.

### 3. Compile the Java Code

Open a terminal or command prompt in the project's root directory and run the appropriate command for your operating system.

**On Windows:**

```bash
javac -cp ".;lib/gson-2.10.1.jar" src/chess/*.java
```

**On macOS / Linux:**

```bash
javac -cp ".:lib/gson-2.10.1.jar" src/chess/*.java
```

*Note: Replace `gson-2.10.1.jar` with the actual version you downloaded.*

### 4. Run the Server

With the code compiled, run the web server. Keep this terminal window open while you play.

**On Windows:**

```bash
java -cp ".;lib/gson-2.10.1.jar" chess.WebServer
```

**On macOS / Linux:**

```bash
java -cp ".:lib/gson-2.10.1.jar" chess.WebServer
```

You should see the message: `Starting server on port 8080...`

### 5. Play the Game!

1.  Navigate to the project folder in your file explorer.
2.  Open the `index.html` file in your web browser.
3.  The game will load, and you can start playing.

---

## ✍️ How to Play

-   **To move a piece**: First, click on the piece you want to move. The square will be highlighted. Then, click on the destination square.
-   **To castle**: Move your king two squares towards the rook (e.g., from e1 to g1 for kingside castling). The rook will move automatically.

---

## 🛠 Future Improvements

-   **Check, Checkmate & Stalemate**: Implement full game-over detection instead of the current king-capture condition.
-   **En Passant**: Add the logic for this special pawn capture.
-   **Enhanced AI Evaluation**: Improve the AI by adding positional awareness (e.g., piece-square tables, king safety, pawn structure) to its evaluation function.
-   **UI Enhancements**: Add features like a move history list, captured pieces display, and a "New Game" button.
-   **Drag-and-Drop**: Implement drag-and-drop functionality for moving pieces as an alternative to click-to-move.
