package Chess;

import Chess.Pieces.Tuple;
import Chess.ChessPiece.PieceColor;
import Console.BoardDisplay;

public class ChessGame {

    private ChessBoard board;
    private BoardDisplay display;
    private boolean isFinished;
    private PieceColor currentPlayer;

    public ChessGame(){
        board = new ChessBoard();
        display = new BoardDisplay();
        currentPlayer = PieceColor.White;
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
            endTurn();
            display.printBoard(board);
        }
    }

    private void endTurn(){
        if (currentPlayer == PieceColor.White) currentPlayer = PieceColor.Black;
        else currentPlayer = PieceColor.White;
    }

    public boolean isValidMove(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        ChessPiece toPiece = board.getTileFromTuple(to).getPiece();


        //Debug
        System.out.println("Current player:" + currentPlayer + ", PieceColor: " + fromPiece.color());

        if (fromPiece == null){
            System.out.println("From tile is empty!");
            return false;
        } else if (fromPiece.color() != currentPlayer) {
            System.out.println("Not your piece!");
            return false;
        } else if (toPiece.color() != currentPlayer) {
            System.out.println("Can't take own piece!");
            return false;
        }

        return isPossibleMoveForPiece(from, to);
    }

    private boolean isPossibleMoveForPiece(Tuple from, Tuple to){
        Move[] validMoves = board.getTileFromTuple(from).getPiece().moves();
        boolean repeatableMoves = board.getTileFromTuple(from).getPiece().repeatableMoves();
        int xMove = from.X() - to.X();
        int yMove = from.Y() - to.Y();

        boolean validMove = false;
        if(!repeatableMoves){
            for (Move move : validMoves) {
                if (move.x == xMove && move.y == yMove) {
                    validMove = true;
                    break;
                }
            }
        } else {
            for(int i = 0; i <= 8; i++){//Max number of repetitions
                for(Move move : validMoves) {
                    if (move.x * i == xMove && move.y * i == yMove) {
                        validMove = true;
                        break;
                    }
                }
            }
        }
        return validMove;
    }

    public void printBoard(){
        display.printBoard(board);
    }

    public boolean isFinished(){
        return isFinished;
    }
}
