//create table rating (
//        game VARCHAR(64) NOT NULL,
//        username VARCHAR(64) NOT NULL,
//        rate INTEGER check (rate in (1,2,3,4,5)) NOT NULL,
//        rated_on TIMESTAMP NOT NULL,
//        UNIQUE (game, username));

package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RatingServiceJDBC implements RatingService{
    private static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";

    private static final String STATEMENT_ADD_RATING = "INSERT INTO rating VALUES (?, ?, ?, ?)";
    private static final String STATEMENT_GET_RATING = "SELECT rate, game, username, rated_on FROM rating WHERE game= ? and username= ?";
    private static final String STATEMENT_RESET = "DELETE FROM rating";
    public static final String STATEMENT_AVERAGE = "SELECT AVG (rate) FROM rating WHERE game=?";
    public static final String STATEMENT_UPDATE_RATING = "UPDATE rating SET rate = ?, rated_on = ? WHERE game= ? and username= ?";


    @Override
    public void setRating(Rating rating) {

        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(STATEMENT_GET_RATING);
        ) {
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getUsername());
            try (var rs = statement.executeQuery()) {
                if (rs.next() && rating.getGame().equals(rs.getString(2)) && rating.getUsername().equals(rs.getString(3))) {

                    try (
                            var statement1 = connection.prepareStatement(STATEMENT_UPDATE_RATING);
                    ) {
                        statement1.setInt(1, rating.getRate());
                        statement1.setTimestamp(2, new Timestamp(rating.getRatedOn().getTime()));
                        statement1.setString(3, rating.getGame());
                        statement1.setString(4, rating.getUsername());
                        statement1.executeUpdate();
                    } catch (SQLException e) {
                        throw new GameStudioException(e);
                    }
                } else {
                    try (var statement2 = connection.prepareStatement(STATEMENT_ADD_RATING)
                    ) {
                        statement2.setString(1, rating.getGame());
                        statement2.setString(2, rating.getUsername());
                        statement2.setInt(3, rating.getRate());
                        statement2.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
                        statement2.executeUpdate();
                    } catch (SQLException e) {
                        throw new GameStudioException(e);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new GameStudioException(e);
        }

        }

    @Override
    public int getAverageRating(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(STATEMENT_AVERAGE)
        ) {
            statement.setString(1, game);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                else return 0;
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public int getRating(String game, String username) {

        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(STATEMENT_GET_RATING)
        ) {
            statement.setString(1, game);
            statement.setString(2, username);

            try (var rs = statement.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                else return 0;
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void reset() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.createStatement()
        ) {
            statement.executeUpdate(STATEMENT_RESET);
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }
    }

