package cz.cvut.fel;

import cz.cvut.fel.Board.Builder;
import cz.cvut.fel.pieces.Bishop;
import cz.cvut.fel.pieces.Knight;
import cz.cvut.fel.pieces.Pawn;
import cz.cvut.fel.pieces.Queen;
import cz.cvut.fel.pieces.Rook;
import org.junit.Test;

/**
 *
 * @author frogp
 */
public class BoardTest {

    @Test(expected = RuntimeException.class)
    public void testInvalidBoard() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, eColour.BLACK));
        builder.setPiece(new Knight(1, eColour.BLACK));
        builder.setPiece(new Bishop(2, eColour.BLACK));
        builder.setPiece(new Queen(3, eColour.BLACK));
        builder.setPiece(new Bishop(5, eColour.BLACK));
        builder.setPiece(new Knight(6, eColour.BLACK));
        builder.setPiece(new Rook(7, eColour.BLACK));
        builder.setPiece(new Pawn(8, eColour.BLACK));
        builder.setPiece(new Pawn(9, eColour.BLACK));
        builder.setPiece(new Pawn(10, eColour.BLACK));
        builder.setPiece(new Pawn(11, eColour.BLACK));
        builder.setPiece(new Pawn(12, eColour.BLACK));
        builder.setPiece(new Pawn(13, eColour.BLACK));
        builder.setPiece(new Pawn(14, eColour.BLACK));
        builder.setPiece(new Pawn(15, eColour.BLACK));
        // White Layout
        builder.setPiece(new Pawn(48, eColour.WHITE));
        builder.setPiece(new Pawn(49, eColour.WHITE));
        builder.setPiece(new Pawn(50, eColour.WHITE));
        builder.setPiece(new Pawn(51, eColour.WHITE));
        builder.setPiece(new Pawn(52, eColour.WHITE));
        builder.setPiece(new Pawn(53, eColour.WHITE));
        builder.setPiece(new Pawn(54, eColour.WHITE));
        builder.setPiece(new Pawn(55, eColour.WHITE));
        builder.setPiece(new Rook(56, eColour.WHITE));
        builder.setPiece(new Knight(57, eColour.WHITE));
        builder.setPiece(new Bishop(58, eColour.WHITE));
        builder.setPiece(new Queen(59, eColour.WHITE));
        builder.setPiece(new Bishop(61, eColour.WHITE));
        builder.setPiece(new Knight(62, eColour.WHITE));
        builder.setPiece(new Rook(63, eColour.WHITE));
        //white to move
        builder.setMoveColour(eColour.WHITE);
        //build the board
        builder.build();
    }

}
