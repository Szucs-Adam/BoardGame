package boardgame.data;

/**
 * The {@code Data} class represents the game
 * data containing information such as the winner color,
 * move counter, and game state.
 */
public class Data {

    /**
     * Color of the winners Circle.
     */
    private String winnerColor;

    /**
     *  With how many moves the winner won.
     */
    private int moveCounter;

    /**
     *  With what state the winner won.
     */
    private String state;

    /**
     * Constructs a new {@code Data} object with the
     * specified winner color, move counter, and game stateAtGameOver.
     *
     * @param winnersColor the color of the winner
     * @param withHowManyMoves the number of moves
     * @param stateAtGameOver the game stateAtGameOver
     */
    public Data(final String winnersColor,
                final int withHowManyMoves, final String stateAtGameOver) {
        this.winnerColor = winnersColor;
        this.moveCounter = withHowManyMoves;
        this.state = stateAtGameOver;
    }

    /**
     * Returns the move counter.
     *
     * @return the move counter
     */
    public int getMoveCounter() {
        return moveCounter;
    }

    /**
     * Returns the color of the winner.
     *
     * @return the color of the winner
     */
    public String getWinnerColor() {
        return winnerColor;
    }

    /**
     * Returns the game state.
     *
     * @return the game state
     */
    public String getState() {
        return state;
    }


}
