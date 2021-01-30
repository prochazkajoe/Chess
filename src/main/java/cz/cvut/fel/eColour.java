package cz.cvut.fel;

import cz.cvut.fel.player.BlackPlayer;
import cz.cvut.fel.player.Player;
import cz.cvut.fel.player.WhitePlayer;

/**
 * Enumeration for black and white.
 *
 * @author frogp
 */
public enum eColour {

    /**
     * enum for white colour
     */
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(int index) {
            return eBoardUtils.INSTANCE.EIGHTH_RANK[index];
        }

        @Override
        public String toString() {
            return "WHITE";
        }

    },
    /**
     * enum for black colour
     */
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isPawnPromotionSquare(int index) {
            return eBoardUtils.INSTANCE.FIRST_RANK[index];
        }

        @Override
        public String toString() {
            return "BLACK";
        }
    };

    /**
     * Returns a direction of moving for black/white colour.
     *
     * @return 1/-1
     */
    public abstract int getDirection();

    /**
     * Returns a opposite direction of moving for black/white colour.
     *
     * @return -1/1
     */
    public abstract int getOppositeDirection();

    /**
     * Returns true if sqauare at index is pawn promotion square, otherwise
     * false.
     *
     * @param index index of sqaure
     * @return boolean
     */
    public abstract boolean isPawnPromotionSquare(int index);

    /**
     * Chooses a player based on their colour.
     *
     * @param whitePlayer white player
     * @param blackPlayer black player
     * @return player
     */
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
