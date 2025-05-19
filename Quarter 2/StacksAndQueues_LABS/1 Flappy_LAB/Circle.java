//Curtis Grover, D Oberle
//a simple class used for special effects in the game like glowing effects or collision smoke-puffs
import java.awt.Color;
import java.util.Set;
import java.util.TreeSet;
public class Circle 
{   
   private int x, y;                //upper-left corner coordinates of the smallest square around the circle
   private int radius, opacity;
   private Color color;
  
   //pre:  dX,dY are >= 0 and < screen dimensions, dR > 0
   //post: construct a circle with a random color 
   public Circle(int dX, int dY, int dR) {
      x = dX;
      y = dY;
      radius = dR;
      opacity = 255;
      int r = (int)(Math.random() * 255);
      int g = (int)(Math.random() * 255);
      int b = (int)(Math.random() * 255);      
      color = new Color(r,g,b,opacity);
   }
   
   public Circle(int dX, int dY, int dR, String c) 
   {
      this(dX, dY, dR);
      Color temp = Color.blue;
      if(c.equalsIgnoreCase("red"))
      {
         temp = Color.red;
      }
      else if(c.equalsIgnoreCase("yellow"))
      {
         temp = Color.yellow;
      }
      opacity = 127;
      color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), opacity);
   }
   
   //post: increases the radius and makes the circle more transparent (smoke that is fading away)  
   public void age() 
   {
      radius++;
      opacity-=5;
   }
   
   public Color getColor()
   {
      int r = color.getRed();
      int g = color.getGreen();
      int b = color.getBlue();
      return new Color(r, g, b, opacity);
   }
   
   public int getX() 
   {
      return x;
   }
   
   public int getY() 
   {
      return y;
   }
   
   public void setX(int dx)
   {
      x = dx;
   }
   
   public void setY(int dy)
   {
      y = dy;
   }
   
   public int getRadius() 
   {
      return radius;
   }
   
   public int getOpacity() 
   {
      return opacity;
   }
   
   //post: returns true if the circle is completely transparent
   public boolean isDead() 
   {
      if (opacity <= 0) 
      {
         return true;
      } 
      return false;
   }
   
}