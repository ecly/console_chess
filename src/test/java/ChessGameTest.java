import Console.*;
import Chess.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChessGameTest {
    @Test
    public void testNewGameIsNotFinished() {
        ChessGame game = new ChessGame();
        assertTrue("Game shouldn't start finished", !game.isFinished());
    }

    @Test
    public void testFoolsMateEndsGame() {
        String[] foolsMate = new String[]{"F2-F3", "E7-E6", "G2-G4", "D8-H4"};
        ChessGame game = new ChessGame();
        InputHandler handler = new InputHandler();
        for (String move : foolsMate){
            Tuple from = handler.getFrom(move);
            Tuple to = handler.getTo(move);

            boolean movePlayed = game.playMove(from, to);
            if (!movePlayed) fail("Should be legal move");
        }
        Console.BoardDisplay.printBoard(game.getBoard());
        assert(game.isFinished());
    }

    @Test
    public void testFirstMovePawn() {
        InputHandler handler = new InputHandler();
        Tuple location = handler.parse("A2");
        ChessGame game = new ChessGame();
        assert(game.isFirstMoveForPawn(location, game.getBoard()));
    }

    @Test
    public void testNotFirstMovePawn() {
        InputHandler handler = new InputHandler();
        String move = "A2-A3";
        Tuple from = handler.getFrom(move);
        Tuple to = handler.getTo(move);
        ChessGame game = new ChessGame();
        game.playMove(from, to);
        assert(!game.isFirstMoveForPawn(to, game.getBoard()));
    }
}
