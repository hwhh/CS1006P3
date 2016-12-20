package MainGame.Logic;

public class Move {

    public int[] location;
    int x;
    int y;

    public Move(int x, int y) {
        location = new int[]{x,y};
        this.x = x;
        this.y = y;
    }
}
