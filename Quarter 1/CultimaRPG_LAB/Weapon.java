import java.util.ArrayList;

public class Weapon extends Item
{
   private String effect;        //does it curse, poison, set on fire, etc

   private int minDamage;        //the damage done will be somewhere between minDamage and maxDamage inclusive
   private int maxDamage;

   private int range;            //the distance from the target that we can strike from

   private int[] statModifier;   //3 element list of how stats of [might, mind and agility] will be modified by holding the weapon. Can be positive (buff) or negative (debuff)

   private byte imageIndex;      //which image do we use for the weapon (in Player.java)

   private int reloadFrames;     //the number of frames needed to reload between attacking with this weapon

   private int mannaCost;        //if this is a magic weapon
     
   public static final int CROSSBOW_RANGE = 5;
   public static final int BOW_RANGE = 8;
   public static final int LONGBOW_RANGE = 10;

  //               name       effect  minDamage maxDamage  range  statModifier value imageIndex reloadTime mannaCost
   public Weapon(String n, String eff, int min, int max, int rng, int[]statM, int v, byte ii, int reload, int mc)
   {
      super(n, v);
      reloadFrames = 0;
      effect = eff;
      minDamage = min;
      maxDamage = max;
      range = rng;
      if(statM != null && statM.length == 3)
         statModifier = statM;
      else
         statModifier = new int[3];
      imageIndex = ii;
      reloadFrames = reload;
      mannaCost = mc;
   }
   
   //             name      effect     minDamage maxDamage range  statModifier value imageIndex reloadTime mannaCost description
   public Weapon(String n, String eff, int min, int max, int rng, int[]statM, int v, byte ii, int reload, int mc, String d)
   {
      super(n, v, d);
      reloadFrames = 0;
      effect = eff;
      minDamage = min;
      maxDamage = max;
      range = rng;
      if(statM != null && statM.length == 3)
         statModifier = statM;
      else
         statModifier = new int[3];
      imageIndex = ii;
      reloadFrames = reload;
      mannaCost = mc;
   }
   
   //              name
   public Weapon(String n)
   {
      super(n, 0);
      Weapon temp = getWeaponWithName(n);
      if(temp != null)
      {
         this.setValue(temp.getValue());
         this.setDescription(temp.getDescription());
         statModifier = temp.statModifier;
         reloadFrames = 0;
         effect = temp.effect;
         minDamage = temp.minDamage;
         maxDamage = temp.maxDamage;
         range = temp.range;
         imageIndex = temp.imageIndex;
         reloadFrames = temp.reloadFrames;
         mannaCost = temp.mannaCost;
      }
      else
      {
         this.setName("!!!UNKNOWN!!!");
      }  
   }

   //given the weapon name, returns the alternate fire weapon if it has one
   public static Weapon getAltFire(String name)
   {
      if(isSpell(name))
      {
         if(CultimaPanel.player.hasItem("Ohnyalei"))
            return getOhnyalei();
         if(CultimaPanel.player.hasItem("Magestaff"))
            return getMageStaff();
         return getStaff();     
      }
      else if(isHalberd(name))
      {
         Weapon halSpin = getWeaponWithName(name);
         halSpin.setReloadFrames(halSpin.getReloadFrames()*10);
         halSpin.setMinDamage(1);
         halSpin.setMaxDamage(1);
         return  halSpin;
      }
      else if(isSwordAndShield(name))
      {
         if(name.contains("mirror"))
            return getShieldBashMirror();
         if(name.contains("royal"))
            return getShieldBashRoyal();
         return getShieldBashBuckler();
      }
      else if(isDagger(name))
      {  //thrown daggers do more minimum damage but longer cooldown
         Weapon thrownDagger = getWeaponWithName(name);
         thrownDagger.setReloadFrames(thrownDagger.getReloadFrames()*10);
         thrownDagger.setRange(CROSSBOW_RANGE);
         thrownDagger.setMinDamage(thrownDagger.getMaxDamage()/2);
         return thrownDagger;
      }
      else if(isSpear(name))
      {
         Weapon thrownSpear = getWeaponWithName(name);
         thrownSpear.setReloadFrames(thrownSpear.getReloadFrames()*10);
         thrownSpear.setRange(CROSSBOW_RANGE);
         thrownSpear.setMinDamage(thrownSpear.getMaxDamage()/2);
         return thrownSpear;
      }
      else if (isTorch(name))
      {
         Weapon torchWave = getWeaponWithName(name);
         torchWave.setReloadFrames(torchWave.getReloadFrames()*10);
         torchWave.setRange(4);
         torchWave.setMinDamage(1);
         torchWave.setMaxDamage(1);
         return torchWave;
      }
      else if (isDualAxes(name))
      {
         Weapon dualAxeSpin = getWeaponWithName(name);
         dualAxeSpin.setReloadFrames(dualAxeSpin.getReloadFrames()*3);
         dualAxeSpin.setMinDamage(dualAxeSpin.getMinDamage()-5);
         dualAxeSpin.setMaxDamage(dualAxeSpin.getMaxDamage()-5);
         return dualAxeSpin;
      }
      else if(isDualSwords(name))
      {
         Weapon dualSwordSpin = getWeaponWithName(name);
         dualSwordSpin.setReloadFrames(dualSwordSpin.getReloadFrames()*3);
         dualSwordSpin.setMinDamage(dualSwordSpin.getMinDamage()-5);
         dualSwordSpin.setMaxDamage(dualSwordSpin.getMaxDamage()-5);
         return dualSwordSpin;
      }
      else if (isGreatAxe(name))
      {
         Weapon greatAxeSwipe = getWeaponWithName(name);
         greatAxeSwipe.setReloadFrames(greatAxeSwipe.getReloadFrames()*3);
         greatAxeSwipe.setMinDamage(greatAxeSwipe.getMinDamage()-10);
         greatAxeSwipe.setMaxDamage(greatAxeSwipe.getMaxDamage()-10);
         return greatAxeSwipe;
      }
      else if(name.equalsIgnoreCase("Bow-of-Karna"))
      {  //fires two arrows - more damage but longer cooldown and twice the ammo cost
         return getBowOfKarnaDoubleShot();
      }
      else if(isSword(name))
      {  //heavy blow with a sword will increase the critical hit probability in Player, but twice the cooldown needed
         Weapon swordHeavyBlow = getWeaponWithName(name);
         swordHeavyBlow.setReloadFrames(swordHeavyBlow.getReloadFrames()*10);
         return swordHeavyBlow;
      }
      else if(isHammer(name))
      {  //heavy blow with a hammer will increase the critical hit probability in Player, but twice the cooldown needed
         Weapon hammerHeavyBlow = getWeaponWithName(name);
         hammerHeavyBlow.setReloadFrames(hammerHeavyBlow.getReloadFrames()*10);
         return hammerHeavyBlow;
      }
      return null;
   }
   
   //return a short description to add to the HUD for the alternate attack type
   //i.e.  instead of (Z)attack, it might say (Z)Throw for a dagger or spear, or (Z)Bash for a sword and buckler shield bash
   public static String getAltTerm(String name)
   {
      if(isSpell(name))
      {
         return "Strike";
      }
      if(isHalberd(name))
      {
         return "Spin";
      }
      if(isDagger(name) || isSpear(name) || isThrown(name))
      {
         return "Throw";
      }
      if(isTorch(name))
      {
         return "Wave";
      }
      if(isSwordAndShield(name))
      {
         return "Bash";
      }
      if (isDual(name))
      {
         return "Spin";
      }
      if (isGreatAxe(name))
      {
         return "Swipe";
      }
      if(isLongStaff(name))
      {
         return "Vault";
      }
      if(name.equalsIgnoreCase("Bow-of-Karna"))
      {
         return "Double";
      }
      if(isSword(name) || isHammer(name))
      {
         return "Heavy";
      }
      return null;
   }

   //given a weapon name in uppercase,return its weapon index
   public static byte getWeaponIndexFromName(String name)
   {
      name = name.trim().toUpperCase();
      if(name.equals("NONE")) 
         return 0;
      if(name.equals("TORCH"))
         return 1;  
      if(name.equals("STAFF")) 
         return 2;
      if(name.equals("LONGSTAFF")) 
         return 3;
      if(name.equals("SPEAR")) 
         return 4;
      if(name.equals("BOW")) 
         return 5;
      if(name.equals("CROSSBOW")) 
         return 6;
      if(name.equals("AXE")) 
         return 7;
      if(name.equals("DUALAXE")) 
         return 8;
      if(name.equals("HAMMER")) 
         return 9;
      if(name.equals("DAGGER")) 
         return 10;
      if(name.equals("SABER")) 
         return 11;
      if(name.equals("SWORDSHIELD")) 
         return 12;
      if(name.equals("DUALBLADES")) 
         return 13;
      if(name.equals("THROWN")) 
         return 14;
      if(name.equals("LUTE")) 
         return 15;
      return -1;
   }
   
   //returns true if the weapon has metal parts
   public boolean isMetal()
   {
      return (imageIndex == Player.SPEAR || imageIndex == Player.CROSSBOW || imageIndex == Player.AXE 
           || imageIndex == Player.DUALAXE || imageIndex == Player.HAMMER || imageIndex == Player.DAGGER 
           || imageIndex == Player.SABER || imageIndex == Player.SWORDSHIELD || imageIndex == Player.DUAL);
   }
   
   //returns true if the weapon is mostly made of wood so it can be damaged by fire if in a barrel
   public boolean hasWood()
   {
      return (imageIndex == Player.SPEAR || imageIndex == Player.CROSSBOW || imageIndex == Player.BOW || imageIndex == Player.TORCH
           || imageIndex == Player.LUTE || imageIndex == Player.STAFF || imageIndex == Player.LONGSTAFF);
   }
   
   //given a weapon index, returns the weapon category name for the Journal
   public static String getWeaponType(byte imageIndex)
   {
      if(imageIndex == Player.NONE) 
         return "Armor:";
      if(imageIndex == Player.TORCH) 
         return "Torch:";
      if(imageIndex == Player.STAFF) 
         return "Magic Staff:";
      if(imageIndex == Player.LONGSTAFF) 
         return "Longstaff:";
      if(imageIndex == Player.SPEAR) 
         return "Spears:";
      if(imageIndex == Player.BOW) 
         return "Bows:";
      if(imageIndex == Player.CROSSBOW) 
         return "Crossbows:";
      if(imageIndex == Player.AXE)
         return "Axes:";
      if(imageIndex == Player.DUALAXE)
         return "Dual-axes:";   
      if(imageIndex == Player.HAMMER) 
         return "Hammers:";
      if(imageIndex == Player.DAGGER) 
         return "Daggers:";
      if(imageIndex == Player.SABER) 
         return "Swords:";
      if(imageIndex == Player.SWORDSHIELD) 
         return "Sword and Shield:";
      if(imageIndex == Player.DUAL) 
         return "Dual blades:";
      if(imageIndex == Player.THROWN) 
         return "Thrown objects:";    
      if(imageIndex == Player.LUTE) 
         return "Lute:";
      return "Misc notes:";
   }
   
   //given a weapon index, returns the weapon category: used for the Player toString
   public static String getWeaponCategory(byte imageIndex)
   {
      if(imageIndex == Player.NONE) 
         return "unarmed";
      if(imageIndex == Player.TORCH) 
         return "torch";
      if(imageIndex == Player.STAFF) 
         return "magic staff";
      if(imageIndex == Player.LONGSTAFF) 
         return "longstaff";
      if(imageIndex == Player.SPEAR) 
         return "spears";
      if(imageIndex == Player.BOW) 
         return "bows";
      if(imageIndex == Player.CROSSBOW) 
         return "crossbows";
      if(imageIndex == Player.AXE)
         return "axes";
      if(imageIndex == Player.DUALAXE)
         return "dual-axes";   
      if(imageIndex == Player.HAMMER) 
         return "hammers";
      if(imageIndex == Player.DAGGER) 
         return "daggers";
      if(imageIndex == Player.SABER) 
         return "swords";
      if(imageIndex == Player.SWORDSHIELD) 
         return "sword and shield";
      if(imageIndex == Player.DUAL) 
         return "dual blades";
      if(imageIndex == Player.THROWN) 
         return "thrown objects";
      if(imageIndex == Player.LUTE) 
         return "lute";
      return "unknown";
   }
   
   //return a description of a weapon class for the journal
   public static String getTypeDescription(byte imageIndex)
   {
      if(imageIndex == Player.NONE) 
         return "Strike with fists, or bite with vampyric or wolfen fangs";
      if(imageIndex == Player.TORCH) 
         return "Light darkened areas, start fires, or fend off smaller enemies. Wave around to intimidate";
      if(imageIndex == Player.STAFF) 
         return "A magic staff that can cast directed spells or strike with a spears reach";
      if(imageIndex == Player.LONGSTAFF) 
         return "An offensive staff for acrobatic warriors, used to quickly strike foes with a spears reach or vault atop city walls and counters";
      if(imageIndex == Player.SPEAR) 
         return "The weapon of a city guard, may be thrust or thrown at foes";
      if(imageIndex == Player.BOW) 
         return "Good for dextorous warriors to strike enemies at long range";
      if(imageIndex == Player.CROSSBOW) 
         return "Throws a heavier bolt than hunting bows, does more damage at the price of a shorter range";
      if(imageIndex == Player.AXE)
         return "Wielded by strong warriors to strike at beasts or chop through wooden doors. Can chop at one enemy or swipe at many";
      if(imageIndex == Player.DUALAXE)
         return "Two small axes, one for each hand. Good for battle, knocking through wooden doors. Spinning attacks can hit many foes in one strike";   
      if(imageIndex == Player.HAMMER) 
         return "For weapons and mining, held by those of great strength. Hammers can knock down doors as easily as foes";
      if(imageIndex == Player.DAGGER) 
         return "The weapon for those that are stealty or agile. Can be thrust or thrown at the range of crossbows";
      if(imageIndex == Player.SABER) 
         return "A two-handed sword for the strong and agile, used to chop down foes with ease and block melee attacks";
      if(imageIndex == Player.SWORDSHIELD) 
         return "A one-handed sword and shield for extra protection. Shield-bash to knock back and stun foes";
      if(imageIndex == Player.DUAL) 
         return "Two one-handed swords, one in each hand. Dextorous warriors can do a spinning attack to hit many foes at once";
      if(imageIndex == Player.THROWN) 
         return "An item in hand can make for a stinging projectile against small and weak foes, and ammo is plentiful and all around thee";
      if(imageIndex == Player.LUTE) 
         return "A magic lute that can inspire those close to dance, including some creatures insuring safe passage";
      return "unknown";
   }

