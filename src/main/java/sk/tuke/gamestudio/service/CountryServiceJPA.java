package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Country;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CountryServiceJPA implements CountryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Country> getCountries() {
        return entityManager.createQuery("select c from Country c").getResultList();
    }

    @Override
    public void addCountry(Country country) {
        entityManager.persist(country);
    }

    @Override
    public Country getCountryByName(String name) {
        return (Country) entityManager.createQuery("select c from Country c where c.name = :Name")
                .setParameter("Name", name)
                .getSingleResult();

    }
}
