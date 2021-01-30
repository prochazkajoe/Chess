package cz.cvut.fel.pieces;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.Square;
import cz.cvut.fel.eBoardUtils;
import cz.cvut.fel.eColour;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for KING
 *
 * @author frogp
 */
public class King extends Piece {

    private final int[] POSSIBLE_MOVE_CANDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    /**
     * Class constructor
     *
     * @param squareID position of king
     * @param colour colour of king
     */
    public King(int squareID, eColour colour) {
        super(ePieceType.KING, squareID, colour, true);
    }

    /**
     * Class constructor
     *
     * @param squareID position of king
     * @param colour colour of king
     * @param isFirstMove true if is first move, otherwise false
     */
    public King(int squareID, eColour colour, boolean isFirstMove) {
        super(ePieceType.KING, squareID, colour, isFirstMove);
    }

    @Override
    public String toString() {
        return ePieceType.KING.toString();
    }

    @Override
    public List<Move> findValidMoves(Board board) {
        List<Move> validMoves = new ArrayList();

        for (int direction : POSSIBLE_MOVE_CANDINATES) {
            if (isFirstColumnExclusion(this.SquareID, direction)
                    || isEighthColumnExclusion(this.SquareID, direction)) {
                continue;
            }

            int position = direction + this.getSquareID();

            if (eBoardUtils.isValidSquareID(position)) {
                Square square = board.getSquare(position);
                if (!square.isOccupied()) {
                    validMoves.add(new Move.NonAttackMove(board, this, position));
                } else {
                    if (square.getPiece().getColour() != this.getColour()) {
                        validMoves.add(new Move.AttackMove(board, this,
                                position, square.getPiece()));
                    }
                }
            }
        }
        return validMoves;
    }

    @Override
    public Piece movePiece(Move move) {
        return new King((move.getDestination()), colour);
    }

    private static boolean isFirstColumnExclusion(final int position,
            final int direction) {
        return eBoardUtils.INSTANCE.FIRST_COLUMN[position]
                && ((direction == -9) || (direction == -1)
                || (direction == 7));
    }

    private static boolean isEighthColumnExclusion(final int position,
            final int direction) {
        return eBoardUtils.INSTANCE.EIGHTH_COLUMN[position]
                && ((direction == -7) || (direction == 1)
                || (direction == 9));
    }
}
