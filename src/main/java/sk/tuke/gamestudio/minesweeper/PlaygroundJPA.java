package sk.tuke.gamestudio.minesweeper;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.service.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Transactional
public class PlaygroundJPA {

    @PersistenceContext
    private EntityManager entityManager;
//    @Autowired
//    StudentService ss;
//
//    @Autowired
//    StudentGroupServiceJPA sgs;

    @Autowired
    CountryService countryService;

    @Autowired
    OccupationService occupationService;

    @Autowired
    PlayerService playerService;


    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    public void fillTable(){

//
//        List<StudyGroup> studyGroups= entityManager.createQuery("select g from StudyGroup g")
//                .getResultList();
//
//            System.out.println("Input your name");
//            String name = input.readLine();
//            System.out.println("Input your surname");
//            String surname = input.readLine();
//            System.out.println("Input number of group");
//            int numberOfGroup = Integer.parseInt(input.readLine());
//            if (name.length()>64 || surname.length()>64 || name.equals("") || surname.equals(""))
//                throw new Exception("Incorrect input data");
//
//            ss.addStudent(new Student(name, surname, studyGroups.get(numberOfGroup)));
//
    countryService.addCountry(new Country("USA"));
    countryService.addCountry(new Country("UK"));
    countryService.addCountry(new Country("Slovakia"));
    Country country1 = new Country("Austria");
    Country country2 = new Country("Germany");
    countryService.addCountry(country1);
    countryService.addCountry(country2);

    occupationService.addOccupation(new Occupation("ziak"));
    Occupation occupation1 = new Occupation("student");
    occupationService.addOccupation(occupation1);
    occupationService.addOccupation(new Occupation("zamestnanec"));
    occupationService.addOccupation(new Occupation("zivnostnik"));
    occupationService.addOccupation(new Occupation("nezamestnany"));
    occupationService.addOccupation(new Occupation("dochodca"));
    occupationService.addOccupation(new Occupation("invalid"));

    playerService.addPlayer(new Player("Artem", "Peshkov", 2, country1, occupation1));

    }

    public void play() {
        System.out.println("Opening JPA playground");
//
//        try {
//            createStudyGroup();
//            inputData();
//        }
//        catch (Exception e) {e.getMessage();}
//
//        ss.output();
//
//
//        System.out.println("Closed JPA playground");
//    }
//
//    private void createStudyGroup() throws IOException {
//        System.out.println("Input name of group");
//        String nameOfGroup = input.readLine();
//        sgs.addGroup(new StudyGroup(nameOfGroup));
//
//    }
        fillTable();
        System.out.println("Closed JPA playground");


    }
}
