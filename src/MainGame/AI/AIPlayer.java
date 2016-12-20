package MainGame.AI;


import MainGame.Interface.GamePanel;
import MainGame.Logic.Game;
import MainGame.Logic.IPlayer;

import java.util.ArrayList;
import java.util.List;

public class AIPlayer implements IPlayer {


    private static Game game;
    private static GamePanel gamePanel;
    private static MoveEvaluation moveEvaluation;

    private List<int[]> myAviableMoves = new ArrayList<>();

    private int arrowsLeft;
    private int[] location;
    private boolean treasureFound;
    private boolean atExit;
    private boolean win;
    private boolean loose;

    public AIPlayer(int [] location) {
        this.location = location;
        this.arrowsLeft = 10;
        this.treasureFound = false;
        moveEvaluation = new MoveEvaluation(this, location );
    }


    public static void setGame(Game game) {
        AIPlayer.game = game;
        moveEvaluation.setGame(game);
    }

    //Allow the AI to access the current game
    public static void setGamePanel(GamePanel gamePanel) {
        AIPlayer.gamePanel = gamePanel;
    }

    //////////////////////////End game check methods\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
    @Override
    public void setAtExit(boolean atExit) {
        this.atExit = atExit;
    }

    ////////////////////////Methods for shooting\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public int getArrowsLeft() {
        return arrowsLeft;
    }
    @Override
    public void setArrowsLeft(int arrowsLeft) {
        this.arrowsLeft = arrowsLeft;
    }
    @Override
    public void shoot() {
        this.arrowsLeft--;
    }
    @Override
    public int arrowsLeft() {
        return arrowsLeft;
    }
    @Override
    public void makeShot(int x, int y) {
        gamePanel.shoot(x , y);
    }

    //Get the players current location
    @Override
    public int[] getPlayerLocation() {
        return location;
    }

    //Set the players current location
    @Override
    public void setPlayerLocation(int[] location) {
        this.location=location;
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


    ////////////////////////Methods for moving\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    @Override
    public void move(int[] to) {
        this.location = to;
    }

    public void finishMove(int x, int y){
        gamePanel.movePlayer(x,y);
    }
    @Override
    public void makeMove() {
        try {
            moveEvaluation.checkCurrentLocation(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

