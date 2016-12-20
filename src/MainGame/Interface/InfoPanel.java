package MainGame.Interface;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {

    private int boardSize;
    private GridBagConstraints c= new GridBagConstraints();

    public InfoPanel(int boardSize) {
        this.boardSize = boardSize;
        this.setLayout(new GridBagLayout());
        buildLabels();
        this.setFocusable(false);

    }

    private JLabel gameOver;
    private JLabel arrows;
    private JLabel treasureFound;
    private JLabel hitOrMiss;
    private JTextArea clues;

    ////////////////////////Set the labels based on players current attributes \\\\\\\\\\\\\\\\\\\\\\
    public JLabel getGameOver() {
        return gameOver;
    }

    public JLabel getArrows() {
        return arrows;
    }

    public JLabel getTreasureFound() {
        return treasureFound;
    }

    public JLabel getHitOrMiss() {
        return hitOrMiss;
    }

    public JTextArea getClues() {
        return clues;
    }


    private void buildLabels(){
        //Using a grid bay layout add the labels and text area to panel
        c.fill = GridBagConstraints.HORIZONTAL;

        gameOver  = new JLabel("Playing...");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 1;
        this.add(gameOver, c);

        arrows  = new JLabel("Arrows Left:");
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        this.add(arrows, c);

        treasureFound = new JLabel("Treasure not found");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        this.add(treasureFound, c);

        hitOrMiss  = new JLabel("");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 4;
        this.add(hitOrMiss, c);


        clues = new JTextArea();
        clues.setEditable(false);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
        c.ipady = 60;
        c.insets = new Insets(2,2,2,2);  //top padding
        c.gridx = 1;       //aligned with button 2
        c.gridy = 0;
        c.gridwidth = 10;   //2 columns wide
        c.gridheight=5;
        this.add(clues, c);

    }


}
