import java.util.*; 

import java.lang.Character; 

// line 8 "model.ump" 

// line 116 "model.ump" 

public class Board 

{ 



	//------------------------ 

	// MEMBER VARIABLES 

	//------------------------ 

	private Cell[][] cells; 

	private int size = 24; 

	private Map<String,Room> rooms; 

	private Map<String,Player> players; 

	//------------------------ 

	// CONSTRUCTOR 

	//------------------------ 

	public Map<String, Room> getRooms(){
        return rooms;
    }

    public Map<String, Player> getPlayersMap(){
		return players;
    }

	public Board() 
	{ 
		cells = new Cell[size][size]; 
		rooms = new HashMap<String,Room>(); 
		players = new HashMap<String,Player>(); 
		buildBoard(); 
	} 



	/**
	 * x coord is J, y coord is I. The board builds left to right. The cells array is cells[x][y]. 
	 */ 
	public void buildBoard(){ 
		char[] layout = charGrid(); 
		buildRooms(); 
		int count = 0; 
		for(int i = 0; i < size; i++){ 
			for(int j = 0; j < size; j++){ 
				Cell c = new Cell(j,i); 
				if(layout[count] == 'H' || layout[count] == 'h'){ 
					rooms.get("Haunted House").addCell(c,j,i); 
				} 
				else if(layout[count] == 'M' || layout[count] == 'm'){  
					rooms.get("Manic Manor").addCell(c,j,i); 
				} 
				else if(layout[count] == 'V' || layout[count] == 'v'){  
					rooms.get("Visitation Villa").addCell(c,j,i); 
				} 
				else if(layout[count] == 'C' || layout[count] == 'c'){  
					rooms.get("Calamity Castle").addCell(c,j,i); 
				} 
				else if(layout[count] == 'P' || layout[count] == 'p'){  
					rooms.get("Peril Palace").addCell(c,j,i); 
				} 
				else if(layout[count] == 'S'){  
					c.addOccupant("_"); 
					c.addWall("Up"); 
				} 
				if(Character.isDigit(layout[count])){ 
					String person = ""; 
					switch(layout[count]){ 
					case '1': 
						players.put("Lucilla",new Player("Lucilla",j,i)); 
						person = "Lucilla"; 
						break; 
					case '2': 
						players.put("Bert",new Player("Bert",j,i)); 
						person = "Bert"; 
						break; 
					case '3': 
						players.put("Malina",new Player("Malina",j,i)); 
						person = "Malina"; 
						break; 
					case '4':  
						players.put("Percy",new Player("Percy",j,i));  
						person = "Percy";  
						break;  
					}  
					c.addOccupant(players.get(person));  
				}  
				if(Character.isLowerCase(layout[count])){  
					c.door(true);  
				}  
				cells[j][i] = c;  
				count ++; 
			} 
		} 
		for(Room r : rooms.values()){ 
			r.buildWalls(cells); 
		} 
	} 

	/**
	 * Creates room objects with needed params, such as name, top left corner coords, bot right coords
	 */
	public void buildRooms(){ 
		rooms.put("Haunted House",new Room("Haunted House",2,2,6,6,1)); 
		rooms.put("Manic Manor",new Room("Manic Manor",17,2,21,6,2)); 
		rooms.put("Visitation Villa",new Room("Visitation Villa",9,10,14,13,3)); 
		rooms.put("Calamity Castle",new Room("Calamity Castle",2,17,6,21,4));  
		rooms.put("Peril Palace",new Room("Peril Palace",17,17,21,21,5));  
	} 

	/**
	 * Takes the preset board layout and breaks it down into a single array
	 * @return 
	 */
	public char[] charGrid(){  
		boardLayout = boardLayout.replace("|","");  
		boardLayout = boardLayout.replace("\n",""); 
		return boardLayout.toCharArray(); 
	} 

	/**
	 * checks if the move is valid by checking if the next square is occupied, not a wall, within the board limits,
	 * and the player hasnt already gone through the cell
	 * @param direction
	 * @param x
	 * @param y
	 * @param movementLog
	 * @return
	 */
	public boolean validMove(String direction, int x, int y,int[][] movementLog) {
		if(x < 0 || x > 23 || y < 0 || y > 23) {return false;}
		if(cells[x][y].isOccupied()) {return false;}
		if(!cells[x][y].canEnterThisWay(direction)) {return false;}
		for(int i = 0; i < movementLog.length; i++) {
			if(movementLog[i][0] == x && movementLog[i][1] == y) {return false;}
		}
		return true;
	}

