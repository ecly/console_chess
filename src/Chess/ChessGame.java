package Chess;

import Chess.ChessPiece.PieceColor;
import Console.BoardDisplay;

public class ChessGame {

    private ChessBoard board;
    private boolean isFinished;
    private PieceColor currentPlayer;

    public ChessGame(){
        board = new ChessBoard();
        currentPlayer = PieceColor.White;
        isFinished = false;

        BoardDisplay.clearConsole();
        BoardDisplay.printBoard(board);
    }

    public void playMove(Tuple from, Tuple to){
        if(isValidMove(from, to)) {
            Tile fromTile = board.getBoardArray()[from.Y()][from.X()];
            ChessPiece pieceToMove = fromTile.getPiece();

            Tile toTile = board.getBoardArray()[to.Y()][to.X()];
            toTile.setPiece(pieceToMove);

            fromTile.empty();
            endTurn();
            BoardDisplay.printBoard(board);
        } else
            System.out.println("Invalid move!");
    }

    private void endTurn(){
        if (currentPlayer == PieceColor.White) currentPlayer = PieceColor.Black;
        else currentPlayer = PieceColor.White;
    }

    public boolean isValidMove(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        ChessPiece toPiece = board.getTileFromTuple(to).getPiece();

        if (fromPiece == null){
            System.out.println("From tile is empty!");
            return false;
        } else if (fromPiece.color() != currentPlayer) {
            System.out.println("Not your piece!");
            return false;
        } else if (toPiece != null && toPiece.color() == currentPlayer) {//null pointer if null not evaluated first
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
                    if (move.onTakeOnly){//if move is only legal on take (pawns)
                        Tile toTile = board.getTileFromTuple(to);
                        if (toTile.isEmpty()) break;

                        ChessPiece toPiece = toTile.getPiece();
                        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
                        validMove = fromPiece.color() != toPiece.color();//if different color, valid move
                        break;
                    } else {
                        validMove = true;
                        break;
                    }
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
        if(!validMove) System.out.println("Illegal move for piece!");
        return validMove;
    }

    public boolean isFinished(){
        return isFinished;
    }
}
