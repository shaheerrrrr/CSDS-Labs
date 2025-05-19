import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.io.File;

public class CharacterBuilder
{
   protected static final byte NAME = 0;
   protected static final byte IMAGEICON = 1;
   protected static final byte SUGGEST_WEAPON = 2;
   protected static final byte MIGHT = 3;
   protected static final byte MIND = 4;
   protected static final byte AGILITY = 5;
   protected static final byte AWARENESS = 6;
   protected static final byte AFFLICTION = 7;
   protected static final byte RANDOM = 8;
   protected static final byte NUM_CHOICES = 9;
   
   private static final String [] choices = {
   "thy character's name",                                          /*NAME*/
   "thy character's weapon of choice",                              /*IMAGEICON*/
   "set to suggest weapon based on current attributes",             /*SUGGESTED_WEAPON*/
   "attack/defence of melee weapons, max health, 1 point",          /*MIGHT*/
   "quanitiy and quality of magic and resistence to, 1 point",      /*MIND*/
   "attack/defence of ranged weapons, speed, theft, 1 point",       /*AGILITY*/
   "mapping, trap detection, knowledge of others, 10 points",       /*AWARENESS*/
   "starting circumstances to overcome as a mission",               /*AFFLICTION*/
   "create a random character"                                      /*RANDOM*/
   };
   
   protected static final byte NONE = 0;
   protected static final byte BOUNTY = 1;
   protected static final byte FRAMED = 2;
   protected static final byte VAMPIRE = 3;
   protected static final byte WEREWOLF = 4;
   protected static final byte NUM_AFFLICTIONS = 5;
   
   private static final String [] afflictionTypes = {"none", "bounty", "framed", "vampire", "werewolf"};
   
   public static void scrollLeft()
   {
      if(CultimaPanel.selAttribute == NAME)
         CultimaPanel.CBname = Utilities.nameGenerator("character");
      else if(CultimaPanel.selAttribute == IMAGEICON)
      {
         CultimaPanel.CBimageIndex--;
         if(CultimaPanel.CBimageIndex==Player.THROWN)    //skip thrown as a weapon class (we all get that ability)
            CultimaPanel.CBimageIndex--;
         if(CultimaPanel.CBimageIndex<0)
            CultimaPanel.CBimageIndex = 0;
         CultimaPanel.clearMessage();
         CultimaPanel.sendMessage(Weapon.getTypeDescription(CultimaPanel.CBimageIndex));   
      }
      else if(CultimaPanel.selAttribute == SUGGEST_WEAPON)
      {
         CultimaPanel.CBimageIndex = getSuggestedCharacter(CultimaPanel.CBmight, CultimaPanel.CBmind, CultimaPanel.CBagility);
         CultimaPanel.clearMessage();
         CultimaPanel.sendMessage(Weapon.getTypeDescription(CultimaPanel.CBimageIndex));
      }
      else if(CultimaPanel.selAttribute == MIGHT)
      {
         if(CultimaPanel.CBPoints < CultimaPanel.CB_MAX_POINTS && CultimaPanel.CBmight>3)
         {
            CultimaPanel.CBmight--;
            CultimaPanel.CBPoints++;
         }
      } 
      else if(CultimaPanel.selAttribute == MIND)
      {
         if(CultimaPanel.CBPoints < CultimaPanel.CB_MAX_POINTS && CultimaPanel.CBmind>3)
         {
            CultimaPanel.CBmind--;
            CultimaPanel.CBPoints++;
            CultimaPanel.CBspellIndex1 = 0;
            CultimaPanel.CBspellIndex2 = 1;
         }
      }
      else if(CultimaPanel.selAttribute == AGILITY)
      {
         if(CultimaPanel.CBPoints < CultimaPanel.CB_MAX_POINTS && CultimaPanel.CBagility>3)
         {
            CultimaPanel.CBagility--;
            CultimaPanel.CBPoints++;
         }
      } 
      else if(CultimaPanel.selAttribute == AWARENESS)
      {
         if(CultimaPanel.CBPoints+10 <= CultimaPanel.CB_MAX_POINTS && CultimaPanel.CBawareness>0)
         {
            CultimaPanel.CBawareness--;
            CultimaPanel.CBPoints+=10;
         }
      }
      else if(CultimaPanel.selAttribute == AFFLICTION)
      {
         CultimaPanel.CBaffliction--;
         if(CultimaPanel.CBaffliction<0)
            CultimaPanel.CBaffliction = 0;
      } 
      else if(CultimaPanel.selAttribute == RANDOM)
      {
         CultimaPanel.CBname = Utilities.nameGenerator("character");
         int [] randomStats = Player.buildRandomStats();
         CultimaPanel.CBmight = randomStats[0];
         CultimaPanel.CBmind = randomStats[1];
         CultimaPanel.CBagility = randomStats[2];
         CultimaPanel.CBawareness = randomStats[3];
         CultimaPanel.CBPoints = 0;
         CultimaPanel.CBimageIndex = getSuggestedCharacter(CultimaPanel.CBmight, CultimaPanel.CBmind, CultimaPanel.CBagility);
         CultimaPanel.CBaffliction = NONE;
         if(Math.random() < 0.05)
         {
            CultimaPanel.CBaffliction = Utilities.rollDie(1, NUM_AFFLICTIONS-1);
         }
      }
   }
   
