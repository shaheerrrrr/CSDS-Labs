import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Color;
import javax.swing.ImageIcon;

public class Player extends AnimatedEntity
{
   //primary image file names for different character types (which weapon we have) and states (sailing, horseback, camping, etc) used when our guard is up and ready to attack/defend
   //dimension 1 designates which weapon type or image we use
   //dimension 2 designates which way we are facing, default RIGHT_IMAGE (0), and for some weapons there is a LEFT_IMAGE(1)
   //dimension 3 designates animation frames where the last element is the dead image
   public static final byte RIGHT_IMAGE = 0;
   public static final byte LEFT_IMAGE = 1;
   public static final String [][][] characters = 
   {  {{"images/characters/player/unarmed1.Gif", "images/characters/player/unarmed2.Gif", "images/characters/player/unarmed1.Gif", "images/characters/player/unarmed2.Gif","images/characters/player/dead.Gif"},              {}},
      {{"images/characters/player/torch1.Gif", "images/characters/player/torch2.Gif", "images/characters/player/torch1.Gif", "images/characters/player/torch2.Gif","images/characters/player/dead.Gif"},                      {}}, 
      {{"images/characters/player/staff1.Gif", "images/characters/player/staff2.Gif", "images/characters/player/staff1.Gif", "images/characters/player/staff2.Gif","images/characters/player/dead.Gif"},                      {"images/characters/player/staff1Left.Gif", "images/characters/player/staff2Left.Gif", "images/characters/player/staff1Left.Gif", "images/characters/player/staff2Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/player/longstaff1.Gif", "images/characters/player/longstaff2.Gif", "images/characters/player/longstaff1.Gif", "images/characters/player/longstaff2.Gif","images/characters/player/dead.Gif"},      {"images/characters/player/longstaff1Left.Gif", "images/characters/player/longstaff2Left.Gif", "images/characters/player/longstaff1Left.Gif", "images/characters/player/longstaff2Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/player/spear1.Gif", "images/characters/player/spear2.Gif", "images/characters/player/spear1.Gif", "images/characters/player/spear2.Gif","images/characters/player/dead.Gif"},                      {"images/characters/player/spear1Left.Gif", "images/characters/player/spear2Left.Gif", "images/characters/player/spear1Left.Gif", "images/characters/player/spear2Left.Gif","images/characters/player/dead.Gif"}},  
      {{"images/characters/player/bow1.Gif", "images/characters/player/bow2.Gif", "images/characters/player/bow1.Gif", "images/characters/player/bow2.Gif","images/characters/player/dead.Gif"},                              {"images/characters/player/bow1Left.Gif", "images/characters/player/bow2Left.Gif", "images/characters/player/bow1Left.Gif", "images/characters/player/bow2Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/player/xbow1.Gif", "images/characters/player/xbow2.Gif", "images/characters/player/xbow1.Gif", "images/characters/player/xbow2.Gif","images/characters/player/dead.Gif"},                          {"images/characters/player/xbow1Left.Gif", "images/characters/player/xbow2Left.Gif", "images/characters/player/xbow1Left.Gif", "images/characters/player/xbow2Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/player/axe1.Gif", "images/characters/player/axe2.Gif", "images/characters/player/axe1.Gif", "images/characters/player/axe2.Gif","images/characters/player/dead.Gif"},                              {"images/characters/player/axe1Left.Gif", "images/characters/player/axe2Left.Gif", "images/characters/player/axe1Left.Gif", "images/characters/player/axe2Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/player/dualaxe1.Gif", "images/characters/player/dualaxe2.Gif", "images/characters/player/dualaxe1.Gif", "images/characters/player/dualaxe2.Gif","images/characters/player/dead.Gif"},              {}},
      {{"images/characters/player/hammer1.Gif", "images/characters/player/hammer2.Gif", "images/characters/player/hammer1.Gif", "images/characters/player/hammer2.Gif","images/characters/player/dead.Gif"},                  {"images/characters/player/hammer1Left.Gif", "images/characters/player/hammer2Left.Gif", "images/characters/player/hammer1Left.Gif", "images/characters/player/hammer2Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/player/dagger1.Gif", "images/characters/player/dagger2.Gif", "images/characters/player/dagger1.Gif", "images/characters/player/dagger2.Gif","images/characters/player/dead.Gif"},                  {"images/characters/player/dagger1Left.Gif", "images/characters/player/dagger2Left.Gif", "images/characters/player/dagger1Left.Gif", "images/characters/player/dagger2Left.Gif","images/characters/player/dead.Gif"}},   
      {{"images/characters/player/saber1.Gif", "images/characters/player/saber2.Gif", "images/characters/player/saber3.Gif", "images/characters/player/saber4.Gif","images/characters/player/dead.Gif"},                      {"images/characters/player/saber1Left.Gif", "images/characters/player/saber2Left.Gif", "images/characters/player/saber3Left.Gif", "images/characters/player/saber4Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/player/swordShield1.Gif", "images/characters/player/swordShield2.Gif", "images/characters/player/swordShield1.Gif", "images/characters/player/swordShield2.Gif","images/characters/player/dead.Gif"}, {"images/characters/player/swordShield1Left.Gif", "images/characters/player/swordShield2Left.Gif", "images/characters/player/swordShield1Left.Gif", "images/characters/player/swordShield2Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/player/dual1.Gif", "images/characters/player/dual2.Gif", "images/characters/player/dual1.Gif", "images/characters/player/dual2.Gif","images/characters/player/dead.Gif"},                          {}},
      {{"images/characters/player/thrown1.Gif", "images/characters/player/thrown2.Gif", "images/characters/player/thrown1.Gif", "images/characters/player/thrown2.Gif","images/characters/player/dead.Gif"},                  {}},
      {{"images/characters/player/lute1.Gif", "images/characters/player/lute2.Gif", "images/characters/player/lute1.Gif", "images/characters/player/lute2.Gif","images/characters/player/dead.Gif"},                          {"images/characters/player/lute1Left.Gif", "images/characters/player/lute2Left.Gif", "images/characters/player/lute1Left.Gif", "images/characters/player/lute2Left.Gif","images/characters/player/dead.Gif"}},
      {{"images/characters/animals/bat1.Gif", "images/characters/animals/bat2.Gif", "images/characters/animals/bat3.Gif", "images/characters/animals/bat4.Gif", "images/characters/monsters/deadClawed.Gif"},                 {}},
      {{"images/characters/monsters/werewolf1.Gif", "images/characters/monsters/werewolf2.Gif", "images/characters/monsters/werewolf3.Gif", "images/characters/monsters/werewolf4.Gif", "images/characters/monsters/deadClawed.Gif"}, {}},
      {{"images/characters/player/camp1.Gif", "images/characters/player/camp2.Gif", "images/characters/player/camp3.Gif", "images/characters/player/camp4.Gif", "images/characters/player/dead.Gif"},                         {}},
      {{"images/characters/player/flight1.Gif", "images/characters/player/flight2.Gif", "images/characters/player/flight3.Gif", "images/characters/player/flight4.Gif", "images/characters/player/dead.Gif"},                 {}},
      {{"images/characters/player/camp0.Gif", "images/characters/player/dead.Gif"},                                                                                                                                           {}},
      {{"images/characters/player/coffin.Gif", "images/characters/player/dead.Gif"},                                                                                                                                          {}},
      {{"images/characters/ships/shipUp.Gif", "images/characters/ships/shipRight.Gif", "images/characters/ships/shipDown.Gif", "images/characters/ships/shipLeft.Gif", "images/characters/monsters/deadWater.Gif"},           {}},
      {{"images/characters/ships/pshipUp.Gif", "images/characters/ships/pshipRight.Gif", "images/characters/ships/pshipDown.Gif", "images/characters/ships/pshipLeft.Gif", "images/characters/monsters/deadWater.Gif"},       {}},
      {{"images/characters/ships/boatUp.Gif", "images/characters/ships/boatRight.Gif", "images/characters/ships/boatDown.Gif", "images/characters/ships/boatLeft.Gif", "images/characters/monsters/deadWater.Gif"},           {}},
      {{"images/characters/player/horseUp.Gif", "images/characters/player/horseRight.Gif", "images/characters/player/horseDown.Gif", "images/characters/player/horseLeft.Gif", "images/characters/monsters/deadGame.Gif"},    {}},
      {{"images/characters/player/unicornUp.Gif", "images/characters/player/unicornRight.Gif", "images/characters/player/unicornDown.Gif", "images/characters/player/unicornLeft.Gif", "images/characters/monsters/deadGame.Gif"}, {}},
      //fear spell illusions level 1
      {{"images/characters/monsters/bugbear1.Gif", "images/characters/monsters/bugbear2.Gif", "images/characters/monsters/bugbear3.Gif", "images/characters/monsters/bugbear4.Gif", "images/characters/player/dead.Gif"},     {}},
      {{"images/characters/monsters/ghost1.Gif", "images/characters/monsters/ghost2.Gif", "images/characters/monsters/ghost3.Gif", "images/characters/monsters/ghost4.Gif", "images/characters/player/dead.Gif"},             {}},
      //fear spell illusions level 2
      {{"images/characters/monsters/troll1.Gif", "images/characters/monsters/troll2.Gif", "images/characters/monsters/troll3.Gif", "images/characters/monsters/troll4.Gif", "images/characters/player/dead.Gif"},             {}},
      {{"images/characters/monsters/cyclops1.Gif", "images/characters/monsters/cyclops2.Gif", "images/characters/monsters/cyclops3.Gif", "images/characters/monsters/cyclops4.Gif", "images/characters/player/dead.Gif"},     {}},
      //fear spell illusions level 3
      {{"images/characters/monsters/demon1.Gif", "images/characters/monsters/demon2.Gif", "images/characters/monsters/demon3.Gif", "images/characters/monsters/demon4.Gif", "images/characters/player/dead.Gif"},             {}},
      {{"images/characters/monsters/dragonDown1.Gif", "images/characters/monsters/dragonDown2.Gif", "images/characters/monsters/dragonDown3.Gif", "images/characters/monsters/dragonDown4.Gif", "images/characters/player/dead.Gif"}, {}},
      //image for hiding in tall grass
      {{"images/characters/player/hide.Gif", "images/characters/player/dead.Gif"},                                                                                                                                            {}}
      };
    
    //secondary image file names for different character types (which weapon we have) used when our guard is down  
   public static final String [][] guardDown = 
   {  {"images/characters/player/unarmed1a.Gif", "images/characters/player/unarmed2a.Gif", "images/characters/player/unarmed1a.Gif", "images/characters/player/unarmed2a.Gif","images/characters/player/dead.Gif"},
      {"images/characters/player/torch1a.Gif", "images/characters/player/torch2a.Gif", "images/characters/player/torch1a.Gif", "images/characters/player/torch2a.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/staff1b.Gif", "images/characters/player/staff2b.Gif", "images/characters/player/staff1b.Gif", "images/characters/player/staff2b.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/longstaff1b.Gif", "images/characters/player/longstaff2b.Gif", "images/characters/player/longstaff1b.Gif", "images/characters/player/longstaff2b.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/spear1b.Gif", "images/characters/player/spear2b.Gif", "images/characters/player/spear1b.Gif", "images/characters/player/spear2b.Gif","images/characters/player/dead.Gif"},   
      {"images/characters/player/bow1b.Gif", "images/characters/player/bow2b.Gif", "images/characters/player/bow1b.Gif", "images/characters/player/bow2b.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/xbow1b.Gif", "images/characters/player/xbow2b.Gif", "images/characters/player/xbow1b.Gif", "images/characters/player/xbow2b.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/axe1b.Gif", "images/characters/player/axe2b.Gif", "images/characters/player/axe1b.Gif", "images/characters/player/axe2b.Gif","images/characters/player/dead.Gif"},
      {"images/characters/player/dualaxe1b.Gif", "images/characters/player/dualaxe2b.Gif", "images/characters/player/dualaxe1b.Gif", "images/characters/player/dualaxe2b.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/hammer1b.Gif", "images/characters/player/hammer2b.Gif", "images/characters/player/hammer1b.Gif", "images/characters/player/hammer2b.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/dagger1b.Gif", "images/characters/player/dagger2b.Gif", "images/characters/player/dagger1b.Gif", "images/characters/player/dagger2b.Gif","images/characters/player/dead.Gif"},    
      {"images/characters/player/saber1b.Gif", "images/characters/player/saber2b.Gif", "images/characters/player/saber1b.Gif", "images/characters/player/saber2b.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/swordShield1b.Gif", "images/characters/player/swordShield2b.Gif", "images/characters/player/swordShield1b.Gif", "images/characters/player/swordShield2b.Gif","images/characters/player/dead.Gif"},
      {"images/characters/player/dual1b.Gif", "images/characters/player/dual2b.Gif", "images/characters/player/dual1b.Gif", "images/characters/player/dual2b.Gif","images/characters/player/dead.Gif"}, 
      {"images/characters/player/thrown1a.Gif", "images/characters/player/thrown2a.Gif", "images/characters/player/thrown1a.Gif", "images/characters/player/thrown2a.Gif","images/characters/player/dead.Gif"},
      {"images/characters/player/lute1b.Gif", "images/characters/player/lute2b.Gif", "images/characters/player/lute1b.Gif", "images/characters/player/lute2b.Gif","images/characters/player/dead.Gif"},
      {},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}};     //needs to be the same # rows as the characters array

   //portraits for the inventory page
   private static final ImageIcon [] portraits = 
   {  new ImageIcon("images/characters2/player/unarmed1.Gif"),
      new ImageIcon("images/characters2/player/torch1.Gif"), 
      new ImageIcon("images/characters2/player/staff1.Gif"), 
      new ImageIcon("images/characters2/player/magic1.Gif"), 
      new ImageIcon("images/characters2/player/spear1.Gif"), 
      new ImageIcon("images/characters2/player/bow1.Gif"),
      new ImageIcon("images/characters2/player/xbow1.Gif"),  
      new ImageIcon("images/characters2/player/axe1.Gif"),
      new ImageIcon("images/characters2/player/dualaxe1.Gif"),
      new ImageIcon("images/characters2/player/hammer1.Gif"), 
      new ImageIcon("images/characters2/player/dagger1.Gif"), 
      new ImageIcon("images/characters2/player/saber1.Gif"), 
      new ImageIcon("images/characters2/player/swordShield1.Gif"),
      new ImageIcon("images/characters2/player/dual1.Gif"),
      new ImageIcon("images/characters2/player/throwable1.Gif"), 
      new ImageIcon("images/characters2/player/lute1.Gif"),
      new ImageIcon("images/characters2/player/dead.Gif"),
      new ImageIcon("images/characters2/player/king1.Gif")
      };

   protected static final byte NONE = 0;           //weapon categories NONE to LUTE
   protected static final byte TORCH = 1;          //cast light, start fires, wave around to intimidate
   protected static final byte STAFF = 2;          //cast offensive magic spells, range 2 melee
   protected static final byte LONGSTAFF = 3;      //range 2 melee
   protected static final byte SPEAR = 4;          //range 2 melee, can be thrown
   protected static final byte BOW = 5;            //range attack, long
   protected static final byte CROSSBOW = 6;       //range attack, medium
   protected static final byte AXE = 7;            //melee, can chop down wood doors
   protected static final byte DUALAXE = 8;        //melee, can spin attack to hit multiple targets
   protected static final byte HAMMER = 9;         //melee, can break down walls, mining
   protected static final byte DAGGER = 10;        //melee, can be thrown
   protected static final byte SABER= 11;          //melee
   protected static final byte SWORDSHIELD = 12;   //melee, shield bash to knock back enemies
   protected static final byte DUAL = 13;          //2 short swords, range attack, can spin to hit multiple targets
   protected static final byte THROWN = 14;        //ranged, thrown rocks, sling or holy hand grenade
   protected static final byte LUTE = 15;          //magic lute, music can calm creatures, move citizens/guards who must dance
   protected static final byte NUM_WEAPONS = 16;
   
   protected static final byte BAT = 16;           //flying when a vampire, also used for DEAD portrait
   protected static final byte WOLF = 17;          //werewolf curse, also used for KING portrait
   protected static final byte CAMP = 18;          //camping with a fire
   protected static final byte FLIGHT = 19;        //for flying spell
   protected static final byte SLEEP = 20;         //sleeping, no fire (in a bed)
   protected static final byte COFFIN = 21;        //sleeping, (in a coffin)
   protected static final byte GREATSHIP = 22;     //for when we sail a great ship
   protected static final byte BRIGANDSHIP = 23;   //for when we sail a briggand skiff
   protected static final byte BOAT = 24;          //for when we are on a row boat
   protected static final byte HORSE = 25;         //for when we ride a horse
   protected static final byte UNICORN = 26;       //for when we ride a unicorn

//fear spell illusions
   protected static final byte BUGBEAR = 27;       //low level fear
   protected static final byte GHOST = 28;
   protected static final byte TROLL = 29;         //medium level fear
   protected static final byte CYCLOPS = 30;
   protected static final byte DEMON = 31;         //high level fear
   protected static final byte DRAGON = 32;
   
   protected static final byte HIDE = 33;          //for ducking behind a hiding spot like tall grass
   protected static final byte NUM_IMAGES = 34;

   protected static final double EXP_COST_PER_LEVEL = 1.10;    //10% increase in # experience points to get to next level

   private int row, col;                  //location for navigation
   private int worldRow, worldCol;        //current location when in the main world (mapIndex 0) to be remembered after we leave a location
   private LinkedList<Teleporter> memory; //stack of locations that we need to remember to go back to when we leave a Location
   private String location;               //what kind of location are we in?
   private int mapIndex;                  //mapIndex of which map the player is in (to be saved/loaded)
   private int [] mapMark;                //row, col, mapIndex of mark on map, used for Teleport spell

   private int awareness;
   public static String[] awarenessNames =  {"dull","natural","keen","sharp","eagle-eye"};

   private int might;
   private int mind;
   private int agility;

   private int body;                //current health
   private int manna;
   private int [] statMod;          //modifiers to stats due to spell
   private int sulliedValue;        //for holding down drinks

   private int experience;          //experience points
   private int expToLevelUp;        //additional experience needed to the next level up
   protected int[] specificExperience;    //individual exp points for might, mind, agility and awareness to steer what you get when you level up
   protected static final int MIGHT=0;    //indicies for specificExperience array
   protected static final int MIND=1;
   protected static final int AGILITY=2;
   protected static final int AWARENESS=3;
   protected static final int MAX_AWARENESS = 4;
   protected static final int MAX_STAT = 50;                        //the maximum value we allow for might,mind, agility or level

   private int reputation;
   public static String[] reputationNames = {"Monster", "Villain", "Scoundrel", "Wanderer", "Adventurer", "Hero", "Legend"};

   private int bounty;              //value to run probability that an assassin appears

   private int gold;

   private ImageIcon portrait;         //picture shown when Inventory is brought up
   private byte imageIndex;            //index of image type for portrait
   private ImageIcon dead;             //graphics for dead player
   private byte startStoryIndex;       //which story in Story.allStories do we start with
   private double hitTime,missTime;    //used to show hit graphic when the player gets damaged
   private int attackTime;             //frame time of last attack: used for reload times for some weapons

   private ArrayList<Weapon> [] weapons;
   private byte weaponSelect;          //index of the selected weapon
   private ArrayList<Integer> [] weaponFrequencies;  //to keep track of the number of weapons of each type we have
   private int numArrows;              //number of arrows in quill
   private int numStones;              //number of stones to throw

   private ArrayList<Armor> armor;
   private byte armorSelect;           //index of selected armor
   private ArrayList<Integer> armorFrequencies; //parallel array of the number of armor items we have

   private byte rations;               //0-15 (1 ration used per day)

   private ArrayList<String> items;    //list of special items owned (lockpick, holdall)

   private int [] potions;  				//list of potions in possession
   private byte potionSelect;          //index of the selected potion

   private ArrayList<Spell> spells;    //list of non-combat spells learned
   private byte spellSelect;           //index of the selected spell
   private ArrayList<Spell> activeSpells;    //collection of spells that are currently active

   private ArrayList<String> effects;  //poison, curse, blessed, etc
   private int painTime;               //frame time to flash the display when we take damage
   private boolean camping;            //are we camping or not
   private double campStartTime;       //to keep track of number of hours of rest
   private boolean hiding;             //are we hiding in tall grass or not

   private byte lastDir;               //last movement direction typed in 0-UP, 1-RIGHT, 2-DOWN, 3-LEFT for drawing ship in proper direcion
   private byte lastWeaponIndex;       //to remember weapon drawn when we board a ship, so we can return to that state when we exit
   private byte lastWeaponSelect;

   public Coord [] weaponHotKeys;      //hot-key assignments for F1-F4 where x:weapon-type and y:weapon-select
   public int [] spellHotKeys;         //hot-key assignments for F5-F8 for casting a spell
   public int [] potionHotKeys;        //hot-key assignments for 0-9 for potions

   public int [] flowerBoxCount;       //how many flowers of each color do we have in a flower box?
   public static final int RED = 0;    //index values for flower colors able to store in a flowerBox
   public static final int YELLOW = 1;
   public static final int BLUE = 2;
   public static final int VIOLET = 3;
   public static final int GREEN = 4;
   public static final int NUM_FLOWERS = 5;
   public static final int FLOWERS_FOR_MANNA = 10;  //number of flowers of each color to boost manna

   private String horseBasicInfo;      //horseName, might, mind, agility, body, charType
   private String dogBasicInfo;        //dogName, might, mind, agility, body, charType
   private String npcBasicInfo;        //npcName, might, mind, agility, body, charType

   private boolean onGuard;				//are we ready to defend ourselves?

   private int deathTime;              //frame number in which we meet our end in case we have a life-pearl to revive us after 5-seconds

   private int [] infoIndexes;         //indes to keep track of the next bit of information on a variety of topics for the journal

   private byte monsterType;           //index of a monster type that swallows us whole
   
   private byte wellNumber;            //data for the underworld puzzle of the 3 wells
   private byte doorNumber,doorChange; //data for the dungeon puzzle of the 3 doors
   
   private byte numKeyTaps;            //count the number of key strikes to escape a web or wake up
   public static final byte MAX_TAPS_TO_ESCAPE = 75;
   public static final byte MAX_TAPS_TO_WAKE = 25;

//***INFO-INDEXES
   public static final byte DOG_INFO = 0;             //index for a list of dog training info            
   public static final byte MONSTERS_INFO = 1;        //index for a list of monster info player aquires  
   public static final byte WEAPONS_INFO = 2;         //index for a list of weapon info player aquires   
   public static final byte SPELLS_INFO = 3;          //index for a list of spell info player aquires    
   public static final byte TOWNSFOLK_INFO = 4;       //index for a list of townsfolk info player aquires
   public static final byte VAMPIRE_INFO = 5;         //index for a list of vampire info                 
   public static final byte HORSE_INFO = 6;           //index for a list of horse training info          
   public static final byte SHIP_INFO = 7;            //index for a list of horse training info          
   public static final byte RIDDLES_INFO = 8;         //index for a list of riddles leaned               
   public static final byte WEREWOLF_INFO = 9;        //index for a list of werewolf info                
   public static final byte MAPPING_INFO = 10;        //index for a list of mapping info
   public static final byte TRAPPING_INFO = 11;       //index for a list of trapping info
   public static final byte TOWERS_PUZZLE_INFO = 12;  //index for instructions for the 3 towers puzzle (towers of hanoi)
   public static final byte DOORS_PUZZLE_INFO = 13;   //index for instructions for the 3 doors puzzle (monty hall's 3 doors)
   public static final byte WELLS_PUZZLE_INFO = 14;   //index for instructions for the 3 wells puzzle
   public static final byte CRAFTING_INFO = 15;       //index for instructions on crafting magic items
   public static final byte NUM_INFO_INDEXES = 16;    //the number of information indexes (and infoIndexes array size)

