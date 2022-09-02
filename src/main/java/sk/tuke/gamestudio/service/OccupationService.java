package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Country;
import sk.tuke.gamestudio.entity.Occupation;

import java.util.List;

public interface OccupationService {
    public List<Occupation> getOccupation();
    void addOccupation(Occupation occupation);

    public Occupation getOccupationByName(String occupation);
}
