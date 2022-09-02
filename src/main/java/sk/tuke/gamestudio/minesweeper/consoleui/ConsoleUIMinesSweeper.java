package sk.tuke.gamestudio.minesweeper.consoleui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.minesweeper.UserInterface;
import sk.tuke.gamestudio.minesweeper.core.Field;
import sk.tuke.gamestudio.minesweeper.core.GameState;
import sk.tuke.gamestudio.service.*;

public class ConsoleUIMinesSweeper implements UserInterface {

    private Field field;
    private Settings settings;
    public String nameOfPlayer;
    private final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static final Pattern PATTERN = Pattern.compile("([MO])([A-Za-z])(\\d{1,2})");
    private static final int CHARINDEX = 65;

    private static final String NAMEGAME = "MinesSweeper";

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private OccupationService occupationService;

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            e.getMessage();
            return null;
        }
    }

    private void inputName() throws IOException {
        System.out.println("Enter you name");
        nameOfPlayer = input.readLine();
        if (nameOfPlayer.length() < 1 | nameOfPlayer.length() > 64) {
            throw new GameStudioException("Name is too short or too long. Try again");
        }
    }

    @Override
    public void newGameStarted(Field field) {
        this.field = field;

        handleInputName();
        System.out.println("Choose your PlayerProfile from below (press digit before UserName) or press 0 for create new Player");
        List<Player> listOfPlayers = playerService.getPlayersByUserName(nameOfPlayer);
        System.out.println(listOfPlayers);

        try {
            int choiseInt = Integer.parseInt(readLine());
            if (choiseInt == 0) {
                createNewPlayer();
            } else {
                for (Player player : listOfPlayers) {
                    if (player.getID() == choiseInt) {
                        //continue with this
                        break;
                    }
                }
            }


        } catch (Exception e) {
            System.out.println("Input data is not correct. Try again");
            newGameStarted(field);

        }


        System.out.println("Input game level \n B - BEGINNER  \t I - INTERMEDIATE \t E - EXPERT");
        try {
            Settings s = switch (input.readLine()) {
                case "I" -> Settings.INTERMEDIATE;
                case "E" -> Settings.EXPERT;
                default -> Settings.BEGINNER;
            };
            this.settings = s;
            this.field = new Field(s.getRowCount(), s.getColumnCount(), s.getMineCount());
        } catch (IOException e) {
            e.getMessage();
        }
        do {
            update();
            processInput();

            var fieldState = this.field.getState();
            try {
                if (fieldState == GameState.FAILED) {
                    scoreService.addScore(new Score(NAMEGAME, nameOfPlayer, 0, new Date()));
                    System.out.println("Loss. Your score is 0");
                    endOfGame();
                    System.exit(0);
                }

                if (fieldState == GameState.SOLVED) {
                    scoreService.addScore(new Score(NAMEGAME, nameOfPlayer, this.field.getScore(), new Date()));
                    System.out.println("Win. Your score is " + this.field.getScore());
                    endOfGame();
                    System.exit(0);
                }
            } catch (Exception e) {
                throw new GameStudioException(e);
            }
        }

        while (true);
    }

    private void createNewPlayer() {
        Random rd = new Random();
        System.out.println("Input fullName");
        String fullName = readLine();
        if (fullName.length() > 128 | fullName.length() == 0) {
            System.out.println("fullName is incorrect. Try again");
            fullName = readLine();
        }
        Occupation occupForWrite = null;
        Country countryForWrire = null;
        System.out.println("Choose your occupation. Press number at the start of line");
        List<Occupation> listOfOccup = occupationService.getOccupation();
        List<Country> listOfCount = countryService.getCountries();
        System.out.println(listOfOccup);
        try {
            int idOccup = Integer.parseInt(readLine());
            for (Occupation o : listOfOccup) {
                if (o.getID() == idOccup) {
                    occupForWrite = o;
                    break;
                }
            }
            if (occupForWrite == null) throw new GameStudioException();

            System.out.println("Choose your country. Press number at the start of line or press O for add new country");
            System.out.println(listOfCount);
            int idCount = Integer.parseInt(readLine());
            for (Country c : listOfCount) {
                if (c.getID() == idCount) {
                    countryForWrire = c;
                    break;
                }
            }
            if (idCount == 0) {
                countryForWrire = createNewCountry();
            }

            if (countryForWrire == null) throw new GameStudioException();

        } catch (Exception e) {
            System.out.println("Input data is incorrect.Try again");
            createNewPlayer();
        }
        playerService.addPlayer(new Player(nameOfPlayer, fullName, rd.nextInt(11), countryForWrire, occupForWrite));
    }

    private Country createNewCountry() {
        System.out.println("Input name of country");
        String nameOfCountry = readLine();
        if (nameOfCountry.length() > 128 | nameOfCountry.length() == 0) {
            System.out.println("Name of country is incorrect. Try again");
            createNewCountry();
        }

        if (countryService.getCountryByName(nameOfCountry)!=null) {
            System.out.println("This country already exists");
            return countryService.getCountryByName(nameOfCountry);
        }
        Country country = new Country(nameOfCountry);
        countryService.addCountry(country);
        return country;
    }

    private void handleInputName() {
        try {
            inputName();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            handleInputName();
        }
    }

    @Override
    public void play() {
        settings = Settings.load();
        Field field = new Field(settings.getRowCount(), settings.getColumnCount(), settings.getMineCount());
        try {
            newGameStarted(field);
        } catch (Exception e) {
            throw new GameStudioException(e);
        }
    }

    private void endOfGame() {
        handlerOfWriterComment();
        handlerOfGivingRate();
        printScoresAndComment();
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
        commentService.addComment(new Comment(NAMEGAME, nameOfPlayer, comment, new Date()));
    }

    private void rateGame() throws IOException {
        System.out.println("Rate this game (from 1 to 5)");
        int rate = Integer.valueOf(input.readLine());
        if (rate < 1 | rate > 5) throw new GameStudioException();
        ratingService.setRating(
                new Rating(NAMEGAME, nameOfPlayer, rate, new Date()));
    }

    private void printScoresAndComment() {
        System.out.println("Best scores are");
        System.out.println(scoreService.getBestScores(NAMEGAME));

        System.out.println("Last comments are:");
        System.out.println(commentService.getComments(NAMEGAME));

        System.out.println("Average rating of this game is " + ratingService.getAverageRating(NAMEGAME));
    }

    @Override
    public void update() {
        char a = CHARINDEX;
        System.out.print("  ");
        for (int i = 0; i < field.getColumnCount(); i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < field.getRowCount(); i++) {
            if (a == 91) a += 6;
            System.out.printf("%c ", a);
            a++;
            for (int j = 0; j < field.getColumnCount(); j++) {
                if (j > 9) System.out.print(" ");
                System.out.print(field.getTile(i, j) + " ");
            }
            System.out.println();
        }
    }

    private void processInput() {
        try {
            handleInput(readLine());
        } catch (WrongFormatException e) {
            e.getMessage();
            processInput();
        }
    }

    private void handleInput(String input) throws WrongFormatException {

        if (input.equals("X")) return;
        Matcher matcher = PATTERN.matcher(input);
        String action = "", rowStr = "", colStr = "";

        if (matcher.find()) {
            action = matcher.group(1);
            rowStr = matcher.group(2);
            colStr = matcher.group(3);
        } else processInput();

        int column = Integer.parseInt(colStr);
        int row = (int) rowStr.charAt(0) - CHARINDEX;

        if (column >= field.getColumnCount() | row < 0 | (row > 25 & row < 32) | row > 57 | row >= field.getRowCount()) {
            throw new WrongFormatException("Wrong parameters of tile");
        }

        switch (action) {
            case "M" -> field.markTile(row, column);
            case "O" -> field.openTile(row, column);
            default -> processInput();
        }
    }
}
