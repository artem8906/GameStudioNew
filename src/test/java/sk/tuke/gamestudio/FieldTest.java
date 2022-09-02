package sk.tuke.gamestudio;

import sk.tuke.gamestudio.minesweeper.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FieldTest {

        private Random randomGenerator = new Random();
        private Field field;
        private int rowCount;
        private int columnCount;
        private int minesCount;

        @BeforeEach
        public void initTest() {
            rowCount = randomGenerator.nextInt(10) + 5;
            columnCount = rowCount;
            minesCount = Math.max(1, randomGenerator.nextInt(rowCount * columnCount));
            field = new Field(rowCount, columnCount, minesCount);
        }
        @Test
        public void checkFieldInitialization() {
            assertEquals(rowCount, field.getRowCount());
            assertEquals(columnCount, field.getColumnCount());
            assertEquals(minesCount, field.getMineCount());
        }

        @Test
        public void checkOpenMine() {
            int rowIndex = 0;
            int colIndex = 0;
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    if (field.getTiles()[i][j] instanceof Mine) {
                        field.openTile(i, j);
                        rowIndex = i;
                        colIndex = j;
                    }
                    break;
                }
            }
            assertEquals(field.getTile(rowIndex, colIndex).getState(), Tile.State.OPEN);
            assertEquals(field.getState(), GameState.FAILED);
        }
    @Test
        public void checkOpenClue() {
        int number = field.getNumberOf(Tile.State.OPEN);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (field.getTiles()[i][j] instanceof Clue && ( (Clue) field.getTiles()[i][j]).getValue() > 0) {
                        field.openTile(i, j);
                        break;
                    }
                }
            }

//        assertEquals(number + 1, field.getNumberOf(Tile.State.OPEN));
        assertEquals(field.getState(), GameState.PLAYING);

        int numberOfRemMine = field.getRemainingMineCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (field.getTiles()[i][j] instanceof Clue) {
                    if (((Clue) field.getTiles()[i][j]).getValue() == 0) {
                        field.openTile(i, j);
                        break;
                    }
                }
            }
        }
        assertEquals(numberOfRemMine, field.getRemainingMineCount());
        assertEquals(field.getState(), GameState.PLAYING);

        int row = 0; int col = 0;
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (field.getTiles()[i][j].getState() == Tile.State.CLOSED) {
                    field.markTile(i, j);
                    field.openTile(i, j);
                    row = i;
                    col = j;
                }
            }
        }
        assertTrue(field.getTiles()[row][col].getState() == Tile.State.MARKED);
    }


        @Test
        public void checkMinesCount() {
            int minesCounter = 0;
            for (int row = 0; row < rowCount; row++) {
                for (int column = 0; column < columnCount; column++) {
                    if (field.getTile(row, column) instanceof Mine) {
                        minesCounter++;
                    }
                }
            }

            assertEquals(minesCount, minesCounter, "Field was initialized incorrectly - " +
                    "a different amount of mines was counted in the field than amount given in the constructor.");
        }

        @Test
        public void fieldWithTooManyMines() {
            Field fieldWithTooManyMines = null;
            int higherMineCount = rowCount * columnCount + randomGenerator.nextInt(10) + 1;
            try {
                fieldWithTooManyMines = new Field(rowCount, columnCount, higherMineCount);
            } catch (Exception e) {
                // field with more mines than tiles should not be created - it may fail on exception
            }
            assertTrue((fieldWithTooManyMines == null) || (fieldWithTooManyMines.getMineCount() <= (rowCount * columnCount)));
        }
    }