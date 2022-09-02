package sk.tuke.gamestudio;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.GameStudioException;
import sk.tuke.gamestudio.service.RatingService;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringClient.class)

public class RatingServiceTestSpring {
    @Autowired
    private RatingService rs;

    @Test
    public void returnNullifEmptyRatingTest() {
        rs.reset();
        assertEquals(0, rs.getAverageRating("MinesSweeper"));
    }

    @Test
    public void averageRatingTest() {
        rs.reset();
        var date = new Date();
        rs.setRating(new Rating("MinesSweeper", "Peto", 5, date));
        rs.setRating(new Rating("MinesSweeper", "Katka", 1, date));
        rs.setRating(new Rating("MinesSweeper", "Zuzka", 4, date));
        rs.setRating(new Rating("MinesSweeper", "Jergus", 3, date));

        assertEquals((3+5+1)/3,rs.getAverageRating("MinesSweeper"));

    }
    @Test
    public void updateRateFromSameUserAndGame() {
        rs.reset();
        var date = new Date();
        rs.setRating(new Rating("MinesSweeper", "Katka", 5, date));
        rs.setRating(new Rating("MinesSweeper", "Katka", 1, date));
        assertEquals(1, rs.getRating("MinesSweeper", "Katka"));
    }
    @Test
    public void rateIllegalValue() {
        rs.reset();
        try {
            rs.setRating(new Rating("MinesSweeper", "Katka", 7, new Date()));
            assertTrue(false);
        }
        catch (GameStudioException e) {
            assertTrue(true);
        }
    }
}

