package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.minesweeper.core.Clue;
import sk.tuke.gamestudio.pexeco.Field;
import sk.tuke.gamestudio.pexeco.GameState;
import sk.tuke.gamestudio.pexeco.Tile;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/pexeco")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class PexecoController {

    private final String NAME_GAME = "pexeco";
    @Autowired
    UserController userController;
    @Autowired
    ScoreService scoreService;

    @Autowired
    CommentService commentService;

    @Autowired
    RatingService ratingService;

    private Field field;
    private boolean isPlaying = true;

    Tile openedTile;
    int rowOpened;
    int colOpened;


    private int score = 50;

    @RequestMapping
    public String processUserInput(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model) {
        startOrUpdateGame(row, column);
        prepareModel(model);
        return "pexeco";
    }

    @RequestMapping("/new")
    public String newGame(Model model) {
        field = new Field(4);
        score = 50;
        prepareModel(model);
        return "pexeco";
    }


    @RequestMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field processUserInputJson() {
        boolean justFinished = checkIfEndOfGame();
        this.field.setJustFinished(justFinished);
        return this.field;
    }

    @RequestMapping(value = "/jsonnew", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field newGameJson() {
        startNewGame();
        this.field.setJustFinished(false);
        return this.field;
    }

    private void startNewGame() {
        this.field = new Field(4);
        this.isPlaying = true;
        score = 50;
    }

    @RequestMapping("/comment")
    public String comment(String comment, Model model) {
        Comment newComment = new Comment(NAME_GAME, userController.getLoggedUser(), comment, new Date());
        commentService.addComment(newComment);
        prepareModel(model);
        return "pexeco";
    }

    @RequestMapping("/rating")
    public String rating(int rating, Model model) {
        Rating newRating;
        if (userController.isLogged()) {
            newRating = new Rating(NAME_GAME, userController.getLoggedUser(), rating, new Date());
            ratingService.setRating(newRating);
        } else {
            newRating = new Rating(NAME_GAME, "Anonym", rating, new Date());
            ratingService.setRating(newRating);
        }
        prepareModel(model);
        return "pexeco";
    }

    @RequestMapping("/asynch")
    public String loadInAsynchMode() {
        startOrUpdateGame(null, null);
        return "pexecoAsynch";
    }


    public Character getTileText(Tile tile) {
        if (tile.getState().equals(Tile.State.OPEN)) {
            return Character.valueOf((char) tile.getValue());
        } else if (tile.getState().equals(Tile.State.SOLVED)) {
            return 'X';
        } else return ' ';
    }

    public boolean checkIfEndOfGame() {
        boolean justFinished = false;
        if (field.isSolved() && this.isPlaying == true) { //I just won/lose
            this.isPlaying = false;
            justFinished = true;

            if (userController.isLogged()) {
                Score newScore = new Score(NAME_GAME, userController.getLoggedUser(), score, new Date());
                scoreService.addScore(newScore);
            }
        }
        return justFinished;

    }

    private void startOrUpdateGame(Integer row, Integer column) {

        Tile currentTile = null;


        boolean justFinished = false;
        if (field == null) {
            startNewGame();
        }

        if (openedTile == null && row != null && column != null) {//prva karticka
            field.open(row, column);
            openedTile = field.getTile(row, column);
            rowOpened = row;
            colOpened = column;
            return;
        }


        if (row != null && column != null && openedTile != null) {//otvaranie druhej karticky
            field.open(row, column);
            currentTile = field.getTile(row, column);
        }


        if (currentTile != null && openedTile != null) {//len pre druhu karticky
            if (currentTile.getValue() == openedTile.getValue() && ((row != rowOpened && column != colOpened))) //spravna volba alebo ne ta ista bunka
            {
                field.getTile(row, column).setState(Tile.State.SOLVED);
                field.getTile(rowOpened, colOpened).setState(Tile.State.SOLVED);
            } else { //nespavna volba
                field.getTile(row, column).setState(Tile.State.CLOSED);
                field.getTile(rowOpened, colOpened).setState(Tile.State.CLOSED);
                score--;//minus bod za nespavny
            }
        }


        if (openedTile != null) openedTile = null;
        if (currentTile != null) currentTile = null;


        if (this.field.getState() != GameState.PLAYING && this.isPlaying == true) { //I just won/lose
            this.isPlaying = false;

            justFinished = true;


            if (userController.isLogged() && this.field.getState() == GameState.SOLVED) {
                Score newScore = new Score(NAME_GAME, userController.getLoggedUser(), score, new Date());
                scoreService.addScore(newScore);
                this.isPlaying = false;
                justFinished = true;

            }
        }
    }

    private String getGameStatusMessage() {
        String gameStatus = "";

        if (this.field.getState() == GameState.SOLVED) {
            gameStatus = "You win with score " + score;
        }
        return gameStatus;
    }

    public void prepareModel(Model model) {
        model.addAttribute("pexecoField", field.getTiles());
        model.addAttribute("comments", commentService.getComments(NAME_GAME));
        model.addAttribute("win", field.isSolved());
        model.addAttribute("avgRating", ratingService.getAverageRating(NAME_GAME));
        model.addAttribute("bestScores", scoreService.getBestScores(NAME_GAME));
        model.addAttribute("gameStatus", getGameStatusMessage());
        model.addAttribute("isPlaying", this.isPlaying);
        model.addAttribute("countOfround", score);


    }

}
