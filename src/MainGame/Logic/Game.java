package MainGame.Logic;



import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


public class Game {

    //default directions (N, E, S, W, NE, SE, SW, NW)
    public int[][] defaultDirections = {{-1,-1,},{-1,0,},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
    private int[][] directions = defaultDirections;
    private int boardSize;
    private List<int[]> validMoves;
    private Board board;
    private IPlayer player;
    public boolean treasureNear;
    public boolean exitEncountered;


    public Game(int boardSize, IPlayer player) {
        this.boardSize = boardSize;
        this.board = new Board(boardSize, this);
        this.player = player;
        //Find the valid moves for the players current location
        findValidMovesForPos(player.getPlayerLocation()[0],player.getPlayerLocation()[1]);
        treasureNear = false;
        exitEncountered=false;

    }

    public IPlayer getPlayer() {
        return player;
    }

    public void setAvailableDirections(int[][] availableDirections ){
        this.directions = availableDirections;
    }

    public Board getBoard() {
        return board;
    }

    public  List<int[]> findValidMovesForPos (int x, int y) {
        //Loop through the directions array
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < directions.length; i++) {
            //If the players current location plus a direction is on the board add the move
            if (x + directions[i][0] <= boardSize - 1 && x + directions[i][0] >= 0 && y + directions[i][1] <= boardSize - 1 && y + directions[i][1] >= 0) {

                moves.add(new Move(x + directions[i][0], y + directions[i][1]));

            } else {
                //If the players is at an edge calculate the available positions that wrap around the board
                if (x == boardSize - 1 && y == boardSize - 1) {
                    moves.add(new Move(boardSize - 1, 0));
                    moves.add(new Move(0, boardSize - 1));
                    moves.add(new Move(boardSize - 2, 0));
                    moves.add(new Move(0, boardSize - 2));
                    moves.add(new Move(0, 0));

                } else if (x == 0 && y == 0) {
                    moves.add(new Move(boardSize - 1, 0));
                    moves.add(new Move(0, boardSize - 1));
                    moves.add(new Move(boardSize - 1, 1));
                    moves.add(new Move(1, boardSize - 1));
                    moves.add(new Move(boardSize - 1, boardSize - 1));

                } else if (x == boardSize - 1 && y == 0) {
                    moves.add(new Move(0, 0));
                    moves.add(new Move(boardSize - 1, boardSize - 1));
                    moves.add(new Move(0, 1));
                    moves.add(new Move(boardSize - 2, boardSize - 1));
                    moves.add(new Move(0, boardSize - 1));

                } else if (x == 0 && y == boardSize - 1) {
                    moves.add(new Move(0, 0));
                    moves.add(new Move(boardSize - 1, boardSize - 1));
                    moves.add(new Move(1, 0));
                    moves.add(new Move(boardSize - 1, boardSize - 2));
                    moves.add(new Move(boardSize - 1, 0));
                }
                else if (x == boardSize - 1)
                    moves.add(new Move(0, y + directions[i][1]));
                else if (x == 0)
                    moves.add(new Move(boardSize - 1, y + directions[i][1]));
                else if (y == boardSize - 1)
                    moves.add(new Move(x + directions[i][0], 0));
                else if (y == 0 )
                    moves.add(new Move(x + directions[i][0], boardSize - 1));
            }
        }

        Set<Move> set = new HashSet<>(moves);
        moves.clear();
        moves.addAll(set);

        List<int[]> result = new ArrayList<>();
        for (Move move : moves) {
            result.add(move.location);
        }

        this.validMoves = result;
        return result;
    }

    public void nullRoom(){
        //If a null room is encountered the available direction change
        List<int[]> availableDirections = new ArrayList<>();
        Random random = new Random();
        //A random of available directions are generated
        for (int i = 0; i<random.nextInt(4)+1; i++) {
            availableDirections.add(defaultDirections[random.nextInt(8)]);
        }
        //The default directions are changed to the new random directions
        int[][] matrix= new int[availableDirections.size()][];
        matrix=availableDirections.toArray(matrix);
        setAvailableDirections(matrix);
    }

    public boolean validMove(int x , int y){
        //Check if a move made by the user is valid
        int[] currentMove = {x,y};
        try {
            for (int[] validMove : validMoves) {
                if (Arrays.equals(currentMove, validMove)) {
                    return true;
                }
            }
            return false;
        }catch (Exception ignored){
            return false;
        }
    }

    public  List<int[]> getValidMoves() {
        return validMoves;
    }

    public boolean nullLocationEncountered(int[] location){
        //Check to see if the user current location is the same as null room location
        for (int[] batLocation : board.nullLocations) {
            if(Arrays.equals(batLocation, location))
                return true;
        }
        return false;
    }

    public boolean treasureEncounter(int[] location){
        //Check to see if a location is the same as null room location
        return Arrays.equals(location, board.treasureLocation);}

    public boolean wumpusEncounter(int[] location){
        //Check to see if a location is the same as the wumpus location
        return Arrays.equals(location, board.wumpusLocation);}

    public boolean exitEncountered(int[] location){
        //Check to see if a location is the same the exit room location
        return Arrays.equals(location, board.exitLocation);
    }

    public boolean pitEncounter(int[] location){
        //Check to see if a location is the same as pit room location
        for (int[] batLocation : board.pitLocations) {
            if(Arrays.equals(batLocation, location))
                return true;
        }
        return false;
    }

    public boolean batEncounter(int[] location){
        //Check to see if a location is the same as bat room location
        for (int[] batLocation : board.batLocations) {
            if(Arrays.equals(batLocation, location))
                return true;
        }
        return false;
    }





}
