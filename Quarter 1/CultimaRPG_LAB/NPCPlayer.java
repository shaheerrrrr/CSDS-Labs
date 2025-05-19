import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.ImageIcon;

public class NPCPlayer extends AnimatedEntity implements Comparable
{
   //****TESTING*** //private static final double missionProb = 3;     //to make it so most NPCs have missions to give so we can test specific mission types
   private static final double missionProb = 0.15;   //15% chance of an npc with a mission
   private static final double werewolfProb = 0.0085; //0.85% chance of an npc with the werewolf curse

   private int row, col, mapIndex;                    //location for navigation
   private int npcX, npcY;                            //recorded from Display when they are drawn, used for visual effects like adding smoke when defeated
   private String location;                           //what kind of location are we in?
   private String homeTown;                           //what is the name of their hometown?
   private int homeRow, homeCol;                      //location of the home they stay in at night
   private char maritalStatus;                        //'M' for married, 'S' for single, 'W' for widowed, 'N' for not-interested
   private String missionInfo;                        //what mission in the missionStack is attached to this character?
   private byte charIndex;                            //what character type are you (see NPC.java)
   private ImageIcon dead;                            //graphics for dead player
   private double hitTime, missTime;                  //used to show hit graphic when the NPC gets damaged
   private double attackHitTime, attackMissTime;      //used to show hit graphic when the NPC attacks the player
   private Coord targetLoc;                           //stores the location that the npc is attacking
   private boolean hasAttacked;                       //true if an NPC with "control" effect has attacked another NPC - used to make townsfolk run
                                                      //also used to see if an NPC has been hit by chain lightning so we don't hit it again
   private boolean flyer;                             //if the NPC can fly
   private boolean swimmer;                           //if the NPC can swim
   private boolean walkAndSwim;                       //is the NPC a ground walker that can swim
   private boolean fireResistant;                     //if the NPC can walk through fire
   private boolean formerCultist;                     //used to make sure reformed cultists run, but don't alert the guards
   private ArrayList<String> effects;                 //poison, cursed, blessed, etc

   private int might;                                 //melee attack and defense of melee attacks
   private int mind;                                  //spell casting and resistance to illusions
   private int agility;                               //ranged attack, defense of ranged attacks
   private int[] statMod;                             //+ or - modifiers for the 3 stats (curse spell, magic items, etc)

   private int body;                                  //# health points

   private int reputation;                            //revealed if Player has higher awareness

   private int gold;                                  //coins in pocket
   private int numArrows;                             //the # arrows lodged in them, to be given to their corpse if killed (not remembered in file)

   private Weapon weapon;                             //weapon wielded
   private Armor armor;                               //armor worn
   private ArrayList<Item> items;                     //items carried (rations, mission items, etc)
   private int sellPrice;                             //value the NPC might be willing to sell their item for

   private byte moveType;                             //how the NPC moves - NPC.STILL, NPC.ROAM, NPC.MARCH, NPC.CHASE, NPC.RUN
   private byte origMoveType;                         //the original moveType of the player that gets restored at the end of the day

   private boolean talking;                           //NPC won't move when you are talking to them
   private boolean hasMet;                            //has Player met the NPC yet?  If it is a ship, it is whether it has been boarded yet.
   private int restoreDay;                            //if the NPC dies, they will repopulate with the same type after the restoreDay
   private int endChaseDay;                           //the day that a chasing NPC will forget about you - used for guards that catch you doing something bad
                                                      //also used to set the day your taxes are due with the TAXMAN
   private int numInfo;                               //the number of pieces of information an NPC will sharewith you; also used to designate special properties (have a mission, zombified, etc)
   private boolean isSwinePlayer;                     //is this player's mission to play Swine for gold?

   private boolean hasBeenAttacked;                   //used to have NPC respond to being attacked
   private double attackTime;                         //to use for timing of attacks against the player
   private boolean hasTrained;                        //true if this NPC has trained player - used to curse player if they slay them
   private boolean hasBeenRescued;                    //did the player rescue this NPC from a dungeon?  Used to remove reputation points if they kill the person they rescue

   public static String[] reputationNames = {"Vile", "Wicked", "Kanvish", "Proper", "Honorable", "Noble", "Divine"};
   public static String[] kingPrefix = {"King", "Emperor", "Sultan", "Baron", "Prince", "Queen", "Baroness", "Duke", "Count", "Empress", "Lady", "Lord"};

//if index == -1, pick a random NPC civilian.
//ARGS:  NPCPlayer(characterIndex, row, col, mapIndex, homeRow, homeCol, locationType)
   public NPCPlayer(byte index, int r, int c, int mi, int hr, int hc, String loc)
   {
      super(Utilities.nameGenerator("name"));
      effects = new ArrayList<String>();
      targetLoc = new Coord();
      numArrows = 0;
      hasBeenAttacked = false;
      hasTrained = false;
      hasBeenRescued = false;
      attackTime = 0;
      statMod = new int[3];
      numInfo = 0;
      isSwinePlayer = false;
      hitTime = -1;
      missTime = -1;
      attackHitTime = -1;
      attackMissTime = -1;
      hasAttacked = false;
      row = r;
      col = c;
      npcX = -1;
      npcY = -1;
      mapIndex = mi; 
      homeRow = hr;
      homeCol = hc;                      
      setLocationType(loc);
      homeTown = "";
      missionInfo = "none";
      talking = false;
      hasMet = false;
      restoreDay = -1;
      endChaseDay = -1;
      moveType = NPC.STILL;
      origMoveType = NPC.STILL;
      if(!this.isShopkeep() &&  (!TerrainBuilder.habitablePlace(loc) ||     /*prisoners and travelers will be from another town*/
      Math.random() < 0.1) && NPC.isCivilian(index) && index!=NPC.CHILD && index!=NPC.KING && index!=NPC.JESTER)    
      {  //make the person from a town different from the one they are found in
         if(CultimaPanel.allDomiciles != null)
         {
            ArrayList<Location> undiscovered = new ArrayList<Location>();
            for(Location lo: CultimaPanel.allDomiciles)
               if(lo.getMapIndex()==-1)    //find an undiscovered town
                  undiscovered.add(lo);
            if(undiscovered.size() > 0)
            {      
               Location home = Utilities.getRandomFrom(undiscovered);
               homeTown = home.getName();
               if(Math.random() < 0.25)   //25% of the time, the town can not be found
                  homeTown =Utilities.nameGenerator("town");
            
               String [] parts = homeTown.split(" ");
               if(parts.length == 2)      //if the city name has a common prefix or suffix, strip it down to just the province name
               {
                  String prefix = parts[0];
                  String suffix = parts[1];
                  for(String pre: Utilities.cityPrefix)
                  {
                     if(pre.equals(prefix))
                        homeTown = suffix;
                  }
                  for(String suf: Utilities.citySuffix)
                  {
                     if(suf.equals(suffix))
                        homeTown = prefix;
                  }
               }
               super.setName(super.getName() + " of " + homeTown);
            }
         }
      }
      else if(index==NPC.ROYALGUARD)
         super.setName("Sir " +super.getName());
      else if(index==NPC.KING)
         super.setName(Utilities.getRandomFrom(kingPrefix) + " " +super.getName());
      else if (this.getName().contains("Skara Brae"))
         super.setName("Skara Brae");
   //people in more populated areas are more likely to have more gold
      int goldMin = 1, goldMax = 3;
      if(loc.contains("castle"))
         goldMax = 5;
      else if(loc.contains("fortress"))
         goldMax = 8;
      else if(loc.contains("city"))
         goldMax = 10;
   
      gold = Utilities.rollDie(goldMin, goldMax);
      if(index == -111)    //-111 is the flag that this monster is a mimic of the player
      { }
      else if(index < 0)
         index = NPC.randCivilian();
   
            
      might = Utilities.rollDie(3,Player.MAX_STAT);
      mind = Utilities.rollDie(3,Player.MAX_STAT);
      agility = Utilities.rollDie(3,Player.MAX_STAT);
   
      flyer = false;
      swimmer = false;
      walkAndSwim = false;
      fireResistant = false;
      formerCultist = false;
      if(Math.random() < 0.2)         //20% of the time, make the potential to have any kind of reputation
         reputation = (int)(Math.random() * 2201) - 1100;
      else                             //80% of the time, reputations are close to the middle
         reputation = (int)(Math.random()*150) - 30;
   
      weapon = new Weapon("none", "none", 1, 1, 1, null, 0, Player.NONE, 0, 0);   
      armor=(new Armor("none", 0, null, 0));
      items = new ArrayList<Item>();
      sellPrice = -1;
   
      maritalStatus = 'N';
      
      setCharIndex(index); 
      setAttributes(charIndex);
   
      body = maxHealth();
      
      if(NPC.isTameableCanine(this.getCharIndex()))
         makeSimpleName();
   }

//if index == -1, pick a random NPC civilian.
//ARGS:  NPCPlayer(characterIndex, row, col, mapIndex, homeRow, homeCol, locationType)
   public NPCPlayer(String n, byte index, int r, int c, int mi, int hr, int hc, String loc)
   {
      this(index, r, c, mi, hr, hc, loc);
      setName(n);
      if(NPC.isTameableCanine(this.getCharIndex()))
      {
         makeSimpleName();
      }
   }

//create a monster
   public NPCPlayer(byte index, int r, int c, int mi, String loc)
   {
      this(index, r, c, mi, r, c, loc);
      if(NPC.isTameableCanine(this.getCharIndex()))
      {
         makeSimpleName();
      }
   }

//basicInfo is in the form: getName()+","+getMight()+","+getMind()+","+getAgility()+","+getBody()+","+getCharIndex()
   public NPCPlayer(byte index, int r, int c, int mi, String loc, String basicInfo, boolean isEscortedNpc)
   {
      this(index,  r,  c,  mi,  loc);
      String[]parts = basicInfo.split(",");
      setName(parts[0].trim());
      setMight(Integer.parseInt(parts[1].trim()));
      setMind(Integer.parseInt(parts[2].trim()));
      setAgility(Integer.parseInt(parts[3].trim()));
      setBody(Integer.parseInt(parts[4].trim()));
      if(parts.length >= 6)
         setCharIndex(Byte.parseByte(parts[5].trim()));
      else
         setCharIndex(index);
      if(isEscortedNpc)
      {                 //make sure the npc we are escorting doesn't pick up a mission when going into a location
         numInfo = 0;
         reputation = CultimaPanel.player.getReputation();
      }
      else if(NPC.isTameableCanine(this.getCharIndex()))
         makeSimpleName();
   }
   
   //basicInfo is in the form: getName()+","+getMight()+","+getMind()+","+getAgility()+","+getBody()+","+getCharIndex()
   public NPCPlayer(int r, int c, int mi, String loc, String basicInfo, boolean isEscortedNpc)
   {
      this((byte)0,  r,  c,  mi,  loc);
    
      String[]parts = basicInfo.split(",");
      this.setName(parts[0].trim());
      might = (Integer.parseInt(parts[1].trim()));
      mind = (Integer.parseInt(parts[2].trim()));
      agility = (Integer.parseInt(parts[3].trim()));
      body = (Integer.parseInt(parts[4].trim()));
      charIndex = (Byte.parseByte(parts[5].trim()));
      setCharIndex(charIndex);
      setAttributes(charIndex);
   
      if(isEscortedNpc)
      {                 //make sure the npc we are escorting doesn't pick up a mission when going into a location
         numInfo = 0;
         reputation = CultimaPanel.player.getReputation();
      }
      else if(NPC.isTameableCanine(this.getCharIndex()))
         makeSimpleName();
   }

   public void setXY(int x, int y)
   {
      npcX = x;
      npcY = y;
   }
   
   public int getNpcX()
   {
      return npcX;
   }
 
   public int getNpcY()
   {
      return npcY;
   }
 
   public String basicInfo()
   {
      return getName()+","+getMight()+","+getMind()+","+getAgilityRaw()+","+getBody()+","+getCharIndex();
   }
   
   public String getNameSimple()
   {
      return makeSimpleName(this.getName());
   }
   
   public boolean formerCultist()
   {
      return formerCultist;
   }
   
   public void setFormerCultist(boolean state)
   {
      formerCultist = state;
   }
   
   public static String makeSimpleName(String name)
   {
      int index = name.indexOf(" of ");
      if(index >= 0)
      {
         String tempName = name.substring(0, index).trim();
         return tempName;
      }
      return name;
   }
   
   public void removeOfCity()
   {
      if(this.isStaticShopkeep() && this.getName().contains(" of "))
      {
         String nameTemp = this.getName();
         int index = nameTemp.indexOf(" of ");
         if(index >= 0)
         {
            this.setName(nameTemp.substring(0, index).trim());
         }
      }
   }
   
   public void makeSimpleName()
   {
      String nameTemp = this.getName();
      int spaceIndex = nameTemp.indexOf(" ");
      if(spaceIndex > 0)
         this.setName(nameTemp.substring(0, spaceIndex));
   }

   //returns the percent health from the absolute max health down to zero
   public double getPercentHealth()
   {
      int maxHealth = NPC.stats[this.getCharIndex()][NPC.MAX_MIGHT]*5;
      return ((this.getBody()) * 100.0) / (maxHealth);
   }

   public void setAttributes(byte index)
   {
      int minMight = NPC.stats[index][NPC.MIN_MIGHT];
      int maxMight = NPC.stats[index][NPC.MAX_MIGHT];
      int minMind = NPC.stats[index][NPC.MIN_MIND];
      int maxMind = NPC.stats[index][NPC.MAX_MIND];
      int minAgil = NPC.stats[index][NPC.MIN_AGIL];
      int maxAgil = NPC.stats[index][NPC.MAX_AGIL];
      int demeanor = NPC.stats[index][NPC.DEMEANOR];
      might = Utilities.rollDie(minMight,maxMight);
      mind = Utilities.rollDie(minMind,maxMind);
      agility = Utilities.rollDie(minAgil,maxAgil);
      switch (index)
      {
         case NPC.BEGGER:     
            armor=Armor.getClothes();
            weapon=Weapon.getFists();   
            numInfo = (int)(Math.random()*3) + 1;  //the number of experience points you can get by donating coins to the begger
            gold = 0;
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb)            //might have mission for us
               numInfo = 10;
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            int mstat = Utilities.rollDie(1,10);
            if(mstat<=6)
               maritalStatus = 'W';
            else if(mstat==8)
               maritalStatus = 'N';
            else
               maritalStatus = 'S';
            setMoveType(NPC.STILL);
            break; 
      
         case NPC.CHILD:      
            armor=Armor.getClothes();
            weapon=Weapon.getFists();  
            gold = Utilities.rollDie(0,1);
            if(Math.random() < 0.1)        //chance will have rations
            {
               items.add(new Item("ration", 1));
            }
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb)            //might have mission for us
               numInfo = 10;
            break;
      
