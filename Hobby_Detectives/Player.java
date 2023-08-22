/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


// line 2 "model.ump"
// line 316 "model.ump"
public class Player
{

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    //Player Attributes
    private final String name;
    private Hand hand;
    private int x;
    private int y;
    private List<Hand> guessesMade;
    private boolean solveAttempt;
    private boolean activePlayer;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Player(String aName, int aX, int aY)
    {
        name = aName;
        x = aX;
        y = aY;
        guessesMade = new ArrayList<>();
        solveAttempt = false;
        activePlayer = false;
    }



    //------------------------
    // INTERFACE
    //------------------------


    /**
     *
     * Moves the player a given number of spaces by iterating through
     * each move with the player's direction
     *
     * Needs a check to skip player turn when trapped by its previous movements 
     *
     * @param b - Board Object
     * @param numMoves - Number of spaces the player is allowed to move
     * @return False if move is invalid or if the player should not have
     * access to making a move, otherwise true
     */
    // line 30 "model.ump"
    public boolean movement(Board b, int numMoves){
        int totalMoves = numMoves;
        String room = b.getRoom(name,x,y);
        Room r;
        String move = "";
        if(!room.isEmpty()) {
        	r = b.getRoom(room);
        	String def = "";
        	Map<String, Cell> exits = r.getExits();
        	String res = "\nSelect a door to exit (";
        	for(String s : exits.keySet()) {
        		if(def.isEmpty()) {def = s;}
        		res = res + s + "|";
        	}
        	res = res + "): ";
        	System.out.print(res);
        	move = "";
            InputStreamReader isr = new InputStreamReader(System.in);  
            BufferedReader br = new BufferedReader(isr);
            try {
                move = capitaliseFirstLetter(br.readLine().trim());
            } catch (IOException e) {
                move = "";
            }
            //Ensures input is a valid action
            if(!(exits.containsKey(move)) || move.isEmpty() || move == null){
                move = def;
            }
            r.removePlayer(this);
            Cell des = exits.get(move);
            des.addOccupant(this);
            this.setX(des.getX());
            this.setY(des.getY());
            numMoves--;
        }
        //Tracks of movements made
        int[][] movementLog = new int[numMoves+1][2];
        movementLog[0][0] = x;
        movementLog[0][1] = y;

        //Check to ensure the player has a right to make moves
        if(!activePlayer || solveAttempt || (!activePlayer && solveAttempt)){
            System.out.println(name+" is not allowed to make moves.");
            return false;
        }

        boolean inRoomGuess = false;

        while(numMoves > 0){
            System.out.println(b.toString());
            while(true){
                System.out.print("\nSelect a direction to move (up|down|left|right): ");

                //Gets user input
                move = "";
                InputStreamReader isr = new InputStreamReader(System.in);  
                BufferedReader br = new BufferedReader(isr);
                try {
                    move = capitaliseFirstLetter(br.readLine().trim());
                } catch (IOException e) {
                    move = "";
                }
                //Ensures input is a valid action
                if(!(move.equals("Up")||move.equals("Left")||
                        move.equals("Down")||move.equals("Right")) || move.isEmpty() || move == null){
                    System.out.println("\nInvalid input. Type \'Up\', \'Down\', \'Left\', or \'Right\' to make a move.");
                    continue;
                }

                //Ensures that movement is possible and position being moved to is not occupied
                boolean playerMoved = b.movePlayer(name, move, x, y, movementLog);
                if(playerMoved){
                	room = b.getRoom(name,x,y);
                	if(!room.isEmpty()) {
                		numMoves= 1;
                		r = b.getRoom(room);
                		r.addPlayer(this, b);
                        inRoomGuess = true;
                	}
                	break;
                }

                System.out.println("\nCannot move in that direction. The \'"+move+"\' direction cannot be accessed.");
            }

            //Adds new position to the movement log
            movementLog[totalMoves+1 - numMoves][0] = x;
            movementLog[totalMoves+1 - numMoves][1] = y;
            numMoves--;
        }
        if(inRoomGuess) {
            guess(b);
        }
        return true;
    }

    /**
     * TODO - Create a new movement method to work together with the GUI
     * move is > than 0
     * moves stored in the private fields
     * Check player is in a room
     * if move is allowed then decrement the movement
     * board and direction as params
     * direction used for the door
     */

