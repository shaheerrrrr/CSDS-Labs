import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class Display extends CultimaPanel
{
   public static final Color unavailable = new Color(175, 175, 175);
   public static final Color magicColor = new Color(0, 50, 30);
   public static final Color weaponColor = new Color(60, 50, 0);
   public static final Color traderColor = new Color(50, 0, 60);
   public static final Color talkColor = new Color(214,104,227);
   public static void drawRainbow(Graphics g, int alpha)	
   {
      if(menuMode==TRAVEL && CultimaPanel.rainbowAlpha==0)
         return;
      if(menuMode!=TRAVEL && menuMode!=MAIN)
         return;
      if(menuMode!=MAIN && TerrainBuilder.indoors())
         return;   
      Color [] colors = {Color.red, Color.red, Color.orange, Color.yellow, Color.green.darker(), new Color(90,90,255), Color.pink};
      int colorIndex=0;
      for(int i=(int)(14.67*SIZE);i>=(int)(12.67*SIZE);i--)
      {
         if(i%10==0 && colorIndex < colors.length-1)
            colorIndex++;
         Color current = new Color(colors[colorIndex].getRed(), colors[colorIndex].getGreen(), colors[colorIndex].getBlue(), alpha);   
         g.setColor(current);
       //g.drawArc(                 x,                y,                  width,   height,  startAngle,arcAngle);
         g.drawArc((int)((13.34*SIZE)-(i)-SIZE), (int)((13.34*SIZE)-(i)), 1+(i*2), 1+(i*2),      0,      180);
      }
   }
   
   public static void drawShadow(Graphics g,int x, int y, int size, byte npcType)
   {
      if(menuMode!=TRAVEL || TerrainBuilder.indoors() || NPC.isVampire(npcType) || NPC.isFlyer(npcType) || npcType==NPC.FIRE)
         return;
      int distFromNoon = (int)(Math.abs(time - 12));
      int opacity = Math.max(0, 175 - (weather*10) - (fogAlpha/10) - (distFromNoon*15));
      g.setColor(new Color(1, 1, 5, opacity));
      if(time < 12 && x > SIZE)                   //morning - draw from left to center
         g.fillOval(x-(int)(SIZE/3)+(6-distFromNoon), (int)(y + SIZE/1.5), SIZE, SIZE/4);
      else if(time > 12 && y<(SCREEN_SIZE*SIZE)-SIZE)     //afternoon - draw from center to right
         g.fillOval(x+(distFromNoon), (int)(y + SIZE/1.5), SIZE, SIZE/4);
   }

   public static void drawFog(Graphics g)
   {
      if(menuMode!=TRAVEL || fogAlpha == 0)
         return;  
      int gray = Math.min(255, 127 + ((int)(CultimaPanel.time*10))); //make fog color darker for earlier in the morning
      Color fogColor = new Color(gray, gray, gray);
   
      int x =0, y=0;		      //upper left corner location of where image will be drawn
      byte[][]currMap = map.get(player.getMapIndex());
      int size = SIZE;
      int numRows = SCREEN_SIZE;
      int numCols = SCREEN_SIZE;
      int focusRow = player.getRow();                        //center of where our map is focused
      int focusCol = player.getCol();
      for(int i=0; i<numRows; i++)
      {
         int regularRow = ((focusRow - (numRows/2)) + i);
         int r = equalizeRow(regularRow); 
         x = 0;						//reset the row distance
         for(int j=0; j<numCols; j++)
         {
            int regularCol = ((focusCol - (numCols/2)) + j);
            int c = equalizeCol(regularCol); 
            int distance = (int)(distanceSimple(i, j, SCREEN_SIZE/2, SCREEN_SIZE/2)); 
            int opacity = Math.min(255, Math.max(0, fogAlpha - (((SCREEN_SIZE/2) - distance)*6)));       //make less depending on dist from center  
            g.setColor(new Color(fogColor.getRed(), fogColor.getGreen(), fogColor.getBlue(), opacity ));
            g.fillRect(x, y, size, size); 
                           
            x+=size;
         }
         y+=size;
      }
               
   }

   public static void drawSmoke(Graphics g)
   {
      if(menuMode == TRAVEL && smoke.size() > 0)
      {
         for(int i=smoke.size() - 1; i >= 0; i--)
         {
            SmokePuff curr = smoke.get(i);
            if(curr.shouldBeRemoved())
            {
               smoke.remove(i);
               continue;
            }
            Color flashColor = curr.getColor();
            g.setColor(flashColor);
            g.fillOval(curr.getX(), curr.getY(), curr.getRadius(), curr.getRadius());
            curr.advance();
         }
      }
   }

   public static void drawPrecipitation(Graphics g)
   {
      String locationType = player.getLocationType();
      if(menuMode == TRAVEL && weather > 0 && !locationType.contains("sewer") && !locationType.contains("cave") && !locationType.contains("mine") && !locationType.contains("dungeon") && !locationType.contains("lair") && !locationType.contains("pacrealm"))
      {
         int numRows = SCREEN_SIZE;
         int numCols = SCREEN_SIZE;
         g.setColor(Color.cyan);
         if(isWinter())
            g.setColor(Color.white);
         Spell firestorm = player.getActiveSpell("Firestorm");
         if(locationType.contains("world") || locationType.contains("temple") || locationType.contains("battlefield") || locationType.contains("jurassica") || locationType.contains("1942"))
         {
            int totalDrops = numRows * weather * 2;
            if(firestorm != null)
               totalDrops = numRows * weather;
            for(int numDrops=0; numDrops<totalDrops; numDrops++)
            {
               if(firestorm != null || locationType.contains("underworld"))
                  g.setColor(new Color(255,Utilities.rollDie(128,255),0));
               int x1 = (int)(Math.random()*(numCols*SIZE));
               int y1 = (int)(Math.random()*(numRows*SIZE));
               int length = (SIZE/8)*weather;
               if(isWinter())
                  g.fillOval(x1, y1, Math.max((int)(weather/1.5), 2), Math.max((int)(weather/1.5), 4));
               else
                  g.drawLine(x1, y1, x1+(weather*2), y1+length);
               if(firestorm != null)
               {
                  for(int thickness=-4; thickness<=4; thickness++)
                  {
                     int fx1=x1;
                     int fy1=y1;
                     int fx2=x1+(weather*2)+thickness;
                     int fy2=y1+length+thickness;                            
                     g.drawLine(fx1, fy1, fx2, fy2);                                 
                  }
               }
            }
         }
         else
         {  //for indoor locations, don't draw rain over panels that are probably indoors
            for(int i=0; i<numRows; i++)
            {
               int focusRow = player.getRow();                        //center of where our map is focused
               int focusCol = player.getCol();
               int regularRow = ((focusRow - (numRows/2)) + i);
               int r = equalizeRow(regularRow); 
               for(int j=0; j<numCols; j++)
               {
                  if(firestorm != null)
                     g.setColor(new Color(255,Utilities.rollDie(128,255),0));
                  int regularCol = ((focusCol - (numCols/2)) + j);
                  int c = equalizeCol(regularCol); 
                  int mapIndex = player.getMapIndex();
                  Terrain curPos = allTerrain.get(Math.abs((map.get(mapIndex))[r][c]));
                  if(!TerrainBuilder.isIndoorFloor(curPos))
                  {  //make it more likely it draws a raindrop in that cell depending on the severity of the storm
                     if(Utilities.rollDie(0,10) < weather)
                     {
                        int x1 = Utilities.rollDie(j*(SIZE-2), j*(SIZE+2));
                        int y1 = Utilities.rollDie(i*(SIZE-2), i*(SIZE+2));
                        int length = (SIZE/8)*weather;
                        if(isWinter())
                           g.fillOval(x1, y1, Math.max((int)(weather/1.5), 2), Math.max((int)(weather/1.5), 4));
                        else
                           g.drawLine(x1, y1, x1+(weather*2), y1+length);
                     }
                  }
               }
            }
         }
      }
   
   }

   //given the time of day, returns the col in which the sun should be reflected in water/ice (noon would be SCREEN_SIZE/2) and the alpha intensity in a Coord object
   public static Coord findSunCol()
   {
      double time = CultimaPanel.time; 
      int alpha = (int)(50 - ((12 - time)*2));
      if(time >= 6 && time < 6.5) 
         return new Coord(24, alpha);
      if(time >= 6.5 && time < 7) 
         return new Coord(23, alpha);
      if(time >= 7 && time < 7.5) 
         return new Coord(22, alpha);
      if(time >= 7.5 && time < 8) 
         return new Coord(21, alpha);
      if(time >= 8 && time < 8.5) 
         return new Coord(20, alpha);
      if(time >= 8.5 && time < 9) 
         return new Coord(19, alpha);
      if(time >= 9 && time < 9.5) 
         return new Coord(18, alpha);
      if(time >= 9.5 && time < 10) 
         return new Coord(17, alpha);
      if(time >= 10 && time < 10.5) 
         return new Coord(16, alpha);
      if(time >= 10.5 && time < 11) 
         return new Coord(15, alpha);
      if(time >= 11 && time < 11.5) 
         return new Coord(14, alpha);
      if(time >= 11.5 && time < 12) 
         return new Coord(13, alpha);
      if(time >= 12 && time < 12.5) 
         return new Coord(12, alpha);
      if(time >= 12.5 && time < 13) 
         return new Coord(11, alpha);
      if(time >= 13 && time < 13.5) 
         return new Coord(10, alpha);
      if(time >= 13.5 && time < 14) 
         return new Coord(9, alpha);
      if(time >= 14 && time < 14.5) 
         return new Coord(8, alpha);
      if(time >= 14.5 && time < 15) 
         return new Coord(7, alpha);
      if(time >= 15 && time < 15.5) 
         return new Coord(6, alpha);
      if(time >= 15.5 && time < 16) 
         return new Coord(5, alpha);
      if(time >= 16 && time < 16.5) 
         return new Coord(4, alpha);
      if(time >= 16.5 && time < 17) 
         return new Coord(3, alpha);
      if(time >= 17 && time < 17.5) 
         return new Coord(2, alpha);
      if(time >= 17.5 && time < 18) 
         return new Coord(1, alpha);
      if(time >= 18 && time < 18.5) 
         return new Coord(0, alpha);
      return null;
   }

   public static void drawPortrait(Graphics g)
   {
      if(player.getBody() > 0 && player.getPortrait()!=null)       
      { 
         g.drawImage(player.getPortrait().getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         if(player.hasItem("arenacape"))
            g.drawImage(clothes[9].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);   
         String armName = player.getArmor().getName().toLowerCase();
         if(armName.contains("chain"))
            g.drawImage(clothes[1].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         else if(armName.contains("leather"))
            g.drawImage(clothes[4].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         else if(armName.contains("pelt"))
            g.drawImage(clothes[5].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         else if(armName.contains("plate"))
            g.drawImage(clothes[6].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         else if(armName.contains("robe") || armName.contains("cloak"))
            g.drawImage(clothes[7].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         else if(armName.contains("scale"))
            g.drawImage(clothes[8].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         if(player.hasItem("swiftboots"))
            g.drawImage(clothes[0].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         if(player.hasItem("blessed-crown"))
            g.drawImage(clothes[2].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
         else if(player.hasItem("focushelm"))
            g.drawImage(clothes[3].getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
      }   
      else if(player.getBody() <= 0 && player.getDeadPortrait()!=null)       
      {
         g.drawImage(player.getDeadPortrait().getImage(), (SIZE*SCREEN_SIZE) - (SIZE*7), (SIZE*2), SIZE*6, SIZE*6, null);
      }
   }

   //shows all of the graphics for the game, called from CultimaPanel's paintComponent
   public static void showAllGraphics(Graphics g)	
   {
      Font readable = new Font("Serif", Font.BOLD | Font.ITALIC, SIZE);
      Font readableP = new Font("Serif", Font.PLAIN | Font.ITALIC, SIZE);
      Font handWriten = new Font("Pristina", Font.BOLD | Font.ITALIC, SIZE);
   
      String pArmor = CultimaPanel.player.getArmor().getName().toLowerCase();
      NPCinSight.clear();
      AllNPCinSight.clear();
      if(menuMode == JOURNAL)
      {
         Journal.showJournal(g);
         return;
      }
      if(menuMode == README)
      {
         readMe(g);
         return;
      }
      if(menuMode == CHARACTER_BUILDER)
      {
         CharacterBuilder.show(g);
         return;
      }
      if(numFrames % animDelay == 0 || isWinter() || isSpring())
         groundColor = findGroundColor(false);
           
      int mapIndex = player.getMapIndex();
      int awareness = player.getAwareness();
      Spell flight = player.getActiveSpell("Flight");
      Spell timestop = player.getActiveSpell("Timestop");
      Spell firestorm = player.getActiveSpell("Firestorm");
      boolean sailing = player.isSailing();
      boolean indoors = TerrainBuilder.indoors();
      String locationType = player.getLocationType();
      byte[][]currMap = map.get(mapIndex);
   
      int x =0, y=0;		      //upper left corner location of where image will be drawn
      double tempTime = time; //change tempTime to some kind of dark if we are in a cave, mine, lair (indoors)
   
      if(locationType.contains("sewer") || locationType.equals("cave") || locationType.equals("mine") || locationType.equals("lair") || locationType.equals("dungeon"))
      {  //find the distance between player position in the cave and the cave entrance to set light level (tempTime)
         boolean found = false;
         if(!locationType.contains("sewer") && mapIndex>=0 && mapIndex<allLocations.size())
         {
            Location currLoc = allLocations.get(mapIndex);
            Teleporter telep = currLoc.getTeleporter();
            int entranceRow = telep.toRow();
            int entranceCol = telep.toCol();
            if(entranceRow>=0 && entranceCol>=0 && entranceRow<currMap.length && entranceCol<currMap[0].length)
            {  //brightest when tempTime is 12, darkest when it is zero
               found = true;
               double maxDist = Math.sqrt(Math.pow(currMap.length,2) + Math.pow(currMap[0].length/2,2));
               double distFromEntrance = distanceSimple(player.getRow(), player.getCol(), entranceRow, entranceCol);
               double percDist = Math.max(0,Math.min(1, 1 - (distFromEntrance/maxDist)));
               double distFrom12 = Math.max(0, Math.min(12, 12 - (Math.abs(12 - time))));
               tempTime = distFrom12*(percDist);
            }
         }
         if(!found)
         {
            if(time >=10 && time <= 14)
               tempTime = 23;
            else if(time >=8 && time <=16)
               tempTime = 3;
            else
               tempTime = 1;
         }
      }
      else if(locationType.contains("pacrealm"))
      {
         tempTime = 12;
      }
      darkened = darkenedCells(tempTime);    //collection of Terrain locations that need to be hidden because of the time of day (darkness)
      int size = SIZE;
      int numRows = SCREEN_SIZE;
      int numCols = SCREEN_SIZE;
      int focusRow = player.getRow();                        //center of where our map is focused
      int focusCol = player.getCol();
      if(menuMode == INVENTORY || menuMode == SHOPPE_ARMORY || menuMode == SHOPPE_MAGIC || menuMode == SHOPPE_TRADER || menuMode == WARDROBE)
      {
         Coord xAndy = showPlayerInfo(g);
         x = xAndy.getRow();
         y = xAndy.getCol();
      }
      else if(menuMode == MAIN)
      {  
         Coord xAndy = showMainMenuInfo(g);
         x = xAndy.getRow();
         y = xAndy.getCol();
      }
      else  //game screen or map
      {  //display terrain, map items, npcs, corpses + effects like sun reflections, shadows and glowing
         Coord xAndy = showGameScreen(g, x, y, tempTime, locationType, timestop);
         x = xAndy.getRow();
         y = xAndy.getCol();
      }
      Terrain currentPos = allTerrain.get(Math.abs((map.get(mapIndex))[focusRow][focusCol]));
      //draw our character in the center of the screen 
      drawTraveler(g, currentPos);
      //draw smoke if any
      drawSmoke(g);
      //draw any fog if any
      drawFog(g);
      //draw rain or snow if any
      if(timestop==null)
      {
         drawPrecipitation(g);
      }
      drawRainbow(g, CultimaPanel.rainbowAlpha);
      //draw text   
      drawText(g, x, y, currentPos);
   }
   
   public static void drawTraveler(Graphics g, Terrain currentPos)
   {
      if(menuMode != TRAVEL)
      {
         return;
      }
      Spell timestop = player.getActiveSpell("Timestop"); 
      Spell flight = player.getActiveSpell("Flight");
      String pArmor = CultimaPanel.player.getArmor().getName().toLowerCase();
      byte imageTemp = 0;
      if(player.isVampire() ||  player.isInvisible())
         imageTemp = NPC.MALEVAMP;     //flag so we don't draw our shadow if we are a vampire
      if(!player.isFlying())
         drawShadow(g, (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, imageTemp);
              
      double sizeMod = 1;
      if(player.getImageIndex()==Player.GREATSHIP || (player.getActiveSpell("Fear")!=null && player.getMind() >40 && !Weapon.isTorch(player.getWeapon().getName())))
         sizeMod = 2;
      else if(player.getImageIndex()==Player.BRIGANDSHIP || (player.getActiveSpell("Fear")!=null && player.getMind() >=16 && !Weapon.isTorch(player.getWeapon().getName())))
         sizeMod = 1.5;
      int xDiff = (SIZE - (int)(SIZE*sizeMod));
      if(player.getBody() <= 0)
         g.drawImage(player.getDeadPicture().getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
      else if(player.isInvisible())
      {
         g.setColor(new Color(255,255,255,75));
         g.fillOval((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, (int)(SIZE/1.15), SIZE);
      }
      else if(player.getMapIndex() == 0)
      {
         if(player.isSailing() || player.onHorse())
         {  //the images for ships and horses are directional depending on which direction you are going
            g.drawImage(player.getPicture(player.getLastDir()).getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)(SIZE*sizeMod), (int)(SIZE*sizeMod), null);
         }
         else if(flight!=null || player.isCamping())
         {  //we want to animate the player in the world-map if they are camping or flying and don't have timestop activated
            if(timestop!=null)
            {
               g.drawImage(player.getCurrentPicture().getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
            }
            else if(numFrames % animDelay == 0)
            {
               g.drawImage(player.getPictureAndAdvance().getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
            }
            else
            {  
               g.drawImage(player.getCurrentPicture().getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
            }
         }
         else
         {
            if(player.getActiveSpell("Fear")!=null && !Weapon.isTorch(player.getWeapon().getName()))
            {
               if(timestop!=null)
               {
                  g.drawImage(player.getCurrentPicture().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)(SIZE*sizeMod), (int)(SIZE*sizeMod), null);
               }
               else if(numFrames % animDelay == 0)
               {
                  g.drawImage(player.getPictureAndAdvance().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)(SIZE*sizeMod), (int)(SIZE*sizeMod), null);
               }
               else
               {
                  g.drawImage(player.getCurrentPicture().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)(SIZE*sizeMod), (int)(SIZE*sizeMod), null);
               }
            }
            else
            {
               g.drawImage(player.getPicture().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)(SIZE*sizeMod), (int)(SIZE*sizeMod), null);
            }
         }
      }
      else  //in a location - not the world-map.  We want to animate our character here so long as we don't have timestop spell activated
      {
         if(timestop!=null || player.hasEffect("web") || player.hasEffect("freeze"))
         {
            g.drawImage(player.getCurrentPicture().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)(SIZE*sizeMod), (int)(SIZE*sizeMod), null);
         }
         else if(numFrames % animDelay == 0)
         {
            g.drawImage(player.getPictureAndAdvance().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)(SIZE*sizeMod), (int)(SIZE*sizeMod), null);
         }
         else
         {
            g.drawImage(player.getCurrentPicture().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)(SIZE*sizeMod), (int)(SIZE*sizeMod), null);
         }
      }
      if(player.hasEffect("web"))
      {
         g.drawImage(web.getImage(), (SCREEN_SIZE/2)*SIZE-(SIZE/2), (SCREEN_SIZE/2)*SIZE-(SIZE/2), (int)(SIZE*sizeMod*2.5), (int)(SIZE*sizeMod*2.5), null);  
      }
      
      int plX = (SCREEN_SIZE/2)*SIZE;
      int plY = (SCREEN_SIZE/2)*SIZE;   
      if(timestop==null)
      {         
         if(player.isCampingWithFire())
         {
            if(Math.random() < 0.03 && smoke.size() < 50)
            {
               int radius = Utilities.rollDie(5);
               int opacity = Utilities.rollDie(63,191);
               smoke.add(new SmokePuff(plX, plY, radius, opacity));
            }
         }
         if(player.hasEffect("fire") || pArmor.contains("holocaust-cloak") || player.isCampingWithFire())
         {
            Color [] colors = {Color.red, Color.orange, Color.yellow};
            g.setColor(Utilities.getRandomFrom(colors));
            int radius = Utilities.rollDie(4, SIZE/4);
            int randX = Utilities.rollDie(plX, plX+SIZE);
            int randY = Utilities.rollDie(plY, plY+SIZE);
            if(player.isCampingWithFire())
            {
               radius = Utilities.rollDie(1, SIZE/6);
               randX = Utilities.rollDie(plX, plX+SIZE/2);
               randY = Utilities.rollDie(plY, plY);
            }
            g.fillOval(randX, randY, radius, radius);
            if(Math.random() < 0.03 && smoke.size() < 50)
            {
               radius = Utilities.rollDie(5);
               int opacity = Utilities.rollDie(63,191);
               smoke.add(new SmokePuff(randX, randY, radius, opacity));
            }
         } 
         if(player.getWeapon().getEffect().toLowerCase().contains("freeze") || player.hasEffect("freeze"))
         {
            Color [] colors = {Color.blue, Color.cyan, Color.white};
            g.setColor(Utilities.getRandomFrom(colors));
            int radius = Utilities.rollDie(4, SIZE/8);
            int randX = Utilities.rollDie(plX, plX+SIZE);
            int randY = Utilities.rollDie(plY, plY+SIZE);
            g.fillOval(randX, randY, radius, radius);
         }
         if((player.hasEffect("poison") || player.getActiveSpell("Fear")!=null) && !player.isWolfForm())
         {
            Color [] colors = {Color.green, Color.gray, Color.black};
            g.setColor(Utilities.getRandomFrom(colors));
            int radius = Utilities.rollDie(4, SIZE/8);
            int randX = Utilities.rollDie(plX, plX+SIZE);
            int randY = Utilities.rollDie(plY, plY+SIZE);
            g.fillOval(randX, randY, radius, radius);
         }
         if(player.hasEffect("curse"))
         {
            Color [] colors = {Color.magenta, Color.green, Color.pink};
            g.setColor(Utilities.getRandomFrom(colors));
            int radius = Utilities.rollDie(2, SIZE/4);
            int randX = Utilities.rollDie(plX, plX+SIZE);
            int randY = Utilities.rollDie(plY, plY+SIZE);
            g.fillOval(randX, randY, radius, radius);
         } 
         if(player.hasEffect("sullied"))
         {
            Color [] colors = {Color.magenta, Color.orange, Color.pink};
            g.setColor(Utilities.getRandomFrom(colors));
            int radius = Utilities.rollDie(2, SIZE/4);
            int randX = Utilities.rollDie(plX, plX+SIZE);
            int randY = Utilities.rollDie(plY, plY+SIZE);
            g.fillOval(randX, randY, radius, radius);
         } 
         if(player.hasEffect("seduced"))
         {
            Color [] colors = {Color.red, Color.pink};
            g.setColor(Utilities.getRandomFrom(colors));
            int radius = Utilities.rollDie(2, SIZE/4);
            int randX = Utilities.rollDie(plX, plX+SIZE);
            int randY = Utilities.rollDie(plY, plY+SIZE);
            g.fillOval(randX, randY, radius, radius);
         }
      }
      //if in shallow water, draw water/swamp over lower portion of us so we look partially submerged
      boolean sailing = player.isSailing();
      int size = SIZE;
      if(flight==null && !sailing)
      {
         String locationType = player.getLocationType();
         if(currentPos.getName().contains("water"))      
         {     
            double alpha = distFromDeadWinter();
            if(currentPos.getName().contains("rapid") || alpha < 0.75)
            {         
               g.drawImage(water.getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
               if(isWinter() && !locationType.contains("underworld"))
               {
                  if(currentPos.getName().contains("rapid"))
                     alpha /= 2;
                  else if(currentPos.getName().contains("deep"))
                     alpha /= 1.5;
                  Color ice = new Color(240,240,255, (int)Math.min(255,(255*alpha)));
                  g.setColor(ice);
                  g.fillRect((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE+(SIZE/2), size, size/2);
               }
            }
               //to reflect the sun in water/ice
            Coord sunReflection = findSunCol();
            int sunCol = -1, sunAlpha = 0;
            if(sunReflection != null)
            {
               sunCol = sunReflection.getRow();
               sunAlpha = sunReflection.getCol();
            }
            //draw yellow transparent square over water/ice for sun reflection
            if(!TerrainBuilder.indoors() && sunReflection != null && CultimaPanel.weather == 0)
            {  
               int opacity = Math.max(0, sunAlpha - ((Math.abs(sunCol - (SCREEN_SIZE/2))))*8);     
               Color glow = Color.yellow;
               g.setColor(new Color(glow.getRed(),glow.getGreen(),glow.getBlue(),opacity));
               g.fillRect((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE+(SIZE/2), size, size/2);
            }
         }
         else if(currentPos.getName().contains("Poison") && !isWinter())                                           
            g.drawImage(poison.getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
         else if(currentPos.getName().contains("Bog") && !isWinter())                                           
            g.drawImage(swamp.getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
         else if(currentPos.getName().contains("Tall_grass"))                                           
            g.drawImage(grass.getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
         else if(currentPos.getName().toLowerCase().contains("forest") && !currentPos.getName().toLowerCase().contains("dead"))                                           
            g.drawImage(forest.getImage(), (SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, SIZE, SIZE, null);
      }
          //draw light effect around player if we have a torch or flaming weapon out
      if((player.getWeapon().getEffect().toLowerCase().contains("fire") && !player.getWeapon().getName().toLowerCase().contains("cannon")) || player.isCampingWithFire() || player.hasEffect("fire") || player.hasItem("blessed-crown"))
      {  //scale the opcacity of the torch flash to the time of day so that it has a greater effect at night/indoors
         int distFromNoon = (int)(Math.abs(time - 12));
         if(TerrainBuilder.indoors())
            distFromNoon = 12;
         int baseOpacity = distFromNoon * 4;
         int opacity = baseOpacity;
         Color glow = Color.yellow;
         if(player.hasItem("blessed-crown"))
         {
            glow = Color.white;
         }
         int radius = (int)(SIZE * 2);
         if(Math.random() < 0.5)
         {  //flicker the glow effect if from fire and not from the crown
            if(Math.random() < 0.5  && !player.hasItem("blessed-crown"))
               glow = Color.orange;
            else
               glow = Color.orange.brighter();
            if(!player.hasItem("blessed-crown"))
            {
               radius = Utilities.rollDie(1, SIZE*3);
            }
            opacity = Math.min(255, Math.max(0,Utilities.rollDie(baseOpacity-25, baseOpacity+25)));
         }
         int xShift = 0;
         if(player.isCampingWithFire() || player.hasEffect("fire") || player.hasItem("blessed-crown"))
            xShift = -1*(radius/4);
         g.setColor(new Color(glow.getRed(),glow.getGreen(),glow.getBlue(),opacity));
         g.fillOval((SCREEN_SIZE/2)*SIZE+xShift, (SCREEN_SIZE/2)*SIZE-(radius/4), radius, radius);
      }//END: draw light effect around player if we have a torch or flaming weapon out
      if(onUs!=null && !onUs.isNonPlayer() && !onUs.isPellet())
      {  //if there is an enemy flying over us, draw it again so that it is on top of the player
         sizeMod = NPC.getSize(onUs);
         xDiff = (int)(SIZE - ((SIZE)*Math.min(2,sizeMod)));  
         if(timestop!=null)
            g.drawImage(onUs.getCurrentPicture().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)((SIZE)*sizeMod), (int)((SIZE)*sizeMod), null);
         else if(numFrames % (animDelay) == 0)   
            g.drawImage(onUs.getPictureAndAdvance().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)((SIZE)*sizeMod), (int)((SIZE)*sizeMod), null);
         else
            g.drawImage(onUs.getCurrentPicture().getImage(), (SCREEN_SIZE/2)*SIZE+xDiff, (SCREEN_SIZE/2)*SIZE+xDiff, (int)((SIZE)*sizeMod), (int)((SIZE)*sizeMod), null);
      }
   }   
   
   public static void drawText(Graphics g, int x, int y, Terrain currentPos)
   {
      Font readable = new Font("Serif", Font.BOLD | Font.ITALIC, SIZE);
      Font readableP = new Font("Serif", Font.PLAIN | Font.ITALIC, SIZE);
      Font handWriten = new Font("Pristina", Font.BOLD | Font.ITALIC, SIZE);
      
      String pname = player.getShortName();
      int focusRow = player.getRow();                        //center of where our map is focused
      int focusCol = player.getCol();
      int mapIndex = player.getMapIndex();
        
      Spell flight = player.getActiveSpell("Flight");
      boolean sailing = player.isSailing();
      int awareness = player.getAwareness();
   
      y = 0;  
      g.setColor(new Color(238, 238, 238));
      g.fillRect(x, y-SIZE, (SCREEN_SIZE*SIZE), (SCREEN_SIZE*SIZE));
      y += SIZE;  
   
      g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
      if(menuMode == TRAVEL)
      {
         Coord xAndy = travelText(g, x, y, currentPos);
         x = xAndy.getRow();
         y = xAndy.getCol();
      }
      else if(menuMode == MAIN)
      {
         g.setColor(new Color(0, 0, 127));
         if(player.getBody() > 0)
            g.drawString("(S)ave your progress.", x, y);
         else
            g.drawString("(L)oad your last save.", x, y);
         y += SIZE;
         g.drawString("(Q)uit and save the game.", x, y);
         y += SIZE;
         g.drawString("(B)uild a new world.", x, y);
         y += SIZE;
         g.drawString("(C)reate a new character.", x, y);
         y += SIZE;
             
         y = (SIZE * SCREEN_SIZE) - (SIZE * 7);
         g.setColor(new Color(0, 0, 0));
         g.setFont(readableP);
         String soundState = "ON";
         if(!CultimaPanel.soundOn)
            soundState = "off";
         g.drawString("(T)oggle sound: "+soundState, x, y);
         y += SIZE;
         g.drawString("(R)ead game information", x, y);
         y += SIZE;
         g.drawString("(M)ap  (I)nventory  (J)ournal(ESC)", x, y);
         y += SIZE;
      }
      else if(menuMode == INVENTORY )
      {
         g.setColor(weaponColor.brighter().brighter());
      //Inventory commands  
         String weapName = player.getWeapon().getName();
         int weaponFreq = player.getWeaponFrequency();
         if(player.canDropWeapon(player.getWeapon()))
         {
            if(weaponFreq > 1)
               g.drawString("(L)eave "+weapName+"("+ weaponFreq +")", x, y);
            else
               g.drawString("(L)eave "+weapName, x, y);
         }
         else
         {
            g.setColor(magicColor.brighter().brighter());   //set color if we can't drop it
            g.drawString(" Weapon:"+weapName, x, y);
         }
         y += SIZE;
         String armName = player.getArmor().getName();
         int armorFreq = player.getArmorFrequencies().get(player.getArmorSelect());  
         if(player.getArmorSelect()!=0)
         {
            g.setColor(weaponColor.brighter().brighter());
            if(armorFreq > 1)
               g.drawString("(R)emove "+armName+"("+ armorFreq +")", x, y);   
            else
               g.drawString("(R)emove "+armName, x, y);
         }
         else
         {
            g.setColor(weaponColor.brighter());             //set color if we can't drop it
            g.drawString(" Armor:"+armName, x, y);   
         }
         y += SIZE;
         y += SIZE;
      
         int xAdd=0;
         Spell spl = player.getSpellInfo();
         String splName = "";
         if(spl != null)
            splName = spl.getName();
         Potion ptn = player.getPotion();
         if(ptn!=null)
         {
            g.setColor(magicColor.brighter().brighter());
            if(ptn.getName().contains("Heal") && player.getBody()==player.getHealthMax())
               g.setColor(unavailable);   //write potion name in gray if we don't need to drink it
            else if(ptn.getName().contains("Cure") && !player.hasEffect("poison"))
               g.setColor(unavailable);
            else if(ptn.getName().contains("Invisibility") && (player.getActiveSpell("Fear")!=null || player.isSailing() || player.onHorse()))    
               g.setColor(unavailable);
            String potionName = ptn.getName();
            if(potionName.length() >= 5)
               potionName = potionName.substring(0,5);	
            g.drawString("(D)rink:"+potionName, x+xAdd, y);
            xAdd += (SIZE*7);
         }
         if(spl!=null)
         {
            g.setColor(magicColor.brighter().brighter());
            if(player.getSpell()==null)         //write spell name in gray if we can't cast it
               g.setColor(unavailable);
            splName += "("+spl.getMannaCost()+":"+player.getManna()+")";  
            g.drawString("(C)ast:"+splName, x+xAdd, y);
            xAdd += (SIZE*10);
         }
      
         y += SIZE;
         y += SIZE;
      
         if(player.getItems().size() > 0 && itemIndex >=0 && itemIndex<player.getItems().size())
         {
            g.setColor(new Color(150, 0, 150));
            String itemName = player.getItems().get(itemIndex);
            if(itemName.contains("treasuremap"))
            {
               Coord treasureLoc = Item.getCoordFromTeasureMap(itemName);
               int treasureRow = treasureLoc.getRow();
               int treasureCol = treasureLoc.getCol();
               treasureRow = (treasureRow/10)*10;
               treasureCol = (treasureCol/10)*10;
               itemName = "treasure near ("+treasureRow+","+treasureCol+")";
            }
            g.drawString("(<>) items:"+itemName, x, y);
            y += SIZE;
            if(player.canRemoveItem(player.getItems().get(itemIndex)))
            {
               String it = new String(player.getItems().get(itemIndex));
               int pos = it.indexOf(":");    //if there is a ":", then we have more than one of this item
               if(pos > 0)                   //remove the frequency
                  it = it.substring(0, pos);
               g.drawString("(T)oss asside "+it, x, y);
            }
            y += SIZE;
            if(player.getItems().get(itemIndex).contains("egg"))
            {
               String it = new String(player.getItems().get(itemIndex));
               int pos = it.indexOf(":");    //if there is a ":", then we have more than one of this item
               if(pos > 0)                   //remove the frequency
                  it = it.substring(0, pos);
               g.drawString("(E)at "+it, x, y);
               y += SIZE;
            }
         }
         y = (SIZE * SCREEN_SIZE) - (SIZE * 10);
         g.setColor(new Color(0, 0, 127));
         y += SIZE;
         if(flight==null && !sailing && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(INSERT/DEL)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                              scroll weapons", x, y);
         }
         y += SIZE;
         if(player.getSpells().size() > 1 && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(HOME/END)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                              scroll spells", x, y);
         }
         y += SIZE; 
         if(player.numPotions() > 1 && player.numArmor() > 1 && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(PG-UP/PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                              scroll potions/armor", x, y);
         }
         else if(player.numPotions() <=1 && player.numArmor() > 1 && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                              scroll armor", x, y);
         }
         else if(player.numPotions() >1 && player.numArmor() <= 1 && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(PG-UP)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                              scroll potions", x, y);
         }
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(K)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                              set hot-keys", x, y);
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(M)ap  (I)nventory  (J)ournal(ESC)", x, y);
         y += SIZE;
      }
      else if(menuMode == WARDROBE )
      {
         g.setColor(weaponColor.brighter().brighter());
      //Inventory commands  
         String weapName = player.getWeapon().getName();
         int weaponFreq = player.getWeaponFrequency();
         if(player.canDropWeapon(player.getWeapon()))
         {
            g.setColor(weaponColor.brighter().brighter());
            if(weaponFreq > 1)
               g.drawString("(L)eave "+weapName+"("+ weaponFreq +") in wardrobe", x, y);
            else
               g.drawString("(L)eave "+weapName+" in wardrobe", x, y);
         }
         else
         {
            g.setColor(magicColor.brighter().brighter());   //set color to red if we can't drop it
            g.drawString(" Weapon:"+weapName, x, y);
         }
         
         int xAdd=0;
      
         y += SIZE;      
         Spell spl = player.getSpellInfo();
         String splName = "";
         if(spl != null)
         {
            splName = spl.getName();
            g.setColor(magicColor.brighter().brighter());
            if(player.getSpell()==null)         //write spell name in gray if we can't cast it
               g.setColor(unavailable);
            g.drawString("Spell:"+splName, x+xAdd, y);
         } 
         
         y += SIZE;
         String armName = player.getArmor().getName();
         int armorFreq = player.getArmorFrequencies().get(player.getArmorSelect());  
         if(player.getArmorSelect()!=0)
         {
            g.setColor(weaponColor.brighter().brighter());
            if(armorFreq > 1)
               g.drawString("(R)emove "+armName+"("+ armorFreq +") to wardrobe", x, y);   
            else
               g.drawString("(R)emove "+armName + " to wardrobe", x, y); 
         }
         else
         {
            g.setColor(weaponColor.brighter());             //set color if we can't drop it
            g.drawString(" Armor:"+armName, x, y);   
         }
         y += SIZE;
                                 
         Potion ptn = player.getPotion();
         if(ptn!=null)
         {
            g.setColor(magicColor.brighter().brighter());
            if(ptn.getName().contains("Heal") && player.getBody()==player.getHealthMax())
               g.setColor(unavailable);   //write potion name in gray if we don't need to drink it
            else if(ptn.getName().contains("Cure") && !player.hasEffect("poison"))
               g.setColor(unavailable);
            String potionName = ptn.getName();
            int potionFreq = player.countPotion(potionName);
            if(potionFreq > 1)
               g.drawString("(D)rop "+potionName+"("+potionFreq + ") to wardrobe", x+xAdd, y);
            else
               g.drawString("(D)rop "+potionName + " to wardrobe", x+xAdd, y);
         }
         y += SIZE; 
         String itemName = "";
         if(player.getItems().size() > 0 && itemIndex >=0 && itemIndex<player.getItems().size())
         {
            itemName = player.getItems().get(itemIndex);
            if(player.canRemoveItem(player.getItems().get(itemIndex)))
            {
               g.setColor(new Color(150, 0, 150));
               g.drawString("(T)oss "+itemName+ " into wardrobe", x, y);
            }
            else
            {
               g.setColor(new Color(150, 0, 0));
               g.drawString(itemName, x, y);
            }
         }
         y += SIZE;
         g.setColor(Color.orange.darker());
         g.drawString("--------Crafting---------", x, y);
         y += SIZE;
         //combine current weapon and current item
         if(itemName.contains("lumber") && (CultimaPanel.player.getNumArrows() < CultimaPanel.player.getMaxArrowCapacity()))
         {
            g.setColor(new Color(0, 150, 0));
            g.drawString("(F)ashion arrows from lumber", x, y);
         }
         else if(Item.canCombine(weapName,itemName))
         { 
            g.setColor(new Color(0, 0, 150));
            if(itemName.length() > 0)
            {
               if(weapName.contains("Magestaff"))
               {
                  g.drawString("(U)pgrade "+weapName+" with Unicorn Horn", x, y);
               }
               else
               {
                  g.drawString("(U)pgrade "+weapName+" with "+itemName, x, y);
               }
            }
         }
         else
         {
            g.setColor(new Color(150, 0, 0));
            g.drawString("Weapon/item combination not upgradable", x, y);
         }
         y += SIZE; 
         if(Item.canCraftArmor(armName,itemName))
         { 
            g.setColor(new Color(0, 0, 150));
            if(itemName.length() > 0)
               g.drawString("(C)raft "+armName+" with "+itemName, x, y);
         }
         else
         {
            g.setColor(new Color(150, 0, 0));
            g.drawString("Armor/item combination not upgradable", x, y);
         }
         y += SIZE;
         if(Item.weaponIsDisassembleable(weapName))
         { 
            g.setColor(new Color(0, 0, 150));
            if(itemName.length() > 0)
               g.drawString("(B)reak gems out of "+weapName, x, y);
         }
         else
         {
            g.setColor(new Color(150, 0, 0));
            g.drawString("Weapon can not be disassembled", x, y);
         }
         y += SIZE;
         
         //show scrollable inventory here*******
         g.setColor(new Color(0, 0, 127));
         if(wardrobeInventory.size() > 0)
         {
            g.drawString("Arrow keys to scroll items to retrieve", x, y);
            y += SIZE;
            Item currItem = wardrobeInventory.get(invIndex);
            boolean canPlayerCarry = true;
            if(currItem instanceof Weapon)
            {
               if(player.hasItem("holdall") && player.numWeapons() > 10)
                  canPlayerCarry = false;
               if(!player.hasItem("holdall") && player.numWeapons() > 3)
                  canPlayerCarry = false;
            }
            else if(currItem instanceof Armor)
            {
               if(player.hasItem("holdall") && player.numArmor() > 10)
                  canPlayerCarry = false;
               if(!player.hasItem("holdall") && player.numArmor() > 2)
                  canPlayerCarry = false;
            }
            else if(currItem instanceof Potion)
            {
               if(player.hasItem("holdall") && player.numPotions() > 20)
                  canPlayerCarry = false;
               if(!player.hasItem("holdall") && player.numPotions() > 4)
                  canPlayerCarry = false;    
            }
            g.setColor(new Color(0, 0, 127));
            g.fillRect(x, y-SIZE+(SIZE/4) ,  (SIZE * SCREEN_SIZE), (SIZE));
            if(canPlayerCarry)
               g.setColor(new Color(0, 127, 0));
            else
               g.setColor(new Color(127, 0, 0));
            g.drawString("(B)ring:" , x, y);
            y += SIZE; 
            for(int i=0; i<3; i++)
            {
               if(invIndex + i < wardrobeInventory.size())
               {
                  if(i==0)
                  {
                     g.setColor(new Color(0, 0, 127));
                     g.fillRect(x, y-SIZE+(SIZE/4) ,  (SIZE * SCREEN_SIZE), (SIZE));
                     g.setColor(Color.gray);
                  }
                  else
                     g.setColor(new Color(127, 0, 0));
                  Item nextItem = wardrobeInventory.get(invIndex + i);
                  Integer nextItemFreq = wardrobeFreq.get(invIndex + i);
                  String extraInfo = "";
                  if(nextItem instanceof Armor)
                     extraInfo = " armor";
                  g.drawString(nextItem.getName()+extraInfo+"("+nextItemFreq+")", x, y);
                  y += SIZE;
               }
            }
         }
         //*************************************
         y = (SIZE * SCREEN_SIZE) - (SIZE * 11);
         g.setColor(new Color(0, 0, 127));
         y += SIZE;
         if(flight==null && !sailing && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(INSERT/DEL)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll weapons", x, y);
         }
         y += SIZE;
         if(player.getSpells().size() > 1 && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(HOME/END)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll spells", x, y);
         }
         y += SIZE; 
         if(player.numPotions() > 1 && player.numArmor() > 1 && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(PG-UP/PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions/armor", x, y);
         }
         else if(player.numPotions() <=1 && player.numArmor() > 1 && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll armor", x, y);
         }
         else if(player.numPotions() >1 && player.numArmor() <= 1 && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(PG-UP)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions", x, y);
         }
         y += SIZE;
         if(player.getItems().size() > 0)
         {
            g.setFont(readableP);
            g.drawString("(</>)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll items", x, y);
         }
      
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(K)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                            set hot-keys", x, y);
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(M)ap  (I)nventory  (J)ournal(ESC)", x, y);
         y += SIZE;
      } 
      else if(menuMode == HOTKEYS )
      {
         g.setColor(new Color(0, 0, 127));		         //draw a blue boarder around the board
         g.fillRect(0, 0, (SCREEN_SIZE*SIZE), (SCREEN_SIZE*SIZE));
         x = SIZE;
         y = SIZE;   
         g.setColor(new Color(255, 100, 100));
         g.drawString("Weapon Hot-keys:", x, y);
         y += SIZE;
         g.setColor(Color.orange);
         for(int i=0; i<player.weaponHotKeys.length; i++)
         {
            byte weaponType = (byte)(player.weaponHotKeys[i].getRow());
            byte weaponSelect = (byte)(player.weaponHotKeys[i].getCol());
            ArrayList<Weapon>[] weapons = player.getWeapons();
            String weapName = "none";
            if(weaponType >=0 && weaponType < weapons.length && weapons[weaponType].size() > 0 && weaponSelect >= 0 && weaponSelect < weapons[weaponType].size())
               weapName = (weapons[weaponType].get(weaponSelect)).getName();    
            g.drawString("F"+(i+1)+": "+weapName, x, y);
            y += SIZE;
         }
      
         y += SIZE;
         g.setColor(new Color(100, 100, 255));
         g.drawString("Spell Hot-keys:", x, y);
         y += SIZE;
         g.setColor(Color.orange);
         for(int i=0; i<player.spellHotKeys.length; i++)
         {
            byte spellSelect = (byte)(player.spellHotKeys[i]);
            ArrayList<Spell> spells = player.getSpells();
            String spellName = "none";
            if(spellSelect >=0 && spellSelect < spells.size())
               spellName = (spells.get(spellSelect)).getName();
            g.drawString("F"+(i+5)+": "+spellName, x, y);
            y += SIZE;
         }
      
         y += SIZE;
         g.setColor(new Color(100, 255, 100));
         g.drawString("Potion Hot-keys:", x, y);
         y += SIZE;
         g.setColor(Color.orange);
         int ptnIndex = 0;
         int [] potionHotKeys = player.potionHotKeys;
         for(byte i=1; i<=10; i++)  //show in order 1-9, then 0 to match the keyboard
         {
            String ptnName = "none";
            boolean ptnFound = false;
            for(byte j=0; j<potionHotKeys.length; j++)
            {
               byte ptnSelect = (byte)(potionHotKeys[j]);
               if(i%10==ptnSelect)
               {
                  Potion ptn = Potion.getPotionWithNum(j);
                  ptnFound = true;
                  if(ptn != null)
                  {
                     ptnName = ptn.getName();
                     break;
                  }
               }
            }
            if(ptnName.equals("none"))
               g.drawString("   "+(i%10)+": "+ptnName, x, y);
            else
               g.drawString("   "+(i%10)+": "+ptnName+"("+player.countPotion(ptnName)+")", x, y);
            y += SIZE;
         }
                 
         x = SIZE * SCREEN_SIZE;
         y = (SIZE); 
         Weapon weapSell = player.getWeapon(); 
         int weaponFreq = player.getWeaponFrequency();  
         g.setColor(new Color(150, 0, 0));
         g.drawString(" Weapon:"+weapSell.getName(), x, y);
         y += SIZE;
         g.setColor(new Color(0, 0, 150));
         Spell spl = player.getSpellInfo();
         String splName = "";
         if(spl != null)
         {
            splName = spl.getName();
            g.drawString(" Spell:"+splName, x, y);
            y += SIZE;
         }  
         Potion ptn = player.getPotion();
         if(ptn!=null)
         {
            g.setColor(new Color(0, 150, 0));
            String potionName = ptn.getName();
            g.drawString(" Potion:"+potionName, x, y);
         }
      
         y = (SIZE * SCREEN_SIZE) - (SIZE * 10);
         g.setColor(new Color(0, 0, 127));
         y += SIZE;
         if(flight==null && !sailing)
         {
            g.setFont(readableP);
            g.drawString("(INSERT/DEL)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll weapons", x, y);
         }
         y += SIZE;
         if(player.getSpells().size() > 1)
         {
            g.setFont(readableP);
            g.drawString("(HOME/END)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll spells", x, y);
         }
         y += SIZE; 
         if(player.numPotions() > 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-UP)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions", x, y);
         }
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(K)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                            exit hot-keys", x, y);
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(M)ap  (I)nventory  (J)ournal(ESC)", x, y);
         y += SIZE;
      }
      else if(menuMode == SHOPPE_ARMORY || menuMode == SHOPPE_TRADER)
      {
         g.setColor(weaponColor.brighter().brighter());
      //Inventory commands
         Weapon weapSell = player.getWeapon(); 
         int weaponFreq = player.getWeaponFrequency();  
         if(player.canDropWeapon(weapSell))
         {
            g.setColor(new Color(0, 127, 0));
            if(weaponFreq > 1)
            {
               g.drawString("(S)ell "+weapSell.getName()+"("+ weaponFreq +") for " + weapSell.getValue()/3 + " gold", x, y);
            }
            else //if(weaponFreq == 1)
            {
               g.drawString("(S)ell "+weapSell.getName()+" for " + weapSell.getValue()/3 + " gold", x, y);
            }
         }
         else
         {
            g.setColor(magicColor.brighter().brighter());   //set color if we can't drop it
            g.drawString(" Weapon:"+weapSell.getName(), x, y);
         }
         y += SIZE;
         Armor armSell = player.getArmor(); 
         int armorFreq = player.getArmorFrequencies().get(player.getArmorSelect());  
         if(player.getArmorSelect() > 0)
         {
            g.setColor(weaponColor.brighter().brighter());
            if(armorFreq > 1)
            {
               g.drawString("(T)rade "+armSell.getName()+"("+ armorFreq +") for " + armSell.getValue()/3 + " gold", x, y);
            }
            else  //if(armorFreq == 1)
            {
               g.drawString("(T)rade "+armSell.getName()+" for " + armSell.getValue()/3 + " gold", x, y);
            }
         }
         else
         {
            g.setColor(weaponColor.brighter());             
            g.drawString(" Armor:"+armSell.getName(), x, y);   
         }
         y += SIZE;
         g.drawString("Arrow keys to scroll items to purchase", x, y);
         y += SIZE;
      
         Item currItem = null;
         if(menuMode == SHOPPE_ARMORY)
         {
            currItem = armoryInventory.get(invIndex);
         }
         else //if(menuMode == SHOPPE_TRADER)
         {
            currItem = traderInventory.get(invIndex);
         }
         boolean canPlayerCarry = true;
         if(currItem instanceof Weapon)
         {
            if(player.hasItem("holdall") && player.numWeapons() > 10)
               canPlayerCarry = false;
            if(!player.hasItem("holdall") && player.numWeapons() > 3)
               canPlayerCarry = false;
            if(player.hasMagicStaff() && currItem.getName().equals("Staff")) 
               canPlayerCarry = false;   
         }
         else if(currItem instanceof Armor)
         {
            if(player.hasItem("holdall") && player.numArmor() > 10)
               canPlayerCarry = false;
            if(!player.hasItem("holdall") && player.numArmor() > 2)
               canPlayerCarry = false;
         }
         g.setColor(new Color(0, 0, 127));
         g.fillRect(x, y-SIZE+(SIZE/4) ,  (SIZE * SCREEN_SIZE), (SIZE));
         if(currItem.isBook())
         {  //set color to red if we have all items for that volume in our journal
            if(Item.doWeHaveBook(currItem))  
               g.setColor(new Color(127, 0, 0));           
            else
               g.setColor(new Color(0, 127, 0));
         }
         else if(player.getGold() >= currItem.getValue() && canPlayerCarry)
         {
            if(currItem.getName().toLowerCase().contains("arrow") && player.getNumArrows() >= player.getMaxArrowCapacity())
            {
               g.setColor(new Color(127, 0, 0));
            }
            else if(currItem instanceof Armor || currItem instanceof Weapon || !player.hasItem(currItem.getName()))
            {
               g.setColor(new Color(0, 127, 0));
            }
            else
            {  //if it is not a weapon or armor and we already have the item
               g.setColor(new Color(127, 0, 0));
            }
         }
         else
         {
            g.setColor(new Color(127, 0, 0));
         }
         g.drawString("(B)uy:" , x, y);      //only show buy if we have enough money for current item
         y += SIZE; 
         for(int i=0; i<5; i++)
         {
            if(menuMode == SHOPPE_ARMORY)
            {
               if(invIndex + i < armoryInventory.size())
               {
                  if(i==0)
                  {
                     g.setColor(new Color(0, 0, 127));
                     g.fillRect(x, y-SIZE+(SIZE/4) ,  (SIZE * SCREEN_SIZE), (SIZE));
                     g.setColor(Color.gray);
                  }
                  else
                     g.setColor(new Color(127, 0, 0));
                  Item nextItem = armoryInventory.get(invIndex + i);
                  String extraInfo = "";
                  if(nextItem instanceof Armor)
                     extraInfo = " armor";
                  g.drawString(nextItem.getName()+extraInfo+", "+nextItem.getValue()+" gold", x, y);
                  y += SIZE;
               }
            }
            else //if(menuMode == SHOPPE_TRADER)
            {
               if(invIndex + i < traderInventory.size())
               {
                  if(i==0)
                  {
                     g.setColor(new Color(0, 0, 127));
                     g.fillRect(x, y-SIZE+(SIZE/4) ,  (SIZE * SCREEN_SIZE), (SIZE));
                     g.setColor(Color.gray);
                  }
                  else
                     g.setColor(new Color(127, 0, 0));
                  Item nextItem = traderInventory.get(invIndex + i);
                  String extraInfo = "";
                  if(nextItem instanceof Armor)
                     extraInfo = " armor";
                  g.drawString(nextItem.getName()+extraInfo+", "+nextItem.getValue()+" gold", x, y);
                  y += SIZE;
               }
            }
         }
        
         y = (SIZE * SCREEN_SIZE) - (SIZE * 10);
         g.setColor(new Color(0, 0, 127));
         y += SIZE;
      
         g.setFont(readableP);
         g.drawString("(INSERT/DEL)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                            scroll weapons", x, y);
         y += SIZE;
         if(player.getSpells().size() > 1)
         {
            g.setFont(readableP);
            g.drawString("(HOME/END)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll spells", x, y);
         }
         y += SIZE; 
         if(player.numPotions() > 1 && player.numArmor() > 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-UP/PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions/armor", x, y);
         }
         else if(player.numPotions() <=1 && player.numArmor() > 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll armor", x, y);
         }
         else if(player.numPotions() >1 && player.numArmor() <= 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-UP)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions", x, y);
         }
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(ESCAPE)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                            to exit shoppe", x, y);            y += SIZE;
      }
      else if(menuMode == SHOPPE_MAGIC)
      {
         g.setColor(new Color(0, 127, 0));       
         g.drawString("Arrow keys to scroll items to purchase", x, y);
         y += SIZE;
      
         Item currItem = magicInventory.get(invIndex);
         boolean canPlayerCarry = true;
         if(currItem instanceof Potion)
         {
            if(player.hasItem("holdall") && player.numPotions() > 20)
               canPlayerCarry = false;
            if(!player.hasItem("holdall") && player.numPotions() > 4)
               canPlayerCarry = false;     
         }
         else if(currItem instanceof Weapon)
         {
            if(!player.hasMagicStaff() && !currItem.getName().equals("Staff")) 
               canPlayerCarry = false;   
            if(player.hasMagicStaff() && currItem.getName().equals("Staff")) 
               canPlayerCarry = false;    
         }
         boolean hasItem = false;
         if(!(currItem instanceof Potion) && player.hasItem(currItem.getName()))
            hasItem = true;
         g.setColor(new Color(0, 0, 127));
         g.fillRect(x, y-SIZE+(SIZE/4) ,  (SIZE * SCREEN_SIZE), (SIZE));
      
         if(currItem.isBook())
         {  //set color to red if we have all items for that volume in our journal
            if(Item.doWeHaveBook(currItem))  
               g.setColor(new Color(127, 0, 0));           
            else
               g.setColor(new Color(0, 127, 0));
         }
         else if(player.getGold() >= currItem.getValue() && !hasItem && canPlayerCarry)
            g.setColor(new Color(0, 127, 0));
         else
            g.setColor(new Color(127, 0, 0));
         g.drawString("(B)uy:" , x, y);      //only show buy if we have enough money for current item
         y += SIZE; 
         for(int i=0; i<5; i++)
         {
            if(invIndex + i < magicInventory.size())
            {
               if(i==0)
               {
                  g.setColor(new Color(0, 0, 127));
                  g.fillRect(x, y-SIZE+(SIZE/4) ,  (SIZE * SCREEN_SIZE), (SIZE));
                  g.setColor(Color.gray);
               }
               else
                  g.setColor(new Color(127, 0, 0));
               Item nextItem = magicInventory.get(invIndex + i);
               String extraInfo = "";
               if(nextItem instanceof Potion)
                  extraInfo = " potion";
               g.drawString(nextItem.getName()+extraInfo+", "+nextItem.getValue()+" gold", x, y);
               y += SIZE;
            }
         }
       
         y = (SIZE * SCREEN_SIZE) - (SIZE * 10);
         g.setColor(new Color(0, 0, 127));
         y += SIZE;
      
         g.setFont(readableP);
         g.drawString("(INSERT/DEL)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                            scroll weapons", x, y);
         y += SIZE;
         if(player.getSpells().size() > 1)
         {
            g.setFont(readableP);
            g.drawString("(HOME/END)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll spells", x, y);
         }
         y += SIZE; 
         if(player.numPotions() > 1 && player.numArmor() > 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-UP/PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions/armor", x, y);
         }
         else if(player.numPotions() <=1 && player.numArmor() > 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll armor", x, y);
         }
         else if(player.numPotions() >1 && player.numArmor() <= 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-UP)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions", x, y);
         }
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(ESCAPE)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                            to exit shoppe", x, y);
         y += SIZE;
      }
      else if(menuMode == MAP)
      {
         g.setColor(new Color(0, 0, 150));
         if(mapIndex == 0)
         {
            g.drawString("I am at East:"+player.getCol()+", South:"+player.getRow(), x, y);
            y += SIZE;
            g.drawString("Wind: ", x, y);
            if(TerrainBuilder.indoors())  //no wind reading when indoors
               g.drawImage(windDirs[windDirs.length-1].getImage(), (int)(x+(SIZE*3)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
            else
               g.drawImage(windDirs[CultimaPanel.windDir].getImage(), (int)(x+(SIZE*3)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
            y += SIZE;
            if(player.getMapMarkMapIndex()!=-1 && player.getMapMarkRow()!=-1 && player.getMapMarkCol()!=-1)
            {
               String locName = "World";
               if(player.getMapMarkMapIndex()!=0)
               {
                  Location markLoc = allLocations.get(player.getMapMarkMapIndex());
                  if(markLoc != null)
                     locName = markLoc.getName();
               }
               g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
               g.drawString("Logged loc:", x, y);
               g.setFont(readable);
               g.drawString(locName + " ("+player.getMapMarkCol()+","+player.getMapMarkRow()+")", x+(SIZE*6), y);
            }
            g.setFont(readable);
            y += SIZE;
            ArrayList<Location> closest = closestLocationsTo(player.getWorldRow(), player.getWorldCol());
            if(player.getMapIndex()==0)
               closest = closestLocationsTo(focusRow, focusCol);
         
            int numShown = 0;
            g.setColor(new Color(0, 127, 127));
            for(Location loc:closest)
            {
               int diffSouth = player.getRow() - loc.getRow();
               int diffEast = player.getCol() - loc.getCol();
               if(diffSouth < 0 && diffEast < 0)            //loc is South/East of us
                  g.drawString(loc.getName() + " is " +Math.abs(diffEast) + " East & " + Math.abs(diffSouth) + " South.", x, y);
               else if(diffSouth > 0 && diffEast < 0)      //loc is North/East of us
                  g.drawString(loc.getName() + " is " +Math.abs(diffEast) + " East & " + Math.abs(diffSouth) + " North.", x, y);
               else if(diffSouth < 0 && diffEast > 0)      //loc is South/West of us
                  g.drawString(loc.getName() + " is " +Math.abs(diffEast) + " West & " + Math.abs(diffSouth) + " South.", x, y);
               else if(diffSouth > 0 && diffEast > 0)      //loc is North/West of us
                  g.drawString(loc.getName() + " is " +Math.abs(diffEast) + " West & " + Math.abs(diffSouth) + " North.", x, y);
               else if(diffSouth == 0 && diffEast < 0)            
                  g.drawString(loc.getName() + " is " +Math.abs(diffEast) + " East.", x, y);
               else if(diffSouth == 0 && diffEast > 0)            
                  g.drawString(loc.getName() + " is " +Math.abs(diffEast) + " West.", x, y);
               else if(diffSouth < 0 && diffEast == 0)            
                  g.drawString(loc.getName() + " is " +Math.abs(diffSouth) + " South.", x, y);
               else if(diffSouth > 0 && diffEast == 0)            
                  g.drawString(loc.getName() + " is " +Math.abs(diffSouth) + " North.", x, y);
               y += SIZE;
               numShown++;
               if(numShown > awareness)
                  break;
            }
         }
         else
         {
            Location inLocation = closestLocationTo(player.getWorldRow(), player.getWorldCol());
            String where = "?";
            if(inLocation != null)
               where = inLocation.getName();
            if(player.getLocationType().toLowerCase().contains("underworld"))
               where = "The Underworld";
            else if(player.getLocationType().toLowerCase().contains("jurassica"))
               where = "Jurassica";
            else if(player.getLocationType().toLowerCase().contains("1942"))
               where = "1942";
            else if(player.getLocationType().toLowerCase().contains("graboid"))
               where = "Graboid-innards";
            else if(player.getLocationType().toLowerCase().contains("beast"))
               where = "Beast-innards";
            else if(player.getLocationType().toLowerCase().contains("wolfenstein"))
               where = "Castle-Wolfen";
            else if(player.getLocationType().toLowerCase().contains("pacrealm"))
               where = "The Pac-Realm";
            g.drawString("I am in ", x, y);
            g.setFont(readable);
            g.drawString(""+where, x+(SIZE*4), y);
            g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
            y += SIZE;
            if(player.isInSpecialRealm())
               g.drawString("East:?  South:?", x, y);
            else
               g.drawString("East:"+player.getWorldCol()+"  South:"+player.getWorldRow(), x, y);
            y += SIZE;
            g.drawString("Wind: ", x, y);
            if(TerrainBuilder.indoors())  //no wind reading when indoors
               g.drawImage(windDirs[windDirs.length-1].getImage(), (int)(x+(SIZE*3)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
            else
               g.drawImage(windDirs[CultimaPanel.windDir].getImage(), (int)(x+(SIZE*3)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
            y += SIZE;
         }
         //show most 5 recent mission onjectives
         y = (SIZE * SCREEN_SIZE) - (SIZE * 16);
         int endY = y + (SIZE * 7);    //the lowest y position we want to write to
         if(missionStack.size() > 0)
         {
            g.setColor(new Color(127, 0, 0));
            g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
            g.drawString("(D)iscard top mission (<>) to scroll", x, y);
            y += SIZE;
            g.setFont(readable);
            if(missionStack!=null && missionStack.size() > 0)
            {
               g.setColor(new Color(127, 0, 127));
               for(int i=missionStack.size()-1-invIndex; i>=(missionStack.size()-1-invIndex)-8 && i>=0; i--)
               {
                  Mission miss = missionStack.get(i);
                  if(miss.isFinished())
                     g.setColor(new Color(0, 0, 0));
                  else
                  {
                     if(i==missionStack.size()-1-invIndex)
                     {
                        g.setColor(new Color(127, 0, 100));
                     }
                     else
                     {
                        g.setColor(new Color(127, 0, 127));
                     }
                  }
                  y = Journal.showSentenceWordWrap(miss.getInfo(),  x,  y,  g);
                  if(y >= endY)
                  {
                     break;
                  }
               }
            }
         }
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
      
         y = (SIZE * SCREEN_SIZE) - (SIZE * 8);
         g.setColor(new Color(0, 0, 0));
         y += SIZE;
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(L)og position         (C)lear log", x, y);
         y += SIZE;
         g.drawString("(M)ap  (I)nventory  (J)ournal(ESC)", x, y);
         y += SIZE;
      }
   
   //show scrolling message array
      y = (SIZE * SCREEN_SIZE) - (SIZE * 4);
      if(talkSel && selected!=null)
         g.setColor(talkColor);
      else
         g.setColor(new Color(100, 100, 255));
      g.fillRect(x, y-SIZE ,  (SIZE * SCREEN_SIZE), (SIZE * 5));
      g.setColor(new Color(60, 0, 0));
      g.setFont(readable);
      y = y - (SIZE/4);
      for(int i=0; i<message.length; i++)
      {
         String sentence =message[i];
         if(i == message.length-1 && typed.length() > 0)
            sentence = "~"+typed;
         String temp = sentence.toLowerCase();
         if(!temp.startsWith("~"))           //dialogue with NPCs will start with a ~
         {
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString(sentence, x, y);
         }
         else
         {
            g.setFont(handWriten);
            String [] words = sentence.split(" ");
            int xAdd = 0;
            for(int j=0; j<words.length; j++)
            {
               g.setColor(new Color(0, 0, 0));
               String tempWord = new String(words[j]);
               if(tempWord.startsWith("~"))
                  tempWord = tempWord.substring(1);
               if(Utilities.isFollowUpWord(tempWord))
               {
                  g.setColor(new Color(20, 20, 150));
                  if(j==0 || tempWord.contains("GARRIOTT")  || tempWord.contains("SKARA") || tempWord.contains("CAERBANNOG") || tempWord.contains("CULTIMA")) 
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
   
   //show important information as text as we are traveling about
   public static Coord travelText(Graphics g, int x, int y, Terrain currentPos)
   {
      String pname = player.getShortName();
      int focusRow = player.getRow();                        //center of where our map is focused
      int focusCol = player.getCol();
      int mapIndex = player.getMapIndex();
   
      String pArmor = CultimaPanel.player.getArmor().getName().toLowerCase();   
      boolean hasKnowing = (CultimaPanel.player.getActiveSpell("Knowing")!=null || pArmor.contains("knowing"));         
      Spell flight = player.getActiveSpell("Flight");
      boolean sailing = player.isSailing();
      int awareness = player.getAwareness();
   
      Font readable = new Font("Serif", Font.BOLD | Font.ITALIC, SIZE);
      Font readableP = new Font("Serif", Font.PLAIN | Font.ITALIC, SIZE);
      Font handWriten = new Font("Pristina", Font.BOLD | Font.ITALIC, SIZE);
   
      if(player.hasEffect("poison"))
      {
         if(player.getPainTime() > numFrames)
            g.setColor(new Color(0, 127, 0).brighter().brighter());
         else
            g.setColor(new Color(0, 127, 0));
      }
      else if(player.hasEffect("cursed") || player.hasEffect("fire"))
      {
         if(player.getPainTime() > numFrames)
            g.setColor(new Color(127, 0, 0).brighter().brighter());
         else
            g.setColor(new Color(127, 0, 0));
      }            
      else if(player.hasEffect("blessed"))
      {
         if(player.getPainTime() > numFrames)
            g.setColor(new Color(127, 10, 127).brighter().brighter());
         else
            g.setColor(new Color(127, 10, 127)); 
      }
      else if(player.hasEffect("freeze"))
      {
         if(player.getPainTime() > numFrames)
            g.setColor(Color.cyan);
         else
            g.setColor(new Color(0, 0, 127)); 
      }
      else    
      {
         if(player.getPainTime() > numFrames)
            g.setColor(new Color(0, 0, 127).brighter().brighter());
         else
            g.setColor(new Color(0, 0, 127));
      }
      g.fillRect(x, y-SIZE,  (SIZE * SCREEN_SIZE), (SIZE+(SIZE/4)));
      if(player.getPainTime() > numFrames)
         g.setColor(new Color(0, 0, 0));
      else
         g.setColor(new Color(255, 0, 0));
      
      String effects = "";
      if(player.getEffects()!=null && player.getEffects().size() > 0 && (player.getEffects().toString()).length()>2)
      {
         effects = ","+player.getEffects().toString().substring(1, player.getEffects().toString().length()-1);
            //if cursed and sullied, only show cursed
         if(effects.contains("curse"))
         {
            if(effects.contains("sullied"))
               effects = effects.replace(", sullied", "");
         }
         if(effects.contains("seduced"))
         {
            if(effects.contains("sullied"))
               effects = effects.replace(", sullied", "");
            if(effects.contains("sullied"))
               effects = effects.replace(",sullied", "");
         }
      }
      String weapName = player.getWeapon().getName(); 
      int weaponFreq = player.getWeaponFrequency();  
      if(weapName.toLowerCase().contains("bow") || weapName.toLowerCase().contains("caster"))  //bow, so show number of arrows in quill
         weapName += "("+player.getNumArrows()+")"; 
      else if(weapName.toLowerCase().contains("throw") || weapName.toLowerCase().contains("sling"))  //show number of stones in satchel
         weapName += "("+player.getNumStones()+")";    
      else if((weapName.toLowerCase().contains("trap") || weapName.toLowerCase().contains("dagger") || weapName.toLowerCase().contains("spear")) && weaponFreq > 1)
         weapName += "("+weaponFreq+")";
      if(player.getImageIndex()==Player.STAFF && !weapName.toLowerCase().contains("staff"))    //magic weapon, so show manna required    
      {
         int mannaCost = player.getWeapon().getMannaCost();
         weapName += "("+mannaCost+":"+player.getManna()+")";   
      }
      g.drawString(pname+":("+player.getBody()+"/"+player.getHealthMax()+")"+effects+":"+weapName, x, y);
      y += SIZE;
      
      String where = "?";
      g.setColor(new Color(127, 0, 0));
      if(CultimaPanel.allLocations != null && CultimaPanel.allLocations.size() > 0)
      {
         where = closestProvinceTo(focusRow, focusCol);
      }
      if(mapIndex == 0)
      {
         g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
         g.drawString("Province: ", x, y);
         g.setFont(readable);
         g.drawString(""+where, x+(SIZE*5), y);
         g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
         
         y += SIZE;
         g.drawString("East:"+focusCol+"  South:"+focusRow + "  Wind: ", x, y);
         if(TerrainBuilder.indoors())  //no wind reading when indoors
            g.drawImage(windDirs[windDirs.length-1].getImage(), (int)(x+(SIZE*13.25)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
         else
            g.drawImage(windDirs[CultimaPanel.windDir].getImage(), (int)(x+(SIZE*13.25)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
         y += SIZE;
      }
      else
      {
         Location inLocation = allLocations.get(mapIndex);
         where = "?";
         if(inLocation != null)
            where = inLocation.getName();
         if(player.getLocationType().toLowerCase().contains("underworld"))
            where = "The Underworld"; 
         else if(player.getLocationType().toLowerCase().contains("jurassica"))
            where = "Jurassica";  
         else if(player.getLocationType().toLowerCase().contains("1942"))
            where = "1942";
         else if(player.getLocationType().toLowerCase().contains("graboid"))
            where = "Graboid-innards";
         else if(player.getLocationType().toLowerCase().contains("beast"))
            where = "Beast-innards";
         else if(player.getLocationType().toLowerCase().contains("wolfenstein"))
            where = "Castle-Wolfen";
         else if(player.getLocationType().toLowerCase().contains("pacrealm"))
            where = "The Pac-Realm";   
         g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
         g.drawString("Location: ", x, y);
         g.setFont(readable);
         g.drawString(""+where, x+(SIZE*5), y);
         g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
         
         y += SIZE;
         if(player.isInSpecialRealm())
            g.drawString("East: ?   South: ?  Wind: ", x, y);
         else
            g.drawString("East:"+player.getWorldCol()+"  South:"+player.getWorldRow() + "  Wind: ", x, y);
         if(TerrainBuilder.indoors())  //no wind reading when indoors
            g.drawImage(windDirs[windDirs.length-1].getImage(), (int)(x+(SIZE*13.25)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
         else
            g.drawImage(windDirs[CultimaPanel.windDir].getImage(), (int)(x+(SIZE*13.25)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
         y += SIZE;  
      }
      String description = currentPos.getDescription().replace('_', ' ');
      if(player.getLocationType().contains("graboid"))
         description = "Graboid-innards";
      else if(player.getLocationType().contains("beast"))
         description = "Beast-innards";
      else if(player.getLocationType().toLowerCase().contains("underworld") && well1!=null && well2!=null && currentPos.getName().contains("water"))
      {
         if(player.getRow()==well1.getRow() && player.getCol()==well1.getCol())
            description = "Poison Well I";
         else if(player.getRow()==well2.getRow() && player.getCol()==well2.getCol())
            description = "Poison Well II";
         else 
            description = "Moat";   
      }  
      int trapDist = closeTrapOrSecret[0];
      int secretDist = closeTrapOrSecret[1];
      if(trapDist>=0)    
      {
         int denom = awareness - (trapDist-1);
         if(denom==0)
            denom = 1;
         if(numFrames % (animDelay*6/denom) == 0)
         {
            description = description + " trap";
         }
      }
      if(secretDist>=0)
      {
         int denom = awareness - (secretDist-1);
         if(denom==0)
            denom = 1;
         if(numFrames % (animDelay*6/denom) == 0)
         {
            description = description + " secret";
         }
      }
      g.drawString("Terrain : ", x, y);
      g.setFont(readable);
      g.drawString(""+description, x+(SIZE*5), y);
      
      y += SIZE;
      String season = getSeason();
      if(player.isInSpecialRealm())
         g.drawString("Day ?", x, y);
      else
         g.drawString(season + ", Day "+ days +", "+timeOfDay(time), x, y);
      y += SIZE;
      g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE));
      if(talkSel)
      {
         g.setColor(talkColor.darker());
         if(selected!=null)
         {
            g.drawString("What have you to say?  'ESC' to exit", x, y);
            y += SIZE;
            if(selected.isOurDog() || (selected.getCharIndex()==NPC.DOG && !selected.hasBeenAttacked()))
            {
               g.drawString("Try common dog commands:", x, y);
               y += SIZE;
               g.drawString("attack, come, go, stay, treat", x, y);
               y += SIZE;
            }
            else
            {
               g.drawString("Try: name, job, home, and", x, y);
               y += SIZE;
               g.drawString("echo keywords in response", x, y);
               y += SIZE;
            }
            y += SIZE;
               //show character name
             
            if(selected.isOurDog())
            {
               if(selected.getCharIndex() == NPC.WOLF)
               {
                  g.drawString("Our trusted "+selected.getName(), x, y);
               }
               else
               {
                  g.drawString("Our trusted "+NPC.characterDescription(selected.getCharIndex()).toLowerCase()+" "+selected.getName(), x, y);
               }
            }
            else
            {
               String reputation = "";
               if(player.getAwarenessRaw() >= 2 || hasKnowing)
                  reputation = selected.getReputationName();
               String npcName =  Utilities.shortName(selected.getName());
               if(npcName.startsWith("~"))   //vampire tag
                  npcName = npcName.substring(1);
               else if(npcName.contains("~"))   //werewolf tag
               {
                  String [] parts = npcName.split("~");
                  npcName = parts[0];
               }  
               g.drawString(reputation + " " + selected.getCharDescription() + " " + npcName, x, y);
               y += SIZE;
               java.util.List<String> expert = NPC.getExpertise(selected);
               if(expert.size() > 0)
               {
                  String exList = expert.toString();
                  exList = exList.substring(1, exList.length()-1);
                  y = Journal.showSentenceWordWrap("Expertise: "+exList,  x,  y,  g);
               }
            }
         }
         else
            g.drawString("Arrow keys for direction to talk towards", x, y);
         y += SIZE;
      }
      else if(player.isCamping())
      {
         if(!player.getDogBasicInfo().equals("none"))
            g.setColor(new Color(50, 0, 100));
         else
            g.setColor(new Color(0, 0, 150));
         if(description.toLowerCase().contains("bed"))
            g.drawString("Sleeping in a soft bed", x, y);
         else  if(description.toLowerCase().contains("coffin"))
            g.drawString("Restoring powers in a coffin", x, y);
         else
            g.drawString("Resting next to a crackling fire", x, y);
         y += SIZE;
         if(!player.getDogBasicInfo().equals("none"))
            g.drawString("under the watch of a guard dog", x, y);
         y += SIZE;
         g.drawString("Tap a key to try to wake up", x, y);
         y += SIZE;
      }
      else if(player.hasEffect("web"))
      {
         g.setColor(new Color(0, 150, 0));
         g.drawString("Trapped in a spider's web!", x, y);
         y += SIZE;
         y += SIZE;
         g.drawString("Tap a key to try to escape!", x, y);
         y += SIZE;
      }
      else if((attackSel || attackSelAlt) && selected==null)
      {
         g.setColor(new Color(150, 0, 0));
         if(Weapon.isLongStaff(weapName) && !attackSel && attackSelAlt)
         {
            g.drawString("Arrow keys for vault direction", x, y);
         }
         else if(weapName.toLowerCase().contains("advance"))
         {
            if(attackSel)
               g.drawString("Arrow keys for teleport direction", x, y);
            else
               g.drawString("Arrow keys for direction to strike", x, y);
         }
         else if(weapName.toLowerCase().contains("trap"))
            g.drawString("Arrow keys for direction to place trap", x, y);
         else if(!attackSel && attackSelAlt)
            g.drawString("Arrow keys for alternate attack", x, y);
         else if(attackSel)
            g.drawString("Arrow keys for direction to attack", x, y);
         y += SIZE;
      }
      else if(examineSel && selected==null  && selectedTerrain==null)
      {
         g.setColor(new Color(0, 0, 150));
         g.drawString("Arrow keys for direction to examine", x, y);
         y += SIZE;
      }
      else if(renovateSel && Utilities.inOurHouse() && selectedRenovate==null)
      {
         g.setColor(new Color(0, 0, 150));
         g.drawString("Arrow keys for space to renovate", x, y);
         y += SIZE;
      }
      else if(stealSel)
      {
         g.setColor(new Color(0, 0, 150));
         g.drawString("Arrow keys for direction to steal", x, y);
         y += SIZE;
      }
      else if(breakSel)
      {
         g.setColor(new Color(0, 0, 150));
         g.drawString("Arrow keys for direction to break", x, y);
         y += SIZE;
      }
      else if(fireSel)
      {
         g.setColor(new Color(0, 0, 150));
         g.drawString("Arrow keys for direction to ignite", x, y);
         y += SIZE;
      }
      else if(pushSel)
      {
         g.setColor(new Color(0, 0, 150));
         if(selectedBarrel==null && Utilities.nextToOurWardrobe()==-1 && Utilities.nextToOurBed(CultimaPanel.player.getRow(), CultimaPanel.player.getCol())==-1)
         {
            g.drawString("Arrow keys for direction of item to push", x, y);
         }
         else
         {
            g.drawString("Arrow keys for direction to push the item", x,y);
         }
         y += SIZE;
      }
      else if(selected!=null && !talkSel && !attackSel)
      {
         if(NPC.isCivilian(selected.getCharIndex()))
            g.setColor(new Color(0, 0, 150));
         else  if(selected.getCharIndex() == NPC.KING)
            g.setColor(new Color(125, 0, 125));
         else if(NPC.isTameAnimal(selected.getCharIndex()))
            g.setColor(new Color(0, 75, 0));
         else if(selected.getCharIndex() == NPC.WHIRLPOOL || selected.getCharIndex() == NPC.TORNADO || selected.getCharIndex() == NPC.FIRE || selected.getCharIndex() == NPC.MONOLITH  || selected.getCharIndex() == NPC.STONE || selected.isStatue() || NPC.isFurniture(selected) || (selected.getCharIndex() == NPC.MIMIC && !selected.hasBeenAttacked()))
            g.setColor(Color.black);
         else if(selected.getCharIndex() == NPC.GREATSHIP || selected.getCharIndex() == NPC.BRIGANDSHIP || selected.getCharIndex() == NPC.BOAT)
            g.setColor(new Color(58, 58, 58));
         else if(NPC.isUnusualMonster(selected.getCharIndex()) || selected.getCharIndex() == NPC.WILLOWISP)
            g.setColor(new Color(200, 10, 10));
         else
            g.setColor(new Color(150, 0, 0));
         String reputation = "";
         if(player.getAwarenessRaw() >= 2 || hasKnowing)
            reputation = selected.getReputationName();
         String level = "less adept";
         if(selected.getLevel() > player.getLevel())
            level = "more adept";   
         String might = "weaker";
         if(selected.getMight() > player.getMight())
            might = "stronger";
         String mind = " less clever";
         if(selected.getMind() > player.getMind())
            mind = "more clever";
         String agile = " less swift";
         if(selected.getAgilityRaw() > player.getAgility())
            agile = "more swift";  
         String body = " less sturdy";
         if(selected.getBody() > player.getBody())
            body = "sturdier";
            
         if(selected.hasMet())
         {
            if(selected.getCharIndex()==NPC.HORSE || selected.getCharIndex()==NPC.UNICORN || selected.getCharIndex()==NPC.GREATSHIP || selected.getCharIndex()==NPC.BRIGANDSHIP || selected.getCharIndex()==NPC.BOAT)
            {
               if(selected.getCharIndex()==NPC.BOAT)
                  g.drawString(selected.getCharDescription(), x, y);
               else
                  g.drawString(pname + "'s " + selected.getCharDescription(), x, y);
            }
            else if(NPC.isTameableCanine(selected.getCharIndex()))
            {
               if(selected.isOurDog() || selected.getName().contains("+"))
               {
                  if(selected.getCharIndex() == NPC.WOLF)
                  {
                     g.drawString("Our trusted "+selected.getName(), x, y);
                  }
                  else
                  {
                     g.drawString("Our trusted "+NPC.characterDescription(selected.getCharIndex()).toLowerCase()+" "+selected.getName(), x, y);
                  }
               }
               else
               {
                  g.drawString(reputation + " " + selected.getName(), x, y);
               }
            }
            else
            {
               if(selected.getName().contains("Minion"))             //minion
               {
                  g.drawString(CultimaPanel.player.getShortName()+"'s Minion", x, y);
               }
               else if(selected.getName().toLowerCase().contains("cultist"))
               {
                  g.drawString("Enthralled cultist", x, y);
               }
               else if(selected.isPrisoner())
               {
                  g.drawString("Prisoner", x, y);
               }
               else if(selected.isZombie())        //zombie
               {
                  g.drawString("Vile Deadite", x, y);
               }
               else if(selected.getCharIndex()==NPC.WILLOWISP)
               {
                  g.drawString("Mysterious light", x, y);
               }
               else if(selected.getCharIndex()==NPC.FIRE || selected.getCharIndex()==NPC.TORNADO)
               {
                  if(selected.getNumInfo() == 0)
                  {
                     g.drawString(selected.getName(), x, y);
                  }
                  else
                  {
                     if(selected.getCharIndex()==NPC.TORNADO)
                     {
                        g.drawString("Wind Elemental", x, y);
                     }
                     else
                     {
                        g.drawString(selected.getName() + " Elemental", x, y);
                     }
                  }
               }
               else if(selected.getCharIndex()==NPC.MIMIC && !selected.hasBeenAttacked())
               {
                  if(awareness == 0)
                     g.drawString("Chest", x, y);
                  else if(awareness == 1)
                     g.drawString("Ordinary chest", x, y);
                  else if(awareness == 2 )
                     g.drawString("Just an ordinary chest...", x, y);
                  else if(awareness == 3)
                     g.drawString("Suspicious chest", x, y);
                  else //if(awareness == 4)
                     g.drawString("Certainly a Mimic!", x, y);
               }
               else if(selected.isStatue() || NPC.isFurniture(selected))   //statue or barrel
               {
                  g.drawString(selected.getName(), x, y);
               }
               else
               {
                  String npcName =  Utilities.shortName(selected.getName());
                  if(npcName.startsWith("~"))   //vampire tag
                     npcName = npcName.substring(1);
                  else if(npcName.contains("~"))   //werewolf tag
                  {
                     String [] parts = npcName.split("~");
                     npcName = parts[0];
                  }
                  g.drawString(reputation + " " + selected.getCharDescription() + " " +npcName, x, y);
               }
            }
         }
         else  //if(!selected.hasMet())
         {
            if(selected.getName().contains("Minion"))             //minion
            {
               g.drawString(CultimaPanel.player.getShortName()+"'s Minion", x, y);
            }
            else if(selected.getName().toLowerCase().contains("cultist"))
            {
               g.drawString("Enthralled cultist", x, y);
            }
            else if(selected.isPrisoner())
            {
               g.drawString("Prisoner", x, y);
            }
            else if(selected.isZombie())
            {
               g.drawString("Vile Deadite", x, y);
            }
            else if(selected.getCharIndex()==NPC.WILLOWISP)
            {
               g.drawString("Mysterious light", x, y);
            }
            else if(selected.getCharIndex()==NPC.FIRE || selected.getCharIndex()==NPC.TORNADO)
            {
               if(selected.getNumInfo() == 0)
               {
                  g.drawString(selected.getName(), x, y);
               }
               else
               {
                  if(selected.getCharIndex()==NPC.TORNADO)
                  {
                     g.drawString("Wind Elemental", x, y);
                  }
                  else
                  {
                     g.drawString(selected.getName() + " Elemental", x, y);
                  }
               }
            }
            else if(selected.getCharIndex()==NPC.MIMIC && !selected.hasBeenAttacked())
            {
               if(awareness == 0)
                  g.drawString("Chest", x, y);
               else if(awareness == 1)
                  g.drawString("Ordinary chest", x, y);
               else if(awareness == 2 )
                  g.drawString("Just an ordinary chest...", x, y);
               else if(awareness == 3)
                  g.drawString("Suspicious chest", x, y);
               else //if(awareness == 4)
                  g.drawString("Certainly a Mimic!", x, y);
            }
            else if(selected.isStatue() || NPC.isFurniture(selected))   //statue
            {
               g.drawString(selected.getName(), x, y);
            }
            else
            {
               String npcName =  selected.getName();
               if(selected.isVampire())                           //vampire tag
                  g.drawString(reputation + " Vampyre", x, y);
               else if(selected.isWerewolf())                     //werewolf tag
               {
                  if(isNight)
                     g.drawString(reputation + " Werewolf", x, y);
                  else
                     g.drawString(reputation + " " + selected.getCharDescription(), x, y);
               }
               else
                  g.drawString(reputation + " " + selected.getCharDescription(), x, y);
            }
         }
         y += SIZE;
         if(!selected.isStatue() && !NPC.isFurniture(selected) && selected.getCharIndex()!=NPC.WILLOWISP && !NPC.isShip(selected.getCharIndex()) && !(selected.getCharIndex()==NPC.MIMIC && !selected.hasBeenAttacked()) )   //statue
         {
            if(player.getAwarenessRaw()>=3 || hasKnowing)
            {
               g.drawString("Level: "+level+" than thou at "+selected.getLevel(), x, y);
               y += SIZE;
               g.drawString("Might: "+might+" than thou at "+selected.getMight(), x, y);
               y += SIZE;
               g.drawString("Mind: "+mind+" than thou at "+selected.getMind(), x, y);
               y += SIZE;
               g.drawString("Agile: "+agile+" than thou at "+selected.getAgilityRaw(), x, y);
               y += SIZE;
               g.drawString("Health: "+body+" than thou at "+selected.getBody(), x, y);
               y += SIZE;
            }
            else if(player.getAwarenessRaw()>=0)
            {
               g.drawString("Level: "+level+" than thou.", x, y);
               y += SIZE;
               g.drawString("Might: "+might+" than thou.", x, y);
               y += SIZE;
               if(player.getAwarenessRaw()>=1)
               {
                  g.drawString("Mind: "+mind+" than thou.", x, y);
                  y += SIZE;
                  g.drawString("Agile: "+agile+" than thou.", x, y);
                  y += SIZE;
                  if(player.getAwarenessRaw() == 2)
                  {
                     g.drawString("Health: "+body+" than thou.", x, y);
                     y += SIZE;
                  }
               }
            }
         }
      }
      else if(selectedTerrain !=null)
      {
         g.setColor(new Color(0, 0, 150));
         String descr = selectedTerrain.getDescription().replace('_',' ');
         if(player.getLocationType().toLowerCase().contains("sewer") && descr.toLowerCase().contains("wood"))
         {
            descr = "Clogging lumber";
         }
         else if(TerrainBuilder.isCity(player.getLocationType()) && descr.toLowerCase().contains("lava"))
         {
            descr = "Hellmouth";
         }
         else if(player.getLocationType().contains("graboid"))
         {
            descr = "Graboid-innards";
         }
         else if(player.getLocationType().contains("beast"))
         {
            descr = "Beast-innards"; 
         }
         if(descr.toLowerCase().endsWith("trap"))
         {
            int pos = descr.indexOf("trap");
            descr = descr.substring(0, pos);
            if(numFrames % ((animDelay*3)/(awareness+1)) == 0)
            {
               descr = descr + " trap";
            }
         }
         else if(selectedTerrain.getName().contains("_D_") && (selectedTerrain.getName().contains("Wall") || selectedTerrain.getName().contains("Rock")))
         {  //examine secret wall
            if(numFrames % ((animDelay*3)/(awareness+1)) == 0)
            {
               descr = descr + " secret";
            }
         }
         else if(player.getLocationType().toLowerCase().contains("sewer"))
         {
            if(descr.toLowerCase().contains("poison"))
            {
               descr = "Revolting sewage";
            }
            else if(descr.toLowerCase().contains("bog"))
            {
               descr = "Smelly sludge";
            }
            else if(descr.toLowerCase().contains("water"))
            {
               descr = "Tepid waste-water";
            }
         }
         g.drawString(descr, x, y);
         y += SIZE;
         if(selectedTerrain.getName().contains("_I_"))
         {
            g.drawString("'Tis impossible to pass by foot!", x, y);
            y += SIZE;
         }
         else if(selectedTerrain.getName().contains("_V_"))
         {
            g.drawString("Quite difficult passing by foot.", x, y);
            y += SIZE;
         }
         else if(selectedTerrain.getName().contains("_S_"))
         {
            g.drawString("'Twill be slow going by foot.", x, y);
            y += SIZE;
         }
      }
      else 
      {
         int xAdd = 0;
         g.setColor(new Color(0, 0, 150));
         g.drawString("(E)xamine", x+xAdd, y);
         xAdd += (SIZE*5);
         if(flight==null && !player.isWolfForm() && ((player.hasItem("lockpick") || player.hasItem("magicpick")) && ((Utilities.nextToALock(map.get(mapIndex), player.getRow(), player.getCol())
            || (!Utilities.nextToALockOrTrap(map.get(mapIndex), player.getRow(), player.getCol()).equals("none") && awareness==4)))))
         {
            g.setColor(new Color(150, 0, 150));
            g.drawString("(P)ick", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(flight==null && (Utilities.nextToABarrelSimple() || Utilities.nextToOurWardrobe()!=-1 || Utilities.nextToOurBed(CultimaPanel.player.getRow(), CultimaPanel.player.getCol())!=-1))
         {  //if we are next to a barrel, give the option to push or pull it to an adjacent empty location (followed by getting a direction to move to)
            g.setColor(new Color(0, 0, 150));
            g.drawString("(P)ush", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(flight==null && Utilities.onHouseAdjacentGround() && !isWinter())
         {  
            g.setColor(new Color(0, 0, 150));
            g.drawString("(P)lant", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(flight==null && Utilities.inOurHouse() && selectedRenovate==null)
         {
            g.setColor(new Color(0, 0, 150));
            g.drawString("(R)enovate", x+xAdd, y);
            xAdd += (SIZE*6);
         }
         else if(flight==null && player.canPickupStone() && player.isOnStone(currentPos))
         {
            g.setColor(new Color(0, 0, 150));
            g.drawString("(P)ickup rock", x+xAdd, y);
            xAdd += (SIZE*7);
         }
         else if(flight==null && player.canBuildBridge())
         {
            g.setColor(new Color(0, 0, 150));
            g.drawString("(B)uild bridge", x+xAdd, y);
            xAdd += (SIZE*7);
         }
         if(flight==null && player.canHide(currentPos))
         {
            g.setColor(new Color(150, 0, 150));
            g.drawString("(H)ide", x+xAdd, y);
            xAdd += (int)(SIZE*3.5);
         }
         if(flight==null && CultimaPanel.doorToClose==null && !player.onBedAndTimeToSleep() && !player.isWolfForm() && ((Utilities.chestInRange() || CultimaPanel.nextToNPCtoPickpocket) && CultimaPanel.humanNPCInSight))
         {
            g.setColor(new Color(150, 0, 0));
            g.drawString("(S)teal", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         if(Utilities.canBreakWall())
         {
            g.setColor(new Color(150, 0, 0));
            g.drawString("(B)reak", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(flight==null && player.canCutDownTree())
         {
            g.setColor(new Color(0, 0, 150));
            g.drawString("(F)ell tree", x+xAdd, y);
            xAdd += (SIZE*7);
         }
         else if(Utilities.canSetOnFire() && !player.getLocationType().toLowerCase().contains("underworld"))
         {
            g.setColor(new Color(150, 0, 0));
            g.drawString("(F)ire", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(player.getLocationType().toLowerCase().contains("underworld") && well1!=null && well2!=null && currentPos.getName().contains("water"))
         {
            g.setColor(new Color(150, 0, 150));
            g.drawString("(F)ill Goblet", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(player.getLocationType().toLowerCase().contains("cave") && door1!=null && door2!=null && door3!=null  && tower1!=null && tower2!=null && tower3!=null)
         {
            int [] tower = null;
            if(player.getRow()==door1.getRow()+1 && player.getCol()==door1.getCol()) 
            {
               tower = tower1.clone();
            }
            else if(player.getRow()==door2.getRow()+1 && player.getCol()==door2.getCol()) 
            {
               tower = tower2.clone();
            }
            else if(player.getRow()==door3.getRow()+1 && player.getCol()==door3.getCol()) 
            {
               tower = tower3.clone();
            }
            if(tower!=null)
            {
               boolean towerHasDisks = false;
               for(int twr=0; twr<tower.length; twr++)
                  if(tower[twr] >= 1)
                  {
                     towerHasDisks = true;
                     break;
                  }
               int topDisk = tower.length-1;
               while(topDisk > 0 && tower[topDisk]!=0)
                  topDisk--;
               g.setColor(new Color(150, 0, 150));
               if(towerHasDisks)
                  g.drawString("(P)ick Disk", x+xAdd, y);
               else if(player.getWellNumber()>=1 && player.getWellNumber() < tower[topDisk])
                  g.drawString("(P)ut Disk", x+xAdd, y);
               xAdd += (SIZE*5);
            }
         }
            
         y += SIZE;
         xAdd = 0;
         if(numNPCsToTalk > 0)
         {
            g.setColor(talkColor.darker());
            g.drawString("(T)alk", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         if(weapName.toLowerCase().contains("advance"))
         {
            if(player.getManna() >= player.getWeapon().getMannaCost())
            {
               g.setColor(new Color(150, 0, 150));
               if(CultimaPanel.inRangeToAttackAlt)
               {
                  g.drawString("Z/(A)dvan", x+xAdd, y);
               }
               else
               {
                  g.drawString("(A)dvanc", x+xAdd, y);
               }
               xAdd += (SIZE*5);
            }
            else  //not enough manna to cast advance spell
            {  
               if(CultimaPanel.inRangeToAttackAlt)
               {
                  g.setColor(new Color(150, 0, 0));
                  g.drawString("(Z)strike", x+xAdd, y);
                  xAdd += (SIZE*5);
               }
            }
         }
         else if(weapName.toLowerCase().contains("trap")&& Utilities.canPlaceTrap())
         {
            g.setColor(new Color(150, 0, 0));
            g.drawString("(A)trap", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(Weapon.isSpell(weapName) && CultimaPanel.player.getManna() < CultimaPanel.player.getWeapon().getMannaCost() && !CultimaPanel.inRangeToAttackAlt)
         {
         }
         else if(Weapon.isSpell(weapName) && ((CultimaPanel.player.getManna() >= CultimaPanel.player.getWeapon().getMannaCost() && CultimaPanel.inRangeToAttack) || CultimaPanel.inRangeToAttackAlt))
         {
            if(CultimaPanel.player.getManna() < CultimaPanel.player.getWeapon().getMannaCost() && CultimaPanel.inRangeToAttackAlt)
            {
               g.setColor(weaponColor.brighter().brighter());
               g.drawString("(Z)strike", x+xAdd, y);
            }
            else  //we have enough manna
            {
               g.setColor(magicColor.brighter().brighter());
               if(CultimaPanel.inRangeToAttack && CultimaPanel.inRangeToAttackAlt)
                  g.drawString("Z/(A)ttack", x+xAdd, y);
               else
                  g.drawString("(A)ttack", x+xAdd, y);
            }
            xAdd += (SIZE*5);
         }
         else if(CultimaPanel.inRangeToAttack && (CultimaPanel.inRangeToAttackAlt || ((Utilities.nextToACityWallSimple() || Utilities.nextToACounterSimple()) && Weapon.isLongStaff(weapName))))
         {//only have attack as an option if NPC is in range of the current weapon
            g.setColor(new Color(150, 0, 0));
            g.drawString("Z/(A)ttack", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(CultimaPanel.inRangeToAttack)
         {//only have attack as an option if NPC is in range of the current weapon
            g.setColor(new Color(150, 0, 0));
            g.drawString("(A)ttack", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         else if(CultimaPanel.inRangeToAttackAlt || ((Utilities.nextToACityWallSimple() || Utilities.nextToACounterSimple()) && Weapon.isLongStaff(weapName)))
         {//only have attack as an option if NPC is in range of the current weapon
            g.setColor(new Color(150, 0, 0));
            String toShow = "(Z)attack";
            String term = Weapon.getAltTerm(weapName);
            if(Weapon.getAltTerm(weapName) != null)
            {
               toShow = "(Z)"+ term;
            }
            g.drawString(toShow, x+xAdd, y);
            xAdd += (SIZE*5);
         }
         g.setColor(new Color(0, 0, 150));
         g.drawString("(W)ait", x+xAdd, y);
         xAdd += (SIZE*5);
         NPCPlayer feedable = getInRangeToFeed;
         if(feedable!=null)
         {//only have attack as an option if NPC is in range of the current weapon
            g.setColor(new Color(150, 0, 150));
            if(NPC.isHorse(feedable))
               g.drawString("(T)oss flowers", x+xAdd, y);
            else
               g.drawString("(T)oss rations", x+xAdd, y);
            xAdd += (SIZE*5);
         }
         y += SIZE;
         xAdd = 0;
         Spell spl = player.getSpellInfo();
         String splName = "";
         if(spl != null)
            splName = spl.getName();
         Potion ptn = player.getPotion();
         if(player.getLocationType().toLowerCase().contains("underworld") && well1!=null && well2!=null && player.getWellNumber()>=0)
         {
            g.setColor(new Color(0, 150, 0));
            g.drawString("(D)rink goblet", x+xAdd, y);
            xAdd += (SIZE*7);
         }
         else if(ptn!=null)
         {
            g.setColor(magicColor.brighter().brighter());
            if(ptn.getName().contains("Heal") && player.getBody()==player.getHealthMax())
               g.setColor(unavailable);   //write potion name in grey if we don't need to drink it
            else if(ptn.getName().contains("Cure") && !player.hasEffect("poison"))
               g.setColor(unavailable);
            else if(ptn.getName().contains("Invisibility") && (player.getActiveSpell("Fear")!=null || player.isSailing() || player.onHorse()))    
               g.setColor(unavailable);
            String potionName = ptn.getName();
            if(potionName.length() >= 5)
               potionName = potionName.substring(0,5);	
            g.drawString("(D)rink:"+potionName, x+xAdd, y);
            xAdd += (SIZE*7);
         }
         if(flight==null && spl!=null && !player.isWolfForm())
         {
            g.setColor(magicColor.brighter().brighter());
            if(player.getSpell()==null)         //write spell name in gray if we can't cast it
               g.setColor(unavailable);
            splName += "("+spl.getMannaCost()+":"+player.getManna()+")";  
            g.drawString("(C)ast:"+splName, x+xAdd, y);
            xAdd += (SIZE*10);
         }
         y += SIZE;
         Corpse cp = CultimaPanel.onACorpse;
         if(cp != null)
         {
            g.setColor(new Color(0, 0, 150));
            Armor arm = cp.getArmor();
            Weapon weap = cp.getWeapon();
            if(arm!=null)
            {
               String armName = arm.getName().toLowerCase();
               if(armName.contains("fur") || armName.contains("skin") || armName.contains("pelt") || armName.contains("hide"))
               {
                  if(NPC.isFurniture(cp.getCharIndex()))
                  {
                     if(cp.getCharIndex() == NPC.BARREL)
                     {
                        g.drawString("Barrel contained: "+arm.getName(), x, y);
                     }
                     else
                     {
                        g.drawString("Furniture contained: "+arm.getName(), x, y);
                     }
                  }
                  else
                     g.drawString("Corpse hide: "+arm.getName(), x, y);
                  y += SIZE;
                  g.setColor(new Color(0, 150, 0));
                  g.drawString("K)eep pelt", x, y);
                  y += SIZE;
               }
               else
               {
                  if(NPC.isFurniture(cp.getCharIndex()))
                  {
                     if(cp.getCharIndex() == NPC.BARREL)
                     {
                        g.drawString("Barrel contained: "+arm.getName(), x, y);
                     }
                     else
                     {
                        g.drawString("Furniture contained: "+arm.getName(), x, y);
                     }
                  }
                  else
                     g.drawString("Corpse wears "+arm.getName(), x, y);
                  y += SIZE;
                  g.setColor(new Color(0, 150, 0));
                  g.drawString("K)eep armor, [ to Swap armor", x, y);
                  y += SIZE;
               }
            }
            if(weap!=null)
            {
               g.setColor(new Color(0, 0, 150));
               if(cp.getCharIndex()==-1)
               {
                  g.drawString("Retrieved "+weap.getName(), x, y);
               }
               else if(NPC.isFurniture(cp.getCharIndex()))
               {
                  if(cp.getCharIndex() == NPC.BARREL)
                  {
                     g.drawString("Barrel contained: "+weap.getName(), x, y);
                  }
                  else
                  {
                     g.drawString("Furniture contained: "+weap.getName(), x, y);
                  }
               }
               else
               {
                  g.drawString("Corpse clutches "+weap.getName(), x, y);
               }
               y += SIZE;
               g.setColor(new Color(0, 150, 0));
               g.drawString("L)oot weapon, ] to Swap weapon", x, y);
               y += SIZE;
            }
         }
         else
         {
            y += SIZE;
            //show strongest NPCPlayer in view, giving priority to NPCs that are mission targets, have missions and monsters
            NPCPlayer NPCinView = CultimaPanel.greatestNPCinSight;
            if(hasKnowing && missionTarget!=null)
               NPCinView = missionTarget;
            if(NPCinView != null && NPCinView.getMapIndex()==player.getMapIndex())
            {
               if(NPCinView.getCharIndex()==NPC.PHANTOM && TerrainBuilder.habitablePlace(CultimaPanel.player.getLocationType()))
               {
                  CultimaPanel.ghostInView = true;
               }
               else
               {
                  CultimaPanel.ghostInView = false;
               }
               Color infoColor = new Color(150, 0, 0);
               int missionPriority = NPCinView.getMissionPriority();
               if(hasKnowing && missionTarget!=null)
                  missionPriority = 3;
               if(missionPriority == 1)
                  infoColor = (new Color(0, 115, 0));          //green
               else if(missionPriority == 2)
                  infoColor = (new Color(0, 0, 150));          //blue
               else if(missionPriority == 3)
                  infoColor = (new Color(150, 0, 0));          //red
               else if(NPC.isCivilian(NPCinView.getCharIndex()))
                  infoColor = (new Color(0, 0, 150));          //blue
               else  if(NPCinView.getCharIndex() == NPC.KING)
                  infoColor = (new Color(125, 0, 125));
               else if(NPC.isTameAnimal(NPCinView.getCharIndex()))
                  infoColor = (new Color(0, 150, 0));
               else if(NPCinView.getCharIndex() == NPC.WHIRLPOOL || NPCinView.getCharIndex() == NPC.TORNADO)
                  infoColor = (Color.black);
               else if(NPCinView.getCharIndex() == NPC.GREATSHIP || NPCinView.getCharIndex() == NPC.BRIGANDSHIP || NPCinView.getCharIndex() == NPC.BOAT)
                  infoColor = (new Color(58, 58, 58));
               else if(NPC.isUnusualMonster(NPCinView.getCharIndex()))
                  infoColor = (new Color(200, 10, 10));
               else
                  infoColor = (new Color(150, 0, 0));
               if(missionPriority > 0)
                  g.setColor(infoColor.brighter());
               else
                  g.setColor(infoColor);   
               if(NPCinView.isNonPlayer())
               {
                  String fullName = "Mission item:";
                  g.drawString(fullName, x, y);
                  if(missionPriority > 0)
                  {
                     int width = g.getFontMetrics().stringWidth(fullName);
                     byte directionTo = TerrainBuilder.getDirectionTowards(player.getRow(),player.getCol(),NPCinView.getRow(),NPCinView.getCol());
                     javax.swing.ImageIcon arrow = missionArrows[directionTo];
                     g.drawImage(arrow.getImage(), (int)(x+(width)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
                  }
               }   
               else
               {
                  String reputation = "";
                  if(player.getAwarenessRaw() >= 2 || hasKnowing)
                     reputation = NPCinView.getReputationName();
                  String level = "less adept";
                  if(NPCinView.getLevel() > player.getLevel())
                     level = "more adept";   
                  String body = " less sturdy";
                  if(NPCinView.getBody() > player.getBody())
                     body = "sturdier";
                  if(NPCinView.hasMet())
                  {
                     if(NPCinView.getCharIndex()==NPC.HORSE || NPCinView.getCharIndex()==NPC.UNICORN || NPCinView.getCharIndex()==NPC.GREATSHIP || NPCinView.getCharIndex()==NPC.BRIGANDSHIP || NPCinView.getCharIndex()==NPC.BOAT)
                     {
                        if(NPCinView.getCharIndex()==NPC.BOAT)
                           g.drawString(NPCinView.getCharDescription(), x, y);
                        else
                           g.drawString(pname + "'s " + NPCinView.getCharDescription(), x, y);
                     }
                     else if(NPC.isTameableCanine(NPCinView.getCharIndex()))
                     {
                        if(NPCinView.isOurDog() && NPCinView.getCharIndex()!=NPC.WOLF)
                           g.drawString(NPCinView.getName() +" the "+ NPC.characterDescription(NPCinView.getCharIndex()).toLowerCase(), x, y);
                        else
                           g.drawString(reputation + " " + NPCinView.getName(), x, y);
                     }
                     else
                     {
                        if(NPCinView.getName().toLowerCase().contains("cultist"))
                        {
                           g.drawString("Enthralled cultist", x, y);
                        }
                        else if(NPCinView.isPrisoner())
                        {
                           g.drawString("Prisoner", x, y);
                        }
                        else if(NPCinView.isZombie())
                        {
                           g.drawString("Vile Deadite", x, y);
                        }
                        else if(NPCinView.getCharIndex()==NPC.WILLOWISP)
                        {
                           g.drawString("Mysterious light", x, y);
                        }
                        else if(NPCinView.getCharIndex()==NPC.FIRE || NPCinView.getCharIndex()==NPC.TORNADO)
                        {
                           if(NPCinView.getNumInfo() == 0)
                           {
                              g.drawString(NPCinView.getName(), x, y);
                           }
                           else
                           {
                              if(NPCinView.getCharIndex()==NPC.TORNADO)
                              {
                                 g.drawString("Wind Elemental", x, y);
                              }
                              else
                              {
                                 g.drawString(NPCinView.getName() + " Elemental", x, y);
                              }
                           }
                        }
                        else if(NPCinView.getCharIndex()==NPC.MIMIC && !NPCinView.hasBeenAttacked())
                        {
                           if(awareness == 0)
                              g.drawString("Chest", x, y);
                           else if(awareness == 1)
                              g.drawString("Ordinary chest", x, y);
                           else if(awareness == 2 )
                              g.drawString("Just an ordinary chest...", x, y);
                           else if(awareness == 3)
                              g.drawString("Suspicious chest", x, y);
                           else //if(awareness == 4)
                              g.drawString("Certainly a Mimic!", x, y);
                        }
                        else
                        {
                           String npcName =  Utilities.shortName(NPCinView.getName());
                           if(NPCinView.isVampire())                    //vampire tag
                              npcName = npcName.substring(1);
                           else if(NPCinView.isWerewolf())              //werewolf tag
                           {
                              String [] parts = npcName.split("~");
                              npcName = parts[0];
                           }
                           String fullName = reputation + " " + NPCinView.getCharDescription() + " " + npcName; 
                           if(missionPriority > 0)
                              fullName += ":";
                           g.drawString(fullName, x, y);
                           if(missionPriority > 0)
                           {
                              int width = g.getFontMetrics().stringWidth(fullName);
                              byte directionTo = TerrainBuilder.getDirectionTowards(player.getRow(),player.getCol(),NPCinView.getRow(),NPCinView.getCol());
                              javax.swing.ImageIcon arrow = missionArrows[directionTo];
                              g.drawImage(arrow.getImage(), (int)(x+(width)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
                           }
                        }
                     }
                  }
                  else
                  {
                     if(NPCinView.getName().toLowerCase().contains("cultist"))
                     {
                        g.drawString("Enthralled cultist", x, y);
                     }
                     else if(NPCinView.isPrisoner())
                     {
                        g.drawString("Prisoner", x, y);
                     }
                     else if(NPCinView.isZombie())
                     {
                        g.drawString("Vile Deadite", x, y);
                     }
                     else if(NPCinView.isVampire())                        //vampire tag
                     {
                        g.drawString(reputation + " Vampyre", x, y);
                     }
                     else if(NPCinView.getLocationType().contains("pacrealm"))
                     {
                        g.drawString(NPCinView.getName(), x, y);
                     }
                     else if(NPCinView.isWerewolf())                       //werewolf tag
                     {
                        String fullName = " Woman";
                        if(NPCinView.getName().contains("~M"))
                           fullName = " Man";
                        if(isNight)
                           fullName = " Werewolf";
                        if(missionPriority > 0)
                           fullName += ":";
                        g.drawString(fullName, x, y);
                        if(missionPriority > 0)
                        {
                           int width = g.getFontMetrics().stringWidth(fullName);
                           byte directionTo = TerrainBuilder.getDirectionTowards(player.getRow(),player.getCol(),NPCinView.getRow(),NPCinView.getCol());
                           javax.swing.ImageIcon arrow = missionArrows[directionTo];
                           g.drawImage(arrow.getImage(), (int)(x+(width)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
                        }
                     }
                     else
                     {
                        String fullName = reputation + " " + NPCinView.getCharDescription(); 
                        if(missionPriority > 0)
                           fullName += ":";
                        g.drawString(fullName, x, y);
                        if(missionPriority > 0)
                        {
                           int width = g.getFontMetrics().stringWidth(fullName);
                           byte directionTo = TerrainBuilder.getDirectionTowards(player.getRow(),player.getCol(),NPCinView.getRow(),NPCinView.getCol());
                           javax.swing.ImageIcon arrow = missionArrows[directionTo];
                           g.drawImage(arrow.getImage(), (int)(x+(width)), (int)(y-(SIZE*0.75)), SIZE, SIZE, null);
                        }
                     }
                  }
                  g.setColor(infoColor);   
                  y += SIZE;
                  if(player.getAwarenessRaw()>=3 || hasKnowing)
                  {
                     g.drawString("Level: "+level+" than thou at "+NPCinView.getLevel(), x, y);
                     y += SIZE;
                     g.drawString("Health: "+body+" than thou at "+NPCinView.getBody(), x, y);
                     y += SIZE;
                  }
                  else if(player.getAwarenessRaw()>=0)
                  {
                     g.drawString("Level: "+level+" than thou.", x, y);
                     y += SIZE;
                     if(player.getAwarenessRaw() == 2)
                     {
                        
                        g.drawString("Health: "+body+" than thou.", x, y);
                        y += SIZE;
                     }
                  }
               }
            }
         }
      }
      g.setColor(new Color(0, 0, 127));
         
      y = (SIZE * SCREEN_SIZE) - (SIZE * 11);
      if(player.getLocationType().equals("battlefield"))
      {
         if(numBattleFieldEnemies == 0)
         {
            g.drawString("Battle Status:", x, y);
            g.setColor(new Color(0, 127, 0));
            g.drawString("                          won", x, y);
         }
         else
         {
            g.drawString("Battle Status:", x, y);
            g.setColor(new Color(127, 0, 0));
            g.drawString("                          undecided", x, y);
         }
      }
      else if(player.getLocationType().equals("ship"))
      {
         if(numEnemiesOnShip == 0)
         {
            g.drawString("Battle Status:", x, y);
            g.setColor(new Color(0, 127, 0));
            g.drawString("                          won - take the helm", x, y);
         }
         else
         {
            g.drawString("Battle Status:", x, y);
            g.setColor(new Color(127, 0, 0));
            g.drawString("                          undecided", x, y);
         }
      }
      g.setColor(new Color(0, 0, 127));
      y += SIZE;
      
      int xAdd = 0;
      if(TerrainBuilder.habitablePlace(player.getLocationType()) && CultimaPanel.doorToClose != null)
      {
         g.drawString("(S)hut door", x, y);
      }
      else if(player.onBedAndTimeToSleep() || (player.canSleep() && !((Utilities.chestInRange() || CultimaPanel.nextToNPCtoPickpocket) && CultimaPanel.humanNPCInSight)))
      {
         if(player.onBedAndTimeToSleep())
         {
            if(CultimaPanel.player.onCoffinAndNotVampire())
            {  //give player a subtle warning that sleeping in a coffin is dangerous
               g.setColor(new Color(127, 0, 0));
            }
            else
            {
               g.setColor(new Color(0, 0, 127));
            }
            g.drawString("(S)leep", x, y);
         }
         else
            g.drawString("(S)et Camp", x, y);
      }
      if(!player.getDogName().equals("none") && !Utilities.inOurHouse())
      {   
         boolean inStay = false;
         NPCPlayer dog = Utilities.getDog();
         if(dog!=null && dog.getMoveType()!=NPC.CHASE)
            inStay = true; 
         if(inStay)
         {
            xAdd += (SIZE*7);    
            g.setColor(new Color(127, 0, 0));
            g.drawString("(R)ecall dog", x+xAdd, y);
         }
      }
      else if(flight==null && Utilities.inOurHouse() && selectedRenovate != null)
      {
         xAdd += (SIZE*7);    
         g.setColor(new Color(0, 127, 0));
         g.drawString("<,> to scroll tiles", x+xAdd, y);
      }
      if(!player.getNpcName().equals("none"))
      {   
         boolean inStay = false;
         NPCPlayer escortee = Utilities.getNpc();
         if(escortee!=null)
         {
            xAdd += (SIZE*7);    
            g.setColor(new Color(0, 127, 0));
            g.drawString("(R)esume, (W)ait", x+xAdd, y);
         }
      }
      y += SIZE;
      if(talkSel && selected!=null)
      {
         g.setFont(readableP);
         g.drawString("(RIGHT)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                           echo keyword", x, y);
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(LEFT/RIGHT)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                           scroll common", x, y);
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(UP)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                           repeat last", x, y);
         y += SIZE;
         g.setFont(readableP);
         g.drawString("(DOWN)", x, y);
         g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
         g.drawString("                           end conversation", x, y);
         y += SIZE;
      }
      else
      {
         if(flight==null && !sailing && !player.isWolfForm())
         {
            g.setFont(readableP);
            g.drawString("(INSERT/DEL)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll weapons", x, y);
         }
         y += SIZE;
         if(flight==null && !player.isWolfForm() && player.getSpells().size() > 1)
         {
            g.setFont(readableP);
            g.drawString("(HOME/END)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll spells", x, y);
         }
         y += SIZE; 
         if(player.canScrollArmor() && player.canScrollPotion() && player.numPotions() > 1 && player.numArmor() > 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-UP/PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions/armor", x, y);
         }
         else if(player.canScrollArmor() && player.numPotions() <=1 && player.numArmor() > 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-DN)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll armor", x, y);
         }
         else if(player.canScrollPotion() && player.numPotions() >1 && player.numArmor() <= 1)
         {
            g.setFont(readableP);
            g.drawString("(PG-UP)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            g.drawString("                            scroll potions", x, y);
         }
         y += SIZE;
         if(flight==null && !player.isHiding())
         {
            g.setColor(Color.white);		         //draw a backgraound that changes from white to red when guard is toggled
            if(player.isOnGuard())
               g.setColor(new Color(255,200,200));
            g.fillRect(x, y-SIZE, (SCREEN_SIZE*SIZE), (SIZE+SIZE/3));
            g.setColor(new Color(0, 0, 127));
            g.setFont(readableP);
            g.drawString("(CTRL)", x, y);
            g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
            if(player.isOnGuard())
               g.drawString("                            toggle guard UP/down", x, y);
            else
               g.drawString("                            toggle guard up/DOWN", x, y);
         }
         y += SIZE;
      }
      g.setFont(readableP);
      g.drawString("(M)ap  (I)nventory  (J)ournal(ESC)", x, y);
      y += SIZE;
      return new Coord(x, y);
   }
   
   //show player info for inventory, shops, wardrobe
   public static Coord showPlayerInfo(Graphics g)
   {
      Font readable = new Font("Serif", Font.BOLD | Font.ITALIC, SIZE);
      Font readableP = new Font("Serif", Font.PLAIN | Font.ITALIC, SIZE);
      Font handWriten = new Font("Pristina", Font.BOLD | Font.ITALIC, SIZE);
   
      String pname = player.getShortName();
      if(menuMode == SHOPPE_MAGIC)
      {
         g.setColor(magicColor);
      }
      else if(menuMode == SHOPPE_ARMORY)
      {
         g.setColor(weaponColor);
      }
      else if(menuMode == SHOPPE_TRADER)
      {
         g.setColor(traderColor);
      }
      else
      {
         g.setColor(new Color(0, 0, 127));		            //draw a blue boarder around the board
      }
      g.fillRect(0, 0, (SCREEN_SIZE*SIZE), (SCREEN_SIZE*SIZE));
      
      g.setColor(Color.orange.darker().darker());        //place a frame around our portrait
      g.fillRect((int)((SIZE*SCREEN_SIZE) - (SIZE*7.5)), (int)(SIZE*1.5), SIZE*7, SIZE*7);                                            
      drawPortrait(g);
      int x = SIZE;
      int y = SIZE;   
      g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
      g.setColor(Color.orange);
      g.drawString("Name:    ", x, y);
      g.setFont(readable);
      g.drawString(pname + " " + player.getReputationName(), x+(SIZE*4), y);
      
      y += SIZE;
      g.setColor(Color.yellow);
      g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
      g.drawString("Level:      " + player.getLevel(), x, y);
      y += SIZE;
         
      int mightDiff = (player.getMight()-player.getMightRaw());
      int mindDiff = (player.getMind()-player.getMindRaw());
      int agilDiff = (player.getAgility()-player.getAgilityRaw());
         
      if(mightDiff<0)
         g.drawString("Might:     " + player.getMightRaw()+"-"+Math.abs(mightDiff), x, y);
      else if(mightDiff>0)
         g.drawString("Might:     " + player.getMightRaw()+"+"+Math.abs(mightDiff), x, y);
      else
         g.drawString("Might:     " + player.getMight(), x, y);
      y += SIZE;
         
      if(mindDiff<0)
         g.drawString("Mind:      " + player.getMindRaw()+"-"+Math.abs(mindDiff), x, y);
      else if(mindDiff>0)
         g.drawString("Mind:      " + player.getMindRaw()+"+"+Math.abs(mindDiff), x, y);
      else
         g.drawString("Mind:      " + player.getMind(), x, y);
      y += SIZE;
         
      if(agilDiff<0)
         g.drawString("Agility:    " + player.getAgilityRaw()+"-"+Math.abs(agilDiff), x, y);
      else if(agilDiff>0)
         g.drawString("Agility:    " + player.getAgilityRaw()+"+"+Math.abs(agilDiff), x, y);
      else
         g.drawString("Agility:    " + player.getAgility(), x, y);
      y += SIZE;
         
      g.setColor(Color.red);
      g.drawString("Body:      " + player.getBody()+"/"+player.getHealthMax(), x, y);
      y += SIZE;
      g.setColor(magicColor.brighter().brighter().brighter().brighter());
      g.drawString("Manna:    " + player.getManna()+"/"+player.getMannaMax(), x, y);
      y += SIZE;
      g.drawString("Awareness: " + player.getAwarenessName(), x, y);
      y += SIZE;
      
      g.setColor(Color.orange);
      g.drawString("Gold:      " + player.getGold(), x, y);
      y += SIZE;
      y += SIZE;
      Weapon weap = player.getWeapon();
      boolean isMagic = (player.getImageIndex()==Player.STAFF && player.getWeaponSelect() > 0);
      boolean isRanged = (weap.getRange() > 2);
      boolean isMelee = (!isMagic && !isRanged);
      if(isMagic)
      {  //if this weapon requires manna to use it, color it with the magic color
         g.setColor(magicColor.brighter().brighter().brighter().brighter());
      }
      else
      {
         g.setColor(weaponColor.brighter().brighter().brighter());
      }
      int bonus = 0;
      if(isMagic)
         bonus = player.getMind();
      else if(isRanged)
         bonus = player.getAgility();
      else  //if melee
         bonus = player.getMight();
      String weapName = weap.getName();   
      String weaponAugment = ""; 
      if(weapName.contains("Throwing-stone") || weapName.contains("Sling"))
      {                       //less of a damage bonus for throwing stones
         bonus /= 2;
      }
      int minDam = (weap.getMinDamage()+bonus);
      int maxDam = (weap.getMaxDamage()+bonus);
      if(weapName.toLowerCase().contains("fist"))
      {
         if(!player.hasItem("iron-bracer") && !player.hasItem("clawed-glove"))
         {
            minDam /=2;
            maxDam /=2;
         }
         else if(player.hasItem("clawed-glove"))
         {
            minDam = (int)(minDam * 1.5);
            maxDam = (int)(maxDam * 1.5);
            weaponAugment = " (Clawed-glove)";
         }
         else if(player.hasItem("iron-bracer"))
         {
            minDam = (int)(minDam * 1.5);
            maxDam = (int)(maxDam * 1.5);
            weaponAugment = " (Iron-bracer)";
         }
         if(player.hasItem("iron-bracer") && player.hasItem("clawed-glove"))
            weaponAugment = " (Clawed-glove/bracer)";
      }
      if(weapName.toLowerCase().contains("bow") || weapName.toLowerCase().contains("caster"))
      {
         if(player.hasItem("swiftquill") && player.hasItem("bow-bracer"))
            weaponAugment = " (Swiftquill/bracer)";
         else if(player.hasItem("swiftquill"))
            weaponAugment = " (Swiftquill)";
         else if(player.hasItem("bow-bracer"))
            weaponAugment = " (Bow-bracer)";
      }
      if(weapName.toLowerCase().contains("lute"))
      {
         if(player.hasItem("songpage"))
         {
            int numPages = player.getItemFrequency("songpage");
            weaponAugment = " (Songpage:"+numPages+")";
         }
      }
      g.drawString("Weapon:    " + weapName +weaponAugment, x, y);
      y += SIZE;
      if(minDam == maxDam)
         g.drawString("Damage:    " + maxDam, x, y);
      else
         g.drawString("Damage:    " + minDam+" to "+maxDam, x, y);
      g.drawString("Range:" + weap.getRange(), x+(SIZE*10), y);
      y += SIZE;
      //if(!weap.getEffect().equals("none"))
      g.drawString("Effect:       " + weap.getEffect(), x, y);
      String weapKeys = Weapon.getKeys(player.getImageIndex(), weapName);
      if(!weapKeys.equals("unknown"))
      {
         g.drawString("Keys:" + weapKeys, x+(SIZE*10), y);
      }   
      y += SIZE;
      
      g.setColor(weaponColor.brighter().brighter().brighter());
      Armor armor = player.getArmor();
      g.drawString("Armor:     " + armor.getName() + " Protection:" + player.getArmorPoints(), x, y);
      y += SIZE;
      
      if(player.isVampire())
      {
         g.setColor(Color.red);
         String status = "full";
         if(player.getRations()==0)
            status = "drained";
         g.drawString("Lifeblood:  " + status, x, y);
      }
      else
      {
         g.setColor(Color.orange);
         g.drawString("Rations:    " + player.getRations(), x, y);
      }
      y += SIZE;
      
      g.setColor(Color.orange);
      String sentence = "Items: ";
      for(int i=0; i<player.getItems().size() ; i++)
      {
         String it = player.getItems().get(i);
         if(!it.contains("bounty") && !it.contains("message")  && !it.contains("parcel") && !it.contains("head")  && !it.contains("tooth")  && !it.contains("essence") && !it.contains("book")  && !it.contains("puzzle") && !it.contains("manual") && !it.contains("songpage") && !it.contains("floretbox") && !it.contains("treasuremap") && !it.contains("serpent-egg") && !it.contains("scale") && !it.contains("lockpick")  && !it.contains("magicpick") && !it.contains("bracer") && !it.contains("clawed-glove")  && !it.contains("swiftquill")   && !it.contains("loaded-cube") && !Item.itemIsStone(it))
         {
            if(it.contains(":"))
            {
               String [] parts = it.split(":");
               it = parts[0];
            }
            sentence += it+ " ";
         }
      }
             //**********WORD WRAP SENTENCE
      String [] words = sentence.split(" ");
      int xAdd = 0;
      for(int j=0; j<words.length; j++)
      {
         String tempWord = new String(words[j].trim());
         tempWord += " ";
         int width = g.getFontMetrics().stringWidth(tempWord);
         if(xAdd + width >= (SIZE * SCREEN_SIZE)-SIZE)
         {
            y += SIZE;
            xAdd = SIZE;
         }	
         g.drawString(tempWord, x+xAdd, y);
         xAdd += width;
      }
      y += SIZE;
      
      g.setColor(magicColor.brighter().brighter().brighter().brighter());
      if(player.numPotions() > 0)
      {
         Potion potion = player.getPotion();
         if(potion != null)
            g.drawString("Potion:     " + potion.getName()+"("+player.countPotion(potion.getName())+")", x, y);
      }
      y += SIZE;
      
      if(player.getSpells().size() > 0 && player.getSpellInfo()!=null)
      {
         Spell spell = player.getSpellInfo();
         if(player.getSpell()==null)         //write spell name in red if we can't cast it
         {
            Spell spl = player.getActiveSpell(player.getSpellInfo().getName());
            String timeLeft = "";
            if(spl != null)
               timeLeft = ":("+spl.getTimeLeft()+"/"+spl.getDuration()+")";
            g.setColor(new Color(150,0,0));
            g.drawString("Spell:       " + spell.getName()+timeLeft, x, y);
         }
         else
            g.drawString("Spell:       " + spell.getName(), x, y);
      }
      y += SIZE;
      
      if(player.hasItem("floretbox"))
      {
         g.setColor(Color.green);
         g.drawString("Florets:", x, y);
         g.setColor(Color.red);
         g.drawString(""+player.numRedFlowers(), (int)(x+SIZE*4.5), y);
         g.setColor(Color.yellow);
         g.drawString(""+player.numYellowFlowers(), (int)(x+SIZE*6), y);
         g.setColor(Color.green);
         g.drawString(""+player.numGreenFlowers(), (int)(x+SIZE*7.5), y);
         g.setColor(new Color(90,90,255));
         g.drawString(""+player.numBlueFlowers(), (int)(x+SIZE*9), y);
         g.setColor(new Color(238,130,238));
         g.drawString(""+player.numVioletFlowers(), (int)(x+SIZE*10.5), y);
      }
      y += SIZE;
      
      g.setColor(Color.red);
      if(player.getEffects().size() > 0)
      {
         String effects = player.getEffects().toString();
         effects = effects.substring(1, effects.length()-1);
         g.drawString("Effect:       " + effects, x, y);
      }
      y += SIZE;
      
      g.drawString("Experience:" + player.getExperience()+"/"+player.expNeededForNextLevel(), x, y);
      y += SIZE;
      int reputation = CultimaPanel.player.getReputationRaw();
      if(reputation >= 1000)
         g.setColor(Color.white);
      else if(reputation >= 500)
         g.setColor(Color.yellow);
      else if(reputation >= 10)   
         g.setColor(Color.green.brighter());
      else if(reputation >= -10)
         g.setColor(Color.green);
      else if(reputation >= -500)
         g.setColor(Color.orange.darker());
      else if(reputation >= -1000)
         g.setColor(Color.red.brighter());
      else
         g.setColor(Color.red); 
      g.drawString("Reputation:" + reputation+" / Bounty:"+CultimaPanel.player.getBounty(), x, y);
      x = SIZE * SCREEN_SIZE;
      return new Coord(x, y);
   }

   public static Coord showMainMenuInfo(Graphics g)
   {
      Font readable = new Font("Serif", Font.BOLD | Font.ITALIC, SIZE);
      Font readableP = new Font("Serif", Font.PLAIN | Font.ITALIC, SIZE);
      Font handWriten = new Font("Pristina", Font.BOLD | Font.ITALIC, SIZE);
      g.setColor(new Color(1, 1, 20));
      g.fillRect(0,0, SIZE*SCREEN_SIZE, SIZE*SCREEN_SIZE);
      drawRainbow(g, 90);
      int x = SIZE*3;
      int y = SIZE*4;  
      g.setFont(new Font("Old English Text MT", Font.BOLD, SIZE*4));
      g.setColor(Color.orange.darker()); 
      g.drawString("Cultima 1", x+3, y+3);
      g.setColor(Color.orange); 
      g.drawString("Cultima 1", x, y);         
      y = SIZE*6;
      g.setFont(readable);
      g.drawString("Exit screen to play", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        ESC", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Change screen size", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        -, +", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Move in travel mode", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        arrow keys", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Constant movement", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        SHIFT", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Inventory / Journal", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        I / J", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("View Map / Wait", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        M / W", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Scroll weapon select", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        INS, DEL", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Scroll spell select", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        HOME, END", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Potion / armor select", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        PG-UP, PG-DN", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Weapon switch hot keys", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        F1 to F4", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Spell cast hot keys", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        F5 to F8", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Potion hot keys", x, y);
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        0 to 9", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Assign weapon hot keys to selected weapon", x, y);
      y += SIZE;
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        SHIFT-F1 to SHIFT-F4", x, y);
      g.setColor(Color.red);
      y += SIZE;
      g.drawString("Assign spell hot keys to selected spell", x, y);
      y += SIZE;
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        SHIFT-F5 to SHIFT-F8", x, y);
      g.setColor(Color.red);              
      y += SIZE;
      g.drawString("Assign potion hot keys to selected potion", x, y);
      y += SIZE;
      g.setColor(Color.orange.darker()); 
      g.drawString("                                        SHIFT-0 to SHIFT-9", x, y);
      g.setColor(Color.red);              
      y += SIZE;
      x = SIZE * SCREEN_SIZE;
      return new Coord(x,y);
   }

   //display terrain, map items, npcs, corpses + effects like sun reflections, shadows and glowing
   public static Coord showGameScreen(Graphics g, int x, int y, double tempTime,  String locationType, Spell timestop)
   {
      int size = SIZE;
      int numRows = SCREEN_SIZE;
      int numCols = SCREEN_SIZE;
      int focusRow = player.getRow();                        //center of where our map is focused
      int focusCol = player.getCol();
      int mapIndex = player.getMapIndex();
      int awareness = player.getAwareness();
      HashMap <Coord, NPCPlayer> allScreenNPCs = new HashMap<Coord, NPCPlayer>();
      Color blockColor = timeColor(tempTime, locationType);
      boolean indoorLocation = (locationType.contains("sewer") || locationType.contains("dungeon") || locationType.contains("cave") || locationType.contains("mine") || locationType.contains("lair"));
      ArrayList<Corpse> corpsesToDraw = new ArrayList<Corpse>();
      ArrayList<Coord> corpseLocs = new ArrayList<Coord>();
      HashMap <Coord, ArrayList<NPCPlayer>> npcsToDraw = new HashMap<Coord,  ArrayList<NPCPlayer>>();
   
      ArrayList<Coord> xMarks = player.getTreasureMapLocs();
      if(menuMode == MAP)
      {
         size /= 3;
         numRows *= 3;
         numCols *= 3; 
         focusRow = mapR;
         focusCol = mapC;
         flashColor = null;
         flashFrame = 0;
         g.setColor(new Color(0, 140, 0));
         g.fillRect(0, 0 ,  (SIZE * SCREEN_SIZE), (SIZE * SCREEN_SIZE));
      }
      if(numFrames > flashFrame + Math.max(3,animDelay/2))
      {
         flashColor = null;
         flashFrame = 0;
      }
      int deathRow = -1, deathCol = -1;   //position of where Death NPC might be on screen
      NPCPlayer death = CultimaPanel.getDeathAbout;
      if(death != null && (!death.hasEffect("stun") || Math.random() < 0.25))
      {
         deathRow = death.getRow();
         deathCol = death.getCol();
      }
         //to reflect the sun in water/ice
      Coord sunReflection = findSunCol();
      int sunCol = -1, sunAlpha = 0;
      if(sunReflection != null)
      {
         sunCol = sunReflection.getRow();
         sunAlpha = sunReflection.getCol();
      }
      for(int i=0; i<numRows; i++)
      {
         int regularRow = ((focusRow - (numRows/2)) + i);
         int r = equalizeRow(regularRow); 
         x = 0;						//reset the row distance
         for(int j=0; j<numCols; j++)
         {
            int regularCol = ((focusCol - (numCols/2)) + j);
            int c = equalizeCol(regularCol); 
            
            //find any NPC on that terrain
            NPCPlayer npc = null;
            NPCPlayer closeNPC = null;
            for(NPCPlayer p: CultimaPanel.AllNPCinRange)
            {  
               if(p.getRow()==r && p.getCol()==c)
               {
                  if(p.getLocationType().contains("pacrealm"))
                  {
                     if(p.isPellet() && npc!=null)
                     {}
                     else
                     {
                        npc = p;
                        allScreenNPCs.put(new Coord(x, y), p);
                     }    
                  }
                  else if(p.getCharIndex()==NPC.FIRE && npc!=null)
                  {}
                  else
                  {
                     npc = p;
                     allScreenNPCs.put(new Coord(x, y), p);   
                  }
               }
               if(wrapDistance(p.getRow(), p.getCol(), r, c) < ((5 - awareness)*2) + 2  && !p.isStatue()  && !p.isNonPlayer() && !NPC.isFurniture(p) && p.getCharIndex()!=NPC.WILLOWISP && !(p.getCharIndex()==NPC.MIMIC && !p.hasBeenAttacked())) 
                  closeNPC = p;
            } 
            g.setColor(blockColor);
            Terrain currentPos = allTerrain.get(Math.abs((map.get(mapIndex))[focusRow][focusCol]));
            Coord temp = new Coord(r, c);
            boolean wrappedCoord = false;    //is this a wrap-around coordinate?  If so, block when in a location
            if(player.getMapIndex() > 0)
            {
               if(regularRow!=r || regularCol!=c)
               {
                  wrappedCoord = true;
                  //mark the Location entrance/exit side with a brighter color    
                  if(r==0 || c==0 || r==(map.get(mapIndex)).length-1 || c==(map.get(mapIndex))[0].length-1)
                     g.setColor(blockColor.brighter().brighter());
               }
            }
            double distToDeath = -1;   //the Death character is shrouded in a radius of darkness
            if(deathRow != -1 && deathCol != -1)
               distToDeath =  findDistance(deathRow, deathCol, r, c);
            if(flashColor == null && distToDeath != -1 && distToDeath <= SCREEN_SIZE/3+1 && CultimaPanel.player.getActiveSpell("Light")==null)
            {
               Color deathColor = new Color(blockColor.getRed(), blockColor.getGreen(), blockColor.getBlue());
               for(int distColor = 1; distColor <= ((SCREEN_SIZE/3+1)-distToDeath); distColor++)
                  deathColor = deathColor.darker();
               g.setColor(deathColor);
               g.fillRect(x, y, size, size);
            }
            else if(wrappedCoord)
               g.fillRect(x, y, size, size);
            else if((blocked.contains(temp) || darkened.contains(temp)) && menuMode != MAP)
            {  //color darkened cells differently if we sense something is there (depending on awareness)
                  //disable if near magic doors
               boolean nearMagicDoor = false;
               if((player.getLocationType().contains("dungeon") || player.getLocationType().contains("cave")) && door1!=null && door2!=null && door3!=null)
               {
                  double d1dist = Display.distanceSimple(CultimaPanel.door1.getRow(),CultimaPanel.door1.getCol(),player.getRow(),player.getCol());
                  double d2dist = Display.distanceSimple(CultimaPanel.door2.getRow(),CultimaPanel.door2.getCol(),player.getRow(),player.getCol());
                  double d3dist = Display.distanceSimple(CultimaPanel.door3.getRow(),CultimaPanel.door3.getCol(),player.getRow(),player.getCol());
                  if(d1dist < SCREEN_SIZE/2 || d2dist < SCREEN_SIZE/2 || d3dist < SCREEN_SIZE/2)
                     nearMagicDoor = true;
               }
               if(!nearMagicDoor)
               {
                  int multiplier = 1;
                  if(player.isWolfForm())
                     multiplier = -5;
                  if(closeNPC!=null && !NPC.isVampire(closeNPC) && !NPC.isEtheral(closeNPC.getCharIndex()) && !NPC.isNatural(closeNPC.getCharIndex()) && !NPC.isShip(closeNPC.getCharIndex()) && closeNPC.getCharIndex()!=NPC.SLIME)
                  {
                     if((closeNPC.getCharIndex()==NPC.GOBLINBARREL || closeNPC.getCharIndex()==NPC.MIMIC) && !closeNPC.hasBeenAttacked())
                     {}                      //goblins hidden in barrels should be hard to detect
                     else
                     {
                        if(NPC.isFurniture(closeNPC) || closeNPC.isStatue() || closeNPC.isNonPlayer())
                           multiplier = 1;   //don't detect barrels, statues, ghosts or vampires
                        int red = blockColor.getRed() - (multiplier*(awareness*2));
                        if(red < 0)
                           red = 0;
                        int green = blockColor.getGreen() - ((awareness*2));
                        if(green < 0)
                           green = 0;
                        int blue = blockColor.getBlue() - ((awareness*2));
                        if(blue < 0)
                           blue = 0;
                        g.setColor(new Color(red, green, blue));
                     }
                  }
               }
               g.fillRect(x, y, size, size);
            }
            else
               if((map.get(mapIndex))[r][c]!=0)
               {
                  Terrain ter = allTerrain.get(Math.abs((map.get(mapIndex))[r][c]));
                  if(menuMode == MAP)
                  {  //show map elements visited/seen or show all if they have eagle-eye
                     if((map.get(mapIndex))[r][c] < 0 || awareness==4)  //negative values means that we have visited there
                     {
                        g.setColor(Color.black);
                        g.fillRect(x, y, size, size);
                        if((player.getLocationType().contains("dungeon") || player.getLocationType().contains("cave")) && door1!=null && door2!=null && door3!=null)
                        {//don't show elements close to magic doors (3-doors puzzle)
                           double d1dist = Display.distanceSimple(CultimaPanel.door1.getRow(),CultimaPanel.door1.getCol(),r,c);
                           double d2dist = Display.distanceSimple(CultimaPanel.door2.getRow(),CultimaPanel.door2.getCol(),r,c);
                           double d3dist = Display.distanceSimple(CultimaPanel.door3.getRow(),CultimaPanel.door3.getCol(),r,c);
                           if(d1dist > 1 && d2dist > 1 && d3dist > 1)
                              g.drawImage(ter.getPicture().getImage(), x, y, size, size, null);   
                        }
                        else
                           g.drawImage(ter.getPicture().getImage(), x, y, size, size, null);  
                        if(npc!=null && (npc.isStatue() || NPC.isFurniture(npc)))
                        {
                           g.drawImage(npc.getCurrentPicture().getImage(), x, y, size, size, null);
                        }
                     }
                     else
                        g.fillRect(x, y, size, size);
                  }
                  else  //in Travel mode
                  {
                     if(flashColor != null && numFrames <= flashFrame+Math.max(3,animDelay/2) && wrapDistance(r, c, player.getRow(), player.getCol()) <= flashRadius)
                     {
                        g.setColor(flashColor);
                        g.fillRect(x, y, size, size);
                     }
                     else
                     {
                        if(indoorLocation || ((player.getLocationType().contains("castle") || player.getLocationType().contains("tower")) && (ter.getName().toLowerCase().contains("torch") || ter.getName().toLowerCase().contains("fire"))))
                           g.setColor(new Color(20,20,20));
                        else
                           g.setColor(groundColor);
                        g.fillRect(x, y, size, size);
                           
                        if(timestop!=null)
                           g.drawImage(ter.getCurrentPicture().getImage(), x, y, size, size, null);  
                        else 
                        {
                           if(ter.getName().toLowerCase().contains("water") || ter.getName().toLowerCase().contains("lava"))
                           {
                              if(numFrames % animDelay == 0)
                                 g.drawImage(ter.getPictureRandom().getImage(), x, y, size, size, null);  
                              else
                                 g.drawImage(ter.getCurrentPicture().getImage(), x, y, size, size, null);  
                           }
                           else if(numFrames % animDelay == 0)
                              g.drawImage(ter.getPictureAndAdvance().getImage(), x, y, size, size, null);  
                           else
                              g.drawImage(ter.getCurrentPicture().getImage(), x, y, size, size, null);  
                          //if this is lava in Jurassica, maybe add some smoke
                           if(locationType.contains("jurassica") && ter.getName().toLowerCase().contains("lava"))
                           {
                              double smokeProb = 3;
                              if(Math.random() < (0.01 * smokeProb) && smoke.size() < 50)
                              {
                                 int radius = Utilities.rollDie(5);
                                 int opacity = Utilities.rollDie(191,255);
                                 smoke.add(new SmokePuff(x, y, radius, opacity));
                              }
                           }
                           else if(ter.getName().toLowerCase().contains("fire"))
                           {
                              double smokeProb = 2;
                              if(Math.random() < (0.01 * smokeProb) && smoke.size() < 50)
                              {
                                 int radius = Utilities.rollDie(4);
                                 int opacity = Utilities.rollDie(191,255);
                                 smoke.add(new SmokePuff(x, y, radius, opacity));
                              }
                           }
                          //end smoke for lava in Jurassica    
                        }
                        if(isWinter() && ter.getName().toLowerCase().contains("water") && !locationType.contains("underworld"))
                        {  //put a film of ice over the water - more transparent for early and late winter (thinner ice)
                           double alpha = distFromDeadWinter();
                           if(ter.getName().toLowerCase().contains("rapid"))
                              alpha /= 2;
                           else  if(ter.getName().toLowerCase().contains("deep"))
                              alpha /= 1.5;
                           Color ice = new Color(240,240,255, (int)Math.min(255,(255*alpha)));
                           g.setColor(ice);
                           g.fillRect(x, y, size, size);
                        } 
                        if(selectedRenovate!=null && r==selectedRenovate.getRow() && c==selectedRenovate.getCol() && mapIndex==selectedRenovate.getMapIndex())
                        {
                           g.setColor(Color.white);
                           g.drawRect(x, y, size, size);
                           g.setColor(Color.cyan);
                           g.drawRect(x+1, y+1, size-2, size-2);
                           g.setColor(Color.blue.brighter());
                           g.drawRect(x+2, y+2, size-4, size-4);
                        }
                     }   
                     if(!player.getLocationType().contains("graboid") && !player.getLocationType().contains("beast"))
                     {
                        if(ter.getName().contains("_T_") || (ter.getName().contains("_D_") && (ter.getName().toLowerCase().contains("wall") || ter.getName().toLowerCase().contains("rock"))))   
                        {//reveal traps and secret doors if we have high enough awareness
                           int multiplier = (5-awareness)*2;
                           if(awareness == 0)
                              multiplier *= 2;
                           int distToTrap = (int)(findDistance( r, c,player.getRow(), player.getCol()));
                           if(awareness>=0)
                           {
                              if(numFrames % Math.max(animDelay,((animDelay*4*multiplier)-((SCREEN_SIZE-distToTrap)*(Math.max(1,awareness))))) == 0)
                              {
                                 if(ter.getName().toLowerCase().contains("chest") || ter.getName().toLowerCase().contains("book"))
                                 {
                                    if(Math.random() < 0.5)
                                       g.setColor(Color.blue);
                                    else
                                       g.setColor(Color.cyan);
                                 }
                                 else
                                 {
                                    if(Math.random() < 0.5)
                                       g.setColor(Color.red);
                                    else
                                       g.setColor(Color.orange);
                                 }
                                 g.fillOval(x+size/4, y+size/4, size/2, size/2);
                              }
                           }   
                        }
                        else if(ter.getName().contains("strange"))   
                        {//make flash of blue pulse faster as you get closer to a strange entrance
                           int distToPortal = (int)(findDistance( r, c,player.getRow(), player.getCol()));
                           if(distToPortal > 0 && numFrames % (distToPortal*2) == 0)
                           {
                              if(Math.random() < 0.5)
                                 g.setColor(Color.blue);
                              else
                              {
                                 g.setColor(Color.cyan);
                                 Sound.portalPulse(distToPortal);
                              }
                              g.fillOval(x+size/4, y+size/4, size/2, size/2);
                           }  
                        }
                     }
                     //draw any eggs
                     for(RestoreItem egg: CultimaPanel.eggsToHarvest)
                     {
                        if(egg.getMapIndex()==mapIndex && egg.getRow()==r && egg.getCol()==c && egg.getValue()>=0)
                        {  //negative egg values means that it is in the queue to be restored
                           byte eggType = (byte)(Math.abs(egg.getValue()));
                           if(eggType == NPC.DRAGON)
                           {
                              g.setColor(Color.white);
                              g.fillOval(x+size/4, y + size - (size/2), size/2, size/3);
                              g.setColor(Color.black);
                              g.drawOval(x+size/4, y + size - (size/2), size/2, size/3);
                           }
                           else
                           {
                              g.setColor(Color.white);
                              g.fillOval(x+size/4, y + size - (size/4), size/3, size/5);
                              g.setColor(Color.black);
                              g.drawOval(x+size/4, y + size - (size/4), size/3, size/5);
                              int rightShift = size/8;
                              int downShift = size/16;
                              g.setColor(Color.white);
                              g.fillOval(x+size/4+rightShift, y + size - (size/4) + downShift, size/3, size/5);
                              g.setColor(Color.black);
                              g.drawOval(x+size/4+rightShift, y + size - (size/4) + downShift, size/3, size/5);
                           }
                        }
                     }  
                     //draw any corpses
                     for(Corpse cp: corpses.get(mapIndex))
                     {
                        if(cp.getRow()==r && cp.getCol()==c)
                        {
                           Coord currCoord = new Coord(x,y);
                           corpsesToDraw.add(cp); 
                           corpseLocs.add(currCoord); 
                        }
                     }
                     //show towers for the 3-towers puzzle   
                     if(player.getLocationType().contains("cave") && door1!=null && door2!=null && door3!=null && tower1!=null && tower2!=null && tower3!=null)
                     {
                        int [] tower = null;
                        if(r==door1.getRow() && c==door1.getCol()) 
                        {
                           tower = tower1.clone();
                        }
                        else if(r==door2.getRow() && c==door2.getCol()) 
                        {
                           tower = tower2.clone();
                        }
                        else if(r==door3.getRow() && c==door3.getCol()) 
                        {
                           tower = tower3.clone();
                        }
                        if(tower != null)
                        {
                            //draw the peg
                           int mid = size/2;
                           int width = 5;
                           int startX = x + mid - (width/2);
                           int startY = y + (width);
                           g.setColor(Color.gray);
                           g.fillRect(startX, startY, width, size-width);
                           g.setColor(Color.black);
                           g.drawRect(startX, startY, width, size-width);
                              //draw the base
                           startX = x+(width/2);
                           startY = (y + size) - width;
                           g.setColor(Color.gray);
                           g.fillRect(startX, startY, size-width, width);
                           g.setColor(Color.black);
                           g.drawRect(startX, startY, size-width, width);
                                                              
                              //draw the disks on the peg (tower array)
                           int length = (int)(size * 0.66);    //the length of a disk of size 4
                           int part = length/4;                //the length of a single unit disk size
                           int height = (int)(size * 0.125);
                           int buffer = 2;      //number of pixels between disks
                           startX = (x + size/2) - (length/2);
                           startY = (y + size) - (height + buffer) - width;   
                           for(int disk = tower.length-1; disk >= 0;disk--)
                           {
                              if(tower[disk] != 0)
                              {
                                 int diskSize = part*tower[disk];
                                 startX = (x + size/2) - (diskSize/2);
                                 g.setColor(Color.orange);
                                 g.fillRect(startX, startY, diskSize, height);
                                 g.setColor(Color.red);
                                 g.drawRect(startX, startY, diskSize, height);
                                 startY -= (height + buffer); 
                              }
                           }
                              
                        }
                     }
                     //populate npcsToDraw so we can show any NPC on that terrain
                     if(npc != null && !npc.isNonPlayer())
                     {
                        Coord xyLoc = new Coord(x,y);
                        if(npcsToDraw.containsKey(xyLoc))
                        {
                           if(npc.canFly() && !npc.isPellet())              //add flyers to the end so they are drawn last
                           {
                              npcsToDraw.get(xyLoc).add(npc);
                           }
                           else
                           {
                              ArrayList<NPCPlayer> npcsHere = npcsToDraw.get(xyLoc);
                              boolean fireHere = false;
                              for(NPCPlayer currNpc: npcsHere)
                              {  //see if there is a fire in the list at this location
                                 if(currNpc.getCharIndex()==NPC.FIRE)
                                 {
                                    fireHere = true;
                                 }
                              }
                              if(fireHere && npc.getCharIndex()!=NPC.FIRE)
                              {  //if there is a fire there, replace it with the non-fire npc
                                 ArrayList<NPCPlayer> npcsThere = new ArrayList<NPCPlayer>();
                                 npcsThere.add(npc);                 
                                 npcsToDraw.put(xyLoc, npcsThere);
                              }
                              else
                                 npcsToDraw.get(xyLoc).add(0,npc); 
                           }
                        }
                        else
                        {
                           ArrayList<NPCPlayer> npcsThere = new ArrayList<NPCPlayer>();
                           npcsThere.add(npc);                 
                           npcsToDraw.put(xyLoc, npcsThere); 
                        }
                        if(allScreenNPCs.containsKey(xyLoc)) 
                           allScreenNPCs.remove(xyLoc);
                     }
                  }
               }
               else
                  g.drawImage(none.getImage(), x, y, size, size, null);  
            
            x+=size;
         }
         y+=size;
      }
         //draw any circles/squares/Xs around locations in maps, glow around torches
      if(menuMode == TRAVEL || menuMode == MAP)
      {
         y=0;
         for(int i=0; i<numRows; i++)
         {
            int regularRow = ((focusRow - (numRows/2)) + i);
            int r = equalizeRow(regularRow); 
            x = 0;						//reset the row distance
            for(int j=0; j<numCols; j++)
            {
               int regularCol = ((focusCol - (numCols/2)) + j);
               int c = equalizeCol(regularCol); 
               Terrain ter = allTerrain.get(Math.abs((map.get(mapIndex))[r][c]));
               Coord temp = new Coord(r,c);
               if(menuMode == TRAVEL)
               {
                  //draw yellow/orange transparent circles over torches and lava - random size unless in Wolfenstein secret realm
                  if(!darkened.contains(temp) && !blocked.contains(temp) && (ter.getName().toLowerCase().contains("torch") || ter.getName().toLowerCase().contains("fire") || ter.getName().toLowerCase().contains("lava")))
                  {  //scale the opcacity of the torch flash to the time of day so that it has a greater effect at night/indoors
                     int distFromNoon = (int)(Math.abs(time - 12));
                     if(TerrainBuilder.indoors())
                        distFromNoon = 12;
                     int baseOpacity = distFromNoon * 4;
                     if(ter.getName().toLowerCase().contains("lava"))
                        baseOpacity = (int)(baseOpacity* 1.5);
                     int opacity = baseOpacity;
                     Color glow = Color.yellow;
                     int radius = (int)(SIZE * 2);
                     if(Math.random() < 0.5 && !player.getLocationType().toLowerCase().contains("wolfenstein"))
                     {
                        if(Math.random() < 0.5)
                           glow = Color.orange;
                        else
                           glow = Color.orange.brighter();
                        radius = Utilities.rollDie(1, SIZE*3);
                        opacity = Math.min(255, Math.max(0,Utilities.rollDie(baseOpacity-25, baseOpacity+25)));
                     }
                     g.setColor(new Color(glow.getRed(),glow.getGreen(),glow.getBlue(),opacity));
                     if(ter.getName().toLowerCase().contains("torch") || ter.getName().toLowerCase().contains("fire"))
                        g.fillOval(x-(radius/4), y-(radius/4), radius, radius);
                     else
                        g.fillRect(x, y, size, size);
                  }
                     //draw yellow transparent square over water/ice for sun reflection
                  if(!darkened.contains(temp) && !blocked.contains(temp) && ter.getName().toLowerCase().contains("water") && !TerrainBuilder.indoors() && sunReflection != null && CultimaPanel.weather == 0)
                  {  
                     int opacity = Math.max(0, sunAlpha - ((Math.abs(sunCol - j)))*8);     
                     Color glow = Color.yellow;
                     g.setColor(new Color(glow.getRed(),glow.getGreen(),glow.getBlue(),opacity));
                     g.fillRect(x, y, size, size);
                  }
               }
               else if(menuMode == MAP && player.getMapIndex()==0)
               {
                  Location loc = TerrainBuilder.getLocationAt(allLocations, r, c, mapIndex);
                  if(xMarks.size() > 0)
                  {  //put an X on the spot where a treasure might be
                     for(Coord ex:xMarks)
                     {
                        int exR = ex.getRow();
                        int exC = ex.getCol();
                        if(r==exR && c==exC)
                        {
                           g.setColor(Color.red);
                           for(int thickness = 0; thickness <=3; thickness++)
                           {
                              g.drawLine(x+thickness, y, x+size+thickness, y+size);
                              g.drawLine(x+thickness, y+size, x+size+thickness, y);
                           }
                        }
                     }
                  }
                  if((map.get(mapIndex))[r][c] < 0 || awareness==4)  //negative values means that we have visited there
                  {
                     if(loc!=null)
                     {
                        g.setColor(Color.red);
                        g.drawOval(x-(size/2), y-(size/2), size*2, size*2);
                        g.setColor(Color.orange);
                        g.drawOval(x-(size/2)+1, y-(size/2)-1, size*2-1, size*2-1);
                        g.setColor(blockColor);
                     }
                     if(player.getMapIndex()==0 && player.getMapMarkRow()==r && player.getMapMarkCol()==c)
                     {
                        g.setColor(Color.red);
                        g.drawRect(x-(size/2), y-(size/2), size*2, size*2);
                        g.setColor(Color.orange);
                        g.drawRect(x-(size/2)+1, y-(size/2)-1, size*2-1, size*2-1);
                     }
                  }
               }
               x+=size;
            }
            y+=size;
         }
      }
           
      //draw corpses and npcs
      if(menuMode == TRAVEL)
      {
         for(int i=0; i<corpsesToDraw.size(); i++)
         {
            Coord cpCoord = corpseLocs.get(i);
            Corpse cp = corpsesToDraw.get(i);
            int cpX = cpCoord.getRow();
            int cpY = cpCoord.getCol();
            if(cp.getHitTime() >= CultimaPanel.numFrames)
            {
               Color weapColor = colorForWeapon(player.getWeapon());
               g.setColor(weapColor);
               for(int thickness=-2; thickness<=2; thickness++)
               {
                  int x1=cpX+(SIZE/2);
                  int y1=cpY+(SIZE/2);
                  int x2=(SCREEN_SIZE/2)*SIZE+(SIZE/2)+thickness;
                  int y2=(SCREEN_SIZE/2)*SIZE+(SIZE/2)+thickness;
                     
                  g.drawLine(x1, y1, x2, y2);                                 
               }
            }
            Terrain ter = allTerrain.get(Math.abs((map.get(mapIndex))[cp.getRow()][cp.getCol()]));
            if(!ter.getName().equals("TER_S_B_$Dense_forest"))
            {
               int dist = 0;
               double sizeMod = Math.min(2,cp.getSize()) * 0.8;
               double sizeModWater = Math.max(1, sizeMod);
               if(mapIndex == 0)
                  dist = (int)(wrapDistance(cp.getRow(), cp.getCol(), player.getRow(), player.getCol()));
               int xDiff = (int)(SIZE - ((SIZE-dist)*Math.min(2,sizeMod)));  
               g.drawImage(cp.getPicture().getImage(), cpX+xDiff, cpY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);  
               if(ter.getName().contains("water") && cp.getSize() < 2)
               {
                  double alpha = distFromDeadWinter();
                  if(ter.getName().toLowerCase().contains("rapid") || alpha < 0.75)
                  {        
                     g.drawImage(water.getImage(), cpX/*+xDiff*/, cpY/*+xDiff*/, (int)((SIZE/*-dist*/)*sizeModWater), (int)((SIZE/*-dist*/)*sizeModWater), null);
                     if(isWinter() && !locationType.contains("underworld"))
                     {
                        if(ter.getName().toLowerCase().contains("rapid"))
                           alpha /= 2;
                        else  if(ter.getName().toLowerCase().contains("deep"))
                           alpha /= 1.5;
                        Color ice = new Color(240,240,255, (int)Math.min(255,(255*alpha)));
                        g.setColor(ice);
                        g.fillRect(cpX/*+xDiff*/, cpY/*+xDiff*/+(SIZE/2), (int)((SIZE/*-dist*/)*sizeModWater), (int)((SIZE/*-dist*/)*sizeModWater)/2);
                     }
                  }
               }
               else if(ter.getName().toLowerCase().contains("tall_grass")  && cp.getSize() < 2)
               {
                  g.drawImage(grass.getImage(), cpX, cpY, SIZE, SIZE, null);
               }
               else if(ter.getName().toLowerCase().contains("poison")  && cp.getSize() < 2)
               {
                  g.drawImage(poison.getImage(), cpX+xDiff, cpY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
               }
               else if(ter.getName().toLowerCase().contains("bog") && !isWinter() && cp.getSize() < 2)
               {
                  g.drawImage(swamp.getImage(), cpX+xDiff, cpY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
               }
               else if(ter.getName().toLowerCase().contains("forest") && !ter.getName().toLowerCase().contains("dead") && cp.getSize() < 2)
               {
                  g.drawImage(forest.getImage(), cpX, cpY, SIZE, SIZE, null);
               }
            
            }
            if(cp.getHitTime() >= CultimaPanel.numFrames)
               g.drawImage(hit.getImage(), cpX, cpY, SIZE, SIZE, null);           
         }
         for(Coord npcCoord: npcsToDraw.keySet())
         {
            ArrayList<NPCPlayer> npcsThere = npcsToDraw.get(npcCoord);
            int npcX = npcCoord.getRow();
            int npcY = npcCoord.getCol();
            for(NPCPlayer npc: npcsThere)
            {
               /*if(npc.getLocationType().contains("pacrealm") && npc.isPellet() && npcsThere.size() > 1)
               {  //don't show pellets if there is another NPC to show
                  continue;
               }*/
               npc.setXY(npcX, npcY);
               int missionPriority = npc.getMissionPriority();
               drawShadow(g,npcX, npcY, (int)(SIZE*NPC.getSize(npc)), npc.getCharIndex());
               if(npc.getHitTime() >= CultimaPanel.numFrames)
               {
                  Color weapColor = colorForWeapon(player.getWeapon());
                  g.setColor(weapColor);
                  for(int thickness=-2; thickness<=2; thickness++)
                  {
                     int x1=npcX+(SIZE/2);
                     int y1=npcY+(SIZE/2);
                     int x2=(SCREEN_SIZE/2)*SIZE+(SIZE/2)+thickness;
                     int y2=(SCREEN_SIZE/2)*SIZE+(SIZE/2)+thickness;                               
                     g.drawLine(x1, y1, x2, y2);                                 
                  }
                  //add smoke-puff if we just hit with lightning or firebolt
                  if((attackSel && !attackSelAlt) && (player.getWeapon().getEffect().toLowerCase().contains("fire") || player.getWeapon().getName().toLowerCase().contains("lightning")))
                  {
                     if(Math.random() < 0.1 && smoke.size() < 50)
                     {
                        int radius = Utilities.rollDie(5);
                        int opacity = Utilities.rollDie(191,255);
                        smoke.add(new SmokePuff(npcX, npcY, radius, opacity));
                     }
                  }
               //make a flash at the point of impact
                  String weapName = player.getWeapon().getName().toLowerCase();
                  int playerManna = player.getManna();
                  int weaponManna = player.getWeapon().getMannaCost();
                  int mannaMax = player.getMannaMax();
                  boolean fullManna = (player.getManna()+player.getWeapon().getMannaCost() == player.getMannaMax());
                  if((weapName.contains("lightning") || weapName.contains("fireball") || weapName.contains("icestrike")) && fullManna && CultimaPanel.flashColor!=null)
                  {
                     double mindPercent = (double)(player.getMind()) / Player.MAX_STAT;
                     int aoeRange = 3 + (Utilities.rollDie(0, (int)(3*mindPercent)));
                     int radius = Math.min((aoeRange*CultimaPanel.SIZE),CultimaPanel.flashRadius*CultimaPanel.SIZE);
                     int opacity = Math.max(75, Math.min(127, player.getWeapon().getMannaCost()));
                     CultimaPanel.smoke.add(new SmokePuff(npcX-(radius/2), npcY-(radius/2), radius, opacity, CultimaPanel.flashColor));
                  }
               }
               if(npc.getMissTime() >= CultimaPanel.numFrames)
               {
                  Color missColor = Color.blue.brighter().brighter();
                  g.setColor(new Color(missColor.getRed(), missColor.getGreen(), missColor.getBlue(), 90));
                  for(int thickness=-2; thickness<=2; thickness++)
                  {
                     int x1=npcX+(SIZE/2);
                     int y1=npcY+(SIZE/2);
                     int x2=(SCREEN_SIZE/2)*SIZE+(SIZE/2)+thickness;
                     int y2=(SCREEN_SIZE/2)*SIZE+(SIZE/2)+thickness;                            
                     g.drawLine(x1, y1, x2, y2);                                 
                  }                              
               }
               if(npc.getAttackHitTime() >= CultimaPanel.numFrames || npc.getAttackMissTime() >= CultimaPanel.numFrames)
               {
                  javax.swing.ImageIcon hitType = hit;
                  if(npc.getAttackHitTime() >= CultimaPanel.numFrames)
                  {
                     Color weapColor = colorForWeapon(npc.getWeapon());
                     g.setColor(weapColor);
                  }
                  else
                  {
                     Color missColor = Color.cyan;
                     g.setColor(new Color(missColor.getRed(), missColor.getGreen(), missColor.getBlue(), 90));
                     g.setColor(missColor);
                     hitType = miss;
                  }
                  Coord targetLoc = npc.getTargetLoc();
                  if(targetLoc.getRow()!=0 && targetLoc.getCol()!=0)
                  {
                     int targRow = targetLoc.getRow();
                     int targCol = targetLoc.getCol();
                     int targX = -1;
                     int targY = -1;
                     if(targRow != player.getRow() || targCol != player.getCol())
                     {
                        for(Coord pt: npcsToDraw.keySet())
                        {
                           ArrayList<NPCPlayer> targList = npcsToDraw.get(pt);
                           for(NPCPlayer targ: targList)
                           {
                              if(targ.getRow() == targRow && targ.getCol() == targCol)
                              {
                                 targX = pt.getRow();
                                 targY = pt.getCol();
                                 break;
                              }
                           }
                        }
                     }
                     else
                     {
                        targX = (SCREEN_SIZE/2)*SIZE;
                        targY = (SCREEN_SIZE/2)*SIZE;
                     }
                     if(targX!=-1 && targY!=-1)
                     {
                        for(int thickness=-2; thickness<=2; thickness++)
                        {
                           int x2=npcX+(SIZE/2)+thickness;
                           int y2=npcY+(SIZE/2)+thickness;
                           g.drawLine(targX+(SIZE/2), targY+(SIZE/2), x2, y2);                                 
                        }
                        g.drawImage(hitType.getImage(), targX, targY, SIZE, SIZE, null);
                     }
                  }
               } 
               Terrain ter = allTerrain.get(Math.abs((map.get(mapIndex))[npc.getRow()][npc.getCol()]));
               if(!ter.getName().equals("TER_S_B_$Dense_forest") || npc.canFly() || NPC.getSize(npc) > 2)
               {
                  int dist = 0;
                  double sizeMod = NPC.getSize(npc);
                  if(mapIndex == 0)
                     dist = (int)(wrapDistance(npc.getRow(), npc.getCol(), player.getRow(), player.getCol()));
                  int xDiff = (int)(SIZE - ((SIZE-dist)*Math.min(2,sizeMod)));  
                  int xAdjust = 0;
                  
                  if(sizeMod == 1.5)
                     xAdjust = (int)(SIZE/2.5);
                  else if(sizeMod == 2)
                     xAdjust = (int)(SIZE/3.5);
                  else if(sizeMod == 2.5)
                     xAdjust = (SIZE/4);
                  else if(sizeMod > 2.5)
                     xAdjust = (int)(SIZE/4.5);
                  boolean shouldNotAnimate = (npc.getMoveType()==NPC.STILL && (time < 6 || time > 20));
                  if(npc.isTrapped())
                  {
                     if(npc.getBody()<=1)
                        shouldNotAnimate = true;
                     else
                        shouldNotAnimate = false;
                  }
                  String npcName = npc.getName().toLowerCase();
                  if(missionPriority > 0)
                  {  //mark npcs that have missions (blue) and npcs that hold objectives (red)
                     xDiff = (SIZE - (int)((SIZE-dist)*sizeMod));    
                     Color dotColor = Color.red;
                     if(missionPriority == 3)
                        dotColor = Color.red;
                     else if(missionPriority == 2)
                        dotColor = (Color.blue.brighter().brighter());
                     else  //if(missionPriority == 1)
                        dotColor = (Color.green.darker().darker());
                     g.setColor(new Color(dotColor.getRed(),dotColor.getGreen(),dotColor.getBlue(),75));
                     g.fillOval((int)(npcX), (int)(npcY), SIZE, SIZE);
                     g.setColor(new Color(dotColor.getRed(),dotColor.getGreen(),dotColor.getBlue(),100));
                     if(sizeMod < 1.5)
                        g.fillOval((int)(npcX+SIZE/2.5), (int)(npcY+SIZE/2.5), (int)(SIZE/2.5), (int)(SIZE/2.5));
                     else
                        g.fillOval(npcX+xDiff+(xAdjust), npcY+xDiff, (int)((SIZE-dist)*sizeMod*1.25), (int)((SIZE-dist)*sizeMod*1.25));                    
                  }
                  if(npc.getLocationType().contains("pacrealm") && npc.getCharIndex()==NPC.GHOST)
                  {
                     Color dotColor = (Color.black);
                     int alpha = 63;
                     if(System.currentTimeMillis() <= CultimaPanel.alertTime + 6000)
                     {  
                        dotColor = (Color.blue.brighter().brighter());
                        alpha = 127;
                     }
                     else if(npc.getName().contains("Blinky"))
                     {
                        dotColor = (Color.red);
                     }
                     else if(npc.getName().contains("Pinky"))
                     {
                        dotColor = (Color.pink);
                     }
                     else if(npc.getName().contains("Inky"))
                     {
                        dotColor = (Color.cyan);
                     }
                     else if(npc.getName().contains("Clyde"))
                     {
                        dotColor = (Color.orange);
                     }
                     g.setColor(new Color(dotColor.getRed(),dotColor.getGreen(),dotColor.getBlue(),alpha));
                     g.fillOval(npcX, npcY, SIZE, SIZE);
                     g.setColor(new Color(dotColor.getRed(),dotColor.getGreen(),dotColor.getBlue(),alpha*2));
                     g.fillOval(npcX+(SIZE/4), npcY+(SIZE/4), SIZE/2, SIZE/2);
                  }
               //people we are talking to, guards on watch (not marching or roaming) and shopkeepers don't animate
                  if(npc.getCharIndex()!=NPC.FIRE && (npc.isTalking() || npc.isShopkeep() || shouldNotAnimate) || npc.isStatue()  || NPC.isFurniture(npc) || (NPC.isGuard(npc.getCharIndex()) && npc.getMoveType()==NPC.STILL)) 
                  {
                     xDiff = (SIZE - (int)((SIZE-dist)*(sizeMod)));    
                     if(ter.getName().toLowerCase().contains("bed") && (npc.getCharIndex()==NPC.GUARD_SPEAR || npc.getCharIndex()==NPC.GUARD_SWORD))
                     {  //if a weapon-wielding guard is in bed, draw the image without a weapon
                        NPCPlayer tempModel = new NPCPlayer(NPC.GUARD_FIST, 0, 0, 0, "");
                        g.drawImage(tempModel.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);                    
                     }
                     else if(ter.getName().toLowerCase().contains("bed") && npc.getCharIndex()==NPC.LUTE)
                     {  //if a lute player is in bed, draw them without their lute
                        NPCPlayer tempModel = new NPCPlayer(NPC.MAN, 0, 0, 0, "");
                        g.drawImage(tempModel.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);                    
                     }                   
                     else if(npc.isStatue() && npc.getCharIndex()!=NPC.MONOLITH && npc.getCharIndex()!=NPC.STONE && !NPC.isFurniture(npc))     //a statue, so put a stone monolith behind it
                     {
                        NPCPlayer tempModel = new NPCPlayer(NPC.MONOLITH, 0, 0, 0, "");
                        g.drawImage(tempModel.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);                    
                        g.drawImage(npc.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);                    
                     }
                     else
                        g.drawImage(npc.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);                    
                  }
                  else
                  {
                     if(npc.getCharIndex()==NPC.PHANTOM && (npc.getAttackHitTime() < CultimaPanel.numFrames && npc.getAttackMissTime() < CultimaPanel.numFrames))    
                     {} //phantoms are invisible unless attacking
                     else if(npc.getCharIndex()==NPC.GOBLINBARREL && !npc.hasBeenAttacked())
                     {  //only show goblins in a barrel that have not been attacked yet as just barrels (no arms sticking out)
                        NPCPlayer tempModel = new NPCPlayer(NPC.BARREL, 0, 0, 0, "");
                        g.drawImage(tempModel.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);                    
                     }
                     else if((npc.getCharIndex()==NPC.MIMIC || NPC.isHorse(npc)) && !npc.hasBeenAttacked() && npc.getMoveType()==NPC.STILL)
                     {  
                        g.drawImage(npc.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);                    
                     }
                     else
                     {  
                        if(npc.getCharIndex()!=NPC.FIRE && npc.getCurrentPicture()!=null)
                        {
                           if(ter.getName().toLowerCase().contains("bed") && (npc.getCharIndex()==NPC.GUARD_SPEAR || npc.getCharIndex()==NPC.GUARD_SWORD))
                           {
                              NPCPlayer tempModel = new NPCPlayer(NPC.GUARD_FIST, 0, 0, 0, "");
                              g.drawImage(tempModel.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);                    
                           }
                           else if(timestop!=null)
                              g.drawImage(npc.getCurrentPicture().getImage(), npcX+xDiff+(xAdjust), npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);  
                           else if(numFrames % animDelay == 0)   
                              g.drawImage(npc.getPictureAndAdvance().getImage(), npcX+xDiff+(xAdjust), npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
                           else
                              g.drawImage(npc.getCurrentPicture().getImage(), npcX+xDiff+(xAdjust), npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);  
                           if(npc.hasEffect("web"))
                           {
                              g.drawImage(web.getImage(), npcX+xDiff-(SIZE/2), npcY+xDiff-(SIZE/2), (int)((SIZE-dist)*sizeMod*2.5), (int)((SIZE-dist)*sizeMod*2.5), null);  
                           }
                        }
                        else  //if(npc.getCharIndex()==NPC.FIRE)
                        {
                           if(timestop!=null)
                              g.drawImage(npc.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
                           else if(numFrames % (animDelay/3) == 0)   
                              g.drawImage(npc.getPictureAndAdvance().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
                           else
                              g.drawImage(npc.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
                        }
                     }
                  }
                  if(!npc.canSwim() && !npc.canFly() && NPC.getSize(npc) <= 1.5) 
                  {           
                     if(ter.getName().contains("water") )
                     {
                        double alpha = distFromDeadWinter();
                        if(ter.getName().toLowerCase().contains("rapid") || alpha < 0.75)
                        {        
                           g.drawImage(water.getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
                           if(isWinter() && !locationType.contains("underworld"))
                           {
                              if(ter.getName().toLowerCase().contains("rapid"))
                                 alpha /= 2;
                              else  if(ter.getName().toLowerCase().contains("deep"))
                                 alpha /= 1.5;
                              Color ice = new Color(240,240,255, (int)Math.min(255,(255*alpha)));
                              g.setColor(ice);
                              g.fillRect(npcX/*+xDiff*/, npcY/*+xDiff*/+(SIZE/2), (int)((SIZE/*-dist*/)*sizeMod), (int)((SIZE/*-dist*/)*sizeMod)/2);
                           }
                        }
                     }
                     else if(ter.getName().toLowerCase().contains("tall_grass") )
                     {
                        g.drawImage(grass.getImage(), npcX, npcY, SIZE, SIZE, null);
                     }
                     else if(ter.getName().toLowerCase().contains("poison") )
                     {
                        g.drawImage(poison.getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
                     }
                     else if(ter.getName().toLowerCase().contains("bog") && !isWinter())
                     {
                        g.drawImage(swamp.getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-dist)*sizeMod), (int)((SIZE-dist)*sizeMod), null);
                     }
                     else if(ter.getName().toLowerCase().contains("forest") && !ter.getName().toLowerCase().contains("dead"))
                     {
                        g.drawImage(forest.getImage(), npcX, npcY, SIZE, SIZE, null);
                     }
                  }
                  if(!npc.isStatue() &&  !NPC.isFurniture(npc) && npc.getCharIndex()!=NPC.WILLOWISP && !(npc.getCharIndex()==NPC.MIMIC && !npc.hasBeenAttacked()))    //if not a statue   
                     NPCinSight.add(npc);
                  AllNPCinSight.add(npc);   
               }
               if(npc.getHitTime() >= CultimaPanel.numFrames)
                  g.drawImage(hit.getImage(), npcX, npcY, SIZE, SIZE, null);
               if(npc.getMissTime() >= CultimaPanel.numFrames)
                  g.drawImage(miss.getImage(), npcX, npcY, SIZE, SIZE, null);
               if(timestop==null)
               {
                  int dist = (int)(findDistance(npc.getRow(), npc.getCol(), player.getRow(), player.getCol()));
                  if(mapIndex == 0)
                     dist = (int)(wrapDistance(npc.getRow(), npc.getCol(), player.getRow(), player.getCol()));
                  boolean outOfFrame = (dist >= Math.sqrt(Math.pow(CultimaPanel.SCREEN_SIZE, 2)*2));   
                  if(!outOfFrame && npc.getMapIndex()==CultimaPanel.player.getMapIndex())
                  {
                     if((npc.getWeapon().getEffect().toLowerCase().contains("fire") && !npc.getWeapon().getName().toLowerCase().contains("cannon") ) || npc.hasEffect("fire") || (npc.getCharIndex()==NPC.WILLOWISP && !npc.getLocationType().contains("pacrealm")))
                     {
                     //add fire glow around npc
                        int distFromNoon = (int)(Math.abs(time - 12));
                        if(TerrainBuilder.indoors())
                           distFromNoon = 12;
                        int baseOpacity = distFromNoon * 4;
                        int opacity = baseOpacity;
                        Color glow = Color.yellow;
                        int radius = (int)(SIZE * 2);
                        if(Math.random() < 0.5)
                        {
                           if(Math.random() < 0.5)
                              glow = Color.orange;
                           else
                              glow = Color.orange.brighter();
                           radius = Utilities.rollDie(1, SIZE*3);
                           opacity = Math.min(255, Math.max(0,Utilities.rollDie(baseOpacity-25, baseOpacity+25)));
                        }
                        g.setColor(new Color(glow.getRed(),glow.getGreen(),glow.getBlue(),opacity));
                        g.fillOval(npcX-(radius/4), npcY-(radius/4), radius, radius);
                     //end add fire glow around npc
                     }
                     if(npc.getCharIndex()==NPC.HYDRACLOPS || npc.getCharIndex()==NPC.SQUIDKING || (NPC.isGiantMonsterKing(npc) && ter.getName().toLowerCase().contains("water")))
                     {  //do splashing water around massive creatures
                        if(smoke.size()<50 && Math.random() < 0.05)
                        {
                           int radius = Utilities.rollDie(1,5);
                           int opacity = Utilities.rollDie(75,127);
                           smoke.add(new SmokePuff(npcX, npcY+(SIZE/2), radius, opacity, SmokePuff.waterColor));
                        }
                        Color [] colors = {Color.blue, Color.cyan, Color.white};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(4, SIZE/4);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY+SIZE, npcY+SIZE+(SIZE/4));
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if((npc.getCharIndex()==NPC.TORNADO) && smoke.size()<50)
                     {
                        int radius = Utilities.rollDie(1,5);
                        int opacity = Utilities.rollDie(30,50);
                        smoke.add(new SmokePuff(npcX, npcY+(SIZE/2), radius, opacity, SmokePuff.dustCloud));
                     } 
                     else if(NPC.isGiantMonsterKing(npc) && smoke.size()<50 && Math.random() < 0.05)
                     {
                        int radius = Utilities.rollDie(3,7);
                        int opacity = Utilities.rollDie(30,50);
                        smoke.add(new SmokePuff(npcX, npcY+(SIZE/2), radius, opacity, SmokePuff.dustCloud));
                     }                 
                     if(npc.hasEffect("fire"))
                     {
                        Color [] colors = {Color.red, Color.orange, Color.yellow};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(4, SIZE/4);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                        double smokeProb = 3;
                        if(npc.getCharIndex()==NPC.FIRE)
                           smokeProb = (npc.getBody()/100);
                        if(Math.random() < (0.01 * smokeProb) && smoke.size() < 50)
                        {
                           int opacity = 127;
                           if(npc.getCharIndex()==NPC.BRIGANDSHIP || npc.getCharIndex()==NPC.GREATSHIP)
                           {
                              radius = Utilities.rollDie(10);
                              opacity = Utilities.rollDie(127,255);
                           }
                           else if(npc.getCharIndex()==NPC.FIRE)
                           {
                              radius = npc.getBody()/10;
                              opacity = Utilities.rollDie(191,255);
                           }
                           else
                           {
                              radius = Utilities.rollDie(5);
                              opacity = Utilities.rollDie(63,191);
                           } 
                           smoke.add(new SmokePuff(npcX, npcY, radius, opacity));
                        }
                     } 
                     if(npc.getWeapon().getEffect().toLowerCase().contains("freeze") || npc.hasEffect("freeze"))
                     {
                        Color [] colors = {Color.blue, Color.cyan, Color.white};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(4, SIZE/8);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if(npc.hasEffect("poison"))
                     {
                        Color [] colors = {Color.green, Color.gray, Color.black};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(4, SIZE/8);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if(npc.hasEffect("curse"))
                     {
                        Color [] colors = {Color.magenta, Color.green, Color.pink};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(2, SIZE/4);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if(npc.hasEffect("sullied"))
                     {
                        Color [] colors = {Color.magenta, Color.orange, Color.pink};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(2, SIZE/4);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if(npc.hasEffect("seduced"))
                     {
                        Color [] colors = {Color.red, Color.pink};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(2, SIZE/4);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if(npc.hasEffect("control"))
                     {
                        Color [] colors = {Color.black, Color.white};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(2, SIZE/4);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if(npc.hasEffect("still"))
                     {
                        Color [] colors = {Color.pink, Color.white};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(4, SIZE/8);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if(npc.hasEffect("stun"))
                     {
                        Color [] colors = {Color.pink, Color.cyan};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(4, SIZE/8);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                     if(npc.isZombie() || npc.getCharIndex()==NPC.DEATH) //zombified - draw flies buzzing around
                     {
                        Color [] colors = {Color.black, new Color(150,40,40)};
                        g.setColor(Utilities.getRandomFrom(colors));
                        int radius = Utilities.rollDie(2, SIZE/8);
                        int randX = Utilities.rollDie(npcX, npcX+SIZE);
                        int randY = Utilities.rollDie(npcY, npcY+SIZE);
                        g.fillOval(randX, randY, radius, radius);
                     }
                  }
               }
               String npcName = npc.getName().toLowerCase();
               if(missionPriority > 0 || (npc.getBounty()>0 || (missionTargetNames!=null && missionTargetNames.contains(Utilities.shortName(npc.getName())))))
               {  //mark npcs that have missions and npcs that hold objectives (red)
                  Color dotColor = Color.red;
                  if(missionPriority==3)                 //they are a target or have an item we want
                  {
                     dotColor = Color.red;
                  }
                  else if(missionPriority==2)            //they have a mission to give
                  {
                     dotColor = Color.blue.brighter();
                  }
                  else if(missionPriority==1 || npc.isEscortedNpc())                     
                  {                                      //we have taken their mission but not completed it yet
                     dotColor = (Color.green.darker().darker());
                  }
                  else if(missionTargetNames!=null && missionTargetNames.contains(Utilities.shortName(npc.getName())))
                  {
                     dotColor = (new Color(255,150,0,75));
                  }
                  g.setColor(new Color(dotColor.getRed(),dotColor.getGreen(),dotColor.getBlue(),75));
                  g.fillOval((int)(npcX+SIZE/2.5), (int)(npcY+SIZE/2.5), SIZE/3, SIZE/3);
                  g.setColor(dotColor.darker());
                  g.drawOval((int)(npcX+SIZE/2.5), (int)(npcY+SIZE/2.5), SIZE/3, SIZE/3);
               } 
               else if(npc.getName().startsWith("+"))    //player's spouse
               {
                  if(npc.getNumInfo()==13)               //npc is aware of infidelity
                  {
                     if(numFrames % animDelay == 0)
                        g.setColor(Color.red);
                     else
                        g.setColor(Color.pink);
                     g.fillOval((int)(npcX+SIZE/2.5), (int)(npcY+SIZE/2.5), SIZE/3, SIZE/3);
                  }
                  else
                  {
                     g.setColor(Color.pink);
                     g.fillOval((int)(npcX+SIZE/2.5), (int)(npcY+SIZE/2.5), SIZE/3, SIZE/3);
                  }
               } 
            }
         }
      }
          //flash hidden npcs if the player is a vampire   
      if(player.isVampire() && menuMode == TRAVEL)
      {
         for(Coord cpCoord:allScreenNPCs.keySet())
         {
            NPCPlayer cp = allScreenNPCs.get(cpCoord);
            if(NPC.isFurniture(cp) || cp.getCharIndex()==NPC.WILLOWISP || cp.isStatue() || cp.isNonPlayer() || (cp.getCharIndex()==NPC.MIMIC && !cp.hasBeenAttacked()))
               continue;
            int npcX = cpCoord.getRow();
            int npcY = cpCoord.getCol();
            int multiplier = (5-awareness)*2;
            if(awareness == 0)
               multiplier *= 2;
            int distToNPC = (int)(findDistance(cp.getRow(), cp.getCol(), player.getRow(), player.getCol()));
            if(mapIndex == 0)
               distToNPC = (int)(wrapDistance(cp.getRow(), cp.getCol(), player.getRow(), player.getCol()));
            double sizeMod = NPC.getSize(cp);
            int xDiff = (SIZE - (int)((SIZE-distToNPC)*sizeMod));
            if(awareness>=0)
               if(numFrames % Math.max(animDelay,((animDelay*4*multiplier)-((SCREEN_SIZE-distToNPC)*(Math.max(1,awareness))))) == 0)
                  g.drawImage(cp.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-distToNPC)*sizeMod), (int)((SIZE-distToNPC)*sizeMod), null);  
         }   
      }
      for(Coord cpCoord:allScreenNPCs.keySet())
      {  //show all nightorcs even if in darkness so we only see their eyes
         NPCPlayer cp = allScreenNPCs.get(cpCoord);
         if((cp.getCharIndex()==NPC.NIGHTORC || cp.getCharIndex()==NPC.CHUD) && !cp.isStatue() && cp.getBody() > 0)
         {
            int npcX = cpCoord.getRow();
            int npcY = cpCoord.getCol();
            int multiplier = (5-awareness)*2;
            int distToNPC = (int)(findDistance(cp.getRow(), cp.getCol(), player.getRow(), player.getCol()));
            if(mapIndex == 0)
               distToNPC = (int)(wrapDistance(cp.getRow(), cp.getCol(), player.getRow(), player.getCol()));
            double sizeMod = NPC.getSize(cp);
            int xDiff = (SIZE - (int)((SIZE-distToNPC)*sizeMod));
            g.drawImage(cp.getCurrentPicture().getImage(), npcX+xDiff, npcY+xDiff, (int)((SIZE-distToNPC)*sizeMod), (int)((SIZE-distToNPC)*sizeMod), null);  
         }
      }
      return new Coord(x, y);
   }
   
   //given a weapon, returns the color of the hit vector that is drawn
   public static Color colorForWeapon(Weapon weapon)
   {
      String weap = weapon.getName().toLowerCase();
      Color ans = Color.red;
      if(weap.contains("fire") || weap.contains("flame") || weap.contains("burn") || weap.contains("magma") || weap.contains("torch"))
         ans = (Color.orange);
      else if(weap.contains("lightning") || weap.contains("cannon"))
         ans = (Color.yellow);
      else if(weap.contains("ice") || weap.contains("frost"))
         ans = (Color.white);
      else if(weap.contains("stone"))
         ans = (new Color(180,180,180));
      else if(weap.contains("vampyric"))
         ans = (Color.red.darker());
      else if(weap.contains("bow") || weap.contains("caster") || weap.contains("hooves") || weap.contains("club") || weap.contains("spear") || weap.contains("staff"))
         ans = (new Color(170,90,90));   
      else if(weap.contains("sword") || weap.contains("dagger") || weap.contains("axe") || weap.contains("hammer") || weap.contains("mace") || weap.contains("blade") || weap.contains("halberd") || weap.contains("sceptre"))
         ans = (new Color(173,173,173));
      else if(weap.contains("death"))
         ans = (Color.black);  
      else if(weap.contains("poison"))
         ans = (Color.green);   
      return(new Color(ans.getRed(), ans.getGreen(), ans.getBlue(), 110));
   }

   public static void swap(ArrayList<Location> list, int a, int b)
   {
      Location temp = list.get(a);
      list.set(a, list.get(b));
      list.set(b, temp);
   }

   //return an array of the find the 5 closest locations, and show as many on the MAP depending on mapSkill
   public static ArrayList<Location> closestLocationsTo(int row, int col)
   {
      int mapIndex = 0;
      int awareness = player.getAwareness();
   
      if((player.getActiveSpell("Focus")!=null || player.hasItem("focushelm"))&& awareness < 4)       //if we have Focus spell activated, show all 5 locations like we have Eagle-eye
         awareness = 4;
   
      ArrayList<Location>found = new ArrayList<Location>();
      for(int i=1; i< CultimaPanel.allLocations.size(); i++)   //we skip index 0, which is a temporary location to make the location index match its mapIndex
      {
         Location loc = (CultimaPanel.allLocations.get(i));
         if(loc.getFromMapIndex() == 0)
            if(((map.get(mapIndex))[loc.getRow()][loc.getCol()]) < 0 || awareness==4)   //negative values means that we have visited there
               found.add(loc);
      }
      for(int i=0; i<found.size(); i++)
      {
         int min = i;
         for(int j=i+1; j<found.size(); j++)
         {
            double distMin = wrapDistance(row, col, found.get(min).getRow(), found.get(min).getCol());
            double distJ = wrapDistance(row, col, found.get(j).getRow(), found.get(j).getCol());
            if(distJ < distMin)
               min = j;
         }
         swap(found, i, min);
      }
      return found;
   }

   public static Location closestLocationTo(int row, int col)
   {
      Location closest = null;
      double minDist = Double.MAX_VALUE;
      for(int i=1; i< CultimaPanel.allLocations.size(); i++)
      {
         Location loc = CultimaPanel.allLocations.get(i);
         if(loc.getFromMapIndex() == 0)
         {
            double dist = wrapDistance(row, col, loc.getRow(), loc.getCol());
            if(dist < minDist)
            {
               minDist = dist;
               closest = loc;
            }
         }
      }
      return closest;
   }

   public static String closestProvinceTo(int row, int col)
   {
      String closest = "?";
      double minDist = Double.MAX_VALUE;
      for(int i=1; i < CultimaPanel.allLocations.size(); i++)
      {
         Location loc = CultimaPanel.allLocations.get(i);
         if(loc.getFromMapIndex() == 0)
         {
            double dist = wrapDistance(row, col, loc.getRow(), loc.getCol());
            if(dist < minDist)
            {
               minDist = dist;
               closest = loc.getName();
            }
         }
      }
      if(minDist <= 3)        //show full name if we are close enough to the Location
         return closest;
      return provinceName(closest);
   }

//given the name of a location, returns it without a prefix or suffix
   public static String provinceName(String locName)
   {
      int spacePos = locName.indexOf(" ");
      if(spacePos >= 0)       //if we are not right next to the Location, trim the name of titles
      {
         String [] parts = locName.split(" ");
         if(parts.length != 2)
            return locName;
         String prefix = parts[0];
         String suffix = parts[1];
         for(String pre: Utilities.cityPrefix)
         {
            if(pre.equals(prefix))
               return suffix;
         }
         for(String pre: Utilities.portPrefix)
         {
            if(pre.equals(prefix))
               return suffix;
         }
         for(String pre: Utilities.capitalPrefix)
         {
            if(pre.equals(prefix))
               return suffix;
         }
         for(String suf: Utilities.citySuffix)
         {
            if(suf.equals(suffix))
               return prefix;
         }
         for(String suf: Utilities.portSuffix)
         {
            if(suf.equals(suffix))
               return prefix;
         }
         for(String suf: Utilities.battlefieldSuffix)
         {
            if(suf.equals(suffix))
               return prefix;
         }
         return prefix;
      }
      return locName;
   }

   //given the time, returns the text description of what time of day it is
   public static String timeOfDay(double time)
   {
      if(time <= 2)
         return "Dead Of Night";
      if(time <= 4)
         return "Pre Dawn";
      if(time <= 6)
         return "Dawn";
      if(time <= 8)
         return "Early Morn";
      if(time <= 10)
         return "Morning";
      if(time <= 12)
         return "Late Morn";
      if(time <= 14)    //2 PM
         return "Afternoon";
      if(time <= 16)    //4 PM
         return "Late Afternoon";
      if(time <= 18)    //6 PM
         return "Pre Dusk";
      if(time <= 20)    //8 PM
         return "Dusk";
      if(time <= 22)    //10 PM
         return "Evening";
      return "Midnight";
   }
   
   //returns the text description of what time of year it is
   public static String getSeason()
   {
      int rem = (days + player.getStartDay()) % 60;
      if(isSummer())
      {
         if(rem<=5)
            return "Early Summer";
         if(rem<=10)
            return "Midsummer";
         if(rem<=15)
            return "Late Summer";
         return "Summer";
      }
      if(isFall())
      {
         if(rem<=20)
            return "Early Autumn";
         if(rem<=25)
            return "Midautumn";
         if(rem<=30)
            return "Late Autumn";
         return "Autumn";
      }
      if(isWinter())
      {
         if(rem<=35)
            return "Early Winter";
         if(rem<=40)
            return "Midwinter";
         if(rem<=45)
            return "Late Winter";
         return "Winter";
      }
      if(isSpring())
      {
         if(rem<=50)
            return "Early Spring";
         if(rem<=55)
            return "Midspring";
         if(rem<=60)
            return "Late Spring";
         return "Spring";
      }
      return "";
   }

   //given Color from and Color to, returns a new Color that is increment closer between the two
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
      ansRed = Math.min(255, ansBlue);
      return new Color(ansRed, ansGreen, ansBlue);
   }

   public static boolean canHaveThunderstorms()
   {  //we can have thunderstroms from mid spring through mid fall
      if(CultimaPanel.player.getLocationType().contains("pacrealm"))
         return false;
      int rem = (days + player.getStartDay()) % 60;         
      return (rem <= 23 || rem >= 53);
   }

  //true if it is summer
   public static boolean isSummer()
   {
      int rem = (days + player.getStartDay()) % 60;         
      return (rem <= 15);
   }
   
   //true if it is the fall season
   public static boolean isFall()
   {
      int rem = (days + player.getStartDay()) % 60;         
      return (rem >= 16 && rem <= 30);
   }
   
   //true if it is winter
   public static boolean isWinter()
   {
      if(player.getLocationType().contains("jurassica"))
         return false;
      int rem = (days + player.getStartDay()) % 60;
      return (rem >= 31 && rem <= 45);
   }
   
   //true if it is spring
   public static boolean isSpring()
   {
      int rem = (days + player.getStartDay()) % 60;
      return (rem >= 46);
   }

   //returns the distance from the middle of winter as a percentage
   public static double distFromDeadWinter()
   {
      if(!isWinter())
         return 0;
      int rem = (days + player.getStartDay()) % 60;         
      return 1.0 - (Math.abs(38 - rem)/7.5);
   }
   
   //true if it is mid winter when the water should be frozen over
   public static boolean frozenWater()
   {
      int rem = (days + player.getStartDay()) % 60;         
      return (rem >= 37 && rem <= 39);
   }
   
   //define the winter snow cover outside the method so we can see it for comparison in CultimaPanel to limit picking up flowers
   public static Color winter = new Color(220, 200, 255);
   //return the color that the ground should be depending on which season it is
   public static Color findGroundColor(boolean first)
   {
      Color fall = new Color(60, 0, 0);
      Color spring = new Color(0, 35, 0);
      Color summer = new Color(0, 25, 0);
       
      if(player.getLocationType().contains("jurassica"))
         return summer;
         
      if(groundColor==null || groundColor.equals(Color.black) || first)
      {
         if(isSpring())
            groundColor = spring;
         if(isSummer())
            groundColor = summer;
         if(isFall())
            groundColor = fall;
         if(isWinter())
            groundColor = winter;
      }
      
      Color ans = groundColor;
      
      if(isWinter() && !groundColor.equals(winter) && weather > 0)
      {  //transition groundColor towards winter
         ans = moveColorTowards(groundColor, winter, 1);//weather);
      }
      else if(isSpring() && !groundColor.equals(spring))
      {  //transition groundColor towards spring
         ans = moveColorTowards(groundColor, spring, 1);
      }
      else if(isSummer() && !groundColor.equals(summer))
      {  //transition groundColor towards summer
         ans = moveColorTowards(groundColor, summer, 1);
      }
      else if(isFall() && !groundColor.equals(fall))
      {  //transition groundColor towards fall
         ans = moveColorTowards(groundColor, fall, 1);
      }
      return ans;
   }

  //pre: time is between [0, 24)
  //post: returns the color to use for darkened or hidden spaces, accounting for if it is cloudy, indoor or outdoor
   public static Color timeColor(double time, String locationType)
   {  //                            "Dead Of Night",        "Pre Dawn",          "Dawn",                 "Early Morn",           "Morning",                   "Late Morn",            2 PM "Afternoon",         4 PM "Late Afternoon",    6 PM "Pre Dusk",   8 PM "Dusk",           10 PM "Evening",        12 PM "Midnight"   
      //int [] timeIntervals =      {   2,                   4,                      6,                      8,                       10,                       12,                        14,                     16,                        18,                  20,                     22,                  24};
      Color[] indoorColors =        {new Color(5, 10, 20), new Color(14, 14, 20), new Color(25, 25, 40), new Color( 35,  35,  50), new Color( 45, 45,  60),  new Color(55, 55, 70),  new Color( 45, 45,  60),  new Color(38, 38, 53), new Color(31, 31, 46), new Color(24, 24, 43), new Color(15, 15, 40), new Color(0, 0, 40)};
      Color[] outdoorColorsSpring = {new Color(5, 10, 20), new Color(10, 40, 10), new Color(15, 60, 15), new Color( 20,  80,  20), new Color( 25, 100,  25), new Color(30, 120, 30), new Color( 25, 100,  25), new Color(20, 80, 30), new Color(15, 60, 35), new Color(10, 40, 60), new Color(5, 20, 40),  new Color(0, 0, 40)};
      
      Color[] outdoorColorsFall =   {new Color(30, 5, 20), new Color(65, 10, 10), new Color(100, 20, 20), new Color( 130,  30,  30), new Color(140,  40,  40), new Color(160, 50, 50), new Color(140,  40,  40), new Color(130, 30, 35), new Color(100, 20, 35), new Color(65, 10, 60), new Color(30, 5, 40),  new Color(0, 0, 40)};
      
      Color[] cloudyColors =        {new Color(15, 15, 15),new Color(20, 20, 20), new Color(40, 40, 40), new Color(70, 70, 70),    new Color(100, 100, 100), new Color(120, 120, 120),new Color(100, 100, 100),new Color(80, 80, 80), new Color(60, 60, 60), new Color(40, 40, 40), new Color(20, 20, 20), new Color(10, 10, 10)};
      Color[] underworldColors =    {new Color(10, 5, 20), new Color(40, 10, 10), new Color(60, 15, 15), new Color( 80,  20,  20), new Color( 100, 25,  25), new Color(120, 30, 30), new Color( 100, 25,  25), new Color(80, 20, 30), new Color(60, 15, 35), new Color(40, 10, 60), new Color(20, 5, 40),  new Color(0, 0, 40)};
      boolean cloudy = (weather > 1 || locationType.contains("wolfenstein"));
      boolean outDoor = (locationType.contains("world") || locationType.contains("temple") || locationType.contains("battlefield") || locationType.contains("jurassica") || locationType.contains("1942"));
      int lowerTime = 0, upperTime = 0;
      time = ((int)(time * 100))/100.0;
      int wholeTime = (int)(time);
      if(wholeTime % 2 != 0)
      {
         lowerTime = (int)(wholeTime - 1);
         upperTime = (int)(wholeTime + 1);
      }
      else
      {
         lowerTime = (int)(wholeTime);
         upperTime = (int)(wholeTime + 2);
      }
      Color lowerColor = null, upperColor = null;
   
      int lowerIndex = (lowerTime/2)-1;
      int upperIndex = (upperTime/2)-1;
      if(upperIndex < 0)
      {
         lowerIndex = indoorColors.length-2;
         upperIndex = indoorColors.length-1;
      }
      if(lowerIndex < 0)
      {
         lowerIndex = indoorColors.length-1;
         upperIndex = 0;
      }
      if(locationType.contains("underworld"))
      {
         lowerColor = underworldColors[lowerIndex];
         upperColor = underworldColors[upperIndex];
      }
      else if(cloudy)
      {
         lowerColor = cloudyColors[lowerIndex];
         upperColor = cloudyColors[upperIndex];
      }
      else if(outDoor)
      {
         if(isSpring() || isSummer())
         {
            lowerColor = outdoorColorsSpring[lowerIndex];
            upperColor = outdoorColorsSpring[upperIndex];
         }
         else
         {
            lowerColor = outdoorColorsFall[lowerIndex];
            upperColor = outdoorColorsFall[upperIndex];
         }
      }
      else
      {
         lowerColor = indoorColors[lowerIndex];
         upperColor = indoorColors[upperIndex];
      }
      
      if(locationType.contains("jurassica"))          //shift colors to make jusrassica look a little different
      {
         int lowerRed = lowerColor.getRed();
         int lowerGreen = lowerColor.getGreen();
         int lowerBlue = lowerColor.getBlue();
         int upperRed = upperColor.getRed();
         int upperGreen = upperColor.getGreen();
         int upperBlue = upperColor.getBlue();
         
         lowerRed = (int)(Math.max(0, lowerRed*0.75));
         lowerGreen = (int)(Math.max(0, lowerGreen*0.85));
         lowerBlue = (int)(Math.min(255, lowerBlue*1.5));
         upperRed = (int)(Math.max(0, upperRed*0.75));
         upperGreen = (int)(Math.max(0, upperGreen*0.85));
         upperBlue = (int)(Math.min(255, upperBlue*1.5));
         lowerColor = new Color(lowerRed, lowerGreen, lowerBlue);
         upperColor = new Color(upperRed, upperGreen, upperBlue);
      }
      
      
      double percentRed = ((time - lowerTime)/2)*100; //percentage of difference between time and lowerTime
      double percentGreen = ((time - lowerTime)/2)*100; //percentage of difference between time and lowerTime
      double percentBlue = ((time - lowerTime)/2)*100; //percentage of difference between time and lowerTime
   
      int lowerRed = lowerColor.getRed();
      int upperRed = upperColor.getRed();
      if(lowerRed > upperRed)
         percentRed = 100 - percentRed;
      int maxRed = Math.max(lowerRed, upperRed);
      int minRed = Math.min(lowerRed, upperRed);
     
      int lowerGreen = lowerColor.getGreen();
      int upperGreen = upperColor.getGreen();
      if(lowerGreen > upperGreen)
         percentGreen = 100 - percentGreen;
      int maxGreen = Math.max(lowerGreen, upperGreen);
      int minGreen = Math.min(lowerGreen, upperGreen);
   
      int lowerBlue = lowerColor.getBlue();
      int upperBlue = upperColor.getBlue();
      if(lowerBlue > upperBlue)
         percentBlue = 100 - percentBlue;
      int maxBlue = Math.max(lowerBlue, upperBlue);
      int minBlue = Math.min(lowerBlue, upperBlue);
      
      int midRed = (int)((percentRed * (maxRed - minRed) / 100.0) + minRed);
      int midGreen = (int)((percentGreen * (maxGreen - minGreen) / 100.0) + minGreen);
      int midBlue = (int)((percentBlue * (maxBlue - minBlue) / 100.0) + minBlue);
      
      midRed = Math.max(0,midRed);        
      midRed = Math.min(255,midRed);
      midGreen = Math.max(0, midGreen);
      midGreen = Math.min(255, midGreen);
      midBlue = Math.max(0, midBlue);
      midRed = Math.min(255, midBlue);
      return new Color(midRed, midGreen, midBlue);
   }
   
   public static double distanceSimple (int x1, int y1, int x2, int y2)
   {
      return Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)));
   }

   public static double findDistance(int x1, int y1, int x2, int y2)
   {
      if(player.getMapIndex()==0)
         return wrapDistance(x1, y1, x2, y2);
      return distanceSimple(x1, y1, x2, y2);
   }

//distance between cells with wrap-around depending on map dimensions, assuming we are in the greater world
   public static double wrapDistance(int x1, int y1, int x2, int y2)
   {
      int mapIndex = 0;
      int numRows = (map.get(mapIndex)).length;
      int numCols = (map.get(mapIndex))[0].length;
      int dx = Math.abs(x2-x1);
      int dy = Math.abs(y2-y1);
      int newDx = numCols - dx;
      int newDy = numRows - dy;
      if(dx < numCols/2)
         newDx = dx;
      if(dy < numRows/2)
         newDy = dy;
      return Math.sqrt(newDx*newDx + newDy*newDy);
   }

//given the coordinates of a vision-blocked terrain, this returns true if it has any neighbors that also block a view
//this is used to not block the view from tiles that have no blocked neighbors in blockedView()   
   public static boolean hasBlockedNeighbors(int r, int c)
   {
      int mapIndex = player.getMapIndex();
      for(int i=r-1; i<=r+1; i++)
         for(int j=c-1; j<=c+1; j++)
         {
            int nr = i;
            int nc = j;
            if(nr < 0)                  //wrap-around if needed
               nr = (map.get(mapIndex)).length-1;
            if(nc < 0)
               nc = (map.get(mapIndex))[0].length-1;
            nr %= (map.get(mapIndex)).length;
            nc %= (map.get(mapIndex))[0].length;
            if(nr==r && nc==c)
               continue;
            if((map.get(mapIndex))[nr][nc]==0 || allTerrain.get(Math.abs((map.get(mapIndex))[nr][nc])).getName().contains("_B_"))
               return true;
         }
      return false;
   }   

   public static boolean allBlockedNeighbors(int r, int c)
   {
      int mapIndex = player.getMapIndex();
      int count = 0;
      for(int i=r-1; i<=r+1; i++)
         for(int j=c-1; j<=c+1; j++)
         {
            int nr = i;
            int nc = j;
            if(nr < 0)                  //wrap-around if needed
               nr = (map.get(mapIndex)).length-1;
            if(nc < 0)
               nc = (map.get(mapIndex))[0].length-1;
            nr %= (map.get(mapIndex)).length;
            nc %= (map.get(mapIndex))[0].length;
            if(nr==r && nc==c)
               continue;
            if((map.get(mapIndex))[nr][nc]==0 || allTerrain.get(Math.abs((map.get(mapIndex))[nr][nc])).getName().contains("_B_"))
               count++;
         }
      if(count==8)
         return true;
      return false;
   
   }

//returns List of (r,c) Terrain locations that should be hidden by darkness (determined by time of day)
   public static ArrayList<Coord> darkenedCells(double tempTime)
   {
      int mapIndex = player.getMapIndex();
      ArrayList<Coord> temp = new ArrayList();
      ArrayList<Coord> lights = new ArrayList();   //stores which locations cast light to exclude darkness
      int x =0, y=0;		                           //upper left corner location of where image will be drawn
   
      if((flashColor != null && numFrames <= flashFrame+Math.max(3,animDelay/2)) || player.getLocationType().equals("pacrealm"))
         return temp;
   
      boolean indoors = TerrainBuilder.indoors();
   
   //see if player has a light spell
      boolean timestop = (player.getActiveSpell("Timestop")!=null);
      boolean hasLight = false;  //light spell - most light, doesn't flicker
      boolean hasBright = false; //bright weapons - less light, doesn't flicker
      boolean hasFlame = false;  //flame weapons - less light, flickers
      boolean hasTorch = false;  //torch weapons - more light, flickers
      final double torchFlickerChance = 0.23;
      final double flameFlickerChance = 0.15;
      String weapName = player.getWeapon().getName().toLowerCase();
      String armName = player.getArmor().getName().toLowerCase();
      
      if(!player.isHiding())
      {
         if(weapName.contains("bright") || weapName.equals("magestaff") || weapName.equals("ohnyalei") || Weapon.isLegendaryWeapon(weapName) || player.hasItem("blessed-crown"))
            hasBright = true;
         if((player.getWeapon().getEffect().contains("fire") || weapName.contains("flame") || weapName.contains("fire") || armName.contains("holocaust")) && (indoors || weather <= 2 || isWinter()))
            hasFlame = true;
         if((weapName.contains("torch") || CultimaPanel.player.isCampingWithFire()) && (indoors || weather <= 2 || isWinter()))
            hasTorch = true;  
      }
      for(Spell s: player.getActiveSpells())
      {
         if(s.getName().contains("Light"))
         {
            hasLight = true;
            break;
         }
      }
      if(timestop)
         hasLight = true;
      if(hasLight)
      {
         if(tempTime <= 3.5)
            tempTime = 5;
         else if(tempTime >= 20.5)
            tempTime = 18;
      }
      else if(hasTorch)
      {
         int min1 = 3, min2 = 17;
         int max1 = 6, max2 = 20;
         if( CultimaPanel.player.isCampingWithFire() && CultimaPanel.player.getMapIndex()==0)
         {  //if we are camping in the world, make the light radius smaller (zoomed out)
            min1--;
            max1-=2;
            min2--;
            max2-=2;;
         }
         if(tempTime <= 3.5)
         {
            if(Math.random() < torchFlickerChance)
               tempTime = Utilities.rollDie(min1,max1);
            else
               tempTime = 4;
         }
         else if(tempTime >= 20.5)
         {
            if(Math.random() < torchFlickerChance)
               tempTime = Utilities.rollDie(min2,max2);
            else
               tempTime = 4;
         }
      }
      else if(hasBright)
      {
         if(tempTime <= 3.5)
            tempTime = 4;
         else if(tempTime >= 20.5)
            tempTime = 19;
      }
      else if(hasFlame)
      {
         if(tempTime <= 3.5)
         {
            if(Math.random() < flameFlickerChance)
               tempTime = Utilities.rollDie(3,4);
            else
               tempTime = 3;
         }
         else if(tempTime >= 20.5)
         {
            if(Math.random() < flameFlickerChance)
               tempTime = Utilities.rollDie(19,20);
            else
               tempTime = 3;
         }
      }
      for(int i=0; i<SCREEN_SIZE; i++)
      {
         int r = equalizeRow((player.getRow() - (SCREEN_SIZE/2)) + i);
         x = 0;						//reset the row distance
         for(int j=0; j<SCREEN_SIZE; j++)
         {
            int c = equalizeCol((player.getCol() - (SCREEN_SIZE/2)) + j);
            NPCPlayer npc = Utilities.getNPCAtFromScreenRange(r, c, mapIndex);
            if(npc!=null)
            {
               weapName = npc.getWeapon().getName().toLowerCase();
               if(weapName.contains("bright") || (npc.getCharIndex()==NPC.SOLDIER || npc.getCharIndex()==NPC.OFFICER || npc.getCharIndex()==NPC.TANK || npc.getCharIndex()==NPC.WILLOWISP || (CultimaPanel.player.getLocationType().contains("camp") && (NPC.isGuard(npc.getCharIndex()) || NPC.isAnyBrigand(npc.getCharIndex())))))
                  lights.add(new Coord(r, c));
               else if(npc.getWeapon().getEffect().contains("fire") || weapName.contains("flame") || weapName.contains("fire") || (npc.getCharIndex()==NPC.GREATSHIP || npc.getCharIndex()==NPC.BRIGANDSHIP || npc.getCharIndex()==NPC.TRADER)) 
                  lights.add(new Coord(r, c));
            }
            if(allTerrain.get(Math.abs((map.get(mapIndex))[r][c])).getName().contains("_L_"))
               lights.add(new Coord(r, c));
            NPCPlayer fire = Utilities.getNPCAtFromScreenRange(r,  c, mapIndex);
            if(fire!=null && (fire.getCharIndex()==NPC.FIRE || fire.getCharIndex()==NPC.MAGMA || fire.hasEffect("fire")))
               lights.add(new Coord(r, c));
         //limit how far we can see depending on tempTime of day
            Coord current = new Coord(r,c);
            if((tempTime <= 2 || tempTime >= 22) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*3 && !temp.contains(current))
               temp.add(current);
            else if((tempTime <= 2.5 || tempTime >= 21.5) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*4)
               temp.add(current);
            else if((tempTime <= 3 || tempTime >= 21) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*5)
               temp.add(current);
            else if((tempTime <= 3.5 || tempTime >= 20.5) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*6)
               temp.add(current);
            else if((tempTime <= 4 || tempTime >= 20) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*7)
               temp.add(current);
            else if((tempTime <= 4.5 || tempTime >= 19.5) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*8)
               temp.add(current);
            else if((tempTime <= 5 || tempTime >= 19) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*9)
               temp.add(current);
            else if((tempTime <= 5.5 || tempTime >= 18.5) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*10)
               temp.add(current);
            else if((tempTime <= 6 || tempTime >= 18) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*11)
               temp.add(current);
            else if((tempTime <= 6.5 || tempTime >= 17.5) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*12)
               temp.add(current);
            else if((tempTime <= 7 || tempTime >= 17) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*13)
               temp.add(current);
            else if((tempTime <= 7.5 || tempTime >= 16.5) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*14)
               temp.add(current);
            else if((tempTime <= 8 || tempTime >= 16) && findDistance((SCREEN_SIZE/2)*SIZE, (SCREEN_SIZE/2)*SIZE, x, y) > SIZE*15)
               temp.add(current);
         
            x+=SIZE;
         }
         y+=SIZE;
      }
      for(Coord p: lights)
      {
         int lightR = p.getRow();
         int lightC = p.getCol();
         temp.remove(p);
         String lightName = allTerrain.get(Math.abs((map.get(mapIndex))[lightR][lightC])).getName();
      
         if(lightName.toLowerCase().contains("counter_lighted") && (time >= 20 || time <= 6))
         {  //turn the lights off in the shoppes when they are closed down
            continue;   
         }                      
      
         int value = 4;             //radius of light around this tile
         if(lightName.length() >=3) //extract the light value from the terrain name, i.e. INR_I_B_L_4_$Red_Brick_Torch would have a light radius of 4
         {
            int pos = lightName.indexOf("_L_");
            String part = lightName.substring(pos+1);
            String radius = "";
            for(int i=0; i<part.length(); i++)
            {
               char digit = part.charAt(i);
               if(Character.isDigit(digit))
               {
                  radius += digit;
                  break;
               }
            }
            if(radius.length() >= 1)
               value = Integer.parseInt(radius);
            if((lightName.toLowerCase().contains("torch") || lightName.toLowerCase().contains("fire") || lightName.toLowerCase().contains("black")) && !player.getLocationType().toLowerCase().contains("wolfenstein"))
            {
               if(timestop)
                  value += 2;
               else
                  value += (int)(Math.random()*3) - 1;   
            }
         }
         for(int i=0; i<temp.size(); i++)
         {
            Coord d = temp.get(i);
            int darkR = d.getRow();
            int darkC = d.getCol();
            double dist = wrapDistance(lightR, lightC, darkR, darkC);
            if(dist <= value)
            {
               temp.remove(d);
               i--;
            }
         }
      }
      return temp;
   }

   public static ArrayList<Coord> blockedView()
   {
      int mapIndex = player.getMapIndex();
      Terrain currentPos = allTerrain.get(Math.abs((map.get(mapIndex))[player.getRow()][player.getCol()]));      //don't block view if we are already in forest or on a mountain
      Spell flight = player.getActiveSpell("Flight");
      boolean indoors = TerrainBuilder.indoors();
      if(currentPos.getName().contains("Mountain") || currentPos.getName().contains("Mountain") || player.getLocationType().equals("ship") || (flight!=null && !indoors) || player.getLocationType().equals("pacrealm"))  
         return new ArrayList<Coord>();
   //Dense-forest
      if(currentPos.getName().contains("Dense")  && hasBlockedNeighbors(player.getRow(), player.getCol())) //block everything except cells within 1 block of the player
      {
         ArrayList<Coord>ans = new ArrayList();
         for(int i=0; i<=SCREEN_SIZE; i++)      
         {
            int r = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + i)); 
            for(int j=0; j<=SCREEN_SIZE; j++)
            {
               int c = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + j));
               if(findDistance(r, c, player.getRow(), player.getCol()) > 1.5)
                  ans.add(new Coord(r, c));
            }
         }
         return ans;
      }
   
      HashSet<Coord> temp = new HashSet();
   
      for(int i=0; i<=SCREEN_SIZE/2; i++)       //upper-left quadrant
      {
         int r = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + i)); 
         for(int j=0; j<=SCREEN_SIZE/2; j++)
         {
            int c = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + j)); 
            String current = allTerrain.get(Math.abs((map.get(mapIndex))[r][c])).getName();
         //  double distToCurrent = distanceWrap(r, c, player.getRow(), player.getCol());
            if(currentPos.getName().contains("Hills") && current.contains("Dense"))
            {}    //if we are on a hill (SnV), we can see over forest (SFB)
            else
               if(current.contains("_B_") && hasBlockedNeighbors(r, c))           
               {                                //Terrain names that end with "B" will block your view behind them
                  int numRows = 1;
                  int numCols = 1;
                  if(i >= j)                                      
                  {      
                     for(int nc=j-1; nc>=0; nc--)                       // *---
                     {                                                  // **--
                        for(int nr=i; nr>=i-numRows; nr--)              // ----
                        {                                               // ----
                           int row = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + nr));
                           int col = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + nc));
                        // double distToThis = distanceWrap(row, col, player.getRow(), player.getCol());
                           if(allTerrain.get(Math.abs((map.get(mapIndex))[row][col])).getName().contains("_B_") && (row==r || col==c) && !allBlockedNeighbors(r, c))
                           {}
                           else
                              temp.add(new Coord(row, col));
                        }
                        numRows++;
                     }                                                 
                  }                                                  
                  else                                   
                  {    
                                    
                     for(int nr=i-1; nr>=0; nr--)                       // **--
                     {                                                  // -*--
                        for(int nc=j; nc>=j-numCols; nc--)              // ----
                        {                                               // ----
                           int row = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + nr));
                           int col = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + nc));
                           if(allTerrain.get(Math.abs((map.get(mapIndex))[row][col])).getName().contains("_B_") && (row==r || col==c) && !allBlockedNeighbors(r, c))
                           {}
                           else
                              temp.add(new Coord(row, col));
                        }
                        numCols++;
                     }                                                                 
                  }                                   
               }
         }
      }
   
      for(int i=0; i<=SCREEN_SIZE/2; i++)       //upper-right quadrant
      {
         int r = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + i)); 
      
         for(int j=SCREEN_SIZE/2; j<=SCREEN_SIZE; j++)
         {
            int c = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + j)); 
            String current = allTerrain.get(Math.abs(Math.abs((map.get(mapIndex))[r][c]))).getName();
            if(currentPos.getName().contains("Hills") && current.contains("Dense"))
            {}    //if we are on a hill (SnV), we can see over forest (SFB)
            else
               if(current.contains("_B_") && hasBlockedNeighbors(r, c))           
               {                                //Terrain names that end with "B" will block your view behind them
                  int numRows = 0;
                  int numCols = 1;
                  if(i <= (SCREEN_SIZE - j))                            // --**
                  {                                                     // --*-
                     for(int nr=i-1; nr>=0; nr--)                       // ----
                     {                                                  // ----
                        for(int nc=j; nc<=j+numCols+1; nc++)                  
                        {                                           
                           int row = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + nr));
                           int col = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + nc));
                           if(allTerrain.get(Math.abs((map.get(mapIndex))[row][col])).getName().contains("_B_") && (row==r || col==c) && !allBlockedNeighbors(r, c))
                           {}
                           else
                              temp.add(new Coord(row, col));
                        }
                        numCols++;
                     }                                                
                  }                
                  else                                         // ---*
                  {                                            // --**
                     for(int nc=j+1; nc<SCREEN_SIZE; nc++)     // ----            
                     {                                         // ----            
                        for(int nr=i; nr>=i-numRows; nr--)                     
                        {                                               
                           int row = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + nr));
                           int col = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + nc));
                           if(allTerrain.get(Math.abs((map.get(mapIndex))[row][col])).getName().contains("_B_") && (row==r || col==c) && !allBlockedNeighbors(r, c))
                           {}
                           else
                              temp.add(new Coord(row, col));
                        }
                        numRows++;
                     }
                  
                  }               
               }
         }
      }
   
      for(int i=SCREEN_SIZE/2; i<=SCREEN_SIZE; i++)       //lower-right quadrant
      {
         int r = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + i)); 
      
         for(int j=SCREEN_SIZE/2; j<=SCREEN_SIZE; j++)
         {
            int c = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + j)); 
            String current = allTerrain.get(Math.abs(Math.abs((map.get(mapIndex))[r][c]))).getName();
            if(currentPos.getName().contains("Hills") && current.contains("Dense"))
            {}    //if we are on a hill (SnV), we can see over forest (SFB)
            else
               if(current.contains("_B_") && hasBlockedNeighbors(r, c))           
               {                                //Terrain names that end with "B" will block your view behind them
                  int numRows = 1;
                  int numCols = 1;
                  if(i <= j)                                           // ----
                  {                                                    // ----
                     for(int nc=j+1; nc<SCREEN_SIZE; nc++)             // --**
                     {                                                 // ---*
                        for(int nr=i; nr<=i+numRows; nr++)
                        {
                           int row = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + nr));
                           int col = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + nc));
                           if(allTerrain.get(Math.abs((map.get(mapIndex))[row][col])).getName().contains("_B_") && (row==r || col==c) && !allBlockedNeighbors(r, c))
                           {}
                           else
                              temp.add(new Coord(row, col));
                        }
                        numRows++;
                     }
                  }                                                     
                  else                                      // ----
                  {                                         // ----
                     for(int nr=i+1; nr<SCREEN_SIZE; nr++)  // --*-
                     {                                      // --**
                        for(int nc=j; nc<=j+numCols; nc++)
                        {
                           int row = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + nr));
                           int col = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + nc));
                           if(allTerrain.get(Math.abs((map.get(mapIndex))[row][col])).getName().contains("_B_") && (row==r || col==c) && !allBlockedNeighbors(r, c))
                           {}
                           else
                              temp.add(new Coord(row, col));
                        }
                        numCols++;
                     }
                  }                                         
               }
         }
      }
   
      for(int i=SCREEN_SIZE/2; i<=SCREEN_SIZE; i++)       //lower-left quadrant
      {
         int r = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + i)); 
      
         for(int j=0; j<=SCREEN_SIZE/2; j++)
         {
            int c = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + j)); 
            String current = allTerrain.get(Math.abs(Math.abs((map.get(mapIndex))[r][c]))).getName();
            if(currentPos.getName().contains("Hills") && current.contains("Dense"))
            {}    //if we are on a hill (SnV), we can see over forest (SFB)
            else
               if(current.contains("_B_") && hasBlockedNeighbors(r, c))           
               {                                //Terrain names that end with "B" will block your view behind them
                  int numRows = 1;
                  int numCols = 1;
                  if ((SCREEN_SIZE - i) <= j)                           // ----
                  {                                                     // ----
                     for(int nr=i+1; nr<SCREEN_SIZE; nr++)              // -*--
                     {                                                  // **--
                        for(int nc=j-numCols; nc<=j; nc++)
                        {
                           int row = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + nr));
                           int col = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + nc));
                           if(allTerrain.get(Math.abs((map.get(mapIndex))[row][col])).getName().contains("_B_") && (row==r || col==c) && !allBlockedNeighbors(r, c))
                           {}
                           else
                              temp.add(new Coord(row, col));
                        }
                        numCols++;
                     }                                                   
                  }                                                     
                  else                                      // ----
                  {                                         // ----
                     for(int nc=j-1; nc>=0; nc--)           // **--
                     {                                      // *---
                        for(int nr=i; nr<=i+numRows; nr++)
                        {
                           int row = equalizeRow(((player.getRow() - (SCREEN_SIZE/2)) + nr));
                           int col = equalizeCol(((player.getCol() - (SCREEN_SIZE/2)) + nc));
                           if(allTerrain.get(Math.abs((map.get(mapIndex))[row][col])).getName().contains("_B_") && (row==r || col==c) && !allBlockedNeighbors(r, c))
                           {}
                           else
                              temp.add(new Coord(row, col));
                        }
                        numRows++;
                     }                                      
                  }                                         
               }
         }
      }
   
      String loc = player.getLocationType().toLowerCase();
      ArrayList<Coord>ans = new ArrayList();
      for(Coord p:temp)
      {
         int r = p.getRow();
         int c = p.getCol();
         if((loc.contains("sewer") || loc.contains("mine") || loc.contains("cave")) && TerrainBuilder.onWater((map.get(mapIndex)), r, c))
         {
         //we want to see where the water is, even if it is blocked
         }
         else
            ans.add(p);
      }
      return ans;
   }

   public static String ordinalNum(int x)
   {
      x = x % 10;
      if(x==1)
         return x+"st";
      if(x==2)
         return x+"nd";
      if(x==3)
         return x+"rd";
      return x+"th";
   }


  
   
   public static final String [][] readMePages = {
      /*page 1*/
     {
      "Cultima: an open world RPG in tribute to Ultima games:",
      "The world, names, and music are all randomly generated.",  
      "     Some will on occassion take the form of something",
      "     provocative or obscene. Any resemblance to shapes or ",
      "     words that cause offense are completely coincidental.",
      "",
      "Controls:", 
      "     -,+  to change screen size", 
      "     arrows to move or scroll chapters in Journal",
      "     SHIFT  to constant movement in last direction",
      "     I or J to view inventory or journal",
      "     M  to view map of visited locations",
      "     W to wait a turn and pass the time",
      "     ESC  to exit current menu",
      "",
      "Assigning quick keys from Inventory:", 
      "SHIFT-F1 to F4: assign key to switch to current weapon", 
      "F1 to F4 will quick switch to weapon assigned to that key",
      "SHIFT-F5 to F8: assign key to cast selected spell",
      "F5 to F8 will call the spell assigned to that key",  
      "SHIFT-0 to 9: assign key to take current potion",
      "0-9 will take the potion type assigned to that key",
     },
      /*page 2*/
     {
      "Various options will become available depending on your",
      "alignment and proximity to an object in the game.",  
      "You can only attack a character if you are horizontally",
      "        or vertically aligned with that characer and are",
      "        within range of your currently equipped weapon.",
      "Situational Controls:", 
      "A    to Attack, cast Advance spell or set a trap",
      "B    to Break down a wall/door (with hammer/axe)",
      "C    to Cast a spell",
      "D    to Drink a potion",
      "E    to Examine a character or object", 
      "F    to start a Fire",
      "K,L to Keep/Loot armor/weapon on corpse",
      "R    to Recall whistle for a dog",
      "P    to Pick a lock or disarm a trap",
      "S    to Sleep or set camp",
      "S    to Steal an item or pickpocket a character",
      "T    to Talk to a character",
      "W    to Wait for a while (1 hour in-game time)",
      "Z    alternate attack (for select weapons)",  
      "[,]  to swap armor/weapon with corpse",  
      "CTRL to toggle guard up (defense)/down (interacting)",
     },
      /*page 3*/
     {   
         "Character stats:",
         "Might - guides melee combat in attack and defense.",
         "Mind - controls capacity and quality of magic spells.",
         "Agility - guides ranged combat in attack and defense", 
         "     and nimbleness in picking locks and avoiding traps.",
         "Awareness - skill in noticing the world and creatures", 
         "     around you, from accurate mapping and knowledge",
         "     of others skills to trap and secret detection.",
         "",
         "On magic:",
         "Magic can be crafted in 4 forms:",
         "General spells can be learned from a book to be called",
         "     whenever the player has enough manna.",
         "Some spells are instant, while others last over a duration.",
         "Directed and aimed spells (mostly combative) require",
         "     a staff to be learned and wielded.",
         "Once a player buys a magic staff, they are bound to it and",
         "     can not discard it, taking a permanent weapons slot.",
         "Instant magic can be performed with potions.",
         "Potions are one time use, but you can hold many.",
         "Many magic items can be found and used by any player",
         "     and their effect is constant."
     },
      /*page 4*/
     {
         "On morality:",
         "Reputation guides whether or not characters will",
         "        talk to you or aid you in the game.",
         "Reputation is based on what others are witness to.",
         "",
         "Good deeds with witness add to a reputation:",
         "     -giving to the poor or donating to a temple",
         "     -finding lost people in caves or mines",
         "     -rescuing prisoners in dungeons or monster lairs",
         "     -completing non-assassination missions, like:",
         "           retrieving an item, delivering messages",
         "           clearing a location of monsters, etc",
         "     -slaying a monster that wanders into a village",
         "     -defeating a rival army with a local militia",
         "Wicked deeds with witness subtract from reputation:",
         "     -vandalism: breaking walls, arson, picking locks",
         "     -pickpocketing villagers, theft of city chests",
         "     -getting caught with more than one spouse",
         "     -abuse of a dog companion or trained horse",
         "     -assault or murder of villagers, guards or royalty",
         "     -completing assassination missions",
         "      (bounty missions given by a king are neutral)"
     },
     /*page 5*/
     {
        "A wicked player can gain wealth and experience faster", 
        "     with theft and murder but get less aid from others.",
        "There are ways a wicked character can charm or impress",
        "     others to help when they wouldn't otherwise.", 
        "A reputed heroic player will find more guidance from",
        "     others such as filling out your map, and for famed",
        "     heroes, get discounts on training and shoppe items.",
        "The witnessed murdering of people will build a bounty",
        "     increasing chances of assassins seeking you out.",
        "The murdering of noble characters or teachers that you",
        "     studied with can anger the gods and result in a curse.",
        "Accepting assassination missions against others can end",
        "     peacefully by telling the target about the bounty.",
        "",
        "All character roles and many game mechanics can be",
        "     learned by talking to the characters in game.",
        "The main character roles can be discovered by first",
        "     finding and conversing with a town's barkeep",
        "Weapon/spell information can be learned from shop",
        "     keepers of armories and magic shops.",
        "Monster information and strategies for fighting them",
        "     can be learned from local swordsmen.",
     },
     /*page 6*/
     {
        "In conversation, single letters will auto-complete:", 
        "N => Name, to ask a character his/her name",
        "J  => Job, to ask a character of their job",
        "H => Home, to ask a character about their home",
        "T => Trade, to ask a shopkeep to trade goods for gold",
        "G => Gold, to give for services/goods/charity",
        "M => Mission if the NPC has one to give",
        "M => Map if the NPC can fill out part of your map",
        "M => Message if the NPC has one for you to deliver",
        "B => Bounty, if the NPC has a price on their head",
        "B => Bribe, to tempt a guard with a bribe of gold",
        "C => Cure, to ask a healer to cure you of poison",
        "S => Spell, to ask a wiseman to teach a new spell",
        "L => Lair, to ask swordsman the location of monster lairs",
        "R => Ration, to buy rations or give rations to a begger",
        "Dog command shortcuts:",
        "S => Stay, to make the dog hold its location",
        "C => Come, to make the dog return to you",
        "A => Attack, to hunt game, distract enemies or guard",
        "G => Go, to make the dog leave your side",
        "T => Treat, to give the dog a ration",
        "'R' will whistle to recall your dog to you."
     },
     /*page 7*/
     {
        "If a character has an item you want, type the name of",
        "     the object, like a LOCKPICK to be bought or sold",
        "The UP-ARROW to repeat last sentence, DOWN-ARROW",
        "     or ESC will leave conversation.",
        "LEFT/RIGHT to scroll through commonly used words.",
        "Most key words that can be followed up in conversation",
        "     will highlight in blue text and echoed when you",
        "     press the RIGHT-ARROW.",
        "All important information learned about monsters, arms,",
        "     townsfolk, spells, locations and riddles are",
        "     automatically logged in the player's journal.",
        "The journal is a game manual that builds as you play.",
        "",
        "Certain character types can have different roles",
        "     depending on their locations in the game world:",
        "     -city, village, castle, temple, hermit's hut, etc",
        "",
        "Some monster types are ubiquitous, but others only",
        "     appear in certain locations, time or seasons.",
        "",
        "There is an end-game narrative that can be achieved if",
        "     the player seeks it out."
     },
      /*page 8*/
     {
        "FAQ:",
        "Why do I die so often or so quickly?",
        "     The game world and difficulty does not scale",
        "     to your level. You must play very carefully at", 
        "     the start to be able to survive.",
        "I started on an island I can't get off!",
        "     Sorry: that's unlucky randomness. Rebuild another",
        "     world by hitting the B key from the main menu.",
        "Is there an auto-save?",
        "     Yes, the game saves your progress each time you",
        "     enter a location healthy and when you complete a",
        "     mission. Regardless, save often by going to the",
        "     main menu (ESC), then (S) to save.",
        "Why is the game so existentialist?",
        "     The central design decisions of the game are",
        "     based on things we can't predict: a random number",
        "     generator. The main theme of the game is a matter",
        "     of doing the best with what you are given. The",
        "     game world has the potential of being incredibly",
        "     unfair or surprisingly generous at various times.",
        "     Just like real life, but with magic and monsters.",
        "Coding by Douglas R Oberle, 2024"
     },
     /*page 9*/
     {
        "Main theme: Joaquin Rodrigo, John McReynolds arr.",
        "",
        "Easter-egg music and lyric snippets:",
        "     Stock Aitken Waterman",
        "     Terry Jones, Michael Palin, Fred Tomlinson",
        "     Michael McKean, Christopher Guest, Harry Shearer",
        "",
        "Design mechanics inspired by the works of:",
        "     Origin Systems, Richard Garriot, Roe Adams",
        "",
        "Graphics tile set/inspired by the works of:",
        "     Denis Loubet, Patricia Hunter, Marcha Meuse",
        "",
        "This is a work of tribute to the artists listed above",
        "and is not to be used for resale or profit."
     }
   };
   
   //journalChapterIndex to change pages
   public static void readMe(Graphics g)	
   {
      Font readable = new Font("Serif", Font.BOLD | Font.ITALIC, SIZE);
      Font readableP = new Font("Serif", Font.PLAIN | Font.ITALIC, SIZE);  
      Font title = new Font("Old English Text MT", Font.BOLD, SIZE);
      Font oldP = new Font("Old English Text MT", Font.PLAIN, SIZE);
      g.setColor(new Color(1, 1, 20));
      g.fillRect(0,0, SIZE*SCREEN_SIZE, SIZE*SCREEN_SIZE);
      int x = SIZE/2;
      int y = SIZE;  
      g.setFont(readableP);
      g.setColor(Color.orange);
      for(String line:readMePages[journalChapterIndex])
      {
         g.drawString(line, x, y);
         y += SIZE;
      }
      g.setColor(Color.red);
      g.drawString("Page "+(journalChapterIndex+1)+" of "+(readMePages.length), x, y);
   
      //RIGHT SIDE OF SCREEN
      x = SIZE * SCREEN_SIZE;
      y = (SIZE); 
      g.setFont(title);
      g.setColor(new Color(0, 0, 127));
      if(CultimaPanel.player.getBody() > 0)
         g.drawString("(S)ave your progress.", x, y);
      else
         g.drawString("(L)oad your last save.", x, y);
      y += SIZE;
      g.drawString("(Q)uit and save the game.", x, y);
      y += SIZE;
      g.drawString("(B)uild a new world.", x, y);
      y += SIZE;
      g.drawString("(C)reate a new character.", x, y);
      y += SIZE;
   
      y = (SIZE * SCREEN_SIZE) - (SIZE * 7);
      g.setColor(new Color(0, 0, 0));
      g.setFont(readableP);
      y += SIZE;
      g.setFont(readableP);
      g.drawString("(LEFT-RIGHT)", x, y);
      g.setFont(new Font("Old English Text MT", Font.PLAIN, SIZE));
      g.drawString("                          to flip page", x, y);
      y += SIZE;
      g.setFont(readableP);
      g.drawString("(M)ap  (I)nventory  (J)ournal(ESC)", x, y);
   //show scrolling message array
      y = (SIZE * SCREEN_SIZE) - (SIZE * 4);
      if(CultimaPanel.talkSel && CultimaPanel.selected!=null)
         g.setColor(new Color(30, 255, 30));
      else
         g.setColor(new Color(100, 100, 255));
   
      g.fillRect(x, y-SIZE ,  (SIZE * SCREEN_SIZE), (SIZE * 5));
      g.setColor(new Color(60, 0, 0));
      g.setFont(readable);
      y = y - (SIZE/4);
      for(int i=0; i<CultimaPanel.message.length; i++)
      {
         String sentence =CultimaPanel.message[i];
         if(i == CultimaPanel.message.length-1 && CultimaPanel.typed.length() > 0)
            sentence = "~"+CultimaPanel.typed;
         String temp = sentence.toLowerCase();
         if(!temp.startsWith("~"))           //dialogue with NPCs will start with a ~
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
               g.setColor(new Color(60, 0, 0));
               String tempWord = new String(words[j]);
               if(tempWord.startsWith("~"))
                  tempWord = tempWord.substring(1);
               if(tempWord.length()>0 && tempWord.equals(tempWord.toUpperCase()) && !tempWord.equals("~I") && !tempWord.equals("~A") && !tempWord.equals("I") && !tempWord.equals("A"))
               {
                  g.setColor(new Color(10, 10, 100));
                  if(j==0 || tempWord.contains("GARRIOTT")  || tempWord.contains("SKARA") || tempWord.contains("CAERBANNOG") || tempWord.contains("CULTIMA")) 
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
}
