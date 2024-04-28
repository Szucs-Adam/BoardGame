package boardgame.Controllers;

import boardgame.data.DataHandler;
import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import boardgame.model.Square;
import boardgame.util.BoardGameMoveSelector;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.tinylog.Logger;


public class BoardGameController {

    /**
     * The game board represented as a GridPane. It contains
     * the visual representation of the game squares.
     */
    @FXML
    private GridPane board;
    /**
     * The model that manages the game state and logic.
     */
    private BoardGameModel model = new BoardGameModel();
    /**
     * The count of red circles on the board.
     */
    private int redCount = 0;
    /**
     * The count of blue circles on the board.
     */
    private int blueCount = 0;

    /**
     * The file path for the statistics file.
     */
    private String filePath = "Statistic.json";

    /**
     * The move selector for the board game.
     * It handles the selection and movement of circles on the board.
     */
    private BoardGameMoveSelector selector = new BoardGameMoveSelector(model);

    /**
     * The UI controller for the game.
     * It manages the user interface and game interactions.
     */
    private UIController uiController = new UIController();

    /**
     * Indicates whether there is a winner in the game.
     * @param isWinner true if the game ended and there is a winner
     */
    public void setThereIsAWWinner(final boolean isWinner) {
        this.thereIsAWWinner = isWinner;
    }

    /**
     * Sets the flag indicating whether there is a winner in the game.
     *
     * @param thereIsAWinner true if there is a winner, false otherwise
     */
    private boolean thereIsAWWinner = false;

    /**
     * Checks if there is a winner in the game.
     *
     * @return true if there is a winner, false otherwise
     */
    public boolean isThereIsAWWinner() {
        return thereIsAWWinner;
    }

    /**
     * This method initializes the game board and
     * sets up the initial state of the game.
     * It creates squares and adds them
     * to the board grid based on the board size.
     * It also sets specific squares with initial values
     * (HEAD, TAIL, BLANK) using the model.
     * This method is called during the initialization phase of the game.
     */
    @FXML
    private void initialize() {
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }

