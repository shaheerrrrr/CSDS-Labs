/** A single node to be used with a binary-search-tree  d oberle 10/2021 */
public class TreeNode
{
   /** Data filed that stores the Employee id in the node */
   private Employee emp;
   /** Data filed that points to the left-subtree of this node */
   private TreeNode left; 
   /** Data filed that points to the right-subtree of this node */
   private TreeNode right;
   
   /** A 3-arg constructor to set a node's id and left and right pointers 
    *  @param v  what we want to store in the node
    *  @param l  what we want this node's left-pointer to point to
    *  @param r  what we want this node's right-pointer to point to
    */
   public TreeNode(Employee v, TreeNode l, TreeNode r)
   { 
      emp = v;
      left = l; 
      right = r; 
   }
   
   /** A 1-arg constructor to set a node's id where the left and right pointers are null
    *  @param v  what we want to store in the node
    */ 
   public TreeNode(Employee v)
   { 
      emp = v;
      left = null; 
      right = null; 
   }

  /** Accessor method to return the id stored in the node; pre-condition: this is not null
   * @return the id that is stored in the node
   */
   public Employee getValue()
   { 
      return emp;
   }
   
  /** Accessor method to return what the left-pointer points to; pre-condition: this is not null
   * @return the reference that is pointed to by the left-pointer
   */
   public TreeNode getLeft() 
   { 
      return left; 
   }
   
  /** Accessor method to return what the right-pointer points to; pre-condition: this is not null
   * @return the reference that is pointed to by the right-pointer
   */
   public TreeNode getRight() 
   { 
      return right; 
   }

  /** Mutator method to allow the id of the node to be changed to a new id; pre-condition: this is not null
   *  @param v  what we want to change the id to
   */
   public void setValue(Employee v) 
   { 
      emp = v;
   }
   
  /** Mutator method to allow the left-pointer to be changed to point to something new; pre-condition: this is not null
   *  @param l  what we want the left-pointer to point to
   */
   public void setLeft(TreeNode l) 
   { 
      left = l; 
   }
   
  /** Mutator method to allow the right-pointer to be changed to point to something new; pre-condition: this is not null
   *  @param r  what we want the right-pointer to point to
   */
   public void setRight(TreeNode r) 
   { 
      right = r; 
   }
   
  /** Return the id contents of node as a string
   *  @return  a String showing the contents of the id in the node  
   */
   @Override
   public String toString()
   {
      return emp.toString();
   }
}


