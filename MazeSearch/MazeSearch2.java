// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2022T2, Assignment 6
 * Name:
 * Username:
 * ID:
 */

import ecs100.UI;
import java.awt.*;
import java.util.*;
import java.util.Queue;

import javax.swing.CellEditor;

/**
 * Search for a path to the goal in a maze.
 * The maze consists of a graph of MazeCells:
 *  Each cell has a collection of neighbouring cells.
 *  Each cell can be "visited" and it will remember that it has been visited
 *  A MazeCell is Iterable, so that you can iterate through its neighbour cells with
 *    for(MazeCell neighbour : cell){....
 *
 * The maze has a goal cell (shown in green, two thirds towards the bottom right corner)
 * The maze.getGoal() method returns the goal cell of the maze.
 * The user can click on a cell, and the program will search for a path
 * from that cell to the goal.
 * 
 * Every cell that is looked at during the search is coloured  yellow, and then,
 * if the cell turns out to be on a dead end, it is coloured red.
 */

public class MazeSearch2 {

    private Maze maze;
    private String search = "first";   // "first", "all", or "shortest"
    private int pathCount = 0;
    private boolean stopNow = false;
    private Map<MazeCell, Integer> weights; // assigns weights to nodes
    private Map<Queue<MazeCell>, Integer> pathWeights; // finds shortest path by weights

    /** CORE
     * Search for a path from a cell to the goal.
     * Return true if we got to the goal via this cell (and don't
     *  search for any more paths).
     * Return false if there is not a path via this cell.
     * 
     * If the cell is the goal, then we have found a path - return true.
     * If the cell is already visited, then abandon this path - return false.
     * Otherwise,
     *  Mark the cell as visited, and colour it yellow [and pause: UI.sleep(delay);]
     *  Recursively try exploring from the cell's neighbouring cells, returning true
     *   if a neighbour leads to the goal
     *  If no neighbour leads to a goal,
     *    colour the cell red (to signal failure)
     *    abandon the path - return false.
     */
    public boolean exploreFromCell(MazeCell cell) {
        if (cell == maze.getGoal()) {
            cell.draw(Color.blue);   // to indicate finding the goal
            return true;
        }
        if(cell.isVisited()) return false;
        cell.visit();
        cell.draw(Color.yellow);
        UI.sleep(delay);
        for (MazeCell neighbour : cell) {
            if(exploreFromCell(neighbour)) {
                return true;
            }
        }
        cell.draw(Color.red);
        return false;
    }

    /** COMPLETION
     * Search for all paths from a cell,
     * If we reach the goal, then we have found a complete path,
     *  so pause for 1000 milliseconds 
     * Otherwise,
     *  visit the cell, and colour it yellow [and pause: UI.sleep(delay);]
     *  Recursively explore from the cell's neighbours, 
     *  unvisit the cell and colour it white.
     * 
     */
    public void exploreFromCellAll(MazeCell cell) {
        if (stopNow) { return; }    // exit if user clicked the stop now button
        if(cell == maze.getGoal()) {
            cell.draw(Color.blue);
            UI.sleep(1000);
            return;
        }
        if(cell.isVisited()) return;
        cell.visit();
        cell.draw(Color.yellow);
        for (MazeCell mazeCell : cell) {
            exploreFromCellAll(mazeCell);
        }
        pathCount++;
        cell.unvisit();
        cell.draw(Color.white);
    }

    /** CHALLENGE
     * Search for shortest path from a cell,
     * Use Breadth first search.
     */
    public void exploreFromCellShortest(MazeCell start) {
        weights = new HashMap<MazeCell,Integer>();
        Queue<MazeCell> queue = new ArrayDeque<MazeCell>();
        exploreFromCellShortest(start, queue);
        while(!queue.isEmpty()) {
            exploreFromCellShortest(queue.poll(), queue);
        }
        weights.replace(maze.getGoal(), 0);
        maze.reset();
        

        pathWeights = new HashMap<Queue<MazeCell>, Integer>();
        int value = 0;
        exploreFromCellShortest(start, value, new ArrayDeque<MazeCell>());
        Queue<MazeCell> hold = null;
        int min = Integer.MAX_VALUE;
        for (Map.Entry<Queue<MazeCell>,Integer> values : pathWeights.entrySet()) {
            if(values.getValue() < min) {
                hold = values.getKey();
                min = values.getValue();
            }
        }
        for (MazeCell mazeCell : hold) {
            mazeCell.draw(Color.yellow);
        }
        maze.getGoal().draw(Color.BLUE);
    }

    /**
     * This is an annoying method... it can call a StackOverflowError
     * due to the BFS style of searching.
     * The Path Queue calls it as it gets up to 9540 elements
     * I have made mutliple different verisons of this code.
     * This one right now uses weights of paths to find the goal 
      */
    public void exploreFromCellShortest(MazeCell cell, Queue<MazeCell> queue) {
        if(weights.isEmpty()){weights.put(cell, 0);}
        cell.visit();
        for (MazeCell mazeCell : cell) {
            if(!mazeCell.isVisited()) {
                queue.add(mazeCell);
                weights.put(mazeCell, weights.get(cell)+1);
            }
        }
    }
    
    public void exploreFromCellShortest(MazeCell cell, int value, Queue<MazeCell> order){
        if(cell == maze.getGoal()) {
            order.add(cell);
            Queue<MazeCell> test = new ArrayDeque<MazeCell>();
            for (MazeCell mazeCell : order) {
                test.add(mazeCell);
            }
            pathWeights.put(test, value);
            return;
        }
        cell.visit();
        order.add(cell);
        value += weights.get(cell);
        for (MazeCell mazeCell : cell) {
            if(!mazeCell.isVisited()) exploreFromCellShortest(mazeCell, value, order);
        }
        cell.unvisit();
        order.remove(cell);
    }

    


    //=================================================

    // fields for gui.
    private int delay = 20;
    private int size = 10;
    /**
     * Set up the interface
     */
    public void setupGui(){
        UI.addButton("New Maze", this::makeMaze);
        UI.addSlider("Maze Size", 4, 40,10, (double v)->{size=(int)v;});
        UI.setMouseListener(this::doMouse);
        UI.addButton("First path",    ()->{search="first";});
        UI.addButton("All paths",     ()->{search="all";});
        UI.addButton("Shortest path", ()->{search="shortest";});
        UI.addButton("Stop",          ()->{stopNow=true;});
        UI.addSlider("Speed", 1, 101,80, (double v)->{delay=(int)(100-v);});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.);
    }

    /**
     * Creates a new maze and draws it .
     */
    public void makeMaze(){
        maze = new Maze(size);
        maze.draw();
    }

    /**
     * Clicking the mouse on a cell should make the program
     * search for a path from the clicked cell to the goal.
     */
    public void doMouse(String action, double x, double y){
        if (action.equals("released")){
            maze.reset();
            maze.draw();
            pathCount = 0;
            MazeCell start = maze.getCellAt(x, y);
            if (search=="first"){
                exploreFromCell(start);
            }
            else if (search=="all"){
                stopNow=false;
                exploreFromCellAll(start);
            }
            else if (search=="shortest"){
                exploreFromCellShortest(start);
            }
        }
    }

    public static void main(String[] args) {
        MazeSearch2 ms = new MazeSearch2();
        ms.setupGui();
        ms.makeMaze();
    }
}