	/**
	 * Moves the given player, checking if its a valid move
	 * @param player
	 * @param direction
	 * @param x
	 * @param y
	 * @param log
	 * @return
	 */
	public boolean movePlayer(String player, String direction, int x, int y, int[][] log){
		if(!cells[x][y].canExitThisWay(direction)) {return false;}
		switch(direction){
		case"Up":
			if(!validMove(direction,x,y-1,log)){return false;}
			cells[x][y].removeOccupant();
			cells[x][y-1].addOccupant(players.get(player));
			players.get(player).setCoords(x,y-1);
			break;
		case"Down":
			if(!validMove(direction,x,y+1,log)){return false;}
			cells[x][y].removeOccupant();
			cells[x][y+1].addOccupant(players.get(player));
			players.get(player).setCoords(x,y+1);
			break;
		case"Left":
			if(!validMove(direction,x-1,y,log)){return false;}
			cells[x][y].removeOccupant();
			cells[x-1][y].addOccupant(players.get(player));
			players.get(player).setCoords(x-1,y);
			break;
		case"Right":
			if(!validMove(direction,x+1,y,log)){return false;}
			cells[x][y].removeOccupant();
			cells[x+1][y].addOccupant(players.get(player));
			players.get(player).setCoords(x+1,y);
			break;
		}                      
		return true;
	}  

	/**
	 * Returns Room with associated string name
	 * @param name
	 * @return
	 */
	public Room getRoom(String name) {
		return rooms.get(name);
	}

	/**
	 * Returns cells of the board class
	 * @return
	 */
	public Cell[][] getCells(){
		return cells;
	}

	/**
	 * Returns the string of a room if the player is in one
	 * @param player
	 * @param x
	 * @param y
	 * @return
	 */
	public String getRoom(String player, int x, int y){ 
		for(Room r : rooms.values()){ 
			if(r.inRoom(x,y)){ 
				return r.getName(); 
			} 
		} 
		return ""; 
	} 

	/**
	 * Returns a map of player names to player objects
	 * @return Map<String, Player>
	 */
	public Map<String, Player> getPlayers(){
		return players;
	} 



	public String toString(){ 
		String res = ""; 
		for(int i = 0; i < size; i++){ 
			for(int j = 0; j < size; j++){ 
				res = res + "|" + cells[j][i].toString(); 
				if(j == size-1){res = res + "|" + "\n";} 
			} 
		} 
		res = res + "1 = Haunted House, 2 = Manic Manor, 3 = Visitation Villa,\n4 = Calamity Castle, 5 = Peril Palace, W = Wall, D = Door";
		return res; 
	} 

	private String boardLayout = """ 

			|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_| 

			|_|_|_|_|_|_|_|_|_|_|_|1|_|_|_|_|_|_|_|_|_|_|_|_| 

			|_|_|H|H|H|H|H|_|_|_|_|_|_|_|_|_|_|M|M|M|M|M|_|_| 

			|_|_|H|H|H|H|h|_|_|_|_|_|_|_|_|_|_|M|M|M|M|M|_|_| 

			|_|_|H|H|H|H|H|_|_|_|_|_|_|_|_|_|_|M|M|M|M|M|_|_| 

			|_|_|H|H|H|H|H|_|_|_|_|S|S|_|_|_|_|m|M|M|M|M|_|_| 

			|_|_|H|H|H|h|H|_|_|_|_|S|S|_|_|_|_|M|M|M|m|M|_|_| 

			|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_| 

			|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_| 

			|_|2|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_| 

			|_|_|_|_|_|_|_|_|_|V|V|V|v|V|V|_|_|_|_|_|_|_|_|_| 

			|_|_|_|_|_|S|S|_|_|V|V|V|V|V|v|_|_|S|S|_|_|_|_|_| 

			|_|_|_|_|_|S|S|_|_|v|V|V|V|V|V|_|_|S|S|_|_|_|_|_| 

			|_|_|_|_|_|_|_|_|_|V|V|v|V|V|V|_|_|_|_|_|_|_|_|_| 

			|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|4|_| 

			|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_| 

			|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_| 

			|_|_|C|c|C|C|C|_|_|_|_|S|S|_|_|_|_|P|p|P|P|P|_|_| 

			|_|_|C|C|C|C|c|_|_|_|_|S|S|_|_|_|_|P|P|P|P|P|_|_| 

			|_|_|C|C|C|C|C|_|_|_|_|_|_|_|_|_|_|P|P|P|P|P|_|_| 

			|_|_|C|C|C|C|C|_|_|_|_|_|_|_|_|_|_|p|P|P|P|P|_|_| 

			|_|_|C|C|C|C|C|_|_|_|_|_|_|_|_|_|_|P|P|P|P|P|_|_| 

			|_|_|_|_|_|_|_|_|_|3|_|_|_|_|_|_|_|_|_|_|_|_|_|_| 

			|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_|_| 

			"""; 
}
