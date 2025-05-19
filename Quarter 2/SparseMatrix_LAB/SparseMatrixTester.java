//    d oberle 2021.
   public class SparseMatrixTester
   {
      public static void main(String[] arg)
      {
         SparseMatrix<String> sm = new SparseMatrix<>(4, 5);
         //sm will be a 4 x 5 array of Strings

         try {
            sm.add(2, 1, "A");
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
         try {
            sm.add(0, 4, "B");
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
         try {
            sm.add(3, 3, "C");
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
         String temp1 = null;			//get the element at row 2, col 1
         try {
            temp1 = sm.get(2, 1);
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
         String temp2 = sm.set(0, 4, "D");	//change the element at row 0, col 4 to a D, return the old value
         String temp3 = sm.remove(3, 3);		//remove the element at row 3, col 3 and return its value
         
         System.out.println(sm);					//show the contents of the sparse matrix
      
         /*
      	should display something like:
      									0 1 2 3 4
      	- - - - D		or		 0         D
      	- - - - -             1                  
      	- A - - - 				 2   A      
      	- - - - -				 3          
      	
      	*/
         
         System.out.println(temp1);		//should be A
         System.out.println(temp2);		//should be B
         System.out.println(temp3);		//should be C
      
      }
   }
