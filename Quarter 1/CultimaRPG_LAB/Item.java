import java.util.ArrayList;

public class Item implements Comparable
{
   private String name;
   private int value;            //amount of gold to buy the item
   private int[] statModifier;   //how much ownership of the item effects player stats [0]-might, [1]-mind, [2]-agility
   private String description;

   public Item(String n, int v)
   {
      name = n;
      value = v;
      statModifier = new int[3];    //[0]-might, [1]-mind, [2]-agility
      description = "";
   }
   
   public Item(String n, int v, String d)
   {
      name = n;
      value = v;
      statModifier = new int[3];    //[0]-might, [1]-mind, [2]-agility
      description = d;
   }
   
   public int compareTo(Object other)
   {
      return this.getName().compareTo(((Item)other).getName());
   }

   public String getName()
   {
      return name;
   }
   
   public int getValue()
   {
      return value;
   }
   
   public void setName(String v)
   {
      name = v;
   }
   
   public void setValue(int v)
   {
      value = v;
   }
   
   public String getDescription()
   {
      if(description.length() > 0)
         return description;
      return getDescription(name);
   }
   
   public void setDescription(String d)
   {
      description = d;
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

     
   public static String getDescription(String itemName)
   {
      itemName = itemName.trim().toLowerCase();
      if(itemName.contains("arenacape"))
         return "Flowing cape worn by the Arena Champion";
      if(itemName.contains("robertsmask"))
         return "Black mask of the Dread Brigand Roberts";
      if(itemName.contains("talisman"))
         return "Control thy very weather: wind, fog and rain";   
      //****MISSION ITEMS*****/
      if(itemName.contains("message"))
         return "Private message to be delivered";
      if(itemName.contains("parcel"))
         return "Important parcel to be delivered";
      if(itemName.contains("receipt"))
         return "Note that a parcel was delivered as promised";
      if(itemName.contains("bounty"))
         return "Proof of assassination to claim payment";
      if(itemName.contains("swine-bounty"))
         return "Proof of defeating an infamous Swine player";
      if(itemName.contains("caerbannog"))
         return "The last words of Olfin Bedwee of Rheged, carved in mystic runes upon the very living rock of Caerbannog";
      if(itemName.contains("head"))
         return "The severed head of a slain enemy, proof of a worthy victory in battle";
      if(itemName.contains("tooth"))
         return "A tooth from a colossal beast, worthy of a trophy and a mighty conversation starter";  
      //**********************/    
      if(itemName.contains("floretbox"))
         return "Magic box that pulls manna from florets";
      if(itemName.contains("lockpick"))
         return "Use to disarm traps and open locked doors";
      if(itemName.contains("magicpick"))
         return "A lockpick that makes thee more nimble";
      if(itemName.contains("holdall"))
         return "Magic satchel that holds many items";
      if(itemName.contains("charmring"))
         return "Makes thee more appealing to others";
      if(itemName.contains("focushelm"))
         return "Sense around thee what the eyes cannot";
      if(itemName.contains("pentangle"))
         return "Protects thee from demons and curses";
      if(itemName.contains("mannastone"))
         return "Restores thy spiritual energy faster";
      if(itemName.contains("blessed-crown"))
         return "Adornment guarantees ascention on death";
      if(itemName.contains("life-pearl"))
         return "Brings life back to the body that lost it"; 
      if(itemName.contains("powerbands"))
         return "Gauntlets that increase thy strength"; 
      if(itemName.contains("swiftboots"))
         return "Boots that make thee more swift"; 
      if(itemName.contains("swiftquill"))
         return "Strengthens arrows to more likely recover";
      if(itemName.contains("mindtome"))
         return "Magic book that strengthens thy mind";  
      if(itemName.contains("beastbook"))
         return "A Monster Manual, if you will";
      if(itemName.contains("armsbook"))
         return "Catalog of popular arms and armor";
      if(itemName.contains("spellbook"))
         return "Manuscript of spell descriptions";
      if(itemName.contains("gossipbook"))
         return "A scathing tell-all of local townsfolk";
      if(itemName.contains("riddlebook"))
         return "Charming riddles in one binding";  
      if(itemName.contains("dogbook"))
         return "Training manual of the domestic dog";  
      if(itemName.contains("horsebook"))
         return "Instructional volume on taming horses";  
      if(itemName.contains("shipbook"))
         return "Book of sailing and battling on the seas"; 
      if(itemName.contains("vampirebook"))
         return "Bound telling of the Vampyric curses";  
      if(itemName.contains("wolfenbook"))
         return "Tale of one who runs among the Wolfen";
      if(itemName.contains("crafting-manual"))
         return "Instructions on crafting arms with gems";
      if(itemName.contains("tax-ledger"))
         return "The missing tax logs of the city Taxman";
      if(itemName.contains("3-wells-puzzle") || itemName.contains("3-doors-puzzle") || itemName.contains("3-towers-puzzle"))
         return "Details the rules of a tricky puzzle";
      if(itemName.contains("songpage"))
         return "Rare scribed score of songs of Skara Brae";
      if(itemName.contains("songbook"))
         return "Catalogue of common songs requested in the pubs of Cultima";
      if(itemName.contains("loaded-cube"))
         return "Unfairly weighted cube for games of Swine";
      if(itemName.contains("swine-cube"))
         return "Properly weighted cube for games of Swine";
      if(itemName.contains("demons-cube"))
         return "Magic number cube for games of Swine";
      if(itemName.contains("moonjem"))
         return "Precious stone that casts light";
      if(itemName.contains("icejem"))
         return "Translucent stone cold to the touch";
      if(itemName.contains("flamejem"))
         return "Glowing glass that radiates fire";
      if(itemName.contains("ruby"))
         return "Reddened stone as a symbol of might";
      if(itemName.contains("jade"))
         return "Green stone as a symbol of mind";
      if(itemName.contains("azurite"))
         return "Blue stone as a symbol of swiftness";
      if(itemName.contains("treasuremap"))
         return "X marks the spot of a hidden treasure";
      if(itemName.contains("dragon-egg"))
         return "Incubator for an embryonic dragon";
      if(itemName.contains("serpent-egg"))
         return "Could be used for a poison's cure";
      if(itemName.contains("wisdom-egg"))
         return "Strange fermented egg from another realm";
      if(itemName.contains("allosaur-egg"))
         return "Strange large egg from a slain beast";
      if(itemName.contains("clawed-glove"))
         return "Make thy fist strike as of an iron clawed beast";
      if(itemName.contains("viperglove"))
         return "Deliver poison with the touch of a greeting";
      if(itemName.contains("iron-bracer"))
         return "Arm band that strengthens thy fist strikes";
      if(itemName.contains("bow-bracer"))
         return "Arm wrapping to augment thy bowmanship";
      if(itemName.contains("sling"))
         return "Throw stones faster and further with a sling, along with a leather satchel to hold stones";
      if(itemName.contains("lumber"))
         return "Wood from a felled tree, good for crafting arrows or building bridges";
      return itemName;
   }
   
   public static Item getWisdomEgg()
   {
      String d = "Strange fermented egg from another realm that augments thy very mind when held close, and may reveal all knowledge of Cultima if eaten";
      Item it = new Item("wisdom-egg", 1000, d);
      int [] statM = {0,1,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getAllosaurEgg()
   {
      String d = "Strange large egg from the innards of a massive reptilian beast";
      Item it = new Item("allosaur-egg", 250, d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getDragonEgg()
   {
      String d = "An incubator for an embryonic dragon - might augment thy might if eaten";
      Item it = new Item("dragon-egg", 500, d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getSerpentEgg()
   {
      String d = "A small leathery serpent egg that might be used for a poison's cure if eaten";
      Item it = new Item("serpent-egg", 30,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }

   public static Item getRobertsMask()
   {
      String d = "The black mask of the Dread Brigand Roberts - will allow thee to pass among the briggands and augments thy might";
      Item it = new Item("robertsmask", 1000,d);
      int [] statM = {1,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getFloretBox()
   {
      String d = "A magic wooden box that pulls manna from florets stored within it - when full will allow thee to cast the Floretlade spell";
      Item it = new Item("floretbox", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }

   public static Item getArenaCape()
   {
      String d = "A flowing cape of red and gold that is worn by the Arena Champion - magically augments thy strength and agility";
      Item it = new Item("arenacape", 5000,d);
      int [] statM = {1,0,1};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getHoldall()
   {
      String d = "A leathery magic satchel with more room within than its outer surface. One can carry many items without adding weight or size";
      Item it = new Item("holdall", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getTalisman()
   {
      String d = "A small, ancient wooden carving hanging upon a leather band that allows thee to control thy very weather: wind, fog and rain";
      Item it = new Item("talisman", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getCharmring()
   {
      String d = "A garish but attractive bronze ring that makes thee more appealing to others";
      Item it = new Item("charmring", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getFocushelm()
   {
      String d = "Light and comfortable helmet that enhances thy senses of the world around thee to better see what the eyes cannot";
      Item it = new Item("focushelm", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getPentangle()
   {
      String d = "An intricate rounded disc of blackend metal, hanging upon a leather neckband that protects thee from demons and curses";
      Item it = new Item("pentangle", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getMannastone()
   {
      String d = "A smooth stone with carvings of ancient symbols on its flat side that restores thy spiritual energy faster";
      Item it = new Item("mannastone", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getLockpick()
   {
      String d = "An intricate tool used to disarm traps and open locked chests and doors. Its chance of success relies on thy agility";
      Item it = new Item("lockpick", 10,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getMagicPick()
   {
      String d = "A finely crafted lockpick that makes thee more nimble and betters thy chances of picking even the most clever of locks";
      Item it = new Item("magicpick", 4000,d);
      int [] statM = {0,0,1};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getBlessedCrown()
   {
      String d = "A glowing golden crown, ornate with gems that blesses its wearer and guarantees ascention on death";
      Item it = new Item("blessed-crown", 4000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getLifePearl()
   {
      String d = "A glowing pearl from the gullet of the most rare of sea creatures that can bring life back to the body that lost it";
      Item it = new Item("life-pearl", 8000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getPowerBands()
   {
      String d = "Band of leather hewn with reddened pipings - augments strength when wrapped around thy arm. Will further a magic weapons qualities when wraped 'round the hilt";
      Item it = new Item("powerbands", 4000,d);
      int [] statM = {2,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getSwiftBoots()
   {
      String d = "Finely crafted leather boots hewn with magic blue pipings. Wearing them makes thee feel lighter and augments thy agility";
      Item it = new Item("swiftboots", 4000,d);
      int [] statM = {0,0,2};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getMindTome()
   {
      String d = "A small ancient magic book adorned with symbols that strengthens thy very mind when held close";
      Item it = new Item("mindtome", 4000,d);
      int [] statM = {0,2,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getSwineCube()
   {
      String d = "A properly weighted 6-sided cube for games of Swine - equal chance to land with its one-side facing upwards";
      Item it = new Item("swine-cube", 1,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getLoadedCube()
   {
      String d = "An unfairly weighted cube for games of Swine - slightly less likely to land with its one-side facing upwards";
      Item it = new Item("loaded-cube", 500,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getDemonsCube()
   {
      String d = "A reddened magic number cube for games of Swine - less likely to land with its one-side facing upwards";
      Item it = new Item("demons-cube", 5000,d);
      int [] statM = {0,1,1};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getMoonjem()
   {
      String d = "A precious rounded stone that glows as if from a full moon - can be crafted to fine weapons to make them cast light where there is none";
      Item it = new Item("moonjem", 500,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getRuby()
   {
      String d = "An angled reddened stone known as a symbol of might - can be crafted to fine weapons to augment thy strength";
      Item it = new Item("ruby", 500,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }

   public static Item getJade()
   {
      String d = "A smooth green stone known as a symbol of mind - can be crafted to fine weapons to augment thy spellcraft";
      Item it = new Item("jade", 500,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }

   public static Item getAzurite()
   {
      String d = "A polished blue stone known as a symbol of swiftness - can be crafted to fine weapons to augment thy agility";
      Item it = new Item("azurite", 500,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getFlamejem()
   {
      String d = "A glowing orange glass that radiates fire - can be crafted to fine weapons to burn with magic flame";
      Item it = new Item("flamejem", 500,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getIcejem()
   {
      String d = "A translucent milky stone cold to the touch - can be crafted to fine weapons to cast a magic frost";
      Item it = new Item("icejem", 500,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getSwiftQuill()
   {
      String d = "A magic arrow quill that increases arrow capactiy and strengthens arrows to improve chances of recovery";
      Item it = new Item("swiftquill", 5000,d);
      int [] statM = {0,0,1};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getBowBracer()
   {
      String d = "A sturdy leather arm wrapping to augment thy bowmanship - more range from shortbows and crossbows and better chance of arrow recovery";
      Item it = new Item("bow-bracer", 500,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getIronBracer()
   {
      String d = "A fitted iron arm band that strengthens thy fist strikes and improves thy armor protection";
      Item it = new Item("iron-bracer", 280,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getViperGlove()
   {
      String d = "A green sliken glove that can quietly deliver a poison with the touch of a greeting";
      Item it = new Item("viperglove", 3000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getClawedGlove()
   {
      String d = "A metalic glove with talons extending from the knuckles - make thy fist strike as if from an iron clawed beast";
      Item it = new Item("clawed-glove", 390,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getSling()
   {
      String d = "Throw stones faster and further with a sling, along with a leather satchel to hold stones";
      Item it = new Item("sling", 5,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getDragonScales()
   {
      String d = "The rough, reddened scales of a dragon, strong but light in weight. Impervious to flame, they could be crafted into fine armor";
      Item it = new Item("dragon-scales", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getDragonQueenScales()
   {
      String d = "The rough, black scales from the Dragon Queen, strong but light in weight. Impervious to flame, they could be crafted into the finest armor";
      Item it = new Item("dragonqueen-scales", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getSeaserpentScales()
   {
      String d = "The shimmering cyan scales of a sea-serpent, strong but light in weight. Impervious to cold, they could be crafted into fine armor";
      Item it = new Item("seaserpent-scales", 1000,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }
   
   public static Item getLumber()
   {
      String d = "A fine piece of wood from a felled tree, good for crafting arrows or building bidges";
      Item it = new Item("lumber", 1,d);
      int [] statM = {0,0,0};
      it.setStatMod(statM);
      return it;
   }

   //returns true if this is an item critical for completing a mission
   public boolean isMissionItem()
   {
      String itemName = this.getName().toLowerCase();
      return (itemName.contains("message") || itemName.contains("parcel") || itemName.contains("bounty")
           || itemName.contains("swine-bounty") || itemName.contains("caerbannog"));
   }

   //post:returns true if the item with the name n is made of metal
   public static boolean isMetal(String n)
   {
      n = n.toLowerCase().trim();
      return (n.contains("charmring") || n.contains("focushelm") || n.contains("pentangle") 
           || n.contains("lockpick")  || n.contains("magicpick") || n.contains("clawed-glove") || n.contains("iron-bracer"));
   }

   //pre: n!=null, n is the name of a valid item
   //post:returns the item with the name n
   public static Item getItemWithName(String n)
   {
      n = n.toLowerCase().trim();
      if(n.contains("dragonqueen-scales"))
         return getDragonQueenScales();
      if(n.contains("dragon-scales"))
         return getDragonScales();
      if(n.contains("seaserpent-scales"))
         return getSeaserpentScales();
      if(n.contains("clawed-glove"))
         return getClawedGlove();
      if(n.contains("viperglove"))
         return getViperGlove();
      if(n.contains("iron-bracer"))
         return getIronBracer();
      if(n.contains("bow-bracer"))
         return getBowBracer();
      if(n.contains("sling"))
         return getSling();
      if(n.contains("allosaur-egg"))
         return getAllosaurEgg();
      if(n.contains("wisdom-egg"))
         return getWisdomEgg();
      if(n.contains("dragon-egg"))
         return getDragonEgg();
      if(n.contains("serpent-egg"))
         return getSerpentEgg();
      if(n.contains("arenacape"))
         return getArenaCape();
      if(n.contains("robertsmask"))
         return getRobertsMask();
      if(n.contains("floretbox"))
         return getFloretBox();
      if(n.contains("holdall"))
         return getHoldall();
      if(n.contains("talisman"))
         return getTalisman();
      if(n.contains("charmring"))
         return getCharmring();
      if(n.contains("focushelm"))
         return getFocushelm();
      if(n.contains("pentangle"))
         return getPentangle();
      if(n.contains("mannastone"))
         return getMannastone();
      if(n.contains("lockpick"))
         return getLockpick();
      if(n.contains("magicpick"))
         return getMagicPick();
      if(n.contains("blessed-crown"))  
         return getBlessedCrown(); 
      if(n.contains("life-pearl"))
         return getLifePearl();  
      if(n.contains("powerbands"))
         return getPowerBands(); 
      if(n.contains("swiftboots"))
         return getSwiftBoots(); 
      if(n.contains("mindtome"))
         return getMindTome(); 
      if(n.contains("swiftquill"))
         return getSwiftQuill();
      if(n.contains("swine-cube"))
         return getSwineCube();
      if(n.contains("loaded-cube"))
         return getLoadedCube(); 
      if(n.contains("demons-cube"))
         return getDemonsCube(); 
      if(n.contains("moonjem"))
         return getMoonjem();
      if(n.contains("ruby"))
         return getRuby();
      if(n.contains("jade"))
         return getJade();
      if(n.contains("azurite"))
         return getAzurite();
      if(n.contains("flamejem"))
         return getFlamejem();
      if(n.contains("icejem"))
         return getIcejem();
      if(n.contains("beastbook"))
         return new Item("beastbook", 20);
      if(n.contains("gossipbook"))
         return new Item("gossipbook", 20);
      if(n.contains("dogbook"))
         return new Item("dogbook", 20);
      if(n.contains("horsebook"))
         return new Item("horsebook", 20);
      if(n.contains("shipbook"))
         return new Item("shipbook", 20);
      if(n.contains("vampirebook"))
         return new Item("vampirebook", 20);
      if(n.contains("wolfenbook"))
         return new Item("wolfenbook", 20);
      if(n.contains("crafting-manual"))
         return new Item("crafting-manual", 200);
      if(n.contains("songpage"))
         return new Item("songpage", 500, getDescription("songpage"));
      if(n.contains("songbook"))
         return new Item("songbook", 25, getDescription("songbook"));
      if(n.contains("3-wells-puzzle"))
         return get3WellsBook();
      if(n.contains("3-doors-puzzle"))
         return get3DoorsBook();
      if(n.contains("3-towers-puzzle"))
         return get3TowersBook();
      if(n.contains("lumber"))
         return getLumber();
      return new Item(n, 0);
   }
   
   public String toString()
   {
      return name+":"+value;
   }

   public static Item getRandomItemFull()
   {
      ArrayList<Item> items = new ArrayList<Item>();
      items.add(getFloretBox());
      items.add(getHoldall());
      items.add(getTalisman());
      items.add(getCharmring());
      items.add(getFocushelm());
      items.add(getPentangle());
      items.add(getMannastone());
      items.add(getLockpick());
      items.add(getMagicPick());
      //items.add(getBlessedCrown()); 
      items.add(getLifePearl());
      items.add(getPowerBands());
      items.add(getSwiftBoots());   
      items.add(getMindTome());   
      items.add(getLoadedCube());
      items.add(getDemonsCube());
      items.add(getMoonjem());
      items.add(getRuby());
      items.add(getJade());
      items.add(getAzurite());
      items.add(getFlamejem());
      items.add(getIcejem());
      items.add(getSwiftQuill());
      items.add(getClawedGlove());
      items.add(getViperGlove());
      items.add(getIronBracer());
      items.add(getBowBracer());
      items.add(getSling());
   
      for(byte i=0; i<Potion.NUM_POTIONS; i++)
         items.add(Potion.getPotionWithNum(i));
      return Utilities.getRandomFrom(items);
   }
   
   public static Item getRandomMagicItem()
   {
      ArrayList<Item> items = new ArrayList<Item>();
      items.add(getTalisman());
      items.add(getCharmring());
      items.add(getFocushelm());
      items.add(getPentangle());
      items.add(getMannastone());
      items.add(getMagicPick());
      items.add(getLifePearl());
      items.add(getPowerBands());
      items.add(getSwiftBoots());   
      items.add(getMindTome()); 
      items.add(getSwiftQuill());
      items.add(getDemonsCube());
      items.add(getMoonjem());
      items.add(getRuby());
      items.add(getJade());
      items.add(getAzurite());
      items.add(getFlamejem());
      items.add(getIcejem());
      return Utilities.getRandomFrom(items);
   }
   
   public static Item getRandomMagicItemPartial()
   {
      ArrayList<Item> items = new ArrayList<Item>();
      items.add(getCharmring());
      items.add(getFocushelm());
      items.add(getPentangle());
      items.add(getMannastone());
      items.add(getMagicPick());
      items.add(getLifePearl());
      items.add(getPowerBands());
      items.add(getSwiftBoots());
      items.add(getSwiftQuill());   
      items.add(getMindTome()); 
      items.add(getDemonsCube());
      items.add(getTalisman());
      return Utilities.getRandomFrom(items);
   }
   
   public static Item getRandomMagicItemSimple()
   {
      ArrayList<Item> items = new ArrayList<Item>();
      items.add(getCharmring());
      items.add(getFocushelm());
      items.add(getPentangle());
      items.add(getMannastone());
      items.add(getPowerBands());
      items.add(getSwiftBoots());
      items.add(getSwiftQuill());   
      items.add(getMindTome());
      items.add(getTalisman()); 
      return Utilities.getRandomFrom(items);
   }
   
   public static Item getRandomStone()
   {
      ArrayList<Item> items = new ArrayList<Item>();
      items.add(getMoonjem());
      items.add(getRuby());
      items.add(getJade());
      items.add(getAzurite());
      items.add(getFlamejem());
      items.add(getIcejem());
      return Utilities.getRandomFrom(items);
   }
   
   public static Item getRandomMiscItem()
   {
      ArrayList<Item> items = new ArrayList<Item>();
      items.add(getFloretBox());
      items.add(getHoldall());
      items.add(getTalisman());
      items.add(getCharmring());
      items.add(getFocushelm());
      items.add(getPentangle());
      items.add(getMannastone());
      items.add(getLockpick());
      items.add(getMagicPick());
      items.add(getLifePearl());
      items.add(getPowerBands());
      items.add(getSwiftBoots());   
      items.add(getMindTome());   
      items.add(getLoadedCube());
      items.add(getDemonsCube());
      items.add(getSwiftQuill());
      items.add(getClawedGlove());
      items.add(getViperGlove());
      items.add(getIronBracer());
      items.add(getBowBracer());        
      items.add(getDragonQueenScales());
      items.add(getDragonScales());
      items.add(getSeaserpentScales());
      items.add(getAllosaurEgg());
      items.add(getWisdomEgg());
      items.add(getDragonEgg());
      items.add(getSerpentEgg());
      items.add(getRobertsMask());
      items.add(getSling());
      items.add(new Item("songpage", 500, getDescription("songpage")));
      return Utilities.getRandomFrom(items);
   }

   public static Item getRandomMagicWeapon()
   { 
      ArrayList<Item> weaponSet = new ArrayList<Item>();
     
      weaponSet.add(Weapon.getBaneDagger());
      weaponSet.add(Weapon.getSoulDagger());
      weaponSet.add(Weapon.getPoisonDagger());
      weaponSet.add(Weapon.getMagmaDagger());
      weaponSet.add(Weapon.getFrostDagger());
      
      weaponSet.add(Weapon.getFlamestaff());
      weaponSet.add(Weapon.getBrightstaff());
      weaponSet.add(Weapon.getWindstaff());
      weaponSet.add(Weapon.getMightstaff());
      weaponSet.add(Weapon.getMindstaff()); 
      weaponSet.add(Weapon.getIceStaff());  
      
      weaponSet.add(Weapon.getOhnyalei());
      weaponSet.add(Weapon.getMageStaff());
      weaponSet.add(Weapon.getHolyHandGrenade());
      
      weaponSet.add(Weapon.getHalberdOfFire());
      weaponSet.add(Weapon.getHalberdOfFrost());
      weaponSet.add(Weapon.getBrightHalberd());
      weaponSet.add(Weapon.getMightHalberd());
      weaponSet.add(Weapon.getWindHalberd());
      
      weaponSet.add(Weapon.getFlameSpear());
      weaponSet.add(Weapon.getFrostSpear()); 
      weaponSet.add(Weapon.getBrightSpear()); 
      weaponSet.add(Weapon.getMightSpear()); 
      weaponSet.add(Weapon.getMindSpear()); 
      weaponSet.add(Weapon.getWindSpear());
      
      weaponSet.add(Weapon.getBrightLongbow());
      weaponSet.add(Weapon.getMightLongbow());
      weaponSet.add(Weapon.getMindLongbow());
      weaponSet.add(Weapon.getWindLongbow());
      
      weaponSet.add(Weapon.getFlamebow());
      weaponSet.add(Weapon.getFrostbow());
      weaponSet.add(Weapon.getBrightBow());
      weaponSet.add(Weapon.getMightBow());
      weaponSet.add(Weapon.getMindBow());
      weaponSet.add(Weapon.getWindBow());
      
      weaponSet.add(Weapon.getSoulCrossbow());
      weaponSet.add(Weapon.getPoisonBoltcaster());
      weaponSet.add(Weapon.getBaneBoltcaster());
      weaponSet.add(Weapon.getFlamecaster());
      weaponSet.add(Weapon.getFrostcaster());
      weaponSet.add(Weapon.getBrightcaster());
      weaponSet.add(Weapon.getMightcaster());
      weaponSet.add(Weapon.getMindcaster());
      weaponSet.add(Weapon.getWindcaster());
      
      weaponSet.add(Weapon.getDualFlameblades());
      weaponSet.add(Weapon.getDualFrostblades());
      weaponSet.add(Weapon.getBrightSwords());
      weaponSet.add(Weapon.getSwordsOfMind());
      weaponSet.add(Weapon.getSwordsOfMight());
      weaponSet.add(Weapon.getWindSwords());
   
      weaponSet.add(Weapon.getFlameblade());
      weaponSet.add(Weapon.getFrostblade());
      weaponSet.add(Weapon.getBrightSword());
      weaponSet.add(Weapon.getSwiftSword());
      weaponSet.add(Weapon.getSwordOfMight());
      weaponSet.add(Weapon.getSwordOfMind());
      weaponSet.add(Weapon.getBlessedSwiftblade());
      weaponSet.add(Weapon.getBlessedMightblade());
      weaponSet.add(Weapon.getBlessedMindblade());
   
      weaponSet.add(Weapon.getExoticFrostHammer());
      weaponSet.add(Weapon.getExoticHammerOfFire());
      weaponSet.add(Weapon.getBrightExoticHammer());
      weaponSet.add(Weapon.getMightExoticHammer());
      weaponSet.add(Weapon.getMindExoticHammer());
      weaponSet.add(Weapon.getSwiftExoticHammer());
      
      weaponSet.add(Weapon.getSpikedFrostHammer());
      weaponSet.add(Weapon.getSpikedHammerOfFire());
      weaponSet.add(Weapon.getBrightSpikedHammer());
      weaponSet.add(Weapon.getMightSpikedHammer());
      weaponSet.add(Weapon.getMindSpikedHammer());
      weaponSet.add(Weapon.getSwiftSpikedHammer());
   
      weaponSet.add(Weapon.getFlameHammer());
      weaponSet.add(Weapon.getFrostHammer());
      weaponSet.add(Weapon.getBrightHammer());
      weaponSet.add(Weapon.getSwiftHammer());
      weaponSet.add(Weapon.getHammerOfMight());
      weaponSet.add(Weapon.getHammerOfMind());
   
      weaponSet.add(Weapon.getFlamebladeMirrorshield());
      weaponSet.add(Weapon.getFrostbladeMirrorshield());
      weaponSet.add(Weapon.getBrightSwordMirrorshield());
      weaponSet.add(Weapon.getMightSwordMirrorshield());
      weaponSet.add(Weapon.getMindSwordMirrorshield());
      weaponSet.add(Weapon.getSwiftSwordMirrorshield());
   
      weaponSet.add(Weapon.getFlamebladeBuckler());
      weaponSet.add(Weapon.getFrostbladeBuckler());
      weaponSet.add(Weapon.getBrightSwordBuckler());
      weaponSet.add(Weapon.getSwiftSwordBuckler());
      weaponSet.add(Weapon.getSwordBucklerOfMight());
      weaponSet.add(Weapon.getSwordBucklerOfMind());
   
      weaponSet.add(Weapon.getDualAxeOfFire());
      weaponSet.add(Weapon.getDualAxeOfFrost());
      weaponSet.add(Weapon.getBrightDualAxe());
      weaponSet.add(Weapon.getDualAxeOfMight());
      weaponSet.add(Weapon.getDualAxeOfMind());
      weaponSet.add(Weapon.getSwiftDualAxe());
      weaponSet.add(Weapon.getVampyricAxes());
   
      weaponSet.add(Weapon.getFlameMirrorAxe());
      weaponSet.add(Weapon.getFrostMirrorAxe());
      weaponSet.add(Weapon.getBrightMirrorAxe());
      weaponSet.add(Weapon.getMightMirrorAxe());
      weaponSet.add(Weapon.getMindMirrorAxe());
      weaponSet.add(Weapon.getSwiftMirrorAxe());
      
      weaponSet.add(Weapon.getFlameAxe());
      weaponSet.add(Weapon.getFrostAxe());
      weaponSet.add(Weapon.getAxeOfMight());
      weaponSet.add(Weapon.getBrightAxe());
      weaponSet.add(Weapon.getAxeOfMind());
      weaponSet.add(Weapon.getSwiftAxe());
      
      weaponSet.add(Weapon.getToothedTorchOfMight());
      weaponSet.add(Weapon.getToothedTorchOfMind());
      weaponSet.add(Weapon.getSwiftToothedTorch());
   
      return Utilities.getRandomFrom(weaponSet);
   }
   
   public static Item getRandomScales()
   {
      ArrayList<Item> items = new ArrayList<Item>();
      items.add(getDragonQueenScales());
      items.add(getDragonScales());
      items.add(getSeaserpentScales());
      return Utilities.getRandomFrom(items);
   }

   public static Item getRandomBook()
   {
      int [] values = {20,20,20,20,20,20,40,40,40,100,200};
      int value = values[(int)(Math.random()*values.length)];
      int whichBook = Utilities.rollDie(1,10);
      if(whichBook == 1)
         return new Item("beastbook", value);
      if(whichBook == 2)
         return new Item("beastbook", value);         
      if(whichBook == 3)
         return new Item("gossipbook", value);
      if(whichBook == 4)
         return new Item("gossipbook", value);
      if(whichBook == 5)
         return new Item("gossipbook", value);
      if(whichBook == 6)
         return new Item("dogbook", value);
      if(whichBook == 7)
         return new Item("horsebook", value);
      if(whichBook == 8)
         return new Item("shipbook", value);
      if(whichBook == 9)
         return new Item("vampirebook", value);
      if(whichBook == 10)
         return new Item("wolfenbook", value);
      return new Item("beastbook", 20);
   }
   
   public static Item get3WellsBook()
   {
      return new Item("3-Wells-Puzzle", 100);
   }
   
   public static Item get3DoorsBook()
   {
      return new Item("3-Doors-Puzzle", 100);
   }
   
   public static Item get3TowersBook()
   {
      return new Item("3-Towers-Puzzle", 100);
   }
   
   public static Item getCraftingManual()
   {
      return new Item("crafting-manual", 200, "Complete manual on weapon crafting to supplement thy journal");
   }
   
   public static Item getRandomBookAll()
   {
      int [] values = {20,20,20,20,20,20,40,40,40,100,100,200};
      int value = values[(int)(Math.random()*values.length)];
      int whichBook = Utilities.rollDie(1,13);
      if(whichBook == 1)
         return new Item("beastbook", value);
      if(whichBook == 2)
         return new Item("gossipbook", value);
      if(whichBook == 3)
         return new Item("dogbook", value);
      if(whichBook == 4)
         return new Item("horsebook", value);
      if(whichBook == 5)
         return new Item("shipbook", value);
      if(whichBook == 6)
         return new Item("vampirebook", value);
      if(whichBook == 7)
         return new Item("wolfenbook", value);
      if(whichBook == 8)   
         return getCraftingManual();
      if(whichBook == 9)   
         return get3TowersBook();
      if(whichBook == 10)   
         return get3DoorsBook();
      if(whichBook == 11)   
         return get3WellsBook();
      if(whichBook == 12)   
         return (new Item("armsbook", value, "A complete manual of arms and armor to supplement thy journal"));
      if(whichBook == 13)   
         return (new Item("spellbook", value, "A complete manual of spellcraft to supplement thy journal"));
      return new Item("beastbook", value);
   }

   public static boolean doWeHaveBook(Item book)
   {
      String itemName = book.getName().toLowerCase();
      if(itemName.contains("beastbook"))
      {
         return (CultimaPanel.player.getMonsterInfo() >= NPC.monsters.length);
      }
      if(itemName.contains("armsbook"))
      {
         return(CultimaPanel.player.getArmsInfo() >= NPC.armsInfo.length);
      }
      if(itemName.contains("spellbook"))
      {
         return(CultimaPanel.player.getSpellIndex() >= NPC.spellInfo.length);
      }
      if(itemName.contains("gossipbook"))
      {
         return(CultimaPanel.player.getTavernInfo() >= NPC.tavernTown.length);
      }
      if(itemName.contains("riddlebook"))
      {
         return(CultimaPanel.player.getRiddleInfo() >= NPC.riddleQst.length);
      }
      if(itemName.contains("dogbook"))
      {
         return(CultimaPanel.player.getDogInfo() >= NPC.dogInfo.length);
      }
      if(itemName.contains("horsebook"))
      {
         return(CultimaPanel.player.getHorseInfo() >= NPC.horseInfo.length);
      }
      if(itemName.contains("shipbook"))
      {
         return(CultimaPanel.player.getShipInfo() >= NPC.shipInfo.length);
      }
      if(itemName.contains("vampirebook"))
      {
         return(CultimaPanel.player.getVampireInfo() >= NPC.vampireInfo.length);
      }
      if(itemName.contains("wolfenbook"))
      {
         return(CultimaPanel.player.getWerewolfInfo() >= NPC.werewolfInfo.length);
      }   
      if(itemName.contains("3-wells-puzzle"))
      {
         return(CultimaPanel.player.getWellsPuzzleInfo() >= NPC.wellsPuzzleInfo.length);
      } 
      if(itemName.contains("3-doors-puzzle"))
      {
         return(CultimaPanel.player.getDoorsPuzzleInfo() >= NPC.doorsPuzzleInfo.length);
      }
      if(itemName.contains("3-towers-puzzle"))
      {
         int journalNum = CultimaPanel.player.getTowersPuzzleInfo();
         int numItems = NPC.towersPuzzleInfo.length;
         return(journalNum >= numItems);
      }
      return false;   
   }

   public boolean isBook()
   {
      String itemName = this.getName().toLowerCase();
      return (itemName.contains("book") || itemName.contains("puzzle"));
   }
   
   //returns the index of where target is found in list
   public static int findItem(ArrayList<Item>list, Item target)
   {
      for(int i=0; i<list.size(); i++)
         if(target != null && list.get(i)!= null && target.getName().equalsIgnoreCase(list.get(i).getName()))
            return i;
      return -1;
   }
   
   public static void sortItemArrays(ArrayList<Item> list, ArrayList<Integer> frequencies)
   {
      ArrayList<Item> weapons = new ArrayList<Item>();
      ArrayList<Item> armor = new ArrayList<Item>();
      ArrayList<Item> potions = new ArrayList<Item>();
      ArrayList<Item> stones = new ArrayList<Item>();
      ArrayList<Item> items = new ArrayList<Item>();
      
      ArrayList<Integer> weaponFreq = new ArrayList<Integer>();
      ArrayList<Integer> armorFreq = new ArrayList<Integer>();
      ArrayList<Integer> potionFreq = new ArrayList<Integer>();
      ArrayList<Integer> stoneFreq = new ArrayList<Integer>();
      ArrayList<Integer> itemFreq = new ArrayList<Integer>();
      int holdAllFreq = 0;    //see if we have a holdall. If so, make sure it is at the front of the wardrobe list
      for(int i=0; i<list.size() && i<frequencies.size(); i++)
      {
         Item curr = list.get(i);
         Integer currFreq = frequencies.get(i);
         if(curr instanceof Weapon)
         {
            weapons.add(curr);
            weaponFreq.add(currFreq);
         }
         else if(curr instanceof Armor)
         {
            armor.add(curr);
            armorFreq.add(currFreq);
         }
         else if(curr instanceof Potion)
         {
            potions.add(curr);
            potionFreq.add(currFreq);
         }
         else if(itemIsStone(curr.getName()))
         {
            stones.add(curr);
            stoneFreq.add(currFreq);
         }
         else
         {
            if(curr.getName().toLowerCase().contains("holdall"))
            {
               holdAllFreq = currFreq;
            }
            else
            {
               items.add(curr);
               itemFreq.add(currFreq);
            }
         }
      }
      list.clear();
      frequencies.clear();
      if(holdAllFreq > 0)
      {
         list.add(getHoldall());
         frequencies.add(holdAllFreq);
      }
      for(int i=0; i<weapons.size() && i<weaponFreq.size(); i++)
      {
         list.add(weapons.get(i));
         frequencies.add(weaponFreq.get(i));
      }
      for(int i=0; i<armor.size() && i<armorFreq.size(); i++)
      {
         list.add(armor.get(i));
         frequencies.add(armorFreq.get(i));
      }
      for(int i=0; i<potions.size() && i<potionFreq.size(); i++)
      {
         list.add(potions.get(i));
         frequencies.add(potionFreq.get(i));
      }
      for(int i=0; i<stones.size() && i<stoneFreq.size(); i++)
      {
         list.add(stones.get(i));
         frequencies.add(stoneFreq.get(i));
      }
      for(int i=0; i<items.size() && i<itemFreq.size(); i++)
      {
         list.add(items.get(i));
         frequencies.add(itemFreq.get(i));
      }
   }
   
   public static void sortItems(ArrayList<String> list)
   {
      ArrayList<String> stones = new ArrayList<String>();
      ArrayList<String> maps = new ArrayList<String>();
      ArrayList<String> magicItems = new ArrayList<String>();
      ArrayList<String> picks = new ArrayList<String>(); 
      ArrayList<String> books = new ArrayList<String>();
      
      for(int i=0; i<list.size(); i++)
      {
         String curr = list.get(i);
         String [] parts = curr.split(":");
         if(parts.length < 0)
            continue;
         String name = parts[0].toLowerCase();
         if(itemIsStone(name))
            stones.add(curr);
         else if(curr.contains("treasure"))
            maps.add(curr);
         else if(curr.contains("book"))
            books.add(curr);
         else if(curr.contains("pick"))
            picks.add(curr);
         else
            magicItems.add(curr);
      }
      list.clear();
      for(int i=0; i<stones.size(); i++)
         list.add(stones.get(i));
      for(int i=0; i<maps.size(); i++)
         list.add(maps.get(i));
      for(int i=0; i<magicItems.size(); i++)
         list.add(magicItems.get(i));
      for(int i=0; i<picks.size(); i++)
         list.add(picks.get(i));
      for(int i=0; i<books.size(); i++)
         list.add(books.get(i));
   }

   
   public static boolean itemIsStone(String itemName)
   {
      itemName = itemName.toLowerCase();
      return itemName.contains("life-pearl") || itemName.contains("mannastone") || itemName.contains("moonjem") || itemName.contains("ruby") || itemName.contains("jade") || itemName.contains("azurite") || itemName.contains("flamejem") || itemName.contains("icejem");
   }
   
   public static boolean weaponIsUpgradable(String weaponName)
   {
      weaponName = weaponName.toLowerCase();
      return (
         weaponName.equals("royal-sword-shield")   || weaponName.equals("royal-sceptre") || 
         weaponName.equals("axe")            || weaponName.equals("dualaxe")  || weaponName.equals("mirrored-axe")      ||
         weaponName.equals("hammer")         || weaponName.equals("sword")    || weaponName.equals("sword-mirrorshield")||
         weaponName.equals("sword-buckler")  || weaponName.equals("dagger")   || weaponName.equals("exotic-hammer")     ||      
         weaponName.equals("short-swords")   || weaponName.equals("crossbow") || weaponName.equals("spiked-hammer")   ||
         weaponName.equals("bow")            || weaponName.equals("longbow")  || weaponName.equals("blessed-sword")   ||
         weaponName.equals("spear")          || weaponName.equals("halberd")  ||
         weaponName.equals("long-staff")     || weaponName.equals("staff")    ||
         weaponName.equals("toothed-torch"));
   }
   
   public static boolean weaponIsDisassembleable(String weaponName)
   {
      weaponName = weaponName.toLowerCase();
      if(weaponName.contains("bright-horn"))
         return false;
      return !Weapon.isSpell(weaponName) && (weaponName.contains("might") || weaponName.contains("mind") || weaponName.contains("swift") 
          || weaponName.contains("frost") || weaponName.contains("flame") ||  weaponName.contains("bright")
          ||  weaponName.contains("wind") ||  weaponName.contains("fire") ||  weaponName.contains("magma")
          ||  weaponName.contains("life"));
   }
   
   public static void disassembleWeapon()
   {
      String weaponName = CultimaPanel.player.getWeapon().getName().toLowerCase();
      if(!weaponIsDisassembleable(weaponName))
         return;
     
      CultimaPanel.player.discardCurrentWeapon();     //weapon breaks removing item
     
      Item retVal = null;
      if(weaponName.contains("might"))
         retVal =  getRuby();
      else if(weaponName.contains("mind"))
         retVal =  getJade();
      else if(weaponName.contains("swift") || weaponName.contains("wind"))
         retVal =  getAzurite();
      else if(weaponName.contains("flame") || weaponName.contains("fire") || weaponName.contains("magma"))
         retVal =  getFlamejem();
      else if(weaponName.contains("frost"))
         retVal =  getIcejem();
      else if(weaponName.contains("bright"))
         retVal =  getMoonjem();
      else if(weaponName.contains("life"))
         retVal =  getLifePearl();
      if(retVal != null)
      {
         if(Utilities.startsWithVowel(retVal.getName()))
            CultimaPanel.sendMessage("You extracted an "+retVal.getName()+"!");
         else 
            CultimaPanel.sendMessage("You extracted a "+retVal.getName()+"!");
         CultimaPanel.player.addItem(retVal.getName());
      }
      else
         CultimaPanel.sendMessage("The gem has broken!");
   }
   
   public static boolean canCraftArmor(String armorName, String itemName)
   {
      armorName = armorName.toLowerCase();
      itemName = itemName.toLowerCase();
      boolean itemOK = (itemName.contains("dragon-scales") || itemName.contains("dragonqueen-scales") || itemName.contains("seaserpent-scales"));
      boolean armorOK = (armorName.contains("scalemail") || armorName.contains("blessed-scale"));
      return (itemOK && armorOK);
   }
   
   public static void upgradeArmor()
   {
      String itemName = "";
      if(CultimaPanel.player.getItems().size() > 0 && CultimaPanel.itemIndex >=0 && CultimaPanel.itemIndex<CultimaPanel.player.getItems().size())
         itemName = CultimaPanel.player.getItems().get(CultimaPanel.itemIndex);
      Armor armor = CultimaPanel.player.getArmor();
      String armName = armor.getName().toLowerCase();
      if(!canCraftArmor(armName, itemName))
      {
         CultimaPanel.sendMessage("These items can not be combined.");
         return;
      }
      //remove the item
      boolean success = CultimaPanel.player.removeItem(CultimaPanel.player.getItems().get(CultimaPanel.itemIndex));
      if(!success)
      {
         CultimaPanel.sendMessage("These items can not be combined.");
         return;
      }
      //upgrade the armor
      if(itemName.contains("scales"))
      {  
         Armor dropped = CultimaPanel.player.getArmor();
         Armor temp = null;
         if(itemName.equalsIgnoreCase("dragonqueen-scales"))  
         {
            if(armName.contains("blessed"))
               temp = Armor.getBlessedDragonqueen();
            else  
               temp = Armor.getDragonqueenScale();
            CultimaPanel.player.removeAllItemsWithName("dragonqueen-scales");
         
         }
         else if(itemName.equalsIgnoreCase("dragon-scales"))  
         {
            if(armName.contains("blessed"))
               temp = Armor.getBlessedDragonScale();
            else
               temp = Armor.getDragonScale();
            CultimaPanel.player.removeAllItemsWithName("dragon-scales");
         }
         else if(itemName.equalsIgnoreCase("seaserpent-scales"))  
         {
            if(armName.contains("blessed"))
               temp = Armor.getBlessedSeaserpent();
            else
               temp = Armor.getSeaserpentScale();
            CultimaPanel.player.removeAllItemsWithName("seaserpent-scales");
         }
         else
            temp = dropped;
         CultimaPanel.player.discardCurrentArmor();
         CultimaPanel.player.addArmor(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());  
         if(itemName.contains("dragon"))
            Achievement.earnAchievement(Achievement.DRAGONSLAYER);              
      }
   }
   
   public static boolean canCombine(String weaponName, String itemName)
   {
      weaponName = weaponName.toLowerCase();
      itemName = itemName.toLowerCase();
      if(weaponName==null || itemName==null || weaponName.length()==0 || itemName.length()==0)
         return false;
      //legendary weapons
      if(weaponName.equals("magestaff") && CultimaPanel.player.hasItem("Bright-horn"))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("might-exotic-hammer") || weaponName.equals("mind-exotic-hammer") || weaponName.equals("swift-exotic-hammer")))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("blessed-swiftblade") || weaponName.equals("blessed-mightblade") || weaponName.equals("blessed-mindblade")))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("might-mirroraxe") || weaponName.equals("mind-mirroraxe") || weaponName.equals("swift-mirroraxe")))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("might-longbow") || weaponName.equals("mind-longbow") || weaponName.equals("wind-longbow")))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("might-halberd") || weaponName.equals("mind-halberd") || weaponName.equals("wind-halberd")))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("toothed-torch-o-might") || weaponName.equals("toothed-torch-o-mind") || weaponName.equals("swift-toothed-torch")))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("windstaff") || weaponName.equals("mightstaff") || weaponName.equals("mindstaff")))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("might-spear") || weaponName.equals("mind-spear") || weaponName.equals("wind-spear")))
         return true;
      if(itemName.contains("powerbands") && (weaponName.equals("dagger-o-might") || weaponName.equals("dagger-o-mind") || weaponName.equals("swift-dagger")))
         return true;
      //regular upgrades 
      if(!itemIsStone(itemName) || !weaponIsUpgradable(weaponName))
         return false;
      if(itemName.contains("mannastone") && !weaponName.equals("staff"))
         return false;     //mannastone only enhances staff weapons
      if(!itemName.contains("mannastone") && weaponName.equals("staff"))
         return false;     //only mannastone enhances staff weapons
      if(itemName.contains("life-pearl") && !weaponName.contains("royal"))
         return false;  //life-pearl can only enhance royal weapons    
      if(!itemName.contains("life-pearl") && weaponName.contains("royal"))
         return false;  //only life-pearl can enhance royal weapons 
      if(itemName.contains("moonjem") && (weaponName.contains("torch") || weaponName.contains("bright") || weaponName.contains("flame")))
         return false;     //moonjems can not be added to weapons that are already casting light
      if(itemName.contains("flamejem") && (weaponName.contains("ice") || weaponName.contains("frost") || weaponName.contains("torch") || weaponName.contains("bright") || weaponName.contains("flame")))
         return false;     //flamejem can not be added to weapons that are already casting light
      if(itemName.contains("icejem") && (weaponName.contains("ice") || weaponName.contains("frost") || weaponName.contains("torch") || weaponName.contains("flame")))
         return false;     //icejem can not be added to weapons that are already freeze
      return true;
   }
   
   //try to upgrade the player's current weapon with the player's current item
   public static void upgradeWeapon()
   {
      String itemName = "";
      if(CultimaPanel.player.getItems().size() > 0 && CultimaPanel.itemIndex >=0 && CultimaPanel.itemIndex<CultimaPanel.player.getItems().size())
         itemName = CultimaPanel.player.getItems().get(CultimaPanel.itemIndex);
      Weapon weapon = CultimaPanel.player.getWeapon();
      String weapName = weapon.getName().toLowerCase();
      itemName = itemName.toLowerCase();
      if(!canCombine(weapName, itemName))
      {
         CultimaPanel.sendMessage("These items can not be combined.");
         return;
      }
      //remove the item
      boolean success = CultimaPanel.player.removeItem(CultimaPanel.player.getItems().get(CultimaPanel.itemIndex));
      if(!success)
      {
         CultimaPanel.sendMessage("These items can not be combined.");
         return;
      }
      //upgrade the weapon
      if(weapName.equals("magestaff") && CultimaPanel.player.hasItem("Bright-horn"))
      {
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = Weapon.getOhnyalei();
         CultimaPanel.player.discardWeapon("Magestaff");
         CultimaPanel.player.discardWeapon("Bright-horn");
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
      else if(itemName.contains("powerbands"))
      {  //legendary weapons
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = null;
         if(weapName.equals("might-exotic-hammer") || weapName.equals("mind-exotic-hammer") || weapName.equals("swift-exotic-hammer"))  
            temp = Weapon.getMjolnir();
         else if(weapName.equals("blessed-swiftblade") || weapName.equals("blessed-mightblade") || weapName.equals("blessed-mindblade"))  
            temp = Weapon.getExcalibur();
         else if(weapName.equals("might-mirroraxe") || weapName.equals("mind-mirroraxe") || weapName.equals("swift-mirroraxe"))  
            temp = Weapon.getForsetisAxe();
         else if(weapName.equals("might-longbow") || weapName.equals("mind-longbow") || weapName.equals("wind-longbow"))  
            temp = Weapon.getBowOfKarna();
         else if(weapName.equals("might-halberd") || weapName.equals("mind-halberd") || weapName.equals("wind-halberd"))
            temp = Weapon.getAmeNoNuhoko();
         else if(weapName.equals("toothed-torch-o-might") || weapName.equals("toothed-torch-o-mind") || weapName.equals("swift-toothed-torch"))
            temp = Weapon.getGadaTorchmace();
         else if(weapName.equals("windstaff") || weapName.equals("mightstaff") || weapName.equals("mindstaff"))
            temp = Weapon.getKhatvanga();
         else if(weapName.equals("might-spear") || weapName.equals("mind-spear") || weapName.equals("wind-spear"))
            temp = Weapon.getGungnir();
         else if(weapName.equals("dagger-o-might") || weapName.equals("dagger-o-mind") || weapName.equals("swift-dagger"))
            temp = Weapon.getCarnwennan();
         else
            temp = dropped;
         CultimaPanel.player.discardCurrentWeapon();
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
         if(Weapon.isLegendaryWeapon(temp.getName()))
            Achievement.earnAchievement(Achievement.LEGENDARY_CRAFTER);                          
      }
      else if(itemName.contains("life-pearl"))
      {  //life weapons
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = null;
         if(weapName.contains("royal-sword-shield"))
            temp = Weapon.getLifeSwordShield();
         else if(weapName.contains("royal-sceptre"))
            temp = Weapon.getLifeSceptre();
         else 
            temp = dropped;   
         CultimaPanel.player.discardCurrentWeapon();
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
      else if(itemName.contains("mannastone"))
      {  //mage's staff
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = Weapon.getMageStaff();
         CultimaPanel.player.discardWeapon("Staff");
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
      else if(itemName.contains("moonjem"))
      {  //bright weapons
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = null;
         if(weapName.equals("axe"))
            temp = Weapon.getBrightAxe();
         else if(weapName.equals("dualaxe"))
            temp = Weapon.getBrightDualAxe();
         else if(weapName.equals("sword-buckler"))
            temp = Weapon.getBrightSwordBuckler();
         else if(weapName.equals("hammer"))
            temp = Weapon.getBrightHammer(); 
         else if(weapName.equals("sword") || weapName.equals("blessed-sword"))
            temp = Weapon.getBrightSword();   
         else if(weapName.equals("short-swords"))
            temp = Weapon.getBrightSwords();  
         else if(weapName.equals("crossbow"))
            temp = Weapon.getBrightcaster();
         else if(weapName.equals("bow"))
            temp = Weapon.getBrightBow();
         else if(weapName.equals("longbow"))
            temp = Weapon.getBrightLongbow();
         else if(weapName.equals("spear"))
            temp = Weapon.getBrightSpear();
         else if(weapName.equals("halberd"))
            temp = Weapon.getBrightHalberd();
         else if(weapName.equals("long-staff"))
            temp = Weapon.getBrightstaff();
         else if(weapName.equals("dagger"))
            temp = Weapon.getBrightDagger();
         else if(weapName.equals("mirrored-axe"))
            temp = Weapon.getBrightMirrorAxe();
         else if(weapName.equals("sword-mirrorshield"))
            temp = Weapon.getBrightSwordMirrorshield();
         else if(weapName.equals("exotic-hammer"))
            temp = Weapon.getBrightExoticHammer();
         else if(weapName.equals("spiked-hammer"))
            temp = Weapon.getBrightSpikedHammer();
         else 
            temp = dropped;
         CultimaPanel.player.discardCurrentWeapon();
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
      else if(itemName.contains("ruby"))
      {  //might weapons
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = null;
         if(weapName.equals("axe"))
            temp = Weapon.getAxeOfMight();
         else if(weapName.equals("dualaxe"))
            temp = Weapon.getDualAxeOfMight();
         else if(weapName.equals("sword-buckler"))
            temp = Weapon.getSwordBucklerOfMight();
         else if(weapName.equals("hammer"))
            temp = Weapon.getHammerOfMight(); 
         else if(weapName.equals("sword"))
            temp = Weapon.getSwordOfMight();   
         else if(weapName.equals("blessed-sword"))
            temp = Weapon.getBlessedMightblade();
         else if(weapName.equals("short-swords"))
            temp = Weapon.getSwordsOfMight();  
         else if(weapName.equals("crossbow"))
            temp = Weapon.getMightcaster();
         else if(weapName.equals("bow"))
            temp = Weapon.getMightBow();
         else if(weapName.equals("longbow"))
            temp = Weapon.getMightLongbow();
         else if(weapName.equals("spear"))
            temp = Weapon.getMightSpear();
         else if(weapName.equals("halberd"))
            temp = Weapon.getMightHalberd();
         else if(weapName.equals("long-staff"))
            temp = Weapon.getMightstaff();
         else if(weapName.equals("dagger"))
            temp = Weapon.getDaggerOfMight();
         else if(weapName.equals("toothed-torch"))
            temp = Weapon.getToothedTorchOfMight();
         else if(weapName.equals("mirrored-axe"))
            temp = Weapon.getMightMirrorAxe();
         else if(weapName.equals("sword-mirrorshield"))
            temp = Weapon.getMightSwordMirrorshield();
         else if(weapName.equals("exotic-hammer"))
            temp = Weapon.getMightExoticHammer();
         else if(weapName.equals("spiked-hammer"))
            temp = Weapon.getMightSpikedHammer();
         else 
            temp = dropped;
         CultimaPanel.player.discardCurrentWeapon();
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
      else if(itemName.contains("jade"))
      {  //mind weapons
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = null;
         if(weapName.equals("axe"))
            temp = Weapon.getAxeOfMind();
         else if(weapName.equals("dualaxe"))
            temp = Weapon.getDualAxeOfMind();
         else if(weapName.equals("sword-buckler"))
            temp = Weapon.getSwordBucklerOfMind();
         else if(weapName.equals("hammer"))
            temp = Weapon.getHammerOfMind(); 
         else if(weapName.equals("sword"))
            temp = Weapon.getSwordOfMind();  
         else if(weapName.equals("blessed-sword"))
            temp = Weapon.getBlessedMindblade(); 
         else if(weapName.equals("short-swords"))
            temp = Weapon.getSwordsOfMind();  
         else if(weapName.equals("crossbow"))
            temp = Weapon.getMindcaster();
         else if(weapName.equals("bow"))
            temp = Weapon.getMindBow();
         else if(weapName.equals("longbow"))
            temp = Weapon.getMindLongbow();
         else if(weapName.equals("spear"))
            temp = Weapon.getMindSpear();
         else if(weapName.equals("halberd"))
            temp = Weapon.getMindHalberd();
         else if(weapName.equals("long-staff"))
            temp = Weapon.getMindstaff();
         else if(weapName.equals("dagger"))
            temp = Weapon.getDaggerOfMind();
         else if(weapName.equals("toothed-torch"))
            temp = Weapon.getToothedTorchOfMind();
         else if(weapName.equals("mirrored-axe"))
            temp = Weapon.getMindMirrorAxe();
         else if(weapName.equals("sword-mirrorshield"))
            temp = Weapon.getMindSwordMirrorshield();
         else if(weapName.equals("exotic-hammer"))
            temp = Weapon.getMindExoticHammer();
         else if(weapName.equals("spiked-hammer"))
            temp = Weapon.getMindSpikedHammer();
         else 
            temp = dropped;
         CultimaPanel.player.discardCurrentWeapon();
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
      else if(itemName.contains("azurite"))
      {  //agility weapons
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = null;
         if(weapName.equals("axe"))
            temp = Weapon.getSwiftAxe();
         else if(weapName.equals("dualaxe"))
            temp = Weapon.getSwiftDualAxe();
         else if(weapName.equals("sword-buckler"))
            temp = Weapon.getSwiftSwordBuckler();
         else if(weapName.equals("hammer"))
            temp = Weapon.getSwiftHammer(); 
         else if(weapName.equals("sword"))
            temp = Weapon.getSwiftSword();   
         else if(weapName.equals("blessed-sword"))
            temp = Weapon.getBlessedSwiftblade(); 
         else if(weapName.equals("short-swords"))
            temp = Weapon.getWindSwords();  
         else if(weapName.equals("crossbow"))
            temp = Weapon.getWindcaster();
         else if(weapName.equals("bow"))
            temp = Weapon.getWindBow();
         else if(weapName.equals("longbow"))
            temp = Weapon.getWindLongbow();
         else if(weapName.equals("spear"))
            temp = Weapon.getWindSpear();
         else if(weapName.equals("halberd"))
            temp = Weapon.getWindHalberd();
         else if(weapName.equals("long-staff"))
            temp = Weapon.getWindstaff();
         else if(weapName.equals("dagger"))
            temp = Weapon.getSwiftDagger();
         else if(weapName.equals("toothed-torch"))
            temp = Weapon.getSwiftToothedTorch();
         else if(weapName.equals("mirrored-axe"))
            temp = Weapon.getSwiftMirrorAxe();
         else if(weapName.equals("sword-mirrorshield"))
            temp = Weapon.getSwiftSwordMirrorshield();
         else if(weapName.equals("exotic-hammer"))
            temp = Weapon.getSwiftExoticHammer();
         else if(weapName.equals("spiked-hammer"))
            temp = Weapon.getSwiftSpikedHammer();
         else 
            temp = dropped;
         CultimaPanel.player.discardCurrentWeapon();
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
      else if(itemName.contains("flamejem"))
      {  //flame weapons
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = null;
         if(weapName.equals("axe"))
            temp = Weapon.getFlameAxe();
         else if(weapName.equals("mirrored-axe"))
            temp = Weapon.getFlameMirrorAxe();
         else if(weapName.equals("dualaxe"))
            temp = Weapon.getDualAxeOfFire();
         else if(weapName.equals("hammer"))
            temp = Weapon.getFlameHammer(); 
         else if(weapName.equals("exotic-hammer"))
            temp = Weapon.getExoticHammerOfFire();
         else if(weapName.equals("sword-buckler"))
            temp = Weapon.getFlamebladeBuckler(); 
         else if(weapName.equals("sword") || weapName.equals("blessed-sword"))
            temp = Weapon.getFlameblade();   
         else if(weapName.equals("short-swords"))
            temp = Weapon.getDualFlameblades();  
         else if(weapName.equals("crossbow"))
            temp = Weapon.getFlamecaster();
         else if(weapName.equals("bow"))
            temp = Weapon.getFlamebow();
         else if(weapName.equals("longbow"))
            temp = Weapon.getFlamebow();
         else if(weapName.equals("spear"))
            temp = Weapon.getFlameSpear();
         else if(weapName.equals("halberd"))
            temp = Weapon.getHalberdOfFire(); 
         else if(weapName.equals("long-staff"))
            temp = Weapon.getFlamestaff();
         else if(weapName.equals("dagger"))
            temp = Weapon.getMagmaDagger();
         else if(weapName.equals("sword-mirrorshield"))
            temp = Weapon.getFlamebladeMirrorshield();
         else if(weapName.equals("spiked-hammer"))
            temp = Weapon.getSpikedHammerOfFire();
         else 
            temp = dropped;
         CultimaPanel.player.discardCurrentWeapon();
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
      else if(itemName.contains("icejem"))
      {  //frost weapons
         Weapon dropped = CultimaPanel.player.getWeapon();
         Weapon temp = null;
         if(weapName.equals("axe"))
            temp = Weapon.getFrostAxe();
         else if(weapName.equals("mirrored-axe"))
            temp = Weapon.getFrostMirrorAxe();
         else if(weapName.equals("dualaxe"))
            temp = Weapon.getDualAxeOfFrost();
         else if(weapName.equals("hammer"))
            temp = Weapon.getFrostHammer(); 
         else if(weapName.equals("exotic-hammer"))
            temp = Weapon.getExoticFrostHammer();
         else if(weapName.equals("sword-buckler"))
            temp = Weapon.getFrostbladeBuckler(); 
         else if(weapName.equals("sword") || weapName.equals("blessed-sword"))
            temp = Weapon.getFrostblade();   
         else if(weapName.equals("short-swords"))
            temp = Weapon.getDualFrostblades();  
         else if(weapName.equals("crossbow"))
            temp = Weapon.getFrostcaster();
         else if(weapName.equals("bow"))
            temp = Weapon.getFrostbow();
         else if(weapName.equals("longbow"))
            temp = Weapon.getFrostbow();
         else if(weapName.equals("spear"))
            temp = Weapon.getFrostSpear();
         else if(weapName.equals("halberd"))
            temp = Weapon.getHalberdOfFrost(); 
         else if(weapName.equals("long-staff"))
            temp = Weapon.getIceStaff();
         else if(weapName.equals("dagger"))
            temp = Weapon.getFrostDagger();
         else if(weapName.equals("sword-mirrorshield"))
            temp = Weapon.getFrostbladeMirrorshield();
         else if(weapName.equals("spiked-hammer"))
            temp = Weapon.getSpikedFrostHammer();
         else 
            temp = dropped;
         CultimaPanel.player.discardCurrentWeapon();
         CultimaPanel.player.addWeaponAndSwitch(temp);
         CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
      }
   }
   
   public static boolean canBeBlessed(String itemName)
   {
      itemName = itemName.toLowerCase();
      if(itemName.contains("blessed"))
         return false;
      return (itemName.equalsIgnoreCase("swift-sword") || itemName.equalsIgnoreCase("sword-o-might") || itemName.equalsIgnoreCase("sword-o-mind") || itemName.equalsIgnoreCase("sword")
           || itemName.equalsIgnoreCase("scalemail") || itemName.equalsIgnoreCase("dragon-scale") || itemName.equalsIgnoreCase("dragonqueen-scale") || itemName.equalsIgnoreCase("seaserpent-scale"));
   }
   
   public static boolean bless(Item toBless)
   {
      if(!canBeBlessed(toBless.getName()))
      {
         CultimaPanel.sendMessage("Blessing failed");
         return false;
      }
      if(toBless instanceof Weapon)
      {
         Weapon dropped = CultimaPanel.player.getWeapon();
         String weapName = dropped.getName().toLowerCase();
         Weapon temp = null;
         if(weapName.equals("sword"))
            temp = Weapon.getBlessedSword();   
         else if(weapName.equals("swift-sword"))
            temp = Weapon.getBlessedSwiftblade();  
         else if(weapName.equals("sword-o-might"))
            temp = Weapon.getBlessedMightblade();
         else if(weapName.equals("sword-o-mind"))
            temp = Weapon.getBlessedMindblade();
         if(temp != null)
         {
            CultimaPanel.player.discardCurrentWeapon();
            CultimaPanel.player.addWeaponAndSwitch(temp);
            CultimaPanel.sendMessage("Your "+dropped.getName()+" has absorbed the blessing!");
            return true;
         }
         else
         {      
            CultimaPanel.sendMessage("Blessing failed"); 
            return false;
         }
      }
      else if(toBless instanceof Armor)
      {
         Armor dropped = CultimaPanel.player.getArmor();
         String armorName = dropped.getName().toLowerCase();
         Armor temp = null;
         if(armorName.equals("scalemail"))
            temp = Armor.getBlessedArmor();   
         else if(armorName.equals("dragon-scale"))
            temp = Armor.getBlessedDragonScale();  
         else if(armorName.equals("dragonqueen-scale"))
            temp = Armor.getBlessedDragonqueen();
         else if(armorName.equals("seaserpent-scale"))
            temp = Armor.getBlessedSeaserpent();
         if(temp != null)
         {
            CultimaPanel.player.discardCurrentArmor();
            CultimaPanel.player.addArmor(temp);
            CultimaPanel.sendMessage("You upgraded:"+dropped.getName()+" to:"+temp.getName());
            return true;
         }
         else
         {      
            CultimaPanel.sendMessage("Blessing failed"); 
            return false;
         }
      }
      else
      {
         CultimaPanel.sendMessage("Blessing failed");
         return false;
      }
   }
   
   public static Coord getCoordFromTeasureMap(String name)
   {
      if(!name.startsWith("treasuremap"))
         return null;
      int leftParen = name.indexOf("(");
      int rightParen = name.indexOf(")");
      int r = -1;
      int c = -1;
      if(leftParen >=0 && rightParen>=0 && leftParen < rightParen)
      {
         String coord = name.substring(leftParen+1, rightParen);
         if(coord.length() >= 3)
         {
            String [] pieces = coord.split(":");
            if(pieces.length == 2)
            {
               if(FileManager.wordIsInt(pieces[0].trim()))
                  c = Integer.parseInt(pieces[0].trim());    
               if(FileManager.wordIsInt(pieces[1].trim()))
                  r = Integer.parseInt(pieces[1].trim());
               if(r>=0 && c>=0)
                  return new Coord(r, c);   
            }
         }
      }
      return null;
   }
}