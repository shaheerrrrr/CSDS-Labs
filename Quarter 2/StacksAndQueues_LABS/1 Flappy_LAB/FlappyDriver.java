//Curtis Grover, D Oberle
//This is the file that runs the program
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class FlappyDriver					   
{       
   public static FlappyPanel screen;					            //Our Custom JPanel

   public static void main(String[]args) throws IOException
   {
      screen = new FlappyPanel();                              //window title
      JFrame frame = new JFrame("P1:mouse click, P2:SPACE, P3:ENTER, (P)ause");	               
      int width = 500, height = 685;
      frame.setSize(width, height);			                     //Size of game window
      frame.setLocation(400, 100);				                  //location of game window on the screen
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      frame.setResizable(false);
      frame.addKeyListener(screen);		                        //KeyListeners go on the JFrame.  MouseListeners would go in the JPanel (screen)
   } 
}
