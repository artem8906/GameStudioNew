package sk.tuke.gamestudio.minesweeper.core;

import java.util.Random;

/**
 * Field represents playing field and game logic.
 */
public class Field {
    /**
     * Playing field tiles.
     */
    private final Tile[][] tiles;

    private static long startMillis = System.currentTimeMillis();
    private boolean marking = false;
    private boolean justFinished = false;

    public Tile[][] getTiles() {
        return tiles;
    }

    /**
     * Field row count. Rows are indexed from 0 to (rowCount - 1).
     */
    private final int rowCount;

    /**
     * Column count. Columns are indexed from 0 to (columnCount - 1).
     */
    private final int columnCount;

    /**
     * Mine count.
     */
    private final int mineCount;

    /**
     * Game state.
     */
    private GameState state = GameState.PLAYING;

    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Constructor.
     *
     * @param rowCount    row count
     * @param columnCount column count
     * @param mineCount   mine count
     */
    public Field(int rowCount, int columnCount, int mineCount) {

        if ( mineCount > (rowCount * columnCount) ) {
            throw new IllegalArgumentException();
        }
        else {
            this.mineCount = mineCount;
        }
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        tiles = new Tile[rowCount][columnCount];

        //generate the field content
        generate();
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public GameState getState() {
        return state;
    }

    public Tile getTile(int row, int column) {
        return tiles[row][column];
    }

    /**
     * Opens tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void openTile(int row, int column) {
        Tile tile = tiles[row][column];
        if (tile.getState() == Tile.State.CLOSED) {
            tile.setState(Tile.State.OPEN);
            if(tile instanceof Clue && ((Clue)tile).getValue() == 0) {
                getOpenAdjacentTiles(row, column);
            }
            if (tile instanceof Mine) {
                    setState(GameState.FAILED);
                    return;
                }
            }
            if (isSolved()) {
                state = GameState.SOLVED;
                return;
            }
        }


    /**
     * Marks tile at specified indeces.
     *
     * @param row    row number
     * @param column column number
     */
    public void markTile(int row, int column) {
        switch (tiles[row][column].getState()) {
            case CLOSED:
                tiles[row][column].setState(Tile.State.MARKED);
                break;
            case MARKED:
                tiles[row][column].setState(Tile.State.CLOSED);
                break;
        }
    }

    /**
     * Generates playing field.
     */
    private void generate() {
        Random rd = new Random();
        int count = 0;
        while (count < mineCount) {
            int row = rd.nextInt(rowCount);
            int column = rd.nextInt(columnCount);

            if (tiles[row][column] == null) {
                tiles[row][column] = new Mine();
                count++;
            }
        }

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == null)
                    tiles[i][j] = new Clue(countAdjacentMines(i, j));
            }
        }
    }

    /**
     * Returns true if game is solved, false otherwise.
     *
     * @return true if game is solved, false otherwise
     */
    public boolean isSolved() {
        return (getNumberOf(Tile.State.OPEN) + mineCount) == rowCount * columnCount;
    }

    public int getNumberOf(Tile.State state) {
        int count = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].getState().equals(state))
                    count++;
            }
        }
        return count;
    }

    /**
     * Returns number of adjacent mines for a tile at specified position in the field.
     *
     * @param row    row number.
     * @param column column number.
     * @return number of adjacent mines.
     */
    private int countAdjacentMines(int row, int column) {
        int count = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        if (tiles[actRow][actColumn] instanceof Mine) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public int getRemainingMineCount() {
        int count = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j].getState()== Tile.State.CLOSED && tiles[i][j] instanceof Mine)
                    count++;
            }
        }
        return count;
    }
    private void getOpenAdjacentTiles(int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            int actRow = row + rowOffset;
            if (actRow >= 0 && actRow < rowCount) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    int actColumn = column + columnOffset;
                    if (actColumn >= 0 && actColumn < columnCount) {
                        openTile(actRow, actColumn);
                    }
                }
            }
        }
    }

    public int getScore() {
        return rowCount * columnCount * 10 - getPlayTimeInSeconds();
    }
    public int getPlayTimeInSeconds(){
        return (int) ((System.currentTimeMillis() - startMillis)/1000);
    }

    public boolean isMarking() {
        return marking;
    }

    public void setMarking(boolean marking) {
        this.marking = marking;
    }

    public boolean isJustFinished() {
        return justFinished;
    }

    public void setJustFinished(boolean justFinished) {
        this.justFinished = justFinished;
    }
}
