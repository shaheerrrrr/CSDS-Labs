import javax.swing.JFrame;                         //allows for panels and timers
import java.awt.event.KeyListener;                 //allows us to get real-time input from the keyboard
import java.awt.event.KeyEvent;

public class CultimaDriver						         //Driver Program
{
   public static JFrame frame;                     //the frame for our game
   public static CultimaPanel screen;				   //the contents that we will fill the frame with
   public static int width, height;                //resizable dimensions

   public static void main(String[]args)
   {
      screen = new CultimaPanel();
      frame = new JFrame("");	                     //window title
      width = 50*screen.SIZE+30;                   //screen.SIZE is the pixel size of each game cell to be drawn
      height = 26*screen.SIZE+30;
      frame.setSize(width, height);					   //Size of game window
      frame.setLocation(0, 0);				         //location of game window on the screen
      frame.setExtendedState(JFrame.NORMAL);  	   //MAXIMIZED_BOTH, MAXIMIZED_VERT, or ICONIFIED
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      frame.addKeyListener(new KBListen());		   //listen for input from the keyboard
   }

   public static class KBListen implements KeyListener 
   {
      public void keyTyped (KeyEvent e)
      { }                                          //not used: only for keys that can be printed (alpha-numerics, symbols), no FN keys, SHIFT, etc
   
      public void keyPressed(KeyEvent e)
      { 
         if(e.getKeyCode()==KeyEvent.VK_SHIFT)
         {
            screen.shiftPressed=true;              //SHIFT used with FN keys to assign hot keys and sprint/automove
         }
         else if(e.getKeyCode()==KeyEvent.VK_ALT)
         {
            screen.altPressed=true;                //we now have ALT available for extra functionality to be added
         }
      }
   
      public void keyReleased(KeyEvent e)
      {
         int k = e.getKeyCode();
         if(k==KeyEvent.VK_PLUS || k==KeyEvent.VK_EQUALS || k==KeyEvent.VK_ADD)		
         {                                         //larger screen size
            screen.SIZE += 2;
            width = 50*screen.SIZE+30;
            height = 26*screen.SIZE+30;
            frame.setSize(width, height);				
            frame.setExtendedState(JFrame.NORMAL); 
            frame.repaint();
         }
         else if(k==KeyEvent.VK_MINUS || k==KeyEvent.VK_SUBTRACT)							
         {                                         //smaller screen size
            screen.SIZE -= 2;
            width = 50*screen.SIZE+30;
            height = 26*screen.SIZE+30;
            frame.setSize(width, height);				
            frame.setExtendedState(JFrame.NORMAL); 
            frame.repaint();
         }
         else if(k==KeyEvent.VK_SHIFT)
         {                                         //SHIFT used with FN keys to assign hot keys and sprint/automove
            screen.shiftPressed=false;
         }
         else if(k==KeyEvent.VK_ALT)
         {                                         //we now have ALT available for extra functionality to be added
            screen.altPressed=false;   
         }
         else
         {                                         //any other key we hit, send to the panel so that the game can respond if it is meaningful
            screen.processUserInput(k);
         }
      }
   }
}
