//Curtis Grover, D Oberle
//Make some sound - requires that your computer has a sound device
import javax.sound.midi.*;
public class Sound
{
   private static MidiChannel[] channels=null;		         //MIDI channels
   protected static final int PLAYER1 = 0, PLAYER2 = 1, PLAYER3 = 2, SOUNDEFFECTS = 3; //channel index names
   private static Instrument[] instr;					         //MIDI instrument bank
   protected static final int PIZZ = 45;
   protected static final int TIMPANI = 47;
   protected static final int ORCH = 55;
   protected static final int SAX = 66;
   protected static final int FLAP = 76; 
   protected static final int OCAR = 79; 
   protected static final int CRASH = 96;                   //here is the list of all instrument sounds 
   protected static final int BELL = 112;
   protected static final int REV_CYMB = 119;               //https://www.midi.org/specifications-old/item/gm-level-1-sound-set  
   protected static final int FRET_NOISE = 120;             //https://www.midi.org/specifications-old/item/gm-level-1-sound-set  
   protected static final int SEA = 122;          
   protected static final int BIRD = 123;          
   protected static final int GUN = 127;          
   protected static final int [] majPent = {0,2,4,7,9,12};  //semi-tones between intervals in different scales
   protected static final int OCTAVE = 12;
   private static int [] scale;
   
   public static boolean initialize()
   {
      try 
      {
         Synthesizer synth = MidiSystem.getSynthesizer();
         synth.open();
         channels = synth.getChannels();
         instr = synth.getDefaultSoundbank().getInstruments();
      }
      catch (Exception ignored) 
      {
         return false;
      }
      channels[PLAYER1].programChange(instr[0].getPatch().getProgram());
      channels[PLAYER2].programChange(instr[0].getPatch().getProgram());
      channels[PLAYER3].programChange(instr[0].getPatch().getProgram());
      channels[SOUNDEFFECTS].programChange(instr[0].getPatch().getProgram());
      silence();	
      int randKey = (int)(Math.random()*12) + 72;
      scale = new int[majPent.length];
      for(int i=0; i<scale.length; i++)
         scale[i] = majPent[i] + randKey;
      return true;
   }

   //turn sounds off on all 3 channels
   public static void silence()
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      channels[PLAYER1].allNotesOff();		
      channels[PLAYER2].allNotesOff();		
      channels[PLAYER3].allNotesOff();	
      channels[SOUNDEFFECTS].allNotesOff();	
   }
   
   //turn sounds off for a specific channel
   public static void silence(int ch)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      channels[ch].allNotesOff();		
   }

   //sound for clicking a button
   public static void click(int ch)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      channels[ch].programChange(instr[FRET_NOISE].getPatch().getProgram());
      int pitch = 100;                    //pitch is how low or high the note is (1-127)  
      int velocity = 60;                  //velocity is how loud the sound is (0-127)
      channels[ch].noteOn(pitch, velocity);
   }
   
    //sound for going to the WATER realm
   public static void waterRealm(int ch)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      silence();
      channels[ch].programChange(instr[SEA].getPatch().getProgram());
      int pitch = 50;                     //pitch is how low or high the note is (1-127)  
      int velocity = 60;                  //velocity is how loud the sound is (0-127)
      channels[ch].noteOn(pitch, velocity);
   }
   
   //sound for going to the AIR realm
   public static void airRealm(int ch)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      silence();
      channels[ch].programChange(instr[REV_CYMB].getPatch().getProgram());
      int pitch = 25;                     //pitch is how low or high the note is (1-127)  
      int velocity = 60;                  //velocity is how loud the sound is (0-127)
      channels[ch].noteOn(pitch, velocity);
   }
   
   //sound for going to the LAND realm
   public static void landRealm(int ch)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      silence();
      channels[ch].programChange(instr[GUN].getPatch().getProgram());
      int pitch = 25;                     //pitch is how low or high the note is (1-127)  
      int velocity = 60;                  //velocity is how loud the sound is (0-127)
      channels[ch].noteOn(pitch, velocity);
   }
   
   //sound for getting a perfect run in a powerup stage
   public static void perfect(int ch)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      Sound.silence(ch);           
      int instrument = ORCH;
      int pitch = scale[(int)(Math.random() * scale.length)]; 
      int velocity = 70;                                     
      channels[ch].programChange(instr[instrument].getPatch().getProgram());
      channels[ch].noteOn(pitch, velocity);
   }
   
   //sound for getting powerup
   public static void powerup(int playerNum, boolean rightColor)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      Sound.silence(playerNum);           //this sound gets priority over flapping sound
      int instrument = BELL;
      int pitch = scale[(int)(Math.random() * scale.length)]; 
      int velocity = 50;                                     
      if(!rightColor)
      {
         instrument = SAX;
         pitch = (int)(Math.random() * 5) + 35;
      }
      channels[playerNum].programChange(instr[instrument].getPatch().getProgram());
      channels[playerNum].noteOn(pitch, velocity);
   }
   
   //sound for pipe collision
   public static void crash(int playerNum)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      int instrument = TIMPANI;
      int pitch = (int)(Math.random() * 20) + 35;                     
      int velocity = 70;                 
      if(playerNum == 1)
      {
         instrument = CRASH;
         pitch = 35;
         velocity = 70;
      }
      else if(playerNum == 2)
      {
         instrument = CRASH;
         pitch = 30;
         velocity = 60;
      }
      channels[playerNum].programChange(instr[instrument].getPatch().getProgram());
      channels[playerNum].noteOn(pitch, velocity);
   }
   
   //sound of bird after collision
   public static void bird(int playerNum)
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      channels[playerNum].programChange(instr[BIRD].getPatch().getProgram());
      int pitch = 65;                     //pitch is how low or high the note is (1-127)  
      int velocity = 40;                  //velocity is how loud the sound is (0-127)
      if(playerNum == 1)
      {
         pitch = 70;
         velocity = 35;
      }
      else if(playerNum == 2)
      {
         pitch = 60;
         velocity = 30;
      }
      channels[playerNum].noteOn(pitch, velocity);
   }
   
   //sound of bird after collision
   public static void flap(int playerNum) 
   {
      if(!FlappyPanel.soundOn)
      {
         return;
      }
      int instrument = FLAP;
      if(FlappyPanel.realm == FlappyPanel.WATER)
      {
         instrument = OCAR;
      }
      int basePitch = 63;
      if(playerNum == 1)
      {
         instrument++;
         basePitch = 73;
         if(FlappyPanel.realm == FlappyPanel.WATER)
         {
            instrument = OCAR;
            basePitch = 73;
         }
      }
      else if(playerNum == 2)
      {
         instrument--;
         basePitch = 68;
         if(FlappyPanel.realm == FlappyPanel.WATER)
         {
            instrument = OCAR;
            basePitch = 53;
         }
      }
      channels[playerNum].programChange(instr[instrument].getPatch().getProgram());
      int pitch = (int)(Math.random()*10)+basePitch;            //pitch is how low or high the note is (1-127)  
      if(FlappyPanel.powerupsOnScreen())
      {
         pitch = scale[(int)(Math.random() * scale.length)] - OCTAVE;
      }
      int velocity = (int)(Math.random()*5) + 50;               //velocity is how loud the sound is (0-127)
      channels[playerNum].noteOn(pitch, velocity);
   } 
}