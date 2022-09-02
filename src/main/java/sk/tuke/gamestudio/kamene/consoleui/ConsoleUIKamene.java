package sk.tuke.gamestudio.kamene.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.kamene.core.Field;
import sk.tuke.gamestudio.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.Date;


public class ConsoleUIKamene {

    private Field field;
    private String name;
    private final String game = "Kamene";
    int score;
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    @Autowired
    ScoreService scoreService;

    @Autowired
    CommentService commentService;

    @Autowired
    RatingService ratingService;

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }
    private void inputName()  {
        System.out.println("Enter you name");
        name = readLine();
        if (name.length() < 1 | name.length() > 64) {
            System.out.println("Name is too short or too long. Try again");
            inputName();
        }
    }
    public void play() {
        this.field = new Field(4);
        inputName();
        field.generate();

        do {
            update();
            processInput();
            if (field.isSolved()) {
                int timeOfPlay = (int) (System.currentTimeMillis() - field.getStartTime())/1000000;
                field.setScore(field.getSize() * 4000 - timeOfPlay);
                System.out.println("You win. Your score is "+ field.getScore());

                endOfGame();

                System.exit(1);
            }
        } while (true);
    }

    private void endOfGame() {
        try {
            scoreService.addScore(new Score(game, name, field.getScore(), new Date()));
            handlerOfWriterComment();
            handlerOfGivingRate();
            printScoresAndComment();
        }
        catch (Exception e) {
            throw new GameStudioException(e);
        }
    }

    private void handlerOfGivingRate() {
        try {
            rateGame();
        } catch (Exception e) {
            System.out.println("From 1 to 5 only. Try again");
            handlerOfGivingRate();
        }
    }

    private void handlerOfWriterComment() {
        try {
            writeComment();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            handlerOfWriterComment();
        }
    }

    private void writeComment() throws IOException {
        System.out.println("Write you comment");
        String comment = input.readLine();
        if (comment.length() > 1000 | comment.length() == 0) {
            throw new GameStudioException("Comment is too long or too short. Try again");
        }
        commentService.addComment(new Comment(game, name, comment, new Date()));
    }

    private void rateGame() throws IOException {
        System.out.println("Rate this game (from 1 to 5)");
        int rate = Integer.valueOf(input.readLine());
        if (rate < 1 | rate > 5) throw new GameStudioException();
        ratingService.setRating(
                new Rating(game, name, rate, new Date()));
    }

    private void printScoresAndComment() {
        System.out.println("Best scores are");
        System.out.println(scoreService.getBestScores(game));

        System.out.println("Last comments are:");
        System.out.println(commentService.getComments(game));

        System.out.println("Average rating of this game is " + ratingService.getAverageRating(game));
    }


    public void update() {
        field.updateState();
        for (int i = 0; i < field.getTiles().length; i++) {
            for (int j = 0; j < field.getTiles().length; j++) {
                System.out.print(field.getTiles()[i][j]);
            }
            System.out.println();
        }
    }

    private void processInput() {
        System.out.println("Press 'w', 's', 'a', 'd' for moving tiles, 'new' for new game or 'exit' for exit");
        String line = readLine();

        try {
            Input(line);
        } catch (WrongUserInputException e) {
            System.out.println("Try again!");
            processInput();
        }
    }

    private void Input(String input) throws WrongUserInputException {
        String action = input;
        switch (action) {
            case "w":
                field.move("UP");
                break;

            case "s":
                field.move("DOWN");
                break;

            case "a":
                field.move("LEFT");
                break;

            case "d":
                field.move("RIGHT");
                break;

            case "new":
                play();
                break;
            case "exit":
                System.exit(1);
                break;

            default:
                throw new WrongUserInputException();
        }
    }
}


