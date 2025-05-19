//Curtis Grover, D Oberle
//Enemy birds and fish that will subtract points from the player that hits them - used in the AIR and WATER realms
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;

public class Enemy
{
   private int x, y;                                           //location in pixel space
   private int minY, maxY;                                     //the min and max y-values it can move between when bobbing up and down
   private int bobRange;                                       //what range of y-values will it bob up and down?
   private int speed;                                          //the speed in which the enemy bobs up and down
   private boolean movingUp;                                   //true if the enemy is moving up, false if moving down hen bobbing up and down
   private ImageIcon picture;                                  //image of the enemy
   protected static final int START_WIDTH = FlappyBird.START_WIDTH;  //dimension of the enemy
   protected static final int START_HEIGHT = FlappyBird.START_HEIGHT;
   private static final String [] fishPictures = {"assets/fish/fishGreen.png", "assets/fish/fishOrange.png","assets/fish/fishPurple.png"};
   private static final String fishAlpha = "assets/fish/fishAlpha.png";
   private static final String [] birdPictures = {"assets/birds/birdGreen.png", "assets/birds/birdOrange.png","assets/birds/birdPurple.png"};
   private static final String birdAlpha = "assets/birds/birdAlpha.png";
   private boolean foreground;                                 //is this enemy in the foreground and should be drawn larger and after the pipes/powerups?
   private boolean [] playerHits;                              //keep track on whether or not each player has hit this powerup
   private boolean alpha;                                      //is this an alpha-enemy that is an obstacle for the player?
   private int glowOpacity;                                    //opacity of glow effect of alpha enemies
   private boolean glowUp;                                     //is alpha-glow opacity increasing or decreasing?
   private int glowRadius;                                     //radius of the glow around alphas

   public Enemy(int dx, int min, int max)
   {
      if(FlappyPanel.realm == FlappyPanel.WATER)
      {
         picture = new ImageIcon(fishPictures[(int)(Math.random()*fishPictures.length)]);
      }
      else //if(FlappyPanel.realm == AIR)
      {
         picture = new ImageIcon(birdPictures[(int)(Math.random()*birdPictures.length)]);
      }
      x = dx; 
      y = (int)(Math.random()*(max-min+1)) + min;
      bobRange = (int)(Math.random() * (max - min + 1));
      minY = y - bobRange/2;
      maxY = y + bobRange/2;
      movingUp = false;
      if(Math.random() < 0.5)
      {
         movingUp = true;
      }
      speed = (int)(Math.random() * 3)+1;
      foreground = false;
      if(Math.random() < 0.5)
      {
         foreground = true;
      }
      playerHits = new boolean[3];
   }
   
   public Enemy(int dx, int min, int max, boolean isAlpha)
   {
      this(dx, min, max);
      if(isAlpha)
      {
         if(FlappyPanel.realm == FlappyPanel.WATER)
         {
            picture = new ImageIcon(fishAlpha);
         }
         else //if(FlappyPanel.realm == AIR)
         {
            picture = new ImageIcon(birdAlpha);
         }
         alpha = isAlpha;
         glowOpacity = 150;
         glowUp = false;
         glowRadius = START_WIDTH;
      }
   }
   
   public void drawEnemy(Graphics g)
   {
      if(alpha && glowOpacity > 0)
      {
         g.setColor(new Color(255, 160, 0, glowOpacity));
         g.fillOval(this.getX()-(glowRadius/6), this.getY()-(glowRadius/6), glowRadius, glowRadius);
      }
      int sizeAdjust = 3;
      if(!foreground)            //make enemies in the foreground slightly larger, in the background slightly smaller
      {
         sizeAdjust = -3;
      }
      if(alpha)                  //make alphas a little bigger
      {
         sizeAdjust += 3;
      }
      g.drawImage(picture.getImage(), x, y, START_WIDTH + sizeAdjust, START_HEIGHT + sizeAdjust, null);
   }
   
   public void bobUpAndDown(int frameNum)
   {
      if(glowOpacity <= 0)       //reset the glow circle around the alpha
      {
         glowRadius = START_WIDTH;
         glowOpacity = 150;
      }
      else                       //pulse the glow circle around the alpha
      {
         glowOpacity -= 5;
         glowRadius++;
      }
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
   
   //pre:  birdX, birdY is a valid coordinate in pixel-space
   //post: returns true if there is a collision between this enemy and the bird at (birdX, birdY)
   public boolean collision(int birdX, int birdY)
   {
      return (FlappyPanel.distance(birdX, birdY, x, y) < START_WIDTH);
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

   public boolean isInForeground()
   {
      return foreground;
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

   public boolean isAlpha()
   {
      return alpha;
   }
}