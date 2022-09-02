package sk.tuke.gamestudio.pexeco;

import java.io.Serializable;
import java.util.Random;

public class Field{

    private final Tile[][] tiles;

    public Tile[][] getTiles() {
        return tiles;
    }

    private final int size;

    private GameState gameState = GameState.PLAYING;

    public GameState getGameState() {
        return gameState;
    }

    private static int countUnValuedPair = 8;


    private boolean justFinished = false;


    public Field(int size) {
        this.size = size;
        tiles = new Tile[size][size];
        generate();
    }

    public Tile getTile(int a, int b) {
        return getTiles()[a][b];
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


    public void open(int a, int b) {
        tiles[a][b].setState(Tile.State.OPEN);
    }


    public void generate() {

        char value = 65;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
            tiles[i][j] = new Tile();
            tiles[i][j].setState(Tile.State.CLOSED);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                tiles[i][j].setValue(value);
                if (j != size-1) {
                    tiles[i][j + 1].setValue(value);
                    value++;
                    j++;
                }
                else if (i != size-1) {
                    tiles[i + 1][0].setValue(value);
                    value++;
                    j++;
                }
            }
        }
        for (int i = 0; i < 15; i++) {
            randomShuffle();
        }

    }

    public void closeUnsolved() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j].getState() != Tile.State.SOLVED) {
                        tiles[i][j].setState(Tile.State.CLOSED);
                }
            }
        }
    }

    public boolean isSolved() {
        int count = 1;
        int idealCount = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j].getState() == Tile.State.SOLVED)
                    idealCount++;
                count++;
            }
        }
        if (count == idealCount) {
            gameState = GameState.SOLVED;
            return true;
        } else return false;

    }
    public GameState getState() {
        return gameState;
    }

    public boolean isJustFinished() {
        return justFinished;
    }

    public void setJustFinished(boolean justFinished) {
        this.justFinished = justFinished;
    }
}




