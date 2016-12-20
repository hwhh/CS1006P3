package MainGame.Logic;

public class Player implements IPlayer {

    private int arrowsLeft;
    private int[] location;
    private boolean treasureFound;
    private boolean atExit;
    private boolean win;
    private boolean loose;


    public Player(int[] location) {
        this.location = location;
        this.arrowsLeft = 10;
        this.treasureFound = false;
    }

    /////////////////////////////End game check methods\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void setWin(boolean win) {
        this.win = win;
    }
    @Override
    public void setLoose(boolean loose) {
        this.loose = loose;
    }
    @Override
    public boolean isLoose() {
        return loose;
    }
    @Override
    public boolean isWin() {
        return win;
    }


    ////////////////////////Methods for finding treasure\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void setTreasureFound(boolean treasureFound) {
        this.treasureFound = treasureFound;
    }
    @Override
    public boolean isTreasureFound() {
        return treasureFound;
    }
    @Override
    public void setAtExit(boolean atExit) {
        this.atExit = atExit;
    }


    ///////////////////////////Methods for shooting\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void makeShot(int x, int y) {

    }
    @Override
    public int arrowsLeft() {
        return arrowsLeft;
    }
    @Override
    public void shoot() {
        this.arrowsLeft--;
    }
    @Override
    public int getArrowsLeft() {
        return arrowsLeft;
    }
    @Override
    public void setArrowsLeft(int arrowsLeft) {
        this.arrowsLeft = arrowsLeft;
    }


    //////////////////////////////Methods for moving\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public int[] getLocation() {
        return location;
    }
    public void setLocation(int[] location) {
        this.location = location;
    }
    @Override
    public void move(int[] to) {
        location=to;
    }
    @Override
    public void makeMove() {

    }
    @Override
    public int[] getPlayerLocation() {
        return location;
    }
    @Override
    public void setPlayerLocation(int[] location) {
        this.location=location;

    }

}
