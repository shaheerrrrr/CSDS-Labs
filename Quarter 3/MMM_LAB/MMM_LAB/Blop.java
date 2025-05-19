public class Blop extends Monster
{
   public Blop(int r, int c, String[][][] image)
   {
      super("The-Blop", r, c, image, 15, 20, 1, 0, 90, "SLUDGE", 50);
      //super ARGS:  name, row, col, image collection, animation delay, stomp power, speed penalty, reload time, walk damage, projectileType, burnDamage
      setIsSwimmer(true);
      setCanSplit(true);
      setImperviousToBullets(true);
      setSlimeTrail(true);
      setCanEatAll(true);
   }

   public Blop(String n, int r, int c, String[][][] image, int sp, int spp, int rt)
   {
      super (n, r, c, image, 15, sp, spp, rt, 90, "SLUDGE", 5);
      setIsSwimmer(true);
      setCanSplit(true);
      setImperviousToBullets(true);
      setSlimeTrail(true);
      setCanEatAll(true);
   }

   public void grabUnit(String name)
   {
      super.setClawContents(name);
   }

   @Override
   public boolean canGrabUnit(String name)
   {
      return true;
   }

   @Override
   public void eatUnit()
   {
      String[] contents = super.getClawContents();
      int index = -1;		//index of claw contents that are full
      if(!contents[0].equals("empty"))
         index = 0;
      else
      if(!contents[1].equals("empty"))
         index = 1;
      if(index >= 0)
      {
         setHealth(getHealth() + (int) (Math.random() * 6 + 5));
         setHunger(getHunger() - 1);
         clearClawContents();
      }
   }

   public String reloadingMessage()
   {
      return "Generating Glop!";
   }
}