//***STAT KEEPING
   public static final byte MONSTERS_KILLED = 0;   
   public static final byte DRAGONS_SLAIN = 1;
   public static final byte GIANTS_SLAIN = 2;
   public static final byte SHIPS_SCUTTLED = 3;
   public static final byte SHIPS_COMMANDEERED = 4;
   public static final byte ALPHAMONSTERS_KILLED = 5;
   public static final byte GAME_KILLED = 6;
   public static final byte PELTS_COLLECTED = 7;   
   public static final byte FISH_CAUGHT = 8;
   public static final byte HORSES_TAMED = 9;
   public static final byte MISSIONS_COMPLETED = 10;
   public static final byte PEOPLE_MET = 11;        
   public static final byte PEOPLE_RESCUED = 12;    
   public static final byte PEOPLE_MURDERED = 13;   
   public static final byte LOCKS_PICKED = 14;
   public static final byte GUARDS_BRIBED = 15;
   public static final byte ITEMS_STOLEN = 16;
   public static final byte GOLD_MINED = 17;
   public static final byte MONEY_DONATED = 18;     
   public static final byte MAP_COMPLETED = 19;  
   public static final byte LOCATIONS_FOUND = 20; 
   public static final byte ARENA_LEVEL = 21;
   public static final byte ASSASSINS_KILLED = 22;
   public static final byte BOUNTIES_COLLECTED = 23;
   public static final byte BOUNTIES_FORGIVEN = 24;
   public static final byte ROYALTY_SLAIN = 25;       
   public static final byte JOURNAL_COMPLETED = 26; 
   public static final byte BATTLES_FOUGHT = 27;       
   public static final byte BATTLES_WON = 28;
   public static final byte DOGS_TRAINED = 29; 
   public static final byte HOUSES_BOUGHT = 30;
   public static final byte TIMES_MARRIED = 31;
   public static final byte SWINE_GAMES_PLAYED = 32;
   public static final byte SWINE_GAMES_WON = 33;
   public static final byte PUZZLES_SOLVED = 34;
   public static final byte NUM_STATS = 35;
   public static int [] stats;
   
   //messages to show if we are losing/gaining health by time increments (effects like poisoned, on fire, etc)
   public static final String[] graboid = {"Thee is being digested!", "Thy skin burns!", "Thy very skin stings!", "Seek thee a way out!", "Thy strength drains from thee.", "Pain wracks thy very skin!","Thee must slice thy way out!","Thee is inside the beast!","A blade can slice thy way to freedom!"};
   public static final String[] poison = {"The poison burns!", "Weakened by poison.", "Thy very blood stings!", "Seek thee a cure!", "Thy strength drains from thee.", "Pain coils within!", "Poison runs through thy veins!", "Sickness is upon thee.", "Thy stomach wretches!", "Thy blood boils!"};
   public static final String[] bless = {"You feel a blessing befall you.", "A blessing from the ether is felt.", "A warm feeling conforts thee.", "Thou has felt the touch of a blessing.", "Thou has been blessed by agents of Skara Brae.", "A blessing befalls thee."};
   public static final String[] blessBurn = {"The blessed item burns!", "The blessed item burns thy flesh!", "A burning sensation strikes thee!", "A searing burn strikes thy skin!"};
   public static final String[] fire = {"The fire burns!", "The fire is consuming you!", "Thy very flesh burns!", "Take action to stiffle the flames!", "Seek thee healing or water!", "Flames are searing thy very skin!"};
   public static final String[] lifePearlMessage = {"Thy life-pearl shimmers!", "Thy life-pearl beats as a heart!", "Thy life-pearl pulses with light!", "Thy life-pearl crackles with spark!", "Thy life-pearl shrieks a high noted wail!"};
   public static final String[] sunBurn = {"The sunlight burns!", "The light of the sun is consuming you!", "Thy very flesh burns!", "Take action to shelter from the sunlight!", "Seek thee shade from the sun!"};
   public static final String[] drain = {"Thy cloak drains thy very life!", "The weight of thy cloak harms thee!", "Thy cloak pulls away thy energy!", "Thy life is drained by the weight of thy cloak!"};
   public static final String[] cold = {"Tis cold! Drape thee in animal fur!", "The cold bites thee! Find thee warm furs!", "Thy attire does not protect thee from the cold!", "An animal hide will shield thee from the cold", "Find thee an animal fur for the stinging cold!"};
   public static final String[] coldWater = {"Tis too cold to swim!", "The cold water bites thee! Find thee dry land!", "Thy attire does not protect thee from the cold water!", "Winter tis not the time to swim!", "Find thee dry land!"};
   public static final String[] mineWarning = {"Dust falls from the mine ceiling...", "The very ground shakes in the mine...", "A low rumbing echoes down the mine corridors...", "Rocks and dirt slide off the mine walls onto the ground...", "This mine seems quite unstable!", "The mine supports are shaking and cracking!", "An urget feeling calls you to flee!", "A mine support close to you breaks like a twig!", "Will these mine supports hold?", "This mine is close to collapse!", "The very ceiling cracks above thee!", "A terrible tremor shakes thee about!", "Will this ceiling come down upon thee?"};
   public static final String[] kaijuWarning = {"<MONSTER_DIST> A concusive thud and dust rises from the ground...", "<MONSTER_DIST> The very ground shakes as the it draws near...", "<MONSTER_DIST> A low rumbing stomp as a goliath approaches...", "<MONSTER_DIST> Walls crack at the base with a deafening rumble...", "<MONSTER_DIST> The massive terror approaches the gates!", "<MONSTER_DIST> The towering terror is heading this way!", "<MONSTER_DIST> An urget feeling calls thee to flee!", "<MONSTER_DIST> Screams of terrified countrymen are heard as it approaces the gates!", "<MONSTER_DIST> The very ground shakes with a terrible rumble", "<MONSTER_DIST> This beast is almost at the gates!", "<MONSTER_DIST> The very ground trembles before this massive, chaotic terror!", "<MONSTER_DIST> A terrible tremor shakes thee about!", "<MONSTER_DIST> Tis coming directly towards you, this towering goliath!"};

  //**************

   public Player(String name, ArrayList<String> images, boolean allowAffliction)
   {
      super(name, images);
      specificExperience = new int[4];
      dogBasicInfo = "none";
      horseBasicInfo = "none";
      npcBasicInfo = "none";
      weaponHotKeys = new Coord[4];
      for(int i=0; i<weaponHotKeys.length; i++)
         weaponHotKeys[i] = new Coord();
      spellHotKeys = new int[4];
      potionHotKeys = new int[Potion.NUM_POTIONS];
      for(int i=0; i<potionHotKeys.length; i++)
         potionHotKeys[i] = -1;
      stats = new int[NUM_STATS];
      numArrows = 0;
      numStones = 0;
   
      infoIndexes = new int[NUM_INFO_INDEXES];
      
      lastDir = 0;
      lastWeaponIndex = 0;
      lastWeaponSelect = 0;
      attackTime = 0;
      statMod = new int[3];
      hitTime = -1;
      missTime = -1;
      weapons = new ArrayList[NUM_WEAPONS];
      for(int i=0; i<weapons.length; i++)
         weapons[i] = new ArrayList<Weapon>();
      weaponFrequencies = new ArrayList[NUM_WEAPONS];
      for(int i=0; i<weaponFrequencies.length; i++)
         weaponFrequencies[i] = new ArrayList<Integer>();  
      int[]statM = {0,0,2}; 
      armor = new ArrayList<Armor>();
      armorFrequencies = new ArrayList<Integer>();
      potions = new int[Potion.NUM_POTIONS];
      spells = new ArrayList<Spell>();
      activeSpells = new ArrayList<Spell>();
      flowerBoxCount = new int[NUM_FLOWERS];
   
      row = CultimaPanel.MAP_SIZE / 2; //start in the middle of the world map
      col = CultimaPanel.MAP_SIZE / 2;
      worldRow = CultimaPanel.MAP_SIZE / 2; 
      worldCol = CultimaPanel.MAP_SIZE / 2;
      memory = new LinkedList<Teleporter>();
      location = "world";
      mapIndex = 0;   
      mapMark = new int[3];
      mapMark[0] = -1;
      mapMark[1] = -1;
      mapMark[2] = -1;
               
      int [] randomStats = buildRandomStats();
      might = randomStats[0];
      mind = randomStats[1];
      agility = randomStats[2];
      awareness = randomStats[3];
    
      body = (might*4 + this.getLevelWhole());
      manna = mind*10;
   
      experience = 44;
      for(int i=0; i<this.getLevel(); i++)
         experience *= EXP_COST_PER_LEVEL;
      expToLevelUp = 500;
   
      reputation = 0;
      bounty = 1;
   
      gold =Utilities.rollDie(1,50);
      rations = 5;
      items = new ArrayList<String>();
      effects = new ArrayList<String>();
   
      images = new ArrayList<String>();
      imageIndex = 0;
   
      weapons[NONE].add(Weapon.getFists());
      weaponFrequencies[NONE].add(1);  
      weapons[THROWN].add(Weapon.getStone());
      weaponFrequencies[THROWN].add(1); 
      armor.add(Armor.getClothes());
      armorFrequencies.add(1);
   
      if(Math.random() < 0.1)
      {
         imageIndex = NONE;         //YOU START WITH NOTHING!
         gold = 0;
         awareness = Math.min(MAX_AWARENESS,awareness+1);
         //but you have all info except riddles in your journal as a bonus
         infoIndexes[DOG_INFO] = NPC.dogInfo.length;
         infoIndexes[MONSTERS_INFO] = NPC.monsters.length;
         infoIndexes[WEAPONS_INFO] = NPC.armsInfo.length;
         infoIndexes[SPELLS_INFO] = NPC.spellInfo.length;
         infoIndexes[TOWNSFOLK_INFO] = NPC.tavernTown.length;
         infoIndexes[VAMPIRE_INFO] = NPC.vampireInfo.length;
         infoIndexes[HORSE_INFO] = NPC.horseInfo.length;
         infoIndexes[SHIP_INFO] = NPC.shipInfo.length;
         infoIndexes[WEREWOLF_INFO] = NPC.werewolfInfo.length;
         infoIndexes[MAPPING_INFO] = NPC.mappingInfo.length;
         infoIndexes[TRAPPING_INFO] = NPC.trappingInfo.length;
         infoIndexes[CRAFTING_INFO] = NPC.gemInfo.length;
      }
      else if(Math.abs(might - mind) <= 4 && Math.abs(might - agility) <= 4 && Math.abs(mind - agility) <= 4)
      {                             //Explorer special class
         imageIndex = TORCH;
         weapons[imageIndex].add(Weapon.getTorch());
         awareness = Math.min(MAX_AWARENESS,awareness+1);
         gold =Utilities.rollDie(25, 75);
         weaponFrequencies[imageIndex].add(1);   
         infoIndexes[MAPPING_INFO] = NPC.mappingInfo.length;
         items.add(Item.getFloretBox().getName());
      }
      else if(might>mind && might>agility && agility>mind)     //might, agility, mind
      {  
         int randType =Utilities.rollDie(1,3);
         if(randType == 1)   //Brigand King special class   
         {
            imageIndex = DUAL;
            weapons[imageIndex].add(Weapon.getShortSwords());
            this.addItem(Item.getLoadedCube().getName());
         }
         else if(randType == 2)
         {
            imageIndex = HAMMER;
            weapons[imageIndex].add(Weapon.getHammer());
            this.addItem(Item.getRandomStone().getName());
         }
         else
         {
            imageIndex = AXE;
            weapons[imageIndex].add(Weapon.getAxe());
            infoIndexes[TOWNSFOLK_INFO] = NPC.tavernTown.length;
            infoIndexes[MAPPING_INFO] = NPC.mappingInfo.length;
         }
         weaponFrequencies[imageIndex].add(1);   
      }
      else if(might>=mind && might>=agility && mind>=agility)     //might, mind, agility
      {  
         int randType =Utilities.rollDie(1,2);
         if(randType == 1)   //Royal guard special class   
         {
            imageIndex = SWORDSHIELD;
            weapons[imageIndex].add(Weapon.getSwordBuckler());
            infoIndexes[HORSE_INFO] = NPC.horseInfo.length;
         }
         else
         {
            imageIndex = SABER;
            weapons[imageIndex].add(Weapon.getSword());
            infoIndexes[MONSTERS_INFO] = NPC.monsters.length;
         }
         weaponFrequencies[imageIndex].add(1);   
      }
      else if(agility>=might && agility>=mind && mind>=might)     //agility, mind, might
      {  
         int randType =Utilities.rollDie(1,3);
         if(randType == 1)   //Royal musician special class   
         {
            imageIndex = LUTE;
            weapons[imageIndex].add(Weapon.getLuteOfDestiny());
            gold =Utilities.rollDie(25, 75);
         }
         else if(randType == 2) 
         {     //bow
            imageIndex = BOW;
            weapons[imageIndex].add(Weapon.getBow());
            numArrows = 25;
            infoIndexes[TRAPPING_INFO] = NPC.trappingInfo.length;
         }
         else //if(randType == 3) 
         {     
            imageIndex = CROSSBOW;
            weapons[imageIndex].add(Weapon.getCrossbow());
            numArrows = 25;
            gold =Utilities.rollDie(25, 75);
         }
         weaponFrequencies[imageIndex].add(1);   
      }
      else if(agility>might && agility>mind && might>mind)        //agility, might, mind
      {  
         int randType =Utilities.rollDie(1,3);
         if(randType==1)   //Assassin special class
         {
            imageIndex = DAGGER;
            weapons[imageIndex].add(Weapon.getDagger());
            weaponFrequencies[imageIndex].add(3);
            this.addItem(Item.getLockpick().getName());
         }
         else if(randType==2)
         {
            imageIndex = SPEAR;
            weapons[imageIndex].add(Weapon.getSpear());
            infoIndexes[SHIP_INFO] = NPC.shipInfo.length;
            weaponFrequencies[imageIndex].add(1);
            infoIndexes[SHIP_INFO] = NPC.shipInfo.length;
         }
         else
         {
            imageIndex = DUALAXE;
            weapons[imageIndex].add(Weapon.getDualAxe());
            weaponFrequencies[imageIndex].add(1);
            armor.add(Armor.getLeather());
            armorFrequencies.add(1);
         }   
      }
      else if(mind>might && mind>agility && might>agility)        //mind, might, agility
      {  //longstaff
         imageIndex = LONGSTAFF;
         weapons[imageIndex].add(Weapon.getLongstaff());
         weaponFrequencies[imageIndex].add(1); 
         //monk starts with 1 book spell
         ArrayList<Item> spellPool = getBookSpellPool(mind);
         if(spellPool.size() > 0)
         {
            Item randSpell = Utilities.getRandomFrom(spellPool);
            spells.add(Spell.getSpellWithName(randSpell.getName()));//(Spell)(randSpell));
            if(randSpell.getName().contains("Floretlade"))
               this.addItem("floretbox");
         }
         infoIndexes[SPELLS_INFO] = NPC.spellInfo.length;
      }
      else if(mind>=might && mind>=agility && agility>=might)     //mind, agility, might
      {  //magic
         imageIndex = STAFF;
         weapons[imageIndex].add(Weapon.getStaff());
         weaponFrequencies[imageIndex].add(1);   
         //mage starts with 2 spells
         ArrayList<Item> spellPool = getAllSpellPool(mind);          
         if(spellPool.size() > 0)
         {
            for(int i = 0; i<2; i++)
            {
               Item randSpell = Utilities.getRandomFrom(spellPool);
               if(randSpell instanceof Weapon && !this.hasWeapon(randSpell.getName()))
               {
                  weapons[STAFF].add(Weapon.getWeaponWithName(randSpell.getName()));
                  weaponFrequencies[STAFF].add(1);
               }
               else if(randSpell instanceof Spell && !this.hasSpell(randSpell.getName()))
               {
                  spells.add(Spell.getSpellWithName(randSpell.getName()));
                  if(randSpell.getName().contains("Floretlade"))
                     this.addItem("floretbox");
               }
            }
         }
         infoIndexes[SPELLS_INFO] = NPC.spellInfo.length;
      }
      else
      {  //dagger
         imageIndex = DAGGER;
         weapons[imageIndex].add(Weapon.getDagger());
         weaponFrequencies[imageIndex].add(3);   
      }
      startStoryIndex = imageIndex;
   
      CultimaPanel.missionStack.add(new Mission(false, false, false));
   //5% chance player starts as a Vampire
      if(Math.random() <  0.05 && allowAffliction)
      {
         startStoryIndex = BAT;    //startStory as Vampire
         setName("~" + name);
         awareness = (Math.min(MAX_AWARENESS,  awareness+2)); 
         if(!this.hasSpell(Spell.getCharm().getName()))
            spells.add(Spell.getCharm());
         if(!this.hasSpell(Spell.getFear().getName()))   
            spells.add(Spell.getFear()); 
         if(!this.hasSpell(Spell.getFlight().getName()))
            spells.add(Spell.getFlight());
         if(!this.hasSpell(Spell.getUnseen().getName()))
            spells.add(Spell.getUnseen());
         weapons[NONE].add(Weapon.getVampyricBite());
         weaponFrequencies[NONE].add(1);   
         manna = Spell.getFlight().getMannaCost();
         CultimaPanel.time = 20;
         rations = 1;
         infoIndexes[VAMPIRE_INFO] = NPC.vampireInfo.length;
         CultimaPanel.missionStack.add(new Mission(true, false, false));
      }
      else if(Math.random() <  0.05 && allowAffliction)
      {
         startStoryIndex = WOLF;    //startStory as Werewolf
         setName(name+"~");
         infoIndexes[WEREWOLF_INFO] = NPC.werewolfInfo.length;
         CultimaPanel.missionStack.add(new Mission(false, true, false));
      }
      else if(Math.random() < 0.05 && allowAffliction)
      {
         startStoryIndex = CAMP;    //startStory as framed
         setName(">"+name+"<");
         reputation = -150;
         bounty = 25;
         body = (might*4 + this.getLevelWhole());
         manna = mind*10;
         CultimaPanel.missionStack.add(new Mission(false, false, true));
      } 
      else if(Math.random() < 0.05 && allowAffliction)
      {
         startStoryIndex = FLIGHT;    //startStory as bounty
         reputation = -50;
         bounty = 100;
         body = (might*4 + this.getLevelWhole());
         manna = mind*10;
         CultimaPanel.missionStack.add(new Mission(false, false, true));
      } 
      weaponSelect = 0;
      armorSelect = 0;
      potionSelect = 0;
      spellSelect = 0;
      int lastIndex = characters[imageIndex][RIGHT_IMAGE].length-1;
      if(guardDown[imageIndex].length > 0)
      {
         for(int i=0; i<guardDown[imageIndex].length-1; i++)  //don't add the last image - it is the dead image
         {
            String p = guardDown[imageIndex][i];
            images.add(p);
         }
      }
      else
      {
         for(int i=0; i<lastIndex; i++)  //don't add the last image - it is the dead image
         {
            String p = characters[imageIndex][RIGHT_IMAGE][i];
            images.add(p);
         }
      }
      dead = new ImageIcon(characters[imageIndex][RIGHT_IMAGE][lastIndex]);
      this.setPictures(images);   
   
      portrait = portraits[imageIndex];
      painTime = -1;
      camping = false;
      campStartTime = -1;
      hiding = false;
      onGuard = false;
   
      deathTime = 0;
      monsterType = 0;
      wellNumber = -1;
      doorNumber = -1;
      doorChange = -1;
      
      sulliedValue = 0;
      
      Utilities.sort(spells);
      numKeyTaps = 0;
   }

   //constructor called from readFile
   public Player(String name, ArrayList<String> images, byte imageInd, byte startInd, int mapInd,
   int r, int c,  int worldR, int worldC, LinkedList<Teleporter>telepMem, String loc, int[]mark,
   int mightVal, int mindVal, int agilVal, int bodyVal, int mannaVal, int aware, int rep, int bnty, int goldVal, int exp, int exlu,
   byte weapSel, byte armorSel, ArrayList<Weapon>[]weap, ArrayList<Integer>[]weapFreq, ArrayList<Armor>arm, ArrayList<Integer>armFreq, byte rat, 
   ArrayList<String>it, ArrayList<String>eff, int [] ptns,  int[] flowers, ArrayList<Spell> spls, int arrows, int stones, String dogN, String horseN, String npcN,
   int[] specExp, int [] statArray, int [] infoInd, Coord [] weapHKeys, int [] spellHKeys, int []potionHKeys)
   {
      super(name, images);
      this.setName(name);  //in case this.costructor adds a starting affliction in the line above
      attackTime = 0;
      lastDir = 0;
      imageIndex = imageInd;
      startStoryIndex = startInd;
      mapIndex = mapInd;     
      mapMark = mark;
      statMod = new int[3];
   
      row = r;
      col = c;
      worldRow = worldR; 
      worldCol = worldC;
      memory = telepMem;
      location = loc;
   
      might = mightVal;
      mind = mindVal;
      agility = agilVal;
      body = bodyVal;
      manna = mannaVal;
      awareness = aware;
      reputation = rep;
      bounty = bnty;
      gold = goldVal;
      experience = exp;
      expToLevelUp = exlu;
   
      weaponSelect = weapSel;
      armorSelect = armorSel; 
      weapons = weap;
      weaponFrequencies = weapFreq;
      armor = arm;
      armorFrequencies = armFreq;
      rations = (byte)(Math.abs(rat));        
      items = it;
      effects = eff;
   
      if(imageIndex >=0 && imageIndex < portraits.length)
      {       
         lastWeaponIndex = imageIndex;
         lastWeaponSelect = weaponSelect;
      }
      else
      {
         lastWeaponIndex = 0;
         lastWeaponSelect = 0;
      }
      potions = ptns;
      flowerBoxCount = flowers;
      spells = spls;
   
      numArrows = arrows;
      numStones = stones;
      
      dogBasicInfo = dogN;
      horseBasicInfo = horseN;
      npcBasicInfo = npcN;
   
      specificExperience = specExp;
      stats = statArray;
   
      infoIndexes = infoInd;
      if(infoIndexes[DOG_INFO] > NPC.dogInfo.length)
         infoIndexes[DOG_INFO] = NPC.dogInfo.length;
      if(infoIndexes[MONSTERS_INFO] > NPC.monsters.length)
         infoIndexes[MONSTERS_INFO] = NPC.monsters.length;
      if(infoIndexes[WEAPONS_INFO] > NPC.armsInfo.length)
         infoIndexes[WEAPONS_INFO] = NPC.armsInfo.length;
      if(infoIndexes[SPELLS_INFO] > NPC.spellInfo.length)
         infoIndexes[SPELLS_INFO] = NPC.spellInfo.length;
      if(infoIndexes[TOWNSFOLK_INFO] > NPC.tavernTown.length)
         infoIndexes[TOWNSFOLK_INFO] = NPC.tavernTown.length;
      if(infoIndexes[VAMPIRE_INFO] > NPC.vampireInfo.length)
         infoIndexes[VAMPIRE_INFO] = NPC.vampireInfo.length;  
      if(infoIndexes[HORSE_INFO] > NPC.horseInfo.length)
         infoIndexes[HORSE_INFO] = NPC.horseInfo.length;
      if(infoIndexes[SHIP_INFO] > NPC.shipInfo.length)
         infoIndexes[SHIP_INFO] = NPC.shipInfo.length;
      if(infoIndexes[WEREWOLF_INFO] > NPC.werewolfInfo.length)
         infoIndexes[WEREWOLF_INFO] = NPC.werewolfInfo.length;
      if(infoIndexes[MAPPING_INFO] > NPC.mappingInfo.length)
         infoIndexes[MAPPING_INFO] = NPC.mappingInfo.length;
      if(infoIndexes[TRAPPING_INFO] > NPC.trappingInfo.length)
         infoIndexes[TRAPPING_INFO] = NPC.trappingInfo.length;
      if(infoIndexes[TOWERS_PUZZLE_INFO] > NPC.towersPuzzleInfo.length)
         infoIndexes[TOWERS_PUZZLE_INFO] = NPC.towersPuzzleInfo.length;  
      if(infoIndexes[DOORS_PUZZLE_INFO] > NPC.doorsPuzzleInfo.length)
         infoIndexes[DOORS_PUZZLE_INFO] = NPC.doorsPuzzleInfo.length; 
      if(infoIndexes[WELLS_PUZZLE_INFO] > NPC.wellsPuzzleInfo.length)
         infoIndexes[WELLS_PUZZLE_INFO] = NPC.wellsPuzzleInfo.length;  
      if(infoIndexes[CRAFTING_INFO] > NPC.gemInfo.length)
         infoIndexes[CRAFTING_INFO] = NPC.gemInfo.length;
      weaponHotKeys = weapHKeys;
      spellHotKeys = spellHKeys;
      potionHotKeys = potionHKeys;
   
      potionSelect = setPotionSelect();
      spellSelect = 0;  
      if(imageIndex >=0 && imageIndex < portraits.length)       
         portrait = portraits[imageIndex];
      else
         portrait = portraits[lastWeaponIndex];
      activeSpells = new ArrayList<Spell>();
   
      painTime = -1;
      camping = false;
      hiding = false;
      campStartTime = -1;
   
      int lastIndex = characters[imageIndex][RIGHT_IMAGE].length-1;
      dead = new ImageIcon(characters[imageIndex][RIGHT_IMAGE][lastIndex]);
   
      onGuard = false;
   
      deathTime = 0;
      Utilities.sort(spells);
      numKeyTaps = 0;
   }
   
   //constructor called from character builder
   //affliction is 0, 1, 2, or 3 for NONE, FRAMED, VAMPIRE, WEREWOLF
   public Player(String name, byte imageInd, int mightVal, int mindVal, int agilVal, int aware, int affliction, Item spell1, Item spell2)
   {
      this(name, null, false);
      CultimaPanel.missionStack.clear();   
      CultimaPanel.missionStack.add(new Mission(false, false, false));
      might = mightVal;
      mind = mindVal;
      agility = agilVal;
      awareness = aware;
      imageIndex = imageInd;
      //set startStory and images to chosen starting character
      gold =Utilities.rollDie(1,50);
      infoIndexes = new int[NUM_INFO_INDEXES];
      startStoryIndex = imageInd;
      portrait = portraits[imageInd];
      int lastIndex = characters[imageIndex][RIGHT_IMAGE].length-1;
      dead = new ImageIcon(characters[imageInd][RIGHT_IMAGE][lastIndex]);
      ArrayList<String> images = new ArrayList<String>();
      if(guardDown[imageIndex].length > 0)
      {
         for(int i=0; i<guardDown[imageIndex].length-1; i++)  //don't add the last image - it is the dead image
         {
            String p = guardDown[imageIndex][i];
            images.add(p);
         }
      }
      else
      {
         for(int i=0; i<lastIndex; i++)  //don't add the last image - it is the dead image
         {
            String p = characters[imageInd][RIGHT_IMAGE][i];
            images.add(p);
         }
      }
      this.setPictures(images);
      //recalcualte body, manna and level
      body = (might*4 + this.getLevelWhole());
      manna = mind*10;
      experience = 44;
      for(int i=0; i<this.getLevel(); i++)
         experience *= EXP_COST_PER_LEVEL;
      expToLevelUp = 500;
      //clear out weapons and spells assigned by this constructor
      weapons = new ArrayList[NUM_WEAPONS];
      for(int i=0; i<weapons.length; i++)
         weapons[i] = new ArrayList<Weapon>();
      weaponFrequencies = new ArrayList[NUM_WEAPONS];
      for(int i=0; i<weaponFrequencies.length; i++)
         weaponFrequencies[i] = new ArrayList<Integer>();
      spells = new ArrayList<Spell>();  
      weapons[NONE].add(Weapon.getFists());
      weaponFrequencies[NONE].add(1);   
      weapons[THROWN].add(Weapon.getStone());
      weaponFrequencies[THROWN].add(1);     
      //give weapons/spells depending on imageInd
      if(imageInd == NONE)
      {
         awareness = Math.min(MAX_AWARENESS,awareness+1);
         gold = 0;
         infoIndexes[DOG_INFO] = NPC.dogInfo.length;
         infoIndexes[MONSTERS_INFO] = NPC.monsters.length;
         infoIndexes[WEAPONS_INFO] = NPC.armsInfo.length;
         infoIndexes[SPELLS_INFO] = NPC.spellInfo.length;
         infoIndexes[TOWNSFOLK_INFO] = NPC.tavernTown.length;
         infoIndexes[VAMPIRE_INFO] = NPC.vampireInfo.length;
         infoIndexes[HORSE_INFO] = NPC.horseInfo.length;
         infoIndexes[SHIP_INFO] = NPC.shipInfo.length;
         infoIndexes[WEREWOLF_INFO] = NPC.werewolfInfo.length;
         infoIndexes[MAPPING_INFO] = NPC.mappingInfo.length;
         infoIndexes[TRAPPING_INFO] = NPC.trappingInfo.length;
         infoIndexes[CRAFTING_INFO] = NPC.gemInfo.length;
      }
      else if(imageInd == TORCH)
      {
         weapons[imageInd].add(Weapon.getTorch());
         weaponFrequencies[imageInd].add(1);
         awareness = Math.min(MAX_AWARENESS,awareness+1);
         gold =Utilities.rollDie(25, 75);
         infoIndexes[MAPPING_INFO] = NPC.mappingInfo.length;
         items.add(Item.getFloretBox().getName());
      }
      else if(imageInd == STAFF)
      {
         weapons[imageInd].add(Weapon.getStaff());
         weaponFrequencies[imageInd].add(1);
         //mage starts with 2 spells
         if(spell1 != null)
         {
            if(spell1 instanceof Weapon && !this.hasWeapon(spell1.getName()))
            {
               weapons[STAFF].add(Weapon.getWeaponWithName(spell1.getName()));
               weaponFrequencies[STAFF].add(1);
            }
            else if(spell1 instanceof Spell && !this.hasSpell(spell1.getName()))
            {
               spells.add(Spell.getSpellWithName(spell1.getName()));
               if(spell1.getName().contains("Floretlade"))
                  this.addItem("floretbox");
            }
         }
         if(spell2 != null)
         {
            if(spell2 instanceof Weapon && !this.hasWeapon(spell2.getName()))
            {
               weapons[STAFF].add(Weapon.getWeaponWithName(spell2.getName()));
               weaponFrequencies[STAFF].add(1);
            }
            else if(spell2 instanceof Spell && !this.hasSpell(spell2.getName()))
            {
               spells.add(Spell.getSpellWithName(spell2.getName()));
               if(spell2.getName().contains("Floretlade"))
                  this.addItem("floretbox");
            }
         }
         infoIndexes[SPELLS_INFO] = NPC.spellInfo.length;
      }
      else if(imageInd == LONGSTAFF)
      {
         weapons[imageInd].add(Weapon.getLongstaff());
         weaponFrequencies[imageInd].add(1);
         //monk starts with 1 book spell
         if(spell1 != null)
         {
            if(spell1 instanceof Weapon && !this.hasWeapon(spell1.getName()))
            {
               weapons[STAFF].add(Weapon.getWeaponWithName(spell1.getName()));
               weaponFrequencies[STAFF].add(1);
            }
            else if(spell1 instanceof Spell && !this.hasSpell(spell1.getName()))
            {
               spells.add(Spell.getSpellWithName(spell1.getName()));
               if(spell1.getName().contains("Floretlade"))
                  this.addItem("floretbox");
            }
         }
         infoIndexes[SPELLS_INFO] = NPC.spellInfo.length;
      }
      else if(imageInd == SPEAR)
      {
         weapons[imageInd].add(Weapon.getSpear());
         weaponFrequencies[imageInd].add(1);
         infoIndexes[SHIP_INFO] = NPC.shipInfo.length;
      }
      else if(imageInd == BOW)
      {
         weapons[imageInd].add(Weapon.getBow());
         weaponFrequencies[imageInd].add(1);
         numArrows = 25;
         infoIndexes[TRAPPING_INFO] = NPC.trappingInfo.length;
      }
      else if(imageInd == CROSSBOW)
      {
         weapons[imageInd].add(Weapon.getCrossbow());
         weaponFrequencies[imageInd].add(1);
         numArrows = 25;
         gold =Utilities.rollDie(25, 75);
      }
      else if(imageInd == AXE)
      {
         weapons[imageInd].add(Weapon.getAxe());
         weaponFrequencies[imageInd].add(1);
         infoIndexes[TOWNSFOLK_INFO] = NPC.tavernTown.length;
         infoIndexes[MAPPING_INFO] = NPC.mappingInfo.length;
      }
      else if(imageInd == DUALAXE)
      {
         weapons[imageInd].add(Weapon.getDualAxe());
         weaponFrequencies[imageInd].add(1);
         armor.add(Armor.getLeather());
         armorFrequencies.add(1);
      }
      else if(imageInd == HAMMER)
      {
         weapons[imageInd].add(Weapon.getHammer());
         weaponFrequencies[imageInd].add(1);
         this.addItem(Item.getRandomStone().getName());
      }
      else if(imageInd == DAGGER)
      {
         weapons[imageInd].add(Weapon.getDagger());
         weaponFrequencies[imageInd].add(3);
         this.addItem(Item.getLockpick().getName());
      }
      else if(imageInd == SABER)
      {
         weapons[imageInd].add(Weapon.getSword());
         weaponFrequencies[imageInd].add(1);
         infoIndexes[MONSTERS_INFO] = NPC.monsters.length;
      }
      else if(imageInd == SWORDSHIELD)
      {
         weapons[imageInd].add(Weapon.getSwordBuckler());
         weaponFrequencies[imageInd].add(1);
         infoIndexes[HORSE_INFO] = NPC.horseInfo.length;
      }
      else if(imageInd == DUAL)
      {
         weapons[imageInd].add(Weapon.getShortSwords());
         weaponFrequencies[imageInd].add(1);
         this.addItem(Item.getLoadedCube().getName());
      }
      else if(imageInd == LUTE)
      {
         weapons[imageInd].add(Weapon.getLuteOfDestiny());
         weaponFrequencies[imageInd].add(1);
         gold =Utilities.rollDie(25, 75);
      }
      
      if(affliction == CharacterBuilder.BOUNTY)
      {
         startStoryIndex = FLIGHT;    //startStory as bounty
         reputation = -50;
         bounty = 100;
         body = (might*4 + this.getLevelWhole());
         manna = mind*10;
         CultimaPanel.missionStack.add(new Mission(false, false, true));
         // Mission(boolean isVampire, boolean isWerewolf, boolean isFramed)
      }
      else if(affliction == CharacterBuilder.FRAMED)
      {
         startStoryIndex = CAMP;    //startStory as framed
         setName(">"+name+"<");
         reputation = -150;
         bounty = 25;
         body = (might*4 + this.getLevelWhole());
         manna = mind*10;
         CultimaPanel.missionStack.add(new Mission(false, false, true));
         // Mission(boolean isVampire, boolean isWerewolf, boolean isFramed)
      }
      else if(affliction == CharacterBuilder.VAMPIRE)
      {
         startStoryIndex = BAT;    //startStory as Vampire
         setName("~" + name);
         awareness = (Math.min(MAX_AWARENESS,  awareness+2)); 
         if(!this.hasSpell(Spell.getCharm().getName()))
            spells.add(Spell.getCharm());
         if(!this.hasSpell(Spell.getFear().getName()))   
            spells.add(Spell.getFear()); 
         if(!this.hasSpell(Spell.getFlight().getName()))
            spells.add(Spell.getFlight());
         if(!this.hasSpell(Spell.getUnseen().getName()))
            spells.add(Spell.getUnseen());
         weapons[NONE].add(Weapon.getVampyricBite());
         weaponFrequencies[NONE].add(1);   
         manna = Spell.getFlight().getMannaCost();
         CultimaPanel.time = 20;
         rations = 1;
         infoIndexes[VAMPIRE_INFO] = NPC.vampireInfo.length;
         CultimaPanel.missionStack.add(new Mission(true, false, false));
         // Mission(boolean isVampire, boolean isWerewolf, boolean isFramed)
      }
      else if(affliction == CharacterBuilder.WEREWOLF)
      {
         startStoryIndex = WOLF;    //startStory as Werewolf
         setName(name+"~");
         infoIndexes[WEREWOLF_INFO] = NPC.werewolfInfo.length;
         CultimaPanel.missionStack.add(new Mission(false, true, false));
         // Mission(boolean isVampire, boolean isWerewolf, boolean isFramed)
      }
      this.scrollWeaponRight();
      Utilities.sort(spells);
      numKeyTaps = 0;
   }
   
   public static int[] buildRandomStats()
   {
      int [] retVal = new int[4];
      int points = 50;
      int order = (int)(Math.random()*3);
      int might, mind, agility, awareness=0;
      if(order==0)         //might first
      {
         might =Utilities.rollDie(3,points-3);
         points = Math.max(3, points-might);
         if(Math.random() < 0.5)
         {
            mind =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-mind);
            agility =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-agility);
         }
         else
         {
            agility =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-agility);
            mind =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-mind);
         }
      }
      else if(order==1)         //mind first
      {
         mind =Utilities.rollDie(3,points-3);
         points = Math.max(3, points-mind);
         if(Math.random() < 0.5)
         {
            agility =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-agility);
            might =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-might);
         }
         else
         {
            might =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-might);
            agility =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-agility);
         }
      }
      else //if(order==2)         //agility first
      {
         agility =Utilities.rollDie(3,points-3);
         points = Math.max(3, points-agility);
         if(Math.random() < 0.5)
         {
            might =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-might);
            mind =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-mind);
         }
         else
         {
            mind =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-mind);
            might =Utilities.rollDie(3,points-3);
            points = Math.max(3, points-might);
         }
      }
      while(points > 10 && awareness < 4)    //distribute any extra points
      {
         awareness++;
         points -= 10;
      }
      if(points > 0)
      {
         int randStat = Utilities.rollDie(1,3);
         if(randStat == 1)
            might += points;
         else if(randStat == 2)
            mind += points;
         else
            agility += points;   
         points = 0;   
      }
      retVal[0] = might;
      retVal[1] = mind;
      retVal[2] = agility;
      retVal[3] = awareness;
      return retVal;
   }
   
   public static ArrayList<Item> getBookSpellPool(int mindValue)
   {
      ArrayList<Item> spellPool = new ArrayList<Item>();
      for(Item it:CultimaPanel.allMagicInventory)
         if(it instanceof Spell && Player.getMannaMax(mindValue) >= (((Spell)(it)).getMannaCost()) && !it.getName().toLowerCase().contains("lute"))
            spellPool.add(it);
      ArrayList<Item> fieldSpellPool = new ArrayList<Item>();
      fieldSpellPool.add(Spell.getTempest());        //add book spells that have to be learned in the field
      fieldSpellPool.add(Spell.getTimeStop());
      fieldSpellPool.add(Spell.getFirestorm());
      fieldSpellPool.add(Spell.getFloretLade());
      fieldSpellPool.add(Spell.getRaiseDead());
      for(Item it:fieldSpellPool)
         if(Player.getMannaMax(mindValue) >= (((Spell)(it)).getMannaCost()))
            spellPool.add(it);
      Utilities.sortItems(spellPool);  
      return spellPool;
   }

   public static ArrayList<Item> getAllSpellPool(int mindValue)
   {
      ArrayList<Item> spellPool = new ArrayList<Item>();
      for(Item it:CultimaPanel.allMagicInventory)
      {
         String itName = it.getName().toLowerCase();
         if(it instanceof Spell || it instanceof Weapon)
         {
            if(it instanceof Spell && Player.getMannaMax(mindValue) >= (((Spell)(it)).getMannaCost()))
               spellPool.add(it);
            if(it instanceof Weapon && Player.getMannaMax(mindValue) >= (((Weapon)(it)).getMannaCost()) && !itName.contains("staff") && !itName.contains("lute"))
               spellPool.add(it);
         }
      }
      ArrayList<Item> fieldSpellPool = new ArrayList<Item>();
      fieldSpellPool.add(Spell.getTempest());        //add book spells that have to be learned in the field
      fieldSpellPool.add(Spell.getTimeStop());
      fieldSpellPool.add(Spell.getFirestorm());
      fieldSpellPool.add(Spell.getFloretLade());
      fieldSpellPool.add(Spell.getRaiseDead());
      fieldSpellPool.add(Weapon.getCurse());          //add staff spells that have to be learned in the field
      fieldSpellPool.add(Weapon.getPossess());
      fieldSpellPool.add(Weapon.getSpidersWeb());
      for(Item it:fieldSpellPool)
         if((it instanceof Spell && Player.getMannaMax(mindValue) >= (((Spell)(it)).getMannaCost())) || (it instanceof Weapon && Player.getMannaMax(mindValue) >= (((Weapon)(it)).getMannaCost())))
            spellPool.add(it);  
      Utilities.sortItems(spellPool);              
      return spellPool;
   }
   
   public int getNumKeyTaps()
   {
      return numKeyTaps;
   }
   
   public void incrementKeyTaps()
   {
      if(numKeyTaps < Byte.MAX_VALUE)
      {
         numKeyTaps++;
      }
   }
   
   public void setKeyTaps(byte kt)
   {
      numKeyTaps = kt;
   }
   
   //start day based on name so different players begin in different seasons
   public int getStartDay()
   {
      return Math.abs(this.getName().hashCode()) % 100;
   }
   
   //returns true if player is at row r, col c
   public boolean isAt(int r, int c)
   {
      return (row==r && col==c);
   }
   
   //for drinking in the tavern and possibly adding sullied effect
   public int getSulliedValue()
   {
      return sulliedValue;
   }
   
   public void setSulliedValue(int sv)
   {
      sulliedValue = sv;
      if(sulliedValue < 0)
      {
         sulliedValue = 0;
         return;
      }
      double percentSullied = ((double)(sulliedValue)/this.getBody())*100;
      if(Utilities.rollDie(200) < percentSullied && !this.hasEffect("sullied"))
      {
         this.addEffect("sullied");
         this.addReputation(-1);               
      }
   }
   
   public void addSulliedValue(int sv)
   {
      sulliedValue += sv;
      if(sulliedValue < 0)
      {
         sulliedValue = 0;
         return;
      }
      double percentSullied = ((double)(sulliedValue)/this.getBody())*100;
      if(Utilities.rollDie(200) < percentSullied && !this.hasEffect("sullied"))
      {
         this.addEffect("sullied");
         this.addReputation(-1);               
      }
   }
   
   public int [] getMapMark()
   {
      return mapMark;
   }
   
   public int getMapMarkMapIndex()
   {
      return mapMark[0];
   }
   
   public int getMapMarkRow()
   {
      return mapMark[1];
   }
   
   public int getMapMarkCol()
   {
      return mapMark[2];
   }
   
   public void setMapMark(int [] mark)
   {
      mapMark = mark;
   }
   
   public void setMapMark()
   {
      mapMark[0] = this.getMapIndex();
      mapMark[1] = this.getWorldRow();
      mapMark[2] = this.getWorldCol();      
   }
   
   public void clearMapMark()
   {
      mapMark[0] = -1;
      mapMark[1] = -1;
      mapMark[2] = -1;
   }
      
    //record the monster type that swallows us whole   
   public byte getMonsterType()
   {
      return monsterType;
   }
      
   public void setMonsterType(byte mt)
   {
      monsterType = mt;
   }
     
   //record the well number we draw water from (0, 1, 2, 3) for the underworld puzzle of the 3 wells      
   public byte getWellNumber()
   {
      return wellNumber;
   }
      
   public void setWellNumber(byte wn)
   {
      wellNumber = wn;
   }

   //record the door number we pick (1, 2, 3) for the dungeon puzzle of the 3 doors      
   public byte getDoorNumber()
   {
      return doorNumber;
   }
      
   public void setDoorNumber(byte dn)
   {
      doorNumber = dn;
   }
   
    //record the door number we might change to (1, 2, 3) for the dungeon puzzle of the 3 doors      
   public byte getDoorChange()
   {
      return doorChange;
   }
      
   public void setDoorChange(byte dc)
   {
      doorChange = dc;
   }
   
   //post: given the name of a tile we walked over that has flowers, this will add any flowers of that color into our flowerBoxCount array if we have room   
   public void pickFlower(String tileName)
   {
      if(this.hasItem("floretbox"))
      {
         if(this.enoughFlowers())
            CultimaPanel.sendMessage("Floretbox is full.");  
         else if(tileName.toLowerCase().contains("red"))
         {
            if(flowerBoxCount[RED]<FLOWERS_FOR_MANNA)
               flowerBoxCount[RED]++;
            else
               CultimaPanel.sendMessage("Red florets full.");  
         }
         else if(tileName.toLowerCase().contains("yellow"))
         {
            if(flowerBoxCount[YELLOW]<FLOWERS_FOR_MANNA)
               flowerBoxCount[YELLOW]++;
            else
               CultimaPanel.sendMessage("Yellow florets full.");  
         }
         else if(tileName.toLowerCase().contains("blue"))
         {
            if(flowerBoxCount[BLUE]<FLOWERS_FOR_MANNA)
               flowerBoxCount[BLUE]++;
            else
               CultimaPanel.sendMessage("Blue florets full.");
         }
         else if(tileName.toLowerCase().contains("violet"))
         {
            if(flowerBoxCount[VIOLET]<FLOWERS_FOR_MANNA)
               flowerBoxCount[VIOLET]++;
            else
               CultimaPanel.sendMessage("Violet florets full.");
         }
         else if(tileName.toLowerCase().contains("green"))
         {
            if(flowerBoxCount[GREEN]<FLOWERS_FOR_MANNA)
               flowerBoxCount[GREEN]++;
            else
               CultimaPanel.sendMessage("Green florets full.");
         }
      }
   }
   
   //post: given a certain color name, returns the index of where it is represented in the flowerBoxCount array
   public int colorToInt(String color)
   {
      color = color.trim().toLowerCase();   
      if(color.contains("red"))
         return 0;    //index values for flower colors able to store in a flowerBox
      if(color.contains("yellow"))
         return 1;
      if(color.contains("blue"))
         return 2;
      if(color.contains("violet"))
         return 3;
      if(color.contains("green"))
         return 4;
      return -1;
   }
   
   //post:  returns the array of flower-color frequencies the player has
   public int [] getFlowerBoxCount()
   {
      return flowerBoxCount;
   }
   
   //post: returns the number of flowers of a specific color
   public int numSpecificFlowers(String color)
   {
      int index = colorToInt(color);
      if(index >= 0 && index < flowerBoxCount.length)
      {
         return flowerBoxCount[index];
      }
      return 0;
   }
   
   //post: removes all flowers of a specific color
   public void emptySpecificFlowers(String color)
   {
      int index = colorToInt(color);
      if(index >= 0 && index < flowerBoxCount.length)
      {
         flowerBoxCount[index] = 0;
      }
   }
   
   //post: returns the number of red flowers in our flowerBoxCount array
   public int numRedFlowers()
   {
      return flowerBoxCount[RED];
   }
   
   //post: returns the number of yellow flowers in our flowerBoxCount array
   public int numYellowFlowers()
   {
      return flowerBoxCount[YELLOW];
   }
   
   //post: returns the number of blue flowers in our flowerBoxCount array
   public int numBlueFlowers()
   {
      return flowerBoxCount[BLUE];
   }
   
   //post: returns the number of violet flowers in our flowerBoxCount array
   public int numVioletFlowers()
   {
      return flowerBoxCount[VIOLET];
   }
   
   //post: returns the number of green flowers in our flowerBoxCount array
   public int numGreenFlowers()
   {
      return flowerBoxCount[GREEN];
   }
   
   //post: returns the total number of flowers in our flowerBoxCount array
   public int numFlowers()
   {
      int count=0;
      for(int i=0; i<flowerBoxCount.length; i++)
         count += flowerBoxCount[i];
      return count;
   }
   
   //post: remove num flowers from floretbox - random colors - for feeding them to the unicorn
   public void removeFlowers(int num)
   {
      if(numFlowers() < num)
      {
         clearFlowers();
         return;
      }
      ArrayList<Byte>notEmpty = new ArrayList();
      for(int i=0; i<flowerBoxCount.length; i++)
         if(flowerBoxCount[i] > 0)
            notEmpty.add((byte)i);
      if(notEmpty.size() > 0)
      {
         for(int i=0; i<num; i++)
         {
            int randIndex = Utilities.getRandomFrom(notEmpty);
            flowerBoxCount[randIndex]--;
            if(flowerBoxCount[randIndex] <= 0)
            {
               for(int j=notEmpty.size()-1; j>=0; j--)
                  if(notEmpty.get(j)==randIndex)
                     notEmpty.remove(j);
               if(notEmpty.size() == 0)
               {
                  clearFlowers();
                  return;
               }
            }
         }
      }
   }
   
   //post: remove num flowers from floretbox of each color for mission
   public void removeAllFlowers(byte num)
   {
      for(byte i=0; i<flowerBoxCount.length; i++)
         flowerBoxCount[i] = Math.max(0, flowerBoxCount[i]-num);
   }


   //true if our weapon is bright or flaming - used to determine if we can successfully hide or not
   public boolean hasBrightWeapon()
   {
      String armName = this.getArmor().getName().toLowerCase();
      Weapon ourWeap = this.getWeapon();
      String weapName = ourWeap.getName().toLowerCase();         
      return (weapName.contains("bright") || ourWeap.getEffect().contains("fire") || weapName.contains("flame") 
           || weapName.contains("fire") || armName.contains("holocaust") || weapName.contains("torch"));
   }

   //do we have enough flowers to cast a Floretlade spell to recharge manna
   public boolean enoughFlowers()
   {
      return (numRedFlowers()==FLOWERS_FOR_MANNA && numYellowFlowers()==FLOWERS_FOR_MANNA && numBlueFlowers()==FLOWERS_FOR_MANNA && numVioletFlowers()==FLOWERS_FOR_MANNA && numGreenFlowers()==FLOWERS_FOR_MANNA);
   }
   
   //do we have 5 flowers of each type for COLLECT_FLORETS1 mission
   public boolean fiveFlowersOfEach()
   {
      return (numRedFlowers()>=5 && numYellowFlowers()>=5 && numBlueFlowers()>=5 && numVioletFlowers()>=5 && numGreenFlowers()>=5);
   }
   
   public void clearFlowers()
   {
      for(int i=0; i<flowerBoxCount.length; i++)
         flowerBoxCount[i]=0;
   }

   public byte getStartStoryIndex()
   {
      return startStoryIndex;
   }

   public boolean isOnGuard()
   {
      return onGuard;
   }
   
   //true if we are holding a threatening weapon
   public boolean threateningWeaponDrawn()
   {
      return (this.getImageIndex()!=Player.NONE && this.getImageIndex()!=Player.STAFF 
           && this.getImageIndex()!=Player.LUTE && this.getImageIndex()!=Player.TORCH 
           && !this.getWeapon().getName().toLowerCase().contains("sceptre"));
   }

   public void toggleOnGuard()
   {
      if(onHorse() || onShip() || isSailing() || isCamping())
      {
         return;
      }
      onGuard = !onGuard;
      this.setImageIndex(this.getImageIndex());
   }

   public void setOnGuard(boolean og)
   {
      if(onHorse() || onShip() || isSailing() || isCamping() || (onGuard == og))
      {
         return;
      }
      onGuard = og;
      this.setImageIndex(this.getImageIndex());
   }

   public int getNumArrows()
   {
      return numArrows;
   }
   
   public int getNumStones()
   {
      return numStones;
   }

   public void setNumArrows(int na)
   {
      numArrows = Math.min(this.getMaxArrowCapacity(), na);
   }
   
   public void setNumStones(int ns)
   {
      numStones = ns;
   }
   
   public int getMaxArrowCapacity()
   {
      int maxArrows = 50;
      if(this.hasItem("holdall"))
         maxArrows = 100;
      else if(this.hasItem("swiftquill"))
         maxArrows = 75;
      return maxArrows;
   }
   
   public void addStone()
   {
      if(canPickupStone())
         numStones++;
   }
   
   public boolean canPickupStone()
   {
      if((hasItem("sling") && numStones < 15) || (!hasItem("sling") && numStones < 5))
         return true;
      return false;
   }
   
   public boolean isOnStone(Terrain currentPos)
   {
      return (currentPos.getName().toLowerCase().contains("mountain") || currentPos.getName().toLowerCase().contains("hill"));
   }
   
   public boolean canHide(Terrain currentPos)
   {
      return (!onHorse() && !onShip() && !isFlying() && !isWolfForm()) && (currentPos.getName().toLowerCase().contains("tall_grass") || currentPos.getName().toLowerCase().contains("water") || currentPos.getName().toLowerCase().contains("bog") || (currentPos.getName().toLowerCase().contains("forest") && !currentPos.getName().toLowerCase().contains("dead")));
   }
   
   public boolean canHide()
   {
      if(onHorse() || onShip() || isFlying() || isWolfForm())
      {
         return false;
      }
      if(mapIndex >=0 && mapIndex < CultimaPanel.map.size())
      {
         byte[][]currMap = (CultimaPanel.map.get(mapIndex));
         if(!LocationBuilder.outOfBounds(currMap, row, col))
         {
            Terrain currentPos = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col]));
            return (currentPos.getName().toLowerCase().contains("tall_grass") || currentPos.getName().toLowerCase().contains("water") || currentPos.getName().toLowerCase().contains("bog") || (currentPos.getName().toLowerCase().contains("forest") && !currentPos.getName().toLowerCase().contains("dead")));
         }
      }
      return false;
   }
   
   public void setHide(boolean state)
   {
      if(!hiding && !state)
      {
         return;
      }
      if(state && canHide())
      {
         this.setLastWeaponIndex(this.getImageIndex());
         this.setLastWeaponSelect(this.getWeaponSelect());
         setImageIndexTemp(HIDE);
         hiding = true;
      }
      else if(!state)
      {
         this.setImageIndex(this.getLastWeaponIndex());
         this.setWeaponSelect(this.getLastWeaponSelect());
         hiding = false;
      }
   }
   
   public boolean isHiding()
   {
      return hiding;
   }

   public boolean onHorse()
   {
      return !horseBasicInfo.equals("none");
   }
   
   public boolean onUnicorn()
   {
      return (this.getHorseBasicInfo().contains("Unicorn"));
   }

   public boolean canMountHorse()
   {
      return !(this.getArmor().getName().toLowerCase().contains("cloak"));
   }
 
   public void mountHorse(NPCPlayer ourHorse)
   {
      if(ourHorse==null)
         return;
      int numTries = 0;
      while (this.getArmor().getName().toLowerCase().contains("cloak") && numTries < 100)
      {
         scrollArmorUp();
         numTries++;
      }
      if(getActiveSpell("Invisibility")!=null)     //invisibility wears off as soon as you attack
      {
         this.removeSpell("Invisibility");
      }
      if(getActiveSpell("Unseen")!=null)           //unseen wears off as soon as you attack
      {
         this.removeSpell("Unseen");
      }
      if(this.getActiveSpell("Fear")!=null)
      {                                            //cancel our fear spell
         this.removeSpell("Fear");
      }
      this.setOnGuard(false);
      horseBasicInfo = ourHorse.basicInfo();
      Sound.getOnHorse();
      if(Utilities.getDog()!=null && Utilities.distToDog() <= 10)
         Achievement.earnAchievement(Achievement.ANIMALS_AS_LEADERS);
            
      if(ourHorse.getCharIndex()==NPC.UNICORN)
      {
         Achievement.earnAchievement(Achievement.THE_RIDE_THE_RAINBOW_ACHIEVEMENT);
         this.setImageIndexTemp(Player.UNICORN);
      }
      else
         this.setImageIndexTemp(Player.HORSE);
      
      if(!ourHorse.hasMet())
         this.stats[Player.HORSES_TAMED]++;
   
   }

   public void dismountHorse()
   {
      this.setOnGuard(false);
      byte horseType = NPC.HORSE;
      if(onUnicorn())
         horseType = NPC.UNICORN;
      NPCPlayer ourHorse = new NPCPlayer(horseType, this.getRow(), this.getCol(), 0, "world", this.getHorseBasicInfo(),true);   
      ourHorse.setHasMet(true);
      if(horseType == NPC.UNICORN && ourHorse.getReputation() % 2 != 0) //a unicorn with an odd reputation can't be mounted
         ourHorse.setReputation(ourHorse.getReputation()+1);            //if we are dismounting, make sure the reputation is even so we can ride it again
      CultimaPanel.worldMonsters.add(ourHorse);
      this.setImageIndex(this.getLastWeaponIndex());
      this.setWeaponSelect(this.getLastWeaponSelect());
      this.setHorseBasicInfo("none");
      Sound.getOnHorse();
   }

   public int getTavernInfo()
   {
      return infoIndexes[TOWNSFOLK_INFO];
   }

   public int getMonsterInfo()
   {
      return infoIndexes[MONSTERS_INFO];
   }

   public int getArmsInfo()
   {
      return infoIndexes[WEAPONS_INFO];
   }

   public int getSpellIndex()
   {
      return infoIndexes[SPELLS_INFO];
   }

   public int getRiddleInfo()
   {
      return infoIndexes[RIDDLES_INFO];
   }

   public int getDogInfo()
   {
      return infoIndexes[DOG_INFO];
   }

   public int getHorseInfo()
   {
      return infoIndexes[HORSE_INFO];
   }

   public int getShipInfo()
   {
      return infoIndexes[SHIP_INFO];
   }

   public int getVampireInfo()
   {
      return infoIndexes[VAMPIRE_INFO];
   }
   
   public int getWerewolfInfo()
   {
      return infoIndexes[WEREWOLF_INFO];
   }
   
   public int getMappingInfo()
   {
      return infoIndexes[MAPPING_INFO];
   }
   
   public int getTrappingInfo()
   {
      return infoIndexes[TRAPPING_INFO];
   }
   
   public int getWellsPuzzleInfo()
   {
      return infoIndexes[WELLS_PUZZLE_INFO];
   }

   public int getDoorsPuzzleInfo()
   {
      return infoIndexes[DOORS_PUZZLE_INFO];
   }
   
   public int getTowersPuzzleInfo()
   {
      return infoIndexes[TOWERS_PUZZLE_INFO];
   }
   
   public int getCraftingInfo()
   {
      return infoIndexes[CRAFTING_INFO];
   }

   public int nextTavernInfo()
   {
      return infoIndexes[TOWNSFOLK_INFO]++;
   }

   public int nextMonsterInfo()
   {
      return infoIndexes[MONSTERS_INFO]++;
   }

   public int nextArmsInfo()
   {
      return infoIndexes[WEAPONS_INFO]++;
   }

   public int nextSpellInfo()
   {
      return infoIndexes[SPELLS_INFO]++;
   }

   public int nextRiddleInfo()
   {
      return infoIndexes[RIDDLES_INFO]++;
   }

   public int nextDogInfo()
   {
      return infoIndexes[DOG_INFO]++;
   }

   public int nextHorseInfo()
   {
      return infoIndexes[HORSE_INFO]++;
   }

   public int nextShipInfo()
   {
      return infoIndexes[SHIP_INFO]++;
   }

   public int nextVampireInfo()
   {
      return infoIndexes[VAMPIRE_INFO]++;
   }
   
   public int nextWerewolfInfo()
   {
      return infoIndexes[WEREWOLF_INFO]++;
   }
   
   public int nextMappingInfo()
   {
      return infoIndexes[MAPPING_INFO]++;
   }
   
   public int nextTrappingInfo()
   {
      return infoIndexes[TRAPPING_INFO]++;
   }
   
   public int nextWellsPuzzleInfo()
   {
      return infoIndexes[WELLS_PUZZLE_INFO]++;
   }
   
   public int nextDoorsPuzzleInfo()
   {
      return infoIndexes[DOORS_PUZZLE_INFO]++;
   }
   
   public int nextTowersPuzzleInfo()
   {
      return infoIndexes[TOWERS_PUZZLE_INFO]++;
   }
   
   public int nextCraftingInfo()
   {
      return infoIndexes[CRAFTING_INFO]++;
   }

   public boolean readAllPuzzleBooks()
   {
      return (infoIndexes[WELLS_PUZZLE_INFO]>=NPC.wellsPuzzleInfo.length && infoIndexes[DOORS_PUZZLE_INFO]>=NPC.doorsPuzzleInfo.length && infoIndexes[TOWERS_PUZZLE_INFO]>=NPC.towersPuzzleInfo.length);
   }

   public int getBounty()
   {
      return bounty;
   }

   public void setBounty(int b)
   {
      bounty = b;
      if(bounty < 1)
         bounty = 1;
   }

   public int getTaxesOwed()
   {
      int taxes = Math.min(1000, this.getGold() / 20);
      if(inLocationWithHome(this.getMapIndex()))  //add property tax
         taxes += 20;
      if(taxes < 2)
         taxes = 2;   
      return taxes;   
   }

   public byte getLastDir()
   {
      return lastDir;
   }

   public void setLastDir(byte ld)
   {
      lastDir = ld;
      setHide(false);   //since this is called every time we try to move, set hiding to false
   }

   public byte getLastWeaponIndex()
   {
      return lastWeaponIndex;
   }

   public void setLastWeaponIndex(byte ld)
   {
      if(ld >=0 && ld < weapons.length)
         lastWeaponIndex = ld;
      else
         lastWeaponIndex = NONE;   
   }

   public byte getLastWeaponSelect()
   {
      return lastWeaponSelect;
   }

   public void setLastWeaponSelect(byte ws)
   {
      if(ws >=0 && imageIndex >= 0 && imageIndex < weapons.length && ws < weapons[imageIndex].size())
         lastWeaponSelect = ws;
      else
         lastWeaponIndex = 0;
   }

   public int getPainTime()
   {
      return painTime;
   }

   public void setPainTime(int pt)
   {
      painTime = pt;
   }

   public boolean isVampire()
   {
      return (this.getName().startsWith("~"));
   }

   public boolean isWerewolf()
   {
      return (this.getName().endsWith("~"));
   }
   
   public boolean isFramed()
   {
      return (this.getName().contains(">"));
   }

   public boolean isWolfForm()
   {
      return (this.getImageIndex()==Player.WOLF);
   }

   public boolean isSailing()
   {
      return (this.getImageIndex()==Player.GREATSHIP || this.getImageIndex()==Player.BRIGANDSHIP || this.getImageIndex()==Player.BOAT);
   }
   
   public boolean onShip()
   {
      return (this.getImageIndex()==Player.GREATSHIP || this.getImageIndex()==Player.BRIGANDSHIP);
   }
   
   public boolean onBoat()
   {
      return (this.getImageIndex()==Player.BOAT);
   }

   public boolean isCamping()
   {
      return camping;
   }
   
   public boolean isCampingWithFire()
   {
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));   
      String current = "";
      if(!LocationBuilder.outOfBounds(currMap, row, col))
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
      return (camping && !current.contains("bed"));
   }
   
   public boolean isOnIce()
   {
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));   
      String current = "";
      if(this.getLocationType().toLowerCase().contains("underworld"))
         return false;
      if(!LocationBuilder.outOfBounds(currMap, row, col))
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
         return (Display.frozenWater() && current.contains("water"));
      }
      return false;
   }
   
   public boolean isInvisible()
   {
      return (this.getActiveSpell("Unseen")!=null  || this.getActiveSpell("Invisibility")!=null  || this.getArmor().getName().toLowerCase().contains("invisibility"));
   }
   
   public boolean isFlying()
   {
      return (this.getActiveSpell("Flight")!=null);
   }
   
   public boolean magicMistActive()
   {
      return (this.getActiveSpell("Magicmist")!=null);
   }

   public boolean isInSpecialRealm()
   {
      return (this.getLocationType().toLowerCase().contains("underworld") || 
             this.getLocationType().toLowerCase().contains("jurassica") || 
             this.getLocationType().toLowerCase().contains("1942") ||
             this.getLocationType().toLowerCase().contains("graboid") ||
             this.getLocationType().toLowerCase().contains("beast") ||
             this.getLocationType().toLowerCase().contains("pacrealm") ||
             this.getLocationType().toLowerCase().contains("wolfenstein"));
   }
   
   public boolean isInArena()
   {
      return (this.getLocationType().toLowerCase().contains("arena"));
   }

   public boolean canSleep()
   {
      boolean sailing = this.isSailing();
      boolean flight = this.isFlying();
      boolean hasCloak = (this.getArmor().getName().toLowerCase().contains("cloak"));
      if(sailing || flight || this.onHorse() || this.isCamping() || this.isWolfForm() || hasCloak)
         return false;
      boolean isVampire = this.isVampire();
      boolean isNight = CultimaPanel.isNight;   //isNight = (time >=20 || time <= 6)
      double time = CultimaPanel.time;
      boolean rightTime = ((!isVampire && isNight) || (isVampire && (time >=8 && time <= 16)));
      boolean rightPlace = ((!isVampire && TerrainBuilder.isCampableSpotForPlayer())
                          || (isVampire && TerrainBuilder.isCampableSpotForVampire()));   
      boolean inBattle = ((this.getLocationType().contains("battlefield") && CultimaPanel.numBattleFieldEnemies > 0) 
                       || (this.getLocationType().equals("ship") && CultimaPanel.numEnemiesOnShip > 0));                                                  
      if(rightTime && rightPlace && !inBattle)    //set camp
         return true;
      return false;
   }
   
   //returns if we are in an optimal place to sleep (to give priority over trying to steal something)
   public boolean onBedAndTimeToSleep()
   {
      int r = CultimaPanel.player.getRow();
      int c = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();
      byte[][] currMap = CultimaPanel.map.get(mi);
      if(LocationBuilder.outOfBounds(currMap, r, c))
         return false;
      String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
      boolean isVampire = this.isVampire();
      double time = CultimaPanel.time;
      boolean onBed = (!isVampire && current.contains("bed")) || (isVampire && current.contains("coffin"));
      boolean rightTime = ((!isVampire && CultimaPanel.isNight) || (isVampire && (time >=8 && time <= 16)));
      return onBed && rightTime;
   }
   
  //returns if we are in danger of getting the vampire curse by sleeping in a coffin
   public boolean onCoffinAndNotVampire()
   {
      int r = CultimaPanel.player.getRow();
      int c = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();
      byte[][] currMap = CultimaPanel.map.get(mi);
      if(LocationBuilder.outOfBounds(currMap, r, c))
         return false;
      String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
      boolean isVampire = this.isVampire();
      double time = CultimaPanel.time;
      boolean onBed = (current.contains("coffin"));
      boolean rightTime = CultimaPanel.isNight;
      return !isVampire && onBed && rightTime;
   }

   //post: returns the total number of spouses across the world - used to trigger infidelity event
   public static int countSpouses()
   {
      int count = 0;
      for(ArrayList<NPCPlayer> c:CultimaPanel.civilians)   
         for(NPCPlayer p: c)
         {
            if(p.getName().startsWith("+"))
               count++;
         }
      return count;
   }
   
   //post: returns Player's spouse in their current location, null if there is none
   public NPCPlayer spouseInLocation()
   {
      for(NPCPlayer p: CultimaPanel.civilians.get(this.getMapIndex()))
      {
         if(p.getName().startsWith("+"))
            return p;
      }
      return null;
   }

   public void setCamping(boolean state)
   {
      if(this.hasEffect("sullied") || this.hasEffect("seduced"))
      {
         this.removeEffect("sullied");
         this.removeEffect("seduced");
      }
      this.setOnGuard(false);
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));   
      String current = "";
      if(!LocationBuilder.outOfBounds(currMap, row, col))
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
      //see if we are in our own home to give us faster healing/manna recovery
      boolean inHome = false;
      boolean withSpouse = false;
      NPCPlayer spouse = null;
      for(int r=row-5; r<=row+5; r++)
         for(int c=col-5; c<=col+5; c++)
            if(!LocationBuilder.outOfBounds(currMap, r, c))
            {
               String carpet = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();  
               if(carpet.contains("purple_floor_inside"))
               {
                  inHome = true;
                  break;
               }
            }
      if(inHome)
      {
         for(NPCPlayer p:CultimaPanel.adjacentTalkableNPCs)
            if(p!=null && p.getName().startsWith("+"))
            {
               withSpouse = true;
               spouse = p;
               break;
            }
      }     
      if(state)
      {
         checkForSpellLearned();
         camping = true;
         setImageIndexTemp(CAMP);
         this.setKeyTaps((byte)0);     //to count the number of key taps to try to wake
         if(current.contains("bed"))
         {
            if(current.contains("coffin"))
               setImageIndexTemp(COFFIN);
            else
               setImageIndexTemp(SLEEP);   //no fire in bed
         }
         campStartTime = CultimaPanel.time;
         this.clearActiveSpells();
         Sound.getOnHorse();        //sound
         if(inHome && withSpouse)
         {
            int numRations = (byte)(Utilities.rollDie(5,15));
            this.addRations((byte)(numRations));
            
            if(spouse != null)
            {   //see if there is more than 1 spouse, roll to see if they find out, if so-set numInfo to 13
               if(this.hasItem("viperglove") && Math.random() < 0.5)
               {
                  spouse.addEffect("poison");
                  CultimaPanel.sendMessage(Utilities.shortName(spouse.getName().substring(1))+" has been poisoned by thy Viperglove!");
               }
               else
                  CultimaPanel.sendMessage(Utilities.shortName(spouse.getName().substring(1))+" has prepared "+numRations+" rations for you tomorrow.");
               int numSpouses = countSpouses();
               if(numSpouses >= 2)
               {
                  int roll =Utilities.rollDie(5000 - (numSpouses*100));
                  if(roll < Math.abs(reputation))
                     spouse.setNumInfo(13);
               }
            }         
         }
      }
      else           //state is false
      {
         camping = false;
         setImageIndex(imageIndex);
         int numHours = 0;
         if(isVampire())
         {
            numHours = (int)(CultimaPanel.time - campStartTime);
         }
         else
         {
            if(campStartTime >= 20)    //camped before midnight
               numHours = (int)(24-campStartTime) + 7;
            else
               numHours = (int)(7 - campStartTime);
         }
         double healPerc = 0.05;
         if(!dogBasicInfo.equals("none") || current.contains("bed"))    //we get better rest if we have a dog guarding us while camping, or we are in a bed
            healPerc = 0.075;
         if(!dogBasicInfo.equals("none") && current.contains("bed"))    //we get best rest if we have a dog guarding us while camping and we are in a bed
            healPerc = 0.1;
         if(inHome && current.contains("bed") && !withSpouse)
            healPerc = 0.15;    
         if((isVampire() && current.contains("coffin")) || (inHome && current.contains("bed") && withSpouse))
            healPerc = 0.2;     
         for(int i=0; i<numHours; i++)      
            heal(Math.max(1,(int)(getHealthMax()*healPerc)));
            
         double mannaBoost = 1.0;
         if(hasItem("mannastone"))
            mannaBoost++;
         if(hasItem("Ohnyalei"))
            mannaBoost++;   
         if(!dogBasicInfo.equals("none"))
            mannaBoost+=0.5;
         if(inHome)
         {
            if(withSpouse)
               mannaBoost+=0.75;
            else
               mannaBoost+=0.5;    
         }        
         setManna((int)(this.getManna() + numHours*(this.getMannaMax()/8)*mannaBoost));
         //end any timed spells
         this.clearActiveSpells();
         if(isVampire() && current.contains("coffin") && numHours >= 5)
         {
            this.setBody(this.getHealthMax());
            manna = this.getMannaMax();
         }   
         if(!dogBasicInfo.equals("none"))   //restore our dog's health
         {
            NPCPlayer dog = Utilities.getDog();
            if(dog!=null)
            {
               dog.setBody(Math.min((int)(dog.getBody()*1.5) , dog.getMight()*5)); 
               dog.clearEffects();     
            }
         }
         if(!npcBasicInfo.equals("none"))   //restore our npc's health
         {
            NPCPlayer npc = Utilities.getNpc();
            if(npc!=null)
            {
               npc.setBody(Math.min((int)(npc.getBody()*1.5) , npc.getMight()*5)); 
               npc.clearEffects();     
            }
         }
      }
   }
   
   public boolean hasMagicStaff()
   {
      return (this.hasItem("Staff") || this.hasItem("Magestaff") || this.hasItem("Ohnyalei"));
   }
   
   public boolean hasLegendaryWeapon()
   {
      return (this.hasItem("forsetis-axe") ||
              this.hasItem("mjolnir") || 
              this.hasItem("bow-of-karna") || 
              this.hasItem("excalibur") || 
              this.hasItem("ame-no-nuhoko") ||
              this.hasItem("gada-torchmace") ||
              this.hasItem("khatvanga") ||
              this.hasItem("gungnir") ||
              this.hasItem("carnwennan") || 
              this.hasItem("ohnyalei"));
   }
   
   public void checkForSpellLearned()
   {
      if(!hasMagicStaff())
         return;
      byte [][] currMap = (CultimaPanel.map.get(mapIndex));
      String currTerr = "";
      if(!LocationBuilder.outOfBounds(currMap, this.getRow(), this.getCol()))
         currTerr = CultimaPanel.allTerrain.get(Math.abs(currMap[this.getRow()][this.getCol()])).getName().toLowerCase();
      String locType = location.toLowerCase();
      if(!this.hasSpell("floretlade") && this.hasItem("floretbox") && this.enoughFlowers())
      {
         if(currTerr.contains("flower") && CultimaPanel.weather == 0)
         {
            CultimaPanel.sendMessage("You learn the Floretlade spell!");
            this.addSpell(Spell.getFloretLade());
            Sound.castSpell();
         }
      }
      if(!this.hasSpell("tempest"))
      {
         if(currTerr.contains("hills") && CultimaPanel.weather != 0)
         {
            CultimaPanel.sendMessage("You learn the Tempest spell!");
            this.addSpell(Spell.getTempest());
            Sound.thunder();
         }
      }
      if(!this.hasSpell("timestop") && this.isInSpecialRealm())
      {
         CultimaPanel.sendMessage("You learn the Timestop spell!");
         this.addSpell(Spell.getTimeStop());
         Sound.thunder();
      }
      if(!this.hasSpell("possess") && locType.contains("temple"))
      {
         Corpse corpse = CultimaPanel.onAnyCorpse;
         if(corpse!=null && (corpse.getCharIndex()==NPC.DEMON || corpse.getCharIndex()==NPC.DEMONKING) && CultimaPanel.weather == 0)
         {
            CultimaPanel.sendMessage("You learn the Possess spell!");
            this.addWeapon(Weapon.getPossess());
            Sound.thunder();
         }
      }
      if(!this.hasSpell("tame-animal") && CultimaPanel.weather==0 && this.getArmor().getName().toLowerCase().contains("pelt"))
      {
         Corpse corpse = CultimaPanel.onAnyCorpse;
         if(corpse!=null && NPC.isTameableAnimal(corpse.getCharIndex()) && CultimaPanel.weather == 0)
         {
            CultimaPanel.sendMessage("You learn the Tame-animal spell!");
            this.addSpell(Spell.getTameAnimal());
            Sound.castSpell();
         }
      }
      if(!this.hasSpell("firestorm"))
      {
         Corpse corpse = CultimaPanel.onAnyCorpse;
         if(corpse!=null && (corpse.getCharIndex()==NPC.DRAGON || corpse.getCharIndex()==NPC.DRAGONKING) && CultimaPanel.weather == 0)
         {
            CultimaPanel.sendMessage("You learn the Firestorm spell!");
            this.addSpell(Spell.getFirestorm());
            Sound.thunder();
         }
      }
      if(!this.hasSpell("raise-dead"))
      {
         Corpse corpse = CultimaPanel.onAnyCorpse;
         if(corpse!=null && (corpse.getCharIndex()==NPC.GHOST || corpse.getCharIndex()==NPC.PHANTOM))
         {
            CultimaPanel.sendMessage("You learn the Raise-dead spell!");
            this.addSpell(Spell.getRaiseDead());
            Sound.thunder();
         }
      }
   }

   public ArrayList<String> getItems()
   {
      return items;
   }

   public void clearItems()
   {
      items.clear();
   }

   public void clearMagicItems()
   {
      for(int i=items.size()-1; i>=0; i--)
      {
         String it = items.get(i).toLowerCase();
         if(it.contains("bounty") || it.contains("message") || it.contains("lockpick") || it.contains("manual") || it.contains("head") || it.contains("book") || it.contains("puzzle") || it.contains("treasuremap") || it.contains("egg"))
            continue;
         items.remove(i);
      }
   }

   public void clearItemsExceptBounties()
   {
      for(int i=items.size()-1; i>=0; i--)
      {
         String it = items.get(i).toLowerCase();
         if(it.contains("bounty") || it.contains("message") || it.contains("manual") || it.contains("head"))
            continue;
         items.remove(i);
      }
   }
   
   public void clearUnnaturalItems()
   {
      for(int i=items.size()-1; i>=0; i--)
      {
         String it = items.get(i).toLowerCase();
         if(it.contains("bounty") || it.contains("message") || it.contains("head")  || it.contains("essence") || it.contains("songpage") || it.contains("book") || it.contains("puzzle") || it.contains("mindtome") || it.contains("treasuremap") || it.contains("egg")  || it.contains("rune") || Item.itemIsStone(it) || items.get(i).toLowerCase().contains("manual"))
            continue;
         items.remove(i);
      }
   }

   public void clearMissionItems()
   {
      for(int i=items.size()-1; i>=0; i--)
      {
         String it = items.get(i).toLowerCase();
         if(it.contains("message") || it.contains("bounty") || it.contains("head") || it.contains("rune"))
            items.remove(i);
      }     
   }
   
   public void clearTreasureMaps()
   {
      for(int i=items.size()-1; i>=0; i--)
         if(items.get(i).toLowerCase().contains("treasuremap"))
            items.remove(i);
   }

