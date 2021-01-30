package cz.cvut.fel.player;

import cz.cvut.fel.Board;
import cz.cvut.fel.Move;
import cz.cvut.fel.Square;
import cz.cvut.fel.eColour;
import cz.cvut.fel.pieces.Piece;
import cz.cvut.fel.pieces.Rook;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class for BLACK PLAYER
 *
 * @author frogp
 */
public class BlackPlayer extends Player {

    /**
     * Class constructor
     *
     * @param board current board
     * @param validMoves players valid moves
     * @param opponentValidMoves players opponent valid moves
     */
    public BlackPlayer(Board board, Collection<Move> validMoves, Collection<Move> opponentValidMoves) {
        super(board, validMoves, opponentValidMoves);
    }

    @Override
    public Collection<Piece> getAlivePieces() {
        return board.getBlackPieces();
    }

    @Override
    public eColour getColour() {
        return eColour.BLACK;
    }

    @Override
    public Player getOpponent() {
        return board.getWhitePlayer();
    }

    @Override
    protected Collection<Move> calculateCastles(Collection<Move> playerValidMoves, Collection<Move> opponentValidMoves) {
        final List<Move> kingCastles = new ArrayList<>();
        if (this.getKing().isFirstMove() && !this.isInCheck()) {
            // king castle
            if (!this.board.getSquare(5).isOccupied()
                    && !this.board.getSquare(6).isOccupied()) {
                final Square rookSquare = this.board.getSquare(7);

                if (rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnSquare(5, opponentValidMoves).isEmpty()
                            && Player.calculateAttacksOnSquare(6, opponentValidMoves).isEmpty()
                            && rookSquare.getPiece().getPieceType() == Piece.ePieceType.ROOK) {
                        kingCastles.add(new Move.KingCastleMove(this.board, this.king, 6,
                                (Rook) rookSquare.getPiece(), rookSquare.getSquareCoordinate(), 5));
                    }
                }
            }
            // queen castle
            if (!this.board.getSquare(1).isOccupied()
                    && !this.board.getSquare(2).isOccupied()
                    && !this.board.getSquare(3).isOccupied()) {
                final Square rookSquare = this.board.getSquare(0);

                if (rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove()
                        && Player.calculateAttacksOnSquare(2, opponentValidMoves).isEmpty()
                        && Player.calculateAttacksOnSquare(3, opponentValidMoves).isEmpty()
                        && rookSquare.getPiece().getPieceType() == Piece.ePieceType.ROOK) {
                    kingCastles.add(new Move.QueenCastleMove(this.board, this.king, 2,
                            (Rook) rookSquare.getPiece(), rookSquare.getSquareCoordinate(), 3));
                }
            }
        }
        return kingCastles;
    }
}
