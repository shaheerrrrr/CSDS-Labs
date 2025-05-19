//Curtis Grover, D Oberle
//Smoke objects are obstacles in the AIR realm, spit out of the pipes
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Smoke
{
   private int x, y;                                        //location in pixel space
   protected static final int START_WIDTH = FlappyBird.START_WIDTH, START_HEIGHT = FlappyBird.START_WIDTH;
   public int width, height;                                //smoke puff dimensions
   private static final ImageIcon [] pictures = {new ImageIcon("assets/smoke/smoke0.png"), new ImageIcon("assets/smoke/smoke1.png"), new ImageIcon("assets/smoke/smoke2.png"),
                                                new ImageIcon("assets/smoke/smoke3.png"), new ImageIcon("assets/smoke/smoke4.png"), new ImageIcon("assets/smoke/smoke5.png"),
                                                new ImageIcon("assets/smoke/smoke6.png"), new ImageIcon("assets/smoke/smoke7.png")};
   private static int pictureIndex;                         //index of current picture from pictures array
   private static int animationSpeed;                       //the frame multiple that we want to advance the animation frames
   private static boolean bigger;                           //bigger smoke to block access
  
   public Smoke(int dx, int dy, boolean b)
   {
      x = dx; 
      y = dy; 
      pictureIndex = (int)(Math.random()*pictures.length);
      animationSpeed = (int)(Math.random() * 3) + 6;
      width = START_WIDTH;
      height = START_HEIGHT;
      bigger = b;
   }
         
   public void drawSmoke(Graphics g)
   {
      g.drawImage(getPicture().getImage(), x, y, width , height, null);
   }
   
   public ImageIcon getPicture()
   {
      if(FlappyPanel.frame % animationSpeed == 0)
      {
         pictureIndex = (pictureIndex + 1) % pictures.length;
      }
      return pictures[pictureIndex];
   }
          
   //pre:  birdX, birdY is a valid coordinate in pixel-space
   //post: returns true if there is a collision between this smoke puff and the bird at (birdX, birdY)
   public boolean collision(int birdX, int birdY)
   {
      return (FlappyPanel.distance(birdX, birdY, x, y) < (int)(START_WIDTH*0.7));
   }
   
   public void smokeRise(int frame)
   {
      y -= 3;
      if( x + width > FlappyPanel.SCREENWIDTH)
      {                          //get smoke to rise faster while it is off the screen to get it going by the time it is visible
         y -= 2;
      }
      if(Math.random() < 0.5)    //random drift to the side
      {
         x--;
      }
      double roll = Math.random();
      if(roll < 0.2 || (bigger && roll < 0.6))
      {
         width += (int)(Math.random()*2)+1;
         height+= (int)(Math.random()*2)+1;
      }
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
   
   public int getWidth()
   {
      return width;
   }
   
   public int getHeight()
   {
      return height;
   }
}