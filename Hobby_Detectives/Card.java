/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.32.1.6535.66c005ced modeling language!*/


import java.util.*;

// line 2 "model.ump"
// line 262 "model.ump"
public class Card implements Comparable {

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Category { CHARACTER, ROOM, WEAPON, BLANK }
  public enum Type { LUCILLA, BERT, MALINA, PERCY, HAUNTEDHOUSE, MANICMANOR, VISITATIONVILLA, CALAMITYCASTLE, PERILPALACE, BROOM, SCISSORS, KNIFE, SHOVEL, IPAD, BLANK }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Card Attributes
  private Category category;
  private Type type;
  private List<String> cat;
  private static List<String> typ;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Card(Category aCategory, Type aType)
  {
    if(!sameType(aCategory, aType)){
      throw new Error("Wrong type");
    }
    this.category = aCategory;
    this.type = aType;
    cat = new ArrayList<String>(); 
    cat.add("Character"); 
    cat.add("Room"); 
    cat.add("Weapon");
    typ = new ArrayList<String>(); 
    typ.add("Lucilla"); 
    typ.add("Bert"); 
    typ.add("Malina"); 
    typ.add("Percy"); 
    typ.add("Haunted House"); 
    typ.add("Manic Manor"); 
    typ.add("Visitation Villa"); 
    typ.add("Calamity Castle"); 
    typ.add("Peril Palace"); 
    typ.add("Broom"); 
    typ.add("Scissors"); 
    typ.add("Knife"); 
    typ.add("Shovel"); 
    typ.add("iPad");
  }
  /**
   * This is used as a blank card.
   */
  public Card() {
    this.category = category.BLANK;
    this.type = type.BLANK;
  }

  //------------------------
  // INTERFACE
  //------------------------

  /**
   * Sets category
   * @param aCategory The category to be set
   * @return true if set otherwise false
   */
  public boolean setCategory(Category aCategory)
  {
    boolean wasSet = false;
    category = aCategory;
    wasSet = true;
    return wasSet;
  }

  /**
   * Sets type
   * @param aType The type to be set
   * @return true if set otherwise false
   */
  public boolean setType(Type aType)
  {
    boolean wasSet = false;
    type = aType;
    wasSet = true;
    return wasSet;
  }
  
  /**
   * Gets the category string at the specified index
   * @param index The index to get the String
   * @return String at that index
   */
  public String getCat(int index)
  {
    String aCat = cat.get(index);
    return aCat;
  }

  /**
   * Returns all the categories in string form
   * @return All the categories in string form
   */
  public String[] getCat()
  {
    String[] newCat = cat.toArray(new String[cat.size()]);
    return newCat;
  }

  /**
   * Checks the size of cat
   * @return size of cat
   */
  public int numberOfCat()
  {
    int number = cat.size();
    return number;
  }

  /**
   * Checks whether the category of strings is empty
   * @return true if cat is empty otherwise false
   */
  public boolean hasCat()
  {
    boolean has = cat.size() > 0;
    return has;
  }

  /**
   * Gets the index of the string of a type
   * @param aCat String to get the index of
   * @return index of the String
   */
  public int indexOfCat(String aCat)
  {
    int index = cat.indexOf(aCat);
    return index;
  }
  
  /**
   * Gets a string of a Type at a certain index
   * @param index Index of the string to get
   * @return String at the index
   */
  public String getTyp(int index)
  {
    String aTyp = typ.get(index);
    return aTyp;
  }

  /**
   * Returns all the types in string form
   * @return
   */
  public String[] getTyp()
  {
    String[] newTyp = typ.toArray(new String[typ.size()]);
    return newTyp;
  }

  /**
   * Checks the size of typ
   * @return size of typ
   */
  public int numberOfTyp()
  {
    int number = typ.size();
    return number;
  }

  /**
   * Checks whether the type of strings is empty
   * @return true if typ is empty otherwie false
   */
  public boolean hasTyp()
  {
    boolean has = typ.size() > 0;
    return has;
  }

  /**
   * Gets the index of the string of a type
   *
   * @param aTyp The string used to find the index
   * @return Index of the String
   */
  public static int indexOfTyp(String aTyp)
  {
    int index = typ.indexOf(aTyp);
    return index;
  }

  /**
   * Checks if the type is a part of the correct category
   * eg. PERCY is a CHARACTER
   * @param c The category of the item eg. Character, Weapon or a Room
   * @param t One of the Items/Types
   */
  // line 51 "model.ump"
   public boolean sameType(Category c, Type t){
    if(c == Category.CHARACTER){
      return isACharacter(t);
    }else if(c == Category.ROOM){
      return isARoom(t);
    }else if(c == Category.WEAPON){
      return isAWeapon(t);
    }
    return false;
  }


  /**
   * 
   * Checks if a type is a part of the Charracter category
   * @param t One of the Items/Types
   * @return true if it is
   */
  // line 67 "model.ump"
   public boolean isACharacter(Type t){
    if(t.ordinal() >= Type.LUCILLA.ordinal() && t.ordinal()<=Type.PERCY.ordinal()){
        return true;
    }
    return false;
  }


  /**
   * 
   * Checks if a type is a part of the Room category
   * @param t One of the Items/Types
   * @return true if it is
   */
  // line 79 "model.ump"
   public boolean isARoom(Type t){
    if(t.ordinal() >= Type.HAUNTEDHOUSE.ordinal() && t.ordinal()<=Type.PERILPALACE.ordinal()){
        return true;
    }
    return false;
  }


  /**
   * 
   * Checks if a type is a part of the Weapon category
   * @param t One of the Items/Types
   * @return true if it is
   */
  // line 91 "model.ump"
   public boolean isAWeapon(Type t){
    if(t.ordinal() >= Type.BROOM.ordinal() && t.ordinal()<=Type.IPAD.ordinal()){
        return true;
    }
    return false;
  }


  /**
   * 
   * Get the category of this card
   * @return The ccategory of the card
   */
  // line 102 "model.ump"
   public Category category(){
    return category;
  }


  /**
   * 
   * Get the type of this card
   * @return The type/Item that the card holds
   */
  // line 110 "model.ump"
   public Type type(){
    return type;
  }


  /**
   * 
   * Returns the card as a String
   */
  // line 125 "model.ump"
   public String toString(){
    return cat.get(category.ordinal()) + ": " + typ.get(type.ordinal());
  }


  /**
   * 
   * Checks if 2 cards are equal
   * @param c The card to check
   * @return true if 2 cards are equal
   */
  // line 134 "model.ump"
  /* public boolean equals(Card c){
    return c.category.equals(this.category) && c.type.equals(this.type);
  } */


  /**
   * Checks if 2 cards are equal
   * @param c The card to check
   * @return returns whether the card is greater(1), less(-1) or equal(0) than this card
   */
  // line 143 "model.ump"
  public int compareTo(Card c){
    if(c.category.compareTo(this.category) == 0) {
			return -1 * c.type.compareTo(this.type);
    }
    return -1 * c.category.compareTo(this.category);
  }
  
  @Override
  public int compareTo(Object obj) {
    Card other = (Card) obj;
    return compareTo(other);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Card other = (Card) obj;
    if (category != other.category)
      return false;
    if (type != other.type)
      return false;
    return true;
  }

}