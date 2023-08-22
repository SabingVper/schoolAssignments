public class Dice{  

    private int value = 0;  
  
   
   public Dice() {
    roll();
   }
  
    public int roll(){   
  
   value = (int)(Math.floor(Math.random() * 6)+1); 
  
    return value;  
  
    } 
  
    public int getValue() {
      return value;
    }
  
    public static int roll2(){
      return (int)(Math.floor(Math.random() * 6)+1);
    }
  
  } 
  