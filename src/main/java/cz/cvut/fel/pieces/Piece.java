package cz.cvut.fel.pieces;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.player.Player;
import cz.cvut.fel.eColour;
import java.util.List;

/**
 * Abstract class for pieces.
 *
 * @author frogp
 */
public abstract class Piece {

    private final static int VALUE_OF_PAWN = 100;
    private final static int VALUE_OF_BISHOP = 300;
    private final static int VALUE_OF_KNIGHT = 300;
    private final static int VALUE_OF_ROOK = 500;
    private final static int VALUE_OF_QUEEN = 900;
    private final static int VALUE_OF_KING = 10000;

    /**
     * Type of piece.
     */
    protected final ePieceType pieceType;

    /**
     * Player, which owns a piece.
     */
    protected Player player;

    /**
     * Colour of piece.
     */
    protected eColour colour;

    /**
     * Contains true if piece has not moved yet, otherwise false.
     */
    protected boolean isFirstMove;

    /**
     * Current position of piece.
     */
    protected int SquareID;
    private final int hashCode;

    /**
     * Class constructor
     *
     * @param pieceType type of piece
     * @param squareID postion of piece
     * @param colour colour of piece
     * @param isFirstMove true if is first move, otherwise false
     */
    public Piece(ePieceType pieceType, int squareID, eColour colour, boolean isFirstMove) {
        this.pieceType = pieceType;
        this.colour = colour;
        this.SquareID = squareID;
        this.isFirstMove = isFirstMove;
        this.hashCode = computeHashCode();
    }

    /**
     * Returns player, which owns piece.
     *
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Return type of piece.
     *
     * @return pieceType
     */
    public ePieceType getPieceType() {
        return pieceType;
    }

    /**
     * Sets a player, which owns piece.
     *
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Returns true if piece has not moved yet, otherwise false.
     *
     * @return boolean
     */
    public boolean isFirstMove() {
        return isFirstMove;
    }

    /**
     * Returns postion of piece.
     *
     * @return position of piece
     */
    public int getSquareID() {
        return SquareID;
    }

    /**
     * Returns colour of piece.
     *
     * @return colour
     */
    public eColour getColour() {
        return colour;
    }

    /**
     * Returns value of piece.
     *
     * @return value of piece
     */
    public int getPieceValue() {
        return pieceType.getPieceValue();
    }

    private int computeHashCode() {
        int hash = pieceType.hashCode();
        hash = 13 * hash + colour.hashCode();
        hash = 13 * hash + SquareID;
        hash = 13 * hash + (isFirstMove() ? 1 : 0);
        return hash;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Piece)) {
            return false;
        }

        final Piece other = (Piece) obj;
        return SquareID == other.getSquareID() && pieceType == other.getPieceType()
                && colour == other.getColour() && isFirstMove == other.isFirstMove();
    }

    /**
     * Returns a piece, which is moved according to move.
     *
     * @param move move to move piece
     * @return moved piece
     */
    public abstract Piece movePiece(Move move);

    /**
     * Returns a list of valid moves for this piece.
     *
     * @param board current board
     * @return list of moves
     */
    public abstract List<Move> findValidMoves(Board board);

    /**
     * Enum for pieces
     */
    public enum ePieceType {

        /**
         * Pawn
         */
        PAWN(VALUE_OF_PAWN, "P"),
        /**
         * Knight
         */
        KNIGHT(VALUE_OF_KNIGHT, "N"),
        /**
         * Bishop
         */
        BISHOP(VALUE_OF_BISHOP, "B"),
        /**
         * Rook
         */
        ROOK(VALUE_OF_ROOK, "R"),
        /**
         * Queen
         */
        QUEEN(VALUE_OF_QUEEN, "Q"),
        /**
         * King
         */
        KING(VALUE_OF_KING, "K");

        private final String pieceName;
        private final int pieceValue;

        private ePieceType(int pieceValue, String pieceName) {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        /**
         * Returns value of piece.
         *
         * @return value of piece
         */
        public int getPieceValue() {
            return this.pieceValue;
        }

        @Override
        public String toString() {
            return pieceName;
        }
    }
}
