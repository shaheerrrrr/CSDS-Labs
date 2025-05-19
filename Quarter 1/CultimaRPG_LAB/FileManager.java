import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.util.Scanner;

public class FileManager
{      
 //to test reading in player information from a file
   public static void main(String [] arg)
   {
      boolean pass = false;
      //try from testPlayerFile1.txt
      Player attempt = FileManager.readPlayerFromFile("testPlayerFile1.txt");
      if(attempt == null)
      {
         System.out.println("testPlayerFile1.txt did not work. Check the last message to see where the problem is.");
         System.out.println("If there is no message preceding this one, then you may have read in an extra line from testPlayerFile1.txt, or testPlayerFile1.txt got corrupted.");
      }
      else
      {
         String fromYourMethod = attempt.toString();
         String fromFile = readFile("testPlayerFile1.txt");
         if(fromYourMethod.equals(fromFile))
         {
            System.out.println("Success reading from testPlayerFile1.txt");
            pass = true;
         }
         else
         {
            System.out.println("Something went wrong. This is what was not read in correctly from testPlayerFile1.txt:");
            LinkedList<String>differences = findStringDifference(fromFile, fromYourMethod);
            for(String diff:differences)
               System.out.println(diff);
         }
      }
      
      //try from testPlayerFile2.txt if the first test worked
      if(pass)
      {
         attempt = FileManager.readPlayerFromFile("testPlayerFile2.txt");
         if(attempt == null)
         {
            System.out.println("testPlayerFile2.txt did not work. Check the last message to see where the problem is.");
            System.out.println("If there is no message preceding this one, then you may have read in an extra line from testPlayerFile2.txt, or testPlayerFile2.txt got corrupted.");
         }
         else
         {
            String fromYourMethod = attempt.toString();
            String fromFile = readFile("testPlayerFile2.txt");
            if(fromYourMethod.equals(fromFile))
            {
               System.out.println("Success reading from testPlayerFile2.txt");
               System.out.println("If both files read in correctly, time to test out the game with CultimaDriver.");
            }
            else
            {
               System.out.println("Something went wrong. This is what was not read in correctly from testPlayerFile2.txt:");
               LinkedList<String>differences = findStringDifference(fromFile, fromYourMethod);
               for(String diff:differences)
                  System.out.println(diff);
            }
         }
      }
      System.exit(0);
   }


//pre: line != null
//post:if active is true, returns an encoded version of the String line to write out to the player file
//     if active is false, returns the unaltered String line
   public static String encode(String line, boolean activate)
   {
      if(!activate)
         return line;
      String ans = "";
      for(int i=0; i<line.length(); i++)
      {
         char let = line.charAt(i);
         if(let < 33 || let > 126)
            ans += let;
         else
            ans += (char)(let - 3);
      }
      return ans;
   }

 //pre: line != null
 //post:if active is true, returns a decoded version of the String line
 //     if active is false, returns the unaltered String line
   public static String decode(String line, boolean activate)
   {
      if(!activate)
         return line;
      String ans = "";
      for(int i=0; i<line.length(); i++)
      {
         char let = line.charAt(i);
         if(let < 33 || let > 126)
            ans += let;
         else
            ans += (char)(let + 3);
      }
      return ans;
   }

//pre: word != null
//post:returns true if word could be represented as a whole number, positive or negative
   public static boolean wordIsInt(String word)
   {
      if(word==null || word.length() == 0)
         return false;
      word = word.trim();  
      if(word.length() == 1 && (word.charAt(0) == '-' || !Character.isDigit(word.charAt(0))))
         return false;
      for(int i=1; i<word.length(); i++)
         if(!Character.isDigit(word.charAt(i)))
         {
            return false;
         }
      return true;
   }

//pre: word != null
//post:returns true if word could be represented as a double, positive or negative
   public static boolean wordIsDouble(String word)
   {
      if(word==null || word.length() == 0)
         return false;
      word = word.trim();   
      if(word.length() == 1 && (word.charAt(0) == '-' || !Character.isDigit(word.charAt(0))))
         return false;
      int numDecimals = 0;   
      for(int i=1; i<word.length(); i++)
      {
         char current = word.charAt(i);
         if(current == '.')
            numDecimals++;
         else if(!Character.isDigit(current))
            return false;
         if(numDecimals > 1)
            return false;
      }
      return true;
   }

//***********************Player file
   
