package software.ulpgc.kata6.application;


import software.ulpgc.kata6.architecture.io.Store;
import software.ulpgc.kata6.architecture.model.Movie;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Stream;

public class DatabaseStore implements Store {

    private final Connection connection;

    public DatabaseStore(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Stream<Movie> movies() {
        try {
            return moviesIn(query());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet query() throws SQLException {
        return connection.createStatement().executeQuery("SELECT * FROM movies");
    }

    private Stream<Movie> moviesIn(ResultSet query) throws SQLException {
        return Stream.generate(()-> {
            try {
                return movieIn(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).takeWhile(Objects::nonNull);
    }

    private Movie movieIn(ResultSet query) throws SQLException {
        try {
            return query.next() ? readFrom(query) : null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static Movie readFrom(ResultSet rs) throws SQLException {
        return new Movie(
                rs.getString(1),
                rs.getInt(2),
                rs.getInt(3)
        );
    }
}
