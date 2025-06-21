import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class BlackBox {

   private Cell[][] cells;
   private Laser l;
   private HashSet<Point> mirrors;
   private HashSet<Point> foundMirrors;
   private HashSet<String> fMirrors;
   private HashSet<Point> guesses;
   private ArrayList<String> history;
   private ArrayList<String> fails;
   private int shotsLeft;
   private int guessesLeft;
   private int mirrorsLeft;

   public BlackBox()
   {
      cells = new Cell[12][12];
      for (int r = 0; r < 12; r++)
      {
         for (int c = 0; c < 12; c++)
         {
            cells[r][c] = new Cell();
         }
      }
      mirrors = new HashSet<>(10);
      foundMirrors = new HashSet<>(10);
      fMirrors = new HashSet<>(10);
      guesses = new HashSet<>(20);
      history = new ArrayList<>();
      fails = new ArrayList<>();

      shotsLeft = 25;
      guessesLeft = 20;
      mirrorsLeft = 10;

      //randomly generate 10 mirrors
      int i = 0;
      while (i < 10)
      {
         int randX = (int)(Math.random()*10) + 1;
         int randY = (int)(Math.random()*10) + 1;
         //if random is greater than .5 forward slash mirror, if not then backslash mirror
         char c = Math.random() >= .5 ? '/' : '\\';
         if (mirrors.add(new Point(randX, randY)))
         {
            cells[randX][randY] = new Cell(c);
            cells[randX][randY].setVisibility(false);
            i++;
         }
      }
   }
   public boolean guess(char r, char c)
   {
      int x = (int)(r - 'k') + 1;
      int y = (int)(c - 'a') + 1;
      guessesLeft--;
      if (cells[x][y].getChar() != '.')
      {
         foundMirrors.add(new Point(x, y));
         fMirrors.add("(" + x + ", " + y + ")");
         cells[x][y].setVisibility(true);
         mirrorsLeft--;
         return true;
      }
      else
      {
         fails.add("(" + x + ", " + y + ")");
         return false;
      }
   }

   public char test(char s)
   {
      shotsLeft--;
      l = new Laser(s);
      char e = l.move(cells);
      history.add(s +" -> " + e);
      return e;
   }

   public void revealAll()
   {
      Iterator it = mirrors.iterator();
      while (it.hasNext())
      {
         Point p = (Point)it.next();
         cells[p.x][p.y].setVisibility(true);
      }
   }

   public HashSet<Point> getMirrors()
   {
      return mirrors;
   }

   public HashSet<Point> getFoundMirrors()
   {
      return foundMirrors;
   }

   public HashSet<String> getfMirrors()
   {
      return fMirrors;
   }

   public HashSet<Point> getGuesses()
   {
      return guesses;
   }

   public ArrayList<String> getHistory()
   {
      return history;
   }

   public ArrayList<String> getFails()
   {
      return fails;
   }

   public int getShotsLeft()
   {
      return shotsLeft;
   }

   public int getGuessesLeft()
   {
      return guessesLeft;
   }

   public int getMirrorsLeft()
   {
      return mirrorsLeft;
   }

   public Cell[][] getCells()
   {
      return cells;
   }

   public String printHistory()
   {
      return history.toString();
   }

   public String printFails()
   {
      return fails.toString();
   }

   @Override
   public String toString()
   {
      StringBuilder s = new StringBuilder();
      s.append("*  a  b  c  d  e  f  g  h  i  j  *\n");
      for (int r = 1; r < 11; r++)
      {
         s.append((char)(106+r) + "  ");
         for (int c = 1; c < 11; c++)
         {
            if (cells[r][c].getVisibility())
               s.append(cells[r][c].getChar() + "  ");
            else
               s.append(".  ");
         }
         s.append((char)(74+r) + "\n");
      }
      s.append("*  A  B  C  D  E  F  G  H  I  J  *");
      return s.toString();
   }

   public static void main(String[] args)
   {
      BlackBox bb = new BlackBox();
      System.out.println("Welcome to Blackbox!\n");
      boolean b = true;
      Scanner input = new Scanner(System.in);
      while (b)
      {
         if (bb.getMirrors() == bb.getFoundMirrors())
         {
            System.out.println("You found all the mirrors! Congratulations!");
            break;
         }
         System.out.println(bb.toString());
         System.out.println("\n# of Guesses Left:" + bb.getGuessesLeft());
         System.out.println("# of Mirrors Left:" + bb.getMirrorsLeft());
         System.out.println("\n# of Shots Left:" + bb.getShotsLeft());
         System.out.println("\nHistory of Laser Shots:\n" + bb.getHistory());
         System.out.println("\nMirrors Found:\n" + bb.getfMirrors());
         System.out.println("\nFailed Mirror Guesses:\n" + bb.getFails());

         System.out.println("Press [1] to shoot a laser");
         System.out.println("Press [2] to guess a mirror's location");
         System.out.println("Press [3] to give up and reveal mirrors");
         System.out.println("Press [4] to quit");
         int choice = input.nextInt();
         while (choice < 0 || choice > 5)
         {
            System.out.println("Please enter a valid option");
            System.out.println("Press [1] to shoot a laser");
            System.out.println("Press [2] to guess a mirror's location");
            System.out.println("Press [3] to give up and reveal mirrors");
            System.out.println("Press [4] to quit");
            choice = input.nextInt();
         }
         if (choice == 1)
         {
            if (bb.getShotsLeft() < 1)
            {
               System.out.println("You are out of shots!");
               continue;
            }
            System.out.println("Choose a station to start at (a-t) or (A-T)");
            char c = input.next().charAt(0);
            System.out.println("The shot landed at " + bb.test(c));
         }
         else if (choice == 2)
         {
            if (bb.getGuessesLeft() < 1)
            {
               System.out.println("You are out of guesses!");
               break;
            }
            System.out.println("What is your guess's column (a-j)?");
            char y = input.next().charAt(0);
            System.out.println("What is your guess's row (k-t)?");
            char x = input.next().charAt(0);

            if (bb.guess(x,y))
            {
               System.out.println("You found a mirror!\n");
            }
            else
            {
               System.out.println("No mirror at (" + x + ", " + y + ")\n");
            }
         }
         else if (choice == 3)
         {
            bb.revealAll();
            System.out.println(bb);
            b = false;
         }
         else if (choice == 4)
         {
            b = false;
         }
      }
      System.out.println("Thanks for playing!");
   }
}