 //pre:  input != null and input is initialized to be a FileReader directed towards the player information text file
 //post: reads in and returns the next line from the file, removing any comment after "**" and possibly decoding it if encoded is true
   private static String readInNextLine(Scanner input, boolean encoded)
   {
      String line = decode(input.nextLine().trim(), encoded);        //**name
      int pos = line.indexOf("**");
      if(pos >= 0)
         line = line.substring(0,pos).trim();
      return line;
   }

//pre: fileName is a properly formated file of player information.
//post:read Player info from file and returns a Player object.  
//     If the file doesn't exist or is improperly formated, returns null.
   public static Player readPlayerFromFile(String fileName)
   {
      try
      {
         Scanner input = new Scanner(new FileReader(fileName));
         boolean encoded = false;
         if(input.hasNextLine())
         {
            String line = input.nextLine().trim();       //read in whether or not the file is encoded, and needs to be decoded
            int pos = line.indexOf("**");                //this will be a random integer.  If it is even, then the file is encoded.
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            int enc = 0;
            if(wordIsInt(line))
               enc = Integer.parseInt(line);
            if(enc % 2 == 0)
               encoded = true;  
         }
         if(input.hasNextLine())
         {  
            //code to read the name from a player on line 2 in a text file (data.player.txt if you run the game, or testPlayerFile1.txt if you run this program)
            String name = "";    
            String line = readInNextLine(input, encoded);         //**name
            name = line;
         
            //code to read the mapMark information from a player on line 3 in a text file (data.player.txt if you run the game, or testPlayerFile1.txt if you run this program)
            int [] mapMark = null;
            line = readInNextLine(input, encoded);                //**mapMark
            //**************TO DO: enter code to process mapMark from file: Line 3
            //As an example, assume line looks like:                 line -> "4 3 12"
            //Initialize the mapMark array to an array of 3 ints.   
            //Split the String line around the space character       parts[0]->"4", parts[1]->"3", parts[2]->"12"
            //Trim each element, and fill the mapMark array with the int version of each cleaned element.
            //the next 7 lines from the file will require very similar code, but with different array names with different sizes.

            String[] parts = line.split(" ");
            String mark1 = parts[0].trim();
            String mark2 = parts[1].trim();
            String mark3 = parts[2].trim();

            mapMark = new int[]{Integer.parseInt(mark1), Integer.parseInt(mark2), Integer.parseInt(mark3)};

            if(mapMark==null || mapMark.length!=3)
            {  //this code should not be changed: it will halt the method and let you know where the problem was if something was done wrong.
               System.out.println("Error reading mapMark (line 3) from "+fileName);
               return null;
            }
         
            //code to read the potions inventory from a player on line 4 in a text file (data.player.txt if you run the game, or testPlayerFile1.txt if you run this program)
            int [] potions = null;
            line = readInNextLine(input, encoded);              //**potions
            //**************TO DO: enter code to process potions from file: Line 4   
            parts = line.split(" ");
            potions = new int[Potion.NUM_POTIONS];
            for (int i = 0; i < potions.length; i++)
               potions[i] = Integer.parseInt(parts[i].trim());
            //******************************************************************/

            if(potions==null || potions.length!=Potion.NUM_POTIONS)
            {
               System.out.println("Error reading potions (line 4) from "+fileName);
               return null;
            }
            
            //code to read flowerBoxCount from file: Line 5
            int [] flowerBoxCount = null;
            line = readInNextLine(input, encoded);              //**flowerBoxCount
            //**************TO DO: enter code to process flowerBoxCount from file: Line 5
            parts = line.split(" ");
            flowerBoxCount = new int[Player.NUM_FLOWERS];
            for (int i = 0; i < flowerBoxCount.length; i++)
               flowerBoxCount[i] = Integer.parseInt(parts[i].trim());
            //******************************************************************/
            if(flowerBoxCount==null || flowerBoxCount.length!=Player.NUM_FLOWERS)
            {
               System.out.println("Error reading flowerBoxCount (line 5) from "+fileName);
               return null;
            }
         
            //code to read specificExperience from file: Line 6
            int [] specificExperience = null;
            line = readInNextLine(input, encoded);              //**specificExperience
            //**************TO DO: enter code to process specificExperience from file: Line 6
            parts = line.split(" ");
            specificExperience = new int[4];
            for (int i = 0; i < specificExperience.length; i++)
               specificExperience[i] = Integer.parseInt(parts[i].trim());
            //******************************************************************/
            if(specificExperience==null || specificExperience.length!=4)
            {
               System.out.println("Error reading specificExperience (line 6) from "+fileName);
               return null;
            }
            
            //code to read stats from file: Line 7
            int [] stats = null;
            line = readInNextLine(input, encoded);              //**stats
            //**************TO DO: enter code to process stats from file: Line 7
            parts = line.split(" ");
            stats = new int[Player.NUM_STATS];
            for (int i = 0; i < stats.length; i++)
               stats[i] = Integer.parseInt(parts[i].trim());
            //******************************************************************/
            if(stats==null || stats.length!=Player.NUM_STATS)
            {
               System.out.println("Error reading stats (line 7) from "+fileName);
               return null;
            }
            
            //code to read infoIndexes from file: Line 8
            int [] infoIndexes = null;
            line = readInNextLine(input, encoded);              //**infoIndexes
            //**************TO DO: enter code to process infoIndexes from file: Line 8
            parts = line.split(" ");
            infoIndexes = new int[Player.NUM_INFO_INDEXES];
            for (int i = 0; i < infoIndexes.length; i++)
               infoIndexes[i] = Integer.parseInt(parts[i].trim());
            //******************************************************************/
            if(infoIndexes==null || infoIndexes.length!=Player.NUM_INFO_INDEXES)
            {
               System.out.println("Error reading infoIndexes (line 8) from "+fileName);
               return null;
            }
         
            //code to read spellHotKeys from file: Line 9
            int []spellHotKeys = null;
            line = readInNextLine(input, encoded);              //**spellHotKeys
            //**************TO DO: enter code to process spellHotKeys from file: Line 9
            parts = line.split(" ");
            spellHotKeys = new int[4];
            for (int i = 0; i < spellHotKeys.length; i++)
               spellHotKeys[i] = Integer.parseInt(parts[i].trim());
            //******************************************************************/
            if(spellHotKeys==null || spellHotKeys.length!=4)
            {
               System.out.println("Error reading spellHotKeys (line 9) from "+fileName);
               return null;
            }
            
            //code to read potionHotKeys from file: Line 10
            int []potionHotKeys = null;
            line = readInNextLine(input, encoded);              //**potionHotKeys
            //**************TO DO: enter code to process potionHotKeys from file: Line 10
            parts = line.split(" ");
            potionHotKeys = new int[Potion.NUM_POTIONS];
            for (int i = 0; i < potionHotKeys.length; i++)
               potionHotKeys[i] = Integer.parseInt(parts[i].trim());
            //******************************************************************/
            if(potionHotKeys==null || potionHotKeys.length!=Potion.NUM_POTIONS)
            {
               System.out.println("Error reading potionHotKeys (line 10) from "+fileName);
               return null;
            }
         
            //code to read weaponHotKeys from file: Line 11
            Coord [] weaponHotKeys = null;
            line = readInNextLine(input, encoded);              //**weaponHotKeys
            //**************TO DO: enter code to process weaponHotKeys from file: Line 11
            //Assume that line has been read in and looks like 8 numbers separated by a space   line -> "2 0 2 1 0 0 14 0"
            //Your task is to get the numeric equivalent of each of these 8 numbers, and populate an array of four Coord objects.
            //Each of the four Coord objects will consist of an x and y value that is the same as every two adjacent numbers that have been extracted from line.
            //weaponHotKeys[0]->(2,0)     weaponHotKeys[1]->(2,1)    weaponHotKeys[2]->(0,0)    weaponHotKeys[3]->(14,0)
            //Open Coord.java to see what the constructor looks like to create an instance of a Coord object.
            //You might correctly guess that you will use the String methods split and trim, the Integer method parseInt and the Coord constructor.
            //******************************************************************/
            parts = line.split(" ");
            weaponHotKeys = new Coord[4];
            for (int i = 0; i < 8; i += 2)
            {
               weaponHotKeys[i/2] = new Coord(Integer.parseInt(parts[i].trim()), Integer.parseInt(parts[i+1].trim()));
            }


            if(weaponHotKeys==null || weaponHotKeys.length!=4)
            {
               System.out.println("Error reading weaponHotKeys (line 11) from "+fileName);
               return null;
            }
         
            //code to read effects from file: Line 12
            ArrayList<String> effects = null;
            line = readInNextLine(input, encoded);               //**effects
            //**************TO DO: enter code to process effects from file: Line 12
            //As an example, let's assume line stores -> "[blessed, poisoned]"
            //Line might store no parts in the brackets, like "[]", up to any finite number of parts like "[blessed, sullied, poisoned, seduced, frozen]"
            //Initialize the ArrayList called effects by assigning it to the ArrayList constructor.             
            //Remove '[' and ']' from line, so that line stores something like: "blessed, poisoned".
            //If line started out like "[]", then removing the brackets will yield an empty string where the line's length is zero.
            //If the line's length is greater than zero, we can now process the string and add elements into the effects List:
            //    Separate elements around the comma character ','    The array that is the result of split might look like this: parts[0]->"blessed", parts[1]->" poisoned"   (note the space before " poisoned" that should be trimmed.
            //    Add each trimmed element into the effects ArrayList: the effects ArrayList might store: "blessed" and "poisoned", depending on what was in the line that we read in from the file.
            //Note: if line started as an empty set of brackets like "[]", the effects ArrayList should be constructed but of size zero.  It should not be null.
            //******************************************************************/
            line = line.substring(1, line.indexOf("]"));
            effects = new ArrayList<>();
            if (!line.isEmpty())
            {
               parts = line.split(",");
               for (int i = 0; i < parts.length; i++)
                  effects.add(parts[i].trim());
            }


            if(effects==null)
            {
               System.out.println("Error reading effects (line 12) from "+fileName);
               return null;
            }
         
            //code to read images from file: Line 13
            ArrayList<String> fileNames = null;
            line = readInNextLine(input, encoded);               //**images ArrayList
            //**************TO DO: enter code to process fileNames from file: Line 13
            //If you test the game and notice that your game character is flashing, make sure you are trimming the substrings that come from the split array.
            //There is likely a file name with a space character as the first or last character in the String.
            //******************************************************************/

            line = line.substring(1, line.indexOf("]"));
            fileNames = new ArrayList<>();
            if (!line.isEmpty())
            {
               parts = line.split(",");
               for (int i = 0; i < parts.length; i++)
                  fileNames.add(parts[i].trim());
            }


            if(fileNames == null || fileNames.isEmpty())
            {
               System.out.println("Error reading fileNames (line 13) from "+fileName);
               return null;
            }
         
            //code to read items from file: Line 14
            ArrayList<String> items = null;
            line = readInNextLine(input, encoded);               //**items
            //**************TO DO: enter code to process items from file: Line 14
            line = line.substring(1, line.indexOf("]"));
            items = new ArrayList<>();
            if (!line.isEmpty())
            {
               parts = line.split(",");
               for (int i = 0; i < parts.length; i++)
                  items.add(parts[i].trim());
            }

            //******************************************************************/
            if(items==null)
            {
               System.out.println("Error reading items (line 14) from "+fileName);
               return null;
            }
                                   
            //code to read spells from file: Line 15
            ArrayList<Spell> spells = null;
            line = readInNextLine(input, encoded);              //**spells
            //**************TO DO: enter code to process spells from file: Line 15 
            //Open up Spell.java to look at the single-argument constructor to see how to create an instance of a Spell object.
            //******************************************************************/
            line = line.substring(1, line.indexOf("]"));
            spells = new ArrayList<>();
            if (!line.isEmpty())
            {
               parts = line.split(",");
               for (int i = 0; i < parts.length; i++)
                  spells.add(new Spell(parts[i].trim()));
            }

            if(spells==null)
            {
               System.out.println("Error reading spells (line 15) from "+fileName);
               return null;
            }
         
            //code to read teleporter memory from file: Line 16
            LinkedList<Teleporter> memory = null;
            line = readInNextLine(input, encoded);               //**teleporter-memory
            //**************TO DO: enter code to process teleporter memory from file: Line 16
            //line might look something like this: line->"[(2 29 21 temple), (0 41 82 world)]".  It also could look like this: line->"[]" or have 1-3 elements in the brackets separated by commas.
            //Given the data in the String line, we want to extract four pieces of information for each element separated by a comma:
            //    For the example where line looks like line->"[(2 29 21 temple), (0 41 82 world)]", we want to extract two groups of information:
            //    The first Teleporter object that we construct and store in memory might have three integers, 2, 29, and 21 and one String: "temple".
            //    The second Teleporter object that we construct and store in memory might have the integers 0, 41, and 82 and the String "world". 
            //Open up Teleporter.java to look at the four-argument constructor to see how to create an instance of a Teleporter object.
            //******************************************************************/

            line = line.substring(1, line.indexOf("]"));
            memory = new LinkedList<>();
            if (!line.isEmpty())
            {
               parts = line.split(",");
               for (int i = 0; i < parts.length; i++)
               {
                  String tel;
                  if (i == 1)
                     tel = parts[i].trim().substring(1, parts[i].length()-2);
                  else
                     tel = parts[i].trim().substring(1, parts[i].indexOf(")"));
                  String[] list = tel.split(" ");
                  memory.add(new Teleporter(Integer.parseInt(list[0]), Integer.parseInt(list[1]), Integer.parseInt(list[2]), list[3]));
               }
            }

            if(memory==null)
            {
               System.out.println("Error reading teleporter memory (line 16) from "+fileName);
               return null;
            }
         
            //code to read armorSet and armorFreq from file: Line 17
            ArrayList<Armor> armorSet = null;
            ArrayList<Integer> armorFreq = null;
            line = readInNextLine(input, encoded);              //**armor:armorFrequency
            //**************TO DO: enter code to process armorSet and armorFreq from file: Line 17
            line = line.substring(1, line.indexOf("]"));
            armorSet = new ArrayList<>();
            armorFreq = new ArrayList<>();
            if (!line.isEmpty())
            {
               parts = line.split(",");
               for (int i = 0; i < parts.length; i++)
               {
                  String[] set = parts[i].split(":");
                  armorSet.add(new Armor(set[0].trim()));
                  armorFreq.add(Integer.parseInt(set[1].trim()));
               }
            }
            //******************************************************************/
            if(armorSet==null || armorFreq==null)
            {
               System.out.println("Error reading armorSet and armorFreq (line 17) from "+fileName);
               return null;
            }
         
            //code to read weapons and weaponFrequencies from file: Lines 18-32
            ArrayList<Weapon> [] weapons = null;
            ArrayList<Integer> [] weaponFrequencies = null;
            //**************TO DO: enter code to process weapons and weaponFrequencies from file: Lines 18-32
            //Note: weapons and weaponFrequencies are standard java arrays where each element of the array will be an ArrayList object.
            //After initializing both java arrays, every element of both arrays will need to be constructed as ArrayLists.
            //There are 15 lines of information that we need to read in from the text file here.  The following code only reads in a single line.
            weapons = new ArrayList[Player.NUM_WEAPONS];
            weaponFrequencies = new ArrayList[Player.NUM_WEAPONS];

            for (int i = 0; i < 16; i++)
            {
               line = readInNextLine(input, encoded);              //**weapon name:weapon frequency
               weapons[i] = new ArrayList<>();
               weaponFrequencies[i] = new ArrayList<>();
               //Recommendation: let a loop do the repetition for you.
               //There should not be any elements of either array that are null.
               //If there is a weapon category for which the player has no weapons of that type, there should be an empty ArrayList there.
               line = line.substring(1, line.indexOf("]"));

               if (!line.isEmpty())
               {
                  parts = line.split(",");
                  for (int k = 0; k < parts.length; k++)
                  {
                     String[] set = parts[k].split(":");
                     weapons[i].add(new Weapon(set[0].trim()));
                     weaponFrequencies[i].add(Integer.parseInt(set[1].trim()));
                  }
               }
            }

            //**************************DONE!************************************/
            if(weapons==null || weaponFrequencies==null || weapons.length!=Player.LUTE+1 || weaponFrequencies.length!=Player.LUTE+1)
            {
               System.out.println("Error reading weapons and weaponFrequencies (lines 18-32) from "+fileName);
               return null;
            }
         
            //code to read imageIndex from file: Line 33
            byte imageIndex = 0;
            line = readInNextLine(input, encoded);               //**imageIndex
            if(!wordIsInt(line))
               return null;
            imageIndex = Byte.parseByte(line);
         
            //code to read startStoryIndex from file: Line 34
            byte startStoryIndex = 0;
            line = readInNextLine(input, encoded);               //**startStoryIndex
            if(!wordIsInt(line))
               return null;
            startStoryIndex = Byte.parseByte(line);
         
            //code to read mapIndex from file: Line 35
            int mapIndex = 0;
            line = readInNextLine(input, encoded);              //**mapIndex
            if(!wordIsInt(line))
               return null;
            mapIndex = Integer.parseInt(line);
         
            //code to read row from file: Line 36
            int row = 0;
            line = readInNextLine(input, encoded);               //**row
            if(!wordIsInt(line))
               return null;
            row = Integer.parseInt(line);
         
            //code to read col from file: Line 37
            int col = 0;
            line = readInNextLine(input, encoded);               //**col
            if(!wordIsInt(line))
               return null;
            col = Integer.parseInt(line);
         
            //code to read worldRow from file: Line 38
            int worldRow = 0;
            line = readInNextLine(input, encoded);               //**world-row
            if(!wordIsInt(line))
               return null;
            worldRow = Integer.parseInt(line);
         
           //code to read worldCol from file: Line 39
            int worldCol = 0;
            line = readInNextLine(input, encoded);               //**world-col
            if(!wordIsInt(line))
               return null;
            worldCol = Integer.parseInt(line);
                  
            //code to read locationType from file: Line 40
            String locationType = "";
            line = readInNextLine(input, encoded);               //**locationType
            locationType = line;
                  
            //code to read might from file: Lne 41
            int might = 0;
            line = readInNextLine(input, encoded);              //**might
            if(!wordIsInt(line))
               return null;
            might = Math.min(Player.MAX_STAT, Integer.parseInt(line));
         
            //code to read mind from file: Line 42
            int mind = 0;
            line = readInNextLine(input, encoded);               //**mind
            if(!wordIsInt(line))
               return null;
            mind = Math.min(Player.MAX_STAT, Integer.parseInt(line));
         
            //code to read agility from file: Line 43
            int agility = 0;
            line = readInNextLine(input, encoded);               //**agility
            if(!wordIsInt(line))
               return null;
            agility = Math.min(Player.MAX_STAT, Integer.parseInt(line));
         
            //code to read body from file: Line 44
            int body = 0;
            line = readInNextLine(input, encoded);               //**body
            if(!wordIsInt(line))
               return null;
            body = Integer.parseInt(line);
            
            //code to read manna from file: Line 45   
            int manna = 0;     
            line = readInNextLine(input, encoded);               //**manna
            if(!wordIsInt(line))
               return null;
            manna = Integer.parseInt(line);
            
            //code to read awareness from file: Line 46 
            int awareness = 0;        
            line = readInNextLine(input, encoded);               //**awareness
            if(!wordIsInt(line))
               return null;
            awareness = Math.min(Player.MAX_AWARENESS, Integer.parseInt(line));
         
            //code to read reputation from file: Line 47
            int reputation = 0;
            line = readInNextLine(input, encoded);               //**reputation
            if(!wordIsInt(line))
               return null;
            reputation = Integer.parseInt(line);
         
            //code to read bounty from file: Line 48
            int bounty = 0;
            line = readInNextLine(input, encoded);               //**bounty
            if(!wordIsInt(line))
               return null;
            bounty = Integer.parseInt(line);
         
            //code to read gold from file: Line 49
            int gold = 0;
            line = readInNextLine(input, encoded);               //**gold
            if(!wordIsInt(line))
               return null;
            gold = Integer.parseInt(line);
         
            //code to read experience from file: Line 50
            int experience = 0;
            line = readInNextLine(input, encoded);               //**experience
            if(!wordIsInt(line))
               return null;
            experience = Integer.parseInt(line);
         
            //code to read expToLevelUp from file: Line 51
            int expToLevelUp = 0;
            line = readInNextLine(input, encoded);               //**experienceToLevelUp
            if(!wordIsInt(line))
               return null;
            expToLevelUp = Integer.parseInt(line);
         
            //code to read weaponSelect from file: Lne 52
            byte weaponSelect = 0;
            line = readInNextLine(input, encoded);               //**weaponSelect
            if(!wordIsInt(line))
               return null;
            weaponSelect = Byte.parseByte(line);
         
            //code to read armorSelect from file: Line 53
            byte armorSelect = 0;
            line = readInNextLine(input, encoded);               //**armorSelect
            if(!wordIsInt(line))
               return null;
            armorSelect = Byte.parseByte(line);
                                  
            //code to read rations from file: Line 54
            byte rations = 0;
            line = readInNextLine(input, encoded);               //**rations
            if(!wordIsInt(line))
               return null;
            rations = Byte.parseByte(line);
                          
            //code to read numArrows from file: Line 55
            int numArrows = 0;
            line = readInNextLine(input, encoded);               //**numArrows
            if(!wordIsInt(line))
               return null;
            numArrows = Integer.parseInt(line);
         
            //code to read numArrows from file: Line 56
            int numStones = 0;
            line = readInNextLine(input, encoded);               //**numStones
            if(!wordIsInt(line))
               return null;
            numStones = Integer.parseInt(line);
         
            //code to read dogBasicInfo from file: Line 57
            String dogBasicInfo = "";
            line = readInNextLine(input, encoded);               //**dogBasicInfo
            dogBasicInfo = line;
         
            //code to read horseBasicInfo from file: Line 58
            line = readInNextLine(input, encoded);               //**horseBasicInfo
            String horseBasicInfo = line;
            
            //code to read npcBasicInfo from file: Line 59
            line = readInNextLine(input, encoded);               //**npcBasicInfo
            String npcBasicInfo = line;
                                     
            Player ans = new Player(name, fileNames, imageIndex, startStoryIndex, mapIndex, row, col, worldRow, worldCol, memory, locationType, mapMark, 
               might, mind, agility, body, manna, awareness, reputation, bounty, gold, experience, expToLevelUp,
               weaponSelect, armorSelect, weapons, weaponFrequencies, armorSet, armorFreq, rations, items, effects, potions, flowerBoxCount, spells, numArrows, numStones, dogBasicInfo, horseBasicInfo, npcBasicInfo,
               specificExperience, stats, infoIndexes, weaponHotKeys, spellHotKeys, potionHotKeys);
         
            int days = 0;
            line = readInNextLine(input, encoded);               //**days
            if(!wordIsInt(line))
               return null;
            days = Integer.parseInt(line);
         
            double time = 0;
            line = readInNextLine(input, encoded);               //**time
            if(!wordIsDouble(line))
               return null;
            time = Double.parseDouble(line);
         
            CultimaPanel.days = days;
            CultimaPanel.time = time;
            input.close();
            return ans;
         }
         return null;
      }
      catch (IOException ex)			//file is corrupted or doesn't exist - remake the file
      {
         System.out.println("readPlayerFromFile error:"+ex);
         return null;
      }			
   }
   
