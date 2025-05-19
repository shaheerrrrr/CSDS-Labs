//Curtis Grover, D Oberle
//A powerup class that allows for items that can be picked up for points or to widen the distance between pipes
import javax.swing.ImageIcon;
import java.awt.Graphics;

public class Powerup
{
   private String color;                                       //"red", "blue", or "yellow"
   private String [] colors = {"red", "blue", "yellow"};       //to make picking a random color easier
   private int x, y;                                           //location in pixel space
   private int minY, maxY;                                     //the min and max y-values it can move between when bobbing up and down
   private int bobRange;                                       //what range of y-values will it bob up and down?
   private int speed;                                          //the speed in which the powerup bobs up and down
   private boolean movingUp;                                   //true if the powerup is moving up, false if moving down hen bobbing up and down
   private ImageIcon picture;                                  //image of the powerup
   private int powerupMode;                                    //which powerup mode it is when generated: PU_GETCOLOR or PU_AVOIDCOLOR
   protected static final int START_WIDTH = FlappyBird.START_HEIGHT;  //dimension of the powerups
   protected static final int START_HEIGHT = FlappyBird.START_HEIGHT;
   private boolean [] playerHits;                              //keep track on whether or not each player has hit this powerup

   public Powerup(int dx, int min, int max, int puMode)
   {
      color = colors[(int)(Math.random()*colors.length)];
      picture = new ImageIcon("assets/powerups/" + color + ".png");
      x = dx; 
      y = Math.abs(max - min)/2;
      bobRange = (int)(Math.random() * (max - min + 1));
      minY = y - bobRange/2;
      maxY = y + bobRange/2;
      movingUp = false;
      if(Math.random() < 0.5)
      {
         movingUp = true;
      }
      speed = (int)(Math.random() * 3)+1;
      powerupMode = puMode;
      playerHits = new boolean[3];
   }
   
   public void drawPowerup(Graphics g)
   {
      g.drawImage(picture.getImage(), x, y, START_WIDTH, START_HEIGHT, null);
   }
   
   public void bobUpAndDown(int frameNum)
   {
      if((y >= maxY && !movingUp) || (y <= minY && movingUp))
      {
         movingUp = !movingUp;
      }
      if(frameNum % 5 == 0)
      {
         if(movingUp)
         {
            y-=speed;
         }
         else if(!movingUp)
         {
            y+=speed;
         }
      }
   }
   
   public boolean collision(int birdX, int birdY)
   {
      return (FlappyPanel.distance(birdX, birdY, x, y) < START_WIDTH);
   }
   
   //pre:  b != null
   //      arg powerupMode is 0 for PU_GETCOLOR or 1 for PU_AVOIDCOLOR
   //post: returns true if the powerup is a collision obstacle for the bird 
   public boolean isObstacle(FlappyBird b)
   {
      if(powerupMode == FlappyPanel.PU_GETCOLOR)
      {
         return !(b.getColor().equalsIgnoreCase(this.getColor()));
      }
      else //if(powerupMode == FlappyPanel.PU_AVOIDCOLOR)
      {
         return (b.getColor().equalsIgnoreCase(this.getColor()));
      }
   }
   
   //pre:  playerNum >=0 and playerNum < 3 (the number of players)
   //post: activates awareness of the player at index playerNum has hit this powerup
   public void setPlayerHit(int playerNum, boolean state)
   {
      if(playerNum >= 0 && playerNum < 3)
      {
         playerHits[playerNum] = state;
      }
   }
   
   //pre:  playerNum >=0 and playerNum < 3 (the number of players)
   //post: returns awareness of whether or not the player at index playerNum has hit this powerup
   public boolean getPlayerHit(int playerNum)
   {
      if(playerNum >= 0 && playerNum < 3)
      {
         return playerHits[playerNum];
      }
      return false;
   }
   
   public int getX()
   {
      return x;
   }
   
   public void setX(int dx)
   {
      x = dx;
   }
   
   public String getColor()
   {
      return color;
   }
}