package Chess.Pieces;

import Chess.ChessPiece;
import Chess.Move;

public class Pawn extends ChessPiece {

	public Pawn(PieceColor color){
		super(PieceType.Pawn, color, validMoves());
	}

	private static Move[] validMoves(){
		return new Move[]{new Move(0, 1, false), new Move(0, 2, false)};
	}
}