   //post: writes out all Player information into a file
   public static boolean writeOutPlayerToFile(String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
         if(CultimaPanel.ENCODE_FILES)
            writer.println(Utilities.rollDie(1,1000)*2);    //starting with an even number means that we want to encode the strings out to the file
         else
            writer.println((Utilities.rollDie(1,1000)*2)+1);
         writer.println(encode(CultimaPanel.player.toString(), CultimaPanel.ENCODE_FILES));
         writer.println(encode(CultimaPanel.days + "     **days", CultimaPanel.ENCODE_FILES));
         double roundedTime = ((int)(CultimaPanel.time * 100))/100.0;
         writer.println(encode(roundedTime + "     ***time", CultimaPanel.ENCODE_FILES));
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeOutPlayerToFile error:"+ex);
         return false;
      }
      return true;
   }
   
//***************Wardrobe Inventory
   public static void readWardrobeInventory(String fileName)
   {
      try
      { 
         Scanner input = new Scanner(new FileReader(fileName));
         while(input.hasNextLine())
         {
            String line = input.nextLine().trim();  //read in name, frequency
            String [] parts = line.split(",");
            if(parts.length != 2)
               continue;
            String name = parts[0].trim();   
            if(!wordIsInt(parts[1].trim()))
               continue;
            int freq = Integer.parseInt(parts[1]); 
         
            Weapon weap = Weapon.getWeaponWithName(name);
            if(weap != null)
            {
               CultimaPanel.wardrobeInventory.add(weap);  
               CultimaPanel.wardrobeFreq.add(freq);  
            }
            else
            {
               Armor arm = Armor.getArmorWithName(name);
               if(arm != null)
               {
                  CultimaPanel.wardrobeInventory.add(arm);  
                  CultimaPanel.wardrobeFreq.add(freq);  
               }
               else
               {
                  Potion ptn = Potion.getPotionWithName(name);
                  if(ptn != null)
                  {
                     CultimaPanel.wardrobeInventory.add(ptn);  
                     CultimaPanel.wardrobeFreq.add(freq);  
                  }   
                  else
                  {
                     Item itm = Item.getItemWithName(name);
                     if(itm != null)
                     {
                        CultimaPanel.wardrobeInventory.add(itm);  
                        CultimaPanel.wardrobeFreq.add(freq);  
                     }
                  }
               }
            }   
         }
         input.close();    
      }
      catch (IOException ex)			//file is corrupted or doesn't exist
      {
         System.out.println("readWardrobeInventory error:"+ex);
      }			
   }
   
   public static boolean writeOutWardrobeInventory(String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
         for(int i=0; i<CultimaPanel.wardrobeInventory.size() && i<CultimaPanel.wardrobeFreq.size(); i++)
            writer.println(CultimaPanel.wardrobeInventory.get(i).getName()+","+CultimaPanel.wardrobeFreq.get(i));
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeOutWardrobeInventory error:"+ex);
         return false;
      }
      return true;
   }

