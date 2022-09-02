package sk.tuke.gamestudio.kamene.core;

import java.io.Serializable;

public class Tile implements Serializable {
    int value;

    public enum State {
        ABLEMOVETORIGHT,
        ABLEMOVETOLEFT,
        ABLEMOVETOUP,
        ABLEMOVETODOWN,
    }

    public int getValue() {
            return value;
    }

    public void setValue(int value) {
        this.value = value;
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
        if (value != 0) return String.format(" %2d ", value);
        else return "    ";
    }


}
