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

        assert(game.isFinished());
    }
}
