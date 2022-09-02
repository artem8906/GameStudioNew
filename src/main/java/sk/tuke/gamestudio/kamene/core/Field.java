package sk.tuke.gamestudio.kamene.core;

import java.io.*;
import java.util.Random;

public class Field implements Serializable {

    private final Tile[][] tiles;

    public Tile[][] getTiles() {
        return tiles;
    }

    private final int size;

    private long startTime;

    public void setScore(int score) {
        this.score = score;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private GameState gameState = GameState.PLAYING;

    public GameState getGameState() {
        return gameState;
    }

    private int score;

    public int getScore() {
        return score;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getSize() {
        return size;
    }

    private boolean scoreCounted = false;

    private boolean isAddscore = false;

    public boolean isAddscore() {
        return isAddscore;
    }

    public void setAddscore(boolean addscore) {
        isAddscore = addscore;
    }

    public Field(int size) {
        this.size = size;
        tiles = new Tile[size][size];
        startTime = System.currentTimeMillis();
        generate();
        randomShuffle();
    }

    public Tile getTile(int a, int b) {
        return getTiles()[a][b];
    }


    public void updateState() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j].setState(null);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile tile = tiles[i][j];
                if ((j + 1 < size) && tiles[i][j + 1].getValue() == 0) tile.setState(Tile.State.ABLEMOVETORIGHT);
                if ((j - 1 >= 0) && tiles[i][j - 1].getValue() == 0) tile.setState(Tile.State.ABLEMOVETOLEFT);
                if ((i + 1 < size) && tiles[i + 1][j].getValue() == 0) tile.setState(Tile.State.ABLEMOVETODOWN);
                if ((i - 1 >= 0) && tiles[i - 1][j].getValue() == 0) tile.setState(Tile.State.ABLEMOVETOUP);
            }
        }

    }

    public void move(String action) {
        int tempValue;

        switch (action) {
            case "DOWN":
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (tiles[i][j].getState() == Tile.State.ABLEMOVETOUP) {
                            tempValue = tiles[i][j].getValue();
                            tiles[i][j].setValue(tiles[i - 1][j].value);
                            tiles[i - 1][j].setValue(tempValue);
                            break;
                        }
                    }
                }
                break;

            case "UP":
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (tiles[i][j].getState() == Tile.State.ABLEMOVETODOWN) {
                            tempValue = tiles[i][j].getValue();
                            tiles[i][j].setValue(tiles[i + 1][j].value);
                            tiles[i + 1][j].setValue(tempValue);
                            break;
                        }
                    }
                }
                break;

            case "RIGHT":
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (tiles[i][j].getState() == Tile.State.ABLEMOVETOLEFT) {
                            int value = tiles[i][j].getValue();
                            tiles[i][j].setValue(tiles[i][j - 1].value);
                            tiles[i][j - 1].setValue(value);
                            break;
                        }
                    }
                }
                break;

            case "LEFT":
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (tiles[i][j].getState() == Tile.State.ABLEMOVETORIGHT) {
                            int value = tiles[i][j].getValue();
                            tiles[i][j].setValue(tiles[i][j + 1].value);
                            tiles[i][j + 1].setValue(value);
                            break;
                        }
                    }
                }
                break;
        }
    }

    public void generate() {
        int count = 1;
        tiles[3][2] = new Tile();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile tile = new Tile();
                if (tiles[i][j] == null) {
                    tiles[i][j] = tile;
                    tile.setValue(count++);

                }
            }
        }
    }

    public void randomShuffle() {
        Random rd = new Random();
        Tile temp;
        int random1_i = rd.nextInt(4);
        int random1_j = rd.nextInt(4);
        int random2_i = rd.nextInt(4);
        int random2_j = rd.nextInt(4);
        temp = tiles[random1_i][random1_j];
        tiles[random1_i][random1_j] = tiles[random2_i][random2_i];
        tiles[random2_i][random2_i] = temp;

    }

    public boolean isSolved() {
        int count = 1;
        int idealCount = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j].value == count)
                    idealCount++;
                count++;
            }
        }
        if (tiles[size - 1][size - 1].value == 0) idealCount++;

        if (count == idealCount) {
            gameState = GameState.SOLVED;
            if (!scoreCounted) {
                int timeOfPlay = (int) (System.currentTimeMillis() - getStartTime()) / 1000;
                score = this.size * 100 - timeOfPlay;
                scoreCounted = true;
            }
            return true;
        } else return false;
    }


}




