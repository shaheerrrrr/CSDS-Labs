//    d oberle 2024.
//Can be run to test your MyQueue object to verify that it works properly and can be used in the game.
import java.util.*;
import java.io.*;

public class queueTest
{
   public static void main(String args[])
   {
      //demonstrate add method works
      MyQueue<String> q = new MyQueue();     
      System.out.println(q);                    //should display []
      q.add("A");
      q.add("B");
      q.add("C");
      
      //demonstrate size method works
      System.out.print(q.size() + ":");
      System.out.println(q);                    //should display 3:[A, B, C]
      
      //demonstrate peek and getLast methods work
      System.out.println("First:"+q.peek());    //should display First:A
      System.out.println("Last:"+q.getLast());  //should display Last:C
      
      //demonstrate isEmptly and remove methods work with queue behavior (FIFO)
      System.out.print("Removing:");
      while(!q.isEmpty())
      {                                         //should display Removing:A B C
         System.out.print(q.remove()+" ");
      }
      System.out.println();
      q.add("1");
      q.add("2");
      q.add("3");
      
      //demonstrate Iterable implementation works
      for(String s:q)
      {
         System.out.print(s+"*");               //should display 1*2*3*
      }
      System.out.println();
      
      //demonstrate clear method works
      q.clear();
      System.out.println(q);                    //should display []
   }    


}