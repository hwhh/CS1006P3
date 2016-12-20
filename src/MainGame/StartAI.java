package MainGame;

import MainGame.AI.AIPlayer;
import MainGame.Interface.GamePanel;
import MainGame.Logic.Game;
import MainGame.Logic.IPlayer;

public class StartAI implements Runnable {

    private Game game;
    private IPlayer player;
    private GamePanel gamePanel;

    public StartAI(Game game, IPlayer player, GamePanel gamePanel) {
        this.game = game;
        this.player = player;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        //On the new thread set the game and game panel for the AI to access
        AIPlayer.setGame(game);
        AIPlayer.setGamePanel(gamePanel);
        while (!player.isWin() && !player.isLoose())
            player.makeMove();//Make moves until game over
    }
}
