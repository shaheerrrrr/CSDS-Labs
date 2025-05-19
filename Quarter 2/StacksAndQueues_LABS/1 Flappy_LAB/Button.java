//Curtis Grover, D Oberle
//a Button class that has data fields and abilities for dynamic buttons in a game compatible with a MouseListener
import java.awt.Color;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics;
import javax.swing.ImageIcon;
public class Button
{
   private Shape shape;       //We can send any class that implements java.awt.Shape interface
   private String title;      //The title is the text we want drawn in the Button (if any).
   private Color color, regularColor, highlightColor, textColor;
   /*color is the current color of the Button which will change from regularColor to highlightColor as the mouse moves over it*/
   private ImageIcon image, regularImage, highlightImage;
   /*using graphic images for the button instead of color*/
   
   public Button(Shape s, String t, Color rc, Color hc, Color tc)
   {
      shape = s;
      title = t;
      regularColor = rc;
      highlightColor = hc;
      textColor = tc;
      color = regularColor;
      regularImage = null;
      highlightImage = null;
      image = null;
   }
   
   public Button(Shape s, String t, ImageIcon ri, ImageIcon hi)
   {
      shape = s;
      title = t;
      regularColor = null;
      highlightColor = null;
      textColor = null;
      regularImage = ri;
      highlightImage = hi;
      image = regularImage;
   }
   
   public Shape getShape()
   {
      return shape;
   }
   
   public String getTitle()
   {
      return title;
   }
   
   public Color getColor()
   {
      return color;
   }
   
   public Color getRegularColor()
   {
      return regularColor;
   }
   
   public Color getHighlightColor()
   {
      return highlightColor;
   }
   
   public Color getTextColor()
   {
      return textColor;
   }
   
   public ImageIcon getImageIcon()
   {
      return image;
   }
   
   public ImageIcon getRegularImage()
   {
      return regularImage;
   }

   public ImageIcon getHighlightImage()
   {
      return highlightImage;
   }

/*These methods will be called in the mouseMoved method from the MouseListener interface.  
If the mouseX and mouseY positions are within the Button's shape, 
we can call highlight() which changes the color to the highlightColor*/
   public void highlight()
   {
      color = highlightColor;
      image = highlightImage;
   }
   
   public void unHighlight()
   {
      color = regularColor;
      image = regularImage;
   }
   
   public void drawButton(Graphics g)
   {
      int borderWidth = 3;
      int x = (int)(this.getShape().getBounds().getX());
      int y = (int)(this.getShape().getBounds().getY());
      int width = (int)(this.getShape().getBounds().getWidth());
      int height = (int)(this.getShape().getBounds().getHeight());
      g.setColor(this.getHighlightColor());
      g.fillRect(x, y, width, height);
      g.setColor(this.getColor());	
      if(image != null)
         g.drawImage(image.getImage(), x, y, width, height, null);  
      else if(this.getShape() instanceof Rectangle)         
         g.fillRect(x+borderWidth, y+borderWidth, width-(borderWidth*2), height-(borderWidth*2));
      else       
         g.fillOval(x, y, width, height);
      if(image == null)
      {   
         g.setColor(this.getTextColor());	           
         g.drawString(this.getTitle(), x+5, y+(height)-15);
      }
   }
   
   public void setShape(Shape s)
   {
      shape = s;
   }
   
   public void setTitle(String t)
   {
      title = t;
   }
   
   public void setRegularColor(Color rc)
   {
      regularColor = rc;
   }
           
   public void setHighlightColor(Color hc)
   {
      highlightColor = hc;
   }
   
   public void setTextColor(Color tc)
   {
      textColor = tc;
   }
   
   public String toString()
   {
      Rectangle r = shape.getBounds();
      return "X:"+r.getX()+",Y:"+r.getY()+" "+title+"\nColor:"+regularColor+" Highlight"+highlightColor+" text"+textColor; 
   }
}