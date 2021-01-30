package cz.cvut.fel.pieces;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.Square;
import cz.cvut.fel.eColour;
import java.util.ArrayList;
import cz.cvut.fel.eBoardUtils;
import java.util.List;

/**
 * Class for PAWN
 *
 * @author frogp
 */
public class Pawn extends Piece {

    private final int[] POSSIBLE_MOVE_CANDIDATES = {8, 16, 7, 9};

    /**
     * Class constructor
     *
     * @param squareID position of pawn
     * @param colour colour of pawn
     */
    public Pawn(int squareID, eColour colour) {
        super(ePieceType.PAWN, squareID, colour, true);
    }

    /**
     * Class constructor
     *
     * @param squareID position of pawn
     * @param colour colour of pawn
     * @param isFirstMove true if is first move, otherwise false
     */
    public Pawn(int squareID, eColour colour, boolean isFirstMove) {
        super(ePieceType.PAWN, squareID, colour, isFirstMove);
    }

    @Override
    public String toString() {
        return ePieceType.PAWN.toString();
    }

    @Override
    public List<Move> findValidMoves(Board board) {
        List<Move> validMoves = new ArrayList();

        for (int direction : POSSIBLE_MOVE_CANDIDATES) {
            int position = (direction * this.getColour().getDirection()) + this.getSquareID();

            if (!eBoardUtils.isValidSquareID(position)) {
                continue;
            }
            Square square = board.getSquare(position);

            if (direction == 8 && !square.isOccupied()) {

                if (this.colour.isPawnPromotionSquare(position)) {
                    validMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, position))); // promotion

                } else {

                    validMoves.add(new Move.PawnMove(board, this, position));
                }
            } else if (direction == 16 && this.isFirstMove()
                    && ((eBoardUtils.INSTANCE.SEVENTH_RANK[this.SquareID] && this.colour == eColour.BLACK)
                    || (eBoardUtils.INSTANCE.SECOND_RANK[this.SquareID] && this.colour == eColour.WHITE))) {
                final int behindSquare
                        = this.SquareID + (this.colour.getDirection() * 8);
                if (!square.isOccupied()
                        && !board.getSquare(behindSquare).isOccupied()) {
                    validMoves.add(new Move.PawnJump(board, this, position));
                }
            } else if (direction == 7
                    && !((eBoardUtils.INSTANCE.EIGHTH_COLUMN[this.SquareID] && this.colour == eColour.WHITE)
                    || (eBoardUtils.INSTANCE.FIRST_COLUMN[this.SquareID] && this.colour == eColour.BLACK))) {
                if (square.isOccupied()) {
                    final Piece pieceOnSquare = square.getPiece();
                    if (this.colour != pieceOnSquare.getColour()) {
                        if (this.colour.isPawnPromotionSquare(position)) {
                            validMoves.add(new Move.PawnPromotion(
                                    new Move.PawnAttackMove(board, this, position, pieceOnSquare))); // promotion

                        } else {

                            validMoves.add(new Move.PawnAttackMove(board, this, position, pieceOnSquare));
                        }
                    }
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getSquareID()
                        == (this.SquareID + (this.colour.getOppositeDirection()))) {

                    final Piece pieceCandidate = board.getEnPassantPawn();
                    if (this.colour != pieceCandidate.getColour()) {
                        validMoves.add(new Move.PawnEnPassantAttackMove(
                                board, this, position, pieceCandidate));
                    }
                }
            } else if (direction == 9
                    && !((eBoardUtils.INSTANCE.FIRST_COLUMN[this.SquareID] && this.colour == eColour.WHITE)
                    || (eBoardUtils.INSTANCE.EIGHTH_COLUMN[this.SquareID] && this.colour == eColour.BLACK))) {
                if (square.isOccupied()) {
                    final Piece pieceOnSquare = square.getPiece();
                    if (this.colour != pieceOnSquare.getColour()) {
                        if (this.colour.isPawnPromotionSquare(position)) {
                            validMoves.add(new Move.PawnPromotion(new Move.PawnMove(board, this, position))); // promotion
                        } else {
                            validMoves.add(new Move.PawnAttackMove(board, this, position, pieceOnSquare));
                        }
                    }
                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getSquareID()
                        == (this.SquareID - (this.colour.getOppositeDirection()))) {

                    final Piece pieceCandidate = board.getEnPassantPawn();
                    if (this.colour != pieceCandidate.getColour()) {
                        validMoves.add(new Move.PawnEnPassantAttackMove(
                                board, this, position, pieceCandidate));
                    }
                }
            }
        }
        return validMoves;
    }

    @Override
    public Piece movePiece(Move move) {
        return new Pawn((move.getDestination()), colour, false);
    }

    /**
     *
     * @return
     */
    public Piece getPromotedPiece() {
        return new Queen(SquareID, this.colour, false);
    }
}
