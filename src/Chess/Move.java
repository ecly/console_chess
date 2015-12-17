package Chess;

public class Move{
    public final int x;
    public final int y;
    public final boolean isRepeatable;
    public Move(int x, int y, boolean isRepeatable){
        this. x = x;
        this. y = y;
        this.isRepeatable = isRepeatable;
    }
}
