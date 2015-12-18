package Chess.Pieces;

/**
 * Used to store an int/int pair to map to tiles on the chessboard.
 */
public class Tuple {
    public int x, y;

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
