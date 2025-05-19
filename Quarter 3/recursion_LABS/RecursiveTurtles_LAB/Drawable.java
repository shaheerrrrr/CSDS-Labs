   /** a collection of common Turtle graphics methods
    *  d oberle 2/2024
    */
public interface Drawable
{
   /**
    * Moves the turtle backward by the distance without changing its direction.
    * @param distance distance to move backward
    * @return state change timestamp
    */
   public long backward(double distance);
   
   /**
    * Clears all the drawing that a turtle has done but all the turtle
    * settings remain the same. (color, location, direction, shape)
    */
   public void clear();

   /**
    * Gets the distance from the turtle to another position at (x,y).
    * @param x x coordinate of target location
    * @param y y coordinate of target location
    * @return distance between turtle's current location and another position at (x,y)
    */
   public double distanceTo(double x, double y);

   /**
    * Put a dot on the canvas of a particular color and size.
    * @param color color of dot
    * @param dotSize diameter of the dot
    * @return state change timestamp
    */
   public long dot(java.awt.Color color,double dotSize);

   /**
    * Moves the turtle forward by the distance.
    * @param distance distance to move forward
    * @return state change timestamp
    */
   public long forward(double distance);

   /**
    * Gets the direction the turtle is facing neglecting tilt.
    * @return direction in degrees
    */
   public double getDirection();

   /**
    * Gives back the turtle's pen color.
    * @return  the color of the line that the turtle will draw.
    */
   public java.awt.Color getPenColor();

  /**
   * Gives back the turtle's pen width
   * @return the width of the turtle's trail
   */
   public double getPenWidth();

   /**
    * Gets the x-coordinate of the turtle.
    * @return x-coordinate
    */
   public double getX();
   
   /**
    * Gets the y-coordinate of the turtle.
    * @return y-coordinate
    */
   public double getY();

   /**
    * Sets the position of a turtle.
    * @param x x coordinate
    * @param y y coordinate
    * @return state change timestamp
    */
   public long goTo(double x, double y);

   /**
    * Hides the turtle but it can still draw.
    * @return state change timestamp
    */
   public long hide();

   /**
    * Moves the turtle to (0,0) and facing east.
    * @return state change timestamp
    */
   public long home();

   /**
    * Determines if a turtle is covering a screen position
    * @param x x screen coordinate
    * @param y y screen coordinate
    * @return true if this turtle is at the indicated screen position.
    */
   public boolean isAt(int x, int y);

   /**
    * Turns the turtle left by the number of indicated degrees.
    * @param angle angle in degrees
    * @return state change timestamp
    */
   public long left(double angle);

   /**
    * Sets the turtle's path color to one that is similar to its current one, but moving towards a target color.
    * @param targetColor  Color that we want to move our penColor towards.
    * @param numSteps  the number that we are allowed to change the R,G,B values by
    * @return state change timestamp
    */
   public long moveColorTo(java.awt.Color targetColor, int numSteps);

   /**
    * Sets the turtle's path color.
    * @param penColor Color of the turtle's path.
    * @return state change timestamp
    */
   public long penColor(java.awt.Color penColor);

   /**
    * Puts the turtle's pen down so it will draw on the screen as it moves.
    * @return state change timestamp
    */
   public long penDown(); 

   /**
    * Sets the width of the turtles path
    * @param width width of the turtles path
    * @return state change timestamp
    */
   public long penWidth(double width);

   /**
    * Picks the turtle's pen up so it won't draw on the screen as it moves.
    * @return state change timestamp
    */
   public long penUp(); 

   /**
    * Turns the turtle right by the number of indicated degrees.
    * @param angle angle in degrees
    * @return state change timestamp
    */
   public long right(double angle);

   /**
    * Sets the direction the turtle is facing neglecting tilt.
    * @param direction angle counter-clockwise from east
    * @return state change timestamp
    */
   public long setDirection(double direction);

   /**
    * Sets the position and direction of a turtle.
    *
    * @param x x coordinate
    * @param y y coordinate
    * @param direction angle counter-clockwise from east in degrees
    * @return state change timestamp
    */
   public long setPosition(double x, double y, double direction);

   /**
    * Sets the shape of the turtle using the built in shapes 
    * ("turtle","square","rectangle","triangle","arrow","circle") or to a image filename.
    * @param shape shapename or filename of image
    * @return state change timestamp
    */
   public long setShape(String shape);

   /**
    * Makes the turtle visible.
    * @return state change timestamp
    */
   public long show();

   /**
    * Sets the speed of the animation.
    * @param delay milliseconds it takes to do one action
    * @return state change timestamp
    */
   public long speed(double delay);

   /**
    * Put a copy of the current turtle shape on the canvas.
    * @return state change timestamp
    */
   public long stamp();

   /**
    * Sets the direction in such a way that it faces (x,y)
    *
    * @param x x coordinate of target location
    * @param y y coordinate of target location
    * @return state change timestamp
    */
   public long turnTowards(double x, double y);

   /**
    * Undo turtle state changes.
    * @param steps the number of state changes to remove
    */
   public void undo(int steps);

   /**
    * Undo the last turtle state change.
    */
   public void undo();

   
  /**
   * STATIC METHODS AVAILABLE TO CALL FROM Turtle.java
   * Sets the background color.
   * @param color Color of the background.
   public static void backgroundColor(Color color);
      
   * Set the background image.
   * @param filename filename for a background image
   public static void backgroundImage(String filename);  
     
   * Gets the distance between two points.
   * @param x1 x coordinate of first location
   * @param y1 y coordinate of first location
   * @param x2 x coordinate of second location
   * @param y2 y coordinate of second location
   * @return distance between (x1,y1) and (x2,y2)
   public static double distanceTo(double x1, double y1, double x2, double y2); 
     
   * Given Color from and Color to, gives back a new Color that is increment closer between the two
   * @param from  the start color that we are attempting to change in the direction of the Color to
   * @param to    the color that we want to move the Color from towards
   * @param increment  the amount by which the color fields can change in this method call
   * @return  the color that is increment steps different for R,G,B in the Color from going towards the color to 
   public static Color moveColorTowards(Color from, Color to, int increment);
   */
}