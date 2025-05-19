  //Curtis Grover, D Oberle
  //Utility class - methods for small jobs, mostly handling the file input/output for the high-scores file
import java.util.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Utilities
{
   public static HashMap<String, String> encode, decode;
   protected static final String [] difficulties = {"EASY:","NORM:","HARD:"};
   public static void initialize()
   {
      String [] lets = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"," "};
      String [] symb = {"0","1","2","3","4","5","6","7","8","9","-","=","_","+","~","!","@","#","$","%","^","&","*","(",")","?"," "};
      encode = new HashMap();
      decode = new HashMap();
      for(int i=0; i<lets.length; i++)
      {              
         encode.put(lets[i], symb[i]);
         decode.put(symb[i], lets[i]);
      } 
   }

   public static String encrypt (String word)
   {
      String ans = "";
      for(int i=0; i<word.length(); i++)
      {
         String current = word.substring(i, i+1);
         if(decode.containsKey(current))
            ans += decode.get(current);
         else if(encode.containsKey(current))
            ans = ans + encode.get(current);
         else
            ans += current;
      }
      return ans;
   }

   public static String decrypt (String word)
   {
      String ans = "";
      for(int i=0; i<word.length(); i++)
      {
         String current =  "" + word.charAt(i);
         if(encode.containsKey(current))
            ans += encode.get(current);
         else if(decode.containsKey(current))
            ans = ans + decode.get(current);
         else
            ans += current;
      }
      return ans;
   }
   
   public static String deCode(String word, int rank)
   {
      String ans = "";  
      for(int i=0; i < word.length(); i++)
      {
         int shift = i % 5;
         if( shift == 0)
            ans +=  (char)((int)(word.charAt(i)) - 7 - rank);
         else if ( shift == 1)
            ans +=  (char)((int)(word.charAt(i)) - 1 - rank);
         else if ( shift == 2)
            ans +=  (char)((int)(word.charAt(i)) + 3 - rank);
         else if ( shift == 3)
            ans +=  (char)((int)(word.charAt(i)) - 4 - rank);
         else
            ans +=  (char)((int)(word.charAt(i)) + 5 - rank);     
      } 
      return ans;
   }

   public static String enCode(String word, int rank)
   {
      String ans = "";  
      for(int i=0; i < word.length(); i++)
      {
         int shift = i % 5;
         if( shift == 0)
            ans +=  (char)((int)(word.charAt(i)) + 7 + rank);
         else if ( shift == 1)
            ans +=  (char)((int)(word.charAt(i)) + 1 + rank);
         else if ( shift == 2)
            ans +=  (char)((int)(word.charAt(i)) - 3 + rank);
         else if ( shift == 3)
            ans +=  (char)((int)(word.charAt(i)) + 4 + rank);
         else
            ans +=  (char)((int)(word.charAt(i)) - 5 + rank);     
      }     
      return ans;  
   } 

   public static LinkedList<String> readFile( String fileName)throws IOException
   {
      LinkedList<String> list = new LinkedList<String>();
       File file = new File(fileName);
      if(!file.exists())		//make sure to write over old file
      {
         resetScores();
         file = new File(fileName);
         System.out.println("Scores file not found - resetting scores");
      }    
      Scanner input = new Scanner(new FileReader(fileName));
      int i = 0;									
      while (input.hasNextLine())		
      {
         String line = input.nextLine();
         line = decrypt(deCode(line, i));	
         list.add(line);	
         i++;				 								         
      }
      input.close();	
      return list;					
   }
   
   public static boolean writeFile(String fileName, LinkedList<String> list)
   {
      File file = new File(fileName);
      if(file.exists())		//make sure to write over old file
      {
         file.delete();
         file = new File(fileName);
      }
      else
         file = new File(fileName);         
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(file);
         for(int i = 0; i < list.size(); i++)
         {
            String line = list.get(i);
            line = enCode(encrypt(list.get(i)), i);
            writer.println(line);    
         }
         writer.close();
      }
      catch(Exception ex)
      {
         System.out.println("writeOutPlayerToFile error:"+ex);
         return false;
      }
      return true;
   } 

   //post:  resets the scores and overwrites the file
   public static void resetScores() throws IOException
   {
      initialize();
      LinkedList<String> top10 = new LinkedList();
      for(int i = 0; i < 10; i++)
      {
         top10.add("0   AAA");
      }
      writeFile("assets/utilities.txt", top10);
   }

   //pre:  index <= list.size(), list != null and list.size() > 0
   public static String submitScore(int index, int score, String name, LinkedList<String> list, int difficulty)
   {  
      if(list == null || list.size() == 0 || name == null || name.length() == 0)
      {
         return "";
      }
      String temp = "";
      boolean repeat = false;
      if(name.length() > 7)           //truncate name if it is too long to fit in the top 10 window
      {
         name = name.substring(0,7);
      }
      name = difficulties[difficulty] + name;
      for(int i = 0; i < index; i++)
      {
         temp = list.get(i);
         if(temp == null || temp.length() == 0 || name == null || name.length() == 0)
         {  //the client just exited out of the dialogue box, so end the method
            return "";
         }
         if(temp.indexOf(name) >= 0)
         {
            temp = temp.substring(temp.indexOf(name));         
         }
         if(name.equals(temp))
         {
            repeat = true;
            break;
         }
      }    
      if(repeat)
      {
         return " Have a higher score";
      }    
      if(score < 10)
         list.add(index, score + "   " + name);
      else if(score < 100)
         list.add(index, score + "  " + name);
      else
         list.add(index, score + " " + name);           
      for(int i = index + 1; i < list.size(); i++)
      {
         temp = list.get(i);
         if(temp.indexOf(name) >= 0)
            temp = temp.substring(temp.indexOf(name));
         if(name.equals(temp))
         {
            list.remove(i);
            return "Improved your Top 10";  
         }
      }         
      list.removeLast();
      return "You made the Top 10!";      
   }
   
   public static int makeTop10(int score,LinkedList<String> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         int compare = Integer.parseInt(list.get(i).substring(0, list.get(i).indexOf(" ")));
         if(score > compare)
         {
            return i;
         }
      }   
      return -1;
   } 
   
   public static void drawTop10(Graphics g, LinkedList<String> list, int x, int y, int textSize)
   {
      g.setColor(Color.black);
      g.setFont(new Font("Monospaced", Font.BOLD,textSize));
      g.drawString("  TOP 10", x+45, y);
      g.fillRect(x+85, y+5, 125, 4);
      g.setFont(new Font("Monospaced", Font.PLAIN,textSize));
      for(int i = 1; i <= 10; i++)
      {
         g.drawString(list.get(i-1),x, y + i * textSize+10);
      }    
   }
   
   public static void drawMessage(Graphics g, String message, int x, int y)
   {
      g.setColor(Color.red);
      g.setFont(new Font("Monospaced", Font.BOLD,30));
      g.drawString(message, x, y);  
   }
   
   //pre:  word != null and word is a player score in the format of <score> <difficulty>:<name>
   //post: extracts and returns the integer score from word
   public static int getScore(String word)
   {
      int space = word.indexOf(" ");
      return Integer.parseInt(word.substring(0,space).trim());
   }
   
   //pre:  list != null
   //post: will sort the scores in list by the score value, greatest-to-least
   public static void sort(  LinkedList<String> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         int max = i;
         for(int j = i+1; j < list.size(); j++)
         {
            if(getScore(list.get(j)) > getScore(list.get(max)))
               max = j;
         }
         String temp = list.get(i);
         list.set(i, list.get(max));
         list.set(max, temp);
      }
   }
   
   //post:  running this will overwrite the scores
   public static void main(String[] args) throws IOException
   {
      initialize();
      LinkedList<String> top10 = Utilities.readFile("assets/utilities.txt");
      submitScore(0, 97,"Grover", top10, 2);
      submitScore(1, 82,"Peasey!", top10, 0);
      submitScore(2, 57,"as iron", top10, 2);
      submitScore(3, 21,"Matias", top10, 1);
      submitScore(4, 17,"McDnld", top10, 1);
      submitScore(5, 14,"Daniel", top10, 1);
      submitScore(6, 14,"Jakob", top10, 1);
      submitScore(7, 4, "Doc Mom", top10, 1);
      submitScore(8, 2,"Ridge", top10, 1);
      submitScore(9, 1,"Kierstin", top10, 1);
      sort(top10);
      writeFile("assets/utilities.txt", top10);
      System.out.println(top10);
   }
}