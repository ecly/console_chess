package Chess;

import Chess.ChessPiece.PieceColor;
import Console.BoardDisplay;
import java.util.ArrayList;

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

    public boolean canColorTakeLocation(PieceColor takeColor, Tuple locationToTake){
        Tuple[] locations = getAllPiecesLocationForColor(takeColor);
        boolean canTake = false;
        for (Tuple tuple: locations){
            if (isValidMove(tuple, locationToTake)) {
                canTake = true;
                break;
            }
        }
        return canTake;
    }

    public Tuple[] getAllPiecesLocationForColor(PieceColor color){
        Tile[][] boardArray = board.getBoardArray();
        ArrayList<Tuple> locations = new ArrayList<>();
        for (int x = 0; x < boardArray.length; x++){
            for (int y = 0; y < boardArray[x].length; y++){
               if(!boardArray[x][y].isEmpty() && boardArray[x][y].getPiece().color() == color)
                   locations.add(new Tuple(x,y));
            }
        }
        return locations.toArray(new Tuple[0]);//allocate new array automatically.
    }

    public Move[] allPossibleMovesForPiece(ChessPiece piece, Tuple currentLocation){
        Move[] moves = piece.moves();
        ArrayList<Move> possibleMoves = new ArrayList<>();

        for(Move move: moves){
            int currentX = currentLocation.X();
            int currentY = currentLocation.Y();

            int newX = currentX + move.x;
            int newY = currentY + move.y;

            Tuple newLocation = new Tuple(newX, newY);

            if (isPossibleMoveForPiece(currentLocation, newLocation)) possibleMoves.add(move);
        }
        return possibleMoves.toArray(new Move[0]);//allocate new array automatically.
    }

    //TODO split in two
    private boolean isPossibleMoveForPiece(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        Move[] validMoves = fromPiece.moves();
        boolean repeatableMoves = fromPiece.repeatableMoves();
        int xMove = from.X() - to.X();
        int yMove = from.Y() - to.Y();

        //Reverse values for black, such that lowering y-values are treated as moving forward
        //this is only relevant in the case of pawns, but added for abstract movement assignment for pieces.
        if (currentPlayer == PieceColor.Black) {
            yMove = -yMove;
        }

        boolean validMove = false;
        if(!repeatableMoves){
            for (Move move : validMoves) {
                if (move.x == xMove && move.y == yMove) {
                    if (move.onTakeOnly){//if move is only legal on take (pawns)
                        Tile toTile = board.getTileFromTuple(to);
                        if (toTile.isEmpty()) break;

                        ChessPiece toPiece = toTile.getPiece();
                        validMove = fromPiece.color() != toPiece.color();//if different color, valid move
                        break;
                    //handling first move only for pawns
                    } else if (move.firstMoveOnly && (fromPiece.color() == PieceColor.White && from.Y() != 6
                               || fromPiece.color() == PieceColor.Black && from.Y() != 1)) {
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
