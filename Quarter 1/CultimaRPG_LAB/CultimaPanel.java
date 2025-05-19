import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.HashMap;
import java.io.File;
import java.io.PrintWriter;
import javax.sound.midi.*;

public class CultimaPanel extends JPanel
{ 
//**SOUND**
   protected static boolean soundOn = true;                 //set to false if your computer does not have an audio driver
   protected static boolean soundInitialized = false;       //will let us know if we need to initialize the sound fields when toggling sound on or off

//**MAP**
   protected static int SIZE=26;	                           //size of cell being drawn
   protected static final int MAP_SIZE=1000;                //map will be MAP_SIZE*MAP_SIZE
   protected static final int SCREEN_SIZE = 25;             //how many SCREEN_SIZExSCREEN_SIZE map tiles we show in the main game screen
   protected static ArrayList<String>mapFiles;              //List of all map file names in addition to world.txt (castles, towns, etc)
   protected static ArrayList<byte[][]> map;                //main world map is at index 0, other elements for towns, caves, etc
   protected static double revealedMap;                     //#of revealed map cells

   protected static ArrayList<Terrain> allTerrain;          //collection of all Terrain for maps, where elements are stored in the index that is the same as the absolute vaue of its byte value
   protected static ArrayList<Location> allLocations;       //list of all Locations placed on the world map
   protected static ArrayList<Location> allDomiciles;       //the elements of allLocations that include villages, towers, castles, forts and cities
   protected static ArrayList<Location> allAdventure;       //the elements of allLocations that include caves, mines and lairs
   protected static ArrayList<Location> allTemples;         //the elements of allLocations that include temples
   protected static ArrayList<Location> allBattlefields;    //the elements of allLocations that include battlefields
   protected static ArrayList<Location> allCamps;           //the elements of allLocations that include soldier camps

   protected static LinkedList<RestoreItem> doorsToClose;   //doors that we opened that we need to remember to close after we leave the map
   protected static RestoreItem doorToClose;                //if we are next to an open door, give option to close it
   protected static ArrayList<RestoreItem> tilesToRestore;  //chests and broken walls that we took that we need to restore after a certain number of days
   protected static ArrayList<RestoreItem> eggsToHarvest;   //collection of eggs (bird, snake, dragon)
   protected static ArrayList<RestoreItem> barrelsToRestore;//barrels that get broken and eventually replaced
   protected static RestoreItem[] ourBeds;                  //keeps track of the location of beds in our house so we can move them around
   protected static ArrayList<NPCPlayer> NPCsToRestore;     //NPCs that died that we need to eventually repopulate a city after a certain number of days
   protected static ArrayList<ArrayList<Corpse>> corpses;   //corpses that are left after fighting - disappear after 3 days, indexed by the mapIndex that they are left in
   protected static ArrayList<Coord> blocked, darkened;	   //points on the screen and map that should be hidden because they are vision blocked or in darkness  
   protected static LinkedList<SmokePuff> smoke;            //puffs of smoke from active fires
   protected static Color groundColor;                      //current color of the ground - changes with the seasons
 
//**WEATHER**
   protected static byte weather;                           //0-clear, 1-light rain to 10-thunderstorm
   protected static double rain_probability;                //the probability that rain starts, changed each new day
   protected static byte windDir;                           //the direction that rapid water pushes you, set when the map is created
   protected static int rainbowFrame;                       //for timing of fading rainbows in and out
   protected static int rainbowAlpha;                       //opacity of an active rainbow
   protected static int fogFrame;                           //for timing of fading fog in and out
   protected static int fogAlpha;                           //opacity of an active fog
//**PUZZLES**
   protected static Coord well1, well2;                     //used to store the location of two wells the player can draw from in the 3-Wells-Puzzle in the Underworld
   protected static Coord door1, door2, door3;              //used to store the location of three doors the player can pick from in the 3-Doors-Puzzle in a dungeon
   protected static int[] tower1, tower2, tower3;           //used to store the disks for the 3-towers puzzle in a cave

//**SHOPPE ITEMS**
   protected static ArrayList<Item> allArmoryInventory;     //all armor and weapons that can be bought in an armory
   protected static ArrayList<Item> allMagicInventory;      //all spells and potions that can be bought in the magic shoppe
   protected static ArrayList<Item> armoryInventory;        //available armor and weapons that can be bought in an armory (some items only available on certain days)
   protected static ArrayList<Item> magicInventory;         //available spells and potions that can be bought in the magic shoppe
   protected static ArrayList<Item> traderInventory;        //available items bought from a traveling trader
   protected static ArrayList<Item> wardrobeInventory;      //items stored in your home's wardrobe (weapons, armor, potions)
   protected static ArrayList<Integer> wardrobeFreq;        //frequencies of items in wardrobeInventory
   protected static int invIndex, itemIndex;                //index of current item to examine in the shoppe inventory, or item scrolling or mission scrolling
   public static boolean shoppeDiscount;                    //for charmed shoppekeeps to give a 20% discount

//**PLAYER**    
   protected static Player player;                          //our player
   protected static int mapR, mapC;                         //center point of where we are scrolling when we are looking at our map
   protected static String typed, lastTyped;                //messages we type to talk to NPCs
   protected static boolean talkSel, attackSel, attackSelAlt, examineSel, stealSel, breakSel, fireSel, pushSel, renovateSel, buildSel;       //we are trying to talk, examine, break or attack, so the next key should be an arrow to make our selection
   protected static String[] conversation = {"affliction","arena","arms","beast","bounty","bribe","cave","city","crafting","cure","death","dog","dungeon", "egg", "future", "gem","gold","goodbye","<<handshake>>","heal","hearth","hello","home","horse","house","<<hug>>","job","lair","legend","location","lockpick","map","marry","message","mission","name","number","pass","puzzle","rainbow","ration","roll","sewer","skara brae","ship","shop", "<<slap>>","spell","swine","tax","townsfolk","trade","train","trap","unicorn","vampire","weapon","weather","werewolf","yes"};
   protected static byte conversationIndex;                                      //index for scrolling through conversation words
   protected static String[] command = {"attack","come","go","stay","treat"};    //commands for a trained dog
   protected static byte commandIndex;                                           //index for scrolling through dog commands
      
   protected static LinkedList<Mission> missionStack;       //missions that the player has started and not yet completed
   protected static byte[] missionsGiven;                   //mission types given to player , 0 or 1 for true or false 
   protected static int numChants;                          //to count the number of chants from cultists for the Ceremony mission
   protected static int monsterDist;                        //used to keep track of the distance (time to arrival) between a monster king and a city for the Kaiju mission
   protected static int mineCollapseTimer;                  //used for the timer for a mine collapse
                                                                                        
   protected static int[] closeTrapOrSecret;                //[0]-the distance from the closest trap, trapped chest or door, and [1]-the distance from the closest secret wall                                                                                                             

   protected static int swineNpcSum, swinePlayerSum;        //sum for each player in Swine dice game played in taverns
   protected static int swineNpcTempSum, swinePlayerTempSum;//temporary sum for each player in Swine dice game played in taverns before banked, or lost by rolling a 1
   protected static int swineTurn;                          //0 if not playing, 1 if player's turn, 2 if npc's turn
   protected static int swineNpcNumRolls;                   //used for npc Swine strategy

   protected static byte [] achievements;                   //array of achievements we have earned. (Not part of player becuase we can accumulate achievements across play with different characters)

//**PLAYER JOURNAL**
   protected static Object[] journal;							   //personal journal that builds as player acquires information
   protected static byte journalChapterIndex;   		  	   //what chapter are we viewing
   protected static byte journalPageIndex;   		     	   //which page are we on

//**PLAYER CHARACTER BUILDER**
   protected static int selAttribute;                       //what is the current attribute that we want to modify?
   protected static String CBname;                          //character builder name
   protected static byte CBimageIndex;                      //character builder imageIndex (starting weapon)
   protected static int CBmight, CBmind, CBagility, CBawareness, CBaffliction, CBPoints;  //character builder player attributes (points to distribute add to 50)
   protected static final int CB_MAX_POINTS = 50;           //the maximum number of points we get to distribute among attribuites in the character builder
   protected static Item CBspell1, CBspell2;                //if we select a class that casts spells, these will store the client choices for which spells to start with
   protected static int CBspellIndex1, CBspellIndex2;       //indexes for spell selection
 
//**NPC and MONSTERS**
   protected static final int MONSTER_SPAWN_PROB = 2;       //% chance a world monster spawns with each move
   protected static final int WATER_SPAWN_PROB = 3;         //% chance a water monster spawns with each move
   protected static ArrayList<ArrayList<NPCPlayer>> civilians;       //inhabitants of towns, indexed by their location index
   protected static ArrayList<ArrayList<NPCPlayer>> furniture;       //barrels and stuff in towns
   protected static boolean NPCsInBed,NPCsOnStreet;         //set to true when NPCs turn in for the night
   protected static NPCPlayer selected;                     //NPC selected to talk to or attack
   protected static NPCPlayer selectedBarrel;               //a barrel to push or pull
   protected static Terrain selectedTerrain;                //terrain selected to examine
   protected static RestoreItem selectedRenovate;           //information for a terrain cell in our house to change (R)enovate 
   protected static RestoreItem selectedBuild;              //information for a terrain cell where we want to build a bridge (B)uild 

   protected static HashSet<NPCPlayer> NPCinSight,          //there is an NPC within sight-line to report stealing/lockpicking/attacking
                                       AllNPCinSight,       //also includes statues and monoliths
                                       AllNPCinRange;       //all NPCs within the screen range
   protected static ArrayList<NPCPlayer> worldMonsters;     //monsters and travelers that are currently in the world that we are navigating around
   protected static ArrayList<NPCPlayer> boats;             //stores the boats that spawn in the world
   protected static boolean orcArmy, trollArmy, brigandArmy, wolfPack, armyGeneral, deaditeArmy;   //to govern packs/armies of monsters you might run across
   protected static final byte NUM_WORLD_MONSTERS = 3;      //number of simultanerous world monsters on screen unless there is an army
   protected static byte nextMonsterSpawn;                  //used for future prediction from NPC mage, reset to -1 when entering a location
   
//**IMAGES**
   protected static final ImageIcon none = new ImageIcon("images/none.jpg");		            	//no graphics found image
   protected static final ImageIcon water = new ImageIcon("images/terrain/water/water2b.Gif");  //half-submerged in shallow water image
   protected static final ImageIcon swamp = new ImageIcon("images/terrain/bogb.Gif");		      //quarter-submerged in swamp image
   protected static final ImageIcon poison = new ImageIcon("images/terrain/poisonb.Gif");		   //quarter-submerged in poison swamp image
   protected static final ImageIcon grass = new ImageIcon("images/terrain/tallgrassb.Gif");     //standing in tall grass
   protected static final ImageIcon forest = new ImageIcon("images/terrain/forest2b.Gif");      //standing in forest
   
   protected static final ImageIcon hit = new ImageIcon("images/hit.Gif");                      //show if player got hit
   protected static final ImageIcon miss = new ImageIcon("images/miss.Gif");                    //show if attack missed or was blocked
   protected static final ImageIcon web = new ImageIcon("images/web.Gif");                      //show if player is trapped by a web
   protected static final ImageIcon spear = new ImageIcon("images/items/spear.gif");            //items thrown and missed that land on the floor
   protected static final ImageIcon dagger = new ImageIcon("images/items/dagger.gif");
   protected static final ImageIcon [] windDirs = {new ImageIcon("images/windDir/north.Gif"),   //wind directions
                                                   new ImageIcon("images/windDir/east.Gif"),
                                                   new ImageIcon("images/windDir/south.Gif"),
                                                   new ImageIcon("images/windDir/west.Gif"),
                                                   new ImageIcon("images/windDir/none.Gif")};          
   protected static final ImageIcon[] clothes =   {new ImageIcon("images/characters2/player/clothes/boots.Gif"),  //clothes for player portrait
                                                   new ImageIcon("images/characters2/player/clothes/chain.Gif"),  //[1]
                                                   new ImageIcon("images/characters2/player/clothes/crown.Gif"),  //[2]
                                                   new ImageIcon("images/characters2/player/clothes/helm.Gif"),   //[3]                                                                                      new ImageIcon("images/characters2/player/clothes/chain.Gif"),
                                                   new ImageIcon("images/characters2/player/clothes/leather.Gif"),//[4]
                                                   new ImageIcon("images/characters2/player/clothes/pelt.Gif"),   //[5]
                                                   new ImageIcon("images/characters2/player/clothes/plate.Gif"),  //[6]
                                                   new ImageIcon("images/characters2/player/clothes/robe.Gif"),   //[7]
                                                   new ImageIcon("images/characters2/player/clothes/scale.Gif"),  //[8]
                                                   new ImageIcon("images/characters2/player/clothes/cape.Gif")};  //[9]
                                                   
   protected static final ImageIcon [] missionArrows = {new ImageIcon("images/missionArrows/up.Gif"),   //wind directions
                                                   new ImageIcon("images/missionArrows/upRight.Gif"),
                                                   new ImageIcon("images/missionArrows/right.Gif"),
                                                   new ImageIcon("images/missionArrows/downRight.Gif"),
                                                   new ImageIcon("images/missionArrows/down.Gif"),
                                                   new ImageIcon("images/missionArrows/downLeft.Gif"),
                                                   new ImageIcon("images/missionArrows/left.Gif"),
                                                   new ImageIcon("images/missionArrows/upLeft.Gif")};
   protected static Color flashColor;                    //flashing the screen with a color for special effects
   protected static int flashFrame;                      //frame in which flash occurs
   protected static int flashRadius;					      //size of the flash around the player

//**TIME**
   protected static double time;                         //time of day
   protected static int days;                            //number of days
   protected static final double WORLD_TIME_INCREMENT = 0.05; //the amount of time that is added to the time of day when we move over normal terrain in the world
   protected static final double LOC_TIME_INCREMENT = 0.01;    //the amount of time that is added to the time of day when we move over normal terrain in a location
   protected static final int MILLIS_BETWEEN_SPELLS = 1000;    //the # milliseconds delay between casting spells
   protected static long spellTime;                      //the time from System.currentTimeMillis() that we cast a spell so we can't spam them too fast
   protected static long cheerTime;                      //the time from System.currentTimeMillis() that the last cheer was made, so that they don't come too fast
   protected static long alertTime;                      //the time from System.currentTimeMillis() that the last guard alert was made
   protected static long gallopTime, gallopTime2;        //the time from System.currentTimeMillis() to control the sound of a horse gallop
   protected static long shipGrappleTime;                //the time from System.currentTimeMillis() that we leave a ship battle, so we don't enter another one too soon
   protected static boolean isNight;                     //true if it is night-time hours

   private Timer t;							                  //used to set the speed of the animation frames
   protected static int animDelay;                       //the number of frames between switching animation frames
   protected static int numFrames;                       //the number of frames that have passed to control animation delay
   protected static int numMoves;                        //count the number of moves we make to time messages about terrain narratives
   protected static boolean shiftPressed;                //is SHIFT being held down?
   protected static boolean altPressed;                  //is ALT being held down?

//**MENU**
   protected static int menuMode;                        //keeps track of what screen mode we are in (MAIN menu, TRAVEL game screen, player INVENTORY, etc)
   protected static final byte MAIN = 0;                 
   protected static final byte TRAVEL = 1;
   protected static final byte INVENTORY = 2;
   protected static final byte HOTKEYS = 3;
   protected static final byte MAP = 4;
   protected static final byte SHOPPE_MAGIC = 5;
   protected static final byte SHOPPE_ARMORY = 6;
   protected static final byte SHOPPE_TRADER = 7;
   protected static final byte WARDROBE = 8;
   protected static final byte JOURNAL = 9;
   protected static final byte README = 10;
   protected static final byte CHARACTER_BUILDER = 11;

//**INTERACTABLES/AWARENESS UPDATES AFTER EACH FRAME/MOVE**
   protected static ArrayList<NPCPlayer> NPCsInOurMap, humanNPCsInOurMap, nonAliveNPCsInOurMap;
   protected static HashSet<String> missionTargetNames;
   protected static NPCPlayer[] adjacentTalkableNPCs, adjacentMonsters;
   protected static NPCPlayer getInRangeToFeed, getDeathAbout, monsterAndCivInView, monsterForGuardTarget, ourDog, ourNpc, onUs, missionTarget, greatestNPCinSight;
   protected static byte numNPCsToTalk, assassinAbout;
   protected static boolean nextToNPCtoPickpocket, inRangeToAttack, inRangeToAttackAlt, zombieAbout, phantomAbout, werewolfAbout, thereIsDemonSwinePlayer;
   protected static int numNPCsInLoc, numWorldMonstersToExclude, distToClosestAwareNPC, numBattleFieldEnemies, numEnemiesOnShip;
   protected static boolean humanNPCInSight, awareHumanNPCInSight, brigandInSight, guardsOnAlert, ghostInView;
   protected static Corpse onACorpse, onAnyCorpse;
   protected static String postedBounty;                 //every two days, refresh which bounty is posted
   protected static int caerbannogRow, caerbannogCol;    //location of the cave of Caerbannog
   protected static boolean caerbannogBeastSpawn;        //whether or not the best of Caerbannog has spawned
   protected static double distToLute;                   //distance from a musician to govern the volume of music we hear

   protected static String [] message;                   //text messages to be shown in the lower right corner (dialogue, game cues, etc)
   protected static String lastMessage;                  //used to make sure the same messages don't repeat back-to-back

   protected static final boolean ENCODE_FILES = false;  //do we want to encode the player game files?
      
   public CultimaPanel()
   {   
      numFrames = 0;
      animDelay = 10; 
      menuMode = MAIN; 
      allTerrain = Terrain.loadTerrain();
      allArmoryInventory = Utilities.loadArmory();
      allMagicInventory = Utilities.loadMagicShoppe();
      if(soundOn)
      {
         Sound.initialize();
      }
      restartGame();
      t = new Timer((animDelay*3), new Listener());			
      t.start();
   }

   public static void restartGame()
   {
      boolean newCharacter = false;
      menuMode = MAIN;   
      time = 9;
      invIndex = 0;
      itemIndex = 0;
      shoppeDiscount = false;
      shiftPressed= false;
      altPressed= false;
      conversationIndex = (byte)(conversation.length - 1);
      commandIndex = (byte)(command.length - 1);
      numMoves = 0;
      numChants = -1;
      mineCollapseTimer = -1;
      monsterDist = -1;
      caerbannogRow = -1;
      caerbannogCol = -1;
      caerbannogBeastSpawn = false;
      doorToClose = null;
      ourBeds = null;
   
      flashColor = null;
      flashFrame = 0;
      flashRadius = 0;
   
      typed = "";
      lastTyped = "";
      armoryInventory = new ArrayList<Item>();
      magicInventory = new ArrayList<Item>();
      traderInventory = Utilities.loadTraderShoppe();
      wardrobeInventory = new ArrayList<Item>();
      wardrobeFreq = new ArrayList<Integer>();
      missionTargetNames = new HashSet();
   
      closeTrapOrSecret = new int[2];
      closeTrapOrSecret[0] = -1;
      closeTrapOrSecret[1] = -1;
      NPCinSight = new HashSet<NPCPlayer>();
      AllNPCinSight = new HashSet<NPCPlayer>();
      AllNPCinRange = new HashSet<NPCPlayer>();
      worldMonsters = new ArrayList<NPCPlayer>();
      boats = new ArrayList<NPCPlayer>();
      corpses = new ArrayList<ArrayList<Corpse>>();
      corpses.add(new ArrayList<Corpse>());  //for world map
      corpses.add(new ArrayList<Corpse>());  //for temporary map to store ship grapple battles
      orcArmy = false;
      trollArmy = false; 
      brigandArmy = false;
      wolfPack = false;
      armyGeneral = false;
      deaditeArmy = false;
      NPCsInBed = false;
      NPCsOnStreet = false;
      distToLute = Double.MAX_VALUE;
   
      message = new String[5];
      for(int i=0; i<message.length; i++)
      {
         message[i] = "";
      }
      sendMessage("Cultima!");
   
      doorsToClose = new LinkedList<RestoreItem>();
      tilesToRestore = new ArrayList<RestoreItem>();
      NPCsToRestore = new ArrayList<NPCPlayer>();
      barrelsToRestore = new ArrayList<RestoreItem>();
      eggsToHarvest = new ArrayList<RestoreItem>();
      civilians = new ArrayList<ArrayList<NPCPlayer>>();
      civilians.add(new ArrayList<NPCPlayer>());      //for world map
      civilians.add(new ArrayList<NPCPlayer>());      //for temporary map to store ship grapple battles
      furniture = new ArrayList<ArrayList<NPCPlayer>>();
      furniture.add(new ArrayList<NPCPlayer>());      //for world map
      furniture.add(new ArrayList<NPCPlayer>());      //for temporary map to store ship grapple battles
      selected = null;
      selectedBarrel = null;
      selectedTerrain = null;
      selectedRenovate = null;
      selectedBuild = null;
      clearSelected();
   
      spellTime = 0;
      cheerTime = 0;
      gallopTime = 0;
      gallopTime2 = 0;
      alertTime = 0;
      shipGrappleTime = 0;
   
      map = new ArrayList<byte[][]>(); 
      map.add(new byte[MAP_SIZE][MAP_SIZE]);          //world map
      map.add(new byte[1][1]);                        //temporary spot for ship grappling battles 
      
      CBname = Utilities.nameGenerator("character");  //character builder name
      CBimageIndex = 0;                               //character builder imageIndex (starting weapon)
      CBmight = 3;                                    //character builder player attributes (points to distribute add to 50), 3-50 for CBmight
      CBmind = 3;                                     //3-50
      CBagility = 3;                                  //3-50
      CBawareness = 0;                                //0-4, each point counts for 10 out of 50
      CBaffliction = 0;                               //0, 1, 2, or 3 for NONE, FRAMED, VAMPIRE, WEREWOLF
      CBspell1 = null;     
      CBspell2 = null;
      CBspellIndex1 = 0;
      CBspellIndex2 = 1;
      player = FileManager.readPlayerFromFile("data/player.txt");
      if(player == null)
      {
         sendMessage("Error reading player file!");        
         sendMessage("Creating a new character.");
         missionStack = new LinkedList<Mission>();
         missionsGiven = new byte[Mission.NUM_MISSIONS];
         player = new Player(Utilities.nameGenerator("character"), null, true);
         FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
         menuMode = JOURNAL;
         newCharacter = true;
         days = 1;
         if(Display.isWinter())     //players that start in winter get a random animal pelt
            player.addArmorAndSwitch(Armor.getRandomPelt());
      }
      else
      {
         sendMessage("Welcome back, "+player.getShortName());
      }
      player.clearLocationData();
   
      groundColor = Display.findGroundColor(true);
   
      FileManager.readWardrobeInventory("data/wardrobe.txt");
      
      achievements = Achievement.readAchievements("data/achievements.txt");
       
      eggsToHarvest = new ArrayList<RestoreItem>();
      barrelsToRestore = new ArrayList<RestoreItem>();
   
      boolean readIn = TerrainBuilder.buildOrLoadWorld((map.get(0)), "maps/world.bin");
   
      if(readIn)        //the world was read in properly, so read in other maps for towns, caves, etc
      {
         revealedMap = 0;
         for(int r=0; r<(map.get(0)).length; r++)
         {
            for(int c=0; c<(map.get(0))[0].length; c++)
            {
               if((map.get(0))[r][c] < 0)
                  revealedMap++; 
            }
         }
         sendMessage("World remembered.");
         mapFiles = FileManager.readMapFileNames("maps/mapFileNames.txt");
         for(int i=0; i<mapFiles.size(); i++)
         {
            civilians.add(new ArrayList<NPCPlayer>());
            corpses.add(new ArrayList<Corpse>());
            furniture.add(new ArrayList<NPCPlayer>());
         }
        
         for(String fileName:mapFiles)    //mapFileNames should be in the format of "mapIndex_numRows_numCols.bin"
         {
            String[]parts = fileName.split("_");
            if(parts.length == 3)
            {
               String index = parts[0];   
               String rows = parts[1];
               String cols = parts[2];
               if(!index.startsWith("maps/") || !cols.endsWith(".bin"))
                  break;
               int pos = index.indexOf("/");
               index = index.substring(pos+1);
               pos = cols.indexOf(".");
               cols = cols.substring(0, pos);
            
               for(int i=0; i<index.length(); i++)        //file name is not correct - skip this one
                  if(!Character.isDigit(index.charAt(i)))
                     break;
               for(int i=0; i<rows.length(); i++)        //file name is not correct - skip this one
                  if(!Character.isDigit(rows.charAt(i)))
                     break;
               for(int i=0; i<cols.length(); i++)        
                  if(!Character.isDigit(cols.charAt(i)))
                     break;
               int thisIndex = Integer.parseInt(index);
               int numRows = Integer.parseInt(rows);
               int numCols = Integer.parseInt(cols);
               if(thisIndex == map.size())               //new, last element
               {
                  map.add(new byte[numRows][numCols]);
               }
               else if(thisIndex < map.size())           //there is already an element at that index, so set it
               {
                  map.set(thisIndex, new byte[numRows][numCols]);
               }
               else                                      //add temporary values to fill up the List to put our map at the right index
               {
                  for(int i=map.size(); i<=thisIndex; i++)
                  {
                     map.add(null);
                  }
                  map.set(thisIndex, new byte[numRows][numCols]);
               }
               boolean success = FileManager.readMapFromBinFile(fileName, (map.get(thisIndex)));
            }
         }
         FileManager.readAllNPCFile(civilians, "maps/civilians.txt");
         FileManager.readAllNPCFile(furniture, "maps/furniture.txt");
         doorsToClose = FileManager.readDoorsFile("maps/doors.txt");
         tilesToRestore = FileManager.readTilesToRestore("maps/worldRestore.txt");
         eggsToHarvest = FileManager.readTilesToRestore("maps/harvestRestore.txt");
         barrelsToRestore = FileManager.readTilesToRestore("maps/barrelRestore.txt");
         NPCsToRestore = FileManager.readNPCFile("maps/NPCRestore.txt");
         worldMonsters = FileManager.readNPCFile("maps/worldMonsters.txt");
         boats = FileManager.readNPCFile("maps/boats.txt");
         missionStack = FileManager.readMissionStack("data/missions.txt");
         if(newCharacter)                     //we have a new character, so start them at a city
         {
            startAtCity();
            int mapSize = CultimaPanel.SCREEN_SIZE;
            TerrainBuilder.markMapArea(CultimaPanel.player.getRow(), CultimaPanel.player.getCol(), mapSize);
            FileManager.writeOutPlayerToFile("data/player.txt");
         }
         else
         {
            Utilities.cleanEscortMissions();
         }
      }
      else     //world was not read in properly
      {
         if(!newCharacter)
            menuMode = MAIN;
         if(player.getBody() <= 0)
            player.setBody(player.getHealthMax());
         sendMessage("New world created.");
         mapFiles = new ArrayList<String>();
         missionStack = new LinkedList<Mission>();
         missionsGiven = new byte[Mission.NUM_MISSIONS];
         missionStack.add(new Mission(false, false, false));
         if(player.isVampire())
            missionStack.add(new Mission(true, false, false));
         if(player.isWerewolf())
            missionStack.add(new Mission(false, true, false));
         if(player.isFramed())
            missionStack.add(new Mission(false, false , true));
         player.clearMissionItems();
         FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
         doorsToClose = new LinkedList<RestoreItem>();
         tilesToRestore = new ArrayList<RestoreItem>();
         NPCsToRestore = new ArrayList<NPCPlayer>();
         civilians = new ArrayList<ArrayList<NPCPlayer>>();
         civilians.add(new ArrayList<NPCPlayer>());
         civilians.add(new ArrayList<NPCPlayer>());   //for temporary map to store ship grapple battles
         furniture = new ArrayList<ArrayList<NPCPlayer>>();
         furniture.add(new ArrayList<NPCPlayer>());
         furniture.add(new ArrayList<NPCPlayer>());   //for temporary map to store ship grapple battles
         corpses = new ArrayList<ArrayList<Corpse>>();
         corpses.add(new ArrayList<Corpse>());
         corpses.add(new ArrayList<Corpse>());        //for temporary map to store ship grapple battles
      
         startAtCity();                               //try to start us off at a city, castle or village
         findGoodStart();                             //make sure player doesn't start on a blocked or impassable spot 
         int mapSize = CultimaPanel.SCREEN_SIZE;
         TerrainBuilder.markMapArea(CultimaPanel.player.getRow(), CultimaPanel.player.getCol(), mapSize);
         FileManager.writeOutPlayerToFile("data/player.txt");
         revealedMap = 0;
         if(player.isVampire())                       //vampires need to start at the fall of night
            time = 18;
         else 
            time = 9;                                  //non-vampires start in the morning
      }
      if(player.getMapIndex() >= map.size())
      {
         player.setMapIndex(0);
      }
      mapR = player.getRow();
      mapC = player.getCol();
   
      blocked = Display.blockedView();                //collection of Terrain locations that need to be hidden because they are blocked by something tall
      darkened = new ArrayList<Coord>();
      smoke = new LinkedList<SmokePuff>();
      
      Location caerLoc = Utilities.getCaerbannog();
      if(caerLoc != null)
      {
         caerbannogRow = caerLoc.getRow();
         caerbannogCol = caerLoc.getCol();
         caerbannogBeastSpawn = false;
      }
      else
      {
         caerbannogBeastSpawn = true;                 //so it doesn't spawn again if the location can't be found
      }
      byte[][]currMap = (map.get(player.getMapIndex())); 
      if(player.getRow() >= currMap.length || player.getCol() >= currMap[0].length)
      {
         player.setMapIndex(0);                       //send player back to worldMap at last worldRow and worldCol
         player.setRow(player.getWorldRow());
         player.setCol(player.getWorldCol());
      }
      String curr = allTerrain.get(Math.abs(currMap[player.getRow()][player.getCol()])).getName().toLowerCase();
      ArrayList<String>imageNames = player.getFileNames();
      boolean isShip = false;
      for(String s:imageNames)
         if(s.contains("ship") || s.contains("boat"))
            isShip = true;
      boolean onHorse = false;
      for(String s:imageNames)
         if(s.contains("horse") || s.contains("unicorn"))
            onHorse = true;    
      if(!curr.contains("water") && (isShip  || player.isSailing()))
      {
         player.setImageIndex(Player.NONE);
         player.setWeaponSelect((byte)(0));
      }
      if(onHorse  && (!player.onHorse() || (player.getImageIndex()!=Player.HORSE && player.getImageIndex()!=Player.UNICORN)))
      {
         player.setImageIndex(Player.NONE);
         player.setWeaponSelect((byte)(0));
      }
      if((onHorse  || player.onHorse() || player.getImageIndex()==Player.HORSE || player.getImageIndex()==Player.UNICORN) && player.getMapIndex()!=0)
      {
         player.setImageIndex(Player.NONE);
         player.setWeaponSelect((byte)(0));
      }
      if(!onHorse && player.onHorse())
      {
         if(player.onUnicorn())
            player.setImageIndexTemp(Player.UNICORN);
         else
            player.setImageIndexTemp(Player.HORSE);
      }
      journal = Journal.buildJournal();
      weather = 0;
      rainbowFrame = 0;
      fogFrame = 0;
   
      if(Math.random() < .25 && !player.getLocationType().toLowerCase().contains("arena"))   //no arena games when raining
         weather = (byte)(Utilities.rollDie(0,10));
      
      rain_probability = Math.random()/2;  
      windDir = (byte)(Math.random()*5);    //set direction of winds and rapid waters
      
      well1 = null;
      well2 = null;
      door1 = null;
      door2 = null;
      door3 = null;
      tower1 = null;
      tower2 = null;
      tower3 = null;
      
      NPCsInOurMap = new ArrayList<NPCPlayer>();
      humanNPCsInOurMap = new ArrayList<NPCPlayer>();    //civilians
      nonAliveNPCsInOurMap = new ArrayList<NPCPlayer>(); //barrels, statues, etc
      
      adjacentTalkableNPCs = new NPCPlayer[4];
      adjacentMonsters = new NPCPlayer[4];
      getInRangeToFeed = null;
      getDeathAbout = null;
      monsterAndCivInView = null;
      monsterForGuardTarget = null;
      ourDog = null;
      ourNpc = null;
      onUs = null;
      missionTarget = null;
      numNPCsToTalk = 0;
      nextToNPCtoPickpocket = false;
      inRangeToAttack = false;
      inRangeToAttackAlt = false;
      zombieAbout = false;
      phantomAbout = false;
      ghostInView = false;
      werewolfAbout = false;
      thereIsDemonSwinePlayer = false;
      assassinAbout = -1;
      numNPCsInLoc = 0;
      numBattleFieldEnemies = 0;
      numEnemiesOnShip = 0;
      numWorldMonstersToExclude = 0;
      humanNPCInSight = false;
      greatestNPCinSight = null;
      awareHumanNPCInSight = false;
      brigandInSight = false;
      distToClosestAwareNPC = CultimaPanel.SCREEN_SIZE;
      guardsOnAlert = false;
      onACorpse = null;
      onAnyCorpse = null;
      nextMonsterSpawn = (byte)(-1);
      Utilities.setBounty();
      lastMessage = "";
     /***TESTING***
      weather = 0; 
      rainbowFrame = 3000;
      rainbowAlpha = 1;
      //fogFrame = 3000;
      //fogAlpha = 1;
     //***********/ 
   }
 
   //clear the booleans for whether or not we have selected a character to talk to, attack, examine, etc
   public static void clearSelected()
   {
      talkSel = false;
      attackSel = false;
      attackSelAlt = false;
      examineSel = false;
      stealSel = false;
      breakSel = false;
      fireSel = false;
      pushSel = false;
      renovateSel = false;
      buildSel = false;
   }

   //add a new sentence and scroll the message array for the Display
   public static void reportMessage(String sentence)
   {
      if(!sentence.equals(message[message.length-1]))
      {
         for(int i=0; i<message.length-1; i++)
         {
            message[i] = message[i+1];
         }
         message[message.length-1] = sentence;
      }
   }
   
   //clear all messages in the message array
   public static void clearMessage()
   {
      for(int i=0; i<message.length; i++)
      {
         message[i] = "";
      }
   }

   public static void sendMessage(String sentence)
   {
      if(lastMessage != null && lastMessage.equals(sentence))    //don't repeat the same message back-to-back
         return;
      String start = "";
      if(sentence.startsWith("~"))
      {
         start = "~";
         boolean timestop = (CultimaPanel.player.getActiveSpell("Timestop")!=null);
         if(timestop)      //no NPC dialogue when timestop is activated
         {
            return;
         }
      }
      String [] words = sentence.split(" ");
      ArrayList<String> lines = new ArrayList<String>();
      String line="";
      for(int i=0; i<words.length; i++)
      {
         String word = words[i];
         if(word.equals("i"))
         {
            word = "I";
         }
         if(i==0)
         {
            line = word;
            line = line.substring(0,1).toUpperCase() + line.substring(1);
         }
         else if(line.length() + word.length() >= 35)
         {
            lines.add(line);
            line = start+word;
         }
         else
         {
            line = line + " " + word;
         }
      }
      lines.add(line);
      for(String l: lines)
      {
         reportMessage(l);
      }
      if(!sentence.contains("-----"))
      {
         lastMessage = sentence;
      }
   }

   //returns coordinates of a spot that is not Impassable   
   public static int[] returnRandomWorldLoc()
   {
      int mapIndex = 0;
      byte[][] currMap = (map.get(mapIndex));
      int r = (int)(Math.random()*currMap.length);
      int c = (int)(Math.random()*currMap[0].length);
      boolean onBlocked = false;
      if(!LocationBuilder.outOfBounds(currMap, r, c))
         onBlocked = (allTerrain.get(Math.abs(currMap[r][c])).getName().contains("_I_"));
      boolean upBlocked = false;
      if(!LocationBuilder.outOfBounds(currMap,r-1,c))
         upBlocked = (allTerrain.get(Math.abs(currMap[equalizeWorldRow(r-1)][c])).getName().contains("_I_"));
      boolean rightBlocked = false;
      if(!LocationBuilder.outOfBounds(currMap,r, c+1))
         rightBlocked = (allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c+1)])).getName().contains("_I_"));
      boolean downBlocked = false;
      if(!LocationBuilder.outOfBounds(currMap,r+1, c))
         downBlocked = (allTerrain.get(Math.abs(currMap[equalizeWorldRow(r+1)][c])).getName().contains("_I_"));
      boolean leftBlocked = false;
      if(!LocationBuilder.outOfBounds(currMap,r, c-1))
         leftBlocked = (allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c-1)])).getName().contains("_I_"));
      int numTries = 0;
      while((onBlocked || (upBlocked && rightBlocked && downBlocked && leftBlocked) || (TerrainBuilder.onWater(currMap, r, c))) && numTries < 1000)
      {
         r = (int)(Math.random() * currMap.length);
         c = (int)(Math.random() * currMap[0].length);
         onBlocked = allTerrain.get(Math.abs(currMap[r][c])).getName().contains("_I_");
         upBlocked = allTerrain.get(Math.abs(currMap[equalizeWorldRow(r-1)][c])).getName().contains("_I_");
         rightBlocked = allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c+1)])).getName().contains("_I_");
         downBlocked = allTerrain.get(Math.abs(currMap[equalizeWorldRow(r+1)][c])).getName().contains("_I_");
         leftBlocked = allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c-1)])).getName().contains("_I_");
         numTries++;
      }
      int [] ans = new int[2];
      ans[0] = r;
      ans[1] = c;
      return ans;
   } 

   //starts player in a spot that is not Impassable   
   public static void findGoodStart()
   {
      int mapIndex = player.getMapIndex();
      int r = player.getRow();
      int c = player.getCol();
      if(mapIndex >= map.size())
      {
         mapIndex = 0;
         player.setMapIndex(0);
      }
      byte[][] currMap = (map.get(mapIndex));
      boolean sailing = player.isSailing();
      if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
      {
         r = currMap.length/2;
         c = currMap[0].length/2;
      }
   
      boolean onBlocked = (!sailing && allTerrain.get(Math.abs(currMap[r][c])).getName().contains("_I_"));
      boolean upBlocked = (!sailing && allTerrain.get(Math.abs(currMap[equalizeWorldRow(r-1)][c])).getName().contains("_I_"));
      boolean rightBlocked = (!sailing && allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c+1)])).getName().contains("_I_"));
      boolean downBlocked = (!sailing && allTerrain.get(Math.abs(currMap[equalizeWorldRow(r+1)][c])).getName().contains("_I_"));
      boolean leftBlocked = (!sailing && allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c-1)])).getName().contains("_I_"));
   
      if(!onBlocked && sailing && !allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase().contains("water"))
         onBlocked = true;
      if(!upBlocked && sailing && !allTerrain.get(Math.abs(currMap[equalizeWorldRow(r-1)][c])).getName().toLowerCase().contains("water"))
         upBlocked = true;
      if(!rightBlocked && sailing && !allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c+1)])).getName().toLowerCase().contains("water"))
         rightBlocked = true;
      if(!downBlocked && sailing && !allTerrain.get(Math.abs(currMap[equalizeWorldRow(r+1)][c])).getName().toLowerCase().contains("water"))
         downBlocked = true;
      if(!leftBlocked && sailing && !allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c-1)])).getName().toLowerCase().contains("water"))
         leftBlocked = true;
   
      while(onBlocked || (upBlocked && rightBlocked && downBlocked && leftBlocked) || (TerrainBuilder.onWater(currMap, r, c) && !sailing))
      {
         r = (int)(Math.random() * currMap.length);
         c = (int)(Math.random() * currMap[0].length);
         onBlocked = allTerrain.get(Math.abs(currMap[r][c])).getName().contains("_I_");
         upBlocked = allTerrain.get(Math.abs(currMap[equalizeWorldRow(r-1)][c])).getName().contains("_I_");
         rightBlocked = allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c+1)])).getName().contains("_I_");
         downBlocked = allTerrain.get(Math.abs(currMap[equalizeWorldRow(r+1)][c])).getName().contains("_I_");
         leftBlocked = allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c-1)])).getName().contains("_I_");
      }
      player.setRow(r);
      player.setCol(c);
   }  

//returns true if we are starting next to a city
   public static boolean startAtCity()
   {
      ArrayList<Location> allCities = new ArrayList<Location>();
      byte[][] currMap = (map.get(0));
      for(Location city: allDomiciles)
      {
         String locType = allTerrain.get(Math.abs(currMap[city.getRow()][city.getCol()])).getName().toLowerCase();
         if(locType.contains("city") || locType.contains("fortress"))
            allCities.add(city);
      } 
      if(allCities.size() == 0)
      {
         allCities.clear();
         for(Location city: allDomiciles)
         {
            String locType = allTerrain.get(Math.abs(currMap[city.getRow()][city.getCol()])).getName().toLowerCase();
            if(locType.contains("city") || locType.contains("fortress") || locType.contains("port") || locType.contains("village"))
               allCities.add(city);
         }
      }
      if(allCities.size() == 0)
      {
         findGoodStart();
         return false;
      }
      boolean atCity = true;
      int r = currMap.length/2;
      int c = currMap[0].length/2;
      boolean upBlocked = true, rightBlocked = true, downBlocked = true, leftBlocked = true;
      ArrayList<Coord> spawns = new ArrayList<Coord>();
      for(Location startLoc:allCities)
      {
         r = startLoc.getRow();
         c = startLoc.getCol();
         String up = allTerrain.get(Math.abs(currMap[equalizeWorldRow(r-1)][c])).getName().toLowerCase();
         String right = allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c+1)])).getName().toLowerCase();
         String down = allTerrain.get(Math.abs(currMap[equalizeWorldRow(r+1)][c])).getName().toLowerCase();
         String left = allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c-1)])).getName().toLowerCase();
         upBlocked = up.contains("_i_") || up.contains("water");
         rightBlocked = right.contains("_i_") || right.contains("water");
         downBlocked = down.contains("_i_") || down.contains("water");
         leftBlocked = left.contains("_i_") || left.contains("water");
         if(!upBlocked || !rightBlocked || !downBlocked || !leftBlocked)
            spawns.add(new Coord(r,c));
      }
      if(spawns.size() > 0)
      {
         Coord spawn = Utilities.getRandomFrom(spawns);
         r = spawn.getRow();
         c = spawn.getCol();
         String up = allTerrain.get(Math.abs(currMap[equalizeWorldRow(r-1)][c])).getName().toLowerCase();
         String right = allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c+1)])).getName().toLowerCase();
         String down = allTerrain.get(Math.abs(currMap[equalizeWorldRow(r+1)][c])).getName().toLowerCase();
         String left = allTerrain.get(Math.abs(currMap[r][equalizeWorldRow(c-1)])).getName().toLowerCase();
         upBlocked = up.contains("_i_") || up.contains("water");
         rightBlocked = right.contains("_i_") || right.contains("water");
         downBlocked = down.contains("_i_") || down.contains("water");
         leftBlocked = left.contains("_i_") || left.contains("water");
      }
      else
      {
         findGoodStart();
         return false;
      }
      if(!upBlocked)
      {
         player.setRow(equalizeWorldRow(r-1));
         player.setCol(c);
      }
      else if(!rightBlocked)
      {
         player.setRow(r);
         player.setCol(equalizeWorldRow(c+1));
      }
      else if(!leftBlocked)
      {
         player.setRow(r);
         player.setCol(equalizeWorldRow(c-1));
      }
      else if(!downBlocked)
      {
         player.setRow(equalizeWorldRow(r+1));
         player.setCol(c);
      }
      return atCity;
   } 

   //adjust player row value to accomodate wrap around from last row to the first
   public static int equalizeRow(int row)
   {
      int mapIndex = player.getMapIndex();
      if(mapIndex < 0 || mapIndex >= map.size())
         return row;
      if(row < 0)
         row = (map.get(mapIndex)).length - (Math.abs(row) % (map.get(mapIndex)).length);
      if(row >= (map.get(mapIndex)).length)
         row %= (map.get(mapIndex)).length;
      return row;
   }

   //adjust player column value to accomodate wrap around from last column to the first
   public static int equalizeCol(int col)
   {
      int mapIndex = player.getMapIndex();
      if(mapIndex < 0 || mapIndex >= map.size())
         return col;
      if(col < 0)
         col = (map.get(mapIndex)).length - (Math.abs(col) % (map.get(mapIndex))[0].length);
      if(col >= (map.get(mapIndex))[0].length)
         col %= (map.get(mapIndex))[0].length;
      return col;
   }

   //adjust player row value to accomodate wrap around from last row to the first, assuming in the greater World
   public static int equalizeWorldRow(int row)
   {
      int mapIndex = 0;
      if(row < 0)
         row = (map.get(mapIndex)).length - (Math.abs(row) % (map.get(mapIndex)).length);
      if(row >= (map.get(mapIndex)).length)
         row %= (map.get(mapIndex)).length;
      return row;
   }

   //adjust player column value to accomodate wrap around from last column to the first, assuming in the greater World
   public static int equalizeWorldCol(int col)
   {
      int mapIndex = 0;
      if(col < 0)
         col = (map.get(mapIndex)).length - (Math.abs(col) % (map.get(mapIndex))[0].length);
      if(col >= (map.get(mapIndex))[0].length)
         col %= (map.get(mapIndex))[0].length;
      return col;
   }

   //returns the Location at row r, col c, mapIndex mi, or null if there is not one there
   public static Location getLocation(ArrayList<Location> allLocations, int r, int c, int mi)
   {
      if(allLocations != null)
         for(Location loc:allLocations)
            if(loc.getRow()==r && loc.getCol()==c && loc.getFromMapIndex()==mi)
               return loc;
      return null;
   }

   //returns the index of the Location at row r, col c, mapIndex mi, or -1 if there is not one there
   public static int getLocationIndex(ArrayList<Location> allLocations, int r, int c, int mi)
   {
      if(allLocations != null)
         for(int i=1; i< allLocations.size(); i++) //skip the first Location, because it is a temp value so that Location index is the same as it's place in the map List
         {
            Location loc = allLocations.get(i);
            if(loc.getRow()==r && loc.getCol()==c && loc.getFromMapIndex()==mi)
               return i;
         }
      return -1;
   }

   //returns the Location with a specific name
   public static Location getLocation(String name)
   {
      if(allLocations != null)
         for(Location loc:allLocations)
            if(loc.getName().equals(name) || Display.provinceName(loc.getName()).equals(name))
               return loc;
      return null;
   }

   public void movePlayer(int k)
   {   
      boolean isWolf = (CultimaPanel.player.isWolfForm());
      boolean holiday = (days % 100 == 50);
      int daysUntilHoliday = (days % 100);
      if(daysUntilHoliday <= 49)
      {
         daysUntilHoliday = 50 - daysUntilHoliday;
      } 
      else
      {
         daysUntilHoliday = 100 - daysUntilHoliday;
      }
      String daysLeft = "" + daysUntilHoliday + " day";
      if(daysUntilHoliday > 1)
      {
         daysLeft += "s";
      }
   
      if(menuMode == TRAVEL)
      {
         int mapIndex = player.getMapIndex();
         if(player.getBody() <= 0)
         {
            repaint();
            return;
         }
         if((player.hasEffect("sullied") || player.hasEffect("seduced")) && !attackSel && !attackSelAlt && !talkSel && !examineSel && !stealSel && !breakSel && !fireSel && !pushSel && !renovateSel && !buildSel)
         {  //scramble direction keys if sullied or seduced
            int rotation = (days%3)+1;
            if(player.hasEffect("seduced"))
               rotation = 2;  //seduced just reverses controls
            if(rotation==1)
            {
               if(k==KeyEvent.VK_UP)
                  k=KeyEvent.VK_RIGHT;
               else if(k==KeyEvent.VK_RIGHT)
                  k=KeyEvent.VK_DOWN;
               else if(k==KeyEvent.VK_DOWN)
                  k=KeyEvent.VK_LEFT;
               else if(k==KeyEvent.VK_LEFT)
                  k=KeyEvent.VK_UP;
            }
            else if(rotation==2)
            {
               if(k==KeyEvent.VK_UP)
                  k=KeyEvent.VK_DOWN;
               else if(k==KeyEvent.VK_RIGHT)
                  k=KeyEvent.VK_LEFT;
               else if(k==KeyEvent.VK_DOWN)
                  k=KeyEvent.VK_UP;
               else if(k==KeyEvent.VK_LEFT)
                  k=KeyEvent.VK_RIGHT;
            }
            else //if(rotation==3)
            {
               if(k==KeyEvent.VK_UP)
                  k=KeyEvent.VK_LEFT;
               else if(k==KeyEvent.VK_RIGHT)
                  k=KeyEvent.VK_UP;
               else if(k==KeyEvent.VK_DOWN)
                  k=KeyEvent.VK_RIGHT;
               else if(k==KeyEvent.VK_LEFT)
                  k=KeyEvent.VK_DOWN;
            }
         }
         NPCsInOurMap.clear();
         humanNPCsInOurMap.clear();
         nonAliveNPCsInOurMap.clear();
         if(player.getMapIndex() < civilians.size())
         {
            for(int i = civilians.get(player.getMapIndex()).size()-1; i>=0; i--)
            {
               NPCPlayer p = civilians.get(player.getMapIndex()).get(i);
               if(p.getCharIndex()==NPC.WILLOWISP && p.getLocationType().contains("pacrealm") && p.getRow()==player.getRow() && p.getCol()==player.getCol())
               {
                  civilians.get(player.getMapIndex()).remove(i);
                  player.addGold(1);
                  sendMessage("You pick up 1 gold coin.");
               }
               else
               {
                  NPCsInOurMap.add(p);
                  if(NPC.isCivilian(p.getCharIndex()))
                  {
                     humanNPCsInOurMap.add(p);
                  }
                  else if(NPC.isStone(p) || NPC.isNatural(p))
                  {
                     nonAliveNPCsInOurMap.add(p);
                  }
               }
            }  
         }
         if(player.getMapIndex() < furniture.size())
         {
            for(NPCPlayer p: furniture.get(player.getMapIndex()))
            {
               NPCsInOurMap.add(p);
               nonAliveNPCsInOurMap.add(p);
            }  
         }
         for(NPCPlayer p: worldMonsters)
         {
            if(player.getLocationType().contains("camp"))
            {
               if(NPC.isCivilian(p.getCharIndex()) && p.isWerewolf())
               {
                  humanNPCsInOurMap.add(p);
               }
            }
            if(p.getMapIndex() == player.getMapIndex())
            {
               NPCsInOurMap.add(p);
            }
         }
         if(player.getMapIndex() == 0)    //boats only spawn in the world
         {      
            for(NPCPlayer p: boats)
               NPCsInOurMap.add(p);
         }
         String dir = "";
         int r = player.getRow();
         int c = player.getCol();
         int lastR = player.getRow();
         int lastC = player.getCol();
      
         byte[][]currMap = (map.get(mapIndex));
         double increment = WORLD_TIME_INCREMENT;
         if(player.isVampire() && (time <8 || time > 16))//vampires move faster at night
            increment /= 2;
      
         byte value = currMap[player.getRow()][player.getCol()];
         Terrain currentPos = allTerrain.get(Math.abs(value));
         String currPosName = currentPos.getName().toLowerCase();
         boolean indoors = TerrainBuilder.indoors();
         String locType = player.getLocationType().toLowerCase();
      
         if(player.hasEffect("fire") && (currPosName.contains("water") || currPosName.contains("bog")))
         {
            player.removeEffect("fire");
            sendMessage("Thy burning pain has extinguished!");
         }
         boolean sailing = player.isSailing();
         boolean flight = (player.getActiveSpell("Flight")!=null);
         boolean timestop = (player.getActiveSpell("Timestop")!=null);
         if(!flight && currPosName.contains("lava"))
         {
            player.damage(100);
            sendMessage("Thy burning feels of death itself!");
         }
         else if(!flight && !sailing && currPosName.contains("rapid"))
         {
            player.damage(50);
            sendMessage("Angry waters swallow thee!");
         }
         else if(!flight && !sailing && currPosName.contains("deep") && !Display.frozenWater())
         {
            player.damage(25);
            sendMessage("'Tis difficult to stay afloat!");
         }
         else if(!flight && currPosName.contains("mountains"))
         {
            player.damage(12);
            sendMessage("'Tis cold upon the mountains!");
         }
         if(k==KeyEvent.VK_S && TerrainBuilder.habitablePlace(player.getLocationType()) && doorToClose!=null)
         {  //restore doorToClose
            int dmapIndex = doorToClose.getMapIndex();
            int drow = doorToClose.getRow();
            int dcol = doorToClose.getCol();
            byte dvalue = doorToClose.getValue();
            if(dmapIndex>=0 && dmapIndex < map.size())
            {
               if(drow>=0 && drow<currMap.length && dcol>=0 && dcol < currMap[0].length)
               {
                  currMap[drow][dcol] = dvalue;
                  //now remove that door from doorsToRestore, becuause we just restored it
                  for(int i=doorsToClose.size()-1; i>=0; i--)
                  {
                     RestoreItem currDoor = doorsToClose.get(i);
                     if(currDoor.equals(doorToClose))
                     {
                        doorsToClose.remove(i);
                        break;
                     }
                  }
               }
            }
         }
         if(k==KeyEvent.VK_S && doorToClose==null && (player.onBedAndTimeToSleep() || (player.canSleep() && !((Utilities.chestInRange() || nextToNPCtoPickpocket) && humanNPCInSight))))    //set camp
         {
            if(Display.isWinter() || weather <= 5 || currPosName.contains("bed"))
            {
               player.clearActiveSpells();
               player.setCamping(true);
               sendMessage("'Tis a good time to rest.");
            }
            else if(weather > 5 && !Display.isWinter())
            {
               sendMessage("'Tis too wet to start a fire and set camp!");
            }
            if((time < 6 || time > 20) && (locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("castle") || locType.contains("tower") || locType.contains("domicile") || locType.contains("port")))
            {
               Utilities.putNPCsToBed(player.getMapIndex());
            }
            repaint();
            return;
         }  
         if(player.isCamping())
         {
            player.incrementKeyTaps();
            if((player.getAgility() * player.getAwareness()) > Utilities.rollDie(1000) || player.getNumKeyTaps() >= Player.MAX_TAPS_TO_WAKE)
            {
               player.setCamping(false);
               sendMessage("You have forced thy way out of slumber!");
               player.setKeyTaps((byte)0);
            }
            else
               sendMessage("ZZZZZ...");
            repaint();
            return;
         }
         if(player.hasEffect("web"))
         {
            player.incrementKeyTaps();
            if((player.getAgility() * player.getMight()) > Utilities.rollDie(15000) || player.getNumKeyTaps() >= Player.MAX_TAPS_TO_ESCAPE)
            {
               player.removeEffect("web");
               sendMessage("You have escaped!");
               player.setKeyTaps((byte)0);
            }
            else
               sendMessage("You are trapped in a tangled web");
            repaint();
            return;
         }
         NPCPlayer escortee = Utilities.getNpc();
         if(k==KeyEvent.VK_W && (escortee==null || escortee.getMoveType()==NPC.STILL))    //wait
         {  //if we are escorting an npc, we want the W key to tell them to wait at that spot
            double oldTime = time;
            time += WORLD_TIME_INCREMENT*8;
            sendMessage("...wait...");
            Utilities.checkWerewolfStatus();
            Utilities.checkRainbow();
            if((int)(oldTime) != (int)(time))      //an hour has passed
            { //change weather while waiting
               Utilities.changeWeather();
               if(locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("castle") || locType.contains("tower") || locType.contains("domicile") || locType.contains("port"))
               {
                  if(time >= 6 && time <= 20)
                  {
                     if(locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("domicile") || locType.contains("port"))
                        Utilities.putNPCsOnStreet(player.getMapIndex());
                     else
                        Utilities.makeNPCsRoam(player.getMapIndex());
                     if(player.getReputation() < -100)
                        Utilities.makeNPCsRun(player.getMapIndex());
                  }
                  else 
                     Utilities.putNPCsToBed(player.getMapIndex());
               }
               else if(locType.contains("arena") && time >= 6 && time <= 20)
                  Utilities.setupArena(player.getMapIndex());
            }
            Utilities.moveMonsters(true); //bonus move for monsters with higher agility  
            Utilities.moveNPCs(true);
         
            player.runEffects();
            for(NPCPlayer npc: civilians.get(player.getMapIndex()))
               npc.runEffects();
            ArrayList<NPCPlayer>fires = new ArrayList<NPCPlayer>();   
            for(int i=worldMonsters.size()-1; i>=0; i--)
            {
               NPCPlayer npc = worldMonsters.get(i);
               npc.runEffects();
            }
            if(player.getMapIndex()==0)
            {                             //some boats might be on fire
               for(int i=boats.size()-1; i>=0; i--)
               {
                  NPCPlayer npc = boats.get(i);
                  npc.runEffects();
               }
            }
         }  //end wait 
         if(!flight && !player.isHiding() && k==KeyEvent.VK_CONTROL)
         {                                //raise or lower your weapon for battle/defense
            player.toggleOnGuard();
            repaint();
            return;
         }
         if(!flight && k==KeyEvent.VK_C && player.getSpell()!=null && !player.getSpell().getName().contains("Disarm") && !isWolf && (System.currentTimeMillis() > spellTime + MILLIS_BETWEEN_SPELLS))       
         {                                         //cast spell - we take care of disarming with the lockpick below
            if(player.onHorse() && player.getSpell().getName().contains("Timestop"))      
               player.dismountHorse();
            Spell spl = player.castSpell();
            if(spl!=null && Spell.getSpellCast(spl.getName()).startsWith("Klaatu")) //couldn't find a spell chant
               sendMessage(spl.getName()+" cast!");
            spellTime = System.currentTimeMillis();   
            repaint();
            return;
         }
         if(k==KeyEvent.VK_D && player.getLocationType().toLowerCase().contains("underworld") && well1!=null && well2!=null && player.getWellNumber()>=0)
         {
            player.setOnGuard(false);
            if(player.getWellNumber()==0)
               sendMessage("Tepid water consumed!");
            else
            {
               if(player.getWellNumber()==1)
                  sendMessage("Poison from Well I consumed!");
               else //if(player.getWellNumber()==2)
                  sendMessage("Poison from Well II consumed!");
               player.addEffectForce("poison");   
            }
            player.setWellNumber((byte)(-1));
            repaint();
            return;
         }
         if(k==KeyEvent.VK_F && player.getLocationType().toLowerCase().contains("underworld") && well1!=null && well2!=null && currentPos.getName().contains("water"))
         {  //fill goblet with water from a well in the 3-Wells Puzzle in the Underworld
            if(player.getRow()==well1.getRow() && player.getCol()==well1.getCol())
               player.setWellNumber((byte)(1));
            else if(player.getRow()==well2.getRow() && player.getCol()==well2.getCol())
               player.setWellNumber((byte)(2));
            else 
               player.setWellNumber((byte)(0));
            repaint();
            return;
         }
         if(k==KeyEvent.VK_D && player.getPotion()!=null)       //drink potion
         {
            player.setOnGuard(false);
            Potion ptn = player.drinkPotion();
            if(ptn != null)
               sendMessage(ptn.getName()+" consumed!");
            repaint();
            return;
         }
         if(player.canScrollArmor() && k==KeyEvent.VK_PAGE_DOWN && player.numArmor() > 0)  //scroll armor
         {
            player.setOnGuard(false);
            Sound.scrollItem();
            player.scrollArmorUp();
            sendMessage(player.getArmor().getName()+" fitted.");
            repaint();
            return;
         }
         if(player.canScrollPotion() && k==KeyEvent.VK_PAGE_UP && player.numPotions() > 0)
         {
            player.setOnGuard(false);
            Sound.scrollItem();
            player.scrollPotion();
            repaint();
            return;
         }
         if(!sailing && k==KeyEvent.VK_INSERT && player.numWeapons() > 0 && !isWolf)
         {
            player.setOnGuard(false);
            if(player.onHorse())      //dismount horse changing weapons
               player.dismountHorse();
            else
            {
               if(player.getActiveSpell("Fear")!=null)
                  player.removeSpell("Fear");
               if(player.getActiveSpell("Flight")!=null)
                  player.removeSpell("Flight");
               player.scrollWeaponLeft();
               Sound.scrollItem();
            }
            repaint();
            return;
         }
         if(!sailing && k==KeyEvent.VK_DELETE && player.numWeapons() > 0 && !isWolf)
         {
            player.setOnGuard(false);
            if(player.onHorse())      //dismount horse changing weapons
               player.dismountHorse();
            else
            {
               if(player.getActiveSpell("Fear")!=null)
                  player.removeSpell("Fear");
               if(player.getActiveSpell("Flight")!=null)
                  player.removeSpell("Flight");
               player.scrollWeaponRight();
               Sound.scrollItem();
            }
            repaint();
            return;
         }
         if(k==KeyEvent.VK_HOME && player.getSpells().size() > 0 && !isWolf)      //scroll spells left
         {
            player.setOnGuard(false);
            Sound.scrollItem();
            player.scrollSpellLeft();
            repaint();
            return;
         }
         if(k==KeyEvent.VK_END && player.getSpells().size() > 0 && !isWolf)     //scroll spells right
         {
            player.setOnGuard(false);
            Sound.scrollItem();
            player.scrollSpellRight();
            repaint();
            return;
         }
         if(k==KeyEvent.VK_T && CultimaPanel.getInRangeToFeed!=null)                       //toss rations to an animal
         {
            boolean hasFed = false;
            int numRations = 5;
            NPCPlayer [] feeders = new NPCPlayer[4];
            for(int direct = LocationBuilder.NORTH; direct <= LocationBuilder.WEST; direct++)
            {
               feeders[direct] = Utilities.scanForNPCinRange(direct, 2);
               if(feeders[direct] != null && NPC.isFeedableMonster(feeders[direct].getCharIndex()) && (feeders[direct].getMoveType()==NPC.CHASE || feeders[direct].getCharIndex()==NPC.UNICORN) && !feeders[direct].hasBeenAttacked())
               {
                  if(!hasFed)
                  {
                     if(feeders[direct].getCharIndex()==NPC.UNICORN)
                     {
                        CultimaPanel.player.removeFlowers(5);
                        if(feeders[direct].getReputation()%2 !=0)   //make reputation even so we can ride it
                           feeders[direct].setReputation(feeders[direct].getReputation()+1);
                     }
                     else
                     {
                        numRations = Math.min(5, player.getRations());
                        player.setRations((byte)(player.getRations() - numRations));
                     }
                  }
                  Sound.addRations();
                  if(Utilities.rollDie(10) < numRations)
                  {
                     feeders[direct].setMoveTypeTemp(NPC.ROAM);
                     if(feeders[direct].getReputation() < 0)
                     {
                        Sound.mapFound();
                        if(feeders[direct].getCharIndex()==NPC.ORC || feeders[direct].getCharIndex()==NPC.BUGBEAR)
                        {
                           if(Utilities.rollDie(10) < numRations)
                              feeders[direct].setReputation(0);
                        }
                        else
                        {
                           if(feeders[direct].getCharIndex()==NPC.ABOMINABLE)
                              Achievement.earnAchievement(Achievement.HUMBLE_BUMBLE);
                           feeders[direct].setReputation(0);
                        }
                     }
                  }
               }
            }
         }
         NPCPlayer ourDog = null;         
         boolean inStay = false;
         if(!player.getDogName().equals("none"))   //see if dog is in a stay command to make the whistle command retrieve the dog
         {   
            for(NPCPlayer p:worldMonsters)
            {
               if(p.isOurDog() && p.getMoveType()!=NPC.CHASE)
               {
                  ourDog = p;
                  inStay = true;
                  break;
               }
            }
         }
         if(inStay && ourDog!=null && k==KeyEvent.VK_R && !Utilities.inOurHouse())
         {
            sendMessage("~<You>:"+Utilities.getRandomFrom(NPC.recallDog).replace("DOG_NAME", ourDog.getName()));
            ourDog.setMoveTypeTemp(NPC.CHASE);
            repaint();
            return;
         }
         if(escortee!=null)
         {
            if(k==KeyEvent.VK_R && escortee.getMoveType()!=NPC.CHASE)
            {  //message from escortee, different from ADULT/CHILD
               sendMessage("~<You>:"+Utilities.getRandomFrom(NPC.recallEscortee).replace("NPC_NAME", Utilities.shortName(escortee.getName())));
               if(escortee.getCharIndex()==NPC.CHILD)
                  sendMessage("~"+NPC.getRandomFrom(NPC.npcFollowChild));
               else
                  sendMessage("~"+NPC.getRandomFrom(NPC.npcFollow));
               escortee.setMoveTypeTemp(NPC.CHASE);
               repaint();
               return;
            }
            if(k==KeyEvent.VK_W && escortee.getMoveType()!=NPC.STILL)
            {  //message from escortee, different from ADULT/CHILD
               sendMessage("~<You>:"+Utilities.getRandomFrom(NPC.holdEscortee).replace("NPC_NAME", Utilities.shortName(escortee.getName())));
               if(escortee.getCharIndex()==NPC.CHILD)
                  sendMessage("~"+NPC.getRandomFrom(NPC.npcWaitChild));
               else
                  sendMessage("~"+NPC.getRandomFrom(NPC.npcWait));
               escortee.setMoveTypeTemp(NPC.STILL);
               repaint();
               return;
            }
         }
      
      //************interact with NPC
         if(examineSel)
         {  //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
            Coord rowCol = Utilities.dirModifierKey(k, r, c);
            int newRow = rowCol.getRow();
            int newCol = rowCol.getCol();
            if(k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT)
            {
               selected = Utilities.scanForNPC(r, c, mapIndex, LocationBuilder.keyEventToDir(k));
               if(selected == null && mapIndex==0)
               {
                  selectedTerrain = allTerrain.get(Math.abs(currMap[equalizeWorldRow(newRow)][newCol]));
               }
               else if (!LocationBuilder.outOfBounds((map.get(mapIndex)), newRow, newCol))    
               {     //(newRow>=0 && newRow<(map.get(mapIndex)).length && newCol>=0 && newCol<(map.get(mapIndex))[0].length)
                  selectedTerrain = allTerrain.get(Math.abs(currMap[newRow][newCol]));
               }
               CultimaPanel.player.specificExperience[Player.AWARENESS]++;
               examineSel = false;
               repaint();
               return;
            }
            else
            {
               clearSelected();
               selected = null;
               selectedBarrel = null;
               selectedTerrain = null;
               selectedRenovate = null;
               selectedBuild = null;
            }
         }
         if(!flight && renovateSel && Utilities.inOurHouse() && selectedRenovate == null)
         {
            if(k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT)
            {  //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
               Coord rowCol = Utilities.dirModifierKey(k, r, c);
               int newRow = rowCol.getRow();
               int newCol = rowCol.getCol();
               if (!LocationBuilder.outOfBounds(currMap, newRow, newCol))
               {//(newRow>=0 && newRow<currMap.length && newCol>=0 && newCol<currMap[0].length)
                  Terrain currTerr = allTerrain.get(Math.abs(currMap[newRow][newCol]));
                  selectedRenovate = new RestoreItem(player.getMapIndex(), newRow, newCol, currTerr.getValue(), -1);
               //see if this is a door that has been opened, and if so, remove it from the restoreDoors collection
                  for(int i=doorsToClose.size()-1; i>=0; i--)
                  {
                     RestoreItem currDoor = doorsToClose.get(i);
                     if(currDoor.sameSpot(selectedRenovate))
                     {
                        doorsToClose.remove(i);
                        break;
                     }
                  }
               }
               renovateSel = false;
               repaint();
               return;
            }
            else
            {
               clearSelected();
               selected = null;
               selectedBarrel = null;
               selectedTerrain = null;
               selectedRenovate = null;
               selectedBuild = null;
            }
         }
         else if(!flight && Utilities.inOurHouse() && selectedRenovate != null)
         {
            if(k==KeyEvent.VK_COMMA || k==KeyEvent.VK_LESS)
            {
               if(LocationBuilder.renovateIndex > 0)
               {
                  LocationBuilder.renovateIndex--;
                  currMap[selectedRenovate.getRow()][selectedRenovate.getCol()] = LocationBuilder.renovateInv[LocationBuilder.renovateIndex].getValue();
               }
            }
            else if(k==KeyEvent.VK_PERIOD || k==KeyEvent.VK_GREATER)
            {
               if(LocationBuilder.renovateIndex < LocationBuilder.renovateInv.length-1)
               {
                  LocationBuilder.renovateIndex++;
                  currMap[selectedRenovate.getRow()][selectedRenovate.getCol()] = LocationBuilder.renovateInv[LocationBuilder.renovateIndex].getValue();
               }
            }
            else
            {
               clearSelected();
               selected = null;
               selectedBarrel = null;
               selectedTerrain = null;
               selectedRenovate = null;
               selectedBuild = null;
            }
         } 
         if(!flight && buildSel && player.canBuildBridge() && selectedBuild == null)
         {
            if(k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT)
            {  //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
               Coord rowCol = Utilities.dirModifierKey(k, r, c);
               int newRow = rowCol.getRow();
               int newCol = rowCol.getCol();
               if (!LocationBuilder.outOfBounds(currMap, newRow, newCol))
               {//(newRow>=0 && newRow<currMap.length && newCol>=0 && newCol<currMap[0].length)
                  Terrain currTerr = allTerrain.get(Math.abs(currMap[newRow][newCol]));
                  selectedBuild = new RestoreItem(player.getMapIndex(), newRow, newCol, currTerr.getValue(), -1);
                  Terrain terUp = null;
                  if(!LocationBuilder.outOfBounds(currMap, newRow, newCol))
                  {
                     terUp = CultimaPanel.allTerrain.get(Math.abs(currMap[newRow][newCol]));
                     if(TerrainBuilder.canBuildBridge(terUp))
                     {
                        currMap[newRow][newCol] = TerrainBuilder.getTerrainWithName("INR_$Wood_Plank_floor").getValue();
                        player.removeItem("lumber");
                        player.removeItem("lumber");
                     }
                     else
                     {
                        sendMessage("You can not build a bridge there");
                     }
                  } 
                  else
                  {
                     sendMessage("You can not build a bridge there");
                  }  
               }
               buildSel = false;
               repaint();
               return;
            }
            else
            {
               clearSelected();
               selected = null;
               selectedBarrel = null;
               selectedTerrain = null;
               selectedRenovate = null;
               selectedBuild = null;
            }
         }
         else if(!flight && (attackSel || attackSelAlt))
         {
            String weapName = player.getWeapon().getName().toLowerCase();
            boolean[] vaultDir1 = Utilities.nextToACityWall();
            boolean[] vaultDir2 = Utilities.nextToACounter();
            boolean[] vaultDirs = new boolean[vaultDir1.length];
            for(int i = 0; i < vaultDirs.length; i++)
            {  //record which directions we are aloud to vault over so we only vault into vaultable places
               vaultDirs[i] = (vaultDir1[i] || vaultDir2[i]);
            }
            if(k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT)
            {  //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
               Coord rowCol = Utilities.dirModifierKey(k, player.getRow(), player.getCol());
               int newRow = rowCol.getRow();
               int newCol = rowCol.getCol();
               byte locDir = LocationBuilder.keyEventToDir(k);
               boolean altFire = attackSelAlt;
               selected = Utilities.scanForNPCinRange(locDir, altFire);
               attackSel = false;
               attackSelAlt = false;
               selected = player.attack(selected, altFire);
               player.setLastDir(LocationBuilder.keyEventToDir(k));
               selectedBarrel = null;
               if(Weapon.isLongStaff(weapName) && altFire && vaultDirs[locDir] && (!LocationBuilder.outOfBounds((map.get(mapIndex)), newRow, newCol)))
               {  //vault over counter or wall
                  player.setRow(newRow);
                  player.setCol(newCol);
               }
               else if(weapName.contains("fireshield") && player.getManna() >= player.getWeapon().getMannaCost() && !altFire)
               {
                  Utilities.fireShield(locDir); 
                  player.setManna(player.getManna() - player.getWeapon().getMannaCost());
               }
               else if(weapName.contains("vortex") && player.getManna() >= player.getWeapon().getMannaCost() && !altFire)
               {
                  Utilities.summonVortex(locDir);      
                  player.setManna(player.getManna() - player.getWeapon().getMannaCost());
               }
               else if(weapName.contains("raise-stone") && player.getManna() >= player.getWeapon().getMannaCost() && !altFire)
               {
                  boolean success = Utilities.raiseStone(locDir);
                  if(success)      
                     player.setManna(player.getManna() - player.getWeapon().getMannaCost());
               }
               else if(weapName.contains("advance") && player.getManna() >= player.getWeapon().getMannaCost() && !altFire)
               {
                  Utilities.advance((byte)locDir);      
                  player.setManna(0);
               }
               else if(weapName.contains("trap"))
               {
                  Utilities.placeTrap(locDir);
               }
               repaint();
               return;
            }
            else
            {
               clearSelected();
               selected = null;
               selectedBarrel = null;
               selectedTerrain = null;
               selectedRenovate = null;
               selectedBuild = null;
            }
         }
         else if(k==KeyEvent.VK_E)     //examine
         {
            clearSelected();
            examineSel = true;
            selected = null;
            selectedBarrel = null;
            selectedTerrain = null;
            selectedRenovate = null;
            selectedBuild = null;
            repaint();
            return;
         }
         else if(!flight && Utilities.inOurHouse() && k==KeyEvent.VK_R)     //renovate
         {
            clearSelected();
            renovateSel = true;
            selected = null;
            selectedBarrel = null;
            selectedTerrain = null;
            selectedRenovate = null;
            selectedBuild = null;
            repaint();
            return;
         }
         else if(!flight && player.canBuildBridge() && k==KeyEvent.VK_B)     //build bridge
         {
            clearSelected();
            buildSel = true;
            selected = null;
            selectedBarrel = null;
            selectedTerrain = null;
            selectedRenovate = null;
            selectedBuild = null;
            repaint();
            return;
         }
         else if(k==KeyEvent.VK_Z && Weapon.isLongStaff(player.getWeapon().getName()) && (Utilities.nextToACityWallSimple() || Utilities.nextToACounterSimple()))
         {  //try to vault over wall or counter
            clearSelected();
            attackSelAlt = true;
            repaint();
            return;
         }
         else if(k==KeyEvent.VK_A && player.getWeapon().getName().toLowerCase().contains("advance") && player.getManna() >= player.getWeapon().getMannaCost())
         {
            clearSelected();
            attackSel = true;
            repaint();
            return;
         }
         else if(k==KeyEvent.VK_A && player.getWeapon().getName().toLowerCase().contains("trap") && Utilities.canPlaceTrap())
         {
            clearSelected();
            attackSel = true;
            repaint();
            return;
         }
         else if((k==KeyEvent.VK_A || k==KeyEvent.VK_Z) && (inRangeToAttack || inRangeToAttackAlt))     //attack: only option if in range to attack
         {
            clearSelected();
            if(k==KeyEvent.VK_A)
            {
               attackSel = true;
               attackSelAlt = false;
            }
            else if(k==KeyEvent.VK_Z)
            {
               attackSel = false;
               attackSelAlt = true;
            }
            repaint();
            return;
         }
         boolean nextToNPC = false;
         for(NPCPlayer p:adjacentTalkableNPCs)
            if(p != null)
               nextToNPC = true;
         if(nextToNPC)
         {            
            if(talkSel)      //we are trying to talk or attack, so look for a direction key to pick our selected target
            {
               byte locDir = LocationBuilder.keyEventToDir(k);
               if((k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT) && adjacentTalkableNPCs[locDir]!=null)
               {
                  selected = adjacentTalkableNPCs[locDir];
                  if(selected.getMoveType()==NPC.RUN) //people running with fear are unwilling to talk
                  {
                     talkSel = false;
                     selected.setTalking(false);
                     selected = null;
                     selectedBarrel = null;
                     selectedTerrain = null;
                     selectedRenovate = null;
                     selectedBuild = null;
                     typed = "";
                  }
                  else if(talkSel)
                  {
                     selected.setTalking(true);
                     sendMessage("-----");
                     if(!selected.getName().contains(" of ") && (Math.random() < 0.25 || !TerrainBuilder.habitablePlace(selected.getLocationType())) && NPC.isCivilian(selected.getCharIndex()))   //add the next closest town to their name
                     {
                        Location loc =  TerrainBuilder.closeLocation(CultimaPanel.allDomiciles);   
                        if(loc != null)                         
                           selected.setName(selected.getName() + " of " + loc.getName());
                     }
                  }
                  repaint();
                  return;
               }
               else
               {
                  clearSelected();
                  selected = null;
                  selectedBarrel = null;
                  selectedTerrain = null;
                  selectedRenovate = null;
                  selectedBuild = null;
               }  
            }
            else if(k==KeyEvent.VK_T && numNPCsToTalk >= 1)      
            {                                //talk:  only option if in range to talk
               if(numNPCsToTalk == 1)
               {
                  if(player.getActiveSpell("Invisibility")!=null)
                  {
                     player.removeSpell("Invisibility");
                  }
                  byte dirTalk = LocationBuilder.NORTH;
                  if(adjacentTalkableNPCs[LocationBuilder.SOUTH]!=null)
                     dirTalk = LocationBuilder.SOUTH;
                  else if(adjacentTalkableNPCs[LocationBuilder.EAST]!=null)
                     dirTalk = LocationBuilder.EAST;
                  else if(adjacentTalkableNPCs[LocationBuilder.WEST]!=null)
                     dirTalk = LocationBuilder.WEST;
                  else if(adjacentTalkableNPCs[LocationBuilder.NORTH]!=null)
                     dirTalk = LocationBuilder.NORTH;
                  selected = adjacentTalkableNPCs[dirTalk];
                  if(selected.getMoveType()==NPC.RUN) //people running with fear are unwilling to talk
                  {
                     talkSel = false;
                     selected.setTalking(false);
                     selected = null;
                     selectedBarrel = null;
                     selectedTerrain = null;
                     selectedRenovate = null;
                     selectedBuild = null;
                     typed = "";
                  }
                  else
                  {
                     talkSel = true;
                     selected.setTalking(true);
                     sendMessage("-----");
                     if(!selected.getName().contains(" of ") && (Math.random() < 0.25 || !TerrainBuilder.habitablePlace(selected.getLocationType())) && NPC.isCivilian(selected.getCharIndex()))   //add the next closest town to their name
                     {
                        Location loc =  TerrainBuilder.closeLocation(CultimaPanel.allDomiciles);   
                        if(loc != null)                         
                           selected.setName(selected.getName() + " of " + loc.getName());
                     }
                  }
               }
               else
               {                 
                  clearSelected();
                  talkSel = true;
                  sendMessage("-----");
               }
               repaint();
               return;
            }
         }
         if((Utilities.nextToABarrelSimple() || Utilities.nextToOurWardrobe()!=-1 || Utilities.nextToOurBed(player.getRow(), player.getCol())!=-1) && pushSel)
         {
            if(selectedBarrel == null && Utilities.nextToOurWardrobe()==-1 && Utilities.nextToOurBed(player.getRow(), player.getCol())==-1)
            {
               boolean [] barrelDirs = Utilities.nextToABarrel();
               int numAdjacentBarrels = 0;
               if(k==KeyEvent.VK_UP && barrelDirs[LocationBuilder.NORTH])
               {
                  selectedBarrel = Utilities.getNPCAtFromScreenRange(equalizeRow(r-1), c, mapIndex);
               }
               else if(k==KeyEvent.VK_DOWN && barrelDirs[LocationBuilder.SOUTH])
               {
                  selectedBarrel = Utilities.getNPCAtFromScreenRange(equalizeRow(r+1), c, mapIndex);
               }
               else if(k==KeyEvent.VK_LEFT && barrelDirs[LocationBuilder.WEST])
               {
                  selectedBarrel = Utilities.getNPCAtFromScreenRange(r, equalizeCol(c-1), mapIndex);
               }
               else if(k==KeyEvent.VK_RIGHT && barrelDirs[LocationBuilder.EAST])
               {
                  selectedBarrel = Utilities.getNPCAtFromScreenRange(r, equalizeCol(c+1), mapIndex);
               }
            }
            else   //item has been selected - now push the item in the direction of the arrow if clear
            {
               int wardrobeDir = Utilities.nextToOurWardrobe();
               int bedDir = Utilities.nextToOurBed(player.getRow(), player.getCol());
               if(wardrobeDir != -1)      //we are trying to push a wardrobe here
               {
                  if(wardrobeDir == TerrainBuilder.NORTH)      //wardrobe is above us
                  {//VK_UP: see if two spots above us is in bounds and purple_floor: if so, swap it with the wardrobe
                     if(k==KeyEvent.VK_UP && Utilities.spotIsOurHouse(r-2, c))
                        Utilities.swapTerrain(r-1, c, r-2, c);
                     else if(k==KeyEvent.VK_RIGHT && Utilities.spotIsOurHouse(r-1, c+1))
                        Utilities.swapTerrain(r-1, c, r-1, c+1); 
                     else if(k==KeyEvent.VK_DOWN && Utilities.spotIsOurHouse(r, c))
                        Utilities.swapTerrain(r-1, c, r, c); 
                     else if(k==KeyEvent.VK_LEFT && Utilities.spotIsOurHouse(r-1, c-1))
                        Utilities.swapTerrain(r-1, c, r-1, c-1);  
                  }
                  else if(wardrobeDir == TerrainBuilder.EAST)
                  {
                     if(k==KeyEvent.VK_UP && Utilities.spotIsOurHouse(r-1, c+1))
                        Utilities.swapTerrain(r, c+1, r-1, c+1);
                     else if(k==KeyEvent.VK_RIGHT && Utilities.spotIsOurHouse(r, c+2))
                        Utilities.swapTerrain(r, c+1, r, c+2); 
                     else if(k==KeyEvent.VK_DOWN && Utilities.spotIsOurHouse(r+1, c+1))
                        Utilities.swapTerrain(r, c+1, r+1, c+1); 
                     else if(k==KeyEvent.VK_LEFT && Utilities.spotIsOurHouse(r, c))
                        Utilities.swapTerrain(r, c+1, r, c); 
                  }
                  else if(wardrobeDir == TerrainBuilder.SOUTH)
                  {
                     if(k==KeyEvent.VK_UP && Utilities.spotIsOurHouse(r, c))
                        Utilities.swapTerrain(r+1, c, r, c);
                     else if(k==KeyEvent.VK_RIGHT && Utilities.spotIsOurHouse(r+1, c+1))
                        Utilities.swapTerrain(r+1, c, r+1, c+1); 
                     else if(k==KeyEvent.VK_DOWN && Utilities.spotIsOurHouse(r+2, c))
                        Utilities.swapTerrain(r+1, c, r+2, c); 
                     else if(k==KeyEvent.VK_LEFT && Utilities.spotIsOurHouse(r+1, c-1))
                        Utilities.swapTerrain(r+1, c, r+1, c-1);
                  }
                  else //if(wardrobeDir == TerrainBuilder.WEST)
                  {
                     if(k==KeyEvent.VK_UP && Utilities.spotIsOurHouse(r-1, c-1))
                        Utilities.swapTerrain(r, c-1, r-1, c-1);
                     else if(k==KeyEvent.VK_RIGHT && Utilities.spotIsOurHouse(r, c))
                        Utilities.swapTerrain(r, c-1, r, c); 
                     else if(k==KeyEvent.VK_DOWN && Utilities.spotIsOurHouse(r+1, c-1))
                        Utilities.swapTerrain(r, c-1, r+1, c-1); 
                     else if(k==KeyEvent.VK_LEFT && Utilities.spotIsOurHouse(r, c-2))
                        Utilities.swapTerrain(r, c-1, r, c-2);
                  }
               }
               else if(bedDir!=-1) //pushing our bed
               {
                  int bedOneRow = ourBeds[0].getRow();
                  int bedOneCol = ourBeds[0].getCol();
                  int bedTwoRow = ourBeds[1].getRow();
                  int bedTwoCol = ourBeds[1].getCol();
                  Coord leftBed = null;
                  if(k==KeyEvent.VK_UP && Utilities.spotIsOurHouse(bedOneRow-1, bedOneCol) && Utilities.spotIsOurHouse(bedTwoRow-1, bedTwoCol))
                  {
                     Utilities.swapTerrain(bedOneRow, bedOneCol, bedOneRow-1, bedOneCol);
                     Utilities.swapTerrain(bedTwoRow, bedTwoCol, bedTwoRow-1, bedTwoCol);
                     leftBed = new Coord(bedOneRow-1, bedOneCol);
                  }
                  else if(k==KeyEvent.VK_RIGHT && (Utilities.spotIsOurHouse(bedOneRow, bedOneCol+1) || Utilities.spotIsOurHouse(bedTwoRow, bedTwoCol+1)))
                  {
                     Utilities.swapTerrain(bedTwoRow, bedTwoCol, bedTwoRow, bedTwoCol+1);
                     Utilities.swapTerrain(bedOneRow, bedOneCol, bedOneRow, bedOneCol+1);
                     leftBed = new Coord(bedOneRow, bedOneCol+1);
                  }
                  else if(k==KeyEvent.VK_DOWN && Utilities.spotIsOurHouse(bedOneRow+1, bedOneCol) && Utilities.spotIsOurHouse(bedTwoRow+1, bedTwoCol))
                  {
                     Utilities.swapTerrain(bedOneRow, bedOneCol, bedOneRow+1, bedOneCol);
                     Utilities.swapTerrain(bedTwoRow, bedTwoCol, bedTwoRow+1, bedTwoCol);
                     leftBed = new Coord(bedOneRow+1, bedOneCol);
                  }
                  else if(k==KeyEvent.VK_LEFT && (Utilities.spotIsOurHouse(bedOneRow, bedOneCol-1) || Utilities.spotIsOurHouse(bedTwoRow, bedTwoCol-1)))
                  {
                     Utilities.swapTerrain(bedOneRow, bedOneCol, bedOneRow, bedOneCol-1);
                     Utilities.swapTerrain(bedTwoRow, bedTwoCol, bedTwoRow, bedTwoCol-1);
                     leftBed = new Coord(bedOneRow, bedOneCol-1);
                  }
                  if(leftBed!=null)      //fix the home-row/col for a spouse
                  {
                     NPCPlayer spouse = Utilities.findSpouse();
                     if(spouse!=null)
                     {
                        spouse.setHomeRow(leftBed.getRow());
                        spouse.setHomeCol(leftBed.getCol());
                     }
                  }
               }
               else
               {
                  if(k==KeyEvent.VK_UP && !Utilities.NPCAt(equalizeRow(selectedBarrel.getRow()-1), selectedBarrel.getCol(), mapIndex) && LocationBuilder.isDryLand(currMap, equalizeRow(selectedBarrel.getRow()-1), selectedBarrel.getCol()))
                  {  //System.out.println("move up");
                     selectedBarrel.moveNorth();
                  }
                  else if(k==KeyEvent.VK_DOWN && !Utilities.NPCAt(equalizeRow(selectedBarrel.getRow()+1), selectedBarrel.getCol(), mapIndex) && LocationBuilder.isDryLand(currMap, equalizeRow(selectedBarrel.getRow()+1), selectedBarrel.getCol()))
                  {  //System.out.println("move down");
                     selectedBarrel.moveSouth();
                  }
                  else if(k==KeyEvent.VK_LEFT && !Utilities.NPCAt(selectedBarrel.getRow(), equalizeCol(selectedBarrel.getCol()-1), mapIndex) && LocationBuilder.isDryLand(currMap, selectedBarrel.getRow(), equalizeCol(selectedBarrel.getCol()-1)))
                  {  //System.out.println("move left");
                     selectedBarrel.moveWest();
                  }
                  else if(k==KeyEvent.VK_RIGHT && !Utilities.NPCAt(selectedBarrel.getRow(), equalizeCol(selectedBarrel.getCol()+1), mapIndex) && LocationBuilder.isDryLand(currMap, selectedBarrel.getRow(), equalizeCol(selectedBarrel.getCol()+1)))
                  {  //System.out.println("move right");
                     selectedBarrel.moveEast();
                  }
               }
               pushSel = false;
               selectedBarrel = null;
            }
         }
         if(Utilities.canBreakWall())
         {
            locType = player.getLocationType().toLowerCase();
            boolean[] wallDirs = Utilities.nextToAWall();
            boolean[] doorDirs = Utilities.nextToAWoodDoor();
            boolean hasHammer = (player.getImageIndex()==Player.HAMMER);
            boolean hasAxe = (player.getImageIndex()==Player.AXE || player.getImageIndex()==Player.DUALAXE);
            boolean hasSpell = (player.getWeapon().getName().contains("Phasewall"));
            boolean cutOutOfGraboid = ((player.getLocationType().contains("graboid") || player.getLocationType().contains("beast")) &&  //we can cut our way out of a graboid's innnards
               (player.getImageIndex()==Player.AXE || player.getImageIndex()==Player.DUALAXE || player.getImageIndex()==Player.DAGGER
               || player.getImageIndex()==Player.SPEAR || player.getImageIndex()==Player.SABER || player.getImageIndex()==Player.SWORDSHIELD
               || player.getImageIndex()==Player.DUAL || player.getImageIndex()==Player.WOLF));
            if(breakSel)
            {       
               byte locDir = LocationBuilder.keyEventToDir(k);  
               //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
               Coord rowCol = Utilities.dirModifierKey(k, player.getRow(), player.getCol());
               int newRow = rowCol.getRow();
               int newCol = rowCol.getCol();      
               player.setOnGuard(true);
               if(cutOutOfGraboid)
               {
                  Sound.NPCdamage((Math.random()*50)+50, 100);
               }
               else if(hasHammer || hasAxe || player.isWolfForm())
               {
                  Sound.hammerWall();
               }
               if((k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT) && 
                 (((hasHammer || hasSpell || cutOutOfGraboid) && wallDirs[locDir]==true) || ((hasAxe || player.isWolfForm()) && doorDirs[locDir]==true)) && 
                 !LocationBuilder.outOfBounds(currMap, newRow, newCol))
               {
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[newRow][newCol])).getName().toLowerCase();
                  if(Utilities.hammerWallSuccess(terr))
                  {
                     byte wallValue = (byte)(Math.abs(currMap[newRow][newCol]));
                     byte replacementTerrain = LocationBuilder.getAdjacentFloor(currMap, newRow, newCol);
                     if(replacementTerrain != -1)
                     {
                        currMap[newRow][newCol] = replacementTerrain;
                     }
                     else
                     {
                        currMap[newRow][newCol] = currMap[player.getRow()][player.getCol()];
                     }
                     if(!Utilities.nextToOurHouse(newRow, newCol))
                     {  //changes to our house should be permanent
                        int restoreDay = Utilities.getWallRestoreDay(locType, wallValue);
                        tilesToRestore.add(new RestoreItem(player.getMapIndex(), newRow, newCol, wallValue, restoreDay));
                     }
                     else
                     {
                        currMap[newRow][newCol] = TerrainBuilder.getTerrainWithName("INR_$Purple_floor_inside").getValue();
                     }
                     if(Utilities.caughtHammeringWall())
                     {
                        CultimaPanel.player.addReputation(-1);
                        if(locType.contains("temple"))
                           sendMessage("~"+ NPC.getRandomFrom(NPC.vandalTemple));
                        else
                           sendMessage("~"+ NPC.getRandomFrom(NPC.vandalGeneral));
                        Utilities.alertGuards();
                     }
                     else
                        sendMessage("Broken!");
                     if(hasSpell)
                     {
                        Sound.castSpell();
                        player.setManna(player.getManna() - player.getWeapon().getMannaCost());
                     }
                     else
                        CultimaPanel.player.specificExperience[Player.MIGHT]++;
                  }
                  else
                  {
                     if(Utilities.caughtHammeringWall())
                     {
                        CultimaPanel.player.addReputation(-1);
                        if(locType.contains("temple"))
                           sendMessage("~"+ NPC.getRandomFrom(NPC.vandalTemple));
                        else
                           sendMessage("~"+ NPC.getRandomFrom(NPC.vandalGeneral));
                        Utilities.alertGuards();
                     }
                     else
                     {
                        if(player.getManna() < player.getWeapon().getMannaCost())
                           sendMessage("Not enough manna.");
                        else
                           sendMessage("Unsuccessful.");
                     }
                  }  
               }
               breakSel = false;
            }//end if(breakSel)
         }//end if(canBreakWall)
         else if(Utilities.canSetOnFire())
         {
            byte locDir = LocationBuilder.keyEventToDir(k);  
            //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
            Coord rowCol = Utilities.dirModifierKey(k, player.getRow(), player.getCol());
            int newRow = rowCol.getRow();
            int newCol = rowCol.getCol();      
            locType = player.getLocationType().toLowerCase();
            boolean[] wallDirs = Utilities.nextToACombustable();
            if(fireSel)
            {               
               if((k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT) && wallDirs[locDir]==true && !LocationBuilder.outOfBounds(currMap, newRow, newCol))
               {
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[newRow][newCol])).getName().toLowerCase();
                  if(Utilities.lightFireSuccess())
                  {
                     player.castSpell("fireguard");
                     NPCPlayer fire = new NPCPlayer(NPC.FIRE, newRow, newCol, player.getMapIndex(), locType);
                     fire.setBody(Utilities.rollDie(15,35));
                     CultimaPanel.worldMonsters.add(fire);                         
                     if(Utilities.caughtStartingFire())
                     {
                        CultimaPanel.player.addReputation(-1);
                        if(locType.contains("temple"))
                           sendMessage("~"+ NPC.getRandomFrom(NPC.arsonTemple));
                        else
                           sendMessage("~"+ NPC.getRandomFrom(NPC.arsonGeneral));
                        Utilities.alertGuards();
                     }
                     else
                        sendMessage("Ignited!");
                  }
                  else
                  {
                     if(Utilities.caughtStartingFire())
                     {
                        CultimaPanel.player.addReputation(-1);
                        if(locType.contains("temple"))
                           sendMessage("~"+ NPC.getRandomFrom(NPC.arsonTemple));
                        else
                           sendMessage("~"+ NPC.getRandomFrom(NPC.arsonGeneral));
                        Utilities.alertGuards();
                     }
                     else
                     {
                        sendMessage("Unsuccessful.");
                     }
                  }  
               }
               fireSel = false;
               repaint();
               return;
            }//end if(fireSel)
         }//end if(canSetOnFire)
      
         if(Utilities.chestInRange() || nextToNPCtoPickpocket)
         {
            byte locDir = LocationBuilder.keyEventToDir(k);
            NPCPlayer npcDir = Utilities.scanForNPCtoPickpocket(locDir);
            Object[] infoDir = Utilities.scanForChest(locDir);
          
            Terrain chest = TerrainBuilder.getTerrainWithName("ITM_D_$Chest");
            Terrain book = TerrainBuilder.getTerrainWithName("ITM_D_$Book");
            Terrain chestTrap = TerrainBuilder.getTerrainWithName("ITM_D_T_$Chest_trap");
         
            if(stealSel)      //we are trying to steal a chest, so look for a direction key to pick our selected target
            {
               int theftBonus = 0;
               if(time >=20 || time <= 6)
                  theftBonus = 2;
               theftBonus += (weather/4);
               boolean monsterAttack = (monsterAndCivInView != null);
               if(monsterAttack)    //people are distracted if there is a monster roaming about - give us a theft bonus
                  theftBonus += 2;   
               if((k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT) && (npcDir!=null || infoDir!=null))
               {
                  if(npcDir != null) //pickpocket
                  {
                     String NPCterr = CultimaPanel.allTerrain.get(Math.abs(currMap[npcDir.getRow()][npcDir.getCol()])).getName().toLowerCase();
                     if(NPCterr.contains("bed"))    //NPC is sleeping
                        theftBonus += 2;                                            //theftBonus can be as high as 4, so add 40& to player agility to attempt the theft
                     if(!Utilities.caughtPickpocketing(npcDir))
                     {
                        ArrayList<Item> items = npcDir.getStealableItems();
                     
                        if(npcDir.hasMissionItem() && npcDir.getWeapon().getName().toLowerCase().contains("dagger"))
                        {
                           Weapon weap = npcDir.getWeapon();
                           if(player.canAddWeapon(weap))
                           {
                              player.addWeapon(weap);
                              npcDir.setWeapon(Weapon.getFists());
                              npcDir.removeItem("dagger");
                              sendMessage("You stole a " +weap.getName()+"!");
                           }
                           else
                              sendMessage("You have no room to carry that "+weap.getName());
                        }
                        else if(items.size() == 0 && npcDir.getGold()==0)
                           sendMessage("Empty pockets.");
                        else if(items.size() == 0 || Math.random() < 0.5)    //steal gold
                        {
                           int stolenGold = npcDir.getGold();
                           npcDir.setGold(0);
                           player.addGold(stolenGold);
                           if(stolenGold > 0)
                              sendMessage("You stole "+stolenGold+" gold.");
                           CultimaPanel.player.specificExperience[Player.AGILITY]++;
                           player.addExperience(1);
                           if(npcDir.hasTrained())
                           {
                              CultimaPanel.sendMessage("The gods curse thee for theft of thy teacher!");
                              CultimaPanel.sendMessage("Thy precious magic items turn to dust!");
                              player.clearMagicItems();
                              player.clearMagicWeapons();
                              player.clearMagicArmor();
                              player.addEffectForce("curse"); 
                              player.addEffectForce("sullied");
                              player.setSulliedValue(200);
                           }
                        }
                        else                                            //steal random item
                        {
                           Item stolenItem = Utilities.getRandomFrom(items);
                           npcDir.removeItem(stolenItem.getName());
                           player.addItem(stolenItem.getName());
                           if(stolenItem.isBook())
                           {    
                              boolean success = Journal.copyFromBook(stolenItem);
                              if(success)
                                 sendMessage("Found " + stolenItem.getName() + " and copied items into your journal");
                              else
                                 sendMessage("Found " + stolenItem.getName());
                           }
                           else
                           {
                              if(Utilities.startsWithVowel(stolenItem.getName()))
                                 sendMessage("You stole an "+stolenItem.getName()+".");
                              else
                                 sendMessage("You stole a "+stolenItem.getName()+".");
                           }
                           CultimaPanel.player.specificExperience[Player.AGILITY]++;
                           player.addExperience(1);
                        }
                     }
                     else
                     {
                        CultimaPanel.player.addReputation(-1);
                        sendMessage("~"+ NPC.getRandomFrom(NPC.thiefGeneral));
                        Utilities.alertGuards();
                     }
                  }
                  else                 //steal chest
                  {
                     Coord chestLoc = (Coord)(infoDir[0]);
                     byte chestType = ((Byte)(infoDir[1])).byteValue();
                     if(Utilities.rollDie(51) < (player.getAgility()*(1+theftBonus/10.0)))
                     {
                        byte chestValue = (byte)(Math.abs(currMap[chestLoc.getRow()][chestLoc.getCol()]));
                        int restoreDay = Utilities.getChestRestoreDay(player.getLocationType());
                        if(chestType != book.getValue())
                           tilesToRestore.add(new RestoreItem(player.getMapIndex(), chestLoc.getRow(), chestLoc.getCol(), chestValue, restoreDay));
                        currMap[chestLoc.getRow()][chestLoc.getCol()] = currMap[player.getRow()][player.getCol()];
                        //if there is a non-player NPC marker there to mark a mission item for the Knowing spell, remove it (because we just stole the item)
                        NPCPlayer nonP = Utilities.getNPCAt(chestLoc.getRow(), chestLoc.getCol(),player.getMapIndex());
                        if(nonP!=null && nonP.isNonPlayer())
                           Utilities.removeNPCat(chestLoc.getRow(), chestLoc.getCol(),player.getMapIndex()); 
                        if(chestType == chestTrap.getValue())
                        {
                           Utilities.setOffTrap();
                        }
                        else
                        {
                           if(chestType == book.getValue())
                           {
                              Item thisBook = Item.getRandomBook();
                              boolean success = Journal.copyFromBook(thisBook);
                              if(success)
                                 sendMessage("Found " + thisBook.getName() + " and copied items into your journal");
                              else
                                 sendMessage("Found " + thisBook.getName());
                              //player.addItem(thisBook.getName());
                           }
                           else
                           {
                              int gold = (int)(Math.random() * 20) + 1;
                              player.addGold(gold);
                              sendMessage("Found " + gold + " gold!");
                           }
                           player.addExperience(1);
                           CultimaPanel.player.specificExperience[Player.AGILITY]++;
                        }
                        if(Utilities.caughtStealingChest())
                        {
                           if(TerrainBuilder.habitablePlace(player.getLocationType()))
                           {
                              CultimaPanel.player.addReputation(-1);
                              Player.stats[Player.ITEMS_STOLEN]++;
                           }
                           sendMessage("~"+ NPC.getRandomFrom(NPC.thiefGeneral));
                           Utilities.alertGuards();
                        }
                        Player.stats[Player.ITEMS_STOLEN]++;
                     }
                     else
                     {
                        sendMessage("Unsuccessful.");
                     }
                  }
                  stealSel = false;
                  repaint();
                  return;
               }
               else
               {
                  clearSelected();
                  selected = null;
                  selectedBarrel = null;
                  selectedTerrain = null;
                  selectedRenovate = null;
                  selectedBuild = null;
               }  
            }
            else if(!flight  && doorToClose==null && !isWolf && k==KeyEvent.VK_S && !player.onBedAndTimeToSleep() && (nextToNPCtoPickpocket || humanNPCInSight))      //steal
            {
               player.setOnGuard(false);
               clearSelected();
               stealSel = true;
               repaint();
               return;
            }
         }
         if(!flight && k==KeyEvent.VK_P && player.isOnStone(currentPos))   //pick up stone
         {
            if(player.canPickupStone())
            {
               player.addStone();
               sendMessage("You find a fine stone for throwing.");
            }
            else
            {
               sendMessage("You can't hold any more stones.");  
            }
         }
         if(!flight && k==KeyEvent.VK_H && player.canHide(currentPos))
         {
            if(player.isHiding())
            {
               player.setHide(false);
            }
            else
            {
               player.setHide(true);
            }
         }
         else if(!flight && k==KeyEvent.VK_F && player.canCutDownTree())
         {   //code to cut down tree - try to add lumber
            int restoreDay = CultimaPanel.days + 20 + Utilities.rollDie(10); 
            byte treeValue = (byte)Math.abs(currMap[player.getRow()][player.getCol()]);
            tilesToRestore.add(new RestoreItem(player.getMapIndex(), player.getRow(), player.getCol(), treeValue, restoreDay));
            if(currPosName.contains("dense_forest"))
            {
               currMap[player.getRow()][player.getCol()] = TerrainBuilder.getTerrainWithName("TER_$Forest").getValue();
            }
            else //if(currPosName.contains("forest"))
            {
               currMap[player.getRow()][player.getCol()] = TerrainBuilder.getTerrainWithName("TER_$High_grassland").getValue();
            }
            player.pickupLumber();
         }
         if(onACorpse!=null)
         {
            Armor corpseArmor = onACorpse.getArmor();
            boolean isPelt = false;
            if(corpseArmor != null)
            {
               String armName = corpseArmor.getName().toLowerCase();
               if(armName.contains("fur") || armName.contains("skin") || armName.contains("pelt") || armName.contains("hide"))
                  isPelt = true;
            }
            if(k==KeyEvent.VK_K && corpseArmor!=null)      //take corpse armor if you have room
            {
               player.setOnGuard(false);
               boolean canPlayerCarry = true;
               if(player.hasItem("holdall") && player.numArmor() > 10 && !isPelt)
                  canPlayerCarry = false;
               if(!player.hasItem("holdall") && player.numArmor() > 2 && !isPelt)
                  canPlayerCarry = false;
               if(!canPlayerCarry)
                  sendMessage("You have not the room to carry this item.");
               else
               {
                  onACorpse.setArmor(null);
                  player.addArmor(corpseArmor);
                  if(isPelt)
                  {
                     Player.stats[Player.PELTS_COLLECTED]++;
                     sendMessage("You skinned: " + corpseArmor.getName());
                  }
                  else
                     sendMessage("You looted: " + corpseArmor.getName());
               }
            }
            else if((k==KeyEvent.VK_BRACELEFT || k==KeyEvent.VK_OPEN_BRACKET) && corpseArmor!=null && !isPelt)  //swap corpse armor with current
            {
               player.setOnGuard(false);
               Armor dropped = player.getArmor();
               if(dropped.getValue()==0)
               {
                  sendMessage("Thou must be wearing armor to drop it.");
               }
               else
               {
                  onACorpse.setArmor(dropped);
                  player.discardCurrentArmor();
                  player.addArmorAndSwitch(corpseArmor);
                  sendMessage("You dropped:"+dropped.getName()+" and picked up:"+corpseArmor.getName());
               }
            }
            else if(k==KeyEvent.VK_L && onACorpse.getWeapon()!=null)      //take corpse weapon if you have room
            {
               player.setOnGuard(false);
               Weapon temp = onACorpse.getWeapon();
               if(!player.canAddWeapon(temp))
                  sendMessage("You have not the room to carry this item.");
               else
               {
                  onACorpse.setWeapon(null);
                  player.addWeapon(temp);
                  sendMessage("You looted: " + temp.getName());
               }
            }
            else if((k==KeyEvent.VK_BRACERIGHT || k==KeyEvent.VK_CLOSE_BRACKET) && onACorpse.getWeapon()!=null)  //swap corpse weapon with current
            {
               player.setOnGuard(false);
               Weapon dropped = player.getWeapon();
               if(!player.canDropWeapon(dropped))
               {
                  sendMessage("Select a weapon that can be dropped...");
               }
               else
               {
                  Weapon temp = onACorpse.getWeapon();
                  onACorpse.setWeapon(dropped);
                  player.discardCurrentWeapon();
                  player.addWeaponAndSwitch(temp);
                  sendMessage("You dropped:"+dropped.getName()+" and picked up:"+temp.getName());
               }
            }
         }
         boolean leavingLocation = false; //used to see if we are leaving a location to return to the next in the Teleporter stack
         selected = null;
         selectedTerrain = null;
         //selectedRenovate = null;
         NPCPlayer stolenShip = null;  //not null if we have successfully stolen a ship from a port
      
      //************
         if(mapIndex == 0)             //navigating in the world has wrap-around
         {
            player.setLocationType("world");
            player.clearMemory();      //we are in the greater world, so clear teleporter memory
            CultimaPanel.NPCsInBed = false;
            CultimaPanel.NPCsOnStreet = false;
            boolean closeToWisp = Utilities.closeToWisp();  //see if we are close enough to a Wisp so that it leads us into a trap
            if(Utilities.rollDie(2000) <= 1 || closeToWisp) //monster army
            {
               int armyType = (int)(Math.random()*4);
               if(armyType == 0 || closeToWisp)
                  orcArmy = true;
               else if(armyType == 1)
                  trollArmy = true;
               else if(armyType == 2)
                  brigandArmy = true;
               else wolfPack = true;
               armyGeneral = false;
            }
            if(k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT)
            {  //every 10 moves in the world, tell us the narrative of what kind of terrain we are navigating over
               numMoves++;
               if(numMoves == Integer.MAX_VALUE)
               {
                  numMoves = 0;
               }
               if(numMoves % 10 == 0)
               {
                  Utilities.reportTerrainNarrative(currentPos);
               }
               byte locDir = LocationBuilder.keyEventToDir(k);
               //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
               Coord rowCol = Utilities.dirModifierKey(k, r, c);
               int newRow = rowCol.getRow();
               int newCol = rowCol.getCol();
               int origR = r, origC = c;
               String toTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(newRow)][equalizeCol(newCol)])).getName();
               boolean movingOnToLand = false;
               if(!toTerrain.toLowerCase().contains("water"))
               {
                  movingOnToLand = true;
               }
               if(sailing && Utilities.oppositeDir(windDir, k) && Math.random() < 0.75 && !movingOnToLand)
               {  //if we are in a boat or ship and trying to sail or row into the winds, make it a bit harder, so learn the Shiftwind spell
                  sendMessage("Fighting the winds.");
                  return;
               }  
               if(flight && Utilities.oppositeDir(windDir, k) && Math.random() < 0.5)
               {  //if we are trying to fly into the winds, make it a bit harder, so learn the Shiftwind spell
                  sendMessage("Fighting the winds.");
                  return;
               }      
               byte shipType = Utilities.shipAt(NPCsInOurMap, equalizeRow(newRow), equalizeCol(newCol), mapIndex);  
               boolean horseTo = Utilities.horseAt(NPCsInOurMap, equalizeRow(newRow), equalizeCol(newCol), mapIndex);  
               boolean iceTo = (Display.frozenWater() && toTerrain.toLowerCase().contains("water") && !toTerrain.toLowerCase().contains("rapid"));
               if(player.onHorse())
                  horseTo = false;
               if(player.isWolfForm() && shipType!=-1)
                  dir = "blocked";   
               else
                  if(iceTo || horseTo || (((toTerrain.toLowerCase().contains("water") && shipType!= -1)  || (toTerrain.toLowerCase().contains("bog") && shipType==NPC.BOAT)) && !player.onHorse()) || ((!toTerrain.contains("_I_") || flight || sailing) && !Utilities.NPCAtMinusDog(NPCsInOurMap, equalizeRow(newRow), equalizeCol(newCol), mapIndex)))
                  {
                     r = newRow;
                     c = newCol;
                     dir = LocationBuilder.keyEventToDirName(k);
                     if((toTerrain.toLowerCase().contains("water") && shipType!= -1)  || (toTerrain.toLowerCase().contains("bog") && shipType==NPC.BOAT))
                     {
                        player.clearActiveSpells();
                        player.setLastWeaponIndex(player.getImageIndex());
                        player.setLastWeaponSelect(player.getWeaponSelect());
                        if(shipType==NPC.GREATSHIP)
                           player.setImageIndexTemp(Player.GREATSHIP);
                        else if(shipType==NPC.BRIGANDSHIP)
                           player.setImageIndexTemp(Player.BRIGANDSHIP);
                        else if(shipType==NPC.BOAT)
                           player.setImageIndexTemp(Player.BOAT);
                        player.setOnGuard(false); 
                        if(!(Utilities.getNPCAtFromScreenRange(equalizeRow(r), equalizeCol(c), mapIndex)).hasMet() && (shipType==NPC.GREATSHIP || shipType==NPC.BRIGANDSHIP))
                        {
                           player.stats[Player.SHIPS_COMMANDEERED]++;
                           if(CultimaPanel.player.stats[Player.SHIPS_COMMANDEERED] >= 5)
                              Achievement.earnAchievement(Achievement.ADMIRAL_OF_THE_NAVY);
                           Sound.waterSound();   
                        }
                        Utilities.removeNPCat(equalizeRow(r), equalizeCol(newCol), mapIndex);
                        NPCPlayer dog = Utilities.getDog();
                        if(dog!=null)
                           player.setDogBasicInfo(dog.basicInfo());
                        else
                           player.setDogBasicInfo("none");
                        Utilities.removeDogFromLocation();
                        NPCPlayer npc = Utilities.getNpc();
                        if(npc!=null)
                           player.setNpcBasicInfo(npc.basicInfo());
                        else
                           player.setNpcBasicInfo("none");
                        Utilities.removeNpcFromLocation();
                     }
                     else if((!toTerrain.toLowerCase().contains("water") || iceTo) && sailing && !(toTerrain.toLowerCase().contains("bog") && player.onBoat()))
                     {
                     //leave our ship there
                        byte leaveShip = NPC.GREATSHIP;
                        if(player.getImageIndex()==Player.BRIGANDSHIP)
                           leaveShip = NPC.BRIGANDSHIP;
                        else  if(player.getImageIndex()==Player.BOAT)
                           leaveShip = NPC.BOAT;
                        NPCPlayer ourShip = new NPCPlayer(leaveShip, equalizeRow(origR), equalizeCol(origC), 0, "world"); 
                        ourShip.setBody(100);  
                        ourShip.setHasMet(true);
                        if(leaveShip == NPC.BOAT)
                        {
                           boats.add(ourShip);
                        }
                        else
                        {
                           worldMonsters.add(ourShip);
                        }
                        player.setImageIndex(player.getLastWeaponIndex());
                        player.setWeaponSelect(player.getLastWeaponSelect());
                        player.setOnGuard(false);
                        Utilities.addDogToLocation(dir);
                        Utilities.addNpcToLocation(dir);
                     }
                     else if(player.onHorse() && toTerrain.startsWith("LOC_"))      //dismount horse going into a location
                     { //we don't call player.disMountHorse here because we need to leave the horse in the world at our old location, not our current one
                        byte horseType = NPC.HORSE;
                        if(player.onUnicorn())
                           horseType = NPC.UNICORN;
                        NPCPlayer ourHorse = new NPCPlayer(horseType, equalizeRow(origR), equalizeCol(origC), 0, "world", player.getHorseBasicInfo(),true);   
                        ourHorse.setHasMet(true);
                        if(horseType == NPC.UNICORN && ourHorse.getReputation() % 2 != 0) //a unicord with an odd reputation can't be mounted
                           ourHorse.setReputation(ourHorse.getReputation()+1);            //if we are dismounting, make sure the reputation is even so we can ride it again
                        worldMonsters.add(ourHorse);
                        player.setImageIndex(player.getLastWeaponIndex());
                        player.setWeaponSelect(player.getLastWeaponSelect());
                        player.setOnGuard(false);
                        player.setHorseBasicInfo("none");
                     }
                     else if(horseTo && player.canMountHorse())
                     {
                        player.clearActiveSpells();
                        player.setLastWeaponIndex(player.getImageIndex());
                        player.setLastWeaponSelect(player.getWeaponSelect());
                        NPCPlayer horse = Utilities.getNPCAtFromScreenRange(equalizeRow(newRow), equalizeCol(newCol), mapIndex);
                        player.mountHorse(horse);
                        Utilities.removeNPCat(equalizeRow(newRow), equalizeCol(newCol), mapIndex);
                     }
                  }
                  else
                     dir = "blocked";
               player.setLastDir(locDir);
            }
            else if(Utilities.canSetOnFire() && k==KeyEvent.VK_F)                   //Start fire
            {
               clearSelected();
               fireSel = true;
               repaint();
               return;
            }
            else if(!flight && (Utilities.nextToABarrelSimple() || Utilities.nextToOurWardrobe()!=-1 || Utilities.nextToOurBed(player.getRow(), player.getCol())!=-1) && k==KeyEvent.VK_P)               
            {                                                                       //Push or pull a barrel
               clearSelected();
               pushSel = true;
               selectedBarrel = null;
               repaint();
               return;
            }
            if(!dir.equals("blocked"))   
            { 
               player.setRow(equalizeRow(r));
               player.setCol(equalizeRow(c));
               if(player.onHorse() && gallopTime2 >= 0 && System.currentTimeMillis() > gallopTime2 + 500)
               {
                  Sound.horseGallop(0);
                  gallopTime = System.currentTimeMillis();
                  gallopTime2 = -1;
               } 
               closeTrapOrSecret = Utilities.scanForTrapOrSecret();
               //shift any smoke puffs over depending on direction
               if(smoke.size() > 0)
               {
                  for(int i=0; i<smoke.size(); i++)
                  {
                     SmokePuff curr = smoke.get(i);
                     if(dir.equalsIgnoreCase("North"))
                        curr.setY(curr.getY()+SIZE);
                     else if(dir.equalsIgnoreCase("South"))
                        curr.setY(curr.getY()-SIZE/2);
                     else if(dir.equalsIgnoreCase("East"))
                        curr.setX(curr.getX()-SIZE);
                     else if(dir.equalsIgnoreCase("West"))
                        curr.setX(curr.getX()+SIZE);
                  }
               }
            }   
            player.setWorldRow(equalizeRow(r));
            player.setWorldCol(equalizeRow(c));
            int multiplier = 4;           //more likely to encounter monsters at night
            if(time >= 6 && time <= 20)
               multiplier = 1;
            if(orcArmy || trollArmy || brigandArmy || wolfPack)
            {
               int armySize = Utilities.rollDie(15,27);
               if(wolfPack)
                  armySize = Utilities.rollDie(5,10);
               if(worldMonsters.size() - numWorldMonstersToExclude < armySize)
               {
                  byte monsterIndex = NPC.ORC;
                  if(trollArmy)
                     monsterIndex = NPC.TROLL;
                  else if(brigandArmy)
                     monsterIndex = NPC.randomWorldBrigand();
                  else if(wolfPack)
                     monsterIndex = NPC.WOLF;
                  else if(closeToWisp || (orcArmy && isNight))
                  {
                     monsterIndex = NPC.NIGHTORC;
                  }
                  if(armyGeneral == false && Math.random() < 0.25)
                  {
                     monsterIndex = NPC.getMonsterKing(monsterIndex);
                     armyGeneral = true;
                  }   
                  Coord monsterLoc = Utilities.findMonsterSpawn(dir, monsterIndex);
                  if(monsterLoc != null)
                  {
                     int monsterRow = monsterLoc.getRow();
                     int monsterCol = monsterLoc.getCol();
                     NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                     if(closeToWisp)
                        monster.setMoveType(NPC.CHASE);
                     worldMonsters.add(monster);
                     Mission.checkSlayMission(monster);
                  }
               }
               else
               {
                  if(orcArmy) 
                     orcArmy = false;
                  if(trollArmy)
                     trollArmy = false;   
                  if(brigandArmy) 
                     brigandArmy = false;
                  if(wolfPack) 
                     wolfPack = false;
                  armyGeneral = false;   
               }
            }  
            int spawnRoll = Utilities.rollDie(100);
            int monsterProb = MONSTER_SPAWN_PROB*multiplier;
            boolean monsterShouldSpawn = (spawnRoll < monsterProb);
            int maxWorldMonsters = (NUM_WORLD_MONSTERS + numWorldMonstersToExclude);
            if(monsterShouldSpawn && worldMonsters.size() < maxWorldMonsters)       //random encounter land monster
            {
               byte monsterIndex = NPC.randomWorldMonsterSunny();
               if(weather > 0)
                  monsterIndex = NPC.randomWorldMonsterRaining();
               //***TESTING*** monsterIndex = NPC.CHUD;                              
               if(Utilities.rollDie(125 - ((player.getLevel()%10)*10)) <= 1)                          //chance we will spawn a monster king
               {
                  monsterIndex = NPC.randomMonsterKing();
                  if(player.hasDeathMission() && Math.random() < 0.5 && !holiday)
                     monsterIndex = NPC.DEATH;
               } 
               if(CultimaPanel.nextMonsterSpawn != -1)
                  monsterIndex = CultimaPanel.nextMonsterSpawn; 
               Coord monsterLoc = Utilities.findMonsterSpawn(dir, monsterIndex);
               if(monsterLoc != null)
               {
                  int monsterRow = monsterLoc.getRow();
                  int monsterCol = monsterLoc.getCol();
                  if(monsterIndex==NPC.DRAGON || monsterIndex==NPC.DRAGONKING)
                     Sound.dragonCall();
                  NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                  worldMonsters.add(monster);
                  Mission.checkSlayMission(monster);
                  if(monsterIndex==NPC.SHRIEKINGEEL && currPosName.contains("water"))
                  {
                     Sound.eelCall();
                  }
               }
            }
            if(Utilities.rollDie(100) < WATER_SPAWN_PROB  && worldMonsters.size() < (NUM_WORLD_MONSTERS + numWorldMonstersToExclude) && !Display.frozenWater())                   //random encounter water monster
            {
               byte monsterIndex = NPC.randomWaterMonster();
               if(isNight)
               {
                  monsterIndex = NPC.randomWaterMonsterNight();
               }           
               if(Utilities.rollDie(150 - ((player.getLevel()%10)*10)) <= 1)                           //change monster will be a monster king
               {
                  monsterIndex = NPC.randomWaterMonsterKing();
               }
               Coord monsterLoc = Utilities.findWaterSpawn(monsterIndex);
               if(monsterLoc != null)
               {
                  int monsterRow = monsterLoc.getRow();
                  int monsterCol = monsterLoc.getCol();
                  if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                  {
                     NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                     worldMonsters.add(monster);
                     Mission.checkSlayMission(monster);
                  }
               }
            }
            
            if(Utilities.rollDie(100) < MONSTER_SPAWN_PROB*multiplier && worldMonsters.size() < (NUM_WORLD_MONSTERS + numWorldMonstersToExclude))       //random encounter sand monster
            {
               if(weather == 0 && (time < 20 && time > 6))     //don't spawn sand monsters in the rain or night
               {
                  byte monsterIndex = NPC.randomSandMonster();
                  Coord monsterLoc = Utilities.findSpawnOfType("sand");
                  if(monsterLoc != null)
                  {
                     int monsterRow = monsterLoc.getRow();
                     int monsterCol = monsterLoc.getCol();
                     if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                     {
                        NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                        worldMonsters.add(monster);
                        Mission.checkSlayMission(monster);
                     }
                  }
               }
            }
            if(Utilities.rollDie(100) < MONSTER_SPAWN_PROB*multiplier && worldMonsters.size() < (NUM_WORLD_MONSTERS + numWorldMonstersToExclude))       //random encounter lava monster
            {
               if(weather == 0)     //don't spawn lava monsters in the rain
               {
                  byte monsterIndex = NPC.randomLavaMonster();
                  Coord monsterLoc = Utilities.findSpawnOfType("lava");
                  if(monsterLoc != null)
                  {
                     int monsterRow = monsterLoc.getRow();
                     int monsterCol = monsterLoc.getCol();
                     if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                     {
                        NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                        worldMonsters.add(monster);
                        Mission.checkSlayMission(monster);
                     }
                  }
               }
            }
            if(Utilities.rollDie(100) < MONSTER_SPAWN_PROB*multiplier && worldMonsters.size() < (NUM_WORLD_MONSTERS + numWorldMonstersToExclude))       //random encounter lava monster
            {
               byte monsterIndex = NPC.randomSwampMonster();
               Coord monsterLoc = Utilities.findSpawnOfType("bog");
               if(monsterLoc != null)
               {
                  int monsterRow = monsterLoc.getRow();
                  int monsterCol = monsterLoc.getCol();
                  if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                  {
                     NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                     worldMonsters.add(monster);
                     Mission.checkSlayMission(monster);
                  }
               }
            }
            if(Utilities.rollDie(100) < MONSTER_SPAWN_PROB*3 && worldMonsters.size() < (NUM_WORLD_MONSTERS + numWorldMonstersToExclude) && multiplier==1)//random encounter grazing animal during the day
            {
               byte monsterIndex = NPC.randomGrazingMonster();
               Coord monsterLoc = Utilities.findMonsterSpawn(dir, monsterIndex);
               double unicornProb = 0.25;
               if(Display.isSpring())
                  unicornProb = 0.5;
               if(rainbowAlpha > 0 && Math.random()<unicornProb && Utilities.flowersInView())    //chance it is a unicorn
               {
                  monsterIndex = NPC.UNICORN;
                  if(monsterLoc != null)
                     sendMessage("The mejestic Unicorn appears!");
               }
               if(monsterLoc != null)
               {
                  int monsterRow = monsterLoc.getRow();
                  int monsterCol = monsterLoc.getCol();
                  if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                  {
                     NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                     worldMonsters.add(monster);
                     Mission.checkSlayMission(monster);
                  }
               }
            }
            double distToCaerbannog = Display.wrapDistance(caerbannogRow, caerbannogCol, player.getWorldRow(), player.getWorldCol());
            if(distToCaerbannog <= 6 && !caerbannogBeastSpawn)
            {
               sendMessage("There he is!");
               NPCPlayer monster = new NPCPlayer(NPC.CAERBANNOG, caerbannogRow+1, caerbannogCol, 0, "world");
               worldMonsters.add(monster);
               Sound.caerbannogSpawn();
               caerbannogBeastSpawn = true;
            }
            double escortDist = -1;
            if(escortee != null)
            {  //see if we our so far away from our escortee that they get lost
               escortDist = Utilities.checkDistanceToEscortee(escortee);
            }
            if((System.currentTimeMillis() > cheerTime + 5000) && !talkSel && escortee != null)
            {  //if we are escorting an npc for a mission, see if they want to comment on anything
               String escorteeMessage = "";
               String theirTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(escortee.getRow())][equalizeCol(escortee.getCol())])).getName().toLowerCase();
               if(escortee.getCharIndex()==NPC.CHILD)
               {
                  if(greatestNPCinSight!=null && !NPC.isCivilian(greatestNPCinSight) && !NPC.isTameAnimal(greatestNPCinSight) && greatestNPCinSight.getReputation() < 0 && Math.random() < 0.25)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcMonsterChild).replace("ADJECTIVE", Utilities.getRandomFrom(NPC.insultAdjective)).replace("MONSTER_TYPE", greatestNPCinSight.getName());
                  }
                  else if(theirTerrain.contains("bog") && Math.random() < 0.75)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcSwampChild).replace("ADJECTIVE", Utilities.getRandomFrom(NPC.insultAdjective));
                  }
                  else if(rainbowAlpha > 0 && Math.random() < 0.25)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcRainbowChild);
                  }
                  else if(weather > 5 && Math.random() < 0.25)
                  {
                     String precipType = "rain";
                     if(Display.isWinter())
                     {
                        precipType = "snow";
                     }
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcWeatherChild).replace("ADJECTIVE", Utilities.getRandomFrom(NPC.insultAdjective)).replace("PRECIP",precipType);
                  }
                  else if(isNight && Math.random() < 0.25)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcDarkChild);
                  }
                  else if(escortDist > ((CultimaPanel.SCREEN_SIZE/4)+1) && Math.random() < 0.25)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcTooFarChild);
                  }
               }
               else
               {
                  if(greatestNPCinSight!=null && !NPC.isCivilian(greatestNPCinSight) && !NPC.isTameAnimal(greatestNPCinSight) && greatestNPCinSight.getReputation() < 0 && Math.random() < 0.25)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcMonster).replace("ADJECTIVE", Utilities.getRandomFrom(NPC.insultAdjective)).replace("MONSTER_TYPE", greatestNPCinSight.getName());
                  }
                  else if(theirTerrain.contains("bog") && Math.random() < 0.75)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcSwamp).replace("ADJECTIVE", Utilities.getRandomFrom(NPC.insultAdjective));
                  }
                  else if(rainbowAlpha > 0 && Math.random() < 0.25)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcRainbow);
                  }
                  else if(weather > 5 && Math.random() < 0.25)
                  {
                     String precipType = "rain";
                     if(Display.isWinter())
                     {
                        precipType = "snow";
                     }
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcWeather).replace("ADJECTIVE", Utilities.getRandomFrom(NPC.insultAdjective)).replace("PRECIP",precipType);
                  }
                  else if(isNight && Math.random() < 0.25)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcDark);
                  }
                  else if(escortDist > ((CultimaPanel.SCREEN_SIZE/4)+1) && Math.random() < 0.25)
                  {
                     escorteeMessage = Utilities.getRandomFrom(NPC.npcTooFar);
                  }
               }
               if(escorteeMessage.length() > 0)
               {
                  sendMessage("-----");
                  sendMessage("~"+escorteeMessage);
                  cheerTime = System.currentTimeMillis();
               }
            } 
         }
         else                 //navigating in a Location does not have wrap-around
         {           
            if(player.getLocationType().toLowerCase().contains("underworld") && Utilities.rollDie(1000) <= 1)//monster army
               deaditeArmy = true;
         
            boolean indoorLoc = false;
            locType = player.getLocationType().toLowerCase();
            if(locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("sewer"))
               indoorLoc  = true;
            boolean seeSky = (flight && !indoorLoc);              
            //see if we are next to a tower for the 3-towers puzzle
            int [] tower = null; //a tower for the 3-towers puzzle that we might be next to
            if(player.getLocationType().toLowerCase().contains("cave") && door1!=null && door2!=null && door3!=null  && tower1!=null && tower2!=null && tower3!=null)
            {
               if(player.getRow()==door1.getRow()+1 && player.getCol()==door1.getCol()) 
                  tower = tower1;
               else if(player.getRow()==door2.getRow()+1 && player.getCol()==door2.getCol()) 
                  tower = tower2;
               else if(player.getRow()==door3.getRow()+1 && player.getCol()==door3.getCol()) 
                  tower = tower3;
            }
            increment = LOC_TIME_INCREMENT;
            if(player.isVampire() && (time <8 || time > 16))   //vampires move faster at night
               increment /= 2;
            if(k==KeyEvent.VK_UP || k==KeyEvent.VK_DOWN || k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT)
            {  
               byte locDir = LocationBuilder.keyEventToDir(k);  
               //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
               Coord rowCol = Utilities.dirModifierKey(k, r, c);
               int newRow = rowCol.getRow();
               int newCol = rowCol.getCol();
               boolean newLocInBounds = (!LocationBuilder.outOfBounds(currMap, newRow, newCol));
               if((k==KeyEvent.VK_UP && r==0) || (k==KeyEvent.VK_DOWN && r==currMap.length-1) || (k==KeyEvent.VK_LEFT && c==0) || (k==KeyEvent.VK_RIGHT && c==currMap[0].length-1))
                  leavingLocation = true;
               else
               {            
                  String terrToMoveTo = "_i_";
                  if(newLocInBounds)
                     terrToMoveTo = allTerrain.get(Math.abs(currMap[newRow][newCol])).getName().toLowerCase();
                  boolean canFlyHere = (flight && (terrToMoveTo.contains("water") || terrToMoveTo.contains("lava") || terrToMoveTo.contains("chest") || terrToMoveTo.contains("book")));
                  boolean impassable = terrToMoveTo.contains("_i_");    //can't move up into an Impassable Terrain (all start with "I")
                  boolean blocked = (indoorLoc && flight && (terrToMoveTo.contains("door") || LocationBuilder.isBreakableWall(terrToMoveTo)));
                  boolean iceThere = (Display.frozenWater() && terrToMoveTo.toLowerCase().contains("water") && !terrToMoveTo.toLowerCase().contains("rapid"));
                  if(newLocInBounds && !blocked && (iceThere || seeSky || canFlyHere || (!impassable && !Utilities.NPCAtMinusDog(NPCsInOurMap, newRow, newCol, mapIndex))))
                  {
                     r = newRow;
                     c = newCol;
                     dir = LocationBuilder.keyEventToDirName(k);
                  }
                  else
                     dir = "blocked";
               }
               player.setLastDir(locDir);
            }
            else if(Utilities.canBreakWall() && k==KeyEvent.VK_B)                   //Break wall
            {
               clearSelected();
               breakSel = true;
               repaint();
               return;
            }
            else if(Utilities.canSetOnFire() && k==KeyEvent.VK_F && !player.canCutDownTree())                   //Start fire
            {
               clearSelected();
               fireSel = true;
               repaint();
               return;
            }
            else if(!flight && (Utilities.nextToABarrelSimple() || Utilities.nextToOurWardrobe()!=-1 || Utilities.nextToOurBed(player.getRow(), player.getCol())!=-1) && k==KeyEvent.VK_P)               
            {                                                                       //Push or pull a barrel
               clearSelected();
               pushSel = true;
               selectedBarrel = null;
               repaint();
               return;
            }
            else if(!flight && TerrainBuilder.isCity(player.getLocationType()) && Utilities.onHouseAdjacentGround() && k==KeyEvent.VK_P && !Display.isWinter())               
            {                                                                       //Plant some flowers
               sendMessage("These flowers will look lovely here!");
               currMap[r][c] = Utilities.getRandomFlower();
            }
            else if(k==KeyEvent.VK_P && tower!=null)
            {
               boolean towerHasDisks = false;
               for(int twr=0; twr<tower.length; twr++)
                  if(tower[twr] >= 1)
                  {
                     towerHasDisks = true;
                     break;
                  }
               int topDisk = 0;
               while(topDisk < tower.length && tower[topDisk]==0)
                  topDisk++;
               boolean canPickDisk = (towerHasDisks && player.getWellNumber()<1);
               boolean canPutDisk = (player.getWellNumber()>=1 && (!towerHasDisks || (topDisk > 0 && topDisk < tower.length && player.getWellNumber() < tower[topDisk])));  
               if(canPickDisk || canPutDisk)
               {
                  if(canPickDisk)
                  {
                     player.setWellNumber((byte)(tower[topDisk]));
                     tower[topDisk]=0;
                  }
                  else //if(canPutDisk)
                  {
                     topDisk--;
                     tower[topDisk]=player.getWellNumber(); 
                     player.setWellNumber((byte)(-1)); 
                     //see if we won the towers-puzzle
                     boolean tower1Empty = true;
                     for(int twr=0; twr<tower1.length; twr++)
                        if(tower1[twr]!=0)
                           tower1Empty = false;
                     boolean tower3Empty = true;
                     for(int twr=0; twr<tower3.length; twr++)
                        if(tower3[twr]!=0)
                           tower3Empty = false;
                     if(tower1Empty && tower3Empty)
                     {
                        sendMessage("3-Towers Puzzle complete!");
                       //teleport the mage out with a flash of light
                        CultimaPanel.flashColor = java.awt.Color.red;
                        CultimaPanel.flashFrame = CultimaPanel.numFrames;
                        CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
                        Sound.thunder();
                        for(NPCPlayer p: civilians.get(player.getMapIndex()))
                        {
                           if(p.getName().contains("Puzzle-Master"))
                           {
                              int oldRow = p.getRow();
                              int oldCol = p.getCol();
                              Utilities.removeNPCat(oldRow, oldCol, player.getMapIndex());
                              break;
                           }
                        }
                        player.specificExperience[Player.MIND]++;
                        player.addExperience(100);
                        Sound.levelUp(60);
                        player.stats[Player.PUZZLES_SOLVED]++;
                        if(CultimaPanel.player.stats[Player.PUZZLES_SOLVED] >= 3 && CultimaPanel.player.readAllPuzzleBooks())
                           Achievement.earnAchievement(Achievement.PUZZLE_SLAYER);
                        Item prize = Item.getRandomMagicItemPartial();
                        player.addItem(prize.getName()); 
                        sendMessage("You aquire:" +prize.getName());
                        CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex()).clearRiddlePoints();
                     }      
                  }
               }
               else if(!towerHasDisks)
                  sendMessage("There are no disks on this tower");
               else if(player.getWellNumber()<1)
                  sendMessage("You hold not a disk to put!");
               else if(towerHasDisks && (topDisk > 0 && topDisk < tower.length && player.getWellNumber() > tower[topDisk]))
                  sendMessage("You may not place a larger disk upon a smaller one");
            
               repaint();
               return;
            }
            else if(!flight && !isWolf && (((player.hasItem("lockpick")||player.hasItem("magicpick")) && k==KeyEvent.VK_P)
            || (player.getSpell()!=null && player.getSpell().getName().contains("Disarm") && k==KeyEvent.VK_C)))     //attempt to pick lock: sound alarm if locked picked within proximity to guards/civilians
            {
               double bonus = 1;
               if(player.hasItem("magicpick"))
                  bonus = 1.25;
               player.setOnGuard(false);
               String lockOrTrap = Utilities.nextToALockOrTrap(currMap, r, c);
               if(!lockOrTrap.equals("none"))
               {
                  double total = ((player.getAgility() + (player.getMind()/2))*bonus)/100.0;
                  if(player.getSpell()!=null && player.getSpell().getName().contains("Disarm") && k==KeyEvent.VK_C)
                     total =((player.getMind() + (player.getMind()/2))*bonus)/100.0;
                  boolean success = false;
                  if(Math.random() < total)
                     success = Utilities.pickALock(currMap, r, c); 
                  if(success)
                  {
                     Sound.pickLock();
                  
                     if(lockOrTrap.equals("trap"))
                     {
                        sendMessage("Disarmed!");
                        player.addExperience(1);
                     }
                     else
                     {
                        sendMessage("Success!");
                        if(!zombieAbout && !player.isInvisible() && awareHumanNPCInSight
                        &&(Utilities.rollDie(50)-distToClosestAwareNPC) > player.getAgility()+(weather) && TerrainBuilder.habitablePlace(player.getLocationType()))
                        {
                           sendMessage("~"+ NPC.getRandomFrom(NPC.lockpickGeneral));
                           Utilities.alertGuards();
                        }
                        else
                           player.addExperience(1);
                     }
                     if(player.getSpell()!=null && player.getSpell().getName().contains("Disarm") && !player.hasItem("lockpick") && !player.hasItem("magicpick"))
                        player.setManna(player.getManna() - player.getSpell().getMannaCost());
                  }
                  else
                  {
                     if(lockOrTrap.equals("trap") && Math.random() < 0.5)
                     {
                        Utilities.setOffTrap();
                        Utilities.pickALock(currMap, r, c);
                     }
                     else
                        sendMessage("Failed.");
                  }
               }  
            }   
            if(!dir.equals("blocked"))   
            { 
               player.setRow(r);
               player.setCol(c);
               closeTrapOrSecret = Utilities.scanForTrapOrSecret();
               clearSelected();
               selected = null;
               selectedBarrel = null;
               selectedTerrain = null;
               //shift any smoke puffs over depending on direction
               if(smoke.size() > 0)
               {
                  for(int i=0; i<smoke.size(); i++)
                  {
                     SmokePuff curr = smoke.get(i);
                     if(dir.equalsIgnoreCase("North"))
                        curr.setY(curr.getY()+SIZE);
                     else if(dir.equalsIgnoreCase("South"))
                        curr.setY(curr.getY()-SIZE/2);
                     else if(dir.equalsIgnoreCase("East"))
                        curr.setX(curr.getX()-SIZE);
                     else if(dir.equalsIgnoreCase("West"))
                        curr.setX(curr.getX()+SIZE);
                  }
               }
            }
            String currTerr = allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if((player.getLocationType().equals("port") || player.getLocationType().contains("city")) && currTerr.contains("ship_helm"))
            {
               if(Utilities.caughtStealingShip())
               {
                  CultimaPanel.player.addReputation(-10);
                  sendMessage("~Stop! Shipjacker!");
                  Utilities.alertGuards();
               }
               else
               {
                  byte shipType = NPC.BRIGANDSHIP;
                  if(Math.random() < 0.33)
                  {
                     shipType = NPC.GREATSHIP;
                  }
                  stolenShip = Utilities.placeAndGetShipNear(player.getWorldRow(), player.getWorldCol(), shipType);
                  if(stolenShip != null)
                  {
                     Player.stats[Player.SHIPS_COMMANDEERED]++;
                     if(CultimaPanel.player.stats[Player.SHIPS_COMMANDEERED] >= 5)
                        Achievement.earnAchievement(Achievement.ADMIRAL_OF_THE_NAVY);
                     Player.stats[Player.ITEMS_STOLEN]++;
                     leavingLocation = true;
                     player.addExperience(50);
                     sendMessage("You have stolen a vessel!");
                        //remove helm for a while from the map so we can't steal the ship over and over
                     byte helmValue = (byte)(Math.abs(currMap[r][c]));
                     currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_$Red_floor").getValue();
                     int restoreDay = Utilities.getChestRestoreDay(locType);
                     tilesToRestore.add(new RestoreItem(mapIndex, r, c, helmValue, restoreDay));   
                     Sound.waterSound();
                  }
                  else
                  {
                     CultimaPanel.player.addReputation(-10);
                     sendMessage("~"+NPC.getRandomFrom(NPC.shipJack));
                     Utilities.alertGuards();
                  }
               }
            }
            else if(player.getLocationType().equals("ship"))
            {
               if(numEnemiesOnShip == 0 && (currTerr.contains("ship_helm") || currTerr.contains("red_floor")))
               {
                  leavingLocation = true;
                  player.addExperience(100);
                  Player.stats[Player.SHIPS_COMMANDEERED]++;
                  if(CultimaPanel.player.stats[Player.SHIPS_COMMANDEERED] >= 5)
                     Achievement.earnAchievement(Achievement.ADMIRAL_OF_THE_NAVY);
                  Sound.waterSound();   
               }
            }
            else if(currTerr.contains("wardrobe"))    //go to wardrobe inventory
            {
               menuMode = WARDROBE;
               invIndex = 0;
               itemIndex = 0;
               Sound.silence(3);
               clearSelected();
               selected = null;
               selectedBarrel = null;
               selectedTerrain = null;
               selectedRenovate = null;
               selectedBuild = null;
               return;
            }
            locType = player.getLocationType().toLowerCase();
            if(locType.contains("sewer") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("dungeon") || 
               locType.contains("temple") || player.isInSpecialRealm())
            {
               if(locType.contains("underworld") && deaditeArmy)
               {
                  int armySize = 25;
                  if(worldMonsters.size() - numWorldMonstersToExclude < armySize)
                  {
                     byte monsterIndex = NPC.SKELETON;
                     if(Math.random() < 0.5)
                        monsterIndex = NPC.GHOST;
                     Coord monsterLoc = Utilities.findMonsterSpawn(dir, monsterIndex);
                     if(monsterLoc != null)
                     {
                        int monsterRow = monsterLoc.getRow();
                        int monsterCol = monsterLoc.getCol();
                        NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 1, "underworld");
                        worldMonsters.add(monster);
                     }
                  }
                  else
                  {
                     deaditeArmy = false;   
                  }
               }
               double multiplier = 1;
               byte dominantMonsterType = -1;    //if there is a monster frequency recorded for this location, this will be the dominant monster type in that location
               if(mapIndex >= 1 && mapIndex < allLocations.size())
               {
                  Location currentLoc = allLocations.get(mapIndex);
                  if(currentLoc.getMonsterTotal() > 0 && LocationBuilder.countMonsters(mapIndex) == 0)
                  {  //if we are in a location with a monster frequency recorded and the location has no monsters in it, increase the chance one will spawn
                     multiplier = 5;
                     dominantMonsterType = currentLoc.getDominantMonsterType();
                  }
                  else if(locType.contains("jurassica"))
                  {
                     multiplier = 1.5;
                  }
               }
               if(Utilities.rollDie(100) < MONSTER_SPAWN_PROB * multiplier && !locType.contains("pacrealm"))
               {                                                                 //no extra monster spawns in pacrealm
                  byte monsterIndex = NPC.randomWorldMonster();
                  if(dominantMonsterType != -1 && Math.random() < 0.75)
                     monsterIndex = dominantMonsterType;
                  else if(locType.contains("dungeon"))
                     monsterIndex = NPC.randomDungeonMonster();
                  else if(locType.contains("lair"))
                     monsterIndex = NPC.randomLairMonster();
                  else if(locType.contains("cave"))
                     monsterIndex = NPC.randomCaveMonster();
                  else if(locType.contains("sewer"))
                     monsterIndex = NPC.randomSewerMonster();
                  else if(locType.contains("mine"))
                     monsterIndex = NPC.randomMineMonster();
                  else if(locType.contains("temple"))
                  {
                     monsterIndex = NPC.randomTempleMonster();
                     double unicornProb = 0.15;
                     if(Display.isSpring())
                        unicornProb = 0.25;
                     if(rainbowAlpha > 0 && Math.random()<unicornProb && Utilities.flowersInView())    //chance it is a unicorn
                        monsterIndex = NPC.UNICORN;
                  }
                  else if(locType.contains("underworld"))
                     monsterIndex = NPC.randomUnderworldMonster();
                  else if(locType.contains("jurassica"))
                     monsterIndex = NPC.randomJurassicMonster();
                  else if(locType.contains("1942"))
                     monsterIndex = NPC.random1942Monster();
                  else if(locType.contains("wolfenstein"))
                     monsterIndex = NPC.randomWolfensteinMonster();
                  if(Utilities.rollDie(75 - (player.getLevel()/2)) <= 1)               //change monster will be a monster king
                     monsterIndex = NPC.getMonsterKing(monsterIndex);          
                  Coord monsterLoc = Utilities.findMonsterSpawn(dir, monsterIndex);
                                 
                  byte DEMON_SWINE_PROB = 20;            //the probability that a demon in a dungeon wants to play a game of swine with high stakes  
                                                 
                  if(locType.contains("temple") && Utilities.humanNPCInLocation(player.getMapIndex()))
                  {} //don't add monsters to this temple if it has people in it
                  else
                     if(monsterLoc != null)
                     {
                        int monsterRow = monsterLoc.getRow();
                        int monsterCol = monsterLoc.getCol();
                        if(monsterIndex==NPC.DRAGON || monsterIndex==NPC.DRAGONKING)
                           Sound.dragonCall();
                        if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                        {
                           NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, player.getMapIndex(), locType);
                           if(locType.contains("underworld") && monsterIndex==NPC.FIRE)
                           {  //make these fire NPCs fire elementals
                              monster.setNumInfo(Utilities.rollDie(1, 10));
                           }
                           //maybe make a dungeon demon one that wants to play a game of swine
                           if((locType.contains("dungeon") || locType.contains("underworld")) && monsterIndex==NPC.DEMON && Utilities.rollDie(100) < DEMON_SWINE_PROB && !thereIsDemonSwinePlayer)
                           {
                              monster.setIsSwinePlayer(true);
                              monster.setMoveType(NPC.STILL);
                              if(Math.random() < 0.25)
                                 monster.addItem(Item.getDemonsCube());
                              (CultimaPanel.civilians.get(player.getMapIndex())).add(monster);
                           }
                           else
                           {
                              if(locType.contains("jurassica"))
                                 monster.setGold(0);
                              worldMonsters.add(monster);
                              if(monster.getCharIndex() == NPC.DILOPHOSAUR)
                              {  //Dilophosaur will travel in packs of 2-4
                                 int packSize = Utilities.rollDie(1,3);
                                 if(packSize >= 1 && monsterRow-1 > 0)
                                 {
                                    worldMonsters.add(new NPCPlayer(monsterIndex, monsterRow-1, monsterCol, player.getMapIndex(), locType));
                                 }
                                 if(packSize >= 2 && monsterCol-1 > 0)
                                 {
                                    worldMonsters.add(new NPCPlayer(monsterIndex, monsterRow, monsterCol-1, player.getMapIndex(), locType));
                                 }
                                 if(packSize >= 2  && monsterRow-1 > 0 && monsterCol-1 > 0)
                                 {
                                    worldMonsters.add(new NPCPlayer(monsterIndex, monsterRow-1, monsterCol-1, player.getMapIndex(), locType));
                                 }
                              }
                              else if(monster.getCharIndex() == NPC.ALLOSAUR || monster.getCharIndex() == NPC.STEGOSAUR)
                              {  //allo and stego might show up in pairs
                                 if(Utilities.rollDie(0,1) == 1 && monsterRow-1 > 0)
                                 {
                                    worldMonsters.add(new NPCPlayer(monsterIndex, monsterRow-1, monsterCol, player.getMapIndex(), locType));
                                 }
                              }
                           }
                           Mission.checkSlayMission(monster);
                        }
                     }
               } 
               if(Utilities.rollDie(200) < WATER_SPAWN_PROB && !locType.contains("temple")  && !locType.contains("wolfenstein") && !locType.contains("1942") && worldMonsters.size() < (NUM_WORLD_MONSTERS + numWorldMonstersToExclude) && !Display.frozenWater())                   //random encounter water monster
               {
                  byte monsterIndex = NPC.randomIndoorWaterMonster();
                  if(locType.contains("jurassica"))
                     monsterIndex = NPC.randomJurassicWaterMonster();
                  else if(locType.contains("underworld"))
                     monsterIndex = NPC.WHIRLPOOL;
                  else if(locType.contains("sewer"))
                     monsterIndex = NPC.randomSewerWaterMonster();   
                  Coord monsterLoc = Utilities.findWaterSpawn(monsterIndex);
                  if(monsterLoc != null)
                  {
                     int monsterRow = monsterLoc.getRow();
                     int monsterCol = monsterLoc.getCol();
                     if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                     {
                        NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, player.getMapIndex(), locType);
                        if(locType.contains("jurassica"))
                           monster.setGold(0);
                        worldMonsters.add(monster);
                        Mission.checkSlayMission(monster);
                     }
                  }
               } 
               if(Utilities.rollDie(100) < MONSTER_SPAWN_PROB && worldMonsters.size() < (NUM_WORLD_MONSTERS + numWorldMonstersToExclude))       //random encounter lava monster
               {
                  byte monsterIndex = NPC.randomLavaMonster();
                  if(locType.contains("jurassica"))      //no magma monster in jurassica
                     monsterIndex = NPC.FIRE;
                  Coord monsterLoc = Utilities.findSpawnOfType("lava");
                  if(monsterLoc != null)
                  {
                     int monsterRow = monsterLoc.getRow();
                     int monsterCol = monsterLoc.getCol();
                     if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                     {
                        NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, player.getMapIndex(), locType);
                        worldMonsters.add(monster);
                        Mission.checkSlayMission(monster);
                     }
                  }
               }
               //extra lava monsters in the underworld
               if(locType.contains("underworld"))
               {
                  if(Utilities.rollDie(100) < MONSTER_SPAWN_PROB && worldMonsters.size() < (NUM_WORLD_MONSTERS + numWorldMonstersToExclude))       //random encounter lava monster
                  {
                     byte monsterIndex = NPC.MAGMA;
                     Coord monsterLoc = Utilities.findSpawnOfType("lava");
                     if(monsterLoc != null)
                     {
                        int monsterRow = monsterLoc.getRow();
                        int monsterCol = monsterLoc.getCol();
                        if(!Utilities.NPCAt(monsterRow, monsterCol, player.getMapIndex()))
                        {
                           NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, player.getMapIndex(), locType);
                           worldMonsters.add(monster);
                        }
                     }
                  }
               }
            }
            if((System.currentTimeMillis() > cheerTime + 5000) && (locType.contains("mine")) && Math.random() < 0.5 && mineCollapseTimer > 0)
            {
               mineCollapseTimer--;
               sendMessage("<"+mineCollapseTimer+"> "+NPC.getRandomFrom(Player.mineWarning));
               Sound.rumble();
               Utilities.makeDustClouds();
               cheerTime = System.currentTimeMillis();
               if(mineCollapseTimer == 0)
               {
                  Utilities.mineCollapse();
               }
            }
            if((System.currentTimeMillis() > cheerTime + 5000) && TerrainBuilder.habitablePlace(player.getLocationType()) &&  monsterDist > 0)
            {
               monsterDist--;
               if(monsterDist >= 1)
               {
                  if(awareHumanNPCInSight)
                  {
                     sendMessage("~"+NPC.getRandomFrom(NPC.kaijuWarning).replace("MONSTER_DIST", ""+monsterDist+" furlong"));
                  }
                  else
                  {
                     sendMessage(NPC.getRandomFrom(Player.kaijuWarning).replace("MONSTER_DIST", ""+monsterDist+" furlong"));
                  }
               }
               Sound.rumble(monsterDist);
               if(monsterDist < 2)
               {
                  Utilities.makeDustClouds();
               }
               cheerTime = System.currentTimeMillis();
               if(monsterDist == 0)
               {
                  byte monsterType = NPC.EARTHELEMENTAL;    //get monsterType from mission info, stored in the mission's targetRow and Col
                  for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
                  {
                     Mission m = CultimaPanel.missionStack.get(i);
                     if(m.getMissionType()==Mission.KAIJU && m.getTargetRow()>=0 && m.getTargetRow()<NPC.NUM_NPCS)
                     {
                        monsterType = (byte)(m.getTargetRow());
                     }     
                  }
                  Utilities.spawnKaiju(monsterType);
               }
            }
            if(!talkSel)
            {
               if((System.currentTimeMillis() > cheerTime + 5000) && (locType.contains("dungeon") || locType.contains("lair") || locType.contains("camp")) && Math.random() < 0.5)
               {  //if we are in a map with prisoners, have them cry out for help depending on how close we are
                  double distToPrisoner = Utilities.distToClosestPrisoner() + 1;
                  if(distToPrisoner != 0 && distToPrisoner < SCREEN_SIZE*2)
                  {
                     if(Utilities.rollDie(SCREEN_SIZE*2) < SCREEN_SIZE*2 - distToPrisoner)
                     {
                        if(locType.contains("camp"))
                        {
                           sendMessage("~"+NPC.getRandomFrom(NPC.helpTrappedWerewolf));
                        }
                        else
                        {
                           String [] distType = {"<Echo from afar>...", "<Heard in the distance>..", "<A sound from the dark>:", ""};
                           int distIndex = 0;
                           if(distToPrisoner < SCREEN_SIZE && distToPrisoner >= SCREEN_SIZE/2)
                           {
                              distIndex = 1;
                           }
                           else if(distToPrisoner < SCREEN_SIZE/2 && distToPrisoner >= SCREEN_SIZE/4)
                           {
                              distIndex = 2;
                           }
                           else if(distToPrisoner < SCREEN_SIZE/4)
                           {
                              distIndex = 3;
                           }
                           if(distIndex < 3)
                           {
                              sendMessage(distType[distIndex]);
                           }
                           sendMessage("~"+NPC.getRandomFrom(NPC.helpPrisoner));
                        }
                        cheerTime = System.currentTimeMillis();
                     }
                  }
               }
               else if((System.currentTimeMillis() > cheerTime + 5000) && awareHumanNPCInSight)
               {  //see if we are close to a downtrodden, and if close enough, have them call out for charity
                  //if we are close to a shopkeep, have them comment about their wares
                  Object [] beggerInfo = Utilities.distToClosestBeggerOrPoisoned();
                  double distToBegger = (Double)(beggerInfo[0]);
                  if(locType.contains("dungeon") || locType.contains("lair") || locType.contains("camp"))
                  {  //make sure that if a prisoner call doesn't activate because of the random event, it doesn't trigger a downtrodden response
                     distToBegger = -1;
                  }
                  int beggerRep = (Integer)beggerInfo[1];
                  double distToPoisoned = (Double)(beggerInfo[2]);
                  String npcName = (String)(beggerInfo[3]);
                  boolean hasMet = (Boolean)(beggerInfo[4]);
                  boolean isShopkeep = Utilities.isShopkeep(npcName);
                  if(distToPoisoned!=-1 && distToPoisoned < 3 && Math.random() < 0.5)
                  {
                     String poisonedCall = NPC.getRandomFrom(NPC.poisoned);
                     sendMessage("~"+poisonedCall);
                     cheerTime = System.currentTimeMillis();
                  }
                  else if(isShopkeep)
                  {
                     if(!isNight)
                     {
                        String playerName = CultimaPanel.player.getShortName();
                        if(!hasMet)
                        {
                           playerName = Utilities.getRandomFrom(NPC.customerName);
                        }
                        if(distToBegger < 4 && Math.random() < 0.25)
                        {
                           String shopMessage = "";
                           if(beggerRep < 0)
                           {
                              if(Utilities.isRationsShopkeep(npcName))
                              {
                                 shopMessage = Utilities.getRandomFrom(NPC.rationsNegative).replace("PLAYER_NAME",playerName);
                              }
                              else if(Utilities.isMagicShopkeep(npcName))
                              {
                                 shopMessage = Utilities.getRandomFrom(NPC.magicNegative).replace("PLAYER_NAME",playerName);
                              }
                              else if(Utilities.isArmsShopkeep(npcName))
                              {
                                 shopMessage = Utilities.getRandomFrom(NPC.armsNegative).replace("PLAYER_NAME",playerName);
                              }
                              else if(Utilities.isTavernShopkeep(npcName))
                              {
                                 shopMessage = Utilities.getRandomFrom(NPC.tavernNegative).replace("PLAYER_NAME",playerName);
                              }
                              else
                              {
                                 shopMessage = "If thee break, thee buys!";
                              }
                           }
                           else
                           {
                              if(Utilities.isRationsShopkeep(npcName))
                              {
                                 if(holiday)
                                 {
                                    shopMessage = Utilities.getRandomFrom(NPC.rationsHoliday).replace("PLAYER_NAME",playerName);
                                 }
                                 else
                                 {
                                    shopMessage = Utilities.getRandomFrom(NPC.rationsPositive).replace("PLAYER_NAME",playerName);
                                 }
                              }
                              else if(Utilities.isMagicShopkeep(npcName))
                              {
                                 if(holiday)
                                 {
                                    shopMessage = Utilities.getRandomFrom(NPC.holidaySale).replace("PLAYER_NAME",playerName);
                                 }
                                 else
                                 {
                                    shopMessage = Utilities.getRandomFrom(NPC.magicPositive).replace("PLAYER_NAME",playerName).replace("DAYS_LEFT",""+daysLeft);
                                 }
                              }
                              else if(Utilities.isArmsShopkeep(npcName))
                              {
                                 if(holiday)
                                 {
                                    shopMessage = Utilities.getRandomFrom(NPC.holidaySale).replace("PLAYER_NAME",playerName);
                                 }
                                 else
                                 {
                                    shopMessage = Utilities.getRandomFrom(NPC.armsPositive).replace("PLAYER_NAME",playerName).replace("DAYS_LEFT",""+daysLeft);
                                 }
                              }
                              else if(Utilities.isTavernShopkeep(npcName))
                              {
                                 if(holiday)
                                 {
                                    shopMessage = Utilities.getRandomFrom(NPC.tavernHoliday).replace("PLAYER_NAME",playerName);
                                 }
                                 else if((player.getImageIndex()==Player.AXE || player.getImageIndex()==Player.DUALAXE) && Sound.whichSong == -1 && Math.random() < 0.5)
                                 {
                                    CultimaPanel.sendMessage("-----");
                                    Sound.pickRandomLumberjackSong();
                                    if(Sound.whichSong == 6 || Sound.whichSong == 7)
                                    {
                                       sendMessage("~<singing>"+Utilities.getRandomFrom(NPC.lumberjackSong1));
                                    }
                                    else if(Sound.whichSong == 8 || Sound.whichSong == 9)
                                    {
                                       sendMessage("~<singing>"+Utilities.getRandomFrom(NPC.lumberjackSong2));
                                    }
                                    else if(Sound.whichSong == 10 || Sound.whichSong == 11)
                                    {
                                       sendMessage("~<singing>"+Utilities.getRandomFrom(NPC.lumberjackSong3));
                                    }
                                 }
                                 else
                                 {
                                    shopMessage = Utilities.getRandomFrom(NPC.tavernPositive).replace("PLAYER_NAME",playerName);
                                 }
                              }
                              else
                              {
                                 shopMessage = "How can I help thee?";
                              }
                           }
                           sendMessage("-----");
                           sendMessage("~"+shopMessage);
                           cheerTime = System.currentTimeMillis();
                        }
                     }
                  }
                  else if(distToBegger!=-1 && (distToBegger < 3 || (npcName.toLowerCase().contains("cultist") && distToBegger < SCREEN_SIZE)) && Math.random() < 0.5)
                  {
                     String begCall = NPC.getRandomFrom(NPC.beggerCallPositiveRep);
                     if(holiday)
                     {
                        begCall = NPC.getRandomFrom(NPC.beggerCallHoliday);
                     }
                     if(beggerRep < 0)
                     {
                        begCall = NPC.getRandomFrom(NPC.beggerCallNegativeRep);
                     }
                     if(npcName.toLowerCase().contains("cultist"))
                     {
                        String chantCount = "<"+(5-numChants)+"> ";
                        if(numChants == -1)
                        {
                           chantCount = "";
                        }
                        if(days % 3 == 0)
                        {
                           begCall = chantCount + NPC.getRandomFrom(NPC.cultistChant3);
                        }
                        else if(days % 2 == 0)
                        {
                           begCall = chantCount + NPC.getRandomFrom(NPC.cultistChant2);
                        }
                        else
                        {
                           begCall = chantCount + NPC.getRandomFrom(NPC.cultistChant1);
                        }
                        if(numChants != -1)
                        {
                           CultimaPanel.flashColor = Color.red;
                           CultimaPanel.flashFrame = CultimaPanel.numFrames;
                           CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
                           numChants++;
                           Sound.thunder();
                           if(numChants >= 5)
                           {
                              for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
                              {
                                 Mission m = CultimaPanel.missionStack.get(i);
                                 Location missLoc = null;
                                 Item missTarget = null;
                                 if(m.getType().contains("Ceremony"))
                                 {
                                    missLoc = CultimaPanel.getLocation(m.getClearLocation());
                                    missTarget = m.getTarget();
                                    boolean breakOuter = false;
                                    for(int j=CultimaPanel.civilians.get(mapIndex).size()-1; j>=0; j--)
                                    {
                                       NPCPlayer statue = CultimaPanel.civilians.get(mapIndex).get(j);
                                       int statueMapIndex = statue.getMapIndex();
                                       if(statue.isStatue() && statue.hasItem(missTarget.getName()) && statueMapIndex == missLoc.getToMapIndex())
                                       {
                                          Utilities.purgeCultists(mapIndex);
                                          CultimaPanel.sendMessage("Thy statue springs to life! Ceremony mission failed!");
                                          NPCPlayer resurectedGod = new NPCPlayer(statue.getCharIndex(), statue.getRow(), statue.getCol(), statue.getMapIndex(), statue.getLocationType());
                                          if(!resurectedGod.canFly() && !resurectedGod.canSwim())
                                          {
                                             resurectedGod.setCanWalkAndSwim(true);
                                          }
                                          if(resurectedGod.hasItem("head"))
                                          {
                                             resurectedGod.removeItem("head");
                                          }
                                          CultimaPanel.worldMonsters.add(resurectedGod);
                                          Utilities.removeStatueAt(statue.getRow(), statue.getCol(), statue.getMapIndex());
                                          Utilities.removeMission(i);
                                          numChants = -1;
                                          breakOuter = true;
                                          break;
                                       }
                                    }
                                    if(breakOuter)
                                    {
                                       break;
                                    }
                                 }
                              }
                           }
                        }
                     }
                     sendMessage("~"+begCall);
                     cheerTime = System.currentTimeMillis();
                  }
                  else if((time < 20 && time > 6) && (locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("port")))
                  {
                     if(player.getReputation() <= -500)
                     {  //cheer or scream depending on reputation and NPCinSight
                        if(Utilities.rollDie(20000) < Math.abs(Math.min(1000,player.getReputation())))
                        {
                           if(player.hasItem("blessed-crown"))
                           {
                              if(Math.random() < 0.5)
                              {
                                 String crown = NPC.getRandomFrom(NPC.blessedCrown);
                                 sendMessage("~"+crown.replace("PLAYER_NAME", player.getShortName()));
                              }
                              else
                              {
                                 String scream = "";
                                 if(player.getReputation() <= -1000)
                                    scream = NPC.getRandomFrom(NPC.scream);
                                 else
                                    scream = NPC.getRandomFrom(NPC.screamLesser).replace("ADJECTIVE",NPC.getRandomFrom(NPC.insultAdjective)).replace("NOUN",NPC.getRandomFrom(NPC.insultNoun));
                                 sendMessage("~"+scream.replace("PLAYER_NAME", player.getShortName()));
                              }
                           }
                           else
                           {
                              String scream = "";
                              if(player.getReputation() <= -1000)
                                 scream = NPC.getRandomFrom(NPC.scream);
                              else
                                 scream = NPC.getRandomFrom(NPC.screamLesser).replace("ADJECTIVE",NPC.getRandomFrom(NPC.insultAdjective)).replace("NOUN",NPC.getRandomFrom(NPC.insultNoun));
                              sendMessage("~"+scream.replace("PLAYER_NAME", player.getShortName()));
                           }
                           cheerTime = System.currentTimeMillis();
                        }
                     }
                     else if(System.currentTimeMillis() > cheerTime + 10000 && player.getReputation() >= 500 && Utilities.rollDie(20000) < Math.min(1000,player.getReputation()))
                     {  
                        if(ourBeds == null)
                        {  //hero reputation in a city that we do not have a house in
                           if(player.hasItem("blessed-crown"))
                           {
                              if(Math.random() < 0.5)
                              {
                                 String crown = NPC.getRandomFrom(NPC.blessedCrown);
                                 sendMessage("~"+crown.replace("PLAYER_NAME", player.getShortName()));
                              }
                              else
                              {
                                 String cheer = "";
                                 if(player.getReputation() < 1000)
                                    cheer = NPC.getRandomFrom(NPC.cheerLesser);
                                 else
                                 {
                                    cheer = (NPC.getRandomFrom(NPC.cheer)).replace("ADJECTIVE",NPC.getRandomFrom(NPC.complimentAdjective));
                                 }
                                 sendMessage("~"+cheer.replace("PLAYER_NAME", player.getShortName()));
                              }
                           }
                           else
                           {
                              boolean hasLegendaryWeapon = false;
                              String weaponName = "";
                              if(player.getWeapon()!=null)
                              {
                                 weaponName = player.getWeapon().getName();
                                 hasLegendaryWeapon = Weapon.isLegendaryWeapon(weaponName);
                              }
                              if(hasLegendaryWeapon && Math.random() < 0.25)
                              {
                                 String cheer = NPC.getRandomFrom(NPC.legendaryWeapon);
                                 sendMessage("~"+cheer.replace("PLAYER_NAME", player.getShortName()).replace("WEAP_NAME", weaponName));
                              }
                              else
                              {
                                 String cheer = "";
                                 if(player.getReputation() < 1000)
                                    cheer = NPC.getRandomFrom(NPC.cheerLesser);
                                 else
                                    cheer = (NPC.getRandomFrom(NPC.cheer)).replace("ADJECTIVE",NPC.getRandomFrom(NPC.complimentAdjective));
                                 sendMessage("~"+cheer.replace("PLAYER_NAME", player.getShortName()));
                              }
                           }
                           cheerTime = System.currentTimeMillis();
                        }
                        else //ourBeds != null
                        {    //we are in a location where we own a house
                           String greeting = "";
                           if(player.getReputation() < 0 && player.getReputation() > -500 && Utilities.rollDie(20000) < Math.abs(Math.min(1000,player.getReputation())))
                           {
                              greeting = NPC.getRandomFrom(NPC.neighborSuspicious);
                           }
                           else if(player.getReputation() >= 0 && Math.random() < (Math.max(1, Math.min(500,player.getReputation())))/750.0)
                           {
                              double bountyWarning = (player.getBounty() / 200.0);
                              if(Math.random() < bountyWarning)
                              {
                                 greeting = NPC.getRandomFrom(NPC.bountyWarning).replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective)).replace("NOUN", NPC.getRandomFrom(NPC.insultNoun)).replace("PLAYER_NAME",player.getShortName());
                              }
                              else
                              {
                                 String adjective = NPC.getRandomFrom(NPC.complimentAdjective);
                                 String response = NPC.getRandomFrom(NPC.neighborGreeting).replace("PLAYER_NAME",player.getShortName());
                                 if(holiday)
                                 {
                                    response = NPC.getRandomFrom(NPC.neighborGreetingHoliday).replace("PLAYER_NAME",player.getShortName());
                                 }
                                 else if(weather > 0)
                                 {
                                    response = NPC.getRandomFrom(NPC.neighborGreetingBadWeather).replace("PLAYER_NAME",player.getShortName());
                                    if(response.toLowerCase().contains("weather"))
                                    {
                                       adjective = NPC.getRandomFrom(NPC.insultAdjective);
                                    }
                                 }
                                 greeting = response.replace("ADJECTIVE",adjective );
                              }
                           }
                           if(greeting.length() > 0)
                           {
                              CultimaPanel.sendMessage("-----");
                              sendMessage("~"+greeting.replace("PLAYER_NAME", player.getShortName()));
                              cheerTime = System.currentTimeMillis();
                           }
                        }
                     }
                  }
               }
               if((locType.contains("city") || locType.contains("village") || locType.contains("fortress") || locType.contains("port")))
               {
                  if(Utilities.nextToABountyPosting(currMap, player.getRow(), player.getCol())  && (System.currentTimeMillis() > cheerTime + 3000) )
                  {
                     CultimaPanel.sendMessage("-----");
                     sendMessage(postedBounty);
                     CultimaPanel.sendMessage("-----");
                     cheerTime = System.currentTimeMillis();
                  }
                  else if(Utilities.nextToAHauntedHouse(currMap, player.getRow(), player.getCol(), 3) && (System.currentTimeMillis() > cheerTime + 5000) )
                  {
                     CultimaPanel.sendMessage("-----");
                     sendMessage(NPC.getRandomFrom(NPC.hauntedHouse));
                     CultimaPanel.sendMessage("-----");
                     cheerTime = System.currentTimeMillis();
                  }
               } 
            }
            if(NPCinSight.size()==0 && (locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("castle") || locType.contains("tower") || locType.contains("domicile") || locType.contains("port")))
            {//if we are walking around in a habitable location and it is past time to get NPCs in or out of bed and it hasn't been done yet, take care of it
               if(time >= 6 && time <= 20)
               {
                  if(locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("domicile") || locType.contains("port"))
                     Utilities.putNPCsOnStreet(player.getMapIndex());
                  else
                     Utilities.makeNPCsRoam(player.getMapIndex());
                  if(player.getReputation() < -100)
                     Utilities.makeNPCsRun(player.getMapIndex());
               }
               else
                  Utilities.putNPCsToBed(player.getMapIndex());
            }
            else if(locType.contains("pacrealm"))
            {  //see if we finish the level in pacrealm by eating all pellets which should open the gates to get out
               LocationBuilder.checkPelletCount();
            }
         }
      //walk over a corpse - take gold and items
         if(!isWolf)
         {
            for(int i = corpses.get(mapIndex).size()-1; i>=0; i--)
            {
               Corpse cp = corpses.get(mapIndex).get(i);
               if(cp.getRow()==player.getRow() && cp.getCol()==player.getCol() && (cp.hasItems() || cp.getNumArrows() > 0 || cp.getCharIndex()==NPC.JAWTRAP))
               {
                  if(cp.getCharIndex()==NPC.JAWTRAP)    //recover trap
                  {
                     player.addWeapon(Weapon.getJawTrap());
                     sendMessage("Recovered Iron-jawtrap.");
                     corpses.get(mapIndex).remove(i);
                     continue;
                  }
               //player gets gold and items  
                  if(cp.getGold() > 0)
                  {
                     player.addGold(cp.getGold());
                     if(cp.getGold() == 1)
                        sendMessage("You found a coin.");
                     else
                        sendMessage("You found "+cp.getGold()+" coins!");
                  }
                  int numArrows = cp.getNumArrows();
                  if(numArrows > 0)
                  {
                     int maxArrows = player.getMaxArrowCapacity(); 
                     if(player.getNumArrows() + numArrows > maxArrows)
                        numArrows = maxArrows - player.getNumArrows();
                     player.setNumArrows(player.getNumArrows() + numArrows);
                     if(numArrows == 1)
                        sendMessage("You recovered an arrow.");
                     else if(numArrows > 1)
                        sendMessage("You recovered "+numArrows+" arrows.");
                     Sound.addRations();   
                  }
                  if(cp.getItems()!=null && cp.getItems().size() > 0)
                  {
                     for(Item it: cp.getItems())
                     {
                        if(it.getName().contains("meat"))
                        { 
                           if(!player.isVampire())
                           {
                              byte numRations = (byte)(it.getValue());
                              player.addRations(numRations);
                              if(cp.getCharIndex()==NPC.BARREL)
                              {
                                 if(numRations == 1)
                                    sendMessage("You found rations for "+numRations+" day.");
                                 else
                                    sendMessage("You found rations for "+numRations+" days.");
                              }
                              else
                                 sendMessage("You cleaved meat for "+numRations+" day's rations.");
                              if(locType.contains("jurassica"))
                                 Achievement.earnAchievement(Achievement.THE_LUNCH_THAT_TIME_FORGOT);
                           }
                        }
                        else
                        {
                           if(it instanceof Potion)
                           {
                              player.addPotion(Potion.getPotionWithName(it.getName()));
                              if(Utilities.startsWithVowel(it.getName()))
                                 sendMessage("You discovered an "+it.getName()+" potion!");
                              else
                                 sendMessage("You discovered a "+it.getName()+" potion!");
                           }
                           else if(it.isBook())
                           {
                              boolean success = Journal.copyFromBook(it);
                              if(success)
                                 sendMessage("Found " + it.getName() + " and copied items into your journal");
                              else
                                 sendMessage("Found " + it.getName());
                              player.addItem(it.getName());   
                           }
                           else if(it.getName().toLowerCase().contains("-head"))
                           {
                              boolean success = player.addItem(it.getName());
                              if(success)
                              {
                                 int headIndex = it.getName().indexOf("-head");
                                 String headName = it.getName().substring(0,headIndex);
                                 sendMessage("You liberate the " + headName + " head and place it in thy satchel");
                              }
                           }
                           else
                           {
                              boolean success = player.addItem(it.getName());
                              if(success)
                              {
                                 if(it.getName().endsWith("s"))
                                    sendMessage("You discovered some "+it.getName()+"!");
                                 else if(Utilities.startsWithVowel(it.getName()))
                                    sendMessage("You discovered an "+it.getName()+"!");
                                 else
                                    sendMessage("You discovered a "+it.getName()+"!");
                              }
                              else  //no room to carry the item
                              {
                                 if(it.getName().endsWith("s"))
                                    sendMessage("You have not the room to carry some "+it.getName()+"!");
                                 else if(Utilities.startsWithVowel(it.getName()))
                                    sendMessage("You have not the room to carry an "+it.getName()+"!");
                                 else
                                    sendMessage("You have not the room to carry a "+it.getName()+"!");
                              }
                           }
                        }
                     }
                  }
                  cp.clearItems();
                  if(cp.getCharIndex()==-1)
                  {  //this is a dropped spear or dagger, so remove the corpse once we pick it up
                     (CultimaPanel.corpses.get(mapIndex)).remove(i);
                  }
                  break;
               }
            }
         }
         double mapProb = 0.1;            //the probability that we find a map or map portion in a chest
      
         value = currMap[player.getRow()][player.getCol()];
         currentPos = allTerrain.get(Math.abs(value));
         int currentLocIndex = getLocationIndex(allLocations, player.getRow(), player.getCol(), mapIndex);
         Location currentLoc = null;
         if(currentLocIndex >= 1)
            currentLoc = allLocations.get(currentLocIndex);
         double oldTime = time;           //used to see if the weather should change by the hour
         
         boolean closeToPuzzleDoor = false;
         if(locType.contains("dungeon"))
         {
            Location here = allLocations.get(player.getMapIndex());
            if(here.getRiddlePoints().size()==3)
            {
               door1 = here.getRiddlePoints().get(0);
               door2 = here.getRiddlePoints().get(1);
               door3 = here.getRiddlePoints().get(2);
               double d1dist = Display.distanceSimple(door1.getRow(), door1.getCol(), player.getRow(),player.getCol());
               double d2dist = Display.distanceSimple(door2.getRow(), door2.getCol(), player.getRow(),player.getCol());
               double d3dist = Display.distanceSimple(door3.getRow(), door3.getCol(), player.getRow(),player.getCol());
               double maxDist = Math.max(Math.max(d1dist,d2dist),d3dist);
               if(maxDist<7.5)
                  closeToPuzzleDoor = true;
            }
            else
            {
               door1 = null;
               door2 = null;
               door3 = null;
               tower1 = null;
               tower2 = null;
               tower3 = null;
            }
         }
         else if(locType.contains("cave"))
         {
            Location here = allLocations.get(player.getMapIndex());
            if(here.getRiddlePoints().size()==3)
            {
               door1 = here.getRiddlePoints().get(0);    //these are the locations of the towers
               door2 = here.getRiddlePoints().get(1);
               door3 = here.getRiddlePoints().get(2);
            }
            else
            {
               door1 = null;
               door2 = null;
               door3 = null;
               tower1 = null;
               tower2 = null;
               tower3 = null;
            }
         }
         else
         {
            door1 = null;
            door2 = null;
            door3 = null;
            tower1 = null;
            tower2 = null;
            tower3 = null;
         }
            
         ArrayList<Coord> xMarks = player.getTreasureMapLocs();                        
         boolean onTreasure = false;
         if(xMarks.size() > 0 && locType.contains("world"))
         {                                //see if we found a treasure on a treasuremap
            for(Coord ex:xMarks)
            {
               int exR = ex.getRow();
               int exC = ex.getCol();
               if(player.getRow()==exR && player.getCol()==exC)
                  onTreasure = true;
            }
         }
         //see if we are standing on any eggs to collect
         RestoreItem egg = Utilities.getEggAt(player.getRow(), player.getCol(), player.getMapIndex());
         if(egg != null && egg.getValue() >= 0) //negative egg values means that it is in the queue to be restored
         {
            byte eggType = (byte)(Math.abs(egg.getValue()));
            byte numRations = (byte)(Utilities.rollDie(1,2));
            if(eggType == NPC.CHICKEN)
            {
               if(((!CultimaPanel.player.hasItem("holdall") && CultimaPanel.player.getRations() < 15) || (CultimaPanel.player.hasItem("holdall") && CultimaPanel.player.getRations() < 100)) && !CultimaPanel.player.isVampire())
               {
                  sendMessage("Collected bird eggs for "+numRations+" day's rations");
                  player.addRations(numRations);
                  Utilities.removeEggAt(player.getRow(), player.getCol(), player.getMapIndex());
                  Sound.addRations();   
               }
            }
            else if(eggType == NPC.SNAKE)
            {
               if(player.canPickupEgg() && !CultimaPanel.player.isVampire())
               {
                  int numEggs = Utilities.rollDie(2,3);
                  sendMessage("Harvested "+numEggs+" serpent eggs");
                  for(int i=0; i<numEggs; i++)
                     player.addItem("serpent-egg");
                  Utilities.removeEggAt(player.getRow(), player.getCol(), player.getMapIndex());
                  Sound.addRations();     
               }     
               else
                  sendMessage("You have not the room to safely carry this egg!");
            }
            else if(eggType == NPC.DRAGON)
            {
               if(!player.hasItem("dragon-egg"))
               {
                  sendMessage("Stole a dragon's egg!");
                  player.addItem("dragon-egg");
                  Utilities.removeEggAt(player.getRow(), player.getCol(), player.getMapIndex());
                  Sound.addRations();
               }     
               else
                  sendMessage("You have not the room to safely carry this egg!");
            }
            else
            {
               if(((!CultimaPanel.player.hasItem("holdall") && CultimaPanel.player.getRations() < 15) || (CultimaPanel.player.hasItem("holdall") && CultimaPanel.player.getRations() < 100)) && !CultimaPanel.player.isVampire())
               {
                  sendMessage("Collected eggs for "+numRations+" day's rations");
                  player.addRations(numRations);
                  Utilities.removeEggAt(player.getRow(), player.getCol(), player.getMapIndex());
                  Sound.addRations();
               }
            }
         }
      
         if(onTreasure && !flight && !isWolf)
         {
            //remove that treasure map from our items
            player.removeTreasureMap();
            player.addExperience(50);
            Utilities.getBigTreasure();
            Achievement.earnAchievement(Achievement.GOLD_DIGGER);
         }
                                 
         if(currentPos.getName().contains("_V_") || currentPos.getName().contains("_D_"))      //Very slow, or destroyable
         {
            if(!timestop && dir.length()>0) //dir only has a length when we move
            {
               Utilities.checkRainbow();
               if(flight)
                  time += (increment/2);
               else if(player.onHorse() || isWolf)
                  time += increment*2;
               else if(sailing)
               {
                  if(player.getImageIndex()==Player.BOAT)
                  {
                     time += (increment*3);
                     sendMessage("Slow rowing.");
                  }
                  else if(Utilities.sameDir(Utilities.byteDirToString(windDir), dir))       //sailing same direction as the wind
                     time += increment/2;
                  else if(Utilities.sideDir(Utilities.byteDirToString(windDir), dir))  //sailing perpindicular direction to the wind
                     time += increment;
                  else                                                                 //sailing opposite direction of the wind    
                     time += increment*3;  
               }   
               else
                  time += (increment*4);
               Utilities.checkWerewolfStatus();  
            }
            if(currentPos.getName().contains("_T_") && !flight)
            {
               Utilities.setOffTrap();
               if((currentPos.getName().toLowerCase().contains("chest") || currentPos.getName().toLowerCase().contains("book")) && !isWolf)
               {
                  byte chestValue = (byte)(Math.abs(value));
                  int restoreDay = Utilities.getChestRestoreDay(player.getLocationType());
                  tilesToRestore.add(new RestoreItem(player.getMapIndex(), player.getRow(), player.getCol(), chestValue, restoreDay));
               
                  if(Math.random() < mapProb && !locType.contains("pacrealm"))        //find a map or map segment
                  {                                                     //we only want gold in pacrealm         
                     if(Math.random() < 0.5 || player.getMapIndex()==0) //find part of the world map
                     {
                        TerrainBuilder.markMapArea();
                        sendMessage("Piece of world map found!");
                     }
                     else                                               //find part of a dungeon map
                     {
                        if(Math.random() < 0.25)                        //find map to entire dungeon
                        {
                           TerrainBuilder.markMapArea(player.getMapIndex());
                           sendMessage("Map found!");
                        }
                        else                                            //find a portion of the dungeon map
                        {
                           sendMessage("Map portion found!");
                           int quadrant = (int)(Math.random()*4)+1;
                           int numRows = (map.get(player.getMapIndex()).length)/2;
                           int numCols = (map.get(player.getMapIndex())[0].length)/2;
                           if(quadrant==1)
                           {
                              TerrainBuilder.markMapArea(player.getMapIndex(), 0, numCols, numRows, numCols);
                           }
                           else if(quadrant==2)
                           {
                              TerrainBuilder.markMapArea(player.getMapIndex(), 0, 0, numRows, numCols);
                           }
                           else if(quadrant==3)
                           {
                              TerrainBuilder.markMapArea(player.getMapIndex(), numRows, 0, numRows, numCols);
                           }
                           else //if(quadrant==4)
                           {
                              TerrainBuilder.markMapArea(player.getMapIndex(), numRows, numCols, numRows, numCols);
                           }
                        }
                     }
                  }
                  else
                  {
                     double goldProb = 0.98;
                     int goldMax = 10;
                     if(locType.contains("wolfenstein") && !CultimaPanel.player.hasWeapon("Grenade-of-Antioch") && Math.random() < 0.5)
                     {
                        CultimaPanel.player.addWeapon(Weapon.getHolyHandGrenade());
                        CultimaPanel.sendMessage("You found the Holy Hand Grenade of Antioch!");
                     }
                     else if(locType.contains("castle") || locType.contains("tower"))        //slightly higher chance of finding jems in a castle chest
                     {
                        goldProb = 0.95;
                        goldMax = 30;
                     }
                     if(Math.random() < goldProb)
                     {
                        int gold = (int)(Math.random() * 20) + 1;
                        player.addGold(gold);
                        sendMessage("Found " + gold + " gold!");
                     }
                     else
                     {
                        Item stone = Item.getRandomStone();
                        player.addItem(stone.getName());
                        sendMessage("Found a sparkling "+stone.getName()+" stone!");
                     }
                     if(Utilities.caughtStealingChest())
                     {
                        if(TerrainBuilder.habitablePlace(player.getLocationType()))
                        {
                           CultimaPanel.player.addReputation(-1);
                           Player.stats[Player.ITEMS_STOLEN]++;
                        }
                        sendMessage("~"+ NPC.getRandomFrom(NPC.thiefGeneral));
                        Utilities.alertGuards();
                     }
                  }
               }
               if((currentPos.getName().toLowerCase().contains("chest") || currentPos.getName().toLowerCase().contains("book")) && isWolf)
               {}
               else
                  currMap[player.getRow()][player.getCol()] = currMap[lastR][lastC];
            }
            else
               if(currentPos.getName().contains("_D_") && !flight)
               {
                  if(currentPos.getName().toLowerCase().contains("gold") && !isWolf)
                  { 
                     double goldProb = 0.98;
                     int goldMax = 10;
                     if(locType.contains("castle") || locType.contains("tower"))        //slightly higher chance of finding jems in a castle chest
                     {
                        goldProb = 0.95;
                        goldMax = 30;
                     }
                     if(Math.random() < goldProb)
                     {
                        int gold = (int)(Math.random() * goldMax) + 1;
                        player.addGold(gold);
                        Player.stats[Player.GOLD_MINED]+=gold;
                        sendMessage("Mined " + gold + " gold!");
                     }
                     else
                     {
                        Item stone = Item.getRandomStone();
                        player.addItem(stone.getName());
                        sendMessage("Mined a sparkling "+stone.getName()+" stone!");
                        Achievement.earnAchievement(Achievement.MINER_2049ER);
                     }
                  }
                  else if(currentPos.getName().toLowerCase().contains("door"))
                  { 
                     String door = currentPos.getDescription().replace("_"," ");
                     sendMessage(door + " opened.");
                     byte doorValue = currMap[player.getRow()][player.getCol()];
                     if(doorValue > 0)       //if we opened this door, we visited it (negative values denote a visited location for the map)
                        doorValue *= -1;
                     Sound.openDoor();
                     if(TerrainBuilder.habitablePlace(player.getLocationType()))
                     {
                        doorsToClose.push(new RestoreItem(player.getMapIndex(), player.getRow(), player.getCol(), doorValue));
                     }
                     else //not in habitable place - we might be opening a cell door with a prisoner.  If so, get them on their feet   
                     {
                        NPCPlayer selected = null;
                        NPCPlayer[] adjNPCs = Utilities.getAdjacentNPCs(player.getRow(), player.getCol(), player.getMapIndex());
                        for(NPCPlayer p:adjNPCs)
                        {
                           if(p != null)
                           {
                              if(p.getCharIndex() == NPC.BEGGER && ! NPC.isStone(p))
                              {
                                 selected = p;
                                 String response = NPC.beggerRescue(selected);
                                 sendMessage("~"+response);
                              }
                           }
                        }
                     }
                  }
                  else if((currentPos.getName().toLowerCase().contains("chest") || currentPos.getName().toLowerCase().contains("book")) && !isWolf)
                  {
                  //change restore day depending on locType
                     boolean isBook = (currentPos.getName().toLowerCase().contains("book"));
                     locType = player.getLocationType().toLowerCase();
                     if(isBook && Utilities.nextToAHauntedHouse(currMap,  player.getRow(), player.getCol(), 1))
                     {  //for HAUNTED_FIND mission - find the songbook that the jester stole from the lute player and threw in the haunted house
                        Item thisBook = Item.getItemWithName("songbook");
                        //see which mission we have - FIND_LEDGER2 should be "tax-ledger" as the name of the book
                        for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
                        { 
                           Mission m = CultimaPanel.missionStack.get(i);
                           if(m.getMissionType()==Mission.FIND_LEDGER2)
                           {
                              thisBook.setName("tax-ledger");
                              break;
                           }
                        }
                        player.addItem(thisBook.getName());
                        sendMessage("Found " + thisBook.getName());
                     }
                     else if(locType.contains("underworld") && isBook)
                     {
                        Item thisBook = Item.get3WellsBook();
                        boolean success = Journal.copyFromBook(thisBook);
                        if(success)
                           sendMessage("Found " + thisBook.getName() + " and copied items into your journal");
                        else
                           sendMessage("Found " + thisBook.getName());
                     }
                     else if(locType.contains("dungeon") && isBook && door1!=null && door2!=null && door3!=null)
                     {
                        Item thisBook = Item.get3DoorsBook();
                        boolean success = Journal.copyFromBook(thisBook);
                        if(success)
                           sendMessage("Found " + thisBook.getName() + " and copied items into your journal");
                        else
                           sendMessage("Found " + thisBook.getName());
                     }
                     else if(locType.contains("cave") && isBook && door1!=null && door2!=null && door3!=null && tower1!=null && tower2!=null && tower3!=null)
                     {
                        Item thisBook = Item.get3TowersBook();
                        boolean success = Journal.copyFromBook(thisBook);
                        if(success)
                           sendMessage("Found " + thisBook.getName() + " and copied items into your journal");
                        else
                           sendMessage("Found " + thisBook.getName());
                     }
                     else if(locType.contains("dungeon") && closeToPuzzleDoor && door1!=null && door2!=null && door3!=null)
                     {  //we opened the right door
                        Utilities.getBigTreasure();
                        if(CultimaPanel.player.getMapIndex() < CultimaPanel.allLocations.size())
                           CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex()).clearRiddlePoints();
                     }
                     else if(Math.random() < mapProb && (locType.contains("sewer") || locType.contains("dungeon") || locType.contains("lair") 
                     || locType.contains("cave") || locType.contains("mine") || locType.contains("world")))        //find a map or map segment
                     {
                        if(Math.random() < 0.5 || player.getMapIndex()==0) //find part of the world map
                        {
                           TerrainBuilder.markMapArea();
                           sendMessage("Piece of world map found!");
                        }
                        else                                               //find part of a dungeon map
                        {
                           if(Math.random() < 0.25)                        //find map to entire dungeon
                           {
                              TerrainBuilder.markMapArea(player.getMapIndex());
                              sendMessage("Map found!");
                           }
                           else                                            //find a portion of the dungeon map
                           {
                              sendMessage("Map portion found!");
                              int quadrant = (int)(Math.random()*4)+1;
                              int numRows = (map.get(player.getMapIndex()).length)/2;
                              int numCols = (map.get(player.getMapIndex())[0].length)/2;
                              if(quadrant==1)
                              {
                                 TerrainBuilder.markMapArea(player.getMapIndex(), 0, numCols, numRows, numCols);
                              }
                              else if(quadrant==2)
                              {
                                 TerrainBuilder.markMapArea(player.getMapIndex(), 0, 0, numRows, numCols);
                              }
                              else if(quadrant==3)
                              {
                                 TerrainBuilder.markMapArea(player.getMapIndex(), numRows, 0, numRows, numCols);
                              }
                              else //if(quadrant==4)
                              {
                                 TerrainBuilder.markMapArea(player.getMapIndex(), numRows, numCols, numRows, numCols);
                              }
                           }
                        }
                     }
                     else
                     {
                        if(isBook)
                        {
                           Item thisBook = Item.getRandomBook();
                           boolean success = Journal.copyFromBook(thisBook);
                           if(success)
                              sendMessage("Found " + thisBook.getName() + " and copied items into your journal");
                           else
                              sendMessage("Found " + thisBook.getName());
                        }
                        else
                        {
                           double goldProb = 0.98;
                           int goldMax = 10;
                           if(locType.contains("wolfenstein") && !CultimaPanel.player.hasWeapon("Grenade-of-Antioch") && Math.random() < 0.5)
                           {
                              CultimaPanel.player.addWeapon(Weapon.getHolyHandGrenade());
                              CultimaPanel.sendMessage("You found the Holy Hand Grenade of Antioch!");
                           }
                           else if(locType.contains("castle") || locType.contains("tower"))        //slightly higher chance of finding jems in a castle chest
                           {
                              goldProb = 0.95;
                              goldMax = 30;
                           }
                           else if(locType.contains("pacrealm"))                          
                           {  //eating a power pellet in pac-realm
                              alertTime = System.currentTimeMillis();
                              Sound.alertGuards();
                           }
                           if(Math.random() < goldProb)
                           {
                              int gold = (int)(Math.random() * 20) + 1;
                              player.addGold(gold);
                              sendMessage("Found " + gold + " gold!");
                           }
                           else
                           {
                              Item stone = Item.getRandomStone();
                              player.addItem(stone.getName());
                              sendMessage("Found a sparkling "+stone.getName()+" stone!");
                           }                           
                        }
                        if(Utilities.caughtStealingChest())
                        {
                           if(TerrainBuilder.habitablePlace(player.getLocationType()))
                           {
                              CultimaPanel.player.addReputation(-1);
                              Player.stats[Player.ITEMS_STOLEN]++;
                           }
                           sendMessage("~"+ NPC.getRandomFrom(NPC.thiefGeneral));
                           Utilities.alertGuards();
                        }
                     }
                     byte chestValue = (byte)(currMap[player.getRow()][player.getCol()]);
                     int restoreDay = Utilities.getChestRestoreDay(locType);
                     if(!isBook)
                        tilesToRestore.add(new RestoreItem(player.getMapIndex(), player.getRow(), player.getCol(), chestValue, restoreDay));
                  }
                  else
                  {
                     if((currentPos.getName().toLowerCase().contains("chest") || currentPos.getName().toLowerCase().contains("book")) && isWolf)
                     {}
                     else
                     {
                        int radius = Utilities.rollDie(5,10);
                        int opacity = Utilities.rollDie(60,85);
                        int plX = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2)  - (radius/2);
                        int plY = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2) - (radius/2);   
                     
                        if(player.getLocationType().contains("graboid") || player.getLocationType().contains("beast"))
                        {
                           sendMessage("Innard tissue torn!");
                           Sound.vampAttack(50,100);
                           CultimaPanel.smoke.add(new SmokePuff(plX, plY, radius, opacity, SmokePuff.bloodCloud));
                        }
                        else
                        {
                           sendMessage(currentPos.getDescription().replace("_"," ") + " destroyed!");
                           Sound.secretWall();
                           CultimaPanel.smoke.add(new SmokePuff(plX, plY, radius, opacity, SmokePuff.dustCloud));
                           if(currentPos.getName().toLowerCase().contains("torch")) //chance we can get burned or catch on fire
                           {
                              if(Utilities.rollDie(50) > player.getAgility())
                              { 
                                 player.damage(5);
                                 int flamableBonus = 0;
                                 boolean playerFlamable = player.getArmor().isFlamable();
                                 if(playerFlamable)
                                    flamableBonus = 4;
                                 if(CultimaPanel.weather > 0 && !indoors && Utilities.rollDie(1,(6+flamableBonus)) <= CultimaPanel.weather)
                                 {}
                                 else 
                                 {
                                    if(Utilities.rollDie(10) < flamableBonus)
                                    {
                                       CultimaPanel.player.addEffect("fire");
                                       sendMessage("Thy attire has caught on fire!");
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
                  if((currentPos.getName().toLowerCase().contains("chest") || currentPos.getName().toLowerCase().contains("book")) && isWolf)
                  {}
                  else
                  {
                     byte floorValue = Utilities.adjacentIndoorFloor(player.getRow(),player.getCol());
                     byte groundValue = Utilities.adjacentIndoorGround(player.getRow(),player.getCol());
                     if(TerrainBuilder.habitablePlace(locType) && currentPos.getName().toLowerCase().contains("door") && floorValue != -1)
                     {  //if we are opening a door in a city/village/port, find an adjacent interior floor tile and replace the door position with that tile
                        currMap[player.getRow()][player.getCol()] = floorValue;
                     }
                     else if(TerrainBuilder.habitablePlace(locType) && currentPos.getName().toLowerCase().contains("torch") && groundValue != -1)
                     {  //if we are knocking over a torch in a city/village/port, find an adjacent outdoor ground tile and replace the torch position with that tile
                        currMap[player.getRow()][player.getCol()] = groundValue;
                     }
                     else
                     {
                        byte replacementTerrain = LocationBuilder.getAdjacentFloor(currMap, player.getRow(),player.getCol());
                        if(replacementTerrain == -1)
                        {
                           replacementTerrain = currMap[lastR][lastC];
                        }
                        currMap[player.getRow()][player.getCol()] = replacementTerrain;
                     }
                  }
               }
               else 
               {
                  if(sailing)
                  {
                     if(player.getImageIndex()==Player.BOAT)
                        sendMessage("Slow rowing.");
                     else 
                        if(Utilities.sameDir(Utilities.byteDirToString(windDir), dir))       //sailing same direction as the wind
                           sendMessage("Sailing with the winds.");
                        else if(Utilities.oppositeDir(Utilities.byteDirToString(windDir), dir))  //sailing perpindicular direction to the wind 
                           sendMessage("Fighting the winds.");
                  }
                  else if(!flight)
                  { 
                     if(player.onHorse())
                        sendMessage("Slow.");
                     else
                        sendMessage("Very slow.");
                  }  
               }
         }//Advance time depending on the speed of movement on the terrain
         else if(currentPos.getName().contains("_S_")) //Slow
         {
            if(!timestop && dir.length()>0)
            {
               Utilities.checkRainbow();
               if(flight || isWolf)
                  time += (increment/2);
               else if(player.onHorse())
                  time += increment;  
               else if(sailing)
               {
                  if(player.getImageIndex()==Player.BOAT)
                  {
                     time += (increment*2);
                     sendMessage("Slow rowing.");
                  }
                  else if(Utilities.sameDir(Utilities.byteDirToString(windDir), dir))       //sailing same direction as the wind
                  {
                     time += increment/2;
                     sendMessage("Fast sailing with the winds.");
                  }
                  else if(Utilities.sideDir(Utilities.byteDirToString(windDir), dir))  //sailing perpindicular direction to the wind
                     time += increment;
                  else 
                  {                                                                //sailing opposite direction of the wind    
                     time += increment*3;  
                     sendMessage("Slow - Fighting the winds.");
                  }
               }      
               else
               {
                  time += (increment*2);
                  sendMessage("Slow.");
               }
               Utilities.checkWerewolfStatus();  
            }
         }
         else if(dir.length() > 0)     
         {                                            //Passable
            if(!timestop)
            {
               Utilities.checkRainbow();
               if(player.getImageIndex()==Player.BOAT)
               {
                  time += (increment*2);
                  sendMessage("Slow rowing.");
               }
               else if(flight || player.onHorse() || isWolf)
                  time += (increment/2);
               else
                  time += increment;
               Utilities.checkWerewolfStatus();   
            }
            if(Display.isWinter() && groundColor.equals(Display.winter))   //no picking up flowers with snow cover
            {}
            else if(currentPos.getName().contains("Flower") && player.hasItem("floretbox") && !player.enoughFlowers())
            {
               if(currentPos.getName().toLowerCase().contains("red") && player.flowerBoxCount[Player.RED] >= Player.FLOWERS_FOR_MANNA)
               {}
               else if(currentPos.getName().toLowerCase().contains("yellow") && player.flowerBoxCount[Player.YELLOW] >= Player.FLOWERS_FOR_MANNA)
               {}
               else if(currentPos.getName().toLowerCase().contains("blue") && player.flowerBoxCount[Player.BLUE] >= Player.FLOWERS_FOR_MANNA)
               {}
               else if(currentPos.getName().toLowerCase().contains("violet") && player.flowerBoxCount[Player.VIOLET] >= Player.FLOWERS_FOR_MANNA)
               {}
               else if(currentPos.getName().toLowerCase().contains("green") && player.flowerBoxCount[Player.GREEN] >= Player.FLOWERS_FOR_MANNA)
               {}
               else
               {
                  String [] flowerMessage = {"Thou has picked some loveley COLOR flowers", "Fragrant COLOR flowers are added to thy Floretbox", "How lucky to find these beautiful COLOR petals", "These delicate COLOR flowers will go nicely with my collection"};
                  String [] terrainParts = currentPos.getName().toLowerCase().split("_");
                  String flowerColor = terrainParts[terrainParts.length - 2].trim(); //the color of the flower is the 2nd to last word in the terrain tile name
                  sendMessage(NPC.getRandomFrom(flowerMessage).replace("COLOR", flowerColor));
                  byte flowerValue = (byte)(Math.abs(value));
                  int restoreDay = CultimaPanel.days + 7 + Utilities.rollDie(10);      //record the day that the flowers will grow back
                  tilesToRestore.add(new RestoreItem(player.getMapIndex(), player.getRow(), player.getCol(), flowerValue, restoreDay));
                  currMap[player.getRow()][player.getCol()] = TerrainBuilder.getTerrainWithName("TER_$Grassland").getValue();
                  player.pickFlower(currentPos.getName());
               }
            }
         }
      
         if(time >= 24)                         //roll over time and use rations
         {
            double leftOver = time - Math.floor(time);
            time = (Math.floor(time) % 24) + leftOver;
            days++;
            Mission.checkMissionTimeOut();
            if(days >= 60)
               Achievement.earnAchievement(Achievement.ONE_ROUND_DOWN);
            if(player.getRations() > 0)
               player.useRation();
            else
            {
               player.damage(10);   
               sendMessage("Weakened from hunger.");
            }
            rain_probability = Math.random()/2;
            if(days % 2 == 0)
            {
               Utilities.setBounty();           //every two days, refresh which bounty is posted
            }
         }
         locType = player.getLocationType().toLowerCase();
         indoors = TerrainBuilder.indoors();
      
         if((int)(oldTime) != (int)(time))      //an hour has passed, so possibly change weather
            Utilities.changeWeather();
            
         if((map.get(mapIndex))[equalizeRow(r)][equalizeRow(c)] > 0)    //visited the location - a negative byte value means that we visited it
         {
            (map.get(mapIndex))[equalizeRow(r)][equalizeRow(c)] *= -1;  
            if(mapIndex == 0)
               revealedMap++;  
         }   
         if(player.getMapIndex() != 0 && leavingLocation)   
         {  //return to world map or last map in Teleporter stack
            if(player.getLocationType().equals("battlefield"))
            {
               if(numBattleFieldEnemies == 0 && corpses.get(player.getMapIndex()).size() > 0)
               {
                  player.stats[Player.BATTLES_WON]++;
                  corpses.get(player.getMapIndex()).clear();
               }
            }
            NPCPlayer dog = Utilities.getDog();
            if(!player.getDogName().equals("none") && dog==null)
            {
               Utilities.getAdjacentNPCs();
            }
            if(dog!=null)
               player.setDogBasicInfo(dog.basicInfo());
            else
               player.setDogBasicInfo("none");
            Utilities.removeDogFromLocation();
            mineCollapseTimer = -1;
            if(player.getLocationType().contains("arena"))
            {//clear corpses from the arena
               corpses.get(mapIndex).clear();
            }
            if(player.isInSpecialRealm())
            {
               if(player.getLocationType().contains("graboid") || player.getLocationType().contains("beast"))
               {
                  CultimaPanel.flashColor = Color.red;
                  CultimaPanel.flashFrame = CultimaPanel.numFrames;
                  CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
                  Sound.NPCdie(2);
                  //add corpse to the world where you escape
                  byte monsterType = NPC.SNAKEKING;
                  if(player.getMonsterType()==NPC.DRAGONKING || player.getMonsterType()==NPC.SEAMONSTER || player.getMonsterType()==NPC.SQUIDKING || player.getMonsterType()==NPC.SNAKEKING || player.getMonsterType()==NPC.HYDRACLOPS)
                     monsterType = player.getMonsterType();
                  NPCPlayer temp = new NPCPlayer(monsterType, player.getWorldRow(), player.getWorldCol(), 0, "world");
                  Corpse dead = temp.getCorpse();
                  CultimaPanel.corpses.get(0).add(dead);
                  for(int i=worldMonsters.size()-1; i>=0; i--)
                  {
                     NPCPlayer p = worldMonsters.get(i); 
                     if(p.getCharIndex()==monsterType)
                     {
                        for(Item it: p.getItems())
                           dead.addItem(it);
                        worldMonsters.remove(i);
                     }
                  }
                  player.addExperience(temp.getLevel()+50);
                  Achievement.earnAchievement(Achievement.BELLY_OF_THE_BEAST);
               }
               else
               {
                  CultimaPanel.flashColor = Color.blue;
                  CultimaPanel.flashFrame = CultimaPanel.numFrames;
                  CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
                  Sound.thunder();
                  byte[]weathers = {0,0,0,0,0,0,0,0,0,0,1,2,3,4,5,6,7,8,9,10};   
                  weather = Utilities.getRandomFrom(weathers);
                  groundColor = Display.findGroundColor(true);
                  if(player.getLocationType().toLowerCase().contains("wolfenstein"))
                     Achievement.earnAchievement(Achievement.RETURN_TO_CASTLE_WOLFENSTEIN);
                  if(player.hasItem("allosaur-egg"))
                  {  //the teleporter ferments the egg into a wisdomegg
                     player.removeItem("allosaur-egg");
                     player.addItem(Item.getWisdomEgg().getName());
                     sendMessage("Thy strange egg ferments and glows with power!");
                     Achievement.earnAchievement(Achievement.FERMENTED_IN_YOUR_MIND);
                  }   
               }
            }
            else if(TerrainBuilder.purgeLocation(player.getLocationType()) && achievements[Achievement.GHOST_TOWN]==0)
            {
               int numCivs = LocationBuilder.countCivilians(allLocations.get(player.getMapIndex()));
               if(numCivs == 0 && player.getReputation() > 0)
                  Achievement.earnAchievement(Achievement.GHOST_TOWN);
            }
            player.clearLocationData();
            Sound.rain(weather);    
            
            String currTerr = allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if((player.getLocationType().equals("port") || player.getLocationType().contains("city")) && stolenShip != null)
            {
               if(stolenShip.getCharIndex()==NPC.GREATSHIP)
                  player.setImageIndexTemp(Player.GREATSHIP);
               else if(stolenShip.getCharIndex()==NPC.BRIGANDSHIP)
                  player.setImageIndexTemp(Player.BRIGANDSHIP);    
               player.setOnGuard(false);                 
               player.setRow( stolenShip.getRow());
               player.setCol( stolenShip.getCol()); 
               player.setWorldRow( stolenShip.getRow());
               player.setWorldCol( stolenShip.getCol());
               ArrayList<Item> shipItems = stolenShip.getItems();  //transfer ship items to the player if they don't have it
               for(Item it:shipItems)
                  if(!player.hasItem(it.getName()))
                  {
                     sendMessage("You plundered:"+it.getName());
                     player.addItem(it.getName());
                  }
               Utilities.removeNPCat(stolenShip.getRow(), stolenShip.getCol(), stolenShip.getMapIndex());   
            }
            else if(player.getLocationType().equals("ship"))
            {
               shipGrappleTime = System.currentTimeMillis();
               NPCPlayer enemyShip = Utilities.getAdjacentEnemyShip();
               NPCPlayer ourShip = Utilities.getAdjacentOwnedShip();
               
               if(ourShip!=null)
               {  //board our ship
                  if(ourShip.getCharIndex()==NPC.GREATSHIP)
                     player.setImageIndexTemp(Player.GREATSHIP);
                  else if(ourShip.getCharIndex()==NPC.BRIGANDSHIP)
                     player.setImageIndexTemp(Player.BRIGANDSHIP);
                  else if(ourShip.getCharIndex()==NPC.BOAT)
                     player.setImageIndexTemp(Player.BOAT);
                  player.setOnGuard(false);   
               
                  Utilities.removeNPCat(ourShip.getRow(), ourShip.getCol(), ourShip.getMapIndex());
               }
               if(enemyShip!=null)
               {
                  if(numEnemiesOnShip == 0)
                  {  //make the enemy ship owned by player
                     enemyShip.setBody(100);  
                     enemyShip.setHasMet(true);
                  }
                  else
                  {  //the enemy ship still has sailors, so clamp its health to the number of sailors
                     int setHealth = numEnemiesOnShip * 100;
                     if(setHealth < enemyShip.getBody())
                        enemyShip.setBody(setHealth);
                  }
               }
               //see if there is a fire left, and if so, set enemy ship with "fire" effect.  Clear npcs from temporary ship battle.
               boolean shipFire = false;
               for(int i=worldMonsters.size()-1; i>=0; i--)
               {
                  NPCPlayer p = worldMonsters.get(i); 
                  if(!p.getLocationType().equals("ship"))
                     continue;
                  if(p.getCharIndex()==NPC.FIRE)
                     shipFire = true; 
                  worldMonsters.remove(i);
               }
               if(shipFire && enemyShip!=null)
                  enemyShip.addEffect("fire");
               
               if(ourShip==null && enemyShip==null && numEnemiesOnShip==0 && (currTerr.contains("ship_helm") || currTerr.contains("red_floor")))
               {  //something went wrong - we took over a ship but couldn't find one in the world, so lets make one
                  byte shipType = NPC.GREATSHIP;
                  if(Math.random() < 0.5)
                  {
                     shipType = NPC.BRIGANDSHIP;
                  }
                  enemyShip = new NPCPlayer(shipType, equalizeRow(player.getWorldRow()), equalizeCol(player.getWorldCol()), 0, "world"); ;
               }
               if(ourShip==null && numEnemiesOnShip == 0 && enemyShip!=null)
               {//if player is on the bridge (helm), put them on the ship and remove it from the world
                  if(currTerr.contains("ship_helm") || currTerr.contains("red_floor"))
                  {
                     if(enemyShip.getCharIndex()==NPC.GREATSHIP)
                        player.setImageIndexTemp(Player.GREATSHIP);
                     else if(enemyShip.getCharIndex()==NPC.BRIGANDSHIP)
                        player.setImageIndexTemp(Player.BRIGANDSHIP);
                     else if(enemyShip.getCharIndex()==NPC.BOAT)
                        player.setImageIndexTemp(Player.BOAT);
                     player.setOnGuard(false);    
                     player.setRow( enemyShip.getRow());
                     player.setCol( enemyShip.getCol()); 
                     player.setWorldRow( enemyShip.getRow());
                     player.setWorldCol( enemyShip.getCol());
                     ArrayList<Item> shipItems = enemyShip.getItems();  //transfer ship items to the player if they don't have it
                     for(Item it:shipItems)
                        if(!player.hasItem(it.getName()))
                        {
                           sendMessage("You plundered:"+it.getName());
                           player.addItem(it.getName());
                        }
                     Utilities.removeNPCat(enemyShip.getRow(), enemyShip.getCol(), enemyShip.getMapIndex());
                  }
               }
            }
            else if(player.getLocationType().contains("battlefield"))
            { 
               for(int i=0; i<missionStack.size(); i++)
               {
                  Mission m = missionStack.get(i);
                  if(m.getTargetRow()==player.getWorldRow() && m.getTargetCol()==player.getWorldCol())
                  {
                     if(!m.getFailed() && numBattleFieldEnemies==0)
                     {
                        Location currLoc = allLocations.get(player.getMapIndex());
                        if(currLoc != null)
                           currLoc.clearMonsters();
                     }
                     else
                        m.setFailed(true);
                     break;   
                  }
               }
            }
            
            for(int i=(CultimaPanel.civilians.get(player.getMapIndex())).size()-1; i>=0; i--)
            {
               NPCPlayer p = (CultimaPanel.civilians.get(player.getMapIndex())).get(i);
               if(p.getBody() <= 0)    //remove any civilians that are badly injured
               {
                  if(NPC.isCivilian(p.getCharIndex()) && (TerrainBuilder.habitablePlace(player.getLocationType()) || player.getLocationType().contains("arena")))
                  {  //after a month, NPCs will repopulate a city
                     NPCPlayer temp = p.clone();
                     if(p.getName().toLowerCase().contains("arenamaster"))
                        temp.setRestoreDay(CultimaPanel.days+Utilities.rollDie(7));
                     else
                        temp.setRestoreDay(CultimaPanel.days+15+Utilities.rollDie(15));
                     if(p.isZombie())		//unzombify our replacement
                        temp.setNumInfo(0);
                     CultimaPanel.NPCsToRestore.add(temp);
                  }
                  civilians.get(player.getMapIndex()).remove(i);
               }
               if(NPC.isGuard(p.getCharIndex()) && p.getMoveType()==NPC.ROAM && p.getNumInfo()==13)   //remove any guards that have been bribed, 13 for numInfo is the tag
               {
                  civilians.get(player.getMapIndex()).remove(i);
               } 
            }
            if(player.getLocationType().contains("dungeon") || player.getLocationType().contains("lair") || player.getLocationType().contains("cave") || player.getLocationType().contains("mine") || player.getLocationType().contains("sewer"))
            {
               for(int i=(CultimaPanel.civilians.get(player.getMapIndex())).size()-1; i>=0; i--)
               {
                  NPCPlayer p = (CultimaPanel.civilians.get(player.getMapIndex())).get(i);
                  if(p.isStatue())                              //keep statues
                     continue;   
                  if(p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN || p.getCharIndex()==NPC.WISE || p.getCharIndex()==NPC.CHILD)   //remove any prisoners that have been freed
                  {
                     civilians.get(player.getMapIndex()).remove(i);
                  } 
               }
            }
            if(player.getLocationType().contains("arena"))
            {
               for(int i=(CultimaPanel.civilians.get(player.getMapIndex())).size()-1; i>=0; i--)
               {
                  NPCPlayer p = (CultimaPanel.civilians.get(player.getMapIndex())).get(i);
                  if(p.isStatue() || p.getNumInfo()!=3 || p.getName().toLowerCase().contains("arenamaster"))                              //keep statues
                     continue;   
                  if(p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN || p.getCharIndex()==NPC.WISE)   //remove any prisoners that have been freed
                  {
                     civilians.get(player.getMapIndex()).remove(i);
                  } 
               }
            }
         //clear world monsters
            LinkedList<NPCPlayer> minions = new LinkedList();   //saved stone minions so we can add them into the map we teleport to
            for(int i=worldMonsters.size()-1; i>=0; i--)
            {
               NPCPlayer p = worldMonsters.get(i);
               if(NPC.isShip(p) && (p.hasMet() || player.getLocationType().equals("ship")))
                  continue;
               if(p.getLocationType().contains("camp"))
                  continue;  
               if(NPC.isHorse(p) && p.hasMet())
                  continue; 
               if(p.hasSlayMissionItem())    
                  continue;  
               if(p.isStatue())                               //keep statues
                  continue; 
               if(p.getName().endsWith("Sibling") || p.getName().endsWith("Brute"))  
               {
                  continue;      
               }
               if(p.isTamed())
                  continue;   
               if(p.isNonPlayer())
                  continue;   
               if(p.getName().contains("Minion"))        //save info for stone minions so they can come with us to the next map
               {
                  minions.add(p);   
               }       
               worldMonsters.remove(i);
            }
           
            Utilities.restoreDoors();
            Utilities.restoreTiles();
            Utilities.restoreBarrels();
            Utilities.restoreEggs();
            Utilities.restoreNPCs();
            Utilities.clearCorpses();
         
            String filename = "maps/"+player.getMapIndex()+"_"+(map.get(player.getMapIndex())).length+"_"+(map.get(player.getMapIndex()))[0].length+".bin";
            FileManager.writeToBinFile(filename, (map.get(player.getMapIndex()))); 
         
            Teleporter exit = null;
            NPCinSight.clear();
            AllNPCinSight.clear();
            AllNPCinRange.clear();
            if(!player.getMemory().isEmpty())
            {
               exit = player.getMemory().pop();
               double distTemp = Display.findDistance(exit.toRow(), exit.toCol(), player.getRow(), player.getCol());
               if(stolenShip!=null || (player.isSailing() && player.getMapIndex()==1 && distTemp==1))    //if we just comandeered a ship, we already have our row and col set
               {}
               else
               {
                  player.setRow(exit.toRow());
                  player.setCol(exit.toCol());
               }
               player.setMapIndex(exit.toIndex());
               if(player.getMapIndex() == 0)
               {
                  player.setLocationType("world");
               }
               else
                  player.setLocationType(exit.getLocType());
               Utilities.addDogToLocation();
               Utilities.addNpcToLocation();
               Utilities.addMinionsToLocation(minions); 
            }
            else
            {
               player.setMapIndex(0);
               player.setRow(player.getWorldRow());
               player.setCol(player.getWorldCol());
               player.setLocationType("world");
               Utilities.addDogToLocation();  
               Utilities.addNpcToLocation();
               Utilities.addMinionsToLocation(minions); 
            }
         }
         Teleporter tele = null;
         
         boolean nextToGraboid = false;
         boolean nextToBeast = false;
         byte monsterType = -1;
         if(player.getMapIndex()==0 && !player.isSailing() && !player.isFlying() && !player.isInvisible() && !player.onHorse())
         {
            for(NPCPlayer p:adjacentMonsters)
            {
               if(p!=null)
               {
                  byte pIndex = p.getCharIndex();
                  if(pIndex==NPC.GRABOID)
                     nextToGraboid = true;
                  if(pIndex==NPC.SNAKEKING || pIndex==NPC.DRAGONKING || pIndex==NPC.SQUIDKING || pIndex==NPC.SEAMONSTER || pIndex==NPC.HYDRACLOPS)
                  {
                     nextToBeast = true;
                     monsterType = pIndex;
                  }
               }
            }
         }
         NPCPlayer shipToGrapple = Utilities.shipAlignedForGrapple();
         if(shipToGrapple != null)
         {// make a teleporter and teleport the player to a new ship location at index 1
            tele = new Teleporter(1, -1, -1, "ship");
            if(player.onHorse())      //dismount horse changing weapons
               player.dismountHorse();
         }
         else if(currentPos.getName().toLowerCase().contains("sand") && nextToGraboid)
         {
            tele = new Teleporter(1, -1, -1, "graboid");    
         }
         else if(nextToBeast && (Utilities.rollDie(50) > player.getAgility() || !player.isOnGuard()))
         {
            if(monsterType != -1)      //record the monster type that swallows us so that it can be remembered to make the correct corpse type if we cut our way out
               player.setMonsterType(monsterType);
            tele = new Teleporter(1, -1, -1, "beast");  
            //do some damage from getting swallowed whole
            NPCPlayer temp = new NPCPlayer(monsterType, player.getWorldRow(), player.getWorldCol(), 0, "world");
            Weapon tempWeapon = temp.getWeapon();
            if(tempWeapon != null)
            {
               int dam = Utilities.rollDie(tempWeapon.getMinDamage(), tempWeapon.getMaxDamage()) + temp.getLevel();
               player.damage(dam/2);
            }
            //we just got swallowd whole by a monster - we lose our dog or escorted npc
            CultimaPanel.player.setDogBasicInfo("none"); 
            for(int i=worldMonsters.size()-1; i>=0; i--)
            {
               NPCPlayer p = worldMonsters.get(i); 
               if(p.isOurDog())     
                  worldMonsters.remove(i);
            }
            CultimaPanel.player.setNpcBasicInfo("none"); 
            for(int i=worldMonsters.size()-1; i>=0; i--)
            {
               NPCPlayer p = worldMonsters.get(i); 
               if(p.isEscortedNpc())     
                  worldMonsters.remove(i);
            }
         }
         else if(currentLoc!=null || currentPos.getName().toLowerCase().contains("enterance_strange"))
         {  //locations are easier to remeber, so add their immediate surroundings to the map
            for(int row= -2; row<=2; row++)
               for(int col= -2; col<=2; col++)
               {
                  if((map.get(player.getMapIndex()))[equalizeRow(r+row)][equalizeCol(c+col)] > 0)
                  {
                     (map.get(player.getMapIndex()))[equalizeRow(r+row)][equalizeCol(c+col)] *= -1;  
                     if(player.getMapIndex() == 0)
                        revealedMap++;
                  }
               }
            if(currentPos.getName().toLowerCase().contains("enterance_strange"))   //teleport us to a new insanity cave, underworld, etc
            {
               CultimaPanel.flashColor = Color.blue;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
               Sound.thunder();
            
               if(player.isInSpecialRealm() || days % 2 == 0)  //teleport us to a new insanity cave  
               {
                  ArrayList<Location>caves = new ArrayList<Location>();
                  for(Location loc:CultimaPanel.allLocations)  
                     if(loc.getTerrain().getDescription().toLowerCase().contains("insanity"))
                        caves.add(loc);
                  if(caves.size() > 0)    
                  {
                     Location thisCave = Utilities.getRandomFrom(caves);
                     tele = new Teleporter(0, thisCave.getRow(), thisCave.getCol(), "world");
                  }
                  else
                  {
                     int[] randLoc = returnRandomWorldLoc();
                     tele = new Teleporter(0, randLoc[0], randLoc[1], "world");
                  }
               }
               else
               {
                  int realmType = Utilities.rollDie(1,5);
                  //***TESTING*** realmType = 5;
                  if(realmType == 1)
                  {
                     player.setWellNumber((byte)(-1));   //reset well 
                     tele = new Teleporter(1, -1, -1, "underworld");
                  }
                  else if(realmType == 2)
                  {
                     tele = new Teleporter(1, -1, -1, "jurassica");
                  }
                  else if(realmType == 3)
                  {
                     tele = new Teleporter(1, -1, -1, "1942");
                  }
                  else if(realmType == 4)
                  {
                     tele = new Teleporter(1, -1, -1, "wolfenstein");
                  }
                  else //if(realmType == 5)
                  {
                     tele = new Teleporter(1, -1, -1, "pacrealm");
                     Sound.enterPacRealm();
                     player.putWeaponAway();    //we want to limit player to standard weapons in pac-realm
                  }
                  byte[]weathers = {0,0,0,0,0,0,0,0,0,0,1,2,3,4,5,6,7,8,9,10};   
                  weather = Utilities.getRandomFrom(weathers);
                  groundColor = Display.findGroundColor(true);
               }
            }  
            else  
               tele = currentLoc.getTeleporter();
         }   
         if(tele != null)
         {   
            player.clearLocationData();
            for(int i=worldMonsters.size()-1; i>=0; i--)
            {
               NPCPlayer p = worldMonsters.get(i); 
               if(NPC.isShip(p) && (p.hasMet() || tele.getLocType().equals("ship")))
                  continue; 
               if(p.getLocationType().contains("camp"))
                  continue;   
               if(NPC.isHorse(p) && p.hasMet())
                  continue;  
               if(p.hasSlayMissionItem())     
                  continue;  
               if(p.isStatue())                               //keep statues
                  continue; 
               if(p.getName().endsWith("Sibling") || p.getName().endsWith("Brute"))            //keep bandits from a save mission
                  continue;     
               if(p.isTamed())
                  continue;   
               if(p.isNonPlayer())
                  continue; 
               worldMonsters.remove(i);
            }
            if(civilians.size() > 1)
               civilians.get(1).clear();
            if(furniture.size() > 1)
               furniture.get(1).clear();
            if(corpses.size() > 1)
               corpses.get(1).clear();
            NPCinSight.clear();
            AllNPCinSight.clear();
            AllNPCinRange.clear();
            Utilities.restoreNPCs();
            if(flight)           //cancel flight when we enter a location
            {
               for(Spell s:player.getActiveSpells())
                  if(s.getName().contains("Flight"))
                  {
                     s.setTimeLeft(0);
                     player.countDownSpells();
                     break;
                  }
            }
         //save player status before entering a location  
            if(!player.hasEffect("poison") && !player.hasEffect("fire")                         //don't save if entering a location on fire or poisoned
               && !(player.isVampire() && (CultimaPanel.time >=8 && CultimaPanel.time <= 16))   //don't save if vampire is out in the daylight
               && shipToGrapple == null                                                         //don't save progress entering a temporary ship battle location
               && !player.isInSpecialRealm())                                                   //don't save if in a teleport realm
               FileManager.saveProgress();                 
         //end save player status
            player.getMemory().push(new Teleporter(player.getMapIndex(), player.getRow(), player.getCol(), player.getLocationType()));
            locType = currentPos.getDescription().toLowerCase();
            if(tele.getLocType().equals("ship"))
               locType = "ship";
            else if(tele.getLocType().equals("underworld"))
               locType = "underworld";
            else if(tele.getLocType().equals("pacrealm"))
               locType = "pacrealm";
            else if(tele.getLocType().equals("jurassica"))
               locType = "jurassica";
            else if(tele.getLocType().equals("1942"))
               locType = "1942";
            else if(tele.getLocType().equals("wolfenstein"))
               locType = "wolfenstein";
            else if(tele.getLocType().equals("world"))
               locType = "world";
            else if(tele.getLocType().equals("graboid"))
               locType = "graboid";
            else if(tele.getLocType().equals("beast"))
               locType = "beast";
            player.setLocationType(locType);
            
            if(!locType.equals("world") && (NPC.isMonsterKing(nextMonsterSpawn) || nextMonsterSpawn==NPC.DRAGON) && player.getLevel()<25)
               nextMonsterSpawn = -1; //if we enter a location and the next monster spawn is special, clear it so a weak character doesn't get stuck with it until combat
          
            if(locType.contains("mine") && days%3==0 && mineCollapseTimer == -1)
            {
               mineCollapseTimer = Utilities.rollDie(10,40);
            }  
         //sound - set scale for music depending on location
            if(locType.contains("city") || locType.contains("fort"))
               Sound.buildScale(Sound.mixolydian, Sound.majPent);
            else if(locType.contains("castle") || locType.contains("tower"))
               Sound.buildScale(Sound.harmMin, Sound.minPent);
            else if(locType.contains("village") || locType.contains("port"))
               Sound.buildScale(Sound.majPent, Sound.majPent);   
            indoors = TerrainBuilder.indoors();
            if(indoors)
               Sound.rain(0);
            else
               Sound.rain(weather);     
            if (tele.toRow() == -1 || tele.toCol() == -1)
            {  //procedurally generate the world at this mapIndex
               String provinceName = "";
               boolean capitalCity = false;
               if(currentLoc!=null)
               {
                  Display.provinceName(currentLoc.getName());
                  capitalCity = Utilities.isCapitalCity(currentLoc);
                  provinceName = currentLoc.getName();
               }
               if(!tele.getLocType().equals("ship") && !tele.getLocType().equals("underworld") && !tele.getLocType().equals("graboid") && !tele.getLocType().equals("beast") &&
                  !tele.getLocType().equals("jurassica") && !tele.getLocType().equals("1942") && !tele.getLocType().equals("wolfenstein") && !tele.getLocType().equals("pacrealm"))
               {
                  civilians.add(new ArrayList<NPCPlayer>());
                  corpses.add(new ArrayList<Corpse>());
                  furniture.add(new ArrayList<NPCPlayer>());
               }
               Object[] temp = null;
            
               if(tele.getLocType().equals("ship") && shipToGrapple!=null)
               {
                  provinceName = "Ship grapple";
                  temp = LocationBuilder.buildShipBattle(player, shipToGrapple);
                  //leave our ship there
                  if(temp!=null && player.isSailing())
                  {
                     byte leaveShip = NPC.GREATSHIP;
                     if(player.getImageIndex()==Player.BRIGANDSHIP)
                        leaveShip = NPC.BRIGANDSHIP;
                     else if(player.getImageIndex()==Player.BOAT)
                        leaveShip = NPC.BOAT;
                     NPCPlayer ourShip = new NPCPlayer(leaveShip, equalizeRow(player.getWorldRow()), equalizeCol(player.getWorldCol()), 0, "world"); 
                     ourShip.setBody(100);  
                     ourShip.setHasMet(true);
                     if(leaveShip == NPC.BOAT)
                     {
                        boats.add(ourShip);
                     }
                     else
                     {
                        worldMonsters.add(ourShip);
                     }
                     player.setImageIndex(player.getLastWeaponIndex());
                     player.setWeaponSelect(player.getLastWeaponSelect());
                     player.setOnGuard(false);
                  }
                  if(temp == null)  //temp ship battlefield map not created properly
                     tele = null;
               }
               else if(tele.getLocType().equals("underworld"))
               {
                  provinceName = "Underworld";
                  temp = LocationBuilder.buildUnderworld();
                  if(temp == null)  //temp map not created properly
                     tele = null;
                  else
                  {
                     well1 = null;
                     well2 = null;
                     Coord [] wells = (Coord[])(temp[2]);
                     if (wells != null)
                     {
                        well1 = wells[0];
                        well2 = wells[1];
                     }
                  }   
               }
               else if(tele.getLocType().equals("graboid"))
               {
                  provinceName = "Graboid-innards";
                  temp = LocationBuilder.buildGraboidInnards();
                  if(temp == null)  //temp map not created properly
                     tele = null;
               }
               else if(tele.getLocType().equals("beast"))
               {
                  provinceName = "Beast-innards";
                  temp = LocationBuilder.buildBeastInnards();
                  if(temp == null)  //temp map not created properly
                     tele = null;
               }
               else if(tele.getLocType().equals("jurassica"))
               {
                  provinceName = "Jurassica";
                  temp = LocationBuilder.buildJurassica();
                  if(temp == null)  //temp map not created properly
                     tele = null;
               }
               else if(tele.getLocType().equals("pacrealm"))
               {
                  provinceName = "Pac-Realm";
                  temp = LocationBuilder.buildPacRealm();
                  if(temp == null)  //temp map not created properly
                     tele = null;
               }
               else if(tele.getLocType().equals("1942"))
               {
                  provinceName = "1942";
                  temp = LocationBuilder.build1942();
                  if(temp == null)  //temp map not created properly
                     tele = null;
               }
               else if(tele.getLocType().equals("wolfenstein"))
               {
                  provinceName = "Castle-Wolfen";
                  temp = LocationBuilder.buildCastleWolfen();
                  if(temp == null)  //temp map not created properly
                     tele = null;
               }
               else if(locType.contains("battlefield"))
               {
                  temp = LocationBuilder.buildBattlefield(allTerrain.get(Math.abs(currMap[lastR][lastC])), locType, map.size(), provinceName, player.getLastDir(), lastR, lastC);
                  sendMessage("-----");
                  sendMessage("~"+Utilities.getRandomFrom(NPC.battlefieldCharge).replace("PROVINCE_NAME",Display.closestProvinceTo(player.getWorldRow(), player.getWorldCol())));
                  Sound.alertGuards();
                  player.stats[Player.BATTLES_FOUGHT]++;
               }
               else if(locType.contains("camp"))
               {
                  temp = LocationBuilder.buildCamp(allTerrain.get(Math.abs(currMap[lastR][lastC])), locType, map.size(), provinceName, player.getLastDir(), lastR, lastC, (byte)-1, (byte)-1);
               }
               else if(locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("dungeon"))
               {
                  temp = LocationBuilder.buildCave(locType, map.size(), player.getRow(), player.getCol(), false, player.getLastDir());
               }
               else //if(locType.equals("domicile"))
               {
                  byte waterDir = -1;
                  if(locType.contains("port") || capitalCity)
                  {
                     int pr = player.getRow();
                     int pc = player.getCol();
                     for(int dist = 1; dist<=2; dist++)
                     {
                        String rightTerrain = allTerrain.get(Math.abs(currMap[pr][equalizeCol(pc+dist)])).getName().toLowerCase();
                        String leftTerrain = allTerrain.get(Math.abs(currMap[pr][equalizeCol(pc-dist)])).getName().toLowerCase();
                        String upTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(pr-dist)][equalizeCol(pc)])).getName().toLowerCase();
                        String downTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(pr+dist)][equalizeCol(pc)])).getName().toLowerCase();
                        if(rightTerrain.contains("water"))
                           waterDir = LocationBuilder.EAST;
                        if(leftTerrain.contains("water"))
                           waterDir = LocationBuilder.WEST;
                        if(upTerrain.contains("water"))
                           waterDir = LocationBuilder.NORTH;
                        if(downTerrain.contains("water"))
                           waterDir = LocationBuilder.SOUTH;
                        if(waterDir == -1)
                        {
                           String upRightTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(pr-dist)][equalizeCol(pc+dist)])).getName().toLowerCase();
                           String upLeftTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(pr-dist)][equalizeCol(pc-dist)])).getName().toLowerCase();
                           String downRightTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(pr+dist)][equalizeCol(pc+dist)])).getName().toLowerCase();
                           String downLeftTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(pr+dist)][equalizeCol(pc-dist)])).getName().toLowerCase();
                           if(upRightTerrain.contains("water"))
                           {
                              waterDir = LocationBuilder.NORTH;
                              if(Math.random()<0.5)
                                 waterDir = LocationBuilder.EAST;
                           }
                           if(upLeftTerrain.contains("water"))
                           {
                              waterDir = LocationBuilder.NORTH;
                              if(Math.random()<0.5)
                                 waterDir = LocationBuilder.WEST;
                           }                           
                           if(downRightTerrain.contains("water"))
                           {
                              waterDir = LocationBuilder.SOUTH;
                              if(Math.random()<0.5)
                                 waterDir = LocationBuilder.EAST;
                           }                           
                           if(downLeftTerrain.contains("water"))
                           {
                              waterDir = LocationBuilder.SOUTH;
                              if(Math.random()<0.5)
                                 waterDir = LocationBuilder.WEST;
                           } 
                        }
                        if(waterDir != -1)
                           break;                   
                     }
                  }
                  temp = LocationBuilder.buildDomicile(allTerrain.get(Math.abs(currMap[lastR][lastC])), locType, map.size(), provinceName, player.getLastDir(), waterDir, capitalCity, lastR, lastC, false);
               }
               if(tele != null)
               {
                  if(achievements[Achievement.GLOBETROTTER] == 0) //so we don't traverse the locations if we already have the achievement
                  {  //check to see if we got the 50 location achievement
                     float numLocs = -1;     //start at -1 because we don't want to count the TEMP location
                     for(Location loc : CultimaPanel.allLocations)
                     {
                        String locType2 = loc.getTeleporter().getLocType().toLowerCase();
                        if(!locType2.contains("dungeon")) //don't include dungeons because their coordinates are internal for temples/castles
                        {   
                           numLocs++;
                        }  
                     }
                     if(numLocs>=50)
                        Achievement.earnAchievement(Achievement.GLOBETROTTER);
                  }
                  int locIndex = map.size();
                  byte[][]newLocation = (byte[][])(temp[0]);
                  int[] startCoord = (int[])(temp[1]);
                  int numOccupants = 0;
                  Coord battleEnemies = new Coord();
                  if(locType.contains("battlefield"))
                     battleEnemies = (Coord)(temp[2]);
                  else if(!locType.contains("underworld") && !locType.contains("pacrealm"))
                  {
                     numOccupants = ((Integer)(temp[2])).intValue();   //number of people at a temple, or # rooms in a cave, mine, lair or dungeon
                     
                     if((locType.contains("cave") || locType.contains("dungeon"))  && temp.length<=4 && temp[3]!=null)
                     {  //see if this is a cave with the 3-towers riddle, record the location of the 3 doors (towers)
                        Coord [] doorLocs = (Coord[])(temp[3]);
                        if(doorLocs.length == 3)
                        {
                           Coord door1 = doorLocs[0];
                           Coord door2 = doorLocs[1];
                           Coord door3 = doorLocs[2];
                           ArrayList<Coord>riddleDoors = new ArrayList<Coord>();
                           riddleDoors.add(door1);
                           riddleDoors.add(door2);
                           riddleDoors.add(door3);
                           currentLoc.setRiddlePoints(riddleDoors);
                        }
                     }  
                  }
                  if(tele.getLocType().equals("ship"))
                  {
                     locIndex = 1;
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], "ship");
                  }
                  else if(tele.getLocType().equals("underworld"))
                  {
                     locIndex = 1;
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], "underworld");
                  }
                  else if(tele.getLocType().equals("pacrealm"))
                  {
                     locIndex = 1;
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], "pacrealm");
                  }
                  else if(tele.getLocType().equals("graboid"))
                  {
                     locIndex = 1;
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], "graboid");
                  }
                  else if(tele.getLocType().equals("beast"))
                  {
                     locIndex = 1;
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], "beast");
                  }
                  else if(tele.getLocType().equals("jurassica"))
                  {
                     locIndex = 1;
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], "jurassica");
                  }
                  else if(tele.getLocType().equals("1942"))
                  {
                     locIndex = 1;
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], "1942");
                  }
                  else if(tele.getLocType().equals("wolfenstein"))
                  {
                     locIndex = 1;
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], "wolfenstein");
                  }
                  else
                     tele = new Teleporter(locIndex, startCoord[0], startCoord[1], locType);
                  if(currentLoc != null)
                  {
                     currentLoc.setTeleporter(tele);
                     currentLoc.setMapIndex(locIndex);
                  }
               //pick and add monsters to currentLoc
                  if(locType.contains("battlefield"))
                  {
                     ArrayList<Coord> monsterFreq = new ArrayList<Coord>();
                     monsterFreq.add(battleEnemies);
                     currentLoc.setMonsterFreq(monsterFreq);
                  }
                  else if(locType.contains("sewer") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("dungeon"))
                  {
                     byte monsterIndex = NPC.randomWorldMonster();
                     if(locType.contains("dungeon"))
                        monsterIndex = NPC.randomDominantDungeonMonster();
                     else if(locType.contains("lair"))
                        monsterIndex = NPC.randomDominantLairMonster();
                     else if(locType.contains("cave"))
                        monsterIndex = NPC.randomCaveMonster();
                     else if(locType.contains("mine"))
                        monsterIndex = NPC.randomDominantMineMonster();
                     else if(locType.contains("sewer"))
                        monsterIndex = NPC.randomSewerMonster();
                     int numMonsters = (int)(Math.random() * (numOccupants/2 - numOccupants/3 + 1)) + (numOccupants/3);
                     double monsterSize = NPC.getSize(monsterIndex);
                     if(monsterSize > 1 && monsterIndex!=NPC.TREE)   
                        numMonsters = (int)(Math.max(1, numMonsters / (monsterSize*2)));    //we need less of very dangerous monsters
                     ArrayList<Coord> monsterFreq = new ArrayList<Coord>();
                     monsterFreq.add(new Coord(monsterIndex, numMonsters));
                     currentLoc.setMonsterFreq(monsterFreq);
                  }
                  else if(locType.contains("temple") && numOccupants == 0)
                  {
                     byte monsterIndex = NPC.randomTempleMonster();
                     int numMonsters = (int)(Math.random() * (newLocation.length/4 - newLocation.length/8 + 1)) + (newLocation.length/8);
                     ArrayList<Coord> monsterFreq = new ArrayList<Coord>();
                     monsterFreq.add(new Coord(monsterIndex, numMonsters));
                     currentLoc.setMonsterFreq(monsterFreq);
                  }           
                  if(   tele.getLocType().equals("ship") || tele.getLocType().equals("underworld") || tele.getLocType().equals("graboid") || tele.getLocType().equals("beast")
                     || tele.getLocType().equals("jurassica") || tele.getLocType().equals("1942") || tele.getLocType().equals("wolfenstein") || tele.getLocType().equals("pacrealm"))
                     map.set(1, newLocation);
                  else
                  {
                     Display.swap(allLocations, currentLocIndex, locIndex);   //move the Location into the same index in allLocations List as the mapIndex in the maps List
                     map.add(newLocation);
                  }
                  FileManager.writeOutLocationsToFile(CultimaPanel.allLocations, "maps/worldLocs.txt");
                  String filename = "maps/"+locIndex+"_"+newLocation.length+"_"+newLocation[0].length+".bin";
                  mapFiles.add(filename);
                  FileManager.writeOutMapFileNames(mapFiles, "maps/mapFileNames.txt");
                  FileManager.writeToBinFile(filename, newLocation); 
               
                  Location loc = LocationBuilder.doesCastleHaveDungeon(locIndex);
                  if(loc!=null && loc.getMapIndex() == -1)
                  {  //procedurally generate the world at this mapIndex
                     if(locType.contains("city") || locType.contains("fortress"))
                     {
                        loc = LocationBuilder.constructAdventureAt(loc, "sewer");
                     }
                     else
                     {
                        loc = LocationBuilder.constructAdventureAt(loc, "dungeon");
                     }
                  }
                  if(locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("castle") || locType.contains("tower") || locType.contains("domicile") || locType.contains("port") || locType.contains("temple") || locType.contains("arena"))
                  {
                     FileManager.writeOutAllNPCFile(CultimaPanel.civilians, "maps/civilians.txt");
                     FileManager.writeOutAllNPCFile(CultimaPanel.furniture, "maps/furniture.txt");
                  }
               }  //end if(tele!=null) //if the location was not built properly
            }//end if(tele.toRow()==-1 || tele.toCol()==-1), location had not been procedurally built yet
            if(tele != null)     //location has already been built
            {
               if(locType.contains("arena"))
               {
                  Utilities.setupArena(tele.toIndex());
               }
               else if(locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("castle") || locType.contains("tower") || locType.contains("domicile") || locType.contains("port"))
               {
                  if(time >= 6 && time <= 20)
                  {
                     if(locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("domicile") || locType.contains("port"))
                        Utilities.putNPCsOnStreet(tele.toIndex());
                     else
                        Utilities.makeNPCsRoam(tele.toIndex());
                     if(player.getReputation() < -100)
                        Utilities.makeNPCsRun(tele.toIndex());
                  }
                  else 
                     Utilities.putNPCsToBed(tele.toIndex());
               }
               if(locType.contains("temple") && player.getRow()==50 && player.getCol()==50)
               {  //our endgame temple
                  Utilities.setTempleDoors(tele.toIndex());
               }
               player.setMapIndex(tele.toIndex());
               boolean bridgeOfDeath = false;
               if(locType.contains("temple"))
               {  //see if this is a temple with a bridge of death so we know whether or not we can enter from the side consistent with our last direction
                  for(NPCPlayer p: civilians.get(tele.toIndex()))
                  {
                     if(p.getName().contains("Keeper of the Bridge") || p.getName().contains("Black Knight"))
                     {
                        bridgeOfDeath = true;
                        break;
                     }
                  }
               }
               if(locType.contains("village") || locType.contains("domicile") || locType.contains("battlefield") || locType.contains("camp") || (locType.contains("temple") && !bridgeOfDeath))
               {  //shift location to represent 
                  int tR = tele.toRow();
                  int tC = tele.toCol();
                  byte lastDir = player.getLastDir();
                  int mapDimension = map.get(player.getMapIndex()).length;   
                  if(lastDir == LocationBuilder.SOUTH)      //enter in row 0 from the top
                  {
                     tR = 0;
                     tC = mapDimension/2;
                  }      
                  else if(lastDir == LocationBuilder.WEST)  //enter in last col from the right side
                  {
                     tR = mapDimension/2;
                     tC = mapDimension - 1;
                  }
                  else if(lastDir == LocationBuilder.NORTH) //enter in last row from the bottom
                  {
                     tR = mapDimension - 1;
                     tC = mapDimension/2;
                  }  
                  else                                      //enter in col 0 from the left side
                  {
                     tR = mapDimension/2;
                     tC = 0;
                  }  
                  player.setRow(tR);
                  player.setCol(tC);
               }
               else if(locType.contains("port") || locType.contains("city") || locType.contains("fortress"))  //might have a wall around it - find the sides that have streets into the city
               {
                  int tR = tele.toRow();
                  int tC = tele.toCol();
                  byte lastDir = player.getLastDir();
                  int mapDimension = map.get(player.getMapIndex()).length;
                  //populate streetOnSide to see if there is on a street on each side of the city   
                  boolean [] streetOnSide = Utilities.findCityStreetEntrance(map.get(player.getMapIndex()));
                  if(lastDir == LocationBuilder.SOUTH && streetOnSide[LocationBuilder.NORTH])      //enter in row 0 from the top
                  {  //we moved into the city from the SOUTH so we are trying to enter the city from the NORTH.  If there is a street there, make that our spawn location
                     tR = 0;
                     tC = mapDimension/2;
                  }      
                  else if(lastDir == LocationBuilder.WEST && streetOnSide[LocationBuilder.EAST])  //enter in last col from the right side
                  {  //we moved into the city from the WEST so we are trying to enter the city from the EAST.  If there is a street there, make that our spawn location
                     tR = mapDimension/2;
                     tC = mapDimension - 1;
                  }
                  else if(lastDir == LocationBuilder.NORTH && streetOnSide[LocationBuilder.SOUTH]) //enter in last row from the bottom
                  {  //we moved into the city from the NORTH so we are trying to enter the city from the SOUTH.  If there is a street there, make that our spawn location
                     tR = mapDimension - 1;
                     tC = mapDimension/2;
                  }  
                  else if(lastDir == LocationBuilder.EAST && streetOnSide[LocationBuilder.WEST])   //enter in col 0 from the left side
                  {  //we moved into the city from the EAST so we are trying to enter the city from the WEST.  If there is a street there, make that our spawn location
                     tR = mapDimension/2;
                     tC = 0;
                  }  
                  player.setRow(tR);
                  player.setCol(tC);
               }
               else
               {
                  player.setRow(tele.toRow());
                  player.setCol(tele.toCol());
               }
               Location enterLoc = allLocations.get(tele.toIndex());
               if(tele.getLocType().equals("ship") && shipToGrapple!=null)
               {
                  if(shipToGrapple.getCharIndex()==NPC.GREATSHIP)
                     sendMessage("Grappling with a mighty Greatship");
                  else
                     sendMessage("Grappling with a fearsome Brigandship");
               }
               else if(tele.getLocType().equals("underworld"))
                  sendMessage("Descending to the Underworld");
               else if(tele.getLocType().equals("pacrealm"))
                  sendMessage("Materializing in the Pac-Realm");
               else if(tele.getLocType().equals("jurassica") || tele.getLocType().equals("1942") || tele.getLocType().equals("wolfenstein"))
                  sendMessage("Spinning through a Time-Portal");
               else if(tele.getLocType().equals("graboid"))
                  sendMessage("Swallowed whole by a Graboid");
               else if(tele.getLocType().equals("beast"))
                  sendMessage("Swallowed whole by a great beast");
               else if(locType.contains("city"))
                  sendMessage("Entering the gates of "+enterLoc.getName());
               else if(locType.contains("fortress"))
                  sendMessage("Passing through the gates of "+enterLoc.getName());
               else if(locType.contains("port"))
                  sendMessage("Walking onto the muddy streets of "+enterLoc.getName());
               else if(locType.contains("village"))
                  sendMessage("Walking onto the paths of "+enterLoc.getName());
               else if(locType.contains("domicile"))
                  sendMessage("Approaching the domicile of "+enterLoc.getName());
               else if(locType.contains("castle"))
               {
                  sendMessage("Crossing the drawbridge of "+enterLoc.getName()); 
                  if(CultimaPanel.player.getImageIndex()!=Player.NONE && CultimaPanel.player.getImageIndex()!=Player.STAFF && CultimaPanel.player.getImageIndex()!=Player.LUTE && CultimaPanel.player.getImageIndex()!=Player.TORCH)
                     sendMessage("~Sheathe thy arms before approaching the King!");
               }
               else if(locType.contains("tower"))
               {
                  sendMessage("Passing under the portcullis of "+enterLoc.getName());
                  if(CultimaPanel.player.getImageIndex()!=Player.NONE && CultimaPanel.player.getImageIndex()!=Player.STAFF && CultimaPanel.player.getImageIndex()!=Player.LUTE && CultimaPanel.player.getImageIndex()!=Player.TORCH)
                     sendMessage("~Sheathe thy weapon before approaching royalty!");
               }
               else if(locType.contains("temple"))
                  sendMessage("Approaching the columns of "+enterLoc.getName());
               else if(locType.contains("cave"))
                  sendMessage("Entering the mouth of "+enterLoc.getName());
               else if(locType.contains("sewer"))
                  sendMessage("Descending into the sewers of "+enterLoc.getName());
               else if(locType.contains("mine"))
                  sendMessage("Ducking into the mouth of "+enterLoc.getName());
               else if(locType.contains("lair"))
                  sendMessage("Entering the maw of "+enterLoc.getName());
               else if(locType.contains("dungeon"))
                  sendMessage("Charging into the maw of "+enterLoc.getName());
               else if(locType.contains("battlefield"))
                  sendMessage("Passing into the fields of "+enterLoc.getName());
               else if(locType.contains("camp"))
                  sendMessage("Sneaking into the camp of "+enterLoc.getName());
               else if(enterLoc.getName().toLowerCase().equals("temp"))
               {
                  sendMessage("Vaulting towards the unknown!");
               }
               else
               {
                  sendMessage("Entering "+enterLoc.getName());
               }
               if((locType.contains("castle") || locType.contains("tower")) && player.isOnGuard())
               {
                  sendMessage("~Lower thy weapon before approaching the King!");
               }
            //add monsters to Location
               if(locType.contains("sewer") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("dungeon") || locType.contains("temple"))
               {
                  if(locType.contains("dungeon") && currentLoc.getRiddlePoints().size()==3)
                  {
                     door1 = currentLoc.getRiddlePoints().get(0);
                     door2 = currentLoc.getRiddlePoints().get(1);
                     door3 = currentLoc.getRiddlePoints().get(2);
                     tower1 = null;
                     tower2 = null;
                     tower3 = null;
                  }
                  else if(locType.contains("cave") && currentLoc.getRiddlePoints().size()==3)
                  {
                     //make sure the puzzle master is there.  If not, remove the riddle points
                     boolean puzzleMaster = false;
                     for(NPCPlayer p: civilians.get(tele.toIndex()))
                     {
                        if(p.getName().contains("Puzzle-Master"))
                        {
                           puzzleMaster = true;
                           break;
                        }
                     }
                     if(!puzzleMaster)
                     {
                        door1 = null;
                        door2 = null;
                        door3 = null;
                        tower1 = null;
                        tower2 = null;
                        tower3 = null;
                        currentLoc.clearRiddlePoints();
                     }
                     else
                     {
                        door1 = currentLoc.getRiddlePoints().get(0);
                        door2 = currentLoc.getRiddlePoints().get(1);
                        door3 = currentLoc.getRiddlePoints().get(2);
                        int numDisks = 4;
                        if(days==3 || days%10 == 3)
                           numDisks = 3;
                        tower1 = new int[numDisks];               //these are the disk numbers on the towers
                        tower2 = new int[numDisks];
                        tower3 = new int[numDisks];
                        for(int i=0; i<tower1.length; i++)        //tower 1 starts with all disks on it
                           tower1[i] = i+1;
                     }
                  }
                  else
                  {
                     door1 = null;
                     door2 = null;
                     door3 = null;
                     tower1 = null;
                     tower2 = null;
                     tower3 = null;
                  }
                  ArrayList<Coord> monsterFreq = currentLoc.getMonsterFreq();
                  int totalMonsters = currentLoc.getMonsterTotal();              
                  for(int i=0; i < totalMonsters; i++)
                  {
                     byte monsterIndex = NPC.randomWorldMonster();
                     if(locType.contains("dungeon"))
                        monsterIndex = NPC.randomDungeonMonster();
                     else if(locType.contains("lair"))
                        monsterIndex = NPC.randomLairMonster();
                     else if(locType.contains("cave"))
                        monsterIndex = NPC.randomCaveMonster();
                     else if(locType.contains("mine"))
                        monsterIndex = NPC.randomMineMonster(); 
                     else if(locType.contains("sewer"))
                        monsterIndex = NPC.randomSewerMonster();  
                     else if(locType.contains("temple"))
                        monsterIndex = NPC.randomTempleMonster();                  
                     if(monsterFreq!=null && monsterFreq.size() > 0)
                     {
                        monsterIndex = (byte)((monsterFreq.get(0)).getRow());
                        if(NPC.isBrigand(monsterIndex))    //make sure we get a variety of brigand types
                           monsterIndex = NPC.randomBrigand();
                        if(monsterFreq.size() > 1)
                        {
                        //pick monster depending on its frequency/totalMonsters percentage
                           int total = totalMonsters;
                           for(Coord p:monsterFreq)
                           {
                              if(total > 0)
                              {
                                 double prob = (p.getCol()/(double)total)*100;
                                 if(Utilities.rollDie(100) < prob)
                                 {
                                    monsterIndex = (byte)(p.getRow());
                                    if(NPC.isBrigand(monsterIndex))    //make sure we get a variety of brigand types
                                       monsterIndex = NPC.randomBrigand();
                                    break;
                                 }
                                 total -= (int)(p.getCol());
                              }
                              else
                              {
                                 monsterIndex = (byte)(monsterFreq.get(monsterFreq.size()-1).getRow());
                                 if(NPC.isBrigand(monsterIndex))    //make sure we get a variety of brigand types
                                    monsterIndex = NPC.randomBrigand();
                              
                              }
                           }
                        }     
                     }
                     Coord monsterLoc = Utilities.findMonsterSpawn("any", monsterIndex);
                     byte DEMON_SWINE_PROB = 20;            //the probability that a demon in a dungeon wants to play a game of swine with high stakes  
                  
                     if(monsterLoc != null)
                     {
                        int monsterRow = monsterLoc.getRow();
                        int monsterCol = monsterLoc.getCol();
                        NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, player.getMapIndex(), locType);
                        //maybe make a dungeon demon one that wants to play a game of swine
                        if(locType.contains("dungeon") && monsterIndex==NPC.DEMON && Utilities.rollDie(100) < DEMON_SWINE_PROB  && !thereIsDemonSwinePlayer)
                        {
                           monster.setIsSwinePlayer(true);
                           monster.setMoveType(NPC.STILL);
                           if(Math.random() < 0.25)
                              monster.addItem(Item.getDemonsCube());
                           (CultimaPanel.civilians.get(player.getMapIndex())).add(monster);
                        }
                        else
                           worldMonsters.add(monster);
                        Mission.checkSlayMission(monster);
                     }
                  }  
               }
            //end add monsters to Location
               Utilities.addDogToLocation();  
               Utilities.addNpcToLocation();  
            }
         }//end if(tele != null)
         else
         {
            if(weather > 1 && !indoors)
            {
               currMap = map.get(player.getMapIndex());
               Terrain currStep = allTerrain.get(Math.abs(currMap[player.getRow()][player.getCol()]));
               if(lastR>=0 && lastC>=0 && lastR<currMap.length && lastC<currMap[0].length)
               {
                  Terrain lastStep = allTerrain.get(Math.abs(currMap[lastR][lastC]));
                  if(indoors)
                     Sound.rain(0);
                  else if(TerrainBuilder.isIndoorFloor(lastStep) && !TerrainBuilder.isIndoorFloor(currStep))
                     Sound.rain(weather);    
               }
            }
         }
         Utilities.moveMonsters(true); //bonus move for monsters with higher agility  
         Utilities.moveNPCs(true);
      
      //take time off active, time-duration spells      
         if(player.getActiveSpells().size() > 0 )
            player.countDownSpells();
         player.runEffects(); 
      
      //update map to include things we see around us depending on awareness level
         int focusRow = player.getRow();                        	//center of where our map is focused
         int focusCol = player.getCol();
         mapIndex = player.getMapIndex();
         String locationType = player.getLocationType();  
         int awareness = player.getAwareness();
         blocked = Display.blockedView();           //collection of Terrain locations that need to be hidden because they are blocked by something tall
         double tempTime = time;
      //change tempTime to some kind of dark if we are in a cave, mine, lair (indoors)
         if(locationType.equals("sewer") || locationType.equals("cave") || locationType.equals("mine") || locationType.equals("lair") || locationType.equals("dungeon"))
         {
            if(time >=10 && time <= 14)
               tempTime = 23;
            else if(time >=8 && time <=16)
               tempTime = 3;
            else
               tempTime = 1;
         }
         darkened = Display.darkenedCells(tempTime);    //collection of Terrain locations that need to be hidden because of the time of day (darkness)
         Utilities.checkIfOnACorpse();
      
         if(awareness>=0 && awareness<=3)
         {
            int range = awareness * (SCREEN_SIZE/6);
            if(range == 0)
               range = (SCREEN_SIZE/6);
            if(flight && !indoors && range < SCREEN_SIZE * 2)
               range = SCREEN_SIZE * 2;   
            for(int vr=focusRow - range; vr<=focusRow+range; vr++)
               for(int vc=focusCol - range; vc<=focusCol+range; vc++)
               {
                  Coord spot = new Coord(vr, vc);
                  if(mapIndex == 0)
                     spot = new Coord(equalizeWorldRow(vr), equalizeWorldCol(vc));
                  if((!blocked.contains(spot) && !darkened.contains(spot)) || (flight && !indoors))
                  {
                     if(mapIndex == 0)
                     {
                        if(((map.get(mapIndex))[equalizeWorldRow(vr)][equalizeWorldCol(vc)]) > 0)
                        {
                           ((map.get(mapIndex))[equalizeWorldRow(vr)][equalizeWorldCol(vc)]) *= -1;
                           revealedMap++;
                        }
                     }
                     else if(!LocationBuilder.outOfBounds((map.get(mapIndex)), vr, vc))
                     {
                        if(((map.get(mapIndex))[vr][vc]) > 0)
                           ((map.get(mapIndex))[vr][vc]) *= -1;
                     }
                  }
               }
         } 
         doorToClose = null;
         if(TerrainBuilder.habitablePlace(player.getLocationType()))
         {
            doorToClose = Utilities.nextToOpenDoor(player.getRow(), player.getCol());
         }
         ourBeds = null;
         if(TerrainBuilder.isCity(player.getLocationType()))
         {
            ourBeds = LocationBuilder.findOurBeds(); 
         }
         if(!player.onHorse() && !flight && !LocationBuilder.outOfBounds(currMap, player.getRow(), player.getCol()))
         {
            Terrain currStep = allTerrain.get(Math.abs(currMap[player.getRow()][player.getCol()]));
            if(currStep.getName().toLowerCase().contains("poison") && !Display.isWinter() && !player.hasEffect("poison") && Math.random() < 0.1)
            {
               sendMessage("-----");
               sendMessage("The wretched filth you are wading in sickens thee");
               player.addEffect("poison");
            }
         }
         repaint();			//refresh the screen
         return; 
      }		//end menuMode == TRAVEL
   }

//THIS METHOD IS ONLY CALLED THE MOMENT A KEY IS HIT - NOT AT ANY OTHER TIME
//pre:   k is a valid keyCode
//post:  changes the players position depending on the key that was pressed (sent from the driver)
//			keeps the player in the bounds of the size of the array board, then the enemy moves
   public void processUserInput(int k)
   {
      int mapIndex = player.getMapIndex();
      byte[][] currMap = (map.get(mapIndex));
      String currTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(player.getRow())][player.getCol()])).getName().toLowerCase();      
      boolean indoors = TerrainBuilder.indoors();
      boolean isWolf = (CultimaPanel.player.isWolfForm());
      if(menuMode == TRAVEL)
      {
         if(player.isOnGuard() && (k==KeyEvent.VK_LEFT || k==KeyEvent.VK_RIGHT) && !player.isSailing() && !player.isFlying() && !player.onHorse() && !player.isWolfForm())
         {  //this will change us between left and right facing images depending on which key we hit
            player.setImageIndex(player.getImageIndex());
         }
         if(player.getActiveSpell("Unseen")!=null)
         {
            player.removeSpell("Unseen");
         }
      }
      if(talkSel && selected!=null)
      {
         if(k==KeyEvent.VK_ESCAPE || k==KeyEvent.VK_DOWN )	
         { 
            if(talkSel && selected!=null && selected.getCharIndex()==NPC.DEMON && selected.swinePlayer())
               NPC.endDemonSwineGame(selected);
            else if(talkSel && selected!=null && selected.getCharIndex()==NPC.TAXMAN && selected.getNumInfo()!=10 && days > selected.getEndChaseDay() && !player.hasItem("blessed-crown"))
            {  //we are trying to run out on the TAXMAN without paying
               NPC.taxEvasion(selected);
            }   
            player.setOnGuard(false);
            talkSel = false;
            selected.setTalking(false);
            selected = null;
            selectedBarrel = null;
            selectedTerrain = null;
            selectedRenovate = null;
            selectedBuild = null;
            typed = "";
         }
         else if(k==KeyEvent.VK_ENTER)            //send the message to the NPC and get a response
         {
            if(typed.toLowerCase().equals("bye") || typed.toLowerCase().equals("goodbye") || typed.toLowerCase().equals("good bye") || typed.toLowerCase().equals("balk")|| (selected!=null && selected.getBody() <= 0))
            {
               if(talkSel && selected!=null && selected.getCharIndex()==NPC.DEMON && selected.swinePlayer())
                  NPC.endDemonSwineGame(selected);               
               talkSel = false;
               selected.setTalking(false);
               selected = null;
               selectedBarrel = null;
               selectedTerrain = null;
               selectedRenovate = null;
               selectedBuild = null;
               repaint();
               return;
            }
            if(selected.isOurDog() || (selected.getCharIndex()==NPC.DOG && !selected.hasBeenAttacked()))
            {
               String dogName = "Doggie";
               if(ourDog != null)
               {
                  dogName = ourDog.getName();
               }
               else
               {
                  double nameRoll = Math.random();
                  if(nameRoll < 0.5)
                  {
                     dogName = "Dog-o";
                  }
                  else if(nameRoll < 0.75)
                  {
                     dogName = "Pup";
                  }
                  else if(nameRoll < 0.9)
                  {
                     dogName = "Puppy";
                  }
                  else if(nameRoll < 0.95)
                  {
                     dogName = "Doge";
                  }
                  else
                  {
                     dogName = "Dog McDogface";
                  }
               }
               if(typed.toLowerCase().equals("c") || typed.toLowerCase().equals("come"))
                  typed = (Utilities.getRandomFrom(NPC.recallDog).replace("DOG_NAME", dogName));//"come";
               else if(typed.toLowerCase().equals("s") || typed.toLowerCase().equals("stay"))
                  typed = (Utilities.getRandomFrom(NPC.stayDog).replace("DOG_NAME", dogName));//"stay";
               else if(typed.toLowerCase().equals("t") || typed.toLowerCase().equals("treat"))
                  typed = (Utilities.getRandomFrom(NPC.treatDog).replace("DOG_NAME", dogName));//"treat";
               else if(typed.toLowerCase().equals("g") || typed.toLowerCase().equals("go"))
                  typed = (Utilities.getRandomFrom(NPC.goDog).replace("DOG_NAME", dogName));//"go";
               else if(typed.toLowerCase().equals("a") || typed.toLowerCase().equals("attack"))
                  typed = (Utilities.getRandomFrom(NPC.attackDog).replace("DOG_NAME", dogName));//"attack";
            }
            else
            {
               if(typed.toLowerCase().equals("a") || typed.toLowerCase().equals("arms") || typed.toLowerCase().equals("weapon") || typed.toLowerCase().equals("weapons"))
                  typed = Utilities.getRandomFrom(NPC.askArms);//"arms";
               else if(typed.toLowerCase().equals("b"))
               {
                  if(selected.getBounty()>0 || selected.isWerewolf())
                     typed = Utilities.getRandomFrom(NPC.askBounty);
                  else if(NPC.isGuard(selected) || selected.getMoveType()==NPC.CHASE)
                     typed = Utilities.getRandomFrom(NPC.askBribe);
                  else 
                     typed = Utilities.getRandomFrom(NPC.askBountyInfo);   
               }
               else if(typed.toLowerCase().equals("c") || typed.toLowerCase().equals("cure"))
                  typed = Utilities.getRandomFrom(NPC.askCure);//"cure";
               else if(typed.toLowerCase().equals("d") || typed.toLowerCase().equals("dog") || typed.toLowerCase().equals("dogs"))
                  typed = Utilities.getRandomFrom(NPC.askDog);//"dog";
               else if(typed.toLowerCase().equals("g") || typed.toLowerCase().equals("gold"))
               {
                  if(selected.getCharIndex()==NPC.JESTER && selected.getNumInfo()==10)
                  {} ////jester has a riddle quest in which the answer is "gold"
                  else if(NPC.isGuard(selected) || (selected.getMoveType()==NPC.CHASE && selected.getCharIndex()!=NPC.TAXMAN && !selected.isEscortedNpc()))
                  {
                     typed = Utilities.getRandomFrom(NPC.giveTenGold);
                  }
                  else if(selected.getCharIndex()==NPC.BEGGER)
                  {
                     typed = Utilities.getRandomFrom(NPC.giveOneGold);
                  }
                  else if(selected.getName().toLowerCase().contains("butcher") || selected.getName().toLowerCase().contains("baker") || selected.getName().toLowerCase().contains("barkeep"))  
                  {
                     typed = Utilities.getRandomFrom(NPC.payOneGold);
                  }
                  else if(selected.isShopkeep())
                  {
                     typed = Utilities.getRandomFrom(NPC.payGold);
                  }
                  else if(selected.getCharIndex()==NPC.WISE)
                  {
                     typed = Utilities.getRandomFrom(NPC.giveTenGold);
                  }
                  else
                  {
                     typed = Utilities.getRandomFrom(NPC.askGold);//"gold";
                  }
               }
               else if(typed.toLowerCase().equals("h") || typed.toLowerCase().equals("home"))
                  typed = Utilities.getRandomFrom(NPC.askHome);//"home";
               else if(typed.toLowerCase().equals("j") || typed.toLowerCase().equals("job"))
                  typed = Utilities.getRandomFrom(NPC.askJob);//"job";
               else if(typed.toLowerCase().equals("l") || typed.toLowerCase().equals("lair") || typed.toLowerCase().equals("lairs"))
                  typed = Utilities.getRandomFrom(NPC.askLair);//"lair";
               else if(typed.toLowerCase().equals("beast") || typed.toLowerCase().equals("beasts") || typed.toLowerCase().equals("monster") || typed.toLowerCase().equals("monsters"))
                  typed = Utilities.getRandomFrom(NPC.askBeast);    
               else if(typed.toLowerCase().equals("m"))
               {
                  if(selected.getName().toLowerCase().contains("arenamaster") &&  selected.getNumInfo()>0)
                     typed = Utilities.getRandomFrom(NPC.askArenaMission);//"mission";
                  else if(selected.hasItem("message"))
                     typed = Utilities.getRandomFrom(NPC.askMessage);//"message";
                  else if((selected.getCharIndex()==NPC.MAN || selected.getCharIndex()==NPC.WOMAN) && selected.getNumInfo()!=10 && !selected.isEscortedNpc())
                     typed = Utilities.getRandomFrom(NPC.askMap);//"map";
                  else   //npc has a mission for us
                  {
                     int missionPriority = selected.getMissionPriority();
                     if(missionPriority == 1)
                     {
                        typed = Utilities.getRandomFrom(NPC.askMissionInProgress);
                     }
                     else
                     {
                        typed = Utilities.getRandomFrom(NPC.askMission);//"mission";
                     }
                  }
               }
               else if(typed.toLowerCase().equals("n") || typed.toLowerCase().equals("name"))
                  typed = Utilities.getRandomFrom(NPC.askName);
               else if(typed.toLowerCase().equals("p"))
                  typed = "pass";   
               else if(typed.toLowerCase().equals("r") || typed.toLowerCase().equals("ration") || typed.toLowerCase().equals("rations"))
               {
                  if(selected.getCharIndex()==NPC.JESTER && selected.getNumInfo()==10)
                  {} ////jester has a riddle quest in which the answer is "r"
                  else if(selected.swinePlayer())
                  {
                     typed = "roll";
                  }
                  else
                  {
                     if(selected.getName().toLowerCase().contains("butcher") || selected.getName().toLowerCase().contains("baker"))
                     {
                        typed = Utilities.getRandomFrom(NPC.buyRations);
                     }
                     else if (selected.getCharIndex()==NPC.BEGGER)
                     {
                        typed = Utilities.getRandomFrom(NPC.giveRations);
                     }
                     else if (selected.isOurDog() || (selected.getCharIndex()==NPC.DOG && !selected.hasBeenAttacked()))
                     {
                        typed = (Utilities.getRandomFrom(NPC.treatDog).replace("DOG_NAME", "pup"));//"treat";
                     }
                     else
                        typed = Utilities.getRandomFrom(NPC.askRations);//"ration";
                  }
               }      
               else if(typed.toLowerCase().equals("s") || typed.toLowerCase().equals("spell") || typed.toLowerCase().equals("magic"))
                  typed = Utilities.getRandomFrom(NPC.askSpell);//"spell";    
               else if(typed.toLowerCase().equals("t"))
               {
                  if(selected.isShopkeep())
                     typed = Utilities.getRandomFrom(NPC.askTrade);//"trade";    
                  else
                     typed = Utilities.getRandomFrom(NPC.askTrain);//"train";
               }
               else if(typed.toLowerCase().equals("v") || typed.toLowerCase().equals("vampire") || typed.toLowerCase().equals("vampyre"))
                  typed = Utilities.getRandomFrom(NPC.askVampire);//"vampire";
               else if(typed.toLowerCase().equals("w") || typed.toLowerCase().equals("weather"))
                  typed = Utilities.getRandomFrom(NPC.askWeather);//"weather";
               else if(typed.toLowerCase().equals("y"))
                  typed = "yes";
            }
            if(typed.length() >= 2)
               sendMessage("~<You>:"+typed.substring(0,1).toUpperCase()+typed.substring(1).toLowerCase()); 
            else
               sendMessage("~"+typed);
            String response = NPC.talk(selected, typed);
            sendMessage("~"+response);
            sendMessage("-----");
            lastTyped = typed;
            typed = "";
         //make any follow-up word in response go to the front of the conversation word list
            conversationIndex = (byte)(conversation.length - 1);
            commandIndex = (byte)(command.length - 1);
            String [] words = response.split(" ");
            int xAdd = 0;
            for(int j=0; j<words.length; j++)
            {
               String tempWord = new String(words[j]);
               if(tempWord.startsWith("~"))
                  tempWord = tempWord.substring(1);
               if(Utilities.isFollowUpWord(tempWord))
               {
                  if(j==0) 
                     tempWord = tempWord.charAt(0) + tempWord.substring(1).toLowerCase();
                  else
                     tempWord = tempWord.toLowerCase();
                  conversation[(byte)(conversation.length -1)] = Utilities.clean(tempWord);   //make the followup word the last word in the conversation list 
                  conversationIndex = (byte)(conversation.length -2);
               }
               tempWord += " ";
            }
         
         }
         else if(((k>=KeyEvent.VK_A && k<=KeyEvent.VK_Z) || (k>=KeyEvent.VK_0 && k<=KeyEvent.VK_9) || k==KeyEvent.VK_SPACE) && typed.length()<24)
            typed += (char)(k);
         else if(k==KeyEvent.VK_BACK_SPACE && typed.length() > 0) 
            typed = typed.substring(0, typed.length()-1);
         else if(lastTyped.length()>0 && k==KeyEvent.VK_UP)
            typed = lastTyped; 
         else if(k==KeyEvent.VK_RIGHT || k==KeyEvent.VK_LEFT)  //scroll through conversation array
         {
            if(selected.isOurDog() || (selected.getCharIndex()==NPC.DOG && !selected.hasBeenAttacked()))
            {
               if(commandIndex < 0 || commandIndex >= command.length)
                  commandIndex = 0;
               if(k==KeyEvent.VK_RIGHT)
               {
                  commandIndex = (byte)((commandIndex + 1) % command.length);
               }
               else
               {
                  commandIndex--;
                  if(commandIndex < 0)
                     commandIndex = (byte)(command.length -1);
               }
               typed = command[commandIndex];
            }
            else
            {
               if(conversationIndex < 0 || conversationIndex >= conversation.length)
                  conversationIndex = 0;
               if(k==KeyEvent.VK_RIGHT)
               {
                  conversationIndex = (byte)((conversationIndex + 1) % conversation.length);
               }
               else
               {
                  conversationIndex--;
                  if(conversationIndex < 0)
                     conversationIndex = (byte)(conversation.length -1);
               }
               typed = conversation[conversationIndex];
            }
         }      
         repaint();
         return;
      }
      lastTyped = "";
   
      if(k==KeyEvent.VK_ESCAPE)					                     //Go to main menu
      {
         Sound.silence(3);
         clearMessage();
         if(menuMode == JOURNAL || menuMode == MAP)
         { 
            if(player.getBody() > 0)
            {
               menuMode = TRAVEL;
               if(!indoors)
                  Sound.rain(weather);
               sendMessage(Utilities.locationInfo());     
            }
            else
            {
               menuMode = MAIN;
               Sound.startMainSong();
            }
         }
         else if(menuMode == MAIN || menuMode == SHOPPE_ARMORY || menuMode == SHOPPE_MAGIC || menuMode == SHOPPE_TRADER || menuMode == WARDROBE || menuMode == INVENTORY  || menuMode == HOTKEYS || menuMode == README)
         {
            if(menuMode == WARDROBE)
            {
               FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
               FileManager.writeOutPlayerToFile("data/player.txt");
            }
            menuMode = TRAVEL;
            if(!indoors)
               Sound.rain(weather); 
            sendMessage(Utilities.locationInfo());       
         }
         else
         {
            if(player.getBody() <=0)
            {
               menuMode = JOURNAL;
               journalChapterIndex = Journal.STATS;
            }
            else
            {
               if(talkSel || attackSel || attackSelAlt || examineSel || stealSel || breakSel || fireSel || pushSel || renovateSel || buildSel)
               {
                  clearSelected();
                  selected = null;
                  selectedBarrel = null;
                  selectedTerrain = null;
                  selectedRenovate = null;
                  selectedBuild = null;
               }
               else
               {
                  clearSelected();
                  selected = null;
                  selectedBarrel = null;
                  selectedTerrain = null;
                  selectedRenovate = null;
                  selectedBuild = null;
                  menuMode = MAIN;
                  Sound.startMainSong();
               }
            }
         }
         repaint();
         return;
      }
      if(k==KeyEvent.VK_J)														//go to or exit Journal
      {
         clearMessage();
         if(menuMode == JOURNAL)
         { 
            if(player.getBody() > 0)
            {
               menuMode = TRAVEL;
               if(!indoors)
                  Sound.rain(weather); 
               sendMessage(Utilities.locationInfo());     
            }
            else
            {
               menuMode = MAIN;
               Sound.silence(3);
               Sound.startMainSong();
            }
         }
         else
         {
            if(menuMode == WARDROBE)
            {
               FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
               FileManager.writeOutPlayerToFile("data/player.txt");
            }
            int beforeComplete =  Player.stats[Player.JOURNAL_COMPLETED];
            journal = Journal.buildJournal();
            int afterComplete =  Player.stats[Player.JOURNAL_COMPLETED];
            if((beforeComplete < 100 && afterComplete == 100) || (afterComplete==100 && achievements[Achievement.DEAR_DIARY]==0))
            {
               //sendMessage("Journal completed!");
               Achievement.earnAchievement(Achievement.DEAR_DIARY);
               int diff = player.expNeededForNextLevel() - player.getExperience();
               if(diff > 0)
                  player.addExperience(diff);
            }
            sendMessage(Journal.getChapterDescription(journalChapterIndex));
            menuMode = JOURNAL; 
            Sound.silence(3);
         }
         repaint();
         return;
      }
      if(k==KeyEvent.VK_I)                                        //go to inventory
      {
         player.setOnGuard(false);
         if(menuMode == INVENTORY || menuMode == WARDROBE)
         {
            if(menuMode == WARDROBE)
            {
               FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
               FileManager.writeOutPlayerToFile("data/player.txt");
            }
            clearMessage();
            menuMode = TRAVEL;
            if(!indoors)
               Sound.rain(weather);
            sendMessage(Utilities.locationInfo());     
         }
         else
         {
            if(currTerrain.contains("purple_floor_inside"))
               menuMode = WARDROBE;
            else
               menuMode = INVENTORY;
            invIndex = 0;
            itemIndex = 0;
            Sound.silence(3);
         }
         repaint();
         return;
      }
      if(k==KeyEvent.VK_M)                                         //go to map
      {
         player.setOnGuard(false);
         if(menuMode == MAP)
         {
            clearMessage();
            menuMode = TRAVEL;
            if(!indoors)
               Sound.rain(weather);
            sendMessage(Utilities.locationInfo());     
         }
         else
         {
            if(menuMode == WARDROBE)
            {
               FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
               FileManager.writeOutPlayerToFile("data/player.txt");
            }
            invIndex = 0;              //for scrolling missions
            menuMode = MAP;
            mapR = player.getRow();    //start center of our map scroll at our player's position
            mapC = player.getCol();
            Sound.silence(3);
         }
         repaint();
         return;
      }
      if(menuMode != TRAVEL && menuMode != SHOPPE_ARMORY && menuMode != SHOPPE_MAGIC && menuMode != SHOPPE_TRADER && menuMode != WARDROBE && menuMode != INVENTORY  && menuMode != HOTKEYS && k==KeyEvent.VK_ESCAPE)             
      {                                //go to travel
         clearMessage();
         Sound.silence(3);
         menuMode = TRAVEL;
         if(!indoors)
            Sound.rain(weather); 
         sendMessage(Utilities.locationInfo());     
         repaint();
         return;
      }
      if(menuMode == MAIN || menuMode == JOURNAL || menuMode == README || menuMode == CHARACTER_BUILDER)
      {
         if(k==KeyEvent.VK_Q)					                              //Save progress and end the program	
         {
            if(player.isCamping())
            {
               player.setCamping(false);
            }
            if(player.isOnGuard())
            {
               player.setOnGuard(false);
            }
            player.clearActiveSpells();  
            Utilities.clearWolfStatus(); 
            if(player.getMapIndex()==1 && player.isInSpecialRealm())
            {} //don't save in temp locatiton
            else
               FileManager.saveProgress(); 
            System.exit(1);
         }
         else if(k==KeyEvent.VK_S && player.getBody()>0)					                        //Save progress	
         {
            if(player.getMapIndex()==1 && player.isInSpecialRealm())      //temporary location, don't save
            {
               sendMessage("You can not save in this location.");
            }
            else
            {
               if(player.isCamping())
               {
                  player.setCamping(false);
               }
               if(player.isOnGuard())
               {
                  player.setOnGuard(false);
               }
               player.clearActiveSpells(); 
               Utilities.clearWolfStatus(); 
               FileManager.saveProgress(); 
               ArrayList<String>imageNames = player.getFileNames();
               boolean onHorse = false;
               for(String s:imageNames)
                  if(s.contains("horse") || s.contains("unicorn"))
                     onHorse = true;    
               if(onHorse  && (!player.onHorse() || (player.getImageIndex()!=Player.HORSE && player.getImageIndex()!=Player.UNICORN)))
               {
                  player.setImageIndex(Player.NONE);
                  player.setWeaponSelect((byte)(0));
               }
               if((onHorse  || player.onHorse() || player.getImageIndex()==Player.HORSE || player.getImageIndex()==Player.UNICORN) && player.getMapIndex()!=0)
               {
                  player.setImageIndex(Player.NONE);
                  player.setWeaponSelect((byte)(0));
               }
               if(!onHorse && player.onHorse())
               {
                  if(player.onUnicorn())
                     player.setImageIndexTemp(Player.UNICORN);
                  else
                     player.setImageIndexTemp(Player.HORSE);
               }
               sendMessage("Progress remembered.");
            }
         }
         else if(k==KeyEvent.VK_L && player.getBody() <=0)              //Load last save
         {
            restartGame();
         }
         else if(k==KeyEvent.VK_B)                                      //Build new map
         {
            player.clearMapMark();
            player.clearTreasureMaps();
            if(player.isSailing())
            {
               player.setImageIndex(player.getLastWeaponIndex());
               player.setWeaponSelect(player.getLastWeaponSelect());
            }
            eggsToHarvest = new ArrayList<RestoreItem>();
            barrelsToRestore = new ArrayList<RestoreItem>();
            File imageFile = new File("maps/world.bin");
            imageFile.delete();
            for(String fileName:mapFiles)                               //delete all location files
            {
               imageFile = new File(fileName);
               imageFile.delete();
            }
            mapFiles = new ArrayList<String>();
            wardrobeInventory = new ArrayList<Item>();
            wardrobeFreq = new ArrayList<Integer>();
            
            imageFile = new File("maps/mapFileNames.txt");
            imageFile.delete();
            
            imageFile = new File("maps/mapFileNames.txt");
            imageFile.delete();
         
            imageFile = new File("maps/worldLocs.txt");
            imageFile.delete();
            
            imageFile = new File("data/wardrobe.txt");
            imageFile.delete();
         
         
            if(player.isVampire())
               time = 20;
            else 
               time = 9; 
            days = 1;
            player.setMapIndex(0);
            startAtCity();
            FileManager.writeOutPlayerToFile("data/player.txt");
            FileManager.writeOutMapFileNames(mapFiles, "maps/mapFileNames.txt");
            FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
            restartGame();
            ArrayList<String>imageNames = player.getFileNames();
            boolean onHorse = false;
            for(String s:imageNames)
               if(s.contains("horse") || s.contains("unicorn"))
                  onHorse = true;    
            if(onHorse  && (!player.onHorse() || (player.getImageIndex()!=Player.HORSE && player.getImageIndex()!=Player.UNICORN)))
            {
               player.setImageIndex(Player.NONE);
               player.setWeaponSelect((byte)(0));
            }
            if((onHorse  || player.onHorse() || player.getImageIndex()==Player.HORSE || player.getImageIndex()==Player.UNICORN) && player.getMapIndex()!=0)
            {
               player.setImageIndex(Player.NONE);
               player.setWeaponSelect((byte)(0));
            }
            if(!onHorse && player.onHorse())
            {
               if(player.onUnicorn())
                  player.setImageIndexTemp(Player.UNICORN);
               else
                  player.setImageIndexTemp(Player.HORSE);
            }
         }
         else if(k==KeyEvent.VK_C) 
         {
            if(menuMode == CHARACTER_BUILDER)
            {
               player = new Player(CBname, CBimageIndex, CBmight, CBmind, CBagility, CBawareness, CBaffliction, CBspell1, CBspell2);
               if(player.isVampire())
                  time = 20;
               else 
                  time = 9; 
               days = 1;
               if(Display.isWinter())     //players that start in winter get a random animal pelt
                  player.addArmorAndSwitch(Armor.getRandomPelt());
               groundColor = Display.findGroundColor(true);
            
               player.setMapIndex(0);
               Utilities.clearNPCMemory();
               startAtCity();
               FileManager.writeOutPlayerToFile("data/player.txt");
               FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
               wardrobeInventory = new ArrayList<Item>();
               wardrobeFreq = new ArrayList<Integer>();
               File imageFile = new File("data/wardrobe.txt");
               imageFile.delete();
               FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
               restartGame();
               menuMode = JOURNAL;
            }
            else
            {
               menuMode = CHARACTER_BUILDER;
               CBPoints = CB_MAX_POINTS;
            }
         }
         else if((menuMode == MAIN || menuMode == README) && k==KeyEvent.VK_R)
         { 
            if(menuMode == README)
            {
               menuMode = MAIN;
               Sound.startMainSong();
            }
            else
            {
               menuMode = README;
               journalChapterIndex = 0;
            }
            Sound.silence(3);
         }
         else if(menuMode == MAIN && k==KeyEvent.VK_T)
         {
            soundOn = !soundOn;
            if(soundOn && !soundInitialized)
            {
               Sound.initialize();
            }
            Sound.silence(3);
         }
         if(menuMode == JOURNAL)		//arrow keys to change chapter and page
         {
            if(k==KeyEvent.VK_LEFT && journalChapterIndex > 0)
            {
               journalChapterIndex--;
               clearMessage();
               sendMessage(Journal.getChapterDescription(journalChapterIndex));
               journalPageIndex = 0;
            }
            else if(k==KeyEvent.VK_RIGHT && journalChapterIndex < Journal.NUM_CHAPTERS-1)
            {
               journalChapterIndex++;
               clearMessage();
               sendMessage(Journal.getChapterDescription(journalChapterIndex));
               journalPageIndex = 0;
            }
            else if(k==KeyEvent.VK_DOWN)
            {
               journalPageIndex++;	//caping maximum page index depending on the number of pages in the journal's chapter done in Journal.showJournal()
               if(journalChapterIndex == Journal.WEAPONS)
               {
                  if(journalPageIndex > Player.NONE && journalPageIndex < Player.NUM_WEAPONS)
                  {
                     clearMessage();
                     sendMessage(Weapon.getTypeDescription(journalPageIndex));
                  }
                  else
                  {
                     clearMessage();
                     sendMessage(Journal.getChapterDescription(journalChapterIndex));
                  }
               }
            }
            else if(k==KeyEvent.VK_UP)
            {
               journalPageIndex--;
               if(journalPageIndex < 0)
                  journalPageIndex = 0;
               if(journalChapterIndex == Journal.WEAPONS)
               {
                  if(journalPageIndex > Player.NONE && journalPageIndex < Player.NUM_WEAPONS)
                  {
                     clearMessage();
                     sendMessage(Weapon.getTypeDescription(journalPageIndex));
                  }
                  else
                  {
                     clearMessage();
                     sendMessage(Journal.getChapterDescription(journalChapterIndex));
                  }
               }
            }
            else if(k==KeyEvent.VK_R && journalChapterIndex == Journal.ACHIEVEMENTS)
            {  //reset achievements
               achievements = new byte[Achievement.NUM_ACHIEVEMENTS];
               Achievement.writeOutAchievements("data/achievements.txt");
               journal = Journal.buildJournal();
               journalChapterIndex = Journal.ACHIEVEMENTS;
            }
         }
         if(menuMode == CHARACTER_BUILDER)		//arrow keys to change attribute selection and value
         {
            if(k==KeyEvent.VK_LEFT)
            {
               CharacterBuilder.scrollLeft();
            }
            else if(k==KeyEvent.VK_RIGHT)
            {
               CharacterBuilder.scrollRight();
            }
            else if(k==KeyEvent.VK_DOWN)
            {
               selAttribute++;
               if(selAttribute >= CharacterBuilder.NUM_CHOICES)	
                  selAttribute = CharacterBuilder.NUM_CHOICES-1;
            }
            else if(k==KeyEvent.VK_UP)
            {
               selAttribute--;
               if(selAttribute < 0)
                  selAttribute = 0;
            }
            else if(k==KeyEvent.VK_BRACELEFT || k==KeyEvent.VK_OPEN_BRACKET)
            {
               CharacterBuilder.scrollLeftSpell1();
            }
            else if(k==KeyEvent.VK_BRACERIGHT || k==KeyEvent.VK_CLOSE_BRACKET)   
            {
               CharacterBuilder.scrollRightSpell1();
            }
            else if(k==KeyEvent.VK_COMMA || k==KeyEvent.VK_LESS)
            {
               CharacterBuilder.scrollLeftSpell2();
            }
            else if(k==KeyEvent.VK_PERIOD || k==KeyEvent.VK_GREATER)   
            {
               CharacterBuilder.scrollRightSpell2();
            }
         }
         else if(menuMode == README)		//arrow keys to change chapter and page
         {
            if(k==KeyEvent.VK_LEFT && journalChapterIndex > 0)
            {
               journalChapterIndex--;
            }
            else if(k==KeyEvent.VK_RIGHT && journalChapterIndex < Display.readMePages.length-1)
            {
               journalChapterIndex++;
            }
         }
         repaint();
         return;
      }
      if((menuMode == INVENTORY || menuMode == HOTKEYS || menuMode == SHOPPE_ARMORY || menuMode == SHOPPE_MAGIC || menuMode == SHOPPE_TRADER || menuMode == WARDROBE || menuMode == TRAVEL) && (k>=KeyEvent.VK_F1 && k<=KeyEvent.VK_F8) && !isWolf)
      {
         boolean sailing = player.isSailing();
         boolean flight = (player.getActiveSpell("Flight")!=null);
         int fIndex = 0;   //f(k==KeyEvent.VK_F1 || k==KeyEvent.VK_F5)
         if(k==KeyEvent.VK_F2 || k==KeyEvent.VK_F6 || k==KeyEvent.VK_F10)
            fIndex = 1;
         else if(k==KeyEvent.VK_F3 || k==KeyEvent.VK_F7 || k==KeyEvent.VK_F11)
            fIndex = 2;
         else if(k==KeyEvent.VK_F4 || k==KeyEvent.VK_F8 || k==KeyEvent.VK_F12)
            fIndex = 3;   
         if(k>=KeyEvent.VK_F1 && k<=KeyEvent.VK_F4)
         {
            if(sailing || flight || isWolf)
            {/*no changing weapons while sailing or flying*/}
            else if(shiftPressed)
               player.weaponHotKeys[fIndex] = new Coord(player.getImageIndex(), player.getWeaponSelect());
            else
            {
               if(player.onHorse())      //dismount horse changing weapons
                  player.dismountHorse();
               if(player.getActiveSpell("Fear")!=null)
                  player.removeSpell("Fear");
               byte weaponType = (byte)(player.weaponHotKeys[fIndex].getRow());
               byte weaponSelect = (byte)(player.weaponHotKeys[fIndex].getCol());
               ArrayList<Weapon>[] weapons = player.getWeapons();
               if(weaponType >=0 && weaponType < weapons.length && weapons[weaponType].size() > 0 && weaponSelect >= 0 && weaponSelect < weapons[weaponType].size())
               {
                  player.setImageIndex(weaponType);
                  player.setWeaponSelect(weaponSelect);
               }
               else		//we no longer have the weapon we set a hot-key for, so clear the hot-key
               {
                  player.weaponHotKeys[fIndex] = new Coord();
               }
            }
         }
         else //if(k>=KeyEvent.VK_F5 && k<=KeyEvent.VK_F8)			//spell cast hot-keys
         {
            if(shiftPressed)
               player.spellHotKeys[fIndex] = player.getSpellSelect();
            else if(System.currentTimeMillis() > spellTime + MILLIS_BETWEEN_SPELLS)
            {
               byte spellSelect = (byte)(player.spellHotKeys[fIndex]);
               ArrayList<Spell> spells = player.getSpells();
               if(spellSelect >=0 && spellSelect < spells.size())
               {
                  if(player.getManna() >= (spells.get(spellSelect)).getMannaCost())
                  {
                     if(player.onHorse() && (spells.get(spellSelect)).getName().contains("Timestop"))      
                        player.dismountHorse();
                     Spell spl = player.castSpell(spellSelect);
                     spellTime =  System.currentTimeMillis();
                     if(spl != null && !spl.getName().contains("Disarm") && Spell.getSpellCast(spl.getName()).startsWith("Klaatu")) //couldn't find a spell chant
                        sendMessage(spl.getName()+" cast!");
                     if(spl != null && spl.getName().contains("Disarm"))
                     {
                        String lockOrTrap = Utilities.nextToALockOrTrap(currMap, player.getRow(), player.getCol());
                        if(!lockOrTrap.equals("none"))
                        {
                           double total = ((player.getMind() + (player.getMind()/2)))/100.0;
                           boolean success = false;
                           if(Math.random() < total)
                              success = Utilities.pickALock(currMap, player.getRow(), player.getCol()); 
                           if(success)
                           {
                              Sound.pickLock();
                           
                              if(lockOrTrap.equals("trap"))
                              {
                                 sendMessage("Disarmed!");
                                 player.addExperience(1);
                              }
                              else
                              {
                                 sendMessage("Success!");
                                 player.addExperience(1);
                              }
                              player.setManna(player.getManna() - player.getSpell().getMannaCost());
                           }
                           else
                           {
                              if(lockOrTrap.equals("trap") && Math.random() < 0.5)
                              {
                                 Utilities.setOffTrap();
                                 Utilities.pickALock(currMap, player.getRow(), player.getCol());
                              }
                              else
                                 sendMessage("Failed.");
                           }
                        }  
                     } 
                  }
                  else
                     sendMessage("Not enough manna.");
               }
               else
                  player.spellHotKeys[fIndex] = 0;
            }
         }
      }
      if((menuMode == INVENTORY || menuMode == HOTKEYS || menuMode == SHOPPE_ARMORY || menuMode == SHOPPE_MAGIC || menuMode == SHOPPE_TRADER || menuMode == WARDROBE || menuMode == TRAVEL) && (k>=KeyEvent.VK_0 && k<=KeyEvent.VK_9) && !isWolf)
      {  //either assign a potion hot-key or use the hot-key to take a potion
         boolean flight = (player.getActiveSpell("Flight")!=null);
         if(!flight)
         {
            byte numKey = (byte)(k - KeyEvent.VK_0);  //the numeric value of the key hit
            if(shiftPressed)  //assign a potion hot-key
            {
               for(int i=0; i<player.potionHotKeys.length; i++)
                  if(player.potionHotKeys[i] == numKey)
                     player.potionHotKeys[i] = -1;
               player.potionHotKeys[player.getPotionSelect()] = numKey;
            }
            else if(menuMode != HOTKEYS)            //use a potion at this hot-key if we have one
            {
            //cycle thru potion selects to see which has the same value as the key pressed - 
            //whichever index it is found is the potion to drink if we have it
               boolean potionFound = false;
               for(byte i=0; i<player.potionHotKeys.length && i<player.getPotions().length; i++)
               {
                  if(player.potionHotKeys[i] == numKey && player.getPotions()[i] > 0)
                  {
                     potionFound = true;
                     player.setPotionSelect(i);
                     player.drinkPotion();
                     break;
                  }
               }
               if(!potionFound)
                  sendMessage("No potion to take!");
            }
         }
      }
      if(menuMode == INVENTORY || menuMode == WARDROBE ||  menuMode == HOTKEYS)
      {
         boolean sailing = player.isSailing();
         boolean flight = false;
         if(player.getActiveSpell("Flight")!=null)
            flight = true;
      
         if(player.getBody() <= 0)
         {
            if(menuMode == WARDROBE || menuMode == HOTKEYS)
               menuMode = INVENTORY;
            repaint();
            return;
         }
         if(k==KeyEvent.VK_ESCAPE)        //Return to travel
         {
            if(menuMode == WARDROBE)
            {
               FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
               FileManager.writeOutPlayerToFile("data/player.txt");
            }
            clearMessage();
            menuMode = TRAVEL;
            sendMessage(Utilities.locationInfo());     
            repaint();
            return;
         }
         if((menuMode == WARDROBE) && (invIndex < 0 || invIndex >= wardrobeInventory.size()))
            invIndex = 0;
         if((menuMode == INVENTORY || menuMode == WARDROBE) && (itemIndex < 0 || itemIndex >= player.getItems().size()))
            itemIndex = 0;
      
         if(k==KeyEvent.VK_K)             //switch to hot-keys menu
         {
            if(menuMode == INVENTORY || menuMode == WARDROBE)
            {
               if(menuMode == WARDROBE)
               {
                  FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
                  FileManager.writeOutPlayerToFile("data/player.txt");
               }
               menuMode = HOTKEYS;
            }
            else
            {
               if(currTerrain.contains("purple_floor_inside"))
                  menuMode = WARDROBE;
               else
                  menuMode = INVENTORY;
            }
         }
         if((menuMode == INVENTORY || menuMode == WARDROBE) && !isWolf && k==KeyEvent.VK_PAGE_DOWN)
         {
            Sound.scrollItem();
            player.scrollArmorUp();
            clearMessage();
            if(player.getArmor()!=null)
               sendMessage(player.getArmor().getDescription());
            if(player.onHorse())
               player.dismountHorse();
            repaint();
            return;
         }
         if(k==KeyEvent.VK_PAGE_UP  && !isWolf && player.numPotions() > 0)
         {
            Sound.scrollItem();
            player.scrollPotion();
            clearMessage();
            if(player.getPotion()!=null)
               sendMessage(player.getPotion().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_INSERT && !flight && !sailing && !isWolf && player.numWeapons() > 0)
         {
            if(player.onHorse())      //dismount horse changing weapons
               player.dismountHorse();
            else
            {
               player.scrollWeaponLeft();
               Sound.scrollItem();
               Weapon current = player.getWeapon();
               clearMessage();
               if(current!=null && current.getDescription().length() > 0)
                  sendMessage(current.getDescription());
            }
            repaint();
            return;
         }
         if(k==KeyEvent.VK_DELETE && !flight && !sailing  && !isWolf && player.numWeapons() > 0)
         {
            if(player.onHorse())      //dismount horse changing weapons
               player.dismountHorse();
            else
            {
               player.scrollWeaponRight();
               Sound.scrollItem();
               Weapon current = player.getWeapon();
               clearMessage();
               if(current!=null && current.getDescription().length() > 0)
                  sendMessage(current.getDescription());
            }
            repaint();
            return;
         }
         if(menuMode == WARDROBE)
         {
            String itemName = "";
            if(player.getItems().size() > 0 && itemIndex >=0 && itemIndex<player.getItems().size())
               itemName = player.getItems().get(itemIndex);
            String weapName = "";
            if(player.getWeapon()!=null)
               weapName = player.getWeapon().getName();
            String armName = "";
            if(player.getArmor()!=null)
               armName = player.getArmor().getName();
            if(k==KeyEvent.VK_U && Item.canCombine(weapName,itemName))           //upgrade weapon
            {
               Item.upgradeWeapon();
               Sound.addPotion();
               repaint();
               return;
            }
            else if(k==KeyEvent.VK_F && itemName.contains("lumber") && (CultimaPanel.player.getNumArrows() < CultimaPanel.player.getMaxArrowCapacity())) 
            {                                                                    //make arrows
               player.removeItem("lumber");
               player.setNumArrows(player.getNumArrows()+25);
               Sound.reloadTime();
               repaint();
               return;
            }
            else if(k==KeyEvent.VK_C && Item.canCraftArmor(armName,itemName))    //upgrade armor
            {
               Item.upgradeArmor();
               Sound.addPotion();
               repaint();
               return;
            }
            else if(k==KeyEvent.VK_B && Item.weaponIsDisassembleable(weapName))    //break gem out of weapon
            {
               Item.disassembleWeapon();
               Sound.addArmor();
               repaint();
               return;
            }
            if(k==KeyEvent.VK_UP && invIndex > 0)                             //scroll wardrobe items up
            {
               invIndex--;
               Sound.scrollShoppeItem();
               if(invIndex >= 0 && invIndex < wardrobeInventory.size())
               {
                  Item currItem = wardrobeInventory.get(invIndex);
                  clearMessage();
                  if(currItem != null)
                  {
                     sendMessage(currItem.getDescription());
                  }
               }
               repaint();
               return;
            }
            if(k==KeyEvent.VK_DOWN && invIndex < wardrobeInventory.size()-1)  //scroll wardrobe items down
            {
               invIndex++;
               Sound.scrollShoppeItem();
               if(invIndex >= 0 && invIndex < wardrobeInventory.size())
               {
                  Item currItem = wardrobeInventory.get(invIndex);
                  clearMessage();
                  if(currItem != null)
                  {
                     sendMessage(currItem.getDescription());
                  }
               }
               repaint();
               return;
            }
            //taking weapons/armor/potions
            if(k==KeyEvent.VK_B || k==KeyEvent.VK_ENTER)                    //bring item if you can carry it
            {
               if(invIndex >= 0 && invIndex < wardrobeInventory.size())
               {
                  Item currItem = wardrobeInventory.get(invIndex);
                  if(currItem != null)
                  {
                     boolean canPlayerCarry = true;
                     if(currItem instanceof Weapon)
                        canPlayerCarry = player.canAddWeapon((Weapon)(currItem));
                     else if(currItem instanceof Armor)
                     {
                        armName = currItem.getName().toLowerCase();
                        if(player.hasItem("holdall") && player.numArmor() > 10 && (!armName.contains("fur") && !armName.contains("skin") && !armName.contains("pelt")  && !armName.contains("hide")))
                           canPlayerCarry = false;
                        if(!player.hasItem("holdall") && player.numArmor() > 2 && (!armName.contains("fur") && !armName.contains("skin") && !armName.contains("pelt")  && !armName.contains("hide")))
                           canPlayerCarry = false;
                     }
                     else if(currItem instanceof Potion)
                     {
                        if(player.hasItem("holdall") && player.numPotions() > 20)
                           canPlayerCarry = false;
                        if(!player.hasItem("holdall") && player.numPotions() > 4)
                           canPlayerCarry = false;    
                     }
                     else if(currItem.isBook())
                     {
                        if(player.hasItem(currItem.getName()))
                           canPlayerCarry = false;
                     }
                     if(!canPlayerCarry)
                        sendMessage("You have not the room to carry this item.");
                     else
                     {
                        if(currItem instanceof Armor)
                        {
                           player.addArmor((Armor)(currItem));
                           sendMessage("Armor fitted!");
                        }
                        else if(currItem instanceof Weapon)
                        {
                           player.addWeapon((Weapon)(currItem));
                           sendMessage("Weapon outfitted!");
                        }
                        else if(currItem.isBook())
                        {
                           sendMessage("Grabbed " + currItem.getName());
                           player.addItem(currItem.getName());   
                        }
                        else if(currItem instanceof Potion)
                        {//potion inventory for player
                           player.addPotion((Potion)(currItem));
                           sendMessage("Potion aquired!");
                        }
                        else if(currItem instanceof Item)
                        {
                           player.addItem(currItem.getName());
                           sendMessage("Item aquired!");
                        }
                        //remove the item from wardrobe
                        if(wardrobeFreq.get(invIndex) > 1)
                           wardrobeFreq.set(invIndex, wardrobeFreq.get(invIndex)-1);
                        else
                        {
                           wardrobeInventory.remove(invIndex);
                           wardrobeFreq.remove(invIndex);
                           invIndex = 0;
                        }
                     }
                  }
               }
               repaint();
               return;
            }
         
         }
         if((menuMode == INVENTORY || menuMode == WARDROBE) && k==KeyEvent.VK_L  && !flight && !sailing && !isWolf && player.canDropWeapon(player.getWeapon()))
         {
            Weapon temp = player.getWeapon();
            player.discardCurrentWeapon();
            if(menuMode == WARDROBE)
            {
               //add removing/taking weapons/armor/potions
               int pos = Item.findItem(wardrobeInventory, temp);
               if(pos == -1)  //not found
               {
                  wardrobeInventory.add(temp);
                  wardrobeFreq.add(1);
               }
               else
                  wardrobeFreq.set(pos, wardrobeFreq.get(pos)+1);
               Item.sortItemArrays(wardrobeInventory, wardrobeFreq);
            }
            repaint();
            return;
         }
         if((menuMode == INVENTORY || menuMode == WARDROBE) && k==KeyEvent.VK_R  && !isWolf && player.getArmorSelect()!=0)
         {
            Armor temp = player.getArmor();
            player.discardCurrentArmor();
            if(menuMode == WARDROBE)
            {
             //add removing/taking weapons/armor/potions
               int pos = Item.findItem(wardrobeInventory, temp);
               if(pos == -1)  //not found
               {
                  wardrobeInventory.add(temp);
                  wardrobeFreq.add(1);
               }
               else
                  wardrobeFreq.set(pos, wardrobeFreq.get(pos)+1);
               Item.sortItemArrays(wardrobeInventory, wardrobeFreq);
            }
            repaint();
            return;
         }
         if(k==KeyEvent.VK_HOME && player.getSpells().size() > 0)      //scroll spells left
         {
            Sound.scrollItem();
            player.scrollSpellLeft();
            clearMessage();
            if(player.getSpellInfo()!=null)
               sendMessage(player.getSpellInfo().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_END && player.getSpells().size() > 0)     //scroll spells right
         {
            Sound.scrollItem();
            player.scrollSpellRight();
            clearMessage();
            if(player.getSpellInfo()!=null)
               sendMessage(player.getSpellInfo().getDescription());
            repaint();
            return;
         }
         if(menuMode == INVENTORY && k==KeyEvent.VK_C && !isWolf && player.getSpell()!=null && !player.getSpell().getName().contains("Disarm") && (System.currentTimeMillis() > spellTime + MILLIS_BETWEEN_SPELLS))       
         {                                                              //cast spell - we take care of disarming with the lockpick
            Spell spl = player.castSpell();
            if(Spell.getSpellCast(spl.getName()).startsWith("Klaatu")) //couldn't find a spell chant
               sendMessage(spl.getName()+" cast!");
            spellTime = System.currentTimeMillis();   
            repaint();
            return;
         }
         if((menuMode == INVENTORY || menuMode == WARDROBE) && !isWolf && k==KeyEvent.VK_D && player.getPotion()!=null)       //drink potion, or drop it in your wardrobe
         {
            Potion ptn = player.getPotion();
            if(menuMode == WARDROBE)
            {
            //add removing/taking weapons/armor/potions
               if(ptn!=null)
               {
                  player.discardPotion();
                  int pos = Item.findItem(wardrobeInventory, ptn);
                  if(pos == -1)  //not found
                  {
                     wardrobeInventory.add(ptn);
                     wardrobeFreq.add(1);
                  }
                  else
                     wardrobeFreq.set(pos, wardrobeFreq.get(pos)+1);   
                  Item.sortItemArrays(wardrobeInventory, wardrobeFreq);
               }               
            }
            else
            {
               ptn = player.drinkPotion();
               if(ptn != null)
                  sendMessage(ptn.getName()+" consumed!");
            }
            repaint();
            return;
         }
         if((menuMode == INVENTORY || menuMode == WARDROBE) && k==KeyEvent.VK_COMMA && player.getItems().size() > 0 && itemIndex > 0)
         {
            Sound.scrollItem();
            itemIndex--;
            clearMessage();
            if(itemIndex >= 0)
            {
               Item temp = Item.getItemWithName(player.getItems().get(itemIndex));
               if(temp != null)
                  sendMessage(temp.getDescription());
            }
            repaint();
            return;
         }
         if((menuMode == INVENTORY || menuMode == WARDROBE) && k==KeyEvent.VK_PERIOD && player.getItems().size() > 0 && itemIndex < player.getItems().size()-1)
         {
            Sound.scrollItem();
            itemIndex++;
            clearMessage();
            if(itemIndex < player.getItems().size())
            {
               Item temp = Item.getItemWithName(player.getItems().get(itemIndex));
               if(temp != null)
                  sendMessage(temp.getDescription());
            }
            repaint();
            return;
         }
         if((menuMode == INVENTORY || menuMode == WARDROBE) && k==KeyEvent.VK_T && player.getItems().size() > 0 && itemIndex >= 0 && itemIndex < player.getItems().size())
         {
            Item temp = Item.getItemWithName(player.getItems().get(itemIndex));
            boolean success = player.removeItem(player.getItems().get(itemIndex));
            if(success)
            {
               if(itemIndex >= player.getItems().size())
                  itemIndex--;
               if(menuMode == WARDROBE)
               {
               //add removing/taking weapons/armor/potions
                  int pos = Item.findItem(wardrobeInventory, temp);
                  if(pos == -1)  //not found
                  {
                     wardrobeInventory.add(temp);
                     wardrobeFreq.add(1);
                  }
                  else
                     wardrobeFreq.set(pos, wardrobeFreq.get(pos)+1); 
                  Item.sortItemArrays(wardrobeInventory, wardrobeFreq);  
               }
               else //if(menuMode == INVENTORY)
               {  //if we dropped a mission item, remove the mission that requires that item
                  if(temp.isMissionItem())
                  {
                     String itemName = temp.getName().toLowerCase();
                     for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
                     {
                        Mission m = CultimaPanel.missionStack.get(i);
                        if(m.getTarget()!=null)
                        {
                           Item targ = m.getTarget();
                           if(targ.getName().toLowerCase().contains(itemName))
                           {
                              CultimaPanel.sendMessage(m.getType() + " mission failed");
                              CultimaPanel.player.addReputation(-5);
                              CultimaPanel.missionStack.remove(i);
                           }
                        }
                     }
                  }
               }
            }
            else
               sendMessage("You can not drop this item.");
            repaint();
            return;
         } 
         if((menuMode == INVENTORY || menuMode == WARDROBE) && k==KeyEvent.VK_E && player.getItems().size() > 0 && itemIndex >= 0 && itemIndex < player.getItems().size() && player.getItems().get(itemIndex).contains("egg"))
         {  //eat an egg
            Item temp = Item.getItemWithName(player.getItems().get(itemIndex));
            String eggName = temp.getName();
            String [] adjectives = {"savory", "delicious", "disgusting", "revolting", "tepid"};
            String response = ("You devoured a ADJECTIVE "+eggName).replace("ADJECTIVE", Utilities.getRandomFrom(adjectives));
            if(eggName.toLowerCase().contains("serpent"))
            {
            //50% chance of curing poison, health boost
               if(player.hasEffect("poison") && Math.random() < 0.5)
               {
                  player.removeEffect("poison");
                  response += " and thy poison is cured!";
               }
               player.heal(Utilities.rollDie(1,5));
            }
            else if(eggName.toLowerCase().contains("dragon"))
            {
            //75% chance of increasing strength, health boost
               if(Math.random() < 0.75 && player.getMightRaw() < 50)
               {
                  player.setMight(Math.min(50, CultimaPanel.player.getMightRaw()+Utilities.rollDie(3)));
                  response += " and thy might surges!";
               }
               player.heal(Utilities.rollDie(10,20));
               Achievement.earnAchievement(Achievement.SCRAMBLED_SMAUG);
            }
            else if(eggName.toLowerCase().contains("allosaur"))
            {
               response += " and thee feels quite well!";
               player.heal(Utilities.rollDie(10,20));
            }
            else if(eggName.toLowerCase().contains("wisdom"))
            {
            //learn all things
               if(Player.stats[Player.JOURNAL_COMPLETED] < 100)
                  Journal.learnAllBooks();
               else if(player.getMindRaw() < 50)
                  player.setMind(Math.min(50, CultimaPanel.player.getMindRaw()+Utilities.rollDie(3))); 
               else if(player.getAwarenessRaw() < 4)
                  player.increaseAwareness();    
               response += " and thy knowledge surges!";  
               player.setManna(Math.max(40,player.getMannaMax()));
            }
            sendMessage(response);
            boolean success = player.removeItem(player.getItems().get(itemIndex));
            if(success)
            {
               if(itemIndex >= player.getItems().size())
                  itemIndex--;
            }
            repaint();
            return;
         }
      }
      if(menuMode == SHOPPE_ARMORY || menuMode == SHOPPE_TRADER)
      {
         if(menuMode == SHOPPE_ARMORY)
         {
            if(invIndex < 0 || invIndex >= armoryInventory.size())
            {
               invIndex = 0;
            }
         }
         else //if(menuMode == SHOPPE_TRADER)
         {
            if(invIndex < 0 || invIndex >= traderInventory.size())
            {
               invIndex = 0;
            }
         }
         
         if(k==KeyEvent.VK_ESCAPE)        //Return to travel
         {
            clearMessage();
            menuMode = TRAVEL;
            sendMessage(Utilities.locationInfo());     
            repaint();
            return;
         }
      
         if(k==KeyEvent.VK_PAGE_DOWN)
         {
            Sound.scrollItem();
            player.scrollArmorUp();
            clearMessage();
            if(player.getArmor()!=null)
               sendMessage(player.getArmor().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_PAGE_UP && player.numPotions() > 0)
         {
            Sound.scrollItem();
            player.scrollPotion();
            clearMessage();
            if(player.getPotion()!=null)
               sendMessage(player.getPotion().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_INSERT && player.numWeapons() > 0)
         {
            Sound.scrollItem();
            player.scrollWeaponLeft();
            Weapon current = player.getWeapon();
            clearMessage();
            if(current != null && current.getDescription().length() > 0)
               sendMessage(current.getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_DELETE && player.numWeapons() > 0)
         {
            Sound.scrollItem();
            player.scrollWeaponRight();
            Weapon current = player.getWeapon();
            clearMessage();
            if(current != null && current.getDescription().length() > 0)
               sendMessage(current.getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_HOME && player.getSpells().size() > 0)     //scroll spells left
         {
            Sound.scrollItem();
            player.scrollSpellLeft();
            clearMessage();
            if(player.getSpellInfo()!=null)
               sendMessage(player.getSpellInfo().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_END && player.getSpells().size() > 0)     //scroll spells right
         {
            Sound.scrollItem();
            player.scrollSpellRight();
            clearMessage();
            if(player.getSpellInfo()!=null)
               sendMessage(player.getSpellInfo().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_S && player.canDropWeapon(player.getWeapon()))  //sell current weapon
         {
            if(Weapon.isLegendaryWeapon(player.getWeapon().getName()))
               Achievement.earnAchievement(Achievement.MERCHANT_MAC);                          
            player.addGold(player.getWeapon().getValue()/3);
            player.discardCurrentWeapon();
            repaint();
            return;
         }
         if(k==KeyEvent.VK_T)                                        //trade current armor for gold
         {                             
            if(player.getArmorSelect() > 0)
            {
               player.addGold(player.getArmor().getValue()/3);
               player.discardCurrentArmor();
            }
            repaint();
            return;
         }
         if(k==KeyEvent.VK_UP && invIndex > 0)                        //scroll shoppe items up
         {
            invIndex--;
            Sound.scrollShoppeItem();
            if(menuMode == SHOPPE_ARMORY)
            {
               if(invIndex >= 0 && invIndex < armoryInventory.size())
               {
                  Item currItem = armoryInventory.get(invIndex);
                  clearMessage();
                  if(currItem != null)
                  {
                     sendMessage(currItem.getDescription());
                  }
               }
            }
            else //if(menuMode == SHOPPE_TRADER)
            {
               if(invIndex >= 0 && invIndex < traderInventory.size())
               {
                  Item currItem = traderInventory.get(invIndex);
                  clearMessage();
                  if(currItem != null)
                  {
                     sendMessage(currItem.getDescription());
                  }
               }
            }
            repaint();
            return;
         }
         if(menuMode == SHOPPE_ARMORY && k==KeyEvent.VK_DOWN && invIndex < armoryInventory.size()-1)  //scroll shoppe items down
         {
            invIndex++;
            Sound.scrollShoppeItem();
            if(invIndex >= 0 && invIndex < armoryInventory.size())
            {
               Item currItem = armoryInventory.get(invIndex);
               clearMessage();
               if(currItem != null)
               {
                  sendMessage(currItem.getDescription());
               }
            }
            repaint();
            return;
         }
         if(menuMode == SHOPPE_TRADER && k==KeyEvent.VK_DOWN && invIndex < traderInventory.size()-1)  //scroll shoppe items down
         {
            invIndex++;
            Sound.scrollShoppeItem();
            if(invIndex >= 0 && invIndex < traderInventory.size())
            {
               Item currItem = traderInventory.get(invIndex);
               clearMessage();
               if(currItem != null)
               {
                  sendMessage(currItem.getDescription());
               }
            }
            repaint();
            return;
         }
         if(k==KeyEvent.VK_B || k==KeyEvent.VK_ENTER)                    //buy item for gold if you have enough and can carry it
         {
         //buy item and add to inventory if we can carry it
            Item currItem = null;
            if(menuMode == SHOPPE_ARMORY)
            {
               currItem = armoryInventory.get(invIndex);
            }
            else //if(menuMode == SHOPPE_TRADER)
            {
               currItem = traderInventory.get(invIndex);
            }
            int cost = currItem.getValue();
            boolean holiday = ((CultimaPanel.days % 100) == 50);   
            if(holiday)
               cost /= 2;
            if(shoppeDiscount)
               cost = (int)(cost * 0.8);
            boolean canPlayerCarry = true;
            if(currItem instanceof Weapon)
               canPlayerCarry = player.canAddWeapon((Weapon)(currItem));
            else if(currItem instanceof Armor)
            {
               String armName = currItem.getName().toLowerCase();
               if(player.hasItem("holdall") && player.numArmor() > 10 && (!armName.contains("fur") && !armName.contains("skin") && !armName.contains("pelt")  && !armName.contains("hide")))
                  canPlayerCarry = false;
               if(!player.hasItem("holdall") && player.numArmor() > 2 && (!armName.contains("fur") && !armName.contains("skin") && !armName.contains("pelt")  && !armName.contains("hide")))
                  canPlayerCarry = false;
            }
            else if(currItem.getName().toLowerCase().contains("arrows"))
            {
               if(player.hasItem("holdall") && player.getNumArrows() >= 100)
                  canPlayerCarry = false;
               else if(!player.hasItem("holdall") && player.getNumArrows() >= 50)
                  canPlayerCarry = false;
            }
            if(player.hasMagicStaff() && currItem.getName().equals("Staff")) 
               sendMessage("You are already bound to a staff");
            else if(player.getGold() < currItem.getValue())
               sendMessage("You have not the gold for this item.");
            /*else if(player.hasItem(currItem.getName()))
               sendMessage("You already possess this item.");*/
            else if(currItem.isBook() && Item.doWeHaveBook(currItem))
               sendMessage("You already possess this knowledge."); 
            else if(!canPlayerCarry)
               sendMessage("You have not the room to carry this item.");
            else
            {
               if(currItem instanceof Armor)
               {
                  player.addArmor((Armor)(currItem));
                  sendMessage("New armor aquired!");
                  player.pay(cost);
               }
               else if(currItem instanceof Weapon)
               {
                  player.addWeapon((Weapon)(currItem));
                  sendMessage("New weapon aquired!");
                  player.pay(cost);
               }
               else if(currItem.getName().toLowerCase().contains("arrows"))
               {
                  int maxArrows = 50;
                  if(player.hasItem("holdall"))
                     maxArrows = 100;
                  else if(player.hasItem("swiftquill"))
                     maxArrows = 75; 
                  player.setNumArrows(Math.min(maxArrows, player.getNumArrows()+10));
                  player.pay(cost);
               }
               else if(currItem.isBook())
               {
                  if(Item.doWeHaveBook(currItem))
                     sendMessage("You already have thy " + currItem.getName());
                  else
                  {
                     boolean success = Journal.copyFromBook(currItem);
                     if(success)
                        sendMessage("Purchased " + currItem.getName() + " and copied items into your journal");
                     else
                        sendMessage("Purchased " + currItem.getName());   
                     player.pay(cost);
                  }
               }
               else if(currItem instanceof Item)
               {
                  if(player.hasItem(currItem.getName()))
                     sendMessage("You already possess this item.");
                  else
                  {
                     player.addItem(currItem.getName());
                     sendMessage("New item aquired!");
                     player.pay(cost);
                  }
               }
            }
            repaint();
            return;
         }
      }
      if(menuMode == SHOPPE_MAGIC)
      {
         if(invIndex < 0 || invIndex >= magicInventory.size())
            invIndex = 0;
         if(k==KeyEvent.VK_ESCAPE)        //Return to travel
         {
            clearMessage();
            menuMode = TRAVEL;
            sendMessage(Utilities.locationInfo());     
            repaint();
            return;
         }
         if(k==KeyEvent.VK_PAGE_DOWN )//&& player.numArmor() > 0)
         {
            Sound.scrollItem();
            player.scrollArmorUp();
            clearMessage();
            if(player.getArmor()!=null)
               sendMessage(player.getArmor().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_PAGE_UP && player.numPotions() > 0)
         {
            Sound.scrollItem();
            player.scrollPotion();
            clearMessage();
            if(player.getPotion()!=null)
               sendMessage(player.getPotion().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_INSERT && player.numWeapons() > 0)
         {
            Sound.scrollItem();
            player.scrollWeaponLeft();
            Weapon current = player.getWeapon();
            clearMessage();
            if(current!=null && current.getDescription().length() > 0)
               sendMessage(current.getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_DELETE && player.numWeapons() > 0)
         {
            Sound.scrollItem();
            player.scrollWeaponRight();
            Weapon current = player.getWeapon();
            clearMessage();
            if(current!=null && current.getDescription().length() > 0)
               sendMessage(current.getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_HOME && player.getSpells().size() > 0)      //scroll spells left
         {
            Sound.scrollItem();
            player.scrollSpellLeft();
            clearMessage();
            if(player.getSpellInfo()!=null)
               sendMessage(player.getSpellInfo().getDescription());
            repaint();
            return;
         }
         if(k==KeyEvent.VK_END && player.getSpells().size() > 0)     //scroll spells right
         {
            Sound.scrollItem();
            player.scrollSpellRight();
            clearMessage();
            if(player.getSpellInfo()!=null)
               sendMessage(player.getSpellInfo().getDescription());
            repaint();
            return;
         }
      
         if(k==KeyEvent.VK_UP && invIndex > 0)                          //scroll shoppe items up
         {
            invIndex--;
            Sound.scrollShoppeItem();
            if(invIndex >= 0 && invIndex < magicInventory.size())
            {
               Item currItem = magicInventory.get(invIndex);
               clearMessage();
               if(currItem != null)
               {
                  sendMessage(currItem.getDescription());
               }
            }
            repaint();
            return;
         }
         if(k==KeyEvent.VK_DOWN && invIndex < magicInventory.size()-1)  //scroll shoppe items down
         {
            invIndex++;
            Sound.scrollShoppeItem();
            if(invIndex >= 0 && invIndex < magicInventory.size())
            {
               Item currItem = magicInventory.get(invIndex);
               clearMessage();
               if(currItem != null)
               {
                  sendMessage(currItem.getDescription());
               }
            }
            repaint();
            return;
         }
      
         if(k==KeyEvent.VK_B || k==KeyEvent.VK_ENTER)                   //buy item for gold
         {
         ///buy item and add to inventory if we can carry it
            Item currItem = magicInventory.get(invIndex);
            int cost = currItem.getValue();
            boolean holiday = ((CultimaPanel.days % 100) == 50);   
            if(rainbowAlpha > 0 || holiday)
               cost /= 2;
            else if(shoppeDiscount)
               cost = (int)(cost * 0.8);
            boolean canPlayerCarry = true;
            boolean playerHasStaff = true;
            if(currItem instanceof Potion)
            {
               if(player.hasItem("holdall") && player.numPotions() > 20)
                  canPlayerCarry = false;
               if(!player.hasItem("holdall") && player.numPotions() > 4)
                  canPlayerCarry = false;    
            }
            if(currItem instanceof Weapon && !player.hasMagicStaff() && !currItem.getName().equals("Staff")) 
            {
               playerHasStaff = false;    
            }
            if(player.hasMagicStaff() && currItem.getName().equals("Staff")) 
               sendMessage("You are already bound to a staff");
            else if(player.getGold() < cost)
               sendMessage("You have not the gold for this item.");
            else if((currItem instanceof Spell || currItem instanceof Weapon) && player.hasItem(currItem.getName()))
               sendMessage("You already learned this spell.");
            else if(!(currItem instanceof Potion) && player.hasItem(currItem.getName()) && !currItem.isBook())
               sendMessage("You already possess this item.");
            else if(currItem.isBook() && Item.doWeHaveBook(currItem))
               sendMessage("You already possess this knowledge."); 
            else if(!canPlayerCarry)
               sendMessage("You have not the room to carry this potion.");
            else if(!playerHasStaff)
               sendMessage("You have not a staff to cast this spell.");
            else if(currItem instanceof Spell && player.getMannaMax() < ((Spell)(currItem)).getMannaCost())
               sendMessage("You have not the manna to cast this spell.");
            else
            {
               if(currItem instanceof Armor)
               {
                  player.addArmor((Armor)(currItem));
                  sendMessage("New robe aquired!");
                  player.pay(cost);
               }
               else if(currItem instanceof Potion)
               {//potion inventory for player
                  player.addPotion((Potion)(currItem));
                  sendMessage("New potion aquired!");
                  player.pay(cost);
               }
               else if(currItem instanceof Weapon)
               {
                  player.addWeapon((Weapon)(currItem));
                  sendMessage("New spell learned!");
                  player.pay(cost);
               }
               else if(currItem instanceof Spell)
               {
                  player.addSpell((Spell)(currItem));
                  sendMessage("New spell learned!");
                  player.pay(cost);
               }
               else if(currItem.isBook())
               {
                  if(player.hasItem(currItem.getName()) && player.getSpellIndex() >= NPC.spellInfo.length)
                     sendMessage("You already have thy " + currItem.getName());
                  else
                  {
                     boolean success = Journal.copyFromBook(currItem);
                     if(success)
                        sendMessage("Purchased " + currItem.getName() + " and copied items into your journal");
                     else
                        sendMessage("Purchased " + currItem.getName());
                     player.pay(cost);
                  }
               }
               else if(currItem instanceof Item)
               {
                  player.addItem(currItem.getName());
                  sendMessage("New item aquired!");
                  player.pay(cost);
               }
            }
            repaint();
            return;
         }            
      }
      if(menuMode == TRAVEL)
      {
         movePlayer(k);
      }
      else if(menuMode == MAP)         //directions to scroll through map
      {
         if(k==KeyEvent.VK_C)
         {
            player.clearMapMark();
            repaint();
            return;
         }
         if(k==KeyEvent.VK_L)
         {
            player.setMapMark();
            repaint();
            return;
         }
         if(k==KeyEvent.VK_COMMA && invIndex > 0 && missionStack.size() > 0)
         {
            invIndex--;
            clearMessage();
            int lastIndex = missionStack.size() - invIndex - 1;
            if(lastIndex >=0 && lastIndex < missionStack.size())
            {
               String missInfo = missionStack.get(lastIndex).getStartStory();
               if(!missInfo.equals("none"))
                  sendMessage(missInfo);
               else
                  sendMessage(missionStack.get(lastIndex).getInfo());
            }
            repaint();
            return;
         }
         if(k==KeyEvent.VK_PERIOD && invIndex < missionStack.size()-1 && missionStack.size()>1)
         {
            invIndex++;
            clearMessage();
            int lastIndex = missionStack.size() - invIndex - 1;
            if(lastIndex >=0 && lastIndex < missionStack.size())
            {
               String missInfo = missionStack.get(lastIndex).getStartStory();
               if(!missInfo.equals("none"))
                  sendMessage(missInfo);
               else
                  sendMessage(missionStack.get(lastIndex).getInfo());
            }
            repaint();
            return;
         }
         if(k==KeyEvent.VK_D && missionStack.size() > 0 && (missionStack.size()-1-invIndex) >= 0 && (missionStack.size()-1-invIndex) < missionStack.size())
         {
           //clear out the NPC that gave this mission
            int missionIndex = missionStack.size()-1-invIndex;
            Utilities.removeMission(missionIndex);
            invIndex = 0;
            repaint();
            return;
         }
         if(mapIndex == 0)             //navigating in the world has wrap-around
         {
            if(k==KeyEvent.VK_UP)
            {                
               mapR = equalizeRow(mapR - (Display.SCREEN_SIZE)/2);
            }
            else if(k==KeyEvent.VK_DOWN)
            {
               mapR = equalizeRow(mapR + (Display.SCREEN_SIZE)/2);
            }
            else if(k==KeyEvent.VK_LEFT)
            {
               mapC = equalizeRow(mapC - (Display.SCREEN_SIZE)/2);
            }
            else if(k==KeyEvent.VK_RIGHT)
            {
               mapC = equalizeRow(mapC + (Display.SCREEN_SIZE)/2);
            }
         }
         else                 //navigating in a location - no wrap-around
         {
            if(k==KeyEvent.VK_UP && (mapR - (Display.SCREEN_SIZE)/2) >= 0)
            {                
               mapR = (mapR - (Display.SCREEN_SIZE)/2);
            }
            else if(k==KeyEvent.VK_DOWN && (mapR + (Display.SCREEN_SIZE)/2) < (map.get(player.getMapIndex())).length)
            {
               mapR = (mapR + (Display.SCREEN_SIZE)/2);
            }
            else if(k==KeyEvent.VK_LEFT && (mapC - (Display.SCREEN_SIZE)/2) >= 0)
            {
               mapC = (mapC - (Display.SCREEN_SIZE)/2);
            }
            else if(k==KeyEvent.VK_RIGHT && (mapC + (Display.SCREEN_SIZE)/2) < (map.get(player.getMapIndex()))[0].length)
            {
               mapC = (mapC + (Display.SCREEN_SIZE)/2);
            }
         }
      }      
      repaint();			//refresh the screen
   }

   public void paintComponent(Graphics g)
   {
      super.paintComponent(g); 
      Display.showAllGraphics(g);					//draw the contents of the array board on the screen
   }

   private class Listener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)	//this is called for each timer iteration - advance animation frames
      {
         boolean timestop = (player.getActiveSpell("Timestop")!=null);
         isNight = (time >=20 || time <= 6);
         if(menuMode == TRAVEL)
         {
            numFrames++;
            Sound.checkToClearSound();
            if(numFrames == Integer.MAX_VALUE)
            {
               numFrames = 0;
               player.setPainTime(0);  
               player.setAttackTime(0);
            }  
            Utilities.getAdjacentNPCs();
            Utilities.findNPCsInSight();
            if(player.getBody() <= 0)
            {
               if(player.hasItem("life-pearl"))    //player can be revived
               {
                  player.runEffects();
               }
            }
            if(CultimaPanel.rainbowAlpha > 0 && !TerrainBuilder.indoors())
            {
               if(CultimaPanel.rainbowAlpha < 90 && CultimaPanel.numFrames < CultimaPanel.rainbowFrame)
                  CultimaPanel.rainbowAlpha++;
               else if(CultimaPanel.numFrames >= CultimaPanel.rainbowFrame)
                  CultimaPanel.rainbowAlpha--;
            }
            else
               CultimaPanel.rainbowFrame = 0;
            if(CultimaPanel.fogAlpha > 0)
            {
               if(CultimaPanel.fogAlpha < 255 && (CultimaPanel.numFrames < CultimaPanel.fogFrame || player.magicMistActive()))
                  CultimaPanel.fogAlpha++;
               else if(CultimaPanel.numFrames >= CultimaPanel.fogFrame && !player.magicMistActive())
                  CultimaPanel.fogAlpha--;
            }
            else
               CultimaPanel.fogFrame = 0;   
            String locType = player.getLocationType().toLowerCase();
            int r = player.getRow();
            int c = player.getCol();
            byte[][]currMap = (map.get(player.getMapIndex()));   
            byte value = currMap[r][c];
            Terrain currentPos = allTerrain.get(Math.abs(value));
            String currPosName = currentPos.getName().toLowerCase();
            if(!timestop)
            {
               Sound.runAmbientSounds();
               if(player.onHorse())
               {
                  if(gallopTime >= 0 && System.currentTimeMillis() > gallopTime + 100)
                  {
                     Sound.horseGallop(1);
                     gallopTime = -1;
                     gallopTime2 = System.currentTimeMillis();
                  }
               }
               Utilities.moveNPCs(false);  
               Utilities.moveMonsters(false);   
            }
            if(player.isCamping())    
            {
               Utilities.dogOnWatch();
               Utilities.demonVisit();    //see if a Demon visits us while sleeping to play Swine
               Utilities.jesterThief();   //see if a theif comes in the night to steal something
               if(player.isVampire() && time > 16)
               {
                  player.setCamping(false);
                  if(currPosName.contains("coffin"))
                     sendMessage("You awake after a comfortable rest.");
                  else
                     sendMessage("You awake to the falling night.");
               }
               else if(!player.isVampire() && time < 20 && time > 6)                   //wake up
               {
                  player.setCamping(false);
                  if(currPosName.contains("bed"))
                     sendMessage("You awake after a comfortable rest.");
                  else
                     sendMessage("The campfire draws down.");
                  if(player.getMapIndex()!=0 && (locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("port")))
                     Utilities.putNPCsOnStreet(player.getMapIndex());
                  else if(locType.contains("arena"))
                     Utilities.setupArena(player.getMapIndex());  
               }
               else
               {
                  double oldTime = time;
                  time += WORLD_TIME_INCREMENT/2;
                  Utilities.checkWerewolfStatus(); 
                  Utilities.checkRainbow();
                  if((int)(oldTime) != (int)(time))      //an hour has passed
                  {
                     Utilities.changeWeather();
                     int prob = Utilities.rollDie(1,10);
                     if((weather > 5 || prob < weather) && !currPosName.contains("bed") && !TerrainBuilder.indoors() && !Display.isWinter())
                     {
                        player.setCamping(false);
                        sendMessage("The rain has extinguished thy campfire!");
                     }
                     //chance someone sets your house on fire with high bounty or a monster
                     if((player.getBounty() >= 100 || player.isVampire() || player.isWerewolf()) && currPosName.contains("coffin") && TerrainBuilder.habitablePlace(player.getLocationType()))
                     {                     
                        int roll = Utilities.rollDie(100);
                        if(roll < 5)
                        {
                           sendMessage("A torch is thrown into thy house!");
                           Utilities.startRandomArsonFire();
                        }
                     }                 
                  }
                  darkened = Display.darkenedCells(time);    
                  if(!player.isVampire() && currPosName.contains("coffin") && Math.random()<0.01)
                  {
                     Utilities.changeToVampire();
                     player.setCamping(false);
                  }
                  if(time >= 24)
                  {
                     double leftOver = time - Math.floor(time);
                     time = (Math.floor(time) % 24) + leftOver;
                     rain_probability = Math.random()/2;
                     days++;
                     Mission.checkMissionTimeOut();
                     if(days % 2 == 0)
                     {  //every 2 days the traveling trader gets new items
                        traderInventory = Utilities.loadTraderShoppe();
                     }
                     if(days >= 60)
                        Achievement.earnAchievement(Achievement.ONE_ROUND_DOWN);   
                     if(player.getRations() > 0)
                     {
                        player.useRation();
                        if(!player.getDogName().equals("none") && player.getRations() > 0 && days%2==0)
                           player.useRation();
                        if(!player.getNpcName().equals("none") && player.getRations() > 0 && days%2==0)
                           player.useRation();
                     }
                     else
                     {
                        player.damage(10);   
                        sendMessage("Weakened from hunger.");
                     }
                  }
               }
            } 
            else if((shiftPressed || (player.isOnIce() && !player.isSailing())) && (numFrames % animDelay == 0) && !locType.contains("pacrealm"))
            {  //we don't want to be able to sprint in pacrealm
               byte lastDir =  player.getLastDir();
               int k = KeyEvent.VK_UP;
               if(lastDir == 1)
                  k = KeyEvent.VK_RIGHT;
               else if(lastDir == 2)
                  k = KeyEvent.VK_DOWN;
               else if(lastDir == 3)
                  k = KeyEvent.VK_LEFT;
               movePlayer(k);
               if(!timestop)
               { 
                  Utilities.moveMonsters(true); //bonus move for monsters with higher agility  
                  Utilities.moveNPCs(true);
               }
            }
            boolean forceLightning = (getDeathAbout!=null || locType.contains("1942"));
            if(!timestop)
            {
            //maybe do thunder if there is a storm
               if(Display.canHaveThunderstorms() && (numFrames % (animDelay*4) == 0) && ((Utilities.rollDie(1,20) < weather) || (forceLightning && Utilities.rollDie(1,20) < 10)))
               {
                  if(!forceLightning && (locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("sewer")))
                  {     //indoor locations - don't flash lightning
                     Sound.thunder();
                  }
                  else if(locType.contains("1942"))
                  {    
                     Sound.explosion();
                  }
                  else
                  {
                     Sound.thunder();
                     flashColor = Color.white;
                     flashFrame = CultimaPanel.numFrames;
                     flashRadius = CultimaPanel.SCREEN_SIZE;
                     double fireProb = 0.01;
                     if(Math.random() < fireProb)         //lightning ignites a random fire on the player screen
                        Utilities.startRandomFire();
                  }
               }
               if(numFrames % ((int)((animDelay*2) - ((animDelay*2)*((player.getMind()/10.0)/100)))) == 0 )
               { 
               //recover manna over time - faster for higher level players
                  if(player.getManna() < player.getMannaMax())
                     player.addManna(1);
               }
               if(numFrames % (animDelay*2) == 0)
               {
                  if(player.hasItem("mannastone") && player.getManna() < player.getMannaMax() && Math.random() < 0.5)
                     player.addManna(1);
                  if(rainbowAlpha > 0 && player.getManna() < player.getMannaMax())    //manna restores faster with a rainbow out
                     player.addManna(Utilities.rollDie(2,10));  
                  if(player.getWeapon().getName().equalsIgnoreCase("Bright-horn") && player.getManna() < player.getMannaMax())    //manna restores faster when weilding unicorn horn
                     player.addManna(Utilities.rollDie(1,5));
                  else if(player.isWieldingLegendaryStaff() && player.getManna() < player.getMannaMax())    //manna restores faster when weilding legendary staff
                     player.addManna(Utilities.rollDie(1,5));         
                  for(int i = civilians.get(player.getMapIndex()).size()-1; i>=0; i--)
                  {
                     NPCPlayer npc = civilians.get(player.getMapIndex()).get(i);
                     npc.runEffects();
                     if(npc.getBody() <= 0)
                     {
                        CultimaPanel.corpses.get(player.getMapIndex()).add(npc.getCorpse());
                        civilians.get(player.getMapIndex()).remove(i);
                     }
                  }
                  ArrayList<NPCPlayer>fires = new ArrayList<NPCPlayer>();   
                  for(int i=worldMonsters.size()-1; i>=0; i--)
                  {
                     NPCPlayer npc = worldMonsters.get(i);
                     npc.runEffects();
                     //Utilities.checkDistanceToEscortee(npc);
                     //chance a vampire on fire teleports away
                     if(NPC.isVampire(npc) && npc.hasEffect("fire") && Math.random() < 0.5)
                     {
                        worldMonsters.remove(i);
                        sendMessage("Vampire teleports away!");
                        Sound.thunder();
                        flashColor = Color.red;
                        flashFrame = CultimaPanel.numFrames;
                        flashRadius = CultimaPanel.SCREEN_SIZE;
                     }
                     if(npc.getBody() <= 0)
                     {
                        CultimaPanel.corpses.get(player.getMapIndex()).add(npc.getCorpse());
                        worldMonsters.remove(i);
                     }
                  }
                  for(int i=boats.size()-1; i>=0; i--)
                  {
                     NPCPlayer npc = boats.get(i);
                     npc.runEffects();
                     if(npc.getBody() <= 0)
                     {
                        CultimaPanel.corpses.get(player.getMapIndex()).add(npc.getCorpse());
                        boats.remove(i);
                     }
                  }
                  Utilities.spreadFires();
                  Utilities.spawnLavaFire();
               
               //push the player in the direction of the rapid water's current
                  boolean flight = (player.getActiveSpell("Flight")!=null);
                  if(currPosName.contains("rapid") && !flight)     
                  {
                     boolean pushed = false;
                     byte dir = windDir;
                     if(dir==LocationBuilder.NORTH)
                     {
                        String upTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(r-1)][c])).getName();
                        if(upTerrain.contains("water"))
                        {
                           pushed = true;
                           r--;
                        }
                     }
                     else if(dir==LocationBuilder.SOUTH)
                     {
                        String downTerrain = allTerrain.get(Math.abs(currMap[equalizeRow(r+1)][c])).getName();
                        if(downTerrain.contains("water"))
                        {
                           pushed = true;
                           r++;
                        }
                     }
                     else if(dir==LocationBuilder.WEST)
                     {
                        String leftTerrain = allTerrain.get(Math.abs(currMap[r][equalizeCol(c-1)])).getName();
                        if(leftTerrain.contains("water"))
                        {
                           pushed = true;
                           c--;
                        }
                     }
                     else if(dir==LocationBuilder.EAST)
                     {
                        String rightTerrain = allTerrain.get(Math.abs(currMap[r][equalizeCol(c+1)])).getName();
                        if(rightTerrain.contains("water"))
                        {
                           pushed = true;
                           c++;
                        }
                     }
                     if(pushed)
                     {
                        player.setRow(equalizeRow(r));
                        player.setCol(equalizeRow(c)); 
                        player.setWorldRow(equalizeRow(r));
                        player.setWorldCol(equalizeRow(c));
                     }
                  } 
               }
            }
         }
         Sound.playSong();
         repaint();
      }
   }
}
