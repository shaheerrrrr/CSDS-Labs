//d oberle - doberle@fcps.edu.
import java.awt.Color;

public class SandUtilities
{
  //pre : c!= null.
  //post: returns the inverted color from the one sent as c.
   public static Color invert(Color c)
   {
      return new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());
   }
   
   //pre:   m!= null.
   //post:  for each non-null element of m, changes it to its inverted color.
   //       skips any color with the value skip1 and skip2, leaving them unchanged.
   public static void invertColors(Color[][]m, Color skip1, Color skip2)
   {
      for (int r = 0; r < m.length; r++)
      {
         for (int c = 0; c < m[0].length; c++)
         {
            if (m[r][c] != null && !m[r][c].equals(skip1) && !m[r][c].equals(skip2))
            {
               m[r][c] = invert(m[r][c]);
            }
         }
      }
   }
   
   //pre:   m is a square 2-D array (m.length==m[0].length).
   //post:  flips the array upside down.
   public static void flipUpsideDown(Object[][] m)
   {
      for (int r = 0; r < m.length/2; r++)
      {
         for (int c = 0; c < m[0].length; c++)
         {
            Object temp = m[r][c];
            m[r][c] = m[m.length-1-r][c];
            m[m.length-1-r][c] = temp;
         }
      }
   }
   
   //pre:   m is a square 2-D array (m.length==m[0].length)
   //post:  rotates the array 90 degrees to the right
   public static void rotateRight(Object[][] m)
   {
      Object[][] boom = new Object[m.length][m.length];
      for (int r = 0; r < m.length; r++)
      {
         for (int c = 0; c < m.length; c++)
         {
            boom[r][c] = m[m.length-1-c][r];
         }
      }
      for (int r = 0; r < m.length; r++)
      {
         for (int c = 0; c < m.length; c++)
         {
            m[r][c] = boom[r][c];
         }
      }
   }

   //pre:   m is a square 2-D array (m.length==m[0].length)
   //post:  rotates the array 90 degrees to the left
   public static void rotateLeft(Object[][] m)
   {
      Object[][] boom = new Object[m.length][m.length];
      for (int r = 0; r < m.length; r++)
      {
         for (int c = 0; c < m.length; c++)
         {
            boom[r][c] = m[c][m.length-1-r];
         }
      }
      for (int r = 0; r < m.length; r++)
      {
         for (int c = 0; c < m.length; c++)
         {
            m[r][c] = boom[r][c];
         }
      }
   }

   //main function for testing and debugging
   public static void main (String [] arg)
   {
      String [][] test = {{"A","B","C"},{"D","E","F"},{"G","H","I"}};
      show(test);
      flipUpsideDown(test);
      show(test);
      rotateLeft(test);
      show(test);
      rotateRight(test);
      show(test);
   }  
   
   public static void show(String [][] grid)
   {
      for(int r = 0; r < grid.length; r++)
      {
         for(int c = 0; c < grid[r].length; c++)
         {
            System.out.print(grid[r][c]+ " ");          
         }
         System.out.println();
      }
      System.out.println();
   }
}