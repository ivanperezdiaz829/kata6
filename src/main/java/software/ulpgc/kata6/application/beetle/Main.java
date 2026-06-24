package software.ulpgc.kata6.application.beetle;

import software.ulpgc.kata6.application.*;
import software.ulpgc.kata6.architecture.model.Movie;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:movies.db")) {
            connection.setAutoCommit(false);
            importIfNeededInto(connection);
            Desktop.create(new DatabaseStore(connection)).display().setVisible(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void importIfNeededInto(Connection connection) throws SQLException, IOException {
        if (new File("movies.db").length() > 0) return;
        Stream<Movie> movies = new RemoteStore(TsvMovieParser::from).movies()
                .filter(movie -> movie.year() > 1900)
                .filter(movie -> movie.year() < 2020);
        new DatabaseRecorder(connection).record(movies);
    }
}
