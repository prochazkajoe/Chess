package cz.cvut.fel;

import cz.cvut.fel.pieces.Bishop;
import cz.cvut.fel.pieces.King;
import cz.cvut.fel.pieces.Knight;
import cz.cvut.fel.pieces.Pawn;
import cz.cvut.fel.pieces.Piece;
import cz.cvut.fel.pieces.Queen;
import cz.cvut.fel.pieces.Rook;
import cz.cvut.fel.player.BlackPlayer;
import cz.cvut.fel.player.Player;
import cz.cvut.fel.player.WhitePlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author frogp
 */
public class Board {

    private Square[] gameBoard;
    private final Collection<Piece> blackPieces;
    private final Collection<Piece> whitePieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    private final Pawn enPassantPawn;

    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.blackPieces = calculateAlivePieces(eColour.BLACK, this.gameBoard);
        this.whitePieces = calculateAlivePieces(eColour.WHITE, this.gameBoard);
        this.enPassantPawn = builder.enPassantPawn;

        final Collection<Move> blackValidMoves = calculateValidMoves(this.blackPieces);
        final Collection<Move> whiteValidMoves = calculateValidMoves(this.whitePieces);

        this.whitePlayer = new WhitePlayer(this, whiteValidMoves, blackValidMoves);
        this.blackPlayer = new BlackPlayer(this, blackValidMoves, whiteValidMoves);
        this.currentPlayer = builder.nextMoveColour.choosePlayer(this.whitePlayer, this.blackPlayer);

    }

    private static Square[] createGameBoard(Builder builder) {
        final Square[] gameBoardSquares = new Square[64];
        for (int i = 0; i < 64; i++) {
            gameBoardSquares[i] = Square.createSquare(i, builder.board.get(i));
        }
        return gameBoardSquares;
    }

    /**
     * Returns field of squares, which represents board.
     *
     * @return current chess board
     */
    public Square[] getBoard() {
        return gameBoard;
    }

    /**
     * Create classic(standart) board for chess game.
     *
     * @return classic board
     */
    public static Board createClassicBoard() {
        final Builder builder = new Builder();

        builder.nextMoveColour = eColour.WHITE;

        builder.setPiece(new Rook(0, eColour.BLACK));
        builder.setPiece(new Knight(1, eColour.BLACK));
        builder.setPiece(new Bishop(2, eColour.BLACK));
        builder.setPiece(new Queen(3, eColour.BLACK));
        builder.setPiece(new King(4, eColour.BLACK));
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
        builder.setPiece(new King(60, eColour.WHITE));
        builder.setPiece(new Bishop(61, eColour.WHITE));
        builder.setPiece(new Knight(62, eColour.WHITE));
        builder.setPiece(new Rook(63, eColour.WHITE));

        return builder.build();
    }

    /**
     * Sets the representation of board from given board.
     *
     * @param board board, which will be set to a current board
     */
    public void setBoard(Square[] board) {
        this.gameBoard = board;
    }

    /**
     * Returns a square, which is at given index.
     *
     * @param index index in board represantion
     * @return square at given index
     */
    public Square getSquare(int index) {
        return gameBoard[index];
    }

    /**
     * Returns player, which will be making next move.
     *
     * @return current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns player, which playes with white pieces.
     *
     * @return white player
     */
    public WhitePlayer getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Returns player, which playes with black pieces.
     *
     * @return
     */
    public BlackPlayer getBlackPlayer() {
        return blackPlayer;
    }

    /**
     * Returns list of all valid moves in current situation for both players.
     *
     * @return list of all valid moves
     */
    public List<Move> getAllValidMoves() {
        List<Move> allValidMoves = new ArrayList<>();
        allValidMoves.addAll(calculateValidMoves(blackPieces));
        allValidMoves.addAll(calculateValidMoves(whitePieces));
        return allValidMoves;
    }

    private static Collection<Piece> calculateAlivePieces(eColour colour, Square[] gameBoard) {
        List<Piece> alivePieces = new ArrayList<>();

        for (Square square : gameBoard) {
            if (square.isOccupied()) {
                Piece piece = square.getPiece();
                if (piece.getColour() == colour) {
                    alivePieces.add(piece);
                }
            }
        }
        return alivePieces;
    }

    private Collection<Move> calculateValidMoves(Collection<Piece> colourPieces) {

        List<Move> validMoves = new ArrayList<>();

        colourPieces.forEach((piece) -> {
            validMoves.addAll(piece.findValidMoves(this));
        });

        return validMoves;
    }

    /**
     * Returns a collection of all black pieces, which are on the board.
     *
     * @return collection of all black pieces
     */
    public Collection<Piece> getBlackPieces() {
        return blackPieces;
    }

    /**
     * Returns a collection of all white pieces, which are on the board.
     *
     * @return collection of all white pieces
     */
    public Collection<Piece> getWhitePieces() {
        return whitePieces;
    }

    /**
     * Returns a pawn, on which we can enPassanted.
     *
     * @return enPassant pawn
     */
    public Pawn getEnPassantPawn() {
        return enPassantPawn;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 64; i++) {
            final String squareString = this.getSquare(i).toString();
            builder.append(String.format("%3s", squareString));

            if ((((i + 1) % 8)) == 0) {
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    /**
     * Class for building a board.
     */
    public static class Builder {

        Map<Integer, Piece> board;
        eColour nextMoveColour;
        Move transitionMove;
        Pawn enPassantPawn;

        /**
         * Class constructor
         */
        public Builder() {
            this.board = new HashMap<>();
        }

        /**
         * Returns a board, which is build from plan (hashmap).
         *
         * @return
         */
        public Board build() {
            return new Board(this);
        }

        /**
         * Sets piece on a builders plan
         *
         * @param piece piece to set
         * @return changed builder
         */
        public Builder setPiece(Piece piece) {
            this.board.put(piece.getSquareID(), piece);
            return this;
        }

        /**
         * Sets the colour of player, which will be making next move.
         *
         * @param colour colour of player of next move
         * @return changed builder
         */
        public Builder setMoveColour(eColour colour) {
            this.nextMoveColour = colour;
            return this;
        }

        /**
         *
         * @param transitionMove
         * @return
         */
        public Builder setTransitionMove(Move transitionMove) {
            this.transitionMove = transitionMove;
            return this;
        }

        /**
         * Sets the pawn, which can be taken by enPassant move.
         *
         * @param movedPawn enPassant pawn
         */
        public void setEnPassantPawn(Pawn movedPawn) {
            this.enPassantPawn = movedPawn;
        }
    }
}
