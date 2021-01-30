package cz.cvut.fel.pieces;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.Square;
import cz.cvut.fel.eBoardUtils;
import cz.cvut.fel.eColour;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for QUEEN
 *
 * @author frogp
 */
public class Queen extends Piece {

    private final int[] POSSIBLE_MOVE_VECTOR_CANDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    /**
     * Class constructor
     *
     * @param squareID position of queen
     * @param colour colour of queen
     */
    public Queen(int squareID, eColour colour) {
        super(ePieceType.QUEEN, squareID, colour, true);
    }

    /**
     * Class constructor
     *
     * @param squareID position of queen
     * @param colour colour of queen
     * @param isFirstMove true if is first move, otherwise false
     */
    public Queen(int squareID, eColour colour, boolean isFirstMove) {
        super(ePieceType.QUEEN, squareID, colour, isFirstMove);
    }

    @Override
    public String toString() {
        return ePieceType.QUEEN.toString();
    }

    @Override
    public List<Move> findValidMoves(Board board) {
        List<Move> validMoves = new ArrayList();

        for (int direction : POSSIBLE_MOVE_VECTOR_CANDINATES) {
            int position = this.SquareID;
            while (true) {
                if (isFirstColumnExclusion(direction, position)
                        || isEightColumnExclusion(direction, position)) {
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
        return new Queen((move.getDestination()), colour);
    }

    private static boolean isFirstColumnExclusion(final int direction,
            final int position) {
        return eBoardUtils.INSTANCE.FIRST_COLUMN[position] && ((direction == -9)
                || (direction == -1) || (direction == 7));
    }

    private static boolean isEightColumnExclusion(final int direction,
            final int position) {
        return eBoardUtils.INSTANCE.EIGHTH_COLUMN[position] && ((direction == -7)
                || (direction == 1) || (direction == 9));
    }
}
