//used to represent a row,col pair in a 2-D array, 2-D list or pixel-space 
//as a replacement for a Point which stores doubles and has to be cast to int for those purposes
public class Coord
{
   private int row, col;

   public Coord(int r, int c)
   {
      row = r;
      col = c;
   }
   
   public Coord()
   {
      this(0,0);
   }

   public int getRow()
   {
      return row;
   }

   public int getCol()
   {
      return col;
   }
   
   public void setRow(int r)
   {
      row = r;
   }
   
   public void setCol(int c)
   {
      col = c;
   }
   
   public void setLocation(int r, int c)
   {
      row = r;
      col = c;
   }

   @Override
   public String toString()
   {
      return "[x="+row+",y="+col+"]";
   }

   @Override
   public boolean equals(Object other)
   {
      Coord that = (Coord)other;
      return (this.row == that.row && this.col == that.col);
   }

   @Override
   public int hashCode()
   {
      return this.toString().hashCode();
   }
}