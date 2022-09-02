package sk.tuke.gamestudio.minesweeper.core;

/**
 * Clue tile.
 */
public class Clue  extends Tile {
    /** Value of the clue. */
    private final int value;
    
    /**
     * Constructor.
     * @param value  value of the clue
     */
    public Clue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (getState().equals(State.MARKED)) return "M";
        else if (getState().equals(State.OPEN)) return Integer.toString(getValue());
        else return super.toString();
    }
}
