package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Date;


public class RatingServiceRest implements RatingService{

    @Value("${remote.server.api}")
    private String url;

    @Autowired
    private RestTemplate template;

    @Override
    public void setRating(Rating rating) {
        template.postForEntity(url + "/rating",rating, Rating.class);
        }



    @Override
    public int getAverageRating(String game) {
        return template.getForObject(url + "/rating/avg/" + game, Rating.class).getRate();
    }

    @Override
    public int getRating(String game, String username) {
        Rating rating = template.getForObject(url + "/rating/" + game + "/" + username, Rating.class);
        return rating.getRate();
    }


    public Rating getRatingObj(String game, String username) {
        var arr = template.getForEntity(url + "/rates/" + game, Rating[].class).getBody();
        Rating forGet = null;
        for (Rating rating : arr) {
            if (rating.getUsername().equals(username) && rating.getGame().equals(game)) {
                forGet = rating;
                break;
            }
        }
        return forGet;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported");
    }

}
