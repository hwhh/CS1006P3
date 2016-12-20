package MainGame.AI;

import MainGame.Logic.Game;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MoveEvaluation{

    private static final int wumpusNear = -100;
    private static final int pitNear = -200;
    private static final int batNear = -50;

    Random random = new Random();



    private static List<int[]> exitLocation = new ArrayList<>();
    private static boolean foundTreausre;


    private AIPlayer aiPlayer;
    private static Game game;
    public int[] previousLocation;

    public boolean wumpusIsNear;
    public int pitIsNear;
    public int batIsNear;
    private boolean firstMove;


    private int currentMoveScore = 0;

    private List<int[]> availableLocations = new ArrayList<>();
    private List<int[]> previousAvailableLocations = new ArrayList<>();
    private List<int[]> safeLocations = new ArrayList<>();
    private List<int[]> previousLocations = new ArrayList<>();


    MoveEvaluation(AIPlayer aiPlayer, int[] previousLocation) {
        this.aiPlayer = aiPlayer;
        this.previousLocation = previousLocation;
        firstMove=true;

    }

    static void setGame(Game game) {
        MoveEvaluation.game = game;
    }


    //Not used.
    private int[] findQuickestMove(int[] currentLocation){
        int[] exit = exitLocation.get(0);
        int xLocation = exit[0];
        int yLocation = exit[1];
        int[] direction = new int[2];
        //If the treasure is found try and make the quickest way back to the exit
        if(currentLocation[0]>xLocation && currentLocation[1]>yLocation)
            direction = new int[]{-1, -1};
        else if(currentLocation[0]>xLocation && currentLocation[1]<yLocation)
            direction =new int[]{-1,1};
        else if(currentLocation[0]>xLocation && currentLocation[1]>yLocation)
            direction =new int[]{-1,-1};
        else if(currentLocation[0]<xLocation && currentLocation[1]>yLocation)
            direction =new int[]{1,-1};
        else if(currentLocation[0]<xLocation && currentLocation[1]<yLocation)
            direction =new int[]{1,1};
        else if(currentLocation[0]==xLocation && currentLocation[1]>yLocation)
            direction =new int[]{0,-1};
        else if(currentLocation[0]>xLocation && currentLocation[1]==yLocation)
            direction =new int[]{-1,0};
        else if(currentLocation[0]<xLocation && currentLocation[1]==yLocation)
            direction =new int[]{1,0};
        else if(currentLocation[0]==xLocation && currentLocation[1]<yLocation)
            direction =new int[]{0,1};
        return direction;
        //The AI now plays defensively so this may get it killed
    }



    private int check(int[] currentLocation){
        //Check the current rooms for clues
        wumpusIsNear = false;
        pitIsNear = 0;
        batIsNear = 0;
        int score = 0;
        wumpusIsNear = false;
        char s = game.getBoard().check(currentLocation);
        //Based on clues for current room assign a score to that room
        if (s != ' ') {
            if (s == 'w') {
                score+=wumpusNear;
                wumpusIsNear = true;
            }
            if (s == 'p') {
                score+=pitNear;
            }
            if (s == 'b') {
                score+=batNear;
            }
        }
        return score;
    }

    public boolean contains(List<int[]> list, int[] item){
        for (int[] ints : list) {
            if(Arrays.equals(ints,item))
                return true;
        }
        return false;
    }


    public void checkSafe(List<int[]> locations) throws InterruptedException {
        if(CollectionUtils.containsAny(locations,safeLocations) ) {
            //Exploration which is safe
            boolean roomFound = false;
            for (int[] availableLocation : locations) {
                if(!CollectionUtils.containsAll(previousLocations,availableLocations)){
                //if (previousLocations.contains(availableLocation)){
                    previousLocations.add(availableLocation);
                    roomFound = true;
                    aiPlayer.finishMove(availableLocation[0], availableLocation[1]);//make a safe move to new place
                    Thread.sleep(100);
                    break;
                }
            }
            //try and make a safe move which was not your last move
            if (!roomFound) {
                for (int[] safeLocation : safeLocations) {
                    if (contains(availableLocations,safeLocation)&& !Arrays.equals(previousLocation, safeLocation)) {
                        previousLocations.add(safeLocation);
                        aiPlayer.finishMove(safeLocation[0], safeLocation[1]);
                        Thread.sleep(100);
                        break;
                    }
                }
            }
        }
        else {
            findBestRoom(locations);
        }
    }

    public void findBestRoom (List<int[]> locations) throws InterruptedException {
        //If there are no safe rooms available
        int count = 0;
        for (int[] availableLocation : locations) {
            //go through available locations and find a location not yet visited
            if(!contains(previousLocations,availableLocation)) {
                previousLocations.add(availableLocation);
                aiPlayer.finishMove(availableLocation[0], availableLocation[1]);//Move the first available room which hasn't been visited
                Thread.sleep(100);
                break;
            }
            if(count==locations.size()-1){
                //If all available rooms have been visited make a random move
                int[] move = availableLocations.get(random.nextInt(availableLocations.size()));
                previousLocations.add(move);
                aiPlayer.finishMove(move[0], move[1]);
                Thread.sleep(100);
                break;
            }
            count++;
        }

    }




    void checkCurrentLocation(int[] currentLocation) throws InterruptedException {
        Random random = new Random();
        char s = game.getBoard().check(currentLocation);
        if (s == 't') {
            foundTreausre = true;
        }
        if (s == 'e') {
            exitLocation.add(currentLocation);
        }
        //Get all the available locations for current position and previous position
        availableLocations = game.findValidMovesForPos(currentLocation[0], currentLocation[1]);
        if (!firstMove) {
            //Safe locations are rooms where there no clues around it
            //Get all the available locations for current position and previous position
            previousAvailableLocations = game.findValidMovesForPos(previousLocation[0], previousLocation[1]);
            currentMoveScore = 0;
            //Check the number of clues for the current room, the more clues the lower the rooms score
            availableLocations.stream().filter(availableLocation -> check(availableLocation) < 0).forEach(availableLocation -> {
                currentMoveScore--;
            });
            if (currentMoveScore == 0) {//If there are no warning all sounding rooms are safe
                availableLocations.stream().filter(availableLocation -> !safeLocations.contains(availableLocation)).forEach(availableLocation -> safeLocations.add(availableLocation));
                checkSafe(availableLocations);
            }
            else {//if there are warnings decide whether better to go back or choose new random room
                currentMoveScore = 0;
                for (int[] availableLocation : availableLocations) {
                    if (check(availableLocation) > check(previousLocation))
                        currentMoveScore++;
                    else if (check(availableLocation) < check(previousLocation))
                        currentMoveScore--;
                }
                if (currentMoveScore > 0) {//new room safer than old room
                    checkSafe(availableLocations);
                }

                if (currentMoveScore < 0){//old room safer than the new room
                    aiPlayer.finishMove(previousLocation[0], previousLocation[1]);
                    Thread.sleep(100);
                    checkSafe(previousAvailableLocations);
                }
                else {//Both
                    int[] move = availableLocations.get(random.nextInt(availableLocations.size()));
                    aiPlayer.finishMove(move[0], move[1]);
                    Thread.sleep(100);
                }
            }
        } else{
            firstMove=false;
            //if the first move make a random move
            int[] availableLocation=  availableLocations.get(random.nextInt(availableLocations.size()));
            aiPlayer.finishMove(availableLocation[0], availableLocation[1]);
            Thread.sleep(100);
        }
        //Record the locations you've been too
        //Set the previous location the current location handed in
        previousLocation=currentLocation;


    }





}



