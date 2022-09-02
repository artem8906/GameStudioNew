package sk.tuke.gamestudio.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long id;
    @Column (nullable = false)
    private String game;
    @Column (nullable = false)
    private String username;
    @Column (nullable = false)
    private String comment;
    @Column (nullable = false)
    private Date commented_on;

    public Comment(String game, String username, String comment, Date commented_on) {
        this.game = game;
        this.username = username;
        this.comment = comment;
        this.commented_on = commented_on;
    }

    public Comment() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommented_on() {
        return commented_on;
    }

    public void setCommented_on(Date commented_on) {
        this.commented_on = commented_on;
    }

    @Override
    public String toString() {
        return "\n{" +
                "game='" + game + '\'' +
                ", username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                ", commented_on=" + commented_on;
    }
}
