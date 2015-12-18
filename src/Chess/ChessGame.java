package Chess;

import Chess.Pieces.Tuple;
import Console.BoardDisplay;

public class ChessGame {

    private ChessBoard board;
    private BoardDisplay display;
    private boolean isFinished;

    public ChessGame(){
        board = new ChessBoard();
        display = new BoardDisplay();
        isFinished = false;

        display.clearConsole();
        display.printBoard(board);
    }

    public void playMove(Tuple from, Tuple to){
        if(isValidMove(from, to)) {
            display.clearConsole();
            Tile fromTile = board.getBoardArray()[from.Y()][from.X()];
            ChessPiece pieceToMove = fromTile.getPiece();

            Tile toTile = board.getBoardArray()[to.Y()][to.X()];
            toTile.setPiece(pieceToMove);

            fromTile.empty();
            display.printBoard(board);
        }
    }

    public boolean isValidMove(Tuple from, Tuple to){
        return true;
    }

    public void printBoard(){
        display.printBoard(board);
    }

    public boolean isFinished(){
        return isFinished;
    }
}