//clear all weapons except the staff and torch
   public void clearWeapons()
   {
      for(int i=0; i<weapons.length; i++)
      {
         if(i==NONE || i==STAFF || i==TORCH || i==THROWN)
            continue;
         weapons[i] = new ArrayList<Weapon>();
      }
      imageIndex = NONE;
      weaponSelect = 0;
      setImageIndex(NONE);
      Sound.dropItem();
   }

//clear all magic weapons except the staff
   public void clearMagicWeapons()
   {
      for(int i=0; i<weapons.length; i++)
      {
         if(i==NONE || i==STAFF )
            continue;
         for(int j=weapons[i].size()-1; j>=0; j--)
         {    
            int freq = weaponFrequencies[i].get(j);
            String weap = weapons[i].get(j).getName().toLowerCase();
            if(weap.contains("blessed") || weap.contains("flame") || weap.contains("bright") || weap.contains("royal") || weap.contains("dual") || weap.contains("poison") || weap.contains("destiny"))
            {
               for(int k=0; k < freq; k++)
                  this.discardWeapon(weap);
            }
         }
      }
      imageIndex = NONE;
      weaponSelect = 0;
      setImageIndex(NONE);
      Sound.dropItem();
   }

//discards all magic armor 
   public void clearMagicArmor()
   {
      for(int i=armor.size()-1; i>=0; i--)
      {
         int freq = armorFrequencies.get(i);
         String arm = armor.get(i).getName().toLowerCase();
         if(arm.contains("blessed") || arm.contains("exotic") || arm.contains("dragon"))
         {
            for(int k=0; k < freq; k++)
               discardArmor(arm);
         }
      }
   }
   
   public void clearUnnaturalArmor()
   {
      for(int i=armor.size()-1; i>=0; i--)
      {
         int freq = armorFrequencies.get(i);
         String arm = armor.get(i).getName().toLowerCase();
         if(arm.contains("leather") || arm.contains("skin") || arm.contains("clothes")  || arm.contains("pelt") || arm.contains("dragon"))
            continue;
         for(int k=0; k < freq; k++)
            discardArmor(arm);
      }
   }

   public void clearArmor()
   {
      armor.clear();
      armor.add(Armor.getClothes());
      armorSelect = 0;
   }

 //post: returns the collection of locations of all owned treasure maps so an X can be drawn on each spot in Display
   public ArrayList<Coord> getTreasureMapLocs()
   {
      ArrayList<Coord> ans = new ArrayList();
      for(String it: items)
         if(it.startsWith("treasuremap"))
         {
            Coord treasureLoc = Item.getCoordFromTeasureMap(it);
            if(treasureLoc != null)
               ans.add(treasureLoc);  
         
         }
      return ans;
   }
   
   //post:  tries to remove the treasuremap that has the location of our current postition
   public void removeTreasureMap()
   {
      if(this.getMapIndex()!=0)
         return;
      int r = this.getRow();
      int c = this.getCol();
      for(int i=items.size()-1; i>=0; i--)
      {
         String it = items.get(i);
         if(it.startsWith("treasuremap"))
         {
            Coord treasureLoc = Item.getCoordFromTeasureMap(it);
            if(treasureLoc != null)
            {
               if(treasureLoc.getRow()==r && treasureLoc.getCol()==c)
               {
                  items.remove(i);
                  return;
               }
            }
         }
      }
   
   }

//post: adds the item with the name itemName - increase the frequency if we already have it
//items contains the item name, and if we have more than one, the frequency after a ":"
//"pentangle", "lockpick:2" means we have one pentangle and 2 lockpicks
   public boolean addItem(String item)
   {
      if(item.contains("meat") || item.contains("ration"))
      {  
         this.addRations((byte)1);
         return true; 
      }
      Weapon weap = Weapon.getWeaponWithName(item);
      if(weap != null && !item.toLowerCase().contains("sling"))
      {
         addWeapon(weap);
         return true;
      }
      item = item.toLowerCase();
      if(item.equals("blessed-crown") && items.contains("blessed-crown"))  //you only get one
         return false;
      if((item.contains("book") || item.contains("puzzle") || item.contains("manual")) && this.hasItem(item))                      //you can only have one copy of each book
         return false; 
      if(item.contains("swiftquill") && this.hasItem(item))                //you can only have one cswiftquill
         return false;  
      if(item.contains("dragonqueen-scales") && this.hasItem(item))  
         return false;   
      if(item.contains("dragon-scales") && this.hasItem(item))  
         return false; 
      if(item.contains("seaserpent-scales") && this.hasItem(item))  
         return false; 
      if(item.contains("allosaur-egg") && this.hasItem(item))  
         return false;
      if(item.contains("wisdom-egg") && this.hasItem(item))  
         return false;   
      if((this.isVampire() || this.isWerewolf()) && item.contains("pentangle") )
         return false; 
      if(items!=null)
      {  
         if(item.contains("talisman"))
         {
            String spellsLearned = "";
            if(!this.hasSpell("tempest"))
            {
               this.addSpell(Spell.getTempest());
               spellsLearned = "Tempest ";
            }
            if(!this.hasSpell("magicmist"))
            {
               this.addSpell(Spell.getMagicMist());
               spellsLearned += "MagicMist ";
            }
            if(!this.hasSpell("shiftwind"))
            {
               this.addSpell(Spell.getShiftWind());
               spellsLearned += "ShiftWind";
            }
            if(spellsLearned.length() > 0)
               CultimaPanel.sendMessage("You learned " + spellsLearned);
            //raise manna high enough to cast these spells
            int mannaMin = Math.max(Spell.getTempest().getMannaCost(), Spell.getMagicMist().getMannaCost());
            mannaMin = Math.max(mannaMin, Spell.getShiftWind().getMannaCost());
            if(this.getMannaMax() < mannaMin)
            {
               int mindTemp = mannaMin / 10;
               if(mind < mindTemp)
               {
                  mind = mindTemp;
               }
               manna = mannaMin;
            }
         }
         if(item.contains("treasuremap") || item.contains("bounty") || item.contains("message") || item.contains("crown") || item.contains("book") || item.contains("puzzle") || item.contains("manual") || items.size()==0)     //add bounties at the end of the list{
         {
            if(item.contains("treasuremap"))
            {
               Coord treasureLoc = Item.getCoordFromTeasureMap(item);  
               if(treasureLoc == null)
                  return false;
               items.add(item);
               TerrainBuilder.markMapArea(treasureLoc.getRow(), treasureLoc.getCol());
               int treasureRow = treasureLoc.getRow();
               int treasureCol = treasureLoc.getCol();
               //when you get a treasure map, make a nonPlayer NPC as a marker for the Knowing spell to be able to direct you to it
               NPCPlayer marker = new NPCPlayer(NPC.BEGGER, treasureRow, treasureCol, 0, "temp", "temp,1,1,1,10,0", false);
               CultimaPanel.worldMonsters.add(marker);
            }
            else
            {
               if(item.contains("crafting-manual"))
               {
                  if(getCraftingInfo() < NPC.gemInfo.length)
                     for(int i=0; i<NPC.gemInfo.length; i++)
                        CultimaPanel.player.nextCraftingInfo();
               }
               items.add(item);
            }
         }
         else
         {
            boolean added=false;
            for(int i=0; i<items.size(); i++)
            {
               String curr = items.get(i).toLowerCase();
               if(curr.contains(item))
               {
                  int pos = curr.indexOf(":");
                  if(pos==-1)
                     curr += ":2";
                  else
                  {
                     String freqTemp = curr.substring(pos+1);
                     int freq = Integer.parseInt(freqTemp.trim())+1;
                     curr = curr.substring(0,pos)+":"+freq;
                  }
                  items.set(i, curr);
                  added=true;
                  break;
               }
            }
            if(!added)
               items.add(0,item);
         }   
         if(item.contains("pentangle"))                     //pentangle guards against curse
         {
            removeEffect("curse");
            statMod = new int[3];
         }
         Item.sortItems(items);                   
         return true;   
      }
      return false;
   }

   public ArrayList<String> getEffects()
   {
      return effects;
   }

   public boolean addEffect(String eff)
   {
      eff = eff.toLowerCase();
      if(eff.contains("still"))
         return false;
      if(eff.contains("web"))
      {  //to count the number of key taps to try to escape
         this.setKeyTaps((byte)0);
      }   
      if(!this.hasItem("spidersweb") && this.hasMagicStaff() && ((this.hasEffect("poison") && eff.contains("web")) || (this.hasEffect("web") && eff.contains("poison"))))
      {
         this.addWeapon(Weapon.getSpidersWeb());
         CultimaPanel.sendMessage("You learn the Spidersweb spell!");
      }
      if((this.isVampire() || this.isWerewolf()) && (eff.contains("curse") || eff.contains("poison") ))     //Vampires can't be cursed or poisoned
      {
         return false;
      }
      if(hasItem("pentangle") && eff.contains("curse"))     //pentangle guards against curse
      {
         CultimaPanel.sendMessage("Your pentangle blocks a curse!");
         return false;
      }
      if(getArmor().getName().toLowerCase().contains("bless") && eff.contains("curse") && Math.random() < 0.5)     //blessed armor can guard against curse
      {
         CultimaPanel.sendMessage("Your blessed armor blocks a curse!");
         return false;
      }
      if(this.getArmor().getName().toLowerCase().contains("dragon") && eff.contains("fire"))
         return false;  
      if(hasItem("Seaserpent-scale") && eff.contains("freeze"))
         return false;  
      else if(effects!=null && !effects.contains(eff))
      {
         if(Math.random() < 0.5)
         {
            effects.add(eff);
         
            if(eff.contains("bless"))
            {
               if(!this.getLocationType().toLowerCase().contains("underworld"))
                  removeEffect("poison");
               removeEffect("curse");
               statMod = new int[3];
            }
            else if(eff.contains("curse"))
            {
               removeEffect("blessed");
               if(Math.random() < 0.5)
               {
                  effects.add("sullied");
                  this.setSulliedValue(Utilities.rollDie(0,200));
               }
            }
            painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
         
            return true;
         }
      }
      return false;
   }

//no blocking of the effect
   public boolean addEffectForce(String eff)
   { 
      if(eff.contains("still"))
         return false;
      if(!this.hasItem("spidersweb") && this.hasMagicStaff() && ((this.hasEffect("poison") && eff.contains("web")) || (this.hasEffect("web") && eff.contains("poison"))))
      {
         this.addWeapon(Weapon.getSpidersWeb());
         CultimaPanel.sendMessage("You learn the Spidersweb spell!");
      }
      
      if(!this.getLocationType().toLowerCase().contains("underworld") && (this.isVampire() || this.isWerewolf()) && (eff.contains("curse") || eff.contains("poison") ))     //Vampires can't be cursed or poisoned
      {
         return false;
      }
      if(eff.contains("curse") && effects!=null && effects.contains(eff) && !effects.contains("sullied"))
      {
         effects.add("sullied");
         this.setSulliedValue(Utilities.rollDie(0,200));
         painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
      }
      else if(effects!=null && !effects.contains(eff))
      {
         effects.add(eff);
         if(eff.contains("bless"))
         {
            if(!this.getLocationType().toLowerCase().contains("underworld"))
               removeEffect("poison");
            removeEffect("curse");
            statMod = new int[3];
         }
         else if(eff.contains("curse"))
         {
            removeEffect("blessed");
            effects.add("sullied");
            this.setSulliedValue(Utilities.rollDie(0,200));
         }
      
         painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
      
      }
      return true;
   }

   public boolean hasEffect()
   {
      return (effects.size() > 0);
   }

   public boolean hasEffect(String eff)
   {
      if(eff.contains("web")) //ends with value of the shooters's level
         eff = "web";
      return (effects!=null && effects.contains(eff));
   }

   public void removeEffect(String eff)
   {
      if(effects!=null && effects.contains(eff))
      {
         for(int i=0; i<effects.size(); i++)
            if(effects.get(i).equals(eff))
            {
               if(effects.get(i).contains("curse") || effects.get(i).contains("freeze"))
                  statMod = new int[3];
               effects.remove(i);
               break;
            }
      }
      painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
   }

