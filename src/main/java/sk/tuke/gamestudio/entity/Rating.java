package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueGameAndUsername", columnNames = { "game", "username" })})
public class Rating {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length=64)
    private String game;

    @Column(nullable = false, length=64)
    private String username;

    @Column (columnDefinition = "INT CHECK(rate BETWEEN 1 AND 5) NOT NULL")
    private int rate;
    @Column(nullable = false)
    private Date ratedOn;

    public Rating(int rate) {
        this.rate = rate;
    }

    public Rating(String game, String username, int rate, Date ratedOn) {
        this.game = game;
        this.username = username;
        this.rate = rate;
        this.ratedOn = ratedOn;
    }

    public Rating() {
    }

    public void Rating(int rating) {
        this.rate = rating;
    }


    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString() {
        return Integer.toString(rate);
    }
}
