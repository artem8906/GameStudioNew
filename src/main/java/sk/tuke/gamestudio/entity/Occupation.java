package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Occupation {
    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false, length = 32, unique = true)
    String occupation;

    @OneToMany(mappedBy = "id")
    private List<Player> players;

    public Occupation(String occupation) {
        this.occupation = occupation;
    }

    public Occupation() {
    }

    @Override
    public String toString() {
        return id + " " + occupation + "\n";
    }

    public int getID() {
        return id;
    }

    public String getOccupation() {
        return occupation;
    }
}
