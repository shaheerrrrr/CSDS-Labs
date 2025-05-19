//note from oberle - your incrementing between yes and no are backwards.  Awaiting completion
//    d oberle 2021.
import java.io.*;
import java.util.*;
/**The Animal Guessing Program:	d oberle, 2021
 * This game will attempt to guess an animal that the user is thinking of by asking yes/no questions.
 * If the program does not know the animal, it will add it to its data base, making it a little smarter for the next user.
 * Implement an array as a heap.  Each index has a parent at (index/2), a left child as (index*2) and a right child at index*2+1.
 * The index path following a "no" response will go to the left child (left subtree).  
 * The index path following a "yes" response will go to the right child (right subtree).*/

public class animalGuesserShell
{
 /** Given the name of a file that exists in the same folder, this counts the number of lines in it. O(n)
  * Helper method for readFile(String fileName).
  * @param fileName  the name of a file that exists in the same folder as this file.
  * @throws IOException  in case we are trying to find the size of a file that does not exist.
  * @return  the number of lines in the file. 
  */    
   public static int getFileSize(String fileName)throws IOException
   {
      Scanner input = new Scanner(new FileReader(fileName));
      int size=0;
      while (input.hasNextLine())				//while there is another line in the file
      {
         size++;										//add to the size
         input.nextLine();							//go to the next line in the file
      }
      input.close();									//always close the files when you are done
      return size;
   }
   
 /** Fills and returns an array of Strings where each element is a line from a file that exists in the same folder. O(n)
  * @param fileName  the name of a file that exists in the same folder as this file.
  * @throws IOException  in case we are trying to read from a file that does not exist.
  * @return  a String array of all the elements in filename.txt, with index 0 unused (heap). 
  */
   public static String[] readFile(String fileName) throws IOException
   {
      int size = getFileSize(fileName);		//holds the # of elements in the file
      String[] list = new String[size];		//a heap will not use index 0;
      Scanner input = new Scanner(new FileReader(fileName));
      int i=0;											//index for placement in the array
      String line;	
      while (input.hasNextLine())				//while there is another line in the file
      {
         line=input.nextLine();					//read in the next Line in the file and store it in line
         list[i]= line;								//add the line into the array
         i++;											//advance the index of the array         
      }
      input.close();	
      return list;					
   }
   
 /** displays all of the elements of the array words for testing. O(n) 
   * @param words  a non-null array of words.
   */     
   public static void showArray(String[] words)
   {
      for (int i=0; i<words.length; i++)
         System.out.println(words[i] + " ");
      System.out.println();
      System.out.println("Size of array:" + words.length);
   }
   
 /** Writes the contents of an array of Strings out to a file where each array element is on a separate line. O(n)
  * @param array  an array of Strings that you want to write out to a file.
  * @throws IOException  in case we are trying to write to a file that can not be created.  
  * @param filename  the name of a file.
  */   
   public static void writeToFile(String[] array, String filename) throws IOException
   {
      System.setOut(new PrintStream(new FileOutputStream(filename)));
      for (int i = 0; i < array.length; i++)
         System.out.println(array[i]);
      System.out.flush();    
      System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
   }

    /** Looks to see if a character is a vowel. O(1)
  * For use of seeing if the article "a" or "an" needs to come before an animal name.
  * i.e. "Is it an ardvark", or "Is it a narwhal".
  * @param let  the alphabetic character to check; precondition: let is between'A' and 'Z', OR between 'a'  and'z'.
  * @return  true if let is a vowel, false if let is not a vowel. 
  */   
   public static boolean isVowel(char let)
   {
      return (let=='a' || let=='e' || let=='i' || let=='o' || let=='u' || let=='A' || let=='E' || let=='I' || let=='O' || let=='U');
   }
   
 /** Looks to see if a String is likely a "no" type response. O(1)
  * For use to see if a client's response to a question requires moving to the left-subtree.
  * @param ans  a non-null String.
  * @return  returns true if a user prompt is N, No, NO, n, nO. 
  */    
   public static boolean isNo(String ans)
   {
      return (ans.equalsIgnoreCase("no") || ans.equalsIgnoreCase("n"));
   }

    /**
     * MY CODE
     * Counts the number of kids for the given array index
     * @param array is the array to check
     * @param index is the index to check if the children has kids
     * @return number of children
     */
    public static int countKids(String[] array, int index)
    {
        int kids = 0;
        if (!(array[index*2].equals("0")) && index*2 < array.length)
            kids++;
        if (!(array[index*2+1].equals("0")) && index*2+1 < array.length)
            kids++;
        return kids;
    }

    /** Looks to see if a String is likely a "yes" type response. O(1)
  * For use to see if a client's response to a question requires moving to the right-subtree.
  * @param ans  a non-null String.
  * @return  returns true if a user prompt is y, Y, Yes, yes, YES, yES, or yeS 
  */      
   public static boolean isYes(String ans)
   {
      return (ans.equalsIgnoreCase("yes") || ans.equalsIgnoreCase("y"));
   }
   
 /** The main-function: here is where you will code the Animal-Guesser game.
  * @param argv  because, you know...we need this.
  * @throws IOException  because we will need to call methods that can throw this exception.  
  */   
   public static void main(String[] argv) throws IOException
   {
       Scanner input = new Scanner(System.in);
       String[] animals = readFile("animal.txt");
       int i = 1;

       while (!animals[i].equals("0") && i*2 < animals.length)
       {
           System.out.println(animals[i]);
           String in = input.nextLine();
           if (isYes(in))
               i *= 2;
           else if (isNo(in))
               i = i*2+1;
       }
       if (animals[i].equals("0"))
       {
           System.out.println("I couldn't guess the animal, what is it?");
           String anim = input.nextLine();
           System.out.println("What is something true about" + anim + "that is not true about" + animals[i/2] + "?");
           String newQues = input.nextLine();
           System.out.println("Would you like to write to file?");
           if (isYes(input.nextLine()))
           {
               animals[i*2] = animals[i];
               animals[i] = newQues;
               animals[i+2+1] = anim;
               writeToFile(animals, "animal.txt");
           }
       }
       else if (i*2 < animals.length)
       {
           System.out.println("I guessed ur animal :yum:");
       }
   } 
}