   public static void scrollRight()
   {
      if(CultimaPanel.selAttribute == NAME)
         CultimaPanel.CBname = Utilities.nameGenerator("character");
      else if(CultimaPanel.selAttribute == IMAGEICON)
      {
         CultimaPanel.CBimageIndex++;
         if(CultimaPanel.CBimageIndex==Player.THROWN)    //skip thrown as a weapon class (we all get that ability)
            CultimaPanel.CBimageIndex++;
         if(CultimaPanel.CBimageIndex>=Player.NUM_WEAPONS)
            CultimaPanel.CBimageIndex = Player.NUM_WEAPONS-1;
         CultimaPanel.clearMessage();
         CultimaPanel.sendMessage(Weapon.getTypeDescription(CultimaPanel.CBimageIndex));
         CultimaPanel.CBspellIndex1 = 0;
         CultimaPanel.CBspellIndex2 = 1;
      } 
      else if(CultimaPanel.selAttribute == SUGGEST_WEAPON)
      {
         CultimaPanel.CBimageIndex = getSuggestedCharacter(CultimaPanel.CBmight, CultimaPanel.CBmind, CultimaPanel.CBagility);
         CultimaPanel.clearMessage();
         CultimaPanel.sendMessage(Weapon.getTypeDescription(CultimaPanel.CBimageIndex));
      }  
      else if(CultimaPanel.selAttribute == MIGHT)
      {
         if(CultimaPanel.CBPoints > 0 && CultimaPanel.CBmight<Player.MAX_STAT)
         {
            CultimaPanel.CBmight++;
            CultimaPanel.CBPoints--;
         }
      } 
      else if(CultimaPanel.selAttribute == MIND)
      {
         if(CultimaPanel.CBPoints > 0 && CultimaPanel.CBmind<Player.MAX_STAT)
         {
            CultimaPanel.CBmind++;
            CultimaPanel.CBPoints--;
            CultimaPanel.CBspellIndex1 = 0;
            CultimaPanel.CBspellIndex2 = 1;
         }
      }
      else if(CultimaPanel.selAttribute == AGILITY)
      {
         if(CultimaPanel.CBPoints > 0 && CultimaPanel.CBagility<Player.MAX_STAT)
         {
            CultimaPanel.CBagility++;
            CultimaPanel.CBPoints--;
         }
      } 
      else if(CultimaPanel.selAttribute == AWARENESS)
      {
         if(CultimaPanel.CBPoints >= 10 && CultimaPanel.CBawareness < Player.MAX_AWARENESS)
         {
            CultimaPanel.CBawareness++;
            CultimaPanel.CBPoints-=10;
         }
      }
      else if(CultimaPanel.selAttribute == AFFLICTION)
      {
         CultimaPanel.CBaffliction++;
         if(CultimaPanel.CBaffliction >= NUM_AFFLICTIONS)
            CultimaPanel.CBaffliction = NUM_AFFLICTIONS-1;
      }
      else if(CultimaPanel.selAttribute == RANDOM)
      {
         CultimaPanel.CBspellIndex1 = 0;
         CultimaPanel.CBspellIndex2 = 1;
         CultimaPanel.CBname = Utilities.nameGenerator("character");
         int [] randomStats = Player.buildRandomStats();
         CultimaPanel.CBmight = randomStats[0];
         CultimaPanel.CBmind = randomStats[1];
         CultimaPanel.CBagility = randomStats[2];
         CultimaPanel.CBawareness = randomStats[3];
         CultimaPanel.CBPoints = 0;
         CultimaPanel.CBimageIndex = getSuggestedCharacter(CultimaPanel.CBmight, CultimaPanel.CBmind, CultimaPanel.CBagility);
         CultimaPanel.CBaffliction = NONE;
         if(Math.random() < 0.05)
         {
            CultimaPanel.CBaffliction = Utilities.rollDie(1, NUM_AFFLICTIONS-1);
         }
         ArrayList<Item>spellPool = Player.getAllSpellPool(CultimaPanel.CBmind);
         if(CultimaPanel.CBimageIndex==Player.LONGSTAFF)
            spellPool = Player.getBookSpellPool(CultimaPanel.CBmind);
         if((CultimaPanel.CBimageIndex==Player.LONGSTAFF || CultimaPanel.CBimageIndex==Player.STAFF) && CultimaPanel.CBspellIndex1>=0 && CultimaPanel.CBspellIndex1<spellPool.size())
         {
            CultimaPanel.CBspellIndex1 = (int)(Math.random()*spellPool.size());
            CultimaPanel.CBspell1 = spellPool.get(CultimaPanel.CBspellIndex1);
         }
         if(CultimaPanel.CBimageIndex==Player.STAFF && CultimaPanel.CBspellIndex2>=0 && CultimaPanel.CBspellIndex2<spellPool.size())
         {
            CultimaPanel.CBspellIndex2 = (int)(Math.random()*spellPool.size());
            CultimaPanel.CBspell2 = spellPool.get(CultimaPanel.CBspellIndex2);
            for(int numTries=0; numTries < 1000 && CultimaPanel.CBspell1.getName().equals(CultimaPanel.CBspell2.getName()); numTries++)
            {
               CultimaPanel.CBspellIndex2 = (int)(Math.random()*spellPool.size());
               CultimaPanel.CBspell2 = spellPool.get(CultimaPanel.CBspellIndex2);
            }
         }
      } 
   }
   
