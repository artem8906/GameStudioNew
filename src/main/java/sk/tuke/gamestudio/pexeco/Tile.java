package sk.tuke.gamestudio.pexeco;

import java.io.Serializable;

public class Tile implements Serializable {
    char value;

    public enum State {
    OPEN, CLOSED, SOLVED
    }

    public int getValue() {
            return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public Tile(char value) {
        this.value = value;
    }

    public Tile() {
    }

    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        if (state.equals(State.OPEN))
        return String.valueOf(value);
        else return "";

    }


}
