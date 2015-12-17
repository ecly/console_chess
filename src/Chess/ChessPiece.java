package Chess;

public abstract class ChessPiece {
    private PieceType type;
    private PieceColor color;
    private Move[] moves;
    private String name;
    private char charValue;

    public ChessPiece(PieceType type, PieceColor color, Move[] moves){
        this.type = type;
        this.color = color;
        this.moves = moves;
        name = type.name();
        charValue = type.name().trim().charAt(0);
    }

    public enum PieceType{
        Pawn, Rook, Knight, Bishop, Queen, King;
    }

    public enum PieceColor {
        White, Black;
    }
    public Move[] moves(){ return moves; }

    public String name() { return name; }

    public char charValue() { return charValue; }

}
