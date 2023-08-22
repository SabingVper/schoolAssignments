/*PLEASE DO NOT EDIT THIS CODE*/ 

/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/ 

  

  

import java.io.BufferedReader; 

import java.io.IOException; 

import java.io.InputStreamReader; 

import java.util.*; 

  

// line 2 "model.ump" 

// line 555 "model.ump" 

public class Game 

{ 

  

  //------------------------ 

  // MEMBER VARIABLES 

  //------------------------ 

  

  //Game Attributes 
  private GameGUI gui;

  private Player[] turnOrder; 

  private Board board; 

  private Hand solution; 

  private int winner; 

  private Game game; 

  private boolean gameReadySetup; 

  private boolean gameReadyExit; 

  private boolean gameReadyActive; 

  private boolean gameReadyFinished; 

  private boolean gameReadyWaiting; 

  private GameGlobalVal gameVars; 

  

  //Game State Machines 

  public enum GameState { GameWaiting, GameSetup, GameActive, GameFinished, GameExit } 

  public enum GameStateGameActive { Null, StartTurn, MakeTurn, EndTurn, EndGame } 

  public enum GameStateGameActiveMakeTurn { Null, Idle, LookHand, LookCardsKnown, MakeGuess, MakeMovement } 

  private GameState gameState; 

  private GameStateGameActive gameStateGameActive; 

  private GameStateGameActiveMakeTurn gameStateGameActiveMakeTurn; 

  

  //Helper Variables 

  private TimedEventHandler timeoutGameWaitingToGameExitHandler; 

  private TimedEventHandler timeoutStartTurnToMakeTurnHandler; 

  private TimedEventHandler timeoutEndTurnToStartTurnHandler; 

  private TimedEventHandler timeoutLookHandToIdleHandler; 

  private TimedEventHandler timeoutLookCardsKnownToIdleHandler; 

  

  //------------------------ 

  // CONSTRUCTOR 

  //------------------------ 

  

  public Game() 

  { 

    board = null; 

    solution = null; 

    winner = -1; 

    game = null; 

    gameReadySetup = false; 

    gameReadyExit = false; 

    gameReadyActive = false; 

    gameReadyFinished = false; 

    gameReadyWaiting = false; 

    gameVars = new GameGlobalVal(); 

    setGameStateGameActive(GameStateGameActive.Null); 

    setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Null); 

