public class Laser
{
   private int x,y;
   private int direction;
   public static final int UP = 0;
   public static final int RIGHT = 1;
   public static final int DOWN = 2;
   public static final int LEFT = 3;

   public Laser(char letter)
   {
      if (letter >= 'A' && letter <= 'J')
      {
         x = 10;
         y = (letter-'A')+1;
         direction = UP;
      }
      else if (letter >= 'K' && (int)letter <= 'T')
      {
         x = (letter-'K')+1;
         y = 10;
         direction = LEFT;
      }
      else if (letter >= 'a' && letter <= 'j')
      {
         x = 1;
         y = (letter-'a')+1;
         direction = DOWN;
      }
      else //if (letter >= 'k' && letter <= 't')
      {
         x = (letter-'k')+1;
         y = 1;
         direction = RIGHT;
      }
   }



   public int getX()
   {
      return x;
   }

   public int getY()
   {
      return y;
   }

   public int getDirection()
   {
      return direction;
   }

   public void setX(int x1)
   {
      x = x1;
   }

   public void setY(int y1)
   {
      y = y1;
   }

   public void setDirection(int d)
   {
      direction = d;
   }

   public char move(Cell[][] c)
   {
      while (x > 0 && x < 11 && y > 0 && y < 11)
         moveOneSpace(c);
      if (x == 0)
         return (char)(y-1+'a');
      else if (x == 11)
         return (char)(y-1+'A');
      else if (y == 0)
         return (char)(x-1+'k');
      else // (y == 11)
         return (char)(x-1+'K');
   }

   public void moveOneSpace(Cell[][] c)
   {
      if (direction == UP)
      {
         if (c[x-1][y].getChar() == '/')
            direction = RIGHT;
         else if (c[x-1][y].getChar() == '\\')
            direction = LEFT;
         x--;

      }
      else if (direction == RIGHT)
      {
         if (c[x][y+1].getChar() == '/')
            direction = UP;
         else if (c[x][y+1].getChar() == '\\')
            direction = DOWN;
         y++;
      }
      else if (direction == DOWN)
      {
         if (c[x+1][y].getChar() == '/')
            direction = LEFT;
         else if (c[x+1][y].getChar() == '\\')
            direction = RIGHT;
         x++;
      }
      else // direction == LEFT
      {
         if (c[x][y - 1].getChar() == '/')
            direction = DOWN;
         else if (c[x][y - 1].getChar() == '\\')
            direction = UP;
         y--;
      }
   }
}
