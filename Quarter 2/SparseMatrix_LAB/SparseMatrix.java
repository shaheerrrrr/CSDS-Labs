import java.util.LinkedList;

public class SparseMatrix<anyType> implements Matrixable<anyType>
{
   private LinkedList<Cell<anyType>> list;

   private final int numRows;
   private final int numCols;
   private char blank = '-';


   public SparseMatrix (int r, int c)
   {
      list = new LinkedList<Cell<anyType>>();
      numRows = r;
      numCols = c;
   }


   /**
    * returns the element at row r, col c.
    *
    * @param r the row location; r >=0 and r is less than the number of rows.
    * @param c the column location; c >=0 and c is less than the number of columns.
    * @return the element at row r, column c, return null if r or c are invalid.
    */
   @Override
   public anyType get(int r, int c) throws Exception
   {
      if ((r<0 || r >= numRows) || (c<0 || c >= numCols))
         throw new ArrayIndexOutOfBoundsException();
   
      int key = getKey(r, c);
      for (int i = 0; i < list.size(); i++)
      {
         Cell<anyType> current = list.get(i);
         int currKey = getKey(current.getRow(), current.getCol());
         if (key == currKey)
            return list.get(i).getVal();
      }
      return null;
   }

   /**
    * changes the element at row r, col c to a new value, returning the old value that was there.
    *
    * @param r the row location; r >=0 and r is less than the number of rows.
    * @param c the column location; c >=0 and c is less than the number of columns.
    * @param x a non-null anyType object
    * @return the element that was at row r, column c before it was changed to x, return null if r or c are invalid.
    */
   public anyType set(int r, int c, anyType x)
   {
      if ((r<0 || r >= numRows) || (c<0 || c >= numCols))
         return null;
   
      anyType returnVal = null;
      int wanted = getKey(r,c);
      for (int i = 0; i < list.size(); i++)
      {
         int key = getKey(list.get(i).getRow(), list.get(i).getCol());
         if (wanted == key)
         {
            returnVal = list.get(i).getVal();
            list.set(i, new Cell<>(x,r,c));
         }
      }
      return returnVal;
   }

   public int getKey(int r, int c)
   {
      return (r*numCols) + c;
   }
   /**
    * adds a new element at row r, col c.
    *
    * @param r the row location; r >=0 and r is less than the number of rows.
    * @param c the column location; c >=0 and c is less than the number of columns.
    * @param x a non-null anyType object
    * @return true if r and c are valid and the item was added, false if r or c are out-of-bounds with no item added
    */
   @Override
   public boolean add(int r, int c, anyType x) throws Exception
   {
      if ((r<0 || r >= numRows) || (c<0 || c >= numCols))
         return false;
      Cell<anyType> toAdd = new Cell<>(x, r, c);
      int addKey = getKey(r, c);
      for (int i = 0; i < list.size(); i++) //doesnt work when size == 0
      {
         Cell<anyType> curr = list.get(i);
         int currKey = getKey(curr.getRow(), curr.getCol());
         if (addKey < currKey) {
            list.add(i, toAdd);
            return true;
         }
      }
      list.add(toAdd);
      return true;
   }

   /**
    * removes and returns the element at row r, col c.
    *
    * @param r the row location; r >=0 and r is less than the number of rows.
    * @param c the column location; c >=0 and c is less than the number of columns.
    * @return the element that was at row r, column c before it was removed, return null if r or c are invalid.
    */
   public anyType remove(int r, int c)
   {
      if ((r<0 || r >= numRows) || (c<0 || c >= numCols))
         return null;
   
      anyType removed = null;
      int wanted = getKey(r,c);
      for (int i = 0; i < list.size(); i++)
      {
         int key = getKey(list.get(i).getRow(), list.get(i).getCol());
         if (wanted == key)
         {
            removed = list.get(i).getVal();
            list.remove(i);
            break;
         }
      }
      return removed;
   }

   /**
    * returns the number of logical elements added to the matrix.
    *
    * @return the number of logical elements added to the matrix.
    */
   public int size()
   {
      return list.size();
   }

   /**
    * returns the number of rows in the matrix.
    *
    * @return the number of rows in the matrix.
    */
   public int numRows()
   {
      return numRows;
   }

   /**
    * returns the number of columns in the matrix.
    *
    * @return the number of columns in the matrix.
    */
   public int numColumns()
   {
      return numCols;
   }

   @Override
   public int[] getLocation(anyType x) {
      return new int[0];
   }

   @Override
   public boolean contains(anyType x)
   {
      return false;
   }

   @Override
   public void clear()
   {
      list.clear();
   }

   @Override
   public boolean isEmpty()
   {
      return list.isEmpty();
   }

   @Override
   public anyType[][] toArray()
   {
      anyType[][] arr = (anyType[][]) new Cell[numRows][numCols];
      for (int i = 0; i < list.size(); i++)
      {
         Cell cell = null;
         try
         {
            cell  = list.get(i);
         }
         catch (Exception e)
         {
            throw new RuntimeException();
         }
         arr[cell.getRow()][cell.getCol()] = (anyType) cell;
      }
      return (anyType[][]) arr;
   }

   public void setBlank(char newBlank) //allows the client to set the character that a blank spot in the array is and represented by in String toString()
   {
      blank = newBlank;
   }


   public String toString()
   {
      StringBuilder ans = new StringBuilder();
      for (int r = 0; r < numRows(); r++)
      {
         for (int c = 0; c < numColumns(); c++)
         {
            anyType current = null;
            try
            {
               current = this.get(r, c);
            }
            catch (Exception e)
            {
               throw new RuntimeException(e);
            }
            if (current == null)
               ans.append(blank);
            else
               ans.append(current);
         }
         ans.append("\n");
      }
      return ans.toString();
   }
}
