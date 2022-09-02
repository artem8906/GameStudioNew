package sk.tuke.gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.kamene.consoleui.ConsoleUIKamene;
import sk.tuke.gamestudio.minesweeper.PlaygroundJPA;
import sk.tuke.gamestudio.minesweeper.consoleui.ConsoleUIMinesSweeper;
import sk.tuke.gamestudio.service.*;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "sk.tuke.gamestudio.server.*"))
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runnerMinesSweeper (ConsoleUIMinesSweeper console) {
        return s->console.play();
    }

//    @Bean
//    public CommandLineRunner runnerKamene (ConsoleUIKamene console) {
//        return s->console.play();
//    }

//    @Bean
//    public CommandLineRunner runnerJPA (PlaygroundJPA console) {
//        return s->console.play();
//    }

    @Bean
    public ConsoleUIMinesSweeper consoleMinesSweeper() {
        return new ConsoleUIMinesSweeper();
    }

    @Bean
    public ConsoleUIKamene consoleKamene() {
        return new ConsoleUIKamene();
    }

    @Bean
    public PlaygroundJPA consoleJPA() {
        return new PlaygroundJPA();
    }

    @Bean
    public ScoreService scoreService(){
        return new ScoreServiceRest();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRest();

    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }


    @Bean
    public StudentService studentService() {
        return new StudentServiceJPA();
    }

    @Bean
    public StudentGroupServiceJPA studentGroupServiceJPA() {
        return new StudentGroupServiceJPA();
    }

    @Bean
    public PlayerService playerService() {
        return new PlayerServiceJPA();
    }

    @Bean
    public CountryService countryService() {
        return new CountryServiceJPA();
    }

    @Bean
    public OccupationService occupationService() {
        return new OccupationServiceJPA();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
