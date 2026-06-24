package software.ulpgc.kata6.application;

import software.ulpgc.kata6.architecture.model.Movie;
import software.ulpgc.kata6.architecture.serializers.MovieParser;

public class TsvMovieParser implements MovieParser {
    public static Movie from(String str) {
        return from(str.split("\t"));
    }

    private static Movie from(String[] str) {
        return new Movie(str[2], toInt(str[5]), toInt(str[7]));
    }

    private static int toInt(String str) {
        if (isVoid(str)) return -1;
        return Integer.parseInt(str);
    }

    private static boolean isVoid(String str) {
        return str.equals("\\N");
    }
}
