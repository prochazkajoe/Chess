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
 * Class for WHITE PLAYER
 *
 * @author frogp
 */
public class WhitePlayer extends Player {

    /**
     * Class constructor
     *
     * @param board current board
     * @param validMoves players valid moves
     * @param opponentValidMoves players opponent valid moves
     */
    public WhitePlayer(Board board, Collection<Move> validMoves, Collection<Move> opponentValidMoves) {
        super(board, validMoves, opponentValidMoves);
    }

    @Override
    public Collection<Piece> getAlivePieces() {
        return board.getWhitePieces();
    }

    @Override
    public eColour getColour() {
        return eColour.WHITE;
    }

    @Override
    public Player getOpponent() {
        return board.getBlackPlayer();
    }

    @Override
    protected Collection<Move> calculateCastles(Collection<Move> playerValidMoves, Collection<Move> opponentValidMoves) {
        final List<Move> kingCastles = new ArrayList<>();
        if (this.getKing().isFirstMove() && !this.isInCheck()) {
            // king castle
            if (!this.board.getSquare(61).isOccupied()
                    && !this.board.getSquare(62).isOccupied()) {
                final Square rookSquare = this.board.getSquare(63);

                if (rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnSquare(61, opponentValidMoves).isEmpty()
                            && Player.calculateAttacksOnSquare(62, opponentValidMoves).isEmpty()
                            && rookSquare.getPiece().getPieceType() == Piece.ePieceType.ROOK) {
                        kingCastles.add(new Move.KingCastleMove(this.board, this.king, 62,
                                (Rook) rookSquare.getPiece(), rookSquare.getSquareCoordinate(), 61));
                    }
                }
            }
            // queen castle
            if (!this.board.getSquare(59).isOccupied()
                    && !this.board.getSquare(58).isOccupied()
                    && !this.board.getSquare(57).isOccupied()) {
                final Square rookSquare = this.board.getSquare(56);

                if (rookSquare.isOccupied() && rookSquare.getPiece().isFirstMove()
                        && Player.calculateAttacksOnSquare(58, opponentValidMoves).isEmpty()
                        && Player.calculateAttacksOnSquare(59, opponentValidMoves).isEmpty()
                        && rookSquare.getPiece().getPieceType() == Piece.ePieceType.ROOK) {
                    kingCastles.add(new Move.QueenCastleMove(this.board, this.king, 58,
                            (Rook) rookSquare.getPiece(), rookSquare.getSquareCoordinate(), 59));
                }
            }
        }
        return kingCastles;
    }
}
