package cz.cvut.fel.pieces;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.Square;
import cz.cvut.fel.eBoardUtils;
import cz.cvut.fel.eColour;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for BISHOP
 *
 * @author frogp
 */
public class Bishop extends Piece {

    private final int[] POSSIBLE_MOVE_VECTOR_CANDINATES = {-9, -7, 7, 9};

    /**
     * Class constructor
     *
     * @param squareID position of bishop
     * @param colour colour of bishop
     */
    public Bishop(int squareID, eColour colour) {
        super(ePieceType.BISHOP, squareID, colour, true);
    }

    /**
     * Class constructor
     *
     * @param squareID position of bishop
     * @param colour colour of bishop
     * @param isFirstMove true if is first move, otherwise false
     */
    public Bishop(int squareID, eColour colour, boolean isFirstMove) {
        super(ePieceType.BISHOP, squareID, colour, isFirstMove);
    }

    @Override
    public String toString() {
        return ePieceType.BISHOP.toString();
    }

    @Override
    public List<Move> findValidMoves(Board board) {
        List<Move> validMoves = new ArrayList();

        for (int direction : POSSIBLE_MOVE_VECTOR_CANDINATES) {
            int position = this.getSquareID();

            while (eBoardUtils.isValidSquareID(position)) {

                if (isFirstColumnExclusion(direction, position)
                        || isEighthColumnExclusion(direction, position)) {
                    break;
                }
                position += direction;

                if (!eBoardUtils.isValidSquareID(position)) {
                    break;
                }
                Square square = board.getSquare(position);
                if (!square.isOccupied()) {
                    validMoves.add(new Move.NonAttackMove(board, this, position));
                } else {
                    if (square.getPiece().getColour() != this.getColour()) {
                        validMoves.add(new Move.AttackMove(board, this,
                                position, square.getPiece()));
                    }
                    break;
                }
            }
        }
        return validMoves;
    }

    @Override
    public Piece movePiece(Move move) {
        return new Bishop((move.getDestination()), colour);
    }

    private static boolean isFirstColumnExclusion(final int direction,
            final int position) {
        return (eBoardUtils.INSTANCE.FIRST_COLUMN[position]
                && ((direction == -9) || (direction == 7)));
    }

    private static boolean isEighthColumnExclusion(final int direction,
            final int position) {
        return eBoardUtils.INSTANCE.EIGHTH_COLUMN[position]
                && ((direction == -7) || (direction == 9));
    }
}