         case NPC.GUARD_SPEAR:      
            if(location.contains("domicile") || location.contains("village"))
               armor=Armor.getLeather();
            else if(location.contains("fortress") || location.contains("city"))
               armor=Armor.getChainmail();
            else if(location.contains("tower") || location.contains("castle"))
               armor=Armor.getScalemail();   
            weapon = Weapon.getSpear(); 
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            }
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb/2)            //guard might ask us to join a brute squad for battle    
               numInfo = 10;
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            pickMaritalStatus();  
            this.setDirection(AnimatedEntity.DOWN);  
            break;
      
         case NPC.GUARD_SWORD:     
            if(location.contains("domicile") || location.contains("village"))
               armor=Armor.getLeather();
            else if(location.contains("fortress") || location.contains("city"))
               armor=Armor.getChainmail();
            else if(location.contains("tower") || location.contains("castle"))
               armor=Armor.getScalemail();   
            weapon = Weapon.getSword();   
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            }
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb/2)            //guard might ask us to join a brute squad for battle    
               numInfo = 10;
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            pickMaritalStatus();
            this.setDirection(AnimatedEntity.DOWN);  
            break;
            
         case NPC.GUARD_FIST:     
            if(location.contains("domicile") || location.contains("village"))
               armor=Armor.getLeather();
            else if(location.contains("fortress") || location.contains("city"))
               armor=Armor.getChainmail();
            else if(location.contains("tower") || location.contains("castle"))
               armor=Armor.getScalemail();   
            weapon = Weapon.getFists();  
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            } 
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb/2)            //guard might ask us to join a brute squad for battle    
               numInfo = 10;
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            pickMaritalStatus();
            this.setDirection(AnimatedEntity.DOWN);  
            break;
      
         case NPC.JESTER:       
            armor=Armor.getClothes();
            weapon=Weapon.getFists();   
            if(Math.random() < 0.75)
               items.add(Item.getLockpick());
            else if(Math.random() < 0.05)
               items.add(Item.getMagicPick());
            else if(Math.random() < 0.05 && this.getReputation() < -100)
               items.add(Item.getViperGlove());
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,2)));
            }
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb*5)            //might have riddle for us
               numInfo = 10;
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            pickMaritalStatus();
            if(location.contains("tower") || location.contains("castle"))  //castle jesters are more agile
               agility = (int)(Math.min(Player.MAX_STAT, agility * 1.25));
            break;
      
         case NPC.LUTE:       
            armor=Armor.getClothes();
            weapon=Weapon.getLute();  
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,2)));
            } 
            numInfo = 1;
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb/2)            //might have mission for us
               numInfo = 10;
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            setMoveType(NPC.STILL);
            pickMaritalStatus();
            break;
      
         case NPC.MAN:        
            armor=Armor.getClothes();
            weapon=Weapon.getFists();   
            numInfo = 1;
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,4)));
            }
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < missionProb)
               {
                  numInfo = 10;
                  gold += 25;
               }
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            pickMaritalStatus();
            break;
      
         case NPC.TRADER:        
            armor=Armor.getClothes();
            weapon=Weapon.getFists();
            gold *= 2;   
            numInfo = 1;
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,8)));
            }
            if(Math.random() < werewolfProb)
            {
               setName(this.getName() + "~" + index);
            }
            maritalStatus = 'N';
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            break;
      
         case NPC.TAXMAN:        
            armor=Armor.getClothes();
            weapon = Weapon.getStaff();   
            numInfo = 1;
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,2)));
            }
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < missionProb)
               {
                  numInfo = 10;
                  gold += 25;
               }
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            gold *= 2;
            pickMaritalStatus();
            this.setDirection(AnimatedEntity.DOWN);  
            break;
      
         case NPC.SWORD:         
            numInfo = (int)(Math.random()*3) + 1;     //the number of caves an adventurer can tell you about
            int armorType = (int)(Math.random()*4);
            if(armorType==0)
               armor=Armor.getLeather();
            else if(armorType==1)
               armor=Armor.getChainmail();               
            else if(armorType==2)
               armor=Armor.getScalemail();   
            else
               armor=Armor.getPlate();   
            weapon = Weapon.getSword();  
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,8)));
            } 
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb)            //might have mission for us
               numInfo = 10;
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            pickMaritalStatus();
            break;
      
         case NPC.WISE:            
            numInfo = 1;
            armor=Armor.getClothes();
            weapon = Weapon.getStaff();  
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,2)));
            } 
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb)            //might have mission for us
               numInfo = 10; 
            mstat = Utilities.rollDie(1,10);
            if(mstat<=5)
               maritalStatus = 'W';
            else if(mstat<=8)
               maritalStatus = 'M';
            else if(mstat==9)
               maritalStatus = 'N';
            else
               maritalStatus = 'S';
            break;
      
         case NPC.WOMAN:         
            armor=Armor.getClothes();
            weapon=Weapon.getFists();   
            numInfo = 1;
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,2)));
            }
            if(this.location.contains("city") || this.location.contains("fort") || this.location.contains("village"))
            {
               if(Math.random() < missionProb)
               {
                  numInfo = 10;
                  gold += 25;
               }
               if(Math.random() < werewolfProb)
               {
                  setName(this.getName() + "~" + index);
                  if(Math.random() < missionProb*2)
                     numInfo = 10;
               }
            }
            pickMaritalStatus();
            break;
      
         case NPC.ROYALGUARD:    
            armorType = (int)(Math.random()*3);
            if(armorType == 0)
               armor = Armor.getPlate();
            else if(armorType == 1)
               armor=Armor.getExoticPlate();   
            else
               armor=Armor.getBlessedArmor();
            weapon = Weapon.getSwordShield(); 
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,4)));
            }  
            gold *= (int)(Math.random()*30)+20;
            if(TerrainBuilder.habitablePlace(location) && Math.random() < missionProb*3)            //might have mission for us
               numInfo = 10;
            mstat = Utilities.rollDie(1,3);
            if(mstat==0)
               maritalStatus = 'M';
            else if(mstat==1)
               maritalStatus = 'W';
            else //if(mstat==2)
               maritalStatus = 'N';
            break;
      
         case NPC.KING:          
            numInfo = (int)(Math.random()*3) + 1;     //the number of missions a king can send you on
            gold *= Utilities.rollDie(50,100);
            armor=Armor.getExoticPlate();   
            weapon=Weapon.getSceptre();  
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,2)));
            } 
            if(Math.random() < 0.5)
            {
               int rand = (int)(Math.random() * 9);
               if(rand == 0)
                  items.add(Item.getCharmring());
               else if(rand == 1)
                  items.add(Item.getFocushelm());
               else if(rand == 2)
                  items.add(Item.getPentangle());
               else if(rand == 3)
                  items.add(Item.getMannastone());
               else if(rand == 4)
                  items.add(Item.getMindTome());
               else if(rand == 5)
                  items.add(Item.getSwiftBoots());
               else if(rand == 6)
                  items.add(Item.getPowerBands());
               else if(rand == 7)
                  items.add(Item.getLifePearl());
               else //if(rand == 8)
                  items.add(Item.getTalisman());
            }
            else
               items.add(Item.getRandomStone());
            mstat = Utilities.rollDie(1,3);
            if(mstat==0)
               maritalStatus = 'M';
            else if(mstat==1)
               maritalStatus = 'W';
            else //if(mstat==2)
               maritalStatus = 'N';
            break;
            
         case NPC.DARKKNIGHT4:    
         case NPC.DARKKNIGHT3:    
         case NPC.DARKKNIGHT2:    
         case NPC.DARKKNIGHT1:    
         case NPC.DARKKNIGHT0:    
            setName("Black Knight");            //"The Black Knight always triumphs!"
            armorType = (int)(Math.random()*3);
            if(armorType == 0)
               armor = Armor.getPlate();
            else if(armorType == 1)
               armor=Armor.getExoticPlate();   
            else
               armor=Armor.getBlessedArmor();
            weapon = Weapon.getRandomSword();
            reputation = demeanor * Utilities.rollDie(500,1000);
            gold *= (int)(Math.random()*20)+15;
            numInfo = 4;
            maritalStatus = 'N';
            break;
      
         case NPC.BUGBEAR:             
            setName("Bugbear"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getFangedBite();   
            break;
      
         case NPC.CYCLOPS:             
            setName("Cyclops"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getGiantMace();   
            break;
      
         case NPC.DEMON:    
            String nameTemp = super.getName();
            int pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            if(Math.random() < 0.25)
               setName(nameTemp + " Demon"); 
            else
               setName("Demon");
            gold = 0;
            reputation = demeanor * Utilities.rollDie(100,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.ROAM;
            fireResistant = true;
            armor=Armor.getHide();
            weapon=Weapon.getDemonicClaw(); 
            if(Math.random() < 0.05)
               items.add(Item.getPentangle());
            else if(Math.random() < 0.025)
               items.add(Item.getDemonsCube());  
            break;
      
         case NPC.DRAGON:           
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            if(Math.random() < 0.25)
               setName(nameTemp + " Dragon"); 
            else
               setName("Dragon");
            gold = 0;
            reputation = Utilities.rollDie(100,1500);
            if(Math.random() < 0.75)
               reputation *= -1;
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            flyer = true;
            fireResistant = true;
            armor=Armor.getHide();//Armor.getDragonScale();
            weapon = Weapon.getDragonfire(); 
            items.add(Item.getDragonScales());  
            break;
      
         case NPC.GHOST:                 
            setName("Spectre"); 
            gold = 0;
            reputation = demeanor * Utilities.rollDie(10,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            flyer = true;
            armor=Armor.getNone();
            if(this.getLocationType().contains("pacrealm"))
            {
               weapon=Weapon.getDeathtouch();
            }
            else
            {
               weapon=Weapon.getBite();   
            }
            break;
      
         case NPC.GIANT:               
            setName("Colosus"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getMassiveFists();   
            break;
            
         case NPC.ABOMINABLE:               
            setName("Abominable"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getBumblePelt();
            weapon=Weapon.getMassiveFists();   
            items.add(Item.getIcejem());
            break;
      
         case NPC.ORC:       
            setName("Orc"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getFangedBite();
            break;
                        
         case NPC.NIGHTORC:                   
            setName("Night-orc"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getFangedBite();
            break;
            
         case NPC.CHUD:                   
            setName("CHUD"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getFangedBite();
            break;
      
         case NPC.GOBLIN: 
         case NPC.GOBLINBARREL:                  
            setName("Goblin"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            gold = 0;
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getBite();
            break;
            
         case NPC.SLIME:                  
            setName("Slime"); 
            reputation = 0;
            walkAndSwim = true;
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getBite();
            break;
            
         case NPC.SEAMONSTER:          
            setName("Sea Serpent"); 
            gold = 0;
            reputation = demeanor * Utilities.rollDie(100,1000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            swimmer = true;
            armor=Armor.getHide();//Armor.getSeaserpentScale();
            weapon=Weapon.getIceBreath(); 
            items.add(Item.getSeaserpentScales());    
            break;
      
         case NPC.SKELETON:            
            setName("Deadite"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getBite();   
            break;
      
         case NPC.TREE:                
            setName("Roper"); 
            gold = 0;
            reputation = demeanor * Utilities.rollDie(10,100);
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            armor=Armor.getWood();
            weapon=Weapon.getCoilingArms();   
            break;
      
         case NPC.GRABOID:               
            setName("Graboid"); 
            gold = 0;
            reputation = demeanor * Utilities.rollDie(10,100);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            armor=Armor.getHide();
            weapon=Weapon.getCrushingJaws();   
            break;
         
         case NPC.TROLL:                  
            setName("Troll"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getSpikedClub();   
            break;
      
         case NPC.CHICKEN:             
            setName("Fowl"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getBite();   
            items.add(new Item("meat", Utilities.rollDie(1,2)));
            break;
      
         case NPC.PIG:              
            setName("Swine"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getBite();   
            items.add(new Item("meat", Utilities.rollDie(2,5)));
            break;
      
         case NPC.BAT:              
            setName("Bat"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            flyer = true;
            armor=Armor.getHide();
            weapon=Weapon.getBite();   
            break;
      
         case NPC.RABBIT:              
            setName("Rabbit"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getBite();   
            items.add(new Item("meat", 1));
            break;
      
         case NPC.ELK:                
            setName("Elk"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getElkPelt();
            weapon=Weapon.getStrikingHooves();
            int numRations = Utilities.rollDie(4,8);
            items.add(new Item("meat", numRations));
            break;
      
         case NPC.HORSE:               
            setName("Horse"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getStrikingHooves();
            numRations = Utilities.rollDie(3,7);
            items.add(new Item("meat", numRations));
            break;
      
         case NPC.RAT:                 
            setName("Rat"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getBite();   
            break;
      
         case NPC.FISH:                 
            setName("Fish"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            swimmer = true;
            armor=Armor.getHide();
            weapon=Weapon.getBite();   
            numRations = Utilities.rollDie(1,2);
            items.add(new Item("meat", numRations));
            break;
      
         case NPC.SHRIEKINGEEL:               
            setName("Shrieking eel"); 
            gold = 0;
            reputation = -1 * Utilities.rollDie(10,100);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            swimmer = true;
            armor=Armor.getHide();
            weapon=Weapon.getCrushingBite();   
            break;
      
         case NPC.SHARK:               
            setName("Shark"); 
            gold = 0;
            reputation = -1 * Utilities.rollDie(10,100);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            swimmer = true;
            armor=Armor.getHide();//getSkarkSkin();
            weapon=Weapon.getCrushingBite();   
            numRations = Utilities.rollDie(2,3);
            items.add(new Item("meat", numRations));
            break;
      
         case NPC.SNAKE:               
            setName("Serpent"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();//getSnakeSkin();
            weapon=Weapon.getPoisonBite(); 
            break;
      
         case NPC.SPIDER:             
            setName("Spider"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getPoisonBite(); 
            break;
      
         case NPC.SQUID:                
            setName("Squid"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            swimmer = true;
            armor=Armor.getHide();
            weapon=Weapon.getCoilingArms();   
            break;
      
         case NPC.WOLF:                  
            setName("Wolfen"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getWolfPelt();
            weapon=Weapon.getFangedBite();   
            break;
            
         case NPC.BEAR:                  
            setName("Bear"); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            walkAndSwim = true;
            armor=Armor.getBearPelt();
            weapon=Weapon.getCrushingBite();   
            break;
      
         case NPC.RUSTCREATURE:                  
            setName("Rustcreature"); 
            gold = 0;
            reputation = 0;
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            walkAndSwim = true;
            armor=Armor.getHide();
            weapon=Weapon.getBite();
            //numInfo for rustcreatures equates to its size
            double roll = Math.random();
            if(roll < 0.7) 
               numInfo = 1;
            else
               numInfo = 2;
            might *= numInfo;
            body = maxHealth();
            break;
      
         case NPC.DOG:                    
            nameTemp = super.getName();
            makeSimpleName(); 
            gold = 0;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            walkAndSwim = true;
            armor=Armor.getHide();
            weapon=Weapon.getFangedBite(); 
            break;
       
         case NPC.TORNADO:             
            setName("Tornado"); 
            gold = 0;
            reputation = 0;
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            flyer = true;
            armor=Armor.getNone();
            weapon=Weapon.getSwirlingChaos();   
            if(Math.random() <  0.1)
            {                                //10% chance this will be an air elemental
               numInfo = 1;
               moveType = NPC.CHASE;
               origMoveType = NPC.CHASE;
            }
            else
            {
               addEffect("control");            //roam around - attack anything it is close to
            }
            break;
      
         case NPC.WHIRLPOOL:           
            setName("Whirlpool"); 
            gold = 0;
            reputation = 0;
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            swimmer = true;
            armor=Armor.getNone();
            weapon=Weapon.getSwirlingChaos();   
            addEffect("control");            //roam around - attack anything it is close to
            break;
      
         case NPC.BRIGAND_SWORD:                
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Brigand"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            if(Math.random() < 0.6)
               armor=Armor.getLeather();
            else
               armor=Armor.getClothes();
            weapon = Weapon.getSword(); 
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            }           
            break;
      
         case NPC.BRIGAND_SPEAR:               
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Brigand"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            if(Math.random() < 0.6)
               armor=Armor.getLeather();
            else
               armor=Armor.getClothes();
            if(Math.random() < 0.5)   
               weapon = Weapon.getSpear();  
            else
               weapon = Weapon.getHalberd();
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            }
            break;
            
         case NPC.BRIGAND_HAMMER:               
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Brigand"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            if(Math.random() < 0.6)
               armor=Armor.getLeather();
            else
               armor=Armor.getClothes();
            weapon = Weapon.getHammer();  
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            }
            break;
        
         case NPC.BRIGAND_DAGGER:                
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Brigand"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            if(Math.random() < 0.6)
               armor=Armor.getLeather();
            else
               armor=Armor.getClothes();
            weapon = Weapon.getDagger();  
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            }
            break;
      
         case NPC.BRIGAND_CROSSBOW:                
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Brigand"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            if(Math.random() < 0.6)
               armor=Armor.getLeather();
            else
               armor=Armor.getClothes();
            weapon = Weapon.getCrossbow();  
            if(Math.random() < 0.025)
            {
               int randXBow = Utilities.rollDie(1,3);
               if(randXBow == 1)
                  weapon = Weapon.getPoisonBoltcaster();
               else if(randXBow == 2)
                  weapon = Weapon.getBaneBoltcaster();
               else
                  weapon = Weapon.getSoulCrossbow();
            }
            numArrows = Utilities.rollDie(0,25);
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            }
            break;
      
         case NPC.BRIGAND_FIST:                
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Brigand"); 
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            if(Math.random() < 0.6)
               armor=Armor.getLeather();
            else
               armor=Armor.getClothes();
            weapon = Weapon.getFists();  
            if(Math.random() < 0.01)
               items.add(Item.getIronBracer());
            if(Math.random() < 0.15)       //chance will have rations
            {
               items.add(new Item("ration", Utilities.rollDie(1,6)));
            }
            break;
      
         case NPC.SORCERER:           
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Sorcerer"); 
            reputation = demeanor * Utilities.rollDie(100,2000);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getClothes();
            if(Math.random() < 0.025)
            {
               int randArmor = Utilities.rollDie(1,3);
               if(randArmor == 1)
                  armor=Armor.getMagesRobe();  
               else if(randArmor == 2)
                  armor=Armor.getHolocaustCloak();
               else
                  armor=Armor.getInvisibilityCloak();            
            }
            weapon = Weapon.getLightningbolt(); 
            if(Math.random() < 0.1)
            {
               int staffType = Utilities.rollDie(1,6);
               if(staffType == 1)
                  weapon = Weapon.getIceStaff();
               else if(staffType == 2)
                  weapon = Weapon.getFlamestaff();
               else if(staffType == 3)
                  weapon = Weapon.getBrightstaff();
               else if(staffType == 4)
                  weapon = Weapon.getWindstaff();
               else if(staffType == 5)
                  weapon = Weapon.getMightstaff();
               else 
                  weapon = Weapon.getMindstaff();
               setAgility(Math.min(getAgility()*2, Player.MAX_STAT));
            }
            if(Math.random() < 0.1)
               items.add(Item.getMannastone());
            else if(Math.random() < 0.05)
               items.add(Item.getMindTome());
            if(Math.random() < 0.25)         //numInfo is the number of undead they can summon
               setNumInfo(Utilities.rollDie(1,9));   
            break;
      
         case NPC.GREATSHIP:           
            setName("Great Ship"); 
            gold = gold * 100;
            reputation = Utilities.rollDie(-100,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            swimmer = true;
            armor=Armor.getWood();
            weapon=Weapon.getCannonball();   
            if(Math.random() < 0.33 && !CultimaPanel.player.hasItem("holdall"))
               items.add(Item.getHoldall());
            else if(Math.random() < 0.25)
            {
               byte[][]currMap = (CultimaPanel.map.get(0));   
               int c=(int)(Math.random()*currMap[0].length);
               int r=(int)(Math.random()*currMap.length);
               if(TerrainBuilder.isGoodSpotForTreasure(r, c))
                  items.add(new Item("treasuremap:("+c+":"+r+")", 500));   
            }
            break;
      
         case NPC.BRIGANDSHIP:            
            setName("Brigand Ship"); 
            gold = gold * 50;
            reputation = demeanor * Utilities.rollDie(10,1000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            armor=Armor.getWood();
            weapon=Weapon.getCannonball();   
            swimmer = true;
            if(Math.random() < 0.5)
            {
               byte[][]currMap = (CultimaPanel.map.get(0));   
               int c=(int)(Math.random()*currMap[0].length);
               int r=(int)(Math.random()*currMap.length);
               if(TerrainBuilder.isGoodSpotForTreasure(r, c))
                  items.add(new Item("treasuremap:("+c+":"+r+")", 500));   
            }
            else if(Math.random() < 0.25 && !CultimaPanel.player.hasItem("holdall"))
               items.add(Item.getHoldall());
            break;
            
         case NPC.DEMONKING:        
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " Demonking"); 
            gold = 500;
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.ROAM;
            fireResistant = true;
            armor=Armor.getHide();
            weapon=Weapon.getFlameblade(); 
            if(Math.random() < 0.5)
            {
               items.add(Item.getPentangle());
            }
            setNumInfo(Utilities.rollDie(5,10));   //number of demons or mimics they can summon    
            break;
      
         case NPC.BEHOLDER:          
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Beholder"); 
            gold = 200;
            reputation = demeanor*Utilities.rollDie(1000,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            flyer = true;
            armor = Armor.getHide();
            weapon = Weapon.getDragonBolt();   
            break;
            
         case NPC.LICHE:          
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Liche"); 
            gold = gold*5;
            reputation = demeanor*Utilities.rollDie(1000,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            fireResistant = true;
            armor = Armor.getHide();
            weapon = Weapon.getDragonBolt();   
            break;
      
         case NPC.DRAGONKING:          
            setName("Trogdor"); 
            if(Math.random() < 0.5)
               setName("Trogdor the Burninator");
            gold = 500;
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            flyer = true;
            fireResistant = true;
            armor = Armor.getHide();//Armor.getDragonqueenScale();
            weapon = Weapon.getDragonBolt();   
            items.add(Item.getDragonQueenScales());  
            break;
         
         case NPC.TREEKING:         
            setName("Great Roper"); 
            gold = 200;
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            armor=Armor.getWood();
            weapon=Weapon.getSwirlingChaos();   
            break;
      
         case NPC.BATKING:          
            setName("Great Bat"); 
            gold = 200;
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            flyer = true;
            armor=Armor.getHide();
            weapon=Weapon.getCrushingBite();   
            break;
      
         case NPC.RATKING:          
            setName("Rat King"); 
            gold = 200;
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getCrushingBite();   
            break;
      
         case NPC.SNAKEKING:        
            setName("Serpent Queen"); 
            gold = 200;
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();//getSnakeSkin();
            weapon=Weapon.getHardPoisionBite(); 
            break;
      
         case NPC.SPIDERKING:          
            setName("Spider Queen"); 
            gold = 200;
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getHardPoisionBite(); 
            break;
      
         case NPC.WOLFKING:         
            setName("Great-Wolfen"); 
            gold = 200;
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getGreatWolfPelt();
            weapon=Weapon.getSwirlingChaos();   
            break;
      
         case NPC.BEARKING:                  
            setName("Great-Bear"); 
            gold = 200;
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            walkAndSwim = true;
            armor=Armor.getGreatBearPelt();
            weapon=Weapon.getSwirlingChaos();   
            break;
            
         case NPC.CAERBANNOG:                  
            setName("Beast-of-Caerbannog"); 
            gold = 0;
            reputation = demeanor*Utilities.rollDie(1000,1500);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            walkAndSwim = true;
            armor=Armor.getHide();
            weapon=Weapon.getCrushingBite();   
            break;
      
         case NPC.SQUIDKING:        
            if(Math.random() < 0.5)
            {
               setName("Squid-Vicious"); 
               reputation = -1 * Utilities.rollDie(1000,2000);
               moveType = NPC.CHASE;
               origMoveType = NPC.CHASE;
            }
            else
            {
               setName("Lord-Squiddish"); 
               reputation = Utilities.rollDie(1000,2000);
               moveType = NPC.ROAM;
               origMoveType = NPC.ROAM;
            }
            gold = 500;
            swimmer = true;
            armor=Armor.getHide();
            weapon=Weapon.getMassiveCoilingArms();  
            items.add(Item.getLifePearl()); 
            break;
            
         case NPC.HYDRACLOPS:        
            setName("Hydraclops"); 
            reputation = -1 * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            gold = 500;
            swimmer = true;
            armor=Armor.getHide();
            weapon=Weapon.getMassiveCoilingArms();  
            if(Math.random() < 0.5)
               items.add(Item.getLifePearl()); 
            else
               items.add(Item.getMannastone());
            numInfo = Utilities.rollDie(3,10);     //number of times it can cast raise-water spell   
            break;
      
         case NPC.BUGBEARKING:         
            setName("Bugbear-King");  
            gold = 200;
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            int weapType = Utilities.rollDie(1,6);
            if(weapType==1)
               weapon=Weapon.getBrightHalberd(); 
            else if(weapType==2)
               weapon=Weapon.getHalberdOfFire(); 
            else if(weapType==3)
               weapon=Weapon.getHalberdOfFrost(); 
            else if(weapType==4)
               weapon=Weapon.getMightHalberd(); 
            else if(weapType==5)
               weapon=Weapon.getMindHalberd(); 
            else //if(weapType==6)
               weapon=Weapon.getWindHalberd(); 
            if(Math.random() < 0.25)
            {
               items.add(Item.getPowerBands());
            }  
            break;
      
         case NPC.BRIGANDKING:            
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " the Brigandking"); 
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.ROAM;
            gold = 200;
            armorType = (int)(Math.random()*4);
            if(armorType==0)
               armor=Armor.getLeather();
            else if(armorType==1)
               armor=Armor.getChainmail();               
            else if(armorType==2)
               armor=Armor.getScalemail();   
            else
               armor=Armor.getPlate();   
            weapType = Utilities.rollDie(1,7);
            if(weapType == 1)   
               weapon = Weapon.getDualFlameblades();
            else if(weapType == 2)   
               weapon = Weapon.getDualFrostblades();
            else if(weapType == 3)   
               weapon = Weapon.getBrightSwords();
            else if(weapType == 4)   
               weapon = Weapon.getSwordsOfMind();
            else if(weapType == 5)   
               weapon = Weapon.getSwordsOfMight();
            else if(weapType == 6)   
               weapon = Weapon.getWindSwords();
            else //if(weapType == 7)   
               weapon = Weapon.getDualblades();   
            break;
            
         case NPC.MALEVAMP:   
         case NPC.FEMALEVAMP:         
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName("~"+nameTemp); 
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.ROAM;
            gold = 200;
            weapon = Weapon.getHardVampyricBite();   
            numInfo = Utilities.rollDie(0,2);   //# spells they can cast
            break;
            
         case NPC.WEREWOLF:         
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            //pick a random human type for the werewolf to change back to   
            byte [] types = {NPC.WOMAN, NPC.MAN};   
            byte pType = types[(int)(Math.random()*types.length)];
            setName(nameTemp + "~"+pType); 
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.ROAM;
            weapon = Weapon.getThrashingJaws();   
            break;
      
         case NPC.BOWASSASSIN:            
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos); 
            setName(nameTemp + " the Bow Assassin"); 
            reputation = demeanor * Utilities.rollDie(1000,2000);
            if(mapIndex == 0)
            {
               moveType = NPC.CHASE;
               origMoveType = NPC.ROAM;
            }
            else
            {
               moveType = NPC.RUN;
               origMoveType = NPC.RUN;
            }
            gold = 200;
            armorType = (int)(Math.random()*2);
            if(armorType==0)
               armor=Armor.getLeather();
            else 
               armor=Armor.getScalemail();   
            int bowType = Utilities.rollDie(1,7);
            if(bowType == 1)   
               weapon = Weapon.getFrostbow(); 
            else if(bowType == 2)
               weapon = Weapon.getWindLongbow();
            else if(bowType == 3)
               weapon = Weapon.getLongbow();
            else if(bowType == 4)
               weapon = Weapon.getBrightLongbow();
            else if(bowType == 5)
               weapon = Weapon.getMightLongbow();
            else if(bowType == 6)
               weapon = Weapon.getMindLongbow();
            else
               weapon = Weapon.getFlamebow();
         
            numArrows = Utilities.rollDie(1,30);
            if(Math.random() < 0.01)
               items.add(Item.getSwiftQuill());
            else if(Math.random() < 0.01)
               items.add(Item.getBowBracer());   
            break;
      
         case NPC.VIPERASSASSIN:          
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);  
            setName(nameTemp + " the Viper-Assassin"); 
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            gold = 200;
            armorType = (int)(Math.random()*2);
            if(armorType==0)
               armor=Armor.getLeather();
            else 
               armor=Armor.getScalemail();   
            int daggerType = Utilities.rollDie(1,10);
            if(daggerType == 1)   
               weapon = Weapon.getPoisonDagger(); 
            else if(daggerType == 2)
               weapon = Weapon.getSoulDagger(); 
            else if(daggerType == 3)
               weapon = Weapon.getFrostDagger();
            else if(daggerType == 4)
               weapon = Weapon.getMagmaDagger();
            else if(daggerType == 5)
               weapon = Weapon.getBaneDagger(); 
            else if(daggerType == 6)
               weapon = Weapon.getBrightDagger(); 
            else if(daggerType == 7)
               weapon = Weapon.getSwiftDagger();
            else if(daggerType == 8)
               weapon = Weapon.getDaggerOfMight();
            else if(daggerType == 9)
               weapon = Weapon.getDaggerOfMind(); 
            else
               weapon = Weapon.getDagger();
            if(Math.random() < 0.01)
               items.add(Item.getViperGlove());
            break;
            
         case NPC.ENFORCER:          
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);  
            setName(nameTemp + " the Dark-Enforcer"); 
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            gold = 200;
            armorType = (int)(Math.random()*2);
            if(armorType==0)
               armor=Armor.getLeather();
            else 
               armor=Armor.getScalemail();   
            int crossbowType = Utilities.rollDie(1,10);
            if(crossbowType == 1)   
               weapon = Weapon.getPoisonBoltcaster(); 
            else if(crossbowType == 2)
               weapon = Weapon.getBaneBoltcaster(); 
            else if(crossbowType == 3)
               weapon = Weapon.getSoulCrossbow();
            else if(crossbowType == 4)
               weapon = Weapon.getFlamecaster();
            else if(crossbowType == 5)
               weapon = Weapon.getFrostcaster();
            else if(crossbowType == 6)
               weapon = Weapon.getBrightcaster();
            else if(crossbowType == 7)
               weapon = Weapon.getMightcaster();
            else if(crossbowType == 8)
               weapon = Weapon.getMindcaster();
            else if(crossbowType == 9)
               weapon = Weapon.getWindcaster();
            else
               weapon = Weapon.getCrossbow();
            numArrows = Utilities.rollDie(0,25);
            if(Math.random() < 0.01)
               items.add(Item.getSwiftQuill());
            else if(Math.random() < 0.01)
               items.add(Item.getBowBracer());
            break;
            
         case NPC.BRIGANDRIDER:          
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);  
            setName(nameTemp + " the Dark-Rider"); 
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            gold = 200;
            armorType = (int)(Math.random()*2);
            if(armorType==0)
               armor=Armor.getLeather();
            else 
               armor=Armor.getScalemail();   
            int axeType = Utilities.rollDie(1,2);
            if(axeType == 1)   
               weapon = Weapon.getAxe(); 
            else// if(axeType == 2)
               weapon = Weapon.getMirroredAxe(); 
            if(Math.random() < 0.01)
               items.add(Item.getIronBracer());
            break;
            
         case NPC.TROLLKING:                  
            nameTemp = super.getName();
            pos = nameTemp.indexOf(" of");
            if(pos > 0)
               nameTemp = nameTemp.substring(0, pos);
            setName(nameTemp + " Trollking");
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapType = Utilities.rollDie(1,7);
            if(weapType==1)
               weapon=Weapon.getBaneHammer(); 
            else if(weapType==2)
               weapon=Weapon.getExoticFrostHammer(); 
            else if(weapType==3)
               weapon=Weapon.getExoticHammerOfFire(); 
            else if(weapType==4)
               weapon=Weapon.getBrightExoticHammer(); 
            else if(weapType==5)
               weapon=Weapon.getMightExoticHammer(); 
            else if(weapType==6)
               weapon=Weapon.getMindExoticHammer(); 
            else //if(weapType==7)
               weapon=Weapon.getSwiftExoticHammer();   
            break;
         
         case NPC.PHANTOM:                 
            gold = 0;
            reputation = demeanor * Utilities.rollDie(1000,2000);
            flyer = true;
            armor=Armor.getNone();
            if(Math.random() < 0.05)
            {
               setName("Phantom-Queen"); 
               moveType = NPC.CHASE;
               origMoveType = NPC.CHASE;
               weapon=Weapon.getVampyricAxes();   
            }
            else
            {
               setName("Phantom"); 
               moveType = NPC.ROAM;
               origMoveType = NPC.ROAM;
               weapon=Weapon.getBite();   
            }
            break;   
         
         case NPC.MAGMA:             
            setName("Magma-mother"); 
            gold = 0;
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            fireResistant = true;
            armor=Armor.getNone();
            weapon=Weapon.getFireball();   
            addEffect("fire");           
            if(Math.random() < 0.5)
               items.add(Item.getRandomStone());
            setNumInfo(Utilities.rollDie(0,10));   //number of elementals they can summon
            break;
         
         case NPC.DEATH:        
            setName("Death"); 
            gold = 0;
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            fireResistant = true;
            flyer = true;
            armor=Armor.getHide();
            weapon=Weapon.getDeathtouch(); 
            int numStones = Utilities.rollDie(1,4);
            for(int i=0; i<numStones; i++)
               if(Math.random() < 0.5)
                  items.add(Item.getRandomStone());
            break;
        
         case NPC.FIRE:             
            setName("Fire"); 
            //if numInfo is 0, it is a regular fire.  
            //if numInfo is not zero, it is a fire elemental that will chase you and stores the number of other elementals they can summon
            //numInfo = Utilities.rollDie(5,10);    
            gold = 0;
            reputation = demeanor * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            flyer = true;
            armor=Armor.getNone();
            weapon=Weapon.getBurn();   
            addEffect("fire");         
            break;
         
         case NPC.STONE:          
            setName("Stone spire");  
            gold = 0;
            reputation = 0;
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            fireResistant = true;
            armor=Armor.getNone();
            weapon = Weapon.getNone();  
            numInfo = -999; 
            break; 
            
         case NPC.MONOLITH:          
            setName("Monolith");  
            gold = 0;
            reputation = demeanor*Utilities.rollDie(1000,2000);
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            fireResistant = true;
            armor=Armor.getNone();
            weapon = Weapon.getDragonBolt();  
            numInfo = -999; 
            break; 
            
         case NPC.JAWTRAP:          
            setName("Jaw-trap");  
            gold = 0;
            reputation = 0;
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            fireResistant = true;
            armor=Armor.getNone();
            weapon = Weapon.getNone();  
            numInfo = 0; 
            break;
            
         case NPC.BARREL:          
            setName("Barrel"); 
            if(Math.random() < 0.95)       //chance will not have gold
               gold = 0;
            if(Math.random() < 0.15)       //chance will have rations
            {
               numRations = Utilities.rollDie(1,15);
               items.add(new Item("meat", numRations));
            } 
            if(this.getLocationType().contains("wolfenstein"))
            {
               gold *= 3;
               if(Math.random() < 0.33)       //extra chance will have rations
               {
                  numRations = Utilities.rollDie(1,15);
                  items.add(new Item("meat", numRations));
               } 
            }
            else
            {
               if(Math.random() < 0.05)      //chance will have a potion
                  items.add(Potion.getRandomSimplePotion());
               if(Math.random() < 0.10)
                  numArrows = Utilities.rollDie(2,25);  
            }
            reputation = 0;
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            armor=Armor.getNone();
            weapon = Weapon.getNone();  
            numInfo = -99; 
            break;
            
         case NPC.BOAT:          
            setName("Boat"); 
            gold = 0;
            reputation = 0;
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            armor=Armor.getNone();
            weapon = Weapon.getOar();  
            numInfo = -99;
            swimmer = true; 
            hasMet = true;
            break;
            
         case NPC.PTEROSAUR:
            setName("Pterosaur"); 
            gold = 0;
            reputation = Utilities.rollDie(10,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            flyer = true;
            armor=Armor.getHide();
            weapon=Weapon.getCrushingBite();   
            break;
            
         case NPC.BRACHIOSAUR:
            setName("Brachiosaur"); 
            gold = 0;
            reputation = Utilities.rollDie(10,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            walkAndSwim = true;
            weapon=Weapon.getMassiveFists();   
            numRations = Utilities.rollDie(15,20);
            items.add(new Item("meat", numRations));
            break;
            
         case NPC.ALLOSAUR:
            setName("Allosaur"); 
            gold = 0;
            reputation = (-1 * Utilities.rollDie(10,110)) + 11;
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            armor=Armor.getHide();
            weapon=Weapon.getMassiveFists(); 
            if(Math.random() < 0.33)
               items.add(Item.getAllosaurEgg());
            break;
            
         case NPC.STEGOSAUR:
            setName("Stegosaur"); 
            gold = 0;
            reputation = Utilities.rollDie(10,110) - 21;
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getMassiveFists();   
            numRations = Utilities.rollDie(10,15);
            items.add(new Item("meat", numRations));
            break;
            
         case NPC.PLESIOSAUR:
            setName("Plesiosaur"); 
            gold = 0;
            reputation = -1 * Utilities.rollDie(10,100);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            swimmer = true;
            armor=Armor.getHide();
            weapon=Weapon.getCrushingBite();   
            break;
            
         case NPC.DILOPHOSAUR:
            setName("Dilophosaur"); 
            gold = 0;
            reputation = -1 * Utilities.rollDie(10,100);
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getHardPoisionBite();   
            break;
            
         case NPC.SOLDIER:   
            setName("Soldier "+Utilities.getGermanName()); 
            armor=Armor.getClothes();
            weapon = Weapon.getGun(); 
            reputation = -1 * Utilities.rollDie(100,1000); 
            if(getLocationType().contains("1942"))
            {
               moveType = NPC.CHASE;
               origMoveType = NPC.CHASE;
            } 
            else
            {
               int marchDir = Utilities.rollDie(1, 4);
               if(marchDir == 1)
               {
                  moveType = NPC.MARCH_NORTH;
                  origMoveType = NPC.MARCH_NORTH;
               }
               else if(marchDir == 2)
               {
                  moveType = NPC.MARCH_EAST;
                  origMoveType = NPC.MARCH_EAST;
               }
               else if(marchDir == 3)
               {
                  moveType = NPC.MARCH_SOUTH;
                  origMoveType = NPC.MARCH_SOUTH;
               }
               else //if(marchDir == 4)
               {
                  moveType = NPC.MARCH_WEST;
                  origMoveType = NPC.MARCH_WEST;
               }
            }
            break;
            
         case NPC.OFFICER:   
            setName("Officer "+Utilities.getGermanName()); 
            armor=Armor.getClothes();
            weapon = Weapon.getGun();   
            reputation = -1 * Utilities.rollDie(100,1000); 
            gold *= (int)(Math.random()*100)+1;
            if(getLocationType().contains("1942"))
            {
               moveType = NPC.CHASE;
               origMoveType = NPC.CHASE;
            } 
            else
            {
               int marchDir = Utilities.rollDie(1, 2);
               if(marchDir == 1)
               {
                  moveType = NPC.STILL;
                  origMoveType = NPC.STILL;
               }
               else //if(marchDir == 2)
               {
                  moveType = NPC.ROAM;
                  origMoveType = NPC.ROAM;
               }
            }
            break;
      
         case NPC.GUARD_DOG:                    
            gold = 0;
            reputation = -1 * Utilities.rollDie(100,1000); 
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            walkAndSwim = true;
            armor=Armor.getHide();
            weapon=Weapon.getFangedBite(); 
            break;
      
         case NPC.TANK:
            gold *= 100;
            reputation = -1 * Utilities.rollDie(100,1000); 
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            armor=Armor.getHide();
            weapon=Weapon.getCannonball();  
            break;
            
         case NPC.SPITFIRE:
            gold = 0;
            reputation = Utilities.rollDie(10,1000); 
            moveType = NPC.MARCH_EAST;
            origMoveType = NPC.MARCH_EAST;
            if(Math.random() < 0.5)
            {
               moveType = NPC.MARCH_WEST;
               origMoveType = NPC.MARCH_WEST;
            }
            armor=Armor.getHide();
            weapon=Weapon.getDragonBolt();  
            flyer = true;
            numInfo = Utilities.rollDie(0,2);   //the number of bombs they can drop
            break;
            
         case NPC.UNICORN:               
            setName("Unicorn"); 
            gold = 0;
            reputation = Utilities.rollDie(500,1500);
            if(reputation % 2 == 0) //start with odd reputation - must be even to tame them, and must feed flowers to make it even
               reputation++;
            moveType = NPC.ROAM;
            origMoveType = NPC.ROAM;
            armor=Armor.getHide();
            weapon=Weapon.getBrightHorn();
            numRations = Utilities.rollDie(3,7);
            items.add(new Item("meat", numRations));
            break;    
            
         case NPC.TRIFFID:         
            setName("Triffid"); 
            reputation = demeanor*Utilities.rollDie(100,1500);
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            armor=Armor.getWood();
            weapon=Weapon.getCoilingArms();   
            break;    
            
         case NPC.WILLOWISP:         
            setName("Will-o-the-Wisp"); 
            reputation = demeanor*Utilities.rollDie(1000,1500);
            if(this.getLocationType().contains("pacrealm"))
            {
               setName("pellet");
               moveType = NPC.STILL;
               origMoveType = NPC.STILL;
               might = 1;
               agility = 0;
               mind = 0;
            }
            else
            {
               moveType = NPC.ROAM;
               origMoveType = NPC.ROAM;
            }
            flyer = true;
            armor=Armor.getNone();
            weapon=Weapon.getNone();   
            break;
            
         case NPC.MAZEMOUTH:                
            setName("Maze-mouth"); 
            gold = 0;
            reputation = demeanor * Utilities.rollDie(1000,1500);
            moveType = NPC.MARCH_EAST;
            origMoveType = NPC.MARCH_EAST;
            armor=Armor.getNone();
            weapon=Weapon.getDeathtouch(); 
            agility = (byte)(CultimaPanel.player.getAgility()*0.9);
            break;
       
         case NPC.MIMIC:                
            setName("Mimic"); 
            gold = Utilities.rollDie(10,100);
            reputation = demeanor * Utilities.rollDie(1000,1500);
            moveType = NPC.STILL;
            origMoveType = NPC.STILL;
            if(Math.random() < 0.25)
               armor=Armor.getRandomArmor();
            else
               armor=Armor.getNone();
            weapon=Weapon.getThrashingJaws();   
            break;
            
         case NPC.EARTHELEMENTAL:        
            setName("Earth Elemental"); 
            reputation = -1 * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            gold = 0;
            armor=Armor.getHide();
            weapon=Weapon.getMassiveCoilingArms(); 
            walkAndSwim = true;
            for(int i=0; i< 3; i++)
            { 
               items.add(Item.getRandomStone());
            }
            numInfo = Utilities.rollDie(0,2);     //number of times it can cast raise-stone spell   
            break;
            
         case NPC.GOBZILLY: 
            setName("Queen of the Monsters");                 
            reputation = -1 * Utilities.rollDie(1000,2000);
            moveType = NPC.CHASE;
            origMoveType = NPC.CHASE;
            gold = 0;
            fireResistant = true;
            armor = Armor.getHide();//Armor.getDragonqueenScale();
            weapon = Weapon.getDragonBolt();   
            items.add(Item.getDragonQueenScales()); 
            walkAndSwim = true;
            for(int i=0; i< 3; i++)
            { 
               items.add(Item.getRandomStone());
            }
            break;
      };
   }
    
   public void setIsSwinePlayer(boolean sp)
   {
      isSwinePlayer = sp;
   }
    
   public boolean swinePlayer()
   {
      return ((isSwinePlayer || hasItem("-swine-bounty") || hasItem("loaded-cube") || hasItem("swine-cube")) && !this.isZombie());
   }
      
   public void setName(String n) 
   {
      super.setName(n);
      if(this.isShopkeep())
      {
         int mstat = Utilities.rollDie(1,3);
         if(mstat==0)
            maritalStatus = 'M';
         else if(mstat==1)
            maritalStatus = 'W';
         else //if(mstat==2)
            maritalStatus = 'N';
      }
   }

//pre:  percent > 0
//post: adjust the players stats by percent (for battles)
   public void modifyStats(double percent)
   {
      might = (int)(might*percent);
      mind = (int)(mind*percent);
      agility = (int)(agility*percent);
      body = (int)(body*percent);
   }

   public char getMaritalStatus()
   {
      return maritalStatus;
   }
   
   public void setMaritalStatus(char m)
   {
      maritalStatus = m;
   }
   
   public void pickMaritalStatus()
   {
      int r = Utilities.rollDie(0,2);                          //travelers in temples, those in castles and prisoners can not be married
      if(TerrainBuilder.isCity(location) && !isShopkeep())  //only those in cities can be married that are not shopkeeps (they need to do their jobs)
         r = Utilities.rollDie(0,3);
      if(this.isWerewolf())
         r = Utilities.rollDie(1,3);   
      if(r==0)
         maritalStatus = 'M';
      else if(r==1)
         maritalStatus = 'W';
      else if(r==2)
         maritalStatus = 'N';
      else
         maritalStatus = 'S';
   }

   public boolean isStatue()
   {
      return (this.getNumInfo()==-999);
   }
   
   public boolean isZombie()
   {
      return (this.getNumInfo() == -666);
   }
   
   public boolean isPrisoner()
   {
      return (this.getCharIndex()==NPC.BEGGER && (this.getLocationType().contains("cave") || this.getLocationType().contains("mine") || this.getLocationType().contains("dungeon") || this.getLocationType().contains("lair")) && this.isCaged());
   }
   
   public boolean isEtheral()
   {
      return (this.getCharIndex()==NPC.GHOST || this.getCharIndex()==NPC.PHANTOM);
   }
   
   public boolean isWerewolf()
   {
      return (this.getName().contains("~") && this.getCharIndex()!=NPC.MALEVAMP && this.getCharIndex()!=NPC.FEMALEVAMP && this.getCharIndex()!=NPC.BAT);
   }
   
   public boolean isVampire()
   {
      return this.getName().startsWith("~") && (this.getCharIndex()==NPC.MALEVAMP || this.getCharIndex()==NPC.FEMALEVAMP || this.getCharIndex()==NPC.BAT);
   }
   
   public boolean isOurSpouse()
   {
      return (this.getName().startsWith("+"));
   }

    //see if we are a temporary non-player entity used to mark locations and make them easy to find with the Knowing spell
   public boolean isNonPlayer()
   {
      return (this.getName().equals("temp"));
   }
   
   public boolean isPellet()
   {
      return (this.getCharIndex()==NPC.WILLOWISP && this.getLocationType().contains("pacrealm"));
   }

   public int getNumArrows()
   {
      return numArrows;
   }

   public void setNumArrows(int na)
   {
      numArrows = na;
   }

   public void addArrow()
   {
      numArrows++;
   }

   public boolean hasBeenRescued()
   {
      return hasBeenRescued;
   }

   public void setHasBeenRescued(boolean hbr)
   {
      hasBeenRescued = hbr;
   }

   public boolean hasTrained()
   {
      return hasTrained;
   }

   public void setHasTrained(boolean ht)
   {
      hasTrained = ht;
   }

   public void setSellPrice(int sp)
   {
      sellPrice = sp;
   }

   public int getSellPrice()
   {
      return sellPrice;
   }   

   public boolean hasBeenAttacked()
   {
      return hasBeenAttacked;
   }

   public double getAttackTime()
   {
      return attackTime;
   }

   public void setAttackTime(double at)
   {
      attackTime = at;
   }

   public void setHasBeenAttacked(boolean hba)
   {
      hasBeenAttacked = hba;
   }

   public ArrayList<String> getEffects()
   {
      return effects;
   }

   public void clearEffects()
   {
      effects.clear();
   }

   public boolean isPoisoned()
   {
      return this.hasEffect("poison");
   }
   
   //true if this has an item needed for a mission so we don't clear it from the worldMonsters list when changing locations
   public boolean hasSlayMissionItem()
   {
      return (this.hasItem("tooth") || this.hasItem("head")  || this.hasItem("essence") || this.hasItem("life-pearl") || this.hasItem("robertsmask"));
   }

   public boolean addEffect(String eff)
   {
      if(effects!=null && !effects.contains(eff))
      {
         if(this.getCharIndex() == NPC.BARREL || this.getCharIndex() == NPC.DEATH || this.getCharIndex() == NPC.MONOLITH  || this.getCharIndex() == NPC.STONE || this.getCharIndex() == NPC.SLIME || this.isStatue())   //no effects on stone
            return false;
         if(this.getCharIndex() == NPC.GHOST || this.getCharIndex() == NPC.PHANTOM)
            return false;     
         if((this.getCharIndex() == NPC.DRAGON || this.getCharIndex() == NPC.DRAGONKING || this.getCharIndex() == NPC.DEMON  || this.getCharIndex() == NPC.DEMONKING) && eff.contains("fire"))
            return false;
         if((NPC.isUndead(this.getCharIndex()) || this.getCharIndex() == NPC.DEMON  || this.getCharIndex() == NPC.DEMONKING || this.getCharIndex() == NPC.SNAKE  || this.getCharIndex() == NPC.SNAKEKING || this.getCharIndex() == NPC.TREE  || this.getCharIndex() == NPC.TREEKING || 
            this.getCharIndex() == NPC.TRIFFID || NPC.isEtheral(this.getCharIndex()) || this.getCharIndex() == NPC.SPIDER  || this.getCharIndex() == NPC.SPIDERKING || NPC.isVampire(this.getCharIndex()) || this.isZombie() || NPC.isShip(this.getCharIndex())) && eff.contains("poison"))
            return false; 
         if((this.getCharIndex() == NPC.HYDRACLOPS || this.getCharIndex() == NPC.SEAMONSTER || this.getCharIndex() == NPC.ABOMINABLE) && eff.contains("freeze"))
            return false;
         if((this.getCharIndex() == NPC.GREATSHIP || this.getCharIndex() == NPC.BRIGANDSHIP) && (eff.contains("stun") || eff.contains("control") || eff.contains("web") || eff.contains("poison") || eff.contains("freeze")))
            return false;
         if((this.getCharIndex() == NPC.RUSTCREATURE) && (eff.contains("stun") || eff.contains("control")))
            return false;   
         if((this.getCharIndex() == NPC.GRABOID || this.getCharIndex() == NPC.SPIDER || NPC.getSize(this.getCharIndex())>=2 || NPC.needsWater(this) || this.getCharIndex() == NPC.MAGMA) && eff.contains("web"))
            return false;    
         if(this.getMind() >= CultimaPanel.player.getMind() && eff.contains("control"))
            return false;
         if(this.getMind() >= CultimaPanel.player.getMind() && eff.contains("curse"))
            return false;   
         effects.add(eff);
         if(this.hasItem("meat") && eff.contains("poison"))    //we poisoned our game - no rations to collect
            this.removeItem("meat");
         String armor = this.getArmor().getName().toLowerCase();
         if((armor.contains("pelt") || armor.contains("skin")) && eff.contains("fire"))    //we are set on fire - no pelt to collect
            this.setArmor(Armor.getHide());
      
         return true;
      }
      return false;
   }
   
   public boolean addEffectForce(String eff)
   {
      if(effects!=null && !effects.contains(eff))
      {
         if(this.getCharIndex() == NPC.STONE || this.getCharIndex() == NPC.MONOLITH || this.isStatue())   //no effects on stone
            return false; 
         effects.add(eff);
         if(this.hasItem("meat") && eff.contains("poison"))    //we poisoned our game - no rations to collect
            this.removeItem("meat");
         String armor = this.getArmor().getName().toLowerCase();
         if((armor.contains("pelt") || armor.contains("skin")) && eff.contains("fire"))    //we are set on fire - no pelt to collect
            this.setArmor(Armor.getHide());
         return true;
      }
      return false;
   }

   public void removeEffect(String eff)
   {
      if(effects!=null && effects.contains(eff))
      {
         for(int i=0; i<effects.size(); i++)
            if(effects.get(i).equals(eff))
            {
               effects.remove(i);
               break;
            }
      }
   }

   public boolean isTrapped()
   {
      return (Utilities.jawTrapAt(this.getRow(), this.getCol(), this.getMapIndex()) && this.getMoveType()==NPC.STILL);
   
   }

   public boolean isUnaware()
   {
      return (this.hasEffect("control") || this.hasEffect("stun") || this.isStatue() || this.isZombie());
   }

   //true if this NPC is totally blocked in
   public boolean isCaged()
   {
      byte[][]currMap = (CultimaPanel.map.get(this.getMapIndex()));   
      int r = this.getRow();
      int c = this.getCol();
      return (LocationBuilder.isImpassableNotWater(currMap, r-1, c) && LocationBuilder.isImpassableNotWater(currMap, r+1, c) && LocationBuilder.isImpassableNotWater(currMap, r, c-1) && LocationBuilder.isImpassableNotWater(currMap, r, c+1));
   }
   
   //true if this NPC is totally blocked in by the player and 3 sides
   public boolean isBoxedIn()
   {
      byte[][]currMap = (CultimaPanel.map.get(this.getMapIndex()));   
      int r = this.getRow();
      int c = this.getCol();
      int pRow = CultimaPanel.player.getRow();
      int pCol = CultimaPanel.player.getCol();
      int blockedSides = 0;
      if(LocationBuilder.isImpassableOrWater(currMap, r-1, c))
      {
         blockedSides++;
      }
      if(LocationBuilder.isImpassableOrWater(currMap, r, c+1))
      {
         blockedSides++;
      }
      if(LocationBuilder.isImpassableOrWater(currMap, r+1, c))
      {
         blockedSides++;
      }
      if(LocationBuilder.isImpassableOrWater(currMap, r, c-1))
      {
         blockedSides++;
      }
      return (Display.findDistance(r, c, pRow, pCol)==1 && blockedSides==3);
   }

   public boolean hasEffect(String eff)
   {
      if(eff.contains("web")) //ends with value of the shooters's level
         eff = "web";
      return (effects!=null && effects.contains(eff));
   }

   public void runEffects()
   {
      boolean indoors = false;
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      if(this.getMapIndex() < 0 || this.getMapIndex() >= CultimaPanel.map.size())
         return;
      byte[][]currMap = (CultimaPanel.map.get(this.getMapIndex()));   
      String thisTerrain = "";
      if(this.getRow() >=0 && this.getCol() >= 0 && this.getRow()<currMap.length && this.getCol() < currMap.length)
         thisTerrain = CultimaPanel.allTerrain.get(Math.abs(currMap[this.getRow()][this.getCol()])).getName().toLowerCase();
   
      if(locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("pacrealm"))
         indoors = true;
      if(charIndex==NPC.FIRE && CultimaPanel.player.getActiveSpell("Firestorm")==null && !locType.contains("underworld"))
      {
         int baseDamage = 1;
         if(!LocationBuilder.isCombustable(thisTerrain))
            baseDamage = 5;
         int damageBoost = (CultimaPanel.weather*4); 
         if(indoors)
         {
            damageBoost = 0;
         }  
         damage(baseDamage+damageBoost,"world");
         return;
      } 
      else if(charIndex==NPC.MAGMA && CultimaPanel.weather > 0 && !indoors && !locType.contains("underworld"))
      {
         damage(CultimaPanel.weather*4,"world");
         return;
      }   
      if(this.isNonPlayer())
         return;  
   
      if(getBody() < 0)
      {
         setMoveTypeTemp(NPC.STILL);
         statMod[0] = -1 * Math.max(1,(might-1));
         statMod[1] = -1 * Math.max(1,(mind-1));
         statMod[2] = -1 * Math.max(1,(agility-1));   
         return;
      }
      if(isTrapped() && Math.random() < 0.25)
      {
         damage(1, "trap");
      }
      if(hasEffect("poison") && Math.random() < 0.5)
      {
         int dam = 5;
         damage(dam, "poison");
      }
      if(hasEffect("fire") && charIndex!=NPC.FIRE  && charIndex!=NPC.MAGMA)
      {
         boolean firestorm = (CultimaPanel.player.getActiveSpell("Firestorm")!=null);
         int extinguishBonus = CultimaPanel.weather * 10;
         if(firestorm)
            extinguishBonus *= -1;
         damage(5, "fire");
         if(Utilities.rollDie(750) < this.getAgility()+extinguishBonus)               //chance the fire goes out
         {
            removeEffect("fire");
         }
         if((swimmer && charIndex!=NPC.GREATSHIP && charIndex!=NPC.BRIGANDSHIP) || charIndex==NPC.GRABOID || thisTerrain.contains("water"))   //fire goes out more often if in water or underground
         {
            if(Utilities.rollDie(Player.MAX_STAT) < Math.max(5,this.getAgility()*2))               //chance the fire goes out
            {
               removeEffect("fire");
            }
         }
      }
      if(hasEffect("control"))
      {
         if(Utilities.rollDie(1000) < this.getMind() && this.getNumInfo()!=999)  //chance the possession wears off
         {                                                                    //999 numInfo is for animals that are tamed        
            removeEffect("control");
         }
      }
      if(hasEffect("stun"))
      {
         if(this.getCharIndex()==NPC.DEATH)                   //chance the stun wears off
         {
            if(Utilities.rollDie(750) < this.getLevel())
               removeEffect("stun");
         }
         else if(Utilities.rollDie(150) < this.getLevel())                   //chance the stun wears off
         {
            removeEffect("stun");
         }
      }
      if(hasEffect("curse") || (clumsyInRain() && CultimaPanel.weather > 0 && !indoors))
      {
         int rand = (int)(Math.random()*3);
         if(rand == 0)
            statMod[0] = -1 * might / 2;
         else if(rand == 1)
            statMod[1] = -1 * mind / 2;
         else
            statMod[2] = -1 * agility / 2;
      }
      if(agileInRain() && !indoors)
      {
         int rand = (int)(Math.random()*3);
         if(rand == 0)
            statMod[0] = might / 2;
         else if(rand == 1)
            statMod[1] = mind / 2;
         else
            statMod[2] = agility / 2;
      }
      if(hasEffect("freeze"))   //undo freeze
      {
         statMod[2] = -1 * agility / 4;
         if(Utilities.rollDie(200) < this.getMight())
            moveType = origMoveType;
      }
      if(hasEffect("still") && CultimaPanel.time < 20 && CultimaPanel.time > 6 && this.getAgility() > Utilities.rollDie(200))
         removeEffect("still");
      if(hasEffect("web") && (this.getAgility()+this.getMight()) > Utilities.rollDie(1000))
         removeEffect("web");    
   }
   
   public boolean clumsyInRain()
   {
      return (charIndex==NPC.TROLL || charIndex==NPC.CYCLOPS);
   }
   
   public boolean agileInRain()
   {
      return (charIndex==NPC.TREE || charIndex==NPC.TREEKING || charIndex==NPC.SNAKE || charIndex==NPC.SNAKEKING);
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

   public double getAttackHitTime()
   {
      return attackHitTime;
   }

   public void setAttackHitTime(double ht)
   {
      attackHitTime = ht;
   }

   public double getAttackMissTime()
   {
      return attackMissTime;
   }

   public void setAttackMissTime(double ht)
   {
      attackMissTime = ht;
   }


   public ArrayList<Item> getItems()
   {
      return items;
   }

   public ArrayList<Item> getStealableItems()
   {
      ArrayList<Item> ans = new ArrayList<Item>();
      for(Item it: items)  //don't allow bounties and swine-cubes to be stealable (having the swine-cube allows them to be identified as a swine player)
         if(!it.getName().contains("bounty") && !it.getName().contains("swine-cube") && !it.getName().contains("head"))
            ans.add(new Item(it.getName(), it.getValue()));
      return ans;
   }

   public void setItems(ArrayList<Item> i) 
   {
      items = i;
   }

   public void clearItems()
   {
      items.clear();
   }
   
   public void clearItemsExceptBounties()
   {
      for(int i=items.size()-1; i>=0; i--)
      {
         Item it = items.get(i);
         if(!it.getName().contains("bounty") && !it.getName().contains("head") && !it.getName().contains("tooth") && !it.getName().contains("essence") && !it.getName().contains("robertsmask"))
            items.remove(i);
      }
   }

   public void addItem(Item i)
   {
      items.add(i);
   }
   
   public void addItems(ArrayList<Item> itemsToAdd)
   {
      for(Item i:itemsToAdd)
         items.add(i);
   }

   public boolean hasItem(String i)
   {
      for(Item item: items)
      {
         if(i.contains("message") && item.getName().toLowerCase().contains("message"))
            return true;
         if(i.contains("parcel") && item.getName().toLowerCase().contains("parcel"))
            return true;
         if(i.contains("-swine-bounty") && item.getName().toLowerCase().contains("-swine-bounty"))
            return true;
         if(i.contains("head") && item.getName().toLowerCase().contains("head"))    //for monster slay missions
            return true;
         if(i.contains("tooth") && item.getName().toLowerCase().contains("tooth"))    //for monster slay missions
            return true;   
         if(i.contains("essence") && item.getName().toLowerCase().contains("essence"))    //for monster slay missions
            return true;
         if(item.getName().toLowerCase().equals(i.toLowerCase()))
            return true;   
      }
      return false;
   }

   public Item getItem(String i)
   {
      for(Item item: items)
      {
         if(i.contains("message") && item.getName().toLowerCase().contains("message"))
            return item;
         if(i.contains("parcel") && item.getName().toLowerCase().contains("parcel"))
            return item;
         if(i.contains("-swine-bounty") && item.getName().toLowerCase().contains("-swine-bounty"))
            return item;
         if(item.getName().toLowerCase().equals(i.toLowerCase()))
            return item;
      }
      return null;
   }

//returns the bounty on this target
   public int getBounty()
   {
      for(Item item: items)
      {
         if(item.getName().toLowerCase().contains("bounty") || item.getName().toLowerCase().contains("head"))
            return item.getValue();
      }
      return 0;
   }
   
   //priority level of mission we might have with this npc
   //0 is no mission
   //1 is mission assignment taken but in progress 
   //2 is mission assignment not taken yet
   //3 is target of mission in some way
   public int getMissionPriority()
   {
      if(this.isEscortedNpc())
      {  //we want our escortee to have a green dot
         return 1;
      }
      String npcName = this.getName().toLowerCase();
      if(this.hasItem("message") || this.getBounty()>0 || this.hasMissionItem() || npcName.contains("sibling") || npcName.contains("brute") || npcName.contains("masterthief"))
         return 3;
      if(this.getNumInfo()==10 || this.swinePlayer()  || ((this.getCharIndex()==NPC.KING || npcName.contains("arenamaster") || npcName.contains("skara brae") || npcName.contains("puzzle")) && this.getNumInfo() > 0))
      {    
         if(this.getMissionInfo().equals("none"))   //mission hasn't been accepted yet
            return 2;
         else
            return 1;
      }
      return 0;
   }

//returns true if NPC has an item that is tied to a Player mission
   public boolean hasMissionItem()
   {
      if(CultimaPanel.missionStack.size()==0)
         return false;
      boolean daggerMission = false;   
      ArrayList<String> itemNames = new ArrayList<String>();
      for(Mission m: CultimaPanel.missionStack)
         if(m.getTarget()!=null)
         {
            String targName = m.getTarget().getName().toLowerCase();
            itemNames.add(targName);
            if(targName.contains("dagger"))
               daggerMission = true;
         }
      if(daggerMission)
      {
         String [] magicDaggers = {"Poison-dagger","Souldagger","Magmadagger","Frostdagger","Banedagger"};   
         for(String dag:magicDaggers)
         {
            if(((this.getWeapon().getName()).toLowerCase()).equals(dag.toLowerCase()))
               return true;
         }    
      }      
      if(items.size() > 0)    
         for(Item NPCitem: items)
            for(String MissionItem: itemNames)
               if(NPCitem.getName().toLowerCase().contains(MissionItem))
                  return true;
      
      return false;
   }
   
   //returns true if in conatins an element of check
   public static boolean stringHasPart(String in, String check)
   {
      String[]parts = check.split(" ");
      for(String p:parts)
         if(in.contains(p))
            return true;
      return false;
   }
   
   public boolean hasMissionItemExclude(String excludeName)
   {
      if(CultimaPanel.missionStack.size()==0)
         return false;
      boolean daggerMission = false;   
      ArrayList<String> itemNames = new ArrayList<String>();
      for(Mission m: CultimaPanel.missionStack)
         if(m.getTarget()!=null)
         {
            String targName = m.getTarget().getName().toLowerCase();
            itemNames.add(targName);
            if(targName.contains("dagger"))
               daggerMission = true;
         }
      if(daggerMission)
      {
         String [] magicDaggers = {"Poison-dagger","Souldagger","Magmadagger","Frostdagger","Banedagger"};   
         for(String dag:magicDaggers)
         {
            if(((this.getWeapon().getName()).toLowerCase()).equals(dag.toLowerCase()))
               return true;
         }    
      }      
      if(items.size() > 0)    
         for(Item NPCitem: items)
            for(String MissionItem: itemNames)
            {
               String itemName = NPCitem.getName().toLowerCase();
               if(itemName.contains(MissionItem) && !stringHasPart(itemName, excludeName))
                  return true;
            }
      return false;
   }

   public Item getItemForSale()
   {
      for(Item item: items)
         if(item.getValue()==getSellPrice())
            return item;
      return null;
   }

   public boolean removeItem(String itemName)
   {          
      itemName = itemName.toLowerCase();
      for(int i=items.size()-1; i>=0; i--)
      {
         String itm = (items.get(i).getName()).toLowerCase();
         if(itm.contains(itemName))
         {
            items.remove(i);
            return true;      
         }
      }
      return false;
   }

   public boolean removeBounty()
   {        
      String bounty = "";  
      String ourName = Utilities.shortName(this.getName()).toLowerCase();
      for(int i=items.size()-1; i>=0; i--)
      {
         String itm = (items.get(i).getName()).toLowerCase();
         if(itm.contains("bounty"))
         {
            bounty = itm;
            items.remove(i);
            break;      
         }
      }
      boolean success = false;
      if(bounty.length()>0)      //remove the mission that has that item name
      {
         for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
         {
            Mission m = CultimaPanel.missionStack.get(i);
            if(m.getType().equals("Assassinate") && m.getTarget().getName().toLowerCase().contains(ourName))
            {
               CultimaPanel.missionStack.remove(i);
               success = true;
               break;
            }
         }
         for(int i=0; i<CultimaPanel.civilians.size(); i++)
            for(NPCPlayer p:CultimaPanel.civilians.get(i))
            {
               String mission = p.getMissionInfo().toLowerCase();
               if(mission.contains("assassinate") && mission.contains(ourName))
               {
                  p.setMissionInfo("none");
                  if(p.getNumInfo() == 10 && !p.getName().toLowerCase().contains("arenamaster"))
                     p.setNumInfo(0);
                  return success;
               }
            }
      }
      return success;
   }

   public String showItems()
   {
      String ans = "[";
      if(items != null)
         for(int i=0; i<items.size(); i++)
         {
            Item item = items.get(i);
            ans+=item.toString();
            if(i < items.size()-1)
               ans+=" ";
         }
      return ans + "]";
   }

   public Weapon getWeapon()
   {
      return weapon;
   }

   public Armor getArmor()
   {
      return armor;
   }

   public void setWeapon(Weapon w)
   {
      weapon = w;
   }

   public void setArmor(Armor a)
   {
      if(a==null)
         armor=(new Armor("none", 0, null, 0));
      else
         armor = a;
   }

   public NPCPlayer clone()
   {
      NPCPlayer ans = new NPCPlayer(this.getName(), this.charIndex, this.row, this.col, this.mapIndex, this.homeRow, this.homeCol, this.location);
      ans.setMoveType(this.getMoveType());
      ans.setRestoreDay(this.getRestoreDay());
      return ans;
   }
   
   public static void copyStatsTo(NPCPlayer from, NPCPlayer to)
   {
      to.setName(from.getName());
      to.setMind(from.getMind());
      to.setMight(from.getMight());
      to.setAgility(from.getAgility());
      to.setReputation(from.getReputation());
      to.setBody(from.getBody());
      to.setWeapon(from.getWeapon());
      to.setArmor(from.getArmor());
      to.setGold(from.getGold());
      to.setItems(from.getItems());
   }

   public int getNumInfo()
   {
      return numInfo;
   }

   public void setNumInfo(int ni)
   {
      numInfo = ni;
      if(numInfo == -999)     //statue
      {
         this.setBody(Utilities.rollDie(100,500));
         this.setMight(0);
         this.setMind(0);
         this.setAgility(0);
         this.setGold(0);
         this.setMoveType(NPC.STILL);
         this.setWeapon(Weapon.getNone());
         this.setArmor(Armor.getNone());
         this.setName("Stone "+NPC.characterDescription(this.getCharIndex()));
         this.clearItemsExceptBounties();
      }
   }  

   public boolean isSellingHouse()
   {
      byte[][]currMap = (CultimaPanel.map.get(this.getMapIndex()));   
      String current = "";
      if(this.getRow() >=0 && this.getCol() >= 0 && this.getRow() < currMap.length && this.getCol() < currMap[0].length)
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[this.getRow()][this.getCol()])).getName().toLowerCase();
      return (TerrainBuilder.isCity(this.getLocationType()) && current.contains("purpledirty"));
   }

   public boolean canFly()
   {
      return flyer;
   }

   public boolean isFireResistant()
   {
      return fireResistant;
   }

   public boolean canSwim()
   {
      return swimmer;
   }
   
   public void setCanSwim(boolean state)
   {
      swimmer = state;
   }
   
   public boolean canWalkAndSwim()
   {
      return walkAndSwim;
   }
   
   public void setCanWalkAndSwim(boolean state)
   {
      walkAndSwim = state;
   }

   public boolean isTalking()
   {
      return talking;
   }   

   public void setTalking(boolean t)
   {
      talking = t;
   }

   public boolean hasMet()
   {
      return hasMet;
   }   

   public void setHasMet(boolean t)
   {
      hasMet = t;
   }


   public byte getCharIndex()
   {
      return charIndex;
   }

   public void setCharIndex(byte index)
   {
      if(index == -111)    //-111 is the flag that this monster is a mimic of the player
         charIndex = NPC.BRIGAND_FIST;
      else
         charIndex = index;
      String[][] pictures = NPC.characters[charIndex];
   
      if(index == -111) //mimic
      {
         String [] mimicPics = Player.characters[CultimaPanel.player.getImageIndex()][CultimaPanel.player.RIGHT_IMAGE];
         pictures = new String[5][1];
         String [] allButLast = new String[mimicPics.length-1];
         String [] deadImage = new String[1];
         for(int i=0; i<allButLast.length; i++)   //copy over all images except the last one which is the death image
            allButLast[i] = mimicPics[i];
         deadImage[0] = mimicPics[mimicPics.length-1];
         for(int i=0; i<4; i++)
            pictures[i] = allButLast;
         pictures[4] = deadImage;
      }
      ArrayList<String>[] images = new ArrayList[5];
      for(int dir=0; dir<images.length; dir++)
      {
         images[dir] = new ArrayList();
         for(int i=0; i<pictures[dir].length; i++)
         {
            String p=pictures[dir][i];
            images[dir].add(p);
         }
      }
      dead = new ImageIcon(pictures[4][0]);
      super.setPictures(images); 
   }

   public String getCharDescription()
   {
      if(NPC.isCivilian(getCharIndex()))
         return NPC.characterDescription(charIndex);
      return getName();
   }

   public boolean isShopkeep()
   {
      String name = this.getName().toLowerCase();
      if(name.contains("ironsmith") || name.contains("mage")
      || name.contains("butcher") || name.contains("baker") || name.contains("barkeep") || charIndex == NPC.TRADER)
         return true;
      return false;
   }
   
   //returns true if this is a non-traveling shopkeep
   public boolean isStaticShopkeep()
   {
      String name = this.getName().toLowerCase();
      if(name.contains("ironsmith") || name.contains("mage")
      || name.contains("butcher") || name.contains("baker") || name.contains("barkeep"))
         return true;
      return false;
   }


   public boolean isPossessed()
   {
      return (this.hasEffect("control") && this.getNumInfo()!=999);
   }
   
   public boolean isTamed()    
   {
      return (this.hasEffect("control") && this.getNumInfo()==999);
   }

   public String getMissionInfo()
   {
      return missionInfo;
   }

   public void setMissionInfo(String mi)
   {
      missionInfo = mi;
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
   
   public void moveNorth()
   {
      int r = this.getRow()-1;
      if(this.getMapIndex()==0)
         r = CultimaPanel.equalizeRow(r);
      this.setRow(r);
      this.setDirection(AnimatedEntity.UP);
   }
   
   public void moveSouth()
   {
      int r = this.getRow()+1;
      if(this.getMapIndex()==0)
         r = CultimaPanel.equalizeRow(r);
      this.setRow(r);
      this.setDirection(AnimatedEntity.DOWN);
   }
   
   public void moveEast()
   {
      int c = this.getCol()+1;
      if(this.getMapIndex()==0)
         c = CultimaPanel.equalizeCol(c);
      this.setCol(c);
      this.setDirection(AnimatedEntity.RIGHT);
   }
   
   public void moveWest()
   {
      int c = this.getCol()-1;
      if(this.getMapIndex()==0)
         c = CultimaPanel.equalizeCol(c);
      this.setCol(c);
      this.setDirection(AnimatedEntity.LEFT);
   }

   public int getHomeRow()
   {
      return homeRow;
   }

   public int getHomeCol()
   {
      return homeCol;
   }

   public void setHomeRow(int r)
   {
      homeRow = r;
   }

   public void setHomeCol(int c)
   {
      homeCol = c;
   }


   public int getMapIndex()
   {
      return mapIndex;
   }

   public void setMapIndex(int mi)
   {
      mapIndex = mi;
   }

   public int getAgility()
   {
      String current = "";
   
      if(hasEffect("control") || this.getCharIndex()==NPC.DEATH || this.isOurDog() || this.isEscortedNpc())
      {
         int retVal = Math.max((agility + statMod[2]), (int)(CultimaPanel.player.getAgility()*1.2));
         return retVal;
      }
      if(this.getMapIndex()>=0 && this.getMapIndex()<CultimaPanel.map.size())
      {
         byte[][]currMap = (CultimaPanel.map.get(this.getMapIndex()));  
         if(this.getRow()>=0 && this.getCol()>=0 && this.getRow()<currMap.length && this.getCol()<currMap[0].length) 
            current = CultimaPanel.allTerrain.get(Math.abs(currMap[this.getRow()][this.getCol()])).getName().toLowerCase();
      }
      if((this.getMoveType()==NPC.STILL && this.getCharIndex()!=NPC.KING && this.getCharIndex()!=NPC.TREE && !NPC.isGuard(this.getCharIndex())) || this.hasEffect("freeze") || current.contains("bed"))   
      {  
         if(this.hasBeenAttacked())             //frozen targets are easier to hit
            return (agility + statMod[2])/4;
         return 1;                              //not suspecting an attack - not good at defending one
      }
      return agility + statMod[2];
   }

   public int getAgilityRaw()
   {
      return agility;
   }

   public void setAgility(int a)
   {
      agility = a;
   }

   public int getMightRaw()
   {
      return might;
   }

   public int getMight()
   {
      return might + statMod[0];
   }

   public void setMight(int m)
   {
      might = m;
   }

   public int getMindRaw()
   {
      return mind;
   }

   public int getMind()
   {
      return mind + statMod[1];
   }

   public void setMind(int m)
   {
      mind = m;
   }

   public int getBody()
   {
      return body;
   }

   public int maxHealth()
   {
      return might*5;
   }

   public void heal(int b)
   {
      body += b;
      if(body >= maxHealth())
         body = maxHealth();
   }

   public void setBody(int b)
   {
      body = b;
      if(this.isOurDog())
         CultimaPanel.player.setDogBasicInfo(this.basicInfo());
      else if(this.isEscortedNpc())
         CultimaPanel.player.setNpcBasicInfo(this.basicInfo());
   }

   public void damage(int d, String type)
   {
      if(this.getCharIndex()==NPC.TORNADO || this.getCharIndex()==NPC.WHIRLPOOL || this.getCharIndex()==NPC.SPITFIRE)
         return;                             //tornados and whirlpools take no damage.  They...can...not...be...stopped...
      if(type.contains("trap") && body==1)   //trap weakens target but does not kill
         return;
      if(!this.hasEffect("poison") && this.isTalking() && CultimaPanel.selected != null)
      {                                      //if the NPC is talking and take damage, the conversation is flat over
         this.setTalking(false);
         CultimaPanel.talkSel = false;
         CultimaPanel.selected = null;       //de-select the person we are chatting with
      }         
      body -= d;
      if(type.contains("trap") && body<=0)   //trap weakens target but does not kill
         body = 1; 
      if(body <= 0)
      {
         if(this.getMapIndex()==CultimaPanel.player.getMapIndex() && ((this.getCharIndex() == NPC.GIANT || this.getCharIndex() == NPC.FIRE || NPC.isEtheral(this.getCharIndex())) && (this.getNpcX()!=-1 && this.getNpcY()!=-1 )))
         {
            int radius = Utilities.rollDie(1,5);
            int opacity = Utilities.rollDie(50,80);  
            CultimaPanel.smoke.add(new SmokePuff(this.getNpcX()-(radius/2), this.getNpcY()-(radius/2), radius, opacity, SmokePuff.dustCloud));
            radius = Utilities.rollDie(1,5);
            opacity = Utilities.rollDie(50,80);  
            CultimaPanel.smoke.add(new SmokePuff(this.getNpcX()-(radius/2), this.getNpcY()-(radius/2), radius, opacity, SmokePuff.dustCloud));
            if(this.getCharIndex() == NPC.GIANT)
            {
               Sound.rumble();
            }
         }
         double size = NPC.getSize(getCharIndex());
         body = 0;
         if(charIndex==NPC.FIRE)
            Sound.openDoor();
         else
            Sound.NPCdie(size);
         if((charIndex==NPC.FIRE || charIndex==NPC.MAGMA) && type.equals("world"))
         {}    //player doesn't get experience if fire or magma monster die due to rain
         else
            CultimaPanel.player.addExperience(this.getLevel());
         if(type.contains("stamp"))
            CultimaPanel.sendMessage("Flame stammed out!");   
         this.clearMissionInfo();
      }
      else if(charIndex!=NPC.FIRE)
      {
         if(type.contains("vamp")) //if vampire attack
            Sound.vampAttack(d,this.getBody());
         else if(type.length() > 0 && this.getBody() < d*3)      //damage from effect like poison or fire
            Sound.NPCdamage(d, d*3);                        //effect damage sound gets too loud when the NPC is close to death
         else                                               //so limit the damage/body ratio to cap the sound made
            Sound.NPCdamage(d, this.getBody());
      }
      if(this.isOurDog())
         CultimaPanel.player.setDogBasicInfo(this.basicInfo());
      else if(this.isEscortedNpc())
         CultimaPanel.player.setNpcBasicInfo(this.basicInfo());   
   }

    //post: see if this player had a mission, and if so, remove it from the mission stack
   public void clearMissionInfo()
   {
      String NPCmiss = this.getMissionInfo();
      if(!NPCmiss.equals("none"))
      {
         for(int i=0; i<CultimaPanel.missionStack.size(); i++)
         {
            Mission m = CultimaPanel.missionStack.get(i);
            if(NPCmiss.equals(m.getInfo()))
            {
               CultimaPanel.missionStack.remove(i);
               FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
               break;
            }
         }
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
   }

   public void pay(int g)
   {
      gold -= g;
   }

   public int getReputation()
   {
      return reputation;
   }

   public void setReputation(int r)
   {
      reputation = r;
   }

   public String getReputationName()
   {
      int repIndex = 3;
      if(reputation >= 1000)
         repIndex = 6;
      else if(reputation >= 100)
         repIndex = 5;
      else if(reputation >= 10)   
         repIndex = 4;
      else if(reputation >= -10)
         repIndex = 3;
      else if(reputation >= -100)
         repIndex = 2;
      else if(reputation >= -1000)
         repIndex = 1;
      else
         repIndex = 0;
   
      return reputationNames[repIndex];
   }

   public String getHomeTown()
   {
      return homeTown;
   }

   public String getLocationType()
   {
      return location;
   }

   public void setLocationType(String lt)
   {
      lt = lt.toLowerCase();
      if(lt.contains("port"))
         lt = "port"; 
      else if(lt.contains("city") || lt.contains("fortress"))
         lt = "city";
      else if(lt.contains("village"))
         lt = "village";
      else if(lt.contains("domicile"))
         lt = "domicile";  
      else if(lt.contains("castle") || lt.contains("tower"))
         lt = "castle";
      else if(lt.contains("temple"))
         lt = "temple";
      else if(lt.contains("cave"))
         lt = "cave"; 
      else if(lt.contains("dungeon"))
         lt = "dungeon";  
      else if(lt.contains("mine"))
         lt = "mine";
      else if(lt.contains("lair"))
         lt = "lair";
      else if(lt.contains("battlefield"))
         lt = "battlefield";
      else if(lt.contains("ship"))
         lt = "ship";
      else if(lt.contains("arena"))
         lt = "arena";   
      else if(lt.contains("camp"))
         lt = "camp";
      else if(lt.contains("sewer"))
         lt = "sewer";
      else if(lt.contains("pacrealm"))
         lt = "pacrealm";
      else lt = "world";
      location = lt;
   }

   public void setRestoreDay(int rd)
   {
      restoreDay = rd;
   }

   public int getRestoreDay()
   {
      return restoreDay;
   }

   public void setEndChaseDay(int rd)
   {
      endChaseDay = rd;
   }

   public int getEndChaseDay()
   {
      return endChaseDay;
   }

   public byte getMoveType()
   {
      return moveType;
   }

   public byte getOrigMoveType()
   {
      return origMoveType;
   }

   public void setMoveType(byte mt)
   {
      if(this.getCharIndex()==NPC.SPITFIRE && mt!=NPC.MARCH_EAST && mt!=NPC.MARCH_WEST)
         return;
      moveType = mt;
      origMoveType = mt;
   }

   public void setMoveTypeTemp(byte mt)
   {
      if(this.getCharIndex()==NPC.SPITFIRE && mt!=NPC.MARCH_EAST && mt!=NPC.MARCH_WEST)
         return;
      moveType = mt;
   }

   public void restoreLoc()
   {
      if(endChaseDay==-1 || endChaseDay < CultimaPanel.days)
         moveType = origMoveType;
      row = homeRow;
      col = homeCol;
   }

   public int getLevel()
   {
      return (might+mind+agility)/3;
   }

   public ImageIcon getDeadPicture()
   {
      return dead;
   }

   public Corpse getCorpse()
   { 
      Weapon weap = null;
      int arrowsInQuill = 0;
      if(weapon != null && weapon.getImageIndex() != Player.NONE && weapon.getImageIndex() != Player.STAFF)
      {
         weap = weapon; 
         if(weap.getName().toLowerCase().contains("bow") || weap.getName().toLowerCase().contains("caster"))
            arrowsInQuill = Utilities.rollDie(1, 25);
      }
      Armor arm = null;
      if(armor!=null && armor.getValue() > 0)
         arm = armor;  
      ArrayList<Item> items = this.getItems();  
      if(this.isStatue())   //this is a statue, use MONOLITH rubble as corpse
      {
         NPCPlayer tempModel = new NPCPlayer(NPC.MONOLITH, 0, 0, 0, "");
         return new Corpse(tempModel.getDeadPicture(), tempModel.getCharIndex(), this.getRow(), this.getCol(), this.getMapIndex(), this.getGold(), null, null, this.getItems(), CultimaPanel.days + 30, NPC.getSize(this.getCharIndex()));
      } 
      else if(this.getCharIndex()==NPC.GRABOID && Math.random()<0.66)   //graboid will have a random weapon, armor or item in its corpse
      {
         int kind = Utilities.rollDie(1,3);
         if(kind == 1)
            weap = Weapon.getRandomWeaponFull();
         else if(kind == 2)
            arm = Armor.getRandomArmorFull();
         else 
            items.add(Item.getRandomItemFull());   
      }   
      int decomposeTime = CultimaPanel.days + 3;
      if(this.getLocationType().contains("arena"))
         decomposeTime = CultimaPanel.days + 1;
      if(this.canSwim())      //corpses in water decompose faster or sink
         decomposeTime = CultimaPanel.days + 1;  
      double size = NPC.getSize(this.getCharIndex());
      if(this.getCharIndex()==NPC.RUSTCREATURE && this.getNumInfo()>=1 && this.getNumInfo()<=3)
         size = this.getNumInfo(); 
      else if(this.getCharIndex()==NPC.BRIGANDRIDER || this.getCharIndex()==NPC.WEREWOLF)  
         size = 1;             
      Corpse temp = new Corpse(this.getDeadPicture(), this.getCharIndex(), this.getRow(), this.getCol(), this.getMapIndex(), this.getGold(), weap, arm, items, decomposeTime, size);
      temp.setNumArrows(this.getNumArrows() + arrowsInQuill);
      return temp;
   }

   public Coord getTargetLoc()
   {
      return targetLoc;
   }

   public String toString()
   {
      String ans = "";
      ans += (getName()+",");
      ans += (getCharIndex()+",");
      ans += (getMapIndex()+",");
      ans += (getRow()+",");
      ans += (getCol()+","); 
      ans += (getHomeRow()+",");
      ans += (getHomeCol()+","); 
      ans += (getMissionInfo()+",");
      ans += (getMightRaw()+ ",");
      ans += (getMindRaw()+",");
      ans += (getAgilityRaw()+",");
      ans += (getBody()+",");
      ans += (getReputation()+",");
      ans += (hasMet()+",");
      ans += (getRestoreDay()+",");
      ans += (getEndChaseDay()+",");
      ans += (getOrigMoveType()+",");
      ans += (getLocationType()+",");
      ans += (getNumInfo()+",");
      if(getWeapon()==null)
         ans += ("none,");
      else
         ans += (getWeapon().getName()+",");
      if(getArmor()==null)
         ans += ("none,");
      else  
         ans += (getArmor().getName()+",");
      ans += (showItems()+",");
      ans += (hasTrained()+",");
      ans += (getGold()+",");
      String effect = "none";
      if(effects!=null && effects.size() >= 1)
         effect = effects.get(0);
      ans += effect+",";
      ans += (getMaritalStatus());
      return ans;
   }
   
   public int hashCode()
   {
      return Math.abs(Utilities.shortName(this.getName()).hashCode());
   }

  //returns true if this is in range to attack player
   public boolean canAttackPlayer()
   {
      double dist = Display.findDistance(this.getRow(), this.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
      int weaponRange = this.getWeapon().getRange();
      return (this.getBody() > 1 && CultimaPanel.player.getBody() > 0 && dist <= weaponRange && (this.getRow()==CultimaPanel.player.getRow() || this.getCol()==CultimaPanel.player.getCol()) 
                        && !Utilities.blockedBetween(this.getRow(), this.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol()));
   }

  //attack the player
   public void attack()
   {
      boolean indoors = false;      //used to see if fire is less potent in the rain, or freeze more potent in the rain
      byte[][]currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());	
      String locType = location.toLowerCase();
      if(locType.contains("camp") && NPC.isMonsterKing(this.getCharIndex()) && Utilities.nextToIronBarDoor(currMap, row, col))
      {                             //make sure a trapped animal in a cell can't hit us when we are trying to free them
         return;
      }
      boolean fireProtection = (CultimaPanel.player.getArmor().getName().toLowerCase().contains("dragon") || CultimaPanel.player.getArmor().getName().toLowerCase().contains("holocaust-cloak") || CultimaPanel.player.getActiveSpell("Fireskin")!=null);
      boolean coldProtection = (CultimaPanel.player.getArmor().getName().toLowerCase().contains("seaserpent") || CultimaPanel.player.wearingFur());
      boolean hasShield = (CultimaPanel.player.getImageIndex()==Player.SWORDSHIELD && CultimaPanel.player.isOnGuard() && CultimaPanel.numFrames >= attackTime + CultimaPanel.player.getWeapon().getReloadFrames());
      boolean isOurDog = this.isOurDog();
      if(this.getCharIndex()==NPC.RABBIT || this.getCharIndex()==NPC.CHICKEN || this.getCharIndex()==NPC.PIG || this.isEscortedNpc())
         return;
      if(CultimaPanel.nextMonsterSpawn != -1 && this.getCharIndex()==CultimaPanel.nextMonsterSpawn)
         CultimaPanel.nextMonsterSpawn = -1;   
   //ship that has been boarded by player should not attack   
      if((this.getCharIndex()==NPC.GREATSHIP || this.getCharIndex()==NPC.BRIGANDSHIP))
      {
         if(this.hasMet())                //ships that player has commendeered should not attack
            return; 
         else
            Sound.fireCannon();           //sound
      } 
      else if(this.getWeapon().getName().toLowerCase().equals("gun"))
         Sound.fireGun(); 
      if(this.getCharIndex()==NPC.SORCERER && this.getNumInfo() > 0 && Math.random() < 0.5)   //maybe summon undead
      {
         byte monsterType = NPC.randomSummonUndead();
         boolean success=Utilities.summonMonster(monsterType, this.getRow(), this.getCol(), 3, this);
         if(success)
         {
            CultimaPanel.flashColor = Color.red;
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
            CultimaPanel.sendMessage(NPC.characterDescription(monsterType)+" summoned!");
            Sound.summonUndead();
            this.setNumInfo(this.getNumInfo()-1);
            return;
         }
      }
      
      targetLoc = new Coord(CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
      if(CultimaPanel.player.onHorse())   //dismount horse when attacked
         CultimaPanel.player.dismountHorse();   
         
      int distance = (int)(Display.findDistance(this.getRow(), this.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol()));
      Weapon weapon = this.getWeapon();
      
      //dragons switch to melee attack when closer
      if(this.getCharIndex()==NPC.DRAGONKING && distance==1)
         weapon = Weapon.getCrushingJaws();
      if(this.getCharIndex()==NPC.DRAGON && distance==1)
         weapon = Weapon.getMassiveFists();   
      
      String playerTerrain = CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.player.getRow()][CultimaPanel.player.getCol()])).getName().toLowerCase();
      String targetTerrain = CultimaPanel.allTerrain.get(Math.abs(currMap[this.getRow()][this.getCol()])).getName().toLowerCase();
      boolean playerInForest = (playerTerrain.contains("forest") && !playerTerrain.contains("dead"));
      int heightBonus = 1;
      if((playerTerrain.contains("hill") || playerTerrain.contains("mountain")) && !targetTerrain.contains("hill") && !targetTerrain.contains("mountain"))
         heightBonus = 2;        //higher chance of hitting our target if we are higher up
   
      int dam = 0;
      int playerCover = 0;       //the amount of cover the player can hide behind
      if(playerInForest)
      {
         if(playerTerrain.contains("dense"))
         {
            playerCover = Utilities.rollDie(5,8);
         }
         else
         {
            playerCover = Utilities.rollDie(3, 5);
         }
      }
      if(hasShield && (weapon.getName().toLowerCase().contains("bow") || weapon.getName().toLowerCase().contains("caster")))
      {                          //sword and shield/buckler gets extra defense against arrows
         playerCover += Utilities.rollDie(5,8);
      }
      //make hitDie based on difference between player and monster level
      int hitDie = Utilities.rollDie(this.getLevel()+CultimaPanel.player.getLevel());
      
      //record if the player being hit was caught close range with a melee weapon and the player has a bow
      boolean meleeAgainstBow = ((CultimaPanel.player.getWeapon().getName().toLowerCase().contains("bow") || CultimaPanel.player.getWeapon().getName().toLowerCase().contains("caster")) && weapon.getRange()<=3);
      boolean closeToMassiveBeastWithRanged = ((NPC.getSize(this.getCharIndex())>1.5 || this.getCharIndex()==NPC.DRAGON) && CultimaPanel.player.getWeapon().getRange()>=2 && distance <= 1);
      if(closeToMassiveBeastWithRanged)
         hitDie = Utilities.rollDie(this.getMight() + (int)(CultimaPanel.player.getMight()/4)); //penalty for being too close to a massive beast with a ranged weapon
      else if((weapon.getRange()==1 && NPC.getSize(this.getCharIndex())<1.5) || (weapon.getRange()<=2 && NPC.getSize(this.getCharIndex())<=1.5) ||  (weapon.getRange() <= 3 && NPC.getSize(this.getCharIndex())>1.5))
      {  //melee weapon: make the hitDie based on the difference between player and monster might
         if(CultimaPanel.player.getWeapon().getRange()==1)
            hitDie = Utilities.rollDie(this.getMight() + CultimaPanel.player.getMight());
         else
            hitDie = Utilities.rollDie(this.getMight() + (int)(CultimaPanel.player.getMight()/1.5)); //penalty for trying to block a melee weapon with a non-melee weapon
      }
      else if(weapon.getRange() > 3)
      {  //ranged weapon: make hitDie based on difference between player and monster agility
         hitDie = Utilities.rollDie(this.getAgility()+CultimaPanel.player.getAgility()+playerCover);
         int enemyClose = 0;
         if(distance <= 1)
            enemyClose = 10;
         int weather = CultimaPanel.weather; 
         if(Display.isWinter())  //no penalty in the snow
            weather = 0;
         if(weapon.getName().toLowerCase().contains("bow") || weapon.getName().toLowerCase().contains("caster"))    //less bow accuracy if fired in the rain
            hitDie = Utilities.rollDie(this.getAgility()+CultimaPanel.player.getAgility()+weather+enemyClose+playerCover);
      }
      double bonusTemp = 1;
      if(heightBonus > 1)
         bonusTemp += 0.5;
      if(meleeAgainstBow)
         bonusTemp += 0.5;
      if(this.getCharIndex()==NPC.RUSTCREATURE || this.getCharIndex()==NPC.SLIME)
         bonusTemp += 1;  
            
      if((int)(this.getLevel()*bonusTemp) >= hitDie || CultimaPanel.player.isCamping() || !CultimaPanel.player.isOnGuard() || this.getCharIndex()==NPC.MONOLITH || this.getLocationType().contains("pacrealm"))
      {                    //they hit player
         if((this.getCharIndex()==NPC.SPIDER || this.getCharIndex()==NPC.SPIDERKING) && !CultimaPanel.player.hasEffect("web"))
         {
            if(!CultimaPanel.player.isSailing() && !CultimaPanel.player.onHorse() && !CultimaPanel.player.isFlying())
            {
               if((this.getCharIndex()==NPC.SPIDER && Math.random() < 0.5) || this.getCharIndex()==NPC.SPIDERKING)
               {
                  CultimaPanel.player.addEffectForce("web");
                  dam = 0;
               }
            }
         }
         dam = Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage()) + this.getLevel();
         if(weapon.getEffect().contains("freeze") &&  CultimaPanel.weather > 0 && !indoors && !CultimaPanel.player.hasItem("Seaserpent-scale"))     //freeze effect does more damage when it is raining
            dam = Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage()) + this.getLevel() + CultimaPanel.weather; 
         else if(weapon.getEffect().contains("fire") &&  CultimaPanel.weather > 0 && !indoors)     //fire effect does less damage when it is raining
            dam = Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage()) + this.getLevel() - CultimaPanel.weather; 
         if(closeToMassiveBeastWithRanged)
            dam *= 4;
         if(meleeAgainstBow)
            dam *= 2;   
         if(!weapon.getEffect().equals("none"))
         {
            if(weapon.getEffect().contains("fire"))
            {   
               int flamableBonus = 0;
               boolean playerFlamable = CultimaPanel.player.getArmor().isFlamable();
               if(playerFlamable)
                  flamableBonus = 4;
               if(CultimaPanel.weather > 0 && !indoors && Utilities.rollDie(1,(6+flamableBonus)) <= CultimaPanel.weather)
                  CultimaPanel.sendMessage("Fire extinguished!");
               else 
               {
                  if(this.getCharIndex()!=NPC.FIRE || (Utilities.rollDie(10) < flamableBonus))
                     CultimaPanel.player.addEffect(weapon.getEffect());
               }
            }
            else
            {
               if(weapon.getEffect().contains("curse") || weapon.getEffect().contains("control"))
               {
                  int effectDie = Utilities.rollDie((this.getLevel() + CultimaPanel.player.getLevel())*2);
                  if(this.getLevel() > effectDie)
                  {
                     CultimaPanel.player.addEffect("curse");
                     //since an NPC can't possess the player, curse and poison them
                     if(weapon.getEffect().contains("control"))
                        CultimaPanel.player.addEffect("poison");
                  }
               }
               else 
                  CultimaPanel.player.addEffect(weapon.getEffect());
            }
         }
      }
      if(weapon.getName().toLowerCase().contains("lightning"))
      {
         CultimaPanel.flashColor = Color.white;
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
         if(this.getCharIndex()==NPC.DRAGONKING && distance <= 1)
            dam *= 2;
         else if(CultimaPanel.player.getWeapon().getName().toLowerCase().contains("mirror") || Weapon.isLegendaryWeapon(CultimaPanel.player.getWeapon().getName()))
         {
            if((CultimaPanel.player.isOnGuard() && Math.random()<0.75) || (!CultimaPanel.player.isOnGuard() && Math.random()<0.25))
            {
               double damage = dam;
               if(CultimaPanel.player.isOnGuard())
                  damage *= (Utilities.rollDie(50,100)/100.0);
               else
                  damage *=  (Utilities.rollDie(25,75)/100.0);
               Sound.trapSprung();
               this.damage((int)(damage),""); 
               this.setHitTime(CultimaPanel.numFrames + CultimaPanel.animDelay*3/2);
               CultimaPanel.sendMessage("Attack reflected!");                    
               return;
            }
         }
         if(CultimaPanel.player.getArmor().isMetal())
            dam = (int)(dam*1.5);   //lightning does more damage if we are wearing metal armor
      }
      else if(weapon.getName().toLowerCase().contains("fire"))
      {
         CultimaPanel.flashColor = Color.orange;
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 2;
      }
      if(this.getCharIndex()==NPC.FIRE)
      {
         double burnProb = 0.5;
         if(fireProtection)
            burnProb = 0.9;
         dam = 1;
         if(Math.random() < burnProb)
            dam = 0;
      }
      else if(this.getCharIndex()==NPC.DEATH && CultimaPanel.player.hasItem("blessed-crown"))
      {
         dam /= 3;
      }
      if(dam > 0)
      {
         boolean found = false;
         if(this.getCharIndex()==NPC.RUSTCREATURE)
         {//destory a metal weapon, armor, or item
            if(CultimaPanel.player.getWeapon().isMetal())
            {
               String weaponName = CultimaPanel.player.getWeapon().getName();
               CultimaPanel.player.discardCurrentWeapon();
               CultimaPanel.sendMessage("Thy "+weaponName+" has been consumed!");
               found = true;
            }
            else if(CultimaPanel.player.getArmor().isMetal())
            {
               String armorName = CultimaPanel.player.getArmor().getName();
               CultimaPanel.player.discardCurrentArmor();
               CultimaPanel.sendMessage("Thy "+armorName+" has been consumed!");
               found = true;
            }
            else
            {  //pick an item in their inventory or gold to be eaten
               ArrayList<Weapon>[] pWeapons = CultimaPanel.player.getWeapons();
               for(ArrayList<Weapon> weapSet: pWeapons)
               {
                  for(Weapon pWeapon: weapSet)
                  {
                     if(pWeapon.isMetal())
                     {
                        CultimaPanel.player.discardWeapon(pWeapon.getName());
                        CultimaPanel.sendMessage("A "+pWeapon.getName()+" has been consumed!");
                        found = true;
                        break;
                     }
                  }
                  if(found)
                     break;
               }
               if(!found)
               {
                  ArrayList<Armor> pArmor = CultimaPanel.player.getAllArmor();
                  for(Armor pArm: pArmor)
                  {
                     if(pArm.isMetal())
                     {
                        CultimaPanel.player.discardArmor(pArm.getName());
                        CultimaPanel.sendMessage("A "+pArm.getName()+" has been consumed!");
                        found = true;
                        break;
                     }
                  }
                  if(!found)
                  {
                     if(gold > 0)
                     {
                        int goldEaten = Utilities.rollDie(CultimaPanel.player.getGold());
                        CultimaPanel.player.pay(goldEaten);
                        CultimaPanel.sendMessage(goldEaten+" gold has been consumed!");
                        found = true;
                     }
                     else if(CultimaPanel.player.getItems().size() > 0)
                     {
                        ArrayList<String> pItems = CultimaPanel.player.getItems();
                        for(String pItem: pItems)
                        {
                           if(Item.isMetal(pItem.trim()))
                           {
                              CultimaPanel.player.removeItem(pItem);
                              CultimaPanel.sendMessage("Thy "+pItem+" has been consumed!");
                              found = true;
                              break;
                           }
                        }
                     }
                  }
               }
            }
            if(!found)  //rustcreature found no items to eat, and goes back to roaming
               this.setMoveType(NPC.ROAM);
            else
               Sound.itemEaten();   
            dam = 0;
         }
         if(this.getLocationType().contains("arena"))
            Sound.applauseSuprise(Utilities.rollDie(50,75));
         if(this.getCharIndex()==NPC.BATKING && (CultimaPanel.time <8 || CultimaPanel.time > 16) && !CultimaPanel.player.isVampire())
         {  //change player into a Vampire
            Utilities.changeToVampire();
            this.setMoveType(NPC.RUN);
            dam = 0;
         }
         if(NPC.isVampire(this.getCharIndex()) && (CultimaPanel.time <8 || CultimaPanel.time > 16) && !CultimaPanel.player.isVampire() && CultimaPanel.player.getBody() < 50 && Math.random() < 0.5)
         {  //change player into a Vampire
            Utilities.changeToVampire();
            this.setMoveType(NPC.RUN);
            dam = 0;
         }
         if((this.getCharIndex()==NPC.WOLF || this.getCharIndex()==NPC.WOLFKING) && !CultimaPanel.player.isWerewolf() && CultimaPanel.player.getArmor().getName().toLowerCase().contains("wolf"))
         {  //change player into a Werewolf
            Utilities.changeToWerewolf();
            this.setMoveType(NPC.RUN);
            dam = 0;
         }
         if((this.getCharIndex()==NPC.WEREWOLF) && !CultimaPanel.player.isWerewolf() && CultimaPanel.player.getBody() < 50 && Math.random() < 0.5)
         {  //change player into a Werewolf
            Utilities.changeToWerewolf();
            this.setMoveType(NPC.RUN);
            dam = 0;
         }
         if(this.getCharIndex()==NPC.ROYALGUARD && CultimaPanel.player.isWerewolf())
         {
            dam *= 4;
            CultimaPanel.sendMessage("Struck with silver-forged arms!");
         }
         if(weapon.getName().toLowerCase().contains("vampyric"))
         {
            this.heal(dam/3);
            CultimaPanel.flashColor = Color.red.darker();
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
            CultimaPanel.sendMessage("Life consumed!");
         }
         if(CultimaPanel.player.getArmorPoints()>0)      //saving throw for armor
         { 
            if(weapon.getName().toLowerCase().contains("lightning") && CultimaPanel.player.getArmor().isMetal())
            {}       //no saving throw for metal armor against lightning attacks
            else
            {
               int numSides = CultimaPanel.player.getArmorPoints()*10;
               int roll = Utilities.rollDie(numSides);
               double save = Math.max(0, (1 - (roll/100.0)));
               if(this.getCharIndex()!=NPC.FIRE)
                  dam = (int)(dam * save);
            }
         }  
         if((weapon.getName().toLowerCase().contains("fire")) && fireProtection)
         {
            if(this.getCharIndex()==NPC.DRAGON && distance <= 1)
               dam *= 2;
            else
            {
            //extra saving thow with dragon-scale armor against dragon attack
               double save = 1 - (Utilities.rollDie(25,100)/100.0);
               if(CultimaPanel.player.getActiveSpell("Fireskin")!=null)
                  save = 1 - (Utilities.rollDie(50,100)/100.0);
               dam = (int)(dam * save);
            }
         }
         if((weapon.getName().toLowerCase().contains("ice")) && coldProtection)
         {
            if(this.getCharIndex()==NPC.SEAMONSTER && distance <= 1)
               dam *= 2;
            else
            {
            //extra saving thow with seaserpent-scale armor against seamonster attack
               double save = 1 - (Utilities.rollDie(25,100)/100.0);
               dam = (int)(dam * save);
            }
         }
         if((this.getCharIndex() == NPC.JESTER  && !this.isZombie()) || (this.getCharIndex() == NPC.GOBLIN && (Math.random() < 0.25 || (!this.hasBeenAttacked() && Math.random()<0.5))))
         {
            this.setAttackMissTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
            if((CultimaPanel.player.getGold()==0 && CultimaPanel.player.getItems().size() == 0) || (CultimaPanel.player.isOnGuard() && Math.random() < 0.75))
            {
               Sound.miss();        //sound
            }
            else
            {
               if(this.getCharIndex() == NPC.JESTER  && this.getReputationName().equals("Kanvish") && Math.random() < 0.5)
               {
                  Sound.slap();
                  CultimaPanel.sendMessage("-----");
                  CultimaPanel.sendMessage("~"+ NPC.randomInsult());
                  this.setMoveTypeTemp(NPC.RUN);
               }
               else
               {
                  if(CultimaPanel.player.getGold()==0 || Math.random() < 0.5) //steal an item
                  {
                     ArrayList<String> playerItems = CultimaPanel.player.getItems();
                     if(CultimaPanel.player.getItems().size() > 0)
                     {
                        Sound.trapSprung();
                        String stolenItem = Utilities.getRandomFrom(playerItems);
                        CultimaPanel.player.removeItemAbsolute(stolenItem);
                        int pos = stolenItem.indexOf(":");
                        if(pos>0)
                           stolenItem = stolenItem.substring(0,pos);
                        this.addItem(new Item(stolenItem, 1000));
                     } 
                  }
                  else //steal some gold
                  {
                     Sound.trapSprung();
                     int gold = Math.min(CultimaPanel.player.getGold(), Utilities.rollDie(1,20));
                     CultimaPanel.player.pay(gold);
                     this.addGold(gold);
                  }
                  CultimaPanel.sendMessage("An item has been stolen!"); 
                  this.setMoveTypeTemp(NPC.RUN);
                  if(this.getCharIndex() == NPC.GOBLIN)
                     this.setMoveType(NPC.RUN);
               }
            }
         }  
         else if(this.getCharIndex() == NPC.TAXMAN && CultimaPanel.days > this.getEndChaseDay() && !this.hasBeenAttacked() && this.getNumInfo()!=10 && !this.isZombie())
         {
            this.setAttackMissTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
            Sound.trapSprung();
            CultimaPanel.talkSel = true;
            CultimaPanel.selected = this;
            this.setTalking(true);
            int taxes = CultimaPanel.player.getTaxesOwed();
            CultimaPanel.sendMessage("~"+ NPC.getRandomFrom(NPC.taxesDue).replace("TAX_VALUE",""+taxes));
            CultimaPanel.sendMessage("-----");
         }
         else if(dam > 0)
         {
            CultimaPanel.player.damage(dam);
            this.setAttackHitTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
            if(this.getCharIndex()==NPC.FIRE)
               CultimaPanel.sendMessage("You have been burned for "+dam+" damage!"); 
            else
               CultimaPanel.sendMessage("You have been hit for "+dam+" damage!"); 
         }
      }
      else if(this.getCharIndex()!=NPC.FIRE)
      {
         this.setAttackMissTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
         if((hasShield && Math.random() < 0.75) || (weapon.getRange() == 1 && CultimaPanel.player.getMight() > CultimaPanel.player.getAgility()))
         {
            Sound.parried();
            CultimaPanel.sendMessage("You blocked!");
         }
         else
         {
            Sound.miss();        //sound
            CultimaPanel.sendMessage("You dodged!");
         }
         if(this.getLocationType().contains("arena"))
         {
            Sound.applauseSuprise(Utilities.rollDie(25,50));   
         }
      }
      if(this.getCharIndex()==NPC.DRAGON || this.getCharIndex()==NPC.DRAGONKING || this.getCharIndex()==NPC.TANK || this.getCharIndex()==NPC.GREATSHIP || this.getCharIndex()==NPC.BRIGANDSHIP)
      {  //start fire in adjacent spot where dragon attacks
         double fireProb = 0.5;
         int fSizeMin = 5;
         int fSizeMax = 25;
         if((this.getCharIndex()==NPC.DRAGON || this.getCharIndex()==NPC.DRAGONKING)&& weapon.getEffect().contains("fire"))
         {
            fireProb = 0.9;
            if(this.getCharIndex()==NPC.DRAGON)
            {
               fSizeMin = 25;
               fSizeMax = 50;
            }
            else
            {
               fSizeMin = 50;
               fSizeMax = 75;
            }
         }
         else if(this.getCharIndex()==NPC.TANK)
         {
            fireProb = 0.75;   
            fSizeMin = 75;
            fSizeMax = 100;
         }
         else if(dam > 0)     //we want more of a chance that a secondary fire starts if the NPC misses us
            fireProb /= 2;
         if(Math.random() < fireProb)
         {
            ArrayList<Coord>adjacentSpots = new ArrayList<Coord>();
            for(int r=CultimaPanel.player.getRow()-1; r<=CultimaPanel.player.getRow()+1; r++)
            {
               for(int c=CultimaPanel.player.getCol()-1; c<=CultimaPanel.player.getCol()+1; c++)
               {
                  if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
                     continue;
                  String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(!current.contains("water"))
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
               fire.setBody(Utilities.rollDie(fSizeMin,fSizeMax));
               CultimaPanel.worldMonsters.add(fire);     
            }
         }
      }
   }

   public boolean hasAttacked()
   {
      return hasAttacked;
   }
   
   public void setHasAttacked(boolean ha)
   {
      hasAttacked = ha;
   }
   
   //true if this is our trained dog
   public boolean isOurDog()
   {
      String thisName = this.getName();
      String ourDogName = CultimaPanel.player.getDogName();
      if(ourDogName.equals("none"))
      {
         return false;
      }
      String npcInfo = CultimaPanel.player.getDogBasicInfo();
      String[]parts = npcInfo.split(",");
      byte charIndex = (Byte.parseByte(parts[5].trim()));
      return (this.getCharIndex()==charIndex && thisName.contains(ourDogName));
   }
   
   //true if this is an npc that we are escorting as part of a mission
   public boolean isEscortedNpc()
   {
      String simpleName = this.getNameSimple();
      String npcName = makeSimpleName(CultimaPanel.player.getNpcName());
      if(npcName.equals("none"))
      {
         return false;
      }
      String npcInfo = CultimaPanel.player.getNpcBasicInfo();
      String[]parts = npcInfo.split(",");
      byte charIndex = (Byte.parseByte(parts[5].trim()));
      return (this.getCharIndex()==charIndex && simpleName.equals(npcName));
   }
   
   //if the npc we are escorting is lost or killed, remove the mission and player
   public void escorteeIsLost()
   {
      if(!this.isEscortedNpc())
      {
         return;
      }
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
      
         if(m.getType().equals("Escort"))
         {
            CultimaPanel.sendMessage(this.getNameSimple()+" is lost. Escort mission failed.");
            CultimaPanel.missionStack.remove(i);
            FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
            CultimaPanel.player.setNpcBasicInfo("none");
            break;
         }
      }
   }

//attack another NPCPlayer (maybe with a specific type of weapon if forceWeapon is not null)
   public void attack(NPCPlayer target)
   {
      if(target==null)
         return;
   //ship that has been boarded by player should not attack   
      if((this.getCharIndex()==NPC.GREATSHIP || this.getCharIndex()==NPC.BRIGANDSHIP) && this.hasMet())
         return; 
      if(this.hasEffect("stun") && Math.random() < 0.75)
         return;  
      if(this.getCharIndex()==NPC.MAN && this.getName().equals(CultimaPanel.player.getNpcName()))
         return;    
      boolean indoors = false;      //used to see if fire is less potent in the rain, or freeze more potent in the rain
      String locType = location.toLowerCase();
      targetLoc = new Coord(target.getRow(), target.getCol());
   
      Weapon weapon = this.getWeapon();
   
   //melee attack
      int hitDie = Utilities.rollDie(20);
      int dodgeDie = Utilities.rollDie(20);
      if(target.hasEffect("control"))
         dodgeDie = (int)(dodgeDie * 1.5);
      if(weapon.getName().toLowerCase().contains("bow") || weapon.getName().toLowerCase().contains("caster"))
         dodgeDie = Utilities.rollDie(20+CultimaPanel.weather);
      int dam = 0;
   
      if(hitDie+this.getLevel() > (dodgeDie + target.getLevel()) || this.getCharIndex()==NPC.MONOLITH || this.getLocationType().contains("pacrealm"))
      {                    //they hit npc
         dam = Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage()) + this.getLevel();
         if(weapon.getEffect().contains("freeze") &&  CultimaPanel.weather > 0 && !indoors)     //freeze effect does more damage when it is raining
            dam = Utilities.rollDie(weapon.getMinDamage(), weapon.getMaxDamage()) + this.getLevel() + CultimaPanel.weather;  
         if(!weapon.getEffect().equals("none"))
         {
            if(weapon.getEffect().contains("fire") && CultimaPanel.weather > 0 && !indoors)
            {   
               if(Utilities.rollDie(1,6) <= CultimaPanel.weather)
               {}
               else
                  target.addEffect(weapon.getEffect());
            }
            else
            {
               if(weapon.getEffect().contains("curse") || weapon.getEffect().contains("control"))
               {
                  int effectDie = Utilities.rollDie((this.getLevel() + target.getLevel())*2);
                  if(this.getLevel() > effectDie)
                     target.addEffect(weapon.getEffect());
               }
               else
                  target.addEffect(weapon.getEffect());
            }
         }
      }
      if(this.getCharIndex()==NPC.FIRE)
      {
         if(target.getCharIndex()==NPC.DEMON || target.getCharIndex()==NPC.DEMONKING ||
         target.getCharIndex()==NPC.DRAGON ||  target.getCharIndex()==NPC.DRAGONKING ||
         target.getCharIndex()==NPC.FIRE || target.getCharIndex()==NPC.MAGMA || target.getCharIndex()==NPC.DEATH)
            dam = 0;
      }
      if(dam > 0)
      {
         if(this.getCharIndex()==NPC.TORNADO)
            dam *= 4;
         if(target.hasEffect("control"))
            dam /= 4;
         if(this.hasEffect("control"))
            hasAttacked = true;   
         target.damage(dam, "");
         if(weapon.getName().toLowerCase().contains("vampyric"))
         {
            this.heal(dam/3);
            CultimaPanel.flashColor = Color.red.darker();
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
         }
         if(weapon.getName().toLowerCase().contains("bow") || weapon.getName().toLowerCase().contains("caster"))
         {
            if((weapon.getEffect().contains("freeze") || weapon.getEffect().contains("fire")) && Math.random() < 0.5)
            {}                //50% chance frozen and fire arrows can't be recovered
            else
               target.addArrow();
         }
         if(target.getBody() <= 0)                                   //target is killed
         {
            if(target.isEscortedNpc())
            {
               target.escorteeIsLost();
            }
            else
            {
               target.clearMissionInfo();
            }
            if(this.getCharIndex()==NPC.TORNADO && NPC.getSize(target.getCharIndex()) >= 1.5)
               Achievement.earnAchievement(Achievement.DOROTHY_GALES_REVENGE);
            if(this.isOurDog() && target.getCharIndex()==NPC.JESTER)
               Achievement.earnAchievement(Achievement.OH_GOOD_DOG);
         
            if(target.getName().equals(CultimaPanel.player.getDogName()))
            {
               CultimaPanel.player.setDogBasicInfo("none");
            }
            if(target.getName().equals(CultimaPanel.player.getNpcName()))
            {
               CultimaPanel.player.setNpcBasicInfo("none");
            }
         //remove target, add to restore, add Corpse
            if(NPC.isCivilian(target.getCharIndex()) && TerrainBuilder.habitablePlace(CultimaPanel.player.getLocationType()))
            {  //after a month, NPCs will repopulate a city
               NPCPlayer temp = target.clone();
               temp.setRestoreDay(CultimaPanel.days+30);
               CultimaPanel.NPCsToRestore.add(temp);
            }
            else if(this.getMapIndex() > 0 && !NPC.isCivilian(target.getCharIndex()))
            {  //remove that monster from the monster frequenices at that location
               Location currLoc = CultimaPanel.allLocations.get(this.getMapIndex());
               if(currLoc != null)
               {
                  if(location.contains("battlefield")) 
                  {
                  //the bow-assassin is on the human team in a battle, so don't decrease from the monster count if they are killed
                     if(!NPC.isCivilian(target.getCharIndex()) && target.getCharIndex() != NPC.BOWASSASSIN)
                     {
                        currLoc.removeMonster(target.getCharIndex());
                     }
                  }
                  else
                  {
                     currLoc.removeMonster(target.getCharIndex());
                  }
               }  
            }
            Corpse dead = target.getCorpse();
            if(target.getCharIndex()==NPC.BRIGANDRIDER)
            {  //kill the rider and leave the horse
               int newRow = target.getRow();
               int newCol = target.getCol();
               int newMapIndex = target.getMapIndex();
               String newLocType = target.getLocationType();
               NPCPlayer dhorse = new NPCPlayer(NPC.HORSE, newRow, newCol, newMapIndex, newLocType);
               if(this.getMapIndex()!=0 || Math.random() < 0.5)
               {
                  dhorse.setHasBeenAttacked(true);
               }
               else
               {
                  dhorse.setMoveTypeTemp(NPC.RUN);
               }
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
               CultimaPanel.corpses.get(this.getMapIndex()).add(dead);
               Utilities.removeNPCat(target.getRow(), target.getCol(), target.getMapIndex());
               this.setAttackHitTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/4));                    
            }
         }  
      }
      else if(!NPC.isNatural(this)) //don't do miss animation for FIRE, TORNADO or WHIRLPOOL
      {
         this.setAttackMissTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/4));
      }
   }
   
   public boolean equals(Object other)
   {
      NPCPlayer that = (NPCPlayer)(other);
      return (this.getCharIndex()==that.getCharIndex() && this.getMapIndex()==that.getMapIndex()
         && this.getRow()==that.getRow() && this.getCol()==that.getCol());
   }
   
   //compares by which NPC we should best draw our attention to
   //prioritizes by risk (monsters/enemies first)
   //then by importance - royalty, then mission status, then level
   public int compareTo(Object other)
   {
      NPCPlayer that = (NPCPlayer)(other);
      if(CultimaPanel.player.getLocationType().equals("battlefield"))
      {
         if(!NPC.isBattleFieldFriend(this) && NPC.isBattleFieldFriend(that))
            return 1;
         if(NPC.isBattleFieldFriend(this) && !NPC.isBattleFieldFriend(that))
            return -1;   
         return (this.getLevel() - that.getLevel());
      }
      if(NPC.isMonsterThreat(this) && !NPC.isMonsterThreat(that))
         return 1;
      if(!NPC.isMonsterThreat(this) && NPC.isMonsterThreat(that))
         return -1;
      if(NPC.isMonsterThreat(this) && NPC.isMonsterThreat(that))
         return (this.getLevel() - that.getLevel());
      if(this.getCharIndex()==NPC.KING && that.getCharIndex()!=NPC.KING)
         return 1;
      if(this.getCharIndex()!=NPC.KING && that.getCharIndex()==NPC.KING)
         return -1;  
      int thisMission = this.getMissionPriority();
      int thatMission = that.getMissionPriority();
      if(thisMission != thatMission)
         return (thisMission - thatMission);
      if(this.getMoveType()==NPC.CHASE && that.getMoveType()!=NPC.CHASE)
         return 1; 
      if(this.getMoveType()!=NPC.CHASE && that.getMoveType()==NPC.CHASE)
         return -1;     
      return (this.getLevel() - that.getLevel());
   }
   
   //returns the sum of the ASCII values of the character's names, used to define movement behaviors (like children that follow you if they have a mission)
   public int getNameNum()
   {
      int sum = 0;
      String name = this.getName();
      for(int i=0; i<name.length(); i++)
         sum += (int)(name.charAt(i));
      return sum;
   }
}