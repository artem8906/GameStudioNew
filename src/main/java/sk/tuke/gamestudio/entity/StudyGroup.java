package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class StudyGroup {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "id")
    private List<Student> students;

    public StudyGroup(String name) {
        this.name = name;
    }
    public StudyGroup() {
    }

    @Override
    public String toString() {
        return name;
    }
}
