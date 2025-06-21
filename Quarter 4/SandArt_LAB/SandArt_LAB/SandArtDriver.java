import javax.swing.JFrame;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;

public class SandArtDriver						                     //Driver Program
{
   public static SandArtPanel screen;
   public static JFrame frame;

   public static void main(String[]args)
   {
      screen = new SandArtPanel();
      frame = new JFrame("DUE TO INCLEMENT WEATHER, ALL FAIRFAX COUNTY PUBLIC SCHOOLS WILL BE CLOSED TODAY");	//window title
      frame.setSize(700, 700);				                     //Size of game window
      frame.setLocation(50, 50);				                     //location of game window on the screen
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      frame.addKeyListener(new KListen());	                  //Get input from the keyboard
   	//make the regular mouse cursor transparent so we can use a custom one in the MMMPanel that changes depending on direction
      BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);  
      Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "MMM cursor");  
      frame.getContentPane().setCursor(blankCursor);
   }
   
   private static boolean shiftIsPressed=false; 
   
   public static class KListen implements KeyListener 
   {   
      public void keyTyped(KeyEvent e)                         //only activates with keys that can be printed
      {}
      
      public void keyPressed(KeyEvent e)                       //activates when any key is hit
      {
         if(e.getKeyCode()==KeyEvent.VK_F12 && frame!=null)
         {
            makeScreenshot();
         }
         if(e.getKeyCode()==KeyEvent.VK_SHIFT)
            shiftIsPressed=true;
         screen.processUserInput(e.getKeyCode(), shiftIsPressed);
      }
   
      public void keyReleased(KeyEvent e)                      //activates when any key is released
      {
         if(e.getKeyCode()==KeyEvent.VK_SHIFT)
            shiftIsPressed=false;
      }
   }
   
   //post: creates a screenshot of the canvas in the frame, ouputted in screenShot.png within the same folder as the code
   public static final void makeScreenshot()
   {
      Dimension frameSize = frame.getSize();
      int xShift = 7;      //accomodates for the size of the frame around the picture
      int yShift = 30;
      Rectangle rect = new Rectangle(frame.getX()+xShift, frame.getY()+yShift, SandArtPanel.getPanelWidth(), SandArtPanel.getPanelHeight());
      try
      {
         Robot karel = new Robot();
         BufferedImage image = karel.createScreenCapture(rect);
         File imageFile = new File("screenShot.png");
         boolean exists = imageFile.exists();
         ImageIO.write(image, "png", imageFile);
         if(!exists)
         {
            System.out.println("Screenshot saved in "+ imageFile); 
         }
         else
         {
            System.out.println("Screenshot overwritten in "+ imageFile);   
         }
      }
      catch (Exception e)
      {
         System.out.println("Something went wrong: screenshot not saved");
      }
   }
}
