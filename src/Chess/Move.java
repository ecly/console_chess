package Chess;

public class Move{
    public final int x;
    public final int y;
    public final boolean firstMoveOnly;
    public Move(int x, int y, boolean firstMoveOnly) {
        this. x = x;
        this. y = y;
        this.firstMoveOnly = firstMoveOnly;
    }
}
