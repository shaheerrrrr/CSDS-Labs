import javax.sound.midi.*;

public class Sound
{
//sound stuff
   protected static MidiChannel[] channels=null;		//MIDI channels
   protected static Instrument[] instr;					//MIDI instrument bank
   public static int soundOffFrame, nextNoteFrame;    //frame number to turn sound off, or go to the next note
   protected static final int PIANO = 0;
   protected static final int HARPSICHORD = 6;
   protected static final int CELESTA = 8;
   protected static final int GLOCKENSPIEL = 9;	
   protected static final int VIBRAPHONE = 11;
   protected static final int XYLOPHONE = 13;
   protected static final int NYLON_GUIT = 24;
   protected static final int STEEL_GUIT = 25;
   protected static final int MUTED_GT = 28;
   protected static final int GUIT_HARM = 31;
   protected static final int TIMPANI = 47;
   protected static final int ORCH_HIT = 55;
   protected static final int BRIGHTNESS = 100;
   protected static final int SITAR = 104;
   protected static final int BANJO = 105;
   protected static final int SHAMISEN = 106;
   protected static final int KALIMBA = 108;
   protected static final int TINKERBELL = 112;
   protected static final int STEEL_DRUM = 114;
   protected static final int WOODBLOCK = 115;
   protected static final int TAIKO = 116;
   protected static final int MELO_TOM = 117;
   protected static final int SYNTH_DRUM = 118;
   protected static final int CYMBAL = 119;
   protected static final int FRET_NOISE = 120;
   protected static final int BREATH_NOISE = 121;
   protected static final int SEA_SHORE = 122;
   protected static final int BIRD = 123;
   protected static final int HELICOPTER = 125;
   protected static final int GUNSHOT = 127;

   public static boolean initialize()
   {
   //sound stuff
      try 
      {
         Synthesizer synth = MidiSystem.getSynthesizer();
         synth.open();
         Sound.channels = synth.getChannels();
         Sound.instr = synth.getDefaultSoundbank().getInstruments();
      }
      catch (Exception ignored) 
      {
         System.out.println("No audio device found - sound turned off.");
         return false;
      }
      channels[0].programChange(instr[PIANO].getPatch().getProgram());
      channels[1].programChange(instr[PIANO].getPatch().getProgram());
   
      channels[0].allNotesOff();		//turn sounds off 
      channels[1].allNotesOff();		//turn sounds off 
      soundOffFrame = Integer.MAX_VALUE;
      nextNoteFrame = 0;
      return true;
   }

   public static void silence()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].allNotesOff();		//turn sounds off 
      channels[1].allNotesOff();		//turn sounds off 
   }

   public static void checkToClearSound()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      if(MMMPanel.numFrames > soundOffFrame)
      {
         silence();						//turn sounds off 
         soundOffFrame = Integer.MAX_VALUE;
      }
   }

//sound of crowd being stepped on my a monster
   public static void splat()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[MUTED_GT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+15;          
      int velocity = (int)(Math.random()*10)+30;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound of small smokepuff explosion (blop running over vehicle)
   public static void smokepuffExplosion()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[MUTED_GT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+15;          
      int velocity = (int)(Math.random()*10)+50;
      channels[0].noteOn(pitch, velocity);
   }
         
  //sound of bullet hit
   public static void bulletHit()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[MUTED_GT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+20;          
      int velocity = (int)(Math.random()*10)+40;
      channels[0].noteOn(pitch, velocity);
   }
   	
  //sound of blop splitting in two
   public static void blopSplit()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[MUTED_GT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+20;          
      int velocity = (int)(Math.random()*10)+90;
      channels[0].noteOn(pitch, velocity);
   }

 //sound for jeep and coastguard machinegun
   public static void machinegun()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+65;          
      int velocity = (int)(Math.random()*10)+30;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for police and national guard gun
   public static void gunshot()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+70;          
      int velocity = (int)(Math.random()*10)+20;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for destroyer cannon
   public static void cannonshot()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+45;          
      int velocity = (int)(Math.random()*10)+85;
      channels[0].noteOn(pitch, velocity);
   }

//sound for small fireball explosion
   public static void smallExplosion()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+30;          
      int velocity = (int)(Math.random()*10)+30;
      channels[0].noteOn(pitch, velocity);
   }

 //sound for fireball explosion
   public static void fireballExplosion()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+30;          
      int velocity = (int)(Math.random()*10)+50;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for big explosion
   public static void bigExplosion()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+25;          
      int velocity = (int)(Math.random()*10)+60;
      channels[0].noteOn(pitch, velocity);
   }

//sound for placing a terrain in the map-maker
   public static void placeSelected()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = 127;          
      int velocity = 50;
      channels[0].noteOn(pitch, velocity);
   }
	
	//sound for monster-maker selection
   public static void selection()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GUNSHOT].getPatch().getProgram());
      int pitch = 127;          
      int velocity = 60;
      channels[0].noteOn(pitch, velocity);
   }

//sound of a massive explosion
   public static void massiveExplosion()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[TIMPANI].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+25;          
      int velocity = (int)(Math.random()*10)+60;
      channels[0].noteOn(pitch, velocity);
   }

//sound of an electrical explosion
   public static void elecExplosion()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[TIMPANI].getPatch().getProgram());
      int pitch = (int)(Math.random()*11)+25;          
      int velocity = (int)(Math.random()*10)+90;
      channels[0].noteOn(pitch, velocity);
   }

