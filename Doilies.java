import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.JPanel;

public class Doilies extends JPanel implements MouseListener, ActionListener, MouseMotionListener
{
    //global variable declarations
    protected JPanel drawingPanel;
    protected int nlines = 12;//store the number of sector defining lines
    protected int currentOvalSize = 10;
    protected Color penColor;
    protected Deque<DoilyPoint> points = new LinkedList<DoilyPoint>();
    int lineLength;
    protected AffineTransform transformPoint;

    public Doilies()
    {

        //The following panel is responsible for drawing/painting points on a given sector grid.
        drawingPanel = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                lineLength = Math.max(getWidth(), getHeight());
                Point centerPoint = new Point(getWidth() / 2, getHeight() / 2);


                    /*The double theta varies according to how many sector lines are to be drawn, ensuring equal angles 
                    between each sector.*/
                double theta = Math.toRadians(360.0 / nlines);

                
                g.setColor(penColor);

                //calculate line coordinates and draw the sector lines
                for (int i = 0; i < nlines; i++)
                {
                	
                    g.drawLine(centerPoint.x, centerPoint.y,
                            centerPoint.x + (int) Math.round(lineLength * Math.cos(theta * i)),
                            centerPoint.y + (int) Math.round(lineLength * Math.sin(theta * i)));
                }
                double delta = 360.0 / (double) nlines;
                    
                    /*The following is responsible for drawing a point onto the display, and transforming it (by rotation)
                    through each sector*/
                Graphics2D gCopy = (Graphics2D) g.create();

                transformPoint = AffineTransform.getRotateInstance(Math.toRadians(delta),centerPoint.x,centerPoint.x);
                //Draws the points from the LinkedList, and transforming
                for (int h = 0; h < nlines; h++)
                {
                    for (DoilyPoint j : points)
                    {
                        gCopy.fillOval(j.getX(), j.getY(), j.getSize(), j.getSize());
                    }
                    //transforms the points through sectors
                    gCopy.transform(transformPoint);
                }
                revalidate();
                repaint();
            }
        };
        drawingPanel.setBackground(Color.BLACK);
        drawingPanel.addMouseMotionListener(this);
        drawingPanel.addMouseListener(this);
        setLayout(new BorderLayout());
        add(drawingPanel);
    }

    //Returns the panel that points are drawn on.
    public JPanel returnDoily()
    {
        return drawingPanel;
    }

    public void setReflector()
    {

    }

    //Grants access to all the points currently drawn.
    public Deque<DoilyPoint> getPoints()
    {
        return points;
    }

    //As the LinkedList implements a queue, to remove the last point it is necessary to remove the last point in the list.
    public void removeLastPoint()
    {
        if(points.size()>=5)
        {
            for(int i =0; i<3; i++)
                points.removeLast();
        }
        points.removeLast();
    }

    //Clears the LinkedList of doily points, so the user will have a blank canvas when clicking 'clear'.
    public void clearPoints()
    {
        points.clear();
    }

    //Grants access to the number of sector lines, modified by a user's input in the JTextField.
    public void setNLines(int i)
    {
        nlines = i;
    }

    public Dimension getPreferredSize()
    {
        return new Dimension(400, 400);
    }

    //The following listens for a mouse click or drag, and draws the points onto the JPanel accordingly.
    public void mouseClicked(MouseEvent e)
    {
        addPoint(e.getX(), e.getY());
    }
    public void mouseDragged(MouseEvent e)
    {
        addPoint(e.getX(), e.getY());
    }

    //This is responsible for adding a newly created point.
    public void addPoint(int x, int y)
    {
        points.add(new DoilyPoint(currentOvalSize, x, y, penColor));
    }

    //When entered into the JTextField, the numerical value specified will be sent here, to alter the pen's drawing size.
    public void setPenSize(int size)
    {
        currentOvalSize = size;
    }

    //This is responsible for changing the pens color, the argument is supplied from the JComboBox on the 'settings' panel.
    public void setPenColour(Color specifiedByUser)
    {
        penColor = specifiedByUser;
        revalidate();
        repaint();
    }

    //Generic mouse listeners, for specific mouse actions.
    public void mousePressed(MouseEvent e)
    {
    }
    public void mouseReleased(MouseEvent e)
    {
    }
    public void mouseEntered(MouseEvent e)
    {
    }
    public void mouseExited(MouseEvent e)
    {
    }
    public void actionPerformed(ActionEvent e)
    {
    }
    public void mouseMoved(MouseEvent e)
    {
    }
}