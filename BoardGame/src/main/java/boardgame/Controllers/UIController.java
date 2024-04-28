package boardgame.Controllers;

import boardgame.data.Data;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.tinylog.Logger;
import boardgame.data.DataHandler;


import java.io.IOException;
import java.util.List;


public class UIController {

    /**
     * The TableView component to display statistics data.
     */
    @FXML
    private TableView<Data> statisticTable;
    /**
     * The TableColumn component for displaying the
     * winner color in the statistics table.
     */
    @FXML
    private TableColumn<Data, String> winner;
    /**
     * The TableColumn component for displaying
     * the number of moves in the statistics table.
     */
    @FXML
    private TableColumn<Data, Integer> moves;
    /**
     * The TableColumn component for displaying
     * the state of the game in the statistics table.
     */
    @FXML
    private TableColumn<Data, String> state;
    /**
     * The Button component to start a new game.
     */
    @FXML
    private Button startNewGame;
    /**
     * The Button component to quit the game.
     */
    @FXML
    private Button quitButton;
    /**
     * The WinnerColor to show the winner at the end of the game.
     */
    @FXML
    private Text winnerColorText;
    /**
     * Initializes the UI and updates the statistics table.
     */
    @FXML
    public void initialize() {
        DataHandler reader = new DataHandler("Statistic.json") {
        };
        List<Data> dataList = reader.readData();

        if (dataList != null) {
            winnerColorText.setText(dataList.get(dataList.size()
                    - 1).getWinnerColor());
            ObservableList<Data> data = FXCollections
                    .observableArrayList(dataList);
            winner.setCellValueFactory(
                    new PropertyValueFactory<>("winnerColor"));
            moves.setCellValueFactory(
                    new PropertyValueFactory<>("moveCounter"));
            state.setCellValueFactory(
                    new PropertyValueFactory<>("state"));
            statisticTable.setItems(data);
            Logger.info("Data base update is done");
        } else {
            Logger.info("Data base update failed.");
        }
        Logger.info("Statistics update done");
    }
    /**
     * Handles the Game Over event and opens the statistics window.
     */
    @FXML
    public void gameOver() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/ui/menu.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Logger.error("FAIL the opening of the statistics window.", e);
        }
    }
    /**
     * Starts a new game.
     *
     * @param event The action event.
     * @throws IOException if an error occurs during loading the FXML file.
     */
    @FXML
    public void startNewGame(final ActionEvent event) throws IOException {
        try {
            Stage stage = (Stage)
                    ((Node) event.getSource()).getScene().getWindow();
            Parent root =
                    FXMLLoader.load(getClass().getResource("/ui/ui.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
            Logger.info("Starting a new game");
        } catch (IOException e) {
            Logger.info("There was an error during starting new game");
        }
    }
    /**
     * Handles the quit button event and exits the application.
     */
    @FXML
    private void quitButton() {
        Logger.info("Quitting the game");
        Platform.exit();
    }

}



