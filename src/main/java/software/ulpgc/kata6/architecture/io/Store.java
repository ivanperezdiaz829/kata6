package software.ulpgc.kata6.architecture.io;

import software.ulpgc.kata6.architecture.model.Movie;

import java.io.IOException;
import java.util.stream.Stream;

public interface Store {
    Stream<Movie> movies() throws IOException;
}
