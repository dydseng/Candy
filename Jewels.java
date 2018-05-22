//Casey McNamara
//5/13/2009
//Plays Bejeweled

import javax.swing.*;		//Basic Swing
import java.awt.*;			//GridLayout and Color
import java.awt.event.*;	//ActionListener
import java.util.*;			//Scanner

public class Jewels implements ActionListener
{

	private JFrame frame;						//A window
	private JButton pause;						//The button to pause/resume the game
	private JTextField livesField;				//The text field that shows how many lives you have left
	private JPanel buttons;						//A panel in the window.  Holds the visual 
													//representation of the jewels
													//(lots of buttons).
	private DecayingTimer timeBar;				//The timer at the top (it's a panel) 
	private int width, height;					//The number of squares wide and tall the jewelfield is
	
	private PriorityQueue<Jewel> toDie;			//the jewels that are waiting to die (bottom to top, left to right)
	private javax.swing.Timer delayedGravity;	//a timer for clock ticks between jewels dying
	
	private int lives;							//the number of lives the player has left
	
	private Map<Spot, Jewel> jewelsMap;			//map linking spots to jewels
	private Map<Spot, JButton> buttonMap;		//map linking spots to buttons
	private static final int BUTTON_SIZE = 52;	//size (width, height) of buttons
	
	private Spot selected;						//the selected spot (to be swapped with the next spot clicked)
	
	private boolean dyingMode;					//mode: jewels are falling (disable clicking, timeBar counts)
	private long timeOfLastDestruction;			//time at which the death of a set of jewels last resolved (exists
													//so that there will be a pause between triples lighting up
													//and them dying)
	private Set<Spot> marked;					//set of spots at which jewels are going to die

	//constructor for the game
    public Jewels() 
    {
    	width = 10;	
    	height = 10;
    	lives = 3;
    	buttonMap = new TreeMap<Spot, JButton>();
    	jewelsMap = new TreeMap<Spot, Jewel>();
    	toDie = new PriorityQueue<Jewel>();
    	delayedGravity = new javax.swing.Timer(50, this);		// 50 milliseconds between jewels dying

    	selected = null;
    	dyingMode = false;
		init();													// do the graphical junk
    }
      
    //ActionPerformed.  Yay.
    //There are timer action events, menu button events (reset, new game, pause/resume) and jewel-click events
    public void actionPerformed(ActionEvent e)
    {
    	if(e.getActionCommand() == null)	//jewel-death timer action event
    	{
    		if(System.currentTimeMillis() > timeOfLastDestruction + 400)	//some time has passed since
    		{																//the triples got highlighted
	    		if(!toDie.isEmpty())	//there are jewels waiting to die
	    		{
	    			dieJewel();			//kill the highest-priority jewel
	    		}
	    		else					//no jewels are left to die
	    		{
	    			if(dyingMode)		//end Dying Mode		
	    			{	
	    				timeBar.resume();	//resume counting life-losing time
	    				dyingMode = false;
	    				change();		//now that everything has fallen, check for more triples to kill
	    			}
	    		}
    		}
    	}
    	else if(e.getActionCommand().equals("reset"))	//reset the board
    	{
    		die();										//lose one life
    		timeBar.reset();							//set the life-loss timer back to 0% expired
    		timeBar.pause();							
    		initializeJewels();							//put random jewels everywhere and resolve any triples;
    													// don't count time while this happens
    		timeBar.resume();
    	}
    	else if(e.getActionCommand().equals("new game"))//start a new game
    	{
    		timeBar.reset();							//set the life-loss timer back to 0% expired	
    		timeBar.pause();
    		initializeJewels();							//put random jewels everywhere and resolve any triples;
    													// don't count time while this happens
    		timeBar.resume();
    		lives = 3;
    		livesField.setText(lives + " lives");		//set lives back to 3
    	}
    	else if(e.getActionCommand().equals("pause"))	//pause the game
    	{
    		timeBar.pause();							//pause the life-loss timer
    		Set<Spot> spots = buttonMap.keySet();		//blackout all the buttons
    		for(Spot s : spots)
    			buttonMap.get(s).setIcon(null);
    		pause.setText("Resume");					//change pause button to resume button
    		pause.setActionCommand("resume");
    	}
    	else if(e.getActionCommand().equals("resume"))	//resume the game
    	{
    		Set<Spot> spots = buttonMap.keySet();		//un-blackout all the buttons
    		for(Spot s : spots)
    			buttonMap.get(s).setIcon(jewelsMap.get(s).getIcon());
    		timeBar.resume();							//resume the life-loss timer
    		pause.setText("Pause");						//change resume button to pause button
    		pause.setActionCommand("pause");
    	}
    	else if(!dyingMode)								//jewel button was clicked; not in the middle of jewels dying
    	{
	    	Scanner in = new Scanner(e.getActionCommand());
	    	int r = in.nextInt();
	    	int c = in.nextInt();
	    	if(selected == null)	//if nothing's selected, select the jewel clicked
	    	{
	    		selected = new Spot(r, c);
	    		buttonMap.get(selected).setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
	    	}
	    	else					//if something's selected, swap the selected jewel with the one just clicked
	    	{
	    		Spot s = new Spot(r, c);
	    		if(s.isAdjacentTo(selected))	//swap only if they're adjacent
	    		{
	    			swap(s, selected);
	    			selected = null;
	    		}
	    		else							//otherwise select the jewel just clicked
	    		{
	    			buttonMap.get(selected).setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
	    			selected = s;
	    			buttonMap.get(selected).setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
	    		}
	    	}
    	}
    }
    