//remove all effects
   public void removeEffects()
   {
      effects.clear();
   }

   public int getRations()
   {
      if(this.isVampire() && rations > 1)
         rations = 1;
      return rations;
   }

   public void setRations(byte r)
   {
      if(this.isVampire() && r > 1)
         r = 1;
      rations = (byte)(Math.abs(r));
   }

   public void addRations(byte r)
   {
      rations = (byte)(Math.min(rations+r, maxRations()));
      Sound.addRations();   
   }
   
   public byte maxRations()
   {
      if(this.hasItem("holdall"))
         return 50;
      return 15;
   }

   public void useRation()
   {
      if(rations > 0)
         rations--;
   }
   
   //returns the number of eggs we have
   public byte numEggs()
   {
      byte count=0;
      for(String item: items)
         if(item.toLowerCase().contains("egg"))
            count++;
      return count;
   }
   
   //returns the number of serpent eggs we have - for egg collecting mission
   public byte numSerpentEggs()
   {
      for(String item: items)
         if(item.toLowerCase().contains("serpent-egg"))
         {
            if(item.contains(":"))
            {
               String [] parts = item.split(":");
               if(parts.length == 2 && FileManager.wordIsInt(parts[1].trim()))
                  return Byte.parseByte(parts[1].trim());
            }
            return 1;
         }
      return 0;
   }
   
   public boolean canPickupEgg()
   {
      byte maxCapacity = 3;
      if(this.hasItem("holdall"))
         maxCapacity = 5;
      return (numSerpentEggs() < maxCapacity);
   }

   public ArrayList<Weapon>[] getWeapons()
   {
      return weapons;
   }

   public Weapon getWeapon()
   {
      if(imageIndex==WOLF)
         return Weapon.getThrashingBite();
      if(imageIndex==GREATSHIP)
         return Weapon.getGreatCannon();
      if(imageIndex==BRIGANDSHIP)
         return Weapon.getBrigandCannon();
      if(imageIndex==BOAT)
         return Weapon.getOar();
   
      if(onHorse())
      {
         if(onUnicorn())
            return Weapon.getBrightHorn();
         return Weapon.getStrikingHooves();
      }   
       
      if(imageIndex >= 0 && imageIndex < weapons.length && weaponSelect>=0 && weaponSelect < weapons[imageIndex].size())
      {
         Weapon ans = weapons[imageIndex].get(weaponSelect);
         String weapName = ans.getName().toLowerCase();
         if((weapName.contains("bow") || weapName.contains("caster")) && !weapName.contains("long") && ans.getRange() < 10 && this.hasItem("bow-bracer"))
         {
            if(weapName.contains("crossbow") || weapName.contains("caster"))
               ans.setRange(Weapon.CROSSBOW_RANGE + 1);
            else 
               ans.setRange(Weapon.BOW_RANGE + 1);
         }
         else if(weapName.contains("throwing-stone") && this.hasItem("sling"))
         {
            ans = Weapon.getSling();
         }
         return ans;
      }
      
      weaponSelect = 0;
      imageIndex = 0;   
      if(weapons==null || weapons.length==0 || weapons[imageIndex]==null || weapons[imageIndex].size()==0)
         return Weapon.getNone();
           
      return weapons[imageIndex].get(weaponSelect);
   }

   public int getWeaponFrequency()
   {
      if(imageIndex >= 0 && imageIndex < weapons.length && weaponSelect>=0 && weaponSelect < weapons[imageIndex].size())
         return weaponFrequencies[imageIndex].get(weaponSelect);
      if(this.isSailing() || imageIndex==WOLF)    
         return 1;
      weaponSelect = 0;
      imageIndex = 0;   
      return weaponFrequencies[imageIndex].get(weaponSelect);
   }

   public byte getWeaponSelect()
   {
      return weaponSelect;
   }

   public void setWeaponSelect(byte ws)
   {
      weaponSelect = ws;
   }

   public void scrollWeaponRight()
   {
      int numSellable = 0;
      if(CultimaPanel.menuMode == CultimaPanel.SHOPPE_ARMORY || CultimaPanel.menuMode == CultimaPanel.SHOPPE_TRADER || this.getLocationType().contains("pacrealm"))
      {//count how many weapon categories we have that are sellable to see if we can safely scroll to the next weapon that is sellable
         for(int i=0; i<weapons.length; i++)
            if(i!=NONE && i!=STAFF && i!=THROWN && weapons[i].size() > 0)
               numSellable++;  
         if(numSellable == 0)
            return;          
      }
      if(weaponSelect < weapons[imageIndex].size()-1)
         weaponSelect++;
      else
      {
         weaponSelect = 0;
         byte i = (byte)((imageIndex+1)%weapons.length);
         while(weapons[i].size()==0)
         {
            i = (byte)((i + 1) % weapons.length);
         }
         setImageIndex(i);
      }
      String weapName = weapons[imageIndex].get(weaponSelect).getName().toLowerCase();
      if((this.getImageIndex()==STAFF) && (!this.hasStaffReadied()) && (this.getMannaMax() < (weapons[imageIndex].get(weaponSelect).getMannaCost())))
         scrollWeaponRight();       //scroll to the next weapon if we don't have enough max-manna to cast this staff spell
      else if(this.getImageIndex()==THROWN && (weapName.contains("throwing-stone") || weapName.contains("sling")) && numStones < 1)
         scrollWeaponRight();       //scroll to the next weapon if we don't have any stones to throw
      else if( (CultimaPanel.menuMode == CultimaPanel.SHOPPE_ARMORY || CultimaPanel.menuMode == CultimaPanel.SHOPPE_TRADER || this.getLocationType().contains("pacrealm")) && numSellable >= 1 && (this.getImageIndex()==STAFF || this.getImageIndex()==NONE || this.getImageIndex()==THROWN))
         scrollWeaponRight();     //scroll past things we can't sell
   }

   public void scrollWeaponLeft()
   {
      int numSellable = 0;
      if(CultimaPanel.menuMode == CultimaPanel.SHOPPE_ARMORY || CultimaPanel.menuMode == CultimaPanel.SHOPPE_TRADER || this.getLocationType().contains("pacrealm"))
      {//count how many weapon categories we have that are sellable to see if we can safely scroll to the next weapon that is sellable
         for(int i=0; i<weapons.length; i++)
            if(i!=NONE && i!=STAFF && i!=THROWN && weapons[i].size() > 0)
               numSellable++;  
         if(numSellable == 0)
            return;    
      }
      if(weaponSelect > 0)
         weaponSelect--;
      else
      {
         weaponSelect = 0;
         byte i = (byte)((imageIndex-1));
         if(i==-1)
            i = (byte)(weapons.length-1);
         while(weapons[i].size()==0)
         {
            i--;
            if(i==-1)
               i = (byte)(weapons.length-1);
         }
         weaponSelect = (byte)(weapons[i].size()-1);
         setImageIndex(i);
      }
      String weapName = weapons[imageIndex].get(weaponSelect).getName().toLowerCase();
      if((this.getImageIndex()==STAFF) && (!this.hasStaffReadied()) && (this.getMannaMax() < (weapons[imageIndex].get(weaponSelect).getMannaCost())))
         scrollWeaponLeft();        //scroll to the next weapon if we don't have enough max-manna to cast this staff spell
      else  if(this.getImageIndex()==THROWN && (weapName.contains("throwing-stone") || weapName.contains("sling")) && numStones < 1)
         scrollWeaponLeft();       //scroll to the next weapon if we don't have any stones to throw   
      else if( (CultimaPanel.menuMode == CultimaPanel.SHOPPE_ARMORY || CultimaPanel.menuMode == CultimaPanel.SHOPPE_TRADER || this.getLocationType().contains("pacrealm")) && numSellable >= 1 && (this.getImageIndex()==STAFF || this.getImageIndex()==NONE || this.getImageIndex()==THROWN))
         scrollWeaponLeft();     //scroll past things we can't sell  
   }

//return the spell for INVENTORY information
   public Spell getSpellInfo()
   {
      return getSpellInfo(spellSelect);
   }

//return the spell for INVENTORY information
   public Spell getSpellInfo(int select)
   {
      if(spells == null || spells.size()==0)
         return null;
      if(select>=0 && select < spells.size())
         return spells.get(select);
      return null;
   }

//return the active spell if we could cast it
   public Spell getSpell()
   {
      return getSpell(spellSelect);
   }

//return the active spell at index select if we could cast it
   public Spell getSpell(int select)
   {
      boolean indoors = false;
      String locType = this.getLocationType().toLowerCase();
      if(locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair"))
         indoors = true;
      if(getImageIndex()==WOLF || this.getLocationType().contains("pacrealm"))
         return null;  
      if(this.getArmor().getName().toLowerCase().contains("cloak"))   
         return null;
      if(select< 0 || spells==null || spells.size() == 0 || select >= spells.size())
         return null;
      Spell curr = getSpellInfo(select); 
      if(curr==null)
         return null; 
      if(curr.getName().contains("Charm") && this.getActiveSpell("Fear")!=null)
      {                             //we should not be able to cast charm while fear is an active spell, even if we have enough manna
         return null;
      }    
      if(spells.get(select).getName().contains("Floretlade") && (!this.hasItem("floretbox") || !this.enoughFlowers()))
         return null;               //can only cast floretlade if you have enough flowers
      if(this.getManna() < curr.getMannaCost())
      {
         if(this.isVampire() && (curr.getName().contains("Charm") || curr.getName().contains("Fear") || curr.getName().contains("Flight") || curr.getName().contains("Unseen"))) 
         {}                         //no manna cost for vampire's base spells
         else
            return null;  
      }
      if(spells.get(select).getName().contains("Cure") && !this.hasEffect("poison") && Utilities.adjacentHurtNPCs(2, true).size()==0)
         return null;               //can only cure if you are poisoned
      if(spells.get(select).getName().contains("Heal") && this.getBody() == this.getHealthMax() && Utilities.adjacentHurtNPCs(2, false).size()==0)
         return null;               //can only heal if you are damaged
      if(spells.get(select).getName().contains("Restore") && this.getBody() == this.getHealthMax() && this.getEffects().size()==0 && Utilities.adjacentHurtNPCs(2, false).size()==0)
         return null;               //can only restore if damaged or you have an affliction
      for(Spell s: activeSpells)    //can't cast if it is already active
         if(s.getName().equals(curr.getName()))
            return null;
      if(spells.get(select).getName().contains("Teleport") && (onHorse() || isSailing()))
         return null;               //teleport can only be cast if not on a horse or a boat
      if(spells.get(select).getName().contains("Flight") && (onHorse() || isSailing()))
         return null;               //flight can only be cast if not on a horse or a boat
      if(spells.get(select).getName().contains("Fear") && (onHorse() || isSailing()))
         return null;               //fear can only be cast if not on a horse or a boat
      if((spells.get(select).getName().contains("Raise-earth") || spells.get(select).getName().contains("Raise-water")) && indoors)
         return null;               //must be outdoors to raise earth or water from the terrain
      if((spells.get(select).getName().contains("Tempest") || spells.get(select).getName().contains("Firestorm") || spells.get(select).getName().contains("Shiftwind")) && indoors)  
         return null;               //must be outdoors to do a spell that affects the weather
      if((spells.get(select).getName().contains("Raise-dead")) && CultimaPanel.onAnyCorpse==null)
         return null;               //must be standing on a corpse to do a raise dead spell
      if((spells.get(select).getName().contains("Animate-stone")) && Utilities.nextToStatue()==null)
         return null;               //must be next to a statue to bring one to life     
      if(spells.get(select).getName().contains("Enchant-stone") && !Utilities.nextToStone())
         return null;               //must be next to a monolith to enchant it
      if(curr.getName().contains("Unseen") && ((CultimaPanel.time >=8 && CultimaPanel.time <= 16) || this.onHorse() || this.isSailing()))
         return null;               //unseen can only be cast at night and never while on a horse or a ship
      return spells.get(select);
   }

//returns the active spell with the given name
   public Spell getActiveSpell(String name)
   {
      if(imageIndex==Player.WOLF)
      {
         if(name.contains("Fear"))
            return Spell.getFear();
         if(name.contains("Focus"))
            return Spell.getFocus();
         if(name.contains("Knowing"))
            return Spell.getKnowing();
         return null;
      }
      if(activeSpells==null || activeSpells.size()==0)
         return null;
      for(Spell s:activeSpells)
         if(s.getName().equals(name))
            return s;
      return null;
   }

   //post: NPCs within a range of the player will be pushed away, applying an effect and damage: used for repel and flameblast spell
   public void pushNPCsAway(int range, String[] effect, int minDamage, int maxDamage, String spellName)
   {
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));
      //all directions to push all NPCs within a radius away, damage them slightly and give them an effect
      for(int i=0; i<3; i++)  //traverse through 2 pssible groups of NPCs: monsters and civilians
      {
         ArrayList<NPCPlayer> group = CultimaPanel.worldMonsters;
         if(i==1)
            group = CultimaPanel.civilians.get(this.getMapIndex());
         else if(i==2)
            group = CultimaPanel.furniture.get(this.getMapIndex());   
         for(NPCPlayer p: group)
         {
            if(p.getMapIndex()!=this.getMapIndex())
               continue;
            double dist = Display.findDistance(p.getRow(), p.getCol(), this.getRow(), this.getCol());
            if(dist > range)
               continue;   
            for(int eff=0; eff<effect.length; eff++)
            {
               double effectChance = 0.9;    //repel has a 90% chance of stunning the targets
               if(spellName.toLowerCase().contains("flameblast") && effect[eff].contains("stun"))
               {  //flameblast has a 90% chance of catching fire and a 95% chance of stunning
                  effectChance = 0.95;
               }
               if(Math.random() < effectChance && p.getCharIndex()!=NPC.BARREL && !p.isStatue())
                  p.addEffect(effect[eff]);
            }
            double damageChance = 0.9;    //repel has a 90% chance of damaging
            if(spellName.toLowerCase().contains("flameblast"))
            {  //flameblast has a 95% chance of damage
               damageChance = 0.95;
            }
            if(Math.random() < damageChance)
            {
               p.damage(Utilities.rollDie(minDamage,maxDamage),"");   
            }
            if(p.getRow()<this.getRow() && p.getCol()==this.getCol())   //right above player
            {
               int endRow = -1;
               int endCol = p.getCol();
               for(int r=p.getRow(); r>=this.getRow()-range; r--)
               {
                  endRow = r;
                  if(this.getMapIndex()==0)
                     endRow = CultimaPanel.equalizeWorldRow(r);
                  else if(LocationBuilder.outOfBounds(currMap, endRow, endCol))
                     break; 
                  if(LocationBuilder.isImpassable(currMap, endRow, endCol))   
                  {  //try to shift left or right
                     int left = endCol-1;
                     if(this.getMapIndex()==0)
                        left = CultimaPanel.equalizeWorldCol(left);
                     if(!LocationBuilder.isImpassable(currMap, endRow, left))
                        endCol = left;
                     else
                     {
                        int right = endCol+1;
                        if(this.getMapIndex()==0)
                           right = CultimaPanel.equalizeWorldCol(right);
                        if(!LocationBuilder.isImpassable(currMap, endRow, right))
                           endCol = right;
                     }   
                  }
                  if(!LocationBuilder.isImpassable(currMap, endRow, endCol))
                  {
                     p.setRow(endRow);
                     p.setCol(endCol);
                  }
               }
            }  
            else if(p.getRow()>this.getRow() && p.getCol()==this.getCol())   //right below player
            {
               int endRow = -1;
               int endCol = p.getCol();
               for(int r=p.getRow(); r<=this.getRow()+range; r++)
               {
                  endRow = r;
                  if(this.getMapIndex()==0)
                     endRow = CultimaPanel.equalizeWorldRow(r);
                  else if(LocationBuilder.outOfBounds(currMap, endRow, endCol))
                     break; 
                  if(LocationBuilder.isImpassable(currMap, endRow, endCol))   
                  {  //try to shift left or right
                     int left = endCol-1;
                     if(this.getMapIndex()==0)
                        left = CultimaPanel.equalizeWorldCol(left);
                     if(!LocationBuilder.isImpassable(currMap, endRow, left))
                        endCol = left;
                     else
                     {
                        int right = endCol+1;
                        if(this.getMapIndex()==0)
                           right = CultimaPanel.equalizeWorldCol(right);
                        if(!LocationBuilder.isImpassable(currMap, endRow, right))
                           endCol = right;
                     }   
                  }
                  if(!LocationBuilder.isImpassable(currMap, endRow, endCol))
                  {
                     p.setRow(endRow);
                     p.setCol(endCol);
                  }
               }
            }
            else if(p.getCol()<this.getCol() && p.getRow()==this.getRow())   //left of player
            {
               int endRow = p.getRow();
               int endCol = -1;
               for(int c=p.getCol(); c>=this.getCol()-range; c--)
               {
                  endCol = c;
                  if(this.getMapIndex()==0)
                     endCol = CultimaPanel.equalizeWorldCol(c);
                  else if(LocationBuilder.outOfBounds(currMap, endRow, endCol))
                     break; 
                  if(LocationBuilder.isImpassable(currMap, endRow, endCol))   
                  {  //try to shift up or down
                     int up = endRow-1;
                     if(this.getMapIndex()==0)
                        up = CultimaPanel.equalizeWorldRow(up);
                     if(!LocationBuilder.isImpassable(currMap, up, endCol))
                        endRow = up;
                     else
                     {
                        int down = endRow+1;
                        if(this.getMapIndex()==0)
                           down = CultimaPanel.equalizeCol(down);
                        if(!LocationBuilder.isImpassable(currMap, down, endCol))
                           endRow = down;
                     }   
                  }
                  if(!LocationBuilder.isImpassable(currMap, endRow, endCol))
                  {
                     p.setRow(endRow);
                     p.setCol(endCol);
                  }
               }
            }  
            else if(p.getCol()>this.getCol() && p.getRow()==this.getRow())   //right of player
            {
               int endRow = p.getRow();
               int endCol = -1;
               for(int c=p.getCol(); c<=this.getCol()+range; c++)
               {
                  endCol = c;
                  if(this.getMapIndex()==0)
                     endCol = CultimaPanel.equalizeWorldCol(c);
                  else if(LocationBuilder.outOfBounds(currMap, endRow, endCol))
                     break; 
                  if(LocationBuilder.isImpassable(currMap, endRow, endCol))   
                  {  //try to shift up or down
                     int up = endRow-1;
                     if(this.getMapIndex()==0)
                        up = CultimaPanel.equalizeWorldRow(up);
                     if(!LocationBuilder.isImpassable(currMap, up, endCol))
                        endRow = up;
                     else
                     {
                        int down = endRow+1;
                        if(this.getMapIndex()==0)
                           down = CultimaPanel.equalizeCol(down);
                        if(!LocationBuilder.isImpassable(currMap, down, endCol))
                           endRow = down;
                     }   
                  }
                  if(!LocationBuilder.isImpassable(currMap, endRow, endCol))
                  {
                     p.setRow(endRow);
                     p.setCol(endCol);
                  }
               }
            }  
            else if(p.getRow()<this.getRow() && p.getCol()<this.getCol())   //above-left of player
            {
               int endRow = -1;
               int endCol = -1;
               for(int r=p.getRow(), c=p.getCol(); r>=this.getRow()-range && c>=this.getCol()-range; r--, c--)
               {
                  endRow = r;
                  endCol = c;
                  if(this.getMapIndex()==0)
                  {
                     endRow = CultimaPanel.equalizeWorldRow(r);
                     endCol = CultimaPanel.equalizeWorldCol(c);
                  }
                  else if(LocationBuilder.outOfBounds(currMap, endRow, endCol))
                     break; 
                  if(LocationBuilder.isImpassable(currMap, endRow, endCol))   
                  {  //try to shift left or up
                     int left = endCol-1;
                     if(this.getMapIndex()==0)
                        left = CultimaPanel.equalizeWorldCol(left);
                     if(!LocationBuilder.isImpassable(currMap, endRow, left))
                        endCol = left;
                     else
                     {
                        int up = endRow-1;
                        if(this.getMapIndex()==0)
                           up = CultimaPanel.equalizeWorldCol(up);
                        if(!LocationBuilder.isImpassable(currMap, endRow, up))
                           endRow = up;
                     }   
                  }
                  if(!LocationBuilder.isImpassable(currMap, endRow, endCol))
                  {
                     p.setRow(endRow);
                     p.setCol(endCol);
                  }
               }
            }
            else if(p.getRow()<this.getRow() && p.getCol()>this.getCol())   //above-right of player
            {
               int endRow = -1;
               int endCol = -1;
               for(int r=p.getRow(), c=p.getCol(); r>=this.getRow()-range && c<=this.getCol()+range; r--, c++)
               {
                  endRow = r;
                  endCol = c;
                  if(this.getMapIndex()==0)
                  {
                     endRow = CultimaPanel.equalizeWorldRow(r);
                     endCol = CultimaPanel.equalizeWorldCol(c);
                  }
                  else if(LocationBuilder.outOfBounds(currMap, endRow, endCol))
                     break; 
                  if(LocationBuilder.isImpassable(currMap, endRow, endCol))   
                  {  //try to shift right or up
                     int right = endCol+1;
                     if(this.getMapIndex()==0)
                        right = CultimaPanel.equalizeWorldCol(right);
                     if(!LocationBuilder.isImpassable(currMap, endRow, right))
                        endCol = right;
                     else
                     {
                        int up = endRow-1;
                        if(this.getMapIndex()==0)
                           up = CultimaPanel.equalizeWorldCol(up);
                        if(!LocationBuilder.isImpassable(currMap, endRow, up))
                           endRow = up;
                     }   
                  }
                  if(!LocationBuilder.isImpassable(currMap, endRow, endCol))
                  {
                     p.setRow(endRow);
                     p.setCol(endCol);
                  }
               }
            }
            else if(p.getRow()>this.getRow() && p.getCol()<this.getCol())   //down-left of player
            {
               int endRow = -1;
               int endCol = -1;
               for(int r=p.getRow(), c=p.getCol(); r<=this.getRow()+range && c>=this.getCol()-range; r++, c--)
               {
                  endRow = r;
                  endCol = c;
                  if(this.getMapIndex()==0)
                  {
                     endRow = CultimaPanel.equalizeWorldRow(r);
                     endCol = CultimaPanel.equalizeWorldCol(c);
                  }
                  else if(LocationBuilder.outOfBounds(currMap, endRow, endCol))
                     break; 
                  if(LocationBuilder.isImpassable(currMap, endRow, endCol))   
                  {  //try to shift left or down
                     int left = endCol-1;
                     if(this.getMapIndex()==0)
                        left = CultimaPanel.equalizeWorldCol(left);
                     if(!LocationBuilder.isImpassable(currMap, endRow, left))
                        endCol = left;
                     else
                     {
                        int down = endRow+1;
                        if(this.getMapIndex()==0)
                           down = CultimaPanel.equalizeWorldCol(down);
                        if(!LocationBuilder.isImpassable(currMap, endRow, down))
                           endRow = down;
                     }   
                  }
                  if(!LocationBuilder.isImpassable(currMap, endRow, endCol))
                  {
                     p.setRow(endRow);
                     p.setCol(endCol);
                  }
               }
            }
            else if(p.getRow()>this.getRow() && p.getCol()>this.getCol())   //down-right of player
            {
               int endRow = -1;
               int endCol = -1;
               for(int r=p.getRow(), c=p.getCol(); r<=this.getRow()+range && c<=this.getCol()+range; r++, c++)
               {
                  endRow = r;
                  endCol = c;
                  if(this.getMapIndex()==0)
                  {
                     endRow = CultimaPanel.equalizeWorldRow(r);
                     endCol = CultimaPanel.equalizeWorldCol(c);
                  }
                  else if(LocationBuilder.outOfBounds(currMap, endRow, endCol))
                     break; 
                  if(LocationBuilder.isImpassable(currMap, endRow, endCol))   
                  {  //try to shift right or down
                     int right = endCol+1;
                     if(this.getMapIndex()==0)
                        right = CultimaPanel.equalizeWorldCol(right);
                     if(!LocationBuilder.isImpassable(currMap, endRow, right))
                        endCol = right;
                     else
                     {
                        int down = endRow+1;
                        if(this.getMapIndex()==0)
                           down = CultimaPanel.equalizeWorldCol(down);
                        if(!LocationBuilder.isImpassable(currMap, endRow, down))
                           endRow = down;
                     }   
                  }
                  if(!LocationBuilder.isImpassable(currMap, endRow, endCol))
                  {
                     p.setRow(endRow);
                     p.setCol(endCol);
                  }
               }
            }
         }
      }
   }

//cast the spell
   public Spell castSpell()
   {
      return castSpell(spellSelect);
   }

//cast the spell at index selected (without changing to that selected spell - used for hot-keys)
   public Spell castSpell(byte selected)
   {
      if(this.getBody() <=0 || this.isWolfForm())
         return null;  
      Spell curr = getSpell(selected);
      int restoreMannaCost = 0;
      if(curr != null)
      {  
         CultimaPanel.sendMessage("~"+Spell.getSpellCast(curr.getName()));
         boolean indoors = false;      //used to see if fire is less potent in the rain, or freeze more potent in the rain
         String locType = location.toLowerCase();
         if(locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair"))
            indoors = true;
         if((curr.getName().contains("Tempest") || curr.getName().contains("Firestorm")) && indoors)  
            return null; 
         if(curr.getName().contains("Fear") && (onHorse() || isSailing()))
            return null;
         if(curr.getName().contains("Enchant-stone") && !Utilities.nextToStone())
            return null;   
         specificExperience[MIND]++;
         if(curr.getDuration() > 0) //duration spell
         {
            curr.setTimeLeft(curr.getDuration());
            activeSpells.add(curr);
            if(curr.getName().contains("Timestop"))
            {
               CultimaPanel.flashColor = Color.orange;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
               Sound.timestop();
            }
            else if(curr.getName().contains("Firestorm"))
            {
               CultimaPanel.flashColor = new Color(255,Utilities.rollDie(128,255),0);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
               Sound.thunder();
               CultimaPanel.weather = (byte)(Math.min(10, (this.getMind())/5 + Utilities.rollDie(1,3)));
               Utilities.firestorm();
            }
            else if(curr.getName().contains("Light"))
            {
               CultimaPanel.flashColor = Color.white;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
            }
            else if(curr.getName().contains("Magicmist"))
            {
               CultimaPanel.flashColor = new Color(180,180,180);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 2;
               CultimaPanel.fogAlpha = 1;
               CultimaPanel.fogFrame = CultimaPanel.numFrames + ((CultimaPanel.animDelay*3) * Utilities.rollDie(100,150));
               Sound.thunder();
            }
            else if(curr.getName().contains("Flight"))
            {
               CultimaPanel.flashColor = Color.white;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               if(this.isVampire())
                  setImageIndexTemp(BAT);
               else
                  setImageIndexTemp(FLIGHT);
               Sound.castFlightSpell();
            }
            else if(curr.getName().contains("Fear"))
            {
            //BUGBEAR = 15, GHOST = 16;
            //TROLL = 17,   CYCLOPS = 18;
            //DEMON = 19,   DRAGON = 20;
               CultimaPanel.flashColor = new Color(20,20,1);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 2;
               if(this.isVampire())
                  setImageIndexTemp(DEMON);
               else
               {
                  byte illusion = -1;
                  byte [] illusions = {BUGBEAR, GHOST, TROLL, CYCLOPS, DEMON, DRAGON};
                  if(Math.random() < 0.75)   //75% of the time, we get an illusion depending on our mind skill
                  {
                     if(getMind() <= 16)
                        illusion = illusions[(int)(Math.random()*2)];
                     else if(getMind() <= 32)
                        illusion = illusions[Utilities.rollDie(2,3)];
                     else
                        illusion = illusions[Utilities.rollDie(4,5)];
                  }
                  else
                     illusion = Utilities.getRandomFrom(illusions);
                  setImageIndexTemp(illusion);
               }
               magicSmoke(SmokePuff.dustCloud);
               Sound.castFearSpell();
            }
            else if(curr.getName().contains("Charm"))
            {
               CultimaPanel.flashColor = new Color(255,192,203);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
               Sound.castSpell();
            }
            else
            {
               Sound.castSpell();
            }
         }   
         else                       //instant spell
         {
            if(curr.getName().contains("Teleport"))
            {
               CultimaPanel.flashColor = new Color(10,200,255);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               if(this.getMapIndex() > 0)
               {  //teleport from the indoor location to world coordinates of where we entered
                  this.setRow(this.getWorldRow());
                  this.setCol(this.getWorldCol());
                  this.setMapIndex(0);
               }
               else
               {
                  if(this.getMapMarkRow()!=-1 && this.getMapMarkCol()!=-1)
                  {  //if we have a map-mark, teleport to it
                     this.setRow(this.getMapMarkRow());
                     this.setCol(this.getMapMarkCol());
                     this.setMapIndex(0);
                  }
                  else
                  {  //telep to random spot in world
                     Coord teleSpot = Utilities.pickRandomSpot(this.getRow(), this.getCol(), CultimaPanel.SCREEN_SIZE*2);
                     if(teleSpot != null)
                     {
                        this.setRow(teleSpot.getRow());
                        this.setCol(teleSpot.getCol());
                     }
                     else
                     {
                        CultimaPanel.sendMessage("Spell failed!");
                     }
                  }
               }
               this.setManna(-100);
               Sound.teleport();
               magicSmoke(SmokePuff.dustCloud);
            }
            else if(curr.getName().contains("Flameblast"))
            {
               CultimaPanel.flashColor = Color.orange;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               int range = 3;
               if(mind > 30)
                  range++;
               if(mind > 40)
                  range++;
               String[] effect ={"stun", "fire"};
               int minDamage = this.getMind()/2;
               int maxDamage = this.getMind();
               pushNPCsAway(range, effect, minDamage, maxDamage, curr.getName());
               this.setManna(0);
               Sound.explosion();
               magicSmoke(SmokePuff.dustCloud);
            }
            else if(curr.getName().contains("Repel"))
            {
               CultimaPanel.flashColor = Color.cyan;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               int range = 3;
               if(mind > 30)
                  range++;
               if(mind > 40)
                  range++;
               String[] effect = {"stun"};
               int minDamage = 2;
               int maxDamage = 2*(this.getMind()/3);
               pushNPCsAway(range, effect, minDamage, maxDamage, curr.getName());
               Sound.explosion();
               magicSmoke(SmokePuff.dustCloud);
            }
            else if(curr.getName().contains("Raise-dead"))
            {
               Corpse corpse = CultimaPanel.onAnyCorpse;
               if(corpse!=null)
               {
                  CultimaPanel.flashColor = Color.black;
                  CultimaPanel.flashFrame = CultimaPanel.numFrames;
                  CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
                  CultimaPanel.sendMessage("The dead rises!");
                  this.setManna(0);
                  NPCPlayer minion = new NPCPlayer(corpse.getCharIndex(), this.getRow(), this.getCol(), this.getMapIndex(), this.getLocationType());
                  minion.setBody(this.getMind()*2);
                  minion.setMind(1);
                  minion.setName(this.getShortName()+"'s Minion");
                  minion.addEffect("control");
                  minion.addEffect("poison");
                  minion.setCanWalkAndSwim(true);
                  CultimaPanel.worldMonsters.add(minion);
                  Sound.thunder();
                  //remove the corpse
                  if(CultimaPanel.corpses != null)
                     for(int mi=0; mi<CultimaPanel.corpses.size(); mi++)
                        for(int i=(CultimaPanel.corpses.get(mi)).size()-1; i>=0; i--)
                        {
                           Corpse d = (CultimaPanel.corpses.get(mi)).get(i);
                           if(d==corpse)
                           {
                              (CultimaPanel.corpses.get(mi)).remove(i);
                              break;
                           }
                        }
               }
               else
                  CultimaPanel.sendMessage("Spell failed!");
            }
            else if(curr.getName().contains("Animate-stone"))
            {
               NPCPlayer statue = Utilities.nextToStatue();
               if(statue!=null)
               {
                  CultimaPanel.flashColor = Color.red;
                  CultimaPanel.flashFrame = CultimaPanel.numFrames;
                  CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
                  CultimaPanel.sendMessage("Thy statue springs to life!");
                  NPCPlayer minion = new NPCPlayer(statue.getCharIndex(), statue.getRow(), statue.getCol(), statue.getMapIndex(), statue.getLocationType());
                  minion.setBody(minion.getBody() + this.getMind()*2);
                  minion.setMight(minion.getMight() + this.getMind()/2);
                  minion.setMind(1);
                  minion.setName(this.getShortName()+"'s Stone Minion");
                  minion.addEffect("control");
                  minion.addEffect("poison");
                  minion.setHasMet(true);
                  if(!minion.canFly() && !minion.canSwim())
                  {
                     minion.setCanWalkAndSwim(true);
                  }
                  CultimaPanel.worldMonsters.add(minion);
                  Sound.thunder();
                  Utilities.removeStatueAt(statue.getRow(), statue.getCol(), statue.getMapIndex());                     
               }
               else
                  CultimaPanel.sendMessage("Spell failed!");
            }
            
            else if(curr.getName().contains("Shiftwind"))
            {
               CultimaPanel.flashColor = Color.cyan;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               CultimaPanel.windDir = (byte)((CultimaPanel.windDir+1)%4);
               CultimaPanel.sendMessage("The winds now blow to the "+Utilities.byteDirToString(CultimaPanel.windDir));
               boolean tornado = false;
               for(NPCPlayer p: CultimaPanel.worldMonsters)
               {
                  if(p.getCharIndex()==NPC.TORNADO && p.getNumInfo()!=0)
                  {
                     tornado = true;
                     p.setNumInfo(0);
                     p.addEffect("control");
                     p.setMoveType(NPC.ROAM);
                     p.setReputation(0);
                     p.setHasBeenAttacked(false);
                  }
               }
               if(tornado)
               {
                  CultimaPanel.sendMessage("Thy Wind Elemental retreats from the vortex!");
               }
               else if(CultimaPanel.fogAlpha > 0)    //winds blow the fog away
               {
                  CultimaPanel.fogFrame = 0;
                  CultimaPanel.sendMessage("Thy winds blow the fog away!");
               }
               else
                  CultimaPanel.sendMessage("The winds now blow to the "+Utilities.byteDirToString(CultimaPanel.windDir));
                  
               Sound.thunder();
            }
            else if(curr.getName().contains("Floretlade"))
            {
               CultimaPanel.flashColor = new Color(238,130,238);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               this.clearFlowers();
               this.setManna(Math.max(40,this.getMannaMax()));
               Sound.castSpell();
            }
            else if(curr.getName().toLowerCase().contains("tame-animal"))
            {
               CultimaPanel.flashColor = new Color(255,192,203);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
               NPCPlayer dog = Utilities.getDog();
               boolean tamed = false;
               for(NPCPlayer p: CultimaPanel.worldMonsters)
               {
                  if(NPC.isShip(p))
                  {
                     continue;
                  } 
                  double dist = Display.findDistance(this.getRow(), this.getCol(), p.getRow(), p.getCol());
                  if(this.getMapIndex()==p.getMapIndex() && NPC.isTameableAnimal(p.getCharIndex()) && dist<=CultimaPanel.flashRadius && p!=dog)
                  {
                     if(NPC.isUnusualMonster(p.getCharIndex()) && Utilities.rollDie(this.getMind()) <Utilities.rollDie(p.getLevel()))
                     {}
                     else
                     {
                        if(this.dogBasicInfo.equals("none") && ((p.getCharIndex()==NPC.WOLF && this.getArmor().getName().toLowerCase().contains("wolf")) || p.getCharIndex()==NPC.GUARD_DOG))
                        {
                           p.setMoveTypeTemp(NPC.CHASE);
                           this.stats[Player.DOGS_TRAINED]++;
                           this.setDogBasicInfo(p.basicInfo());
                           CultimaPanel.sendMessage("<pant...pant...>");
                           CultimaPanel.sendMessage("You have earned the trust of this "+NPC.characterDescription(p.getCharIndex()));
                        }
                        else
                        {
                           p.setNumInfo(999);
                           p.addEffect("control");
                        }
                        tamed = true;
                        break;
                     }
                  }
               }
               if(!tamed)
               {
                  for(NPCPlayer p: CultimaPanel.civilians.get(this.getMapIndex()))
                  {
                     if(NPC.isShip(p))
                     {
                        continue;
                     } 
                     double dist = Display.findDistance(this.getRow(), this.getCol(), p.getRow(), p.getCol());
                     if(this.getMapIndex()==p.getMapIndex() && NPC.isTameableAnimal(p.getCharIndex()) && dist<=CultimaPanel.flashRadius && p!=dog)
                     {
                        if(NPC.isUnusualMonster(p.getCharIndex()) && Utilities.rollDie(this.getMind()) <Utilities.rollDie(p.getLevel()))
                        {}
                        else
                        {
                           if(this.dogBasicInfo.equals("none") && ((p.getCharIndex()==NPC.WOLF && this.getArmor().getName().toLowerCase().contains("wolf")) || p.getCharIndex()==NPC.GUARD_DOG))
                           {
                              p.setMoveTypeTemp(NPC.CHASE);
                              this.stats[Player.DOGS_TRAINED]++;
                              this.setDogBasicInfo(p.basicInfo());
                              CultimaPanel.sendMessage("<pant...pant...>");
                              CultimaPanel.sendMessage("You have earned the trust of this "+NPC.characterDescription(p.getCharIndex()));
                           }
                           else
                           {
                              p.setNumInfo(999);
                              p.addEffect("control");
                           }
                           tamed = true;
                           break;
                        }
                     }
                  }
               }
               Sound.castSpell();
            }
            else if(curr.getName().contains("Heal"))
            {
               CultimaPanel.flashColor = new Color(255,10,255);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               heal(5);
               removeEffect("fire");
               ArrayList<NPCPlayer> adjacents = Utilities.adjacentHurtNPCs(2, false);
               for(NPCPlayer p:adjacents)
               {
                  p.removeEffect("fire");
                  p.heal(5);
               }  
               Sound.castSpell();
               NPCPlayer death = CultimaPanel.getDeathAbout;
               if(death!=null && Display.findDistance(death.getRow(), death.getCol(), this.getRow(), this.getCol()) <= CultimaPanel.flashRadius) 
               {  //healing close to death can damage and stun it
                  if(hasItem("blessed-crown"))
                  {
                     death.addEffectForce("stun");
                     if(death.getBody() > 5)
                        death.damage(5,"");
                  }
               }
            }
            else if(curr.getName().contains("Restore"))
            {
               CultimaPanel.flashColor = new Color(255,10,255);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
               
               int damageToHeal = this.getHealthMax() - this.getBody();
               int mannaTemp = this.getManna();
               int healCost = Spell.getHeal().getMannaCost();
               while(mannaTemp > healCost && this.getBody() < this.getHealthMax())
               {
                  if(Math.random() < 0.5)
                     heal(5);
                  else
                     heal(6);
                  mannaTemp -= healCost;
                  restoreMannaCost += healCost;
               }
               if(!this.getLocationType().toLowerCase().contains("underworld"))
                  removeEffect("poison");
               removeEffect("curse");
               removeEffect("sullied");
               removeEffect("fire");
               ArrayList<NPCPlayer> adjacents = Utilities.adjacentHurtNPCs(2, false);
               for(NPCPlayer p:adjacents)
               {
                  p.removeEffect("fire");
                  p.removeEffect("poison");
                  p.heal(20);
               } 
               Sound.castSpell();  
               NPCPlayer death = CultimaPanel.getDeathAbout;
               if(death!=null && Display.findDistance(death.getRow(), death.getCol(), this.getRow(), this.getCol()) <= CultimaPanel.flashRadius) 
               {  //healing close to death can damage and stun it
                  if(hasItem("blessed-crown"))
                  {
                     death.addEffectForce("stun");
                     if(death.getBody() > 20)
                        death.damage(20,"");
                  }
               } 
            }
            else if(curr.getName().contains("Cure"))
            {
               CultimaPanel.flashColor = new Color(10,255,10);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               if(!this.getLocationType().toLowerCase().contains("underworld"))
                  removeEffect("poison");
               ArrayList<NPCPlayer> adjacents = Utilities.adjacentHurtNPCs(2, true);
               for(NPCPlayer p:adjacents)
               {
                  p.removeEffect("poison");
               }
               Sound.castSpell();
            }
            else if(curr.getName().contains("Tempest"))
            {
               CultimaPanel.flashColor = Color.white;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
               Sound.thunder();
               CultimaPanel.weather = (byte)(Math.min(10, (this.getMind())/5 + Utilities.rollDie(1,3)));
               boolean tornado = false;
               for(NPCPlayer p: CultimaPanel.NPCinSight)
               {
                  if(p.getCharIndex()==NPC.TORNADO && p.getNumInfo()!=0)
                  {
                     tornado = true;
                     p.setNumInfo(0);
                     p.setReputation(0);
                     p.addEffect("control");
                     p.setMoveType(NPC.ROAM);
                  }
               }
               if(tornado)
               {
                  CultimaPanel.sendMessage("Thy Wind Elemental retreats from the vortex!");
               }
            }
            else if(curr.getName().contains("Raise-earth"))
            {
               CultimaPanel.flashColor = Color.white;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
               Sound.thunder();
               byte [][] currMap = (CultimaPanel.map.get(mapIndex));
               for(int r=this.getRow() - 2; r<=this.getRow()+2; r++)
               {
                  for(int c=this.getCol() - 2; c<=this.getCol() + 2; c++)
                  {
                     double dist = Display.distanceSimple(r, c, this.getRow(), this.getCol());
                     if(dist > 2)            //make likelyhood of skipping depend on distance
                        continue;
                     int checkRow = r;
                     int checkCol = c;   
                     if(mapIndex != 0)
                     {
                        if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
                           continue;   
                     }
                     else
                     {
                        checkRow = CultimaPanel.equalizeWorldRow(r);
                        checkCol = CultimaPanel.equalizeWorldCol(c);
                     }
                     String currTerr = CultimaPanel.allTerrain.get(Math.abs(currMap[checkRow][checkCol])).getName().toLowerCase();
                     if(currTerr.contains("deep_water") || currTerr.contains("rapid_water"))
                        currMap[checkRow][checkCol]=TerrainBuilder.getTerrainWithName("TER_V_$Shallow_water").getValue();
                     else if(currTerr.contains("shallow_water"))
                        currMap[checkRow][checkCol]=TerrainBuilder.getTerrainWithName("TER_S_$Bog").getValue();
                     else if(currTerr.contains("bog"))
                        currMap[checkRow][checkCol]=TerrainBuilder.getTerrainWithName("TER_$Grassland").getValue(); 
                  }
               } 
               boolean earthElem = false;
               for(NPCPlayer p: CultimaPanel.NPCinSight)
               {
                  if(p.getCharIndex()==NPC.EARTHELEMENTAL)
                  {
                     earthElem = true;
                     p.setNumInfo(0);
                     p.setReputation(0);
                     p.setMoveType(NPC.ROAM);
                  }
               }
               if(earthElem)
               {
                  CultimaPanel.sendMessage("You have earned respect of the Earth Elemental!");
               } 
            }
            else if(curr.getName().contains("Raise-water"))
            {
               CultimaPanel.flashColor = Color.white;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
               Sound.thunder();
               Utilities.raiseWater(this.getRow(), this.getCol(), this.getMapIndex());  
               boolean earthElem = false;
               for(NPCPlayer p: CultimaPanel.NPCinSight)
               {
                  if(p.getCharIndex()==NPC.EARTHELEMENTAL)
                  {
                     earthElem = true;
                     p.setNumInfo(0);
                     p.setReputation(0);
                     p.setMoveType(NPC.ROAM);
                  }
               }
               if(earthElem)
               {
                  CultimaPanel.sendMessage("You have earned respect of the Earth Elemental!");
               } 
            }
            else if(curr.getName().contains("Enchant-stone"))
            {
               CultimaPanel.flashColor = Color.gray;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
               Sound.thunder();
               boolean success = Utilities.enchantStone(); 
               if(!success)
               {
                  CultimaPanel.sendMessage("Spell failed!");
                  return curr;
               }
               else
               {
                  this.setManna(-100); 
                  boolean earthElem = false;
                  for(NPCPlayer p: CultimaPanel.NPCinSight)
                  {
                     if(p.getCharIndex()==NPC.EARTHELEMENTAL)
                     {
                        earthElem = true;
                        p.setNumInfo(0);
                        p.setReputation(0);
                        p.setMoveType(NPC.ROAM);
                     }
                  }
                  if(earthElem)
                  {
                     CultimaPanel.sendMessage("You have earned respect of the Earth Elemental!");
                  } 
               }
            }
         }  
         int mannaCost = curr.getMannaCost();
         if(curr.getName().contains("Restore") && restoreMannaCost > 0)
            mannaCost = restoreMannaCost;
         if(this.getActiveSpell("Alphamind")!=null || this.hasItem("Ohnyalei"))
            mannaCost = (int)(mannaCost*0.75);     //less manna cost if we have an alphamind potion active or the legendary staff
         if(this.hasItem("talisman") && (curr.getName().contains("Tempest") || curr.getName().contains("Shiftwind") || curr.getName().contains("Magicmist")))   
            mannaCost /= 2;
         if(this.isVampire() && (curr.getName().contains("Charm") || curr.getName().contains("Fear") || curr.getName().contains("Flight") || curr.getName().contains("Unseen"))) 
         {}    //no manna cost for Vampire's base spells
         else if(curr.getName().contains("Teleport") || curr.getName().contains("Raise-dead") || curr.getName().contains("Flameblast") || curr.getName().contains("Enchant-stone"))  //already set manna
         {}
         else  
            setManna(this.getManna() - mannaCost); 
      
         painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
      }
      if(CultimaPanel.flashColor != null && curr!=null)
      {
         int radius = Math.min(((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE),CultimaPanel.flashRadius*CultimaPanel.SIZE);
         int opacity = Math.max(75, Math.min(127, curr.getMannaCost()));
         int plX = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2)  - (radius/2);
         int plY = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2) - (radius/2);   
         CultimaPanel.smoke.add(new SmokePuff(plX, plY, radius, opacity, CultimaPanel.flashColor));
      }
      return curr;
   }

