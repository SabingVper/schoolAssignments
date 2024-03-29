// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2022T2, Assignment 5
 * Name: Ryan Sturgess
 * Username: sturgeryan
 * ID: 300618020
 */

import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
import java.nio.file.*;

/** <description of class OrganisationChart>
 */

public class OrganisationChart {

    // Fields
    private Position organisation;             // the root of the current organisational chart 
    private Position pressedPosition = null;   // the position on which the mouse was pressed
    private Position selectedPosition = null;  // the selected position on which we can modify
                                               //  the attributes.
    private boolean newPosition = false;       // adding a new Position to tree
                                               //  the user entered

    // private String newRole = null;

    // constants for the layout
    public static final double NEW_LEFT = 10;  // left of the new position Icon
    public static final double NEW_TOP = 10;   // top of the new position Icon

    public static final double ICON_X = 40;    // location and size of the remove icon
    public static final double ICON_Y = 100;   
    public static final double ICON_RAD = 20;   


    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI(){
        UI.setMouseListener( this::doMouse );
        UI.addTextField("Change Role", this::setRole);
        UI.addButton("Load test tree",  this::makeTestTree); 
        UI.addButton("Load test tree from File", ()->{loadTree("test-tree.txt");});
        UI.addButton("Load Tree", ()->loadTree(UIFileChooser.open("Open File")));
        UI.addButton("Save Tree", this::saveTree);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100,500);
        UI.setDivider(0);
    }

    /**
     * initialise the root of the organisation
     */
    public void initialiseChart() {
        organisation = new Position("CEO");   // Set the root node of the organisation
        redraw();
    }
    /** If a Position has been selected, update the name of the role of this position */
    public void setRole (String v){
        if (selectedPosition != null) {
            selectedPosition.setRole(v);
        }
        redraw();
    }

    /**
     * Most of the work is initiated by the mouse.
     * 
     * The action depends on where the mouse is pressed:
     *   on the new icon,
     *   a Position in the tree, or
     * and where it is released:
     *   on the same Position,
     *   another Position in the tree,
     *   on the delete Icon, or
     *   empty space
     * 
     * See the table in the assignment description.
     * The method follows the structure of the table.
     */
    public void doMouse(String action, double x, double y){
        if (action.equals("pressed")){
            //initialise
            newPosition = false;
            pressedPosition = null;

            if (onNewIcon(x, y)) {// adding a new vacant Position to tree
                newPosition = true;
            }
            else { // acting on an existing Position
                pressedPosition = findPosition(x, y, organisation);
                if (pressedPosition != null) {
                    pressedPosition.draw(false, true);
                }
            }
        } else if (action.equals("dragged")){
            if(pressedPosition != null){
                pressedPosition.moveOffset(x);
            }
        }
        else if (action.equals("released")){
            Position targetPosition = findPosition(x, y, organisation);

            // pressed on "new" icon, released on a target
            if (newPosition && targetPosition != null){
                Position newP = new Position();
                addNewPosition(newP, targetPosition);     // Method to complete!
            }
            // pressed and released on a Position 
            else if (pressedPosition != null && targetPosition == pressedPosition) {
                // selecting a position
                selectedPosition = targetPosition;
            }
            // pressed on a Position and released on empty space
            else if (pressedPosition != null && targetPosition == null && ! onRemoveIcon(x, y)){
                // move the Position to left or right
                pressedPosition.moveOffset(x);
                }
            // pressed on a Position 
            else if (pressedPosition != null){
                if (targetPosition != null ) {
                    // moving pressed position to target
                    movePosition(pressedPosition, targetPosition);  // Method to complete!
                }
                else if (onRemoveIcon(x, y) ){
                    // removing Position from tree
                    removePosition(pressedPosition);                // Method to complete!
                }
            }
            this.redraw();
        } 
    }


    //  METHODS FOR YOU TO COMPLETE ===============================
    
    // Drawing the tree  =========================================
    /** [STEP 1:]
     *  Recursive method to draw all nodes in a subtree, given the root node.
     *  (The provided code just draws the root node;
     *  you need to make it draw all the nodes.)
     */
    private void drawTree(Position pos) {
        if (pos==selectedPosition){
            pos.drawHighlighted();
        }
        else {
            pos.draw();
        }
        //draw the nodes under pos
        if(pos.isManager()) {
            for (Position teamMemb : pos.getTeam()) {
                drawTree(teamMemb);
            }
        }

    }

    /** 
     * Find and return a Position that is currently placed over the point (x,y). 
     * Must do a recursive search of the subtree whose root is the given Position.
     * [STEP 2:] 
     *    Returns a Position if it finds one,
     *    Returns null if it doesn't.
     * [Completion:] If (x,y) is under two Positions, it should return the top one.
     */
    private Position findPosition(double x, double y, Position pos){
        Position ans = null;
        if(pos.on(x, y)) {
            return pos;
        }
           
        /* if(pos.isManager()) {
            for (Position position : pos.getTeam()) {
                ans = findPosition(x, y, position);
                if(ans != null) {
                    return ans;
                }
            }
        } */

        Stack<Position> tmpStack = new Stack<Position>(); 
        if(pos.isManager()) {
            for (Position teamMemb : pos.getTeam()) {
                tmpStack.push(findPosition(x, y, teamMemb));
            }
        }
        while (!tmpStack.isEmpty()) {
            if(tmpStack.peek() != null){
                return tmpStack.pop();
            } else{
                tmpStack.pop();
            }   
        }
        return ans;
    }

    /** [STEP 2:] 
     * Add the new position to the target's team.
     * Check the arguments are valid first.
     */
    public void addNewPosition(Position newPos, Position target){
        if((newPos == null) || (target == null)) return;
        target.addToTeam(newPos);
        this.redraw();

    }

    /** [STEP 2:] 
     *    Move a current position (pos) to another position (target)
     *    by adding the position to the team of the target,
     *    (and bringing the whole subtree of the position with them)
     *    Check the arguments are valid first.
     *
     * [COMPLETION:]
     *   Moving any position to a target that is in the
     *   position's subtree is a problem and should not be allowed. (Why?)
     *   (one consequence is that the CEO position can't be moved at all)
     */
    private void movePosition(Position pos, Position target) {
        if ((pos == null) || (target == null)){return;}   //invalid arguments.
        if(inSubtree(target, pos)) {
            return;
        }
        target.addToTeam(pos);
        this.redraw();

    }

    /** [STEP 2:]
     * Remove a position by removing it from the tree completely.
     * The position cannot be a manager of another position.
     * If this removes the current selected position, then there
     *  should now be no selected position
     */
    public void removePosition(Position pos){
        if(pos.isManager() || pos.equals(organisation)) return;
        try{
            if(selectedPosition.equals(pos)){
                selectedPosition = null;
            }
        } catch (NullPointerException E) {}
        pos.getManager().removeFromTeam(pos); 
        this.redraw();

    }

    /** [COMPLETION:]
     * Return true if position is in the subtree, and false otherwise
     * Uses == to determine node equality
     *  Check if positition is the same as the root of subTree
     *  if not, check if in any of the subtrees of the team members of the root
     *  (recursive call, which must return true if it finds the position)
     */
    private boolean inSubtree(Position pos, Position subtree) {
        if((subtree == null) || (pos == null)) return false;
        if(subtree.equals(pos)) return true;
        if(subtree.isManager()) {
            for (Position mem : subtree.getTeam()) {
                if(inSubtree(pos, mem)) return true;
            }
        }
        return false;

    }

    /* 
     * Loads tree into memory from file
     * calls a recurvse function
     */

    public void loadTree(String fileName) {
        if(!Files.exists(Path.of(fileName))) {
            UI.println("No such file exists " + fileName);
            return;
        }
        try {
            organisation = loadSubTree(new ArrayDeque<String>(Files.readAllLines(Path.of(fileName))));
            this.redraw();
        } catch (IOException e) {
            UI.println("File failed to read lines " + e);
        }
    }
    
    /* 
     * Load tree or subtree
     * Called Recurvisly 
     */

    public Position loadSubTree(Queue<String> lines) {
        Scanner line = new Scanner(lines.poll());
        int teamNum = line.nextInt();
        double offset = line.nextDouble();
        // String origin = line.next();
        String name = line.nextLine().trim();
        Position node = new Position(name, offset);
        for (int i = 0; i < teamNum; i++) {
            Position leaf = loadSubTree(lines);
            node.addToTeam(leaf);
        }
        return node;
    }

    /** 
     * Saves tree to file
     */
    public void saveTree() {
        Queue<String> lines = treeToStrings(organisation);
        try {
            String pathName = UI.askString("Output File Name: ");
            while(pathName.contains(" ")){
                UI.println("Invaild File Name (should not cotain whitespace)");
                pathName = UI.askString("Output File Name: ");
            }
            File newFile = new File(pathName + ".txt");
            while(newFile.exists()) {
                UI.println("Can't write to files that exists");
                pathName = UI.askString("Output File Name: ");
                while(pathName.contains(" ")){
                    UI.println("Invaild File Name (should not cotain whitespace)");
                    pathName = UI.askString("Output File Name: ");
                }
            }
            // From stackoverflow.com/questions/50257374
            FileWriter fWriter = new FileWriter(newFile);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            while(!lines.isEmpty()) {
                if(lines.size() == 1){
                    bWriter.write(String.format(lines.poll()));
                    break;
                }
                bWriter.write(String.format(lines.poll()+"%n"));
            }
            bWriter.close();
            UI.println("File saved");
        } catch (IOException e) {
            UI.println("File Error " + e);
            return;
        }
        
    }


    /**
     * Runs through tree and return 
     */
    public Queue<String> treeToStrings(Position pos) {
        Queue<String> lines = new ArrayDeque<String>();
        String line = "";
        line += pos.getTeam().size() + " ";
        line += pos.getOffset() + " ";
        line += pos.getRole();
        lines.add(line);
        if(pos.isManager()) {
            for (Position mem : pos.getTeam()) {
                lines.addAll(treeToStrings(mem)); 
            }
        }
        return lines;
    }

    /**
     * Redraw the entire organisation chart.
     */
    private void redraw() {
        UI.clearText();
        UI.clearGraphics();
        drawTree(organisation);
        drawNewIcon();
        drawRetireIcon();
    }

    // OTHER DRAWING METHODS =======================================
    /**
     * Redraw the new Person box
     */
    private void drawNewIcon(){
        UI.setColor(Position.BACKGROUND_COL);
        UI.fillRect(NEW_LEFT,NEW_TOP,Position.WIDTH, Position.HEIGHT);
        UI.setColor(Color.black);
        UI.drawRect(NEW_LEFT,NEW_TOP,Position.WIDTH, Position.HEIGHT);
        UI.drawString("NEW", NEW_LEFT+8, NEW_TOP+Position.HEIGHT/2-5);
        UI.drawString("POSN", NEW_LEFT+5, NEW_TOP+Position.HEIGHT/2+10);
    }

    /**
     * Redraw the remove Icon
     */
    private void drawRetireIcon(){
        UI.setColor(Color.red);
        UI.setLineWidth(5);
        UI.drawOval(ICON_X-ICON_RAD, ICON_Y-ICON_RAD, ICON_RAD*2, ICON_RAD*2);
        double off = ICON_RAD*0.68;
        UI.drawLine((ICON_X - off), (ICON_Y - off), (ICON_X + off), (ICON_Y + off));
        UI.setLineWidth(1);
        UI.setColor(Color.black);
    }

    /** is the mouse position on the New Position box */
    private boolean onNewIcon(double x, double y){
        return ((x >= NEW_LEFT) && (x <= NEW_LEFT + Position.WIDTH) &&
            (y >= NEW_TOP) && (y <= NEW_TOP + Position.HEIGHT));
    }

    /** is the mouse position on the remove icon */
    private boolean onRemoveIcon(double x, double y){
        return (Math.abs(x - ICON_X) < ICON_RAD) && (Math.abs(y - ICON_Y) < ICON_RAD);
    }


    // Testing ==============================================
    /**
     * Makes an initial tree so you can test your program
     */
    private void makeTestTree(){
        organisation = new Position("CEO");
        Position aa = new Position("VP1");
        Position bb = new Position("VP2");
        Position cc = new Position("VP3");
        Position dd = new Position("VP4");
        Position a1 = new Position("AL1");
        Position a2 = new Position("AL2");
        Position b1 = new Position("AS");
        Position b2 = new Position("DPA");
        Position d1 = new Position("DBP");
        Position d2 = new Position("SEP");
        Position d3 = new Position("MSP");

        organisation.addToTeam(aa); aa.setOffset(-160);
        organisation.addToTeam(bb); bb.setOffset(-50);
        organisation.addToTeam(cc); cc.setOffset(15);
        organisation.addToTeam(dd); dd.setOffset(150);

        aa.addToTeam(a1); a1.setOffset(-35);
        aa.addToTeam(a2); a2.setOffset(25);
        bb.addToTeam(b1); b1.setOffset(-25);
        bb.addToTeam(b2); b2.setOffset(35);
        dd.addToTeam(d1); d2.setOffset(-60);
        dd.addToTeam(d2); 
        dd.addToTeam(d3); d3.setOffset(60);
        organisation.addToTeam(aa); aa.setOffset(-160);

        selectedPosition = null;
        this.redraw();
    }

    //* Test for printing out the tree structure, indented text */
    private void printTree(Position posn, String indent){
        UI.println(indent+posn+ " " +
            (posn.getManager()==null?"noM":"hasM") + " " +
            posn.getTeam().size()+" reports");
        String subIndent = indent+"  ";
        for (Position tm : posn.getTeam()){
            printTree(tm, subIndent);
        }
    }

    // Main
    public static void main(String[] arguments) {
        OrganisationChart oc = new OrganisationChart();
        oc.setupGUI();
        oc.initialiseChart();
    }
        

}
