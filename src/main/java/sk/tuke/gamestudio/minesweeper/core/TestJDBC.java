package sk.tuke.gamestudio.minesweeper.core;

import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;

public class TestJDBC {
    public static void main(String[] args) {
        ScoreService ss = new ScoreServiceJDBC();
        ss.reset();
    }

//    public static void main(String[] args) throws Exception {
//
//        try (var connection = DriverManager.getConnection("jdbc:postgresql://localhost/gamestudio", "postgres", "postgres");
//             var statement = connection.createStatement();
//             var rs = statement.executeQuery("SELECT game, username, points, played_on FROM score WHERE game='minesweeper' ORDER BY points DESC LIMIT 5")
//        ) {
//            while (rs.next()) {
//                System.out.printf("%s, %s, %d, %s \n", rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4));
//            }
//            System.out.println("Pripojenie uspesne.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//
//    }
}