   public static void scrollLeftSpell1()
   {
      if(CultimaPanel.CBimageIndex!=Player.LONGSTAFF && CultimaPanel.CBimageIndex!=Player.STAFF)
         return;
      ArrayList<Item>spellPool = Player.getAllSpellPool(CultimaPanel.CBmind);
      if(CultimaPanel.CBimageIndex==Player.LONGSTAFF)
      {
         spellPool = Player.getBookSpellPool(CultimaPanel.CBmind);
      }
      CultimaPanel.CBspellIndex1 = (CultimaPanel.CBspellIndex1-1);
      if(CultimaPanel.CBspellIndex1 < 0)
         CultimaPanel.CBspellIndex1 = spellPool.size() - 1;
      if(CultimaPanel.CBspellIndex1>=0 && CultimaPanel.CBspellIndex1<spellPool.size())
      {
         CultimaPanel.CBspell1 = spellPool.get(CultimaPanel.CBspellIndex1);
         if(CultimaPanel.CBimageIndex==Player.STAFF && CultimaPanel.CBspellIndex2>=0 && CultimaPanel.CBspellIndex2<spellPool.size())
         {
            CultimaPanel.CBspell2 = spellPool.get(CultimaPanel.CBspellIndex2);
            if(CultimaPanel.CBspell2!= null && CultimaPanel.CBspell1.equals(CultimaPanel.CBspell2) && spellPool.size() >= 2)
            {
               scrollLeftSpell1();
            }
         }
      }
   }
   
