import java.io.*;
/**a binary-search-tree container d oberle 10/2021.  */  
public class Tree
{
/** Data field: a reference to the first node of the tree. */
   private TreeNode bigRoot;

/** No arg constructor initializes and empty tree. */   
   public Tree()
   {
      bigRoot = null;
   }
   
/**Adds a new element to the tree such that the tree is still an ordered Binary Search Tree.
 * @param  x a non-null Employee Object.
 */   
   public void add(Employee x)
   {
      bigRoot = addHelper(bigRoot, x);
   }
  
/**Helper method for add(x).
 * @param   root is the root of a tree (or subtree for recursive calls). 
 * @param   x a non-null Employee Object.   
 * @return  the root of the ordered binary search tree after x has been added.
 */    
   private TreeNode addHelper(TreeNode root, Employee x)
   {
      if (root == null)
         root = new TreeNode(x);
      else if (root.getValue().compareTo(x) > 0)
         root.setLeft(addHelper(root.getLeft(), x));
      else if (root.getValue().compareTo(x) < 0)
         root.setRight(addHelper(root.getRight(), x));
      return root;
   }
   
/**Removes an element from the tree such that the tree is still an ordered Binary Search Tree.
 * @param  x a non-null Employee Object.
 */   
   public void remove(Employee x)
   {
      bigRoot = removeHelper(bigRoot, x);
   }
   
/**Helper method for remove(x).
 * @param   root is the root of a tree (or subtree for recursive calls).  
 * @param   x a non-null Employee Object. 
 * @return  the root of the ordered binary search tree after x has been removed.
 */   
   private TreeNode removeHelper(TreeNode root, Employee x)
   {
      TreeNode del = searchHelper(root, x);
      TreeNode parentDel = searchParent(root, x);
      if (del == null)
         return root;
      if (isLeaf(del))
      {
         if (parentDel.getLeft() == del)
            parentDel.setLeft(null);
         else if (parentDel.getRight() == del)
            parentDel.setRight(null);
      }
      else if (oneKid(del))
      {
         if (del.getLeft() != null && parentDel.getLeft() == del)
            parentDel.setLeft(del.getLeft());
         else if (del.getRight() != null && parentDel.getRight() == del)
            parentDel.setRight(del.getRight());
         else if (del.getRight() != null && parentDel.getLeft() == del)
            parentDel.setLeft(del.getRight());
         else if (del.getLeft() != null && parentDel.getRight() == del)
            parentDel.setRight(del.getLeft());
      }
      else //two kids
      {
         TreeNode m = findMax(del.getLeft());
         Employee temp = m.getValue();
         removeHelper(del, temp);
         del.setValue(temp);
      }
      return root;
   }

   /** Finds the max element in the tree/subtree
    */
   private TreeNode findMax(TreeNode root)
   {
      TreeNode curr = root;
      while (curr.getRight() != null)
      {
         curr = curr.getRight();
      }
      return curr;
   }

   /** Displays  the elements of the tree such that they are displayed in prefix order. */
   public void showPreOrder()
   {
      preOrderHelper(bigRoot);
      System.out.println();  
   }
   
/**Helper method for showPreOrder().
 * Because the process is recursive and needs to continue by sending subtrees as the next root to process.
 * @param   root is the root of a tree (or subtree for recursive calls).  
 */   
   private void preOrderHelper(TreeNode root)
   {
      if (root != null)
      {
         System.out.print(root.getValue() + " ");
         preOrderHelper(root.getLeft());
         preOrderHelper(root.getRight());
      }

   }
   
/** Displays  the elements of the tree such that they are displayed in infix order. */ 
   public void showInOrder()
   {
      inOrderHelper(bigRoot);
      System.out.println();
   }

/**Helper method for showInOrder().
 * Because the process is recursive and needs to continue by sending subtrees as the next root to process.
 * @param   root is the root of a tree (or subtree for recursive calls).  
 */   
   private void inOrderHelper(TreeNode root)   
   {
      if(root!=null)
      {
         inOrderHelper(root.getLeft());
         System.out.print(root.getValue() + " ");    
         inOrderHelper(root.getRight());
      }
   }
      
/** Displays  the elements of the tree such that they are displayed in postfix order. */ 
   public void showPostOrder()
   {
      postOrderHelper(bigRoot);
      System.out.println();   
   }
   
/**Helper method for showPostOrder(). 
 * Because the process is recursive and needs to continue by sending subtrees as the next root to process.
 * @param   root is the root of a tree (or subtree for recursive calls).  
 */   
   private void postOrderHelper(TreeNode root)
   {
      if (root != null)
      {
         postOrderHelper(root.getLeft());
         postOrderHelper(root.getRight());
         System.out.print(root.getValue() + " ");
      }
   }
   
/**Searches for an element in the tree.
 * @param   x a non-null Employee Object.
 * @return  true if x is found; false if x is not found in the tree
 */    
   public boolean contains(Employee x)
   {
      return searchHelper(bigRoot, x) != null;
   }

