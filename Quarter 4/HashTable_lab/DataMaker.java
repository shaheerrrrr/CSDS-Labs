import java.io.*;

public class DataMaker
{
   public static void main(String[] args) throws IOException
   {
      try (FileWriter fw = new FileWriter("hashMillion.txt")) {
         long start = System.nanoTime();
         for (int i = 0; i < 10000000; i++) {
            Employee e = new Employee(i);
            fw.write(e + "\n");
         }
         long end = System.nanoTime();
         double total = (end-start)/1_000_000_000d;
         System.out.println("Total Time to create file: " + total + " seconds");
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }
}