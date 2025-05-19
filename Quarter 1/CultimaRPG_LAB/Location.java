import java.util.ArrayList;

public class Location implements Comparable
{
   private String name;
   private int fromMapIndex, toMapIndex;
   private int row, col;
   private Terrain terrain;
   private Teleporter teleporter;
   private ArrayList<Coord> monsterFreq;  //row is the monster index, col is the number of monsters of that type
   private ArrayList<Coord> riddlePoints; //row and col of critical locations for riddles (like 3-doors and 3-wells riddles)

   public Location(String n, int r, int c, int mi, Terrain t, Teleporter tele)
   {
      name = n;
      fromMapIndex = mi;
      toMapIndex = -1;
      row = r;
      col = c;
      terrain = t;
      teleporter = tele;
      monsterFreq = new ArrayList<Coord>();
      riddlePoints = new ArrayList<Coord>();
   }
   
   public int compareTo(Object other)
   {
      Location that = (Location)(other);
      int dist = (int)(Display.wrapDistance(this.row, this.col, that.row, that.col));
      if(this.row == that.row && this.col == that.col)
         return 0;
      if(this.row < that.row)
         return -1*dist;
      if(this.col < that.col)
         return -1*dist;
      return dist;
   }
   
   public ArrayList<Coord> getMonsterFreq()
   {
      return monsterFreq;
   }
   
   public void setMonsterFreq(ArrayList<Coord> mf)
   {
      monsterFreq = mf;
   }
   
   public ArrayList<Coord> getRiddlePoints()
   {
      return riddlePoints;
   }
   
   public void setRiddlePoints(ArrayList<Coord> rp)
   {
      riddlePoints = rp;
   }
   
   public void clearRiddlePoints()
   {
      riddlePoints.clear();
   }
   
   //returns the total number of monsters
   public int getMonsterTotal()
   {
      int sum=0;
      for(Coord p:monsterFreq)
         sum += p.getCol();
      return sum;
   }
   
   //returns the dominant monster type among its monster frequencies, -1 if there are none
   public byte getDominantMonsterType()
   {
      int max=0;
      int monsterType = -1;
      for(Coord p:monsterFreq)
      {
         if (p.getCol() > max)
         {
            max = p.getCol();
            monsterType = p.getRow();
         }
      }
      return (byte)monsterType;
   }
   
   //return the probability that the monster in monsterFreq with charIndex mi will spawn out of the total # monsters
   public int getMonsterProb(int mi)
   {
      int total = getMonsterTotal();
      if(total > 0)
         for(Coord p:monsterFreq)
            if(p.getRow()==mi)
               return (int)((p.getCol() / (double)total)*100);
      return 0;
   }
   
   public void addMonster(byte mi)
   {
      for(Coord p:monsterFreq)
      {
         if(p.getRow()==mi)
            p.setLocation(mi, p.getCol()+1);
         return;
      }
      monsterFreq.add(new Coord(mi, 1));
   }
   
   public boolean removeMonster(byte mi)
   {
      for(int i=0; i<monsterFreq.size(); i++)
      {
         Coord p = monsterFreq.get(i);
         if(p.getRow()==mi || (NPC.isBrigand((byte)(p.getRow())) && NPC.isBrigand(mi)))
         {
            if(p.getCol() == 1)
            {
               monsterFreq.remove(i);
               return true;
            }
            p.setLocation(mi, p.getCol()-1);
            return true;
         }
      }
      return false;
   }
   
   public void clearMonsters()
   {
      monsterFreq.clear();
   }
   
   public String getName()
   {
      return name;
   }
      
   public int getMapIndex()
   {
      return toMapIndex;
   }
   
   public void setMapIndex(int mi)
   {
      toMapIndex = mi;
   }
   
   public int getFromMapIndex()
   {
      return fromMapIndex;
   }
   
   public void setFromMapIndex(int mi)
   {
      fromMapIndex = mi;
   }

   public int getToMapIndex()
   {
      return toMapIndex;
   }
      
   public int getRow()
   {
      return row;
   }
   
   public int getCol()
   {
      return col;
   }
   
   public Terrain getTerrain()
   {
      return terrain;
   }
   
   public Teleporter getTeleporter()
   {
      return teleporter;
   }

   public void setTeleporter(Teleporter tele)
   {
      teleporter = tele;
   }
   
   public String getLocType()
   {
      return teleporter.getLocType();
   }

   @Override
   public String toString()
   {
      return name+":("+row+","+col+")";
   }
   
   public String toString2()
   {
      return name+" at "+col+" East and "+row+" South";
   }

}