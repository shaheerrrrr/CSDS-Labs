import java.util.LinkedList;

public class MyStack<anyType> implements Stackable<anyType>
{

   private LinkedList<anyType> list;

   public MyStack()
   {
      list = new LinkedList<>();
   }
   /**
    * add x to the top of the stack.
    *
    * @param x a non-null anyType object.
    */
   @Override
   public void push(anyType x) {
      list.push(x);
   }

   /**
    * removes and returns the element at the top of a non-empty stack.
    *
    * @return the value that was removed; returns null if the stack is empty.
    */
   @Override
   public anyType pop() {
      return list.pop();
   }

   /**
    * returns the element at the top of a non-empty stack.
    *
    * @return the element at the top of the stack; returns null if the stack is empty.
    */
   @Override
   public anyType peek() {
      return list.peek();
   }

   /**
    * lets the client know if the stack has any elements or is empty.
    *
    * @return true if the stack is empty; returns false if the stack has elements stored in it.
    */
   @Override
   public boolean isEmpty() {
      return list.isEmpty();
   }

   /**
    * returns the number of logical elements stored in the stack.
    *
    * @return the number of elements added into the stack.
    */
   @Override
   public int size() {
      return list.size();
   }
}
