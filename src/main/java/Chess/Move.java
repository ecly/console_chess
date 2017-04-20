package Chess;

public class Move{
    public final int x;
    public final int y;
    public final boolean firstMoveOnly;
    public final boolean onTakeOnly;
    public Move(int x, int y, boolean firstMoveOnly, boolean onTakeOnly) {
        this. x = x;
        this. y = y;
        this.firstMoveOnly = firstMoveOnly;
        this.onTakeOnly = onTakeOnly;
    }
}
