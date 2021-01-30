package cz.cvut.fel.pieces;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.Square;
import cz.cvut.fel.eBoardUtils;
import cz.cvut.fel.eColour;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for ROOK
 *
 * @author frogp
 */
public class Rook extends Piece {

    private final int[] POSSIBLE_MOVE_VECTOR_CANDINATES = {-8, -1, 1, 8};

    /**
     * Class constructor
     *
     * @param squareID position of rook
     * @param colour colour of rook
     */
    public Rook(int squareID, eColour colour) {
        super(ePieceType.ROOK, squareID, colour, true);
    }

    /**
     * Class constructor
     *
     * @param squareID position of rook
     * @param colour colour of rook
     * @param isFirstMove true if is first move, otherwise false
     */
    public Rook(int squareID, eColour colour, boolean isFirstMove) {
        super(ePieceType.ROOK, squareID, colour, isFirstMove);
    }

    @Override
    public String toString() {
        return ePieceType.ROOK.toString();
    }

    @Override
    public List<Move> findValidMoves(Board board) {

        List<Move> validMoves = new ArrayList();

        for (int direction : POSSIBLE_MOVE_VECTOR_CANDINATES) {
            int position = this.SquareID;
            while (eBoardUtils.isValidSquareID(position)) {
                if (isColumnExclusion(direction, position)) {
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
        return new Rook((move.getDestination()), colour);
    }

    private static boolean isColumnExclusion(final int direction,
            final int position) {
        return (eBoardUtils.INSTANCE.FIRST_COLUMN[position] && (direction == -1))
                || (eBoardUtils.INSTANCE.EIGHTH_COLUMN[position] && (direction == 1));
    }
}
