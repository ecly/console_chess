import Chess.ChessGame;
import Chess.Tuple;
import Console.InputHandler;
import Console.BoardDisplay;

import java.util.Scanner;


public class Program {

    public static void main(String args[]) throws Exception {
        InputHandler handler = new InputHandler();
        Scanner scanner = new Scanner(System.in);

        ChessGame game = new ChessGame();
        BoardDisplay.clearConsole();
        BoardDisplay.printBoard(game.getBoard());
        while (!game.isFinished()) {
            System.out.println("Enter move (eg. A2-A3): ");
            String input = scanner.nextLine();

            if (!handler.isValid(input)) {
                System.out.println("Invalid input!");
                System.out.println("Valid input is in form: A2-A3");
            } else {
                Tuple from = handler.getFrom(input);
                Tuple to = handler.getTo(input);

                boolean movePlayed = game.playMove(from, to);
                if (!movePlayed)
                    System.out.println("Illegal move!");
                else {
                    BoardDisplay.clearConsole();
                    BoardDisplay.printBoard(game.getBoard());
                }
            }
        }
        scanner.close();
        System.out.println("Game has finished. Thanks for playing.");
    }
}
