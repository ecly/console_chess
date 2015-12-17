package Chess;

import Console.BoardDisplay;

public class ChessGame {

    private ChessBoard board;
    private BoardDisplay display;
    private boolean isFinished;

    public ChessGame(){
        board = new ChessBoard();
        display = new BoardDisplay();
        isFinished = false;
    }

    public ChessBoard getBoard(){
        return board;
    }

    public void printBoard(){
        display.printBoard(board);
    }

    private boolean isFinished(){
        return isFinished;
    }
}
