import java.util.ArrayList;

public class Mission
{
   private String type;             //Clear, Rescue, Find, Assassinate, Battle, Remove, Train, Mercy
   private String startStory;       //start narrative
   private String middleStory;      //narrative while mission is in progress
   private String endStory;         //end narrative
   private String info;             //short version of mission information for a mission log
   private int worldRow, worldCol;  //the location where the mission starts
   private int day;                 //the day that the mission must me completed by. Set to -1 if not used.
   private int goldReward;          //monetary pay for completion
   private Item itemReward;         //item reward for completion
   private String clearLocation;    //name of the location where a clearing mission or battle takes place - clear out monsters/rescue prisoners
   private Item target;             //item we might have to retrieve
   private String targetHolder;     //name of the person that holds the target
   private int targetRow, targetCol;//where the target is placed (might be at targetRow, targetCol within the clearLocation or the greater world if clearLocation is null
   private boolean failed;          //battle lost 

   private byte state;              //is it NOT_STARTED, IN_PROGRESS or FINISHED?
   public static final byte NOT_STARTED=0;
   public static final byte IN_PROGRESS=1;
   public static final byte FINISHED=2;

   public byte missionType;  //stores values of one of the mission types listed below
//**Missions - each defines the index for the missions array
   public static final byte MAIN_QUEST=0;             //given to all players
   public static final byte CLEAR_NAME=1;             //given at character creation - start with bounty and bad reputation looking for redemption
   public static final byte CURE_VAMPIRE=2;           //given at character creation - start as a vampire, looking for cure
   public static final byte CURE_WEREWOLF=3;          //given at character creation - start as werewolf, looking for cure
   public static final byte KILL_DEATH=4;             //given after main quest - defeat death
   public static final byte CLEAR_DUNGEON=5;          //given by NPC.KING - clear monsters out of a castle dungeon
   public static final byte CLEAR_MINE=6;             //given by adventureres (NPC.SWORD) - clear monsters out of a nearby mine
   public static final byte CLEAR_TEMPLE=7;           //given by NPC.WISE - clear monsters from a nearby temple
   public static final byte CLEAR_CAVE=8;             //given by NPC.BEGGER - clear monsters out of a nearby cave
   public static final byte ASSASSINATE_KING = 9;     //given by NPC.WISE - commoner ask to overthrow an evil local ruler
   public static final byte ASSASSINATION=10;         //given by NPC.KING - royalty asks to assassinate a threat to the kingdom
   public static final byte RESCUE_PRISONERS=11;      //given by NPC.KING - royalty asks to rescue prisoners in monster lair
   public static final byte PURGE_CITY=12;            //given by NPC.KING - wicked royalty asks to empty a city where a rebellion is forming
   public static final byte PURGE_HOLY=13;            //given by NPC.WISE - wicked holy leader asks to purge a village of infidels
   public static final byte ZOMBIE_PLAGUE=14;         //given by guard - guard asks to cleanse a city over-run by zombie sickness
   public static final byte FIND_ITEM=15;             //given by NPC.KING - royalty asks to retrieve a magic item from a commoner
   public static final byte FIND_WEAPON=16;           //given by adventureres (NPC.SWORD) - adventurer asks to find lost weapon or armor
   public static final byte FIND_PELT=17;             //given by downtrodden (NPC.BEGGER) - downtrodden asks to retrieve a pelt for warmth
   public static final byte FIND_LUTE = 18;           //given by NPC.LUTE - musician asks to find magic lute
   public static final byte FIND_SONG = 19;           //given by NPC.LUTE - musician asks to find lost pages of music
   public static final byte FIND_PERSON=20;           //given by commoner (NPC.MAN/NPC.WOMAN) - commoner asks to find lost loved one (cave/mine/lair)
   public static final byte FIND_PARENT = 21;         //given by NPC.CHILD - child asks to find lost parent (cave/mine/lair)
   public static final byte FIND_PET = 22;            //given by NPC.CHILD - child asks for a puppy
   public static final byte FIND_PEARL = 23;          //given by NPC.WISE - mage asks for a life-pearl from a giant squid
   public static final byte FIND_MAGIC_ITEM = 24;     //given by NPC.WISE - mage asks to find magic item stolen by a wicked apprentice
   public static final byte FIND_CAERBANNOG = 25;     //given by NPC.WISE - mage asks to find runes from the Cave of Caerbannog
   public static final byte STEAL_PICK = 26;          //given by NPC.JESTER - thief asks to steal magic lockpick from another thief
   public static final byte STEAL_BOOK = 27;          //given by NPC.JESTER - jester asks to steal riddle book form another commoner
   public static final byte STEAL_DICE = 28;          //given by NPC.JESTER - thief asks to steal loaded dice from a player of swine
   public static final byte STEAL_DAGGERS = 29;       //given by NPC.JESTER - thief asks to steal 5 magic daggers from travelers in the city/castle
   public static final byte DESTROY_STATUE = 30;      //given by NPC.JESTER - jester asks to vandalize a city statue 
   public static final byte CITY_REVENGE=31;          //given by commoner (NPC.MAN/NPC.WOMAN) - commoner asks for revenge against another commoner
   public static final byte MESSAGE=32;               //given by commoner (NPC.MAN/NPC.WOMAN) - commoner asks for a message delivery
   public static final byte LOVE_TRIANGLE=33;         //given by commoner (NPC.MAN/NPC.WOMAN) - commoner asks to frighten a former partner out of town
   public static final byte BATTLE=34;                //given by guards - guard asks to join brute squad to defeat a monster army  
   public static final byte CASTLE_ASSASSIN=35;       //given by royal guard - guard asks to hunt an assassin in a castle dungeon
   public static final byte CASTLE_BEAST=36;          //given by royal guard - guard asks to hunt a beast in a castle dungeon
   public static final byte MOVE_BEGGER = 37;         //downtrodden mission - downtrodden asks you to move another downtrodden from his spot
   public static final byte CLEAR_BEGGERS = 38;       //shoppekeep mission - shop-keeper asks to remove all downtrodden from the village
   public static final byte TRAIN_DOG = 39;           //shoppekeep mission - shoppekeep asks to train a dog and give it to them
   public static final byte TRAIN_HORSE = 40;         //given by royal guard  - royal-guard asks to train a wild horse and bring it to them
   public static final byte SLAY_MONSTER = 41;        //given by guard - guard asks to slay a monster outside the city
   public static final byte SLAY_ROBERTS = 42;        //given by port guard - guard asks you to defeat the dread brigand roberts and retrieve their black mask
   public static final byte ARENA = 43;               //given by Arenamaster - compete in the arena
   public static final byte ARENA_BOUNTY = 44;        //given by Arenamaster - track down a prisoner in the city
   public static final byte ARENA_RESCUE = 45;        //given by downtrodden - rescue people being forced to fight in the arena
   public static final byte TEACH_SPELL = 46;         //given by wise - learn a rare spell and teach it to a mage
   public static final byte CLEAR_BANDITS = 47;       //given by shoppekeep - kill gang of bandits that come to town
   public static final byte BEAT_SWINE_PLAYER = 48;   //given by townsfolk - defeat a player with loaded dice in a game of swine
   public static final byte TREASURE_MAP = 49;        //given by adventurers (NPC.SWORD) - recover treasure given a location on the map
   public static final byte FIND_SERPENT_EGGS = 50;   //given by NPC.WISE (mage's shoppe) - retrieve eggs found in the wild for a mage
   public static final byte FIND_DRAGON_EGG = 51;     //given by NPC.GOYALGUARD - steal a dragon egg from a cave for a guard
   public static final byte FIRE_FIGHTER = 52;        //given by city MAN or WOMAN - extinguish a fire in the city
   public static final byte CHASE_THIEF = 53;         //given by city GUARD - remove a master thief in the city           
   public static final byte FIND_ELK_PELT = 54;       //given by hermit - retrieve an elk pelt for a roof repair
   public static final byte FIND_GEM = 55;            //given by ironsmith - find a magic gem from a mine for a mage
   public static final byte KILL_WEREWOLF = 56;       //given by begger - slay a werewolf hiding among the people of the city
   public static final byte MERCY_KILL = 57;          //given by man/woman - mercy kill a guilt-ridden werewolf
   public static final byte WEREWOLF_TOWN = 58;       //given by adventurers (NPC.SWORD) - empty a village of outcast werewolves
   public static final byte SLAY_VAMPIRE = 59;        //given by townsfolk - slay a vampire in a nearby cave and burn its coffin
   public static final byte GET_BOAT = 60;            //given by port adventurer (NPC.SWORD) - retrieve a vessel for an adventurer
   public static final byte DESTROY_SIGNS = 61;       //given by CHILD - destroy shop signs for the child of a sign maker
   public static final byte DESTROY_STATUE2 = 62;     //given by CHILD - destroy a city statue that frightens a child
   public static final byte BURN_COFFINS = 63;        //given by townsfolk (MAN/WOMAN) - destroy coffins in a town to drive out a vampire
   public static final byte TAX_COLLECTOR = 64;       //given by TAXMAN - help tax collector get overdue takes from a commoner
   public static final byte FIND_LEDGER = 65;         //given by TAXMAN - help tax collector find their lost tax ledger
   public static final byte TAX_IMPOSTER = 66;        //given by TAXMAN - stop a commoner pretending to be a tax collector
   public static final byte TAX_MOB = 67;             //given by TAXMAN - stop the gang hired to drive out the tax collector
   public static final byte MARRIAGE = 68;            //given by WISE - earn a dowry for marrying the son/daughter of an old mage
   public static final byte CITY_SIEGE = 69;          //given by GUARD at the gate - defend the city from an attacking band of monsters
   public static final byte ESCORT_ADULT = 70;        //given by MAN - escort an adult to a close town or castle
   public static final byte ESCORT_KID = 71;          //given by WOMAN,MAN - protect a child who wants to see a battle
   public static final byte CEREMONY = 72;            //given by WISE - stop a cult ceremony before they bring their god's statue to life
   public static final byte CULT_RESCUE = 73;         //given by WOMAN, MAN - rescue their family member from a cult
   public static final byte KAIJU = 74;               //given by SWORD - stop or redirect a giant monster that is advancing on a city or castle
   public static final byte DOGTRAP = 75;             //given by CHILD - their dog has been trapped by hunters trapping wolves
   public static final byte WEREWOLFTRAP = 76;        //given by MAN/WOMAN - werewolf kin have been trapped by guards in a camp
   public static final byte MONSTERTRAP = 77;         //given by GUARD - brigands have trapped monsters to recruit for their army
   public static final byte PUBLIC_HOUSING = 78;      //given by downtrodden - buy a house for all the city downtrodden
   public static final byte REMOVE_CULT = 79;         //given by LUTE - remove the cult who are chanting in disharmony 
   public static final byte HAUNTED_HOUSE = 80;       //given by CHILD - knock down haunted house that frightens them
   public static final byte HAUNTED_FIND = 81;        //given by LUTE - retrieve book from haunted house
   public static final byte HAUNTED_CLEAR = 82;       //given by widowed BEGGER - liberate phantoms from haunted house
   public static final byte CLOSE_HELLMOUTH = 83;     //given by WISE - close a hellmouth in the haunted house with raise-stone
   public static final byte FIND_LUMBER1 = 84;        //given by ironsmith - retrieve lumber so ironsmith can make weapons
   public static final byte FIND_LUMBER2 = 85;        //given by barkeeps - retrieve lumber to make barrels
   public static final byte FIND_LUMBER3 = 86;        //given by other shopkeeps - retrieve lumber to repair shop counter
   public static final byte FIND_LUMBER4 = 87;        //given by GUARD - retrieve lumber for guard who is building a house
   public static final byte FIX_PATH = 88;            //given by shopkeeps - bridge over muddy patch in the path
   public static final byte BUILD_PIER = 89;          //given by SWORD - build pier for easier travel
   public static final byte RAISE_WATER = 90;         //given by downtrodden - use raise water spell for city whose well has dried up
   public static final byte COLLECT_FLORETS1 = 91;    //given by MAN/WOMAN, not a shopkeeper - collect a full floretbox worth of florets for a garden
   public static final byte COLLECT_FLORETS2 = 92;    //given by WISE - collect a full floretbox worth of florets for floretlade
   public static final byte COLLECT_FLORETS3 = 93;    //given by CHILD - collect 10 florets of a child's favorite color
   public static final byte RAIN_MAKER1 = 94;         //given by MAN/WOMAN - make it rain by the end of the day for crops to grow
   public static final byte RAIN_MAKER2 = 95;         //given by single MAN/WOMON - make it rain on a specific day to delay a wedding
   public static final byte HEAL_WARRIOR = 96;        //given by SWORD with 0 agility - injured warrior gets agility restored when healed with potion/spell - gem reward
   public static final byte HEAL_TAXMAN = 97;         //given by TAXMAN - broken their leg chasing down a tax-evader
   public static final byte CURE_POISONED = 98;       //given by MAN/WOMAN - bitten by a snake, needs cure spell or potion
   public static final byte CURE_KING = 99;           //given by royalty - cure King poisoned by someone in the castle, followed up with a bounty for them
   public static final byte FIND_SCALES = 100;        //given by ironsmith - needs dragon/seaserpent scales to make armor for king - if dragon, puts one in a nearby cave
   public static final byte FEED_POOR = 101;          //given by MAN/WOMAN - donate 15 rations to help the downtrodden make it through winter
   public static final byte SEWER_LOST = 102;         //given by MAN/WOMAN - lost child in the sewer
   public static final byte SEWER_CLOG = 103;         //given by guard - clear clog in the sewer
   public static final byte SEWER_RATS = 104;         //given by butcher/barkeep - clear rats from the sewer
   public static final byte SEWER_SLAY = 105;         //given by adventurer - slay giant monster in the sewer
   public static final byte SEWER_SIEGE = 106;        //given by guard - clear invading siege army in the sewer
   public static final byte SEWER_CHUD = 107;         //given by guard - clear Chud from sewer and around the entrance - night
   public static final byte PRANK_PARCEL = 108;       //given by jester - give exploding parcel to guard
   public static final byte DELIVER_MEAT = 109;       //given by butcher - deliver meat by a certain day before it spoils
   public static final byte TEACH_3_DOORS = 110;      //given by sword - teach the 3-doors puzzle
   public static final byte TEACH_3_TOWERS = 111;     //given by wise - teach the 3-towers puzzle
   public static final byte FIND_LEDGER2 = 112;       //given by taxman - tax ledger thrown into haunted house by the jester
   public static final byte NUM_MISSIONS = 113;       //CAN ONLY GET AS LARGE AS 127

   public static final String[][] missions = {
      //MAIN_QUEST 0          
      {"Discover the number, find the Red Square.","",""},
      //CLEAR_NAME 1
      {"Clear thy reputation.","",""},
      //CURE_VAMPIRE 2
      {"Purge the Vampyric curse.","",""},
      //CURE_WEREWOLF 3
      {"Purge the Wolfen curse.","",""},
      //KILL_DEATH 4
      {"Purge Death from this plane.","",""},
      //CLEAR_DUNGEON 5
      {"These ADJECTIVE MONSTER_TYPEs have overrun thy dungeon. Purge them for GOLD_REWARD gold coins.",                   "Some ADJECTIVE MONSTER_TYPEs still lurk in thy dungeon. Purge them with haste!",               "Thou has done me great service by clearing thy dungeon. Here is thy GOLD_REWARD coins."},
      //CLEAR_MINE 6
      {"I've troubles clearing MONSTER_TYPEs from LOCATION_NAME for thy miners. Finish me quest for GOLD_REWARD gold.",    "Miners need LOCATION_NAME cleared of MONSTER_TYPEs to work. Purge them with haste!",           "The miners can safely return! Here is thy well deserved GOLD_REWARD gold."},
      //CLEAR_TEMPLE 7
      {"Thse MONSTER_TYPEs have haunted our sacred temple at LOCATION_NAME. Smite them for GOLD_REWARD gold coins.",       "Some MONSTER_TYPEs still haunt our temple at LOCATION_NAME. Worshipers have no place to pray!","Our worshipers have returned. Please taketh thy GOLD_REWARD coin payment as thanks."},
      //CLEAR_CAVE 8
      {"'Twas forced out mine shanty at LOCATION_NAME by MONSTER_TYPE. I want not to live among filthy people.",           "I nearly died just walkin' by mine own shanty at LOCATION_NAME. I want to move back in.",      "I can now move back into thy own shanty. I have nothing to give, but I will tell others of thy deeds on my way out."},
      //ASSASSINATE_KING 9
      {"The tyrant NPC_NAME of LOCATION_NAME must be thrown! For the love of liberty, assassinate thy ruler!",             "NPC_NAME of LOCATION_NAME rules over all with a cruel fist. Strike them down to liberate thy people!", "Liberty is now within the grasp of these good people. Take this precious artifact as our thanks."},
      //ASSASSINATION
      {"That ADJECTIVE NPC_NAME of LOCATION_NAME is a threat to the kingdom. Smite them for GOLD_REWARD bounty.",          "That ADJECTIVE NPC_NAME of LOCATION_NAME still walks. Smite them with haste!",                 "That ADJECTIVE NPC_NAME is a threat no more! Here is thy payment of GOLD_REWARD gold."},
      //RESCUE_PRISONERS 11
      {"My subjects are held captive at LOCATION_NAME. Free them for GOLD_REWARD gold coins.",                             "My subjects still remain at LOCATION_NAME. Free them with haste!",                             "Thy subjects are free from their captors! Here is thy reward of GOLD_REWARD coins."},
      //PURGE_CITY 12
      {"The scum of LOCATION_NAME plan to march against thee! Purge all that live there for GOLD_REWARD coins!",           "LOCATION_NAME must be purged of all living things! Execute them at once!",                     "Those that stand against us will think twice now given the fate of LOCATION_NAME! Here are thy GOLD_REWARD coins."},
      //PURGE_HOLY 13
      {"The heathens of LOCATION_NAME do not believe Skara Brae has wings! They must be driven from their city!",          "Drive the infidels from LOCATION_NAME so Skara Brae's chosen ones can seed it at once!",       "Skara Brae herself will reward you for this most holy and noble of feats."},
      //ZOMBIE_PLAGUE 14
      {"The Deadite plague has overrun LOCATION_NAME. Purge the undead for GOLD_REWARD gold!",                             "LOCATION_NAME must be purged of all the undead before they spread to another town!",           "Thow has done grim work, but for the best of the land! Here is thy well deserved pay of GOLD_REWARD coins."},
      //FIND_ITEM 15
      {"NPC_NAME of LOCATION_NAME carries a fine ITEM_NAME. Bring it to me for GOLD_REWARD gold coins.",                   "NPC_NAME of LOCATION_NAME still holds the ITEM_NAME I desire. Bring it to me with haste!",     "Tis a most excellent ITEM_NAME. Thou has done me great service. Here is thy GOLD_REWARD coin reward."},
      //FIND_WEAPON 16
      {"Would thee be able to help a battle veteran? I hath lost mine old ITEM_NAME in LOCATION_NAME.",                    "'Twould lift my spirits to have mine old ITEM_NAME. Please go with haste and retrieve it.",    "By Skara Brae, I didn't think you'd come back with thy ITEM_NAME. Here be some coin for thanks."},
      //FIND_PELT 17
      {"My child's has rags for clothes and will freeze this Winter! Can thou spare a ITEM_NAME?",                         "We need the ITEM_NAME to survive the winter. Please help us!",                                 "My child is saved thanks to your kindness. I will tell others of your deeds!"},
      //FIND_LUTE 18
      {"NPC_NAME of LOCATION_NAME has thy Lute Of Destiny! I will give thee GOLD_REWARD coins to bring it to me.",         "NPC_NAME's Lute Of Destiny calls to me. Bring it to me from LOCATION_NAME for thy gold!",      "For the love of music, thank thee! Take all the gold I have!"},      
      //FIND_SONG 19
      {"NPC_NAME of LOCATION_NAME has a page from the Songs of Skara Brae. I will pay GOLD_REWARD coins for it!",          "Bring me the page of magic music. NPC_NAME at LOCATION_NAME does not deserve to own it!",      "A page from the Songs of Skara Brae! Now all I need is the Lute of Destiny! Here are thy GOLD_REWARD coins."},
      //FIND_PERSON 20
      {"My love is lost at LOCATION_NAME. Please find them for GOLD_REWARD coins!",                                        "My love is still lost at LOCATION_NAME. Please bring them back to me!",                        "I thank thee. Whilst I can't give you much, I can give thee GOLD_REWARD coins."},
      //FIND_PARENT 21
      {"Mine Pama gone! Mine Pama! Mine Pama at LOCATION_NAME! Find mine Pama!",                                           "Mine Pama gone at LOCATION_NAME! Find mine Pama! Mine Pama!",                                  "Thank thee! Mine Pama back! Thank thee! Take mine coin!"},
      //FIND_PET 22
      {"Puppy! Puppy! Me want mine own puppy!",                                                                            "Please puppy! Me need mine own puppy!",                                                        "A puppy! Thanks thanks! Take mine coin! A puppy!"},
      //FIND_PEARL 23
      {"The MONSTER_TYPE of legend has been sighted in the bay. Bring me the Lifepearl for GOLD_REWARD gold!",             "The MONSTER_TYPE is elusive. Retrieve its Lifepearl and I will give you GOLD_REWARD coins!",   "'Tis the most magic of items, at last! Here is thy well deserved pay of GOLD_REWARD gold!"},
      //FIND_MAGIC_ITEM 24
      {"Evil has befallen my apprentice who hides in LOCATION_NAME with my ITEM_NAME. Return it to me with haste!",        "My ITEM_NAME is still held by my sorcerous apprentice at LOCATION_NAME. Retrieve it for me.",  "My glorious ITEM_NAME is returned! I can teach you a SPELL as thanks!"},
      //FIND_CAERBANNOG 25
      {"Find me the last words of Olfin Bedwee of Rheged, carved in the very living rock of CAERBANNOG!",                  "The dreadful Cave of CAERBANNOG holds the mystic runes I must have. Find them for GOLD_REWARD coins!","You survived Caerbannog? Thou is unmatched in strength and valor and well deserve these GOLD_REWARD coins!"},
      //STEAL_PICK 26
      {"NPC_NAME of LOCATION_NAME has thy Magic Lockpick! I will give thee GOLD_REWARD coins to bring it to me.",          "NPC_NAME's Magic Lockpick must me mine! Bring it to me from LOCATION_NAME for thy gold!",      "Skara Brae, my Magic Lockpick! No reward for you! Muahahahahaha!"},     
      {"NPC_NAME of LOCATION_NAME has the rarest Riddlebook! Steal it for me and I give you GOLD_REWARD gold!",            "NPC_NAME's Riddlebook must me mine! Bring it to me from LOCATION_NAME for GOLD_REWARD gold!",  "At last! More riddles! I have no gold for you, but you earn my gratitude! Goodbye!"},      
      {"NPC_NAME of LOCATION_NAME plays Swine with loaded cubes! GOLD_REWARD coins to bring it to me.",                    "NPC_NAME's loaded cubes must me mine! Bring it to me from LOCATION_NAME for thy gold!",        "I shall never lose at Swine with thy loaded cubes! No reward for you! Muahahahahaha!"},     
      //STEAL_DAGGERS
      {"Five strangers are in town to deliver magic daggers for the king. Steal all five for a mighty reward!",            "The 5 travelers still hold the magic daggers. Steal them for me for thy great reward!",        "The five magic daggers! You are a mighty thief! Your reward is my great respect! Goodbye!"},
      {"The statue of the courtyard 'tis too serious. Knock it down for me for a laugh, and GOLD_REWARD gold!",            "The courtyard statue still stands. Strike it to pieces for GOLD_REWARD coins!",                "The statue has fallen! Too funny! You get no gold, but my priceless respect! Muahahahahaha!"},
      //CITY_REVENGE
      {"That ADJECTIVE NPC_NAME of LOCATION_NAME has done me wrong.  Wilt thou bring me justice for GOLD_REWARD coins?",   "That ADJECTIVE NPC_NAME of LOCATION_NAME still haunts me. Please end my misery by ending their breath!",  "I thank thee. Whilst I can't give you much, here are GOLD_REWARD coins."},
      {"NPC_NAME of LOCATION_NAME carries a message for me.  Wilt thou deliver it to me for GOLD_REWARD coins?",           "NPC_NAME of LOCATION_NAME still holds my message.  Please deliver it to me.",                  "I thank thee.  Whilst I can't give you much, here is GOLD_REWARD gold."},
      {"NPC_NAME vies for the hand of my love. Frighten them out of town and I will grant thee GOLD_REWARD gold.",         "NPC_NAME still courts my love. Frighten them out of our town for GOLD_REWARD coins!",          "My love has tanken to thy arms only. I thank thee, and grant you these GOLD_REWARD coins for your deeds!"},
      //BATTLE
      {"A regiment of beasts is marching to LOCATION_NAME. Wilt thou join the Brute Squad for GOLD_REWARD gold?",          "The battle lines are drawn outside the gates of LOCATION_NAME. Join us in battle!",            "The reinforcements have cleaned up the rest at LOCATION_NAME. Here is thy GOLD_REWARD coin payment."},
      {"An assassin lurks in our dungeon. I must stay by thy King's side. End this scum for GOLD_REWARD gold!",            "A scum assassin still lurks in thy dungeon. Kill them for GOLD_REWARD coins!",                 "Silencing that assassin was a great service for thy King. I grant you this large sum of GOLD_REWARD gold."},
      {"A massive beast stalks our dungeon. Investigating guards have disappeared. Bring proof of its end for GOLD_REWARD gold!", "We still hear the beast's roar from our dungeons. Slay it for GOLD_REWARD coins!",      "With the dungeon beast slain, you have done a mighty service for thy King. Here is GOLD_REWARD gold."},
      //MOVE_BEGGER
      {"NPC_NAME took mine corner: I need that corner back. 'Tis the best in the city. Make 'em leave for me.",            "NPC_NAME still stands at mine corner. Make 'em leave and you'll get what thee deserve.",       "I can return to me corner! I can give thee no coin, but I will tell all about thee good deeds!"},
      {"Thy beggers are ruining my buisness. I will pay GOLD_REWARD if ye find a way to 'relocate' them.",                 "My buisness still sufferes from all thee beggers about. Thy GOLD_REWARD awaits thee.",         "'Tis like a new town now the beggers be gone! I thank thee and grant thy good pay of GOLD_REWARD coins."},
      //TRAIN_DOG
      {"I need a fine dog to help protect my shoppe at night. Find me a hearty one for GOLD_REWARD gold.",                 "I am still in need of a dog to guard my shoppe at night. GOLD_REWARD coins awaits thee.",      "'Tis a fine guard dog!  Here are thy GOLD_REWARD gold coins."},
      {"I must stay by thy king's side, but thy stable is in need of a horse. Bring one for GOLD_REWARD coin!",            "I need a horse with haste, but must stay by the king's side. Retrieve one, quickly!",          "'Tis a very fine horse.  Here is thy well deserved pay - a mighty sum of GOLD_REWARD coins."},
      //SLAY_MONSTER
      {"A MONSTER_TYPE is seen close to our gates. I will stay to defend the commoners. Slay it for GOLD_REWARD gold!",    "You have no proof of slaying the MONSTER_TYPE outside our gates. I can not give you the bounty's reward.", "The beast is slain. Good work. Here are thy GOLD_REWARD coins."},
      {"The ship of the Dread Brigand Roberts is ravaging our port. Bring me his mask for GOLD_REWARD coins!",             "'Tis the ship of the Dread Pirate Roberts on our shores. Defeat him and bring me his mask",    "We are saved! Our ships can return to thy city! Here is thy well earned reward of GOLD_REWARD coins!"},
      {"You have been chosen to fight in The Arena! Slay thy foes or fall! Delight the crowd for GOLD_REWARD coins!",      "The spectators demand action! Fight for the masses for GOLD_REWARD gold!",                     "The spectators cheer thee! I salute you, Warrior of The Arena! Here is thy GOLD_REWARD gold!"},
      {"That ADJECTIVE NPC_NAME has escaped thy cell to flee the combat of The Arena, hiding in LOCATION_NAME. End them!", "GOLD_REWARD awaits thee for ending that ADJECTIVE NPC_NAME for their insolence!",              "Justice has been served. Here is thy reward of GOLD_REWARD coins. Nobody escapes The Arenamaster!"},
      {"Thy family is being forced fo fight in the Arena! Please free them - they deserve not this fate!",                 "The Arenamaster will soon throw my family to the beasts! For the love of Skara Brae, free them!","My family is safe and returns to me! I have no coin to give thee, but you have my eternal thanks!"},
      //TEACH_SPELL
      {"I must learn the SPELL_NAME spell. Discover its secrets and I will pay thee GOLD_REWARD coins!",                   "The SPELL_NAME spell still eludes me. Find its secrets and teach them to me for thy reward!",  "Here is your well earned GOLD_REWARD gold. I thank thee for thy precious information!"},
      //CLEAR_BANDITS
      {"The ADJECTIVE NPC_NAME gang is back in thy town! Purge them from thy streets for GOLD_REWARD coins! Hurry!",       "We are under the bow of the ADJECTIVE NPC_NAME gang! Liberate us for GOLD_REWARD gold as payment!", "The ADJECTIVE NPC_NAME gang will haunt us no more! Thank thee, and here is thy GOLD_REWARD gold!"},
      //BEAT_SWINE_PLAYER
      {"That ADJECTIVE NPC_NAME is undefeated in Swine. I suspect they have loaded cubes! Can thee defeat them in Swine?", "That ADJECTIVE NPC_NAME is still without loss in Swine. Dost thou have the skill to beat them?", "Huzzah! You have my congratulations for your victory and superior Swine strategy, O'PLAYER_NAME!"},
      //TREASURE_MAP
      {"I need GOLD_AMOUNT gold to pay off thy bounty. Recover my gold with this treasure map and earn the difference.",   "I am still in need of GOLD_AMOUNT coins for my bounty. Did you find my treasure?",             "Thank thee. I can clear thy name at last and worry not about bounty hunters! Take these coins as thanks."},
      //FIND_SERPENT_EGGS
      {"The making of cure potions requires serpent eggs from the caves. Bring me three for GOLD_REWARD coins.",           "I am still in the need of three cave serpent eggs to mix cure potions. GOLD_REWARD gold awaits thee.","You have my thanks. These eggs will make for profitable potions! Here is thy GOLD_REWARD gold!"},
      //FIND_DRAGON_EGG
      {"I have met thy might's peak and must consume a Dragon egg to grow stronger. Bring one for GOLD_REWARD gold.",      "GOLD_REWARD coins await thee when a Dragon's egg is laid at my feet. Move with haste!",        "Thou has done good work to help me better protect thy king. Here is thy reward, a sum of GOLD_REWARD gold."},
      //FIRE_FIGHTER
      {"'Tis a terrible fire in the DIR part of town! Extinguish it before too much damage is done!",                      "Thy fire still rages in the DIR part of the city! Help put it out with haste!",                "The fire is out and the city is saved! We have collected GOLD_REWARD gold to give thee as thanks!"},
      //CHASE_THIEF
      {"The master thief NPC_NAME is in town and too swift to catch! Bring them justice for GOLD_REWARD gold!",            "The elusive thief NPC_NAME still evades our capture! Drive them out for GOLD_REWARD coins!",   "NPC_NAME, that elusive knave, is gone! Here is thy GOLD_REWARD gold!"},
      //FIND_ELK_PELT
      {"Thy roof has born a hole that needs mending. I can use an elk pelt to seal thy breach. Can thou spare one?",       "Thy rain still breaches thy hut. An elk pelt will help thee mend my hovel.",                   "'Tis just what I need to mend thy roof. I shall tell others of your good deeds. Thank thee!"},
      //FIND_GEM
      {"I am in need of a ITEM_NAME for the hilt of a magic sword forged for the King. GOLD_REWARD gold awaits thee to bring one!","Thy King's sword still needs a ITEM_NAME for the hilt to be completed!",               "Perfect, this ADJECTIVE ITEM_NAME! Now I can finish the King's magic sword. Here is thy GOLD_REWARD gold as thanks."},
      //KILL_WEREWOLF
      {"A Werewolf is among us! Those without home fear we will be next! Save us! Find the cursed one and drive them out!","We are still stalked by a Werewolf at night! Kill the monster before more are lost!",          "We are saved! PLAYER_NAME has saved our town from the Wolfen! Three cheers for PLAYER_NAME!"},
      //MERCY_KILL
      {"I confess to be the Werewolf! My guilt of murder is unbearable! End me with a Royal Weapon to save thy soul!",     "I can not bear the weight of the Wolfen curse! Take thy life with a Royal Weapon with haste!", ""},
      //WEREWOLF_TOWN
      {"The Wolfen outcasts have been banished to LOCATION_NAME, but are ravaging our livestock. GOLD_REWARD gold to purge the Werewolf village!","Thee shall have GOLD_REWARD gold to burn down the Werewolf outcasts of LOCATION_NAME!","Our livestock is now safe from the Wolfen freaks. Here is thy GOLD_REWARD reward!"},
      //SLAY_VAMPIRE
      {"The vile Vampyre NPC_NAME has cursed our town! Slay them at LOCATION_NAME and burn out their coffin refuge to save us!","NPC_NAME the Vampyre still haunts our town. Drive them out of LOCATION_NAME and burn their coffin!","NPC_NAME be slain! Our town is saved! All hail PLAYER_NAME! PLAYER_NAME has saved our town!"},
      //GET_BOAT
      {"I am in need of a boat or ship! I have a precious gem found in the mines to trade for one!",                       "A magic gem from the mines is still up for trade for a fine vessel of the seas!",              "I thank thee! Here is thy precious gem. I am off to sail the high seas!"},
      //DESTROY_SIGNS
      {"Mine Pama make shoppe signs but no work! Pama so sad, no food! Break shoppe signs so mine Pama gets work!",        "Still shoppe signs, mine Pama needs work! Break all shoppe signs! Please, mine Pama!",         "Mine Pama has work! Mine Pama makes signs! Thanks! Thanks!"},
      //DESTROY_STATUE2
      {"Statue scares me! Statue scares me! Make statue go away!",                                                         "Statue still there! Make statue leave! Statue scares me!",                                     "Statue gone! Hurray!"},
      //BURN_COFFINS
      {"The Coffinsmith of this town invites death and Vampyres with his craft. Burn the coffins away to drive them out!", "There still be coffin making in this town. Tis a bad omen - burn them away!",                  "The Coffinsmith has left. Our town is relived of his burden! My thanks!"},
      //TAX_COLLECTOR
      {"Thy tax evader NPC_NAME has eluded my sight. Extract their debt of GOLD_AMOUNT gold and thee will earn a percentage as payment!", "NPC_NAME owes tax debt and still eludes me. Collect GOLD_AMOUNT coins from them and earn a percentage.", "Thy collections for this season are finally complete. Thank thee and here is thy ten-percent commission!"},
      //FIND_LEDGER
      {"My tax ledger has gone missing! The King demands complete records of revenue! This will cost me head if not found!","Thy tax ledger is still missing! For the love of Skara Brae, help me find it!",               "Huzzah! You have found my tax ledger! I shall cover thy taxes for this season! Thank thee!"},
      //TAX_IMPOSTER
      {"The knavish NPC_NAME has stolen a Tax-collector uniform and fleecing the townsfolk of their money! Stop them!",    "NPC_NAME is a Tax-collector imposter! End their reign of swindlery!",                          "The tax-imposter has been stopped! Thank thee. Your taxes for this season have been pardoned!"},
      //TAX_MOB
      {"The townsfolk have called the ADJECTIVE NPC_NAME gang to drive me out of town! Save me from their wrath!",         "The ADJECTIVE NPC_NAME gang still roams the street, seeking my ousting. Save me!",             "You have purged the NPC_NAME gang! Tax collecting in this town shall continue another season! Thank thee!"},
      //MARRIAGE
      {"My own NPC_NAME is without spouse, lonely and lost of spirit. Wilt thou take their hand in marriage for a dowry of home and horse?","A dowry of home and horse awaits thee to take NPC_NAME's hand in marriage",   "Rejoice! You may call me father! Treat NPC_NAME well, and thee will always have a home here."},
      //CITY_SIEGE
      {"The Beast Horde has reached the gates of our very town! Aid us in battle to defend our home!",                     "I can hear the running steps and screams from our citizens. Some beasts still linger in the city!", "Huzzah! The Beast Horde has been driven back. We shall rebuild and stand stronger than ever before."},
      //ESCORT_ADULT
      {"I need safe passage to LOCATION_NAME. I offer GOLD_REWARD coins to escort me through my journey through these dangerous lands?", "My people in LOCATION_NAME awaits my arrival. Please guide me there with haste!",          "I thank thee for thy protection in my journeys. Here is thy GOLD_REWARD gold. May Skara Brae guide you."},
      //ESCORT_KID
      {"I offer GOLD_REWARD coins to guarantee the safety so thy own kin can witness the battle brewing outside our gates.", "My little warrior needs to study the battle tactics outside our gates. See to their safety!", "I thank thee for the protection of my very own child. Here is thy GOLD_REWARD gold."},
      //CEREMONY
      {"The cultists attempt to resurrect their god at LOCATION_NAME. Stop their ceremony before we fall to its terrible wrath!","The god of the cultists is almost upon us! Stop their ceremony at LOCATION_NAME!",                 "The ceremony is halted! We are saved! Here is a simple offering as thanks."},
      //CULT_RESCUE = 73
      {"My love has been indoctrinated into that ADJECTIVE1 cult and cowers all day praying to some ADJECTIVE2 god!",      "The cultists have my love under their spell. Please help me shake them of their confused trance!","My love has returned to me! I thank thee for thy care and offer thee the coins I have."},
      //KAIJU = 74
      {"A colossal MONSTER_TYPE is approaching the gates! Drive it away from our home before it destroys us all!",         "The massive MONSTER_TYPE is almost upon us! Wilt thou lead it away from our gates?",           "The MONSTER_TYPE has disappeared over the horizon! Huzzah! Thee cheers for PLAYER_NAME!"},
      //DOGTRAP = 75 given by CHILD - their dog has been trapped by hunters trapping wolves
      {"Mine puppy! The trappers catch him with Woofen! In the camp! Free mine puppy I give you mine coin!",               "I need mine puppy! Trapped with Woofen in the guard camp!",                                    "Mine puppy! Thanks to thee! Take mine coin!"},
      //WEREWOLFTRAP = 76 given by MAN/WOMAN in a werewolf town - werewolf kin have been trapped by guards in a camp
      {"My love lives with the Wolfen curse and has been snared with their kin in the camp outside town! They are not monsters!","Free my love trapped be they by guards in the camp! Those with the Wolfen curse only want peace!","Rejoice! My love and their kin have been saved! Take these coins for thy heroism and grace!"},
      //MONSTERTRAP = 77 given by GUARD - brigands have trapped monsters to recruit for their army
      {"The camped Brigands have trapped beasts, aiming to train them. Disrupt their plans before they raise an army against us!","Keep the camped Brigands from training the beasts they captured! A fine purse awaits thee for this most heroic task!","Good work brave one! The Brigands shall not raise an army this day! Here is thy payment."},
      //PUBLIC_HOUSING = 78
      {"We did not choose life on the street. Please help us find a roof to live under before the weather takes its toll!", "A roof over a place to sleep and shelter will help us stay alive. Please help us!",           "Rejoice! We have a place to live! Thy compassion will not go unknown, O'PLAYER_NAME!"},
      //REMOVE_CULT = 79
      {"Those ADJECTIVE cultists babble in disharmony when I try to play in the city center. Can thee quiet them?",        "I want to play in the city center, but those ADJECTIVE cultists chant in dissonance with thy lute!","At last, I can perform in the city center again! Take thy daily contributions as thanks!"},
      //HAUNTED_HOUSE = 80
      {"Gray house is haunted! Ghosts and monsters! Knock it down! Make them leave!",                                      "Ghosts in gray house! Monsters and ghosts! Knock gray house down! Knock it down!",             "Ghost house gone! Thanks thanks! Take mine coin!"},
      //HAUNTED_FIND = 81
      {"That ADJECTIVE jester threw my songbook into that old gray house! Tis haunted! GOLD_REWARD coins if thee finds it!", "I will give thee GOLD_REWARD coins if you find my songbook in that old gray house!",         "My precious songbook! Thou are indeed brave - here are thy GOLD_REWARD coins!"},
      //HAUNTED_CLEAR = 82
      {"The ghost of thy lost love is imprisoned in this realm, haunting the old gray house. Can thou set them free?",     "The cursed gray house has become a prison for passed souls who haunt it. My lost love is there - free them!", "Thank thee! I feel the ghost of my lost love has been able to pass this world to meet Skara Brae!"},
      //CLOSE_HELLMOUTH = 83
      {"A Hellmouth is opening in that cursed gray house! A Raise-stone spell will seal it, but I know not that spell!",   "Thy gray house has a Hellmouth opening within! Plug it with Raise-stone before the Underworld's minions come through!", "We are saved! The Hellmouth is sealed! Take these coins as gratitude!"},
      //FIND_LUMBER1 = 84 given by ironsmith - retrieve lumber so ironsmith can make weapons
      {"I have and order for 12 spears from the guards but am out of lumber. GOLD_REWARD coins if you fetch me some.",     "GOLD_REWARD coins await you for some lumber. I must get busy making spears for the guard.",    "Thank thee for thy lumber. Here are GOLD_REWARD coins for thy labor."},
      //FIND_LUMBER2 = 85 given by barkeep - 
      {"Earn GOLD_REWARD coins if you fetch me some lumber - I need to make more barrels for meade before it ferments.",   "GOLD_REWARD coins await you for some lumber. I must build barrels for storing meade.",         "Here are GOLD_REWARD coins for thy labor for lumber. Tis fine wood for building barrels."},
      //FIND_LUMBER3 = 86 given by other shopkeeps
      {"This shoppe counter needs repairs. I will pay thee GOLD_REWARD coins if you fetch me good lumber.",                "GOLD_REWARD coins await you for some lumber. I must repair this shoppe counter.",              "My counters will look as if new with these fine planks. Take these GOLD_REWARD coins for thy dutiful work."},
      //FIND_LUMBER4 = 87
      {"My hovel repairs require lumber but I must stand this shift. I will give thee a quarter days coin for good lumber.","I have not the time to cut the lumber I need with this long shift. Fetch me some for good coin.","Yes, these are excellent planks. Here is one quarter a day's coin."},
      //FIX_PATH = 88 given by shopkeeps - bridge over muddy patch in the path
      {"That muddy patch on the path is diminishing buisness. If someone could only build lumber planks to cover them...", "My buisness would improve if the muddy patch on the path was covered with lumber planks.",     "Buisness is picking up already. Thank thee for helping thy buisness thrive. Here is a percentage of my profits this day."},
      //BUILD_PIER = 89 given by SWORD - build pier for easier travel
      {"If this town had a pier, I'd have a better launch site for thy seafaring adventures. Can thee construct with lumber well?", "I'd invest GOLD_REWARD coins to aid the construction of a lumber pier for this town.", "At last, a fine launching point for thy travels. Here is a small token of gratitude."},
      //RAISE_WATER = 90
      {"Our well right outiside of town has dried up. If thee knows the Raise-water spell, help us before we are forced to move",  "Our local mages know not the Raise-water spell and our wells have all but dried up!", "Our wells are full again! We can stay in this beautiful city under the skies of Skara Brae! Rejoice!"},
      //COLLECT_FLORETS1 = 91 given by MAN/WOMAN, not a shopkeeper - collect a full floretbox worth of florets for a garden
      {"I am in the need of florets to seed my garden. If I only had 5 of each color, that would be sufficient...",        "Thy garden will not blossom without florets...5 of each color will do.",                       "How kind of thee - these florets will do just right to seed thy garden.  Thank thee!"},
      //COLLECT_FLORETS2 = 92 given by WISE - collect a full floretbox worth of florets for floretlade
      {"I am in need of 10 florets of each color to cast Floretlade, but thy back has siezed and I can not bend!",         "Thy back - tis injured and I can not bend to pick florets! How will I be able to cast Floretlade?","You are very kind to lend me thy florets. I can teach thee a SPELL as gratitude."},
      //COLLECT_FLORETS3 = 93 given by CHILD
      {"Thy COLOR is mine favorite! Help me find COLOR flowers! I want ten!",                                              "Ten COLOR flowers! COLOR is mine favorite! Help me find ten!",                                 "Ten COLOR flowers! Thanks thanks! Have mine coin!"},
      //RAIN_MAKER1 = 94 given by MAN/WOMAN - make it rain by the end of the day for crops to grow
      {"Tis too dry for thy crops and the local mage knows not the Tempest spell. Make it rain this day for GOLD_REWARD coins!","If thee can save thy crops and make it rain this day, I will give thee GOLD_REWARD coins!","Thy crops are saved! Here are thy GOLD_REWARD coins as thanks!"},
      //RAIN_MAKER2 = 95 given by single MAN/WOMON - make it rain on a specific day to delay a wedding
      {"Thy wedding is on day WEDDING_DAY and I am not ready. If thee can make it rain on day WEDDING_DAY, I will pay thee GOLD_REWARD coins!","Tis close to day WEDDING_DAY! Make it rain on that day for GOLD_REWARD coins!","Thy wedding is postponed - I have time to plan thy escape! Here is thy GOLD_REWARD payment."},
      //HEAL_WARRIOR = 96 given by SWORD with 0 agility - injured warrior gets agility restored when healed with potion/spell - gem reward
      {"Thy armor failed me in battle and I can not travel without risking death. Does thou know the healing arts?",       "Thy wounds are too great - I can not walk to the mages shoppe for healing. Can thee aid a fellow adventurer?", "Thank thee for thy grace - I am well enough to travel now. Here is a gem I found in my last adventure."},
      //HEAL_TAXMAN = 97 given by TAXMAN - broken their leg chasing down a tax-evader
      {"I have twisted thy ankle chasing a tax-evader and can not move with haste! By spell or potion, I beg of thee to help!",  "I have fallen and I can not get up! Be there a healer about? I am in need of healing!", "Thank thee, O'PLAYER_NAME. Consider thy taxes paid in full for the season!"},
      //CURE_POISONED = 98 given by MAN/WOMAN - bitten by a snake, needs cure spell or potion
      {"This ADJECTIVE1 viper has stricken me leg! I need a poison's cure lest I perrish in the street!",                  "Some ADJECTIVE2 viper is about and has stricken me with venom! Does thou have a poison's cure?","I am feeling much better - thank thee, O'PLAYER_NAME! Be on the lookout for that ADJECTIVE2 viper!"},
      //CURE_KING = 99 given by royalty - cure King poisoned by someone in the castle, followed up with a bounty for them
      {"I feel unwell...thy blood boils...I have been poisoned! Quick! By spell or by potion, bring a cure!",              "A poison runs through me! I demand a cure - a potion or spell with haste!",                    "Must have been NPC_NAME who poisioned my goblet! Bring me NPC_NAME's head and I will award thee a grand treasure!"},
      //FIND_SCALES = 100 given by ironsmith - needs dragon/seaserpent scales to make armor for king - if dragon, puts one in a nearby cave
      {"Thy king has ordered me to fashion armor, made of ITEM_NAME. Bring me some and I will grant thee GOLD_REWARD coins!","I need ITEM_NAME to craft the King's new armor. Fetch some and GOLD_REWARD coins awaits thee.", "These are ADJECTIVE ITEM_NAME and will make excellent armor for thy King. Here is thy payment of GOLD_REWARD coins."},
      //FEED_POOR = 101 given by MAN/WOMAN - donate 15 rations to help the downtrodden make it through winter
      {"The winter has been hard on the downtrodden. We are collecting rations to help and are 15 short of our goal",      "We need 15 rations to meet our goal to help the downtrodden through this winter",              "Thy donation is generous. Thank thee, O'PLAYER_NAME!"},
      //SEWER_LOST = 102 given by MAN/WOMAN - lost child in the sewer
      {"My child is lost! They were playing by thy sewer entrance! Please, PLAYER_NAME, find thy kin!",                    "My child, they be lost in the sewer! Find them, please!",                                      "Thy kin has been returned to me! Thank thee, O'ADJECTIVE PLAYER_NAME!"},
      //SEWER_CLOG = 103 given by guard - clear clog in the sewer
      {"Lumber has washed into thy sewer, causing a clog. I must see this post but offer GOLD_REWARD coins if thee clears it", "GOLD_REWARD coin awaits thee to clear the lumber clogging thy sewers",                     "Here is thy payment of GOLD_REWARD coins for your diligence and hard work. Thank thee, O'PLAYER_NAME"},
      //SEWER_RATS = 104 given by butcher/barkeep - clear rats from the sewer
      {"These ADJECTIVE1 rats be crawling from thy sewers and tearing through my indredients! Clear them for GOLD_REWARD gold!", "I can't keep thy ingredients stocked with these ADJECTIVE2 rats! Purge them from the sewer for GOLD_REWARD coins!", "With those rats gone I can safely store thy ingredients! Here are GOLD_REWARD coins as payment for thy services"},
      //SEWER_SLAY = 105 given by adventurer - slay giant monster in the sewer
      {"Me companions be training in the sewers and were dragged off by a great beast!",                                   "Something large and vile be skulking in thy sewers!",                                          "Good work, brave PLAYER_NAME! These sewers are safe to train in again!"},
      //SEWER_SIEGE = 106 given by guard - clear invading siege army in the sewer
      {"The beast army aims to invade the city by skulking through the sewers! Purge them before they emerge in the streets!", "A regiment of beasts aims to enter the city through the sewers! GOLD_REWARD coins to help stop them!", "Our town is no longer under siege! Well done, valliant PLAYER_NAME! Here are GOLD_REWARD coins for thy bravery!"},
      //SEWER_CHUD = 107 given by guard - clear Chud from sewer and around the entrance - night
      {"The Chud are emerging round the sewer entrance and snatching citizens! End them for GOLD_REWARD coins!",           "The legion of guard offer GOLD_REWARD coins if thee can end the Chud menace around and in the sewer!", "The Chuds have been vanquished! You are brave and strong, O'PLAYER_NAME! Here are thy GOLD_REWARD coins!"},
      //PRANK_PARCEL = 108 given by jester - give exploding parcel to guard
      {"I will grant thee 1000 gold for you to give this completely normal parcel to guard NPC_NAME! 'Twill be great fun!", "All thee has to do is give the non-explosive parcel to guard NPC_NAME and 1000 coins will be yours! We will have such a laugh!", "HAAAHAAA! 'Twas great fun! Well done, O'PLAYER_NAME! Whelp, time for thee to flee!"},
      //DELIVER_MEAT = 109 given by butcher - deliver meat by a certain day before it spoils
      {"I have an order of meat ready for NPC_NAME. GOLD_REWARD coins to deliver by day DAY before it spoils!",            "This order of meats will spoil if not delivered to NPC_NAME by day DAY!",                      "Thank thee for thy prompt delivery. Here is thy payment of GOLD_REWARD gold."},
      //TEACH_3_DOORS = 110  given by castle wise with dungeon - teach the 3-doors puzzle
      {"The ghost of thy master haunts the dungeon and oversees the Puzzle of the Three Doors. Learn its secrets and teach me for a PRIZE","I must know the secrets of the Puzzle of the Three Doors. Teach me and I will grant thee a magic treasure.","At last I know the secrets of my former master! Here is thy PRIZE, O'PLAYER_NAME!"},
      //TEACH_3_TOWERS = 111 given by sword - teach the 3-towers puzzle
      {"I failed the Puzzle of the Three Towers while exploring thy favorite cave. Teach me its secret for a PRIZE",       "I need to pass the Puzzle of the Three Towers to complete thy adventures! I offer magic gems for thee to teach me!","At last I can complete my adventures! Thank thee - here is thy PRIZE!"},
      //FIND_LEDGER2 = 112 given by taxman - tax ledger thrown into haunted house by the jester
      {"That ADJECTIVE jester threw my tax ledger into that old gray haunted house! Retrieve it and thy taxes are pardoned!", "I will pardon thy taxes if you find my tax ledger in that old gray haunted house!",         "My tax ledger! Thou are indeed brave - thy taxes are pardoned for this season!"}
      };

   public Mission()
   {
      state = IN_PROGRESS;
      type = "";
      startStory = "";
      middleStory = "";
      endStory = "";
      info = "";
      worldRow = -1;
      worldCol = -1;
      day = -1;
      goldReward = 0;
      itemReward = null;
      clearLocation = "";
      target = null;
      targetHolder = "";
      targetRow = -1;
      targetCol = -1;
      failed = false;
      missionType = -1;
   }
   
   //set starting mission when you create a player
   public Mission(boolean isVampire, boolean isWerewolf, boolean isFramed)
   {
      state = IN_PROGRESS;
      startStory = "none";
      middleStory = "none";
      endStory = "none";
      if(isVampire)
      {
         info = "Purge the Vampyric curse";
         type = "Curse";
         missionType = CURE_VAMPIRE;
      }
      else if(isWerewolf)
      {
         info = "Purge the Wolfen curse";
         type = "Werewolf";
         missionType = CURE_WEREWOLF;
      }
      else if(isFramed)
      {
         info = "Clear thy reputation";
         type = "Framed";
         missionType = CLEAR_NAME;
      }
      else
      {
         info = "Discover the number, find the Red Square";
         type = "Main";
         missionType = MAIN_QUEST;
      }
      worldRow = -1;
      worldCol = -1;
      day = -1;
      goldReward = 0;
      itemReward = null;
      clearLocation = "none";
      target = null;
      targetHolder = "none";
      targetRow = -1;
      targetCol = -1;
      failed = false;
   }

   //set mission when you get the crown
   public Mission(String t)
   {
      state = IN_PROGRESS;
      startStory = "none";
      middleStory = "none";
      endStory = "none";
          
      info = "Purge Death from this plane";
      type = "Death";
    
      worldRow = -1;
      worldCol = -1;
      day = -1;
      goldReward = 0;
      itemReward = null;
      clearLocation = "none";
      target = null;
      targetHolder = "none";
      targetRow = -1;
      targetCol = -1;
      failed = false;
      missionType = KILL_DEATH;
   }

  //pre: story.length == 3 where [0] is the startStory, [1] is the middleStory, [2] is the endStory
   public Mission(String t, String[]story, int wRow, int wCol, int gold, String loc, byte mt, int d)
   {
      type = t;
      state = IN_PROGRESS;
      startStory = story[0];
      middleStory = story[1];
      endStory = story[2];
      worldRow = wRow;
      worldCol = wCol;
      day = d;
      goldReward = gold;
      itemReward = null;
      clearLocation = loc;
      target = null;
      targetHolder = "";
      targetRow = -1;
      targetCol = -1;
      failed = false;
      missionType = mt;
      info = buildInfo();
   }

   public Mission(String t, String[]story, int wRow, int wCol, int gold, Item iw, String loc, Item targ, String targHolder, int targR, int targC, byte mt, int d)
   {
      type = t;
      state = IN_PROGRESS;
      startStory = story[0];
      middleStory = story[1];
      endStory = story[2];
      worldRow = wRow;
      worldCol = wCol;
      day = d;
      goldReward = gold;
      itemReward = iw;
      clearLocation = loc;
      target = targ;
      targetHolder = targHolder;
      targetRow = targR;
      targetCol = targC;
      failed = false;
      missionType = mt;
      info = buildInfo();
   }

   public Mission(String t, String[]story, int wRow, int wCol, int gold, Item iw, String loc, Item targ, String targHolder, int targR, int targC, boolean fa, byte mt, int d)
   {
      this(t, story,  wRow,  wCol,  gold,  iw,  loc,  targ,  targHolder,  targR,  targC, mt, d);
      failed = fa;
      info = buildInfo();
   }

   public int getDay()
   {
      return day;
   }
   
   public void setDay(int d)
   {
      day = d;
   }

   public boolean getFailed()
   {
      return failed;
   }

   public void setFailed(boolean fa)
   {
      failed = fa;
   }

   public String getType()
   {
      return type;
   }
   
   public byte getMissionType()
   {
      return missionType;
   }

   public void setType(String t)
   {
      type = t;
   }

   public byte getState()
   {
      return state;
   }

   public void setState(byte s)
   {
      state = s;
   }

   public String getStartStory()
   {
      return startStory;
   }

   public void setStartStory(String ss)
   {
      startStory = ss;
   }

   public String getMiddleStory()
   {
      return middleStory;
   }

   public void setMiddleStory(String ms)
   {
      middleStory = ms;
   }

   public String getEndStory()
   {
      return endStory;
   }

   public void setEndStory(String es)
   {
      endStory = es;
   }

//returns the short info that is displayed to the user in the Map
   public String buildInfo()
   {
      if(type.equals("Main")) //starting vampire mission
         return "Discover the number, find the Red Square";
      if(type.equals("Curse")) //starting vampire mission
         return "Purge the Vampyric curse";
      if(type.equals("Werewolf")) //starting werewolf mission
         return "Purge the Wolfen curse";
      if(type.equals("Framed")) //starting framed mission
         return "Clear thy reputation";
      if(type.equals("Death")) //given when main mission is cleared
         return "Purge Death from this plane";
      if((missionType == COLLECT_FLORETS1 || missionType == COLLECT_FLORETS2 || missionType == COLLECT_FLORETS3) && !targetHolder.equals("none"))
      {
         if(missionType == COLLECT_FLORETS3)
         {
            String favoriteColor = startStory.split(" ")[1];   //the 2nd word in the start story is the color we want
            return "("+worldCol+"-"+worldRow+"):Collect 10 "+favoriteColor+" florets for "+targetHolder;
         }
         return "("+worldCol+"-"+worldRow+"):Collect florets for "+targetHolder;   
      }
      else if(missionType == SEWER_LOST)
      {
         return "("+worldCol+"-"+worldRow+"):Rescue child lost in the sewer";
      }
      else if(missionType == FEED_POOR && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Collect 15 rations for "+targetHolder + " to feed the downtrodden";
      }
      else  if((missionType == HEAL_WARRIOR || missionType == HEAL_TAXMAN) && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Heal "+targetHolder + " with spell or potions";   
      }
      else  if((missionType == CURE_POISONED || missionType == CURE_KING) && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Cure "+targetHolder + " with spell or potion";   
      }
      else if((missionType == RAIN_MAKER1 || missionType == RAIN_MAKER2) && clearLocation.length()>0 && !clearLocation.equals("none") && day != -1)
      {
         return "("+worldCol+"-"+worldRow+"):Summon rain on day "+day + " in " + clearLocation;
      }
      else if(missionType == RAISE_WATER && clearLocation.length()>0 && !clearLocation.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Raise water for a well near "+clearLocation;   
      }
      else if(missionType == FIX_PATH && clearLocation.length()>0 && !clearLocation.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Cover the muddy stone paths of "+clearLocation;   
      }
      else if(missionType == SEWER_CLOG && clearLocation.length()>0 && !clearLocation.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Clear the lumber clog in "+clearLocation;   
      }
      else if(missionType == SEWER_SIEGE && clearLocation.length()>0 && !clearLocation.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Purge the invading army from "+clearLocation;   
      }
      else if(missionType == SEWER_RATS && clearLocation.length()>0 && !clearLocation.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Clear rats from "+clearLocation;   
      }
      else if(missionType == SEWER_CHUD && clearLocation.length()>0 && !clearLocation.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Eliminate the Chud in and around "+clearLocation;   
      }
      else if(missionType == BUILD_PIER && clearLocation.length()>0 && !clearLocation.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Build a lumber pier near "+clearLocation;   
      }
      else if(type.equals("Treasure") && !targetHolder.equals("none"))
      {
         if(targetRow!=-1 && targetCol!=-1)
         {
            return "("+worldCol+"-"+worldRow+"):find "+targetHolder+"'s treasure near (" + targetRow + "-" + targetCol + ")";
         }
         if(goldReward > 0)  
         {  
            return "("+worldCol+"-"+worldRow+"):find "+goldReward+" gold for "+targetHolder;
         }
         return "("+worldCol+"-"+worldRow+"):find gold for "+targetHolder+"'s bounty";
      }
      else if(type.equals("Boat") && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Acquire a boat or ship for "+targetHolder;   
      }
      else if(type.equals("Collect") && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Collect overdue taxes from "+targetHolder;   
      }
      else if(type.equals("Break") && clearLocation.length()>0 && missionType == DESTROY_SIGNS)
      {
         return "("+worldCol+"-"+worldRow+"):Break all shoppe signs of "+clearLocation;   
      }
      else if(type.equals("Break") && clearLocation.length()>0 && missionType == HAUNTED_HOUSE)
      {
         return "("+worldCol+"-"+worldRow+"):Level the gray haunted house at "+clearLocation;   
      }
      else if(type.equals("Burn") && clearLocation.length()>0)
      {
         return "("+worldCol+"-"+worldRow+"):Burn all coffins out of "+clearLocation;   
      }
      else if(type.equals("Save") && clearLocation.length()>0 && !targetHolder.equals("none"))
      {
         if(missionType == KAIJU)
         {
            return "("+worldCol+"-"+worldRow+"):Lead the threat away from "+clearLocation;
         }
         if(targetRow!=-1 && targetCol!=-1)
         {
            return "("+worldCol+"-"+worldRow+"):"+type+" the Taxman from the "+targetHolder+" gang";
         }
         return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation+" from the "+targetHolder+" gang";
      }
      else if(type.equals("Save") && clearLocation.length()>0 && targetHolder.equals("none"))
      {
         if(missionType == PUBLIC_HOUSING && !clearLocation.equals("none"))
         {
            return "("+worldCol+"-"+worldRow+"):Find a house for the downtrodden to live in";
         }
         return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation+" from the Werewolf";
      }
      else if(type.equals("Siege") && clearLocation.length()>0 && targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Save "+clearLocation+" from the Beast Horde";
      }
      else if(type.equals("Mercy") && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Mercy kill Werewolf "+targetHolder+" with Royal arms";
      }
      else if(type.equals("Marry") && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Marry "+targetHolder+" and inherit a dowry";
      }
      else if(type.equals("Swine") && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Defeat "+targetHolder+" in a game of Swine";
      }
      else if(type.equals("Clear") && clearLocation.length()>0)
      {
         if(missionType == DOGTRAP && clearLocation.length()>0)
         {
            if(targetRow==-1 && targetCol==-1)
               return "("+worldCol+"-"+worldRow+"):Rescue trapped dog in "+clearLocation;
            return "("+worldCol+"-"+worldRow+"):Rescue trapped dog at (" + targetCol + "-" + targetRow + ")";
         }
         else if(missionType == WEREWOLFTRAP && clearLocation.length()>0)
         {
            if(targetRow==-1 && targetCol==-1)
               return "("+worldCol+"-"+worldRow+"):Rescue trapped Werewolves at "+clearLocation;
            return "("+worldCol+"-"+worldRow+"):Rescue trapped Werewolves at (" + targetCol + "-" + targetRow + ")";
         }
         else if(missionType == MONSTERTRAP && clearLocation.length()>0)
         {
            if(targetRow==-1 && targetCol==-1)
               return "("+worldCol+"-"+worldRow+"):Disrupt the Brigand camp at "+clearLocation;
            return "("+worldCol+"-"+worldRow+"):Disrupt the Brigand camp at (" + targetCol + "-" + targetRow + ")";
         }
         else if(missionType == HAUNTED_CLEAR)
         {
            return "("+worldCol+"-"+worldRow+"):Exorcise phantoms from the haunted house";
         }
         else if(missionType == CLOSE_HELLMOUTH)
         {
            return "("+worldCol+"-"+worldRow+"):Seal the Hellmouth with Raise-stone spell";
         }
         else
         {
            if(targetRow==-1 && targetCol==-1)
               return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation;
            else if(targetRow!=-1 && targetCol!=-1)
               return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation + " at (" + targetCol + "-" + targetRow + ")";
         }
      }
      else if(type.equals("Purge") && clearLocation.length()>0)
      {
         if(targetRow==-1 && targetCol==-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation;
         else if(targetRow!=-1 && targetCol!=-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation + " at (" + targetCol + "-" + targetRow + ")";
      }
      else if(type.equals("Extinguish") && clearLocation.length()>0)
      {
         return "("+worldCol+"-"+worldRow+"):"+type+" the fire at "+clearLocation;
      }
      else if(type.equals("Arena") && clearLocation.length()>0)
      {
         if(targetRow==-1 && targetCol==-1)
            return "("+worldCol+"-"+worldRow+"):contend in "+clearLocation;
         else if(targetRow!=-1 && targetCol!=-1)
            return "("+worldCol+"-"+worldRow+"):contend in "+clearLocation + " at (" + targetCol + "-" + targetRow + ")";
      }
      else if(type.equals("Escort") && clearLocation.length()>0 && !targetHolder.equals("none"))
      {
         if(missionType == ESCORT_KID)
         {
            if(targetRow==-1 && targetCol==-1)
               return "("+worldCol+"-"+worldRow+"):Protect "+ targetHolder +"'s child at the battle of "+ clearLocation;
            else if(targetRow!=-1 && targetCol!=-1)
               return "("+worldCol+"-"+worldRow+"):Protect "+ targetHolder +"'s child at (" + targetCol + "-" + targetRow + ")";
         }
         else //if (missionType == ESCORT_ADULT)
         {
            if(targetRow==-1 && targetCol==-1)
               return "Escort "+ targetHolder +" to "+ clearLocation;
            else if(targetRow!=-1 && targetCol!=-1)
               return "Escort "+ targetHolder +" to "+ clearLocation + " at (" + targetCol + "-" + targetRow + ")";
         }
      }
      else if(type.equals("Battle") && clearLocation.length()>0)
      {
         if(targetRow==-1 && targetCol==-1)
            return "("+worldCol+"-"+worldRow+"):Win the day at the battle of "+clearLocation;
         else if(targetRow!=-1 && targetCol!=-1)
            return "("+worldCol+"-"+worldRow+"):Win the battle of "+clearLocation + " at (" + targetCol + "-" + targetRow + ")";
      }
      else if(type.equals("Ceremony") && clearLocation.length()>0)
      {
         if(targetRow==-1 && targetCol==-1)
            return "("+worldCol+"-"+worldRow+"):Halt the "+clearLocation + "ceremony";
         else if(targetRow!=-1 && targetCol!=-1)
            return "("+worldCol+"-"+worldRow+"):Halt the ceremony at (" + targetCol + "-" + targetRow + ")";
      }
      else if(missionType == CULT_RESCUE)
      {
         return "("+worldCol+"-"+worldRow+"):Free cultists from their trance";
      }
      else if(type.equals("Rescue") && clearLocation.length()>0)
      {
         if(targetRow==-1 && targetCol==-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation;
         else if(targetRow!=-1 && targetCol!=-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation + " at (" + targetCol + "-" + targetRow + ")";
      }
      else if(type.equals("Harvest") && target!=null && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName()+" for " +targetHolder;
      }
      else if(type.equals("Find") && target!=null)
      {
         if(!targetHolder.equals("none"))
         {
            if(targetRow!=-1 && targetCol!=-1)
               return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+"'s "+target.getName() + " at (" + targetCol + "-" + targetRow + ")";
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+"'s "+target.getName();   
         }
         else //if(targetHolder.equals("none"))
         {
            if(targetRow!=-1 && targetCol!=-1)
               return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName() + " at (" + targetCol + "-" + targetRow + ")";
            return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName() + " near (" + worldCol + "-" + worldRow + ")";   
         }
      }
      else if(type.equals("Give") && target!=null && !targetHolder.equals("none"))
      {
         String part = "";
         String itemName = target.getName();
         if(itemName.contains("prank-parcel"))
         {
            itemName = "prank-parcel";
         }
         else if(itemName.contains("parcel"))
         {
            itemName = "parcel";
         }
         if(targetRow==-1 && targetCol==-1)
            part = "("+worldCol+"-"+worldRow+"):"+type+" "+itemName + " to " + targetHolder;   
         else if(targetRow!=-1 && targetCol!=-1)
            part = "("+worldCol+"-"+worldRow+"):"+type+" "+itemName + " to " + targetHolder + " at (" + targetCol + "-" + targetRow + ")";
         if(day != -1)
            part += " by day " + day;
         return part;   
      }
      else if(type.equals("Steal") && target!=null && !targetHolder.equals("none"))
      {
         if(targetRow==-1 && targetCol==-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+"'s "+target.getName();   
         else if(targetRow!=-1 && targetCol!=-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+"'s "+target.getName() + " at (" + targetCol + "-" + targetRow + ")";
      }
      else if(type.equals("Steal") && target!=null && targetHolder.equals("none"))
      {
         if(targetRow==-1 && targetCol==-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName() + " near (" + worldCol + "-" + worldRow + ")";   
         else if(targetRow!=-1 && targetCol!=-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName() + " at (" + targetCol + "-" + targetRow + ")";
      }
      else if(type.equals("Steal") && target!=null)
      {
         return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName();
      }
      else if(type.equals("Assassinate") && target!=null)
      {
         if(targetRow!=-1 && targetCol!=-1)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName().replace("-bounty","").replace("-head","") + " at (" + targetCol + "-" + targetRow + ")";
         else if(clearLocation.length()>0 && !clearLocation.equals("none"))
            return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName().replace("-bounty","").replace("-head","") + " at " + clearLocation; 
         return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName().replace("-bounty","").replace("-head","");
      }
      else if(type.equals("Remove"))
      {
         if(missionType == REMOVE_CULT && clearLocation!=null && !clearLocation.equals("none"))
         {
            return "("+worldCol+"-"+worldRow+"):Quiet the cultists in the center of "+clearLocation;
         }
         if(!targetHolder.equals("none"))
         {
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder;
         }
      }
      else if((type.equals("Frighten") || type.equals("Chase")) && !targetHolder.equals("none"))
      {
         return "("+worldCol+"-"+worldRow+"):Remove "+targetHolder;
      }
      else if(type.equals("Train") && !targetHolder.equals("none") && target!=null)
      {
         return "("+worldCol+"-"+worldRow+"):Bring a "+target.getName()+" to "+targetHolder;
      }
      else if(type.equals("Slay") && target!=null)
      {
         if(clearLocation.length()>0 && !targetHolder.equals("none") && targetRow>=0 && targetCol>=0)
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder + " at " + clearLocation+"(" + targetCol + "-" + targetRow + ")";
         if(clearLocation.length()>0 && !targetHolder.equals("none"))
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder + " at " + clearLocation;
         return "("+worldCol+"-"+worldRow+"):"+type+" the "+target.getName().replace("-head","") + " near (" + worldCol + "-" + worldRow + ")";
      }
      else if(type.equals("Roberts") && worldCol!=-1 && worldRow!=-1)
      {
         return "("+worldCol+"-"+worldRow+"):Defeat Dread Brigand Roberts near (" + worldCol + "-" + worldRow + ")";
      }
      else if(type.equals("Destroy") && target!=null)
      {
         if(clearLocation.length()>0 && !targetHolder.equals("none"))
            return "("+worldCol+"-"+worldRow+"):"+type+" the statue at " + clearLocation;
         return "("+worldCol+"-"+worldRow+"):"+type+" the statue near (" + worldCol + "-" + worldRow + ")";
      }
      else if(type.equals("Teach") && !targetHolder.equals("none"))
      {
         if(missionType == TEACH_3_DOORS)
         {
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+" the Three Doors Puzzle";
         }
         if(missionType == TEACH_3_TOWERS)
         {
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+" the Three Towers Puzzle";
         }
         if( target!=null )
         {
            return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+" the "+target.getName()+" spell";   
         }
      } 
      if(clearLocation.length()>0 && target == null && targetRow==-1 && targetCol==-1)
         return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation;
      else if(clearLocation.length()>0 && target == null && targetRow!=-1 && targetCol!=-1)
         return "("+worldCol+"-"+worldRow+"):"+type+" "+clearLocation + " at (" + targetCol + "-" + targetRow + ")";
      else if(target!=null && !targetHolder.equals("none") && targetRow==-1 && targetCol==-1)
         return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+"'s "+target.getName();   
      else if(target!=null && !targetHolder.equals("none") && targetRow!=-1 && targetCol!=-1)
         return "("+worldCol+"-"+worldRow+"):"+type+" "+targetHolder+"'s "+target.getName() + " at (" + targetCol + "-" + targetRow + ")";
      else if(target!=null && clearLocation.length()==0 && targetRow==-1 && targetCol==-1)
         return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName().replace("-bounty","");
      else if(target!=null && clearLocation.length()>0 && targetRow==-1 && targetCol==-1)
         return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName().replace("-bounty","") + " at " + clearLocation; 
      else if(target!=null && clearLocation.length()>0 && targetRow!=-1 && targetCol!=-1)
         return "("+worldCol+"-"+worldRow+"):"+type+" "+target.getName().replace("-bounty","") + " at (" + targetCol + "-" + targetRow + ")";  
      return "?";
   }

   public String getInfo()
   {
      return info;
   }

   public void setInfo(String i)
   {
      info = i;
   }

   public int getWorldRow()
   {
      return worldRow;
   }

   public void setWorldRow(int wr)
   {
      worldRow = wr;
   }

   public int getWorldCol()
   {
      return worldCol;
   }

   public void setWorldCol(int wc)
   {
      worldCol = wc;
   }

   public int getGoldReward()
   {
      return goldReward;
   }

   public void setGoldReward(int gw)
   {
      goldReward = gw;
   }

   public Item getItemReward()
   {
      return itemReward;
   }

   public void setItemReward(Item ir)
   {
      itemReward = ir;
   }

   public String getClearLocation()
   {
      return clearLocation;
   }

   public void setClearLocation(String cl)
   {
      clearLocation = cl;
   }

   public Item getTarget()
   {
      return target;
   }

   public void setTarget(Item t)
   {
      target = t;
   }

   public String getTargetHolder()
   {
      return targetHolder;
   }

   public void setTargetHolder(String th)
   {
      targetHolder = th;
   }

   public int getTargetRow()
   {
      return targetRow;
   }

   public void setTargetRow(int wr)
   {
      targetRow = wr;
   }

   public int getTargetCol()
   {
      return targetCol;
   }

   public void setTargetCol(int wc)
   {
      targetCol = wc;
   }
   
   public static void checkMissionTimeOut()
   {
      boolean missionRemoved = false;
      for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(m.getDay() != -1 && CultimaPanel.days > m.getDay())
         {
            if(m.getTarget()!=null)
            {
               Item targ = m.getTarget();
               if(targ.getName().contains("parcel") && CultimaPanel.player.hasItem("parcel"))
               {
                  if(CultimaPanel.player.hasItem("receipt"))
                  {  //we have the receipt, so we must have delivered it on time
                     continue;
                  }
                  CultimaPanel.player.removeItem("parcel");
               }
            }
            CultimaPanel.sendMessage(m.getType() + " mission failed");
            CultimaPanel.player.addReputation(-5);
            CultimaPanel.missionStack.remove(i);
            missionRemoved = true;
         }
         if(!CultimaPanel.player.isVampire() && !CultimaPanel.player.isWerewolf() && m.getType().equals("Curse"))
         {  //check to see if we have a curse mission, but are no longer a vampire or werewolf - might happen if you get cursed, die, then reload
            CultimaPanel.missionStack.remove(i);
            missionRemoved = true;
         }
      }
      if(missionRemoved)
      {
         FileManager.writeOutMissionStack(CultimaPanel.missionStack, "data/missions.txt");
      }
   }
   
   //called when we successfully complete a mission
   public static void finishMission(Mission currMission, NPCPlayer selected, int rep)
   {
      currMission.setState(Mission.FINISHED);
      Player.stats[Player.MISSIONS_COMPLETED]++;
      if(Player.stats[Player.MISSIONS_COMPLETED] >= 50)
         Achievement.earnAchievement(Achievement.TASK_MASTER);
      CultimaPanel.player.addReputation(rep);
      if(selected != null)
      {
         selected.setReputation(CultimaPanel.player.getReputationRaw());   //you completed a mission - make them allignment compatible with you
      }
   }

   public boolean isFinished()
   {
      if((missionType == COLLECT_FLORETS1) && CultimaPanel.player.fiveFlowersOfEach())
      {
         return true;
      }
      else if(missionType == COLLECT_FLORETS2 && CultimaPanel.player.enoughFlowers())
      {
         return true;
      }
      else if(missionType == COLLECT_FLORETS3)
      {
         String favoriteColor = startStory.split(" ")[1];   //the 2nd word in the start story is the color we want
         if(CultimaPanel.player.numSpecificFlowers(favoriteColor) >= 10)
         {
            return true;
         }
      }
      else if(missionType == TEACH_3_DOORS)
      {
         return (CultimaPanel.player.getDoorsPuzzleInfo()>=NPC.doorsPuzzleInfo.length && CultimaPanel.player.stats[Player.PUZZLES_SOLVED] >= 1);
      }
      if(missionType == TEACH_3_TOWERS)
      {
         return (CultimaPanel.player.getTowersPuzzleInfo()>=NPC.towersPuzzleInfo.length && CultimaPanel.player.stats[Player.PUZZLES_SOLVED] >= 1);
      }
      else if(missionType == SEWER_LOST)
      {
         boolean withNpc = (!CultimaPanel.player.getNpcName().equals("none"));
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (withNpc && LocationBuilder.countKids(thisLoc)==0);
      }
      else if(missionType == SEWER_SIEGE)
      {
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (LocationBuilder.countSiegeMonsters(thisLoc)==0);
      }
      else if(missionType == SEWER_CHUD)
      {
         Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
         Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
         return (LocationBuilder.countMonsterType(missLoc, NPC.CHUD)==0 && LocationBuilder.countMonsterType(thisLoc, NPC.CHUD)==0);
      }
      else if(missionType == FEED_POOR && CultimaPanel.player.getRations() >= 15)
      {
         return true;
      }
      else if((missionType == RAIN_MAKER1 || missionType == RAIN_MAKER2) && this.day <= CultimaPanel.days)
      {
         return (CultimaPanel.weather > 0);
      }
      else if(missionType == HEAL_WARRIOR || missionType == HEAL_TAXMAN)
      {
         int mi = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, worldRow, worldCol, 0);
         for(NPCPlayer p: CultimaPanel.civilians.get(mi))
         {
            if(!p.getMissionInfo().equals("none") && p.getMissionInfo().toLowerCase().contains("heal") && p.getBody() >= p.maxHealth()/2)
            {
               return true;
            }
         }
         return false;
      }
      else if(missionType == CURE_POISONED || missionType == CURE_KING)
      {
         int mi = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, worldRow, worldCol, 0);
         for(NPCPlayer p: CultimaPanel.civilians.get(mi))
         {
            if(!p.getMissionInfo().equals("none") && p.getMissionInfo().toLowerCase().contains("cure") && p.hasEffect("poison"))
            {
               return false;
            }
         }
         return true;
      }
      else if(missionType == RAISE_WATER)
      {
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         return(Utilities.waterInView(thisLoc));
      }
      else if(missionType == FIX_PATH)
      {
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (LocationBuilder.countSwampTiles(thisLoc, 8)==0);
      }
      else if(missionType == SEWER_CLOG)
      {
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (LocationBuilder.countWoodTiles(thisLoc)==0);
      }
      else if(missionType == BUILD_PIER)
      {
         return (Utilities.dockSpots().size() > 0);   
      }
      else if(missionType == PUBLIC_HOUSING)
      {  //we have bought a house
         Location houseLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (LocationBuilder.findOurBed(houseLoc)!=null);
      }
      else if(missionType == REMOVE_CULT)
      {
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (LocationBuilder.countCultists(thisLoc)==0);
      }
      else if(missionType == HAUNTED_CLEAR)
      {
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (LocationBuilder.countPhantoms(thisLoc)==0);
      }
      else if(missionType == CLOSE_HELLMOUTH)
      {
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (!LocationBuilder.hasHellMouth(thisLoc));
      }
      else if(missionType == DOGTRAP)
      {  //if there is no dog at the location and you have a dog
         Location campLoc = CultimaPanel.getLocation(this.getClearLocation());
         int numDogs = LocationBuilder.countDogs(campLoc);
         return (numDogs == 0 && !CultimaPanel.player.getDogName().equals("none"));
      }
      else if(missionType == KAIJU)
      {  //done if we kill the kaiju or lead it far enough away
         Location cityLoc = CultimaPanel.getLocation(this.getClearLocation());
         Coord kaijuLocation = LocationBuilder.findKaiju(cityLoc);
         if(kaijuLocation == null)
         {
            CultimaPanel.monsterDist = -1;
            return true;
         }
         else
         {
            double distToCity = Display.findDistance(kaijuLocation.getRow(), kaijuLocation.getCol(), cityLoc.getRow(), cityLoc.getCol());
            if(distToCity >= CultimaPanel.SCREEN_SIZE*1.5)
            {
               CultimaPanel.monsterDist = -1;
               return true;
            }
         }
      }
      else if(missionType == WEREWOLFTRAP)
      {  //if there are no werewolves at the location
         Location campLoc = CultimaPanel.getLocation(this.getClearLocation());
         return (LocationBuilder.countLockedCellDoors(campLoc) == 0);
      }
      else if(missionType == MONSTERTRAP)
      {  //if there are no brigands or monsters at the location
         Location campLoc = CultimaPanel.getLocation(this.getClearLocation());
         int numBrigands = LocationBuilder.countBrigands(campLoc);
         int numMonsters = LocationBuilder.countWildMonsters(campLoc);
         int numDoors = LocationBuilder.countLockedCellDoors(campLoc);
         return (numBrigands == 0 || numMonsters == 0 || numDoors == 0);
      }
      else if(this.getType().contains("Escort"))
      {
         if(this.missionType == ESCORT_ADULT)
         {
            boolean atLocation = (CultimaPanel.player.getWorldRow() == this.targetRow && CultimaPanel.player.getWorldCol() == this.targetCol);
            boolean withNpc = (!CultimaPanel.player.getNpcName().equals("none"));
            if(atLocation && withNpc)
            {
               return true;
            }
         }
         else //if(this.missionType == ESCORT_KID)
         {
            Location battleLoc = CultimaPanel.getLocation(this.getClearLocation());
            boolean withNpc = (!CultimaPanel.player.getNpcName().equals("none"));
            if(battleLoc!= null)
            {
               Teleporter tele = battleLoc.getTeleporter();
               //mission is done if we have visited the location and there are still monsters left
               if (tele.toRow()!=-1 && tele.toCol()!=-1 && withNpc)
                  return true;
            }
         } 
      }
      else if(this.getType().contains("Ceremony"))
      {
         Item target = this.getTarget();
         String targetName = target.getName();
         boolean hasHead = (CultimaPanel.player.hasItem(targetName));
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         int numCultists = LocationBuilder.countCultists(thisLoc); 
         int numMonsters = LocationBuilder.countMonsters(thisLoc);
         int numStatues = LocationBuilder.countStatues(thisLoc);  
         if(hasHead || (numCultists==0 && numStatues > 0 && numMonsters == 0))
         {
            CultimaPanel.numChants = -1;
            return true;
         }
      }
      else if(missionType == CULT_RESCUE)
      {
         Location thisLoc = CultimaPanel.getLocation(this.getClearLocation());
         int numCultists = LocationBuilder.countCultists(thisLoc); 
         if(numCultists==0)
         {
            return true;
         }
      }
      else if(this.getType().contains("Boat"))   //bring a boat or ship to someone
      {
         for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
         {
            NPCPlayer p = CultimaPanel.worldMonsters.get(i);
            if(NPC.isShip(p) && p.hasMet() && Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), p.getRow(), p.getCol()) < CultimaPanel.SCREEN_SIZE/2)
               return true;
         }
         for(int i=CultimaPanel.boats.size()-1; i>=0; i--)
         {
            NPCPlayer p = CultimaPanel.boats.get(i);
            if(NPC.isShip(p) && Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), p.getRow(), p.getCol()) < CultimaPanel.SCREEN_SIZE/2)
               return true;
         }
      }
      else if(this.getType().contains("Give") && !CultimaPanel.player.hasItem(target.getName()) && CultimaPanel.player.hasItem("receipt"))
      {
         return true;
      }
      else if(this.getType().contains("Extinguish") && this.getClearLocation()!=null)
      {
         int endDay = this.getTargetRow();
         int endTime = this.getTargetCol();
         if(CultimaPanel.days > endDay || CultimaPanel.time > endTime)
            return true;
         Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
         if(missLoc!=null)
         {
            int numFires = LocationBuilder.countFires(missLoc);
            if(numFires==0)      
               return true;
         }
      }
      else if(this.getType().equals("Save") && clearLocation.length()>0 && targetHolder.equals("none"))
      {  //KILL_WEREWOLF mission
         int worldRow = this.getWorldRow();
         int worldCol = this.getWorldCol();
         Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations, worldRow, worldCol, 0);
         int mi = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, worldRow, worldCol, 0);
         boolean werewolf = CultimaPanel.werewolfAbout;
         if(!werewolf)
            return true;
      }
      else if(this.getType().contains("Save") && this.getClearLocation()!=null)
      {
         int numGangMembers = LocationBuilder.countGangMembers(CultimaPanel.getLocation(this.getClearLocation())); 
         if(numGangMembers == 0)
            return true;
      }
      else if(this.getType().contains("Siege") && this.getClearLocation()!=null)
      {
         int numBeasts = LocationBuilder.countSiegeMonsters(CultimaPanel.getLocation(this.getClearLocation())); 
         int numCivs = LocationBuilder.countCivilians(CultimaPanel.getLocation(this.getClearLocation())); 
         if(numBeasts == 0 && numCivs > 0)
            return true;
      }
      else if((this.getType().contains("Treasure") || this.getType().contains("Collect"))  && this.getGoldReward() > 0)
      {
         if(CultimaPanel.player.getGold() > this.getGoldReward())
            return true; 
      }
      else if(this.getType().contains("Teach")  && this.getTarget() != null)
      {
         Item target = this.getTarget();
         String targetName = target.getName(); 
         if(CultimaPanel.player.hasSpell(targetName) || CultimaPanel.player.hasItem(targetName))
            return true; 
      }
      else if(this.getType().contains("Battle") && this.getClearLocation()!=null)
      {
         Location battleLoc = CultimaPanel.getLocation(this.getClearLocation());
         if(battleLoc!= null)
         {
            Teleporter tele = battleLoc.getTeleporter();
            //mission is done if we have visited the location and there are still monsters left
            if (tele.toRow()!=-1 && tele.toCol()!=-1)
               return true;
         }
      }//end of checking battle mission finish
      else if(this.getType().contains("Purge") && this.getClearLocation()!=null)
      {
         if(this.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
            if(missLoc!=null)
            {
               int numCivs = Integer.MAX_VALUE;
               if(this.getMissionType()==ZOMBIE_PLAGUE)
               {
                  numCivs = LocationBuilder.countZombies(missLoc);
               }
               else
               {
                  numCivs = LocationBuilder.countCivilians(missLoc);
               }
               if(numCivs==0)      //we killed or fightened out all the inhabitants
                  return true;
            }
         }
      }
      else if(this.getType().contains("Arena") && this.getClearLocation()!=null)
      {
         if(this.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
            if(missLoc!=null)
            {
               int numMonsters = LocationBuilder.countMonsters(missLoc);
               if(numMonsters==0)      //we killed all the monsters in the arena
                  return true;
            }
         }
      }
      else if((this.getType().contains("Destroy") || this.getType().contains("Slay") || this.getType().contains("Assassinate") || this.getType().contains("Find") || this.getType().contains("Swine") || this.getType().contains("Roberts")) && this.getTarget()!=null)
      {   //check to see if Slay mission is finished   
         Item target = this.getTarget();
         String targetName = target.getName();
         if(this.getStartStory().toLowerCase().contains("vampyre") && this.getClearLocation().length()>0)  //make sure we also burned the coffin out of the area
         {
            boolean hasHead = (CultimaPanel.player.hasItem(targetName));
            Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
            boolean hasCoffin = TerrainBuilder.hasCoffin(missLoc);
            if(hasHead && !hasCoffin)
               return true;
         }
         else if(CultimaPanel.player.hasItem(targetName))
            return true;
      }
      else if(this.getType().contains("Break") && this.getClearLocation().length()>0 && this.getMissionType() == DESTROY_SIGNS)
      {   //check to see if break shoppe sign mission is finished   
         Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
         return !TerrainBuilder.hasSigns(missLoc);
      }
      else if(this.getType().contains("Break") && this.getClearLocation().length()>0 && this.getMissionType() == HAUNTED_HOUSE)
      {   //check to see if any gray tiles exist in the map  
         Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
         return !TerrainBuilder.hasGrayTiles(missLoc);
      }
      else if(this.getType().contains("Burn") && this.getClearLocation().length()>0)
      {   //check to see if burn coffins mission is finished   
         Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
         return !TerrainBuilder.hasCoffin(missLoc);
      }
      else if(this.getType().contains("Harvest") && this.getTarget()!=null)
      {   
         if(this.getMissionType() == FIND_SERPENT_EGGS)
         {//check to see if harvest egg mission is finished   
            Item target = this.getTarget();
            String targetName = target.getName().toLowerCase();
            if(targetName.contains("3-serpent-eggs") && CultimaPanel.player.numSerpentEggs()>=3)
               return true;
         }
         else if(this.getMissionType() == FIND_LUMBER1 || this.getMissionType() == FIND_LUMBER2 || this.getMissionType() == FIND_LUMBER3 || this.getMissionType() == FIND_LUMBER4)
         {
            Item target = this.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
               return true; 
         }
      }
      else if(this.getType().contains("Train") && this.getTarget()!=null)   //bring a trained horse or dog to someone
      {
         Item target = this.getTarget();
         String targetName = target.getName().toLowerCase();
         if(targetName.contains("horse"))
         {
            for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
            {
               NPCPlayer p = CultimaPanel.worldMonsters.get(i);
               if(p.getCharIndex() == NPC.HORSE && p.hasMet())
                  return true;
            }
         }
         else if(targetName.contains("dog"))
         {
            if(!CultimaPanel.player.getDogName().equals("none"))
               return true;
         }  
      }
      else if(this.getType().contains("Clear") && this.getClearLocation()!=null && this.getClearLocation().length()>0)
      {                    //this is a mission to clear a location of monsters
         Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
         if(missLoc!=null)
         {
            ArrayList<Coord> monsterFreq = missLoc.getMonsterFreq();
            if(monsterFreq==null || monsterFreq.size()==0)  //we cleared out the monsters here
               return true;
         }
      }
      else if(this.getType().contains("Rescue") && this.getClearLocation().length()>0)
      {
         Location missLoc = CultimaPanel.getLocation(this.getClearLocation());
         if(missLoc!=null)
         {
            if(LocationBuilder.countPrisoners(missLoc)==0)      //we freed all the prisoners
               return true;
         }
      }
      else if(this.getType().contains("Remove") && this.getTargetHolder()!=null && this.getTargetHolder().length()>0)        //remove beggers from a town, or help them to be upright citizens
      {
         int worldRow = this.getWorldRow();
         int worldCol = this.getWorldCol();
         Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations, worldRow, worldCol, 0);
         int mi = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, worldRow, worldCol, 0);
         if(this.getTargetHolder().contains("all beggers"))
         {
            int numBeggers = LocationBuilder.countPrisoners(thisLoc);   
            if(numBeggers==0)
               return true;
         }
         else
         {
            boolean removed = true;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
            {
               String pName = Utilities.shortName(p.getName());
               String targName = Utilities.shortName(this.getTargetHolder());
               if(pName.equalsIgnoreCase(targName) && p.getCharIndex()==NPC.BEGGER)
               {
                  removed = false;
                  break;
               }
            }
            if(removed)
               return true;
         }
      } 
      else if((this.getType().contains("Frighten") || this.getType().contains("Chase")) && this.getTargetHolder()!=null && this.getTargetHolder().length()>0)        //remove beggers from a town, or help them to be upright citizens
      {
         int worldRow = this.getWorldRow();
         int worldCol = this.getWorldCol();
         Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations, worldRow, worldCol, 0);
         int mi = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, worldRow, worldCol, 0);
         boolean removed = true;
         for(NPCPlayer p:CultimaPanel.civilians.get(mi))
         {
            String pName = Utilities.shortName(p.getName());
            String targName = Utilities.shortName(this.getTargetHolder());
            if(pName.equalsIgnoreCase(targName))
            {
               removed = false;
               break;
            }
         }
         if(removed)
            return true;
      }
      else if((this.getType().contains("Marry")) && this.getTargetHolder()!=null && this.getTargetHolder().length()>0)        //remove beggers from a town, or help them to be upright citizens
      {
         int worldRow = this.getWorldRow();
         int worldCol = this.getWorldCol();
         Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations, worldRow, worldCol, 0);
         int mi = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, worldRow, worldCol, 0);
         boolean married = false;
         for(NPCPlayer p:CultimaPanel.civilians.get(mi))
         {
            String pName = Utilities.shortName(p.getName());   //starting the name with + means we are married to them
            String targName = Utilities.shortName(this.getTargetHolder());
            if(pName.equalsIgnoreCase("+"+targName))
            {
               married = true;
               break;
            }
         }
         return married;
      }
      else if(this.getType().contains("Steal") && this.getTarget() != null)
      {
         Item target = this.getTarget();
         String targetName = target.getName();                 //steal magic pick or riddle book
         if (target.getName().contains("pick") || target.getName().contains("book") || target.getName().contains("cube"))
         {
            if(CultimaPanel.player.hasItem(targetName))
               return true;
         }
         else if(targetName.toLowerCase().contains("dagger"))  //steal the 5 magic daggers mission
         {
            String [] magicDaggers = {"Poison-dagger","Souldagger","Magmadagger","Frostdagger","Banedagger"};
            boolean hasDaggers = true;
            for(String dag: magicDaggers)
               if(!CultimaPanel.player.hasItem(dag))
                  hasDaggers = false;
            if(hasDaggers)   
               return true;
         }
      }   
      return false;
   }

   public static void spawnMonsters(Location loc, int arenaLevel)
   {
      //see what side of the arena we are likely on so we know which side to spawn the monsters on
      int mapIndex = loc.getMapIndex();
      byte[][]currMap = (CultimaPanel.map.get(mapIndex));
      Teleporter tele = loc.getTeleporter();
      int toRow = tele.toRow();
      int toCol = tele.toCol();
      Terrain monsterSpawnTile =  TerrainBuilder.getTerrainWithName("INR_$Red_floor");
      ArrayList<Coord>monsterSpawns = new ArrayList<Coord>();
      for(int r=0; r<currMap.length; r++)
         for(int c=0; c<currMap[0].length; c++)
         {
            if(Math.abs(currMap[r][c]) == monsterSpawnTile.getValue())
               monsterSpawns.add(new Coord(r,c));
            String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
            if(curr.contains("night_door"))
               currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_I_B_$Night_door_closed").getValue();
         } 
       //arenaLevels for each index (which level it is) contains (monster charIndex, # of monsters)  
      ArrayList<Coord> arenaLevels = new ArrayList<Coord>();    
      arenaLevels.add(new Coord(NPC.SKELETON, 1));
      arenaLevels.add(new Coord(NPC.SKELETON, 1));
      arenaLevels.add(new Coord(NPC.WOLF, 1));
      arenaLevels.add(new Coord(NPC.SKELETON, 3));
      arenaLevels.add(new Coord(NPC.WOLF, 4));
      arenaLevels.add(new Coord(NPC.ORC, 1));
      arenaLevels.add(new Coord(NPC.BUGBEAR, 1));
      arenaLevels.add(new Coord(NPC.SKELETON, 6));
      arenaLevels.add(new Coord(NPC.BEAR, 1));
      arenaLevels.add(new Coord(NPC.BUGBEAR, 3));
      arenaLevels.add(new Coord(NPC.SKELETON, 10));
      arenaLevels.add(new Coord(NPC.ORC, 6));
      arenaLevels.add(new Coord(NPC.BRIGAND_SWORD, 1));
      arenaLevels.add(new Coord(NPC.BEAR, 2));
      arenaLevels.add(new Coord(NPC.BUGBEAR, 6));
      arenaLevels.add(new Coord(NPC.TROLL, 1));
      arenaLevels.add(new Coord(NPC.BRIGAND_SWORD, 3));
      arenaLevels.add(new Coord(NPC.TROLL, 3));
      arenaLevels.add(new Coord(NPC.CYCLOPS, 1));
      arenaLevels.add(new Coord(NPC.SKELETON, 20));
      arenaLevels.add(new Coord(NPC.BRIGAND_SWORD, 5));
      arenaLevels.add(new Coord(NPC.CYCLOPS, 3));
      arenaLevels.add(new Coord(NPC.TROLL, 6));
      arenaLevels.add(new Coord(NPC.ENFORCER, 1));
      arenaLevels.add(new Coord(NPC.WOLF, 8));
      arenaLevels.add(new Coord(NPC.BOWASSASSIN, 1));
      arenaLevels.add(new Coord(NPC.ORC, 8));
      arenaLevels.add(new Coord(NPC.BEAR, 4));
      arenaLevels.add(new Coord(NPC.BRIGANDKING, 1));
      arenaLevels.add(new Coord(NPC.CYCLOPS, 5));
      arenaLevels.add(new Coord(NPC.BRIGAND_SWORD, 8));
      arenaLevels.add(new Coord(NPC.ORC, 10));
      arenaLevels.add(new Coord(NPC.RATKING, 1));
      arenaLevels.add(new Coord(NPC.BUGBEAR, 10));
      arenaLevels.add(new Coord(NPC.SNAKEKING, 1));
      arenaLevels.add(new Coord(NPC.BOWASSASSIN, 2));
      arenaLevels.add(new Coord(NPC.BRIGAND_SWORD, 10));
      arenaLevels.add(new Coord(NPC.SPIDERKING, 1));
      arenaLevels.add(new Coord(NPC.SKELETON, 30));
      arenaLevels.add(new Coord(NPC.WOLFKING, 1));
      arenaLevels.add(new Coord(NPC.CYCLOPS, 8));
      arenaLevels.add(new Coord(NPC.BUGBEARKING, 1));
      arenaLevels.add(new Coord(NPC.ENFORCER, 5));
      arenaLevels.add(new Coord(NPC.TROLLKING, 1));
      arenaLevels.add(new Coord(NPC.SORCERER, 10));
      arenaLevels.add(new Coord(NPC.BOWASSASSIN, 3));
      arenaLevels.add(new Coord(NPC.BEARKING, 2));
      arenaLevels.add(new Coord(NPC.CYCLOPS, 10));
      arenaLevels.add(new Coord(NPC.ENFORCER, 10));
      arenaLevels.add(new Coord(NPC.SKELETON, 40));
      arenaLevels.add(new Coord(NPC.BOWASSASSIN, 6));
      
      Coord level = arenaLevels.get(arenaLevel);
      byte monsterIndex = (byte)(level.getRow());
      byte frequency = (byte)(level.getCol());
      for(int i=0; i<frequency; i++)
      {
         if(monsterSpawns.size()==0)
            break;
         Coord spawn = monsterSpawns.remove((int)(Math.random()*monsterSpawns.size()));
         NPCPlayer monster = null;
         if(monsterIndex==NPC.BRIGAND_SWORD)
            monster = new NPCPlayer(NPC.randomArenaBrigand(), spawn.getRow(), spawn.getCol(), mapIndex, "arena");
         else   
            monster = new NPCPlayer(monsterIndex, spawn.getRow(), spawn.getCol(), mapIndex, "arena");
         monster.setMoveType(NPC.CHASE);
         monster.setGold(0);
         CultimaPanel.worldMonsters.add(monster);
      }
   }

   public static String arenaMission(NPCPlayer selected)
   {
      String [] noMission = {"Excuse me. I have no mission. I just wanted to greet you personally", "You seem the type that is good for a mission. Sadly, I don't know of any", "You have a mission: look for missions from others", "Did thou say mission? There must be a vex in thy matrix"};
      String [] noBattle = {"Come back on day WHICH_DAY for more glory", "Thee has seen enough death for today. Return on day WHICH_DAY for more glory", "On day WHICH_DAY, we will have the arena ready for another victory for thee", "'Tis time for thee to rest. On day WHICH_DAY, return to thrill us with another victory"};
      String [] arenaChampion = {"You are the Champion of the Arena. Your legend requires no more glory", "You have retired as the legendary Arena Champion", "You may rest as the Champion of the Arena", "As Arena Champion, you are too great for any challenges here"};
      String [] tooLate = {"The curtain of night has fallen. Thy arena will wait 'til morn", "You may find glory in the arena at sunrise", "Come thee back when the light of day shines down on this glorious arena", "'Tis the fall of night. Rest thee until morn for more battle"};
      String response = "";
      if(selected.getNumInfo() == 0)
         return Utilities.getRandomFrom(arenaChampion);
      if(selected.getNumInfo() >= CultimaPanel.days)
         return Utilities.getRandomFrom(noBattle).replace("WHICH_DAY", (""+(CultimaPanel.days + 1)));
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if((CultimaPanel.time >=20 || CultimaPanel.time <= 6) && currMission==null)
      {
          
         return Utilities.getRandomFrom(tooLate);
      }
   
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getType().contains("Arena"))
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!= null)
            {
               int numMonsters = LocationBuilder.countMonsters(missLoc);
               if(numMonsters==0)      //we killed all the monsters in the arena
               {
                  int reputationGained = 5;
                  finishMission(currMission, selected, reputationGained);
               }
            }
         }
         else if(currMission.getType().contains("Assassinate"))
         {   //check to see if Slay mission is finished   
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.missionTargetNames.remove(targetName);
               CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
               CultimaPanel.player.removeItem(target.getName());
               int reputationGained = -10;
               finishMission(currMission, selected, reputationGained);               
            }
         }
      }//end seeing if we finished a mission
      if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else 
         if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
         {
            Sound.applauseWin(Utilities.rollDie(100,200));
            response = currMission.getEndStory();
            CultimaPanel.player.addExperience(100);
            int goldReward = currMission.getGoldReward();
            Item itemReward = currMission.getItemReward();
            CultimaPanel.player.addGold(goldReward);
            if(itemReward != null)
               CultimaPanel.player.addItem(itemReward.getName());
            Player.stats[Player.ARENA_LEVEL] = Player.stats[Player.ARENA_LEVEL] + 1;   //Math.min(50, selected.getNumInfo()));
            selected.setNumInfo(selected.getNumInfo()+1);
            if(Player.stats[Player.ARENA_LEVEL] > 50)
            {
               selected.setNumInfo(0);
               CultimaPanel.player.addItem("arenacape");
               Achievement.earnAchievement(Achievement.MAXIMUS_OVERDRIVE);
               int diff = CultimaPanel.player.expNeededForNextLevel() - CultimaPanel.player.getExperience();
               if(diff > 0)
                  CultimaPanel.player.addExperience(diff);
               Player.stats[Player.ARENA_LEVEL] = 1;   
            }
            selected.setMissionInfo("none");
            //open gates to arena to let you out
            int mapIndex = CultimaPanel.player.getMapIndex();
            byte[][]currMap = (CultimaPanel.map.get(mapIndex));
            for(int r=0; r<currMap.length; r++)
               for(int c=0; c<currMap[0].length; c++)
               {
                  String curr = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(curr.contains("night_door"))
                     currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_$Night_door_floor").getValue();
               } 
            if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
               CultimaPanel.missionStack.remove(currMissionIndex);
            FileManager.saveProgress();
         } 
         else
         { //create a new mission
            String missionType = "Arena";
            //possibly make a mission to assasinate an escaped prisoner in the next closest town
            String []locTypes2 = {"city","fortress","port","village"};
            Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
            ArrayList<NPCPlayer> inLocNPCs = Utilities.getNPCsInLoc(closeCity.getMapIndex(), NPC.CHILD, selected.getName());
            if(inLocNPCs.size() >= 1 && Math.random() < 0.1)
            {
               missionType = "Assassinate";
            }
            if(missionType.equals("Arena"))
            {
               int mapIndex = CultimaPanel.player.getMapIndex();
               Location loc = CultimaPanel.allLocations.get(mapIndex);       
               if(loc!=null)
               {
                  String [] temp = missions[ARENA];
                  String [] story = new String[3];
                  story[0] = new String(temp[0]);
                  story[1] = new String(temp[1]);
                  story[2] = new String(temp[2]);     
                  String locName = Display.provinceName(loc.getName());
               
                  int arenaLevel = Math.min(50, CultimaPanel.player.stats[Player.ARENA_LEVEL]);  //selected.getNumInfo();
                  int goldReward = Math.max(5, Math.min(250, arenaLevel*5));
               
                  spawnMonsters(loc, arenaLevel);
               
                  if(CultimaPanel.shoppeDiscount)   //guard is charmed
                     goldReward = (int)(goldReward * 1.20);
                     
                  for(int i=0; i<story.length; i++)
                  {
                     story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  }
                  int startRow = CultimaPanel.player.getWorldRow();
                  int startCol = CultimaPanel.player.getWorldCol();
                  int mRow = loc.getRow();
                  int mCol = loc.getCol();
               //public           Mission(String t, String[]story, int wRow, int wCol, int gold, Item iw, String loc, Item targ, String targHolder, int targR, int targC)
                  Mission getMission = new Mission(missionType, story, startRow, startCol, goldReward, null, locName, null, "none", mRow, mCol, ARENA, -1);
                  selected.setMissionInfo(getMission.getInfo());
                  CultimaPanel.missionStack.add(getMission);
                  if(CultimaPanel.missionsGiven[ARENA]==0)
                     CultimaPanel.missionsGiven[ARENA]++;
                  FileManager.saveProgress();
                  response = getMission.getStartStory();            
               }
            }
            else  //Assassinate escaped prisoner
            {
               int randMission = Mission.ARENA_BOUNTY;
               missionType = "Assassinate";
               NPCPlayer targNPC = null;
               Location loc = null;
               if(inLocNPCs.size() >= 1 && closeCity!=null)
               {
                  ArrayList<NPCPlayer> targets = new ArrayList<NPCPlayer>();
                  for(NPCPlayer pl: inLocNPCs)
                     if(!pl.isShopkeep() && (pl.getCharIndex()==NPC.MAN || pl.getCharIndex()==NPC.WOMAN || pl.getCharIndex()==NPC.BEGGER || pl.getCharIndex()==NPC.WISE || pl.getCharIndex()==NPC.SWORD))
                        targets.add(pl);
                  if(targets.size()==0)
                     targNPC = null;
                  else
                     targNPC = Utilities.getRandomFrom(targets);
               
                  loc = closeCity;
                  if(targNPC!=null && loc!=null)
                  {
                     String [] temp = missions[randMission];
                     String [] story = new String[3];
                     story[0] = new String(temp[0]);
                     story[1] = new String(temp[1]);
                     story[2] = new String(temp[2]);
                  //higher reward for targets further away
                     int goldReward = Math.max(60, targNPC.getLevel()+(int)(Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), loc.getRow(), loc.getCol())));
                     String targName = Utilities.shortName(targNPC.getName());  
                     CultimaPanel.missionTargetNames.add(targName);   
                     String target = targName+"-bounty";
                     int targItemPrice = (int)(goldReward);
                     targNPC.addItem(new Item(target, targItemPrice));
                     String locName = Display.provinceName(loc.getName());
                     for(int i=0; i<story.length; i++)
                     {
                        story[i]=story[i].replace("NPC_NAME", targName);
                        story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                        story[i]=story[i].replace("LOCATION_NAME", locName);
                        story[i]=story[i].replace("ITEM_NAME", target);
                        story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
                     }
                     int startRow = CultimaPanel.player.getWorldRow();
                     int startCol = CultimaPanel.player.getWorldCol();
                     int mRow = -1, mCol = -1;
                     if(targNPC.getMapIndex() != selected.getMapIndex())
                     {
                        mRow = loc.getRow();
                        mCol = loc.getCol();
                     }
                     TerrainBuilder.markMapPath(loc.getName());
                     Mission getMission = new Mission(missionType, story, startRow, startCol, goldReward, null, locName, new Item(target, goldReward), targName, mRow, mCol, (byte)randMission, -1);
                     selected.setMissionInfo(getMission.getInfo());
                     CultimaPanel.missionStack.add(getMission);
                     if(CultimaPanel.missionsGiven[randMission]==0)
                        CultimaPanel.missionsGiven[randMission]++;
                     FileManager.saveProgress();
                     response = getMission.getStartStory();
                  }
                  else
                  {
                  //no mission
                     response = Utilities.getRandomFrom(NPC.mainMission)+".";
                     selected.setNumInfo(0);
                  }   
               }
            }
         }
      if(response.length()==0)
         response = Utilities.getRandomFrom(noMission);   
      return response;
   }

   public static String guardMission(NPCPlayer selected)
   {
      String [] noBattle = {"Excuse me. I have no mission. I just wanted to greet you personally", "<...seek the sacred number...>", "You have a mission: look for missions from others", "Did thou say mission? There must be a vex in thy matrix"};
      String [] lostBattle = {"Thy reinforcements have driven them back, but thy losses were too heavy for pay", "You left the battle before reinforcements cleared the area.  You get no pay"};
   
      byte[][] worldMap = CultimaPanel.map.get(0);
      String response = "";
      int mi = CultimaPanel.player.getMapIndex();
      boolean hasSewer = LocationBuilder.doesCityHaveSewer(mi);
      byte [][] currMap = CultimaPanel.map.get(mi);      
      String currLoc = CultimaPanel.player.getLocationType().toLowerCase();
      String currentSpot = CultimaPanel.allTerrain.get(Math.abs(currMap[selected.getRow()][selected.getCol()])).getName().toLowerCase();
      //make sure we don't launch a siege mission with a guard that is on the castle gate - the enemies will spawn right on top of us
      boolean castleGateGuard = ((currLoc.contains("castle") || currLoc.contains("tower")) && currentSpot.contains("wood"));
      ArrayList<Coord>spawnCoords = new ArrayList<Coord>();
      for(int cr = 5; cr<currMap.length-5; cr++)
         for(int cc = 5; cc<currMap[0].length-5; cc++)
         {
            if(currLoc.contains("city") || currLoc.contains("fort"))
            {
               if(TerrainBuilder.isGoodCityMonsterSpawn(cr, cc, mi))
                  spawnCoords.add(new Coord(cr, cc));
            }
            else
            {
               if(TerrainBuilder.isInsideFloor(cr, cc, mi))
                  spawnCoords.add(new Coord(cr, cc));
            }
         }
      
      ArrayList<Coord>monsterSpawnCoords = new ArrayList<Coord>();
      for(int cr = 0; cr<currMap.length; cr++)
         for(int cc = 0; cc<currMap[0].length; cc++)
            if(TerrainBuilder.isTraversable(cr, cc, mi))
               monsterSpawnCoords.add(new Coord(cr, cc));   
         
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getMissionType() == SEWER_CLOG)
         {
            Location thisLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if (LocationBuilder.countWoodTiles(thisLoc)==0)
            {
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getMissionType() == SEWER_SIEGE)
         {
            Location thisLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if (LocationBuilder.countSiegeMonsters(thisLoc)==0)
            {
               finishMission(currMission, selected, 10);
            }
         }
         else if(currMission.getMissionType() == SEWER_CHUD)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            if (LocationBuilder.countMonsterType(missLoc, NPC.CHUD)==0 && LocationBuilder.countMonsterType(thisLoc, NPC.CHUD)==0)
            {
               finishMission(currMission, selected, 10);
            }
         }
         else if(currMission.getType().toLowerCase().contains("clear") && currMission.getMissionType() == MONSTERTRAP)
         {     //if there are no brigands or monsters at the location
            Location campLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            int numBrigands = LocationBuilder.countBrigands(campLoc);
            int numMonsters = LocationBuilder.countWildMonsters(campLoc);
            int numDoors = LocationBuilder.countLockedCellDoors(campLoc);
            if (numBrigands == 0 || numMonsters == 0 || numDoors == 0)
            {
               CultimaPanel.civilians.get(campLoc.getMapIndex()).clear();
               CultimaPanel.furniture.get(campLoc.getMapIndex()).clear();
               Utilities.standDownGuards();
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Battle"))
         {
            Location battleLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(battleLoc!= null)
            {
               Teleporter tele = battleLoc.getTeleporter();
               //mission is done if we have visited the location and there are still monsters left
               if (tele.toRow()!=-1 && tele.toCol()!=-1)
               {
                  currMission.setState(Mission.FINISHED);
                  if(currMission.getFailed() == false)
                  {
                     Utilities.standDownGuards();
                     Player.stats[Player.MISSIONS_COMPLETED]++;
                     if(Player.stats[Player.MISSIONS_COMPLETED] >= 50)
                        Achievement.earnAchievement(Achievement.TASK_MASTER);
                     selected.setReputation(CultimaPanel.player.getReputationRaw());   //you completed a mission - make them allignment compatible with you
                     CultimaPanel.player.addReputation(5);
                  }
                  else
                  {
                     selected.setNumInfo(0);  
                     selected.setMissionInfo("none");
                     if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
                        CultimaPanel.missionStack.remove(currMissionIndex);
                     FileManager.saveProgress();
                     return Utilities.getRandomFrom(lostBattle);
                  }
               }
            }
         }//end of checking battle mission finish
         else if(currMission.getType().contains("Purge"))
         {
            if(currMission.getClearLocation().length()>0)
            {
               Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
               if(missLoc!=null)
               {
                  int numCivs = Integer.MAX_VALUE;
                  if(currMission.getMissionType()==ZOMBIE_PLAGUE)
                  {
                     numCivs = LocationBuilder.countZombies(missLoc);
                  }
                  else
                  {
                     numCivs = LocationBuilder.countCivilians(missLoc);
                  }
                  if(numCivs==0)      //we killed all the inhabitants
                  {
                     Utilities.standDownGuards();
                     int reputationPoints = 15;
                     finishMission(currMission, selected, reputationPoints);
                  }
               }
            }
         }//end of checking purge mission finish
         else if(currMission.getType().contains("Slay") || currMission.getType().contains("Roberts"))
         {   //check to see if Slay mission is finished   
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.player.removeItem(target.getName());
               if(currMission.getType().contains("Roberts"))
                  selected.addItem(target);
               Utilities.standDownGuards();
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }//end of checking slay/roberts mission finish
         else if(currMission.getType().contains("Harvest"))
         {   //check to see if FIND_LUMBER4 mission is finished   
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.player.removeItem(target.getName());
               selected.addItem(target);
               Utilities.standDownGuards();
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }//end of checking harvest mission finish
         else if(currMission.getType().contains("Frighten") || currMission.getType().contains("Chase"))
         {
            boolean removed = true;
            for(NPCPlayer p:CultimaPanel.civilians.get(selected.getMapIndex()))
            {
               String pName = Utilities.shortName(p.getName());
               String targName = Utilities.shortName(currMission.getTargetHolder());
               if(pName.equalsIgnoreCase(targName))
               {
                  removed = false;
                  break;
               }
            }
            if(removed)
            {
               int reputationPoints = -5;
               if(currMission.getType().contains("Chase"))
               {
                  reputationPoints = 5;
               }
               finishMission(currMission, selected, reputationPoints);
            }
         }  //end of checking frighten/chase mission finish
         else if(currMission.getType().contains("Siege") && currMission.getClearLocation()!=null)
         {
            int numBeasts = LocationBuilder.countSiegeMonsters(CultimaPanel.getLocation(currMission.getClearLocation())); 
            int numCivs = LocationBuilder.countCivilians(CultimaPanel.getLocation(currMission.getClearLocation())); 
            if(numBeasts==0 && numCivs > 0)
            {
               selected.setMoveType(NPC.ROAM);
               selected.setHasBeenAttacked(false);
               Utilities.standDownGuards();
               Utilities.standDownTaxmen();
               int reputationPoints = 15;
               finishMission(currMission, selected, reputationPoints);
            }
         } //end of checking siege mission finish
      }  //end seeing if we finished a mission
      
      if(selected.getNumInfo() <= 0)
         response = Utilities.getRandomFrom(NPC.noMission)+".";
      else   
         if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
            response = currMission.getMiddleStory();
         else 
            if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
            {
               response = currMission.getEndStory();
               CultimaPanel.player.addExperience(100);
               int goldReward = currMission.getGoldReward();
               Item itemReward = currMission.getItemReward();
               CultimaPanel.player.addGold(goldReward);
               if(itemReward != null)
                  CultimaPanel.player.addItem(itemReward.getName());
               selected.setNumInfo(0);  
               selected.setMissionInfo("none");
               if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
                  CultimaPanel.missionStack.remove(currMissionIndex);
               FileManager.saveProgress();
            } 
            else
            { //create a new mission
               String missionType = "";
               //see which missions we have yet to be given
               ArrayList<String>missTemp = new ArrayList<String>();
               
               ArrayList<Coord>waterLocs =  Utilities.waterInView();
               
               if(CultimaPanel.missionsGiven[SEWER_CLOG]==0 && hasSewer)
                  missTemp.add("Fix");
               if(CultimaPanel.missionsGiven[SEWER_SIEGE]==0 && hasSewer)
                  missTemp.add("Sewer siege");
               if(CultimaPanel.missionsGiven[SEWER_CHUD]==0 && hasSewer)
                  missTemp.add("Clear Chud");
               if(CultimaPanel.missionsGiven[CITY_SIEGE]==0 && monsterSpawnCoords.size() > 0 && !castleGateGuard)
                  missTemp.add("Siege");
               if(CultimaPanel.missionsGiven[BATTLE]==0)
                  missTemp.add("Battle");
               if(CultimaPanel.missionsGiven[FIND_LUMBER4]==0)
                  missTemp.add("Harvest");
               if(CultimaPanel.missionsGiven[MONSTERTRAP]==0)
                  missTemp.add("Clear");
               if(CultimaPanel.missionsGiven[SLAY_MONSTER]==0)
                  missTemp.add("Slay");
               if(CultimaPanel.missionsGiven[ZOMBIE_PLAGUE]==0)
                  missTemp.add("Purge");
               if(waterLocs.size() >= 5 && CultimaPanel.missionsGiven[SLAY_ROBERTS]==0 && !Display.frozenWater())
                  missTemp.add("Roberts"); 
               if(CultimaPanel.missionsGiven[CHASE_THIEF]==0 && spawnCoords.size()>0)
                  missTemp.add("Chase");   
               if(selected.isWerewolf() && CultimaPanel.missionsGiven[MERCY_KILL]==0)   //werewolf tag
                  missTemp.add("Mercy");  
               //if we have seen each mission type before, pick a random one   
               if(missTemp.size()==0)  
               {   
                  if(hasSewer)
                  {
                     missTemp.add("Fix");
                     missTemp.add("Sewer siege");
                  }
                  if(selected.isWerewolf())
                  {
                     missTemp.add("Mercy");
                  }
                  if(spawnCoords.size() > 0)
                  {
                     missTemp.add("Chase");   
                  }
                  if(waterLocs.size() >= 5 && !Display.frozenWater())
                  {
                     missTemp.add("Roberts");
                  }
                  if(!castleGateGuard && monsterSpawnCoords.size() > 0)
                  {
                     missTemp.add("Siege");
                  }
                  missTemp.add("Harvest");
                  missTemp.add("Battle");
                  missTemp.add("Slay");
                  missTemp.add("Purge");
                  missTemp.add("Clear");
               }
               //pick a random among them
               missionType = Utilities.getRandomFrom(missTemp);
            
               //***TESTING**** missionType = "Clear Chud"; 
                   
               if(missionType.equals("Fix"))
               {
                  Location sewerLoc = LocationBuilder.findSewerForCity(mi);
                  if(sewerLoc == null)
                  {
                     response = Utilities.getRandomFrom(noBattle);
                  }
                  else
                  {
                     ArrayList<Coord> woodSpawns = new ArrayList<Coord>();
                     byte[][] sewerMap = CultimaPanel.map.get(sewerLoc.getMapIndex());
                     for(int r=1; r<sewerMap.length-1; r++)
                     {
                        for(int c=1; c<sewerMap[0].length-1; c++)
                        {
                           String sewerTile = CultimaPanel.allTerrain.get(Math.abs(sewerMap[r][c])).getName().toLowerCase();
                           if(LocationBuilder.nextToAFloor(sewerMap, r, c) && (sewerTile.contains("bog") || sewerTile.contains("water")))
                           {
                              woodSpawns.add(new Coord(r, c));
                           }
                        }
                     }
                     if(woodSpawns.size() == 0)
                     {
                        response = Utilities.getRandomFrom(noBattle);
                     }
                     else
                     {
                        byte lumber = TerrainBuilder.getTerrainWithName("INR_$Wood_Plank_floor").getValue();
                        Coord clogSpot = Utilities.getRandomFrom(woodSpawns);
                        int clogR = clogSpot.getRow();
                        int clogC = clogSpot.getCol();
                        sewerMap[clogR][clogC] = lumber;
                        if(Math.random() < 0.5 && clogR+1 < sewerMap.length && (CultimaPanel.allTerrain.get(Math.abs(sewerMap[clogR+1][clogC])).getName().toLowerCase().contains("bog") || CultimaPanel.allTerrain.get(Math.abs(sewerMap[clogR+1][clogC])).getName().toLowerCase().contains("water")))
                        {
                           sewerMap[clogR+1][clogC] = lumber;
                        }
                        if(Math.random() < 0.5 && clogR-1 >= 0 && (CultimaPanel.allTerrain.get(Math.abs(sewerMap[clogR-1][clogC])).getName().toLowerCase().contains("bog") || CultimaPanel.allTerrain.get(Math.abs(sewerMap[clogR-1][clogC])).getName().toLowerCase().contains("water")))
                        {
                           sewerMap[clogR-1][clogC] = lumber;
                        }
                        if(Math.random() < 0.5 && clogC+1 < sewerMap[0].length && (CultimaPanel.allTerrain.get(Math.abs(sewerMap[clogR][clogC+1])).getName().toLowerCase().contains("bog") || CultimaPanel.allTerrain.get(Math.abs(sewerMap[clogR][clogC+1])).getName().toLowerCase().contains("water")))
                        {
                           sewerMap[clogR][clogC+1] = lumber;
                        }
                        if(Math.random() < 0.5 && clogC-1 >= 0 && (CultimaPanel.allTerrain.get(Math.abs(sewerMap[clogR][clogC-1])).getName().toLowerCase().contains("bog") || CultimaPanel.allTerrain.get(Math.abs(sewerMap[clogR][clogC-1])).getName().toLowerCase().contains("water")))
                        {
                           sewerMap[clogR][clogC-1] = lumber;
                        }
                        String [] temp = missions[SEWER_CLOG];
                        String [] story = new String[3];
                        story[0] = new String(temp[0]);
                        story[1] = new String(temp[1]);
                        story[2] = new String(temp[2]);
                        int goldReward = Utilities.rollDie(5,15);
                        for(int i=0; i<story.length; i++)
                        {
                           story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
                           story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                        }
                        int startRow = CultimaPanel.player.getWorldRow();
                        int startCol = CultimaPanel.player.getWorldCol();
                        int mRow = -1, mCol = -1;
                        Mission rescueMission = new Mission(missionType, story, startRow, startCol, goldReward, null, sewerLoc.getName(), null, "none", mRow, mCol, SEWER_CLOG, -1);
                        selected.setMissionInfo(rescueMission.getInfo());
                        CultimaPanel.missionStack.add(rescueMission);
                        if(CultimaPanel.missionsGiven[SEWER_CLOG]==0)
                           CultimaPanel.missionsGiven[SEWER_CLOG]++;
                        FileManager.saveProgress();
                        response = rescueMission.getStartStory();
                     }
                  }
               }
               else if(missionType.equals("Sewer siege"))
               {
                  Location sewerLoc = LocationBuilder.findSewerForCity(mi);
                  if(sewerLoc == null)
                  {
                     response = Utilities.getRandomFrom(noBattle);
                  }
                  else
                  {
                     ArrayList<Coord> spawns = new ArrayList<Coord>();
                     byte[][] sewerMap = CultimaPanel.map.get(sewerLoc.getMapIndex());
                     for(int r=1; r<sewerMap.length-1; r++)
                     {
                        for(int c=1; c<sewerMap[0].length-1; c++)
                        {
                           String sewerTile = CultimaPanel.allTerrain.get(Math.abs(sewerMap[r][c])).getName().toLowerCase();
                           if(sewerTile.contains("floor"))
                           {
                              spawns.add(new Coord(r, c));
                           }
                        }
                     }
                     if(spawns.size() == 0)
                     {
                        response = Utilities.getRandomFrom(noBattle);
                     }
                     else
                     {
                        boolean mixedMonsters = false;
                        if(Math.random() < 0.5)
                           mixedMonsters = true;
                        byte charType = NPC.randomSiegeMonster();
                        int armySize = Math.min(spawns.size(), Utilities.rollDie(8,15));
                        for(int i=0; i<armySize && spawns.size() > 0; i++)
                        {
                           Coord spot = spawns.remove((int)(Math.random()*spawns.size()));
                           int row = spot.getRow();
                           int col = spot.getCol();
                           if(mixedMonsters)
                           {
                              charType = NPC.randomSiegeMonster();
                           }
                           else if(NPC.isBrigand(charType))
                           {
                              charType = NPC.randomBrigand();
                           }
                           NPCPlayer monster = new NPCPlayer(charType, row, col, sewerLoc.getMapIndex(), row, col, "sewer");
                           if(NPC.isBrigand(charType))
                              monster.modifyStats(1.2);
                           monster.setMoveType(NPC.CHASE);
                           monster.setCanWalkAndSwim(true);
                           CultimaPanel.civilians.get(sewerLoc.getMapIndex()).add(monster);
                           if(!mixedMonsters && (NPC.getSize(charType) >= 1.5 || NPC.isMonsterKing(charType)))
                           {  //we are not mixing monster types and we picked a general - change the charType to a minion type
                              charType = NPC.randomSiegeMinion();
                           }
                        }
                        String [] temp = missions[SEWER_SIEGE];
                        String [] story = new String[3];
                        story[0] = new String(temp[0]);
                        story[1] = new String(temp[1]);
                        story[2] = new String(temp[2]);
                        int goldReward = Utilities.rollDie(25,50);
                        for(int i=0; i<story.length; i++)
                        {
                           story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
                           story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                        }
                        int startRow = CultimaPanel.player.getWorldRow();
                        int startCol = CultimaPanel.player.getWorldCol();
                        int mRow = -1, mCol = -1;
                        Mission rescueMission = new Mission(missionType, story, startRow, startCol, goldReward, null, sewerLoc.getName(), null, "none", mRow, mCol, SEWER_SIEGE, -1);
                        selected.setMissionInfo(rescueMission.getInfo());
                        CultimaPanel.missionStack.add(rescueMission);
                        if(CultimaPanel.missionsGiven[SEWER_SIEGE]==0)
                           CultimaPanel.missionsGiven[SEWER_SIEGE]++;
                        FileManager.saveProgress();
                        response = rescueMission.getStartStory();
                     }
                  }
               }
               else if(missionType.equals("Clear Chud"))
               {
                  Location sewerLoc = LocationBuilder.findSewerForCity(mi);
                  Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
                  if(sewerLoc == null)
                  {
                     response = Utilities.getRandomFrom(noBattle);
                  }
                  else
                  {
                     ArrayList<Coord> spawns = new ArrayList<Coord>();
                     byte[][] sewerMap = CultimaPanel.map.get(sewerLoc.getMapIndex());
                     for(int r=1; r<sewerMap.length-1; r++)
                     {
                        for(int c=1; c<sewerMap[0].length-1; c++)
                        {
                           String sewerTile = CultimaPanel.allTerrain.get(Math.abs(sewerMap[r][c])).getName().toLowerCase();
                           if(sewerTile.contains("floor"))
                           {
                              spawns.add(new Coord(r, c));
                           }
                        }
                     }
                     if(spawns.size() == 0)
                     {
                        response = Utilities.getRandomFrom(noBattle);
                     }
                     else
                     {
                        byte charType = NPC.CHUD;
                        int armySize = Math.min(spawns.size(), Utilities.rollDie(8,15));
                        for(int i=0; i<armySize && spawns.size() > 0; i++)
                        {
                           Coord spot = spawns.remove((int)(Math.random()*spawns.size()));
                           int row = spot.getRow();
                           int col = spot.getCol();
                           NPCPlayer monster = new NPCPlayer(charType, row, col, sewerLoc.getMapIndex(), row, col, "sewer");
                           monster.setMoveType(NPC.CHASE);
                           monster.setCanWalkAndSwim(true);
                           CultimaPanel.civilians.get(sewerLoc.getMapIndex()).add(monster);
                        }
                        int row = sewerLoc.getRow();
                        int col = sewerLoc.getCol();
                        spawns.clear();
                        //add some chud in a 8x8 area around (row, col)
                        for(int r = row-4; r <= row+4; r++)
                        {
                           for(int c = col-4; c <= col+4; c++)
                           {
                              if(!LocationBuilder.isImpassable(currMap, r, c))
                              {
                                 spawns.add(new Coord(r,c)); 
                              }
                           }
                        }
                        armySize = Math.min(spawns.size(), Utilities.rollDie(1,5));
                        for(int i=0; i<armySize && spawns.size() > 0; i++)
                        {
                           Coord spot = spawns.remove((int)(Math.random()*spawns.size()));
                           row = spot.getRow();
                           col = spot.getCol();
                           NPCPlayer monster = new NPCPlayer(charType, row, col, mi, row, col, CultimaPanel.player.getLocationType());
                           monster.setMoveType(NPC.ROAM);
                           monster.setCanWalkAndSwim(true);
                           CultimaPanel.worldMonsters.add(monster);
                        }
                        String [] temp = missions[SEWER_CHUD];
                        String [] story = new String[3];
                        story[0] = new String(temp[0]);
                        story[1] = new String(temp[1]);
                        story[2] = new String(temp[2]);
                        int goldReward = Utilities.rollDie(25,50);
                        for(int i=0; i<story.length; i++)
                        {
                           story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
                           story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                        }
                        int startRow = CultimaPanel.player.getWorldRow();
                        int startCol = CultimaPanel.player.getWorldCol();
                        int mRow = -1, mCol = -1;
                        Mission rescueMission = new Mission(missionType, story, startRow, startCol, goldReward, null, sewerLoc.getName(), null, "none", mRow, mCol, SEWER_CHUD, -1);
                        selected.setMissionInfo(rescueMission.getInfo());
                        CultimaPanel.missionStack.add(rescueMission);
                        if(CultimaPanel.missionsGiven[SEWER_CHUD]==0)
                           CultimaPanel.missionsGiven[SEWER_CHUD]++;
                        FileManager.saveProgress();
                        response = rescueMission.getStartStory();
                     }
                  }
               }
               
               else if(missionType.equals("Harvest"))
               {  
                  NPCPlayer targNPC = selected;       
                  if(targNPC!=null)
                  {
                     Item ourItem = new Item("lumber",3);
                     String [] temp = missions[FIND_LUMBER4];
                     String [] story = new String[3];
                     story[0] = new String(temp[0]);
                     story[1] = new String(temp[1]);
                     story[2] = new String(temp[2]);
                     int goldReward = Utilities.rollDie(5, 15);
                     if(CultimaPanel.shoppeDiscount)   //charmed
                        goldReward = (int)(goldReward * 1.20);
                     String targName = Utilities.shortName(selected.getName());         
                     for(int i=0; i<story.length; i++)
                        story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                     int startRow = CultimaPanel.player.getWorldRow();
                     int startCol = CultimaPanel.player.getWorldCol();
                     int mRow = -1, mCol = -1;
                     Mission getMission = new Mission(missionType, story, startRow, startCol, goldReward, null, "none", ourItem, targName, mRow, mCol, FIND_LUMBER4, -1);
                     selected.setMissionInfo(getMission.getInfo());
                     CultimaPanel.missionStack.add(getMission);
                     if(CultimaPanel.missionsGiven[FIND_LUMBER4]==0)
                        CultimaPanel.missionsGiven[FIND_LUMBER4]++;
                     FileManager.saveProgress();
                     response = getMission.getStartStory();
                  }   
               } 
               else if(missionType.equals("Clear"))
               {  //look to see if there is a camp in a 6x6 area around this site, and if so, reuse it
                  Location closeCamp = TerrainBuilder.closeLocation("camp", 7);
                  int r = CultimaPanel.player.getWorldRow();
                  int c = CultimaPanel.player.getWorldCol();
                  Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations,  r,  c, 0);
                  String thisName = "";
                  if(thisLoc != null)
                     thisName = Display.provinceName(thisLoc.getName());
                  ArrayList <Coord> campSpots = new ArrayList<Coord>();
                  Location thisCamp = null;
                  if(closeCamp == null)   //there is no camp close to us, so we will build one
                  {   
                     for(int cr = r-6; cr <= r+6; cr++)
                     {
                        for(int cc = c-6; cc <= c+6; cc++)
                        {
                           if(LocationBuilder.goodSpotForLocation(worldMap, CultimaPanel.equalizeWorldRow(cr), CultimaPanel.equalizeWorldCol(cc)))
                           {
                              if(cr==r && (cc==c || cc==c-1 || cc==c+1))
                              {  //a location can take up 3 spots, (r, c-1), (r, c) and (r, c+1), so skip those coordinates for finding a campsite
                                 continue;
                              }
                              campSpots.add(new Coord(CultimaPanel.equalizeWorldRow(cr), CultimaPanel.equalizeWorldCol(cc)));
                           }
                        }
                     }
                  }
                  if(campSpots.size() == 0 && closeCamp == null)   //no open camp spots
                  {
                     selected.setNumInfo(0);
                     response = Utilities.getRandomFrom(noBattle);
                  }
                  else
                  {
                     if(closeCamp == null)
                     {
                        Coord campSpot = Utilities.getRandomFrom(campSpots);
                        r = campSpot.getRow();
                        c = campSpot.getCol();
                        worldMap[r][c] = TerrainBuilder.getTerrainWithName("LOC_L_1_$Camp").getValue();
                        Teleporter teleporter = new Teleporter((CultimaPanel.map).size());
                        thisCamp = new Location(thisName + " camp", r, c, 0, CultimaPanel.allTerrain.get(Math.abs(worldMap[r][c])), teleporter);
                        CultimaPanel.allLocations.add(thisCamp);
                        CultimaPanel.allCamps.add(thisCamp);
                     }
                     else
                     {
                        r = closeCamp.getRow();
                        c = closeCamp.getCol();
                        closeCamp.getTeleporter().resetTo();
                        thisCamp = closeCamp;
                     }
                     //populate thisCamp with brigands and monsters and save the location
                     LocationBuilder.constructCampAdventureAt(thisCamp, NPC.ORC, NPC.BRIGAND_FIST);
                     TerrainBuilder.markMapPath(thisCamp.getName());
                     String [] temp = missions[MONSTERTRAP];
                     String [] story = new String[3];
                     story[0] = new String(temp[0]);
                     story[1] = new String(temp[1]);
                     story[2] = new String(temp[2]);
                     int goldReward = Utilities.rollDie(25,75);
                     if(CultimaPanel.shoppeDiscount)   //guard is charmed
                        goldReward = (int)(goldReward * 1.20);
                     int startRow = CultimaPanel.player.getWorldRow();
                     int startCol = CultimaPanel.player.getWorldCol();
                     int mRow = thisCamp.getRow();
                     int mCol = thisCamp.getCol();
                     Mission campMission = new Mission(missionType, story, startRow, startCol, goldReward, null, thisCamp.getName(), null, "none", mRow, mCol, MONSTERTRAP, -1);
                     selected.setMissionInfo(campMission.getInfo());
                     CultimaPanel.missionStack.add(campMission);
                     if(CultimaPanel.missionsGiven[MONSTERTRAP]==0)
                        CultimaPanel.missionsGiven[MONSTERTRAP]++;
                     FileManager.saveProgress();
                     response = campMission.getStartStory();
                  }
               }//end create Battle mission
               else if(missionType.equals("Battle"))
               {
                  int r = CultimaPanel.player.getWorldRow();
                  int c = CultimaPanel.player.getWorldCol();
                  Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations,  r,  c, 0);
                  String thisName = "";
                  if(thisLoc != null)
                     thisName = Display.provinceName(thisLoc.getName());
                  ArrayList <Coord> battleSpots = new ArrayList<Coord>();
                  if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r-2), CultimaPanel.equalizeWorldCol(c)))
                     battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r-2), CultimaPanel.equalizeWorldCol(c)));
                  if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r+2), CultimaPanel.equalizeWorldCol(c)))
                     battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r+2), CultimaPanel.equalizeWorldCol(c)));
                  if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r-3), CultimaPanel.equalizeWorldCol(c)))
                     battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r-3), CultimaPanel.equalizeWorldCol(c)));
                  if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r+3), CultimaPanel.equalizeWorldCol(c)))
                     battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r+3), CultimaPanel.equalizeWorldCol(c)));
                  if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(c-3)))
                     battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(c-3)));
                  if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(c+3)))
                     battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(c+3)));
                  if(battleSpots.size() == 0)   //no open battle spots
                  {
                     selected.setNumInfo(0);
                     response = Utilities.getRandomFrom(noBattle);
                  }
                  else
                  {
                     Coord battleSpot = Utilities.getRandomFrom(battleSpots);
                     r = battleSpot.getRow();
                     c = battleSpot.getCol();
                     Location battlefield = null;
                  //if there has already been a battle at this place, just replace the same location with a new battlefield
                     Location oldBattlefield = CultimaPanel.getLocation(thisName + " battlefield");
                     if(oldBattlefield != null)
                     {
                        r = oldBattlefield.getRow();
                        c = oldBattlefield.getCol();
                        oldBattlefield.getTeleporter().resetTo();
                        battlefield = oldBattlefield;
                     }
                     else
                     {
                        worldMap[r][c] = TerrainBuilder.getTerrainWithName("LOC_$Battlefield").getValue();
                        Teleporter teleporter = new Teleporter((CultimaPanel.map).size());
                        battlefield = new Location(thisName + " battlefield", r, c, 0, CultimaPanel.allTerrain.get(Math.abs(worldMap[r][c])), teleporter);
                        CultimaPanel.allLocations.add(battlefield);
                        CultimaPanel.allBattlefields.add(battlefield);
                     }
                     String [] temp = missions[BATTLE];
                     String [] story = new String[3];
                     story[0] = new String(temp[0]);
                     story[1] = new String(temp[1]);
                     story[2] = new String(temp[2]);
                     int goldReward = 100;
                     if(CultimaPanel.shoppeDiscount)   //guard is charmed
                        goldReward = (int)(goldReward * 1.20);
                     for(int i=0; i<story.length; i++)
                     {
                        story[i]=story[i].replace("LOCATION_NAME", battlefield.getName());
                        story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                     }
                     int startRow = CultimaPanel.player.getWorldRow();
                     int startCol = CultimaPanel.player.getWorldCol();
                     int mRow = battlefield.getRow();
                     int mCol = battlefield.getCol();
                     Mission battleMission = new Mission(missionType, story, startRow, startCol, goldReward, null, battlefield.getName(), null, "none", mRow, mCol, BATTLE, -1);
                     selected.setMissionInfo(battleMission.getInfo());
                     CultimaPanel.missionStack.add(battleMission);
                     if(CultimaPanel.missionsGiven[BATTLE]==0)
                        CultimaPanel.missionsGiven[BATTLE]++;
                     FileManager.saveProgress();
                     response = battleMission.getStartStory();
                  }
               }//end create Battle mission
               else if(missionType.equals("Mercy"))
               {
                  String type = "Mercy";
                  Location loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
                  String [] temp = missions[MERCY_KILL];
                  String [] story = new String[3];
                  story[0] = new String(temp[0]);
                  story[1] = new String(temp[1]);
                  story[2] = new String(temp[2]);
                  int goldReward = 0;
                  String targName = Utilities.shortName(selected.getName());
                  if(targName.contains("~"))
                  {
                     String [] parts = targName.split("~");
                     targName = parts[0];
                  }         
                  String locName = Display.provinceName(loc.getName());
                  int startRow = CultimaPanel.player.getWorldRow();
                  int startCol = CultimaPanel.player.getWorldCol();
                  int mRow = -1, mCol = -1;
                  Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, targName, mRow, mCol, MERCY_KILL, -1);
                  selected.setMissionInfo(getMission.getInfo());
                  CultimaPanel.missionStack.add(getMission);
                  if(CultimaPanel.missionsGiven[MERCY_KILL]==0)
                     CultimaPanel.missionsGiven[MERCY_KILL]++;
                  FileManager.saveProgress();
                  response = getMission.getStartStory();
               }
               else if(missionType.equals("Chase"))
               { 
                  String locType = CultimaPanel.player.getLocationType();
                  Coord spawn = Utilities.getRandomFrom(spawnCoords);
                  int r = spawn.getRow();
                  int c = spawn.getCol();
                  NPCPlayer target = new NPCPlayer(NPC.JESTER, r, c, mi, r, c, locType);
                  target.setAgility(Utilities.rollDie(50,70));
                  target.setGold(Utilities.rollDie(25,75));
                  target.setName("Masterthief " + Utilities.shortName(target.getName()));
                  target.setMoveType(NPC.RUN);
                  target.setReputation(-1 * Utilities.rollDie(500,1500));
                  if(Math.random() < 0.25)
                     target.addItem(Item.getRandomMagicItem()); 
                  else 
                     target.addItem(Item.getViperGlove());  
                  CultimaPanel.civilians.get(mi).add(target);
                  String targName = Utilities.shortName(target.getName());
                  String [] temp = missions[CHASE_THIEF];
                  String [] story = new String[3];
                  story[0] = new String(temp[0]);
                  story[1] = new String(temp[1]);
                  story[2] = new String(temp[2]);
                  int goldReward = Utilities.rollDie(40,100);
                  for(int i=0; i<story.length; i++)
                  {
                     story[i]=story[i].replace("NPC_NAME", targName);
                     story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  }
                  int startRow = CultimaPanel.player.getWorldRow();
                  int startCol = CultimaPanel.player.getWorldCol();
                  int mRow = -1;
                  int mCol = -1;
                  Mission chaseMission = new Mission(missionType, story, startRow, startCol, goldReward, null, "none", null, targName, mRow, mCol, CHASE_THIEF, -1);
                  selected.setMissionInfo(chaseMission.getInfo());
                  CultimaPanel.missionStack.add(chaseMission);
                  if(CultimaPanel.missionsGiven[CHASE_THIEF]==0)
                     CultimaPanel.missionsGiven[CHASE_THIEF]++;
                  FileManager.saveProgress();
                  response = chaseMission.getStartStory();
               }
               else if(missionType.equals("Purge"))
               {
                  Location loc = null;
                  //find next closest castle
                  String [] locTypes = {"castle","tower"};
                  Location closeCastle = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
                  ArrayList<NPCPlayer> castleNPCs = CultimaPanel.civilians.get(closeCastle.getMapIndex());
                  double distToCastle = Display.wrapDistance(closeCastle.getRow(), closeCastle.getCol(), CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol());           
                  //find next closest town
                  String []locTypes2 = {"city","fortress","port","village"};
                  Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
                  ArrayList<NPCPlayer>  cityNPCs = CultimaPanel.civilians.get(closeCity.getMapIndex());
                  double distToCity = Display.wrapDistance(closeCity.getRow(), closeCity.getCol(), CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol());           
                  
                  if(castleNPCs.size() > 0 && cityNPCs.size() > 0)
                  {
                     if(distToCastle < distToCity)
                        loc = closeCastle;
                     else
                        loc = closeCity;
                  }
                  else if(castleNPCs.size() > 0)
                     loc = closeCastle;
                  else if(cityNPCs.size() > 0)
                     loc = closeCity;
                  else
                     loc = null;
                                       
                  if(loc!=null)
                  {
                     for(NPCPlayer p:CultimaPanel.civilians.get(loc.getMapIndex()))
                     {
                        if(p.getMapIndex()==loc.getMapIndex() && NPC.isCivilian(p.getCharIndex()) && !p.isNonPlayer())
                        {  //if p has a mission, make sure to remove it from our mission list
                           String NPCmiss = p.getMissionInfo();
                           if(!NPCmiss.equals("none"))
                           {
                              for(int i=0; i<CultimaPanel.missionStack.size(); i++)
                              {
                                 Mission m = CultimaPanel.missionStack.get(i);
                                 if(NPCmiss.equals(m.getInfo()))
                                 {
                                    CultimaPanel.missionStack.remove(i);
                                 }
                              }
                           }
                           p.setNumInfo(-666);  //this changes our civilian into a zombie
                        }
                     }
                     TerrainBuilder.markMapPath(loc.getName());
                     String [] temp = missions[ZOMBIE_PLAGUE];
                     String [] story = new String[3];
                     story[0] = new String(temp[0]);
                     story[1] = new String(temp[1]);
                     story[2] = new String(temp[2]);
                     
                     String locName = Display.provinceName(loc.getName());
                     int numCivs = LocationBuilder.countCivilians(loc);
                     int goldReward = Math.max(250, (numCivs * 20)+(int)(Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), loc.getRow(), loc.getCol())));
                     if(CultimaPanel.shoppeDiscount)   //guard is charmed
                        goldReward = (int)(goldReward * 1.20);
                     
                     for(int i=0; i<story.length; i++)
                     {
                        story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                        story[i]=story[i].replace("LOCATION_NAME", locName);
                     }
                     int startRow = CultimaPanel.player.getWorldRow();
                     int startCol = CultimaPanel.player.getWorldCol();
                     int mRow = loc.getRow();
                     int mCol = loc.getCol();
                     Mission getMission = new Mission(missionType, story, startRow, startCol, goldReward, null, locName, null, "none", mRow, mCol, ZOMBIE_PLAGUE, -1);
                     selected.setMissionInfo(getMission.getInfo());
                     CultimaPanel.missionStack.add(getMission);
                     if(CultimaPanel.missionsGiven[ZOMBIE_PLAGUE]==0)
                        CultimaPanel.missionsGiven[ZOMBIE_PLAGUE]++;
                     FileManager.saveProgress();
                     response = getMission.getStartStory();
                  } 
               }//end create Purge Mission
               else if(missionType.equals("Slay"))
               {  //create Slay mission
                  byte monsterIndex = NPC.randomSlayMissionMonster();
                  if(CultimaPanel.weather > 0)
                     monsterIndex = NPC.randomSlayMissionMonsterRaining();
                  Coord monsterLoc = Utilities.findMonsterSpawn(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), monsterIndex);
                  if(monsterLoc != null)
                  {
                     int monsterRow = monsterLoc.getRow();
                     int monsterCol = monsterLoc.getCol();
                     if(monsterIndex==NPC.DRAGON || monsterIndex==NPC.DRAGONKING)     
                        Sound.dragonCall();
                     if(!Display.frozenWater() && waterLocs.size() > 5 && Math.random() < 0.25)   //25% chance it is a water monster
                     {
                        monsterIndex = NPC.randomSlayMissionWaterMonster();
                        monsterLoc = Utilities.getRandomFrom(waterLocs);
                        monsterRow = monsterLoc.getRow();
                        monsterCol = monsterLoc.getCol();
                     }
                           
                     if(Utilities.NPCAt(monsterRow, monsterCol, 0))
                        Utilities.removeNPCat(monsterRow, monsterCol, 0);
                        
                     String [] temp = missions[SLAY_MONSTER];
                     String [] story = new String[3];
                     story[0] = new String(temp[0]);
                     story[1] = new String(temp[1]);
                     story[2] = new String(temp[2]);
                        //higher reward for targets further away
                     String target = NPC.characterDescription(monsterIndex)+"-head";
                     NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                     int targItemPrice = (monster.getLevel()*5);
                     if(targItemPrice > 250)
                        targItemPrice = 250;
                     int targetRow = monsterIndex;  //record the monster index for spawning a monster of the same type in case we missed it the first time
                     int targetCol = monsterIndex;
                     monster.addItem(new Item(target,targItemPrice));
                     CultimaPanel.worldMonsters.add(monster);
                     for(int i=0; i<story.length; i++)
                     {
                        story[i]=story[i].replace("GOLD_REWARD", ""+targItemPrice);
                        story[i]=story[i].replace("MONSTER_TYPE", ""+NPC.characterDescription(monsterIndex));
                     }
                     int startRow = CultimaPanel.player.getWorldRow();
                     int startCol = CultimaPanel.player.getWorldCol();
                     int mRow = -1, mCol = -1;
                     Mission getMission = new Mission("Slay", story, startRow, startCol, targItemPrice, null, "none", new Item(target, targItemPrice), "none", mRow, mCol, SLAY_MONSTER, -1);
                     getMission.setTargetRow(targetRow);
                     getMission.setTargetCol(targetCol);
                     selected.setMissionInfo(getMission.getInfo());
                     CultimaPanel.missionStack.add(getMission);
                     if(CultimaPanel.missionsGiven[SLAY_MONSTER]==0)
                        CultimaPanel.missionsGiven[SLAY_MONSTER]++;
                     FileManager.saveProgress();
                     response = getMission.getStartStory();
                  }
                  else  
                  {  //could not find valid monster spawn - clear the mission
                     response = "";
                  }
               }//end create Slay mission
               else if(missionType.equals("Roberts"))
               {  //create Roberts mission
                  byte monsterIndex = NPC.BRIGANDSHIP;
                  if(Math.random()<0.5)
                     monsterIndex = NPC.GREATSHIP;
                  Coord monsterLoc =  Utilities.getRandomFrom(waterLocs);
                  if(monsterLoc != null)
                  {
                     int monsterRow = monsterLoc.getRow();
                     int monsterCol = monsterLoc.getCol();
                           
                     if(Utilities.NPCAt(monsterRow, monsterCol, 0))
                        Utilities.removeNPCat(monsterRow, monsterCol, 0);
                           
                     String [] temp = missions[SLAY_ROBERTS];
                     String [] story = new String[3];
                     story[0] = new String(temp[0]);
                     story[1] = new String(temp[1]);
                     story[2] = new String(temp[2]);
                           //higher reward for targets further away
                     String target = Item.getRobertsMask().getName();
                     NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
                     monster.setReputation(-1000);
                     int targItemPrice = (monster.getLevel()*5);
                     if(targItemPrice > 250)
                        targItemPrice = 250;
                     int targetRow = monsterIndex;  //record the monster index for spawning a monster of the same type in case we missed it the first time
                     int targetCol = monsterIndex;
                     monster.addItem(new Item(target,targItemPrice));
                     CultimaPanel.worldMonsters.add(monster);
                     for(int i=0; i<story.length; i++)
                     {
                        story[i]=story[i].replace("GOLD_REWARD", ""+targItemPrice);
                     }
                     int startRow = CultimaPanel.player.getWorldRow();
                     int startCol = CultimaPanel.player.getWorldCol();
                     int mRow = -1, mCol = -1;
                     Mission getMission = new Mission("Roberts", story, startRow, startCol, targItemPrice, null, "none", new Item(target, targItemPrice), "none", mRow, mCol, SLAY_ROBERTS, -1);
                     getMission.setTargetRow(targetRow);
                     getMission.setTargetCol(targetCol);
                     selected.setMissionInfo(getMission.getInfo());
                     CultimaPanel.missionStack.add(getMission);
                     if(CultimaPanel.missionsGiven[SLAY_ROBERTS]==0)
                        CultimaPanel.missionsGiven[SLAY_ROBERTS]++;
                     FileManager.saveProgress();
                     response = getMission.getStartStory();
                  }
                  else  
                  {  //could not find valid monster spawn - clear the mission
                     response = "";
                  }
               }//end create Slay mission
               else if(missionType.equals("Siege") && monsterSpawnCoords.size() > 0)
               { 
                  Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
                  String [] temp = missions[CITY_SIEGE];
                  String [] story = new String[3];
                  story[0] = new String(temp[0]);
                  story[1] = new String(temp[1]);
                  story[2] = new String(temp[2]);
                  int goldReward = Utilities.rollDie(75, 200);
                  int startRow = CultimaPanel.player.getWorldRow();
                  int startCol = CultimaPanel.player.getWorldCol();
                  int mRow = -1;
                  int mCol = -1;
                        //***add monster army to the location
                  String locType = CultimaPanel.player.getLocationType();
                  int numEnemies = Math.max(6, 15);
                  boolean mixedMonsters = false;
                  if(Math.random() < 0.5)
                     mixedMonsters = true;
                  byte monsterType = NPC.randomBattlefieldMonsterLimited();  
                           
                  int row = 1;            //enter from the north
                  int col = (currMap[0].length / 2) - (numEnemies / 4);
                  int randDir = Utilities.rollDie(0,3);
                  if(currLoc.contains("castle") || currLoc.contains("tower"))
                  {  //if there is a monster siege on a castle, make them start at the entrance side
                     Teleporter tele = thisLoc.getTeleporter();
                     int tRow = tele.toRow();
                     int tCol = tele.toCol();
                     if(tRow <= 2)
                     {
                        randDir = 0;
                     }
                     else if(tRow >= currMap.length - 3)
                     {
                        randDir = 2;
                     }
                     else if(tCol <= 2)
                     {
                        randDir = 3;
                     }
                     else //if(tCol >= currMap[0].length - 3)
                     {
                        randDir = 1;
                     }
                  }
                  if(randDir == 1)        //enter from the east
                  {
                     row = (currMap.length / 2) - (numEnemies / 4);
                     col = currMap[0].length - 3;
                  }
                  else if(randDir == 2)   //enter from the south
                  {
                     row = currMap.length - 3;
                     col = (currMap[0].length / 2) - (numEnemies / 4);
                  }
                  else if(randDir == 3)   //enter from the west
                  {
                     row = (currMap.length / 2) - (numEnemies / 4);
                     col = 1;
                  }
                  ArrayList<Coord>fireSpawnCoords = new ArrayList<Coord>();
                  for(int cr = 0; cr<currMap.length; cr++)
                  {
                     for(int cc = 0; cc<currMap[0].length; cc++)
                     {
                        if(LocationBuilder.isCombustableStructure(currMap, cr,cc))
                        {
                           if((randDir == 0 && cr < currMap.length / 3) || (randDir == 1 && cc >= 2*currMap[0].length/3) || (randDir == 2 && cr >= 2*currMap.length/3) || (randDir == 3 && cc < currMap[0].length/3))
                              fireSpawnCoords.add(new Coord(cr, cc));
                        }
                     }
                  }
                           //lite some fires in on the side of town that the army is entering by
                  if(fireSpawnCoords.size() > 0)
                  {
                     int numFires = Math.min(fireSpawnCoords.size(), numEnemies/4);
                     for(int i = 0; i < numFires && fireSpawnCoords.size() > 0; i++)
                     {
                        int fireIndex = (int)(Math.random() * fireSpawnCoords.size());
                        Coord fireSpawn = fireSpawnCoords.remove(fireIndex);
                        int fireRow = fireSpawn.getRow();
                        int fireCol = fireSpawn.getCol();
                        NPCPlayer fire = new NPCPlayer(NPC.FIRE, fireRow, fireCol, mi, fireRow, fireCol, locType);
                        CultimaPanel.worldMonsters.add(fire);
                     }
                  }
                  boolean hasCaptain = false;
                  for(int i=0; i<numEnemies; i++)
                  {
                     byte charType = monsterType;
                     if(mixedMonsters)
                     {
                        charType = NPC.randomBattlefieldMonster();
                        if(hasCaptain)                //only one captain per human army
                           charType = NPC.randomBattlefieldMonsterLimited();
                        if(charType == NPC.CYCLOPS || charType == NPC.GIANT)
                           hasCaptain = true;
                     }
                     else if(NPC.isBrigand(charType))
                     {
                        charType = NPC.randomBrigand();
                     }
                     NPCPlayer monster = new NPCPlayer(charType, row, col, mi, row, col, locType);
                     if(NPC.isBrigand(charType))
                        monster.modifyStats(1.2);
                     if(randDir == 0 || randDir == 2)
                     {
                        col++;
                     }
                     else
                     {
                        row++;
                     }
                     monster.setMoveType(NPC.CHASE);
                     monster.setCanWalkAndSwim(true);
                     if(monsterSpawnCoords.contains(new Coord(row, col)))
                     {
                        CultimaPanel.worldMonsters.add(monster);
                     }
                     if(i == numEnemies / 2)
                     {
                        if(randDir == 0 || randDir == 2)
                        {
                           row++;
                           col = (currMap[0].length / 2) - (numEnemies / 4);
                                 
                        }
                        else
                        {
                           col++;
                           row = (currMap.length / 2) - (numEnemies / 4);
                        }
                     }
                  }
                        
                        //***                   //Mission(String t, String[]story, int wRow, int wCol, int gold,   Item iw,  String loc,  Item targ, String targHolder, int targR, int targC, byte missionType)
                  Mission saveMission = new Mission("Siege",     story,    startRow, startCol, goldReward, null,  thisLoc.getName(),  null,      "none",          mRow,      mCol, CITY_SIEGE, -1);
                  selected.setMissionInfo(saveMission.getInfo());
                  CultimaPanel.missionStack.add(saveMission);
                  if(CultimaPanel.missionsGiven[CITY_SIEGE]==0)
                     CultimaPanel.missionsGiven[CITY_SIEGE]++;
                  FileManager.saveProgress();
                  response = saveMission.getStartStory();
               }
            }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(noBattle)+".";
         selected.setNumInfo(0);
      }
      return response;
   }
   
   //if we have an active Slay mission and the arg monster is a type we are trying to slay,
   //add the mission item to a random spawning monster of the same type if we lose the original
   public static void checkSlayMission(NPCPlayer monster)
   {
     //check world monsters to make sure there doesn't exist one of type monster arg with a head item
      for(NPCPlayer p: CultimaPanel.worldMonsters)
      {
         if(p.getCharIndex()==monster.getCharIndex() && p.hasItem("head"))    //we found it
            return; 
      }
      Mission currMission = null;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(m.getType().toLowerCase().contains("slay") && m.getTarget()!=null)
            if(m.getTargetRow() == monster.getCharIndex())     //we stored the monster index in the targetRow to remember the monster type
            {
               String target = NPC.characterDescription(monster.getCharIndex())+"-head";
               if(!CultimaPanel.player.hasItem(target))
               {
                  int targItemPrice = (monster.getLevel()*10);
                  if(targItemPrice > 150)
                     targItemPrice = 150;
                  monster.addItem(new Item(target,targItemPrice));
               }
               break;
            } 
            else if(m.getType().toLowerCase().contains("roberts") && m.getTarget()!=null)
               if(m.getTargetRow() == monster.getCharIndex())     //we stored the monster index in the targetRow to remember the monster type
               {
                  if(!CultimaPanel.player.hasItem(m.getTarget().getName()))
                  {
                     monster.addItem(m.getTarget());
                  }
                  break;
               }     
      }
   }

   public static String royalGuardMission(NPCPlayer selected)
   {
      String response = "";
      Location loc = LocationBuilder.doesCastleHaveDungeon(CultimaPanel.player.getMapIndex());
      String [] locTypes = {"cave"};
      Location caveLoc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes);
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         if((currMission.getType().contains("Assassinate") || currMission.getType().contains("Slay") || currMission.getType().contains("Find")) && currMission.getTarget()!=null)
         {
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
               if(currMission.getType().contains("Find") && !targetName.contains("bounty") && !targetName.contains("head"))
                  selected.addItem(target);
               CultimaPanel.player.removeItem(target.getName());
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
            else if(currMission.getType().contains("Assassinate") && CultimaPanel.player.hasItem("bounty"))
            {  //if we just have an item called bounty and there is only one bounty mission, resolve it.
               int count = 0;
               for(Mission m:CultimaPanel.missionStack)
               {
                  if(m.getType().contains("Assassinate"))
                     count++;
               }
               if (count==1)
               {
                  CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
                  CultimaPanel.player.removeItem("bounty");
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Train"))   //bring a trained horse to the castle
         {
            for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
            {
               NPCPlayer p = CultimaPanel.worldMonsters.get(i);
               if(p.getCharIndex() == NPC.HORSE && p.hasMet())
               {
                  CultimaPanel.player.setHorseBasicInfo("none");
                  CultimaPanel.worldMonsters.remove(i);
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
                  break;
               }
            }
         
         }
      }  //end seeing if we finished a mission
      if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
         Item itemReward = currMission.getItemReward();
         CultimaPanel.player.addGold(goldReward);
         if(itemReward != null)
            CultimaPanel.player.addItem(itemReward.getName());
         selected.setNumInfo(0);    
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else
      {
         //CASTLE_ASSASSIN, TRAIN HORSE, CASTLE_BEAST
         byte randMission = CASTLE_ASSASSIN;
         
         //see which missions we have yet to be given
         ArrayList<Byte>missionTypes = new ArrayList<Byte>();
         if(CultimaPanel.missionsGiven[CASTLE_ASSASSIN]==0 && loc != null)
            missionTypes.add(CASTLE_ASSASSIN);
         if(CultimaPanel.missionsGiven[CASTLE_BEAST]==0 && loc != null)
            missionTypes.add(CASTLE_BEAST);
         if(CultimaPanel.missionsGiven[TRAIN_HORSE]==0)
            missionTypes.add(TRAIN_HORSE);
         if(CultimaPanel.missionsGiven[FIND_DRAGON_EGG]==0 &&  caveLoc !=null)   
            missionTypes.add(FIND_DRAGON_EGG);          
             
         if(missionTypes.size()==0) //if we have seen all the missions before, pick a random one
         {
            missionTypes.add(TRAIN_HORSE);
            if(loc != null)
            {
               missionTypes.add(CASTLE_ASSASSIN);
               missionTypes.add(CASTLE_BEAST);
            }
            if(caveLoc !=null)   
               missionTypes.add(FIND_DRAGON_EGG);    
         }
         randMission = Utilities.getRandomFrom(missionTypes);                
                                         
         if(randMission == TRAIN_HORSE)
         {
            Item horse = new Item("horse", 100);
            String targetName = Utilities.shortName(selected.getName());
            type = "Train";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 100;
            double priceShift = ((Math.random()*51) - 25)/100.0;  //-.25 to +.25
            goldReward = (int)(goldReward + (goldReward * priceShift));            
            if(CultimaPanel.shoppeDiscount)   //charmed
               goldReward = (int)(goldReward * 1.20);
         
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission trainMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", horse, targetName, mRow, mCol, TRAIN_HORSE, -1);
            selected.setMissionInfo(trainMission.getInfo());
            CultimaPanel.missionStack.add(trainMission);
            if(CultimaPanel.missionsGiven[TRAIN_HORSE]==0)
               CultimaPanel.missionsGiven[TRAIN_HORSE]++;
            FileManager.saveProgress();
            response = trainMission.getStartStory();
         }
         else if(randMission == CASTLE_ASSASSIN)
         {
         //add an assassin into loc
            int assassinType = Utilities.rollDie(1,3);
                       
            byte monsterType = NPC.BOWASSASSIN;
            String targName = "She-Assassin";
            if(assassinType == 1)
            {
               monsterType = NPC.VIPERASSASSIN;
               targName = "Viper-Assassin";
            }
            else if(assassinType == 2)
            {
               monsterType = NPC.ENFORCER;
               targName = "Dark-Enforcer";
            }
            byte[][]currMap = (CultimaPanel.map.get(loc.getMapIndex()));
            int r = (int)(Math.random()*currMap.length);
            int c = (int)(Math.random()*currMap[0].length);
            
            ArrayList<Coord>spawnCoords = new ArrayList<Coord>();
            for(int cr = 0; cr<currMap.length; cr++)
               for(int cc = 0; cc<currMap[0].length; cc++)
                  if(LocationBuilder.canMoveTo(currMap, cr, cc) && !TerrainBuilder.onWater(currMap, cr, cc))
                     spawnCoords.add(new Coord(cr, cc));
            if(spawnCoords.size() > 0)
            {
               Coord spawn = Utilities.getRandomFrom(spawnCoords);
               r = spawn.getRow();
               c = spawn.getCol();
            } 
            else
            {
               selected.setNumInfo(0);    
               return NPC.guardResponse(selected,"");
            }        
         
            NPCPlayer targNPC = new NPCPlayer(monsterType, r, c, loc.getMapIndex(), "dungeon");
            if(Math.random() < 0.75)
               targNPC.addItem(Item.getViperGlove());
            CultimaPanel.civilians.get(loc.getMapIndex()).add(targNPC);
         
         //Assassinate mission
            type = "Assassinate";
            if(targNPC!=null && loc!=null)
            {
              //fill out the map so you can find where the dungeon is
               int numRows = (CultimaPanel.map.get(CultimaPanel.player.getMapIndex()).length);
               int numCols = (CultimaPanel.map.get(CultimaPanel.player.getMapIndex())[0].length);
               TerrainBuilder.markMapArea(CultimaPanel.player.getMapIndex(), 1, 1, numRows-1, numCols-1);
              
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 250;
               String target = targName+"-bounty";
               targNPC.addItem(new Item(target, goldReward));
            
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target, goldReward), targName, mRow, mCol, CASTLE_ASSASSIN, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[CASTLE_ASSASSIN]==0)
                  CultimaPanel.missionsGiven[CASTLE_ASSASSIN]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         else if(randMission == CASTLE_BEAST)
         {
         //add a beast into loc
            byte monsterType = NPC.randomMonsterKing();
                        
            byte[][]currMap = (CultimaPanel.map.get(loc.getMapIndex()));
            int r = (int)(Math.random()*currMap.length);
            int c = (int)(Math.random()*currMap[0].length);
            
            ArrayList<Coord>spawnCoords = new ArrayList<Coord>();
            for(int cr = 0; cr<currMap.length; cr++)
               for(int cc = 0; cc<currMap[0].length; cc++)
                  if(LocationBuilder.canMoveTo(currMap, cr, cc) && !TerrainBuilder.onWater(currMap, cr, cc))
                     spawnCoords.add(new Coord(cr, cc));
            if(spawnCoords.size() > 0)
            {
               Coord spawn = Utilities.getRandomFrom(spawnCoords);
               r = spawn.getRow();
               c = spawn.getCol();
            } 
            else
            {
               selected.setNumInfo(0);    
               return NPC.guardResponse(selected,"");
            }        
         
            NPCPlayer targNPC = new NPCPlayer(monsterType, r, c, loc.getMapIndex(), "dungeon");
            String target = NPC.characterDescription(monsterType)+"-head";
            int goldReward = 250;
            targNPC.addItem(new Item(target, goldReward));
            targNPC.setMoveType(NPC.CHASE);
            CultimaPanel.civilians.get(loc.getMapIndex()).add(targNPC);
         
            type = "Slay";
            if(targNPC!=null && loc!=null)
            {
               //fill out the map so you can find where the dungeon is
               int numRows = (CultimaPanel.map.get(CultimaPanel.player.getMapIndex()).length);
               int numCols = (CultimaPanel.map.get(CultimaPanel.player.getMapIndex())[0].length);
               TerrainBuilder.markMapArea(CultimaPanel.player.getMapIndex(), 1, 1, numRows-1, numCols-1);
               
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
                                
               String locName = Display.provinceName(loc.getName());
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target, goldReward), "large beast", mRow, mCol, CASTLE_BEAST, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[CASTLE_BEAST]==0)
                  CultimaPanel.missionsGiven[CASTLE_BEAST]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         else if (randMission == FIND_DRAGON_EGG && caveLoc !=null)
         {
            type = "Find";
         //add a dragon and egg into loc
            Item target = Item.getDragonEgg();
            byte monsterType = NPC.DRAGON;
            if(Math.random() < 0.05)
               monsterType = NPC.DRAGONKING;
            NPCPlayer targNPC = new NPCPlayer(monsterType, 0, 0, 0, "cave");
            Coord spawnCoord = TerrainBuilder.addNPCtoLocationReturnPoint(caveLoc, "cave", targNPC);
            if(targNPC!=null && caveLoc!=null && spawnCoord!=null)
            {
               CultimaPanel.eggsToHarvest.add(new RestoreItem(caveLoc.getMapIndex(), spawnCoord.getRow(), spawnCoord.getCol(), NPC.DRAGON));
               NPCPlayer marker = new NPCPlayer(NPC.BEGGER, spawnCoord.getRow(), spawnCoord.getCol(), caveLoc.getMapIndex(), "temp", "temp,1,1,1,10", false);
               CultimaPanel.civilians.get(caveLoc.getMapIndex()).add(marker);   //add a nonPlayer NPC marker so we can be directed to the mission item with the Knowing spell
               TerrainBuilder.markMapPath(caveLoc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Math.max(150, target.getValue()/3);
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(selected.getName());         
               String locName = Display.provinceName(caveLoc.getName());
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               mRow = caveLoc.getRow();
               mCol = caveLoc.getCol();
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, target, targName, mRow, mCol, FIND_DRAGON_EGG, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[FIND_DRAGON_EGG]==0)
                  CultimaPanel.missionsGiven[FIND_DRAGON_EGG]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
         }
      }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(NPC.kingNoMission)+".";
         selected.setNumInfo(0);
      }
      return response;
   }

   public static String kingMission(NPCPlayer selected)
   {
      String response = "";
      boolean dungeonWithMonsters = false;
      boolean closeLairWithPrisoners = false;
      Location loc = LocationBuilder.doesCastleHaveDungeon(CultimaPanel.player.getMapIndex());
      if(loc != null && loc.getMonsterFreq().size()>0)
         dungeonWithMonsters = true;
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;
      boolean isGarriott = (selected.getName().contains("Garriott"));//Is this Richard Garriott (Lord British)?
   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getType().contains("Clear") && currMission.getClearLocation().length()>0)
         {                    //this is a mission to clear a location of monsters
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               ArrayList<Coord> monsterFreq = missLoc.getMonsterFreq();
               if(monsterFreq==null || monsterFreq.size()==0)  //we cleared out the monsters here
               {
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getMissionType() == CURE_KING)
         {
            if(!selected.hasEffect("poison"))
            {
               finishMission(currMission, selected, 15);
            }
         }
         else if((currMission.getType().contains("Assassinate") || currMission.getType().contains("Find")) && (currMission.getTarget()!=null))
         {
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               if(currMission.getType().contains("Assassinate"))
                  CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
               CultimaPanel.player.removeItem(target.getName());
               if(!target.getName().contains("bounty") && !target.getName().contains("head"))
                  selected.addItem(target);
               int reputationPoints = 0;
               if(currMission.getType().contains("Find"))
                  reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
            else if(currMission.getType().contains("Assassinate") && CultimaPanel.player.hasItem("bounty"))
            {  //if we just have an item called bounty and there is only one bounty mission, resolve it.
               int count = 0;
               for(Mission m:CultimaPanel.missionStack)
               {
                  if(m.getType().contains("Assassinate"))
                     count++;
               }
               if (count==1)
               {
                  CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
                  if(currMission.getType().contains("Assassinate"))
                     CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
                  CultimaPanel.player.removeItem("bounty");
                  int reputationPoints = 0;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Rescue") && currMission.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               if(LocationBuilder.countPrisoners(missLoc)==0)      //we freed all the prisoners
               {
                  int reputationPoints = 10;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Purge") && currMission.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               int numCivs = LocationBuilder.countCivilians(missLoc);
               if(numCivs==0)      //we killed or scared out all the inhabitants
               {
                  int reputationPoints = -10;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
      }
      if(selected.getNumInfo() <= 0)
         response = Utilities.getRandomFrom(NPC.kingNoMission)+".";
      else if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         Utilities.standDownGuards();
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
         Item itemReward = currMission.getItemReward();
         CultimaPanel.player.addGold(goldReward);
         if(itemReward != null)
            CultimaPanel.player.addItem(itemReward.getName());
         selected.setNumInfo(selected.getNumInfo()-1);
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         if(currMission.getMissionType() == CURE_KING)
         {  //make a follow-up bounty mission
            if(selected.getNumInfo()==0)
            {  
               selected.setNumInfo(1);
            }
            ArrayList<NPCPlayer> inLocNPCs = Utilities.getNPCsInLoc(selected.getMapIndex(), NPC.CHILD, selected.getName());
            NPCPlayer targNPC = null;
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            ArrayList<NPCPlayer> targets = new ArrayList<NPCPlayer>();
            for(NPCPlayer pl: inLocNPCs)
               if(!pl.getName().equals(selected.getName()))
                  targets.add(pl);
            if(targets.size()==0)
               targNPC = null;
            else
               targNPC = Utilities.getRandomFrom(targets);    
            if(targNPC!=null && loc!=null)
            {
               targNPC.setMoveType(NPC.RUN);
               String [] temp = missions[ASSASSINATION];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               goldReward = Utilities.rollDie(50,150);
               if(CultimaPanel.shoppeDiscount)   //king is charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(targNPC.getName());
               response = response.replace("NPC_NAME",  targName);
               CultimaPanel.missionTargetNames.add(targName);
               String target = targName+"-head";
               if(isGarriott)
               {
                  goldReward *= 2;
               }
               targNPC.addItem(new Item(target, 0));
               String locName = Display.provinceName(loc.getName());
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
                  story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission("Assassinate", story, startRow, startCol, goldReward, null, locName, new Item(target, goldReward), targName, mRow, mCol, ASSASSINATION, -1);
               getMission.setState(Mission.IN_PROGRESS);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[ASSASSINATION]==0)
                  CultimaPanel.missionsGiven[ASSASSINATION]++;
            }     
         }   
         FileManager.saveProgress();
      }   
      else
      {
      //CLEAR_DUNGEON, ASSASSINATION, RESCUE_PRISONERS, FIND, PURGE_CITY
         byte randMission = ASSASSINATION;
         ArrayList<Byte>missionTypes = new ArrayList<Byte>();
         if(CultimaPanel.missionsGiven[CLEAR_DUNGEON]==0 && dungeonWithMonsters)
            missionTypes.add(CLEAR_DUNGEON);
         if(CultimaPanel.missionsGiven[ASSASSINATION]==0)
            missionTypes.add(ASSASSINATION);
         if(CultimaPanel.missionsGiven[CURE_KING]==0)
            missionTypes.add(CURE_KING);
         if(CultimaPanel.missionsGiven[RESCUE_PRISONERS]==0)
            missionTypes.add(RESCUE_PRISONERS);
         if(CultimaPanel.missionsGiven[FIND_ITEM]==0)
            missionTypes.add(FIND_ITEM);
         if(CultimaPanel.missionsGiven[PURGE_CITY]==0 && selected.getReputation() <= -100)
            missionTypes.add(PURGE_CITY);
         
         //if we have seen all the missions before, pick a random one
         if(missionTypes.size()==0)
         {     
            missionTypes.add(CURE_KING);       
            if(dungeonWithMonsters)
               missionTypes.add(Mission.CLEAR_DUNGEON);
            missionTypes.add(Mission.ASSASSINATION);
            if(selected.getReputation() > -100)
               missionTypes.add(Mission.RESCUE_PRISONERS);
            missionTypes.add(Mission.FIND_ITEM);
            if(selected.getReputation() <= -100)
               missionTypes.add(Mission.PURGE_CITY);
         
            randMission = Utilities.getRandomFrom(missionTypes);
            if(selected.getNumInfo()==1)
            {
               if(dungeonWithMonsters)                   //prioritize clearing our own dungeon and rescuing subjects
                  randMission = Mission.CLEAR_DUNGEON;
               else if(closeLairWithPrisoners)
                  randMission = Mission.RESCUE_PRISONERS;
            } 
         }
         else  //pick among the missions we have not seen yet
         {
            randMission = Utilities.getRandomFrom(missionTypes);
         }
         
         //***TESTING*** randMission = CURE_KING;
         
         if(randMission == Mission.RESCUE_PRISONERS)
         {
            String [] locs = {"lair"};
            Location lairLoc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locs);
            lairLoc = TerrainBuilder.closeLairWithPrisoners(CultimaPanel.allLocations);
            if(lairLoc==null)
            {
               for(int i=missionTypes.size()-1; i>=0; i--)
                  if(missionTypes.get(i) == Mission.RESCUE_PRISONERS)
                  {
                     missionTypes.remove(i);
                     break;
                  }
            }
            if(lairLoc==null)       
            {              
               if(missionTypes.size() == 0)
                  randMission = -1;
               else
                  randMission = Utilities.getRandomFrom(missionTypes);
            }
            else
               loc = lairLoc;
         }
         if(randMission == -1)      //no mission found
         {
            response = Utilities.getRandomFrom(NPC.kingNoMission)+".";
         }   
         else if(loc!=null && randMission == Mission.CLEAR_DUNGEON)
         {          
            TerrainBuilder.markMapPath(loc.getName());
            type = "Clear";
            ArrayList<Coord> monsterFreq = loc.getMonsterFreq();
            if(monsterFreq.size() > 0)
            {
               //fill out the map so you can find where the dungeon is
               int numRows = (CultimaPanel.map.get(CultimaPanel.player.getMapIndex()).length);
               int numCols = (CultimaPanel.map.get(CultimaPanel.player.getMapIndex())[0].length);
               TerrainBuilder.markMapArea(CultimaPanel.player.getMapIndex(), 1, 1, numRows-1, numCols-1);
               
               byte monsterIndex = (byte)((monsterFreq.get(0)).getRow());
               String monsterType = NPC.characterDescription(monsterIndex);
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Math.max(250, (int)((monsterFreq.get(0)).getCol()));
               if(isGarriott)
                  goldReward *= 2;
               if(CultimaPanel.shoppeDiscount)   //king is charmed
                  goldReward = (int)(goldReward * 1.20);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("MONSTER_TYPE", monsterType);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission clearMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, "none", mRow, mCol, CLEAR_DUNGEON, -1);
               selected.setMissionInfo(clearMission.getInfo());
               CultimaPanel.missionStack.add(clearMission);
               if(CultimaPanel.missionsGiven[CLEAR_DUNGEON]==0)
                  CultimaPanel.missionsGiven[CLEAR_DUNGEON]++;
               FileManager.saveProgress();
               response = clearMission.getStartStory();
            }
            else
            {  //no monsters in the location
               response = Utilities.getRandomFrom(NPC.kingNoMission)+".";
            }
         }
         else if(randMission == CURE_KING)
         { 
            selected.addEffect("poison");
            Location currentLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            type = "Cure";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            String targName = Utilities.shortName(selected.getName());         
            Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, currentLoc.getName(), null, targName, mRow, mCol, randMission, -1);
            selected.setMissionInfo(rescueMission.getInfo());
            CultimaPanel.missionStack.add(rescueMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = rescueMission.getStartStory();
         }
         
         else if(randMission == Mission.ASSASSINATION || randMission == Mission.FIND_ITEM || randMission == Mission.PURGE_CITY)
         {  //Assassinate mission
            if(randMission == Mission.ASSASSINATION)
               type = "Assassinate";
            else if(randMission == Mission.FIND_ITEM)
               type = "Find";
            else
               type = "Purge";  
            int randType = Utilities.rollDie(10);     //[1,10]
         
            ArrayList<NPCPlayer> inLocNPCs = null;
            if(randMission == Mission.ASSASSINATION)
            {     //for assassination missions, exclude children
               inLocNPCs = Utilities.getNPCsInLoc(selected.getMapIndex(), NPC.CHILD, selected.getName());
            }
            else  //FIND_ITEM or PURGE_CITY
            {
               inLocNPCs = Utilities.getNPCsInLoc(selected.getMapIndex(), selected.getName());
            }
            NPCPlayer targNPC = null;
            if(inLocNPCs.size() <= 1 || randMission == Mission.PURGE_CITY)
               randType = Utilities.rollDie(4,10);    //if there is nobody else in this location or a purge city mission, always pick a different location for the target 
            if(randType <= 3)                      //target is at the same location 30% of the time (roll 1-3), unless it is PURGE or nobody is in this location
            {
               loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               ArrayList<NPCPlayer> targets = new ArrayList<NPCPlayer>();
               for(NPCPlayer pl: inLocNPCs)
                  if(!pl.getName().equals(selected.getName()))
                     targets.add(pl);
               if(targets.size()==0)
                  targNPC = null;
               else
                  targNPC = Utilities.getRandomFrom(targets);    
            }
            else if(randType <= 6)                 //target is at the next closest castle 30% of the time (roll 4-6), or a PURGE mission 
            {
               String [] locTypes = {"castle","tower"};
               Location closeCastle = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
               int locMapIndex = selected.getMapIndex();
               if(closeCastle != null)
               {
                  locMapIndex = closeCastle.getMapIndex();
               }
               if(randMission == Mission.ASSASSINATION)
               {     //for assassination missions, exclude children
                  inLocNPCs = Utilities.getNPCsInLoc(locMapIndex, NPC.CHILD, selected.getName());
               }
               else  //FIND_ITEM or PURGE_CITY
               {
                  inLocNPCs = Utilities.getNPCsInLoc(locMapIndex, selected.getName());
               }
               if(inLocNPCs.size() >= 1)
               {
                  targNPC = Utilities.getRandomFrom(inLocNPCs);
                  loc = closeCastle;
               }
               else
                  loc = null;
            }
            else                                   //target is at the next closest city 40% of the time (roll 7-10), or a PURGE mission
            {
               String []locTypes2 = {"city","fortress","port","village"};
               Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
               int locMapIndex = selected.getMapIndex();
               if(closeCity != null)
               {
                  locMapIndex = closeCity.getMapIndex();
               }
               if(randMission == Mission.ASSASSINATION)
               {     //for assassination missions, exclude children
                  inLocNPCs = Utilities.getNPCsInLoc(locMapIndex, NPC.CHILD, selected.getName());
               }
               else  //FIND_ITEM or PURGE_CITY
               {
                  inLocNPCs = Utilities.getNPCsInLoc(locMapIndex, selected.getName());
               }
               if(inLocNPCs.size() >= 1)
               {
                  targNPC = Utilities.getRandomFrom(inLocNPCs);
                  loc = closeCity;
               }
               else
                  loc = null;
            }
            if(targNPC!=null && loc!=null)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               if(randMission == Mission.PURGE_CITY)
               {
                  String locName = Display.provinceName(loc.getName());
                  int numCivs = LocationBuilder.countCivilians(loc);
                  int goldReward = Math.max(350, (numCivs * 20)+(int)(Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), loc.getRow(), loc.getCol())));
                  if(isGarriott)
                     goldReward *= 2;
                  if(CultimaPanel.shoppeDiscount)   //king is charmed
                     goldReward = (int)(goldReward * 1.20);
               
                  for(int i=0; i<story.length; i++)
                  {
                     story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                     story[i]=story[i].replace("LOCATION_NAME", locName);
                     story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
                  }
                  int startRow = CultimaPanel.player.getWorldRow();
                  int startCol = CultimaPanel.player.getWorldCol();
                  int mRow = loc.getRow();
                  int mCol = loc.getCol();
                  Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, "none", mRow, mCol, PURGE_CITY, -1);
                  selected.setMissionInfo(getMission.getInfo());
                  CultimaPanel.missionStack.add(getMission);
                  if(CultimaPanel.missionsGiven[PURGE_CITY]==0)
                     CultimaPanel.missionsGiven[PURGE_CITY]++;
                  FileManager.saveProgress();
                  response = getMission.getStartStory();
               }
               else
               {
               //higher reward for targets further away
                  int goldReward = Math.max(250, targNPC.getLevel()+(int)(Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), loc.getRow(), loc.getCol())));
                  if(CultimaPanel.shoppeDiscount)   //king is charmed
                     goldReward = (int)(goldReward * 1.20);
                  String targName = Utilities.shortName(targNPC.getName());
                  CultimaPanel.missionTargetNames.add(targName);
                  String target = targName+"-bounty";
                  if(randMission == Mission.FIND_ITEM)
                  {
                     String [] items = {"holdall", "charmring", "focushelm", "pentangle", "mannastone", "powerbands", "swiftboots", "mindtome", "swiftquill"};
                     goldReward = 500;
                     target = Utilities.getRandomFrom(items);
                  }
                  if(isGarriott)
                     goldReward *= 2;
               
               //the target might value the item differently if they are willing to sell it
                  double priceShift = ((Math.random()*51) - 25)/100.0;  //-.25 to +.25
                  if(randMission == Mission.ASSASSINATION)
                     priceShift = 0;
                  int targItemPrice = (int)(goldReward + (goldReward * priceShift));
                  targNPC.addItem(new Item(target, targItemPrice));
                
                  String locName = Display.provinceName(loc.getName());
               
                  for(int i=0; i<story.length; i++)
                  {
                     story[i]=story[i].replace("NPC_NAME", targName);
                     story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                     story[i]=story[i].replace("LOCATION_NAME", locName);
                     story[i]=story[i].replace("ITEM_NAME", target);
                     story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
                  }
                  int startRow = CultimaPanel.player.getWorldRow();
                  int startCol = CultimaPanel.player.getWorldCol();
                  int mRow = -1, mCol = -1;
                  if(targNPC.getMapIndex() != selected.getMapIndex())
                  {
                     mRow = loc.getRow();
                     mCol = loc.getCol();
                  }
                  Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target, goldReward), targName, mRow, mCol, randMission, -1);
                  selected.setMissionInfo(getMission.getInfo());
                  CultimaPanel.missionStack.add(getMission);
                  if(randMission==ASSASSINATION && CultimaPanel.missionsGiven[ASSASSINATION]==0)
                     CultimaPanel.missionsGiven[ASSASSINATION]++;
                  else if(randMission==FIND_ITEM && CultimaPanel.missionsGiven[FIND_ITEM]==0)
                     CultimaPanel.missionsGiven[FIND_ITEM]++;
                  FileManager.saveProgress();
                  response = getMission.getStartStory();
               }
            }   
         } 
         else if(randMission == Mission.RESCUE_PRISONERS)
         {        //rescue mission
            int numPrisoners = LocationBuilder.countPrisoners(loc);
            if(numPrisoners > 0)
            {
               type = "Rescue";
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Math.max(250, numPrisoners*20);
               if(isGarriott)
                  goldReward *= 2;
               if(CultimaPanel.shoppeDiscount)   //king is charmed
                  goldReward = (int)(goldReward * 1.20);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", loc.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = loc.getRow();
               int mCol = loc.getCol();
            
               TerrainBuilder.markMapPath(loc.getName());      
               Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, "none", mRow, mCol, RESCUE_PRISONERS, -1);
               selected.setMissionInfo(rescueMission.getInfo());
               CultimaPanel.missionStack.add(rescueMission);
               if(CultimaPanel.missionsGiven[RESCUE_PRISONERS]==0)
                  CultimaPanel.missionsGiven[RESCUE_PRISONERS]++;
               FileManager.saveProgress();
               response = rescueMission.getStartStory();
            }
         }
      }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(NPC.kingNoMission)+".";
         selected.setNumInfo(0);
      }
      return response;
   }

   public static String swordMission(NPCPlayer selected)
   {
      String response = "";
      Location loc = null;
      Item target = null;
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;
      int currMissionIndex = -1;
      int mi = CultimaPanel.player.getMapIndex();
      boolean hasSewer = LocationBuilder.doesCityHaveSewer(mi);
   
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getMissionType() == HEAL_WARRIOR)
         {
            if(selected.getBody() >= selected.maxHealth()/2)
            {
               selected.setAgility(Utilities.rollDie(15,50));
               finishMission(currMission, selected, 10);
            } 
         }
         else if(currMission.getMissionType() == BUILD_PIER)
         {
            if(Utilities.dockSpots().size() > 0)
            {
               finishMission(currMission, selected, 10);
            }   
         }
         else if(currMission.getType().contains("Boat"))   //bring a boat or ship to someone
         {
            boolean missionDone = false;
            for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
            {
               NPCPlayer p = CultimaPanel.worldMonsters.get(i);
               if(NPC.isShip(p) && p.hasMet() && Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), p.getRow(), p.getCol()) < CultimaPanel.SCREEN_SIZE/2)
               {  //see if there is a boat or ship close to the port that we are in
                  CultimaPanel.worldMonsters.remove(i);
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
                  missionDone = true;
                  break;
               }
            }
            if(!missionDone)
            {
               for(int i=CultimaPanel.boats.size()-1; i>=0; i--)
               {
                  NPCPlayer p = CultimaPanel.boats.get(i);
                  if(NPC.isShip(p) && Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), p.getRow(), p.getCol()) < CultimaPanel.SCREEN_SIZE/2)
                  {  //see if there is a boat or ship close to the port that we are in
                     CultimaPanel.boats.remove(i);
                     int reputationPoints = 5;
                     finishMission(currMission, selected, reputationPoints);
                     break;
                  }
               }
            }
         }
         else if(currMission.getMissionType() == KAIJU)
         {  //done if we kill the kaiju or lead it far enough away
            Location cityLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            Coord kaijuLocation = LocationBuilder.findKaiju(cityLoc);
            boolean kaijuDone = false;
            if(kaijuLocation == null)
            {
               kaijuDone = true;
            }
            else
            {
               double distToCity = Display.findDistance(kaijuLocation.getRow(), kaijuLocation.getCol(), cityLoc.getRow(), cityLoc.getCol());
               if(distToCity >= CultimaPanel.SCREEN_SIZE*1.5)
               {
                  kaijuDone = true;
               }
            }
            if(kaijuDone)
            {
               for(int i=CultimaPanel.worldMonsters.size()-1; i>=0; i--)
               {
                  NPCPlayer p = CultimaPanel.worldMonsters.get(i); 
                  if(NPC.isGiantMonsterKing(p))
                  {
                     CultimaPanel.worldMonsters.remove(i);
                  }
               }
               CultimaPanel.monsterDist = -1;
               finishMission(currMission, selected, 20);
            } 
         }
         else if(currMission.getType().contains("Clear") && currMission.getClearLocation().length()>0)
         {                    //this is a mission to clear a location of monsters
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               ArrayList<Coord> monsterFreq = missLoc.getMonsterFreq();
               if(monsterFreq==null || monsterFreq.size()==0)  //we cleared out the monsters here
               {
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Purge") && currMission.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               int numCivs = LocationBuilder.countCivilians(missLoc);
               if(numCivs==0)      //we killed or scared out all the inhabitants
               {
                  int reputationPoints = -10;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Find") && currMission.getTarget()!=null)
         {
            target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               if(Weapon.getWeaponWithName(target.getName())!=null)
               {   //remove weapon from player and add to selected
                  Weapon dropped = CultimaPanel.player.discardWeapon(targetName);
                  selected.setWeapon(dropped);
               }
               else if(Armor.getArmorWithName(target.getName())!=null)
               {  //remove armor from player and add to selected
                  Armor dropped = CultimaPanel.player.discardArmor(targetName);
                  selected.setArmor(dropped);
               }
               else
               {
                  CultimaPanel.player.removeItem(target.getName());
                  selected.addItem(target);
               }
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Treasure") && currMission.getGoldReward() > 0)
         {
            if(CultimaPanel.player.getGold() > currMission.getGoldReward())
            {
               CultimaPanel.player.pay(currMission.getGoldReward());
               selected.addGold(currMission.getGoldReward());
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         if(currMission.getType().contains("Slay") && currMission.getTarget()!=null)
         {
            Item missTarget = currMission.getTarget();
            String targetName = missTarget.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
               CultimaPanel.player.removeItem(missTarget.getName());
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getMissionType() == TEACH_3_TOWERS)
         {
            if(CultimaPanel.player.getTowersPuzzleInfo()>=NPC.towersPuzzleInfo.length && CultimaPanel.player.stats[Player.PUZZLES_SOLVED] >= 1)
            {
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
      }
      Coord kaijuSpawn = Utilities.findKaijuMonsterSpawn(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol());
      
      if(selected.getNumInfo() != 10)
         response = Utilities.getRandomFrom(NPC.noMission)+".";
      else if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
         Item itemReward = currMission.getItemReward();
         if(!currMission.getType().contains("Treasure"))       //don't add gold or item for Treasure map mission - goldReward is used to store the amount we need to get and give to the adventurer
         {
            CultimaPanel.player.addGold(goldReward);           
            if(itemReward != null)
               CultimaPanel.player.addItem(itemReward.getName());
         }
         selected.setNumInfo(Utilities.rollDie(1,3));
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else
      {  //CLEAR_MINE, FIND_WEAPON, TREASURE_MAP
         byte randMission = FIND_WEAPON;
         String ourLoc = CultimaPanel.player.getLocationType().toLowerCase();
         boolean inCapital = Utilities.isCapitalCity(CultimaPanel.player.getMapIndex());
         ArrayList<Coord> waterSpots = Utilities.waterInView();
         ArrayList<Byte> missionTypes = new ArrayList<Byte>();
         if(waterSpots.size() >= 5 && CultimaPanel.missionsGiven[BUILD_PIER]==0)
            missionTypes.add(Mission.BUILD_PIER);
         if(hasSewer && CultimaPanel.missionsGiven[SEWER_SLAY]==0)
            missionTypes.add(Mission.SEWER_SLAY);   
         if(CultimaPanel.missionsGiven[CLEAR_MINE]==0)
            missionTypes.add(Mission.CLEAR_MINE);
         if(CultimaPanel.missionsGiven[TEACH_3_TOWERS]==0)
            missionTypes.add(Mission.TEACH_3_TOWERS);
         if(CultimaPanel.missionsGiven[HEAL_WARRIOR]==0)
            missionTypes.add(Mission.HEAL_WARRIOR);
         if(CultimaPanel.missionsGiven[KAIJU]==0 && kaijuSpawn!=null)
            missionTypes.add(Mission.KAIJU);
         if(CultimaPanel.missionsGiven[FIND_WEAPON]==0)
            missionTypes.add(Mission.FIND_WEAPON);
         if(CultimaPanel.missionsGiven[TREASURE_MAP]==0 && selected.getReputation() < -10)
            missionTypes.add(Mission.TREASURE_MAP);
         if(selected.isWerewolf() && CultimaPanel.missionsGiven[MERCY_KILL]==0)   //werewolf tag
            missionTypes.add(MERCY_KILL);   
         if(CultimaPanel.missionsGiven[WEREWOLF_TOWN]==0)
            missionTypes.add(WEREWOLF_TOWN);   
         if(CultimaPanel.missionsGiven[GET_BOAT]==0 && (ourLoc.contains("port") || inCapital))
            missionTypes.add(GET_BOAT);   
      
         if(missionTypes.size()==0)
         {
            missionTypes.add(Mission.TEACH_3_TOWERS);
            if(hasSewer)
               missionTypes.add(Mission.SEWER_SLAY);
            missionTypes.add(Mission.HEAL_WARRIOR);
            if(waterSpots.size() >= 5)
               missionTypes.add(Mission.BUILD_PIER);
            missionTypes.add(Mission.CLEAR_MINE);
            missionTypes.add(Mission.FIND_WEAPON);
            if(kaijuSpawn!=null)
               missionTypes.add(Mission.KAIJU);
            if(selected.getReputation() < -10)
               missionTypes.add(Mission.TREASURE_MAP);
            if(selected.isWerewolf())   //werewolf tag
               missionTypes.add(MERCY_KILL);
            missionTypes.add(WEREWOLF_TOWN);
            if(ourLoc.contains("port") || inCapital)
               missionTypes.add(GET_BOAT);
         }
         randMission = Utilities.getRandomFrom(missionTypes);
         
        //***TESTING*** randMission=SEWER_SLAY;
      
        //try to build a good treasure map
         Item treasuremap = null;
         if(randMission == Mission.TREASURE_MAP)
         {
            byte[][]currMap = (CultimaPanel.map.get(0));   
            int c = CultimaPanel.equalizeWorldCol(CultimaPanel.player.getWorldCol() + Utilities.rollDie(-100, 100));
            int r = CultimaPanel.equalizeWorldRow(CultimaPanel.player.getWorldRow() + Utilities.rollDie(-100, 100));
            int numTries = 0;
            while(!TerrainBuilder.isGoodSpotForTreasure(r, c) && numTries < 1000)
            {
               c = CultimaPanel.equalizeWorldCol(CultimaPanel.player.getWorldCol() + Utilities.rollDie(-100, 100));
               r = CultimaPanel.equalizeWorldRow(CultimaPanel.player.getWorldRow() + Utilities.rollDie(-100, 100));
               numTries++;
            }
            if(TerrainBuilder.isGoodSpotForTreasure(r, c))
               treasuremap = (new Item("treasuremap:("+c+":"+r+")", 500));
            else
               randMission = FIND_WEAPON;  
         }
         //try to find a good location for CLEAR_MINE
         if(randMission == Mission.CLEAR_MINE)
         {
            String [] locTypes = {"mine"};
            Location mineLoc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
            if(mineLoc==null || mineLoc.getMonsterFreq().size() == 0)
               randMission = FIND_WEAPON;  
            else
               loc = mineLoc;    
         } 
         //try to find a good location for TEACH_3_TOWERS
         if(randMission == Mission.TEACH_3_TOWERS)
         {
            String [] locTypes = {"cave"};
            Location caveLoc = TerrainBuilder.closeLocation(CultimaPanel.allLocations, locTypes);
            if(caveLoc==null)
               randMission = FIND_WEAPON;  
            else
               loc = caveLoc;    
         }
         if(loc!=null && randMission == Mission.TEACH_3_TOWERS)
         {          
            TerrainBuilder.markMapPath(loc.getName());
            type = "Teach";
            String targName = Utilities.shortName(selected.getName());                        
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            Item randGem = Item.getRandomStone();
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("PRIZE", randGem.getName());
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            mRow = loc.getRow();
            mCol = loc.getCol();
            Mission clearMission = new Mission(type, story, startRow, startCol, goldReward, randGem, loc.getName(), null, targName, mRow, mCol, TEACH_3_TOWERS, -1);
            selected.setMissionInfo(clearMission.getInfo());
            CultimaPanel.missionStack.add(clearMission);
            if(CultimaPanel.missionsGiven[TEACH_3_TOWERS]==0)
               CultimaPanel.missionsGiven[TEACH_3_TOWERS]++;
            FileManager.saveProgress();   
            response = clearMission.getStartStory();
         }       
         if(loc!=null && randMission == Mission.CLEAR_MINE)
         {          
            TerrainBuilder.markMapPath(loc.getName());
            type = "Clear";
            ArrayList<Coord> monsterFreq = loc.getMonsterFreq();
            if(monsterFreq.size() > 0)
            {
               byte monsterIndex = (byte)((monsterFreq.get(0)).getRow());
               String monsterType = NPC.characterDescription(monsterIndex);
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Math.max(50, (int)((monsterFreq.get(0)).getCol()));
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("MONSTER_TYPE", monsterType);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  if(randMission == Mission.CLEAR_MINE)
                     story[i]=story[i].replace("LOCATION_NAME", loc.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(randMission == Mission.CLEAR_MINE)
               {
                  mRow = loc.getRow();
                  mCol = loc.getCol();
               }
               Mission clearMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, "none", mRow, mCol, CLEAR_MINE, -1);
               selected.setMissionInfo(clearMission.getInfo());
               CultimaPanel.missionStack.add(clearMission);
               if(CultimaPanel.missionsGiven[CLEAR_MINE]==0)
                  CultimaPanel.missionsGiven[CLEAR_MINE]++;
               FileManager.saveProgress();   
               response = clearMission.getStartStory();
            }
            else
            {  //no monsters in the location
               response = Utilities.getRandomFrom(NPC.mainMission)+".";
            }
         }
         else if(randMission == SEWER_SLAY)
         {
            Location sewerLoc = LocationBuilder.findSewerForCity(mi);
            if(sewerLoc == null)
            {
               response = Utilities.getRandomFrom(NPC.mainMission)+".";
            }
            else
            {  //add a beast into loc                           
               byte[][]currMap = (CultimaPanel.map.get(sewerLoc.getMapIndex()));
               int r = (int)(Math.random()*currMap.length);
               int c = (int)(Math.random()*currMap[0].length);
            
               ArrayList<Coord>spawnCoords = new ArrayList<Coord>();
               ArrayList<Coord>deepWaterCoords = new ArrayList<Coord>();
               for(int cr = 0; cr<currMap.length; cr++)
               {
                  for(int cc = 0; cc<currMap[0].length; cc++)
                  {
                     String current = CultimaPanel.allTerrain.get(Math.abs(currMap[cr][cc])).getName().toLowerCase();
                     if(LocationBuilder.canMoveTo(currMap, cr, cc) && !TerrainBuilder.onWater(currMap, cr, cc))
                     {
                        spawnCoords.add(new Coord(cr, cc));
                     }
                     else if(current.contains("deep_water"))
                     {
                        deepWaterCoords.add(new Coord(cr, cc));    
                     }
                  }
               }  
            
               if(spawnCoords.size() == 0 && deepWaterCoords.size() == 0)
               {
                  selected.setNumInfo(0);    
                  response = Utilities.getRandomFrom(NPC.mainMission)+".";
               }  
               else
               {
                  Coord spawn = Utilities.getRandomFrom(spawnCoords);
                  byte monsterType = NPC.randomSewerMonsterKing();
                  if(deepWaterCoords.size() > 0 && Math.random() < 0.5)
                  {
                     monsterType = NPC.randomSewerMissionWaterMonster();
                     spawn = Utilities.getRandomFrom(deepWaterCoords);
                  }
                  r = spawn.getRow();
                  c = spawn.getCol();
                  NPCPlayer targNPC = new NPCPlayer(monsterType, r, c, sewerLoc.getMapIndex(), "sewer");
                  if(targNPC.getReputation() >= 0)
                     targNPC.setReputation(-1*Utilities.rollDie(100,1000));
                  String missTarget = NPC.characterDescription(monsterType)+"-head";
                  int goldReward = 250;
                  targNPC.addItem(new Item(missTarget, goldReward));
                  targNPC.setMoveType(NPC.CHASE);
                  CultimaPanel.civilians.get(sewerLoc.getMapIndex()).add(targNPC);
               
                  type = "Slay";
                  if(targNPC!=null && sewerLoc!=null)
                  {
                     String [] temp = missions[randMission];
                     String [] story = new String[3];
                     story[0] = new String(temp[0]);
                     story[1] = new String(temp[1]);
                     story[2] = new String(temp[2]);
                                
                     String locName = Display.provinceName(sewerLoc.getName());
                     for(int i=0; i<story.length; i++)
                     {
                        story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
                     }
                     int startRow = CultimaPanel.player.getWorldRow();
                     int startCol = CultimaPanel.player.getWorldCol();
                     int mRow = -1, mCol = -1;
                     Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(missTarget, goldReward), "large beast", mRow, mCol, SEWER_SLAY, -1);
                     selected.setMissionInfo(getMission.getInfo());
                     CultimaPanel.missionStack.add(getMission);
                     if(CultimaPanel.missionsGiven[SEWER_SLAY]==0)
                        CultimaPanel.missionsGiven[SEWER_SLAY]++;
                     FileManager.saveProgress();
                     response = getMission.getStartStory();
                  }  
               } 
            }
         }
         else if(randMission==KAIJU)
         {
            type = "Save";
            byte monsterIndex = NPC.randomGiantMonsterKing();   
            //***TESTING*** monsterIndex = NPC.EARTHELEMENTAL;
                    
            //pick a spot close to the location we are in
            if(kaijuSpawn != null)
            {
               int monsterRow = kaijuSpawn.getRow();
               int monsterCol = kaijuSpawn.getCol();
                           
               if(Utilities.NPCAt(monsterRow, monsterCol, 0))
                  Utilities.removeNPCat(monsterRow, monsterCol, 0);
               String targetHolder = NPC.characterDescription(monsterIndex);          
               String [] temp = missions[KAIJU];
               String [] story = new String[3];
               for(int i=0; i<story.length; i++)
               {
                  story[i]=(new String(temp[i])).replace("MONSTER_TYPE", ""+targetHolder).replace("PLAYER_NAME", ""+CultimaPanel.player.getShortName());
               }
               NPCPlayer monster = new NPCPlayer(monsterIndex, monsterRow, monsterCol, 0, "world");
               if(monster.getCharIndex() == NPC.TORNADO)
               {  //this turns the regular tornado into an air elemental
                  monster.setNumInfo(1);
                  monster.removeEffect("control");
                  monster.setMoveType(NPC.CHASE);
                  monster.setReputation(-1000);
               }
               int targItemPrice = (monster.getLevel()*5);
               if(targItemPrice > 250)
                  targItemPrice = 250;
               int targetRow = monsterIndex;  //record the monster index for spawning a monster of the same type in case we missed it the first time
               int targetCol = monsterIndex;
               String targetName = NPC.characterDescription(monsterIndex)+"-tooth";
               if(monster.getCharIndex() == NPC.TORNADO)
               {
                  targetName = NPC.characterDescription(monsterIndex)+"-essence";
               }
               Item thisItem = new Item(targetName,targItemPrice);
               monster.addItem(thisItem);
               CultimaPanel.worldMonsters.add(monster);
               CultimaPanel.monsterDist = (int)(Display.findDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), monsterRow, monsterCol));
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               Mission getMission = new Mission(type, story, startRow, startCol, targItemPrice, null, thisLoc.getName(), thisItem, targetHolder, mRow, mCol, KAIJU, -1);
               getMission.setTargetRow(targetRow);
               getMission.setTargetCol(targetCol);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[KAIJU]==0)
                  CultimaPanel.missionsGiven[KAIJU]++;
               FileManager.saveProgress();   
               response = getMission.getStartStory();
            }
            else  
            {  //could not find valid monster spawn - clear the mission
               response = "";
            }
         }
         else if(randMission==BUILD_PIER)
         {
            if(waterSpots.size() >= 5)
            {
               type = "Build";
               String [] temp = missions[BUILD_PIER];
               String [] story = new String[3];
               int targItemPrice = Utilities.rollDie(5, 15);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=(new String(temp[i])).replace("GOLD_REWARD", ""+targItemPrice);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               Mission getMission = new Mission(type, story, startRow, startCol, targItemPrice, null, thisLoc.getName(), null, "none", mRow, mCol, BUILD_PIER, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[BUILD_PIER]==0)
                  CultimaPanel.missionsGiven[BUILD_PIER]++;
               FileManager.saveProgress();    
               response = getMission.getStartStory();
            }
            else  
            {  //could not find valid monster spawn - clear the mission
               response = "";
            }
         }
         else if(randMission==GET_BOAT)
         {
            type = "Boat";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            Item gem = Item.getRandomStone();
            String targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            String locName = Display.provinceName(loc.getName());
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
         //              public Mission(String t, String[]story, int wRow, int wCol, int gold,    Item iw, String loc,    Item targ,  String targHolder, int targR, int targC, byte missionType)
            Mission getMission = new Mission(type, story,        startRow, startCol, goldReward,     gem, locName,           null,           targName,     mRow,     mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();   
            response = getMission.getStartStory();
         }
         else if(randMission==HEAL_WARRIOR)
         {
            int damageDone = selected.getBody()/2 + Utilities.rollDie(1, selected.getBody()/2-1);
            selected.setBody(selected.getBody() - damageDone);
            selected.setAgility(Utilities.rollDie(1,3));
            type = "Heal";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            Item gem = Item.getRandomStone();
            String targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            String locName = Display.provinceName(loc.getName());
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
         //              public Mission(String t, String[]story, int wRow, int wCol, int gold,    Item iw, String loc,    Item targ,  String targHolder, int targR, int targC, byte missionType, int day)
            Mission getMission = new Mission(type, story,        startRow, startCol, goldReward,     gem,   locName,         null,       targName,          mRow,      mCol, (byte)randMission,    -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();   
            response = getMission.getStartStory();
         }
         else if(randMission==MERCY_KILL)
         {
            type = "Mercy";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            String targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            String locName = Display.provinceName(loc.getName());
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, targName, mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();   
            response = getMission.getStartStory();
         }
         else if(randMission == Mission.WEREWOLF_TOWN)
         {  //Purge a city or town of werewolves
            type = "Purge";  
            ArrayList<NPCPlayer> inLocNPCs = null;               
            String []locTypes2 = {"village", "domicile"};
            Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
            inLocNPCs = CultimaPanel.civilians.get(closeCity.getMapIndex());
            if(inLocNPCs.size() >= 1)
            {
               loc = closeCity;
               for(NPCPlayer p: inLocNPCs)      //change all to werewolf
               {
                  if(!NPC.isCivilian(p.getCharIndex()) || p.isNonPlayer())
                     continue;
                  if(!p.getName().contains("~"))
                     p.setName(p.getName()+"~"+p.getCharIndex());
               }
            }
            else
               loc = null;
            if(loc!=null)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               String locName = Display.provinceName(loc.getName());
               int numCivs = LocationBuilder.countCivilians(loc);
               int goldReward = Utilities.rollDie(50,150);             
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("LOCATION_NAME", locName);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = loc.getRow();
               int mCol = loc.getCol();
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, "none", mRow, mCol, WEREWOLF_TOWN, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[WEREWOLF_TOWN]==0)
                  CultimaPanel.missionsGiven[WEREWOLF_TOWN]++;
               FileManager.saveProgress();   
               response = getMission.getStartStory();
            }   
         }
         else if(treasuremap!=null && randMission == Mission.TREASURE_MAP)
         {          
            type = "Treasure";
            String targHolder = Utilities.shortName(selected.getName());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(50,200);
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_AMOUNT", ""+goldReward);
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;            
            String itemName = treasuremap.getName();
            CultimaPanel.player.addItem(itemName);
            if(itemName.contains("treasuremap"))
            {
               Coord treasureLoc = Item.getCoordFromTeasureMap(itemName);
               int treasureRow = treasureLoc.getRow();
               int treasureCol = treasureLoc.getCol();
               mRow = (treasureRow/10)*10;
               mCol = (treasureCol/10)*10;
            }
            //              public Mission(String t,     String[]story, int wRow, int wCol, int gold,    Item iw, String loc,    Item targ,  String targHolder, int targR, int targC, byte missionType)
            Mission treasureMission = new Mission(type,  story,         startRow, startCol, goldReward,  null,    "none",        null,       targHolder,           mRow,       mCol, TREASURE_MAP, -1);
            selected.setMissionInfo(treasureMission.getInfo());
            CultimaPanel.missionStack.add(treasureMission);
            if(CultimaPanel.missionsGiven[TREASURE_MAP]==0)
               CultimaPanel.missionsGiven[TREASURE_MAP]++;
            FileManager.saveProgress();   
            response = treasureMission.getStartStory();
         }   
         else if(randMission == Mission.FIND_WEAPON)
         {  
            type = "Find";
            String locType = "place";
            NPCPlayer targNPC = null;
         //pick a close adventure spot or battlefield
            loc =  TerrainBuilder.closeLocation(CultimaPanel.allAdventure);
            if(loc == null)
            {
               String [] locTypes = {"cave"};
               loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes);
               locType = "cave";
            }
            if(loc == null)
            {
               String [] locTypes = {"mine"};
               loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes);
               locType = "mine";
            }
            if(loc == null)
            {
               String [] locTypes = {"lair"};
               loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes);
               locType = "lair";
            }
            if(loc != null)
            {
               String terrType = loc.getTerrain().getName().toLowerCase();
               if(TerrainBuilder.habitablePlace(terrType))
                  locType =  "town";
               else if(terrType.contains("cave"))
                  locType = "cave";
               else if(terrType.contains("mine"))
                  locType = "mine";
               else if(terrType.contains("lair"))
                  locType = "lair";
               else if(terrType.contains("dungeon"))
                  locType = "dungeon";
               else if(terrType.contains("temple"))
                  locType = "temple";   
            }
            byte monsterType = NPC.TROLL;
            target =  Weapon.getRandomWeapon(); 
            if(Math.random() < 0.5)
            {  //make it one of 5 armed brigands
               int brigType = Utilities.rollDie(1,5);
               if(brigType == 1)
               {
                  monsterType = NPC.BRIGAND_SWORD;
                  target = Weapon.getSword();
                  if(Math.random() < 0.5)
                     target = Weapon.getBlessedSword(); 
               }
               else if(brigType == 2)
               {
                  monsterType = NPC.BRIGAND_SPEAR;
                  int weapType = Utilities.rollDie(1,4);
                  if(weapType==1)
                     target = Weapon.getHalberd(); 
                  else if(weapType==2)  
                     target = Weapon.getIceStaff();
                  else if(weapType==3)
                     target = Weapon.getBrightHalberd();
                  else
                     target = Weapon.getSpear();
               }
               else if(brigType == 3)
               {
                  monsterType = NPC.BRIGAND_DAGGER;
                  int weapType = Utilities.rollDie(1,6);
                  if(weapType==1)
                     target = Weapon.getDagger(); 
                  else if(weapType==2)  
                     target = Weapon.getPoisonDagger();
                  else if(weapType==3)
                     target = Weapon.getSoulDagger();
                  else if(weapType==4)
                     target = Weapon.getBaneDagger();
                  else if(weapType==5)
                     target = Weapon.getFrostDagger();
                  else //if(weapType==6)
                     target = Weapon.getMagmaDagger();
               }
               else if(brigType == 4)
               {
                  monsterType = NPC.BRIGAND_HAMMER;
                  int weapType = Utilities.rollDie(1,4);
                  if(weapType==1)
                     target = Weapon.getHammer(); 
                  else if(weapType==2)  
                     target = Weapon.getSpikedHammer();
                  else if(weapType==3)
                     target = Weapon.getBaneHammer();
                  else
                     target = Weapon.getExoticHammer();
               }
               else if(brigType == 5)
               {
                  monsterType = NPC.BRIGAND_CROSSBOW;
                  target = Weapon.getCrossbow();
               }
            }
            if(Math.random() < 0.5)
               target = Armor.getRandomArmor();
            targNPC = new NPCPlayer(monsterType, 0, 0, 0, locType);  
            if(target instanceof Weapon)
               targNPC.setWeapon((Weapon)(target));  
            else
               targNPC.setArmor((Armor)(target));              
            boolean success = TerrainBuilder.addNPCtoLocation(loc, locType, targNPC);
         
            if(targNPC!=null && loc!=null && success)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Math.max(150, target.getValue()/3);
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(selected.getName());         
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
                  story[i]=story[i].replace("ITEM_NAME", target.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(targNPC.getMapIndex() != selected.getMapIndex())
               {
                  mRow = loc.getRow();
                  mCol = loc.getCol();
               }
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target.getName(),target.getValue()), targName, mRow, mCol, FIND_WEAPON, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[FIND_WEAPON]==0)
                  CultimaPanel.missionsGiven[FIND_WEAPON]++;
               FileManager.saveProgress();   
               response = getMission.getStartStory();
            }   
         } 
      }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(NPC.mainMission)+".";
         selected.setNumInfo(Utilities.rollDie(1,3));
      }
      return response;
   }

   public static String taxmanMission(NPCPlayer selected)
   {
      String response = "";  
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      //***find possible spawn locations for a gang mission
      Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      int mi = CultimaPanel.player.getMapIndex();
      byte [][] currMap = CultimaPanel.map.get(mi);
      ArrayList<Coord>spawnCoords = new ArrayList<Coord>();
      for(int cr = 0; cr<currMap.length; cr++)
         for(int cc = 0; cc<currMap[0].length; cc++)
            if(TerrainBuilder.isGoodCityMonsterSpawn(cr, cc, mi))
               spawnCoords.add(new Coord(cr, cc));
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getMissionType() == HEAL_TAXMAN)
         {
            if(selected.getBody() >= selected.maxHealth()/2)
            {
               selected.setAgility(Utilities.rollDie(15,50));
               selected.setEndChaseDay(CultimaPanel.days + 15);
               selected.setMoveType(NPC.ROAM);
               finishMission(currMission, selected, 10);
               Utilities.standDownGuards();
               Utilities.standDownTaxmen();
            } 
         }
         else if(currMission.getType().contains("Collect") && currMission.getGoldReward() > 0)
         {
            if(CultimaPanel.player.getGold() > currMission.getGoldReward())
            {
               CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
               CultimaPanel.player.pay(currMission.getGoldReward());
               selected.addGold(currMission.getGoldReward());
               selected.setEndChaseDay(CultimaPanel.days + 15);
               selected.setMoveType(NPC.ROAM);
               selected.setHasBeenAttacked(false);
               Utilities.standDownGuards();
               Utilities.standDownTaxmen();
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Find"))
         {
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.player.removeItem(target.getName());
               selected.setEndChaseDay(CultimaPanel.days + 15);
               selected.setMoveType(NPC.ROAM);
               selected.setHasBeenAttacked(false);
               Utilities.standDownGuards();
               Utilities.standDownTaxmen();
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Save") && currMission.getClearLocation()!=null)
         {
            int numGangMembers = LocationBuilder.countGangMembers(CultimaPanel.getLocation(currMission.getClearLocation())); 
            if(numGangMembers==0)
            {
               selected.setEndChaseDay(CultimaPanel.days + 15);
               selected.setMoveType(NPC.ROAM);
               selected.setHasBeenAttacked(false);
               Utilities.standDownGuards();
               Utilities.standDownTaxmen();
               int reputationPoints = 10;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Assassinate") && currMission.getTarget()!=null)
         {
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
               CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
               CultimaPanel.player.removeItem(target.getName());
               if(!target.getName().contains("bounty") && !target.getName().contains("head"))
                  selected.addItem(target);
               selected.setEndChaseDay(CultimaPanel.days + 15);
               selected.setMoveType(NPC.ROAM);
               selected.setHasBeenAttacked(false);
               Utilities.standDownGuards();
               Utilities.standDownTaxmen();
               int reputationPoints = 0;
               finishMission(currMission, selected, reputationPoints);
            }
            else if(CultimaPanel.player.hasItem("bounty"))
            {  //if we just have an item called bounty and there is only one bounty mission, resolve it.
               int count = 0;
               for(Mission m:CultimaPanel.missionStack)
               {
                  if(m.getType().contains("Assassinate"))
                     count++;
               }
               if (count==1)
               {
                  CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
                  if(currMission.getType().contains("Assassinate"))
                     CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
                  CultimaPanel.player.removeItem("bounty");
                  int reputationPoints = 0;
                  Utilities.standDownGuards();
                  Utilities.standDownTaxmen();
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
      
      }  //end seeing if we finished a mission
      if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward()/10;
         Item itemReward = currMission.getItemReward();
         CultimaPanel.player.addGold(goldReward);
         if(itemReward != null)
            CultimaPanel.player.addItem(itemReward.getName());
         selected.setNumInfo(0);    
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else
      { 
         byte randMission = TAX_COLLECTOR;
         boolean hasGrayTiles = TerrainBuilder.hasGrayTiles(thisLoc);
         boolean hauntedMission = false;
         for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
         {  //make sure we don't have more than one mission active where we have to find an item in a haunted house
            Mission m = CultimaPanel.missionStack.get(i);
            if(m.getMissionType()==Mission.HAUNTED_FIND)
            {
               hauntedMission = true;
               break;
            }
         }
         ArrayList<Byte>missionTypes = new ArrayList<Byte>();
         //fill list of missions to pick from with the ones we have not seen yet
         if(CultimaPanel.missionsGiven[HEAL_TAXMAN]==0)
            missionTypes.add(HEAL_TAXMAN);
         if(CultimaPanel.missionsGiven[TAX_COLLECTOR]==0)
            missionTypes.add(TAX_COLLECTOR);
         if(CultimaPanel.missionsGiven[TAX_IMPOSTER]==0)
            missionTypes.add(TAX_IMPOSTER);
         if(CultimaPanel.missionsGiven[TAX_MOB]==0 && spawnCoords.size()>0)
            missionTypes.add(TAX_MOB);
         if(CultimaPanel.missionsGiven[FIND_LEDGER]==0)
            missionTypes.add(FIND_LEDGER);
         if(CultimaPanel.missionsGiven[FIND_LEDGER2]==0 && hasGrayTiles && !hauntedMission)
         {
            missionTypes.add(FIND_LEDGER2);
         }
         if(selected.isWerewolf() && CultimaPanel.missionsGiven[MERCY_KILL]==0)   //werewolf tag
            missionTypes.add(MERCY_KILL);
         if(missionTypes.size()==0)    //we have seen all the mission types before, so pick a random one
         {
            missionTypes.add(HEAL_TAXMAN);
            missionTypes.add(TAX_COLLECTOR);
            missionTypes.add(FIND_LEDGER);
            if(hasGrayTiles && !hauntedMission)
            {
               missionTypes.add(FIND_LEDGER2);
            }
            missionTypes.add(TAX_IMPOSTER);
            if(spawnCoords.size()>0)
               missionTypes.add(TAX_MOB);
            if(selected.isWerewolf())   //werewolf tag
               missionTypes.add(MERCY_KILL);
         }
         randMission = Utilities.getRandomFrom(missionTypes);
         
         //***TESTING*** if(hasGrayTiles) randMission = FIND_LEDGER2;
         
         NPCPlayer targetRival = null;
         String targName = "";
         ArrayList<NPCPlayer> otherNPCs = Utilities.getNPCsInLoc(selected.getMapIndex(), NPC.CHILD, selected.getName());
         if(otherNPCs.size() > 0)
         {
            targetRival = Utilities.getRandomFrom(otherNPCs);
            targName = Utilities.shortName(targetRival.getName());
         }
         if(targetRival!=null && randMission == Mission.TAX_COLLECTOR)
         {
            CultimaPanel.missionTargetNames.add(targName);
            String type = "Collect";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(20,200);
            targetRival.addGold(goldReward);
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_AMOUNT", ""+goldReward);
               story[i]=story[i].replace("NPC_NAME", targName);
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;            
            //         public Mission(String t,     String[]story, int wRow, int wCol, int gold,    Item iw, String loc,    Item targ,  String targHolder, int targR, int targC, byte missionType)
            Mission taxMission = new Mission(type,  story,         startRow, startCol, goldReward,  null,    "none",        null,       targName,           mRow,       mCol, TAX_COLLECTOR, -1);
            selected.setMissionInfo(taxMission.getInfo());
            CultimaPanel.missionStack.add(taxMission);
            if(CultimaPanel.missionsGiven[TAX_COLLECTOR]==0)
               CultimaPanel.missionsGiven[TAX_COLLECTOR]++;
            FileManager.saveProgress();   
            response = taxMission.getStartStory();
         }
         else if(randMission==HEAL_TAXMAN)
         {
            int damageDone = selected.getBody()/2 + Utilities.rollDie(1, selected.getBody()/2-1);
            selected.setBody(selected.getBody() - damageDone);
            selected.setAgility(1);
            String type = "Heal";
            Location loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String locName = Display.provinceName(loc.getName());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
            }
            int goldReward = 0;
            targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
         //              public Mission(String t, String[]story, int wRow, int wCol, int gold,    Item iw, String loc,    Item targ,  String targHolder, int targR, int targC, byte missionType, int day)
            Mission getMission = new Mission(type, story,        startRow, startCol, goldReward,   null,   locName,         null,       targName,          mRow,      mCol, (byte)randMission,    -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();   
            response = getMission.getStartStory();
         }  
         else if(randMission == FIND_LEDGER)
         {
            String [] temp = missions[FIND_LEDGER];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            String targetName = "tax-ledger";
            int targItemPrice = (int)(goldReward);
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            //place ledger somewhere in the map
            Coord bookCoord = TerrainBuilder.addBooktoLocation(CultimaPanel.player.getMapIndex());
            if(bookCoord != null)
            {
               int mRow = -1, mCol = -1;
               int bookRow = bookCoord.getRow();
               int bookCol = bookCoord.getCol();
               //add temp nonPlayer
               NPCPlayer marker = new NPCPlayer(NPC.BEGGER, bookRow, bookCol, CultimaPanel.player.getMapIndex(), "temp", "temp,1,1,1,10", false);
               CultimaPanel.civilians.get(CultimaPanel.player.getMapIndex()).add(marker);   //add a nonPlayer NPC marker so we can be directed to the mission item with the Knowing spell
               Mission getMission = new Mission("Find", story, startRow, startCol, goldReward, null, "none", new Item(targetName, goldReward), "none", mRow, mCol, FIND_LEDGER, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[FIND_LEDGER]==0)
                  CultimaPanel.missionsGiven[FIND_LEDGER]++;
               FileManager.saveProgress();   
               response = getMission.getStartStory();
            }
            else
               response = ""; //no book placed, so clear out mission 
         } 
         else if(randMission==FIND_LEDGER2)
         {
            String locType = CultimaPanel.player.getLocationType();
            //place a songbook in a haunted house
            ArrayList<Coord> spawnPoints = new ArrayList<Coord>();
            int mapIndex = selected.getMapIndex();
            for(int r = 1; r < currMap.length -1; r++)
            {
               for(int c = 1; c < currMap[r].length - 1; c++)
               {
                  String currentTile = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(currentTile.contains("floor") && Utilities.nextToAHauntedHouse(currMap, r, c, 1) && !LocationBuilder.nextToADoor(currMap, r, c))
                  {
                     spawnPoints.add(new Coord(r,c));
                  }
               }
            }
            if(spawnPoints.size() == 0)
            {
               response = ""; //no book placed, so clear out mission 
            }
            Coord bookSpawn = spawnPoints.remove((int)(Math.random()*spawnPoints.size()));
            currMap[bookSpawn.getRow()][bookSpawn.getCol()] = TerrainBuilder.getTerrainWithName("ITM_D_$Book").getValue();
            int numPhantoms = Utilities.rollDie(1, spawnPoints.size()/2);
            for(int i = 0; i < numPhantoms && spawnPoints.size() > 0; i++)
            {
               Coord phantomSpawn = spawnPoints.remove((int)(Math.random()*spawnPoints.size()));
               if(!Utilities.NPCAt(phantomSpawn.getRow(), phantomSpawn.getCol(), mapIndex))
               {
                  NPCPlayer toAdd = new NPCPlayer(NPC.PHANTOM, phantomSpawn.getRow(), phantomSpawn.getCol(), mapIndex, phantomSpawn.getRow(), phantomSpawn.getCol(), locType);
                  CultimaPanel.civilians.get(mapIndex).add(toAdd);
               } 
            }
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            String locName = Display.provinceName(thisLoc.getName());
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = thisLoc.getRow(), mCol = thisLoc.getCol();
            int goldReward = 0;
            Mission getMission = new Mission("Find", story, startRow, startCol, goldReward, null, locName, new Item("tax-ledger",0), "none", mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         } 
         else if(randMission == TAX_MOB && spawnCoords.size() > 0)
         { 
            String targetHolder = Utilities.nameGenerator("name");
            String type = "Save";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("NPC_NAME", ""+targetHolder);
               story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            selected.setMoveType(NPC.STILL);
            int mRow = selected.getRow();
            int mCol = selected.getCol();
            //***add bandits to the location
            String locType = CultimaPanel.player.getLocationType();
            int numBandits = Utilities.rollDie(3,7);
            byte indexType = NPC.getRandGangMember();
            for(int i=0; i<numBandits; i++)
            { 
               Coord spawn = Utilities.getRandomFrom(spawnCoords);
               int r = spawn.getRow();
               int c = spawn.getCol();
               NPCPlayer bandit = new NPCPlayer(indexType, r, c, mi, r, c, locType);
               bandit.setName("Brute "+targetHolder);
               if(indexType==NPC.BRIGANDKING)
                  bandit.setWeapon(Weapon.getShortSwords());
               else if(indexType==NPC.BOWASSASSIN)
                  bandit.setWeapon(Weapon.getLongbow());
               else if(indexType==NPC.VIPERASSASSIN)
                  bandit.setWeapon(Weapon.getDagger());
               else if(indexType==NPC.ENFORCER)
                  bandit.setWeapon(Weapon.getCrossbow());
               bandit.setMoveType(NPC.CHASE);      
               CultimaPanel.worldMonsters.add(bandit);
            }
            //***                   //Mission(String t, String[]story, int wRow, int wCol, int gold,   Item iw,  String loc,        Item targ, String targHolder, int targR, int targC, byte missionType)
            Mission saveMission = new Mission(type,     story,         startRow, startCol, goldReward, null,     thisLoc.getName(), null,      targetHolder,      mRow,      mCol, TAX_MOB, -1);
            selected.setMissionInfo(saveMission.getInfo());
            CultimaPanel.missionStack.add(saveMission);
            if(CultimaPanel.missionsGiven[TAX_MOB]==0)
               CultimaPanel.missionsGiven[TAX_MOB]++;
            FileManager.saveProgress();   
            response = saveMission.getStartStory();
         }
         else if(randMission == Mission.TAX_IMPOSTER)
         {
            ArrayList<NPCPlayer> inLocNPCs = Utilities.getNPCsInLoc(selected.getMapIndex(), NPC.CHILD, selected.getName());
            String type = "Assassinate";
            NPCPlayer targNPC = null;
               
            ArrayList<NPCPlayer> targets = new ArrayList<NPCPlayer>();
            for(NPCPlayer pl: inLocNPCs)
               if(!pl.getName().equals(selected.getName()) && pl.getCharIndex()==NPC.TAXMAN)
                  targets.add(pl);
            if(targets.size()==0)
               for(NPCPlayer pl: inLocNPCs)
                  if(!pl.getName().equals(selected.getName()) && pl.getCharIndex()==NPC.JESTER)
                     targets.add(pl);
            if(targets.size()==0)
               targNPC = null;
            else
               targNPC = Utilities.getRandomFrom(targets);
         
            if(targNPC!=null && thisLoc!=null)
            {
               if(targNPC.getCharIndex()==NPC.JESTER)
                  targNPC.setCharIndex(NPC.TAXMAN);
               if(targNPC.getReputation() > 0)
                  targNPC.setReputation(targNPC.getReputation()*-1);   
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
            //higher reward for targets further away
               int goldReward = 0;
               targName = Utilities.shortName(targNPC.getName());  
               CultimaPanel.missionTargetNames.add(targName);   
               String target = targName+"-bounty";
               int targItemPrice = (int)(goldReward);
               targNPC.addItem(new Item(target, targItemPrice));
               targNPC.setGold(targNPC.getGold()*5);
               String locName = Display.provinceName(thisLoc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target, goldReward), targName, mRow, mCol, (byte)randMission, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               response = getMission.getStartStory();
               FileManager.saveProgress();
            }
            else
               response = "";
         }
         else if(randMission==MERCY_KILL)
         {
            String type = "Mercy";
            Location loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            String locName = Display.provinceName(loc.getName());
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, targName, mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            response = getMission.getStartStory();
            FileManager.saveProgress();
         }
         else
            response = "";
      }
      if(response.length() == 0)
      {  //no mission
         response = Utilities.getRandomFrom(NPC.mainMission)+".";
         selected.setNumInfo(0);
      }
      return response;
   }

   public static String cityMission(NPCPlayer selected)
   {
      String [] lostMission = {"Thy fire raged for too long. Much was lost.", "Too much damage was done by thy fire.", "'Tis a shame. Much was lost in that terrible fire.", "I know thee tried, but much was destroyed by thy fire."};
      String [] lostBattle = {"You show my child a defeat? You have taught them how to fail", "I shall not pay for you to teach my child how to retreat", "You have only shown my child how to abandon the fight! You get no pay from me"};
      String [] lostChild = {"Where be my child? You monster", "I shall not speak to thee! My child is lost", "I trusted thee with my own kin!"};
      String [] noBattle = {"Excuse me. I have no mission. I just wanted to greet you personally", "<...seek the sacred number...>", "You have a mission: look for missions from others", "Did thou say mission? There must be a vex in thy matrix"};
      String response = "";
      boolean closeLairWithPrisoners = false;
      boolean hasCoffins = TerrainBuilder.hasCoffin();
      String selName = selected.getName().toLowerCase();
      boolean isButcherOrBaker = (selName.contains("butcher") || selName.contains("baker"));
      boolean isButcherOrBarkeep = (isButcherOrBaker || selName.contains("barkeep"));
      Location loc = TerrainBuilder.closeLairWithPrisoners(CultimaPanel.allLocations, 50);
      Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      ArrayList<NPCPlayer> inLocNPCs = Utilities.getNPCsInLoc(selected.getMapIndex(), NPC.CHILD, selected.getName());
      //***find possible spawn locations for a gang mission
      int mi = CultimaPanel.player.getMapIndex();
      boolean hasSewer = LocationBuilder.doesCityHaveSewer(mi);
      byte [][] currMap = CultimaPanel.map.get(mi);
      byte[][] worldMap = CultimaPanel.map.get(0);
      ArrayList<Coord>spawnCoords = new ArrayList<Coord>();
      for(int cr = 0; cr<currMap.length; cr++)
         for(int cc = 0; cc<currMap[0].length; cc++)
            if(TerrainBuilder.isGoodCityMonsterSpawn(cr, cc, mi))
               spawnCoords.add(new Coord(cr, cc));
      ArrayList<Coord>fireSpawnCoords = new ArrayList<Coord>();
      for(int cr = 0; cr<currMap.length; cr++)
         for(int cc = 0; cc<currMap[0].length; cc++)
            if(LocationBuilder.isCombustableStructure(currMap, cr,cc))
               fireSpawnCoords.add(new Coord(cr, cc));
      ArrayList<NPCPlayer> downtrodden = new ArrayList<NPCPlayer>();
      for(NPCPlayer p:inLocNPCs)
      {
         if(p.getCharIndex()==NPC.BEGGER && !p.getName().startsWith("Cultist"))
         {
            downtrodden.add(p);
         }
      }
      int numBeggers = downtrodden.size();
     //***
      String locType = "dungeon";
      if(loc == null)
      {
         loc = TerrainBuilder.closeLocation(CultimaPanel.allAdventure);
         if(loc == null)
         {
            String [] locTypes = {"cave"};
            loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes);
            locType = "cave";
         }
         if(loc == null)
         {
            String [] locTypes = {"mine"};
            loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes);
            locType = "mine";
         }
         if(loc == null)
         {
            String [] locTypes = {"lair"};
            loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes);
            locType = "lair";
         }
      }
      else
         closeLairWithPrisoners = true;
   
      if(loc != null)
      {
         String terrType = loc.getTerrain().getName().toLowerCase();
         if(TerrainBuilder.habitablePlace(terrType))
            locType =  "town";
         else if(terrType.contains("cave"))
            locType = "cave";
         else if(terrType.contains("mine"))
            locType = "mine";
         else if(terrType.contains("lair"))
            locType = "lair";
         else if(terrType.contains("dungeon"))
            locType = "dungeon";
         else if(terrType.contains("temple"))
            locType = "temple";   
      }  
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getMissionType() == SEWER_LOST)
         {
            boolean withNpc = (!CultimaPanel.player.getNpcName().equals("none"));
            Location currLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if (withNpc && LocationBuilder.countKids(currLoc)==0)
            {
               NPCPlayer escortee = Utilities.getNpc();
               if(escortee != null)
               {
                  Utilities.moveWorldNpcToCivilians(escortee.getRow(), escortee.getCol(), escortee.getMapIndex());
               }
               CultimaPanel.player.setNpcBasicInfo("none");
               selected.setNumInfo(3);
               finishMission(currMission, selected, 10);
            }
         }
         else if(currMission.getMissionType() == SEWER_RATS)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               ArrayList<Coord> monsterFreq = missLoc.getMonsterFreq();
               if(monsterFreq==null || monsterFreq.size()==0)  //we cleared out the monsters here
               {
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getMissionType() == COLLECT_FLORETS1 && CultimaPanel.player.fiveFlowersOfEach())
         {
            CultimaPanel.player.removeAllFlowers((byte)5);
            finishMission(currMission, selected, 5);
         }
         else if(currMission.getMissionType() == FEED_POOR && CultimaPanel.player.getRations() >= 15)
         {
            CultimaPanel.player.setRations((byte)(CultimaPanel.player.getRations() - 15));
            for(int i=0; i<downtrodden.size(); i++)
            {
               NPCPlayer p = downtrodden.get(i);
               p.clearMissionInfo();
               int restoreType = (int)(Math.random()*7);
               if(restoreType<=2)
               {
                  p.setCharIndex(NPC.MAN);
                  p.setNumInfo(3);                         //a man with a numInfo of 3 is the flag that they will map for free
               }
               else if(restoreType<=5)
               {
                  p.setCharIndex(NPC.WOMAN);
                  p.setNumInfo(3);                         //a woman with a numInfo of 3 is the flag that they will map for free
               }
               else //if restoreType==6
               {
                  p.setCharIndex(NPC.WISE);
                  p.setNumInfo(3);                         //a wise-man with a numInfo of 3 is the flag that they will teach us a spell
               }
            }
         
            finishMission(currMission, selected, 10);
         }
         else if(currMission.getMissionType() == CURE_POISONED)
         {
            if(!selected.hasEffect("poison"))
            {
               finishMission(currMission, selected, 5);
            }
         }
         else if(currMission.getMissionType() == RAIN_MAKER1 || currMission.getMissionType() == RAIN_MAKER2)
         {
            if(CultimaPanel.weather > 0 && currMission.day <= CultimaPanel.days)
            {
               finishMission(currMission, selected, 5);
            }
         }
         else if(currMission.getMissionType() == FIX_PATH)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if (LocationBuilder.countSwampTiles(missLoc, 12)==0)
            {
               finishMission(currMission, selected, 5);
            }
         }
         else if(currMission.getType().toLowerCase().contains("clear") && currMission.getMissionType() == WEREWOLFTRAP)
         {
            Location campLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            int numDoors = LocationBuilder.countLockedCellDoors(campLoc);
            if (numDoors == 0)
            {
               CultimaPanel.civilians.get(campLoc.getMapIndex()).clear();
               CultimaPanel.furniture.get(campLoc.getMapIndex()).clear();
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getMissionType() == CULT_RESCUE)
         {
            Location currLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            int numCultists = LocationBuilder.countCultists(currLoc); 
            if(numCultists==0)
            {
               finishMission(currMission, selected, 5);
            }
         }
         else if(currMission.getType().contains("Escort"))
         {
            if(currMission.getMissionType() == ESCORT_ADULT)
            {
               boolean atLocation = (CultimaPanel.player.getWorldRow() == currMission.targetRow && CultimaPanel.player.getWorldCol() == currMission.targetCol);
               boolean withNpc = (!CultimaPanel.player.getNpcName().equals("none"));
               if(atLocation && withNpc)
               {
                  selected.setNumInfo(3);
                  Utilities.moveWorldNpcToCivilians(selected.getRow(), selected.getCol(), selected.getMapIndex());
                  CultimaPanel.player.setNpcBasicInfo("none");
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
            else //if (currentMission.getType() == ESCORT_CHILD)
            {
               Location battleLoc = CultimaPanel.getLocation(currMission.getClearLocation());
               if(battleLoc!= null)
               {
                  Teleporter tele = battleLoc.getTeleporter();
                  boolean withNpc = (!CultimaPanel.player.getNpcName().equals("none"));
               //mission is done if we have visited the location and there are still monsters left
                  if (tele.toRow()!=-1 && tele.toCol()!=-1)
                  {
                     CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
                     currMission.setState(Mission.FINISHED);
                     NPCPlayer escortee = Utilities.getNpc();
                     selected.setNumInfo(0);
                     if(escortee != null)
                     {
                        Utilities.moveWorldNpcToCivilians(escortee.getRow(), escortee.getCol(), escortee.getMapIndex());
                     }
                     CultimaPanel.player.setNpcBasicInfo("none");
                     if(currMission.getFailed() == false && withNpc)
                     {
                        Player.stats[Player.MISSIONS_COMPLETED]++;
                        if(Player.stats[Player.MISSIONS_COMPLETED] >= 50)
                           Achievement.earnAchievement(Achievement.TASK_MASTER);
                        selected.setReputation(CultimaPanel.player.getReputationRaw());   //you completed a mission - make them allignment compatible with you
                        selected.setNumInfo(3);
                        CultimaPanel.player.addReputation(10);
                     }
                     else
                     {
                        selected.setMissionInfo("none");
                        if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
                           CultimaPanel.missionStack.remove(currMissionIndex);
                        if(withNpc)
                        {
                           return Utilities.getRandomFrom(lostBattle);
                        }
                        else
                        {
                           CultimaPanel.player.addReputation(-5);
                           selected.setMoveType(NPC.CHASE);
                           return Utilities.getRandomFrom(lostChild);
                        }
                     }
                     FileManager.saveProgress();
                  }
               }
            }
         }
         else if(currMission.getType().contains("Extinguish") && currMission.getClearLocation()!=null)
         {
            int endDay = currMission.getTargetRow();
            int endTime = currMission.getTargetCol();
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               int numFires = LocationBuilder.countFires(missLoc);
               if(numFires==0)      
               {
                  if(CultimaPanel.days==endDay && CultimaPanel.time <= endTime)
                  {
                     int reputationPoints = 10;
                     finishMission(currMission, selected, reputationPoints);
                  }
                  else  //we didn't put out the fire in time
                  {
                     selected.setNumInfo(0);  
                     selected.setMissionInfo("none");
                     if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
                        CultimaPanel.missionStack.remove(currMissionIndex);
                     FileManager.saveProgress();   
                     return Utilities.getRandomFrom(lostMission);
                  }
               }
            }
         }
         else if(currMission.getType().contains("Burn") && currMission.getClearLocation().length()>0)
         {   //check to see if break shoppe sign mission is finished   
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if (!hasCoffins)
            {
               int reputationPoints = -5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Save") && currMission.getClearLocation()!=null)
         {
            int numGangMembers = LocationBuilder.countGangMembers(CultimaPanel.getLocation(currMission.getClearLocation())); 
            if(numGangMembers==0)
            {
               int reputationPoints = 10;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getMissionType()==FIND_LUMBER1 || currMission.getMissionType()==FIND_LUMBER2 || currMission.getMissionType()==FIND_LUMBER3)
         {   //check to see if FIND_LUMBER mission is finished   
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.player.removeItem(target.getName());
               selected.addItem(target);
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }//end of checking FIND_LUMBER mission finish
         else if(currMission.getType().contains("Harvest") && currMission.getTarget()!=null)
         {   //check to see if harvest egg mission is finished   
            Item target = currMission.getTarget();
            String targetName = target.getName().toLowerCase();
            if(targetName.contains("3-serpent-eggs") && CultimaPanel.player.numSerpentEggs()>=3)
            {
               for(int i=0; i<3; i++)
               {
                  CultimaPanel.player.removeItem("serpent-egg");
                  selected.addItem(Item.getSerpentEgg());
               }
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }            
         }
         else if(currMission.getType().contains("Give") && !CultimaPanel.player.hasItem(currMission.getTarget().getName()) && CultimaPanel.player.hasItem("receipt"))
         {
            int reputationPoints = 5;
            finishMission(currMission, selected, reputationPoints);
            CultimaPanel.player.removeItem("receipt");
         }
         else if(currMission.getType().contains("Frighten"))
         {
            boolean removed = true;
            for(NPCPlayer p:CultimaPanel.civilians.get(selected.getMapIndex()))
            {
               String pName = Utilities.shortName(p.getName());
               String targName = Utilities.shortName(currMission.getTargetHolder());
               if(pName.equalsIgnoreCase(targName))
               {
                  removed = false;
                  break;
               }
            }
            if(removed)
            {
               CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
               int reputationPoints = -5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if((currMission.getType().contains("Slay") || currMission.getType().contains("Assassinate") || currMission.getType().contains("Find") || currMission.getType().contains("Swine")) && (currMission.getTarget()!=null))
         {
            Item target = currMission.getTarget();
            String targetName = target.getName();
            boolean vampMiss = (currMission.getStartStory().toLowerCase().contains("vampyre")); 
            boolean hasCoffin = true;
            boolean hasHead = (CultimaPanel.player.hasItem(targetName));
            if(vampMiss)  //make sure we also burned the coffin out of the area
            {
               Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
               hasCoffin = TerrainBuilder.hasCoffin(missLoc);
            } 
            if((vampMiss && !hasCoffin && hasHead) || (!vampMiss && hasHead))
            {
               CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
               if(currMission.getType().contains("Assassinate"))
               {
                  CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
               }
               CultimaPanel.player.removeItem(target.getName());
               if(Item.itemIsStone(targetName))
               {
                  selected.addItem(target);
               }
               int reputationPoints = 5;
               if(currMission.getType().contains("Assassinate"))
               {
                  reputationPoints = -10;
               }
               finishMission(currMission, selected, reputationPoints);
            }
            else if(currMission.getType().contains("Assassinate") && CultimaPanel.player.hasItem("bounty"))
            {  //if we just have an item called bounty and there is only one bounty mission, resolve it.
               int count = 0;
               for(Mission m:CultimaPanel.missionStack)
               {
                  if(m.getType().contains("Assassinate"))
                     count++;
               }
               if (count==1)
               {
                  CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
                  if(currMission.getType().contains("Assassinate"))
                     CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
                  CultimaPanel.player.removeItem("bounty");
                  int reputationPoints = -10;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Rescue") && currMission.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               if(LocationBuilder.countPrisoners(missLoc)==0)      //we freed all the prisoners
               {
                  int reputationPoints = 10;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Remove"))        //help downtrodden to be upright citizens
         {
            if(numBeggers==0)
            {
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Train"))
         {
            if(!CultimaPanel.player.getDogName().equals("none"))
            {
               NPCPlayer dog = Utilities.getDog();
               dog.setMoveTypeTemp(NPC.ROAM);
               dog.setHasMet(false);
               CultimaPanel.player.setDogBasicInfo("none");
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
      }  //end seeing if we finished a mission
      if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
         Item itemReward = currMission.getItemReward();
         CultimaPanel.player.addGold(goldReward);
         if(itemReward != null)
            CultimaPanel.player.addItem(itemReward.getName());
         selected.setNumInfo(3);    //setting a man, woman or wise value to 3 will make it so they will map for you for free, or teach you a spell as thanks
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else  //start a new mission
      {
         String currLoc = selected.getLocationType().toLowerCase();
         boolean isCity = (currLoc.contains("city") || currLoc.contains("port") || currLoc.contains("fortress"));
         boolean isCityOrVillage = (isCity || currLoc.contains("village"));
         boolean inCapital = Utilities.isCapitalCity(selected.getMapIndex());
         int numSwampTiles = LocationBuilder.countSwampTiles(thisLoc, 12);
      //CITY_REVENGE, FIND_PERSON, MESSAGE, CLEAR_BEGGERS, TRAIN_DOG, CLEAR_BANDITS, FIND_SERPENT_EGGS, FIRE_FIGHTER, FIND_GEM, FIND_SCALES
         ArrayList<Byte> missionTypes = new ArrayList<Byte>();    
         byte randMission = MESSAGE; 
         if(hasSewer && CultimaPanel.missionsGiven[SEWER_LOST]==0 && selected.getMaritalStatus()=='M' && CultimaPanel.player.getNpcBasicInfo().equals("none"))
            missionTypes.add(SEWER_LOST);
         if(hasSewer && CultimaPanel.missionsGiven[SEWER_RATS]==0 && isButcherOrBarkeep)
            missionTypes.add(SEWER_RATS);
         if(CultimaPanel.missionsGiven[DELIVER_MEAT]==0 && isButcherOrBaker && !CultimaPanel.player.hasItem("parcel") && !CultimaPanel.player.hasItem("receipt"))
            missionTypes.add(DELIVER_MEAT);   
         if(numBeggers > 0 && CultimaPanel.missionsGiven[FEED_POOR]==0 && Display.isWinter())
            missionTypes.add(FEED_POOR);
         if(CultimaPanel.missionsGiven[COLLECT_FLORETS1]==0 && isCityOrVillage && (Display.isSpring() || Display.isSummer()) && !selected.isShopkeep()) 
            missionTypes.add(COLLECT_FLORETS1);
         if(CultimaPanel.missionsGiven[RAIN_MAKER1]==0 && isCityOrVillage && (Display.isSpring() || Display.isSummer()) && !selected.isShopkeep() && CultimaPanel.weather == 0) 
            missionTypes.add(RAIN_MAKER1);
         if(CultimaPanel.missionsGiven[RAIN_MAKER2]==0 && isCityOrVillage && (Display.isSpring() || Display.isSummer()) && selected.getMaritalStatus()=='N' && CultimaPanel.weather == 0) 
            missionTypes.add(RAIN_MAKER2);
         if(selected.isShopkeep() && numSwampTiles == 0 && CultimaPanel.missionsGiven[FIX_PATH]==0 && isCity)
            missionTypes.add(FIX_PATH);
         if(CultimaPanel.missionsGiven[CULT_RESCUE]==0 && !inCapital && isCity && selected.getMaritalStatus()=='M') 
            missionTypes.add(CULT_RESCUE);
         if(CultimaPanel.missionsGiven[WEREWOLFTRAP]==0 && selected.getMaritalStatus()=='M')
            missionTypes.add(WEREWOLFTRAP);
         if(CultimaPanel.missionsGiven[ESCORT_ADULT]==0 && !selected.isShopkeep() && CultimaPanel.player.getNpcBasicInfo().equals("none"))
            missionTypes.add(ESCORT_ADULT);
         if(CultimaPanel.missionsGiven[ESCORT_KID]==0 && !selected.isShopkeep() && CultimaPanel.player.getNpcBasicInfo().equals("none") && (selected.getMaritalStatus()=='M' || selected.getMaritalStatus()=='W'))
            missionTypes.add(ESCORT_KID);   
         if(CultimaPanel.missionsGiven[BEAT_SWINE_PLAYER]==0 && inLocNPCs.size()>1)
            missionTypes.add(BEAT_SWINE_PLAYER);
         if(CultimaPanel.missionsGiven[BURN_COFFINS]==0 && hasCoffins)
            missionTypes.add(BURN_COFFINS);
         if(CultimaPanel.missionsGiven[LOVE_TRIANGLE]==0 && inLocNPCs.size()>1 && selected.getMaritalStatus()=='M')
            missionTypes.add(LOVE_TRIANGLE);
         if(CultimaPanel.missionsGiven[CURE_POISONED]==0)
            missionTypes.add(CURE_POISONED);
         if(CultimaPanel.missionsGiven[MESSAGE]==0)
            missionTypes.add(MESSAGE);
         if(CultimaPanel.missionsGiven[CITY_REVENGE]==0)
            missionTypes.add(CITY_REVENGE);
         if(CultimaPanel.missionsGiven[FIND_PERSON]==0 && loc != null && selected.getMaritalStatus()=='M')
            missionTypes.add(FIND_PERSON);
         if(CultimaPanel.missionsGiven[SLAY_VAMPIRE]==0 && loc != null)
            missionTypes.add(SLAY_VAMPIRE);
         if(CultimaPanel.missionsGiven[CLEAR_BEGGERS]==0 && numBeggers > 0 && selected.isShopkeep() && (selected.getCharIndex()==NPC.MAN || selected.getCharIndex()==NPC.WOMAN))
            missionTypes.add(CLEAR_BEGGERS); 
         if(CultimaPanel.missionsGiven[FIND_SERPENT_EGGS]==0 && selected.isShopkeep() && selected.getName().toLowerCase().contains("mage"))
         {
            missionTypes.add(FIND_SERPENT_EGGS);
            missionTypes.add(FIND_LUMBER3);     
         }
         if(CultimaPanel.missionsGiven[FIND_LUMBER1]==0 && selected.isShopkeep() && selected.getName().toLowerCase().contains("ironsmith"))
            missionTypes.add(FIND_LUMBER1);
         if(CultimaPanel.missionsGiven[FIND_LUMBER2]==0 && selected.isShopkeep() && selected.getName().toLowerCase().contains("barkeep"))
            missionTypes.add(FIND_LUMBER2);  
         if(CultimaPanel.missionsGiven[FIND_LUMBER3]==0 && selected.isShopkeep() && selected.getName().toLowerCase().contains("butcher"))
            missionTypes.add(FIND_LUMBER3);     
         if(CultimaPanel.missionsGiven[TRAIN_DOG]==0 && selected.isShopkeep() && (selected.getCharIndex()==NPC.MAN || selected.getCharIndex()==NPC.WOMAN))
            missionTypes.add(TRAIN_DOG);
         if(spawnCoords.size() > 0 && isCityOrVillage && CultimaPanel.missionsGiven[CLEAR_BANDITS]==0 && selected.isShopkeep() && (selected.getCharIndex()==NPC.MAN || selected.getCharIndex()==NPC.WOMAN))
            missionTypes.add(CLEAR_BANDITS);
         if(CultimaPanel.missionsGiven[FIND_GEM]==0 && selected.isShopkeep() && selected.getName().toLowerCase().contains("ironsmith"))
            missionTypes.add(FIND_GEM);
         if(CultimaPanel.missionsGiven[FIND_SCALES]==0 && selected.isShopkeep() && selected.getName().toLowerCase().contains("ironsmith"))
            missionTypes.add(FIND_SCALES);
         if(CultimaPanel.missionsGiven[FIRE_FIGHTER]==0 && CultimaPanel.time >= 0 && CultimaPanel.time <= 18 && CultimaPanel.weather == 0)
            missionTypes.add(FIRE_FIGHTER);
         if(CultimaPanel.missionsGiven[FIND_ELK_PELT]==0 && selected.getLocationType().toLowerCase().contains("village"))
            missionTypes.add(FIND_ELK_PELT);
         if(selected.isWerewolf() && CultimaPanel.missionsGiven[MERCY_KILL]==0)   //werewolf tag
            missionTypes.add(MERCY_KILL);
         if(missionTypes.size()==0)
         {
            if(hasSewer && isButcherOrBarkeep)
            {
               missionTypes.add(SEWER_RATS);
            }
            if(isButcherOrBaker && !CultimaPanel.player.hasItem("parcel") && !CultimaPanel.player.hasItem("receipt"))
            {
               missionTypes.add(DELIVER_MEAT);
            }
            if(hasSewer && selected.getMaritalStatus()=='M' && CultimaPanel.player.getNpcBasicInfo().equals("none"))
            {
               missionTypes.add(SEWER_LOST);
            }
            if(numBeggers > 0 && Display.isWinter())
            {
               missionTypes.add(FEED_POOR);
            }
            if(isCityOrVillage && (Display.isSpring() || Display.isSummer()) && !selected.isShopkeep() && CultimaPanel.weather == 0) 
            {
               missionTypes.add(COLLECT_FLORETS1);
               missionTypes.add(RAIN_MAKER1);
            }
            if(isCityOrVillage && (Display.isSpring() || Display.isSummer()) && selected.getMaritalStatus()=='N' && CultimaPanel.weather == 0) 
            {
               missionTypes.add(RAIN_MAKER2);
            }
            if(selected.isShopkeep() && numSwampTiles == 0 && isCity)
            {
               missionTypes.add(FIX_PATH);
            }
            if(selected.getMaritalStatus()=='M')
            {
               missionTypes.add(WEREWOLFTRAP);
            }
            if(CultimaPanel.missionsGiven[CULT_RESCUE]==0 && !inCapital && isCity && selected.getMaritalStatus()=='M') 
            {
               missionTypes.add(CULT_RESCUE);
            }
            if(inLocNPCs.size()>1)
            {
               if(selected.getMaritalStatus()=='M')
               {
                  missionTypes.add(LOVE_TRIANGLE);
               }
               missionTypes.add(BEAT_SWINE_PLAYER);
            }
            if(hasCoffins)
            {
               missionTypes.add(BURN_COFFINS);
            }
            missionTypes.add(CURE_POISONED);
            missionTypes.add(MESSAGE);
            missionTypes.add(CITY_REVENGE);
            if(!selected.isShopkeep() && CultimaPanel.player.getNpcBasicInfo().equals("none"))
            {
               missionTypes.add(ESCORT_ADULT);
               if(selected.getMaritalStatus()=='M' || selected.getMaritalStatus()=='W')
               {
                  missionTypes.add(ESCORT_KID);
               }
            }
            if(loc != null)
            {
               if(selected.getMaritalStatus()=='M')
               {
                  missionTypes.add(FIND_PERSON);
               }
               missionTypes.add(SLAY_VAMPIRE);
            }
            if(numBeggers > 0 && selected.isShopkeep() && (selected.getCharIndex()==NPC.MAN || selected.getCharIndex()==NPC.WOMAN))
               missionTypes.add(CLEAR_BEGGERS);     
            if(selected.isShopkeep() && (selected.getCharIndex()==NPC.MAN || selected.getCharIndex()==NPC.WOMAN))
               missionTypes.add(TRAIN_DOG);
            if(spawnCoords.size() > 0  && isCityOrVillage && selected.isShopkeep() && (selected.getCharIndex()==NPC.MAN || selected.getCharIndex()==NPC.WOMAN))
               missionTypes.add(CLEAR_BANDITS);  
            if(selected.isShopkeep() && selected.getName().toLowerCase().contains("mage"))
            {
               missionTypes.add(FIND_SERPENT_EGGS); 
               missionTypes.add(FIND_LUMBER3);
            }
            if(selected.isShopkeep() && selected.getName().toLowerCase().contains("ironsmith"))
               missionTypes.add(FIND_LUMBER1);
            if(selected.isShopkeep() && selected.getName().toLowerCase().contains("barkeep"))
               missionTypes.add(FIND_LUMBER2);  
            if(selected.isShopkeep() && selected.getName().toLowerCase().contains("butcher"))
               missionTypes.add(FIND_LUMBER3);     
            if(CultimaPanel.time >= 0 && CultimaPanel.time <= 18 && CultimaPanel.weather == 0)
               missionTypes.add(FIRE_FIGHTER);
            if(selected.isShopkeep() && selected.getName().toLowerCase().contains("ironsmith"))
               missionTypes.add(FIND_GEM);
            if(selected.isShopkeep() && selected.getName().toLowerCase().contains("ironsmith"))
               missionTypes.add(FIND_SCALES);
            if(selected.getLocationType().toLowerCase().contains("village"))
               missionTypes.add(FIND_ELK_PELT);
            if(selected.isWerewolf())   //werewolf tag
               missionTypes.add(MERCY_KILL);
         }
         randMission = Utilities.getRandomFrom(missionTypes);
            
         //***TESTING***  randMission = Mission.ESCORT_ADULT;    
                         
         if(randMission == FIND_PERSON && !closeLairWithPrisoners)
         {  //add a BEGGER into loc
            boolean success = TerrainBuilder.addPrisoner(loc, locType);
            if(!success)
            {
               missionTypes.clear();
               missionTypes.add(CURE_POISONED);
               missionTypes.add(MESSAGE);
               missionTypes.add(CITY_REVENGE);
               randMission = Utilities.getRandomFrom(missionTypes);
            }
         }
         if(randMission==SEWER_LOST)
         {
            Location sewerLoc = LocationBuilder.findSewerForCity(mi);
            if(sewerLoc == null)
            {
               response = "";
            }
            else
            {
               boolean success = TerrainBuilder.addLostNpc(NPC.CHILD, sewerLoc, "sewer");
               if(!success)
               {
                  response = "";
               }
               else
               {
                  type = "Rescue";
                  String [] temp = missions[randMission];
                  String [] story = new String[3];
                  story[0] = new String(temp[0]);
                  story[1] = new String(temp[1]);
                  story[2] = new String(temp[2]);
                  int goldReward = 0;
                  for(int i=0; i<story.length; i++)
                  {
                     story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
                     story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.complimentAdjective));
                  }
                  int startRow = CultimaPanel.player.getWorldRow();
                  int startCol = CultimaPanel.player.getWorldCol();
                  int mRow = -1, mCol = -1;
                  TerrainBuilder.markMapPath(loc.getName());      
                  Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, sewerLoc.getName(), null, "none", mRow, mCol, randMission, -1);
                  selected.setMissionInfo(rescueMission.getInfo());
                  CultimaPanel.missionStack.add(rescueMission);
                  if(CultimaPanel.missionsGiven[randMission]==0)
                     CultimaPanel.missionsGiven[randMission]++;
                  FileManager.saveProgress();
                  response = rescueMission.getStartStory();
               }
            }
         }
         else if(randMission == SEWER_RATS)
         {
            type = "Clear";
            Location sewerLoc = LocationBuilder.findSewerForCity(mi);
            ArrayList<Coord> monsterFreq = sewerLoc.getMonsterFreq();
            byte monsterIndex = NPC.RAT;
            int mFreq = 15;
            if(monsterFreq.size() == 0)
            {
               monsterFreq.add(new Coord(monsterIndex, mFreq));
            }
            else
            {
               mFreq = Math.max(monsterFreq.get(0).getCol(), 10);
               monsterFreq.get(0).setRow(monsterIndex);
               monsterFreq.get(0).setCol(mFreq);
            }
            sewerLoc.setMonsterFreq(monsterFreq);
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(5, 15);
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               story[i]=story[i].replace("ADJECTIVE1", NPC.getRandomFrom(NPC.insultAdjective));
               story[i]=story[i].replace("ADJECTIVE2", NPC.getRandomFrom(NPC.insultAdjective));
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission clearMission = new Mission(type, story, startRow, startCol, goldReward, null, sewerLoc.getName(), null, "none", mRow, mCol, randMission, -1);
            selected.setMissionInfo(clearMission.getInfo());
            CultimaPanel.missionStack.add(clearMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = clearMission.getStartStory();
         }
         else if(randMission==FIX_PATH)
         {
            if(numSwampTiles == 0)
            {  //place mud on a path somewhere in the 12x12 area around the center that is a brick path
               ArrayList<Coord> bogSpawns = new ArrayList<Coord>();
               for(int r=currMap.length/2; r>=currMap.length/2-6; r--)
               {
                  if( CultimaPanel.allTerrain.get(Math.abs(currMap[r][currMap[0].length/2])).getName().toLowerCase().contains("brick_floor"))
                  {
                     bogSpawns.add(new Coord(r, currMap[0].length/2));
                  }
               }
               for(int r=currMap.length/2; r>=currMap.length/2+6; r++)
               {
                  if( CultimaPanel.allTerrain.get(Math.abs(currMap[r][currMap[0].length/2])).getName().toLowerCase().contains("brick_floor"))
                  {
                     bogSpawns.add(new Coord(r, currMap[0].length/2));
                  }
               }
               for(int c=currMap[0].length/2; c>=currMap[0].length/2-6; c--)
               {
                  if( CultimaPanel.allTerrain.get(Math.abs(currMap[currMap.length/2][c])).getName().toLowerCase().contains("brick_floor"))
                  {
                     bogSpawns.add(new Coord(currMap.length/2, c));
                  }
               }
               for(int c=currMap[0].length/2; c>=currMap[0].length/2+6; c++)
               {
                  if( CultimaPanel.allTerrain.get(Math.abs(currMap[currMap.length/2][c])).getName().toLowerCase().contains("brick_floor"))
                  {
                     bogSpawns.add(new Coord(currMap.length/2, c));
                  }
               }
               if(bogSpawns.size() > 0)
               {
                  Coord bogSpawn = Utilities.getRandomFrom(bogSpawns);
                  currMap[bogSpawn.getRow()][bogSpawn.getCol()] = TerrainBuilder.getTerrainWithName("TER_S_$Bog").getValue();
                  if(Math.random() < 0.5 &&  CultimaPanel.allTerrain.get(Math.abs(currMap[bogSpawn.getRow()-1][bogSpawn.getCol()])).getName().toLowerCase().contains("brick_floor"))
                  {
                     currMap[bogSpawn.getRow()-1][bogSpawn.getCol()] = TerrainBuilder.getTerrainWithName("TER_S_$Bog").getValue();
                  }
                  if(Math.random() < 0.5 &&  CultimaPanel.allTerrain.get(Math.abs(currMap[bogSpawn.getRow()+1][bogSpawn.getCol()])).getName().toLowerCase().contains("brick_floor"))
                  {
                     currMap[bogSpawn.getRow()+1][bogSpawn.getCol()] = TerrainBuilder.getTerrainWithName("TER_S_$Bog").getValue();
                  }
                  if(Math.random() < 0.5 &&  CultimaPanel.allTerrain.get(Math.abs(currMap[bogSpawn.getRow()][bogSpawn.getCol()-1])).getName().toLowerCase().contains("brick_floor"))
                  {
                     currMap[bogSpawn.getRow()][bogSpawn.getCol()-1] = TerrainBuilder.getTerrainWithName("TER_S_$Bog").getValue();
                  }
                  if(Math.random() < 0.5 &&  CultimaPanel.allTerrain.get(Math.abs(currMap[bogSpawn.getRow()][bogSpawn.getCol()+1])).getName().toLowerCase().contains("brick_floor"))
                  {
                     currMap[bogSpawn.getRow()][bogSpawn.getCol()+1] = TerrainBuilder.getTerrainWithName("TER_S_$Bog").getValue();
                  }
                  type = "Build";
                  String [] temp = missions[FIX_PATH];
                  String [] story = new String[3];
                  int targItemPrice = Utilities.rollDie(5, 15);
                  for(int i=0; i<story.length; i++)
                  {
                     story[i]=(new String(temp[i])).replace("GOLD_REWARD", ""+targItemPrice);
                  }
                  int startRow = CultimaPanel.player.getWorldRow();
                  int startCol = CultimaPanel.player.getWorldCol();
                  int mRow = -1, mCol = -1;
                  Mission getMission = new Mission(type, story, startRow, startCol, targItemPrice, null, thisLoc.getName(), null, "none", mRow, mCol, FIX_PATH, -1);
                  selected.setMissionInfo(getMission.getInfo());
                  CultimaPanel.missionStack.add(getMission);
                  if(CultimaPanel.missionsGiven[FIX_PATH]==0)
                     CultimaPanel.missionsGiven[FIX_PATH]++;
                  response = getMission.getStartStory();
                  FileManager.saveProgress();
               }
               else  
               {  //could not find valid monster spawn - clear the mission
                  response = "";
               }
            }
            else  
            {  //could not find valid monster spawn - clear the mission
               response = "";
            }
         }
         else if(randMission == CULT_RESCUE)
         { 
            Location currentLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               //add cultists
            int mapIndex = selected.getMapIndex();
            byte [][] house = CultimaPanel.map.get(mapIndex);
            spawnCoords = new ArrayList();
            ArrayList<NPCPlayer> worshipers = new ArrayList();
         //find spawnCoords in a 4x4 space around the center of the city
            java.awt.Rectangle courtyard = new java.awt.Rectangle(house.length/2-2,house[0].length/2-2, 4, 4); 
            for(int r=house.length/2-2; r<=house.length/2+2; r++)
            {
               for(int c=house[0].length/2-2; c<=house[0].length/2+2; c++)
               {
                  NPCPlayer p = Utilities.getNPCAt(r, c, mapIndex);
                  if(p!=null && courtyard.contains(p.getHomeRow(), p.getHomeCol()))
                  {  //move nps that are in the courtyard out of the way
                     p.restoreLoc();
                  }
                  if(TerrainBuilder.isTraversable(r, c, mapIndex))
                     spawnCoords.add(new Coord(r, c));
               }
            }
            int numWorshipers = Math.min(spawnCoords.size(), Utilities.rollDie(2,4));
            for(int i = 0; i < numWorshipers; i++)
            {
               Coord spawnCoord = spawnCoords.remove((int)(Math.random()*spawnCoords.size()));
               int row = spawnCoord.getRow();
               int col = spawnCoord.getCol();
               if(Utilities.getNPCAt(row, col, mapIndex)==null)
               {
                  NPCPlayer p = new NPCPlayer(NPC.BEGGER, row, col, mapIndex, row, col, selected.getLocationType());
                  p.setName("Cultist "+p.getNameSimple());
                  p.setNumInfo(0);
                  worshipers.add(p);
               }
            }
            for(NPCPlayer p:worshipers)
            {
               CultimaPanel.civilians.get(mapIndex).add(p);
            }
         
            type = "Rescue";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("ADJECTIVE1", ""+NPC.getRandomFrom(NPC.insultAdjective)).replace("ADJECTIVE2", ""+NPC.getRandomFrom(NPC.insultAdjective));
            }
            int goldReward = Utilities.rollDie(10,50);
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, thisLoc.getName(), null, "", mRow, mCol, CULT_RESCUE, -1);
            selected.setMissionInfo(rescueMission.getInfo());
            CultimaPanel.missionStack.add(rescueMission);
            if(CultimaPanel.missionsGiven[CULT_RESCUE]==0)
               CultimaPanel.missionsGiven[CULT_RESCUE]++;
            FileManager.saveProgress();
            response = rescueMission.getStartStory();
         }
         else if(randMission == CURE_POISONED)
         { 
            selected.addEffect("poison");
            Location currentLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            //add snake
            int mapIndex = selected.getMapIndex();
            if(spawnCoords.size() > 0)
            {
               Coord spawn = Utilities.getRandomFrom(spawnCoords);
               int r = spawn.getRow();
               int c = spawn.getCol();
               NPCPlayer snake = new NPCPlayer(NPC.SNAKE, r, c, mapIndex, r, c, locType);
               CultimaPanel.worldMonsters.add(snake); 
            }
            type = "Cure";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            String targName = Utilities.shortName(selected.getName());         
         
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("ADJECTIVE1", ""+NPC.getRandomFrom(NPC.insultAdjective)).replace("ADJECTIVE2", ""+NPC.getRandomFrom(NPC.insultAdjective)).replace("PLAYER_NAME", ""+CultimaPanel.player.getShortName());
            }
            int goldReward = 0;
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, thisLoc.getName(), null, targName, mRow, mCol, randMission, -1);
            selected.setMissionInfo(rescueMission.getInfo());
            CultimaPanel.missionStack.add(rescueMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = rescueMission.getStartStory();
         }
         else if(randMission == FIND_LUMBER1 || randMission == FIND_LUMBER2 || randMission == FIND_LUMBER3 || randMission == COLLECT_FLORETS1 || randMission == FEED_POOR)
         {  
            type = "Harvest";
            NPCPlayer targNPC = selected;       
            if(targNPC!=null)
            {
               Item ourItem = new Item("lumber", 3);
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Utilities.rollDie(3, 8);
               if(randMission == COLLECT_FLORETS1)
               {
                  ourItem = new Item("florets", 3);
                  goldReward = Utilities.rollDie(5, 15);
               }
               else if(randMission == FEED_POOR)
               {
                  ourItem = new Item("15 rations", 15);
                  goldReward = 0;
               }
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(selected.getName());         
               for(int i=0; i<story.length; i++)
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward).replace("PLAYER_NAME",CultimaPanel.player.getShortName());
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", ourItem, targName, mRow, mCol, randMission, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         else if(randMission == RAIN_MAKER1 || randMission == RAIN_MAKER2)
         {  
            type = "Cast";
            NPCPlayer targNPC = selected;       
            if(targNPC!=null)
            {
               int missionDay = -1;
               if(randMission == RAIN_MAKER1)
               {
                  missionDay = CultimaPanel.days;
               }
               else
               {
                  missionDay = CultimaPanel.days + Utilities.rollDie(2,4);
               }
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Utilities.rollDie(3, 8);
               if(randMission == RAIN_MAKER1)
               {
                  goldReward = Utilities.rollDie(5, 15);
               }
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(selected.getName());         
               for(int i=0; i<story.length; i++)
               {
                  story[i]=(story[i].replace("GOLD_REWARD", ""+goldReward)).replace("WEDDING_DAY",""+missionDay);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, thisLoc.getName(), null, targName, mRow, mCol, randMission, missionDay);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         else if(randMission == Mission.WEREWOLFTRAP)
         {
            type = "Clear";
            //look to see if there is a camp in a 6x6 area around this site, and if so, reuse it
            Location closeCamp = TerrainBuilder.closeLocation("camp", 7);
            int r = CultimaPanel.player.getWorldRow();
            int c = CultimaPanel.player.getWorldCol();
            thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations,  r,  c, 0);
            String thisName = "";
            if(thisLoc != null)
               thisName = Display.provinceName(thisLoc.getName());
            ArrayList <Coord> campSpots = new ArrayList<Coord>();
            Location thisCamp = null;
            if(closeCamp == null)   //there is no camp close to us, so we will build one
            {   
               for(int cr = r-6; cr <= r+6; cr++)
               {
                  for(int cc = c-6; cc <= c+6; cc++)
                  {
                     if(LocationBuilder.goodSpotForLocation(worldMap, CultimaPanel.equalizeWorldRow(cr), CultimaPanel.equalizeWorldCol(cc)))
                     {
                        if(cr==r && (cc==c || cc==c-1 || cc==c+1))
                        {  //a location can take up 3 spots, (r, c-1), (r, c) and (r, c+1), so skip those coordinates for finding a campsite
                           continue;
                        }
                        campSpots.add(new Coord(CultimaPanel.equalizeWorldRow(cr), CultimaPanel.equalizeWorldCol(cc)));
                     }
                  }
               }
            }
            if(campSpots.size() == 0 && closeCamp == null)   //no open camp spots
            {
               selected.setNumInfo(0);
               response = Utilities.getRandomFrom(noBattle);
            }
            else
            {
               if(closeCamp == null)
               {
                  Coord campSpot = Utilities.getRandomFrom(campSpots);
                  r = campSpot.getRow();
                  c = campSpot.getCol();
                  worldMap[r][c] = TerrainBuilder.getTerrainWithName("LOC_L_1_$Camp").getValue();
                  Teleporter teleporter = new Teleporter((CultimaPanel.map).size());
                  thisCamp = new Location(thisName + " camp", r, c, 0, CultimaPanel.allTerrain.get(Math.abs(worldMap[r][c])), teleporter);
                  CultimaPanel.allLocations.add(thisCamp);
                  CultimaPanel.allCamps.add(thisCamp);
               }
               else
               {
                  r = closeCamp.getRow();
                  c = closeCamp.getCol();
                  closeCamp.getTeleporter().resetTo();
                  thisCamp = closeCamp;
               }
                     //populate thisCamp with brigands and monsters and save the location
               LocationBuilder.constructCampAdventureAt(thisCamp, NPC.WEREWOLF, NPC.GUARD_FIST);
               TerrainBuilder.markMapPath(thisCamp.getName());
               String [] temp = missions[WEREWOLFTRAP];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Utilities.rollDie(25,75);
               if(CultimaPanel.shoppeDiscount)   //guard is charmed
                  goldReward = (int)(goldReward * 1.20);
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = thisCamp.getRow();
               int mCol = thisCamp.getCol();
               Mission campMission = new Mission(type, story, startRow, startCol, goldReward, null, thisCamp.getName(), null, "none", mRow, mCol, WEREWOLFTRAP, -1);
               selected.setMissionInfo(campMission.getInfo());
               CultimaPanel.missionStack.add(campMission);
               if(CultimaPanel.missionsGiven[WEREWOLFTRAP]==0)
                  CultimaPanel.missionsGiven[WEREWOLFTRAP]++;
               FileManager.saveProgress();
               response = campMission.getStartStory();
            }
         }
         else if(randMission == Mission.SLAY_VAMPIRE && loc!=null)
         {  
            type = "Slay";
            NPCPlayer targNPC = null;
            byte monsterType = NPC.MALEVAMP;
            if(Math.random() < 0.5)
               monsterType = NPC.FEMALEVAMP;
            String target = NPC.characterDescription(monsterType)+"-head";
            targNPC = new NPCPlayer(monsterType, 0, 0, 0, locType);  
            targNPC.modifyStats(1.2);
            targNPC.addItem(new Item(target,0));
            boolean success = TerrainBuilder.addNPCtoLocation(loc, locType, targNPC);
            if(targNPC!=null && success)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 0;
               String targName = Utilities.shortName(selected.getName());         
               String locName = Display.provinceName(loc.getName());
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("LOCATION_NAME", locName);
                  story[i]=story[i].replace("NPC_NAME", Utilities.shortName(targNPC.getName()));
                  story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(targNPC.getMapIndex() != selected.getMapIndex())
               {
                  mRow = loc.getRow();
                  mCol = loc.getCol();
               }
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target,0), targName, mRow, mCol, SLAY_VAMPIRE, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[SLAY_VAMPIRE]==0)
                  CultimaPanel.missionsGiven[SLAY_VAMPIRE]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            } 
         }
         else if(randMission == Mission.ESCORT_ADULT)
         {  
            type = "Escort";
            int randType = Utilities.rollDie(1,10);
            NPCPlayer targNPC = null;
            if(randType<=7)       //escort to the next closest town
            {
               String []locTypes2 = {"city","fortress","port","village"};
               Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
               loc = closeCity;
            }
            else                 //escort to the next closest castle
            {
               String [] locTypes = {"castle","tower"};
               Location closeCastle = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
               loc = closeCastle;
            }
            if(loc!=null)
            {
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               //higher reward for targets further away
               int goldReward = Math.max(60, selected.getLevel()+(int)(Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), loc.getRow(), loc.getCol())));
               String targName = Utilities.shortName(selected.getName());         
                                                                  
               CultimaPanel.missionTargetNames.add(targName);
               int targItemPrice = (int)(goldReward);
                
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = loc.getRow();
               int mCol = loc.getCol();
               TerrainBuilder.markMapPath(loc.getName());
                               //public Mission(type, story, worldRow, worldCol, goldReward, itemReward, clearLocation, target,  targHolder, targetRow, targetCol, missionType)
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null,       locName,       null,    targName,   mRow,      mCol, (byte)randMission, -1);
               
               CultimaPanel.player.setNpcBasicInfo(targName +","+ selected.getMight() +","+ selected.getMind() +","+ selected.getAgility() +","+ selected.getBody() +","+ selected.getCharIndex());
               //remove selected and add their clone as a worldMonster that follows us
               NPCPlayer npc = new NPCPlayer(selected.getCharIndex(), selected.getRow(), selected.getCol(), selected.getMapIndex(), selected.getLocationType(), CultimaPanel.player.getNpcBasicInfo(), true);
               npc.setHasMet(true);
               npc.setMoveType(NPC.CHASE);
               npc.setMissionInfo(getMission.getInfo());
               Utilities.removeNPCat(selected.getRow(), selected.getCol(), selected.getMapIndex());
               CultimaPanel.worldMonsters.add(npc);      
               
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
            else
            {
            //no mission
               response = Utilities.getRandomFrom(NPC.mainMission)+".";
               selected.setNumInfo(0);
            }   
         } 
         else if(randMission == Mission.ESCORT_KID)
         {  
            type = "Escort";
            NPCPlayer targNPC = null;
            //make or find a battlefield close to this city
            int r = CultimaPanel.player.getWorldRow();
            int c = CultimaPanel.player.getWorldCol();
            thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations,  r,  c, 0);
            String thisName = "";
            if(thisLoc != null)
               thisName = Display.provinceName(thisLoc.getName());
            ArrayList <Coord> battleSpots = new ArrayList<Coord>();
            if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r-2), CultimaPanel.equalizeWorldCol(c)))
               battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r-2), CultimaPanel.equalizeWorldCol(c)));
            if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r+2), CultimaPanel.equalizeWorldCol(c)))
               battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r+2), CultimaPanel.equalizeWorldCol(c)));
            if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r-3), CultimaPanel.equalizeWorldCol(c)))
               battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r-3), CultimaPanel.equalizeWorldCol(c)));
            if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r+3), CultimaPanel.equalizeWorldCol(c)))
               battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r+3), CultimaPanel.equalizeWorldCol(c)));
            if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(c-3)))
               battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(c-3)));
            if(!LocationBuilder.isImpassable(worldMap, CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(c+3)))
               battleSpots.add(new Coord(CultimaPanel.equalizeWorldRow(r), CultimaPanel.equalizeWorldCol(c+3)));
            if(battleSpots.size() == 0)   //no open battle spots
            {
               selected.setNumInfo(0);
               response = Utilities.getRandomFrom(noBattle);
            }
            else
            {
               Coord battleSpot = Utilities.getRandomFrom(battleSpots);
               r = battleSpot.getRow();
               c = battleSpot.getCol();
               Location battlefield = null;
                  //if there has already been a battle at this place, just replace the same location with a new battlefield
               Location oldBattlefield = CultimaPanel.getLocation(thisName + " battlefield");
               if(oldBattlefield != null)
               {
                  r = oldBattlefield.getRow();
                  c = oldBattlefield.getCol();
                  oldBattlefield.getTeleporter().resetTo();
                  battlefield = oldBattlefield;
               }
               else
               {
                  worldMap[r][c] = TerrainBuilder.getTerrainWithName("LOC_$Battlefield").getValue();
                  Teleporter teleporter = new Teleporter((CultimaPanel.map).size());
                  battlefield = new Location(thisName + " battlefield", r, c, 0, CultimaPanel.allTerrain.get(Math.abs(worldMap[r][c])), teleporter);
                  CultimaPanel.allLocations.add(battlefield);
                  CultimaPanel.allBattlefields.add(battlefield);
               }
            
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               //higher reward for targets further away
               int goldReward = Utilities.rollDie(10,40);
               String targName = Utilities.shortName(selected.getName());         
                                                                  
               CultimaPanel.missionTargetNames.add(targName);
               int targItemPrice = (int)(goldReward);
                
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = battlefield.getRow();
               int mCol = battlefield.getCol();
                               //public Mission(type, story, worldRow, worldCol, goldReward, itemReward, clearLocation,          target,  targHolder, targetRow, targetCol, missionType)
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null,       battlefield.getName(), null,    targName,     mRow,      mCol, (byte)randMission, -1);
               
               int kidRow = selected.getRow();
               int kidCol = selected.getCol();
               ArrayList<Coord>kidSpawns = new ArrayList();
               if(!Utilities.NPCAt(kidRow-1, kidCol, selected.getMapIndex()))
                  kidSpawns.add(new Coord(kidRow-1, kidCol));
               if(!Utilities.NPCAt(kidRow+1, kidCol, selected.getMapIndex()))
                  kidSpawns.add(new Coord(kidRow+1, kidCol));
               if(!Utilities.NPCAt(kidRow, kidCol-1, selected.getMapIndex()))
                  kidSpawns.add(new Coord(kidRow, kidCol-1));
               if(!Utilities.NPCAt(kidRow, kidCol+1, selected.getMapIndex()))
                  kidSpawns.add(new Coord(kidRow, kidCol+1));
               if(kidSpawns.size() > 0)
               {
                  Coord kidSpawn = Utilities.getRandomFrom(kidSpawns);
                  kidRow = kidSpawn.getRow();
                  kidCol = kidSpawn.getCol();
               }   
               NPCPlayer tempKid = new NPCPlayer(NPC.CHILD, kidRow, kidCol, selected.getMapIndex(), selected.getLocationType());
               CultimaPanel.player.setNpcBasicInfo(tempKid.getNameSimple() +","+ tempKid.getMight() +","+ tempKid.getMind() +","+ tempKid.getAgility() +","+ tempKid.getBody() + "," + NPC.CHILD);
               NPCPlayer npc = new NPCPlayer(NPC.CHILD, kidRow, kidCol, selected.getMapIndex(), selected.getLocationType(), CultimaPanel.player.getNpcBasicInfo(), true);
               npc.setHasMet(true);
               npc.setMoveType(NPC.CHASE);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.worldMonsters.add(npc);      
               
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            } 
         }
         else if(randMission == Mission.CITY_REVENGE || randMission == Mission.MESSAGE || randMission == Mission.DELIVER_MEAT)
         {  //Assassinate mission
            if(randMission == Mission.CITY_REVENGE)
               type = "Assassinate";
            else if(randMission == Mission.MESSAGE)
               type = "Find";
            else if(randMission == Mission.DELIVER_MEAT)
               type = "Give";   
            int randType = Utilities.rollDie(10);
            if(randMission == Mission.MESSAGE || randMission == Mission.DELIVER_MEAT)
               randType = Utilities.rollDie(4,10);
            NPCPlayer targNPC = null;
            if(inLocNPCs.size() <= 1)
               randType = Utilities.rollDie(5,10);
            if(randType < 5)           //assassinate someone at the same location
            {
               loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               targNPC = Utilities.getRandomFrom(inLocNPCs);
               
               ArrayList<NPCPlayer> targets = new ArrayList<NPCPlayer>();
               for(NPCPlayer pl: inLocNPCs)
                  if(!pl.getName().equals(selected.getName()))
                     targets.add(pl);
               if(targets.size()==0)
                  targNPC = null;
               else
                  targNPC = Utilities.getRandomFrom(targets);
            }
            if(targNPC==null || (randType>=5 && randType<=8))       //assassinate or find someone at the next closest town
            {
               String []locTypes2 = {"city","fortress","port","village"};
               Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
               inLocNPCs = Utilities.getNPCsInLoc(closeCity.getMapIndex(), NPC.CHILD, selected.getName());
               if(inLocNPCs.size() >= 1)
               {
                  targNPC = Utilities.getRandomFrom(inLocNPCs);
                  loc = closeCity;
               }
               else
                  loc = null;
            }
            if(targNPC==null || randType>8)      //assassinate or find someone at the next closest castle
            {
               String [] locTypes = {"castle","tower"};
               Location closeCastle = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
               inLocNPCs = Utilities.getNPCsInLoc(closeCastle.getMapIndex(), NPC.CHILD, selected.getName());
               if(inLocNPCs.size() >= 1)
               {
                  targNPC = Utilities.getRandomFrom(inLocNPCs);
                  loc = closeCastle;
               }
               else
                  loc = null;
            }
            if(targNPC==null)    //couln't find a target, so try the same city again
            {
               loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               targNPC = Utilities.getRandomFrom(inLocNPCs);
               
               ArrayList<NPCPlayer> targets = new ArrayList<NPCPlayer>();
               for(NPCPlayer pl: inLocNPCs)
                  if(!pl.getName().equals(selected.getName()))
                     targets.add(pl);
               if(targets.size()==0)
                  targNPC = null;
               else
                  targNPC = Utilities.getRandomFrom(targets);
            }
         
            if(targNPC!=null && loc!=null)
            {
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
            //higher reward for targets further away
               int goldReward = Math.max(60, targNPC.getLevel()+(int)(Display.wrapDistance(CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol(), loc.getRow(), loc.getCol())));
               String targName = Utilities.shortName(targNPC.getName());     
               CultimaPanel.missionTargetNames.add(targName);
               String target = targName+"-bounty";
               int missionDay = -1;
               if(randMission == Mission.MESSAGE)
               {
                  selName = Utilities.shortName(selected.getName());
                  String [] selNameParts = selName.split(" ");
                  if(selNameParts.length > 1)
                  {
                     selName = selNameParts[1];
                  }
                  goldReward = 25;
                  target = selName+"-message";      //make this the short version of selected's name
               }
               else if(randMission == Mission.DELIVER_MEAT)
               {
                  String [] selNameParts = targName.split(" ");
                  if(selNameParts.length > 1)
                  {
                     targName = selNameParts[1];
                  }
                  target = targName+"-parcel";
                  goldReward = Math.max(5, goldReward/10);
               }
               int targItemPrice = (int)(goldReward);
               if(randMission == Mission.DELIVER_MEAT)
               {
                  CultimaPanel.player.addItem(target);
                  targNPC.addItem(new Item("message-parcel-receipt", 0));
               }
               else
               {
                  targNPC.addItem(new Item(target, targItemPrice));
               }
               String locName = Display.provinceName(loc.getName());
            
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(randMission == Mission.DELIVER_MEAT)
               {  //more time to deliver parcel if it is in another town
                  missionDay = CultimaPanel.days + 1;
                  if(targNPC.getMapIndex() != selected.getMapIndex())
                  {
                     missionDay += Utilities.rollDie(1,2);
                  }
               }
               if(targNPC.getMapIndex() != selected.getMapIndex())
               {
                  mRow = loc.getRow();
                  mCol = loc.getCol();
               }
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
                  story[i]=story[i].replace("ITEM_NAME", target);
                  story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
                  story[i]=story[i].replace("DAY", ""+missionDay);
               }
            
               TerrainBuilder.markMapPath(loc.getName());
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target, goldReward), targName, mRow, mCol, (byte)randMission, missionDay);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
            else
            {
            //no mission
               response = Utilities.getRandomFrom(NPC.mainMission)+".";
               selected.setNumInfo(0);
            }   
         } 
         else if(randMission==MERCY_KILL)
         {
            type = "Mercy";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            String targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            String locName = Display.provinceName(loc.getName());
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, targName, mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         }
         else if(randMission == Mission.FIND_PERSON)
         {        //rescue mission
            int numPrisoners = LocationBuilder.countPrisoners(loc);
            if(numPrisoners > 0)
            {
               String locName = loc.getName();
               int locMapIndex = loc.getFromMapIndex();
               if(locMapIndex!=0)  
               {  //if this is a dungeon in a castle or temple, set the location to the one that leads to that dungeon
                  loc = CultimaPanel.allLocations.get(locMapIndex);
               }
               type = "Rescue";
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Utilities.rollDie(40,100);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = loc.getRow();
               int mCol = loc.getCol();
               TerrainBuilder.markMapPath(loc.getName());      
               Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, "none", mRow, mCol, FIND_PERSON, -1);
               selected.setMissionInfo(rescueMission.getInfo());
               CultimaPanel.missionStack.add(rescueMission);
               if(CultimaPanel.missionsGiven[FIND_PERSON]==0)
                  CultimaPanel.missionsGiven[FIND_PERSON]++;
               FileManager.saveProgress();
               response = rescueMission.getStartStory();
            }
         }
         else if(randMission == CLEAR_BEGGERS)
         { 
            String targName = " all beggers";
            type = "Remove";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 50 + (5*numBeggers);
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission removeMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, targName, mRow, mCol, CLEAR_BEGGERS, -1);
            selected.setMissionInfo(removeMission.getInfo());
            CultimaPanel.missionStack.add(removeMission);
            if(CultimaPanel.missionsGiven[CLEAR_BEGGERS]==0)
               CultimaPanel.missionsGiven[CLEAR_BEGGERS]++;
            FileManager.saveProgress();
            response = removeMission.getStartStory();
         }
         else if(randMission == TRAIN_DOG)
         {
            Item dog = new Item("dog", 25);
            String targetName = Utilities.shortName(selected.getName());
            type = "Train";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 25;
            double priceShift = ((Math.random()*51) - 25)/100.0;  //-.25 to +.25
            goldReward = (int)(goldReward + (goldReward * priceShift));            
            if(CultimaPanel.shoppeDiscount)   //charmed
               goldReward = (int)(goldReward * 1.20);
         
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission trainMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", dog, targetName, mRow, mCol, TRAIN_DOG, -1);
            selected.setMissionInfo(trainMission.getInfo());
            CultimaPanel.missionStack.add(trainMission);
            if(CultimaPanel.missionsGiven[TRAIN_DOG]==0)
               CultimaPanel.missionsGiven[TRAIN_DOG]++;
            FileManager.saveProgress();
            response = trainMission.getStartStory();
         }
         else if(randMission == CLEAR_BANDITS && spawnCoords.size() > 0)
         { 
            String targetHolder = Utilities.nameGenerator("name");
            type = "Save";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(75, 200);
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               story[i]=story[i].replace("NPC_NAME", ""+targetHolder);
               story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            //***add bandits to the location
            locType = CultimaPanel.player.getLocationType();
            int numBandits = Utilities.rollDie(3,7);
            byte indexType = NPC.getRandGangMember();
            for(int i=0; i<numBandits; i++)
            { 
               Coord spawn = Utilities.getRandomFrom(spawnCoords);
               int r = spawn.getRow();
               int c = spawn.getCol();
               NPCPlayer bandit = new NPCPlayer(indexType, r, c, mi, r, c, locType);
               bandit.setName(targetHolder+" Sibling");
               if(indexType==NPC.BRIGANDKING)
                  bandit.setWeapon(Weapon.getShortSwords());
               else if(indexType==NPC.BOWASSASSIN)
                  bandit.setWeapon(Weapon.getLongbow());
               else if(indexType==NPC.VIPERASSASSIN)
                  bandit.setWeapon(Weapon.getDagger());
               else if(indexType==NPC.ENFORCER)
                  bandit.setWeapon(Weapon.getCrossbow());
               bandit.setMoveType(NPC.CHASE);      
               CultimaPanel.worldMonsters.add(bandit);
            }
            //***                   //Mission(String t, String[]story, int wRow, int wCol, int gold,   Item iw,  String loc,        Item targ, String targHolder, int targR, int targC, byte missionType)
            Mission saveMission = new Mission(type,     story,         startRow, startCol, goldReward, null,     thisLoc.getName(), null,      targetHolder,      mRow,      mCol, CLEAR_BANDITS, -1);
            selected.setMissionInfo(saveMission.getInfo());
            CultimaPanel.missionStack.add(saveMission);
            if(CultimaPanel.missionsGiven[CLEAR_BANDITS]==0)
               CultimaPanel.missionsGiven[CLEAR_BANDITS]++;
            FileManager.saveProgress();
            response = saveMission.getStartStory();
         }
         else if(randMission == Mission.FIND_SERPENT_EGGS)
         {  
            type = "Harvest";
            NPCPlayer targNPC = selected;       
            if(targNPC!=null)
            {
               Item ourItem = new Item("3-serpent-eggs",90);
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Utilities.rollDie(25, 100);
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(selected.getName());         
               for(int i=0; i<story.length; i++)
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", ourItem, targName, mRow, mCol, FIND_SERPENT_EGGS, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[FIND_SERPENT_EGGS]==0)
                  CultimaPanel.missionsGiven[FIND_SERPENT_EGGS]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         } 
         else if(randMission == LOVE_TRIANGLE)
         { 
            NPCPlayer targetRival = null;
            String targName = "";
            ArrayList<NPCPlayer> otherNPCs = Utilities.getNPCsInLoc(selected.getMapIndex(), NPC.CHILD, selected.getName());
            loc = CultimaPanel.allLocations.get(selected.getMapIndex());
            targetRival = Utilities.getRandomFrom(otherNPCs);
            targName = Utilities.shortName(targetRival.getName());
            CultimaPanel.missionTargetNames.add(targName);
            type = "Frighten";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(40,100);
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("NPC_NAME", targName);
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission removeMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, targName, mRow, mCol, LOVE_TRIANGLE, -1);
            selected.setMissionInfo(removeMission.getInfo());
            CultimaPanel.missionStack.add(removeMission);
            if(CultimaPanel.missionsGiven[LOVE_TRIANGLE]==0)
               CultimaPanel.missionsGiven[LOVE_TRIANGLE]++;
            FileManager.saveProgress();
            response = removeMission.getStartStory();
         }
         else if(randMission == BEAT_SWINE_PLAYER)
         { 
            NPCPlayer targetRival = null;
            String targName = "";
            ArrayList<NPCPlayer> otherNPCs = Utilities.getNPCsInLoc(selected.getMapIndex(), NPC.CHILD, selected.getName());
            loc = CultimaPanel.allLocations.get(selected.getMapIndex());
            targetRival = Utilities.getRandomFrom(otherNPCs);
            targName = Utilities.shortName(targetRival.getName());
            CultimaPanel.missionTargetNames.add(targName);   
            String target = targName+"-swine-bounty";
            targetRival.addItem(new Item(target, 0));
            targetRival.setIsSwinePlayer(true);
            targetRival.addItem(Item.getLoadedCube());
            type = "Swine";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("NPC_NAME", targetRival.getName());
               story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
               story[i]=story[i].replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission swineMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), new Item(target, goldReward), targName, mRow, mCol, BEAT_SWINE_PLAYER, -1);
            selected.setMissionInfo(swineMission.getInfo());
            CultimaPanel.missionStack.add(swineMission);
            if(CultimaPanel.missionsGiven[BEAT_SWINE_PLAYER]==0)
               CultimaPanel.missionsGiven[BEAT_SWINE_PLAYER]++;
            FileManager.saveProgress();
            response = swineMission.getStartStory();
         }
         else if(randMission == FIRE_FIGHTER && fireSpawnCoords.size() > 0)
         { 
            type = "Extinguish";
            Coord spawn = Utilities.getRandomFrom(fireSpawnCoords);
            //see what part of town the fire is in
            String direction = "center";
            double[]directionDist = new double[4];
            String[]directions = {"North", "East", "South", "West"};
            directionDist[LocationBuilder.NORTH] = (spawn.getRow() - currMap.length/2);
            if(directionDist[LocationBuilder.NORTH] < 0)
               directionDist[LocationBuilder.NORTH] = Math.abs(directionDist[LocationBuilder.NORTH]);
            else
               directionDist[LocationBuilder.NORTH] = 0;
            directionDist[LocationBuilder.SOUTH] = (spawn.getRow() - currMap.length/2);
            if(directionDist[LocationBuilder.SOUTH] > 0)
               directionDist[LocationBuilder.SOUTH] = Math.abs(directionDist[LocationBuilder.SOUTH]);
            else
               directionDist[LocationBuilder.SOUTH] = 0;
            directionDist[LocationBuilder.WEST] = (spawn.getCol() - currMap[0].length/2);
            if(directionDist[LocationBuilder.WEST] < 0)
               directionDist[LocationBuilder.WEST] = Math.abs(directionDist[LocationBuilder.WEST]);
            else
               directionDist[LocationBuilder.WEST] = 0;
            directionDist[LocationBuilder.EAST] = (spawn.getCol() - currMap[0].length/2);
            if(directionDist[LocationBuilder.EAST] > 0)
               directionDist[LocationBuilder.EAST] = Math.abs(directionDist[LocationBuilder.EAST]);
            else
               directionDist[LocationBuilder.EAST] = 0;
            int maxDir = 0;
            for(int i=0; i<4; i++)
               if(directionDist[i] > directionDist[maxDir])
                  maxDir = i;      
            direction = directions[maxDir];
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(50, 100);
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               story[i]=story[i].replace("DIR", ""+direction);
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = CultimaPanel.days;
            int mCol = (int)(CultimaPanel.time) + 4;
            //***add fire to the location
            locType = CultimaPanel.player.getLocationType();
            byte indexType = NPC.FIRE;
            int r = spawn.getRow();
            int c = spawn.getCol();
            NPCPlayer fire = new NPCPlayer(indexType, r, c, mi, r, c, locType);
            CultimaPanel.worldMonsters.add(fire);
            //***                   //Mission(String t, String[]story, int wRow, int wCol, int gold,   Item iw,  String loc,        Item targ, String targHolder, int targR, int targC, int missionType)
            Mission saveMission = new Mission(type,     story,         startRow, startCol, goldReward, null,     thisLoc.getName(), null,      "none",            mRow,      mCol, FIRE_FIGHTER, -1);
            selected.setMissionInfo(saveMission.getInfo());
            CultimaPanel.missionStack.add(saveMission);
            if(CultimaPanel.missionsGiven[FIRE_FIGHTER]==0)
               CultimaPanel.missionsGiven[FIRE_FIGHTER]++;
            FileManager.saveProgress();
            response = saveMission.getStartStory();
         }
         else if(randMission == Mission.FIND_GEM || randMission == Mission.FIND_SCALES)
         {  
            type = "Find";
            String targName = Utilities.shortName(selected.getName());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(25,75);
            Item randGem = Item.getRandomStone();
            String gemType = randGem.getName();
            if(randMission == Mission.FIND_SCALES)
            {
               randGem = Item.getRandomScales();
               gemType = randGem.getName();
               if(gemType.toLowerCase().contains("dragon"))
               {
                  String [] locTypes = {"cave"};
                  Location caveLoc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes);
                  if(caveLoc != null)
                  {
                     byte monsterType = NPC.DRAGON;
                     if(gemType.toLowerCase().contains("queen"))
                     {
                        monsterType = NPC.DRAGONKING;
                     }
                     NPCPlayer targNPC = new NPCPlayer(monsterType, 0, 0, 0, "cave");
                     Coord spawnCoord = TerrainBuilder.addNPCtoLocationReturnPoint(caveLoc, "cave", targNPC);
                     TerrainBuilder.markMapPath(caveLoc.getName());
                  }
               }
            }
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("ITEM_NAME", gemType);
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               story[i]=story[i].replace("ADJECTIVE", ""+NPC.getRandomFrom(NPC.complimentAdjective));
            }             
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", randGem, targName, mRow, mCol, randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         } 
         else if(randMission == Mission.FIND_ELK_PELT)
         {  
            type = "Find";
            String targName = Utilities.shortName(selected.getName());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            Armor randPelt = Armor.getElkPelt();
            String peltType = randPelt.getName();
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("ITEM_NAME", peltType);
            }             
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", new Item(peltType, 30), targName, mRow, mCol, FIND_ELK_PELT, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[FIND_ELK_PELT]==0)
               CultimaPanel.missionsGiven[FIND_ELK_PELT]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         } 
         else if(randMission == BURN_COFFINS)
         {
            type = "Burn";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;         
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission burnMission = new Mission(type, story, startRow, startCol, goldReward, null, thisLoc.getName(), null, "none", mRow, mCol, BURN_COFFINS, -1);
            selected.setMissionInfo(burnMission.getInfo());
            CultimaPanel.missionStack.add(burnMission);
            if(CultimaPanel.missionsGiven[BURN_COFFINS]==0)
               CultimaPanel.missionsGiven[BURN_COFFINS]++;
            FileManager.saveProgress();
            response = burnMission.getStartStory();
         }
      }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(NPC.mainMission)+".";
         selected.setNumInfo(Utilities.rollDie(3));
      }
      return response;
   }

   public static String childMission(NPCPlayer selected)
   {
      String [] noMission = {"Numbers! Numbers!", "Tag! Thee is it!", "Can thee count to the number?", "Play Sly Fox with me!"};
   
      String response = "";
      String [] locTypes = {"cave", "mine", "lair", "dungeon"};
      Location loc =  TerrainBuilder.closeLocation(CultimaPanel.allLocations, locTypes);
      Location currLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      byte[][] worldMap = CultimaPanel.map.get(0);
      boolean hasSigns = TerrainBuilder.hasSigns(currLoc);
      boolean hasGrayTiles = TerrainBuilder.hasGrayTiles(currLoc);
      NPCPlayer targNPC = null;
      String locType = "";
      if(loc == null)
      {
         String [] locTypes2 = {"cave"};
         loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes2);
         locType = "cave";
      }
      if(loc == null)
      {
         String [] locTypes2 = {"mine"};
         loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes2);
         locType = "mine";
      }
      if(loc == null)
      {
         String [] locTypes2 = {"lair"};
         loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes2);
         locType = "lair";
      }
      if(loc == null)
      {
         String [] locTypes2 = {"dungeon"};
         loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes2);
         locType = "dungeon";
      }
      if(loc != null)
      {
         String terrType = loc.getTerrain().getName().toLowerCase();
         if(TerrainBuilder.habitablePlace(terrType))
            locType =  "town";
         else if(terrType.contains("cave"))
            locType = "cave";
         else if(terrType.contains("mine"))
            locType = "mine";
         else if(terrType.contains("lair"))
            locType = "lair";
         else if(terrType.contains("dungeon"))
            locType = "dungeon";
         else if(terrType.contains("temple"))
            locType = "temple";
         else if(terrType.contains("sewer"))
            locType = "sewer";   
      }
      else
      {
         selected.setNumInfo(0);
         return NPC.childResponse(selected, "");
      }  
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getType().contains("Clear") && currMission.getMissionType() == DOGTRAP)
         {  //if there is no dog at the location and you have a dog
            Location campLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            int numDogs = LocationBuilder.countDogs(campLoc);
            if (numDogs == 0 && !CultimaPanel.player.getDogName().equals("none"))
            {
               CultimaPanel.civilians.get(campLoc.getMapIndex()).clear();
               NPCPlayer dog = Utilities.getDog();
               dog.setMoveTypeTemp(NPC.ROAM);
               dog.setHasMet(false);
               CultimaPanel.player.setDogBasicInfo("none");
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getMissionType() == COLLECT_FLORETS3)
         {
            String favoriteColor = NPC.simpleColors[Math.abs(selected.hashCode()) % NPC.simpleColors.length];
            if(CultimaPanel.player.numSpecificFlowers(favoriteColor) >= 10)
            {
               CultimaPanel.player.emptySpecificFlowers(favoriteColor);
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Rescue") && currMission.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               if(LocationBuilder.countPrisoners(missLoc)==0)      //we freed all the prisoners
               {
                  int reputationPoints = 10;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Train"))
         {
            if(!CultimaPanel.player.getDogName().equals("none"))
            {
               NPCPlayer dog = Utilities.getDog();
               dog.setMoveTypeTemp(NPC.ROAM);
               dog.setHasMet(false);
               CultimaPanel.player.setDogBasicInfo("none");
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Break") && currMission.getClearLocation().length()>0)
         {   //check to see if break shoppe sign mission is finished   
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if ((currMission.getMissionType() == DESTROY_SIGNS && !hasSigns) || (currMission.getMissionType() == HAUNTED_HOUSE && !hasGrayTiles))
            {
               int reputationPoints = -5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Destroy"))
         {   //check to see if Destroy mission is finished   
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.player.removeItem(target.getName());
               int reputationPoints = -5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
      }  //end seeing if we finished a mission
      if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
         Item itemReward = currMission.getItemReward();
         CultimaPanel.player.addGold(goldReward);
         if(itemReward != null)
            CultimaPanel.player.addItem(itemReward.getName());
         selected.setNumInfo(0);    
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else
      {  
         //see if there is a statue in this location.
         NPCPlayer statue = null;
         Location locTry = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
         ArrayList<NPCPlayer> inLocNPCs = CultimaPanel.civilians.get(selected.getMapIndex());
         for(NPCPlayer p: inLocNPCs)
         {
            if(p.isStatue())
            {
               statue = p;
               targNPC = statue;
               break;
            }
         }
      //FIND_PARENT, FIND_PET                         
         byte randMission = FIND_PARENT;
         ArrayList<Byte>missionTypes = new ArrayList<Byte>();
         //fill list of missions to pick from with the ones we have not seen yet
         if(CultimaPanel.missionsGiven[FIND_PARENT]==0)
            missionTypes.add(FIND_PARENT);
         if(CultimaPanel.missionsGiven[FIND_PET]==0)
            missionTypes.add(FIND_PET);
         if(CultimaPanel.missionsGiven[COLLECT_FLORETS3]==0 && !Display.isWinter())
            missionTypes.add(COLLECT_FLORETS3);
         if(CultimaPanel.missionsGiven[DOGTRAP]==0)
            missionTypes.add(DOGTRAP);
         if(hasSigns && CultimaPanel.missionsGiven[DESTROY_SIGNS]==0)
            missionTypes.add(DESTROY_SIGNS);
         if(hasGrayTiles && CultimaPanel.missionsGiven[HAUNTED_HOUSE]==0)
            missionTypes.add(HAUNTED_HOUSE);
      
         if(statue!=null && CultimaPanel.missionsGiven[DESTROY_STATUE2]==0)
            missionTypes.add(DESTROY_STATUE2);   
         if(missionTypes.size()==0)    //we have seen all the mission types before, so pick a random one
         {
            missionTypes.add(FIND_PARENT);
            if(!Display.isWinter())
               missionTypes.add(COLLECT_FLORETS3);
            missionTypes.add(FIND_PET);
            missionTypes.add(DOGTRAP);
            if(hasSigns)
               missionTypes.add(DESTROY_SIGNS);
            if(hasGrayTiles)
               missionTypes.add(HAUNTED_HOUSE);
            if(statue!=null)
               missionTypes.add(DESTROY_STATUE2);
         }
         randMission = Utilities.getRandomFrom(missionTypes);
         
         //***TESTING*** randMission = Mission.FIND_PARENT;
         
         if(randMission == Mission.DOGTRAP)
         {
            type = "Clear";
            //look to see if there is a camp in a 6x6 area around this site, and if so, reuse it
            Location closeCamp = TerrainBuilder.closeLocation("camp", 7);
            int r = CultimaPanel.player.getWorldRow();
            int c = CultimaPanel.player.getWorldCol();
            Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations,  r,  c, 0);
            String thisName = "";
            if(thisLoc != null)
               thisName = Display.provinceName(thisLoc.getName());
            ArrayList <Coord> campSpots = new ArrayList<Coord>();
            Location thisCamp = null;
            if(closeCamp == null)   //there is no camp close to us, so we will build one
            {   
               for(int cr = r-6; cr <= r+6; cr++)
               {
                  for(int cc = c-6; cc <= c+6; cc++)
                  {
                     if(LocationBuilder.goodSpotForLocation(worldMap, CultimaPanel.equalizeWorldRow(cr), CultimaPanel.equalizeWorldCol(cc)))
                     {
                        if(cr==r && (cc==c || cc==c-1 || cc==c+1))
                        {  //a location can take up 3 spots, (r, c-1), (r, c) and (r, c+1), so skip those coordinates for finding a campsite
                           continue;
                        }
                        campSpots.add(new Coord(CultimaPanel.equalizeWorldRow(cr), CultimaPanel.equalizeWorldCol(cc)));
                     }
                  }
               }
            }
            if(campSpots.size() == 0 && closeCamp == null)   //no open camp spots
            {
               selected.setNumInfo(0);
               response = Utilities.getRandomFrom(noMission);
            }
            else
            {
               if(closeCamp == null)
               {
                  Coord campSpot = Utilities.getRandomFrom(campSpots);
                  r = campSpot.getRow();
                  c = campSpot.getCol();
                  worldMap[r][c] = TerrainBuilder.getTerrainWithName("LOC_L_1_$Camp").getValue();
                  Teleporter teleporter = new Teleporter((CultimaPanel.map).size());
                  thisCamp = new Location(thisName + " camp", r, c, 0, CultimaPanel.allTerrain.get(Math.abs(worldMap[r][c])), teleporter);
                  CultimaPanel.allLocations.add(thisCamp);
                  CultimaPanel.allCamps.add(thisCamp);
               }
               else
               {
                  r = closeCamp.getRow();
                  c = closeCamp.getCol();
                  closeCamp.getTeleporter().resetTo();
                  thisCamp = closeCamp;
               }
               //populate thisCamp with guards and wolves with one dog and save the location
               LocationBuilder.constructCampAdventureAt(thisCamp, NPC.DOG, NPC.GUARD_FIST);
               TerrainBuilder.markMapPath(thisCamp.getName());
               String [] temp = missions[DOGTRAP];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 1;
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = thisCamp.getRow();
               int mCol = thisCamp.getCol();
               Mission campMission = new Mission(type, story, startRow, startCol, goldReward, null, thisCamp.getName(), null, "none", mRow, mCol, DOGTRAP, -1);
               selected.setMissionInfo(campMission.getInfo());
               CultimaPanel.missionStack.add(campMission);
               if(CultimaPanel.missionsGiven[DOGTRAP]==0)
                  CultimaPanel.missionsGiven[DOGTRAP]++;
               FileManager.saveProgress();
               response = campMission.getStartStory();
            }
         }
         else if(randMission == COLLECT_FLORETS3)
         {  
            String favoriteColor = NPC.simpleColors[Math.abs(selected.hashCode()) % NPC.simpleColors.length];
            type = "Harvest";
            targNPC = selected;       
            if(targNPC!=null)
            {
               Item ourItem = new Item("10 "+favoriteColor+" florets", 1);
               int goldReward = 1;
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               String targName = Utilities.shortName(selected.getName());         
               for(int i=0; i<story.length; i++)
                  story[i]=story[i].replace("COLOR", favoriteColor);
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", ourItem, targName, mRow, mCol, randMission, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         else if(randMission == FIND_PET)
         {
            Item dog = new Item("dog", 25);
            String targetName = Utilities.shortName(selected.getName());
            type = "Train";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 1;         
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission trainMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", dog, targetName, mRow, mCol, FIND_PET, -1);
            selected.setMissionInfo(trainMission.getInfo());
            CultimaPanel.missionStack.add(trainMission);
            if(CultimaPanel.missionsGiven[FIND_PET]==0)
               CultimaPanel.missionsGiven[FIND_PET]++;
            FileManager.saveProgress();
            response = trainMission.getStartStory();
         }
         else if(randMission == FIND_PARENT)
         {  //rescue mission
            boolean success = TerrainBuilder.addPrisoner(loc, locType);  //add a BEGGER into loc
            String locName = loc.getName();
            if(success)
            {
               int locMapIndex = loc.getFromMapIndex();
               if(locMapIndex!=0)  
               {  //if this is a dungeon in a castle or temple, set the location to the one that leads to that dungeon
                  loc = CultimaPanel.allLocations.get(locMapIndex);
               }
               type = "Rescue";
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 1;
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = loc.getRow();
               int mCol = loc.getCol();
               TerrainBuilder.markMapPath(loc.getName());      
               Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, "none", mRow, mCol, FIND_PARENT, -1);
               selected.setMissionInfo(rescueMission.getInfo());
               CultimaPanel.missionStack.add(rescueMission);
               if(CultimaPanel.missionsGiven[FIND_PARENT]==0)
                  CultimaPanel.missionsGiven[FIND_PARENT]++;
               FileManager.saveProgress();
               response = rescueMission.getStartStory();
            }
            else
            {  //no mission
               selected.setNumInfo(0);
               return NPC.getRandomFrom(noMission);
            }
         }
         else if(randMission == DESTROY_SIGNS)
         {
            type = "Break";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;         
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            Mission breakMission = new Mission(type, story, startRow, startCol, goldReward, null, thisLoc.getName(), null, "none", mRow, mCol, DESTROY_SIGNS, -1);
            selected.setMissionInfo(breakMission.getInfo());
            CultimaPanel.missionStack.add(breakMission);
            if(CultimaPanel.missionsGiven[DESTROY_SIGNS]==0)
               CultimaPanel.missionsGiven[DESTROY_SIGNS]++;
            FileManager.saveProgress();
            response = breakMission.getStartStory();
         }
         else if(randMission == HAUNTED_HOUSE)
         {
            type = "Break";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;         
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Location thisLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            Mission breakMission = new Mission(type, story, startRow, startCol, goldReward, null, thisLoc.getName(), null, "none", mRow, mCol, HAUNTED_HOUSE, -1);
            selected.setMissionInfo(breakMission.getInfo());
            CultimaPanel.missionStack.add(breakMission);
            if(CultimaPanel.missionsGiven[HAUNTED_HOUSE]==0)
               CultimaPanel.missionsGiven[HAUNTED_HOUSE]++;
            FileManager.saveProgress();
            response = breakMission.getStartStory();
         }
         else if(randMission == DESTROY_STATUE2 && statue!=null)
         {
            targNPC = statue;
            type = "Destroy";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            if(targNPC!=null && loc!=null)
            {
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 2000;
               String targName = "statue-head";     
               targNPC.addItem(new Item(targName, goldReward));
                
               String locName = Display.provinceName(loc.getName());
            
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               mRow = loc.getRow();
               mCol = loc.getCol();
             
               TerrainBuilder.markMapPath(loc.getName());
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(targName, goldReward), targName, mRow, mCol, DESTROY_STATUE2, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[DESTROY_STATUE2]==0)
                  CultimaPanel.missionsGiven[DESTROY_STATUE2]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
         }
         else
         {
            selected.setNumInfo(0);
            return NPC.childResponse(selected, "");
         }
      }
      if(response.length() == 0)
      {
         response = NPC.childResponse(selected,"");
         selected.setNumInfo(0);
      }
      return response;
   }

   public static String beggerMission(NPCPlayer selected)
   {
      String [] noMission = {"Excuse me. I have no mission. I just wanted to greet you personally", "You seem the type that is good for a mission. Sadly, I don't know of any", "You have a mission: look for missions from others", "Did thou say mission? There must be a vex in thy matrix"};
      String response = "";
      Location loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
      int numBeggers = 0;
      boolean houseToSell = false;
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      ArrayList<NPCPlayer> inLocNPCs = Utilities.getNPCsInLoc(selected.getMapIndex());
      ArrayList<NPCPlayer> downtrodden = new ArrayList<NPCPlayer>(); 
      byte[][]currMap = (CultimaPanel.map.get(selected.getMapIndex()));
      for(NPCPlayer p:inLocNPCs)
      {
         int diff = NPC.allignmentDifference(p);
         if(p.getCharIndex()!=NPC.CHILD)
         {
            String current = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][p.getCol()])).getName().toLowerCase();
            if(current.contains("purple") && TerrainBuilder.isInsideFloor(current))
            {  //make sure there is a resident of this location that is there to sell a house
               houseToSell = true;   
            }
         }
         if(p.getCharIndex()==NPC.BEGGER && !p.getName().startsWith("Cultist"))
         {
            downtrodden.add(p);
         }
      }
      numBeggers = downtrodden.size();
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getMissionType() == RAISE_WATER)
         {
            Location thisLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(Utilities.waterInView(thisLoc))
            {  //the person was begging for help for the mission - get them on their feet when it is completed
               int restoreType = (int)(Math.random()*7);
               if(restoreType<=2)
               {
                  selected.setCharIndex(NPC.MAN);
               }
               else if(restoreType<=5)
               {
                  selected.setCharIndex(NPC.WOMAN);
               }
               else //if restoreType==6
               {
                  selected.setCharIndex(NPC.WISE);
               }   
               selected.setNumInfo(3);                         //a wise-man with a numInfo of 3 is the flag that they will teach us a spell
               selected.setMoveType(NPC.ROAM);
               int reputationPoints = 10;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getMissionType() == HAUNTED_CLEAR)
         {
            Location thisLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(LocationBuilder.countPhantoms(thisLoc)==0)
            {
               int restoreType = (int)(Math.random()*7);
               if(restoreType<=2)
               {
                  selected.setCharIndex(NPC.MAN);
               }
               else if(restoreType<=5)
               {
                  selected.setCharIndex(NPC.WOMAN);
               }
               else //if restoreType==6
               {
                  selected.setCharIndex(NPC.WISE);
               }   
               selected.setNumInfo(3);                         //a wise-man with a numInfo of 3 is the flag that they will teach us a spell
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getMissionType() == PUBLIC_HOUSING)
         {  
            Location houseLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(LocationBuilder.findOurBed(houseLoc)!=null)
            { //for each one of the downtrodden, get them on their feet and change their homeRow,homeCol to our house
               ArrayList<Coord> relocPoints = new ArrayList<Coord>();
               for(int r = 0; r < currMap.length; r++)
               {
                  for(int c = 0; c < currMap.length; c++)
                  {
                     String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                     if(current.contains("purple") && TerrainBuilder.isInsideFloor(current))
                     {  //make sure there is a resident of this location that is there to sell a house
                        relocPoints.add(new Coord(r,c));   
                     }
                  }
               }
               for(int i=0; i<downtrodden.size() && relocPoints.size()>0; i++)
               {
                  NPCPlayer p = downtrodden.get(i);
                  Coord reloc = relocPoints.remove((int)(Math.random()*relocPoints.size()));
                  p.setHomeRow(reloc.getRow());
                  p.setHomeCol(reloc.getCol());
                  int restoreType = (int)(Math.random()*7);
                  p.clearMissionInfo();
                  if(restoreType<=2)
                  {
                     p.setCharIndex(NPC.MAN);
                     p.setNumInfo(3);                         //a man with a numInfo of 3 is the flag that they will map for free
                  }
                  else if(restoreType<=5)
                  {
                     p.setCharIndex(NPC.WOMAN);
                     p.setNumInfo(3);                         //a woman with a numInfo of 3 is the flag that they will map for free
                  }
                  else //if restoreType==6
                  {
                     p.setCharIndex(NPC.WISE);
                     p.setNumInfo(3);                         //a wise-man with a numInfo of 3 is the flag that they will teach us a spell
                  }
               }
               finishMission(currMission, selected, 15);
            }
         }
         else if(currMission.getType().contains("Remove"))
         {
            boolean removed = true;
            for(NPCPlayer p:CultimaPanel.civilians.get(selected.getMapIndex()))
            {
               String pName = Utilities.shortName(p.getName());
               String targName = Utilities.shortName(currMission.getTargetHolder());
               if(pName.equalsIgnoreCase(targName) && p.getCharIndex()==NPC.BEGGER)
               {
                  removed = false;
                  break;
               }
            }
            if(removed)
            {
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Clear") && currMission.getClearLocation().length()>0)
         {                    //this is a mission to clear a location of monsters
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               ArrayList<Coord> monsterFreq = missLoc.getMonsterFreq();
               if(monsterFreq==null || monsterFreq.size()==0)  //we cleared out the monsters here
               {
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Rescue") && currMission.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               if(LocationBuilder.countPrisoners(missLoc)==0)      //we freed all the prisoners
               {
                  int reputationPoints = 10;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Find") && currMission.getTarget()!=null)
         {
            Item target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               if(Armor.getArmorWithName(target.getName())!=null)
               {  //remove armor from player and add to selected
                  Armor dropped = CultimaPanel.player.discardArmor(targetName);
                  selected.setArmor(dropped);
               }
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().equals("Save") && currMission.getClearLocation().length()>0 && currMission.getTargetHolder().equals("none"))
         {  //KILL_WEREWOLF mission
            int worldRow = currMission.getWorldRow();
            int worldCol = currMission.getWorldCol();
            Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations, worldRow, worldCol, 0);
            int mi = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, worldRow, worldCol, 0);
            boolean werewolf = CultimaPanel.werewolfAbout;
            if(!werewolf)
            {
               int reputationPoints = 10;
               finishMission(currMission, selected, reputationPoints);
            }
         }
      }  //end seeing if we finished a mission
      if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
         Item itemReward = currMission.getItemReward();
         CultimaPanel.player.addGold(goldReward);
         if(itemReward != null)
            CultimaPanel.player.addItem(itemReward.getName());
         selected.setNumInfo(Utilities.rollDie(1,3));    
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else
      {  
         int mi = CultimaPanel.player.getMapIndex();
         Location thisLoc = CultimaPanel.allLocations.get(mi);
         String locType = CultimaPanel.player.getLocationType();
         boolean hasGrayTiles = TerrainBuilder.hasGrayTiles(thisLoc);              
         boolean werewolf = CultimaPanel.werewolfAbout;    
         boolean hasWater = Utilities.waterInView(thisLoc); //there is water in or near this location
         boolean isTown = (locType.contains("city") || locType.contains("village") || locType.contains("fortress"));
         byte randMission = MOVE_BEGGER; 
         ArrayList<Byte> missionTypes = new ArrayList<Byte>();
         if(!hasWater && isTown && CultimaPanel.missionsGiven[RAISE_WATER]==0)
         {
            missionTypes.add(RAISE_WATER);
         }
         if(hasGrayTiles && CultimaPanel.missionsGiven[HAUNTED_CLEAR]==0 && selected.getMaritalStatus()=='W')
         {
            missionTypes.add(HAUNTED_CLEAR);
         }
         if(numBeggers >=2 && houseToSell && CultimaPanel.missionsGiven[PUBLIC_HOUSING]==0)
         {
            missionTypes.add(PUBLIC_HOUSING);
         }
         if(numBeggers >=2 && CultimaPanel.missionsGiven[MOVE_BEGGER]==0)
         {
            missionTypes.add(MOVE_BEGGER);
         }
         if(werewolf && CultimaPanel.missionsGiven[KILL_WEREWOLF]==0)
         {
            missionTypes.add(KILL_WEREWOLF);
         }
         if((Display.isFall() || Display.isWinter()) && CultimaPanel.missionsGiven[FIND_PELT]==0)
         {
            missionTypes.add(FIND_PELT); 
         }
         if(selected.isWerewolf() && CultimaPanel.missionsGiven[MERCY_KILL]==0)   //werewolf tag
         {
            missionTypes.add(MERCY_KILL);     
         }
         Location arena = TerrainBuilder.closeArena(CultimaPanel.allLocations);
         boolean arenaWithPrisoners = false;
         if(arena != null)
         { 
            LocationBuilder.constructAdventureAt(arena, "arena");
            if(LocationBuilder.countPrisoners(loc)>0)
            {
               arenaWithPrisoners = true;
            }
            if(arenaWithPrisoners  && CultimaPanel.missionsGiven[ARENA_RESCUE]==0)
            {
               missionTypes.add(ARENA_RESCUE);
            }
         }            
         if(CultimaPanel.missionsGiven[CLEAR_CAVE]==0)
         {
            missionTypes.add(CLEAR_CAVE);
         }
         if(missionTypes.size()==0)
         {
            if(!hasWater && isTown)
            {
               missionTypes.add(RAISE_WATER);
            }
            if(hasGrayTiles && selected.getMaritalStatus()=='W')
            {
               missionTypes.add(HAUNTED_CLEAR);
            }
            if(numBeggers >=2 && houseToSell)
            {
               missionTypes.add(PUBLIC_HOUSING);
            }
            if(numBeggers >=2)
            {
               missionTypes.add(MOVE_BEGGER);
            }
            if(arenaWithPrisoners)
            {
               missionTypes.add(ARENA_RESCUE);
            }
            missionTypes.add(CLEAR_CAVE);
            if(Display.isFall() || Display.isWinter())
            {
               missionTypes.add(FIND_PELT);
            }
            if(werewolf)
            {
               missionTypes.add(KILL_WEREWOLF);
            }
            if(selected.isWerewolf())   //werewolf tag
            {
               missionTypes.add(MERCY_KILL);  
            }      
         }        
         randMission = Utilities.getRandomFrom(missionTypes);      
                  
         //***TESTING*** if(!hasWater && isTown) randMission = Mission.RAISE_WATER;
         
         if(randMission==RAISE_WATER)
         {
            if(!hasWater)
            {                    
               type = "Cast";
               String [] temp = missions[RAISE_WATER];
               String [] story = new String[3];
               int targItemPrice = Utilities.rollDie(5, 15);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=(new String(temp[i]));
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, targItemPrice, null, thisLoc.getName(), null, "none", mRow, mCol, RAISE_WATER, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[RAISE_WATER]==0)
                  CultimaPanel.missionsGiven[RAISE_WATER]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
            else  
            {  //could not find valid monster spawn - clear the mission
               response = "";
            }
         }
         else if(randMission == HAUNTED_CLEAR)
         { 
            Location currentLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            //add phantoms
            ArrayList<Coord> spawnPoints = new ArrayList<Coord>();
            int mapIndex = selected.getMapIndex();
            for(int r = 1; r < currMap.length -1; r++)
            {
               for(int c = 1; c < currMap[r].length - 1; c++)
               {
                  String currentTile = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(currentTile.contains("floor") && Utilities.nextToAHauntedHouse(currMap, r, c, 2) && !LocationBuilder.nextToADoor(currMap, r, c))
                  {
                     spawnPoints.add(new Coord(r,c));
                  }
               }
            }
            if(spawnPoints.size() == 0)
            {
               return NPC.getRandomFrom(noMission);
            }
            int numPhantoms = Utilities.rollDie(1, spawnPoints.size()/2);
            for(int i = 0; i < numPhantoms && spawnPoints.size() > 0; i++)
            {
               Coord phantomSpawn = spawnPoints.remove((int)(Math.random()*spawnPoints.size()));
               if(!Utilities.NPCAt(phantomSpawn.getRow(), phantomSpawn.getCol(), mapIndex))
               {
                  NPCPlayer toAdd = new NPCPlayer(NPC.PHANTOM, phantomSpawn.getRow(), phantomSpawn.getCol(), mapIndex, phantomSpawn.getRow(), phantomSpawn.getCol(), locType);
                  CultimaPanel.civilians.get(mapIndex).add(toAdd);
               } 
            }
         
            type = "Clear";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            
            int goldReward = 0;
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, currentLoc.getName(), null, "", mRow, mCol, HAUNTED_CLEAR, -1);
            selected.setMissionInfo(rescueMission.getInfo());
            CultimaPanel.missionStack.add(rescueMission);
            if(CultimaPanel.missionsGiven[HAUNTED_CLEAR]==0)
               CultimaPanel.missionsGiven[HAUNTED_CLEAR]++;
            FileManager.saveProgress();
            response = rescueMission.getStartStory();
         }       
         else if(randMission == KILL_WEREWOLF)
         { 
            type = "Save";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getName());
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission werewolfMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, "none", mRow, mCol, KILL_WEREWOLF, -1);
            selected.setMissionInfo(werewolfMission.getInfo());
            CultimaPanel.missionStack.add(werewolfMission);
            if(CultimaPanel.missionsGiven[KILL_WEREWOLF]==0)
               CultimaPanel.missionsGiven[KILL_WEREWOLF]++;
            FileManager.saveProgress();
            response = werewolfMission.getStartStory();
         }
         else if(randMission == Mission.PUBLIC_HOUSING)
         {  
            type = "Save";               
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("PLAYER_NAME", ""+CultimaPanel.player.getShortName());
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, "none", mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         }
         else if(randMission == MOVE_BEGGER)
         { 
            NPCPlayer targetBegger = null;
            for(NPCPlayer p:Utilities.getNPCsInLoc(selected.getMapIndex()))
            {
               if(!p.getName().toLowerCase().contains(selected.getName().toLowerCase()) && p.getCharIndex()==NPC.BEGGER)
               {
                  targetBegger = p;
                  break;
               }
            }
            String targName = Utilities.shortName(targetBegger.getName());
            type = "Remove";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("NPC_NAME", Utilities.shortName(targetBegger.getName()));
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            TerrainBuilder.markMapPath(loc.getName());      
            Mission removeMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, targName, mRow, mCol, MOVE_BEGGER, -1);
            selected.setMissionInfo(removeMission.getInfo());
            CultimaPanel.missionStack.add(removeMission);
            if(CultimaPanel.missionsGiven[MOVE_BEGGER]==0)
               CultimaPanel.missionsGiven[MOVE_BEGGER]++;
            FileManager.saveProgress();
            response = removeMission.getStartStory();
         }
         else if(randMission == CLEAR_CAVE)
         {
            String [] locTypes = {"cave"};
            loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
            if(loc==null || loc.getMonsterFreq().size() == 0)
            {
               selected.setNumInfo(Utilities.rollDie(1,3));
               return  Utilities.getRandomFrom(NPC.mainMission)+".";
            }         
            TerrainBuilder.markMapPath(loc.getName());
            type = "Clear";
            ArrayList<Coord> monsterFreq = loc.getMonsterFreq();
            byte monsterIndex = (byte)((monsterFreq.get(0)).getRow());
            String monsterType = NPC.characterDescription(monsterIndex);
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("MONSTER_TYPE", monsterType);
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               story[i]=story[i].replace("LOCATION_NAME", loc.getName());
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            mRow = loc.getRow();
            mCol = loc.getCol();
            Mission clearMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, "none", mRow, mCol, CLEAR_CAVE, -1);
            selected.setMissionInfo(clearMission.getInfo());
            CultimaPanel.missionStack.add(clearMission);
            if(CultimaPanel.missionsGiven[CLEAR_CAVE]==0)
               CultimaPanel.missionsGiven[CLEAR_CAVE]++;
            FileManager.saveProgress();
            response = clearMission.getStartStory();
         }
         else if(randMission == Mission.ARENA_RESCUE && arena!=null)
         {        //rescue mission
            loc = arena;
            int numPrisoners = LocationBuilder.countPrisoners(loc);
            if(numPrisoners > 0)
            {
               type = "Rescue";
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 0;
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = loc.getRow();
               int mCol = loc.getCol();
               TerrainBuilder.markMapPath(loc.getName());      
               Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, "none", mRow, mCol, ARENA_RESCUE, -1);
               selected.setMissionInfo(rescueMission.getInfo());
               CultimaPanel.missionStack.add(rescueMission);
               if(CultimaPanel.missionsGiven[ARENA_RESCUE]==0)
                  CultimaPanel.missionsGiven[ARENA_RESCUE]++;
               FileManager.saveProgress();
               response = rescueMission.getStartStory();
            }
         }
         else if(randMission == Mission.FIND_PELT)
         {  
            type = "Find";
            String targName = Utilities.shortName(selected.getName());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            Armor randPelt = Armor.getRandomPelt();
            String peltType = randPelt.getName();
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("ITEM_NAME", peltType);
            }             
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", new Item(peltType, 30), targName, mRow, mCol, FIND_PELT, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[FIND_PELT]==0)
               CultimaPanel.missionsGiven[FIND_PELT]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         } 
         else if(randMission==MERCY_KILL)
         {
            type = "Mercy";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            String targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            String locName = Display.provinceName(loc.getName());
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, targName, mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         }
      
      }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(NPC.mainMission)+".";
         selected.setNumInfo(Utilities.rollDie(1,3));
      }
      return response;
   }

   public static String luteMission(NPCPlayer selected)
   {
      String [] noMission = {"Excuse me. I have no mission. I just wanted to greet you personally", "You seem the type that is good for a mission. Sadly, I don't know of any", "You have a mission: look for missions from others", "Did thou say mission? There must be a vex in thy matrix"};
   
      String response = "";
      Location loc = null;
      Item target = null;
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getMissionType() == REMOVE_CULT)
         {
            Location thisLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(LocationBuilder.countCultists(thisLoc)==0)
            {
               finishMission(currMission, selected, 5);
            }
         }
         else if(currMission.getType().contains("Find") && currMission.getTarget()!=null)
         {
            target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               if(targetName.toLowerCase().contains("lute"))
               {
                  if(Weapon.getWeaponWithName(target.getName())!=null)
                  {   //remove weapon from player and add to selected
                     Weapon dropped = CultimaPanel.player.discardWeapon(targetName);
                     selected.setWeapon(dropped);
                  }
               }
               else
               {
                  CultimaPanel.player.removeItem(target.getName());
                  selected.addItem(target);
               }
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
      }
      if(selected.getNumInfo() != 10)
         response = Utilities.getRandomFrom(NPC.noMission)+".";
      else if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
         Item itemReward = currMission.getItemReward();
         CultimaPanel.player.addGold(goldReward);
         if(itemReward != null)
            CultimaPanel.player.addItem(itemReward.getName());
         selected.setNumInfo(Utilities.rollDie(1,3));
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else
      {  //FIND_SONG, FIND_LUTE, REMOVE_CULT
         String currLoc = selected.getLocationType().toLowerCase();
         boolean isCity = (currLoc.contains("city") || currLoc.contains("port") || currLoc.contains("fortress"));
         int mi = CultimaPanel.player.getMapIndex();
         Location thisLoc = CultimaPanel.allLocations.get(mi);
         byte [][] currMap = CultimaPanel.map.get(mi);
         boolean hasGrayTiles = TerrainBuilder.hasGrayTiles(thisLoc);
         boolean hauntedMission = false;
         for(int i=CultimaPanel.missionStack.size()-1; i>=0; i--)
         {  //make sure we don't have more than one mission active where we have to find an item in a haunted house
            Mission m = CultimaPanel.missionStack.get(i);
            if(m.getMissionType()==Mission.FIND_LEDGER2)
            {
               hauntedMission = true;
               break;
            }
         }
         int centerRow = currMap.length/2;
         int centerCol = currMap[0].length/2;
         ArrayList<Coord>spawnCoords = new ArrayList<Coord>();
         //to see if we can do the remove_cult mission, we need at least 3 spawnable points within the 4x4 square in the city center
         for(int cr = centerRow-2; cr<=centerRow+2; cr++)
            for(int cc = centerCol-2; cc<=centerCol+2; cc++)
               if(LocationBuilder.canMoveTo(currMap, cr, cc) && !TerrainBuilder.onWater(currMap, cr, cc))
                  spawnCoords.add(new Coord(cr, cc));
         byte randMission = FIND_LUTE;
         ArrayList<Byte>missionTypes = new ArrayList<Byte>();
         //fill list of missions to pick from with the ones we have not seen yet
         if(CultimaPanel.missionsGiven[HAUNTED_FIND]==0 && hasGrayTiles && !hauntedMission)
         {
            missionTypes.add(HAUNTED_FIND);
         }
         if(CultimaPanel.missionsGiven[REMOVE_CULT]==0 && spawnCoords.size() >= 3 && isCity && selected.getReputation() <= 10)
         {
            missionTypes.add(REMOVE_CULT);
         }
         if(CultimaPanel.missionsGiven[FIND_SONG]==0)
         {
            missionTypes.add(FIND_SONG);
         }
         if(CultimaPanel.missionsGiven[FIND_LUTE]==0 && !((selected.getWeapon().getName()).toLowerCase()).contains("destiny"))
         {
            missionTypes.add(FIND_LUTE);
         }
         if(selected.isWerewolf() && CultimaPanel.missionsGiven[MERCY_KILL]==0)   //werewolf tag
         {
            missionTypes.add(MERCY_KILL);
         }
         if(missionTypes.size()==0)    //we have seen all the mission types before, so pick a random one
         {
            if(hasGrayTiles && !hauntedMission)
            {
               missionTypes.add(HAUNTED_FIND);
            }
            if(spawnCoords.size() >= 3 && isCity && selected.getReputation() <= 10)
            {
               missionTypes.add(REMOVE_CULT);
            }
            missionTypes.add(FIND_SONG);
            if(!((selected.getWeapon().getName()).toLowerCase()).contains("destiny"))
            {
               missionTypes.add(FIND_LUTE);
            }
            if(selected.isWerewolf())   //werewolf tag
               missionTypes.add(MERCY_KILL);
         }
         randMission = Utilities.getRandomFrom(missionTypes);
         
         //***TESTING**** randMission = HAUNTED_FIND;
         
         if(randMission == REMOVE_CULT)
         { 
            Location currentLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               //add cultists
            int mapIndex = selected.getMapIndex();
            int numWorshipers = Math.min(spawnCoords.size(), Utilities.rollDie(2,4));
            ArrayList<NPCPlayer> worshipers = new ArrayList();
            for(int i = 0; i < numWorshipers; i++)
            {
               Coord spawnCoord = spawnCoords.remove((int)(Math.random()*spawnCoords.size()));
               int row = spawnCoord.getRow();
               int col = spawnCoord.getCol();
               if(Utilities.getNPCAt(row, col, mapIndex)==null)
               {
                  NPCPlayer p = new NPCPlayer(NPC.BEGGER, row, col, mapIndex, row, col, selected.getLocationType());
                  p.setName("Cultist "+p.getNameSimple());
                  p.setNumInfo(0);
                  worshipers.add(p);
               }
            }
            for(NPCPlayer p:worshipers)
            {
               CultimaPanel.civilians.get(mapIndex).add(p);
            }
         
            type = "Remove";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("ADJECTIVE", ""+NPC.getRandomFrom(NPC.insultAdjective));
            }
            int goldReward = Utilities.rollDie(5,15);
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Mission rescueMission = new Mission(type, story, startRow, startCol, goldReward, null, currentLoc.getName(), null, "", mRow, mCol, REMOVE_CULT, -1);
            selected.setMissionInfo(rescueMission.getInfo());
            CultimaPanel.missionStack.add(rescueMission);
            if(CultimaPanel.missionsGiven[REMOVE_CULT]==0)
               CultimaPanel.missionsGiven[REMOVE_CULT]++;
            FileManager.saveProgress();
            response = rescueMission.getStartStory();
         }
         else if(randMission==MERCY_KILL)
         {
            type = "Mercy";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            String targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            String locName = Display.provinceName(loc.getName());
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, targName, mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         } 
         else if(randMission==HAUNTED_FIND)
         {
            type = "Find";
            String locType = CultimaPanel.player.getLocationType();
            target = new Item("songbook",25);
            //place a songbook in a haunted house
            ArrayList<Coord> spawnPoints = new ArrayList<Coord>();
            int mapIndex = selected.getMapIndex();
            for(int r = 1; r < currMap.length -1; r++)
            {
               for(int c = 1; c < currMap[r].length - 1; c++)
               {
                  String currentTile = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(currentTile.contains("floor") && Utilities.nextToAHauntedHouse(currMap, r, c, 1) && !LocationBuilder.nextToADoor(currMap, r, c))
                  {
                     spawnPoints.add(new Coord(r,c));
                  }
               }
            }
            if(spawnPoints.size() == 0)
            {
               return NPC.getRandomFrom(noMission);
            }
            Coord bookSpawn = spawnPoints.remove((int)(Math.random()*spawnPoints.size()));
            currMap[bookSpawn.getRow()][bookSpawn.getCol()] = TerrainBuilder.getTerrainWithName("ITM_D_$Book").getValue();
            int numPhantoms = Utilities.rollDie(1, spawnPoints.size()/2);
            for(int i = 0; i < numPhantoms && spawnPoints.size() > 0; i++)
            {
               Coord phantomSpawn = spawnPoints.remove((int)(Math.random()*spawnPoints.size()));
               if(!Utilities.NPCAt(phantomSpawn.getRow(), phantomSpawn.getCol(), mapIndex))
               {
                  NPCPlayer toAdd = new NPCPlayer(NPC.PHANTOM, phantomSpawn.getRow(), phantomSpawn.getCol(), mapIndex, phantomSpawn.getRow(), phantomSpawn.getCol(), locType);
                  CultimaPanel.civilians.get(mapIndex).add(toAdd);
               } 
            }
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(3, 15);
            double priceShift = ((Math.random()*51) - 25)/100.0;  //-.25 to +.25
            goldReward = (int)(goldReward + (goldReward * priceShift));               
            if(CultimaPanel.shoppeDiscount)   //charmed
               goldReward = (int)(goldReward * 1.20);
            String locName = Display.provinceName(thisLoc.getName());
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("GOLD_REWARD", ""+goldReward).replace("ADJECTIVE", NPC.getRandomFrom(NPC.insultAdjective));
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = thisLoc.getRow(), mCol = thisLoc.getCol();
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target.getName(),target.getValue()), "none", mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         } 
         else
         {
            type = "Find";
            String locType = "place";
            loc = thisLoc;
            int randType = Utilities.rollDie(10);
            ArrayList<NPCPlayer> inLocNPCs = Utilities.getNPCsInLoc(selected.getMapIndex());
            ArrayList<NPCPlayer> lutes = new ArrayList<NPCPlayer>();
            for(NPCPlayer p: inLocNPCs)
            {
               if(p.getCharIndex()==NPC.LUTE && !p.getName().equals(selected.getName()))
                  lutes.add(p);
               if(randMission == FIND_SONG && (p.getCharIndex()==NPC.WISE || p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN || p.getCharIndex()==NPC.KING))
                  lutes.add(p);  
            }
            NPCPlayer targNPC = null;
            if(lutes.size() <= 1)
               randType = Utilities.rollDie(2,10);
            if(randType < 2)           //lute goes to someone at the same location
            {
               loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               locType = CultimaPanel.player.getLocationType();
               if(lutes.size() == 0)
                  targNPC = null;
               else
                  targNPC = Utilities.getRandomFrom(lutes);
            }
            if((randType >=2 && randType <= 5) || targNPC==null)      //lute goes to someone at the next closest castle
            {
               String [] locTypes = {"castle","tower"};
               Location closeCastle = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
               inLocNPCs = Utilities.getNPCsInLoc(closeCastle.getMapIndex());
               lutes = new ArrayList<NPCPlayer>();
               for(NPCPlayer p: inLocNPCs)
               {
                  if(p.getCharIndex()==NPC.LUTE)
                     lutes.add(p);
                  if(randMission == FIND_SONG && (p.getCharIndex()==NPC.WISE || p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN || p.getCharIndex()==NPC.KING))
                     lutes.add(p); 
               }
               if(lutes.size() > 0)
               {
                  targNPC = Utilities.getRandomFrom(lutes);
                  loc = closeCastle;
               }
            }
            if(targNPC==null)                       //lute goes to  someone at the next closest town
            {
               String []locTypes2 = {"city","fortress","port","village"};
               Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
               inLocNPCs = Utilities.getNPCsInLoc(closeCity.getMapIndex());
               lutes = new ArrayList<NPCPlayer>();
               for(NPCPlayer p: inLocNPCs)
               {
                  if(p.getCharIndex()==NPC.LUTE)
                     lutes.add(p);
                  if(randMission == FIND_SONG && (p.getCharIndex()==NPC.WISE || p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN || p.getCharIndex()==NPC.KING))
                     lutes.add(p);
               }
               if(lutes.size() > 0)
               {
                  targNPC = Utilities.getRandomFrom(lutes);
                  loc = closeCity;
               }
            }
            if(randMission == FIND_SONG)
               target = new Item("Songpage",500);
            else
               target =  Weapon.getLuteOfDestiny(); 
          
            if(targNPC!=null && loc!=null)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = target.getValue()/10;
               double priceShift = ((Math.random()*51) - 25)/100.0;  //-.25 to +.25
               goldReward = (int)(goldReward + (goldReward * priceShift));
            
               if(randMission == FIND_SONG)
                  targNPC.addItem(target);
               else
                  targNPC.setWeapon((Weapon)(target));
            
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(targNPC.getName());         
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
                  story[i]=story[i].replace("ITEM_NAME", target.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(targNPC.getMapIndex() != selected.getMapIndex())
               {
                  mRow = loc.getRow();
                  mCol = loc.getCol();
               }
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target.getName(),target.getValue()), targName, mRow, mCol, (byte)randMission, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            } 
         }   
      }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(NPC.mainMission)+".";
         selected.setNumInfo(0);
      }
      return response;
   }

   public static String wiseMission(NPCPlayer selected)
   {//make sure ceremony, marry missions are only in cities/port towns
      String [] noMission = {"Excuse me. I have no mission. I just wanted to greet you personally", "You seem the type that is good for a mission. Sadly, I don't know of any", "You have a mission: look for missions from others", "Did thou say mission? There must be a vex in thy matrix"};
      String response = "";
      Location loc = null;
      Item target = null;
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;
      int currMissionIndex = -1;
        
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         if(currMission.getMissionType() == COLLECT_FLORETS2 && CultimaPanel.player.enoughFlowers()) 
         {
            CultimaPanel.player.clearFlowers();
            finishMission(currMission, selected, 5);
         }
         else if(currMission.getMissionType() == CLOSE_HELLMOUTH)
         {
            Location thisLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(!LocationBuilder.hasHellMouth(thisLoc))
            {
               finishMission(currMission, selected, 10);
            }
         }
         else if(currMission.getType().contains("Teach"))
         {
            if(currMission.getMissionType() == TEACH_3_DOORS)
            {
               if(CultimaPanel.player.getDoorsPuzzleInfo()>=NPC.doorsPuzzleInfo.length && CultimaPanel.player.stats[Player.PUZZLES_SOLVED] >= 1)
               {
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
            else if(currMission.getTarget() != null)
            {
               Item thisTarget = currMission.getTarget();
               String targetName = thisTarget.getName(); 
               if(CultimaPanel.player.hasSpell(targetName) || CultimaPanel.player.hasItem(targetName))
               {
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            } 
         }
         else if(currMission.getType().contains("Ceremony"))
         {
            Item thisTarget = currMission.getTarget();
            String targetName = thisTarget.getName();
            boolean hasHead = (CultimaPanel.player.hasItem(targetName));
            Location thisLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            int numCultists = LocationBuilder.countCultists(thisLoc); 
            int numMonsters = LocationBuilder.countMonsters(thisLoc);
            int numStatues = LocationBuilder.countStatues(thisLoc);  
            if(hasHead || (numCultists==0 && numStatues > 0 && numMonsters == 0))
            {
               CultimaPanel.numChants = -1;
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if((currMission.getType().contains("Marry")) && currMission.getTargetHolder()!=null && currMission.getTargetHolder().length()>0)        //remove beggers from a town, or help them to be upright citizens
         {
            int worldRow = currMission.getWorldRow();
            int worldCol = currMission.getWorldCol();
            Location thisLoc = CultimaPanel.getLocation(CultimaPanel.allLocations, worldRow, worldCol, 0);
            int mi = CultimaPanel.getLocationIndex(CultimaPanel.allLocations, worldRow, worldCol, 0);
            boolean married = false;
            NPCPlayer ourSpouse = null;
            for(NPCPlayer p:CultimaPanel.civilians.get(mi))
            {
               String pName = Utilities.shortName(p.getName());   //starting the name with + means we are married to them
               String targName = Utilities.shortName(currMission.getTargetHolder());
               if(pName.equalsIgnoreCase("+"+targName))
               {
                  married = true;
                  ourSpouse = p;
                  break;
               }
            }
            if(married && ourSpouse!=null)
            {
               CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
               //give us a house
               ArrayList<Coord> houseLocs = NPC.buyHouse();
               NPCPlayer currOwner = null;
               for(NPCPlayer p:CultimaPanel.civilians.get(mi))
               {
                  Coord currOwnerHome = new Coord(p.getHomeRow(),p.getHomeCol());
                  if(houseLocs.contains(currOwnerHome))
                  {
                     currOwner = p;
                     break;
                  }
               }
               if(currOwner != null)
               {
                  ArrayList<Coord> newLocs = new ArrayList<Coord>();
                  byte[][]currMap = (CultimaPanel.map.get(selected.getMapIndex()));   
                  for(int r=0; r<currMap.length; r++)
                     for(int c=0; c<currMap[0].length; c++)
                     {
                        String current = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                        if(!current.contains("purple") && TerrainBuilder.isInsideFloor(current))
                           newLocs.add(new Coord(r, c));   
                     }
                  if(newLocs.size() > 0)              //move old resident to a new place
                  {
                     Coord newHomeLoc = Utilities.getRandomFrom(newLocs);
                     currOwner.setHomeRow(newHomeLoc.getRow());
                     currOwner.setHomeCol(newHomeLoc.getCol());
                  }
               }
              //give us a horse
               int horseRow=CultimaPanel.player.getWorldRow(); 
               int horseCol=CultimaPanel.player.getWorldCol();
               java.util.LinkedList<Teleporter> telepMemory = CultimaPanel.player.getMemory();
               if(telepMemory.size() == 1)
               {  //our getWorldRow and Col might not be updated if we resumed while already in the location
                  //so retrieve it from teleporter memory
                  Teleporter telep = telepMemory.get(0);
                  if(telep.toIndex()==0)
                  {
                     horseRow = telep.toRow();
                     horseCol = telep.toCol();
                  }
               }
               ArrayList<Coord>horseLocs = new ArrayList();
               for(int r=horseRow-3; r<=horseRow+3; r++)
                  for(int c=horseCol-3; c<=horseCol+3; c++)
                  {
                     String terrain = CultimaPanel.allTerrain.get(Math.abs((CultimaPanel.map.get(0))[r][c])).getName();
                     if((r==horseRow && c==horseCol) || Utilities.NPCAt(r, c, 0) || terrain.startsWith("LOC_") || terrain.contains("_I_") || terrain.toLowerCase().contains("water"))
                        continue;
                     horseLocs.add(new Coord(CultimaPanel.equalizeWorldRow(r),CultimaPanel.equalizeWorldRow(c)));
                  }
               if(horseLocs.size() > 0)
               {
                  Coord horseSpawn = Utilities.getRandomFrom(horseLocs);
                  horseRow=horseSpawn.getRow(); 
                  horseCol=horseSpawn.getCol();
                  NPCPlayer ourHorse = new NPCPlayer(NPC.HORSE, horseRow, horseCol, 0, "world");
                  ourHorse.setHasMet(true);
                  CultimaPanel.worldMonsters.add(ourHorse);
               } 
               int reputationPoints = 0;
               finishMission(currMission, selected, reputationPoints);  
            }
         }
         else if(currMission.getType().contains("Clear") && currMission.getClearLocation().length()>0)
         {                    //this is a mission to clear a location of monsters
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               ArrayList<Coord> monsterFreq = missLoc.getMonsterFreq();
               if(monsterFreq==null || monsterFreq.size()==0)  //we cleared out the monsters here
               {
                  TerrainBuilder.addWorshipersToTemple(missLoc);
                  int reputationPoints = 5;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Find"))
         {
            target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               if(Weapon.getWeaponWithName(target.getName())!=null)
               {   //remove weapon from player and add to selected
                  Weapon dropped = CultimaPanel.player.discardWeapon(targetName);
                  selected.setWeapon(dropped);
               }
               else if(Armor.getArmorWithName(target.getName())!=null)
               {  //remove armor from player and add to selected
                  Armor dropped = CultimaPanel.player.discardArmor(targetName);
                  selected.setArmor(dropped);
               }
               else
               {
                  CultimaPanel.player.removeItem(target.getName());
                  selected.addItem(target);
               }
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Assassinate") && currMission.getTarget()!=null)
         {
            target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
               if(currMission.getType().contains("Assassinate"))
                  CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
               CultimaPanel.player.removeItem(target.getName());
               if(!target.getName().contains("bounty"))
                  selected.addItem(target);
               Item reward = currMission.getItemReward();
               if(reward!=null)
               {
                  selected.removeItem(reward.getName());
                  CultimaPanel.player.addItem(reward.getName());
                  CultimaPanel.sendMessage("<Recieved "+reward.getName()+">");
               }   
               int reputationPoints = 0;
               finishMission(currMission, selected, reputationPoints);
            }
            else if(CultimaPanel.player.hasItem("bounty"))
            {  //if we just have an item called bounty and there is only one bounty mission, resolve it.
               int count = 0;
               for(Mission m:CultimaPanel.missionStack)
               {
                  if(m.getType().contains("Assassinate"))
                     count++;
               }
               if (count==1)
               {
                  CultimaPanel.missionTargetNames.remove(currMission.targetHolder);
                  if(currMission.getType().contains("Assassinate"))
                     CultimaPanel.player.stats[Player.BOUNTIES_COLLECTED]++;
                  CultimaPanel.player.removeItem("bounty");
                  Item reward = currMission.getItemReward();
                  if(reward!=null)
                  {
                     selected.removeItem(reward.getName());
                     CultimaPanel.player.addItem(reward.getName());
                     CultimaPanel.sendMessage("<Recieved "+reward.getName()+">");
                  }
                  int reputationPoints = 0;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Purge") && currMission.getClearLocation().length()>0)
         {
            Location missLoc = CultimaPanel.getLocation(currMission.getClearLocation());
            if(missLoc!=null)
            {
               int numCivs = LocationBuilder.countCivilians(missLoc);
               if(numCivs==0)      //we killed or scared out all the inhabitants
               {
                  int reputationPoints = -10;
                  finishMission(currMission, selected, reputationPoints);
               }
            }
         }
         else if(currMission.getType().contains("Harvest") && currMission.getTarget()!=null)
         {   //check to see if harvest egg mission is finished   
            Item target2 = currMission.getTarget();
            String targetName = target2.getName().toLowerCase();
            if(targetName.contains("3-serpent-eggs") && CultimaPanel.player.numSerpentEggs()>=3)
            {
               for(int i=0; i<3; i++)
               {
                  CultimaPanel.player.removeItem("serpent-egg");
                  selected.addItem(Item.getSerpentEgg());
               }
               int reputationPoints = 5;
               finishMission(currMission, selected, reputationPoints);
            }            
         }
      }     //end see if we completed a mission
      if(selected.getNumInfo() != 10)
         response = Utilities.getRandomFrom(NPC.noMission)+".";
      else if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
         Item itemReward = currMission.getItemReward();
         CultimaPanel.player.addGold(goldReward);
         if(itemReward != null)
            CultimaPanel.player.addItem(itemReward.getName());
         selected.setNumInfo(3);    //setting a man, woman or wise value to 3 will make it so they will map for you for free, or teach you a spell as thanks
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else     //create the mission
      {  
         String [] locTypes = {"castle","tower"};
         Location closeCastle = null;
         Location currentLoc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
         boolean hasGrayTiles = TerrainBuilder.hasGrayTiles(currentLoc);
         String thisLoc = selected.getLocationType().toLowerCase();
         boolean isCity = (thisLoc.contains("city") || thisLoc.contains("port") || thisLoc.contains("fortress"));
         if(thisLoc.contains("castle") || thisLoc.contains("tower"))
            closeCastle =  CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
         else
            closeCastle = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
         ArrayList<NPCPlayer> inLocNPCs = Utilities.getNPCsInLoc(closeCastle.getMapIndex());
         NPCPlayer king = null;
         if(inLocNPCs.size() >= 1)
         {
            for(NPCPlayer p:inLocNPCs)
               if(p.getCharIndex()==NPC.KING)
               {
                  if(!p.hasMet())
                     p.setReputation(-1000);
                  if(p.getReputation() < 0)
                  {
                     king = p;
                     break;
                  }
               } 
         }
         inLocNPCs = Utilities.getNPCsInLoc(selected.getMapIndex());
         NPCPlayer single = null;
         if(inLocNPCs.size() >= 2)
         {
            boolean houseToSell = false;
            for(NPCPlayer p:inLocNPCs)
            {
               int diff = NPC.allignmentDifference(p);
               if(p.getCharIndex()!=NPC.CHILD)
               {
                  byte[][]currMap = (CultimaPanel.map.get(selected.getMapIndex()));
                  String current = CultimaPanel.allTerrain.get(Math.abs(currMap[p.getRow()][p.getCol()])).getName().toLowerCase();
                  if(current.contains("purple") && TerrainBuilder.isInsideFloor(current))
                  {  //make sure there is a resident of this location that is there to sell a house
                     houseToSell = true;   
                  }
               }
               if(p.getCharIndex()==NPC.MAN || p.getCharIndex()==NPC.WOMAN)
               {
                  if((CultimaPanel.player.getReputation() < 0 && diff > 100 && p.getReputation() > CultimaPanel.player.getReputation()))
                     continue;
                  if(p.isShopkeep() || p.swinePlayer())
                     continue;   
                  if(p.getMaritalStatus() == 'S')
                  {
                     single = p;
                  }
               } 
            }
            if(!houseToSell)
            {
               single = null;   
            }
         }
         //of the spells we may be asked to discover and teach, find out which ones we don't already know
         String [] spellNames = {"Curse","Spidersweb","Tempest","Firestorm","Floretlade","Timestop"};
         ArrayList<String> unknownSpells = new ArrayList<String>();
         for(String spell:spellNames)
            if(!CultimaPanel.player.hasItem(spell) && !CultimaPanel.player.hasSpell(spell))
               unknownSpells.add(spell);
         
         ArrayList<Coord>waterLocs =  Utilities.waterInView();
         
         String [] templeType = {"temple"};
         Location closeTemple = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, templeType);
         boolean thereIsCaerbannog = (CultimaPanel.caerbannogRow!=-1 && CultimaPanel.caerbannogCol!=-1);
         double distToCaerbannog = Double.MAX_VALUE;
         if(thereIsCaerbannog)
         {
            distToCaerbannog = Display.wrapDistance(CultimaPanel.caerbannogRow, CultimaPanel.caerbannogCol, CultimaPanel.player.getWorldRow(), CultimaPanel.player.getWorldCol());
         }
         byte randMission = CLEAR_TEMPLE;      
         boolean inCapital = Utilities.isCapitalCity(selected.getMapIndex());
      
         ArrayList<Byte>missionTypes = new ArrayList<Byte>();
         //fill list of missions to pick from with the ones we have not seen yet
         if(CultimaPanel.missionsGiven[COLLECT_FLORETS2]==0 && !Display.isWinter()) 
         {
            missionTypes.add(COLLECT_FLORETS2);
         }
         if(CultimaPanel.missionsGiven[CLOSE_HELLMOUTH]==0 && hasGrayTiles) 
            missionTypes.add(CLOSE_HELLMOUTH);
         if(CultimaPanel.missionsGiven[FIND_CAERBANNOG]==0 && distToCaerbannog<=100) 
            missionTypes.add(FIND_CAERBANNOG);
         if(CultimaPanel.missionsGiven[CEREMONY]==0 && closeTemple != null && !inCapital && isCity) 
            missionTypes.add(CEREMONY);
         if(CultimaPanel.missionsGiven[TEACH_SPELL]==0 && unknownSpells.size() > 0)
            missionTypes.add(TEACH_SPELL);
         if(CultimaPanel.missionsGiven[MARRIAGE]==0 && single!=null && isCity && (selected.getMaritalStatus()=='M' || selected.getMaritalStatus()=='W'))
            missionTypes.add(MARRIAGE);
         if(CultimaPanel.missionsGiven[CLEAR_TEMPLE]==0 && closeTemple != null)
            missionTypes.add(CLEAR_TEMPLE);
         if(CultimaPanel.missionsGiven[TEACH_3_DOORS]==0)
            missionTypes.add(TEACH_3_DOORS);
         if(CultimaPanel.missionsGiven[FIND_PEARL]==0 && !Display.frozenWater() && waterLocs.size() >= 5)
            missionTypes.add(FIND_PEARL);
         if(CultimaPanel.missionsGiven[ASSASSINATE_KING]==0 && closeCastle!=null && king !=null)
            missionTypes.add(ASSASSINATE_KING);  
         if(CultimaPanel.missionsGiven[PURGE_HOLY]==0 && selected.getReputation() <= 0)
            missionTypes.add(PURGE_HOLY);   
         if(CultimaPanel.missionsGiven[FIND_MAGIC_ITEM]==0)
            missionTypes.add(Mission.FIND_MAGIC_ITEM);   
         if(CultimaPanel.missionsGiven[FIND_SERPENT_EGGS]==0 && selected.isShopkeep() && selected.getName().toLowerCase().contains("mage"))
            missionTypes.add(FIND_SERPENT_EGGS);  
         if(missionTypes.size()==0)    //we have seen all the mission types before, so pick a random one
         {
            missionTypes.add(TEACH_3_DOORS);
            if(!Display.isWinter()) 
            {
               missionTypes.add(COLLECT_FLORETS2);
            }
            if(hasGrayTiles) 
               missionTypes.add(CLOSE_HELLMOUTH);
            if(distToCaerbannog<=100) 
               missionTypes.add(FIND_CAERBANNOG);
            if(unknownSpells.size() > 0)
               missionTypes.add(TEACH_SPELL);
            if(single!=null && isCity && (selected.getMaritalStatus()=='M' || selected.getMaritalStatus()=='W'))
               missionTypes.add(MARRIAGE);
            if(closeTemple != null)
            {   
               missionTypes.add(CLEAR_TEMPLE);
               if(!inCapital && isCity)
               {
                  missionTypes.add(CEREMONY);
               }
            }
            missionTypes.add(Mission.FIND_MAGIC_ITEM);
            if(!Display.frozenWater() && waterLocs.size() >= 5 && Math.random() < 0.25)
               missionTypes.add(FIND_PEARL);
            if(closeCastle!=null && king !=null && Math.random() < 0.5)
               missionTypes.add(ASSASSINATE_KING);   
            if(selected.getReputation() <= 0)
               missionTypes.add(PURGE_HOLY);  
            if(selected.isShopkeep() && selected.getName().toLowerCase().contains("mage"))
               missionTypes.add(FIND_SERPENT_EGGS);   
         }
         randMission = Utilities.getRandomFrom(missionTypes);
         
         //***TESTING*** randMission = CEREMONY;
         
         if(randMission == COLLECT_FLORETS2)
         {  
            type = "Harvest";
            NPCPlayer targNPC = selected;       
            if(targNPC!=null)
            {
               Item ourItem = new Item("florets", 3);
               int goldReward = Utilities.rollDie(5, 15);
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(selected.getName());         
               for(int i=0; i<story.length; i++)
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", ourItem, targName, mRow, mCol, randMission, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         
         else if(randMission == CLOSE_HELLMOUTH)
         {
            type = "Clear";
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = Utilities.rollDie(5, 15);         
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1;
            int mCol = -1;
            Location thisLocation = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            ArrayList<Coord> spawnPoints1 = new ArrayList<Coord>();  //try to find spawnpoints in the middle of the house
            ArrayList<Coord> spawnPoints2 = new ArrayList<Coord>();  //if we couldn't find any (small house), take any floor even if next to a wall or door
            int mapIndex = CultimaPanel.player.getMapIndex();
            byte[][]currMap = (CultimaPanel.map.get(mapIndex));
            for(int r = 1; r < currMap.length -1; r++)
            {
               for(int c = 1; c < currMap[r].length - 1; c++)
               {
                  String currentTile = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(currentTile.contains("floor")  && !currentTile.contains("brick") && Utilities.nextToAHauntedHouse(currMap, r, c, 2))
                  {
                     if(!Utilities.nextToAHauntedHouse(currMap, r, c, 1) && !LocationBuilder.nextToADoor(currMap, r, c))
                     {  //best spawn points (in the middle of the house, away from the door)
                        spawnPoints1.add(new Coord(r,c));   
                     }
                     spawnPoints2.add(new Coord(r,c));   //all house spawnpoints
                  }
               }
            }
            if(spawnPoints1.size() == 0 && spawnPoints2.size() == 0)
            {
               return NPC.getRandomFrom(noMission);
            }
            Coord mouthSpawn = null;
            if(spawnPoints1.size() > 0)
            {
               mouthSpawn = Utilities.getRandomFrom(spawnPoints1);
            }
            else
            {
               mouthSpawn = Utilities.getRandomFrom(spawnPoints2);
            }
            currMap[mouthSpawn.getRow()][mouthSpawn.getCol()] = TerrainBuilder.getTerrainWithName("TER_I_L_3_$Lava").getValue();
            for(int r=mouthSpawn.getRow()-1; r<=mouthSpawn.getRow()+1; r++)
            {
               for(int c=mouthSpawn.getCol()-1; c<=mouthSpawn.getCol()+1; c++)
               {
                  String currentTile = CultimaPanel.allTerrain.get(Math.abs(currMap[r][c])).getName().toLowerCase();
                  if(currentTile.contains("floor") && !LocationBuilder.nextToADoor(currMap, r, c))
                  {  //make the floor around the hellmouth charred 
                     currMap[r][c] = TerrainBuilder.getTerrainWithName("INR_$Black_floor").getValue();
                  }
               }
            }
            Mission breakMission = new Mission(type, story, startRow, startCol, goldReward, null, thisLocation.getName(), null, "none", mRow, mCol, CLOSE_HELLMOUTH, -1);
            selected.setMissionInfo(breakMission.getInfo());
            CultimaPanel.missionStack.add(breakMission);
            if(CultimaPanel.missionsGiven[CLOSE_HELLMOUTH]==0)
               CultimaPanel.missionsGiven[CLOSE_HELLMOUTH]++;
            FileManager.saveProgress();
            response = breakMission.getStartStory();
         }
         else if(randMission == CEREMONY)
         {
            Location missionLoc = null;
            NPCPlayer godStatue = null;   
            int numTempleStatues = LocationBuilder.countStatues(closeTemple);
            boolean templeIsEndgame = (closeTemple.getRow()==50 && closeTemple.getCol()==50);
            int numCityStatues = LocationBuilder.countStatues(currentLoc);
            String missionLocType = "";
            if(numTempleStatues > 0 && !templeIsEndgame)
            {
               missionLocType = "temple";
               godStatue = LocationBuilder.putInGodStatue(closeTemple, missionLocType);
               missionLoc = closeTemple;
            }
            else if(numCityStatues > 0)
            {
               missionLocType = thisLoc;
               godStatue = LocationBuilder.putInGodStatue(currentLoc, missionLocType);
               missionLoc = currentLoc;
            }
            else  //no statues in closest temple or this city, so pick one at random and put a statue in the center
            {
               if(Math.random() < 0.5 && !templeIsEndgame)
               {
                  missionLoc = closeTemple;
                  missionLocType = "temple";
               }
               else
               {
                  missionLoc = currentLoc;
                  missionLocType = thisLoc;
               }
               godStatue = LocationBuilder.insertGodStatue(missionLoc, missionLocType);
            }
            if(missionLoc != null && godStatue != null)
            {
               String targName = godStatue.getName()+"-head";
               int targItemPrice = (godStatue.getLevel()*5);
               if(targItemPrice > 250)
                  targItemPrice = 250;
               godStatue.addItem(new Item(targName,targItemPrice));
               
               TerrainBuilder.markMapPath(missionLoc.getName());
               type = "Ceremony";
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Utilities.rollDie(20, godStatue.getLevel());
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("LOCATION_NAME", missionLoc.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               mRow = missionLoc.getRow();
               mCol = missionLoc.getCol();
            
               CultimaPanel.numChants = 0;
               Mission ceremonyMission = new Mission(type, story, startRow, startCol, goldReward, null, missionLoc.getName(), new Item(targName,targItemPrice), targName, mRow, mCol, CEREMONY, -1);
               selected.setMissionInfo(ceremonyMission.getInfo());
               CultimaPanel.missionStack.add(ceremonyMission);
               if(CultimaPanel.missionsGiven[CEREMONY]==0)
                  CultimaPanel.missionsGiven[CEREMONY]++;
               FileManager.saveProgress();
               response = ceremonyMission.getStartStory();
            }
         }
         if(randMission == Mission.TEACH_3_DOORS)
         {          
            type = "Teach";
            String targName = Utilities.shortName(selected.getName());                        
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            Item randMagicItem = Item.getRandomMagicItem();
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("PRIZE", randMagicItem.getName());
               story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission clearMission = new Mission(type, story, startRow, startCol, goldReward, randMagicItem, "none", null, targName, mRow, mCol, TEACH_3_DOORS, -1);
            selected.setMissionInfo(clearMission.getInfo());
            CultimaPanel.missionStack.add(clearMission);
            if(CultimaPanel.missionsGiven[TEACH_3_DOORS]==0)
               CultimaPanel.missionsGiven[TEACH_3_DOORS]++;
            response = clearMission.getStartStory();
            FileManager.saveProgress();
         }
         else if(randMission == CLEAR_TEMPLE)
         {
            loc = closeTemple;
         
            if(loc!=null)
            {          
               TerrainBuilder.markMapPath(loc.getName());
               type = "Clear";
               ArrayList<Coord> monsterFreq = loc.getMonsterFreq();
               if(monsterFreq.size() == 0)
               {
                  ArrayList<Coord> mf = new ArrayList<Coord>();   //x is the monster index, y is the number of monsters of that type
                  byte monsterType = NPC.randomTempleMonster();
                  int numMonsters = Utilities.rollDie(3,10);
                  mf.add(new Coord(monsterType, numMonsters));
                  loc.setMonsterFreq(mf);
                  monsterFreq = loc.getMonsterFreq();
               }
               byte monsterIndex = (byte)((monsterFreq.get(0)).getRow());
               String monsterType = NPC.characterDescription(monsterIndex);
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Math.max(50, (int)((monsterFreq.get(0)).getCol()));
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("MONSTER_TYPE", monsterType);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", loc.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               mRow = loc.getRow();
               mCol = loc.getCol();
            
               Mission clearMission = new Mission(type, story, startRow, startCol, goldReward, null, loc.getName(), null, "none", mRow, mCol, CLEAR_TEMPLE, -1);
               selected.setMissionInfo(clearMission.getInfo());
               CultimaPanel.missionStack.add(clearMission);
               if(CultimaPanel.missionsGiven[CLEAR_TEMPLE]==0)
                  CultimaPanel.missionsGiven[CLEAR_TEMPLE]++;
               FileManager.saveProgress();
               response = clearMission.getStartStory();
            } 
         }
         else if(randMission == FIND_PEARL)
         {
            String [] temp = missions[FIND_PEARL];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            //higher reward for targets further away
            int goldReward = Math.min(250, Utilities.rollDie(selected.getLevel()*100));
            String targetName = "life-pearl";
            int targItemPrice = (int)(goldReward);
            Coord monsterLoc = Utilities.getRandomFrom(waterLocs);
            if(monsterLoc != null)
            {
               int monsterRow = monsterLoc.getRow();
               int monsterCol = monsterLoc.getCol();
               if(Utilities.NPCAt(monsterRow, monsterCol, 0))
                  Utilities.removeNPCat(monsterRow, monsterCol, 0);
               NPCPlayer targNPC = null;
               String monsterType = "";
               if(Math.random() < 0.5)
               {
                  targNPC = new NPCPlayer(NPC.SQUIDKING, monsterRow, monsterCol, 0, "world");
                  monsterType = "Gargantuan Squid";
               }
               else
               {
                  targNPC = new NPCPlayer(NPC.HYDRACLOPS, monsterRow, monsterCol, 0, "world");
                  monsterType = "Hydraclops";
               }
               if(!targNPC.hasItem("life-pearl"))
                  targNPC.addItem(new Item(targetName, targItemPrice));
               CultimaPanel.worldMonsters.add(targNPC);
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("MONSTER_TYPE", ""+monsterType);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission("Find", story, startRow, startCol, goldReward, null, "none", new Item(targetName, goldReward), "none", mRow, mCol, FIND_PEARL, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[FIND_PEARL]==0)
                  CultimaPanel.missionsGiven[FIND_PEARL]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
            else
               response = ""; //no monster spawned, so clear out mission 
         }
         else if(randMission == Mission.ASSASSINATE_KING)
         {  //Assassinate mission
            type = "Assassinate";
                       
            if(king!=null && closeCastle!=null)
            {
               TerrainBuilder.markMapPath(closeCastle.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 0;
               Item [] items = {Item.getItemWithName("mannastone"), Item.getItemWithName("mindtome")};
               Item reward = Utilities.getRandomFrom(items);
               String targName = Utilities.shortNameWithTitle(king.getName());
               CultimaPanel.missionTargetNames.add(targName);
               String locName = Display.provinceName(closeCastle.getName());
               
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               targName = targName + "-bounty";
               king.addItem(new Item(targName, goldReward));
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(king.getMapIndex() != selected.getMapIndex())
               {
                  mRow = closeCastle.getRow();
                  mCol = closeCastle.getCol();
               }
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, reward, locName, new Item(targName, goldReward), targName, mRow, mCol, ASSASSINATE_KING, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[ASSASSINATE_KING]==0)
                  CultimaPanel.missionsGiven[ASSASSINATE_KING]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         else if(randMission == Mission.PURGE_HOLY)
         {  //Purge a city or town of infidels
            type = "Purge";  
            inLocNPCs = null;
            NPCPlayer targNPC = null;
            
            String []locTypes2 = {"city","fortress","port","village"};
            Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
            inLocNPCs = CultimaPanel.civilians.get(closeCity.getMapIndex());
            if(inLocNPCs.size() >= 1)
            {
               targNPC = Utilities.getRandomFrom(inLocNPCs);
               loc = closeCity;
            }
            else
               loc = null;
            
            if(targNPC!=null && loc!=null)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               String locName = Display.provinceName(loc.getName());
               int numCivs = LocationBuilder.countCivilians(loc);
               int goldReward = 0;
                                  
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = loc.getRow();
               int mCol = loc.getCol();
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, "none", mRow, mCol, PURGE_HOLY, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[PURGE_HOLY]==0)
                  CultimaPanel.missionsGiven[PURGE_HOLY]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         else if(randMission == Mission.FIND_CAERBANNOG)
         {  
            type = "Find";
            String locType = "place";
            NPCPlayer targNPC = null;
            loc =  Utilities.getCaerbannog();
            locType = "cave";
            byte monsterType = NPC.randomChallengingMonster();
            int targItemPrice = Utilities.rollDie(100,300);
            target =  new Item("Caerbannog runes", targItemPrice);
            targNPC = new NPCPlayer(monsterType, 0, 0, 0, locType);  
            targNPC.modifyStats(1.2);
            targNPC.addItem(target);   
            boolean success = TerrainBuilder.addNPCtoLocation(loc, locType, targNPC);
         
            if(targNPC!=null && loc!=null && success)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = targItemPrice;
               String targName = Utilities.shortName(selected.getName());         
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(targNPC.getMapIndex() != selected.getMapIndex())
               {
                  mRow = loc.getRow();
                  mCol = loc.getCol();
               }
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target.getName(),target.getValue()), targName, mRow, mCol, FIND_CAERBANNOG, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[FIND_CAERBANNOG]==0)
                  CultimaPanel.missionsGiven[FIND_CAERBANNOG]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
         else if(randMission == Mission.FIND_MAGIC_ITEM)
         {  
            type = "Find";
            String locType = "place";
            NPCPlayer targNPC = null;
         //pick a close adventure spot or battlefield
            loc =  TerrainBuilder.closeLocation(CultimaPanel.allAdventure);
            if(loc == null)
            {
               String [] locTypes3 = {"cave"};
               loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes3);
               locType = "cave";
            }
            if(loc == null)
            {
               String [] locTypes3 = {"mine"};
               loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes3);
               locType = "mine";
            }
            if(loc == null)
            {
               String [] locTypes3 = {"lair"};
               loc = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations,locTypes3);
               locType = "lair";
            }
            if(loc != null)
            {
               String terrType = loc.getTerrain().getName().toLowerCase();
               if(TerrainBuilder.habitablePlace(terrType))
                  locType =  "town";
               else if(terrType.contains("cave"))
                  locType = "cave";
               else if(terrType.contains("mine"))
                  locType = "mine";
               else if(terrType.contains("lair"))
                  locType = "lair";
               else if(terrType.contains("dungeon"))
                  locType = "dungeon";
               else if(terrType.contains("temple"))
                  locType = "temple";   
            }
            byte monsterType = NPC.SORCERER;
            target =  Weapon.getIceStaff();
            double targetType = Math.random();
            if(targetType >= 0.33 && targetType< 0.66)
               target = Armor.getRandomRobe();
            else if(targetType >= 0.66)
            {
               Item [] magicItems = {Item.getFloretBox(), Item.getMannastone(), Item.getMindTome()};
               target = magicItems[(int)(Math.random()*magicItems.length)];
            }   
            targNPC = new NPCPlayer(monsterType, 0, 0, 0, locType);  
            targNPC.modifyStats(1.2);
            if(target instanceof Weapon)
               targNPC.setWeapon((Weapon)(target));  
            else if(target instanceof Armor)
               targNPC.setArmor((Armor)(target));              
            else 
               targNPC.addItem(target);   
            boolean success = TerrainBuilder.addNPCtoLocation(loc, locType, targNPC);
         
            if(targNPC!=null && loc!=null && success)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 0;
               String targName = Utilities.shortName(selected.getName());         
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("LOCATION_NAME", locName);
                  story[i]=story[i].replace("ITEM_NAME", target.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(targNPC.getMapIndex() != selected.getMapIndex())
               {
                  mRow = loc.getRow();
                  mCol = loc.getCol();
               }
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target.getName(),target.getValue()), targName, mRow, mCol, FIND_MAGIC_ITEM, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[FIND_MAGIC_ITEM]==0)
                  CultimaPanel.missionsGiven[FIND_MAGIC_ITEM]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         } 
         else if(randMission == Mission.TEACH_SPELL)
         {  
            type = "Teach";
            NPCPlayer targNPC = selected;
            String spellName = Utilities.getRandomFrom(unknownSpells);
            target =  new Item(spellName, 0);     
            if(targNPC!=null)
            {
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Utilities.rollDie(50,100);
               String targName = Utilities.shortName(selected.getName());                        
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("SPELL_NAME", spellName);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", target, targName, mRow, mCol, TEACH_SPELL, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[TEACH_SPELL]==0)
                  CultimaPanel.missionsGiven[TEACH_SPELL]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         } 
         else if(randMission == Mission.MARRIAGE && single!=null)
         {  
            type = "Marry";
            NPCPlayer targNPC = single;      
            
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            String targName = Utilities.shortName(targNPC.getName());   
            CultimaPanel.missionTargetNames.add(targName);                     
            for(int i=0; i<story.length; i++)
            {
               story[i]=story[i].replace("NPC_NAME", targName);
            }
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", null, targName, mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         }
         else if(randMission == Mission.FIND_SERPENT_EGGS)
         {  
            type = "Harvest";
            NPCPlayer targNPC = selected;       
            if(targNPC!=null)
            {
               Item ourItem = new Item("3-serpent-eggs",90);
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = Utilities.rollDie(25, 100);
               if(CultimaPanel.shoppeDiscount)   //charmed
                  goldReward = (int)(goldReward * 1.20);
               String targName = Utilities.shortName(selected.getName());         
               for(int i=0; i<story.length; i++)
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, "none", ourItem, targName, mRow, mCol, FIND_SERPENT_EGGS, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[FIND_SERPENT_EGGS]==0)
                  CultimaPanel.missionsGiven[FIND_SERPENT_EGGS]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }   
         }
      }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(NPC.mainMission)+".";
         selected.setNumInfo(3);
      }
      return response;
   }

   public static String jesterMission(NPCPlayer selected)
   {
      String response = "";
      Location loc = null;
      Item target = null;
      String type = "";
      String missionInfo = selected.getMissionInfo();
      Mission currMission = null;
      String [] magicDaggers = {"Poison-dagger","Souldagger","Magmadagger","Frostdagger","Banedagger"};
   
      int currMissionIndex = -1;
      for(int i=0; i<CultimaPanel.missionStack.size(); i++)
      {
         Mission m = CultimaPanel.missionStack.get(i);
         if(missionInfo.equals(m.getInfo()))
         {
            currMission = m;
            currMissionIndex = i;
            break;
         }
      }
      if(currMission!=null)   //see if we finished the mission
      {
         target = currMission.getTarget();
         if(currMission.getType().contains("Steal") && (target != null && (target.getName().contains("pick") || target.getName().contains("book") || target.getName().contains("cube"))))
         {
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               if(selected.getReputation() > 0)
                  selected.setReputation(-1*selected.getReputation());   //you completed a mission, but jester runs off - make him evil
               CultimaPanel.player.removeItem(target.getName());
               selected.addItem(target);
               selected.setMoveTypeTemp(NPC.RUN);
               int reputationPoints = -5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Steal") && target!=null)
         {
            target = currMission.getTarget();
            String targetName = target.getName();
            boolean hasDaggers = true;
            for(String dag: magicDaggers)
               if(!CultimaPanel.player.hasItem(dag))
                  hasDaggers = false;
            if(targetName.toLowerCase().contains("dagger") && hasDaggers)   //steal the 5 magic daggers mission
            {
               if(selected.getReputation() > 0)
                  selected.setReputation(-1*selected.getReputation());   //you completed a mission, but jester runs off - make him evil
               for(String dag: magicDaggers)
                  if(Weapon.getWeaponWithName(dag)!=null)
                  {   //remove weapon from player and add to selected
                     Weapon dropped = CultimaPanel.player.discardWeapon(dag);
                     selected.addItem(new Item(dropped.getName(), dropped.getValue()));
                  }                  
               selected.setMoveTypeTemp(NPC.RUN);
               int reputationPoints = -5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Destroy"))
         {   //check to see if Destroy mission is finished   
            target = currMission.getTarget();
            String targetName = target.getName();
            if(CultimaPanel.player.hasItem(targetName))
            {
               CultimaPanel.player.removeItem(target.getName());
               int reputationPoints = -5;
               finishMission(currMission, selected, reputationPoints);
            }
         }
         else if(currMission.getType().contains("Give") && !CultimaPanel.player.hasItem(currMission.getTarget().getName()) && CultimaPanel.player.hasItem("receipt"))
         {
            CultimaPanel.player.removeItem("receipt");
            selected.setMoveTypeTemp(NPC.RUN);
            int reputationPoints = -5;
            finishMission(currMission, selected, reputationPoints);
         }
      }
      if(selected.getNumInfo() != 10)
         response = Utilities.getRandomFrom(NPC.noMission)+".";
      else if(currMission!=null && currMission.getState() == Mission.IN_PROGRESS)
         response = currMission.getMiddleStory();
      else if(currMission!=null && currMission.getState() ==  Mission.FINISHED)
      {
         response = currMission.getEndStory();
         CultimaPanel.player.addExperience(100);
         int goldReward = currMission.getGoldReward();
            //jester stiffs us of the reward
         selected.setNumInfo(0);
         selected.setMissionInfo("none");
         if(currMissionIndex >=0 && currMissionIndex < CultimaPanel.missionStack.size())
            CultimaPanel.missionStack.remove(currMissionIndex);
         FileManager.saveProgress();
      }   
      else
      {  //STEAL_PICK, STEAL_BOOK, STEAL_DICE, STEAL_DAGGERS or DESTROY_STATUE
         //see if there is a statue in this location.  If not, pick a steal mission type
         NPCPlayer statue = null;
         Location locTry = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
         ArrayList<NPCPlayer> inLocNPCs = CultimaPanel.civilians.get(selected.getMapIndex());
         for(NPCPlayer p: inLocNPCs)
         {
            if(p.isStatue())
            {
               statue = p;
               break;
            }
         }
         
         byte randMission = STEAL_BOOK;
         ArrayList<Byte>missionTypes = new ArrayList<Byte>();
         //fill list of missions to pick from with the ones we have not seen yet
         if(CultimaPanel.missionsGiven[PRANK_PARCEL]==0 && !CultimaPanel.player.hasItem("parcel") && !CultimaPanel.player.hasItem("receipt"))
            missionTypes.add(PRANK_PARCEL);
         if(CultimaPanel.missionsGiven[STEAL_BOOK]==0)
            missionTypes.add(STEAL_BOOK);
         if(CultimaPanel.missionsGiven[STEAL_PICK]==0)
            missionTypes.add(STEAL_PICK);
         if(CultimaPanel.missionsGiven[STEAL_DICE]==0)
            missionTypes.add(STEAL_DICE);
         if(CultimaPanel.missionsGiven[STEAL_DAGGERS]==0)
            missionTypes.add(STEAL_DAGGERS);
         if(selected.isWerewolf() && CultimaPanel.missionsGiven[MERCY_KILL]==0)   //werewolf tag
            missionTypes.add(MERCY_KILL);
         if(CultimaPanel.missionsGiven[DESTROY_STATUE]==0 && statue != null)
            for(int i=0; i<6; i++)     //make it more likely we get this mission
               missionTypes.add(DESTROY_STATUE);
         if(missionTypes.size()==0)    //we have seen all the mission types before, so pick a random one
         {
            if(!CultimaPanel.player.hasItem("parcel") && !CultimaPanel.player.hasItem("receipt"))
            {
               missionTypes.add(PRANK_PARCEL);
            }
            missionTypes.add(STEAL_BOOK);
            missionTypes.add(STEAL_PICK);
            missionTypes.add(STEAL_DICE);
            missionTypes.add(STEAL_DAGGERS);
            if(selected.isWerewolf())   //werewolf tag
               missionTypes.add(MERCY_KILL);
            if(statue != null)         //make it more likely we get this mission
               for(int i=0; i<6; i++)
                  missionTypes.add(DESTROY_STATUE);
         }
         randMission = Utilities.getRandomFrom(missionTypes);
         
         //***TESTING***  randMission = STEAL_DICE;
                     
         NPCPlayer targNPC = null;
         if(randMission == DESTROY_STATUE)
         {
            targNPC = statue;
            type = "Destroy";
         }
         else
            type = "Steal";
         String locType = "place";
         
         if(randMission == STEAL_DAGGERS)
         {
            target = new Item("5 magic daggers",10000);
            locType = CultimaPanel.player.getLocationType();
            int mi = CultimaPanel.player.getMapIndex();
            byte [][] currMap = CultimaPanel.map.get(mi);
            int r = (int)(Math.random()*currMap.length);
            int c = (int)(Math.random()*currMap[0].length);
            ArrayList<Coord>spawnCoords = new ArrayList<Coord>();
            for(int cr = 0; cr<currMap.length; cr++)
               for(int cc = 0; cc<currMap[0].length; cc++)
                  if(LocationBuilder.canMoveTo(currMap, cr, cc) && !TerrainBuilder.onWater(currMap, cr, cc))
                     spawnCoords.add(new Coord(cr, cc));
            if(spawnCoords.size() > 0)
            {
               for(String dag: magicDaggers)
               { 
                  Weapon weap = Weapon.getWeaponWithName(dag);
                  if(weap != null)
                  {
                     byte indexType = NPC.randTraveler();
                  
                     Coord spawn = Utilities.getRandomFrom(spawnCoords);
                     r = spawn.getRow();
                     c = spawn.getCol();
                     NPCPlayer traveler = new NPCPlayer(indexType, r, c, mi, r, c, locType);
                     traveler.setWeapon(weap);
                     CultimaPanel.civilians.get(mi).add(traveler);
                  }
                  else
                  {
                     selected.setNumInfo(0);    
                     return NPC.jesterResponse(selected,"");
                  }  
               }
            }
            else
            {
               selected.setNumInfo(0);    
               return NPC.jesterResponse(selected,"");
            }
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);               
         
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
         
            Mission getMission = new Mission(type, story, startRow, startCol, 0, null, "none", new Item(target.getName(),target.getValue()), "none", mRow, mCol, STEAL_DAGGERS, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[STEAL_DAGGERS]==0)
               CultimaPanel.missionsGiven[STEAL_DAGGERS]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         }
         else if(randMission == STEAL_PICK || randMission == STEAL_BOOK || randMission == STEAL_DICE)
         {
            int randType = Utilities.rollDie(10);
            inLocNPCs = CultimaPanel.civilians.get(selected.getMapIndex());
            ArrayList<NPCPlayer> jesters = new ArrayList<NPCPlayer>();
            for(NPCPlayer p: inLocNPCs)
               if(p.getCharIndex()==NPC.JESTER)
                  jesters.add(p);
            if(jesters.size() <= 1)
               randType = Utilities.rollDie(2,10);
            targNPC = null;
                           
            if(randType < 2)           //item goes to someone at the same location
            {
               loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
               locType = CultimaPanel.player.getLocationType();
               if(jesters.size()==0)
                  targNPC = null;
               else
                  targNPC = Utilities.getRandomFrom(jesters);
            }
            if(randType <= 5 || targNPC==null)      //item goes to someone at the next closest castle
            {
               String [] locTypes = {"castle","tower"};
               Location closeCastle = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes);
               inLocNPCs = CultimaPanel.civilians.get(closeCastle.getMapIndex());
               jesters = new ArrayList<NPCPlayer>();
               for(NPCPlayer p: inLocNPCs)
               {
                  if(randMission == STEAL_PICK || randMission == STEAL_DICE)
                  {
                     if(p.getCharIndex()==NPC.JESTER)
                        jesters.add(p);
                  }
                  else  
                     if(p.getCharIndex()==NPC.JESTER || NPC.isGuard(p.getCharIndex()) || p.getCharIndex()==NPC.SWORD || p.getCharIndex()==NPC.KING)
                        jesters.add(p);     
               }
               if(jesters.size() > 0)
               {
                  targNPC = Utilities.getRandomFrom(jesters);
                  loc = closeCastle;
               }
            }
            
            //***TESTING*** targNPC = null;
            
            if(targNPC==null)                       //item goes to  someone at the next closest town
            {
               String []locTypes2 = {"city","fortress","port","village"};
               Location closeCity = TerrainBuilder.closeLocationAndBuild(CultimaPanel.allLocations, locTypes2);
               inLocNPCs = CultimaPanel.civilians.get(closeCity.getMapIndex());
               jesters = new ArrayList<NPCPlayer>();
               for(NPCPlayer p: inLocNPCs)
               {
                  if(randMission == STEAL_PICK || randMission == STEAL_DICE)
                  {
                     if(p.getCharIndex()==NPC.JESTER)
                        jesters.add(p);
                  }
                  else  
                     if(p.getCharIndex()==NPC.JESTER || NPC.isGuard(p.getCharIndex()) || p.getCharIndex()==NPC.SWORD || p.getCharIndex()==NPC.KING)
                        jesters.add(p); 
               }
               if(jesters.size() > 0)
               {
                  targNPC = Utilities.getRandomFrom(jesters);
                  loc = closeCity;
               }
            }
            if(randMission == STEAL_PICK)
               target =  Item.getMagicPick(); 
            else if(randMission == STEAL_DICE)
               target =  Item.getLoadedCube(); 
            else
               target = new Item("riddlebook",50);
          
            if(targNPC!=null && loc!=null)
            {
               TerrainBuilder.markMapPath(loc.getName());
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = target.getValue()*10;
               targNPC.addItem(target);
               String targName = Utilities.shortName(targNPC.getName());         
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
                  story[i]=story[i].replace("ITEM_NAME", target.getName());
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               if(targNPC.getMapIndex() != selected.getMapIndex())
               {
                  mRow = loc.getRow();
                  mCol = loc.getCol();
               }
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(target.getName(),target.getValue()), targName, mRow, mCol, (byte)randMission, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
         }
         else if(randMission==MERCY_KILL)
         {
            type = "Mercy";
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            String [] temp = missions[randMission];
            String [] story = new String[3];
            story[0] = new String(temp[0]);
            story[1] = new String(temp[1]);
            story[2] = new String(temp[2]);
            int goldReward = 0;
            String targName = Utilities.shortName(selected.getName());
            if(targName.contains("~"))
            {
               String [] parts = targName.split("~");
               targName = parts[0];
            }         
            String locName = Display.provinceName(loc.getName());
            int startRow = CultimaPanel.player.getWorldRow();
            int startCol = CultimaPanel.player.getWorldCol();
            int mRow = -1, mCol = -1;
            Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, null, targName, mRow, mCol, (byte)randMission, -1);
            selected.setMissionInfo(getMission.getInfo());
            CultimaPanel.missionStack.add(getMission);
            if(CultimaPanel.missionsGiven[randMission]==0)
               CultimaPanel.missionsGiven[randMission]++;
            FileManager.saveProgress();
            response = getMission.getStartStory();
         }
         else if(randMission == DESTROY_STATUE)
         {
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            if(targNPC!=null && loc!=null)
            {
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 2000;
               String targName = "statue-head";     
               targNPC.addItem(new Item(targName, goldReward));
                
               String locName = Display.provinceName(loc.getName());
            
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("GOLD_REWARD", ""+goldReward);
                  story[i]=story[i].replace("LOCATION_NAME", locName);
               }
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               mRow = loc.getRow();
               mCol = loc.getCol();
             
               TerrainBuilder.markMapPath(loc.getName());
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(targName, goldReward), targName, mRow, mCol, DESTROY_STATUE, -1);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[DESTROY_STATUE]==0)
                  CultimaPanel.missionsGiven[DESTROY_STATUE]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
         }  
         else if(randMission == Mission.PRANK_PARCEL)
         {  
            type = "Give";   
            loc = CultimaPanel.allLocations.get(CultimaPanel.player.getMapIndex());
            targNPC = Utilities.getRandomFrom(inLocNPCs);  
            ArrayList<NPCPlayer> targets = new ArrayList<NPCPlayer>();
            ArrayList<NPCPlayer> guards = new ArrayList<NPCPlayer>();
            for(NPCPlayer pl: inLocNPCs)
            {
               if(!pl.getName().equals(selected.getName()))
               {
                  if(NPC.isGuard(pl))
                  {
                     guards.add(pl);
                  }
                  else if(pl.getCharIndex() != NPC.CHILD)
                  {
                     targets.add(pl);
                  }   
               }
            }
            if(guards.size() > 0)
            {
               targNPC = Utilities.getRandomFrom(guards);
            }
            else if(targets.size() > 0)
            {
               targNPC = Utilities.getRandomFrom(targets);
            }
            else
            {
               targNPC = null;
            }
            if(targNPC!=null && loc!=null)
            {
               String [] temp = missions[randMission];
               String [] story = new String[3];
               story[0] = new String(temp[0]);
               story[1] = new String(temp[1]);
               story[2] = new String(temp[2]);
               int goldReward = 0;
               String targName = Utilities.shortName(targNPC.getName());     
               CultimaPanel.missionTargetNames.add(targName);
               int missionDay = -1;
               String targetName = targName+"-prank-parcel";
               CultimaPanel.player.addItem(targetName);
               targNPC.addItem(new Item("message-parcel-receipt", 0));
               String locName = Display.provinceName(loc.getName());
               int startRow = CultimaPanel.player.getWorldRow();
               int startCol = CultimaPanel.player.getWorldCol();
               int mRow = -1, mCol = -1;
               for(int i=0; i<story.length; i++)
               {
                  story[i]=story[i].replace("NPC_NAME", targName);
                  story[i]=story[i].replace("PLAYER_NAME", CultimaPanel.player.getShortName());
               }
               Mission getMission = new Mission(type, story, startRow, startCol, goldReward, null, locName, new Item(targetName, goldReward), targName, mRow, mCol, (byte)randMission, missionDay);
               selected.setMissionInfo(getMission.getInfo());
               CultimaPanel.missionStack.add(getMission);
               if(CultimaPanel.missionsGiven[randMission]==0)
                  CultimaPanel.missionsGiven[randMission]++;
               FileManager.saveProgress();
               response = getMission.getStartStory();
            }
            else
            {  //no mission
               response = "";
            }   
         } 
      }
      if(response.length() == 0)
      {
         response = Utilities.getRandomFrom(NPC.mainMission)+".";
         selected.setNumInfo(0);
      }
      return response;
   }

   public String toString()
   {
      String ans = "";
      ans += type + "     **type\n";
      ans += missionType + "     **missionType\n";
      ans += startStory + "     **startStory\n";
      ans += middleStory + "     **middleStory\n";         
      ans += endStory + "     **endStory\n";               
      ans += "("+worldRow+","+worldCol + ")     **(worldRow,worldCol)\n";  
      ans += day + "     **mission end day\n";
      ans += goldReward + "     **goldReward\n";    
      ans += itemReward + "     **itemReward\n";   
      ans += clearLocation + "     **clearLocation\n";  
      ans += target + "     **target\n";       
      ans += targetHolder + "     **targetHolder\n";      
      ans += "("+targetRow+","+targetCol + ")     **(targetRow,targetCol)\n";
      ans += failed + "     **failed";
      return ans;
   }
      
}