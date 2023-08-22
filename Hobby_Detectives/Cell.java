import java.util.*; 

// line 14 "model.ump" 

// line 122 "model.ump" 

public class Cell 

{ 



	//------------------------ 

	// MEMBER VARIABLES 

	//------------------------ 

	private Object occupant; 

	private Set<String> walls; 

	private boolean isADoor = false; 
	
	private int x;
	private int y;

	//------------------------ 

	// CONSTRUCTOR 

	//------------------------ 



	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	} 



	//------------------------ 

	// INTERFACE 

	//------------------------ 

	/**
	 * Returns the x coord of cell
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns y coord of cell
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns Occupant
	 * @return
	 */
	public Object getOccupant() {
		return occupant;
	}

	/**
	 * Returns a boolean of true or false based on weather the cell is occupied
	 * @return boolean
	 */
	public boolean isOccupied(){ 
		if(occupant == null){return false;} 
		return true; 
	} 


	/**
	 * Adds an object to be stored in the cell
	 * @param o 
	 */
	public void addOccupant(Object o){ 
		occupant = o; 
	} 


	/**
	 * Sets occupant to null or empty
	 */
	public void removeOccupant(){ 
		occupant = null; 
	} 


	/**
	 * Adds the side of the cell that has a wall unless its a door
	 * @param direction
	 */
	public void addWall(String direction){ 
		if(!isADoor){ 
			if(walls == null){ 
				walls = new HashSet<String>(); 
			} 
			walls.add(direction); 
		} 
	} 

	/**
	 * Checks that a move isnt in the direction of a wall
	 * @param direction
	 * @return boolean
	 */
	public boolean canExitThisWay(String direction){ 
		if(walls == null) {return true;}
		if(walls.contains(direction)){ 
			return false; 
		} 
		return true; 
	} 


	/**
	 * Checks that a move isnt in the direction of a wall but from the cell next to this one
	 * @param direction
	 * @return boolean
	 */
	public boolean canEnterThisWay(String direction){ 
		String dir = ""; 
		switch(direction){ 
		case "Up": 
			dir = "Down"; 
			break; 
		case "Down": 
			dir = "Up"; 
			break; 
		case "Left": 
			dir = "Right"; 
			break; 
		case "Right": 
			dir = "Left"; 
			break; 
		} 
		return canExitThisWay(dir); 
	} 


	/**
	 * Sets a boolean to tell the cell its a door
	 * @param isDoor
	 */
	public void door(boolean isDoor){ 
		isADoor = isDoor; 
	} 

	/**
	 * Returns the boolean for if its a door or not
	 * @return
	 */
	public boolean isDoor() {
		return isADoor;
	}

	/**
	 * Returns the cell in string form for printing on the board
	 */
	public String toString (){ 
		if(isADoor){ 
			return "D"; 
		} 
		else if(walls != null){ 
			return "W"; 
		} 
		else if(occupant != null){ 
			if(occupant instanceof Player) { 
				return ((Player) occupant).getName().charAt(0) + ""; 
			} 
			return occupant.toString(); 
		} 
		else{ 
			return "_"; 
		} 
	}
}
