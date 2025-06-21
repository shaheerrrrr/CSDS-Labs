import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Polynomial
{
   private final int degree;
   private final int derivDegree;
   private final double[] coefficients;
   private final double[] deriv;
   public static final double TOLERANCE = 0.00001;
   public static final int MAXITERATIONS = 10000;

   public Polynomial(double[] coefs)
   {
      coefficients = coefs;
      degree = coefficients.length-1;
      derivDegree = degree-1;
      deriv = new double[degree];
      for (int i = 0; i < degree; i++)
      {
         deriv[i] = coefficients[i] * (degree-i);
      }
   }

   public int getDegree()
   {
      return degree;
   }

   public double getRange()
   {
      double[] absCoefs = new double[getDegree()-1];
      for (int i = 1; i < getDegree(); i++)
      {
         absCoefs[i-1] = Math.abs(coefficients[i]/coefficients[0]);
      }
      double max = Arrays.stream(absCoefs).max().getAsDouble();
      double sum = Arrays.stream(absCoefs).sum();
      return Math.min(Math.max(1, sum), 1+max);
   }

   public double derivRange()
   {
      double[] absCoefs = new double[derivDegree-1];
      for (int i = 1; i < derivDegree; i++)
      {
         absCoefs[i-1] = Math.abs(deriv[i]/deriv[0]);
      }
      double max = Arrays.stream(absCoefs).max().getAsDouble();
      double sum = Arrays.stream(absCoefs).sum();
      return Math.min(Math.max(1, sum), 1+max);
   }

   public double func(double x)
   {
      double sum = 0;
      for (int i = 0; i < coefficients.length; i++)
      {
         sum += coefficients[i]* Math.pow(x, degree-i);
      }
      return sum;
   }

   public double derivFunc(double x)
   {
      double sum = 0;
      for (int i = 0; i < deriv.length; i++)
      {
         sum += deriv[i]* Math.pow(x, derivDegree-i);
      }
      return sum;
   }

   public ArrayList<Double> solve()
   {
      ArrayList<Double> sols = new ArrayList<>();
      if (degree == 1)
         sols = linearSolve();
      else if (degree == 2)
         sols = quadraticSolve();
      else if (degree == 3)
         sols = cubicSolve();
      else
         sols = falsePosition();
      sols.replaceAll(this::newtonsMethod);
      sols.replaceAll(x -> Math.round(x * 1_000_000) / 1_000_000.0);
      return sols;
   }

   public ArrayList<Double> linearSolve()
   {
      ArrayList<Double> sols = new ArrayList<>();
      sols.add(-coefficients[1]/coefficients[0]);
      return sols;
   }

   public ArrayList<Double> quadraticSolve()
   {
      ArrayList<Double> sols = new ArrayList<>();
      double a = coefficients[0];
      double b = coefficients[1];
      double c = coefficients[2];

      double discriminant = Math.pow(b,2)-4*a*c;
      if (discriminant < 0)
         return sols;
      if (discriminant == 0)
      {
         sols.add((-b + Math.sqrt(discriminant))/(2*a));
      }
      else if (b==0)
      {
         sols.add(Math.sqrt(Math.abs(c))/Math.sqrt(Math.abs(a)));
         sols.add(-Math.sqrt(Math.abs(c))/Math.sqrt(Math.abs(a)));
      }
      else if (c==0)
      {
         sols.add(0.0);
         sols.add(-b/a);
      }
      else if (discriminant > 0)
      {
         sols.add((-b + Math.sqrt(discriminant))/(2*a));
         sols.add((-b - Math.sqrt(discriminant))/(2*a));
      }
      return sols;
   }

   public ArrayList<Double> cubicSolve()
   {
      ArrayList<Double> sols = new ArrayList<>();
      double a = coefficients[0];
      double b = coefficients[1];
      double c = coefficients[2];
      double d = coefficients[3];
      double v = 4 * Math.pow(c, 3) + 27 * Math.pow(d, 2);
      if (b == 0 && v > 0) // depressed cubic cardano's formula
      {
         sols.add(Math.cbrt((-0.5*d)+Math.sqrt((Math.pow(d,2)/4)+(Math.pow(c,3)/27))) + Math.cbrt((-0.5*d)-Math.sqrt((Math.pow(d,2)/4)+(Math.pow(c,3)/27))));
      }
      else if (b == 0 && v < 0) // cardanos formula if thingy is less than 0
         return sols;
      else // if not depressed
         sols = falsePosition();
      return sols;
   }

   public ArrayList<Double> falsePosition()
   {
      ArrayList<Double> sols = new ArrayList<>();
      double min = -getRange();
      double max = getRange();
      double step = (max-min)/MAXITERATIONS;

      for (double x1 = min; x1 < max; x1 += step)
      {
         double x2 = x1 + step;
         if (func(x1) * func(x2) < 0)
         {
            double root = fPHelper(x1, x2);

            boolean isNew = true;
            for (double s : sols)
            {
               if (Math.abs(s-root) < TOLERANCE)
               {
                  isNew = false;
                  break;
               }
            }
            if (isNew)
               sols.add(root);
         }
      }
      return sols;
   }

   public double fPHelper(double min, double max)
   {
      double fmin = func(min);
      double fmax = func(max);
      double c = min;
      for (int i = 0; i < MAXITERATIONS; i++)
      {
         c = (min *fmax - max *fmin)/(fmax-fmin);
         double fc = func(c);
         if (Math.abs(fc) < TOLERANCE)
            break;
         if (fmin * fc < 0)
         {
            max = c;
            fmax = fc;
         }
         else
         {
            min = c;
            fmin = fc;
         }
      }
      return c;
   }

   public double newtonsMethod(double x)
   {
      double h = func(x)/derivFunc(x);
      while (Math.abs(h)>= TOLERANCE)
      {
         h = func(x)/derivFunc(x);
         x -= h;
      }
      return x;
   }

   public ArrayList<Double> findExtrema()
   {
      ArrayList<Double> sols = new ArrayList<>();
      if (derivDegree == 0)
         return new ArrayList<>();
      if (derivDegree == 1)
         sols = derivLinear();
      else if (derivDegree == 2)
         sols = derivQuadratic();
      else if (derivDegree == 3)
         sols = derivCubic();
      else
         sols = derivFP();
      sols.replaceAll(x -> Math.round(x * 1_000_000) / 1_000_000.0);
      return sols;
   }

   public ArrayList<Double> derivLinear()
   {
      ArrayList<Double> sols = new ArrayList<>();
      sols.add(-deriv[1]/deriv[0]);
      return sols;
   }

   public ArrayList<Double> derivQuadratic()
   {
      ArrayList<Double> sols = new ArrayList<>();
      double a = deriv[0];
      double b = deriv[1];
      double c = deriv[2];

      double discriminant = Math.pow(b,2)-4*a*c;
      if (b==0 && discriminant >= 0)
      {
         sols.add(Math.sqrt(c)/Math.sqrt(a));
         sols.add(-Math.sqrt(c)/Math.sqrt(a));
      }
      else if (c==0 && discriminant >= 0)
      {
         sols.add(0.0);
         sols.add(-b/a);
      }
      else if (discriminant == 0)
      {
         sols.add((-b + Math.sqrt(discriminant))/(2*a));
      }
      else if (discriminant > 0)
      {
         sols.add((-b + Math.sqrt(discriminant))/(2*a));
         sols.add((-b - Math.sqrt(discriminant))/(2*a));
      }
      return sols;
   }

   public ArrayList<Double> derivCubic()
   {
      ArrayList<Double> sols = new ArrayList<>();
      double a = deriv[0];
      double b = deriv[1];
      double c = deriv[2];
      double d = deriv[3];
      double v = 4 * Math.pow(c, 3) + 27 * Math.pow(d, 2);
      if (b == 0 && v > 0) // depressed cubic cardano's formula
      {
         sols.add(Math.cbrt((-0.5*d)+Math.sqrt((Math.pow(d,2)/4)+(Math.pow(c,3)/27))) + Math.cbrt((-0.5*d)-Math.sqrt((Math.pow(d,2)/4)+(Math.pow(c,3)/27))));
      }
      else if (b == 0 && v < 0) // cardanos formula if thingy is less than 0
         return sols;
      else // if not depressed
         sols = derivFP();
      return sols;
   }

   public ArrayList<Double> derivFP()
   {
      ArrayList<Double> sols = new ArrayList<>();
      double min = -derivRange();
      double max = derivRange();
      double step = (max-min)/MAXITERATIONS;

      for (double x1 = min; x1 < max; x1 += step)
      {
         double x2 = x1 + step;
         if (derivFunc(x1) * derivFunc(x2) < 0)
         {
            double root = fPHelper(x1, x2);

            boolean isNew = true;
            for (double s : sols)
            {
               if (Math.abs(s-root) < TOLERANCE)
               {
                  isNew = false;
                  break;
               }
            }
            if (isNew)
               sols.add(root);
         }
      }
      return sols;
   }

   @Override
   public String toString()
   {
      StringBuilder poly = new StringBuilder();
      for (int i = 0; i < coefficients.length; i++)
      {
         int exp = degree-i;
         if (coefficients[i] == 0)
            continue;
         if (!poly.isEmpty() && coefficients[i] > 0)
            poly.append(" + ");
         if (exp == 1)
            poly.append(coefficients[i]).append("x");
         else if (exp == 0)
            poly.append(coefficients[i]);
         else
            poly.append(coefficients[i]).append("x^").append(exp);
      }
      return poly.toString();
   }

   public String derivToString()
   {
      StringBuilder poly = new StringBuilder();
      for (int i = 0; i < deriv.length; i++)
      {
         int exp = derivDegree-i;
         if (deriv[i] == 0)
            continue;
         if (!poly.isEmpty() && deriv[i] > 0)
            poly.append(" + ");
         if (exp == 1)
            poly.append(deriv[i]).append("x");
         else if (exp == 0)
            poly.append(deriv[i]);
         else
            poly.append(deriv[i]).append("x^").append(exp);
      }
      return poly.toString();
   }
}
