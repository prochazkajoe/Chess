package cz.cvut.fel;

import cz.cvut.fel.pieces.King;
import cz.cvut.fel.pieces.Pawn;
import cz.cvut.fel.player.MoveMaker;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Created by frogp
 */
public class PieceTest {

    @Test
    public void testWhiteEnPassant() {
        final Board.Builder builder = new Board.Builder();
        // black Layout
        builder.setPiece(new King(4, eColour.BLACK, false));
        builder.setPiece(new Pawn(11, eColour.BLACK));
        // white Layout
        builder.setPiece(new Pawn(52, eColour.WHITE));
        builder.setPiece(new King(60, eColour.WHITE, false));
        // set the current player
        builder.setMoveColour(eColour.WHITE);
        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, eBoardUtils.INSTANCE.getIndexAtPosition(
                "e2"), eBoardUtils.INSTANCE.getIndexAtPosition("e4"));
        final MoveMaker t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("e8"), eBoardUtils.INSTANCE.getIndexAtPosition("d8"));
        final MoveMaker t2 = t1.getBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("e4"), eBoardUtils.INSTANCE.getIndexAtPosition("e5"));
        final MoveMaker t3 = t2.getBoard().getCurrentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("d7"), eBoardUtils.INSTANCE.getIndexAtPosition("d5"));
        final MoveMaker t4 = t3.getBoard().getCurrentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("e5"), eBoardUtils.INSTANCE.getIndexAtPosition("d6"));
        final MoveMaker t5 = t4.getBoard().getCurrentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
    }

    @Test
    public void testBlackEnPassant() {
        final Board.Builder builder = new Board.Builder();
        // black Layout
        builder.setPiece(new King(4, eColour.BLACK, false));
        builder.setPiece(new Pawn(11, eColour.BLACK));
        // white Layout
        builder.setPiece(new Pawn(52, eColour.WHITE));
        builder.setPiece(new King(60, eColour.WHITE, false));
        // set the current player
        builder.setMoveColour(eColour.WHITE);
        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, eBoardUtils.INSTANCE.getIndexAtPosition(
                "e1"), eBoardUtils.INSTANCE.getIndexAtPosition("d1"));
        final MoveMaker t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("d7"), eBoardUtils.INSTANCE.getIndexAtPosition("d5"));
        final MoveMaker t2 = t1.getBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("d1"), eBoardUtils.INSTANCE.getIndexAtPosition("c1"));
        final MoveMaker t3 = t2.getBoard().getCurrentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("d5"), eBoardUtils.INSTANCE.getIndexAtPosition("d4"));
        final MoveMaker t4 = t3.getBoard().getCurrentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("e2"), eBoardUtils.INSTANCE.getIndexAtPosition("e4"));
        final MoveMaker t5 = t4.getBoard().getCurrentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
        final Move m6 = Move.MoveFactory.createMove(t5.getBoard(), eBoardUtils.INSTANCE.getIndexAtPosition("d4"), eBoardUtils.INSTANCE.getIndexAtPosition("e3"));
        final MoveMaker t6 = t5.getBoard().getCurrentPlayer().makeMove(m6);
        assertTrue(t6.getMoveStatus().isDone());
    }

    @Test
    public void testKingEquality() {
        final Board board = Board.createClassicBoard();
        final Board board2 = Board.createClassicBoard();
        assertEquals(board.getSquare(60).getPiece(), board2.getSquare(60).getPiece());
        assertFalse(board.getSquare(60).getPiece().equals(null));
    }

}
