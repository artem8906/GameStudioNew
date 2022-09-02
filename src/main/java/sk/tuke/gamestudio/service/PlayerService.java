package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Occupation;
import sk.tuke.gamestudio.entity.Player;

import java.util.List;

public interface PlayerService {

    public List<Player> getPlayersByUserName(String uName);

    public void addPlayer(Player player);
}
