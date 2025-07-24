package chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Board.java
 * Represents the chessboard, handles move generation, validation, and evaluation.
 * This version includes a copy constructor for a robust AI implementation.
 */
public class Board {
    private final Piece[][] board;
    // --- Castling Flags ---
    private boolean whiteKingMoved;
    private boolean blackKingMoved;
    private boolean whiteKingsideRookMoved;
    private boolean whiteQueensideRookMoved;
    private boolean blackKingsideRookMoved;
    private boolean blackQueensideRookMoved;

    public Board() {
        board = new Piece[8][8];
        setupInitialBoard();
    }

    /**
     * Copy constructor. Creates a deep copy of another board's state.
     * This is crucial for the AI to simulate moves without altering the real board.
     */
    public Board(Board other) {
        this.board = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            System.arraycopy(other.board[r], 0, this.board[r], 0, 8);
        }
        this.whiteKingMoved = other.whiteKingMoved;
        this.blackKingMoved = other.blackKingMoved;
        this.whiteKingsideRookMoved = other.whiteKingsideRookMoved;
        this.whiteQueensideRookMoved = other.whiteQueensideRookMoved;
        this.blackKingsideRookMoved = other.blackKingsideRookMoved;
        this.blackQueensideRookMoved = other.blackQueensideRookMoved;
    }

    public Piece getPiece(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) return null;
        return board[row][col];
    }

    private void setupInitialBoard() {
        board[0] = new Piece[]{Piece.BLACK_ROOK, Piece.BLACK_KNIGHT, Piece.BLACK_BISHOP, Piece.BLACK_QUEEN, Piece.BLACK_KING, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT, Piece.BLACK_ROOK};
        board[1] = new Piece[]{Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN};
        board[6] = new Piece[]{Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN};
        board[7] = new Piece[]{Piece.WHITE_ROOK, Piece.WHITE_KNIGHT, Piece.WHITE_BISHOP, Piece.WHITE_QUEEN, Piece.WHITE_KING, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT, Piece.WHITE_ROOK};
    }

    public int evaluate() {
        int score = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null) {
                    score += board[r][c].getValue();
                }
            }
        }
        return score;
    }

    public List<Move> getLegalMoves(boolean isWhitePlayer) {
        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.isWhite() == isWhitePlayer) {
                    switch (piece) {
                        case WHITE_PAWN, BLACK_PAWN -> getPawnMoves(moves, r, c);
                        case WHITE_ROOK, BLACK_ROOK -> getSlidingMoves(moves, r, c, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}});
                        case WHITE_KNIGHT, BLACK_KNIGHT -> getKnightMoves(moves, r, c);
                        case WHITE_BISHOP, BLACK_BISHOP -> getSlidingMoves(moves, r, c, new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
                        case WHITE_QUEEN, BLACK_QUEEN -> getSlidingMoves(moves, r, c, new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
                        case WHITE_KING, BLACK_KING -> getKingMoves(moves, r, c);
                    }
                }
            }
        }
        return moves;
    }

    private void getPawnMoves(List<Move> moves, int r, int c) {
        Piece piece = board[r][c];
        int direction = piece.isWhite() ? -1 : 1;
        int startRow = piece.isWhite() ? 6 : 1;

        if (isValid(r + direction, c) && board[r + direction][c] == null) {
            addMove(moves, r, c, r + direction, c);
        }
        if (r == startRow && board[r + direction][c] == null && isValid(r + 2 * direction, c) && board[r + 2 * direction][c] == null) {
            addMove(moves, r, c, r + 2 * direction, c);
        }
        if (isValid(r + direction, c - 1) && board[r + direction][c - 1] != null && board[r + direction][c - 1].isWhite() != piece.isWhite()) {
            addMove(moves, r, c, r + direction, c - 1);
        }
        if (isValid(r + direction, c + 1) && board[r + direction][c + 1] != null && board[r + direction][c + 1].isWhite() != piece.isWhite()) {
            addMove(moves, r, c, r + direction, c + 1);
        }
    }

    private void getSlidingMoves(List<Move> moves, int r, int c, int[][] directions) {
        Piece piece = board[r][c];
        for (int[] d : directions) {
            for (int i = 1; i < 8; i++) {
                int newR = r + i * d[0];
                int newC = c + i * d[1];
                if (!isValid(newR, newC)) break;
                if (board[newR][newC] == null) {
                    addMove(moves, r, c, newR, newC);
                } else {
                    if (board[newR][newC].isWhite() != piece.isWhite()) {
                        addMove(moves, r, c, newR, newC);
                    }
                    break;
                }
            }
        }
    }

    private void getKnightMoves(List<Move> moves, int r, int c) {
        int[][] knightMoves = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
        for (int[] m : knightMoves) {
            int newR = r + m[0];
            int newC = c + m[1];
            if (isValid(newR, newC)) {
                Piece target = board[newR][newC];
                if (target == null || target.isWhite() != board[r][c].isWhite()) {
                    addMove(moves, r, c, newR, newC);
                }
            }
        }
    }

    private void getKingMoves(List<Move> moves, int r, int c) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int newR = r + dr;
                int newC = c + dc;
                if (isValid(newR, newC)) {
                    Piece target = board[newR][newC];
                    if (target == null || target.isWhite() != board[r][c].isWhite()) {
                        addMove(moves, r, c, newR, newC);
                    }
                }
            }
        }
        getCastlingMoves(moves, r, c);
    }

    private void getCastlingMoves(List<Move> moves, int r, int c) {
        boolean isWhite = board[r][c].isWhite();
        if ((isWhite && whiteKingMoved) || (!isWhite && blackKingMoved)) return;
        if (isSquareAttacked(r, c, !isWhite)) return;

        if ((isWhite && !whiteKingsideRookMoved) || (!isWhite && !blackKingsideRookMoved)) {
            if (board[r][c + 1] == null && board[r][c + 2] == null) {
                if (!isSquareAttacked(r, c + 1, !isWhite) && !isSquareAttacked(r, c + 2, !isWhite)) {
                    addMove(moves, r, c, r, c + 2);
                }
            }
        }
        if ((isWhite && !whiteQueensideRookMoved) || (!isWhite && !blackQueensideRookMoved)) {
            if (board[r][c - 1] == null && board[r][c - 2] == null && board[r][c - 3] == null) {
                if (!isSquareAttacked(r, c - 1, !isWhite) && !isSquareAttacked(r, c - 2, !isWhite)) {
                    addMove(moves, r, c, r, c - 2);
                }
            }
        }
    }

    public boolean isSquareAttacked(int r, int c, boolean byWhite) {
        int pawnDir = byWhite ? 1 : -1;
        if (isValid(r + pawnDir, c - 1) && getPiece(r + pawnDir, c - 1) == (byWhite ? Piece.WHITE_PAWN : Piece.BLACK_PAWN)) return true;
        if (isValid(r + pawnDir, c + 1) && getPiece(r + pawnDir, c + 1) == (byWhite ? Piece.WHITE_PAWN : Piece.BLACK_PAWN)) return true;

        int[][] knightMoves = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
        for(int[] m : knightMoves) {
            if (isValid(r + m[0], c + m[1]) && getPiece(r + m[0], c + m[1]) == (byWhite ? Piece.WHITE_KNIGHT : Piece.BLACK_KNIGHT)) return true;
        }

        int[][] slidingDirections = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}};
        for(int[] d : slidingDirections) {
            for(int i = 1; i < 8; i++) {
                int newR = r + i * d[0];
                int newC = c + i * d[1];
                if (!isValid(newR, newC)) break;
                Piece p = getPiece(newR, newC);
                if (p != null) {
                    if (p.isWhite() == byWhite) {
                        boolean isRook = p.getSymbol().equalsIgnoreCase("R");
                        boolean isBishop = p.getSymbol().equalsIgnoreCase("B");
                        boolean isQueen = p.getSymbol().equalsIgnoreCase("Q");
                        boolean isKing = p.getSymbol().equalsIgnoreCase("K");
                        boolean isStraight = d[0] == 0 || d[1] == 0;
                        boolean isDiagonal = d[0] != 0 && d[1] != 0;

                        if (isQueen || (isRook && isStraight) || (isBishop && isDiagonal)) return true;
                        if (isKing && i == 1) return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    private void addMove(List<Move> moves, int r, int c, int newR, int newC) {
        Piece piece = board[r][c];
        boolean isPromotion = (piece == Piece.WHITE_PAWN && newR == 0) || (piece == Piece.BLACK_PAWN && newR == 7);
        moves.add(new Move(r, c, newR, newC, board[newR][newC], isPromotion));
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < 8 && c >= 0 && c < 8;
    }

    public boolean isMoveLegal(Move move, boolean isWhitePlayer) {
        List<Move> legalMoves = getLegalMoves(isWhitePlayer);
        for(Move legalMove : legalMoves){
            if(legalMove.startRow() == move.startRow() && legalMove.startCol() == move.startCol() &&
                    legalMove.endRow() == move.endRow() && legalMove.endCol() == move.endCol()){
                return true;
            }
        }
        return false;
    }

    public void makeMove(Move move) {
        Piece piece = board[move.startRow()][move.startCol()];

        if (piece == Piece.WHITE_KING) whiteKingMoved = true;
        if (piece == Piece.BLACK_KING) blackKingMoved = true;
        if (piece == Piece.WHITE_ROOK && move.startRow() == 7 && move.startCol() == 0) whiteQueensideRookMoved = true;
        if (piece == Piece.WHITE_ROOK && move.startRow() == 7 && move.startCol() == 7) whiteKingsideRookMoved = true;
        if (piece == Piece.BLACK_ROOK && move.startRow() == 0 && move.startCol() == 0) blackQueensideRookMoved = true;
        if (piece == Piece.BLACK_ROOK && move.startRow() == 0 && move.startCol() == 7) blackKingsideRookMoved = true;

        if (piece.getSymbol().equalsIgnoreCase("K") && Math.abs(move.startCol() - move.endCol()) == 2) {
            if (move.endCol() > move.startCol()) {
                Piece rook = board[move.startRow()][7];
                board[move.startRow()][5] = rook;
                board[move.startRow()][7] = null;
            } else {
                Piece rook = board[move.startRow()][0];
                board[move.startRow()][3] = rook;
                board[move.startRow()][0] = null;
            }
        }

        board[move.endRow()][move.endCol()] = piece;
        board[move.startRow()][move.startCol()] = null;

        if (move.isPromotion()) {
            board[move.endRow()][move.endCol()] = piece.isWhite() ? Piece.WHITE_QUEEN : Piece.BLACK_QUEEN;
        }
    }

    public boolean isGameOver() {
        boolean whiteKingFound = false;
        boolean blackKingFound = false;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] == Piece.WHITE_KING) whiteKingFound = true;
                if (board[r][c] == Piece.BLACK_KING) blackKingFound = true;
            }
        }
        return !whiteKingFound || !blackKingFound;
    }
}
