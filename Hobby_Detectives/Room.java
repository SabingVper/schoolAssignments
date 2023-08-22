import java.util.*; 

public class Room 

{ 



	//------------------------ 

	// MEMBER VARIABLES 

	//------------------------ 



	//Room Attributes 

	private String roomName; 

	private int roomNo;

	private Cell[][] roomCells; 

	private int topX; 

	private int topY; 

	private int botX; 

	private int botY; 

	private int sizeX; 

	private int sizeY; 

	private Map<String, Cell> exits;

	private Cell[] players;



	//------------------------ 

	// CONSTRUCTOR 

	//------------------------ 



	public Room(String roomName, int topX, int topY,int botX, int botY, int roomNo) 

	{ 
		this.roomName = roomName; 
		this.topX = topX; 
		this.topY = topY; 
		this.botX = botX; 
		this.botY = botY; 
		this.sizeX = botX-topX+1; 
		this.sizeY = botY-topY+1; 
		this.roomCells = new Cell[sizeX][sizeY]; 
		this.roomNo = roomNo;
		this.exits = new HashMap<String, Cell>();
		this.players = new Cell[4];
	} 

	public int getTopX(){
		return topX;
	 }
	
	 public int getTopY(){
		return topY;
	 }
	
	 public int getBottomX(){
		return botX;
	 }
	
	 public int getBottomY(){
		return botY;
	 }

	/**
	 * Adds a cell within the rooms boundry to a stored 2D array
	 * @param c Cell
	 * @param x coord
	 * @param y coord
	 */
	public void addCell(Cell c,int x, int y){ 
		roomCells[x-topX][y-topY] = c; 
	} 

	/**
	 * Adds the direction players cant move through to wall cells
	 */
	public void buildWalls(Cell[][] board){ 
		for(int i = 0; i < sizeX; i++){ 
			Cell c = roomCells[i][0];
			if(c.isDoor()) {
				exits.put("Up", board[topX+i][topY-1]);
			}
			else {
				roomCells[i][0].addWall("Up");
			}	
		} 
		for(int i = 0; i < sizeX; i++){ 
			Cell c = roomCells[i][sizeY-1];
			if(c.isDoor()) {
				exits.put("Down", board[topX+i][botY+1]);
			}
			else {
				roomCells[i][sizeY-1].addWall("Down"); 
			}

		} 
		for(int i = 0; i < sizeY; i++){ 
			Cell c = roomCells[0][i];
			if(c.isDoor()) {
				exits.put("Left", board[topX-1][topY+i]);
			}
			else {
				roomCells[0][i].addWall("Left"); 
			}
		} 
		for(int i = 0; i < sizeY; i++){ 
			Cell c = roomCells[sizeX-1][i];
			if(c.isDoor()) {
				exits.put("Right", board[botX+1][topY+i]);
			}
			else {
				roomCells[sizeX-1][i].addWall("Right"); 
			}
		} 
		if(roomNo == 3) {
			roomCells[1][1].addOccupant(roomNo);
			for(int i = 0; i < 4;i++) {
				players[i] = roomCells[i+1][2];
			}
		}
		else{
			roomCells[2][2].addOccupant(roomNo);
			for(int i = 0; i < 3; i++) {
				players[i] = roomCells[i+1][3];
			}
			players[3] = roomCells[3][2];
		}
	} 

	public void addPlayer(Player p, Board b) {
		int i = 0;
		while(i < 4) {
			if(!players[i].isOccupied()) {
				Cell[][] c = b.getCells();
				c[p.getX()][p.getY()].removeOccupant();
				players[i].addOccupant(p);
				break;
			}
			i++;
		}
	}
	
	public void removePlayer(Player p) {
		for(int i = 0; i < 4; i++) {
			if(players[i].getOccupant() != null && players[i].getOccupant().equals(p)) {
				players[i].removeOccupant();
			}
		}
	}

	/**
	 * A check to see if the given coords are within the bounds of the room
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean inRoom(int x, int y){ 
		if(x >= topX && x <= botX && y >= topY && y <= botY){ 
			return true; 
		} 
		return false; 
	} 

	/**
	 * Returns room name
	 * @return
	 */
	public String getName(){ 
		return this.roomName; 
	} 

	/**
	 * Returns a map of exits to the direction input
	 * @return
	 */
	public Map<String, Cell> getExits(){
		return exits;
	}

	public String toString() 
	{ 
		return super.toString() + "["+ "]"; 
	} 

}
