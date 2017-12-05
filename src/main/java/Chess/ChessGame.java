package Chess;

import Chess.ChessPiece.*;
import Console.BoardDisplay;
import java.util.ArrayList;

public class ChessGame {

    private final ChessBoard board;
    private boolean isFinished;
    private PieceColor currentPlayer;

    public ChessGame(){
        board = new ChessBoard();
        currentPlayer = PieceColor.White;
        isFinished = false;

        BoardDisplay.clearConsole();
        BoardDisplay.printBoard(board);
    }

    /**
     * @return returns true if move was played, false if move was illegal
     */
    public boolean playMove(Tuple from, Tuple to){
        if(isValidMove(from, to, false)) {
            Tile fromTile = board.getBoardArray()[from.Y()][from.X()];
            ChessPiece pieceToMove = fromTile.getPiece();

            Tile toTile = board.getBoardArray()[to.Y()][to.X()];
            toTile.setPiece(pieceToMove);

            fromTile.empty();
            endTurn();
            BoardDisplay.printBoard(board);
            return true;
        } else {
            System.out.println("Invalid move!");
            return false;
        }
    }

    //Function that check if any non-king piece can prevent check for given color
    private boolean canAnyPiecePreventCheck(PieceColor color){
        boolean canPreventCheck = false;
        Tuple[] locations = board.getAllPiecesLocationForColor(color);
        for(Tuple location : locations){
            Tile fromTile = board.getTileFromTuple(location);
            ChessPiece piece = fromTile.getPiece();
            if(piece.pieceType() == PieceType.King) continue;
            Move[] possibleMoves = allPossibleMovesForPiece(piece, location);

            for(Move move : possibleMoves){
                int newX = location.X() + move.x;
                int newY = location.Y() + move.y;

                Tuple newLocation = new Tuple(newX, newY);

                Tile toTile = board.getTileFromTuple(newLocation);
                ChessPiece toPiece = toTile.getPiece();

                //temporarily play the move to see if it makes us check
                toTile.setPiece(piece);
                fromTile.empty();

                //if we're no longer check
                if (!isKingCheck(color))
                    canPreventCheck = true;

                //revert temporary move
                toTile.setPiece(toPiece);
                fromTile.setPiece(piece);
                if(canPreventCheck){ // early out
                    System.out.printf("Prevented with from:" + fromTile + ", to: " + toTile);
                    return canPreventCheck;
                }
            }
        }
        return canPreventCheck;
    }

    private boolean isColorCheckMate(PieceColor color){
        if(!isKingCheck(color)) return false;//if not check, then we're not mate
        Tuple kingLocation = board.getKingLocation(color);

        ChessPiece king = board.getTileFromTuple(kingLocation).getPiece();
        Move[] possibleMoves = allPossibleMovesForPiece(king, kingLocation);

        for (Move move : possibleMoves){
            int newX = kingLocation.X() + move.x;
            int newY = kingLocation.Y() + move.y;
            Tuple newLocation = new Tuple(newX, newY);

            //if the new location isn't check, and we can move there, return false
            if (!isLocationCheckForColor(newLocation, color)) {
                Tile tile = board.getTileFromTuple(newLocation);
                if (tile.isEmpty() || tile.getPiece().color() != color) {
                    return false;
                }
            }
        }

        // check if other pieces can circumvent check;
        return canAnyPiecePreventCheck(color);
    }

    private boolean isKingCheck(PieceColor kingColor){
        Tuple kingLocation = board.getKingLocation(kingColor);
        return isLocationCheckForColor(kingLocation, kingColor);
    }

    private boolean isLocationCheckForColor(Tuple location, PieceColor color){
        System.out.println(color.toString());
        PieceColor opponentColor = ChessPiece.opponent(color);
        Tuple[] piecesLocation = board.getAllPiecesLocationForColor(opponentColor);

        for(Tuple fromTuple: piecesLocation) {
            if(board.getTileFromTuple(fromTuple).getPiece().pieceType() == PieceType.Queen)
                System.out.printf("checking (%s, %s) to (%s, %s)\n", fromTuple.X(), fromTuple.Y(), location.X(), location.Y());

            if (isValidMove(fromTuple, location, true))
                return true;
        }

        return false;
    }

