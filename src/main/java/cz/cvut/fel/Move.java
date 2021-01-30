package cz.cvut.fel;

import cz.cvut.fel.Board.Builder;
import cz.cvut.fel.pieces.Pawn;
import cz.cvut.fel.pieces.Piece;
import cz.cvut.fel.pieces.Rook;

/**
 *
 * @author frogp
 */
public abstract class Move {

    /**
     * Current board, which will be transform to next move board.
     */
    protected final Board board;

    /**
     * Piece, which makes move.
     */
    protected final Piece piece;

    /**
     * Position, whre piece is going to.
     */
    protected final int destination;

    /**
     * Contains true, if is first move of piece, otherwise false.
     */
    protected final boolean isFirstMove;

    /**
     * Instatnce of move, which is not valid.
     */
    public static Move NON_VALID_MOVE = new NonValidMove();

    private Move(Board board, Piece piece, int destination) {
        this.board = board;
        this.piece = piece;
        this.destination = destination;
        this.isFirstMove = piece.isFirstMove();
    }

    private Move(Board board, int destination) {
        this.board = board;
        this.piece = null;
        this.destination = destination;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + getDestination();
        hash = 37 * hash + this.piece.hashCode();
        hash = 37 * hash + this.piece.getSquareID();
        hash = hash + ((isFirstMove) ? 0 : 1);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Move)) {
            return false;
        }

        final Move other = (Move) obj;
        return this.getCurrentSquareIDX() == other.getCurrentSquareIDX()
                && getDestination() == other.getDestination()
                && this.piece.equals(obj);
    }

    /**
     * Returns a destionation of piece, which is going to make move.
     *
     * @return index in board represantion
     */
    public int getDestination() {
        return destination;
    }

    /**
     * Method for executing a move.
     *
     * @return board after execution of move
     */
    public Board execute() {
        final Board.Builder builder = new Board.Builder();
        for (final Piece piece : this.board.getCurrentPlayer().getAlivePieces()) {
            if (!this.piece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getAlivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.piece.movePiece(this));
        builder.setMoveColour(this.board.getCurrentPlayer().getOpponent().getColour());
        builder.setTransitionMove(this);
        return builder.build();
    }

    /**
     * Returns a index of piece in board represantion.
     *
     * @return index of piece
     */
    public int getCurrentSquareIDX() {
        return piece.getSquareID();
    }

    /**
     * Returns true, if move is attack, otherwise false.
     *
     * @return false
     */
    public boolean isAttack() {
        return false;
    }

    /**
     * Returns true, if move is castling, otherwise false.
     *
     * @return false
     */
    public boolean isCastlingMove() {
        return false;
    }

    /**
     * Returns a piece, which is at destination index(piece, that will be
     * attacked).
     *
     * @return null
     */
    public Piece getAttackedPiece() {
        return null;
    }

    /**
     * Class for NonAttack moves.
     */
    public static final class NonAttackMove extends Move {

        /**
         * Class constructor
         *
         * @param board Current board, which will be transform to next move
         * board.
         * @param piece Piece, which makes move.
         * @param destination Index of square, where piece is moving to.
         */
        public NonAttackMove(Board board, Piece piece, int destination) {
            super(board, piece, destination);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof NonAttackMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return piece.getPieceType().toString()
                    + eBoardUtils.INSTANCE.getPositionAtIndex(destination);
        }

    }

    /**
     * Class for attack moves.
     */
    public static class AttackMove extends Move {

        Piece attackedPiece;

        /**
         * Class constructor
         *
         * @param board Current board, which will be transform to next move
         * board.
         * @param piece Piece, which makes move.
         * @param destination Index of square, where piece is moving to.
         * @param attackedPiece Piece at destination index.
         */
        public AttackMove(Board board,
                Piece piece,
                int destination,
                Piece attackedPiece) {
            super(board, piece, destination);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AttackMove)) {
                return false;
            }
            final AttackMove other = (AttackMove) obj;
            return super.equals(other) && getAttackedPiece().equals(other.getAttackedPiece());
        }

        /**
         * Returns true, if move is attack, otherwise false.
         *
         * @return true
         */
        @Override
        public boolean isAttack() {
            return true;
        }

        /**
         * Returns a piece, which is at destination index.
         *
         * @return piece at destination index
         */
        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }
    }

    /**
     * Class for pawn moves.
     */
    public static class PawnMove extends Move {

        /**
         * Class constructor
         *
         * @param board Current board, which will be transform to next move
         * board.
         * @param piece Piece, which makes move.
         * @param destination Index of square, where piece is moving to.
         */
        public PawnMove(Board board, Piece piece, int destination) {
            super(board, piece, destination);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PawnMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return eBoardUtils.INSTANCE.getPositionAtIndex(this.destination);
        }
    }

    /**
     * Class for pawn attack moves.
     */
    public static class PawnAttackMove extends AttackMove {

        /**
         * Class constructor
         *
         * @param board current board, which will be transform to next move
         * board
         * @param piece piece, which makes move
         * @param destination index of square, where piece is moving to
         * @param attackedPiece piece at destination index
         */
        public PawnAttackMove(Board board, Piece piece, int destination, Piece attackedPiece) {
            super(board, piece, destination, attackedPiece);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PawnAttackMove && super.equals(obj);
        }

        @Override
        public String toString() {
            return eBoardUtils.INSTANCE.getPositionAtIndex(this.piece.getSquareID()).substring(0, 1)
                    + "x" + eBoardUtils.INSTANCE.getPositionAtIndex(this.destination);
        }
    }

    /**
     * Class for pawn EnPassant moves.
     */
    public static final class PawnEnPassantAttackMove extends AttackMove {

        /**
         * Class constructor
         *
         * @param board current board, which will be transform to next move
         * board
         * @param piece piece, which makes move
         * @param destination index of square, where piece is moving to
         * @param attackedPiece piece at destination index
         */
        public PawnEnPassantAttackMove(Board board, Piece piece, int destination, Piece attackedPiece) {
            super(board, piece, destination, attackedPiece);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PawnEnPassantAttackMove && super.equals(obj);
        }

        /**
         * Executes the enPassant move.
         *
         * @return board after execution of move
         */
        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece curPiece : board.getCurrentPlayer().getAlivePieces()) {
                if (!this.piece.equals(curPiece)) {
                    builder.setPiece(curPiece);
                }
            }
            for (final Piece curPiece : board.getCurrentPlayer().getOpponent().getAlivePieces()) {
                if (!curPiece.equals(this.attackedPiece)) {
                    builder.setPiece(curPiece);
                }
            }
            builder.setPiece(this.piece.movePiece(this));
            builder.setMoveColour(this.board.getCurrentPlayer().getOpponent().getColour());
            builder.setTransitionMove(this);

            return builder.build();
        }
    }

    /**
     * Class for pawn jumps.
     */
    public static final class PawnJump extends Move {

        /**
         * Class constructor
         *
         * @param board current board, which will be transform to next move
         * board
         * @param piece piece, which makes move
         * @param destination index of square, where piece is moving to
         */
        public PawnJump(Board board, Piece piece, int destination) {
            super(board, piece, destination);
        }

        /**
         * Executes a pawn jump.
         *
         * @return board after execution of move
         */
        @Override
        public Board execute() {
            final Builder builder = new Builder();
            for (final Piece piece : this.board.getCurrentPlayer().getAlivePieces()) {
                if (!this.piece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getAlivePieces()) {
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.piece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveColour(this.board.getCurrentPlayer().getOpponent().getColour());
            builder.setTransitionMove(this);
            return builder.build();
        }
    }

    /**
     * Class for pawn promotions.
     */
    public static final class PawnPromotion extends Move {

        final Move decoratedMove;
        final Pawn promotedPawn;

        /**
         * Class constructor
         *
         * @param decoratedMove standart pawn move, which will be transform to
         * pawn promotion
         */
        public PawnPromotion(final Move decoratedMove) {
            super(decoratedMove.board, decoratedMove.piece, decoratedMove.getDestination());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.piece;
        }

        /**
         * Executes a pawn promotion.
         *
         * @return board after execution of move
         */
        @Override
        public Board execute() {
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Builder builder = new Builder();
            for (final Piece piece : pawnMovedBoard.getCurrentPlayer().getAlivePieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getAlivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotedPiece().movePiece(this));
            builder.setMoveColour(pawnMovedBoard.getCurrentPlayer().getOpponent().getColour());
            return builder.build();
        }

        @Override
        public String toString() {
            return "";
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (37 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof PawnPromotion && this.decoratedMove.equals(obj);
        }

    }

    /**
     * Class for castle moves.
     */
    public static abstract class CastleMove extends Move {

        /**
         * Rook, which we castle.
         */
        protected final Rook castleRook;

        /**
         * Index of square, where castleRook is right now.
         */
        protected final int castleRookStart;

        /**
         * Index of square, where castleRook is going to.
         */
        protected final int castleRookDestination;

        /**
         * Class constructor
         *
         * @param board current board, which will be transform to next move
         * board
         * @param piece piece, which makes move
         * @param destination index of square, where piece is moving to
         * @param castleRook rook, which we castle
         * @param castleRookStart current index of castleRook
         * @param castleRookDestination index of square, where catleRook is
         * moving to
         */
        public CastleMove(Board board, Piece piece, int destination,
                Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destination);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        /**
         * Returns rook, which is castling.
         *
         * @return castling rook
         */
        public Rook getCastleRook() {
            return castleRook;
        }

        /**
         * Executes a castle move.
         *
         * @return board after execution of move
         */
        @Override
        public Board execute() {
            final Builder builder = new Builder();

            for (final Piece piece : this.board.getCurrentPlayer().getAlivePieces()) {
                if (!this.piece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getAlivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(new Rook(this.castleRookDestination, this.piece.getColour(), false));
            builder.setMoveColour(this.board.getCurrentPlayer().getOpponent().getColour());

            return builder.build();
        }

        @Override
        public int hashCode() {
            int hash = super.hashCode();
            hash = 37 * hash + this.castleRook.hashCode();
            hash = 37 * hash + this.castleRookDestination;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof CastleMove)) {
                return false;
            }
            final CastleMove other = (CastleMove) obj;
            return super.equals(other) && this.castleRook.equals(other.getCastleRook());
        }

    }

    /**
     * Class for King Castle moves.
     */
    public static final class KingCastleMove extends CastleMove {

        /**
         * Class constructor
         *
         * @param board current board, which will be transform to next move
         * board
         * @param piece piece, which makes move
         * @param destination index of square, where piece is moving to
         * @param castleRook rook, which we castle
         * @param castleRookStart current index of castleRook
         * @param castleRookDestination index of square, where catleRook is
         * moving to
         */
        public KingCastleMove(Board board, Piece piece,
                int destination, Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destination, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0";
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof KingCastleMove && super.equals(obj);
        }
    }

    /**
     * Class for Queen Castle moves.
     */
    public static final class QueenCastleMove extends CastleMove {

        /**
         * Class constructor
         *
         * @param board current board, which will be transform to next move
         * board
         * @param piece piece, which makes move
         * @param destination index of square, where piece is moving to
         * @param castleRook rook, which we castle
         * @param castleRookStart current index of castleRook
         * @param castleRookDestination index of square, where catleRook is
         * moving to
         */
        public QueenCastleMove(Board board, Piece piece, int destination,
                Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, piece, destination, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString() {
            return "0-0-0";
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof QueenCastleMove && super.equals(obj);
        }

    }

    /**
     * Class of nonvalid move.
     */
    public static final class NonValidMove extends Move {

        /**
         * Class constructor
         */
        public NonValidMove() {
            super(null, -1);
        }

        /**
         * Execute method of nonvalid move cannot be executed!
         *
         * @return exception
         */
        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute NonValid Move!");
        }

        /**
         * Returns -1 because is nonvalid move.
         *
         * @return -1
         */
        @Override
        public int getCurrentSquareIDX() {
            return -1;
        }
    }

    /**
     * Class for creating moves from GUI to engine.
     */
    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }

        /**
         * Tries to find move, which is same as move in all valid moves and move
         * entered by user. If exist returns that move, otherwise return
         * NONVALID move.
         *
         * @param board Current board, which will be transform to next move
         * board.
         * @param squareIDX Index of square, whre piece is standing right now.
         * @param destination Index of square, where piece is moving to.
         * @return move, created from GUI
         */
        public static Move createMove(final Board board,
                final int squareIDX,
                final int destination) {
            for (Move move : board.getCurrentPlayer().getValidMoves()) {

                if (move.getCurrentSquareIDX() == squareIDX
                        && move.getDestination() == destination) {
                    return move;
                }
            }
            return NON_VALID_MOVE;
        }
    }
}
