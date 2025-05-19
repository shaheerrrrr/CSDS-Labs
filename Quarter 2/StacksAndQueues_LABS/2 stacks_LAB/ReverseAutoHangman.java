import java.util.Scanner;

public class ReverseAutoHangman {

   public static void main(String[] args)
   {
      int score = 0;
      String guess = "";
      int numGuessed = 1;
      StringBuilder gLetters = new StringBuilder();
      Scanner input = new Scanner(System.in);
      System.out.println("give me a word");
      MyStack<String> word = new MyStack<String>();
      guess = input.nextLine();
      for(int i=0; i<guess.length(); i++)
         word.push("" + guess.charAt(i));
      int initialLength = word.size();
      for (int i = 0; i < initialLength-1; i++)
      {
         for (int u = 0; u < initialLength- numGuessed; u++)
         {
            System.out.print("_");
         }
         System.out.println(word.peek() + gLetters);
         gLetters.insert(0, word.peek());
         System.out.println("SCORE: " + score);
         System.out.println("\n What letter do you guess");
         guess = input.nextLine();
         numGuessed++;
         word.pop();
         if (guess.equalsIgnoreCase(word.peek()))
         {
            score += initialLength-i-1;
            System.out.println("Good job u get " + (initialLength-i-1) + " pts");
         }
         else
         {
            score -= numGuessed;
            System.out.println("Bad job u lose " + (numGuessed) + " pts");
         }
      }
      System.out.println("FINAL SCORE: " + score);
   }
}