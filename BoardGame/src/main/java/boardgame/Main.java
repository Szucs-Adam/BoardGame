package boardgame;

import javafx.application.Application;

/**
 * The main entry point for the Board Game application.
 * This class provides the `main` method to launch the application.
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private Main() {

    }

    /**
     * The main method that launches the Board Game application.
     *
     * @param args the command-line arguments
     */
    public static void main(final String[] args) {
        Application.launch(BoardGameApplication.class, args);
    }
}
