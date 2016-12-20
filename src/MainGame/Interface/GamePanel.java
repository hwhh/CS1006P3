package MainGame.Interface;


import MainGame.Logic.Game;
import MainGame.Main;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GamePanel extends JPanel implements ActionListener {

    public JButton squares[][];
    private int boardSize;
    private Game game;
    private int x, y;
    private boolean selected;
    private boolean gameOver;
    private boolean AI;

    public GamePanel(int boardSize, Game game, boolean AI){
        //Sets the sie of the grid
        this.boardSize=boardSize;
        //creates an array of buttons
        squares = new JButton[boardSize][boardSize];
        this.game = game;
        //change the style of the GUI in order for button clicks
        try {
            UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        //Create a grid layout so all buttons have the same size
        this.setLayout(new GridLayout(boardSize,boardSize));
        buildButtons();
        addKeyListeners();
        //Make this panel the one in focus
        this.setFocusable(true);
        gameOver=false;
        //Check if AI is playing or player is
        this.AI=AI;
    }


    //map actions to specific keys
    private void addKeyListeners (){
       //When the frame is in focus map the space bar key to a method
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "Space pressed");
        this.getActionMap().put("Space pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                spaceWasPressed(ae);
            }
        });
        //When the frame is in focus map the enter key to a method
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "Enter pressed");
        this.getActionMap().put("Enter pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                enterWasPressed(ae);
            }
        });
    }



    public void setAvailableMoves(){
        //Using the Game class find the available moves the player can make, and highlight them green
        for (int[] ints : game.getValidMoves()) {
            squares[ints[0]][ints[1]].setBackground(Color.GREEN);
            if(game.treasureNear && !game.getBoard().treasureFound){
                squares[game.getBoard().treasureLocation[0]][game.getBoard().treasureLocation[1]].setBackground(Color.orange);
            }
            if(game.getPlayer().isTreasureFound() && !game.getBoard().treasureFound){
                squares[game.getBoard().treasureLocation[0]][game.getBoard().treasureLocation[1]].setBackground(Color.green);
            }
            if(game.exitEncountered){
                squares[game.getBoard().exitLocation[0]][game.getBoard().exitLocation[1]].setBackground(Color.magenta);
            }
        }
        //Set the players current location red
        squares[game.getPlayer().getPlayerLocation()[0]][game.getPlayer().getPlayerLocation()[1]].setBackground(Color.RED);
    }

    private void clearAvailableMoves(){
        //Remove the green highlighting after th player has moved
        for (int[] ints : game.findValidMovesForPos(game.getPlayer().getPlayerLocation()[0], game.getPlayer().getPlayerLocation()[1])) {
            squares[ints[0]][ints[1]].setBackground(Color.GRAY);
        }
    }

    private void buildButtons(){
        //Create and add the buttons to the GUI
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                //Add the new buttons to the array of buttons
                squares[i][j] = new JButton();
                //Set a variables i and j to the coordinates of the current button
                int finalI = i;
                int finalJ = j;
                squares[i][j].setBackground(Color.gray);
                squares[i][j].setFocusable(false);
                //Add an action lister to the current button

                squares[i][j].addActionListener(e -> {
                    if (!gameOver && !AI){
                        //If the game isn't over and its not an AI playing allow a user to click buttons
                        x = finalI;
                        y = finalJ;
                        //If the button the user clicked on is available to be clicked on
                        if (game.validMove(finalI, finalJ)) {
                            selected = true;
                            setAvailableMoves();
                            //Clicked on square made blue
                            squares[finalI][finalJ].setBackground(Color.blue);
                        }
                    }
                });
                //Add the current button the panel
                this.add(squares[i][j]);
            }
        }
        setAvailableMoves();
    }

    //When the player chooses to move
    public void movePlayer(int x, int y) {
        //Clear highlighted squares and set the new location of the player red
        clearAvailableMoves();
        squares[x][y].setBackground(Color.red);
        squares[game.getPlayer().getPlayerLocation()[0]][game.getPlayer().getPlayerLocation()[1]].setBackground(Color.gray);
        //Send the move back to the board
        game.getBoard().move(x,y);
        //Check if the game is over
        if (game.getPlayer().isLoose() || game.getPlayer().isWin()) {
            //If the game is over ask user is they wish to restart
            int n = JOptionPane.showConfirmDialog(this, "Would you like restart?", "Game Over.", JOptionPane.YES_NO_OPTION);
            if (n==0) {
                Main.gameFrame.dispose();//Close the current form
                Main.start();
            }else
                Main.gameFrame.dispose();

        }else
            setAvailableMoves();
    }


    public void shoot(int x, int y){
        game.getBoard().shoot(x,y);//Send the shot back to the board
    }

    private void spaceWasPressed(ActionEvent e) {
        //If the space bar was pressed and a button has been selected
        if (selected) {
            movePlayer(this.x, this.y);//move the player
            selected=false;
        }
    }

    private void enterWasPressed(ActionEvent e) {
        //If the enter button was pressed and a button has been selected
        if (selected)
            shoot(this.x, this.y);
            selected=false;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
