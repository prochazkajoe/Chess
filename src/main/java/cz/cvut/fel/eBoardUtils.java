package cz.cvut.fel;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author frogp
 */
public enum eBoardUtils {

    /**
     * Instance of enum Bord utils.
     */
    INSTANCE;

    /**
     * Number of squares.
     */
    public static final int NUM_SQUARES = 64;

    /**
     * Number of squares per row.
     */
    public static final int NUM_SQUARES_PER_ROW = 8;

    /**
     * Field of booleans contains true if index is in first column, otherwise
     * false.
     */
    public final boolean[] FIRST_COLUMN = initColumn(0);

    /**
     * Field of booleans contains true if index is in second column, otherwise
     * false.
     */
    public final boolean[] SECOND_COLUMN = initColumn(1);

    /**
     * Field of booleans contains true if index is in third column, otherwise
     * false.
     */
    public final boolean[] THIRD_COLUMN = initColumn(2);

    /**
     * Field of booleans contains true if index is in fourth column, otherwise
     * false.
     */
    public final boolean[] FOURTH_COLUMN = initColumn(3);

    /**
     * Field of booleans contains true if index is in fifth column, otherwise
     * false.
     */
    public final boolean[] FIFTH_COLUMN = initColumn(4);

    /**
     * Field of booleans contains true if index is in sixth column, otherwise
     * false.
     */
    public final boolean[] SIXTH_COLUMN = initColumn(5);

    /**
     * Field of booleans contains true if index is in seventh column, otherwise
     * false.
     */
    public final boolean[] SEVENTH_COLUMN = initColumn(6);

    /**
     * Field of booleans contains true if index is in eight column, otherwise
     * false.
     */
    public final boolean[] EIGHTH_COLUMN = initColumn(7);

    /**
     * Field of booleans contains true if index is in eigth rank, otherwise
     * false.
     */
    public final boolean[] EIGHTH_RANK = initRow(0);

    /**
     * Field of booleans contains true if index is in seventh rank, otherwise
     * false.
     */
    public final boolean[] SEVENTH_RANK = initRow(8);

    /**
     * Field of booleans contains true if index is in sixth rank, otherwise
     * false.
     */
    public final boolean[] SIXTH_RANK = initRow(16);

    /**
     * Field of booleans contains true if index is in fifth rank, otherwise
     * false.
     */
    public final boolean[] FIFTH_RANK = initRow(24);

    /**
     * Field of booleans contains true if index is in fourth rank, otherwise
     * false.
     */
    public final boolean[] FOURTH_RANK = initRow(32);

    /**
     * Field of booleans contains true if index is in third rank, otherwise
     * false.
     */
    public final boolean[] THIRD_RANK = initRow(40);

    /**
     * Field of booleans contains true if index is in second rank, otherwise
     * false.
     */
    public final boolean[] SECOND_RANK = initRow(48);

    /**
     * Field of booleans contains true if index is in first rank, otherwise
     * false.
     */
    public final boolean[] FIRST_RANK = initRow(56);

    /**
     * Field of strings contains name of square at index.
     */
    public final String[] NAMES_OF_SQUARES = initializeNameOfSquares();

    /**
     * Map for saving/loading name of square.
     */
    public final Map<String, Integer> POSITION_TO_INDEX = initializePositionToIndexMap();

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_SQUARES];
        for (int i = 0; i < column.length; i++) {
            column[i] = false;
        }
        do {
            column[columnNumber] = true;
            columnNumber += NUM_SQUARES_PER_ROW;
        } while (columnNumber < NUM_SQUARES);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_SQUARES];
        for (int i = 0; i < row.length; i++) {
            row[i] = false;
        }
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % NUM_SQUARES_PER_ROW != 0);
        return row;
    }

    private static String[] initializeNameOfSquares() {
        return new String[]{
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    private Map<String, Integer> initializePositionToIndexMap() {
        final Map<String, Integer> positionToIndex = new HashMap<>();
        for (int i = 0; i < NUM_SQUARES; i++) {
            positionToIndex.put(NAMES_OF_SQUARES[i], i);
        }
        return positionToIndex;
    }

    /**
     * Returns true if ID is valid, otherwise false
     *
     * @param ID index of square
     * @return true/false
     */
    public static boolean isValidSquareID(int ID) {
        return ID >= 0 && ID <= 63;
    }

    /**
     * Returns a index of square of given position.
     *
     * @param position name of square
     * @return name of square
     */
    public int getIndexAtPosition(final String position) {
        return POSITION_TO_INDEX.get(position);
    }

    /**
     * Returns a name of square of given index.
     *
     * @param index index of position in board represantation
     * @return index of position
     */
    public String getPositionAtIndex(final int index) {
        return NAMES_OF_SQUARES[index];
    }
}