   //return the keys to attack/alternate attack for the journal
   public static String getKeys(byte imageIndex, String weapName)
   {
      weapName = weapName.toLowerCase();
      if(imageIndex == Player.NONE) 
      {                                   //maybe alternate fire to grapple to keep close
         if(CultimaPanel.player.isVampire() || CultimaPanel.player.isWolfForm())
         {
            return "(A) bite";
         }
         if(weapName.contains("trap"))
         {
            return "(A) lay trap";   
         }
         return "(A) punch";
      }
      if(imageIndex == Player.TORCH) 
      {
         return "(A) swipe, (Z) wave";
      }
      if(imageIndex == Player.STAFF) 
      {
         if(isSpell(CultimaPanel.player.getWeapon().getName()))
         {
            return "(A) cast spell, (Z) strike";
         }
         return "(A) strike";
      }  
      if(imageIndex == Player.LONGSTAFF) 
      {
         return "(A) strike, (Z) vault";       
      }
      if(imageIndex == Player.SPEAR)
      { 
         if(isSpear(weapName))
         {
            return "(A) thrust, (Z) throw";
         }
         return "(A) thrust, (Z) spin";    //halberds
      }
      if(imageIndex == Player.BOW)
      {              
         if(weapName!=null && weapName.contains("karna") && CultimaPanel.player.getNumArrows() >= 2)
         {
            return "(A) shoot, (Z) double";
         }
         return "(A) shoot";
      }
      if(imageIndex == Player.CROSSBOW)    
         return "(A) shoot";
      if(imageIndex == Player.AXE)
         return "(A) chop, (Z) swipe";
      if(imageIndex == Player.DUALAXE)
         return "(A) chop, (Z) spin";   
      if(imageIndex == Player.HAMMER) 
         return "(A) bash, (Z) heavy";
      if(imageIndex == Player.DAGGER) 
         return "(A) strike, (Z) throw";
      if(imageIndex == Player.SABER) 
         return "(A) strike, (Z) heavy";
      if(imageIndex == Player.SWORDSHIELD) 
         return "(A) strike, (Z) shield bash";
      if(imageIndex == Player.DUAL) 
         return "(A) slash, (Z) spin";
      if(imageIndex == Player.THROWN) 
         return "(A) throw";    
      if(imageIndex == Player.LUTE) 
         return "(A) strike";
      return "unknown";
   }
   
   public int getMannaCost()
   {
      return mannaCost;
   }

   public void setMannaCost(int mc)
   {
      mannaCost = mc;
   }

   public int getReloadFrames()
   {
      return reloadFrames;
   }

   public void setReloadFrames(int rf)
   {
      reloadFrames = rf;
   }

   public String getEffect()
   {
      return effect;
   }

   public int getMinDamage()
   {
      return minDamage;
   }
   
   public void setMinDamage(int r)
   {
      minDamage = r;
   }

   public int getMaxDamage()
   {
      return maxDamage;
   }
   
   public void setMaxDamage(int r)
   {
      maxDamage = r;
   }

   public int getRange()
   {
      return range;
   }
   
   public void setRange(int r)
   {
      range = r;
   }

   //[0]-might, [1]-mind, [2]-agility   
   public int[] statModifier()
   {
      return statModifier;
   }

   public void setStatMod(int [] sm)
   {
      statModifier = sm;
   }

   public byte getImageIndex()
   {
      return imageIndex;
   }

   public static Weapon getRandomWeapon()
   {
      ArrayList<Weapon> weapons = new ArrayList<Weapon>();
      weapons.add(getAxe());  
      weapons.add(getMirroredAxe());    
      weapons.add(getDualAxe());
      weapons.add(getHammer());     
      weapons.add(getSpikedHammer());
      weapons.add(getExoticHammer());
      weapons.add(getSword());
      weapons.add(getSwordBuckler());  
      weapons.add(getSwordMirrorshield());   
      weapons.add(getBlessedSword());  
      weapons.add(getShortSwords());   
      weapons.add(getBow());  
      weapons.add(getCrossbow()); 
      weapons.add(getSoulCrossbow());
      weapons.add(getPoisonBoltcaster());  
      weapons.add(getBaneBoltcaster());  
      weapons.add(getLongbow());     
      weapons.add(getSpear());     
      weapons.add(getLongstaff());     
      weapons.add(getDagger());     
      weapons.add(getPoisonDagger()); 
      weapons.add(getFrostDagger()); 
      weapons.add(getMagmaDagger());    
      return weapons.get((int)(Math.random()*weapons.size()));
   }
   
   public static Weapon getRandomWeaponSimple()
   {
      ArrayList<Weapon> weapons = new ArrayList<Weapon>();
      weapons.add(getAxe());  
      weapons.add(getAxe());  
      weapons.add(getAxe());  
      weapons.add(getAxe());  
      weapons.add(getAxe());  
      weapons.add(getDualAxe());
      weapons.add(getHammer());    
      weapons.add(getDualAxe());
      weapons.add(getHammer());    
      weapons.add(getDualAxe());
      weapons.add(getHammer());    
      weapons.add(getDualAxe());
      weapons.add(getHammer());    
      weapons.add(getDualAxe());
      weapons.add(getHammer());     
      weapons.add(getSpikedHammer());
      weapons.add(getSword());
      weapons.add(getSword());
      weapons.add(getSword());
      weapons.add(getSword());
      weapons.add(getSword());
      weapons.add(getSwordBuckler());  
      weapons.add(getShortSwords());   
      weapons.add(getBow());
      weapons.add(getBow());  
      weapons.add(getBow());  
      weapons.add(getBow());  
      weapons.add(getBow());    
      weapons.add(getCrossbow()); 
      weapons.add(getLongbow());     
      weapons.add(getSpear());     
      weapons.add(getLongstaff());     
      weapons.add(getDagger());     
      return weapons.get((int)(Math.random()*weapons.size()));
   }
   
   public static Weapon getRandomWeaponFull()
   {
      ArrayList<Weapon> weapons = new ArrayList<Weapon>();
      weapons.add(getAxe());    
      weapons.add(getMirroredAxe()); 
      weapons.add(getDualAxe());
      weapons.add(getVampyricAxes());
      weapons.add(getTorch());
      weapons.add(getToothedTorch());
      weapons.add(getSpikedClub());
      weapons.add(getGiantMace());
      weapons.add(getHammer());     
      weapons.add(getSpikedHammer());
      weapons.add(getBaneHammer());
      weapons.add(getExoticHammer());
      weapons.add(getSword());   
      weapons.add(getSwordBuckler()); 
      weapons.add(getSwordMirrorshield());     
      weapons.add(getShortSwords());       
      weapons.add(getBlessedSword());     
      weapons.add(getFlameblade());
      weapons.add(getDualblades());
      weapons.add(getSwordShield());
      weapons.add(getBow()); 
      weapons.add(getCrossbow());
      weapons.add(getSoulCrossbow());
      weapons.add(getPoisonBoltcaster()); 
      weapons.add(getBaneBoltcaster());          
      weapons.add(getLongbow()); 
      weapons.add(getFlamebow());    
      weapons.add(getSpear());  
      weapons.add(getJawTrap());
      weapons.add(getBrightHalberd());    
      weapons.add(getLongstaff()); 
      weapons.add(getIceStaff());    
      weapons.add(getSceptre());     
      weapons.add(getDagger());     
      weapons.add(getPoisonDagger());   
      weapons.add(getSoulDagger()); 
      weapons.add(getBaneDagger()); 
      weapons.add(getFrostDagger()); 
      weapons.add(getMagmaDagger()); 
      weapons.add(getLuteOfDestiny());           
      return weapons.get((int)(Math.random()*weapons.size()));
   }
   
   //post: returns a random legendary weapon
   public static Weapon getRandomLegendaryWeapon()
   {
      ArrayList<Weapon> weapons = new ArrayList<Weapon>();
      weapons.add(getGadaTorchmace());  
      weapons.add(getForsetisAxe());    
      weapons.add(getMjolnir());
      weapons.add(getExcalibur());     
      weapons.add(getBowOfKarna());
      weapons.add(getGungnir());
      weapons.add(getAmeNoNuhoko());
      weapons.add(getKhatvanga());  
      weapons.add(getCarnwennan()); 
      weapons.add(getHolyHandGrenade());  
      return weapons.get((int)(Math.random()*weapons.size()));
   }
   
   //post: returns a random legendary weapon that the player does not have
   public static Weapon getRandomUniqueLegendaryWeapon()
   {
      ArrayList<Weapon> weapons = new ArrayList<Weapon>();
      if(!CultimaPanel.player.hasWeapon(getGadaTorchmace().getName()))
         weapons.add(getGadaTorchmace()); 
      if(!CultimaPanel.player.hasWeapon(getForsetisAxe().getName())) 
         weapons.add(getForsetisAxe()); 
      if(!CultimaPanel.player.hasWeapon(getMjolnir().getName()))   
         weapons.add(getMjolnir());
      if(!CultimaPanel.player.hasWeapon(getExcalibur().getName()))
         weapons.add(getExcalibur());     
      if(!CultimaPanel.player.hasWeapon(getBowOfKarna().getName()))
         weapons.add(getBowOfKarna());
      if(!CultimaPanel.player.hasWeapon(getGungnir().getName()))
         weapons.add(getGungnir());
      if(!CultimaPanel.player.hasWeapon(getAmeNoNuhoko().getName()))
         weapons.add(getAmeNoNuhoko());
      if(!CultimaPanel.player.hasWeapon(getKhatvanga().getName()))
         weapons.add(getKhatvanga());  
      if(!CultimaPanel.player.hasWeapon(getCarnwennan().getName()))
         weapons.add(getCarnwennan());
      if(!CultimaPanel.player.hasWeapon(getOhnyalei().getName()))
         weapons.add(getOhnyalei());   
      if(!CultimaPanel.player.hasWeapon(getHolyHandGrenade().getName()))
         weapons.add(getHolyHandGrenade()); 
      if(weapons.size() == 0)
         return getRandomLegendaryWeapon();     
      return weapons.get((int)(Math.random()*weapons.size()));
   }
   
   public static Weapon getRandomSword()
   {
      ArrayList<Weapon> weapons = new ArrayList<Weapon>();
      weapons.add(getSword());
      weapons.add(getBrightSword());
      weapons.add(getSwiftSword());
      weapons.add(getSwordOfMight());
      weapons.add(getSwordOfMind());
      weapons.add(getBlessedSword());
      return weapons.get((int)(Math.random()*weapons.size()));
   }
    
   public static Weapon getNone()
   {
      int[]statM = {0, 0, 0};
      Weapon temp = new Weapon("none", "none", 1, 1, 1, statM, 0, Player.NONE, 0,0);
      return temp;
   }
    //**************TORCHES***********************************  
   public static Weapon getTorch()
   {
      int[]statM = {0, 0, 0};
      String d = "A basic torch to bring light where there is none and defend against smaller beasts. Can set both creature and structure ablaze and waved about to intimidate";
      Weapon temp = new Weapon("Torch", "fire", 1, 1, 1, statM, 4, Player.TORCH, (CultimaPanel.animDelay/2),0,d);
      return temp;
   }
   
   public static Weapon getToothedTorch()
   {
      int[]statM = {0, 0, -1};
      String d = "A heavy toothed mace wrapped with torchcloth, bringing light for thy exploing and a heavy blow in combat. Can be waved about to intimidate person and beast";
      Weapon temp = new Weapon("Toothed-torch", "fire", 30, 40, 1, statM, 2000, Player.TORCH, (CultimaPanel.animDelay/2),0,d);
      return temp;
   }  
   
   public static Weapon getToothedTorchOfMight()
   {
      int[]statM = {1, 0, 0};
      String d = "A ruby adorned toothed mace, cast with flame as a torch - supplemts thy strength when wielded. Waving about can intimidate man and beast of smaller variety";
      Weapon temp = new Weapon("Toothed-torch-o-might", "fire", 35, 40, 1, statM, 4000, Player.TORCH, (CultimaPanel.animDelay/2),0,d);
      return temp;
   }  
   
   public static Weapon getToothedTorchOfMind()
   {
      int[]statM = {0, 1, 0};
      String d = "A jade adorned toothed mace, cast with flame as a torch - supplemts thy spellcraft when wielded. Can be waved about to intimidate person and beast";
      Weapon temp = new Weapon("Toothed-torch-o-mind", "fire", 35, 40, 1, statM, 4000, Player.TORCH, (CultimaPanel.animDelay/2),0,d);
      return temp;
   }
   
   public static Weapon getSwiftToothedTorch()
   {
      int[]statM = {0, 0, 1};
      String d = "An azurite adorned toothed mace, cast with flame as a torch - supplemts thy agility when wielded. Can be waved about to intimidate person and beast";
      Weapon temp = new Weapon("Swift-toothed-torch", "fire", 35, 40, 1, statM, 4000, Player.TORCH, (CultimaPanel.animDelay/2),0,d);
      return temp;
   }
   
   public static Weapon getGadaTorchmace()
   {
      int[]statM = {2, 2, 2};
      String d = "A glowing, gem adorned toothed mace, cast with flame as a torch - power surges through thee when wielded. Can be waved about to intimidate person and beast";
      Weapon temp = new Weapon("Gada-torchmace", "fire", 40, 45, 1, statM, 10000, Player.TORCH, (CultimaPanel.animDelay/2),0,d);
      return temp;
   }
   
   //************************AXES************************************    
   public static Weapon getAxe()
   {
      int[]statM = {0, 0, -1};
      String d = "Heavy, two handed axe suitable for felling enemies and wooden doors";
      Weapon temp = new Weapon("Axe", "none", 50, 70, 1, statM, 90, Player.AXE, CultimaPanel.animDelay/2,0, d);
      return temp;
   }
   
   public static Weapon getFlameAxe()
   {
      int[]statM = {0, 0, -1};
      String d = "A flamejem adorned heavy axe suitable for felling enemies and wooden doors, cast in magic fire";
      Weapon temp = new Weapon("Flameaxe", "fire", 50, 70, 1, statM, 300, Player.AXE, CultimaPanel.animDelay/2,0, d);
      return temp;
   }
   
