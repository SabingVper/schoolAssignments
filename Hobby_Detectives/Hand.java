/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import java.util.*;

// line 150 "model.ump"
// line 268 "model.ump"
public class Hand
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Hand Associations
  private List<Card> cards;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Hand()
  {
    cards = new ArrayList<Card>();
  }

  //------------------------
  // INTERFACE
  //------------------------
  /**
   * Gets cards a card from a certain index
   * @param index The index of the card
   * @return Card at the index
   */
  public Card getCard(int index)
  {
    Card aCard = cards.get(index);
    return aCard;
  }

    /**
   * Returns all the cards in the hand
   * @return all the cards in the hand
   */
  public List<Card> getCards()
  {
    List<Card> newCards = Collections.unmodifiableList(cards);
    return newCards;
  }

  /**
   * Checks how many card the hand contains
   * @return umber of cards the hand contains
   */
  public int numberOfCards()
  {
    int number = cards.size();
    return number;
  }

  /**
   * check if the hand contains any cards
   * @return true is cards is greater than 0
   */
  public boolean hasCards()
  {
    boolean has = cards.size() > 0;
    return has;
  }

  /**
   * Gives the index of a card
   * @param aCard
   * @return
   */
  public int indexOfCard(Card aCard)
  {
    int index = cards.indexOf(aCard);
    return index;
  }
  
  /**
	 * Add a card to the hand.
	 * @param card The card to be added.
	 */
  public boolean addCard(Card aCard)
  {
    boolean wasAdded = false;
    if (cards.contains(aCard)) { return false; }
    cards.add(aCard);
    wasAdded = true;
    return wasAdded;
  }

  /**
   * Removes Card from hand
   * @param aCard
   * @return true if card was removed
   */
  public boolean removeCard(Card aCard)
  {
    boolean wasRemoved = false;
    if (cards.contains(aCard))
    {
      cards.remove(aCard);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /**
   * Removes a Card from the Hand and returns the card that was removed
   * @return The card that was removed
   */
  public Card removeFromHand(){
    if(cards.isEmpty()){throw new Error("No more cards to remove");}
    Card c = cards.get(0);
    cards.remove(0);
    return c;
  }

  /**
   * Adds card at a certain position
   * @param aCard
   * @param index
   * @return
   */
  public boolean addCardAt(Card aCard, int index)
  {  
    boolean wasAdded = false;
    if(addCard(aCard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCards()) { index = numberOfCards() - 1; }
      cards.remove(aCard);
      cards.add(index, aCard);
      wasAdded = true;
    }
    return wasAdded;
  }

  /**
   * Adds or moves card to a certain position
   * @param aCard Card need to be added
   * @param index The index where the card is movedd or added
   * @return true if Card was added otherwise false
   */
  public boolean addOrMoveCardAt(Card aCard, int index)
  {
    boolean wasAdded = false;
    if(cards.contains(aCard))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCards()) { index = numberOfCards() - 1; }
      cards.remove(aCard);
      cards.add(index, aCard);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCardAt(aCard, index);
    }
    return wasAdded;
  }

  /**
   * 
   * Clears all the cards in the hand
   */
  public void delete()
  {
    cards.clear();
  }

  //---------------------------------------------------------------------------------------------

  /**
   * 
   * Checks whether a given card is contained in this hand
   * @param card Card to check
   * @return true if the card is contained in this hand otherwise false
   */
  // line 157 "model.ump"
   public boolean contains(Card card){
    return cards.contains(card);
  }
  /**
   * 
   * Checks whether all the cards given list is contained in this hand
   * @param list list to check
   * @return true if the cards are contained in this hand otherwise false
   */
  public boolean containsAll(List<Card> list) {
    return cards.containsAll(list);
  }

  /**
   * 
   * Checks whether all the cards given list is contained in this hand
   * @param hand list to check
   * @return true if the cards are contained in this hand otherwise false
   */
  public boolean containsAll(Hand hand) {
    for(Card c : hand.getCards()) {
      boolean check = false;
      for(Card o : getCards()) {
        if(o.equals(c)) {
          check = true;
          break;
        }
      }
      if(!check) {
        return false;
      }
    }
    return true;
  }

  /**
   * 
   * Get the card in hand by putting in the name of the card
   * @param s The name of the Card
   * @return The card  with the same name otherwise null
   */
  // line 203 "model.ump"
   public Card getCardByTypeName(String s){
    return cards.stream()
    .filter(c-> c.type().toString().toLowerCase().equals(s.toLowerCase()))
    .findAny()
    .orElse(null);
  }


  /**
   * 
   * Return all cards in this hand which  match the given category
   * @param category The catagory to check for
   * @return The set of matching cards if any
   */
  // line 215 "model.ump"
   public List<Card> matches(Card.Category category){
    List<Card> m = new ArrayList<Card>();
		for(Card c : cards) {
			if(c.category() == category) {
				m.add(c);
			}
		}
		return m;
  }

  /**
   * 
   * Sets a set of cards as the hand
   * @param sCards The set of cards to become the hand
   */
  // line 249 "model.ump"
   public void setCards(List<Card> sCards){
    cards.clear();
    cards.addAll(sCards);
  }

  /**
   * Shuffles the Hand
   */
   public void shuffle(){
    Collections.shuffle(cards);
  }

  /**
   * Builds the deck
   */
  public void buildDeck(){
    cards.clear();
    int i = 0;
    for(Card.Type t : Card.Type.values()){
      if(t == Card.Type.BLANK) {continue;}
      if(i >= Card.Type.LUCILLA.ordinal() && i<=Card.Type.PERCY.ordinal()){
        addCard(new Card(Card.Category.CHARACTER, t));
      }else if(i >= Card.Type.HAUNTEDHOUSE.ordinal() && i<=Card.Type.PERILPALACE.ordinal()){
        addCard(new Card(Card.Category.ROOM, t));
      }else{
        addCard(new Card(Card.Category.WEAPON, t));
      }
      i++;
    }
  }

  /**
   * Some testing
   * @param args
   */
  public static void main(String[] args){
    Hand h = new Hand();
    //Tests addCard method and Card toString()
    h.addCard(new Card(Card.Category.CHARACTER, Card.Type.BERT));
    h.addCard(new Card(Card.Category.WEAPON, Card.Type.BROOM));
    h.addCard(new Card(Card.Category.ROOM, Card.Type.HAUNTEDHOUSE));
    h.getCards().forEach(c -> System.out.println(c.toString()));
    System.out.println("----------------------------------------");
    
    //Tests Shuffle
    h.shuffle();
    h.getCards().forEach(c -> System.out.println(c.toString()));

    System.out.println("----------------------------------------");
    //Test create deck
    Hand h2 = new Hand();
    h2.buildDeck();
    h2.getCards().forEach(c -> System.out.println(c.toString()));

    System.out.println("----------------------------------------");
    //Tests to check if deck can be seperated by category
    //Charcter
    h2.matches(Card.Category.CHARACTER).forEach(c -> System.out.println(c.toString()));
    System.out.println("----------------------------------------");
    //Weapon
    h2.matches(Card.Category.WEAPON).forEach(c -> System.out.println(c.toString()));
    System.out.println("----------------------------------------");
    //Room
    h2.matches(Card.Category.ROOM).forEach(c -> System.out.println(c.toString()));

    System.out.println("----------------------------------------");
    //checks if can put List in the hand
    Hand charHand = new Hand();
    charHand.setCards(h2.matches(Card.Category.CHARACTER));
    charHand.getCards().forEach(c -> System.out.println(c.toString()));

    System.out.println("----------------------------------------");
    //Test shuffling of the deck
    h2.shuffle();
    h2.getCards().forEach(c -> System.out.println(c.toString()));

    System.out.println("----------------------------------------");
    //Tests removing from deck and adding into another hand
    Hand p1 = new Hand();
    p1.addCard(h2.removeFromHand());
    p1.getCards().forEach(c -> System.out.println(c.toString()));
    System.out.println("----------------------------------------");
    //Tests if card was removed
    h2.getCards().forEach(c -> System.out.println(c.toString()));

    System.out.println("----------------------------------------");
    //Checks can get card from typename
    System.out.println(h.getCardByTypeName("bRoOm").toString());
  }

  public String toString() {
    String str = "";
    for(Card c : cards) {
      str += "[" + c.toString() + "] ";
    }
    return str;
  }
}