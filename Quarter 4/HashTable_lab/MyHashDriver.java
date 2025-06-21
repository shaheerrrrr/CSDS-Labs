import java.io.IOException;
import java.util.Scanner;

class MyHashDriver
{
   public static void main(String[] args) throws IOException {
      Scanner input = new Scanner(System.in);
      System.out.println("What name do you want to search for?");
      String n = input.nextLine();
      long start = System.nanoTime();
      int id = MyHashTable.search(n);
      long end = System.nanoTime();
      double searchTime = (end - start)/1_000_000_000d;
      if (id == -1)
         System.out.println("That name does not exist");
      else
      {
         System.out.println("The id of " + n + " is " + id);
      }
      System.out.println("Search took " + searchTime + " seconds");
   }
}