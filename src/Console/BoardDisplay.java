package Console;

import Chess.ChessBoard;
import Chess.ChessPiece;
import Chess.Tile;

public class BoardDisplay {


    public BoardDisplay(){

    }

    public static void printBoard(ChessBoard board){
        Tile[][] b = board.getBoardArray();
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++ ){
            System.out.print(b[i][j].value());
            }
        System.out.println();
        }
    }
}
