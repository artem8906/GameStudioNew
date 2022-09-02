package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.StudyGroup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class StudentGroupServiceJPA {
    @PersistenceContext
    private EntityManager entityManager;
    public void addGroup (StudyGroup group) {
        entityManager.persist(group);
    }
}