    private void endTurn(){
        if (currentPlayer == PieceColor.White) currentPlayer = PieceColor.Black;
        else currentPlayer = PieceColor.White;
    }

    /**
     * @param hypothetical if the move is hypothetical, we disregard if it sets the from player check
     * @return a boolean indicating whether the move is valid or not
     */
    private boolean isValidMove(Tuple from, Tuple to, boolean hypothetical){
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
        } else if (isValidMoveForPiece(from, to)){
            if(hypothetical)return true;//if hypothetical and valid, return true
            
            //temporarily play the move to see if it makes us check
            toTile.setPiece(fromPiece);
            fromTile.empty();
            if (isKingCheck(currentPlayer)){//check that move doesn't put oneself in check
                toTile.setPiece(toPiece);
                fromTile.setPiece(fromPiece);//revert
                return false;
            }

            //if mate, finish game
            if (isColorCheckMate(ChessPiece.opponent(currentPlayer)))
                isFinished = true;

            //revert temporary move
            toTile.setPiece(toPiece);
            fromTile.setPiece(fromPiece);

            //conditional path for legal move
            return true;
        }
        return false;
    }

    //A utility function that gets all the possible moves for a piece, with illegal ones removed.
    //NOTICE: Does not check for check when evaluating legality.
    //        This means it mostly check if it is a legal move for the piece in terms
    //        of ensuring its not taking one of its own.
    private Move[] allPossibleMovesForPiece(ChessPiece piece, Tuple currentLocation){
        Move[] moves = piece.moves();
        ArrayList<Move> possibleMoves = new ArrayList<>();

        for(Move move: moves){
            if(piece.repeatableMoves()){

            } else {
                int currentX = currentLocation.X();
                int currentY = currentLocation.Y();
                int newX = currentX + move.x;
                int newY = currentY + move.y;
                if (newX < 0 || newX > 7 || newY < 0 || newY > 7) continue;
                Tuple newLocation = new Tuple(newX,newY);
                if (isValidMoveForPiece(currentLocation, newLocation)) possibleMoves.add(move);
            }
        }
        return possibleMoves.toArray(new Move[0]);//allocate new array automatically.
    }

    private boolean isValidMoveForPiece(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        boolean repeatableMoves = fromPiece.repeatableMoves();

        if(!repeatableMoves)
            return validMoveForPieceNonRepeatable(from, to);
        else
            return validMoveForPieceRepeatable(from, to);
    }

    private boolean validMoveForPieceRepeatable(Tuple from, Tuple to) {
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        Move[] validMoves = fromPiece.moves();

        int xMove = to.X() - from.X();
        int yMove = to.Y() - from.Y();

        for(int i = 1; i <= 7; i++){//Max number of repetitions
            for(Move move : validMoves) {
                if (move.x * i == xMove && move.y * i == yMove) {//generally check for possible move
                    //if move is generally valid - check if path is valid up till i
                    for (int j = 1; j <= i; j++){
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
        return false;
    }

    private boolean validMoveForPieceNonRepeatable(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        Move[] validMoves = fromPiece.moves();

        int xMove = to.X() - from.X();
        int yMove = to.Y() - from.Y();

        //Reverse values for white, such that lowering y-values are treated as moving forward
        //this is only relevant in the case of pawns, but added for abstract movement assignment for pieces.
        if (currentPlayer == PieceColor.White && fromPiece.pieceType() == PieceType.Pawn)
            yMove = -yMove;

        for (Move move : validMoves) {
            if (move.x == xMove && move.y == yMove) {
                if (move.onTakeOnly){//if move is only legal on take (pawns)
                    Tile toTile = board.getTileFromTuple(to);
                    if (toTile.isEmpty()) break;

                    ChessPiece toPiece = toTile.getPiece();
                    return fromPiece.color() != toPiece.color();//if different color, valid move

                    //handling first move only for pawns
                } else if (move.firstMoveOnly && (fromPiece.color() == PieceColor.White && from.Y() != 6
                        || fromPiece.color() == PieceColor.Black && from.Y() != 1)) {
                    break;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFinished(){
        return isFinished;
    }
}