//****************Missions (all and those underway)
   public static LinkedList<Mission> readMissionStack(String fileName)
   {
      try{
         Scanner input = new Scanner(new FileReader(fileName));
         LinkedList<Mission> missionStack = new LinkedList<Mission>();
         CultimaPanel.missionsGiven = new byte[Mission.NUM_MISSIONS];
         boolean encoded = false;
         String type = "";
         byte missionType = -1;
         String [] story = {"","",""};
         int worldRow = -1, worldCol = -1;
         int day = -1;
         int gold = 0;
         Item item = null;
         String clearLoc = "";
         Item target = null;
         String targetHolder = "";
         int targetRow=-1, targetCol=-1;
         Mission mission = null;
         if(input.hasNextLine())
         {
            String line = input.nextLine().trim();       //read in whether or not the file is encoded, and needs to be decoded
            int pos = line.indexOf("**");                //this will be a random integer.  If it is even, then the file is encoded.
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            int enc = 0;
            if(wordIsInt(line))
               enc = Integer.parseInt(line);
            if(enc % 2 == 0)
               encoded = true;  
         }
         if(input.hasNextLine())                         //read in missionsGiven array
         {
            String line = decode(input.nextLine().trim(), encoded);       //read in mission types given, 0 or 1 for false or true
            int pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            String [] parts = line.split(" ");
            if(parts.length == Mission.NUM_MISSIONS)
            {
               for(int i=0; i<parts.length; i++)
               {
                  String s = parts[i].trim();
                  if(!wordIsInt(s))
                     break;
                  CultimaPanel.missionsGiven[i] = Byte.parseByte(s);
               }
            }      
         }
         while(input.hasNextLine())
         { 
            String line = decode(input.nextLine().trim(), encoded);       //read in mission type
            if(line.startsWith("**"))
               continue;
            int pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();       
            type = line;
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in byte missionType
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            missionType = -1;
            if(wordIsInt(line))
               missionType = Byte.parseByte(line);
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in startStory
            if(line.startsWith("**"))
               continue;
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();       
            String startStory = line;
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in middleStory
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();   
            String middleStory = line;
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in endStory
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();   
            String endStory = line;
            story[0] = startStory;
            story[1] = middleStory;
            story[2] = endStory;
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in worldRow, worldCol
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            line = line.substring(1, line.length()-1);
            String [] parts = line.split(",");
            if(wordIsInt(parts[0].trim()) && wordIsInt(parts[1].trim()))
            { 
               worldRow = Integer.parseInt(parts[0].trim());
               worldCol = Integer.parseInt(parts[1].trim());
            } 
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in day
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            day = -1;
            if(wordIsInt(line))
               day = Integer.parseInt(line);
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in gold
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            gold = 0;
            if(wordIsInt(line))
               gold = Integer.parseInt(line);
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in item
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            String itemInfo = line.trim();
            item = null;
            if(!itemInfo.equals("null"))
            {
               parts = itemInfo.split(":");
               if(wordIsInt(parts[1].trim()))
               {
                  item = new Item(parts[0].trim(), Integer.parseInt(parts[1].trim()));
               }
            } 
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in clearLoc
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();   
            clearLoc = line.trim();
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in target
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            itemInfo = line.trim();
            target = null;
            if(!itemInfo.equals("null"))
            {
               parts = itemInfo.split(":");
               if(wordIsInt(parts[1].trim()))
               {
                  target = new Item(parts[0].trim(), Integer.parseInt(parts[1].trim()));
               }
            }
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in targetHolder
            if(line.startsWith("**"))
               continue;
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();      
            targetHolder = line;
            String missType = type.toLowerCase();
            if(missType.contains("collect") || missType.contains("frighten") || missType.contains("assassinate") || missType.contains("marry"))
               CultimaPanel.missionTargetNames.add(targetHolder);
            if(!input.hasNextLine())
               break;
         
            line = decode(input.nextLine().trim(), encoded);              //read in targetRow, targetCol
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            line = line.substring(1, line.length()-1);
            parts = line.split(",");
            if(wordIsInt(parts[0].trim()) && wordIsInt(parts[1].trim()))
            { 
               targetRow = Integer.parseInt(parts[0].trim());
               targetCol = Integer.parseInt(parts[1].trim());
            }
         
            line = decode(input.nextLine().trim(), encoded);              //read in failed
            pos = line.indexOf("**");
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            if(!line.equals("true") && !line.equals("false"))
               break;    
            boolean failed = Boolean.parseBoolean(line);
         
         //make new mission, add it to the missionStack
            mission = new Mission(type, story, worldRow, worldCol, gold, item, clearLoc, target, targetHolder, targetRow, targetCol, failed, missionType, day);
            missionStack.add(mission);
         }
         input.close();
         return missionStack;
      }
      catch (IOException ex)			//file is corrupted or doesn't exist - remake the file
      {
         System.out.println("readMissionStack error:"+ex);
         return new LinkedList<Mission>();
      }		
   }

   public static boolean writeOutMissionStack(LinkedList<Mission> missionStack, String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
         int missionNum = 0;
         if(CultimaPanel.ENCODE_FILES)
            writer.println(Utilities.rollDie(1,1000)*2);    //starting with an even number means that we want to encode the strings out to the file
         else
            writer.println((Utilities.rollDie(1,1000)*2)+1);
         for(byte num:CultimaPanel.missionsGiven)
            writer.print(num+" ");
         writer.println("**missionsGiven");   
         for(Mission m: missionStack)
         {
            writer.println(encode("******************Mission "+missionNum, CultimaPanel.ENCODE_FILES));
            missionNum++;
            writer.println(encode(m.toString(), CultimaPanel.ENCODE_FILES));
         }
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeOutMissionStack error:"+ex);
         return false;
      }
      return true;
   }

