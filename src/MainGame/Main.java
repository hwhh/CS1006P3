package MainGame;
import MainGame.AI.AIPlayer;
import MainGame.Interface.GamePanel;
import MainGame.Interface.InfoPanel;
import MainGame.Interface.StartGUI;
import MainGame.Logic.*;


import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {


    public static JFrame gameFrame;
    public static JFrame startFrame;
    public static IPlayer iPlayer;


    public static void main(String[] args) {
        start();
    }


    public static JFrame getGameFrame() {
        return gameFrame;
    }
    public static JFrame getStartFrame() {
        return startFrame;
    }

    public static void start(){
        //Create new frame
        startFrame = new JFrame("Start Game");
        //set frame properties
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StartGUI startGUI = new StartGUI();
        //Add the start panel to the frame
        startFrame.add(startGUI);
        //Display the frame
        startFrame.setVisible(true);
        startFrame.pack();
        startFrame.setResizable(false);
    }

    public static void startGame (int size, int boardSize){
        //Create Game panel and Info panel
        Game game = new Game(size, iPlayer);
        InfoPanel infoPanel = new InfoPanel(boardSize);
        GamePanel gamePanel;
        //Add the game and player to the game panel
        gamePanel = new GamePanel(size, game, iPlayer.getClass().equals(AIPlayer.class));
        //Add the info panel to the board
        Board.setInfoPanel(infoPanel);
        java.awt.EventQueue.invokeLater(() -> {//Create a new thread for the game frame
            //Create new frame
            gameFrame = new JFrame("Wumpus");
            //set frame properties
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //Create a split pane to add the game panel and info panel to the frame
            JSplitPane jsp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            jsp.setEnabled(false);
            jsp.setLeftComponent(infoPanel);
            jsp.setRightComponent(gamePanel);
            gameFrame.getContentPane().add(jsp, BorderLayout.CENTER);
            gameFrame.setSize(boardSize, boardSize + 100);
            gameFrame.setVisible(true);
            gameFrame.setResizable(false);
        });
        //Check if the player is an AI
        if (iPlayer.getClass().equals(AIPlayer.class)) {
            (new Thread(new StartAI(game,iPlayer,gamePanel))).start();//If the player is an AI create a new thread for the AI
        }


    }






}
