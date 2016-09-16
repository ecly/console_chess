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

    public boolean isColorCheckMate(PieceColor color){
        Tuple kingLocation = board.getKingLocation(color);
        ChessPiece king = board.getTileFromTuple(kingLocation).getPiece();
        Move[] possibleMoves = allPossibleMovesForPiece(king, kingLocation);
        boolean checkMate = true;
        for (Move move : possibleMoves){
            int newX = kingLocation.X() + move.x;
            int newY = kingLocation.Y() + move.y;
            Tuple newLocation = new Tuple(newX, newY);

            //TODO check if king can move here (eg. is own piece taking spot)

            if (!isLocationCheckForColor(newLocation, color)){
                checkMate = false;
                break;
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
        return validMove;
    }

    public boolean isFinished(){
        return isFinished;
    }
}
