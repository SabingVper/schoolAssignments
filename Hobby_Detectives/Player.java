/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;


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
    private Hand cardsKnown;
    private boolean solveAttempt = false;
    private boolean activePlayer = false;
    private int moves = 0;
    private int[][] movementLog;
    private List<String> validMove;
    private boolean inRoom;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    public Player(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        cardsKnown = new Hand();
        validMove = new ArrayList<>();
        guessesMade = new ArrayList<>();
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
     * @return False if move is invalid or if the player should not have
     * access to making a move, otherwise true
     */
    // line 30 "model.ump"
    public boolean movement(Board b){
        if(exitRoom(b, null)) {return true;}

        //Check to ensure the player has a right to make moves
        if(!activePlayer || solveAttempt || (!activePlayer && solveAttempt)){
            System.out.println(name+" is not allowed to make moves.");
            return false;
        }

        //System.out.println(b);
        while(true){
            System.out.print("\nSelect a direction to move (up|down|left|right): ");

            //Gets user input
            String move = getCapitalisedUserInput();
            if(moves<=0 || validMove.size() >= 4){
                validMove.clear();
                moves = 0;
                return true;
            }

            if(move == null || move.isEmpty()){
                System.out.println("\nInvalid input. Try again.");
                continue;
            }
            //Ensures input is a valid action
            if(!(move.equals("Up")||move.equals("Left")||
                    move.equals("Down")||move.equals("Right"))){
                System.out.println("\nInvalid input. Type \'Up\', \'Down\', \'Left\', or \'Right\' to make a move.");
                continue;
            }

            //Ensures that movement is possible and position being moved to is not occupied
            boolean playerMoved = b.movePlayer(name, move, x, y, movementLog);
            /* Checks player position for room entrance and adds player to the room whilst making
            the player's guess ability available */
            if(playerMoved){
                validMove.clear();

                String room = b.getRoom(name,x,y);
                if(!room.isEmpty()) {
                    moves = 1;
                    Room r = b.getRoom(room);
                    r.addPlayer(this, b);
                    inRoom = true;
                }
                break;
            }

            if(!validMove.contains(move)){ validMove.add(move);}

            if(validMove.size() >= 4){
                System.out.println("ERROR: Cannot revisit cells. No valid moves available, you are in a deadlock state.\n" +
                        "Your turn has been terminated. Hit ENTER in the console to proceed.");
                continue;
            }
            System.out.println("\nCannot move in that direction. The \'"+move+"\' direction cannot be accessed.");
        }

        //Tracks of movements made
        movementLog[movementLog.length - moves][0] = x;
        movementLog[movementLog.length - moves][1] = y;
        moves--;

        return true;
    }

    /**
     * Performs the player's movements based on their mouse input's direction within the GUI.
     * @param b         - Board object
     * @param direction - direction for the player's movement
     */
    public void movement(Board b, String direction, GameGUI gui) {
        //Check to ensure the player has a right to make moves
        if(moves<=0 || !activePlayer || solveAttempt || (!activePlayer && solveAttempt)){
            movementLog = null;
            return;
        }

        System.out.println("Player has "+moves+" moves.");
        //if(exitRoom(b, gui)) {return;}


        //Ensures input is a valid action
        if(!(direction.equals("Up")||direction.equals("Left")||
                direction.equals("Down")||direction.equals("Right")) || direction.isEmpty() || direction == null){
            gui.showInformation("\nInvalid input. Type \'Up\', \'Down\', \'Left\', or \'Right\' to make a move.");
            return;
        }

        //Ensures that movement is possible and position being moved to is not occupied
        boolean playerMoved = b.movePlayer(name, direction, x, y, movementLog);
        if(playerMoved){
            /* Checks player position for room entrance and adds player to the room whilst making
            the player's guess ability available */
            validMove.clear();

            String room = b.getRoom(name,x,y);
            if(!room.isEmpty()) {
                moves = 1;
                Room r = b.getRoom(room);
                r.addPlayer(this, b);
                inRoom = true;
            }
        }
        else{
            if(!validMove.contains(direction)){ validMove.add(direction);}

            if(validMove.size() >= 4){
                gui.showInformation("Error: Cannot revisit cells, you are in a deadlock state.\n" +
                        "Your turn has been terminated.");
            }
            else{
                gui.showInformation("Error: Cannot move in that direction. The \'"+direction+
                        "\' direction cannot be accessed.");
            }

            return;
        }

        //Tracks of movements made
        movementLog[movementLog.length - moves][0] = x;
        movementLog[movementLog.length - moves][1] = y;
        moves--;
    }

    /**
     * Performs the action of letting the player exit the room
     * @param b - Board class
     * @return True if player is in a room and exits, False otherwise
     */
    public boolean exitRoom(Board b, GameGUI gui) {
        String room = b.getRoom(name, x, y);
        Room r;
        String move;
        if(!room.isEmpty()) {
            r = b.getRoom(room);
            String def = "";
            Map<String, Cell> exits = r.getExits();
            if(gui == null) {
                String res = "\nSelect a door to exit (";
                for (String s : exits.keySet()) {
                    if (def.isEmpty()) {
                        def = s;
                    }
                    res = res + s + "|";
                }
                res = res + "): ";
                System.out.print(res);

                move = getCapitalisedUserInput();
            }
            else{
                move = gui.showComboOptions("Select a door to exit:", exits.keySet().toArray(new String[0]));
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
            moves--;
            inRoom = false;
            return true;
        }
        return false;
    }

    /**
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
    public Hand guess(Board b, GameGUI gui) {
        if(solveAttempt){
            gui.showInformation("Error: "+name+" has already made solve attempt.\n Cannot make a guess.");
            return null;
        }
        Hand guess = performGuessAndSolve(b, gui, "Guess");
        guessesMade.add(guess);
        return guess;
    }


    /**
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
    public Hand solveAttempt(Board b, GameGUI gui) {
        if(solveAttempt){
            gui.showInformation("Error: "+name+" has already made solve attempt.\n " +
                    "Cannot make a another solve attempt.");
            return null;
        }
        Hand solveAttemptHand = performGuessAndSolve(b, gui, "Solve Attempt");
        if(solveAttemptHand==null){ return null; }

        setSolveAttempt(true);
        return solveAttemptHand;
    }


    /**
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
    private Hand performGuessAndSolve(Board b, GameGUI gui, String guessOrSolve) {
        String roomName = b.getRoom(name, x, y);
        if(roomName.isEmpty()){
            throw new Error("You are not in a room, no "+guessOrSolve+"(es/s) can be made.");
        }

        //Show the player their previous guesses before they proceed to making further guesses
        if(!guessesMade.isEmpty()){gui.showInformation("Previous Guesses: \n" + getPrevGuessString());}


        //Guess/Accusation Room - Defaulted to player's current room
        Card roomCard = getRoomCard(roomName.replace(" ", "").toUpperCase());
        if(hand.contains(roomCard)){
            //System.out.println("Your hand contains "+ roomName + " card. Cannot make a guess.");
            gui.showInformation("Your hand contains "+ roomName + " card. Cannot make a guess.");
            return null;
        }
        //System.out.println("\n\nYour "+guessOrSolve+" has been defaulted to, "+ roomName +", the current room that you are in.");
        gui.showInformation("Your "+guessOrSolve+" has been defaulted to, "+ roomName +", the current room that you are in.");

        //Guess/Accusation Character
        Card characCard = playerGuessOrAccuseCharacter(gui, guessOrSolve);

        //Guess/Accusation Weapon
        Card weaponCard = playerGuessOrAccuseWeapon(gui, guessOrSolve);

        Hand guessOrSolveHand = createAndSetHand(List.of(roomCard, characCard, weaponCard));
        gui.showInformation(this.name + " your guess is: "+guessOrSolveHand);
        return guessOrSolveHand;
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
    private Card playerGuessOrAccuseCharacter(GameGUI gui, String guessOrSolve) {
        Card characCard;
        while(true){
            /*
            System.out.println("\n\nCharacter names: Lucilla, Bert, Malina, and Percy");
            System.out.print("Guess a character from the list above: ");

            //Gets User Input
            String character = getCapitalisedUserInput();
            */

            String character = gui.showComboOptions("Select a character: ",
                    new String[]{"Lucilla", "Bert", "Malina", "Percy"});

            if(character == null){continue;}
            character = character.toUpperCase();

            //Ensures the input is a valid character name
            if(character.equals("LUCILLA")||character.equals("BERT")||
                    character.equals("MALINA")||character.equals("PERCY")){

                characCard = getCharacterCard(character);
                //Ensures the character is not within the player's own hand
                Card finalCharacCard = characCard;
                if(hand.getCards().stream().noneMatch(c->c.equals(finalCharacCard))){break;}

                //System.out.println("\nInvalid "+guessOrSolve+": Card is in your hand. Try again.");
                gui.showInformation("Invalid "+guessOrSolve+": Card is in your hand. Try again.");
                continue;
            }
            //System.out.println("\nInvalid "+guessOrSolve+": Not a valid character name. Try again.");
            gui.showInformation("Invalid "+guessOrSolve+": Not a valid character name. Try again.");
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
    private Card playerGuessOrAccuseWeapon(GameGUI gui, String guessOrSolve) {
        Card weaponCard = null;
        while(true){
            /*
            System.out.println("\n\nWeapon names: Broom, Scissors, Knife, Shovel, and iPad");
            System.out.print("Guess a weapon from the list above: ");

            //Gets user input
            String weapon = getCapitalisedUserInput();
            */

            String weapon = gui.showComboOptions("Select a weapon: ", new String[]{"Broom", "Scissors", "Knife",
                    "Shovel", "iPad"});
            if(weapon == null){continue;}
            weapon = weapon.toUpperCase();

            //Ensures the input is a valid weapon name
            if(weapon.equals("BROOM")||weapon.equals("SCISSORS")|| weapon.equals("KNIFE")||
                    weapon.equals("SHOVEL")||weapon.equals("IPAD")){

                weaponCard = getWeaponCard(weapon);
                //Ensures the weapon is not within the player's own hand
                Card finalWeaponCard = weaponCard;
                if(hand.getCards().stream().noneMatch(c->c.equals(finalWeaponCard))){break;}

                //System.out.println("\nInvalid "+guessOrSolve+": Card is in your hand. Try again.");
                gui.showInformation("Invalid "+guessOrSolve+": Card is in your hand. Try again.");
                continue;
            }
            //System.out.println("\nInvalid "+guessOrSolve+": Not a valid weapon name. Try again.");
            gui.showInformation("Invalid "+guessOrSolve+": Not a valid weapon name. Try again.");
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
    private Hand createAndSetHand(List<Card> guessOrSolveCards) {
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
    private Card getWeaponCard(String weapon) {
        return new Card(Card.Category.WEAPON, Card.Type.valueOf(weapon));
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
    private Card getCharacterCard(String character) {
        return new Card(Card.Category.CHARACTER, Card.Type.valueOf(character));
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
    private Card getRoomCard(String room) {
        return new Card(Card.Category.ROOM, Card.Type.valueOf(room));
    }


    /**
     *
     * ---------------------------------------------------------------------------------
     * HELPER METHOD: getPrevGuessString() (7/7)
     * ---------------------------------------------------------------------------------
     * Returns a String of all the player's previous guesses
     */
    // line 279 "model.ump"
    public String getPrevGuessString(){
        StringBuilder guesses = new StringBuilder();
        for(int i=0; i<guessesMade.size(); i++) {
            guesses.append("\t").append(i+1).append(". ").append(guessesMade.get(i)).append("\n");
        }
        return guesses.toString();
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
    private String capitaliseFirstLetter(String cardName) {
        if(cardName.isEmpty()||cardName == null){return null;}
        return cardName.substring(0,1).toUpperCase()+cardName.substring(1).toLowerCase();
    }
    /**
     * ---------------------------------------------------------------------------------
     * GENERAL ACTION HELPER METHOD
     * ---------------------------------------------------------------------------------
     * Gets and returns the user's input, whilst ensuring the first letter of the
     * card being referenced is capitalised
     * @return String of user input with the first letter capitalised
     */
    private String getCapitalisedUserInput() {
        String input;
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        try {
            input = capitaliseFirstLetter(br.readLine().trim());
        } catch (IOException e) {
            input = "";
        }
        return input;
    }

    //-------------------------------------------------------------------------
    //                          GETTERS  AND  SETTERS
    //-------------------------------------------------------------------------

    /**
     * @return String of the player's name
     */
    public String getName() {return name;}


    /**
     * @return True is player is in room, false if player is not in a room
     */
    public boolean isInRoom(){return inRoom;}

        //-----------------------------------------------
        //           moves GETTERS AND SETTERS
        //-----------------------------------------------

    /**
     * @return an integer indicating the number of moves the player has
     */
    public int getMoves() {return moves;}

    /**
     * Sets the number of moves a player is allowed, so long as the current moves equal to 0
     * and reinitialized the movementLog
     * @param moves - number of spaces a player is allowed to move
     */
    public void setMoves(int moves) {
        this.moves = moves;
        movementLog = new int[moves+1][2];
        movementLog[0][0] = x;
        movementLog[0][0] = y;
    }

        //-----------------------------------------------
        //           hand GETTERS AND SETTERS
        //-----------------------------------------------

    /**
     * @return Player's hand as a Hand object
     */
    public Hand getHand() {return hand;}
    /**
     * Sets the player's hand
     * @param aHand - player's allocated hand
     * @return true if successful
     */
    public boolean setHand(Hand aHand) {
        this.hand = aHand;
        cardsKnown.setCards(hand.getCards());
        return true;
    }

        //-----------------------------------------------
        //      cardsKnown GETTERS AND SETTERS
        //-----------------------------------------------
    /**
     * @return list of all guesses made by the player as a String
     */
    public Hand getCardsKnown() { return cardsKnown; }

    /**
     * Sets the list for guesses the user has already attempted to make
     * @param cardsKnown - list of hands containing the player's guessed
     */
    public void seCardsKnown(Hand cardsKnown) { this.cardsKnown = cardsKnown; }

    /**
     * Adds hand to cardsKnown List
     * @return boolean of if the hand was added successfully
     */
    public boolean addToCardsKnown(Card card){ return this.cardsKnown.addCard(card); }

    /**
     * @return a String of all the player's previous guesses
     */
    // line 279 "model.ump"
    public String getCardsKnownString(){
        StringBuilder guesses = new StringBuilder();
        for(int i=0; i<cardsKnown.getCards().size(); i++) {
            guesses.append("\t").append(i+1).append(". ").append(cardsKnown.getCard(i)).append("\n");
        }
        return guesses.toString();
    }


        //-----------------------------------------------
        //      solveAttempt GETTERS AND SETTERS
        //-----------------------------------------------
    /**
     * @return boolean of whether the player has made a solve attempt
     */
    public boolean getSolveAttempt(){ return solveAttempt; }
    /**
     * Sets whether the solve attempt has or has not been made
     * @param aSolveAttempt - boolean to set the solve attempt to
     * @return True if successful
     */
    public boolean setSolveAttempt(boolean aSolveAttempt) {
        solveAttempt = aSolveAttempt;
        return true;
    }

        //-----------------------------------------------
        //      activePlayer GETTERS AND SETTERS
        //-----------------------------------------------

    /**
     * @return boolean indicating current player's turn
     */
    public boolean getActivePlayer() {return activePlayer;}
    /**
     * Sets whether it is the current player's turn
     * @param aActivePlayer - boolean to set the activePlayer field to
     * @return True if successful
     */
    public boolean setActivePlayer(boolean aActivePlayer) {
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
    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the x coordinate only
     * @param aX - x coordinate
     */
    public void setX(int aX) {x = aX;}
    /**
     * Sets the y coordinate only
     * @param aY - y coordinate
     */
    public void setY(int aY) { y = aY; }

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