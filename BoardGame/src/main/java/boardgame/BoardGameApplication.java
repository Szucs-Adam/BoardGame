package boardgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class BoardGameApplication extends Application {

    /**
     * Starts the board game application by
     * loading the UI from the menu FXML file.
     * Subclasses can override this method to provide custom startup behavior.
     *
     * @param stage the primary stage for the application
     * @throws IOException if an error occurs while loading the FXML file
     */
    @Override
    public void start(final Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/menu.fxml"));
        stage.setTitle("Board Game");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

