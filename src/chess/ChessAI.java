package chess;

import java.util.List;

/**
 * ChessAI.java
 * Contains the AI logic using the Minimax algorithm with Alpha-Beta Pruning.
 * This version uses a "copy-on-write" approach for simulations, which is more robust
 * and prevents state corruption bugs.
 */
public class ChessAI {

    public Move findBestMove(Board board, int depth) {
        Move bestMove = null;
        int bestValue = Integer.MAX_VALUE;

        for (Move move : board.getLegalMoves(false)) {
            // Create a copy of the board for simulation
            Board nextBoard = new Board(board);
            nextBoard.makeMove(move);

            int moveValue = minimax(nextBoard, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

            // No undoMove is needed because we used a copy

            if (moveValue < bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private int minimax(Board board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || board.isGameOver()) {
            return board.evaluate();
        }

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getLegalMoves(true)) {
                Board nextBoard = new Board(board);
                nextBoard.makeMove(move);
                int eval = minimax(nextBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : board.getLegalMoves(false)) {
                Board nextBoard = new Board(board);
                nextBoard.makeMove(move);
                int eval = minimax(nextBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }
}
