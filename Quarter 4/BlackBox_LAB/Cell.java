public class Cell
{
   private char character;
   private boolean visibility;

   public Cell()
   {
      character = '.';
      visibility = false;
   }

   public Cell(char c)
   {
      character = c;
      visibility = false;
   }

   public void setCharLeft()
   {
      character = '\\';
   }

   public void setCharRight()
   {
      character = '/';
   }

   public void setVisibility(boolean b)
   {
      visibility = b;
   }

   public char getChar()
   {
      return character;
   }

   public boolean getVisibility()
   {
      return visibility;
   }

   @Override
   public String toString()
   {
      return character + "";
   }
}
