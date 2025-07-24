package chess;

/**
 * Move.java
 * A record to represent a move.
 * It now includes an isPromotion flag to correctly handle pawn promotions.
 */
public record Move(
        int startRow,
        int startCol,
        int endRow,
        int endCol,
        Piece pieceCaptured,
        boolean isPromotion
) {
}
