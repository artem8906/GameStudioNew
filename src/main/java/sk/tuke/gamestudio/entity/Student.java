package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
public class Student {
@Id
@GeneratedValue
    private long id;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String firstName;

    @ManyToOne
    @JoinColumn(name = "StudyGroup.id", nullable = false)
    private StudyGroup studyGroup;

    public Student(String lastName, String firstName, StudyGroup sg) {
        this.lastName = lastName;
        this.firstName = firstName;
        studyGroup = sg;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", studyGroup=" + studyGroup +
                '}';
    }
}