//***************Locations file
//reads in all Location information on a map into an ArrayList of Locations for CultimaPanel.allLocations.  Expecting the format to be:
//<name>:<row>:<col>:<Terrain mapIndex>:<Terrain value>:<teleporter>, where teleporter is "(toIndex,toRow,toCol)"
   public static ArrayList<Location> readLocations(String fileName)
   {
      try
      { 
         Scanner input = new Scanner(new FileReader(fileName));
         ArrayList<Location> ans = new ArrayList<Location>();
         while(input.hasNextLine())
         {
            String name="";
            int row=0, col=0, fromMapIndex=0, mapIndex=0;
            byte value=0;
            int toIndex=0, toRow=0, toCol=0;
            String locType = "?";
         
            String line = input.nextLine().trim();  //read in name
         
            if(line.startsWith("**"))
               continue;
         
            String [] parts = line.split(":");
            if(parts.length != 9)   //something went wrong
               continue;
            name = parts[0].trim();                //NAME
         
            if(!wordIsInt(parts[1]))
               continue;
            row = Integer.parseInt(parts[1]);      //row
         
            if(!wordIsInt(parts[2]))
               continue;
            col = Integer.parseInt(parts[2]);      //col
         
            if(!wordIsInt(parts[3]))
               continue;
            fromMapIndex = Integer.parseInt(parts[3]); //fromMapIndex
         
            if(!wordIsInt(parts[4]))
               continue;
            mapIndex = Integer.parseInt(parts[4]); //toMapIndex
         
            if(!wordIsInt(parts[5]))
               continue;
            value = Byte.parseByte(parts[5]);      //value
         
            Teleporter tele = null;
            String teleInfo = parts[6];
            if(!teleInfo.equals("null"))
            {
               if(teleInfo.length()<7 || teleInfo.charAt(0)!='(' || teleInfo.charAt(teleInfo.length()-1)!=')')
                  continue;
            
               teleInfo = teleInfo.substring(1,teleInfo.length()-1);  //remove () on each side
               String [] coord = teleInfo.split(" ");
               if(coord.length != 4)
                  continue;
               if(!wordIsInt(coord[0]))
                  continue;
               toIndex = Integer.parseInt(coord[0]);     //toIndex
               if(!wordIsInt(coord[1]))
                  continue;
               toRow = Integer.parseInt(coord[1]);       //toRow
               if(!wordIsInt(coord[2]))
                  continue;
               toCol = Integer.parseInt(coord[2]);       //toCol  
               locType = coord[3].trim();
            //public Teleporter(int ti, int tr, int tc)
               tele = new Teleporter(toIndex, toRow, toCol, locType);
            }
         
            String mf = parts[7];
            ArrayList<Coord>monsterFreq = new ArrayList<Coord>();
            if(mf.length() < 2 || !mf.startsWith("[") || !mf.endsWith("]"))
               continue;
            mf = mf.substring(1,mf.length()-1);  //remove () on each side
            if(mf.length() > 0)
            {
               String [] points = mf.split(", ");
               for(String p: points)
               {
                  int xStart = p.indexOf("[x=");
                  int xEnd = p.indexOf(",y=");
                  int yEnd = p.indexOf("]");
                  if(xStart == -1 || xEnd == -1 || yEnd == -1)
                     continue;
                  String xWord = p.substring(xStart+3, xEnd);
                  String yWord = p.substring(xEnd+3, yEnd);
                  if(!wordIsInt(xWord) || !wordIsInt(yWord))
                     continue;
                  int x = Integer.parseInt(xWord);
                  int y = Integer.parseInt(yWord);
                  Coord toAdd = new Coord(x, y);
                  monsterFreq.add(toAdd);
               }
            }
            
            String rp = parts[8];
            ArrayList<Coord>riddlePoints = new ArrayList<Coord>();
            if(rp.length() < 2 || !rp.startsWith("[") || !rp.endsWith("]"))
               continue;
            rp = rp.substring(1,rp.length()-1);  //remove () on each side
            if(rp.length() > 0)
            {
               String [] points = rp.split(", ");
               for(String p: points)
               {
                  int xStart = p.indexOf("[x=");
                  int xEnd = p.indexOf(",y=");
                  int yEnd = p.indexOf("]");
                  if(xStart == -1 || xEnd == -1 || yEnd == -1)
                     continue;
                  String xWord = p.substring(xStart+3, xEnd);
                  String yWord = p.substring(xEnd+3, yEnd);
                  if(!wordIsInt(xWord) || !wordIsInt(yWord))
                     continue;
                  int x = Integer.parseInt(xWord);
                  int y = Integer.parseInt(yWord);
                  Coord toAdd = new Coord(x, y);
                  riddlePoints.add(toAdd);
               }
            }
         
         //public Terrain(String name, ArrayList<String> fileNames, int ad, HashMap<String, Integer> nei, String d,  int mi, byte v)
            Terrain ter =  TerrainBuilder.getTerrainWithValue(value);
         //public Location(String n, int r, int c, Terrain t, Teleporter tele) 
            Location loc = new Location(name, row, col, fromMapIndex, ter, tele);
            loc.setMapIndex(mapIndex);
            loc.setMonsterFreq(monsterFreq);
            loc.setRiddlePoints(riddlePoints);
            String locTerType = TerrainBuilder.locationTerrainType(ter);
            if(locTerType.equals("town"))
               CultimaPanel.allDomiciles.add(loc);
            else if (locTerType.equals("adventure"))  
               CultimaPanel.allAdventure.add(loc);
            else if (locTerType.equals("temple"))  
               CultimaPanel.allTemples.add(loc);
            else if (locTerType.equals("battlefield"))  
               CultimaPanel.allBattlefields.add(loc);
            ans.add(loc);
         }
         input.close();
         return ans;
      }
      catch (IOException ex)			//file is corrupted or doesn't exist - clear high scores and remake the file
      {
         System.out.println("readLocations error:"+ex);
         return null;
      }			
   }

//writes out all Location information on a map into a file in the format:
//<name>:<row>:<col>:<Terrain mapIndex>:<Terrain value>:<teleporter>, where teleporter is "(toIndex,toRow,toCol)"
   public static boolean writeOutLocationsToFile(ArrayList<Location>allLocations, String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
         writer.println("** World Map Location info");
         writer.println("** name : row : col : fromMapIndex : getMapIndex : terrain : teleporter : monsterFrequencies : riddlePoints");
         for(Location loc: allLocations)
         {
            String line = loc.getName()+":"+loc.getRow()+":"+loc.getCol()+":"+loc.getFromMapIndex()+":"+loc.getMapIndex()+":"+(loc.getTerrain()).getValue()+":"+loc.getTeleporter()+":"+loc.getMonsterFreq()+":"+loc.getRiddlePoints();
            line = line.replace("java.awt.Point","");
            writer.println(line);
         }
         writer.println();
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeOutLocationsToFile error:"+ex);
         return false;
      }
      return true;
   }