        model.setSquare(new Position(0, 0), Square.HEAD);
        model.setSquare(new Position(BoardGameModel.BOARD_SIZE - 1,
                BoardGameModel.BOARD_SIZE - 1), Square.HEAD);
        model.setSquare(new Position(0,
                BoardGameModel.BOARD_SIZE - 1), Square.TAIL);
        model.setSquare(new Position(BoardGameModel.BOARD_SIZE - 1,
                0), Square.TAIL);
        model.setSquare(new Position(BoardGameModel.BOARD_SIZE - (2 + 1),
                BoardGameModel.BOARD_SIZE - (2 + 1)), Square.BLANK);
    }

    /**
     * This method checks if the game has ended.
     * It iterates over each square on the board and
     * checks various conditions to determine the game outcome.
     * It checks if any player has no valid moves left,
     * if both players have circles on the board,
     * and if they can make moves. If a game-ending condition is met,
     * it logs the result and returns true.
     * Otherwise, it returns false, indicating that the game is still ongoing.
     * @return true if the game has ended, false otherwise
     */
    public boolean checkEndGame() {
        boolean redCircleFound = false;
        boolean blueCircleFound = false;
        boolean redCanMove = false;
        boolean blueCanMove = false;
        boolean noMoreMove = false;

        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = (StackPane)
                 board.getChildren().get(i * BoardGameModel.BOARD_SIZE + j);
                var circle = (Circle) square.getChildren().get(0);
                if (!model.hasSpaceForCircle(model.isPlayerRedTurn())
                        || !model.hasSpaceForCircle(!model.isPlayerRedTurn())) {
                    Logger.info("Can not move --- GAME OVER");
                    if (model.isPlayerRedTurn()) {
                        blueCount = model.countNoneSquares();
                } else {
                            redCount = model.countNoneSquares();
                        }
                    noMoreMove = true;
                } else if (circle.getFill().equals(Color.RED)
                        && model.hasMovableCircle()) {
                    redCircleFound = true;
                    redCanMove = true;

                } else if (circle.getFill().equals(Color.BLUE)
                        && model.hasMovableCircle()) {
                    blueCircleFound = true;
                    blueCanMove = true;

                }   else if (!model.hasMovableCircle()) {
                    Logger.info("No valid moves for either player");
                    noMoreMove = true;
                }

                if (redCircleFound && blueCircleFound
                        && (redCanMove || blueCanMove)) {
                    return false;
                }

            }
        }
        if (noMoreMove) {
            if (redWon()) {
                Logger.info("Red wins");
            } else {
                Logger.info("Blue wins");
            }
        } else if (!redCircleFound) {
            Logger.info("Blue wins");
            redWon();
        } else if (!blueCircleFound) {
            Logger.info("Red wins");
            redWon();
        }
        return true;
    }

    /**
     * This method creates a square for the game board
     * at the specified position (i, j).
     * It creates a StackPane for the square and sets its style class.
     * Then, based on the square value in the model,
     * it adds either a black square or a colored circle to the StackPane.
     * The circle's fill color is bound to the square value in the model.
     * Finally, it sets a mouse click event handler for the square.
     *
     * @param i the row index of the square
     * @param j the column index of the square
     * @return the created StackPane representing the square
     */
    private StackPane createSquare(final int i, final int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");

        final int rectangleSize = 100;

        final  int circleRadius = 50;

        var blackSquare = new Rectangle(rectangleSize, rectangleSize);

        var circle = new Circle(circleRadius);

        if (model.squareProperty(i, j).get() == Square.BLANK) {
            square.getChildren().add(blackSquare);
        } else {
            square.getChildren().add(circle);
        }
        circle.fillProperty()
                .bind(createSquareBinding(model.squareProperty(i, j)));

        square.setOnMouseClicked(this::handleMouseClick);
        return square;

    }


    /**
     * Handles the mouse click event on the game board.
     *
     * @param event The mouse click event
     */
    @FXML
    private void handleMouseClick(final MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Click on square ({}, {})", row, col);

        selector.select(new Position(row, col));

        var selectedSquare = model.squareProperty(row, col).get();
        var currentPlayerSquare =
                model.isPlayerRedTurn() ? Square.HEAD : Square.TAIL;

        if (selector.isReadyToMove()) {
            selector.makeMove(model.isPlayerRedTurn());

            handleEndGame();

            incrementMoveCounter();

            printOutMoveCounter();

        } else if (selectedSquare == Square.NONE
                && hasAdjacentCircle(row, col, currentPlayerSquare)) {
            model.placeACircle(row, col);

            Logger.info("Placed a circle to ({}, {})", row, col);

            if (model.isPlayerRedTurn()) {
            model.setPlayerRedTurn(false);
            } else {
                model.setPlayerRedTurn(true);
            }


            handleEndGame();

            selector.reset();

            incrementMoveCounter();

            printOutMoveCounter();

        }
    }

    /**
     * Prints out the move counters for both the red and blue players.
     */

    private void printOutMoveCounter() {
        Logger.info("Red Player Moves: {}", model.getRedPlayerMoveCounter());
        Logger.info("Blue Player Moves: {}", model.getBluePlayerMoveCounter());
    }

    /**
     * Increments the move counter based on the current player's turn.
     */
    private void incrementMoveCounter() {
        if (!model.isPlayerRedTurn()) {
            model.addRedPlayerMoveCounter();
        } else {
            model.addBluePlayerMoveCounter();
        }
    }
    /**
     * Handles the end of the game. Checks if the game has ended and
     * performs the necessary actions.
     */
    private void handleEndGame() {
        if (checkEndGame()) {
            Logger.info("Game Over");
            setThereIsAWWinner(true);
            Logger.info(isThereIsAWWinner());
            uiController.gameOver();

        }
    }

    /**
     * Creates a binding for the squareProperty to determine
     * the fill color of the circles in squares.
     * @param squareProperty the square property to bind
     * @return the object binding for the fill color
     * of the circle in the square
     */

    private ObjectBinding<Paint>
    createSquareBinding(final ReadOnlyObjectProperty<Square> squareProperty) {
        return new ObjectBinding<Paint>() {
            {
                super.bind(squareProperty);
            }
            @Override
            protected Paint computeValue() {
                return switch (squareProperty.get()) {
                    case NONE -> Color.TRANSPARENT;
                    case HEAD -> Color.RED;
                    case TAIL -> Color.BLUE;
                    case BLANK -> Color.BLACK;
                };
            }
        };
    }

    /**
     * Checks if there is an adjacent circle to the
     * chosen position with the same currentPlayerSquare.
     * @param row the row index of the position
     * @param col the column index of the position
     * @param currentPlayerSquare the square of the current player
     * @return true if there is an adjacent circle with the same square,
     * false otherwise
     */
    private boolean hasAdjacentCircle(final int row, final int col,
                                      final Square currentPlayerSquare) {
        for (int i = Math.max(0, row - 1);
             i <= Math.min(BoardGameModel.BOARD_SIZE - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1);
                 j <= Math.min(BoardGameModel.BOARD_SIZE - 1, col + 1); j++) {
                if (i != row || j != col) {
                    if (model.getSquare(new Position(i, j))
                            == currentPlayerSquare) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Checks if the red player has won the game based
     * on the number of red and blue circles on the board.
     * If the red player has won, it logs the result
     * and updates the data file.
     *
     * @return true if the red player has won, false otherwise.
     */
    public boolean redWon() {

        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = (StackPane)
                     board.getChildren().get(i * BoardGameModel.BOARD_SIZE + j);
                var circle = (Circle) square.getChildren().get(0);

                if (circle.getFill().equals(Color.RED)) {
                    redCount++;
                } else if (circle.getFill().equals(Color.BLUE)) {
                    blueCount++;
                }
            }
        }
        return checkAndLogTheWin(redCount, blueCount, filePath);
       }


    /**
     * Creates a string representation of the state
     * using the given integer values for the UI.
     *
     * @param int1 the first integer value
     * @param int2 the second integer value
     * @return the state string in the format "int1:int2"
     */

    public String createState(final int int1, final int int2) {
        return int1 + ":" + int2;
    }

    /**
     * Checks the counts of red and blue circles and determines the winner.
     * Logs the result and updates the data file with the winner's information.
     *
     * @param countRed the count of red circles
     * @param countBlue the count of blue circles
     * @param pathOfFile the path to the data file
     * @return true if the red player has won, false otherwise.
     */
    public boolean checkAndLogTheWin(final int countRed,
                                     final int countBlue,
                                     final String pathOfFile) {
        DataHandler writer = new DataHandler(pathOfFile);

        if (countRed > countBlue) {
            Logger.info("Red wins ({}:{})", countRed, countBlue);
            writer.extendData("Red",
model.getRedPlayerMoveCounter() + 1, createState(countRed, countBlue));
            return true;
        } else {
            Logger.info("Blue wins ({}:{})", countBlue, countRed);
            writer.extendData("Blue",
model.getBluePlayerMoveCounter() + 1, createState(countRed, countBlue));
            return false;
        }
    }

}
