package sk.tuke.gamestudio.minesweeper;

import sk.tuke.gamestudio.minesweeper.consoleui.ConsoleUIMinesSweeper;

public class Minesweeper {

    private static Minesweeper instance;

    public static Minesweeper getInstance() {
        if (instance == null) {
            new Minesweeper();
        }
        return instance;
    }

    private Minesweeper() {
        instance = this;
          final UserInterface userInterface = new ConsoleUIMinesSweeper();
          userInterface.play();
    }

    public static void main(String[] args) {
        new Minesweeper();
    }

}

