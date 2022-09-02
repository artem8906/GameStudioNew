package sk.tuke.gamestudio.server.webservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;



@RestController
@RequestMapping("/api/rating")
public class RatingWebServiceRest {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/{game}/{username}")
    public int getRating(@PathVariable String game, @PathVariable String name) {
        return ratingService.getRating(game, name);

    }

    @PostMapping()
    public void addRating(@RequestBody Rating rating) {

        ratingService.setRating(rating);

        }

    @GetMapping("/{game}")
    public int getAverageRating(@PathVariable String game) {
        return ratingService.getAverageRating(game);
    }
    }

