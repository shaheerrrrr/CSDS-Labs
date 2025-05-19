//Curtis Grover, D Oberle
//FlappyPanel has game logic and produces the graphics that fill the jFrame
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;                    //there is a swing timer and an awt timer, so we have to be specific and not use .*
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;
import javax.swing.JOptionPane;
  
public class FlappyPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{
   //PANEL AND TIMER DATA-FIELDS
   protected static final int SCREENWIDTH = 500;   //width size of screen to be drawn
   protected static final int SCREENHEIGHT = 650;  //height size of screen to be drawn
   private static final int textSize = 35;         //font size
   private static final int DELAY = 30;	         //#miliseconds delay between each time the screen refreshes for the timer
   private static Timer t;							      //used to control what happens each frame (game code)
   protected static int frame;                     //keeps track of how many frames have elapsed for the timing of events
   //GAME MODE DATA-FIELDS
   private static final int STARTSCREEN=0, GAME=1, GAMEOVER=2, LEADERBOARD=3, REALMSWITCH=4; //different game modes
   private static int gameMode;                    //either STARTSCREEN, GAME, GAMEOVER or LEADERBOARD
   private static int difficulty;                  //0, 1 or 2 for easy, normal or hard
   protected static final String [] difficulties = {"easy  ","normal","hard  "};
   protected static boolean soundOn;               //toggle sound on or off
   protected static boolean pause;                 //toggle pause on/off
   //BIRD-PLAYER DATA-FIELDS 
   private static FlappyBird [] players;           //up to 3 players
   private static String[] colors = {"yellow", "blue", "red"};
   private static ArrayList<String> colorsLeft;    //collection of remaining colors for player 2 after player 1 selects theirs
   private static int best = 0;                    //the best score from the current play session
   private static boolean hover;                   //is the bird hovering prior to the game starting with the first flap?
   private static Point [] startPoints;            //the starting positions of each player
   protected static boolean [] powerupStreak;      //records if we have a perfect run in a powerup stage by getting every powerup of our color and not hitting any wrong ones
   //POWERUP DATA-FIELDS
   private static ArrayList<Powerup> currPowerups; //current powerups that are scrolling across the screen
   private static int powerups;                    //how many powerups have we collected? (can be used to widen the gap between pipes)
   private static int powerupBuff;                 //what will the powerups do for us (give points or increase pipe-gap)
   private static final int PU_ADDPOINTS = 0;      //    every powerup grabbed will add a point 
   private static final int PU_INCREASEGAP = 1;    //    every powerup grabbed will increase the gap between pipes
   private static final int PU_NUM_BUFFS = 2;      //    the number of powerup buff types
   protected static int powerupMode;               //what do we need to do to get a powerup (grab our color or avoid our color), -1 if not active
   protected static final int PU_GETCOLOR = 0;     //    we have to grab a powerup that is our color (others will be an obstacle strike)
   protected static final int PU_AVOIDCOLOR = 1;   //    we have to grab a powerup that is not cour color (our color will be an obstacle strike)
   private static final int PU_NUM_MODES = 2;      //    the number of powerup modes
   private static int powerupFrame;                //the frame in which powerup mode is activated
   private static final int PU_DURATION = 350;     //the # frames that powerup mode is activated
   private static final double PU_PROB = 0.5;      //the probability that we go into powerup mode every 10th pipe
   //LEVEL-SCENERY DATA-FIELDS - PIPES AND FLOOR
   private static MyQueue<Pipe> pipes;             //obstacles collection
   protected static final int MIN_PIPE_DIST = (int)(Pipe.START_WIDTH * 2.5); //the minimum horizontal distance from one pipe to the next
   private static int numPipes;                    //the number of pipes we have passed
   private static int distBetweenPipes;            //make the distance between pipes variable so we can increase the # of pipes as the level advances
   private static Rectangle [] movingFloor = new Rectangle[5];
   private static ImageIcon [] ground =      {new ImageIcon("assets/worlds/airGround.png"),  new ImageIcon("assets/worlds/pipeGround.png"),  new ImageIcon("assets/worlds/waterGround.png")}; 
   //LEVEL-SCENERY DATA-FIELDS - SKY
   private static ImageIcon [] background1 = {new ImageIcon("assets/worlds/airDay.png"),     new ImageIcon("assets/worlds/pipeDay.png"),     new ImageIcon("assets/worlds/waterDay.png")};
   private static ImageIcon [] background2 = {new ImageIcon("assets/worlds/airNight.png"),   new ImageIcon("assets/worlds/pipeNight.png"),   new ImageIcon("assets/worlds/waterNight.png")};
   private static ImageIcon currentBackground;     //will toggle between background1 (day) and background2 (night)
   private static Color [] skyColors = {new Color(132, 217, 250), new Color(32, 97, 160), new Color(2, 77, 130), new Color(0, 10, 50) , new Color(2, 87, 120), new Color(32, 107, 150), new Color(247, 234, 234)};
   private static int skyColorIndex;               //index of a sky color in skyColors array that we are transitioning towards
   private static final Color [] rainbow = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, new Color(200, 0, 255), Color.white};
   private static int rainbowIndex;                //colors and index of sky colors when we are in powerup mode 
   private static Color skyColor;                  //current sky color
   private static ArrayList<Point> stars;          //collection of stars to show at night (alpha-channel will be inverse of sky brightness)
   private static boolean restoreSkyColor;         //should we accelerate color transformation after powerup mode
    //LEVEL-SCENERY DATA-FIELDS - PORTALS AND REALMS
   private static boolean goToUpPortal;            //flag to let us know to switch to the sky-realm
   private static boolean goToDownPortal;          //flag to let us know to switch to the water-realm
   private static boolean portalSpawned;           //true if a portal to another realm has spawned
   protected static int realm;                     //value to have knowledge of what realm we are in (AIR, LAND or WATER) defaulting to LAND
   protected static final int AIR = 0, LAND = 1, WATER = 2;
   private static boolean forceRealmSwitch;        //if true, will block the path so we have to switch realms
   private static ArrayList<Enemy> enemies;        //enemies that fly at you in the AIR and WATER realms
   //EFFECT DATA-FIELDS
   private static ArrayList<Circle> circles;       //smoke puffs from colliding with a pipe or the ground
   protected static ArrayList<Circle> bubbles;     //bubbles made when flapping under water
   protected static ArrayList<Smoke> smokePuffs;   //smoke that emerges from the top of pipes in the AIR realm
   private static int flashFrame;                  //with a collision, we want to flash the sky color for a moment - this keeps track of the frame number we collide on
   private static final int FLASH_DURRATION = 10;  //# milliseconds we want a collision flash to stay
   private static int transitionOpacity;           //used to apply a filter over the screen to transition to other realms
   private static boolean transitionUp;            //when true, opacity is increased.  False, opacity decreases.
   //UI DATA-FIELDS
   private static LinkedList<String> top10;        //top 10 high scores
   private static String message;                  //any text we want to show to the client
   private static int messageTime;                 //the frame number we want the message to stop showing on
   private static int messageColorIndex;           //used to rotate message colors
   private static final int MESSAGE_DURATION = 50; //the number of frames we want the message to show
   private static Button restart = new Button(new Rectangle(SCREENWIDTH/4-10,SCREENHEIGHT/2,115, 50), "PLAY", Color.black, Color.gray, Color.white);
   private static Button leaders = new Button(new Rectangle(SCREENWIDTH/4+115,SCREENHEIGHT/2,155, 50), "LEADERS", Color.black, Color.gray, Color.white);
   private static Button submit = new Button(new Rectangle(SCREENWIDTH-155,SCREENHEIGHT-80,135, 50), "SUBMIT", Color.black, Color.gray, Color.white);
   protected static int mouseX;			            //locations for the mouse pointer
   protected static int mouseY;
          
   public FlappyPanel() throws IOException
   {
      soundOn = Sound.initialize();
      Utilities.initialize();       
      top10 = Utilities.readFile("assets/utilities.txt");
      difficulty = 1;                              //normal difficulty        
      players = new FlappyBird[3];  
      powerupStreak = new boolean[3];
      startPoints = new Point[3];  
      int startX = SCREENWIDTH * 4 / 10;
      int startY = SCREENHEIGHT/2;
      startPoints[0] = new Point(startX, startY);
      startPoints[1] = new Point(startX - FlappyBird.START_WIDTH, startY);  
      startPoints[2] = new Point(startX + FlappyBird.START_WIDTH, startY);
      hover = true;
      initializeGame();
      addMouseListener( this );
      addMouseMotionListener( this );
      t = new Timer(DELAY, new Listener());	
      t.start();
      frame = 0;
   }
   
   //post:  reset to start a new game
   public void reset()
   {
      initializeGame();
      resetBirds();
      Sound.landRealm(Sound.SOUNDEFFECTS);
   }
   
   //post:  restore birds to flying state and starting position for the begining of the game and changing realms
   public static void resetBirds()
   {
      for(int i = 0; i < players.length; i++)
      {
         FlappyBird b = players[i];
         if(b != null)
         {
            Point startPoint = startPoints[i];
            b.setX((int)startPoint.getX() );                         //reset bird
            b.setY( (int)startPoint.getY());   
            b.setAngle(0);
            b.setVelo(0);
            b.setIsFlying(true);
         }
      }
   }
   
   //post:  resets the array to keep track of powerup streaks to see if we have a perfect run through a powerup stage
   public static void resetPowerupStreak()
   {
      for(int i=0; i<powerupStreak.length; i++)
      {
         powerupStreak[i] = true;
      }
   }
   
   //post:  at the start of a new game, initialize the player and pipes
   public static void initializeGame()
   {
      pause = false;
      gameMode = STARTSCREEN;
      distBetweenPipes = Pipe.START_DIST; 
      currPowerups = new ArrayList();
      powerups = 0;
      powerupFrame = 0; 
      powerupBuff = (int)(Math.random() * PU_NUM_BUFFS);
      powerupMode = -1;
      forceRealmSwitch = false;
      resetPowerupStreak();
      skyColorIndex = 0;
      messageColorIndex = 0;
      skyColor = skyColors[skyColorIndex];
      rainbowIndex = (int)(Math.random()*rainbow.length);
      restoreSkyColor = false;
      realm = LAND;
      portalSpawned = false;
      currentBackground = background1[realm];
      goToUpPortal = false;
      goToDownPortal = false;
      transitionOpacity = 0;
      transitionUp = true;
      makeStars();
      pipes = new MyQueue<Pipe>();
      enemies = new ArrayList<Enemy>();
      numPipes = 0;
      placeStartingPipes(); 
      String randColor = colors[(int)(Math.random()*colors.length)];
      colorsLeft = new ArrayList();
      for(int i = 0; i < colors.length; i++)
      {
         if(!colors[i].equals(randColor))
         {
            colorsLeft.add(colors[i]);
         } 
      }
      Point startPoint = startPoints[0];
      players[0] = new FlappyBird(randColor, (int)startPoint.getX(), (int)startPoint.getY()); 
      players[1] = null; 
      players[2] = null;  
      Sound.silence();  
      circles = new ArrayList<Circle>();  
      bubbles = new ArrayList<Circle>();  
      smokePuffs = new ArrayList<Smoke>();
      flashFrame = Integer.MAX_VALUE;
      generateFloor();
      messageTime = 0;
   }
   
   //post:  initialize the floor dimensions - these change when going from AIR realm to LAND for the paralax effect in AIR
   public static void generateFloor()
   {
      for(int i = 0; i < movingFloor.length; i++)
      {
         int floorX = i *(40*4);
         int floorY = 535;
         int floorWidth = 40*4;
         int floorHeight = 6*3;
         movingFloor[i] = new Rectangle(floorX, floorY, floorWidth, floorHeight);
      } 
   }
   
   //post:  add in starting pipes at the beginning of the game or when we switch realms
   public static void placeStartingPipes()
   {
      for(int i= 3; i <= 5; i++)
      {
         int yUp = SCREENHEIGHT - 120 - 210;
         if(realm == AIR)  //there is not a top-pipe in the sky realm, so make sure the bottom pipe is tall enough to stick through the clouds
         {
            yUp -= 120;
         }      
         boolean isSmoker = false;
         double smokerChance = 0.5 + (0.15 * difficulty);                       //higher chance of smoking pipe stacks with higher difficulty
         if(realm == AIR && Math.random() < smokerChance)
         {
            isSmoker = true;
         }          
         Pipe p = new Pipe((i * distBetweenPipes), 60, yUp, Pipe.START_GAP, isSmoker);
         pipes.add(p);
      } 
   }
   
   //pre:   g != null
   //post:  displays all graphics in the panel
   public static void showGraphics(Graphics g)	
   {         
      drawSky(g);                                     //draw sky
      if(gameMode == GAME && frame < messageTime && message != null && message.length() > 0)
      {
         g.setColor(rainbow[messageColorIndex]);
         messageColorIndex = (messageColorIndex + 1) % rainbow.length;	
         g.setFont(new Font("Monospaced", Font.BOLD,(int)(textSize*1.8)));
         int width = g.getFontMetrics().stringWidth(message);
         g.drawString(message, SCREENWIDTH/2-width/2, SCREENHEIGHT/4);
      }
      for(Circle curr: circles)                 
      {                                               //draw smoke puffs from collisions
         g.setColor(curr.getColor());		            
         g.fillOval(curr.getX()-(curr.getRadius()/2), curr.getY()-(curr.getRadius()/2), curr.getRadius(), curr.getRadius());
      }
      for(Smoke curr: smokePuffs)                 
      {                                               //draw smoke puffs from pipes in AIR realm
         curr.drawSmoke(g);
      }
      if(realm == WATER)                              //draw bubbles in the water realm
      {
         Graphics2D g2 = (Graphics2D)g;
         g2.setStroke(new BasicStroke(4));
         for(Circle curr: bubbles)
         {
            g.setColor(Color.cyan);
            g.drawOval(curr.getX()-(curr.getRadius()/2), curr.getY()-(curr.getRadius()/2), curr.getRadius(), curr.getRadius());
         }
      }
      for(int i=players.length-1; i >= 0; i--)
      {
         if(players[i] != null)
         {
            players[i].drawBird(g, frame);
         }                                               //draw FlappyBird(s)
      }
      for(Enemy e:enemies)                               //draw enemies in the background
      {
         if(!e.isInForeground())
         {
            e.drawEnemy(g);
         }
      }
      for(Pipe r:pipes)                                  //draw pipes
      {
         r.drawPipes(g);
      }
      for(Enemy e:enemies)                               //draw enemies in the foreground
      {
         if(e.isInForeground())
         {
            e.drawEnemy(g);
         }
      }
      for(Powerup p: currPowerups)                       //draw powerups
      {
         p.drawPowerup(g);
      }
      if(transitionOpacity > 0)                          //we are transitioning to another realm via a portal so draw a rectangle over the play screen
      {
         g.setColor(new Color(0, 0, 0, transitionOpacity));
         g.fillRect(0, 0, SCREENWIDTH, SCREENHEIGHT-100);
      }
      g.setColor(new Color(222, 216, 150));		         
      g.fillRect(0, SCREENHEIGHT-117, SCREENWIDTH, 117); //draw a bottom rectangle underground to cover bottom of pipes
      for(Rectangle r: movingFloor)                      //draw the moving ground image
      {  
         int newX = (int)(r.getX());  
         int newY = (int)(r.getY());
         int newW = (int)(r.getWidth());
         int newH = (int)(r.getHeight());                //draw based on the rectangles location & size
         int paralaxBoost = 0;                           //increase ground floor movement in AIR realm for paralax effect
         if(realm == AIR)
         {
            newY -= newH*2;
            newH *= 4;
            paralaxBoost = 2;
         }
         g.drawImage(ground[realm].getImage(), newX, newY, newW + paralaxBoost, newH, null);      
      }
      if(realm != AIR)
      {
         g.setColor(Color.black);		                  //draw a black rectangle for ground top         
         g.fillRect(0, SCREENHEIGHT-121, SCREENWIDTH, 4);
      }
      showScores(g);
   
      if(gameMode == GAMEOVER)                           //add buttons to restart or view/add to leaderboard
      { 
         restart.drawButton(g);     
         leaders.drawButton(g);
      }
      else if(gameMode == LEADERBOARD)                   //display box with top 10 scores
      {
         restart.drawButton(g);                          //move to bottom left
         if( Utilities.makeTop10(best, top10) >= 0)      //only show submit button if client makes a top 10 score
         {
            submit.drawButton(g);                        //bottom right
         }
         g.setColor(Color.white);                        //background for top10 text
         g.fillRect(75,100,3*SCREENWIDTH/4,SCREENHEIGHT-200);
         
         Utilities.drawTop10(g, top10, 100, 110+textSize, textSize);        
         Utilities.drawMessage(g, message, 80, 535);
      }
   }
   
   //pre:   g != null
   //post:  display the scores
   public static void showScores(Graphics g)
   {
      int x = textSize, y = textSize;                                //display score
      g.setColor(new Color(255,255,255));	
      g.setFont(new Font("Monospaced", Font.BOLD,textSize));
      if(gameMode == STARTSCREEN)
      {  
         String title = "FLAPPY DERP";
         int width = g.getFontMetrics().stringWidth(title);
         g.drawString(title, SCREENWIDTH/2-width/2, SCREENHEIGHT/4);
         String state = "on ";
         if(!soundOn)
         {
            state = "off";
         }
         title = "(S)ound: "+ state;
         width = g.getFontMetrics().stringWidth(title);
         g.drawString(title, SCREENWIDTH/2-width/2, SCREENHEIGHT - 25 - textSize);
         title = "(D)ifficulty: "+ difficulties[difficulty];
         width = g.getFontMetrics().stringWidth(title);
         g.drawString("(D)ifficulty: "+ difficulties[difficulty], SCREENWIDTH/2-width/2, SCREENHEIGHT - 15);    
      }
      else if(gameMode == REALMSWITCH)
      {
         String realmName = "EARTH";
         if(realm == AIR)
         {
            realmName = "AIR";
         }
         else if (realm == WATER)
         {
            realmName = "WATER";
         }
         String title = realmName + " REALM";
         int width = g.getFontMetrics().stringWidth(title);
         g.drawString(title, SCREENWIDTH/2-width/2, SCREENHEIGHT/4);        
      }
      else                                                              //we are playing the game here
      {
         if(pause)
         {
            String title = "PAUSED";
            int width = g.getFontMetrics().stringWidth(title);
            g.drawString(title, SCREENWIDTH/2-width/2, SCREENHEIGHT/4);
            title = "(P) to unpause";
            width = g.getFontMetrics().stringWidth(title);
            g.drawString(title, SCREENWIDTH/2-width/2, SCREENHEIGHT - 25 - textSize);
         }
         else
         {
            if(powerupMode != -1 && gameMode == GAME)                      //show directions for powerup mode
            {
               String pu_message1 = "POWERUP TIME:";
               if(powerupBuff == PU_ADDPOINTS)
               {
                  pu_message1 = "POWERUP FOR POINTS:";
               }
               else //if(powerupBuff == PU_INCREASEGAP
               {
                  pu_message1 = "POWERUP FOR SPACE:";
               }
               String pu_message2 = "";
               if(powerupMode == PU_GETCOLOR)
               {
                  pu_message2 += "get your color!";
               }
               else //if(powerupMode == PU_AVOIDCOLOR)
               {
                  pu_message2 += "avoid your color!";
               }
               int width = g.getFontMetrics().stringWidth(pu_message1);
               g.drawString(pu_message1, SCREENWIDTH/2-width/2, SCREENHEIGHT - 25 - textSize);
               width = g.getFontMetrics().stringWidth(pu_message2);
               g.drawString(pu_message2, SCREENWIDTH/2-width/2, SCREENHEIGHT - 15);    
            }
            else if(players[1] != null && players[2] == null)              //player 1 and player 2, but no player 3
            {
               String scoreInfo = "Player 1: "+ players[0].getScore();
               int width = g.getFontMetrics().stringWidth(scoreInfo);
               g.drawString(scoreInfo, SCREENWIDTH/2-width/2, SCREENHEIGHT - 25 - textSize);
               g.drawString("Player 2: "+ players[1].getScore(), SCREENWIDTH/2-width/2, SCREENHEIGHT - 15);
            }
            else if(players[1] == null && players[2] != null)              //player 1 and player 3, but no player 2
            {
               String scoreInfo = "Player 1: "+ players[0].getScore();
               int width = g.getFontMetrics().stringWidth(scoreInfo);
               g.drawString(scoreInfo, SCREENWIDTH/2-width/2, SCREENHEIGHT - 25 - textSize);
               g.drawString("Player 3: "+ players[2].getScore(), SCREENWIDTH/2-width/2, SCREENHEIGHT - 15);
            }
            else if(players[1] != null && players[2] != null)              //player 1 and player 2 and player 3
            {
               String scoreInfo = "P2:"+ players[1].getScore() + " P1:"+ players[0].getScore() + " P3:"+ players[2].getScore();
               int width = g.getFontMetrics().stringWidth(scoreInfo);
               g.drawString(scoreInfo, SCREENWIDTH/2-width/2, SCREENHEIGHT - 25 - textSize);
            }
            else if(players[1] == null && players[2] == null)               //only player 1
            {
               String scoreInfo = "Score: "+ players[0].getScore();
               int width = g.getFontMetrics().stringWidth(scoreInfo);
               g.drawString(scoreInfo, SCREENWIDTH/2-width/2, SCREENHEIGHT - 25 - textSize);
               if(players[0].getScore() != best)
               {
                  if(best >= 10)
                     g.drawString("Best : "+ best , SCREENWIDTH/2-width/2, SCREENHEIGHT - 15);
                  else 
                     g.drawString("Best : "+ best , SCREENWIDTH/2-width/2, SCREENHEIGHT - 15);
               }
            }
         }
      }
   }
   
   //pre:   g != null
   //post:  draw sky with a transitioning color or flashing yellow with a collission
   public static void drawSky(Graphics g)
   {
      if(frame < flashFrame + FLASH_DURRATION)
      {  //flash the screen some shade of yellow with a collision
         int red = (int)(Math.random()*55) + 200;
         int green = (int)(Math.random()*55) + 200;
         int blue = 0;
         g.setColor(new Color(red, green, blue));
      }
      else
      {  //just use regular sky color                                          
         g.setColor(skyColor);	                                           
      }
      g.fillRect(0, 0, SCREENWIDTH, SCREENHEIGHT-100);               //draw a blue rectangle for sky 	
      g.drawImage(currentBackground.getImage(), 0, 0, SCREENWIDTH, SCREENHEIGHT-100, null);
      for(Point p:stars)                                             //draw any stars
      {
         int x = (int)p.getX();
         int y = (int)p.getY();
         int skyBrightness = (skyColor.getRed() + skyColor.getGreen() + skyColor.getBlue()) / 3;
         int alpha = 255 - skyBrightness;                            //make the star brightness the inverse of the sky brightness
         Color starColor = new Color(200,200,255,alpha);
         g.setColor(starColor);
         g.fillOval(x, y, 5, 5);
      }     
   }
   
   //post: for all player,s change their y-value, check for and resolve collisions with pipes or the ground
   public static void movePlayers()
   { 
      for(int playerNum = 0; playerNum < players.length; playerNum++)
      {
         FlappyBird p = players[playerNum];
         if(p == null)                         
         {     
            continue;
         }
         boolean pipeCollision = p.isFlying() && checkPipeCollisions(p);
         boolean aboveTop = p.isFlying() && (p.getY() < 0);
         boolean groundCollision = p.isFlying() && (p.getY() >= SCREENHEIGHT - 120 - p.getHeight());
         boolean enemyCollision = p.isFlying() && checkEnemyCollisions(p, playerNum);
         if(realm == AIR && groundCollision) //we are in the air realm and pass below the clouds, so go back to the LAND realm with pipes
         {
            groundCollision = false;
            goToDownPortal = true;
            transitionOpacity = 1;
            transitionUp = true;
            continue;
         }
         else if(realm != AIR && aboveTop) //we are in the land or water realm and breach the surface, so go back to the AIR or LAND realm
         {
            goToUpPortal = true;
            transitionOpacity = 1;
            transitionUp = true;
            continue;
         }
         checkPowerupCollisions(p, playerNum);
         if(pipeCollision || groundCollision || enemyCollision)
         {                                   //collision check with pipes or the ground        
            if(gameMode == GAME)
            {
               p.setVelo(0);                 //stop going up if there is a collision
               if(pipeCollision)
               {
                  Sound.crash(playerNum);
               }
               Sound.bird(playerNum);
               p.setIsFlying(false);
               circles.add(new Circle(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, p.getWidth()));
               flashFrame = frame;
            }
            if(activePlayers() == 0)
            {                                //1 player is stunned, or 2 players and both stunned or 3 players and all stunned
               gameMode = GAMEOVER;
               frame = 0;                    //so starting bird image is chosen
               int red = (int)(Math.random()*55) + 200;
               int green = (int)(Math.random()*55) + 200;
               int blue = 0;                 //we just crashed, so change the sky color to the yellow dflash color so that it can transition back to a normal sky color smoothly
               skyColor = (new Color(red, green, blue));
               flashFrame = Integer.MAX_VALUE;
            }
         }  
         if(groundCollision)                 //once reaching the ground don't go further
            p.setVelo(0);    
         else   
            p.gravity();                     //fall until reach the ground even after collsion
      }
   }
   
   //post: for all players, advance their score if they make it past a pipe and slide the pipes from right to left
   public static void advancePlayerAndPipes()
   {
      movePlayers();   
      for(int i=0; i<players.length; i++)
      {
         FlappyBird p = players[i];
         if(p == null)
         {
            continue;
         }
         p.decayGlow();
         for(Pipe pipe: pipes)
         {
            if(!pipe.scoreCounted(i) && pipe.getX() < p.getX() && p.isFlying())         
            {                                      //update score - we passed through a pipe
               if(!pipe.scoreCounted())            //first time we counted a score for this pipe, so increment the number of pipes passed
               {                                   //used so we can have the probability of going into powerup mode after every 10 pipes passed
                  numPipes++;
               }
               p.incrementScore();
               pipe.setScoreCounted(i, true);      //make sure we don't score this pipe again
               if(((!portalSpawned && powerupMode == -1) || forceRealmSwitch) && numPipes % 10 == 0)
               {
                  if((Math.random() < PU_PROB || (realm != LAND)) && !forceRealmSwitch)          
                  {                                //PU_PROB% chance we go into powerup mode every 10 pipes                  
                     powerupMode = (int)(Math.random() * PU_NUM_MODES);
                     powerupBuff = (int)(Math.random() * PU_NUM_BUFFS);
                     powerupFrame = frame;
                     resetPowerupStreak();
                  }
                  else
                  {
                     Pipe lastPipe = pipes.getLast();
                     if(lastPipe.getY2() > 475)
                     {
                        lastPipe.setY2(475);
                     }
                     if(realm == LAND)
                     {
                        lastPipe.setIsPortal();
                        portalSpawned = true;
                     }
                     else if(forceRealmSwitch)
                     {
                        if(realm == AIR)
                        {
                           lastPipe.setSmokeFrame(-1);
                           lastPipe.setIsSmoker();
                           lastPipe.setY2(150);
                        }
                     }
                  }
               }
            }            
            if(p.getScore() > best)
               best = p.getScore();     
         }
      }
      for(Pipe pipe: pipes)
      {
         pipe.setPanelFrame(frame);
         pipe.setX( (int)(pipe.getX())-5);         //slide pipe across screen 
         boolean addSmoke = pipe.isSmoker();
         if(realm == AIR && addSmoke && frame % 8 == 0)
         {
            boolean bigger = (pipe.getSmokeFrame() == -1);
            smokePuffs.add(new Smoke(pipe.getX()+Smoke.START_WIDTH/2, pipe.getY2(), bigger));
         }   
      }
      for(Circle c:circles)                        //slide any smoke puffs across the screen
      {
         c.setX((int)(c.getX())-5);
      }
      for(Circle c:bubbles)                        //slide any bubbles up and across the screen
      {
         c.setX((int)(c.getX())-5);
         c.setY((int)(c.getY())-8);
      }
      for(Smoke c:smokePuffs)                       //slide any smokePuffs across the screen
      {
         c.setX((int)(c.getX())-5);
      }
      for(int i = currPowerups.size()-1; i>= 0; i--)
      {
         Powerup pu = currPowerups.get(i);
         pu.setX(pu.getX() - 5);                   //slide any powerups up and across the screen
         pu.bobUpAndDown(frame);
         if(pu.getX() < 0 - Powerup.START_WIDTH)   //remove any powerups that go off the screen
         {
            Powerup removed = currPowerups.remove(i);
            for(int playerNum = 0; playerNum < players.length; playerNum++)
            {
               if(players[playerNum] == null || !players[playerNum].isFlying())
               {
                  powerupStreak[playerNum] = false;
               }
               else if(players[playerNum].getColor().equalsIgnoreCase(removed.getColor()) && powerupMode == PU_GETCOLOR)
               {                                  //we missed a powerup that we were supposed to collect
                  powerupStreak[playerNum] = false;
               }
               else if(!players[playerNum].getColor().equalsIgnoreCase(removed.getColor()) && powerupMode == PU_AVOIDCOLOR)
               {                                  //we missed a powerup that we were supposed to collect
                  powerupStreak[playerNum] = false;
               }
            }
            if(powerupMode == -1 && currPowerups.size() == 0)
            {                                      //powerup mode is over and all powerups are off the screen, so see if we had a perfect run
               for(int pnum = 0; pnum < powerupStreak.length; pnum++)
               {
                  if(powerupStreak[pnum] == true)
                  {
                     message = "Perfect run!";
                     messageTime = frame + MESSAGE_DURATION;
                     players[pnum].addScore(5);
                     Sound.perfect(Sound.SOUNDEFFECTS);
                  }
               } 
            }
         }
      }
      for(int i = enemies.size()-1; i>= 0; i--)    
      {
         Enemy e = enemies.get(i);
         e.setX(e.getX() - 8);                     //slide any enemies up and across the screen
         e.bobUpAndDown(frame);
         if(e.getX() < 0 - e.START_WIDTH)          //remove any enemies that go off the screen
         {
            enemies.remove(i);
         }
      }
      if(powerupMode != -1 && currPowerups.size() < 10)
      {                                            //add powerups
         if(currPowerups.size() == 0 || frame % 10 == 0)
         {
            int dx = SCREENWIDTH + (Powerup.START_WIDTH*2);
            if(currPowerups.size() > 0)
            {                                      //if the last powerup has yet to make it on the screen, make the current one a bit further to the right
               Powerup last = currPowerups.get(currPowerups.size()-1);
               if(last.getX() >= SCREENWIDTH)
               {
                  dx = last.getX() + (Powerup.START_WIDTH*2);
               }
            }
            int min = 0;
            int max = 535;
            currPowerups.add(new Powerup(dx, min, max, powerupMode));
         }  
      }
      if(realm == AIR || realm == WATER)           //add enemies in the AIR or WATER realms
      {
         int dx = SCREENWIDTH + (Enemy.START_WIDTH*2);
         if(enemies.size() > 0)
         {                                         //if the last enemy has yet to make it on the screen, make the current one a bit further to the right
            Enemy last = enemies.get(enemies.size()-1);
            int xMult = 11 - difficulty;
            if(realm == AIR && powerupMode != -1)
            {
               xMult /= 2;
            }
            if(last.getX() >= SCREENWIDTH)
            {
               dx = last.getX() + (Enemy.START_WIDTH * xMult) + (int)(Math.random() * (Enemy.START_WIDTH * xMult)) ;
            }
         }
         int min = 0;
         int max = 535;
         boolean isAlpha = false;                  //chance this enemy is an alpha which acts as an obstacle  
         double alphaProb = 0.1 * difficulty;
         if(realm == AIR)
         {
            alphaProb = 0.1 * (difficulty + 1 + (numPipes/10));
         }
         if(Math.random() < alphaProb )
         { 
            isAlpha = true;
         }
         if(realm == WATER || (realm == AIR && powerupMode != -1))
            enemies.add(new Enemy(dx, min, max, isAlpha));
      }
      if(powerupMode != -1 && frame > powerupFrame + PU_DURATION)
      {                                            //deactivate powerup mode if time for it ends
         powerupMode = -1;
         powerupFrame = 0;
         placeStartingPipes();
         restoreSkyColor = true;
         if(realm == AIR)
         {
            for(int i = enemies.size()-1; i>=0; i--)
            {
               Enemy curr = enemies.get(i);
               if(curr.getX() + curr.START_WIDTH >= SCREENWIDTH)
               {
                  enemies.remove(i);
               }
            }
         }
      }
   }
   
   //pre:  x1,y1,x2 and y2 are valid positions in pixel-space
   //post: returns the distance between points (x1,y1) and (x2,y2)
   public static double distance(int x1, int y1, int x2, int y2)
   {
      return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
   }
   
   //returns true if the player collides with any of the pipes
   public static boolean checkPipeCollisions(FlappyBird p)
   {
      for(Pipe r: pipes)
      {  //collisionType == 1 if the bird collides with the upper-pipe, 2 if collides with lower pipe, 0 if no collision
         int collisionType = r.collision(p.getX(), p.getY(), p.getWidth(), p.getHeight());
         if(collisionType != 0)                 //there is a collison of some kind
         {
            if(r.isPortal() && p.isFlying())    //we hit a portal: find out if it is an up or down portal
            {
               if(collisionType == 1)           //collide with upper-pipe
               {
                  goToUpPortal = true;
               }
               else //if(collisionType == 2)    //collide with lower pipe
               {
                  goToDownPortal = true;
               }
               transitionOpacity = 1;           //set to 1 so that a black filter starts to fade the screen out so that it can fade back in with the new level loaded
               return false;
            }
            return true;
         }
      } 
      return false;
   }
   
    //pre:  playerNum >=0 and playerNum < 3 (the number of players)
    //post: handle what happens when a player collides with any of the powerups
   public static void checkPowerupCollisions(FlappyBird p, int playerNum)
   { 
      if(currPowerups.size()==0 || p==null || !p.isFlying())
      {
         return;
      }
      int numPlayers = activePlayers();
      for(int i = currPowerups.size()-1; i>=0; i--)
      {
         Powerup curr = currPowerups.get(i);
         if(curr.collision(p.getX(), p.getY()))
         {
            boolean obstacle = curr.isObstacle(p);
            if(obstacle)
            {                                               //we are supposed to grab our color powerup but hit a different color                            
               if(!curr.getPlayerHit(playerNum))
               {
                  p.decrementScore();
                  circles.add(new Circle(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, p.getWidth(), curr.getColor()));   
                  Sound.powerup(playerNum, false);
                  powerupStreak[playerNum] = false;
               }
               curr.setPlayerHit(playerNum, true); 
            }
            else                                            //grab the powerup
            {
               Sound.powerup(playerNum, true);
               if(powerupBuff == PU_ADDPOINTS)
               {
                  p.incrementScore();
               }
               else                                         //if(powerupBuff == PU_INCREASEGAP)
               {
                  powerups++;                               //this value gets added to the vertical gap that is placed between pipes that we have to fly through
               }
               p.setGlowOpacity(75);
            }
            if(numPlayers > 1 && obstacle)                  //if there is more than 1 player, don't remove the obstacle if they hit the wrong color
            {}
            else
               currPowerups.remove(i);
         }
      } 
   }
   
   //post:  returns true if there are powerups currently active on the screen
   public static boolean powerupsOnScreen()
   {
      return (currPowerups.size() > 0);
   }
   
   //pre:  playerNum >=0 and playerNum < 3 (the number of players)
   //post: handle what happens when a player collides with any of the enemies
   public static boolean checkEnemyCollisions(FlappyBird p, int playerNum)
   { 
      for(Enemy curr: enemies)
      {
         if(curr.collision(p.getX(), p.getY()) && !curr.getPlayerHit(playerNum))
         {
            p.decrementScore();
            circles.add(new Circle(p.getX()+p.getWidth()/2, p.getY()+p.getHeight()/2, p.getWidth(), p.getColor()));   
            Sound.powerup(playerNum, false);
            curr.setPlayerHit(playerNum, true);
            if(curr.getY() > p.getY())                      //enemy is below player, so push player up
            {
               p.setY(p.getY()+5);
            }
            else
            {
               p.setY(p.getY()-5);
            }
            if(curr.isAlpha())
            {
               p.addScore(-4);
            }
         }
      }
      for(Smoke curr: smokePuffs)
      {
         if(curr.collision(p.getX(), p.getY()))
         {
            return true;
         }
      }
      return false;
   }
   
   //post:  move the floor texture from left to right
   public static void slideFloor()
   { 
      for(int i = 0; i < movingFloor.length; i++)           //slide floor
      {
         int paralaxBoost = 0;                              //increase ground floor movement in AIR realm for paralax effect
         if(realm == AIR)
         {
            paralaxBoost = 1;
         }
         int newX = (int)(movingFloor[i].getX())-(5 + paralaxBoost);
         if(newX < 0-40*4)                                  //once off screen-left move back to the end-right
         {
            newX = 4 *(40*4)-(5 + paralaxBoost); 
         }  
         int newY = (int)(movingFloor[i].getY());
         int newW = (int)(movingFloor[i].getWidth());
         int newH = (int)(movingFloor[i].getHeight());         
         movingFloor[i] = new Rectangle(newX, newY, newW , newH);          
      }  
   }

   /**
    * Given Color from and Color to, gives back a new Color that is increment closer between the two
    * @param from  the start color that we are attempting to change in the direction of the Color to
    * @param to    the color that we want to move the Color from towards
    * @param increment  the amount by which the color fields can change in this method call
    * @return  the color that is increment steps different for R,G,B in the Color from going towards the color to 
    */
   public static Color moveColorTowards(Color from, Color to, int increment)
   {
      int ansRed = 0, ansGreen = 0, ansBlue = 0;
   
      int fromRed = from.getRed();
      int fromGreen = from.getGreen();
      int fromBlue = from.getBlue();
   
      int toRed = to.getRed();
      int toGreen = to.getGreen();
      int toBlue = to.getBlue();
   
      if(fromRed < toRed)
         ansRed = Math.min(toRed, fromRed+increment);
      else if(fromRed > toRed)
         ansRed = Math.max(toRed, fromRed-increment);
      else 
         ansRed = toRed;
         
      if(fromGreen < toGreen)
         ansGreen = Math.min(toGreen, fromGreen+increment);
      else if(fromGreen > toGreen)
         ansGreen = Math.max(toGreen, fromGreen-increment);
      else ansGreen = toGreen;
   
      if(fromBlue < toBlue)
         ansBlue = Math.min(toBlue, fromBlue+increment);
      else if(fromBlue > toBlue)
         ansBlue = Math.max(toBlue, fromBlue-increment);
      else
         ansBlue = toBlue;
         
      ansRed = Math.max(0,ansRed);        
      ansRed = Math.min(255,ansRed);
      ansGreen = Math.max(0, ansGreen);
      ansGreen = Math.min(255, ansGreen);
      ansBlue = Math.max(0, ansBlue);
      ansBlue = Math.min(255, ansBlue);
      return new Color(ansRed, ansGreen, ansBlue);
   }

   //pre:   arg frameMultiple - at what multiple of frame values do we want to advance the sky color, frameMultiple >=1 and frameMultiple < Integer.MAX_VALUE
   //       arg transitionMultiple - how much do we want to advance color values in transition from one color to another, transitionMultiple >= 1 and transitionMultiple < 255
   //       arg advanceToNextColor - if true will change to the next color in the array skyColors once it reaches the color at the current index skyColorIndex
   //post:  every so many frames, transition the sky color so that it cycles between day and night
   public static void advanceSkyColor(int frame, int frameMultiple, int transitionValue, boolean advanceToNextColor)
   {
      if(powerupMode != -1)                                 //use rainbow colors if we are in powerup mode
      {
         Color nextColor = rainbow[(rainbowIndex+1)%rainbow.length];
         skyColor = moveColorTowards(skyColor, nextColor, 10);
         if(skyColor.equals(nextColor))
         {
            rainbowIndex = (rainbowIndex+1) % rainbow.length;
         }
      }
      else
         if(frame % frameMultiple == 0)
         {
            Color nextColor = skyColors[(skyColorIndex+1)%skyColors.length];
            skyColor = moveColorTowards(skyColor, nextColor, transitionValue);
            if(advanceToNextColor && skyColor.equals(nextColor))
            {                                               //we arrived at the next color in the colors array, so switch to the next index with wrap-around
               restoreSkyColor = false;
               skyColorIndex = (skyColorIndex + 1) % skyColors.length;
               if(skyColorIndex == skyColors.length - 1)
               {
                  makeStars();
               }
               if(skyColorIndex >= skyColors.length/2-2 && skyColorIndex <= skyColors.length/2+1)
               {
                  currentBackground = background2[realm];
               }
               else
               {
                  currentBackground = background1[realm];
               }
            } 
         }
   }
   
   //post:  at the start of each next day, we will rearrange the stars numbers and positions for variety
   public static void makeStars()
   {
      stars = new ArrayList();
      int numStars = (int)(Math.random() * 5) + 5;
      for(int i = 0; i < numStars; i++)
      {
         int x = (int)(Math.random() * SCREENWIDTH);
         int y = (int)(Math.random() * ((SCREENHEIGHT-100)*0.6));
         stars.add(new Point(x, y));
      }
   }

   //pre:   frame >= 0 and frame < Integer.MAX_VALUE
   //post:  fade out any smoke puffs that exist
   public static void smokeDynamics(int frame)
   {
      int frameMultiple = 5;
      if(frame % frameMultiple == 0)   //every so many frames, fade the smoke out a little
      { 
         for(int i=circles.size()-1; i>=0; i--)    
         {
            Circle curr = circles.get(i);
            curr.age();                //if a smoke puff is totally transparent or has moved off the left side of the screen, remove it
            if(curr.isDead() || curr.getX()+curr.getRadius()*2 < 0)
               circles.remove(i);
         }
         for(int i=bubbles.size()-1; i>=0; i--)    
         {
            Circle curr = bubbles.get(i);
            if(curr.getY()+curr.getRadius()*2 < 0 || curr.getX()+curr.getRadius()*2 < 0)
               bubbles.remove(i);
         }
      }
      for(int i=smokePuffs.size()-1; i>=0; i--)    
      {
         Smoke curr = smokePuffs.get(i);
         curr.smokeRise(frame);
         if(curr.getY() + curr.getWidth() < 0 || curr.getX() + curr.getWidth() < 0)
         {
            smokePuffs.remove(i);
         }
      }
   }

    //post:  returns the number of players that are still active
   public static int activePlayers()
   {
      int count = 0;
      for(FlappyBird b: players)
      {
         if(b != null && b.isFlying())
         {
            count++;
         }
      }
      return count;
   }
   
   //pre:  g != null
   //called every frame
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      showGraphics(g);					      
   }
   
   //post: returns true if we should pause action and input if we have paused the game or are transitioning to another realm via a portal
   public static boolean pauseAction()
   {
      return (pause || goToUpPortal || goToDownPortal);
   }
   
   //post:  adjust the dimming of the filter and transition to another realm by going through an up or down pipe
   public static void transitionRealms()
   {
      if(transitionUp)
      {
         if(transitionOpacity == 255)
         {
            transitionUp = false;            //change to the new realm here   
            if(goToUpPortal)
            {
               if(realm == WATER)
               {
                  realm = LAND;
                  enemies.clear();
                  Sound.landRealm(Sound.SOUNDEFFECTS);
               }
               else if(realm == LAND)
               {
                  realm = AIR;
                  Sound.airRealm(Sound.SOUNDEFFECTS);
               }
               else if(realm == AIR)
               {
                  realm = WATER;
                  Sound.waterRealm(Sound.SOUNDEFFECTS);
               }
               goToUpPortal = false;
            }
            else                             //if(goToDownPortal)
            {
               if(realm == WATER)
               {
                  realm = AIR;
                  Sound.airRealm(Sound.SOUNDEFFECTS);
               }
               else if(realm == LAND)
               {
                  realm = WATER;
                  Sound.waterRealm(Sound.SOUNDEFFECTS);
               }
               else if(realm == AIR)
               {
                  realm = LAND;
                  enemies.clear();
                  Sound.landRealm(Sound.SOUNDEFFECTS);
                  generateFloor();           //restore the floor dimensions to default since they are changed in AIR for paralax motion
               }
               goToDownPortal = false;
            }
            gameMode = REALMSWITCH;
            resetBirds();
            circles.clear();
            bubbles.clear();
            smokePuffs.clear();
            currentBackground = background1[realm];
            if(skyColorIndex >= skyColors.length/2-2 && skyColorIndex <= skyColors.length/2+1)
            {
               currentBackground = background2[realm];
            }
            currPowerups.clear();
            powerupMode = -1;
            pipes.clear();  
            placeStartingPipes(); 
            portalSpawned = false;
            //Sound.silence();
            forceRealmSwitch = false;
         }
         else if(transitionOpacity > 0 && transitionOpacity < 255)
         {
            transitionOpacity = Math.min(255, transitionOpacity + 10);
         }
      }
      else                                   //transitioning opacity down
      {
         if(transitionOpacity > 0)
         {
            transitionOpacity = Math.max(0, transitionOpacity - 10);
         }
         else
         {
            transitionUp = true;
         }
      }
   }
   
   //post:  hover players up and down before game starts or at the start of moving to a new realm
   public static void hoverPlayers()
   {
      for(int i=0; i < players.length; i++)
      {
         FlappyBird b = players[i];
         if(b != null)
         {
            if(hover)                           //hover up and down during startscreen
            {
               b.setY(b.getY()-1);
            }
            else
            {
               b.setY(b.getY()+1);
            }
            Point startPoint = startPoints[i];
            if(b.getY() < (int)startPoint.getY() - 30 && hover || b.getY() > (int)startPoint.getY() && !hover)
            {
               hover = !hover;                  //change direction of hover
            }
         }
      }
   }
   
   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)//this is called for each timer iteration
      {  
         smokeDynamics(frame);
         transitionRealms();
         if(gameMode == GAME && !pauseAction()) //game logic begins    
         {
            frame++;
            if(frame == Integer.MAX_VALUE)      //in case game goes REALLY long, the int doesn't get too big to handle
            {
               frame = 0;
            }    
            advancePlayerAndPipes();
            Pipe nextPipe = pipes.peek();
            if(nextPipe != null && nextPipe.getX() < 0 - nextPipe.getWidth())    
            {                                    //once a pipe goes off screen-left move it back to the end-right  
               if(nextPipe.isPortal())
               {
                  portalSpawned = false;
               }
               pipes.remove();
               if(powerupMode == -1)
               {
                  nextPipe = pipes.peek();
                  Pipe lastPipe = pipes.getLast();
                  if(nextPipe != null && lastPipe != null)
                  {
                     int scoreModifier = (int)(numPipes*2.5) * difficulty;
                     int gapBetweenPipes = (Pipe.START_GAP - scoreModifier + (powerups*2));  //make the vertical gap between pipes dependant on the # pipes passed
                     int distToNextPipe = Math.max(nextPipe.getWidth()*2, distBetweenPipes - scoreModifier);
                     if(Math.random() < 0.25)                                                //25% chance the horiz distance between pipes will be increased
                     {
                        distToNextPipe = Math.min(Pipe.START_DIST, distToNextPipe + (int)(Math.random()*(nextPipe.getWidth()*2)) + (powerups));
                     }
                     if((numPipes + 1) % 10 == 0)                                            //the next pipe is a down portal, so give a little space so we don't see it spawn in since it is larger
                     {
                        if((realm == AIR || realm == WATER) && Math.random() < 0.25)                                   
                        {                                                                    //25% chance every 10 pipes that the path will be blocked, forcing us to another realm
                           forceRealmSwitch = true;
                           gapBetweenPipes = 0;
                           distToNextPipe = (int)(Pipe.START_DIST*1.5);
                        }
                     } 
                     if(Math.random() < 0.25 && lastPipe.isPortal())                         //25% chance the pipe after a portal will block the player, forcing use of the portal
                     {
                        gapBetweenPipes = 0;
                        distToNextPipe = nextPipe.getWidth() * 2;
                     } 
                     distToNextPipe = Math.max(MIN_PIPE_DIST, distToNextPipe);
                     int nextX = lastPipe.getX() +  distToNextPipe;                          //the horizontal distance to the next pipe   
                     int minY = 60;                                                          
                     int maxY = SCREENHEIGHT - 120 - 210;
                     if(realm == AIR)
                     {                          //there is not a top-pipe in the sky realm, so make sure the bottom pipe is tall enough to stick through the clouds
                        maxY -= 120;
                     }
                     boolean isSmoker = false;
                     double smokerChance = 0.5 + (0.25 * difficulty);                       //higher chance of smoking pipe stacks with higher difficulty
                     if(realm == AIR && Math.random() < smokerChance)
                     {
                        isSmoker = true;
                     }    
                     pipes.add(new Pipe(nextX, minY, maxY, gapBetweenPipes, isSmoker));
                  }
               }
            }                                   //make it so that the further we progress, the smaller the gap is between pipes  
            slideFloor();
            int skyColorTransitionSpeed = 1;
            if(powerupMode != -1)               //we are in a powerup mode and sky colors go to rainbow sequence, so transition sky colors faster
            {
               skyColorTransitionSpeed = 2;
            }
            else if(restoreSkyColor)
            {                                   //we came out of powerup mode, so transition back to regular sky color faster
               skyColorTransitionSpeed = 3;
            }
            advanceSkyColor(frame, 5, skyColorTransitionSpeed, true);
         }                                      //end game logic
         else if (gameMode == GAMEOVER)
         {
            movePlayers();  
            advanceSkyColor(frame, 1, 2, false);              
         }
         else if (gameMode == STARTSCREEN || gameMode == REALMSWITCH)
         {
            slideFloor();
            hoverPlayers();
         }
         repaint();  
      }
   }

   //called the moment a mouse is clicked
   public void mouseClicked( MouseEvent e )
   {
      int button = e.getButton();
      if(button == MouseEvent.BUTTON1)
      {
         if(restart.getShape().contains(mouseX, mouseY))
         {                                      //reset button
            if((gameMode == GAMEOVER || gameMode == LEADERBOARD) && restart.getTitle().contains("PLAY"))
            {
               Sound.click(Sound.SOUNDEFFECTS);
               restart = new Button(new Rectangle(SCREENWIDTH/4-10,SCREENHEIGHT/2,115, 50), "PLAY", Color.black, Color.gray, Color.white);
               reset();
            }       
         }
         if(leaders.getShape().contains(mouseX, mouseY))
         {                                      //leaderboard button
            if(gameMode == GAMEOVER && leaders.getTitle().equals("LEADERS"))
            {
               Sound.click(Sound.SOUNDEFFECTS);
               gameMode = LEADERBOARD;
               restart = new Button(new Rectangle(0+10,SCREENHEIGHT-80,115, 50), "PLAY", Color.black, Color.gray, Color.white);
               int i = Utilities.makeTop10(best, top10);
               if(i >= 0)
                  message = "  Best is Top 10!";
               else
                  message = "   Keep Trying!";
            }       
         }
         if(submit.getShape().contains(mouseX, mouseY))
         {                                      //submit button
            if(submit.getTitle().equals("SUBMIT"))
            {
               Sound.click(Sound.SOUNDEFFECTS);
               int i = Utilities.makeTop10(best, top10);
               if(i >= 0)
               {
                  message = " Best is Top 10!";
                  String name = JOptionPane.showInputDialog(null, "Enter your name:");
                  message = Utilities.submitScore(i, best,name, top10, difficulty);
                  Utilities.writeFile("assets/utilities.txt", top10);
               }
               else
                  message = "Best is Not Enough";
               players[0].clearScore();
               best = 0;
            }  
         }
      } 
      else if(button == MouseEvent.BUTTON3)
      {}
      repaint();
   }

   public void mousePressed( MouseEvent e )
   {
      if(gameMode == STARTSCREEN || gameMode == REALMSWITCH)
         gameMode = GAME;     
      if(gameMode == GAME && players[0].isFlying() && !pauseAction())
      {
         players[0].flap(); 
         Sound.flap(Sound.PLAYER1);
      }
   }

   public void mouseReleased( MouseEvent e )
   {
      Sound.silence(Sound.PLAYER1);   
   }

   public void mouseEntered( MouseEvent e )
   {}

   public void mouseMoved( MouseEvent e)
   {
      mouseX = e.getX();
      mouseY = e.getY(); 
      if(leaders.getShape().contains(mouseX, mouseY))
         leaders.highlight();                   //highlight buttons when hovering over them
      else                    
         leaders.unHighlight();  
      if(restart.getShape().contains(mouseX, mouseY))
         restart.highlight();
      else
         restart.unHighlight();  
      if(submit.getShape().contains(mouseX, mouseY))
         submit.highlight();
      else
         submit.unHighlight();
      repaint();			
   }

   public void mouseDragged( MouseEvent e)
   {}

   public void mouseExited( MouseEvent e )
   {}
   
   public void moveMouse(Point p) 
   {}
   
   public void keyTyped(KeyEvent e)         
   {}
   
   //called the moment a key is pressed   
   public void keyPressed(KeyEvent e)
   {
      int key = e.getKeyCode();
      if(key==KeyEvent.VK_ESCAPE)	                   	
         System.exit(1);
      if(key==KeyEvent.VK_S || key==KeyEvent.VK_LEFT) 
      {                                               //toggle sound on or off
         soundOn = !soundOn;
         if(soundOn)
         {
            Sound.click(Sound.SOUNDEFFECTS);
         }
      }   
      else if(gameMode == GAME && (key==KeyEvent.VK_P || key==KeyEvent.VK_DOWN || key==KeyEvent.VK_RIGHT)) 
      {                                               //toggle pause on or off
         pause = !pause;
         if(soundOn)
         {
            Sound.click(Sound.SOUNDEFFECTS);
         }
      } 
      else if(gameMode != GAME && (key==KeyEvent.VK_D || key==KeyEvent.VK_RIGHT))
      {
         difficulty = (difficulty + 1) % 3;
      }
      else if(key==KeyEvent.VK_UP && (gameMode == STARTSCREEN || gameMode == REALMSWITCH))
      {
         gameMode = GAME;
         players[0].flap();
         Sound.flap(Sound.PLAYER1);     
      }
      else if(key==KeyEvent.VK_UP && gameMode == GAMEOVER)
      {
         Sound.click(Sound.SOUNDEFFECTS);
         restart = new Button(new Rectangle(SCREENWIDTH/4-10,SCREENHEIGHT/2,115, 50), "PLAY", Color.black, Color.gray, Color.white);
         reset();
      }
      else if(key==KeyEvent.VK_SPACE || key==KeyEvent.VK_ENTER || key==KeyEvent.VK_UP)	                   	
      {
         if(gameMode == GAME && !pauseAction())
         {
            if(key==KeyEvent.VK_SPACE && players[1] == null)
            {                                   //add in player 1 (space-bar)
               String randColor = colors[(int)(Math.random()*colors.length)];
               if(colorsLeft.size() > 0)        //pick a color that is not the same as p1's color
               {
                  randColor = colorsLeft.remove((int)(Math.random()*colorsLeft.size()));
               }
               int startY = (int)startPoints[1].getY();
               if(players[0] != null && players[0].isFlying())        
               {                                //begin our new player's y-position at the same elevation as the current flying player
                  startY = players[0].getY();
               }
               else if(players[2] != null && players[2].isFlying())
               {
                  startY = players[2].getY();
               }
               players[1] = new FlappyBird(randColor, (int)startPoints[1].getX(), startY);
            }
            else if(key==KeyEvent.VK_SPACE && players[1] != null && players[1].isFlying())              
            {                                   //make sure we only flap if not stunned from hitting an obstacle
               players[1].flap();
               Sound.flap(Sound.PLAYER2);
            }
            else if(key==KeyEvent.VK_ENTER && players[2] == null)
            {                                   //player 2 is adding in to the game (enter-key)
               String randColor = colors[(int)(Math.random()*colors.length)];
               if(colorsLeft.size() > 0)        //pick a color that is not the same as p1's color
               {
                  randColor = colorsLeft.remove((int)(Math.random()*colorsLeft.size()));
               }
               int startY = (int)startPoints[2].getY();
               if(players[0].isFlying())        //begin our new player's y-position at the same elevation as the current flying player
               {
                  startY = players[0].getY();
               }
               else if(players[1] != null && players[1].isFlying())
               {
                  startY = players[1].getY();
               }
               players[2] = new FlappyBird(randColor, (int)startPoints[2].getX(), startY);
            }
            else if(key==KeyEvent.VK_ENTER && players[2] != null && players[2].isFlying())
            {                                   //make sure we only flap if not stunned from hitting an obstacle
               players[2].flap();
               Sound.flap(Sound.PLAYER3);
            }
            else if(key==KeyEvent.VK_UP)
            { 
               if(players[0] != null && players[0].isFlying())
               {                                   //make sure we only flap if not stunned from hitting an obstacle
                  players[0].flap();
                  Sound.flap(Sound.PLAYER1);
               }
            }
         }   
         if(gameMode == GAMEOVER && players[0].getVelo() == 0)
         {
            Sound.click(Sound.SOUNDEFFECTS);
            reset();
         }
      }    
   }
   
   public void keyReleased(KeyEvent e)
   {
      int key = e.getKeyCode();
      if(key==KeyEvent.VK_SPACE)
      {
         Sound.silence(Sound.PLAYER2);
      }
      if(key==KeyEvent.VK_ENTER)
      {
         Sound.silence(Sound.PLAYER3);
      }
      if(key==KeyEvent.VK_UP)
      {
         Sound.silence(Sound.PLAYER1);
      }
   }

}