//******************NPC Civilians
//read in all NPC civilians into an ArrayList of ArrayLists, indexed by which location index they live in  
   public static void readAllNPCFile(ArrayList<ArrayList<NPCPlayer>> ans, String fileName)
   {
      try
      { 
         Scanner input = new Scanner(new FileReader(fileName));
         boolean encoded = false;
         if(input.hasNextLine())
         {
            String line = input.nextLine().trim();       //read in whether or not the file is encoded, and needs to be decoded
            int pos = line.indexOf("**");                //this will be a random integer.  If it is even, then the file is encoded.
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            int enc = 0;
            if(wordIsInt(line))
               enc = Integer.parseInt(line);
            if(enc % 2 == 0)
               encoded = true;  
         }
         while(input.hasNextLine())
         {
            String name="";
            byte charIndex = (byte)(-1);
            int mapIndex=0;
            int row=0, col=0;
            int homeRow=0, homeCol=0;
            char maritalStatus='N';
            String missionInfo = "none";
            int might=0, mind=0, agility=0;
            int body = 0, reputation = 0, bounty = 0;
            boolean hasMet = false;
            int restoreDay = -1;
            int endChaseDay = -1;
            byte moveType = NPC.STILL;
            String locationType="";
            int numInfo = 0;
            String weaponName = "";
            Weapon weapon = null;
            String armorName = "";
            Armor armor = null;
            ArrayList<Item> items = new ArrayList<Item>();
            boolean hasTrained = false;
            int gold = 0;
            String effect="none";
         
            String line = decode(input.nextLine().trim(), encoded);  //read in name
            if(line.startsWith("**"))
               continue;
            String [] parts = line.split(",");
            if(parts.length != 26)                          //something went wrong
               continue;
            name = parts[0].trim();                         //NAME
         
            if(!wordIsInt(parts[1]))                        //charIndex 
               continue;
            charIndex = Byte.parseByte(parts[1]); 
         
            if(!wordIsInt(parts[2]))                        //mapIndex 
               continue;
            mapIndex = Integer.parseInt(parts[2]);
         
            if(!wordIsInt(parts[3]))                        //row    
               continue;
            row = Integer.parseInt(parts[3]);      
         
            if(!wordIsInt(parts[4]))                        //col
               continue;
            col = Integer.parseInt(parts[4]);      
         
            if(!wordIsInt(parts[5]))                        //homeRow    
               continue;
            homeRow = Integer.parseInt(parts[5]);      
         
            if(!wordIsInt(parts[6]))                        //homeCol
               continue;
            homeCol = Integer.parseInt(parts[6]);      
         
            missionInfo = parts[7].trim();                  //missionInfo
         
            if(!wordIsInt(parts[8]))                        //might
               continue;
            might = Integer.parseInt(parts[8]); 
         
            if(!wordIsInt(parts[9]))                        //mind
               continue;
            mind = Integer.parseInt(parts[9]);
         
            if(!wordIsInt(parts[10]))                        //agility
               continue;
            agility = Integer.parseInt(parts[10]);
         
            if(!wordIsInt(parts[11]))                        //body
               continue;
            body = Integer.parseInt(parts[11]);
         
            if(!wordIsInt(parts[12]))                        //reputation
               continue;
            reputation = Integer.parseInt(parts[12]);
         
            if(!parts[13].equals("true") && !parts[13].equals("false"))                        
               parts[13]="false";
            hasMet = Boolean.parseBoolean(parts[13]);
         
            if(!wordIsInt(parts[14]))                        //restoreDay
               continue;
            restoreDay = Integer.parseInt(parts[14]);
         
            if(!wordIsInt(parts[15]))                          //endChaseDay
               continue;
            endChaseDay = Integer.parseInt(parts[15]);
         
            if(!wordIsInt(parts[16]))                          //moveType
               continue;
            moveType = Byte.parseByte(parts[16]);
         
            locationType = parts[17].trim();                   //locationType
         
            if(!wordIsInt(parts[18]))                          //numInfo
               continue;
            numInfo = Integer.parseInt(parts[18]);
         
            weaponName = parts[19].trim();                     //weapon
            weapon = Weapon.getWeaponWithName(weaponName);
         
            armorName = parts[20].trim();                      //armor
            armor = Armor.getArmorWithName(armorName);
               
            String it = parts[21].trim();                   //items
            if(it.length()<2)
               continue;
            it = it.substring(1, it.length()-1);            //remove the [] on each side
            String[] itemParts = it.split(" ");
            for(String ip: itemParts)
            {
               if(!ip.contains(":"))
                  continue;
               String[] temp = ip.split(":");
               if(temp.length != 2)
                  continue;
               String itemName = temp[0].trim();
               if(!wordIsInt(temp[1]))
                  continue;
               int itemValue = Integer.parseInt(temp[1]);
               items.add(new Item(itemName, itemValue));
            }
         
            if(!parts[22].equals("true") && !parts[22].equals("false"))                        
               parts[22]="false";
            hasTrained = Boolean.parseBoolean(parts[22]);
         
            if(!wordIsInt(parts[23]))                        //gold
               parts[23]="0";
            gold = Integer.parseInt(parts[23]);
         
            effect = parts[24].trim();                       //effect
         
            maritalStatus = (parts[25].trim().charAt(0));  //maritalStatus
         
         //public NPCPlayer(byte index, int r, int c, int mi, String loc, int animDelay)
            NPCPlayer p = new NPCPlayer(charIndex, row, col, mapIndex, homeRow, homeCol,"");
            p.setName(name);
            p.setMissionInfo(missionInfo);
            p.setMight(might);
            p.setMind(mind);
            p.setAgility(agility);
            p.setBody(body);
            p.setReputation(reputation);
            p.setHasMet(hasMet);
            p.setRestoreDay(restoreDay);
            p.setEndChaseDay(endChaseDay);
            p.setMoveType(moveType);
            if(endChaseDay!=-1 && endChaseDay >= CultimaPanel.days)
               p.setMoveTypeTemp(NPC.CHASE);
            p.setLocationType(locationType);
            p.setNumInfo(numInfo);
            p.setWeapon(weapon);
            p.setArmor(armor);
            p.setItems(items);
            p.setHasTrained(hasTrained);
            p.setGold(gold);
            if(!effect.equals("none"))
               p.addEffect(effect);
            p.setMaritalStatus(maritalStatus);   
            while(mapIndex >= ans.size())
               ans.add(new ArrayList<NPCPlayer>());
         
            ans.get(mapIndex).add(p);
         }
         input.close();
      }
      catch (IOException ex)			//file is corrupted or doesn't exist
      {
         System.out.println("readAllNPCFile error:"+ex);
         return;
      }			
   }

   public static boolean writeOutAllNPCFile(ArrayList<ArrayList<NPCPlayer>> collection, String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
         if(CultimaPanel.ENCODE_FILES)
            writer.println(Utilities.rollDie(1,1000)*2);    //starting with an even number means that we want to encode the strings out to the file
         else
            writer.println((Utilities.rollDie(1,1000)*2)+1);
         writer.println(encode("**name,charIndex,mapIndex,row,col,homeRow,homeCol,missionIndex,might,mind,agility,body,reputation,hasMet,restoreDay,endChaseDay,origMoveType,locationType,numInfo,weapon,armor,items,gold,effect,maritalStatus", CultimaPanel.ENCODE_FILES));
         for(ArrayList<NPCPlayer> c:collection)   
            for(NPCPlayer p: c)
               writer.println(encode(p.toString(), CultimaPanel.ENCODE_FILES));
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeOutAllNPCFile error:"+ex);
         return false;
      }
      return true;
   }