   public static void scrollRightSpell1()
   {
      if(CultimaPanel.CBimageIndex!=Player.LONGSTAFF && CultimaPanel.CBimageIndex!=Player.STAFF)
         return;
      ArrayList<Item>spellPool = Player.getAllSpellPool(CultimaPanel.CBmind);
      if(CultimaPanel.CBimageIndex==Player.LONGSTAFF)
      {
         spellPool = Player.getBookSpellPool(CultimaPanel.CBmind);
      }
      CultimaPanel.CBspellIndex1 = (CultimaPanel.CBspellIndex1+1) % spellPool.size();
      if(CultimaPanel.CBspellIndex1>=0 && CultimaPanel.CBspellIndex1<spellPool.size())
      {
         CultimaPanel.CBspell1 = spellPool.get(CultimaPanel.CBspellIndex1);
         if(CultimaPanel.CBimageIndex==Player.STAFF && CultimaPanel.CBspellIndex2>=0 && CultimaPanel.CBspellIndex2<spellPool.size())
         {
            CultimaPanel.CBspell2 = spellPool.get(CultimaPanel.CBspellIndex2);
            if(CultimaPanel.CBspell2!= null && CultimaPanel.CBspell1.equals(CultimaPanel.CBspell2) && spellPool.size() >= 2)
            {
               scrollRightSpell1();
            }
         }
      }
   }
   
   public static void scrollLeftSpell2()
   {
      if(CultimaPanel.CBimageIndex!=Player.LONGSTAFF && CultimaPanel.CBimageIndex!=Player.STAFF)
         return;
      ArrayList<Item>spellPool = Player.getAllSpellPool(CultimaPanel.CBmind);
      if(CultimaPanel.CBimageIndex==Player.LONGSTAFF)
      {
         spellPool = Player.getBookSpellPool(CultimaPanel.CBmind);
      }
      CultimaPanel.CBspellIndex2 = (CultimaPanel.CBspellIndex2-1);
      if(CultimaPanel.CBspellIndex2 < 0)
         CultimaPanel.CBspellIndex2 = spellPool.size() - 1;
      if(CultimaPanel.CBspellIndex2>=0 && CultimaPanel.CBspellIndex2<spellPool.size())
      {
         CultimaPanel.CBspell2 = spellPool.get(CultimaPanel.CBspellIndex2);
         if(CultimaPanel.CBimageIndex==Player.STAFF && CultimaPanel.CBspellIndex1>=0 && CultimaPanel.CBspellIndex1<spellPool.size())
         {
            CultimaPanel.CBspell1 = spellPool.get(CultimaPanel.CBspellIndex1);
            if(CultimaPanel.CBspell2!= null && CultimaPanel.CBspell1.equals(CultimaPanel.CBspell2) && spellPool.size() >= 2)
            {
               scrollLeftSpell2();
            }
         }
      }
   }
   
   public static void scrollRightSpell2()
   {
      if(CultimaPanel.CBimageIndex!=Player.LONGSTAFF && CultimaPanel.CBimageIndex!=Player.STAFF)
         return;
      ArrayList<Item>spellPool = Player.getAllSpellPool(CultimaPanel.CBmind);
      if(CultimaPanel.CBimageIndex==Player.LONGSTAFF)
      {
         spellPool = Player.getBookSpellPool(CultimaPanel.CBmind);
      }
      CultimaPanel.CBspellIndex2 = (CultimaPanel.CBspellIndex2+1) % spellPool.size();
      if(CultimaPanel.CBspellIndex2>=0 && CultimaPanel.CBspellIndex2<spellPool.size())
      {
         CultimaPanel.CBspell2 = spellPool.get(CultimaPanel.CBspellIndex2);
         if(CultimaPanel.CBimageIndex==Player.STAFF && CultimaPanel.CBspellIndex1>=0 && CultimaPanel.CBspellIndex1<spellPool.size())
         {
            CultimaPanel.CBspell1 = spellPool.get(CultimaPanel.CBspellIndex1);
            if(CultimaPanel.CBspell2!= null && CultimaPanel.CBspell1.equals(CultimaPanel.CBspell2) && spellPool.size() >= 2)
            {
               scrollRightSpell2();
            }
         }
      }
   }