//sound for water explosion
   public static void waterExplosion()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[CYMBAL].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+35;          
      int velocity = (int)(Math.random()*10)+30;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for water explosion
   public static void waterDetonation()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[CYMBAL].getPatch().getProgram());
      int pitch = (int)(Math.random()*11)+50;          
      int velocity = (int)(Math.random()*10)+25;
      channels[0].noteOn(pitch, velocity);
   }  
   
	//sound for monster getting injured
   public static void monsterHurt()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[BIRD].getPatch().getProgram());
      int pitch = (int)(Math.random()*11);          
      int velocity = (int)(Math.random()*10)+90;
      channels[0].noteOn(pitch, velocity);
   } 
   
	//sound for monster losing health for being hungry
   public static void monsterHungry()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[BIRD].getPatch().getProgram());
      int pitch = (int)(Math.random()*11);          
      int velocity = (int)(Math.random()*10)+90;
      channels[0].noteOn(pitch, velocity);
   }
	
	//sound for monster partner death
   public static void monsterDie()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[BIRD].getPatch().getProgram());
      int pitch = (int)(Math.random()*11)+25;          
      int velocity = (int)(Math.random()*10)+90;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for monster shriek attack
   public static void shriekAttack()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[BIRD].getPatch().getProgram());
      int pitch = (int)(Math.random()*11)+40;          
      int velocity = (int)(Math.random()*10)+30;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for web or sludge being shot by a monster
   public static void shootWeb()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[FRET_NOISE].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+45;          
      int velocity = (int)(Math.random()*10)+30;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for throwing car
   public static void throwCar()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[FRET_NOISE].getPatch().getProgram());
      int pitch = (int)(Math.random()*6)+35;          
      int velocity = (int)(Math.random()*10)+30;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for robot hurt
   public static void robotHurt()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[FRET_NOISE].getPatch().getProgram());
      int pitch = 28;          
      int velocity = (int)(Math.random()*10)+90;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for entering value into text field
   public static void enterText()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[FRET_NOISE].getPatch().getProgram());
      int pitch = 100;          
      int velocity = 60;
      channels[0].noteOn(pitch, velocity);
   }
   
	 //sound for adding AI monster
   public static void win()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[ORCH_HIT].getPatch().getProgram());
      int pitch = 42;          
      int velocity = 100;
      channels[0].noteOn(pitch, velocity);
   }
	
	//sound for death-beam being shot by a monster
   public static void shootBeam()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[ORCH_HIT].getPatch().getProgram());
      int pitch = 25;          
      int velocity = (int)(Math.random()*10)+70;
      channels[0].noteOn(pitch, velocity);
   }
   
  //sound for adding AI monster
   public static void addMonster()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[ORCH_HIT].getPatch().getProgram());
      int pitch = 43;          
      int velocity = 90;
      channels[0].noteOn(pitch, velocity);
   }

  //sound for removing AI monster
   public static void removeMonster()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[SHAMISEN].getPatch().getProgram());
      int pitch = 22;          
      int velocity = 90;
      channels[0].noteOn(pitch, velocity);
   }

//sound for missile being shot by a fighterplane
   public static void shootMissile()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[TAIKO].getPatch().getProgram());
      int pitch = (int)(Math.random()*10)+5;          
      int velocity = (int)(Math.random()*10)+40;
      channels[0].noteOn(pitch, velocity);
   }
   
	 //sound for flame being shot by a flametank
   public static void shootFlame()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[TAIKO].getPatch().getProgram());
      int pitch = (int)(Math.random()*10)+15;          
      int velocity = (int)(Math.random()*10)+40;
      channels[0].noteOn(pitch, velocity);
   }

	//sound for fireball being shot by a monster
   public static void shootFire()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[TAIKO].getPatch().getProgram());
      int pitch = (int)(Math.random()*10)+10;          
      int velocity = (int)(Math.random()*10)+100;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for building crumbling
   public static void buildingFall()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[TAIKO].getPatch().getProgram());
      int pitch = (int)(Math.random()*11)+10;          
      int velocity = (int)(Math.random()*10)+60;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound of monster stomping or entering flight
   public static void stomp()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[BANJO].getPatch().getProgram());
      int pitch = (int)(Math.random()*11)+5;          
      int velocity = (int)(Math.random()*10)+80;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound of monster digging underground
   public static void dig()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[GLOCKENSPIEL].getPatch().getProgram());
      int pitch = (int)(Math.random()*11)+5;          
      int velocity = (int)(Math.random()*10)+80;
      channels[0].noteOn(pitch, velocity);
   }
		
	//sound for title screen note
   public static void titleScreen()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[PIANO].getPatch().getProgram());
      int pitch = 22;          
      int velocity = 80;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for nuke drop
   public static void nuke()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[PIANO].getPatch().getProgram());
      int pitch = 22;          
      int velocity = 100;
      channels[0].noteOn(pitch, velocity);
   }
   
	//sound for threat response changing
   public static void threatResponse()
   {
      if(!MMMPanel.soundOn)
      {
         return;
      }
      channels[0].programChange(instr[BRIGHTNESS].getPatch().getProgram());
      int pitch = 22;          
      int velocity = 80;
      channels[0].noteOn(pitch, velocity);
   }

	
}