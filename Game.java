import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Game implements ActionListener {
    private JFrame frame;
    private JPanel candies;
    private int width;
    private int height;
    
    /* what is a priority queue? */
    private ArrayList<Candy> matched;
    private Map<Location, Candy> candyScreen;
    private Map<Location, JButton> buttonScreen;
    private static final int BUTT_SIZE = 52;
    
    private Location selected;
    private Set<Location> crushed;
    
    public Game() {
        width = 10;
        height = 10;
        buttonScreen = new TreeMap<Location, JButton>();
        candyScreen = new TreeMap<Location, Candy>();
        selected = null;
        /* put in extra graphical stuff later. */
    }
  
    public void moveMade(ActionEvent e) {
        if(!matched.isEmpty()) {
            crushCandy();
        }
        else {
            Scanner in = new Scanner(e.getActionCommand());
            int row = in.nextInt();
            int col = in.nextInt();
            
            if(selected == null)
                selected = new Location(row, col);
            else {
                Location other = new Location(row, col);
                if(other.isAdjacentTo(selected)) {
                    /* WRITE THIS METHOD
                     * 
                     * 
                     * 
                     */
                    change(other, selected);
                    selected = null;
                }
                else
                    selected = other;
            }
        }
    }
    
    /* Adds candies to matched (candies that need to be removed. */
    public void addToMatched (Candy candy) {
        if(candy != null)
            matched.add(candy);
    }
    
    public void removeFromMatched() {
        for(int i = matched.size() - 1; i >= 0; i--)
            matched.remove(i);
        }
    
    /* Sets up the screen by putting a new candy at each location. */
    public void initialize() {
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                /* Sets a random color candy at a location. */
                Location loc = new Location(row, col);
                int color = (int)(Math.random() * Candy.NUM_COLORS);
                Candy candy = new Candy(color, loc, candyScreen, this);
                
                /* Removes whatever was there before and adds a candy and button. */
                candyScreen.remove(loc);
                candyScreen.put(loc, candy);
                updateButton(loc);
            }
        }
        /* WRITE THIS METHOD:
         * 
         * 
         * 
         */
        
        /* Searches for matched candy from a preset screen. */
        searchMatch();
    }
    
    /* Puts a button beneath each candy. */
    public void makeButtons(){
        for (int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                /* Put a new, randomized candy in each location on the screen. */
                Location loc = new Location(row, col);
                int color = (int)(Math.random() * Candy.NUM_COLORS);
                Candy candy = new Candy(color, loc, candyScreen, this);
                candyScreen.put(loc, candy);
                
                /* Set up botton to be put in a location. */
                JButton button = new JButton();
                button.setIcon(candy.getImage());
                button.setBackground(Color.WHITE);
                
                /* Actually put botton there. */
                buttonScreen.put(loc, button);
                buttons.add(button);
                button.addActionListener(this);
            }
        }
    }
    
    public void change(Location one, Location two) {
        /* Swap the colors of the two candies. */
        String temp = candyScreen.get(one).getColor();
        candyScreen.get(one).getColor(candyScreen.get(two).getColor());
        candyScreen.get(one).setColor(temp);
        
        /* Update buttons of two new ones. */
        updateButton(one);
        updateButton(two);
        
        /*
         * 
         * DO WE NEED CHANGE()? 
         * (used if nothing happens, reverses actions above)
         * 
         * 
         */
    }
    
    public void updateButton(Location loc) {
        Candy candy = candyScreen.get(candy);
        if(candy == null)
            buttonScreen.get(loc).setIcon(null);
        else {
            buttonScreen.get(loc).setIcon(candy.getIcon());
        }
    }
    
    public void searchMatch() {
        
    }
    
    /* do we need append() or addToSet()?? */
    
    
    
    public void crushCandy() {
        
    }
}