   //draws image on the right page of the journal for monsters, weapons, townsfolk, etc
   //draws a ? in a frame if picture is null
   public static void drawPictureInFrame(Graphics g, ImageIcon picture)
   {
      int SIZE = CultimaPanel.SIZE;
      int SCREEN_SIZE = CultimaPanel.SCREEN_SIZE;
      int xStart = 5;
      int yStart = 4;
      g.setColor(Color.orange.darker().darker());        		//place a frame around our portrait
      g.fillRect((int)((SIZE*SCREEN_SIZE) + (SIZE*(xStart + 0.5))), (int)(SIZE*(yStart+0.25)), SIZE*7, SIZE*7);    
      g.setColor(new Color(245, 230, 220));							//draw canvas in frame
      g.fillRect((int)((SIZE*SCREEN_SIZE) + (SIZE*(xStart + 1))), (int)(SIZE*(yStart+0.75)), SIZE*6, SIZE*6);                                        
      g.setColor(new Color(10, 10, 10));
      if(picture != null)
      {
         g.drawImage(picture.getImage(), (SIZE*SCREEN_SIZE) + (SIZE*(xStart + 1)), (int)(SIZE*(yStart+0.75)), SIZE*6, SIZE*6, null);
      }
      else
      {
         g.setColor(Color.orange.darker().darker());        		//place a frame around our portrait
         g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE*4));
         g.drawString("?", (SIZE*SCREEN_SIZE) + (SIZE*(xStart + 3)), (SIZE*(yStart+5)));
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.setColor(new Color(10, 10, 10));
      }
   }

   public static void show(Graphics g)
   {	 
      int SIZE = CultimaPanel.SIZE;
      int SCREEN_SIZE = CultimaPanel.SCREEN_SIZE;
      Font readable = new Font("Serif", Font.BOLD | Font.ITALIC, SIZE);
      Font readableP = new Font("Serif", Font.PLAIN | Font.ITALIC, SIZE);  
      Font handWriten = new Font("Pristina", Font.BOLD | Font.ITALIC, SIZE);
      Font title = new Font("Old English Text MT", Font.BOLD, SIZE);
      Font oldP = new Font("Old English Text MT", Font.PLAIN, SIZE);
   
      g.setColor(new Color(0, 0, 127));		         //draw a blue boarder around the board
      g.fillRect(0,0, SIZE*SCREEN_SIZE, SIZE*SCREEN_SIZE);
      int x = SIZE;
      int y = SIZE;  
   
      g.setFont(oldP);
      if(CultimaPanel.selAttribute == NAME)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Name:    ", x, y);
      g.setFont(readable);
      if(CultimaPanel.selAttribute == NAME)
         g.setColor(Color.white);
      else
         g.setColor(Color.red);
      g.drawString(CultimaPanel.CBname, x+(SIZE*8), y);
      
      //draw points left to add stats
      if(CultimaPanel.CBPoints > 10)
         g.setColor(Color.green.darker());
      else if(CultimaPanel.CBPoints > 0)
         g.setColor(Color.yellow);
      else
         g.setColor(Color.red);
      g.drawString("Points left:"+CultimaPanel.CBPoints, (SIZE*SCREEN_SIZE)-(SIZE*8), y);   
      
      y += SIZE;
      g.setColor(Color.orange);
      g.setFont(oldP);
      y += SIZE;
      
      if(CultimaPanel.selAttribute == IMAGEICON)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Weapon preference:    ", x, y);
      g.setFont(readable);
      if(CultimaPanel.selAttribute == IMAGEICON)
         g.setColor(Color.white);
      else
         g.setColor(Color.red);
      String weapName = Weapon.getWeaponType(CultimaPanel.CBimageIndex);
      weapName = weapName.substring(0,weapName.length()-1);
      if(CultimaPanel.CBimageIndex==0)
         weapName = "none";
      g.drawString(weapName, x+(SIZE*8), y);
      y += SIZE;
      g.setColor(Color.orange);
      g.setFont(oldP);
      y += SIZE;
   
      if(CultimaPanel.selAttribute == SUGGEST_WEAPON)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Suggest weapon:    ", x, y);
      y += SIZE;
      g.setColor(Color.orange);
      g.drawString(choices[SUGGEST_WEAPON], x, y);
      y += SIZE;
      y += SIZE;
   
      if(CultimaPanel.selAttribute == MIGHT)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Might:    ", x, y);
      g.setFont(readable);
      if(CultimaPanel.selAttribute == MIGHT)
         g.setColor(Color.white);
      else if(CultimaPanel.CBPoints > 10)
         g.setColor(Color.green.darker());
      else if(CultimaPanel.CBPoints > 0)
         g.setColor(Color.yellow);
      else
         g.setColor(Color.red);
      g.drawString(""+(CultimaPanel.CBmight), x+(SIZE*8), y);
      y += SIZE;
      g.setColor(Color.orange);
      g.setFont(oldP);
      g.drawString(choices[MIGHT], x, y);
      y += SIZE;
      y += SIZE;
      
      if(CultimaPanel.selAttribute == MIND)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Mind:    ", x, y);
      g.setFont(readable);
      if(CultimaPanel.selAttribute == MIND)
         g.setColor(Color.white);
      else if(CultimaPanel.CBPoints > 10)
         g.setColor(Color.green.darker());
      else if(CultimaPanel.CBPoints > 0)
         g.setColor(Color.yellow);
      else
         g.setColor(Color.red);
      g.drawString(""+(CultimaPanel.CBmind), x+(SIZE*8), y);
      y += SIZE;
      g.setColor(Color.orange);
      g.setFont(oldP);
      g.drawString(choices[MIND], x, y);
      y += SIZE;
      y += SIZE;
   
      if(CultimaPanel.selAttribute == AGILITY)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Agility:    ", x, y);
      g.setFont(readable);
      if(CultimaPanel.selAttribute == AGILITY)
         g.setColor(Color.white);
      else if(CultimaPanel.CBPoints > 10)
         g.setColor(Color.green.darker());
      else if(CultimaPanel.CBPoints > 0)
         g.setColor(Color.yellow);
      else
         g.setColor(Color.red);
      g.drawString(""+(CultimaPanel.CBagility), x+(SIZE*8), y);
      y += SIZE;
      g.setColor(Color.orange);
      g.setFont(oldP);
      g.drawString(choices[AGILITY], x, y);
      y += SIZE;
      y += SIZE;
      
      if(CultimaPanel.selAttribute == AWARENESS)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Awareness:    ", x, y);
      g.setFont(readable);
      if(CultimaPanel.selAttribute == AWARENESS)
         g.setColor(Color.white);
      else if(CultimaPanel.CBPoints > 10)
         g.setColor(Color.green.darker());
      else if(CultimaPanel.CBPoints > 0)
         g.setColor(Color.yellow);
      else
         g.setColor(Color.red);
      g.drawString(""+(CultimaPanel.CBawareness), x+(SIZE*8), y);
      y += SIZE;
      g.setColor(Color.orange);
      g.setFont(oldP);
      g.drawString(choices[AWARENESS], x, y);
      y += SIZE;
      y += SIZE;
      
      if(CultimaPanel.selAttribute == AFFLICTION)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Starting affliction:    ", x, y);
      g.setFont(readable);
      if(CultimaPanel.selAttribute == AFFLICTION)
         g.setColor(Color.white);
      else
         g.setColor(Color.red);
      g.drawString(afflictionTypes[CultimaPanel.CBaffliction], x+(SIZE*8), y);
      y += SIZE;
      g.setColor(Color.orange);
      g.setFont(oldP);
      g.drawString(choices[AFFLICTION], x, y);
      y += SIZE;
      y += SIZE;
      
      if(CultimaPanel.selAttribute == RANDOM)
         g.setColor(Color.white);
      else
         g.setColor(Color.orange);
      g.drawString("Random Build", x, y);
      y += SIZE;
      g.setColor(Color.orange);
      g.drawString(choices[RANDOM], x, y);
      y += SIZE;
      y += SIZE;
      
      //draw the player's image in the right info-bar if you have information on it                                     
      ImageIcon temp = null;
      if(CultimaPanel.CBimageIndex>=0 && CultimaPanel.CBimageIndex<=Player.LUTE)
      {
         if(CultimaPanel.CBimageIndex==Player.STAFF || CultimaPanel.CBimageIndex==Player.LONGSTAFF || CultimaPanel.CBimageIndex==Player.LUTE)
         {
            temp = new ImageIcon(Player.guardDown[CultimaPanel.CBimageIndex][0]);
         }
         else
         {
            temp = new ImageIcon(Player.characters[CultimaPanel.CBimageIndex][CultimaPanel.player.RIGHT_IMAGE][0]);
         }
      }
      drawPictureInFrame(g, temp);
      x = SIZE * SCREEN_SIZE;
      y = SIZE * 11;
      g.setColor(new Color(0, 127, 0));
      g.drawString(getWeaponInfo(CultimaPanel.CBimageIndex), x, y+=SIZE);
      g.drawString(getAfflictionInfo(CultimaPanel.CBaffliction), x, y+=SIZE);
      //if we get to learn a spell, give options to scroll through to select it/them
      g.setColor(new Color(0, 0, 127));
      ArrayList<Item>spellPool = Player.getAllSpellPool(CultimaPanel.CBmind);
      if(CultimaPanel.CBimageIndex==Player.LONGSTAFF || CultimaPanel.CBimageIndex==Player.STAFF)
      {  //select CBspell1
         if(CultimaPanel.CBimageIndex==Player.LONGSTAFF)
            spellPool = Player.getBookSpellPool(CultimaPanel.CBmind);
         g.drawString("   [] to scroll: "+spellPool.get(CultimaPanel.CBspellIndex1).getName(), x, y+=SIZE);
      }
      if(CultimaPanel.CBimageIndex==Player.STAFF)
      {  //select CBspell2
         g.drawString("<> to scroll: "+spellPool.get(CultimaPanel.CBspellIndex2).getName(), x, y+=SIZE);
      }
      
   //RIGHT info bar
      x = SIZE * SCREEN_SIZE;
      y = (SIZE); 
      g.setFont(title);
      g.setColor(new Color(0, 0, 127));
      if(CultimaPanel.player.getBody() > 0)
         g.drawString("(S) to save your progress.", x, y);
      else
         g.drawString("(L) to load your last save.", x, y);
      y += SIZE;
      g.drawString("(Q) to quit and save the game.", x, y);
      y += SIZE;
      g.drawString("(B) to build a new world.", x, y);
      y += SIZE;
      g.drawString("(C)ommit to this character and start.", x, y);
      y += SIZE;
   
      y = (SIZE * SCREEN_SIZE) - (SIZE * 7);
      g.setColor(new Color(0, 0, 0));
      g.setFont(readableP);
      g.drawString("(UP-DOWN)", x, y);
      g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
      g.drawString("                         to select attribute", x, y);
      y += SIZE;
      g.setFont(readableP);
      g.drawString("(LEFT-RIGHT)", x, y);
      g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
      g.drawString("                         to change values", x, y);
      y += SIZE;
      g.setFont(readableP);
      g.drawString("(M)ap  (I)nventory  (J)ournal(ESC)", x, y);
   //show scrolling message array
      y = (SIZE * SCREEN_SIZE) - (SIZE * 4);
      if(CultimaPanel.talkSel && CultimaPanel.selected!=null)
         g.setColor(new Color(30, 255, 30));
      else
         g.setColor(new Color(0, 0, 127));
   
      g.fillRect(x, y-SIZE ,  (SIZE * SCREEN_SIZE), (SIZE * 5));
      if(CultimaPanel.talkSel && CultimaPanel.selected!=null)
         g.setColor(new Color(127, 0, 0));
      else
         g.setColor(new Color(255, 0, 0));
      g.setFont(readable);
      y = y - (SIZE/4);
      for(int i=0; i<CultimaPanel.message.length; i++)
      {
         String sentence =CultimaPanel.message[i];
         if(i == CultimaPanel.message.length-1 && CultimaPanel.typed.length() > 0)
            sentence = "~"+CultimaPanel.typed;
         String tempSent = sentence.toLowerCase();
         if(!tempSent.startsWith("~"))           //dialogue with NPCs will start with a ~
         {
            g.setFont(oldP);
            g.drawString(sentence, x, y);
         }
         else
         {
            g.setFont(readable);
            String [] words = sentence.split(" ");
            int xAdd = 0;
            for(int j=0; j<words.length; j++)
            {
               g.setColor(new Color(127, 0, 0));
               String tempWord = new String(words[j]);
               if(tempWord.startsWith("~"))
                  tempWord = tempWord.substring(1);
               if(tempWord.length()>0 && tempWord.equals(tempWord.toUpperCase()) && !tempWord.equals("~I") && !tempWord.equals("~A") && !tempWord.equals("I") && !tempWord.equals("A"))
               {
                  g.setColor(new Color(60, 0, 60));
                  if(j==0) 
                     tempWord = tempWord.charAt(0) + tempWord.substring(1).toLowerCase();
                  else
                     tempWord = tempWord.toLowerCase();
               }
               tempWord += " ";
               g.drawString(tempWord, x+xAdd, y);
               int width = g.getFontMetrics().stringWidth(tempWord);
               xAdd += width;
            }
         }
         y += SIZE;
      }
   
   }

   public static byte getSuggestedCharacter(int might, int mind, int agility)
   {
      if(Math.abs(might - mind) <= 4 && Math.abs(might - agility) <= 4 && Math.abs(mind - agility) <= 4)
         return Player.TORCH;
      if(might>mind && might>agility && agility>mind)       //might, agility, mind
         return Player.HAMMER;
      if(might>=mind && might>=agility && mind>=agility)    //might, mind, agility
         return Player.SABER;
      if(agility>=might && agility>=mind && mind>=might)    //agility, mind, might
         return Player.BOW;
      if(agility>might && agility>mind && might>mind)       //agility, might, mind
         return Player.SPEAR;
      if(mind>might && mind>agility && might>agility)       //mind, might, agility
         return Player.LONGSTAFF;
      if(mind>=might && mind>=agility && agility>=might)    //mind, agility, might
         return Player.STAFF;
      return Player.DAGGER;
   }
   
   public static String getWeaponInfo(byte imageIndex)
   {
      if(imageIndex == Player.NONE) 
         return "No gold-Awareness,knowledge bonus";
      if(imageIndex == Player.TORCH) 
         return "Awareness,gold,floretbox bonus";
      if(imageIndex == Player.STAFF) 
         return "2 magic spells,spell knowledge";
      if(imageIndex == Player.LONGSTAFF) 
         return "1 magic spell,spell knowledge";
      if(imageIndex == Player.SPEAR) 
         return "Shipping knowledge";
      if(imageIndex == Player.BOW) 
         return "25 arrows,trapping knowledge";
      if(imageIndex == Player.CROSSBOW) 
         return "25 arrows added,gold";
      if(imageIndex == Player.AXE)
         return "Town and mapping info";
      if(imageIndex == Player.DUALAXE)
         return "Leather armor bonus";   
      if(imageIndex == Player.HAMMER) 
         return "Magic gem bonus";
      if(imageIndex == Player.DAGGER) 
         return "3 daggers,lockpick bonus";
      if(imageIndex == Player.SABER) 
         return "Monster knowledge";
      if(imageIndex == Player.SWORDSHIELD) 
         return "Horse knowledge";
      if(imageIndex == Player.DUAL) 
         return "Loaded-cube bonus";
      if(imageIndex == Player.LUTE) 
         return "Gold bonus";
      return "";
   }

   public static String getAfflictionInfo(int affliction)
   {
      if(affliction == BOUNTY) 
         return "-50 reputation,100 bounty,+1 to all stats";
      if(affliction == FRAMED) 
         return "-150 reputation,25 bounty,+2 to all stats";
      if(affliction == VAMPIRE) 
         return "Vampyric curse,4 spells added,awareness bonus";
      if(affliction == WEREWOLF) 
         return "Wolfen curse,x2 strength,awareness as wolf";
      return "";
   }
}