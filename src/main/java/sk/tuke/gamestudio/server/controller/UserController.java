package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Country;
import sk.tuke.gamestudio.entity.Occupation;
import sk.tuke.gamestudio.entity.Player;
import sk.tuke.gamestudio.service.CountryService;
import sk.tuke.gamestudio.service.CountryServiceJPA;
import sk.tuke.gamestudio.service.OccupationService;
import sk.tuke.gamestudio.service.PlayerService;

import java.util.ArrayList;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private String loggedUser;
    private List<Player> listOfPlayer;
    private boolean isExist = true;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private OccupationService occupationService;

    @RequestMapping("/login")
    public String login(String login, String password, Model model){
        if(("password").equals(password)){
            this.loggedUser=login.trim();
            if(this.loggedUser.length()>0) {
                if (getListOfPlayer().isEmpty()) {
                    isExist=false;
                    prepareModel(model);
                    return "redirect:/registration";
                }

                else {
                    isExist=true;
                    return "redirect:/gamestudio";
                }
            }
        }
//        this.loggedUser=null;
        return "redirect:/gamestudio";
    }

    @RequestMapping("/logout")
    public String login(){
        this.loggedUser=null;
        return "redirect:/gamestudio";
    }

    public List<Player> getListOfPlayer() {
        List<Player> playerList = new ArrayList<>();
        if (loggedUser!=null)
        playerList = playerService.getPlayersByUserName(loggedUser);
        return playerList;
    }

    public boolean isExist() {
        return isExist;
    }

    public String getLoggedUser() {return loggedUser;}

    public boolean isLogged() {return loggedUser!=null;}

    @RequestMapping("/registration/addNewPlayer")
    public String addnewPlayer(String fullName, int selfEvaluation, String occupationStr, String countryStr) {
        Player newPlayer = new Player(loggedUser, fullName, selfEvaluation, countryService.getCountryByName(countryStr), occupationService.getOccupationByName(occupationStr));
        playerService.addPlayer(newPlayer);
        isExist = true;
        return "redirect:/gamestudio";
    }

    @RequestMapping("/registration")
    public String mainPage(Model model) {
        prepareModel(model);
        return "registration";
    }

    public void prepareModel(Model model) {
        model.addAttribute("listOccupation", occupationService.getOccupation());
        model.addAttribute("listCountry", countryService.getCountries());

    }


}
