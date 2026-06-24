package software.ulpgc.kata6.architecture.control;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import software.ulpgc.kata6.architecture.io.Store;
import software.ulpgc.kata6.architecture.model.Movie;
import software.ulpgc.kata6.architecture.viewmodel.Histogram;
import software.ulpgc.kata6.architecture.viewmodel.HistogramBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class HistogramHandler implements Handler {

    private final Store store;

    public HistogramHandler(Store store) {
        this.store = store;
    }

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String fromParam = ctx.queryParam("from");
        String toParam = ctx.queryParam("to");
        Stream<Movie> movies = store.movies();

        if (fromParam != null && toParam != null) {
            int from = Integer.parseInt(fromParam);
            int to = Integer.parseInt(toParam);
            movies = movies.filter(m -> m.year() >= from && m.year() <= to);
        }
        Histogram histogram = HistogramBuilder.with(movies).use(Movie::year);
        Map<String, Integer> jsonResult = new HashMap<>();
        for (Integer bin : histogram) {
            jsonResult.put(String.valueOf(bin), histogram.count(bin));
        }
        ctx.json(jsonResult);
    }
}
