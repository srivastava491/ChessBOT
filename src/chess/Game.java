package chess;

/**
 * Game.java
 * Manages the game state and provides accessors for the WebServer.
 * This version is updated to handle the new isPromotion flag in Move objects.
 */
public class Game {
    private Board board;
    private ChessAI ai;

    public Game() {
        this.board = new Board();
        this.ai = new ChessAI();
    }

    public Board getBoard() {
        return board;
    }

    public ChessAI getAi() {
        return ai;
    }

    public Move parseMove(String moveStr) {
        if (moveStr == null || moveStr.length() != 4) {
            return null;
        }
        try {
            int startCol = moveStr.charAt(0) - 'a';
            int startRow = 8 - (moveStr.charAt(1) - '0');
            int endCol = moveStr.charAt(2) - 'a';
            int endRow = 8 - (moveStr.charAt(3) - '0');

            if (startRow < 0 || startRow > 7 || startCol < 0 || startCol > 7 ||
                    endRow < 0 || endRow > 7 || endCol < 0 || endCol > 7) {
                return null;
            }

            // Determine if the move is a promotion
            Piece movingPiece = board.getPiece(startRow, startCol);
            boolean isPromotion = (movingPiece == Piece.WHITE_PAWN && endRow == 0) ||
                    (movingPiece == Piece.BLACK_PAWN && endRow == 7);

            return new Move(startRow, startCol, endRow, endCol, board.getPiece(endRow, endCol), isPromotion);
        } catch (Exception e) {
            return null;
        }
    }
}