    setGameState(GameState.GameWaiting); 

  } 

  

  //------------------------ 

  // INTERFACE 

  //------------------------ 

  /* Code from template attribute_SetMany */ 

  

  public boolean setBoard(Board aBoard) 

  { 

    boolean wasSet = false; 

    board = aBoard; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setSolution(Hand aSolution) 

  { 

    boolean wasSet = false; 

    solution = aSolution; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setWinner(int aWinner) 

  { 

    boolean wasSet = false; 

    winner = aWinner; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setGame(Game aGame) 

  { 

    boolean wasSet = false; 

    game = aGame; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setGameReadySetup(boolean aGameReadySetup) 

  { 

    boolean wasSet = false; 

    gameReadySetup = aGameReadySetup; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setGameReadyExit(boolean aGameReadyExit) 

  { 

    boolean wasSet = false; 

    gameReadyExit = aGameReadyExit; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setGameReadyActive(boolean aGameReadyActive) 

  { 

    boolean wasSet = false; 

    gameReadyActive = aGameReadyActive; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setGameReadyFinished(boolean aGameReadyFinished) 

  { 

    boolean wasSet = false; 

    gameReadyFinished = aGameReadyFinished; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setGameReadyWaiting(boolean aGameReadyWaiting) 

  { 

    boolean wasSet = false; 

    gameReadyWaiting = aGameReadyWaiting; 

    wasSet = true; 

    return wasSet; 

  } 

  

  /* Code from template attribute_GetMany */ 

  

  public Board getBoard() 

  { 

    return board; 

  } 

  

  public Hand getSolution() 

  { 

    return solution; 

  } 

  

  public int getWinner() 

  { 

    return winner; 

  } 

  

  public Game getGame() 

  { 

    return game; 

  } 

  

  public boolean getGameReadySetup() 

  { 

    return gameReadySetup; 

  } 

  

  public boolean getGameReadyExit() 

  { 

    return gameReadyExit; 

  } 

  

  public boolean getGameReadyActive() 

  { 

    return gameReadyActive; 

  } 

  

  public boolean getGameReadyFinished() 

  { 

    return gameReadyFinished; 

  } 

  

  public boolean getGameReadyWaiting() 

  { 

    return gameReadyWaiting; 

  } 

  

  public String getGameStateFullName() 

  { 

    String answer = gameState.toString(); 

    if (gameStateGameActive != GameStateGameActive.Null) { answer += "." + gameStateGameActive.toString(); } 

    if (gameStateGameActiveMakeTurn != GameStateGameActiveMakeTurn.Null) { answer += "." + gameStateGameActiveMakeTurn.toString(); } 

    return answer; 

  } 

  

  public GameState getGameState() 

  { 

    return gameState; 

  } 

  

  public GameStateGameActive getGameStateGameActive() 

  { 

    return gameStateGameActive; 

  } 

  

  public GameStateGameActiveMakeTurn getGameStateGameActiveMakeTurn() 

  { 

    return gameStateGameActiveMakeTurn; 

  } 

  

  public boolean setupGame() 

  { 

    boolean wasEventProcessed = false; 

     

    GameState aGameState = gameState; 

    switch (aGameState) 

    { 

      case GameWaiting: 

        if (getGameReadyWaiting()) 

        { 

          exitGameState(); 

          setGameState(GameState.GameSetup); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      case GameFinished: 

        if (resetGame()) 

        { 

          setGameState(GameState.GameSetup); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean exitGame() 

  { 

    boolean wasEventProcessed = false; 

     

    GameState aGameState = gameState; 

    switch (aGameState) 

    { 

      case GameWaiting: 

        if (getGameReadyExit()) 

        { 

          exitGameState(); 

          setGameState(GameState.GameExit); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      case GameSetup: 

        if (getGameReadyExit()) 

        { 

          setGameState(GameState.GameExit); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      case GameActive: 

        if (getGameReadyExit()) 

        { 

          exitGameState(); 

          setGameState(GameState.GameExit); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      case GameFinished: 

        if (getGameReadyExit()) 

        { 

          setGameState(GameState.GameExit); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean timeoutGameWaitingToGameExit() 

  { 

    boolean wasEventProcessed = false; 

     

    GameState aGameState = gameState; 

    switch (aGameState) 

    { 

      case GameWaiting: 

        exitGameState(); 

        // line 38 "model.ump" 

        message("Timed out"); 

        setGameState(GameState.GameExit); 

        wasEventProcessed = true; 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean activeGame() 

  { 

    boolean wasEventProcessed = false; 

     

    GameState aGameState = gameState; 

    switch (aGameState) 

    { 

      case GameSetup: 

        if (getGameReadySetup()) 

        { 

          setGameState(GameState.GameActive); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean finishGame() 

  { 

    boolean wasEventProcessed = false; 

     

    GameState aGameState = gameState; 

    GameStateGameActive aGameStateGameActive = gameStateGameActive; 

    switch (aGameState) 

    { 

      case GameActive: 

        if (getGameReadyFinished()) 

        { 

          exitGameState(); 

          setGameState(GameState.GameFinished); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    switch (aGameStateGameActive) 

    { 

      case EndGame: 

        if (getGameReadyExit()) 

        { 

          exitGameState(); 

          setGameState(GameState.GameFinished); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean timeoutStartTurnToMakeTurn() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActive aGameStateGameActive = gameStateGameActive; 

    switch (aGameStateGameActive) 

    { 

      case StartTurn: 

        exitGameStateGameActive(); 

        setGameStateGameActive(GameStateGameActive.MakeTurn); 

        wasEventProcessed = true; 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean timeoutEndTurnToStartTurn() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActive aGameStateGameActive = gameStateGameActive; 

    switch (aGameStateGameActive) 

    { 

      case EndTurn: 

        exitGameStateGameActive(); 

        // line 124 "model.ump" 

         

        setGameStateGameActive(GameStateGameActive.StartTurn); 

        wasEventProcessed = true; 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean gameEnd() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActive aGameStateGameActive = gameStateGameActive; 

    switch (aGameStateGameActive) 

    { 

      case EndTurn: 

        if (getGameReadyExit()) 

        { 

          exitGameStateGameActive(); 

          setGameStateGameActive(GameStateGameActive.EndGame); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean setGameReadyExit(String str) 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActive aGameStateGameActive = gameStateGameActive; 

    switch (aGameStateGameActive) 

    { 

      case EndGame: 

        exitGameState(); 

        setGameState(GameState.GameFinished); 

        wasEventProcessed = true; 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean handLook() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActiveMakeTurn aGameStateGameActiveMakeTurn = gameStateGameActiveMakeTurn; 

    switch (aGameStateGameActiveMakeTurn) 

    { 

      case Idle: 

        if (gameVars.getCheckHand()) 

        { 

          exitGameStateGameActiveMakeTurn(); 

          setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.LookHand); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean knownCards() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActiveMakeTurn aGameStateGameActiveMakeTurn = gameStateGameActiveMakeTurn; 

    switch (aGameStateGameActiveMakeTurn) 

    { 

      case Idle: 

        if (gameVars.getCheckCardsKnown()) 

        { 

          exitGameStateGameActiveMakeTurn(); 

          setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.LookCardsKnown); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean guessMake() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActiveMakeTurn aGameStateGameActiveMakeTurn = gameStateGameActiveMakeTurn; 

    switch (aGameStateGameActiveMakeTurn) 

    { 

      case Idle: 

        if (gameVars.getMakingGuess()) 

        { 

          exitGameStateGameActiveMakeTurn(); 

          setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.MakeGuess); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean movementMake() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActiveMakeTurn aGameStateGameActiveMakeTurn = gameStateGameActiveMakeTurn; 

    switch (aGameStateGameActiveMakeTurn) 

    { 

      case Idle: 

        if (gameVars.getMakingMovement()) 

        { 

          exitGameStateGameActiveMakeTurn(); 

          setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.MakeMovement); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean timeoutLookHandToIdle() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActiveMakeTurn aGameStateGameActiveMakeTurn = gameStateGameActiveMakeTurn; 

    switch (aGameStateGameActiveMakeTurn) 

    { 

      case LookHand: 

        exitGameStateGameActiveMakeTurn(); 

        // line 84 "model.ump" 

         

        setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Idle); 

        wasEventProcessed = true; 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean timeoutLookCardsKnownToIdle() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActiveMakeTurn aGameStateGameActiveMakeTurn = gameStateGameActiveMakeTurn; 

    switch (aGameStateGameActiveMakeTurn) 

    { 

      case LookCardsKnown: 

        exitGameStateGameActiveMakeTurn(); 

        // line 92 "model.ump" 

         

        setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Idle); 

        wasEventProcessed = true; 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  public boolean turnEnd() 

  { 

    boolean wasEventProcessed = false; 

     

    GameStateGameActiveMakeTurn aGameStateGameActiveMakeTurn = gameStateGameActiveMakeTurn; 

    switch (aGameStateGameActiveMakeTurn) 

    { 

      case MakeGuess: 

        if (gameVars.getGuessMade()) 

        { 

          exitGameStateGameActive(); 

          setGameStateGameActive(GameStateGameActive.EndTurn); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      case MakeMovement: 

        if (gameVars.getMovementMade()) 

        { 

          exitGameStateGameActive(); 

          setGameStateGameActive(GameStateGameActive.EndTurn); 

          wasEventProcessed = true; 

          break; 

        } 

        break; 

      default: 

        // Other states do respond to this event 

    } 

  

    return wasEventProcessed; 

  } 

  

  private void exitGameState() 

  { 

    switch(gameState) 

    { 

      case GameWaiting: 

        stopTimeoutGameWaitingToGameExitHandler(); 

        break; 

      case GameActive: 

        exitGameStateGameActive(); 

        break; 

      case GameExit: 

        // line 160 "model.ump" 

        System.out.println("Game Ended"); 

        break; 

    } 

  } 

  

  private void setGameState(GameState aGameState) 

  { 

    gameState = aGameState; 

  

    // entry actions and do activities 

    switch(gameState) 

    { 

      case GameWaiting: 

        // line 32 "model.ump" 

        gameWaiting(); 

        startTimeoutGameWaitingToGameExitHandler(); 

        break; 

      case GameSetup: 

        // line 44 "model.ump" 

        gameSetup(); 

        break; 

      case GameActive: 

        // line 55 "model.ump"
        gameActive();

        if (gameStateGameActive == GameStateGameActive.Null) { setGameStateGameActive(GameStateGameActive.StartTurn); } 

        break; 

    } 

  } 

  

  private void exitGameStateGameActive() 

  { 

    switch(gameStateGameActive) 

    { 

      case StartTurn: 

        setGameStateGameActive(GameStateGameActive.Null); 

        stopTimeoutStartTurnToMakeTurnHandler(); 

        break; 

      case MakeTurn: 

        exitGameStateGameActiveMakeTurn(); 

        setGameStateGameActive(GameStateGameActive.Null); 

        break; 

      case EndTurn: 

        setGameStateGameActive(GameStateGameActive.Null); 

        stopTimeoutEndTurnToStartTurnHandler(); 

        break; 

      case EndGame: 

        setGameStateGameActive(GameStateGameActive.Null); 

        break; 

    } 

  } 

  

  private void setGameStateGameActive(GameStateGameActive aGameStateGameActive) 

  { 

    gameStateGameActive = aGameStateGameActive; 

    if (gameState != GameState.GameActive && aGameStateGameActive != GameStateGameActive.Null) { setGameState(GameState.GameActive); } 

  

    // entry actions and do activities 

    switch(gameStateGameActive) 

    { 

      case StartTurn: 

        // line 58 "model.ump" 

        turnStart(); 

        startTimeoutStartTurnToMakeTurnHandler(); 

        break; 

      case MakeTurn: 

        if (gameStateGameActiveMakeTurn == GameStateGameActiveMakeTurn.Null) { setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Idle); } 

        break; 

      case EndTurn: 

        // line 62 "model.ump"
        displayEnd();

        startTimeoutEndTurnToStartTurnHandler(); 

        break; 

      case EndGame: 

        // line 132 "model.ump" 

        setGameReadyFinished(true); 

        break; 

    } 

  } 

  

  private void exitGameStateGameActiveMakeTurn() 

  { 

    switch(gameStateGameActiveMakeTurn) 

    { 

      case Idle: 

        setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Null); 

        break; 

      case LookHand: 

        setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Null); 

        stopTimeoutLookHandToIdleHandler(); 

        break; 

      case LookCardsKnown: 

        setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Null); 

        stopTimeoutLookCardsKnownToIdleHandler(); 

        break; 

      case MakeGuess: 

        setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Null); 

        break; 

      case MakeMovement: 

        setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn.Null); 

        break; 

    } 

  } 

  

  private void setGameStateGameActiveMakeTurn(GameStateGameActiveMakeTurn aGameStateGameActiveMakeTurn) 

  { 

    gameStateGameActiveMakeTurn = aGameStateGameActiveMakeTurn; 

    if (gameStateGameActive != GameStateGameActive.MakeTurn && aGameStateGameActiveMakeTurn != GameStateGameActiveMakeTurn.Null) { setGameStateGameActive(GameStateGameActive.MakeTurn); } 

  

    // entry actions and do activities 

    switch(gameStateGameActiveMakeTurn) 

    { 

      case Idle: 

        // line 68 "model.ump" 

        displayIdle(); 

        break; 

      case LookHand: 

        // line 82 "model.ump" 

        displayHand(); 

        startTimeoutLookHandToIdleHandler(); 

        break; 

      case LookCardsKnown: 

        // line 90 "model.ump" 

        displayKnown(); 

        startTimeoutLookCardsKnownToIdleHandler(); 

        break; 

      case MakeGuess: 

        // line 98 "model.ump" 

        runSolutionGuess(); 

        break; 

      case MakeMovement: 

        // line 106 "model.ump" 

        runMovement(); 

        break; 

    } 

  } 

  

  private void startTimeoutGameWaitingToGameExitHandler() 

  { 

    timeoutGameWaitingToGameExitHandler = new TimedEventHandler(this,"timeoutGameWaitingToGameExit",180); 

  } 

  

  private void stopTimeoutGameWaitingToGameExitHandler() 

  { 

    timeoutGameWaitingToGameExitHandler.stop(); 

  } 

  

  private void startTimeoutStartTurnToMakeTurnHandler() 

  { 

    timeoutStartTurnToMakeTurnHandler = new TimedEventHandler(this,"timeoutStartTurnToMakeTurn",5); 

  } 

  

  private void stopTimeoutStartTurnToMakeTurnHandler() 

  { 

    timeoutStartTurnToMakeTurnHandler.stop(); 

  } 

  

  private void startTimeoutEndTurnToStartTurnHandler() 

  { 

    timeoutEndTurnToStartTurnHandler = new TimedEventHandler(this,"timeoutEndTurnToStartTurn",5); 

  } 

  

  private void stopTimeoutEndTurnToStartTurnHandler() 

  { 

    timeoutEndTurnToStartTurnHandler.stop(); 

  } 

  

  private void startTimeoutLookHandToIdleHandler() 

  { 

    timeoutLookHandToIdleHandler = new TimedEventHandler(this,"timeoutLookHandToIdle",5); 

  } 

  

  private void stopTimeoutLookHandToIdleHandler() 

  { 

    timeoutLookHandToIdleHandler.stop(); 

  } 

  

  private void startTimeoutLookCardsKnownToIdleHandler() 

  { 

    timeoutLookCardsKnownToIdleHandler = new TimedEventHandler(this,"timeoutLookCardsKnownToIdle",5); 

  } 

  

  private void stopTimeoutLookCardsKnownToIdleHandler() 

  { 

    timeoutLookCardsKnownToIdleHandler.stop(); 

  } 

  

  public static class TimedEventHandler extends TimerTask   

  { 

    private Game controller; 

    private String timeoutMethodName; 

    private double howLongInSeconds; 

    private Timer timer; 

     

    public TimedEventHandler(Game aController, String aTimeoutMethodName, double aHowLongInSeconds) 

    { 

      controller = aController; 

      timeoutMethodName = aTimeoutMethodName; 

      howLongInSeconds = aHowLongInSeconds; 

      timer = new Timer(); 

      timer.schedule(this, (long)howLongInSeconds*1000); 

    } 

     

    public void stop() 

    { 

      timer.cancel(); 

    } 

     

    public void run () 

    { 

      if ("timeoutGameWaitingToGameExit".equals(timeoutMethodName)) 

      { 

        boolean shouldRestart = !controller.timeoutGameWaitingToGameExit(); 

        if (shouldRestart) 

        { 

          controller.startTimeoutGameWaitingToGameExitHandler(); 

        } 

        return; 

      } 

      if ("timeoutStartTurnToMakeTurn".equals(timeoutMethodName)) 

      { 

        boolean shouldRestart = !controller.timeoutStartTurnToMakeTurn(); 

        if (shouldRestart) 

        { 

          controller.startTimeoutStartTurnToMakeTurnHandler(); 

        } 

        return; 

      } 

      if ("timeoutEndTurnToStartTurn".equals(timeoutMethodName)) 

      { 

        boolean shouldRestart = !controller.timeoutEndTurnToStartTurn(); 

        if (shouldRestart) 

        { 

          controller.startTimeoutEndTurnToStartTurnHandler(); 

        } 

        return; 

      } 

      if ("timeoutLookHandToIdle".equals(timeoutMethodName)) 

      { 

        boolean shouldRestart = !controller.timeoutLookHandToIdle(); 

        if (shouldRestart) 

        { 

          controller.startTimeoutLookHandToIdleHandler(); 

        } 

        return; 

      } 

      if ("timeoutLookCardsKnownToIdle".equals(timeoutMethodName)) 

      { 

        boolean shouldRestart = !controller.timeoutLookCardsKnownToIdle(); 

        if (shouldRestart) 

        { 

          controller.startTimeoutLookCardsKnownToIdleHandler(); 

        } 

        return; 

      } 

    } 

  } 

  

  public void delete() 

  {} 

  

  // line 170 "model.ump" 

   private static void message(String text){ 
    System.out.println(text); 

  } 

  

  // line 176 "model.ump" 

   private static String askMessage(String text){ 

    message(text);  

      InputStreamReader isr = new InputStreamReader(System.in);  
  

      BufferedReader br = new BufferedReader(isr);  

        try{ 

        String action = br.readLine(); 

        return action; 

    } catch(IOException e) { 

        return ""; 

    } 

       

  } 

  

  // line 190 "model.ump" 

   private void gameWaiting(){ 

    message("Game Waiting");  

  

    while(true) {  

  

      String action = askMessage("Ready to Game?? Y or N");  

  

      if(!action.toUpperCase().equals("Y") && !action.toUpperCase().equals("N")) {  

  

        continue;  

  

      }  

  

      if(action.toUpperCase().equals("Y")) {  

  

        setGameReadyWaiting(true);  

  

        break;  

  

      } else {  

  

        setGameReadyExit(true);  

  

        break;  

  

      }  

  

    } 

  } 

  

  // line 222 "model.ump" 

   private void gameSetup(){ 

    message("Game Setup");  

  

    board = new Board(); // create new board  

    board.buildBoard();

    // Check number of human players and create players with turn order  
    this.gui = new GameGUI(this);
  

    String action = "";  

  

    while(true) {  

  

      //action = askMessage("Number of human players?? 3 or 4");  
      action = this.gui.askQuestion("Number of human players?? 3 or 4");
  

      if(!action.trim().equals("3") && !action.trim().equals("4")) {  

  

        message("invalid input");  

  

        continue;  

  

      }  

      break; 

  

    }  

  

    turnOrder = createAssignPlayers(Integer.parseInt(action.trim()));  

  

    // Builds base deck  

  

    Hand hand = new Hand();  

  

    hand.buildDeck();  

  

    hand.shuffle();  

  

    // Assign Solution  

  

    solution = createSolution(hand);  

  

    // Assign Characters Cards  

  

    turnOrder = assignCards(turnOrder, hand);  

  

    // Set Game Setup Ready to true  

  

    setGameReadySetup(true); 

  } 

  

  // line 276 "model.ump" 
  /***
   * This is a method that just runs nothing.
   */
   private void gameActive(){ 
     
  } 

  

  // line 300 "model.ump" 
  /**
   * Starts the turn and stats whos turn it is
   * 
   */
   private void turnStart(){ 

    // Message start of the game  
    setPlayerActive(); 

    message(turnOrder[getPlayerActive()].getName() + ", start turn your turn.");  
    this.gui.showInformation(turnOrder[getPlayerActive()].getName() + ", start your turn.");
  

    // Game will exit after 5 seconds to active 

  } 

  

  // line 310 "model.ump" 

   private void turnActive(){ 

     

     

  } 

  /**
   * Displays the end of the game with the winner.
   */
  private void displayEnd() {
    message("\n\n\n\n");
    message(board.toString());
    message("Your turn has ended " + turnOrder[getPlayerActive()].getName() + ".\nPlease wait 5 seconds for the next players turn.");
    this.gui.showInformation("Your turn has ended " + turnOrder[getPlayerActive()].getName() + ".\nPlease wait 5 seconds for the next players turn.");
  }

  
  /**
   * Displays board and the uses choice.<br>
   * Then runs the players choice.<br>
   * It returns to itself it check hand or guesses is made.
   */
  private void displayIdle() {  

    // Ask Choice  

  

    String action = "";  

    message(board.toString());

    while(true) {  

  

      //action = askMessage("Hi " + turnOrder[getPlayerActive()].getName() + ". \n1: Check hand 2: Check Cards Known 3: Make Final Guess 4: Make Roll");  
      //action = this.gui.askQuestion("Hi " + turnOrder[getPlayerActive()].getName() + ". \n1: Check hand 2: Check Cards Known 3: Make Final Guess 4: Make Roll");
      action = this.gui.showInfoButtons("Hi " + turnOrder[getPlayerActive()].getName());

      if(!action.trim().equals("1") && !action.trim().equals("2") && !action.trim().equals("3") && !action.trim().equals("4")) {  

  

        message("Invalid input");  

  

        continue;  

  

      }  

      break; 

    }  

  

    // Run the made choice  

  

    switch (action.trim().toCharArray()[0]) {  

  

        case '1' -> {// print hand  

  

          gameVars.setCheckHand(true);  

  

          handLook();  

  

        }  

  

        case '2' -> {// print cards known  

  

          gameVars.setCheckCardsKnown(true);  

  

          knownCards();  

  

        }  

  

        case '3' -> {// Make solution guess  

  

          gameVars.setMakingGuess(true);  

  

          guessMake();  

  

        }  

  

        case '4' -> {// print hand  

  

          gameVars.setMakingMovement(true);  

  

          movementMake();  

  

        }  

  

    } 

    turnEnd();

  } 

  

  

  /** 

   * Display Hand Method 

   */ 

  // line 382 "model.ump" 
  /**
   * Prints the hand for the current players cards
   */
   private void displayHand(){ 

    // Message the players cards in hand  

  

    //message("Your Cards: " + turnOrder[getPlayerActive()].getHand().toString());  
    this.gui.showInformation("Your Cards: " + turnOrder[getPlayerActive()].getHand().toString());
  

    // State machine waits 5 seconds then exits. 

  } 

  

  

  /** 

   * Display Cards Known Method 

   */ 

  // line 396 "model.ump" 

   private void displayKnown(){ 

    // Message the players known cards  

  

    //message("Cards you know: "); //turnOrder[getPlayerActive()]   
    this.gui.showInformation("Cards you Know: ");
  

    // This will be added when cardsKnown is added to player 

  } 

  

  

  /** 

   * Solution Guess Method 

   */ 

  // line 410 "model.ump" 

   private void runSolutionGuess(){ 

    Hand tmp;// Returned hand object  
  
    try {
      tmp = turnOrder[getPlayerActive()].solveAttempt(board);  
    } catch (Exception e) {
      message("You failed to make a solve attempt. Please try again next turn.");
      gameVars.setGuessMade(true);
      return;
    }
    message("Your guess: " + tmp.toString());
    // Checks if length of hand matches and cards match\  
    if(tmp.numberOfCards() == solution.numberOfCards() && solution.containsAll(tmp)) {
      message("You have successfully guessed the solution.");

      // if true then set game to won 
      setGameReadyFinished(true);
    } else {
      message("You have unsuccessfully guessed the solution.\nYou will sit out the rest of the game.");
    }

    	 

  

    	// if false set Player's solve ability to false  

    gameVars.setGuessMade(true);
    // End Turn 

  } 

  

  

  /** 

   * Movement Method 

   */ 

  // line 430 "model.ump" 
  
   private void runMovement(){ 

    gameVars.setMovementMade(false); 

  

    Dice dice1 = new Dice();  

  

    Dice dice2 = new Dice();  

  

    // Message rolled values  

    this.gui.showInformation("You rolled a " + dice1.getValue() + " and " + dice2.getValue() + ". You Have " 
    + (dice1.getValue() + dice2.getValue()) + " moves");

    //message("You rolled a " + dice1.getValue() + " and " + dice2.getValue());  

  

    // Give board and value to player  
    String color = "";
    if(turnOrder[getPlayerActive()].toString().equals("L")){
      color = "green";
    }else if(turnOrder[getPlayerActive()].toString().equals("B")){
      color = "yellow";
    }else if(turnOrder[getPlayerActive()].toString().equals("M")){
      color = "blue";
    }else{
      color = "red";
    }

    this.gui.showInformation(turnOrder[getPlayerActive()].getName() + " your color is " + color +
    ". Please move the " + color 
    + " dot where you want to go. You can only move 1 square at a time");
    turnOrder[getPlayerActive()].movement(board, (dice1.getValue() + dice2.getValue()));  

  

    gameVars.setMovementMade(true); 
  } 

  

  // line 462 "model.ump" 
  /**
   * Assigns players with a hand that have been made up for 
   * @param players players to give cards to
   * @param hand the current deck of cards
   * @return
   */
   private Player[] assignCards(Player[] players, Hand hand){ 

    // Go through players one by one and give them the card at the top of the hand parameter  
    int count = 0;
    Hand[] hands = {new Hand(), new Hand(), new Hand(), new Hand()};
    while(hand.numberOfCards() != 0) {
      hands[count].addCard(hand.removeFromHand());
      count = (count+1 == 4) ? 0 : count +1;
    }
    for(int i = 0; i < hands.length; i++) {
      players[i].setHand(hands[i]);
    }

    return players; 

  } 

  

  // line 472 "model.ump" 
  /**
   * Creates the final solution by taking the top card of each catorgory.
   * @param deck deck should every card in it.
   * @return a Hand object with the solution
   */
  private Hand createSolution(Hand deck){

    Hand tmp = new Hand();  

    // Seperates into each category
    Card wC = deck.matches(Card.Category.WEAPON).remove(0);
    Card chC = deck.matches(Card.Category.CHARACTER).remove(0);
    Card rC = deck.matches(Card.Category.ROOM).remove(0);

    //adds card to the solution
    tmp.addCard(chC);
    tmp.addCard(wC);
    tmp.addCard(rC);  

    //removes the card from the deck
    deck.removeCard(chC);
    deck.removeCard(wC);
    deck.removeCard(rC);

    return tmp;

  }

  

  // line 492 "model.ump" 
    /**
     * Creates the players/characters for the game.
     * @param humanPlayers number of humans player
     * @return 
     */
   private Player[] createAssignPlayers(int humanPlayers){ 

    Player[] players = new Player[4];  

    // Create default player references  

    // since we don't have computer players currently made humanPlayers is not used

    // randomaly Create a player from reference not in players and assign if it is a human or player  
    for (Player player : board.getPlayers().values()) {
      if(player.getName().startsWith("L")) {
        players[0] = player;
      } else if (player.getName().startsWith("B")) {
        players[1] = player;
      } else if (player.getName().startsWith("M")) {
        players[2] = player;
      } else {
        players[3] = player;
      }
      
    }

    return players; 

  } 

  

  

  /** 

   *  

   *  

   * Set the current active play by giving the index of the player in turn order 

   */ 

  // line 510 "model.ump" 
  /**
   * Goes through and sets the next active player.<br>
   * If no players are active then set the first player to active.
   */
   private void setPlayerActive(){ 
    boolean next = false;

    for(int i = 0; i < turnOrder.length; i++) {  

  
      if(next) {
        turnOrder[i].setActivePlayer(true); 
        continue; 
      }
      if(turnOrder[i].getActivePlayer() && i != turnOrder.length -1) {  

  
        next = true;
        turnOrder[i].setActivePlayer(false);  

  

        continue;  

  

      }    
      turnOrder[i].setActivePlayer(false);  

  

    } 
    if(!next) {
      turnOrder[0].setActivePlayer(true); 
    }

  } 

  

  // line 528 "model.ump" 
  /**
   * Finds active player from players array
   * @return the index of the active player
   */
   private int getPlayerActive(){ 

    for(int i = 0; i < turnOrder.length; i++) {  

      if(turnOrder[i].getActivePlayer()) {  

        return i;  

      }  

    } 

    assert false; 

    return 0; 

  } 

  

   

  

  // line 544 "model.ump" 

   private Boolean resetGame(){ 

    return true; 

  } 

  /**
   * Runs the main portion of the game
   */
  public void run() {
    
      setupGame();
      activeGame();

  }
  

  public static void main(String[] args) {
    Game game = new Game();
    game.run();
  }

  public String toString() 

  { 

    return super.toString() + "["+ 

            "winner" + ":" + getWinner()+ "," + 

            "gameReadySetup" + ":" + getGameReadySetup()+ "," + 

            "gameReadyExit" + ":" + getGameReadyExit()+ "," + 

            "gameReadyActive" + ":" + getGameReadyActive()+ "," + 

            "gameReadyFinished" + ":" + getGameReadyFinished()+ "," + 

            "gameReadyWaiting" + ":" + getGameReadyWaiting()+ "," + 

            "  " + "board" + "=" + (getBoard() != null ? !getBoard().equals(this)  ? getBoard().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") + 

            "  " + "solution" + "=" + (getSolution() != null ? !getSolution().equals(this)  ? getSolution().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") + 

            "  " + "game" + "=" + (getGame() != null ? !getGame().equals(this)  ? getGame().toString().replaceAll("  ","    ") : "this" : "null"); 

  } 

} 

  

class GameGlobalVal 

{ 

  

  //------------------------ 

  // MEMBER VARIABLES 

  //------------------------ 

  

  //GameGlobalVal Attributes 

  private boolean checkHand; 

  private boolean checkCardsKnown; 

  private boolean makingGuess; 

  private boolean makingMovement; 

  private boolean guessMade; 

  private boolean movementMade; 

  

  //------------------------ 

  // CONSTRUCTOR 

  //------------------------ 

  

  public GameGlobalVal() 

  { 

    reset(); 

  } 

  

  //------------------------ 

  // INTERFACE 

  //------------------------ 

  

  public boolean setCheckHand(boolean aCheckHand) 

  { 

    boolean wasSet = false; 

    checkHand = aCheckHand; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setCheckCardsKnown(boolean aCheckCardsKnown) 

  { 

    boolean wasSet = false; 

    checkCardsKnown = aCheckCardsKnown; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setMakingGuess(boolean aMakingGuess) 

  { 

    boolean wasSet = false; 

    makingGuess = aMakingGuess; 

    wasSet = true; 

    return wasSet; 

     

  } 

  

  public boolean setGuessMade(boolean aGuessMade) 

  { 

    boolean wasSet = false; 

    guessMade = aGuessMade; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setMovementMade(boolean aMovementMade) 

  { 

    boolean wasSet = false; 

    movementMade = aMovementMade; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean setMakingMovement(boolean aMakingMovement) 

  { 

    boolean wasSet = false; 

    makingMovement = aMakingMovement; 

    wasSet = true; 

    return wasSet; 

  } 

  

  public boolean getCheckHand() 

  { 

    return checkHand; 

  } 

  

  public boolean getCheckCardsKnown() 

  { 

    return checkCardsKnown; 

  } 

  

  public boolean getMakingGuess() 

  { 

    return makingGuess; 

  } 

  

  public boolean getMakingMovement() 

  { 

    return makingMovement; 

  } 

  

  public boolean getGuessMade() 

  { 

    return guessMade; 

  } 

  

  public boolean getMovementMade() 

  { 

    return movementMade; 

  } 

  

  public void reset() { 

    checkHand = false; 

    checkCardsKnown = false; 

    makingGuess = false; 

    makingMovement = false; 

    guessMade = false; 

    movementMade = false; 

  } 

  

  public void delete() 

  {} 
  

  public String toString() 

  { 

    return super.toString() + "["+ 

            "checkHand" + ":" + getCheckHand()+ "," + 

            "checkCardsKnown" + ":" + getCheckCardsKnown()+ "," + 

            "makingGuess" + ":" + getMakingGuess()+ "," + 

            "makingMovement" + ":" + getMakingMovement()+ "]"; 

  } 

} 

 