package chess;

/**
 * Piece.java
 * An enum representing each chess piece, its color, value, and symbol for printing.
 */
public enum Piece {
    // White pieces
    WHITE_KING("K", 1000, true),
    WHITE_QUEEN("Q", 9, true),
    WHITE_ROOK("R", 5, true),
    WHITE_BISHOP("B", 3, true),
    WHITE_KNIGHT("N", 3, true),
    WHITE_PAWN("P", 1, true),

    // Black pieces
    BLACK_KING("k", -1000, false),
    BLACK_QUEEN("q", -9, false),
    BLACK_ROOK("r", -5, false),
    BLACK_BISHOP("b", -3, false),
    BLACK_KNIGHT("n", -3, false),
    BLACK_PAWN("p", -1, false);

    private final String symbol;
    private final int value;
    private final boolean isWhite;

    Piece(String symbol, int value, boolean isWhite) {
        this.symbol = symbol;
        this.value = value;
        this.isWhite = isWhite;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getValue() {
        return value;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
