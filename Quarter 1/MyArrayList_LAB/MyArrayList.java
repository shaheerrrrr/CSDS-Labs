//create your own version of the ArrayList by completing this class definition
//look in ListInterface to see what methods you need to create.

/** Recreating an ArrayList */    
public class MyArrayList<anyType> implements ListInterface<anyType>
{
/** Data field to stores the elements that the client adds with buffer space */
   private Object[] list;
/** Data field to keep track of the number of logical elements added */   
   private int numElements;

/** No-arg constructor starts with a buffer size of 10 */   	
   public MyArrayList()
   {
      list = new Object[10];
      numElements = 0;
   }
 
/** Helper method to double the capacity of list in case we run out of buffer space */   
   private void doubleCapacity()	//private because this is a helper method that need not be used outside the class
   {
      Object[] temp = new Object[numElements];
      if (size() == list.length)
      {
         for (int i = 0; i < numElements; i++)
         {
            temp[i] = list[i];
         }
         list = new Object[numElements*2];
         for (int i = 0; i < numElements; i++)
         {
            list[i] = temp[i];
         }
      }
   }

/** Helper method to cut the list size in half in case we are wasting memory with our buffer space */       
   private void cutCapacity()	//private because this is a helper method that need not be used outside of the class
   {
      Object[] temp = new Object[numElements];
      if (size()  == list.length/2)
      {
         for (int i = 0; i < numElements; i++)
         {
            temp[i] = list[i];
         }
         list = new Object[list.length/2];
         for (int i = 0; i < numElements; i++)
         {
            list[i] = temp[i];
         }
      }
   }

   public boolean add(anyType x)
   {
      if (numElements == list.length)
         doubleCapacity();
      list[numElements] = x;
      numElements++;
      return true;
   }

   /** adds element x to the list at a particular index.
    *  @param  index index >= 0 and index is less than or equal to size().
    *  @param  x a non-null anyType object.
    *  @return true if added correctly (possibly false for containers with restrictions).
    */
   public boolean add(int index, anyType x) throws ArrayIndexOutOfBoundsException
   {
      if (numElements == list.length)
         doubleCapacity();
      for (int i = numElements-1; i >= index; i--)
      {
         list[i+1] = list[i];
      }
      list[index] = x;
      numElements++;
      return true;
   }

   /** returns the number of logical elements stored in the list.
    *  @return the number of elements added into the list.
    */
   public int size()
   {
      return numElements;
   }

   /** changes the element at a specific index to x, returning the element that was originally there.
    *  @param  index index >= 0 and index is less than size().
    *  @param  x a non-null anyType object.
    *  @return the old value that was replaced with x.
    */
   public anyType set(int index, anyType x)
   {
      anyType y = (anyType) list[index];
      list[index] = x;
      return y;
   }

   /** returns the element at a specific index (first element is index 0).
    *  @param  index index >= 0 and index is less than size().
    *  @return the element stored at position index.
    */
   public anyType get(int index)
   {
      return (anyType) list[index];
   }

   /** removes and returns the element at a specific index to x
    *  @param  index index >= 0 and index is less than size().
    *  @return the value that was removed.
    */
   public anyType remove(int index)
   {
      anyType y = (anyType) list[index];
      if (numElements == list.length/2)
         cutCapacity();
      for (int i = index; i < numElements; i++)
      {
         list[i] = list[i+1];
      }
      numElements--;
      return y;
   }


/** Return the logical contents of list as a string
 *  @return  a String showing the contents of the list in the form [a0, a1, a2, ... ,an] where n is (numElements-1)  
 */
   @Override
   public String toString()
   {
      String ans = "[";
      for (int i = 0; i < numElements; i++)
      {
         if (i == numElements-1)
            ans += get(i) + "";
         else
            ans += get(i) + ", ";
      }
      	//add all array elements with a comma separating each, i.e. [A, B, C]
      return ans + "]";
   }    
}