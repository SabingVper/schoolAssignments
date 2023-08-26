// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2022T2, Assignment 6
 * Name: Ryan Sturgess
 * Username: sturgeryan
 * ID: 300618020
 */

import ecs100.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class BusNetworks {

    /** Map of towns, indexed by their names */
    private Map<String,Town> busNetwork = new HashMap<String,Town>();

    /** CORE
     * Loads a network of towns from a file.
     * Constructs a Set of Town objects in the busNetwork field
     * Each town has a name and a set of neighbouring towns
     * First line of file contains the names of all the towns.
     * Remaining lines have pairs of names of towns that are connected.
     */
    public void loadNetwork(String filename) {
        try {
            busNetwork.clear();
            UI.clearText();
            List<String> lines = Files.readAllLines(Path.of(filename));
            String firstLine = lines.remove(0);
            Scanner sc = new Scanner(firstLine);
            while(sc.hasNext()){
                String str = sc.next();
                busNetwork.put(str, new Town(str));
            }
            while(!lines.isEmpty()){
                sc = new Scanner(lines.remove(0));
                String str1 = sc.next();
                String str2 = sc.next();
                busNetwork.get(str1).addNeighbour(busNetwork.get(str2));
                busNetwork.get(str2).addNeighbour(busNetwork.get(str1));
            }
            sc.close();
            UI.println("Loaded " + busNetwork.size() + " towns:");

        } catch (IOException e) {throw new RuntimeException("Loading data.txt failed" + e);}
    }

    /**  CORE
     * Print all the towns and their neighbours:
     * Each line starts with the name of the town, followed by
     *  the names of all its immediate neighbours,
     */
    public void printNetwork() {
        UI.println("The current network: \n====================");
        List<String> pStrings = new ArrayList<String>();
        for (Town town1 : busNetwork.values()) {
            String str = town1.getName() + "-> ";
            for(Town town2 : town1.getNeighbours()){
                str += town2.getName() + " ";
            }
            pStrings.add(str);
        }
        Collections.sort(pStrings);
        for(String str : pStrings) UI.println(str);
    }

    /** COMPLETION
     * Return a set of all the nodes that are connected to the given node.
     * Traverse the network from this node in the standard way, using a
     * visited set, and then return the visited set
     */
    public Set<Town> findAllConnected(Town town) {
        Set<Town> townSet = new HashSet<Town>();
        townSet.add(town);
        for (Town town2 : town.getNeighbours()) {
            if(!townSet.contains(town2)) {
                findAllConnected(town2, townSet);
            }
        }
        /* Cheeky Way
        townSet.addAll(town.getNeighbours());
        for (Town neighbour : town.getNeighbours()) {
            townSet.addAll(neighbour.getNeighbours());
        } */

        return townSet;

    }

    /**
     * Helper Recursive method for findAllConnected
     */

    public void findAllConnected(Town town, Set<Town> townSet){
        townSet.add(town);
        for (Town town2 : town.getNeighbours()) {
            if(!townSet.contains(town2)) {
                findAllConnected(town2, townSet);
            }
        }
    }

    /**  COMPLETION
     * Print all the towns that are reachable through the network from
     * the town with the given name.
     * Note, do not include the town itself in the list.
     */
    public void printReachable(String name){
        Town town = busNetwork.get(name);
        if (town==null){
            UI.println(name+" is not a recognised town");
        }
        else {
            UI.println("\nFrom "+town.getName()+" you can get to:");
            Set<Town> neighbours = findAllConnected(town);
            neighbours.remove(town);
            List<String> pStrings = new ArrayList<String>();
            for (Town town2 : neighbours) {
                String str = town2.getName() + " ";
                pStrings.add(str);
            }
            Collections.sort(pStrings);
            String str = "";
            for (String string : pStrings) {
                str += string;
            }
            UI.print(str.trim());
        }

    }

    /**  COMPLETION
     * Print all the connected sets of towns in the busNetwork
     * Each line of the output should be the names of the towns in a connected set
     * Works through busNetwork, using findAllConnected on each town that hasn't
     * yet been printed out.
     */
    public void printConnectedGroups() {
        UI.println("Groups of Connected Towns: \n================");
        int groupNum = 1;
        Set<Town> visited = new HashSet<Town>();
        for (Town town : busNetwork.values()) {
            if(!visited.contains(town)) {
                if(groupNum ==1 && !visited.isEmpty()) groupNum++;
                Set<Town> group = findAllConnected(town);
                String string = "Group "+ (groupNum) + ": ";
                for(Town town2 : group){
                    string += town2.getName() + " ";
                }
                UI.println(string.trim());
                visited.addAll(group);
            }
        }

    }

    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI() {
        UI.addButton("Load", ()->{loadNetwork(UIFileChooser.open());});
        UI.addButton("Print Network", this::printNetwork);
        UI.addTextField("Reachable from", this::printReachable);
        UI.addButton("All Connected Groups", this::printConnectedGroups);
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100, 500);
        UI.setDivider(1.0);
        loadNetwork("data-small.txt");
    }

    // Main
    public static void main(String[] arguments) {
        BusNetworks bnw = new BusNetworks();
        bnw.setupGUI();
    }

}
