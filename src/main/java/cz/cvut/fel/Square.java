package cz.cvut.fel;

import cz.cvut.fel.pieces.Piece;

/**
 *
 * @author frogp
 */
public abstract class Square {

    int squareCoordinates;

    /**
     * Class constructor
     *
     * @param squarePosition index of position in board represantion
     */
    public Square(int squarePosition) {
        this.squareCoordinates = squarePosition;
    }

    /**
     * Returns true if square is occupied, otherwise false.
     *
     * @return boolean
     */
    public abstract boolean isOccupied();

    /**
     * Returns piece at square.
     *
     * @return piece at square
     */
    public abstract Piece getPiece();

    /**
     * Creates a square, if is occupied, creates a occupiedSquare, otherwise
     * emptySquare.
     *
     * @param position index of square in board represantation
     * @param piece piece, which will be putted at square
     * @return square
     */
    public static Square createSquare(int position, Piece piece) {
        if (piece == null) {
            return new emptySquare(position);
        } else {
            return new occupiedSquare(position, piece);
        }
    }

    /**
     * Returns index of square in board represantation.
     *
     * @return index of square
     */
    public int getSquareCoordinate() {
        return this.squareCoordinates;
    }

    /**
     * Class for empty squares.
     */
    public static final class emptySquare extends Square {

        /**
         * Class constructor.
         *
         * @param squarePosition index of position in board represantion
         */
        public emptySquare(int squarePosition) {
            super(squarePosition);
        }

        /**
         * Returns true if square is occupied, otherwise false.
         *
         * @return false
         */
        @Override
        public boolean isOccupied() {
            return false;
        }

        /**
         * Returns piece at square.
         *
         * @return null
         */
        @Override
        public Piece getPiece() {
            return null;
        }

        @Override
        public String toString() {
            return "-";
        }
    }

    /**
     * Class for occupied squares.
     */
    public static final class occupiedSquare extends Square {

        private final Piece piece;

        /**
         * Class constructor.
         *
         * @param squarePosition index of position in board represantion
         * @param piece p
         */
        public occupiedSquare(int squarePosition, Piece piece) {
            super(squarePosition);
            this.piece = piece;
        }

        /**
         * Returns true if square is occupied, otherwise false.
         *
         * @return true
         */
        @Override
        public boolean isOccupied() {
            return true;
        }

        /**
         * Returns piece at square.
         *
         * @return piece at square
         */
        @Override
        public Piece getPiece() {
            return piece;
        }

        @Override
        public String toString() {
            return (getPiece().getColour() == eColour.BLACK)
                    ? getPiece().toString().toLowerCase()
                    : getPiece().toString().toUpperCase();
        }
    }
}