//read in world-monsters and NPCs to restore into a single arraylist
   public static ArrayList<NPCPlayer> readNPCFile(String fileName)
   {
      try
      { 
         Scanner input = new Scanner(new FileReader(fileName));
         ArrayList<NPCPlayer> ans = new ArrayList<NPCPlayer>();
         boolean encoded = false;
         if(input.hasNextLine())
         {
            String line = input.nextLine().trim();       //read in whether or not the file is encoded, and needs to be decoded
            int pos = line.indexOf("**");                //this will be a random integer.  If it is even, then the file is encoded.
            if(pos >= 0)
               line = line.substring(0,pos).trim();
            int enc = 0;
            if(wordIsInt(line))
               enc = Integer.parseInt(line);
            if(enc % 2 == 0)
               encoded = true;  
         }
         while(input.hasNextLine())
         {
            String name="";
            byte charIndex = (byte)(-1);
            int mapIndex=0;
            int row=0, col=0;
            int homeRow=0, homeCol=0;
            String missionInfo = "none";
            int might=0, mind=0, agility=0;
            int body = 0, reputation = 0;
            boolean hasMet = false;
            int restoreDay = -1;
            int endChaseDay = -1;
            byte moveType = NPC.STILL;
            String locationType="";
            int numInfo = 0;
            String weaponName = "";
            Weapon weapon = null;
            String armorName = "";
            Armor armor = null;
            ArrayList<Item>items = new ArrayList<Item>();
            boolean hasTrained = false;
            int gold = 0;
            String effect = "none";
            char maritalStatus = 'N';
            String line = decode(input.nextLine().trim(), encoded);  //read in name
            String [] parts = line.split(",");
            if(parts.length != 26)                  //something went wrong
               continue;
         
            name = parts[0].trim();                         //NAME
         
            if(!wordIsInt(parts[1]))                        //charIndex 
               continue;
            charIndex = Byte.parseByte(parts[1]); 
         
            if(!wordIsInt(parts[2]))                        //mapIndex 
               continue;
            mapIndex = Integer.parseInt(parts[2]);
         
            if(!wordIsInt(parts[3]))                        //row    
               continue;
            row = Integer.parseInt(parts[3]);      
         
            if(!wordIsInt(parts[4]))                        //col
               continue;
            col = Integer.parseInt(parts[4]);      
         
            if(!wordIsInt(parts[5]))                        //homeRow    
               continue;
            homeRow = Integer.parseInt(parts[5]);      
         
            if(!wordIsInt(parts[6]))                        //homeCol
               continue;
            homeCol = Integer.parseInt(parts[6]);      
         
            missionInfo = parts[7].trim();                  //missionInfo 
         
            if(!wordIsInt(parts[8]))                        //might
               continue;
            might = Integer.parseInt(parts[8]); 
         
            if(!wordIsInt(parts[9]))                        //mind
               continue;
            mind = Integer.parseInt(parts[9]);
         
            if(!wordIsInt(parts[10]))                        //agility
               continue;
            agility = Integer.parseInt(parts[10]);
         
            if(!wordIsInt(parts[11]))                        //body
               continue;
            body = Integer.parseInt(parts[11]);
         
            if(!wordIsInt(parts[12]))                        //reputation
               continue;
            reputation = Integer.parseInt(parts[12]);
         
            if(!parts[13].equals("true") & !parts[13].equals("false"))                        
               parts[13]="false";
            hasMet = Boolean.parseBoolean(parts[13]);
         
            if(!wordIsInt(parts[14]))                        //restoreDay
               continue;
            restoreDay = Integer.parseInt(parts[14]);
         
            if(!wordIsInt(parts[15]))                        //endChaseDay
               continue;
            endChaseDay = Integer.parseInt(parts[15]);
         
            if(!wordIsInt(parts[16]))                         //moveType
               continue;
            moveType = Byte.parseByte(parts[16]);
         
            locationType = parts[17].trim();                 //locationType
         
            if(!wordIsInt(parts[18]))                        //numInfo
               continue;
            numInfo = Integer.parseInt(parts[18]);
         
            weaponName = parts[19].trim();                     //weapon
            weapon = Weapon.getWeaponWithName(weaponName);
         
            armorName = parts[20].trim();                      //armor
            armor = Armor.getArmorWithName(armorName);
             
            String it = parts[21].trim();                   //items
            if(it.length()<2)
               continue;
            it = it.substring(1, it.length()-1);            //remove the [] on each side
            String[] itemParts = it.split(" ");
            for(String ip: itemParts)
            {
               if(!ip.contains(":"))
                  continue;
               String[] temp = ip.split(":");
               if(temp.length != 2)
                  continue;
               String itemName = temp[0].trim();
               if(!wordIsInt(temp[1]))
                  continue;
               int itemValue = Integer.parseInt(temp[1]);
               items.add(new Item(itemName, itemValue));
            }
         
            if(!parts[22].equals("true") && !parts[22].equals("false"))                        
               parts[22]="false";
            hasTrained = Boolean.parseBoolean(parts[22]);
         
            if(!wordIsInt(parts[23]))                        //gold
               parts[23]="0";
            gold = Integer.parseInt(parts[23]);
              
            effect = parts[24].trim();                         //effect
                  
            maritalStatus = (parts[25].trim().charAt(0));  //maritalStatus
         
                     
         //public NPCPlayer(byte index, int r, int c, int mi, String loc, int animDelay)
            NPCPlayer p = new NPCPlayer(charIndex, row, col, mapIndex, homeRow, homeCol,"");
            p.setName(name);
            p.setMissionInfo(missionInfo);
            p.setMight(might);
            p.setMind(mind);
            p.setAgility(agility);
            p.setBody(body);
            p.setReputation(reputation);
            p.setHasMet(hasMet);
            p.setRestoreDay(restoreDay);
            p.setEndChaseDay(endChaseDay);
            p.setMoveType(moveType);
            if(endChaseDay!=-1 && endChaseDay >= CultimaPanel.days)
               p.setMoveTypeTemp(NPC.CHASE);
            p.setLocationType(locationType);
            p.setNumInfo(numInfo);
            p.setWeapon(weapon);
            p.setArmor(armor);
            p.setItems(items);
            p.setHasTrained(hasTrained);
            p.setGold(gold);
            ans.add(p);
            if(!effect.equals("none"))
               p.addEffect(effect);
            p.setMaritalStatus(maritalStatus);
         }
         input.close();
         return ans;
      }
      catch (IOException ex)			//file is corrupted or doesn't exist - clear high scores and remake the file
      {
         System.out.println("readNPCFile error:"+ex);
         return new ArrayList<NPCPlayer>();
      }			
   }

   public static boolean writeOutNPCFile(ArrayList<NPCPlayer> collection, String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
         if(CultimaPanel.ENCODE_FILES)
            writer.println(Utilities.rollDie(1,1000)*2);    //starting with an even number means that we want to encode the strings out to the file
         else
            writer.println((Utilities.rollDie(1,1000)*2)+1);
         writer.println(encode("**name,charIndex,mapIndex,row,col,homeRow,homeCol,missionIndex,might,mind,agility,body,reputation,hasMet,restoreDay,endChaseDay,origMoveType,locationType,numInfo,weapon,armor,items,gold,effect,maritalStatus", CultimaPanel.ENCODE_FILES));
         for(NPCPlayer c:collection)   
            writer.println(encode(c.toString(), CultimaPanel.ENCODE_FILES));
      
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeOutNPCFile error:"+ex);
         return false;
      }
      return true;
   }

//*****************Doors file
//writes out all Doors to close information into a file
   public static boolean writeOutDoorsToFile(String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
      
         for(RestoreItem d:CultimaPanel.doorsToClose)   
            writer.println(d);
      
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeOutDoorsToFile error:"+ex);
         return false;
      }
      return true;
   }

   public static LinkedList<RestoreItem> readDoorsFile(String fileName)
   {  //Door in the form of (mapIndex row col value)
      try
      { 
         Scanner input = new Scanner(new FileReader(fileName));
         LinkedList<RestoreItem> ans = new LinkedList<RestoreItem>();
         while(input.hasNextLine())
         {
            int mapIndex=0, row=0, col=0;
            byte value=0;
            int days=0;
         
            String line = input.nextLine().trim();  //read in name
            if(line.length()<11 || line.charAt(0)!='(' || line.charAt(line.length()-1)!=')')
               continue;
         
            line = line.substring(1,line.length()-1);  //remove () on each side
         
            String [] coord = line.split(" ");
            if(coord.length != 5)
               continue;
            if(!wordIsInt(coord[0]))
               continue;
            mapIndex = Integer.parseInt(coord[0]);    
            if(!wordIsInt(coord[1]))
               continue;
            row = Integer.parseInt(coord[1]);      
            if(!wordIsInt(coord[2]))
               continue;
            col = Integer.parseInt(coord[2]);  
            if(!wordIsInt(coord[3]))
               continue;
            value = Byte.parseByte(coord[3]); 
            if(!wordIsInt(coord[4]))
               continue;
            days = Integer.parseInt(coord[4]); 
         
            ans.push(new RestoreItem(mapIndex, row, col, value));
         }
         input.close();    
         return ans;
      }
      catch (IOException ex)			//file is corrupted or doesn't exist
      {
         System.out.println("readDoorsFile error:"+ex);
         return new LinkedList<RestoreItem>();
      }			
   }