    //Adds a jewel to the priority queue of jewels that will die (lower ones die earlier)
    public void addToDieQueue(Jewel j)
    {
    	if(j != null)
    		toDie.add(j);
    }
    
    //Add everything in the arraylist to the set
    public void append(Set set, ArrayList arrayList)
    {
    	for(Object o : arrayList)
    	{
    		set.add(o);
    	}
    }
    
    //Find all triples (or larger rows/columns of identical jewels) and deal with them.  Return true if any exist.
    public boolean change()
    {
    	Set<Spot> spots = jewelsMap.keySet();
    	marked = new TreeSet<Spot>();
    	for(Spot spot : spots)	//Look at each spot to see if it's part of a triple
    	{
    		String wanted = jewelsMap.get(spot).getColor();	//color of the spot (the color we want)
    		for(Spot test : spot.getAdjacentSpots())		//Look at each adjacent spot to our current spot
    		{
    			if(spot.inBounds(height, width) && test.inBounds(height, width))	//Presuming they're both inbounds...
    			{
	    			String testColor = jewelsMap.get(test).getColor();
	    			if(testColor.equals(wanted))			//If the color of the adjacent spot is the one we want
	    			{
	    				int[] transform = spot.getTransformTo(test);	//get the direction from first spot to second spot
	    				ArrayList<Spot> inLine = new ArrayList<Spot>();
	    				inLine.add(spot);
	    				inLine.add(test);
	    				Spot test1 = test.transform(transform);
	    				while(test1.inBounds(height, width) &&//Going in the direction from original spot to adjacent spot,
	    					jewelsMap.get(test1).getColor().equals(wanted))//add, to the arraylist representing the line,
	    				{									//all the jewels that are in the line and have the same color 
	    					inLine.add(test1);				//until you encounter one that does not
	    					test1 = test1.transform(transform);	
	    				}
	    				Spot test2 = spot.transform(Spot.getInverse(transform));
	    				while(test2.inBounds(height, width) &&	//same, but in the opposite direction
	    					jewelsMap.get(test2).getColor().equals(wanted))
	    				{
	    					inLine.add(test2);
	    					test2 = test2.transform(Spot.getInverse(transform));	
	    				}
	    				if(inLine.size() >= 3)	//if there is a triple, add it to the set of jewels marked to die
	    					append(marked, inLine);
	    			}
    			}
    		}
    	}
    	if(marked.size() > 0)	//if any jewels are marked to die, set up their death, and return true: change shall occur
    	{
    		dropDown();
    		return true;
    	}
    	return false;			//return false: there will be no change
    }
    
    //Kills the player (lose one life; game over, if lives have dropped to zero)
    public void die()
    {
    	lives--;
    	livesField.setText(lives + " lives");
    	if(lives <= 0)
    	{
    		System.out.println("You lose");
    		frame.dispose();
    	}
    }
    
    //Kills the next jewel to die (taking it off the queue)
    public void dieJewel()
    {
    	Jewel j = toDie.remove();
    	if(j != null)
    		j.die();
    }
    
    //Set up the death of all jewels marked to die
    public void dropDown()
    {
    	timeBar.pause();
    	Set<Jewel> jewels = new HashSet<Jewel>();
    	for(Spot spot: marked)	//get all the jewels marked to die, and outline them in yellow
    	{
    		jewels.add(jewelsMap.get(spot));
    		jewelsMap.get(spot).mark();
    		updateButton(spot);
    	}
		timeOfLastDestruction = System.currentTimeMillis();	//mark the time, so that we'll get to see the pretty yellow
															//for a bit
    	for(Jewel jewel: jewels)
    	{
    		addToDieQueue(jewel);		//Add all the marked jewels to the list of those waiting to die
    		timeBar.addTime(.01);		//Reward the player for having killed the jewels by giving them more life
    	}
    	dyingMode = true;				//Begin dying mode: the jewels will start to fall
    	timeBar.pause();				//Pause the life-loss timer so that you don't get penalized for this occuring
    }
    