   public static Weapon getFrostAxe()
   {
      int[]statM = {0, 0, -1};
      String d = "Icejem adorned heavy axe suitable for felling enemies and wooden doors, cast in magic frost";
      Weapon temp = new Weapon("Frostaxe", "freeze", 50, 70, 1, statM, 300, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
  
   public static Weapon getAxeOfMight()
   {
      int[]statM = {1, 0, 0};
      String d = "A ruby adorned two handed axe suitable for felling enemies and wooden doors that supplements thy might";
      Weapon temp = new Weapon("Axe-o-might", "none", 55, 70, 1, statM, 200, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getAxeOfMind()
   {
      int[]statM = {0, 1, 0};
      String d = "A jade adorned two handed axe suitable for felling enemies and wooden doors that supplements thy spellcraft";
      Weapon temp = new Weapon("Axe-o-mind", "none", 55, 70, 1, statM, 200, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getSwiftAxe()
   {
      int[]statM = {0, 0, 1};
      String d = "Azurite adorned two handed axe suitable for felling enemies and wooden doors that supplements thy agility";
      Weapon temp = new Weapon("Swiftaxe", "none", 55, 70, 1, statM, 200, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getBrightAxe()
   {
      int[]statM = {0, 0, 0};
      String d = "A glowing two handed axe suitable for felling enemies and wooden doors that casts light where there is none";
      Weapon temp = new Weapon("Bright-axe", "none", 55, 70, 1, statM, 200, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getMirroredAxe()
   {
      int[]statM = {0, 0, 0};
      String d = "A fine two handed axe made of a strange reflective metal, good for felling enemies and wooden doors";
      Weapon temp = new Weapon("Mirrored-axe", "none", 55, 75, 1, statM, 3000, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getFlameMirrorAxe()
   {
      int[]statM = {0, 0, 0};
      String d = "A flamejem adorned two handed axe made of a strange reflective metal and cast in magic fire, good for felling beasts and wooden doors";
      Weapon temp = new Weapon("Flame-Mirroraxe", "fire", 55, 75, 1, statM, 6000, Player.AXE, CultimaPanel.animDelay/2,0, d);
      return temp;
   }
   
   public static Weapon getFrostMirrorAxe()
   {
      int[]statM = {0, 0, 0};
      String d = "Icejem adorned two handed axe made of a strange reflective metal and cast in magic frost, good for felling beasts and wooden doors";
      Weapon temp = new Weapon("Frost-Mirroraxe", "freeze", 55, 75, 1, statM, 6000, Player.AXE, CultimaPanel.animDelay/2,0, d);
      return temp;
   }
   
   public static Weapon getBrightMirrorAxe()
   {
      int[]statM = {0, 0, 0};
      String d = "A glowing two handed axe made of a strange reflective metal that casts light where there is none, good for felling beasts and wooden doors";
      Weapon temp = new Weapon("Bright-Mirroraxe", "none", 60, 75, 1, statM, 6000, Player.AXE, CultimaPanel.animDelay/2,0, d);
      return temp;
   }

   public static Weapon getMightMirrorAxe()
   {
      int[]statM = {1, 0, 0};
      String d = "A ruby adorned two handed axe made of a strange reflective metal that augments thy might, good for felling beasts and wooden doors";
      Weapon temp = new Weapon("Might-Mirroraxe", "none", 60, 75, 1, statM, 6000, Player.AXE, CultimaPanel.animDelay/2,0, d);
      return temp;
   }
   
   public static Weapon getMindMirrorAxe()
   {
      int[]statM = {0, 1, 0};
      String d = "A jade adorned two handed axe made of a strange reflective metal that augments thy spellcraft, good for felling beasts and wooden doors";
      Weapon temp = new Weapon("Mind-Mirroraxe", "none", 60, 75, 1, statM, 6000, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }

   public static Weapon getSwiftMirrorAxe()
   {
      int[]statM = {0, 0, 1};
      String d = "Azurite adorned two handed axe made of a strange reflective metal that augments thy agility, good for felling beasts and wooden doors";
      Weapon temp = new Weapon("Swift-Mirroraxe", "none", 60, 75, 1, statM, 6000, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getForsetisAxe()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing two handed axe that surges with awesome power, good for felling beasts and wooden doors";
      Weapon temp = new Weapon("Forsetis-Axe", "none", 65, 80, 1, statM, 10000, Player.AXE, CultimaPanel.animDelay/2,0,d);
      return temp;
   }

   //*****************DUAL AXES*************************************
   public static Weapon getDualAxe()
   {
      int[]statM = {0, 0, 0};
      String d = "A one-handed axe on each arm that can quickly strike upon beasts and wooden doors. Spinning attacks can strike more than one beast";
      Weapon temp = new Weapon("Dualaxe", "none", 10, 50, 1, statM, 65, Player.DUALAXE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getDualAxeOfFire()
   {
      int[]statM = {0, 0, 0};
      String d = "Flamejem adorned axes that can quickly strike upon beasts and wooden doors and set them to fire. Spinning attacks can strike more than one beast";
      Weapon temp = new Weapon("Dualaxe-O-Fire", "fire", 10, 50, 1, statM, 300, Player.DUALAXE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }

   public static Weapon getDualAxeOfFrost()
   {
      int[]statM = {0, 0, 0};
      String d = "Icejem adorned axes that can quickly strike upon beasts and wooden doors and set them to frost. Spinning attacks can strike more than one beast";
      Weapon temp = new Weapon("Dualaxe-O-Frost", "freeze", 10, 50, 1, statM, 300, Player.DUALAXE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }

   public static Weapon getBrightDualAxe()
   {
      int[]statM = {0, 0, 0};
      String d = "Moonjem adorned axes that quickly strike upon beasts and wooden doors, casting light where there is none. Spinning attacks strike more than one beast";
      Weapon temp = new Weapon("Bright-Dualaxe", "none", 15, 50, 1, statM, 140, Player.DUALAXE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getDualAxeOfMight()
   {
      int[]statM = {1, 0, 0};
      String d = "Ruby adorned axes that can quickly strike upon beasts and wooden doors and augment thy strength. Spinning attacks can strike more than one beast";
      Weapon temp = new Weapon("Dualaxe-O-Might", "none", 15, 50, 1, statM, 140, Player.DUALAXE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getDualAxeOfMind()
   {
      int[]statM = {0, 1, 0};
      String d = "Jade adorned axes that can quickly strike upon beasts and wooden doors and augment thy spellcraft. Spinning attacks can strike more than one beast";
      Weapon temp = new Weapon("Dualaxe-O-Mind", "none", 15, 50, 1, statM, 140, Player.DUALAXE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getSwiftDualAxe()
   {
      int[]statM = {0, 0, 1};
      String d = "Azurite adorned axes that can quickly strike upon beasts and wooden doors and augment thy agility. Spinning attacks can strike more than one beast";
      Weapon temp = new Weapon("Swift-Dualaxe", "none", 15, 50, 1, statM, 140, Player.DUALAXE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
      
   public static Weapon getVampyricAxes()
   {
      int[]statM = {0, -2, 0};
      String d ="Strange blackened axes that give to thee the very live they take from their victims. Spinning attacks can strike more than one beast";
      Weapon temp = new Weapon("Vampyric-Axes", "none", 10, 40, 1, statM, 4000, Player.DUALAXE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   //*********************HAMMERS**********************************************             
   public static Weapon getHammer()
   {
      int[]statM = {0, 0, -1};
      String d = "Heavy, two handed hammer suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Hammer", "none", 50, 60, 1, statM, 70, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getFlameHammer()
   {
      int[]statM = {0, 0, -1};
      String d = "Heavy, flamejem adorned hammer cast in magic flame, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Flamehammer", "fire", 50, 60, 1, statM, 300, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }

   public static Weapon getFrostHammer()
   {
      int[]statM = {0, 0, -1};
      String d = "Heavy, icejem adorned hammer that casts thy foes to frost. Suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Frosthammer", "freeze", 50, 60, 1, statM, 300, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }

   public static Weapon getBrightHammer()
   {
      int[]statM = {0, 0, 0};
      String d = "A glowing two-handed hammer casting light where there is none. Suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Bright-hammer", "none", 55, 60, 1, statM, 150, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getSwiftHammer()
   {
      int[]statM = {0, 0, 1};
      String d = "Azurite adorned hammer that augments thy agility, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Swift-hammer", "none", 55, 60, 1, statM, 150, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getHammerOfMight()
   {
      int[]statM = {1, 0, 0};
      String d = "A ruby adorned hammer that augments thy strength, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Hammer-O-Might", "none", 55, 60, 1, statM, 150, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getHammerOfMind()
   {
      int[]statM = {0, 1, 0};
      String d = "A jade adorned hammer that augments thy spellcraft, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Hammer-O-Mind", "none", 55, 60, 1, statM, 150, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }

   public static Weapon getSpikedHammer()
   {
      int[]statM = {0, 0, -1};
      String d = "A heavy toothed-hammer for dealing massive damage to enemies, doors and walls, suitable for mining";
      Weapon temp = new Weapon("Spiked-hammer", "none", 60, 70, 1, statM, 100, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
  
   public static Weapon getSpikedHammerOfFire()
   {
      int[]statM = {0, 0, -1};
      String d = "A heavy toothed-hammer cast in magic flame, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Spiked-flamehammer", "fire", 60, 70, 1, statM, 100, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getSpikedFrostHammer()
   {
      int[]statM = {0, 0, -1};
      String d = "A heavy toothed-hammer that casts foes in magic frost, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Spiked-Frosthammer", "freeze", 60, 70, 1, statM, 100, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getBrightSpikedHammer()
   {
      int[]statM = {0, 0, 0};
      String d = "A heavy toothed-hammer glowing to bring light where there is none, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Bright-Spiked-hammer", "none", 65, 70, 1, statM, 200, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getMightSpikedHammer()
   {
      int[]statM = {1, 0, 0};
      String d = "A heavy toothed-hammer augmenting thy strength, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Might-Spiked-hammer", "none", 65, 70, 1, statM, 200, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }

   public static Weapon getMindSpikedHammer()
   {
      int[]statM = {0, 1, 0};
      String d = "A heavy toothed-hammer augmenting thy spellcraft, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Mind-Spiked-hammer", "none", 65, 70, 1, statM, 200, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }

   public static Weapon getSwiftSpikedHammer()
   {
      int[]statM = {0, 0, 1};
      String d = "A heavy toothed-hammer augmenting thy agility, suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Swift-Spiked-hammer", "none", 65, 70, 1, statM, 200, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getBaneHammer()
   {
      int[]statM = {0, -2, 0};
      String d = "A heavy blackened-hammer, can cast a curse on its victim. Commoners will run in fear at the sight of it";
      Weapon temp = new Weapon("Banehammer", "curse", 50, 50, 1, statM, 2000, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getExoticHammer()
   {
      int[]statM = {1, 0, 0};
      String d = "A large hammer made of a strange metal, strong but easy to wield. Suitable for felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Exotic-hammer", "none", 55, 65, 1, statM, 1000, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getExoticHammerOfFire()
   {
      int[]statM = {1, 0, 0};
      String d = "A flamejem adorned hammer made of a strange metal, strong but easy to wield and cast in magic flame";
      Weapon temp = new Weapon("Exotic-flamehammer", "fire", 55, 65, 1, statM, 3000, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getExoticFrostHammer()
   {
      int[]statM = {1, 0, 0};
      String d = "Icejem adorned hammer made of a strange metal, strong but easy to wield and cast in magic frost";
      Weapon temp = new Weapon("Exotic-frosthammer", "freeze", 55, 65, 1, statM, 3000, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getBrightExoticHammer()
   {
      int[]statM = {1, 0, 0};
      String d = "A moonjem adorned hammer made of a strange metal, strong but easy to wield, and casting light where there is none";
      Weapon temp = new Weapon("Bright-Exotic-hammer", "none", 60, 65, 1, statM, 2000, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getMightExoticHammer()
   {
      int[]statM = {1, 0, 0};
      String d = "A ruby adorned hammer made of a strange metal, strong but easy to wield, augmenting thy strength. Can fell enemies, doors, walls or used in mining";
      Weapon temp = new Weapon("Might-Exotic-hammer", "none", 60, 65, 1, statM, 2000, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getMindExoticHammer()
   {
      int[]statM = {1, 1, 0};
      String d = "A jade adorned hammer made of a strange metal, strong but easy to wield, augmenting thy spellcraft. Can fell enemies, doors, walls or used in mining";
      Weapon temp = new Weapon("Mind-Exotic-hammer", "none", 60, 65, 1, statM, 2000, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getSwiftExoticHammer()
   {
      int[]statM = {1, 0, 1};
      String d = "Azurite adorned hammer made of a strange metal, strong but easy to wield, augmenting thy agility. Can fell enemies, doors, walls or used in mining";
      Weapon temp = new Weapon("Swift-Exotic-hammer", "none", 60, 65, 1, statM, 2000, Player.HAMMER, CultimaPanel.animDelay/2,0,d);
      return temp;
   }
   
   public static Weapon getMjolnir()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing two handed hammer that surges with awesome power, adept at felling enemies, doors, walls and mining";
      Weapon temp = new Weapon("Mjolnir", "stun", 65, 70, 1, statM, 10000, Player.HAMMER, CultimaPanel.animDelay/2,0, d);
      return temp;
   }
   
   public static Weapon getGiantMace()
   {
      int [] statM = {0, 0, -2};
      String d = "Unwieldy large mace, toothed at the top, suitable for massive man-beasts. Can fell enemies, doors, walls or used in mining";
      Weapon temp = new Weapon("Giant-mace", "none", 70, 80, 1, statM, 100, Player.HAMMER, CultimaPanel.animDelay,0,d);
      return temp;
   }

   public static Weapon getSpikedClub()
   {
      int [] statM = {0, 0, -3};
      String d = "Large wooden club with sharpened spikes, suitable for massive man-beasts. Can fell enemies, doors, walls or used in mining";
      Weapon temp = new Weapon("Spiked-club", "none", 80, 90, 1, statM, 70, Player.HAMMER, CultimaPanel.animDelay,0,d);
      return temp;
   }
   //******************************SWORDS**********************************************
   public static Weapon getSword()
   {
      int[]statM = {0, 0, 0};
      String d = "A two-handed sword, common among beast fighting adventurers";
      return new Weapon("Sword", "none", 50, 75, 1, statM, 100, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getBrightSword()
   {
      int[]statM = {0, 0, 0};
      String d = "A two-handed sword adorned with a moonjem, casting light where there is not";
      return new Weapon("Bright-sword", "none", 55, 75, 1, statM, 200, Player.SABER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getSwiftSword()
   {
      int[]statM = {0, 0, 1};
      String d = "A two-handed sword adorned with azurite, augmenting thy agility";
      return new Weapon("Swift-sword", "none", 55, 75, 1, statM, 200, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getSwordOfMight()
   {
      int[]statM = {1, 0, 0};
      String d = "A two-handed sword adorned with a ruby, augmenting thy strength";
      return new Weapon("Sword-o-might", "none", 55, 75, 1, statM, 200, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getSwordOfMind()
   {
      int[]statM = {0, 1, 0};
      String d = "A two-handed sword adorned with jade, augmenting thy spellcraft";
      return new Weapon("Sword-o-mind", "none", 55, 75, 1, statM, 200, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }

   public static Weapon getBlessedSword()
   {
      int[]statM = {0, 0, 0};
      String d = "A fine two-handed sword, elegant enough to impress the gods to bless thee";
      return new Weapon("Blessed-sword", "none", 50, 75, 1, statM, 1000, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getBlessedSwiftblade()
   {
      int[]statM = {0, 0, 1};
      String d = "An impressive two-handed sword adorned with azurite, augmenting thy agility";
      return new Weapon("Blessed-swiftblade", "none", 55, 75, 1, statM, 200, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getBlessedMightblade()
   {
      int[]statM = {1, 0, 0};
      String d = "An impressive two-handed sword adorned with a ruby, augmenting thy strength";
      return new Weapon("Blessed-mightblade", "none", 55, 75, 1, statM, 200, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getBlessedMindblade()
   {
      int[]statM = {0, 1, 0};
      String d = "An impressive two-handed sword adorned with a jade, augmenting thy spellcraft";
      return new Weapon("Blessed-mindblade", "none", 55, 75, 1, statM, 200, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getExcalibur()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing two handed sword that surges with awesome power";
      return new Weapon("Excalibur", "none", 60, 80, 1, statM, 10000, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }

   public static Weapon getFlameblade()
   {
      int []statM = {0,0,0};
      String d = "An impressive two-handed sword adorned with a flamejem, casting its victim to fire";
      return new Weapon("Flameblade", "fire", 60, 75, 1, statM, 2000, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getFrostblade()
   {
      int[]statM = {0, 0, 0};
      String d = "An impressive two-handed sword adorned with an icejem, casting its victim to frost";
      return new Weapon("Frostblade", "freeze", 50, 75, 1, statM, 100, Player.SABER, (CultimaPanel.animDelay/4),0,d);
   }
   //*************************SWORD AND SHIELD***************************************
   public static Weapon getSwordBuckler()
   {
      int[]statM = {0, 0, 0};
      String d = "A combination short-sword and small shield, balanced attacking power and defensive abilities. A shield bash can knock back foes";
      Weapon temp = new Weapon("Sword-Buckler", "none", 40, 65, 1, statM, 120, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getFlamebladeBuckler()
   {
      int[]statM = {0, 0, 0};
      String d = "A small shield and flamejem adorned short-sword to set enemies to fire. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Flameblade-Buckler", "fire", 40, 65, 1, statM, 400, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getFrostbladeBuckler()
   {
      int[]statM = {0, 0, 0};
      String d = "A small shield and icejem adorned short-sword to set enemies to frost. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Frostblade-Buckler", "freeze", 40, 65, 1, statM, 400, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }

   public static Weapon getBrightSwordBuckler()
   {
      int[]statM = {0, 0, 0};
      String d = "A small shield and moonjem adorned short-sword to cast light where there is none. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Bright-Sword-Buckler", "none", 45, 65, 1, statM, 240, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getSwordBucklerOfMight()
   {
      int[]statM = {1, 0, 0};
      String d = "A small shield and ruby adorned short-sword to augment thy strength. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Sword-Buckler-O-Might", "none", 45, 65, 1, statM, 240, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getSwordBucklerOfMind()
   {
      int[]statM = {0, 1, 0};
      String d = "A small shield and jade adorned short-sword to augment thy spellcraft. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Sword-Buckler-O-Mind", "none", 45, 65, 1, statM, 240, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getSwiftSwordBuckler()
   {
      int[]statM = {0, 0, 1};
      String d = "A small shield and azurite adorned short-sword to augment thy agility. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Swift-Sword-Buckler", "none", 45, 65, 1, statM, 240, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getSwordMirrorshield()
   {
      int[]statM = {0, 0, 0};
      String d = "A reflective shield and fine short-sword to balance attack and defense. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Sword-Mirrorshield", "none", 50, 70, 1, statM, 800, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getFlamebladeMirrorshield()
   {
      int[]statM = {0, 0, 0};
      String d = "A reflective shield and flamejem adorned short-sword to set thy foes to fire. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Flameblade-Mirrorshield", "fire", 50, 70, 1, statM, 3000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }

   public static Weapon getFrostbladeMirrorshield()
   {
      int[]statM = {0, 0, 0};
      String d = "A reflective shield and icejem adorned short-sword to set thy foes to frost. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Frostblade-Mirrorshield", "freeze", 50, 70, 1, statM, 3000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getBrightSwordMirrorshield()
   {
      int[]statM = {0, 0, 0};
      String d = "A reflective shield and moonjem adorned short-sword to set light where there is none. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Bright-Sword-Mirrorshield", "none", 55, 70, 1, statM, 2000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getMightSwordMirrorshield()
   {
      int[]statM = {1, 0, 0};
      String d = "A reflective shield and ruby adorned short-sword to augment thy strength. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Might-Sword-Mirrorshield", "none", 55, 70, 1, statM, 2000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }

   public static Weapon getMindSwordMirrorshield()
   {
      int[]statM = {0, 1, 0};
      String d = "A reflective shield and jade adorned short-sword to augment thy spellcraft. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Mind-Sword-Mirrorshield", "none", 55, 70, 1, statM, 2000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }

   public static Weapon getSwiftSwordMirrorshield()
   {
      int[]statM = {0, 0, 1};
      String d = "A reflective shield and azurite adorned short-sword to augment thy agility. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Swift-Sword-Mirrorshield", "none", 55, 70, 1, statM, 2000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
 
   public static Weapon getSwordShield()
   {
      int[]statM = {0, 0, 0};
      String d = "A finely crafted shield and sword, certain to impress commoners. A shield bash can knock back and stun foes";
      Weapon temp = new Weapon("Royal-sword-shield", "none", 60, 75, 1, statM, 1000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getLifeSwordShield()
   {
      int[]statM = {1, 1, 1};
      String d = "A finely crafted shield and sword adorned with a glowing life pearl - certain to impress the living and frighten the undead in this realm";
      Weapon temp = new Weapon("Royal-sword-of-life", "none", 65, 75, 1, statM, 10000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }

   public static Weapon getShieldBashBuckler()
   {
      int[]statM = {0, 0, 0};
      String d = "A combination short-sword and small shield, balanced attacking power and defensive abilities";
      Weapon temp = new Weapon("Buckler-shield-bash", "none", 1, 1, 1, statM, 120, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getShieldBashMirror()
   {
      int[]statM = {0, 0, 0};
      String d = "A reflective shield and fine short-sword to balance attack and defense";
      Weapon temp = new Weapon("Mirrorshield-bash", "none", 1, 1, 1, statM, 800, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getShieldBashRoyal()
   {
      int[]statM = {0, 0, 0};
      String d = "A finely crafted shield and sword, certain to impress commoners";
      Weapon temp = new Weapon("Royal-shield-bash", "none", 1, 1, 1, statM, 1000, Player.SWORDSHIELD, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   //******************DUAL BLADES*******************************************************************
   public static Weapon getShortSwords()
   {
      int []statM = {0,0,0};
      String d = "Short-swords in each hand, allowing thee to strike quickly. A spinning attack can damage many foes at once";
      return new Weapon("Short-swords", "none", 40, 65, 1, statM, 120, Player.DUAL, (CultimaPanel.animDelay/8),0,d);
   }
   
   public static Weapon getBrightSwords()
   {
      int []statM = {0,0,0};
      String d = "Quick striking short-swords in each hand, adorned with moonjems to cast light where there is none. A spinning attack can damage many foes at once";
      return new Weapon("Bright-swords", "none", 45, 65, 1, statM, 250, Player.DUAL, (CultimaPanel.animDelay/8),0, d);
   }
   
   public static Weapon getSwordsOfMight()
   {
      int []statM = {1,0,0};
      String d = "Quick striking short-swords in each hand, adorned with rubys to augment thy strength. A spinning attack can damage many foes at once";
      return new Weapon("Swords-o-might", "none", 45, 65, 1, statM, 250, Player.DUAL, (CultimaPanel.animDelay/8),0,d);
   }
   
   public static Weapon getSwordsOfMind()
   {
      int []statM = {0,1,0};
      String d = "Quick striking short-swords in each hand, adorned with jade to augment thy spellcraft. A spinning attack can damage many foes at once";
      return new Weapon("Swords-o-mind", "none", 45, 65, 1, statM, 250, Player.DUAL, (CultimaPanel.animDelay/8),0,d);
   }
   
   public static Weapon getWindSwords()
   {
      int []statM = {0,0,1};
      String d = "Quick striking short-swords in each hand, adorned with azurite to augment thy agility. A spinning attack can damage many foes at once";
      return new Weapon("Wind-swords", "none", 45, 65, 1, statM, 250, Player.DUAL, (CultimaPanel.animDelay/8),0,d);
   }

   public static Weapon getDualblades()
   {
      int []statM = {0,0,1};
      String d = "Finely crafted swords in each hand, allowing for quick and deadly strikes. A spinning attack can damage many foes at once";
      return new Weapon("Dualblades", "none", 70, 85, 1, statM, 2000, Player.DUAL, (CultimaPanel.animDelay/8),0,d);
   }

   public static Weapon getDualFlameblades()
   {
      int []statM = {0,0,0};
      String d = "Finely crafted swords in each hand adorned with flamejems, setting thy foes to fire. A spinning attack can damage many foes at once";
      return new Weapon("Dual-flameblades", "fire", 40, 65, 1, statM, 600, Player.DUAL, (CultimaPanel.animDelay/8),0,d);
   }
   
   public static Weapon getDualFrostblades()
   {
      int []statM = {0,0,0};
      String d = "Finely crafted swords in each hand adorned with icejems, setting thy foes to frost. A spinning attack can damage many foes at once";
      return new Weapon("Dual-frostblades", "freeze", 40, 65, 1, statM, 600, Player.DUAL, (CultimaPanel.animDelay/8),0,d);
   }
   
   //***************CROSSBOWS********************************************
   public static Weapon getCrossbow()
   {
      int[]statM = {0, 0, 0};
      String d = "Sturdy crossbow to throw a heavy bolt at moderate range";
      Weapon temp = new Weapon("Crossbow", "none", 10, 50, CROSSBOW_RANGE, statM, 180, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }
   
   public static Weapon getFlamecaster()
   {
      int[]statM = {0, 0, 0};
      String d = "Flamejem adorned crossbow to throw a heavy bolt to cast its target on fire";
      Weapon temp = new Weapon("Flamecaster", "fire", 10, 50, CROSSBOW_RANGE, statM, 600, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }
   
   public static Weapon getFrostcaster()
   {
      int[]statM = {0, 0, 0};
      String d = "Icejem adorned crossbow to throw a heavy bolt to cast its target to frost";
      Weapon temp = new Weapon("Frostcaster", "freeze", 10, 50, CROSSBOW_RANGE, statM, 600, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }

   public static Weapon getBrightcaster()
   {
      int[]statM = {0, 0, 0};
      String d = "Moonjem adorned crossbow to throw a heavy bolt at moderate range, glowing to cast light where there is none";
      Weapon temp = new Weapon("Brightcaster", "none", 15, 50, CROSSBOW_RANGE, statM, 400, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }
   
   public static Weapon getMightcaster()
   {
      int[]statM = {1, 0, 0};
      String d = "Ruby adorned crossbow to throw a heavy bolt at moderate range that augments thy strength";
      Weapon temp = new Weapon("Mightcaster", "none", 15, 50, CROSSBOW_RANGE, statM, 400, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }
   
   public static Weapon getMindcaster()
   {
      int[]statM = {0, 1, 0};
      String d = "Jade adorned crossbow to throw a heavy bolt at moderate range that augments thy spellcraft";
      Weapon temp = new Weapon("Mindcaster", "none", 15, 50, CROSSBOW_RANGE, statM, 400, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }
   
   public static Weapon getWindcaster()
   {
      int[]statM = {0, 0, 1};
      String d = "Azurite adorned crossbow to throw a heavy bolt at moderate range that augments thy agility";
      Weapon temp = new Weapon("Windcaster", "none", 15, 50, CROSSBOW_RANGE, statM, 400, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }
   
   public static Weapon getPoisonBoltcaster()
   {
      int[]statM = {0, 0, 0};
      String d = "Finely crafted crossbow that throws a poison-laced bolt at moderate range";
      Weapon temp = new Weapon("Poison-boltcaster", "poison", 15, 50, CROSSBOW_RANGE, statM, 2000, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }
   
   public static Weapon getBaneBoltcaster()
   {
      int[]statM = {0, -2, 0};
      String d = "Blackened crossbow that throws a cursing bolt at moderate range, terrifying commoners to flee at the sight of it";
      Weapon temp = new Weapon("Bane-boltcaster", "curse", 15, 50, CROSSBOW_RANGE, statM, 2000, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }
   
   public static Weapon getSoulCrossbow()
   {
      int[]statM = {0, 0, 0};
      String d = "Strange carved crossbow that throws a bolt at moderate range with a chance of possessing its victim";
      return new Weapon("Soul-crossbow", "control", 5, 30, CROSSBOW_RANGE, statM, 2000, Player.CROSSBOW, (int)(CultimaPanel.animDelay*2.5),0,d);
   }
   //********************BOWS*********************************
   public static Weapon getBow()
   {
      int[]statM = {0, 0, 0};
      String d = "Wooden shortbow that can accurately deliver arrows to ranged enemies";
      Weapon temp = new Weapon("Bow", "none", 5, 40, BOW_RANGE, statM, 80, Player.BOW, CultimaPanel.animDelay,0,d);
      return temp;
   }
   
   public static Weapon getBrightBow()
   {
      int[]statM = {0, 0, 0};
      String d = "Moonjem adorned shortbow that casts light where there is none";
      Weapon temp = new Weapon("Bright-bow", "none", 10, 40, BOW_RANGE, statM, 160, Player.BOW, CultimaPanel.animDelay,0,d);
      return temp;
   }
   
   public static Weapon getMightBow()
   {
      int[]statM = {1, 0, 0};
      String d = "Ruby adorned shortbow that augments thy strength";
      Weapon temp = new Weapon("Might-bow", "none", 10, 40, BOW_RANGE, statM, 160, Player.BOW, CultimaPanel.animDelay,0,d);
      return temp;
   }

   public static Weapon getMindBow()
   {
      int[]statM = {0, 1, 0};
      String d = "Jade adorned shortbow that augments thy spellcraft";
      Weapon temp = new Weapon("Mind-bow", "none", 10, 40, BOW_RANGE, statM, 160, Player.BOW, CultimaPanel.animDelay,0,d);
      return temp;
   }

   public static Weapon getWindBow()
   {
      int[]statM = {0, 0, 1};
      String d = "Azurite adorned shortbow that augments thy agility";
      Weapon temp = new Weapon("Wind-bow", "none", 10, 40, BOW_RANGE, statM, 160, Player.BOW, CultimaPanel.animDelay,0,d);
      return temp;
   }

   public static Weapon getFlamebow()
   {
      int[]statM = {0, 0, 0};
      String d = "Finely crafted shortbow adorned with a flamejem, casting its target to fire";
      Weapon temp = new Weapon("Flamebow", "fire", 8, 42, BOW_RANGE, statM, 2000, Player.BOW, (int)(CultimaPanel.animDelay*2.5),0, d);
      return temp;
   }
   
   public static Weapon getFrostbow()
   {
      int[]statM = {0, 0, 0};
      String d = "Finely crafted shortbow adorned with an icejem, casting its target to frost";
      Weapon temp = new Weapon("Frostbow", "freeze", 8, 42, BOW_RANGE, statM, 2000, Player.BOW, (int)(CultimaPanel.animDelay*2.5),0,d);
      return temp;
   }

   public static Weapon getLongbow()
   {
      int[]statM = {0, 0, 0};
      String d = "Wooden longbow that can deliver an arrow to far-ranged enemies";
      Weapon temp = new Weapon("Longbow", "none", 10, 45, LONGBOW_RANGE, statM, 120, Player.BOW, CultimaPanel.animDelay*2,0,d);
      return temp;
   }
   
   public static Weapon getBrightLongbow()
   {
      int[]statM = {0, 0, 0};
      String d = "Moonjem adorned longbow that can deliver an arrow to far-ranged enemies and cast light where there is none";
      Weapon temp = new Weapon("Bright-longbow", "none", 15, 45, LONGBOW_RANGE, statM, 250, Player.BOW, CultimaPanel.animDelay*2,0,d);
      return temp;
   }
   
   public static Weapon getMightLongbow()
   {
      int[]statM = {1, 0, 0};
      String d = "Ruby adorned longbow that can deliver an arrow to far-ranged enemies and augment thy strength";
      Weapon temp = new Weapon("Might-longbow", "none", 15, 45, LONGBOW_RANGE, statM, 250, Player.BOW, CultimaPanel.animDelay*2,0,d);
      return temp;
   }
   
   public static Weapon getMindLongbow()
   {
      int[]statM = {0, 1, 0};
      String d = "Jade adorned longbow that can deliver an arrow to far-ranged enemies and augment thy spellcraft";
      Weapon temp = new Weapon("Mind-longbow", "none", 15, 45, LONGBOW_RANGE, statM, 250, Player.BOW, CultimaPanel.animDelay*2,0,d);
      return temp;
   }

   public static Weapon getWindLongbow()
   {
      int[]statM = {0, 0, 1};
      String d = "Azurite adorned longbow that can deliver an arrow to far-ranged enemies and augment thy agility";
      Weapon temp = new Weapon("Wind-longbow", "none", 15, 45, LONGBOW_RANGE, statM, 250, Player.BOW, CultimaPanel.animDelay*2,0,d);
      return temp;
   }
   
   public static Weapon getBowOfKarna()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing longbow that surges with awesome power, can fire one or two arrows at a time";
      Weapon temp = new Weapon("Bow-of-Karna", "none", 20, 50, LONGBOW_RANGE, statM, 10000, Player.BOW, CultimaPanel.animDelay*2,0,d);
      return temp;
   }
   
   public static Weapon getBowOfKarnaDoubleShot()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing longbow that surges with awesome power, can fire one or two arrows at a time";
      Weapon temp = new Weapon("Bow-of-Karna", "none", 40, 65, LONGBOW_RANGE, statM, 10000, Player.BOW, (int)(CultimaPanel.animDelay*1.5),0,d);
      return temp;
   }
   //***********************SPEARS************************************
   public static Weapon getSpear()
   {
      int[]statM = {0, 0, 0};
      String d = "A wooden handled spear for keeping foes at range, can be thrust or thrown";
      return new Weapon("Spear", "none", 10, 50, 2, statM, 60, Player.SPEAR, (CultimaPanel.animDelay/2),0, d);
   }
   
   public static Weapon getFlameSpear()
   {
      int[]statM = {0, 0, 0};
      String d = "A flamejem adorned spear for keeping foes at range and casting them to fire, can be thrust or thrown";
      return new Weapon("Flamespear", "fire", 10, 50, 2, statM, 300, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getFrostSpear()
   {
      int[]statM = {0, 0, 0};
      String d = "An icejem adorned spear for keeping foes at range and casting them to frost, can be thrust or thrown";
      return new Weapon("Frostspear", "freeze", 10, 50, 2, statM, 300, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }

   public static Weapon getBrightSpear()
   {
      int[]statM = {0, 0, 0};
      String d = "A moonjem adorned spear for keeping foes at range and casting light where there is none, can be thrust or thrown";
      return new Weapon("Bright-spear", "none", 15, 50, 2, statM, 120, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getMightSpear()
   {
      int[]statM = {1, 0, 0};
      String d = "A ruby adorned spear for keeping foes at range and augmenting thy strength, can be thrust or thrown";
      return new Weapon("Might-spear", "none", 15, 50, 2, statM, 120, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getMindSpear()
   {
      int[]statM = {0, 1, 0};
      String d = "A jade adorned spear for keeping foes at range and augmenting thy spellcasting, can be thrust or thrown";
      return new Weapon("Mind-spear", "none", 15, 50, 2, statM, 120, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getWindSpear()
   {
      int[]statM = {0, 0, 1};
      String d = "An azurite adorned spear for keeping foes at range and augmenting thy agility, can be thrust or thrown";
      return new Weapon("Wind-spear", "none", 15, 50, 2, statM, 120, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getGungnir()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing spear that surges with awesome power, can be thrust or thrown and may return to thee";
      return new Weapon("Gungnir", "none", 20, 55, 2, statM, 10000, Player.SPEAR, (CultimaPanel.animDelay/2),0, d);
   }
   
   public static Weapon getHalberd()
   {
      int[]statM = {0, 0, 0};
      String d = "A wooden handled halberd for striking enemies at range";
      return new Weapon("Halberd", "none", 25, 60, 2, statM, 200, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }

   public static Weapon getHalberdOfFire()
   {
      int[]statM = {0, 0, 0};
      String d = "A flamejem adorned halberd for striking enemies at range and casting them to fire";
      return new Weapon("Halberd-O-Fire", "fire", 25, 60, 2, statM, 600, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getHalberdOfFrost()
   {
      int[]statM = {0, 0, 0};
      String d = "An icejem adorned halberd for striking enemies at range and casting them to frost";
      return new Weapon("Halberd-O-Frost", "freeze", 25, 60, 2, statM, 600, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }

   public static Weapon getBrightHalberd()
   {
      int[]statM = {0, 0, 2};
      String d = "A moonjem adorned halberd for striking enemies at range and casting light where there is none";
      return new Weapon("Bright-Halberd", "none", 25, 60, 2, statM, 2000, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getMightHalberd()
   {
      int[]statM = {1, 0, 0};
      String d = "A ruby adorned halberd for striking enemies at range and augmenting thy strength";
      return new Weapon("Might-Halberd", "none", 30, 60, 2, statM, 500, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getMindHalberd()
   {
      int[]statM = {0, 1, 0};
      String d = "A jade adorned halberd for striking enemies at range and augmenting thy spellcasting";
      return new Weapon("Mind-Halberd", "none", 30, 60, 2, statM, 500, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getWindHalberd()
   {
      int[]statM = {0, 0, 1};
      String d = "An azurite adorned halberd for striking enemies at range and augmenting thy agility";
      return new Weapon("Wind-Halberd", "none", 30, 60, 2, statM, 500, Player.SPEAR, (CultimaPanel.animDelay/4),0,d);
   }
   
   public static Weapon getOar()
   {
      int[]statM = {0, 0, 0};
      String d = "A wooden paddle used to row, row, row thy boat - may be used to strike upon small creatures of the water";
      return new Weapon("Boat-oar", "none", 5, 25, 1, statM, 4, Player.SPEAR, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getAmeNoNuhoko()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing halberd that surges with awesome power";
      return new Weapon("Ame-No-Nuhoko", "none", 35, 65, 2, statM, 10000, Player.SPEAR, (CultimaPanel.animDelay/4),0, d);
   }
   //************************LONGSTAFF*************************
   public static Weapon getLongstaff()
   {
      int[]statM = {0, 0, 2};
      String d = "A wooden longstaff to acrobatically striking foes at range or vault atop temple and city walls and counters";
      return new Weapon("Long-staff", "none", 5, 35, 2, statM, 40, Player.LONGSTAFF, (CultimaPanel.animDelay/8),0, d);
   }
   
   public static Weapon getFlamestaff()
   {
      int[]statM = {0, 0, 2};
      String d = "A flamejem adorned longstaff to acrobatically striking foes at range and casting them to fire or vault atop city walls and counters";
      return new Weapon("Flamestaff", "fire", 5, 35, 2, statM, 200, Player.LONGSTAFF, (CultimaPanel.animDelay/8),0, d);
   }

   public static Weapon getWindstaff()
   {
      int[]statM = {0, 0, 3};
      String d = "An azurite adorned longstaff to acrobatically striking foes at range or vault atop city walls and counters, augmenting thy agility";
      return new Weapon("Windstaff", "none", 10, 35, 2, statM, 100, Player.LONGSTAFF, (CultimaPanel.animDelay/8),0, d);
   }
   
   public static Weapon getBrightstaff()
   {
      int[]statM = {0, 0, 2};
      String d = "A moonjem adorned longstaff to acrobatically striking foes at range or vault atop city walls and counters, casting light where there was none";
      return new Weapon("Brightstaff", "none", 10, 35, 2, statM, 100, Player.LONGSTAFF, (CultimaPanel.animDelay/8),0, d);
   }
   
   public static Weapon getMightstaff()
   {
      int[]statM = {1, 0, 2};
      String d = "A ruby adorned longstaff to acrobatically striking foes at range or vault atop temple and city walls and counters, augmenting thy strength";
      return new Weapon("Mightstaff", "none", 10, 35, 2, statM, 100, Player.LONGSTAFF, (CultimaPanel.animDelay/8),0, d);
   }
   
   public static Weapon getMindstaff()
   {
      int[]statM = {0, 1, 2};
      String d = "A jade adorned longstaff to acrobatically striking foes at range or vault atop city walls and counters, augmenting thy spellcasting";
      return new Weapon("Mindstaff", "none", 10, 35, 2, statM, 100, Player.LONGSTAFF, (CultimaPanel.animDelay/8),0, d);
   }
   
   public static Weapon getKhatvanga()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing longstaff that surges with awesome power. Can be used to vault atop city walls and counters or strike foes at range";
      return new Weapon("Khatvanga", "none", 15, 40, 2, statM, 10000, Player.LONGSTAFF, (CultimaPanel.animDelay/8),0, d);
   }
    
   public static Weapon getSceptre()
   {
      int [] statM2 = {1, 1, 1};
      String d = "A golden, jeweled staff that will impress the living upon sight";
      return new Weapon("Royal-sceptre", "none", 4, 7, 2, statM2, 1000, Player.LONGSTAFF, (CultimaPanel.animDelay/4),0,d);   
   }
   
   public static Weapon getLifeSceptre()
   {
      int [] statM2 = {1, 1, 1};
      String d = "A golden jeweled staff, adorned with a lifepearl. Will impress the living upon sight, and frighten the undead that visit our realm";
      return new Weapon("Royal-lifesceptre", "none", 5, 35, 2, statM2, 10000, Player.LONGSTAFF, (CultimaPanel.animDelay/6),0,d);   
   }
   
   public static Weapon getIceStaff()
   {
      int[]statM = {0, 0, 2};
      String d = "An icejem adorned longstaff for acrobatically striking foes at range and casting them to frost";
      return new Weapon("Bright-Icestaff", "freeze", 5, 15, 2, statM, 2000, Player.LONGSTAFF, (CultimaPanel.animDelay/8),0, d);
   }
   //********************DAGGERS********************************
   public static Weapon getDagger()
   {
      int[]statM = {0, 0, 1};
      String d = "An iron dagger with a wooden handle for agile striking, can be thrust or thrown";
      return new Weapon("Dagger", "none", 5, 50, 1, statM, 50, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getBrightDagger()
   {
      int[]statM = {0, 0, 1};
      String d = "A glowing iron dagger with a moonjem adorned handle for agile striking, can be thrust or thrown";
      return new Weapon("Bright-dagger", "none", 10, 50, 1, statM, 100, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getSwiftDagger()
   {
      int[]statM = {0, 0, 2};
      String d = "An iron dagger with an azurite adorned handle to augment thy agility, can be thrust or thrown";
      return new Weapon("Swift-dagger", "none", 10, 50, 1, statM, 100, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getDaggerOfMight()
   {
      int[]statM = {1, 0, 1};
      String d = "An iron dagger with a ruby adorned handle to augment thy strength, can be thrust or thrown";
      return new Weapon("Dagger-o-might", "none", 10, 50, 1, statM, 100, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getDaggerOfMind()
   {
      int[]statM = {0, 1, 1};
      String d = "An iron dagger with a jade adorned handle to augment thy spellcraft, can be thrust or thrown";
      return new Weapon("Dagger-o-mind", "none", 10, 50, 1, statM, 100, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getCarnwennan()
   {
      int[]statM = {2, 2, 2};
      String d = "A legendary glowing dagger that surges with awesome power, may return to thee if thrown";
      return new Weapon("Carnwennan", "none", 15, 55, 1, statM, 10000, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }

   public static Weapon getPoisonDagger()
   {
      int[]statM = {0, 0, 2};
      String d = "A poison-laced iron dagger with a sculpted handle, can be thrust or thrown";
      return new Weapon("Poison-dagger", "poison", 10, 50, 1, statM, 1000, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getSoulDagger()
   {
      int[]statM = {0, 0, 2};
      String d = "A black-bladed dagger that can possess its victim, can be thrust or thrown";
      return new Weapon("Souldagger", "control", 10, 50, 1, statM, 2000, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getMagmaDagger()
   {
      int[]statM = {0, 0, 2};
      String d = "A iron dagger with a glowing orange blade and flamejem adorned handle to cast thy victims to fire, can be thrust or thrown";
      return new Weapon("Magmadagger", "fire", 10, 50, 1, statM, 1000, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getFrostDagger()
   {
      int[]statM = {0, 0, 2};
      String d = "An iron dagger with an icejem adorned handle to cast thy victims to frost, can be thrust or thrown";
      return new Weapon("Frostdagger", "freeze", 10, 50, 1, statM, 1000, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   public static Weapon getBaneDagger()
   {
      int[]statM = {0, -2, 2};
      String d = "A blackened dagger that will send commonors running upon sight and curse its victim, can be thrust or thrown";
      return new Weapon("Banedagger", "curse", 10, 50, 1, statM, 1000, Player.DAGGER, (CultimaPanel.animDelay/4),0, d);
   }
   
   //*********************UNARMED WEAPONS*********************************
   public static Weapon getFists()
   {
      int[]statM = {0, 0, 0};
      String d = "Thy very own fists, closed to strike an enemy";
      return new Weapon("Fists", "none", 1, 1, 1, statM, 0, Player.NONE, (CultimaPanel.animDelay/8),0,d);
   }
   
   public static Weapon getVampyricBite()
   {
      int[]statM = {0, 0, 0};
      String d = "A bite from thy sharpened teeth, 'twill render an unalert victim to stillness and give thee strength from their blood";
      Weapon temp = new Weapon("Vampyric-bite", "still", 2, 3, 1, statM, 0, Player.NONE, CultimaPanel.animDelay/4,0,d);
      return temp;
   }
   
   public static Weapon getHardVampyricBite()
   {
      int[]statM = {0, 0, 0};
      Weapon temp = new Weapon("Vampyric-bite", "curse", 5, 25, 1, statM, 0, Player.NONE, CultimaPanel.animDelay/4,0);
      return temp;
   }
   
   public static Weapon getJawTrap()
   {
      int[]statM = {0, 0, 0};
      String d = "An iron-toothed snare to trap game and foe";
      Weapon temp = new Weapon("Iron-jaw-trap", "still", 10, 25, 1, statM, 0, Player.NONE, CultimaPanel.animDelay/4,0, d);
      return temp;
   }
  //*************************WEREWOLF ATTACKS**********************************    
   public static Weapon getThrashingBite()    //werewolf attack player
   {
      int[]statM = {0, 0, 0};
      return new Weapon("Thrashing-bite", "none", 50, 75, 1, statM, 0, Player.WOLF, (CultimaPanel.animDelay/4),0);
   }
   
   public static Weapon getThrashingJaws()    //werewolf attack npc
   {
      int[]statM = {0, 0, 0};
      return new Weapon("Thrashing-jaws", "none", 50, 75, 1, statM, 0, Player.NONE, (CultimaPanel.animDelay/4),0);
   }

//**************MUSICAL INSTRUMENTS***************************

   public static Weapon getLute()
   {
      int[]statM = {0, 0, 0};
      return new Weapon("Lute", "none", 1, 1, 1, statM, 0, Player.NONE, (CultimaPanel.animDelay),0);
   }

   public static Weapon getLuteOfDestiny()
   {
      int[]statM = {0, 1, 0};
      String d = "Beautifully crafted lute that can charm some with an irresistable song, compelling them to follow and dance";
      return new Weapon("Lute-O-Destiny", "none", 1, 1, 1, statM, 2000, Player.LUTE, (CultimaPanel.animDelay),0,d);
   }

//*************MAGE STAFF***************************

   public static Weapon getStaff()
   {
      int[]statM = {0, 0, 0};
      String d = "Ancient wooden staff, carved with symbols that can direct offensive magic";
      return new Weapon("Staff", "none", 5, 25, 2, statM, 40, Player.STAFF, (CultimaPanel.animDelay/2),0,d);
   }
   
   public static Weapon getMageStaff()
   {
      int[]statM = {0, 1, 0};
      String d = "Ancient wooden staff, carved with symbols and adorned with a mannastone. Can direct offensive magic and has a chance of stunning an enemy with a strike";
      return new Weapon("Magestaff", "stun", 10, 30, 2, statM, 1000, Player.STAFF, (CultimaPanel.animDelay/3),0,d);
   }
   
   public static Weapon getOhnyalei()  //Legendary magic staff
   {
      int[]statM = {0, 3, 3};
      String d = "Strange magic has fused the mighty Magestaff with a unicorn horn. The magic within surges with power and augments your mind and agility";
      return new Weapon("Ohnyalei", "stun", 15, 40, 2, statM, 5000, Player.STAFF, (CultimaPanel.animDelay/3),0,d);
   }

//**************STAFF SPELLS*********************************
   public static Weapon getAdvance()
   {
      int[] statM = {0,0,0};
      String d = "Teleport thee through solid to the first open space in the direction of thy staff";
      Weapon w = new Weapon("Advance", "none", 0, 0, 1, statM, 4000, Player.STAFF, 0, 160, d);
      return w;
   }
   
   public static Weapon getBlindingLight()
   {
      int[]statM = {0, 0, 0};
      String d = "Temporarily stun thy enemy with a silent but blinding flash, rendering them less aware for a moment";
      Weapon w =  new Weapon("Blindinglight", "stun", 0, 1, 5, statM, 200, Player.STAFF, 0,20, d);
      return w;
   }

   public static Weapon getCurse()
   {
      int[]statM = {0, 0, 0};
      String d = "Cast dread and confusion in the mind of thy enemy, making them less adept in combat";
      return new Weapon("Curse", "curse", 0, 0, 3, statM, 800, Player.STAFF, 0,160,d);
   }

   public static Weapon getDeathtouch()
   {
      int[]statM = {0, 0, 0};
      String d = "Pull the very life from a close victim from the end of thy very finger";
      return new Weapon("Deathtouch", "none", 500, 500, 1, statM, 800, Player.STAFF, 0,160,d);
   }

   public static Weapon getFireball()
   {
      int[]statM = {0, 0, 0};
      String d = "From thy magic staff, launch a ball of flame to set thy foe ablaze";
      Weapon w = new Weapon("Fireball", "fire", 10, 20, 8, statM, 200, Player.STAFF, 0,40, d);
      return w;
   }
   
   public static Weapon getFireShield()
   {
      int[] statM = {0,0,0};
      String d = "Summon a wall of protective flame in front of thee";
      return new Weapon("Fireshield", "none", 0, 0, 3, statM, 4000, Player.STAFF, 0,180,d);
   }
   
   public static Weapon getIcestrike()
   {
      int[]statM = {0, 0, 0};
      String d = "Damage and slow a foe from range with piercing cold";
      Weapon w = new Weapon("Icestrike", "freeze", 10, 30, 8, statM, 400, Player.STAFF, 0,80, d);
      return w;
   }

   public static Weapon getLightning()
   {
      int[]statM = {0, 0, 0};
      String d = "Summon from thy staff a great bolt from a storm, striking an enemy at range";
      Weapon w = new Weapon("Lightning", "none", 60, 100, 5, statM, 400, Player.STAFF, 0,80,d);
      return w;
   }

   public static Weapon getPhasewall()
   {
      int[]statM = {0, 0, 0};
      String d = "Shift into existence an opening in a wall of stone, earth or wood to allow thee to pass through";
      return new Weapon("Phasewall", "none", 0, 0, 1, statM, 800, Player.STAFF, 0,300, d);
   }

   public static Weapon getPossess()
   {
      int[]statM = {0, 0, 0};
      String d = "Dark spellcraft that renders thy foe mindless, so it might attack its own kind";
      return new Weapon("Possess", "control", 0, 0, 3, statM, 800, Player.STAFF, 0,160,d);
   }
   
   public static Weapon getRaiseStone()
   {
      int[] statM = {0,0,0};
      String d = "Summon a spire like rock from the earth to block a path, or perhaps turn into a magic monolith";
      return new Weapon("Raise-Stone", "none", 0, 0, 1, statM, 2000, Player.STAFF, 0,100,d);
   }

   public static Weapon getSpidersWeb()
   {
      int[]statM = {0, 0, 0};
      String d = "Ensnare a beast in a magic web as from a great spider";
      Weapon temp = new Weapon("Spidersweb", "web", 0, 0, 5, statM, 8000, Player.STAFF, 0,30, d);
      return temp;
   }
   
   public static Weapon getStonecast()
   {
      int[]statM = {0, 0, 0};
      String d = "Render any living creature as solid stone at the grasp of thy hand";
      return new Weapon("Stonecast", "none", 500, 500, 5, statM, 8000, Player.STAFF, 0,200,d);
   }
   
   public static Weapon getSummonVortex()
   {
      int[] statM = {0,0,0};
      String d = "Summon a mindless spinning cyclone of air to deal massive injury to thy enemies";
      return new Weapon("Summon-Vortex", "none", 0, 0, CultimaPanel.SCREEN_SIZE/4, statM, 4000, Player.STAFF, 0,260,d);
   }
   
//**************THROWN WEAPONS*********************
   public static Weapon getStone()
   {
      int[]statM = {0, 0, 0};
      String d = "Stones can be effective against small and weak foes, and ammo easy to come by";
      //                       name       effect  minDamage maxDamage  range  statModifier value imageIndex          reloadTime    mannaCost description
      return new Weapon("Throwing-stone", "none",      1,       5,       4,       statM,     0,  Player.THROWN, CultimaPanel.animDelay*2, 0,    d);
   } 
   
   public static Weapon getSling()
   {
      int[]statM = {0, 0, 0};
      String d = "Throw stones faster and further with a sling, along with a leather satchel to hold stones";
      return new Weapon("Sling", "none", 1, 6, 6, statM, 5, Player.THROWN, (int)(CultimaPanel.animDelay*2.5),0, d);
   }

   public static Weapon getHolyHandGrenade()
   {
      int[]statM = {0, 0, 0};
      String d = "First shalt thou take out the Holy Pin, then shalt thou count to three, not more, no less";
      return new Weapon("Grenade-of-Antioch", "fire", 2000, 3000, 5, statM, 9000, Player.THROWN, CultimaPanel.animDelay*4,0, d);
   }   
//**************SHIP WEAPONS************************

   public static Weapon getBrigandCannon()
   {
      int[]statM = {0, 0, 5};
      String d = "Fire from great range an exploding ball of iron, ending foes and likely the items they carry";
      return new Weapon("Brigand-cannon", "fire", 200, 300, 10, statM, 1000, Player.BRIGANDSHIP, CultimaPanel.animDelay*4,0,d);
   }

   public static Weapon getGreatCannon()
   {
      int[]statM = {0, 0, 5};
      String d = "Fire from great range an exploding ball of iron, ending foes and likely the items they carry";
      return new Weapon("Great-cannon", "fire", 300, 400, 12, statM, 1000, Player.GREATSHIP, CultimaPanel.animDelay*4,0, d);
   }
   
  //**********HORSE WEAPONS************************* 
   
   public static Weapon getBrightHorn()   //unicorn horn
   {
      int[]statM = {0, 2, 0};
      String d = "The glowing horn of a majestic unicorn - can keep foes at bay and give thee faster manna recovery";
      return  new Weapon("Bright-horn", "none", 15, 50, 2, statM, 10000, Player.LONGSTAFF, 0 ,0,d);
   }
   
   public static Weapon getStrikingHooves()
   {
      int[]statM = {0, 0, 2};
      String d = "The pounding hooves of a rearing steed can strike at enemies";
      return  new Weapon("Striking-hooves", "none", 5, 20, 1, statM, 0, Player.NONE, 0 ,0, d);
   }

  //**************NPC WEAPONS*****************************

   public static Weapon getCannonball()
   {
      return new Weapon("Cannonball", "none", 25, 50, 8, null, 0, Player.NONE, 0,0);   
   }
   
   public static Weapon getCrushingJaws()
   {
      int[]statM = {0, 0, 0};
      return new Weapon("Crushing-jaws", "none", 100, 500, 1, statM, 800, Player.NONE, 0,0);
   }

   public static Weapon getSwirlingChaos()
   {
      return new Weapon("Swirling-chaos", "none", 50, 150, 2, null, 0, Player.NONE, 0,0);
   }
   
   public static Weapon getMassiveFists()
   {
      return new Weapon("Massive-fists", "none", 50, 150, 2, null, 0, Player.NONE, 0,0);
   }

   public static Weapon getBite()
   {
      return new Weapon("Bite", "none", 3, 5, 1, null, 0, Player.NONE, 0,0);   
   }
   
   public static Weapon getBurn()
   {
      return new Weapon("Burn", "fire", 3, 5, 1, null, 0, Player.NONE, 0,0);   
   }

   public static Weapon getFangedBite()
   {
      return new Weapon("Fanged-bite", "none", 8, 15, 1, null, 0, Player.NONE, 0 ,0);   
   }

   public static Weapon getCrushingBite()
   {
      return  new Weapon("Crushing-bite", "none", 30, 50, 1, null, 0, Player.NONE, 0 ,0);   
   }

   public static Weapon getCoilingArms()
   {
      return  new Weapon("Coiling-arms", "none", 10, 25, 2, null, 0, Player.NONE, 0 ,0);   
   }

   public static Weapon getMassiveCoilingArms()
   {
      return  new Weapon("Massive-coiling-arms", "none", 30, 80, 3, null, 0, Player.NONE, 0 ,0);   
   }

   public static Weapon getHardPoisionBite()
   {
      return new Weapon("Hard-poison-bite", "poison", 30, 80, 1, null, 0, Player.NONE, 0 ,0); 
   }

   public static Weapon getPoisonBite()
   {
      return new Weapon("Poison-bite", "poison", 2, 5, 1, null, 0, Player.NONE, 0 ,0); 
   }

   public static Weapon getIceBreath()
   {
      return  new Weapon("Ice-breath", "freeze", 20, 40, 8, null, 0, Player.NONE, 0 ,0);   
   }

   public static Weapon getDemonicClaw()
   {
      return new Weapon("Demonic-claw", "curse", 25, 50, 1, null, 0, Player.NONE, 0 ,0); 
   }

   public static Weapon getDragonfire()
   {
      return new Weapon("Dragonfire", "fire", 20, 40, 9, null, 0, Player.NONE, 0, 0);
   }

//npc sorcerer's lightning attack
   public static Weapon getLightningbolt()
   {
      return new Weapon("Lightningbolt", "none", 25, 60, 5, null, 0, Player.NONE, 0, 0);
   }

   public static Weapon getDragonBolt()
   {
      return new Weapon("Lightningfire", "none", 50, 100, 9, null, 0, Player.NONE, 0, 0);
   }
   
   public static Weapon getGun()
   {
      return new Weapon("Gun", "none", 50, 75, 9, null, 0, Player.NONE, 0, 0);
   }

   //true if the weapon with the name n is a throwable object
   public static boolean isThrown(String n)
   {
      return (n.toLowerCase().contains("throw") || n.toLowerCase().contains("sling")  || n.toLowerCase().contains("grenade"));
   }

   public static boolean isLegendaryWeapon(String n)
   {
      n = n.toLowerCase().trim();
      return (n.contains("forsetis-axe") ||
              n.contains("mjolnir") || 
              n.contains("bow-of-karna") || 
              n.contains("excalibur") || 
              n.contains("ame-no-nuhoko") ||
              n.contains("gada-torchmace") ||
              n.contains("khatvanga") ||
              n.contains("gungnir") ||
              n.contains("ohnyalei") ||
              n.contains("carnwennan") ||
              n.contains("grenade-of-antioch"));
   }   
   
   public static boolean isSpell(String n)
   {
      n = n.toLowerCase().trim();
      return (n.contains("phasewall") ||
           n.contains("advance") ||
           n.contains("curse") ||
           n.contains("possess") ||
           n.contains("fireball") ||
           n.contains("spidersweb") ||
           n.contains("icestrike") ||
           n.contains("lightning") ||
           n.contains("deathtouch") ||
           n.contains("fireshield") ||
           n.contains("summon-vortex") ||
           n.contains("raise-stone") ||
           n.contains("lightningbolt") ||
           n.contains("stonecast") ||
           n.contains("blindingLight"));
   }
   
   public static boolean isSwordAndShield(String n)
   {
      n = n.toLowerCase().trim();
      return (n.contains("royal-sword-shield") ||
         n.contains("royal-sword-of-life") ||
         n.contains("sword-buckler") ||
         n.contains("flameblade-buckler") ||
         n.contains("frostblade-buckler") ||
         n.contains("bright-sword-buckler") ||
         n.contains("swift-sword-buckler") ||
         n.contains("sword-buckler-o-might") ||
         n.contains("sword-buckler-o-mind") ||
         n.contains("sword-mirrorshield") ||
         n.contains("flameblade-mirrorshield") ||
         n.contains("frostblade-mirrorshield") ||
         n.contains("bright-sword-mirrorshield") ||
         n.contains("might-sword-mirrorshield") ||
         n.contains("mind-sword-mirrorshield") ||
         n.contains("swift-sword-mirrorshield"));
   }
   
   public static boolean isDagger(String n)
   {
      return (n.toLowerCase().contains("dagger") || n.toLowerCase().contains("carnwennan"));
   }
   
   public static boolean isTorch(String n)
   {
      return (n.toLowerCase().contains("torch"));
   }
   
   //true if the weapon with the name n is a kind of spear that is throwable
   public static boolean isSpear(String n)
   {
      return (n.toLowerCase().contains("spear") || n.toLowerCase().contains("gungnir"));
   }
   
   public static boolean isHalberd(String n)
   {
      return (n.toLowerCase().contains("halberd") || n.toLowerCase().contains("ame-no-nuhoko"));
   }
   
   //true if the weapon with the name are is two one-handed weapons: one in each hand
   public static boolean isDual(String n)
   {
      return (n.toLowerCase().contains("dual") || n.toLowerCase().contains("swords") || n.toLowerCase().contains("axes")  || n.toLowerCase().contains("blades"));
   }
   
   public static boolean isDualAxes(String n)
   {
      return (n.equalsIgnoreCase("dualaxe") || 
            n.equalsIgnoreCase("dualaxe-o-fire") ||
            n.equalsIgnoreCase("dualaxe-o-frost") ||
            n.equalsIgnoreCase("bright-dualaxe") ||
            n.equalsIgnoreCase("dualaxe-o-might") ||
            n.equalsIgnoreCase("dualaxe-o-mind") ||
            n.equalsIgnoreCase("swift-dualaxe") ||
            n.equalsIgnoreCase("vampyric-axes"));
   }
   
   public static boolean isGreatAxe(String n)
   {
      return (n.equalsIgnoreCase("axe") ||
         n.equalsIgnoreCase("mirrored-axe") ||
         n.equalsIgnoreCase("flame-mirroraxe") ||
         n.equalsIgnoreCase("frost-mirroraxe") || 
         n.equalsIgnoreCase("bright-mirroraxe") || 
         n.equalsIgnoreCase("might-mirroraxe") || 
         n.equalsIgnoreCase("mind-mirroraxe") || 
         n.equalsIgnoreCase("swift-mirroraxe") || 
         n.equalsIgnoreCase("forsetis-axe"));  
   }
   
   public static boolean isDualSwords(String n)
   {
      return (n.toLowerCase().contains("swords") || n.toLowerCase().contains("blades"));
   }
   
   public static boolean isLongStaff(String n)
   {
      return (n.equalsIgnoreCase("long-staff") || 
         n.equalsIgnoreCase("flamestaff") ||
         n.equalsIgnoreCase("brightstaff") ||
         n.equalsIgnoreCase("windstaff") ||
         n.equalsIgnoreCase("mightstaff") ||
         n.equalsIgnoreCase("mindstaff") ||
         n.equalsIgnoreCase("bright-icestaff") ||
         n.equalsIgnoreCase("khatvanga") ||
         n.equalsIgnoreCase("royal-lifesceptre") ||
         n.equalsIgnoreCase("royal-sceptre"));   
   }
   
   public static boolean isSword(String n)
   {
      return(n.equalsIgnoreCase("sword") || 
            n.equalsIgnoreCase("flameblade") ||
            n.equalsIgnoreCase("frostblade") ||
            n.equalsIgnoreCase("bright-sword") ||
            n.equalsIgnoreCase("swift-sword") ||
            n.equalsIgnoreCase("sword-o-might") ||
            n.equalsIgnoreCase("sword-o-mind") ||
            n.equalsIgnoreCase("blessed-sword") ||
            n.equalsIgnoreCase("blessed-swiftblade") ||
            n.equalsIgnoreCase("blessed-mightblade") ||
            n.equalsIgnoreCase("blessed-mindblade") ||
            n.equalsIgnoreCase("excalibur"));            
   }
   
   public static boolean isHammer(String n)
   {
      return(n.equalsIgnoreCase("hammer") ||
         n.equalsIgnoreCase("flamehammer") ||
         n.equalsIgnoreCase("frosthammer") ||
         n.equalsIgnoreCase("bright-hammer") ||
         n.equalsIgnoreCase("swift-hammer") ||
         n.equalsIgnoreCase("hammer-o-might") ||
         n.equalsIgnoreCase("hammer-o-mind") ||
         n.equalsIgnoreCase("spiked-hammer") ||
         n.equalsIgnoreCase("spiked-frosthammer") ||
         n.equalsIgnoreCase("spiked-flamehammer") ||
         n.equalsIgnoreCase("bright-spiked-hammer") ||
         n.equalsIgnoreCase("might-spiked-hammer") ||
         n.equalsIgnoreCase("mind-spiked-hammer") ||
         n.equalsIgnoreCase("swift-spiked-hammer") ||
         n.equalsIgnoreCase("banehammer") ||
         n.equalsIgnoreCase("exotic-hammer") ||
         n.equalsIgnoreCase("exotic-frosthammer") ||
         n.equalsIgnoreCase("exotic-flamehammer") ||
         n.equalsIgnoreCase("bright-exotic-hammer") ||
         n.equalsIgnoreCase("might-exotic-hammer") ||
         n.equalsIgnoreCase("mind-exotic-hammer") ||
         n.equalsIgnoreCase("swift-exotic-hammer") ||
         n.equalsIgnoreCase("mjolnir") ||
         n.equalsIgnoreCase("giant-mace") ||
         n.equalsIgnoreCase("spiked-club"));            
   }

   public static Weapon getWeaponWithName(String n)
   {
      n = n.trim();
      if(n.equalsIgnoreCase("none"))
         return getNone();
     
      if(n.equalsIgnoreCase("boat-oar"))
         return getOar();
     
      if(n.equalsIgnoreCase("torch"))
         return getTorch();
         
      if(n.equalsIgnoreCase("toothed-torch"))
         return getToothedTorch();
      if(n.equalsIgnoreCase("toothed-torch-o-might"))
         return getToothedTorchOfMight();
      if(n.equalsIgnoreCase("toothed-torch-o-mind"))
         return getToothedTorchOfMind();
      if(n.equalsIgnoreCase("swift-toothed-torch"))
         return getSwiftToothedTorch();
      if(n.equalsIgnoreCase("gada-torchmace"))        //legendary weapon
         return getGadaTorchmace();
        
      if(n.equalsIgnoreCase("axe"))
         return getAxe();
      if(n.equalsIgnoreCase("flameaxe"))
         return getFlameAxe();
      if(n.equalsIgnoreCase("frostaxe"))
         return getFrostAxe();
      if(n.equalsIgnoreCase("axe-o-might"))
         return getAxeOfMight();
      if(n.equalsIgnoreCase("bright-axe"))
         return getBrightAxe();
      if(n.equalsIgnoreCase("axe-o-mind"))
         return getAxeOfMind();
      if(n.equalsIgnoreCase("swiftaxe"))
         return getSwiftAxe();
         
      if(n.equalsIgnoreCase("mirrored-axe"))
         return getMirroredAxe();
      if(n.equalsIgnoreCase("flame-mirroraxe"))
         return getFlameMirrorAxe();
      if(n.equalsIgnoreCase("frost-mirroraxe"))
         return getFrostMirrorAxe();
      if(n.equalsIgnoreCase("bright-mirroraxe"))
         return getBrightMirrorAxe();
      if(n.equalsIgnoreCase("might-mirroraxe"))
         return getMightMirrorAxe();
      if(n.equalsIgnoreCase("mind-mirroraxe"))
         return getMindMirrorAxe();
      if(n.equalsIgnoreCase("swift-mirroraxe"))
         return getSwiftMirrorAxe(); 
      if(n.equalsIgnoreCase("forsetis-axe"))       //legendary weapon
         return getForsetisAxe();  
         
      if(n.equalsIgnoreCase("dualaxe"))
         return getDualAxe();
      if(n.equalsIgnoreCase("dualaxe-o-fire"))
         return getDualAxeOfFire();
      if(n.equalsIgnoreCase("dualaxe-o-frost"))
         return getDualAxeOfFrost();
      if(n.equalsIgnoreCase("bright-dualaxe"))
         return getBrightDualAxe();
      if(n.equalsIgnoreCase("dualaxe-o-might"))
         return getDualAxeOfMight();   
      if(n.equalsIgnoreCase("dualaxe-o-mind"))
         return getDualAxeOfMind();
      if(n.equalsIgnoreCase("swift-dualaxe"))
         return getSwiftDualAxe();    
      if(n.equalsIgnoreCase("vampyric-axes"))
         return getVampyricAxes();
         
      if(n.equalsIgnoreCase("royal-sword-shield"))
         return getSwordShield();
      if(n.equalsIgnoreCase("royal-sword-of-life"))   
         return getLifeSwordShield();
         
      if(n.equalsIgnoreCase("sword-buckler"))
         return getSwordBuckler();
      if(n.equalsIgnoreCase("flameblade-buckler"))
         return getFlamebladeBuckler();
      if(n.equalsIgnoreCase("frostblade-buckler"))
         return getFrostbladeBuckler();
      if(n.equalsIgnoreCase("bright-sword-buckler"))
         return getBrightSwordBuckler();
      if(n.equalsIgnoreCase("swift-sword-buckler"))
         return getSwiftSwordBuckler();
      if(n.equalsIgnoreCase("sword-buckler-o-might"))
         return getSwordBucklerOfMight();
      if(n.equalsIgnoreCase("sword-buckler-o-mind"))
         return getSwordBucklerOfMind();
         
      if(n.equalsIgnoreCase("sword-mirrorshield"))
         return getSwordMirrorshield();   
      if(n.equalsIgnoreCase("flameblade-mirrorshield"))
         return getFlamebladeMirrorshield(); 
      if(n.equalsIgnoreCase("frostblade-mirrorshield"))
         return getFrostbladeMirrorshield();  
      if(n.equalsIgnoreCase("bright-sword-mirrorshield"))
         return getBrightSwordMirrorshield();
      if(n.equalsIgnoreCase("might-sword-mirrorshield"))
         return getMightSwordMirrorshield();
      if(n.equalsIgnoreCase("mind-sword-mirrorshield"))
         return getMindSwordMirrorshield();
      if(n.equalsIgnoreCase("swift-sword-mirrorshield"))
         return getSwiftSwordMirrorshield();
             
      if(n.equalsIgnoreCase("hammer"))
         return getHammer();
      if(n.equalsIgnoreCase("flamehammer"))
         return getFlameHammer();
      if(n.equalsIgnoreCase("frosthammer"))
         return getFrostHammer();
      if(n.equalsIgnoreCase("bright-hammer"))
         return getBrightHammer(); 
      if(n.equalsIgnoreCase("swift-hammer"))
         return getSwiftHammer(); 
      if(n.equalsIgnoreCase("hammer-o-might"))
         return getHammerOfMight();
      if(n.equalsIgnoreCase("hammer-o-mind"))
         return getHammerOfMind();
          
      if(n.equalsIgnoreCase("spiked-hammer"))
         return getSpikedHammer();
      if(n.equalsIgnoreCase("spiked-frosthammer"))
         return getSpikedFrostHammer();
      if(n.equalsIgnoreCase("spiked-flamehammer"))
         return getSpikedHammerOfFire();
      if(n.equalsIgnoreCase("bright-spiked-hammer"))
         return getBrightSpikedHammer();
      if(n.equalsIgnoreCase("might-spiked-hammer"))
         return getMightSpikedHammer();
      if(n.equalsIgnoreCase("mind-spiked-hammer"))
         return getMindSpikedHammer();
      if(n.equalsIgnoreCase("swift-spiked-hammer"))
         return getSwiftSpikedHammer(); 
      if(n.equalsIgnoreCase("banehammer"))
         return getBaneHammer();
         
      if(n.equalsIgnoreCase("exotic-hammer"))
         return getExoticHammer();
      if(n.equalsIgnoreCase("exotic-frosthammer"))
         return getExoticFrostHammer();
      if(n.equalsIgnoreCase("exotic-flamehammer"))
         return getExoticHammerOfFire();
      if(n.equalsIgnoreCase("bright-exotic-hammer"))
         return getBrightExoticHammer();
      if(n.equalsIgnoreCase("might-exotic-hammer"))
         return getMightExoticHammer();
      if(n.equalsIgnoreCase("mind-exotic-hammer"))
         return getMindExoticHammer();
      if(n.equalsIgnoreCase("swift-exotic-hammer"))
         return getSwiftExoticHammer();
      if(n.equalsIgnoreCase("mjolnir"))            //legendary weapon
         return getMjolnir();
         
      if(n.equalsIgnoreCase("giant-mace"))
         return getGiantMace();
      if(n.equalsIgnoreCase("spiked-club"))
         return getSpikedClub();
         
      if(n.equalsIgnoreCase("sword"))
         return getSword();
      if(n.equalsIgnoreCase("flameblade"))
         return getFlameblade();
      if(n.equalsIgnoreCase("frostblade"))
         return getFrostblade();
      if(n.equalsIgnoreCase("bright-sword"))
         return getBrightSword();
      if(n.equalsIgnoreCase("swift-sword"))
         return getSwiftSword();
      if(n.equalsIgnoreCase("sword-o-might"))
         return getSwordOfMight();
      if(n.equalsIgnoreCase("sword-o-mind"))
         return getSwordOfMind();
         
      if(n.equalsIgnoreCase("blessed-sword"))
         return getBlessedSword();
      if(n.equalsIgnoreCase("blessed-swiftblade"))
         return getBlessedSwiftblade();
      if(n.equalsIgnoreCase("blessed-mightblade"))
         return getBlessedMightblade();
      if(n.equalsIgnoreCase("blessed-mindblade"))
         return getBlessedMindblade();    
      if(n.equalsIgnoreCase("excalibur"))          //legendary weapon
         return getExcalibur();            
         
      if(n.equalsIgnoreCase("dualblades"))
         return getDualblades(); 
      if(n.equalsIgnoreCase("short-swords"))
         return getShortSwords();
      if(n.equalsIgnoreCase("dual-flameblades"))
         return getDualFlameblades();
      if(n.equalsIgnoreCase("dual-frostblades"))
         return getDualFrostblades();
      if(n.equalsIgnoreCase("bright-swords"))
         return getBrightSwords();
      if(n.equalsIgnoreCase("swords-o-mind"))
         return getSwordsOfMind();
      if(n.equalsIgnoreCase("swords-o-might"))
         return getSwordsOfMight();
      if(n.equalsIgnoreCase("wind-swords"))
         return getWindSwords();   
         
      if(n.equalsIgnoreCase("crossbow"))
         return getCrossbow();
      if(n.equalsIgnoreCase("flamecaster"))
         return getFlamecaster(); 
      if(n.equalsIgnoreCase("frostcaster"))
         return getFrostcaster();
      if(n.equalsIgnoreCase("brightcaster"))
         return getBrightcaster();    
      if(n.equalsIgnoreCase("mightcaster"))
         return getMightcaster();
      if(n.equalsIgnoreCase("mindcaster"))
         return getMindcaster();
      if(n.equalsIgnoreCase("windcaster"))
         return getWindcaster();  
         
      if(n.equalsIgnoreCase("soul-crossbow"))
         return getSoulCrossbow();
      if(n.equalsIgnoreCase("poison-boltcaster"))
         return getPoisonBoltcaster();
      if(n.equalsIgnoreCase("bane-boltcaster"))
         return getBaneBoltcaster();
         
      if(n.equalsIgnoreCase("bow"))
         return getBow();
      if(n.equalsIgnoreCase("flamebow"))
         return getFlamebow();
      if(n.equalsIgnoreCase("frostbow"))
         return getFrostbow();
      if(n.equalsIgnoreCase("bright-bow"))
         return getBrightBow();
      if(n.equalsIgnoreCase("might-bow"))
         return getMightBow();
      if(n.equalsIgnoreCase("mind-bow"))
         return getMindBow();
      if(n.equalsIgnoreCase("wind-bow"))
         return getWindBow();
         
      if(n.equalsIgnoreCase("longbow"))
         return getLongbow();
      if(n.equalsIgnoreCase("bright-longbow"))
         return getBrightLongbow();
      if(n.equalsIgnoreCase("might-longbow"))
         return getMightLongbow();
      if(n.equalsIgnoreCase("mind-longbow"))
         return getMindLongbow();
      if(n.equalsIgnoreCase("wind-longbow"))
         return getWindLongbow();
      if(n.equalsIgnoreCase("bow-of-karna"))       //legendary weapon
         return getBowOfKarna();   
         
      if(n.equalsIgnoreCase("spear"))
         return getSpear();
      if(n.equalsIgnoreCase("flamespear"))
         return getFlameSpear();
      if(n.equalsIgnoreCase("frostspear"))
         return getFrostSpear();
      if(n.equalsIgnoreCase("bright-spear"))
         return getBrightSpear();
      if(n.equalsIgnoreCase("might-spear"))
         return getMightSpear();
      if(n.equalsIgnoreCase("mind-spear"))
         return getMindSpear();
      if(n.equalsIgnoreCase("wind-spear"))
         return getWindSpear();
      if(n.equalsIgnoreCase("gungnir"))            //legendary weapon
         return getGungnir();
         
      if(n.equalsIgnoreCase("halberd"))
         return getHalberd();
      if(n.equalsIgnoreCase("halberd-o-fire"))
         return getHalberdOfFire();
      if(n.equalsIgnoreCase("halberd-o-frost"))
         return getHalberdOfFrost();
      if(n.equalsIgnoreCase("bright-halberd"))
         return getBrightHalberd();
      if(n.equalsIgnoreCase("might-halberd"))
         return getMightHalberd();
      if(n.equalsIgnoreCase("mind-halberd"))
         return getMindHalberd();
      if(n.equalsIgnoreCase("wind-halberd"))
         return getWindHalberd();
      if(n.equalsIgnoreCase("ame-no-nuhoko"))         //legendary weapon
         return getAmeNoNuhoko();
         
      if(n.equalsIgnoreCase("magestaff"))
         return getMageStaff();
      if(n.equalsIgnoreCase("ohnyalei"))
         return getOhnyalei();
      if(n.equalsIgnoreCase("staff"))
         return getStaff();
         
      if(n.equalsIgnoreCase("long-staff"))
         return getLongstaff();
      if(n.equalsIgnoreCase("flamestaff"))
         return getFlamestaff();
      if(n.equalsIgnoreCase("brightstaff"))
         return getBrightstaff();
      if(n.equalsIgnoreCase("windstaff"))
         return getWindstaff();
      if(n.equalsIgnoreCase("mightstaff"))
         return getMightstaff();
      if(n.equalsIgnoreCase("mindstaff"))
         return getMindstaff();
      if(n.equalsIgnoreCase("khatvanga"))          //legendary weapon
         return getKhatvanga();
      if(n.equalsIgnoreCase("bright-icestaff"))
         return getIceStaff();
         
      if(n.equalsIgnoreCase("dagger"))
         return getDagger();
      if(n.equalsIgnoreCase("bright-dagger"))
         return getBrightDagger();
      if(n.equalsIgnoreCase("swift-dagger"))
         return getSwiftDagger();
      if(n.equalsIgnoreCase("dagger-o-might"))
         return getDaggerOfMight();
      if(n.equalsIgnoreCase("dagger-o-mind"))
         return getDaggerOfMind();
      if(n.equalsIgnoreCase("carnwennan"))         //legendary weapon
         return getCarnwennan();
         
      if(n.equalsIgnoreCase("poison-dagger"))
         return getPoisonDagger();
      if(n.equalsIgnoreCase("souldagger"))
         return getSoulDagger();
      if(n.equalsIgnoreCase("banedagger"))
         return getBaneDagger();
      if(n.equalsIgnoreCase("magmadagger"))
         return getMagmaDagger();
      if(n.equalsIgnoreCase("frostdagger"))
         return getFrostDagger();
                 
      if(n.equalsIgnoreCase("royal-sceptre"))
         return getSceptre();
      if(n.equalsIgnoreCase("royal-lifesceptre"))
         return getLifeSceptre();  
      
      if(n.equalsIgnoreCase("throwing-stone"))
         return getStone();
      if(n.equalsIgnoreCase("sling"))
         return getSling();
      if(n.equalsIgnoreCase("grenade-of-antioch"))
         return getHolyHandGrenade();
             
      if(n.equalsIgnoreCase("lightningfire"))
         return getDragonBolt();
      if(n.equalsIgnoreCase("dragonfire"))
         return getDragonfire();
      if(n.equalsIgnoreCase("demonic-claw"))
         return getDemonicClaw();
      if(n.equalsIgnoreCase("ice-breath"))
         return getIceBreath();
      if(n.equalsIgnoreCase("striking-hooves"))
         return getStrikingHooves();
      if(n.equalsIgnoreCase("bright-horn"))
         return getBrightHorn();
   
      if(n.equalsIgnoreCase("bite"))
         return getBite();
      if(n.equalsIgnoreCase("burn"))
         return getBurn();         
      if(n.equalsIgnoreCase("crushing-bite"))
         return getCrushingBite();
      if(n.equalsIgnoreCase("poison-bite"))
         return getPoisonBite();
      if(n.equalsIgnoreCase("hard-poison-bite"))
         return getHardPoisionBite();
      if(n.equalsIgnoreCase("coiling-arms"))
         return getCoilingArms();
      if(n.equalsIgnoreCase("massive-coiling-arms"))
         return getMassiveCoilingArms();
      if(n.equalsIgnoreCase("fanged-bite"))
         return getFangedBite();
      if(n.equalsIgnoreCase("swirling-chaos"))
         return getSwirlingChaos();
      if(n.equalsIgnoreCase("massive-fists"))
         return getMassiveFists();
      if(n.equalsIgnoreCase("crushing-jaws"))
         return getCrushingJaws();
      if(n.equalsIgnoreCase("cannonball"))
         return getCannonball();
      if(n.equalsIgnoreCase("great-cannon"))
         return getGreatCannon();
      if(n.equalsIgnoreCase("brigand-cannon"))
         return getBrigandCannon();
                  
      if(n.equalsIgnoreCase("fists"))
         return getFists();
      if(n.equalsIgnoreCase("vampyric-bite"))    //vampire attack
         return getVampyricBite();
      if(n.equalsIgnoreCase("hard-vampyric-bite"))    //vampire attack
         return getHardVampyricBite();
         
      if(n.equalsIgnoreCase("iron-jaw-trap"))
         return getJawTrap();
      if(n.equalsIgnoreCase("thrashing-bite"))   //werewolf attack player
         return  getThrashingBite();
      if(n.equalsIgnoreCase("thrashing-jaws"))   //werewolf attack npc
         return  getThrashingJaws();
    
      if(n.equalsIgnoreCase("lute-o-destiny"))
         return getLuteOfDestiny(); 
      if(n.equalsIgnoreCase("lute"))
         return getLute();    
         
      if(n.equalsIgnoreCase("phasewall"))
         return getPhasewall();
      if(n.equalsIgnoreCase("advance"))
         return getAdvance();
      if(n.equalsIgnoreCase("curse"))
         return getCurse();
      if(n.equalsIgnoreCase("possess"))
         return getPossess();
      if(n.equalsIgnoreCase("fireball"))
         return getFireball();
      if(n.equalsIgnoreCase("spidersweb"))
         return getSpidersWeb();
      if(n.equalsIgnoreCase("icestrike"))
         return getIcestrike();
      if(n.equalsIgnoreCase("lightning"))
         return getLightning();
      if(n.equalsIgnoreCase("deathtouch"))
         return getDeathtouch();
      if(n.equalsIgnoreCase("fireshield"))
         return getFireShield();
      if(n.equalsIgnoreCase("summon-vortex"))
         return getSummonVortex();
      if(n.equalsIgnoreCase("raise-stone"))
         return getRaiseStone();
      if(n.equalsIgnoreCase("lightningbolt"))
         return getLightningbolt();
      if(n.equalsIgnoreCase("stonecast"))
         return getStonecast(); 
      if(n.equalsIgnoreCase("blindingLight"))
         return getBlindingLight();
         
      if(n.equalsIgnoreCase("gun"))
         return getGun();  
      return null;
   }

   public String toString()
   {
      String ans="";
      ans += getName()+" "+effect+" "+minDamage+" "+maxDamage+" "+range;
      for(int n: statModifier)
         ans+= n+" ";
      ans+=getValue()+" "+imageIndex+" "+reloadFrames;   
      return ans;
   }

}