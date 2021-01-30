package cz.cvut.fel.player;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.eColour;
import cz.cvut.fel.pieces.King;
import cz.cvut.fel.pieces.Piece;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class for PLAYER
 *
 * @author frogp
 */
public abstract class Player {

    /**
     * Current board.
     */
    protected final Board board;

    /**
     * Players king.
     */
    protected final King king;

    /**
     * Collection of players valid moves.
     */
    protected final Collection<Move> validMoves;
    private final boolean isInCheck;

    /**
     * Class constructor
     *
     * @param board current board
     * @param validMoves players valid moves
     * @param opponentValidMoves players opponent valid moves
     */
    public Player(Board board, Collection<Move> validMoves, Collection<Move> opponentValidMoves) {
        this.board = board;
        this.king = crownKing();
        this.validMoves = validMoves;
        //this.validMoves.addAll(calculateCastles(validMoves, opponentValidMoves));
        this.isInCheck = !Player.calculateAttacksOnSquare(this.king.getSquareID(), opponentValidMoves).isEmpty();
    }

    private King crownKing() {
        for (Piece piece : getAlivePieces()) {
            if (piece.getClass() == King.class) {
                return (King) piece;
            }
        }
        throw new RuntimeException("King was not find! Board was not valid!");
    }

    /**
     * Returns players king.
     *
     * @return
     */
    public King getKing() {
        return this.king;
    }

    /**
     * Returns a list with all valid attack moves on square.
     *
     * @param position index of square
     * @param opponentsMoves players opponent valid moves
     * @return list of moves
     */
    protected static Collection<Move> calculateAttacksOnSquare(int position, Collection<Move> opponentsMoves) {
        final List<Move> attackMoves = new ArrayList();
        for (Move move : opponentsMoves) {
            if (position == move.getDestination()) {
                attackMoves.add(move);
            }
        }
        return attackMoves;
    }

    /**
     * Returns true, if move is valid, otherwise false.
     *
     * @param move move to be checked
     * @return boolean
     */
    public boolean isMoveValid(Move move) {
        return this.validMoves.contains(move);
    }

    /**
     * Returns true, if player is in check, otherwise false.
     *
     * @return boolean
     */
    public boolean isInCheck() {
        return isInCheck;
    }

    /**
     * Returns true, if player is in stalemate, otherwise false.
     *
     * @return boolean
     */
    public boolean isInStaleMate() {
        return !isInCheck && !hasEscapeMoves();
    }

    /**
     * Returns true, if player is in checkmate, otherwise false.
     *
     * @return boolean
     */
    public boolean isInCheckMate() {
        return isInCheck && !hasEscapeMoves();
    }

    /**
     * Returns true, if player is castled, otherwise false.
     *
     * @return boolean
     */
    public boolean isCastled() {
        return false;
    }

    /**
     * Returns true, if player has escape moves, otherwise false.
     *
     * @return boolean
     */
    protected boolean hasEscapeMoves() {
        for (Move move : this.validMoves) {
            MoveMaker mooveMaker = makeMove(move);
            if (mooveMaker.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a collection of valid moves.
     *
     * @return valid moves
     */
    public Collection<Move> getValidMoves() {
        return this.validMoves;
    }

    /**
     * Method for making move, transform current board to board after move is
     * done.
     *
     * @param move move to make
     * @return moveMaker
     */
    public MoveMaker makeMove(Move move) {
        if (!isMoveValid(move)) {
            return new MoveMaker(this.board, move, MoveStatus.NONVALID_MOVE);
        }
        final Board nextMoveBoard = move.execute();

        final Collection<Move> kingAttacks
                = Player.calculateAttacksOnSquare(nextMoveBoard.getCurrentPlayer().getOpponent().getKing().getSquareID(), nextMoveBoard.getCurrentPlayer().getValidMoves());

        if (!kingAttacks.isEmpty()) {
            return new MoveMaker(board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveMaker(nextMoveBoard, move, MoveStatus.DONE);
    }

    /**
     * Returns a collection of players pieces on board.
     *
     * @return collection of pieces
     */
    public abstract Collection<Piece> getAlivePieces();

    /**
     * Returns players colour.
     *
     * @return players colour
     */
    public abstract eColour getColour();

    /**
     * Returns players opponent.
     *
     * @return players opponent
     */
    public abstract Player getOpponent();

    /**
     * Calaculates queen and king castles
     *
     * @param playerValidMoves players valid move
     * @param opponentValidMoves players opponent valid move
     * @return collection of moves
     */
    protected abstract Collection<Move> calculateCastles(
            Collection<Move> playerValidMoves,
            Collection<Move> opponentValidMoves);
}