//****************world tiles, barrels and egg restore files****************************************
//writes out all chests to restore information into a file
   public static boolean writeTilesToRestore(ArrayList<RestoreItem> toRestore, String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
      
         for(RestoreItem d:toRestore)   
            writer.println(d);
      
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeTilesToRestore error:"+ex);
         return false;
      }
      return true;
   }

   public static ArrayList<RestoreItem> readTilesToRestore(String fileName)
   {  //Chests to restore in the form of (mapIndex row col value)
      try
      { 
         Scanner input = new Scanner(new FileReader(fileName));
         ArrayList<RestoreItem> ans = new ArrayList<RestoreItem>();
         while(input.hasNextLine())
         {
            int mapIndex=0, row=0, col=0;
            byte value=0;
            int days=0;
         
            String line = input.nextLine().trim();  //read in name
            if(line.length()<11 || line.charAt(0)!='(' || line.charAt(line.length()-1)!=')')
               continue;
         
            line = line.substring(1,line.length()-1);  //remove () on each side
         
            String [] coord = line.split(" ");
            if(coord.length != 5)
               continue;
            if(!wordIsInt(coord[0]))
               continue;
            mapIndex = Integer.parseInt(coord[0]);    
            if(!wordIsInt(coord[1]))
               continue;
            row = Integer.parseInt(coord[1]);      
            if(!wordIsInt(coord[2]))
               continue;
            col = Integer.parseInt(coord[2]);  
            if(!wordIsInt(coord[3]))
               continue;
            value = Byte.parseByte(coord[3]); 
            if(!wordIsInt(coord[4]))
               continue;
            days = Integer.parseInt(coord[4]); 
            ans.add(new RestoreItem(mapIndex, row, col, value, days));
         }
         input.close();     
         return ans;
      }
      catch (IOException ex)			//file is corrupted or doesn't exist
      {
         System.out.println(fileName+"restore file error:"+ex);
         return new ArrayList<RestoreItem>();
      }			
   }
   
//********************Map file
   public static boolean readMapFromBinFile(String fileName, byte[][] board)
   {
   //Files.readAllBytes   java.nio.file.Files
   
      int r = 0;
      int c = 0;
   
      try
      {
         FileInputStream fileIs = new FileInputStream(fileName);
         ObjectInputStream is = new ObjectInputStream(fileIs);
         while (r<board.length && c<board[0].length)
         {
            boolean found = false;
            byte value = is.readByte();
            board[r][c] = value;
            c++;
            if(c >= board[0].length)
            {
               c = 0;
               r++;
            }
         
         }
         is.close();
      }
      catch (FileNotFoundException e)
      {
         System.out.println("readMapFromBinFile file not found");
         return false;
      }
      catch (IOException ex)
      {
         System.out.println("readMapFromBinFile failed to read past "+r+":"+c+" with error:"+ex);
         return false;
      }
      return true;
   }

   public static boolean writeToBinFile(String fileName, byte[][] board)
   {
      int r=0, c=0;
      try
      {
         FileOutputStream fileOs = new FileOutputStream(fileName);
         ObjectOutputStream os = new ObjectOutputStream(fileOs);
         for( r = 0; r < board.length; r++)
            for( c = 0; c < board[0].length; c++)
            {
               byte current = board[r][c];
               os.writeByte(current);
            }
      //FLUSH 
         os.flush();
         os.close();
         fileOs.flush();
         fileOs.close();
      
      }
      catch (FileNotFoundException e)
      {
         System.out.println("writeToBinFile file not found");
         return false;
      }
      catch (IOException ex)
      {
         System.out.println("writeToBinFile failed to close properly with error:"+ex);
         return false;
      }
      return true;
   }

//***************Map File Names
   public static ArrayList<String> readMapFileNames(String fileName)
   {
      ArrayList<String> fileNames = new ArrayList<String>();
      try
      { 
         Scanner input = new Scanner(new FileReader(fileName));
         while(input.hasNextLine())
         {
            String current = input.nextLine();
            File imageFile = new File(current);
            if(imageFile.exists())		      
               fileNames.add(current);
         }
         input.close();
      }
      catch (IOException ex)			//file is corrupted or doesn't exist - clear and remake the file
      {
         System.out.println("readMapFileNames error:"+ex);
         return new ArrayList<String>();
      }			
      return fileNames;
   
   }

   public static boolean writeOutMapFileNames(ArrayList<String>mapFileNames, String fileName)
   {
      File imageFile = new File(fileName);
      if(imageFile.exists())		//make sure to write over old file
      {
         imageFile.delete();
         imageFile = new File(fileName);
      }
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(imageFile);
         for(String filename: mapFileNames)
         {
            writer.println(filename);
         }
         writer.close();
      }
      catch(Exception ex){
         System.out.println("writeOutMapFileNames error:"+ex);
         return false;
      }
      return true;
   }

   public static void saveProgress()
   {
      if(CultimaPanel.player.getMapIndex()==1 && CultimaPanel.player.isInSpecialRealm())
         return;
      if(CultimaPanel.player.getBody() > 0)
      {
         FileManager.writeToBinFile("maps/world.bin", (CultimaPanel.map.get(0)));  
         FileManager.writeOutPlayerToFile("data/player.txt");
         FileManager.writeOutWardrobeInventory("data/wardrobe.txt");
         FileManager.writeOutDoorsToFile("maps/doors.txt");
         FileManager.writeTilesToRestore(CultimaPanel.tilesToRestore, "maps/worldRestore.txt");
         FileManager.writeTilesToRestore(CultimaPanel.eggsToHarvest, "maps/harvestRestore.txt");
         FileManager.writeTilesToRestore(CultimaPanel.barrelsToRestore, "maps/barrelRestore.txt");
         FileManager.writeOutAllNPCFile(CultimaPanel.civilians, "maps/civilians.txt");
         FileManager.writeOutAllNPCFile(CultimaPanel.furniture, "maps/furniture.txt");
         FileManager.writeOutNPCFile(CultimaPanel.NPCsToRestore, "maps/NPCRestore.txt");
         FileManager.writeOutNPCFile(CultimaPanel.worldMonsters, "maps/worldMonsters.txt");
         FileManager.writeOutNPCFile(CultimaPanel.boats, "maps/boats.txt");
         FileManager.writeOutLocationsToFile(CultimaPanel.allLocations, "maps/worldLocs.txt");
         FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
         int index = CultimaPanel.player.getMapIndex();
         if(index > 0)                                               //save the mapping progress and destroyed map-elements if we quit inside of a Location
         {
            String filename = "maps/"+index+"_"+(CultimaPanel.map.get(index)).length+"_"+(CultimaPanel.map.get(index))[0].length+".bin";
            FileManager.writeToBinFile(filename, (CultimaPanel.map.get(index))); 
         }   
      }
   }
   
	//pre:  "fileName" is the name of a real file containing lines of text - the first line intended to be unused
   //post:returns a String of all the lines in <filename>.txt for testing the readPlayerFromFile method
   public static String readFile(String fileName)
   {
      String ans = "";	
      try
      {	                  
         Scanner input = new Scanner(new FileReader(fileName));
         input.nextLine();                      //skip the first line in the file (used for encoding/decoding)
         while (input.hasNextLine())				//while there is another line in the file
         {
            String current = input.nextLine().trim(); //read in the next Line in the file and store it in the String
            if(!current.contains("**days") && !current.contains("**time"))
            {
               ans += current;
            }
            if(input.hasNextLine())
            {	                   
               ans += "\n";
            }
         }
         input.close();	
      }
      catch (IOException ex)			//file is corrupted or doesn't exist - remake the file
      {
         System.out.println("readFile error:"+ex);
      }
      return ans.trim();					
   }

  //pre: fromFile and fromYourMerhod are not null, Strings of data read in from multi-line files
  //post:returns the difference between the two strings, used for testing the readPlayerFromFile method
   public static LinkedList<String> findStringDifference(String fromFile, String fromYourMethod)
   {
      LinkedList<String> ans = new LinkedList<String>();
      String [] fromFileParts = fromFile.split("\n");
      String [] fromMethodParts = fromYourMethod.split("\n");
      for(int i=0; i<fromFileParts.length && i<fromMethodParts.length; i++)
      {
         if(!fromFileParts[i].equals(fromMethodParts[i]))
         {
            System.out.println("Line "+(i+2)+"   read as:"+fromMethodParts[i]);
            System.out.println("Line "+(i+2)+" should be:"+fromFileParts[i]);
         }
      }
      return ans;
   }
}
