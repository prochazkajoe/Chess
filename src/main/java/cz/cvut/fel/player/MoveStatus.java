package cz.cvut.fel.player;

/**
 * Enum for progress of move.
 *
 * @author frogp
 */
public enum MoveStatus {

    /**
     * Done status
     */
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    /**
     * Nonvalid move status
     */
    NONVALID_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }

    },
    /**
     * Leaves player in check status
     */
    LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isDone() {
            return false;
        }

    };

    /**
     * Returns true, if move is done, otherwise false.
     *
     * @return
     */
    public abstract boolean isDone();
}