//cast the spell with a specific name, no manna needed (for potions)
   public Spell castSpell(String name)
   {
      if(getImageIndex()==WOLF)
         return null;
      Spell curr = Spell.getSpellWithName(name);
      if(curr != null)
      {
         if(curr.getName().contains("Fear") && (onHorse() || isSailing()))
            return null;
         if(curr.getName().contains("Enchant-stone") && !Utilities.nextToStone())
            return null; 
         specificExperience[MIND]++;
         if(curr.getDuration() > 0) //duration spell
         {
            curr.setTimeLeft(curr.getDuration());
            activeSpells.add(curr);
            if(curr.getName().contains("Flight"))
            {
               CultimaPanel.flashColor = Color.white;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               if(this.isVampire())
                  setImageIndexTemp(BAT);
               else
                  setImageIndexTemp(FLIGHT);
               Sound.castFlightSpell();
            }
            else if(curr.getName().contains("Fear"))
            {
            //BUGBEAR = 15, GHOST = 16;
            //TROLL = 17,   CYCLOPS = 18;
            //DEMON = 19,   DRAGON = 20;
               CultimaPanel.flashColor = new Color(20,20,1);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 2;
               byte illusion = -1;
               byte [] illusions = {BUGBEAR, GHOST, TROLL, CYCLOPS, DEMON, DRAGON};
               if(Math.random() < 0.75)   //75% of the time, we get an illusion depending on our mind skill
               {
                  if(getMind() <= 16)
                     illusion = illusions[(int)(Math.random()*2)];
                  else if(getMind() <= 32)
                     illusion = illusions[Utilities.rollDie(2,3)];
                  else
                     illusion = illusions[Utilities.rollDie(4,5)];
               }
               else
                  illusion = Utilities.getRandomFrom(illusions);
               setImageIndexTemp(illusion);
               magicSmoke(SmokePuff.dustCloud);
               Sound.castFearSpell();
            }
            else if(curr.getName().contains("Charm"))
            {
               CultimaPanel.flashColor = new Color(255,192,203);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
               Sound.castSpell();
            }
            else if(name.toLowerCase().contains("fireguard"))  //no sound for fireguard (when we light a fire, gives us 2 seconds of fire protection
            {
               CultimaPanel.flashColor = Color.yellow;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 32;
            }
            else
               Sound.castSpell();
         }   
         else                       //instant spell
         {
            if(curr.getName().contains("Heal"))
            {
               CultimaPanel.flashColor = new Color(255,10,255);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               heal(5);
               removeEffect("fire");
               ArrayList<NPCPlayer> adjacents = Utilities.adjacentHurtNPCs(2, false);
               for(NPCPlayer p:adjacents)
               {
                  p.removeEffect("fire");
                  p.heal(5);
               } 
               NPCPlayer death = CultimaPanel.getDeathAbout;
               if(death!=null && Display.findDistance(death.getRow(), death.getCol(), this.getRow(), this.getCol()) <= CultimaPanel.flashRadius) 
               {
                  if(hasItem("blessed-crown"))
                  {
                     death.addEffectForce("stun");
                     if(death.getBody() > 5)
                        death.damage(5,"");
                  }
               }
            }
            else if(curr.getName().contains("Restore"))
            {
               CultimaPanel.flashColor = new Color(255,10,255);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
               heal(20);
               if(!this.getLocationType().toLowerCase().contains("underworld"))
                  removeEffect("poison");
               removeEffect("curse");
               removeEffect("sullied");
               removeEffect("fire");
               ArrayList<NPCPlayer> adjacents = Utilities.adjacentHurtNPCs(2, false);
               for(NPCPlayer p:adjacents)
               {
                  p.removeEffect("fire");
                  p.removeEffect("poison");
                  p.heal(20);
               } 
               NPCPlayer death = CultimaPanel.getDeathAbout;
               if(death!=null && Display.findDistance(death.getRow(), death.getCol(), this.getRow(), this.getCol()) <= CultimaPanel.flashRadius) 
               {
                  if(hasItem("blessed-crown"))
                  {
                     death.addEffectForce("stun");
                     if(death.getBody() > 20)
                        death.damage(20,"");
                  }
               } 
            }
            else if(curr.getName().contains("Cure"))
            {
               CultimaPanel.flashColor = new Color(10,255,10);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
               if(!this.getLocationType().toLowerCase().contains("underworld"))
                  removeEffect("poison");
               ArrayList<NPCPlayer> adjacents = Utilities.adjacentHurtNPCs(2, true);
               for(NPCPlayer p:adjacents)
               {
                  p.removeEffect("poison");
               }
            }
            Sound.castSpell();
         }  
         painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
      }
      if(CultimaPanel.flashColor != null)
      {
         int radius = Math.min(((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE),CultimaPanel.flashRadius*CultimaPanel.SIZE);
         int opacity = Math.max(75, Math.min(127, curr.getMannaCost()));
         int plX = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2)  - (radius/2);
         int plY = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2) - (radius/2);   
         CultimaPanel.smoke.add(new SmokePuff(plX, plY, radius, opacity, CultimaPanel.flashColor));
      }
      return curr;
   }


   public ArrayList<Spell> getActiveSpells()
   {
      return activeSpells;
   }
   
   public void magicSmoke(Color c)
   {
      int radius = Utilities.rollDie(5,10);
      int opacity = Utilities.rollDie(75,127);
      int plX = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2)  - (radius/2);
      int plY = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2) - (radius/2);  
      for(int i=0; i<10 && CultimaPanel.smoke.size()<50; i++)
      {
         CultimaPanel.smoke.add(new SmokePuff(plX+Utilities.rollDie(-6,6), plY+Utilities.rollDie(-6,6), radius, opacity, c));
      }
   }

//decrement the counter for each active spell by 1 and remove those that are done
   public void countDownSpells()
   {
   
      for(int i=activeSpells.size()-1; i>=0; i--)
      {
         Spell curr = activeSpells.get(i);
         curr.countDown();
         if(curr.getName().contains("Firestorm") && CultimaPanel.weather > 0)
         {
            for(int j=0; j<Math.max(CultimaPanel.weather/2, 1); j++)
               Utilities.startRandomFire();
            CultimaPanel.weather--;
         }
         if(curr.getTimeLeft() < 0)
         {
            if(curr.getName().contains("Fear"))
            {
               setImageIndex(imageIndex);
               magicSmoke(SmokePuff.dustCloud);
            }
            if(curr.getName().contains("Flight"))
            {
               Terrain current = CultimaPanel.allTerrain.get(Math.abs(CultimaPanel.map.get(this.getMapIndex())[this.getRow()][this.getCol()]));
               String currentName = current.getName().toLowerCase();
               boolean indoors = false;
               String locType = this.getLocationType().toLowerCase();
               if(locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair"))
                  indoors = true; 
               if(!indoors && TerrainBuilder.isIndoorFloor(current))
                  continue;
               if((TerrainBuilder.habitablePlace(locType)|| locType.contains("arena")) && (currentName.contains("i_") || currentName.contains("door")))
                  continue;  
               setImageIndex(imageIndex);
            }
            if(curr.getName().contains("Timestop"))
               Sound.silence(3);  
            if(curr.getName().contains("Firestorm"))
               CultimaPanel.weather = 0; 
            activeSpells.remove(i);
         }
      }
   }

   public void removeSpell(String name)
   {
      for(int i=activeSpells.size()-1; i>=0; i--)
      {
         Spell curr = activeSpells.get(i);
         if(curr.getName().equals(name))
         {
            if(curr.getName().contains("Fear"))
            {
               setImageIndex(imageIndex);
               magicSmoke(SmokePuff.dustCloud);
            }
            activeSpells.remove(i);
            return;
         }
      }
   }

   public void clearActiveSpells()
   {
      setImageIndex(imageIndex);
      Sound.silence(3);
      activeSpells.clear();
   }

   //returns true if we are wearing some kind of animal hide that protects against cold
   public boolean wearingFur()
   {
      return (this.getArmor().getName().toLowerCase().contains("pelt") || this.getArmor().getName().toLowerCase().contains("holocaust-cloak") || this.getArmor().getName().toLowerCase().contains("seaserpent") || this.isWolfForm());
   }

   public void runEffects()
   {
      byte[][]currMap = CultimaPanel.map.get(this.getMapIndex());	
      String playerTerrain = "";
      if(this.getRow()>=0 && this.getCol()>=0 && this.getRow()<currMap.length && this.getCol()<currMap[0].length)
         playerTerrain = CultimaPanel.allTerrain.get(Math.abs(currMap[this.getRow()][this.getCol()])).getName().toLowerCase();
      boolean inForest = playerTerrain.contains("dense_forest");
      boolean sailing = this.isSailing();
      boolean flight = this.isFlying();
      boolean indoors = TerrainBuilder.indoors();
      boolean inGraboid = (this.getLocationType().contains("graboid") || this.getLocationType().contains("beast"));
      boolean nextToHeatSource = LocationBuilder.adjacentToHeatSource(currMap, this.getRow(), this.getCol());
      double winter = Display.distFromDeadWinter();
      NPCPlayer onUs = CultimaPanel.onUs;    //see if we are standing on fire 
      if(this.getBody() <= 0 && hasItem("life-pearl"))
      {
         if(CultimaPanel.numFrames % 50 == 0)
            CultimaPanel.sendMessage(Utilities.getRandomFrom(lifePearlMessage));
         if(CultimaPanel.numFrames >= this.deathTime + 250)
         {
            ArrayList<String>imageNames = this.getFileNames();
            boolean isShip = false;
            for(String s:imageNames)
               if(s.contains("ship") || s.contains("boat"))
                  isShip = true;
            boolean onHorse = false;
            for(String s:imageNames)
               if(s.contains("horse"))
                  onHorse = true;
            if(isShip || onHorse || sailing || onHorse())
            {
               this.setImageIndex(Player.NONE);
               this.setWeaponSelect((byte)(0));
            }    
            int hp = (int)(Math.random()*((might*3 + agility*2)-5)) + 5;
            this.setBody(hp);
            CultimaPanel.sendMessage("You have sprung back to life!");
            removeItem("life-pearl");
         }
      }
      if(this.getBody() <= 0)
         return;
      if(getSulliedValue() <= 0 && (hasEffect("sullied") || hasEffect("seduced")))
      {
         removeEffect("sullied");
         removeEffect("seduced");
      }
      if(hasEffect("sullied") && Math.random() < 0.25)
      {  //rain or snow falling helps sober us up faster
         addSulliedValue(-1*Math.max(1,CultimaPanel.weather/2));
      }  
      if(hasEffect("seduced") && Math.random() < 0.1 && !this.onHorse() && !this.isSailing())
      {  //if seduced, we keep dropping our guard
         this.setOnGuard(false);
         this.setImageIndex(Player.NONE);
         this.setWeaponSelect((byte)(0));
      }  
      if(onUs!=null && onUs.getCharIndex()==NPC.FIRE && !this.getArmor().getName().equalsIgnoreCase("holocaust-cloak"))
      {
         int damMod = 2;
         double catchOnFire = onUs.getPercentHealth()*1.5;
         if(!this.getArmor().getName().toLowerCase().contains("dragon"))
         {
            damMod *= 4;
            catchOnFire /= 4;
         }
         damage(1+(onUs.getBody()/damMod));
         if(this.getArmor().isFlamable())     
            catchOnFire *= 2;
         if(Utilities.rollDie(10) < catchOnFire)
            this.addEffect("fire");  
      }
      if(inGraboid && Math.random() < 0.25)
      {  //damage from being digested
         CultimaPanel.sendMessage(Utilities.getRandomFrom(graboid));
         damage(1);
      }
      if(!indoors && !sailing && winter > 0.33 && !this.wearingFur() && this.getImageIndex()!=TORCH && !nextToHeatSource && Math.random() < (winter/2.0) && CultimaPanel.weather >= 3)
      {  //damage from snow in winter without a pelt
         int damage = 1;
         CultimaPanel.sendMessage(Utilities.getRandomFrom(cold));
         damage(damage);
      }
      if(!sailing && !flight && winter > 0 && playerTerrain.contains("water") && !Display.frozenWater() && Math.random() < 0.25 && !this.getArmor().getName().toLowerCase().contains("seaserpent") && !this.getLocationType().toLowerCase().contains("underworld"))
      {  //damage from swimming in winter
         int damage = (int)(1 + winter*5);
         CultimaPanel.sendMessage(Utilities.getRandomFrom(coldWater));
         damage(damage);
      }
      if(hasEffect("poison") && Math.random() < 0.1)
      {  //damage from poison
         CultimaPanel.sendMessage(Utilities.getRandomFrom(poison));
         damage(1);
         if(!this.getLocationType().toLowerCase().contains("underworld") && Utilities.rollDie(MAX_STAT*100) < this.getHealthMax())                   //chance the poison runs its course
         {
            removeEffect("poison");
            CultimaPanel.sendMessage("Thy poison has run its course.");
         }
      }
      if((hasEffect("blessed") || hasBlessedItem()) && Math.random() < 0.015 && this.getBody() < this.getHealthMax())
      {
         if(this.isVampire() || this.isWerewolf())
         {
            CultimaPanel.sendMessage(Utilities.getRandomFrom(blessBurn));
            damage(Utilities.rollDie(10));
         }
         else if(!this.getLocationType().contains("underworld") && !Utilities.nextToAHauntedHouse(currMap, this.getRow(), this.getCol(), 3))
         {  //no effects from blessed items when in the underworld or near a haunted house
            CultimaPanel.sendMessage(Utilities.getRandomFrom(bless));
            heal(1);
         }
      }
      if(this.isVampire() && CultimaPanel.weather <=1 && (CultimaPanel.time >=8 && CultimaPanel.time <= 16) && Math.random() < 0.5)
      {
         if((CultimaPanel.weather == 1 && Math.random() < 0.5) || sailing || indoors || inForest)
         {}
         else
         {
            CultimaPanel.sendMessage(Utilities.getRandomFrom(sunBurn));
            int distFromNoon = (int)(Math.abs(12 - CultimaPanel.time));
            int burn = 1 + (12-distFromNoon);
            if(CultimaPanel.weather > 0)
               burn /= 2;
            if(this.getArmor().getName().toLowerCase().contains("dragon"))
               burn /= 2;
            damage(Math.max(1,burn));
         }
      }
      if(hasEffect("fire"))
      {
         CultimaPanel.sendMessage(Utilities.getRandomFrom(fire));
         damage(1);
         if(Utilities.rollDie(MAX_STAT*10) < this.getAgility() || this.getArmor().getName().toLowerCase().contains("dragon") || this.getActiveSpell("Fireskin")!=null)                   //chance the fire goes out
         {
            removeEffect("fire");
            CultimaPanel.sendMessage("Thy fire extinguishes!");
         }
      }
      if(hasEffect("freeze"))
      {
         statMod[2] = -1 * agility / 2;
         if(Math.random() < 0.25)
            damage(1);
         if(Utilities.rollDie(100) < this.getAgility() || this.getArmor().getName().contains("Seaserpent-scale"))                   
         {
            removeEffect("freeze");
         }
         if(Utilities.rollDie(MAX_STAT) < this.getAgility() && this.wearingFur())               
         {
            removeEffect("freeze");
         }
      }
      if(this.getArmor().getName().toLowerCase().contains("invisibility"))
      {
         if(Math.random() < 0.5)
         {
            CultimaPanel.sendMessage(Utilities.getRandomFrom(drain));
            damage(1);
         }
      }
      if(hasEffect("curse"))
      {
         int rand = (int)(Math.random()*3);
         if(rand == 0)
            statMod[0] = -1 * might / 4;
         else if(rand == 1)
            statMod[1] = -1 * mind / 4;
         else
            statMod[2] = -1 * agility / 4;
         boolean curseSong = false;   
         int numSongPages = this.getItemFrequency("Songpage");
         if(this.getImageIndex()==LUTE && numSongPages >= 2)
            curseSong = true;
         if(Utilities.rollDie(10000) < this.getMind() || curseSong)                   //chance the curse is purged
         {
            removeEffect("curse");
            removeEffect("sullied");
            removeEffect("seduced");
            CultimaPanel.sendMessage("Thy curse has been purged.");
         }
      }
   }

   public boolean hasScales()
   {
      return (this.hasItem("dragon-scales") || this.hasItem("dragonqueen-scales") ||this.hasItem("seaserpent-scales"));
   }

   public boolean hasBlessedItem()
   {
      if(getWeapon().getName().toLowerCase().contains("bless"))
         return true;
      if(getArmor().getName().toLowerCase().contains("bless"))
         return true;
      for(String itm: items)
         if(itm.contains("bless"))
            return true; 
      return false;
   }

   public void scrollSpellRight()
   {
      if(spells==null || spells.size()==0)
         return;
      if(spellSelect < spells.size()-1)
      {
         spellSelect++;
         return;
      }
      spellSelect = 0;
   }

   public void scrollSpellLeft()
   {
      if(spells==null || spells.size()==0)
         return;
      if(spellSelect >= 1)
      {
         spellSelect--;
         return;
      }
      spellSelect = (byte)(spells.size()-1);
   }

   public byte getSpellSelect()
   {
      return spellSelect;
   }

   public void setSpellSelect(byte ss)
   {
      if(ss>=0 && ss<spells.size())
         spellSelect = ss;
   }

//used to initialize potionSelect to the first potion in your inventory if you have at least one
   public byte setPotionSelect()
   {
      for(byte i=0; i<potions.length; i++)
         if(potions[i] > 0)
         {
            return i;
         }
      return 0;
   }

   public boolean canScrollPotion()
   {
      boolean flight = (this.getActiveSpell("Flight")!=null);
      boolean hasCloak = (this.getArmor().getName().toLowerCase().contains("cloak"));
      return (!flight && !this.isCamping() && !this.isWolfForm() && !hasCloak);
   }


//scroll to the next potion that is different from the previous
   public void scrollPotion()
   {
      if(numPotions() > 0)
      {
         potionSelect = (byte)((potionSelect + 1) % potions.length);
         while(potions[potionSelect] == 0)
            potionSelect = (byte)((potionSelect + 1) % potions.length);
      }
   }

   public int countPotion(String n)
   {
      n = n.toLowerCase();
      if(n.equals("health"))
         return potions[Potion.HEALTH];
      if(n.equals("cure"))
         return potions[Potion.CURE];
      if(n.equals("focus"))
         return potions[Potion.FOCUS];
      if(n.equals("protection"))
         return potions[Potion.PROTECTION];
      if(n.equals("speed"))
         return potions[Potion.SPEED];
      if(n.equals("strength"))
         return potions[Potion.STRENGTH];
      if(n.equals("invisibility"))
         return potions[Potion.INVISIBILITY];
      if(n.equals("alphamind"))
         return potions[Potion.ALPHAMIND];
      if(n.equals("fireskin"))
         return potions[Potion.FIRESKIN];
      return 0;
   }

   public Potion getPotion()
   {
      if(getImageIndex()==WOLF || this.getLocationType().contains("pacrealm"))
         return null;
      if(this.getArmor().getName().toLowerCase().contains("cloak"))   
         return null;
      if(this.getActiveSpell("Flight")!=null)
         return null;
      if(potions[potionSelect] > 0)
         return Potion.getPotionWithNum(potionSelect);
      return null;
   }

   public byte getPotionSelect()
   {
      return potionSelect;
   }

   public void setPotionSelect(byte ps)
   {
      if(ps >=0 && ps < potions.length)
         potionSelect = ps;
   }

//change potionSelect index to the potion with the given name
   public int setPotionSelect(String name)
   {
      potionSelect = Potion.getPotionNumWithName(name);
      if(potions[potionSelect] > 0)
         return potionSelect;
      return -1;
   }

   public Potion drinkPotion()
   {
      if(this.getBody() <= 0 || getImageIndex()==WOLF)
         return null;
      if(potions[potionSelect]>0)
      {
         Potion curr = getPotion();
         if(curr.getName().contains("Heal") && this.getBody()==this.getHealthMax())
            return null;
         if(curr.getName().contains("Cure") && !hasEffect("poison"))
            return null;
         if(curr.getName().contains("Invisibility") && (getActiveSpell("Fear")!=null || isSailing() || onHorse()))
            return null;   
         if(curr.getName().contains("Protection") || curr.getName().contains("Speed") || curr.getName().contains("Strength") || curr.getName().contains("Invisibility") || curr.getName().contains("Alphamind") || curr.getName().contains("Fireskin") || curr.getName().contains("Focus"))  
         {
            Spell spell = castSpell(curr.getName());
            if(spell == null)
               return null;
            potions[potionSelect]--;  
            potionSelect = setPotionSelect(); 
            return curr;
         }	
         if(curr.getName().contains("Heal"))       //gain 15% of health back
            heal((int)(getHealthMax() * .15));
         else if(curr.getName().contains("Cure"))
         {
            if(this.getLocationType().toLowerCase().contains("underworld"))
            {
               CultimaPanel.sendMessage("No effect!");
               painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
               potions[potionSelect]--;
               potionSelect = setPotionSelect();
               Sound.drinkPotion();
               return null;
            }
            else
               removeEffect("poison");
         }
         painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
         potions[potionSelect]--;
         potionSelect = setPotionSelect();
         Sound.drinkPotion();
         return curr;
      }
      return null;
   }
   
   public Potion discardPotion()
   {
      if(this.getBody() <= 0 || getImageIndex()==WOLF)
         return null;
      if(potions[potionSelect]>0)
      {
         Potion curr = getPotion();
         potions[potionSelect]--;
         potionSelect = setPotionSelect();
         Sound.drinkPotion();
         return curr;
      }
      return null;
   }
   
   public void discardPotion(String potionName)
   {
      int index = Potion.getPotionNumWithName(potionName);
      if(index >=0 && index < potions.length && potions[index]>0)
      {
         potions[index]--;
         potionSelect = setPotionSelect(); 
      }
   }

   public boolean hasPotion(String potionName)
   {
      int index = Potion.getPotionNumWithName(potionName);
      return (index >=0 && index < potions.length && potions[index] > 0);
   }

   public boolean canDropWeapon(Weapon weap)
   {
      String weapName = weap.getName().toLowerCase();
      if(weap.getImageIndex()==STAFF ||  weap.getImageIndex()==NONE || weap.getImageIndex()==THROWN)
      {
         return false;
      }
      return true;
   }

   public void discardCurrentWeapon()
   {
      String weapName = getWeapon().getName().toLowerCase();
      if(imageIndex==STAFF || (imageIndex==THROWN && !weapName.contains("grenade-of-antioch")))                      //can't get rid of fists or magic
         return;
      if(imageIndex==NONE && !weapName.contains("trap"))
         return;
      if(weapons[imageIndex].size() > 0 && weaponSelect>=0 && weaponSelect<weapons[imageIndex].size())
      {
         int frequency = weaponFrequencies[imageIndex].get(weaponSelect);
         if(frequency > 1)
            weaponFrequencies[imageIndex].set(weaponSelect, (frequency-1));
         else
         {
            Weapon dropped = weapons[imageIndex].remove(weaponSelect);
            weaponFrequencies[imageIndex].remove(weaponSelect);
            setImageIndex(NONE);
            weaponSelect = 0;
         }
         Sound.dropItem();
      }
   }
   
   public void putWeaponAway()
   {
      setImageIndex(NONE);
      weaponSelect = 0;
   }

//discard weapon with a certain name (weapName)
   public Weapon discardWeapon(String weapName)
   {
      Weapon dropped = null;
      for(int w=0; w<weapons.length; w++)
      {
         ArrayList<Weapon> weapTypes = weapons[w];
         for(int i=weapTypes.size()-1; i>=0; i--)
         {
            Weapon weap = weapTypes.get(i);
            if(weap.getName().toLowerCase().equals(weapName.toLowerCase()))
            {
               int frequency = weaponFrequencies[w].get(i);
               if(frequency > 1)
               {
                  weaponFrequencies[w].set(i, (frequency-1));
                  dropped = weapons[w].get(i);
               }
               else
               {
                  dropped = weapons[w].remove(i);
                  weaponFrequencies[w].remove(i);
                  setImageIndex(NONE);
                  weaponSelect = 0;
               }
               Sound.dropItem();
               return dropped;
            }
         }
      }
      return dropped;
   }

//discard armor with a certain name (armName)
   public Armor discardArmor(String armName)
   {
      Armor dropped = null;
      for(int i=0; i<armor.size(); i++)
      {
         Armor arm = armor.get(i);
         if(arm.getName().toLowerCase().equals(armName.toLowerCase()))
         {
            if(armorFrequencies.get(i) > 1)
               armorFrequencies.set(i, (armorFrequencies.get(i).intValue()-1));
            else
            {   
               dropped = armor.remove(i);
               armorFrequencies.remove(i);
               if(armorSelect == i)
                  armorSelect = 0;   
               else
               {
                  if(armorSelect > i)
                  {
                     armorSelect--;
                     if(armorSelect < 0 || armorSelect >= armor.size())
                        armorSelect = 0;
                  }
               }
            }
            Sound.dropItem();
            return dropped;
         }
      }
      return dropped;
   }

//returns the weaponSelect index of the new weapon added
   public int addWeapon(Weapon toAdd)
   {
      int NUM_SPELLS = 39;
      if((this.getWeapons()[Player.STAFF]).size() +this.getSpells().size()==NUM_SPELLS)
         Achievement.earnAchievement(Achievement.SPEAK_AND_SPELL);
      if(this.hasWeapon("Grenade-of-Antioch") && toAdd.getName().contains("Grenade-of-Antioch"))
      {                       //we only get one holy hand grenade
         return -1;
      }  
      byte imageInd = toAdd.getImageIndex();
      if(imageInd>=NONE && imageInd<weapons.length)
      {
         int temp = -1;       //index of where we find the weapon in the ArrayList at weapons[imageInd]
         boolean found = false;
         for(ArrayList<Weapon> weapTypes:weapons)
         {
            for(int i=0; i<weapTypes.size(); i++)
            {
               Weapon weap = weapTypes.get(i);
               if(weap.getName().equals(toAdd.getName()))
               {
                  temp = i;
                  found = true;
                  break;
               }
            }
            if(found)
               break;
         }  
         Sound.addWeapon();
         if(temp != -1)
         {
            int frequency = weaponFrequencies[imageInd].get(temp);
            weaponFrequencies[imageInd].set(temp, (frequency+1));
            return temp;
         }
         else
         {
            if(toAdd.getName().equals("Magestaff") || toAdd.getName().equals("Ohnyalei"))      //make sure magestaff goes to index 0 of STAFF arraylist
            {
               weapons[imageInd].add(0,toAdd);
               weaponFrequencies[imageInd].add(0,1);
               if(toAdd.getName().equals("Ohnyalei"))
               {  //if we are getting the legendary staff, lose any other staff we have (we can't have more than one)
                  this.discardWeapon("Staff");
                  this.discardWeapon("Magestaff");
               }
            }
            else
            {
               weapons[imageInd].add(toAdd);
               weaponFrequencies[imageInd].add(1);
            }
            return 0;
         }
      }
      return -1;
   }

//add a weapon and make it your active weapon
   public void addWeaponAndSwitch(Weapon toAdd)
   {
      byte imageInd = toAdd.getImageIndex();
      if(imageInd>=NONE && imageInd<weapons.length)
      {
         int temp = addWeapon(toAdd);
         if(temp != -1)
         {
            setImageIndex(imageInd);
            weaponSelect = (byte)(temp);
         }
      }
   }

   public void switchToWeapon(String weapName)
   {
      for(ArrayList<Weapon> weapTypes:weapons)
      {
         for(int i=0; i<weapTypes.size(); i++)
         {
            Weapon weap = weapTypes.get(i);
            if(weap.getName().toLowerCase().equals(weapName.toLowerCase()))
            {
               setImageIndex(weap.getImageIndex());
               weaponSelect = (byte)(i);
               return;
            }
         }
      } 
   }

   public boolean hasItem(String itemName)
   {
      itemName = itemName.toLowerCase();
      if(weapons!=null)
         for(ArrayList<Weapon> weapTypes:weapons)
            for(Weapon weap: weapTypes)
               if(weap.getName().toLowerCase().equals(itemName))
                  return true;
      if(armor!=null)
         for(Armor arm: armor)
            if(arm.getName().toLowerCase().equals(itemName))
               return true;
      if(spells!=null)
         for(Spell spell: spells)
            if(spell.getName().toLowerCase().equals(itemName))
               return true;   
      if(items!=null)             
         for(String itm: items)
         {
            if(itemName.equals("charm") && itm.toLowerCase().contains("charmring"))
               continue;
            if(itemName.equals("focus") && itm.toLowerCase().contains("focushelm"))
               continue;
            if(itm.toLowerCase().contains(itemName))
               return true;
            if(itemName.contains("bounty"))
            {
               if(itemName.contains(" "))    //name might have a title in front of it
               {
                  String [] parts = itemName.split(" ");
                  for(String part: parts)
                  {
                     if(part.contains(itm.toLowerCase()))
                        return true;
                  }
               }
            } 
            if(itemName.contains("head"))
            {
               if(itemName.toLowerCase().contains(itm.toLowerCase()))
               {
                  return true;
               }
            }        
         }   
      return false;
   }
   
   public Item getItem(String itemName)
   {
      itemName = itemName.toLowerCase();
      if(weapons!=null)
         for(ArrayList<Weapon> weapTypes:weapons)
            for(Weapon weap: weapTypes)
               if(weap.getName().toLowerCase().equals(itemName))
                  return weap;
      if(armor!=null)
         for(Armor arm: armor)
            if(arm.getName().toLowerCase().equals(itemName))
               return arm;
      if(spells!=null)
         for(Spell spell: spells)
            if(spell.getName().toLowerCase().equals(itemName))
               return spell;   
      if(items!=null)             
         for(String itm: items)
         {
            if(itemName.equals("charm") && itm.toLowerCase().contains("charmring"))
               continue;
            if(itemName.equals("focus") && itm.toLowerCase().contains("focushelm"))
               continue;
            if(itm.toLowerCase().contains(itemName))
               return new Item(itm, 1);
            if(itemName.contains("bounty"))
            {
               if(itemName.contains(" "))    //name might have a title in front of it
               {
                  String [] parts = itemName.split(" ");
                  for(String part: parts)
                  {
                     if(part.contains(itm.toLowerCase()))
                        return new Item(itm, 1);
                  }
               }
            } 
            if(itemName.contains("head"))
            {
               if(itemName.toLowerCase().contains(itm.toLowerCase()))
               {
                  return new Item(itm, 1);
               }
            }        
         }   
      return null;
   }

   public boolean hasItemWithName(String itemName)
   {
      if(items!=null)             
         for(String itm: items)
         {
            int pos = itm.indexOf(":");
            if(pos >= 1)
               itm = itm.substring(0, pos).trim();
            if(itm.equalsIgnoreCase(itemName))
               return true;
         }      
      return false;
   }

   public boolean hasWeapon(String itemName)
   {
      if(weapons==null)
         return false;
      itemName = itemName.toLowerCase();
      for(ArrayList<Weapon> weapTypes:weapons)
         for(Weapon weap: weapTypes)
            if(weap.getName().toLowerCase().equals(itemName))
               return true;
      return false;
   }

   public boolean hasSpell(String itemName)
   {
      if(spells==null)
         return false;
      itemName = itemName.toLowerCase();
      for(Spell spell: spells)
         if(spell.getName().toLowerCase().equals(itemName))
            return true;            
      return false;
   }
   
   public boolean hasArmor(String itemName)
   {
      itemName = itemName.toLowerCase();
      if(armor!=null)
         for(Armor arm: armor)
            if(arm.getName().toLowerCase().equals(itemName))
               return true;
      return false;
   }

//returns the number of items you have named itemName
   public int getItemFrequency(String itemName)
   {
      if(!hasItem(itemName))
         return 0;
      itemName = itemName.toLowerCase();
      for(int i=items.size()-1; i>=0; i--)
      {
         String itm = items.get(i).toLowerCase();
         if(itm.contains(itemName))
         {
            int pos = itm.indexOf(":");
            if(pos==-1)
               return 1;
            else
            {
               String freqTemp = itm.substring(pos+1);
               int frequency = Integer.parseInt(freqTemp);
               return frequency;   
            }    
         }
      }
      return 0;
   
   }

   //returns true if we are allowed to drop the item with the name itemName
   public boolean canRemoveItem(String itemName)
   {
      itemName = itemName.toLowerCase();
      if(itemName.contains("blessed-crown") || itemName.contains("treasuremap")/* || itemName.contains("manual")*/)    //the blessed-crown can not be removed
         return false;
      return  true;
   }

//post: removes the item with the name itemName - decrease the frequency if we have more than one
//items contains the item name, and if we have more than one, the frequency after a ":"
//"pentangle", "lockpick:2" means we have one pentangle and 2 lockpicks
   public boolean removeItem(String itemName)
   {          
      itemName = itemName.toLowerCase();
      if(!canRemoveItem(itemName))    
         return false;
      return  removeItemAbsolute(itemName);
   }
   
   public boolean removeAllItemsWithName(String itemName)
   {          
      for(int i=items.size()-1; i>=0; i--)
      {
         String itm = items.get(i);
         int pos = itm.indexOf(":");
         if(pos >= 1)
            itm = itm.substring(0,pos);
         if(itm.equalsIgnoreCase(itemName))
         {
            items.remove(i);
            return true;      
         }
      }
      return false;
   }


//removes an item, but does not protect the crown from being dropped
   public boolean removeItemAbsolute(String itemName)
   {          
      itemName = itemName.toLowerCase();
      for(int i=items.size()-1; i>=0; i--)
      {
         String itm = items.get(i).toLowerCase();
         if(itm.contains(itemName))
         {
            int pos = itm.indexOf(":");
            if(pos==-1 || itemName.contains("treasuremap"))
               items.remove(i);
            else
            {
               String freqTemp = itm.substring(pos+1);
               int frequency = Integer.parseInt(freqTemp)-1;
               if(frequency==1)
                  itm = itm.substring(0,pos);
               else
                  itm = itm.substring(0,pos)+":"+frequency;
               items.set(i, itm);   
            }
            Sound.dropItem();
            return true;      
         }
      }
      return false;
   }
   
   //see if we are weilding the regular magic staff
   public boolean isWieldingStaff()
   {  //if we have the regular staff and we have a weapon seleted that is fired from the staff
      return (hasItem("Staff") && getImageIndex()==STAFF);
   }
   
   //see if we are weilding the upgraded magic staff
   public boolean isWieldingMagestaff()
   {  //if we have the magestaff and we have a weapon seleted that is fired from the staff
      return (hasItem("Magestaff") && getImageIndex()==STAFF);
   }
   
   //see if we are weilding the legendary magic staff
   public boolean isWieldingLegendaryStaff()
   {  //if we have the Ohnyalei and we have a weapon seleted that is fired from the staff
      return (hasItem("Ohnyalei") && getImageIndex()==STAFF);
   }

//returns the number of weapons carried by the player
   public int numWeapons()
   {
      int count=0;
      
      if(this.hasMagicStaff())         //multiple spells in the slot for the magician's staff
         count++;
      if(hasItem("Vampyric-bite"))     //multiple spells in the slot for the magician's staff
         count++;       
      for(int i=0; i<weapons.length; i++)
      {
         ArrayList<Weapon> weapTypes = weapons[i];
         ArrayList<Integer> weapFreq = weaponFrequencies[i];
         for(int j=0; j<weapTypes.size(); j++)
         {
            Weapon weap = weapTypes.get(j);
            int freq = weapFreq.get(j);
            if(weap.getImageIndex()!=STAFF && weap.getImageIndex()!=NONE)
               count+=freq;  //include weaponFrequencies
         }
      }
      return count;
   }

//returns true if player has enough room to add the arg weapon toAdd to their inventory
   public boolean canAddWeapon(Weapon toAdd)
   {
      double toAddWeight = 1;
      if(toAdd.getImageIndex()==DAGGER || toAdd.getName().toLowerCase().contains("trap") || toAdd.getName().toLowerCase().contains("exotic"))   
         toAddWeight = 0.5;      //daggers, traps and weapons made with exotic metal are half the weight
      else if(toAdd.getImageIndex()==SPEAR || toAdd.getImageIndex()==LONGSTAFF)
         toAddWeight = 0.75;   
      else if(toAdd.getImageIndex()==STAFF || toAdd.getName().equalsIgnoreCase("torch") || toAdd.getImageIndex()==NONE)
         toAddWeight = 0;        //we will be adding staff later, not count staff spells.  Standard torch is light enough to not effect weight
      double totalWeight = getWeaponWeight();
      if(this.hasItem("holdall") && (totalWeight + toAddWeight) <= 10)
         return true;
      if(!this.hasItem("holdall") && (totalWeight + toAddWeight) <= 3)
         return true;
      return false;
   }
   
   //returns the weight of the weapons we are carrying to see if we can pick up another weapon or some lumber
   public double getWeaponWeight()
   {
      double totalWeight = 0;
      if(this.hasMagicStaff())    //multiple spells in the slot for the magician's staff
         totalWeight += 0.5;
      for(int i=0; i<weapons.length; i++)
      {
         ArrayList<Weapon> weapTypes = weapons[i];
         ArrayList<Integer> weapFreq = weaponFrequencies[i];
         for(int j=0; j<weapTypes.size(); j++)
         {
            Weapon weap = weapTypes.get(j);
            double weight = 1;
            if(weap.getImageIndex()==DAGGER || weap.getName().toLowerCase().contains("trap") || weap.getName().toLowerCase().contains("exotic"))
               weight = 0.5;
            else if(weap.getImageIndex()==SPEAR || weap.getImageIndex()==LONGSTAFF)
               weight = 0.75;
            else if(weap.getImageIndex()==STAFF || weap.getName().equalsIgnoreCase("torch") || weap.getImageIndex()==NONE || weap.getImageIndex()==THROWN)
               weight = 0;
            int freq = weapFreq.get(j);
            totalWeight += (freq*weight);  //include weaponFrequencies
         }
      }
      totalWeight += (getItemFrequency("lumber"))/2;
      return totalWeight;
   }
   
   public boolean canPickupLumber()
   {
      double weight = getWeaponWeight();
      return (this.hasItem("holdall") && weight <= 9) || (!this.hasItem("holdall") && weight <= 3);
   }
   
   public void pickupLumber()
   {
      if(canPickupLumber())
      {
         this.addItem(Item.getLumber().getName());
         CultimaPanel.sendMessage("You picked up a fine piece of lumber");
      }
      else
      {
         CultimaPanel.sendMessage("You are carrying too much to bear the weight of that lumber");
      }
   }
   
   public boolean canCutDownTree()
   {
      boolean hasAxe = (getImageIndex()==AXE || getImageIndex()==DUALAXE);
      byte[][] currMap = CultimaPanel.map.get(this.getMapIndex());
      Terrain currentTer = CultimaPanel.allTerrain.get(Math.abs(currMap[this.getRow()][this.getCol()]));
      String terName = currentTer.getName().toLowerCase();
      boolean onTree = (terName.contains("forest") && !terName.contains("dead"));
      return (hasAxe && onTree);
   }
   
   public boolean canBuildBridge()
   {
      if(getItemFrequency("lumber") < 2)
         return false;
      int r = this.getRow();
      int c = this.getCol();
      byte[][] currMap = CultimaPanel.map.get(this.getMapIndex());
      Terrain terUp = null;
      if(!LocationBuilder.outOfBounds(currMap, r-1, c))
         terUp = CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c]));
      Terrain terDown = null;
      if(!LocationBuilder.outOfBounds(currMap, r+1, c))
         terDown = CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c]));
      Terrain terLeft = null;
      if(!LocationBuilder.outOfBounds(currMap, r, c-1))
         terLeft = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1]));
      Terrain terRight = null;
      if(!LocationBuilder.outOfBounds(currMap, r, c+1))
         terRight = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1]));      
      return (TerrainBuilder.canBuildBridge(terUp) || TerrainBuilder.canBuildBridge(terDown) || TerrainBuilder.canBuildBridge(terLeft) || TerrainBuilder.canBuildBridge(terRight)) ;
   }

