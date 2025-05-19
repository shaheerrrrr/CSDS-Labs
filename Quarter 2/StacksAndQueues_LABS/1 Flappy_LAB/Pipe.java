//Curtis Grover, D Oberle
//Pipe class for the main obstacles for the birds - each pipe has a top half and a bottom half with a gap inbetween
//Pipes in the AIR realm might spit smoke out of the top
import javax.swing.ImageIcon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
   
public class Pipe
{   
   private int x, y1, y2;                             //x-coord of the pipe, and y-coords of the upper part of the pipe and lower part
   protected static int START_WIDTH = 18*5, START_HEIGHT = 50*10;
   private int width, height;                         //dimensions of the obstacle
   protected static final int START_DIST = 285;       //horizontal gap between one pipe and the next  
   protected static final int START_GAP = 250;        //vertical gap between the top and bottom pipes at the start of the game  
   protected static final int MIN_GAP = 150;          //mnumum vertical gap between the top and bottom pipes  
   private int gap;                                   //vertical gap between the top and bottom pipes that the bird flys through - gets smaller as game progresses
   private boolean [] scoreCounted;                   //whether or not player 1 and/or player 2 and/or player 3 have passed through this pipe          
   private ImageIcon[][] pictures = new ImageIcon[3][3];	//array of images for the pipe in each of the 3 realms
   private boolean isPortal;                          //is it a down-portal to the water realm? 
   private boolean isSmoker;                          //if true, this pipe belches smoke in the AIR realm 
   protected static final int FRAMES_NO_SMOKE = 60;   //the number of frames for which there is a break from an AIR realm pipe spitting smoke
   protected static final int [] goodSmokeFrames = {25,50};
   private int smokeFrame;                            //the frame mutiple in which a break from an AIR realm pipe will stop spitting smoke
   private int panelFrame;                            //sent from the panel to know what frame it is on (used to turn smoke on/off)
   private int smokeStop;                             //the frame # that the smoke starts spitting out
   private boolean isSmoking;                         //true if the pipe is spitting smoke

  //pre:  dx is a valid pixel dimension, where dx is the x-coord of where the pipe will be drawn
  //      min >=0, max > min and max < screen height, where min and max are the boundries on the screen where the pipes can start to be drawn   
  //      g > 0 and g < screen height, whereg is the vertical gap size between the top and bottom pipe
   public Pipe(int dx, int min, int max, int g, boolean is)
   {
      assignImages();
      if(g == 0)
      {
         gap = 0;
      }
      else
      {
         gap = Math.min(START_GAP, Math.max(MIN_GAP, g));
      }
      width = START_WIDTH;
      height = START_HEIGHT;
      int rand = (int)(Math.random()*(max-min+1))+min;
      x = dx;
      y1 = rand - height;
      y2 = rand + gap; 
      scoreCounted = new boolean[3];  
      isPortal = false;
      isSmoker = is;
      smokeFrame = goodSmokeFrames[(int)(Math.random()*goodSmokeFrames.length)];
      panelFrame = 0;
      smokeStop = 0;
      isSmoking = false;
   }
     
   //post:  assigns images for the pipes from the assets folder
   private void assignImages()
   {
      pictures[0][FlappyPanel.AIR] = new ImageIcon("assets/pipe/pipeTopAir.png");    
      pictures[1][FlappyPanel.AIR] = new ImageIcon("assets/pipe/pipeBotAir.png");
      pictures[2][FlappyPanel.AIR] = new ImageIcon("assets/pipe/pipeBotAir.png");
      pictures[0][FlappyPanel.LAND] = new ImageIcon("assets/pipe/pipeTop.png");    
      pictures[1][FlappyPanel.LAND] = new ImageIcon("assets/pipe/pipeBot.png");
      pictures[2][FlappyPanel.LAND] = new ImageIcon("assets/pipe/pipePortalDown.png");
      pictures[0][FlappyPanel.WATER] = new ImageIcon("assets/pipe/pipeTopWater.png");    
      pictures[1][FlappyPanel.WATER] = new ImageIcon("assets/pipe/pipeBotWater.png");
      pictures[2][FlappyPanel.WATER] = new ImageIcon("assets/pipe/pipeBotWater.png");
   
   }
   
