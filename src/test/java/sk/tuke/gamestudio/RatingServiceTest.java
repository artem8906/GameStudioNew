package sk.tuke.gamestudio;

import sk.tuke.gamestudio.entity.Rating;;
import org.junit.jupiter.api.Test;

import sk.tuke.gamestudio.service.GameStudioException;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.RatingServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RatingServiceTest {

    private RatingService rs = new RatingServiceJDBC();

    @Test
    public void returnNullifEmptyRatingTest() {
        rs.reset();
        assertEquals(0, rs.getAverageRating("sk/tuke/gamestudio"));
    }

    @Test
    public void averageRatingTest() {
        rs.reset();
        var date = new Date();
        rs.setRating(new Rating("sk/tuke/gamestudio", "Peto", 5, date));
        rs.setRating(new Rating("sk/tuke/gamestudio", "Katka", 1, date));
        rs.setRating(new Rating("tiles", "Zuzka", 4, date));
        rs.setRating(new Rating("sk/tuke/gamestudio", "Jergus", 3, date));

        assertEquals((3+5+1)/3,rs.getAverageRating("sk/tuke/gamestudio"));

    }
    @Test
    public void updateRateFromSameUserAndGame() {
        rs.reset();
        var date = new Date();
        rs.setRating(new Rating("sk/tuke/gamestudio", "Katka", 5, date));
        rs.setRating(new Rating("sk/tuke/gamestudio", "Katka", 1, date));
        assertEquals(1, rs.getRating("sk/tuke/gamestudio", "Katka"));
    }
    @Test
    public void rateIllegalValue() {
        rs.reset();
        try {
            rs.setRating(new Rating("sk/tuke/gamestudio", "Katka", 7, new Date()));
            assertTrue(false);
        }
        catch (GameStudioException e) {
            assertTrue(true);
        }
    }
}
