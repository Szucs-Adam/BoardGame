package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import org.tinylog.Logger;


public class BoardGameModel {

    /**
     * Defining the Board-size for the board-game.
     */
    public static final int BOARD_SIZE = 6;

    /**
     * Returns the current move counter for the red player.
     *
     * @return the current move counter for the red player
     */
    public int getRedPlayerMoveCounter() {
        return redPlayerMoveCounter;
    }

    /**
     * Increases the move counter for the blue player by 1.
     */
    public void addRedPlayerMoveCounter() {
        redPlayerMoveCounter++;
    }

    /**
     * The move counter for the red player.
     */
    private int redPlayerMoveCounter = 0;

    /**
     * Returns the current move counter for the blue player.
     *
     * @return the current move counter for the blue player
     */

    public int getBluePlayerMoveCounter() {
        return bluePlayerMoveCounter;
    }

    /**
     * Increases the move counter for the blue player by 1.
     */
    public void addBluePlayerMoveCounter() {
        bluePlayerMoveCounter++;
    }

    /**
     * The move counter for the blue player.
     */
    private int bluePlayerMoveCounter = 0;

    /**
     * Represents the game board as a 2D array
     * of read-only object wrappers for squares.
     * The board is a grid of squares with
     * dimensions {@link #BOARD_SIZE} x {@link #BOARD_SIZE}.
     * Each element in the array is a read-only
     * object wrapper for a square, providing
     * a read-only property for accessing the square at a specific position.
     */
    private ReadOnlyObjectWrapper<Square>[][] board =
            new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];

    /**
     * Returns a boolean value indicating whether
     * it is currently the red player's turn.
     *
     * @return true if it is currently the red player's turn, false otherwise
     */
    public boolean isPlayerRedTurn() {
        return isPlayerRedTurn;
    }

    /**
     * Sets the boolean value indicating
     * whether it is currently the red player's turn.
     *
     * @param playerRedTurn the boolean value to set,
     * true if it is currently the red player's turn, false otherwise
     */
    public void setPlayerRedTurn(final boolean playerRedTurn) {
        isPlayerRedTurn = playerRedTurn;
    }

    /**
     * Indicates whether it is currently the red player's turn.
     */
    private boolean isPlayerRedTurn = true;

    /**
     * Constructs a new BoardGameModel object and initializes the game board.
     */
    public BoardGameModel() {
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = new ReadOnlyObjectWrapper<Square>(
                        switch (i) {
                            default -> Square.NONE;
                        }
                );
            }
        }
    }

    /**
     * Returns the read-only property for the square at the specified position.
     *
     * @param i the row index of the position
     * @param j the column index of the position
     * @return the read-only property for the square at the specified position
     */
    public ReadOnlyObjectProperty<Square>
    squareProperty(final int i, final int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * Returns the square at the specified position.
     *
     * @param p the position to retrieve the square from
     * @return the square at the specified position
     */
    public Square getSquare(final Position p) {

        return board[p.row()][p.col()].get();
    }

    /**
     * Sets the square at the specified position to the given square value.
     *
     * @param p      the position to set the square
     * @param square the square value to set
     */
    public void setSquare(final Position p, final Square square) {
        board[p.row()][p.col()].set(square);
    }

    /**
     * Moves a circle from the "from" position to the "to" position.
     *
     * @param from the starting position of the circle
     * @param to   the target position for the circle
     */
    public void move(final Position from, final Position to) {
        var currentPlayerSquare = getCurrentPlayerSquare();
        var fromSquare = getSquare(from);

        if (fromSquare != currentPlayerSquare) {
            Logger.info("Invalid move: Not the current player's turn");
            return;
        }

        if (!canMove(from, to)) {
            Logger.info("Not a valid move for the selected piece");
            return;
        }

        setSquare(to, getSquare(from));
        setSquare(from, Square.NONE);

        placeACircle(to.row(), to.col());
        Logger.info("Placed a circle to ({}, {})", to.row(), to.col());

        if (isPlayerRedTurn) {
            isPlayerRedTurn = false;
        } else {
            isPlayerRedTurn = true;
        }

    }

    /**
     * Returns the square corresponding to the current player.
     *
     * @return the square of the current player (HEAD for red, TAIL for blue)
     */

    private Square getCurrentPlayerSquare() {
        return isPlayerRedTurn ? Square.HEAD : Square.TAIL;
    }

    /**
     * Checks if a move from the "from" position
     * to the "to" position is a valid move for a circle.
     *
     * @param from the starting position
     * @param to   the target position
     * @return true if the move is valid, false otherwise
     */

    public boolean canMove(final Position from, final Position to) {
        return isOnBoard(from) && isOnBoard(to)
                && !isEmpty(from) && isEmpty(to) && isPawnMove(from, to);
    }


    /**
     * Checks if the specified position is empty (contains Square.NONE).
     *
     * @param p the position to check
     * @return true if the position is empty, false otherwise
     */

    public boolean isEmpty(final Position p) {
        return getSquare(p) == Square.NONE;
    }

    /**
     * Checks if the specified position is within the board boundaries.
     *
     * @param p the position to check
     * @return true if the position is on the board, false otherwise
     */
    public static boolean isOnBoard(final Position p) {
        return 0 <= p.row() && p.row() < BOARD_SIZE
                && 0 <= p.col() && p.col() < BOARD_SIZE;
    }

    /**
     * To check what is the Max move in line.
     */
    private static int maxMoveDistance = 2 + 1;

    /**
     * To check what is the Max move in cross.
     */
    private static int maxMoveCross = 2 * 2;

    /**
     * Checks if a move from the "from" position
     * to the "to" position is a valid move for a pawn.
     *
     * @param from the starting position
     * @param to   the target position
     * @return true if the move is valid for a pawn, false otherwise
     */
    public static boolean isPawnMove(final Position from, final Position to) {
        var dx = Math.abs(to.row() - from.row());
        var dy = Math.abs(to.col() - from.col());
        if (dx < maxMoveDistance && dy < maxMoveDistance) {
            return dx + dy == 2
                    || dx * dy == maxMoveCross
                    || dx + dy == 1
                    || dx * dy == 1;
        }

        return  false;

    }

    /**
     * Returns a string representation of the board.
     *
     * @return a string representation of the board
     */
    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var i = 0; i < BOARD_SIZE; i++) {
            for (var j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Places a circle at the specified row and column coordinates.
     * Updates the square at the specified position and
     * its adjacent squares with the newSquare value.
     *
     * @param row       the row coordinate
     * @param col       the column coordinate
     */
    public void placeACircle(final int row, final int col) {
        Square newSquare = (isPlayerRedTurn ? Square.HEAD : Square.TAIL);

        setSquare(new Position(row, col), newSquare);

        for (int i = Math.max(0, row - 1);
             i <= Math.min(BoardGameModel.BOARD_SIZE - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1);
                 j <= Math.min(BoardGameModel.BOARD_SIZE - 1, col + 1); j++) {
                Square squareToChange = getSquare(new Position(i, j));
                if (squareToChange
                        == (isPlayerRedTurn ? Square.TAIL : Square.HEAD)) {
                    setSquare(new Position(i, j), newSquare);
                }
            }
        }
    }
    /**
     * Checks if there is at least one movable circle on the board.
     *
     * @return true if there is a movable circle, false otherwise
     */
    public boolean hasMovableCircle() {
        for (var i = 0; i < BoardGameModel.BOARD_SIZE; i++) {
            for (var j = 0; j < BoardGameModel.BOARD_SIZE; j++) {
                var currentPosition = new Position(i, j);
                if (!isEmpty(currentPosition)) {
                    for (var x = 0; x < BoardGameModel.BOARD_SIZE; x++) {
                        for (var y = 0; y < BoardGameModel.BOARD_SIZE; y++) {
                            var newPosition = new Position(x, y);
                            if (canMove(currentPosition, newPosition)
                               && isPawnMove(currentPosition, newPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the current player has a free space next to a friendly circle.
     *
     * @param isRedPlayerTurn true if it's the red player's turn,
     * false if it's the blue player's turn
     * @return true if the current player has a free space next
     * to a friendly circle, false otherwise
     */
    public boolean hasSpaceForCircle(final boolean isRedPlayerTurn) {
        Square currentPlayerSquare
                = isRedPlayerTurn ? Square.HEAD : Square.TAIL;

        for (int i = 0; i < BoardGameModel.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardGameModel.BOARD_SIZE; j++) {
                if (getSquare(new Position(i, j)) == currentPlayerSquare) {
                    for (int x = Math.max(0, i - 1);
                         x <= Math.min(BoardGameModel.BOARD_SIZE - 1, i + 1);
                         x++) {
                        for (int y = Math.max(0, j - 1);
                         y <= Math.min(BoardGameModel.BOARD_SIZE - 1, j + 1);
                             y++) {
                            if (x != i || y != j) {
                             if (getSquare(new Position(x, y))
                                        == Square.NONE) {
                               if (hasAdjacentFriendlyCircle(new Position(x, y),
                                            currentPlayerSquare)) {
                                    Logger.info("Current player has free "
                                          + "space next to a friendly circle");
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Logger.info("Current player does not have free space");
        return false;
    }

    /**
     * Checks if there is an adjacent friendly circle to the given position.
     *
     * @param position           the position to check
     * @param currentPlayerSquare the square type of the current player
     * @return true if there is an adjacent friendly circle, false otherwise
     */

    private boolean hasAdjacentFriendlyCircle(final Position position,
                                         final Square currentPlayerSquare) {
        for (int i = Math.max(0, position.row() - 1);
             i <= Math.min(BoardGameModel.BOARD_SIZE - 1,
                     position.row() + 1); i++) {
            for (int j = Math.max(0, position.col() - 1);
                 j <= Math.min(BoardGameModel.BOARD_SIZE - 1,
                         position.col() + 1); j++) {
                if (getSquare(new Position(i, j)) == currentPlayerSquare) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The main method of the program.
     * It creates a new BoardGameModel instance
     * and prints its string representation.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        var model = new BoardGameModel();
        System.out.println(model);
    }

    /**
     * Counts the number of squares with the Square.NONE type.
     *
     * @return the count of squares with the Square.NONE type
     */

    public int countNoneSquares() {
        int count = 0;
        for (int i = 0; i < BoardGameModel.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardGameModel.BOARD_SIZE; j++) {
                if (getSquare(new Position(i, j)) == Square.NONE) {
                    count++;
                }
            }
        }
        return count;
    }

}
