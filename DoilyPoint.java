import java.awt.Color;

public class DoilyPoint
{
    //These are the four properties of a point drawn by a Doily.
    private int size;
    private int x;
    private int y;
    private Color colour;

    public DoilyPoint(int size, int x, int y, Color colour)
    {
        this.size = size;
        this.x = x;
        this.y = y;
        this.colour = colour;
    }

	/*The following provide access to all the variables of the DoilyPoint, so that changes to
	a point's size, colour and position can be made*/

    //Size of a DoilyPoint
    public void setSize(int a)
    {
        this.size = a;
    }
    public int getSize()
    {
        return size;
    }

    //Coords of a DoilyPoint
    public void setX(int a)
    {
        this.x = a;
    }
    public int getX()
    {
        return x;
    }
    public void setY(int a)
    {
        this.y = a;
    }
    public int getY()
    {
        return y;
    }

    //Colour of a DoilyPoint
    public void setColor(Color r)
    {
        colour = r;
    }
    Color getColor()
    {
        return colour;
    }



}
