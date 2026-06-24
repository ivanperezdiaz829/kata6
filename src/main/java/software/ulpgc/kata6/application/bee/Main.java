package software.ulpgc.kata6.application.bee;

import software.ulpgc.kata6.application.Desktop;
import software.ulpgc.kata6.application.RemoteStore;
import software.ulpgc.kata6.application.TsvMovieParser;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        Desktop.create(new RemoteStore(TsvMovieParser::from)).display().setVisible(true);
    }
}