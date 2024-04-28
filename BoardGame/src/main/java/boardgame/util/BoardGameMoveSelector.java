package boardgame.util;

import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * The {@code BoardGameMoveSelector} class represents
 * a utility class for managing moves in a board game.
 * It allows the selection of positions and tracks the
 * current phase of the move selection process.
 * This class is designed to work with implementations of the
 * {@link boardgame.model.BoardGameModel} interface.
 */
public class BoardGameMoveSelector {



    /**
     * The different phases of the move selection process in the board game.
     */
    public enum Phase {
        /**
         * The phase where the player is selecting the starting position.
         */
        SELECT_FROM,

        /**
         * The phase where the player is selecting the target position.
         */
        SELECT_TO,

        /**
         * The phase where the move is ready to be executed.
         */
        READY_TO_MOVE
    }

    /**
     * Defining the BoardGameModel for the move rules.
     */
    private BoardGameModel model;


    /**
     * The current phase of the move selection process.
     */
    private ReadOnlyObjectWrapper<Phase> phase
            = new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);

    /**
     * Indicates whether the current selection is invalid.
     */
    private boolean invalidSelection = false;

    /**
     * The position from which the move is being made.
     */
    private Position from;

    /**
     * The target position to which the move is being made.
     */
    private Position to;

    /**
     * Constructs a new {@code BoardGameMoveSelector}
     * object with the specified board game model.
     *
     * @param boardGameModel the board game model
     */
    public BoardGameMoveSelector(final BoardGameModel boardGameModel) {
        this.model = boardGameModel;
    }

    /**
     * Returns the current phase of move selection.
     *
     * @return the current phase
     */
    public Phase getPhase() {
        return phase.get();
    }

    /**
     * Returns the read-only property representing
     * the current phase of move selection.
     *
     * @return the read-only property of the current phase
     */
    public ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase.getReadOnlyProperty();
    }

    /**
     * Checks if the move selector is in the "ready to move" phase.
     *
     * @return {@code true} if in the "ready to move" phase,
     * {@code false} otherwise
     */
    public boolean isReadyToMove() {
        return phase.get() == Phase.READY_TO_MOVE;
    }

    /**
     * Selects the specified position based on
     * the current phase of move selection.
     *
     * @param position the position to select
     * @throws IllegalStateException if the selection is
     * not valid in the current phase
     */
    public void select(final Position position) {
        switch (phase.get()) {
            case SELECT_FROM -> selectFrom(position);
            case SELECT_TO -> selectTo(position);
            case READY_TO_MOVE -> throw new IllegalStateException();
            default ->
                    throw new IllegalArgumentException("Invalid phase");
        }
    }

    /**
     * Selects the "from" position in the move selection process.
     *
     * @param position the "from" position to select
     */
    public  void selectFrom(final Position position) {
        if (!model.isEmpty(position)) {
            from = position;
            phase.set(Phase.SELECT_TO);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }

    /**
     * Selects the "to" position in the move selection process.
     *
     * @param position the "to" position to select
     */
    public void selectTo(final Position position) {
        if (model.canMove(from, position)) {
            to = position;
            phase.set(Phase.READY_TO_MOVE);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }


    /**
     * Returns the "from" position selected in the move selection process.
     *
     * @return the "from" position
     * @throws IllegalStateException if the current phase is not "select from"
     */
    public Position getFrom() {
        if (phase.get() == Phase.SELECT_FROM) {
            throw new IllegalStateException();
        }
        return from;
    }

    /**
     * Returns the "to" position selected in the move selection process.
     *
     * @return the "to" position
     * @throws IllegalStateException if the current phase is not "ready to move"
     */
    public Position getTo() {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        return to;
    }

    /**
     * Checks if the current move selection is invalid.
     *
     * @return {@code true} if the selection is invalid, {@code false} otherwise
     */
    public boolean isInvalidSelection() {
        return invalidSelection;
    }

    /**
     * Makes a move based on the selected positions
     * and the current player's turn.
     *
     * @param isPlayerRedTurn {@code true} if it is
     * the red player's turn, {@code false} otherwise
     * @throws IllegalStateException if the current
     * phase is not "ready to move"
     */
    public void makeMove(final boolean isPlayerRedTurn) {
        if (phase.get() != Phase.READY_TO_MOVE) {
            throw new IllegalStateException();
        }
        model.move(from, to);

        reset();
    }

    /**
     * Resets the move selector by clearing the selected
     * positions and setting the phase back to "select from".
     */
    public void reset() {
        from = null;
        to = null;
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
    }

}