   //pre:  g != null
   //post: draws the pipe on the screen
   public void drawPipes(Graphics g)
   {
      if(!isPortal)
      {         
         if(FlappyPanel.realm != FlappyPanel.AIR)
         {
            g.drawImage(pictures[0][FlappyPanel.realm].getImage(), x, y1, width, height, null);
         }
         g.drawImage(pictures[1][FlappyPanel.realm].getImage(), x, y2, width, height, null);
      }
      else
      {
         g.drawImage(pictures[2][FlappyPanel.realm].getImage(), x, y2, width, height, null);
      }
   }       
          
   public int getX()
   {
      return x;
   }
   	
   public int getY1()
   {
      return y1;
   }
   
   public int getY2()
   {
      return y2;
   }

   public void setX(int dX)
   {
      x = dX;
   }
   
   public void setY2(int dY)
   {
      y2 = dY;
   }
   
   public int getWidth()
   {
      return width;
   }
   
   public int getHeight()
   {
      return height;
   }
   
   //pre:  index >=0 and index < scoreCounted.length   (index is 0, 1 or 2 for a player index)
   //post:  sets the score counted for a particular player at an index to a particular state
   public void setScoreCounted(int index, boolean state)
   {
      if(index >=0 && index < scoreCounted.length)
      {
         scoreCounted[index] = state;
      }
   }
   
   //post:  sets the score counted for all players to a particular state
   public void setScoreCounted(boolean state)
   {
      scoreCounted[0] = state;
      scoreCounted[1] = state;
      scoreCounted[2] = state;
   }

   //pre:  index >=0 and index < scoreCounted.length   (index is 0, 1 or 2 for a player index)
   //post:  returns whether or not this pipes score has been counted for a particular player at index
   public boolean scoreCounted(int index)
   {
      if(index >=0 && index < scoreCounted.length)
      {
         return scoreCounted[index];
      }
      return false;
   }
   
   //post:  returns if this pipes score has been counted for any player
   public boolean scoreCounted()
   {
      for(int i=0; i<scoreCounted.length; i++)
      {
         if(scoreCounted[i])
         {
            return true;     
         }
      }
      return false;
   }

   //pre:  birdX, birdY, birdW and birdH are all > 0, where they represent the bird location in pixel-space (birdX, birdY) and dimensions of width and height (birdW, birdH)
   //post: returns 1 if the bird collides with the upper-pipe, returns 2 if collides with lower pipe, returns 0 if no collision
   public int collision( int birdX, int birdY, int birdW, int birdH)
   {  //return ((birdX - 9 + birdW > x && birdX < x + width-9) && (birdY < y1 + height || birdY + birdH - 6 > y2));
      if(((birdX - 9 + birdW > x && birdX < x + width-9) && (birdY + birdH - 6 > y2)))
      {
         return 2;
      }    
      if((((birdX - 9 + birdW > x && birdX < x + width-9) && (birdY < y1 + height))) && !isPortal && FlappyPanel.realm != FlappyPanel.AIR)
      {
         return 1;
      }  
      return 0; 
   }
   
   public boolean isPortal()
   {
      return isPortal;
   }
   
   public void setIsPortal()
   {
      isPortal = true;
      width = width * 2;
   }
   
   public boolean isSmoker()
   {
      return (isSmoker && isSmoking);
   }
   
   public void setIsSmoker()
   {
      isSmoker = true;
      isSmoking = true;
   }
   
   public int getSmokeFrame()
   {
      return smokeFrame;
   }
   
   //pre:  sf >= 1 && sf < Integer.MAX_VALUE
   public void setSmokeFrame(int sf)
   {
      smokeFrame = sf;
   }
   
   //pre:  pf is the frame count sent from FlappyPanel.  pf >=0 && pf < Integer.MAX_VALUE.
   //post: records the frame number and if this pipe is from the AIR realm and spits smoke, will start or stop the smoke
   public void setPanelFrame(int pf)
   {
      panelFrame = pf;
      if(isSmoker)
      {
         if(smokeFrame == -1)
         {
            isSmoking = true;
         }
         else if(isSmoking && panelFrame % smokeFrame == 0)
         {
            isSmoking = false;
            smokeStop = panelFrame;
         } 
         else if(!isSmoking && panelFrame >= smokeStop + FRAMES_NO_SMOKE)
         {
            isSmoking = true;
            smokeStop = 0;
         }
      }
   }
   
   @Override
   public String toString()
   {
      return "(" +  (y1+height) + ", " + y2 +")";
   }

}