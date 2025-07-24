package chess;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * WebServer.java
 * This is the main class to run. It starts a simple HTTP server to allow a web
 * frontend to interact with the chess engine.
 * This version includes a fix for CORS preflight requests.
 */
public class WebServer {

    private static Game game;
    private static final Gson gson = new Gson();
    private static final int AI_SEARCH_DEPTH = 6;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/new-game", new NewGameHandler());
        server.createContext("/move", new MoveHandler());
        server.createContext("/get-board", new GetBoardHandler());
        server.setExecutor(null);
        System.out.println("Starting server on port 8080...");
        server.start();
    }

    // --- NEW: Helper method to handle CORS preflight requests ---
    private static void handleOptionsRequest(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type,X-Game-Over");
        exchange.sendResponseHeaders(204, -1); // 204 No Content
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Expose-Headers", "X-Game-Over");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // --- Handlers updated to check for OPTIONS method ---

    static class NewGameHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleOptionsRequest(exchange);
            } else {
                game = new Game();
                String jsonBoard = gson.toJson(getBoardState(game.getBoard()));
                sendResponse(exchange, 200, jsonBoard);
            }
        }
    }

    static class GetBoardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleOptionsRequest(exchange);
            } else {
                if (game == null) {
                    sendResponse(exchange, 404, "{\"error\":\"Game not started\"}");
                    return;
                }
                String jsonBoard = gson.toJson(getBoardState(game.getBoard()));
                sendResponse(exchange, 200, jsonBoard);
            }
        }
    }

    static class MoveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                handleOptionsRequest(exchange);
            } else {
                if (game == null) {
                    sendResponse(exchange, 400, "{\"error\":\"Game not started\"}");
                    return;
                }

                Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
                String from = params.get("from");
                String to = params.get("to");

                Move playerMove = game.parseMove(from + to);

                if (playerMove != null && game.getBoard().isMoveLegal(playerMove, true)) {
                    game.getBoard().makeMove(playerMove);

                    if (game.getBoard().isGameOver()) {
                        exchange.getResponseHeaders().set("X-Game-Over", "You Win!");
                    } else {
                        Move aiMove = game.getAi().findBestMove(game.getBoard(), AI_SEARCH_DEPTH);
                        if (aiMove != null) {
                            game.getBoard().makeMove(aiMove);
                            if (game.getBoard().isGameOver()) {
                                exchange.getResponseHeaders().set("X-Game-Over", "AI Wins!");
                            }
                        } else {
                            exchange.getResponseHeaders().set("X-Game-Over", "Stalemate!");
                        }
                    }

                    String jsonBoard = gson.toJson(getBoardState(game.getBoard()));
                    sendResponse(exchange, 200, jsonBoard);

                } else {
                    sendResponse(exchange, 400, "{\"error\":\"Invalid move\"}");
                }
            }
        }
    }

    private static String[][] getBoardState(Board board) {
        String[][] state = new String[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board.getPiece(r, c);
                state[r][c] = (piece == null) ? null : piece.getSymbol();
            }
        }
        return state;
    }

    public static Map<String, String> queryToMap(String query) {
        if (query == null) return new HashMap<>();
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
