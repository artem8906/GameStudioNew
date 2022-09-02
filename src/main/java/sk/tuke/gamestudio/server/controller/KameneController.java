package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.kamene.core.Field;
import sk.tuke.gamestudio.kamene.core.GameState;
import sk.tuke.gamestudio.kamene.core.Tile;
import sk.tuke.gamestudio.service.*;

import java.util.Date;

@Controller
@RequestMapping("/kamene")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class KameneController {

    private final String NAME_GAME = "kamene";
    @Autowired
    UserController userController;
    @Autowired
    ScoreService scoreService;

    @Autowired
    CommentService commentService;

    @Autowired
    RatingService ratingService;

    private Field field;


    @RequestMapping("/new")
    public String newGame(Model model) {
        field = new Field(4);
        prepareModel(model);
        return "kamene";
    }

    @RequestMapping
    public String processUserInput(Model model) {
        startOrUpdateGame();
        prepareModel(model);
        return "kamene";
    }

    @RequestMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field processUserInputJson() {
        checkIfEndOfGame();
        return this.field;
    }

    @RequestMapping(value = "/jsonnew", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field newGameJson() {
        startNewGame();
        return this.field;
    }

    private void startNewGame() {
        this.field = new Field(4);
    }

    @RequestMapping("/comment")
    public String comment(String comment, Model model) {
        if (userController.isLogged()) {
            Comment newComment = new Comment(NAME_GAME, userController.getLoggedUser(), comment, new Date());
            commentService.addComment(newComment);
        } else {
            Comment newComment = new Comment(NAME_GAME, "Anonym", comment, new Date());
            commentService.addComment(newComment);
        }
        prepareModel(model);
        return "kamene";
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
        return "kamene";
    }

    @RequestMapping("/asynch")
    public String loadInAsynchMode() {
        startOrUpdateGame();
        return "kameneAsynch";
    }


    public String getTileText(Tile tile) {
        if (tile.getValue() == 0) return "";
        else
            return String.valueOf(tile.getValue());
    }

    public void checkIfEndOfGame() {
        if (field.isSolved() && field.isAddscore() == false) { //I just won/lose

            if (userController.isLogged()) {
                Score newScore = new Score(NAME_GAME, userController.getLoggedUser(), this.field.getScore(), new Date());
                scoreService.addScore(newScore);
                field.setAddscore(true);
                field.setGameState(GameState.SOLVED);

            } else {
                field.setAddscore(true);
                field.setGameState(GameState.SOLVED);
            }
        }
    }

    private void startOrUpdateGame() {
        if (field == null) {
            startNewGame();
        }

        if (userController.isLogged() && field.getGameState() == GameState.SOLVED && field.isAddscore() == false) {
            Score newScore = new Score(NAME_GAME, userController.getLoggedUser(), this.field.getScore(), new Date());
            scoreService.addScore(newScore);
            field.setAddscore(true);
        }
    }


    @RequestMapping(value = "/Jup", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field moveUp() {
        if (!field.isSolved()) {
            field.move("UP");
            field.updateState();
            checkIfEndOfGame();
        }
        return this.field;
    }

    @RequestMapping(value = "/Jdown", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field moveDown() {
        if (!field.isSolved()) {
            field.move("DOWN");
            field.updateState();
            checkIfEndOfGame();
        }
        return this.field;
    }

    @RequestMapping(value = "/Jrigth", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field moveRigth() {
        if (!field.isSolved()) {
            field.move("RIGHT");
            field.updateState();
            checkIfEndOfGame();
        }
        return this.field;
    }

    @RequestMapping(value = "/Jleft", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Field moveLeft() {
        if (!field.isSolved()) {
            field.move("LEFT");
            field.updateState();
            checkIfEndOfGame();

        }
        return this.field;
    }

    @RequestMapping("/up")
    public String moveUp(Model model) {
        if (!field.isSolved()) {
            field.move("UP");
            field.updateState();
            checkIfEndOfGame();
            prepareModel(model);
        }
        return "kamene";
    }

    @RequestMapping("/down")
    public String moveDown(Model model) {
        if (!field.isSolved()) {
            field.move("DOWN");
            field.updateState();
            checkIfEndOfGame();
            prepareModel(model);
        }
        return "kamene";
    }

    @RequestMapping("/right")
    public String moveRight(Model model) {
        if (!field.isSolved()) {
            field.move("RIGHT");
            field.updateState();
            checkIfEndOfGame();
            prepareModel(model);
        }
        return "kamene";
    }

    @RequestMapping("/left")
    public String moveLeft(Model model) {
        if (!field.isSolved()) {
            field.move("LEFT");
            field.updateState();
            checkIfEndOfGame();
            prepareModel(model);
        }
        return "kamene";
    }


    public void prepareModel(Model model) {
        model.addAttribute("kameneField", field.getTiles());
        model.addAttribute("comments", commentService.getComments(NAME_GAME));
        model.addAttribute("win", field.getGameState() == GameState.SOLVED);//field.getIsSolved()
        model.addAttribute("avgRating", ratingService.getAverageRating(NAME_GAME));
        model.addAttribute("bestScores", scoreService.getBestScores(NAME_GAME));
        model.addAttribute("currentScore", field.getScore());


    }

}
