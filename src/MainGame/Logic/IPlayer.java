package MainGame.Logic;

public interface IPlayer {

    /////////////////////////////End game check methods\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    void setWin(boolean win);

    void setLoose(boolean loose);

    boolean isLoose();

    boolean isWin();


    ///////////////////////////Methods for shooting\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    int getArrowsLeft();

    void setArrowsLeft(int arrowsLeft);

    int arrowsLeft();

    void makeShot(int x, int y);

    void shoot();


    ////////////////////////Methods for finding treasure\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    void setTreasureFound(boolean treasureFound);

    boolean isTreasureFound();

    void setAtExit(boolean atExit) ;


    //////////////////////////////Methods for moving\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    int[] getPlayerLocation();

    void setPlayerLocation(int[] location);

    void move(int[] to);

    void makeMove();


}
