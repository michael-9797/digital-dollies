
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.*;
import javax.swing.*;
import javax.swing.text.html.StyleSheet;

public class DigitalDoilies
{
    Doilies doily = new Doilies();
    protected JFrame mainFrame;
    protected JPanel gallery;
    protected JScrollPane scrollPane;
    protected ArrayList<JLabel> temp = new ArrayList<JLabel>(12);

    //Creating the new DigitalDoilies triggers the constructor to execute.
    public static void main(String[] args)
    {
        DigitalDoilies z = new DigitalDoilies();
    }

    public DigitalDoilies()
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
                {
                    ex.printStackTrace();
                }
                //Calls upon the instructions to build the main display for drawing and configuring.
                createMainDisplay();
            }
        });
    }

    //The following method is responsible for constructing the main display for the doily to draw on.
    public void createMainDisplay()
    {
        mainFrame = new JFrame("Digital Doilies");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setResizable(false);
        mainFrame.setPreferredSize(new Dimension(800,600));

        //Creating the display for drawing on.
        JPanel displayGrid = new JPanel();
        displayGrid.setPreferredSize(new Dimension(1000,500));
        displayGrid.add(doily);

        //Creating the Panel to hold all the configuration buttons
        JPanel settings = new JPanel();
        settings.setLayout(new FlowLayout());

        /*********************Adding each setting (such as buttons) to the panel********************/

        //Adding the pen's colour change functionality.
        JLabel colourLabel = new JLabel("Colour:");
        settings.add(colourLabel);
        //This creates a drop down list of colours for a user to specify the pen drawing colour.
        JComboBox changeColour = new JComboBox();
        ArrayList<String> colours = new ArrayList<String>(13);
        colours.add("White");
        colours.add("Blue");
        colours.add("Cyan");
        colours.add("DarkGray");
        colours.add("Gray");
        colours.add("Green");
        colours.add("Yellow");
        colours.add("LightGray");
        colours.add("Magenta");
        colours.add("Orange");
        colours.add("Pink");
        colours.add("Red");
        colours.add("Black");

        //Adds all the colour options to the ComboBox
        for (String s : colours)
        {
            changeColour.addItem(s);
        }
        settings.add(changeColour);

        //Adds functionality to change the pen size.
        JLabel sizeLabel = new JLabel("Pen Size:");
        settings.add(sizeLabel);
        JTextField penSize = new JTextField("size");
        settings.add(penSize);

        //Specifying how many sector lines to show.
        JLabel sectors = new JLabel("No Sectors:");
        settings.add(sectors);
        JTextField specifySectors = new JTextField("12");
        settings.add(specifySectors);

        //Toggling the sector lines to show.
        JButton toggleSectorLines = new JButton("Toggle");
        settings.add(toggleSectorLines);

        //Saving a drawing to the gallery.
        JButton save = new JButton("Save");
        settings.add(save);

        //Adding the clear display button.
        JButton clear = new JButton("Clear");
        settings.add(clear);

        //Undoing the last point drawn.
        JButton undo = new JButton("Undo");
        settings.add(undo);

        //This adds the option to remove an image, according to its postion in the gallery.
        JButton removeImage = new JButton("Remove Image:");
        settings.add(removeImage);
        String[] imagesByNumber = {"1","2","3","4","5","6","7","8","9","10","11","12"};
        JComboBox removeSelectedImage = new JComboBox(imagesByNumber);
        settings.add(removeSelectedImage);

        //Creating the Panel to store saved drawings.
        gallery = new JPanel();
        gallery.setLayout(new FlowLayout(12));
        gallery.setBackground(Color.BLUE);
        for(JLabel l : temp)
        {
            gallery.add(l);
        }
        
        //This is so 12 images can fit in a restricted panel size.
        scrollPane = new JScrollPane(gallery);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(mainFrame.getWidth(),110));

         /*Below adds the three newly created Panels, putting the settings at the top, the drawing panel as the centre focus,
         and having the gallery as a bottom bar*/
        mainFrame.add(displayGrid, BorderLayout.CENTER);
        mainFrame.add(settings,BorderLayout.NORTH);
        mainFrame.add(scrollPane,BorderLayout.SOUTH);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        /**************************** Listeners *******************************/

        //This takes a users input from a textfield, and changes the amount of sectors to display accordingly.
        specifySectors.addActionListener(new ActionListener()
        {
            double d;

            //Checks whether the sectors to display has been specified with an integer.
            public boolean isNumeric(String s)
            {
                try
                {
                    d = Double.parseDouble(s);
                }
                catch(NumberFormatException nFE)
                {
                    return false;
                }
                return true;
            }
            public void actionPerformed(ActionEvent specifiedSectors)
            {
                if(isNumeric(specifySectors.getText()) && d > 0)
                {
                    doily.setNLines((int) d);
                    doily.repaint();
                }
            }
        });

        /**************************** Listeners ******************************/

        //When clicked, this will clear all points stored in Doilies, and reprinting the display to show a clear grid for drawing on.
        clear.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent clearDisplay)
            {
                doily.clearPoints();
                doily.repaint();
            }
        });

        /**************************** Listeners ******************************/
        
        //When the undo button is clicked, a maximum of 3 points are removed at a time.
        undo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent undoLastPoint)
            {
                //Checks for whether there is no points to undo
                if((int)doily.getPoints().size() == 0)
                {
                    System.out.println("No points to undo");
                }
                else
                {
                    doily.removeLastPoint();
                    doily.repaint();
                }
            }
        });

        /**************************** Listeners ******************************/

        //This saves the main panel to a buffered image, scaling it down and then printing it to the gallery panel.
        save.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent saveDrawing)
            {
                int x = (int) (0.2 * displayGrid.getWidth());
                int y = (int) (0.2 * displayGrid.getWidth());
                BufferedImage img = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = img.createGraphics();
                g2d.scale(0.2, 0.2);
                displayGrid.printAll(g2d);
                g2d.dispose();
                addToJLabels(new JLabel((new ImageIcon(img))));

                gallery.revalidate();
                gallery.repaint();
                mainFrame.pack();
            }
        });

        /**************************** Listeners ******************************/

        //The following removes images from the gallery panel.
        removeImage.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent removeImageFromGallery)
            {
                String x = removeSelectedImage.getSelectedItem().toString();
                int slotToRemove = Integer.parseInt(x);
                removeFromGallery(slotToRemove);
                
            }
        });

        /**************************** Listeners ******************************/

        toggleSectorLines.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent removeSectorLines)
            {

                doily.setNLines(1);
                doily.setReflector();
                displayGrid.repaint();
            }
        });

        /**************************** Listeners ******************************/
        
        //This is responsible for recording the users colour choice, this will in turn change the colour of the display(points and grid)
        changeColour.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent changePenColour)
            {
                System.out.println("dnsja");
                //A local variable that will be sent to Doiles to change a point's colour.
                Color colourChanger = null;

                //Retrieves the selected colour from the combobox.
                String colourAsTextChoice = (String) changeColour.getSelectedItem();

        		/*This compares the choice made in the combobox, with the slot in the colour array, to parse into
        		setPenColour(Color c)*/
                Color[] coloursToChange = {Color.white,Color.blue,Color.cyan,Color.darkGray,Color.gray,Color.green,Color.yellow,
                        Color.lightGray,Color.magenta,Color.orange,Color.pink,Color.red,Color.black};

                //Cycles through the String array of colours, and selects the corresponding array slot in 'coloursToChange'.

                for(String s : colours)
                {
                    System.out.println(colours.size());
                    if(colourAsTextChoice.equals(s))
                    {
                        System.out.println(colourAsTextChoice);
                        System.out.println(s);
                        //Retrieves the actual colour from the array (by comparing to the colours written as strings)
                        colourChanger = coloursToChange[colours.lastIndexOf(s)];
                        System.out.println(colours.lastIndexOf(s));
                    }else {
                    }
                }
                doily.setPenColour(colourChanger);
                doily.revalidate();
                doily.repaint();
            }
        });

        /**************************** Listeners ******************************/

        //This is responsible for altering the pen size when a new DoilyPoint is drawn.
        penSize.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent changePenSize)
            {
                String penSizeStorer = penSize.getText();
                //This attemps to retrieve the text in the text field, to parse its numerical value as the new pen size.
                try
                {
                    int size = Integer.parseInt(penSizeStorer);
                    //Checks for an invalid pen size, and alerts the user if it is invalid.
                    if (size <= 0)
                    {
                        JOptionPane.showMessageDialog(null,"Pen size must be greater than 0 to be seen");
                    }
                    doily.setPenSize(size);
                }
                //The following is a contingency in case the user accidentally specified a letter for the pen size.
                catch (NumberFormatException e)
                {
                    JOptionPane.showMessageDialog(null,"Pen size specified is NOT a number");
                }
                displayGrid.repaint();
            }
        });
    }

    /*The following is called upon in the 'save' ActionListener, adding the saved bufferedImage (of displayGrid)
    to the gallery panel*/
    public void addToJLabels(JLabel toAdd)
    {
        //Ensures a maximum of 12 images can be stored on the gallery.
        if(temp.size() < 12)
        {
            //Adds the bufferedImages to an array of JLabels.
            temp.add(toAdd);
            for(JLabel l : temp)
            {
                gallery.add(l);
            }
        }
        //In the event a 13th image is attempted to be added, the error window below will appear.
        else
        {
            JOptionPane.showMessageDialog(null,"Gallery full, please remove an Image first.");
        }
    }

    
    //A seperate method to implement removing from a gallery, to not add congestion to the action listener of 'removeImage'
    public void removeFromGallery(int toRemove)
    {
    	if(temp.size()!=0)
    	{
    		if(temp.get(toRemove-1)!= null)
            {
                JLabel l = temp.get(toRemove-1);
                temp.remove(toRemove -1);
                gallery.remove(l);
            }
    	}
        
        

    }
}

