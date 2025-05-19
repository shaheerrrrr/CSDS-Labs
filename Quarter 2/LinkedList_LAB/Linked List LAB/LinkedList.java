import java.util.List;

/**a linked list container - conserves memory (no buffer space)
   d oberle 10/2021.
*/

public class LinkedList<anyType> implements ListInterface<anyType>
{
/** Data field: a reference to the first node in the list */
   private ListNode<anyType> head;
   private int size;

/**
 * No arg constructor initializes the LinkedList to an empty list.
 */
   public LinkedList()
   {
      head = null;
      size = 0;
   }

/**
 * Finds if the LinkedList is empty (true) or contains items (false).
 *
 * @return whether or not the LinkedList is empty.
 */
   public boolean isEmpty()
   {
      return (size() == 0);
   }   

/**
 * Adds a new element to the front of the LinkedList.
 *
 * @param  x a non-null Object.
 */
   public void addFirst(anyType x)				
   {
      ListNode<anyType> l = new ListNode<>(x, head);
      head = l;
      size++;
   }

/**
 * Adds a new element to the end of the LinkedList.
 *
 * @param  x a non-null Object.
 */
   public void addLast(anyType x)
   {
      if (head==null)										//if list is empty
         head = new ListNode<>(x,null);					//head refers to the only node
      else
      {
         ListNode current = head;
         while(current.getNext()!= null)				//make current go to the last element
            current = current.getNext();
         current.setNext(new ListNode<>(x, null));	//make the last element's next become a new ending node
      }
      size++;
   }

/**
 * Retrieve the first node in the LinkedList if the head is not null
 *
 * @return the value of the first node in the List, or null if the head is null
 */
   public anyType getFirst()
   {
      if (head==null)							//if list is empty
         return null;							//this is our flag for an unsuccessful add
      return head.getValue();
   }

/**
 * Retrieve the last node in the LinkedList if the head is not null
 *
 * @return the value of the last node in the List, or null if the head is null
 */
   public anyType getLast() throws Exception
   {
      if (head == null)
         throw new NullPointerException();
      ListNode<anyType> current = head;
      while (current.getNext() != null)
      {
         current = current.getNext();
      }
      return current.getValue();
   }

/**
 * Remove the first node in the LinkedList and return its value if the head is not null
 *
 * @return the value of the node removed from the List, or null if the LinkedList is empty
 */
   public anyType removeFirst() 
   {
      anyType val = head.getValue();
      head = head.getNext();
      size--;
      return val;
   }

/**
 * Remove the last element of the list and return its value if the list is not empty
 *
 * @return the value of the element removed, or null if the list is empty
 */
   public anyType removeLast() throws Exception {
      if (head==null)											//if list is empty
         return null;
      anyType temp = getLast();
      if (head.getNext() == null)							//only one element in the list
         head = null;
      else
      {
         ListNode current = head;							//current will traverse the list
         while(current.getNext().getNext() != null)	//move current to the second to last node
            current=current.getNext();
         current.setNext(null);								//then cap off the end of the new, smaller list with null
      }
      size--;
      return temp;
   }

/**
 * Returns the number of logical elements stored in the LinkedList.
 *
 * @return the size of the LinkedList.
 */
   public int size()
   {
      return size;
   }

/**
 * Finds the Object that resides at a given index
 *
 * @param index a value such that index is greater or equal to 0 and index is less than size()
 * @return the value stored in the node at the given index, or null if the list is empty or invalid index
 */
   public anyType get(int index) throws Exception
   {
      if (index >= size() || index < 0)
         throw new ArrayIndexOutOfBoundsException();
      ListNode<anyType> current = head;
      for (int i = 0; i < index; i++)
      {
         current = current.getNext();
      }
      return current.getValue();
   }	

/**
 * Change the Object that resides at a given index to a new value
 *
 * @param index a value such that index is greater or equal to 0 and index is less than size()
 * @param x a non-null Object
 * @return the old value stored in the node at the given index, or null if the list is empty or invalid index
 */
   public anyType set(int index, anyType x) throws Exception
   {
      if (index >= size() || index < 0)
         throw new ArrayIndexOutOfBoundsException();
      ListNode current = head;
      for (int i = 0; i < index-1; i++)
      {
         current = current.getNext();
      }
      anyType oldVal = (anyType) current.getNext().getValue();
      current.setNext(new ListNode(x, current.getNext().getNext()));
      return oldVal;
   }

/**
 * Add a new element at the end of the list
 *
 * @param x a non-null Object
 * @return true
 */
   public boolean add(anyType x)
   {
      addLast(x);
      return true;			
   }	

/**
 * Add a new element at a given index
 *
 * @param index a value such that index is greater or equal to 0 and index is less than size()
 * @param x a non-null Object
 * @return if the element was added successfully, false if the index is invalid
 */
   public boolean add(int index, anyType x) throws Exception
   {
      if (index >= size() || index < 0)
         throw new ArrayIndexOutOfBoundsException();
      ListNode<anyType> current = head;
      for (int i = 0; i < index-1; i++)
      {
         current = current.getNext();
      }
      current.setNext(new ListNode(x, current.getNext()));
      size++;
      return true;
   }	

/**
 * Remove a node that resides at a given index and return its value
 *
 * @param index a value such that index is greater or equal to 0 and index is less than size()
 * @return the value of the element removed, or null if the list is empty or invalid index
 */
   public anyType remove(int index)		
   {
      if (index >= size() || index < 0)
         throw new ArrayIndexOutOfBoundsException();
      ListNode<anyType> current = head;
      for (int i = 0; i < index-1; i++)
      {
         current = current.getNext();
      }
      ListNode<anyType> a = current.getNext();
      current.setNext(current.getNext().getNext());
      size--;
      return a.getValue();
   }	

/**
 * Returns a String of all the elements in the List in the form [a0, a1, a2, . . . , an-1]
 *
 * @return String of all the list elements separated by a comma
 */
   public String toString()
   {
      String ans = "[";									//start with left bookend						
      ListNode current =  head;
      while(current != null)
      {
         ans += current.getValue().toString();
         current = current.getNext();
         if (current != null)							//don't add comma after the last element
            ans += ",";
      }
      ans += "]";											//end with right bookend
      return ans;
   }
}