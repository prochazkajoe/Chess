package cz.cvut.fel;

import cz.cvut.fel.gui.Table;

/**
 *
 * @author frogp
 */
public class ChessGame {

    /**
     * Main class for chess game. By launching this class, you will start the
     * game.
     *
     * @param args args from command line
     */
    public static void main(String[] args) {

        Board.createClassicBoard();

        Table.get().show();
    }
}
