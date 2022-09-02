package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class StudentServiceJPA implements StudentService {
    @PersistenceContext
    private EntityManager entityManager;

    public void addStudent(Student student) {
        entityManager.persist(student);
    }

    @Override
    public void reset() {
            entityManager.createNativeQuery("DELETE from Student").executeUpdate();
    }

    @Override
    public void deleteStudent() {
//            entityManager.createNativeQuery("select st from Student where st.name = :");
    }

    @Override
    public void output() {
        List<Student> students= entityManager.createQuery("select s from Student s").getResultList();
        System.out.println(students);
    }


}
