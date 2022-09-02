package sk.tuke.gamestudio.kamene;

import sk.tuke.gamestudio.kamene.consoleui.ConsoleUIKamene;

public class Kamene {

    private static Kamene instance;

    public static Kamene getInstance() {
        if (instance==null) instance = new Kamene();
        return instance;
    }

    private Kamene() {
        instance = this;
        final ConsoleUIKamene userInterface = new ConsoleUIKamene();
        userInterface.play();
    }

    public static void main(String[] args) {
        getInstance();
    }
}