    /**
     *
     * Performs the GUESS action:
     * Player guesses a murder Weapon and Character for the current
     * Room they are in that they suspect is the crime scene
     *
     * @param b - Board Object
     * @return If the player has already made a solve attempt then an ERROR is thrown.
     * If the player is in the same room as in their hand then null is returned. Otherwise,
     * a Hand containing the corresponding items the player has made an solve attempt on.
     */
    // line 84 "model.ump"
    public Hand guess(Board b){
        if(solveAttempt){throw new Error("Player has already made solve attempt");}
        Hand guess = performGuessAndSolve(b, "Guess");
        guessesMade.add(guess);
        return guess;
    }


    /**
     *
     * Performs the SOLVE ATTEMPT action:
     * Player makes an accusation on a murder weapon, character and room. The
     * corresponding hand containing these items is created and the player is marked as no
     * longer in the game.
     *
     * @param b - Board Object
     * @return If the player has already made a solve attempt then an ERROR is thrown.
     * If the player is in the same room as in their hand then null is returned. Otherwise,
     * a Hand containing the corresponding items the player has made an solve attempt on.
     */
    // line 100 "model.ump"
    public Hand solveAttempt(Board b){
        if(solveAttempt){throw new Error("Player has already made solve attempt");}
        Hand solveAttemptHand = performGuessAndSolve(b, "Solve Attempt");
        if(solveAttemptHand==null){ return null; }

        setSolveAttempt(true);
        return solveAttemptHand;
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: guess() and solveAttempt()
     * ---------------------------------------------------------------------------------
     * Performs the action of getting the user's solve or guess attempt through the use
     * of the user's input.
     * If a guess or solve attempt is not valid, then it returns null, otherwise it
     * returns a hand containing the corresponding cards to the player's guesses.
     *
     * @param b - board object
     * @param guessOrSolve - used to indicate to the user whether they are making a solve
     * or guess attempt
     * @return null if the guess or solve attempt contains invalid information or cannot
     * be performed, otherwise returns a hand of Cards derived from the user's solve
     * attempt/guess input. Throws an Error if player is not currently within a room.
     */
    // line 125 "model.ump"
    private Hand performGuessAndSolve(Board b, String guessOrSolve){
        String roomName = b.getRoom(name, x, y);
        if(roomName.isEmpty()){
            throw new Error("You are not in a room, no "+guessOrSolve+"(es/s) can be made.");
        }

        if(!guessesMade.isEmpty()){printPrevGuesses();}

        //Guess/Accusation Room - Defaulted to player's current room
        Card roomCard = getRoomCard(roomName);
        if(hand.contains(roomCard)){
            System.out.println("Your hand contains "+ roomName + "card. Cannot make a guess.");
            return null;
        }
        System.out.println("\n\nYour "+guessOrSolve+" has been defaulted to, "+ roomName +", the current room that you are in.");

        //Guess/Accusation Character
        Card characCard = playerGuessOrAccuseCharacter(guessOrSolve);

        //Guess/Accusation Weapon
        Card weaponCard = playerGuessOrAccuseWeapon(guessOrSolve);

        return createAndSetHand(List.of(roomCard, characCard, weaponCard));
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: performGuessAndSolve() (1/7)
     * ---------------------------------------------------------------------------------
     * User makes a guess or solve attempt on a character name. If it is a valid character
     * accusation/guess, then the corresponding character card is returned, otherwise the
     * it continues to loop till a valid character name is given.
     *
     * @param guessOrSolve - String representing whether this is a guess or solve attempt
     * for user output
     * @return a Card object of a Character type
     */
    // line 162 "model.ump"
    private Card playerGuessOrAccuseCharacter(String guessOrSolve){
        Card characCard = null;
        while(true){
            System.out.println("\n\nCharacter names: Lucilla, Bert, Malina, and Percy");
            System.out.print("Guess a character from the list above: ");

            //Gets user input
            String character = "";
            InputStreamReader isr = new InputStreamReader(System.in);  
            BufferedReader br = new BufferedReader(isr);
            try {
                character = capitaliseFirstLetter(br.readLine().trim());
            } catch (IOException e) {
                character = "";
            }

            //Ensures the input is a valid character name
            if(character.equals("Lucilla")||character.equals("Bert")||
                    character.equals("Malina")||character.equals("Percy") ||
                    (character != null && !character.isEmpty())){

                characCard = getCharacterCard(character);
                //Ensures the character is not within the player's own hand
                if(!hand.contains(characCard)){break;}

                System.out.println("\nInvalid "+guessOrSolve+": Card is in your hand. Try again.");
                continue;
            }
            System.out.println("\nInvalid "+guessOrSolve+": Not a valid character name. Try again.");
        }
        return characCard;
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: performGuessAndSolve() (2/7)
     * ---------------------------------------------------------------------------------
     * User makes a guess or solve attempt on a Weapon name. If it is a valid Weapon
     * accusation/guess, then the corresponding character card is returned, otherwise the
     * it continues to loop till a valid character name is given.
     *
     * @param guessOrSolve - String representing whether this is a guess or solve attempt
     * for user output
     * @return a Card object of a Weapon type
     */
    // line 200 "model.ump"
    private Card playerGuessOrAccuseWeapon(String guessOrSolve){
        Card weaponCard = null;
        while(true){
            System.out.println("\n\nWeapon names: Broom, Scissors, Knife, Shovel, and iPad");
            System.out.print("Guess a weapon from the list above: ");

            //Gets user input
            String weapon = "";
            InputStreamReader isr = new InputStreamReader(System.in);  
            BufferedReader br = new BufferedReader(isr);
            try {
                weapon = capitaliseFirstLetter(br.readLine().trim());
            } catch (IOException e) {
                weapon = "";
            }

            //Ensures the input is a valid weapon name
            if(weapon.equals("Broom")||weapon.equals("Scissors")|| weapon.equals("Knife")||
                    weapon.equals("Shovel")||weapon.equals("iPad") ||
                    (!weapon.isEmpty() && weapon != null)){

                weaponCard = getWeaponCard(weapon);
                //Ensures the weapon is not within the player's own hand
                if(!hand.contains(weaponCard)){break;}

                System.out.println("\nInvalid "+guessOrSolve+": Card is in your hand. Try again.");
                continue;
            }
            System.out.println("\nInvalid "+guessOrSolve+": Not a valid weapon name. Try again.");
        }
        return weaponCard;
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: performGuessAndSolve() (3/7)
     * ---------------------------------------------------------------------------------
     * Creates and sets hand. Then returns the newly made hand.
     *
     * @param guessOrSolveCards - List of guessed cards for the guess or solve method
     * @return a Hand Object containing the cards from the guess or solve actions
     */
    // line 235 "model.ump"
    private Hand createAndSetHand(List<Card> guessOrSolveCards){
        Hand guessOrSolveHand = new Hand();
        guessOrSolveHand.setCards(guessOrSolveCards);
        return guessOrSolveHand;
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: performGuessAndSolve() (4/7)
     * ---------------------------------------------------------------------------------
     * Returns a Card Object of the String name for the Weapon
     *
     * @param weapon - Name of weapon
     * @return Card object of the weapon
     */
    // line 248 "model.ump"
    private Card getWeaponCard(String weapon){
        return new Card(Card.Category.WEAPON, Card.Type.values()[Card.indexOfTyp(weapon)]);
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: performGuessAndSolve() (5/7)
     * ---------------------------------------------------------------------------------
     * Returns a Card Object of the String name for the Character
     *
     * @param character - Name of character
     * @return Card object of the character
     */
    // line 259 "model.ump"
    private Card getCharacterCard(String character){
        return new Card(Card.Category.CHARACTER, Card.Type.values()[Card.indexOfTyp(character)]);
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: performGuessAndSolve() (6/7)
     * ---------------------------------------------------------------------------------
     * Returns a Card Object of the String name for the room
     *
     * @param room - Name of room
     * @return Card object of the room
     */
    // line 270 "model.ump"
    private Card getRoomCard(String room){
        return new Card(Card.Category.ROOM, Card.Type.values()[Card.indexOfTyp(room)]);
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: performGuessAndSolve() (7/7)
     * ---------------------------------------------------------------------------------
     * Prints the player's previous guess attempts
     */
    // line 279 "model.ump"
    private void printPrevGuesses(){
        System.out.println("These are the previous guesses you have made: ");
        for(int i=0; i<guessesMade.size(); i++){
            System.out.println("\t" + i + ". " + guessesMade.get(i).getCards());
        }
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * GENERAL ACTION HELPER METHOD
     * ---------------------------------------------------------------------------------
     * Capitalises first letter of string
     *
     * @param cardName - String of card type name
     * @return String with first letter capitalised, or returns an empty string
     */
    // line 295 "model.ump"
    private String capitaliseFirstLetter(String cardName){
        if(cardName.isEmpty() || cardName == null){return null;}
        return cardName.substring(0,1).toUpperCase()+cardName.substring(1).toLowerCase();
    }




    //-------------------------------------------------------------------------
    //                          GETTERS  AND  SETTERS
    //-------------------------------------------------------------------------

    public String getName()
    {
        return name;
    }

        //-----------------------------------------------
        //           hand GETTERS AND SETTERS
        //-----------------------------------------------

    /**
     * @return Player's hand as a Hand object
     */
    public Hand getHand()
    {
        return hand;
    }
    /**
     * Sets the player's hand
     * @param aHand - player's allocated hand
     * @return true if successful
     */
    public boolean setHand(Hand aHand)
    {
        hand = aHand;
        return true;
    }

        //-----------------------------------------------
        //      solveAttempt GETTERS AND SETTERS
        //-----------------------------------------------
    /**
     * @return list of all guesses made by the player as a String
     */
    public List getGuessesMade()
    {
        return guessesMade;
    }

    /**
     * Sets the list for guesses the user has already attempted to make
     * @param aGuessesMade - list of hands containing the player's guessed
     */
    public void setGuessesMade(List aGuessesMade)
    {
        guessesMade = aGuessesMade;
    }


        //-----------------------------------------------
        //      solveAttempt GETTERS AND SETTERS
        //-----------------------------------------------
    /**
     * @return boolean of whether the player has made a solve attempt
     */
    public boolean getSolveAttempt()
    {
        return solveAttempt;
    }
    /**
     * Sets whether the solve attempt has or has not been made
     * @param aSolveAttempt - boolean to set the solve attempt to
     * @return True if successful
     */
    public boolean setSolveAttempt(boolean aSolveAttempt)
    {
        solveAttempt = aSolveAttempt;
        return true;
    }

        //-----------------------------------------------
        //      activePlayer GETTERS AND SETTERS
        //-----------------------------------------------

    /**
     * @return boolean indicating current player's turn
     */
    public boolean getActivePlayer()
    {
        return activePlayer;
    }
    /**
     * Sets whether it is the current player's turn
     * @param aActivePlayer - boolean to set the activePlayer field to
     * @return True if successful
     */
    public boolean setActivePlayer(boolean aActivePlayer)
    {
        activePlayer = aActivePlayer;
        return true;
    }

        // ---------------------------------------------
        //       x AND y GETTERS AND SETTERS
        // --------------------------------------------


    /**
     * Sets the x and y coords
     *
     * @param x - x coordinate
     * @param y - y coordinate
     */
    // line 306 "model.ump"
    public void setCoords(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the x coordinate only
     * @param aX - x coordinate
     */
    public void setX(int aX){x = aX;}
    /**
     * Sets the y coordinate only
     * @param aY - y coordinate
     */
    public void setY(int aY)
    {
        y = aY;
    }

    /** @return the x coordinate of the player's position */
    public int getX() {return x;}
    /** @return the y coordinate of the player's position */
    public int getY() {return y;}



    @Override
    public String toString()
    {
        return getName().charAt(0) + "";
        /*return super.toString() + "["+
                "name" + ":" + getName()+ "," +
                "x" + ":" + getX()+ "," +
                "y" + ":" + getY()+ "," +
                "solveAttempt" + ":" + getSolveAttempt()+ "," +
                "activePlayer" + ":" + getActivePlayer()+ "]" + System.getProperties().getProperty("line.separator") +
                " " + "hand" + "=" + (getHand() != null ? !getHand().equals(this) ? getHand().toString().replaceAll(" "," ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
                " " + "guessesMade" + "=" + (getGuessesMade() != null ? !getGuessesMade().equals(this) ? getGuessesMade().toString().replaceAll(" "," ") : "this" : "null");*/
    }
}