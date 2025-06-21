import java.util.Scanner;

public class PolynomialDriver
{
   public static void main(String[] args)
   {
      Scanner scan = new Scanner(System.in);
      System.out.println("What is the highest degree");
      double[] co = new double[scan.nextInt()+1];
      System.out.println("What are the coefficients (include 0 coefficients)");

      for (int i = co.length-1; i >= 0; i--)
      {
         if (i == 1)
            System.out.println("What is the coefficient of the x term");
         else if (i == 0)
            System.out.println("What is the coefficient of the constant term");
         else
            System.out.println("What is the coefficient of the x^" +(i) + " term");
         co[co.length-1-i] = scan.nextDouble();
      }
      Polynomial poly = new Polynomial(co);
      System.out.println("Your Function:");
      System.out.println(poly);
      System.out.println("Derivative of your Function:");
      System.out.println(poly.derivToString());
      System.out.println("Solutions:");
      System.out.println(poly.solve());
      System.out.println("Extrema (Not specified so either minima or maxima):");
      System.out.println(poly.findExtrema());

   }
}
