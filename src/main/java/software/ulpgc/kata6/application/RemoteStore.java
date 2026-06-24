package software.ulpgc.kata6.application;

import software.ulpgc.kata6.architecture.io.Store;
import software.ulpgc.kata6.architecture.model.Movie;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class RemoteStore implements Store {

    private final Function<String, Movie> deserializer;
    private static final String RemoteURL = "https://datasets.imdbws.com/title.basics.tsv.gz";

    public RemoteStore(Function<String, Movie> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public Stream<Movie> movies() throws IOException {
        return loadFrom(new URL(RemoteURL));
    }

    private Stream<Movie> loadFrom(URL url) throws IOException {
        return loadFrom(url.openConnection());
    }

    private Stream<Movie> loadFrom(URLConnection connection) throws IOException {
        return loadFrom(unzip(connection.getInputStream()));
    }

    private Stream<Movie> loadFrom(InputStream is) throws IOException {
        return loadFrom(toReader(is));
    }

    private Stream<Movie> loadFrom(BufferedReader reader) throws IOException {
        return reader.lines().skip(1).map(deserializer);
    }

    private BufferedReader toReader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is));
    }

    private InputStream unzip(InputStream is) throws IOException {
        return new GZIPInputStream(new BufferedInputStream(is));
    }
}
