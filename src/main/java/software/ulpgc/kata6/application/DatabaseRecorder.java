package software.ulpgc.kata6.application;

import software.ulpgc.kata6.architecture.io.Recorder;
import software.ulpgc.kata6.architecture.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

public class DatabaseRecorder implements Recorder {

    private final Connection connection;
    private final PreparedStatement statement;
    private Integer count = 0;

    public DatabaseRecorder(Connection connection) throws SQLException {
        this.connection = connection;
        this.createTableIfNotExists();
        this.statement = connection.prepareStatement("INSERT INTO movies (title, year, duration) VALUES (?, ?, ?)");
    }

    private void createTableIfNotExists() throws SQLException {
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS movies (title STRING, year INTEGER, duration INTEGER)");
    }

    @Override
    public void record(Stream<Movie> movies) {
        try {
            movies.forEach(this::record);
            flush();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void flush() throws SQLException {
        statement.executeBatch();
    }

    private void record(Movie movie) {
        try {
            write(movie);
            flushIfIsNeeded();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(Movie movie) throws SQLException {
        statement.setString(1, movie.name());
        statement.setInt(2, movie.year());
        statement.setInt(3, movie.duration());
        statement.addBatch();
    }

    private void flushIfIsNeeded() throws SQLException {
        if (mustFlush()) flush();
    }

    private boolean mustFlush() throws SQLException {
        return ++count % 10_000 == 0;
    }
}
