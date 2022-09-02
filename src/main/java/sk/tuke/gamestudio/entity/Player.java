package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints =
        {@UniqueConstraint(name = "UniqueUserName", columnNames = { "userName"})})
public class Player {
    @Id
    @GeneratedValue
    int id;

    @Column(nullable = false, length = 32)
    String userName;
    @Column(nullable = false, length = 128)
    String fullName;

    @Column (columnDefinition = "INT CHECK(self_evaluation BETWEEN 1 AND 10) NOT NULL")
    int selfEvaluation;

    @ManyToOne
    @JoinColumn(name = "Country.id")
    Country country;

    @ManyToOne
    @JoinColumn(name = "Occupation.id")
    Occupation occupation;

    public Player(String userName, String fullName, int selfEvaluation, Country country, Occupation occupation) {
        this.userName = userName;
        this.fullName = fullName;
        this.selfEvaluation = selfEvaluation;
        this.country = country;
        this.occupation = occupation;
    }

    public Player(String userName) {
        this.userName = userName;
    }

    public Player() {
    }


    @Override
    public String toString() {
        return id + " " + userName + " " + fullName + " country " + country.name + " occupation " + occupation.occupation +"\n";
    }

    public int getID() {
        return id;
    }
}


