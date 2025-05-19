//d oberle 2024
//use turtle graphics to draw random concentric shapes
import java.awt.Color;
import java.awt.Point;
public class TurtleDemo
{
   public static void main(String[] args) 
   {
      double [] angles = {60, 70, 90, 120};              //random angles to pick from (triangle, square, pentagon, hexagon)
      Color [] spiderweb = {Color.white, Color.gray};    //arrays of colors to transition from and to
      Color [] rainbow = {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, new Color(200, 0, 255), Color.white};
      Color [] colors = null;
      while(true)                                        //go until we close the program
      {
         int angleIndex = (int)(Math.random()*angles.length);
         double angle = angles[angleIndex];              //pick a clean angle from the array angles
         if(Math.random() < 0.5)                         //50% chance that we pick a random angle
         {  
            angle = (Math.random()*100)+50;
         }
         double change = ((Math.random()*60)+20)/100.0;  //amount the angle of the turtle turn and length of vertex will change from one step to the next
         int colorStrategy = (int)(Math.random() * 3);   //0-ranbow colors, 1-grayscale, 2-random number and hue of colors
         if(colorStrategy == 0)                          //rainbow colors        
         {  
            colors = rainbow;
         }
         else if (colorStrategy == 1)                    //gray-scale
         {
            colors = spiderweb;
         }
         else //if(colorStrategy == 2)                   //random colors
         {
            int numColors = (int)(Math.random()*12) + 1; //1-12 colors
            colors = new Color[numColors];
            for(int i = 0; i < colors.length; i++)
            {
               colors[i] = new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));
            }
         }
         int colorChange = (int)(Math.random()*10)+1;    //step increase in which a color can transition from one to another  
         boolean widthChange = false;
         if(Math.random() < 0.5)                         //coin-flip pick whether line widths stay the same or get larger as they get closer to the edges
         {
            widthChange = true;
         } 
         int dotStyle = (int)(Math.random()*3);          //0-no dots, 1-draw lines and dots/stamps, 2-draw only dots/stamps
         colorSpiral(angle, change, colors, colorChange, widthChange, dotStyle);
      }
   }
   
   //PRE:  param angle is the angle for which the turtle will turn after moving, angle is between 50 and 150
   //      param change is the difference with the next distance and angle, change is between 0.2 and 0.8
   //      param colors is the collection of colors we want to traverse through, colors != null and colors has at least one element
   //      param colorChange is the step increase in which a color can transition from one to another, colorChange is between 1 and 10
   //      if widthChange is false, all vertexes will be the same pen width.  If true, the vertex thickness will depend on its distance from the origin.
   //      if drawDots is true, smidge will place a dot at the start of each vertex.
   //      param dotStyle is between 0 and 3 where 0==no dots, 1==draw lines and dots, 2==draw only dots
   public static void colorSpiral(double angle, double change, Color[]colors, int colorChange, boolean widthChange, int dotStyle) 
   {
      String [] shapes = {"turtle","square","rectangle","triangle","arrow","circle","brushShapes/spider1.png","brushShapes/spider2.png",
                          "brushShapes/turtle1.png","brushShapes/turtle2.png","brushShapes/turtle3.png","brushShapes/karel.png",
                          "brushShapes/star.png","brushShapes/rex.png","brushShapes/dragon.png","brushShapes/flower1.png","brushShapes/flower2.png"};
      Turtle.backgroundColor(Color.black);
      int colorIndex = (int)(Math.random()*colors.length);
      Turtle smidge = new Turtle();
      smidge.setShape(shapes[(int)(Math.random()*shapes.length)]);
      smidge.fillColor(new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)));
      double originX = smidge.getX();
      double originY = smidge.getY();
      smidge.penColor(colors[colorIndex]);
      boolean stamp = false;                             //coin flip to decide if we leave a dot at the end of each vertex, or a stamp of the turtle image
      if(Math.random() < 0.5)
      {
         stamp = true;
      }
      int dist = 0;                                      //the distance the turtle will travel on the next move
      while(!Turtle.outOfBounds(smidge))                 //keep drawing concentric shapes until we hit the edge of the window
      {
         if(dotStyle > 0)                                //maybe draw a dot at the start of the vertex
         {                                               //dotStyle == 0-no dots, 1-draw lines and dots/stamps, 2-draw only dots/stamps
            smidge.penDown();
            if(stamp)
            {
               if(widthChange)                           //if we are allowing the pen line width to change, also change the stamp and dot size
               {
                  smidge.shapeSize((int)smidge.getPenWidth()*10, (int)smidge.getPenWidth()*10);
               }
               smidge.stamp();
            }
            else
            {
               smidge.dot(smidge.getPenColor().brighter().brighter(), smidge.getPenWidth()*4);
            }
         }
         if(dotStyle == 2)                               //dotStyle == 0-no dots, 1-draw lines and dots/stamps, 2-draw only dots/stamps
         {
            smidge.penUp();
         }
         double distFromOrigin = smidge.distanceTo(originX, originY);
         if(widthChange)                                 //maybe set the width to a thickness dependant on the distance from the origin
         {
            double width = Math.max(1, distFromOrigin/50);
            smidge.penWidth(width);
         }
         
         smidge.forward(dist*(1+change));                //this algorithm is, in short, move, turn, repeat (with a slightly larger distance to move and angle to turn)
         smidge.left(angle+change);
         dist++;
                                                         //transition our pen color to move towards the next color in the colors array
         smidge.moveColorTo(colors[(colorIndex+1)%colors.length], colorChange);
         if(smidge.getPenColor().equals(colors[(colorIndex+1)%colors.length]))
         {                                               //we arrived at the next color in the colors array, so switch to the next index with wrap-around
            colorIndex = (colorIndex + 1) % colors.length;
         }
      }
      smidge.hide();                                     //we are done drawing our shape, so clear the screen for the next spider
      smidge.clear();
   }
}

