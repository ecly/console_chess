package Chess;

/**
 * Used to store an int/int pair to map to tiles on the chessboard.
 */
public class Tuple {
    private int x;
    private int y;

    public Tuple(int x, int y){
            this.x = x;
            this.y =y;
    }

    public int X(){
        return x;
    }

    public int Y(){
        return y;
    }

}
