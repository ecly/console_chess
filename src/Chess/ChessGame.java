package Chess;

import Chess.ChessPiece.PieceColor;
import Chess.Pieces.Pawn;
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

    public boolean isColorCheckMate(PieceColor color){
        Tuple kingLocation = board.getKingLocation(color);

        ChessPiece king = board.getTileFromTuple(kingLocation).getPiece();
        Move[] possibleMoves = allPossibleMovesForPiece(king, kingLocation);
        boolean checkMate = true;
        for (Move move : possibleMoves){
            int newX = kingLocation.X() + move.x;
            int newY = kingLocation.Y() + move.y;
            Tuple newLocation = new Tuple(newX, newY);

            //if king can move to an empty tile or tile with opponent's piece in it, we're not check
            Tile tile = board.getTileFromTuple(newLocation);
            if (tile.isEmpty() || tile.getPiece().color() != color) {
                return false;
            }

            if (!isLocationCheckForColor(newLocation, color)){
                return false;
            }
        }
        return checkMate;
    }

    public boolean isKingCheck(PieceColor kingColor){
        Tuple kingLocation = board.getKingLocation(kingColor);
        return isLocationCheckForColor(kingLocation, kingColor);
    }

    private boolean isLocationCheckForColor(Tuple location, PieceColor color){
        PieceColor opponentColor = ChessPiece.opponent(color);
        Tuple[] piecesLocation = board.getAllPiecesLocationForColor(opponentColor);
        boolean isCheck = false;
        for(Tuple fromTuple: piecesLocation){
            if(isValidMove(fromTuple, location)){
                isCheck = true;
                break;
            }
    }
        return isCheck;
    }

    private void endTurn(){
        if (currentPlayer == PieceColor.White) currentPlayer = PieceColor.Black;
        else currentPlayer = PieceColor.White;
    }

    public boolean isValidMove(Tuple from, Tuple to){
        Tile fromTile = board.getTileFromTuple(from);
        Tile toTile = board.getTileFromTuple(to);
        ChessPiece fromPiece = fromTile.getPiece();
        ChessPiece toPiece = toTile.getPiece();

        if (fromPiece == null){
            return false;
        } else if (fromPiece.color() != currentPlayer) {
            return false;
        } else if (toPiece != null && toPiece.color() == currentPlayer) {//null pointer if null not evaluated first
            return false;
        } else if (isPossibleMoveForPiece(from, to)){
            toTile.setPiece(fromPiece);//temporarily play the move
            fromTile.empty();
            if (isKingCheck(currentPlayer)){//check that moves doesn't put oneself in check
                toTile.setPiece(toPiece);
                fromTile.setPiece(fromPiece);//revert
                return false;
            }
            //if mate, finish game
            if (isColorCheckMate(ChessPiece.opponent(currentPlayer)))
                isFinished = true;

            toTile.setPiece(toPiece);
            fromTile.setPiece(fromPiece);//revert
            return true;//conditional path for legal move
        }
        return false;
    }

    public boolean canColorTakeLocation(PieceColor takeColor, Tuple locationToTake){
        Tuple[] locations = board.getAllPiecesLocationForColor(takeColor);
        boolean canTake = false;
        for (Tuple tuple: locations){
            if (isValidMove(tuple, locationToTake)) {
                canTake = true;
                break;
            }
        }
        return canTake;
    }

    private Move[] allPossibleMovesForPiece(ChessPiece piece, Tuple currentLocation){
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

        //due to the orientation of the board
        int xMove = to.X() - from.X();
        int yMove = to.Y() - from.Y();

        //Reverse values for white, such that lowering y-values are treated as moving forward
        //this is only relevant in the case of pawns, but added for abstract movement assignment for pieces.
        if (currentPlayer == PieceColor.White && fromPiece instanceof Pawn) {
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
                        return true;
                    }
                }
            }
        } else {
            for(int i = 1; i <= 7; i++){//Max number of repetitions
                for(Move move : validMoves) {
                    if (move.x * i == xMove && move.y * i == yMove) {//generally check for possible move
                        //if move is generally valid - check if path is valid up till i
                        for (int j = 1; j <= i; j++){
                            System.out.printf("fromX: %s, fromY: %s, move.x: %s, move.y %s, j: %s, i: %s\n", from.X(), from.Y(), move.x, move.y, j, i);
                            Tile tile = board.getTileFromTuple(new Tuple(from.X() + move.x * j, from.Y() +move.y * j));

                            //if passing through non empty tile return false
                            if (j != i && !tile.isEmpty())
                                return false;

                            //if last move and toTile is empty or holds opponents piece, return true
                            if (j == i && (tile.isEmpty() || tile.getPiece().color() != currentPlayer))
                                return true;
                        }
                    }
                }
            }
        }
        return validMove;
    }

    public boolean isFinished(){
        return isFinished;
    }
}
