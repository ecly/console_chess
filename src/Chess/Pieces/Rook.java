package Chess.Pieces;

import Chess.ChessPiece;
import Chess.Move;

public class Rook extends ChessPiece {

	public Rook(PieceColor color){
		super(PieceType.Rook, color, validMoves());
	}


	private static Move[] validMoves(){
		return new Move[]{	new Move(1, 0, true), new Move(0, 1, true),
                            new Move(-1, 0, true), new Move(0, -1, true)};
	}
}
