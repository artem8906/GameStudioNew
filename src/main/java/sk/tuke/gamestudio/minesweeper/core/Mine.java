package sk.tuke.gamestudio.minesweeper.core;

/**
 * Mine tile.
 */
public class Mine extends Tile {
    @Override
    public String toString() {
        if (getState().equals(State.MARKED)) return "M";
        else if (getState().equals(State.OPEN)) return "*";
        else return super.toString();
    }
}
