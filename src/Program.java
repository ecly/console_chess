import Chess.ChessGame;
import Chess.Pieces.Tuple;
import Console.InputHandler;

import java.util.Scanner;

public class Program {

    public static void main(String args[]){
        InputHandler handler = new InputHandler();
        Scanner scanner = new Scanner(System.in);
        ChessGame game = new ChessGame();
        while (!game.isFinished()){
            System.out.println("Enter move (eg. A2-A3):");
            String input = scanner.nextLine().trim();

            if(!handler.isValid(input)){
                System.out.println("Invalid input!");
                System.out.println("Valid input is in form: A2-A3");
            } else {

                Tuple from = handler.getFrom(input);
                Tuple to = handler.getTo(input);

                if (game.isValidMove(from, to)) {
                    game.playMove(from, to);
                } else {
                    System.out.println("Invalid move!");
                    System.out.println("Please read a cheese book for rules.");
                    System.out.println("If you believe the move to be valid nonetheless, it's still not.");
                }
            }
        }
        System.out.println("Game has finished. Thanks for playing.");
    }

}
