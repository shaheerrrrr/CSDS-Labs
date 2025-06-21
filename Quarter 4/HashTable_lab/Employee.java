public class Employee implements Comparable<Employee>
{
   private int idNum;
   private String name;

   public Employee(int id)
   {
      idNum = id;
      name = generateRandomName();
   }

   public Employee(int id, String n)
   {
      idNum = id;
      name = n;
   }

   public int getId()
   {
      return idNum;
   }

   public String getName()
   {
      return name;
   }

   public String generateRandomName()
   {
      StringBuilder s = new StringBuilder();
      int rand = (int)(Math.random()*6)+3;
      for (int k = 0; k < rand; k++)
      {
         if (k%2 == 1)
         {
            char c;
            double r = Math.random();
            if (r < .332) //percentage of vowels that is e
               c = 'e';
            else if (r < .547) //percentage of vowels that is a
               c = 'a';
            else if (r < .743) //percentage of vowels that is o
               c = 'o';
            else if (r < .926) // percentage of vowels that is i
               c = 'i';
            else
               c = 'u';
            s.append(c);
         }
         else
         {
            double r = Math.random();
            char c;
            if (r < .146) // same logic as vowels
               c = 't';
            else if (r < .255)
               c = 'n';
            else if (r < .356)
               c = 's';
            else if (r < .455)
               c = 'h';
            else if (r < .552)
               c = 'r';
            else if (r < .621)
               c = 'd';
            else if (r < .686)
               c = 'l';
            else if (r < .731)
               c = 'c';
            else if (r < .770)
               c = 'm';
            else if (r < .809)
               c = 'w';
            else if (r < .844)
               c = 'f';
            else if (r < .876)
               c = 'g';
            else if (r < .909)
               c = 'y';
            else if (r < .939)
               c = 'p';
            else if (r < .964)
               c = 'b';
            else if (r < .979)
               c = 'v';
            else if (r < .992)
               c = 'k';
            else if (r < .994)
               c = 'j';
            else if (r < .997)
               c = 'x';
            else if (r < .998)
               c = 'q';
            else
               c = 'z';

            if (c == 'q')
            {
               s.append(c).append('u');
               k++;
            }
            else
               s.append(c);
         }
      }
      return s.toString();
   }

   @Override
   public String toString()
   {
      return idNum + " " + name;
   }

   @Override
   public int hashCode()
   {
      return Math.abs(name.hashCode()) % 1000;
   }

   public boolean equals(Employee e)
   {
      return (getId() == e.getId() && getName().equals(e.getName()));
   }

   public int compareTo(Employee e)
   {
      return getName().compareTo(e.getName());
   }
}
