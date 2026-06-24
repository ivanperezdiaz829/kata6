package software.ulpgc.kata6.application.bumblebee;

import io.javalin.Javalin;
import software.ulpgc.kata6.application.*;
import software.ulpgc.kata6.architecture.control.HistogramHandler;
import software.ulpgc.kata6.architecture.io.Store;
import software.ulpgc.kata6.architecture.model.Movie;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:movies.db");
        importIfNeededInto(connection);
        Store store = new DatabaseStore(connection);
        Javalin app = Javalin.create().start(9000);
        app.get("/histogram", new HistogramHandler(store));
        System.out.println("Server started!");
    }

    private static void importIfNeededInto(Connection connection) throws SQLException, IOException {
        if (new File("movies.db").length() > 0) return;
        Stream<Movie> movies = new RemoteStore(TsvMovieParser::from).movies();
        new DatabaseRecorder(connection).record(movies);
    }
}