//returns the number of armor types carried by the player
   public int numArmor()
   {
      int count=0;
      for(int i= 0; i<armor.size(); i++)
      {
         Armor arm = armor.get(i);
         int freq = armorFrequencies.get(i);
         String name = arm.getName().toLowerCase();
         if(arm.getValue() > 0 && !name.contains("fur") && !name.contains("skin") && !name.contains("pelt")  && !name.contains("hide"))
            count+=freq;
      }
      return count;
   }

   public int numPotions()
   {
      int count=0;
      for(int i=0; i<potions.length; i++)
         count += potions[i];
      return count;
   }

   public void addPotion(Potion p)
   {
      byte index = Potion.getPotionNumWithName(p.getName());
      if(index >=0 && index < potions.length)
         potions[index]++;
      if(numPotions() == 1)
         potionSelect = setPotionSelect();
      boolean allPotions = true;
      for(int po:potions)
         if(po==0)
            allPotions = false; 
      if(allPotions)
         Achievement.earnAchievement(Achievement.POTION_CONTROL);        
      Sound.addPotion();
   }

   public void addArmor(Armor toAdd)
   {
      boolean added = false;
      for(int i=0; i<armor.size(); i++)      //see if we already have this armor, and if so, just add to the frequency
      {
         String currArmor = armor.get(i).getName();
         if(currArmor.equals(toAdd.getName()))
         {
            armorFrequencies.set(i, (armorFrequencies.get(i).intValue()+1));
            added = true;
            break;
         }
      }
      if(!added)
      {
         if(toAdd.getName().toLowerCase().contains("pelt") || toAdd.getName().toLowerCase().contains("skin"))
         {  //add pelts to the end of the list
            armor.add(toAdd);
            armorFrequencies.add(1);
         }
         else//add regular armor to the end of the list, but before pelts and skins
         {   //find the first index of where pelts are
            int indexToAdd = -1;
            for(int i=0; i<armor.size(); i++)      
            {
               String currArmor = armor.get(i).getName();
               if(currArmor.toLowerCase().contains("pelt") || currArmor.toLowerCase().contains("skin"))
               {
                  indexToAdd = i;
                  break;
               }
            
            }
            if(indexToAdd >=0 && indexToAdd < armor.size())
            {
               armor.add(indexToAdd, toAdd);
               armorFrequencies.add(indexToAdd, 1);
            }
            else
            {
               armor.add(toAdd);
               armorFrequencies.add(1);
            }
         }
      }
      if(toAdd.getName().toLowerCase().contains("pelt") && this.hasItem("wolf-pelt") && this.hasItem("bear-pelt") && this.hasItem("elk-pelt")) 
         Achievement.earnAchievement(Achievement.TRAPPER_KEEPER);
      Sound.addArmor();
   }

   public void addArmorAndSwitch(Armor toAdd)
   {
      addArmor(toAdd);
      for(int i=0; i<armor.size(); i++)      //see if we already have this armor, and if so, just add to the frequency
      {
         String currArmor = armor.get(i).getName();
         if(currArmor.equals(toAdd.getName()))
         {
            armorSelect = (byte)(i);
            break;
         }
      }
   }

   public void discardCurrentArmor()
   {
      if(armorSelect > 0 && armor.size() > 1 && armorSelect<armor.size())  //can't get rid of cloth armor
      {
         if(armorFrequencies.get(armorSelect) > 1)
            armorFrequencies.set(armorSelect, (armorFrequencies.get(armorSelect).intValue()-1));
         else
         {   
            armor.remove(armorSelect);
            armorFrequencies.remove(armorSelect);
            armorSelect = 0;   
         }
         Sound.dropItem();
      }
   }

   public int[] getPotions()
   {
      return potions;
   }

   public ArrayList<String> getPotionNames()
   {
      ArrayList<String> potionNames = new ArrayList<String>();
      for(int i=0; i<potions.length; i++)
      {
         if(potions[i] > 0)
         {
            Potion curr = Potion.getPotionWithNum((byte)(i));
            potionNames.add(curr.getName());
         }
      }
      return potionNames;
   }

   public ArrayList<Spell> getSpells()
   {
      return spells;
   }

   public ArrayList<String> getSpellNames()
   {
      ArrayList<String> spellNames = new ArrayList<String>();
      for(Spell s:spells)
         spellNames.add(s.getName());
      return spellNames;
   }


   public void addSpell(Spell p)
   {
      if(this.hasSpell(p.getName()))
         return;
      Sound.addSpell();
      spells.add(p);
      Sound.castSpell();
      int NUM_SPELLS = 39;
      if((this.getWeapons()[Player.STAFF]).size() +this.getSpells().size()==NUM_SPELLS)
      {
         Achievement.earnAchievement(Achievement.SPEAK_AND_SPELL);
      }
     //record our picks for spellHotKeys and reassign after being sorted
      String [] spellNames = new String[4];
      for(int i=0; i<spellNames.length && i<spellHotKeys.length; i++)
      {
         if(spellHotKeys[i] < spells.size())
         {
            spellNames[i] = (spells.get(spellHotKeys[i])).getName();
         }
         else
         {
            spellNames[i] = "?";
         }
      }    
      Utilities.sort(spells);
      for(int i=0; i<spellNames.length && i<spellHotKeys.length; i++)
      {
         int spellIndex = 0;
         for(int j=0; j<spells.size(); j++)
         {
            Spell current = spells.get(j);
            if(current.getName().equalsIgnoreCase(spellNames[i]))
            {
               spellHotKeys[i] = j;
               break;
            }
         }
      }
   }

   public boolean canScrollArmor()
   {
      boolean flight = (this.getActiveSpell("Flight")!=null);
      return (!flight && !this.onHorse() && !this.isCamping() && !this.isWolfForm());
   }

   public void scrollArmorUp()
   {
      armorSelect = (byte)((armorSelect+1)%armor.size());
   }

   public void scrollArmorDown()
   {
      armorSelect--;
      if(armorSelect == -1)
         armorSelect = (byte)(armor.size()-1);
   }

   public Armor getArmor()
   {
      if(armorSelect >= 0 && armorSelect < armor.size())
         return armor.get(armorSelect);
      return Armor.getNone();
   }

   public int getArmorPoints()
   {
      int armorPoints = 0;
      for(Spell s: activeSpells)
      {
         if(s.getName().toLowerCase().contains("protection"))
         {
            armorPoints += 5;
            break;
         }
      }
      if(getArmor()!=null)
         armorPoints += getArmor().armorPoints();
      if(imageIndex == SWORDSHIELD)
         armorPoints += 2;  
      if(this.onShip())
         armorPoints += 10;  
      if(this.hasItem("iron-bracer"))
         armorPoints++;  
      return armorPoints;
   }

   public byte getArmorSelect()
   {
      return armorSelect;
   }

   public ArrayList<Armor> getAllArmor()
   {
      return armor;
   }

   public ArrayList<String> getAllArmorNames()
   {
      ArrayList<String> armorNames = new ArrayList<String>();
      for(int i=0; i<armor.size(); i++)
      {
         Armor a = armor.get(i);
         int frequency = armorFrequencies.get(i);
         armorNames.add(a.getName()+":"+frequency);
      }
      return armorNames;
   }

   public ArrayList<Integer> getArmorFrequencies()
   {
      return armorFrequencies;
   }

   public ArrayList<String> getAllWeaponNames(ArrayList<Weapon>weaponSlot, int index)
   {
      ArrayList<String> weaponNames = new ArrayList<String>();
      for(int i=0; i<weaponSlot.size(); i++)
      {  
         Weapon w=weaponSlot.get(i);
         weaponNames.add(w.getName()+":"+weaponFrequencies[index].get(i));
      }
      return weaponNames;
   }

//awareness using randomness for focus spell to give cool visual effect
   public int getAwareness()
   {
      if(awareness == MAX_AWARENESS)
         return awareness;
      if(this.isWolfForm())
      {
         if(CultimaPanel.numFrames % 10 == 0)
            return MAX_AWARENESS;
         return MAX_AWARENESS-1;
      }
      for(Spell s: activeSpells)
      {
         if(s.getName().contains("Eagle-eye"))
         {
            return MAX_AWARENESS;
         }
         if(s.getName().contains("Focus"))
         {
            if(awareness == MAX_AWARENESS-1)
               return MAX_AWARENESS;
            return awareness + (int)(Math.random() * (MAX_AWARENESS-1)) + 1;
         }
      }
      if(hasItem("focushelm"))
      {
         if(awareness < 2)
            return awareness + 2;
         if(awareness == 2)
            return awareness + 1;   
      }
      return awareness;
   }

//awareness without randomness for stable NPC stat viewing with focus spell
   public int getAwarenessRaw()
   {
      if(awareness == MAX_AWARENESS)
         return awareness;
      for(Spell s: activeSpells)
      {
         if(s.getName().contains("Eagle-eye"))
         {
            return MAX_AWARENESS;
         }
         if(s.getName().contains("Focus"))
         {
            if(awareness == MAX_AWARENESS-1)
               return MAX_AWARENESS;
            return awareness + 2;
         }
      }
      if(hasItem("focushelm"))
      {
         if(awareness < 2)
            return awareness + 2;
         if(awareness == 2)
            return awareness + 1;   
      }
      return awareness;
   }


   public void setAwareness(int ma)
   {
      awareness = ma;
      if(awareness >= awarenessNames.length)
         awareness = awarenessNames.length - 1;
      else if(awareness < 0)
         awareness = 0;
      painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
   }

   public String getAwarenessName()
   {
      int awareBoost = 0;
      for(Spell s: activeSpells)
      {
         if(s.getName().contains("Eagle-eye"))
            return awarenessNames[awarenessNames.length-1];
         if(s.getName().contains("Focus"))
         {
            if(awareness == MAX_AWARENESS-1)
               return awarenessNames[awarenessNames.length-1];
            if(awareness < MAX_AWARENESS-1)
               return awarenessNames[awareness+2];
         }
      }
      if(hasItem("focushelm"))
      {
         if(awareness < 2)
            return awarenessNames[awareness+2];
      }
      if(awareness>=0 && awareness < awarenessNames.length)
         return awarenessNames[awareness];
      return "?";
   }

   public void increaseAwareness()
   {
      if(awareness + 1 < awarenessNames.length-1)  //you can only get eagle-eye with a spell
      {
         awareness++;
         painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
         Sound.levelUp(60);
      }  
   }

   public int getRow()
   {
      return row;
   }

   public int getCol()
   {
      return col;
   }

   public void setRow(int r)
   {
      row = r;
   }

   public void setCol(int c)
   {
      col = c;
   }

   public int getWorldRow()
   {
      return worldRow;
   }

   public int getWorldCol()
   {
      return worldCol;
   }

   public void setWorldRow(int r)
   {
      worldRow = r;
   }

   public void setWorldCol(int c)
   {
      worldCol = c;
   }

   public LinkedList<Teleporter> getMemory()
   {
      return memory;
   }

   public void setMemory(LinkedList<Teleporter> m)
   {
      memory = m;
   }

   public void clearMemory()
   {
      memory.clear();
   }

   public int getMapIndex()
   {
      return mapIndex;
   }

   public void setMapIndex(int mi)
   {
      mapIndex = mi;
   }

   public String getLocationType()
   {
      return location;
   }

   public void setLocationType(String loc)
   {
      location = loc;
   }

   public int getAgility()
   {
      int[]weaponMod = getWeapon().statModifier();
      int[]armorMod = getArmor().statModifier();
      int agilityBoost = 0;
      if(startStoryIndex==CAMP)
         agilityBoost += 2;
      else if(startStoryIndex==FLIGHT)
         agilityBoost += 1;
      if(activeSpells!=null)
      {
         for(Spell s: activeSpells)
         {
            int[] statM = s.statModifier();
            agilityBoost += statM[2];
         }
      }
      if(items!=null)
      {
         for(String curr: items)
         {
            Item it = Item.getItemWithName(curr);
            if(it!=null)
               agilityBoost += it.statModifier()[2];
         }
      }
      if(this.isWolfForm())
         agilityBoost = (int)(agility * 1.5);
      int finalAgility =  agility + weaponMod[2] + armorMod[2] + agilityBoost + statMod[2];
      if(onHorse())
      {
         String horse = this.getHorseBasicInfo();
         String [] parts = horse.split(",");
         if(parts.length==5)
         {
            String agil = parts[3].trim();
            if(FileManager.wordIsInt(agil))
               return Math.max(Integer.parseInt(agil), finalAgility);
         }
         return Math.max(60, finalAgility);
      }
      return Math.min(MAX_STAT, Math.max(1,finalAgility));
   }

   public int getAgilityRaw()
   {
      return agility;
   }

   public void setAgility(int a)
   {
      agility = a;
      painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
      Sound.levelUp(59);
   }

   public int getMight()
   {
      
      int[]weaponMod = getWeapon().statModifier();
      int[]armorMod = getArmor().statModifier();
      int mightBoost = 0;
      if(startStoryIndex==CAMP)
         mightBoost += 2;
      else if(startStoryIndex==FLIGHT)
         mightBoost += 1;
      if(activeSpells!=null)
      {
         for(Spell s: activeSpells)
         {
            int[] statM = s.statModifier();
            mightBoost += statM[0];
         }
      }
      if(items!=null)
      {
         for(String curr: items)
         {
            Item it = Item.getItemWithName(curr);
            if(it!=null)
               mightBoost += it.statModifier()[0];
         }
      }
      if(this.isWolfForm())
         mightBoost = (int)(might * 1.5);
      int finalMight = might + weaponMod[0] + armorMod[0] + mightBoost + statMod[0];
      return Math.min(MAX_STAT, Math.max(1, finalMight));
   }

   public int getMightRaw()
   {
      return might;
   }

   public void setMight(int m)
   {
      might = m;
      painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
      Sound.levelUp(57);
   
   }

   public int getMind()
   {
      int[]weaponMod = getWeapon().statModifier();
      int[]armorMod = getArmor().statModifier();
      int mindBoost = 0;
      if(startStoryIndex==CAMP)
         mindBoost += 2;
      else if(startStoryIndex==FLIGHT)
         mindBoost += 1;
      if(this.isWieldingMagestaff())
      {  //we have the magestaff and are doing a spell (we don't have a regular weapon armed)
         int[]staffMod = Weapon.getMageStaff().statModifier();
         mindBoost += staffMod[1];
      }
      else if(this.isWieldingLegendaryStaff())
      {  //we have the Ohnyalei and are doing a spell (we don't have a regular weapon armed)
         int[]staffMod = Weapon.getOhnyalei().statModifier();
         mindBoost += staffMod[1];
      }
      if(this.hasItem("Bright-horn"))
      {  //we have the Unicorn's horn
         int[]staffMod = Weapon.getBrightHorn().statModifier();
         mindBoost += staffMod[1];
      }
      if(activeSpells!=null)
      {
         for(Spell s: activeSpells)
         {
            int[] statM = s.statModifier();
            mindBoost += statM[1];
         }
      }
      if(items!=null)
      {
         for(String curr: items)
         {
            Item it = Item.getItemWithName(curr);
            if(it!=null)
               mindBoost += it.statModifier()[1];
         }
      }   
      if(this.isWolfForm())
         mindBoost = -1*(int)(mind * 0.5);
   
      int finalMind = mind + weaponMod[1] + armorMod[1] + mindBoost + statMod[1];
      return Math.min(MAX_STAT, Math.max(1, finalMind));
   }

   public int getMindRaw()
   {
      return mind;
   }

   public void setMind(int m)
   {
      mind = m;
      painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
      Sound.levelUp(55);
   }

   public int getBody()
   {
      if(this.isWolfForm())
         return body*2;
      return body;
   }

   public void setBody(int b)
   {
      body = b; 
   }

   public int getHealthMax()
   {
      return (this.getMight()*3 + this.getAgility()*2);
   }

   public void damage(int d)
   {
      if(this.getBody() <= 0)
         return;
      if(this.isCamping())       //wake us up if we get damaged
      {
         d *= 4;						//quad damage if hit while sleeping
         this.setCamping(false);
      }   
      this.setBody(body-d);
      if(hasEffect("sullied"))   //getting damaged sobers us up some
         addSulliedValue(-1*Math.max(1,(d/2)));
      if(this.getBody() < 0)
      {
         this.setBody(0);
         Sound.playerDie();        //sound
         //if you die during a battle or escort mission, remove the missions and any npcs associated with them
         boolean saveMissions = false;
         NPCPlayer escortee = Utilities.getNpc();
         if(escortee != null)
         {
            escortee.escorteeIsLost();
            Utilities.removeNPCat(escortee.getRow(), escortee.getCol(), escortee.getMapIndex());
            FileManager.writeOutAllNPCFile(CultimaPanel.civilians, "maps/civilians.txt");
            FileManager.writeOutNPCFile(CultimaPanel.worldMonsters, "maps/worldMonsters.txt");
            saveMissions = true;
         }
         if(this.getLocationType().contains("battlefield"))
         {
            for(int i=0; i<CultimaPanel.missionStack.size(); i++)
            {
               Mission m = CultimaPanel.missionStack.get(i);
               if(m.getMissionType()==Mission.BATTLE || m.getMissionType()==Mission.ESCORT_KID)
               {
                  CultimaPanel.missionStack.remove(i);
                  saveMissions = true;
               }
            }
         }
         if(saveMissions)
         {
            FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");   
         }
      }
      else
      {
         Sound.playerDamage(d);        //sound
      }
      painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
      if(this.getBody() <= 0)
      {
         removeEffects();
         clearActiveSpells();
         CultimaPanel.talkSel = false;
         if(CultimaPanel.selected!=null)
            CultimaPanel.selected.setTalking(false);
         CultimaPanel.selected = null;
         CultimaPanel.selectedTerrain = null;
         if(hasItem("life-pearl"))
            CultimaPanel.sendMessage("Life escapes thee, but thy life-pearl glows!");
         else
            if(hasItem("blessed-crown"))
               CultimaPanel.sendMessage("You ascend to the Gods as a cousin to Skara Brae!");
            else
               CultimaPanel.sendMessage("You fall to your empty, cold death!");
      
         this.deathTime = CultimaPanel.numFrames;
         stats[MAP_COMPLETED] = (int)(Math.round(CultimaPanel.revealedMap/((CultimaPanel.map.get(0)).length * (CultimaPanel.map.get(0))[0].length)*100));
         double foundLoc=0;
         for(Location loc: CultimaPanel.allLocations)
         {
            if(loc.getMapIndex() >= 0)
               foundLoc++;
         }
         stats[LOCATIONS_FOUND] = (int)(Math.round(foundLoc/(CultimaPanel.allLocations.size()-1)*100));
         //we died before finishing a save mission, so remove them
         boolean missionsAltered = false;
         for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
         {
            Mission m = CultimaPanel.missionStack.get(i);
            if(m.getType().equals("Save") || m.getType().equals("Ceremony"))
            {
               CultimaPanel.missionStack.remove(i);
               missionsAltered = true;
               CultimaPanel.numChants = -1;
            }
         }
         if(missionsAltered)
            FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
      }      
   }

   public void heal(int d)
   {
      this.setBody(this.getBody()+d);
      if(!this.isWolfForm() && this.getBody() > this.getHealthMax())
         this.setBody(this.getHealthMax());
      painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
   }

   public int getManna()
   {
      return manna;
   }

   public int getMannaMax()
   {
      return this.getMind()*10;
   }
   
   public static int getMannaMax(int mindValue)
   {
      return mindValue*10;
   }

   public void setManna(int m)
   {
      if(this.hasItem("Ohnyalei") && m < 0)        //if we have the legendary staff, our manna never goes negative (no cooldown needed)
      {
         m = 0;
      }
      if((CultimaPanel.rainbowAlpha > 0) && m < 0)   //half as much negative manna cost if a rainbow is out
         m /= 2;   
      manna = m;
      if(manna > this.getMannaMax())
         manna = this.getMannaMax();
   }

   public void addManna(int m)
   {
      if(this.getBody() > 0)
      {
         manna += m;
         if(manna > this.getMannaMax())
            manna = this.getMannaMax();
      }
   }
   
   public int getGold()
   {
      return gold;
   }

   public void setGold(int g)
   {
      gold = g;
   }

   public void addGold(int g)
   {
      gold += g;
      Sound.addGold(g);
   }

   public void pay(int g)
   {
      gold -= g;
      Sound.payGold();
   }

   public int getReputationRaw()
   {
      return reputation;
   }

   public int getReputation()
   {
      int bonus = 0;
      if(this.getWeapon().getName().toLowerCase().contains("royal"))
         bonus += 100;
      return reputation + bonus;
   }


   public void setReputation(int r)
   {
      reputation = r;
   }

   public void addReputation(int r)
   {
      boolean timestop = (this.getActiveSpell("Timestop")!=null);
      if(timestop)
      {
         return;
      }
      reputation += r;
      if(startStoryIndex == CAMP && reputation>=0)
      {
         if(this.isFramed())
         {
            CultimaPanel.flashColor = Color.blue;
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
            Sound.levelUp(65);
            CultimaPanel.sendMessage("You have cleared thy reputation!");
            this.setName(this.getName().replace("<","").replace(">",""));//remove the > and < from the name
            for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
            {
               Mission m = CultimaPanel.missionStack.get(i);
               if(m.getType().equals("Framed"))
               {
                  CultimaPanel.missionStack.remove(i);
                  Player.stats[Player.MISSIONS_COMPLETED]++;
                  CultimaPanel.player.addExperience(100);
                  FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
                  break;
               }
            }
         }
      }
   }

   public byte getImageIndex()
   {
      return imageIndex;
   }

   public int getLevel()
   {
      return (might+mind+agility)/3;
   }
   
   public int getLevelWhole()
   {
      return (this.getMight() + this.getMind() + this.getAgility())/3;
   }

   public void setImageIndex(byte g)
   {
      if(g >=0 && g < characters.length && g < portraits.length && g < guardDown.length)
      {
         imageIndex = g;
         ArrayList<String> images = new ArrayList<String>();
         int lastIndex = characters[imageIndex][RIGHT_IMAGE].length-1;
         if(guardDown[imageIndex].length > 0 && !onGuard)
         {
            for(int i=0; i<guardDown[imageIndex].length-1; i++)  //don't add the last image - it is the dead image
            {
               String p = guardDown[imageIndex][i];
               images.add(p);
            }
         }
         else
         {
            byte direction = RIGHT_IMAGE;
            if(lastDir == LocationBuilder.WEST && characters[imageIndex][LEFT_IMAGE].length > 0 && characters[imageIndex][LEFT_IMAGE].length == characters[imageIndex][RIGHT_IMAGE].length)
            {
               direction = LEFT_IMAGE;
            }
            for(int i=0; i<lastIndex; i++)  //don't add the last image - it is the dead image
            {
               String p = characters[imageIndex][direction][i];
               images.add(p);
            }
         }
         dead = new ImageIcon(characters[imageIndex][RIGHT_IMAGE][lastIndex]);
         this.setPictures(images);   
         portrait = portraits[imageIndex];
         if(imageIndex == LUTE)
         {
            Sound.buildScale(Sound.harmMin, Sound.minPent);
         }
      }
   }

//for changing to Flight or a Ship
   public void setImageIndexTemp(byte g)
   {
      if(g >=0 && g < characters.length)
      {
         imageIndex = g;
         ArrayList<String> images = new ArrayList<String>();
         int lastIndex = characters[imageIndex][RIGHT_IMAGE].length-1;
         if(guardDown[imageIndex].length > 0 && !onGuard)
         {
            for(int i=0; i<guardDown[imageIndex].length-1; i++)  //don't add the last image - it is the dead image
            {
               String p = guardDown[imageIndex][i];
               images.add(p);
            }
         }
         else
         {
            for(int i=0; i<lastIndex; i++)  //don't add the last image - it is the dead image
            {
               String p = characters[imageIndex][RIGHT_IMAGE][i];
               images.add(p);
            }
         }
         dead = new ImageIcon(characters[imageIndex][RIGHT_IMAGE][lastIndex]);
         this.setPictures(images);   
      }
   }

   public String getShortName()
   {
      return this.getName().replace("~","").replace("<","").replace(">","");
   }

   //post: returns the reputaion name for the inventory, modified for special status (vamppire/framed/etc) or special armor (dragon/pelt/etc)
   public String getReputationName()
   {
      if(this.isVampire())
         return "the Vampire";
      if(this.isWerewolf())
         return "the Wolfen";
      if(this.isFramed())
         return "the Framed";
      int repIndex = 3;
      if(reputation >= 1000)
         repIndex = 6;
      else if(reputation >= 500)
         repIndex = 5;
      else if(reputation >= 10)   
         repIndex = 4;
      else if(reputation >= -10)
         repIndex = 3;
      else if(reputation >= -500)
         repIndex = 2;
      else if(reputation >= -1000)
         repIndex = 1;
      else
         repIndex = 0;
      String armorName = getArmor().getName().toLowerCase();  
      if(armorName.contains("dragon"))
         return "the Dragonslayer";
      if(armorName.contains("seaserpent"))
         return "of the Seas";
      if(armorName.contains("skin") || armorName.contains("pelt"))
         return "the Beastmaster";
   
      return "the "+reputationNames[repIndex];
   }
   
   //post: returns the pure reputation name for the journal
   public String getPureReputationName()
   {
      int repIndex = 3;
      if(reputation >= 1000)
         repIndex = 6;
      else if(reputation >= 500)
         repIndex = 5;
      else if(reputation >= 10)   
         repIndex = 4;
      else if(reputation >= -10)
         repIndex = 3;
      else if(reputation >= -500)
         repIndex = 2;
      else if(reputation >= -1000)
         repIndex = 1;
      else
         repIndex = 0;   
      return reputationNames[repIndex];
   }

    //post: returns portait for inventory page when player is alive
   public ImageIcon getPortrait()
   {
      return portrait;
   }

    //post: returns portrait for inventory page shown when the player is dead
   public ImageIcon getDeadPortrait()
   {
      if(hasItem("blessed-crown"))
         return portraits[LUTE+2];
      return portraits[LUTE+1];
   }

   public double getHitTime()
   {
      return hitTime;
   }

   public void setHitTime(double ht)
   {
      hitTime = ht;
   }

   public double getMissTime()
   {
      return missTime;
   }

   public void setMissTime(double ht)
   {
      missTime = ht;
   }

   public int getExperience()
   {
      return experience;
   }

   public int expNeededForNextLevel()
   {
      int oldExperience = 44;
      for(int i=0; i<this.getLevel(); i++)
         oldExperience *= EXP_COST_PER_LEVEL;
      return oldExperience + expToLevelUp;   
   }

    //post: level up and add to stats
   public void addExperience(int e)
   { 
      experience += e;
      
      if(Player.stats[Player.MISSIONS_COMPLETED]==50)
         Achievement.earnAchievement(Achievement.TASK_MASTER);
   
      if(experience >= expNeededForNextLevel())
      {         
         if(might < MAX_STAT || mind < MAX_STAT || agility < MAX_STAT || awareness < 3)
         {
            Sound.levelUp(60);
            painTime = CultimaPanel.numFrames + (CultimaPanel.animDelay*3);
         }
         expToLevelUp = (int)(expToLevelUp * EXP_COST_PER_LEVEL);
         ArrayList<Integer>statIndex = new ArrayList<Integer>();
         if(might < MAX_STAT)
            statIndex.add(0); //might
         if(mind < MAX_STAT)
            statIndex.add(1); //mind
         if(agility < MAX_STAT)
            statIndex.add(2); //agility
         if(statIndex.size() == 0)
         {
            if(awareness < 3)
            {
               awareness++;
               CultimaPanel.sendMessage("Thy awareness sharpens!");
            }
            else
            {
            //when we reach max stats, recharge
               body = this.getHealthMax();
               manna = this.getMannaMax();
            }
         }
         else
         {
            double totalCoords = 0;          //to make specific experience guide the quality we increase when we level up
            for(int pt: specificExperience)  //find the percentage of each specific experience points out of the total
               totalCoords += pt;
            double[] expPercentages = new double[specificExperience.length];
            for(int i=0; i<expPercentages.length; i++)
               expPercentages[i] = specificExperience[i] / totalCoords;
            double rand = Math.random();
            if(rand < expPercentages[MIGHT] && might < MAX_STAT)
            {
               might++;
               CultimaPanel.sendMessage("Thy core strengthens!");   
            }
            else if(rand < expPercentages[MIGHT] + expPercentages[MIND] && mind < MAX_STAT)
            {
               mind++;
               CultimaPanel.sendMessage("Thy mind awakens!");   
            }
            else if(rand < expPercentages[MIGHT] + expPercentages[MIND] + expPercentages[AGILITY] && agility < MAX_STAT)
            {
               agility++;
               CultimaPanel.sendMessage("Thy reflexes surge!");   
            }
            else
            {
               int index = statIndex.get((int)(Math.random()*statIndex.size()));
               if(index==0)
               {
                  might++;
                  CultimaPanel.sendMessage("Thy core strengthens!");   
               }
               else if(index==1)
               {
                  mind++;
                  CultimaPanel.sendMessage("Thy mind awakens!");   
               }
               else if(index==2)
               {
                  agility++;
                  CultimaPanel.sendMessage("Thy reflexes surge!");   
               }
               else 
               {
                  if(awareness < MAX_AWARENESS-1)
                  {
                     awareness++;
                     CultimaPanel.sendMessage("Thy awareness sharpens!");
                  }
                  else
                  {
                  //when we reach max stats
                     body = this.getHealthMax();
                     manna = this.getMannaMax();
                  }
               }
            }
            for(int i=0; i<specificExperience.length; i++)
               specificExperience[i] = 0;
         }
      }
   }

   public int getExpToLevelUp()
   {
      return expToLevelUp;
   }

   public ImageIcon getDeadPicture()
   {
      return dead;
   }

   public String getStatsString()
   {
      String ans = "";
      for(int stat:stats)
         ans += stat+ " ";
      return ans;
   }
   
   public String getInfoIndexesString()
   {
      String ans = "";
      for(int num:infoIndexes)
         ans += num+ " ";
      return ans;
   }

   public String getSpecificExpString()
   {
      String ans = "";
      for(int stat:specificExperience)
         ans += stat+ " ";
      return ans;
   }
   
   public boolean hasDeathMission()
   {
      for(Mission m : CultimaPanel.missionStack)
         if(m.getType().equals("Death"))
            return true;
      return false;
   }

   public int getAttackTime()
   {
      return attackTime;
   }

   public void setAttackTime(int at)
   {
      attackTime = at;
   }
   
   public boolean hasStaffReadied()
   {
      Weapon weapon = this.getWeapon();
      String weapName = weapon.getName();
      return (weapName.equals("Staff") || weapName.equals("Magestaff") || weapName.equals("Ohnyalei"));
   }