    //sets up all the graphical junk.  Yes, it's long.
    public void init()
    {
    	JPanel bigPanel = new JPanel();									//Big panel that holds everything
    	bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
    	
    	JPanel menu = new JPanel();										//Top panel (buttons, lives)
    	menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
    	livesField = new JTextField(lives + " lives");					//Initialize the life field
    	livesField.setEditable(false);									//You don't get to alter your lives :)	
    	
    	JPanel menuButtons = new JPanel();								//Left side of top panel (buttons)
    	menuButtons.setLayout(new BoxLayout(menuButtons, BoxLayout.Y_AXIS));
    	JButton newGame = new JButton("New Game");						//Button to start a new game
    	newGame.addActionListener(this);
    	newGame.setActionCommand("new game");
    	JButton buyReset = new JButton("Reset the board (costs one life)");	//Button to reset the board 
    																			//(in case no moves are left)
    	buyReset.addActionListener(this);
    	buyReset.setActionCommand("reset");
    	pause = new JButton("Pause");									//Button to pause the game
    	pause.addActionListener(this);
    	pause.setActionCommand("pause");
    	menuButtons.add(newGame);										//Add the buttons to the left panel
    	menuButtons.add(buyReset);
    	menuButtons.add(pause);
    	menu.add(menuButtons);											//Add the left panel and right text field to the
    																		//top panel
    	menu.add(livesField);
    	bigPanel.add(menu);												//Add the top panel to the big panel
    	
    	timeBar = new DecayingTimer("timeBar.JPG", this);				//Make the timer panel (red thing that counts down)
    	timeBar.setSize(new Dimension(width * BUTTON_SIZE, 
    		(int)(timeBar.getSize().getHeight())));
    	timeBar.setPercentExpired(0);
    	bigPanel.add(timeBar);											//Add the timer panel to the big panel
    	
    	buttons = new JPanel();											//Make the panel of buttons
    	buttons.setLayout(new GridLayout(width, height));
    	buttons.setPreferredSize(new Dimension(width * BUTTON_SIZE, height * BUTTON_SIZE));
    	makeButtons();
    	bigPanel.add(buttons);											//Yeah, add them too
    	
    	frame = new JFrame("Jewel Game");								//Make the frame
    	frame.add(bigPanel);
    	frame.setSize(new Dimension(width * BUTTON_SIZE, 
    		(int)(height * (BUTTON_SIZE + 5) + timeBar.getPreferredSize().getHeight() + 
    			menu.getMinimumSize().getHeight())));					//Just the right size to fit everything
    	frame.setResizable(false);
    	frame.setVisible(true);
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	delayedGravity.start();  										//Start the falling timer
    	change();														//Get rid of any preexisting triples
    	timeBar.startCounting();										//Start the life-losing timer
    }
    
    //called to reset the board: puts random jewels everywhere, updates the buttons, and resolves any triples
    public void initializeJewels()
    {
    	for(int r = 0; r < height; r++)
    		for(int c = 0; c < width; c++)
    		{
    			Spot s = new Spot(r, c);
    			int color = (int)(Math.random() * Jewel.NUM_COLORS);
    			Jewel jewel = new Jewel(color, s, jewelsMap, this);
    			jewelsMap.remove(s);
    			jewelsMap.put(s, jewel);
    			updateButton(s);
    		}
    	change();
    }
    
    //called during initialization: puts a jewel at every spot, and sets up the button there
    public void makeButtons()
    {
    	for(int r = 0; r < height; r++)
    		for(int c = 0; c < width; c++)
    		{
    			Spot s = new Spot(r, c);
    			int color = (int)(Math.random() * Jewel.NUM_COLORS);
    			Jewel jewel = new Jewel(color, s, jewelsMap, this);
    			jewelsMap.put(s, jewel);
    			JButton button = new JButton();
    			button.setIcon(jewel.getIcon());
    			button.setBackground(Color.BLACK);
    			button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    			buttonMap.put(s, button);
    			buttons.add(button);
    			button.addActionListener(this);
    			button.setActionCommand(r + " " + c);
    		}
    }
    
    public void swap(Spot spot1, Spot spot2)
    {
    	//swap the two jewels (actually, swap their colors)
    	String hold = jewelsMap.get(spot1).getColor();
    	jewelsMap.get(spot1).setColor(jewelsMap.get(spot2).getColor());
    	jewelsMap.get(spot2).setColor(hold);
    	updateButton(spot1);
    	updateButton(spot2);
    	if(!change())	//if nothing happens, put them back again
    	{
    		jewelsMap.get(spot2).setColor(jewelsMap.get(spot1).getColor());
    		jewelsMap.get(spot1).setColor(hold);
    		updateButton(spot1);
    		updateButton(spot2);
    	}
    }
    
    //Update the button at the given spot
    public void updateButton(Spot s)
    {
		Jewel thing = jewelsMap.get(s);
		if(thing == null)
			buttonMap.get(s).setIcon(null);		//if there's no jewel there, there should be no icon
		else
		{
			buttonMap.get(s).setIcon(thing.getIcon());	//set the icon to the picture of the jewel there
			if(thing.isMarked())						//outline jewels marked to die in yellow
				buttonMap.get(s).setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
			else										//outline others in (invisible) black
				buttonMap.get(s).setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		}
    }
    
    //It's a main method.  YAY
    public static void main(String[] args)
    {
    	Jewels a = new Jewels();
    }
}