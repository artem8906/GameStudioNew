package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Country;

import java.util.List;

public interface CountryService {
    public List<Country> getCountries();
    void addCountry(Country country);

    Country getCountryByName(String name);

}
