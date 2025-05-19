public class Cell<anyType>
{
   private anyType val;
   private int r, c;

   public Cell(anyType value, int row, int col)
   {
      val = value;
      r = row;
      c = col;
   }

   public anyType getVal()
   {
      return val;
   }

   private anyType setVal(anyType x)
   {
      anyType temp = val;
      val = x;
      return temp;
   }

   public int getRow()
   {
      return r;
   }

   public int getCol()
   {
      return c;
   }


   @Override
   public String toString()
   {
      return val.toString();
   }
}
