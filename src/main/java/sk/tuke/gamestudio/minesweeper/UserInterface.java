package sk.tuke.gamestudio.minesweeper;

import sk.tuke.gamestudio.minesweeper.core.Field;

import java.io.IOException;

public interface UserInterface {
    void newGameStarted(Field field);
    void play();

    void update();
}
