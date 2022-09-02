package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Occupation;
import sk.tuke.gamestudio.entity.Player;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class PlayerServiceJPA implements PlayerService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Player> getPlayersByUserName(String Name) {
        return entityManager.createQuery("select p from Player p where p.userName = :uName")
                .setParameter("uName", Name)
                .getResultList();

    }

    @Override
    public void addPlayer(Player player) {
        entityManager.persist(player);
    }
}
