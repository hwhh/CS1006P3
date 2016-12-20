package MainGame.Logic;

import MainGame.Interface.InfoPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board {

    private static InfoPanel infoPanel;
    private Game game;
    Random random = new Random();
    private  int boardSize;
    public boolean wumpusDead;
    public boolean treasureFound;

    List<int[]> locations = new ArrayList<>();
    List<int[]> arrowLocations = new ArrayList<>();
    public List<int[]> batLocations = new ArrayList<>();
    public List<int[]> pitLocations = new ArrayList<>();
    public List<int[]> nullLocations = new ArrayList<>();
    private List<String> status = new ArrayList<>();

    public int[] treasureLocation;
    public int[] wumpusLocation;
    public int[] currentLocation;
    public int[] exitLocation;


    Board(int boardSize, Game game) {
        this.boardSize = boardSize;
        this.game=game;
        //Put the items in a random locations
        wumpusLocation = getNewLocation();
        treasureLocation = getNewLocation();
        exitLocation = getNewLocation();
        //Add multiple bats to the cave
        for(int i=0; i< Math.round(boardSize); i++){
            batLocations.add(getNewLocation());
        }
        //Add multiple pits to the cave
        for(int i=0; i< Math.round(boardSize); i++){
            pitLocations.add(getNewLocation());
        }
        //Add multiple null location to the cave
        for(int i=0; i< Math.round(boardSize); i++){
            nullLocations.add(getNewLocation());
        }
        wumpusDead=false;
        treasureFound=false;
        System.out.println(treasureLocation[0] +"    "+treasureLocation[1]);
        System.out.println(exitLocation[0] +"    "+exitLocation[1]);
    }



    public int[] getNewLocation (){
        //Create a new random location which wont have anything in it
        try {
            int[] coordinates;
            do {
                coordinates = new int[]{random.nextInt(boardSize), random.nextInt(boardSize)};
            } while (locations.contains(coordinates));
            return coordinates;
        }catch (Exception e){
            return new int[]{0,0};
        }
    }

    //Allow the board to access the info panel
    public static void setInfoPanel(InfoPanel infoPanel){
        Board.infoPanel = infoPanel;
    }

    //Check a location for items
    public char check (int[] location){
        if(game.batEncounter(location)) return 'b';
        if(game.treasureEncounter(location)) return 't';
        if(game.wumpusEncounter(location)) return 'w';
        if(game.exitEncountered(location)) return 'e';
        if(game.pitEncounter(location)) return 'p';
        if(game.nullLocationEncountered(location)) return 'n';
        else return ' ';
    }

    public void shoot(int x, int y){
        //Check to see if the player can make the shot
        if (game.getPlayer().arrowsLeft()>0) {
            //Reduce users arrows
            game.getPlayer().shoot();
            //Record the destination location of the shot arrow
            arrowLocations.add(new int[]{x, y});
            //Check to see if the wumpus has been killed
            char s = check(new int[]{x, y});
            if (s != ' ') {
                status.clear();
                if (s == 'w') {
                    infoPanel.getClues().append("You killed the wumpus.\n");
                    infoPanel.getHitOrMiss().setText("Hit! Wumpus Dead");
                    infoPanel.getArrows().setText("Arrows Left: " + game.getPlayer().arrowsLeft());
                    //Take the wumpus off the board
                    wumpusLocation = new int[]{};
                    wumpusDead = true;
                }

            } else {
                //if the wumpus was missed he will move to a new location
                wumpusLocation = getNewLocation();
                infoPanel.getHitOrMiss().setText("Miss Wumpus run away");
            }
        }
            infoPanel.getArrows().setText("Arrows Left: " + game.getPlayer().arrowsLeft());
    }


    public void move (int x, int y){
        //update the players current location
        this.currentLocation = new int[]{x,y};
        //Find clues for the current room
        char s = check(currentLocation);
        //Check if at exit
        game.getPlayer().setAtExit(false);
        //Rest the default directions
        game.setAvailableDirections(game.defaultDirections);
        if(s != ' ') {
            //Check to see what items are in the current room
            infoPanel.getClues().setText("");
            if (s=='n'){
                //If null room randomly change the exit
                status.add("You have entered a null room, exits changed...\n");
                game.nullRoom();
            }
            if (s=='w') {
                //If your in room with wumpus 25% chance you live
                if(random.nextInt(100)>25) {
                    status.add("The wumpus killed you. Game over.\n");
                    infoPanel.getGameOver().setText("You loose");
                    game.getPlayer().setLoose(true);
                }else {
                    status.add("The wumpus let you live, lucky you.\n");
                    wumpusLocation=getNewLocation();
                }
            }
            if (s =='p') {
                status.add("You've fallen through a pit. Game over.\n");
                infoPanel.getGameOver().setText("You loose");
                game.getPlayer().setLoose(true);
            }
            if (s =='e') {
                game.getPlayer().setAtExit(true);
                game.exitEncountered=true;
                status.add("You've found the exit\n");
                if(game.getPlayer().isTreasureFound()) {
                    game.getPlayer().setWin(true);
                    infoPanel.getGameOver().setText("You Win!");
                }
            }
            if (s =='t') {
                infoPanel.getTreasureFound().setText("Treasure found");
                status.add("You've found the TREASURE\n");
                game.getPlayer().setTreasureFound(true);
                treasureFound=true;
                treasureLocation=new int[]{};

            }
            if (s =='b') {
                //Randomly move the player to a new safe room
                status.add("Ahh bats... You have teleported\n");
                this.currentLocation = getNewLocation();
                game.getPlayer().move(currentLocation);
            }
        }
        //Check to see if the current room contains any arrows and
        int arrowPickedUp = arrowEncounter(this.currentLocation);
        game.getPlayer().setArrowsLeft(game.getPlayer().getArrowsLeft()+arrowPickedUp);
        infoPanel.getArrows().setText("Arrows Left: " + game.getPlayer().arrowsLeft());
        game.getPlayer().move(currentLocation);
        //check to if the end game conditions
        if(!game.getPlayer().isWin() || !game.getPlayer().isLoose())
            checkValidMoves();
    }

    public void checkValidMoves (){
        //Check surrounding rooms for items in order to create clues
        for (int[] ints : game.findValidMovesForPos(currentLocation[0],currentLocation[1])) {
            char s = check(ints);
            //based on the items add correct statement
            infoPanel.getClues().setText("");
            if (s =='b')
                status.add("Bats near by...\n");
            if (s =='w')
                status.add("I smell a wumpus...\n");
            if (s =='p')
                status.add("You feel a draft\n");
            if(s == 't') {
                status.add("The treasure is near by");
                game.treasureNear = true;
            }
        }
        //Add all the clues to the text area
        for (String clues : status) {
            infoPanel.getClues().append(clues);
        }
        //remove the clues for the status array list
        status.clear();
    }

    private int arrowEncounter(int[] location){
        //If the user goes into a room with an arrow in it
        int x = 0;
        List<int[]> locations = new ArrayList<>();
        //Check all the rooms containing arrows
        for (int[] arrowLocation : arrowLocations) {
            //If there in a room containing an arrow
            if (Arrays.equals(arrowLocation, location)) {
                x++;
                locations.add(arrowLocation);
            }
        }
        //Remove picked up arrows from the arrow locations array list
        for (int[] ints : locations) {
            arrowLocations.remove(ints);
        }
        return x;
    }









}
