package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Country {
    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false, length = 128, unique = true)
    String name;

    @OneToMany(mappedBy = "id")
    private List<Player> players;

    public Country(String name) {
        this.name = name;
    }

    public Country() {
    }

    @Override
    public String toString() {
        return id + " " + name + "\n";
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }
}
