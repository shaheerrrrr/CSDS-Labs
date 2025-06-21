import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class MyHashTable
{
   public static Tree[] readFile(String filename) throws IOException
   {
      Tree[] table = new Tree[1000];
      for (int i = 0; i < table.length; i++)
      {
         table[i] = new Tree();
      }

      Scanner input = new Scanner(new FileReader(filename));
      while (input.hasNextLine())
      {
         String line = input.nextLine();
         String[] parts = line.split(" ");
         Employee temp = new Employee(Integer.parseInt(parts[0]), parts[1]);
         int index = temp.hashCode();
         table[index].add(temp);
      }
      input.close();
      return table;
   }

   public static int search(String name) throws IOException
   {
      Tree[] table = readFile("hashMillion.txt");
      Employee temp = new Employee(0, name);
      int index = temp.hashCode();
      TreeNode found = table[index].search(temp);
      if (found == null)
         return -1;
      return found.getValue().getId();
   }
}