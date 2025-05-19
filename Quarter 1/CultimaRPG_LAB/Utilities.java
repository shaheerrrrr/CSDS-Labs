import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.awt.Color;

public class Utilities
{      
//components for giving game elements random names
   public static final String[] starts = {"B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "Th", "Sh", "St", "Ch"};
   public static final String[] consonants = {"b", "c", "d", "k", "l", "m", "n", "p", "r", "s", "t"};
   public static final String[] vowels = {"a", "e", "i", "o", "u"};
   public static final String[] endings = {"bane", "berry", "borne", "bridge", "burn", "burg", "burgh", "chester", "cott", "dale", "den", "dover", "dunn", "ester", "field", "ford", "folk", "gate", "glen", "ia", "ingdon", "ington", "kirk", "ham", "holm", "hope", "smith", "howe", "hirst", "king", "land", "loch", "more", "nor", "pen", "pool", "port", "pont", "pike", "shaw", "shire", "shot", "stow", "stoe", "thane", "thorp", "ville", "wick", "wich", "worth"};
   public static final String[] capitalPrefix = {"First", "Greater", "Great", "Royal", "Upper"};
   public static final String[] cityPrefix = {"Old", "New", "Fort", "Kings", "Lower", "Middle", "Dragons", "Knights", "Heroes", "Lords"};
   public static final String[] citySuffix = {"Proper", "Town", "City", "Commons", "Manor", "Township", "Keep", "Hold"};
   public static final String[] portPrefix = {"Old", "New", "Lower", "Middle", "Piers", "Port", "Waters"};
   public static final String[] portSuffix = {"Anchorage", "Bay", "Cove", "Dockage", "Docks", "Gates", "Gulf", "Harbor", "Haven", "Inlet", "Landing", "Wharf", "Yard"};
   public static final String[] templeSuffix = {"Temple", "Ruins", "Mission", "Santuary", "Shrine", "Pantheon", "Chapel", "Tabernacle", "Manor", "Cathedral"};
   public static final String[] caveSuffix = {"cave", "cavern", "hollow", "grotto", "burrow", "den"};
   public static final String[] mineSuffix = {"mine", "quarry", "fount", "shaft"};
   public static final String[] lairSuffix = {"lair", "burrow", "den", "grotto", "nest", "fort", "keep", "hold"};
   public static final String[] dungeonSuffix = {"dungeon", "crypt", "prison", "catacombs", "mausoleum", "tomb", "undercroft", "bastille", "underworld"};
   public static final String[] sewerSuffix = {"sewer", "waterworks", "drainworks", "channel", "outlet", "cesspit", "cessworks", "cesspool", "culvert", "pipeworks"};
   public static final String[] battlefieldSuffix = {"Pass", "Crossing", "Valley", "Field", "Front", "Passing", "Gorge", "Basin", "Plain", "Clove", "Gulch", "Gap", "Trenches"};
   public static final String[] campSuffix = {"Camp", "Encampment", "Barracks", "Base", "Site", "Campsite", "Grounds", "Pitch", "Holdover"};
   public static final String[] arenaSuffix = {"Arena", "Bowl", "Coliseum", "Field", "Hippodrome", "Odeum", "Ring", "Stade", "Stadium"};
   public static final String[] trapTypes = {"Poison-needle", "Poison-gas", "Spike", "Arrow", "Hammer", "Steel-jaw", "Web"};

   //returns a random int between [min and max] inclusive
   public static int rollDie(int min, int max)
   {
      return (int)(Math.random()*(max-min+1)) + min;
   }

   //returns a random int between [1 and numSides] inclusive
   public static int rollDie(int numSides)
   {
      return rollDie(1, numSides);
   }
   
   //given the direction from a KeyEvent arrow key and a (row,col) pair, returns the modified (row,col) in the direction of the key sent
   public static Coord dirModifierKey(int k, int row, int col)
   {
      if(k==java.awt.event.KeyEvent.VK_UP)
         return new Coord(row-1, col);
      if(k==java.awt.event.KeyEvent.VK_DOWN)
         return new Coord(row+1, col);
      if(k==java.awt.event.KeyEvent.VK_LEFT)
         return new Coord(row, col-1);
      if(k==java.awt.event.KeyEvent.VK_RIGHT)
         return new Coord(row, col+1);
      return new Coord(-1, -1);
   }

//post: returns a randomly generated name
   public static String randomName()
   {
      String ans = "";
      int numTries = 0;
      do
      {
         ans = getRandomFrom(starts);
         byte length = (byte)((Math.random()*4) + 3);
         if(length % 2 != 0)
         {
            if(Math.random() < 0.5)
               length--;
            else
               length++;
         }
         for(byte i=0; i<length; i++)
         {
            if(i%2!=0)
               ans += getRandomFrom(consonants);
            else
               ans += getRandomFrom(vowels);
         }
         numTries++;
      }
      while(numTries<1000 && NPC.isChallenge(ans));
      return ans;
   }

//pre: type != null, for locations, type is either mine,cave,temple,lair,dungeon,battlefield,port,town,capital,arena
//post:returns a randomly generated name with appropriate prefix or suffix depending on arg type
   public static String nameGenerator(String type)
   {
      String ans = randomName();
      if(type.equals("mine"))
         ans = ans + " " + getRandomFrom(mineSuffix);
      else if(type.equals("cave"))
         ans = ans + " " + getRandomFrom(caveSuffix);
      else if(type.equals("temple"))
         ans = ans + " " + getRandomFrom(templeSuffix);
      else if(type.equals("lair"))
         ans = ans + " " + getRandomFrom(lairSuffix);
      else if(type.equals("dungeon"))
         ans = ans + " " + getRandomFrom(dungeonSuffix);
      else if(type.equals("battlefield"))
         ans = ans + " " + getRandomFrom(battlefieldSuffix);
      else if(type.equals("camp"))
         ans = ans + " " + getRandomFrom(campSuffix);
      else if(type.equals("port"))
      {
         if(ans.length() <= 6)
            ans += getRandomFrom(endings);
         if(Math.random() < 0.5)
            ans = getRandomFrom(portPrefix) + " " + ans;  
         else
            ans = ans + " " + getRandomFrom(portSuffix);
      }
      else if(type.equals("town") || type.equals("village"))
      {
         if(ans.length() <= 6)
            ans += getRandomFrom(endings);
         if(Math.random() < 0.5)
            ans = getRandomFrom(cityPrefix) + " " + ans;  
         else
            ans = ans + " " + getRandomFrom(citySuffix);
      }
      else if(type.equals("capital"))
      {
         ans = getRandomFrom(capitalPrefix) + " " + ans;  
      }
      else if(type.equals("arena"))
      {
         ans = ans + " " + getRandomFrom(arenaSuffix);
      }
      return ans;
   }

//pre:  longName != null
//post: given a name of any complexity, return the simple name
//      i.e.  "Mage Trogdor of North Shopshire" would just return "Trogdor"
   public static String shortName(String longName)
   {
      String targName = new String(longName);
      if(targName.indexOf(" of ") >= 0)
      {
         int pos = targName.indexOf(" of ");
         targName = targName.substring(0, pos);
      }
      targName.replace("Ironsmith ","");
      targName.replace("Mage ","");
      targName.replace("Madame Baker ","");
      targName.replace("Butcher ","");
      targName.replace("Madame Barkeep ","");
      targName.replace("Barkeep ","");
      targName.replace("Sir ","");
   
      if(targName.startsWith("~"))    //vampire tag
      {
         String [] parts = targName.split("~");
         return parts[1];
      }
      if(targName.contains("~"))    //werewolf tags are at the end of NPC names follwed by thier imageIndex, like Lothar~2
      {
         String [] parts = targName.split("~");
         return parts[0];
      }
      return targName;
   }
   
//pre:  longName != null
//post: given a name of any complexity, return the simple name without hometown but keeps a title like Mage or Sir
//      i.e.  "King Trogdor of North Shopshire" would just return "King Trogdor"
   public static String shortNameWithTitle(String longName)
   {
      String targName = new String(longName);
      if(targName.indexOf(" of ") >= 0)
      {
         int pos = targName.indexOf(" of ");
         targName = targName.substring(0, pos);
      }
      if(targName.startsWith("~"))    //vampire tag
      {
         String [] parts = targName.split("~");
         return parts[1];
      }
      if(targName.contains("~"))    //werewolf tag
      {
         String [] parts = targName.split("~");
         targName = parts[0];
      }
      return targName;
   }
  
 //post: returns a random german name, used for characters encountered in being teleported to 1942 and wolfenstein realm  
   public static String getGermanName()
   {
      String [] names = {  "Dieter", "Gunter", "Hans", "Horst", "Jurgen", "Klaus", "Manfred", "Uwe", "Wolfgang",
                           "Gerhard", "Heinz", "Helmut", "Karl", "Kurt", "Werner",
                           "Ernst", "Friedrich", "Hans", "Heinrich", "Hermann", "Otto", "Wilhelm",
                           "Jorg", "Jurgen", "Stefan"};
      return getRandomFrom(names);
   }  
   
   //pre: name != null
   //post:true if name is a rations shopkeep behind a counter
   public static boolean isRationsShopkeep(String name)
   {
      name = name.toLowerCase();
      if(name.contains("butcher") || name.contains("baker"))
         return true;
      return false;
   }
   
   //pre: name != null
   //post:true if name is an arms shopkeep behind a counter
   public static boolean isArmsShopkeep(String name)
   {
      name = name.toLowerCase();
      if(name.contains("ironsmith"))
         return true;
      return false;
   }
   
   //pre: name != null
   //post:true if name is a tavern shopkeep behind a counter
   public static boolean isTavernShopkeep(String name)
   {
      name = name.toLowerCase();
      if(name.contains("barkeep"))
         return true;
      return false;
   }
   
   //pre: name != null
   //post:true if name is a magic shopkeep behind a counter
   public static boolean isMagicShopkeep(String name)
   {
      name = name.toLowerCase();
      if(name.contains("mage"))
         return true;
      return false;
   }
   
   //pre: name != null
   //post:true if name is a shopkeep behind a counter
   public static boolean isShopkeep(String name)
   {
      name = name.toLowerCase();
      if(name.contains("ironsmith") || name.contains("mage")
      || name.contains("butcher") || name.contains("baker") || name.contains("barkeep"))
         return true;
      return false;
   }

//pre: word != null
//post:returns word of external punctuation
   public static String clean(String word)
   {
      if(word == null || word.length() == 0)
         return word;
      int first = -1, last = -1;
      for(int i=0; i<word.length(); i++)
         if(Character.isLetter(word.charAt(i)))
         {
            first = i;
            break;
         }
      if(first == -1)
         return word;
      for(int i=word.length()-1; i>=0; i--)
         if(Character.isLetter(word.charAt(i)))
         {
            last = i;
            break;
         }
      return word.substring(first,last+1);
   }

   //post: returns true if word starts with a vowel
   public static boolean startsWithVowel(String word)
   {
      if(word==null || word.length() == 0)
         return false;
      word = word.toLowerCase();
      char first = word.charAt(0);
      return (first=='a' || first=='e' || first=='i' || first=='o' || first=='u');
   }
  
   //post: for NPC dialogue, returns true if this is a word that can be followed up on in conversation
   public static boolean isFollowUpWord(String tempWord)
   {
      if(tempWord == null || tempWord.length()==0)
         return false;
      if(tempWord.length() == 1) //should filter out punctuation, "I" and "a"
      {
         if(!FileManager.wordIsInt(tempWord))
            return false;
         return true;
      }
      if(tempWord.equals(tempWord.toUpperCase()) || FileManager.wordIsInt(tempWord))
         return true;
      return false;
   }

   //post: given a number, returns the name of the direction it represents
   public static String byteDirToString(byte dir)
   {
      if(dir==LocationBuilder.NORTH)
         return "North";
      if(dir==LocationBuilder.SOUTH)
         return "South";
      if(dir==LocationBuilder.EAST)
         return "East";
      if(dir==LocationBuilder.WEST)
         return "West";
      return "none";
   }

   //post: returns true if dir1 and dir2 are the same direction
   public static boolean sameDir(String dir1, String dir2)
   {
      return (dir1.trim().toLowerCase().equals(dir2.trim().toLowerCase()) && !dir1.equals("none"));
   }
   //post: returns true if dir1 and dir2 are the same direction
   public static boolean sameDir(byte dir1, byte dir2)
   {
      return (dir1 == dir2 && dir1!=4);      //4 is the value for no wind
   }
   
   //post: returns true if dir1 and dir2 are opposite directions
   public static boolean oppositeDir(String dir1, String dir2)
   {
      dir1 = dir1.trim().toLowerCase();
      dir2 = dir2.trim().toLowerCase();
      return ((dir1.equals("north") && dir2.equals("south")) || (dir2.equals("north") && dir1.equals("south")) || 
              (dir1.equals("east") && dir2.equals("west")) || (dir2.equals("east") && dir1.equals("west")) );
   }
   
   public static boolean oppositeDir(byte direction1, int direction2)
   {
      String dir1 = byteDirToString(direction1).trim().toLowerCase();
      String dir2 = "North";
      if(direction2==java.awt.event.KeyEvent.VK_RIGHT)
         dir2 = "East";
      else if(direction2==java.awt.event.KeyEvent.VK_DOWN)
         dir2 = "South";
      else if(direction2==java.awt.event.KeyEvent.VK_LEFT)
         dir2 = "West";
      return (oppositeDir(dir1, dir2));
   }
   
  //post: returns true if dir1 and dir2 are opposite directions
   public static boolean oppositeDir(byte dir1, byte dir2)
   {
      return ((dir1==LocationBuilder.NORTH && dir2==LocationBuilder.SOUTH) || (dir2==LocationBuilder.NORTH && dir1==LocationBuilder.SOUTH) || 
              (dir1==LocationBuilder.EAST && dir2==LocationBuilder.WEST) || (dir2==LocationBuilder.EAST && dir1==LocationBuilder.WEST) );
   }
   
   //post: returns true if dir1 and dir2 are adjacent but different directions
   public static boolean sideDir(String dir1, String dir2)
   {
      return (!sameDir(dir1, dir2) && !oppositeDir(dir1, dir2));
   }
   //post: returns true if dir1 and dir2 are adjacent but different directions
   public static boolean sideDir(byte dir1, byte dir2)
   {
      return (!sameDir(dir1, dir2) && !oppositeDir(dir1, dir2));
   }

//**************Shoppe Files
//returns an arraylist of weapons and armor
   public static ArrayList<Item> loadArmory()
   { 
      ArrayList<Item> items = new ArrayList<Item>();
   
      items.add(new Item("10-arrows", 5, "Fine arrows for thy longbow, bow and crossbow"));
   
      items.add(Armor.getLeather());
      items.add(Armor.getChainmail());
      items.add(Armor.getScalemail());
      items.add(Armor.getPlate());
      items.add(Armor.getBlessedArmor());
      items.add(Armor.getExoticPlate());
   
      items.add(Weapon.getTorch());
      items.add(Item.getSling());
      items.add(Weapon.getToothedTorch());
      items.add(Weapon.getStaff());
      items.add(Weapon.getLongstaff());
      items.add(Weapon.getSpear());
      items.add(Weapon.getHalberd());
      items.add(Weapon.getBow());
      items.add(Weapon.getLongbow());
      items.add(Weapon.getFlamebow());
      items.add(Weapon.getCrossbow()); 
      items.add(Weapon.getPoisonBoltcaster());  
      items.add(Weapon.getAxe());
      items.add(Weapon.getMirroredAxe());
      items.add(Weapon.getDualAxe());
      items.add(Weapon.getHammer());
      items.add(Weapon.getSpikedHammer());
      items.add(Weapon.getExoticHammer());
      items.add(Weapon.getDagger());
      items.add(Weapon.getPoisonDagger());
      items.add(Weapon.getFrostDagger()); 
      items.add(Weapon.getMagmaDagger()); 
      items.add(Weapon.getSoulDagger()); 
      items.add(Weapon.getBaneDagger()); 
      items.add(Weapon.getSword());
      items.add(Weapon.getBlessedSword());
      items.add(Weapon.getSwordBuckler());
      items.add(Weapon.getSwordMirrorshield());
      items.add(Weapon.getShortSwords());
      items.add(Weapon.getLuteOfDestiny());
   
      items.add(Item.getIronBracer());
      items.add(Item.getClawedGlove());
      items.add(Item.getBowBracer());
   
      items.add(new Item("armsbook", 100, "A complete manual of arms and armor to supplement thy journal"));
      return items;
   }

   public static ArrayList<Item> loadMagicShoppe()
   {  
      ArrayList<Item> items = new ArrayList<Item>();
   
      items.add(Weapon.getStaff());
      items.add(Weapon.getBlindingLight());
      items.add(Weapon.getPhasewall());
      items.add(Weapon.getRaiseStone());
   // items.add(Weapon.getCurse());       //some spells have to be learned in the field
   // items.add(Weapon.getPossess());
      items.add(Weapon.getFireball());
      items.add(Weapon.getIcestrike());
      items.add(Weapon.getLightning());
      items.add(Weapon.getDeathtouch());
      items.add(Weapon.getFireShield());
      items.add(Weapon.getSummonVortex());
      items.add(Weapon.getAdvance());
      items.add(Weapon.getStonecast());
   // items.add(Weapon.getSpidersWeb());
      items.add(Weapon.getLuteOfDestiny());
   
      items.add(Armor.getMagesRobe());
      items.add(Armor.getKnowingRobe());
      items.add(Armor.getHolocaustCloak());
      items.add(Armor.getInvisibilityCloak());
   
      items.add(Spell.getShiftWind());
      items.add(Spell.getDisarm());
      items.add(Spell.getLight());
      items.add(Spell.getCharm());
      items.add(Spell.getFear());
      items.add(Spell.getHeal());
      items.add(Spell.getCure());
      items.add(Spell.getRepel());
      items.add(Spell.getRestore()); 
      items.add(Spell.getKnowing());
      items.add(Spell.getFocus());
      items.add(Spell.getEagleEye());
      items.add(Spell.getFlight());
      items.add(Spell.getMagicMist());
   // items.add(Spell.getTempest());      //must be learned in the field
      items.add(Spell.getRaiseEarth());
      items.add(Spell.getRaiseWater());
   // items.add(Spell.getTameAnimal());   //must be learned in the field
   // items.add(Spell.getRaiseDead());   
   // items.add(Spell.getTimeStop());
   // items.add(Spell.getFirestorm());
   // items.add(Spell.getFloretLade());
      items.add(Spell.getUnseen());
      items.add(Spell.getTeleport());
      items.add(Spell.getFlameBlast());
      items.add(Spell.getEnchantStone());
      items.add(Spell.getAnimateStone());
      
      items.add(Potion.getHealth());
      items.add(Potion.getCure());
      items.add(Potion.getFocus());
      items.add(Potion.getProtection());
      items.add(Potion.getSpeed());
      items.add(Potion.getStrength());
      items.add(Potion.getInvisibility());
      items.add(Potion.getAlphaMind());
      items.add(Potion.getFireSkin());
   
      items.add(Item.getHoldall());
      items.add(Item.getCharmring());
      items.add(Item.getFocushelm());
      items.add(Item.getPentangle());
      items.add(Item.getMannastone());
      items.add(Item.getLifePearl());
      items.add(Item.getMindTome());
      items.add(Item.getPowerBands());
      items.add(Item.getSwiftBoots());
      items.add(Item.getSwiftQuill());
      items.add(Item.getTalisman());
      items.add(Item.getFloretBox());
   
      items.add(new Item("spellbook", 100, "A complete manual of spellcraft to supplement thy journal"));
   
      return items;    		
   }
   
   //returns an arraylist of items that the traveling trader has
   public static ArrayList<Item> loadTraderShoppe()
   {  
      ArrayList<Item> items = new ArrayList<Item>();
      items.add(Item.getRandomMagicWeapon());
      items.add(Item.getRandomBookAll());
      items.add(Item.getRandomStone());
      items.add(Item.getRandomMiscItem());
      return items;
   }

//restore doors that we opened when we leave a Location
   public static void restoreDoors()
   {
      if(CultimaPanel.doorsToClose != null)
      {
         while(!CultimaPanel.doorsToClose.isEmpty())
         {
            RestoreItem d = CultimaPanel.doorsToClose.pop();
            int mapIndex = d.getMapIndex();
            int row = d.getRow();
            int col = d.getCol();
            byte value = d.getValue();
            if(mapIndex>=0 && mapIndex < CultimaPanel.map.size())
            {
               byte [][] curr = (CultimaPanel.map.get(mapIndex));
               if(row>=0 && row<curr.length && col>=0 && col < curr[0].length)
                  curr[row][col] = value;
            }
         }
      }
   }
   
   //restore any eggs that were havested if time
   public static void restoreEggs()
   {
      for(int i=CultimaPanel.eggsToHarvest.size()-1; i>=0; i--)
      {
         RestoreItem ri = CultimaPanel.eggsToHarvest.get(i);
         if(ri.getValue() < 0 && CultimaPanel.days >= ri.getRestoreDay())
         {
            ri.setValue((byte)(-1 * ri.getValue()));
            ri.setRestoreDay(-1);
         }
      }
   }

//returns restore-day of destroyed wall
   public static int getWallRestoreDay(String locType, byte wallValue)
   {
      byte nightDoor = TerrainBuilder.getTerrainWithName("INR_I_B_$Night_door_closed").getValue();
      if(wallValue == nightDoor)
         return CultimaPanel.days + rollDie(2);
      int restoreDay = CultimaPanel.days + 7;
      if(locType.contains("city") || locType.contains("arena"))
         restoreDay = CultimaPanel.days + 4 + rollDie(7);
      else if(locType.contains("fortress") || locType.contains("port"))
         restoreDay = CultimaPanel.days + 7 + rollDie(7);
      else if(locType.contains("castle"))
         restoreDay = CultimaPanel.days + 4 + rollDie(7);
      else if(locType.contains("tower"))
         restoreDay = CultimaPanel.days + 7 + rollDie(7);
      else if(locType.contains("village"))
         restoreDay = CultimaPanel.days + 14 + rollDie(7);
      return restoreDay;
   }

//returns restore-day of stolen chest
   public static int getChestRestoreDay(String locType)
   {
      int restoreDay = CultimaPanel.days + 2;
      if(locType.contains("test"))
         restoreDay = CultimaPanel.days + 1;
      else if(locType.contains("city"))
         restoreDay = CultimaPanel.days + 2 + rollDie(7);
      else if(locType.contains("fortress") || locType.contains("port"))
         restoreDay = CultimaPanel.days + 4 + rollDie(7);
      else if(locType.contains("castle"))
         restoreDay = CultimaPanel.days + 6 + rollDie(7);
      else if(locType.contains("tower"))
         restoreDay = CultimaPanel.days + 8 + rollDie(7);
      else if(locType.contains("village"))
         restoreDay = CultimaPanel.days + 10 + rollDie(7);
      else if(locType.contains("domicile"))
         restoreDay = CultimaPanel.days + 12 + rollDie(7);  
      else if(locType.contains("dungeon"))
         restoreDay = CultimaPanel.days + 14 + rollDie(7);
      else if(locType.contains("lair"))
         restoreDay = CultimaPanel.days + 16 + rollDie(7);
      else if(locType.contains("mine"))
         restoreDay = CultimaPanel.days + 18 + rollDie(7);
      else if(locType.contains("cave"))
         restoreDay = CultimaPanel.days + 20 + rollDie(7);
      else if(locType.contains("world"))
         restoreDay = CultimaPanel.days + 22 + rollDie(7);
      return restoreDay;
   }

//clear corpse from the world after 3 days
   public static void clearCorpses()
   {
      if(CultimaPanel.corpses != null)
         for(int mi=0; mi<CultimaPanel.corpses.size(); mi++)
            for(int i=(CultimaPanel.corpses.get(mi)).size()-1; i>=0; i--)
            {
               Corpse d = (CultimaPanel.corpses.get(mi)).get(i);
               int remDay = d.getRemovalDay();
               if(CultimaPanel.days >= remDay)
                  (CultimaPanel.corpses.get(mi)).remove(i);
            }
   }

//restore Chests and destroyed walls after a certain amount of time after we took it
   public static void restoreTiles()
   {
      if(CultimaPanel.tilesToRestore != null)
         for(int i=CultimaPanel.tilesToRestore.size()-1; i>=0; i--)
         {
            RestoreItem d = CultimaPanel.tilesToRestore.get(i);
            int mapIndex = d.getMapIndex();
            int row = d.getRow();
            int col = d.getCol();
            byte value = d.getValue();
            int restDays = d.getRestoreDay();
            if(CultimaPanel.days >= restDays)
            {
               if(mapIndex>=0 && mapIndex < CultimaPanel.map.size())
               {
                  byte[][]currMap = (CultimaPanel.map.get(mapIndex));
                  if(row>=0 && row<currMap.length && col>=0 && col<currMap[0].length)
                     currMap[row][col] = value;
                  CultimaPanel.tilesToRestore.remove(i);
               }
            }
         }
   }

//restore barrels after a certain amount of time after we smashed it
   public static void restoreBarrels()
   {
      if(CultimaPanel.barrelsToRestore != null)
         for(int i=CultimaPanel.barrelsToRestore.size()-1; i>=0; i--)
         {
            RestoreItem d = CultimaPanel.barrelsToRestore.get(i);
            int mapIndex = d.getMapIndex();
            int row = d.getRow();
            int col = d.getCol();
            byte value = d.getValue();
            int restDays = d.getRestoreDay();
            if(CultimaPanel.days >= restDays)
            {
               if(mapIndex>=0 && mapIndex < CultimaPanel.map.size())
               {
                  CultimaPanel.furniture.get(mapIndex).add(new NPCPlayer(NPC.BARREL, row, col, mapIndex, CultimaPanel.player.getLocationType()));
                  CultimaPanel.barrelsToRestore.remove(i);
               }
            }
         }
   }

//restore NPCs after a certain amount of time after they died
   public static void restoreNPCs()
   {
      if(CultimaPanel.NPCsToRestore != null)
         for(int i=CultimaPanel.NPCsToRestore.size()-1; i>=0; i--)
         {
            NPCPlayer d = CultimaPanel.NPCsToRestore.get(i);
         
            byte charIndex = d.getCharIndex();
            if(!NPC.isGuard(charIndex) && !d.isShopkeep())    //guards and shopkeeps should restore, otherwise make a new random character
            {
               charIndex = -1;
            }
            int row = d.getRow();
            int col = d.getCol();
            int mapIndex = d.getMapIndex();
            int homeRow = d.getHomeRow();
            int homeCol = d.getHomeCol();
            String locType = d.getLocationType();
            String name = d.getName();
            int restDays = d.getRestoreDay();
            byte moveType = d.getMoveType();
            if(restDays != -1 && CultimaPanel.days >= restDays)
            {
               NPCPlayer toAdd = new NPCPlayer(charIndex, row, col, mapIndex, homeRow, homeCol, locType);
               //don't restore the old moveType - a new player that has moved in should not hold a grudge
               if(moveType != NPC.CHASE)
               {
                  toAdd.setMoveType(moveType);
               }
               if(d.isShopkeep())
               {
                  String namePrefix = "";
                  if(name.toLowerCase().contains("ironsmith"))
                  {
                     namePrefix = "Ironsmith";
                  }
                  else if(name.toLowerCase().contains("mage"))
                  {
                     namePrefix = "Mage";
                  }
                  else if(name.toLowerCase().contains("butcher"))
                  {
                     namePrefix = "Butcher";
                  } 
                  else if(name.toLowerCase().contains("baker"))
                  {
                     namePrefix = "Baker";
                  } 
                  else if(name.toLowerCase().contains("barkeep"))
                  {
                     namePrefix = "Barkeep";
                  }
                  if(namePrefix.length() > 0)
                  {
                     toAdd.setName(namePrefix + " " + toAdd.getName());
                     toAdd.setMoveType(NPC.STILL);
                     if((locType.contains("castle") || locType.contains("tower")) && namePrefix.contains("Ironsmith"))
                     {
                        toAdd.addItem(Item.getCraftingManual());
                     }
                  }
               }
               if(mapIndex >= CultimaPanel.civilians.size())
               {
                  CultimaPanel.civilians.add(mapIndex, new ArrayList<NPCPlayer>());
               }
               CultimaPanel.civilians.get(mapIndex).add(toAdd);
               CultimaPanel.NPCsToRestore.remove(i);
            }
         }
   }
   
   //restores all NPCs to original move type and location
   //used if you make a new character in the same world - any that were chasing the old character will forget them
   public static void clearNPCMemory()
   {
      for(ArrayList<NPCPlayer> c:CultimaPanel.civilians)   
         for(NPCPlayer p: c)
         {
            p.setEndChaseDay(-1);
            p.restoreLoc();
         }
   }

//picks a lock next to row, col
   public static boolean pickALock(byte[][]cave, int row, int col)
   {
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      String thisLocName = thisLoc.getName().toLowerCase();
      boolean inCamp = (locType.contains("camp") || thisLocName.endsWith(" camp") || CultimaPanel.player.getLocationType().toLowerCase().contains("camp"));
      
      boolean success=false;
      for(int r=row - 1; r<=row+1; r++)
         for(int c=col - 1; c<=col + 1; c++)
         {
            if(r<0 || c<0 || r>=cave.length || c>=cave[0].length || (r==row && c==col))
               continue;
            Terrain current = CultimaPanel.allTerrain.get(Math.abs(cave[r][c]));
            String name = current.getName();
         
            if(name.toLowerCase().contains("locked") || name.toLowerCase().contains("trap"))
            {
               int index = current.getDescription().indexOf("_locked");
               if(index == -1)
                  index = current.getDescription().indexOf("_trap");
               if(CultimaPanel.door1!=null && CultimaPanel.door2!=null && CultimaPanel.door3 != null && (CultimaPanel.player.getLocationType().toLowerCase().contains("dungeon") || CultimaPanel.player.getLocationType().toLowerCase().contains("cave") ))
               {  //these are magic locked doors for the dungeon's 3 doors puzzle
                  if((r==CultimaPanel.door1.getRow() && c==CultimaPanel.door1.getCol()) || (r==CultimaPanel.door2.getRow() && c==CultimaPanel.door2.getCol()) || (r==CultimaPanel.door3.getRow() && c==CultimaPanel.door3.getCol())   )
                     index = -1;
               }
               if(index > 0)   //destroy trap, or change locked door to unlocked
               {
                  if(name.toLowerCase().contains("locked"))
                     Player.stats[Player.LOCKS_PICKED]++;
                  String temp = current.getDescription().substring(0, index).toLowerCase();
                  Terrain unlocked = null;
                  for(Terrain t: CultimaPanel.allTerrain)
                  {
                     String descr = t.getDescription().toLowerCase();
                     if(descr.contains(temp) && !descr.contains("locked") && !descr.contains("trap") && !descr.contains("book"))
                     {
                        cave[r][c] = (byte)(-1*(t.getValue()));
                        success = true;
                        //if this is unlocking the cell of a trapped creature in a camp, set its move-type or alignment
                        if(inCamp)
                        {  
                           alertGuards();
                           //find creature adjacent to the lock
                           NPCPlayer[] adjacents = getAdjacentNPCs(r, c, CultimaPanel.player.getMapIndex());
                           for(int i=0; i<adjacents.length; i++)
                           {
                              NPCPlayer p = adjacents[i];
                              if(p!=null && !NPC.isBrigand(p.getCharIndex()) && !NPC.isGuard(p.getCharIndex()))
                              {
                                 byte pType = p.getCharIndex();
                                 int pRow = p.getRow();
                                 int pCol = p.getCol();
                                 //once the iron door is unlocked, monster will spring out, removing the door themselves
                                 cave[r][c] = cave[pRow][pCol]; 
                                 for(int pr=pRow-1; pr<=pRow+1; pr++)
                                 {
                                    for(int pc=pCol-1; pc<=pCol+1; pc++)
                                    { 
                                       if(CultimaPanel.allTerrain.get(Math.abs(cave[pr][pc])).getName().toLowerCase().contains("door"))
                                       {
                                          cave[pr][pc] = cave[pRow][pCol];
                                       }
                                    }
                                 }
                                 if(NPC.isCivilian(pType))
                                 {  //werewolves in human form
                                    p.setMoveTypeTemp(NPC.RUN);
                                 } 
                                 else if(pType==NPC.BEAR || pType==NPC.BEARKING || pType==NPC.WOLFKING || pType==NPC.WOLF)
                                 {  //bears and wolves will not attack you when you liberate them
                                    p.setMoveTypeTemp(NPC.ROAM);
                                 }
                                 else if(pType==NPC.DOG)
                                 {  
                                    p.setHasMet(true);
                                    p.setReputation(CultimaPanel.player.getReputation());
                                    p.setMoveTypeTemp(NPC.CHASE);
                                    CultimaPanel.player.stats[Player.DOGS_TRAINED]++;  
                                    p.makeSimpleName(); 
                                    CultimaPanel.player.setDogBasicInfo(p.basicInfo());
                                 }
                                 else
                                 {
                                    p.setMoveTypeTemp(NPC.CHASE);
                                 }
                              }
                           }
                        }
                        break;
                     }
                  }
                  CultimaPanel.player.specificExperience[Player.AGILITY]++;
               }
            }   
         }
      return success;
   }

  //fills adjacentTalkableNPCs: the adjacent civilian or friendly dog NPCs to the position at row r, col c in map index mi
  //assigns CultimaPanel.getInRangeToFeed, which is the NPC that is feedable and close enough to feed
  //counts the number of adjacent NPCs that can talk
  //sees if we are next to any NPCs that we can pickpocket 
   public static void getAdjacentNPCs()
   {
      int r = CultimaPanel.player.getRow();
      int c = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();
      String pArmor = CultimaPanel.player.getArmor().getName().toLowerCase();
      boolean hasKnowing = (CultimaPanel.player.getActiveSpell("Knowing")!=null || pArmor.contains("knowing"));
      CultimaPanel.AllNPCinRange = new HashSet<NPCPlayer>();
      CultimaPanel.adjacentTalkableNPCs = new NPCPlayer[4];
      CultimaPanel.adjacentMonsters = new NPCPlayer[4];
      CultimaPanel.getInRangeToFeed = null;
      CultimaPanel.getDeathAbout = null;
      CultimaPanel.ourDog = null;
      CultimaPanel.ourNpc = null;
      CultimaPanel.onUs = null;
      CultimaPanel.missionTarget = null;
      CultimaPanel.numNPCsToTalk = 0;
      CultimaPanel.nextToNPCtoPickpocket = false;
      CultimaPanel.zombieAbout = false;
      CultimaPanel.phantomAbout = false;
      CultimaPanel.werewolfAbout = false;
      CultimaPanel.guardsOnAlert = false;
      CultimaPanel.thereIsDemonSwinePlayer = false;
      CultimaPanel.assassinAbout = -1;
      CultimaPanel.numNPCsInLoc = 0;
      CultimaPanel.numWorldMonstersToExclude = 0;
      CultimaPanel.numBattleFieldEnemies = 0;
      CultimaPanel.numEnemiesOnShip = 0;
      if(r < 0 || c < 0)
         return;
      byte[][]currMap = CultimaPanel.map.get(mi);	
      boolean up=false, down=false, left=false, right=false;      //used to count the number of NPCs that are available to talk
      String current = CultimaPanel.allTerrain.get(Math.abs(CultimaPanel.map.get(mi)[r][c])).getName().toLowerCase(); 
      if(mi == 0)
      {
         for(int pi=CultimaPanel.boats.size()-1; pi>=0; pi--)
         {
            NPCPlayer p = CultimaPanel.boats.get(pi);
            if(p.getRow() >= currMap.length || p.getCol() >= currMap[0].length)
               break;
            String pSpot = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][p.getCol()])).getName().toLowerCase();
            if(!pSpot.contains("water") && !pSpot.contains("bog"))
            {  //if the boat is not on water, remove it
               Utilities.removeNPCat(p.getRow(), p.getCol(), mi);
               continue;
            } 
            double dist = Display.findDistance(p.getRow(), p.getCol(), r, c);
            if(dist <= CultimaPanel.SCREEN_SIZE/2*1.5)
               CultimaPanel.AllNPCinRange.add(p);    
            if(p.getRow()==r-1 && p.getCol()==c)
            {
               CultimaPanel.adjacentMonsters[LocationBuilder.NORTH] = p;  
            }
            if(p.getRow()==r+1 && p.getCol()==c)
            {
               CultimaPanel.adjacentMonsters[LocationBuilder.SOUTH] = p;
            }
            if(p.getRow()==r && p.getCol()==c-1)
            {
               CultimaPanel.adjacentMonsters[LocationBuilder.WEST] = p;
            }
            if(p.getRow()==r && p.getCol()==c+1)
            {
               CultimaPanel.adjacentMonsters[LocationBuilder.EAST] = p;
            }
         }
      }
      for(int pi=CultimaPanel.furniture.get(mi).size()-1; pi>=0; pi--)
      {
         NPCPlayer p = CultimaPanel.furniture.get(mi).get(pi);
         double dist = Display.findDistance(p.getRow(), p.getCol(), r, c);
         if(dist <= CultimaPanel.SCREEN_SIZE/2*1.5)
            CultimaPanel.AllNPCinRange.add(p);    
         if(p.getRow()==r-1 && p.getCol()==c)
         {
            CultimaPanel.adjacentMonsters[LocationBuilder.NORTH] = p;  
         }
         if(p.getRow()==r+1 && p.getCol()==c)
         {
            CultimaPanel.adjacentMonsters[LocationBuilder.SOUTH] = p;
         }
         if(p.getRow()==r && p.getCol()==c-1)
         {
            CultimaPanel.adjacentMonsters[LocationBuilder.WEST] = p;
         }
         if(p.getRow()==r && p.getCol()==c+1)
         {
            CultimaPanel.adjacentMonsters[LocationBuilder.EAST] = p;
         }
      }
   
      for(int pi=CultimaPanel.civilians.get(mi).size()-1; pi>=0; pi--)
      {
         NPCPlayer p = CultimaPanel.civilians.get(mi).get(pi);
         if(mi!=0 && NPC.isShip(p))
         {  //ships and boats should not be in locations, so remove them if they end up there somehow
            CultimaPanel.civilians.get(mi).remove(pi);
            continue; 
         }
         double dist = Display.findDistance(p.getRow(), p.getCol(), r, c);
         if(dist <= CultimaPanel.SCREEN_SIZE/2*1.5)
            CultimaPanel.AllNPCinRange.add(p);
         if(p.getRow()==CultimaPanel.player.getRow() && p.getCol()==CultimaPanel.player.getCol() && p.getMapIndex()==CultimaPanel.player.getMapIndex())
         {
            CultimaPanel.onUs = p;
            if(p.isNonPlayer())
            {  //non-players are used to mark mission targets on a map - remove them if we find it
               CultimaPanel.civilians.get(mi).remove(pi);
               continue;
            }
         }   
         if(NPC.isTameableCanine(p.getCharIndex()))
         {
            if(p.isOurDog())
            {
               if(p.getBody() <= 0)
               {
                  removeDogFromLocation();
                  CultimaPanel.player.setDogBasicInfo("none");
               }
               else
                  CultimaPanel.ourDog = p;
            }
         }
         if(p.isEscortedNpc())
         {
            if(p.getBody() <= 0)
            {
               removeNpcFromLocation();
               CultimaPanel.player.setNpcBasicInfo("none");
            }
            else
               CultimaPanel.ourNpc = p;
         }
      
         if(hasKnowing)
         {
            String pName = Utilities.shortName(p.getName());
            String pNameWithTitle = Utilities.shortNameWithTitle(p.getName());
            if(CultimaPanel.missionTargetNames.contains(pName) || CultimaPanel.missionTargetNames.contains(pNameWithTitle) || p.getBounty() > 0 || p.hasMissionItem() || p.isNonPlayer())
            {
               double distToLastTarg = Double.MAX_VALUE;
               if(CultimaPanel.missionTarget != null)
                  distToLastTarg = Display.findDistance(CultimaPanel.missionTarget.getRow(), CultimaPanel.missionTarget.getCol(), r, c);
               if(dist < distToLastTarg)
                  CultimaPanel.missionTarget = p;
            }
         }
         if(p.getMapIndex()==mi  && !p.isStatue()  && !p.isNonPlayer() && p.getCharIndex()!=NPC.BARREL)
            CultimaPanel.numNPCsInLoc++;
         if(NPC.isGuard(p.getCharIndex()) && p.getMoveType()==NPC.CHASE)
            CultimaPanel.guardsOnAlert = true;
                
         if(p.getNumInfo()>=0)      //if zombie or statue, they are not really considered people any more
         {
            if(p.getRow()==r-1 && p.getCol()==c)
            {
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.NORTH] = p;
               CultimaPanel.nextToNPCtoPickpocket = true;
            }
            if(p.getRow()==r+1 && p.getCol()==c)
            {
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.SOUTH] = p;
               CultimaPanel.nextToNPCtoPickpocket = true;
            }
            if(p.getRow()==r && p.getCol()==c-1)
            {
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.WEST] = p; 
               CultimaPanel.nextToNPCtoPickpocket = true;
            }
            if(p.getRow()==r && p.getCol()==c+1)
            {
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.EAST] = p;
               CultimaPanel.nextToNPCtoPickpocket = true;
            }
            boolean listeningDog = (p.isOurDog() || (p.getCharIndex()==NPC.DOG && (p.getMoveType()!=NPC.CHASE)));
            boolean talkingDemon = (p.getName().contains("Skara Brae") || p.getName().contains("Puzzle") || p.swinePlayer());
            if((p.getMoveType()!=NPC.RUN && (NPC.isCivilian(p) || listeningDog || talkingDemon)) && p.getNumInfo()>=0)  
            {
               if(p.getRow()==r-1 && p.getCol()==c)
               {
                  CultimaPanel.numNPCsToTalk++;
                  up = true;
               }
               if(p.getRow()==r+1 && p.getCol()==c)
               {
                  CultimaPanel.numNPCsToTalk++;                  
                  down = true;
               }
               if(p.getRow()==r && p.getCol()==c-1)
               {
                  CultimaPanel.numNPCsToTalk++;                  
                  left = true;
               }
               if(p.getRow()==r && p.getCol()==c+1)
               {
                  CultimaPanel.numNPCsToTalk++;                  
                  right = true;
               }
               if(p.getRow()==r-2 && p.getCol()==c && !LocationBuilder.isBlockingToTalk(currMap, r-1, c) && !up)
                  CultimaPanel.numNPCsToTalk++;
               if(p.getRow()==r+2 && p.getCol()==c && !LocationBuilder.isBlockingToTalk(currMap, r+1, c) && !down)
                  CultimaPanel.numNPCsToTalk++;
               if(p.getRow()==r && p.getCol()==c-2 && !LocationBuilder.isBlockingToTalk(currMap, r, c-1) && !left) 
                  CultimaPanel.numNPCsToTalk++;
               if(p.getRow()==r && p.getCol()==c+2 && !LocationBuilder.isBlockingToTalk(currMap, r, c+1) && !right)
                  CultimaPanel.numNPCsToTalk++;
            }
         }
      }
      for(NPCPlayer p:CultimaPanel.civilians.get(mi))   //scan for npcs 2 spaces away if we did not find one 1 space away
      {
         if(p.isZombie() && !p.isStatue() && !p.isNonPlayer())
            CultimaPanel.zombieAbout = true;
         if(p.isWerewolf() && !p.isStatue() && !p.isNonPlayer())
            CultimaPanel.werewolfAbout = true;
         if(p.getCharIndex()==NPC.DEMON)
         {
            if(p.swinePlayer())
               CultimaPanel.thereIsDemonSwinePlayer = true;
            else if(p.getMoveType()!=NPC.CHASE)
               p.setMoveType(NPC.CHASE);
         }
         if(!p.isStatue() && !p.isNonPlayer())
         {
            if(NPC.isAssassin(p.getCharIndex()))
               CultimaPanel.assassinAbout = p.getCharIndex();
            if(p.getCharIndex()==NPC.WEREWOLF || p.getCharIndex()==NPC.MALEVAMP || p.getCharIndex()==NPC.FEMALEVAMP)
               CultimaPanel.assassinAbout = p.getCharIndex();
         }
         if(p.getNumInfo()>=0)      //if zombie or statue, they are not really considered people any more
         {
            if(CultimaPanel.adjacentTalkableNPCs[LocationBuilder.NORTH] == null  && p.getRow()==r-2 && p.getCol()==c  && !LocationBuilder.isBlockingToTalk(currMap, r-1, c))
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.NORTH] = p;
            if(CultimaPanel.adjacentTalkableNPCs[LocationBuilder.SOUTH] == null  && p.getRow()==r+2 && p.getCol()==c && !LocationBuilder.isBlockingToTalk(currMap, r+1, c))
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.SOUTH] = p;
            if(CultimaPanel.adjacentTalkableNPCs[LocationBuilder.WEST] == null  && p.getRow()==r && p.getCol()==c-2 && !LocationBuilder.isBlockingToTalk(currMap, r, c-1))
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.WEST] = p;
            if(CultimaPanel.adjacentTalkableNPCs[LocationBuilder.EAST] == null  && p.getRow()==r && p.getCol()==c+2 && !LocationBuilder.isBlockingToTalk(currMap, r, c+1))
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.EAST] = p;
         }
      }
      for(int pi=CultimaPanel.worldMonsters.size()-1; pi>=0; pi--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(pi);
      
         if(CultimaPanel.player.getLocationType().equals("battlefield") && p.getMapIndex()==CultimaPanel.player.getMapIndex() && !NPC.isCivilian(p) && p.getCharIndex()!=NPC.BOWASSASSIN  && p.getCharIndex()!=NPC.ENFORCER && !p.isEscortedNpc() && !p.isOurDog() && p.getCharIndex()!=NPC.FIRE && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WILLOWISP && !p.isStatue() && !p.isNonPlayer())
            CultimaPanel.numBattleFieldEnemies++;
      
         if(CultimaPanel.player.getLocationType().equals("ship") && p.getMapIndex()==CultimaPanel.player.getMapIndex() && !p.isEscortedNpc() && !p.isOurDog() && p.getCharIndex()!=NPC.FIRE && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WILLOWISP && !p.isStatue() && !p.isNonPlayer())
            CultimaPanel.numEnemiesOnShip++;
      
         double dist = Display.findDistance(p.getRow(), p.getCol(), r, c);
         if(p.getRow()==CultimaPanel.player.getRow() && p.getCol()==CultimaPanel.player.getCol() && p.getMapIndex()==CultimaPanel.player.getMapIndex())
         {
            CultimaPanel.onUs = p;
            if(p.getCharIndex() == NPC.FIRE && p.getPercentHealth() < 20)
            {  //we stomp the fire out
               p.damage(rollDie(1,5),"stamp");
               if(Math.random() < 0.1)
               {
                  int radius = rollDie(5,10);
                  int opacity = rollDie(60,85);
                  int plX = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2)  - (radius/2);
                  int plY = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2) - (radius/2);   
                  CultimaPanel.smoke.add(new SmokePuff(plX, plY, radius, opacity, SmokePuff.dustCloud));
               }
               continue;
            } 
            if(p.isNonPlayer())
            {
               CultimaPanel.worldMonsters.remove(pi);
               continue;
            }
         }
         if(hasKnowing && p.getMapIndex()==CultimaPanel.player.getMapIndex())
         {
            String pName = Utilities.shortName(p.getName());
            String pNameWithTitle = Utilities.shortNameWithTitle(p.getName());
            if(CultimaPanel.missionTargetNames.contains(pName) || CultimaPanel.missionTargetNames.contains(pNameWithTitle) || p.getBounty() > 0 || p.hasMissionItem() || p.isNonPlayer())
            {
               double distToLastTarg = Double.MAX_VALUE;
               if(CultimaPanel.missionTarget != null)
                  distToLastTarg = Display.findDistance(CultimaPanel.missionTarget.getRow(), CultimaPanel.missionTarget.getCol(), r, c);
               if(dist < distToLastTarg)
                  CultimaPanel.missionTarget = p;
            }
         }
         if(p.getMapIndex()==mi && dist <= CultimaPanel.SCREEN_SIZE/2*1.5)
            CultimaPanel.AllNPCinRange.add(p);
         if(NPC.isTameableCanine(p.getCharIndex()))
         {
            if(p.isOurDog())
            {
               if(p.getBody() <= 0)
               {
                  removeDogFromLocation();
                  CultimaPanel.player.setDogBasicInfo("none");
               }
               else
                  CultimaPanel.ourDog = p;
            }
         }
         if(p.isEscortedNpc())
         {
            if(p.getBody() <= 0)
            {
               removeNpcFromLocation();
               CultimaPanel.player.setNpcBasicInfo("none");
            }
            else
               CultimaPanel.ourNpc = p;
         }
         if(p.getRow()==r-1 && p.getCol()==c)
         {
            CultimaPanel.adjacentMonsters[LocationBuilder.NORTH] = p;
            if(p.getNumInfo()>=0 && p.getMapIndex()==mi && NPC.isBrigand(p.getCharIndex()))      //if zombie or statue, they are not really considered people any more
               CultimaPanel.nextToNPCtoPickpocket = true;
            if(NPC.isFeedableMonster(p.getCharIndex()) && !p.hasBeenAttacked() && !p.isOurDog()) 
               CultimaPanel.getInRangeToFeed = p;   
         }
         if(p.getRow()==r+1 && p.getCol()==c)
         {
            CultimaPanel.adjacentMonsters[LocationBuilder.SOUTH] = p;
            if(p.getNumInfo()>=0 && p.getMapIndex()==mi && NPC.isBrigand(p.getCharIndex()))      //if zombie or statue, they are not really considered people any more
               CultimaPanel.nextToNPCtoPickpocket = true;
            if(NPC.isFeedableMonster(p.getCharIndex()) && !p.hasBeenAttacked() && !p.isOurDog()) 
               CultimaPanel.getInRangeToFeed = p;
         }
         if(p.getRow()==r && p.getCol()==c-1)
         {
            CultimaPanel.adjacentMonsters[LocationBuilder.WEST] = p;
            if(p.getNumInfo()>=0 && p.getMapIndex()==mi && NPC.isBrigand(p.getCharIndex()))      //if zombie or statue, they are not really considered people any more
               CultimaPanel.nextToNPCtoPickpocket = true;
            if(NPC.isFeedableMonster(p.getCharIndex()) && !p.hasBeenAttacked() && !p.isOurDog()) 
               CultimaPanel.getInRangeToFeed = p;
         }
         if(p.getRow()==r && p.getCol()==c+1)
         {
            CultimaPanel.adjacentMonsters[LocationBuilder.EAST] = p;
            if(p.getNumInfo()>=0 && p.getMapIndex()==mi && NPC.isBrigand(p.getCharIndex()))      //if zombie or statue, they are not really considered people any more
               CultimaPanel.nextToNPCtoPickpocket = true;
            if(NPC.isFeedableMonster(p.getCharIndex()) && !p.hasBeenAttacked() && !p.isOurDog()) 
               CultimaPanel.getInRangeToFeed = p;
         }
         if(p.isOurDog() || p.isEscortedNpc() || p.getCharIndex()==NPC.TRADER || (p.getCharIndex()==NPC.DOG && !p.hasBeenAttacked()))
         {
            if(p.getRow()==r-1 && p.getCol()==c)
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.NORTH] = p;
            if(p.getRow()==r+1 && p.getCol()==c)
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.SOUTH] = p;
            if(p.getRow()==r && p.getCol()==c-1)
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.WEST] = p;
            if(p.getRow()==r && p.getCol()==c+1)
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.EAST] = p;
         }
         boolean listeningDog = (p.isOurDog() || (p.getCharIndex()==NPC.DOG && (p.getMoveType()!=NPC.CHASE)));
         if(listeningDog || p.isEscortedNpc() || p.getCharIndex()==NPC.TRADER)
         {
            if(p.getRow()==r-1 && p.getCol()==c)
               CultimaPanel.numNPCsToTalk++;
            if(p.getRow()==r+1 && p.getCol()==c)
               CultimaPanel.numNPCsToTalk++;
            if(p.getRow()==r && p.getCol()==c-1)
               CultimaPanel.numNPCsToTalk++;
            if(p.getRow()==r && p.getCol()==c+1)
               CultimaPanel.numNPCsToTalk++;
            if(p.getRow()==r-2 && p.getCol()==c && !LocationBuilder.isBlockingToTalk(currMap, r-1, c))
               CultimaPanel.numNPCsToTalk++;
            if(p.getRow()==r+2 && p.getCol()==c && !LocationBuilder.isBlockingToTalk(currMap, r+1, c))
               CultimaPanel.numNPCsToTalk++;
            if(p.getRow()==r && p.getCol()==c-2 && !LocationBuilder.isBlockingToTalk(currMap, r, c-1)) 
               CultimaPanel.numNPCsToTalk++;
            if(p.getRow()==r && p.getCol()==c+2 && !LocationBuilder.isBlockingToTalk(currMap, r, c+1))
               CultimaPanel.numNPCsToTalk++;  
         }
         if(p.getRow()==r-2 && p.getCol()==c && NPC.isFeedableMonster(p.getCharIndex()) && !p.hasBeenAttacked() && !p.isOurDog()) 
            CultimaPanel.getInRangeToFeed = p;
         if(p.getRow()==r+2 && p.getCol()==c && NPC.isFeedableMonster(p.getCharIndex()) && !p.hasBeenAttacked() && !p.isOurDog()) 
            CultimaPanel.getInRangeToFeed = p;
         if(p.getRow()==r && p.getCol()==c-2 && NPC.isFeedableMonster(p.getCharIndex()) && !p.hasBeenAttacked() && !p.isOurDog()) 
            CultimaPanel.getInRangeToFeed = p;
         if(p.getRow()==r && p.getCol()==c+2 && NPC.isFeedableMonster(p.getCharIndex()) && !p.hasBeenAttacked() && !p.isOurDog())
            CultimaPanel.getInRangeToFeed = p;
      }
      for(NPCPlayer p:CultimaPanel.worldMonsters)   //scan for npcs 2 spaces away if we did not find one 1 space away
      {
         if(p.getCharIndex()==NPC.DEATH && p.getMapIndex()==mi)
            CultimaPanel.getDeathAbout = p;
         if(p.getCharIndex()==NPC.PHANTOM && p.getMapIndex()==mi)
            CultimaPanel.phantomAbout = true;
         if(p.hasMet() || p.isStatue()  || p.isNonPlayer() || NPC.isShip(p.getCharIndex()) || p.getCharIndex()==NPC.MONOLITH || p.getCharIndex()==NPC.MAGMA || NPC.isCivilian(p) || NPC.isNatural(p))
            CultimaPanel.numWorldMonstersToExclude++;
      
         if(p.getMapIndex()==mi && !p.isStatue() && !p.isNonPlayer())
         {
            if(NPC.isAssassin(p.getCharIndex()))
               CultimaPanel.assassinAbout = p.getCharIndex();
            if(p.getCharIndex()==NPC.WEREWOLF || p.getCharIndex()==NPC.MALEVAMP || p.getCharIndex()==NPC.FEMALEVAMP)
               CultimaPanel.assassinAbout = p.getCharIndex();
         }
         if(p.isOurDog() || p.isEscortedNpc() || p.getCharIndex()==NPC.TRADER || (p.getCharIndex()==NPC.DOG && !p.hasBeenAttacked()))
         {
            if(CultimaPanel.adjacentTalkableNPCs[LocationBuilder.NORTH] == null  && p.getRow()==r-2 && p.getCol()==c && !LocationBuilder.isBlockingToTalk(currMap, r-1, c))
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.NORTH] = p;
            if(CultimaPanel.adjacentTalkableNPCs[LocationBuilder.SOUTH] == null  && p.getRow()==r+2 && p.getCol()==c && !LocationBuilder.isBlockingToTalk(currMap, r+1, c))
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.SOUTH] = p;
            if(CultimaPanel.adjacentTalkableNPCs[LocationBuilder.WEST] == null  && p.getRow()==r && p.getCol()==c-2 && !LocationBuilder.isBlockingToTalk(currMap, r, c-1))
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.WEST] = p;
            if(CultimaPanel.adjacentTalkableNPCs[LocationBuilder.EAST] == null  && p.getRow()==r && p.getCol()==c+2 && !LocationBuilder.isBlockingToTalk(currMap, r, c+1))
               CultimaPanel.adjacentTalkableNPCs[LocationBuilder.EAST] = p;
         }
      }
      if(current.contains("bed"))   //prioritize sleeping over stealing, especially if it is our spouse next to us
         CultimaPanel.nextToNPCtoPickpocket = false;
      //for counting num of NPCs available to talk
      boolean flight = (CultimaPanel.player.getActiveSpell("Flight")!=null);
      boolean timestop = (CultimaPanel.player.getActiveSpell("Timestop")!=null);
      String armName = CultimaPanel.player.getArmor().getName().toLowerCase();
      boolean isWolf = (CultimaPanel.player.isWolfForm());
      if(flight || timestop || isWolf || armName.contains("cloak"))
      {
         CultimaPanel.numNPCsToTalk = 0;
         CultimaPanel.getInRangeToFeed = null;
      }
      if(CultimaPanel.player.getRations() <= 0)
         CultimaPanel.getInRangeToFeed = null;
   }
   
   public static NPCPlayer[] getAdjacentMonsters(int r, int c, int mi)
   {
      NPCPlayer[] adjacent = new NPCPlayer[4];
      if(r < 0 || c < 0)
         return adjacent;
      if(mi == 0)
      {   
         for(NPCPlayer p:CultimaPanel.boats)
         {
            if(p.getRow()==r-1 && p.getCol()==c)
               adjacent[LocationBuilder.NORTH] = p;
            if(p.getRow()==r+1 && p.getCol()==c)
               adjacent[LocationBuilder.SOUTH] = p;
            if(p.getRow()==r && p.getCol()==c-1)
               adjacent[LocationBuilder.WEST] = p;
            if(p.getRow()==r && p.getCol()==c+1)
               adjacent[LocationBuilder.EAST] = p;
         } 
      }  
      for(NPCPlayer p:CultimaPanel.furniture.get(mi))
      {
         if(p.getRow()==r-1 && p.getCol()==c)
            adjacent[LocationBuilder.NORTH] = p;
         if(p.getRow()==r+1 && p.getCol()==c)
            adjacent[LocationBuilder.SOUTH] = p;
         if(p.getRow()==r && p.getCol()==c-1)
            adjacent[LocationBuilder.WEST] = p;
         if(p.getRow()==r && p.getCol()==c+1)
            adjacent[LocationBuilder.EAST] = p;
      }
      for(NPCPlayer p:CultimaPanel.worldMonsters)
      {
         if(p.getRow()==r-1 && p.getCol()==c)
            adjacent[LocationBuilder.NORTH] = p;
         if(p.getRow()==r+1 && p.getCol()==c)
            adjacent[LocationBuilder.SOUTH] = p;
         if(p.getRow()==r && p.getCol()==c-1)
            adjacent[LocationBuilder.WEST] = p;
         if(p.getRow()==r && p.getCol()==c+1)
            adjacent[LocationBuilder.EAST] = p;
      }      
      return adjacent;
   }
   
   public static NPCPlayer[] getAdjacentNPCs(int r, int c, int mi)
   {
      NPCPlayer[] adjacent = new NPCPlayer[4];
      if(r < 0 || c < 0)
         return adjacent;
      if(mi == 0)
      {   
         for(NPCPlayer p:CultimaPanel.boats)
         {
            if(p.getRow()==r-1 && p.getCol()==c)
               adjacent[LocationBuilder.NORTH] = p;
            if(p.getRow()==r+1 && p.getCol()==c)
               adjacent[LocationBuilder.SOUTH] = p;
            if(p.getRow()==r && p.getCol()==c-1)
               adjacent[LocationBuilder.WEST] = p;
            if(p.getRow()==r && p.getCol()==c+1)
               adjacent[LocationBuilder.EAST] = p;
         } 
      }     
      for(NPCPlayer p:CultimaPanel.civilians.get(mi))
      {
         if(p.getRow()==r-1 && p.getCol()==c)
            adjacent[LocationBuilder.NORTH] = p;
         if(p.getRow()==r+1 && p.getCol()==c)
            adjacent[LocationBuilder.SOUTH] = p;
         if(p.getRow()==r && p.getCol()==c-1)
            adjacent[LocationBuilder.WEST] = p;
         if(p.getRow()==r && p.getCol()==c+1)
            adjacent[LocationBuilder.EAST] = p;
      }     
      for(NPCPlayer p:CultimaPanel.worldMonsters)
      {
         if(p.getRow()==r-1 && p.getCol()==c && p.getMapIndex()==mi)
            adjacent[LocationBuilder.NORTH] = p;
         if(p.getRow()==r+1 && p.getCol()==c && p.getMapIndex()==mi)
            adjacent[LocationBuilder.SOUTH] = p;
         if(p.getRow()==r && p.getCol()==c-1 && p.getMapIndex()==mi)
            adjacent[LocationBuilder.WEST] = p;
         if(p.getRow()==r && p.getCol()==c+1 && p.getMapIndex()==mi)
            adjacent[LocationBuilder.EAST] = p;
      }  
      for(NPCPlayer p:CultimaPanel.furniture.get(mi))
      {
         if(p.getRow()==r-1 && p.getCol()==c)
            adjacent[LocationBuilder.NORTH] = p;
         if(p.getRow()==r+1 && p.getCol()==c)
            adjacent[LocationBuilder.SOUTH] = p;
         if(p.getRow()==r && p.getCol()==c-1)
            adjacent[LocationBuilder.WEST] = p;
         if(p.getRow()==r && p.getCol()==c+1)
            adjacent[LocationBuilder.EAST] = p;
      }    
      return adjacent;
   }
   
   //post: returns a collection of npcs within range of the player that are poisoned or are hurt (for healing/curing)
   public static ArrayList<NPCPlayer> adjacentHurtNPCs(int range, boolean onlyPoisoned)
   {
      int r = CultimaPanel.player.getRow();
      int c = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();
      ArrayList <NPCPlayer> adjacent = new ArrayList();
      if(r < 0 || c < 0 || mi < 0 || mi >= CultimaPanel.civilians.size())
         return adjacent;
      for(NPCPlayer p:CultimaPanel.civilians.get(mi))
      {
         if(onlyPoisoned && !p.hasEffect("poison"))
         {
            continue;
         }
         if((NPC.isCivilian(p) && p.getMoveType()!=NPC.CHASE) || p.isOurDog() || (NPC.isHorse(p) && p.hasMet()))
         {
            double distToPlayer = Display.findDistance(p.getRow(), p.getCol(), r, c);
            if(distToPlayer <= range && p.getBody() < p.maxHealth())
            {
               adjacent.add(p);
            }
         }
      }     
      for(NPCPlayer p:CultimaPanel.worldMonsters)
      {
         if(onlyPoisoned && !p.hasEffect("poison"))
         {
            continue;
         }
         if((NPC.isCivilian(p) && p.getMoveType()!=NPC.CHASE) || p.isOurDog() || (NPC.isHorse(p) && p.hasMet()))
         {
            double distToPlayer = Display.findDistance(p.getRow(), p.getCol(), r, c);
            if(distToPlayer <= range && p.getBody() < p.maxHealth())
            {
               adjacent.add(p);
            }
         }        
      }  
      return adjacent;
   }

   
   //returns a statue if we are adjacent to one, null if we are not
   //used for the Animate-stone spell
   public static NPCPlayer nextToStatue()
   {
      NPCPlayer statue = null;
      NPCPlayer[] adjacentNPCs = getAdjacentNPCs(CultimaPanel.player.getRow(), CultimaPanel.player.getCol(), CultimaPanel.player.getMapIndex());
      for(NPCPlayer current:adjacentNPCs)
      {
         if(current != null && current.isStatue())
         {
            return current;
         }
      }
      return statue;
   }
   
   //true if we are next to a bounty sign.  If so, it will display the message
   public static boolean nextToABountyPosting(byte[][]currMap, int row, int col)
   {
      String current = "";
      if(row+1>=0 && row+1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row+1][col])).getName().toLowerCase();
         if(current.contains("sign_bounty"))
            return true;
      }
      if(row-1>=0 && row-1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row-1][col])).getName().toLowerCase();
         if(current.contains("sign_bounty"))
            return true;
      }
      if(row>=0 && row<currMap.length && col+1>=0 && col+1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col+1])).getName().toLowerCase();
         if(current.contains("sign_bounty"))
            return true;
      }
      if(row>=0 && row<currMap.length && col-1>=0 && col-1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col-1])).getName().toLowerCase();
         if(current.contains("sign_bounty"))
            return true;
      }
      return false;
   }
   
   //true if we are near or in a haunted house.  If so, it will display the message
   public static boolean nextToAHauntedHouse(byte[][]currMap, int row, int col, int range)
   {
      String current = "";
      for(int r = row-range; r <= row+range; r++)
      {
         for(int c = col-range; c <= col+range; c++)
         {
            if(!LocationBuilder.outOfBounds(currMap, r, c))
            {
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               if(current.contains("gray_tile_wall"))
                  return true;
            }
         }
      }
      return false;
   }
   
    //true if (row,col) is next to iron bar doors (in a cell). 
   public static boolean nextToIronBarDoor(byte[][]currMap, int row, int col)
   {
      String current = "";
      if(row+1>=0 && row+1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row+1][col])).getName().toLowerCase();
         if(current.contains("iron_bar_door"))
            return true;
      }
      if(row-1>=0 && row-1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row-1][col])).getName().toLowerCase();
         if(current.contains("iron_bar_door"))
            return true;
      }
      if(row>=0 && row<currMap.length && col+1>=0 && col+1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col+1])).getName().toLowerCase();
         if(current.contains("iron_bar_door"))
            return true;
      }
      if(row>=0 && row<currMap.length && col-1>=0 && col-1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col-1])).getName().toLowerCase();
         if(current.contains("iron_bar_door"))
            return true;
      }
      return false;
   }
   
   //true if (row, col) is one tile away from our owned house to know whether or not we can build
   public static boolean nextToOurHouse(int row, int col)
   {
      byte[][] currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      for(int r=row-1; r<=row+1; r++)
      {
         for(int c=col-1; c<=col+1; c++)
         {
            if(!LocationBuilder.outOfBounds(currMap, r, c))
            {
               String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               if(current.contains("purple_floor"))
               {
                  return true;
               }
            }
         }
      }
      return false;
   }
   
   //true if (row, col) is one tile away from a house that can be bought for placing a double bed
   public static boolean inBuyableHouse(int row, int col, byte[][]currMap)
   {
      for(int r=row-1; r<=row+1; r++)
      {
         for(int c=col-1; c<=col+1; c++)
         {
            if(!LocationBuilder.outOfBounds(currMap, r, c))
            {
               String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               if(current.contains("purple"))
               {
                  return true;
               }
            }
         }
      }
      return false;
   }
   
   public static boolean spotIsOurHouse(int r, int c)
   {
      byte[][] currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      if(!LocationBuilder.outOfBounds(currMap, r, c))
      {
         String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
         if(current.contains("purple_floor"))
         {
            return true;
         }    
      }
      return false;
   }
   
   //returns the direction of wardrobe if we are next to it.  If so, we can push or pull it.  returns -1 if we are not
   public static int nextToOurWardrobe()
   {
      int row = CultimaPanel.player.getRow();
      int col = CultimaPanel.player.getCol();
      byte[][] currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      String current = "";
      if(row+1>=0 && row+1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row+1][col])).getName().toLowerCase();
         if(current.contains("wardrobe"))
            return TerrainBuilder.SOUTH;
      }
      if(row-1>=0 && row-1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row-1][col])).getName().toLowerCase();
         if(current.contains("wardrobe"))
            return TerrainBuilder.NORTH;
      }
      if(row>=0 && row<currMap.length && col+1>=0 && col+1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col+1])).getName().toLowerCase();
         if(current.contains("wardrobe"))
            return TerrainBuilder.EAST;
      }
      if(row>=0 && row<currMap.length && col-1>=0 && col-1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col-1])).getName().toLowerCase();
         if(current.contains("wardrobe"))
            return TerrainBuilder.WEST;
      }
      return -1;
   }
   
   public static RestoreItem nextToOpenDoor(int row, int col)
   {
      int mapIndex = CultimaPanel.player.getMapIndex();
      for(RestoreItem r: CultimaPanel.doorsToClose)
      {
         if(r.getRow()==row-1 && r.getCol()==col && r.getMapIndex()==mapIndex && !NPCAt(r.getRow(), r.getCol(), r.getMapIndex()))
            return r;
         if(r.getRow()==row && r.getCol()==col+1 && r.getMapIndex()==mapIndex && !NPCAt(r.getRow(), r.getCol(), r.getMapIndex()))
            return r;
         if(r.getRow()==row+1 && r.getCol()==col && r.getMapIndex()==mapIndex && !NPCAt(r.getRow(), r.getCol(), r.getMapIndex()))
            return r;
         if(r.getRow()==row && r.getCol()==col-1 && r.getMapIndex()==mapIndex && !NPCAt(r.getRow(), r.getCol(), r.getMapIndex()))
            return r;
      }
      return null;
   }
   
   //returns the direction of our bed if we are next to it.  If so, we can push or pull it.  returns -1 if we are not
   public static int nextToOurBed(int r, int c)
   {
      if(CultimaPanel.ourBeds == null || !TerrainBuilder.isCity(CultimaPanel.player.getLocationType()))
         return -1;
      for(RestoreItem p: CultimaPanel.ourBeds)
      {   
         if(CultimaPanel.player.getMapIndex()==p.getMapIndex())
         {
            if(r-1==p.getRow() && c==p.getCol() )
               return TerrainBuilder.NORTH;
            if(r==p.getRow() && c+1==p.getCol() )
               return TerrainBuilder.EAST;
            if(r+1==p.getRow() && c==p.getCol() )
               return TerrainBuilder.SOUTH;
            if(r==p.getRow() && c-1==p.getCol() )
               return TerrainBuilder.WEST;
         }
      }   
      return -1;
   }
   
   //looks at adjacent spots of (r,c) and if any is an indoor floor, returns its byte value.  returns -1 if not found.
   //used to set the tile to the floor of a door that was just opened
   public static byte adjacentIndoorFloor(int row, int col)
   {
      byte[][] currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      Terrain current = null;
      if(row+1>=0 && row+1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row+1][col]));
         if(current.getName().toLowerCase().contains("floor"))
            return current.getValue();
      }
      if(row-1>=0 && row-1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row-1][col]));
         if(current.getName().toLowerCase().contains("floor"))
            return current.getValue();
      }
      if(row>=0 && row<currMap.length && col+1>=0 && col+1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col+1]));
         if(current.getName().toLowerCase().contains("floor"))
            return current.getValue();
      }
      if(row>=0 && row<currMap.length && col-1>=0 && col-1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col-1]));
         if(current.getName().toLowerCase().contains("floor"))
            return current.getValue();
      }
      return -1;
   }
   
   //looks at adjacent spots of (r,c) and if any is an outdoor ground, returns its byte value.  returns -1 if not found.
   //used to set the tile to the ground of a torch that was knocked over
   public static byte adjacentIndoorGround(int row, int col)
   {
      byte[][] currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      Terrain current = null;
      ArrayList<Terrain> replacements = new ArrayList<Terrain>();
      if(row+1>=0 && row+1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row+1][col]));
         if(current.getName().startsWith("TER_") && !current.getName().contains("_I_"))
            replacements.add(current);
      }
      if(row-1>=0 && row-1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row-1][col]));
         if(current.getName().startsWith("TER_") && !current.getName().contains("_I_"))
            replacements.add(current);
      }
      if(row>=0 && row<currMap.length && col+1>=0 && col+1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col+1]));
         if(current.getName().startsWith("TER_") && !current.getName().contains("_I_"))
            replacements.add(current);
      }
      if(row>=0 && row<currMap.length && col-1>=0 && col-1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col-1]));
         if(current.getName().startsWith("TER_") && !current.getName().contains("_I_"))
            replacements.add(current);
      }
      if(replacements.size() == 0)
      {
         return -1;
      }
      //prioritize grass and sand
      for(Terrain ter:replacements)
      {
         String terName = ter.getName().toLowerCase();
         if(terName.contains("grass") || terName.contains("sand"))
         {
            return ter.getValue();
         } 
      }
      return getRandomFrom(replacements).getValue();
   }

   
   //returns true if we are on a spot adjacent to our house where we can plant flowers
   public static boolean onHouseAdjacentGround()
   {
      int r = CultimaPanel.player.getRow();
      int c = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();         
      byte[][] currMap = CultimaPanel.map.get(mi);
      Terrain currentTer = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
      String terName = currentTer.getName();
      boolean onTerrain = (terName.contains("TER_$Sand") || terName.contains("TER_S_$Hills") || terName.toLowerCase().contains("grass"));
      boolean closeToHouse = false;
      boolean breakOuter = false;
      Terrain floor = TerrainBuilder.getTerrainWithName("INR_$Purple_floor_inside");
      for(int r2=r-2; r2<=r+2; r2++)
      {
         for(int c2=c-2; c2<=c+2; c2++)
         {
            if(LocationBuilder.outOfBounds(currMap, r2, c2))
               continue;
            if(Math.abs(currMap[r2][c2])==floor.getValue())
            {
               closeToHouse = true;
               breakOuter = true;
               break;   
            }
         }
         if(breakOuter)
            break;
      }
      boolean nextToDoor = (nextToADoor(r, c) || CultimaPanel.doorToClose != null);
      boolean nextToHouse = (onTerrain && closeToHouse);
      return (nextToHouse && !nextToCornerStone() && !nextToDoor);
   }
   
   //returns true if we are in our house - used to give option to renovate a space
   public static boolean inOurHouse()
   {
      int r = CultimaPanel.player.getRow();
      int c = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();         
      byte[][] currMap = CultimaPanel.map.get(mi);
      Terrain currentTer = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
      String terName = currentTer.getName();
      return terName.contains("Purple_floor") || nextToCornerStone();
   }
   
   //used to see if we are next to a corner stone so that we have the option to renovate it as opposed to plant flowers
   public static boolean nextToCornerStone()
   {
      int r = CultimaPanel.player.getRow();
      int c = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();         
      byte[][] currMap = CultimaPanel.map.get(mi);
      Terrain currentTer = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
      String terName = currentTer.getName();
      boolean onTerrain = (terName.contains("TER_$Sand") || terName.contains("TER_S_$Hills") || terName.toLowerCase().contains("grass"));
      Terrain floor = TerrainBuilder.getTerrainWithName("INR_$Purple_floor_inside");
      boolean nextToCornerStone = false;  //we want to be able to renovate our corner stone, so don't include those spots for planting flowers
      if(onTerrain)
      {   //upper-left corner
         if(!LocationBuilder.outOfBounds(currMap, r-1, c+1) && !LocationBuilder.outOfBounds(currMap, r+1, c+2) && (Math.abs(currMap[r+1][c+2])==floor.getValue()))
         {
            Terrain currentTer2 = CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c+1]));
            String terName2 = currentTer2.getName();
            boolean onTerrain2 = (terName2.contains("TER_$Sand") || terName2.contains("TER_S_$Hills") || terName2.toLowerCase().contains("grass"));
            if(onTerrain2)
               nextToCornerStone = true;
         }
         //upper-right corner
         if(!LocationBuilder.outOfBounds(currMap, r-1, c-1) && !LocationBuilder.outOfBounds(currMap, r+1, c-2) && (Math.abs(currMap[r+1][c-2])==floor.getValue()))
         {
            Terrain currentTer2 = CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c-1]));
            String terName2 = currentTer2.getName();
            boolean onTerrain2 = (terName2.contains("TER_$Sand") || terName2.contains("TER_S_$Hills") || terName2.toLowerCase().contains("grass"));
            if(onTerrain2)
               nextToCornerStone = true;
         }
         //lower-left corner
         if(!LocationBuilder.outOfBounds(currMap, r+1, c+1) && !LocationBuilder.outOfBounds(currMap, r-1, c+2) && (Math.abs(currMap[r-1][c+2])==floor.getValue()))
         {
            Terrain currentTer2 = CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c+1]));
            String terName2 = currentTer2.getName();
            boolean onTerrain2 = (terName2.contains("TER_$Sand") || terName2.contains("TER_S_$Hills") || terName2.toLowerCase().contains("grass"));
            if(onTerrain2)
               nextToCornerStone = true;
         }
         //lower-right corner
         if(!LocationBuilder.outOfBounds(currMap, r+1, c-1) && !LocationBuilder.outOfBounds(currMap, r-1, c-2) && (Math.abs(currMap[r-1][c-2])==floor.getValue()))
         {
            Terrain currentTer2 = CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c-1]));
            String terName2 = currentTer2.getName();
            boolean onTerrain2 = (terName2.contains("TER_$Sand") || terName2.contains("TER_S_$Hills") || terName2.toLowerCase().contains("grass"));
            if(onTerrain2)
               nextToCornerStone = true;
         }
      }
      return nextToCornerStone;
   }

   
   //used for planting flowers around our hourse
   public static byte getRandomFlower()
   {
      Terrain [] flowers = {TerrainBuilder.getTerrainWithName("TER_$Grassland_Red_Flowers"), TerrainBuilder.getTerrainWithName("TER_$Grassland_Blue_Flowers"), TerrainBuilder.getTerrainWithName("TER_$Grassland_Yellow_Flowers"),  TerrainBuilder.getTerrainWithName("TER_$Grassland_Violet_Flowers"),  TerrainBuilder.getTerrainWithName("TER_$Grassland_Green_Flowers")}; 
      return Utilities.getRandomFrom(flowers).getValue();
   }
   
   public static void swapTerrain(int r1, int c1, int r2, int c2)
   {
      byte[][] currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      byte temp = currMap[r1][c1];
      currMap[r1][c1] = currMap[r2][c2];
      currMap[r2][c2] = temp;
   }
   
   public static NPCPlayer findSpouse()
   {
      int mi = CultimaPanel.player.getMapIndex();
      for(int i=0; i<CultimaPanel.civilians.get(mi).size(); i++)
      {
         NPCPlayer selected = (CultimaPanel.civilians.get(mi)).get(i);
         if(selected.isOurSpouse()) //it is our own spouse
         {
            return selected;
         }
      }
      return null;
   }
   
   //returns lock type if cave[r][c] is next to a lock to pick, "trap", "lock" or "none"
   public static String nextToALockOrTrap(byte[][]cave, int row, int col)
   {
      for(int r=row - 1; r<=row+1; r++)
         for(int c=col - 1; c<=col + 1; c++)
         {
            if(r<0 || c<0 || r>=cave.length || c>=cave[0].length || (r==row && c==col))
               continue;
            String current = CultimaPanel.allTerrain.get(Math.abs(cave[r][c])).getName().toLowerCase();
            if(current.contains("trap"))
               return "trap";   
            if(current.contains("locked"))
               return "lock";
         }
      return "none";
   }

//returns true if cave[r][c] is next to a lock to pick
   public static boolean nextToALock(byte[][]cave, int row, int col)
   {
      for(int r=row - 1; r<=row+1; r++)
         for(int c=col - 1; c<=col + 1; c++)
         {
            if(r<0 || c<0 || r>=cave.length || c>=cave[0].length || (r==row && c==col))
               continue;
            String current = CultimaPanel.allTerrain.get(Math.abs(cave[r][c])).getName().toLowerCase();
            if(current.contains("locked"))
               return true;   
         }
      return false;
   }

   //returns true for the directions of a wall that a player is next to that can be broken
   public static boolean[] nextToAWall()
   {
      int r=CultimaPanel.player.getRow();
      int c=CultimaPanel.player.getCol();
      int mi=CultimaPanel.player.getMapIndex();
      boolean [] dirs = {false, false, false, false};   
      if(r < 0 || c < 0 || mi==0)
         return dirs;
      byte[][]currMap = CultimaPanel.map.get(mi);
      dirs[LocationBuilder.NORTH] = (r-1>=0 && LocationBuilder.isHammerBreakable(currMap,r-1,c));
      dirs[LocationBuilder.EAST] = (c+1<currMap[0].length && LocationBuilder.isHammerBreakable(currMap,r,c+1));
      dirs[LocationBuilder.SOUTH] = (r+1<currMap.length && LocationBuilder.isHammerBreakable(currMap,r+1,c));
      dirs[LocationBuilder.WEST] = (c-1>=0 && LocationBuilder.isHammerBreakable(currMap,r,c-1));
      return dirs;
   }
   
   public static boolean[] nextToACombustable()
   {
      int r=CultimaPanel.player.getRow();
      int c=CultimaPanel.player.getCol();
      int mi=CultimaPanel.player.getMapIndex();
      boolean [] dirs = {false, false, false, false};   
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return dirs;
      byte[][]currMap = CultimaPanel.map.get(mi);
      if(mi==0)
      {
         dirs[LocationBuilder.NORTH] = (LocationBuilder.isCombustable(currMap, CultimaPanel.equalizeWorldRow(r-1),c));
         dirs[LocationBuilder.EAST] = (LocationBuilder.isCombustable(currMap,r,CultimaPanel.equalizeWorldCol(c+1)));
         dirs[LocationBuilder.SOUTH] = (LocationBuilder.isCombustable(currMap,CultimaPanel.equalizeWorldRow(r+1),c));
         dirs[LocationBuilder.WEST] = (LocationBuilder.isCombustable(currMap,r,CultimaPanel.equalizeWorldCol(c-1)));
      }
      else
      {
         dirs[LocationBuilder.NORTH] = (r-1>=0 && LocationBuilder.isCombustable(currMap, r-1,c));
         dirs[LocationBuilder.EAST] = (c+1<currMap[0].length && LocationBuilder.isCombustable(currMap,r,c+1));
         dirs[LocationBuilder.SOUTH] = (r+1<currMap.length && LocationBuilder.isCombustable(currMap,r+1,c));
         dirs[LocationBuilder.WEST] = (c-1>=0 && LocationBuilder.isCombustable(currMap,r,c-1));
      }
      return dirs;
   }
   
   public static boolean[] nextToACombustable(NPCPlayer target)
   {
      int r=target.getRow();
      int c=target.getCol();
      int mi=target.getMapIndex();
      boolean [] dirs = {false, false, false, false};   
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return dirs;
      byte[][]currMap = CultimaPanel.map.get(mi);
      if(mi==0)
      {
         dirs[LocationBuilder.NORTH] = (LocationBuilder.isCombustable(currMap, CultimaPanel.equalizeWorldRow(r-1),c));
         dirs[LocationBuilder.EAST] = (LocationBuilder.isCombustable(currMap,r,CultimaPanel.equalizeWorldCol(c+1)));
         dirs[LocationBuilder.SOUTH] = (LocationBuilder.isCombustable(currMap,CultimaPanel.equalizeWorldRow(r+1),c));
         dirs[LocationBuilder.WEST] = (LocationBuilder.isCombustable(currMap,r,CultimaPanel.equalizeWorldCol(c-1)));
      }
      else
      {
         dirs[LocationBuilder.NORTH] = (r-1>=0 && LocationBuilder.isCombustable(currMap, r-1,c));
         dirs[LocationBuilder.EAST] = (c+1<currMap[0].length && LocationBuilder.isCombustable(currMap,r,c+1));
         dirs[LocationBuilder.SOUTH] = (r+1<currMap.length && LocationBuilder.isCombustable(currMap,r+1,c));
         dirs[LocationBuilder.WEST] = (c-1>=0 && LocationBuilder.isCombustable(currMap,r,c-1));
      }
      return dirs;
   }
   
   //used to see if we are next to a wood door - for axe breaking down
   public static boolean[] nextToAWoodDoor()
   {
      int r=CultimaPanel.player.getRow();
      int c=CultimaPanel.player.getCol();
      int mi=CultimaPanel.player.getMapIndex();
      boolean [] dirs = {false, false, false, false};   
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return dirs;
      byte[][]currMap = CultimaPanel.map.get(mi);
      dirs[LocationBuilder.NORTH] = (r-1>=0 && LocationBuilder.isWoodDoor(currMap, r-1,c));
      dirs[LocationBuilder.EAST] = (c+1<currMap[0].length && LocationBuilder.isWoodDoor(currMap,r,c+1));
      dirs[LocationBuilder.SOUTH] = (r+1<currMap.length && LocationBuilder.isWoodDoor(currMap,r+1,c));
      dirs[LocationBuilder.WEST] = (c-1>=0 && LocationBuilder.isWoodDoor(currMap,r,c-1));
      return dirs;
   }
   
  //used to see if we are next to a wood door - for npc werewolf breaking down
   public static boolean[] nextToAWoodDoor(int r, int c)
   {
      int mi=CultimaPanel.player.getMapIndex();
      boolean [] dirs = {false, false, false, false};   
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return dirs;
      byte[][]currMap = CultimaPanel.map.get(mi);
      dirs[LocationBuilder.NORTH] = (r-1>=0 && LocationBuilder.isWoodDoor(currMap, r-1,c));
      dirs[LocationBuilder.EAST] = (c+1<currMap[0].length && LocationBuilder.isWoodDoor(currMap,r,c+1));
      dirs[LocationBuilder.SOUTH] = (r+1<currMap.length && LocationBuilder.isWoodDoor(currMap,r+1,c));
      dirs[LocationBuilder.WEST] = (c-1>=0 && LocationBuilder.isWoodDoor(currMap,r,c-1));
      return dirs;
   }

   //used to see if we are next to a city wall to possibly pole vault over it
   //returns an array of booleans that will describe which direction the wall is so that we can move to it
   public static boolean[] nextToACityWall()
   {
      boolean [] dirs = {false, false, false, false}; 
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      if(!locType.contains("village") && !locType.contains("city") && !locType.contains("port") && !locType.contains("fortress") && !locType.contains("temple"))
      {
         return dirs;
      }
      boolean isTemple = (locType.contains("temple"));
      int r=CultimaPanel.player.getRow();
      int c=CultimaPanel.player.getCol();
      int mi=CultimaPanel.player.getMapIndex();  
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return dirs;
      byte[][]currMap = CultimaPanel.map.get(mi);
      dirs[LocationBuilder.NORTH] = (r-1>=0 && LocationBuilder.isCityWall(currMap, r-1,c)) || (isTemple && LocationBuilder.isTempleWall(currMap, r-1,c));
      dirs[LocationBuilder.EAST] = (c+1<currMap[0].length && LocationBuilder.isCityWall(currMap,r,c+1)) || (isTemple && LocationBuilder.isTempleWall(currMap, r,c+1));
      dirs[LocationBuilder.SOUTH] = (r+1<currMap.length && LocationBuilder.isCityWall(currMap,r+1,c)) || (isTemple && LocationBuilder.isTempleWall(currMap, r+1,c));
      dirs[LocationBuilder.WEST] = (c-1>=0 && LocationBuilder.isCityWall(currMap,r,c-1)) || (isTemple && LocationBuilder.isTempleWall(currMap, r,c-1));
      return dirs;
   }
   
   //used to see if we are next to a city wall to possibly pole vault over it
   public static boolean nextToACityWallSimple()
   {
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      if(!locType.contains("village") && !locType.contains("city") && !locType.contains("port") && !locType.contains("fortress") && !locType.contains("temple"))
      {
         return false;
      }
      boolean isTemple = (locType.contains("temple"));
      int r=CultimaPanel.player.getRow();
      int c=CultimaPanel.player.getCol();
      int mi=CultimaPanel.player.getMapIndex();  
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return false;
      byte[][]currMap = CultimaPanel.map.get(mi);
      if (r-1>=0 && (LocationBuilder.isCityWall(currMap, r-1,c) || (isTemple && LocationBuilder.isTempleWall(currMap, r-1,c))))
         return true;
      if (c+1<currMap[0].length && (LocationBuilder.isCityWall(currMap,r,c+1) || (isTemple && LocationBuilder.isTempleWall(currMap, r,c+1))))
         return true;
      if (r+1<currMap.length && (LocationBuilder.isCityWall(currMap,r+1,c) || (isTemple && LocationBuilder.isTempleWall(currMap, r+1,c))))
         return true;
      if (c-1>=0 && (LocationBuilder.isCityWall(currMap,r,c-1) || (isTemple && LocationBuilder.isTempleWall(currMap, r,c-1)) ))
         return true;
      return false;
   }
   
    //used to see if we are next to a counter (store or arena wall) to possibly pole vault over it
    //returns an array of booleans that will describe which direction the counter is so that we can move to it
   public static boolean[] nextToACounter()
   {
      boolean [] dirs = {false, false, false, false}; 
      int r=CultimaPanel.player.getRow();
      int c=CultimaPanel.player.getCol();
      int mi=CultimaPanel.player.getMapIndex();  
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return dirs;
      byte[][]currMap = CultimaPanel.map.get(mi);
      dirs[LocationBuilder.NORTH] = (r-1>=0 && CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase().contains("counter"));
      dirs[LocationBuilder.EAST] = (c+1<currMap[0].length && CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase().contains("counter"));
      dirs[LocationBuilder.SOUTH] = (r+1<currMap.length && CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase().contains("counter"));
      dirs[LocationBuilder.WEST] = (c-1>=0 && CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase().contains("counter"));
      return dirs;
   }
   
   //used to see if we are next to a counter (store or arena wall) to possibly pole vault over it
   public static boolean nextToACounterSimple()
   {
      int r=CultimaPanel.player.getRow();
      int c=CultimaPanel.player.getCol();
      int mi=CultimaPanel.player.getMapIndex();  
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return false;
      byte[][]currMap = CultimaPanel.map.get(mi);
      if (r-1>=0 && CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase().contains("counter"))
         return true;
      if (c+1<currMap[0].length && CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase().contains("counter"))
         return true;
      if (r+1<currMap.length && CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase().contains("counter"))
         return true;
      if (c-1>=0 && CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase().contains("counter"))
         return true;
      return false;
   }

//returns true if the player can break a wall they are next to
   public static boolean canBreakWall()
   {
      boolean flight = (CultimaPanel.player.getActiveSpell("Flight")!=null);
      String armName = CultimaPanel.player.getArmor().getName().toLowerCase();
      Player p = CultimaPanel.player;
      if(flight || armName.contains("cloak") || p.getLocationType().contains("pacrealm"))
         return false;
      if(p.getMapIndex()==0)
         return false;
         //next magic locked doors for the dungeon's 3 doors puzzle
      if(CultimaPanel.door1!=null && CultimaPanel.door2!=null && CultimaPanel.door3 != null && (p.getLocationType().toLowerCase().contains("dungeon") || CultimaPanel.player.getLocationType().toLowerCase().contains("cave")))
      {
         double d1dist = Display.distanceSimple((int)(CultimaPanel.door1.getRow()),(int)(CultimaPanel.door1.getCol()),p.getRow(),p.getCol());
         double d2dist = Display.distanceSimple((int)(CultimaPanel.door2.getRow()),(int)(CultimaPanel.door2.getCol()),p.getRow(),p.getCol());
         double d3dist = Display.distanceSimple((int)(CultimaPanel.door3.getRow()),(int)(CultimaPanel.door3.getCol()),p.getRow(),p.getCol());
         if(d1dist < 7.5 || d2dist < 7.5 || d3dist < 7.5)
            return false;
      }
         
      if(p.getImageIndex()==Player.AXE || p.getImageIndex()==Player.DUALAXE || p.getImageIndex()==Player.WOLF)
      {
         boolean[] doorDirs = nextToAWoodDoor();
         return (doorDirs[0] || doorDirs[1] || doorDirs[2] || doorDirs[3]);
      }
      if((p.getLocationType().contains("graboid") || p.getLocationType().contains("beast")) &&  //we can cut our way out of a graboid's innnards
          (p.getImageIndex()==Player.AXE || p.getImageIndex()==Player.DUALAXE || p.getImageIndex()==Player.DAGGER
         || p.getImageIndex()==Player.SPEAR || p.getImageIndex()==Player.SABER || p.getImageIndex()==Player.SWORDSHIELD
         || p.getImageIndex()==Player.DUAL || p.getImageIndex()==Player.WOLF))
      {
         boolean[] dirs = nextToAWall();
         return (dirs[0] || dirs[1] || dirs[2] || dirs[3]);
      }
      if(p.getImageIndex()!=Player.HAMMER && !p.getWeapon().getName().contains("Phasewall"))
         return false;
      boolean[] dirs = nextToAWall();
      return (dirs[0] || dirs[1] || dirs[2] || dirs[3]);
   }
   
   //returns true if the player can set something that they are next to on fire
   public static boolean canSetOnFire()
   {
      boolean flight = (CultimaPanel.player.getActiveSpell("Flight")!=null);
      String armName = CultimaPanel.player.getArmor().getName().toLowerCase();
      Player p = CultimaPanel.player;
      if(flight || armName.contains("invisibility-cloak") || CultimaPanel.player.isWolfForm() || CultimaPanel.player.isSailing() || p.getLocationType().contains("pacrealm"))
         return false; 
      boolean indoors = false;
      String locType = p.getLocationType().toLowerCase();
      if(locType.contains("underworld"))
         return false;     
      if(CultimaPanel.door1!=null && CultimaPanel.door2!=null && (CultimaPanel.door3 != null && locType.contains("dungeon") || CultimaPanel.door3 != null && locType.contains("cave")))
      {//next magic locked doors for the dungeon's 3 doors puzzle
         double d1dist = Display.distanceSimple((int)(CultimaPanel.door1.getRow()),(int)(CultimaPanel.door1.getCol()),p.getRow(),p.getCol());
         double d2dist = Display.distanceSimple((int)(CultimaPanel.door2.getRow()),(int)(CultimaPanel.door2.getCol()),p.getRow(),p.getCol());
         double d3dist = Display.distanceSimple((int)(CultimaPanel.door3.getRow()),(int)(CultimaPanel.door3.getCol()),p.getRow(),p.getCol());
         if(d1dist <= 1 || d2dist <= 1 || d3dist <= 1)
            return false;
      }
      if(locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair") || locType.contains("sewer"))
         indoors = true;
      if(CultimaPanel.player.getImageIndex()!=Player.TORCH && !CultimaPanel.player.getWeapon().getEffect().toLowerCase().contains("fire") && !CultimaPanel.player.getWeapon().getEffect().toLowerCase().contains("flame") && !CultimaPanel.player.getArmor().getName().toLowerCase().contains("holocaust-cloak"))
         return false;
      if(!indoors && CultimaPanel.weather > 1)
         return false;   
      boolean[] dirs = nextToACombustable();
      return (dirs[0] || dirs[1] || dirs[2] || dirs[3]);
   }

//removes the NPC at a specific location from the list where it was found (when an NPC is killed)
   public static void removeNPCat(int r, int c, int mi)
   {
      if(r < 0 || c < 0 || mi < 0)
         return;
      for(int i=0; i<CultimaPanel.civilians.get(mi).size(); i++)
      {
         NPCPlayer p = (CultimaPanel.civilians.get(mi)).get(i);
         if(p.getRow()==r && p.getCol()==c)
         {
            (CultimaPanel.civilians.get(mi)).remove(i);
            return;
         }
      }
      for(int i=0; i<CultimaPanel.furniture.get(mi).size(); i++)
      {
         NPCPlayer p = (CultimaPanel.furniture.get(mi)).get(i);
         if(p.getRow()==r && p.getCol()==c)
         {
            (CultimaPanel.furniture.get(mi)).remove(i);
            return;
         }
      }
      for(int i=0; i<CultimaPanel.worldMonsters.size(); i++)
      {
         NPCPlayer p = (CultimaPanel.worldMonsters).get(i);
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
         {
            (CultimaPanel.worldMonsters).remove(i);
            return;
         }
      }
      if(mi == 0)
      {
         for(int i=0; i<CultimaPanel.boats.size(); i++)
         {
            NPCPlayer p = (CultimaPanel.boats).get(i);
            if(p.getRow()==r && p.getCol()==c)
            {
               (CultimaPanel.boats).remove(i);
               return;
            }
         }
      }      
   }
   
//removes the NPC at a specific location in worldMonsters and adds them to civilians (used for ESCORT missions when complete)
   public static void moveWorldNpcToCivilians(int r, int c, int mi)
   {
      if(r < 0 || c < 0 || mi < 0)
         return;
      NPCPlayer npc = null;
      for(int i=0; i<CultimaPanel.worldMonsters.size(); i++)
      {
         NPCPlayer p = (CultimaPanel.worldMonsters).get(i);
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
         {
            npc = (CultimaPanel.worldMonsters).remove(i);
            break;
         }
      }
      if(npc != null)
      {
         npc.setMoveType(NPC.ROAM);
         (CultimaPanel.civilians.get(mi)).add(npc);
      }      
   }   
   
      //removes the statue at a specific location from the list where it was found (for Animate-stone spell)
   public static void removeStatueAt(int r, int c, int mi)
   {
      if(r < 0 || c < 0 || mi < 0)
         return;
      for(int i=0; i<CultimaPanel.civilians.get(mi).size(); i++)
      {
         NPCPlayer p = (CultimaPanel.civilians.get(mi)).get(i);
         if(p.getRow()==r && p.getCol()==c && p.isStatue())
         {
            p.damage(p.getBody(),"");
            return;
         }
      }
      for(int i=0; i<CultimaPanel.worldMonsters.size(); i++)
      {
         NPCPlayer p = (CultimaPanel.worldMonsters).get(i);
         if(p.getRow()==r && p.getCol()==c && p.isStatue())
         {
            p.damage(p.getBody(),"");
            return;
         }
      }      
   }

   public static NPCPlayer scanForNPC(int row, int col, int mi, int dir)
   {
      if(row < 0 || col < 0)
         return null;
      int numTimes = 0;
      byte[][]currMap = CultimaPanel.map.get(mi);	
      if(dir==LocationBuilder.NORTH)   
         for(int r=row-1; r>=row-CultimaPanel.SCREEN_SIZE/2; r--)
         {
            if(r<0)
            {
               if(mi==0)
                  r = CultimaPanel.equalizeWorldRow(r);
               else
                  return null;
            }
            if(LocationBuilder.isBlocking(currMap, r, col))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==r && p.getCol()==col)
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col)
                  return p;
            for(NPCPlayer p:CultimaPanel.furniture.get(mi))
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col)
                  return p;
            for(NPCPlayer p:CultimaPanel.boats)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col)
                  return p;      
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;      
         }
      if(dir==LocationBuilder.SOUTH)   
         for(int r=row+1; r<=row+CultimaPanel.SCREEN_SIZE/2; r++)
         {
            if(mi==0 && r>=CultimaPanel.map.get(0).length)
            {
               r = CultimaPanel.equalizeWorldRow(r);
            }
            if(LocationBuilder.isBlocking(currMap, r, col))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==r && p.getCol()==col)
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col)
                  return p;
            for(NPCPlayer p:CultimaPanel.furniture.get(mi))
               if(p.getRow()==r && p.getCol()==col)
                  return p;
            for(NPCPlayer p:CultimaPanel.boats)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col)
                  return p;      
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;
         }
      if(dir==LocationBuilder.EAST)   
         for(int c=col+1; c<=col+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            if(mi==0 && c>=CultimaPanel.map.get(0)[0].length)
            {
               c = CultimaPanel.equalizeWorldCol(c);
            }
            if(LocationBuilder.isBlocking(currMap, row, c))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==row && p.getCol()==c)
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c)
                  return p;
            for(NPCPlayer p:CultimaPanel.furniture.get(mi))
               if(p.getRow()==row && p.getCol()==c)
                  return p;
            for(NPCPlayer p:CultimaPanel.boats)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c)
                  return p;
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;
         }
      if(dir==LocationBuilder.WEST)   
         for(int c=col-1; c>=col-CultimaPanel.SCREEN_SIZE/2; c--)
         {
            if(c<0)
            {
               if(mi==0)
                  c = CultimaPanel.equalizeWorldCol(c);
               else
                  return null;
            }
            if(LocationBuilder.isBlocking(currMap, row, c))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==row && p.getCol()==c)
                  return p;   
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c)
                  return p;
            for(NPCPlayer p:CultimaPanel.furniture.get(mi))
               if(p.getRow()==row && p.getCol()==c)
                  return p;
            for(NPCPlayer p:CultimaPanel.boats)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c)
                  return p;
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;      
         }
      return null;
   }

//set fires for defensive fireshield spell
   public static void fireShield(int dir)
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      String locType = CultimaPanel.player.getLocationType();
      int range = CultimaPanel.player.getWeapon().getRange();
      byte[][]currMap = CultimaPanel.map.get(mi);	
      ArrayList<Coord> fireSpots = new ArrayList<Coord>();
      if(dir==LocationBuilder.NORTH)   
      {
         if(mi == 0)
         {
            for(int c=col-(range-1); c<=col+(range-1); c++)
            {
               if(c==col-(range-1) || c==col+(range-1))
                  fireSpots.add(new Coord(CultimaPanel.equalizeWorldRow(row-(range-1)), CultimaPanel.equalizeWorldCol(c)));
               else
                  fireSpots.add(new Coord(CultimaPanel.equalizeWorldRow(row-range), CultimaPanel.equalizeWorldCol(c)));
            }
         }
         else
         {
            for(int c=col-(range-1); c<=col+(range-1); c++)
            {
               if((c==col-(range-1) || c==col+(range-1)) && row-(range-1) >= 0 && c>=0 && c<currMap[0].length)
                  fireSpots.add(new Coord(row-(range-1), c));
               else if(row-range >= 0 && c>=0 && c<currMap[0].length)
                  fireSpots.add(new Coord(row-range, c));
            }
         }
      }
      else if(dir==LocationBuilder.SOUTH)   
      {
         if(mi == 0)
         {
            for(int c=col-(range-1); c<=col+(range-1); c++)
            {
               if(c==col-(range-1) || c==col+(range-1))
                  fireSpots.add(new Coord(CultimaPanel.equalizeWorldRow(row+(range-1)), CultimaPanel.equalizeWorldCol(c)));
               else
                  fireSpots.add(new Coord(CultimaPanel.equalizeWorldRow(row+range), CultimaPanel.equalizeWorldCol(c)));
            }
         }
         else
         {
            for(int c=col-(range-1); c<=col+(range-1); c++)
            {
               if((c==col-(range-1) || c==col+(range-1)) && row+(range-1) < currMap.length && c>=0 && c<currMap[0].length)
                  fireSpots.add(new Coord(row+(range-1), c));
               else if(row+range < currMap.length && c>=0 && c<currMap[0].length)
                  fireSpots.add(new Coord(row+range, c));
            }
         }
      }
      if(dir==LocationBuilder.WEST)   
      {
         if(mi == 0)
         {
            for(int r=row-(range-1); r<=row+(range-1); r++)
            {
               if(r==row-(range-1) || r==row+(range-1))
                  fireSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(col-(range-1))));
               else
                  fireSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(col-range)));
            }
         }
         else
         {
            for(int r=row-(range-1); r<=row+(range-1); r++)
            {
               if((r==row-(range-1) || r==row+(range-1)) && col-(range-1) >= 0 && r>=0 && r<currMap.length)
                  fireSpots.add(new Coord(r, col-(range-1)));
               else if(col-range >= 0 && r>=0 && r<currMap.length)
                  fireSpots.add(new Coord(r, col-range));
            }
         }
      }
      if(dir==LocationBuilder.EAST)   
      {
         if(mi == 0)
         {
            for(int r=row-(range-1); r<=row+(range-1); r++)
            {
               if(r==row-(range-1) || r==row+(range-1))
                  fireSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(col+(range-1))));
               else
                  fireSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(col+range)));
            }
         }
         else
         {
            for(int r=row-(range-1); r<=row+(range-1); r++)
            {
               if((r==row-(range-1) || r==row+(range-1)) && col+(range-1) <currMap[0].length && r>=0 && r<currMap.length)
                  fireSpots.add(new Coord(r, col+(range-1)));
               else if(col+range<currMap[0].length && r>=0 && r<currMap.length)
                  fireSpots.add(new Coord(r, col+range));
            }
         }
      }
      if(fireSpots.size() > 0)
      {
         for(Coord p:fireSpots)
         {
            int r = p.getRow();
            int c = p.getCol();
            Terrain spot = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
            String spotName = spot.getName().toLowerCase();
            if(spotName.contains("water"))
               continue;
            if(spotName.contains("_i_") && !LocationBuilder.isCombustable(spotName))
               continue;   
            if(spotName.contains("loc_"))
               continue;   
            CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, r, c, mi, locType));
         }
      }
   }
   
   //completely surround player with a ring of fire
   public static void surroundWithFire()
   {
      boolean allFireAreElementals = false;
      boolean someFireAreElementals = false;
      if(Math.random() < 0.25)
      {
         if(Math.random() < 0.25)
         {
            allFireAreElementals = true;
         }
         else
         {
            someFireAreElementals = true;
         }
      }
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      byte[][]currMap = CultimaPanel.map.get(mi);	
      String locType = CultimaPanel.player.getLocationType();
      ArrayList<Coord> fireSpots = new ArrayList<Coord>();
      int range = rollDie(2,4);
      if(allFireAreElementals || someFireAreElementals)
      {
         range = 4;
      }
      for(int r=row-(range+2); r<=row+(range+2); r++)
      {
         for(int c=col-(range+2); c<=col+(range+2); c++)
         {
            if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
               continue;
            int dist = (int)(Display.findDistance(row, col, r, c));
            if(dist==range)
               fireSpots.add(new Coord(r, c));
         }
      }
      if(fireSpots.size() > 0)
      {
         for(Coord p:fireSpots)
         {
            int r = p.getRow();
            int c = p.getCol();
            Terrain spot = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
            String spotName = spot.getName().toLowerCase();
            if(spotName.contains("water"))
               continue;
            if(spotName.contains("_i_") && !LocationBuilder.isCombustable(spotName))
               continue;   
            if(spotName.contains("loc_"))
               continue;   
            NPCPlayer thisFire = new NPCPlayer(NPC.FIRE, r, c, mi, locType);
            if(allFireAreElementals || (someFireAreElementals && Math.random() < 0.5))
            {
               thisFire.setNumInfo(Utilities.rollDie(1,2));
            }
            CultimaPanel.worldMonsters.add(thisFire);
         
         }
      }
   }
   
   public static void surroundWithFire(NPCPlayer target)
   {
      int row=target.getRow(), col=target.getCol(), mi=target.getMapIndex(); 
      byte[][]currMap = CultimaPanel.map.get(mi);	
      String locType = target.getLocationType();
      ArrayList<Coord> fireSpots = new ArrayList<Coord>();
      int range = rollDie(1,2);
      for(int r=row-(range+2); r<=row+(range+2); r++)
      {
         for(int c=col-(range+2); c<=col+(range+2); c++)
         {
            if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
               continue;
            int dist = (int)(Display.findDistance(row, col, r, c));
            if(dist==range)
               fireSpots.add(new Coord(r, c));
         }
      }
      if(fireSpots.size() > 0)
      {
         for(Coord p:fireSpots)
         {
            int r = p.getRow();
            int c = p.getCol();
            Terrain spot = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
            String spotName = spot.getName().toLowerCase();
            if(spotName.contains("water"))
               continue;
            if(spotName.contains("_i_") && !LocationBuilder.isCombustable(spotName))
               continue;   
            if(spotName.contains("loc_"))
               continue;   
            CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, r, c, mi, locType));
         }
      }
   }
   
   public static void explosion(byte[][]currMap, int row, int col, int mi, String locType)
   {
      ArrayList<Coord> fireSpots = new ArrayList<Coord>();
      int range = rollDie(1,2);
      for(int r=row-(range+2); r<=row+(range+2); r++)
      {
         for(int c=col-(range+2); c<=col+(range+2); c++)
         {
            if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
               continue;
            int dist = (int)(Display.findDistance(row, col, r, c));
            if(dist==range)
               fireSpots.add(new Coord(r, c));
            NPCPlayer target = getNPCAt(r, c, mi);
            if(target != null && target.getBody() > 0)
            {
               target.damage(rollDie(200,400), "fire");  
            }
         }
      }
      NPCPlayer centerFire = new NPCPlayer(NPC.FIRE, row, col, mi, locType);
      CultimaPanel.worldMonsters.add(centerFire);
      Color fire = Color.orange.brighter();
      int radius = Utilities.rollDie(10,15);
      int opacity = Utilities.rollDie(85,137);
      int ex = centerFire.getNpcX(), ey = centerFire.getNpcY();
      CultimaPanel.smoke.add(new SmokePuff(ex, ey, radius, opacity, fire));
      Sound.explosion();
      CultimaPanel.flashColor = Color.yellow;
      CultimaPanel.flashFrame = CultimaPanel.numFrames;
      CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
      if(fireSpots.size() > 0)
      {
         for(Coord p:fireSpots)
         {
            int r = p.getRow();
            int c = p.getCol();
            Terrain spot = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
            String spotName = spot.getName().toLowerCase();
            if(spotName.contains("water"))
               continue;
            if(spotName.contains("_i_") && !LocationBuilder.isCombustable(spotName))
               continue;   
            if(spotName.contains("loc_"))
               continue;   
            CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, r, c, mi, locType));
         }
      }
   }

  //completely surround player with a ring of raised stones
   public static void surroundWithStones()
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      byte[][]currMap = CultimaPanel.map.get(mi);	
      String locType = CultimaPanel.player.getLocationType();
      ArrayList<Coord> stoneSpots = new ArrayList<Coord>();
      int range = 4;
      int numMissing = Utilities.rollDie(1, 3);    //used to put some possible gaps in the stone arrangement
      for(int r=row-(range+2); r<=row+(range+2); r++)
      {
         for(int c=col-(range+2); c<=col+(range+2); c++)
         {
            if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
               continue;
            int dist = (int)(Display.findDistance(row, col, r, c));
            if(dist==range)
            {
               if(Math.random() < 0.25 && numMissing > 0)
               {  
                  numMissing--;
               }
               else
               {
                  stoneSpots.add(new Coord(r, c));
               }
            }
         }
      }
      if(stoneSpots.size() > 0)
      {
         for(Coord p:stoneSpots)
         {
            int r = p.getRow();
            int c = p.getCol();
            Terrain spot = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
            String spotName = spot.getName().toLowerCase();
            if(spotName.contains("water"))
               continue;
            if(spotName.contains("_i_") && !LocationBuilder.isCombustable(spotName))
               continue;   
            if(spotName.contains("loc_"))
               continue;   
            NPCPlayer thisStone = new NPCPlayer(NPC.STONE, r, c, mi, locType);
            CultimaPanel.worldMonsters.add(thisStone);
         }
      }
   }

   //creates a vortex 3 spaces ahead of the player
   public static void summonVortex(int dir)
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      String locType = CultimaPanel.player.getLocationType();
      int range = CultimaPanel.player.getWeapon().getRange();
      byte[][]currMap = CultimaPanel.map.get(mi);	
      ArrayList<Coord> vortexSpots = new ArrayList<Coord>();
      if(dir==LocationBuilder.NORTH)   
      {
         if(mi == 0)
            vortexSpots.add(new Coord(CultimaPanel.equalizeWorldRow(row-range), CultimaPanel.equalizeWorldCol(col)));
         else if(row-range >= 0)
            vortexSpots.add(new Coord(row-range, col));
      }
      else if(dir==LocationBuilder.SOUTH)   
      {
         if(mi == 0)
            vortexSpots.add(new Coord(CultimaPanel.equalizeWorldRow(row+range), CultimaPanel.equalizeWorldCol(col)));
         else if(row+range < currMap.length)
            vortexSpots.add(new Coord(row+range, col));
      }
      if(dir==LocationBuilder.WEST)   
      {
         if(mi == 0)
            vortexSpots.add(new Coord(CultimaPanel.equalizeWorldRow(row), CultimaPanel.equalizeWorldCol(col-range)));
         else if(col-range >= 0)
            vortexSpots.add(new Coord(row, col-range));
      }
      if(dir==LocationBuilder.EAST)   
      {
         if(mi == 0)
            vortexSpots.add(new Coord(CultimaPanel.equalizeWorldRow(row), CultimaPanel.equalizeWorldCol(col+range)));
         else if(col+range<currMap[0].length)
            vortexSpots.add(new Coord(row, col+range));
      }
      if(vortexSpots.size() > 0)
      {
         for(Coord p:vortexSpots)
         {
            int r = p.getRow();
            int c = p.getCol();
            Terrain spot = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
            String spotName = spot.getName().toLowerCase();
            if(spotName.contains("loc_"))
               continue;   
            NPCPlayer ourTornado = new NPCPlayer(NPC.TORNADO, r, c, mi, locType);
            ourTornado.setNumInfo(0);        //set fields in case we summoned an AIRELEMENTAL
            ourTornado.setMoveType(NPC.ROAM);
            ourTornado.addEffect("control");   
            CultimaPanel.worldMonsters.add(ourTornado);
         }
      }
   }
   
    //creates a stone-spire 1 space ahead of the player
   public static boolean raiseStone(int dir)
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      String locType = CultimaPanel.player.getLocationType();
      if(locType.toLowerCase().contains("beast"))
      {
         CultimaPanel.sendMessage("Spell failed!");
         return false;
      } 
      int range = CultimaPanel.player.getWeapon().getRange();
      byte[][]currMap = CultimaPanel.map.get(mi);	
      Coord stoneSpot = null;
      if(dir==LocationBuilder.NORTH)   
      {
         int stoneRow = row-range;
         int stoneCol = col;
         if(mi != 0)
         {
            if(stoneRow < 0)
            {
               CultimaPanel.sendMessage("Spell failed!");
               return false;
            }
         }
         else
         {
            stoneRow = CultimaPanel.equalizeWorldRow(row-range);
            stoneCol = CultimaPanel.equalizeWorldCol(col);
         }
         NPCPlayer pUp = getNPCAt(stoneRow, stoneCol, mi);
         if(pUp != null)
         {
            CultimaPanel.sendMessage("Spell failed!");
            return false;
         }
         stoneSpot = new Coord(stoneRow, stoneCol);
      }
      else if(dir==LocationBuilder.SOUTH)   
      {
         int stoneRow = row+range;
         int stoneCol = col;
         if(mi != 0)
         {
            if(stoneRow >= currMap.length)
            {
               CultimaPanel.sendMessage("Spell failed!");
               return false;
            }
         }
         else
         {
            stoneRow = CultimaPanel.equalizeWorldRow(row+range);
            stoneCol = CultimaPanel.equalizeWorldCol(col);
         }
         NPCPlayer pUp = getNPCAt(stoneRow, stoneCol, mi);
         if(pUp != null)
         {
            CultimaPanel.sendMessage("Spell failed!");
            return false;
         }
         stoneSpot = new Coord(stoneRow, stoneCol);
      }
      if(dir==LocationBuilder.WEST)   
      {
         int stoneRow = row;
         int stoneCol = col-range;
         if(mi != 0)
         {
            if(stoneCol < 0)
            {
               CultimaPanel.sendMessage("Spell failed!");
               return false;
            }
         }
         else
         {
            stoneRow = CultimaPanel.equalizeWorldRow(row);
            stoneCol = CultimaPanel.equalizeWorldCol(col-range);
         }
         NPCPlayer pUp = getNPCAt(stoneRow, stoneCol, mi);
         if(pUp != null)
         {               
            CultimaPanel.sendMessage("Spell failed!");
            return false;
         }
         stoneSpot = new Coord(stoneRow, stoneCol);
      }
      if(dir==LocationBuilder.EAST)   
      {
         int stoneRow = row;
         int stoneCol = col+range;
         if(mi != 0)
         {
            if(stoneCol > currMap[0].length)
            {
               CultimaPanel.sendMessage("Spell failed!");
               return false;
            }
         }
         else
         {
            stoneRow = CultimaPanel.equalizeWorldRow(row);
            stoneCol = CultimaPanel.equalizeWorldCol(col+range);
         }
         NPCPlayer pUp = getNPCAt(stoneRow, stoneCol, mi);
         if(pUp != null)
         {
            CultimaPanel.sendMessage("Spell failed!");
            return false;
         }
         stoneSpot = new Coord(stoneRow, stoneCol);
      }
      if(stoneSpot != null)
      {
         int r = stoneSpot.getRow();
         int c = stoneSpot.getCol();
         Terrain spot = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c]));
         String spotName = spot.getName().toLowerCase();
         if(spotName.contains("loc_"))
         {
            CultimaPanel.sendMessage("Spell failed!");
            return false;   
         }
         if(spotName.contains("lava"))
         {
            currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_$Black_floor").getValue();
         }
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.STONE, r, c, mi, locType));
      }
      return true;
   }
   
  //returns true if we are next to a stone that can be enchanted to become a monolith
   public static boolean nextToStone()
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      int range = 1;
      byte[][]currMap = CultimaPanel.map.get(mi);	
      //check north
      int stoneRow = row-range;
      int stoneCol = col;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row-range);
         stoneCol = CultimaPanel.equalizeWorldCol(col);
      }
      NPCPlayer pUp = getNPCAt(stoneRow, stoneCol, mi);
      if(pUp != null && pUp.getCharIndex()==NPC.STONE)
         return true;
      //check north-east
      stoneRow = row-range;
      stoneCol = col+range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row-range);
         stoneCol = CultimaPanel.equalizeWorldCol(col+range);
      }
      NPCPlayer pUpRight = getNPCAt(stoneRow, stoneCol, mi);
      if(pUpRight != null && pUpRight.getCharIndex()==NPC.STONE)
         return true;
     //check north-west
      stoneRow = row-range;
      stoneCol = col-range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row-range);
         stoneCol = CultimaPanel.equalizeWorldCol(col-range);
      }
      NPCPlayer pUpLeft = getNPCAt(stoneRow, stoneCol, mi);
      if(pUpLeft != null && pUpLeft.getCharIndex()==NPC.STONE)
         return true;
     //check south
      stoneRow = row+range;
      stoneCol = col;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row+range);
         stoneCol = CultimaPanel.equalizeWorldCol(col);
      }
      NPCPlayer pDown = getNPCAt(stoneRow, stoneCol, mi);
      if(pDown != null && pDown.getCharIndex()==NPC.STONE)
         return true;
         //check south-east
      stoneRow = row+range;
      stoneCol = col+range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row+range);
         stoneCol = CultimaPanel.equalizeWorldCol(col+range);
      }
      NPCPlayer pDownRight = getNPCAt(stoneRow, stoneCol, mi);
      if(pDownRight != null && pDownRight.getCharIndex()==NPC.STONE)
         return true;
         //check south-west
      stoneRow = row+range;
      stoneCol = col-range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row+range);
         stoneCol = CultimaPanel.equalizeWorldCol(col-range);
      }
      NPCPlayer pDownLeft = getNPCAt(stoneRow, stoneCol, mi);
      if(pDownLeft != null && pDownLeft.getCharIndex()==NPC.STONE)
         return true;
      //check west
      stoneRow = row;
      stoneCol = col-range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row);
         stoneCol = CultimaPanel.equalizeWorldCol(col-range);
      }
      NPCPlayer pLeft = getNPCAt(stoneRow, stoneCol, mi);
      if(pLeft != null && pLeft.getCharIndex()==NPC.STONE)
         return true;
      //check east
      stoneRow = row;
      stoneCol = col+range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row);
         stoneCol = CultimaPanel.equalizeWorldCol(col+range);
      }
      NPCPlayer pRight = getNPCAt(stoneRow, stoneCol, mi);
      if(pRight != null && pRight.getCharIndex()==NPC.STONE)
         return true;
   
      return false;
   }
   
  //changes surrounding stone-spires into monoliths
   public static boolean enchantStone()
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      int range = 1;
      byte[][]currMap = CultimaPanel.map.get(mi);	
      String locType = CultimaPanel.player.getLocationType();
      boolean success = false;
      //check north
      int stoneRow = row-range;
      int stoneCol = col;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row-range);
         stoneCol = CultimaPanel.equalizeWorldCol(col);
      }
      NPCPlayer pUp = getNPCAt(stoneRow, stoneCol, mi);
      if(pUp != null && pUp.getCharIndex()==NPC.STONE)
      {
         removeNPCat(stoneRow, stoneCol, mi);
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.MONOLITH, stoneRow, stoneCol, mi, locType));
         success = true;
      }
      //check north-east
      stoneRow = row-range;
      stoneCol = col+range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row-range);
         stoneCol = CultimaPanel.equalizeWorldCol(col+range);
      }
      NPCPlayer pUpRight = getNPCAt(stoneRow, stoneCol, mi);
      if(pUpRight != null && pUpRight.getCharIndex()==NPC.STONE)
      {
         removeNPCat(stoneRow, stoneCol, mi);
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.MONOLITH, stoneRow, stoneCol, mi, locType));
         success = true;
      }
      //check north-west
      stoneRow = row-range;
      stoneCol = col-range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row-range);
         stoneCol = CultimaPanel.equalizeWorldCol(col-range);
      }
      NPCPlayer pUpLeft = getNPCAt(stoneRow, stoneCol, mi);
      if(pUpLeft != null && pUpLeft.getCharIndex()==NPC.STONE)
      {
         removeNPCat(stoneRow, stoneCol, mi);
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.MONOLITH, stoneRow, stoneCol, mi, locType));
         success = true;
      }
     //check south
      stoneRow = row+range;
      stoneCol = col;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row+range);
         stoneCol = CultimaPanel.equalizeWorldCol(col);
      }
      NPCPlayer pDown = getNPCAt(stoneRow, stoneCol, mi);
      if(pDown != null && pDown.getCharIndex()==NPC.STONE)
      {
         removeNPCat(stoneRow, stoneCol, mi);
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.MONOLITH, stoneRow, stoneCol, mi, locType));
         success = true;
      }
      //check south-east
      stoneRow = row+range;
      stoneCol = col+range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row+range);
         stoneCol = CultimaPanel.equalizeWorldCol(col+range);
      }
      NPCPlayer pDownRight = getNPCAt(stoneRow, stoneCol, mi);
      if(pDownRight != null && pDownRight.getCharIndex()==NPC.STONE)
      {
         removeNPCat(stoneRow, stoneCol, mi);
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.MONOLITH, stoneRow, stoneCol, mi, locType));
         success = true;
      }
      //check south-west
      stoneRow = row+range;
      stoneCol = col-range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row+range);
         stoneCol = CultimaPanel.equalizeWorldCol(col-range);
      }
      NPCPlayer pDownLeft = getNPCAt(stoneRow, stoneCol, mi);
      if(pDownLeft != null && pDownLeft.getCharIndex()==NPC.STONE)
      {
         removeNPCat(stoneRow, stoneCol, mi);
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.MONOLITH, stoneRow, stoneCol, mi, locType));
         success = true;
      }
      //check west
      stoneRow = row;
      stoneCol = col-range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row);
         stoneCol = CultimaPanel.equalizeWorldCol(col-range);
      }
      NPCPlayer pLeft = getNPCAt(stoneRow, stoneCol, mi);
      if(pLeft != null && pLeft.getCharIndex()==NPC.STONE)
      {
         removeNPCat(stoneRow, stoneCol, mi);
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.MONOLITH, stoneRow, stoneCol, mi, locType));
         success = true;
      }
      //check east
      stoneRow = row;
      stoneCol = col+range;
      if(mi == 0)
      {
         stoneRow = CultimaPanel.equalizeWorldRow(row);
         stoneCol = CultimaPanel.equalizeWorldCol(col+range);
      }
      NPCPlayer pRight = getNPCAt(stoneRow, stoneCol, mi);
      if(pRight != null && pRight.getCharIndex()==NPC.STONE)
      {
         removeNPCat(stoneRow, stoneCol, mi);
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.MONOLITH, stoneRow, stoneCol, mi, locType));
         success = true;
      }
      return success;
   }

   public static boolean canPlaceTrap()
   {
      return (canPlaceTrap(LocationBuilder.NORTH) || canPlaceTrap(LocationBuilder.SOUTH) || canPlaceTrap(LocationBuilder.EAST) || canPlaceTrap(LocationBuilder.WEST));
   }

   //returns if we can drop a jaw trap in front of player
   public static boolean canPlaceTrap(int dir)
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      String locType = CultimaPanel.player.getLocationType();
      byte[][]currMap = CultimaPanel.map.get(mi);	
      if(dir==LocationBuilder.NORTH)
      {
         if(mi == 0)
            row = CultimaPanel.equalizeWorldRow(row-1);
         else
            row--;
      }
      else if(dir==LocationBuilder.SOUTH)
      {
         if(mi == 0)
            row = CultimaPanel.equalizeWorldRow(row+1);
         else
            row++;
      }
      else if(dir==LocationBuilder.WEST)
      {
         if(mi == 0)
            col = CultimaPanel.equalizeWorldCol(col-1);
         else
            col--;
      }
      else if(dir==LocationBuilder.EAST)
      {
         if(mi == 0)
            col = CultimaPanel.equalizeWorldCol(col+1);
         else
            col++;
      }
      if(LocationBuilder.outOfBounds(currMap, row, col))
         return false;
      if(jawTrapAt(row, col, mi))
         return false;
      String spot = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
      if(spot.contains("water") || spot.contains("lava") || spot.contains("bog") || LocationBuilder.isImpassable(currMap, row, col))
         return false;
      if(TerrainBuilder.habitablePlace(spot))   //in case the terrain tile is one of the world locations like a city tile
         return false;
      return true;
   }

      //drops a jaw trap in front of player
   public static void placeTrap(int dir)
   {
      if(!canPlaceTrap(dir))
         return;
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      String locType = CultimaPanel.player.getLocationType();
      byte[][]currMap = CultimaPanel.map.get(mi);	
      if(dir==LocationBuilder.NORTH)
      {
         if(mi == 0)
            row = CultimaPanel.equalizeWorldRow(row-1);
         else
            row--;
      }
      else if(dir==LocationBuilder.SOUTH)
      {
         if(mi == 0)
            row = CultimaPanel.equalizeWorldRow(row+1);
         else
            row++;
      }
      else if(dir==LocationBuilder.WEST)
      {
         if(mi == 0)
            col = CultimaPanel.equalizeWorldCol(col-1);
         else
            col--;
      }
      else if(dir==LocationBuilder.EAST)
      {
         if(mi == 0)
            col = CultimaPanel.equalizeWorldCol(col+1);
         else
            col++;
      }
      NPCPlayer trap = new NPCPlayer(NPC.JAWTRAP, row, col, mi, locType);
      CultimaPanel.corpses.get(trap.getMapIndex()).add(trap.getCorpse());
      CultimaPanel.player.discardCurrentWeapon();
   }
      
  //pre:  p != null
  //post: returns true if p is in range for us to attack
   public static boolean isNPCinRangeToAttack(NPCPlayer p, Weapon weapType)
   { 
      if(p.getCharIndex()==NPC.TORNADO || p.getCharIndex()==NPC.WHIRLPOOL || p.getCharIndex()==NPC.WILLOWISP || p.getCharIndex()==NPC.MAZEMOUTH || p.getCharIndex()==NPC.SPITFIRE)
         return false;
      if(p.getCharIndex()==NPC.GHOST && p.getLocationType().contains("pacrealm") && System.currentTimeMillis() > CultimaPanel.alertTime + 6000)
         return false;   
      boolean flight = (CultimaPanel.player.getActiveSpell("Flight")!=null);
      String armName = CultimaPanel.player.getArmor().getName().toLowerCase();
      if(flight || armName.contains("cloak") || p.isCaged())
         return false;
      if(p.isOurDog())              //don't target dog if it is ours
      {
         return false;  
      }
      if(p.isEscortedNpc())         //don't target escorted npc if it is ours
      {
         return false;
      }     
      int row = CultimaPanel.player.getRow();
      int col = CultimaPanel.player.getCol();
      int pr = p.getRow();
      int pc = p.getCol();
      int pmi = p.getMapIndex();
      int mi = CultimaPanel.player.getMapIndex();
      if(mi != pmi)              //in different maps
         return false;
      if(row!=pr && col!=pc)         //not aligned in the same row or col
         return false;
      int range = weapType.getRange();
      double dist = Display.findDistance(p.getRow(), p.getCol(), row, col);
      if(dist > range)           //out of range to attack
         return false;
      //see if there is anything blocking between player and p in each direction
      byte[][]currMap = CultimaPanel.map.get(mi);
      int numTimes = 0;	
      //(dir==LocationBuilder.NORTH)   
      for(int r=row-1; r>=row-CultimaPanel.SCREEN_SIZE/2 && r>=row-range; r--)
      {
         if(r<0)
         {
            if(mi==0)
               r = CultimaPanel.equalizeWorldRow(r);
            else
               break;
         }
         if(LocationBuilder.isBlocking(currMap, r, col))
            break;
         if(p.getRow()==r && p.getCol()==col)
            return true;
         numTimes++;
         if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
            break;      
      }
      //(dir==LocationBuilder.SOUTH)   
      numTimes = 0;
      for(int r=row+1; r<=row+CultimaPanel.SCREEN_SIZE/2 && r<=row+range; r++)
      {
         if(mi==0 && r>=CultimaPanel.map.get(0).length)
         {
            r = CultimaPanel.equalizeWorldRow(r);
         }
         if(LocationBuilder.isBlocking(currMap, r, col))
            break;
         if(p.getRow()==r && p.getCol()==col)
            return true;
         numTimes++;
         if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
            break;      
      }
      //(dir==LocationBuilder.EAST)  
      numTimes = 0; 
      for(int c=col+1; c<=col+CultimaPanel.SCREEN_SIZE/2 && c<=col+range; c++)
      {
         if(mi==0 && c>=CultimaPanel.map.get(0)[0].length)
         {
            c = CultimaPanel.equalizeWorldCol(c);
         }
         if(LocationBuilder.isBlocking(currMap, row, c))
            break;
         if(p.getRow()==row && p.getCol()==c)
            return true;
         numTimes++;
         if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
            break;
      }
      //(dir==LocationBuilder.WEST)  
      numTimes = 0; 
      for(int c=col-1; c>=col-CultimaPanel.SCREEN_SIZE/2 && c>=col-range; c--)
      {
         if(c<0)
         {
            if(mi==0)
               c = CultimaPanel.equalizeWorldCol(c);
            else
               break;
         }
         if(LocationBuilder.isBlocking(currMap, row, c))
            break;
         if(p.getRow()==row && p.getCol()==c)
            return true;
         numTimes++;
         if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
            break;
      }
      return false;
   }


//given a direction from a player, returns the enitity that is within attack distance from the player
   public static NPCPlayer scanForNPCinRange(int dir, boolean isAltFire)
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      int range = CultimaPanel.player.getWeapon().getRange();
      Weapon altFireWeapon = Weapon.getAltFire(CultimaPanel.player.getWeapon().getName());
      if(isAltFire && altFireWeapon != null)
      {
         range = altFireWeapon.getRange();
      }
      int numTimes = 0;
      byte[][]currMap = CultimaPanel.map.get(mi);	
      if(dir==LocationBuilder.NORTH)   
         for(int r=row-1; r>=row-CultimaPanel.SCREEN_SIZE/2 && r>=row-range; r--)
         {
            if(r<0)
            {
               if(mi==0)
                  r = CultimaPanel.equalizeWorldRow(r);
               else
                  return null;
            }
            if(LocationBuilder.isBlocking(currMap, r, col))
               return null;
            for(NPCPlayer p:CultimaPanel.AllNPCinSight)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL && p.getCharIndex()!=NPC.WILLOWISP && p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.SPITFIRE)
               {
                  if(p.isOurDog() || p.isEscortedNpc())                    //no targeting our own dog or escorted npc
                  {}
                  else
                     return p;
               }   
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;      
         }
      if(dir==LocationBuilder.SOUTH)   
         for(int r=row+1; r<=row+CultimaPanel.SCREEN_SIZE/2 && r<=row+range; r++)
         {
            if(mi==0 && r>=CultimaPanel.map.get(0).length)
            {
               r = CultimaPanel.equalizeWorldRow(r);
            }
            if(LocationBuilder.isBlocking(currMap, r, col))
               return null;
            for(NPCPlayer p:CultimaPanel.AllNPCinSight)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL && p.getCharIndex()!=NPC.WILLOWISP && p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.SPITFIRE)
               {
                  //if((dog!=null && p==dog) || (npc!=null && p==npc))       //no targeting our own dog or escorted npc
                  if(p.isOurDog() || p.isEscortedNpc())
                  {}
                  else
                     return p;
               }  
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;
         }
      if(dir==LocationBuilder.EAST)   
         for(int c=col+1; c<=col+CultimaPanel.SCREEN_SIZE/2 && c<=col+range; c++)
         {
            if(mi==0 && c>=CultimaPanel.map.get(0)[0].length)
            {
               c = CultimaPanel.equalizeWorldCol(c);
            }
            if(LocationBuilder.isBlocking(currMap, row, c))
               return null;
            for(NPCPlayer p:CultimaPanel.AllNPCinSight)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL && p.getCharIndex()!=NPC.WILLOWISP && p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.SPITFIRE)
               {
                  //if((dog!=null && p==dog) || (npc!=null && p==npc))       //no targeting our own dog or escorted npc
                  if(p.isOurDog() || p.isEscortedNpc())
                  {}
                  else
                     return p;
               }
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;
         }
      if(dir==LocationBuilder.WEST)   
         for(int c=col-1; c>=col-CultimaPanel.SCREEN_SIZE/2 && c>=col-range; c--)
         {
            if(c<0)
            {
               if(mi==0)
                  c = CultimaPanel.equalizeWorldCol(c);
               else
                  return null;
            }
            if(LocationBuilder.isBlocking(currMap, row, c))
               return null;
            for(NPCPlayer p:CultimaPanel.AllNPCinSight)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL && p.getCharIndex()!=NPC.WILLOWISP && p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.SPITFIRE)
               {
                  //if((dog!=null && p==dog) || (npc!=null && p==npc))       //no targeting our own dog or escorted npc
                  if(p.isOurDog() || p.isEscortedNpc())
                  {}
                  else
                     return p;
               }
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;      
         }
      return null;
   }
   
   //given a direction from a player, returns the enitity that is within range distance from the player
   public static NPCPlayer scanForNPCinRange(int dir, int range)
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      int numTimes = 0;
      byte[][]currMap = CultimaPanel.map.get(mi);	
      if(dir==LocationBuilder.NORTH)   
         for(int r=row-1; r>=row-CultimaPanel.SCREEN_SIZE/2 && r>=row-range; r--)
         {
            if(r<0)
            {
               if(mi==0)
                  r = CultimaPanel.equalizeWorldRow(r);
               else
                  return null;
            }
            if(LocationBuilder.isBlocking(currMap, r, col))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==r && p.getCol()==col)
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL && p.getCharIndex()!=NPC.WILLOWISP && p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.SPITFIRE)
               {
                  if(p.isOurDog() || p.isEscortedNpc())
                  {}
                  else
                     return p;
               }
            for(NPCPlayer p:CultimaPanel.furniture.get(mi))
               if(p.getRow()==r && p.getCol()==col)
                  return p;
            if(mi == 0)
            {   
               for(NPCPlayer p:CultimaPanel.boats)
                  if(p.getRow()==r && p.getCol()==col)
                  {
                     return p;
                  }
            }   
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;      
         }
      if(dir==LocationBuilder.SOUTH)   
         for(int r=row+1; r<=row+CultimaPanel.SCREEN_SIZE/2 && r<=row+range; r++)
         {
            if(mi==0 && r>=CultimaPanel.map.get(0).length)
            {
               r = CultimaPanel.equalizeWorldRow(r);
            }
            if(LocationBuilder.isBlocking(currMap, r, col))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==r && p.getCol()==col)
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL && p.getCharIndex()!=NPC.WILLOWISP && p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.SPITFIRE)
               {
                  //no targeting our own dog or escorted npc
                  if(p.isOurDog() || p.isEscortedNpc())
                  {}
                  else
                     return p;
               }
            for(NPCPlayer p:CultimaPanel.furniture.get(mi))
               if(p.getRow()==r && p.getCol()==col)
                  return p;
            if(mi == 0)
            {   
               for(NPCPlayer p:CultimaPanel.boats)
                  if(p.getRow()==r && p.getCol()==col)
                  {
                     return p;
                  }
            }
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;
         }
      if(dir==LocationBuilder.EAST)   
         for(int c=col+1; c<=col+CultimaPanel.SCREEN_SIZE/2 && c<=col+range; c++)
         {
            if(mi==0 && c>=CultimaPanel.map.get(0)[0].length)
            {
               c = CultimaPanel.equalizeWorldCol(c);
            }
            if(LocationBuilder.isBlocking(currMap, row, c))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==row && p.getCol()==c)
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL && p.getCharIndex()!=NPC.WILLOWISP && p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.SPITFIRE)
               {
                  //no targeting our own dog or escorted npc
                  if(p.isOurDog() || p.isEscortedNpc())
                  {}
                  else
                     return p;
               }
            for(NPCPlayer p:CultimaPanel.furniture.get(mi))
               if(p.getRow()==row && p.getCol()==c)
                  return p;
            if(mi == 0)
            {   
               for(NPCPlayer p:CultimaPanel.boats)
                  if(p.getRow()==row && p.getCol()==c)
                  {
                     return p;
                  }
            }
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;
         }
      if(dir==LocationBuilder.WEST)   
         for(int c=col-1; c>=col-CultimaPanel.SCREEN_SIZE/2 && c>=col-range; c--)
         {
            if(c<0)
            {
               if(mi==0)
                  c = CultimaPanel.equalizeWorldCol(c);
               else
                  return null;
            }
            if(LocationBuilder.isBlocking(currMap, row, c))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==row && p.getCol()==c)
                  return p;   
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL && p.getCharIndex()!=NPC.WILLOWISP && p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.SPITFIRE)
               {
                  //no targeting our own dog or escorted npc
                  if(p.isOurDog() || p.isEscortedNpc())
                  {}
                  else
                     return p;
               }
            for(NPCPlayer p:CultimaPanel.furniture.get(mi))
               if(p.getRow()==row && p.getCol()==c)
                  return p;
            if(mi == 0)
            {   
               for(NPCPlayer p:CultimaPanel.boats)
                  if(p.getRow()==row && p.getCol()==c)
                  {
                     return p;
                  }
            }
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;      
         }
      return null;
   }

//given a direction from a player, returns the enitity that is within 1 from the player
   public static NPCPlayer scanForNPCtoPickpocket(int dir)
   {
      int row=CultimaPanel.player.getRow(), col=CultimaPanel.player.getCol(), mi=CultimaPanel.player.getMapIndex(); 
      int range = 1;
      int numTimes = 0;
      byte[][]currMap = CultimaPanel.map.get(mi);	
      if(dir==LocationBuilder.NORTH)   
         for(int r=row-1; r>=row-CultimaPanel.SCREEN_SIZE/2 && r>=row-range; r--)
         {
            if(r<0)
            {
               if(mi==0)
                  r = CultimaPanel.equalizeWorldRow(r);
               else
                  return null;
            }
            if(LocationBuilder.isBlocking(currMap, r, col))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==r && p.getCol()==col && !p.isStatue() && !p.isNonPlayer())
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col && !NPC.isNatural(p.getCharIndex()) && p.getCharIndex()!=NPC.WILLOWISP && !p.isStatue() && !p.isNonPlayer())
                  return p;
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;      
         }
      if(dir==LocationBuilder.SOUTH)   
         for(int r=row+1; r<=row+CultimaPanel.SCREEN_SIZE/2 && r<=row+range; r++)
         {
            if(mi==0 && r>=CultimaPanel.map.get(0).length)
            {
               r = CultimaPanel.equalizeWorldRow(r);
            }
            if(LocationBuilder.isBlocking(currMap, r, col))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==r && p.getCol()==col && !p.isStatue() && !p.isNonPlayer())
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==r && p.getCol()==col && !NPC.isNatural(p.getCharIndex()) && p.getCharIndex()!=NPC.WILLOWISP && !p.isStatue() && !p.isNonPlayer())
                  return p;
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;
         }
      if(dir==LocationBuilder.EAST)   
         for(int c=col+1; c<=col+CultimaPanel.SCREEN_SIZE/2 && c<=col+range; c++)
         {
            if(mi==0 && c>=CultimaPanel.map.get(0)[0].length)
            {
               c = CultimaPanel.equalizeWorldCol(c);
            }
            if(LocationBuilder.isBlocking(currMap, row, c))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==row && p.getCol()==c && !p.isStatue() && !p.isNonPlayer())
                  return p;
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c && !NPC.isNatural(p.getCharIndex()) && p.getCharIndex()!=NPC.WILLOWISP && !p.isStatue() && !p.isNonPlayer())
                  return p;
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;
         }
      if(dir==LocationBuilder.WEST)   
         for(int c=col-1; c>=col-CultimaPanel.SCREEN_SIZE/2 && c>=col-range; c--)
         {
            if(c<0)
            {
               if(mi==0)
                  c = CultimaPanel.equalizeWorldCol(c);
               else
                  return null;
            }
            if(LocationBuilder.isBlocking(currMap, row, c))
               return null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               if(p.getRow()==row && p.getCol()==c && !p.isStatue() && !p.isNonPlayer())
                  return p;   
            for(NPCPlayer p:CultimaPanel.worldMonsters)
               if(p.getMapIndex()==mi && p.getRow()==row && p.getCol()==c && !NPC.isNatural(p.getCharIndex()) && p.getCharIndex()!=NPC.WILLOWISP && !p.isStatue() && !p.isNonPlayer())
                  return p;
            numTimes++;
            if(numTimes >= CultimaPanel.SCREEN_SIZE/2+1)
               return null;      
         }
      return null;
   }
     
   public static boolean chestInRange()
   {
      if(scanForChest(LocationBuilder.NORTH)!=null || scanForChest(LocationBuilder.SOUTH)!=null || scanForChest(LocationBuilder.EAST)!=null || scanForChest(LocationBuilder.WEST)!=null)
         return true;
      return false;
   }

//returns [0]-the distance from the closest trap, trapped chest or door, and [1]-the distance from the closest secret wall
   public static int[] scanForTrapOrSecret()
   {
      int[] ans = {-1,-1};
      int row = CultimaPanel.player.getRow();
      int col = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();
      byte[][]currMap = (CultimaPanel.map.get(mi));
      int range = CultimaPanel.player.getAwareness();
      for(int r=row-range; r<=row+range; r++)
      {
         for(int c=col-range; c<=col+range; c++)
         {
            if(r<0 || c<0 || r>=currMap.length || c>=currMap.length)
               continue;
            String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(terr.contains("trap"))
            {
               int dist = (int)(Display.findDistance(row, col, r, c));
               if((ans[0]==-1 || dist < ans[0]) && dist<=range)
                  ans[0] = dist;
            }
            if(terr.contains("_d_") && (terr.contains("rock") || terr.contains("wall")))
            {
               int dist = (int)(Display.findDistance(row, col, r, c));
               if((ans[0]==-1 || dist < ans[0]) && dist<=range)
                  ans[1] = dist;
            }
         }
      }
      return ans;
   }

//given a direction, returns the location and type of a chest if there is one within two spaces of player
   public static Object[] scanForChest(int dir)
   {
      int r = CultimaPanel.player.getRow();
      int c = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();
      byte[][]currMap = (CultimaPanel.map.get(mi));   
      Terrain chest = TerrainBuilder.getTerrainWithName("ITM_D_$Chest");
      Terrain chestTrap = TerrainBuilder.getTerrainWithName("ITM_D_T_$Chest_trap");
      Terrain book = TerrainBuilder.getTerrainWithName("ITM_D_$Book");
   
      Object[]ans = new Object[2];
   
      if(dir==LocationBuilder.NORTH)  
      {
         if(r-1 >= 0 && (Math.abs(currMap[r-1][c])==chest.getValue() || Math.abs(currMap[r-1][c])==chestTrap.getValue() || Math.abs(currMap[r-1][c])==book.getValue()))
         {
            ans[0] = new Coord(r-1, c);
            ans[1] = ((byte)(Math.abs(currMap[r-1][c])));
            return ans;
         }
         else if(r-2 >= 0 && (Math.abs(currMap[r-2][c])==chest.getValue() || Math.abs(currMap[r-2][c])==chestTrap.getValue() || Math.abs(currMap[r-2][c])==book.getValue()))
         {
            ans[0] = new Coord(r-2, c);
            ans[1] = ((byte)(Math.abs(currMap[r-2][c])));
            return ans;
         }
      }
      else if(dir==LocationBuilder.SOUTH)  
      {
         if(r+1 < currMap.length && (Math.abs(currMap[r+1][c])==chest.getValue() || Math.abs(currMap[r+1][c])==chestTrap.getValue() || Math.abs(currMap[r+1][c])==book.getValue()))
         {
            ans[0] = new Coord(r+1, c);
            ans[1] = ((byte)(Math.abs(currMap[r+1][c])));
            return ans;           
         }
         else if(r+2 < currMap.length && (Math.abs(currMap[r+2][c])==chest.getValue() || Math.abs(currMap[r+2][c])==chestTrap.getValue() || Math.abs(currMap[r+2][c])==book.getValue()))
         {
            ans[0] = new Coord(r+2, c);
            ans[1] = ((byte)(Math.abs(currMap[r+2][c])));
            return ans;
         }
      } 
      else if(dir==LocationBuilder.WEST)  
      {
         if(c-1 >= 0 && (Math.abs(currMap[r][c-1])==chest.getValue() || Math.abs(currMap[r][c-1])==chestTrap.getValue() || Math.abs(currMap[r][c-1])==book.getValue()))
         {
            ans[0] = new Coord(r, c-1);
            ans[1] = ((byte)(Math.abs(currMap[r][c-1])));
            return ans;
         }
         else if(c-2 >= 0 && (Math.abs(currMap[r][c-2])==chest.getValue() || Math.abs(currMap[r][c-2])==chestTrap.getValue() || Math.abs(currMap[r][c-2])==book.getValue()))
         {
            ans[0] = new Coord(r, c-2);
            ans[1] = ((byte)(Math.abs(currMap[r][c-2])));
            return ans;
         }
      }
      else if(dir==LocationBuilder.EAST)  
      {
         if(c+1 < currMap[0].length && (Math.abs(currMap[r][c+1])==chest.getValue() || Math.abs(currMap[r][c+1])==chestTrap.getValue() || Math.abs(currMap[r][c+1])==book.getValue()))
         {
            ans[0] = new Coord(r, c+1);
            ans[1] = ((byte)(Math.abs(currMap[r][c+1])));
            return ans;
         }
         else if(c+2 < currMap[0].length && (Math.abs(currMap[r][c+2])==chest.getValue() || Math.abs(currMap[r][c+2])==chestTrap.getValue() || Math.abs(currMap[r][c+2])==book.getValue()))
         {
            ans[0] = new Coord(r, c+2);
            ans[1] = ((byte)(Math.abs(currMap[r][c+2])));
            return ans;
         }
      }
      return null;
   }
   
   //returns an egg if there exists one at a particular location
   public static RestoreItem getEggAt(int r, int c, int mi)
   {
      if(r < 0 || c < 0 || mi < 0 || mi >= CultimaPanel.map.size()) 
         return null;   
      for(RestoreItem ri: CultimaPanel.eggsToHarvest)
         if(ri.getMapIndex()==mi && ri.getRow()==r && ri.getCol()==c)
            return ri;
      return null;
   }

  //removes an egg if there exists one at a particular location
   public static boolean removeEggAt(int r, int c, int mi)
   {
      if(r < 0 || c < 0 || mi < 0 || mi >= CultimaPanel.map.size()) 
         return false;   
      for(int i=CultimaPanel.eggsToHarvest.size()-1; i>=0; i--)
      {
         RestoreItem ri = CultimaPanel.eggsToHarvest.get(i);
         if(ri.getMapIndex()==mi && ri.getRow()==r && ri.getCol()==c)
         {
            ri.setValue((byte)(-1 * ri.getValue()));
            ri.setRestoreDay(CultimaPanel.days + rollDie(30,60));
            return true;
         }
      }
      return false;
   }

//returns true if there is an NPC player at row r, col c at that mapIndex
   public static boolean NPCAt(int r, int c, int mi)
   {
      if(r < 0 || c < 0)
         return false;
      for(NPCPlayer p:CultimaPanel.civilians.get(mi))
         if(p.getRow()==r && p.getCol()==c)
            return true;
      for(NPCPlayer p:CultimaPanel.worldMonsters)
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
            return true;
      for(NPCPlayer p:CultimaPanel.furniture.get(mi))
         if(p.getRow()==r && p.getCol()==c)
            return true;
      if(mi == 0)
      {   
         for(NPCPlayer p:CultimaPanel.boats)
            if(p.getRow()==r && p.getCol()==c)
               return true;
      }
      return false;
   }
   
   //returns true if there is an NPC player at row r, col c at that mapIndex
   //used to see if another npc can move to that location so it ignores pellets in Pac-Realm
   public static boolean NPCAtMoveTo(int r, int c, int mi)
   {
      if(r < 0 || c < 0)
         return false;
      for(NPCPlayer p:CultimaPanel.civilians.get(mi))
         if(p.getRow()==r && p.getCol()==c && !p.getLocationType().contains("pacrealm"))
            return true;
      for(NPCPlayer p:CultimaPanel.worldMonsters)
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
            return true;
      for(NPCPlayer p:CultimaPanel.furniture.get(mi))
         if(p.getRow()==r && p.getCol()==c)
            return true;
      if(mi == 0)
      {   
         for(NPCPlayer p:CultimaPanel.boats)
            if(p.getRow()==r && p.getCol()==c)
               return true;
      }
      return false;
   }

   //returns NPCPlayer if there is an NPC player at row r, col c at that mapIndex from the collecton of NPCs within screen range
   public static NPCPlayer getNPCAtFromScreenRange(int r, int c, int mi)
   {
      if(r < 0 || c < 0)
         return null;
      for(NPCPlayer p:CultimaPanel.AllNPCinRange)
         if(p.getRow()==r && p.getCol()==c)
            return p;
      return null;
   }

//returns NPCPlayer if there is an NPC player at row r, col c at that mapIndex
   public static NPCPlayer getNPCAt(int r, int c, int mi)
   {
      if(r < 0 || c < 0)
         return null;
      for(NPCPlayer p:CultimaPanel.civilians.get(mi))
         if(p.getRow()==r && p.getCol()==c)
            return p;
      for(NPCPlayer p:CultimaPanel.worldMonsters)
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
            return p;
      for(NPCPlayer p:CultimaPanel.furniture.get(mi))
         if(p.getRow()==r && p.getCol()==c)
            return p;
      if(mi == 0)
      {   
         for(NPCPlayer p:CultimaPanel.boats)
            if(p.getRow()==r && p.getCol()==c)
               return p;
      }
      return null;
   }

//returns true if there is an NPC player at row r, col c at that mapIndex
   public static boolean NPCAt(ArrayList<NPCPlayer> NPCs, int r, int c, int mi)
   {
      if(r < 0 || c < 0)
         return false;
      for(NPCPlayer p:NPCs)
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
            return true;
      return false;
   }

//returns true if there is an NPC player at row r, col c at that mapIndex, but does not include our dog or escorted npc
//also ignores NPCs that are short or not flying, when the player is
//used for restricting or allowing player movement into (r,c) in CultimaPanel
   public static boolean NPCAtMinusDog(ArrayList<NPCPlayer> NPCs, int r, int c, int mi)
   {
      if(r < 0 || c < 0)
         return false;
      boolean flight = false;
      if(CultimaPanel.player.getActiveSpell("Flight")!=null)
         flight = true;
   
      for(NPCPlayer p:NPCs)
      {
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
         {
            if(p.isOurDog())
               continue;
            if(p.isEscortedNpc())
               continue;   
            if(flight && !p.canFly() && NPC.getSize(p.getCharIndex()) < 2)
               continue;
            if(!flight && p.canFly() && NPC.getSize(p.getCharIndex()) < 2)
               continue;
            if(p.getCharIndex()==NPC.FIRE && CultimaPanel.player.getArmor().getName().toLowerCase().contains("holocaust-cloak"))
               continue;   //walk thru fire with a holocaust cloak
            if(p.getCharIndex()==NPC.FIRE && p.getPercentHealth() < 33)
               continue;   //walk thru a smaller fire    
            if(p.isNonPlayer() || NPC.isEtheral(p.getCharIndex()))
               continue;   //walk thru temporary entities used to mark mission targets (to find with Knowing spell)     
            return true;
         }
      }
      return false;
   }

//returns charIndex if there is a boardable ship at row r, col c at that mapIndex, -1 if not
   public static byte shipAt(ArrayList<NPCPlayer> NPCs, int r, int c, int mi)
   {
      if(CultimaPanel.player.isSailing())
         return -1;           //we can't board a ship from another ship
      if(r < 0 || c < 0)
         return -1;
      for(NPCPlayer p:NPCs)
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
         {
            if(NPC.isShip(p) && (p.getBody() < CultimaPanel.player.getBody() || p.hasMet()))
               return p.getCharIndex();
         }
      return -1;
   }

//returns true if there is a tamable horse at row r, col c at that mapIndex, false if not
   public static boolean horseAt(ArrayList<NPCPlayer> NPCs, int r, int c, int mi)
   {
      if(CultimaPanel.player.onHorse())
         return false;           //we can't ride a horse from another horse
      if(r < 0 || c < 0)
         return false;
      for(NPCPlayer p:NPCs)
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
         {
            if(!NPC.isHorse(p) || p.isStatue())  //not a horse or statue
               continue;
            if(horseCanBeTamed(p))
               return true;
         }
      return false;
   }

//returns horse if there is a tamable horse at row r, col c at that mapIndex, null if not
   public static NPCPlayer getHorseAt(ArrayList<NPCPlayer> NPCs, int r, int c, int mi)
   {
      if(CultimaPanel.player.onHorse())
         return null;           //we can't ride a horse from another horse
      if(r < 0 || c < 0)
         return null;
      for(NPCPlayer p:NPCs)
         if(p.getRow()==r && p.getCol()==c && p.getMapIndex()==mi)
         {
            if(!NPC.isHorse(p) || p.isStatue())  //not a horse or statue
               continue;
            if(horseCanBeTamed(p))
               return p;
         }
      return null;
   }
   
//returns true if horse can be tamed
   public static boolean horseCanBeTamed(NPCPlayer p)
   {
      if(p.hasBeenAttacked())
         return false;
      if(p.isStatue())   //statue
         return false;  
      if(p.getCharIndex()==NPC.UNICORN && p.hasMet() && p.getReputation()%2==0)
         return true;
      if(p.getCharIndex()==NPC.UNICORN && (CultimaPanel.player.getReputation() < 0 || CultimaPanel.player.numFlowers() < 5 || p.getReputation()%2!=0))
         return false;     //unicorns with an odd reputation are not ridable - you have to feed them flowers to make their repuation even and ridable
      if(p.hasMet())
         return true;   
      if(CultimaPanel.player.getImageIndex()==Player.LUTE && !CultimaPanel.player.isOnGuard())
         return true;   
      if(CultimaPanel.player.getImageIndex()!=Player.NONE)
         return false;   
      if((p.getReputation() < 0 && CultimaPanel.player.getReputation() < 0) || (p.getReputation() >= 0 && CultimaPanel.player.getReputation() >= 0))
         return true;
      if((CultimaPanel.player.getActiveSpell("Charm")!=null || CultimaPanel.player.hasItem("charmring")) && p.getReputation() >= (-100 - (CultimaPanel.player.getMind()*10)) )
         return true;
      return false;
   }

//returns true if there is a vision blocking Terrain between (r1,c1) and (r2, c2) - assuming they are in the same row or col (for attacking)
   public static boolean blockedBetween(int r1, int c1, int r2, int c2)
   {
      if(r1!=r2 && c1!=c2)
         return false;   
      if(r1==r2)     //in the same row, check left-to-right and right-to-left
      {
         if(Math.abs(c1-c2)==1)  //players are 1 space away: there is no terrain tile between them
            return false;
         if(c1 < c2) //check left-to-right
         {
            for(int col=c1+1; col<c2; col++)
            {
               String terr = CultimaPanel.allTerrain.get(Math.abs(CultimaPanel.map.get(CultimaPanel.player.getMapIndex())[r1][col])).getName();
               if(terr.contains("_B_"))
                  return true;
            }
         }
         else        //check right-to-left
         {
            for(int col=c1-1; col>c2; col--)
            {
               String terr = CultimaPanel.allTerrain.get(Math.abs(CultimaPanel.map.get(CultimaPanel.player.getMapIndex())[r1][col])).getName();
               if(terr.contains("_B_"))
                  return true;
            }
         }
      }
      else           //in the same col, chek up-to-down and down-to-up
      {
         if(Math.abs(r1-r2)==1)  //players are 1 space away: there is no terrain tile between them
            return false;
         if(r1 < r2) //check up-to-down
         {
            for(int row=r1+1; row<r2; row++)
            {
               String terr = CultimaPanel.allTerrain.get(Math.abs(CultimaPanel.map.get(CultimaPanel.player.getMapIndex())[row][c1])).getName();
               if(terr.contains("_B_"))
                  return true;
            }
         }
         else        //check down-to-up
         {
            for(int row=r1-1; row>r2; row--)
            {
               String terr = CultimaPanel.allTerrain.get(Math.abs(CultimaPanel.map.get(CultimaPanel.player.getMapIndex())[row][c1])).getName();
               if(terr.contains("_B_"))
                  return true;
            }
         }
      }
      return false;
   }
   
   public static boolean nextToADoor(int r, int c)
   {
      int mi=CultimaPanel.player.getMapIndex();
      boolean [] dirs = {false, false, false, false};   
      if(r < 0 || c < 0 || mi<0 || mi>CultimaPanel.map.size())
         return false;
      byte[][]currMap = CultimaPanel.map.get(mi);
      dirs[LocationBuilder.NORTH] = (r-1>=0 && LocationBuilder.isDoor(currMap, r-1,c));
      dirs[LocationBuilder.EAST] = (c+1<currMap[0].length && LocationBuilder.isDoor(currMap,r,c+1));
      dirs[LocationBuilder.SOUTH] = (r+1<currMap.length && LocationBuilder.isDoor(currMap,r+1,c));
      dirs[LocationBuilder.WEST] = (c-1>=0 && LocationBuilder.isDoor(currMap,r,c-1));
      return dirs[LocationBuilder.NORTH] || dirs[LocationBuilder.EAST] || dirs[LocationBuilder.SOUTH] || dirs[LocationBuilder.WEST];
   }
   
   //returns an array of booleans as to whether or not the player is row or col adjacent to a barrel that can be pulled or pushed [NORTH, EAST, SOUTH, WEST]
   public static boolean[] nextToABarrel()
   {
      int r=CultimaPanel.player.getRow();
      int c=CultimaPanel.player.getCol();
      int mi=CultimaPanel.player.getMapIndex();
      boolean [] dirs = {false, false, false, false};   
      if(r < 0 || c < 0 || mi==0)
         return dirs;
      byte[][]currMap = CultimaPanel.map.get(mi);
      for(NPCPlayer p:CultimaPanel.nonAliveNPCsInOurMap)
      {
         if(p!=null && NPC.isFurniture(p))
         {  
            dirs[LocationBuilder.NORTH] = dirs[LocationBuilder.NORTH] || (r-1>=0 && p.getRow() == r-1 && p.getCol() == c);
            dirs[LocationBuilder.EAST] = dirs[LocationBuilder.EAST] || (c+1<currMap[0].length && p.getCol() == c+1 && p.getRow() == r);
            dirs[LocationBuilder.SOUTH] = dirs[LocationBuilder.SOUTH] || (r+1<currMap.length && p.getRow() == r+1 && p.getCol() == c);
            dirs[LocationBuilder.WEST] = dirs[LocationBuilder.WEST] || (c-1>=0 && p.getCol() == c-1 && p.getRow() == r);
         }
      }
      return dirs;
   }

   //returns whether or not the player is row or col adjacent to a barrel that can be pulled or pushed
   public static boolean nextToABarrelSimple()
   {
      boolean [] dirs = nextToABarrel();   
      return dirs[LocationBuilder.NORTH] || dirs[LocationBuilder.EAST] || dirs[LocationBuilder.SOUTH] || dirs[LocationBuilder.WEST];
   }
   
   //returns the distance to the closest prisoner even if out of sight, -1 if there is no prisoner on the map
   //to be used to have prisoners call out for help - more often as you get closer
   public static double distToClosestPrisoner()
   {
      Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      String thisLocName = thisLoc.getName().toLowerCase();
      boolean inCamp = (CultimaPanel.player.getLocationType().contains("camp") || thisLocName.endsWith(" camp"));
      double dist = Double.MAX_VALUE;
      for(NPCPlayer p:CultimaPanel.humanNPCsInOurMap)
      {
         if(p!=null)
         { 
            if(p.getMapIndex() != CultimaPanel.player.getMapIndex())
               continue;
            inCamp = inCamp || p.getLocationType().contains("camp");
            if((p.getCharIndex() == NPC.BEGGER || (inCamp && p.isWerewolf())))
            {  
               double thisDist = Display.distanceSimple(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
               if(thisDist < dist && p.isCaged())//nextToADoor(p.getRow(), p.getCol()))
               {  //make sure the prisoner is next to a door - we only want them pleading for help if beind a closed door
                  dist = thisDist;
               }
            }
         }
      }
      if(dist == Double.MAX_VALUE)    //no prisoners found
      {
         return -1;
      }
      return dist;
   }
   
   //returns the distance to the barkeep to see if they sing the lumberjack song when you have axes out, Double.MAX_VALUE if there is no barkeep on the map
   public static double distToBarkeep()
   {
      double dist = Double.MAX_VALUE;
      for(NPCPlayer p:CultimaPanel.humanNPCsInOurMap)
      {
         if(p!=null && p.getName().toLowerCase().contains("barkeep"))
         {  
            if(p.getMapIndex() != CultimaPanel.player.getMapIndex())
               continue;
            double thisDist = Display.distanceSimple(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
            if(thisDist < dist)
            {  
               dist = thisDist;
            }
         }
      }
      return dist;
   }
   
  //returns the distance to the closest begger in sight and their reputation, or someone poisoned, -1 if there is no begger or poisoned person on the map
  //to be used to have beggers call out for charity when you get close, or a poisoned person to remark on their condition
  //in the returned array, index 0 is the distance to a begger (-1 if none), index 1 is their reputation (0 if none), index 2 is the distance to a poisoned person (-1 if none)
  //index 3 is the name of the closest NPC
  //This will also return the distance to a shopkeep so that they can make a comment about their wares when you get close
   public static Object[] distToClosestBeggerOrPoisoned()
   {
      double distToBegger = Double.MAX_VALUE;
      double distToPoisoned = Double.MAX_VALUE;
      int reputation = 0;
      String npcName = "";
      boolean hasMet = false;
      Object [] retValue = new Object[5];
      retValue[0] = -1.0;
      retValue[1] = 0;
      retValue[2] = -1.0;
      retValue[3] = "";
      retValue[4] = false;
      for(NPCPlayer p:CultimaPanel.NPCinSight)
      {
         if(p!=null)
         {
            if(p.getMapIndex() != CultimaPanel.player.getMapIndex())
               continue;
            double thisDist = Display.distanceSimple(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
            if(p.isPoisoned() && !p.getName().contains("Minion"))
            {
               if(thisDist < distToPoisoned)
               { 
                  distToPoisoned = thisDist;
                  npcName = p.getName();
                  hasMet = p.hasMet();
               }
            }
            else if((p.getCharIndex() == NPC.BEGGER && !p.getLocationType().contains("temple") && !p.getLocationType().contains("arena")) || p.getName().toLowerCase().contains("cultist") || isShopkeep(p.getName()))
            {  
               if(thisDist < distToBegger)
               { 
                  distToBegger = thisDist;
                  reputation = p.getReputation();
                  npcName = p.getName();
                  hasMet = p.hasMet();
               }
            }
         }
      }
      if(distToBegger == Double.MAX_VALUE && distToPoisoned==Double.MAX_VALUE)    //no prisoners found
      {
         return retValue;
      }
      retValue[0] = distToBegger;
      retValue[1] = reputation;
      retValue[2] = distToPoisoned;
      retValue[3] = npcName;
      retValue[4] = hasMet;
      return retValue;
   }
   
   public static void purgeCultists(int mapIndex)
   {
      for(int j=CultimaPanel.civilians.get(mapIndex).size()-1; j>=0; j--)
      {
         NPCPlayer curr = CultimaPanel.civilians.get(mapIndex).get(j);   
         if(curr.getName().toLowerCase().contains("cultist"))
         {
            curr.damage(1000,"fire");
            NPCPlayer fire = new NPCPlayer(NPC.FIRE, curr.getRow(), curr.getCol(), curr.getMapIndex(), curr.getLocationType());
            fire.setBody(Utilities.rollDie(5,25));
            CultimaPanel.worldMonsters.add(fire);
            CultimaPanel.numChants = -1;
         }
      }
   }
   
   //remove a mission from the mission stack and clear that mission from the NPC that gave it
   public static void removeMission(int missionIndex)
   {
      String toRemove = CultimaPanel.missionStack.remove(missionIndex).getInfo();
      for(ArrayList<NPCPlayer> c:CultimaPanel.civilians)
      {   
         boolean found = false;
         for(NPCPlayer p: c)
         {
            if(p.getMissionInfo().equals(toRemove))
            {
               p.setMissionInfo("none");
               if(p.getNumInfo()==10)
                  p.setNumInfo(0); 
               found = true;
               break;   
            }
         }
         if(found)
         {
            break;
         }
      }  
      FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
   }
   
   public static boolean NPCisInSight(NPCPlayer npc)
   {
      for(NPCPlayer p:CultimaPanel.NPCinSight)
         if(p.getName().equals(npc.getName()))
            return true;
      return false;
   }

//returns a collection of all non-statue, non-zombie npcs at a certain mapindex for selecting mission targets
   public static ArrayList<NPCPlayer> getNPCsInLoc(int mi)
   {
      ArrayList<NPCPlayer> ans = new ArrayList<NPCPlayer>();
      for(NPCPlayer p: CultimaPanel.civilians.get(mi))
      {
         if(NPC.isCivilian(p))
            ans.add(p);
      }
      return ans;
   }
   
   //returns a collection of all non-statue, non-zombie npcs at a certain mapindex for selecting mission targets, excluding a certain type
   public static ArrayList<NPCPlayer> getNPCsInLoc(int mi, int exclude)
   {
      ArrayList<NPCPlayer> ans = new ArrayList<NPCPlayer>();
      for(NPCPlayer p: CultimaPanel.civilians.get(mi))
      {
         if(NPC.isCivilian(p) && p.getCharIndex() != exclude)
            ans.add(p);
      }
      return ans;
   }
   
            //returns a collection of all non-statue, non-zombie npcs at a certain mapindex for selecting mission targets, excluding a certain NPC name
   public static ArrayList<NPCPlayer> getNPCsInLoc(int mi, String excludeName)
   {
      ArrayList<NPCPlayer> ans = new ArrayList<NPCPlayer>();
      for(NPCPlayer p: CultimaPanel.civilians.get(mi))
      {
         String pName = p.getName();
         if(NPC.isCivilian(p) && !pName.equalsIgnoreCase(excludeName))
            ans.add(p);
      }
      return ans;
   }
   
      //returns a collection of all non-statue, non-zombie npcs at a certain mapindex for selecting mission targets, excluding a certain type and NPC name
   public static ArrayList<NPCPlayer> getNPCsInLoc(int mi, int exclude, String excludeName)
   {
      ArrayList<NPCPlayer> ans = new ArrayList<NPCPlayer>();
      for(NPCPlayer p: CultimaPanel.civilians.get(mi))
      {
         String pName = p.getName();
         if(NPC.isCivilian(p) && p.getCharIndex() != exclude && !pName.equalsIgnoreCase(excludeName))
            ans.add(p);
      }
      return ans;
   }
   
   //returns a collection of all non-statue, non-zombie npcs at a certain mapindex for selecting mission targets, excluding a certain type and NPC name
   public static ArrayList<NPCPlayer> getNPCsInLoc(int mi, int exclude1, int exclude2, String excludeName)
   {
      ArrayList<NPCPlayer> ans = new ArrayList<NPCPlayer>();
      for(NPCPlayer p: CultimaPanel.civilians.get(mi))
      {
         String pName = p.getName();
         if(NPC.isCivilian(p) && p.getCharIndex() != exclude1 && p.getCharIndex() != exclude2 && !pName.equalsIgnoreCase(excludeName))
            ans.add(p);
      }
      return ans;
   }
   
//NPC Players will more according to move type STILL, ROAM, MARCH, CHASE or RUN and their agility compared to Player
   public static void moveNPCs(boolean keyHit)
   {
      Player player = CultimaPanel.player;
      int mi = player.getMapIndex();
      String locType = player.getLocationType().toLowerCase();
      if(locType.contains("pacrealm"))
      {
         pacRealmMovement();
      }   
      else
      {
         byte[][]currMap = (CultimaPanel.map.get(mi));   
         String playerSpot = CultimaPanel.allTerrain.get(Math.abs(currMap[player.getRow()][player.getCol()])).getName().toLowerCase();
         Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
         String thisLocName = thisLoc.getName().toLowerCase();
         boolean inCamp = (locType.contains("camp") || thisLocName.endsWith(" camp"));
      
         boolean fear = ( player.getActiveSpell("Fear")!=null || player.getWeapon().getName().contains("Bane") || player.getArmor().getName().contains("Holocaust"));
         boolean firestorm = (player.getActiveSpell("Firestorm")!=null);
         NPCPlayer monster = CultimaPanel.monsterAndCivInView;
         NPCPlayer guardMonsterTarget = CultimaPanel.monsterForGuardTarget;
      
         boolean helpMessage = false;     //used to trigger no more than 1 help message per call to the method
         ArrayList<NPCPlayer> civsInLoc = (CultimaPanel.civilians.get(mi));
         CultimaPanel.distToLute = Double.MAX_VALUE;
         for(int i=civsInLoc.size()-1; i>=0; i--)
         {
            NPCPlayer p = civsInLoc.get(i);   
         
            if(p.getBody() <= 0)
               continue;
            if(p.getCharIndex()==NPC.MONOLITH)
            {
               monolithAttack(p);
               continue;
            } 
            if(p.isStatue() || p.getCharIndex()==NPC.BARREL)     
               continue;
            if(p.isNonPlayer())
            {                   
               continue; 
            }
            if(LocationBuilder.outOfBounds(currMap, p.getRow(), p.getCol()))
               continue;
         
            String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][p.getCol()])).getName().toLowerCase();
            if(curr.contains("poison") && !Display.isWinter() && !p.hasEffect("poison") && !p.canFly() && Math.random() < 0.1)
            {  //if p is standing in a poison bog, chance they will be poisoned
               p.addEffect("poison");
            }
         
            if(p.hasEffect("still") || p.hasEffect("web"))
               continue; 
            if(p.hasEffect("freeze") && Math.random() < 0.75)
               continue; 
             
            inCamp = inCamp || p.getLocationType().contains("camp");   
            boolean guardDefender = false;      //is the current NPC a guard in a city that will attack a monster in town
                  
            if(removeStuckNPCs(p,  i, (CultimaPanel.civilians.get(mi))))
            {
               continue;
            }  
            if(checkTraps(p))
            {                 //see if the npc steps into a trap
               continue;
            }  
            double distToPlayer = Display.findDistance(p.getRow(), p.getCol(), player.getRow(), player.getCol());
            if(p.getCharIndex()==NPC.LUTE && distToPlayer <  CultimaPanel.distToLute)
            {                 //record distance to lute player to govern how loud the music is in Sound.java
               CultimaPanel.distToLute = distToPlayer;
            }
            double size = NPC.getSize(p);
            if(size == 1.5)   //make it so range 1 weapons in the hand of large beasts has a range of 2
               size = 2;
            double weaponRange = Math.max(p.getWeapon().getRange(), size);
         
            if(nocturnalCheck(p, i, (CultimaPanel.civilians.get(mi))))
            {
               continue;
            }
            if(NPC.isGuard(p.getCharIndex()) && guardMonsterTarget!=null && (NPC.getSize(guardMonsterTarget.getCharIndex()) < 1.5 || p.getCharIndex()==NPC.ROYALGUARD || p.getReputation()>-10) && !guardMonsterTarget.getName().toLowerCase().contains("sibling") && !guardMonsterTarget.getName().toLowerCase().contains("brute"))
            {  //if this is a guard in a city and a monster walks in, any roaming guard will search for it unless they are mission borne monsters (those are our responsibility)
               guardDefender = true;
            } 
         
            helpMessage = setMovementType(p, helpMessage, guardDefender);
         
            if(p.getMoveType()==NPC.STILL && !guardDefender)
               continue;
            if(p.isTalking())
               continue;
            if(p.getRow() < 0 || p.getCol() < 0 || p.getRow() >= currMap.length || p.getCol() >= currMap[0].length)
               continue;
            if(p.getMoveType()==NPC.RUN && distToPlayer > (CultimaPanel.SCREEN_SIZE/2+1))
               continue;   
            if(guardDefender || p.getMoveType()==NPC.ROAM || (p.getMoveType()>=NPC.MARCH_NORTH  && p.getMoveType()<=NPC.MARCH_WEST) || player.isInvisible())
            {
               int delayTime = (CultimaPanel.animDelay*3);
               if(guardDefender)
               {
                  delayTime = (int)(CultimaPanel.animDelay*1.5);
               }
               if(CultimaPanel.numFrames % delayTime != 0)  
                  continue; 
            }
            if(mi > 0 && p.getMoveType()==NPC.RUN && (p.getRow()<=0 || p.getCol()<=0 || p.getRow()>=currMap.length-1 || p.getCol()>=currMap[0].length-1)) 
            {     //fleeing NPCs that leave a location are removed
               if(NPC.isCivilian(p) && TerrainBuilder.habitablePlace(p.getLocationType()))
               {  //after a while, NPCs will repopulate a city
                  NPCPlayer temp = p.clone();
                  temp.setRestoreDay(CultimaPanel.days+rollDie(1,30));
                  CultimaPanel.NPCsToRestore.add(temp);
               }
            //remove any current missions with players in this location since they are dead
               String NPCmiss = p.getMissionInfo();
               if(!NPCmiss.equals("none"))
               {
                  for(int missIndex=0; missIndex<CultimaPanel.missionStack.size(); missIndex++)
                  {
                     Mission m = CultimaPanel.missionStack.get(missIndex);
                     if(NPCmiss.equals(m.getInfo()))
                     {
                        CultimaPanel.missionStack.remove(missIndex);
                        FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
                        break;
                     }
                  }
               }
               for(int missIndex=0; missIndex<CultimaPanel.missionStack.size(); missIndex++)
               {
                  Mission m = CultimaPanel.missionStack.get(missIndex);
                  if(m.getTarget() != null)
                  {
                     String targetName = m.getTarget().getName().replace("-bounty","").replace("-head","");
                     if(Utilities.shortName(p.getName()).contains(targetName))
                     {
                        CultimaPanel.missionStack.remove(missIndex);
                        FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
                        break;
                     }
                  }
               }
               (CultimaPanel.civilians.get(mi)).remove(i);
               continue;
            }
            if(keyHit)     //make speed of NPC chasing or running depend on agility difference
            {
               String playerTerrain = CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.player.getRow()][CultimaPanel.player.getCol()])).getName().toLowerCase();
               if(!p.canFly() && !p.canSwim() && curr.contains("_v_") && !playerTerrain.contains("_v_")) 
                  continue;   //no extra movement if the npc is on very slow terrain and the player is not
               else if(!p.canFly() && !p.canSwim() && curr.contains("_s_") && !playerTerrain.contains("_v_") && !playerTerrain.contains("_s_"))
                  continue;   //no extra movement if the npc is on slow terrain and the player is not   
               else if( (Math.abs(p.getRow()-player.getRow())== 1 && p.getCol()==player.getCol()) || (Math.abs(p.getCol()-player.getCol())== 1 && p.getRow()==player.getRow()))
                  continue;   //if p is one step away from player and in attack range, continue
               else
               {
                  int agilityDiff = player.getAgility() - p.getAgility();
                  if(agilityDiff < 0 && (rollDie(100) < Math.abs(agilityDiff)*2) && (p.getMoveType()==NPC.RUN || p.getMoveType()==NPC.CHASE))
                  { }
                  else
                     if(CultimaPanel.numFrames % (CultimaPanel.animDelay*3/2) != 0)  
                        continue;
               }
            }  
            else if(p.getMoveType()==NPC.RUN || p.getMoveType()==NPC.CHASE)
            {
               if(CultimaPanel.numFrames % (CultimaPanel.animDelay*3/2) != 0)  
                  continue; 
            }
            else if(p.getMoveType()==NPC.FOLLOW)
            {
               if(CultimaPanel.numFrames % (CultimaPanel.animDelay*6) != 0)  
                  continue; 
            }
         //guards that are roaming have been bribed and should be removed for the day after they leave the viewing screen   
         //a numInfo of 13 is the tag that a guard has been bribed
            if(NPC.isGuard(p.getCharIndex()) && p.getMoveType()==NPC.ROAM && p.getNumInfo()==13 && Display.findDistance(p.getRow(), p.getCol(), player.getRow(), player.getCol()) > (CultimaPanel.SCREEN_SIZE/2+1))
            {
               (CultimaPanel.civilians.get(mi)).remove(i);
               continue;
            } 
         
            affectTerrain(p);    //big monsters might change the terrain by walking on it
         
            NPCPlayer target = closestEnemyNPCTarget(p);
         
            guardDefender = guardDefender || (NPC.isGuard(p.getCharIndex()) && target!=null && inCamp); 
            boolean pIsFreedCreature = false;
            if(target != null && inCamp)
            {
               if(p.getCharIndex()==NPC.DOG && p.hasMet())
               {  //just get the dog to follow us
                  target = null;
               }
               pIsFreedCreature = areEnemies(p, target) && !NPC.isAnyBrigand(p.getCharIndex()) && !NPC.isGuard(p);
            }
            double distToTarg = Double.MAX_VALUE;
            if(target != null)
            {
               distToTarg = Display.findDistance(p.getRow(), p.getCol(), target.getRow(), target.getCol());
            } 
            if(target != null && distToTarg <= weaponRange)
            {
               p.attack(target);
               continue;
            }
            if(guardDefender && monster != null && Display.findDistance(p.getRow(), p.getCol(), monster.getRow(), monster.getCol()) <= weaponRange)
            {
               p.attack(monster);
               continue;
            }
            if(p.getMoveType()==NPC.MARCH_NORTH && p.getRow()-1>=0 && !guardDefender)
            {
               int r = p.getRow();
               int c = p.getCol();
               String up = CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase();
               boolean badUp = (up.contains("water") || up.contains("door") || (up.contains("_d_") && (up.contains("wall") || up.contains("rock"))));
               if((player.getRow()==r-1 && player.getCol()==c))
               {//don't move into the postition where the player or other NPCs are
                  if(System.currentTimeMillis() > CultimaPanel.cheerTime + 5000)
                  {
                     CultimaPanel.sendMessage("~"+NPC.guardBlock(p));
                     CultimaPanel.cheerTime = System.currentTimeMillis();
                  }
               }    
               else if(p.getRow() <= 2 || !LocationBuilder.canMoveTo(currMap, r-1, c) || badUp /*|| NPCAtMoveTo(r-1, c, p.getMapIndex())*/)
                  p.setMoveType(NPC.MARCH_SOUTH);
               else
                  p.moveNorth();   
            }
            else if(p.getMoveType()==NPC.MARCH_SOUTH && p.getRow()+1<currMap.length && !guardDefender)
            {
               int r = p.getRow();
               int c = p.getCol();
               String down = CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase();
               boolean badDown = (down.contains("water") || down.contains("door") || (down.contains("_d_") && (down.contains("wall") || down.contains("rock"))));
               if((player.getRow()==r+1 && player.getCol()==c))
               {//don't move into the postition where the player or other NPCs are
                  if(System.currentTimeMillis() > CultimaPanel.cheerTime + 5000)
                  {
                     CultimaPanel.sendMessage("~"+NPC.guardBlock(p));
                     CultimaPanel.cheerTime = System.currentTimeMillis();
                  }
               } 
               else if(p.getRow() >= currMap.length - 2 || !LocationBuilder.canMoveTo(currMap, r+1, c) || badDown /*|| NPCAtMoveTo(r+1, c, p.getMapIndex())*/)
                  p.setMoveType(NPC.MARCH_NORTH);
               else
                  p.moveSouth();   
            } 
            else if(p.getMoveType()==NPC.MARCH_EAST && p.getCol()+1<currMap[0].length && !guardDefender)
            {
               int r = p.getRow();
               int c = p.getCol();
               String right = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase();
               boolean badRight = (right.contains("water") || right.contains("door") || (right.contains("_d_") && (right.contains("wall") || right.contains("rock"))));
               if((player.getRow()==r && player.getCol()==c+1))
               {//don't move into the postition where the player or other NPCs are
                  if(System.currentTimeMillis() > CultimaPanel.cheerTime + 5000)
                  {
                     CultimaPanel.sendMessage("~"+NPC.guardBlock(p));
                     CultimaPanel.cheerTime = System.currentTimeMillis();
                  }
               }    
               else if(p.getCol() >= currMap[0].length - 2 || !LocationBuilder.canMoveTo(currMap, r, c+1) || badRight /*|| NPCAtMoveTo(r, c+1, p.getMapIndex())*/)
                  p.setMoveType(NPC.MARCH_WEST);
               else
                  p.moveEast();   
            }
            else if(p.getMoveType()==NPC.MARCH_WEST && p.getCol()-1>=0 && !guardDefender)
            {
               int r = p.getRow();
               int c = p.getCol();
               String left = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase();
               boolean badLeft = (left.contains("water") || left.contains("door") || (left.contains("_d_") && (left.contains("wall") || left.contains("rock"))));
               if((player.getRow()==r && player.getCol()==c-1))
               {//don't move into the postition where the player or other NPCs are
                  if(System.currentTimeMillis() > CultimaPanel.cheerTime + 5000)
                  {
                     CultimaPanel.sendMessage("~"+NPC.guardBlock(p));
                     CultimaPanel.cheerTime = System.currentTimeMillis();
                  }
               } 
               else if(p.getCol() <= 2 || !LocationBuilder.canMoveTo(currMap, r, c-1) || badLeft /*|| NPCAtMoveTo(r, c-1, p.getMapIndex())*/)
                  p.setMoveType(NPC.MARCH_EAST);
               else
                  p.moveWest();   
            }
            else if(p.getMoveType()==NPC.ROAM || p.hasEffect("control") || guardDefender || pIsFreedCreature)
            {
               if(p.hasEffect("control") || pIsFreedCreature)
               {
                  if(target != null && !target.hasEffect("control") && distToTarg <= weaponRange)
                  {
                     p.attack(target);
                     continue;
                  }
               }
               if(Math.random() < 0.5 || p.hasEffect("control") || guardDefender || pIsFreedCreature)
               {
                  int dir = (int)(Math.random()*4);
                  if(guardDefender && monster!=null)
                     dir = TerrainBuilder.getDirectionTowardsSimple(p.getRow(), p.getCol(), monster.getRow(), monster.getCol());
                  else if(p.hasEffect("control") || p.getCharIndex()==NPC.WEREWOLF)
                  { 
                     if (target!=null && !target.hasEffect("control"))
                        dir = TerrainBuilder.getDirectionTowardsSimple(p.getRow(), p.getCol(), target.getRow(), target.getCol());
                     else
                        dir = TerrainBuilder.getDirectionTowardsSimple(p.getRow(), p.getCol(), player.getRow(), player.getCol());
                  }
                  if(dir==LocationBuilder.NORTH && p.getRow()-1>=0)
                  {
                     int r = p.getRow();
                     int c = p.getCol();
                     String up = CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase();
                     boolean badUp = (up.contains("water") || up.contains("door") || (up.contains("_d_") && (up.contains("wall") || up.contains("rock"))));
                     if((player.getRow()==r-1 && player.getCol()==c) || NPCAtMoveTo(r-1, c, p.getMapIndex()))
                     {}    //don't move into the postition where the player or other NPCs are
                     else if(p.getRow() <= 2 || !LocationBuilder.canMoveTo(currMap, r-1, c) || badUp)
                     {}
                     else
                        p.moveNorth();   
                  }
                  else if(dir==LocationBuilder.SOUTH && p.getRow()+1<currMap.length)
                  {
                     int r = p.getRow();
                     int c = p.getCol();
                     String down = CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase();
                     boolean badDown = (down.contains("water") || down.contains("door") || (down.contains("_d_") && (down.contains("wall") || down.contains("rock"))));
                     if((player.getRow()==r+1 && player.getCol()==c) || NPCAtMoveTo(r+1, c, p.getMapIndex()))
                     {}    //don't move into the postition where the player or other NPCs are
                     else if(p.getRow() >= currMap.length - 2 || !LocationBuilder.canMoveTo(currMap, r+1, c) || badDown)
                     {}
                     else
                        p.moveSouth();   
                  } 
                  else if(dir==LocationBuilder.EAST && p.getCol()+1<currMap[0].length)
                  {
                     int r = p.getRow();
                     int c = p.getCol();
                     String right = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase();
                     boolean badRight = (right.contains("water") || right.contains("door") || (right.contains("_d_") && (right.contains("wall") || right.contains("rock"))));
                     if((player.getRow()==r && player.getCol()==c+1) || NPCAtMoveTo(r, c+1, p.getMapIndex()))
                     {}    //don't move into the postition where the player or other NPCs are
                     else if(p.getCol() >= currMap[0].length - 2 || !LocationBuilder.canMoveTo(currMap, r, c+1) || badRight)
                     {}
                     else
                        p.moveEast();   
                  }
                  else if(dir==LocationBuilder.WEST && p.getCol()-1>=0)
                  {
                     int r = p.getRow();
                     int c = p.getCol();
                     String left = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase();
                     boolean badLeft = (left.contains("water") || left.contains("door") || (left.contains("_d_") && (left.contains("wall") || left.contains("rock"))));
                     if((player.getRow()==r && player.getCol()==c-1) || NPCAtMoveTo(r, c-1, p.getMapIndex()))
                     {}    //don't move into the postition where the player or other NPCs are
                     else if(p.getCol() <= 2 || !LocationBuilder.canMoveTo(currMap, r, c-1) || badLeft)
                     {}
                     else
                        p.moveWest();   
                  }
               }
            }
            else if(p.getMoveType()==NPC.CHASE || p.getMoveType()==NPC.RUN  || p.getMoveType()==NPC.FOLLOW)
            {
               int fogDist = 5;  //the distance that a pursuer will lose us in the fog
               if(mi == 0)
                  fogDist = 3;   //shorter distance when in the world map
               if((p.getMoveType()==NPC.CHASE || p.getMoveType()==NPC.RUN) && !p.isZombie() && !p.isEscortedNpc() && !p.isOurDog() &&
               (((player.getActiveSpell("Charm")!=null) && p.getReputation() >= (-100 - (player.getMind()*2)) && !p.hasBeenAttacked()) || player.isInvisible()))
               {  //break of oppositional behavior if the player has the charm spell activated
                  p.setMoveTypeTemp(NPC.ROAM);
                  continue;
               }
               if((p.getMoveType()==NPC.CHASE || p.getMoveType()==NPC.RUN) && CultimaPanel.fogAlpha > 100 && distToPlayer >= fogDist && !p.hasBeenAttacked() && !p.isEscortedNpc() && !p.isOurDog())
               {  //people chasing us in the fog loose us if they are farther away 
                  p.setMoveTypeTemp(NPC.ROAM);
                  continue;
               }
               if(p.getMoveType()==NPC.FOLLOW && distToPlayer <= 2)
                  continue;
               if(!inCamp && p.getMoveType()==NPC.RUN && monster==null && !fear && p.getNumInfo()==0 && p.getCharIndex()!=NPC.JESTER && !p.formerCultist() && !p.getName().equalsIgnoreCase("Keeper of the Bridge") && !p.getName().equalsIgnoreCase("Black Knight"))                    
               {  //see if there is a guard in sight, and if so, alert them
                  for(NPCPlayer g:CultimaPanel.NPCinSight)
                  {
                     if(NPC.isGuard(g.getCharIndex()))
                     {
                        CultimaPanel.sendMessage("~Help! Guards!");
                     }
                  }
               }
               if((p.getMoveType()==NPC.CHASE || (p.getMoveType()==NPC.RUN && p.isBoxedIn()) || NPC.isAssassin(p.getCharIndex()) || NPC.isBlackKnight(p.getCharIndex())) && !p.hasEffect("control") && !p.isEscortedNpc() && !p.isOurDog())   //attack if in range
               {
                  if(p.canAttackPlayer())
                  {
                     if(CultimaPanel.numFrames >= p.getAttackTime())
                     {
                        if(p.getCharIndex()==NPC.JESTER && p.getReputation() > (-1*rollDie(1500)) && !CultimaPanel.player.isCamping())
                        {
                        //jester doesn't always try to steal
                        }
                        else
                        {
                           p.attack();
                           int extraTime = 100-p.getAgility();     //extra time added to attack "reload" with lower agility
                           if(extraTime < 0)
                              extraTime = 0;
                           p.setAttackTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3) + extraTime);
                           continue;
                        }
                     }
                     else
                     {  //keep p in the pocket to attack depending on their agility
                        int moveRoll = rollDie(p.getAgility()+CultimaPanel.player.getAgility());
                        if(!NPC.isJumpy(p) && (p.getAgility() < moveRoll || Math.random() < 0.5))
                           continue;
                     }
                  }
               }
               if(player.getBody() <= 0)
               {
                  p.setMoveTypeTemp(NPC.ROAM);
                  continue;
               }
               if(p.getCharIndex()==NPC.PHANTOM && TerrainBuilder.habitablePlace(p.getLocationType()))
               {  //make phantoms in haunted houses sit still until you get close enough for them to attack you
                  continue;
               }
               int r = p.getRow();
               int c = p.getCol();
               int playerR = player.getRow();
               int playerC = player.getCol();
            
               if(monster!=null && !NPC.isNatural(monster.getCharIndex()))    //don't set these if NPC.CHASE and monster is NPC.FIRE
               {
                  playerR = monster.getRow();
                  playerC = monster.getCol();
               }
               else if(target != null && distToTarg < distToPlayer)
               {
                  playerR = target.getRow();
                  playerC = target.getCol();
               }
            
               int dir = TerrainBuilder.getDirectionTowards(r, c, playerR, playerC);
               if(inCamp && target!=null && (NPC.isAnyBrigand(p.getCharIndex()) || NPC.isGuard(p)))
               {
                  dir = TerrainBuilder.getDirectionTowards(r, c, target.getRow(), target.getCol());
               }
               if(p.getMoveType() == NPC.CHASE && !NPC.isPsychic(p) && CultimaPanel.player.isHiding() && distToPlayer >= 3)
               {  //if we are hiding, make it so that the creature seeking us essentially is put to roam depending on how agilities compare
                  if(CultimaPanel.numFrames % 2 == 0)
                  {  //slow p down if they are searching for a hidden player
                     continue;
                  }
                  double hideValue = 0.8;
                  if(CultimaPanel.isNight)
                  {  //more effective hiding at night
                     hideValue += 0.1;
                  }
                  if(Display.isWinter())
                  {  //less effective hiding in winter
                     hideValue -= 0.2;
                  }
                  if(CultimaPanel.fogAlpha > 0)
                  {  //more effective in fog
                     hideValue += CultimaPanel.fogAlpha/1000.0;
                  }
                  if(CultimaPanel.player.hasBrightWeapon())
                  {  //if we have a bright or flaming weapon out, we are not hiding very well
                     hideValue = 0.1;
                  }
                  if((p.getCharIndex()==NPC.FIRE || p.getCharIndex()==NPC.TORNADO) && p.getNumInfo()!=0)
                  {
                     hideValue = 1.0;
                  }
                  if(p.canSwim() && playerSpot.contains("water"))
                  {  //swimmers can see you if you are hiding in water
                     hideValue = 0; 
                  }
                  double agilDiff = Math.min(hideValue, (CultimaPanel.player.getAgility() / (Math.max(1, p.getAgility()))));
                  if(Math.random() < agilDiff)
                  {
                     dir = TerrainBuilder.getRandomDirection(); 
                  }
               }
               boolean moveDown = (dir==TerrainBuilder.SOUTH);          //(c==playerC && r<playerR);
               boolean moveUp = (dir==TerrainBuilder.NORTH);            //(c==playerC && r>playerR);
               boolean moveLeft = (dir==TerrainBuilder.WEST);           //(r==playerR && c>playerC);
               boolean moveRight = (dir==TerrainBuilder.EAST);          //(r==playerR && c<playerC);
               boolean moveDownLeft = (dir==TerrainBuilder.SOUTHWEST);  //(r<playerR && c>playerC);
               boolean moveDownRight = (dir==TerrainBuilder.SOUTHEAST); //(r<playerR && c<playerC);
               boolean moveUpLeft = (dir==TerrainBuilder.NORTHWEST);    //(r>playerR && c>playerC);
               boolean moveUpRight = (dir==TerrainBuilder.NORTHEAST);   //(r>playerR && c<playerC);
               if(p.getMoveType()==NPC.RUN)
               {
                  moveDown = (dir==TerrainBuilder.NORTH);               //(c==playerC && r>playerR);
                  moveUp = (dir==TerrainBuilder.SOUTH);                 //(c==playerC && r<playerR);
                  moveLeft = (dir==TerrainBuilder.EAST);                //(r==playerR && c<playerC);
                  moveRight = (dir==TerrainBuilder.WEST);               //(r==playerR && c>playerC);
                  moveDownLeft = (dir==TerrainBuilder.NORTHEAST);       //(r>playerR && c<playerC);
                  moveDownRight = (dir==TerrainBuilder.NORTHWEST);      //(r>playerR && c>playerC);
                  moveUpLeft = (dir==TerrainBuilder.SOUTHEAST);         //(r<playerR && c<playerC);
                  moveUpRight = (dir==TerrainBuilder.SOUTHWEST);        //(r<playerR && c>playerC);
               }               
               if(moveDown)
               {
                  if(!LocationBuilder.canMoveTo(p, currMap, r+1, c))
                  {     //blocked down, so try left or right
                     if(LocationBuilder.canMoveTo(p, currMap, r, c-1) && LocationBuilder.canMoveTo(p, currMap, r, c+1))
                     {  //left and right are both clear, so pick randomly
                        if(Math.random() < 0.5)
                           p.moveWest();
                        else
                           p.moveEast();
                     }
                     else if(LocationBuilder.canMoveTo(p, currMap, r, c-1))
                        p.moveWest();
                     else if(LocationBuilder.canMoveTo(p, currMap, r, c+1))
                        p.moveEast();
                  }
                  else
                     p.moveSouth();   
               }
               else if(moveUp)
               {
                  if(!LocationBuilder.canMoveTo(p, currMap, r-1, c))
                  {     //blocked down, so try left or right
                     if(LocationBuilder.canMoveTo(p, currMap, r, c-1) && LocationBuilder.canMoveTo(p, currMap, r, c+1))
                     {  //left and right are both clear, so pick randomly
                        if(Math.random() < 0.5)
                           p.moveWest();
                        else
                           p.moveEast();
                     }
                     else if(LocationBuilder.canMoveTo(p, currMap, r, c-1))
                        p.moveWest();
                     else if(LocationBuilder.canMoveTo(p, currMap, r, c+1))
                        p.moveEast();
                  }
                  else
                     p.moveNorth();   
               }
               else if(moveLeft)
               {
                  if(!LocationBuilder.canMoveTo(p, currMap, r, c-1))
                  {     //blocked left, so try up or down
                     if(LocationBuilder.canMoveTo(p, currMap, r-1, c) && LocationBuilder.canMoveTo(p, currMap, r+1, c))
                     {  //up and down are both clear, so pick randomly
                        if(Math.random() < 0.5)
                           p.moveNorth();
                        else
                           p.moveSouth();
                     }
                     else if(LocationBuilder.canMoveTo(p, currMap, r-1, c))
                        p.moveNorth();
                     else if(LocationBuilder.canMoveTo(p, currMap, r+1, c))
                        p.moveSouth();
                  }
                  else
                     p.moveWest();   
               }
               else if(moveRight)
               {
                  if((playerR==r && playerC==c+1) || !LocationBuilder.canMoveTo(p, currMap, r, c+1))
                  {     //blocked left, so try up or down
                     if(LocationBuilder.canMoveTo(p, currMap, r-1, c) && LocationBuilder.canMoveTo(p, currMap, r+1, c))
                     {  //up and down are both clear, so pick randomly
                        if(Math.random() < 0.5)
                           p.moveNorth();
                        else
                           p.moveSouth();
                     }
                     else if(LocationBuilder.canMoveTo(p, currMap, r-1, c))
                        p.moveNorth();
                     else if(LocationBuilder.canMoveTo(p, currMap, r+1, c))
                        p.moveSouth();
                  }
                  else
                     p.moveEast();   
               }
               else if(moveDownLeft)
               {
                  if(LocationBuilder.canMoveTo(p, currMap, r+1, c) && LocationBuilder.canMoveTo(p, currMap, r, c-1))
                  {  //clear down and left, so pick randomly
                     if(Math.random() < 0.5)
                        p.moveSouth();
                     else
                        p.moveWest();
                  }
                  else  if(LocationBuilder.canMoveTo(p, currMap, r+1, c))//clear down
                     p.moveSouth();
                  else if(LocationBuilder.canMoveTo(p, currMap, r, c-1))//clear left
                     p.moveWest();
               }
               else if(moveDownRight)
               {
                  if(LocationBuilder.canMoveTo(p, currMap, r+1, c) && LocationBuilder.canMoveTo(p, currMap, r, c+1))
                  {  //clear down and left, so pick randomly
                     if(Math.random() < 0.5)
                        p.moveSouth();
                     else
                        p.moveEast();
                  }
                  else  if(LocationBuilder.canMoveTo(p, currMap, r+1, c))//clear down
                     p.moveSouth();
                  else if(LocationBuilder.canMoveTo(p, currMap, r, c+1))//clear left
                     p.moveEast();
               }
               else if(moveUpLeft)
               {
                  if(LocationBuilder.canMoveTo(p, currMap, r-1, c) && LocationBuilder.canMoveTo(p, currMap, r, c-1))
                  {  //clear up and left, so pick randomly
                     if(Math.random() < 0.5)
                        p.moveNorth();
                     else
                        p.moveWest();
                  }
                  else  if(LocationBuilder.canMoveTo(p, currMap, r-1, c))//clear up
                     p.moveNorth();
                  else if(LocationBuilder.canMoveTo(p, currMap, r, c-1) )//clear left
                     p.moveWest();
               }
               else if(moveUpRight)
               {
                  if(LocationBuilder.canMoveTo(p, currMap, r-1, c) && LocationBuilder.canMoveTo(p, currMap, r, c+1))
                  {  //clear up and left, so pick randomly
                     if(Math.random() < 0.5)
                        p.moveNorth();
                     else
                        p.moveEast();
                  }
                  else  if(LocationBuilder.canMoveTo(p, currMap, r-1, c))//clear up
                     p.moveNorth();
                  else if(LocationBuilder.canMoveTo(p, currMap, r, c+1))//clear left
                     p.moveEast();
               }
            }
         }
      }
   }

   //if p is stuck in a structure, remove them and any mission associated with them (except fire, escorted npcs, magmamothers)
   public static boolean removeStuckNPCs(NPCPlayer p, int i, ArrayList<NPCPlayer> npcCollection)
   {
      if(p.getMapIndex()>=0 && p.getMapIndex()<CultimaPanel.map.size())
      {
         boolean pIsOurNpc = (p.isEscortedNpc());
         boolean pIsOurDog = (p.isOurDog());
         if(!LocationBuilder.canMoveToNoNPCcheck(p, CultimaPanel.map.get(p.getMapIndex()), p.getRow(), p.getCol()) && i<npcCollection.size() && !pIsOurNpc && !p.isOurDog() && p.getCharIndex()!=NPC.MAGMA && p.getCharIndex()!=NPC.FIRE && p.getCharIndex()!=NPC.TORNADO)
         {  //remove npcs that got stuck in a wall somehow
            //remove any current missions with players in this location since they are removed
            if(!p.getName().contains("Skara Brae"))
            {
               String NPCmiss = p.getMissionInfo();
               if(!NPCmiss.equals("none"))
               {
                  for(int j=0; j<CultimaPanel.missionStack.size(); j++)
                  {
                     Mission m = CultimaPanel.missionStack.get(j);
                     if(NPCmiss.equals(m.getInfo()))
                     {
                        CultimaPanel.missionStack.remove(j);
                        break;
                     }
                  }
               }
               npcCollection.remove(i);
               return true;
            }
         }     
      }
      return false;
   }
   
   public static boolean checkTraps(NPCPlayer p)
   {
    //see if NPC steps into a trap
      if(!p.canFly() && !p.canSwim() && !NPC.isNatural(p.getCharIndex()) && p.getCharIndex()!=NPC.MONOLITH)
      {
         int mi = p.getMapIndex();
         for(int j = CultimaPanel.corpses.get(mi).size()-1; j>=0; j--)
         {
            Corpse cp = CultimaPanel.corpses.get(mi).get(j);
            if(cp.getRow()==p.getRow() && cp.getCol()==p.getCol() && cp.getCharIndex()==NPC.JAWTRAP)
            {
               boolean trapDestroyed = false;
               int rollD = rollDie(10);
               if(NPC.isMonsterKing(p.getCharIndex()) || (NPC.getSize(p.getCharIndex()) == 1.5 && rollD < 5))
                  trapDestroyed = true;
               else if(NPC.getSize(p.getCharIndex()) == 1.5 && rollD < 5)
                  trapDestroyed = true;
               else if(NPC.getSize(p.getCharIndex()) > 1.5 && rollD < 7)
                  trapDestroyed = true;
               if(trapDestroyed && p.getMoveType()!=NPC.STILL)
               {
                  p.damage(rollDie(10,25),"trap");
                  CultimaPanel.corpses.get(mi).remove(j);
                  CultimaPanel.sendMessage("Trap destroyed!");
                  p.setMoveType(NPC.CHASE);
                  continue;
               } 
               else
               {  
                  int roll = rollDie(p.getAgility());
                  if(roll < 45)
                  {
                     if(p.getMoveType()!=NPC.STILL)
                     {
                        p.damage(rollDie(10,25),"trap");
                        p.setMoveTypeTemp(NPC.STILL);  
                        if(CultimaPanel.NPCinSight.size()>1 && !p.isZombie() && TerrainBuilder.habitablePlace(p.getLocationType()))
                        {
                           alertGuards();
                        }
                     }
                     if(p.canAttackPlayer() && CultimaPanel.numFrames >= p.getAttackTime())
                     {
                        p.attack();
                        int extraTime = 100-p.getAgility();     //extra time added to attack "reload" with lower agility
                        if(extraTime < 0)
                           extraTime = 0;
                        p.setAttackTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3) + extraTime);
                     }
                     break;
                  }
               }
            }
         }
         if(p.isTrapped())
            return true;
      }
      return false;
   }
   
   //see if the npc uses their move turn to do a special attack like summon a minion
   public static boolean specialMove(NPCPlayer p, int i, ArrayList<NPCPlayer> npcList)
   {
      double dist = Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
      if(p.getCharIndex()==NPC.EARTHELEMENTAL && p.getNumInfo()>0 && Math.random() < 0.025 && dist <= 2)    
      {  //EARTHELEMENTAL might surround you with raised stones
         p.setNumInfo(p.getNumInfo()-1);
         surroundWithStones();
         return true;
      }
      if(p.getCharIndex()==NPC.DEMONKING && p.getNumInfo()>0 && Math.random() < 0.025 && dist <= CultimaPanel.SCREEN_SIZE/2 && !p.getName().toLowerCase().contains("puzzle"))    
      {  //DEMONKING might summon a demon minion on this movement
         if(p.getNumInfo() <= 4 && Math.random() < 0.25)
         {
            p.setNumInfo(p.getNumInfo()-1);
            surroundWithFire();
         }
         else
         {
            byte summonType = NPC.DEMON;
            if(Math.random() < 0.25 && !CultimaPanel.player.isSailing() && !CultimaPanel.player.onHorse())
               summonType = -111;  //-111 is the flag that this monster is a mimic
            boolean success = summonMonster(summonType, p.getRow(), p.getCol(), 3, p);
            if(success)
            {
               p.setNumInfo(p.getNumInfo()-1);
               if(dist <= CultimaPanel.SCREEN_SIZE/2)
               {
                  CultimaPanel.flashColor = Color.red;
                  CultimaPanel.flashFrame = CultimaPanel.numFrames;
                  CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
               }
               if(summonType == -111)
                  CultimaPanel.sendMessage("Mimic summoned!");
               else
                  CultimaPanel.sendMessage(NPC.characterDescription(summonType)+" summoned!");
               return true;
            }
         }
      }
      if(p.getCharIndex()==NPC.MAGMA && p.getNumInfo()>0 && Math.random() < 0.025 && dist <= CultimaPanel.SCREEN_SIZE/2)    
      {//MAGMAMOTHER might summon a fire elemental on this movement
         boolean success = summonMonster(NPC.FIRE, p.getRow(), p.getCol(), 1, p);
         if(success)
         {
            p.setNumInfo(p.getNumInfo()-1);
            CultimaPanel.sendMessage("Elemental summoned!");
            return true;
         }
      }
      if(p.getCharIndex()==NPC.FIRE && p.getNumInfo()>1 && Math.random() < 0.02 && dist <= CultimaPanel.SCREEN_SIZE/2)    
      {//FIRE elemental might summon another fire elemental on this movement
         boolean success = summonMonster(NPC.FIRE, p.getRow(), p.getCol(), 1, p);
         if(success)
         {
            p.setNumInfo(p.getNumInfo()-1);
            return true;
         }
      }
      if(NPC.isVampire(p.getCharIndex()))
      {
         if(p.getBody() < 40 || (!CultimaPanel.isNight && !TerrainBuilder.indoors()))
         {  //change to bats and run when hurt
            NPCPlayer bat = new NPCPlayer(NPC.BAT, p.getRow(), p.getCol(), p.getMapIndex(), CultimaPanel.player.getLocationType());
            bat.setName(p.getName());
            bat.setAgility(p.getAgility()*2);
            if(bat.getBody() < p.getBody())
               bat.setBody(p.getBody());
            bat.setMoveType(NPC.RUN);
            bat.addItems(p.getItems());
            if(dist <= CultimaPanel.SCREEN_SIZE/2)
            {
               Sound.thunder();
               CultimaPanel.flashColor = Color.red;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
            }
            npcList.set(i, bat);
            return true;
         }
         else if(p.getNumInfo()>0 && Math.random() < 0.025 && dist <= CultimaPanel.SCREEN_SIZE/4 && !CultimaPanel.player.isVampire())    
         {//vampire might try to seduce us on this movement
            boolean success = (CultimaPanel.player.getMind() < rollDie(75));
            if(success)
            {
               p.setNumInfo(p.getNumInfo()-1);
               CultimaPanel.flashColor = Color.pink;
               CultimaPanel.flashFrame = CultimaPanel.numFrames;
               CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE/4;
               CultimaPanel.sendMessage("Seduced by a Vampyre!");
               CultimaPanel.player.addEffect("seduced");
               CultimaPanel.player.setSulliedValue(rollDie(150 - CultimaPanel.player.getMind()));
               return true;
            }
         }
      }
      
      if(p.getCharIndex()==NPC.TRIFFID && Math.random() < 0.02 && dist <= CultimaPanel.SCREEN_SIZE/8)    
      {//triffid might try to dizzy us with spores on this movement
         boolean success = (CultimaPanel.player.getMight() < rollDie(100));
         if(success)
         {
            p.setNumInfo(p.getNumInfo()-1);
            CultimaPanel.flashColor = Color.green;
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE/8;
            CultimaPanel.sendMessage("Dizzied by Triffid spores!");
            CultimaPanel.player.addEffect("sullied");
            CultimaPanel.player.setSulliedValue(rollDie(150 - CultimaPanel.player.getMight()));
            return true;
         }
      }
      return false;
   }
   
   public static boolean nocturnalCheck(NPCPlayer p, int i, ArrayList<NPCPlayer> civsInLoc)
   {
      if(p.getCharIndex()==NPC.WILLOWISP && p.getLocationType().contains("pacrealm"))
         return false;
      String locType = p.getLocationType().toLowerCase();
      boolean inCamp = (locType.contains("camp"));
      double dist = Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
      int mi = p.getMapIndex();
      if(!CultimaPanel.isNight && NPC.isNocturnal(p.getCharIndex()))
      {  //remove any nocturnal creatures if we can't see them on the screen
         if(p.getCharIndex()==NPC.WILLOWISP || dist > CultimaPanel.SCREEN_SIZE/2)
         {
            Utilities.removeNPCat(p.getRow(), p.getCol(), mi);
            return true;
         }
      }
      if(!CultimaPanel.isNight && p.getCharIndex()==NPC.WEREWOLF)
      {  //change back to human from werewolf
         NPCPlayer human = null;
         String pName = p.getName();
         byte pType = NPC.MAN;
         if(pName.contains("~"))    //werewolf names are in the format of <name>~<imageIndex>, like "Kessler~5".  The number stores their original form
         {
            String [] nameParts = pName.split(" ");
            for(int ni=0; ni<nameParts.length; ni++)
            {  //the name might be a complex one, with a job prefix or a town name afterwards
               if(nameParts[ni].contains("~"))
               {
                  pName = nameParts[ni];
                  break;
               }
            }
            String [] parts = pName.split("~");
            if(FileManager.wordIsInt(parts[1].trim()))
               pType = Byte.parseByte(parts[1].trim());
            if(pType == NPC.LUTE || pType == NPC.SWORD)
               pType = NPC.MAN;                 //the werewolf loses their lute in the transformation
            else if(NPC.isGuard(pType))
               pType = NPC.GUARD_FIST;          //the werewolf loses their weapon in the transformation
         }
         else                                   //this might happen if a werewolf spawns 
         {
            byte [] humanTypes = {NPC.MAN, NPC.WOMAN, NPC.GUARD_FIST};
            pType = getRandomFrom(humanTypes);
            p.setName(randomName()+"~"+pType);  //pick a human name
         }
         human = new NPCPlayer(pType, p.getRow(), p.getCol(), p.getMapIndex(), CultimaPanel.player.getLocationType());
         human.setName(p.getName());
         human.setReputation(p.getNumInfo());
         if(!p.getMissionInfo().equals("none"))
            human.setNumInfo(10);
         if(human.getBody() < p.getBody())
            human.setBody(p.getBody());
         human.setHomeRow(p.getHomeRow());
         human.setHomeCol(p.getHomeCol());   
         human.setMaritalStatus(p.getMaritalStatus());
         if(inCamp)
         {
            human.setMoveType(NPC.RUN);
         }
         else
         {
            human.setMoveType(NPC.ROAM);
         }
         human.setMissionInfo(p.getMissionInfo());
         if(dist <= CultimaPanel.SCREEN_SIZE/2)
         {
            Sound.thunder();
            CultimaPanel.flashColor = Color.red;
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
         }
         civsInLoc.set(i, human);
         return true;
      }
      else if(CultimaPanel.isNight && CultimaPanel.weather == 0 && p.getCharIndex()!=NPC.WEREWOLF && p.isWerewolf())
      {  //change back to werewolf
         NPCPlayer werewolf = null;
         werewolf = new NPCPlayer(NPC.WEREWOLF, p.getRow(), p.getCol(), p.getMapIndex(), CultimaPanel.player.getLocationType());
         werewolf.setName(p.getName());
         werewolf.setNumInfo(p.getReputation());
         if(werewolf.getBody() < p.getBody())
            werewolf.setBody(p.getBody());
         werewolf.setMoveType(NPC.CHASE);
         werewolf.setHomeRow(p.getHomeRow());
         werewolf.setHomeCol(p.getHomeCol());
         werewolf.setMaritalStatus(p.getMaritalStatus());
         werewolf.setMissionInfo(p.getMissionInfo());
         if(dist <= CultimaPanel.SCREEN_SIZE/2)
         {
            Sound.thunder();
            CultimaPanel.flashColor = Color.red;
            CultimaPanel.flashFrame = CultimaPanel.numFrames;
            CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
         }
         civsInLoc.set(i, werewolf);
         return true;
      }
      return false;
   }
   
   public static void affectTerrain(NPCPlayer p)
   {
      int mi = p.getMapIndex();
      byte[][]currMap = (CultimaPanel.map.get(mi));
      String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][p.getCol()])).getName().toLowerCase();
      double dist = Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
   
      if(NPC.isGiantMonsterKing(p) && TerrainBuilder.habitablePlace(p.getLocationType()))
      {  //make kaiju destroy any structure that they come close to
         for(int mr=-1; mr<=1; mr++)
         {
            for(int mc=-1; mc<=1; mc++)
            {
               if(p.getRow() + mr >= 0 && p.getRow() + mr < currMap.length && p.getCol() + mc >=0 && p.getCol() + mc < currMap[0].length)
               {
                  String closeSpot = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()+mr][p.getCol()+mc])).getName().toLowerCase();
                  boolean structureDestroyed = false;
                  boolean wallDestroyed = false;
                  if(closeSpot.contains("high_grass") || closeSpot.contains("forest"))
                  {//change to dead forest
                     currMap[p.getRow()+mr][p.getCol()+mc]=TerrainBuilder.getTerrainWithName("TER_$Dead_forest").getValue();
                  }
                  else if(closeSpot.contains("grassland") || closeSpot.contains("tall_grass"))
                  {//change to dry grass
                     currMap[p.getRow()+mr][p.getCol()+mc]=TerrainBuilder.getTerrainWithName("TER_$Dry_grass").getValue();
                  }
                  else if((closeSpot.contains("floor") && !closeSpot.contains("black") ) || (closeSpot.contains("brick") && !closeSpot.contains("_i_")))
                  {
                     byte wallValue = (byte)(Math.abs(currMap[p.getRow()+mr][p.getCol()+mc]));
                     currMap[p.getRow()+mr][p.getCol()+mc]=TerrainBuilder.getTerrainWithName("TER_$Dry_grass").getValue();
                     int restoreDay = Utilities.getWallRestoreDay(CultimaPanel.player.getLocationType().toLowerCase(), wallValue);
                     CultimaPanel.tilesToRestore.add(new RestoreItem(mi, p.getRow()+mr, p.getCol()+mc, wallValue, restoreDay));
                     structureDestroyed = true;
                  }
                  else if(closeSpot.contains("inr_") || closeSpot.contains("wall") || closeSpot.contains("door") || closeSpot.contains("rock") || closeSpot.contains("torch") || closeSpot.contains("window") || (closeSpot.contains("brick") && closeSpot.contains("_i_")))
                  {
                     byte wallValue = (byte)(Math.abs(currMap[p.getRow()+mr][p.getCol()+mc]));
                     currMap[p.getRow()+mr][p.getCol()+mc]=TerrainBuilder.getTerrainWithName("INR_$Black_floor").getValue();
                     int restoreDay = Utilities.getWallRestoreDay(CultimaPanel.player.getLocationType().toLowerCase(), wallValue);
                     CultimaPanel.tilesToRestore.add(new RestoreItem(mi, p.getRow()+mr, p.getCol()+mc, wallValue, restoreDay));
                     if(Math.random() < 0.1)
                     {
                        summonMonster(NPC.FIRE, p.getRow(), p.getCol(), 2, p);
                     }
                     structureDestroyed = true;
                     if(closeSpot.contains("wall") || closeSpot.contains("door") || closeSpot.contains("window"))
                     {
                        wallDestroyed = true;
                     }
                  }
                  if(structureDestroyed)
                  {
                     int radius = Utilities.rollDie(2,5);
                     int opacity = Utilities.rollDie(50,80);  
                     CultimaPanel.smoke.add(new SmokePuff(p.getNpcX()-(radius/2), p.getNpcY()-(radius/2), radius, opacity, SmokePuff.dustCloud));
                     radius = Utilities.rollDie(2,5);
                     opacity = Utilities.rollDie(50,80);  
                     CultimaPanel.smoke.add(new SmokePuff(p.getNpcX()-(radius/2), p.getNpcY()-(radius/2), radius, opacity, SmokePuff.dustCloud));
                     if(wallDestroyed)
                     {
                        Sound.rumble();
                     }
                  }
               }
            }
         }
      }
      else if(p.getCharIndex() == NPC.WHIRLPOOL || p.getCharIndex() == NPC.TORNADO)                            //make tordados do damage to the terrain
      {
         if(curr.contains("water") && !curr.contains("shallow"))
         {//change to shallow water
            currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_V_$Shallow_water").getValue();
         }
         if(p.getCharIndex() == NPC.TORNADO)                            //make tordados do damage to the terrain
         {
            if(curr.contains("high_grass") || curr.contains("forest"))
            {//change to dead forest
               currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Dead_forest").getValue();
            }
            else if(curr.contains("grassland") || curr.contains("tall_grass"))
            {//change to dry grass
               currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Dry_grass").getValue();
            }
            else if(curr.contains("inr_") || curr.contains("wall") || curr.contains("door") || curr.contains("rock") || curr.contains("torch") || curr.contains("window") || (curr.contains("brick") && curr.contains("_i_")))
            {
               byte wallValue = (byte)(Math.abs(currMap[p.getRow()][p.getCol()]));
               currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("INR_$Black_floor").getValue();
               int restoreDay = Utilities.getWallRestoreDay(CultimaPanel.player.getLocationType().toLowerCase(), wallValue);
               CultimaPanel.tilesToRestore.add(new RestoreItem(mi, p.getRow(), p.getCol(), wallValue, restoreDay));
            }
            else if(curr.contains("floor") || (curr.contains("brick") && !curr.contains("_i_")))
            {
               byte wallValue = (byte)(Math.abs(currMap[p.getRow()][p.getCol()]));
               currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Dry_grass").getValue();
               int restoreDay = Utilities.getWallRestoreDay(CultimaPanel.player.getLocationType().toLowerCase(), wallValue);
               CultimaPanel.tilesToRestore.add(new RestoreItem(mi, p.getRow(), p.getCol(), wallValue, restoreDay));
            }
         }
      }
      else if(p.getCharIndex()==NPC.GRABOID)
      {
         if(curr.contains("dry_grass"))
         {//change to sand
            currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Sand").getValue();
         }
      }
      else if(p.getCharIndex()==NPC.RUSTCREATURE || p.getCharIndex()==NPC.MAZEMOUTH)
      {  //rust creature eats gold
         byte floorValue = (byte)(Math.abs(currMap[p.getRow()][p.getCol()]));
         boolean goldUp = LocationBuilder.chestAt(currMap, p.getRow()-1, p.getCol());
         boolean goldDown = LocationBuilder.chestAt(currMap, p.getRow()+1, p.getCol());
         boolean goldLeft = LocationBuilder.chestAt(currMap, p.getRow(), p.getCol()-1);
         boolean goldRight = LocationBuilder.chestAt(currMap, p.getRow(), p.getCol()+1);
         boolean goldEaten = false;
         if(goldUp)
         {
            currMap[p.getRow()-1][p.getCol()]=floorValue;
            goldEaten = true;
         }
         if(goldDown)
         {
            currMap[p.getRow()+1][p.getCol()]=floorValue; 
            goldEaten = true;
         }
         if(goldLeft)
         {
            currMap[p.getRow()][p.getCol()-1]=floorValue;
            goldEaten = true;
         }
         if(goldRight)
         {
            currMap[p.getRow()][p.getCol()+1]=floorValue;
            goldEaten = true;
         }
         if(goldEaten && p.getCharIndex()==NPC.MAZEMOUTH)
         {   //used to trigger blue ghosts
            CultimaPanel.alertTime = System.currentTimeMillis();
         }    
      }
      else if(p.getCharIndex()==NPC.FIRE)
      {  
         if(curr.contains("grass") || curr.contains("forest"))
         {//change to burnt trees
            if(Math.random() < 0.15)
               currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Dead_forest").getValue();
         }
         else if(!curr.contains("lava") && LocationBuilder.isCombustable(curr))
         {
            if(Math.random() < 0.1)
            {
               byte wallValue = (byte)(Math.abs(currMap[p.getRow()][p.getCol()]));
               currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("INR_$Black_floor").getValue();
               if(curr.contains("wall") || curr.contains("door") || curr.contains("torch") || curr.contains("window") || curr.contains("bed") || curr.contains("coffin"))
               {
                  int restoreDay = Utilities.getWallRestoreDay(CultimaPanel.player.getLocationType().toLowerCase(), wallValue);
                  CultimaPanel.tilesToRestore.add(new RestoreItem(mi, p.getRow(), p.getCol(), wallValue, restoreDay));
               }
            }
         }
      }  
      else if(p.getCharIndex()==NPC.PHANTOM)
      {
         if(curr.contains("high_grass") || curr.contains("forest") || curr.contains("grassland") || curr.contains("dry_grass") || curr.contains("bog") || curr.contains("tall_grass"))
         {//change to dead forest
            currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Dead_forest").getValue();
         }
      }
      else if(p.getCharIndex()==NPC.WEREWOLF && TerrainBuilder.habitablePlace(p.getLocationType()))
      {  //werewolf breaks down wooden doors - its just what they do
         boolean[] doorDirs = nextToAWoodDoor(p.getRow(), p.getCol());
         if ((doorDirs[LocationBuilder.NORTH] || doorDirs[LocationBuilder.EAST] || doorDirs[LocationBuilder.SOUTH] || doorDirs[LocationBuilder.WEST]) && Math.random() < 0.5)
         {
            Sound.hammerWall();
            if(doorDirs[LocationBuilder.NORTH] && p.getRow()-1 > 1)
            {
               Sound.openDoor();
               byte floorValue = Utilities.adjacentIndoorFloor(p.getRow()-1,p.getCol());
               byte wallValue = currMap[p.getRow()-1][p.getCol()];
               currMap[p.getRow()-1][p.getCol()]  = floorValue;
               int restoreDay = Utilities.getWallRestoreDay(p.getLocationType(), wallValue);
               CultimaPanel.tilesToRestore.add(new RestoreItem(p.getMapIndex(), p.getRow()-1, p.getCol(), wallValue, restoreDay));
            }
            else if(doorDirs[LocationBuilder.SOUTH] && p.getRow()+1 < currMap.length-1)
            {
               Sound.openDoor();
               byte floorValue = Utilities.adjacentIndoorFloor(p.getRow()+1,p.getCol());
               byte wallValue = currMap[p.getRow()+1][p.getCol()];
               currMap[p.getRow()+1][p.getCol()]  = floorValue;
               int restoreDay = Utilities.getWallRestoreDay(p.getLocationType(), wallValue);
               CultimaPanel.tilesToRestore.add(new RestoreItem(p.getMapIndex(), p.getRow()+1, p.getCol(), wallValue, restoreDay));
            }
            else if(doorDirs[LocationBuilder.EAST] && p.getCol()+1 < currMap[0].length-1)
            {
               Sound.openDoor();
               byte floorValue = Utilities.adjacentIndoorFloor(p.getRow(),p.getCol()+1);
               byte wallValue = currMap[p.getRow()][p.getCol()+1];
               currMap[p.getRow()][p.getCol()+1]  = floorValue;
               int restoreDay = Utilities.getWallRestoreDay(p.getLocationType(), wallValue);
               CultimaPanel.tilesToRestore.add(new RestoreItem(p.getMapIndex(), p.getRow(), p.getCol()+1, wallValue, restoreDay));
            }
            else if(doorDirs[LocationBuilder.WEST] && p.getCol()-1 > 1)
            {
               Sound.openDoor();
               byte floorValue = Utilities.adjacentIndoorFloor(p.getRow(),p.getCol()-1);
               byte wallValue = currMap[p.getRow()][p.getCol()-1];
               currMap[p.getRow()][p.getCol()-1]  = floorValue;
               int restoreDay = Utilities.getWallRestoreDay(p.getLocationType(), wallValue);
               CultimaPanel.tilesToRestore.add(new RestoreItem(p.getMapIndex(), p.getRow(), p.getCol()-1, wallValue, restoreDay));
            }
         }
      }
      else if(NPC.getSize(p.getCharIndex())>=2 && !p.canFly())    //big monsters knock trees down
      {
         //change dense forest to high_grass
         if(curr.contains("dense_forest"))
         {
            currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Forest").getValue();
         }
         else if(curr.contains("forest") && !curr.contains("dead"))   //change regular forest but not dead forest
         {
            currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$High_grassland").getValue();
         }
         else if(curr.contains("high_grass"))
         {//change to dead forest
            currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Grassland").getValue();
         }
         else if(curr.contains("grassland") || curr.contains("tall_grass"))
         {//change to dry grass
            currMap[p.getRow()][p.getCol()]=TerrainBuilder.getTerrainWithName("TER_$Dry_grass").getValue();
         }
      }
      if(NPC.isStompingMonster(p.getCharIndex()) && p.getMoveType()!=NPC.STILL)
      {
         if(CultimaPanel.numFrames % (CultimaPanel.animDelay*2) == 0)
            Sound.stomp((int)(dist));
      } 
   }
   
   public static void pacRealmMovement()
   {
      int mi = CultimaPanel.player.getMapIndex();
      ArrayList<NPCPlayer> civsInLoc = (CultimaPanel.civilians.get(mi));
      for(int i=civsInLoc.size()-1; i>=0; i--)
      {
         NPCPlayer p = civsInLoc.get(i);   
      
         if(p==null || !p.getLocationType().contains("pacrealm") || (p.getCharIndex()!=NPC.MAZEMOUTH && p.getCharIndex()!=NPC.GHOST))
            continue;
         boolean ghostFear = (p.getCharIndex()==NPC.GHOST && (System.currentTimeMillis() <= CultimaPanel.alertTime + 6000));
         int r = p.getRow();
         int c = p.getCol();
         if(mi < 0 || mi >= CultimaPanel.map.size() || r < 0 || c < 0)
            continue;
         byte[][]currMap = (CultimaPanel.map.get(mi));   
         if(r >= currMap.length || c >= currMap[0].length)
            continue;
         if(p.getBody() <= 0)
            continue;   
         if(p.getCharIndex()==NPC.WILLOWISP)
            continue;
         if(p.getRow()==14 && p.getCol()==3)
         {
            p.setCol(27);
            continue;
         }
         else if(p.getRow()==14 && p.getCol()==28)
         {
            p.setCol(4);
            continue;
         }   
         if(p.getCharIndex()==NPC.MAZEMOUTH)
         {
            affectTerrain(p);
            boolean goToNext = false;
            for(int j=(CultimaPanel.civilians.get(mi)).size()-1; j>=0; j--)
            {
               NPCPlayer currNPC = (CultimaPanel.civilians.get(mi)).get(j);
               double distToNPC = Display.distanceSimple(p.getRow(), p.getCol(), currNPC.getRow(), currNPC.getCol());
               if(currNPC.isPellet() && distToNPC <= 1)
               {
                  if(j < i)
                  {
                     i--;
                  }
                  (CultimaPanel.civilians.get(mi)).remove(j);
                  break;
               }
               else if(currNPC.getCharIndex()==NPC.GHOST && distToNPC <= 1)
               {
                  if(System.currentTimeMillis() <= CultimaPanel.alertTime + 6000)
                  { //blue ghosts can be eaten
                     p.attack(currNPC);
                  }
                  else
                  { //pacman gets eaten by the ghost
                     currNPC.attack(p);
                     goToNext = true;
                     break;
                  }
               }
            }
            if(goToNext)
               continue;
         }
         int delayTime = (int)(CultimaPanel.animDelay);
         double distToPlayer = Display.distanceSimple(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
         if(!ghostFear)
         {
            if(distToPlayer <= 1 && CultimaPanel.player.getBody() > 0)
            {  //make sure npc is moving towards player to attack them
               if((p.getMoveType()==NPC.MARCH_NORTH && CultimaPanel.player.getRow() <= p.getRow()) || 
                  (p.getMoveType()==NPC.MARCH_SOUTH && CultimaPanel.player.getRow() >= p.getRow()) ||
                  (p.getMoveType()==NPC.MARCH_WEST && CultimaPanel.player.getCol() <= p.getCol()) ||
                  (p.getMoveType()==NPC.MARCH_EAST && CultimaPanel.player.getCol() >= p.getCol()))
                  p.attack();
            }
         }
         else
         {  //slow them down when you eat a power pellet
            if(distToPlayer <=1 && p.getCharIndex()==NPC.GHOST)
            {
               CultimaPanel.player.attack(p, false);
            }
            delayTime = (int)(CultimaPanel.animDelay*1.5);
         }
         if(CultimaPanel.numFrames % delayTime != 0)  
            return;
         ArrayList<Byte>openDirs = new ArrayList<Byte>();
         if(LocationBuilder.canMoveToPacRealm(currMap, r-1, c))
         {
            if(ghostFear && CultimaPanel.player.getCol()==p.getCol() && CultimaPanel.player.getRow() < p.getRow())
            {}    //player is above the frightened ghost, so don't add that direction
            else
            {
               openDirs.add(NPC.MARCH_NORTH);
            }
         }
         if(LocationBuilder.canMoveToPacRealm(currMap, r, c+1))
         {
            if(ghostFear && CultimaPanel.player.getRow()==p.getRow() && CultimaPanel.player.getCol() > p.getCol())
            {}    //player is right the frightened ghost, so don't add that direction
            else
            {
               openDirs.add(NPC.MARCH_EAST);
            }
         }
         if(LocationBuilder.canMoveToPacRealm(currMap, r+1, c))
         {
            if(ghostFear && CultimaPanel.player.getCol()==p.getCol() && CultimaPanel.player.getRow() > p.getRow())
            {}    //player is below the frightened ghost, so don't add that direction
            else
            {
               openDirs.add(NPC.MARCH_SOUTH);
            }
         }
         if(LocationBuilder.canMoveToPacRealm(currMap, r, c-1))
         {
            if(ghostFear && CultimaPanel.player.getRow()==p.getRow() && CultimaPanel.player.getCol() < p.getCol())
            {}    //player is right the frightened ghost, so don't add that direction
            else
            {
               openDirs.add(NPC.MARCH_WEST);
            }
         }
         if(openDirs.size() == 0)
         {        //a ghost is trapped with no direction to go, so add anything that is open in the map
            if(LocationBuilder.canMoveToPacRealm(currMap, r-1, c))
            {
               openDirs.add(NPC.MARCH_NORTH);
            }
            if(LocationBuilder.canMoveToPacRealm(currMap, r, c+1))
            {
               openDirs.add(NPC.MARCH_EAST);
            }
            if(LocationBuilder.canMoveToPacRealm(currMap, r+1, c))
            {
               if(ghostFear && CultimaPanel.player.getCol()==p.getCol() && CultimaPanel.player.getRow() > p.getRow())
                  openDirs.add(NPC.MARCH_SOUTH);
            }
            if(LocationBuilder.canMoveToPacRealm(currMap, r, c-1))
            {
               openDirs.add(NPC.MARCH_WEST);
            }
         }
         ArrayList<Byte>intersectionOptions = new ArrayList<Byte>();
         for(Byte b:openDirs)
         {  //see if we are at an intersection - all open spaces except the one that is opposite that we are moving
            if((p.getMoveType()==NPC.MARCH_NORTH && b!=NPC.MARCH_SOUTH) || (p.getMoveType()==NPC.MARCH_SOUTH && b!=NPC.MARCH_NORTH) || 
               (p.getMoveType()==NPC.MARCH_EAST && b!=NPC.MARCH_WEST) || (p.getMoveType()==NPC.MARCH_WEST && b!=NPC.MARCH_EAST))
            {
               intersectionOptions.add(b);
            }
         }
         if(openDirs.size() > 0 && 
         ((p.getMoveType() < NPC.MARCH_NORTH || p.getMoveType() > NPC.MARCH_WEST) ||
         (p.getMoveType() == NPC.MARCH_NORTH && !openDirs.contains(NPC.MARCH_NORTH)) ||
         (p.getMoveType() == NPC.MARCH_EAST && !openDirs.contains(NPC.MARCH_EAST)) ||
         (p.getMoveType() == NPC.MARCH_SOUTH && !openDirs.contains(NPC.MARCH_SOUTH)) ||
         (p.getMoveType() == NPC.MARCH_WEST && !openDirs.contains(NPC.MARCH_WEST))))
         {
            byte randDir = getRandomFrom(openDirs);
            p.setMoveType(randDir);
         } 
         if(intersectionOptions.size() > 1 && Math.random() < 0.5)
         {
            byte randDir = getRandomFrom(intersectionOptions);
            p.setMoveType(randDir);
         }
         if(p.getMoveType() == NPC.MARCH_NORTH)
         {
            p.moveNorth();
         }
         else if( p.getMoveType() == NPC.MARCH_EAST)
         {
            p.moveEast();
         }
         else if( p.getMoveType() == NPC.MARCH_SOUTH)
         {
            p.moveSouth();
         }
         else if( p.getMoveType() == NPC.MARCH_WEST)
         {
            p.moveWest();
         }
      }
   }
   
   public static boolean setMovementType(NPCPlayer p, boolean searchMessage, boolean guardDefender)
   {
      NPCPlayer monster = CultimaPanel.monsterAndCivInView;
      boolean fear = ( CultimaPanel.player.getActiveSpell("Fear")!=null || CultimaPanel.player.getWeapon().getName().contains("Bane") || CultimaPanel.player.getArmor().getName().contains("Holocaust"));
      boolean firestorm = (CultimaPanel.player.getActiveSpell("Firestorm")!=null);
      double dist = Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
      if((p.getName().toLowerCase().contains("puzzle") || p.swinePlayer()) && !p.hasBeenAttacked())
      {
         p.setMoveTypeTemp(NPC.STILL);
         return searchMessage;
      }
      if(NPC.isGiantMonsterKing(p))
      {  //see if this is a mission monster for the KAIJU mission.  If so, see if it is far enough away from the city to end the mission and put it on ROAM
         for(int mIndex=CultimaPanel.missionStack.size()-1; mIndex>=0; mIndex--)
         {
            Mission currMission = CultimaPanel.missionStack.get(mIndex);
            if(currMission.getMissionType()==Mission.KAIJU)
            {
               Location cityLoc = CultimaPanel.getLocation(currMission.getClearLocation());
               if(cityLoc != null)
               {
                  Coord kaijuLocation = LocationBuilder.findKaiju(cityLoc);
                  if(kaijuLocation != null)
                  {
                     double distToCity = Display.findDistance(kaijuLocation.getRow(), kaijuLocation.getCol(), cityLoc.getRow(), cityLoc.getCol());
                     if(distToCity >= CultimaPanel.SCREEN_SIZE*1.5)
                     {
                        CultimaPanel.monsterDist = -1;
                        p.removeItem("tooth");     //remove mission item
                        p.removeItem("essence");
                        break;
                     }
                  }
               }
            }
         }
         if(dist <= 5 && !CultimaPanel.player.isHiding() && p.getReputation() != 0/* && !p.hasBeenAttacked()*/)
         {  //if the monster has been impressed by us, its reputation will be set to 0 and we don't want it to chase us anymore
            p.setMoveTypeTemp(NPC.CHASE);
            return searchMessage;
         }
         if(CultimaPanel.player.isHiding() || (p.getMapIndex()==0 && dist > CultimaPanel.SCREEN_SIZE/1.5) || (p.getReputation()==0 && !p.hasBeenAttacked()))
         {
            p.setMoveTypeTemp(NPC.ROAM);
            return searchMessage;
         }
      }
      if(NPC.isMonsterKing(p.getCharIndex()) && (p.getName().toLowerCase().contains("puzzle")  || p.getName().toLowerCase().contains("skara")) && !p.hasBeenAttacked())
      {
         return searchMessage;
      }
      if(NPC.isBattleFieldFriend(p) && p.getLocationType().toLowerCase().equals("battlefield") && !p.hasBeenAttacked())
      {  //if the battle is over, set our friends to roam
         if(CultimaPanel.numBattleFieldEnemies == 0)
         {  //do one more check to make sure there are no battlefield enemies in case this is called before they are counted in getAdjacentNPCs
            for(int pi=CultimaPanel.worldMonsters.size()-1; pi>=0; pi--)
            {
               NPCPlayer bp = CultimaPanel.worldMonsters.get(pi);
            
               if(bp.getMapIndex()==CultimaPanel.player.getMapIndex() && !NPC.isCivilian(bp) && bp.getCharIndex()!=NPC.BOWASSASSIN  && bp.getCharIndex()!=NPC.ENFORCER && !bp.isEscortedNpc() && !bp.isOurDog() && bp.getCharIndex()!=NPC.FIRE && bp.getCharIndex()!=NPC.TORNADO && bp.getCharIndex()!=NPC.WILLOWISP && !bp.isStatue() && !bp.isNonPlayer())
                  CultimaPanel.numBattleFieldEnemies++;
            }
            if(CultimaPanel.numBattleFieldEnemies == 0)
            {  //the battle is over - set our friends to roam
               p.setMoveTypeTemp(NPC.ROAM);
               return searchMessage;
            }
         }
      }
      if(p.getCharIndex()==NPC.FIRE)
      {
         if(p.getNumInfo() != 0)
         {  //this is a fire elemental - have it chase us
            p.setMoveType(NPC.CHASE);
         }
         return searchMessage;
      }
      if(p.isZombie())
      {
         p.setMoveType(NPC.CHASE);
         return searchMessage;
      }
      if(p.getCharIndex()==NPC.TORNADO && p.getNumInfo() == 0 && !p.hasItem("essence"))
      {
         p.setMoveType(NPC.ROAM);
         return searchMessage;
      }
      if(!NPC.isGiantMonsterKing(p) && p.hasEffect("control") && p.getMoveType()!=NPC.ROAM)        //possessed creatures will not chase you nor run from you
      {
         p.setMoveTypeTemp(NPC.ROAM);
         return searchMessage;
      }
      if(p.hasEffect("stun") && p.getMoveType()!=NPC.ROAM)           //stunned creatures will not chase you nor run from you
      {
         p.setMoveTypeTemp(NPC.ROAM);  
         return searchMessage;
      }
      if(TerrainBuilder.habitablePlace(p.getLocationType()) && NPC.isCivilian(p))
      {
         if(p.getCharIndex()==NPC.CHILD && p.getMissionPriority() > 0)
         {                                                           //make children with a mission follow you around
            int followValue = (p.getNameNum() % 3);                  //0,1 or 2 use the child's name to determine how diligently they follow you
            if(Math.random() < (0.5 + followValue/10.0))
            {
               p.setMoveTypeTemp(NPC.FOLLOW);
            }
            else
               p.setMoveType(p.getOrigMoveType());
         }
         if(monster == null && !fear && !firestorm && p.getMoveType()==NPC.RUN && p.getCharIndex()!=NPC.JESTER && !p.hasBeenAttacked())
         {
            p.setMoveType(p.getOrigMoveType()); 
         }
         if(guardDefender)
         {  //if this is a guard in a city and a monster walks in, any roaming guard will search for it unless they are mission borne monsters (those are our responsibility)
            if(p.getMoveType()==NPC.STILL)
            {
               int dir=rollDie(1,4);
               if(dir==1)
                  p.setMoveTypeTemp(NPC.MARCH_NORTH);
               else if(dir==2)
                  p.setMoveTypeTemp(NPC.MARCH_EAST);
               else if(dir==3)
                  p.setMoveTypeTemp(NPC.MARCH_SOUTH);
               else// if(dir==4)
                  p.setMoveTypeTemp(NPC.MARCH_WEST);
            }
         }
         if((monster != null || fear || firestorm) && !p.isZombie() && !p.getName().equalsIgnoreCase("Black Knight"))
         {
            int npcLevel = p.getLevel();
            int playerLevel = CultimaPanel.player.getLevel();
            if(CultimaPanel.player.getActiveSpell("Firestorm")!=null && p.getMoveType() != NPC.RUN)
            {
               p.setMoveTypeTemp(NPC.RUN);
               if((System.currentTimeMillis() > CultimaPanel.cheerTime + 5000) && !searchMessage)
               {
                  CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.helpFire).replace("PLAYER_NAME", CultimaPanel.player.getShortName()));
                  searchMessage = true;
                  CultimaPanel.cheerTime = System.currentTimeMillis();
               }
               return searchMessage;
            }
            if(CultimaPanel.player.getActiveSpell("Fear")!=null && CultimaPanel.player.getImageIndex()!=Player.DRAGON && CultimaPanel.player.getImageIndex()!=Player.DEMON
               && (((NPC.isGuard(p.getCharIndex()) || p.getCharIndex()==NPC.SWORD) && npcLevel > playerLevel) ))
            {  //player has fear spell activated
               if(NPCisInSight(p))
               {  //p is a guard or adventurer that is not intimidated by a low-level fear spell
                  p.setMoveTypeTemp(NPC.CHASE);
                  return searchMessage;
               }
            }
            if(p.getMoveType() != NPC.RUN)
            {
               if(CultimaPanel.player.getWeapon().getName().contains("Bane") && (NPC.isGuard(p.getCharIndex()) || p.getCharIndex()==NPC.SWORD))
               {}    //guard and adventurers are not as intimidated by the bane hammer
               else
               {
                  if(guardDefender)
                  {}
                  else
                     p.setMoveTypeTemp(NPC.RUN);
                  if((System.currentTimeMillis() > CultimaPanel.cheerTime + 5000) && !searchMessage)
                  {
                     if(monster!=null)
                     {
                        if(monster.getCharIndex()==NPC.FIRE)
                        {
                           CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.helpFire).replace("PLAYER_NAME", CultimaPanel.player.getShortName()));
                           searchMessage = true;
                        }
                        else if(NPC.isShip(monster))
                        {
                           CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.helpShip).replace("PLAYER_NAME", CultimaPanel.player.getShortName()));
                           searchMessage = true;
                        }
                        else if(monster.getCharIndex()==NPC.TORNADO)
                        {
                           CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.helpGeneral).replace("PLAYER_NAME", CultimaPanel.player.getShortName()));
                           searchMessage = true;
                        }
                        else
                        {
                           CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.helpMonster).replace("PLAYER_NAME", CultimaPanel.player.getShortName()));
                           searchMessage = true;
                        }
                     }
                     else
                     {
                        if(Weapon.isTorch(CultimaPanel.player.getWeapon().getName()) && CultimaPanel.player.getActiveSpell("Fear")!=null)
                        {
                           CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.helpTorch).replace("PLAYER_NAME", CultimaPanel.player.getShortName()));
                           searchMessage = true;
                        }
                        else
                        {
                           CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.helpGeneral).replace("PLAYER_NAME", CultimaPanel.player.getShortName()));
                           searchMessage = true;
                        }
                     }
                     CultimaPanel.cheerTime = System.currentTimeMillis();
                  }
               }
            }
         }
      }
      int mi = p.getMapIndex();
      byte [] swimmingMonsters = {NPC.SEAMONSTER, NPC.SHARK, NPC.SQUID, NPC.WHIRLPOOL, NPC.SQUIDKING, NPC.FISH, NPC.BEAR, NPC.BEARKING, NPC.HYDRACLOPS, NPC.SHRIEKINGEEL};
      byte [] sandMonsters = {NPC.GRABOID};
      byte [] swampMonsters = {NPC.TRIFFID};
      byte [] smarterMonsters = {NPC.BRIGAND_SWORD, NPC.BRIGAND_SPEAR, NPC.BRIGAND_HAMMER, NPC.BRIGAND_DAGGER, NPC.BRIGAND_CROSSBOW, NPC.BRIGAND_FIST, NPC.BRIGANDKING, NPC.SORCERER, NPC.BRIGANDKING, NPC.BOWASSASSIN, NPC.VIPERASSASSIN, NPC.ENFORCER};
      byte[][]currMap = (CultimaPanel.map.get(mi));
      String playerSpot = CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.player.getRow()][CultimaPanel.player.getCol()])).getName().toLowerCase();
      NPCPlayer dog = getDog();
      double dogDist = Integer.MAX_VALUE;    //will be the distance between our player and our dog if we have one
      if(dog!=null)                          //used to see if our dog is close enough to scare monsters (we may have a dog in a 'stay' command off the screen that should not scare monsters)
         dogDist = Display.distanceSimple(dog.getRow(), dog.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
        //see if the monster is downwind of us - might smell us coming
      boolean downWind = false;
      double smellValue = 0;  //value to modify distance needed for a monster to detect you
      if(CultimaPanel.windDir == LocationBuilder.NORTH && p.getRow() < CultimaPanel.player.getRow() && Math.abs(p.getCol()-CultimaPanel.player.getCol()) <= 2)
         downWind = true;
      if(CultimaPanel.windDir == LocationBuilder.SOUTH && p.getRow() > CultimaPanel.player.getRow() && Math.abs(p.getCol()-CultimaPanel.player.getCol()) <= 2)
         downWind = true;
      if(CultimaPanel.windDir == LocationBuilder.EAST && p.getCol() > CultimaPanel.player.getCol() && Math.abs(p.getRow()-CultimaPanel.player.getRow()) <= 2)
         downWind = true;
      if(CultimaPanel.windDir == LocationBuilder.WEST && p.getCol() < CultimaPanel.player.getCol() && Math.abs(p.getRow()-CultimaPanel.player.getRow()) <= 2)
         downWind = true;
      if(downWind)
         smellValue = 1.5;   
      NPCPlayer[] adjacentMonsters = getAdjacentMonsters(p.getRow(), p.getCol(), mi);
      boolean nextToMonster = false;
      boolean nextToFire = false;
      for(NPCPlayer adjMonster:adjacentMonsters)
      {
         if(adjMonster!=null && !NPC.isTameAnimal(adjMonster.getCharIndex()))
            nextToMonster = true;
         if(adjMonster!=null && adjMonster.getCharIndex()==NPC.FIRE)
            nextToFire = true;
      }
      //**********setting move types for running, roaming or chasing when hiding/fear/charm is not an option to change it
      if(NPC.isFrightenedAnimal(p.getCharIndex()) && (nextToMonster || nextToFire))
      {  //animals gets spooked by a monster or fire
         p.setMoveTypeTemp(NPC.RUN);
         if(p.hasMet())
            CultimaPanel.player.setHorseBasicInfo("none");
         return searchMessage;
      }
      if(NPC.isVampire(p) && nextToFire)
      {  //vamps gets spooked by fire
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      } 
      if(NPC.isHorse(p) && dist < CultimaPanel.SCREEN_SIZE/(4-smellValue))
      {  //alignment compatable horses can be tamed if player doesn't have a weapon in hand
         if(CultimaPanel.player.isWolfForm() || p.hasBeenAttacked())
         {
            CultimaPanel.player.setHorseBasicInfo("none");
            p.setMoveType(NPC.RUN);
            p.setHasMet(false);        //if it is a horse, they are done with us
         }
         else if(p.hasMet())                                           //a tamed horse
            p.setMoveTypeTemp(NPC.STILL);
         else if(!horseCanBeTamed(p))                               
            p.setMoveTypeTemp(NPC.RUN);                        //horse runs if player is not alignment compatable
         return searchMessage;
      }
      if(mi == 0 && (p.getCharIndex()==NPC.DRAGON || p.getCharIndex()==NPC.DRAGONKING) && CultimaPanel.weather > 1)
      {
         p.setMoveTypeTemp(NPC.RUN);      //dragons retreat in the rain
         return searchMessage;
      }
      if(p.getCharIndex() == NPC.DEATH)
      {  //death can not see you on a ship
         if(CultimaPanel.player.isSailing())
            p.setMoveType(NPC.RUN);
         else if(CultimaPanel.player.isInvisible())
            p.setMoveType(NPC.ROAM);
         else
            p.setMoveType(NPC.CHASE);
         return searchMessage;   
      }
      if((p.getCharIndex()==NPC.RABBIT && dist < CultimaPanel.SCREEN_SIZE/(5-smellValue)) || (p.getCharIndex()==NPC.ELK && dist < CultimaPanel.SCREEN_SIZE/(3-smellValue)))
      {  //some animals always run
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      }
      if((p.getCharIndex()==NPC.CHICKEN || p.getCharIndex()==NPC.PIG || p.getCharIndex()==NPC.FISH) && dist < 2)
      {  //some animals always run
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      }
      if(p.getCharIndex()==NPC.UNICORN && dist < 3 && CultimaPanel.player.numFlowers() < 5 && !p.hasBeenAttacked())
      {  //unicorn runs if you don't have enough flowers
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      }
      if(p.getCharIndex()==NPC.RUSTCREATURE)
      {
         if(p.getBody() < ((p.getMight()*5)/2))
         {  //some monsters run when hurt to half their helth
            p.setMoveTypeTemp(NPC.RUN);
         }
         else if(p.hasBeenAttacked() || (dist <= 2 && (CultimaPanel.player.getWeapon().isMetal() || CultimaPanel.player.getArmor().isMetal())))
         {  //rust creatures will chase you down if they smell metal on you
            p.setMoveType(NPC.CHASE);
         }
         return searchMessage;
      }
      if(p.getCharIndex()==NPC.MIMIC)
      {
         if(p.hasBeenAttacked() || dist <= 1)
         {  //mimics always attack if you get too close
            if(!p.hasBeenAttacked())
               Sound.mimic();
            p.setMoveType(NPC.CHASE);
            p.setHasBeenAttacked(true);
         }
         else
         {  //mimics stay stationary to lure you in
            p.setMoveType(NPC.STILL);
         }
         return searchMessage;
      } 
      if(NPC.isUndead(p.getCharIndex()) && CultimaPanel.player.getWeapon().getName().toLowerCase().contains("life") && !CultimaPanel.player.getLocationType().toLowerCase().contains("underworld"))
      {  //the undead run from life weapons
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      }
      if(p.getCharIndex()==NPC.ORC && !CultimaPanel.player.getDogName().equals("none") && dogDist <= CultimaPanel.SCREEN_SIZE/2 && !p.hasBeenAttacked())
      {  //Orcs are afraid of dogs
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      }
      if((p.getCharIndex()==NPC.GOBLIN || p.getCharIndex()==NPC.GOBLINBARREL || p.getCharIndex()==NPC.BAT || p.getCharIndex()==NPC.BATKING  || p.getCharIndex()==NPC.NIGHTORC || (p.getCharIndex()==NPC.SPIDER && CultimaPanel.player.getLocationType().toLowerCase().contains("cave"))) && (CultimaPanel.player.getActiveSpell("Light")!=null))
      {  //some creatures are afraid of light
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      }
      if((p.getCharIndex()==NPC.WOLF || p.getCharIndex()==NPC.BEAR) && !CultimaPanel.player.getDogName().equals("none") && dogDist <= CultimaPanel.SCREEN_SIZE/2 && !p.hasBeenAttacked() && !p.isOurDog())
      {  //wolves and bears leave you alone if you have a dog
         p.setMoveTypeTemp(NPC.ROAM);
         return searchMessage;
      }
      if((p.getCharIndex()==NPC.SHARK || p.getCharIndex()==NPC.SHRIEKINGEEL) && CultimaPanel.player.isSailing())
      {  //sharks don't mess with ships
         p.setMoveTypeTemp(NPC.ROAM);
         return searchMessage;
      }
      if(p.getCharIndex()==NPC.BAT && p.getName().contains("~"))
      {  //this is a vampire trying to get away - don't set to attack
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      } 
      if((p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN) && p.isWerewolf() && p.getMoveType()==NPC.CHASE)
      {  //this is a werewolf turned back human - don't set to attack
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      }
      if(p.getBody() < 10 && p.getCharIndex()!=NPC.SKELETON && p.getCharIndex()!=NPC.SQUID && p.getCharIndex()!=NPC.SPIDER && p.getCharIndex()!=NPC.MIMIC && p.hasBeenAttacked() && !NPC.isBlackKnight(p.getCharIndex()))
      {  //some monsters run when hurt
         p.setMoveTypeTemp(NPC.RUN);
         return searchMessage;
      }
      if(CultimaPanel.player.getReputation()<=-1000 && (p.getCharIndex()==NPC.DEMON || p.getCharIndex()==NPC.DEMONKING) && !p.hasBeenAttacked())
      {
         p.setMoveTypeTemp(NPC.ROAM);     //demons will respect you and leave you be if you are evil enough
         return searchMessage;
      }
      if(CultimaPanel.player.isVampire() && (p.getCharIndex()==NPC.DEMON || p.getCharIndex()==NPC.DEMONKING || p.getCharIndex()==NPC.BAT || p.getCharIndex()==NPC.BATKING || NPC.isVampire(p)) && !p.hasBeenAttacked())
      {
         p.setMoveTypeTemp(NPC.ROAM);     //demons, bats and vampires will leave you be if you are a vampire
         return searchMessage;
      }
      if(CultimaPanel.player.isWerewolf() && (p.getCharIndex()==NPC.WOLF || p.getCharIndex()==NPC.WOLFKING || p.getCharIndex()==NPC.WEREWOLF) && !p.hasBeenAttacked())
      {
         p.setMoveTypeTemp(NPC.ROAM);     //wolves will leave yo be if you are a werewolf
         return searchMessage;
      }
      if(CultimaPanel.player.getBody() <= 0 && p.getCharIndex()!=NPC.TREE && p.getCharIndex()!=NPC.TREEKING)         
      {
         p.setMoveTypeTemp(NPC.ROAM);     //make sure that creatures don't continue to attack you when dead
         return searchMessage;
      }
      if((p.getCharIndex()==NPC.DOG && !p.hasMet() || NPC.isFrightenedAnimal(p.getCharIndex())))
      {
         if(!p.hasBeenAttacked())
         {
            p.setMoveType(NPC.ROAM);
         }
         else
         {
            if(p.getCharIndex()==NPC.DOG && p.getReputation() < 0 && dist < CultimaPanel.SCREEN_SIZE/4 )
            {
               p.setMoveTypeTemp(NPC.CHASE);
            }
            else
            {
               p.setMoveTypeTemp(NPC.RUN);
            }
         }
         return searchMessage;
      }
      if((p.getCharIndex()==NPC.WOLF || p.getCharIndex()==NPC.WOLFKING) && p.hasBeenAttacked() && !CultimaPanel.player.isWolfForm() && !p.isOurDog())
      {  //if one wolf is attacked by player, others in sight will be set to chase
         p.setMoveType(NPC.CHASE);
         for(NPCPlayer closeWolf: CultimaPanel.NPCinSight)
         {
            if((closeWolf.getCharIndex()==NPC.WOLF || closeWolf.getCharIndex()==NPC.WOLFKING) && closeWolf.getMoveType()!=NPC.CHASE)
            {
               closeWolf.setHasBeenAttacked(true);
               closeWolf.setMoveType(NPC.CHASE);
            }
         }
         return searchMessage;
      } 
      
      //**********setting move types for chasing when hiding/fear/charm is an option to change it
      int tempDist = 0;          //used to add to the distance in which evil npcs will see you
      if(p.getMapIndex() != 0)   //the distance is greater indoors
      {
         tempDist = 4;
      }
      if(p.getCharIndex()==NPC.JESTER && p.getReputation() < -10 && dist < CultimaPanel.SCREEN_SIZE/3 && !p.hasBeenAttacked() && p.getMoveType()!=NPC.RUN)
      {  //evil jesters will follow and annoy you
         if(p.getName().startsWith("Masterthief"))
         {
            p.setMoveTypeTemp(NPC.RUN);
         }
         else
         {
            p.setMoveTypeTemp(NPC.CHASE);
         }
      }
      else if((p.getCharIndex()==NPC.ORC || p.getCharIndex()==NPC.TROLL || NPC.isBrigand(p.getCharIndex())) && CultimaPanel.player.getReputation() <= -1000  && dist < CultimaPanel.SCREEN_SIZE/4)
      {  //some monsters run if player is very evil
         p.setMoveTypeTemp(NPC.RUN);
         if(p.hasBeenAttacked() && Math.abs(p.getLevel() - CultimaPanel.player.getLevel()) <= 10)
         {  //some will fight back if levels are close enough
            p.setMoveTypeTemp(NPC.CHASE);
         }
      }
      else if(NPC.isBrigand(p.getCharIndex()) && p.hasBeenAttacked() )
      {  //if one brigand is attacked by a player, others will be set to chase
         p.setMoveType(NPC.CHASE);
         alertBrigands();
      } 
      else if(p.getCharIndex()==NPC.CAERBANNOG)
      {
         if(p.hasBeenAttacked() || dist <= 4)
         {  //the beast of Caerbannog will chase relentlessly if you get too close
            p.setMoveType(NPC.CHASE);
            p.setHasBeenAttacked(true);
         }
         else
         {
            p.setMoveType(NPC.STILL);
         }
      }
      else if(NPC.isSmeller(p) && dist < CultimaPanel.SCREEN_SIZE/(3-smellValue)+tempDist && p.getReputation() < 0 && !p.isOurDog())
      {  //some monsters chase when player gets close depending on smell ability and winds
         p.setMoveTypeTemp(NPC.CHASE);
      }
      else if(!NPC.isCivilian(p) && dist < CultimaPanel.SCREEN_SIZE/4+tempDist && p.getReputation() < 0 && !p.isOurDog())
      {
         p.setMoveTypeTemp(NPC.CHASE);
         if(CultimaPanel.player.getActiveSpell("Charm")!=null && p.getReputation() >= (-100 - (CultimaPanel.player.getMind()*2)) )
         {
            if((NPC.isAnyBrigand(p.getCharIndex()) || p.getCharIndex()==NPC.CYCLOPS || p.getCharIndex()==NPC.TROLL) &&  rollDie(100) < CultimaPanel.player.getMind())
            {  //there is a chance we can charm a brigand, cyclops or troll
               p.setMoveTypeTemp(NPC.ROAM);
            }
         }
      }
      
      if(p.hasBeenAttacked())             //switch to run or chase if we have been attacked
      {
         if((p.getMoveType()==NPC.CHASE && p.getCharIndex()!=NPC.JESTER) || NPC.isBlackKnight(p.getCharIndex()))
         {}
         else
         {
            p.setMoveType(NPC.CHASE);
               
            if(NPC.isFrightenedAnimal(p.getCharIndex()) || p.getCharIndex()==NPC.BAT || NPC.isTameableCanine(p.getCharIndex()) || NPC.isNonCombativeCivilian(p))
            {
               p.setMoveType(NPC.RUN);
               if(NPC.isHorse(p))
               {
                  p.setHasMet(false);        //if it is a horse and we attacked it, they are done with us
               }
            }
            if(NPC.isCivilian(p) || NPC.isSoldier(p))
            {
               int npcLevel = p.getLevel();
               int playerLevel = CultimaPanel.player.getLevel();
               if((npcLevel > playerLevel && Math.random() < .9) || NPC.isGuard(p.getCharIndex()) || p.getCharIndex()==NPC.SWORD || p.getCharIndex()==NPC.SOLDIER || p.getCharIndex()==NPC.OFFICER)
               {
                  p.setMoveTypeTemp(NPC.CHASE);
               }
            }
         }
      }
      
      if(CultimaPanel.player.isHiding() && isNPCInSight(p) && p.getMoveType()==NPC.CHASE && (NPC.isBrigand(p.getCharIndex()) || NPC.isGuard(p)) && !searchMessage && (System.currentTimeMillis() > CultimaPanel.cheerTime + 7500))
      {
         CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.searchForHidingPlayer).replace("PLAYER_NAME", CultimaPanel.player.getShortName()).replace("ADJECTIVE",NPC.getRandomFrom(NPC.insultAdjective)).replace("NOUN",NPC.getRandomFrom(NPC.insultNoun)));
         searchMessage = true;
         CultimaPanel.cheerTime = System.currentTimeMillis();
      }
      if(NPC.isDirectionVision(p) && isNPCInSight(p) && dist <= CultimaPanel.SCREEN_SIZE/4)
      {
         boolean afraidOfDog = (p.getReputation() < 0 && dogDist <= CultimaPanel.SCREEN_SIZE/2);
         boolean holiday = ((CultimaPanel.days % 100) == 50);   
         if(p.getCharIndex()==NPC.TAXMAN  && !p.hasBeenAttacked() && (CultimaPanel.days <= p.getEndChaseDay() || p.getNumInfo()==10 || CultimaPanel.player.hasItem("blessed-crown") || afraidOfDog || holiday))
         {  //taxman does not chase you if they have a mission or you have the blessed crown or its a holiday or they are afraid of your dog so long as they have not been attacked
            p.setMoveType(NPC.ROAM);
         }
         else
         {
            boolean spotted = false;
            int dir = p.getAnimationIndex();
            if(dir == 0 && CultimaPanel.player.getRow() < p.getRow() && Math.abs(CultimaPanel.player.getCol()-p.getCol()) < dist)        //facing NORTH
               spotted = true;
            else if(dir == 1 && CultimaPanel.player.getCol() > p.getCol() && Math.abs(CultimaPanel.player.getRow()-p.getRow()) < dist)   //facing EAST
               spotted = true;
            else if(dir == 2 && CultimaPanel.player.getRow() > p.getRow() && Math.abs(CultimaPanel.player.getCol()-p.getCol()) < dist)   //facing SOUTH
               spotted = true;
            else if(dir == 3 && CultimaPanel.player.getCol() < p.getCol() && Math.abs(CultimaPanel.player.getRow()-p.getRow()) < dist)   //facing WEST
               spotted = true;
            if(spotted && !CultimaPanel.player.isInvisible())
            {
               if(CultimaPanel.player.getImageIndex()==Player.LUTE && !CultimaPanel.player.isOnGuard())
               {
                  if(p.getCharIndex()==NPC.OFFICER || p.getMind() > CultimaPanel.player.getMind())                                        //officer is not fooled
                  {
                     p.setMoveType(NPC.CHASE);
                  }   
               }
               else
               {
                  p.setMoveType(NPC.CHASE);
               }
               if(System.currentTimeMillis() > CultimaPanel.cheerTime + 1000)
               {
                  if(p.getCharIndex()==NPC.OFFICER || p.getCharIndex()==NPC.SOLDIER)
                  {  //have German soldier yell out a Wolfenstein quote
                     CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.wolfensteinAlarm));
                  }
                  else if(NPC.isGuard(p.getCharIndex()))
                  {  //have guard yell out
                     CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.guardAlarm).replace("ADJECTIVE",NPC.getRandomFrom(NPC.insultAdjective)).replace("NOUN",NPC.getRandomFrom(NPC.insultNoun)));
                  }
                  CultimaPanel.cheerTime = System.currentTimeMillis();
               }
            }
         }
      }
      if(p.getMoveType() == NPC.CHASE && !search(swimmingMonsters, p.getCharIndex()) && !search(smarterMonsters, p.getCharIndex()))
      {  //chasing monsters might break off their chase if the player is hiding in water
         if(playerSpot.contains("water") && Math.random() < 0.5)
            p.setMoveType(NPC.ROAM);
      }
      if(p.getCharIndex() == NPC.DRAGON || p.getCharIndex() == NPC.GIANT || p.getCharIndex() == NPC.CYCLOPS || p.getCharIndex() == NPC.TANK || p.getCharIndex() == NPC.DEMON || NPC.isMonsterKing(p.getCharIndex()))
      {  //big monsters break off their chase if the player is hiding in the dense forest
         if(playerSpot.contains("dense_forest") || ((p.getCharIndex() == NPC.DRAGON || p.getCharIndex() == NPC.DRAGONKING || p.getCharIndex() == NPC.DEMON || p.getCharIndex() == NPC.DEMONKING) && playerSpot.contains("water") && !CultimaPanel.player.isSailing()) )
         {
            p.setMoveType(NPC.ROAM);
         }
         else
         {
            p.setMoveType(NPC.CHASE);
         }
      }
      if((CultimaPanel.player.getImageIndex()==Player.LUTE && !CultimaPanel.player.isOnGuard()) && !p.isZombie() && !p.getName().toLowerCase().contains("puzzle"))  //the Lute Of Destiny can charm some creatures
      {    
         if(p.getCharIndex()==NPC.LUTE || p.getCharIndex()==NPC.CHILD)
         {
            p.setMoveTypeTemp(NPC.FOLLOW);
         }
         int numSongPages = CultimaPanel.player.getItemFrequency("Songpage");
         if(CultimaPanel.player.hasItem("charmring") && CultimaPanel.player.getActiveSpell("Charm")!=null)
         {
            numSongPages++;
         }
         ArrayList<Byte> dancers = new ArrayList<Byte>();
         //monsters that must dance
         dancers.add(NPC.CYCLOPS);
         dancers.add(NPC.GIANT);
         dancers.add(NPC.TROLL);
         dancers.add(NPC.ORC);
         //civilians that must dance
         dancers.add(NPC.BEGGER);
         dancers.add(NPC.JESTER);
         dancers.add(NPC.MAN);
         dancers.add(NPC.WOMAN);
         dancers.add(NPC.WISE);
         dancers.add(NPC.KING);
         dancers.add(NPC.SOLDIER);
         if(numSongPages >= 1)
         {
            dancers.add(NPC.BRIGAND_SWORD);
            dancers.add(NPC.BRIGAND_SPEAR);
            dancers.add(NPC.BRIGAND_HAMMER);
            dancers.add(NPC.BRIGAND_DAGGER);
            dancers.add(NPC.BRIGAND_CROSSBOW);
            dancers.add(NPC.BRIGAND_FIST);
            //civilians that must dance
            dancers.add(NPC.GUARD_SPEAR);
            dancers.add(NPC.GUARD_SWORD);
            dancers.add(NPC.GUARD_FIST);
         
         }
         if(numSongPages >= 2)
         {
            dancers.add(NPC.SORCERER);
            dancers.add(NPC.DEMON);
            dancers.add(NPC.BUGBEAR);
            //civilians that must dance
            dancers.add(NPC.SWORD);
            dancers.add(NPC.ROYALGUARD);
            dancers.add(NPC.OFFICER);
         }
         if(numSongPages >= 3 || (numSongPages >= 2 || (CultimaPanel.player.hasItem("charmring")) && CultimaPanel.player.getActiveSpell("Charm")!=null))
         {
            dancers.add(NPC.BOWASSASSIN);
            dancers.add(NPC.VIPERASSASSIN);
            dancers.add(NPC.ENFORCER);
            dancers.add(NPC.DEMONKING);
            dancers.add(NPC.BUGBEARKING);
            dancers.add(NPC.TROLLKING);
         }
            
         if(!p.hasBeenAttacked() && dancers.contains(p.getCharIndex()))
         {
            if(NPC.isCivilian(p))
            {  //civilians will follow
               if(Math.random() < (0.2 + numSongPages/10.0))
               {
                  p.setMoveTypeTemp(NPC.FOLLOW);
                  if(p.getCharIndex()==NPC.ROYALGUARD)
                     Achievement.earnAchievement(Achievement.INFECTIOUS_GROOVES);
               }
               else
                  p.setMoveType(p.getOrigMoveType());
            }
            else
            {  //monsters will roam
               p.setMoveTypeTemp(NPC.ROAM);
            }
         }    
      }
      else
      {
         if(p.getMoveType()==NPC.FOLLOW)
            p.setMoveType(p.getOrigMoveType());
      }
   
      //wearing an animal skin will allow you to pass among them
      if((CultimaPanel.player.getArmor().getName().toLowerCase().contains("wolf") || CultimaPanel.player.isWolfForm()) && (p.getCharIndex()==NPC.WOLF || p.getCharIndex()==NPC.WOLFKING || p.getCharIndex()==NPC.WEREWOLF) && !p.hasBeenAttacked() && !p.isOurDog())
         p.setMoveTypeTemp(NPC.ROAM);
      if(CultimaPanel.player.getArmor().getName().toLowerCase().contains("bear") && (p.getCharIndex()==NPC.BEAR || p.getCharIndex()==NPC.BEARKING) && !p.hasBeenAttacked())
         p.setMoveTypeTemp(NPC.ROAM);
      if(CultimaPanel.player.getArmor().getName().toLowerCase().contains("snake") && (p.getCharIndex()==NPC.SNAKE || p.getCharIndex()==NPC.SNAKEKING) && !p.hasBeenAttacked())
         p.setMoveTypeTemp(NPC.ROAM);
      if(CultimaPanel.player.getArmor().getName().toLowerCase().contains("elk") && (p.getCharIndex()==NPC.ELK) && !p.hasBeenAttacked())
         p.setMoveTypeTemp(NPC.ROAM);
      if(NPC.isBrigand(p.getCharIndex()) && CultimaPanel.player.hasItem("robertsmask") && !p.hasBeenAttacked() && !p.getLocationType().contains("battlefield")  && !p.getLocationType().contains("arena") && !p.getName().toLowerCase().contains("sibling") && !p.getName().toLowerCase().contains("brute"))
         p.setMoveTypeTemp(NPC.ROAM);
         
      if(CultimaPanel.player.isInvisible() && p.getCharIndex()!=NPC.DEMON && p.getCharIndex()!=NPC.DEMONKING)
         p.setMoveTypeTemp(NPC.ROAM);
      if(CultimaPanel.player.getActiveSpell("Fear")!=null)
      {
         if(p.isOurDog() || p.isEscortedNpc())
         {}
         else if(NPC.isFrightenedAnimal(p.getCharIndex()) || NPC.isHumanoid(p.getCharIndex()))
            p.setMoveTypeTemp(NPC.RUN); 
         else if(CultimaPanel.player.getImageIndex()==Player.DEMON && (p.getCharIndex()==NPC.DEMON || p.getCharIndex()==NPC.DEMONKING))
            p.setMoveTypeTemp(NPC.ROAM);
         else if(CultimaPanel.player.getImageIndex()==Player.DRAGON && (p.getCharIndex()==NPC.DRAGON || p.getCharIndex()==NPC.DRAGONKING))
            p.setMoveTypeTemp(NPC.ROAM);
         else if(CultimaPanel.player.getImageIndex()==Player.TROLL && (p.getCharIndex()==NPC.TROLL || p.getCharIndex()==NPC.TROLLKING))
            p.setMoveTypeTemp(NPC.ROAM);
         else if(CultimaPanel.player.getImageIndex()==Player.BUGBEAR && (p.getCharIndex()==NPC.BUGBEAR || p.getCharIndex()==NPC.BUGBEARKING))
            p.setMoveTypeTemp(NPC.ROAM);
         else if(CultimaPanel.player.getImageIndex()==Player.CYCLOPS && p.getCharIndex()==NPC.CYCLOPS)
            p.setMoveTypeTemp(NPC.ROAM);
         else if(CultimaPanel.player.getImageIndex()==Player.GHOST && (p.getCharIndex()==NPC.GHOST || p.getCharIndex()==NPC.PHANTOM))
            p.setMoveTypeTemp(NPC.ROAM);
         else if(CultimaPanel.player.getMind() >40 && NPC.getSize(p.getCharIndex()) < 2)
            p.setMoveTypeTemp(NPC.RUN);
         else if(CultimaPanel.player.getMind() >=16 && NPC.getSize(p.getCharIndex()) < 1.5)
            p.setMoveTypeTemp(NPC.RUN); 
         else
            p.setMoveTypeTemp(NPC.ROAM);   
      }
      if(CultimaPanel.player.isWolfForm())
      {
         if(p.getCharIndex()==NPC.ROYALGUARD || (NPC.isGuard(p.getCharIndex()) && (Math.abs(p.getLevel()-CultimaPanel.player.getLevel()) < 5 || (p.hasBeenAttacked() && p.getBody() > 20))) )
         {  //royal guards have silver weapons to slay werewolves, and guards whose level is within 5 of the player will try to help
            p.setMoveTypeTemp(NPC.CHASE); 
         }
         else if(NPC.getSize(p.getCharIndex()) >= 2)
         {
            p.setMoveTypeTemp(NPC.CHASE);
         }
         else if(NPC.getSize(p.getCharIndex()) >= 1.5)
         {
            if(p.hasBeenAttacked())
            {
               p.setMoveTypeTemp(NPC.CHASE);
            }
            else
            {
               p.setMoveTypeTemp(NPC.ROAM);
            }
         }
         else if(NPC.isHumanoid(p.getCharIndex()) || NPC.isTameAnimal(p.getCharIndex()))
            p.setMoveTypeTemp(NPC.RUN);
      }
      return searchMessage;
   }

   //set NPCs not indoors on fire
   public static void firestorm()
   {
      boolean indoors = TerrainBuilder.indoors();
      double mindPercent = (CultimaPanel.player.getMind() / 50.0);                //the percentage we are to max mind, used to do more damage depending on level
      int midDamage = rollDie(Weapon.getFireball().getMinDamage(), Weapon.getFireball().getMaxDamage());     
      int damageBoost = (int)(midDamage * mindPercent);  //the amount of extra damage done from this spell
      for(NPCPlayer p:CultimaPanel.NPCinSight)
      {
         if(p.getCharIndex()==NPC.DRAGON || p.getCharIndex()==NPC.DRAGONKING || p.getCharIndex()==NPC.DEMON || p.getCharIndex()==NPC.DEMONKING
         || p.getCharIndex()==NPC.MAGMA || p.getCharIndex()==NPC.FIRE ||  p.getCharIndex()==NPC.GHOST ||  p.getCharIndex()==NPC.PHANTOM || p.getCharIndex()==NPC.MAZEMOUTH
         || p.getCharIndex()==NPC.WHIRLPOOL || p.getCharIndex()==NPC.TORNADO || p.getCharIndex()==NPC.WILLOWISP || p.isStatue()|| p.isNonPlayer())
            continue;
         p.damage(damageBoost,"fire");
         if(!TerrainBuilder.indoors(p) && rollDie(10,CultimaPanel.weather * 10) > rollDie(p.getAgility()-CultimaPanel.weather))
         {
            p.addEffect("fire");
         }
      }
   }
   
  //a random fire starts on a combustable place on the screen (lightning strike)
   public static void startRandomFire()
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      int mi = CultimaPanel.player.getMapIndex();
      String locType = CultimaPanel.player.getLocationType();
      byte[][]currMap = CultimaPanel.map.get(mi);
      for(int r=CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2; r++)
      {
         for(int c=CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            int row = r;
            int col = c;
            if(CultimaPanel.player.getMapIndex() == 0)
            {
               row = CultimaPanel.equalizeWorldRow(r);
               col = CultimaPanel.equalizeWorldCol(c);
               if(LocationBuilder.isCombustable(currMap, row, col) && !NPCAt(row, col, mi))
                  locs.add(new Coord(row, col));    
            }
            else //if(CultimaPanel.player.getMapIndex() != 0)
            {
               if(row>=0 && row<currMap.length && col>=0 && col<currMap[0].length && LocationBuilder.isCombustable(currMap, row, col) && !NPCAt(row, col, mi))
                  locs.add(new Coord(row, col));
            } 
         }
      }
      if(locs.size() > 0)
      {
         Coord pick = getRandomFrom(locs);
         int r = pick.getRow();
         int c = pick.getCol();
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, r, c, mi, locType));
      }
   }
   
     //a random fire starts on a combustable place on the screen close to player's location (arson of your house)
   public static void startRandomArsonFire()
   {
      int range = 5;
      ArrayList<Coord> locs = new ArrayList<Coord>();
      int mi = CultimaPanel.player.getMapIndex();
      String locType = CultimaPanel.player.getLocationType();
      byte[][]currMap = CultimaPanel.map.get(mi);
      for(int r=CultimaPanel.player.getRow()-range; r<=CultimaPanel.player.getRow()+range; r++)
      {
         for(int c=CultimaPanel.player.getCol()-range; c<=CultimaPanel.player.getCol()+range; c++)
         {
            int row = r;
            int col = c;
            if(CultimaPanel.player.getMapIndex() == 0)
            {
               row = CultimaPanel.equalizeWorldRow(r);
               col = CultimaPanel.equalizeWorldCol(c);
               if(LocationBuilder.isCombustable(currMap, row, col) && !NPCAt(row, col, mi))
                  locs.add(new Coord(row, col));    
            }
            else //if(CultimaPanel.player.getMapIndex() != 0)
            {
               if(row>=0 && row<currMap.length && col>=0 && col<currMap[0].length && LocationBuilder.isCombustable(currMap, row, col) && !NPCAt(row, col, mi))
                  locs.add(new Coord(row, col));
            } 
         }
      }
      if(locs.size() > 0)
      {
         Coord pick = getRandomFrom(locs);
         int r = pick.getRow();
         int c = pick.getCol();
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, r, c, mi, locType));
      }
   }
   
   public static void startRandomFire(int mapIndex)
   {
      ArrayList<Coord> locs = getFireLocations(mapIndex);
      String locType = CultimaPanel.player.getLocationType();
      if(locs.size() > 0)
      {
         Coord pick = getRandomFrom(locs);
         int r = pick.getRow();
         int c = pick.getCol();
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, r, c, mapIndex, locType));
      }
   }
   
   public static ArrayList<Coord> getFireLocations(byte[][]currMap, int mapIndex)
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      for(int r=0; r<currMap.length; r++)
      {
         for(int c=0; c<currMap[0].length; c++)
         {
            int row = r;
            int col = c;
            if(LocationBuilder.isCombustable(currMap, row, col) && !NPCAt(row, col, mapIndex))
               locs.add(new Coord(row, col));
         }
      }
      return locs;
   }
   
   public static ArrayList<Coord> getFireLocations(int mapIndex)
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      if(mapIndex<=0 || mapIndex>=CultimaPanel.map.size())
         return locs;     //we only want to call this for city locations - not the world
      byte[][]currMap = CultimaPanel.map.get(mapIndex);
      String locType = CultimaPanel.player.getLocationType();
      for(int r=0; r<currMap.length; r++)
      {
         for(int c=0; c<currMap[0].length; c++)
         {
            int row = r;
            int col = c;
            if(LocationBuilder.isCombustable(currMap, row, col) && !NPCAt(row, col, mapIndex))
               locs.add(new Coord(row, col));
         }
      }
      return locs;
   }
   
   public static void spreadFires()
   {
      ArrayList<Coord> firesToAdd = new ArrayList<Coord>();
      ArrayList<Coord> smallFiresToAdd = new ArrayList<Coord>();
   
      int mi = CultimaPanel.player.getMapIndex();
      String locType = CultimaPanel.player.getLocationType();
      int numFires = 0;
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(i);
         if(p.getMapIndex() != mi)
            continue;
         if(p.getCharIndex()!=NPC.FIRE && !p.hasEffect("fire"))
            continue;
         if(p.getBody() <= 0)
         {
            Corpse dead = p.getCorpse();
            CultimaPanel.corpses.get(mi).add(dead);
            CultimaPanel.worldMonsters.remove(i);
            continue;
         }
         boolean isFire = false;
         if(p.getCharIndex()==NPC.FIRE)
         {
            numFires++;
            isFire = true;
         }
         byte[][]currMap = (CultimaPanel.map.get(mi));   
         int row = p.getRow();
         int col = p.getCol();
         if(row < 0 || col < 0 || row >= currMap.length ||col >= currMap[0].length)
            continue;
         if(p.getCharIndex()!=NPC.FIRE && p.hasEffect("fire"))
         { 
            if(p.getLocationType().equals("ship")) //only spread fires intentionally on a ship
               continue;  
            if(Math.random() < 0.25)                //make those set on fire spread the fire less often
               continue;                   
         }
         for(int r=row - 1; r<=row+1; r++)
         {
            for(int c=col - 1; c<=col + 1; c++)
            {
               if(r==row && c==col)
                  continue;
               int checkRow = r;
               int checkCol = c;   
               if(mi != 0)
               {
                  if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
                     continue;   
               }
               else
               {
                  checkRow = CultimaPanel.equalizeWorldRow(r);
                  checkCol = CultimaPanel.equalizeWorldCol(c);
               }
            
               if(LocationBuilder.isCombustable(currMap, checkRow, checkCol))
               {  //if there is no NPC there, add the point into firesToAdd
                  boolean NPCthere = NPCAt(checkRow, checkCol, mi);
                  double fireSpreadProb = 0.05;
                  if(CultimaPanel.windDir == LocationBuilder.NORTH)
                  {
                     if(checkRow < row)
                        fireSpreadProb = 0.06;
                     else if(checkRow == row)
                        fireSpreadProb = 0.04;
                     else
                        fireSpreadProb = 0.02;      
                  }
                  else if(CultimaPanel.windDir == LocationBuilder.SOUTH)
                  {
                     if(checkRow > row)
                        fireSpreadProb = 0.06;
                     else if(checkRow == row)
                        fireSpreadProb = 0.04;
                     else
                        fireSpreadProb = 0.02;      
                  }
                  else if(CultimaPanel.windDir == LocationBuilder.EAST)
                  {
                     if(checkCol > col)
                        fireSpreadProb = 0.06;
                     else if(checkCol == col)
                        fireSpreadProb = 0.04;
                     else
                        fireSpreadProb = 0.02;      
                  }
                  else if(CultimaPanel.windDir == LocationBuilder.WEST)
                  {
                     if(checkCol < col)
                        fireSpreadProb = 0.06;
                     else if(checkCol == col)
                        fireSpreadProb = 0.04;
                     else
                        fireSpreadProb = 0.02;      
                  }
                  boolean fireRoll = (rollDie(3) >= CultimaPanel.weather && Math.random()<fireSpreadProb);
                  if(!NPCthere && fireRoll)
                  {
                     if(isFire)
                        firesToAdd.add(new Coord(checkRow, checkCol));
                     else
                        smallFiresToAdd.add(new Coord(checkRow, checkCol));
                  }
               }
            }
         }
      }
      for(int i=CultimaPanel.civilians.get(mi).size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.civilians.get(mi).get(i);
         if(p.getMapIndex() != mi)
            continue;
         if(p.getCharIndex()!=NPC.FIRE && !p.hasEffect("fire"))
            continue;
         byte[][]currMap = (CultimaPanel.map.get(mi));   
         int row = p.getRow();
         int col = p.getCol();
         if(row < 0 || col < 0 || row >= currMap.length ||col >= currMap[0].length)
            continue;
         if(p.getCharIndex()!=NPC.FIRE && p.hasEffect("fire"))
         { 
            if(p.getLocationType().equals("ship")) //only spread fires intentionally on a ship
               continue;  
            if(Math.random() < 0.25)                //make those set on fire spread the fire less often
               continue;                   
         }
         boolean isFire = false;
         if(p.getCharIndex()==NPC.FIRE)
            isFire = true;
         for(int r=row - 1; r<=row+1; r++)
         {
            for(int c=col - 1; c<=col + 1; c++)
            {
               if(r==row && c==col)
                  continue;
               int checkRow = r;
               int checkCol = c;   
               if(mi != 0)
               {
                  if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
                     continue;   
               }
               else
               {
                  checkRow = CultimaPanel.equalizeWorldRow(r);
                  checkCol = CultimaPanel.equalizeWorldCol(c);
               }
            
               if(LocationBuilder.isCombustable(currMap, checkRow, checkCol))
               {  //if there is no NPC there, add the point into firesToAdd
                  boolean NPCthere = NPCAt(checkRow, checkCol, mi);
                  double fireSpreadProb = 0.02;
                  boolean fireRoll = (rollDie(5) >= CultimaPanel.weather && Math.random()<fireSpreadProb);
                  if(!NPCthere && fireRoll)
                  {
                     if(isFire)
                        firesToAdd.add(new Coord(checkRow, checkCol));
                     else
                        smallFiresToAdd.add(new Coord(checkRow, checkCol));
                  }
               }
            }
         }
      }
      //firesToAdd to be added to worldMonsters
      if(numFires < 20)
      {
         for(Coord p:firesToAdd)
         {
            CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, p.getRow(), p.getCol(), mi, locType));
            numFires++;
            if(numFires >= 20)
               break;
         }
         for(Coord p:smallFiresToAdd)
         {
            NPCPlayer fire = new NPCPlayer(NPC.FIRE, p.getRow(), p.getCol(), mi, locType);
            fire.setBody(rollDie(10,35));
            CultimaPanel.worldMonsters.add(fire);
            numFires++;
            if(numFires >= 20)
               break;
         }
      }
   }
   
   public static void spreadFiresFromLava(ArrayList<Coord>fires)
   {
      if(fires==null || fires.size()==0)
         return;
      ArrayList<Coord> firesToAdd = new ArrayList<Coord>();
      int mi = CultimaPanel.player.getMapIndex();
      String locType = CultimaPanel.player.getLocationType();
      for(int i=fires.size()-1; i>=0; i--)
      {
         Coord p = fires.get(i);
         byte[][]currMap = (CultimaPanel.map.get(mi));   
         int row = p.getRow();
         int col = p.getCol();
         if(row < 0 || col < 0 || row >= currMap.length ||col >= currMap[0].length)
            continue;
         for(int r=row - 1; r<=row+1; r++)
         {
            for(int c=col - 1; c<=col + 1; c++)
            {
               if(r==row && c==col)
                  continue;
               int checkRow = r;
               int checkCol = c;   
               if(mi != 0)
               {
                  if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
                     continue;   
               }
               else
               {
                  checkRow = CultimaPanel.equalizeWorldRow(r);
                  checkCol = CultimaPanel.equalizeWorldCol(c);
               }
            
               if(LocationBuilder.isCombustable(currMap, checkRow, checkCol))
               {  //if there is no NPC there, add the point into firesToAdd
                  boolean NPCthere = NPCAt(checkRow, checkCol, mi);
                  double fireSpreadProb = 0.05;
                  boolean fireRoll = (rollDie(5) >= CultimaPanel.weather && Math.random()<fireSpreadProb);
                  if(!NPCthere && fireRoll)
                     firesToAdd.add(new Coord(checkRow, checkCol));
               }
            }
         }
      }
      //firesToAdd added to worldMonsters
      for(Coord p:firesToAdd)
      {
         CultimaPanel.worldMonsters.add(new NPCPlayer(NPC.FIRE, p.getRow(), p.getCol(), 0, locType));
      }
   }

   //possibly spawn fire if lava is next to a combustable
   public static void spawnLavaFire()
   {
      if(CultimaPanel.player.getMapIndex() != 0)
         return;
      ArrayList<Coord> locs = new ArrayList<Coord>();
      Terrain lava = TerrainBuilder.getTerrainWithName("TER_I_L_3_$Lava");
      byte[][]currMap = CultimaPanel.map.get(0);
      for(int r=CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2; r++)
      {
         for(int c=CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            int row = r;
            int col = c;
            row = CultimaPanel.equalizeWorldRow(r);
            col = CultimaPanel.equalizeWorldCol(c);
            if(Math.abs(currMap[row][col]) == lava.getValue())
               locs.add(new Coord(row, col));    
         }
      }
      if(locs.size() > 0)
         spreadFiresFromLava(locs);
   
   }
   
   //pick a random spot that can be traveled on within range units of (r, c) in the world (mapIndex 0)
   public static Coord pickRandomSpot(int startRow, int startCol, int range)
   {
      if(CultimaPanel.player.getMapIndex() != 0)
         return null;
      ArrayList<Coord> locs = new ArrayList<Coord>();
      byte[][]currMap = CultimaPanel.map.get(0);
      for(int r=startRow-range/2; r<=startRow+range/2; r++)
      {
         for(int c=startCol-range/2; c<=startCol+range/2; c++)
         {
            int row = r;
            int col = c;
            row = CultimaPanel.equalizeWorldRow(r);
            col = CultimaPanel.equalizeWorldCol(c);
            if(LocationBuilder.canSpawnOn(currMap, row, col))
               locs.add(new Coord(row, col));    
         }
      }
      if(locs.size() > 0)
         return getRandomFrom(locs);
      return null;   
   }
   
   //send vector in the direction of dir until we find an impassable spot.  
   //Continue until we find a passable one, and teleport the player to that location
   //if there is no spot before reaching the map limits, spell fails
   public static void advance(byte dir)
   {
      int maxRange = CultimaPanel.SCREEN_SIZE;
      int row = CultimaPanel.player.getRow();
      int col = CultimaPanel.player.getCol();
      int mi = CultimaPanel.player.getMapIndex();
      byte[][]currMap = CultimaPanel.map.get(mi);
      int impassRow = -1;   //position of the first impassable we come across
      int impassCol = -1;
      if(dir==LocationBuilder.NORTH)
      {
         int r=row-1;
         //find the first impassable place
         if(mi==0)
            r = CultimaPanel.equalizeWorldRow(r);
         else if(r < 0)
            return;
         while(!LocationBuilder.isImpassable(currMap, r, col) && !LocationBuilder.isBreakable(currMap, r, col))
         {
            r--;
            if(mi==0)
               r = CultimaPanel.equalizeWorldRow(r);
            else if(r < 0)
               return;
            if(Math.abs(row - r) >= maxRange)
               return;  
         }
           //find the first passable place
         while((LocationBuilder.isImpassable(currMap, r, col) || LocationBuilder.isBreakable(currMap, r, col)) && !NPCAt(r, col, mi))
         {
            r--;
            if(mi==0)
               r = CultimaPanel.equalizeWorldRow(r);
            else if(r < 0)
               return;
            if(Math.abs(row - r) >= maxRange)
               return;  
         }
         CultimaPanel.player.setRow(r);
         return;                 
      }
      else if(dir==LocationBuilder.SOUTH)
      {
         int r=row+1;
         //find the first impassable place
         if(mi==0)
            r = CultimaPanel.equalizeWorldRow(r);
         else if(r >= currMap.length)
            return;
         while(!LocationBuilder.isImpassable(currMap, r, col) && !LocationBuilder.isBreakable(currMap, r, col))
         {
            r++;
            if(mi==0)
               r = CultimaPanel.equalizeWorldRow(r);
            else if(r >= currMap.length)
               return;
            if(Math.abs(row - r) >= maxRange)
               return;  
         }
           //find the first passable place
         while((LocationBuilder.isImpassable(currMap, r, col) || LocationBuilder.isBreakable(currMap, r, col)) && !NPCAt(r, col, mi))
         {
            r++;
            if(mi==0)
               r = CultimaPanel.equalizeWorldRow(r);
            else if(r >= currMap.length)
               return;
            if(Math.abs(row - r) >= maxRange)
               return;  
         }
         CultimaPanel.player.setRow(r);
         return;                 
      }
      else if(dir==LocationBuilder.WEST)
      {
         int c=col-1;
         //find the first impassable place
         if(mi==0)
            c = CultimaPanel.equalizeWorldCol(c);
         else if(c < 0)
            return;
         while(!LocationBuilder.isImpassable(currMap, row, c) && !LocationBuilder.isBreakable(currMap, row, c))
         {
            c--;
            if(mi==0)
               c = CultimaPanel.equalizeWorldCol(c);
            else if(c < 0)
               return;
            if(Math.abs(col - c) >= maxRange)
               return;  
         }
           //find the first passable place
         while((LocationBuilder.isImpassable(currMap, row, c) || LocationBuilder.isBreakable(currMap, row, c)) && !NPCAt(row, c, mi))
         {
            c--;
            if(mi==0)
               c = CultimaPanel.equalizeWorldCol(c);
            else if(c < 0)
               return;
            if(Math.abs(col - c) >= maxRange)
               return;  
         }
         CultimaPanel.player.setCol(c);
         return;                 
      }
      else if(dir==LocationBuilder.EAST)
      {
         int c=col+1;
         //find the first impassable place
         if(mi==0)
            c = CultimaPanel.equalizeWorldCol(c);
         else if(c >= currMap[0].length)
            return;
         while(!LocationBuilder.isImpassable(currMap, row, c) && !LocationBuilder.isBreakable(currMap, row, c))
         {
            c++;
            if(mi==0)
               c = CultimaPanel.equalizeWorldCol(c);
            else if(c >= currMap[0].length)
               return;
            if(Math.abs(col - c) >= maxRange)
               return;  
         }
           //find the first passable place
         while((LocationBuilder.isImpassable(currMap, row, c) || LocationBuilder.isBreakable(currMap, row, c)) && !NPCAt(row, c, mi))
         {
            c++;
            if(mi==0)
               c = CultimaPanel.equalizeWorldCol(c);
            else if(c >= currMap[0].length)
               return;
            if(Math.abs(col - c) >= maxRange)
               return;  
         }
         CultimaPanel.player.setCol(c);
         return;                 
      }
   }

   //returns true if c is between a and b
   public static boolean isBetween(int a, int b, int c)
   {
      return ((c > a && c < b) || (c > b && c < a));
   }

   //if we hit with a target with max manna, do an area of effect damage on other NPCs that are close
   //returns true if we hit multiple targets
   public static boolean areaOfEffectAttack(NPCPlayer target, int range, Weapon weapType)
   {
      int numHits = 0;
      int mi = CultimaPanel.player.getMapIndex();
      int baseDamage = rollDie(weapType.getMinDamage(), weapType.getMaxDamage());
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(i);
         if(p.getMapIndex() != mi || p.getBody() <= 0 || p.equals(target))
         {
            continue;  
         }
         double dist = Display.distanceSimple(target.getRow(), target.getCol(), p.getRow(), p.getCol());
         if(dist > range)
         { 
            continue;
         } 
         double distPercent = (range - dist) / range;
         if(range == 1 && dist == 1)
         {
            distPercent = 1;
         }
         int damageDone = (int)(distPercent * baseDamage);
         if(damageDone > 0)
         {
            p.damage(damageDone, weapType.getEffect());
            numHits++;
         }   
      }
      if(mi == 0)
      {
         for(int i=CultimaPanel.boats.size()-1; i>=0; i--)
         {
            NPCPlayer p = CultimaPanel.boats.get(i);
            if(p.getMapIndex() != mi || p.getBody() <= 0 || p.equals(target))
            {
               continue;  
            }
            double dist = Display.distanceSimple(target.getRow(), target.getCol(), p.getRow(), p.getCol());
            if(dist > range)
            { 
               continue;
            } 
            double distPercent = (range - dist) / range;
            if(range == 1 && dist == 1)
            {
               distPercent = 1;
            }
            int damageDone = (int)(distPercent * baseDamage);
            if(damageDone > 0)
            {
               p.damage(damageDone, weapType.getEffect());
               numHits++;
            }   
         }
      }
      ArrayList<NPCPlayer> civsInLoc = (CultimaPanel.civilians.get(mi));
      for(int i=civsInLoc.size()-1; i>=0; i--)
      {
         NPCPlayer p = civsInLoc.get(i);
         if(p.getMapIndex() != mi || p.getBody() <= 0 || p.equals(target))
            continue;
         double dist = Display.distanceSimple(target.getRow(), target.getCol(), p.getRow(), p.getCol());
         if(dist > range)
         { 
            continue;
         } 
         double distPercent = (range - dist) / range;
         if(range == 1 && dist == 1)
         {
            distPercent = 1;
         }
         int damageDone = (int)(distPercent * baseDamage);
         if(damageDone > 0)
         {
            p.damage(damageDone, weapType.getEffect());
            numHits++;
         } 
      }
      //maybe make fires or smoke
      String weapName = weapType.getName().toLowerCase();
      if(weapName.contains("fire") || weapName.contains("lightning"))
      {
         ArrayList<Coord> locs = new ArrayList<Coord>();
         String locType = CultimaPanel.player.getLocationType();
         byte[][]currMap = CultimaPanel.map.get(mi);
         for(int r=target.getRow()-(range/2); r<=target.getRow()+(range/2); r++)
         {
            for(int c=target.getCol()-(range/2); c<=target.getCol()+(range/2); c++)
            {
               int row = r;
               int col = c;
               if(CultimaPanel.player.getMapIndex() == 0)
               {
                  row = CultimaPanel.equalizeWorldRow(r);
                  col = CultimaPanel.equalizeWorldCol(c);
                  String current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
                  if(!NPCAt(row, col, mi) && !LocationBuilder.isImpassable(currMap, row, col) && !current.contains("water") && !current.contains("bog"))
                     locs.add(new Coord(row, col));    
               }
               else //if(CultimaPanel.player.getMapIndex() != 0)
               {
                  if(row>=0 && row<currMap.length && col>=0 && col<currMap[0].length )
                  {
                     String current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
                     if(!NPCAt(row, col, mi) && !LocationBuilder.isImpassable(currMap, row, col) && !current.contains("water") && !current.contains("bog"))
                        locs.add(new Coord(row, col));
                  }
               } 
            }
         }
         for(int i=0; i< locs.size(); i++)
         {
            Coord pick = locs.get(i);
            int r = pick.getRow();
            int c = pick.getCol();
            double dist = Display.distanceSimple(target.getRow(), target.getCol(), r, c);
            double distPercent = (range - dist) / range;
            if(Math.random() < distPercent/2)
            {
               NPCPlayer flame = new NPCPlayer(NPC.FIRE, r, c, mi, locType);
               int flameHealth = rollDie(flame.getBody()/10, flame.getBody()/6);
               if(weapType.getEffect().equals("fire"))
               {
                  flameHealth = rollDie(flame.getBody()/6, flame.getBody()/4);
               }
               flame.setBody(flameHealth);
               CultimaPanel.worldMonsters.add(flame);
            }
         }
      }
      return (numHits >= 1);
   }
   
    //if we hit a target with a wide strike, also strike any npc that is adjacent to it
   //returns true if we hit multiple targets
   public static boolean wideSwipe(NPCPlayer target, int range, Weapon weapType)
   {
      int numHits = 0;
      int mi = CultimaPanel.player.getMapIndex();
      int baseDamage = rollDie(weapType.getMinDamage(), weapType.getMaxDamage());
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(i);
         if(p.getMapIndex() != mi || p.getBody() <= 0 || p.equals(target))
         {
            continue;  
         }
         double dist = Display.distanceSimple(target.getRow(), target.getCol(), p.getRow(), p.getCol());
         if(dist > range)
         { 
            continue;
         } 
         if((p.getRow()==target.getRow() && Math.abs(p.getCol() - target.getCol()) == 1) || (p.getCol()==target.getCol() && Math.abs(p.getRow() - target.getRow()) == 1))
         {  //p is next to target, so do damage to them as well
            p.damage(baseDamage, weapType.getEffect());
            numHits++;
         } 
      }
   
      ArrayList<NPCPlayer> civsInLoc = (CultimaPanel.civilians.get(mi));
      for(int i=civsInLoc.size()-1; i>=0; i--)
      {
         NPCPlayer p = civsInLoc.get(i);
         if(p.getMapIndex() != mi || p.getBody() <= 0 || p.equals(target))
            continue;
         double dist = Display.distanceSimple(target.getRow(), target.getCol(), p.getRow(), p.getCol());
         if(dist > range)
         { 
            continue;
         } 
         if((p.getRow()==target.getRow() && Math.abs(p.getCol() - target.getCol()) == 1) || (p.getCol()==target.getCol() && Math.abs(p.getRow() - target.getRow()) == 1))
         {  //p is next to target, so do damage to them as well
            p.damage(baseDamage, weapType.getEffect());
            numHits++;
         }  
      }
      //maybe make fires or smoke
      String weapName = weapType.getName().toLowerCase();
      if(weapName.contains("fire") || weapName.contains("lightning"))
      {
         ArrayList<Coord> locs = new ArrayList<Coord>();
         String locType = CultimaPanel.player.getLocationType();
         byte[][]currMap = CultimaPanel.map.get(mi);
         for(int r=target.getRow()-(range/2); r<=target.getRow()+(range/2); r++)
         {
            for(int c=target.getCol()-(range/2); c<=target.getCol()+(range/2); c++)
            {
               int row = r;
               int col = c;
               if(CultimaPanel.player.getMapIndex() == 0)
               {
                  row = CultimaPanel.equalizeWorldRow(r);
                  col = CultimaPanel.equalizeWorldCol(c);
                  if(!NPCAt(row, col, mi) && !LocationBuilder.isImpassable(currMap, row, col))
                     locs.add(new Coord(row, col));    
               }
               else //if(CultimaPanel.player.getMapIndex() != 0)
               {
                  if(row>=0 && row<currMap.length && col>=0 && col<currMap[0].length && !NPCAt(row, col, mi) && !LocationBuilder.isImpassable(currMap, row, col))
                     locs.add(new Coord(row, col));
               } 
            }
         }
         for(int i=0; i< locs.size(); i++)
         {
            Coord pick = locs.get(i);
            int r = pick.getRow();
            int c = pick.getCol();
            double dist = Display.distanceSimple(target.getRow(), target.getCol(), r, c);
            double distPercent = (range - dist) / range;
            if(Math.random() < distPercent/2)
            {
               NPCPlayer flame = new NPCPlayer(NPC.FIRE, r, c, mi, locType);
               int flameHealth = rollDie(flame.getBody()/10, flame.getBody()/6);
               if(weapType.getEffect().equals("fire"))
               {
                  flameHealth = rollDie(flame.getBody()/6, flame.getBody()/4);
               }
               flame.setBody(flameHealth);
               CultimaPanel.worldMonsters.add(flame);
            }
         }
      }
      return (numHits >= 1);
   }

//see if there is a player between monolith attacker and another, and attack if so
   public static void monolithAttack(NPCPlayer attacker)
   {
      int mi = CultimaPanel.player.getMapIndex();
      byte[][]currMap = CultimaPanel.map.get(mi);
      Coord monoCol = null;    //will store coordinates of a twin monolith that is in the same row or col as attacker
      Coord monoRow = null;
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(i);
         if(p.getRow()==attacker.getRow() && p.getCol()==attacker.getCol())
            continue;
         if(p.getMapIndex() != mi || p.getBody() <= 0)
            continue;  
         if(p.getCharIndex()==NPC.MONOLITH && (p.getRow()==attacker.getRow() || p.getCol()==attacker.getCol()))
         {     //another monolith in the same row or col
            if(p.getRow()==attacker.getRow())
            {
               boolean blockedBetween = false;
               for(int c=Math.min(p.getCol(),attacker.getCol()); c<=Math.max(p.getCol(),attacker.getCol()); c++)
               {
                  if(LocationBuilder.outOfBounds(currMap, p.getRow(), c))
                     continue;
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][c])).getName();
                  if(terr.contains("_B_"))   //blocks view
                  {
                     blockedBetween = true;
                     break;
                  }
                  NPCPlayer temp = getNPCAt(p.getRow(), c, mi);
                  if(temp!=null && temp.isStatue() && temp.getCharIndex()!=NPC.MONOLITH)
                  {
                     blockedBetween = true;
                     break;
                  }
               }
               if(!blockedBetween)
                  monoRow = new Coord(p.getRow(), p.getCol());
            }
            else if(p.getCol()==attacker.getCol())
            {
               boolean blockedBetween = false;
               for(int r=Math.min(p.getRow(),attacker.getRow()); r<=Math.max(p.getRow(),attacker.getRow()); r++)
               {
                  if(LocationBuilder.outOfBounds(currMap, r, p.getCol()))
                     continue;
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][p.getCol()])).getName();
                  if(terr.contains("_B_"))   //blocks view
                  {
                     blockedBetween = true;
                     break;
                  }
                  NPCPlayer temp = getNPCAt(r, p.getCol(), mi);
                  if(temp!=null && temp.isStatue() && temp.getCharIndex()!=NPC.MONOLITH)
                  {
                     blockedBetween = true;
                     break;
                  }
               }
               if(!blockedBetween)
                  monoCol = new Coord(p.getRow(), p.getCol());
            }
         }
      }
      ArrayList<NPCPlayer> civsInLoc = (CultimaPanel.civilians.get(mi));
      for(int i=civsInLoc.size()-1; i>=0; i--)
      {
         NPCPlayer p = civsInLoc.get(i);
         if(p.getMapIndex() != mi || p.getBody() <= 0)
            continue;
         if(p.getRow()==attacker.getRow() && p.getCol()==attacker.getCol())
            continue;
         if(p.getCharIndex()==NPC.MONOLITH && (p.getRow()==attacker.getRow() || p.getCol()==attacker.getCol()))
         {     //another monolith in the same row or col
            if(p.getRow()==attacker.getRow())
            {
               boolean blockedBetween = false;
               for(int c=Math.min(p.getCol(),attacker.getCol()); c<=Math.max(p.getCol(),attacker.getCol()); c++)
               {
                  if(LocationBuilder.outOfBounds(currMap, p.getRow(), c))
                     continue;
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][c])).getName();
                  if(terr.contains("_B_"))   //blocks view
                  {
                     blockedBetween = true;
                     break;
                  }
                  NPCPlayer temp = getNPCAt(p.getRow(), c, mi);
                  if(temp!=null && temp.isStatue() && temp.getCharIndex()!=NPC.MONOLITH)
                  {
                     blockedBetween = true;
                     break;
                  }
               }
               if(!blockedBetween)
                  monoRow = new Coord(p.getRow(), p.getCol());
            }
            else if(p.getCol()==attacker.getCol())
            {
               boolean blockedBetween = false;
               for(int r=Math.min(p.getRow(),attacker.getRow()); r<=Math.max(p.getRow(),attacker.getRow()); r++)
               {
                  if(LocationBuilder.outOfBounds(currMap, r, p.getCol()))
                     continue;
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][p.getCol()])).getName();
                  if(terr.contains("_B_"))   //blocks view
                  {
                     blockedBetween = true;
                     break;
                  }
                  NPCPlayer temp = getNPCAt(r, p.getCol(), mi);
                  if(temp!=null && temp.isStatue() && temp.getCharIndex()!=NPC.MONOLITH)
                  {
                     blockedBetween = true;
                     break;
                  }
               }
               if(!blockedBetween)
                  monoCol = new Coord(p.getRow(), p.getCol());
            }
         }
      }
   
      //scan for player or NPCs inbetween the monoliths and attack
      int playRow = CultimaPanel.player.getRow();
      int playCol = CultimaPanel.player.getCol();
      int attRow = attacker.getRow();
      int attCol = attacker.getCol();
      double weaponRange = attacker.getWeapon().getRange();
      boolean flight = (CultimaPanel.player.getActiveSpell("Flight")!=null);
      if(CultimaPanel.player.getBody() > 0 && !flight)
      {
         if(monoRow != null && playRow == attRow && isBetween(attCol, monoRow.getCol(), playCol))
         {
            double dist = Display.findDistance(attRow, attCol, playRow, playCol);
            if(dist <= weaponRange)
               attacker.attack();
         }
         if(monoCol != null && playCol == attCol && isBetween(attRow, monoCol.getRow(), playRow))
         {
            double dist = Display.findDistance(attRow, attCol, playRow, playCol);
            if(dist <= weaponRange)
               attacker.attack();
         }
      }
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(i);
         if(p.getRow()==attRow && p.getCol()==attCol)
            continue;
         if(p.getBody() <= 0 || p.getMapIndex()!=mi)
            continue;
         if(p.getCharIndex()==NPC.MONOLITH || p.getCharIndex()==NPC.DEATH || p.getCharIndex()==NPC.DEMONKING || NPC.isNatural(p.getCharIndex()) || NPC.isCivilian(p.getCharIndex()) || p.isStatue() || p.canFly() || p.getName().equalsIgnoreCase("Keeper of the Bridge") || p.getName().equalsIgnoreCase("Black Knight"))
            continue;   
         if(monoRow != null && p.getRow() == attRow && isBetween(attCol, monoRow.getCol(), p.getCol()))
         {
            double dist = Display.findDistance(attRow, attCol, p.getRow(), p.getCol());
            if(dist <= weaponRange)
               attacker.attack(p);
         }
         if(monoCol != null && p.getCol() == attCol && isBetween(attRow, monoCol.getRow(), p.getRow()))
         {
            double dist = Display.findDistance(attRow, attCol, p.getRow(), p.getCol());
            if(dist <= weaponRange)
               attacker.attack(p);
         }
      }
   }
   
   public static boolean jawTrapAt(int row, int col, int mi)
   {
      if(CultimaPanel.corpses.size() <= mi || row < 0 || col < 0)
         return false;
      for(int j = CultimaPanel.corpses.get(mi).size()-1; j>=0; j--)
      {
         Corpse cp = CultimaPanel.corpses.get(mi).get(j);
         if(cp.getRow()==row && cp.getCol()==col && cp.getCharIndex()==NPC.JAWTRAP)
         {
            return true;
         }
      }
      return false;
   }
   
   public static Corpse getCorpseAt(int row, int col, int mi)
   {
      if(CultimaPanel.corpses.size() <= mi || row < 0 || col < 0)
         return null;
      for(int j = CultimaPanel.corpses.get(mi).size()-1; j>=0; j--)
      {
         Corpse cp = CultimaPanel.corpses.get(mi).get(j);
         if(cp.getRow()==row && cp.getCol()==col)
         {
            return cp;
         }
      }
      return null;
   }
   
   //cast raise-water spell
   public static void raiseWater(int row, int col, int mi)
   {
      byte [][] currMap = (CultimaPanel.map.get(mi));
      for(int r=row - 2; r<=row+2; r++)
      {
         for(int c=col - 2; c<=col + 2; c++)
         {
            double dist = Display.distanceSimple(r, c, row, col);
            if(dist > 2)  //make likelyhood of skipping depend on distance
               continue;
            int checkRow = r;
            int checkCol = c;   
            if(mi != 0)
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
            if(currTerr.contains("bog"))
               currMap[checkRow][checkCol]=TerrainBuilder.getTerrainWithName("TER_V_$Shallow_water").getValue(); 
            else if(currTerr.contains("grass") || currTerr.contains("sand") || currTerr.contains("dead"))
               currMap[checkRow][checkCol]=TerrainBuilder.getTerrainWithName("TER_S_$Bog").getValue();  
         }
      }  
   }
   
   //simple left-right movement for planes on the battlefield in the 1942 realm
   public static void planeMove(NPCPlayer p, byte[][]house)
   {
      if(p.getRow() < 0 || p.getCol() < 0 || p.getRow() >= house.length || p.getCol() >= house[0].length)
      {  //plane moves off the screen, so have it reappear somewhere else
         int row = (int)(Math.random()*house.length);
         int col = 0;
         byte marchDir = NPC.MARCH_EAST;
         if(Math.random() < 0.5)    //start on left side
         {
            col = 0;
            marchDir = NPC.MARCH_EAST;
         }
         else                       //start on right side
         {
            col = house[0].length-1;
            marchDir = NPC.MARCH_EAST;
         }
         p.setMoveType(marchDir);
         p.setRow(row);
         p.setCol(col);
         p.setNumInfo(rollDie(0,2));
      }
      double weaponRange = p.getWeapon().getRange();
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer target = CultimaPanel.worldMonsters.get(i);
         if(p.getRow()==target.getRow() && Display.findDistance(p.getRow(), p.getCol(), target.getRow(), target.getCol()) <= weaponRange)
         {
            p.attack(target);
         }
      }
      if(p.getNumInfo() > 0 && ((p.getMoveType()==NPC.MARCH_EAST && p.getCol() >= 5) || (p.getMoveType()==NPC.MARCH_WEST && p.getCol() < house[0].length-5)))
      {
         if(Math.random() < 0.05)
         {
            int col = p.getCol() - 3;
            if(p.getMoveType()==NPC.MARCH_WEST)
            {
               col = p.getCol() + 3;
            }
            explosion(house, p.getRow(), col, p.getMapIndex(), p.getLocationType());
            p.setNumInfo(p.getNumInfo()-1);
         }
      }
      if(p.getMoveType()==NPC.MARCH_EAST)
      {
         p.moveEast();
      }
      else if(p.getMoveType()==NPC.MARCH_WEST)
      {
         p.moveWest();
      }
   }
   
//NPC Monsters will more according to move type STILL, ROAM, MARCH, CHASE or RUN and their agility compared to Player
//maybe make extra move for monsters with a higher agility if keyHit is true
   public static void moveMonsters(boolean keyHit)
   {
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      String thisLocName = thisLoc.getName().toLowerCase();
      boolean inCamp = (locType.contains("camp") || thisLocName.endsWith(" camp"));
      
      boolean flight = false;
      if(CultimaPanel.player.getActiveSpell("Flight")!=null)
         flight = true;
      NPCPlayer dog = getDog();
   
      boolean searchMessage = false;         //used to trigger no more than 1 search message per call to the method   
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(i);
         boolean pIsOurNpc = (p.isEscortedNpc());
         boolean pIsOurDog = (p.isOurDog());
         int mi =  CultimaPanel.player.getMapIndex();  
         byte[][]currMap = (CultimaPanel.map.get(mi));   
         if(p.getMapIndex() != mi)
            continue;
         if(p.getCharIndex()==NPC.SPITFIRE)
         {
            planeMove(p, currMap);
            continue;
         }   
         if(p.getRow() < 0 || p.getCol() < 0 || p.getRow() >= currMap.length || p.getCol() >= currMap[0].length)
         {
            continue;
         }  
         if(p.getCharIndex()==NPC.MONOLITH)
         {
            monolithAttack(p);
            continue;
         }
         if(p.isStatue() || p.getCharIndex()==NPC.BARREL || p.getCharIndex()==NPC.BOAT) 
         {     
            continue; 
         }
         if(p.isNonPlayer() || (p.getCharIndex()==NPC.WILLOWISP && p.getLocationType().contains("pacrealm")))
         {                    //WILLOWISP in pacrealm are pellets
            continue;
         }
         if(p.canSwim() && Display.frozenWater())
         {
            continue;
         }
         if(removeStuckNPCs(p,  i, CultimaPanel.worldMonsters))
         {
            continue;
         }       
      //ship that has been boarded by player should not move on its own   
         if(NPC.isShip(p) && p.hasMet())
         {
            continue;
         }
         if(p.getCharIndex()==NPC.TORNADO && p.getNumInfo()!=0 && Math.random() < 0.5)  //air elemental - slow it down
         {
            continue;
         }   
         inCamp = inCamp || p.getLocationType().contains("camp"); 
         if(p.getBody() <= 0)
            continue;  
         String playerSpot = CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.player.getRow()][CultimaPanel.player.getCol()])).getName().toLowerCase();
         String pSpot = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][p.getCol()])).getName().toLowerCase();
         if(pSpot.contains("poison") && !Display.isWinter() && !p.hasEffect("poison") && !p.canFly() && Math.random() < 0.1)
         {  //if p is standing in a poison bog, chance they will be poisoned
            p.addEffect("poison");
         }
         if(p.hasEffect("still") || p.hasEffect("web"))
            continue;   
         if(p.hasEffect("freeze") && Math.random() < 0.75)
            continue;  
         if(p.getCharIndex()==NPC.SLIME && Math.random() < 0.5)   //slimes need to move slower than slow
            continue;  
         double dist = Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
         double size = NPC.getSize(p);
         if(size == 1.5)   //make it so range 1 weapons in the hand of large beasts has a range of 2
            size = 2;
         double weaponRange = Math.max(p.getWeapon().getRange(), size);
         String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][p.getCol()])).getName().toLowerCase();
         Location currLoc = CultimaPanel.allLocations.get(mi);
         if(mi > 0 && p.getMoveType()==NPC.RUN && (p.getRow()<=0 || p.getCol()<=0 || p.getRow()>=currMap.length-1 || p.getCol()>=currMap[0].length-1)) 
         {  //fleeing monsters that leave the map are removed
            if(currLoc!=null)
            {
               currLoc.removeMonster(p.getCharIndex());
            }
             //remove any current missions with players in this location since they are gone
            String NPCmiss = p.getMissionInfo();
            if(!NPCmiss.equals("none"))
            {
               for(int missIndex=0; missIndex<CultimaPanel.missionStack.size(); missIndex++)
               {
                  Mission m = CultimaPanel.missionStack.get(i);
                  if(NPCmiss.equals(m.getInfo()))
                  {
                     CultimaPanel.missionStack.remove(missIndex);
                     FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
                     break;
                  }
               }
            }
            CultimaPanel.worldMonsters.remove(i);
            continue;
         }
         int oldRow = p.getRow();      //record the monster's position before they move
         int oldCol = p.getCol();      //if p is a FIRE elemental, we can leave a fire trail behind it at (oldRow, oldCol)
      
         //see if the monster steps into a trap
         if(checkTraps(p))
         {
            continue;
         }   
         if(specialMove(p, i, CultimaPanel.worldMonsters))
         {  //see if npc uses this turn for a special move like summoning or a special attack
            continue;
         }
         if(p.canSwim() && !curr.contains("water"))
         { 
            if(p.getCharIndex()==NPC.WHIRLPOOL)
            {
               CultimaPanel.player.addExperience(p.getLevel());
               CultimaPanel.worldMonsters.remove(i);
               continue;
            }
            else //if(NPC.isShip(p))
            {
               CultimaPanel.player.addExperience(p.getLevel());
               Corpse dead = p.getCorpse();
               CultimaPanel.corpses.get(mi).add(dead);
               Utilities.removeNPCat(p.getRow(), p.getCol(), mi);
            }
         }
         if(curr.contains("water") && (p.getCharIndex()==NPC.GRABOID || p.getCharIndex()==NPC.DEMON || p.getCharIndex()==NPC.DEMONKING || p.getCharIndex()==NPC.MAGMA || p.getCharIndex()==NPC.FIRE))
         {
            CultimaPanel.player.addExperience(p.getLevel());
            Corpse dead = p.getCorpse();
            CultimaPanel.corpses.get(mi).add(dead);
            Utilities.removeNPCat(p.getRow(), p.getCol(), mi);
         }
      
         if(mi == 0)
         {
            if(dist > CultimaPanel.SCREEN_SIZE*3)     //remove monsters that are far away
            {
               if((NPC.isShip(p) && p.hasMet()) || p.isEscortedNpc() || p.isOurDog() || p.isNonPlayer())
               {}
               if(i >= 0 && i<CultimaPanel.worldMonsters.size())  
               {                                      //don't remove ships we have commendeered
                  CultimaPanel.worldMonsters.remove(i);
               }
               continue;
            }
         }
         if(CultimaPanel.weather > 1 && p.getCharIndex()==NPC.GRABOID)
         {                                            //graboids go back underground when it starts raining
            CultimaPanel.worldMonsters.remove(i);
            continue;
         } 
         if(p.getCharIndex()!=NPC.FIRE)
         {
            if(nocturnalCheck(p, i, CultimaPanel.worldMonsters))
            {
               continue;
            }
            if(p.getMoveType()==NPC.STILL && !p.hasBeenAttacked() && p.getCharIndex()!=NPC.TREE  && p.getCharIndex()!=NPC.TREEKING && p.getCharIndex()!=NPC.CAERBANNOG  && p.getCharIndex()!=NPC.MIMIC && !p.hasEffect("control"))
               continue;
         }
         if(p.getMoveType()==NPC.RUN && dist > (CultimaPanel.SCREEN_SIZE/2+1))
            continue;   
         if(p.getMoveType()==NPC.ROAM || (p.getMoveType()>=NPC.MARCH_NORTH  && p.getMoveType()<=NPC.MARCH_WEST))
         {
            if(CultimaPanel.numFrames % (CultimaPanel.animDelay*3) != 0)  
               continue; 
         }
         if(keyHit)     //make speed of NPC chasing or running depend on agility difference
         {
            String playerTerrain = CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.player.getRow()][CultimaPanel.player.getCol()])).getName().toLowerCase();
            if(!p.canFly() && !p.canSwim() && curr.contains("_v_") && !playerTerrain.contains("_v_")) 
               continue;   //no extra movement if the npc is on very slow terrain and the player is not
            if(!p.canFly() && !p.canSwim() && curr.contains("_s_") && !playerTerrain.contains("_v_") && !playerTerrain.contains("_s_"))
               continue;   //no extra movement if the npc is on slow terrain and the player is not
            else
            {
               int agilityDiff = CultimaPanel.player.getAgility() - p.getAgility();
               if(p.hasEffect("control") || (p==dog && Math.random()<0.5) || (agilityDiff < 0 && (rollDie(100) < Math.abs(agilityDiff)*2) && (p.getMoveType()==NPC.RUN || p.getMoveType()==NPC.CHASE || p.getMoveType()==NPC.ATTACK)))
               {           //for every 5 points of agility that the monster is greater, add a 10% higher chance that they will get an extra move when player does       
               
               }
               else
                  if(NPC.isShip(p) && (CultimaPanel.numFrames % (CultimaPanel.animDelay*3) != 0))  
                     continue;
                  else if(CultimaPanel.numFrames % (CultimaPanel.animDelay*3/2) != 0)  
                     continue;
            }
         }  
         else if(p.getMoveType()==NPC.RUN || p.getMoveType()==NPC.CHASE || p.getMoveType()==NPC.ATTACK)
         {
            if(NPC.isShip(p) && (CultimaPanel.numFrames % (CultimaPanel.animDelay*3) != 0))  
               continue;
            else if(CultimaPanel.numFrames % (CultimaPanel.animDelay*3/2) != 0)  
               continue; 
         }
         
         affectTerrain(p);       //big monsters might change the terrain by walking on it
      
         searchMessage = setMovementType(p, searchMessage, false);
      
         NPCPlayer target = closestEnemyNPCTarget(p); 
      
         if(inCamp)
         {
            if(p.getCharIndex()==NPC.DOG && p.hasMet())
            {  //just get the dog to follow us
               target = null;
            }
         }   
      
         boolean pIsFreedCreature = false;    
         if(target != null && inCamp && NPC.isImprisonedMonster(p))
         {
            pIsFreedCreature = true;
         }     
      
         if(p.getMoveType()==NPC.CHASE && !p.isOurDog() && !p.isEscortedNpc())         //see if there is a human target in range to attack
         {  
            if(target != null && target!=p)
            {
               if(Display.findDistance(p.getRow(), p.getCol(), target.getRow(), target.getCol()) <= weaponRange)
               {
                  if(locType.contains("battlefield"))
                  {  //attack if on the other side
                     p.attack(target);
                     continue;
                  }
                  else if((p.hasEffect("control") && !target.hasEffect("control") && !target.hasMet()) || (!p.hasEffect("control") && target.hasEffect("control") ) || NPC.isGiantMonsterKing(p) ||
                          (NPC.isCivilian(target.getCharIndex()) && !p.getLocationType().equals("ship") && !p.getLocationType().equals("arena")))
                  {                                               //make sure bowassassins and enforcers don't attack their own shipmates
                     p.attack(target);
                     continue;
                  }
               }
            }
         }
      
         if( p.isOurDog() && p.getMoveType()==NPC.ATTACK)
         {  //dog attacks closest target when given attack command
            if(target!=null)
            {
               if(Display.findDistance(p.getRow(), p.getCol(), target.getRow(), target.getCol()) <= weaponRange)
               {
                  if(isGameForDog(target))   //only attack game - otherwise, dog is overpowered - just distract others
                     p.attack(target);
                  continue;
               }
            }
            else
            {
               p.setMoveType(NPC.CHASE);
            }
         }
         if(p.getCharIndex()==NPC.GOBLINBARREL && !p.hasBeenAttacked() && Math.random() < 0.9)
            continue;   //make goblins in a barrel move less before they are attacked
         if(p.getCharIndex()==NPC.GOBLIN && (p.getItems().size()>0 || p.getGold() > 0))
            p.setMoveType(NPC.RUN); //if a goblin has stolen something, make them run away
         boolean isChasingWindElemental = (p.getCharIndex()==NPC.TORNADO && p.hasItem("essence") && p.getNumInfo()!=0);
         boolean randomMove = (p.getCharIndex()==NPC.TORNADO && !isChasingWindElemental) || (isChasingWindElemental && Math.random()<.1);
         if(p.getMoveType()==NPC.MARCH_NORTH && p.getRow()-1>=0)
         {
            int r = p.getRow();
            int c = p.getCol();
            String up = CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase();
            boolean badUp = (up.contains("water") || up.contains("door") || (up.contains("_d_") && (up.contains("wall") || up.contains("rock"))));
            if((CultimaPanel.player.getRow()==r-1 && CultimaPanel.player.getCol()==c))
            {}    //don't move into the postition where the player or other NPCs are
            else if(p.getRow() <= 2 || !LocationBuilder.canMoveTo(p, currMap, r-1, c) || badUp)
               p.setMoveType(NPC.MARCH_SOUTH);
            else
               p.moveNorth();
         }
         else if(p.getMoveType()==NPC.MARCH_SOUTH && p.getRow()+1<currMap.length)
         {
            int r = p.getRow();
            int c = p.getCol();
            String down = CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase();
            boolean badDown = (down.contains("water") || down.contains("door") || (down.contains("_d_") && (down.contains("wall") || down.contains("rock"))));
            if((CultimaPanel.player.getRow()==r+1 && CultimaPanel.player.getCol()==c))
            {}    //don't move into the postition where the player or other NPCs are
            else if(p.getRow() >= currMap.length - 2 || !LocationBuilder.canMoveTo(p, currMap, r+1, c) || badDown)
               p.setMoveType(NPC.MARCH_NORTH);
            else
               p.moveSouth();
         } 
         else if(p.getMoveType()==NPC.MARCH_EAST && p.getCol()+1<currMap[0].length)
         {
            int r = p.getRow();
            int c = p.getCol();
            String right = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase();
            boolean badRight = (right.contains("water") || right.contains("door") || (right.contains("_d_") && (right.contains("wall") || right.contains("rock"))));
            if((CultimaPanel.player.getRow()==r && CultimaPanel.player.getCol()==c+1))
            {}    //don't move into the postition where the player or other NPCs are
            else if(p.getCol() >= currMap[0].length - 2 || !LocationBuilder.canMoveTo(p, currMap, r, c+1) || badRight)
               p.setMoveType(NPC.MARCH_WEST);
            else
               p.moveWest();
         }
         else if(p.getMoveType()==NPC.MARCH_WEST && p.getCol()-1>=0)
         {
            int r = p.getRow();
            int c = p.getCol();
            String left = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase();
            boolean badLeft = (left.contains("water") || left.contains("door") || (left.contains("_d_") && (left.contains("wall") || left.contains("rock"))));
            if((CultimaPanel.player.getRow()==r && CultimaPanel.player.getCol()==c-1))
            {}    //don't move into the postition where the player or other NPCs are
            else if(p.getCol() <= 2 || !LocationBuilder.canMoveTo(p, currMap, r, c-1) || badLeft)
               p.setMoveType(NPC.MARCH_EAST);
            else
               p.moveEast();
         }
         else if(!isChasingWindElemental && (randomMove || (NPC.isNatural(p.getCharIndex()) && p.getNumInfo()==0) || p.getMoveType()==NPC.ROAM || ((p.getCharIndex()==NPC.TREE || p.getCharIndex()==NPC.TREEKING || p.getCharIndex()==NPC.SLIME) && !p.hasBeenAttacked()) || p.hasEffect("control")))
         {
            if(NPC.isNatural(p.getCharIndex()) || p.getCharIndex()==NPC.TREE  || p.getCharIndex()==NPC.TREEKING || p.getCharIndex()==NPC.SLIME)
            {
               if(p.canAttackPlayer())
               {
                  if(p.getCharIndex()==NPC.FIRE && CultimaPanel.player.getArmor().getName().toLowerCase().contains("holocaust-cloak"))
                  {}    //holocaust cloak protects from fire
                  else
                  {
                     if((p.getCharIndex()==NPC.TREE  || p.getCharIndex()==NPC.TREEKING || p.getCharIndex()==NPC.SLIME) && (CultimaPanel.numFrames % (CultimaPanel.animDelay*4) != 0) )
                     {}
                     else
                     {
                        p.attack();
                        continue;
                     }
                  }
               }
            }
            if(p.hasEffect("control") || NPC.isNatural(p.getCharIndex()) || p.getCharIndex()==NPC.GRABOID || ((p.getCharIndex()==NPC.TREE || p.getCharIndex()==NPC.TREEKING || p.getCharIndex()==NPC.SLIME) && !p.hasBeenAttacked()))
            {
               if(target != null && target!=p && Display.findDistance(p.getRow(), p.getCol(), target.getRow(), target.getCol()) <= weaponRange)
               {
                  if(NPC.isNatural(p.getCharIndex()) ||  (!target.hasEffect("control") && !target.getName().equalsIgnoreCase("Keeper of the Bridge") && !target.getName().equalsIgnoreCase("Black Knight")))
                  {
                     if(p.getCharIndex()==NPC.SLIME && target.getCharIndex()==NPC.SLIME)
                     {}    //slimes don't attack each other
                     else if((p.getCharIndex()==NPC.TREE  || p.getCharIndex()==NPC.TREEKING || p.getCharIndex()==NPC.SLIME) && (CultimaPanel.numFrames % (CultimaPanel.animDelay*2) != 0) )
                     {}
                     else
                     {
                        p.attack(target);
                        continue; 
                     }
                  }
                  if(NPC.isNatural(p.getCharIndex()) ||  p.getCharIndex()==NPC.GRABOID)
                  {}
                  else
                     continue;
               }              
            }
            if(((p.getCharIndex()==NPC.FIRE) && p.getNumInfo()==0) || p.getMoveType()==NPC.STILL)
               continue;   
            
            else if(randomMove || ((Math.random() < 0.5 || p.hasEffect("control") || ((p.getCharIndex()==NPC.TORNADO && p.getNumInfo()==0) || p.getCharIndex()==NPC.WHIRLPOOL))))
            {
               int dir = (int)(Math.random()*4);
               if(p.hasEffect("control") && p.getCharIndex()!=NPC.TORNADO && p.getCharIndex()!=NPC.WHIRLPOOL)
               { 
                  if (target!=null && !target.hasEffect("control"))
                     dir = TerrainBuilder.getDirectionTowardsSimple(p.getRow(), p.getCol(), target.getRow(), target.getCol());
                  else
                     dir = TerrainBuilder.getDirectionTowardsSimple(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
               } 
               if((p.getCharIndex()==NPC.TORNADO && p.getNumInfo()==0) || (p.canSwim() && curr.contains("rapid")))      
               {     //tornado more likely to follow the wind, ships follow currents
                  double sameDir = 0.5;
                  double sideDir = 0.9;
                  if(CultimaPanel.windDir == LocationBuilder.NORTH)
                  {
                     double roll = Math.random();
                     if(roll < sameDir)
                        dir = LocationBuilder.NORTH;
                     else if(roll < sideDir)
                     {
                        if(Math.random() < 0.5)
                           dir = LocationBuilder.WEST;
                        else
                           dir = LocationBuilder.EAST;
                     }  
                     else
                        dir = LocationBuilder.SOUTH;
                  }
                  else if(CultimaPanel.windDir == LocationBuilder.SOUTH)
                  {
                     double roll = Math.random();
                     if(roll < sameDir)
                        dir = LocationBuilder.SOUTH;
                     else if(roll < sideDir)
                     {
                        if(Math.random() < 0.5)
                           dir = LocationBuilder.WEST;
                        else
                           dir = LocationBuilder.EAST;
                     }  
                     else
                        dir = LocationBuilder.NORTH;
                  }  
                  else if(CultimaPanel.windDir == LocationBuilder.EAST)
                  {
                     double roll = Math.random();
                     if(roll < sameDir)
                        dir = LocationBuilder.EAST;
                     else if(roll < sideDir)
                     {
                        if(Math.random() < 0.5)
                           dir = LocationBuilder.NORTH;
                        else
                           dir = LocationBuilder.SOUTH;
                     }  
                     else
                        dir = LocationBuilder.WEST;
                  } 
                  else if(CultimaPanel.windDir == LocationBuilder.WEST)
                  {
                     double roll = Math.random();
                     if(roll < sameDir)
                        dir = LocationBuilder.WEST;
                     else if(roll < sideDir)
                     {
                        if(Math.random() < 0.5)
                           dir = LocationBuilder.NORTH;
                        else
                           dir = LocationBuilder.SOUTH;
                     }  
                     else
                        dir = LocationBuilder.EAST;
                  }
               }
               if(dir==LocationBuilder.NORTH && p.getRow()-1>=0)
               {
                  if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                     continue;      //skip turn for fighting the winds
                  if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                     continue;      //skip turn for fighting the currents
                  int r = p.getRow();
                  int c = p.getCol();
                  if(LocationBuilder.canMoveTo(p, currMap, r-1, c) || (p.getCharIndex() == NPC.TORNADO && r-1 >= 0))
                  {
                     p.moveNorth();
                     continue;
                  }
               }
               else if(dir==LocationBuilder.SOUTH && p.getRow()+1<currMap.length)
               {
                  if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                     continue;      //skip turn for fighting the winds
                  if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                     continue;      //skip turn for fighting the currents
                  int r = p.getRow();
                  int c = p.getCol();
                  if(LocationBuilder.canMoveTo(p, currMap, r+1, c) || (p.getCharIndex() == NPC.TORNADO && r+1 < currMap.length))
                  {
                     p.moveSouth();
                     continue;
                  }   
               } 
               else if(dir==LocationBuilder.EAST && p.getCol()+1<currMap[0].length)
               {
                  if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                     continue;      //skip turn for fighting the winds
                  if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                     continue;      //skip turn for fighting the currents
                  int r = p.getRow();
                  int c = p.getCol();
                  if(LocationBuilder.canMoveTo(p, currMap, r, c+1) || (p.getCharIndex() == NPC.TORNADO && c+1 < currMap[0].length))
                  {
                     p.moveEast();
                     continue;
                  }                 
               }
               else if(dir==LocationBuilder.WEST && p.getCol()-1>=0)
               {
                  if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                     continue;      //skip turn for fighting the winds
                  if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                     continue;      //skip turn for fighting the currents
                  int r = p.getRow();
                  int c = p.getCol();
                  if(LocationBuilder.canMoveTo(p, currMap, r, c-1) || (p.getCharIndex() == NPC.TORNADO && c-1 >= 0))
                  {
                     p.moveWest();               
                     continue;
                  }
               }
            }
         }
         else if(p.getMoveType()==NPC.CHASE || p.getMoveType()==NPC.RUN || p.getMoveType()==NPC.ATTACK)
         {
            if(p.isOurDog())
            {
            //we already have dog attack code above
            }
            else
            {
               if(CultimaPanel.player.getActiveSpell("Charm")!=null && p.getReputation() >= (-100 - (CultimaPanel.player.getMind()*2)) && !p.hasBeenAttacked())
               {  //break of oppositional behavior if the player has the charm spell activated
                  p.setMoveTypeTemp(NPC.ROAM);
                  continue;
               }
               int fogDist = 5;  //the distance that a pursuer will lose us in the fog
               if(mi == 0)
                  fogDist = 3;   //shorter distance when in the world map
               if((NPC.isHumanoid(p.getCharIndex()) && (p.getMoveType()==NPC.CHASE || p.getMoveType()==NPC.RUN) && CultimaPanel.fogAlpha > 100 && dist >= fogDist) && !p.hasBeenAttacked())
               {  //people chasing us in the fog loose us if they are farther away
                  p.setMoveTypeTemp(NPC.ROAM);
                  continue;
               }
               if((!(NPC.isTameableCanine(p.getCharIndex()) && !p.hasBeenAttacked() && p.hasMet())) && !p.isOurDog() && !p.isEscortedNpc() && (p.getMoveType()==NPC.CHASE || (p.getMoveType()==NPC.RUN && p.isBoxedIn()) || NPC.isAssassin(p.getCharIndex())))     //attack if in range
               {
                  if(p.canAttackPlayer())
                  {
                     if(CultimaPanel.numFrames >= p.getAttackTime())
                     {
                        if(locType.contains("battlefield"))
                        {
                        //attack only if on the other side
                           if((!NPC.isCivilian(p.getCharIndex()) && p.getCharIndex()!=NPC.BOWASSASSIN && p.getCharIndex()!=NPC.ENFORCER) || p.hasBeenAttacked())
                           {
                              p.attack();
                              int extraTime = 100-p.getAgility();     //extra time added to attack "reload" with lower agility
                              if(extraTime < 0)
                                 extraTime = 0;
                              p.setAttackTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3) + extraTime);
                              continue;
                           }
                        }
                        else if(CultimaPanel.player.getBody() > 0 && !p.hasEffect("control"))
                        {  //only monsters tall enough or with a long range weapon can strike player when they are flying
                           if(flight && !p.canFly() && NPC.getSize(p.getCharIndex()) < 2 && weaponRange <= 2) 
                              continue;
                           //a ship can not fire its cannons if it only has one sailor on board at the helm   
                           if(NPC.isShip(p) && p.getBody()<=100)
                              continue; 
                           if(inCamp && p.isWerewolf() && p.getCharIndex()!=NPC.WEREWOLF)
                              continue;   
                           p.attack();
                           int extraTime = 100-p.getAgility();     //extra time added to attack "reload" with lower agility
                           if(extraTime < 0)
                              extraTime = 0;
                           p.setAttackTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3) + extraTime);
                           continue;
                        }
                     } 
                     else
                     {  //keep p in the pocket to attack depending on their agility
                        int moveRoll = rollDie(p.getAgility()+CultimaPanel.player.getAgility());
                        if(!NPC.isJumpy(p) && (p.getAgility() < moveRoll || Math.random() < 0.5))
                           continue;
                     }
                  }
               }
            }
            if(CultimaPanel.player.getBody() <= 0 && p.getCharIndex()!=NPC.TREE && p.getCharIndex()!=NPC.TREEKING)
            {
               p.setMoveType(NPC.ROAM);
               continue;
            }
            if(p.isEscortedNpc() && p.getMoveType()==NPC.CHASE && dist <3)
               continue;
            else if(p.isOurDog() && p.getMoveType()==NPC.CHASE && dist <2)
               continue;
         
            String up="", down="", left="", right="";
            int r = p.getRow();
            int c = p.getCol();
            int playerR = CultimaPanel.player.getRow();
            int playerC = CultimaPanel.player.getCol();
         
            int dir = TerrainBuilder.getDirectionTowards(r, c, playerR, playerC);
            
            if(p.getMoveType() == NPC.CHASE && !NPC.isPsychic(p) && CultimaPanel.player.isHiding() && dist >= 3)
            {  //if we are hiding, make it so that the creature seeking us essentially is put to roam depending on how agilities compare
               if(CultimaPanel.numFrames % 2 == 0)
               {  //slow p down if they are searching for a hidden player
                  continue;
               }
               double hideValue = 0.8;
               if(CultimaPanel.isNight)
               {  //more effective hiding at night
                  hideValue += 0.1;
               }
               if(Display.isWinter())
               {  //less effective hiding in winter
                  hideValue -= 0.2;
               }
               if(CultimaPanel.fogAlpha > 0)
               {  //more effective in fog
                  hideValue += CultimaPanel.fogAlpha/1000.0;
               }
               if(CultimaPanel.player.hasBrightWeapon())
               {  //if we have a bright or flaming weapon out, we are not hiding very well
                  hideValue = 0.1;
               }
               if((p.getCharIndex()==NPC.FIRE || p.getCharIndex()==NPC.TORNADO) && p.getNumInfo()!=0)
               {
                  hideValue = 1.0;
               }
               if(p.canSwim() && playerSpot.contains("water"))
               {  //swimmers can see you if you are hiding in water
                  hideValue = 0; 
               }
               double agilDiff = Math.min(hideValue, (CultimaPanel.player.getAgility() / (Math.max(1, p.getAgility()))));
               if(Math.random() < agilDiff)
               {
                  dir = TerrainBuilder.getRandomDirection(); 
               }
            }
            if(locType.contains("battlefield") && target!=null)
            {
               if(!NPC.isCivilian(p.getCharIndex()) && p.getCharIndex()!=NPC.BOWASSASSIN && p.getCharIndex()!=NPC.ENFORCER)    //monster combatant on battlefield
               {  //chase and attack whoever is closer: player or closest enemy NPC
                  double distToTarg = Display.findDistance(p.getRow(), p.getCol(), target.getRow(), target.getCol());
                  double distToPlayer = Display.findDistance(p.getRow(), p.getCol(), playerR, playerC);
                  if(distToTarg < distToPlayer)
                     dir = TerrainBuilder.getDirectionTowards(r, c, target.getRow(), target.getCol());
               }
               else
                  dir = TerrainBuilder.getDirectionTowards(r, c, target.getRow(), target.getCol());
            }
            if(p.isOurDog() && p.getMoveType()==NPC.ATTACK)
            {
               if(target!=null)
                  dir = TerrainBuilder.getDirectionTowards(r, c, target.getRow(), target.getCol());
               else
               {
                  p.setMoveType(NPC.ROAM);
                  continue;
               }
            }
            if(!NPC.isTameableCanine(p.getCharIndex()) && distToDog(p) <=3 && Math.random() < 0.2 && dog!=null)
            {
               if(locType.contains("battlefield") && !areEnemies(p, dog))
               {}
               else
               {
                  dir = TerrainBuilder.getDirectionTowards(r, c, dog.getRow(), dog.getCol());
                  if(Display.findDistance(p.getRow(), p.getCol(), dog.getRow(), dog.getCol()) <= weaponRange && Math.random() < 0.25)
                  {
                     p.attack(dog);
                     continue;
                  }
               }
            }
            boolean moveDown = (dir==TerrainBuilder.SOUTH);          //(c==playerC && r<playerR);
            boolean moveUp = (dir==TerrainBuilder.NORTH);            //(c==playerC && r>playerR);
            boolean moveLeft = (dir==TerrainBuilder.WEST);           //(r==playerR && c>playerC);
            boolean moveRight = (dir==TerrainBuilder.EAST);          //(r==playerR && c<playerC);
            boolean moveDownLeft = (dir==TerrainBuilder.SOUTHWEST);  //(r<playerR && c>playerC);
            boolean moveDownRight = (dir==TerrainBuilder.SOUTHEAST); //(r<playerR && c<playerC);
            boolean moveUpLeft = (dir==TerrainBuilder.NORTHWEST);    //(r>playerR && c>playerC);
            boolean moveUpRight = (dir==TerrainBuilder.NORTHEAST);   //(r>playerR && c<playerC);
         
            if(CultimaPanel.player.getMapIndex()==0)
            {
               up = CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(r-1)][c])).getName().toLowerCase();
               down = CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(r+1)][c])).getName().toLowerCase();
               left = CultimaPanel.allTerrain.get(Math.abs(currMap[r][CultimaPanel.equalizeWorldCol(c-1)])).getName().toLowerCase();
               right = CultimaPanel.allTerrain.get(Math.abs(currMap[r][CultimaPanel.equalizeWorldCol(c+1)])).getName().toLowerCase();
            }
            else
            {
               if(r-1 < 0)
                  up = "out";
               else
                  up = CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase();
               if(r+1 >= currMap.length)
                  down = "out";
               else   
                  down = CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase();
               if(c-1 < 0)
                  left = "out";
               else
                  left = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase();
               if(c+1 >= currMap[0].length)
                  right = "out";
               else
                  right = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase();
            }
            boolean badLeft = !LocationBuilder.canMoveTo(p, currMap, r, c-1); 
            boolean badRight = !LocationBuilder.canMoveTo(p, currMap, r, c+1);
            boolean badUp = !LocationBuilder.canMoveTo(p, currMap, r-1, c); 
            boolean badDown = !LocationBuilder.canMoveTo(p, currMap, r+1, c); 
         
            if(p.getMoveType()==NPC.RUN || (p.isOurDog() && p.getMoveType()==NPC.ATTACK && target!=null && !isGameForDog(target) && Display.findDistance(p.getRow(), p.getCol(), target.getRow(), target.getCol()) <= 2))
            {
               moveDown = (dir==TerrainBuilder.NORTH);            //(c==playerC && r>playerR);
               moveUp = (dir==TerrainBuilder.SOUTH);              //(c==playerC && r<playerR);
               moveLeft = (dir==TerrainBuilder.EAST);             //(r==playerR && c<playerC);
               moveRight = (dir==TerrainBuilder.WEST);            //(r==playerR && c>playerC);
               moveDownLeft = (dir==TerrainBuilder.NORTHEAST);    //(r>playerR && c<playerC);
               moveDownRight = (dir==TerrainBuilder.NORTHWEST);   //(r>playerR && c>playerC);
               moveUpLeft = (dir==TerrainBuilder.SOUTHEAST);      //(r<playerR && c<playerC);
               moveUpRight = (dir==TerrainBuilder.SOUTHWEST);     //(r<playerR && c>playerC);
            }           
            if(moveDown)
            {
               if(!LocationBuilder.canMoveTo(p, currMap, r+1, c))
               {     //blocked down, so try left or right
                  if(LocationBuilder.canMoveTo(p, currMap, r, c-1) && LocationBuilder.canMoveTo(p, currMap, r, c+1))
                  {  //left and right are both clear, so pick randomly
                     if(Math.random() < 0.5)
                     {
                        if((playerR==r && playerC==c-1) || badLeft)   
                        {}
                        else
                        {
                           if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                              continue;      //skip turn for fighting the winds
                           if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                              continue;      //skip turn for fighting the currents
                           p.moveWest();
                        }
                     }
                     else
                     {
                        if((playerR==r && playerC==c+1) || badRight)   
                        {}
                        else
                        {
                           if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                              continue;      //skip turn for fighting the winds
                           if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                              continue;      //skip turn for fighting the currents
                           p.moveEast();  
                        }
                     }
                  }
                  else if((LocationBuilder.canMoveTo(p, currMap, r, c-1)))
                  {
                     if((playerR==r && playerC==c-1) || badLeft)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveWest();
                     }
                  }
                  else if((LocationBuilder.canMoveTo(p, currMap, r, c+1)))
                  {
                     if((playerR==r && playerC==c+1) || badRight)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveEast();
                     }
                  }
               }
               else
               {
                  if((playerR==r+1 && playerC==c) || badDown)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveSouth();
                  }                
               }
            }
            else if(moveUp)
            {
               if((!LocationBuilder.canMoveTo(p, currMap, r-1, c)))
               {     //blocked down, so try left or right
                  if(LocationBuilder.canMoveTo(p, currMap, r, c-1) && LocationBuilder.canMoveTo(p, currMap, r, c+1))
                  {  //left and right are both clear, so pick randomly
                     if(Math.random() < 0.5)
                     {
                        if((playerR==r && playerC==c-1) || badLeft)   
                        {}
                        else
                        {
                           if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                              continue;      //skip turn for fighting the winds
                           if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                              continue;      //skip turn for fighting the currents
                           p.moveWest();
                        }
                     }
                     else
                     {
                        if((playerR==r && playerC==c+1) || badRight)   
                        {}
                        else
                        {
                           if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                              continue;      //skip turn for fighting the winds
                           if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                              continue;      //skip turn for fighting the currents
                           p.moveEast();
                        }
                     }
                  }
                  else if((LocationBuilder.canMoveTo(p, currMap, r, c-1)))
                  {
                     if((playerR==r && playerC==c-1) || badLeft)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveWest();
                     }
                  }
                  else if((LocationBuilder.canMoveTo(p,currMap, r, c+1)))
                  {
                     if((playerR==r && playerC==c+1) || badRight)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveEast();
                     }
                  }
               }
               else
               {
                  if((playerR==r-1 && playerC==c) || badUp)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveNorth();
                  }
               }
            }
            else if(moveLeft)
            {
               if((!LocationBuilder.canMoveTo(p, currMap, r, c-1)))
               {     //blocked left, so try up or down
                  if(LocationBuilder.canMoveTo(p, currMap, r-1, c) && LocationBuilder.canMoveTo(p, currMap, r+1, c))
                  {  //up and down are both clear, so pick randomly
                     if(Math.random() < 0.5)
                     {
                        if((playerR==r-1 && playerC==c) || badUp)   
                        {}
                        else
                        {
                           if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                              continue;      //skip turn for fighting the winds
                           if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                              continue;      //skip turn for fighting the currents
                           p.moveNorth();
                        }
                     }
                     else
                     {
                        if((playerR==r+1 && playerC==c) || badDown)   
                        {}
                        else
                        {
                           if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                              continue;      //skip turn for fighting the winds
                           if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                              continue;      //skip turn for fighting the currents
                           p.moveSouth();
                        }
                     }
                  }
                  else if((LocationBuilder.canMoveTo(p, currMap, r-1, c)))
                  {
                     if((playerR==r-1 && playerC==c) || badUp)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveNorth();
                     }
                  }
                  else if((LocationBuilder.canMoveTo(p, currMap, r+1, c)))
                  {
                     if((playerR==r+1 && playerC==c) || badDown)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveSouth();
                     }
                  }
               }
               else
               {
                  if((playerR==r && playerC==c-1) || badLeft)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveWest();
                  }
               }
            }
            else if(moveRight)
            {
               if((!LocationBuilder.canMoveTo(p, currMap, r, c+1)))
               {     //blocked left, so try up or down
                  if(LocationBuilder.canMoveTo(p, currMap, r-1, c) && LocationBuilder.canMoveTo(p,currMap, r+1, c))
                  {  //up and down are both clear, so pick randomly
                     if(Math.random() < 0.5)
                     {
                        if((playerR==r-1 && playerC==c) || badUp)   
                        {}
                        else
                        {
                           if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                              continue;      //skip turn for fighting the winds
                           if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                              continue;      //skip turn for fighting the currents
                           p.moveNorth();
                        }
                     }
                     else
                     {
                        if((playerR==r+1 && playerC==c) || badDown)   
                        {}
                        else
                        {
                           if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                              continue;      //skip turn for fighting the winds
                           if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                              continue;      //skip turn for fighting the currents
                           p.moveSouth();
                        }
                     }
                  }
                  else if((LocationBuilder.canMoveTo(p,currMap, r-1, c)))
                  {
                     if((playerR==r-1 && playerC==c) || badUp)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveNorth();
                     }
                  }
                  else if((LocationBuilder.canMoveTo(p, currMap, r+1, c)))
                  {
                     if((playerR==r+1 && playerC==c) || badDown)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveSouth();
                     }
                  }
               }
               else
               {
                  if((playerR==r && playerC==c+1) || badRight)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveEast();
                  }
               }
            }
            else if(moveDownLeft)
            {
               if(LocationBuilder.canMoveTo(p,currMap, r+1, c) && LocationBuilder.canMoveTo(p,currMap, r, c-1))
               {  //clear down and left, so pick randomly
                  if(Math.random() < 0.5)
                  {
                     if((playerR==r+1 && playerC==c) || badDown)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveSouth();
                     }
                  }
                  else
                  {
                     if((playerR==r && playerC==c-1) || badLeft)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveWest();
                     }
                  }
               }
               else if(LocationBuilder.canMoveTo(p,currMap, r+1, c))
               {  //clear down
                  if((playerR==r+1 && playerC==c) || badDown)  
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveSouth();
                  }
               }
               else if((LocationBuilder.canMoveTo(p,currMap, r, c-1)))
               {  //clear left
                  if((playerR==r && playerC==c-1) || badLeft)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveWest();
                  }
               }
            }
            else if(moveDownRight)
            {
               if(LocationBuilder.canMoveTo(p,currMap, r+1, c) && LocationBuilder.canMoveTo(p,currMap, r, c+1))
               {  //clear down and left, so pick randomly
                  if(Math.random() < 0.5)
                  {
                     if((playerR==r+1 && playerC==c) || badDown)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveSouth();
                     }
                  }
                  else
                  {
                     if((playerR==r && playerC==c+1) || badRight)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveEast();
                     }
                  }
               }
               else  if((LocationBuilder.canMoveTo(p,currMap, r+1, c)))
               {  //clear down
                  if((playerR==r+1 && playerC==c) || badDown)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.NORTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveSouth();
                  }
               }
               else if((LocationBuilder.canMoveTo(p,currMap, r, c+1)))
               {  //clear left
                  if((playerR==r && playerC==c+1) || badRight)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveEast();
                  }
               }
            }
            else if(moveUpLeft)
            {
               if(LocationBuilder.canMoveTo(p,currMap, r-1, c) && !LocationBuilder.canMoveTo(p,currMap, r, c-1))
               {  //clear up and left, so pick randomly
                  if(Math.random() < 0.5)
                  {
                     if((playerR==r-1 && playerC==c) || badUp)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveNorth();
                     }
                  }
                  else
                  {
                     if((playerR==r && playerC==c-1) || badLeft)  
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveWest();
                     }
                  }
               }
               else  if((LocationBuilder.canMoveTo(p,currMap, r-1, c)))
               {  //clear up
                  if((playerR==r-1 && playerC==c) || badUp)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveNorth();
                  }
               }
               else if((LocationBuilder.canMoveTo(p,currMap, r, c-1)))
               {  //clear left
                  if((playerR==r && playerC==c-1) || badLeft)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.EAST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveWest();
                  }
               }
            }
            else if(moveUpRight)
            {
               if(LocationBuilder.canMoveTo(p,currMap, r-1, c) && !LocationBuilder.canMoveTo(p,currMap, r, c+1))
               {  //clear up and left, so pick randomly
                  if(Math.random() < 0.5)
                  {
                     if((playerR==r-1 && playerC==c) || badUp)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveNorth();
                     }
                  }
                  else
                  {
                     if((playerR==r && playerC==c+1) || badRight)   
                     {}
                     else
                     {
                        if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the winds
                        if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                           continue;      //skip turn for fighting the currents
                        p.moveEast();
                     }
                  }
               }
               else  if((LocationBuilder.canMoveTo(p,currMap, r-1, c)))
               {  //clear up
                  if((playerR==r-1 && playerC==c) || badUp)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.SOUTH && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveNorth();
                  }
               }
               else if((LocationBuilder.canMoveTo(p,currMap, r, c+1)))
               {  //clear left
                  if((playerR==r && playerC==c+1) || badRight)   
                  {}
                  else
                  {
                     if(NPC.isShip(p) && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the winds
                     if(p.canSwim() && curr.contains("rapid") && CultimaPanel.windDir == LocationBuilder.WEST && Math.random() < 0.5)
                        continue;      //skip turn for fighting the currents
                     p.moveEast();
                  }
               }
            }
            Coord oldSpot = new Coord(oldRow, oldCol);
            Coord newSpot = new Coord(p.getRow(), p.getCol());
            if(p.getCharIndex()==NPC.FIRE && p.getNumInfo()!=0 && !oldSpot.equals(newSpot) && Math.random() < 0.5 && !NPCAt(oldRow, oldCol, p.getMapIndex()))
            {   //we want fire elementals to leave behind a trail of fire
               NPCPlayer thisFire = new NPCPlayer(NPC.FIRE, oldRow, oldCol, p.getMapIndex(), p.getLocationType());
               thisFire.setBody(p.getBody()/2);
               CultimaPanel.worldMonsters.add(thisFire);
            }
         }
      }
   }

   public static void setupArena(int mapIndex)
   {
      byte[][]currMap = (CultimaPanel.map.get(mapIndex)); 
      boolean open = false;  
      if(CultimaPanel.time >= 6 && CultimaPanel.time <= 20 && CultimaPanel.weather==0)
         open = true;
      for(int r=0; r<currMap.length; r++)
      {
         for(int c=0; c<currMap[0].length; c++)
         {
            String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(curr.contains("night_door"))
            {
               if(open)
                  currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_$Night_door_floor").getValue();
               else
                  currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_I_B_$Night_door_closed").getValue();
            }
         }
      }
   }

   public static void putNPCsToBed(int mapIndex)
   {
      if(CultimaPanel.NPCsInBed)
         return;
      CultimaPanel.NPCsInBed = true;
      CultimaPanel.NPCsOnStreet = false;
      boolean holiday = ((CultimaPanel.days % 100) == 50);
    //close doors to shops
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));   
      ArrayList<Coord>spawns = new ArrayList<Coord>();
      ArrayList<Coord>groundSpawns = new ArrayList<Coord>();
      boolean hasCoffin = false;    //to see if vampire should spawn
      for(int r=0; r<currMap.length; r++)
      {
         for(int c=0; c<currMap[0].length; c++)
         {
            String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(curr.contains("night_door"))
            {
               currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_I_B_$Night_door_closed").getValue();
               moveGuard(r, c, mapIndex);   
            }
            if(LocationBuilder.canSpawnOn(currMap, r, c) || TerrainBuilder.isCampableSpot(r, c, mapIndex))
               spawns.add(new Coord(r, c));
            if(TerrainBuilder.isCampableSpot(r, c, mapIndex))
               groundSpawns.add(new Coord(r, c));
            if(curr.contains("coffin"))
               hasCoffin = true;
         }
      }
      boolean thereIsWerewolf = false;
   
      for(NPCPlayer p: CultimaPanel.civilians.get(mapIndex))
      {
         if(NPC.isGuard(p.getCharIndex()) || !NPC.isCivilian(p))
            continue;
         if(p.isShopkeep() || p.isTrapped() || p.getName().toLowerCase().contains("masterthief"))  //shoppekeeps and statues stay in place
            continue;
         if(CultimaPanel.weather == 0 && p.isWerewolf() && (!p.isOurSpouse() || Math.random()<0.5)) //werewolf tag (but not our spouse)
         {                             //maybe keep spouse werewolf at home to suprise us
            if(groundSpawns.size() > 0)
            {                          //put werewolf on street
               Coord spawn = getRandomFrom(groundSpawns);
               int r = spawn.getRow();
               int c = spawn.getCol();
               p.setRow(r);
               p.setCol(c);
               thereIsWerewolf = true;
               continue;
            }
         }   
         int hRow=p.getHomeRow();
         int hCol=p.getHomeCol();
         if(hRow==CultimaPanel.player.getRow() && hCol==CultimaPanel.player.getCol() && p.getMapIndex()==CultimaPanel.player.getMapIndex())
         {}
         else
         {   
            p.restoreLoc();
            p.setMoveType(NPC.STILL);
         }
         if(p.swinePlayer())
            p.setIsSwinePlayer(false);
         if(p.hasItem("loaded-cube"))
            p.removeItem("loaded-cube");
      }
      
      boolean thereIsAssassin = false;     
      int numSides = 100;
      if(thereIsWerewolf)
         numSides += 25;
      if(rollDie(numSides) < CultimaPanel.player.getBounty() && !holiday)      //put the She-assassin on the street
      {
         int r = (int)(Math.random()*currMap.length);
         int c = (int)(Math.random()*currMap[0].length);
         if(spawns.size() > 0)
         {
            Coord spawn = getRandomFrom(spawns);
            r = spawn.getRow();
            c = spawn.getCol();
         }
         NPCPlayer sheAssassin1 = new NPCPlayer(NPC.BOWASSASSIN, r, c, mapIndex, "city");
         CultimaPanel.worldMonsters.add(sheAssassin1);
         numSides += 25;
         thereIsAssassin = true; 
      }
      if(rollDie(numSides) < CultimaPanel.player.getBounty() && !holiday)      //put the Viper-assassin on the street
      {
         int r = (int)(Math.random()*currMap.length);
         int c = (int)(Math.random()*currMap[0].length);
         if(spawns.size() > 0)
         {
            Coord spawn = getRandomFrom(spawns);
            r = spawn.getRow();
            c = spawn.getCol();
         }
         NPCPlayer sheAssassin2 = new NPCPlayer(NPC.VIPERASSASSIN, r, c, mapIndex, "city");
         CultimaPanel.worldMonsters.add(sheAssassin2);
         numSides += 25;
         thereIsAssassin = true; 
      }
      if(rollDie(numSides) < CultimaPanel.player.getBounty() && !holiday)      //put the Dark-enforcer on the street
      {
         int r = (int)(Math.random()*currMap.length);
         int c = (int)(Math.random()*currMap[0].length);
         if(spawns.size() > 0)
         {
            Coord spawn = getRandomFrom(spawns);
            r = spawn.getRow();
            c = spawn.getCol();
         }
         NPCPlayer sheAssassin3 = new NPCPlayer(NPC.ENFORCER, r, c, mapIndex, "city");
         CultimaPanel.worldMonsters.add(sheAssassin3);
         thereIsAssassin = true; 
      }
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      if(locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("port"))
      {
         double vampProb = 0.015;
         if(thereIsWerewolf)
            vampProb -= 0.005;
         if(thereIsAssassin)   
            vampProb -= 0.005;
         if(Math.random() < vampProb && !holiday)      //put the vampire on the street
         {
            int r = (int)(Math.random()*currMap.length);
            int c = (int)(Math.random()*currMap[0].length);
            if(spawns.size() > 0)
            {
               Coord spawn = getRandomFrom(spawns);
               r = spawn.getRow();
               c = spawn.getCol();
            }
            byte vampType = NPC.MALEVAMP;
            if(Math.random() < 0.5)
               vampType = NPC.FEMALEVAMP;
            NPCPlayer vamp = new NPCPlayer(vampType, r, c, mapIndex, "city");
            CultimaPanel.worldMonsters.add(vamp);
         } 
      }  
   }

//returns true if the location is a capital city
   public static boolean isCapitalCity(Location currentLoc)
   {
      if(currentLoc == null)
         return false;
      boolean capitalCity = false;
      String [] parts = currentLoc.getName().split(" ");
      if(parts.length == 2)
      {
         String prefix = parts[0];
         String suffix = parts[1];
         for(String pre: Utilities.capitalPrefix)
         {
            if(pre.equals(prefix))
               capitalCity = true;
         }
      }
      return capitalCity;
   }
   
   //returns true if the location at mapIndex is a capital city
   public static boolean isCapitalCity(int mapIndex)
   {
      Location currentLoc = CultimaPanel.allLocations.get(mapIndex);
      if(currentLoc == null)
         return false;
      boolean capitalCity = false;
      String [] parts = currentLoc.getName().split(" ");
      if(parts.length == 2)
      {
         String prefix = parts[0];
         String suffix = parts[1];
         for(String pre: Utilities.capitalPrefix)
         {
            if(pre.equals(prefix))
               capitalCity = true;;
         }
      }
      return capitalCity;
   }
 
   //returns the closest Capital City to row r, col c, null if there is no capital
   public static Location getClosestCapital(int r, int c)
   {
      double minDist = Double.MAX_VALUE;
      Location closest = null;
      for(Location loc:CultimaPanel.allDomiciles)
         if(isCapitalCity(loc))
         {
            double dist = Display.wrapDistance(r, c, loc.getRow(), loc.getCol());
            if(dist < minDist)
            {
               minDist = dist;
               closest = loc;
            }
         }
      if(closest!=null)
         return closest;
      return null;
   }  
   
    //returns the location of Caerbannog
   public static Location getCaerbannog()
   {
      for(Location loc:CultimaPanel.allAdventure)
      {
         String locName = loc.getName().toLowerCase();
         if(locName.contains("caerbannog"))
            return loc;
      }
      return null;
   } 
	
//place commonors out on the sreet roaming in cities (not castles)
   public static void putNPCsOnStreet(int mapIndex)
   {
      if(CultimaPanel.NPCsOnStreet)
         return;
      
      CultimaPanel.NPCsOnStreet = true;
      CultimaPanel.NPCsInBed = false;
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));  
      ArrayList<Coord>spawns = new ArrayList<Coord>();
      ArrayList<Coord>monsterSpawns = new ArrayList<Coord>();
      ArrayList<Coord>capitalSpawns = new ArrayList<Coord>();
      Location currentLoc = CultimaPanel.allLocations.get(mapIndex);
      boolean capitalCity = isCapitalCity(currentLoc);
      byte [] cityMonsters = {NPC.TORNADO, NPC.BUGBEAR, NPC.CYCLOPS, NPC.GIANT, NPC.ORC, NPC.TROLL, NPC.WOLFKING, NPC.DEMONKING, NPC.SKELETON, 
                              NPC.SNAKEKING, NPC.RATKING, NPC.SPIDERKING, NPC.BUGBEARKING, NPC.TROLLKING, NPC.DRAGON, NPC.DRAGONKING, 
                              NPC.SEAMONSTER, NPC.SHARK, NPC.SQUID, NPC.GREATSHIP, NPC.BRIGANDSHIP};
      int countMonster = 0;
      int countFire = 0;
      int countDog = 0;
      int countChicken = 0;
      int countPig = 0;
      int countFish = 0;
      int countShark = 0;
      for(NPCPlayer p:CultimaPanel.worldMonsters)
      {
         if(p.getMapIndex() != mapIndex)
            continue;
         if(search(cityMonsters,p.getCharIndex()))
            countMonster++;
         if(p.getCharIndex()==NPC.FIRE)
            countFire++;
         if(p.getCharIndex()==NPC.DOG)
            countDog++;
         if(p.getCharIndex()==NPC.CHICKEN)
            countChicken++;
         if(p.getCharIndex()==NPC.PIG)
            countPig++;
         if(p.getCharIndex()==NPC.FISH)
            countFish++;
         if(p.getCharIndex()==NPC.SHARK)
            countShark++;
      }
      
     //open doors to shops and find spawn points
      int castleSize = (CultimaPanel.SCREEN_SIZE+25);
      int tavernRow = -1, tavernCol = -1;
      ArrayList<Coord>ourHouse = new ArrayList();
      Coord hellmouth = null;             //record the position of a hellmouth if there is one because we might spawn a creature
      for(int r=0; r<currMap.length; r++)
      {
         for(int c=0; c<currMap[0].length; c++)
         {   
            String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(terr.contains("lava"))
            {
               hellmouth = new Coord(r, c);
            }
            if(terr.contains("tavern"))   //mark location of tavern sign so we can find floors around it for Swine players to go
            {
               tavernRow = r;
               tavernCol = c;
            }
            if(terr.contains("purple_floor"))
            {  //don't allow commoners to wander into our house (purple floor)
               ourHouse.add(new Coord(r, c));
            }
            else if((LocationBuilder.canSpawnOn(currMap, r, c) || TerrainBuilder.isCampableSpot(r, c, mapIndex)))
               spawns.add(new Coord(r, c));                                                                 
            if(TerrainBuilder.isGoodCityMonsterSpawn(r, c, mapIndex))
               monsterSpawns.add(new Coord(r, c));
            if(terr.contains("brick_floor") && capitalCity)
            {
               if(r>=(currMap.length/2 - castleSize/2) && r<=(currMap.length/2 + castleSize/2)
                && c>=(currMap[0].length/2 - castleSize/2) && c<=(currMap[0].length/2 + castleSize/2))
               {}
               else
                  capitalSpawns.add(new Coord(r, c)); 
            } 
            if(CultimaPanel.player.getReputation() <= -1000)   //close shoppe doors if player is a monster
            {
               if(terr.contains("night_door"))
               {
                  currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_I_B_$Night_door_closed").getValue();
                  moveGuard(r, c, mapIndex);   
               }
            }
            else
            {
               if(terr.contains("night_door"))
                  currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_$Night_door_floor").getValue();
            }
           
         }
      }
      
      //find floor locations int taverns for Swine players to spawn on during the day
      ArrayList<Coord> tavernFloor = new ArrayList<Coord>();
      if(tavernRow!=-1 && tavernCol!=-1)
      {
         for(int r=tavernRow-3; r<=tavernRow+3; r++)
            for(int c=tavernCol-3; c<=tavernCol+3; c++)
            {
               if(!LocationBuilder.outOfBounds(currMap, r, c) && !NPCAt(r, c, mapIndex))
               {
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(terr.contains("floor"))
                     tavernFloor.add(new Coord(r, c));
               }
            }     
      }
      ArrayList<Coord> waterLocs = waterLocsInMap(currMap);
   
   //rare chance of a beast running though the city
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      if(locType.contains("city") || locType.contains("fortress") || locType.contains("village") || locType.contains("port"))
      {
         double monsterChance = 0.05;
         if(Math.random() < monsterChance && countMonster==0)  //5% chance monster attacks city
         {
            int row = (int)(Math.random()*currMap.length);
            int col = (int)(Math.random()*currMap[0].length);
            byte monsterType = NPC.randomCityAttackingMonsterSunny();
            if(CultimaPanel.weather > 0)
               monsterType = NPC.randomCityAttackingMonsterRaining();
         
            boolean spawnFound = false;
            if(locType.contains("port"))
            {
               monsterType = NPC.randomPortAttackingWaterMonster();
               if(waterLocs!=null && waterLocs.size() > 0)
               {
                  Coord waterSpawn = getRandomFrom(waterLocs);
                  row = waterSpawn.getRow();
                  col = waterSpawn.getCol();
                  spawnFound = true;
               }
            }
            else
            {
               spawnFound = false;
               if(monsterSpawns.size() > 0)
               {
                  Coord spawn = getRandomFrom(monsterSpawns);
                  row = spawn.getRow();
                  col = spawn.getCol();
                  spawnFound = true;
               }
            }
            if(spawnFound)
            {
               NPCPlayer cityMonster = new NPCPlayer(monsterType, row, col, mapIndex, locType);
               Mission.checkSlayMission(cityMonster);
               cityMonster.setMoveTypeTemp(NPC.CHASE);
               CultimaPanel.worldMonsters.add(cityMonster);
               if(monsterType==NPC.DRAGON || monsterType==NPC.DRAGONKING)
                  Sound.dragonCall();
            
            }
         }
         else if(Math.random() < 0.05 && CultimaPanel.weather == 0 && countFire==0)        
         {//5% chance a fire breaks out in the map
            startRandomFire(mapIndex);
         }
         else if(Math.random() < 0.5 && hellmouth!=null)
         {//25% chance a minion emerges from a hellmouth
            ArrayList<Coord>minionSpawn = new ArrayList();
            for(int r=hellmouth.getRow()-1; r<=hellmouth.getRow()+1; r++)
            {
               for(int c=hellmouth.getCol()-1; c<=hellmouth.getCol()+1; c++)
               {
                  if(!LocationBuilder.outOfBounds(currMap, r, c) && !NPCAt(r, c, mapIndex))
                  {
                     String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                     if(terr.contains("floor"))
                        minionSpawn.add(new Coord(r, c));
                  }
               }
            } 
            if(minionSpawn.size() > 0)
            {
               Coord mSpawn = getRandomFrom(minionSpawn);
               byte monsterType = NPC.randomHellmouthMonster();
               NPCPlayer minion = new NPCPlayer(monsterType, mSpawn.getRow(), mSpawn.getCol(), mapIndex, locType);
               minion.setMoveTypeTemp(NPC.ROAM);
               CultimaPanel.worldMonsters.add(minion);
            }   
         }
      }
   
      if(locType.contains("domicile") || locType.contains("village"))
      {
         int numChickens = (int)(Math.random() * 8);
         if(countChicken != 0)     //we already have chickens
            numChickens = 0;
         for(int i=0; i<numChickens; i++)
         {
            int row = (int)(Math.random()*currMap.length);
            int col = (int)(Math.random()*currMap[0].length);
            byte animalType = NPC.CHICKEN;
            if(spawns.size() > 0)
            {
               Coord spawn = getRandomFrom(spawns);
               row = spawn.getRow();
               col = spawn.getCol();
            }
            NPCPlayer cityAnimal = new NPCPlayer(animalType, row, col, mapIndex, locType);
            CultimaPanel.worldMonsters.add(cityAnimal);
         }
         int numPigs = (int)(Math.random() * 5);
         if(countPig != 0)     //we already have chickens
            numPigs = 0;
         for(int i=0; i<numPigs; i++)
         {
            int row = (int)(Math.random()*currMap.length);
            int col = (int)(Math.random()*currMap[0].length);
            byte animalType = NPC.PIG;
            if(spawns.size() > 0)
            {
               Coord spawn = getRandomFrom(spawns);
               row = spawn.getRow();
               col = spawn.getCol();
            }
            NPCPlayer cityAnimal = new NPCPlayer(animalType, row, col, mapIndex, locType);
            CultimaPanel.worldMonsters.add(cityAnimal);
         }
      }
   
      if(locType.contains("domicile") || locType.contains("village") || locType.contains("city") || locType.contains("fort") || locType.contains("port"))
      { 
         double dogProb = 0.5;
         if(locType.contains("domicile"))
            dogProb = 0.75;
         else if(locType.contains("village"))
            dogProb = 0.66; 
         else if(locType.contains("fort") || locType.contains("port"))
            dogProb = 0.5;
         else  //if(locType.contains("city"))
            dogProb = 0.33;
      
         if(Math.random() < dogProb && countDog==0)      //maybe add one dog
         {
            int row = (int)(Math.random()*currMap.length);
            int col = (int)(Math.random()*currMap[0].length);
            byte animalType = NPC.DOG;
            if(spawns.size() > 0)
            {
               Coord spawn = getRandomFrom(spawns);
               row = spawn.getRow();
               col = spawn.getCol();
            }
            NPCPlayer cityAnimal = new NPCPlayer(animalType, row, col, mapIndex, locType);
            CultimaPanel.worldMonsters.add(cityAnimal);      
         }
      }
      if(locType.contains("port")  || locType.contains("village"))  //add possible fish and maybe shark to port
      {
         double fishProb = 0.66;
         double sharkProb = 0.2;
         ArrayList<Coord> waterSpawns = new ArrayList<Coord>();
         for(Coord wp: waterLocs)
            if(!Utilities.NPCAt(wp.getRow(), wp.getCol(), mapIndex))
               waterSpawns.add(wp);
         if(Math.random() < fishProb && waterSpawns.size() >= 2)
         {
            int numFish = rollDie(1,2);
            if(countFish != 0)     //we already have chickens
               numFish = 0;
            for(int i=0; i<numFish; i++)
            {
               if(waterSpawns.size()==0)
                  break;
               Coord point = waterSpawns.remove((int)(Math.random()*waterSpawns.size()));
               int row = point.getRow();
               int col = point.getCol();
               byte animalType = NPC.FISH;
               NPCPlayer cityAnimal = new NPCPlayer(animalType, row, col, mapIndex, locType);
               CultimaPanel.worldMonsters.add(cityAnimal);
            }
         }
         waterSpawns.clear();
         for(Coord wp: waterLocs)
            if(!NPCAt(wp.getRow(), wp.getCol(), mapIndex))
               waterSpawns.add(wp);
         if(locType.contains("port")  && waterSpawns.size() >= 10 && Math.random() < sharkProb && countShark==0)
         {
            Coord point = getRandomFrom(waterSpawns);
            int row = point.getRow();
            int col = point.getCol();
            byte animalType = NPC.SHARK;
            NPCPlayer cityAnimal = new NPCPlayer(animalType, row, col, mapIndex, locType);
            CultimaPanel.worldMonsters.add(cityAnimal);
         }
      }
      boolean swinePlayerPicked = false;
      int missionCount = 0;
      for(NPCPlayer p: CultimaPanel.civilians.get(mapIndex))
      {
         if(!NPC.isCivilian(p) || p.isTrapped())  //statues stay in place)
            continue; 
         if(NPC.isGuard(p.getCharIndex()) || p.getCharIndex()==NPC.KING || p.getName().toLowerCase().startsWith("cultist")) 
         {
            p.restoreLoc();
            continue;
         }
         if(p.getLocationType().contains("castle"))
         {
            continue;
         }
         if(p.isShopkeep())
         {
            p.restoreLoc();
            continue;
         }
      
         int r = p.getHomeRow();
         int c = p.getHomeCol(); 
       
         if(p.isSellingHouse())  //this is a person looking to sell their home - don't put them on the street
         {
            continue;
         }
         if(p.getNumInfo()==10)
         {
            missionCount++;
         }
         if(tavernFloor.size() > 0 && p.getNumInfo()<=3 && (p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN) && !p.isOurSpouse() && !p.isShopkeep())
         {                       //place a Swine player in the tavern
            if(!swinePlayerPicked)
            {
               Coord spawnLoc = getRandomFrom(tavernFloor);
               p.setMoveTypeTemp(NPC.STILL);
               p.setIsSwinePlayer(true);
               r = spawnLoc.getRow();
               c = spawnLoc.getCol();
               p.setRow(r);
               p.setCol(c);
               if(p.getReputation() < 0 && (Math.random() < 0.5 || p.getReputation()<-1000) && !p.hasItem(Item.getLoadedCube().getName()))
               {                    //wicked swine players will try to cheat with loaded-cubes
                  p.addItem(Item.getLoadedCube());
               }
               swinePlayerPicked = true;
               continue;
            }
            else if(p.swinePlayer())
            {
               p.setIsSwinePlayer(false);
               if(p.hasItem("loaded-cube"))
                  p.removeItem("loaded-cube");
            }
         }
      //***put some npcs indoors depending on the weather
         int stayInRoll = 11;
         if(p.getCharIndex()==NPC.LUTE || p.getCharIndex()==NPC.TAXMAN)
            stayInRoll = 3;
         else if(p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN || p.getCharIndex()==NPC.WISE)
            stayInRoll = 6;
         else if(p.getCharIndex()==NPC.JESTER || p.getCharIndex()==NPC.SWORD)
            stayInRoll = 9;
         if(rollDie(stayInRoll) < CultimaPanel.weather)
         {
            if(r==CultimaPanel.player.getRow() && c==CultimaPanel.player.getCol() && p.getMapIndex()==CultimaPanel.player.getMapIndex())
            {}
            else
            {
               Coord homeCoord = new Coord(p.getHomeRow(), p.getHomeCol());
               if(!ourHouse.contains(homeCoord))
               {
                  p.restoreLoc();
               }
            }
            continue;    
         }
      //***end put some npcs indoors depending on the weather
      
         boolean horiz = false;  //place on horiz or vert street
         if(Math.random() < 0.5)
            horiz = true;   
         if(capitalCity)         //avoid the center where the castle is
         {
            if(capitalSpawns.size() > CultimaPanel.civilians.get(mapIndex).size())
            {
               Coord spawn = getRandomFrom(capitalSpawns);
               r = spawn.getRow();
               c = spawn.getCol();
            }
            else if(spawns.size() > 0)
            {
               Coord spawn = getRandomFrom(spawns);
               r = spawn.getRow();
               c = spawn.getCol();
            }
         }//end if capitalCity
         else
         {   
            ArrayList<Coord> horizStreet = new ArrayList<Coord>();
            ArrayList<Coord> vertStreet = new ArrayList<Coord>();
            for(int row=Math.max(0,(currMap.length/2 - 3)); row<currMap.length && row<=(currMap.length/2 + 3); row++)
               for(int col=6; col<currMap[0].length && col<=currMap[0].length - 6; col++)
               {
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
                  if(terr.contains("purple_floor"))
                  {}    //don't put NPCs in our house
                  else if(LocationBuilder.canSpawnOn(currMap, row, col))
                     horizStreet.add(new Coord(row,col));
               }
            for(int row=6; row<currMap.length && row<=currMap.length - 6; row++)
               for(int col=Math.max(0,(currMap[0].length/2 - 3)); col<currMap[0].length && col<=(currMap[0].length/2 + 3); col++)
               {
                  String terr = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
                  if(terr.contains("purple_floor"))
                  {     //don't put NPCs in our house
                  }
                  else if(LocationBuilder.canSpawnOn(currMap, row, col))
                     vertStreet.add(new Coord(row,col));
               }
            if(horiz && horizStreet.size()>0)
            {
               Coord spawn = getRandomFrom(horizStreet);
               r = spawn.getRow();
               c = spawn.getCol();
            }   
            else if(!horiz && vertStreet.size()>0)
            {
               Coord spawn = getRandomFrom(vertStreet);
               r = spawn.getRow();
               c = spawn.getCol();
            }
            else if(spawns.size() > 0)
            {
               Coord spawn = getRandomFrom(spawns);
               r = spawn.getRow();
               c = spawn.getCol();
            }
         }
         if(NPCAt(r, c, mapIndex))
            p.restoreLoc();
         else
         {
            p.setRow(r);
            p.setCol(c);
            if(p.getMoveType()==NPC.RUN || (p.getMoveType()==NPC.STILL && p.getCharIndex()!=NPC.LUTE && p.getCharIndex()!=NPC.BEGGER && p.getCharIndex()!=NPC.KING))
               p.setMoveType(NPC.ROAM);
            if(p.getCharIndex()==NPC.TAXMAN && CultimaPanel.days > p.getEndChaseDay())  //make TAXMAN march a certain direction
            {
               int randDir = rollDie(1,4);
               if(randDir==1)
                  p.setMoveType(NPC.MARCH_NORTH);
               else if(randDir==2)
                  p.setMoveType(NPC.MARCH_EAST);
               else if(randDir==3)
                  p.setMoveType(NPC.MARCH_SOUTH);
               else //if(randDir==4)
                  p.setMoveType(NPC.MARCH_WEST);
            }   
         }
      }
      if(missionCount == 0 && Math.random() < 0.1 && mapIndex > 0 && mapIndex < CultimaPanel.civilians.size() &&  CultimaPanel.civilians.get(mapIndex).size()>0)
      {     //pick a random person in this location to have a mission
         int randIndex = (int)(Math.random()* CultimaPanel.civilians.get(mapIndex).size());
         NPCPlayer randPick = CultimaPanel.civilians.get(mapIndex).get(randIndex);
         if(!randPick.isZombie() && !randPick.isStatue() && NPC.isCivilian(randPick) && !randPick.isOurSpouse() && randPick.getCharIndex()!=NPC.TRADER)
         {
            randPick.setNumInfo(10);
         }
      }
   }

//only opens up the endgame temple doors for players at level 50 or above, or on the 50th day.
//might also add a monster king to protect the temple
   public static void setTempleDoors(int mapIndex)
   {
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));
      boolean holiday = (CultimaPanel.days % 100 == 50);
   
      ArrayList<Coord>spawns = new ArrayList<Coord>();
   
      for(int r=0; r<currMap.length; r++)
      {
         for(int c=0; c<currMap[0].length; c++)
         {
            if(LocationBuilder.canSpawnOn(currMap, r, c) || TerrainBuilder.isCampableSpot(r, c, mapIndex))
               spawns.add(new Coord(r, c));
            if(CultimaPanel.player.getLevel() < 50 && !holiday)   //close temple doors if player is not experienced enough
            {
               String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               if(curr.contains("night_door"))
               {
                  currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_I_B_$Night_door_closed").getValue();
                  moveGuard(r, c, mapIndex);   
               }
            }
            else
            {
               String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               if(curr.contains("night_door"))
                  currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_$Night_door_floor").getValue();
            }
         }
      }
      
      if(Math.random() < .5)
      {
         int row = (int)(Math.random()*currMap.length);
         int col = (int)(Math.random()*currMap[0].length);
         byte monsterType = NPC.randomMonsterKing();
         if(spawns.size() > 0)
         {
            Coord spawn = getRandomFrom(spawns);
            row = spawn.getRow();
            col = spawn.getCol();
         }
         NPCPlayer templeMonster = new NPCPlayer(monsterType, row, col, mapIndex, "temple");
         templeMonster.setMoveTypeTemp(NPC.CHASE);
         templeMonster.setGold(0);
         CultimaPanel.worldMonsters.add(templeMonster);
      }
   }

//we are trying to close a night-door at a shoppe, so move any guards in the way of the door to another location
   public static void moveGuard(int row, int col, int mapIndex)
   {
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));   
   
      ArrayList<Coord> horizStreet = new ArrayList<Coord>();
      ArrayList<Coord> vertStreet = new ArrayList<Coord>();
      for(int r=Math.max(0,(currMap.length/2 - 3)); r<currMap.length && r<=(currMap.length/2 + 3); r++)
         for(int c=6; c<currMap[0].length && c<=currMap[0].length - 6; c++)
            if(LocationBuilder.canSpawnOn(currMap, r, c))
               horizStreet.add(new Coord(r,c));
      for(int r=6; r<currMap.length && r<=currMap.length - 6; r++)
         for(int c=Math.max(0,(currMap[0].length/2 - 3)); c<currMap[0].length && c<=(currMap[0].length/2 + 3); c++)
            if(LocationBuilder.canSpawnOn(currMap, r, c))
               vertStreet.add(new Coord(r,c));
      for(NPCPlayer p: CultimaPanel.civilians.get(mapIndex))
      {
         if(NPC.isGuard(p.getCharIndex()) && p.getRow()==row && p.getCol()==col)
         {
            int r = p.getHomeRow();
            int c = p.getHomeCol();   
            boolean horiz = false;      //place on horiz or vert street
            if(Math.random() < 0.5)
               horiz = true;   
            if(horiz && horizStreet.size()>0)
            {
               Coord spawn = getRandomFrom(horizStreet);
               r = spawn.getRow();
               c = spawn.getCol();
            }   
            else if(!horiz && vertStreet.size()>0)
            {
               Coord spawn = getRandomFrom(vertStreet);
               r = spawn.getRow();
               c = spawn.getCol();
            }
            p.setRow(r);
            p.setCol(c);
         }
      }
   }

   public static void makeNPCsRoam(int mapIndex)
   {
      for(NPCPlayer p: CultimaPanel.civilians.get(mapIndex))
      {
         if((NPC.isGuard(p.getCharIndex()) && p.getMoveType()==NPC.STILL) || p.getCharIndex()==NPC.KING) 
         {
            p.restoreLoc();
            continue;
         }
         if(p.getCharIndex()==NPC.LUTE || p.getCharIndex()==NPC.BEGGER)
            continue;
         if(p.getName().startsWith("Madame ") || p.getName().startsWith("Mage ") || p.getName().startsWith("Ironsmith ")
         || p.getName().startsWith("Butcher ") || p.getName().startsWith("Barkeep "))
         {
            p.restoreLoc();
            continue;
         }
         if(p.getMoveType()==NPC.STILL)
            p.setMoveType(NPC.ROAM);
      }
   }

  //set any brigand within our sights to chase
   public static void alertBrigands()
   {
      for(NPCPlayer closeBrig: CultimaPanel.NPCinSight)
      {
         if(NPC.isBrigand(closeBrig.getCharIndex()) && closeBrig.getMoveType()!=NPC.CHASE)
         {
            closeBrig.setHasBeenAttacked(true);
            closeBrig.setMoveType(NPC.CHASE);
         }
      }
   }

   public static void alertGuards()
   {         
      boolean timestop = (CultimaPanel.player.getActiveSpell("Timestop")!=null);
      if(timestop)
      {
         return;
      }
      int mapIndex = CultimaPanel.player.getMapIndex();
      String loc = CultimaPanel.player.getLocationType();
      Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      String thisLocName = thisLoc.getName().toLowerCase();
      boolean inCamp = (loc.toLowerCase().contains("camp") || thisLocName.endsWith(" camp"));
      
      boolean guards = false;
      for(NPCPlayer p: CultimaPanel.civilians.get(mapIndex))
      {
         inCamp = inCamp || p.getLocationType().contains("camp");
         if(NPC.isGuard(p.getCharIndex()) || (inCamp && NPC.isAnyBrigand(p.getCharIndex())))
         {
            p.setMoveTypeTemp(NPC.CHASE);
            p.setEndChaseDay(CultimaPanel.days + 2);
            guards = true;
         }
      }
      if(!TerrainBuilder.habitablePlace(loc))
         for(NPCPlayer p: CultimaPanel.worldMonsters)
         {
            inCamp = inCamp || p.getLocationType().contains("camp");
            if((NPC.isBrigand(p.getCharIndex()) || NPC.isGuard(p.getCharIndex())) && p.getMapIndex()==mapIndex)
            {
               p.setMoveTypeTemp(NPC.CHASE);
               p.setHasBeenAttacked(true);
               guards = true;
            }
         }
      if(guards)
      {
         if(System.currentTimeMillis() > CultimaPanel.alertTime + 5000)
         {
            CultimaPanel.sendMessage("-----");
            CultimaPanel.sendMessage("~"+getRandomFrom(NPC.guardAlert).replace("ADJECTIVE",getRandomFrom(NPC.insultAdjective)).replace("NOUN",getRandomFrom(NPC.insultNoun)));
            Sound.alertGuards();        //sound
            CultimaPanel.alertTime = System.currentTimeMillis();
         }
         if(inCamp)
         {
            CultimaPanel.sendMessage("~"+NPC.getRandomFrom(NPC.cageOpen)); 
         }
         else if(!TerrainBuilder.habitablePlace(loc) && !loc.contains("arena"))
            CultimaPanel.sendMessage("Brigands alerted!");
         else
            CultimaPanel.sendMessage("Guards alerted!");
      }
   }
   
   public static void standDownGuards()
   {         
      int mapIndex = CultimaPanel.player.getMapIndex();
      String loc = CultimaPanel.player.getLocationType();
      boolean command = false;
      for(NPCPlayer p: CultimaPanel.civilians.get(mapIndex))
      {
         if(NPC.isGuard(p.getCharIndex()) && p.getMoveType()==NPC.CHASE)
         {
            int dir=rollDie(1,5);
            if(dir==1)
               p.setMoveTypeTemp(NPC.MARCH_NORTH);
            else if(dir==2)
               p.setMoveTypeTemp(NPC.MARCH_EAST);
            else if(dir==3)
               p.setMoveTypeTemp(NPC.MARCH_SOUTH);
            else if(dir==4)
               p.setMoveTypeTemp(NPC.MARCH_WEST);
            else
               p.setMoveTypeTemp(NPC.ROAM);
            p.setEndChaseDay(-1);
            command = true;
         }
      }
      if(command)
      {
         if(System.currentTimeMillis() > CultimaPanel.cheerTime + 5000)
         {
            CultimaPanel.cheerTime = System.currentTimeMillis();
            Sound.alertGuards();        //sound
         }
         CultimaPanel.sendMessage("Guards stand down");
      }
   }
   
   //if we pay our taxex in the city and there are more than one taxman, make them aware of it
   public static void standDownTaxmen()
   {         
      int mapIndex = CultimaPanel.player.getMapIndex();
      String loc = CultimaPanel.player.getLocationType();
      for(NPCPlayer p: CultimaPanel.civilians.get(mapIndex))
      {
         if(p.getCharIndex()==NPC.TAXMAN)
         {
            p.setEndChaseDay(CultimaPanel.days + 15);
            p.setMoveType(NPC.ROAM);
            p.setHasBeenAttacked(false);
         }
      }
      standDownGuards();
   }

//if we win the 3-doors puzzle, remove the magic dragons
   public static void removeDragons()
   {         
      int mapIndex = CultimaPanel.player.getMapIndex();
      for(int i = CultimaPanel.civilians.get(mapIndex).size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.civilians.get(mapIndex).get(i);
         if(p.getCharIndex()==NPC.DRAGON)
         {
            int r = p.getRow();
            int c = p.getCol();
            removeNPCat(r, c, mapIndex);
         }
      }
   }

   public static void makeNPCsRun(int mapIndex)
   {
      for(NPCPlayer p: CultimaPanel.civilians.get(mapIndex))
      {
         if(NPC.isCivilian(p))
         {
            if(CultimaPanel.player.getReputation() <= -100)
            {
               if(NPC.isGuard(p.getCharIndex()) || p.getCharIndex()==NPC.SWORD)
               {
                  if(rollDie(100) < Math.abs(CultimaPanel.player.getReputation())/10 && !p.getLocationType().toLowerCase().contains("castle"))
                     p.setMoveTypeTemp(NPC.RUN);
                  else
                  {  //TO DO: check if player has charm spell, ring or lute
                     p.setMoveTypeTemp(NPC.CHASE);
                     if(NPC.isGuard(p.getCharIndex()))
                        p.setEndChaseDay(CultimaPanel.days + 1);
                  }
               }
               else if(rollDie(100) < Math.abs(CultimaPanel.player.getReputation())/10)
                  p.setMoveTypeTemp(NPC.RUN);
            }
         }
      }
   }
 
   public static boolean isNPCInSight(NPCPlayer target)
   {
      for(NPCPlayer p: CultimaPanel.NPCinSight)
         if(p==target || p.equals(target))
            return true;
      return false;
   }
            
   //true if p should move towards and attack target
   public static boolean areEnemies(NPCPlayer p, NPCPlayer target)
   {
      if(p==null || target==null)
      {
         return false;
      }
      if(p.equals(target) || p.getBody()<=0 || target.getBody()<=0 || p.isStatue() || target.isStatue() || target.getCharIndex()==NPC.BARREL)
      {
         return false;
      }
      if(NPC.isNatural(target) || p.getCharIndex()==NPC.WILLOWISP  || target.getCharIndex()==NPC.WILLOWISP || p.isNonPlayer() || target.isNonPlayer())
      {
         return false;
      } 
      if((p.getCharIndex()==NPC.SLIME || NPC.isNatural(p)) && (target.getCharIndex()==NPC.SLIME || NPC.isNatural(target)))
      {
         return false;
      } 
      if(p.isCaged() || target.isCaged())
      {
         return false;           //don't attack creatures that are totally blocked in
      }
      if(NPC.isCivilian(p) && (NPC.isCivilian(target) && !target.hasEffect("control")  && !target.isZombie()))
      {  //includes guards
         return false;           //civilians do not attack other civilians unless they are possessed or zombies
      }
      if(target.getName().equalsIgnoreCase("Keeper of the Bridge") || target.getName().equalsIgnoreCase("Black Knight") || target.getName().toLowerCase().contains("puzzle"))
      {
         return false;
      }
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      String thisLocName = thisLoc.getName().toLowerCase();
      boolean inCamp = (locType.contains("camp") || thisLocName.endsWith(" camp") || p.getLocationType().toLowerCase().contains("camp"));
      boolean inBattleField = (locType.contains("battlefield") || p.getLocationType().toLowerCase().contains("battlefield"));
      boolean pIsHumanSide =    NPC.isBattleFieldFriend(p);
      boolean targIsHumanSide = NPC.isBattleFieldFriend(target);
      boolean nonHuman = !NPC.isCivilian(target);
      boolean aggressive = ((target.getMoveType() == NPC.CHASE) || (target.getCharIndex()==NPC.JESTER && target.getMoveType()==NPC.RUN));
      if(NPC.isAssassin(p.getCharIndex()) && NPC.isCivilian(target.getCharIndex()) && TerrainBuilder.isTown(locType))
      {  //assassins are out for you, not civilians (does not include castles, because we want castle guards to not tolerate bounty hunters and assassins near royalty)
         return false;
      }
      if(NPC.isGuard(p.getCharIndex()) && NPC.isAssassin(target.getCharIndex()) && TerrainBuilder.isTown(locType))
      {  //city guards train with bounty hunters, so they leave them be
         return false;
      }
      if(p.isOurDog() && (isGameForDog(target) || ((nonHuman || aggressive) && target.getCharIndex()!=NPC.DOG && !NPC.isHorse(target) && !NPC.isShip(target))))
      {
         return true;
      }
      if((p.hasEffect("control") || p.isOurDog() || p.isEscortedNpc()) && (target.hasEffect("control") || target.isEscortedNpc() || target.isOurDog() || target.hasMet()))
      {
         return false;           //controlled creatures will not attack other controlled creatures or our dog or escortee
      }
      if(NPC.isGiantMonsterKing(p))
      {
         return true;
      }
      if(inBattleField)
      {
         return ((pIsHumanSide && !targIsHumanSide) || (!pIsHumanSide && targIsHumanSide));
      }
      if(inCamp)
      {
         if(NPC.isImprisonedMonster(p) && (NPC.isGuard(target.getCharIndex()) || NPC.isAnyBrigand(target.getCharIndex())))
         {                       //imprisoned monsters will attack anything that has imprisoned them
            return true;     
         }
         else if(NPC.isGuard(p.getCharIndex()) || NPC.isAnyBrigand(p.getCharIndex()))
         {                       //camp guards will attack anything that is possessed or not in their family
            if(NPC.isHorse(target))
            {
               return false;
            }
            if((target.hasEffect("control") && p.hasBeenAttacked()) || !NPC.isInFamily(p, target))
            {
               if(target.isOurDog() && target.getMoveType()!=NPC.ATTACK)
               {
                  return false;
               }
               return true;
            }
         }
      }
      else if(TerrainBuilder.habitablePlace(locType))
      { 
         if(p.getName().contains("Brute"))   //for mob targeting taxman mission
         {
            if(target.getCharIndex()==NPC.TAXMAN)
               return true;                  //the brute squad called on the taxman only targets the taxman and the player
            else
               return false;
         } 
         else if(NPC.isCityAttackingMonster(p) && NPC.isCivilian(target))
         {
            return true;
         } 
         else if(NPC.isGuard(p.getCharIndex()))
         {
            if(target.getName().toLowerCase().contains("sibling") || target.getName().toLowerCase().contains("brute"))
            {  //we need to take out these characters to make the mission a challenge - the guards don't get involved in this one
               return false;
            }
            else if(!NPC.isInFamily(p, target) && !NPC.isTameAnimal(target))
            {
               return true;
            }
         }
         else if(!NPC.isTameAnimal(p) && !NPC.isCivilian(p) && !NPC.isInFamily(p, target) && (NPC.isCivilian(target) || NPC.isTameAnimal(target)))
         {
            return true;
         }
      }
      if(p.hasEffect("control") && (target.hasBeenAttacked() || target.getMoveType()==NPC.CHASE))
      {                          //safe call because if p has control, a previous if will return false if target is a dog or escorted npc
         return true;            //our controlled creatures will attack anything that we have attacked or are chasing us
      }
      return false;
   }
      
    //returns the closest enemy NPC to p that is not at that position
   public static NPCPlayer closestEnemyNPCTarget(NPCPlayer p)
   {
      NPCPlayer temp = null;
      double dist = CultimaPanel.SCREEN_SIZE*4;
      int row = p.getRow();
      int col = p.getCol();
      int mi = p.getMapIndex();
      for(NPCPlayer target: CultimaPanel.civilians.get(p.getMapIndex()))
      { 
         if(!areEnemies(p, target))
         {
            continue;
         } 
         double currDist = Display.findDistance(row, col, target.getRow(), target.getCol());
         if(currDist < dist)
         {
            dist = currDist;
            temp = target;
         }
      }
      for(NPCPlayer target: CultimaPanel.worldMonsters)
      { 
         if(p.getMapIndex()!=target.getMapIndex() || !areEnemies(p, target))
         {
            continue;
         } 
         double currDist = Display.findDistance(row, col, target.getRow(), target.getCol());
         if(currDist < dist)
         {
            dist = currDist;
            temp = target;
         }
      }
   
      return temp;
   }
      
   public static boolean isGameForDog(NPCPlayer target)
   {
      if(target == null)
         return false;
      return (target.getCharIndex()==NPC.FISH || target.getCharIndex()==NPC.RABBIT || target.getCharIndex()==NPC.ELK || target.getCharIndex()==NPC.CHICKEN || target.getCharIndex()==NPC.PIG || (target.getCharIndex()==NPC.JESTER && target.getMoveType()==NPC.RUN));
   }

   public static boolean search(byte[]list, byte target)
   {
      for(byte b:list)
         if(b==target)
            return true;
      return false;
   }

//returns forest locations if the player is in view of dense forest - for spawn location of small monsters
   public static ArrayList<Coord> deepForestInView()
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      Terrain deepForest = TerrainBuilder.getTerrainWithName("TER_S_B_$Dense_forest");
      byte[][]currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      for(int r=CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2; r++)
      {
         for(int c=CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            int row = r;
            int col = c;
            if(CultimaPanel.player.getMapIndex() == 0)
            {
               row = CultimaPanel.equalizeWorldRow(r);
               col = CultimaPanel.equalizeWorldCol(c);
               if(Math.abs(currMap[row][col]) == deepForest.getValue())
                  locs.add(new Coord(row, col));    
            }
            else //if(CultimaPanel.player.getMapIndex() != 0)
            {
               if(row>=0 && row<currMap.length && col>=0 && col<currMap[0].length && Math.abs(currMap[row][col]) == deepForest.getValue())
                  locs.add(new Coord(row, col));
            } 
         }
      }
      return locs;
   }

//returns water locations if the player is in view of water - for spawn location of water monsters
   public static ArrayList<Coord> waterInView(byte monsterIndex)
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      byte[][]currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      for(int r=CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2; r++)
         for(int c=CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2; c++)
            if(CultimaPanel.player.getMapIndex() != 0)
            {
               if(r>=0 && r<currMap.length && c>=0 && c<currMap[0].length)
               {
                  String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(current.contains("water"))
                  {
                     if(NPC.isShip(monsterIndex))
                     {
                        if(r==CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2 || r==CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2
                        || c==CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2 || c==CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2)
                           locs.add(new Coord(r, c));
                     }
                     else
                     {
                        boolean waterLeft = (c-1>=0 && CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase().contains("water"));
                        boolean waterRight = (c+1<currMap[0].length && CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase().contains("water"));
                        boolean waterUp = (r-1>=0 && CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase().contains("water"));
                        boolean waterDown = (r+1<currMap.length && CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase().contains("water"));
                        if(waterLeft && waterRight && waterUp && waterDown)
                           locs.add(new Coord(r, c));
                     }
                  }
               }
            }
            else
            {
               int row = CultimaPanel.equalizeWorldRow(r);
               int col = CultimaPanel.equalizeWorldCol(c);
               String current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
               if(current.contains("water"))
               {
                  if(NPC.isShip(monsterIndex))
                  {
                     if(row==CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2 || row==CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2
                     || col==CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2 || col==CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2)
                        locs.add(new Coord(row, col));
                  }
                  else
                  {
                     boolean waterLeft = (CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col-1)])).getName().toLowerCase().contains("water"));
                     boolean waterRight = (CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col+1)])).getName().toLowerCase().contains("water"));
                     boolean waterUp = (CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row-1)][col])).getName().toLowerCase().contains("water"));
                     boolean waterDown = (CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row+1)][col])).getName().toLowerCase().contains("water"));
                     if(waterLeft && waterRight && waterUp && waterDown)
                        locs.add(new Coord(row, col));
                  }
               }
            }
      return locs;
   }
   
    //returns water locations if the player is in view of water for their world location
   public static ArrayList<Coord> waterInView()
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      byte[][]currMap = CultimaPanel.map.get(0);
      for(int r=CultimaPanel.player.getWorldRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getWorldRow()+CultimaPanel.SCREEN_SIZE/2; r++)
         for(int c=CultimaPanel.player.getWorldCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getWorldCol()+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            int row = CultimaPanel.equalizeWorldRow(r);
            int col = CultimaPanel.equalizeWorldCol(c);
            String current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
            if(current.contains("water"))
            {
               boolean waterLeft = (CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col-1)])).getName().toLowerCase().contains("water"));
               boolean waterRight = (CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col+1)])).getName().toLowerCase().contains("water"));
               boolean waterUp = (CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row-1)][col])).getName().toLowerCase().contains("water"));
               boolean waterDown = (CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row+1)][col])).getName().toLowerCase().contains("water"));
               if(waterLeft && waterRight && waterUp && waterDown)
                  locs.add(new Coord(row, col));
            }
         }
      return locs;
   }
   
   //returns true if there is water in the location or in the world around that location, used for RAISE_WATER mission
   public static boolean waterInView(Location thisLoc)
   {
      int currentLocIndex = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, thisLoc.getRow(), thisLoc.getCol(), thisLoc.getFromMapIndex());
      byte[][]currMap = CultimaPanel.map.get(currentLocIndex);
      byte[][]worldMap = CultimaPanel.map.get(0);
      for(int r=thisLoc.getRow()-10; r<=thisLoc.getRow()+10; r++)
      {
         for(int c=thisLoc.getCol()-10; c<=thisLoc.getCol()+10; c++)
         {
            int row = CultimaPanel.equalizeWorldRow(r);
            int col = CultimaPanel.equalizeWorldCol(c);
            String current = CultimaPanel.allTerrain.get(Math.abs(worldMap[row][col])).getName().toLowerCase();
            if(current.contains("water"))
            {
               return true;
            }
         }
      }
      for(int r = 0; r < currMap.length; r++)
      {
         for(int c = 0; c < currMap[0].length; c++)
         {
            String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(current.contains("water"))
            {
               return true;
            }
         }
      }
      return false;
   }
   
   //returns water locations if the player is in view of water for their world location that are next to a dock
   public static ArrayList<Coord> dockSpots()
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      byte[][]currMap = CultimaPanel.map.get(0);
      for(int r=CultimaPanel.player.getWorldRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getWorldRow()+CultimaPanel.SCREEN_SIZE/2; r++)
         for(int c=CultimaPanel.player.getWorldCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getWorldCol()+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            int row = CultimaPanel.equalizeWorldRow(r);
            int col = CultimaPanel.equalizeWorldCol(c);
            String current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
            if(current.contains("water") && nextToAPier(currMap, row, col))
            {
               locs.add(new Coord(row, col));
            }
         }
      return locs;
   }
   
   //true if we are next to a bridge/pier
   public static boolean nextToAPier(byte[][]currMap, int row, int col)
   {
      String current = "";
      if(row+1>=0 && row+1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row+1][col])).getName().toLowerCase();
         if(current.contains("wood_plank_floor"))
            return true;
      }
      if(row-1>=0 && row-1<currMap.length && col>=0 && col<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row-1][col])).getName().toLowerCase();
         if(current.contains("wood_plank_floor"))
            return true;
      }
      if(row>=0 && row<currMap.length && col+1>=0 && col+1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col+1])).getName().toLowerCase();
         if(current.contains("wood_plank_floor"))
            return true;
      }
      if(row>=0 && row<currMap.length && col-1>=0 && col-1<currMap[0].length)
      {
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col-1])).getName().toLowerCase();
         if(current.contains("wood_plank_floor"))
            return true;
      }
      return false;
   }
   
  //returns the location of a monster spawn somewhere close to row,col 
   public static Coord findMonsterSpawn(int row, int col, byte index)
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      byte[][]currMap = CultimaPanel.map.get(0);
      for(int r=row-CultimaPanel.SCREEN_SIZE/2; r<=row+CultimaPanel.SCREEN_SIZE/2; r++)
         for(int c=col-CultimaPanel.SCREEN_SIZE/2; c<=col+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            int erow = CultimaPanel.equalizeWorldRow(r);
            int ecol = CultimaPanel.equalizeWorldCol(c);
            String current = CultimaPanel.allTerrain.get(Math.abs(currMap[erow][ecol])).getName().toLowerCase();
            if((!current.contains("i_") && !current.contains("water")) || (index==NPC.DRAGON || index==NPC.DRAGONKING))
               locs.add(new Coord(erow, ecol));
         }
      if(locs.size()==0)
         return null;
      return getRandomFrom(locs);
   }
   
   //returns the location of a monster spawn somewhere off an unblocked straight line to the location at row,col 
   public static Coord findKaijuMonsterSpawn(int row, int col)
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      byte[][]currMap = CultimaPanel.map.get(0);
      //pick spots that are 5-8 spaces away from (row, col) that are not blocked by impassables or water
      for(int r=row; r>=row-8; r--)
      {
         int erow = CultimaPanel.equalizeWorldRow(r);
         int ecol = CultimaPanel.equalizeWorldCol(col);
         String current = CultimaPanel.allTerrain.get(Math.abs(currMap[erow][ecol])).getName().toLowerCase();
         if(current.contains("i_") || current.contains("water"))
         {
            break;
         }
         if(erow <= CultimaPanel.equalizeWorldRow(row-5))
         {
            locs.add(new Coord(erow, ecol));
         }
      }
      for(int c=col; c<col+8; c++)
      {
         int erow = CultimaPanel.equalizeWorldRow(row);
         int ecol = CultimaPanel.equalizeWorldCol(c);
         String current = CultimaPanel.allTerrain.get(Math.abs(currMap[erow][ecol])).getName().toLowerCase();
         if(current.contains("i_") || current.contains("water"))
         {
            break;
         }
         if(ecol >= CultimaPanel.equalizeWorldCol(col+5))
         {
            locs.add(new Coord(erow, ecol));
         }
      }
      for(int r=row; r<=row+8; r++)
      {
         int erow = CultimaPanel.equalizeWorldRow(r);
         int ecol = CultimaPanel.equalizeWorldCol(col);
         String current = CultimaPanel.allTerrain.get(Math.abs(currMap[erow][ecol])).getName().toLowerCase();
         if(current.contains("i_") || current.contains("water"))
         {
            break;
         }
         if(erow >= CultimaPanel.equalizeWorldRow(row+5))
         {
            locs.add(new Coord(erow, ecol));
         }
      }
      for(int c=col; c>=col-8; c--)
      {
         int erow = CultimaPanel.equalizeWorldRow(row);
         int ecol = CultimaPanel.equalizeWorldCol(c);
         String current = CultimaPanel.allTerrain.get(Math.abs(currMap[erow][ecol])).getName().toLowerCase();
         if(current.contains("i_") || current.contains("water"))
         {
            break;
         }
         if(ecol <= CultimaPanel.equalizeWorldCol(col-5))
         {
            locs.add(new Coord(erow, ecol));
         }
      }
         
      if(locs.size()==0)
         return null;
      return getRandomFrom(locs);
   }

//finds location for a monster to spawn in to on the border in the direction of movement (dir), or deep forest for smaller monsters
   public static Coord findMonsterSpawn(String dir, byte index)
   {
      byte[][]currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      byte [] smallMonsters = {NPC.BUGBEAR, NPC.DEMON, NPC.GHOST, NPC.ORC, NPC.SKELETON, NPC.TREE, NPC.TROLL, NPC.BAT, NPC.ELK, NPC.HORSE, NPC.UNICORN, NPC.RAT, NPC.SNAKE, NPC.SPIDER, NPC.WOLF, 
                               NPC.BRIGAND_SWORD,  NPC.BRIGAND_SPEAR,  NPC.BRIGAND_HAMMER,  NPC.BRIGAND_CROSSBOW,  NPC.BRIGAND_DAGGER,  NPC.BRIGAND_FIST, NPC.SORCERER};
      byte [] flyingMonsters = {NPC.DRAGON, NPC.DRAGONKING, NPC.GHOST, NPC.BAT, NPC.BATKING, NPC.TORNADO, NPC.WILLOWISP};
   
      boolean isSmallMonster = search(smallMonsters, index);
      boolean okLeft=true, okRight=true, okUp=true, okDown=true;
   
      String current = "";
   
   //add monster into deep forest for a suprise
      ArrayList<Coord> locs = deepForestInView();
      if(locs.size()>0 && isSmallMonster && Math.random() < .5)
         return getRandomFrom(locs);
      locs = new ArrayList<Coord>();
      if(dir.equals("any"))
      {
         for(int r=3; r<currMap.length-3; r++)
            for(int c=3; c<currMap[0].length-3; c++)
            {
               if(!CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase().contains("water") 
               && !LocationBuilder.isImpassable(currMap, r, c) && !LocationBuilder.isBreakable(currMap, r, c))
                  locs.add(new Coord(r, c));
            }
      }
      else if(dir.equals("North"))
      {
         int r = CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2;
         if(CultimaPanel.player.getMapIndex() != 0 && (r<0 || r>= currMap.length))
            return null;
         if(CultimaPanel.player.getMapIndex() == 0)
            r = CultimaPanel.equalizeWorldRow(r);
         for(int c=CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            if(CultimaPanel.player.getMapIndex() != 0 && (c<0 || c>= currMap[0].length))
               continue;
            if(CultimaPanel.player.getMapIndex() != 0)
            {
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               okLeft = (c-1>=0 && !CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase().contains("water"));
               okRight = (c+1<currMap[0].length && !CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase().contains("water"));
               okUp = (r-1>=0 && !CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase().contains("water"));
               okDown = (r+1<currMap.length && !CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase().contains("water"));
            }
            int row = r, col = c;
            if(CultimaPanel.player.getMapIndex() == 0)
            {
               row = CultimaPanel.equalizeWorldRow(r);
               col = CultimaPanel.equalizeWorldCol(c);
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
               okLeft = (!CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col-1)])).getName().toLowerCase().contains("water"));
               okRight = (!CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col+1)])).getName().toLowerCase().contains("water"));
               okUp = (!CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row-1)][col])).getName().toLowerCase().contains("water"));
               okDown = (!CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row+1)][col])).getName().toLowerCase().contains("water"));
            }
            if((!LocationBuilder.isImpassable(currMap, row, col) && !LocationBuilder.isBreakable(currMap, row, col) && !current.contains("water") && okLeft && okRight && okUp && okDown))
               locs.add(new Coord(row, col));
         }
      }
      else if(dir.equals("South"))
      {
         int r = CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2;
         if(CultimaPanel.player.getMapIndex() != 0 && (r<0 || r>= currMap.length))
            return null;
         if(CultimaPanel.player.getMapIndex() == 0)
            r = CultimaPanel.equalizeWorldRow(r);
         for(int c=CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2; c++)
         {
            if(CultimaPanel.player.getMapIndex() != 0 && (c<0 || c>= currMap[0].length))
               continue;
            if(CultimaPanel.player.getMapIndex() != 0)
            {
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               okLeft = (c-1>=0 && !CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase().contains("water"));
               okRight = (c+1<currMap[0].length && !CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase().contains("water"));
               okUp = (r-1>=0 && !CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase().contains("water"));
               okDown = (r+1<currMap.length && !CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase().contains("water"));
            }
            int row = r, col = c;
            if(CultimaPanel.player.getMapIndex() == 0)
            {
               row = CultimaPanel.equalizeWorldRow(r);
               col = CultimaPanel.equalizeWorldCol(c);
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
               okLeft = (!CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col-1)])).getName().toLowerCase().contains("water"));
               okRight = (!CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col+1)])).getName().toLowerCase().contains("water"));
               okUp = (!CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row-1)][col])).getName().toLowerCase().contains("water"));
               okDown = (!CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row+1)][col])).getName().toLowerCase().contains("water"));
            }        
            if((!LocationBuilder.isImpassable(currMap, row, col) && !LocationBuilder.isBreakable(currMap, row, col) && !current.contains("water") && okLeft && okRight && okUp && okDown))
               locs.add(new Coord(row, col));
         }
      }
      else if(dir.equals("West"))
      {
         int c = CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2;
         if(CultimaPanel.player.getMapIndex() != 0 && (c<0 || c>= currMap[0].length))
            return null;
         if(CultimaPanel.player.getMapIndex() == 0) 
            c = CultimaPanel.equalizeWorldCol(c);
         for(int r=CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2; r++)
         {
            if(CultimaPanel.player.getMapIndex() != 0 && (r<0 || r>= currMap.length))
               continue;
            if(CultimaPanel.player.getMapIndex() != 0)
            {
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               okLeft = (c-1>=0 && !CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase().contains("water"));
               okRight = (c+1<currMap[0].length && !CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase().contains("water"));
               okUp = (r-1>=0 && !CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase().contains("water"));
               okDown = (r+1<currMap.length && !CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase().contains("water"));
            }
            int row = r, col = c;
            if(CultimaPanel.player.getMapIndex() == 0)
            {
               row = CultimaPanel.equalizeWorldRow(r);
               col = CultimaPanel.equalizeWorldCol(c);
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
               okLeft = (!CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col-1)])).getName().toLowerCase().contains("water"));
               okRight = (!CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col+1)])).getName().toLowerCase().contains("water"));
               okUp = (!CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row-1)][col])).getName().toLowerCase().contains("water"));
               okDown = (!CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row+1)][col])).getName().toLowerCase().contains("water"));
            }
         
            if((!LocationBuilder.isImpassable(currMap, row, col) && !LocationBuilder.isBreakable(currMap, row, col) && !current.contains("water") && okLeft && okRight && okUp && okDown))
               locs.add(new Coord(row, col));          
         }
      }
      else if(dir.equals("East"))
      {
         int c = CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2;
         if(CultimaPanel.player.getMapIndex() != 0 && (c<0 || c>= currMap[0].length))
            return null;
         if(CultimaPanel.player.getMapIndex() == 0) 
            c = CultimaPanel.equalizeWorldCol(c);
         for(int r=CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2; r++)
         {
            if(CultimaPanel.player.getMapIndex() != 0 && (r<0 || r>= currMap.length))
               continue;
            if(CultimaPanel.player.getMapIndex() != 0)
            {
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
               okLeft = (c-1>=0 && !CultimaPanel.allTerrain.get(Math.abs(currMap[r][c-1])).getName().toLowerCase().contains("water"));
               okRight = (c+1<currMap[0].length && !CultimaPanel.allTerrain.get(Math.abs(currMap[r][c+1])).getName().toLowerCase().contains("water"));
               okUp = (r-1>=0 && !CultimaPanel.allTerrain.get(Math.abs(currMap[r-1][c])).getName().toLowerCase().contains("water"));
               okDown = (r+1<currMap.length && !CultimaPanel.allTerrain.get(Math.abs(currMap[r+1][c])).getName().toLowerCase().contains("water"));
            }
            int row = r, col = c;
            if(CultimaPanel.player.getMapIndex() == 0)
            {
               row = CultimaPanel.equalizeWorldRow(r);
               col = CultimaPanel.equalizeWorldCol(c);
               current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
               okLeft = (!CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col-1)])).getName().toLowerCase().contains("water"));
               okRight = (!CultimaPanel.allTerrain.get(Math.abs(currMap[row][CultimaPanel.equalizeWorldCol(col+1)])).getName().toLowerCase().contains("water"));
               okUp = (!CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row-1)][col])).getName().toLowerCase().contains("water"));
               okDown = (!CultimaPanel.allTerrain.get(Math.abs(currMap[CultimaPanel.equalizeWorldRow(row+1)][col])).getName().toLowerCase().contains("water"));
            }        
            if((!LocationBuilder.isImpassable(currMap, row, col) && !LocationBuilder.isBreakable(currMap, row, col) && !current.contains("water") && okLeft && okRight && okUp && okDown))
               locs.add(new Coord(row, col));            
         }
      }
      if(locs.size()>0)  
         return getRandomFrom(locs);
      return null;
   }

//finds location for a monster to spawn in the water
   public static Coord findWaterSpawn(byte monsterIndex)
   {
      ArrayList<Coord> locs = waterInView(monsterIndex);
      if(locs.size()>0)  
         return getRandomFrom(locs);
      return null;
   }
        
   //finds location for a monster to spawn in a terrain tile that contains "type", i.e. bog, water, sand, lava
   public static Coord findSpawnOfType(String type)
   {
      type = type.toLowerCase();
      ArrayList<Coord> locs = new ArrayList<Coord>();
      byte[][]currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      for(int r=CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2; r++)
         for(int c=CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2; c++)
            if(CultimaPanel.player.getMapIndex() != 0)
            {
               if(r>=0 && r<currMap.length && c>=0 && c<currMap[0].length)
               {
                  String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(current.contains(type))
                     locs.add(new Coord(r, c));
               }
            }
            else
            {
               int row = CultimaPanel.equalizeWorldRow(r);
               int col = CultimaPanel.equalizeWorldCol(c);
               String current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
               if(current.contains(type))
                  locs.add(new Coord(row, col));
            }
      if(locs.size()>0)  
         return getRandomFrom(locs);
      return null;
   }
   
   //looks if there are flowers in view for a unicorn to spawn
   public static boolean flowersInView()
   {
      byte[][]currMap = CultimaPanel.map.get(CultimaPanel.player.getMapIndex());
      for(int r=CultimaPanel.player.getRow()-CultimaPanel.SCREEN_SIZE/2; r<=CultimaPanel.player.getRow()+CultimaPanel.SCREEN_SIZE/2; r++)
         for(int c=CultimaPanel.player.getCol()-CultimaPanel.SCREEN_SIZE/2; c<=CultimaPanel.player.getCol()+CultimaPanel.SCREEN_SIZE/2; c++)
            if(CultimaPanel.player.getMapIndex() != 0)
            {
               if(r>=0 && r<currMap.length && c>=0 && c<currMap[0].length)
               {
                  String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(current.contains("flower"))
                     return true;
               }
            }
            else
            {
               int row = CultimaPanel.equalizeWorldRow(r);
               int col = CultimaPanel.equalizeWorldCol(c);
               String current = CultimaPanel.allTerrain.get(Math.abs(currMap[row][col])).getName().toLowerCase();
               if(current.contains("flower"))
                  return true;
            }
      return false;
   }
   
   public static void setShoppeInventory()
   {
      boolean goodWeather = (CultimaPanel.weather <= 1);
      boolean evenDay = (CultimaPanel.days % 2 == 0);
      boolean holiday = (CultimaPanel.days % 100 == 50);
      CultimaPanel.armoryInventory.clear();
      for(Item it: CultimaPanel.allArmoryInventory)
      {
         String name = it.getName().toLowerCase();
         if(name.contains("mirror") && (evenDay || !goodWeather) && !holiday)
         {}             //only sell mirrored-axe on sunny odd days
         else if(name.contains("frostdagger") && (CultimaPanel.days != 1 || goodWeather) && !holiday)
         {} 
         else if((name.contains("blessed-armor") || name.contains("blessed-sword")) && CultimaPanel.days % 10 != 3 && !holiday)
         {}             //only offer Blessed-armor on days that end with 3
         else if((name.contains("flamebow") || name.contains("magmadagger")) && (CultimaPanel.days % 10 != 5 || !goodWeather) && !holiday)
         {}             //only offer Flame-longbow on days that end with 5
         else if((name.contains("exotic-plate") || name.contains("exotic-hammer") || name.contains("poison-boltcaster")) && CultimaPanel.days % 10 != 7 && !holiday)
         {}             //only offer Exotic-plate on days that end with 7
         else if((name.contains("souldagger") || name.contains("banedagger")) && (CultimaPanel.days != 9 || goodWeather) && !holiday)
         {}
         else if(name.contains("lute-o-destiny") && CultimaPanel.days != 11 && !holiday)
         {}             //only offer Nigel's Lute on the 11th day
         else CultimaPanel.armoryInventory.add(it);
      }
   
      CultimaPanel.magicInventory.clear();
      for(Item it: CultimaPanel.allMagicInventory)
      {
         String name = it.getName().toLowerCase();
         if(name.contains("tempest") && (evenDay || goodWeather) && !holiday)
         {}             //only teach tempest spell on a rainy odd day
         else if(name.contains("fireshield") && (evenDay || !goodWeather) && !holiday)
         {}             //only teach fireshield spell on a dry odd day
         else if(name.contains("animate-stone") && (CultimaPanel.days % 10 != 7 || !goodWeather) && !holiday)
         {}             //only teach animate-stone spell on a dry 7th day
         else if(name.contains("floretbox") && (evenDay || goodWeather) && !holiday)
         {}             //only offer floretbox item on a rainy odd day
         else if(name.contains("holdall") && CultimaPanel.days % 10 != 1 && !holiday)
         {}             //only offer holdall on days that end with 1
         else if(name.contains("raise-earth") && (CultimaPanel.days % 10 != 1 || goodWeather) && !holiday)
         {}             //only offer raise-earth spell on the 1st rainy day of 10
         else if(name.contains("charmring") && CultimaPanel.days % 10 != 3 && !holiday)
         {}             //only offer charmring on days that end with 3
         else if(name.contains("raise-water") && (CultimaPanel.days % 10 != 3 || goodWeather) && !holiday)
         {}             //only offer raise-water spell on the 3rd rainy day of 10
         else if(name.contains("focushelm") && CultimaPanel.days % 10 != 5 && !holiday)
         {}             //only offer focushelm on days that end with 5
         else if(name.contains("swiftboots") && (CultimaPanel.days % 10 != 5 || !goodWeather) && !holiday)
         {}             //only offer swiftboots on days that end with 5 that are sunny
         else if(name.contains("pentangle") && CultimaPanel.days % 10 != 7 && !holiday)
         {}             //only offer pentangle on days that end with 7
         else if(name.contains("powerbands") && (CultimaPanel.days % 10 != 7 || !goodWeather) && !holiday)
         {}             //only offer powerbands on days that end with 7 that are sunny
         else if(name.contains("mannastone") && CultimaPanel.days % 10 != 9 && !holiday)
         {}             //only offer mannastone on days that end with 9
         else if(name.contains("mindtome") && (CultimaPanel.days % 10 != 9 || !goodWeather) && !holiday)
         {}             //only offer mindtome on days that end with 9 that are sunny
         else if(name.contains("lute-o-destiny") && CultimaPanel.days % 100 != 11 && !holiday)
         {}             //only offer Nigel's Lute on the 11th day
         else if(name.contains("protection") && CultimaPanel.days % 10 != 1 && !holiday)
         {}             //only offer Protection Potion on days that end with 1
         else if(name.contains("speed") && CultimaPanel.days % 10 != 3 && !holiday)
         {}             //only offer Speed Potion on days that end with 3
         else if(name.contains("strength") && CultimaPanel.days % 10 != 5 && !holiday)
         {} 				//only offer Strength Potion on days that end with 5
         else if(name.contains("invisibility") && CultimaPanel.days % 10 != 7 && !holiday)
         {} 				//only offer Invisibility Potion on days that end with 7
         else if(name.contains("alphamind") && CultimaPanel.days % 10 != 9 && !holiday)
         {} 				//only offer AlphaMind Potion on days that end with 9
         else if(name.contains("fireskin") && (evenDay || !goodWeather) && !holiday)
         {}             //only offer FireSkin on a sunny odd day
         else if(name.contains("timestop") && CultimaPanel.days%10 != rollDie(10) && !holiday)
         {} 				//only offer Timestop on 1 in 10 roll
         else if(name.contains("firestorm") && CultimaPanel.days%10 != rollDie(10) && !holiday)
         {} 				//only offer Timestop on 1 in 10 roll
         else if(name.contains("life-pearl") && CultimaPanel.days % 100 != 50 && !holiday)
         {} 				//only offer "life-pearl" on day 50
         else CultimaPanel.magicInventory.add(it);
      }
   }

   public static void setOffTrap()
   {
      String trapType = getRandomFrom(trapTypes);
      if(Math.random() < (CultimaPanel.player.getAgility()/100.0))
      {
         Sound.trapAvoided();
         CultimaPanel.sendMessage(trapType + " trap avoided!");
         CultimaPanel.player.setMissTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
         CultimaPanel.player.specificExperience[Player.AGILITY]++;
      }                           
      else
      {
         Sound.trapSprung();
         int dam = (int)(Math.random() * 20) + 1;
         if(trapType.contains("Poison"))
         {
            dam = (int)(Math.random() * 3) + 1;
            CultimaPanel.player.addEffect("poison");  
         }
         if(trapType.contains("Web"))
         {
            dam = 0;
            CultimaPanel.player.addEffect("web");  
         }
         CultimaPanel.player.damage(dam);
         CultimaPanel.sendMessage(trapType + " trap! " + dam + " damage.");
         CultimaPanel.player.setHitTime(CultimaPanel.numFrames + (CultimaPanel.animDelay*3/2));
      }
   }
   
//pre: send the lower-case name of the terrain of the wall the player is trying to hammer through
//returns true if the player successfully hammers through a wall
//returns false if they don't
   public static boolean hammerWallSuccess(String terr)
   {
      String loc = CultimaPanel.player.getLocationType().toLowerCase();
      boolean hasAxe = (CultimaPanel.player.getImageIndex()==Player.AXE || CultimaPanel.player.getImageIndex()==Player.DUALAXE);
      boolean isWolf = (CultimaPanel.player.getImageIndex()==Player.WOLF);
      boolean cutOutOfGraboid = ((loc.contains("graboid") || loc.contains("beast")) &&  //we can cut our way out of a graboid's innnards
               (CultimaPanel.player.getImageIndex()==Player.AXE || CultimaPanel.player.getImageIndex()==Player.DUALAXE || CultimaPanel.player.getImageIndex()==Player.DAGGER
               || CultimaPanel.player.getImageIndex()==Player.SPEAR || CultimaPanel.player.getImageIndex()==Player.SABER || CultimaPanel.player.getImageIndex()==Player.SWORDSHIELD
               || CultimaPanel.player.getImageIndex()==Player.DUAL || CultimaPanel.player.getImageIndex()==Player.WOLF));
      boolean isMinePost = false;
      if(terr.contains("wood") && loc.contains("mine"))
         isMinePost = true;
      boolean sewerClog = (terr.contains("wood") && loc.contains("sewer")); 
      int smashRoll = 400;
      if(terr.contains("iron_door_locked"))
         smashRoll = 200;
      else if(terr.contains("iron_door"))
         smashRoll = 150;
      else if(terr.contains("wood_door_locked"))
         smashRoll = 100;  
      else if(terr.contains("wood_door"))
         smashRoll = 50;
      else if(terr.contains("window"))
         smashRoll = 25;
      if(hasAxe && terr.contains("wood_door"))
         smashRoll /= 2;    
      if(cutOutOfGraboid)
         smashRoll /= 2;  
      if(Weapon.isLegendaryWeapon(CultimaPanel.player.getWeapon().getName()))
         smashRoll /= 2;      
      boolean goodSmash = (CultimaPanel.player.getImageIndex()==Player.HAMMER && rollDie(smashRoll) < CultimaPanel.player.getMight());
      boolean goodAxe = (hasAxe && rollDie(smashRoll) < CultimaPanel.player.getMight());
      boolean goodSpell = (CultimaPanel.player.getWeapon().getName().contains("Phasewall") && (CultimaPanel.player.getManna() >= CultimaPanel.player.getWeapon().getMannaCost()) && rollDie(50) < CultimaPanel.player.getMind());   
      boolean goodWolf = (isWolf && rollDie(smashRoll) < CultimaPanel.player.getMight());
      boolean goodCutOutOfGraboid = (rollDie(smashRoll) < CultimaPanel.player.getMight());
      boolean clearClog = (CultimaPanel.player.getImageIndex()==Player.HAMMER && sewerClog);
      if(goodSmash || goodSpell || goodAxe || goodWolf || goodCutOutOfGraboid || clearClog)
      {
         if(isMinePost && Math.random() < 0.66) //if we destroyed a minepost, make possibility that rocks fall
         {
            mineCollapse();
         }
         int radius = rollDie(5,10);
         int opacity = rollDie(60,85);
         int plX = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2)  - (radius/2);
         int plY = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2) - (radius/2);   
         CultimaPanel.smoke.add(new SmokePuff(plX, plY, radius, opacity, SmokePuff.dustCloud));
         return true;
      }
      return false;
   }
   
   public static void mineCollapse()
   {
      int mi = CultimaPanel.player.getMapIndex();
      byte[][]currMap = (CultimaPanel.map.get(mi));
      int row = CultimaPanel.player.getRow();
      int col = CultimaPanel.player.getCol();
      boolean crash = false;
      for(int r=row-3; r<=row+3; r++)
         for(int c=col-3; c<=col+3; c++)
         {
            if(r<0 || c<0 || r>=currMap.length || c>=currMap[0].length)
               continue;
            if(LocationBuilder.isImpassable(currMap, r, c))
               continue;
            if(Math.random() < 0.5)
            {
               crash = true;
               if(Math.random() < 0.5)
               {
                  if(r!=row && c!=col)
                     currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_I_B_$Gray_Rock").getValue();
                  if(r==row && c==col)
                     CultimaPanel.player.damage(rollDie(100,300));
               }
               else
               {
                  if(r!=row && c!=col)
                     currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_D_B_$Gray_Rock").getValue();
                  if(r==row && c==col)
                     CultimaPanel.player.damage(rollDie(1,100));
               }
            }   
         }
      makeDustClouds();   
      if(crash)
      {
         CultimaPanel.sendMessage("Mine tunnel collapse!");
         Sound.thunder();
         if(CultimaPanel.player.getBody() > 0)
            Achievement.earnAchievement(Achievement.LIVING_PROOF);
      }     
   }
   
   public static void makeDustClouds()
   {
      int row = CultimaPanel.player.getRow();
      int col = CultimaPanel.player.getCol();
      int radius = rollDie(10,CultimaPanel.SIZE);
      int opacity = rollDie(75,127);
      for(int r=row-3; r<=row+3; r++)
         for(int c=col-3; c<=col+3; c++)
         {
            if(Math.random() < 0.25)
            {
               int plX = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2)  - (radius/2) + ((col - c)*CultimaPanel.SIZE);
               int plY = ((CultimaPanel.SCREEN_SIZE/2)*CultimaPanel.SIZE) + (CultimaPanel.SIZE/2) - (radius/2) + ((row - r)*CultimaPanel.SIZE);   
               CultimaPanel.smoke.add(new SmokePuff(plX, plY, radius, opacity, SmokePuff.dustCloud));
            }
         }
   }
   
   public static boolean lightFireSuccess()
   {
      boolean indoors = false;
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      if(locType.contains("castle") || locType.contains("tower") || locType.contains("dungeon") || locType.contains("cave") || locType.contains("mine") || locType.contains("lair"))
         indoors = true;
      int fireRoll = 10;   
      if(!indoors && CultimaPanel.weather > 0)
         fireRoll += 5;
      if(CultimaPanel.player.getImageIndex() != Player.TORCH)
         fireRoll += 5;
      if(rollDie(fireRoll) <= 5)
         return true;
      return true;
   }

   //sets boolean data fields to let us know what kind of NPCs are in sight
   public static void findNPCsInSight()
   {
      CultimaPanel.humanNPCInSight = false;
      CultimaPanel.awareHumanNPCInSight = false;
      CultimaPanel.brigandInSight = false;
      CultimaPanel.monsterAndCivInView = null;
      CultimaPanel.monsterForGuardTarget = null;
      CultimaPanel.greatestNPCinSight = null;
      String locType = CultimaPanel.player.getLocationType().toLowerCase();
      boolean monsterInView = false;
      boolean civilianInView = false;
      boolean monsterForGuardInView = false;
      CultimaPanel.inRangeToAttack = false;  
      CultimaPanel.inRangeToAttackAlt = false;  
      NPCPlayer monster = null;
      NPCPlayer monsterForGuard = null;
      CultimaPanel.distToClosestAwareNPC = CultimaPanel.SCREEN_SIZE;
      //for finding greatestNPCinSight
      String pArmor = CultimaPanel.player.getArmor().getName().toLowerCase();
      boolean hasKnowing = (CultimaPanel.player.getActiveSpell("Knowing")!=null || pArmor.contains("knowing"));
      NPCPlayer greatest = new NPCPlayer(NPC.BEGGER, 0, 0, 0, "temp", "temp,0,0,0,0,0",false);
      Weapon weapType = CultimaPanel.player.getWeapon();
      String weapName = weapType.getName().toLowerCase();
      Weapon altFire = Weapon.getAltFire(weapType.getName());
      if(weapName.contains("fireshield") || weapName.contains("summon-vortex") || weapName.contains("raise-stone"))
      {
         CultimaPanel.inRangeToAttack = true;
      }
      for(NPCPlayer p:CultimaPanel.AllNPCinSight)
      {
         //find greatestNPCinSight for knowing spell or show info of most important visible player
         if(hasKnowing && CultimaPanel.missionTarget != null)
         {
            greatest = CultimaPanel.missionTarget;
         }
         else
         {
            if(p.hasEffect("control") || NPC.isNatural(p.getCharIndex()) || NPC.isStone(p) || p.getCharIndex()==NPC.BARREL || p.getCharIndex()==NPC.WILLOWISP || (p.getCharIndex()==NPC.MIMIC && !p.hasBeenAttacked()) || (p.getCharIndex() == NPC.GOBLINBARREL && !p.hasBeenAttacked()) || (NPC.isShip(p) && p.hasMet()))    //statue or owned
            {}
            else if(p.compareTo(greatest) > 0)
               greatest = p;
         }
         if(isNPCinRangeToAttack(p, weapType))
         {
            CultimaPanel.inRangeToAttack = true;
         }
         if(altFire != null)
         {
            if(isNPCinRangeToAttack(p, altFire))
            {
               if(Weapon.isTorch(weapType.getName()) && (NPC.isNatural(p.getCharIndex()) || NPC.isStone(p) || p.getCharIndex()==NPC.BARREL))
               {}    //alternate fire for torch is to wave around to intimidate, which shouldn't have an effect on tornados, whirlpools, fires, statues or barrels
               if(weapType.getName().contains("Bow-of-Karna") && CultimaPanel.player.getNumArrows() < 2)
               {}    //legendary bow requires at least 2 arrows for alternate attack
               else
               {
                  CultimaPanel.inRangeToAttackAlt = true;
               }
            }
         }
         if((NPC.isCivilian(p.getCharIndex()) || (NPC.isBrigand(p.getCharIndex()) && !TerrainBuilder.habitablePlace(locType) )) && !p.isStatue() && !p.isNonPlayer())
            CultimaPanel.humanNPCInSight = true;
         if((NPC.isCivilian(p.getCharIndex()) || NPC.isBrigand(p.getCharIndex())) && !p.isUnaware() && !p.isStatue() && !p.isNonPlayer())
            CultimaPanel.awareHumanNPCInSight = true;  
         if(NPC.isBrigand(p.getCharIndex()) && !p.isUnaware() && !p.isStatue() && !p.isNonPlayer())
            CultimaPanel.brigandInSight = true;
         if(NPC.isCivilian(p.getCharIndex()) && !p.isStatue() && !p.isNonPlayer())
            civilianInView = true;
         if((!NPC.isCivilian(p.getCharIndex()) || (p.isPossessed() && p.hasAttacked())) 
         && !NPC.isTameAnimal(p.getCharIndex()) && p.getCharIndex()!=NPC.SHARK && !p.isStatue() && !p.isNonPlayer()
         && !p.getName().contains("Skara Brae") && p.getCharIndex()!=NPC.BARREL && !p.isOurDog() && !p.isCaged())
         {
            monsterInView = true;
            monster = p;
         }
         if((!NPC.isCivilian(p.getCharIndex()) || (p.isPossessed() && p.hasAttacked())) 
         && !NPC.isTameAnimal(p.getCharIndex()) && p.getCharIndex()!=NPC.SHARK && !p.isStatue() && !p.isNonPlayer()
         && !p.getName().contains("Skara Brae")  && p.getCharIndex()!=NPC.BARREL && !NPC.isNatural(p.getCharIndex()) && !p.isOurDog() && !p.isCaged())
         {
            monsterForGuardInView = true;
            monsterForGuard = p;
         }
         if((NPC.isCivilian(p.getCharIndex()) && !p.isUnaware()) || (!TerrainBuilder.habitablePlace(locType) && NPC.isBrigand(p.getCharIndex()) && !p.isUnaware()) )
         {
            int currDist = (int)(Display.findDistance(CultimaPanel.player.getRow(), CultimaPanel.player.getCol(), p.getRow(), p.getCol()));
            if(currDist < CultimaPanel.distToClosestAwareNPC)
               CultimaPanel.distToClosestAwareNPC = currDist;
         }
      }
      if(greatest.isNonPlayer())
         CultimaPanel.greatestNPCinSight = null;   
      else 
         CultimaPanel.greatestNPCinSight = greatest;
      if((locType.contains("castle") || locType.contains("tower")) && !CultimaPanel.guardsOnAlert && CultimaPanel.player.threateningWeaponDrawn())
      {  //check to see if we are in the presence of royalty in their castle and have a weapon drawn
         for(NPCPlayer p:CultimaPanel.NPCinSight)
         {
            if(p.getCharIndex()==NPC.KING)
            {
               CultimaPanel.player.addReputation(-3);
               CultimaPanel.sendMessage("~Save the king!");
               alertGuards();
            }
         }
      }
      if(monsterInView && civilianInView)
         CultimaPanel.monsterAndCivInView =  monster;
      if(monsterForGuardInView && civilianInView)
         CultimaPanel.monsterForGuardTarget = monsterForGuard;
   }

      //returns true if NPCs in current location contain living humans
   public static boolean humanNPCInLocation(int mi)
   {
      ArrayList<NPCPlayer> civsInLoc = (CultimaPanel.civilians.get(mi));
      for(int i=civsInLoc.size()-1; i>=0; i--)
      {
         NPCPlayer p = civsInLoc.get(i);
         if(NPC.isCivilian(p))
            return true;
      }
      return false;
   }

//returns true if the player gets caught lighting a fire in a habitable place
//returns false if they get away with it
   public static boolean caughtStartingFire()
   {
      boolean timestop = (CultimaPanel.player.getActiveSpell("Timestop")!=null);
      if(timestop)
      {
         return false;
      }
      int mi = CultimaPanel.player.getMapIndex();
      boolean zombieAbout = CultimaPanel.zombieAbout;
      boolean awareHumanNPCInSight = CultimaPanel.awareHumanNPCInSight;
      String loc = CultimaPanel.player.getLocationType();
      int weatherBonus = (int)(CultimaPanel.weather/1.5);                  //thunder makes it easier to get away with hammering
      int numNPCsInLoc = CultimaPanel.numNPCsInLoc;
      int distractionBonus = 0;
      boolean monsterAttack = (CultimaPanel.monsterAndCivInView != null);
      if(monsterAttack)
         distractionBonus = 10;
      if(CultimaPanel.player.getImageIndex()!=Player.TORCH && !CultimaPanel.player.getWeapon().getEffect().toLowerCase().contains("fire"))
         return false;
      if(zombieAbout)                                                      //not getting caught if everyone is a zombie
         return false;
      if((!TerrainBuilder.habitablePlace(loc) && !loc.contains("arena") && !loc.contains("temple")) || !awareHumanNPCInSight)        //nobody to report us hammering
         return false;
      if(numNPCsInLoc>0 && rollDie(10) < 5-weatherBonus-distractionBonus)
         return true;
      return false;
   }
   
//returns true if the player gets caught hammering a wall in a habitable place
//returns false if they get away with it
   public static boolean caughtHammeringWall()
   {
      boolean timestop = (CultimaPanel.player.getActiveSpell("Timestop")!=null);
      if(timestop)
      {
         return false;
      }
      int mi = CultimaPanel.player.getMapIndex();
      boolean zombieAbout = CultimaPanel.zombieAbout;
      boolean awareHumanNPCInSight = CultimaPanel.awareHumanNPCInSight;
      String loc = CultimaPanel.player.getLocationType();
      int weatherBonus = (int)(CultimaPanel.weather/1.5);                     //thunder makes it easier to get away with hammering
      int distractionBonus = 0;
      //see if we are next to a purple floor -that is our house and we can hammer our own house
      if(nextToOurHouse(CultimaPanel.player.getRow(), CultimaPanel.player.getCol()))
         return false;
      boolean monsterAttack = (CultimaPanel.monsterAndCivInView != null);
      if(monsterAttack)
         distractionBonus = 10;
      int numNPCsInLoc = CultimaPanel.numNPCsInLoc;
      if(CultimaPanel.player.getImageIndex()!=Player.HAMMER || zombieAbout)   //not getting caught if everyone is a zombie
         return false;
      if((!TerrainBuilder.habitablePlace(loc) && !loc.contains("arena") && !loc.contains("temple")) || !awareHumanNPCInSight)          
         return false;                                                        //nobody to report us hammering
      if(numNPCsInLoc>0 && rollDie(10) < 5-weatherBonus-distractionBonus)
         return true;
      return false;
   }
   
//returns true if the player gets caught stealing a chest in a habitable place
//returns false if they get away with it
   public static boolean caughtStealingChest()
   {
      boolean timestop = (CultimaPanel.player.getActiveSpell("Timestop")!=null);
      if(timestop)
      {
         return false;
      }
      int mi = CultimaPanel.player.getMapIndex();
      boolean zombieAbout = CultimaPanel.zombieAbout;
      boolean awareHumanNPCInSight = CultimaPanel.awareHumanNPCInSight;
      String loc = CultimaPanel.player.getLocationType();
      int distractionBonus = 0;
      boolean monsterAttack = (CultimaPanel.monsterAndCivInView != null);
      if(monsterAttack && TerrainBuilder.habitablePlace(loc))
         distractionBonus = 10;
      int weatherBonus = CultimaPanel.weather;                     //thunder makes it easier to get away with stealing
      if(!TerrainBuilder.habitablePlace(loc))
         weatherBonus = 0;
      if(CultimaPanel.player.isInvisible() || zombieAbout)         //not getting caught if invisible or everyone is a zombie
         return false;
      if(!awareHumanNPCInSight)        //nobody to report us stealing
         return false;
      int dieRoll = rollDie(62-weatherBonus-distractionBonus);
      int distToNPC =  CultimaPanel.distToClosestAwareNPC;
      if(dieRoll - distToNPC > CultimaPanel.player.getAgility())
         return true;
      return false;
   }
   
   //returns true if the player gets caught stealing a ship in a habitable place
   //returns false if they get away with it
   public static boolean caughtStealingShip()
   {
      boolean timestop = (CultimaPanel.player.getActiveSpell("Timestop")!=null);
      if(timestop)
      {
         return false;
      }
      int mi = CultimaPanel.player.getMapIndex();
      boolean zombieAbout = CultimaPanel.zombieAbout;
      boolean awareHumanNPCInSight = CultimaPanel.awareHumanNPCInSight;
      String loc = CultimaPanel.player.getLocationType();
      int distractionBonus = 0;
      boolean monsterAttack = (CultimaPanel.monsterAndCivInView != null);
      if(monsterAttack && TerrainBuilder.habitablePlace(loc))
         distractionBonus = 10;
      int weatherBonus = CultimaPanel.weather;                     //thunder makes it easier to get away with stealing
      if(!TerrainBuilder.habitablePlace(loc))
         weatherBonus = 0;
      if(CultimaPanel.player.isInvisible() || zombieAbout)         //not getting caught if invisible or everyone is a zombie
         return false;
      if(!awareHumanNPCInSight)        //nobody to report us stealing
         return false;
      return true;
   }
   
   //returns true if the player gets caught pickpocketing from NPC p
   //returns false if they get away with it
   public static boolean caughtPickpocketing(NPCPlayer p)
   {
      boolean timestop = (CultimaPanel.player.getActiveSpell("Timestop")!=null);
      if(timestop)
      {
         return false;
      }
      String loc = CultimaPanel.player.getLocationType().toLowerCase();
      int mi = CultimaPanel.player.getMapIndex();
      byte[][]currMap = (CultimaPanel.map.get(mi));
      boolean zombieAbout = CultimaPanel.zombieAbout;
      if(CultimaPanel.player.isInvisible() || zombieAbout || p.isUnaware())         //not getting caught if invisible or everyone is a zombie
         return false;
      int distractionBonus = 0;
      boolean monsterAttack = (CultimaPanel.monsterAndCivInView != null);
      if(monsterAttack)
         distractionBonus = 10;
      String NPCterr = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][p.getCol()])).getName().toLowerCase();   
      if(NPCterr.contains("bed"))
         distractionBonus += 10;   
      int weatherBonus = CultimaPanel.weather;                     //thunder makes it easier to get away with stealing 
      int dieRoll = rollDie(CultimaPanel.player.getAgility()+p.getAgility());
      if(dieRoll > CultimaPanel.player.getAgility() + weatherBonus + distractionBonus)
         return true;
      if(p.getCharIndex()==NPC.JESTER)
      {
         if(p.getMoveType()==NPC.RUN && (loc.contains("port") || loc.contains("city") || loc.contains("fortress") || loc.contains("village")))
            Achievement.earnAchievement(Achievement.RETURN_TO_SENDER);
      }   
      return false;
   }
    
    //fills CultimaPanel datafields for onACorpse (for looting) and onAnyCorpse (for learning spells in Player)   
   public static void checkIfOnACorpse()
   {
      CultimaPanel.onACorpse = null;
      CultimaPanel.onAnyCorpse = null;
      if(CultimaPanel.player.isWolfForm())
         return;
      for(Corpse cp: CultimaPanel.corpses.get(CultimaPanel.player.getMapIndex()))
         if(cp.getRow()==CultimaPanel.player.getRow() && cp.getCol()==CultimaPanel.player.getCol())
         {
            if(cp.getArmor()!=null || cp.getWeapon()!=null)
               CultimaPanel.onACorpse = cp;
            CultimaPanel.onAnyCorpse = cp;
         }
   }

//see if a Demon visits us while sleeping to play Swine
   public static void demonVisit()
   {
      int DEMON_VISIT_PROB = Math.max(5, Math.min(25, Player.stats[Player.SWINE_GAMES_WON]));
      int mi = CultimaPanel.player.getMapIndex();
      if(mi==0 || Player.stats[Player.SWINE_GAMES_WON]==0 || !CultimaPanel.player.isCamping())
         return;
      if(CultimaPanel.time <= 22)      //demons only appear at midnight when it is stormy
         return;
      if(CultimaPanel.weather < 2)
      {
         CultimaPanel.time = 0;
         CultimaPanel.isNight = (CultimaPanel.time >=20 || CultimaPanel.time <= 6);
         CultimaPanel.days++;
         if(CultimaPanel.days >= 60)
            Achievement.earnAchievement(Achievement.ONE_ROUND_DOWN);  
         return;
      }  
      int roll = rollDie(100); 
      if(roll >= DEMON_VISIT_PROB)
      {
         CultimaPanel.time = 0;
         CultimaPanel.isNight = (CultimaPanel.time >=20 || CultimaPanel.time <= 6);
         CultimaPanel.days++;
         if(CultimaPanel.days >= 60)
            Achievement.earnAchievement(Achievement.ONE_ROUND_DOWN);  
         return; 
      }
      //make sure there already isn't a demon there
      int row = CultimaPanel.player.getRow();
      int col = CultimaPanel.player.getCol();
      for(int r=row-1; r<=row+1; r++)
         for(int c=col-1; c<=col+1; c++)
         {
            NPCPlayer temp = getNPCAt(r, c, mi);
            if(temp != null && temp.getCharIndex()==NPC.DEMON)
               return;
         }
      String locType = CultimaPanel.player.getLocationType();
      byte[][] currMap = CultimaPanel.map.get(mi);
      ArrayList<Coord> locs = new ArrayList<Coord>();
      for(int r=row-1; r<=row+1; r++)
         for(int c=col-1; c<=col+1; c++)
            if((LocationBuilder.canSpawnOn(currMap, r, c) || TerrainBuilder.isCampableSpot(r, c, mi)) && !NPCAt(r, c, mi))
               locs.add(new Coord(r,c));
      if(locs.size() > 0)
      {
         Coord spawnLoc = getRandomFrom(locs);
         int monsterRow = spawnLoc.getRow();
         int monsterCol = spawnLoc.getCol();
         CultimaPanel.flashColor = java.awt.Color.red;
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE;
         CultimaPanel.player.setCamping(false);
         NPCPlayer monster = new NPCPlayer(NPC.DEMON, monsterRow, monsterCol, mi, locType);
         monster.setIsSwinePlayer(true);
         monster.setMoveType(NPC.STILL);
         if(Math.random() < 0.25)
            monster.addItem(Item.getDemonsCube());
         (CultimaPanel.civilians.get(mi)).add(monster);
      }
      CultimaPanel.time = 0;
      CultimaPanel.isNight = (CultimaPanel.time >=20 || CultimaPanel.time <= 6);
      CultimaPanel.days++;
      if(CultimaPanel.days >= 60)
         Achievement.earnAchievement(Achievement.ONE_ROUND_DOWN);  
   }

//see if a jester tries stealing from us while sleeping
   public static void jesterThief()
   {
      int mi = CultimaPanel.player.getMapIndex();
      String locType = CultimaPanel.player.getLocationType();
      if(mi==0 || !CultimaPanel.player.isCamping() || !TerrainBuilder.habitablePlace(locType))
         return;
      if((int)(CultimaPanel.time) != 1)      //thief only appear at midnight
         return;  
   
      NPCPlayer thief = null;
      for(NPCPlayer p:CultimaPanel.civilians.get(mi))
      {
         if(p.getCharIndex()==NPC.JESTER && p.getReputation() < -10)
         {
            thief = p;
            break;
         }
      }
      if(thief==null)               //no bad jesters in this town
         return;
      int THIEF_VISIT_PROB = Math.max(5, Math.min(25, Player.stats[Player.ITEMS_STOLEN]));
      int roll = rollDie(100); 
      if(roll >= THIEF_VISIT_PROB)
      {
         CultimaPanel.time = 2;
         CultimaPanel.isNight = (CultimaPanel.time >=20 || CultimaPanel.time <= 6);
         return; 
      }
      //make sure there already isn't a thief there
      int row = CultimaPanel.player.getRow();
      int col = CultimaPanel.player.getCol();
      for(int r=row-3; r<=row+3; r++)
         for(int c=col-3; c<=col+3; c++)
         {
            NPCPlayer temp = getNPCAt(r, c, mi);
            if(temp != null && (temp.getCharIndex()==NPC.JESTER || temp.getCharIndex()==NPC.DEMON))
               return;
         }
      byte[][] currMap = CultimaPanel.map.get(mi);
      ArrayList<Coord> locs = new ArrayList<Coord>();
      for(int r=row-3; r<=row+3; r++)
         for(int c=col-3; c<=col+3; c++)
            if(TerrainBuilder.isInsideFloor(r, c, mi) && !NPCAt(r, c, mi))
               locs.add(new Coord(r,c));
      if(locs.size() > 0)
      {
         Coord spawnLoc = getRandomFrom(locs);
         int monsterRow = spawnLoc.getRow();
         int monsterCol = spawnLoc.getCol();
         thief.setRow(monsterRow);
         thief.setCol(monsterCol);
         thief.setMoveType(NPC.CHASE);
      }
      CultimaPanel.time = 2;
      CultimaPanel.isNight = (CultimaPanel.time >=20 || CultimaPanel.time <= 6);
   }

//if you have a trained dog, add it back to the location you are moving in to via Teleporter
   public static void addDogToLocation()
   {
      if(!CultimaPanel.player.getDogName().equals("none"))      //we have a dog
      {
         NPCPlayer dog = getDog();
         if(dog!=null && dog.getBody() <= 0)
         {
            removeDogFromLocation();
            CultimaPanel.player.setDogBasicInfo("none");
            return;
         }
         int row = CultimaPanel.player.getRow();
         int col = CultimaPanel.player.getCol();
         int mapIndex = CultimaPanel.player.getMapIndex();
         String locType = CultimaPanel.player.getLocationType();
         dog = new NPCPlayer(row, col, mapIndex, locType, CultimaPanel.player.getDogBasicInfo(),true);
         if(dog.getBody() > 0)
         {
            dog.setHasMet(true);
            dog.setMoveType(NPC.CHASE);
            CultimaPanel.worldMonsters.add(dog);      
         }
         else
         {
            removeDogFromLocation();
            CultimaPanel.player.setDogBasicInfo("none");
         }
      }
   } 
   
//if you have a trained dog, add it back to the location you are moving in to via Teleporter
//shifted one space in the direction of DIR, for exiting ships
   public static void addDogToLocation(String dir)
   {
      if(!CultimaPanel.player.getDogName().equals("none"))      //we have a dog
      {
         NPCPlayer dog = getDog();
         if(dog!=null && dog.getBody() <= 0)
         {
            removeDogFromLocation();
            CultimaPanel.player.setDogBasicInfo("none");
            return;
         }
         int row = CultimaPanel.player.getRow();
         int col = CultimaPanel.player.getCol();
         if(dir.equals("North"))
            row--;
         else if(dir.equals("South"))
            row++;
         else if(dir.equals("East"))
            col++;
         else if(dir.equals("West"))
            col--;
         int mapIndex = CultimaPanel.player.getMapIndex();
         String locType = CultimaPanel.player.getLocationType();
         dog = new NPCPlayer(row, col, mapIndex, locType, CultimaPanel.player.getDogBasicInfo(),true);
      
         if(dog.getBody() > 0)
         {
            dog.setHasMet(true);
            dog.setMoveType(NPC.CHASE);
         
            NPCPlayer worldDog = getDog();
            if(worldDog == null)
               CultimaPanel.worldMonsters.add(dog);      
            else
            {
               worldDog.setRow(row);
               worldDog.setCol(col);
               worldDog.setMapIndex(mapIndex);
               worldDog.setLocationType(locType);
            }
         }
         else
         {
            removeDogFromLocation();
            CultimaPanel.player.setDogBasicInfo("none");
         }
      }
   }
   
   //if you have a trained dog, remove it from the location (for getting onto a ship)
   public static void removeDogFromLocation()
   {
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(i);
         if(p.isOurDog() && !p.isStatue() && !p.isNonPlayer())
         {
            CultimaPanel.worldMonsters.remove(i);
            return;
         }
      }
      int mapIndex = CultimaPanel.player.getMapIndex();
      if(mapIndex > 0 && mapIndex < CultimaPanel.civilians.size())
      {
         ArrayList<NPCPlayer> civs = CultimaPanel.civilians.get(mapIndex);
         for(int i=civs.size()-1; i>=0; i--)
         {
            NPCPlayer p = civs.get(i);
            if(p.isOurDog() && !p.isStatue() && !p.isNonPlayer())
            {
               civs.remove(i);
               return;
            }
         }
      }
   }

    //if you are escorting an npc, add it back to the location you are moving in to via Teleporter
   public static void addNpcToLocation()
   {
      if(!CultimaPanel.player.getNpcName().equals("none"))  
      {
         NPCPlayer npc = getNpc();
         if(npc!=null && npc.getBody() <= 0)
         {
            removeNpcFromLocation();
            CultimaPanel.player.setNpcBasicInfo("none");
            return;
         }
         String npcInfo = CultimaPanel.player.getNpcBasicInfo();
         int row = CultimaPanel.player.getRow();
         int col = CultimaPanel.player.getCol();
         int mapIndex = CultimaPanel.player.getMapIndex();
         String locType = CultimaPanel.player.getLocationType();
         npc = new NPCPlayer(row, col, mapIndex, locType, CultimaPanel.player.getNpcBasicInfo(),true);
         if(npc.getBody() > 0)
         {
            npc.setHasMet(true);
            if(locType.contains("battlefield"))
            {
               if(npc.getCharIndex() == NPC.CHILD)
                  npc.setMoveTypeTemp(NPC.ROAM);
               else
                  npc.setMoveTypeTemp(NPC.STILL);
            }
            else
            {
               npc.setMoveType(NPC.CHASE);
            }
            CultimaPanel.worldMonsters.add(npc);      
         }
         else
         {
            removeNpcFromLocation();
            CultimaPanel.player.setNpcBasicInfo("none");
         }
      }
   } 
   
//if you are escorting an npc, add it back to the location you are moving in to via Teleporter
//shifted one space in the direction of DIR, for exiting ships
   public static void addNpcToLocation(String dir)
   {
      if(!CultimaPanel.player.getNpcName().equals("none"))  
      {
         NPCPlayer npc = getNpc();
         if(npc!=null && npc.getBody() <= 0)
         {
            removeNpcFromLocation();
            CultimaPanel.player.setNpcBasicInfo("none");
            return;
         }
         int row = CultimaPanel.player.getRow();
         int col = CultimaPanel.player.getCol();
         if(dir.equals("North"))
            row--;
         else if(dir.equals("South"))
            row++;
         else if(dir.equals("East"))
            col++;
         else if(dir.equals("West"))
            col--;
         int mapIndex = CultimaPanel.player.getMapIndex();
         String locType = CultimaPanel.player.getLocationType();
         npc = new NPCPlayer(row, col, mapIndex, locType, CultimaPanel.player.getNpcBasicInfo(), true);
         if(npc.getBody() > 0)
         {
            npc.setHasMet(true);
            npc.setMoveType(NPC.CHASE);
         
            NPCPlayer worldNpc = getNpc();
            if(worldNpc == null)
               CultimaPanel.worldMonsters.add(npc);      
            else
            {
               worldNpc.setRow(row);
               worldNpc.setCol(col);
               worldNpc.setMapIndex(mapIndex);
               worldNpc.setLocationType(locType);
            }
         }
         else
         {
            removeNpcFromLocation();
            CultimaPanel.player.setNpcBasicInfo("none");
         }
      }
   }   

   //if you are escorting an npc, remove it from the location (for getting onto a ship)
   public static void removeNpcFromLocation()
   {
      for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
      {
         NPCPlayer p = CultimaPanel.worldMonsters.get(i);
         if(p.isEscortedNpc() && !p.isStatue() && !p.isNonPlayer())
         {
            CultimaPanel.worldMonsters.remove(i);
            return;
         }
      }
   }  
   
   public static void addMinionsToLocation(LinkedList<NPCPlayer> minions)
   {
      if(minions != null && minions.size() > 0)
      {
         int row = CultimaPanel.player.getRow();
         int col = CultimaPanel.player.getCol();
         int mapIndex = CultimaPanel.player.getMapIndex();
         String locType = CultimaPanel.player.getLocationType();
      
         for(NPCPlayer minion : minions)
         {
            minion.setRow(row);
            minion.setCol(col);
            minion.setMapIndex(mapIndex);
            minion.setLocationType(locType);
            CultimaPanel.worldMonsters.add(minion);
         } 
      }
   }
   
 //finds an escort mission for an npc with a certain name
   public static Mission getEscortMissionWith(NPCPlayer p)
   {
      if(p.isEscortedNpc())
      {
         for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
         {
            Mission m = CultimaPanel.missionStack.get(i);
            if(m.getType().equals("Escort"))
            {
               return m;
            }
         }
      }
      return null;
   }
   
   //if we are escorting an npc in the world and get too far away, they get lost and we lose the mission
   public static double checkDistanceToEscortee(NPCPlayer p)
   {
      double dist = -1;
      if(p == null || !p.isEscortedNpc())
         return dist;
      if(CultimaPanel.player.getMapIndex() == 0)
      {
         dist = Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(),  CultimaPanel.player.getCol());
         if(dist > ((CultimaPanel.SCREEN_SIZE/2)+1))
         {
            p.escorteeIsLost();
            removeNPCat(p.getRow(), p.getCol(),p.getMapIndex());
         }
      }
      cleanEscortMissions();
      return dist;
   }   
   
   public static void cleanEscortMissions()
   {
      if(!CultimaPanel.player.getNpcBasicInfo().equals("none"))
      {  //see if there is no Escort mission, and if so, remove the npc.  This can happen if we die or reset the game inbetween the escortee getting lost and the mission info getting updated
         boolean escortMission = false;
         for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
         {
            Mission m = CultimaPanel.missionStack.get(i);
            if(m.getType().equals("Escort") || m.getMissionType()==Mission.SEWER_LOST)
            {
               escortMission = true;
               break;
            }
         }
         if(!escortMission)
         {
            CultimaPanel.player.setNpcBasicInfo("none");
         }
      }
   }

   //return true is we are close enought to a wisp to trigger an ambush
   public static boolean closeToWisp()
   {
      if(CultimaPanel.player.getMapIndex() != 0)   //wisps are only in the greater world at mapIndex 0
         return false;
      for(NPCPlayer p:CultimaPanel.worldMonsters)
      {
         if(p.getCharIndex()==NPC.WILLOWISP)
         {
            double distToWisp = (Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol()));
            if(distToWisp  <= 4 )
            {
               CultimaPanel.sendMessage("Tis a trap!");
               Sound.thunder();
               Utilities.removeNPCat(p.getRow(), p.getCol(), p.getMapIndex());
               return true;
            }
         }
      }
      return false;
   }

//if we have a dog watching over the camp, it wakes us up if a monster gets within 5 cells
   public static void dogOnWatch()
   {
      if(!CultimaPanel.player.getDogName().equals("none"))
      {
         for(NPCPlayer p:CultimaPanel.worldMonsters)
         {
            if(NPC.isTameAnimal(p) || (NPC.isCivilian(p) && p.getMoveType()!=NPC.CHASE) || NPC.isShip(p) || p.isStatue() || p.isNonPlayer() || p.hasEffect("control") || p.isOurDog() || p.isEscortedNpc())
               continue;
            if(p.getMapIndex() == CultimaPanel.player.getMapIndex())
            {
               double dist = Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
               if(dist <= 5)
               {
                  CultimaPanel.player.setCamping(false);
                  CultimaPanel.sendMessage("~Woof! Woof! Woof!");
               }
            }
         }
         for(NPCPlayer p:CultimaPanel.civilians.get(CultimaPanel.player.getMapIndex()))
         {
            if(p.isOurDog() || NPC.isHorse(p) || p.isStatue()  || p.isNonPlayer() || p.getMoveType()==NPC.STILL || p.isEscortedNpc())
               continue;
            double dist = Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(), CultimaPanel.player.getCol());
            if(dist <= 5)
            {
               CultimaPanel.player.setCamping(false);
               CultimaPanel.sendMessage("~Woof! Woof! Woof!");
            }
         }
      }
   }

//return the distance between targ and your dog if you have one
   public static double distToDog(NPCPlayer targ)
   {
      if(!CultimaPanel.player.getDogName().equals("none"))
      {
         for(NPCPlayer p:CultimaPanel.worldMonsters)
         {
            if(p.isOurDog())
            {
               return Display.findDistance(p.getRow(), p.getCol(), targ.getRow(), targ.getCol());
            }
         }
      }
      return Double.MAX_VALUE;
   }
   
   //return the distance between player and your dog if you have one
   public static double distToDog()
   {
      if(!CultimaPanel.player.getDogName().equals("none"))
      {
         for(NPCPlayer p:CultimaPanel.worldMonsters)
         {
            if(p.isOurDog())
            {
               return Display.findDistance(p.getRow(), p.getCol(), CultimaPanel.player.getRow(),  CultimaPanel.player.getCol());
            }
         }
      }
      return Double.MAX_VALUE;
   }

//returns the dog if you have one
   public static NPCPlayer getDog()
   {
      if(!CultimaPanel.player.getDogName().equals("none"))
         return CultimaPanel.ourDog;
      return null;
   }
   
   //returns the npc if you are escorting one
   public static NPCPlayer getNpc()
   {
      if(!CultimaPanel.player.getNpcName().equals("none"))
         return CultimaPanel.ourNpc;
      return null;
   }

   //when we move into a city, we need to know which sides have a street that allows us to enter from that side
   //that way we can spawn the player to be on the side of the city they enter from based on the direction of their last move when they enter the city
   //but only if there is an entrance there, so this returns an array of 4 booleans as to if there is a street on that side of the city
   //[NORTH, EAST, SOUTH, WEST]: the elements of the array will be true if there is a city street on that side.  Called from CultimaPanel
   public static boolean[] findCityStreetEntrance(byte[][] currMap)
   {
      boolean [] streetOnSide = new boolean[4];
      int mapDimensionRow = currMap.length, mapDimensionCol = currMap[0].length;   
      int r = 0;                 //top row, middle column
      int c = mapDimensionCol/2;
      String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
      if(current.contains("brick_floor"))
         streetOnSide[LocationBuilder.NORTH] = true;
           
      //enter in last col from the right side
      r = mapDimensionRow/2;
      c = mapDimensionCol - 1;
      current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
      if(current.contains("brick_floor"))
         streetOnSide[LocationBuilder.EAST] = true;
         
      //enter in last row from the bottom
      r = mapDimensionRow - 1;
      c = mapDimensionCol/2;
      current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
      if(current.contains("brick_floor"))
         streetOnSide[LocationBuilder.SOUTH] = true;
         
      //enter in col 0 from the left side
      r = mapDimensionRow/2;
      c = 0;
      current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
      if(current.contains("brick_floor"))
         streetOnSide[LocationBuilder.WEST] = true;
         
      return streetOnSide;
   }

//returns water locations in currMap
   public static ArrayList<Coord> waterLocsInMap(byte[][] currMap)
   {
      ArrayList<Coord> locs = new ArrayList<Coord>();
      for(int r=0; r<currMap.length; r++)
         for(int c=0; c<currMap[0].length; c++)
         {
            String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(current.contains("water"))
               locs.add(new Coord(r,c));
         }
      return locs;
   }

    //returns water locations in world adjacent to row, col
    //will include diagonals if diagonals is true
   public static ArrayList<Coord> waterLocsAdjacentTo(int row, int col, boolean diagonals)
   {
      byte[][] currMap = CultimaPanel.map.get(0);
      ArrayList<Coord> locs = new ArrayList<Coord>();
      if(diagonals)
      {
         for(int r=row-1; r<=row+1; r++)
            for(int c=col-1; c<=col+1; c++)
            {
               int r1 = CultimaPanel.equalizeWorldRow(r);
               int c1 = CultimaPanel.equalizeWorldCol(c);
               String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r1][c1])).getName().toLowerCase();
               if(current.contains("water"))
                  locs.add(new Coord(r1,c1));
            }
      }
      else
      {
         int r1 = CultimaPanel.equalizeWorldRow(row-1);     //UP
         int c1 = CultimaPanel.equalizeWorldCol(col);
         String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r1][c1])).getName().toLowerCase();
         if(current.contains("water"))
            locs.add(new Coord(r1,c1));
         r1 = CultimaPanel.equalizeWorldRow(row+1);         //DOWN
         c1 = CultimaPanel.equalizeWorldCol(col);
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[r1][c1])).getName().toLowerCase();
         if(current.contains("water"))
            locs.add(new Coord(r1,c1));
         r1 = CultimaPanel.equalizeWorldRow(row);         //LEFT
         c1 = CultimaPanel.equalizeWorldCol(col-1);
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[r1][c1])).getName().toLowerCase();
         if(current.contains("water"))
            locs.add(new Coord(r1,c1));
         r1 = CultimaPanel.equalizeWorldRow(row);         //RIGHT
         c1 = CultimaPanel.equalizeWorldCol(col+1);
         current = CultimaPanel.allTerrain.get(Math.abs(currMap[r1][c1])).getName().toLowerCase();
         if(current.contains("water"))
            locs.add(new Coord(r1,c1));
      }
      return locs;
   }
      
   //pre:  monsterType is a valid monster type index, row and col are valid coordinates of the current map, range > 0, p is the monster that is summoning it
   //post: tries to spawn a monster of type monsterType in an open space within range distance from (row, col)
   public static boolean summonMonster(byte monsterType, int row, int col, int range, NPCPlayer p)
   {
      int mapIndex = CultimaPanel.player.getMapIndex();
      String locType = CultimaPanel.player.getLocationType();
      byte[][] currMap = CultimaPanel.map.get(mapIndex);
      if(row < 0 || col < 0 || row >= currMap.length || col >= currMap[0].length || range < 0)
         return false;
      ArrayList<Coord> spawns = new ArrayList<Coord>();
      for(int r=row-range; r<=row+range; r++)
      {
         for(int c=col-range; c<=col+range; c++)
         {
            int newRow = r;
            int newCol = c;
            if(mapIndex == 0)
            {
               newRow = CultimaPanel.equalizeWorldRow(r);
               newCol = CultimaPanel.equalizeWorldCol(c);
            }
            if(newRow < 0 || newCol < 0 || newRow >= currMap.length || newCol >= currMap[0].length || (row==newRow && col==newCol))
               continue;
            if((LocationBuilder.canSpawnOn(currMap, newRow, newCol) || TerrainBuilder.isCampableSpot(newRow, newCol, mapIndex)) && !NPCAt(newRow, newCol, mapIndex))
               spawns.add(new Coord(newRow, newCol));
         }
      }
      if(spawns.size() == 0)
         return false;
      Coord spawn = getRandomFrom(spawns);
      int spawnR = spawn.getRow();
      int spawnC = spawn.getCol();
      NPCPlayer monster = new NPCPlayer(monsterType, spawnR, spawnC, mapIndex, locType);
      if(monsterType==NPC.FIRE)
      {  //make these fire NPCs fire elementals
         if(p!=null && p.getCharIndex()==NPC.MAGMA)
         {
            monster.setNumInfo(Utilities.rollDie(1, 10));
         }
         else if(p!=null && p.getCharIndex()==NPC.FIRE)
         {
            monster.setBody(p.getBody()/2);
            monster.setNumInfo(Math.max(1, p.getNumInfo()/2));
         }
      }
      if(monsterType == -111)    //-111 is the flag that this is a mimic being summoned
      {
         monster.setMight(CultimaPanel.player.getMight());
         monster.setMind(CultimaPanel.player.getMind());
         monster.setAgility(CultimaPanel.player.getAgility());
         monster.setWeapon(CultimaPanel.player.getWeapon());
         monster.setName(CultimaPanel.player.getShortName());
         monster.setReputation(-2000);
      }
      monster.setMoveTypeTemp(NPC.CHASE);
      CultimaPanel.worldMonsters.add(monster);
      return true;
   }
   
   //for KAIJU mission - monster appears at the gate of a habitable location
   public static void spawnKaiju(byte monsterType)
   {
      CultimaPanel.monsterDist = -1;
      int mapIndex = CultimaPanel.player.getMapIndex();
      String locType = CultimaPanel.player.getLocationType();
      boolean isCastle = (locType.contains("castle") || locType.contains("tower"));
      byte[][] currMap = CultimaPanel.map.get(mapIndex);
      ArrayList<Coord> spawnCoords = new ArrayList<Coord>();
      for(int c=-1; c<=1; c++)
      {
         if((isCastle && TerrainBuilder.isGoodCastleKaijuSpawn(1, currMap[0].length/2+c, mapIndex)) ||   
             (!isCastle && TerrainBuilder.isGoodCityKaijuSpawn(1, currMap[0].length/2+c, mapIndex)))
         {  //north-center spawns
            spawnCoords.add(new Coord(1, currMap[0].length/2+c));
         }
      }
      for(int c=-1; c<=1; c++)
      {
         if((isCastle && TerrainBuilder.isGoodCastleKaijuSpawn(currMap.length-2, currMap[0].length/2+c, mapIndex)) ||
            (!isCastle && TerrainBuilder.isGoodCityKaijuSpawn(currMap.length-2, currMap[0].length/2+c, mapIndex)))
         {  //south-center spawns
            spawnCoords.add(new Coord(currMap.length-2, currMap[0].length/2+c));
         }
      }
      for(int r=-1; r<=1; r++)
      {
         if((isCastle && TerrainBuilder.isGoodCastleKaijuSpawn(currMap.length/2+r, 1, mapIndex)) ||
            (!isCastle && TerrainBuilder.isGoodCityKaijuSpawn(currMap.length/2+r, 1, mapIndex)))
         {  //west-center spawns
            spawnCoords.add(new Coord(currMap.length/2+r, 1));
         }
      }
      for(int r=-1; r<=1; r++)
      {
         if((isCastle && TerrainBuilder.isGoodCastleKaijuSpawn(currMap.length/2+r, currMap[0].length-2, mapIndex)) ||
            (!isCastle && TerrainBuilder.isGoodCityKaijuSpawn(currMap.length/2+r, currMap[0].length-2, mapIndex)))
         {  //east-center spawns
            spawnCoords.add(new Coord(currMap.length/2+r, currMap[0].length-2));
         }
      }
      if(spawnCoords.size() > 0)
      {  //remove any kaiju from the world map
         for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
         {
            NPCPlayer p = CultimaPanel.worldMonsters.get(i); 
            if(NPC.isGiantMonsterKing(p))
            {
               CultimaPanel.worldMonsters.remove(i);
            }
         }
         Coord spawn = getRandomFrom(spawnCoords);
         int monsterRow = spawn.getRow();
         int monsterCol = spawn.getCol();
         NPCPlayer monster = new NPCPlayer(monsterType, monsterRow, monsterCol, mapIndex, locType);
         CultimaPanel.worldMonsters.add(monster);
         CultimaPanel.sendMessage("The beast is here! Mission failed!");
         Sound.alertGuards();
      }
      else  //no valid spawn points found
      {
         CultimaPanel.sendMessage("The beast fades away! Twas an illusion of sorcery!");
      }
      for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(m.getMissionType()==Mission.KAIJU)
         {
            CultimaPanel.missionStack.remove(i);
         }     
      }
   
   }
   
   public static void changeToVampire()
   {  //change player into a Vampire
      if(CultimaPanel.player.isVampire() || CultimaPanel.player.isWerewolf())
         return;
      if(CultimaPanel.player.hasItem("pentangle"))
      {
         CultimaPanel.sendMessage("Thy Pentangle shatters");
         CultimaPanel.player.removeItem("pentangle");
         if(Math.random() < 0.5 || CultimaPanel.player.hasItem("pentangle"))
         {
            CultimaPanel.sendMessage("and blocks the Vampyric curse!");
            return;
         }
      }
   
      CultimaPanel.player.setName("~"+ CultimaPanel.player.getName());
      CultimaPanel.player.setAwareness(Math.min(4,  CultimaPanel.player.getAwareness())); 
      if(!CultimaPanel.player.hasSpell("Charm")) 
         CultimaPanel.player.addSpell(Spell.getCharm());
      if(!CultimaPanel.player.hasSpell("Fear")) 
         CultimaPanel.player.addSpell(Spell.getFear()); 
      if(!CultimaPanel.player.hasSpell("Flight")) 
         CultimaPanel.player.addSpell(Spell.getFlight());
      if(!CultimaPanel.player.hasWeapon("Vampyric-bite"))
         CultimaPanel.player.addWeapon(Weapon.getVampyricBite());
      if(CultimaPanel.player.getMannaMax() < Spell.getFlight().getMannaCost())  //need at least as much manna as needed for flight spell
      {
         CultimaPanel.player.setManna(Spell.getFlight().getMannaCost());
      }
      CultimaPanel.player.removeItemAbsolute("blessed-crown");
      CultimaPanel.player.setRations((byte)(1));         
      CultimaPanel.flashColor = Color.red.darker();
      CultimaPanel.flashFrame = CultimaPanel.numFrames;
      CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
      Sound.thunder();
      CultimaPanel.sendMessage("You have the Vampyric curse!");
      CultimaPanel.player.removeEffect("seduced");
      if(CultimaPanel.player.getReputation() > 0)
         CultimaPanel.player.setReputation(-1*CultimaPanel.player.getReputation());
   
      summonMonster(NPC.BAT, CultimaPanel.player.getRow(), CultimaPanel.player.getCol(), 2, null); 
      
      boolean found = false;
      for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(m.getType().equals("Curse"))
         {
            found = true;
            break;
         }
      }
      if(!found)
      {
         CultimaPanel.missionStack.add(new Mission(true, false, false));
         FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
      }
   }
   
   public static void removeVampireCurse()
   {  //change player back into human
      if(!CultimaPanel.player.isVampire())
         return;
      Achievement.earnAchievement(Achievement.MORE_HUMAN_THAN_HUMAN);
      CultimaPanel.flashColor = Color.red.darker();
      CultimaPanel.flashFrame = CultimaPanel.numFrames;
      CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
      Sound.thunder();
      CultimaPanel.player.setName(CultimaPanel.player.getName().replace("~",""));
      CultimaPanel.sendMessage("You are relieved of the Vampyric curse!");
      if(CultimaPanel.player.getReputation() < 0)
         CultimaPanel.player.setReputation(0);
   
      CultimaPanel.player.discardWeapon("Vampyric-bite");
      for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(m.getType().equals("Curse"))
         {
            CultimaPanel.missionStack.remove(i);
            Player.stats[Player.MISSIONS_COMPLETED]++;
            if(Player.stats[Player.MISSIONS_COMPLETED] >= 50)
               Achievement.earnAchievement(Achievement.TASK_MASTER);
            CultimaPanel.player.addExperience(100);
            FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
            break;
         }
      }
   }
   
   public static void changeToWerewolf()
   {  //change player into a Werewolf
      if(CultimaPanel.player.isVampire() || CultimaPanel.player.isWerewolf())
         return;
      CultimaPanel.player.setName(CultimaPanel.player.getName()+"~");
      CultimaPanel.player.removeItemAbsolute("blessed-crown");      
      CultimaPanel.flashColor = new Color(165, 42, 42);
      CultimaPanel.flashFrame = CultimaPanel.numFrames;
      CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
      Sound.thunder();
      if(CultimaPanel.player.hasItem("pentangle"))
      {
         CultimaPanel.sendMessage("Thy Pentangle shatters");
         CultimaPanel.player.removeItem("pentangle");
         if(Math.random() < 0.5 || CultimaPanel.player.hasItem("pentangle"))
         {
            CultimaPanel.sendMessage("and blocks the Wolfen curse!");
            return;
         }
      }
      CultimaPanel.sendMessage("You have the Wolfen curse!");
      if(CultimaPanel.player.getReputation() > 0)
         CultimaPanel.player.setReputation(-1*CultimaPanel.player.getReputation());
            
      boolean found = false;
      for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(m.getType().equals("Werewolf"))
         {
            found = true;
            break;
         }
      }
      if(!found)
      {
         CultimaPanel.missionStack.add(new Mission(false, true, false));
         FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
      }
   }
 
   public static void removeWerewolfCurse()
   {  //change player back into human
      if(!CultimaPanel.player.isWerewolf())
         return;
      Achievement.earnAchievement(Achievement.MORE_HUMAN_THAN_HUMAN);
      CultimaPanel.flashColor = new Color(165, 42, 42);
      CultimaPanel.flashFrame = CultimaPanel.numFrames;
      CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
      Sound.thunder();
      CultimaPanel.player.setName(CultimaPanel.player.getName().replace("~",""));
      CultimaPanel.sendMessage("You are relieved of the Wolfen curse!");
      if(CultimaPanel.player.getReputation() < 0)
         CultimaPanel.player.setReputation(0);
   
      for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(m.getType().equals("Werewolf"))
         {
            CultimaPanel.missionStack.remove(i);
            Player.stats[Player.MISSIONS_COMPLETED]++;
            if(Player.stats[Player.MISSIONS_COMPLETED] >= 50)
               Achievement.earnAchievement(Achievement.TASK_MASTER);
            CultimaPanel.player.addExperience(100);
            FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
            break;
         }
      }
   }
   
   public static boolean isWerewolfTime()
   {
      return (CultimaPanel.time>=21 || CultimaPanel.time<=4);
   }
   
   public static void clearWolfStatus()
   {
      if(CultimaPanel.player.isWolfForm())
         CultimaPanel.player.setImageIndexTemp(Player.NONE);
   }
   
   public static void checkWerewolfStatus()
   {
      if(!CultimaPanel.player.isWerewolf() || CultimaPanel.player.isSailing() || TerrainBuilder.indoors())
         return;
      if(isWerewolfTime() && !CultimaPanel.player.isWolfForm() && CultimaPanel.weather==0)
      {
         if(CultimaPanel.player.onHorse())      //dismount horse changing weapons
            CultimaPanel.player.dismountHorse();
         CultimaPanel.flashColor = new Color(165, 42, 42);
         CultimaPanel.flashFrame = CultimaPanel.numFrames;
         CultimaPanel.flashRadius = CultimaPanel.SCREEN_SIZE / 4;
         Sound.thunder();
         CultimaPanel.player.setImageIndexTemp(Player.WOLF);
         if(CultimaPanel.player.isCamping())
            CultimaPanel.player.setCamping(false);
         CultimaPanel.sendMessage("The beast within emerges!");
         CultimaPanel.player.setBody(CultimaPanel.player.getHealthMax());
      }
      else if(!isWerewolfTime() && CultimaPanel.player.isWolfForm())
      {
         clearWolfStatus();
         CultimaPanel.sendMessage("Thy humanity returns!");
      }   
   }

//used when the player purchases a ship from a port town
//post: places a ship in the world owned to the player at row, col
//      returns false if it can not be placed
   public static boolean placeShipNear(int row, int col, byte type)
   {
      return (placeAndGetShipNear(row, col, type) != null);
   }
   
//used when the player purchases a ship from a port town
//post: places a ship in the world owned to the player at row, col and returns it
//      returns null if it can not be placed
   public static NPCPlayer placeAndGetShipNear(int row, int col, byte type)
   {
   //find the closest water location to row,col that is also adjacent to another water
      ArrayList<Coord> locs =  waterLocsAdjacentTo(row, col, false);
      if(locs.size() == 0)
      {
         ArrayList<Coord> locsUp =  waterLocsAdjacentTo(CultimaPanel.equalizeWorldRow(row-1), col, false);
         ArrayList<Coord> locsDown =  waterLocsAdjacentTo(CultimaPanel.equalizeWorldRow(row-1), col, false);
         ArrayList<Coord> locsRight =  waterLocsAdjacentTo(CultimaPanel.equalizeWorldRow(row),  CultimaPanel.equalizeWorldCol(col+1), false);
         ArrayList<Coord> locsLeft =  waterLocsAdjacentTo(CultimaPanel.equalizeWorldRow(row),  CultimaPanel.equalizeWorldCol(col-1), false);
         for(Coord p:locsUp)
            locs.add(p);
         for(Coord p:locsDown)
            locs.add(p);
         for(Coord p:locsRight)
            locs.add(p);
         for(Coord p:locsLeft)
            locs.add(p);
      }
      if(locs.size() == 0)
         return null;
      int maxIndex = 0;
      ArrayList<Coord> maxAdj = waterLocsAdjacentTo(locs.get(maxIndex).getRow(), locs.get(maxIndex).getCol(), true);
      for(int i=1; i<locs.size(); i++)
      {
         Coord p = locs.get(i);
         ArrayList<Coord> adj =  waterLocsAdjacentTo(p.getRow(), p.getCol(), true);
         if (adj.size() > maxAdj.size())
         {
            maxIndex = i;
            maxAdj =  waterLocsAdjacentTo(locs.get(maxIndex).getRow(), locs.get(maxIndex).getCol(), true);
         }
      }
      if(maxAdj.size() == 0)
         return null;
      int r = CultimaPanel.equalizeWorldRow(locs.get(maxIndex).getRow());  
      int c =  CultimaPanel.equalizeWorldCol(locs.get(maxIndex).getCol());
      removeNPCat( r,  c, 0);
      NPCPlayer ourShip = new NPCPlayer(type, r, c, 0, "world");
      ourShip.setGold(0);
      ourShip.clearItems();
      ourShip.setBody(100);  
      ourShip.setHasMet(true);
      if(type == NPC.BOAT)
         CultimaPanel.boats.add(ourShip);
      else
         CultimaPanel.worldMonsters.add(ourShip);
      return ourShip;
   }
   
   //used to resolve ship grappling battles in CultimaPanel
   //post: returns an NPCPlayer that is a ship that is in an adjacent spot to the player and that the player doesn't own
   //      returns null if there isn't one
   public static NPCPlayer getAdjacentEnemyShip()
   {
      Player p = CultimaPanel.player;
      NPCPlayer npc = null;
      NPCPlayer shipUp = getNPCAt(CultimaPanel.equalizeWorldRow(p.getWorldRow()-1), p.getWorldCol(), 0);
      NPCPlayer shipDown = getNPCAt(CultimaPanel.equalizeWorldRow(p.getWorldRow()+1), p.getWorldCol(), 0);
      NPCPlayer shipLeft = getNPCAt(p.getWorldRow(), CultimaPanel.equalizeWorldCol(p.getWorldCol()-1), 0);
      NPCPlayer shipRight = getNPCAt(p.getWorldRow(), CultimaPanel.equalizeWorldCol(p.getWorldCol()+1), 0);
      NPCPlayer shipUpLeft = getNPCAt(CultimaPanel.equalizeWorldRow(p.getWorldRow()-1), CultimaPanel.equalizeWorldCol(p.getWorldCol()-1), 0);
      NPCPlayer shipDownLeft = getNPCAt(CultimaPanel.equalizeWorldRow(p.getWorldRow()+1),CultimaPanel.equalizeWorldCol(p.getWorldCol()-1), 0);
      NPCPlayer shipUpRight = getNPCAt(CultimaPanel.equalizeWorldRow(p.getWorldRow()-1), CultimaPanel.equalizeWorldCol(p.getWorldCol()+1), 0);
      NPCPlayer shipDownRight = getNPCAt(CultimaPanel.equalizeWorldRow(p.getWorldRow()+1), CultimaPanel.equalizeWorldCol(p.getWorldCol()+1), 0);
      if(shipUp!=null && NPC.isBigShip(shipUp))
         npc = shipUp;
      else if(shipDown!=null && NPC.isBigShip(shipDown))
         npc = shipDown;
      else if(shipLeft!=null && NPC.isBigShip(shipLeft))
         npc = shipLeft;
      else if(shipRight!=null && NPC.isBigShip(shipRight))
         npc = shipRight;
      else if(shipUpLeft!=null && NPC.isBigShip(shipUpLeft))
         npc = shipUpLeft;
      else if(shipDownLeft!=null && NPC.isBigShip(shipDownLeft))
         npc = shipDownLeft;
      else if(shipUpRight!=null && NPC.isBigShip(shipUpRight))
         npc = shipUpRight;
      else if(shipDownRight!=null && NPC.isBigShip(shipDownRight))
         npc = shipDownRight;   
      if(npc!=null && !npc.hasMet())
         return npc;
      return null;
   }
   
   //used to resolve ship grappling battles in CultimaPanel
   //post: returns an NPCPlayer ship that is <= 1 dist to the player and that the player owns
   //      returns null if there isn't one
   public static NPCPlayer getAdjacentOwnedShip()
   {
      Player p = CultimaPanel.player;
      for(int r=p.getWorldRow()-1; r<=p.getWorldRow()+1; r++)
         for(int c=p.getWorldCol()-1; c<=p.getWorldCol()+1; c++)
         {
            int row = CultimaPanel.equalizeWorldRow(r);
            int col = CultimaPanel.equalizeWorldCol(c);
            NPCPlayer npc = getNPCAt(row, col, 0);
            if(npc!=null && NPC.isShip(npc) && npc.hasMet())
               return npc;
         }
      return null;
   }

  //post: returns adjacent npc ship if player and adjacent ship npc are sailing and their sides are presenting to one another 1 space away
  //      returns adjacent npc ship if player is on land and adjacent npc is a ship and the npc side is presented to the player 1 space away
  //      returns null if there is no ship to grapple with
   public static NPCPlayer shipAlignedForGrapple()
   {
      //need a 5 second cool-down before entering another battle with a ship
      if(System.currentTimeMillis() <= CultimaPanel.shipGrappleTime+5000)
         return null;
      Player p = CultimaPanel.player;
      NPCPlayer npc = getAdjacentEnemyShip();
      if(p==null || npc==null)
         return null;
      if(!NPC.isBigShip(npc))
         return null;                                          //npc player must be a ship that is not ours
      if(npc.hasMet())
         return null;                                         
      double dist = Display.findDistance(p.getRow(), p.getCol(), npc.getRow(), npc.getCol());
      if(dist != 1)                                            //must be 1 space away
         return null;
      if(p.getCol()!=npc.getCol() && p.getRow()!=npc.getRow()) //must be in same row or col
         return null;
   
      boolean sailing = p.onShip();
      String playerOn = CultimaPanel.allTerrain.get(Math.abs(CultimaPanel.map.get(0)[p.getRow()][p.getCol()])).getName().toLowerCase();
      boolean flight = (p.getActiveSpell("Flight")!=null);
      if((!sailing && playerOn.contains("water")) || flight || CultimaPanel.player.getImageIndex()==Player.BOAT)
         return null;
      
      boolean pLeftOfNpc = false;
      boolean pRightOfNpc = false;   
      boolean pAboveNpc = false;
      boolean pBelowNpc = false;
   
      if(p.getCol() == CultimaPanel.equalizeWorldCol(npc.getCol()-1) && p.getRow()==npc.getRow())
         pLeftOfNpc = true;
      if(p.getCol() == CultimaPanel.equalizeWorldCol(npc.getCol()+1) && p.getRow()==npc.getRow())
         pRightOfNpc = true;
   
      if(p.getRow() == CultimaPanel.equalizeWorldRow(npc.getRow()-1) && p.getCol()==npc.getCol())
         pAboveNpc = true;
      if(p.getRow() == CultimaPanel.equalizeWorldRow(npc.getRow()+1) && p.getCol()==npc.getCol())
         pBelowNpc = true;
         
      boolean pFacingUp = false;
      boolean pFacingRight = false;
      boolean pFacingDown = false;
      boolean pFacingLeft = false;
      byte pDir = p.getLastDir();
      if(pDir==LocationBuilder.NORTH)
         pFacingUp = true;
      else if(pDir==LocationBuilder.EAST)
         pFacingRight = true;
      else if(pDir==LocationBuilder.SOUTH)
         pFacingDown = true;
      else //if(pDir==LocationBuilder.WEST)
         pFacingLeft = true;
         
      boolean npcFacingUp = false;
      boolean npcFacingRight = false;
      boolean npcFacingDown = false;
      boolean npcFacingLeft = false;
      String pic = npc.getCurrentPicture().toString().toLowerCase();
      if(pic.contains("shipup"))
         npcFacingUp = true;
      else if(pic.contains("shipright"))
         npcFacingRight = true;
      else if(pic.contains("shipdown"))
         npcFacingDown = true;
      else //if(pic.contains("shipleft"))
         npcFacingLeft = true;
   
      if(sailing)    //make sure sides of ships are presented to each other
      {
         if((pFacingUp || pFacingDown) && (npcFacingUp || npcFacingDown) && (pLeftOfNpc || pRightOfNpc))
            return npc;
         if((pFacingLeft || pFacingRight) && (npcFacingLeft || npcFacingRight) && (pAboveNpc || pBelowNpc))
            return npc;
      }
      else           //p is on land - make sure side of ship is facing player
      {
         return npc;
      }
      return null;
   }
   
   public static void getBigTreasure()
   {
      int treasureType = rollDie(1,4);
      if(CultimaPanel.player.hasWeapon("Grenade-of-Antioch"))
      {
         treasureType = rollDie(1,3);
      }
      if(treasureType==1)
      {
         int gold = ((int)(Math.random() * 60) + 1)*10;
         CultimaPanel.player.addGold(gold);
         CultimaPanel.sendMessage("You found a grand treasure with "+gold+" gold!");
      }
      else if(treasureType==2)
      {
         String stone = Item.getRandomStone().getName();
         CultimaPanel.player.addItem(stone);
         int gold = ((int)(Math.random() * 40) + 1)*5;
         CultimaPanel.player.addGold(gold);
         CultimaPanel.sendMessage("You found a grand treasure with "+gold+" gold and a "+stone+"!");
      }
      else if(treasureType==3)
      {
         String stone1 = Item.getRandomStone().getName();
         String stone2 = Item.getRandomStone().getName();
         int numTries = 0;
         while(stone1.equals(stone2) && numTries<1000)
         {
            stone2 = Item.getRandomStone().getName();
            numTries++;
         }
         CultimaPanel.player.addItem(stone1);
         CultimaPanel.player.addItem(stone2);
         int gold = ((int)(Math.random() * 20) + 1)*2;
         CultimaPanel.player.addGold(gold);
         CultimaPanel.sendMessage("You found a grand treasure with "+gold+" gold, a "+stone1+" and a "+stone2+"!");
      }
      else //if(treasureType==4)
      {
         CultimaPanel.player.addWeapon(Weapon.getHolyHandGrenade());
         CultimaPanel.sendMessage("You found the Holy Hand Grenade of Antioch!");
      }
   }
   
   //dissolves rainbow if we go indoors, it starts raining or it gets too dark
   public static void checkRainbow()
   {
      if(CultimaPanel.rainbowAlpha > 0)
      {
         if(CultimaPanel.time > 16 || CultimaPanel.weather > 2 || TerrainBuilder.indoors())
         {
            CultimaPanel.rainbowFrame = 0;
         }
      }
   }
   
   public static void changeWeather()
   {
      boolean rainStart = (Math.random() < CultimaPanel.rain_probability);
      double rainbowProb = 0.25, fogProb = 0.4;
      if(Display.isSummer())
         rainbowProb *= 2;
      if(Display.isSpring() || Display.isFall())
         fogProb *= 2;   
      if(CultimaPanel.player.getLocationType().toLowerCase().contains("jurassica"))
         fogProb *= 10;   
      boolean holiday = ((CultimaPanel.days % 100) == 50);   
      if(holiday && !CultimaPanel.isNight)
      {
         if(CultimaPanel.weather > 0)
         {
            CultimaPanel.weather--;
         }
         CultimaPanel.rainbowFrame = CultimaPanel.numFrames + ((CultimaPanel.animDelay*3) * rollDie(100,150));
         CultimaPanel.rainbowAlpha = 1;
         return;
      }
      if((CultimaPanel.weather == 0 && !rainStart) || CultimaPanel.player.getLocationType().toLowerCase().contains("arena"))
      {
         if(Math.random() < fogProb && CultimaPanel.windDir==0 && !Display.isWinter() && CultimaPanel.weather <= 1 
            && (CultimaPanel.time >=3 && CultimaPanel.time <= 8) && CultimaPanel.fogAlpha==0 && !TerrainBuilder.indoors())
         {
            CultimaPanel.fogFrame = CultimaPanel.numFrames + ((CultimaPanel.animDelay*3) * rollDie(100,150));
            CultimaPanel.fogAlpha = 1;
         }  
         else
            CultimaPanel.fogFrame = 0; 
      }
      else
      {
         int weatherChange = rollDie(-3,2);
         int weatherBefore = CultimaPanel.weather;
         CultimaPanel.weather += weatherChange;
         int weatherAfter = CultimaPanel.weather;
         
         if(Math.random() < rainbowProb && weatherBefore >= 2 && weatherAfter < 2 && !Display.isWinter() 
            && (CultimaPanel.time >=8 && CultimaPanel.time <= 16) && CultimaPanel.rainbowAlpha==0)
         {
            CultimaPanel.rainbowFrame = CultimaPanel.numFrames + ((CultimaPanel.animDelay*3) * rollDie(100,150));
            CultimaPanel.rainbowAlpha = 1;
         }  
         else
            CultimaPanel.rainbowFrame = 0;
          
         if(Math.random() < fogProb && CultimaPanel.windDir==0 && !Display.isWinter() && CultimaPanel.weather <= 1 
            && (CultimaPanel.time >=3 && CultimaPanel.time <= 8) && CultimaPanel.fogAlpha==0 && !TerrainBuilder.indoors())
         {
            CultimaPanel.fogFrame = CultimaPanel.numFrames + ((CultimaPanel.animDelay*3) * rollDie(100,150));
            CultimaPanel.fogAlpha = 1;
         }  
         else
            CultimaPanel.fogFrame = 0; 
              
         if(CultimaPanel.weather < 0)
            CultimaPanel.weather = 0;
         if(CultimaPanel.weather > 10)
            CultimaPanel.weather = 10;
         if(weatherChange != 0 && CultimaPanel.weather >= 1 && !TerrainBuilder.indoors())
            Sound.rain(CultimaPanel.weather);
      }
      if(CultimaPanel.weather == 0 || TerrainBuilder.indoors())
         Sound.rain(0);
      if(Math.random() < 0.25)    //change the wind
      {
         if(CultimaPanel.windDir == 5)
            CultimaPanel.windDir = (byte)(Math.random()*5); 
         else
         {
            if(Math.random() < 0.25)
               CultimaPanel.windDir = 4;
            else
            {
               if(Math.random() < 0.5)
                  CultimaPanel.windDir = (byte)((CultimaPanel.windDir + 1) % 4);
               else
                  CultimaPanel.windDir = (byte)(Math.max(0, CultimaPanel.windDir - 1));
            }
         }
      }
   }

   public static String locationInfo()
   {
      String locType = CultimaPanel.player.getLocationType();
      int mi = CultimaPanel.player.getMapIndex();
      String enterLoc = CultimaPanel.allLocations.get(mi).getName();
      if(mi==0)
         return ("In the vast expanse of Cultima...");
      else if(locType.equals("ship") )
         return ("On the shore before a great ship...");
      else if(locType.equals("pacrealm") )
         return ("Confused in the labyrinth of the Pac-Realm...");
      else if(locType.equals("underworld"))
         return("Upon the smoldering dirt of the Underworld...");
      else if(locType.equals("jurassica"))
         return("In the misty jungles of a forgotten time...");
      else if(locType.equals("1942"))
         return("On the frozen battlefield of a future hellscape...");
      else if(locType.equals("wolfenstein"))
         return("In a strange castle from the future...");
      else if(locType.equals("graboid"))
         return("Within the acidic innards of a Graboid...");
      else if(locType.equals("beast"))
         return("Within the slimy innards of a great beast...");
      else if(locType.contains("city"))
         return("Within the great city walls of "+enterLoc+"...");
      else if(locType.contains("fortress"))
         return("Within the city gates of "+enterLoc+"...");
      else if(locType.contains("port"))
         return("Upon the muddy port-town streets of "+enterLoc+"...");
      else if(locType.contains("village"))
         return("Upon the village paths of "+enterLoc+"...");
      else if(locType.contains("domicile"))
         return("At the domicile of "+enterLoc+"...");
      else if(locType.contains("castle"))
         return("Within the castle halls of "+enterLoc+"..."); 
      else if(locType.contains("tower"))
         return("Within the castle halls of "+enterLoc+"...");
      else if(locType.contains("temple"))
         return("Upon the temple columns of "+enterLoc+"...");
      else if(locType.contains("cave"))
         return("Within the darkened cave of "+enterLoc+"...");
      else if(locType.contains("mine"))
         return("Traversing the mine tunnels of "+enterLoc+"...");
      else if(locType.contains("lair"))
         return("Within the monsterous lair of "+enterLoc+"...");
      else if(locType.contains("dungeon"))
         return("Exploring the dungeon depths of "+enterLoc+"...");
      else if(locType.contains("battlefield"))
         return("Marching the scarred fields of "+enterLoc+"...");
      return "Within "+enterLoc;
   }
  
  //post: sends a message about the type of terrain the player is moving over
   public static void reportTerrainNarrative(Terrain currentPos)
   {
      if(currentPos.getName().contains("Flower") && CultimaPanel.player.hasItem("floretbox") && !CultimaPanel.player.enoughFlowers())
      {}  //skip flowers in case we are picking some - we have our own message for that
      else 
      {  
         int movementType = Terrain.WALKING;
         if(CultimaPanel.player.isFlying())
         {
            movementType = Terrain.FLYING;
         }
         else if(CultimaPanel.player.onHorse())
         {
            movementType = Terrain.HORSEBACK;
         }
         else if(CultimaPanel.player.onShip())
         {
            movementType = Terrain.SAILING;
         }
         else if(CultimaPanel.player.isSailing())
         {
            movementType = Terrain.ROWING;
         }
         String terrainNarrative = currentPos.getNarrative(movementType);
         if(terrainNarrative != null && terrainNarrative.length() > 0)
         {
            CultimaPanel.sendMessage(terrainNarrative);
         }
      }
   }
   
   public static NPCPlayer getRandomBarrel(int r, int c, int mi, String locType, String type)
   {
      String typeNames = "emptygoldarmoryrationstavernmagic";
      String [] types = {"empty", "empty", "arrows", "arrows", "gold", "gold", "armory", "rations", "tavern", "magic"};
      NPCPlayer barrel = (new NPCPlayer(NPC.BARREL, r, c, mi, locType));
      barrel.clearItems();
      barrel.setGold(0);
      if(type == null || type.length()==0 || !typeNames.contains(type))
      {
         type = getRandomFrom(types);
      }
      //make barrel contents match the shoppe type
      if(type.equals("armory"))
      {
         if(Math.random() < 0.5)
         {
            barrel.setWeapon(Weapon.getRandomWeaponSimple());
         }
         else if(Math.random() < 0.5)
         {
            barrel.setArmor(Armor.getRandomArmorSimple());
         }
      } 
      else if(type.equals("magic"))
      {
         if(Math.random() < 0.5)
            barrel.addItem(Potion.getRandomPotion());
         else if(Math.random() < 0.1)
            barrel.addItem(Item.getRandomMagicItemSimple());   
      }
      else if(type.equals("rations"))
      {
         if(Math.random() < 0.5)       //chance will have rations
         {
            barrel.addItem(new Item("meat", Utilities.rollDie(2,6)));
         }
      }
      else if(type.equals("tavern"))
      {
         if(Math.random() < 0.25)       //chance will have rations
         {
            barrel.addItem(new Item("meat", Utilities.rollDie(1,2)));
         }
         if(Math.random() < 0.1)
            barrel.setGold(Utilities.rollDie(1, 10));
      } 
      else if(type.equals("gold"))
      {
         barrel.setGold(rollDie(1, 15));
      }
      else if(type.equals("arrows"))
      {
         barrel.setNumArrows(rollDie(2,25));
      }
      return barrel;
   }
   
   //every two days, refresh which bounty is posted
   public static void setBounty()
   {
      boolean holiday = ((CultimaPanel.days % 100) == 50);   
      if(holiday)
      {
         CultimaPanel.postedBounty = "<Holiday sign:> Merry Skara Brae Day to all who enter here!";
      }
      else
      {
         String [] bountyNames = {"Dread Brigand Roberts", "She-Assassin", "Viper Assassin", "Bow Assassin", "Brigand King"};
         String bountyName = NPC.getRandomFrom(bountyNames);
         int bountyValue = 100;
         int playerBounty = CultimaPanel.player.getBounty();
         if(Utilities.rollDie(1,100) < playerBounty)
         {
            bountyValue = playerBounty;
            bountyName = CultimaPanel.player.getShortName() + " " + CultimaPanel.player.getReputationName();
         }
         CultimaPanel.postedBounty = "<Bounty sign:> "+bountyValue + " gold for the head of the " + NPC.getRandomFrom(NPC.insultAdjective) + " " + bountyName;
      }
   }
  
  //swaps values between list at index i and list at index j (for the sort method)
   public static void swap(ArrayList<Spell> list, int i, int j)
   {
      Spell temp = list.get(i);
      list.set(i, list.get(j));
      list.set(j, temp);
   }
   
   //sorts a List from least to greatest (for spells list)
   public static void sort(ArrayList<Spell> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         int min = i;
         for(int j = i + 1; j < list.size(); j++)
         {
            if(list.get(j).compareTo(list.get(min)) < 0)
            {
               min = j;
            }
         }
         swap(list, i, min);
      }
   }
   
     //swaps values between list at index i and list at index j (for the sort method)
   public static void swapItems(ArrayList<Item> list, int i, int j)
   {
      Item temp = list.get(i);
      list.set(i, list.get(j));
      list.set(j, temp);
   }
   
   //sorts a List from least to greatest (for spells list)
   public static void sortItems(ArrayList<Item> list)
   {
      for(int i = 0; i < list.size(); i++)
      {
         int min = i;
         for(int j = i + 1; j < list.size(); j++)
         {
            if(list.get(j).compareTo(list.get(min)) < 0)
            {
               min = j;
            }
         }
         swapItems(list, i, min);
      }
   }
   
   //pre: list != null
   //post:returns a random element from the list
   public static <anyType> anyType getRandomFrom(List<anyType> list)
   {
      if(list==null || list.size()==0)
      {
         return null;
      }
      int index = (int)(Math.random()*list.size());
      return list.get(index);
   }
   
   //pre: list != null
   //post:returns a random element from the list
   public static String getRandomFrom(String [] list)
   {
      if(list==null || list.length==0)
      {
         return null;
      }
      int index = (int)(Math.random()*list.length);
      return list[index];
   }
   
   //pre: list != null
   //post:returns a random element from the list
   public static byte getRandomFrom(byte [] list)
   {
      if(list==null || list.length==0)
      {
         return -1;
      }
      int index = (int)(Math.random()*list.length);
      return list[index];
   }
   
   //pre: list != null
   //post:returns a random element from the list
   public static Terrain getRandomFrom(Terrain [] list)
   {
      if(list==null || list.length==0)
      {
         return null;
      }
      int index = (int)(Math.random()*list.length);
      return list[index];
   }
   
   //pre: list != null
   //post:returns a random element from the list
   public static Item getRandomFrom(Item [] list)
   {
      if(list==null || list.length==0)
      {
         return null;
      }
      int index = (int)(Math.random()*list.length);
      return list[index];
   }
   
   //pre: list != null
   //post:returns a random element from the list
   public static Color getRandomFrom(Color [] list)
   {
      if(list==null || list.length==0)
      {
         return null;
      }
      int index = (int)(Math.random()*list.length);
      return list[index];
   }
}