   public TreeNode search(Employee x)
   {
      return searchHelper(bigRoot, x);
   }

/**Helper method for contains(x).
 * Because  the process is recursive and needs to continue by sending subtrees as the next root to process.
 * @param   root is the root of a tree (subroots for recursive calls).
 * @param   x a non-null Employee Object.
 * @return  a pointer to the TreeNode that contains the value x; returns null if not found
 */   
   private TreeNode searchHelper(TreeNode root, Employee x)
   {
      if (root == null)
         return null;
      if (root.getValue().equals(x))
         return root;
      if (x.compareTo(root.getValue()) < 0)
         return searchHelper(root.getLeft(), x);
      if (x.compareTo(root.getValue()) > 0)
         return searchHelper(root.getRight(), x);
      return root;
   }
   
/**Helper method for removeHelper(root, x).
 * Because  the process is recursive and needs to continue by sending subtrees as the next root to process.
 * @param   root is the root of a tree (subroots for recursive calls).
 * @param   x a non-null Employee Object.
 * @return  a pointer to the parent of the node that contains the value x; returns null if not found
 */    
   private TreeNode searchParent(TreeNode root, Employee x)
   {
      TreeNode t = null;
      if (root == null)
         return null;
      if (x.compareTo(root.getValue()) < 0 && root.getLeft() != null)
      {
         if (root.getLeft().getValue().compareTo(x) == 0)
            t = root;
         else
            t = searchParent(root.getLeft(), x);
      }
      else if (root.getRight() != null)
      {
         if (root.getRight().getValue().compareTo(x) == 0)
            t = root;
         else
            t = searchParent(root.getRight(), x);
      }
      return t;
   }
   
/**Helper method for removeHelper(root, x).
 * @param   root is the root of a tree.
 * @return  true if root has no children; returns false if root has 1 or 2 children
 */ 
   private boolean isLeaf(TreeNode root)
   {
      return (root.getLeft() == null && root.getRight() == null);
   }
      
/**Helper method for removeHelper(root, x).
 * @param   root is the root of a tree.
 * @return  true if root has exactly one child; returns false if root has 0 or 2 children
 */
   private boolean oneKid(TreeNode root)
   {
      return ((root.getRight() != null && root.getLeft() == null) || (root.getLeft() != null && root.getRight() == null));
   }
      
/**Returns the number of logical elements stored in the tree.
 * @return the number of nodes in the tree.
 */ 
   public int size()
   {
      return sizeHelper(bigRoot);
   }
   
/**Helper method for size().
 * Because  the process is recursive and needs to continue by sending subtrees as the next root to process.
 * @param   root is the root of a tree (or subtree for recursive calls). 
 * @return  the size of the tree that starts at root 
 */    
   private int sizeHelper(TreeNode root)
   {
      if (root == null)
         return 0;
      int count = 1;
      if (root.getRight() != null)
         count += sizeHelper(root.getRight());
      if (root.getLeft() != null)
         count += sizeHelper(root.getLeft());
      return count;
   }

/**Returns the number of levels in the tree.  
 * An empty tree is height -1.  A 1-node tree is height 0.
 * A 2-node tree and a balanced 3-node tree are height 1.
 * @return the height/depth/number of levels of the tree.
 */          
   public int height()
   {
      return heightHelper(bigRoot);
   }

/**Helper method for height().
 * Because  the process is recursive and needs to continue by sending subtrees as the next root to process.
 * @param   root is the root of a tree (or subtree for recursive calls). 
 * @return the height/depth/number of levels of the tree that starts at root.
 */   
   private int heightHelper(TreeNode root)
   {
      if (root == null)
         return -1;
      if (isLeaf(root))
         return 0;
      return 1 + Math.max(heightHelper(root.getLeft()), heightHelper(root.getRight()));
   }
   
/**EXTRA CREDIT: returns true if p is an ancestor of c.
 * @param   root is the root of a tree (or subtree for recursive calls). 
 * @param   p a non-null Employee Object that can be found in the tree. 
 * @param   c a non-null Employee Object that can be found in the tree.  
 * @return  true if p is an ancestor of c; return false if not or one/both can not be found in the tree.
 */    
   private boolean isAncestorHelper(TreeNode root, Employee p, Employee c)
   {
   //************COMPLETE THIS METHOD*****************************   
      return false;     //temporary return statement to keep things compiling
   }
   public boolean isAncestor(Employee p, Employee c)
   {
      return isAncestorHelper(bigRoot, p, c);
   }
   
/**EXTRA CREDIT: displays all elements of the tree at a particular depth/level/height.
 * level 0 will show the root.  level 1 will show all elements that are children of the root.
 * A level that is less than 0 or greater than the max depth will display nothing.
 * @param   root is the root of a tree. 
 * @param   level is the depth in which you want to see all the elements of. 
 */ 
   private void printLevelHelper(TreeNode root, int level)
   {
      
   }
   public void printLevel(int level)
   {
      printLevelHelper(bigRoot, level);
   }
 
/**Helper method for toString().
 * @param   root is the root of a tree (or subtree for recursive calls). 
 */ 
   private String inOrderHelper2(TreeNode root)   
   {
      if(root==null)
      {
         return "";
      }
      else
      {
         return inOrderHelper2(root.getLeft()) + (root.getValue() + ", ") + inOrderHelper2(root.getRight());
      }
   }

/**Returns a String of all the elements in the tree in the form [a1, a2, a3, . . . , an] in order
 * @return String of all the in-oder tree elements separated by a comma
 */
   @Override
   public String toString()
   {
      String temp=inOrderHelper2(bigRoot);
      if(temp.length() > 1)
         temp = temp.substring(0, temp.length()-2);
      return "[" + temp + "]";
   }
}