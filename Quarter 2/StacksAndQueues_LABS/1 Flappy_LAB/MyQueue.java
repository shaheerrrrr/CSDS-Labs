import java.util.Iterator;
import java.util.LinkedList;

public class MyQueue<anyType> implements Queueable<anyType>, Iterable<anyType>
{
   private LinkedList<anyType> list;

   public MyQueue()
   {
      list = new LinkedList<>();
   }
   /**
    * lets the client know if the queue has any elements or is empty.
    *
    * @return true if the queue is empty; returns false if the queue has elements stored in it.
    */
   @Override
   public boolean isEmpty() {
      return list.isEmpty();
   }

   /**
    * add x to the end of the queue.
    * precondition: queue is [a1, a2, ..., an].
    * postcondition: queue is [a1, a2, ..., an, x].
    *
    * @param x a non-null E object.
    */
   @Override
   public void add(anyType x) {
      list.add(x);
   }

   /**
    * removes and returns the element at the front of a non-empty queue.
    * precondition: queue is [a1, a2, ..., an].
    * postcondition: queue is [a2, ..., an]; returns a1.
    *
    * @return the value that was removed; returns null if the queue is empty.
    */
   @Override
   public anyType remove() {
      return list.removeFirst();
   }

   /**
    * returns the element at the front of a non-empty queue.
    * precondition: queue is [a1, a2, ..., an].
    * postcondition: returns a1.
    *
    * @return the element at the front of the queue; returns null if the queue is empty.
    */
   @Override
   public anyType peek() {
      return list.peek();
   }

   /**
    * returns the element at the back of a non-empty queue.
    * precondition: queue is [a1, a2, ..., an].
    * postcondition: returns an.
    *
    * @return the element at the back of the queue; returns null if the queue is empty.
    */
   @Override
   public anyType getLast() {
      return list.getLast();
   }

   /**
    * returns the number of logical elements stored in the queue.
    *
    * @return the number of elements added into the queue.
    */
   @Override
   public int size() {
      return list.size();
   }

   /**
    * clears the contents ot the queue, leaving it empty
    */
   @Override
   public void clear() {
      list = new LinkedList<>();
   }

   /**
    * Returns an iterator over elements of type {@code T}.
    *
    * @return an Iterator.
    */
   @Override
   public Iterator<anyType> iterator() {
      return list.iterator();
   }

   public String toString()
   {
      return list.toString();
   }
}
