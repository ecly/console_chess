package Chess;

public enum ChessPiece {
    PAWN("Pawn", new Move[]{new Move(0, 1, false), new Move(0, 2, false)}),

    ROOK("Rook", new Move[]{new Move(1, 0, true), new Move(0, 1, true),
                            new Move(-1, 0, true), new Move(0, -1, true)}),

    KNIGHT("Knight", new Move[]{new Move(2, 1, false), new Move(1, 2, false),
                                new Move(2, -1, false), new Move(-1, 2, false),
                                new Move(2, -1, false), new Move(-1, 2, false),
                                new Move(-2, 1, false), new Move(1, -2, false),
                                new Move(-2, -1, false), new Move(-1, -2, false),
                                new Move(-2, -1, false), new Move(-1, -2, false)}),

    BISHOP("Bishop", new Move[]{new Move(1, 1, true), new Move(1, -1, true),
                                new Move(-1, 1, true), new Move(-1, -1, true)}),

    QUEEN("Queen", new Move[]{new Move(1, 0, true), new Move(0, 1, true),
                              new Move(-1, 0, true), new Move(0, -1, true),
                              new Move(1, 1, true), new Move(1, -1, true),
                              new Move(-1, 1, true), new Move(-1, -1, true)}),

    KING("King", new Move[]{new Move(1, 0, false), new Move(0, 1, false),
                            new Move(-1, 0, false), new Move(0, -1, false),
                            new Move(1, 1, false), new Move(1, -1, false),
                            new Move(-1, 1, false), new Move(-1, -1, false)});

    private final Move[] validMoves;
    private final String pieceName;
    private char charValue;

    ChessPiece(String pieceName, Move[] validMoves){
        this.pieceName = pieceName;
        this.validMoves = validMoves;
        charValue = pieceName.trim().toCharArray()[0];
    }

    Move[] validMoves(){ return validMoves; }

    String pieceName() { return pieceName; }

    char charValue() { return charValue; }

}