//returns true if the location is in a home the player owns
   public static boolean inHome(int mi, int row, int col)
   {
      byte[][]currMap = (CultimaPanel.map.get(mi));   
      for(int r=row-1; r<=row+1; r++)
         for(int c=col-1; c<=col+1; c++)
         {
            if(r < 0 || c < 0 || r>=currMap.length || c>=currMap[0].length)
               continue;
            String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(current.contains("purple_floor_inside"))
               return true;
         }
      return false;
   }
   
   public static boolean inLocationWithHome(int mi)
   {
      byte[][]currMap = (CultimaPanel.map.get(mi));   
      for(int r=1; r<currMap.length-1; r++)
         for(int c=1; c<currMap[0].length-1; c++)
         {
            if(r < 0 || c < 0 || r>=currMap.length || c>=currMap[0].length)
               continue;
            String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(current.contains("purple_floor_inside"))
               return true;
         }
      return false;
   }
   
   public static boolean onDungeonEntrance(int mi, int row, int col)
   {
      for(Location loc:CultimaPanel.allLocations)
      {
         if(loc.getRow()==row && loc.getCol()==col && loc.getFromMapIndex()==mi)
            return true;
      }
      return false;
   }

  //returns the target if we want to force the game to talk with them (assigned to CultimaPanel.selected), or null if not
   public NPCPlayer attack(NPCPlayer target, boolean altAttack)
   {
      if(this.getArmor().getName().toLowerCase().contains("cloak"))     //can't attack with a cloak on
      {
         CultimaPanel.sendMessage("Thou can not attack with a cloak on!");
         return null;
      }
      Weapon weapon = this.getWeapon();
      if(Weapon.isLongStaff(weapon.getName()) && altAttack)
      {  //we are trying to vault over a wall or counter here
         return null;
      }
      Weapon altFire = Weapon.getAltFire(weapon.getName());
      boolean isThrownSpear = false;
      boolean isThrownDagger = false;
      Weapon thrownWeapon = null;   //to place on target if they get hit
      if(altAttack && altFire!=null)
      {  
         weapon = altFire;
         if(weapon.getName().toLowerCase().contains("spear"))
         {
            isThrownSpear = true;
         }
         else if(weapon.getName().toLowerCase().contains("dagger"))
         {
            isThrownDagger = true;
         }
      }
      String weapName = weapon.getName().toLowerCase();
      if(weapName.contains("torch") && altAttack)                       //wave torch around to cause fear
      {
         CultimaPanel.flashColor = Color.yellow;
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 8;
         activeSpells.add(Spell.getFearTorch());
         magicSmoke(SmokePuff.dustCloud);
         return null;
      }
      if(weapName.contains("phasewall") && target != null && !target.isStatue() && target.getCharIndex()!=NPC.MONOLITH)
         return null;
      boolean isGarriott = (target!=null && target.getName().contains("Garriott") && !target.isZombie());
      if(isGarriott && Math.random() < 0.5)   
         this.addEffectForce("curse");
      if(weapName.contains("fireshield") || weapName.contains("summon-vortex") || weapName.contains("advance") || weapName.contains("raise-stone"))
      {
         int mannaCost = weapon.getMannaCost();   
         if(this.getManna() < mannaCost)
         {
            CultimaPanel.sendMessage("Not enough manna!");
            return null;
         }
         else
         {
            if(weapName.contains("advance"))
            {
               CultimaPanel.flashColor = new Color(10, 200, 255);
               Sound.teleport();        //sound
            }
            else if(weapName.contains("fireshield"))
            {
               CultimaPanel.flashColor = Color.orange;
               Sound.fireCannon();        //sound
            }
            else
            {
               CultimaPanel.flashColor = Color.cyan;
               Sound.thunder();        //sound
            }
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 10;
            if(weapName.contains("raise-stone"))
            {
               boolean earthElem = false;
               for(NPCPlayer p: CultimaPanel.worldMonsters)
               {
                  if(p.getCharIndex()==NPC.EARTHELEMENTAL)
                  {
                     earthElem = true;
                     p.setNumInfo(0);
                     p.setMoveType(NPC.ROAM);
                     p.setReputation(0);
                     p.setHasBeenAttacked(false);
                  }
               }
               if(earthElem)
               {
                  CultimaPanel.sendMessage("You have earned respect of the Earth Elemental!");
               } 
            }
            return null;
         }
      }
      if(weapName.contains("trap"))
      {
         Sound.dropItem();
         return null;
      }
   
      if(target == null)
      {
         if(weapon.getRange()<=2)
            CultimaPanel.sendMessage("Thou swipes at the very air!");
         else
            CultimaPanel.sendMessage("Thou deftly attacks the air!");
         return null;
      }
      if(CultimaPanel.nextMonsterSpawn != -1 && target.getCharIndex()==CultimaPanel.nextMonsterSpawn)
         CultimaPanel.nextMonsterSpawn = -1;
      boolean indoors = false;      //used to see if fire is less potent in the rain, or freeze more potent in the rain
      boolean isZombie = target.isZombie();
      boolean isStatue = target.isStatue();
      String locType = location.toLowerCase();
      if(locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("wolfenstein"))
         indoors = true;   
      this.setOnGuard(true);   
      if(CultimaPanel.numFrames < attackTime + weapon.getReloadFrames())   //we are reloading
      {
         Sound.reloadTime();        //sound
         return null;
      }
      if(weapName.contains("bow")|| weapName.contains("caster"))
      {
         if(this.getNumArrows() <= 0 || (weapName.contains("karna") && altAttack && this.getNumArrows() <= 1))
         {  //no arrow for a regular attack, not at least 2 arrows for alternate attack with the legendary bow (needs two arrows)
            Sound.reloadTime();
            return null;
         }
         this.setNumArrows(this.getNumArrows()-1);
         if(weapName.contains("karna") && altAttack)
         {
            this.setNumArrows(this.getNumArrows()-1);
         }
      }
      if(weapName.contains("sling") || weapName.contains("throwing-stone"))
      {
         if(this.getNumStones() <= 0)
         {
            Sound.reloadTime();
            return null;
         }
         this.setNumStones(this.getNumStones()-1);
         if(this.getNumStones() < 1)
         {
            setImageIndex(NONE);
            weaponSelect = 0;
         }
      }
      if(getActiveSpell("Invisibility")!=null)     //invisibility wears off as soon as you attack
      {
         this.removeSpell("Invisibility");
      }
      if(getActiveSpell("Unseen")!=null)           //unseen wears off as soon as you attack
      {
         this.removeSpell("Unseen");
      }
      if(this.getActiveSpell("Fear")!=null)
      {                                            //cancel our fear spell
         this.removeSpell("Fear");
      }
   
      attackTime = CultimaPanel.numFrames;
      byte[][]currMap = CultimaPanel.map.get(this.getMapIndex());	
      String playerTerrain = "";
      if(!LocationBuilder.outOfBounds(currMap, this.getRow(), this.getCol()))
         playerTerrain = CultimaPanel.allTerrain.get(Math.abs(currMap[this.getRow()][this.getCol()])).getName().toLowerCase();
      String targetTerrain = "";
      if(!LocationBuilder.outOfBounds(currMap, target.getRow(), target.getCol()))
         targetTerrain = CultimaPanel.allTerrain.get(Math.abs(currMap[target.getRow()][target.getCol()])).getName().toLowerCase();
      int targetCover = 0;       //if target can take cover around obstacles
      if(targetTerrain.contains("forest") && !targetTerrain.contains("dead"))
      {
         if(targetTerrain.contains("dense"))
         {
            targetCover = Utilities.rollDie(3, 6);
         }
         else
         {
            targetCover = Utilities.rollDie(1, 4);
         }
      }
      int heightBonus = 1;
      if(this.onShip() || ((playerTerrain.contains("hill") || playerTerrain.contains("mountain")) && !targetTerrain.contains("hill") && !targetTerrain.contains("mountain")))
         heightBonus = 2;        //higher chance of hitting our target if we are higher up
      if(target.getMoveType() == NPC.RUN)
         heightBonus = 3;
      boolean isMagic = (imageIndex==STAFF && !hasStaffReadied());
      boolean fullManna = (this.getManna() >= this.getMannaMax());   //see if we are at full manna for more potent lightning spell
      int mannaCost = 0;
      if(isMagic && weaponSelect > 0)  //defaut staff is used as a melee attack
      {
         mannaCost = weapon.getMannaCost();   
         if(this.getManna() < mannaCost)
         {
            CultimaPanel.sendMessage("Not enough manna!");
            return null;
         }
         else if(weapName.contains("stonecast"))
         {
            manna = -100;
         }
         else
            manna -= mannaCost;
      }
      if(this.onShip())
      {
         CultimaPanel.flashColor = Color.yellow;
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 10;
         Sound.fireCannon();        //sound
      }
      else if(weapName.contains("lightning"))
      {
         CultimaPanel.flashColor = Color.white;
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
         Sound.fireCannon();        //sound
      }
      else if(weapName.contains("blind"))
      {
         CultimaPanel.flashColor = Color.white;
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = weapon.getRange();
         Sound.secretWall();        //sound
      }
      else if(weapName.contains("stonecast"))
      {
         CultimaPanel.flashColor = new Color(180,180,180);
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 10;
         Sound.thunder();        //sound
      }
      else if(weapName.contains("antioch"))
      {
         CultimaPanel.flashColor = Color.orange;
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         Sound.thunder();
      }
      boolean isRanged = (weapon.getRange() >= 2);
      boolean isMelee = (!isMagic && !isRanged);
      boolean mightyBlow = (altAttack && (weapon.getImageIndex()==SABER || weapon.getImageIndex()==HAMMER));   //are we attepting a mighty attack with a sword or hammer
      boolean vampyricBite = (weapName.contains("vampyric-bite") && NPC.isHumanoid(target.getCharIndex()));
      boolean critical = false;
      boolean turnedStone = false;
      int dam = 0;
      if(weapon.getEffect().contains("control") && target.hasEffect("control"))
      {
         dam = 0;
         if(weapon.getImageIndex()==STAFF)
            CultimaPanel.sendMessage("The spell is repelled!");   
         else if(weapon.getImageIndex()==CROSSBOW || weapon.getImageIndex()==BOW)
            CultimaPanel.sendMessage("The bow already owns this soul and is repelled!");   
         else
            CultimaPanel.sendMessage("The blade already owns this soul and is repelled!");
         return null;
      }
      int distance = (int)(Display.findDistance(this.getRow(), this.getCol(), target.getRow(), target.getCol()));
      double mindPercent = (double)(mind) / MAX_STAT;       //the percentage of mind attribute as a decimal - used for random throws
      double agilityPercent = (double)(agility) / MAX_STAT; //the percentage of agility attribute as a decimal - used for random throws
      if(isMagic)
      {
         int hitDie =Utilities.rollDie(this.getMind() + ((target.getAgility()+target.getMind())/2));
         if(weapon.getEffect().contains("curse") || weapon.getEffect().contains("control") || weapName.contains("deathtouch") || weapName.contains("stonecast")  || NPC.isShip(target.getCharIndex()))
         {
            heightBonus = 3;  //curse and control spell is not on a vector towards the target, so it has a higher chance of hitting
            hitDie =Utilities.rollDie(this.getMind()+target.getMind());
         }
         double bonusTemp = 1;
         if(heightBonus > 1 || this.hasItem("Magestaff") || this.hasItem("Ohnyalei"))
            bonusTemp = 1.5;
         if(target.hasEffect("control") || target.hasEffect("freeze") || target.hasEffect("web") || target.hasEffect("stun"))
            bonusTemp *= 2;   
         if(isGarriott)       //little chance we can use magic against Richard Garriott
         {
            bonusTemp = 1;
            if(Math.random() < 0.75)
               hitDie *= 2;
         }   
         if((int)(this.getMind()*bonusTemp) >= hitDie || target.getBody() < 1  || weapName.contains("phasewall"))
         {                    //we hit our target
            dam =Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage())  + this.getMind();  
            if( weapName.contains("phasewall"))
               dam = target.getBody();
            if(weaponSelect == 0)      //defaut staff is used as a melee attack
               dam =Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage())  + this.getAgility();
            if(weapon.getEffect().contains("freeze") &&  CultimaPanel.weather > 0 && !indoors)     //freeze effect does more damage when it is raining
               dam =Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage())  + this.getMind() + CultimaPanel.weather;    
            int critDie =Utilities.rollDie(400);
            double critBonus = 1;
            if(this.hasItem("Magestaff") || this.hasItem("Ohnyalei"))
               critBonus = 1.25;
            if (critDie <= getMind()*critBonus)
            {
               dam =  weapon.getMaxDamage()*2;
               critical = true;
            }
            if(weapon.getEffect().contains("curse") || weapon.getEffect().contains("control") || (weapon.getEffect().contains("stun") && !target.hasEffect("stun")))
            {
               dam /= 10;
            }
            if(weapon.getEffect().contains("web") || weapon.getName().toLowerCase().contains("blind"))
            {
               dam = 0;
            }
            if(weapon.getEffect().contains("fire") || weapon.getEffect().contains("freeze") || weapon.getEffect().contains("poison") || weapon.getEffect().contains("curse") || weapon.getEffect().contains("control") || weapon.getEffect().contains("web") || weapon.getEffect().contains("stun"))
            {
               if(weapon.getEffect().contains("freeze"))
                  target.setMoveTypeTemp(NPC.STILL);
               if(weapon.getEffect().contains("fire") && CultimaPanel.weather > 0 && !indoors)
               {   
                  if(Utilities.rollDie(1,6) <= CultimaPanel.weather)
                  {
                     CultimaPanel.sendMessage("Fire extinguished!");
                  }
                  else
                  {
                     boolean success = target.addEffect(weapon.getEffect());
                     if(!success)
                        CultimaPanel.sendMessage("No effect!");
                  }
               }
               else
               {  
                  boolean success = target.addEffect(weapon.getEffect());
                  if(!success)
                  {
                     if(target.hasEffect(weapon.getEffect()))
                     {}
                     else
                        CultimaPanel.sendMessage("No effect!");
                  }
               }
            } 
            if(weapName.contains("stonecast"))
            {
               if(NPC.isEtheral(target.getCharIndex()) || NPC.isShip(target.getCharIndex()))
               {}
               else
               {
                  target.setNumInfo(-999);
                  turnedStone = true;
               }
            }
            else if((weapName.contains("lightning") || weapName.contains("fireball") || weapName.contains("icestrike")) && fullManna)
            {  //chance of lightning or fireball doing an area of effect damage if we are at our max manna value
               if(Math.random() < mindPercent)     //the higher our mind, the more likely we get chain lightining
               {
                  int aoeRange = 3 + (Utilities.rollDie(0, (int)(3*mindPercent)));
                  Utilities.areaOfEffectAttack(target, aoeRange, weapon);  
               }
            }
         }
      }
      else if (isRanged)
      {//higher chance of hitting our target if we are closer
         boolean isHalberdSpin = (Weapon.isHalberd(weapName) && altAttack);
         int hitDie =Utilities.rollDie(this.getAgility()+target.getAgility()+(distance/heightBonus)+targetCover);
         int enemyClose = 0;
         if(distance == 1)
            enemyClose = 10;
         int weather = CultimaPanel.weather; 
         if(Display.isWinter())  //no penalty in the snow
            weather = 0;  
         if(weapName.contains("bow") || weapName.contains("caster"))  //less bow accuracy if fired in the rain or if enemy is 1 space away      
            hitDie =Utilities.rollDie(this.getAgility()+target.getAgility()+(distance/heightBonus)+weather+enemyClose+targetCover);
         double bonusTemp = 1;
         if(heightBonus > 1 || Weapon.isLegendaryWeapon(weapName))
            bonusTemp = 1.25;
         if(target.hasEffect("control") || target.hasEffect("freeze") || ((NPC.isUndead(target.getCharIndex()) || target.isZombie()) && weapName.contains("life")) || NPC.isShip(target.getCharIndex()))
            bonusTemp *= 2;
            
         if((int)(this.getAgility()*bonusTemp) >= hitDie || target.getBody() < 1 || weapName.contains("grenade-of-antioch"))
         {                          //we hit our target
            int damageBonus = this.getAgility();
            if(weapName.contains("throwing-stone") || weapName.contains("sling"))
            {                       //less of a damage bonus for throwing stones
               damageBonus /= 2;
            }
            dam =Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage())  + damageBonus;
            if(weapon.getEffect().contains("freeze") &&  CultimaPanel.weather > 0 && !indoors)     //freeze effect does more damage when it is raining
               dam =Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage())  + this.getAgility() + CultimaPanel.weather;  
            int critDie =Utilities.rollDie(400);
            double legendBonus = 1;
            if(Weapon.isLegendaryWeapon(weapName))
               legendBonus = 1.25;
            if (critDie <= getAgility()*legendBonus)
            {
               dam =  weapon.getMaxDamage()*2;
               critical = true;
            }
            if(weapon.getEffect().contains("fire") || weapon.getEffect().contains("freeze") || weapon.getEffect().contains("poison"))
            {
               if(weapon.getEffect().contains("freeze"))
               {
                  target.setMoveTypeTemp(NPC.STILL);
                  int effectDie = Utilities.rollDie((this.getLevel() + target.getLevel()*2));
                  if(this.getLevel() > effectDie || (CultimaPanel.weather > 0 && !indoors))
                     target.addEffect(weapon.getEffect());
               }
               else if(weapon.getEffect().contains("fire") && CultimaPanel.weather > 0 && !indoors)
               {   
                  if(Utilities.rollDie(1,6) <= CultimaPanel.weather)
                     CultimaPanel.sendMessage("Fire extinguished!");
                  else
                     target.addEffect(weapon.getEffect());
               }
               else  
                  target.addEffect(weapon.getEffect());
            }
            else if((weapon.getEffect().contains("curse") || weapon.getEffect().contains("control") || weapon.getEffect().contains("stun")) && !target.hasEffect(weapon.getEffect()))
            {
               int effectDie = Utilities.rollDie((this.getLevel() + target.getLevel()*2));
               if(this.getLevel() > effectDie)
                  target.addEffect(weapon.getEffect());
            }
            if(weapName.contains("grenade-of-antioch"))
            {  
               Utilities.surroundWithFire(target);
            }
         }
         if(altAttack && ( Weapon.isDagger(weapName) || Weapon.isSpear(weapName)))    //alternate attack for daggers and spears - throw it
         {  //remove the dagger/spear from our inventory, go to unarmed weapon and give the dagger/spear to the target (to hopefully collect from its corpse)
            if((weapName.contains("carnwennan") || weapName.contains("gungnir")) && Math.random() < agilityPercent)
            {
               CultimaPanel.sendMessage("Thy "+weapName+" returns to thee!");
            }
            else
            {
               Weapon dropped = this.getWeapon();
               this.discardCurrentWeapon();
               thrownWeapon = dropped;
            }
         }
         else if(weapName.contains("grenade-of-antioch"))
         {  //we just threw our one grenade - remove it from our inventory
            this.discardCurrentWeapon();
         }
         else if(isHalberdSpin)
         {//alternate attack for halberd
            int range = 3;
            String[] effect ={};
            int minDamage = 1;
            int maxDamage = 1;
            pushNPCsAway(range, effect, minDamage, maxDamage, "Halberd-spin");
            CultimaPanel.flashColor = new Color(127, 127, 175, 100);
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 16;   
         }
      }
      else //if (isMelee)
      {
         boolean isShieldBash = (weapon.getName().toLowerCase().contains("shield-bash"));
         int hitDie =Utilities.rollDie(this.getMight()+target.getAgility());
         double bonusTemp = 1;
         if(heightBonus > 1 || Weapon.isLegendaryWeapon(weapName) || isShieldBash)
            bonusTemp = 1.25;
         if(target.hasEffect("control") || target.hasEffect("freeze") || isShieldBash || ((NPC.isUndead(target.getCharIndex()) || target.isZombie()) && weapName.contains("life"))  || NPC.isShip(target.getCharIndex()))
            bonusTemp *= 2;
         if((int)(this.getMight()*bonusTemp) >= hitDie || target.getBody() < 1 || (vampyricBite && target.getMoveType()==NPC.STILL))
         {                    //we hit our target
            if(weapName.contains("dagger"))
               dam =Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage())  + this.getAgility();
            else
               dam =Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage()) + this.getMight();      
            if(weapon.getEffect().contains("freeze") &&  CultimaPanel.weather > 0 && !indoors)     //freeze effect does more damage when it is raining
               dam =Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage())  + this.getMight() + CultimaPanel.weather;  
            int critDie =Utilities.rollDie(400);
            double legendBonus = 1;
            if(Weapon.isLegendaryWeapon(weapName) && mightyBlow)
               legendBonus = 1.5;        //higher chance of critical hit if we have a legendary weapon or we are doing an alternate mighty blow with a sword or hammer
            else if(Weapon.isLegendaryWeapon(weapName) || mightyBlow)
               legendBonus = 1.25;        //higher chance of critical hit if we have a legendary weapon or we are doing an alternate mighty blow with a sword or hammer
            if (critDie <= getMight()*legendBonus)
            {                 
               dam =  weapon.getMaxDamage()*2;
               critical = true;
            }
            if(weapon.getEffect().contains("fire") || weapon.getEffect().contains("freeze") || weapon.getEffect().contains("poison"))
            {
               if(weapon.getEffect().contains("freeze"))
               {
                  target.setMoveTypeTemp(NPC.STILL);
                  int effectDie = Utilities.rollDie((this.getLevel() + target.getLevel()*2));
                  if(this.getLevel() > effectDie || (CultimaPanel.weather > 0 && !indoors))
                     target.addEffect(weapon.getEffect());
               }
               else if(weapon.getEffect().contains("fire") && CultimaPanel.weather > 0 && !indoors)
               {   
                  if(Utilities.rollDie(1,6) <= CultimaPanel.weather)
                     CultimaPanel.sendMessage("Fire extinguished!");
                  else
                     target.addEffect(weapon.getEffect());
               }
               else
               {
                  target.addEffect(weapon.getEffect());
               }
            }
            else if((weapon.getEffect().contains("curse") || weapon.getEffect().contains("control") || weapon.getEffect().contains("stun")) && !target.hasEffect(weapon.getEffect()))
            {
               int effectDie = Utilities.rollDie((this.getLevel() + target.getLevel()*2));
               if(this.getLevel() > effectDie)
                  target.addEffect(weapon.getEffect());
            }
            else if(vampyricBite &&(Utilities.rollDie(this.getAgility()) >Utilities.rollDie(target.getAgility()*4) || (target.getMoveType()==NPC.STILL && CultimaPanel.isNight)))
            {
               if(NPC.isGuard(target.getCharIndex()) && !targetTerrain.contains("bed"))
               {}    //a guard that is not sleeping in a bed is standing guard, so they will not get the "still" effect
               else
                  target.addEffect(weapon.getEffect());
            }
            if(isShieldBash)
            {  //alternate attack for sword and shield/buckler
               int range = 2;
               String[] effect ={"stun"};
               int minDamage = 1;
               int maxDamage = 1;
               pushNPCsAway(range, effect, minDamage, maxDamage, "Shield-bash");
               CultimaPanel.flashColor = new Color(127, 127, 175, 100);
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 16;
            }
            else if(Weapon.isDual(weapName) && altAttack)
            {  //see if dual weapon strikes any adjacent targets as well
               boolean multipleHit = Utilities.areaOfEffectAttack(target, 1, weapon);
               if(multipleHit)
               {
                  CultimaPanel.sendMessage("Spinning attack!");
                  CultimaPanel.flashColor = new Color(127, 127, 175, 100);
                  CultimaPanel.flashFrame = CultimaPanel.numFrames;
                  CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 16;
               }
            }
            else if(Weapon.isGreatAxe(weapName) && altAttack)
            {  //see if dual weapon strikes any adjacent targets as well
               boolean multipleHit = Utilities.wideSwipe(target, 1, weapon);
               if(multipleHit)
               {
                  CultimaPanel.sendMessage("Swipe attack!");
                  CultimaPanel.flashColor = new Color(127, 127, 175, 100);
                  CultimaPanel.flashFrame = CultimaPanel.numFrames;
                  CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 16;
               }
            }
         }
      }
      if(target.getName().equals("Skara Brae") && !target.hasBeenAttacked())
      {
         CultimaPanel.sendMessage("Thy precious magic items turn to dust!");
         this.clearMagicItems();
         this.clearMagicWeapons();
         this.clearMagicArmor();
      }
      if(target.getCharIndex()==NPC.GOBLINBARREL && !target.hasBeenAttacked() && Math.random() < 0.75)
      {  //before a goblin in a barrel gets attacked, it just looks like a barrel.  So set it to hasBeenAttacked so we can see its arms and head sticking out (done in Display)
         target.setHasBeenAttacked(true);
         target.setHitTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
         Sound.NPCdamage(50, 150);
         return null;
      }
      target.setHasBeenAttacked(true);
      //chance a demon will teleport closer
      if((target.getCharIndex()==NPC.DEMON || target.getCharIndex()==NPC.DEMONKING) && distance >= 3 && Math.random() < 0.25)
      {
         ArrayList<Coord> teleSpots = new ArrayList<Coord>();
         for(int dr=-3; dr<=3; dr++)
            for(int dc=-3; dc<=3; dc++)
            {
               if(dr==0 && dc==0)
                  continue;
               int teleRow = this.getRow()+dr;
               int teleCol = this.getCol()+dc;
               if(this.getMapIndex()==0)
               {
                  teleRow = CultimaPanel.equalizeWorldRow(teleRow);
                  teleCol = CultimaPanel.equalizeWorldCol(teleCol);
               }
               else if(teleRow < 0 || teleCol < 0 || teleRow >= currMap.length || teleCol >= currMap[0].length)
                  continue;
               if(!LocationBuilder.canSpawnOn(currMap, teleRow, teleCol))
                  continue;    
               teleSpots.add(new Coord(teleRow, teleCol));
            }
         if(teleSpots.size() > 0)
         {
            int oldRow = target.getRow();
            int oldCol = target.getCol();
            dam = 0;
            Coord teleportTo = Utilities.getRandomFrom(teleSpots);
            target.setRow(teleportTo.getRow());
            target.setCol(teleportTo.getCol());   
            CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, oldRow, oldCol, this.getMapIndex(), this.getLocationType()));
         }   
      }  //chance a LICHE or BEHOLDER, when close, will teleport further away and in line with player
      else if((target.getCharIndex()==NPC.LICHE || target.getCharIndex()==NPC.BEHOLDER || target.getName().equals("Skara Brae")) && distance < 3 && Math.random() < 0.25)
      {
         ArrayList<Coord> teleSpots = new ArrayList<Coord>();
         for(int dr=-CultimaPanel.SCREEN_SIZE/2; dr<=CultimaPanel.SCREEN_SIZE/2; dr++)
            for(int dc=-CultimaPanel.SCREEN_SIZE/2; dc<=CultimaPanel.SCREEN_SIZE/2; dc++)
            {
               if(Math.abs(dr)<3 && Math.abs(dc)<3)
                  continue;
               int teleRow = this.getRow()+dr;
               int teleCol = this.getCol()+dc;
               if(this.getMapIndex()==0)
               {
                  teleRow = CultimaPanel.equalizeWorldRow(teleRow);
                  teleCol = CultimaPanel.equalizeWorldCol(teleCol);
               }
               else if(teleRow < 0 || teleCol < 0 || teleRow >= currMap.length || teleCol >= currMap[0].length)
                  continue;
               if(!LocationBuilder.canSpawnOn(currMap, teleRow, teleCol))
                  continue;    
               teleSpots.add(new Coord(teleRow, teleCol));
            }
         if(teleSpots.size() > 0)
         {
            int oldRow = target.getRow();
            int oldCol = target.getCol();
            dam = 0;
            Coord teleportTo = Utilities.getRandomFrom(teleSpots);
            target.setRow(teleportTo.getRow());
            target.setCol(teleportTo.getCol());
            CultimaPanel.flashColor = Color.red.darker().darker();
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4; 
            Sound.teleport();  
         }   
      }
      //chance a hydraclops will cast a raise-water spell
      if((target.getCharIndex()==NPC.HYDRACLOPS || target.getCharIndex()==NPC.SQUIDKING) && distance >= 3 && Math.random() < 0.25 && target.getNumInfo()>0)
      {
         Utilities.raiseWater(target.getRow(), target.getCol(), target.getMapIndex());
         target.setNumInfo(target.getNumInfo()-1);
      }
      if((NPC.isUndead(target) || target.isZombie() || NPC.isVampire(target)) && weapName.contains("life"))
         dam *= 5;     //massive damage done to undead with a life weapon
      else if(NPC.isVampire(target))
      {
         if(weapName.contains("caster") || weapName.contains("crossbow") || weapon.getEffect().contains("fire"))
         {}    //crossbows and fire weapons do standard damage to vampires
         else
         {     //melee weapons do 1/4 damage, and other ranged weapons might do none
            if(weapon.getRange() > 1 && Math.random() < 0.5)
               dam = 0;
            else
               dam /= 4;
         }
      }
      else if(target.getCharIndex()==NPC.WEREWOLF)
      {
         if(weapName.contains("royal") || Weapon.isLegendaryWeapon(weapName))
         {}    //royal weapons are silver and do standard damage to werewolves
         else
         {     //other weapons do 1/4 damage - ranged weapons might do none
            if(weapon.getRange() > 1 && Math.random() < 0.5)
               dam = 0;
            else
               dam /= 4;
         }
      }
      else if(NPC.isBigShip(target))
      {
         if(target.hasMet())
         {
            if(weapName.contains("bow")|| weapName.contains("caster"))
               dam = 1;
            else
               dam /= 8;
         }
         else if(target.getBody() - dam <= 0 && !weapon.getEffect().contains("fire"))
         {  //if we haven't taken over the ship yet, the next shot would end it, so just kill the crew
            target.setBody(101);
            dam = 1;
            target.setHasMet(true);
            CultimaPanel.sendMessage("The ship is left afloat! It's crew has been defeated!");
         }
      }
      else if((weapName.contains("bow")|| weapName.contains("caster")) && target.isEtheral())
         dam = 0;    //ghosts and barrels aren't damaged by bows
      if(target.getCharIndex()==NPC.SLIME && !weapon.getEffect().contains("fire") && !weapon.getEffect().contains("freeze") && !weapName.contains("lightning"))
      {
         dam = 0;       // SLIME will only take damamge from cold/fire/electricity, split otherwise
         boolean success = Utilities.summonMonster(NPC.SLIME, target.getRow(), target.getCol(), 1, target);
         target.setHasBeenAttacked(true);   
         if(success)
            CultimaPanel.sendMessage("The slime splits!");
      }
      if(target.getName().contains("Black Knight") && !Weapon.isLegendaryWeapon(weapName))
      {                 //the Black Knight is only significantly damaged by legendary weapons
         dam /= 10;
      }
      if(dam > 0 || turnedStone)
      {
         if(thrownWeapon != null)
         {              //we threw a spear or dagger at them and hit, so add this weapon into their inventory so their corpse will have it
            target.addItem(thrownWeapon);
         }
         if((isGarriott || target.getCharIndex()==NPC.SLIME || target.getName().equals("Skara Brae")) && Math.random() < 0.75)       //Richard Garriott has awesome armor
            dam /= 2;
         if(weapName.contains("fist") && !this.hasItem("iron-bracer") && !this.hasItem("clawed-glove"))
            dam /=2;   
         if(weapName.contains("fist") && this.hasItem("clawed-glove"))  
            dam = (int)(dam * 1.5);        
         if(target.getCharIndex()==NPC.BARREL && weapon.getEffect().contains("fire"))
         {
            if(target.getItems() != null && target.getItems().size() > 0)
            {
               for(int i=target.getItems().size()-1; i>=0; i--)
               {
                  Item it = target.getItems().get(i);
                  if((it instanceof Potion && Math.random() < 0.75) || Math.random() < 0.5)
                     target.getItems().remove(i);   
               }
            }
            if(target.getArmor() != null && !target.getArmor().isMetal() && Math.random() < 0.75)
               target.setArmor(Armor.getNone());
            if(target.getWeapon() != null && target.getWeapon().hasWood() && Math.random() < 0.75)
               target.setArmor(Armor.getNone());    
         }
         else if(target.getCharIndex()==NPC.GOBLINBARREL)      //saving throw for barrel armor
         { 
            if(!weapon.getEffect().contains("fire"))
            {
               int numSides = 60;
               int roll = Utilities.rollDie(numSides);
               double save = Math.max(0, (1 - (roll/100.0)));
               dam = (int)(dam * save);
            }
            if(dam >= target.getBody()/2)                   //break the barrel
            {//make a barrel corpse, remove the goblinbarrel and put a regular goblin in its place (with the same health)
               dam = 0;
               int newMight = target.getMight();
               int newMind = target.getMind();
               int newAgility = target.getAgility();
               int newHealth = target.getBody();
               int newRow = target.getRow();
               int newCol = target.getCol();
               int newMapIndex = target.getMapIndex();
               String newLocType = target.getLocationType();
               NPCPlayer barrel = new NPCPlayer(NPC.BARREL, newRow, newCol, newMapIndex, newLocType);
               Corpse deadBarrel = barrel.getCorpse();
               deadBarrel.setHitTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
               CultimaPanel.corpses.get(this.getMapIndex()).add(deadBarrel);
               Utilities.removeNPCat(newRow, newCol, newMapIndex);
               NPCPlayer goblin = new NPCPlayer(NPC.GOBLIN, newRow, newCol, newMapIndex, newLocType);
               goblin.setMight(newMight);
               goblin.setMind(newMind);
               goblin.setAgility(newAgility);
               goblin.setBody(newHealth);
               CultimaPanel.worldMonsters.add(goblin);
               Sound.NPCdamage(50, 150);
               return null; 
            }
         }
         if(!weapon.getEffect().contains("control") && target.hasEffect("control")) //harming an entity under your spell will wake them from it
            target.removeEffect("control");   
         if(this.isWolfForm() && rations == 0)
            rations = 1;
         if(weapName.contains("vampyric") && !NPC.isShip(target.getCharIndex()) && !NPC.isNatural(target.getCharIndex()))
         {
            boolean success = (Math.random() < 0.25);
            if(weapon.getName().toLowerCase().contains("bite"))
            {
               if(NPC.isHumanoid(target.getCharIndex()))      //vampire bite
                  success = true;
               else
                  success = false;
            }
            if(success)
            {
               if(weapon.getName().toLowerCase().contains("bite"))
               {
                  this.heal(dam);
                  if(this.getBody() == this.getHealthMax())
                  {
                     this.rations = 1;
                  }
               }
               else 
                  this.heal(dam/3);
               CultimaPanel.flashColor = Color.red.darker();
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
               CultimaPanel.sendMessage("Life consumed!");
            }
         }
         else if(weapon.getName().toLowerCase().contains("icestrike"))
         {
            CultimaPanel.flashColor = Color.white;
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 2;
         }
         else if(weapon.getName().toLowerCase().contains("deathtouch"))
         {
            CultimaPanel.flashColor = Color.black;
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
         }
         else if(weapon.getName().toLowerCase().contains("fireball"))
         {
            CultimaPanel.flashColor = Color.orange;
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 2;
         }
         if(isMagic)                                        //specific experience point for successfully damaging a target by specific means
            specificExperience[MIND]++;
         else if(isRanged)
            specificExperience[AGILITY]++;
         else 
            specificExperience[MIGHT]++;
         double arrowRecoverProb = 0.75;
         double magicArrowRecoverProb = 0.5;
         if(this.hasItem("swiftquill") || this.hasItem("bow-bracer"))
         {
            arrowRecoverProb = 0.95;
            magicArrowRecoverProb = 0.75;
         }
         if(this.hasItem("swiftquill") && this.hasItem("bow-bracer"))
         {
            arrowRecoverProb = 0.975;
            magicArrowRecoverProb = 0.8;
         }
         if(!NPC.isEtheral(target.getCharIndex()) && (weapName.contains("bow") || weapName.contains("caster")))  
         {                    //lodge an arrow in the target
            if((weapon.getEffect().contains("freeze") || weapon.getEffect().contains("fire")))
            {                //50% chance frozen and fire arrows can't be recovered
               if(Math.random() < magicArrowRecoverProb)
                  target.addArrow();
            }
            else 
            {
               int numArrows = 1;
               if(weapName.contains("karna") && altAttack)
                  numArrows = 2;
               for(int na = 0; na < numArrows; na++)
               {
                  if(Math.random() < arrowRecoverProb) //25% chance an arrow breaks and can't be recovered
                  {
                     target.addArrow();
                  }
               }
            }
         }
         if(target.getMoveType()==NPC.STILL && target.getCharIndex()!=NPC.TREE && !NPC.isGuard(target.getCharIndex()) && !targetTerrain.contains("bed"))
            dam *= 1.5;          //50% more damage if the target isn't expecting it
         if(target.getCharIndex()==NPC.CHUD)
         {
            if(weapon.getEffect().contains("fire"))
               dam = (int)(dam * 1.5);
            else
               dam /= 4;     
         }
         if(target.getCharIndex()==NPC.SEAMONSTER)
         {
            if(weapon.getEffect().contains("freeze"))
               dam /=10;
            else if(weapon.getEffect().contains("fire"))
               dam = (int)(dam * 1.5);
         }
         else if(target.getCharIndex()==NPC.DRAGON || target.getCharIndex()==NPC.DRAGONKING || target.getCharIndex()==NPC.DEMON || target.getCharIndex()==NPC.DEMONKING)
         {
            if(weapon.getEffect().contains("freeze"))
               dam = (int)(dam * 1.5);
            else if(weapon.getEffect().contains("fire"))
               dam /=10;
         }
         if(target.getCharIndex()==NPC.FIRE)
         {
            if(weapon.getEffect().contains("freeze"))
               dam = (int)(dam * 10);
            else if(weapon.getEffect().contains("fire") || weapName.contains("bow") || weapName.contains("caster"))
            {
               target.heal(dam);
               dam = 0;
            }
            else
               dam = 0;
         }
         else if(target.getCharIndex()==NPC.MAGMA)
         {
            if(weapon.getEffect().contains("freeze"))
               dam = (int)(dam * 10);
            else if(weapon.getEffect().contains("fire"))
            {
               target.heal(dam);
               dam = 0;
            }
         }
         else if((target.getCharIndex()==NPC.MONOLITH || isStatue) && this.getImageIndex()!=HAMMER && !weapon.getName().toLowerCase().contains("phasewall"))
         {
            dam = 0;
         }
         else if((weapName.contains("bow")|| weapName.contains("caster")) && (target.getCharIndex()==NPC.BARREL && !weapon.getEffect().contains("fire")))
         {
            dam = 1;    //ghosts and barrels aren't damaged much by bows
         }
         else if(target.getCharIndex()==NPC.DEATH)
         {
            if(this.onShip() || (this.hasItem("blessed-crown") && weapon.getName().toLowerCase().contains("royal") && weapon.getName().toLowerCase().contains("life")))
            {}
            else
            {
               CultimaPanel.sendMessage("No effect!");
               dam = 0;
            }
         }
         if(weapon.getName().toLowerCase().contains("vampyric-bite"))
            target.damage(dam, "vamp");   //alerts NPCPlayer to make a different sound
         else
            target.damage(dam, "");
         if(!target.getLocationType().contains("pacrealm"))   
         {  //since eliminating a ghost in pac-realm only makes it teleport to the respawn area, we don't want to show the hit vector
            target.setHitTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
         }
         if(target.getCharIndex()==NPC.MONOLITH || isStatue) 
         {
            if(dam==0)
               CultimaPanel.sendMessage("Statue takes no damage.");
            else
               CultimaPanel.sendMessage("Statue takes "+dam+" damage!");
         }
         else if(target.getCharIndex()==NPC.BARREL) 
         {
            if(dam==0)
               CultimaPanel.sendMessage("Barrel takes no damage.");
            else
               CultimaPanel.sendMessage("Barrel takes "+dam+" damage!");
         }
         else if(NPC.isShip(target) && target.hasMet())
         {
            if(dam==0)
               CultimaPanel.sendMessage("Ship takes no damage.");
            else
               CultimaPanel.sendMessage("Ship takes "+dam+" damage!");
         }
         else if(target.getCharIndex()!=NPC.FIRE)
         {
            if(turnedStone)
               CultimaPanel.sendMessage("Foe is turned to stone!");
            else if(critical && dam > weapon.getMaxDamage())
               CultimaPanel.sendMessage("Foe suffers critical hit for "+dam+" damage!");
            else if(dam < weapon.getMaxDamage()/2)
               CultimaPanel.sendMessage("Foe partially blocks for "+dam+" damage!");
            else
               CultimaPanel.sendMessage("Foe suffers "+dam+" damage!");
         }
         if(target.getName().contains("Black Knight") && target.getBody() <= 0)
         {
            byte newCharIndex = NPC.DARKKNIGHT4;
            if(target.getCharIndex() == NPC.DARKKNIGHT4)
               newCharIndex = NPC.DARKKNIGHT3;
            else if(target.getCharIndex() == NPC.DARKKNIGHT3)
               newCharIndex = NPC.DARKKNIGHT2; 
            else if(target.getCharIndex() == NPC.DARKKNIGHT2)
               newCharIndex = NPC.DARKKNIGHT1;
            else if(target.getCharIndex() == NPC.DARKKNIGHT1)
            {
               newCharIndex = NPC.DARKKNIGHT0;
               target.setMoveTypeTemp(NPC.STILL);
               target.setReputation(-1000);
            }
            if(target.getCharIndex() != NPC.DARKKNIGHT0)
            {
               NPCPlayer newKnight = new NPCPlayer(newCharIndex, target.getRow(), target.getCol(), target.getMapIndex(), target.getRow(), target.getCol(), target.getLocationType());
               Utilities.removeNPCat(target.getRow(), target.getCol(), target.getMapIndex());
               CultimaPanel.civilians.get(target.getMapIndex()).add(newKnight);
               target = newKnight;
               //pause to allow dialogue
               CultimaPanel.selected = target;
               target.setTalking(true);
               CultimaPanel.talkSel = true;
               CultimaPanel.sendMessage("-----");
               CultimaPanel.sendMessage("~"+ NPC.getBlackKnightResponse(target));
               CultimaPanel.sendMessage("-----");
               return target;
            }   
         }
         if(target.getBody() <= 0 || turnedStone)                                   //target is killed
         {  //check for achievements
            if(target.getCharIndex()==NPC.SHARK && !this.isSailing() && playerTerrain.contains("water"))
               Achievement.earnAchievement(Achievement.BRUCE_IS_LOOSE);
            if(weapon.getName().toLowerCase().contains("fists") && target.getCharIndex()!=NPC.BARREL)
               Achievement.earnAchievement(Achievement.HANDS_OF_STONE);
            if(this.hasEffect("sullied") || this.hasEffect("seduced"))
               Achievement.earnAchievement(Achievement.DEADLY_WHEN_DIZZY);
            if(target.getCharIndex()==NPC.DEATH)
               Achievement.earnAchievement(Achievement.KILLED_BY_DEATH);
            if(target.getName().trim().equalsIgnoreCase(this.getShortName().trim()))
               Achievement.earnAchievement(Achievement.TWISTED_SISTER); 
            if(target.getCharIndex()==NPC.JESTER && (target.getMoveType()==NPC.RUN || target.getMoveType()==NPC.CHASE) && (inHome(this.getMapIndex(), this.getRow(), this.getCol()) || inHome(target.getMapIndex(), target.getRow(), target.getCol())))
               Achievement.earnAchievement(Achievement.HOLD_YER_GROUND);
            if(NPC.isMonsterKing(target.getCharIndex()) && TerrainBuilder.habitablePlace(this.getLocationType()))
               Achievement.earnAchievement(Achievement.KAIJU_KILLER);      
            if(target.isStatue())
            {
               if(getLocationType().contains("temple") && onDungeonEntrance(target.getMapIndex(), target.getRow(), target.getCol()))
               {
                  Achievement.earnAchievement(Achievement.INDY_2500);
               }
               //check to see if we just destroyed the statue from the Ceremony mission, and if so, kill the cultists and put a ring of fire around us
               for(int i=0; i<CultimaPanel.missionStack.size(); i++)
               {
                  Mission m = CultimaPanel.missionStack.get(i);
                  Location missLoc = null;
                  Item missTarget = null;
                  if(m.getType().contains("Ceremony"))
                  {
                     missLoc = CultimaPanel.getLocation(m.getClearLocation());
                     missTarget = m.getTarget();
                     int mapIndex = target.getMapIndex();
                     if(target.hasItem(missTarget.getName()) && this.getMapIndex() == missLoc.getToMapIndex())
                     {
                        Utilities.surroundWithFire();
                        Utilities.purgeCultists(mapIndex);
                        target.clearItems();
                        this.addItem(missTarget.getName());
                        CultimaPanel.numChants = -1;
                        break;
                     }
                  }
               }
            
            }
           //if killed by cannon-fire, chance items are destroyed
            if(this.isSailing())
            {
               if(Math.random() < 0.5)
                  target.clearItemsExceptBounties();
               if(Math.random() < 0.75)
                  target.setWeapon(Weapon.getNone());
               if(Math.random() < 0.75)
                  target.setArmor(Armor.getNone());
               if(Math.random() < 0.5)
                  target.setGold(0); 
               if(target.getCharIndex()==NPC.GREATSHIP || target.getCharIndex()==NPC.BRIGANDSHIP)
                  Achievement.earnAchievement(Achievement.CAPTAIN_CRUNCH);      
            }
            if(this.getLocationType().contains("arena"))
               Sound.applauseWin(Utilities.rollDie(50,100));
            if(Math.random() < 0.5 && (target.getCharIndex()==NPC.OFFICER || target.getCharIndex()==NPC.SOLDIER) && !turnedStone)
            {  //have German soldier yell out a Wolfenstein quote
               CultimaPanel.sendMessage("~"+Utilities.getRandomFrom(NPC.wolfensteinDeath));
            }
            if(target.getCharIndex()==NPC.BARREL)
               CultimaPanel.barrelsToRestore.add(new RestoreItem(target.getMapIndex(), target.getRow(), target.getCol(), (byte)(-1), Utilities.getChestRestoreDay(target.getLocationType())));
            
            if(isMagic)                      //extra specific experience point for killing a target by specific means
               specificExperience[MIND]++;
            else if(isRanged)
               specificExperience[AGILITY]++;
            else 
               specificExperience[MIGHT]++;
         
            if(target.getName().equals(this.getDogName()))
            {
               this.setDogBasicInfo("none");
               this.addReputation(-5);
            }
            if(target.getName().equals("Skara Brae") || target.getCharIndex()==NPC.DEATH)
            {
               int diff = this.expNeededForNextLevel() - this.getExperience();
               if(diff > 0)
                  this.addExperience(diff);
               if(target.getCharIndex()==NPC.DEATH)
               {
                  for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
                  {
                     Mission m = CultimaPanel.missionStack.get(i);
                     if(m.getType().equals("Death"))
                     {
                        CultimaPanel.missionStack.remove(i);
                        Player.stats[Player.MISSIONS_COMPLETED]++;
                        this.addExperience(1000);
                        CultimaPanel.sendMessage("Death is sent back to the Underworld.  Mission complete!");
                        FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
                        break;
                     }
                  }
               }   
            }
            if(target.getCharIndex()==NPC.MALEVAMP || target.getCharIndex()==NPC.FEMALEVAMP)
               removeEffect("seduced");
            if(turnedStone)
               addExperience(target.getLevel());
            boolean mercyKill = false;
         //remove any current missions with players in this location since they are dead
            String NPCmiss = target.getMissionInfo();
            if(!NPCmiss.equals("none"))
            {
               for(int i=0; i<CultimaPanel.missionStack.size(); i++)
               {
                  Mission m = CultimaPanel.missionStack.get(i);
                  if(NPCmiss.equals(m.getInfo()))
                  {
                     if(m.getType().equals("Mercy"))
                     {
                        if(weapName.contains("royal"))
                        {
                           Player.stats[Player.MISSIONS_COMPLETED]++;
                           this.addExperience(100);
                           CultimaPanel.sendMessage("Mercy mission complete!");
                           mercyKill = true;
                        }
                        else
                           CultimaPanel.sendMessage(target.getName()+" is slain with a weapon that is not Royal. Mercy mission failed.");
                     }
                     CultimaPanel.missionStack.remove(i);
                     FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
                     break;
                  }
               }
            }
         
         //remove target, add to restore, add Corpse, adjust reputation
            if(NPC.isCivilian(target.getCharIndex()) && (TerrainBuilder.habitablePlace(getLocationType()) || getLocationType().contains("arena")))
            {  //after a month, NPCs will repopulate a city
               NPCPlayer temp = target.clone();
               if(target.getName().toLowerCase().contains("arenamaster"))
                  temp.setRestoreDay(CultimaPanel.days+Utilities.rollDie(7));
               else
                  temp.setRestoreDay(CultimaPanel.days+15+Utilities.rollDie(15));
               if(isZombie)		//unzombify our replacement
               {
                  temp.setNumInfo(0);
               }
               else if(Math.random() < 0.5)           //civilian crys out
               {
                  CultimaPanel.sendMessage("~"+Utilities.getRandomFrom(NPC.deathScream));
               }   
               CultimaPanel.NPCsToRestore.add(temp);
            }
            else if(this.getMapIndex() > 0 && !NPC.isCivilian(target.getCharIndex()))
            {  //remove that monster from the monster frequenices at that location
               Location currLoc = CultimaPanel.allLocations.get(this.getMapIndex());
               if(currLoc != null)
               {
                  if(location.contains("battlefield"))
                  { //the bow-assassin is on the human team in a battle, so don't decrease from the monster count if they are killed
                     if(target.getCharIndex() != NPC.BOWASSASSIN)
                        currLoc.removeMonster(target.getCharIndex());
                  }
                  else
                     currLoc.removeMonster(target.getCharIndex());
               }  
            }
            if(NPC.isCivilian(target.getCharIndex()) && !isZombie && !isStatue && !this.getLocationType().equals("ship") && !mercyKill && !target.getName().contains("Masterthief"))
            {                                                                    //target is a person that is not a zombie                                                               
               stats[PEOPLE_MURDERED]++;     
               if(target.hasBeenRescued())                                       //remove the reputation points for rescuing the person you just killed
                  this.addReputation(-5);
               if(target.hasTrained() || target.getCharIndex() == NPC.KING || (target.getCharIndex() == NPC.TAXMAN && target.getBounty()==0)) 
               {  //penalty for killing someone who trained you
                  bounty = 100;
                  this.addReputation(-100);
               }
               else
               {
                  bounty++;
               }
               if(target.getReputation() >= 10 || target.hasTrained() || target.getCharIndex() == NPC.CHILD || target.getName().equalsIgnoreCase("Keeper of the Bridge") || target.getName().startsWith("+"))
               {  //roll to see if you get cursed
                  int numSides = 1000;
                  if(target.getCharIndex() == NPC.KING)
                  {
                     stats[ROYALTY_SLAIN]++;
                     numSides = 500;
                  }
                  int curseRoll =Utilities.rollDie(numSides);
                  if(!this.isVampire() && !this.isWerewolf() && !target.isWerewolf() && (curseRoll < target.getReputation() || target.hasTrained() || target.getCharIndex() == NPC.CHILD || target.getName().equalsIgnoreCase("Keeper of the Bridge") || target.getName().startsWith("+")))
                  {
                     CultimaPanel.sendMessage("The gods send down a curse for this vile murder!");
                     if(target.hasTrained())
                     {
                        CultimaPanel.sendMessage("Thy precious magic items turn to dust!");
                        this.clearMagicItems();
                        this.clearMagicWeapons();
                        this.clearMagicArmor();
                     }
                     else
                     {
                        if(this.hasItem("pentangle"))
                        {
                           if(Math.random() < .1)
                           {
                              CultimaPanel.sendMessage("Thy Pentangle shatters!");
                              this.removeItem("pentangle");
                           }
                           else
                              CultimaPanel.sendMessage("Thy Pentangle trembles and grows hot!");
                        }
                     }
                     this.addEffectForce("curse");  
                     if(target.hasTrained() || ((target.getName().startsWith("+") || target.getCharIndex() == NPC.CHILD) && Math.random() < 0.33 ))
                     {
                        this.addEffectForce("sullied");
                        this.setSulliedValue(200);
                     }                   
                  }   
               }
            }
            else if(target.getCharIndex() == NPC.DRAGON || target.getCharIndex() == NPC.DRAGONKING)
            {
               stats[DRAGONS_SLAIN]++;
               if(target.getCharIndex() == NPC.DRAGONKING)
                  stats[ALPHAMONSTERS_KILLED]++;
            }
            else if(NPC.isMonsterKing(target.getCharIndex()))
            {
               if(target.getCharIndex() == NPC.SPIDERKING && !this.hasItem("spidersweb") && this.hasMagicStaff())
               {
                  this.addWeapon(Weapon.getSpidersWeb());
                  CultimaPanel.sendMessage("You learn the Spidersweb spell!");
               }
               stats[ALPHAMONSTERS_KILLED]++;
            }
            else if(target.getCharIndex() == NPC.GIANT)
               stats[GIANTS_SLAIN]++;
            else if(target.getCharIndex() == NPC.BRIGANDSHIP || target.getCharIndex() == NPC.GREATSHIP)
               stats[SHIPS_SCUTTLED]++;
            else if(NPC.isAssassin(target.getCharIndex()))
            {
               if(TerrainBuilder.habitablePlace(this.getLocationType()))
               {  //only drop bounty if killed an assassin in a town or castle because they were sent after you (as opposed to being in a dungeon or battlefield)
                  if(this.getReputation()>=100)
                     this.setBounty(1);
                  else
                     this.setBounty(this.getBounty()/2);
               }
               stats[ASSASSINS_KILLED]++;
            }
            else if(target.getCharIndex() == NPC.ELK || target.getCharIndex() == NPC.HORSE || target.getCharIndex() == NPC.RABBIT || target.getCharIndex() == NPC.PIG || target.getCharIndex() == NPC.CHICKEN)
               stats[GAME_KILLED]++;
            else if(target.getCharIndex() == NPC.SHARK || target.getCharIndex() == NPC.FISH || target.getCharIndex() == NPC.SHRIEKINGEEL)
               stats[FISH_CAUGHT]++;
            else if(target.getCharIndex() != NPC.BARREL && !target.isStatue())
               stats[MONSTERS_KILLED]++;
            if(!turnedStone)
            {   
               if(target.getCharIndex()==NPC.BRIGANDRIDER)
               {  //kill the rider and leave the horse
                  int newRow = target.getRow();
                  int newCol = target.getCol();
                  int newMapIndex = target.getMapIndex();
                  String newLocType = target.getLocationType();
                  NPCPlayer dhorse = new NPCPlayer(NPC.HORSE, newRow, newCol, newMapIndex, newLocType);
                  if(this.getMapIndex()!=0 || Math.random() < 0.5)
                     dhorse.setHasBeenAttacked(true);
                  else
                     dhorse.setMoveTypeTemp(NPC.RUN);
                  CultimaPanel.worldMonsters.add(dhorse);
               
               }
               if((target.getCharIndex()==NPC.MAZEMOUTH || target.getCharIndex()==NPC.GHOST) && target.getLocationType().contains("pacrealm"))
               {
                  Color dotColor = (Color.blue.brighter().brighter());
                  if(target.getCharIndex()==NPC.MAZEMOUTH)
                  {
                     dotColor = Color.yellow;
                  }
                  int plX = target.getNpcX();
                  int plY = target.getNpcY();  
                  for(int i=0; i<5 && CultimaPanel.smoke.size()<50; i++)
                  {
                     int radius = Utilities.rollDie(5,15);
                     int opacity = Utilities.rollDie(85,137);
                     CultimaPanel.smoke.add(new SmokePuff(plX+Utilities.rollDie(-6,6), plY+Utilities.rollDie(-6,6), radius, opacity, dotColor));
                  }
                  int respawnRow = target.getHomeRow();
                  int respawnCol = target.getHomeCol();
                  target.restoreLoc();
                  target.setBody(10);
                  plX = respawnRow;
                  plY = respawnCol;  
                  for(int i=0; i<5 && CultimaPanel.smoke.size()<50; i++)
                  {
                     int radius = Utilities.rollDie(5,15);
                     int opacity = Utilities.rollDie(85,137);
                     CultimaPanel.smoke.add(new SmokePuff(plX+Utilities.rollDie(-6,6), plY+Utilities.rollDie(-6,6), radius, opacity, dotColor.brighter()));
                  }
               }
               else
               {
                  Corpse dead = target.getCorpse();
                  dead.setHitTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
                  CultimaPanel.corpses.get(this.getMapIndex()).add(dead);
                  Utilities.removeNPCat(target.getRow(), target.getCol(), target.getMapIndex());
               }
            }
            if((TerrainBuilder.habitablePlace(this.getLocationType()) || getLocationType().contains("arena") || getLocationType().contains("temple")) && numHumanWitnesses() >= 1 && !isZombie)
            {
               if(NPC.isCivilian(target.getCharIndex()) && numHumanWitnesses() > 1) //don't include target with numberHumanWitnesses
               {
                  if(isStatue && this.getImageIndex()==HAMMER)                      //this is a statue
                  {
                     this.addReputation(-1);
                     if(getLocationType().contains("temple"))
                        CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.vandalTemple));
                     else
                        CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.vandalGeneral));
                     Utilities.alertGuards();
                  }
                  else 
                  {
                     if(!target.getName().contains("Masterthief"))   //the Masterthief bounty mission is given by the guard and should not trigger them
                     {  
                        this.addReputation(-10);
                        CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.murderGeneral));
                        Utilities.alertGuards();
                     }
                  }
               }
               else if(target.getCharIndex() == NPC.CHICKEN || target.getCharIndex() == NPC.PIG)
               {
                  this.addReputation(-1);
                  CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.thiefGeneral));
                  Utilities.alertGuards();
               }
               else if(target.getCharIndex() == NPC.BARREL  && numHumanWitnesses() > 1)
               {
                  this.addReputation(-1);
                  CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.vandalGeneral));
                  Utilities.alertGuards();
               }
               else if(!NPC.isCivilian(target.getCharIndex()) && target.getCharIndex() != NPC.BARREL  && numHumanWitnesses() > 1)
               {
                  if(TerrainBuilder.habitablePlace(this.getLocationType()))
                  {
                     this.addReputation(5);
                  }
                  else
                  {
                     this.addReputation(2);
                  }
                  if(TerrainBuilder.habitablePlace(this.getLocationType()) || Math.random() < 0.25)
                  {
                     String cheer = (Utilities.getRandomFrom(NPC.cheer)).replace("ADJECTIVE",NPC.getRandomFrom(NPC.complimentAdjective));
                     CultimaPanel.sendMessage("~"+cheer.replace("PLAYER_NAME", this.getShortName()));
                  }
               }
            }
         }  
         if(target.getBody() > 0 && CultimaPanel.NPCinSight.size()>1 && Utilities.rollDie(200)-CultimaPanel.distToClosestAwareNPC > this.getAgility() 
         && (TerrainBuilder.habitablePlace(this.getLocationType()) || getLocationType().contains("arena") || getLocationType().contains("temple")) && NPC.isCivilian(target.getCharIndex()) && !isZombie && !isStatue
         && !weapon.getEffect().contains("control") && !weapon.getEffect().contains("curse") && !weapon.getName().toLowerCase().contains("blind"))
         {  //Civilian calls for help
            
            //see if this is a mercy mission
            boolean mercyMission = false;
            String NPCmiss = target.getMissionInfo();
            if(!NPCmiss.equals("none"))
            {
               for(int i=0; i<CultimaPanel.missionStack.size(); i++)
               {
                  Mission m = CultimaPanel.missionStack.get(i);
                  if(NPCmiss.equals(m.getInfo()))
                  {
                     if(m.getType().equals("Mercy"))
                     {
                        mercyMission = true;
                        break;
                     }
                  }
               }
            }
            if(target.getName().toLowerCase().startsWith("cultist"))  //from the Ceremony mission
            {              //slap a cultist to break them out of their trance
               NPC.deprogramCultist(target);
            }
            else
            {
               if(!mercyMission) 
               {
                  this.addReputation(-1);
                  if(getLocationType().contains("temple"))
                     CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.assaultTemple));
                  else
                     CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.assaultCity));
               }
               target.setNumInfo(0);
               if(!target.getName().contains("Masterthief"))   //the Masterthief bounty mission is given by the guard and should not trigger them
               {  
                  for(NPCPlayer g:CultimaPanel.NPCinSight)
                  {
                     if(NPC.isGuard(g.getCharIndex()))
                     {
                        Utilities.alertGuards();
                     }
                  }
               }
            }
         } 
      }
      else     //you miss
      {
         String missMessage = "You Missed!";
         target.setMissTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
         boolean ourWeaponIsMetal = this.getWeapon().isMetal();
         boolean weHaveWeapon = (imageIndex!=NONE && imageIndex!=THROWN);
         boolean enemyHasWeapon = NPC.hasWeapon(target.getCharIndex());
         boolean enemyWeaponHasMetal = NPC.hasMetalWeapon(target.getCharIndex());
         boolean metalClank = ((ourWeaponIsMetal && enemyHasWeapon) || (weHaveWeapon && enemyWeaponHasMetal));
         boolean isCannon = (this.getWeapon().getName().toLowerCase().contains("cannon"));
         boolean[] combustables = Utilities.nextToACombustable(target);
         boolean nextToACombustable = (combustables[LocationBuilder.NORTH] || combustables[LocationBuilder.EAST] || combustables[LocationBuilder.SOUTH] || combustables[LocationBuilder.WEST]);
         if(isThrownSpear || isThrownDagger)
         {  //leave a spear or dagger on the floor (corpse)
            Corpse alreadyThere = Utilities.getCorpseAt(target.getRow(), target.getCol(), target.getMapIndex());
            if(alreadyThere != null && alreadyThere.getCharIndex()==-1 && alreadyThere.hasItem(weapon))
            {
               alreadyThere.forceAddItem(weapon);
            }
            else
            {
               ImageIcon pict = CultimaPanel.spear;
               if(isThrownDagger)
               {
                  pict = CultimaPanel.dagger;
               }
               ArrayList<Item> items = new ArrayList<Item>();
               items.add(weapon);
               Corpse dropped = new Corpse(pict, (byte)(-1), target.getRow(), target.getCol(), target.getMapIndex(), 0, null, null, items, CultimaPanel.days + 10, 1);
               CultimaPanel.corpses.get(this.getMapIndex()).add(dropped);
            }
         }
         if(isCannon)
         {
            Sound.thud();
            CultimaPanel.sendMessage(missMessage);
         }
         else
         {
            if(mightyBlow)
            {  //we attempted a mighty blow and missed - target gets a chance to get an extra attack on us depending on their agility
               int hitDie = Utilities.rollDie(target.getLevel()+this.getLevel());
               if((int)(target.getLevel()*2) >= hitDie)
               {
                  missMessage = "You missed and are parried!";
                  if(metalClank)
                     Sound.parried();
                  else
                     Sound.thud();
                  target.attack();
               }
            }
            if(NPC.getSize(target) >= 1.5)
            {
               if(weapName.contains("bow") || weapName.contains("caster"))
               {
                  if(Utilities.rollDie(10,MAX_STAT) < this.getAgility())
                  {
                     int missType =Utilities.rollDie(1,3);
                     if(missType==1)
                        CultimaPanel.sendMessage("Arrow bounces off!");
                     else if(missType==2)
                        CultimaPanel.sendMessage("Arrow batted away!");
                     else
                        CultimaPanel.sendMessage("Arrow breaks on impact!");
                  }
                  else
                     CultimaPanel.sendMessage(missMessage);
               }
               if(metalClank)
                  Sound.parried();
               else
                  Sound.thud();
            }
            else
            {
               if(Math.random() < 0.5)
               {
                  CultimaPanel.sendMessage("Blocked!");
                  if(metalClank)
                     Sound.parried();
                  else
                     Sound.thud();
               }
               else
               {
                  CultimaPanel.sendMessage("You Missed!");
                  Sound.miss();        //sound
               }
            }
         }
         //if we missed with a fire weapon, maybe start a fire next to target
         if(weapon.getEffect().contains("fire"))
         {  //start fire in adjacent spot where we missed our attack
            double fireProb = 0.5;
            if(weapon.getRange() > 2 || nextToACombustable)
               fireProb = 0.75;    
            if(Math.random() < fireProb)
            {
               ArrayList<Coord>adjacentSpots = new ArrayList<Coord>();
               for(int r=target.getRow()-1; r<=target.getRow()+1; r++)
               {
                  for(int c=target.getCol()-1; c<=target.getCol()+1; c++)
                  {
                     if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
                        continue;
                     String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                     if(!current.contains("water") && !current.contains("bog"))
                        adjacentSpots.add(new Coord(r, c));
                  }
               }
               if(adjacentSpots.size() > 0)
               {
                  Coord fireSpot = Utilities.getRandomFrom(adjacentSpots);
                  int r = fireSpot.getRow();
                  int c = fireSpot.getCol();
                  int mi = this.getMapIndex();
                  NPCPlayer fire = new NPCPlayer(NPC.FIRE, r, c, mi, locType);
                  fire.setBody(Utilities.rollDie(5,25));
                  CultimaPanel.worldMonsters.add(fire);
               }
            }
         }
      }
      return null;
   }

   public static int numHumanWitnesses()
   {
      int count = 0;
      for(NPCPlayer p:CultimaPanel.NPCinSight)
         if(NPC.isCivilian(p.getCharIndex()))
            count++;
      return count;
   }

   public String getDogName()
   {
      if(dogBasicInfo.equals("none"))
         return "none";
      int pos = dogBasicInfo.indexOf(",");
      return dogBasicInfo.substring(0,pos);
   }
     
   public void setDogBasicInfo(String dn)
   {
      dogBasicInfo = dn;
   }

   public String getDogBasicInfo()
   {
      return dogBasicInfo;
   }

   public String getNpcName()
   {
      if(npcBasicInfo.equals("none"))
         return "none";
      int pos = npcBasicInfo.indexOf(",");
      return npcBasicInfo.substring(0,pos);
   }

   public void setNpcBasicInfo(String nn)
   {
      npcBasicInfo = nn;
   }

   public String getNpcBasicInfo()
   {
      return npcBasicInfo;
   }

   public void setHorseBasicInfo(String dn)
   {
      horseBasicInfo = dn;
   }

   public String getHorseBasicInfo()
   {
      return horseBasicInfo;
   }
   
   //clears any data that only needs to be remembered in a specific location (playing swine, solving a riddle
   public void clearLocationData()
   {
      CultimaPanel.swineNpcSum=0;
      CultimaPanel.swinePlayerSum=0;
      CultimaPanel.swineNpcTempSum=0;
      CultimaPanel.swinePlayerTempSum=0;
      CultimaPanel.swineTurn=0;
      CultimaPanel.swineNpcNumRolls=0;
      this.setDoorNumber((byte)(-1));
      this.setDoorChange((byte)(-1));
      this.setWellNumber((byte)(-1));
      if(CultimaPanel.smoke != null)
         CultimaPanel.smoke.clear();
      if(magicMistActive())
      {
         CultimaPanel.fogAlpha = 0;
         CultimaPanel.fogFrame = 0;
         clearActiveSpells();
      }   
   }
   
   public void setPictures(ArrayList<String> x)
   {
      ArrayList<String>[] images = new ArrayList[5];
      for(int dir=0; dir<images.length; dir++)
         images[dir]=new ArrayList();
      for(String s:x)
         images[0].add(s);
      super.setPictures(images);
   }

   public String toString()
   {
      String ans = "";
      ans += (getName()+"   **name\n");                        //player.txt line 2
      
      String values = "";
      for(int index=0; index<mapMark.length; index++)                   //line 3
         values += mapMark[index] + " ";
      ans += values + "    **mapMark\n";
      
      values = "";                                                      //line 4
      for(int index=0; index<potions.length; index++)
         values += potions[index] + " ";
      ans += values + "    **potions\n";
   
      values = "";                                                      //line 5
      for(int index=0; index<flowerBoxCount.length; index++)
         values += flowerBoxCount[index] + " ";
      ans += values + "    **flowerBoxCount\n";
   
      ans += (getSpecificExpString()+ "     **specificExperience\n");   //line 6
      ans += (getStatsString()+ "     **stats\n");                      //line 7
      ans += (getInfoIndexesString()+ "     **infoIndexes\n");          //line 8
   
      values = "";                                                      //line 9
      for(int index=0; index<spellHotKeys.length; index++)
         values += spellHotKeys[index] + " ";
      ans += values + "    **spellHotKeys\n";
      
      values = "";                                                      //line 10
      for(int index=0; index<potionHotKeys.length; index++)
         values += potionHotKeys[index] + " ";
      ans += values + "    **potionHotKeys\n";
   
      values = "";                                                      //line 11
      for(int index=0; index<weaponHotKeys.length; index++)
         values += weaponHotKeys[index].getRow() + " " + weaponHotKeys[index].getCol() + " ";
      ans += values + "    **weaponHotKeys\n";
   
      ans += (getEffects()+ "     **effects\n");                        //line 12
      ans += (getFileNames()+"   **images\n");                          //line 13
      ans += (getItems()+ "     **item:itemFrequency\n");               //line 14
      ans += (getSpellNames()+ "     **spells\n");                      //line 15
      ans += (getMemory()+"   **teleporter-memory\n");                  //line 16
      ans += (getAllArmorNames()+"  **armor:armorFrequency\n");         //line 17
      
      for(int index=0; index<weapons.length; index++)                   //lines 18-32
      {
         ArrayList<Weapon> weap = weapons[index];
         ans += (getAllWeaponNames(weap, index)+"   **weapon:weaponFrequency for "+Weapon.getWeaponCategory((byte)(index)) +"\n");
      }
   
      ans += (getImageIndex()+"     **imageIndex\n");                   //line 33
      ans += (getStartStoryIndex()+"     **startStoryIndex\n");         //line 34
      ans += (getMapIndex()+"   **mapIndex\n");                         //line 35
      ans += (getRow()+"   **row\n");                                   //line 36
      ans += (getCol()+"   **column\n");                                //line 37
      ans += (getWorldRow()+"   **world-row\n");                        //line 38
      ans += (getWorldCol()+"   **world-column\n");                     //line 39
      ans += (getLocationType()+ "     **locationType\n");              //line 40
      ans += (might+ "     **might\n");                                 //line 41
      ans += (mind+"     **mind\n");                                    //line 42
      ans += (agility+"     **agility\n");                              //line 43
      ans += (body+"     **body\n");                                    //line 44
      ans += (getManna()+"     **manna\n");                             //line 45
      ans += (awareness+"    **awareness\n");                           //line 46
      ans += (reputation+"   **reputation\n");                          //line 47
      ans += (getBounty()+"   **bounty\n");                             //line 48
      ans += (getGold()+"   **gold\n");                                 //line 49
      ans += (getExperience()+"   **experience\n");                     //line 50
      ans += (getExpToLevelUp()+"     **experienceToLevelUp\n");        //line 51
      ans += (weaponSelect+"     **weaponSelect\n");                    //line 52
      ans += (armorSelect+"     **armorSelect\n");                      //line 53
      ans += (getRations()+"   **rations\n");                           //line 54
      ans += (getNumArrows()+ "     **numArrows\n");                    //line 55
      ans += (getNumStones()+ "     **numStones\n");                    //line 56
      ans += (getDogBasicInfo()+ "     **dogBasicInfo\n");              //line 58
      ans += (getHorseBasicInfo()+ "     **horseBasicInfo\n");          //line 59
      ans += (getNpcBasicInfo()+ "     **npcBasicInfo");                //line 60     
      return ans;
   }
   
   //test the 3-D array format potential for player images
   public static void main(String [] arg)
   {
      String [][][] test =   {{{"0_left_a","0_left_b","0_left_c","0_left_d"},{"0_right_a","0_right_b","0_right_c","0_right_d"}},
                               {{"1_left_a","1_left_b","1_left_c","1_left_d"},{"1_right_a","1_right_b","1_right_c","1_right_d"}},
                               {{"2_left_a","2_left_b","2_left_c","2_left_d"},{"2_right_a","2_right_b","2_right_c","2_right_d"}}};
      int weapNum = 0;    //weapon number
      int direction = 0;  //0-left, 1-right
      int frame = 0;      //animation frame                  
      System.out.println(test[weapNum][direction].length);
   }
}