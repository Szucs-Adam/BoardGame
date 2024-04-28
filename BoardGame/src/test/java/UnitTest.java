
import boardgame.Controllers.BoardGameController;
import boardgame.model.BoardGameModel;
import boardgame.model.Position;
import boardgame.model.Square;

import boardgame.util.BoardGameMoveSelector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {


    @Test
    public void testHasMovableCircleFalse() {
        BoardGameModel model = new BoardGameModel();

        model.setSquare(new Position(0, 0), Square.HEAD);
        model.setSquare(new Position(1, 1), Square.BLANK);

        boolean result = model.hasMovableCircle();

        assertTrue(result);
    }

    @Test
    public void testHasMovableCircleTrue() {
        BoardGameModel model = new BoardGameModel();

        model.setSquare(new Position(0, 0), Square.HEAD);
        model.setSquare(new Position(0, 1), Square.TAIL);

        boolean result = model.hasMovableCircle();

        assertTrue(result);
    }

    @Test
    public void testPlaceACircleRed() {
        BoardGameModel model = new BoardGameModel();

        model.setPlayerRedTurn(true);

        model.setSquare(new Position(0, 0), Square.TAIL);
        model.setSquare(new Position(0, 1), Square.HEAD);
        model.setSquare(new Position(1, 0), Square.HEAD);
        model.setSquare(new Position(1, 1), Square.TAIL);

        model.placeACircle(0, 0);

        assertEquals(Square.HEAD, model.getSquare(new Position(0, 0)));

        assertEquals(Square.HEAD, model.getSquare(new Position(0, 1)));
        assertEquals(Square.HEAD, model.getSquare(new Position(1, 0)));
        assertEquals(Square.HEAD, model.getSquare(new Position(1, 1)));
    }

    @Test
    public void testPlaceACircleBlue() {
        BoardGameModel model = new BoardGameModel();
        model.setPlayerRedTurn(false);

        model.setSquare(new Position(0, 0), Square.HEAD);
        model.setSquare(new Position(0, 1), Square.TAIL);
        model.setSquare(new Position(1, 0), Square.TAIL);
        model.setSquare(new Position(1, 1), Square.HEAD);

        model.placeACircle(0, 0);

        assertEquals(Square.TAIL, model.getSquare(new Position(0, 0)));

        assertEquals(Square.TAIL, model.getSquare(new Position(0, 1)));
        assertEquals(Square.TAIL, model.getSquare(new Position(1, 0)));
        assertEquals(Square.TAIL, model.getSquare(new Position(1, 1)));
    }

    @Test
    public void testHasAdjacentFreeSpaceFalse() {
        BoardGameModel model = new BoardGameModel();

        model.setSquare(new Position(0, 0), Square.HEAD);
        model.setSquare(new Position(0, 1), Square.BLANK);
        model.setSquare(new Position(1, 0), Square.BLANK);

        boolean result = model.hasSpaceForCircle(true);

        assertTrue(result);
    }

    @Test
    public void testHasAdjacentFreeSpaceTrue() {
        BoardGameModel model = new BoardGameModel();

        model.setSquare(new Position(0, 0), Square.HEAD);
        model.setSquare(new Position(0, 1), Square.TAIL);
        model.setSquare(new Position(1, 0), Square.TAIL);

        boolean result = model.hasSpaceForCircle(true);

        assertTrue(result);
    }

    @Test
    public void testCountNoneSquares() {
        BoardGameModel model = new BoardGameModel();

        // Set specific squares to Square.NONE
        model.setSquare(new Position(0, 0), Square.TAIL);
        model.setSquare(new Position(1, 2), Square.HEAD);
        model.setSquare(new Position(2, 1), Square.BLANK);

        int count = model.countNoneSquares();

        Assertions.assertEquals(33, count);
    }

    @Test
    public void testRedWins() {
        final int countRed = 5;
        final int countBlue = 3;
        final String pathOfFile = "data.txt";

        BoardGameController myClass = new BoardGameController();

        boolean result = myClass.checkAndLogTheWin(countRed, countBlue, pathOfFile);

        Assertions.assertTrue(result);
    }

    @Test
    public void testBlueWins() {
        final int countRed = 3;
        final int countBlue = 5;
        final String pathOfFile = "UnitTest.json";

        BoardGameController myClass = new BoardGameController();

        boolean result = myClass.checkAndLogTheWin(countRed, countBlue, pathOfFile);

        Assertions.assertFalse(result);
    }


    @Test
    void testIsOnBoard() {
        assertTrue(BoardGameModel.isOnBoard(new Position(0, 0)));
        assertTrue(BoardGameModel.isOnBoard(new Position(3, 3)));
        assertTrue(BoardGameModel.isOnBoard(new Position(2, 1)));

        assertFalse(BoardGameModel.isOnBoard(new Position(-1, 0)));
        assertFalse(BoardGameModel.isOnBoard(new Position(0, -1)));
        assertFalse(BoardGameModel.isOnBoard(new Position(-2, 3)));
        assertFalse(BoardGameModel.isOnBoard(new Position(2, -3)));
        assertFalse(BoardGameModel.isOnBoard(new Position(6, 5)));
    }

    @Test
    void testHasSpaceForCircle() {
        BoardGameModel boardGameModel = new BoardGameModel();

        boardGameModel.setSquare(new Position(0, 0), Square.HEAD);
        boardGameModel.setSquare(new Position(1, 1), Square.TAIL);
        boardGameModel.setSquare(new Position(0, 1), Square.TAIL);
        boardGameModel.setSquare(new Position(1, 0), Square.TAIL);

        boolean hasSpaceRed = boardGameModel.hasSpaceForCircle(true);
        assertFalse(hasSpaceRed);

        boolean hasSpaceBlue = boardGameModel.hasSpaceForCircle(false);
        assertTrue(hasSpaceBlue);
    }
}
