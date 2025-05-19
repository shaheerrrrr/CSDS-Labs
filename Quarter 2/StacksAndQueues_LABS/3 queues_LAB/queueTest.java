//    d oberle 2023.
import java.util.*;
import java.io.*;

public class queueTest
{
//pre:  word is not a reference to the null String
//post: returns a Queue of single character Strings comprised of the characters in word
   public static MyQueue<String> toQueue(String word)
   {
      MyQueue<String> ans = new MyQueue();  
      for(int i=0; i<word.length(); i++)
         ans.add("" + word.charAt(i));
      return ans;
   }

   public static void main(String args[])
   {
      MyQueue<String> wordQueue = new MyQueue();
      wordQueue = toQueue("Hello");
      while (!wordQueue.isEmpty())
         System.out.println("Removing " + wordQueue.remove() + " off of the top of the queue");
   
   
      MyQueue<String> books = new MyQueue();
      books.add("War & Peace");
      books.add("C++ for U++");
      books.add("Emma");
      books.add("Godel, Escher, Bach");
                
      System.out.println();	
      System.out.println("The book on the top of the queue is " + books.peek());
      while (!books.isEmpty())
         System.out.println("Removing " + books.remove() + " off of the top of the queue");
   
   }    


}