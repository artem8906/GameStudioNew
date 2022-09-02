package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

@Transactional
public class RatingServiceJPA implements RatingService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {

        String game = rating.getGame();
        String name = rating.getUsername();

        Rating ratingOld = null;
        try {
            ratingOld = (Rating) entityManager.createQuery("select r from Rating r where r.username=:user and r.game =:game").setParameter("user", name).setParameter("game", game)
                    .getSingleResult();
            ratingOld.setRatedOn(new Date());
            ratingOld.setRate(rating.getRate());
        } catch (NoResultException e) {
            entityManager.persist(rating);
        }
    }


    @Override
    public int getAverageRating(String game) {
        Number number = (Number) entityManager.createQuery("select avg (rate) from Rating rt where rt.game = :myGame")
                .setParameter("myGame", game).getSingleResult();
        if (number==null) {
            return 0;
        }
        return number.intValue();
    }

    @Override
    public int getRating(String game, String username) {
        Rating rating = (Rating) entityManager.createQuery("select rt from Rating rt where rt.game = :myGame and rt.username = :MyUser")
                .setParameter("myGame", game).setParameter("myUser", username).getSingleResult();
                return rating.getRate();
    }

    @Override
    public void reset() {
        entityManager.createNativeQuery("DELETE from rating").executeUpdate();
    }
}
