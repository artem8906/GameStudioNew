package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Student;

public interface StudentService {

    public void addStudent(Student student);

    public void reset();

    public void deleteStudent();

    void output();
}
