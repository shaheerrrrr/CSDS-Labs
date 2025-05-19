//Curtis Grover, D Oberle
//The player-controlled fishy looking birds in the game
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class FlappyBird
{   
   private int x, y;                                  //position
   private int width, height;                         //diminsions of the image
   private int velo;                                  //how fast going up(-) vs down(+)
   private double angle;                              //angle (in radians) to display image
   private int score;                                 //the number of pipes we have passed between       
   private static Graphics2D g2d;                     //for drawing roated image
   private ImageIcon[] pictures;                      //array of images for the player animation
   private int imageIndex;                            //the index of what picture we want to show
   private ImageIcon stunnedPicture;                  //picture for when the bird hits an obstacle
   private boolean flying;                            //have we yet to hit an obstacle?
   private String color;                              //color of the bird, "red", "blue" or "yellow"
   private Color glowColor;                           //color of a glow effect we want to put behind the bird after getting a powerup
   private int glowOpacity;                           //the transparentness of the glow effect which will quickly decrease
   protected static final int START_WIDTH = 51;       //dimension of the birds
   protected static final int START_HEIGHT = 36;

    //pre:  c is "yellow", "blue" or "red", x and y are valid pixel coordinates in the panel, w/h ratio is 17:12
    //post: constructs an instance of a flappy bird
   public FlappyBird(String c, int dx, int dy)
   {
      assignImages(c);
      color = c;
      x = dx;
      y = dy;
      width = START_WIDTH;
      height = START_HEIGHT;
      angle = 0;
      score = 0;
      flying = true;
      glowColor = Color.yellow;
      glowOpacity = 0;
   }
   
   //pre:  color is "yellow", "blue" or "red"
   //post: assigns images to the array of animation frames and the image to be used when a bird is stunned
   private void assignImages(String color) 
   { 
      pictures = new ImageIcon[4];
      pictures[0] = new ImageIcon("assets/" + color + "/bird0.png");  
      pictures[1] = new ImageIcon("assets/" + color + "/bird1.png");
      pictures[2] = new ImageIcon("assets/" + color + "/bird2.png");
      pictures[3] = new ImageIcon("assets/" + color + "/bird2.png");
      stunnedPicture = new ImageIcon("assets/" + color + "/birdStunned.png");
      imageIndex = (int)(Math.random()*pictures.length);
   }
   
   //post:  called when the client clicks to flap their wings, sets the y velocity to go up
   public void flap()
   {
      boolean aboveTop = this.isFlying() && (this.getY() < 0);
      if(FlappyPanel.realm == FlappyPanel.AIR && aboveTop)
      {  //if you go above the air level, it bounces you back down
         return;
      }
      velo = -17;
      if(FlappyPanel.realm == FlappyPanel.WATER)
      {
         FlappyPanel.bubbles.add(new Circle(this.getX()+this.getWidth()/2, this.getY()+this.getHeight()/2, (int)(Math.random()*(this.getWidth()/2 + 5 ))+5));
      }
   }
   
   //post:  changes the bird's y-value and angle as gravity pulls it down
   public void gravity()
   {    
      if(velo <= 25)                      //25 is terminal velocity
      {
         velo += 2; 
      }
      if(FlappyPanel.realm == FlappyPanel.WATER)
      {                                      //in the water, we are buoyant, so flapping is done to keep us down
         y -= velo;
      }
      else
      {                                      //change y based on veolocity
         y += velo;
      }
      if(velo > 10)                          //change angle
      {
         if( angle < Math.toRadians(80))
         {
            angle += Math.toRadians(10);
         }
         else
         {
            angle = Math.toRadians(90);
         }
      }
      else if(angle > Math.toRadians(-5))
      {
         angle -= Math.toRadians(15);
      }
      else
      {
         angle = Math.toRadians(-20);    
      }
   }
 
   //pre:   g != null, numFrames >= 0 and numFrames < Integer.MAX_VALUE
   //post:  draws the bird to the graphics object
   public void drawBird(Graphics g, int numFrames) 
   {
      if(glowOpacity > 0)
      {
         g.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), glowOpacity));
         g.fillOval(this.getX(), this.getY(), this.getWidth(), this.getWidth());
      }
      ImageIcon temp = getPicture(numFrames);               //get current picture for animation  
      //create BufferedImage from the ImageIcon
      BufferedImage bImage = new BufferedImage(temp.getIconWidth(), temp.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = bImage.createGraphics();
      g2d.drawImage(temp.getImage(), 0, 0, null);
      g2d.dispose();                                        //doesn't work without this, but not sure why...
      BufferedImage rotatedImage = rotateImage(bImage);     //rotate the image
      g.drawImage(rotatedImage, x, y, width, height, null); //draw the rotated image at correct location
   }

   //pre:  image != null
   //post: rotates the image to a specific angle
   private BufferedImage rotateImage(BufferedImage image) 
   {
      //get dimensions of image (should be the same as width & height but not???)
      int w = image.getWidth();
      int h = image.getHeight();
      
      //get new dimensions with new angle and create new image
      double sin = Math.abs(Math.sin(angle));
      double cos = Math.abs(Math.cos(angle));
      width = (int) (w * cos + h * sin);
      height = (int) (h * cos + w * sin);
      BufferedImage rotated = new BufferedImage(width, height, image.getType());
      
      //roate it about the center
      AffineTransform transform = new AffineTransform();
      transform.translate((width - w) / 2, (height - h) / 2);
      transform.rotate(angle, w / 2, h / 2);
      
      //use 2D grapcis to draw and return the rotated image
      Graphics2D g2d = rotated.createGraphics();
      g2d.drawImage(image, transform, null);
      g2d.dispose();
      return rotated;
   }

   //pre:   numFrames >=0 && numFrames < Integer.MAX_VALUE
   //post:  return the current animation picture
   public ImageIcon getPicture(int numFrames)
   {   
      if(!this.isFlying())
      {
         return stunnedPicture;
      }  
      if( velo >=25)                         //starting or falling with terminal speed straight wings
         return pictures[1];
      else 
      {
         if(numFrames % 10 < 5)            //otherwise alternate every 5 frames
         {
            imageIndex = (imageIndex + 1) % pictures.length; 
         }
         return pictures[imageIndex];
      }
   }  
  
   public int getX()
   {
      return x;
   }
   
   public void setX(int x)
   {
      this.x = x;
   }
   	
   public int getY()
   {
      return y;
   }
   
   public void setY(int y)
   {
      this.y = y;
   }
   
   public int getVelo()
   {
      return velo;
   }
   
   public void setVelo(int v)
   {
      velo = v;
   }
   
   public int getWidth()
   {
      return width;
   }
   
   public int getHeight()
   {
      return height;
   }

   public double getAngle()
   {
      return angle;
   }
   
   public void setAngle(double a)
   {
      angle = a;
   }
   
   public int getScore()
   {
      return score;
   }
   
   public void incrementScore()
   {
      score++;
   }
   
   public void addScore(int x)
   {
      score += x;
   }
   
   public void decrementScore()
   {
      score = Math.max(0, score-1);
   }
   
   public void clearScore()
   {
      score = 0;
   }
   
   public boolean isFlying()
   {
      return flying;
   }
   
   public void setIsFlying(boolean state)
   {
      flying = state;
   }
   
   public String getColor()
   {
      return color;
   }
      
   public void setGlowOpacity(int go)
   {
      glowOpacity = go;
   }
   
   //post:  decreases the opacity of the glow effect so that it fades out
   public void decayGlow()
   {
      glowOpacity = Math.max(0, glowOpacity - 3);
   }
   
   @Override
   public String toString()
   {
      return "(" + x + ", " + y + ")" + width + " x " + height;
   }
}